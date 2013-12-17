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
package org.opensheet.client.widges;

import java.util.List;

import org.opensheet.client.dto.UserDTO;
import org.opensheet.client.mvc.events.EventBus;
import org.opensheet.client.mvc.events.SheetPanelEvent;
import org.opensheet.client.services.UserService;
import org.opensheet.client.services.UserServiceAsync;
import org.opensheet.client.utils.Resources;

import com.extjs.gxt.ui.client.data.BaseListLoader;
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
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.menu.AdapterMenuItem;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserSwitchMenu extends Button {
	private UserDTO user;
	private BranchComboBox branchComboBox;
	private RadioGroup radioGroup;
	private ComboBox<BeanModel> usersComboBox;
	final private UserServiceAsync userService = GWT.create(UserService.class);
	private ListStore<BeanModel> userStore;
	private ListLoader<?> userloader;
	private Integer branch = 9999999;
	private String  userStatus = "1";
	private Integer selectedUser;
	
	public UserSwitchMenu(UserDTO user){
		super();
		this.user = user;
	 
	    setWidth(200);
	    setText(user.getfullName());
	    setTitle(user.getfullName());
	    setIcon(Resources.ICONS.user_add());  
	    
	    if(user.getPermission().getAdmin() != true || user.getPermission().getDm() == true ){
	    	doMenu();
	    }
	}    
	    
	private void doMenu(){    
	
	    Menu menu = new Menu(); 
	    branchComboBox = new BranchComboBox(true);
	    branchComboBox.setWidth(180);
	    branchComboBox.setValue(branchComboBox.getStore().getAt(0));
	    branchComboBox.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				branch = Integer.parseInt(branchComboBox.getValue().get("id").toString());
				if( usersComboBox.getStore().getModels().isEmpty() != true){
					
					usersComboBox.getStore().removeAll();
					usersComboBox.getStore().getLoader().load();
				}
			}
	    });
	    
	    AdapterMenuItem adapter = new AdapterMenuItem(branchComboBox);  
	    adapter.setManageFocus(true);  
	    adapter.setCanActivate(true); 

	    Radio radioAll = new Radio();  
	    radioAll.setBoxLabel("ALL"); 
	    radioAll.setItemId("any");
	    radioAll.setValue(true); 
	    
	    Radio radioActive = new Radio();  
	    radioActive.setBoxLabel("Active"); 
	    radioActive.setItemId("1");
	    radioActive.setValue(true); 
	    
	    
	    Radio radioInactive = new Radio();  
	    radioInactive.setItemId("0");
	    radioInactive.setBoxLabel("Inactive");  
	    radioInactive.setValue(true); 
	    
	    radioGroup = new RadioGroup();
	    radioGroup.setFieldLabel("Users status: ");
	    radioGroup.addListener(Events.Change,new  Listener<FieldEvent>(){
			@Override		public void handleEvent(FieldEvent be) {
				userStatus = radioGroup.getValue().getItemId();
				
				usersComboBox.getStore().removeAll();
				usersComboBox.getStore().getLoader().load();
				
			}
	    	
	    });
	    
	    radioGroup.add(radioAll);
	    radioGroup.add(radioActive);
	    radioGroup.add(radioInactive);
	    radioGroup.setValue(radioActive);
	    
	    menu.add(new MenuItem("<b>Branch:</b>")); 
	    menu.add(adapter);
	    
	    menu.add(new MenuItem("<b>Users:</b>")); 
	    menu.add(radioGroup);
	    setMenu(menu) ;  
	 
	    RpcProxy<List<UserDTO>> UserProxy = new RpcProxy<List<UserDTO>>() {
            @Override  protected void load(Object loadConfig, AsyncCallback<List<UserDTO>> callback) {
            	userService.getUsersAccordingOfAskersRoleByStatusAndByBranch(userStatus,branch,callback);
            }
        };
        userloader = new BaseListLoader<ListLoadResult<ModelData>>(UserProxy,new BeanModelReader());
        userStore = new ListStore<BeanModel>(userloader);
        userStore.setMonitorChanges(true);
        userloader.load();
	    
	    
	    
	    usersComboBox = new ComboBox<BeanModel>();
	    usersComboBox.setDisplayField("fullName");  
	    usersComboBox.setWidth(200);
	    usersComboBox.setName("user");
	    usersComboBox.setFieldLabel("users:");
	    usersComboBox.setAllowBlank(true);
	    usersComboBox.setEditable(false);
	    usersComboBox.setStore(userStore);
	    usersComboBox.setEmptyText("This User");
	    usersComboBox.setTypeAhead(true);  
	    usersComboBox.setTriggerAction(TriggerAction.ALL); 
	    usersComboBox.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override	public void handleEvent(BaseEvent be) {
				setButtonText(usersComboBox.getValue().get("fullName").toString());
				selectedUser = usersComboBox.getValue().get("id");
				EventBus.get().fireEvent(SheetPanelEvent.UserChanged, new BaseEvent(SheetPanelEvent.UserChanged));
				closeAll();
			}
	    });
	    
	    
	    AdapterMenuItem adapterUsersComboBox = new AdapterMenuItem(usersComboBox);  
	    adapterUsersComboBox.setManageFocus(true);  
	    adapterUsersComboBox.setCanActivate(true);
	    menu.add(adapterUsersComboBox);
	}
	
	
	private void closeAll(){
		this.hideMenu();
		
	}
	
	public void setButtonText(String text){
		this.setText(text);
		
	}
	

	public UserDTO getSelectedUser(){
		return new UserDTO(selectedUser);
	}

}
