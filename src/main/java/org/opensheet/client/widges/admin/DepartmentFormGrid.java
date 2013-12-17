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
package org.opensheet.client.widges.admin;



import java.util.ArrayList;
import java.util.List;

import org.opensheet.client.dto.DepartmentDTO;
import org.opensheet.client.dto.UserDTO;

import org.opensheet.client.services.DepartmentService;
import org.opensheet.client.services.DepartmentServiceAsync;
import org.opensheet.client.services.UserService;
import org.opensheet.client.services.UserServiceAsync;
import org.opensheet.client.widges.BranchComboBox;
import org.opensheet.client.widges.admin.windows.addDepartmentWindow;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binding.FormBinding;
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
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DepartmentFormGrid extends ContentPanel{
	
	 private FormPanel panel;
	 private FormBinding formBindings;
	 private Integer selectedBranch = 1;

	 private TextField<String> note,name,owner;
	 private NumberField  id;
	 private DateField start,finish,updated;
	 private Boolean status;
	 private CheckBox statusBox; 
	 private ListStore<BeanModel> store;
	 private ComboBox<BeanModel> ownerComboBox;
	 private String usersRpcCriteria = "1";
	 private String departmentRpcCriteria = "1";
	 private ListStore<BeanModel> userStore;
	 private ListLoader<?> loader;
	 private ListLoader<?> userloader;
	 private TextArea noteTextArea;
	 private BranchComboBox branchComboBox,newUserBranchComboBox,toolBarBranchComboBox;

	 private DepartmentServiceAsync departmentService = GWT.create(DepartmentService.class);
	 private UserServiceAsync 		userService       = GWT.create(UserService.class);

		
	 
	 @SuppressWarnings({ "deprecation", "rawtypes" })
	public DepartmentFormGrid(){
		 
	            RpcProxy<List<DepartmentDTO>> proxy = new RpcProxy<List<DepartmentDTO>>() {
	                @Override
	                protected void load(Object loadConfig, AsyncCallback<List<DepartmentDTO>> callback) {
	                	departmentService.getDepartmentsByBranch(departmentRpcCriteria,selectedBranch,callback);
	                }
	            };
	            
	     
	            
	        
	        
	        this.loader = new BaseListLoader<ListLoadResult<ModelData>>(proxy,new BeanModelReader());
	        final ListStore<BeanModel> store = new ListStore<BeanModel>(this.loader);
	        store.setMonitorChanges(true);
	        loader.load();

	 
	   List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
	   
	    columns.add(new ColumnConfig("name", "Department", 120));
	    columns.add(new ColumnConfig("owner.fullName", "Owner", 200));
	    columns.add(new ColumnConfig("status", "Status", 200));
	    
	    ColumnConfig  column = new ColumnConfig("started", "Started", 100);  
	    column.setAlignment(HorizontalAlignment.RIGHT);  
	    column.setDateTimeFormat(DateTimeFormat.getShortDateFormat()); 
	    
	    columns.add(column);
	    
	    ColumnModel cm = new ColumnModel(columns);
   
	    final Grid<BeanModel> grid = new Grid<BeanModel>(store, cm);

	    
	    
	   grid.setLoadMask(true);
       grid.setWidth(800);
       grid.setHeight(200);
       
       grid.addStyleName(".my-table-style");
       grid.setBorders(true);
       grid.setAutoExpandColumn("name");
       grid.getView().setEmptyText("no data");
       grid.setId("myid");
       
       
       
//     setHeading("Departments");  
       setFrame(true);  
       setSize(800, 400);  
       setLayout(new RowLayout(Orientation.HORIZONTAL));
       
       ToolBar toolBar = new ToolBar();  
       toolBar.add(new LabelToolItem("Department Mode: "));
       
       final SimpleComboBox<String> type = new SimpleComboBox<String>();  
       type.setTriggerAction(TriggerAction.ALL);  
       type.setEditable(false);  
       type.setFireChangeEventOnSetValue(true);  
       type.setWidth(100);  
       type.add("Active");  
       type.add("Killed in Action");
       type.add("EveryBody");
       type.setSimpleValue("EveryBody");
       
       toolBar.add(type);
       type.addListener(Events.Change, new Listener<FieldEvent>() {
    	   		@Override
    	      public void handleEvent(FieldEvent be) { 
    	   			if(type.getSimpleValue().equals("EveryBody")){
    	   				departmentRpcCriteria = "any";
    	   			}else if(type.getSimpleValue().equals("Active")){
    	   				departmentRpcCriteria = "1";
    	   			}else if(type.getSimpleValue().equals("Killed in Action")){
    	   				departmentRpcCriteria = "0";
    	   			}
    	   			grid.getStore().getLoader().load();
    	      }  
    	    }); 
       
       toolBar.add(new SeparatorToolItem());  
       
       toolBar.add(new LabelToolItem("Branch: "));
       toolBarBranchComboBox =  new BranchComboBox(true);
       toolBarBranchComboBox.setEmptyText("Default");
       toolBarBranchComboBox.addListener(Events.Select, new Listener<FieldEvent>(){
		@Override
		public void handleEvent(FieldEvent be) {
			selectedBranch = Integer.parseInt(toolBarBranchComboBox.getValue().get("id").toString());
			grid.getStore().getLoader().load();
		}
    	   
       });
       
       toolBar.add(toolBarBranchComboBox);
       
       final BoxComponent spaceItem = new BoxComponent(){
	       @Override
	       protected void onRender(final Element target, final int index)
	       	{
	    	   this.setElement(DOM.createDiv(), target, index);
	       	}
	       };
       spaceItem.setWidth(20);
       toolBar.add(spaceItem);
       
       toolBar.add(new SeparatorToolItem());
       
       Button addDepartmnetButton = new Button("Add Department", new SelectionListener<ButtonEvent>() {  
    	      @Override  
    	      public void componentSelected(ButtonEvent ce) {  
    	    	   new addDepartmentWindow(grid);
    	      }  
    	    });  
       addDepartmnetButton.setIconStyle("icon-user");
    
       toolBar.add(addDepartmnetButton);
       setTopComponent(toolBar); 
       setHeaderVisible(false); 
       
      
       
       grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);  
       grid.getSelectionModel().addListener(Events.SelectionChange,  
           new Listener<SelectionChangedEvent<BeanModel>>() {  
             public void handleEvent(SelectionChangedEvent<BeanModel> be) { 
              	  
               if (be.getSelection().size() > 0) {  
                 formBindings.bind((ModelData) be.getSelection().get(0)); 
                 
               } else {  
                 formBindings.unbind();  
                 
               }  
             }  
           });  
       
       
       
       FormPanel panel = createForm();  
       formBindings = new FormBinding(panel, true);  
       formBindings.setStore((Store) grid.getStore());
       
       
       add(grid, new RowData(.6, 1));
       add(panel, new RowData(.4, 1));
      
       
       
 
   }	
	
       private FormPanel createForm() {
    	   
    	   RpcProxy<List<UserDTO>> UserProxy = new RpcProxy<List<UserDTO>>() {
               @Override
               protected void load(Object loadConfig, AsyncCallback<List<UserDTO>> callback) {
               	userService.getUsersByRole("dm",callback);
               }
           };
      

	       userloader = new BaseListLoader<ListLoadResult<ModelData>>(UserProxy,new BeanModelReader());
	       userStore = new ListStore<BeanModel>(userloader);
	       userStore.setMonitorChanges(true);
	       userloader.load();
    	   
    	   
    	   
    	   panel = new FormPanel();
           panel.setHeaderVisible(false);  
         
           id = new NumberField();
           id.setName("id");
           id.hide();
           panel.add(id);

           name = new TextField<String>();
           name.setName("name");
           name.setAllowBlank(false);
           name.setAutoValidate(true);
           name.setFieldLabel("Name"); 
           panel.add(name);
           
           statusBox = new CheckBox();
           statusBox.setName("status");
           statusBox.setFieldLabel("Status");
           panel.add(statusBox);
           
           
           branchComboBox = new BranchComboBox(false);
           branchComboBox.setName("branch");
           branchComboBox.setDisplayField("name"); 
           panel.add(branchComboBox);
           
           
           
           ownerComboBox = new ComboBox<BeanModel>();
           ownerComboBox.setDisplayField("fullName");  
           ownerComboBox.setWidth(150);
           ownerComboBox.setName("owner");
           ownerComboBox.setFieldLabel("Owner");
           ownerComboBox.setAllowBlank(false);
           ownerComboBox.setEditable(false);
           ownerComboBox.setStore(userStore);  
           ownerComboBox.setTriggerAction(TriggerAction.ALL);
           panel.add(ownerComboBox);
           
           
           start = new DateField();
           start.setName("started");
           start.setFieldLabel("Start"); 
           start.disable();
           panel.add(start);

           updated = new DateField();
           updated.setName("updated");
           updated.setFieldLabel("Updated");
           updated.disable();
           panel.add(updated);
           
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
           
           
           Button button = new Button();
           button.setText("Send");
           panel.add(button);

           button.addListener(Events.OnClick, new Listener<BaseEvent>() {
               @Override
               public void handleEvent(BaseEvent be) {
            	if((Integer) id.getValue().intValue() == 1 && statusBox.getValue() == false ){
                    MessageBox.alert("Update Failed", "Default Department cant be disabled", null);
                    loader.load();
            	}else{
            		updateDepartment();
            	}
               	
               }
           });
           return panel; 
       }
       
       
     
   
   
   private void updateDepartment() {
       

	   DepartmentDTO departmentDTO = new DepartmentDTO();
	   departmentDTO.setId((Integer) id.getValue().intValue());
	   departmentDTO.setName(name.getValue().toString());
	   departmentDTO.setBranch(branchComboBox.get());
	   
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
	   
         
	   departmentService.updateDepartment(departmentDTO, new AsyncCallback<Void>() {
           @Override
           public void onFailure(Throwable caught) {
               MessageBox.alert("Cannot update department", caught.getMessage(), null);
           }
           @Override
           public void onSuccess(Void result) {
        	   Info.display("Ok!", "Department has been updated");
               loader.load();
           }
       });
       
       
   } 
   
 
   
   
}
