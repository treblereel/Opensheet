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

import org.opensheet.shared.model.User;

import com.extjs.gxt.ui.client.data.BeanModelTag;

public class UserDTO implements Serializable, BeanModelTag{
	

private static final long serialVersionUID = 1L;
	
	
	private Integer id;
	private String  login;
	private String  password;
	private String  firstName;
	private String  secondName;
	private String  email;
	private String  note;
	private String  lang;
	private String  fullName;
	private Boolean  status;
	private BranchDTO branch;
	private DepartmentDTO department =null;
	private PermissionDTO permission = null;
	private Date    start;
	private Date    finish;
	private Date    updated;
	private AuthmethodDTO authmethod;
	private UserRateDTO userRateDTO;
	
	
	

	
	public UserDTO(){
		}
	
	public UserDTO(Integer id){
		this.setId(id);
	}
	
	public User getUser(){
		return new User(this);
	}
	
	/**FIX PASSWORD, DONT SEND IT OVET NET
	 * 
	 * 
	 * @param user
	 */
	
	
	public UserDTO(User user){
		this.setId(user.getId());
		this.setLogin(user.getLogin());
		this.setFirstName(user.getFirstName());
		this.setSecondName(user.getSecondName());
		this.setFullName();
		this.setEmail(user.getEmail());
		this.setNote(user.getNote());
		this.setLang(user.getLang());
		this.setStatus(user.getStatus());
		this.setStart(user.getStart());
		this.setFinish(user.getFinish());
		this.setUpdated(user.getUpdated());
		this.setAuthmethod(new AuthmethodDTO(user.getAuthmethod()));
		this.fullName = this.secondName + " " + this.firstName;
		this.setBranch(new BranchDTO(user.getBranch()));
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
	
	public void setFullName(){
		this.fullName = secondName + " " + firstName ;
	}
	
	public String getfullName() {
		return fullName;
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
	

	
	public DepartmentDTO getDepartment() {
		return department;
	}
	public void setDepartment(DepartmentDTO department) {
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
	
	


	public void setPermission(PermissionDTO permission) {
		this.permission = permission;
	}


	public PermissionDTO getPermission() {
		return permission;
	}

	public void setAuthmethod(AuthmethodDTO authmethod) {
		this.authmethod = authmethod;
	}

	public AuthmethodDTO getAuthmethod() {
		return authmethod;
	}

	public UserRateDTO getUserRateDTO() {
		return userRateDTO;
	}

	public void setUserRateDTO(UserRateDTO userRateDTO) {
		this.userRateDTO = userRateDTO;
	}

	public BranchDTO getBranch() {
		return branch;
	}

	public void setBranch(BranchDTO branch) {
		this.branch = branch;
	}

	
	
	

}
