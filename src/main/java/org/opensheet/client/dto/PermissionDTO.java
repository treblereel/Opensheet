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
package org.opensheet.client.dto;

import java.io.Serializable;

import org.opensheet.shared.model.Permission;

import com.extjs.gxt.ui.client.data.BeanModelTag;

public class PermissionDTO implements Serializable, BeanModelTag{

	private static final long serialVersionUID = 1L;

	
	private UserDTO user;
	private Boolean admin;
	private Boolean pm;
	private Boolean dm;
	private Boolean fd;
	
	public PermissionDTO(){
		
	}
	
	public PermissionDTO(Permission permission){
//		this.setId(permission.getId());
		this.setUser(permission.getUser().getUserDTO());
		this.setAdmin(permission.getAdmin());
		this.setDm(permission.getDm());
		this.setFd(permission.getFd());
		this.setPm(permission.getPm());
		
		
	}
	
	
	public Permission getPermission(){
		return new Permission(this);		
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Boolean getPm() {
		return pm;
	}

	public void setPm(Boolean pm) {
		this.pm = pm;
	}

	public Boolean getDm() {
		return dm;
	}

	public void setDm(Boolean dm) {
		this.dm = dm;
	}

	public Boolean getFd() {
		return fd;
	}

	public void setFd(Boolean fd) {
		this.fd = fd;
	}
/*
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
*/	
	
}
