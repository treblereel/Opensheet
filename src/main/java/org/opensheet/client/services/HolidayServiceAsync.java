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


import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HolidayServiceAsync {

	public void getHolidays(Date date,AsyncCallback<List<BaseModelData>> callback);
	public void setAndUpdateHolidays(Date date,Boolean status,AsyncCallback<List<BaseModelData>> callback);
}
