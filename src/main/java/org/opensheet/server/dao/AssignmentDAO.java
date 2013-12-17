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
import java.util.Map;

import org.opensheet.client.dto.grid.AssignmentGridTemplate;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Branch;
import org.opensheet.shared.model.Department;

import com.extjs.gxt.ui.client.data.BaseModel;

public interface AssignmentDAO {


	
	public Assignment getAssignmentById(Integer id);
	
	public List<Assignment> getAssignments(Integer type);
	
	public AssignmentGridTemplate getAssignmentDTOs(Integer type,Boolean status,Integer branch);
	public List<Assignment> getRootAssignments(Integer type,Boolean status,Integer branch);
	public BaseModel updateAssignmnet(Assignment assignment);
	public void addAssignmnet(Assignment assignment);
	/**
	 * Does this Assignment has a Children?
	 * 
	 * @param assignemnt
	 * @return
	 */
	public Boolean hasChildren(Assignment assignment);
	public void changeStatusAssignment(String id);

	public  Assignment getAssignemntsDefaultTask(Assignment assignment);
	public Integer getSumHourAssignment(Assignment assignment); 
	public List<Assignment> getDefaultAssignment();
	public List<Assignment> getAssignmentByBranch(Branch branch);
	public List<Assignment> getAssignmentByBranchAndByType(Branch branch,Integer type);


	public Map<Assignment,List<Assignment>> getAssignmentByDepartment(Department department);
	public void setDepartment(Assignment assignment,List<Department> departments);

	public List<Department> getDepartmentsByBranch(Assignment assignment,Branch branch);
	
}
