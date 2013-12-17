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
import java.util.Date;
import java.util.List;

import org.opensheet.client.dto.AuthmethodDTO;
import org.opensheet.client.dto.DepartmentDTO;
import org.opensheet.client.dto.UserDTO;
import org.opensheet.client.dto.UserRateDTO;

import org.opensheet.client.services.AuthmethodService;
import org.opensheet.client.services.AuthmethodServiceAsync;
import org.opensheet.client.services.DepartmentService;
import org.opensheet.client.services.DepartmentServiceAsync;
import org.opensheet.client.services.UserService;
import org.opensheet.client.services.UserServiceAsync;
import org.opensheet.client.widges.BranchComboBox;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.SelectionMode;
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
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserFormGrid extends ContentPanel{
	
	/* do relead all combo boxes
	 * 
	 * 
	 * 
	 */
	
		
	 private FormPanel panel;

	 
	 
	 private Window windowAddUser;
	 
	 /* define with collection of Users 
	  * we want to get
	  * 
	  */
	 private String usersRpcCriteria = "1";
	 private String departmentRpcCriteria = "1";
	 private Integer selectedBranch = 1;
	 
	 private TextField<String> login,password,firstNameTextField,lastNameTextField,emailTextField,lang,addUserEmailTextField = null;
	 private NumberField  id,internalRateField;
	 private DateField start,finish,updated;
	 private TextArea noteTextArea;
	 private CheckBox statusBox;
	 private ComboBox<BeanModel> departmentComboBox,addUserdepartmentComboBox;
	 private ComboBox<BeanModel> authmethoCombo;
	 private Grid<BeanModel> grid;
	 private BranchComboBox branchComboBox,newUserBranchComboBox,toolBarBranchComboBox;
	 
	 private ListStore<BeanModel> userStore;
	 private ListStore<BeanModel>  departmentStore;
	 private ListStore<BeanModel>  authmethodStore;
	 
	 private UserServiceAsync userService = GWT.create(UserService.class);
	 private DepartmentServiceAsync departmentService = GWT.create(DepartmentService.class);
	 private AuthmethodServiceAsync authmethodService = GWT.create(AuthmethodService.class);

	 private ListLoader<?> loader;
	 private ListLoader<?> Dloader;
	 private ListLoader<?> authmethodloader;
	 
		
	 
	public UserFormGrid(){
		   setHeaderVisible(false); 
		   setFrame(true);  
	       setSize(1024, 600);  
	       setLayout(new RowLayout(Orientation.HORIZONTAL));
	    
	       RpcProxy<List<UserDTO>> UserProxy = new RpcProxy<List<UserDTO>>() {
	                @Override
	                protected void load(Object loadConfig, AsyncCallback<List<UserDTO>> callback) {
	                     userService.getUsersByBranch(usersRpcCriteria,selectedBranch,callback);
	                }
	            };    
	        this.loader = new BaseListLoader<ListLoadResult<ModelData>>(UserProxy,new BeanModelReader());
	        final ListStore<BeanModel> UserStore = new ListStore<BeanModel>(loader);
	        UserStore.setMonitorChanges(true);
	        
	        
	       
	
	 
	    List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
	   
	    columns.add(new ColumnConfig("login", "Login", 100));
	    columns.add(new ColumnConfig("fullName", "Name", 220));
	    columns.add(new ColumnConfig("status", "Status", 40));
	    columns.add(new ColumnConfig("department.name", "Department", 80));
	    ColumnModel cm = new ColumnModel(columns);
   
	   grid = new Grid<BeanModel>(UserStore, cm);
	   grid.setLoadMask(true);
       grid.setWidth(1800);
       grid.addStyleName(".my-table-style");
       grid.setBorders(true);
       grid.setAutoExpandColumn("login");
       grid.getView().setEmptyText("no data");
       grid.setId("myid");
       grid.addListener(Events.OnClick,new  Listener<BaseEvent>(){
		@Override 	public void handleEvent(BaseEvent be) {
			if(grid.getSelectionModel().getSelectedItem().get("authmethod.id").toString().equals("1")){
				password.enable();
			}else{
				password.disable();
			}
		}
       });
       grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);  
       grid.getSelectionModel().addListener(Events.SelectionChange,  
           new Listener<SelectionChangedEvent<BeanModel>>() {  
             public void handleEvent(SelectionChangedEvent<BeanModel> be) { 
              	  
               if (be.getSelection().size() > 0) { 
            	  userService.getUser(Integer.parseInt(grid.getSelectionModel().getSelectedItem().get("id").toString()),new AsyncCallback<UserDTO>(){
					@Override	public void onFailure(Throwable caught) {		}
					@Override	public void onSuccess(UserDTO result) {
						populateUserForm(result);
					}
            		  
            	  }); 
                  for(BeanModel a: authmethoCombo.getSelection()){
                	 if(a.get("id").equals(be.getSelection().get(0).get("authmethod.id"))){
                		 authmethoCombo.select(a);
                	 }
                 }
               } else {  
            	   panel.clear();  
               }  
             }  
           }); 
       
       
       
       
       
       
       ToolBar toolBar = new ToolBar();  
       toolBar.add(new LabelToolItem("User Mode: "));
       
       final SimpleComboBox<String> type = new SimpleComboBox<String>();  
       type.setTriggerAction(TriggerAction.ALL);  
       type.setEditable(false);  
       type.setFireChangeEventOnSetValue(true);  
       type.setWidth(100);  
       type.add("Active");  
       type.add("Killed in Action");
       type.add("EveryBody");
       type.setSimpleValue("Active");
       
       toolBar.add(type);
       type.addListener(Events.Change, new Listener<FieldEvent>() {
    	   		@Override
    	      public void handleEvent(FieldEvent be) { 
    	   			if(type.getSimpleValue().equals("EveryBody")){
    	   				usersRpcCriteria = "any";
    	   			}else if(type.getSimpleValue().equals("Active")){
    	   				usersRpcCriteria = "1";
    	   			}else if(type.getSimpleValue().equals("Killed in Action")){
    	   				usersRpcCriteria = "0";
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
       
       
       Button addUserButton = new Button("Add User", new SelectionListener<ButtonEvent>() {  
    	      @Override  
    	      public void componentSelected(ButtonEvent ce) {  
    	        addUserWindow();  
    	      }  
    	    });  
       addUserButton.setIconStyle("icon-user");
       
       
       
       toolBar.add(addUserButton);
       setTopComponent(toolBar); 
       
       FormPanel panel = createForm();  

       add(grid, new RowData(.6, 1));
       add(panel, new RowData(.4, 1));

   }	
	
       private FormPanel createForm() {
    
    	   
    	   RpcProxy<List<DepartmentDTO>> DepartmentProxy = new RpcProxy<List<DepartmentDTO>>() {
               @Override
               protected void load(Object loadConfig, AsyncCallback<List<DepartmentDTO>> callback) {
               	departmentService.getDepartments("1",callback);
               }
           }; 
    	   
           	Dloader = new BaseListLoader<ListLoadResult<ModelData>>(DepartmentProxy,new BeanModelReader());
	        departmentStore = new ListStore<BeanModel>(this.Dloader);
	        departmentStore.setMonitorChanges(true);
	        Dloader.load();
	        
	        RpcProxy<List<AuthmethodDTO>> AuthmetodProxy =  new RpcProxy<List<AuthmethodDTO>>(){
				@Override protected void load(Object loadConfig,	AsyncCallback<List<AuthmethodDTO>> callback) {
					authmethodService.getAuthMethods(callback);
				}
	        };
	        
	        
	        
	        
	        authmethodloader = new BaseListLoader<ListLoadResult<ModelData>>(AuthmetodProxy,new BeanModelReader());
	        authmethodStore = new ListStore<BeanModel>(authmethodloader);
	        authmethodStore.setMonitorChanges(true);
	        authmethodloader.load();
	        
	        
    	   
    	   panel = new FormPanel();
           panel.setHeaderVisible(false);  
           
           
           id = new NumberField();
           id.setName("id");
           id.hide();
           panel.add(id);
           
           login = new TextField<String>();
           login.setName("login");
           login.setAllowBlank(false);
           login.setAutoValidate(true);
           login.setFieldLabel("Login");
           
           panel.add(login);
           
          

           
           firstNameTextField = new TextField<String>();
           firstNameTextField.setName("firstName");
           firstNameTextField.setFieldLabel("First Name");
           firstNameTextField.setAllowBlank(false);
           firstNameTextField.setAutoValidate(true);
           panel.add(firstNameTextField);
           
           lastNameTextField = new TextField<String>();
           lastNameTextField.setName("secondName");
           lastNameTextField.setFieldLabel("Last Name");
           lastNameTextField.setAllowBlank(false);
           lastNameTextField.setAutoValidate(true);
           panel.add(lastNameTextField);
           
           emailTextField = new TextField<String>();
           emailTextField.setName("email");
           emailTextField.setFieldLabel("E-Mail"); 
           panel.add(emailTextField);
           
           
           authmethoCombo = new ComboBox<BeanModel>();
           authmethoCombo.setDisplayField("description"); 
           authmethoCombo.setWidth(150);
           authmethoCombo.setName("authmethod");
           authmethoCombo.setFieldLabel("Auth method");
           authmethoCombo.setAllowBlank(false);
           authmethoCombo.setEditable(false);
           authmethoCombo.setStore(authmethodStore); 
           authmethoCombo.setTypeAhead(true);
           authmethoCombo.setTriggerAction(TriggerAction.ALL);
           authmethoCombo.addListener(Events.Select, new Listener<FieldEvent>(){
   			@Override	public void handleEvent(FieldEvent fe) {
   				password.disable();
   				
   				if(authmethoCombo.getValue().get("id").toString().equals("1")){
   					password.enable();
   				}
   		
   			}
           	   
              });
           
           
           
           panel.add(authmethoCombo);
           
           
           password = new TextField<String>();
           password.setPassword(true);
           password.setName("password");
           password.setFieldLabel("Password");
           password.disable();
           
           
           panel.add(password);
           
           
           statusBox = new CheckBox();
           statusBox.setName("status");
           statusBox.setFieldLabel("Status");
           panel.add(statusBox);
           
          
           
           start = new DateField();
           start.setName("start");
           start.setFieldLabel("Start"); 
           start.disable();
           panel.add(start);
           
           updated = new DateField();
           updated.setName("updated");
           updated.setFieldLabel("Updated");
           updated.disable();
           panel.add(updated);
           
           finish = new DateField();
           finish.setName("finish");
           finish.setFieldLabel("Finish"); 
           panel.add(finish);
           
    
           branchComboBox = new BranchComboBox(false);
           panel.add(branchComboBox);
           
           departmentComboBox = new ComboBox<BeanModel>();
           departmentComboBox.setDisplayField("name");  
           departmentComboBox.setWidth(150);
           departmentComboBox.setName("department");
           departmentComboBox.setFieldLabel("Department");
           departmentComboBox.setAllowBlank(false);
           departmentComboBox.setEditable(false);
           departmentComboBox.setStore(departmentStore);  
           departmentComboBox.setTriggerAction(TriggerAction.ALL);  
           panel.add(departmentComboBox);
 
           
           LayoutContainer main = new LayoutContainer();  
           main.setLayout(new ColumnLayout());
           
           LayoutContainer left = new LayoutContainer(); 
   	       FormLayout layout = new FormLayout();  
   	       layout.setLabelAlign(LabelAlign.LEFT);  
   	       left.setLayout(layout); 
           
	   	   LayoutContainer right = new LayoutContainer();  
		   layout = new FormLayout();  
		   layout.setLabelAlign(LabelAlign.LEFT); 
		   right.setLayout(layout);
   	       
		   internalRateField = new NumberField();
		   internalRateField.setName("updated");
		   internalRateField.setFieldLabel("Internal Rate");
		   left.add(internalRateField,new FormData(60, 22));

		   
		 

		   
           main.add(right,new FormData(140, 22));
           main.add(left,new FormData(140, 22));
           
          
           
           panel.add(main);
          
           noteTextArea = new TextArea();  
           noteTextArea.setPreventScrollbars(true);  
           noteTextArea.setFieldLabel("Description");
           noteTextArea.setName("note");
           noteTextArea.setMaxLength(450);
           panel.add(noteTextArea); 
           
           
           Button button = new Button();
           button.setText("Save");
           panel.add(button);

           button.addListener(Events.OnClick, new Listener<BaseEvent>() {
               @Override  public void handleEvent(BaseEvent be) {
   	        	if( login.isValid() && firstNameTextField.isValid() && lastNameTextField.isValid()){
   	        		updateUser();
   	        	}               	
               }
           });
           
           
            return panel; 
           
       }
       
       
     
   
   
   private void updateUser() {

       UserDTO userDTO = new UserDTO();
       UserRateDTO userRate = new UserRateDTO();
       userDTO.setUserRateDTO(userRate);
       
       userDTO.setId((Integer) id.getValue().intValue());
       userDTO.setLogin(login.getValue().toString());       
       userDTO.setAuthmethod(new AuthmethodDTO((Integer.parseInt(authmethoCombo.getValue().get("id").toString() ))));
       userDTO.setBranch(branchComboBox.get());
       
       
       if(authmethoCombo.getValue().get("id").toString().equals("1")){
    	   userDTO.setPassword(password.getValue());   
       }
       
       
       userDTO.setFirstName(firstNameTextField.getValue().toString());
       userDTO.setSecondName(lastNameTextField.getValue().toString());
       
       if(emailTextField.getValue() != null){
       	   userDTO.setEmail(emailTextField.getValue().toString());
       }
       
       if(noteTextArea.getValue() == null){
		   	userDTO.setNote(null);
	      } else {
	    	userDTO.setNote(noteTextArea.getValue().toString());
	   	  }
       
       if(internalRateField.getValue() == null){
    	   userDTO.getUserRateDTO().setInternalRate(0);
       }else{
    	   userDTO.getUserRateDTO().setInternalRate((Integer)internalRateField.getValue().intValue());

       }
       
      
       
       userDTO.setStatus((Boolean) statusBox.getValue().booleanValue());
       
       DepartmentDTO departmentDTO = new DepartmentDTO((Integer) Integer.parseInt(departmentComboBox.getValue().get("id").toString()));
       userDTO.setDepartment(departmentDTO);
       userDTO.setFinish(finish.getValue());
       
       userService.updateUser(userDTO, new AsyncCallback<Void>() {
           @Override    public void onFailure(Throwable caught) {
               MessageBox.alert("Cannot Update User", caught.getMessage(), null);
           }
           @Override
           public void onSuccess(Void result) {
              Info.display("Ok!", "The User has been Updated");
               loader.load();
           }
       });
       
       

   } 
   
   private void addUserWindow(){
	    windowAddUser = new Window();  
	    windowAddUser.setSize(360, 440);  
	    windowAddUser.setPlain(true);  
	    windowAddUser.setModal(true);  
	    windowAddUser.setBlinkModal(true);  
	    windowAddUser.setHeadingHtml("Add new user");  
	    windowAddUser.setLayout(new FitLayout());
	    
	   

	    
	    
	    panel = new FormPanel();
        panel.setHeaderVisible(false);  
      
        login = new TextField<String>();
        login.setName("login");
        login.setFieldLabel("Login");
        login.setAllowBlank(false);
        login.setAutoValidate(true);
        panel.add(login);
        
       
        firstNameTextField = new TextField<String>();
        firstNameTextField.setName("firstName");
        firstNameTextField.setFieldLabel("First Name");
        firstNameTextField.setAllowBlank(false);
        lastNameTextField.setAutoValidate(true);
        panel.add(firstNameTextField);
        
        lastNameTextField = new TextField<String>();
        lastNameTextField.setName("secondName");
        lastNameTextField.setFieldLabel("Last Name");
        lastNameTextField.setAllowBlank(false);
        lastNameTextField.setAutoValidate(true);
        panel.add(lastNameTextField);
        
        addUserEmailTextField = new TextField<String>();
        addUserEmailTextField.setName("email2");
        addUserEmailTextField.setFieldLabel("E-Mail"); 
        addUserEmailTextField.setAllowBlank(true);
        addUserEmailTextField.setRegex(".+@.+\\.[a-z]+");
        addUserEmailTextField.getMessages().setRegexText("Bad email address!!");
        addUserEmailTextField.setAutoValidate(true);
        panel.add(addUserEmailTextField);
        
       
        statusBox = new CheckBox();
        statusBox.setName("status");
        statusBox.setFieldLabel("Status");
        statusBox.setValue(true);  
        panel.add(statusBox);
        
       
        
        authmethoCombo = new ComboBox<BeanModel>();
        authmethoCombo.setDisplayField("description"); 
        authmethoCombo.setWidth(150);
        authmethoCombo.setName("authmethod");
        authmethoCombo.setFieldLabel("Auth method");
        authmethoCombo.setAllowBlank(false);
        authmethoCombo.setAutoValidate(true);
        authmethoCombo.setEditable(false);
        authmethoCombo.setForceSelection(true);
        authmethoCombo.setStore(authmethodStore); 
        authmethoCombo.setTypeAhead(true);
        authmethoCombo.select(authmethodStore.getAt(0));
        authmethoCombo.setTriggerAction(TriggerAction.ALL);
        authmethoCombo.addListener(Events.Select, new Listener<FieldEvent>(){
			@Override	public void handleEvent(FieldEvent fe) {
				password.disable();
				if(authmethoCombo.getValue().get("id").toString().equals("1")){
					password.enable();
				}
		
			}
        	   
           });
        
        
        
        panel.add(authmethoCombo);
        
        
        password = new TextField<String>();
        password.setPassword(true);
        password.setName("password");
        password.setFieldLabel("Password");
        password.disable();
        panel.add(password);
        
        
        start = new DateField();
        start.setName("start");
        start.setFieldLabel("Start"); 
        start.disable();
        start.setValue(new Date());
        panel.add(start);
        
        updated = new DateField();
        updated.setName("updated");
        updated.setFieldLabel("Updated");
        updated.disable();
        updated.setValue(new Date());
        panel.add(updated);
        
        finish = new DateField();
        finish.setName("finish");
        finish.setFieldLabel("Finish");
        finish.setValue(new Date());
        finish.setAllowBlank(true);
        
        panel.add(finish);
        
        newUserBranchComboBox = new BranchComboBox(false);
        panel.add(newUserBranchComboBox);
        
        addUserdepartmentComboBox = new ComboBox<BeanModel>();
        addUserdepartmentComboBox.setDisplayField("name");  
        addUserdepartmentComboBox.setWidth(150);
        addUserdepartmentComboBox.setName("department");
        addUserdepartmentComboBox.setFieldLabel("Department");
        addUserdepartmentComboBox.setAllowBlank(false);
        addUserdepartmentComboBox.setEditable(false);
        addUserdepartmentComboBox.setValue(departmentStore.getAt(0));
        addUserdepartmentComboBox.setStore(this.departmentStore);  
        addUserdepartmentComboBox.setTypeAhead(true);  
        addUserdepartmentComboBox.setTriggerAction(TriggerAction.ALL);  
        panel.add(addUserdepartmentComboBox);

       
        noteTextArea = new TextArea();  
        noteTextArea.setPreventScrollbars(true);  
        noteTextArea.setFieldLabel("Description");
        noteTextArea.setName("note");
        noteTextArea.setMaxLength(450);
        panel.add(noteTextArea); 
        
        
        windowAddUser.addButton(new Button("Add user", new SelectionListener<ButtonEvent>() {  
	        @Override  
	        public void componentSelected(ButtonEvent ce) {  
	        
	        	if( login.isValid() && firstNameTextField.isValid() && lastNameTextField.isValid() && authmethoCombo.isValid() && newUserBranchComboBox.isValid()){
	        		addUser();
	        	}
	        }  
	      }));
	    windowAddUser.addButton(new Button("Cancel", new SelectionListener<ButtonEvent>() {  
	        @Override  
	        public void componentSelected(ButtonEvent ce) {  
	          windowAddUser.hide();  
	        }  
	      }));
	    
	    windowAddUser.add(panel);
	    windowAddUser.show();
	    
	   
	   
   }
   
  private void addUser() {
	  UserDTO userDTO = new UserDTO();
      
      userDTO.setLogin(login.getValue().toString());
	  
	  
	  if(addUserEmailTextField.getValue() == null){
		   userDTO.setEmail(null);
       }else if(addUserEmailTextField.getValue().isEmpty() == false){
    	   userDTO.setEmail(addUserEmailTextField.getValue().toString());
       }
	   
	  if(noteTextArea.getValue() == null){
		   userDTO.setNote(null);
      }
	  else if(noteTextArea.getValue().isEmpty() == false)
	  {
   	   	  userDTO.setNote(noteTextArea.getValue().toString());
      }
	  
	  if(statusBox.getValue() != true){
		  userDTO.setStatus(false);
      }
	  
	  userDTO.setAuthmethod(new AuthmethodDTO((Integer.parseInt(authmethoCombo.getValue().get("id").toString() ))));
      if(authmethoCombo.getValue().get("id").toString().equals("1")){
    	  userDTO.setPassword(password.getValue());   
      }

	  
       userDTO.setFirstName(firstNameTextField.getValue().toString());
       userDTO.setSecondName(lastNameTextField.getValue().toString());
       userDTO.setDepartment((DepartmentDTO) addUserdepartmentComboBox.getValue().getBean());
       userDTO.setStart(start.getValue());
       userDTO.setUpdated(updated.getValue());
       
       if(finish.getValue() == null){
		   userDTO.setFinish(null);
       }else{
    	   userDTO.setFinish(finish.getValue());
       }
       
       userDTO.setBranch(newUserBranchComboBox.get());

       	
       
       
       userService.addUser(userDTO, new AsyncCallback<Void>() {
           @Override
           public void onFailure(Throwable caught) {
               MessageBox.alert("Cannot add User", caught.getMessage(), null);
           }
           @Override
           public void onSuccess(Void result) {
             Info.display("Ok!", "The userDTO has been added");
               loader.load();
               windowAddUser.hide();
           }
       });
       
       

   }    
   
  public void reloadGrid(String usersRpcCriteria){
	  if(grid != null){
		  this.usersRpcCriteria = usersRpcCriteria;
		  grid.getStore().getLoader().load();
	  }
  }
  
  
  
  private void populateUserForm(UserDTO user){
	  
	  
	  panel.clear();
	  id.setValue(user.getId());
	  login.setValue(user.getLogin());
	  firstNameTextField.setValue(user.getFirstName());
	  lastNameTextField.setValue(user.getSecondName());
	  password.setValue("********");
	  emailTextField.setValue(user.getEmail());
	  statusBox.setValue(user.getStatus());
	  start.setValue(user.getStart());
	  updated.setValue(user.getUpdated());
	  finish.setValue(user.getFinish());
	  internalRateField.setValue(user.getUserRateDTO().getInternalRate());
	  noteTextArea.setValue(user.getNote());
	  branchComboBox.set(user.getBranch());
	  departmentComboBox.setValue(departmentComboBox.getStore().findModel("id",user.getDepartment().getId()));
	  authmethoCombo.setValue(authmethoCombo.getStore().findModel("id",user.getAuthmethod().getId()));
	  
  }
  

   
}
