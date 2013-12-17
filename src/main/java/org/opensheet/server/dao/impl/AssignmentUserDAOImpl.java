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
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.opensheet.server.dao.AssignmentDAO;
import org.opensheet.server.dao.AssignmentUserDAO;
import org.opensheet.server.dao.DepartmentDAO;
import org.opensheet.server.dao.UserDAO;
import org.opensheet.server.utils.Comparators;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.User;
import org.opensheet.shared.model.BaseModel.UserBaseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AssignmentUserDAOImpl implements AssignmentUserDAO{
	
	@Autowired
    private UserDAO userDAO;
	
	@Autowired
    private DepartmentDAO departmentDAO;
	
	@Autowired
    private AssignmentDAO assignmentDAO;
	
	@Autowired
	private SessionFactory sessionFactory;
	

	/**
	 * 
	 * parameters:
	 *  status:
	 *    true for users in assignment
	 *    false for users outside assignment
	 */     
	
	@Override
	@Transactional
	public List<User> getAssignmentUsers(Assignment a,Boolean assigned){
		
			List<User> assignedUsers;
			List<User> result = null;
			List<User> allActiveUsers = null;
		

			Assignment assignment = (Assignment) sessionFactory.getCurrentSession().get(Assignment.class, new Integer(a.getId()));
			assignedUsers = new ArrayList<User>(assignment.getUsers());
				if(assignment.getLevel() == 1){
					allActiveUsers = new ArrayList<User>(assignment.getParent().getUsers());
				}
	
	        if(assigned == true){
		  		return assignedUsers;
		  	}else{
		  		if(assignment.getLevel() == 0)      {
		  			allActiveUsers = userDAO.listUser("1");
		  		}
		  		allActiveUsers.removeAll(assignedUsers);
				result = allActiveUsers;
		  	}
	        
	      
		return result;
	}
	
	
	
	
	@Override
	@Transactional
	public void updateAssignmentUsers(List<UserBaseModel> users,Assignment a,Department d){
		if(a.getId() != null && d.getId() != null){
			
			List<User> updatedUsers = new ArrayList<User>();
			Assignment assignment = (Assignment) sessionFactory.getCurrentSession().get(Assignment.class, new Integer(a.getId()));
			Department department = (Department) sessionFactory.getCurrentSession().get(Department.class, new Integer(d.getId()));

			for(UserBaseModel u: users){
				updatedUsers.add(new User(u.getId()));
			}

				
				List<User> assignmentUsers = assignment.getUsers();
				List<User> oldUser = getAssignmentUsersByDepartment(assignment,department);
				
				assignmentUsers.removeAll(oldUser); 
				assignmentUsers.addAll(updatedUsers);
				assignment.setUsers(assignmentUsers);
			
		}
	}
			/**TODO: fix this 
			 * 
			 * 
			 */
			@Override
			@SuppressWarnings("unchecked")
			@Transactional
			public List<User> getAssignmentUsersByDepartment(Assignment assignment, Department department) {
				
				List<User>  users = (List<User>) sessionFactory.getCurrentSession().createQuery(
				"select a.users from Assignment a  where a.id=:assignmentId")
			//	.setInteger("departmentId", department.getId())
				.setInteger("assignmentId", assignment.getId())
				.list();
				List<User>  answer = new ArrayList<User>();
				for(User u: users){
					if(u.getDepartment().getId() == department.getId()){
						answer.add(u);
					}
				}
				Collections.sort(answer,Comparators.ComparatorUserName);
				return answer;
			}

	
	
	
	
			@Override
			@Transactional
			public List<User> getAssignmentUsersByDepartment(Assignment a,Department d,Boolean assigned){
				
				
				Assignment assignment =  assignmentDAO.getAssignmentById(a.getId());
				Department department = departmentDAO.getDepartmentById(d.getId());
			
				
				List<User>  indaClub = getAssignmentUsersByDepartment(assignment,department);

				if(assigned == true){
					return indaClub;
				}else{
					if(assignment.getLevel() == 0){
						List<User> departmentUsers =  userDAO.getUsersByBranchAndByDepartment("1", department.getBranch().getId(), department.getId());
						departmentUsers.removeAll(indaClub);
						return departmentUsers;
					}else{
						List<User> departmentUsers =  getAssignmentUsersByDepartment(assignment.getParent(),department);
						departmentUsers.removeAll(indaClub);
						return departmentUsers;
					}

				}

			}




			@Override
			@Transactional
			public Map<Assignment, List<Assignment>> getAssignedAssignmentByUser(User user) {
				Map<Assignment, List<Assignment>> map = new HashMap<Assignment, List<Assignment>>();
				
				for(Assignment a: user.getAssignments()){
					if(a.hasParent()){
						if(map.containsKey(a.getParent())){
							List<Assignment> l = map.get(a.getParent());
							l.add(a);
							map.put(a.getParent(),l);
						}else{
							List<Assignment> l = new ArrayList<Assignment>();
							l.add(a);
							map.put(a.getParent(), l);
						}
					}else{
						if(!map.containsKey(a)){
							List<Assignment> l = new ArrayList<Assignment>();
							map.put(a, l);
						}
					}
				}
				
	
				
				
				return map;
				
				
			}




			@SuppressWarnings("unchecked")
			@Override
			@Transactional
			public Map<Assignment, List<Assignment>> getSheetAssignmentByUser(User user, Calendar cal) {
				
				List<Assignment> listA = (ArrayList<Assignment>) sessionFactory.getCurrentSession().createQuery("select distinct h.assignment from Hour h where h.user =? and year(h.date)=? and month(h.date)=?")
				.setInteger(0, user.getId())
				.setInteger(1, cal.get(Calendar.YEAR))
				.setInteger(2, cal.get(Calendar.MONTH)+1)
				.list();
				
				//List<Assignment> listT = (ArrayList<Assignment>) sessionFactory.getCurrentSession().createQuery("select distinct h.task from Hour h where h.user =? and h.task.level=1 and h.task.first=0 and year(h.date)=? and month(h.date)=?")
				List<Assignment> listT = (ArrayList<Assignment>) sessionFactory.getCurrentSession().createQuery("select distinct h.task from Hour h where h.user =? and h.task.level=1  and year(h.date)=? and month(h.date)=?")

				.setInteger(0, user.getId())
				.setInteger(1, cal.get(Calendar.YEAR))
				.setInteger(2, cal.get(Calendar.MONTH)+1)
				.list();
				
				Map<Assignment, List<Assignment>> map = new HashMap<Assignment, List<Assignment>>();
				
				
				for(Assignment a: listA){
					List<Assignment> l = new ArrayList<Assignment>();
					map.put(a, l);
				}
				for(Assignment a: listT){
					if(map.containsKey(a.getParent())){
						List<Assignment> l = map.get(a.getParent());
						l.add(a);
						map.put(a.getParent(),l);
					}
				}


				return map;
			}




			@SuppressWarnings("rawtypes")
			@Override
			public Map<Assignment, List<Assignment>> getMergedAssignmentsByUser(User user, Calendar cal) {
				HashMap<Assignment, List<Assignment>> mapsheet = (HashMap<Assignment, List<Assignment>>) getSheetAssignmentByUser(user, cal);
				HashMap<Assignment, List<Assignment>> mapassigned = (HashMap<Assignment, List<Assignment>>) getAssignedAssignmentByUser(user);
				
				@SuppressWarnings("unchecked")
				HashMap<Assignment, List<Assignment>> map = (HashMap<Assignment, List<Assignment>>) mapsheet.clone();
				Iterator iterator = mapassigned.keySet().iterator();
				while(iterator.hasNext()){
					Assignment a = (Assignment) iterator.next();
					if(mapsheet.containsKey(a)){
						List<Assignment> listSheet = mapsheet.get(a);
						List<Assignment> listAssigned = mapassigned.get(a);
						for(Assignment as: listAssigned){
							if(!listSheet.contains(as)){
								listSheet.add(as);
								map.put(a, listSheet);
							}
						}
						
					}else{
						List<Assignment> l = mapassigned.get(a);
						map.put(a,l);
					}
				}

	
				
				
				
				return map;
				
			}



			/*TODO Fix silly query
			 * 
			 * (non-Javadoc)
			 * @see org.opensheet.server.dao.AssignmentUserDAO#getDiffAssignmentsByUser(org.opensheet.shared.model.User, java.util.Calendar)
			 */
			
			
			@Override
			@Transactional
			public List<Assignment> getDiffAssignmentsByUser(User user,	Calendar cal) {
				Map<Assignment, List<Assignment>> mapSheet = this.getSheetAssignmentByUser(user, cal);
				Map<Assignment, List<Assignment>> mapAssign = this.getAssignedAssignmentByUser(user);
	
				
			/*	
				for(Map.Entry<Assignment, List<Assignment>>  kv: mapSheet.entrySet()){
					System.out.println("> " + kv.getKey().getName());
					for(Assignment a: kv.getValue()){
						System.out.println(">   " + a.getName());

					}
					
				}
				
				for(Map.Entry<Assignment, List<Assignment>>  kv: mapAssign.entrySet()){
					System.out.println("* " + kv.getKey().getName());
					for(Assignment a: kv.getValue()){
						System.out.println("*   " + a.getName());

					}
					
				}
				*/
				
				
				List<Assignment> result = new ArrayList<Assignment>();
				
				
				for(Map.Entry<Assignment, List<Assignment>> kv: mapSheet.entrySet()){
					Assignment a = kv.getKey();
					if(mapAssign.containsKey(a)){
						if(kv.getValue().size() >0){
							List<Assignment> assigned = mapAssign.get(a);
							for(Assignment sub: kv.getValue()){
								if(!assigned.contains(sub)){
									result.add(sub);
								}
							}
							
						}
						
						
						
					}else{
						result.add(a);
					}
				}	
				
				/*
						List<Assignment> assigned = mapAssign.get(a);	
						List<Assignment> insheet =  mapSheet.get(a);
						
						if(assigned.size() != 0){
							for(Assignment asheet: insheet){
								if(!assigned.contains(asheet)){
									listAssignmentsFromSheet.add(asheet);
								}
							}
						}else{
							for(Assignment asheet: insheet){
									listAssignmentsFromSheet.add(asheet);
							}
						}
					}else{
						List<Assignment> l = mapSheet.get(a);
						if(!l.isEmpty()){
							for(Assignment as: l)
								listAssignmentsFromSheet.add(as);
						}
						listAssignmentsFromSheet.add(a);	
					}
				}
	*/
				return result;
			}




			@SuppressWarnings("unchecked")
			@Override
			@Transactional
			public List<Assignment> getProjectManagerAssignments(User user) {
				List<Assignment> assignments = null;
				if(user.getPermission().getAdmin() == true){
					assignments = (ArrayList<Assignment>) sessionFactory.getCurrentSession().createQuery("from Assignment a where a.level=0").list();
				}else{
					assignments = (ArrayList<Assignment>) sessionFactory.getCurrentSession().createQuery("from Assignment a where a.level=0 and a.owner = ?").setInteger(0,user.getId()).list();
				}
				return assignments;
			}




			@Override
			@Transactional
			public void setAssignmentToUser(List<Assignment> assignments,User user) {
				if(assignments.size() != 0 && user !=null){
					List<Assignment> assignedAssignmentToUser = user.getAssignments();
					if(assignedAssignmentToUser.size() == 0){
						user.setAssignments(assignments);
						sessionFactory.getCurrentSession().saveOrUpdate(user);
					}else{
						for(Assignment a: assignments){
							if(!assignedAssignmentToUser.contains(a)){
								assignedAssignmentToUser.add(a);
							}
						}
						user.setAssignments(assignedAssignmentToUser);
						sessionFactory.getCurrentSession().saveOrUpdate(user);
					}
				}
			
				
			}




			@SuppressWarnings("unchecked")
			@Override
			@Transactional
			public List<User> getAssignmentUsers(Assignment assignment) {
				List<User> result = new ArrayList<User>();
				List<User> users = sessionFactory.getCurrentSession()
				.createQuery("select a.users from Assignment a       where a=?")
				.setEntity(0, assignment)
				.list();
	
				for(User u: users){
					if(u.getStatus() == true){
						result.add(userDAO.getUser(u.getId()));
					}
				}
				
				return result;
			}




	
	
	
	
}
