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


import java.util.List;

import org.opensheet.client.dto.AssignmentDTO;
import org.opensheet.client.dto.DepartmentDTO;
import org.opensheet.client.services.AssignmentService;
import org.opensheet.client.services.AssignmentServiceAsync;
import org.opensheet.client.services.UserService;
import org.opensheet.client.services.UserServiceAsync;
import org.opensheet.client.utils.Languages;

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
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AssignmentComboBox extends ComboBox<BeanModel>{
	private ListStore<BeanModel>  store;
	private AssignmentServiceAsync assignmentService  = GWT.create(AssignmentService.class);
	private Integer branchId;
	private Integer typeId;
	
	
	public AssignmentComboBox(){
		
		 RpcProxy<List<AssignmentDTO>> proxy = new RpcProxy<List<AssignmentDTO>>() {
	            @Override
	            protected void load(Object loadConfig, AsyncCallback<List<AssignmentDTO>> callback) {
	            	assignmentService.getAssignmentsByBranchAndByType(branchId, typeId,true, callback);
	            }
		};
		ListLoader<?> loader = new BaseListLoader<ListLoadResult<ModelData>>(proxy,new BeanModelReader());
		store = new ListStore<BeanModel>(loader);
		loader.load();
		
		
		
		store.setMonitorChanges(true);

       
        setEditable(false);  
        setWidth(150);
        setDisplayField("name");
        setName("name");
        setStore(store);
        setAllowBlank(true);
        setValue(store.getAt(0));
        setEmptyText(" Any");
        
        
        
        
	}
	
	public void setData(Integer branch,Integer type){
		if(branch!= null && type != null){
			this.branchId = branch;
			this.typeId = type;
			this.getStore().getLoader().load();
			setValue(store.findModel("id", "9999999"));
		}
	}

}
