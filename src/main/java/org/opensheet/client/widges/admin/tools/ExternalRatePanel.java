package org.opensheet.client.widges.admin.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.opensheet.client.dto.UserDTO;
import org.opensheet.client.dto.grid.AssignmentGridTemplate;
import org.opensheet.client.services.AssignmentService;
import org.opensheet.client.services.AssignmentServiceAsync;
import org.opensheet.client.services.HourService;
import org.opensheet.client.services.HourServiceAsync;
import org.opensheet.client.services.TimelineService;
import org.opensheet.client.services.TimelineServiceAsync;
import org.opensheet.client.services.UserService;
import org.opensheet.client.services.UserServiceAsync;
import org.opensheet.client.utils.Resources;
import org.opensheet.client.widges.BranchComboBox;

import com.extjs.gxt.ui.client.Style.HideMode;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class ExternalRatePanel extends ContentPanel{
	 private Grid<BeanModel> grid;
	 private UserServiceAsync userService = GWT.create(UserService.class);
	 private TimelineServiceAsync timelineService = GWT.create(TimelineService.class);

	 private ListLoader<?> loader;
	 private String usersRpcCriteria = "1";
	 private Integer selectedBranch = 1;
	 private BranchComboBox toolBarBranchComboBox;
	 private NumberField internalRate,externalRate;
	 private Integer userId;
	 private DateField start,end;
	 private TreeGrid<ModelData> tree;
	 private TreeStore<ModelData>  store;
	
	
	public ExternalRatePanel(){
		setHideMode(HideMode.OFFSETS);
		this.setWidth(1024);
		this.setHeight(600);
		this.setLayout(new ColumnLayout());
		this.add(doUserGrid());
		this.add(doRatePanel());
		grid.getStore().getLoader().load();
		
	
	}
	
	
	private ContentPanel doUserGrid(){
		ContentPanel cp = new ContentPanel();
		cp.setHeaderVisible(false);
		cp.setHeight(600);
		cp.setWidth(380);
		cp.setFrame(true);
		
		cp.setTopComponent(doToolBar());
		cp.add(doGrid());
		
		return cp;
		
	}
	
	private Grid<BeanModel> doGrid(){
		RpcProxy<List<UserDTO>> UserProxy = new RpcProxy<List<UserDTO>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<List<UserDTO>> callback) {
                 userService.getUsersByBranch(usersRpcCriteria,selectedBranch,callback);
            }
        };    
    loader = new BaseListLoader<ListLoadResult<ModelData>>(UserProxy,new BeanModelReader());
    final ListStore<BeanModel> UserStore = new ListStore<BeanModel>(loader);
    UserStore.setMonitorChanges(true);
    
    List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
    
    columns.add(new ColumnConfig("login", "Login", 80));
    columns.add(new ColumnConfig("fullName", "Name", 150));
    columns.add(new ColumnConfig("status", "Status", 60));
    columns.add(new ColumnConfig("department.name", "Department", 80));
    ColumnModel cm = new ColumnModel(columns);
		
    grid = new Grid<BeanModel>(UserStore, cm);
	grid.setLoadMask(true);
    grid.setWidth(380);
    grid.setHeight(600);
    grid.addStyleName(".my-table-style");
    grid.setBorders(true);
    grid.setAutoExpandColumn("login");
    grid.getView().setEmptyText("no data");
    grid.setId("myGridid");
    grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);  
    grid.getSelectionModel().addListener(Events.SelectionChange,new Listener<SelectionChangedEvent<BeanModel>>(){
		@Override
		public void handleEvent(SelectionChangedEvent<BeanModel> be) {
			 if (be.getSelection().size() > 0) { 
				 	userId  = Integer.parseInt(grid.getSelectionModel().getSelectedItem().get("id").toString());
				 	setExternalRateFieldValue();
			 }
		}
    });
            
		
	return grid;
		
	}
	
	
	private ToolBar doToolBar(){
		
	     ToolBar toolBar = new ToolBar();  
	       toolBar.add(new LabelToolItem("User Mode: "));
	       
	       final SimpleComboBox<String> type = new SimpleComboBox<String>();  
	       type.setTriggerAction(TriggerAction.ALL);  
	       type.setEditable(false);  
	       type.setFireChangeEventOnSetValue(true);  
	       type.setWidth(100);  
	       type.add("Active");  
	       type.add("Killed in Action");
	       type.add("EveryBody");
	       type.setSimpleValue("Active");
	       
	      
	       type.addListener(Events.Change, new Listener<FieldEvent>() {
	    	   		@Override
	    	      public void handleEvent(FieldEvent be) { 
	    	   			if(type.getSimpleValue().equals("EveryBody")){
	    	   				usersRpcCriteria = "any";
	    	   			}else if(type.getSimpleValue().equals("Active")){
	    	   				usersRpcCriteria = "1";
	    	   			}else if(type.getSimpleValue().equals("Killed in Action")){
	    	   				usersRpcCriteria = "0";
	    	   			}
	    	 
	    	   			grid.getStore().getLoader().load();
	    	      }  
	    	    }); 
	       
	       toolBar.add(type);
	       toolBar.add(new SeparatorToolItem()); 
	       toolBar.add(new LabelToolItem("Branch: "));
	       toolBarBranchComboBox =  new BranchComboBox(true);
	       toolBarBranchComboBox.setEmptyText("Default");
	       toolBarBranchComboBox.addListener(Events.Select, new Listener<FieldEvent>(){
			@Override
			public void handleEvent(FieldEvent be) {
				selectedBranch = Integer.parseInt(toolBarBranchComboBox.getValue().get("id").toString());
				grid.getStore().getLoader().load();
			}
	    	   
	       });
	       
	       toolBar.add(toolBarBranchComboBox);
	       return  toolBar;
		
	}

	@SuppressWarnings("deprecation")
	public FormPanel doRatePanel(){
		
		
		FormPanel cp = new FormPanel();
		cp.setHeaderVisible(false);
		cp.setHeight(500);
		cp.setWidth(640);
		cp.setFrame(true);	
		
		
		
		ToolBar toolBar = new ToolBar();
		toolBar.add(new LabelToolItem("From: "));

		Date startDate = new Date();
		startDate.setMonth(startDate.getMonth()-5);
		
		start =new DateField();
		start.setValue(startDate);
		start.setWidth(100);
		toolBar.add(start);
		
		toolBar.add(new LabelToolItem("To: "));
		
		end =new DateField();
		end.setWidth(100);
		end.setValue(new Date());
		toolBar.add(end);
		
		toolBar.add(new LabelToolItem("Internal Rate: "));
		internalRate = new NumberField();
		internalRate.setWidth(60);
		toolBar.add(internalRate);

		toolBar.add(new LabelToolItem("External Rate: "));
		externalRate = new NumberField();
		externalRate.setWidth(60);
		toolBar.add(externalRate);
		
		Button loadAssignmentTreeBtn = new Button("Load");
		loadAssignmentTreeBtn.setIcon(Resources.ICONS.project_icon());
		loadAssignmentTreeBtn.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override
			public void componentSelected(ButtonEvent ce) {
				loadAssignmentTreeGrid();				
			}
		});
		
		
		
		toolBar.add(loadAssignmentTreeBtn);
		cp.setTopComponent(toolBar);
		
		cp.add(doAssignmentTree());

		
		
		
		
		Button saveInternal = new Button("Save Internal Rate");
		saveInternal.setIcon(Resources.ICONS.reload());
		saveInternal.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override
			public void componentSelected(ButtonEvent ce) {
				timelineService.recalculateInternalRate(userId, internalRate.getValue().intValue(), start.getValue(), end.getValue(),new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						MessageBox.info("ERROR",caught.getMessage(),null);						
					}

					@Override
					public void onSuccess(Void result) {
						Info.display("OK","Updated");
						
					}
					
				});
				
			}
			
		});
		cp.addButton(saveInternal);
		
		Button saveExternal = new Button("Save External Rate");
		saveExternal.setIcon(Resources.ICONS.reload());
		saveExternal.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override
			public void componentSelected(ButtonEvent ce) {
				timelineService.recalculateInternalRate(userId, externalRate.getValue().intValue(), start.getValue(), end.getValue(),new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						MessageBox.info("ERROR",caught.getMessage(),null);						
					}

					@Override
					public void onSuccess(Void result) {
						Info.display("OK","Updated");
						
					}
					
				});
				
			}
			
		});
		cp.addButton(saveExternal);
		

	
		
		return cp;
	}
	
	
	private void setExternalRateFieldValue(){
		timelineService.getUserInternalRate(userId, new AsyncCallback<Integer>(){

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.info("ERROR",caught.getMessage(),null);
			}

			@Override
			public void onSuccess(Integer result) {
				internalRate.setValue(result);
			}
			
		});
		
		
		
	}
	
	
	private TreeGrid<ModelData> doAssignmentTree(){
		
		    
		    ColumnConfig name = new ColumnConfig("name", "Name", 100);
		    name.setRenderer(new TreeGridCellRenderer<ModelData>());  
		    ColumnConfig hours = new ColumnConfig("hours", "Hours", 80);
		    ColumnConfig inrate = new ColumnConfig("inrate", "Inrate", 80);
		    ColumnConfig exrate = new ColumnConfig("exrate", "Exrate", 80);
		    ColumnModel cm = new ColumnModel(Arrays.asList(name, hours, inrate,exrate));  
		  
		    store = new TreeStore<ModelData>();
		  
		    tree = new TreeGrid<ModelData>(store, cm);  
		    tree.setBorders(true);  
		    tree.setAutoExpandColumn("name");  
		    tree.setTrackMouseOver(false);
		    tree.setLoadMask(true);
		    tree.setWidth(630);
		    tree.setHeight(400);
		    tree.setBorders(true);
		    tree.getView().setEmptyText("no data");
		    tree.setId("myTreeid");
		    tree.getSelectionModel().addListener(Events.SelectionChange,  
			            new Listener<SelectionChangedEvent<ModelData>>() {  
		            public void handleEvent(SelectionChangedEvent<ModelData> be) { 
		      
		            }
		          });  
		
		
		return tree;
	}

	public  void loadAssignmentTreeGrid(){
		tree.getStore().removeAll();
		
		
		
		final HourServiceAsync hourService = GWT.create(HourService.class);
		hourService.getHoursByUserAndByAssignmentOnPeriod(userId, start.getValue(), end.getValue(), new AsyncCallback<AssignmentGridTemplate>(){

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.info("ERROR", caught.getMessage(), null);
			}
			@Override
			public void onSuccess(AssignmentGridTemplate result) {
				if(result != null){
					store.add(result.getChildren(), true);
				}
			}
		});
		
			
		}
	
}
