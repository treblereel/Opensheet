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

import java.util.Date;
import org.hibernate.SessionFactory;
import org.opensheet.client.exceptions.ParentTimelineIsNullException;
import org.opensheet.client.exceptions.ParentTimelineTooSmallException;
import org.opensheet.client.exceptions.TimelineSmallerWhanSumChildException;
import org.opensheet.client.services.TimelineService;
import org.opensheet.server.dao.AssignmentDAO;
import org.opensheet.server.dao.DepartmentDAO;
import org.opensheet.server.dao.HourDAO;
import org.opensheet.server.dao.StatDAO;
import org.opensheet.server.dao.TimelineDAO;
import org.opensheet.server.dao.UserDAO;
import org.opensheet.server.security.CheckUserContext;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.AssignmentDepartmentTimeline;
import org.opensheet.shared.model.AssignmentUserDetail;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**TODO: rewrite class. move to DAO
 * 
 * 
 * @author chani
 *
 */

@Service("TimelineService")
public class TimelineServiceImpl extends RemoteServiceServlet implements TimelineService{

	
	private static final long serialVersionUID = 1L;

	@Autowired 	private AssignmentDAO assignmentDAO;
	@Autowired 	private DepartmentDAO departmentDAO;
	@Autowired 	private UserDAO userDAO;
	@Autowired  private TimelineDAO   timelineDAO;
	@Autowired	private StatDAO 	   statDAO;
	@Autowired	private HourDAO 	   hourDAO;
	
	@Autowired	private SessionFactory sessionFactory;
	
	@Autowired 	private CheckUserContext checkUserContext;
	
	@Override
	@Transactional
	public Integer getAssignmentTimeline(Integer assignmentId) {
		return assignmentDAO.getAssignmentById(assignmentId).getTimeline().getHour();
	}

	
	
	
	@Override
	@Transactional
	public BaseModel setAssignmentTimeline(Integer assignmentId, Integer timeline,Boolean allocate) throws ParentTimelineTooSmallException, ParentTimelineIsNullException, TimelineSmallerWhanSumChildException {
		
		Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
		Integer timelineSumm = 0;
		
		if(assignment.getLevel() == 0){
			for(Assignment a: assignment.getChildren()){
				if(a.getTimeline().getHour() !=null){
						timelineSumm = timelineSumm + a.getTimeline().getHour(); 
				}
			}
			
			if(timeline < timelineSumm)
			{
				throw new TimelineSmallerWhanSumChildException("Timeline of Assignemnt is smaller whan Sum of subassignment's timeline. Shrink them first!"   +     "Root timeline :" + timeline + ", subassignemnt's  timeline sum :" + timelineSumm);
			}
		}else{
			
			
			for(Assignment a: assignment.getParent().getChildren()){
				if(a.getTimeline().getHour() != null){
					if(!a.equals(assignment)){
						timelineSumm = timelineSumm + a.getTimeline().getHour(); 
					}
				}
			}	
				
			timelineSumm = timelineSumm + timeline;
	
				
			if(timelineSumm > assignment.getParent().getTimeline().getHour()){
				if(allocate == true){

					assignment.getTimeline().setHour(timeline);
					assignment.getTimeline().setDate(new Date());
					assignment.getTimeline().setUser(checkUserContext.getUser(SecurityContextHolder.getContext()));
					sessionFactory.getCurrentSession().saveOrUpdate(assignment);
					
					
					Assignment parent = assignment.getParent();
					parent.getTimeline().setHour(timelineSumm);
					parent.getTimeline().setDate(new Date());
					parent.getTimeline().setUser(checkUserContext.getUser(SecurityContextHolder.getContext()));
					sessionFactory.getCurrentSession().saveOrUpdate(parent);
					
					
					BaseModel bm = new BaseModel();
					bm.set("type", "success");
					bm.set("string", "OK");
					return bm;
					
				}else{
					throw new ParentTimelineTooSmallException("Unables allocate timeline for "  + assignment.getName() + " coz sum is bigger than parent's timeline.<br> Details: new timeline = " + timeline + ", but Sum =  " + timelineSumm + ".");
				}	
			}else if(assignment.getParent().getTimeline().getHour() == null){
				throw new ParentTimelineIsNullException("PARENT TIMELINE is Null, allocate " + assignment.getParent().getName() + " timeline ");
			}
		}	
		
		
		
		
		assignment.getTimeline().setHour(timeline);
		assignment.getTimeline().setDate(new Date());
		assignment.getTimeline().setUser(checkUserContext.getUser(SecurityContextHolder.getContext()));
		sessionFactory.getCurrentSession().saveOrUpdate(assignment);
		
		BaseModel bm = new BaseModel();
		bm.set("type", "success");
		bm.set("string", "OK");
		
		return bm;
	}




	@Override
	@Transactional
	public Integer getAssignmentDepartmentTimeline(Integer assignmentId, Integer departmentId) {
		Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
		Department department = departmentDAO.getDepartmentById(departmentId);
		AssignmentDepartmentTimeline assignmentDepartmentTimeline = timelineDAO.getAssignmentDepartmentTimeline(assignment, department);
		if(assignmentDepartmentTimeline == null){
			return 0;
		}else{
			return assignmentDepartmentTimeline.getHour();
		}
	}




	@Override
	@Transactional
	public BaseModel setAssignmentDepartmentTimeline(Integer assignmentId,Integer departmentId, Integer timeline, Boolean allocate) throws ParentTimelineIsNullException, ParentTimelineTooSmallException {
		Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
		Department department = departmentDAO.getDepartmentById(departmentId);
		AssignmentDepartmentTimeline assignmentDepartmentTimeline = timelineDAO.getAssignmentDepartmentTimeline(assignment, department);
		
		if(assignmentDepartmentTimeline == null){
			assignmentDepartmentTimeline = new AssignmentDepartmentTimeline();
			assignmentDepartmentTimeline.setAssignment(assignment);
			assignmentDepartmentTimeline.setDepartment(department);
			assignmentDepartmentTimeline.setDate(new Date());
			assignmentDepartmentTimeline.setHour(0);
			assignmentDepartmentTimeline.setUser(checkUserContext.getUser(SecurityContextHolder.getContext()));

		}
		
		Integer currentDepartmentTimeline =  assignmentDepartmentTimeline.getHour();
		Integer maxTimeline = 0;
		Integer sumOfAllocatedTimelinesForCurrentDep=0;
		
	
		
		if(assignment.getLevel() == 0){
			if(assignment.getTimeline().getHour() == null){
				throw new ParentTimelineIsNullException("Please set Timeline to " + assignment.getName()  +  " first");
				
			}else{
				maxTimeline = assignment.getTimeline().getHour();
				Integer currentTimelinesSum = timelineDAO.getAssignmentDepartmentTimelineSumByAssignment( assignment);
						
						
						
						
				if(currentTimelinesSum !=null){
					if((currentTimelinesSum-currentDepartmentTimeline+timeline) > maxTimeline){
						throw new ParentTimelineTooSmallException("You trying to allocate too much hours! Expand  `" + assignment.getName()  +  "` hours first");
					}else{
						assignmentDepartmentTimeline.setDate(new Date());
						assignmentDepartmentTimeline.setHour(timeline);
						assignmentDepartmentTimeline.setUser(checkUserContext.getUser(SecurityContextHolder.getContext()));
						sessionFactory.getCurrentSession().saveOrUpdate(assignmentDepartmentTimeline);
						
						BaseModel answer = new  BaseModel();
						answer.set("type", "ok");
						return answer;
					}
				}else{
					if(timeline > maxTimeline){
						throw new ParentTimelineTooSmallException("You trying to allocate too much hours! Expand  `" + assignment.getName()  +  "` hours first");
					}else{
						assignmentDepartmentTimeline.setDate(new Date());
						assignmentDepartmentTimeline.setHour(timeline);
						assignmentDepartmentTimeline.setUser(checkUserContext.getUser(SecurityContextHolder.getContext()));
						sessionFactory.getCurrentSession().saveOrUpdate(assignmentDepartmentTimeline);
						BaseModel answer = new  BaseModel();
						answer.set("type", "ok");
						return answer;
						
					}
				}
			}
			}else{
			for(Assignment a: assignment.getParent().getChildren()){
				if(getAssignmentDepartmentTimeline(a.getId(),departmentId) !=null)
					sumOfAllocatedTimelinesForCurrentDep += getAssignmentDepartmentTimeline(a.getId(),departmentId);
			}
			
			//получаем сумму тс любого отделя для этой подзадачи
			Integer currentTimelinesAssignmentSum = timelineDAO.getAssignmentDepartmentTimelineSumByAssignment( assignment);

			// проверяем что сумма тс этого отдела для подпроектов меньше или равна назначеной тс для этого отдела родительсктго проекта
			if((sumOfAllocatedTimelinesForCurrentDep-currentDepartmentTimeline+timeline) > 	getAssignmentDepartmentTimeline(assignment.getParent().getId(),departmentId)
				|| 	(currentTimelinesAssignmentSum-currentDepartmentTimeline+timeline) > assignment.getTimeline().getHour()
				){
				throw new ParentTimelineTooSmallException("You trying to allocate too much hours! Expand  Timeline of " + department.getName() +   " for  `" + assignment.getName()  +  "` first");
			}else{
				assignmentDepartmentTimeline.setDate(new Date());
				assignmentDepartmentTimeline.setHour(timeline);
				assignmentDepartmentTimeline.setUser(checkUserContext.getUser(SecurityContextHolder.getContext()));
				sessionFactory.getCurrentSession().saveOrUpdate(assignmentDepartmentTimeline);
				
				BaseModel answer = new  BaseModel();
				answer.set("type", "ok");
				return answer;
			}
			
		}
	}




	@Override
	@Transactional
	public BaseModel getAssignmentDepartmentDetail(Integer assignmentId,Integer departmentId) {
		BaseModel baseModel = new BaseModel();
		baseModel.set("timeline", getAssignmentDepartmentTimeline(assignmentId,departmentId));
		baseModel.set("sum", statDAO.getAssignmentDepartmentSum(assignmentId, departmentId));
		return baseModel;
	}




	@Override
	@Transactional
	public void setUserAssignmentTimeline(Integer userId,Integer assignmentId, Integer timeline) throws ParentTimelineIsNullException, ParentTimelineTooSmallException {
		User user = userDAO.getUser(userId);
		Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
		timelineDAO.setUserAssignmentTimeline(assignment, user, timeline);
	}




	@Override
	@Transactional
	public void setUserAssignmentRate(Integer userId,Integer assignmentId, Integer rate) {
			User user = userDAO.getUser(userId);
			Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
			timelineDAO.setUserAssignmentRate(assignment, user, rate);
		}




	@Override
	public void setUserAssignmentTimelineAndRate(Integer userId,
			Integer assignmentId, Integer timeline, Integer rate) {
		User user = userDAO.getUser(userId);
		Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
		timelineDAO.setUserAssignmentTimelineAndRate(user,assignment, timeline,rate);
		
	}




	@Override
	@Transactional
	public Integer getUserInternalRate(Integer userId) {
		User user = userDAO.getUser(userId);
		return user.getUserRate().getInternalRate();
	}




	@Override
	public void recalculateInternalRate(Integer userId, Integer rate,
			Date start, Date end) {
		hourDAO.recalculateInternalRate(userId, rate, start, end);
		
		
	}





	

	

}
