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

import org.opensheet.client.dto.UserDTO;
import org.opensheet.client.l10n.OpensheetConstants;
import org.opensheet.client.mvc.events.AdminEvents;
import org.opensheet.client.mvc.events.AppEvents;
import org.opensheet.client.utils.Resources;
import org.opensheet.client.widges.combo.LangComboBox;
import org.opensheet.client.widges.windows.ReferenceBookWindow;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;

public class SheetToolBar extends ToolBar{

	Dispatcher dispatcher = Dispatcher.get();
	private OpensheetConstants myMessages = (OpensheetConstants) GWT.create(OpensheetConstants.class);

	public SheetToolBar(){
		UserDTO userCurrent = (UserDTO)  Registry.get("userCurrent");
		final Dispatcher dispatcher = Dispatcher.get();
		this.setId("sheetToolBarId");
		
		Button opensheetButton = new Button("Opensheet");
		opensheetButton.setIconStyle("icon-opensheet"); 
		opensheetButton.addListener(Events.Select,new Listener<ButtonEvent>() {
   		 @Override public void handleEvent(ButtonEvent be) {
          	  dispatcher.dispatch(AppEvents.Sheet);
   	      }  
   	    });
		
		add(opensheetButton);
		add(new SeparatorToolItem());
		
		Button departmentsButton = new Button(myMessages.my_department());  
		departmentsButton.setIconStyle("icon-dm");
		
		if(userCurrent.getPermission().getDm() == true){
		 departmentsButton.addListener(Events.Select,new Listener<ButtonEvent>() {
   		 @Override public void handleEvent(ButtonEvent be) {
          	  dispatcher.dispatch(AppEvents.Department);
   	      }  
   	    });
		}else{
			departmentsButton.disable();	
		}
		add(departmentsButton);
		add(new SeparatorToolItem());
		
		Button projectsButton = new Button(myMessages.my_projects());  
		projectsButton.setIconStyle("icon-pm"); 
		if(userCurrent.getPermission().getPm() == true || userCurrent.getPermission().getAdmin() == true){
			projectsButton.addListener(Events.Select,new Listener<ButtonEvent>() {
	  		 @Override public void handleEvent(ButtonEvent be) {
	         	  dispatcher.dispatch(AppEvents.Project);
	  	      }  
	  	    });
		}else{
			projectsButton.disable();	
		}
		add(projectsButton);
		add(new SeparatorToolItem());
		
		Button financialButton = new Button(myMessages.my_reports());  
		financialButton.setIconStyle("icon-fd"); 
		if(userCurrent.getPermission().getFd() == true){

			financialButton.addListener(Events.Select,new Listener<ButtonEvent>() {
	  		 @Override public void handleEvent(ButtonEvent be) {
						dispatcher.dispatch(AppEvents.Financial);						

	  	      }  
	  	    });
		}else{
			financialButton.disable();	
		}
		add(financialButton);
		
		add(new SeparatorToolItem());
		Button referenceButton = new Button("Reference book");
		referenceButton.disable();
		referenceButton.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override		public void componentSelected(ButtonEvent ce) {
				GWT.runAsync(new RunAsyncCallback(){
					@Override		public void onFailure(Throwable reason) {
						MessageBox.info("Error",reason.getMessage(),null);
					}
					@Override	public void onSuccess() {
						new ReferenceBookWindow();
					}
				});
			}
		});
		referenceButton.setIcon(Resources.ICONS.album());
		add(referenceButton);
		
		add(new SeparatorToolItem());
		
		add(new FillToolItem());
		LangComboBox langComboBox =  new LangComboBox(userCurrent.getLang());
		add(langComboBox);
//		langComboBox.setLang(user.getLang());
		
		add(new SeparatorToolItem());
		Button adminButton = new Button(myMessages.administration());
		adminButton.setIconStyle("icon-admin");
		if(userCurrent.getPermission().getAdmin() == true){
			 adminButton.addListener(Events.Select,new Listener<ButtonEvent>() {
				 @Override public void handleEvent(ButtonEvent be) {
					 dispatcher.dispatch(AdminEvents.AdminUser);
			      }  
			    });  
		}else{
			adminButton.disable();
		}
		 add(adminButton); 
		 
		 add(new SeparatorToolItem());

		 
		 
		 Button logoutButton = new Button(myMessages.logout());
		 logoutButton.setIconStyle("icon-logout");
		 logoutButton.addListener(Events.Select,new Listener<ButtonEvent>() {
			 @Override public void handleEvent(ButtonEvent be) {
					Window.Location.assign(GWT.getHostPageBaseURL().toString() +"j_spring_security_logout");
		      }  
		    });  
		 add(logoutButton); 
		 add(new SeparatorToolItem());
		 
		
	}
	
	
}
