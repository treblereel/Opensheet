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

import com.extjs.gxt.ui.client.data.BaseTreeModel;

public class HolidayBaseModel extends BaseTreeModel implements Serializable{
	

	private static final long serialVersionUID = 1L;

	public HolidayBaseModel(){
		
	}

	public HolidayBaseModel(Integer week,Boolean status_1,Boolean status_2,Boolean status_3,Boolean status_4,Boolean status_5,Boolean status_6, Boolean status_7,Integer date_1,Integer date_2,Integer date_3,Integer date_4,Integer date_5,Integer date_6,Integer date_7,Integer step_1,Integer step_2,Integer step_3,Integer step_4,Integer step_5,Integer step_6,Integer step_7 ){
		set("week",week);
		set("step_1",step_1);
		set("step_2",step_2);
		set("step_3",step_3);
		set("step_4",step_4);
		set("step_5",step_5);
		set("step_6",step_6);
		set("step_7",step_7);
		set("date_1",date_1);
		set("date_2",date_2);
		set("date_3",date_3);
		set("date_4",date_4);
		set("date_5",date_5);
		set("date_6",date_6);
		set("date_7",date_7);
		set("status_1",status_1);
		set("status_2",status_2);
		set("status_3",status_3);
		set("status_4",status_4);
		set("status_5",status_5);
		set("status_6",status_6);
		set("status_7",status_7);
	}

	public Integer getWeek(){
		return (Integer) get("week");
	}
	
	public Boolean getDay_1(){
		return (Boolean) get("day_1");
	}
	public Boolean getDay_2(){
		return (Boolean) get("day_2");
	}
	public Boolean getDay_3(){
		return (Boolean) get("day_3");
	}
	public Boolean getDay_4(){
		return (Boolean) get("day_4");
	}
	public Boolean getDay_5(){
		return (Boolean) get("day_5");
	}
	public Boolean getDay_6(){
		return (Boolean) get("day_6");
	}
	public Boolean getDay_7(){
		return (Boolean) get("day_7");
	}
	public Integer getDate_1(){
		return (Integer) get("date_1");
	}
	public Integer getDate_2(){
		return (Integer) get("date_2");
	}
	public Integer getDate_7(){
		return (Integer) get("date_7");
	}
	public Integer getDate_3(){
		return (Integer) get("date_3");
	}
	public Integer getDate_4(){
		return (Integer) get("date_4");
	}
	public Integer getDate_5(){
		return (Integer) get("date_5");
	}
	public Integer getDate_6(){
		return (Integer) get("date_6");
	}
	
	public void setId(Integer id){
		set("id",id);
	}
	
	public Integer getId(){
		return (Integer) get("id");
	}
	
	
	/*
	public void setWeek(Integer week){
		set("week",week);
	}
	
	public void setDay(Integer week){
		set("week",week);
	}
	*/
}
