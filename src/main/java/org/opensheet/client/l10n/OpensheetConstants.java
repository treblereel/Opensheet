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
package org.opensheet.client.l10n;

import com.google.gwt.i18n.client.Constants;

public interface OpensheetConstants  extends Constants {
	
	
	//ToolBar
	@DefaultStringValue("User")
	String user();
	
	@DefaultStringValue("Assignment")
	String assignment();
	
	@DefaultStringValue("My Department")
	String my_department();
	
	@DefaultStringValue("My Projects")
	String my_projects();
	
	@DefaultStringValue("My Reports")
	String my_reports();
	
	@DefaultStringValue("Administration")
	String administration();
	
	@DefaultStringValue("Logout")
	String logout();
	
	// Month
	@DefaultStringValue("January")
	String January();
	@DefaultStringValue("February")
	String February();
	@DefaultStringValue("March")
	String March();
	@DefaultStringValue("April")
	String April();
	@DefaultStringValue("May")
	String May();
	@DefaultStringValue("June")
	String June();
	@DefaultStringValue("July")
	String July();
	@DefaultStringValue("August")
	String August();
	@DefaultStringValue("September")
	String September();
	@DefaultStringValue("October")
	String October();
	@DefaultStringValue("November")
	String November();
	@DefaultStringValue("December")
	String December();
	
	//Action
	
	@DefaultStringValue("Save")
	String save();
	
	
	//Grids
	
	@DefaultStringValue("Type")
	String type();
	
	
	//Assignment
	@DefaultStringValue("Name")
	String name();
	@DefaultStringValue("Index")
	String index();
	@DefaultStringValue("Owner")
	String owner();
	@DefaultStringValue("Branch")
	String branch();
	@DefaultStringValue("Status")
	String status();
	@DefaultStringValue("Department")
	String department();
	
	@DefaultStringValue("Timeline")
	String timeline();
	
	@DefaultStringValue("Assignment details")
	String assignment_details();
	
	@DefaultStringValue("Hour note")
	String hour_note();
	
	//Reports
	
	@DefaultStringValue("Hour report")
	String hour_report();
	
	@DefaultStringValue("Department report")
	String department_report();

}
