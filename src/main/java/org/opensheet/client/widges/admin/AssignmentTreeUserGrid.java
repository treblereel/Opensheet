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
import org.opensheet.client.dto.DepartmentDTO;
import org.opensheet.client.dto.grid.AssignmentGridTemplate;
import org.opensheet.client.mvc.events.AdminEvents;
import org.opensheet.client.mvc.events.EventBus;
import org.opensheet.client.services.AssignmentService;
import org.opensheet.client.services.AssignmentServiceAsync;
import org.opensheet.client.utils.AssignmentTypes;
import org.opensheet.client.utils.Resources;
import org.opensheet.client.widges.BranchComboBox;
import org.opensheet.client.widges.BranchDepartmentUserSwitchMenu;
import org.opensheet.shared.model.BaseModel.DepartmentBaseModel;
import org.opensheet.shared.model.BaseModel.UserBaseModel;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.dnd.ListViewDragSource;
import com.extjs.gxt.ui.client.dnd.ListViewDropTarget;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

//public class AssignmentTreeUserGrid extends TabPanel{

public class AssignmentTreeUserGrid extends ContentPanel {

	private TreeGrid<ModelData> tree;
	private TreeStore<ModelData> store;
	private ListStore<UserBaseModel> activeUsersStore;
	private ListView<UserBaseModel> activeUsers;
	private ListView<UserBaseModel> inactiveUsers;
	private ListStore<DepartmentBaseModel> departmentBaseModelStore;
	private BranchComboBox toolBarBranchComboBox;
	private Integer assignmentBranch = 1;
	private Integer userBranch = 1;
	private Integer department  = 1;
	private Integer assignmentType = 0;
	private Integer assignment;
	private BranchDepartmentUserSwitchMenu branchDepartmentUserSwitchMenu;
	
	/* here we updating department combobox store, coz it can changed since 
	 * start.
	 * .. and assignment tree also
	
	public void doUpdateStore(){
  	  if(departmentComboBox.isRendered()){
  		final DepartmentServiceAsync depService = GWT.create(DepartmentService.class);
	       depService.getDepartmentsBaseModel("1", new AsyncCallback<List<DepartmentBaseModel>>() {
				 	@Override	
				 	public void onFailure(Throwable caught) {	}
					@Override
					public void onSuccess(List<DepartmentBaseModel> result) {
						departmentBaseModelStore = new ListStore<DepartmentBaseModel>();
						departmentBaseModelStore.add(new DepartmentBaseModel(99999999,"All Users"));
						departmentBaseModelStore.add(result);
						departmentComboBox.setStore(departmentBaseModelStore);
						departmentComboBox.setValue(departmentBaseModelStore.getAt(0));
					}
		 });
 	  }
  	  
  	  loadAssignmentTreeGrid();
	}
	*/
	
	public AssignmentTreeUserGrid(){
		setFrame(true);  
	    setSize(1024, 600);  
	    setLayout(new RowLayout(Orientation.HORIZONTAL));  
	    setHeaderVisible(false);
	    
	    
	    getAssignmentTreeGrid();
	    loadAssignmentTreeGrid();
	    addUserLists();
	    
	    
	}
		
	public void getAssignmentTreeGrid(){
	
		
		
		
		    ColumnConfig name = new ColumnConfig("name", "Name", 230);
		    name.setRenderer(new TreeGridCellRenderer<ModelData>());  
		    name.setSortable(false);
		    name.setFixed(true);
		    name.setMenuDisabled(true);
		    ColumnModel cm = new ColumnModel(Arrays.asList(name)); 
		    store = new TreeStore<ModelData>();
		    
		    tree = new TreeGrid<ModelData>(store, cm);  
		    tree.setBorders(true);  
		    tree.setAutoExpandColumn("name");  
		    tree.setTrackMouseOver(false);
		    tree.setLoadMask(true);
		    tree.setWidth(250);
		    tree.setHeight(400);
		    tree.setBorders(true);
		    tree.getView().setEmptyText("no data");
		    tree.setId("myTreeid");
		    tree.getSelectionModel().addListener(Events.SelectionChange,  
		            new Listener<SelectionChangedEvent<ModelData>>() {  
	            public void handleEvent(SelectionChangedEvent<ModelData> be) { 
	              if (be.getSelection().size() > 0) {  
	            	 assignment = Integer.parseInt(be.getSelectedItem().getProperties().get("index").toString());
	            	 AssignmentDTO a = new AssignmentDTO(assignment);
	            	     populateUserLists(a,new DepartmentDTO(department),assignmentBranch);
	              
	              }
	            }
	          });
	    
	    
	        ToolBar toolBar = new ToolBar();  
	        toolBar.add(new LabelToolItem("Assignment Mode: "));
	       
	       
	       	List<AssignmentTypes> list = AssignmentTypes.get();  
	        final ListStore<AssignmentTypes>  store = new ListStore<AssignmentTypes>();  
	        store.add(list);  
	       
	        final ComboBox<AssignmentTypes> typeCombo = new ComboBox<AssignmentTypes>(); 
	        typeCombo.setTriggerAction(TriggerAction.ALL);  
	        typeCombo.setEditable(false);  
	        typeCombo.setWidth(100);
	        typeCombo.setStore(store);
	        typeCombo.setDisplayField("name");
	        typeCombo.setValue(store.getAt(0));
	        typeCombo.setFieldLabel("Type");
	        typeCombo.setAllowBlank(false);
	        
	        typeCombo.addListener(Events.Select, new Listener<BaseEvent>() {  
	        	@Override
	            public void handleEvent(BaseEvent be) {  
	        		assignmentType = typeCombo.getValue().getId();
    	   			loadAssignmentTreeGrid();
	            }
	          });
	        
	        
	        
	        
	        
	       toolBar.add(typeCombo);
	       toolBar.add(new SeparatorToolItem()); 
	       toolBar.add(new LabelToolItem("Branch: "));
	       toolBarBranchComboBox = new BranchComboBox(false);
	       toolBarBranchComboBox.setEmptyText("Default");
	       toolBarBranchComboBox.addListener(Events.Select,new Listener<BaseEvent>(){
			@Override		public void handleEvent(BaseEvent be) {
						assignmentBranch = Integer.parseInt(toolBarBranchComboBox.getValue().get("id").toString());
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
   
	       toolBar.add(new LabelToolItem("Branch/Department: "));
	       branchDepartmentUserSwitchMenu = new BranchDepartmentUserSwitchMenu();
	       
	       
	       toolBar.add(branchDepartmentUserSwitchMenu);
	       
	       EventBus.get().addListener(AdminEvents.AdminAssignmentDepartmentUserSwitch, new Listener<BaseEvent>() {
				public void handleEvent(BaseEvent e) {
					BaseModel result  = branchDepartmentUserSwitchMenu.getResult();
					
					userBranch = Integer.parseInt(result.get("branch").toString());
					department = Integer.parseInt(result.get("department").toString());
					populateUserLists(new AssignmentDTO(assignment),new DepartmentDTO(department),userBranch);
				}
			});   
	       
	    
	     
	       
		   setTopComponent(toolBar);
	       add(tree);
	       
	}
	
	
	
	
	
	
	public  void loadAssignmentTreeGrid(){
		
		 final AssignmentServiceAsync assignmentService = GWT.create(AssignmentService.class);
			 assignmentService.getAssignmentDTOs(assignmentType,true,assignmentBranch, new AsyncCallback<AssignmentGridTemplate>() {
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
	
	public void addUserLists(){
			
		
		ContentPanel cp = new ContentPanel();  
	    cp.setHeadingHtml("Left List -> Users within Assignment,Right nope, drap&drop them");  
	    cp.setSize(500, 400);  
	    cp.setLayout(new RowLayout(Orientation.HORIZONTAL));  
		
	    ToolBar toolBar = new ToolBar();
	    Button btnCommit = new Button("Commit");
	    btnCommit.addListener(Events.Select, new Listener<ButtonEvent>() {
	     		 @Override public void handleEvent(ButtonEvent be) {
	     			AssignmentDTO a = null;

	     			
	     			try{
	     				a = new AssignmentDTO((Integer) tree.getSelectionModel().getSelectedItem().get("index"));
	     				updateUsersList(activeUsers.getStore().getModels(),a,new DepartmentDTO(department));

	     			}catch (NumberFormatException nfe)
	     		    {
	     				System.out.println("NumberFormatException: " + nfe.getMessage());
	     		    }
	     			 }  
	     	    });
	    
	    btnCommit.setIcon(Resources.ICONS.add());
	    Button btnClean = new Button("Reload");
	    btnClean.addListener(Events.Select, new Listener<ButtonEvent>() {
    		 @Override public void handleEvent(ButtonEvent be) {
					 AssignmentDTO  a = new AssignmentDTO((Integer) tree.getSelectionModel().getSelectedItem().get("index"));
    				 populateUserLists(a,new DepartmentDTO(department),userBranch);
   			 }  
    	    });
	    
	    btnClean.setIcon(Resources.ICONS.delete());

	    toolBar.add(btnCommit);
	    toolBar.add(btnClean);
	    
	    cp.setBottomComponent(toolBar);
		activeUsers = new ListView<UserBaseModel>();
		activeUsers.setWidth(250);
		activeUsers.setDisplayProperty("name");
		activeUsers.setTitle("Users in Assignmnet");
		
		RowData data = new RowData(.5, 1);  
	    data.setMargins(new Margins(5));
		cp.add(activeUsers,data);
		
		
		
		inactiveUsers = new ListView<UserBaseModel>();
		inactiveUsers.setDisplayProperty("name"); 
		
		inactiveUsers.setTitle("Users not in Assignmnet");
		inactiveUsers.setWidth(250);
		cp.add(inactiveUsers,data);
		
		
		add(cp);
	}

	
	
	
	
	
	public void populateUserLists(AssignmentDTO assigmentDTO,DepartmentDTO departmentDTO,Integer branch){
		final AssignmentServiceAsync assignmentService = GWT.create(AssignmentService.class);
		 assignmentService.getAssignmentUsersByDepartmentByBranch(assigmentDTO,departmentDTO,true ,branch,new AsyncCallback<List<UserBaseModel>>() {
			 	@Override	
			 	public void onFailure(Throwable caught) {
			 		Info.display("Somethings wrong!", caught.toString());
					
				}
				@Override
				public void onSuccess(List<UserBaseModel> result) {
					activeUsersStore = new ListStore<UserBaseModel>();
					activeUsersStore.add(result);
					activeUsers.setStore(activeUsersStore);
				}
		 });
		 
		 assignmentService.getAssignmentUsersByDepartmentByBranch(assigmentDTO,departmentDTO,false,branch,new AsyncCallback<List<UserBaseModel>>() {
			 	@Override	
			 	public void onFailure(Throwable caught) {
					
				}
				@Override
				public void onSuccess(List<UserBaseModel> result) {
					ListStore<UserBaseModel> inactiveUsersStore = new ListStore<UserBaseModel>();
					inactiveUsersStore.add(result);
					inactiveUsers.setStore(inactiveUsersStore);
				}
		 });
		 
		 	new ListViewDragSource(activeUsers);  
		    new ListViewDragSource(inactiveUsers);  
		  
		    new ListViewDropTarget(activeUsers);  
		    new ListViewDropTarget(inactiveUsers); 
	}
	
	
	
	
	public void updateUsersList(List<UserBaseModel> users,AssignmentDTO assignmentDTO,DepartmentDTO departmentDTO){
		final AssignmentServiceAsync assignmentService = GWT.create(AssignmentService.class);
		 
		 assignmentService.updateAssignmentUsers(users,assignmentDTO,departmentDTO,  new AsyncCallback<Void>() {
			 	@Override	
			 	public void onFailure(Throwable caught) {
					MessageBox.alert("Error!", "Try again later" + caught, null);
				}
				@Override
				public void onSuccess(Void result) {
					Info.display("Assignment Has Been Added", "Succesfull");
				}
		 });
	}
	
	
	
	
	
}
