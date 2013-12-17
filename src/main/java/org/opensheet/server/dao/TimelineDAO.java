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
package org.opensheet.server.dao;

import org.opensheet.client.exceptions.ParentTimelineIsNullException;
import org.opensheet.client.exceptions.ParentTimelineTooSmallException;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.AssignmentDepartmentTimeline;
import org.opensheet.shared.model.AssignmentTimeline;
import org.opensheet.shared.model.AssignmentUserDetail;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.User;

public interface TimelineDAO {

	public Assignment getAssignmentTimeline(Assignment assignment);
	public AssignmentDepartmentTimeline getAssignmentDepartmentTimeline(Assignment assignment, Department department);
	public Integer getAssignmentDepartmentTimelineSumByAssignment(Assignment assignment);
	public Assignment setAssignmentTimeline(Assignment assignment,AssignmentTimeline assignmentTimeline);
	public AssignmentUserDetail getAssignmentUserDetail(Assignment assignment,User user);
	public void setAssignmentUserDetail(AssignmentUserDetail assignmentUserDetail);
	public void setUserAssignmentTimeline(Assignment assignment,User user,Integer timeline) throws ParentTimelineIsNullException, ParentTimelineTooSmallException;
	public void setUserAssignmentRate(Assignment assignment,User user,Integer rate);
	public void setUserAssignmentTimelineAndRate(User user,Assignment assignment,Integer timeline, Integer rate);
}
