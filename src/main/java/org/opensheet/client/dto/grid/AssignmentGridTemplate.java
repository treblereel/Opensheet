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

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseTreeModel;

public class AssignmentGridTemplate  extends BaseTreeModel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID =  1L;

	public AssignmentGridTemplate(){
		
		
	}
	
	public AssignmentGridTemplate(String name){
		set("name",name);	
	}
	
	/**
	 * 
	 * @param name
	 * @param status
	 * @param index
	 * @param assignmentIndex
	 * @param leaf
	 * @param hasChild
	 */
	
	public AssignmentGridTemplate(String name,Boolean status,Integer index, String assignmentIndex,Boolean leaf,Boolean hasChildren){
		set("index",index);
		set("assignmentIndex", assignmentIndex);
	    set("name", name);
	    set("status",status);
	    set("leaf",leaf);
	    set("hasChildren",hasChildren);	
		}
	
	
	public AssignmentGridTemplate(Integer index,String name,Boolean leaf,Boolean hasChildren,Integer hours,Integer inrateSum, Integer exRateSum,Boolean first){
		set("id",index);
		set("index",index);
	    set("name", name);
	    set("leaf",leaf);
	    set("hasChildren",hasChildren);
	    set("inrate",inrateSum);
	    set("exrate",exRateSum);
	    set("hours",hours);
	    set("first",first);
	}
	
	
	
	public AssignmentGridTemplate(Integer index, String name,String assignmentIndex,String owner,Integer type,Boolean status,Boolean leaf,Boolean hasChildren){
		set("index",index);
		set("name", name);
		set("assignmentIndex", assignmentIndex);
	    set("owner", owner);
	    set("type",type);
	    set("status",status);
	    set("leaf",leaf);
	    set("hasChildren",hasChildren);	
		}
	
	public AssignmentGridTemplate(String name,Boolean status,Integer index, String assignmentIndex,Boolean leaf,Boolean hasChildren,String owner,Integer timeline,Integer hours){
		set("id",index);
		set("index",index);
		set("assignmentIndex", assignmentIndex);
	    set("name", name);
	    set("status",status);
	    set("leaf",leaf);
	    set("hasChildren",hasChildren);	
	    set("owner",owner);
	    set("timeline",timeline);
	    set("hours",hours);
		}
	
	
	public AssignmentGridTemplate(Integer index,String name,Boolean leaf,Boolean hasChildren,Integer hours,Integer inrateSum, Integer exRateSum,ArrayList<AssignmentGridTemplate> assignmentGridTemplate){
		this(index,name,leaf,hasChildren,hours,inrateSum,exRateSum,false);
		 for(AssignmentGridTemplate a: assignmentGridTemplate){
			 add(a);
		 }
	}
	
	
	  public AssignmentGridTemplate(String string,Boolean status, int i, String assignmentIndex,Boolean leaf,Boolean hasChildren,String owner,Integer timeline,Integer hours, ArrayList<AssignmentGridTemplate> assignmentGridTemplate) {
		  this(string,status,i,assignmentIndex,leaf,hasChildren);
		  
		  for(AssignmentGridTemplate a: assignmentGridTemplate){
			 add(a);
		 }
	  }	
	
	  public AssignmentGridTemplate(String string,Boolean status, int i, String assignmentIndex, ArrayList<AssignmentGridTemplate> assignmentGridTemplate,Boolean leaf,Boolean hasChildren) {
		  this(string,status,i,assignmentIndex,leaf,hasChildren);
	  	  for(AssignmentGridTemplate a: assignmentGridTemplate){
			 add(a);
		 }
	  }	
	  
	  
	  public Integer getHours() {
		    return (Integer) get("hours");
		  }
	  
	  public Integer getTimeline() {
		    return (Integer) get("timeline");
		  }
	  
	   public String getOwner() {
		    return (String) get("owner");
		  }
	  
	  
	 	public String getName() {
		    return (String) get("name");
		  }
	 	
	 	public Boolean getStatus() {
		    return (Boolean) get("status");
		}
		
	 	public Integer getId() {
		    return (Integer) get("id");
		  }  
	 	
		  public Integer getIndex() {
			    return (Integer) get("index");
			  }

		  public String getAssignmentIndex() {
			    return (String) get("assignmentIndex");
			  }
		  
		  public Boolean getLeaf(){
			  return (Boolean) get("leaf");
		  }

		  public String toString() {
		    return getName();
	
		  }
	
		  public Boolean getHasChild(){
			  return (Boolean) get("hasChild");
			  
		  }
		  
		  
		  public void setHours(Integer hours){
			  set("hours",hours);
		  }
		  
		  public void setInrate(Integer inrate){
			  set("inrate",inrate);
		  }
		  public void setExrate(Integer exrate){
			  set("exrate",exrate);
		  }
		  
		  public Integer getInrate(){
			  return (Integer) get("inrate");
		  }
		  
		  public Integer getExrate(){
			  return (Integer) get("exrate");
		  }
		  
		  public void setHasChild(Boolean value){
			  set("hasChild",value);
		  }
		  
		  
		  public void addChild(List<AssignmentGridTemplate> assignmentGridTemplate){
			  for(AssignmentGridTemplate a: assignmentGridTemplate){
					 add(a);
				 }
		  }
}
