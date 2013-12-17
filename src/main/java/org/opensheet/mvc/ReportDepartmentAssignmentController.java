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

import java.util.List;
import java.util.Map;

import org.opensheet.server.dao.DepartmentDAO;
import org.opensheet.server.dao.ReportDAO;

import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Department;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/reportdepartmentassignmenttoxls.htm")
public class ReportDepartmentAssignmentController{
    Logger logger = LoggerFactory.getLogger(ReportDepartmentAssignmentController.class);

	
	@Autowired
	private ReportDAO reportDAO;
	
	
	

	@RequestMapping(method = RequestMethod.GET)
	protected ModelAndView handleRequestInternal(@RequestParam("branch") Integer branch,
			@RequestParam("type") Integer typeId) throws Exception {
		Map<Department, List<Assignment>> answer = reportDAO.getAssignmentsByDepartmentAndByType(branch, typeId);
			if(answer !=null){
				return new ModelAndView("reportdepartmentassignmenttoxlsview","data",answer);
			}else{
				return null;
			}
		}	
	}
