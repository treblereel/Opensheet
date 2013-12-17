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

import java.util.Arrays;
import java.util.Date;
import org.opensheet.client.dto.grid.AssignmentGridTemplate;
import org.opensheet.client.services.AssignmentService;
import org.opensheet.client.services.AssignmentServiceAsync;
import org.opensheet.client.services.StatService;
import org.opensheet.client.services.StatServiceAsync;
import org.opensheet.client.services.TimelineService;
import org.opensheet.client.services.TimelineServiceAsync;


import org.opensheet.client.services.UserService;
import org.opensheet.client.services.UserServiceAsync;
import org.opensheet.client.utils.AssignmentTypes;
import org.opensheet.client.utils.Resources;
import org.opensheet.client.widges.Reloadable;
import org.opensheet.client.widges.windows.AddAssignmentWindow;

import com.extjs.gxt.ui.client.Style.HideMode;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;


public class ProjectPanel extends ContentPanel implements Reloadable{
	

	private AssignmentServiceAsync  assignmentService  = GWT.create(AssignmentService.class);
	private UserServiceAsync 		userService        = GWT.create(UserService.class);
	private StatServiceAsync        chartService       = GWT.create(StatService.class);
	private TimelineServiceAsync    timelineService    = GWT.create(TimelineService.class);


	

	private TabPanel tabPanel;
	private TreeGrid<ModelData> grid;
	private TreeStore<ModelData> assignmentStore;
    final ListStore<AssignmentTypes>  typeStore = new ListStore<AssignmentTypes>();

    private FormPanel form;
    private Boolean statusOfAssignmentToLoad = true;
    private Button statusAssignmentButton;
    private Date date = new Date();;
 
    private ContentPanel south;
    private AssignmentDetailPanel assignmentDetailPanel;
    private AssignmentDepartmentPanel assignmentDepartmentPanel;
    
    
	public ProjectPanel() {
		
		this.addListener(Events.Attach, new Listener<BaseEvent>(){
			@Override	public void handleEvent(BaseEvent be) {
				loadAssignments();

			}
			
		});
		
		
		
		
		
		
		setLayout(new RowLayout(Orientation.VERTICAL));
		setFrame(true);
		setHeaderVisible(false);
		
		ContentPanel north = new ContentPanel();
		south = new ContentPanel();
		
		
		north.setLayout(new RowLayout(Orientation.HORIZONTAL));
		north.setWidth(1024);
		north.setHeight(330);
		north.setHeaderVisible(false);

		
		
//		south.setLayout(new RowLayout(Orientation.HORIZONTAL));
		south.setWidth(1024);
		south.setHeight(265);
		south.setHeaderVisible(false);
		south.setFrame(true);
		south.setScrollMode(Scroll.AUTO);
	
//		assignmentQuickReportContentPanel =  new AssignmentQuickReportContentPanel(south);
		
		
		ContentPanel temp = new ContentPanel();
		temp.setWidth(20);
		temp.setHeight(330);
		temp.setFrame(true);
		temp.setBorders(false);
		temp.setHeaderVisible(false);
		
		north.add(drawAssignemntTree());
		north.add(temp);
		north.add(tabPanel());
		

		
		add(north);
		add(south);
	}
	
	
	
	private ContentPanel drawAssignemntTree(){
		
		
		assignmentStore = new TreeStore<ModelData>();
		loadAssignments();
		
		
		ColumnConfig name = new ColumnConfig("name", "Name", 300);
	    name.setRenderer(new TreeGridCellRenderer<ModelData>());
	    name.setMenuDisabled(true);
	    name.setSortable(false);
	    

	   
	    ColumnModel cm = new ColumnModel(Arrays.asList(name));  
	    grid = new TreeGrid<ModelData>(assignmentStore, cm);
		grid.setHeight(280);
		grid.setWidth(330);
		grid.getSelectionModel().addListener(Events.SelectionChange,  new Listener<SelectionChangedEvent<ModelData>>() {  
			public void handleEvent(SelectionChangedEvent<ModelData> be) { 
              if (be.getSelection().size() > 0) {
            	  Integer assignmentId = Integer.parseInt(be.getSelectedItem().getProperties().get("index").toString());
            	  assignmentDetailPanel.setData(assignmentId);
            	  assignmentDepartmentPanel.setData(assignmentId);
            	  
	           // 	  loadAssignmentDetailsForm(assignmentId);
	           // 	  getAssignmentTimeline(assignmentId);  //убрать в loadAssignmentDetailsForm
	            //	  drawAssignemntYearChart(date.getYear(),assignmentId);
	            	  
	            //	  departmentTimeline.setData(assignmentId);
	            //	  assignmentQuickReportContentPanel.setData(assignmentId);
	            //	  assignmentExportAndReportContentPanel.setData(assignmentId);
	            //	  departmentCheckGrid.setData(assignmentId);
              } else {  
            	// form.clear();  
              }
            }
          });   
		
	    final Menu contextMenu = new Menu();
	    final MenuItem insert = new MenuItem();
  	  	insert.setText("Insert Task");  
  	  	insert.setIconStyle("icon-add");
  	  	insert.addSelectionListener(new SelectionListener<MenuEvent>() {  
	      public void componentSelected(MenuEvent ce) { 
	    	  final Integer parentId = (Integer) grid.getSelectionModel().getSelectedItem().getProperties().get("index");
	    	  	addAssignmentWindow(false,parentId);
	      }  
	    });
	    contextMenu.add(insert);
	    
	    final MenuItem addDepartmentMenuItem = new MenuItem();
	    addDepartmentMenuItem.setText("Department Managment");  
	    addDepartmentMenuItem.setIcon(Resources.ICONS.table());
	    addDepartmentMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {  
	      public void componentSelected(MenuEvent ce) {
	    	  final Integer assignmentId = (Integer) grid.getSelectionModel().getSelectedItem().getProperties().get("index");
	    	  addDepartmentWindow(assignmentId);
	      }  
	    });
	    contextMenu.add(addDepartmentMenuItem);
	    
	    
	    
	    MenuItem remove = new MenuItem();  
	    remove.setIconStyle("icon-delete");
	    remove.setText("Change Status");  
	    remove.addSelectionListener(new SelectionListener<MenuEvent>() {  
	      public void componentSelected(MenuEvent ce) {  
	    	  ModelData sel = grid.getSelectionModel().getSelectedItem();
	    	  	   assignmentService.changeStatusAssignment(sel.get("index").toString(), new AsyncCallback<Void>() {
	 	           @Override
	 	           public void onFailure(Throwable caught) {
	 	               MessageBox.alert("Cannot Change Status", caught.getMessage(), null);
	 	           }
	 	           @Override
	 	           public void onSuccess(Void result) {
	 	        	  loadAssignments();
	 	           }
	 	       });
	      }  
	    });  
	    contextMenu.add(remove);
	    contextMenu.addListener(Events.BeforeShow, new Listener<BaseEvent>(){
			@Override	public void handleEvent(BaseEvent be) {
		    	  Boolean leaf = (Boolean) grid.getSelectionModel().getSelectedItem().getProperties().get("leaf");
		    	 if(leaf == false){
		    		 insert.enable();
		    	 }else{
		    		 insert.disable(); 
		    	 }
			}
	    	
	    });

	   
	    
	    MenuItem users = new MenuItem("Users");  
	    users.setIcon(Resources.ICONS.user_add());
	    users.addSelectionListener(new SelectionListener<MenuEvent>() {  
		      public void componentSelected(MenuEvent ce) {  
		    	  ModelData sel = grid.getSelectionModel().getSelectedItem();
		    	  final Integer assignmentId = Integer.parseInt(sel.get("index").toString());
		    //	  new AssignmentUsers(assignmentId);
		    //	  addDepartmentWindow(assignmentId);
		    	  
		    	  GWT.runAsync(new RunAsyncCallback() {
		    	      public void onFailure(Throwable err) {
		    	      MessageBox.info("ERROR",err.getMessage(),null);
		    	      }
		    	      public void onSuccess() {
						new AssignmentUsers(assignmentId);		    	      }
		    	    });
		    	    
		      }
		    });
	    contextMenu.add(users);
	    
	    grid.setContextMenu(contextMenu);
	    
		
		ToolBar assignmentToolBar = new ToolBar();
		assignmentToolBar.add(new LabelToolItem("Status Mode: "));
		statusAssignmentButton = new Button();
	       statusAssignmentButton.setWidth(100);
	       statusAssignmentButton.setText("Active");
	       statusAssignmentButton.setBorders(true);
	       statusAssignmentButton.setIcon(Resources.ICONS.user_add());
	       statusAssignmentButton.addListener(Events.Select,new Listener<ButtonEvent>() {
	     		 @Override public void handleEvent(ButtonEvent be) {
	     			 if(statusOfAssignmentToLoad == true){
	     				statusAssignmentButton.setText("All");
	     				statusAssignmentButton.setIcon(Resources.ICONS.user_delete());
	     				statusOfAssignmentToLoad = false;
	     			 }else if(statusOfAssignmentToLoad == false){
		     				statusAssignmentButton.setText("Active");
		     				statusAssignmentButton.setIcon(Resources.ICONS.user_add());
		     				statusOfAssignmentToLoad = true;
	     			 }
	     			loadAssignments();
	     	      }  
	     	    });
	       assignmentToolBar.add(statusAssignmentButton);
	       assignmentToolBar.add(new SeparatorToolItem());
		
	       Button addAssignmentButton = new Button("Add new Assignment");
	       addAssignmentButton.setIcon(Resources.ICONS.add());
	       addAssignmentButton.addListener(Events.Select,new Listener<ButtonEvent>() {
	     		 @Override public void handleEvent(ButtonEvent be) {
	     			addAssignmentWindow(true,0);
	     			 }  
	     	    });
	       assignmentToolBar.add(addAssignmentButton);
		
		ContentPanel  p = new ContentPanel();
		p.add(assignmentToolBar);
		p.setTopComponent(assignmentToolBar);
		p.setHeadingHtml("Your Assignments:");
		p.add(grid);
		
		
		
		
		return p;
	}

	
	private void addAssignmentWindow(final Boolean root,final Integer parentId){
		final ProjectPanel cp = this;	
		  GWT.runAsync(new RunAsyncCallback() {
  		    public void onSuccess() {
				new AddAssignmentWindow(root,parentId,cp);
  		    }
  		    public void onFailure(Throwable msg) {
  		     MessageBox.info("Somethings wrong, ask your Administrator", msg.getMessage(),null);
  		    }
  	  });
		
		
	}
	
	private void addDepartmentWindow(final Integer assignmentId){
		final ProjectPanel cp = this;	
		  GWT.runAsync(new RunAsyncCallback() {
  		    public void onSuccess() {
				new AddDepartmentToAssignmentWindow(assignmentId,cp);
  		    }
  		    public void onFailure(Throwable msg) {
  		     MessageBox.info("Somethings wrong, ask your Administrator", msg.getMessage(),null);
  		    }
  	  });
		
		
	}
	
	public void loadAssignments(){
		 final AssignmentServiceAsync assignmentService = GWT.create(AssignmentService.class);
		 assignmentService.getProjectManagerAssignments(statusOfAssignmentToLoad, new AsyncCallback<AssignmentGridTemplate>(){

			@Override	public void onFailure(Throwable caught) {

			}

			@Override	public void onSuccess(AssignmentGridTemplate result) {
				grid.getStore().removeAll();
				grid.setIconProvider(new ModelIconProvider<ModelData>() {
				public AbstractImagePrototype getIcon(ModelData model) {
					ModelData attribute = (ModelData ) model;
							if (attribute.get("leaf").equals(true) && attribute.get("status").equals(true)) {
							return Resources.ICONS.add();
						}else if(attribute.get("leaf").equals(true) && attribute.get("status").equals(false)){
							return Resources.ICONS.delete();
						} else if (attribute.get("leaf").equals(false) && attribute.get("status").equals(true)){
							return Resources.ICONS.table();
						}else{
							return Resources.ICONS.delete();
						}
					}

					});
				assignmentStore.add(result.getChildren(), true);
			}
		 });
		 

	}
	
	
	
	private TabPanel tabPanel(){
		
		
					
					tabPanel = new TabPanel();
					tabPanel.setWidth(670);
					tabPanel.setHeight(300);
					tabPanel.setAutoHeight(true); 
					
				    
					TabItem assignmentDetailsTab = new TabItem("Assignment Details");  
				    assignmentDetailsTab.setIcon(Resources.ICONS.table());  
				    assignmentDetailsTab.addStyleName("pad-text");  
				    assignmentDetailPanel = new AssignmentDetailPanel(this);
				    assignmentDetailsTab.add(assignmentDetailPanel);
				   
				    TabItem assignmentDepartmentTab = new TabItem("Assignment => Department");  
				    assignmentDepartmentTab.setHideMode(HideMode.OFFSETS);
				    assignmentDepartmentTab.setIconStyle("icon-dm");
				    assignmentDepartmentTab.addStyleName("pad-text");  
				    assignmentDepartmentPanel = new AssignmentDepartmentPanel();
				    assignmentDepartmentTab.add(assignmentDepartmentPanel);

	    
	    
	    
	    
	    
	  
	    
	    TabItem assignmentReportTab = new TabItem("Assignemnt Report");  
	    assignmentReportTab.setIcon(Resources.ICONS.table());
	    assignmentReportTab.setLayout(new RowLayout());

		tabPanel.add(assignmentDetailsTab);
		tabPanel.add(assignmentDepartmentTab);
		return tabPanel;
	}



	@Override
	public void reload() {
		loadAssignments();
	}

	
	
	 public void assignmentDepartmentPanelSetData(Integer assignmentId){
		 assignmentDepartmentPanel.setData(assignmentId);
	 }

	
	/*
		        Button save = new Button();
		        save.setText("Save");
		        save.addListener(Events.Select, new Listener<BaseEvent>(){
					@Override	public void handleEvent(BaseEvent be) {
						assignmentUpdateDTO = new AssignmentDTO(idField.getValue().intValue());
						assignmentUpdateDTO.setName(name.getValue());
						assignmentUpdateDTO.setIndex(indexTextField.getValue());
						assignmentUpdateDTO.setOwner(thisAssignmentDTO.getOwner());
						assignmentUpdateDTO.setType(typeSimpleComboBox.getValue().getId());
						assignmentUpdateDTO.setFinished(finish.getValue());
						assignmentUpdateDTO.setNote(noteTextArea.getValue());
						assignmentUpdateDTO.setStatus(checkBox.getValue());
						
						timelineService.setAssignmentTimeline(idField.getValue().intValue(), assignmentTimeline.getValue().intValue(),false, new AsyncCallback<BaseModel>(){
							@Override	public void onFailure(Throwable caught) {
		                         MessageBox.alert("ERROR", caught.getMessage(), null);
								
							}
							@Override	public void onSuccess(BaseModel result) {
								Integer assignmentId = Integer.parseInt(grid.getSelectionModel().getSelectedItem().getProperties().get("index").toString());

								if(result.get("type").equals("error") || result.get("type").equals("root_error")){
									assignmentTimelineError(assignmentId,assignmentTimeline.getValue().intValue(),result.get("type").toString(),result.get("string").toString());
								}else{
									Info.display("Success", "Everythings OK");	
								}
							}
						});
						
						
						
						if(checkBox.getValue() == false && thisAssignmentDTO.getStatus()==true){
							final Dialog dialog = new Dialog();  
							dialog.setHeading("Assignemnt Disable Action");  
							dialog.setButtons(Dialog.YESNO);  
							dialog.setBodyStyleName("pad-text");  
							dialog.addText("If you disable  an Assignemnt it will remove all users/departments from this assignment");  
							dialog.getItem(0).getFocusSupport().setIgnore(true);  
							dialog.setScrollMode(Scroll.AUTO);  
							dialog.setHideOnButtonClick(true);  
							dialog.getButtonById("yes").addListener(Events.Select, new SelectionListener<ButtonEvent>(){
								@Override	public void componentSelected(ButtonEvent ce) {
									assignmentUpdateDTO.setStatus(false);
									assignmentService.updateAssignment(assignmentUpdateDTO, new AsyncCallback<BaseModel>(){
										@Override	public void onFailure(Throwable caught) {				}
										@Override	public void onSuccess(BaseModel result) {
											loadAssignments();
											
											
											// Add byDefault Assignemnt check
										}
									 });
								}
					        });
							
							dialog.getButtonById("no").addListener(Events.Select, new SelectionListener<ButtonEvent>(){
								@Override	public void componentSelected(ButtonEvent ce) {
									assignmentUpdateDTO.setStatus(true);
									
									assignmentService.updateAssignment(assignmentUpdateDTO, new AsyncCallback<BaseModel>(){
										@Override	public void onFailure(Throwable caught) {				}
										@Override	public void onSuccess(BaseModel result) {
											
											// Add Error Check
											loadAssignments();
										}
									 });
								}
					        });
							dialog.setClosable(false);
							dialog.show();

						}else{
							assignmentService.updateAssignment(assignmentUpdateDTO, new AsyncCallback<BaseModel>(){
								@Override	public void onFailure(Throwable caught) {				}
								@Override	public void onSuccess(BaseModel result) {
									
									// Add Error Dialog!
									loadAssignments();
								}
							 });
						}
					}
		        });
		        
		        Button cancel = new Button();
		        cancel.setText("Cancel");
		        cancel.addListener(Events.Select, new Listener<BaseEvent>(){
					@Override	public void handleEvent(BaseEvent be) {
						form.clear();
						if(thisAssignmentDTO !=null)
							loadAssignmentDetailsForm(thisAssignmentDTO.getId());
					}
		        });
		        
		      
		        form.setButtonAlign(HorizontalAlignment.LEFT);
		        form.addButton(save);
		        form.addButton(cancel);
				
		        fcp.add(form);
		        
		
		        
		        TabPanel tabPanel = new TabPanel();
		        tabPanel.setHeight(300);
		        tabPanel.setWidth(255);

		  //      tabPanel.setWidth(268);
		        
		        departmentCheckGrid = new DepartmentCheckGrid();
		        departmentTimeline =  new DepartmentTimeline(south);
		        departmentTimeline.disable();
		        
		        TabItem departmentTimelineTab = new TabItem();
		        departmentTimelineTab.setText("departments & timelines");
		        departmentTimelineTab.add(departmentTimeline);
		        tabPanel.add(departmentTimelineTab);
		        
		        TabItem departmentCheckTab = new TabItem();
		        departmentCheckTab.setText("departments");
		        departmentCheckTab.add(departmentCheckGrid);
		        tabPanel.add(departmentCheckTab);
		        
		        
		        
		        
		        fcp.add(tabPanel);
		        
				return fcp;
			}
			

			
			private void  drawAssignemntYearChart(int year,int assignemntId){
				final Chart chart = new Chart("resources/chart/open-flash-chart.swf");
				chart.setId("chartId");
				chartService.getAssignemntStatByYear(year+1900,assignemntId,new AsyncCallback<List<Number>>(){
					@Override	public void onFailure(Throwable caught) {	}
					@Override	public void onSuccess(List<Number> result) {
						south.removeAll();
						MonthChart f = new MonthChart();
						chart.setHeight(220);
						chart.setChartModel(f.getChartModel(result));
						
						south.add(chart);
						south.layout();
					
					}
				});
			}
			

			
		
			
			
			private void  getAssignmentTimeline(Integer assignmentId){
				timelineService.getAssignmentTimeline(assignmentId, new AsyncCallback<Integer>(){
					@Override	public void onFailure(Throwable caught) {}
					@Override	public void onSuccess(Integer result) {
						assignmentTimeline.setValue(result);
					}
				});
			}
			
	*/		
	
	/*
			private void assignmentTimelineError(final Integer assignmentId,final Integer timeline,String error,String errorString){
				
				
				assignmentTimelineErrorWindow =  new Window();
				assignmentTimelineErrorWindow.setWidth(250);
				assignmentTimelineErrorWindow.setHeight(180);
				assignmentTimelineErrorWindow.setResizable(false);
				assignmentTimelineErrorWindow.setHeading("ERROR");
				assignmentTimelineErrorWindow.setClosable(false);
				
				
				Button okButton = new Button("OK");
				okButton.addListener(Events.Select,new Listener<ButtonEvent>(){
					@Override	public void handleEvent(ButtonEvent be) {
						timelineService.setAssignmentTimeline(assignmentId, timeline,true, new AsyncCallback<BaseModel>(){
							@Override	public void onFailure(Throwable caught) {
								Info.display("ERROR", "Something Wrong");								
							}
							@Override	public void onSuccess(BaseModel result) {
								if(result.get("type").equals("error") || result.get("type").equals("root_error")){
									assignmentTimelineError(assignmentId,timeline,result.get("type").toString(),result.get("string").toString());
								}else{
									Info.display("Success", "Everythings OK");	
								}
							}
						});
						assignmentTimelineErrorWindow.hide();
					}
				});
				
				
				
				Button cancelButton = new Button("Cancel");
				cancelButton.addListener(Events.Select,new Listener<ButtonEvent>(){
					@Override	public void handleEvent(ButtonEvent be) {
						assignmentTimelineErrorWindow.hide();
			//			getAssignmentTimeline(assignmentId);
						}
					});

				if(error.equals("root_error")){
					assignmentTimelineErrorWindow.addText(errorString);
					okButton.disable();
				}else if(error.equals("error")){
					assignmentTimelineErrorWindow.addText(errorString + "<br>" + "You can allocate additional hours to Root assignment or cancel allocation for this assignment ");

				}
				
				assignmentTimelineErrorWindow.addButton(okButton);
				assignmentTimelineErrorWindow.addButton(cancelButton);
				assignmentTimelineErrorWindow.show();

			}
			
	*/		
		
					
			
}



