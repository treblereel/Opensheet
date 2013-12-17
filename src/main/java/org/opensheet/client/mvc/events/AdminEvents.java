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
package org.opensheet.client.mvc.events;

import com.extjs.gxt.ui.client.event.EventType;

public class AdminEvents {
	
	public static final EventType Init = new EventType();
	public static final EventType App = new EventType();
	public static final EventType AdminUser = new EventType();
	public static final EventType AdminDepartment = new EventType();
	public static final EventType AdminAssignment = new EventType();
	public static final EventType AdminSettings = new EventType();
	public static final EventType AdminBranch = new EventType();
	public static final EventType AdminTools = new EventType();
	
	public static final EventType AdminAssignmentDepartmentUserSwitch = new EventType();

	
}
