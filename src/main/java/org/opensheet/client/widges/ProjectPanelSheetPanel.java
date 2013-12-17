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

import org.opensheet.client.dto.AssignmentDTO;
import org.opensheet.client.l10n.OpensheetConstants;
import org.opensheet.client.services.AssignmentService;
import org.opensheet.client.services.AssignmentServiceAsync;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ProjectPanelSheetPanel extends FormPanel{
	private Integer assignemntId;
	private Integer sum;
	
	private TextField<String> assignmentName,assignmentIndex,assignmentOwner,assignmentBranch,assignmentTimeline;
	private AssignmentServiceAsync assignmentService = GWT.create(AssignmentService.class);
	private OpensheetConstants myConstants = (OpensheetConstants) GWT.create(OpensheetConstants.class);

	public ProjectPanelSheetPanel(){
		
		this.setFrame(true);
		this.setWidth(590);
		this.setHeight(200);
		this.setHeadingHtml(myConstants.assignment_details());
		
		LayoutContainer main = new LayoutContainer();  
	    main.setLayout(new ColumnLayout()); 
	    
	    LayoutContainer left = new LayoutContainer();  
	    left.setStyleAttribute("paddingRight", "10px");  
	    FormLayout layout = new FormLayout();  
	    layout.setLabelAlign(LabelAlign.TOP);  
	    left.setLayout(layout);  
	    
	    LayoutContainer right = new LayoutContainer();  
	    right.setStyleAttribute("paddingLeft", "10px");  
	    layout = new FormLayout();  
	    layout.setLabelAlign(LabelAlign.TOP);  
	    right.setLayout(layout); 
	    
	    assignmentName = new TextField<String>(); 
	    assignmentName.setFieldLabel(myConstants.name()); 
	    assignmentName.setReadOnly(true);
	    left.add(assignmentName);
	    
	    assignmentIndex = new TextField<String>();  
	    assignmentIndex.setFieldLabel(myConstants.index()); 
	    assignmentIndex.setReadOnly(true);
	    right.add(assignmentIndex);
	    
	    assignmentOwner = new TextField<String>();  
	    assignmentOwner.setFieldLabel(myConstants.owner());  
	    assignmentOwner.setReadOnly(true);
	    left.add(assignmentOwner);
	    
	    assignmentBranch = new TextField<String>();  
	    assignmentBranch.setFieldLabel(myConstants.branch()); 
	    assignmentBranch.setReadOnly(true);
	    right.add(assignmentBranch);
	    
	    assignmentTimeline = new TextField<String>(); 
	    assignmentTimeline.setReadOnly(true);
	    assignmentTimeline.setFieldLabel(myConstants.timeline());  
	    right.add(assignmentTimeline);
	    
	    
	    main.add(left, new ColumnData(.5));  
	    main.add(right, new ColumnData(.5));  
	    
	    add(main);
	    
	    
	}
	
	
	public void updateData(Integer assignemntId,String sumString){
		this.sum = Integer.parseInt(sumString);
		this.assignemntId=assignemntId;
		assignmentService.getAssignmentDTOById(assignemntId,new AsyncCallback<AssignmentDTO>(){

			@Override
			public void onFailure(Throwable caught) {
                MessageBox.alert("Cannot find Assignemnt details,ask your support plz", caught.getMessage(), null);
				
			}

			@Override
			public void onSuccess(AssignmentDTO result) {
				assignmentName.setValue(result.getName());
				assignmentIndex.setValue(result.getIndex());
				assignmentOwner.setValue(result.getOwner().getSecondName()+ " " +result.getOwner().getFirstName());
				assignmentBranch.setValue(result.getBranch().getName());
				
			}
			
		});
	}
}
