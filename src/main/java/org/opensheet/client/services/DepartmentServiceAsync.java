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
import com.google.gwt.user.client.rpc.AsyncCallback;


public interface DepartmentServiceAsync {
	 public void getDepartmentsBaseModel(String criteria,AsyncCallback<List<DepartmentBaseModel>> callback);
	 public void getDepartmentsBaseModelByAssignemnt(Integer assignmentId,Boolean criteria,AsyncCallback<List<BaseModel>> callback);
	 public void getDepartmentsBaseModelByAssignemntAndByBranch(Integer assignmentId,Integer branch,AsyncCallback<List<BaseModel>> callback);
	 public void setDepartmentToAssignmentByBranch(List<BaseModel> departments, Integer assignmentId,Integer branch,AsyncCallback<Void> callback);

	 public void setAssignemntToDepartment(Integer assignemntId,List<BaseModel> departments, AsyncCallback<Void> asyncCallback);
	 public void getDepartments(String criteria,AsyncCallback<List<DepartmentDTO>> callback);
	 public void getDepartments(AsyncCallback<List<DepartmentDTO>> callback);
	 public void getDepartmentsByBranch(String criteria,Integer branch,AsyncCallback<List<DepartmentDTO>> callback);
     public void updateDepartment(DepartmentDTO department, AsyncCallback<Void> asyncCallback);
     public void addDepartment(DepartmentDTO department, AsyncCallback<Void> asyncCallback);
     
     public void getUsersByDepartmentAndByAssignment(Integer departmentId,Integer assignmentId,AsyncCallback<List<BaseModel>> callback);
     public void setUsersByDepartmentAndByAssignment(List<BaseModel> users,Integer departmentId,Integer assignmentId,AsyncCallback<Void> callback);


}
