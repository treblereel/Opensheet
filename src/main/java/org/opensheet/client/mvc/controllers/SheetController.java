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
import org.opensheet.client.mvc.views.DepartmentView;
import org.opensheet.client.mvc.views.ProjectView;
import org.opensheet.client.mvc.views.ReportView;
import org.opensheet.client.mvc.views.SheetView;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;

public class SheetController extends Controller {

	private View sheetView;
	private View projectView;
	private View reportView;
	private View departmentView;

	public SheetController() {
		registerEventTypes(AppEvents.Init);
		registerEventTypes(AppEvents.Sheet);
		registerEventTypes(AppEvents.Project);
		registerEventTypes(AppEvents.Financial);
		registerEventTypes(AppEvents.Department);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		 if (type == AppEvents.Sheet){
			 forwardToView(sheetView, event); 
		 }else if(type == AppEvents.Project){
			 forwardToView(projectView, event);
			 
		 }else if(type == AppEvents.Financial){
			 forwardToView(reportView, event);
			 
		 }else if(type == AppEvents.Department){
			 forwardToView(departmentView, event);
			 
		 }
		
		
	}

	@Override
	public void initialize() {
		super.initialize();
		sheetView = new SheetView(this);
		projectView = new ProjectView(this);
		reportView = new ReportView(this);
		departmentView = new DepartmentView(this);
		
	}
}
