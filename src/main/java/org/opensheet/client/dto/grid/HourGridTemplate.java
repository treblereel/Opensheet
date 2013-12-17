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
package org.opensheet.client.dto.grid;



import java.io.Serializable;
import java.util.List;

import org.opensheet.shared.model.Hour;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.BeanModelTag;

public class HourGridTemplate extends BaseTreeModel implements Serializable, BeanModelTag{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private static int ID = 0;
	
	
	public HourGridTemplate(){
		set("id", ID++);
		
	}
	
	public HourGridTemplate(String name){
		set("name", name);
		set("id", ID++);
		
	}
	
	
	@SuppressWarnings("deprecation")
	public HourGridTemplate(Integer user,Integer assignment, String name,Integer year,Integer month, List<Hour> hours){
		set("index", assignment);
		set("user",user);
		set("assignment",assignment);
		set("name",name);
		set("year",year);
		set("month",month);
		set("id", ID++);
		
		for(Hour h: hours ){
			set(Integer.toString(h.getDate().getDay()),h.getHour());
		}
		
	}
	
	public HourGridTemplate(Integer user,Integer assignment, String name,Integer year,Integer month, List<Hour> hours,List<HourGridTemplate> hourGridTemplates){
		
		this(user,assignment,name, year,month,hours);
		for(HourGridTemplate h : hourGridTemplates){
				
			
			add(h);
		
		}
	}
	
	
	
	public Integer get1(){
		return (Integer) get("1");
	}
	public Integer get2(){
		return (Integer) get("2");
	}
	public Integer get3(){
		return (Integer) get("3");
	}
	public Integer get4(){
		return (Integer) get("4");
	}
	public Integer get5(){
		return (Integer) get("5");
	}
	public Integer get6(){
		return (Integer) get("6");
	}
	public Integer get7(){
		return (Integer) get("7");
	}
	public Integer get8(){
		return (Integer) get("8");
	}
	public Integer get9(){
		return (Integer) get("9");
	}
	public Integer get10(){
		return (Integer) get("10");
	}
	public Integer get11(){
		return (Integer) get("11");
	}
	public Integer get12(){
		return (Integer) get("12");
	}
	public Integer get13(){
		return (Integer) get("13");
	}
	public Integer get14(){
		return (Integer) get("14");
	}
	public Integer get15(){
		return (Integer) get("15");
	}
	public Integer get16(){
		return (Integer) get("16");
	}
	public Integer get17(){
		return (Integer) get("17");
	}
	public Integer get18(){
		return (Integer) get("18");
	}
	public Integer get19(){
		return (Integer) get("19");
	}
	public Integer get20(){
		return (Integer) get("20");
	}
	public Integer get21(){
		return (Integer) get("21");
	}
	public Integer get22(){
		return (Integer) get("22");
	}
	public Integer get23(){
		return (Integer) get("23");
	}
	public Integer get24(){
		return (Integer) get("24");
	}
	public Integer get25(){
		return (Integer) get("25");
	}
	public Integer get26(){
		return (Integer) get("26");
	}
	public Integer get27(){
		return (Integer) get("27");
	}
	public Integer get28(){
		return (Integer) get("28");
	}
	public Integer get29(){
		return (Integer) get("29");
	}
	public Integer get30(){
		return (Integer) get("30");
	}
	public Integer get31(){
		return (Integer) get("31");
	}
	
	
	public Integer getUser(){
		return (Integer) get("user");
	}
	
	
	
	
	public String getName(){
		return (String) get("name");
	}
	
	public String getAssignment(){
		return (String) get("assignment");
	}
	

	public Integer getHour(){
		  return (Integer) get("hour");
	  }
	
	public Integer getYear(){
		  return (Integer) get("year");
		  
	  }
	
	public Integer getMonth(){
		return (Integer) get("month");
	}
	
	public Integer getIndex(){
		return (Integer) get("index");
	}
	
	public Integer getId() {
	    return (Integer) get("id");
	  }
}
