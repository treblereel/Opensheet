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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

import com.extjs.gxt.ui.client.data.BeanModelTag;

@Entity
@Table(name= "hour")
public class Hour implements Serializable, BeanModelTag{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -892772512055946252L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person", referencedColumnName = "id",nullable= false)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assignment", referencedColumnName = "id", nullable= false)
	private Assignment assignment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task", referencedColumnName = "id",  nullable= false)
	private Assignment task;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department", referencedColumnName = "id")
	private Department department;
	
	@Column(nullable= false)
	@Temporal(value=TemporalType.DATE)
	private Date date;
	@Temporal(value=TemporalType.DATE)
	private Date started;
	@Temporal(value=TemporalType.DATE)
	private Date updated;
	@Column(nullable= false)
	private Integer hour;
	
	@Size(max=1024)
	@Column(nullable = true,length = 1024)
	private String note;
	

	private Integer inratesum;
	private Integer extratesum;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch", referencedColumnName = "id")
	private Branch branch;
	
	
	public Hour(){
		
	}

	public Hour(User user,Assignment assignment,Assignment task,Date date){
			this.setAssignment(assignment);
			this.setTask(task);
			this.setUser(user);
			this.setHour(hour);
	}





	public Integer getId() {
		return id;
	}







	public void setId(Integer id) {
		this.id = id;
	}







	public User getUser() {
		return user;
	}







	public void setUser(User user) {
		this.user = user;
	}






//	@OneToOne(fetch=FetchType.LAZY,optional=false)
	
	public Assignment getAssignment() {
		return assignment;
	}







	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}






//	@OneToOne(fetch=FetchType.LAZY,optional=false)
	public Assignment getTask() {
		return task;
	}







	public void setTask(Assignment task) {
		this.task = task;
	}







	public Date getDate() {
		return date;
	}







	public void setDate(Date date) {
		this.date = date;
	}







	public Integer getHour() {
		return hour;
	}







	public void setHour(Integer hour) {
		this.hour = hour;
	}







	public void setUpdated(Date updated) {
		this.updated = updated;
	}







	public Date getUpdated() {
		return updated;
	}

	public void setStarted(Date started) {
		this.started = started;
	}

	public Date getStarted() {
		return started;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Department getDepartment() {
		return department;
	}

	public Integer getInratesum() {
		return inratesum;
	}

	public void setInratesum(Integer inratesum) {
		this.inratesum = inratesum;
	}

	public Integer getExtratesum() {
		return extratesum;
	}

	public void setExtratesum(Integer extratesum) {
		this.extratesum = extratesum;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	
}
