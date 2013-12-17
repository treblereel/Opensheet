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

import org.opensheet.server.dao.AuthDAO;
import org.opensheet.server.dao.AuthFactory;
import org.opensheet.server.utils.Exceptions.BadAuthMethodException;
import org.opensheet.shared.model.Authmethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
public class UserPasswdCheckImpl implements UserPasswdCheck{

		@Autowired	private AuthFactory authFactory;
		@Autowired  private AuthDAO authmethodDAO;
	
		
		@Override
		@Transactional
		public  Boolean check(CustomUser user,String password) throws BadAuthMethodException{
			
			
			return authFactory.get(user.getAuthMethod()).doAuth(user,password,authmethodDAO.get(user.getAuthMethod()));
		}



	}
