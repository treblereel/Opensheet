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
package org.opensheet.client.services;

import java.util.List;

import org.opensheet.client.dto.DepartmentDTO;
import org.opensheet.shared.model.BaseModel.DepartmentBaseModel;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("service/DepartmentService")
public interface DepartmentService extends RemoteService {
	
	public  List<DepartmentBaseModel> getDepartmentsBaseModel(String criteria);
	public  List<DepartmentDTO> getDepartments(String criteria);
	
	/**  if user is dm, get his departments	 */
	public  List<DepartmentDTO> getDepartments();

	public  List<DepartmentDTO> getDepartmentsByBranch(String criteria,Integer branch);

	public  List<BaseModel> getDepartmentsBaseModelByAssignemnt(Integer assignmentId,Boolean criteria);
	public  List<BaseModel> getDepartmentsBaseModelByAssignemntAndByBranch(Integer assignmentId,Integer branch);
	public  void setDepartmentToAssignmentByBranch(List<BaseModel> departments, Integer assignmentId,Integer branch);

	
	public  void setAssignemntToDepartment(Integer assignemntId,List<BaseModel> departments);
	
    public void updateDepartment(DepartmentDTO department);
    public void addDepartment(DepartmentDTO department);
    
    public List<BaseModel> getUsersByDepartmentAndByAssignment(Integer departmentId,Integer assignmentId);
    public void setUsersByDepartmentAndByAssignment(List<BaseModel> users,Integer departmentId,Integer assignmentId);

}
