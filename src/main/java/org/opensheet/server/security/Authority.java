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

public class Authority implements Serializable {
	
	
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String username;
    private String authority;
    
    public Authority(Integer id,String username,String authority){
    	this.setId(id);
    	this.setUsername(username);
    	this.setAuthority(authority);
    	
    }
    
    
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public String getAuthority() {
		return authority;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getId() {
		return id;
	}
}
