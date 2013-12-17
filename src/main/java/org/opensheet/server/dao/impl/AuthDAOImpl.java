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

import org.hibernate.SessionFactory;
import org.opensheet.server.dao.AuthDAO;
import org.opensheet.shared.model.Authmethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class AuthDAOImpl implements AuthDAO{

	@Autowired	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public Authmethod get(Integer id) {
		Authmethod authmethod = null;
		authmethod = (Authmethod)	sessionFactory.getCurrentSession().get(Authmethod.class, id);
		return authmethod;

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Authmethod> get() {
		List<Authmethod> authmethods = new ArrayList<Authmethod>(0);
		authmethods 	 = (List<Authmethod>) sessionFactory.getCurrentSession().createQuery("from Authmethod").list(); 
		return authmethods;
	}

	@Override
	@Transactional
	public void set(Authmethod authmethodUpdated) {
		Authmethod authmethod = (Authmethod)	sessionFactory.getCurrentSession().get(Authmethod.class, authmethodUpdated.getId());
		authmethod.setDescription(authmethodUpdated.getDescription());
		authmethod.setData(authmethodUpdated.getData());
		sessionFactory.getCurrentSession().save(authmethod);
	}
	
}
