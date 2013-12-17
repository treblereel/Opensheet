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
package org.opensheet.server.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.opensheet.server.dao.DepartmentDAO;
import org.opensheet.server.dao.ReportDAO;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.Hour;
import org.opensheet.shared.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.core.java.util.Collections;

@Repository
public class ReportDAOImpl implements ReportDAO{

	
	
	@Autowired
	private DepartmentDAO departmentDAO;
	
	@Autowired
	private SessionFactory sessionFactory;
	
    Logger logger = LoggerFactory.getLogger(ReportDAOImpl.class);

	
	@Override
	@Transactional
	public Map<Department, List<Assignment>> getAssignmentsByDepartmentAndByType(
			Integer branch, Integer typeId) {
		
		Map<Department, List<Assignment>>  answer = new HashMap<Department, List<Assignment>>();
		List<Department> departments = departmentDAO.listDepartmentByBranch("1", branch);
		
		
		for(Department department: departments){
			List<Assignment> assignments = department.getAssignments();
			for(Assignment assignment: assignments){
				   if(assignment.getType() == typeId && assignment.getStatus() == true){
					   Hibernate.initialize(assignment.getOwner());
					   if(answer.containsKey(department)){
						   answer.get(department).add(assignment); 
					   }else{
						   List<Assignment> list = new ArrayList<Assignment>();
						   list.add(assignment);
						   answer.put(department,list);
					   }
				   }
			   }
			
		}
		   return  answer;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Map<String,List<Hour>> getHourAndUsernameAndAssignmentnameByDepartmentOnPeriod(
			Department department, Date started, Date finished) {
		
		Map<String,List<Hour>> result = new LinkedMap();
		
		List<Object[]> list = (List<Object[]>) sessionFactory.getCurrentSession()
				.createQuery("select sum(h.hour),h.user.secondName,h.user.firstName,h.assignment.name," +
						     "sum(h.inratesum),sum(h.extratesum),h.branch.name,h.department.name,h.assignment.id, " +
						     "h.user.id  from Hour h" +
						     " where h.date >= ? and h.date <= ? and h.department.id=? group by h.user,h.assignment" +
						     " order by h.user.secondName,h.assignment.type,h.assignment.name")
						     .setDate(0, started).setDate(1, finished).setInteger(2, department.getId()).list();
			for(Object[] row: list){
			
				
				
			String name = row[1] + " " + row[2];
			
			
			if(result.containsKey(name)){
				Hour hour = new Hour();
				hour.setHour(Integer.parseInt(row[0].toString()));
				Assignment assignment = new Assignment();
				assignment.setId(Integer.parseInt(row[8].toString()));
				assignment.setName(row[3].toString());
				User user = new User();
				user.setId(Integer.parseInt(row[9].toString()));
				user.setFirstName(row[2].toString());
				user.setSecondName(row[1].toString());
				hour.setUser(user);
				hour.setAssignment(assignment);
				result.get(name).add(hour);
			}else{
				List<Hour> hourList = new ArrayList<Hour>();
				Hour hour = new Hour();
				hour.setHour(Integer.parseInt(row[0].toString()));
				User user = new User();
				user.setId(Integer.parseInt(row[9].toString()));
				user.setFirstName(row[2].toString());
				user.setSecondName(row[1].toString());
				hour.setUser(user);
				Assignment assignment = new Assignment();
				assignment.setId(Integer.parseInt(row[8].toString()));
				assignment.setName(row[3].toString());
				hour.setAssignment(assignment);
				hourList.add(hour);
				result.put(name, hourList);
			}
		}
		
		return result;
	}
}
