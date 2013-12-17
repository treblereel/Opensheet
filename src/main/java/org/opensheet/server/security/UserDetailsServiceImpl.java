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

import java.util.HashSet;
import java.util.Set;

import org.opensheet.server.dao.UserDAO;
import org.opensheet.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired private UserDAO userDAO;
	
	@Override
	@Transactional(readOnly = true)
	public CustomUser loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		
		

		User userEntity = userDAO.getUser(username);
	    if (userEntity == null)
	      throw new UsernameNotFoundException("user not found");

	    
	    CustomUser customUser = new CustomUser();
	    customUser.setId(userEntity.getId());
	    customUser.setPassword(userEntity.getPassword());
	    customUser.setUsername(userEntity.getLogin());
	    customUser.setAccountNonExpired(userEntity.getStatus());
	    customUser.setAccountNonLocked(userEntity.getStatus());
	    customUser.setCredentialsNonExpired(userEntity.getStatus());
	    customUser.setEnabled(userEntity.getStatus());
	    customUser.setAuthMethod(userEntity.getAuthmethod());
	    
	    Set<Authority> roles = new HashSet<Authority>();
	     roles.add(new Authority(userEntity.getId(),userEntity.getLogin(),"ROLE_USER"));
	    if(userEntity.getPermission().getAdmin() == true)
		     roles.add(new Authority(userEntity.getId(),userEntity.getLogin(),"ROLE_ADMIN"));
	    if(userEntity.getPermission().getDm() == true)
		     roles.add(new Authority(userEntity.getId(),userEntity.getLogin(),"ROLE_DM"));
	    if(userEntity.getPermission().getFd() == true)
		     roles.add(new Authority(userEntity.getId(),userEntity.getLogin(),"ROLE_FD"));
	    if(userEntity.getPermission().getPm() == true)
		     roles.add(new Authority(userEntity.getId(),userEntity.getLogin(),"ROLE_PM"));

	    
	    
	    
	    customUser.setRoles(roles);
	    
	   
	    
	    return  customUser;
		
		
		
		
	} 

}
