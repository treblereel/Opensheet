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

import org.opensheet.client.mvc.events.AdminEvents;
import org.opensheet.client.mvc.events.AppEvents;
import org.opensheet.client.utils.Resources;
import org.opensheet.client.widges.admin.windows.HolidayPanel;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class AdminToolBar extends ToolBar{
	
	Dispatcher dispatcher = Dispatcher.get();
	
	public String name = "adminToolBar";

	
	public AdminToolBar(){
		
		final Dispatcher dispatcher = Dispatcher.get();

		this.setId("adminToolBarId");
	
		
		Button opensheetButton = new Button("Opensheet");
		opensheetButton.setIconStyle("icon-opensheet"); 
		opensheetButton.addListener(Events.Select,new Listener<ButtonEvent>() {
   		 @Override public void handleEvent(ButtonEvent be) {
          	  dispatcher.dispatch(AppEvents.Sheet);
   	      }  
   	    });
		
		
   	 add(opensheetButton);
	 add(new SeparatorToolItem()); 
	
	 
	 Button userButton = new Button("Users");  
	 add(userButton); 
	 userButton.setIconStyle("icon-user");
	 userButton.addListener(Events.Select,new Listener<ButtonEvent>() {
		 @Override public void handleEvent(ButtonEvent be) {
       	  dispatcher.dispatch(AdminEvents.AdminUser);
	      }  
	    });
	 
	 add(new SeparatorToolItem()); 
	 
	 
	 Button groupButton = new Button("Departments");  
	 groupButton.setIconStyle("icon-dm"); 
	 add(groupButton); 
	 groupButton.addListener(Events.Select,new Listener<ButtonEvent>() {
		 @Override public void handleEvent(ButtonEvent be) {
 		 	  dispatcher.dispatch(AdminEvents.AdminDepartment);
	      }  
	    });
	 
	 add(new SeparatorToolItem());
	 
	 Button projectsButton = new Button("Assignments");
	 projectsButton.setIconStyle("icon-pm"); 
	 projectsButton.addListener(Events.Select,new Listener<ButtonEvent>() {
		 @Override public void handleEvent(ButtonEvent be) {
 		 	  dispatcher.dispatch(AdminEvents.AdminAssignment);
	      }  
	    });
	 
	 
	 add(projectsButton); 
	 add(new SeparatorToolItem()); 
	 
	 
	 
	 Button admButton = new Button("Settings");
	 admButton.setIconStyle("icon-admin"); 
	 admButton.addListener(Events.Select,new Listener<ButtonEvent>() {
		 @Override public void handleEvent(ButtonEvent be) {
			 dispatcher.dispatch(AdminEvents.AdminSettings);
	      }  
	    });
	 add(admButton);
	 add(new SeparatorToolItem());
	 
	 Button calendarButton = new Button("Calendar");
	 calendarButton.setIcon(Resources.ICONS.calendar_icon()); 
	 calendarButton.addListener(Events.Select,new Listener<ButtonEvent>() {
		 @Override public void handleEvent(ButtonEvent be) {
			 new HolidayPanel();
	      }  
	    });
	 add(calendarButton);
	 add(new SeparatorToolItem());
	 
	 Button branchButton = new Button("Branches");
	 branchButton.setIcon(Resources.ICONS.calendar()); 
	 branchButton.addListener(Events.Select,new Listener<ButtonEvent>() {
		 @Override public void handleEvent(ButtonEvent be) {
			 dispatcher.dispatch(AdminEvents.AdminBranch);
	      }  
	    });
	 add(branchButton);
	 add(new SeparatorToolItem());
	 
	 
	 
	 Button toolsButton = new Button("Tools");
	 toolsButton.setIcon(Resources.ICONS.tools_icon()); 
	 toolsButton.addListener(Events.Select,new Listener<ButtonEvent>() {
		 @Override public void handleEvent(ButtonEvent be) {
			 dispatcher.dispatch(AdminEvents.AdminTools);
	      }  
	    });
	 add(toolsButton);
	 add(new SeparatorToolItem());
	 
	 
	 
	 
	 add(new FillToolItem());
	 add(new SeparatorToolItem());
	 
	 
	 
	 Button logoutButton = new Button("Logout");
	 logoutButton.setIconStyle("icon-logout");
	 
	 logoutButton.addListener(Events.Select,new Listener<ButtonEvent>() {
		 @Override public void handleEvent(ButtonEvent be) {
			 
				Window.Location.assign(GWT.getHostPageBaseURL().toString() + "j_spring_security_logout");
	      }  
	    });  
	 add(logoutButton); 
	 add(new SeparatorToolItem());
	 
   	 
   	 
   	 
   	 
   	 
   	 
   	 
	}
	
	public String getName(){
		return this.name;
	}

}
