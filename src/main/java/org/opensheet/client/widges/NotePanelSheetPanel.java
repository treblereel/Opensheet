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

import java.util.Arrays;
import java.util.Date;

import org.opensheet.client.dto.UserDTO;
import org.opensheet.client.l10n.OpensheetConstants;
import org.opensheet.client.l10n.OpensheetErrorConstants;
import org.opensheet.client.services.HourService;
import org.opensheet.client.services.HourServiceAsync;
import org.opensheet.client.utils.Resources;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HtmlEditor;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class NotePanelSheetPanel extends FormPanel{
	private Button saveBtn;
	private UserDTO userDTO;
	private Integer assignmentId;
	private Date date;
	private HtmlEditor a;
	private HourServiceAsync hourService = GWT.create(HourService.class);
	private OpensheetConstants myConstants = (OpensheetConstants) GWT.create(OpensheetConstants.class);
	private OpensheetErrorConstants opensheetErrorConstants = (OpensheetErrorConstants) GWT.create(OpensheetErrorConstants.class);

	
	public NotePanelSheetPanel(){
	
		this.setWidth(590);
		this.setHeight(180);

		this.setHeadingHtml(myConstants.hour_note());
		this.setLayout(new RowLayout());


		
		
		a = new HtmlEditor();  
	    a.setHeight(165);
	    a.setWidth(580);
	    a.disable();

	    add(a, new FormData("100%"));

	    
	    
	    saveBtn = new Button(myConstants.save());
	    saveBtn.setIcon(Resources.ICONS.add());
	    saveBtn.addListener(Events.Select,new Listener<BaseEvent>(){
			@Override	public void handleEvent(BaseEvent be) {
				
				hourService.setNote(userDTO.getId(), assignmentId, date, a.getValue(),new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
	                    MessageBox.alert(opensheetErrorConstants.cannot_save_note(), caught.getMessage(), null);
					}
					@Override
					public void onSuccess(Void result) {
                   	 Info.display("Note Has Been saved", "Succesfull");
					}
				});
			}
	    });
	    
	    
	    this.setButtonAlign(HorizontalAlignment.CENTER);
	    this.addButton(saveBtn);

	}
	
	public void updateData(Integer hour,UserDTO userDTO, Date date,Integer assignment,Boolean leaf,Integer type){
		
		
		Integer[] types  = {0,1,2,3};
		if(!Arrays.asList(types).contains(type)){
			a.clear();
			a.disable();
			a.setValue("It's not an assignment");
		}else if(leaf != true){
			a.clear();
			a.disable();
			a.setValue("It's not a leaf ");
		}else if(hour == null){
			a.clear();
			a.disable();
			a.setValue("Set Hour first");
		}else{
			this.assignmentId = assignment;
			this.userDTO =      userDTO;
			this.date = date;
			a.clear();
			
			hourService.getNote(userDTO.getId(), assignment, date,new AsyncCallback<String>(){
				@Override	public void onFailure(Throwable caught) { 
                    MessageBox.alert("Cannot get Note", caught.getMessage(), null);
				}
				@Override
				public void onSuccess(String result) {
					a.setValue(result);
				}
			});
			a.enable();
		}

		
	}

}
