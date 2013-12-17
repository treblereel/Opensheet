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
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service/StatService")
public interface StatService extends RemoteService {

	public List<Number> getAssignemntStatByYear(int year,int assignmentId);
	public List<BaseModel> getAssignemntStatByPeriod(Date start,Date end,int assignmentId);
	public List<Number> getAssignemntDepartmentStatByYear(int year,int assignmentId,int departmentId);
	public BaseModel    getAssignmentQuickDetails(int assignmentId);
	public BaseModel    getAssignmentDepartmentDetail(int assignmentId,int departmentId);
	public BaseModel    getAssignmentDepartmentUserDetail(int assignmentId,int departmentId,int userId);
	public List<BaseModel> getAssignemntHoursInternalExternalRatesSummThisYear(int assignmentId);
	public List<BaseModel> getAssignmentHoursMonthThisYear(int assignmentId);
	
    public List<BaseModel> getUsersByDepartmentAndByAssignmentWithStats(Integer departmentId,Integer assignmentId);

	
	public List<BaseModel> getHourReportByUserByPeriodByAssignmentByBranch(Integer assignmentId,Integer branch,Integer type,Date start,Date end);

}
