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
import java.util.Date;
import java.util.List;

import org.opensheet.client.services.StatService;
import org.opensheet.server.dao.AssignmentDAO;
import org.opensheet.server.dao.DepartmentDAO;
import org.opensheet.server.dao.StatDAO;
import org.opensheet.server.dao.TimelineDAO;
import org.opensheet.server.dao.UserDAO;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.AssignmentDepartmentTimeline;
import org.opensheet.shared.model.AssignmentUserDetail;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@Service("StatService")
public class StatServiceImpl extends RemoteServiceServlet implements StatService{

	
	@Autowired  private StatDAO statDAO;
	@Autowired	private AssignmentDAO assignmentDAO;
	@Autowired  private DepartmentDAO departmentDAO;
	@Autowired  private TimelineDAO timelineDAO;
	@Autowired  private UserDAO userDAO;
	
	private static final long serialVersionUID = 1L;

	
	
	@Override
	public List<Number> getAssignemntStatByYear(int year,int assignemntId) {
		return statDAO.getAssignmentSumByMonthByYear(year, assignemntId);
	}



	@Override
	public List<Number> getAssignemntDepartmentStatByYear(int year,	int assignemntId, int departmentId) {
		return  statDAO.getAssignmentDepartmentSumByMonthByYear(year, assignemntId, departmentId);
	}



	@Override
	public BaseModel getAssignmentQuickDetails(int assignemntId) {
		Assignment assignment = assignmentDAO.getAssignmentById(assignemntId);
		return statDAO.getAssignmentQuickDetails(assignment);
	}



	@Override
	public List<BaseModel> getAssignemntStatByPeriod(Date start, Date end,	int assignemntId)
	{
		return statDAO.getAssignmentStatByPeriod(start, end, assignemntId);
	
	}



	@Override
	public List<BaseModel> getAssignemntHoursInternalExternalRatesSummThisYear(int assignmentId) {
		return	statDAO.getAssignmentHoursInternalExternalRatesSummThisYear(assignmentId);
	}



	@Override
	public List<BaseModel> getAssignmentHoursMonthThisYear(int assignmentId) {
		
		
		return statDAO.getAssignmentHoursMonthThisYear(assignmentId);
	}


	/**@param assignmentId set assignment, 9999999 - for every assignment
	 * @param branch
	 * @param type
	 * 
	 * 
	 */
	@Override
	public List<BaseModel> getHourReportByUserByPeriodByAssignmentByBranch(Integer assignmentId,Integer branch,Integer type,Date start,Date end) {
		return statDAO.getHourReportByUserByPeriodByAssignmentByTypeByBranch(assignmentId,branch, type, start, end);

	}



	@Override
	public BaseModel getAssignmentDepartmentDetail(int assignmentId,int departmentId) {
		Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
		Department department = departmentDAO.getDepartmentById(departmentId);
		BaseModel answer = new BaseModel();
		answer.set("name", assignment.getName());
		answer.set("owner", assignment.getOwner().getFullName());
		AssignmentDepartmentTimeline timeline = timelineDAO.getAssignmentDepartmentTimeline(assignment, department);
		
		if(timeline == null){
			answer.set("timeline", 0);
		}else{
			answer.set("timeline", timeline.getHour());
		}
		
		Calendar starting = Calendar.getInstance();
		starting.set(Calendar.HOUR_OF_DAY, 0);
		starting.set(Calendar.MINUTE, 0);
		starting.set(Calendar.DATE, 1);
		
		Calendar ending = Calendar.getInstance();
		ending.set(Calendar.DATE, ending.getActualMaximum(Calendar.DAY_OF_MONTH));
		ending.set(Calendar.HOUR_OF_DAY, 23);
		ending.set(Calendar.MINUTE, 59);
		
		Integer intratesum 		= statDAO.getInternalRateSumByAssignmentAndByDepartment(assignment, department);
		Integer intratesumMonth = statDAO.getInternalRateSumByAssignmentAndByDepartmentOnPeriod(assignment, department, starting, ending);
		Integer extraratesum 	= statDAO.getExternalRateSumByAssignmentAndByDepartment(assignment, department);
		Integer extratesumMonth = statDAO.getExternalRateSumByAssignmentAndByDepartmentOnPeriod(assignment, department, starting, ending);

		
		
		Integer sum = statDAO.getAssignmentDepartmentSum(assignmentId, departmentId);
		answer.set("sum", sum);
		
		
		answer.set("intratesum", intratesum);
		answer.set("intratesumMonth", intratesumMonth);
		answer.set("extraratesum", extraratesum);
		answer.set("extratesumMonth", extratesumMonth);
		
		
		Integer sumMonth = statDAO.getHourSumByAssignmentAndByDepartmentOnPeriod(assignment, department, starting, ending);
		answer.set("sum_month", sumMonth);
		
		return answer;
	}


	/**MOVE TO TIMELINE
	 * 
	 * 
	 */
	@Override
	@Transactional
	public BaseModel getAssignmentDepartmentUserDetail(int assignmentId,
			int departmentId, int userId) {
		Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
		User	   user		  = userDAO.getUser(userId);
		
		Calendar starting = Calendar.getInstance();
		starting.set(Calendar.HOUR_OF_DAY, 0);
		starting.set(Calendar.MINUTE, 0);
		starting.set(Calendar.DATE, 1);
		
		Calendar ending = Calendar.getInstance();
		ending.set(Calendar.DATE, ending.getActualMaximum(Calendar.DAY_OF_MONTH));
		ending.set(Calendar.HOUR_OF_DAY, 23);
		ending.set(Calendar.MINUTE, 59);
		
		AssignmentUserDetail timeline = timelineDAO.getAssignmentUserDetail(assignment, user);
		BaseModel answer = new BaseModel();
		Integer sum = statDAO.getHourSumByAssignmentAndByUser(assignment, user);
		Integer sumMonth = statDAO.getHourSumByAssignmentAndByUserOnPeriod(assignment, user, starting, ending);
		
		answer.set("sum", sum );
		answer.set("sum_month", sumMonth);
		answer.set("intra_rate", user.getUserRate().getInternalRate());

		if(timeline != null){
			answer.set("timeline",   timeline.getTimeline());
			answer.set("extra_rate", timeline.getRate());
		}else{
			answer.set("timeline",  0);
			answer.set("extra_rate",0);
		}
		
		return answer;
	}


	/**for department user/assignment grid
	 * 
	 */
	@Override
	@Transactional	
	public List<BaseModel> getUsersByDepartmentAndByAssignmentWithStats(
			Integer departmentId, Integer assignmentId) {
		 Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
		 Department department = departmentDAO.getDepartmentById(departmentId);	
		List<User> users = departmentDAO.getUserByAssignmentAndByDepartment(assignment, department);
		List<BaseModel> result  = new ArrayList<BaseModel>(users.size());
		for(User u: users){
			BaseModel bm = new BaseModel();
			bm.set("name", u.getFullName());
			bm.set("id", u.getId());
			
			AssignmentUserDetail timeline = timelineDAO.getAssignmentUserDetail(assignment, u);
			Integer sum = statDAO.getHourSumByAssignmentAndByUser(assignment, u);
			
			bm.set("sum", sum );
			bm.set("internalrate", u.getUserRate().getInternalRate());

			if(timeline != null){
				bm.set("timeline",   timeline.getTimeline());
				bm.set("externalrate", timeline.getRate());
			}else{
				bm.set("timeline",  0);
				bm.set("externalrate",0);
			}
			
			result.add(bm);
		}
		
			
	
	
		return result;
	}




}
