package org.opensheet.client.mvc.views.admin;

import org.opensheet.client.mvc.events.AdminEvents;
import org.opensheet.client.mvc.views.AppView;
import org.opensheet.client.widges.admin.AdminToolBar;
import org.opensheet.client.widges.admin.tools.ToolsTab;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;

public class AdminToolsView extends View{
	 private LayoutContainer container;
		
		
		
	public AdminToolsView(Controller controller) {
		super(controller);
	}
	
	@Override
	protected void handleEvent(AppEvent event) {
		
		if (event.getType() == AdminEvents.AdminTools) {
		  	LayoutContainer toolbar = (LayoutContainer) Registry.get(AppView.NORTH_PANEL);
		  	if(toolbar.getItems().isEmpty() != true && !toolbar.getItem(0).getItemId().equalsIgnoreCase("adminToolBarId")){
				  	AdminToolBar adminToolBar = new  AdminToolBar();
				  	toolbar.removeAll();
				  	toolbar.add(adminToolBar);
				  	toolbar.layout();
		  	}else if(toolbar.getItems().isEmpty()){
		  		AdminToolBar adminToolBar = new  AdminToolBar();
		  		toolbar.add(adminToolBar);
			  	toolbar.layout();
		  	}
		
		  	  
		  	  LayoutContainer center = (LayoutContainer) Registry.get(AppView.CENTER_PANEL);
		  	  center.removeAll();
		  	  center.add(container);
		  	  center.layout();
		      return;
			    

		}	
	}

		@Override
	  protected void initialize() {
		    container = new LayoutContainer();
		    BorderLayout layout = new BorderLayout();
		    layout.setEnableState(false);
		    container.setLayout(layout);
		    container.add(new ToolsTab(), new BorderLayoutData(LayoutRegion.CENTER));		    

	}
	
}



