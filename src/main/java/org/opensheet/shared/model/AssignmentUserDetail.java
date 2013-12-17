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
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.extjs.gxt.ui.client.data.BeanModelTag;

@Entity
@Audited
@Table(name= "assignment_person_detail")
public class AssignmentUserDetail implements Serializable, BeanModelTag {

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private Assignment assignment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private User user;
	
	@NotNull
	private Integer timeline;
	
	@NotNull
	private Integer rate;
	@Temporal(value=TemporalType.DATE)
	private Date started;
	@Temporal(value=TemporalType.DATE)
	private Date updated;
	
	
	private static final long serialVersionUID = 8540044431844448342L;

	
	public AssignmentUserDetail(){
		
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Assignment getAssignment() {
		return assignment;
	}


	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Integer getTimeline() {
		return timeline;
	}


	public void setTimeline(Integer timeline) {
		this.timeline = timeline;
	}


	public Integer getRate() {
		return rate;
	}


	public void setRate(Integer rate) {
		this.rate = rate;
	}


	public Date getStarted() {
		return started;
	}


	public void setStarted(Date started) {
		this.started = started;
	}


	public Date getUpdated() {
		return updated;
	}


	public void setUpdated(Date updated) {
		this.updated = updated;
	}


	@Override
	public String toString() {
		return "AssignmentUserDetail [id=" + id + ", assignment=" + assignment
				+ ", user=" + user + ", timeline=" + timeline + ", rate="
				+ rate + ", started=" + started + ", updated=" + updated + "]";
	}
	
	
	
}
