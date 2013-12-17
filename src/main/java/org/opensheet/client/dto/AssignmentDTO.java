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
package org.opensheet.client.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.opensheet.shared.model.Assignment;

import com.extjs.gxt.ui.client.data.BeanModelTag;

public class AssignmentDTO  implements Serializable, BeanModelTag{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -488164335684354541L;
	
	private Integer id;
	private String name;
	private List<AssignmentDTO> children = new LinkedList<AssignmentDTO>();
	private AssignmentDTO parent = null;
	private Integer level;
	private Integer type;
	private Integer timeline;
	private Boolean status;
	private Boolean first;
	private Boolean byDefault;
	private String note;
	private String index;
	private Date started;
	private Date finished;
	private Date updated;
	private UserDTO owner;
	private BranchDTO branch;
	
	public AssignmentDTO(){
		
	}
	
	public AssignmentDTO(Integer id){
		this.setId(id);
	}
	
	public AssignmentDTO(Assignment assignment){
		this.setId(assignment.getId());		
		this.setName(assignment.getName());
		this.setNote(assignment.getNote());
		this.setLevel(assignment.getLevel());
		this.setType(assignment.getType());
		this.setStatus(assignment.getStatus());
		this.setIndex(assignment.getIndex());
		this.setStarted(assignment.getStarted());
		this.setUpdated(assignment.getUpdated());
		this.setFinished(assignment.getFinished());
		this.setFirst(assignment.getFirst());
		this.setByDefault(assignment.getByDefault());
	}
	
	
	public Assignment getAssignment(AssignmentDTO assignment){
		
		return new Assignment(assignment);
	}
	
	public Assignment getAssignment(){
		return new Assignment(this);
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

	public List<AssignmentDTO> getChildren() {
		return children;
	}

	public void setChildren(List<AssignmentDTO> children) {
		this.children = children;
	}

	public AssignmentDTO getParent() {
		return parent;
	}

	public void setParent(AssignmentDTO parent) {
		this.parent = parent;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getTimeline() {
		return timeline;
	}

	public void setTimeline(Integer timeline) {
		this.timeline = timeline;
	}

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


	public void setOwner(UserDTO owner) {
		this.owner = owner;
	}


	public UserDTO getOwner() {
		return owner;
	}


	public void setFirst(Boolean first) {
		this.first = first;
	}


	public Boolean getFirst() {
		return first;
	}

	public Boolean getByDefault() {
		return byDefault;
	}

	public void setByDefault(Boolean byDefault) {
		this.byDefault = byDefault;
	}

	public BranchDTO getBranch() {
		return branch;
	}

	public void setBranch(BranchDTO branch) {
		this.branch = branch;
	}
	
}
