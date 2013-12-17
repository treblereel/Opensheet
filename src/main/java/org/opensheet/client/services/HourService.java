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
import java.util.HashMap;
import org.opensheet.client.dto.UserDTO;
import org.opensheet.client.dto.grid.AssignmentGridTemplate;
import org.opensheet.client.exceptions.EditDateIsOverException;
import org.opensheet.client.exceptions.PermissionDeniedException;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("service/HourService")
public interface HourService extends RemoteService {
	
	
	public BaseTreeModel getHours(UserDTO userDTO,Date date) throws Exception;
	public BaseTreeModel getHours(Integer assignment,Integer department);
	public BaseTreeModel getHours(Integer assignment,Integer department,Date starting,Date finishing);

	public String getNote(Integer user,Integer assignment,Date date);
	public void setNote(Integer user,Integer assignment,Date date,String note);

	
	public HashMap<Integer,Boolean>  getHolidays(Date date);
	public void setOrUpdateHour(UserDTO userDTO,Date date,Integer oldValue,Integer newValue,Integer assignmentId) throws EditDateIsOverException, PermissionDeniedException;
	public AssignmentGridTemplate getHoursByUserAndByAssignmentOnPeriod(Integer userId,Date start,Date end);
}
