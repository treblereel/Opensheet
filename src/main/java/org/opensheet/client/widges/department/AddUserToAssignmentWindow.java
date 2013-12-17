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
package org.opensheet.client.widges.department;

import java.util.ArrayList;
import java.util.List;

import org.opensheet.client.services.DepartmentService;
import org.opensheet.client.services.DepartmentServiceAsync;
import org.opensheet.client.widges.Reloadable;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AddUserToAssignmentWindow extends Window{
	private Grid<BaseModel> userAssignmentCheckGrid;
	private DepartmentServiceAsync  departmentService  = GWT.create(DepartmentService.class);
	private Integer departmentId;
	private Integer assignmentId;
	private Reloadable panel;
	
	
	public AddUserToAssignmentWindow(Integer assignment,Integer department,Reloadable p){
		
		this.assignmentId = assignment;
		this.departmentId = department;
		this.panel = p;
		
		setFrame(true);
		setHeight(450);
		setWidth(300);
		setHeadingHtml("Users in assignment: ");
		setHeaderVisible(true);
		setClosable(true);
		setResizable(false);
		
		 Button saveButton = new Button("Save");
		 saveButton.addListener(Events.Select,new Listener<BaseEvent>(){
			@Override	public void handleEvent(BaseEvent be) {
			List<BaseModel> selectedItems =	userAssignmentCheckGrid.getSelectionModel().getSelectedItems();
				departmentService.setUsersByDepartmentAndByAssignment(selectedItems,
						departmentId, assignmentId,new  AsyncCallback<Void>(){
							@Override	public void onFailure(Throwable caught) {
		                         MessageBox.alert("Cannot add Users", caught.getMessage(), null);
							}
							@Override	public void onSuccess(Void result) {
									Info.display("Success", "it's ok");	
									loadUserAssignmentCheckGrid();	
									panel.reload();
							}
				});
			}
		 });
		 Button relaodButton = new Button("Reload");
		 relaodButton.addListener(Events.Select,new Listener<BaseEvent>(){
				@Override public void handleEvent(BaseEvent be) {
					loadUserAssignmentCheckGrid();				}
			 });
		 Button closeButton = new Button("Close");
		 closeButton.addListener(Events.Select,new Listener<BaseEvent>(){
				@Override public void handleEvent(BaseEvent be) {
					hide();
					}
			 });
		 
		 add(doUserAssignmentCheckGrid());
		 addButton(saveButton);
		 addButton(relaodButton);
		 addButton(closeButton);
		 loadUserAssignmentCheckGrid();
		 show();
	}
	
	
		private void loadUserAssignmentCheckGrid(){
				departmentService.getUsersByDepartmentAndByAssignment(departmentId,assignmentId,new AsyncCallback<List<BaseModel>>(){
					@Override	public void onFailure(Throwable caught) {
				 		Info.display("Error", caught.toString());
					}
					@Override
					public void onSuccess(List<BaseModel> result) {
						userAssignmentCheckGrid.getStore().removeAll();
						userAssignmentCheckGrid.getStore().add(result);
						for(BaseModel bm: result){
							if(bm.get("selected").equals(true)) 
								userAssignmentCheckGrid.getSelectionModel().select(bm, true);
						}
					}
				});
		}
		
			private Grid<BaseModel> doUserAssignmentCheckGrid(){
			
			
			 final  CheckBoxSelectionModel<BaseModel> sm = new CheckBoxSelectionModel<BaseModel>();  
			 sm.getColumn().setMenuDisabled(true);
			 sm.getColumn().setFixed(true);
			 sm.setSelectionMode(SelectionMode.SIMPLE);
			 sm.getColumn().setSortable(false);
			 sm.getColumn().setDataIndex("selected");
			 List<ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
			 configs.add(sm.getColumn());
		
		
				
			 ColumnConfig userConfig = new ColumnConfig("name", "User", 150);
			 userConfig.setFixed(true);
			 userConfig.setSortable(false);
			 userConfig.setMenuDisabled(true);
		     configs.add(userConfig);
			 ColumnModel cm = new ColumnModel(configs);
		
			 userAssignmentCheckGrid = new Grid<BaseModel>(new ListStore<BaseModel>(), cm); 
			 userAssignmentCheckGrid.setLoadMask(true);
			 userAssignmentCheckGrid.setWidth(288);
			 userAssignmentCheckGrid.setHeight(400);
			 userAssignmentCheckGrid.addStyleName(".my-table-style");
			 userAssignmentCheckGrid.setBorders(true);
			 userAssignmentCheckGrid.setSelectionModel(sm);
			 userAssignmentCheckGrid.setAutoExpandColumn("name");
			 userAssignmentCheckGrid.getView().setEmptyText("no data");
			 userAssignmentCheckGrid.setId("userAssignmentCheckGridId");
			return userAssignmentCheckGrid;
		}


}
