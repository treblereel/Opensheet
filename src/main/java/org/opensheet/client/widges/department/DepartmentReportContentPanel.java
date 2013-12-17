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

import java.util.Date;

import org.opensheet.client.utils.Resources;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.DatePickerEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class DepartmentReportContentPanel extends ContentPanel{
	private Date starting;
	private Date finishing;
	private DateField startingDateField;
	private DateField finishingDateField;
	private Integer departmentId;
	
	
	
	@SuppressWarnings("deprecation")
	public DepartmentReportContentPanel(){
		setFrame(true);
		setWidth(724);
		setHeight(600);
		setHeadingHtml("Department Users: ");
	
		
		starting = new Date();
		starting.setDate(1);
	    finishing = new Date();
		

		add(AddReportPanel());
	}
	
	
	private FieldSet AddReportPanel(){
		FieldSet fieldSet = new FieldSet();  
	    fieldSet.setHeadingHtml("Department Month Report");  
	    
	  
	    FormLayout layout = new FormLayout();  
	    layout.setLabelWidth(75);  
	    fieldSet.setLayout(layout);  
		
	    
	    
		startingDateField = new DateField();  
		startingDateField.setAllowBlank(false);
		startingDateField.setValue(starting);
		startingDateField.setFieldLabel("Starting");
		fieldSet.add(startingDateField);
		
		finishingDateField = new DateField();  
		finishingDateField.setAllowBlank(false);
		finishingDateField.setValue(finishing);
		finishingDateField.setFieldLabel("Finishing");
		fieldSet.add(finishingDateField);
		
		
		Button export = new Button("Xls export");
		export.setIcon(Resources.ICONS.table()); 
		export.addListener(Events.Select, new Listener<BaseEvent>(){
			@SuppressWarnings("deprecation")
			@Override public void handleEvent(BaseEvent be) {
				if(departmentId == null){
					MessageBox.info("ERORR","Choose Department first",null);
				}else{
				
				Date start  = startingDateField.getValue();
				Date end = finishingDateField.getValue();
				Window.Location.assign(GWT.getHostPageBaseURL().toString() +"quickdepartmentreportbyassignmentandbyuser.htm?department="+
						departmentId+"&s_year="+start.getYear()+"&s_month="+start.getMonth()+
						"&s_day="+start.getDate()+"&e_year="+end.getYear()+"&e_month="
						+end.getMonth()+"&e_day="+end.getDate());
				}		
			}
	    });
		
		fieldSet.add(export);
		
		return fieldSet;
	}

	public void setDepartment(Integer id){
		this.departmentId = id;
	}
	
}
