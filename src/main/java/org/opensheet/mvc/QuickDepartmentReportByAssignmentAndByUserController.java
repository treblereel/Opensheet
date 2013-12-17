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
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.hibernate.Hibernate;
import org.opensheet.server.dao.AssignmentDAO;
import org.opensheet.server.dao.DepartmentDAO;
import org.opensheet.server.dao.ReportDAO;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.Hour;
import org.opensheet.shared.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping("/quickdepartmentreportbyassignmentandbyuser.htm")
public class QuickDepartmentReportByAssignmentAndByUserController {
	 Logger logger = LoggerFactory.getLogger(ReportDepartmentAssignmentController.class);

		
		@Autowired
		private ReportDAO reportDAO;
		
		@Autowired
		private AssignmentDAO assignmentDAO;
		
		@Autowired
		private DepartmentDAO departmentDAO;

		
		@SuppressWarnings("deprecation")
		@RequestMapping(method = RequestMethod.GET)
		protected ModelAndView handleRequestInternal(
				@RequestParam("s_year")  Integer s_year,
				@RequestParam("s_month") Integer s_month,
				@RequestParam("s_day")   Integer s_day,
				@RequestParam("e_year")  Integer e_year,
				@RequestParam("e_month") Integer e_month,
				@RequestParam("e_day")   Integer e_day,
				@RequestParam("department")  Integer departmentId
				) throws Exception {
			
			
			Date start =  new Date();
			Date end   =  new Date();


			start.setYear(s_year);
			start.setMonth(s_month);
			start.setDate(s_day);
			
			end.setYear(e_year);
			end.setMonth(e_month);
			end.setDate(e_day);
			
			
			Department department = departmentDAO.getDepartmentById(departmentId);
			List<User> users = departmentDAO.getUsers(department);
			Map<Integer,Assignment> assignments = new HashMap<Integer,Assignment>();
			
			Map<Integer,Integer> usersRates = new HashMap<Integer,Integer>();
			
			Map<Integer,Map<Integer,List<Hour>>> mapByType = new LinkedHashMap<Integer,Map<Integer,List<Hour>>>();
			Map<String,List<Hour>> hourMap  =   reportDAO.getHourAndUsernameAndAssignmentnameByDepartmentOnPeriod(department, start, end);
			
			for(Map.Entry<String,List<Hour>> kv: hourMap.entrySet()){
				List<Hour> list = kv.getValue();
				for(Hour h: list){
					Assignment a = assignmentDAO.getAssignmentById(h.getAssignment().getId());
					assignments.put(a.getId(), a);
					
					if(mapByType.containsKey(a.getType())){
						if(mapByType.get(a.getType()).containsKey(a.getId())){
							mapByType.get(a.getType()).get(a.getId()).add(h);
						}else{
							List<Hour> temp = new ArrayList<Hour>();
							temp.add(h);
							mapByType.get(a.getType()).put(a.getId(),temp);
						}
					}else{
						List<Hour> temp = new ArrayList<Hour>();
						temp.add(h);
						Map<Integer,List<Hour>> map = new LinkedHashMap<Integer,List<Hour>>();
						map.put(a.getId(), temp);
						mapByType.put(a.getType(), map);
					}
				}
			}
			
			for(User user: users){
				if(user.getStatus() == true){
					if(!hourMap.containsKey(user.getSecondName() + " " + user.getFirstName())){
						hourMap.put(user.getSecondName() + " " + user.getFirstName(), new ArrayList<Hour>());
						
					}
				
				}
				System.out.println("& " + user.getFullName() + " " + user.getId());

				usersRates.put(user.getId(),user.getUserRate().getInternalRate());
			}
			
			ModelAndView model = new ModelAndView("quickdepartmentreportbyassignmentandbyuserview");
			model.addObject("hourMap", hourMap);
			model.addObject("mapByType",mapByType);
			model.addObject("Assignments",assignments);
			model.addObject("UsersRates", usersRates);
			return model;
			}	
		}
