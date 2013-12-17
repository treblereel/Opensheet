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
package org.opensheet.shared.model;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.opensheet.client.dto.PermissionDTO;

import com.extjs.gxt.ui.client.data.BeanModelTag;


@Entity
@Table(name= "permission")
public class Permission implements Serializable, BeanModelTag{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	
//	@EmbeddedId
//	@GeneratedValue(strategy=GenerationType.AUTO)
//	private Integer id;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	private User    user;
	@Column(columnDefinition = "tinyint")
	private Boolean admin;
	@Column(columnDefinition = "tinyint")
	private Boolean pm;
	@Column(columnDefinition = "tinyint")
	private Boolean dm;
	@Column(columnDefinition = "tinyint")
	private Boolean fd;
	
	
	
	public Permission(){
	}
	
	

	public Permission(PermissionDTO permissionDTO){
		
//		this.setId(permissionDTO.getId());
		
		this.setAdmin(permissionDTO.getAdmin());
		this.setUser(permissionDTO.getUser().getUser());
		this.setDm(permissionDTO.getDm());
		this.setFd(permissionDTO.getFd());
		this.setPm(permissionDTO.getPm());
	}
	
	
	public PermissionDTO getPermissionDTO(){
		return new PermissionDTO(this);
	}
	

	public void setFd(Boolean fd) {
		this.fd = fd;
	}


	public Boolean getFd() {
		return fd;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
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
/*
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
*/



	public void setId(Integer id) {
		this.id = id;
	}



	public Integer getId() {
		return id;
	}

}
