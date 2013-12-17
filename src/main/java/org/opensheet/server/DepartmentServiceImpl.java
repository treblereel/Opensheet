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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import org.hibernate.Hibernate;
import org.opensheet.client.dto.BranchDTO;
import org.opensheet.client.dto.DepartmentDTO;
import org.opensheet.client.services.DepartmentService;
import org.opensheet.server.dao.AssignmentDAO;
import org.opensheet.server.dao.DepartmentDAO;
import org.opensheet.server.dao.UserDAO;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Branch;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.User;
import org.opensheet.shared.model.BaseModel.DepartmentBaseModel;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@Service("DepartmentService")
public class DepartmentServiceImpl extends RemoteServiceServlet implements DepartmentService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	DepartmentDAO departmentDAO;
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	private AssignmentDAO assignmentDAO;
	

	
		/*params:
		 * String s -> active/inactive/all
		 * 
		 * (non-Javadoc)
		 * @see org.opensheet.client.services.DepartmentService#getDepartments(java.lang.String)
		 */
	
	
	  @Override
	  @Transactional
	  public List<DepartmentDTO> getDepartments(String s) {
		List<Department> dapartments = departmentDAO.listDepartment(s);
		List<DepartmentDTO> dapartmentDTOs = new ArrayList<DepartmentDTO>(dapartments != null ? dapartments.size():0);
		for(Department d: dapartments){
			DepartmentDTO departmentDTO = new DepartmentDTO(d);
			departmentDTO.setOwner(d.getOwner().getUserDTO());
			departmentDTO.getOwner().setFullName(); 
			dapartmentDTOs.add(departmentDTO);
		}
	  return dapartmentDTOs;
	
	  }
	
	
	@Override
	  public List<DepartmentBaseModel> getDepartmentsBaseModel(String s) {
		List<Department> departmens =  departmentDAO.listDepartment(s);
		List<DepartmentBaseModel> departmensBaseModel = new ArrayList<DepartmentBaseModel>(departmens != null ? departmens.size() : 0);
		
		for(Department d: departmens){
			departmensBaseModel.add(new DepartmentBaseModel(d.getId(),d.getName()));
			}
	  return departmensBaseModel;
	  }
	
	
	
	
	@Override
	public void updateDepartment(DepartmentDTO  departmentDTO) {
		 Department  department = new  Department(departmentDTO);
		 department.setOwner(departmentDTO.getOwner().getUser());
		 department.setBranch(new Branch(departmentDTO.getBranch().getId()));
		 departmentDAO.updateDepartment(department);
	}
	
	@Override
	public void addDepartment( DepartmentDTO  departmentDTO) {
		 Department  department = new  Department(departmentDTO);
		 department.setOwner(departmentDTO.getOwner().getUser());
		 department.setBranch(new Branch(departmentDTO.getBranch().getId()));
		 departmentDAO.addDepartment(department);
	}

	/** Integer assignmentId  is an assignment id
	 *  Boolean criteria   : if true get only checked assignment list
	 * 		if false, get checked and unchecked assignments.
	 */
	
	
	@Override
	@Transactional
	public List<BaseModel> getDepartmentsBaseModelByAssignemnt(Integer assignmentId,Boolean criteria) {
		Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
		List<Department> assignedDeps = assignment.getDepartments();
		List<BaseModel> bmList = new ArrayList<BaseModel>();
		List<Department>  depList;
		
		if(criteria == true){
			if(assignment.getLevel() == 0){
				depList = 	departmentDAO.listDepartment("1");
			}else{
				depList = assignment.getParent().getDepartments();
			}
			
			
			
			for(Department dep: depList){
				BaseModel bm = new BaseModel();
				bm.set("name", dep.getName());
				bm.set("id", dep.getId());
				
				if(assignedDeps.contains(dep)){
					bm.set("selected", true);
				}else{
					bm.set("selected", false);
				}
				
				bmList.add(bm);
			}
		}else{
			for(Department dep: assignedDeps){
				BaseModel bm = new BaseModel();
				bm.set("name", dep.getName());
				bm.set("id", dep.getId());
				bmList.add(bm);
			}
		}
		
		return bmList;
	}


	@Override
	@Transactional
	public void setAssignemntToDepartment(Integer assignmentId,	List<BaseModel> departments) {
			Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
		
		if(assignment.getStatus() == true)		{
		List<Department> list = new ArrayList<Department>(departments.size());
			for(BaseModel bm: departments){
				list.add(departmentDAO.getDepartmentById((Integer) bm.get("id")));
			}
			departmentDAO.setAssignemntToDepartment(assignment, list);
		}
	}

	/**
	 * 
	 * @param  status  active/inactive/all   "1","0","any"
	 * @param  branch  id of the branch (9999999 is for all)
	 * 
	 * @see org.opensheet.client.services.DepartmentService#getDepartments(java.lang.String)
	 */

	@Override
	@Transactional
	public List<DepartmentDTO> getDepartmentsByBranch(String status,Integer branch) {
		List<Department> dapartments = departmentDAO.listDepartmentByBranch(status,branch);
		List<DepartmentDTO> dapartmentDTOs = new ArrayList<DepartmentDTO>(dapartments != null ? dapartments.size():0);
		for(Department d: dapartments){
			DepartmentDTO departmentDTO = new DepartmentDTO(d);
			departmentDTO.setOwner(d.getOwner().getUserDTO());
			departmentDTO.getOwner().setFullName(); 
			departmentDTO.setBranch(new BranchDTO(d.getBranch()));
			dapartmentDTOs.add(departmentDTO);
		}
	  return dapartmentDTOs;
	}


	@Override
	@Transactional
	public List<DepartmentDTO> getDepartments() {
		User user = userDAO.whoIam();
		if(user.getPermission().getDm()==true){
			List<Department> dapartments = departmentDAO.getDepartment(user);
			List<DepartmentDTO> dapartmentDTOs = new ArrayList<DepartmentDTO>(dapartments != null ? dapartments.size():0);
			for(Department d: dapartments){
		//		DepartmentDTO departmentDTO = new DepartmentDTO(d);
				dapartmentDTOs.add(new DepartmentDTO(d));
			}
		  return dapartmentDTOs;
		}
		return null;
	}


	@Override
	@Transactional
	public List<BaseModel> getUsersByDepartmentAndByAssignment(
			Integer departmentId, Integer assignmentId) {
		 
		 Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
		 Department department = departmentDAO.getDepartmentById(departmentId);
		
		 Hibernate.initialize(department.getUsers());
		 
		 List<User> checkList = new ArrayList<User>();
		 List<User> users = (List<User>) departmentDAO.getUserByAssignmentAndByDepartment(assignment,department);
		 List<BaseModel> list = new  ArrayList<BaseModel>(users.size());

		 if(assignment.getLevel() == 0){
			 for(User u:  department.getUsers()){
				 checkList.add(u);
			 }
		 }else{
			 checkList =  (List<User>) departmentDAO.getUserByAssignmentAndByDepartment(assignment.getParent(),department);
		 }
		 
		 
		 for(User u:  checkList){
			if(u.getStatus() == true){
				 BaseModel bm = new  BaseModel();
				 bm.set("name", u.getFullName());
				 bm.set("id", u.getId());
				
				 if(users.contains(u)){
					 bm.set("selected", true);
				 }else{
					 bm.set("selected", false); 
				 }
				 list.add(bm);
			}
		 }
		 
		 
	
		return list;
	}


	@Override
	@Transactional
	public void setUsersByDepartmentAndByAssignment(List<BaseModel> usersBaseModel, Integer departmentId, Integer assignmentId) {
		 Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
		 Department department = departmentDAO.getDepartmentById(departmentId);
		 List<User> usersDepartmentSelected = new ArrayList<User>(usersBaseModel.size());
		 for(BaseModel bm: usersBaseModel){
			 usersDepartmentSelected.add(userDAO.getUser(Integer.parseInt(bm.get("id").toString())));
		 }
		 userDAO.setUsersByDepartmentAndByAssignment(usersDepartmentSelected, department, assignment);
	}


	@Override
	@Transactional
	public List<BaseModel> getDepartmentsBaseModelByAssignemntAndByBranch(Integer assignmentId,Integer branchId) {
		Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
		List<Department> departmentAll;
		List<Department> assignedDeps = assignment.getDepartments();
		List<BaseModel> bmList = new ArrayList<BaseModel>();
		
		//Fucking walkarount !!! somethings wrong with dep.equals
		List<Integer> list = new ArrayList<Integer>(assignedDeps.size());
		for(Department dt: assignedDeps){
			list.add(dt.getId());
		}
		
		if(branchId == 9999999){
			if(assignment.getLevel() == 0){
				departmentAll = departmentDAO.listDepartment("1");
			}else{
				departmentAll = assignment.getParent().getDepartments();	
			}
		}else{
			if(assignment.getLevel() == 0){
				departmentAll =  departmentDAO.listDepartmentByBranch("1",branchId);
			}else{
				departmentAll= new ArrayList<Department>();
				List<Department> temp = assignment.getParent().getDepartments();
				for(Department d: temp){
					if(d.getBranch().getId() == branchId){
						departmentAll.add(d);	
					}
				}
			}
		}
		for(Department dep: departmentAll){
			BaseModel bm = new BaseModel();
			bm.set("name", dep.getName());
			bm.set("id", dep.getId());
			if(list.contains(dep.getId())){
				bm.set("selected", true);
			}else{
				bm.set("selected", false);
			}
			bmList.add(bm);
		}
		return bmList;
	}


	
	
//	Убирать у детей департамент убранный у верхнего проекта
	@Override
	@Transactional
	public void setDepartmentToAssignmentByBranch(List<BaseModel> departmentList,Integer assignmentId, Integer branchId) {
		Assignment  assignment =  assignmentDAO.getAssignmentById(assignmentId);
		List<Department> departmentAll;
		List<Department> departmentOld = assignment.getDepartments() ;
		Map<Assignment,List<Department>> diff = new HashMap<Assignment,List<Department>>();
		List<Department> departments = new ArrayList<Department>();
		for(BaseModel bm: departmentList){
			departments.add(departmentDAO.getDepartmentById(Integer.parseInt(bm.get("id").toString())));
		}
		List<Department> result = new ArrayList<Department>();
		if(assignment.getLevel() == 0){
			if(branchId == 9999999){
				assignment.setDepartments(departments);
				departmentOld.removeAll(departments);
			}else{
				departmentAll = departmentDAO.listDepartment("1");
				for(Department d: departmentAll){
					if(d.getBranch().getId() != branchId){
						result.add(d);
					}
				}
				result.addAll(departments);
				assignment.setDepartments(result);
			}
			for(Assignment a: assignment.getChildren()){
				for(Department d: a.getDepartments()){
					if(departmentOld.contains(d)){
						if(diff.containsKey(a)){
							diff.get(a).add(d);
						}else{
							List<Department> depp = new ArrayList<Department>();
							depp.add(d);
							diff.put(a, depp);
						}
					}
				}
			}
			for(Map.Entry<Assignment,List<Department>> value: diff.entrySet()){
				Assignment a = value.getKey();
				for(Department dt: value.getValue()){
					a.getDepartments().remove(dt);
				}
			}	
		}else{
			if(branchId == 9999999){
				assignment.setDepartments(departments);
			}else{
				departmentAll= new ArrayList<Department>();
				List<Department> temp = assignment.getDepartments();
				for(Department d: temp){
					if(d.getBranch().getId() != branchId){
						departmentAll.add(d);	
					}
				}
				departmentAll.addAll(departments);
				assignment.setDepartments(departmentAll);
			}
		}
	}
	
	
}
