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

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.Hour;
import org.opensheet.shared.model.User;

public interface ReportDAO {
	
	public Map<Department,List<Assignment>>  getAssignmentsByDepartmentAndByType(Integer branch, Integer typeId);
	public Map<String,List<Hour>> getHourAndUsernameAndAssignmentnameByDepartmentOnPeriod(Department department,Date started,Date finished);

}
