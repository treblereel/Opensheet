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
package org.opensheet.client.widges.windows;

import java.util.Date;
import java.util.List;

import org.opensheet.client.dto.AssignmentDTO;
import org.opensheet.client.dto.UserDTO;
import org.opensheet.client.services.AssignmentService;
import org.opensheet.client.services.AssignmentServiceAsync;
import org.opensheet.client.services.UserService;
import org.opensheet.client.services.UserServiceAsync;
import org.opensheet.client.utils.AssignmentTypes;
import org.opensheet.client.widges.BranchComboBox;
import org.opensheet.client.widges.Reloadable;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * addAssignmentWindow -- Add new Assignment Window
 *   root -- true/false: 
 *   false -- its a task, we can't change Assignment type 
 *   true  --  its a new Assignment, we can change type
 * 
 */

public class AddAssignmentWindow extends Window{
	
	private FormPanel addAssignmentPanel;
	private Reloadable panel;
	private CheckBox  newbyDefaultCheckBoxAssignment;
	private ComboBox<BeanModel> newAssignmentOwnerComboBox;
	private ListStore<BeanModel> userStore = null;
	private ListLoader<?> 		 userloader;
	private Integer parentId;
	private Boolean root;
	private UserServiceAsync 		userService       = GWT.create(UserService.class);
	private AssignmentServiceAsync assigmentService = GWT.create(AssignmentService.class);
	private BranchComboBox branchComboBox;
	private final ComboBox<AssignmentTypes> typeCombo;
	private final TextField<String> index;
	
		public AddAssignmentWindow(final Boolean root,final Integer parentId,Reloadable panel){
			this.root = root;
			this.parentId = parentId;
			this.panel = panel;
			
			
			
			
		    setSize(360, 440);  
		    setPlain(true);  
		    setModal(true);  
		    setBlinkModal(true);
		    if(root == true){
		    	setHeadingHtml("Add new Assignment2");  
		    }else{
			   setHeadingHtml("Add new Task");  
		    }
		    setLayout(new FitLayout());
			
			
		    addAssignmentPanel = new FormPanel();
		    addAssignmentPanel.setHeaderVisible(false);  
		
		    
		    final TextField<String> name = new TextField<String>();
		    name.setAllowBlank(false);
		    name.setAutoValidate(true);
		    name.setFieldLabel("Name"); 
		    addAssignmentPanel.add(name);
		        
		        
		    List<AssignmentTypes> list = AssignmentTypes.get();  
		    final ListStore<AssignmentTypes>  store = new ListStore<AssignmentTypes>();  
		    store.add(list);  
		        
		    typeCombo = new ComboBox<AssignmentTypes>(); 
		        
		    if(root != true){
		    	typeCombo.disable();
		    	
		    }
		        
		        
		    typeCombo.setTriggerAction(TriggerAction.ALL);  
		    typeCombo.setEditable(false);  
		    typeCombo.setWidth(100);
		    typeCombo.setStore(store);
		    typeCombo.setDisplayField("name");
		//    typeCombo.setValue(new AssignmentTypes(assignmentType));
		    typeCombo.setFieldLabel("Type");
		    typeCombo.setAllowBlank(false);
		    typeCombo.setAutoValidate(true);
		    typeCombo.addSelectionChangedListener(new SelectionChangedListener<AssignmentTypes>(){
		    	@Override	public void selectionChanged( SelectionChangedEvent<AssignmentTypes> se) {
		    		getIndex();					
				}
				
		    	
		    });
		        
		    index = new TextField<String>();
		    index.setAllowBlank(true);
		    index.setFieldLabel("Index"); 
		    addAssignmentPanel.add(index);
		        
		    newbyDefaultCheckBoxAssignment = new CheckBox();
		    newbyDefaultCheckBoxAssignment.setFieldLabel("is Default ?");
		    newbyDefaultCheckBoxAssignment.setName("name");
		    addAssignmentPanel.add(newbyDefaultCheckBoxAssignment);
		        
		    branchComboBox = new BranchComboBox(false);
		    branchComboBox.setFieldLabel("Branch");
		    branchComboBox.addSelectionChangedListener(new SelectionChangedListener<BeanModel>(){
				@Override	public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
					getIndex();
				}
		    });
		    		
			newAssignmentOwnerComboBox = new ComboBox<BeanModel>();
			newAssignmentOwnerComboBox.setDisplayField("fullName");  
			newAssignmentOwnerComboBox.setWidth(150);
			newAssignmentOwnerComboBox.setName("owner");
			newAssignmentOwnerComboBox.setFieldLabel("Owner");
			newAssignmentOwnerComboBox.setAllowBlank(false);
			newAssignmentOwnerComboBox.setEditable(false);
			newAssignmentOwnerComboBox.setStore(userStore);  
			newAssignmentOwnerComboBox.setTypeAhead(true);  
			newAssignmentOwnerComboBox.setTriggerAction(TriggerAction.ALL);  
			newAssignmentOwnerComboBox.addListener(Events.BeforeRender,new Listener<BaseEvent>(){
			    	@Override	public void handleEvent(BaseEvent be) {
							if(userStore != null){
								newAssignmentOwnerComboBox.setStore(userStore);
							}else{
								newAssignmentOwnerComboBox.setStore(getProjectManagerStore());	
							}
							
						}
			        	
			        });
			        
			     if(root == true){ 
			        addAssignmentPanel.add(newAssignmentOwnerComboBox);
			        addAssignmentPanel.add(branchComboBox);
				    addAssignmentPanel.add(typeCombo);
			     }else{
			    	 getIndex();
			     }
		        
		        
		        
		    DateField startA = new DateField();
		    startA.setValue(new Date());
		    startA.setFieldLabel("Start"); 
		    startA.disable();
		    addAssignmentPanel.add(startA);
		        

		    final DateField finished = new DateField();
		    finished.setFieldLabel("Finish");
		    finished.setAllowBlank(true);
		    addAssignmentPanel.add(finished);
		       
		    final TextArea note = new TextArea();  
		    note.setPreventScrollbars(true);  
		    note.setFieldLabel("Description");
		    note.setMaxLength(450);
		    addAssignmentPanel.add(note); 
		        
		    
		    addAssignmentPanel.addButton(new Button("Add Assignment/Task", new SelectionListener<ButtonEvent>() {  
		        @Override  
		        public void componentSelected(ButtonEvent ce) {  
		        
		        	if( name.isValid() == true){
		        		
		        		AssignmentDTO a = new AssignmentDTO();
		        		a.setName(name.getValue());
		        		a.setIndex(index.getValue());
		        		a.setFinished(finished.getValue());
		        		a.setNote(note.getValue());
		        		
		        		if(newbyDefaultCheckBoxAssignment.getValue() == true){
		        			a.setByDefault(true);	
		        		}else{
		        			a.setByDefault(false);
		        		}
		        		if(root == true){
		        			a.setType(typeCombo.getValue().getId());
		        			a.setLevel(0);
			        		a.setOwner((UserDTO) newAssignmentOwnerComboBox.getValue().getBean());
			        		a.setBranch(branchComboBox.get());

		        		}else{
		        			AssignmentDTO parent = new AssignmentDTO();
		        			parent.setId(parentId);
		        			a.setParent(parent);	
		        			a.setLevel(1);
		        		}
		        		
		        		addAssignment(a);
		        	hide();	
		        }
		        }  
		      }));
		    
		        
		    
			 addAssignmentPanel.addButton(new Button("Cancel", new SelectionListener<ButtonEvent>() {  
			        @Override  
			        public void componentSelected(ButtonEvent ce) {  
			        	hide();  
			        }  
			 }));
		
		 add(addAssignmentPanel);
		 show();
    	}
		
		
		private ListStore<BeanModel> getProjectManagerStore(){
			
			RpcProxy<List<UserDTO>> UserProxy = new RpcProxy<List<UserDTO>>() {
	            @Override
	            protected void load(Object loadConfig, AsyncCallback<List<UserDTO>> callback) {
	            	userService.getUsersByRole("pm",callback);
	            }
	        };
	        
	        
		    
		    userloader = new BaseListLoader<ListLoadResult<ModelData>>(UserProxy,new BeanModelReader());
		    final ListStore<BeanModel> userStore = new ListStore<BeanModel>(userloader);
		    userStore.setMonitorChanges(true);
		    userloader.load();
			return userStore;
		}
		
		
	private void addAssignment(AssignmentDTO assignmentDTO){
			assigmentService.addAssignment(assignmentDTO, new AsyncCallback<Void>() {
		           @Override
		           public void onFailure(Throwable caught) {
		               MessageBox.alert("Cannot add Assignmnet", caught.getMessage(), null);
		           }
		           @Override
		           public void onSuccess(Void result) {
		        	   panel.reload();
	           }
			});
		
		}	
		
	private void getIndex(){
		Integer type  = null;
		Integer branch = null;
		if(root == true){
			type = typeCombo.getValue().getId();
			branch =  Integer.parseInt(branchComboBox.getValue().get("id").toString());
		}
		
		
		if((type != null && branch!=null) || (root == false)){
			assigmentService.generateIndexForNewAssignment(type, branch, parentId,new AsyncCallback<String>(){
				@Override	public void onFailure(Throwable caught) {
					MessageBox.info("Error, can't generate Index",caught.getMessage(),null);
				}
				@Override	public void onSuccess(String result) {
						index.setValue(result);					
				}
				
			});
		}
		
	}
		

}
