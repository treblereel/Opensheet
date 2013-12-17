/*******************************************************************************
 * Copyright (c) 2012 Dmitry Tikhomirov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Dmitry Tikhomirov - initial API and implementation
 ******************************************************************************/
package org.opensheet.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.opensheet.client.dto.UserDTO;
import org.opensheet.client.dto.grid.AssignmentGridTemplate;
import org.opensheet.client.dto.grid.HourFolder;
import org.opensheet.client.exceptions.EditDateIsOverException;
import org.opensheet.client.exceptions.PermissionDeniedException;
import org.opensheet.client.services.HourService;
import org.opensheet.server.dao.AssignmentDAO;
import org.opensheet.server.dao.AssignmentUserDAO;
import org.opensheet.server.dao.DepartmentDAO;
import org.opensheet.server.dao.HolidaysDAO;
import org.opensheet.server.dao.HourDAO;
import org.opensheet.server.dao.SettingsDAO;
import org.opensheet.server.dao.UserDAO;
import org.opensheet.server.utils.Comparators;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.Hour;
import org.opensheet.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@Service("HourService")
public class HourServiceImpl extends RemoteServiceServlet implements
		HourService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2478861970876403071L;

	@Autowired
	private HolidaysDAO holidays;
	@Autowired
	private HourDAO hourDAO;
	@Autowired
	private AssignmentDAO assignmentDAO;
	@Autowired
	private AssignmentUserDAO assignmentUserDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private DepartmentDAO departmentDAO;
	@Autowired
	private SettingsDAO settingsDAO;

	@SuppressWarnings({ "deprecation" })
	@Override
	@Transactional
	public BaseTreeModel getHours(UserDTO userDTO, Date date) {
		Map<Assignment, List<Assignment>> map = null;

		HashMap<Boolean, Date> timeSheetInputMode = settingsDAO
				.getTimeSheetInputMode();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		HashMap<Integer, Boolean> getHolidaysMap = holidays.getHolidays(cal);

		User user = (User) userDAO.getUser(userDTO.getId()); // sessionFactory.getCurrentSession().get(User.class,
																// new
																// Integer(userDTO.getId()));
		User userContext = (User) userDAO.whoIam(); // sessionFactory.getCurrentSession().get(User.class,
													// customUser.getId());

		if (!userContext.getPermission().getAdmin() == true) {
			if (!user.equals(userContext)) {

				if (userContext.getPermission().getDm() == true) {
					List<User> departmentManagerUsers = departmentDAO
							.getDepartmentManagerUsers(userContext);
					if (!departmentManagerUsers.contains(user)) {
						return null;
					}

				}

			}
		}

		HourFolder root = new HourFolder("root");
		if (userContext.getPermission().getAdmin() == true) {
			map = assignmentUserDAO.getMergedAssignmentsByUser(user, cal);
		} else if (date.getMonth() == new Date().getMonth()
				&& date.getYear() == new Date().getYear()) {
			map = assignmentUserDAO.getMergedAssignmentsByUser(user, cal);// .getSheetAssignmentByUser(user,cal);
		} else if (timeSheetInputMode.containsKey(true)
				&& (new Date().getMonth() - date.getMonth()) == 1) {

			int results = timeSheetInputMode.get(true).compareTo(date);
			if (results < 0) {
				map = assignmentUserDAO.getSheetAssignmentByUser(user, cal);

			} else {
				map = assignmentUserDAO.getMergedAssignmentsByUser(user, cal);
			}
		} else {
			map = assignmentUserDAO.getSheetAssignmentByUser(user, cal);
		}

		map = sortByValue(map);

		Iterator<Assignment> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			Assignment a = (Assignment) iterator.next();
			List<BaseTreeModel> bmt = new ArrayList<BaseTreeModel>(map.size());
			if (map.get(a).size() > 1) {
				for (Assignment as : map.get(a)) {
					bmt.add(new HourFolder(as.getName(), as.getId(), as
							.getType(), hourDAO.getHours(user, as, cal)));
				}
			} else if (map.get(a).size() == 1) {
				Assignment as = map.get(a).get(0);
				if (as.getFirst() != true) {
					bmt.add(new HourFolder(as.getName(), as.getId(), as
							.getType(), hourDAO.getHours(user, as, cal)));
				}
			}

			root.add((HourFolder) new HourFolder(a.getName(), a.getId(), a
					.getType(), hourDAO.getHours(user, a, cal), bmt));
		}

		List<Hour> hoursSummList = hourDAO.getHoursSumm(user, cal);
		List<Hour> overtimeList = new ArrayList<Hour>(hoursSummList.size());
		for (Hour h : hoursSummList) {
			if (getHolidaysMap.get(h.getDate().getDate())) {
				Hour temp = new Hour();
				temp.setHour(h.getHour());
				temp.setDate(h.getDate());
				overtimeList.add(temp);
			} else if (h.getHour() > 8) {

				Hour temp = new Hour();
				temp.setHour(h.getHour() - 8);
				temp.setDate(h.getDate());
				overtimeList.add(temp);

			}
		}

		root.add((HourFolder) new HourFolder("Overtime", 909999998, 4,
				overtimeList));
		root.add((HourFolder) new HourFolder("Result", 909999999, 5,
				hoursSummList));
		return root;

	}

	@Override
	public HashMap<Integer, Boolean> getHolidays(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return holidays.getHolidays(cal);
	}

	@SuppressWarnings("deprecation")
	@Override
	@Transactional
	public void setOrUpdateHour(UserDTO userDTO, Date date, Integer oldValue,
			Integer newValue, Integer assignmentId)
			throws EditDateIsOverException, PermissionDeniedException {
		User user = userDAO.getUser(userDTO.getId());

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		HashMap<Boolean, Date> timeSheetInputMode = settingsDAO
				.getTimeSheetInputMode();
		User thisUser = userDAO.whoIam();

		if (thisUser.getPermission().getAdmin() == true) {
			hourDAO.setOrUpdateHour(user, cal, oldValue, newValue, assignmentId);
		} else if (timeSheetInputMode.containsKey(true)
				&& user.equals(thisUser)) {
			Date borderDate = timeSheetInputMode.get(true);

			if (date.getMonth() == new Date().getMonth()
					&& date.getYear() == new Date().getYear()) {
				hourDAO.setOrUpdateHour(user, cal, oldValue, newValue,
						assignmentId);
			} else if (date.getMonth() <= borderDate.getMonth()
					&& date.getYear() <= borderDate.getYear()) {
				hourDAO.setOrUpdateHour(user, cal, oldValue, newValue,
						assignmentId);
			} else {
				throw new EditDateIsOverException(
						"You can Add/Edit data coz border date is gone : "
								+ borderDate);
			}
		} else if (user.equals(thisUser)
				&& date.getMonth() == new Date().getMonth()
				&& date.getYear() == new Date().getYear()) {
			hourDAO.setOrUpdateHour(user, cal, oldValue, newValue, assignmentId);
		} else {
			throw new PermissionDeniedException("Permission Denied");
		}

	}

	@Override
	public BaseTreeModel getHours(Integer assignmentId, Integer departmentId) {
		Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
		Department department = departmentDAO.getDepartmentById(departmentId);
		BaseTreeModel root = new BaseTreeModel();
		root.set("name", "root");
		root.set("id", 99999999);

		Random randomGenerator = new Random();
		Map<User, List<Hour>> map = hourDAO.getHours(assignment, department);

		for (Map.Entry<User, List<Hour>> m : map.entrySet()) {
			List<Hour> list = m.getValue();
			Hour rootHour = list.get(0);

			BaseTreeModel b = new BaseTreeModel();
			b.set("name", m.getKey().getFullName());
			b.set("id", randomGenerator.nextInt(100000));
			b.set("assignment", rootHour.getAssignment().getName());
			b.set("hour", rootHour.getHour());
			b.set("inratesum", rootHour.getInratesum());
			b.set("extratesum", rootHour.getExtratesum());
			list.remove(0);

			if (assignment.getLevel() == 0) {
				for (Hour h : list) {
					// if(!h.getAssignment().getFirst()){
					BaseTreeModel child = new BaseTreeModel();
					child.set("name", m.getKey().getFullName());
					child.set("id", randomGenerator.nextInt(100000));
					child.set("assignment", h.getAssignment().getName());
					child.set("hour", h.getHour());
					child.set("inratesum", h.getInratesum());
					child.set("extratesum", h.getExtratesum());
					b.add(child);
					// }
				}
			}

			root.add(b);
		}

		return root;
	}

	@Override
	public String getNote(Integer user, Integer assignment, Date date) {
		return hourDAO.getNote(user, assignment, date);
	}

	@Override
	public void setNote(Integer user, Integer assignment, Date date, String note) {
		hourDAO.setNote(user, assignment, date, note);
	}

	@Override
	public BaseTreeModel getHours(Integer assignmentId, Integer departmentId,
			Date starting, Date finishing) {

		Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
		Department department = departmentDAO.getDepartmentById(departmentId);
		BaseTreeModel root = new BaseTreeModel();
		root.set("name", "root");
		root.set("id", 99999999);

		Random randomGenerator = new Random();
		Map<User, List<Hour>> map = hourDAO.getHours(assignment, department,
				starting, finishing);

		for (Map.Entry<User, List<Hour>> m : map.entrySet()) {
			List<Hour> list = m.getValue();
			Hour rootHour = list.get(0);

			BaseTreeModel b = new BaseTreeModel();
			b.set("name", m.getKey().getFullName());
			b.set("id", randomGenerator.nextInt(100000));
			b.set("assignment", rootHour.getAssignment().getName());
			b.set("hour", rootHour.getHour());
			b.set("inratesum", rootHour.getInratesum());
			b.set("extratesum", rootHour.getExtratesum());
			list.remove(0);

			if (assignment.getLevel() == 0) {
				for (Hour h : list) {
					BaseTreeModel child = new BaseTreeModel();
					child.set("name", " ");
					child.set("id", randomGenerator.nextInt(100000));
					child.set("assignment", h.getAssignment().getName());
					child.set("hour", h.getHour());
					child.set("inratesum", h.getInratesum());
					child.set("extratesum", h.getExtratesum());
					b.add(child);

				}
			}
			root.add(b);
		}
		return root;
	}

	static Map<Assignment, List<Assignment>> sortByValue(
			Map<Assignment, List<Assignment>> map) {
		List<Assignment> list = new LinkedList<Assignment>(map.keySet());

		Collections.sort(list, new Comparator<Assignment>() {
			public int compare(Assignment o1, Assignment o2) {
				if (o1.getType() == o2.getType()) {
					return o1.getName().compareTo(o2.getName());
				}
				return o1.getType().compareTo(o2.getType());
			}
		});

		Map<Assignment, List<Assignment>> result = new LinkedHashMap<Assignment, List<Assignment>>();
		for (Iterator<Assignment> it = list.iterator(); it.hasNext();) {
			Assignment entry = (Assignment) it.next();
			List<Assignment> assignments = map.get(entry);
			Collections.sort(assignments, Comparators.ComparatorAssignmentName);
			result.put(entry, map.get(entry));
		}
		return result;
	}

	@Secured({ "ROLE_ADMIN" })
	@Override
	public AssignmentGridTemplate getHoursByUserAndByAssignmentOnPeriod(
			Integer userId, Date start, Date end) {
		return hourDAO
				.getHoursByUserAndByAssignmentOnPeriod(userId, start, end);
	}

}
