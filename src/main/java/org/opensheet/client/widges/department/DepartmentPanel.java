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
import java.util.Arrays;
import java.util.List;

import org.omg.CORBA.INTERNAL;
import org.opensheet.client.dto.DepartmentDTO;
import org.opensheet.client.dto.grid.AssignmentGridTemplate;
import org.opensheet.client.services.AssignmentService;
import org.opensheet.client.services.AssignmentServiceAsync;
import org.opensheet.client.services.DepartmentService;
import org.opensheet.client.services.DepartmentServiceAsync;
import org.opensheet.client.services.HourService;
import org.opensheet.client.services.HourServiceAsync;
import org.opensheet.client.services.StatService;
import org.opensheet.client.services.StatServiceAsync;
import org.opensheet.client.services.TimelineService;
import org.opensheet.client.services.TimelineServiceAsync;
import org.opensheet.client.services.UserService;
import org.opensheet.client.services.UserServiceAsync;
import org.opensheet.client.utils.Resources;
import org.opensheet.client.widges.Reloadable;

import com.extjs.gxt.ui.client.Style.HideMode;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;

public class DepartmentPanel extends ContentPanel implements Reloadable{
	
	private ContentPanel assignemntsTreePanel;
	private UserDepartmentReportContentPanel userDepartmentReportContentPanel;
	private ComboBox<BeanModel> departmentComboBox;
	private TreeGrid<ModelData> assignmentTreeGrid;
	private TreeStore<ModelData> assignmentStore;

	private Grid<BaseModel> userAssignmentGrid;
	private DepartmentServiceAsync  departmentService  = GWT.create(DepartmentService.class);
	private StatServiceAsync  		statService  	   = GWT.create(StatService.class);
	private TimelineServiceAsync  	timelineService    = GWT.create(TimelineService.class);
	private UserServiceAsync  	    userService  	   = GWT.create(UserService.class);

	
	private AssignmentDetailPanel assignmentDetailPanel;
	private NumberField userHourSumField,userHourSumThisMonthField,timelineField, timelineOverTimeField,externalRateField,internalRateField;
	private FormPanel userAssignmentDetailsContentPanel;
	private DepartmentReportContentPanel departmentReportContentPanel;
	
	private Integer departmentId;
	private Integer assignmentId;
	private Integer userId;
	
	public DepartmentPanel(){
		this.setHeaderVisible(false);
		setLayout(new RowLayout(Orientation.HORIZONTAL));
		setFrame(true);

		add(doLeftPanel());
		add(doRightPanel());
		
	}
	
	private ContentPanel doLeftPanel(){
		assignemntsTreePanel = new ContentPanel();
		assignemntsTreePanel.setFrame(true);
		assignemntsTreePanel.setWidth(300);
		assignemntsTreePanel.setHeight(700);
		
		assignemntsTreePanel.setHeadingHtml("Assignments:");
		ToolBar toolBar = new ToolBar();
		toolBar.add(new LabelToolItem("Choose Department: "));
		toolBar.add(doDepartmentComboBox());
		assignemntsTreePanel.add(toolBar);
		
		assignemntsTreePanel.add(doAssignmentTreeGrid());
		return assignemntsTreePanel;
	}
	
	
	
	private TabPanel doRightPanel(){
		TabPanel folder = new TabPanel(); 
		
		TabItem assignmentTab = new TabItem("Assignment Detail"); 
		assignmentTab.setWidth(724);
		assignmentTab.setHeight(600);
		assignmentDetailPanel = new  AssignmentDetailPanel();
		assignmentTab.add(assignmentDetailPanel);
		folder.add(assignmentTab);
		
		TabItem usersTreePanelTab = new TabItem("Users Statistic");
		usersTreePanelTab.setHideMode(HideMode.OFFSETS);
		usersTreePanelTab.setWidth(724);
		usersTreePanelTab.setHeight(600);
		
		
		userDepartmentReportContentPanel = new UserDepartmentReportContentPanel();
		
		usersTreePanelTab.add(userDepartmentReportContentPanel);
		folder.add(usersTreePanelTab);
		
		
		TabItem usersAssignmentTab = new TabItem("Users Assignments"); 
		usersAssignmentTab.setHideMode(HideMode.OFFSETS);
		usersAssignmentTab.add(doUserAssignmentContentPanel());
		folder.add(usersAssignmentTab );
		
		TabItem reportTab = new TabItem("Department Report"); 
		reportTab.setHideMode(HideMode.OFFSETS);
		reportTab.setWidth(724);
		reportTab.setHeight(600);
		departmentReportContentPanel = new  DepartmentReportContentPanel();
		reportTab.add(departmentReportContentPanel);
		folder.add(reportTab);
		
		return folder;
	}
	
	
	private ComboBox<BeanModel> doDepartmentComboBox(){
		 ListStore<BeanModel>  departmentStore;
		 final DepartmentServiceAsync departmentService = GWT.create(DepartmentService.class);
		 ListLoader<?> loader;
		 
		 RpcProxy<List<DepartmentDTO>> DepartmentProxy = new RpcProxy<List<DepartmentDTO>>() {
             @Override
             protected void load(Object loadConfig, AsyncCallback<List<DepartmentDTO>> callback) {
             	departmentService.getDepartments(callback);
             	
             }
         }; 
  	   
        loader = new BaseListLoader<ListLoadResult<ModelData>>(DepartmentProxy,new BeanModelReader());
	    departmentStore = new ListStore<BeanModel>(loader);
	    departmentStore.setMonitorChanges(true);
	    loader.load();
		
		
		departmentComboBox = new ComboBox<BeanModel>();
        departmentComboBox.setDisplayField("name");  
        departmentComboBox.setWidth(150);
        departmentComboBox.setName("department");
        departmentComboBox.setFieldLabel("Department");
        departmentComboBox.setAllowBlank(false);
        departmentComboBox.setEditable(false);
        departmentComboBox.setValue(departmentStore.getAt(0));
        departmentComboBox.setStore(departmentStore);  
        departmentComboBox.setTypeAhead(true);  
        departmentComboBox.setTriggerAction(TriggerAction.ALL); 
        departmentComboBox.addListener(Events.Select,new Listener<BaseEvent>(){
			@Override	public void handleEvent(BaseEvent be) {
				departmentId = 	Integer.parseInt(departmentComboBox.getValue().get("id").toString());	
				loadAssignmentTreeGrid();
				departmentReportContentPanel.setDepartment(departmentId);
			}
        });
        
        
        return departmentComboBox;
	}
	
	private TreeGrid<ModelData> doAssignmentTreeGrid(){
		assignmentStore = new TreeStore<ModelData>();
		
		ColumnConfig name = new ColumnConfig("name", "Name", 100);
	    name.setRenderer(new TreeGridCellRenderer<ModelData>());  
	    ColumnConfig index = new ColumnConfig("assignmentIndex", "Index", 100);
	    index.setSortable(false);
	    index.setFixed(true);
	    index.setMenuDisabled(true);
	    ColumnConfig status = new ColumnConfig("status", "Status", 100);  
	    ColumnModel cm = new ColumnModel(Arrays.asList(name, index, status)); 
		
		
		
		
		assignmentTreeGrid = new TreeGrid<ModelData>(assignmentStore, cm);  
		assignmentTreeGrid.setBorders(true);  
		assignmentTreeGrid.setAutoExpandColumn("name");  
		assignmentTreeGrid.setTrackMouseOver(false);
		assignmentTreeGrid.setLoadMask(true);
		assignmentTreeGrid.setWidth(600);
		assignmentTreeGrid.setHeight(580);
		assignmentTreeGrid.setBorders(true);
		assignmentTreeGrid.getView().setEmptyText("no data");
		assignmentTreeGrid.setId("myAssignmentTreeGridid");
		assignmentTreeGrid.getSelectionModel().addListener(Events.SelectionChange,  
			            new Listener<SelectionChangedEvent<ModelData>>() {  
		            public void handleEvent(SelectionChangedEvent<ModelData> be) { 
		              if (be.getSelection().size() > 0) {  
		            	  assignmentId	= Integer.parseInt(assignmentTreeGrid.getSelectionModel().getSelectedItem().get("index").toString());
		            	  userDepartmentReportContentPanel.loadData(assignmentId, departmentId);
		            	  loadUserAssignmentCheckGrid();
		            	  assignmentDetailPanel.setData(assignmentId, departmentId);
		              } else {  
		            	//panel.clear();  
		              }
		            }
		          }); 
		
		Menu menu = new Menu();
		MenuItem addUserToAssignmentMenuItem = new MenuItem();
		addUserToAssignmentMenuItem.setText("Add/Remove User to Assignment");
		addUserToAssignmentMenuItem.setIcon(Resources.ICONS.add16());
		addUserToAssignmentMenuItem.addSelectionListener(new SelectionListener<MenuEvent>(){
			@Override		public void componentSelected(MenuEvent ce) {
					addUserToAssignmentWindow();
					
			}
			
		});
		menu.add(addUserToAssignmentMenuItem);
		assignmentTreeGrid.setContextMenu(menu);
		
		return assignmentTreeGrid;
	}
	
	private void addUserToAssignmentWindow(){
		final DepartmentPanel panel = this;
		GWT.runAsync(new RunAsyncCallback(){
			@Override
			public void onFailure(Throwable reason) {
				MessageBox.info("ERROR, can't draw addUserToAssignmentWindow",reason.getMessage(),null);					
			}
			@Override
			public void onSuccess() {
				new AddUserToAssignmentWindow(assignmentId,departmentId,panel);
			}
		});
		
	}
	
	
	public  void loadAssignmentTreeGrid(){
		 final AssignmentServiceAsync assignmentService = GWT.create(AssignmentService.class);
		 assignmentService.getDepartmentManagerAssignments(new DepartmentDTO(departmentId), new AsyncCallback<AssignmentGridTemplate>() {
					public void onFailure(Throwable caught) {
	                    MessageBox.alert("Cant get Assignemnts",caught.getMessage(), null);

						
					}
					public void onSuccess(AssignmentGridTemplate model) {
						assignmentTreeGrid.getStore().removeAll();
						assignmentStore.add(model.getChildren(), true);
					}
				});
		}
	
	
	
	private ContentPanel doUserAssignmentContentPanel(){
		
		 ContentPanel userAssignmentContentPanel  = new ContentPanel();
		 userAssignmentContentPanel.setFrame(true);
		 userAssignmentContentPanel.setWidth(724);
		 userAssignmentContentPanel.setHeight(600);
		 userAssignmentContentPanel.setLayout(new RowLayout());
		 userAssignmentContentPanel.setHeadingHtml("Users in assignment: ");
		 userAssignmentContentPanel.add(doUserAssignmentCheckGrid());
		 userAssignmentContentPanel.add(doUserAssignmentDetailsContentPanel());

		return userAssignmentContentPanel;
	}
	
	
		private void loadUserAssignmentCheckGrid(){
			userAssignmentGrid.getStore().removeAll();
			statService.getUsersByDepartmentAndByAssignmentWithStats(departmentId,assignmentId,new AsyncCallback<List<BaseModel>>(){
				@Override	public void onFailure(Throwable caught) {
			 		Info.display("Error", caught.toString());
				}
				@Override
				public void onSuccess(List<BaseModel> result) {
					userAssignmentGrid.getStore().add(result);
				}	
			});
	}
	
		
		
		
		
		
	private Grid<BaseModel> doUserAssignmentCheckGrid(){
		 List<ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		 ColumnConfig userConfig = new ColumnConfig("name", "User", 250);
		 userConfig.setFixed(true);
		 userConfig.setSortable(false);
		 userConfig.setMenuDisabled(true);
	     configs.add(userConfig);
	     
	     ColumnConfig sumConfig = new ColumnConfig("sum", "∑", 50);
	     sumConfig.setFixed(true);
		 sumConfig.setSortable(false);
		 sumConfig.setMenuDisabled(true);
	     configs.add(sumConfig);
	     
	     ColumnConfig timelineConfig = new ColumnConfig("timeline", "Timeline", 50);
	     timelineConfig.setFixed(true);
	     timelineConfig.setSortable(false);
	     timelineConfig.setMenuDisabled(true);
	     configs.add(timelineConfig);
	     
	     ColumnConfig overtimelineConfig = new ColumnConfig("overtimeline", "Over Timeline", 70);
	     overtimelineConfig.setFixed(true);
	     overtimelineConfig.setSortable(false);
	     overtimelineConfig.setMenuDisabled(true);
	     configs.add(overtimelineConfig);
	     
	     ColumnConfig internalrateConfig = new ColumnConfig("internalrate", "in cost", 70);
	     internalrateConfig.setFixed(true);
	     internalrateConfig.setSortable(false);
	     internalrateConfig.setMenuDisabled(true);
	     configs.add(internalrateConfig);
	     
	     ColumnConfig internalrateSumConfig = new ColumnConfig("internalrateSum", "in cost ∑ ", 70);
	     internalrateSumConfig.setFixed(true);
	     internalrateSumConfig.setSortable(false);
	     internalrateSumConfig.setMenuDisabled(true);
	     configs.add(internalrateSumConfig);	
	     
	     ColumnConfig externalRateConfig = new ColumnConfig("externalrate", "ext cost", 70);
	     externalRateConfig.setFixed(true);
	     externalRateConfig.setSortable(false);
	     externalRateConfig.setMenuDisabled(true);
	     configs.add(externalRateConfig);
	     
	     ColumnConfig externalRateSumConfig = new ColumnConfig("externalRateSum", "ext cost ∑", 70);
	  //   externalRateSumConfig.setFixed(true);
	     externalRateSumConfig.setSortable(false);
	     externalRateSumConfig.setMenuDisabled(true);
	     configs.add(externalRateSumConfig);	
	     
		
			
		 ColumnModel cm = new ColumnModel(configs);

		 userAssignmentGrid = new Grid<BaseModel>(new ListStore<BaseModel>(), cm); 
		 userAssignmentGrid.setLoadMask(true);
		 userAssignmentGrid.setWidth(700);
		 userAssignmentGrid.setHeight(350);
		 userAssignmentGrid.addStyleName(".my-table-style");
		 userAssignmentGrid.setBorders(true);
		 userAssignmentGrid.setAutoExpandColumn("name");
		 userAssignmentGrid.getView().setEmptyText("No users assigned");
		 userAssignmentGrid.setId("userAssignmentCheckGridId");
		 userAssignmentGrid.getSelectionModel().addListener(Events.SelectionChange,  
		            new Listener<SelectionChangedEvent<ModelData>>() {  
			            public void handleEvent(SelectionChangedEvent<ModelData> be) { 
			              if (be.getSelection().size() > 0) {  
			            	  userId	= Integer.parseInt(userAssignmentGrid.getSelectionModel().getSelectedItem().get("id").toString());
			            	  loadUserAssignmentDetail();
			              } else {  
			            	  userAssignmentDetailsContentPanel.clear();
			              }
			            }
	          }); 
	return userAssignmentGrid;
	}
	
	
	private FormPanel doUserAssignmentDetailsContentPanel(){
		userAssignmentDetailsContentPanel = new FormPanel();
		userAssignmentDetailsContentPanel.setHeaderVisible(false);
		userAssignmentDetailsContentPanel.setWidth(724);   //288
		userAssignmentDetailsContentPanel.setHeight(250);
		
		
		FlexTable table = new FlexTable();  
	    table.getElement().getStyle().setProperty("margin", "10px");  
	    table.setCellSpacing(8);  
	    table.setCellPadding(4);  
	
	    Label label = new Label("Timeline");
	    table.setWidget(0,0, label);
	    
	    timelineField = new NumberField();
	    timelineField.setWidth(60);
		    
	    table.setWidget(0, 1,timelineField);  
	    Button saveTimelineRateBtn = new Button("Save");
	    saveTimelineRateBtn.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override	public void componentSelected(ButtonEvent ce) {
				saveTimeline();
			}
		});
	    
	    
	    
	    
	    table.setWidget(0,2, saveTimelineRateBtn);
	    
	    
	    label = new Label("Internal Cost");
	    table.setWidget(1,0, label);
	    
	    internalRateField = new NumberField();
	    internalRateField.setWidth(60);
	    
	    table.setWidget(1, 1,internalRateField); 
	    
	    Button saveInternalRateBtn = new Button("Save");
	    saveInternalRateBtn.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override	public void componentSelected(ButtonEvent ce) {
				saveInternal();
			}
		});
	    
	    table.setWidget(1,2, saveInternalRateBtn);
	    
	    label = new Label("External Cost");
	    table.setWidget(2,0, label);
	    
	    externalRateField = new NumberField();
	    externalRateField.setWidth(60);
	    
	    table.setWidget(2, 1,externalRateField); 
	    
	    Button saveExternalRateBtn = new Button("Save");
	    saveExternalRateBtn.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override	public void componentSelected(ButtonEvent ce) {
				saveExternal();
			}
		});
	    table.setWidget(2,2, saveExternalRateBtn);
	    userAssignmentDetailsContentPanel.add(table);
		return userAssignmentDetailsContentPanel;
	}

	private void loadUserAssignmentDetail(){
		userAssignmentDetailsContentPanel.clear();
		statService.getAssignmentDepartmentUserDetail(assignmentId, departmentId, userId, new AsyncCallback<BaseModel>(){
			@Override	public void onFailure(Throwable caught) {
					MessageBox.info("Error", caught.getMessage(), null);				
			}
			@Override
			public void onSuccess(BaseModel result) {
				timelineField.setValue(Integer.parseInt(result.get("timeline").toString()));
				internalRateField.setValue(Integer.parseInt(result.get("intra_rate").toString()));
				externalRateField.setValue(Integer.parseInt(result.get("extra_rate").toString()));
			}
		});
	}
	
	
	@Override
	public void reload() {
		loadUserAssignmentCheckGrid();
	}

	
	
	private void saveTimeline(){
		if(assignmentId != null && userId != null){
			Integer timeline = (Integer) timelineField.getValue().intValue();
			timelineService.setUserAssignmentTimeline(userId, assignmentId, timeline, new AsyncCallback<Void>(){
			@Override	public void onFailure(Throwable caught) {
				MessageBox.info("ERROR",caught.getMessage(),null);					
			}
			@Override	public void onSuccess(Void result) {
				Info.display("OK","Timeline updated");		
				loadUserAssignmentCheckGrid();
			}
		});
		
		}
		
	}
	
	private void saveInternal(){
		Integer internal = (Integer) internalRateField.getValue().intValue();
		userService.setUserInternalRate(userId, internal, new AsyncCallback<Void>(){
			@Override	public void onFailure(Throwable caught) {
				MessageBox.info("ERROR",caught.getMessage(),null);					
			}
			@Override	public void onSuccess(Void result) {
				Info.display("OK","Internal cost updated");		
				loadUserAssignmentCheckGrid();
			}
		});
		
	}
	
	private void saveExternal(){
		if(assignmentId != null && userId != null){
		Integer external = (Integer) externalRateField.getValue().intValue();
		timelineService.setUserAssignmentRate(userId, assignmentId, external,new AsyncCallback<Void>(){
			@Override	public void onFailure(Throwable caught) {
				MessageBox.info("ERROR",caught.getMessage(),null);					
			}
			@Override	public void onSuccess(Void result) {
				Info.display("OK","External cost updated");	
				loadUserAssignmentCheckGrid();
			}
		});
		
		}
	}
}


