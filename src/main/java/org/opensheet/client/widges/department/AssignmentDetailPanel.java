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
package org.opensheet.client.widges.department;

import org.opensheet.client.services.StatService;
import org.opensheet.client.services.StatServiceAsync;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AssignmentDetailPanel extends ContentPanel{
	private TextField<String> name,owner,indexTextField;
	private NumberField assignmentHourSumField,assignmentHourSumThisMonthField, timelineField,timelineOverTimeField,budgetField,overBudgetTimeField;
	private NumberField intrarateField,extrarateField,intrarateCurrentMonthField,extrarateCurrentMonthField;
	
	private Integer assignmentId,departmentId;
	private StatServiceAsync statService = GWT.create(StatService.class);
	private FormPanel fp;
	
	

	public AssignmentDetailPanel(){
		
		setFrame(true);
		setWidth(724);
		setHeight(600);
		setLayout(new ColumnLayout());
		add(detailPanel());
	}
	
	private FormPanel detailPanel(){
		fp =  new FormPanel();
		fp.setWidth(724);
		fp.setHeight(500);
		fp.setHeaderVisible(false);
		fp.setLayout(new ColumnLayout());
		
		
		FormPanel cp = new FormPanel();
		cp.setHeaderVisible(false);
		cp.setHeight(500);
		cp.setWidth(250);
		
		
		
		
		LayoutContainer main = new LayoutContainer();  
	    main.setLayout(new ColumnLayout()); 
	    
	    LayoutContainer left = new LayoutContainer();  
	    left.setStyleAttribute("paddingRight", "10px");  
	    left.setWidth(150);
	    FormLayout layout = new FormLayout();  
	    layout.setLabelAlign(LabelAlign.LEFT);  
	    left.setLayout(layout); 
	    
	    LayoutContainer rightContainer = new LayoutContainer(); 
	    rightContainer.setWidth(400);
	    rightContainer.setLayout(new ColumnLayout());
	    
	    
	    LayoutContainer center = new LayoutContainer();  
	    center.setStyleAttribute("paddingLeft", "10px");  
	    layout = new FormLayout();  
	    layout.setLabelAlign(LabelAlign.TOP);  
	    center.setLayout(layout);  
	    
	    LayoutContainer right = new LayoutContainer();  
	    right.setStyleAttribute("paddingLeft", "10px");  
	    layout = new FormLayout();  
	    layout.setLabelAlign(LabelAlign.TOP);  
	    right.setLayout(layout);  
	    
	    
	    name = new TextField<String>();
        name.setName("Name");
        name.setReadOnly(true);
        name.setFieldLabel("Name");
		cp.add(name,new FormData(180, 22));
		
		owner = new TextField<String>();
        owner.setName("Owner");
        owner.setReadOnly(true);
        owner.setFieldLabel("Owner");
		cp.add(owner,new FormData(180, 22));
		
		
		
		assignmentHourSumField = new NumberField();
	    assignmentHourSumField.setFieldLabel("Hours Sum");
	    assignmentHourSumField.setEditable(false);
	    right.add(assignmentHourSumField,new FormData(100, 22));
	    
	    
	    assignmentHourSumThisMonthField = new NumberField();
	    assignmentHourSumThisMonthField.setFieldLabel("Hours Sum this Month");
	    assignmentHourSumThisMonthField.setEditable(false);
	    right.add(assignmentHourSumThisMonthField,new FormData(100, 22));
	    
	    timelineField = new NumberField();
	    timelineField.setFieldLabel("Timeline");
	    timelineField.setEditable(true);
	    center.add(timelineField,new FormData(100, 22));
	    
	    timelineOverTimeField = new NumberField();
	    timelineOverTimeField.setFieldLabel("Over Time");
	    timelineOverTimeField.setEditable(false);
	    center.add(timelineOverTimeField,new FormData(100, 22));
	    

	    
	    intrarateField = new NumberField();
	    intrarateField.setFieldLabel("Sum of Internal rates");
	    intrarateField.setEditable(false);
	    center.add(intrarateField,new FormData(100, 22));
	    
	    extrarateField = new NumberField();
	    extrarateField.setFieldLabel("Sum of External rates this month");
	    extrarateField.setEditable(false);
	    center.add(extrarateField,new FormData(100, 22));
	    
	    intrarateCurrentMonthField = new NumberField();
	    intrarateCurrentMonthField.setFieldLabel("Sum of Internal rates, this month");
	    intrarateField.setEditable(false);
	    right.add(intrarateCurrentMonthField,new FormData(100, 22));
	    
	    extrarateCurrentMonthField = new NumberField();
	    extrarateCurrentMonthField.setFieldLabel("Sum of External rates, this month");
	    extrarateCurrentMonthField.setEditable(false);
	    right.add(extrarateCurrentMonthField,new FormData(100, 22));
	    
	    
	    
	    
	    
	    
	    budgetField = new NumberField();
	    budgetField.setFieldLabel("Budget");
	    budgetField.setEditable(false);
	    center.add(budgetField,new FormData(100, 22));
	    
	    overBudgetTimeField = new NumberField();
	    overBudgetTimeField.setFieldLabel("Over budget");
	    overBudgetTimeField.setEditable(false);
	    right.add(overBudgetTimeField,new FormData(100, 22));
	  //  main.add(left,   new ColumnData(.5)); 
	    main.add(center, new ColumnData(.5)); 
	    main.add(right,  new ColumnData(3.5));
//	    main.add(rightContainer, new ColumnData(.5));
	    
	    fp.add(cp);
	    fp.add(main);
		
		
		return fp;
	}
	
	public void setData(Integer assignmentId,Integer departmentId){
		fp.clear();
		statService.getAssignmentDepartmentDetail(assignmentId, departmentId, new AsyncCallback<BaseModel>(){
			@Override	public void onFailure(Throwable caught) {
					MessageBox.info("Error",caught.getMessage(),null);				
			}
			@Override
			public void onSuccess(BaseModel result) {
				name.setValue(result.get("name").toString());
				owner.setValue(result.get("owner").toString());
				
				intrarateField.setValue(Integer.parseInt(result.get("intratesum").toString()));
				extrarateField.setValue(Integer.parseInt(result.get("intratesumMonth").toString()));
				intrarateCurrentMonthField.setValue(Integer.parseInt(result.get("extraratesum").toString()));
				extrarateCurrentMonthField.setValue(Integer.parseInt(result.get("extratesumMonth").toString()));
				
				Integer timeline = Integer.parseInt(result.get("timeline").toString());
				Integer sum = Integer.parseInt(result.get("sum").toString());
				Integer sumMonth = Integer.parseInt(result.get("sum_month").toString());

				timelineField.setValue(timeline);
				assignmentHourSumField.setValue(sum);
				assignmentHourSumThisMonthField.setValue(sumMonth);
				
				if(sum > timeline){
					Integer overTime = sum - timeline;
					timelineOverTimeField.setValue(overTime);
				}

			}
			
			
		});
		
	}
	
	
	
}
