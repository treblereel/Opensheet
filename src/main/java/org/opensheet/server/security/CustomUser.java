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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUser implements Serializable, UserDetails{
	  private static final long serialVersionUID = 1L;
      private Integer  id;
      private String password;
      private String username;
      private Set<Authority> roles;
      
      private boolean accountNonExpired;
      private boolean accountNonLocked;
      private boolean credentialsNonExpired;
      private boolean enabled;
      
      private  Integer authMethod;
      

    public CustomUser() {
    }

    public Set<Authority> getRoles() {
        return roles;
    }
    

    
    @SuppressWarnings("deprecation")
	@Override
    public Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        for (Authority role : roles) {
              list.add(new GrantedAuthorityImpl(role.getAuthority()));
        }
        return list;
        
}

   public void setRoles(Set<Authority> roles) {
         this.roles = roles;
   }



  
 

			public Integer getId() {
				return id;
			}

			public void setId(Integer id) {
				this.id = id;
			}

			public void setPassword(String password) {
				this.password = password;
			}

			public void setUsername(String username) {
				this.username = username;
			}

			public void setAccountNonExpired(boolean accountNonExpired) {
				this.accountNonExpired = accountNonExpired;
			}

			public void setAccountNonLocked(boolean accountNonLocked) {
				this.accountNonLocked = accountNonLocked;
			}

			public void setCredentialsNonExpired(boolean credentialsNonExpired) {
				this.credentialsNonExpired = credentialsNonExpired;
			}

			public void setEnabled(boolean enabled) {
				this.enabled = enabled;
			}

			@Override
			public String getPassword() {
				return this.password;
			}

			@Override
			public String getUsername() {
				return this.username;
			}

			@Override
			public boolean isAccountNonExpired() {
				return this.accountNonExpired;
			}

			@Override
			public boolean isAccountNonLocked() {
				return this.accountNonLocked;
			}

			@Override
			public boolean isCredentialsNonExpired() {
				return this.credentialsNonExpired;
			}

			@Override
			public boolean isEnabled() {
				return this.enabled;
			}

		

			public Integer getAuthMethod() {
				return authMethod;
			}

			public void setAuthMethod(Integer authMethod) {
				this.authMethod = authMethod;
			}




}

	

