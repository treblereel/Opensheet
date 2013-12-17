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
import java.util.Date;
import java.util.List;

import org.opensheet.client.services.DepartmentService;
import org.opensheet.client.services.DepartmentServiceAsync;
import org.opensheet.client.services.StatService;
import org.opensheet.client.services.StatServiceAsync;
import org.opensheet.client.services.TimelineService;
import org.opensheet.client.services.TimelineServiceAsync;
import org.opensheet.client.widges.MonthChart;

import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DepartmentTimeline extends ContentPanel{
	private Integer assignmentId;
	private Integer departmentId;
	private NumberField timelineField;
	private Window timelineErrorWindow;
	private Button process,reload;
	private TimelineServiceAsync    timelineService    = GWT.create(TimelineService.class);
	private Grid<BaseModel> departmentGrid;
	private Date date;
	private DepartmentServiceAsync  departmentService  = GWT.create(DepartmentService.class);
	private StatServiceAsync        chartService       = GWT.create(StatService.class);

	
	private ContentPanel  panel;
	
	
	public DepartmentTimeline(ContentPanel cp){
		this.panel = cp;
		setFrame(true);
		setHeaderVisible(false);
		setCollapsible(false);  
		setSize(268, 270);
		setFooter(true);
		drawGrid();
	}

	

	
		private void drawGrid(){
			
			date = new Date();
			List<ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
			ColumnConfig depsConfig = new ColumnConfig("name", "Department", 180);
			depsConfig.setSortable(false);
			depsConfig.setMenuDisabled(true);
			configs.add(depsConfig);
			ColumnModel cm = new ColumnModel(configs);
			
			departmentGrid = new Grid<BaseModel>(new ListStore<BaseModel>(), cm); 
			departmentGrid.setLoadMask(true);
			;
			departmentGrid.setWidth(268);
			departmentGrid.setHeight(230);
			
			
			departmentGrid.addStyleName(".my-table-style");
			departmentGrid.setBorders(true);
			departmentGrid.setAutoExpandColumn("name");
			departmentGrid.getView().setEmptyText("no data");
			departmentGrid.setId("departmentDetailsGridId");
			departmentGrid.getSelectionModel().addListener(Events.SelectionChange,  new Listener<SelectionChangedEvent<ModelData>>() {
				@SuppressWarnings("deprecation")
				@Override	   public void handleEvent(SelectionChangedEvent<ModelData> be) { 
			              if (be.getSelection().size() > 0) {
			            	  enable();
			            	  departmentId = (Integer) departmentGrid.getSelectionModel().getSelectedItem().get("id");		  
			            	  makeRequest();
			            	  
			            	  drawAssignemntDepartmentYearChart(date.getYear(),assignmentId,departmentId);
			              
			              }else{
			            	  disable();
			              }
				   }
			   });
			
			
			ToolBar departmentGridFooter = new ToolBar();
			
			timelineField = new NumberField();
			
			timelineField.setFieldLabel("Timeline"); //dont know why this doesnt work 
			timelineField.setName("timelineField");
			timelineField.setMaxLength(11);
			timelineField.setWidth(60);
			departmentGridFooter.add(timelineField);

			process =  new Button("Save");
			process.setWidth(50);
			process.addListener(Events.Select,new Listener<BaseEvent>(){
				@Override	public void handleEvent(BaseEvent be) {
					if(timelineField.isValid())
					timelineService.setAssignmentDepartmentTimeline(assignmentId, departmentId,(Integer) timelineField.getValue().intValue(), false,new AsyncCallback<BaseModel>(){
						@Override	public void onFailure(Throwable caught) {		}
						@Override	public void onSuccess(BaseModel result) {
							if(result.get("type").equals("error")){
								showError(result);
							}else{
								Info.display("Success", "Ok");
							}
						}
					});
				}
			});
			
			departmentGridFooter.add(process);
			setBottomComponent(departmentGridFooter);
			
			add(departmentGrid);
			
		}
		
	
	
	
		public void setData(Integer assignemnt){
			timelineField.setValue(null);
			this.assignmentId = assignemnt;
			populateDepartmentGrid();
			
		
		}
	
		
		private void makeRequest(){
			
			timelineService.getAssignmentDepartmentTimeline(assignmentId,departmentId,new AsyncCallback<Integer>(){

				@Override	public void onFailure(Throwable caught) {		}
				@Override	public void onSuccess(Integer result) {
					timelineField.setValue(result);
				
				}
				
			});
		}
		
		private void populateDepartmentGrid(){
			departmentService.getDepartmentsBaseModelByAssignemnt(assignmentId,false, new AsyncCallback<List<BaseModel>>(){
				@Override	public void onFailure(Throwable caught) {	}
				@Override	public void onSuccess(List<BaseModel> result) {
					departmentGrid.getStore().removeAll();
					departmentGrid.getStore().add(result);
				
				}
			});
		}
		
		
		private void showError(BaseModel result){
			
			timelineErrorWindow =  new Window();
			timelineErrorWindow.setWidth(250);
			timelineErrorWindow.setHeight(180);
			timelineErrorWindow.setResizable(false);
			timelineErrorWindow.setHeadingHtml("ERROR");
			timelineErrorWindow.setClosable(false);
			timelineErrorWindow.addText(result.get("string").toString());
			
			Button btn = new Button("Cancel");
			btn.addListener(Events.Select, new Listener<BaseEvent>(){
				@Override	public void handleEvent(BaseEvent be) {
					timelineErrorWindow.hide();	
					makeRequest();
				}
				
			});
			
			timelineErrorWindow.addButton(btn);
			timelineErrorWindow.show();
		}
		
		public void clearForm(){
			timelineField.setValue(null);	
		}
		
		public void enable(){
			timelineField.enable();
			process.enable();
		}
		
		public void disable(){
			clearForm();
			timelineField.disable();
			process.disable();
		
		}
		
		private void  drawAssignemntDepartmentYearChart(int year,int assignemntId,int departmentId){
			final Chart chart = new Chart("resources/chart/open-flash-chart.swf");
			chart.setId("chartId");
			chartService.getAssignemntDepartmentStatByYear(year+1900,assignemntId,departmentId,new AsyncCallback<List<Number>>(){
				@Override	public void onFailure(Throwable caught) {	}
				@Override	public void onSuccess(List<Number> result) {
					
					panel.removeAll();
					MonthChart f = new MonthChart();
					chart.setHeight(220);
					chart.setChartModel(f.getChartModel(result));
					panel.add(chart);
					panel.layout();
		
				}
			});
		}
		
		
}
