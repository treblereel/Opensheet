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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.User;

import com.extjs.gxt.ui.client.data.BaseModel;

public interface StatDAO {

	public List<Number> getAssignmentSumByMonthByYear(int year, int assignemntId);
	public List<BaseModel> getAssignmentStatByPeriod(Date start,Date end,int assignemntId);
	public List<Number> getAssignmentDepartmentSumByMonthByYear(int year, int assignemntId,int departmentId);
	public Integer getAssignmentDepartmentSum(int assignemntId,int departmentId);
	public Integer getHourSumByAssignmentAndByDepartmentOnPeriod(Assignment assignment, Department department,Calendar starting,Calendar ending);
	public BaseModel getAssignmentQuickDetails(Assignment assignment);
	public List<BaseModel> getAssignmentHoursInternalExternalRatesSummThisYear(int assignmentId);
	public List<BaseModel> getAssignmentHoursMonthThisYear(int assignmentId);
	public Integer getHourSumByAssignmentAndByUser(Assignment assignment,User user);
	public Integer getHourSumByAssignmentAndByUserOnPeriod(Assignment assignment,User user,Calendar starting,Calendar ending);

	public Integer getInternalRateSumByAssignmentAndByDepartment(Assignment assignment,Department department);
	public Integer getInternalRateSumByAssignmentAndByDepartmentOnPeriod(Assignment assignment,Department department,Calendar starting,Calendar ending);
	public Integer getExternalRateSumByAssignmentAndByDepartment(Assignment assignment,Department department);
	public Integer getExternalRateSumByAssignmentAndByDepartmentOnPeriod(Assignment assignment,Department department,Calendar starting,Calendar ending);

	public Integer getInternalRateSumByAssignmentAndByDepartmentAndByUser(Assignment assignment,Department department,User user);
	public Integer getExternalRateSumByAssignmentAndByDepartmentAndByUser(Assignment assignment,Department department,User user);

	
	
	public List<BaseModel> getHourReportByUserByPeriodByTypeByBranch(Integer branch,Integer type,Date start,Date end);
	public List<BaseModel> getHourReportByUserByPeriodByAssignmentByTypeByBranch(Integer assignment, Integer branch,Integer type,Date start,Date end);

	
}
