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

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.User;
import org.opensheet.shared.model.BaseModel.UserBaseModel;

public interface AssignmentUserDAO {
	
	public List<User> getAssignmentUsers(Assignment assignment,Boolean assigned);
	public List<User> getAssignmentUsers(Assignment assignment);
	public void updateAssignmentUsers(List<UserBaseModel> users,Assignment assignment,Department department);
	public List<User> getAssignmentUsersByDepartment(Assignment assignment,Department department);
	public List<User> getAssignmentUsersByDepartment(Assignment assignment,Department department,Boolean assigned);
	public Map<Assignment,List<Assignment>> getAssignedAssignmentByUser(User user);
	public Map<Assignment,List<Assignment>> getSheetAssignmentByUser(User user,Calendar cal);
	public Map<Assignment,List<Assignment>> getMergedAssignmentsByUser(User user,Calendar cal);
	public List<Assignment> getDiffAssignmentsByUser(User user,Calendar cal);
	public List<Assignment> getProjectManagerAssignments(User user);
    public void  setAssignmentToUser(List<Assignment> Assignment, User user);

}
