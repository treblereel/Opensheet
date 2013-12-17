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
package org.opensheet.client.widges.admin.settings;

import java.util.ArrayList;
import java.util.List;

import org.opensheet.client.dto.AuthmethodDTO;
import org.opensheet.client.services.AuthmethodService;
import org.opensheet.client.services.AuthmethodServiceAsync;
import org.opensheet.client.utils.Resources;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentPlugin;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AuthPanel extends ContentPanel {
	private ToolBar toolBar;
	private AuthmethodServiceAsync authService = GWT.create(AuthmethodService.class);
	private ListStore<BeanModel>   authMethodStore = null;
	private Grid<BeanModel>        authmethodGrid;
	private ListLoader<?> loader;
	private FormPanel  authmethodFormPanel;
	private TextField<String> bindUserField,passwdbindUserField,ldapUrlField,BaseCNField,methodDescriptionField,domainField;
	private NumberField methodIdField;
	private BaseModel answer;
	private Integer authMethodId;

	
	public AuthPanel(){
		createToolBar();
		setLayout(new ColumnLayout());
		setHeaderVisible(false);

		add(createAuthmethodGrid());
		add(emptyAuthmethodFormPanel());
		
	}
	
	
	
	private void createToolBar(){
		toolBar = new ToolBar();
		
		Button newAuth = new Button("add new Auth source");
		newAuth.setIcon(Resources.ICONS.user_add());
		
		
		
		toolBar.add(newAuth);
		setTopComponent(toolBar);
	}
	
	private Grid<BeanModel> createAuthmethodGrid(){
		populateAuthmethodGrid();
		
		
		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		
		ColumnConfig typeColumnConfig = new ColumnConfig("type", "Type", 100);
		typeColumnConfig.setSortable(false);
		typeColumnConfig.setMenuDisabled(true);
		columns.add(typeColumnConfig);
		
		
		ColumnConfig descriptionColumnConfig = new ColumnConfig("description", "Description", 300);
		descriptionColumnConfig.setSortable(false);
		descriptionColumnConfig.setMenuDisabled(true);
		columns.add(descriptionColumnConfig);
		
		
		
		ColumnModel cm = new ColumnModel(columns);
		
		authmethodGrid = new Grid<BeanModel>(authMethodStore,cm);
		authmethodGrid.setLoadMask(true);
		authmethodGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); 
		authmethodGrid.setSize(400, 300);
		authmethodGrid.addStyleName(".my-table-style");
		authmethodGrid.setBorders(true);
		authmethodGrid.setAutoExpandColumn("type");
		authmethodGrid.getView().setEmptyText("no data");
		authmethodGrid.setId("myauthmethodGridIid");
		
		authmethodGrid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<BeanModel>(){

			@Override			public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
				chooseAuthmethodFormType();
				Integer authmethodId = Integer.parseInt(authmethodGrid.getSelectionModel().getSelectedItem().get("id").toString());
				String  type =  authmethodGrid.getSelectionModel().getSelectedItem().get("type");
				if(type.equals("ad")){
					ADFormPanel(authmethodId);
					getAuthMetodDetails(authmethodId);
				}else{
					authmethodFormPanel.removeAll();
				}
				
			}
			
		});
		

		
		
		authmethodGrid.addListener(Events.OnClick, new Listener<BaseEvent>(){
			@Override	public void handleEvent(BaseEvent be) {
				if (authmethodGrid.getSelectionModel().getSelection().size() > 0) {  
			//		chooseAuthmethodFormType();
			
					
	               } 
			}
		});
		
		authmethodGrid.getSelectionModel().select(authmethodGrid.getStore().getAt(0), true);
		
		return authmethodGrid;
	}
	
	
	private void populateAuthmethodGrid(){
		  RpcProxy<List<AuthmethodDTO>> authMethodProxy = new RpcProxy<List<AuthmethodDTO>>() {
              @Override
              protected void load(Object loadConfig, AsyncCallback<List<AuthmethodDTO>> callback) {
            	  authService.getAuthMethods(callback);
             }
          };    
      
          
          
      
      loader = new BaseListLoader<ListLoadResult<ModelData>>(authMethodProxy,new BeanModelReader());
      authMethodStore = new ListStore<BeanModel>(loader);
      authMethodStore.setMonitorChanges(true);
      loader.load();
		
	}
	
	private FormPanel emptyAuthmethodFormPanel(){
		authmethodFormPanel = new FormPanel();
		authmethodFormPanel.setSize(400, 300);
		authmethodFormPanel.setFrame(true);
		authmethodFormPanel.setHeaderVisible(false);
		return authmethodFormPanel;
	}
	

	private void chooseAuthmethodFormType(){
		Integer authmethodId = Integer.parseInt(authmethodGrid.getSelectionModel().getSelectedItem().get("id").toString());
		String  type =  authmethodGrid.getSelectionModel().getSelectedItem().get("type");
		if(type.equals("ad")){
			ADFormPanel(authmethodId);
		}else{
			authmethodFormPanel.removeAll();
		}
		
	}
	
	
	private void ADFormPanel(Integer authmethodId){
		final Integer id = authmethodId;
		
		authmethodFormPanel.removeAll();
		FormData formData = new FormData("-20");
		VerticalPanel vp = new VerticalPanel();  
	    vp.setSpacing(10); 
	    authmethodFormPanel.add(vp);
	    
	    ComponentPlugin plugin = new ComponentPlugin() {  
	        public void init(Component component) {  
	          component.addListener(Events.Render, new Listener<ComponentEvent>() {  
	            public void handleEvent(ComponentEvent be) {  
	              El elem = be.getComponent().el().findParent(".x-form-element", 3);  
	              // should style in external CSS  rather than directly  
	              elem.appendChild(XDOM.create("<div style='color: #615f5f;padding: 1 0 2 0px;'>" + be.getComponent().getData("text") + "</div>"));  
	            }  
	          });  
	        }  
	      };  
	    
	    methodIdField = new NumberField();
	    methodIdField.setName("description");
	    methodIdField.setAllowBlank(false);
	    methodIdField.setAutoValidate(true);
	    methodIdField.hide();
	    
	    methodDescriptionField = new TextField<String>();
	    methodDescriptionField.setName("description");
	    methodDescriptionField.setFieldLabel("Description");
	    methodDescriptionField.setAllowBlank(false);
	    methodDescriptionField.setAutoValidate(true);
		authmethodFormPanel.add(methodDescriptionField);  
		
		bindUserField = new TextField<String>();
		bindUserField.setName("binduser");
		bindUserField.setFieldLabel("Bind User");
		bindUserField.setAllowBlank(false);
		bindUserField.setAutoValidate(true);
		authmethodFormPanel.add(bindUserField);
		
		domainField = new TextField<String>();
		domainField.setName("domain");
		domainField.setFieldLabel("Domain name");
		domainField.setAllowBlank(false);
		domainField.setAutoValidate(true);
		authmethodFormPanel.add(domainField);
		
		passwdbindUserField = new TextField<String>();
		passwdbindUserField.setName("passwdbinduser");
		passwdbindUserField.setFieldLabel("Password");
		passwdbindUserField.setAllowBlank(false);
		passwdbindUserField.setPassword(true);
		passwdbindUserField.setAutoValidate(true);
		authmethodFormPanel.add(passwdbindUserField);
		
		
		ldapUrlField = new TextField<String>();
		ldapUrlField.setName("ldapurl");
		ldapUrlField.setFieldLabel("Ldap Url");
		ldapUrlField.setAllowBlank(false);
		ldapUrlField.setAutoValidate(true);
		ldapUrlField.addPlugin(plugin);
		ldapUrlField.setData("text", "Example here");
		authmethodFormPanel.add(ldapUrlField,formData);
		
		BaseCNField = new TextField<String>();
		BaseCNField.setName("basecn");
		BaseCNField.setFieldLabel("Base CN");
		BaseCNField.setAllowBlank(false);
		BaseCNField.setAutoValidate(true);
		authmethodFormPanel.add(BaseCNField);
		
		
		LayoutContainer main = new LayoutContainer();  
	    main.setLayout(new ColumnLayout());  
	    
	    LayoutContainer left = new LayoutContainer();  
	    left.setStyleAttribute("paddingRight", "10px");  
	    FormLayout layout = new FormLayout();  
	    layout.setLabelAlign(LabelAlign.TOP);  
	    left.setLayout(layout);  
	    
	    LayoutContainer center = new LayoutContainer();  
	    center.setStyleAttribute("paddingRight", "10px");  
	    layout = new FormLayout();  
	    layout.setLabelAlign(LabelAlign.TOP);  
	    center.setLayout(layout);
	    
	    LayoutContainer right = new LayoutContainer();  
	    right.setStyleAttribute("paddingLeft", "10px");  
	    layout = new FormLayout();  
	    layout.setLabelAlign(LabelAlign.TOP);  
	    right.setLayout(layout);  
		
		
		Button saveADsettingsBtn = new Button();
		saveADsettingsBtn.setText("Save");
		saveADsettingsBtn.addListener(Events.Select,new Listener<BaseEvent>(){
			@Override			public void handleEvent(BaseEvent be) {
				if(authMethodId !=null && methodDescriptionField.isValid() == true && domainField.isValid() == true && bindUserField.isValid()== true && passwdbindUserField.isValid()== true && ldapUrlField.isValid()== true && BaseCNField.isValid()== true){
					BaseModel bm = new BaseModel();
					bm.set("id", authMethodId);
					bm.set("description", methodDescriptionField.getValue());
					bm.set("domain", domainField.getValue());
					bm.set("binduser", bindUserField.getValue());	
					bm.set("bindpasswd", passwdbindUserField.getValue());	
					bm.set("url", ldapUrlField.getValue());	
					bm.set("basecn", BaseCNField.getValue());
					updateAuthMethod(bm);
				}
				
				
				
			}
			
		});
		left.add(saveADsettingsBtn);
		
		Button removeADsettingsBtn = new Button();
		removeADsettingsBtn.setText("Remove");
		removeADsettingsBtn.addListener(Events.Select,new Listener<BaseEvent>(){
			@Override		public void handleEvent(BaseEvent be) {
				confirmAuthSourceRemoval();
			}
			
		});

		
		
		center.add(saveADsettingsBtn);
		
		Button reloadADsettingsBtn = new Button();
		reloadADsettingsBtn.setText("Reload");
		reloadADsettingsBtn.addListener(Events.Select,new Listener<BaseEvent>(){
			@Override			public void handleEvent(BaseEvent be) {
				getAuthMetodDetails(authMethodId);
			}
			
		});
		
		
		
		right.add(reloadADsettingsBtn);
		
		main.add(left, new ColumnData(.5));
		main.add(center, new ColumnData(.5));  
	    main.add(right, new ColumnData(.5));  
		
	    
	    authmethodFormPanel.add(main, new FormData("100%")); 
		authmethodFormPanel.layout();
	}
	
	private void loadFormDataAd(BaseModel result){
		authMethodId = (Integer)result.get("id");
		methodDescriptionField.setValue(result.get("description").toString());
		domainField.setValue(result.get("domain").toString());
		bindUserField.setValue(result.get("binduser").toString());
		passwdbindUserField.setValue(result.get("bindpasswd").toString());
		ldapUrlField.setValue(result.get("url").toString());
		BaseCNField.setValue(result.get("basecn").toString());
	}
	
	
	
	private BaseModel getAuthMetodDetails(Integer authId){
		authService.get(authId,true,new AsyncCallback<BaseModel>(){
			@Override			public void onFailure(Throwable caught) {		}
			@Override			public void onSuccess(BaseModel result) {
				if(result.get("type").equals("ad")){
					loadFormDataAd(result);
				}
			}
		});
		return answer;
	}
	
	private void updateAuthMethod(BaseModel bm){
		authService.set(bm, new AsyncCallback<Void>(){

			@Override	public void onFailure(Throwable caught) {
					Info.display("Error", "Somethings wrong");				
			}

			@Override	public void onSuccess(Void result) {
				Info.display("Success", "Ok");				
			}
			
		});
		
		
	}
	
	
	private void confirmAuthSourceRemoval(){
		final Window w = new Window();
		w.setWidth(100);
		w.setHeight(100);
		w.setTitle("Confirm Auth Source Removal");
		
		Button yes = new Button("Yes");
		yes.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override			public void handleEvent(BaseEvent be) {
				authMethodStore.getLoader().load();
				w.hide();
			}
			
		});
		w.add(yes);
		
		Button no = new Button("No");
		yes.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override			public void handleEvent(BaseEvent be) {
				w.hide();
			}
			
		});
		w.add(no);
	}
	
	
}
