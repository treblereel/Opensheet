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
package org.opensheet.mvc;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.opensheet.server.dao.HourDAO;
import org.opensheet.server.dao.UserDAO;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;




@Controller
@RequestMapping("/timesheettoprinter.htm")
public class TimesheetToPrinterController{
 
	@Autowired
	private HourDAO hourDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView timesheetToPrinter(@RequestParam("user_id") Integer user_id,
			@RequestParam("month") Integer month,
			@RequestParam("year") Integer year){
 
		Calendar cal = Calendar.getInstance();
		cal.set(year,month,1);
		User user = userDAO.getUser(user_id);
		Map<Assignment, ArrayList<Integer>> hoursByDays = hourDAO.getHoursUserHourByAssignmentByMonth(user, cal);
		
		
		ModelAndView model = new ModelAndView("timesheettoprinter");
		model.addObject("hoursByDays", hoursByDays);
		model.addObject("user", user);
		model.addObject("calendar", cal);
		return model;
	}
}
