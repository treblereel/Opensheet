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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.opensheet.client.dto.UserRateDTO;

import com.extjs.gxt.ui.client.data.BeanModelTag;


@Entity
@Audited
@Table(name= "person_rate")
public class UserRate  implements Serializable, BeanModelTag{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5500716300937604573L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	

	@Column(name="internal_rate")
	private Integer internalRate;

	@Temporal(value=TemporalType.DATE)
	private Date date;
	
	public UserRate(){
		
	}
	
	public UserRate(UserRateDTO userRateDTO){
		this.setId(userRateDTO.getId());
		this.setDate(userRateDTO.getDate());
		this.setInternalRate(userRateDTO.getInternalRate());
	}
	
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}



	public Integer getInternalRate() {
		return internalRate;
	}

	public void setInternalRate(Integer internalRate) {
		this.internalRate = internalRate;
	}



	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}


	
}
