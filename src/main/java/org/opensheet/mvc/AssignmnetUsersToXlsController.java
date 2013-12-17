package org.opensheet.mvc;

import java.util.Collections;
import java.util.List;

import org.opensheet.server.dao.AssignmentDAO;
import org.opensheet.server.dao.AssignmentUserDAO;
import org.opensheet.server.utils.Comparators;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping("/userassignmenttoxls.htm")
public class AssignmnetUsersToXlsController {

	@Autowired
	private AssignmentDAO assignmentDAO;
	@Autowired
	private AssignmentUserDAO assignmentUserDAO;
	
	@RequestMapping(method = RequestMethod.GET)
	protected ModelAndView handleRequestInternal(
			@RequestParam("assignment_id")    Integer assignmentId
			) throws Exception {
		
		Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
	
		List<User> users  =  assignmentUserDAO.getAssignmentUsers(assignment);
		Collections.sort(users,Comparators.ComparatorUserName);
		
		
		
		
		return new ModelAndView("userassignmenttoxlsview","Users",users);
	}	
	
}
