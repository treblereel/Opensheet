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
package org.opensheet.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.hibernate.Hibernate;
import org.opensheet.client.dto.AssignmentDTO;
import org.opensheet.client.dto.BranchDTO;
import org.opensheet.client.dto.DepartmentDTO;
import org.opensheet.client.dto.UserDTO;
import org.opensheet.client.dto.grid.AssignmentGridTemplate;
import org.opensheet.client.services.AssignmentService;
import org.opensheet.server.dao.AssignmentDAO;
import org.opensheet.server.dao.AssignmentUserDAO;
import org.opensheet.server.dao.BranchDAO;
import org.opensheet.server.dao.DepartmentDAO;
import org.opensheet.server.dao.UserDAO;
import org.opensheet.server.security.CheckUserContext;
import org.opensheet.server.utils.Comparators;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Branch;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.User;
import org.opensheet.shared.model.BaseModel.UserBaseModel;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@Service("AssignmentService")
public class AssignmentServiceImpl extends RemoteServiceServlet implements AssignmentService{
	
	@Autowired
	private AssignmentUserDAO assignmentUserDAO;
	@Autowired
	private DepartmentDAO departmentDAO;
	
	@Autowired	private CheckUserContext checkUserContext;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private AssignmentDAO assignmentDAO;	
	@Autowired
	private BranchDAO branchDAO;
	

	
	
	@Secured({"ROLE_ADMIN"})
	@Override
	public AssignmentGridTemplate getAssignmentDTOs(Integer type,Boolean status,Integer branch){
			return assignmentDAO.getAssignmentDTOs(type,status,branch);
		
	}
	
	@Secured({"ROLE_ADMIN","ROLE_PM"})
	@Override
	@Transactional
	public BaseModel updateAssignment(AssignmentDTO assignmentDTO){
		Assignment assignment = new  Assignment(assignmentDTO);
		
		if(assignmentDTO.getOwner() != null)
			assignment.setOwner(assignmentDTO.getOwner().getUser());
		
		assignment.setBranch(new Branch(assignmentDTO.getBranch()));
		return assignmentDAO.updateAssignmnet(assignment);
	}

	
	@Secured({"ROLE_ADMIN","ROLE_PM"})
	@Override
	public void addAssignment(AssignmentDTO assignmentDTO){
		Assignment assignment = new Assignment(assignmentDTO);
		if(assignmentDTO.getLevel() != 0){
			assignment.setParent(assignmentDTO.getParent().getAssignment());
		}
		if(assignmentDTO.getLevel() == 0){
			assignment.setOwner(assignmentDTO.getOwner().getUser());
			assignment.setBranch(new Branch(assignmentDTO.getBranch()));

		}
		assignmentDAO.addAssignmnet(assignment);

		
	}
	
	@Secured({"ROLE_ADMIN","ROLE_PM"})
	@Override
	public void changeStatusAssignment(String id){
		assignmentDAO.changeStatusAssignment(id);
	}
	
	
	@Secured({"ROLE_ADMIN"})
	@Override
	@Transactional
	public List<UserBaseModel> getAssignmentUsers(AssignmentDTO assignmentDTO,Boolean status){
		List<User> listUsers =  assignmentUserDAO.getAssignmentUsers(new Assignment(assignmentDTO),status);
		if(listUsers != null){
		List<UserBaseModel> usersBaseModel = new ArrayList<UserBaseModel>(listUsers != null ? listUsers.size() : 0);
		for( User u:  listUsers){
			usersBaseModel.add(new UserBaseModel(u.getId(),u.getFullName()));
		}
		return usersBaseModel;
		}else{
			return null;
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@Override
	@Transactional
	public List<UserBaseModel> getAssignmentUsersByDepartmentByBranch(AssignmentDTO assignmentDTO,DepartmentDTO departmentDTO,Boolean status,Integer branch){
	
		
		List<User> listUsers = null;
			if(departmentDTO.getId() == 99999999){
				listUsers =  assignmentUserDAO.getAssignmentUsers(new Assignment(assignmentDTO),status);
			}	else {
				
				listUsers =  assignmentUserDAO.getAssignmentUsersByDepartment(new Assignment(assignmentDTO),new Department(departmentDTO),status);
			}
		if(listUsers != null){
			List<UserBaseModel> usersBaseModel = new ArrayList<UserBaseModel>(listUsers != null ? listUsers.size() : 0);
			for( User u:  listUsers){
				usersBaseModel.add(new UserBaseModel(u.getId(),u.getFullName()));
			}
			return usersBaseModel;
		}
		
		return Collections.emptyList();
	}
	
	
	@Secured({"ROLE_ADMIN"})
	@Override
	@Transactional
	public void updateAssignmentUsers(List<UserBaseModel> users,AssignmentDTO assignmentDTO,DepartmentDTO departmentDTO){
		assignmentUserDAO.updateAssignmentUsers(users,new Assignment(assignmentDTO),new Department(departmentDTO));
	}


	
	/**This method return the list of assignments which has been associated 
	 * with current User by has been removed after. Return List of Id's of this
	 * assignments.
	 * @param
	 * userDTO current user (DTO object)
	 * date    current date of timesheet
	 * @return List<Integer>
	 * 
	 */
	
	@Secured({"ROLE_USER"})
	@Override
	@Transactional
	public List<Integer> getDisabledAssignment(UserDTO userDTO, Date date) {
		User user = userDAO.getUser(userDTO.getId());

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		List<Assignment>  disabledAssignments = assignmentUserDAO.getDiffAssignmentsByUser(user, cal);
		List<Integer> disabledAssignmentsId  = new ArrayList<Integer>();
		for(Assignment dis: disabledAssignments){
			disabledAssignmentsId.add(dis.getId());
	//	System.out.println(" desabled  " + dis.getName());
		
		}
		return disabledAssignmentsId;
	}

	@Secured({"ROLE_ADMIN","ROLE_PM"})
	@Override
	@Transactional
	public AssignmentGridTemplate getProjectManagerAssignments(Boolean status) {
		User u = checkUserContext.getUser(SecurityContextHolder.getContext());
		List<Assignment> assignments = assignmentUserDAO.getProjectManagerAssignments(u);
		Collections.sort(assignments, Comparators.ComparatorAssignmentName);
		AssignmentGridTemplate root = new AssignmentGridTemplate("root",true,100000,"nope",false,false);
		for(Assignment a: assignments){
			if(status == true){
				if(a.getStatus() == true){
						if(a.getChildren().isEmpty() == true){
							root.add(new AssignmentGridTemplate(a.getName(),a.getStatus(),a.getId(),a.getIndex(),false,false));
						}else{
							ArrayList<AssignmentGridTemplate> assignmentGridTemplate = new ArrayList<AssignmentGridTemplate>();
							for(Assignment child: a.getChildren()){
								if(a.getChildren().size() >=2 && child.getStatus() == true && child.getFirst() != true){
									assignmentGridTemplate.add(new AssignmentGridTemplate(child.getName(),child.getStatus(),child.getId(),child.getIndex(),true,false));
								}
							}
							root.add(new  AssignmentGridTemplate(a.getName(), a.getStatus(),a.getId(),a.getIndex(),assignmentGridTemplate,false,true));
						}
				}
			}else{
				if(a.getChildren().isEmpty() == true){
					root.add(new AssignmentGridTemplate(a.getName(),a.getStatus(),a.getId(),a.getIndex(),false,false));
				}else{
					ArrayList<AssignmentGridTemplate> assignmentGridTemplate = new ArrayList<AssignmentGridTemplate>();
					for(Assignment child: a.getChildren()){
						if(child.getFirst() != true){
							assignmentGridTemplate.add(new AssignmentGridTemplate(child.getName(),child.getStatus(),child.getId(),child.getIndex(),true,false));
						}
					}
					root.add(new AssignmentGridTemplate(a.getName(),a.getStatus(),a.getId(),a.getIndex(),assignmentGridTemplate,false,true));
					assignmentGridTemplate = new ArrayList<AssignmentGridTemplate>();
				}
			}
		}
		return root;
	}

	@Secured({"ROLE_ADMIN","ROLE_DM"})
	@Override
	@Transactional
	public AssignmentGridTemplate getDepartmentManagerAssignments(DepartmentDTO departmentDTO) {
	   AssignmentGridTemplate root = new AssignmentGridTemplate("root",true,100000,"nope",false,false);
	   Map<Assignment, List<Assignment>> map = assignmentDAO.getAssignmentByDepartment(new Department(departmentDTO.getId()));
	   for(Map.Entry<Assignment, List<Assignment>> entety: map.entrySet()){
			if(entety.getValue().isEmpty()){
				Assignment a = entety.getKey();
				AssignmentGridTemplate agt
			   = new AssignmentGridTemplate(a.getName(),a.getStatus(),a.getId(),a.getIndex(),false,false,a.getOwner().getFullName(),0,0);
				root.add(agt);
			}else{
				Assignment assignment = entety.getKey();
				List<Assignment> list = entety.getValue();
				List<AssignmentGridTemplate> agts = new ArrayList<AssignmentGridTemplate>(list.size());
				for(Assignment a: list){
					agts.add(new AssignmentGridTemplate(a.getName(),a.getStatus(),a.getId(),a.getIndex(),true,false,a.getOwner().getFullName(),0,0));
				}
				AssignmentGridTemplate agt
				   = new AssignmentGridTemplate(assignment.getName(),assignment.getStatus(),assignment.getId(),assignment.getIndex(),false,true,assignment.getOwner().getFullName(),0,0,(ArrayList<AssignmentGridTemplate>) agts);
					root.add(agt);
			}
		}
		return root;
	}

	@Secured({"ROLE_ADMIN","ROLE_PM"})
	@SuppressWarnings("deprecation")
	@Override
	@Transactional
	public String generateIndexForNewAssignment(Integer type, Integer branchId,
			Integer rootAssignment) {
		Map<Integer,String> typeToIndex = new HashMap<Integer,String>();
		typeToIndex.put(0, "P");
		typeToIndex.put(1, "T");
		typeToIndex.put(2, "O");
		typeToIndex.put(3, "Off");
		if(rootAssignment == 0){
			Branch branch  = branchDAO.getBranchById(branchId);
			Integer step = assignmentDAO.getAssignmentByBranch(branch).size();
			step++;
			Date date = new Date();
			return branch.getIndex() + "-" + (date.getYear()+1900) + "-" + step + "-" +  typeToIndex.get(type);
		}else{
			Assignment assignment = assignmentDAO.getAssignmentById(rootAssignment);
			Integer step = assignment.getChildren().size();
			step++;
			return assignment.getIndex() + "_" + step;
		}
	}

	@Secured({"ROLE_ADMIN","ROLE_FD"})
	@Override
	@Transactional
	public AssignmentGridTemplate getAssignmentsByDepartmentAndByType(Integer departmentId, Integer typeId) {
		   AssignmentGridTemplate root = new AssignmentGridTemplate("root",true,100000,"nope",false,false);
		   Department department = departmentDAO.getDepartmentById(departmentId);
		   List<Assignment> assignments = department.getAssignments();
		   
		   for(Assignment assignment: assignments){
			   if(assignment.getType() == typeId && assignment.getStatus() == true){
				   AssignmentGridTemplate assignmentGridTemplate = new AssignmentGridTemplate(
						   assignment.getId(),
						   assignment.getIndex(),
						   assignment.getName(),
						   assignment.getOwner().getFullName(),
						   assignment.getType(),
						   assignment.getStatus(),
						   true,
						   false
						   );
				   root.add(assignmentGridTemplate);
			   }
		   }
		return root;
	}

	@Secured({"ROLE_ADMIN","ROLE_FD"})
	@Override
	@Transactional
	public List<AssignmentDTO> getAssignmentsByBranchAndByType(Integer branchId, Integer typeId,Boolean needAnyValue) {
		AssignmentDTO dto = new AssignmentDTO();
		dto.setId(9999999);
		dto.setName("Any");
		
		if(branchId == null && typeId == null && needAnyValue == true ){
			List<AssignmentDTO> list = new ArrayList<AssignmentDTO>();
			list.add(dto);
			return list;
		}
	
		Branch branch = branchDAO.getBranchById(branchId);
		List<Assignment> assignments = assignmentDAO.getAssignmentByBranchAndByType(branch, typeId);
		List<AssignmentDTO> assignmentDTOs = new ArrayList<AssignmentDTO>();
		if(needAnyValue == true ){
			assignmentDTOs.add(dto);
		}
		for(Assignment a: assignments){
			assignmentDTOs.add(new AssignmentDTO(a));
		}
		return assignmentDTOs;
	}

	@Secured({"ROLE_ADMIN","ROLE_PM"})
	@Override
	public List<UserDTO> getAssignmentUsers(Integer assignmentId) {
		Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
		 List<User> users  = assignmentUserDAO.getAssignmentUsers(assignment);
		
		List<UserDTO> resultList  = new ArrayList<UserDTO>();
		Collections.sort(users,Comparators.ComparatorUserName);
		
		
		for(User u: users){
			if(u.getStatus() == true){
				Department d = departmentDAO.getDepartmentById(u.getDepartment().getId());
				UserDTO userDTO = new UserDTO(u);
				userDTO.setDepartment(new DepartmentDTO(d));
				resultList.add(userDTO);
			}
		}
		
		
		return resultList;
	}

	@Secured({"ROLE_USER"})
	@Transactional
	public AssignmentDTO getAssignmentDTOById(Integer id){
		Assignment assignment = assignmentDAO.getAssignmentById(id);
		AssignmentDTO a = new AssignmentDTO(assignment);
		a.setOwner(assignment.getOwner().getUserDTO());
		a.setBranch(new BranchDTO(assignment.getBranch()));
		return a;
	}
	
}
