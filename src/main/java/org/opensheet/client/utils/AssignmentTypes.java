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

public class AssignmentTypes extends BaseModel implements Serializable, BeanModelTag{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3399544495411610859L;

	
	public AssignmentTypes(){
				
	}
	
	public AssignmentTypes(Integer id,String name){
		set("name", name);
		set("id", id);
		
	}
	
	public AssignmentTypes(Integer id){
		switch(id){
			case 0: {
				set("id",0);
				set("name","Project");
				 break;
			}
			case 1: {
				set("id",1);
				set("name","Tender");
				 break;
			}
			case 2: {
				set("id",2);
				set("name","Office Job");
				 break;
			}
			case 3: {
				set("id",3);
				set("name","Off Hour");
				 break;
			}
			case 9999999: {
				set("id",9999999);
				set("name","All");
				 break;
			}
		}
	}
	
	
	
	public static List<AssignmentTypes> get() {
		
	    List<AssignmentTypes> list = new ArrayList<AssignmentTypes>();
	    
	    list.add(new AssignmentTypes(0,"Project"));
	    list.add(new AssignmentTypes(1,"Tender"));
	    list.add(new AssignmentTypes(2,"Office Job"));
	    list.add(new AssignmentTypes(3,"Off Hour"));
	    return list;
	}
	
		public static List<AssignmentTypes> getWithAllTypes() {
	    List<AssignmentTypes> list = new ArrayList<AssignmentTypes>();
	    
	    list.add(new AssignmentTypes(0,"Project"));
	    list.add(new AssignmentTypes(1,"Tender"));
	    list.add(new AssignmentTypes(2,"Office Job"));
	    list.add(new AssignmentTypes(3,"Off Hour"));
	    list.add(new AssignmentTypes(9999999,"All"));
	    return list;
	}
	
	
	public String getName() {
	    return (String) get("name");
	 
	}
	
	public Integer getId() {
	    return (Integer) get("id");
	 
	}
	
}
