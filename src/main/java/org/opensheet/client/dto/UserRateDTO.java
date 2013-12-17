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

import org.opensheet.shared.model.UserRate;

import com.extjs.gxt.ui.client.data.BeanModelTag;



public class UserRateDTO implements Serializable, BeanModelTag{


	/**
	 * 
	 */
	private static final long serialVersionUID = 4375884143082286740L;
	private Integer id;
	private Integer  internalRate;
	private Date    date;
	
	public UserRateDTO(){
		
		
	}
	
	public UserRateDTO(UserRate userRate){
		this.setId(userRate.getId());
		this.setDate(userRate.getDate());
		this.setInternalRate(userRate.getInternalRate());
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
