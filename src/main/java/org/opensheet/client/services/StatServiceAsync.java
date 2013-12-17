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
import java.util.List;


import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StatServiceAsync {
	public void getAssignemntStatByYear(int year,int assignemntId,AsyncCallback<List<Number>> callback);
	public void getAssignemntStatByPeriod(Date start,Date end,int assignemntId,AsyncCallback<List<BaseModel>> callback);
	public void getAssignemntDepartmentStatByYear(int year,int assignemntId,int departmentId,AsyncCallback<List<Number>> callback);
	public void getAssignmentQuickDetails(int assignmentId,AsyncCallback<BaseModel> callback);
	public void getAssignmentDepartmentDetail(int assignmentId,int departmentId,AsyncCallback<BaseModel> callback);
	public void getAssignemntHoursInternalExternalRatesSummThisYear(int assignmentId,AsyncCallback<List<BaseModel>> callback);
	public void getAssignmentDepartmentUserDetail(int assignmentId,int departmentId,int userId,AsyncCallback<BaseModel> callback);

	public void getAssignmentHoursMonthThisYear(int assignmentId,AsyncCallback<List<BaseModel>> callback);
	public void getHourReportByUserByPeriodByAssignmentByBranch(Integer assignmentId,Integer branch,Integer type,Date start,Date end,AsyncCallback<List<BaseModel>> callback);

    public void getUsersByDepartmentAndByAssignmentWithStats(Integer departmentId,Integer assignmentId,AsyncCallback<List<BaseModel>> callback);

}
