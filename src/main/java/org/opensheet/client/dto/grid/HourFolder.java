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

import java.util.List;

import org.opensheet.shared.model.Hour;

import com.extjs.gxt.ui.client.data.BaseTreeModel;

public class HourFolder extends BaseTreeModel{


	private static final long serialVersionUID = 7910910242500548220L;

	
	  private static int ID = 0;
	  private int summ = 0;
	  
	  public HourFolder() {
	    set("id", ID++);
	  }
	
	  public HourFolder(String name) {
		    set("name", name);
		    set("id", ID++);
		  }
	  
	  @SuppressWarnings("deprecation")
	public HourFolder(String name,Integer index,Integer type,List<Hour> hours) {
		    set("id", ID++);
		    set("name", name);
		    set("index",index);
		    set("type",type);
		    set("leaf",true);

		    for(Hour h: hours ){
				set(Integer.toString(h.getDate().getDate()),h.getHour());
				this.summ = this.summ + h.getHour();
			}
		    set("summ",this.summ);
		  }
	  
	  
	  @SuppressWarnings("deprecation")
	public HourFolder(String name,Integer index,Integer type,List<Hour> hours,List<BaseTreeModel> children) {
		    set("id", ID++);
		    set("name", name);
		    set("index",index);
		    set("type",type);
   
			   if(children.size() >0){
				    set("leaf",false);
			   }else{
				    set("leaf",true);
			   }

		    
		    for(Hour h: hours ){
				set(Integer.toString(h.getDate().getDate()),h.getHour());
				this.summ = this.summ + h.getHour();
			}
		    
		    set("summ",this.summ);
		    
		    for(BaseTreeModel b: children){
		    	add(b);		    	
		    }
		    
		  }
	  
	  
	  
	  public Integer getId() {
		    return (Integer) get("id");
		  }
	  
	  public Integer getIndex() {
		    return (Integer) get("index");
		  }
	  
	  public Integer getType() {
		    return (Integer) get("type");
		  }

	  public String getName() {
		    return (String) get("name");
		  }
	  
	  public Boolean getLeaf() {
		    return (Boolean) get("leaf");
		  }

     public String toString() {
		    return getName();
		  }
     
     public Integer getUser(){
 		return (Integer) get("user");
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
 	
 	public Integer getSumm(){
 		return (Integer) get("summ");
 	}
	
}
