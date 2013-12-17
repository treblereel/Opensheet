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
package org.opensheet.client.widges.admin.windows;

import java.util.List;

import org.opensheet.client.dto.BranchDTO;
import org.opensheet.client.dto.DepartmentDTO;
import org.opensheet.client.dto.UserDTO;
import org.opensheet.client.services.DepartmentService;
import org.opensheet.client.services.DepartmentServiceAsync;
import org.opensheet.client.services.UserService;
import org.opensheet.client.services.UserServiceAsync;
import org.opensheet.client.widges.BranchComboBox;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
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
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class addDepartmentWindow extends Window{

	private TextField<String> name;
	private CheckBox  statusBox;
	private ComboBox<BeanModel>  ownerComboBox;
	private BranchComboBox branchComboBox;
	private DateField finish;
	private TextArea noteTextArea;
	private String usersRpcCriteria = "1";
	private ListLoader<?> Userloader;
	private UserServiceAsync 		userService       = GWT.create(UserService.class);
	private ListLoader<?> loader;
	private Grid<BeanModel> grid;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public addDepartmentWindow(Grid grid){
		   this.grid = grid;
		   
		   RpcProxy<List<UserDTO>> UserProxy = new RpcProxy<List<UserDTO>>() {
               @Override
               protected void load(Object loadConfig, AsyncCallback<List<UserDTO>> callback) {
               	userService.getUsersByRole("dm",callback);
               }
           };
       
      

       Userloader = new BaseListLoader<ListLoadResult<ModelData>>(UserProxy,new BeanModelReader());
       final ListStore<BeanModel> UserStore = new ListStore<BeanModel>(Userloader);
       UserStore.setMonitorChanges(true);
       Userloader.load();
	    
       setSize(360, 440);  
	   setPlain(true);  
	   setModal(true);  
	   setBlinkModal(true);  
	   setHeadingHtml("Add new Department");  
	   setLayout(new FitLayout());
		    
	   FormPanel panel = new FormPanel();
	   panel.setHeaderVisible(false); 
	        
	   name = new TextField<String>();
	   name.setName("name");
	   name.setAllowBlank(false);
	   name.setAutoValidate(true);
	   name.setFieldLabel("Name"); 
	   panel.add(name);
	           
	   statusBox = new CheckBox();
	   statusBox.setName("status");
	   statusBox.setFieldLabel("Status");
	   statusBox.setValue(true);
	   panel.add(statusBox);
	       
	   branchComboBox = new BranchComboBox(false);
	   panel.add(branchComboBox);
	   
	   ownerComboBox = new ComboBox<BeanModel>();
	   ownerComboBox.setDisplayField("fullName");  
	   ownerComboBox.setWidth(150);
	   ownerComboBox.setName("owner");
	   ownerComboBox.setFieldLabel("Owner");
	   ownerComboBox.setAllowBlank(false);
	   ownerComboBox.setEditable(false);
	   ownerComboBox.setStore(UserStore);  
	   ownerComboBox.setTypeAhead(true);  
	   ownerComboBox.setValue(UserStore.getAt(0));
	   ownerComboBox.setTriggerAction(TriggerAction.ALL);  
	   panel.add(ownerComboBox);
	           
	           
	           
	   finish = new DateField();
	   finish.setName("finished");
	   finish.setFieldLabel("Finish"); 
	   panel.add(finish);
	           
	   noteTextArea = new TextArea();  
	   noteTextArea.setPreventScrollbars(true);  
	   noteTextArea.setFieldLabel("Description");
	   noteTextArea.setName("note");
	   noteTextArea.setMaxLength(450);
	   panel.add(noteTextArea);
	           
	           
	   panel.addButton(new Button("Add Department", new SelectionListener<ButtonEvent>() {  
	   @Override  
	   public void componentSelected(ButtonEvent ce) {  
		 if( name.isValid() && ownerComboBox.isValid() && branchComboBox.isValid()){
		        		addDepartment();
		        	}
	   	        }
	     }));   
	          
	    panel.addButton(new Button("Cancel", new SelectionListener<ButtonEvent>() {  
		    @Override  
		     public void componentSelected(ButtonEvent ce) {  
			        	hide();
		   	        }
		}));  
		   
	    add(panel);    
	    show();
	           
	   }

	   
	   private void addDepartment(){
		   
		   DepartmentDTO departmentDTO = new DepartmentDTO();
		   departmentDTO.setName(name.getValue().toString());
		   departmentDTO.setBranch((BranchDTO)branchComboBox.getValue().getBean());
		   if(statusBox.getValue() != true){
			   departmentDTO.setStatus(false);
		      }else{
		    	  departmentDTO.setStatus(true); 
		      }

		   departmentDTO.setOwner((UserDTO) ownerComboBox.getValue().getBean()); 
		   departmentDTO.setFinished(finish.getValue());
		   
		   if(noteTextArea.getValue() == null){
			   departmentDTO.setNote(null);
		      }	  else	  {
				  departmentDTO.setNote(noteTextArea.getValue().toString());
		      }
		   
	         
 
   		  DepartmentServiceAsync departmentService = GWT.create(DepartmentService.class);
   		  
		   
		   departmentService.addDepartment(departmentDTO, new AsyncCallback<Void>() {
	           @Override
	           public void onFailure(Throwable caught) {
	               MessageBox.alert("Cannot update department", caught.getMessage(), null);
	           }
	           @Override
	           public void onSuccess(Void result) {
	               hide();
	               
	               grid.getStore().getLoader().load();
	           }
	       });
		   
		   
	   }
	    
	   
	
}
