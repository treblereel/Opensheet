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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

import com.extjs.gxt.ui.client.data.BeanModelTag;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.opensheet.client.dto.DepartmentDTO;

@Entity
@Table(name= "department")
public class Department  implements Serializable, BeanModelTag{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7024158783873211610L;

	/**
	 * 
	 */
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner", referencedColumnName = "id")
	private User owner;
	
	
	@OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "department", referencedColumnName = "id")
	private Set<User> users = new HashSet<User>(0);
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "assignment_department", joinColumns = { @JoinColumn(name = "department") },inverseJoinColumns = { @JoinColumn(name = "assignment") })
	private List<Assignment> assignments = new ArrayList<Assignment>(0);
	@Column(columnDefinition = "tinyint")
	private Boolean status;
	private String name;
	private String note;
	@Temporal(value=TemporalType.DATE)
	private Date started;
	@Temporal(value=TemporalType.DATE)
	private Date finished;
	@Temporal(value=TemporalType.DATE)
	private Date updated;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
	private Branch branch;
	
	public Department(Integer id){
		this.setId(id);
		
	}
	
	public Department(){
			
		}
	
	
	public Department(DepartmentDTO department){
		this.setId(department.getId());
//		this.setOwner(department.getOwner().getUser());
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

	public User getOwner() {
		return this.owner;
	}
	
	public void setOwner(User owner) {
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

	
	public DepartmentDTO getDepartmentDTO(){
		return new DepartmentDTO(this);
	}










	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<User> getUsers() {
		return users;
	}

	public List<Assignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<Assignment> assignments) {
		this.assignments = assignments;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}







}
