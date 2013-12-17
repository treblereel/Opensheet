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

import org.opensheet.shared.model.Authmethod;


import com.extjs.gxt.ui.client.data.BeanModelTag;

public class AuthmethodDTO implements Serializable, BeanModelTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	
	private Integer id;
	private String  type;
	private String  description;
	private Boolean scannable;
	private String  data;
	
	
	public AuthmethodDTO(){

	}

	
	public AuthmethodDTO(Integer id){
		this.setId(id);
		
	}

	public AuthmethodDTO(Authmethod authmethod){
		this.setId(authmethod.getId());
		this.setType(authmethod.getType());
		this.setDescription(authmethod.getDescription());
		this.setData("null");
	}

	
	
	public Authmethod  getAuthmethod(){
		return new Authmethod();
	//	return new Authmethod(this);
	}
	
	/**Coz we don't want to send auth object
	 * 
	 * @param authmethod
	 * @return
	 */
	public AuthmethodDTO getAuthmethodSimpleDTO(){
		AuthmethodDTO authmethodDTO  = new AuthmethodDTO();
		authmethodDTO.setId(this.getId());
		authmethodDTO.setType(this.getType());
		authmethodDTO.setDescription(this.getDescription());
		authmethodDTO.setData(null);
		return authmethodDTO;
	}
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	

	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Boolean getScannable() {
		return scannable;
	}


	public void setScannable(Boolean scannable) {
		this.scannable = scannable;
	}


	public void setData(String data) {
		this.data = data;
	}


	public String getData() {
		return data;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthmethodDTO other = (AuthmethodDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
