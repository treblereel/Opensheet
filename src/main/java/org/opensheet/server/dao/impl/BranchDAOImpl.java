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

import java.util.List;

import org.hibernate.SessionFactory;
import org.opensheet.server.dao.AssignmentDAO;
import org.opensheet.server.dao.BranchDAO;
import org.opensheet.server.dao.DepartmentDAO;
import org.opensheet.server.dao.UserDAO;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Branch;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class BranchDAOImpl implements BranchDAO {

	@Autowired	private SessionFactory sessionFactory;
	@Autowired  private  UserDAO userDAO;
	@Autowired  private  AssignmentDAO assignmentDAO;
	@Autowired  private  DepartmentDAO departmentDAO;  
	
	@Override
	@Transactional
	public Branch getBranchById(Integer id) {
		Branch branch = (Branch) sessionFactory.getCurrentSession().get(Branch.class, id);
		return branch;
	}

	@Override
	@Transactional
	public List<Branch> getBranchList(Boolean status) {
		@SuppressWarnings("unchecked")
		List<Branch> list = (List<Branch>) sessionFactory.getCurrentSession().createQuery("FROM Branch b WHERE b.status= ? ").setBoolean(0, status).list();
		return list;
	}

	
	/* Move users to default if status 1=>0
	 * 
	 * (non-Javadoc)
	 * @see org.opensheet.server.dao.BranchDAO#setBranch(org.opensheet.shared.model.Branch)
	 */
	@Override
	@Transactional
	public void setBranch(Branch branch) {
		Branch b = 	this.getBranchById(branch.getId());
		b.setName(branch.getName());
		b.setOwner(branch.getOwner());
		b.setIndex(branch.getIndex());
		if(branch.getId() != 1){
			b.setStatus(branch.getStatus());
			if(b.getStatus()== false){
				Branch defaultBranch = getBranchById(1);
				
				List<User> users  = userDAO.getUsersByBranch("any", b.getId());
				for(User u: users){
					u.setBranch(defaultBranch);
				}
				List<Department> departments = departmentDAO.listDepartmentByBranch("any", b.getId());
				for(Department d: departments){
					d.setBranch(defaultBranch);
				}
				
				List<Assignment> assignments = assignmentDAO.getAssignmentByBranch(b);
				for(Assignment a: assignments){
					System.out.println(a.getName() + " " + a.getId() + " " + a.getBranch().getId() );
					if(a.getName() != null)
						a.setBranch(defaultBranch);
				}
				
				//sessionFactory.getCurrentSession().flush();
			}
		}
	}

	
	@Override
	@Transactional
	public void addBranch(Branch branch) {
		sessionFactory.getCurrentSession().save(branch);
	}

}
