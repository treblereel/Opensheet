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
package org.opensheet.client.widges.admin;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;

public class AssignmentTab  extends TabPanel{

	final AssignmentTreeGrid assignmentTreeGrid = new AssignmentTreeGrid();

	
	
	public AssignmentTab(){
		this.addListener(Events.Select,new Listener<ComponentEvent>() {  
		      public void handleEvent(ComponentEvent be) {
		    	  
			      }  
		}); 
		
		
		TabItem am = new TabItem("Assignment's Managment");
		final AssignmentTreeGrid assignmentTreeGrid = new AssignmentTreeGrid();
		am.addListener(Events.Select, new Listener<ComponentEvent>() {  
	      public void handleEvent(ComponentEvent be) {
	    	  assignmentTreeGrid.loadProjectManagers();
	      }  
	    }); 

		
		
	//	am.add(new AssignmentTreeGrid());
		am.add(assignmentTreeGrid);
		add(am);
		
		TabItem aum = new TabItem("Assignment/User Managment");
		final AssignmentTreeUserGrid assignmentTreeUserGrid = new AssignmentTreeUserGrid();
			aum.addListener(Events.Select, new Listener<ComponentEvent>() {  
		      public void handleEvent(ComponentEvent be) {

		  //		assignmentTreeUserGrid.doUpdateStore();
		      }  
		    }); 
		aum.add(assignmentTreeUserGrid);
		add(aum);
		

		
		
		
	}
	
	
}
