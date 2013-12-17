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
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.opensheet.server.dao.PermissionDAO;
import org.opensheet.shared.model.Permission;
import org.opensheet.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;

@Repository
public class PermissionDAOImpl implements PermissionDAO {

	@Autowired
	private SessionFactory sessionFactory;

	
	@Override
	@Transactional
	public User   getUserPermission(User u){
		
		    User user = (User) sessionFactory.getCurrentSession().get(User.class, new Integer(u.getId()));
		//    User user = (User) sessionFactory.getCurrentSession().createQuery("from Permission where user=" + u.getId()).uniqueResult();
		    user.getPermission();
			return user;
	}
	
	
	
	@Override
	@Transactional
	public List<User> getUsersPermissions(PagingLoadConfig loadConfig){
		
		@SuppressWarnings("unchecked")
		List<User> users = new ArrayList<User>(sessionFactory.getCurrentSession().createQuery("select u from User u  join fetch u.permission as p where  u.status=1 order by u.secondName")
		.setFirstResult(loadConfig.getOffset())
		.setMaxResults(loadConfig.getLimit())
		.list());
		
		for(User u: users){
			Hibernate.initialize(u.getPermission());
		}
		
		return users;
	}
	
	@Override
	@Transactional
	public void updateUserPermission(User u){
	//	Permission permission = (Permission) sessionFactory.getCurrentSession().get(Permission.class, new Integer(u.getId()));
		Permission permission = (Permission) sessionFactory.getCurrentSession().createQuery("from Permission where user=" + u.getId()).uniqueResult();
		permission.setAdmin(u.getPermission().getAdmin());
		permission.setPm(u.getPermission().getPm());
		permission.setDm(u.getPermission().getDm());
		permission.setFd(u.getPermission().getFd());
		sessionFactory.getCurrentSession().saveOrUpdate(permission);
	}
	
	
}
