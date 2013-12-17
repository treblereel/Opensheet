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
package org.opensheet.client.mvc.views;

import org.opensheet.client.mvc.events.AppEvents;
import org.opensheet.client.widges.admin.AdminToolBar;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.RootPanel;

public class AppView extends View {

	  public static final String VIEWPORT = "viewport";
	  public static final String NORTH_PANEL = "north";
	  public static final String CENTER_PANEL = "center";
	  

		Dispatcher dispatcher = Dispatcher.get();

	  public AdminToolBar adminToolBar;
		
	  private Viewport viewport;
	  private LayoutContainer north;
	  private LayoutContainer center;

	  public AppView(Controller controller) {
	    super(controller);
	  }

	  protected void initialize() {

	  }

	  private void initUI() {
        viewport = new Viewport();
	    viewport.setLayout(new BorderLayout());

	    createNorth();
	    createCenter();

	
	    
	    
	    
	    Registry.register(VIEWPORT, viewport);
	    Registry.register(NORTH_PANEL, north);
	    Registry.register(CENTER_PANEL, center);
	    RootPanel.get().add(viewport);
	    dispatcher.dispatch( AppEvents.Sheet);
	  }

	  private void createNorth() {
	  	north = new LayoutContainer();
	  //	north.setLayout(new RowLayout());
		//  	north.setLayout(new FitLayout());

	  	north.setLayout(new FitLayout());
	//  	new BorderLayout()
	  	
        BorderLayoutData adminToolbarLayoutData = new BorderLayoutData(LayoutRegion.NORTH, 25);
        adminToolbarLayoutData.setMargins(new Margins(5));
	    
        viewport.add(north, adminToolbarLayoutData);
	  }

	
	  private void createCenter() {
		  
	    center = new LayoutContainer();
	//    center.setLayout(new RowLayout());
	    center.setLayout(new FitLayout());

	    BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
	    data.setMargins(new Margins(5, 5, 5, 5));
	    
	    viewport.add(center, data);
	  }

	  protected void handleEvent(AppEvent event) {
	    if (event.getType() == AppEvents.Init) {
	      initUI();
	    }
	  }

	}
