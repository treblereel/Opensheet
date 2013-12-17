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
package org.opensheet.client.widges.project;

import java.util.List;

import org.opensheet.client.dto.AssignmentDTO;
import org.opensheet.client.dto.BranchDTO;
import org.opensheet.client.exceptions.ParentTimelineIsNullException;
import org.opensheet.client.exceptions.ParentTimelineTooSmallException;
import org.opensheet.client.exceptions.TimelineSmallerWhanSumChildException;
import org.opensheet.client.services.AssignmentService;
import org.opensheet.client.services.AssignmentServiceAsync;
import org.opensheet.client.services.StatService;
import org.opensheet.client.services.StatServiceAsync;
import org.opensheet.client.services.TimelineService;
import org.opensheet.client.services.TimelineServiceAsync;
import org.opensheet.client.utils.AssignmentTypes;
import org.opensheet.client.widges.BranchComboBox;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AssignmentDetailPanel extends FormPanel{
	final AssignmentServiceAsync assignmentService = GWT.create(AssignmentService.class);
	final TimelineServiceAsync timelineService = GWT.create(TimelineService.class);

	private ContentPanel parentPanel;
	private TextField<String> name,indexTextField;
	private ComboBox<AssignmentTypes> typeSimpleComboBox;
	private DateField start,end;
	private TextArea noteTextArea ;
	private NumberField assignmentHourSumField, timelineField,timelineOverTimeField,budgetField,overBudgetTimeField;
	private BranchComboBox branchComboBox;
	private Integer assignmentId;
	private AssignmentDTO assignment;
	private StatServiceAsync statServiceAsync = GWT.create(StatService.class);

	
	public AssignmentDetailPanel(ContentPanel contentPanel){
		this.parentPanel=contentPanel;
		
		setWidth("100%");
		setHeight(300);
		setFrame(true);
		setHeaderVisible(false);
		setPadding(0); 
		setBodyBorder(false);
		setLabelAlign(LabelAlign.TOP);  
		setButtonAlign(HorizontalAlignment.CENTER);  
		
		doDetailPanel();
	}

	
	private void doDetailPanel(){

		LayoutContainer main = new LayoutContainer();  
	    main.setLayout(new ColumnLayout()); 
	    
	    LayoutContainer left = new LayoutContainer();  
	    left.setStyleAttribute("paddingRight", "10px");  
	    FormLayout layout = new FormLayout();  
	    layout.setLabelAlign(LabelAlign.LEFT);  
	    left.setLayout(layout); 
	    
	    LayoutContainer rightContainer = new LayoutContainer();  
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
        name.setAllowBlank(false);
        name.setAutoValidate(true);
        name.setFieldLabel("Name");
		left.add(name);
	    
		indexTextField = new TextField<String>();
        indexTextField.setName("index");
        indexTextField.setAllowBlank(true);
        indexTextField.setFieldLabel("Index");
        center.add(indexTextField,new FormData(120, 22));
	    
        List<AssignmentTypes> list = AssignmentTypes.get();  
        final ListStore<AssignmentTypes>  typeStore = new ListStore<AssignmentTypes>();  
        typeStore.add(list);
        
        typeSimpleComboBox = new ComboBox<AssignmentTypes>();  
        typeSimpleComboBox.setTriggerAction(TriggerAction.ALL);  
        typeSimpleComboBox.setEditable(false);  
        typeSimpleComboBox.setWidth(100);
        typeSimpleComboBox.setDisplayField("name");
        typeSimpleComboBox.setName("name");
        typeSimpleComboBox.setFieldLabel("Type");
        typeSimpleComboBox.setStore(typeStore);
        typeSimpleComboBox.setAllowBlank(false);
        left.add(typeSimpleComboBox,new FormData(100, 22));
        
        
        start = new DateField();
        start.setName("start");
        start.setFieldLabel("Start"); 
        start.disable();
        left.add(start,new FormData(120, 22));
        
        end = new DateField();
        end.setName("end");
        end.setFieldLabel("Ends"); 
        end.disable();
        left.add(end,new FormData(120, 22));
        
        noteTextArea = new TextArea();  
        noteTextArea.setPreventScrollbars(true);  
        noteTextArea.setFieldLabel("Description");
        noteTextArea.setName("note");
        noteTextArea.setWidth(300);
        noteTextArea.setMaxLength(450);
   //     left.add(noteTextArea,new FormData("100%")); 
        
        assignmentHourSumField = new NumberField();
	    assignmentHourSumField.setFieldLabel("Hours Sum");
	    assignmentHourSumField.setEditable(false);
	    right.add(assignmentHourSumField,new FormData(100, 22));
	    
	    timelineField = new NumberField();
	    timelineField.setFieldLabel("Timeline");
	    timelineField.setEditable(true);
	    center.add(timelineField,new FormData(100, 22));
	    
	    timelineOverTimeField = new NumberField();
	    timelineOverTimeField.setFieldLabel("Over Time");
	    timelineOverTimeField.setEditable(false);
	    right.add(timelineOverTimeField,new FormData(100, 22));
	    
	    budgetField = new NumberField();
	    budgetField.setFieldLabel("Budget");
	    budgetField.setEditable(false);
	    center.add(budgetField,new FormData(100, 22));
	    
	    overBudgetTimeField = new NumberField();
	    overBudgetTimeField.setFieldLabel("Over budget");
	    overBudgetTimeField.setEditable(false);
	    right.add(overBudgetTimeField,new FormData(100, 22));
	    
        branchComboBox = new BranchComboBox(false);
	    left.add(branchComboBox,new FormData(100, 22));
	    
	    
	    
	    main.add(left,   new ColumnData(.5)); 
	    rightContainer.add(center, new ColumnData(.5)); 
	    rightContainer.add(right,  new ColumnData(3.5));
	    main.add(rightContainer, new ColumnData(.5));
	    
	    add(main);
	    
	    
	    Button save = new Button();
        save.setText("Save");
        save.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override	public void componentSelected(ButtonEvent ce) {
				if(name.isValid()){
					assignment.setName(name.getValue());
					assignment.setIndex(indexTextField.getValue());
					assignment.setType(typeSimpleComboBox.getValue().getId());
					assignment.setBranch(new BranchDTO(Integer.parseInt(branchComboBox.getValue().get("id").toString())));
					assignment.setTimeline(timelineField.getValue().intValue());
					updateAssignmentDetailsForm(assignment);
				}
			}
        	
        });
        
        Button reload = new Button();
        reload.setText("Reload");
        reload.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override	public void componentSelected(ButtonEvent ce) {
				loadAssignmentDetailsForm();
			}
        });
        
        setButtonAlign(HorizontalAlignment.CENTER);
        addButton(save);
        addButton(reload);
	}
	
	
	
	
	
	private void updateAssignmentDetailsForm(final AssignmentDTO assignmentDTO){
		assignmentService.updateAssignment(assignmentDTO,new AsyncCallback<BaseModel>(){
			@Override	public void onFailure(Throwable caught) {
				MessageBox.info("Can't update Assignment Details",caught.getMessage(),null);
			}
			@Override	public void onSuccess(BaseModel result) {
				if(result.get("result").equals("failed")){
	 	               MessageBox.alert("Somethings goes wrong", result.get("msg").toString(), null);
	        	}else{
	        		Info.display("Assignment", "updated");
	        	}
			}
			
		});
		
		timelineService.setAssignmentTimeline(assignmentId,assignmentDTO.getTimeline(), false,new  AsyncCallback<BaseModel>(){
			@Override	public void onFailure(Throwable caught) {
				if(caught instanceof ParentTimelineTooSmallException){
					timelineMessageBoxError(((ParentTimelineTooSmallException) caught).getMessage().toString(),assignmentId,assignmentDTO.getTimeline());
				}else if(caught instanceof ParentTimelineIsNullException){
					MessageBox.alert("ERROR",caught.getMessage().toString(), null);
				}else if(caught instanceof TimelineSmallerWhanSumChildException){
					MessageBox.alert("ERROR",caught.getMessage().toString(), null);
				}
				
			}
			@Override		public void onSuccess(BaseModel result) {
								Info.display("Timeline", "updated");
				}
				
			
			
		});
		
	}
	
	private void loadAssignmentDetailsForm(){
		clearPanel();
			assignmentService.getAssignmentDTOById(assignmentId, new AsyncCallback<AssignmentDTO>() {
			public void onFailure(Throwable caught) {
				MessageBox.info("Can't get Assignment Details",caught.getMessage(),null);
			}
				
			@Override	public void onSuccess(AssignmentDTO assignmentDTO) {
				assignment =  assignmentDTO;
		        name.setValue(assignmentDTO.getName());
			    indexTextField.setValue(assignmentDTO.getIndex());
		        typeSimpleComboBox.setValue(new AssignmentTypes(assignmentDTO.getType()));
		        start.setValue(assignmentDTO.getStarted());
		        end.setValue(assignmentDTO.getFinished());
		        noteTextArea.setValue(assignmentDTO.getNote());
		        branchComboBox.set(assignmentDTO.getBranch());
		     
		        if(assignmentDTO.getLevel() != 0){
		        	branchComboBox.disable();
		        	typeSimpleComboBox.disable();
		        }else{
		        	branchComboBox.enable();
		        	typeSimpleComboBox.enable();
		        }

		        
			}
		});
	
			statServiceAsync.getAssignmentQuickDetails(assignmentId,new AsyncCallback<BaseModel>(){
				@Override	public void onFailure(Throwable caught) {
					MessageBox.info("Can't get Assignment Stats",caught.getMessage(),null);

				}

				@Override	public void onSuccess(BaseModel result) {
					int overtime = (Integer) Integer.parseInt(result.get("overtime").toString());
					if(overtime > 0){
						overtime =0;
					}
					assignmentHourSumField.setValue((Integer) Integer.parseInt(result.get("hours").toString()));
					timelineField.setValue((Integer) Integer.parseInt(result.get("timeline").toString()));
					timelineOverTimeField.setValue(overtime);
					budgetField.setValue((Integer) Integer.parseInt(result.get("budget").toString()));
					overBudgetTimeField.setValue((Integer) Integer.parseInt(result.get("overbudget").toString()));
					if(overtime < 0)
						timelineOverTimeField.setInputStyleAttribute("color", "red");
				}
			});
		}
	
	private void clearPanel(){
		this.clear();
	}
	
	public void setData(Integer assignmentId){
		this.assignmentId=assignmentId;
		loadAssignmentDetailsForm();
		
	}
	
	private void timelineMessageBoxError(String msg,Integer assignment,final Integer timeline){
		final Window box = new Window();
		box.setHeight(300);
		box.setHeadingHtml("Error");
		box.setModal(true);
		box.add(new Label(msg));
		box.setTitle("ERROR");
		box.setClosable(false);
		
		Button yes = new Button("Yes");
		yes.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override	public void componentSelected(ButtonEvent ce) {
					timelineService.setAssignmentTimeline(assignmentId,timeline, true,new  AsyncCallback<BaseModel>(){
						@Override	public void onFailure(Throwable caught) {
								MessageBox.info("ERROR",caught.getMessage(),null);
						
						}
						@Override	public void onSuccess(BaseModel result) {
							Info.display("Ok", "updated");	
							box.hide();
						}
				});
			}
		});
		
		Button no = new Button("No"); 
		no.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override	public void componentSelected(ButtonEvent ce) {
				box.hide();
			}
		});
		box.addButton(yes);
		box.addButton(no);
		box.show();
	}
	
}
