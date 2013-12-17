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

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;


/**TODO
 * Reload Users after new export
 * OR just reload users after select
 * @author chani
 *
 */


public class UserTab extends TabPanel{
	TabItem pm,importTab;
	private PermissionFormGrid  permissionPanel = new PermissionFormGrid();;
	private UserFormGrid 		userFormGrid    = new UserFormGrid();
	private ImportUsersFromExternalStoreGrid importUsersFromExternalStoreGrid = new ImportUsersFromExternalStoreGrid();
	
	public UserTab(){
		
		TabItem um = new TabItem("Users Managment");
		um.addListener(Events.Select,new Listener<ComponentEvent>() {  
		      public void handleEvent(ComponentEvent be) {
		    	  userFormGrid.reloadGrid("1");
		    	 		      }  
		    });  
		um.add(userFormGrid);
		
		add(um);
		
		importTab = new TabItem("Import Users");  
		importTab.add(importUsersFromExternalStoreGrid);
		
	    add(importTab);
		
		
		
		final TabItem pm = new TabItem("Permission Managment");
		pm.addListener(Events.Select, new Listener<ComponentEvent>() {  
		      public void handleEvent(ComponentEvent be) {
		    //	  permissionPanel.reloadGrid();
		    	 		      }  
		    });  
		
		
		   pm.add(permissionPanel);
		   add(pm);
		       
		      }
		   
		
		
	}
	
	
	

