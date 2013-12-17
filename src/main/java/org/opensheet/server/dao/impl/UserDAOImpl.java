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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.opensheet.server.dao.AssignmentDAO;
import org.opensheet.server.dao.AssignmentUserDAO;
import org.opensheet.server.dao.AuthDAO;
import org.opensheet.server.dao.UserDAO;
import org.opensheet.server.security.CustomUser;
import org.opensheet.server.utils.Comparators;
import org.opensheet.server.utils.Hash;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.AssignmentUserDetail;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.Permission;
import org.opensheet.shared.model.User;
import org.opensheet.shared.model.UserRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("UserDAO")
public class UserDAOImpl implements UserDAO{
	
	@Autowired	private SessionFactory sessionFactory;
	@Autowired 	private AuthDAO authDAO;
	@Autowired 	private AssignmentUserDAO assignmentUserDAO;
	@Autowired 	private AssignmentDAO assignmentDAO;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<User> listUser(String s) {
		List<User> users;
		if(s.equalsIgnoreCase("any")){
			users = new ArrayList<User>(sessionFactory.getCurrentSession().createQuery("from User").list());
					}else{
			users = new ArrayList<User>(sessionFactory.getCurrentSession().createQuery("from User u where u.status = ?").setString(0, s).list());
			}

		
		
		Collections.sort(users, Comparators.ComparatorUserName);
		
		return users;
	}


	
	
	

	@Override
	@Transactional
	public void addUser(User user) {
		Permission p = new Permission();
		p.setAdmin(false);
		p.setDm(false);
		p.setFd(false);
		p.setPm(false);
		
		UserRate userRate = new UserRate();
		userRate.setDate(new Date());
		userRate.setInternalRate(0);
		sessionFactory.getCurrentSession().save(userRate);
		
		user.setLang("en");
		user.setStatus(true);
		user.setPermission(p);
		user.setPassword(Hash.md5(user.getPassword()));
		user.setStart(new Date());
		user.setUpdated(new Date());
		user.setUserRate(userRate);
		sessionFactory.getCurrentSession().save(user);
		p.setUser(user);
		sessionFactory.getCurrentSession().save(p);
		
		assignmentUserDAO.setAssignmentToUser(assignmentDAO.getDefaultAssignment(), user);
		
	}



	/** FIX AUTH
	 * 
	 */
	
	
	@Override
	@Transactional
	public void updateUser(User u) {
		User user = (User) sessionFactory.getCurrentSession().get(User.class, new Integer(u.getId()));
		UserRate userrate = user.getUserRate();
		
		user.setLogin(u.getLogin());
		user.setFirstName(u.getFirstName());
		user.setSecondName(u.getSecondName());
		user.setEmail(u.getEmail());
		user.setNote(u.getNote());
		user.setStatus(u.getStatus());
		user.setAuthmethod(u.getAuthmethod());
		user.setBranch(u.getBranch());

		if(u.getPassword() != null){
			if(u.getAuthmethod() == 1 && u.getPassword().equals("********") != true){
				user.setPassword(Hash.md5(u.getPassword()));
			}
		}
		
	//	Убрать мертвых пользователей из описания проектво в отделах
		
		
		
		user.setDepartment(u.getDepartment());
		user.setFinish(u.getFinish());
		user.setUpdated(new Date());

		userrate.setInternalRate(u.getUserRate().getInternalRate());
		userrate.setDate(new Date());
		
		if(user.getStatus() == false){
			List<Assignment> fakeList = new ArrayList<Assignment>(); 
			user.setAssignments(fakeList);
		}
		
		sessionFactory.getCurrentSession().saveOrUpdate(userrate);
		sessionFactory.getCurrentSession().saveOrUpdate(user);
	}



	@Override
	@Transactional
	public void updateUserPermission(User u) {
		User user = (User) sessionFactory.getCurrentSession().get(User.class, new Integer(u.getId()));
		user.setPermission(u.getPermission());
		sessionFactory.getCurrentSession().saveOrUpdate(user);
		
	}

	
	/**Save user internal rate
	 * 
	 * 
	 */
	@Override
	@Transactional
	public void setUserInternalRate(User u,Integer rate){
		User user = this.getUser(u.getId());
		user.getUserRate().setInternalRate(rate);
		sessionFactory.getCurrentSession().saveOrUpdate(user);
	}

	/** Get List<User> by their role:
	 * Maybe better to use enum here?
	 * @param String:
	 * pm - project manager,
	 * dm - department manager,
	 * admin - administrator
	 * fd - financial department 
	 * where all users active
	 * @return List<UserDTO
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<User> getUsersByRole(String s) {
		List<User> users;
		users =  new ArrayList<User>(sessionFactory.getCurrentSession().createQuery("from User u join fetch u.permission as p  where u.status=1 and p." + s +"=1 order by u.secondName").
				list());
		return users;
	}






	@Override
	@Transactional
	public List<Assignment> getUserAssignment(User user) {
	//	User user = (User) sessionFactory.getCurrentSession().get(User.class, new Integer(u.getId()));
		List<Assignment> listAssignment = new ArrayList<Assignment>(user.getAssignments() != null ? user.getAssignments().size() : 0);
		for(Assignment a: user.getAssignments()){
			if(a.getLevel() == 0){
				listAssignment.add(a);
			}
		}
			return listAssignment;
	}
	
	
	@Override
	@Transactional
	public List<Assignment> getUserSubAssignment(User u, Assignment a) {
		String hql = "SELECT a FROM Assignment a JOIN a.Users u where a.level=1 and u.id=" + u.getId() + "and a.first!=1 and a.parent.id=" + a.getId() + " GROUP BY a.name";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<Assignment> la = (List<Assignment>) query.list();
		return la;
	}





		/** get User by Login
		 * 
		 * 
		 */
		@Override
		public User getUser(String login) {

		User user = (User) sessionFactory.getCurrentSession()
				.createQuery("from User u " +
				" join fetch  u.permission as p  " +
				"  where u.login=?")
				.setString(0, login).setFirstResult(0).uniqueResult();
		if(user!=null){
			return user;
		}else{
			return null;
		}
	}






	@Override
	@Transactional
	public User getUser(Integer id) {
		User user = (User) sessionFactory.getCurrentSession().createQuery("from User u join " +
				"fetch u.department as d  " +
				"join fetch  u.branch as b " +
				"join fetch  u.userRate as ur " +
				"join fetch  u.permission as p  " +
				"where u.id=?").setInteger(0, id).uniqueResult();

		return user;
	}




			/*Check if user is DM but have no departments!
			 * (non-Javadoc)
			 * @see org.opensheet.server.dao.UserDAO#getManagedDepartments(org.opensheet.shared.model.User)
			 */

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Department> getManagedDepartments(User user) {
		if(user.getPermission().getDm() == true){
			
		List<Department> departments = (List<Department>) sessionFactory.getCurrentSession().createQuery("From Department d where d.status=1 and d.owner=?")
		.setInteger(0,user.getId())
		.list();	
		if(departments.size() != 0)
			return departments;
		
		}
		return Collections.emptyList();
	}






	@Override
	@Transactional
	public AssignmentUserDetail getUserAssignmentDetail(User user,Assignment assignment) {
		AssignmentUserDetail  assignmentPersonDetail = (AssignmentUserDetail) sessionFactory.getCurrentSession().createQuery("from AssignmentUserDetail apd where apd.assignment=? and apd.user=?").
				setInteger(0, assignment.getId()).setInteger(1, user.getId()).uniqueResult();
	if(assignmentPersonDetail != null){
		return assignmentPersonDetail;
	}
		return null;
	}






	@Override
	@Transactional
	public void setUserAssignmentDetail(AssignmentUserDetail a) {
		AssignmentUserDetail assignmentUserDetail; 
		if(a.getId() == null){
			assignmentUserDetail = new AssignmentUserDetail();
			assignmentUserDetail.setStarted(new Date());
		}else{
			assignmentUserDetail = (AssignmentUserDetail)	sessionFactory.getCurrentSession().get(AssignmentUserDetail.class,a.getId());
		}
		assignmentUserDetail.setAssignment(a.getAssignment());
		assignmentUserDetail.setUser(a.getUser());
		assignmentUserDetail.setRate(a.getRate());
		assignmentUserDetail.setTimeline(a.getTimeline());
		assignmentUserDetail.setUpdated(new Date());
		
		sessionFactory.getCurrentSession().saveOrUpdate(assignmentUserDetail);
	}






	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<User> getUsersByBranch(String s, Integer branch) {
		List<User> users;
		if(s.equalsIgnoreCase("any")){
			if(branch == 9999999){
				users = new ArrayList<User>(sessionFactory.getCurrentSession().createQuery("from User u join fetch u.department as d  join fetch  u.branch as b order by u.secondName").list());

			}else{
				users = new ArrayList<User>(sessionFactory.getCurrentSession().createQuery("from User u join fetch u.department as d  join fetch  u.branch as b where u.branch=? order by u.secondName").setInteger(0, branch).list());
			}
			
		}else{
			if(branch == 9999999){
				users = new ArrayList<User>(sessionFactory.getCurrentSession().createQuery("from User u join fetch u.department as d  join fetch  u.branch as b where u.status = ? order by u.secondName").setString(0, s).list());
			}else{
				users = new ArrayList<User>(sessionFactory.getCurrentSession().createQuery("from User u join fetch u.department as d  join fetch  u.branch as b  where u.status = ? and u.branch=? order by u.secondName").setString(0, s).setInteger(1, branch).list());

			}
		}
		
		if(users.isEmpty()){
			return Collections.emptyList();
		}
		
		return users;
	}






	@Override
	@Transactional
	public User whoIam() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
	    Authentication authentication = securityContext.getAuthentication();
	    CustomUser customUser = (CustomUser) authentication.getPrincipal();
		return getUser(customUser.getId());
	}






	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<User> getUsersByBranchAndByDepartment(String s, Integer branch,	Integer department) {
		List<User> users;
		if(s.equalsIgnoreCase("any")){
			if(branch == 9999999){
				users = new ArrayList<User>(sessionFactory.getCurrentSession().createQuery("from User u " +
				"join fetch u.department as d  join fetch  u.branch as b where u.department=?" +
				" order by u.secondName").setInteger(0, department).list());

			}else{
				users = new ArrayList<User>(sessionFactory.getCurrentSession().createQuery("from User u " +
				"join fetch u.department as d  join fetch  u.branch as b where u.branch=? and u.department=?" +
				" order by u.secondName").setInteger(0, branch).setInteger(1, department).list());
			}
			
		}else{
			if(branch == 9999999){
				users = new ArrayList<User>(sessionFactory.getCurrentSession().createQuery("from User u " +
				"join fetch u.department as d  join fetch  u.branch as b where u.status = ? and u.department=? " +
				" order by u.secondName").setString(0, s).setInteger(1, department).list());
			}else{
				users = new ArrayList<User>(sessionFactory.getCurrentSession().createQuery("from User u join" +
				" fetch u.department as d  join fetch  u.branch as b  where u.status = ? and u.branch=? and" +
				" u.department=? order by u.secondName").setString(0, s).setInteger(1, branch).setInteger(2, department).list());
			}
		}
		
		if(users.isEmpty()){
			return Collections.emptyList();
		}
		return users;

	}






	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<User> getUsersByDepartment(String s, Integer department) {
		List<User> users;
		
		
		if(s.equalsIgnoreCase("any")){
			
				users = new ArrayList<User>(sessionFactory.getCurrentSession().createQuery("from User u " +
				" join fetch u.department as d  join fetch  u.branch as b where u.department=?" +
				" order by u.secondName").setInteger(0, department).list());
		}else{

				users = new ArrayList<User>(sessionFactory.getCurrentSession().createQuery("from User u " +
				" join fetch u.department as d  join fetch  u.branch as b where u.status = ? and  u.department=? " +
				" order by u.secondName").setString(0, s).setInteger(1, department).list());

		}
		
		if(users.isEmpty()){
			return Collections.emptyList();
		}
		return users;

	}






	@Override
	@Transactional
	public void setUsersByDepartmentAndByAssignment(List<User> users,Department department, Assignment assignment) {
		List<User> usersAssignment = assignment.getUsers();
		for(User u: department.getUsers()){			
			 if(users.contains(u)){
				 if(!usersAssignment.contains(u)){
					 usersAssignment.add(u);
				 }
			 }else{
				 if(usersAssignment.contains(u)){
					 usersAssignment.remove(u); 
				 }
			 }
		 }
		 assignment.setUsers(usersAssignment);
		 sessionFactory.getCurrentSession().save(assignment);
	}






	@Override
	@Transactional
	public void setLang(User u) {
		User user = this.getUser(u.getId());
		user.setLang(u.getLang());
	}
	

}
