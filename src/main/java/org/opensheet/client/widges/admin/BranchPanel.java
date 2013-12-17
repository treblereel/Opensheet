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

import org.opensheet.client.dto.BranchDTO;
import org.opensheet.client.dto.UserDTO;
import org.opensheet.client.services.BranchService;
import org.opensheet.client.services.BranchServiceAsync;
import org.opensheet.client.services.UserService;
import org.opensheet.client.services.UserServiceAsync;
import org.opensheet.client.utils.Resources;
import org.opensheet.client.widges.admin.windows.AddBranchWindow;


import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class BranchPanel extends ContentPanel{
	private Boolean status = true;
	private ListStore<BeanModel> store;
	
	private ListLoader<?> 		 userloader;
	
	private ListLoader<ListLoadResult<BeanModel>> loader;
	private BranchServiceAsync branchService = GWT.create(BranchService.class);
	private UserServiceAsync 		userService       = GWT.create(UserService.class);
	private Integer branchId;
	private TextField<String> branchName,newBranchName,index;
	private CheckBox statusBox,newStatusBox;
	private FormPanel fp;
	private ComboBox<BeanModel> ownerComboBox,newOwnerComboBox;
	private Grid<BeanModel> grid;
	private BeanModel selectedBean;
	private Button activeButton;
	
	public BranchPanel(){
		
		this.setHeadingHtml("Branch Managment");
		this.setWidth(1024);
		this.setHeight(600);
		this.setFrame(true);
		this.setTopComponent(makeToolBar());
		this.setLayout(new ColumnLayout());
		
		add(getGrid());
		add(getExistingBranchFormPanel());
	}
	
	
	private ToolBar makeToolBar(){
		ToolBar toolBar = new ToolBar();
		
		activeButton = new Button("Show Active");
		activeButton.addListener(Events.Select,new Listener<BaseEvent>(){
			@Override		public void handleEvent(BaseEvent be) {
				if(status == true){
					activeButton.setText("Show Active");
					
					status = false;	
				}else{
					activeButton.setText("Show Inactive");
					status = true;
				}
				
				reloadGrid();
			}
			
		});
		toolBar.add(activeButton);
		
		Button addNewBranchButton =  new Button("Add new Branch");
		addNewBranchButton.setIcon(Resources.ICONS.add());
		addNewBranchButton.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override			public void handleEvent(BaseEvent be) {
					addBranchWindow();
				}
		});
		
		toolBar.add(addNewBranchButton);
		
		
		return toolBar;
	}
	
	private void addBranchWindow(){
		final BranchPanel panel = this;
		GWT.runAsync(new RunAsyncCallback(){
			@Override
			public void onFailure(Throwable reason) {
				MessageBox.info("ERROR", reason.getMessage(), null);
			}
			@Override
			public void onSuccess() {
				new AddBranchWindow(panel);				
			}
		});
		
	}
	
	private Grid<BeanModel> getGrid(){
		loadGrid();
		
		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		
		ColumnConfig nameColumnConfig = new ColumnConfig("name", "Branch", 120);
		nameColumnConfig.setSortable(false);
		nameColumnConfig.setMenuDisabled(true);
		columns.add(nameColumnConfig);
		
		ColumnConfig ownerColumnConfig = new ColumnConfig("owner.fullName", "Owner", 120);
		ownerColumnConfig.setSortable(false);
		ownerColumnConfig.setMenuDisabled(true);
		columns.add(ownerColumnConfig);
		
		
		ColumnModel cm = new ColumnModel(columns);

		grid = new Grid<BeanModel>(store, cm);
		grid.setLoadMask(true);
	    grid.setWidth(400);
	    grid.setHeight(300);
	    grid.addStyleName(".my-table-style");
	    grid.setBorders(true);
	    grid.setAutoExpandColumn("name");
	    grid.getView().setEmptyText("no data");
	    grid.setId("myBranchid");
        grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);  
	    grid.getSelectionModel().addListener(Events.SelectionChange,new Listener<SelectionChangedEvent<BeanModel>>(){
			@Override	public void handleEvent(SelectionChangedEvent<BeanModel> be) {
				branchId = Integer.parseInt(grid.getSelectionModel().getSelectedItem().get("id").toString());
				loadFormPanel(branchId);
			}
	    });
	    
	    
	    
	    return grid;
	}
	
	private void loadGrid(){
		RpcProxy<List<BranchDTO>> proxy = new RpcProxy<List<BranchDTO>>() {
            @Override  protected void load(Object loadConfig, AsyncCallback<List<BranchDTO>> callback) {
            	branchService.getBranchList(status,callback);
            }
        };
		
        loader = new BaseListLoader<ListLoadResult<BeanModel>>(proxy,new BeanModelReader());
        store = new ListStore<BeanModel>(loader);
        store.setMonitorChanges(true);
        loader.load();
        
	}
	
	public void reloadGrid(){
		grid.getStore().getLoader().load();
		grid.getView().refresh(true);
		grid.getView().refresh(true); /// hmmm, fix it
	}
	
	/**String t
	 * params: new and exist branch
	 * 
	 * 
	 * @return
	 */
	
	private FormPanel getExistingBranchFormPanel(){
			fp = getFormPanel();
		
			Button saveBranch = new Button("Save");
			saveBranch.addListener(Events.Select,new Listener<BaseEvent>(){
				@Override	public void handleEvent(BaseEvent be) {
					if(branchName.isValid() && ownerComboBox.isValid() && branchId != null){
						
						selectedBean = grid.getSelectionModel().getSelectedItem();
						
						
						BranchDTO branchDTO = new BranchDTO();
						branchDTO.setId(Integer.parseInt(grid.getSelectionModel().getSelectedItem().get("id").toString()));
						branchDTO.setName(branchName.getValue());
						UserDTO owner = new UserDTO(Integer.parseInt(ownerComboBox.getValue().get("id").toString()));
						branchDTO.setOwner(owner);
						branchDTO.setIndex(index.getValue());
						
						branchDTO.setStatus(statusBox.getValue());
						

						
						branchService.setBranch(branchDTO, new AsyncCallback<Void>(){
							@Override	public void onFailure(Throwable caught) {
					            MessageBox.alert("Cannot update Branch", caught.getMessage(), null);
							}
							@Override	public void onSuccess(Void result) {
								reloadGrid();
								
								Info.display("Success", "Branch has been updated");
								fp.clear();

								reloadGrid();
								
							}
						});
					}
				}
			});
			
			fp.addButton(saveBranch);
			
			Button realadBranch = new Button("Reload");
			realadBranch.addListener(Events.Select,new Listener<BaseEvent>(){
				@Override	public void handleEvent(BaseEvent be) {
					fp.clear();
			
					reloadGrid();
					
				}
			});
			fp.addButton(realadBranch);
			
		return fp;
		
	}
	
	
	private FormPanel getFormPanel(){
		
		
		FormPanel formPanel = new FormPanel();
		formPanel.setHeaderVisible(false);
		formPanel.setHeight(300);
		formPanel.setWidth(300);
		
		branchName  = new TextField<String>();
		branchName.setTitle("name");
		branchName.setFieldLabel("Name");
		branchName.setAutoValidate(true);
		branchName.setAllowBlank(false);
		formPanel.add(branchName,new FormData(150, 22));

		statusBox = new CheckBox();
		statusBox.setFieldLabel("Status");
		formPanel.add(statusBox);
		
		ownerComboBox = new ComboBox<BeanModel>();
        ownerComboBox.setDisplayField("fullName");  
        ownerComboBox.setWidth(150);
        ownerComboBox.setName("user");
        ownerComboBox.setFieldLabel("Owner");
        ownerComboBox.setAllowBlank(false);
        ownerComboBox.setEditable(false);
        ownerComboBox.setTypeAhead(true);  
        ownerComboBox.setTriggerAction(TriggerAction.ALL);  
        ownerComboBox.setStore(loadOwnerStore());
        ownerComboBox.addListener(Events.BeforeRender,new Listener<BaseEvent>(){
			@Override	public void handleEvent(BaseEvent be) {
				if(ownerComboBox.getStore() != null){
					ownerComboBox.setStore(loadOwnerStore());
				}else{
					ownerComboBox.setStore(loadOwnerStore());	
				}
			}
        });
        ownerComboBox.setValue(ownerComboBox.getStore().getAt(0));
        formPanel.add(ownerComboBox,new FormData(150, 22));
        
        index  = new TextField<String>();
		index.setTitle("index");
		index.setFieldLabel("Branch Index");
		index.setAutoValidate(true);
		index.setAllowBlank(false);
		formPanel.add(index,new FormData(150, 22));
		
		
		return formPanel;
	}
	
	private void loadFormPanel(Integer id){
		fp.clear();
		branchService.getBranchById(id, new AsyncCallback<BranchDTO>(){

			@Override	public void onFailure(Throwable caught) {
	               MessageBox.alert("Cannot find Branch", caught.getMessage(), null);
			}

			@Override	public void onSuccess(BranchDTO result) {
				branchName.setValue(result.getName());
				statusBox.setValue(result.getStatus());
				ownerComboBox.setValue(ownerComboBox.getStore().findModel("id", result.getOwner().getId()));
				index.setValue(result.getIndex());
			}
			
			
		});
		
	}
	
	
	
	
	private ListStore<BeanModel> loadOwnerStore(){
		
		RpcProxy<List<UserDTO>> UserProxy = new RpcProxy<List<UserDTO>>() {
            @Override   protected void load(Object loadConfig, AsyncCallback<List<UserDTO>> callback) {
            	userService.getUsersByRole("fd",callback);
            }
        };
 	    userloader = new BaseListLoader<ListLoadResult<ModelData>>(UserProxy,new BeanModelReader());
	    final ListStore<BeanModel> userStore = new ListStore<BeanModel>(userloader);
	    userStore.setMonitorChanges(true);
	    userloader.load();
		return userStore;
	}
	
	
	public void reloadOwnerCombo(){
		ownerComboBox.getStore().removeAll();
		ownerComboBox.setStore(loadOwnerStore());
	}
	

	
}
