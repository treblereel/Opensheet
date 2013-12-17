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
package org.opensheet.client.services;

import java.util.Date;

import org.opensheet.client.exceptions.ParentTimelineIsNullException;
import org.opensheet.client.exceptions.ParentTimelineTooSmallException;
import org.opensheet.client.exceptions.TimelineSmallerWhanSumChildException;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("service/TimelineService")
public interface TimelineService extends RemoteService{

	public Integer getAssignmentTimeline(Integer assignmentId);
	public BaseModel setAssignmentTimeline(Integer assignmentId,Integer timeline,Boolean allocate) throws ParentTimelineTooSmallException, ParentTimelineIsNullException, TimelineSmallerWhanSumChildException;
	public Integer getAssignmentDepartmentTimeline(Integer assignmentId,Integer departmentId);
	public BaseModel getAssignmentDepartmentDetail(Integer assignmentId,Integer departmentId);
	public BaseModel setAssignmentDepartmentTimeline(Integer assignmentId,Integer departmentId,Integer timeline,Boolean allocate) throws ParentTimelineIsNullException, ParentTimelineTooSmallException;

	public void setUserAssignmentTimeline(Integer userId,Integer assignmentId,Integer timeline) throws ParentTimelineIsNullException, ParentTimelineTooSmallException;
	public void setUserAssignmentRate(Integer userId,Integer assignmentId,Integer rate);
	public void setUserAssignmentTimelineAndRate(Integer userId,Integer assignmentId,Integer timeline,Integer rate);

	public Integer getUserInternalRate(Integer userId);
	public void recalculateInternalRate(Integer userId,Integer rate,Date start,Date end);
	
}
