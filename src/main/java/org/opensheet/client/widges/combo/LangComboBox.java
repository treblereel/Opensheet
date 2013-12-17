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
package org.opensheet.client.widges.combo;

import java.util.List;

import org.opensheet.client.dto.UserDTO;
import org.opensheet.client.services.UserService;
import org.opensheet.client.services.UserServiceAsync;
import org.opensheet.client.utils.Languages;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LangComboBox extends ComboBox<Languages>{
	private ListStore<Languages>  store;
	private UserServiceAsync userService  = GWT.create(UserService.class);
	
	public LangComboBox(String id){
		List<Languages> list = Languages.get();  
        store = new ListStore<Languages>();
        store.add(list); 
        
  
        
        setTriggerAction(TriggerAction.ALL);  
        setEditable(false);  
        setWidth(150);
        setDisplayField("name");
        setName("name");
        setStore(store);
        setAllowBlank(false);
        setTemplate(getFlagTemplate());  

        setValue(getStore().findModel("id", id));
        addSelectionChangedListener(new SelectionChangedListener<Languages>(){
			@Override	public void selectionChanged(SelectionChangedEvent<Languages> se) {
				UserDTO user	 = (UserDTO)  Registry.get("userCurrent"); 
				user.setLang(se.getSelectedItem().getId());
				userService.setLang(user, new AsyncCallback<Void>(){
					@Override	public void onFailure(Throwable caught) {
						MessageBox.info("Error",caught.getMessage(),null);
					}
					@Override	public void onSuccess(Void result) {
						Window.Location.assign(GWT.getHostPageBaseURL().toString()+"Opensheet.htm");
					}
					
				});

				
			}
        	
        });
    setValue(getStore().findModel("id", id));
        
        
	}
	
	private native String getFlagTemplate() /*-{ 
    return  [ 
    '<tpl for=".">', 
    '<div class="x-combo-list-item"><img width="16px" height="11px" src="resources/images/flags/{[values.id]}.png"> {[values.name]}</div>', 
    '</tpl>' 
    ].join(""); 
  }-*/;

	public void setLang(String id){
		setValue(getStore().findModel("id", id));
	}
	
	
}
