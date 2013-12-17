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
package org.opensheet.client.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BeanModelTag;

public class Languages extends BaseModel implements Serializable, BeanModelTag{
	

	private static final long serialVersionUID = -1754645035944225592L;

	public Languages(){
	}
	
	public Languages(String id,String name){
		set("name", name);
		set("id", id);
	}
	
	
	public Languages(String id){
		set("id",id);
		if(id.equals("ru")){
			set("name","Russian");
		}else if(id.equals("en")){
			set("name","English");
		}
	}
	
	
	public static List<Languages> get() {
	    List<Languages> list = new ArrayList<Languages>();
	    list.add(new Languages("ru"));
	    list.add(new Languages("en"));
	    return list;
	}
	
	public String getName() {
	    return (String) get("name");
	 
	}
	
	public String getId() {
	    return (String) get("id");
	 
	}

}
