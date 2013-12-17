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
package org.opensheet.client.widges.project;

import java.util.ArrayList;
import java.util.List;

import org.opensheet.client.dto.UserDTO;
import org.opensheet.client.services.AssignmentService;
import org.opensheet.client.services.AssignmentServiceAsync;
import org.opensheet.client.utils.Resources;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AssignmentUsers extends Window{
	private Integer assignment;
	private Grid<BaseModel> departmentGrid;
	private AssignmentServiceAsync  assignmnetService  = GWT.create(AssignmentService.class);

	
	public AssignmentUsers(Integer assignment){
		
		
		this.assignment = assignment;
		setHeight(500);
		setWidth(600);
		setClosable(true);
		setHeadingHtml("Assignment Users");
		add(addToolBar());
		add(doGrid());
		populateGrid();
		show();
	}
	
	private ToolBar addToolBar(){
		ToolBar tb = new ToolBar();
		 Button xlsExportButton = new Button();
		    xlsExportButton.setText("Excel Export");
		    xlsExportButton.setIcon(Resources.ICONS.table()); 
		    xlsExportButton.addListener(Events.Select, new Listener<BaseEvent>(){
				@Override
				public void handleEvent(BaseEvent be) {
					com.google.gwt.user.client.Window.Location.assign(GWT.getHostPageBaseURL().toString() +"userassignmenttoxls.htm?assignment_id="+assignment);
			
				}
		    	
		    	
		    });
		
		tb.add(xlsExportButton);
		
		return tb;
	}
	
	
	private Grid<BaseModel> doGrid(){
	    List<ColumnConfig> configs = new ArrayList<ColumnConfig>(); 

	    ColumnConfig nameConfig = new ColumnConfig("name", "Name", 250);
		nameConfig.setMenuDisabled(true);
		configs.add(nameConfig);
		
		ColumnConfig depsConfig = new ColumnConfig("department", "Department", 320);
		depsConfig.setMenuDisabled(true);
		configs.add(depsConfig);
		
	    
		ColumnModel cm = new ColumnModel(configs);
	    
		
		   departmentGrid = new Grid<BaseModel>(new ListStore<BaseModel>(), cm); 
		   departmentGrid.setLoadMask(true);
		   departmentGrid.setWidth(580);
		   departmentGrid.setHeight(440);
		   departmentGrid.addStyleName(".my-table-style");
		   departmentGrid.setBorders(true);
		   departmentGrid.setAutoExpandColumn("name");
		   departmentGrid.getView().setEmptyText("no data");
		   departmentGrid.setId("usersGridGhrtId");
		   add(departmentGrid);
		   
		   return departmentGrid;
		}
	
	
	private void populateGrid(){
		assignmnetService.getAssignmentUsers(assignment, new AsyncCallback<List<UserDTO>>(){

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.info("EROR",caught.getMessage(),null);
				
			}

			@Override
			public void onSuccess(List<UserDTO> result) {
				departmentGrid.getStore().removeAll();
				for(UserDTO u: result){
					BaseModel bm = new BaseModel();
					bm.set("id", u.getId());
					bm.set("name", u.getfullName());
					bm.set("department", u.getDepartment().getName());
					departmentGrid.getStore().add(bm);
				}
			}
		});
	}

}
