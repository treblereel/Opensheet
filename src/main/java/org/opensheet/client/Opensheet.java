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
package org.opensheet.client;

import org.opensheet.client.dto.UserDTO;
import org.opensheet.client.mvc.controllers.AdminController;
import org.opensheet.client.mvc.controllers.AppController;
import org.opensheet.client.mvc.controllers.SheetController;
import org.opensheet.client.mvc.events.AppEvents;
import org.opensheet.client.services.UserService;
import org.opensheet.client.services.UserServiceAsync;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.MessageBox;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Opensheet implements EntryPoint {

	private Timer sessionTimeoutResponseTimer;
    private UserServiceAsync userService = (UserServiceAsync) GWT.create(UserService.class);
    private final int INITIAL_TIMEOUT_PAD = 60000;
    private final int TIMEOUT_PAD = 15000; 
    
    
	
	@Override
	public void onModuleLoad() {
	
		initSessionTimers();
		
		
		
		
		final Dispatcher dispatcher = Dispatcher.get();
		dispatcher.addController(new AppController());
		dispatcher.addController(new SheetController());
		dispatcher.addController(new AdminController());

		
		final UserServiceAsync userService = GWT.create(UserService.class);
		userService.whoAmI(new AsyncCallback<UserDTO>(){
			@Override
			public void onFailure(Throwable arg0) {	
				
                MessageBox.alert("Something Wrong!!", arg0.getMessage(), null);
			}

			@Override
			public void onSuccess(UserDTO userDTO) {
				if(userDTO == null){
	                MessageBox.alert("Something Wrong!!", "Cant understand this.user", null);
				}else{
				    Registry.register("userCurrent", userDTO);
			        dispatcher.dispatch(AppEvents.Init);
				}
			}
		});
	    
		
	}

	
	
	
	
	  private void initSessionTimers()
	    {
	        
	        userService.getUserSessionTimeout(new AsyncCallback<Integer>()
	        {
	            public void onSuccess(Integer timeout)
	            {
	                sessionTimeoutResponseTimer = new Timer()
	                {
	                    @Override
	                    public void run()
	                    {
	                        checkUserSessionAlive();
	                    }
	                };
	                sessionTimeoutResponseTimer.schedule(timeout+INITIAL_TIMEOUT_PAD);
	            }

	            public void onFailure(Throwable caught)
	            {
	                displaySessionTimedOut();
	            }
	        });
	    }

	    private void checkUserSessionAlive()
	    {
	        userService.getUserSessionTimeout(new AsyncCallback<Integer>()
	        {
	            public void onSuccess(Integer timeout)
	            {
	                sessionTimeoutResponseTimer.cancel();
	                sessionTimeoutResponseTimer.schedule(timeout+TIMEOUT_PAD);
	            }

	            public void onFailure(Throwable caught)
	            {
	                displaySessionTimedOut();
	            }
	        });

	    }

	    private void displaySessionTimedOut()
	    {
	        MessageBox.alert(
	                "Session Timeout",
	                "Your session has timed out.",
	                new Listener<MessageBoxEvent>()
	        {

	            public void handleEvent(MessageBoxEvent be) {
	                Window.Location.reload();
	            }
	        });
	    }
	
}
