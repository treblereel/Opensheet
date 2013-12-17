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

import org.opensheet.client.exceptions.ParentTimelineIsNullException;
import org.opensheet.client.exceptions.ParentTimelineTooSmallException;
import org.opensheet.client.services.DepartmentService;
import org.opensheet.client.services.DepartmentServiceAsync;
import org.opensheet.client.services.TimelineService;
import org.opensheet.client.services.TimelineServiceAsync;
import org.opensheet.client.utils.Resources;
import org.opensheet.client.widges.BranchComboBox;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.SelectionModel;

public class AssignmentDepartmentPanel extends ContentPanel {
	private BranchComboBox branchComboBox;
	private Integer branchId = 9999999;
	private Integer assignmentId,departmentId;
	private Grid<BaseModel> departmentGrid;
	private DepartmentServiceAsync  departmentService  = GWT.create(DepartmentService.class);
	final private TimelineServiceAsync     timelineService = GWT.create(TimelineService.class);
	private FormPanel fp;
	private NumberField timelineField,timelineOverTimeField,hourSumField;
	
	public AssignmentDepartmentPanel(){
		setWidth(700);
		setHeight(300);
		setFrame(true);
		setHeaderVisible(false);
		setBodyBorder(false);
		setButtonAlign(HorizontalAlignment.CENTER); 
		
	
		setLayout(new ColumnLayout());
		doDepartmentPanel();
		doDetailPanel();
		
	}
	
	private void doDepartmentPanel(){
		ToolBar tb = new ToolBar();
		branchComboBox =  new BranchComboBox(true);
		branchComboBox.setEmptyText("all");
		branchComboBox.addSelectionChangedListener(new SelectionChangedListener<BeanModel>(){
			@Override	public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
				branchId = Integer.parseInt(se.getSelectedItem().get("id").toString());	
				populateDepartmentGrid();
			}
			
		});
		tb.add(new LabelToolItem("Choose Branch: "));
		tb.add(branchComboBox);
		
		 Button xlsExportButton = new Button();
		    xlsExportButton.setText("Excel Export");
		    xlsExportButton.setIcon(Resources.ICONS.table()); 
		    xlsExportButton.addListener(Events.Select, new Listener<BaseEvent>(){
				@Override
				public void handleEvent(BaseEvent be) {
					com.google.gwt.user.client.Window.Location.assign(GWT.getHostPageBaseURL().toString() +"departmentassignmenttoxls.htm?assignment_id="+assignmentId+"&branch_id"+branchId);
			
				}
		    	
		    	
		    });
		
		tb.add(xlsExportButton);
		
		setTopComponent(tb);
		doDepartmentGrid();
	}
	
	private void doDepartmentGrid(){
		    List<ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
			ColumnConfig depsConfig = new ColumnConfig("name", "Department", 150);
			depsConfig.setSortable(false);
			depsConfig.setMenuDisabled(true);
			configs.add(depsConfig);
		    ColumnModel cm = new ColumnModel(configs);
		    
		   departmentGrid = new Grid<BaseModel>(new ListStore<BaseModel>(), cm); 
		   departmentGrid.setLoadMask(true);
		   departmentGrid.setWidth(300);
		   departmentGrid.setHeight(260);
		   departmentGrid.addStyleName(".my-table-style");
		   departmentGrid.setBorders(true);
		   departmentGrid.setAutoExpandColumn("name");
		   departmentGrid.getView().setEmptyText("no data");
		   departmentGrid.setId("departmentGridId");
		   departmentGrid.getSelectionModel().addListener(Events.SelectionChange,  new Listener<SelectionChangedEvent<ModelData>>() {  
				public void handleEvent(SelectionChangedEvent<ModelData> be) { 
	              if (be.getSelection().size() > 0) {
	            	  departmentId = Integer.parseInt(departmentGrid.getSelectionModel().getSelectedItem().get("id").toString());
	      			  populateDepartmentTimelinePanel(); 
	              }
				}
		   });
	
		   add(departmentGrid);
		   
	}
	
	public void setData(Integer assignmentId){
		this.assignmentId = assignmentId;
		populateDepartmentGrid();
	}
	
	private void populateDepartmentGrid(){
		departmentService.getDepartmentsBaseModelByAssignemntAndByBranch(assignmentId,branchId, new AsyncCallback<List<BaseModel>>(){
			@Override	public void onFailure(Throwable caught) {	}
			@Override	public void onSuccess(List<BaseModel> result) {
				departmentGrid.getStore().removeAll();
				if(result != null){
					for(BaseModel bm: result){
						if(bm.get("selected").equals(true)) {
							departmentGrid.getStore().add(bm);
						}
					}
				}else{
						MessageBox.info("Error", "answer is null", null);
				}
			}
		});
	}
	
	private void doDetailPanel(){
		fp = new FormPanel();
		fp.setHeight(260);
		fp.setWidth(300);
		fp.setHeaderVisible(false);
		
		LayoutContainer main = new LayoutContainer();  
	    main.setLayout(new ColumnLayout()); 
	    
	    LayoutContainer left = new LayoutContainer();  
	    left.setStyleAttribute("paddingRight", "10px");  
	    FormLayout layout = new FormLayout();  
	    layout.setLabelAlign(LabelAlign.TOP);  
	    left.setLayout(layout); 
	    
	    LayoutContainer right = new LayoutContainer();  
	    right.setStyleAttribute("paddingLeft", "10px");  
	    layout = new FormLayout();  
	    layout.setLabelAlign(LabelAlign.TOP);  
	    right.setLayout(layout);  

	    timelineField = new NumberField();
	    timelineField.setFieldLabel("Timeline");
	    timelineField.setEditable(true);
	    left.add(timelineField,new FormData(100, 22));
	    
	    hourSumField = new NumberField();
	    hourSumField.setFieldLabel("Hour Sum");
	    hourSumField.setEditable(false);
	    right.add(hourSumField,new FormData(100, 22));
		    
	    timelineOverTimeField = new NumberField();
	    timelineOverTimeField.setFieldLabel("Over Time");
	    timelineOverTimeField.setEditable(false);
	    right.add(timelineOverTimeField,new FormData(100, 22));
		
	    
	    
	    main.add(left,   new ColumnData(.5)); 
	    main.add(right, new ColumnData(.5));
	    fp.add(main);
	    
	    
	    
	    
	    Button save = new Button("Save");
	    save.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override	public void componentSelected(ButtonEvent ce) {
				System.out.println("click");
				setAssignmentDepartmentTimeline();
			}
	    	
	    });
	    
	    
	    
	    
	    
	    Button reload = new Button("Reload");
	    reload.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override	public void componentSelected(ButtonEvent ce) {
				populateDepartmentTimelinePanel();
				}
	    });
	    
	    fp.addButton(save);
	    fp.addButton(reload);
	    add(fp);
		
	}
	
	private void populateDepartmentTimelinePanel(){
		fp.clear();
		
		if(assignmentId != null && departmentId != null){
		timelineService.getAssignmentDepartmentDetail(assignmentId, departmentId, new AsyncCallback<BaseModel>(){
			@Override	public void onFailure(Throwable caught) {
				MessageBox.info("Error","Somethings wrong, ask your support",null);
			}

			@Override	public void onSuccess(BaseModel result) {
				Integer timeline = Integer.parseInt(result.get("timeline").toString());
				Integer sum =      Integer.parseInt(result.get("sum").toString());
				Integer overtime = 0;
				if(sum > timeline){
					overtime = sum - timeline;
					timelineOverTimeField.setValue(overtime);
					timelineOverTimeField.setInputStyleAttribute("color", "red");

				}
				
				timelineField.setValue(timeline);
				hourSumField.setValue(sum);
			}
			
		});
		
		
		
		}
	}
	private void setAssignmentDepartmentTimeline(){
		System.out.println("assignmentId :" + assignmentId + " departmentId :" + departmentId);
		Integer timeline = timelineField.getValue().intValue();
		if(assignmentId != null && departmentId != null){
			timelineService.setAssignmentDepartmentTimeline(assignmentId, departmentId, timeline, false, new AsyncCallback<BaseModel>(){
				@Override	public void onFailure(Throwable caught) {
					if(caught instanceof ParentTimelineIsNullException){
						MessageBox.info("Error",caught.getMessage(),null);					
					}else if(caught instanceof ParentTimelineTooSmallException){
						MessageBox.info("Error",caught.getMessage(),null);					
					}else{
						MessageBox.info("Error",caught.getMessage(),null);					

					}
				}
				@Override	public void onSuccess(BaseModel result) {
					Info.display("Success","OK");					
				}
			});
		}
	}
}
