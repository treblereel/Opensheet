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

import org.opensheet.client.dto.AuthmethodDTO;
import org.opensheet.client.services.AuthmethodService;
import org.opensheet.client.services.AuthmethodServiceAsync;
import org.opensheet.client.services.UserService;
import org.opensheet.client.services.UserServiceAsync;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**RELOAD STORE AFTER SELECT
 * 
 * 
 * 
 * @author chani
 *
 */
public class ImportUsersFromExternalStoreGrid extends ContentPanel{
	private ComboBox<BeanModel> exportSourceComboBox;
	private ListStore<BeanModel> exportSourceStore;
	private ListLoader<?> authmethodloader;
	private AuthmethodServiceAsync authmethodService = GWT.create(AuthmethodService.class);
	private UserServiceAsync userService   = GWT.create(UserService.class);
	private  Grid<BaseModel> usersGrid;
	
	
	
	public ImportUsersFromExternalStoreGrid(){
		
		setSize(650,450);
		setFrame(true);
		setHeaderVisible(false);
		
		ToolBar tb = new ToolBar();
		tb.add(new LabelToolItem("Select Import Source =>  "));
		tb.add(getImportUsersComboBox());
		add(tb);
		
		
		ToolBar btb = new ToolBar();
		
		Button saveSelectionBtn = new Button("Save");
		saveSelectionBtn.addListener(Events.Select,new Listener<BaseEvent>(){
			@Override	public void handleEvent(BaseEvent be) {
					importUsersToOpensheet(); 
			}
			
		});
		btb.add(saveSelectionBtn);
		
		Button reloadSelectionBtn = new Button("Reload");
		reloadSelectionBtn.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override		public void handleEvent(BaseEvent be) {
				Integer sourceId = Integer.parseInt(exportSourceComboBox.getValue().get("id").toString());
				loadGrid(sourceId);
			}
			
		});
		btb.add(reloadSelectionBtn);
		
		add(createGrid());
		
		setBottomComponent(btb);

	}
	
	private ContentPanel createGrid(){
		ContentPanel cp  = new ContentPanel();
		cp.setWidth(600);
		cp.setHeight(400);
		
		final  CheckBoxSelectionModel<BaseModel> sm = new CheckBoxSelectionModel<BaseModel>();  
	    sm.getColumn().setMenuDisabled(true);
	    sm.getColumn().setSortable(false);
	    sm.getColumn().setDataIndex("selected");
	    sm.setSelectionMode(SelectionMode.MULTI);
	    
	    List<ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		configs.add(sm.getColumn());


		
		ColumnConfig usersColumnConfig = new ColumnConfig("fullName", "Username", 150);
		usersColumnConfig.setSortable(false);
		usersColumnConfig.setMenuDisabled(true);
		configs.add(usersColumnConfig);
		
		ColumnConfig loginColumnConfig = new ColumnConfig("login", "Login", 150);
		loginColumnConfig.setSortable(false);
		loginColumnConfig.setMenuDisabled(true);
		configs.add(loginColumnConfig);
		
		ColumnConfig emailColumnConfig = new ColumnConfig("email", "email", 150);
		emailColumnConfig.setSortable(false);
		emailColumnConfig.setMenuDisabled(true);
		configs.add(emailColumnConfig);
		
		 ColumnModel cm = new ColumnModel(configs);
		    
		 usersGrid = new Grid<BaseModel>(new ListStore<BaseModel>(), cm); 
		 usersGrid.setLoadMask(true);
		 usersGrid.setWidth(600);
		 usersGrid.setHeight(350);
		 usersGrid.addStyleName(".my-table-style");
		 usersGrid.setBorders(true);
		 usersGrid.setSelectionModel(sm);

		 usersGrid.setAutoExpandColumn("login");
		 usersGrid.getView().setEmptyText("no data");
		 usersGrid.setId("usersGridId");
		
		 cp.add(usersGrid);
		
		return cp;
	}
	
	private ListStore<BeanModel> createImportSourceStore(){
		RpcProxy<List<AuthmethodDTO>> authmetodProxy =  new RpcProxy<List<AuthmethodDTO>>(){
			@Override protected void load(Object loadConfig,	AsyncCallback<List<AuthmethodDTO>> callback) {
				authmethodService.getImportSources(callback);
			}
        };
        
        authmethodloader = new BaseListLoader<ListLoadResult<ModelData>>(authmetodProxy,new BeanModelReader());
        exportSourceStore = new ListStore<BeanModel>(authmethodloader);
        exportSourceStore.setMonitorChanges(true);
        authmethodloader.load();
		
        return exportSourceStore;
	}

	private ComboBox<BeanModel> getImportUsersComboBox(){
		
		exportSourceComboBox = new ComboBox<BeanModel>();
		exportSourceComboBox.setDisplayField("description"); 
		exportSourceComboBox.setWidth(150);
		exportSourceComboBox.setName("authmethod");
		exportSourceComboBox.setFieldLabel("Auth method");
		exportSourceComboBox.setAllowBlank(false);
		exportSourceComboBox.setEditable(false);
		exportSourceComboBox.setStore(createImportSourceStore()); 
		exportSourceComboBox.setTypeAhead(true);
		exportSourceComboBox.setTriggerAction(TriggerAction.ALL);
		exportSourceComboBox.addListener(Events.Select, new Listener<FieldEvent>(){
  			@Override	public void handleEvent(FieldEvent fe) {
  					Integer sourceId = Integer.parseInt(exportSourceComboBox.getValue().get("id").toString());
  					loadGrid(sourceId);
  						}
          	   
             });
		exportSourceComboBox.select(exportSourceComboBox.getStore().getAt(0));
		
		return exportSourceComboBox;
	}
	
	private void loadGrid(Integer sourceId){
		 usersGrid.getStore().removeAll();
		 userService.getUsersFromExternalStore(new AuthmethodDTO(sourceId), new  AsyncCallback<List<BaseModel>>(){
			@Override			public void onFailure(Throwable caught) {}
			@Override			public void onSuccess(List<BaseModel> result) {
				usersGrid.getStore().add(result);
				
			}
			 
		 });
		
		
	}
	
	private void importUsersToOpensheet(){
		List<BaseModel> users = usersGrid.getSelectionModel().getSelectedItems();
		
		
		Integer storeId = Integer.parseInt(exportSourceComboBox.getValue().get("id").toString());
		if(users.isEmpty() == false && storeId != null){
			userService.setUsersFromExternalStore(users, new AuthmethodDTO(storeId), new AsyncCallback<Void>(){
				@Override	public void onFailure(Throwable caught) {
	
					 	MessageBox box = new MessageBox();
					    box.setButtons(MessageBox.CANCEL);
					    box.setIcon(MessageBox.QUESTION);
					    box.setTitleHtml("Error");
					    box.setMessage("Somethings wrong");
					    box.show();
					}
			
				@Override
				public void onSuccess(Void result) {
					Integer storeId = Integer.parseInt(exportSourceComboBox.getValue().get("id").toString());
					loadGrid(storeId);
					
				}
				
			});
		
		
		}
	}
	
	
	
}
