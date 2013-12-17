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
package org.opensheet.client.widges.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opensheet.client.l10n.OpensheetConstants;
import org.opensheet.client.services.StatService;
import org.opensheet.client.services.StatServiceAsync;
import org.opensheet.client.utils.AssignmentTypes;
import org.opensheet.client.utils.Resources;
import org.opensheet.client.widges.BranchComboBox;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class HourUserReport extends ContentPanel{
	private FormPanel leftSidePanel;
	private BranchComboBox branchComboBox;
	private Integer branch = 9999999;
	private Integer type =   9999999;
	private Integer assignment = 9999999;
	private DateField start,end;
	private ListStore<BaseModel> store;
	private ComboBox<AssignmentTypes> typeSimpleComboBox;
	private Grid<BaseModel> grid;
	private StatServiceAsync statService = GWT.create(StatService.class);
	private OpensheetConstants myConstants = (OpensheetConstants) GWT.create(OpensheetConstants.class);
	private AssignmentComboBox assignmentComboBox;
	
	public HourUserReport(){
		
		setHeaderVisible(false);
		setFrame(true);
		setLayout(new ColumnLayout());
		add(doSideMenu());
		add(doReportPanel());
		loadAssignmentComboBox();
	}
	
	
	
	
	@SuppressWarnings("deprecation")
	private FormPanel doSideMenu(){
		leftSidePanel = new FormPanel();
		leftSidePanel.setWidth(340);
		leftSidePanel.setHeight(440);
		leftSidePanel.setHeaderVisible(false);
		leftSidePanel.setFrame(true);
		
		 branchComboBox = new BranchComboBox(true);
		 branchComboBox.setWidth(180);
		 branchComboBox.setValue(branchComboBox.getStore().getAt(0));
		 branchComboBox.addListener(Events.Select, new Listener<BaseEvent>(){
				@Override
				public void handleEvent(BaseEvent be) {
					branch = Integer.parseInt(branchComboBox.getValue().get("id").toString());
					 loadAssignmentComboBox();
					
				}
		    });
		branchComboBox.setAllowBlank(false);
		branchComboBox.setLazyRender(true);
		branchComboBox.setEmptyText("All_");
		branchComboBox.setFieldLabel(myConstants.branch());
		leftSidePanel.add(branchComboBox);
		
		start = new DateField(); 
		start.setFieldLabel("Start");
		Date date = new Date();
		date.setMonth(date.getMonth()-1);
		start.setValue(date);
		leftSidePanel.add(start);
		
		end = new DateField(); 
		end.setFieldLabel("End");
		end.setValue(new Date());
		leftSidePanel.add(end);
		
		
		List<AssignmentTypes> list = AssignmentTypes.getWithAllTypes();
	    final ListStore<AssignmentTypes>  store = new ListStore<AssignmentTypes>();  
        store.add(list);
		
		typeSimpleComboBox = new ComboBox<AssignmentTypes>();  
        typeSimpleComboBox.setTriggerAction(TriggerAction.ALL);  
        typeSimpleComboBox.setEditable(false);  
        typeSimpleComboBox.setWidth(100);
        typeSimpleComboBox.setDisplayField("name");
        typeSimpleComboBox.setName("name");
        typeSimpleComboBox.setFieldLabel("Type");
        typeSimpleComboBox.setStore(store);
        typeSimpleComboBox.setAllowBlank(false);
        typeSimpleComboBox.setValue(new AssignmentTypes(9999999));
        typeSimpleComboBox.addListener(Events.Select,new  Listener<BaseEvent>(){
			@Override				public void handleEvent(BaseEvent be) {
				type = Integer.parseInt(typeSimpleComboBox.getValue().get("id").toString());
				 loadAssignmentComboBox();
			}
        	
        });
        leftSidePanel.add(typeSimpleComboBox);
        
       assignmentComboBox  = new  AssignmentComboBox(); 
       assignmentComboBox.setFieldLabel("Assignments");
       assignmentComboBox.addListener(Events.Select,new Listener<BaseEvent>(){
		@Override 		public void handleEvent(BaseEvent be) {
			 assignment = Integer.parseInt(assignmentComboBox.getValue().get("id").toString());
		}
    	   
       });
       
       
       leftSidePanel.add(assignmentComboBox);
       assignmentComboBox.getStore().getLoader().load();
       assignmentComboBox.setValue(assignmentComboBox.getStore().findModel("id","9999999"));
       
      
       
		
		Button goButton = new Button("Get Report");
		goButton.addListener(Events.Select,new  Listener<BaseEvent>(){
			@Override	public void handleEvent(BaseEvent be) {
				grid.getStore().getLoader().load();
			}
		});
		
		leftSidePanel.addButton(goButton);
		
		return leftSidePanel;
		
	}
	
	private ContentPanel doReportPanel(){
		
		
		ContentPanel rightSidePanel = new ContentPanel();
		rightSidePanel.setWidth(760);
		rightSidePanel.setHeight(600);
		rightSidePanel.setHeaderVisible(false);
		rightSidePanel.setFrame(true);
		rightSidePanel.setTopComponent(addToolBar());
		rightSidePanel.add(doGrid());
		return rightSidePanel;
		
	}
	
	private ToolBar addToolBar(){
		
		ToolBar toolBar = new ToolBar();
		Button printButton = new Button();
	    printButton.setText("Print Me");
	    printButton.setIconStyle("icon-printer");
	    printButton.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override public void handleEvent(BaseEvent be) {
			//	Integer getYear = date.getYear() +1900;
				/*
				String winUrl = GWT.getHostPageBaseURL() + "timesheettoprinter.htm?user_id=" + userDTO.getId() + "&month="+date.getMonth()+"&year="+ getYear;
				String winName = "Print Page";
				openNewWindow(winName, winUrl);  
				*/
			}
	    });	
		
	    toolBar.add(printButton);
	    toolBar.add(new SeparatorToolItem());

	    Button xlsExportButton = new Button();
	    xlsExportButton.setText("Excel Export");
	    xlsExportButton.setIcon(Resources.ICONS.table()); 
	    xlsExportButton.addListener(Events.Select, new Listener<BaseEvent>(){
			@SuppressWarnings("deprecation")
			@Override
			public void handleEvent(BaseEvent be) {
				Window.Location.assign(GWT.getHostPageBaseURL().toString() +"userassignmentreporttoxls.htm?assignment="+assignment+"&branch=" + branch + "&type="+
				type+"&s_year="+start.getValue().getYear()+"&s_month="+start.getValue().getMonth()+
				"&s_day="+start.getValue().getDate()+"&e_year="+end.getValue().getYear()+"&e_month="
				+end.getValue().getMonth()+"&e_day="+end.getValue().getDate());
				
			}
	    });
	    toolBar.add(xlsExportButton);
		return toolBar;
	}
	
	private Grid<BaseModel> doGrid(){
		
		RpcProxy<List<BaseModel>> proxy = new RpcProxy<List<BaseModel>>() {
			@Override
            protected void load(Object loadConfig, AsyncCallback<List<BaseModel>> callback) {
            	Date startDate = start.getValue();
            	Date endDate = end.getValue();
                statService.getHourReportByUserByPeriodByAssignmentByBranch(assignment,branch,type,startDate, endDate,callback);
            }
        };    
        
        ListLoader<?> loader = new BaseListLoader<ListLoadResult<BaseModel>>(proxy);
        store = new ListStore<BaseModel>(loader);
        store.setMonitorChanges(true);
		
	    List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
	    columns.add(new ColumnConfig("username",myConstants.user(), 180));
	    columns.add(new ColumnConfig("assignment", "Assignment", 150));
	    columns.add(new ColumnConfig("hour", "Hour", 50));
	    columns.add(new ColumnConfig("department",myConstants.department(), 100));
	    columns.add(new ColumnConfig("branch",myConstants.branch(), 100));
	    columns.add(new ColumnConfig("inratesum", "inRateSum", 50));
	    columns.add(new ColumnConfig("extratesum", "extRateSum", 50));
	    
	    ColumnModel cm = new ColumnModel(columns);
	    
		grid = new Grid<BaseModel>(store,cm);
		grid.setLoadMask(true);
		grid.setWidth(740);
		grid.setHeight(400);
	    grid.addStyleName(".my-table-style");
	    grid.setBorders(true);
	    grid.setAutoExpandColumn("username");
	    grid.getView().setEmptyText("no data");
	    grid.setId("myid");
        grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);  

		
		return grid;
		
	}
	
	private void loadAssignmentComboBox(){
		if(branch != null && type != null){
			assignmentComboBox.setData(branch, type);
		}
		
		
	}
}
