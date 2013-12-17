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

import org.opensheet.client.dto.BranchDTO;
import org.opensheet.client.services.BranchService;
import org.opensheet.client.services.BranchServiceAsync;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class BranchComboBox  extends ComboBox<BeanModel>{
	private BranchServiceAsync branchService  = GWT.create(BranchService.class);
	private ListStore<BeanModel> branchStore;
	private ListLoader<?> 		 loader;
	private Boolean addAllValue; 
	
	/* args:
	 * Boolean: add "ALL" value to combo
	 * 
	 * 
	 */
	public BranchComboBox(Boolean all){
		super();
		this.addAllValue = all;
		 
	   setEditable(false);  
	   setWidth(100);
	   setDisplayField("name");
	   setName("name");
	   setFieldLabel("Branch");
	   setAllowBlank(false);
	   setStore(load());
	   setForceSelection(true);
	   setValue(store.getAt(0));
	   setLazyRender(false);
	}
	
	
	private ListStore<BeanModel> load(){
		RpcProxy<List<BranchDTO>> proxy = new RpcProxy<List<BranchDTO>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<List<BranchDTO>> callback) {
            	branchService.getBranchListForCombo(true,addAllValue, callback);
            }
        };
	    
	    loader = new BaseListLoader<ListLoadResult<ModelData>>(proxy,new BeanModelReader());
	    branchStore = new ListStore<BeanModel>(loader);
	    branchStore.setMonitorChanges(true);
	    loader.load();
		return branchStore;
	}
	
	
	public BranchDTO get(){
		
		return new BranchDTO(Integer.parseInt(getValue().get("id").toString()));
	}
	
	
	public void loadData(){
		load();
	}
	
	public void set(BranchDTO b){
		setValue(getStore().findModel("id", b.getId()));
	}
	

}
