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
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.User;


public interface DepartmentDAO {
	
	public List<Department> listDepartment(String s);
	public List<Department> getDepartment(User user);
	public List<Department> listDepartmentByBranch(String s,Integer branch);
	public void updateDepartment(Department department);
	public void addDepartment(Department department);
	public List<User> getUsers(Department department);
	public Department getDepartmentById(Integer id);
	public void setAssignemntToDepartment(Assignment assignment,List<Department> departments);
	
	public List<User> getUserByAssignmentAndByDepartment(Assignment assignment,Department department);
	public List<User> getDepartmentManagerUsers(User user);

}
