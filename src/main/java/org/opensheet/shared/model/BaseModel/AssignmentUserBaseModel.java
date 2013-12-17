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
package org.opensheet.shared.model.BaseModel;

import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BeanModelTag;


public class AssignmentUserBaseModel extends BaseModel implements Serializable, BeanModelTag{

	
	
	private static final long serialVersionUID = 1165835526096584623L;

	
	public AssignmentUserBaseModel(){
		
		
	}
	
	public AssignmentUserBaseModel(Integer id, String name){
		
		set("id",id);
		set("name",name);
		
	}
	
	public Integer getId(){
		
		return (Integer) get("id");
	}
	
	public String getName(){
		return (String) get("name");
	}
}
