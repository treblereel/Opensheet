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
package org.opensheet.server.security;

import org.opensheet.server.dao.UserDAO;
import org.opensheet.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Repository;




@Repository
public class CheckUserContextImpl implements CheckUserContext {



	
	@Autowired
	private UserDAO userDAO;


	

	@Override
	public User getUser(SecurityContext securityContext){
		Authentication authentication = securityContext.getAuthentication();
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		User user = (User) userDAO.getUser(customUser.getId());
		return user;
	}
	
}
