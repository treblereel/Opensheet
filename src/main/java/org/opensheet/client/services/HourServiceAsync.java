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

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HourServiceAsync {
	
	public void getHours(UserDTO userDTO,Date date,  AsyncCallback<BaseTreeModel> callback);
	public void getHours(Integer assignment,Integer department,  AsyncCallback<BaseTreeModel> callback);
	public void getHours(Integer assignment,Integer department,Date starting,Date finishing,  AsyncCallback<BaseTreeModel> callback);

	public void getNote(Integer user,Integer assignment,Date date,AsyncCallback<String> callback);
	public void setNote(Integer user,Integer assignment,Date date,String note,AsyncCallback<Void> callback);
	
	public void getHolidays(Date date, AsyncCallback<HashMap<Integer, Boolean>> callback);
    public void setOrUpdateHour(UserDTO userDTO,Date date,Integer oldValue,Integer newValue,Integer assignmentId,AsyncCallback<Void> callback);
	public void getHoursByUserAndByAssignmentOnPeriod(Integer userId,Date start,Date end,AsyncCallback<AssignmentGridTemplate> callback);

}
