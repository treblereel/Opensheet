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
package org.opensheet.server.dao;

import java.util.List;

import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.AssignmentUserDetail;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.User;

public interface UserDAO {
	
//	@Secured("ROLE_ADMIN")
	public List<User> listUser(String s);
	public List<User>  getUsersByBranch(String s,Integer branch);
	
	public List<User>  getUsersByDepartment(String s,Integer department);
	public List<User>  getUsersByBranchAndByDepartment(String s,Integer branch,Integer department);

	public void addUser(User user);
	public User getUser(String user);
	public User getUser(Integer id);
	public User whoIam();
	public void updateUser(User user);
	public void setUserInternalRate(User user,Integer rate);
	public void updateUserPermission(User user);
	public List<User> getUsersByRole(String s);
	public List<Assignment> getUserAssignment(User user);
	public List<Assignment> getUserSubAssignment(User user,Assignment assignment);
	public List<Department> getManagedDepartments(User user);
	public AssignmentUserDetail  getUserAssignmentDetail(User user,Assignment assignment);
	public void  setUserAssignmentDetail(AssignmentUserDetail assignmentUserDetail);
	public void setUsersByDepartmentAndByAssignment(List<User> users,Department department, Assignment assignment);
	public void setLang(User user);
	
}
