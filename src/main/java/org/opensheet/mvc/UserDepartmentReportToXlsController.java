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

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.opensheet.server.dao.AssignmentDAO;
import org.opensheet.server.dao.DepartmentDAO;
import org.opensheet.server.dao.HourDAO;
import org.opensheet.server.dao.StatDAO;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.Hour;
import org.opensheet.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.extjs.gxt.ui.client.data.BaseModel;




@Controller
@RequestMapping("/departmentreporttoxls.htm")
public class UserDepartmentReportToXlsController {
	@Autowired
	private HourDAO hourDAO;
	
	@Autowired
	private AssignmentDAO  assignmentDAO;
	
	@Autowired
	private DepartmentDAO  departmentDAO;
	
	
	@SuppressWarnings("deprecation")
	@RequestMapping(method = RequestMethod.GET)
	protected ModelAndView handleRequestInternal(
			@RequestParam("s_year")  Integer s_year,
			@RequestParam("s_month") Integer s_month,
			@RequestParam("s_day")   Integer s_day,
			@RequestParam("e_year")  Integer e_year,
			@RequestParam("e_month") Integer e_month,
			@RequestParam("e_day")   Integer e_day,
			@RequestParam("assignment")  Integer assignmentId,
			@RequestParam("department")    Integer departmentId) throws Exception {
		
		Date start =  new Date();
		Date end   =  new Date();


		start.setYear(s_year);
		start.setMonth(s_month);
		start.setDate(s_day);
		
		end.setYear(e_year);
		end.setMonth(e_month);
		end.setDate(e_day);
		
		Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
		Department department = departmentDAO.getDepartmentById(departmentId);
		
		Map<User, List<Hour>>  map = hourDAO.getHours(assignment, department,start,end);
		
		ModelAndView modelAndView = new ModelAndView("departmentreporttoxlsview","Hours",map);
		modelAndView.addObject("assignment", assignment);
		
		return  modelAndView;
	}

}
