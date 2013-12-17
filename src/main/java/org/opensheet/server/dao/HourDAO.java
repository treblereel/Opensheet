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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.opensheet.client.dto.grid.AssignmentGridTemplate;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.Hour;
import org.opensheet.shared.model.User;

public interface HourDAO {

	public void setOrUpdateHour(User user, Calendar date, Integer oldValue,Integer newValue, Integer assignmentId);
	public Hour getHour(User user,Assignment assignment,Calendar date);
	public List<Hour> getHours(User user,Assignment assignment,Calendar cal);

	public List<Hour> getHoursSumm(User user,Calendar cal);
	public List<Hour> getHoursSumByUser(User user,Calendar cal);
	public Map<Assignment,ArrayList<Integer>> getHoursUserHourByAssignmentByMonth(User user,Calendar cal);
	public Map<User,List<Hour>>  getHours(Assignment assignment, Department department);
	public Map<User,List<Hour>>  getHours(Assignment assignment, Department department,Date starting,Date finishing);
	public String getNote(Integer user,Integer assignment,Date date);
	public void setNote(Integer user,Integer assignment,Date date,String note);
	public void recalculateInternalRate(Integer userId, Integer rate,Date start, Date end);
	
	public AssignmentGridTemplate getHoursByUserAndByAssignmentOnPeriod(Integer userId,Date start,Date end);

}
