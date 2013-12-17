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

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TimelineServiceAsync {
	public void getAssignmentTimeline(Integer assignmentId,AsyncCallback<Integer> callback);
	public void setAssignmentTimeline(Integer assignmentId,Integer timeline,Boolean allocate,AsyncCallback<BaseModel> callback);
	public void getAssignmentDepartmentTimeline(Integer assignmentId,Integer departmentId,AsyncCallback<Integer> callback);
	public void getAssignmentDepartmentDetail(Integer assignmentId,Integer departmentId,AsyncCallback<BaseModel> callback);
	public void setAssignmentDepartmentTimeline(Integer assignmentId,Integer departmentId,Integer timeline,Boolean allocate,AsyncCallback<BaseModel> callback);
	public void setUserAssignmentTimeline(Integer userId,Integer assignmentId,Integer timeline,AsyncCallback<Void> callback);
	public void setUserAssignmentRate(Integer userId,Integer assignmentId,Integer rate,AsyncCallback<Void> callback);
	public void setUserAssignmentTimelineAndRate(Integer userId,Integer assignmentId,Integer timeline,Integer rate,AsyncCallback<Void> callback);
	public void getUserInternalRate(Integer userId,AsyncCallback<Integer> callback);
	public void recalculateInternalRate(Integer userId,Integer rate,Date start,Date end,AsyncCallback<Void> callback);

}
