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
package org.opensheet.client.utils;

import org.opensheet.client.utils.icons.Icons;

import com.google.gwt.core.client.GWT;

public class Resources {

	  public static final Icons ICONS = GWT.create(Icons.class);

	  
	  public static final	String[] monthName = {"January", "February","March", "April", "May", "June", "July","August", "September", "October", "November","December"};

}




//ExampleMessages messages = Hermes.get(ExampleMessages.class, "en-us");
//messages.sampleString();
