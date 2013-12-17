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

import org.opensheet.shared.model.Branch;

import com.extjs.gxt.ui.client.data.BeanModelTag;

public class BranchDTO  implements Serializable, BeanModelTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8328511803875792652L;
	
	private Integer id;
	private String name;
	private UserDTO owner;
	private Boolean status;
	private String index;
	
	public BranchDTO(Branch branch){
		this.setId(branch.getId());
		this.setName(branch.getName());
		this.setStatus(branch.getStatus());
		this.setIndex(branch.getIndex());
	}
	
	public BranchDTO(){
		
		
	}
	
	public BranchDTO(Integer id) {
		this.setId(id);
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public UserDTO getOwner() {
		return owner;
	}
	public void setOwner(UserDTO owner) {
		this.owner = owner;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	

	
}
