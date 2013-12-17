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
import org.opensheet.client.widges.BranchComboBox;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AddDepartmentToAssignmentWindow extends Window {
	private BranchComboBox branchComboBox;
	private Integer branchId = 9999999;
	private Integer assignmentId;
	private Grid<BaseModel> departmentGrid;
	private DepartmentServiceAsync  departmentService  = GWT.create(DepartmentService.class);
	private ProjectPanel projectPanel;
	
	public AddDepartmentToAssignmentWindow(final Integer assignmentId,final ProjectPanel projectPanel){
		this.projectPanel=projectPanel;
		
		setWidth(310);
		setHeight(300);
	//	setFrame(true);
		setClosable(true);
		setHeaderVisible(true);
		setBodyBorder(true);
		this.setOnEsc(true);
		setButtonAlign(HorizontalAlignment.CENTER); 
		
	
		setLayout(new ColumnLayout());
		doDepartmentPanel();
		setData(assignmentId);
		show();
		
		
	}
	
	private void doDepartmentPanel(){
		ToolBar tb = new ToolBar();
		branchComboBox =  new BranchComboBox(true);
		branchComboBox.setEmptyText("all");
		branchComboBox.addSelectionChangedListener(new SelectionChangedListener<BeanModel>(){
			@Override	public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
				branchId = Integer.parseInt(se.getSelectedItem().get("id").toString());	
				populateDepartmentCheckGrid();
			}
			
		});
		tb.add(new LabelToolItem("Choose Branch: "));
		tb.add(branchComboBox);
		setTopComponent(tb);
		doDepartmentCheckGrid();
	}
	
	private void doDepartmentCheckGrid(){
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
		   departmentGrid.setWidth(300);
		   departmentGrid.setHeight(240);
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
				
				
				departmentService.setDepartmentToAssignmentByBranch(departmentGrid.getSelectionModel().getSelectedItems(),assignmentId, branchId, new AsyncCallback<Void>(){
					@Override	public void onFailure(Throwable caught) {	
						Info.display("Failed", "something wrong");
						projectPanel.assignmentDepartmentPanelSetData(assignmentId);

					}
					@Override 	public void onSuccess(Void result) {
						Info.display("Succesfull", "Departments processed !");
						projectPanel.assignmentDepartmentPanelSetData(assignmentId);

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
					 populateDepartmentCheckGrid();
				 }
			}
		   });
		   departmentGridFooter.add(reloadDeps);
		   setBottomComponent(departmentGridFooter);
		
		
		
		
	}
	
	public void setData(Integer assignmentId){
		this.assignmentId = assignmentId;
		populateDepartmentCheckGrid();
	}
	
	private void populateDepartmentCheckGrid(){
		departmentService.getDepartmentsBaseModelByAssignemntAndByBranch(assignmentId,branchId, new AsyncCallback<List<BaseModel>>(){
			@Override	public void onFailure(Throwable caught) {	}
			@Override	public void onSuccess(List<BaseModel> result) {
				departmentGrid.getStore().removeAll();
				if(result != null){
					departmentGrid.getStore().add(result);
					for(BaseModel bm: result){
						if(bm.get("selected").equals(true)) {
							departmentGrid.getSelectionModel().select(bm, true);
						}
					}
				}else{
						MessageBox.info("Error", "answer is null", null);
				}
			}
				
			
		});
	}
}
