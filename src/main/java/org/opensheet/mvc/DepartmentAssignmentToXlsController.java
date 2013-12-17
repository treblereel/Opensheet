package org.opensheet.mvc;

import java.util.ArrayList;
import java.util.List;

import org.opensheet.server.dao.AssignmentDAO;
import org.opensheet.server.dao.BranchDAO;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Branch;
import org.opensheet.shared.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping("/departmentassignmenttoxls.htm")
public class DepartmentAssignmentToXlsController {
	
	@Autowired
	private AssignmentDAO assignmentDAO;

	@Autowired
	private BranchDAO branchDAO;
	
	
	@RequestMapping(method = RequestMethod.GET)
	protected ModelAndView handleRequestInternal(
			@RequestParam("assignment_id")    Integer assignmentId,
			@RequestParam("branch_id")    Integer branchId
			) throws Exception {
		
		Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
		Branch branch;
		if(branchId == 9999999){
			branch = new Branch(9999999);
		}else{
			branch = branchDAO.getBranchById(branchId);
		}
		List<Department> departments = assignmentDAO.getDepartmentsByBranch(assignment, branch);
		
		
		return new ModelAndView("departmentassignmenttoxlsview","Departments",departments);
		
	
		
		
	}
	

}
