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


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.opensheet.client.dto.AuthmethodDTO;




@Entity
@Table(name= "authmethod")
public class Authmethod{


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	private String type;
	
	private String description;
	@Column(columnDefinition = "tinyint")
	private Boolean scannable;
	
	private String data;

	public Authmethod(){
		
		
	}
	
	public Authmethod(Integer id){
	this.id = id;	
		
		
	}
	
	
	/**Coz we don't want to send auth object
	 * 
	 * @param authmethod
	 * @return
	 */
	
	public AuthmethodDTO getAuthmethodSimpleDTO(){
		AuthmethodDTO authmethodDTO  = new AuthmethodDTO();
		authmethodDTO.setId(this.getId());
		authmethodDTO.setType(this.type);
		authmethodDTO.setDescription(this.getDescription());
		authmethodDTO.setData(null);
		return authmethodDTO;
	}
	
	
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setScannable(Boolean scannable) {
		this.scannable = scannable;
	}

	public Boolean getScannable() {
		return scannable;
	}
	

}
