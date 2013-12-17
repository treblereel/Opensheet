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


import org.opensheet.client.mvc.events.AdminEvents;
import org.opensheet.client.mvc.views.admin.AdminDepartmentView;
import org.opensheet.client.mvc.views.admin.AdminAssignmentView;
import org.opensheet.client.mvc.views.admin.AdminUserView;
import org.opensheet.client.mvc.views.admin.AdminSettingsView;
import org.opensheet.client.mvc.views.admin.AdminBranchView;
import org.opensheet.client.mvc.views.admin.AdminToolsView;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;

public class AdminController extends Controller {

	private View adminUserView;
	private View adminDepartmentView;
	private View adminAssignmentView;
	private View adminSettingsView;
	private View adminBranchView;
	private View adminToolsView;

	public AdminController() {
		registerEventTypes(AdminEvents.AdminUser);
		registerEventTypes(AdminEvents.AdminDepartment);
		registerEventTypes(AdminEvents.AdminAssignment);
		registerEventTypes(AdminEvents.AdminSettings);
		registerEventTypes(AdminEvents.AdminBranch);
		registerEventTypes(AdminEvents.AdminTools);

	}

	@Override
	public void handleEvent(AppEvent event) {
		   EventType type = event.getType();

		 if (type == AdminEvents.AdminUser) {
			 forwardToView(adminUserView, event);
		    } else if (type == AdminEvents.AdminDepartment) {
		     forwardToView(adminDepartmentView, event);

		    }else if (type == AdminEvents.AdminAssignment) {
		     forwardToView(adminAssignmentView, event);

		    }else if (type == AdminEvents.AdminSettings) {
			     forwardToView(adminSettingsView, event);

			}else if (type == AdminEvents.AdminBranch) {
			     forwardToView(adminBranchView, event);

			}else if (type == AdminEvents.AdminTools) {
			     forwardToView(adminToolsView, event);
			}       
		
	}

	@Override
	public void initialize() {

		super.initialize();
		adminUserView = 	  new AdminUserView(this);
		adminDepartmentView = new AdminDepartmentView(this);
		adminAssignmentView = new AdminAssignmentView(this);
		adminSettingsView   = new AdminSettingsView(this);
		adminBranchView     = new AdminBranchView(this);
		adminToolsView      = new AdminToolsView(this);
		
	}
}
