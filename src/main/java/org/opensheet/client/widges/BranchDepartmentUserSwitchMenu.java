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

import org.opensheet.client.dto.DepartmentDTO;
import org.opensheet.client.mvc.events.AdminEvents;
import org.opensheet.client.mvc.events.EventBus;
import org.opensheet.client.services.DepartmentService;
import org.opensheet.client.services.DepartmentServiceAsync;
import org.opensheet.client.utils.Resources;

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
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.menu.AdapterMenuItem;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class BranchDepartmentUserSwitchMenu  extends Button{

	
	
	final private DepartmentServiceAsync departmentService = GWT.create(DepartmentService.class);

	private BranchComboBox branchComboBox;
	private Integer branch     = 1;
	private Integer department = 1;
	private ComboBox<BeanModel>   departmentComboBox;
	private ListStore<BeanModel>  departmentStore;
	private ListLoader<?> dloader;
	private BaseModel result;
	
	public BranchDepartmentUserSwitchMenu(){
		super();
		doMenu();
		
		setWidth(200);
	    setText("Choose Branch/Department: ");
	    setTitle("Choose Branch/Department: ");
	    setIcon(Resources.ICONS.user_add());  
		
	}
	
	private void doMenu(){    
		
	    Menu menu = new Menu(); 
	    branchComboBox = new BranchComboBox(false);
	    branchComboBox.setWidth(180);
	    branchComboBox.setValue(branchComboBox.getStore().getAt(0));
	    branchComboBox.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				branch = Integer.parseInt(branchComboBox.getValue().get("id").toString());
				departmentComboBox.getStore().getLoader().load();
			}
	    });
	    
	    AdapterMenuItem adapterBranch = new AdapterMenuItem(branchComboBox);  
	    adapterBranch.setManageFocus(true);  
	    adapterBranch.setCanActivate(true); 
	    
	    
	    RpcProxy<List<DepartmentDTO>> departmentProxy = new RpcProxy<List<DepartmentDTO>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<List<DepartmentDTO>> callback) {
            	departmentService.getDepartmentsByBranch("1", branch, callback);
            }
        }; 
 	   
        dloader = new BaseListLoader<ListLoadResult<ModelData>>(departmentProxy,new BeanModelReader());
	    departmentStore = new ListStore<BeanModel>(dloader);
	    departmentStore.setMonitorChanges(true);
	    
	    
	    
	    departmentComboBox = new ComboBox<BeanModel>();
        departmentComboBox.setDisplayField("name");  
        departmentComboBox.setWidth(150);
        departmentComboBox.setName("department");
        departmentComboBox.setFieldLabel("Department");
        departmentComboBox.setAllowBlank(false);
        departmentComboBox.setEditable(false);
        departmentComboBox.setStore(departmentStore);  
        departmentComboBox.setTypeAhead(true);  
        departmentComboBox.setTriggerAction(TriggerAction.ALL); 
        departmentComboBox.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				department = Integer.parseInt(departmentComboBox.getValue().get("id").toString());
				result = new BaseModel();
				result.set("branch", branch);
				result.set("department", department);
				EventBus.get().fireEvent(AdminEvents.AdminAssignmentDepartmentUserSwitch, new BaseEvent(AdminEvents.AdminAssignmentDepartmentUserSwitch));
				closeAll();
			}
	    });
	    
	    
        AdapterMenuItem adapterDepartment = new AdapterMenuItem(departmentComboBox);  
        adapterDepartment.setManageFocus(true);  
        adapterDepartment.setCanActivate(true); 
	    
	    
	    

	    menu.add(new MenuItem("<b>Branch:</b>")); 
	    menu.add(adapterBranch);
	    menu.add(new MenuItem("<b>Department:</b>")); 
	    menu.add(adapterDepartment);
	    
	    setMenu(menu) ; 
	}

	public BaseModel getResult(){
		return result;
	}
	
	private void closeAll(){
		this.hideMenu();
	}

}
