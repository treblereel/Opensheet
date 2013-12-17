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
package org.opensheet.client.widges.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.opensheet.client.dto.AuthmethodDTO;
import org.opensheet.client.dto.PermissionDTO;
import org.opensheet.client.dto.UserDTO;
import org.opensheet.client.services.UserService;
import org.opensheet.client.services.UserServiceAsync;

import com.extjs.gxt.ui.client.Style.SortDir;  
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PermissionFormGrid extends ContentPanel{
	
	private FormPanel panel;
	private ListStore<BeanModel> userPermissionStore;
	private PagingLoader<PagingLoadResult<ModelData>> loader;
	private UserServiceAsync userPermissionService = GWT.create(UserService.class);
	private FormBinding formBindings;
	private NumberField id;
	private CheckBox admin,pm,dm,fd;
	private Grid<BeanModel> UserPermissionGrid;
	
	@SuppressWarnings("rawtypes")
	public PermissionFormGrid(){
		setFrame(true);  
	    setSize(1024, 600);  
	    setLayout(new RowLayout(Orientation.HORIZONTAL));
	
		RpcProxy<PagingLoadResult<BaseModel>> proxy = new RpcProxy<PagingLoadResult<BaseModel>>() {  
		@Override  
	    public void load(Object loadConfig, AsyncCallback<PagingLoadResult<BaseModel>> callback) {
		        userPermissionService.getUsersPermissions((PagingLoadConfig)loadConfig, callback);
		      }  
	    };  
		  
		loader = new BasePagingLoader<PagingLoadResult<ModelData>>(proxy);  
		loader.setRemoteSort(true); 
		
		ListStore<BaseModel> store = new ListStore<BaseModel>(loader); 
		PagingToolBar toolBar = new PagingToolBar(20);  
	    toolBar.bind(loader);   

	    
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
	    columns.add(new ColumnConfig("fullName", "Login", 170));
	 
	
	    ColumnConfig adminColumnConfig = new ColumnConfig("admin", "Administrator", 100);
	    adminColumnConfig.setRenderer(getGridCellRenderer());
	    adminColumnConfig.setSortable(false);
		adminColumnConfig.setMenuDisabled(true);
		columns.add(adminColumnConfig);
		    
	    ColumnConfig pmColumnConfig = new ColumnConfig("pm", "Project Manager", 100);
	    pmColumnConfig.setSortable(false);
	    pmColumnConfig.setMenuDisabled(true);
	    pmColumnConfig.setRenderer(getGridCellRenderer());
	    columns.add(pmColumnConfig);
		    
	    ColumnConfig dmColumnConfig = new ColumnConfig("dm", "Department Manager", 100);
	    dmColumnConfig.setSortable(false);
	    dmColumnConfig.setMenuDisabled(true);
	    dmColumnConfig.setRenderer(getGridCellRenderer());
	    columns.add(dmColumnConfig);
		    
	    ColumnConfig fmColumnConfig =new ColumnConfig("fd", "Financinal Department", 100);
	    fmColumnConfig.setSortable(false);
	    fmColumnConfig.setSortable(false);
	    fmColumnConfig.setRenderer(getGridCellRenderer());
	    columns.add(fmColumnConfig);
  
	    ColumnModel cm = new ColumnModel(columns);
	    
	    
	    
	    final Grid<BaseModel> grid = new Grid<BaseModel>(store, cm);
	    grid.setStateId("PermissionFormGrid");
	    grid.setStateful(true);  
	    grid.addListener(Events.Attach, new Listener<GridEvent<BeanModel>>() {
	    public void handleEvent(GridEvent<BeanModel> be) {
	    	
	    PagingLoadConfig config = new BasePagingLoadConfig();
	    config.setOffset(0);
	    config.setLimit(20);

	    Map<String, Object> state = grid.getState();
		    if (state.containsKey("offset")) {
		    	int offset = (Integer) state.get("offset");
		    	int limit = (Integer) state.get("limit");
		    	config.setOffset(offset);
		    	config.setLimit(limit);
		    }
		    if (state.containsKey("sortField")) {
		    	config.setSortField((String) state.get("sortField"));
		    	config.setSortDir(SortDir.valueOf((String) state.get("sortDir")));
		    }
	    loader.load(config);
	    }
	    });
	    grid.setLoadMask(true);
	    grid.setBorders(true);
	    grid.setAutoExpandColumn("fullName");
	    grid.setStyleAttribute("borderTop", "none");
	    grid.setStripeRows(true);
	    grid.setColumnLines(true);
	    grid.setWidth(600); 
	    grid.setHeight(300);
        grid.getSelectionModel().addListener(Events.SelectionChange,  
	               new Listener<SelectionChangedEvent<BeanModel>>() {  
	                 public void handleEvent(SelectionChangedEvent<BeanModel> be) { 
		                   if (be.getSelection().size() > 0) {  
		                     formBindings.bind((ModelData) be.getSelection().get(0)); 
		                   } else {  
		                     formBindings.unbind();  
		                   }  
		                 }  
		               });  
		          
           panel = createForm();
           panel.setWidth(300);
           formBindings = new FormBinding(panel, true);  
           formBindings.setStore((Store) grid.getStore());  
		   
           ContentPanel cp = new ContentPanel();
		   cp.add(grid);
		   cp.setHeaderVisible(false);
		   cp.setBottomComponent(toolBar); 
		   add(cp);
		   add(panel);
	}
	
		   private FormPanel createForm() {
			   panel = new FormPanel();
			   panel.setFrame(true);
	           panel.setHeaderVisible(false);  
	         
	           id = new NumberField();
	           id.setName("id");
	           id.hide();
	           panel.add(id);
	           
	           admin = new CheckBox();
	           admin.setName("admin");
	           admin.setFieldLabel("Administrator");
	           panel.add(admin);
		        	   
	           pm = new CheckBox();
	           pm.setName("pm");
	           pm.setFieldLabel("Project Manager");
	           panel.add(pm); 
	           
	           dm = new CheckBox();
	           dm.setName("dm");
	           dm.setFieldLabel("Department Manager");
	           panel.add(dm);
	           
	           fd = new CheckBox();
	           fd.setName("fd");
	           fd.setFieldLabel("Financial Manager");
	           panel.add(fd);
	           
	           Button button = new Button();
	           button.setText("Send");
	           panel.add(button);

	           button.addListener(Events.OnClick, new Listener<BaseEvent>() {
	               @Override
	               public void handleEvent(BaseEvent be) {
	               	updatePermission();
	               	
	               }
	           });
		        	   
	           return panel;
	}
	
	public void updatePermission(){
		
		UserDTO u = new UserDTO();
		PermissionDTO  p =new PermissionDTO();
		u.setId((Integer) id.getValue().intValue());
		
		
		 if(admin.getValue() != true){
			 p.setAdmin(false);
		 }else{	
			 p.setAdmin(true); 
		 }
		 
		 if(pm.getValue() != true){
			 p.setPm(false);
		 }else{	
			 p.setPm(true); 
		 }
		 if(dm.getValue() != true){
			 p.setDm(false);
		 }else{	
			 p.setDm(true); 
		 }
		 if(fd.getValue() != true){
			 p.setFd(false);
		 }else{	
			 p.setFd(true); 
		 }
		
		u.setPermission(p);
		u.setAuthmethod(new AuthmethodDTO());


		userPermissionService.updateUserPermission(u, new AsyncCallback<Void>() {
	           @Override
	           public void onFailure(Throwable caught) {
	               MessageBox.alert("Cannot update permission", caught.getMessage(), null);
	           }
	           @Override
	           public void onSuccess(Void result) {
	               loader.load();
	           }
	       });
		
	}
	

	private GridCellRenderer<ModelData> getGridCellRenderer(){
		
		GridCellRenderer<ModelData> r = new GridCellRenderer<ModelData>(){
			@Override	public Object render(ModelData model, String property,ColumnData config, int rowIndex, int colIndex,
					ListStore<ModelData> store, Grid<ModelData> grid) {
				if(model.get(property).toString().equals("true")){
					config.style = "background-image: url("+ GWT.getHostPageBaseURL().toString() + "resources/icons/add.gif) !important; background-repeat: no-repeat;";
				}else{
					config.style = "background-color: white;";
				}
				return "";
			}
		};
		
		return r;
		
		
	}
	
	
}
