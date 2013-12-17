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
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.opensheet.client.dto.AuthmethodDTO;
import org.opensheet.client.dto.UserDTO;
import com.extjs.gxt.ui.client.data.BeanModelTag;

@Entity
@Table(name= "person")
public class User implements Serializable, BeanModelTag{
	

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4552313640486846057L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String  login;
	private String  password;
	 @Column(name="first_name")
	private String  firstName;
	 @Column(name="second_name")
	private String  secondName;
	private String  email;
	
	/*
	@Transient
	private String  fullName;
	*/
	@Max(value=1024)
	@Column(length = 1024)
	
	private String  note;
	private String  lang;
	

	@Column(columnDefinition = "tinyint")
	private Boolean status;



	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department", referencedColumnName = "id")
	private Department department;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch", referencedColumnName = "id")
	private Branch branch;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	private Permission permission;
	
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rate_id", referencedColumnName = "id")
	private  UserRate userRate;

	
	@Column(name="started")
	@Temporal(value=TemporalType.DATE)
	private Date    start;
	
	@Column(name="finished")
	@Temporal(value=TemporalType.DATE)
	private Date    finish;
	
	@Column(name="updated")
	@Temporal(value=TemporalType.DATE)
	private Date    updated;
	
//	@ManyToMany(fetch = FetchType.LAZY,mappedBy="users",targetEntity=Assignment.class)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "assignment_person", joinColumns = { @JoinColumn(name = "person") },inverseJoinColumns = { @JoinColumn(name = "assignment") })
	private List<Assignment> assignments = new ArrayList<Assignment>(); 
	
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id",nullable=false)
    @LazyToOne(value = LazyToOneOption.NO_PROXY)
	public Permission getPermission() {
		return permission;
	}


	
	private Integer authmethod;
	
	
	public List<Assignment> getAssignments() {
		return assignments;
	}
	public void setAssignments(List<Assignment> a) {
		this.assignments = a;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public String getFullName() {
		return this.getSecondName() + " " + this.getFirstName();
	}
	
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	

	
	public Department getDepartment() {
		
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	

	
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getFinish() {
		return finish;
	}
	public void setFinish(Date finish) {
		this.finish = finish;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
	

	
	public void setPermission(Permission permission) {
		this.permission = permission;
	}
	
	/*
	public void setAuthmethod(Authmethod authmethod) {
		this.authmethod = authmethod;
	}
	public Authmethod getAuthmethod() {
		return authmethod;
	}
	
	*/
	
	public void setAuthmethod(Integer authmethod) {
		this.authmethod = authmethod;
	}
	public Integer getAuthmethod() {
		return authmethod;
	}
	
	
	public UserRate getUserRate() {
		return userRate;
	}
	public void setUserRate(UserRate userRate) {
		this.userRate = userRate;
	}
	public User(){

	}
	public User(Integer id){
		this.setId(id);
	}
	

	public User(UserDTO userDTO){
		this.setId(userDTO.getId());
		this.setLogin(userDTO.getLogin());
		this.setPassword(userDTO.getPassword());
		this.setFirstName(userDTO.getFirstName());
		this.setSecondName(userDTO.getSecondName());
		this.setEmail(userDTO.getEmail());
		this.setNote(userDTO.getNote());
		this.setLang(userDTO.getLang());
		this.setStatus(userDTO.getStatus());
		this.setStart(userDTO.getStart());
		this.setFinish(userDTO.getFinish());
		this.setUpdated(userDTO.getUpdated());
		this.setAuthmethod(userDTO.getAuthmethod().getId());
	}
	
	public UserDTO getUserDTO(){
		
		UserDTO userDTO =	new UserDTO();
		userDTO.setId(this.getId());
		userDTO.setLogin(this.getLogin());
		userDTO.setFirstName(this.getFirstName());
		userDTO.setSecondName(this.getSecondName());
		userDTO.setEmail(this.getEmail());
		userDTO.setNote(this.getNote());
		userDTO.setLang(this.getLang());
		userDTO.setStatus(this.getStatus());
		userDTO.setStart(this.getStart());
		userDTO.setFinish(this.getFinish());
		userDTO.setUpdated(this.getUpdated());
		userDTO.setAuthmethod(new AuthmethodDTO(this.getAuthmethod()));
		return userDTO;
	}
	
	@Override
	public String toString(){
		StringBuilder result = new StringBuilder();
		result.append(this.getClass());
		result.append("__");
		result.append(this.getId());
		result.append("__");
		result.append(this.getFullName());
		result.append("__");
		result.append(this.getLogin());
		result.append("__");
		result.append(this.getFirstName());
		result.append("__");
		result.append(this.getSecondName());
		result.append("__AuthMethod:");
		result.append(this.getAuthmethod());
		
	return 	result.toString();
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	/**
	 * This equals suxx
	 * 
	 */
	
		@Override
		public boolean equals(Object obj) {
			User other = (User) obj;
			if(id != other.getId()){
				return false;
			}
				return true;	
		}
	public Branch getBranch() {
		return branch;
	}
	public void setBranch(Branch branch) {
		this.branch = branch;
	}
	

	

}
