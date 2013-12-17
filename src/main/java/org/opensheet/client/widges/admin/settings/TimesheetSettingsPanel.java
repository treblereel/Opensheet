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
package org.opensheet.client.widges.admin.settings;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.opensheet.client.services.SystemService;
import org.opensheet.client.services.SystemServiceAsync;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;

public class TimesheetSettingsPanel extends FormPanel{
	private SystemServiceAsync systemService = GWT.create(SystemService.class);
	private CheckBox timeSheetInputModeCheckBox;
	private DateField timeSheetInputModeDateField;
	public TimesheetSettingsPanel(){
		setFrame(true);
		setWidth(724);
		setHeight(600);
		this.setHeaderVisible(false);
		
		FlexTable table = new FlexTable();  
	    table.getElement().getStyle().setProperty("margin", "10px");  
	    table.setCellSpacing(8);  
	    table.setCellPadding(4); 
	    
	    Label timeSheetInputModeLabel = new Label("Hard TimeSheet Input Mode");
	    timeSheetInputModeLabel.setWidth(200);
	    table.setWidget(0,0, timeSheetInputModeLabel);
	    
	    
	    timeSheetInputModeCheckBox  = new CheckBox(); 
	    timeSheetInputModeCheckBox.setWidth(100);
	    table.setWidget(0,1, timeSheetInputModeCheckBox);
	    
	    timeSheetInputModeDateField = new DateField();
	    timeSheetInputModeDateField.setWidth(100);
	    table.setWidget(0,2,timeSheetInputModeDateField);
	    
	    Button timeSheetInputModeButton = new Button("Save");
	    timeSheetInputModeButton.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override	public void componentSelected(ButtonEvent ce) {
				HashMap<Boolean, Date> result = new HashMap<Boolean, Date>();
				result.put(timeSheetInputModeCheckBox.getValue(), timeSheetInputModeDateField.getValue());
				systemService.setTimeSheetInputMode(result,new AsyncCallback<Void>(){
					@Override public void onFailure(Throwable caught) {
						MessageBox.info("Somethings wrong",caught.getMessage(), null);
					}
					@Override	public void onSuccess(Void result) {
						Info.display("Ok", "updated");
					}
				});
			}
	    });
	    table.setWidget(0,3,timeSheetInputModeButton);
	    add(table);
	    populateTimeSheetInputModeData();
	}

	private void populateTimeSheetInputModeData(){
		systemService.getTimeSheetInputMode(new AsyncCallback<HashMap<Boolean, Date>>(){
			@Override	public void onFailure(Throwable caught) {
				MessageBox.info("Somethings wrong",caught.getMessage(), null);
			}
			@Override	public void onSuccess(HashMap<Boolean, Date> result) {
				for(Map.Entry<Boolean, Date> kv: result.entrySet()){
					timeSheetInputModeCheckBox.setValue(kv.getKey());
					timeSheetInputModeDateField.setValue(kv.getValue());
				}
				
			}
			
				
		});
		
		
	}
	
}
