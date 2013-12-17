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
package org.opensheet.client.mvc.controllers;


import org.opensheet.client.mvc.events.AppEvents;
import org.opensheet.client.mvc.views.AppView;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;

public class AppController extends Controller {

	private View appView;

	public AppController() {
		registerEventTypes(AppEvents.Init);


	}

	@Override
	public void handleEvent(AppEvent event) {
		
		   EventType type = event.getType();
		    if (type == AppEvents.Init) {
		      onInit(event);
		    }
		
		  }
		
		
	
	
	public void onInit(AppEvent event){
	    forwardToView(appView, event);
	}
	
	public void onApp(AppEvent event){
		forwardToView(appView, event);
	}
	

	
	

	@Override
	public void initialize() {
		appView = new AppView(this);
	}
	
}
