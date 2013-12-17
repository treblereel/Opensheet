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


import org.opensheet.server.dao.AuthDAO;
import org.opensheet.server.dao.AuthFactory;
import org.opensheet.server.security.IAuth;
import org.opensheet.server.security.IAuthAD;
import org.opensheet.server.security.IAuthDbPasswd;
import org.opensheet.server.utils.Exceptions.BadAuthMethodException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
public class AuthFactoryImpl extends AuthFactory 
{

	
	@Autowired private AuthDAO authDAO;
	
	@Override
	@Transactional
	public IAuth get(Integer authId) throws BadAuthMethodException{
		String type = "" ;
		
		try{
			type =	authDAO.get(authId).getType().toString();
		}catch(Exception e){
			e.getStackTrace();
			
		}
		
	
		if(type.equals("db")){
			return new	IAuthDbPasswd();
			
			
			
		}else if(type.equals("ad")){
			return new IAuthAD();
		/*	
		}else if(type.equals("ldap")){
			
		}else if(type.equals("ntlm")){
			
		}else if(type.equals("kerberos")){
		*/	
		}else{
			 throw new BadAuthMethodException();
		}

	}
	
	
}







