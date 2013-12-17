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
import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.opensheet.server.dao.BranchDAO;
import org.opensheet.server.dao.DepartmentDAO;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class DepartmentDAOImpl implements DepartmentDAO{
	
	@Autowired	private SessionFactory sessionFactory;

	@Autowired BranchDAO branchDAO;
	
	
    Logger logger = LoggerFactory.getLogger(DepartmentDAOImpl.class);

	
	/**params: any for all department , 1 for active, 0 for inactive
	 * returns: List<Department>
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Department> listDepartment(String s) {
		List<Department> departments;
		if(s.equalsIgnoreCase("any")){
			departments = new ArrayList<Department>(sessionFactory.getCurrentSession().createQuery("from Department d join fetch d.owner as u order by d.name").list());
		}else{
			departments = new ArrayList<Department>(sessionFactory.getCurrentSession().createQuery("from Department d join fetch d.owner as u   where d.status = ? order by d.name").setString(0, s).list());
		}
	    return departments;
	}
	
	
	
	@Override
	@Transactional
	public void updateDepartment(Department department){
		Department d = (Department) sessionFactory.getCurrentSession().get(Department.class, new Integer(department.getId()));
		d.setName(department.getName());
		d.setOwner(department.getOwner());
		d.setStatus(department.getStatus());
		d.setNote(department.getNote());
		d.setFinished(department.getFinished());
		d.setUpdated(new Date());
		d.setBranch(branchDAO.getBranchById(department.getBranch().getId()));
		sessionFactory.getCurrentSession().saveOrUpdate(d);
	}
	
	
	
	@Override
	@Transactional
	public void addDepartment(Department department){
		department.setStarted(new Date());
		department.setUpdated(new Date());
		sessionFactory.getCurrentSession().save(department);

	}


	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<User> getUsers(Department department) {
		List<User> user = sessionFactory.getCurrentSession().createQuery(
				"from User u  join fetch u.userRate as r       where u.department=? order by u.secondName")
				.setInteger(0, department.getId()).list();
	return user;
	}



	@Override
	@Transactional
	public void setAssignemntToDepartment(Assignment assignment,List<Department> departments) {
		assignment.setDepartments(departments);
	}



	@Override
	@Transactional
	public Department getDepartmentById(Integer id) {
		Department department = (Department) sessionFactory.getCurrentSession().get(Department.class, new Integer(id));
		return department;
	}



	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Department> listDepartmentByBranch(String s, Integer branch) {
		List<Department> departments;
		if(s.equalsIgnoreCase("any")){
			if(branch == 9999999){
				departments = new ArrayList<Department>(sessionFactory.getCurrentSession().createQuery("from Department d join fetch d.owner as u join fetch d.branch as b order by d.name").list());
			}else{
				departments = new ArrayList<Department>(sessionFactory.getCurrentSession().createQuery("from Department d join fetch d.owner as u join fetch d.branch as b where d.branch = ? order by d.name").setInteger(0, branch).list());
			}
		}else{
			if(branch == 9999999){
				departments = new ArrayList<Department>(sessionFactory.getCurrentSession().createQuery("from Department d join fetch d.owner as u join fetch d.branch as b  where d.status = ?  order by d.name").setString(0, s).list());
			}else{
				departments = new ArrayList<Department>(sessionFactory.getCurrentSession().createQuery("from Department d join fetch d.owner as u join fetch d.branch as b  where d.status = ? and d.branch = ? order by d.name").setString(0, s).setInteger(1, branch).list());
			}
		}

	    return departments;
	}


	/**
	 * @param user -- department manager
	 * @return List<Department> of departments owned by this Department Manager
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Department> getDepartment(User user) {
		ArrayList<Department> departments = new ArrayList<Department>(
		sessionFactory.getCurrentSession().createQuery("from Department d join fetch d.owner as u" +
		" join fetch d.branch as b  where d.owner=? order by d.name").setInteger(0, user.getId()).list());

		return departments;
	}



	@Override
	@Transactional
	public List<User> getUserByAssignmentAndByDepartment(Assignment assignment,Department department) {
		List<User> users = new ArrayList<User>();
		Hibernate.initialize(assignment.getUsers());
		Hibernate.initialize(department.getUsers());
		List<User> usersAssignment = assignment.getUsers();
		   Iterator<User> itr =  department.getUsers().iterator();
		   while(itr.hasNext()){
			   User u = itr.next();
			   if(usersAssignment.contains(u) && u.getStatus() == true){
					users.add(u);
				}  
		   }
		return users;
	}



	@Override
	@Transactional
	public List<User> getDepartmentManagerUsers(User user) {
		List<User> users = new ArrayList<User>();
		for(Department d: getDepartment(user)){
			for(User u: d.getUsers()){
				if(!users.contains(u)){
					users.add(u);
				}
			}
		}
		return users;
	}
	
}
