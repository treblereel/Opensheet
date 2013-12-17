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

import org.opensheet.client.services.DepartmentService;
import org.opensheet.client.services.DepartmentServiceAsync;
import org.opensheet.client.utils.Resources;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class DepartmentCheckGrid extends ContentPanel{
	private DepartmentServiceAsync  departmentService  = GWT.create(DepartmentService.class);
	private  Grid<BaseModel> departmentGrid;
	private  Integer assignmentId;
	
	public DepartmentCheckGrid(){
		
		setFrame(true);
		setHeaderVisible(false);
		setCollapsible(false);  
		setSize(268, 270);
		setFooter(true);
		
		
		
		 final  CheckBoxSelectionModel<BaseModel> sm = new CheckBoxSelectionModel<BaseModel>();  
		    sm.getColumn().setMenuDisabled(true);
		    sm.getColumn().setSortable(false);
		    sm.getColumn().setDataIndex("selected");
		    sm.setSelectionMode(SelectionMode.SIMPLE);
		    
		    
		    List<ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
			configs.add(sm.getColumn());


			
			ColumnConfig depsConfig = new ColumnConfig("name", "Department", 150);
			depsConfig.setSortable(false);
			depsConfig.setMenuDisabled(true);
			configs.add(depsConfig);
			
		
			
		    ColumnModel cm = new ColumnModel(configs);
		    
		   departmentGrid = new Grid<BaseModel>(new ListStore<BaseModel>(), cm); 
		   departmentGrid.setLoadMask(true);
		   departmentGrid.setWidth(268);
		   departmentGrid.setHeight(230);
		   departmentGrid.addStyleName(".my-table-style");
		   departmentGrid.setBorders(true);
		   departmentGrid.setSelectionModel(sm);
		   
		   departmentGrid.setAutoExpandColumn("name");
		   departmentGrid.getView().setEmptyText("no data");
		   departmentGrid.setId("departmentGridId");
		   
		   add(departmentGrid);
		   
		   
		   
		   
		   ToolBar departmentGridFooter = new ToolBar();
		   
		   Button processDeps = new Button("Process");
		   processDeps.setIcon(Resources.ICONS.add());
		   processDeps.addListener(Events.Select,new Listener<BaseEvent>(){
			@Override	public void handleEvent(BaseEvent be) {
				departmentService.setAssignemntToDepartment(assignmentId, departmentGrid.getSelectionModel().getSelectedItems(), new AsyncCallback<Void>(){
					@Override	public void onFailure(Throwable caught) {	
						Info.display("Failed", "something wrong");
					}
					@Override 	public void onSuccess(Void result) {
						Info.display("Succesful", "Departments processed !");
					}
				});
			}
			   
		   });
		   
		   departmentGridFooter.add(processDeps);
		   
		   Button reloadDeps = new Button("Reload");
		   reloadDeps.setIcon(Resources.ICONS.reload());
		   reloadDeps.addListener(Events.Select, new Listener<BaseEvent>(){
			   @Override	public void handleEvent(BaseEvent be) {
				 if(assignmentId != null){
					 populateDepartmentCheckGrid(assignmentId);
				 }
			}
		   });
		   departmentGridFooter.add(reloadDeps);
		   setBottomComponent(departmentGridFooter);
		
		
	}
	
	
	private void populateDepartmentCheckGrid(Integer assignmentId){
		departmentService.getDepartmentsBaseModelByAssignemnt(assignmentId,true, new AsyncCallback<List<BaseModel>>(){
			@Override	public void onFailure(Throwable caught) {	}
			@Override	public void onSuccess(List<BaseModel> result) {
				departmentGrid.getStore().removeAll();
				departmentGrid.getStore().add(result);
				for(BaseModel bm: result){
					if(bm.get("selected").equals(true)) 
						departmentGrid.getSelectionModel().select(bm, true);
				}
			}
		});
	}
	
	
	
	
	
	public void setData(Integer assignment){
		this.assignmentId =  assignment;
		populateDepartmentCheckGrid(assignmentId);
		
	}

}
