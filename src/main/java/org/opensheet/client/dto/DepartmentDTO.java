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
import java.util.Date;

import org.opensheet.shared.model.Department;



import com.extjs.gxt.ui.client.data.BeanModelTag;

public class DepartmentDTO implements Serializable, BeanModelTag{

	/**	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private Integer id;
	private UserDTO owner;
	private Boolean status;
	private String name;
	private String note;
	private Date started;
	private Date finished;
	private Date updated;
	private BranchDTO branch;
	
	
	public DepartmentDTO(){
		
	}
	
	public DepartmentDTO(Integer id){
		this.setId(id);	
	}
	
	
	public DepartmentDTO(Department department){
		this.setId(department.getId());
		this.setStatus(department.getStatus());
		this.setName(department.getName());
		this.setNote(department.getNote());
		this.setStarted(department.getStarted());
		this.setFinished(department.getFinished());
		this.setUpdated(department.getUpdated());	
	}
	
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getStarted() {
		return started;
	}
	public void setStarted(Date started) {
		this.started = started;
	}
	public Date getFinished() {
		return finished;
	}
	public void setFinished(Date finished) {
		this.finished = finished;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Department getDepartment(){
		return new Department(this);
	}

	public BranchDTO getBranch() {
		return branch;
	}

	public void setBranch(BranchDTO branch) {
		this.branch = branch;
	}




	

}
