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
package org.opensheet.client.widges.report;

import org.opensheet.client.l10n.OpensheetConstants;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.google.gwt.core.client.GWT;

public class ReportPanel extends ContentPanel{
	private OpensheetConstants myConstants = (OpensheetConstants) GWT.create(OpensheetConstants.class);

	
		public ReportPanel(){
			this.setHeaderVisible(false);
			TabPanel folder = new TabPanel(); 
			folder.setHeight(700);
			
			TabItem hourReportTabItem = new TabItem( myConstants.hour_report()); 
			TabItem departmentReportTabItem = new TabItem( myConstants.department_report()); 

			hourReportTabItem.add(new HourUserReport());
			departmentReportTabItem.add(new DepartmentReport());
			
			folder.add(hourReportTabItem);
			folder.add(departmentReportTabItem);
			
			add(folder);
		}
		
		
	
}
