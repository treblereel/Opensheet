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

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.opensheet.client.services.HourService;
import org.opensheet.client.services.HourServiceAsync;
import org.opensheet.client.utils.Resources;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.DatePickerEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserDepartmentReportContentPanel extends ContentPanel{
	private TreeGrid<ModelData> userTreeGrid;
	private TreeStore<ModelData> userStore;
	private Date starting;
	private Date finishing;
	private DateField startingDateField;
	private DateField finishingDateField;
	private Integer assignmentId,departmentId;
	
	@SuppressWarnings("deprecation")
	public UserDepartmentReportContentPanel(){
		setFrame(true);
		setWidth(724);
		setHeight(600);
		setHeadingHtml("Department Users: ");
	
		
		starting = new Date();
		starting.setYear(starting.getYear()-5);
		starting.setDate(1);
		
		finishing = new Date();
		
		ToolBar toolbar  =  new ToolBar();
		Label startingLabel = new Label(" Starting : ");
		Label finishingLabel = new Label(" Finishing : ");
		
		startingDateField = new DateField();  
		startingDateField.setAllowBlank(false);
		startingDateField.setValue(starting);
		startingDateField.getDatePicker().addListener(Events.Select, new Listener<DatePickerEvent>() {
            public void handleEvent(DatePickerEvent be) {
            	System.out.println("started");
            	loadData();
            }
            
        });
		
		finishingDateField = new DateField();  
		finishingDateField.setAllowBlank(false);
		finishingDateField.setValue(finishing);
		finishingDateField.getDatePicker().addListener(Events.Select, new Listener<DatePickerEvent>() {
            public void handleEvent(DatePickerEvent be) {
            	loadData();
            }
            
        });
		
		Button export = new Button("Xls export");
		export.setIcon(Resources.ICONS.table()); 
		export.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override public void handleEvent(BaseEvent be) {
				Date start  = startingDateField.getValue();
				Date end = finishingDateField.getValue();
				Window.Location.assign(GWT.getHostPageBaseURL().toString() +"departmentreporttoxls.htm?assignment="
						+ assignmentId + "&department="+
						departmentId+"&s_year="+start.getYear()+"&s_month="+start.getMonth()+
						"&s_day="+start.getDate()+"&e_year="+end.getYear()+"&e_month="
						+end.getMonth()+"&e_day="+end.getDate());
						
			}
	    });
		
		
		toolbar.add(startingLabel );
		toolbar.add(startingDateField) ;
		toolbar.add(finishingLabel );
		toolbar.add(finishingDateField) ;
		
		
		toolbar.add(new SeparatorToolItem());
		toolbar.add(export); 
		setTopComponent(toolbar);
		add(doUserTreeGrid());
	}
	
	private TreeGrid<ModelData> doUserTreeGrid(){
		userStore = new TreeStore<ModelData>();
		
		ColumnConfig name = new ColumnConfig("name", "Name", 100);
	    name.setRenderer(new TreeGridCellRenderer<ModelData>());  
	    name.setFixed(true);
	    ColumnConfig assignment = new ColumnConfig("assignment", "Assignment", 300);
	    assignment.setFixed(true);
	    assignment.setSortable(false);
	    assignment.setMenuDisabled(true);
	    
	    ColumnConfig hour = new ColumnConfig("hour", "Hours", 50);
	    ColumnConfig inratesum = new ColumnConfig("inratesum", "Inratesum", 70);  
	    ColumnConfig extratesum = new ColumnConfig("extratesum", "Extratesum", 70);  
	    ColumnModel cm = new ColumnModel(Arrays.asList(name, assignment, hour,inratesum,extratesum)); 
		
		userTreeGrid = new TreeGrid<ModelData>(userStore, cm);  
		userTreeGrid.setBorders(true);  
		userTreeGrid.setAutoExpandColumn("name");  
		userTreeGrid.setTrackMouseOver(false);
		userTreeGrid.setLoadMask(true);
		userTreeGrid.setWidth(700);
		userTreeGrid.setHeight(500);
		userTreeGrid.setBorders(true);
		userTreeGrid.getView().setEmptyText("No users assigned");
		userTreeGrid.setId("myUserTreeGridid");
		
		
		return userTreeGrid;
	}
	
	public  void loadUserTreeGrid(Integer assignmentId,Integer departmentId, Date starting, Date finishing){
			if(assignmentId != null && departmentId != null){
				if(starting.compareTo(finishing) > 0 && finishing.compareTo(starting) < 0){
					MessageBox.info("ERROR"," Starting Data must be less whan Finishing Data",null);
				}else{
					 userTreeGrid.getStore().removeAll();
					 final HourServiceAsync hourService = GWT.create(HourService.class);
					 hourService.getHours(assignmentId, departmentId,starting, finishing, new AsyncCallback<BaseTreeModel>() {
								public void onFailure(Throwable caught) {
										MessageBox.info("Cant get users information",caught.getMessage() , null);
						}
						public void onSuccess(BaseTreeModel model) {
							userStore.add(model.getChildren(), true);
						}
					});
			 }
			}		
		}
	
	
	private void loadData(){
		Date starting  = startingDateField.getValue();
		Date finishing = finishingDateField.getValue();
		
		loadUserTreeGrid(assignmentId,departmentId,starting,finishing);
	}
	
	
	public void loadData(Integer assignmentId,Integer departmentId){
		Date starting  = startingDateField.getValue();
		Date finishing = finishingDateField.getValue();
		this.assignmentId = assignmentId;
		this.departmentId = departmentId;
		loadUserTreeGrid(assignmentId,departmentId,starting,finishing);
	}
	
	

}
