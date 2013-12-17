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

import java.util.List;

import org.opensheet.client.dto.BranchDTO;
import org.opensheet.client.dto.UserDTO;
import org.opensheet.client.services.BranchService;
import org.opensheet.client.services.BranchServiceAsync;
import org.opensheet.client.services.UserService;
import org.opensheet.client.services.UserServiceAsync;
import org.opensheet.client.widges.admin.BranchPanel;

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
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AddBranchWindow extends Window {
	public BranchPanel panel;
 	private BranchServiceAsync      branchService = GWT.create(BranchService.class);
	private UserServiceAsync 		userService       = GWT.create(UserService.class);
	private TextField<String>newBranchName,index;
	private CheckBox  newStatusBox;
	private ComboBox<BeanModel> newOwnerComboBox;
	private ListLoader<?> 		 userloader;

	
	public AddBranchWindow(BranchPanel panel){
		this.panel = panel;
		
		setResizable(false);
		setHeadingHtml("Add new Branch");
		setTitle("Add new Branch");
		setWidth(310);
		setHeight(300);
		setTitle("New Branch");
		
		final FormPanel newBranchFormPanel = new FormPanel();
		newBranchFormPanel.setHeaderVisible(false);
		newBranchFormPanel.setWidth(300);
		newBranchFormPanel.setHeight(200);
		
		newBranchName  = new TextField<String>();
		newBranchName.setTitle("name");
		newBranchName.setFieldLabel("Name");
		newBranchName.setAutoValidate(true);
		newBranchName.setAllowBlank(false);
		newBranchFormPanel.add(newBranchName,new FormData(150, 22));

		newStatusBox = new CheckBox();
		newStatusBox.setFieldLabel("Status");
		newBranchFormPanel.add(newStatusBox);
		
		newOwnerComboBox = new ComboBox<BeanModel>();
		newOwnerComboBox.setDisplayField("fullName");  
		newOwnerComboBox.setWidth(150);
		newOwnerComboBox.setName("user");
		newOwnerComboBox.setFieldLabel("Owner");
		newOwnerComboBox.setAllowBlank(false);
		newOwnerComboBox.setEditable(false);
		newOwnerComboBox.setTypeAhead(true);  
		newOwnerComboBox.setTriggerAction(TriggerAction.ALL);  
		newOwnerComboBox.setStore(loadOwnerStore());
		newOwnerComboBox.addListener(Events.BeforeRender,new Listener<BaseEvent>(){
			@Override	public void handleEvent(BaseEvent be) {
				if(newOwnerComboBox.getStore() != null){
					newOwnerComboBox.setStore(loadOwnerStore());
				}else{
					newOwnerComboBox.setStore(loadOwnerStore());	
				}
			}
        });
		newOwnerComboBox.setValue(newOwnerComboBox.getStore().getAt(0));
		newBranchFormPanel.add(newOwnerComboBox,new FormData(150, 22));
		
	
		
		
		newStatusBox.setValue(true);
		newOwnerComboBox.setValue(newOwnerComboBox.getStore().getAt(0));
		
	     index  = new TextField<String>();
	     index.setTitle("index");
		 index.setFieldLabel("Branch Index");
		 index.setAutoValidate(true);
		 index.setAllowBlank(false);
		 newBranchFormPanel.add(index,new FormData(150, 22));
		 
		 
		
		Button save = new Button("Save");
		save.addListener(Events.Select,new Listener<BaseEvent>(){
			@Override			public void handleEvent(BaseEvent be) {
				if(newBranchName.isValid() && newOwnerComboBox.isValid()){
					BranchDTO branchDTO = new BranchDTO();
					branchDTO.setName(newBranchName.getValue());
					UserDTO owner = new UserDTO(Integer.parseInt(newOwnerComboBox.getValue().get("id").toString()));
					branchDTO.setOwner(owner);
					branchDTO.setStatus(newStatusBox.getValue());
					branchDTO.setIndex(index.getValue());
					
					branchService.addBranchDTO(branchDTO, new AsyncCallback<Void>(){
						@Override		public void onFailure(Throwable caught) {
				            MessageBox.alert("Cannot add new Branch", caught.getMessage(), null);
						}
						@Override	public void onSuccess(Void result) {
							reloadGrid();
							Info.display("Success", "New Branch has been added");
							reloadGrid();
						    hide();
						}
					});
				}
			}
		});
		
		
		
		Button close = new Button("Close");
		close.addListener(Events.Select,new Listener<BaseEvent>(){
			@Override			public void handleEvent(BaseEvent be) {
				hide();	
				
			}
			
		});

		addButton(save);
		addButton(close);
		
		add(newBranchFormPanel);
		show();
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
	
	
	private void reloadGrid(){
		panel.reloadGrid();
	}
	
}
