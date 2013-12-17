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
package org.opensheet.server.dao.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.opensheet.client.dto.grid.AssignmentGridTemplate;
import org.opensheet.server.dao.AssignmentDAO;
import org.opensheet.server.dao.HourDAO;
import org.opensheet.server.dao.UserDAO;
import org.opensheet.server.utils.Comparators;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.AssignmentUserDetail;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.Hour;
import org.opensheet.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class HourDAOImpl implements HourDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private AssignmentDAO assignmentDAO;

	@Autowired
	private UserDAO userDAO;

	@Override
	@Transactional
	public void setOrUpdateHour(User user, Calendar date, Integer oldValue,
			Integer newValue, Integer assignmentId) {

		Assignment assignment = (Assignment) sessionFactory.getCurrentSession()
				.get(Assignment.class, new Integer(assignmentId));
		AssignmentUserDetail assignmentUserDetail = userDAO
				.getUserAssignmentDetail(user, assignment);
		Hour hour = getHour(user, assignment, date);

		if (hour == null && newValue != null) {
			Hour h = new Hour();
			h.setDate(date.getTime());
			h.setStarted(new Date());
			h.setUpdated(new Date());
			h.setHour(newValue);
			h.setUser(user);
			h.setBranch(user.getBranch());
			h.setDepartment(user.getDepartment());
			h.setInratesum(newValue * user.getUserRate().getInternalRate());
			if (assignmentUserDetail != null) {
				if (assignmentUserDetail.getRate() == null) {
					h.setExtratesum(0);
				} else {
					h.setExtratesum(newValue * assignmentUserDetail.getRate());
				}
			} else {
				h.setExtratesum(0);
			}
			if (assignment.getLevel() == 0) {

				Assignment a = (Assignment) sessionFactory
						.getCurrentSession()
						.createQuery(
								"from Assignment a where a.parent = ? and a.first = 1 ")
						.setInteger(0, assignment.getId()).uniqueResult();

				h.setAssignment(assignment);
				h.setTask(a);

			} else if (assignment.getLevel() == 1) {
				h.setAssignment(assignment.getParent());
				h.setTask(assignment);

			}

			sessionFactory.getCurrentSession().save(h);

		} else {
			if (newValue == 0 || newValue == null) {
				sessionFactory.getCurrentSession().delete(hour);
			} else {
				hour.setInratesum(newValue
						* user.getUserRate().getInternalRate());
				if (assignmentUserDetail != null) {
					if (assignmentUserDetail.getRate() == null) {
						hour.setExtratesum(0);
					} else {
						hour.setExtratesum(newValue
								* assignmentUserDetail.getRate());
					}
				} else {
					hour.setExtratesum(0);
				}
				hour.setHour(newValue);
				hour.setUpdated(new Date());
				sessionFactory.getCurrentSession().save(hour);
			}
		}

	}

	@Override
	@Transactional
	public Hour getHour(User user, Assignment assignment, Calendar date) {
		Hour hour = null;
		if (assignment.getLevel() == 1) {
			hour = (Hour) sessionFactory
					.getCurrentSession()
					.createQuery(
							"from Hour h where h.date = ? and h.assignment.id =? and h.user.id = ? and h.task = ?")
					.setDate(0, date.getTime())
					.setInteger(1, assignment.getParent().getId())
					.setInteger(2, user.getId())
					.setInteger(3, assignment.getId()).uniqueResult();
		} else if (assignment.getLevel() == 0) {

			Assignment defaultTask = assignmentDAO
					.getAssignemntsDefaultTask(assignment);

			hour = (Hour) sessionFactory
					.getCurrentSession()
					.createQuery(
							"from Hour h where h.date = ? and h.assignment.id =? and h.user.id = ? and h.task = ?")
					.setDate(0, date.getTime())
					.setInteger(1, assignment.getId())
					.setInteger(2, user.getId())
					.setInteger(3, defaultTask.getId()).uniqueResult();

		}

		return hour;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Hour> getHours(User u, Assignment assignment, Calendar cal) {

		Integer maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		Calendar firstDay = (Calendar) cal.clone();
		firstDay.set(Calendar.DATE, 1);
		Calendar lastDay = (Calendar) cal.clone();
		lastDay.set(Calendar.DATE, maxDays);

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		List<Hour> hours = new ArrayList<Hour>(maxDays);
		List<Hour> ihours = null;

		if (assignment.getLevel() == 1) {
			ihours = (List<Hour>) sessionFactory
					.getCurrentSession()
					.createQuery(
							"from Hour h where h.date >= ? and h.date <= ? and h.assignment.id =? and h.user.id = ? and h.task = ?")
					.setDate(0, firstDay.getTime())
					.setDate(1, lastDay.getTime())
					.setInteger(2, assignment.getParent().getId())
					.setInteger(3, u.getId()).setInteger(4, assignment.getId())
					.list();

		} else if (assignment.getLevel() == 0) {
			List<Object[]> hs = (List<Object[]>) sessionFactory
					.getCurrentSession()
					.createQuery(
							"select sum(h.hour),"
									+ "sum(h.inratesum),sum(h.extratesum),h.date from Hour h where h.date >= ? and h.date <= ?"
									+ " and h.assignment =? and h.user = ? group by h.date")
					.

					setDate(0, firstDay.getTime())
					.setDate(1, lastDay.getTime())
					.setInteger(2, assignment.getId()).setInteger(3, u.getId())
					.list();

			ihours = new ArrayList<Hour>(hs.size());

			for (Object[] o : hs) {

				Hour h = new Hour();
				h.setAssignment(assignment);
				h.setUser(u);

				try {
					h.setDate((Date) formatter.parse(o[3].toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}

				h.setBranch(u.getBranch());
				h.setDepartment(u.getDepartment());
				h.setHour(Integer.parseInt(o[0].toString()));
				h.setInratesum(Integer.parseInt(o[1].toString()));
				h.setExtratesum(Integer.parseInt(o[2].toString()));
				ihours.add(h);
			}

		}

		if (ihours != null) {
			return ihours;
		}

		return hours;
	}

	/**
	 * Shitty name, rename it after, get hours sum by assignment
	 * 
	 * 
	 */

	@Override
	@Transactional
	public List<Hour> getHoursSumm(User user, Calendar cal) {

		Integer maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		Calendar firstDay = (Calendar) cal.clone();
		firstDay.set(Calendar.DATE, 1);
		Calendar lastDay = (Calendar) cal.clone();
		lastDay.set(Calendar.DATE, maxDays);

		@SuppressWarnings("unchecked")
		List<Hour> hoursSumm = (List<Hour>) sessionFactory
				.getCurrentSession()
				.createSQLQuery(
						"Select h.assignment,h.date,sum(h.hour) as hour,h.branch,(FLOOR( 1 + RAND( ) *600000 )) as id,(FLOOR( 1 + RAND( ) *600000 )) as note,started,updated,(FLOOR( 1 + RAND( ) *600000 )) as task,(FLOOR( 1 + RAND( ) *600000 )) as department,sum(h.inratesum) as inratesum,sum(h.extratesum) as extratesum,person from hour h where h.date >= ? and h.date <= ? and h.person = ? group by h.date")
				.addEntity(Hour.class).setDate(0, firstDay.getTime())
				.setDate(1, lastDay.getTime()).setInteger(2, user.getId())
				.list();

		return hoursSumm;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Hour> getHoursSumByUser(User user, Calendar cal) {

		Integer maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		Calendar firstDay = (Calendar) cal.clone();
		firstDay.set(Calendar.DATE, 1);
		Calendar lastDay = (Calendar) cal.clone();
		lastDay.set(Calendar.DATE, maxDays);

		List<Hour> hours = (List<Hour>) sessionFactory
				.getCurrentSession()
				.createSQLQuery(
						"Select h.assignment,sum(h.hour) as hour, ? as date,h.branch,(FLOOR( 1 + RAND( ) *600000 )) as id,(FLOOR( 1 + RAND( ) *600000 )) as note,started,updated,(FLOOR( 1 + RAND( ) *600000 )) as task,sum(h.inratesum) as inratesum,sum(h.extratesum) as extratesum,(FLOOR( 1 + RAND( ) *600000 )) as department,person from hour h where h.date >= ? and h.date <= ? and h.person = ? group by h.assignment")
				.

				addEntity(Hour.class).setDate(0, firstDay.getTime())
				.setDate(1, firstDay.getTime()).setDate(2, lastDay.getTime())
				.setInteger(3, user.getId()).list();

		for (Hour h : hours)
			Hibernate.initialize(h.getAssignment());

		Collections.sort(hours, Comparators.ComparatorHoursByAssignmentName);
		return hours;
	}

	@SuppressWarnings("deprecation")
	@Override
	@Transactional
	public Map<Assignment, ArrayList<Integer>> getHoursUserHourByAssignmentByMonth(
			User user, Calendar cal) {

		Integer maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		Calendar firstDay = (Calendar) cal.clone();
		firstDay.set(Calendar.DATE, 1);
		Calendar lastDay = (Calendar) cal.clone();
		lastDay.set(Calendar.DATE, maxDays);

		@SuppressWarnings("unchecked")
		List<Hour> hoursByDays = (List<Hour>) sessionFactory
				.getCurrentSession()
				.createSQLQuery(
						"Select h.assignment,h.date,sum(h.hour) as hour,h.branch,(FLOOR( 1 + RAND( ) *600000 )) as id,(FLOOR( 1 + RAND( ) *600000 )) as note,started,updated,(FLOOR( 1 + RAND( ) *600000 )) as task,sum(h.inratesum) as inratesum,sum(h.extratesum) as extratesum,(FLOOR( 1 + RAND( ) *600000 )) as department,person from hour h where h.date >= ? and h.date <= ? and h.person = ? group by h.date,h.assignment")
				.addEntity(Hour.class).setDate(0, firstDay.getTime())
				.setDate(1, lastDay.getTime()).setInteger(2, user.getId())
				.list();

		Map<Assignment, ArrayList<Integer>> daysMap = new HashMap<Assignment, ArrayList<Integer>>();
		Integer max = maxDays;
		max++;

		for (Hour h : hoursByDays) {
			if (daysMap.containsKey(h.getAssignment())) {
				ArrayList<Integer> currentAssignment = daysMap.get(h
						.getAssignment());
				currentAssignment.set(h.getDate().getDate(), h.getHour());
				Hibernate.initialize(h.getAssignment());
				daysMap.put(h.getAssignment(), currentAssignment);
			} else {
				ArrayList<Integer> daysList = new ArrayList<Integer>();
				for (Integer day = 1; day <= max; day++)
					daysList.add(0);
				daysList.set(h.getDate().getDate(), h.getHour());
				Hibernate.initialize(h.getAssignment());
				daysMap.put(h.getAssignment(), daysList);
			}
		}

		return daysMap;
	}

	/**
	 * TODO: FIX IT
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Map<User, List<Hour>> getHours(Assignment assignment,
			Department department) {
		Random randomGenerator = new Random();
		Map<User, List<Hour>> map = new HashMap<User, List<Hour>>();
		List<Object[]> answer;
		if (assignment.getLevel() == 0) {
			answer = (List<Object[]>) sessionFactory
					.getCurrentSession()
					.createQuery(
							"select h.user.id,h.user.firstName,h.user.secondName,sum(h.hour),sum(h.inratesum),sum(h.extratesum),h.task.id,h.task.name,"
									+ "h.assignment.id,h.assignment.name,h.assignment.first  from Hour h where h.department=?"
									+ " and h.assignment=? group by h.user,h.task.name order by h.user.secondName")
					.setInteger(0, department.getId())
					.setInteger(1, assignment.getId()).list();
		} else {
			answer = (List<Object[]>) sessionFactory
					.getCurrentSession()
					.createQuery(
							"select h.user.id,h.user.firstName,h.user.secondName,sum(h.hour),sum(h.inratesum),sum(h.extratesum),h.task.id,h.task.name,"
									+ "h.task.id,h.task.name,h.task.first  from Hour h where h.department=?"
									+ " and h.task=? group by h.user,h.task.name order by h.user.secondName")
					.setInteger(0, department.getId())
					.setInteger(1, assignment.getId()).list();
		}
		for (Object[] s : answer) {
			User user = new User(Integer.parseInt(s[0].toString()));
			if (map.containsKey(user)) {
				map.get(user)
						.get(0)
						.setHour(
								map.get(user).get(0).getHour()
										+ Integer.parseInt(s[3].toString()));
				map.get(user)
						.get(0)
						.setInratesum(
								map.get(user).get(0).getInratesum()
										+ Integer.parseInt(s[4].toString()));
				map.get(user)
						.get(0)
						.setExtratesum(
								map.get(user).get(0).getExtratesum()
										+ Integer.parseInt(s[5].toString()));

				Hour hour = new Hour();
				hour.setId(randomGenerator.nextInt(100000));

				Assignment as = new Assignment();
				as.setId(Integer.parseInt(s[6].toString()));
				as.setName(s[7].toString());
				hour.setAssignment(as);
				hour.setHour(Integer.parseInt(s[3].toString()));
				hour.setExtratesum(Integer.parseInt(s[5].toString()));
				hour.setInratesum(Integer.parseInt(s[4].toString()));
				map.get(user).add(hour);
			} else {
				user.setFirstName(s[1].toString());
				user.setSecondName(s[2].toString());
				List<Hour> list = new ArrayList<Hour>();
				Hour parent = new Hour();
				parent.setId(randomGenerator.nextInt(100000));

				Assignment a = new Assignment();
				a.setId(Integer.parseInt(s[8].toString()));
				a.setName(s[9].toString());
				a.setFirst(Boolean.parseBoolean(s[10].toString()));

				parent.setAssignment(a);
				parent.setHour(Integer.parseInt(s[3].toString()));
				parent.setExtratesum(Integer.parseInt(s[5].toString()));
				parent.setInratesum(Integer.parseInt(s[4].toString()));
				list.add(0, parent);

				Hour hour = new Hour();
				hour.setId(randomGenerator.nextInt(100000));

				Assignment as = new Assignment();
				as.setId(Integer.parseInt(s[8].toString()));
				as.setName(s[9].toString());
				as.setFirst(Boolean.parseBoolean(s[10].toString()));
				hour.setAssignment(as);

				hour.setHour(Integer.parseInt(s[3].toString()));
				hour.setExtratesum(Integer.parseInt(s[5].toString()));
				hour.setInratesum(Integer.parseInt(s[4].toString()));

				list.add(hour);
				map.put(user, list);
			}
		}
		return map;
	}

	@Override
	@Transactional
	public String getNote(Integer u, Integer a, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		User user = userDAO.getUser(u);
		Assignment assignment = assignmentDAO.getAssignmentById(a);
		Hour hour = getHour(user, assignment, cal);
		return hour.getNote();
	}

	@Override
	@Transactional
	public void setNote(Integer u, Integer a, Date date, String note) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		User user = userDAO.getUser(u);
		Assignment assignment = assignmentDAO.getAssignmentById(a);
		Hour h = this.getHour(user, assignment, cal);
		if (h != null) {
			h.setNote(note);
			sessionFactory.getCurrentSession().update(h);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Map<User, List<Hour>> getHours(Assignment assignment,
			Department department, Date starting, Date finishing) {

		Random randomGenerator = new Random();
		Map<User, List<Hour>> map = new HashMap<User, List<Hour>>();
		List<Object[]> answer;
		if (assignment.getLevel() == 0) {
			answer = (List<Object[]>) sessionFactory
					.getCurrentSession()
					.createQuery(
							"select h.user.id,h.user.firstName,h.user.secondName,sum(h.hour),sum(h.inratesum),sum(h.extratesum),h.task.id,h.task.name,"
									+ "h.assignment.id,h.assignment.name,h.assignment.first  from Hour h where h.department=?"
									+ " and h.assignment=? and h.date >= ? and h.date <= ? group by h.user,h.task.name order by h.user.secondName")
					.setInteger(0, department.getId())
					.setInteger(1, assignment.getId()).setDate(2, starting)
					.setDate(3, finishing).list();
		} else {
			answer = (List<Object[]>) sessionFactory
					.getCurrentSession()
					.createQuery(
							"select h.user.id,h.user.firstName,h.user.secondName,sum(h.hour),sum(h.inratesum),sum(h.extratesum),h.task.id,h.task.name,"
									+ "h.task.id,h.task.name,h.task.first  from Hour h where h.department=?"
									+ " and h.task=? and h.date >= ? and h.date <= ? group by h.user,h.task.name order by h.user.secondName")
					.setInteger(0, department.getId())
					.setInteger(1, assignment.getId()).setDate(2, starting)
					.setDate(3, finishing).list();
		}

		for (Object[] s : answer) {
			User user = new User(Integer.parseInt(s[0].toString()));
			if (map.containsKey(user)) {
				map.get(user)
						.get(0)
						.setHour(
								map.get(user).get(0).getHour()
										+ Integer.parseInt(s[3].toString()));
				map.get(user)
						.get(0)
						.setInratesum(
								map.get(user).get(0).getInratesum()
										+ Integer.parseInt(s[4].toString()));
				map.get(user)
						.get(0)
						.setExtratesum(
								map.get(user).get(0).getExtratesum()
										+ Integer.parseInt(s[5].toString()));

				Hour hour = new Hour();
				hour.setId(randomGenerator.nextInt(100000));

				Assignment as = new Assignment();
				as.setId(Integer.parseInt(s[6].toString()));
				as.setName(s[7].toString());
				hour.setAssignment(as);
				hour.setHour(Integer.parseInt(s[3].toString()));
				hour.setExtratesum(Integer.parseInt(s[5].toString()));
				hour.setInratesum(Integer.parseInt(s[4].toString()));
				map.get(user).add(hour);
			} else {
				user.setFirstName(s[1].toString());
				user.setSecondName(s[2].toString());
				List<Hour> list = new ArrayList<Hour>();
				Hour parent = new Hour();
				parent.setId(randomGenerator.nextInt(100000));

				Assignment a = new Assignment();
				a.setId(Integer.parseInt(s[8].toString()));
				a.setName(s[9].toString());
				a.setFirst(Boolean.parseBoolean(s[10].toString()));

				parent.setAssignment(a);
				parent.setHour(Integer.parseInt(s[3].toString()));
				parent.setExtratesum(Integer.parseInt(s[5].toString()));
				parent.setInratesum(Integer.parseInt(s[4].toString()));
				list.add(0, parent);

				Hour hour = new Hour();
				hour.setId(randomGenerator.nextInt(100000));

				Assignment as = new Assignment();
				as.setId(Integer.parseInt(s[8].toString()));
				as.setName(s[9].toString());
				as.setFirst(Boolean.parseBoolean(s[10].toString()));
				hour.setAssignment(as);

				hour.setHour(Integer.parseInt(s[3].toString()));
				hour.setExtratesum(Integer.parseInt(s[5].toString()));
				hour.setInratesum(Integer.parseInt(s[4].toString()));

				list.add(hour);
				map.put(user, list);
			}
		}
		return map;
	}

	@Override
	@Transactional
	public void recalculateInternalRate(Integer userId, Integer rate,
			Date start, Date end) {
		sessionFactory
				.getCurrentSession()
				.createQuery(
						"UPDATE Hour h SET h.inratesum = h.hour*?"
								+ " WHERE h.user=? AND h.date >= ? AND h.date <= ?")
				.setInteger(0, rate).setInteger(1, userId).setDate(2, start)
				.setDate(3, end).executeUpdate();

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public AssignmentGridTemplate getHoursByUserAndByAssignmentOnPeriod(
			Integer userId, Date start, Date end) {
		AssignmentGridTemplate root = new AssignmentGridTemplate("root", true,
				100000, "nope", false, false);
		Map<Integer, AssignmentGridTemplate> map = new HashMap<Integer, AssignmentGridTemplate>();
		Map<Integer, List<AssignmentGridTemplate>> m = new HashMap<Integer, List<AssignmentGridTemplate>>();

		List<Object[]> answer = (List<Object[]>) sessionFactory
				.getCurrentSession()
				.createQuery(
						"select h.assignment.id,h.assignment.name,h.task.id,h.task.name,sum(h.hour),sum(h.inratesum),sum(h.extratesum),"
								+ "h.task.first  from Hour h where  h.user.id=? and "
								+ " h.date >= ? and h.date <= ? group by h.task")
				.setInteger(0, userId).setDate(1, start).setDate(2, end).list();

		for (Object[] obj : answer) {

			Integer assignmentId = Integer.parseInt(obj[0].toString());
			Integer taskId = Integer.parseInt(obj[2].toString());
			String assignmentName = obj[1].toString();
			String taskName = obj[3].toString();
			Integer hours = Integer.parseInt(obj[4].toString());
			Integer inRate = Integer.parseInt(obj[5].toString());
			Integer exRate = Integer.parseInt(obj[6].toString());
			Boolean first = Boolean.parseBoolean(obj[7].toString());

			if (!m.containsKey(assignmentId)) {
				AssignmentGridTemplate parent = new AssignmentGridTemplate(
						assignmentId, assignmentName, false, false, hours,
						inRate, exRate, false);

				map.put(assignmentId, parent);
				List<AssignmentGridTemplate> list = new ArrayList<AssignmentGridTemplate>();
				AssignmentGridTemplate a = new AssignmentGridTemplate(taskId,
						taskName, true, false, hours, inRate, exRate, first);
				list.add(a);
				m.put(assignmentId, list);

			} else {
				AssignmentGridTemplate temp = map.get(assignmentId);
				temp.setHours(temp.getHours() + hours);
				temp.setInrate(temp.getInrate() + inRate);
				temp.setExrate(temp.getExrate() + exRate);
				map.put(assignmentId, temp);

				AssignmentGridTemplate a = new AssignmentGridTemplate(taskId,
						taskName, true, false, hours, inRate, exRate, first);

				m.get(assignmentId).add(a);
			}
		}

		for (Map.Entry<Integer, List<AssignmentGridTemplate>> kv : m.entrySet()) {
			if (kv.getValue().size() != 1) {
				AssignmentGridTemplate a = map.get(kv.getKey());
				a.addChild(m.get(kv.getKey()));
				root.add(a);
			} else {
				root.add(map.get(kv.getKey()));
			}

		}
		return root;
	}

}
