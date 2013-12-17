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
import java.util.HashMap;

import org.opensheet.shared.model.Holiday;

public interface HolidaysDAO {

	
	public HashMap<Integer, Boolean> getHolidays(Calendar c);
	public Boolean checkDay(Calendar c);
	public void saveOrUpdateDay(Calendar c,Boolean status);
	public Holiday getDay(Calendar c);
	public HashMap<Integer,Holiday> getHolidayMap(Calendar c);
	
}
