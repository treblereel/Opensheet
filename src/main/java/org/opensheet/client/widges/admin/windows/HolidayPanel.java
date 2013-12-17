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
package org.opensheet.client.widges.admin.windows;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opensheet.client.services.HolidayService;
import org.opensheet.client.services.HolidayServiceAsync;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid.ClicksToEdit;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;



public class HolidayPanel extends Window{
	
	private HolidayServiceAsync 		holidayService       = GWT.create(HolidayService.class);
	
	private ListStore<BaseModelData> store = new ListStore<BaseModelData>(); 
	
	private EditorGrid<BaseModelData> grid;
	private Date date;
	private Button thisMonthButton;
	
	@SuppressWarnings({ "deprecation"})
	public HolidayPanel(){
		
		
		date = new Date();
		
		setHeadingHtml("Set Holiday");
		setWidth("140");
		setHeight("220");
		setResizable(false);
		
		ToolBar toolBar = new ToolBar();
		toolBar.setWidth(180);
		thisMonthButton = new Button();
	    thisMonthButton.setWidth(90);
	    thisMonthButton.setText((date.getMonth()+1) + " " + (date.getYear()+1900));
	    thisMonthButton.addListener(Events.Select, new  Listener<BaseEvent>(){
			@Override public void handleEvent(BaseEvent be) {
				date = new Date();
				thisMonthButton.setText((date.getMonth()+1) + " " + (date.getYear()+1900));
				loadHoliday();
			}
	    });
		
	    Button privMonthButton = new Button();
	    privMonthButton.setWidth(30);
	    privMonthButton.setIconStyle("privMonthButton");
	    privMonthButton.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override	public void handleEvent(BaseEvent be) {
				 date.setMonth(date.getMonth()-1);
				 thisMonthButton.setText((date.getMonth()+1) + " " + (date.getYear()+1900));
				 loadHoliday();
			}
	    	
	    	
	    });
	    
	    Button nextMonthButton = new Button();
	    nextMonthButton.setWidth(30);
	    nextMonthButton.setIconStyle("nextMonthButton");
	    nextMonthButton.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override	public void handleEvent(BaseEvent be) {
				
				date.setMonth(date.getMonth()+1);
				 thisMonthButton.setText((date.getMonth()+1) + " " + (date.getYear()+1900));
				 loadHoliday();
			}
	    	
	    	
	    });
		
	    
	    toolBar.add(privMonthButton);
	    toolBar.add(thisMonthButton);
	    toolBar.add(nextMonthButton);
		add(toolBar);
		
		
		RpcProxy<List<BaseModelData>> proxy = new RpcProxy<List<BaseModelData>>() {
			@Override
            protected void load(Object loadConfig, AsyncCallback<List<BaseModelData>> callback) {
				holidayService.getHolidays(date, callback);
            }
        };    
        ListLoader<?> loader = new BaseListLoader<ListLoadResult<BaseModel>>(proxy);
        store = new ListStore<BaseModelData>(loader);
        store.setMonitorChanges(true);
        store.getLoader().load();
		
		
		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		   
		
		

	  
	    ColumnConfig day1ColumnConfig = new ColumnConfig();
	    day1ColumnConfig.setDataIndex("day_1");
	    day1ColumnConfig.setHeaderHtml("1");
	    day1ColumnConfig.setWidth(25);
	    day1ColumnConfig.setId("day1ColumnConfigId");
	    day1ColumnConfig.setMenuDisabled(true);
	    day1ColumnConfig.setSortable(false);
	    day1ColumnConfig.setEditor(new CellEditor(new NumberField())); 
	    day1ColumnConfig.setRenderer(new GridCellRenderer<BaseModelData>() {
			@Override public Object render(BaseModelData model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<BaseModelData> store, Grid<BaseModelData> grid) {
				config.css = ""; 
				if(!model.get("day_1").equals(0)){
					if(model.get("status_1").equals(true))
						config.css = "x-treegrid-column-holiday"; 
					
					return model.get("day_1");
				}
				return "<span> </span>";
			}
	    });
	    
	    columns.add(day1ColumnConfig);
	    
	    ColumnConfig day2ColumnConfig = new ColumnConfig();
	    day2ColumnConfig.setDataIndex("day_2");
	    day2ColumnConfig.setId("day2ColumnConfigId");
	    day2ColumnConfig.setHeaderHtml("2");
	    day2ColumnConfig.setWidth(25);
	    day2ColumnConfig.setMenuDisabled(true);
	    day2ColumnConfig.setSortable(false);
	    day2ColumnConfig.setEditor(new CellEditor(new NumberField()));
	    day2ColumnConfig.setRenderer(new GridCellRenderer<BaseModelData>() {
			@Override public Object render(BaseModelData model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<BaseModelData> store, Grid<BaseModelData> grid) {
				config.css = ""; 
				if(!model.get("day_2").equals(0)){
					if(model.get("status_2").equals(true))
						config.css = "x-treegrid-column-holiday"; 
					return model.get("day_2");
				}
				return "<span> </span>";
			}
	    });
	    columns.add(day2ColumnConfig);
	    
	    ColumnConfig day3ColumnConfig = new ColumnConfig();
	    day3ColumnConfig.setDataIndex("day_3");
	    day3ColumnConfig.setId("day3ColumnConfigId");
	    day3ColumnConfig.setHeaderHtml("3");
	    day3ColumnConfig.setWidth(25);
	    day3ColumnConfig.setMenuDisabled(true);
	    day3ColumnConfig.setSortable(false);
	    day3ColumnConfig.setEditor(new CellEditor(new NumberField())); 
	    day3ColumnConfig.setRenderer(new GridCellRenderer<BaseModelData>() {
			@Override public Object render(BaseModelData model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<BaseModelData> store, Grid<BaseModelData> grid) {
				config.css = ""; 
				if(!model.get("day_3").equals(0)){
					if(model.get("status_3").equals(true))
						config.css = "x-treegrid-column-holiday"; 
					return model.get("day_3");
				}
				return "<span> </span>";
			}
	    });
	    columns.add(day3ColumnConfig);
	    
	    ColumnConfig day4ColumnConfig = new ColumnConfig();
	    day4ColumnConfig.setDataIndex("day_4");
	    day4ColumnConfig.setId("day4ColumnConfigId");
	    day4ColumnConfig.setHeaderHtml("4");
	    day4ColumnConfig.setWidth(25);
	    day4ColumnConfig.setMenuDisabled(true);
	    day4ColumnConfig.setSortable(false);
	    day4ColumnConfig.setEditor(new CellEditor(new NumberField()));
	    day4ColumnConfig.setRenderer(new GridCellRenderer<BaseModelData>() {
			@Override public Object render(BaseModelData model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<BaseModelData> store, Grid<BaseModelData> grid) {
				config.css = ""; 
				if(!model.get("day_4").equals(0)){
					if(model.get("status_4").equals(true))
						config.css = "x-treegrid-column-holiday"; 
					return model.get("day_4");
				}
				return "<span> </span>";
			}
	    });
	    columns.add(day4ColumnConfig);
	    
	    ColumnConfig day5ColumnConfig = new ColumnConfig();
	    day5ColumnConfig.setDataIndex("day_5");
	    day5ColumnConfig.setHeaderHtml("5");
	    day5ColumnConfig.setId("day5ColumnConfigId");
	    day5ColumnConfig.setWidth(25);
	    day5ColumnConfig.setMenuDisabled(true);
	    day5ColumnConfig.setSortable(false);
	    day5ColumnConfig.setEditor(new CellEditor(new NumberField())); 
	    day5ColumnConfig.setRenderer(new GridCellRenderer<BaseModelData>() {
			@Override public Object render(BaseModelData model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<BaseModelData> store, Grid<BaseModelData> grid) {
				config.css = ""; 
				if(!model.get("day_5").equals(0)){
					if(model.get("status_5").equals(true))
						config.css = "x-treegrid-column-holiday"; 
					return model.get("day_5");
				}
				return "<span> </span>";
			}
	    });
	    columns.add(day5ColumnConfig);
	    
	    ColumnConfig day6ColumnConfig = new ColumnConfig();
	    day6ColumnConfig.setDataIndex("day_6");
	    day6ColumnConfig.setHeaderHtml("6");
	    day6ColumnConfig.setId("day6ColumnConfigId");
	    day6ColumnConfig.setWidth(25);
	    day6ColumnConfig.setMenuDisabled(true);
	    day6ColumnConfig.setSortable(false);
	    day6ColumnConfig.setEditor(new CellEditor(new NumberField()));
	    day6ColumnConfig.setRenderer(new GridCellRenderer<BaseModelData>() {
			@Override public Object render(BaseModelData model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<BaseModelData> store, Grid<BaseModelData> grid) {
				config.css = ""; 
				if(!model.get("day_6").equals(0)){
					if(model.get("status_6").equals(true))
						config.css = "x-treegrid-column-holiday"; 
					return model.get("day_6");
				}
				return "<span> </span>";
			}
	    });
	    columns.add(day6ColumnConfig);
	    
	    ColumnConfig day7ColumnConfig = new ColumnConfig();
	    day7ColumnConfig.setDataIndex("day_7");
	    day7ColumnConfig.setHeaderHtml("7");
	    day7ColumnConfig.setId("day7ColumnConfigId");
	    day7ColumnConfig.setWidth(25);
	    day7ColumnConfig.setMenuDisabled(true);
	    day7ColumnConfig.setSortable(false);
	    day7ColumnConfig.setEditor(new CellEditor(new NumberField()));
	    day7ColumnConfig.setRenderer(new GridCellRenderer<BaseModelData>() {
			@Override public Object render(BaseModelData model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<BaseModelData> store, Grid<BaseModelData> grid) {
				config.css = ""; 
				if(!model.get("day_7").equals(0)){
					if(model.get("status_7").equals(true))
						config.css = "x-treegrid-column-holiday"; 
					return model.get("day_7");
				}
				return "<span> </span>";
			}
	    });
	    columns.add(day7ColumnConfig);


	    
	    ColumnModel cm = new ColumnModel(columns);
		
	       grid = new EditorGrid<BaseModelData>(store, cm);
		   grid.setLoadMask(true);
		   grid.setWidth(180);
		   grid.setHeight(160);
//		   grid.setAutoExpandColumn("day1ColumnConfigId");
		   grid.setBorders(true);
		   grid.setColumnLines(true);
		   grid.addStyleName(".my-table-style");
		   grid.setBorders(true);
		   grid.setClicksToEdit(ClicksToEdit.ONE);
		  
		   
		   grid.getView().setEmptyText("no data");
		   grid.setId("myHolidayId");
		   grid.addListener(Events.BeforeEdit,new Listener<GridEvent<BaseModelData>>(){
			@Override	public void handleEvent(GridEvent<BaseModelData> be) {
				be.setCancelled(true);
				
				if(!be.getValue().equals(0)){
					Boolean status;
					Date day = new Date();
					day.setYear(date.getYear());
					day.setMonth(date.getMonth());
					day.setDate((Integer) be.getValue());
					
					Integer column = (Integer) be.getColIndex();
					column++;
					
					if(be.getRecord().get("status_"+column).equals(true)){
						status = false;
					}else{
						status = true;
					}
					
					holidayService.setAndUpdateHolidays(day, status, new AsyncCallback<List<BaseModelData>>(){
						@Override					public void onFailure(Throwable caught) {	}
						@Override 					public void onSuccess(List<BaseModelData> result) {
							grid.getStore().getLoader().load();
							
						}
					});
					
				}
			}
		});
		   

		 
		   
	
		 add(grid);
	show();	
		
	}
	
	private void loadHoliday(){
		
		
		
		
		holidayService.getHolidays(date, new AsyncCallback<List<BaseModelData>>(){
			@Override public void onFailure(Throwable caught) {	}
			@Override public void onSuccess(List<BaseModelData> result) {
				grid.getStore().getLoader().load();
			}
		});
		
	}
	
	
	

}
