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

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;

public class FootBarSheetPanel extends ContentPanel{
	private ProjectPanelSheetPanel projectPanelSheetPanel;
	private NotePanelSheetPanel notePanelSheetPanel;
	private UserDTO userDTO;
	private Integer assignmentId;
	private Date date;
	
	
	public FootBarSheetPanel(){
		
		this.setFrame(true);
		this.setHeaderVisible(false);
		this.setWidth(1200);
		this.setHeight(200);
		this.setLayout(new ColumnLayout());
	
		
		projectPanelSheetPanel = new ProjectPanelSheetPanel();
		add(projectPanelSheetPanel);
		notePanelSheetPanel = new NotePanelSheetPanel();
		add(notePanelSheetPanel);
		
	}
	
	public void updateData(UserDTO newUserDTO, Date newDate,Integer newAssignmentId,Integer hour,String sumString,Boolean leaf,Integer type){
		
		
				
		
		Integer[] types  = {0,1,2,3};
		if(Arrays.asList(types).contains(type)){
			if(newAssignmentId != assignmentId ){
				projectPanelSheetPanel.updateData(newAssignmentId,sumString);
				this.assignmentId=newAssignmentId;
				
			}
		}
			notePanelSheetPanel.updateData(hour,newUserDTO, newDate, newAssignmentId,leaf,type);
			
			this.userDTO=newUserDTO;
			this.date=newDate;
			
		
		
		
		
		
	}

}
