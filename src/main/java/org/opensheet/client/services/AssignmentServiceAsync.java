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


import java.util.Date;
import java.util.List;

import org.opensheet.client.dto.AssignmentDTO;
import org.opensheet.client.dto.DepartmentDTO;
import org.opensheet.client.dto.UserDTO;
import org.opensheet.client.dto.grid.AssignmentGridTemplate;
import org.opensheet.shared.model.BaseModel.UserBaseModel;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AssignmentServiceAsync {
	
    public void getAssignmentDTOById(Integer id, AsyncCallback<AssignmentDTO> asyncCallback);
    public void getAssignmentDTOs(Integer type ,Boolean status,Integer branch, AsyncCallback<AssignmentGridTemplate> asyncCallback);
    public void updateAssignment(AssignmentDTO assignmentDTO,AsyncCallback<BaseModel> asyncCallback);
    public void addAssignment(AssignmentDTO assignmentDTO,AsyncCallback<Void> asyncCallback);
    public void changeStatusAssignment(String id,AsyncCallback<Void> asyncCallback);
    public void getAssignmentUsers(AssignmentDTO assignmentDTO,Boolean status,AsyncCallback<List<UserBaseModel>> asyncCallback );
    public void getAssignmentUsers(Integer assignmentId,AsyncCallback<List<UserDTO>> asyncCallback );
    public void getAssignmentUsersByDepartmentByBranch(AssignmentDTO assignmentDTO,DepartmentDTO departmentDTO,Boolean status,Integer branch,AsyncCallback<List<UserBaseModel>> asyncCallback );
    public void updateAssignmentUsers(List<UserBaseModel> users,AssignmentDTO assignmentDTO,DepartmentDTO departmentDTO,AsyncCallback<Void> asyncCallback);
    public void getDisabledAssignment(UserDTO userDTO, Date date,AsyncCallback<List<Integer>> asyncCallback );
    public void getProjectManagerAssignments(Boolean status,AsyncCallback<AssignmentGridTemplate> asyncCallback );
    public void getDepartmentManagerAssignments(DepartmentDTO departmentDTO,AsyncCallback<AssignmentGridTemplate> asyncCallback );
    public void generateIndexForNewAssignment(Integer type,Integer branch,Integer rootAssignment,AsyncCallback<String> asyncCallback);
    public void getAssignmentsByDepartmentAndByType(Integer departmentId,Integer typeId,AsyncCallback<AssignmentGridTemplate> asyncCallback);
    public void getAssignmentsByBranchAndByType(Integer branchId,Integer typeId,Boolean needAnyValue,AsyncCallback<List<AssignmentDTO>> asyncCallback);
}
