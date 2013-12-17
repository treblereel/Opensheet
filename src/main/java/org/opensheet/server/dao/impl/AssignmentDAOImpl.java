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
package org.opensheet.server.dao.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.opensheet.client.dto.grid.AssignmentGridTemplate;
import org.opensheet.server.dao.AssignmentDAO;
import org.opensheet.server.security.CheckUserContext;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.AssignmentTimeline;
import org.opensheet.shared.model.Branch;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.extjs.gxt.ui.client.data.BaseModel;

/**<p> This class is an implementation of interface AssignmentDAO that can be used for different operation under 
 * "Assignment" Objects .</p>
 * 
 * 
 * @author chani
 *
 */

@Repository
public class AssignmentDAOImpl implements AssignmentDAO {

	
	@Autowired	private SessionFactory sessionFactory;

	@Autowired 	private CheckUserContext checkUserContext;

	/** Searches the specified object by their unique id.
	 * @param the index of the search key
	 * @return Assignment object
	 * 
	 */
	@Override
	@Transactional
	public Assignment getAssignmentById(Integer id){
		Assignment assignment = (Assignment) sessionFactory.getCurrentSession().
				createQuery("from Assignment a join fetch a.owner as u join fetch a.branch as b" +
						    " where a.id=?").setInteger(0, id).uniqueResult();
		return assignment;
	}
	
	
	/** Return the collection of the Assignment objects by type
	 * @param type by type (0 - project, 1 -tender, 2 - office assignmens, 3 - out of the office tasks)
	 * @return List<Assignment>
	 */
	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<Assignment> getAssignments(Integer type){
		List<Assignment> assignments = new ArrayList<Assignment>(sessionFactory.getCurrentSession().createQuery("from Assignment a join fetch a.owner as u WHERE a.type =?").
					setInteger(0, type).list());
		return assignments;
	}
	
	
	
	/** This method used to get Assignment TreeGrid model for AssignmentTreeGrid/AssignmentTreeUserGrid 
	 * @param 
	 *  type by type (0 - project, 1 -tender, 2 - office assignments, 3 - out of the office tasks)
	 *  status active/inactive Assignments
	 *  branch id if the branch
	 *  @return AssignmentGridTemplate
	 * @see org.opensheet.client.dto.grid.AssignmentGridTemplate
	 * 
	 */
	
	@Override
	@Transactional
	public AssignmentGridTemplate getAssignmentDTOs(Integer type,Boolean status,Integer branch){
		AssignmentGridTemplate root = new AssignmentGridTemplate("root",true,100000,"nope",false,false);
		
		for(Assignment a: getRootAssignments(type,status,branch)){
			if(status == true){
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
					assignmentGridTemplate = new ArrayList<AssignmentGridTemplate>();
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
	
	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<Assignment> getRootAssignments(Integer type,Boolean status,Integer branch){
		
		List<Assignment> assignments = null;
		
		
			if(status == true){
				assignments = new ArrayList<Assignment>(sessionFactory.getCurrentSession().createQuery("from Assignment a WHERE a.status = ? AND a.type =? AND a.level=0 AND a.branch=? order by a.name").
						setBoolean(0, status).
						setInteger(1, type).setInteger(2, branch).
						list());
			}else if(status == false){
				assignments = new ArrayList<Assignment>(sessionFactory.getCurrentSession().createQuery("from Assignment a WHERE a.type =? AND a.level=0 AND a.branch=? order by a.name").
						setInteger(0, type)
						.setInteger(1, branch)
						.list());
			}
			
			for(Assignment a: assignments ){
				a.getChildren();
				
				/* for sub_tasks */
					for(Assignment child: a.getChildren()){
						child.getOwner();
					}
			}
	  return assignments;
	}
	
	
	
	@Override
	@Transactional
	public BaseModel updateAssignmnet(Assignment assignment){
		
		
			Assignment a = (Assignment) sessionFactory.getCurrentSession().get(Assignment.class, new Integer(assignment.getId()));
			
			a.setName(assignment.getName());
			a.setIndex(assignment.getIndex());
			a.setType(assignment.getType());
			a.setNote(assignment.getNote());
			
			if(a.getLevel() != 0){
				if(assignment.getByDefault() == true &&   a.getParent().getByDefault() == true ){
					a.setByDefault(assignment.getByDefault());
				}else if(assignment.getByDefault() == false){
					a.setByDefault(assignment.getByDefault());

				}else{
					BaseModel bm  = new BaseModel();
					  bm.set("result", "failed");
					  bm.set("msg", "Parent Assignment is not Default");
					  return bm;	
					
				}
			}else{
				a.setByDefault(assignment.getByDefault());
				a.setBranch(assignment.getBranch());
			}
			/** set owner to A and to childs
			  */
			if(assignment.getOwner() !=null){
				a.setOwner(assignment.getOwner());
				if(a.getLevel() == 0 ){
					for(Assignment child: a.getChildren()){
						child.setOwner(a.getOwner());
					}
				}
				
			}
			
			
			a.setFinished(assignment.getFinished());
			a.setUpdated(new Date());
			
			if(assignment.getStatus() == false && a.getStatus() == true){
				onAssignmentStatusChange(a);
			}
			a.setStatus(assignment.getStatus());

			BaseModel bm  = new BaseModel();
					  bm.set("result", "ok");
			return bm;		  
	}
	
	
	@Override
	@Transactional
	public void addAssignmnet(Assignment assignment){
			Assignment parent = null;

			assignment.setFirst(false);	
			assignment.setStarted(new Date());
			assignment.setUpdated(new Date());
			assignment.setStatus(true);
			
			AssignmentTimeline timeline = new AssignmentTimeline();
			timeline.setAssignment(assignment);
			timeline.setHour(null);
			timeline.setDate(new Date());
			timeline.setUser(checkUserContext.getUser(SecurityContextHolder.getContext()));
			assignment.setTimeline(timeline);
			
			
			if(assignment.getLevel() != 0 ){
				parent	= (Assignment) sessionFactory.getCurrentSession().get(Assignment.class, new Integer(assignment.getParent().getId()));
				assignment.setParent(parent);
				assignment.setOwner(parent.getOwner());
				assignment.setBranch(parent.getBranch());
				assignment.setType(parent.getType());
				
				sessionFactory.getCurrentSession().save(assignment);
				
				sessionFactory.getCurrentSession().saveOrUpdate(timeline);

			
			}else{
				assignment.setParent(null);
				sessionFactory.getCurrentSession().save(assignment);
				sessionFactory.getCurrentSession().saveOrUpdate(timeline);

				
				Assignment child = new Assignment();
				child.setName(assignment.getName() + " " + "default child");
				child.setIndex(assignment.getIndex() + " " + "default index");
				child.setOwner(assignment.getOwner());
				child.setBranch(assignment.getBranch());
				child.setByDefault(assignment.getByDefault());
				child.setStarted(new Date());
				child.setUpdated(new Date());
				child.setFinished(assignment.getFinished());
				child.setStatus(true);
				child.setType(assignment.getType());
				child.setLevel(assignment.getLevel() +1);
				child.setFirst(true);
				child.setParent(assignment);
				
				AssignmentTimeline childTimeline = new AssignmentTimeline();
				childTimeline.setAssignment(child);
				childTimeline.setHour(null);
				childTimeline.setDate(new Date());
				childTimeline.setUser(checkUserContext.getUser(SecurityContextHolder.getContext()));
				child.setTimeline(childTimeline);
				
				sessionFactory.getCurrentSession().save(child);
				sessionFactory.getCurrentSession().saveOrUpdate(childTimeline);

				
				List<Assignment> childrenList = new LinkedList<Assignment>();
				childrenList.add(child);
				assignment.setChildren(childrenList);
				
				sessionFactory.getCurrentSession().save(assignment);
				sessionFactory.getCurrentSession().saveOrUpdate(timeline);

			}
		
	}


	
	
	/**
	 * Does this Assignment has a Children?
	 * 
	 * @param assignemnt
	 * @return
	 */
	
	
	public Boolean hasChildren(Assignment assignment){
		
		if(assignment.getChildren().size() > 1)
		{
			return true;
		}else{
			return false;
		}

	}

	
	
	@Override
	@Transactional
	public void changeStatusAssignment(String id) throws NumberFormatException {
		
		Integer index = Integer.parseInt(id);
			Assignment a = (Assignment) getAssignmentById(index);
			if(a.getStatus() == true){
				a.setStatus(false);
				List<User> users = new ArrayList<User>();
				a.setUsers(users);
				for(Assignment asub: a.getChildren()){
					asub.setUsers(users);
					sessionFactory.getCurrentSession().save(asub);
				}
			}else if(a.getStatus() == false){
				a.setStatus(true);
			}
			sessionFactory.getCurrentSession().save(a);
		}
	
	@Override
	@Transactional
	public Assignment getAssignemntsDefaultTask(Assignment assignment){
		Assignment defaultTask = (Assignment) sessionFactory.getCurrentSession().createQuery("from Assignment a WHERE a.parent = ? AND a.first = ?")
		.setInteger(0, assignment.getId())
		.setBoolean(1, true)
		.uniqueResult();
		return defaultTask;
	}

	
	@Transactional
	private void onAssignmentStatusChange(Assignment assignment){
		List<User> users = new ArrayList<User>();
		assignment.setUsers(users);
		for(Assignment a: assignment.getChildren()){
			a.setUsers(users);
			sessionFactory.getCurrentSession().saveOrUpdate(a);
			
		}
		sessionFactory.getCurrentSession().saveOrUpdate(assignment);
//		sessionFactory.getCurrentSession().flush();
	}

	@Override
	@Transactional
	public Integer getSumHourAssignment(Assignment assignment) {
		Long currentTimelinesAssignmentSummLong = null;
		if(assignment.getLevel() == 0){
		currentTimelinesAssignmentSummLong =  (Long) sessionFactory.getCurrentSession().createQuery("SELECT SUM(h.hour) FROM Hour h where h.assignment=? ")
				.setInteger(0, assignment.getId())
				.uniqueResult();
		
		}else{
			currentTimelinesAssignmentSummLong =  (Long) sessionFactory.getCurrentSession().createQuery("SELECT SUM(h.hour) FROM Hour h where h.task=? ")
					.setInteger(0, assignment.getId())
					.uniqueResult();	
		}
		
		
		if(currentTimelinesAssignmentSummLong != null){
			return (Integer ) currentTimelinesAssignmentSummLong.intValue();
		}
		
		return 0;
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<Assignment> getDefaultAssignment() {
		List<Assignment> assignments =  (ArrayList<Assignment>) sessionFactory.getCurrentSession().createQuery("from Assignment a where a.status=1 and a.byDefault=1").list();
		return assignments;
	}

	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Map<Assignment, List<Assignment>> getAssignmentByDepartment(Department department) {
		List<Assignment>  temp  = (ArrayList<Assignment>) sessionFactory.getCurrentSession()
	    .createQuery("select d.assignments from Department d where d.id=? ").setInteger(0, department.getId()).list();
		
		
		
		
		List<Assignment> list = new ArrayList<Assignment>();
		for(Assignment a: temp){
			if(a.getStatus() == true && a.getFirst() !=true){
				list.add(a);
			}
		}
		
		
		
		Map<Assignment, List<Assignment>> map = new HashMap<Assignment, List<Assignment>>();
		for(Assignment a: list){
			if(a.getLevel() == 0){
				if(!map.containsKey(a)){
					List<Assignment> l = new ArrayList<Assignment>();
					map.put(a,l);
				}
			}else{
				if(map.containsKey(a.getParent())){
					map.get(a.getParent()).add(a);
				}else{
					List<Assignment> l = new ArrayList<Assignment>();
					l.add(a);
					map.put(a.getParent(),l);
				}
			}
		}
		
		return map;
	}

	@Override
	@Transactional
	public void setDepartment(Assignment assignment,List<Department> departments) {
		assignment.setDepartments(departments);
		sessionFactory.getCurrentSession().save(assignment);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Assignment> getAssignmentByBranch(Branch branch) {
		List<Assignment> result = (ArrayList<Assignment>) sessionFactory.getCurrentSession()
			    .createQuery("from Assignment a where a.branch=? ").setInteger(0, branch.getId()).list();
		return result;
	}


	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<Assignment> getAssignmentByBranchAndByType(Branch branch,Integer type) {

		StringBuilder sb = new StringBuilder();
		sb.append("from Assignment a where a.level=0");
		if(branch != null && branch.getId() != 9999999)
			sb.append(" and a.branch="+branch.getId());
		if(type !=null && type != 9999999)
			sb.append(" and a.type="+type);
		sb.append(" order by a.name");
		
		List<Assignment> result = (ArrayList<Assignment>) sessionFactory.getCurrentSession()
			    .createQuery(sb.toString())
			    .list();
		return result;
	}


	@Override
	@Transactional
	public List<Department> getDepartmentsByBranch(Assignment assignment,Branch branch) {
		List<Department>  deps = assignment.getDepartments();
		List<Department>  result = assignment.getDepartments();
		
		if(branch.getId() == 9999999){
			return deps;
		}
		
		for(Department d: deps){
			if(d.getBranch() == branch){
				result.add(d);
			}
		}
		return result;
	}
	
	
}
