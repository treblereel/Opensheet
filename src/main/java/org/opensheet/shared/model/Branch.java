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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.opensheet.client.dto.BranchDTO;

import com.extjs.gxt.ui.client.data.BeanModelTag;


@Entity
@Table(name= "branch")
public class Branch implements Serializable, BeanModelTag {

	private static final long serialVersionUID = -7463337990722988422L;

	
	
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	
	private String name;
	@Column(columnDefinition = "tinyint")
	private Boolean status;
	
	@Column(name="branch_index",length=64)
	private String index;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id", referencedColumnName = "id",nullable= false)
	private User owner;

	
	public Branch (){
			
		}
	
	public Branch (Integer id){
		this.id = id;
	}
	
	public Branch (BranchDTO branchDTO){
		this.setId(branchDTO.getId());
		this.setName(branchDTO.getName());
//		this.setOwner(new User(branchDTO.getOwner().getId()));
		this.setStatus(branchDTO.getStatus());
		this.setIndex(branchDTO.getIndex());

		}
	
	
	
	
	public User getOwner() {
		return owner;
	}

	
	
	
	
	
	
	
	
	
	
	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
