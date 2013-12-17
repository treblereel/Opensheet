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

import java.util.Arrays;
import java.util.List;

import org.opensheet.client.dto.AssignmentDTO;
import org.opensheet.client.dto.BranchDTO;
import org.opensheet.client.dto.UserDTO;
import org.opensheet.client.dto.grid.AssignmentGridTemplate;
import org.opensheet.client.dto.grid.Folder;
import org.opensheet.client.services.AssignmentService;
import org.opensheet.client.services.AssignmentServiceAsync;
import org.opensheet.client.services.UserService;
import org.opensheet.client.services.UserServiceAsync;
import org.opensheet.client.utils.AssignmentTypes;
import org.opensheet.client.utils.Resources;
import org.opensheet.client.widges.BranchComboBox;
import org.opensheet.client.widges.Reloadable;
import org.opensheet.client.widges.windows.AddAssignmentWindow;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;


public class AssignmentTreeGrid extends ContentPanel implements Reloadable{
	private FormPanel panel;
	private NumberField IdNumberField;
	private TextField<String> nameTextField,indexTextField;
	private TextArea noteTextArea;
	private DateField start,finish,updated;
	private ComboBox<AssignmentTypes> typeSimpleComboBox,typeComboBox;
	private ComboBox<BeanModel> ownerComboBox;
	private CheckBox statusAssignment,byDefaultCheckBoxAssignment;
	private BranchComboBox branchComboBox,toolBarBranchComboBox;
	private Integer branch = 1;
	private Integer assignmentType = 0;
	private Boolean assignmentStatus = true;
	private Integer selectedAssignment;
	private Button statusAssignmentButton;
	public  Folder folder,model;
	
	public TreeStore<ModelData> store;
	public TreeGrid<ModelData> tree;
	
	private ListStore<BeanModel> userStore = null;
	private ListLoader<?> 		 userloader;
	
	private AssignmentServiceAsync assigmentService = GWT.create(AssignmentService.class);
	private UserServiceAsync 		userService       = GWT.create(UserService.class);

	

	public AssignmentTreeGrid(){

	    setHeaderVisible(false);
		setLayout(new FlowLayout(10));  
	    store = new TreeStore<ModelData>();
	    
	    ColumnConfig name = new ColumnConfig("name", "Name", 100);
	    name.setRenderer(new TreeGridCellRenderer<ModelData>());  
	    ColumnConfig index = new ColumnConfig("assignmentIndex", "Index", 100);
	    index.setSortable(false);
	    index.setFixed(true);
	    index.setMenuDisabled(true);
	    ColumnConfig status = new ColumnConfig("status", "Status", 100);  
	    ColumnModel cm = new ColumnModel(Arrays.asList(name, index, status));  
	  
 
	  
	    tree = new TreeGrid<ModelData>(store, cm);  
	    tree.setBorders(true);  
	    tree.setAutoExpandColumn("name");  
	    tree.setTrackMouseOver(false);
	    tree.setLoadMask(true);
	    tree.setWidth(600);
	    tree.setHeight(400);
	    tree.setBorders(true);
	    tree.getView().setEmptyText("no data");
	    tree.setId("myTreeid");
	    tree.getSelectionModel().addListener(Events.SelectionChange,  
		            new Listener<SelectionChangedEvent<ModelData>>() {  
	            public void handleEvent(SelectionChangedEvent<ModelData> be) { 
	              if (be.getSelection().size() > 0) {  
	            	  populateAssignmentForm((Integer) be.getSelectedItem().getProperties().get("index"));
	              } else {  
	            	panel.clear();  
	              }
	            }
	          });  
	    
	    
	    
	       ToolBar toolBar = new ToolBar();  
	       toolBar.add(new LabelToolItem("Assignemnt Type: "));
	           
	        List<AssignmentTypes> list = AssignmentTypes.get();  
	        final ListStore<AssignmentTypes>  store = new ListStore<AssignmentTypes>();  
	        store.add(list);  
	        
	        typeComboBox = new ComboBox<AssignmentTypes>();  
	        typeComboBox.setTriggerAction(TriggerAction.ALL);  
	        typeComboBox.setEditable(false);  
	        typeComboBox.setWidth(100);
	        typeComboBox.setDisplayField("name");
	        typeComboBox.setName("name");
	        typeComboBox.setFieldLabel("Type");
	        typeComboBox.setStore(store);
	        typeComboBox.setAllowBlank(false);
	        typeComboBox.setValue(store.getAt(0));
	        typeComboBox.addListener(Events.Select, new Listener<FieldEvent>() {
	    	   		@Override
	    	      public void handleEvent(FieldEvent be) { 
	    	   		    assignmentType = typeComboBox.getValue().getId();
	    	   			loadAssignmentTreeGrid();
	    	      }  
	    	    }); 
	       toolBar.add(typeComboBox);
	       toolBar.add(new SeparatorToolItem());   
	       
	       toolBar.add(new LabelToolItem("Branch: "));
	       toolBarBranchComboBox = new BranchComboBox(false);
	       toolBarBranchComboBox.setEmptyText("Default");
	       toolBarBranchComboBox.addListener(Events.Select,new Listener<BaseEvent>(){
			@Override		public void handleEvent(BaseEvent be) {
						branch = Integer.parseInt(toolBarBranchComboBox.getValue().get("id").toString());
						loadAssignmentTreeGrid();
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

	       
	       toolBar.add(new LabelToolItem("Status Mode: "));
	       statusAssignmentButton = new Button();
	       statusAssignmentButton.setWidth(100);
	       statusAssignmentButton.setText("Active");
	       statusAssignmentButton.setBorders(true);
	       statusAssignmentButton.setIcon(Resources.ICONS.user_add());
	       statusAssignmentButton.addListener(Events.Select,new Listener<ButtonEvent>() {
	     		 @Override public void handleEvent(ButtonEvent be) {
	     			 if(assignmentStatus == true){
	     				    statusAssignmentButton.setText("All");
	     				    statusAssignmentButton.setIcon(Resources.ICONS.user_delete());
	     				    assignmentStatus = false;
	     			 }else if(assignmentStatus == false){
		     				statusAssignmentButton.setText("Active");
		     				statusAssignmentButton.setIcon(Resources.ICONS.user_add());
		     				assignmentStatus = true;
	     			 }
	     			loadAssignmentTreeGrid();
	     	      }  
	     	    });
	       toolBar.add(statusAssignmentButton);
	       
	       toolBar.add(new SeparatorToolItem());  
	       
	       Button addAssignmentButton = new Button();
	       addAssignmentButton.setWidth(140);
	       addAssignmentButton.setText("Add Assignment");
	       addAssignmentButton.setIcon(Resources.ICONS.add());
	       addAssignmentButton.addListener(Events.Select,new Listener<ButtonEvent>() {
	     		 @Override public void handleEvent(ButtonEvent be) {
	     			 				addAssignmentWindow(true,0);
	     			 }  
	     	    });
	       toolBar.add(addAssignmentButton);
    	   setTopComponent(toolBar);

	    
              
	    loadAssignmentTreeGrid();
	    
	    Menu contextMenu = new Menu();  
	    
	    MenuItem insert = new MenuItem();  
	    insert.setText("Insert Task");  
	    insert.setIconStyle("icon-add");
	    insert.addSelectionListener(new SelectionListener<MenuEvent>() {  
	      public void componentSelected(MenuEvent ce) { 
	    	
	    	  Integer parentId = (Integer) tree.getSelectionModel().getSelectedItem().getProperties().get("index");
	    	  addAssignmentWindow(false,parentId);
	    	  
	      }  
	    });  
	    contextMenu.add(insert);  
	  
	    MenuItem remove = new MenuItem();  
	    remove.setIconStyle("icon-delete");
	    remove.setText("Change Status");  
	    remove.addSelectionListener(new SelectionListener<MenuEvent>() {  
	      public void componentSelected(MenuEvent ce) {  
	    	  ModelData sel = tree.getSelectionModel().getSelectedItem();
	        	assigmentService.changeStatusAssignment(sel.get("index").toString(), new AsyncCallback<Void>() {
	 	           @Override
	 	           public void onFailure(Throwable caught) {
	 	               MessageBox.alert("Cannot Change Status", caught.getMessage(), null);
	 	           }
	 	           @Override
	 	           public void onSuccess(Void result) {
	 	        	  loadAssignmentTreeGrid();
	 	           }
	 	       });

	        
	      }  
	    });  
	    contextMenu.add(remove);  
	    setFrame(true);  
	    setSize(1024, 600);  
	    setLayout(new RowLayout(Orientation.HORIZONTAL)); 
	    
	    tree.setContextMenu(contextMenu); 
	    add(tree);
	    
	    FormPanel panel = createForm(); 
	    add(panel);

	}


	
	public  FormPanel createForm() {
		
		panel = new FormPanel();
	    panel.setWidth(400);   
        panel.setHeaderVisible(false);  
      
        IdNumberField = new NumberField();
        IdNumberField.setName("id");
        IdNumberField.hide();
        panel.add(IdNumberField);	
		
        nameTextField = new TextField<String>();
        nameTextField.setName("name");
        nameTextField.setAllowBlank(false);
        nameTextField.setAutoValidate(true);
        nameTextField.setFieldLabel("Name"); 
        panel.add(nameTextField);
        
        
        List<AssignmentTypes> list = AssignmentTypes.get();  
        final ListStore<AssignmentTypes>  store = new ListStore<AssignmentTypes>();  
        store.add(list);  
        
          
        
        
        typeSimpleComboBox = new ComboBox<AssignmentTypes>();  
        typeSimpleComboBox.setTriggerAction(TriggerAction.ALL);  
        typeSimpleComboBox.setEditable(false);  
        typeSimpleComboBox.setWidth(100);
        typeSimpleComboBox.setDisplayField("name");
        typeSimpleComboBox.setName("name");
        typeSimpleComboBox.setFieldLabel("Type");
        typeSimpleComboBox.setStore(store);
        typeSimpleComboBox.setAllowBlank(false);
        panel.add(typeSimpleComboBox);
        
        indexTextField = new TextField<String>();
        indexTextField.setName("index");
        indexTextField.setAllowBlank(true);
        indexTextField.setFieldLabel("Index"); 
        panel.add(indexTextField);
        
        byDefaultCheckBoxAssignment = new CheckBox();
        byDefaultCheckBoxAssignment.setFieldLabel("is Default ?");
        byDefaultCheckBoxAssignment.setName("name");
        panel.add(byDefaultCheckBoxAssignment);
        
        branchComboBox =  new BranchComboBox(false);
        panel.add(branchComboBox);
        
        ownerComboBox = new ComboBox<BeanModel>();
        ownerComboBox.setDisplayField("fullName");  
        ownerComboBox.setWidth(150);
        ownerComboBox.setName("user");
        ownerComboBox.setFieldLabel("Owner");
        ownerComboBox.setAllowBlank(false);
        ownerComboBox.setEditable(false);
        ownerComboBox.setTypeAhead(true);  
        ownerComboBox.setTriggerAction(TriggerAction.ALL);  
        ownerComboBox.addListener(Events.BeforeRender,new Listener<BaseEvent>(){
			@Override	public void handleEvent(BaseEvent be) {
				if(userStore != null){
					ownerComboBox.setStore(userStore);
				}else{
					ownerComboBox.setStore(getProjectManagerStore());	
				}
			}
        });

          
        
        panel.add(ownerComboBox);
        
        
        start = new DateField();
        start.setName("start");
        start.setFieldLabel("Start"); 
        start.disable();
        panel.add(start);
        
        updated = new DateField();
        updated.setFieldLabel("Updated");
        updated.disable();
        panel.add(updated);
        
        finish = new DateField();
        finish.setName("finish");
        finish.setFieldLabel("Finish");
        finish.setAllowBlank(true);
        panel.add(finish);
        
        noteTextArea = new TextArea();  
        noteTextArea.setPreventScrollbars(true);  
        noteTextArea.setFieldLabel("Description");
        noteTextArea.setName("note");
        noteTextArea.setMaxLength(450);
        panel.add(noteTextArea); 
   
        statusAssignment = new CheckBox();
        statusAssignment.setVisible(false);
        panel.add(statusAssignment);
        
       
        
        Button button = new Button();
        button.setText("Send");
        panel.add(button);

        button.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
            	
            	if(IdNumberField.getValue() != null && nameTextField.getValue() != null){
            	//	ModelData selected = tree.getSelectionModel().getSelectedItem();
                	updateAssignment();
                	panel.clear();
                //	tree.getSelectionModel().select(selected, true);
            	}
            }
        });
        return panel;
		
		
	}
	
	private void populateAssignmentForm(Integer id){
		
	 final AssignmentServiceAsync assignmentService = GWT.create(AssignmentService.class);
		 
		 assignmentService.getAssignmentDTOById(id, new AsyncCallback<AssignmentDTO>() {
				public void onFailure(Throwable caught) {
					
				}
				
				public void onSuccess(AssignmentDTO assignmentDTO) {
					panel.clear();
					IdNumberField.setValue(assignmentDTO.getId());
					selectedAssignment = assignmentDTO.getId();
					nameTextField.setValue((String) assignmentDTO.getName().toString());
					if(assignmentDTO.getIndex() != null){
						indexTextField.setValue((String) assignmentDTO.getIndex().toString());
					}
					if(assignmentDTO.getNote() != null){
						noteTextArea.setValue((String) assignmentDTO.getNote().toString());
					}
							
					ownerComboBox.setValue(ownerComboBox.getStore().findModel("id", assignmentDTO.getOwner().getId()));
					
					if(assignmentDTO.getLevel() != 0){
						ownerComboBox.disable();
						typeSimpleComboBox.disable();
						branchComboBox.disable();
					}else{
						ownerComboBox.enable();
						typeSimpleComboBox.enable();
						branchComboBox.enable();
					}
					
					statusAssignment.setValue(assignmentDTO.getStatus());
					byDefaultCheckBoxAssignment.setValue(assignmentDTO.getByDefault());
					branchComboBox.setValue(branchComboBox.getStore().findModel("id",assignmentDTO.getBranch().getId()));
				
					assignmentDTO.setByDefault(byDefaultCheckBoxAssignment.getValue());
					typeSimpleComboBox.setValue(new AssignmentTypes(assignmentDTO.getType()));
				
					
					
					if(assignmentDTO.getStarted() != null){
						start.setValue(assignmentDTO.getStarted());
					}
					if(assignmentDTO.getUpdated() != null){
						updated.setValue(assignmentDTO.getUpdated());
					}
					if(assignmentDTO.getFinished() != null){
						finish.setValue(assignmentDTO.getFinished());
					}

				}
			});
	}
	
	public  void loadAssignmentTreeGrid(){
		
		 final AssignmentServiceAsync assignmentService = GWT.create(AssignmentService.class);
			 assignmentService.getAssignmentDTOs(assignmentType,assignmentStatus,branch, new AsyncCallback<AssignmentGridTemplate>() {
					public void onFailure(Throwable caught) {
						
					}
					public void onSuccess(AssignmentGridTemplate model) {
					tree.getStore().removeAll();
					tree.setIconProvider(new ModelIconProvider<ModelData>() {

						public AbstractImagePrototype getIcon(ModelData model) {
						ModelData attribute = (ModelData ) model;
							if (attribute.get("leaf").equals(true) && attribute.get("status").equals(true)) {
								return Resources.ICONS.add();
							}else if(attribute.get("leaf").equals(true) && attribute.get("status").equals(false)){
								return Resources.ICONS.delete();
							} else {
								return Resources.ICONS.table();
							}				
						}

						});
			
						store.add(model.getChildren(), true);

					}
				});
			 
			
		}
	

	
	
	private void updateAssignment(){
		AssignmentDTO assignmentDTO = new AssignmentDTO();
		assignmentDTO.setId(selectedAssignment);
		
		
		if(nameTextField.getValue() !=null){
			assignmentDTO.setName(nameTextField.getValue());	
		}
		assignmentDTO.setIndex(indexTextField.getValue());
		assignmentDTO.setOwner((UserDTO) ownerComboBox.getValue().getBean()); 
		assignmentDTO.setType(typeSimpleComboBox.getValue().getId());
		assignmentDTO.setBranch(new BranchDTO(Integer.parseInt(branchComboBox.getValue().get("id").toString())));
		if(noteTextArea.getValue() == null){
			assignmentDTO.setNote(null);
		}else{
			assignmentDTO.setNote(noteTextArea.getValue());	
		}

		assignmentDTO.setByDefault(byDefaultCheckBoxAssignment.getValue());
		assignmentDTO.setStatus(statusAssignment.getValue());
		
		assigmentService.updateAssignment(assignmentDTO, new AsyncCallback<BaseModel>() {
	           @Override       public void onFailure(Throwable caught) {
	               MessageBox.alert("Cannot Update Assignment", caught.getMessage(), null);
	           }
	           @Override     public void onSuccess(BaseModel result) {
	        	    if(result.get("result").equals("failed")){
	 	               MessageBox.alert("Somethings goes wrong", result.get("msg").toString(), null);
	        	    }
 	               loadAssignmentTreeGrid();
 
	           }
	       });
		
	}
	
		/**
		 * addAssignmentWindow -- Add new Assignment Window
		 *   root -- true/false: 
		 *   false -- its a task, we can't change Assignment type 
		 *   true  --  its a new Assignment, we can change type
		 *   
		 */
		private void addAssignmentWindow(final Boolean root,final Integer parentId){
			final AssignmentTreeGrid panel = this;
			GWT.runAsync(new RunAsyncCallback(){
				@Override	public void onFailure(Throwable reason) {
					MessageBox.info("ERROR, can't draw New_assignment_Window",reason.getMessage(),null);					
				}
				@Override	public void onSuccess() {
					new AddAssignmentWindow(root,parentId,panel);					
				}
			});
		}
		
		public void loadProjectManagers(){
			ownerComboBox.setStore(getProjectManagerStore());		
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



		@Override
		public void reload() {
			loadAssignmentTreeGrid();
			
		}
		
}
