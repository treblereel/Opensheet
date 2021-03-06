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
package org.opensheet.client.mvc.views.admin;



import org.opensheet.client.mvc.events.AdminEvents;
import org.opensheet.client.mvc.views.AppView;
import org.opensheet.client.widges.admin.AdminToolBar;
import org.opensheet.client.widges.admin.UserTab;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;

public class AdminUserView extends View{

	
	 private LayoutContainer container;
	Dispatcher dispatcher = Dispatcher.get();


	public AdminUserView(Controller controller) {
		super(controller);
	}
	
	

	@Override
	protected void handleEvent(AppEvent event) {

		
		if (event.getType() == AdminEvents.AdminUser) {
		  	LayoutContainer toolbar = (LayoutContainer) Registry.get(AppView.NORTH_PANEL);
		  	if(toolbar.getItems().isEmpty() != true && !toolbar.getItem(0).getItemId().equalsIgnoreCase("adminToolBarId")){
				  	AdminToolBar adminToolBar = new  AdminToolBar();
				  	toolbar.removeAll();
				  	toolbar.add(adminToolBar);
				  	toolbar.layout();
		  	}else if(toolbar.getItems().isEmpty()){
		  		AdminToolBar adminToolBar = new  AdminToolBar();
		  		toolbar.add(adminToolBar);
			  	toolbar.layout();
		  	}
		
		  	  
		  	  LayoutContainer center = (LayoutContainer) Registry.get(AppView.CENTER_PANEL);
		  	  center.removeAll();
		  	  center.add(container);
		  	  center.layout();
		      return;
			    

		}	
	}

		@Override
	  protected void initialize() {
		    container = new LayoutContainer();
		    BorderLayout layout = new BorderLayout();
		    layout.setEnableState(false);
		    container.setLayout(layout);
		    container.add(new UserTab(), new BorderLayoutData(LayoutRegion.CENTER));		    

	}
	
}
