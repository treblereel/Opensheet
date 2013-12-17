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

import org.opensheet.client.widges.admin.settings.AuthPanel;
import org.opensheet.client.widges.admin.settings.TimesheetSettingsPanel;

import com.extjs.gxt.ui.client.Style.HideMode;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;

public class SettingsTab extends TabPanel{

	
	public SettingsTab(){
		TabItem authTab = new TabItem("Auth Settings");
		authTab.setHideMode(HideMode.OFFSETS);
		authTab.add(new AuthPanel());
		add(authTab);
		
		TabItem settingsTab = new TabItem("Timesheet Settings");
		settingsTab.setHideMode(HideMode.OFFSETS);
		settingsTab.add(new TimesheetSettingsPanel());
		add(settingsTab);
		
		
	}
	
}
