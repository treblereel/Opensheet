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

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.opensheet.client.exceptions.ParentTimelineIsNullException;
import org.opensheet.client.exceptions.ParentTimelineTooSmallException;
import org.opensheet.server.dao.DepartmentDAO;
import org.opensheet.server.dao.TimelineDAO;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.AssignmentDepartmentTimeline;
import org.opensheet.shared.model.AssignmentTimeline;
import org.opensheet.shared.model.AssignmentUserDetail;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class TimelineDAOImpl implements TimelineDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private DepartmentDAO departmentDAO;

	@Override
	public	Assignment getAssignmentTimeline(Assignment assignment){
		return null;
	}

	@Override
	public Assignment setAssignmentTimeline(Assignment assignment,	AssignmentTimeline assignmentTimeline) {
		
		
		
		return null;
	}

	@Override
	@Transactional
	public AssignmentDepartmentTimeline getAssignmentDepartmentTimeline(
			Assignment assignment, Department department) {
		
		AssignmentDepartmentTimeline assignmentDepartmentTimeline = (AssignmentDepartmentTimeline)
				sessionFactory.getCurrentSession().createQuery("FROM AssignmentDepartmentTimeline adt" +
						" where adt.assignment=? and adt.department=?").setEntity(0, assignment)
						.setEntity(1, department)
						.uniqueResult();
		
		return assignmentDepartmentTimeline;
	}

	@Override
	@Transactional
	public Integer getAssignmentDepartmentTimelineSumByAssignment(Assignment assignment) {
		Long sum = (Long) sessionFactory.getCurrentSession().createQuery("SELECT SUM(adt.hour) FROM AssignmentDepartmentTimeline adt where adt.assignment=? ")
		.setEntity(0, assignment)
		.uniqueResult();
		if(sum == null){
			return 0;
		}
		return sum.intValue();
		
	}

	@Override
	@Transactional
	public AssignmentUserDetail getAssignmentUserDetail(Assignment assignment,
			User user) {
		AssignmentUserDetail assignmentUserDetail = (AssignmentUserDetail) sessionFactory
				.getCurrentSession().createQuery("from AssignmentUserDetail as aud where" +
				" aud.assignment=? and aud.user=? ").setEntity(0, assignment)
													.setEntity(1, user)
													.uniqueResult();
		
		return assignmentUserDetail;
	}

	@Override
	@Transactional
	public void setAssignmentUserDetail(AssignmentUserDetail assignmentUserDetail) {
		sessionFactory.getCurrentSession().saveOrUpdate(assignmentUserDetail);
	}

	@Override
	@Transactional
	public  void setUserAssignmentTimeline(Assignment assignment, User user,
			Integer timeline) throws ParentTimelineIsNullException, ParentTimelineTooSmallException {
		Department department = user.getDepartment();
		AssignmentDepartmentTimeline assignmentDepartmentTimeline= this.getAssignmentDepartmentTimeline(assignment, department);
		Integer currentAssignmentTimeline = 0;
		
		if(assignmentDepartmentTimeline == null){
			throw new ParentTimelineIsNullException("Assignment/Department Timeline is not set");
		}
		Integer timelineMax = assignmentDepartmentTimeline.getHour();
		List<User> users = departmentDAO.getUserByAssignmentAndByDepartment(assignment, department);
		for(User u: users) {
			if(u != user){
				AssignmentUserDetail aud = getAssignmentUserDetail(assignment, u);
				if(aud != null)
					currentAssignmentTimeline = currentAssignmentTimeline+ aud.getTimeline();
			}
		}
		
		if(timelineMax < (currentAssignmentTimeline + timeline)){
			throw new ParentTimelineTooSmallException("You r trying to allocate too large timeline");
		}else{
		AssignmentUserDetail details = getAssignmentUserDetail(assignment, user);
		if(details == null){
			details = new AssignmentUserDetail();
			details.setAssignment(assignment);
			details.setUser(user);
			details.setStarted(new Date());
			details.setRate(0);
		}
		details.setTimeline(timeline);
		details.setUpdated(new Date());
		sessionFactory.getCurrentSession().saveOrUpdate(details);
		}

	}

	@Override
	@Transactional
	public  void setUserAssignmentRate(Assignment assignment, User user,Integer rate) {
		AssignmentUserDetail details = getAssignmentUserDetail(assignment, user);
		if(details == null){
			details = new AssignmentUserDetail();
			details.setAssignment(assignment);
			details.setUser(user);
			details.setStarted(new Date());
			details.setTimeline(0);
		}
		details.setRate(rate);
		details.setUpdated(new Date());
		sessionFactory.getCurrentSession().saveOrUpdate(details);
	}

	@Override
	@Transactional
	public void setUserAssignmentTimelineAndRate(User user,Assignment assignment, Integer timeline, Integer rate) {
		AssignmentUserDetail details = getAssignmentUserDetail(assignment, user);
		if(details == null){
			details = new AssignmentUserDetail();
			details.setAssignment(assignment);
			details.setUser(user);
			details.setStarted(new Date());
			
		}
		details.setRate(rate);
		details.setTimeline(timeline);
		details.setUpdated(new Date());
		sessionFactory.getCurrentSession().saveOrUpdate(details);
		
	}
	
	

}
