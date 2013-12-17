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
package org.opensheet.shared.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.opensheet.client.dto.AssignmentDTO;

import com.extjs.gxt.ui.client.data.BeanModelTag;



	@Entity
	@Table(name = "assignment")
	public class Assignment  implements Serializable, BeanModelTag{

	
	

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;


		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		private Integer id;

		
		private String name;

		@OneToMany(fetch=FetchType.LAZY)
		@JoinColumn(name = "parent")
		@Column(nullable = true)
		private List<Assignment> children = new LinkedList<Assignment>();
		
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name = "parent", referencedColumnName = "id")
		private Assignment parent;
		
		private Integer level;
		@NotNull
		private Integer type;
		@Column(columnDefinition = "tinyint")
		private Boolean status;
		@Column(columnDefinition = "tinyint")
		private Boolean first;
		
		@NotNull
		@Column(name = "`default`",columnDefinition = "tinyint")
		private Boolean byDefault;
		
		private String note;
		@Column(name="`index`")
		private String index;
		@Temporal(value=TemporalType.DATE)
		private Date started;
		@Temporal(value=TemporalType.DATE)
		private Date finished;
		@Temporal(value=TemporalType.DATE)
		private Date updated;
		
		@ManyToOne(fetch = FetchType.LAZY)
		private User owner;
		

		@ManyToMany(fetch = FetchType.LAZY)
		@JoinTable(name = "assignment_person", joinColumns = { @JoinColumn(name = "assignment") },inverseJoinColumns = { @JoinColumn(name = "person") })
		private List<User> users = new ArrayList<User>(0);
		
		@ManyToMany(fetch = FetchType.LAZY)
		@JoinTable(name = "assignment_department", joinColumns = { @JoinColumn(name = "assignment") },inverseJoinColumns = { @JoinColumn(name = "department") })
		private List<Department> departments = new ArrayList<Department>(0);
		
		@ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "branch_id", referencedColumnName = "id")
		private Branch branch;
		
		@OneToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "timeline", referencedColumnName = "id")
		private AssignmentTimeline timeline;
		
		
		@Transient
		private Boolean hasChildren;
		
		
		public Assignment(){
			
		}
		
		public Assignment(AssignmentDTO assignmentDTO){
			this.setId(assignmentDTO.getId());		
			this.setName(assignmentDTO.getName());
			this.setNote(assignmentDTO.getNote());
			this.setLevel(assignmentDTO.getLevel());
			this.setType(assignmentDTO.getType());
	//		this.setTimeline(assignmentDTO.getTimeline());
			this.setStatus(assignmentDTO.getStatus());
			this.setIndex(assignmentDTO.getIndex());
			this.setStarted(assignmentDTO.getStarted());
			this.setUpdated(assignmentDTO.getUpdated());
			this.setFinished(assignmentDTO.getFinished());
			this.setFirst(assignmentDTO.getFirst());
			this.setByDefault(assignmentDTO.getByDefault());
		}
		
		public AssignmentDTO getAssignmentDTO(Assignment assignment){
			
			return new AssignmentDTO(assignment);
		}
		
		
		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<Assignment> getChildren() {
			return children;
		}

		public void setChildren(List<Assignment> children) {
			this.children = children;
		}

		public Assignment getParent() {
			return parent;
		}

		public void setParent(Assignment parent) {
			this.parent = parent;
		}

		public void setLevel(Integer level) {
			this.level = level;
		}

		public Integer getLevel() {
			return level;
		}

		public Boolean hasParent() {
			if(this.level == 0){
				return false;	
			}else{
				return true;
			}
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

	/*	
		public void setTimeline(Integer timeline) {
			this.timeline = timeline;
		}
*/
		public Boolean getStatus() {
			return status;
		}

		public void setStatus(Boolean status) {
			this.status = status;
		}

		public String getNote() {
			return note;
		}

		public void setNote(String note) {
			this.note = note;
		}

		public String getIndex() {
			return index;
		}

		public void setIndex(String index) {
			this.index = index;
		}

		public Date getStarted() {
			return started;
		}

		public void setStarted(Date started) {
			this.started = started;
		}

		public Date getFinished() {
			return finished;
		}

		public void setFinished(Date finished) {
			this.finished = finished;
		}

		public Date getUpdated() {
			return updated;
		}

		public void setUpdated(Date updated) {
			this.updated = updated;
		}

		

	

		public void setHasChildren(Boolean hasChildren) {
			this.hasChildren = hasChildren;
		}

		public Boolean getHasChildren() {
			return hasChildren;
		}

		public void setOwner(User owner) {
			this.owner = owner;
		}

		public User getOwner() {
			return owner;
		}

	
		public void setFirst(Boolean first) {
			this.first = first;
		}

		public Boolean getFirst() {
			return first;
		}

		public void setUsers(List<User> u) {
			this.users = u;
		}

		public List<User> getUsers() {
			return users;
		}

		public List<Department> getDepartments() {
			return departments;
		}

		public void setDepartments(List<Department> departments) {
			this.departments = departments;
		}

/*		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((users == null) ? 0 : users.hashCode());
			result = prime * result
					+ ((children == null) ? 0 : children.hashCode());
			result = prime * result
					+ ((finished == null) ? 0 : finished.hashCode());
			result = prime * result + ((first == null) ? 0 : first.hashCode());
			result = prime * result
					+ ((hasChildren == null) ? 0 : hasChildren.hashCode());
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + ((index == null) ? 0 : index.hashCode());
			result = prime * result + ((level == null) ? 0 : level.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((note == null) ? 0 : note.hashCode());
			result = prime * result + ((owner == null) ? 0 : owner.hashCode());
			result = prime * result
					+ ((parent == null) ? 0 : parent.hashCode());
			result = prime * result
					+ ((started == null) ? 0 : started.hashCode());
			result = prime * result
					+ ((status == null) ? 0 : status.hashCode());
			
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			result = prime * result
					+ ((updated == null) ? 0 : updated.hashCode());
			return result;
		}

		
		public boolean equals(Object obj) {
			Assignment other = (Assignment) obj;
			if(id != other.getId()){
				return false;
			}
				return true;	
		}
*/
		public AssignmentTimeline getTimeline() {
			return timeline;
		}

		public void setTimeline(AssignmentTimeline timeline) {
			this.timeline = timeline;
		}

		public Boolean getByDefault() {
			return byDefault;
		}

		public void setByDefault(Boolean byDefault) {
			this.byDefault = byDefault;
		}

		public Branch getBranch() {
			return branch;
		}

		public void setBranch(Branch branch) {
			this.branch = branch;
		}

		
		
	
}
