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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.opensheet.server.dao.AssignmentUserDAO;
import org.opensheet.server.dao.DepartmentDAO;
import org.opensheet.server.dao.HolidaysDAO;
import org.opensheet.server.dao.HourDAO;
import org.opensheet.server.dao.UserDAO;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Hour;
import org.opensheet.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/timesheettoxls.htm")
public class TimesheetToXlsController{
	private Calendar cal;
	
	
	@Autowired
	private HourDAO hourDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private HolidaysDAO holidays;

	@Autowired
	private AssignmentUserDAO  assignmentUserDAO;
	
	@Autowired
	private DepartmentDAO departmentDAO;
	
	@RequestMapping(method = RequestMethod.GET)
	protected ModelAndView handleRequestInternal(@RequestParam("user_id") Integer user_id,
			@RequestParam("month") Integer month,@RequestParam("year") Integer year) throws Exception {
		
		
		Map<Assignment, List<Assignment>> map = null;
		Map<Assignment,List<Integer>> result = new LinkedHashMap<Assignment,List<Integer>>();
		cal = Calendar.getInstance();
		cal.set(year,month,1);
		
		
		HashMap<Integer, Boolean> getHolidaysMap = holidays.getHolidays(cal); 	
		
		User user = (User) userDAO.getUser(user_id);   
		User userContext = (User) userDAO.whoIam(); 

		if(!userContext.getPermission().getAdmin() == true){
			if(!user.equals(userContext)){

				if(userContext.getPermission().getDm() == true){
					List<User> departmentManagerUsers = departmentDAO.getDepartmentManagerUsers(userContext);
					if(!departmentManagerUsers.contains(user)){
						return null;
					}
				}
				
			}
		}

		
			map = assignmentUserDAO.getSheetAssignmentByUser(user, cal);
			Iterator<Assignment> iterator = map.keySet().iterator();
			while(iterator.hasNext()){
				Assignment a = (Assignment) iterator.next();
 		        result.put(a, makeHourList(user,a));
 		       	if(map.get(a).size() >1){
			     	for(Assignment as: map.get(a)){
			      	 	result.put(as, makeHourList(user,as));
			       	}
			    }
			}
		
			List<Hour> hoursSummList = hourDAO.getHoursSumm(user,cal);
			Assignment hoursSummListAssignment = new Assignment();
			hoursSummListAssignment.setName("Result");
			hoursSummListAssignment.setLevel(0);
		
		
			
			List<Hour> overtimeList = new ArrayList<Hour>(hoursSummList.size());
			for(Hour h: hoursSummList){
				if(getHolidaysMap.get(h.getDate().getDate())){
						Hour temp = new Hour();
						temp.setHour(h.getHour());
						temp.setDate(h.getDate());
						overtimeList.add(temp);
				}else if(h.getHour() > 8 ){
					
					Hour temp = new Hour();
					temp.setHour(h.getHour()-8);
					temp.setDate(h.getDate());
					overtimeList.add(temp);
					
				}
			}
			Assignment hoursOvertimeListAssignment = new Assignment();
			hoursOvertimeListAssignment.setName("Overtime");
			hoursOvertimeListAssignment.setLevel(0);
			
			result.put(hoursOvertimeListAssignment, makeHourSummList(overtimeList ));
			result.put(hoursSummListAssignment, makeHourSummList(hoursSummList));
			
			
			ModelAndView model = new ModelAndView("timesheettoxlsview","Hours",result);
			model.addObject("holidays", getHolidaysMap);
			
		return model;
 
		}	
	
	private List<Integer> freshList(Calendar cal){
		int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		max++;
		List<Integer> hours = new ArrayList<Integer>(max);
		for(int i=1;i<=max; i++){
			hours.add(0);
		}
		
		return hours;
	}
	
	
	@SuppressWarnings("deprecation")
	private List<Integer> makeHourList(User user,Assignment a){
		int sum = 0;
		List<Integer> list = this.freshList(this.cal);
    	for(Hour h: hourDAO.getHours(user,a,cal)){
    		list.remove(h.getDate().getDate());
    		list.add(h.getDate().getDate(), h.getHour());
    		sum = sum + h.getHour();
    	}
    	list.remove(0);
    	list.add(0, sum);
		
		return list;
	}
	
	@SuppressWarnings("deprecation")
	private List<Integer> makeHourSummList(List<Hour> hoursSummList){
		int sum = 0;
		List<Integer> list = this.freshList(this.cal);
    	for(Hour h: hoursSummList){
    		list.remove(h.getDate().getDate());
    		list.add(h.getDate().getDate(), h.getHour());
    		sum = sum + h.getHour();
    	}
    	list.remove(0);
    	list.add(0, sum);
		
		return list;
	}
	
}
