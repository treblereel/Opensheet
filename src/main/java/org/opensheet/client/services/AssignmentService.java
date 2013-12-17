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
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;



@RemoteServiceRelativePath("service/AssignmentService")

	public interface AssignmentService extends RemoteService {
		
		public  AssignmentDTO getAssignmentDTOById(Integer id);
		public  AssignmentGridTemplate getAssignmentDTOs(Integer type,Boolean status,Integer branch);
		public BaseModel updateAssignment(AssignmentDTO assignmentDTO);
		public void addAssignment(AssignmentDTO assignmentDTO);
		public void changeStatusAssignment(String id);
	    public List<UserBaseModel> getAssignmentUsers(AssignmentDTO assignmentDTO,Boolean status);
	    public List<UserDTO> getAssignmentUsers(Integer assignmentId);
	    public List<UserBaseModel> getAssignmentUsersByDepartmentByBranch(AssignmentDTO assignmentDTO,DepartmentDTO departmentDTO,Boolean status,Integer branch); 
	    public void updateAssignmentUsers(List<UserBaseModel> users,AssignmentDTO assignmentDTO,DepartmentDTO departmentDTO);
	    public List<Integer> getDisabledAssignment(UserDTO userDTO, Date date);
	    public AssignmentGridTemplate getProjectManagerAssignments(Boolean status);
	    public AssignmentGridTemplate getDepartmentManagerAssignments(DepartmentDTO departmentDTO);
	    public String generateIndexForNewAssignment(Integer type,Integer branch,Integer rootAssignment);
	    
	    public AssignmentGridTemplate getAssignmentsByDepartmentAndByType(Integer departmentId,Integer typeId);
	    public List<AssignmentDTO> getAssignmentsByBranchAndByType(Integer branchId,Integer typeId,Boolean needAnyValue);
}
