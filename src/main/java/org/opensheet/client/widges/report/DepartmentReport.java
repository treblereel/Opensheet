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
package org.opensheet.client.widges.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opensheet.client.dto.DepartmentDTO;
import org.opensheet.client.dto.grid.AssignmentGridTemplate;
import org.opensheet.client.l10n.OpensheetConstants;
import org.opensheet.client.services.AssignmentService;
import org.opensheet.client.services.AssignmentServiceAsync;
import org.opensheet.client.services.DepartmentService;
import org.opensheet.client.services.DepartmentServiceAsync;
import org.opensheet.client.utils.Resources;
import org.opensheet.client.widges.BranchComboBox;
import org.opensheet.client.widges.combo.AssignmentTypeComboBox;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class DepartmentReport extends ContentPanel{
	private OpensheetConstants myConstants = (OpensheetConstants) GWT.create(OpensheetConstants.class);
	private FormPanel leftSidePanel;
	private BranchComboBox branchComboBox;
	private Integer  type = 0;
	private TreeGrid<ModelData> assignmentTreeGrid;
	private TreeStore<ModelData>assignmentStore;
	private Integer branch = 9999999;
	private DepartmentServiceAsync departmentService = GWT.create(DepartmentService.class);
	private Grid<BeanModel> departmentGrid;
	private Integer departmentId;

	
	public DepartmentReport(){
		
		setHeaderVisible(false);
		setFrame(true);
		setLayout(new ColumnLayout());
		add(doSideMenu());
		add(doReportPanel());
		
	}
	
	private FormPanel doSideMenu(){
		leftSidePanel = new FormPanel();
		leftSidePanel.setWidth(450);
		leftSidePanel.setHeight(580);
		leftSidePanel.setHeaderVisible(false);
		leftSidePanel.setFrame(true);
		
		ToolBar tb = new ToolBar();
		tb.add(new LabelToolItem(myConstants.branch() + "  :" ));
		
		branchComboBox = new BranchComboBox(true);
		branchComboBox.setWidth(100);
		branchComboBox.setValue(branchComboBox.getStore().getAt(0));
		branchComboBox.addListener(Events.Select, new Listener<BaseEvent>(){
				@Override
				public void handleEvent(BaseEvent be) {
					branch = Integer.parseInt(branchComboBox.getValue().get("id").toString());
					 departmentGrid.getStore().getLoader().load();
				}
		    });
		branchComboBox.setAllowBlank(false);
		branchComboBox.setLazyRender(true);
		branchComboBox.setEmptyText("All_");
		tb.add(branchComboBox);
		
		tb.add(new LabelToolItem(myConstants.type() + "  :" ));
		final AssignmentTypeComboBox assignmentTypeComboBox =  new  AssignmentTypeComboBox();
		assignmentTypeComboBox.addListener(Events.Select,new  Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				type = assignmentTypeComboBox.getValue().getId();
				loadAssignmentTreeGrid();
			}
		});
		
		
		tb.add(assignmentTypeComboBox);
		
		 Button xlsExportButton = new Button();
		    xlsExportButton.setText("Excel Export");
		    xlsExportButton.setIcon(Resources.ICONS.table()); 
		    xlsExportButton.addListener(Events.Select, new Listener<BaseEvent>(){
				@Override
				public void handleEvent(BaseEvent be) {
					if(branch !=9999999){
						Window.Location.assign(GWT.getHostPageBaseURL().toString() + "reportdepartmentassignmenttoxls.htm?branch=" + branch + "&type="+ type);
					}else{
						MessageBox.info("ERROR","Plz choose branch first",null);
					}
				}
				
		    });
		tb.add(xlsExportButton);
		leftSidePanel.setTopComponent(tb);
		leftSidePanel.add(DepartmentFormGrid());
		return leftSidePanel;
	}
	
	
	private Grid<BeanModel> DepartmentFormGrid(){
	      RpcProxy<List<DepartmentDTO>> proxy = new RpcProxy<List<DepartmentDTO>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<List<DepartmentDTO>> callback) {
            	departmentService.getDepartmentsByBranch("1",branch,callback);
            }
	      };
	      ListLoader<?> loader = new BaseListLoader<ListLoadResult<ModelData>>(proxy,new BeanModelReader());
	      final ListStore<BeanModel> store = new ListStore<BeanModel>(loader);
	      store.setMonitorChanges(true);
	      loader.load();


	      List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		  columns.add(new ColumnConfig("name", myConstants.department(), 120));
		  columns.add(new ColumnConfig("owner.fullName",myConstants.owner(), 200));
  		  ColumnModel cm = new ColumnModel(columns);
			
  		  departmentGrid = new Grid<BeanModel>(store, cm);
  		  departmentGrid.setLoadMask(true);
  		  departmentGrid.setWidth(440);
  		  departmentGrid.setHeight(500);

  		  departmentGrid.addStyleName(".my-table-style");
  		  departmentGrid.setBorders(true);
  		  departmentGrid.setAutoExpandColumn("name");
  		  departmentGrid.getView().setEmptyText("no data");
  		  departmentGrid.setId("myDepartmentGridiD");
 		  departmentGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);  
  		  departmentGrid.getSelectionModel().addListener(Events.SelectionChange,  
			   new Listener<SelectionChangedEvent<BeanModel>>() {  
			     public void handleEvent(SelectionChangedEvent<BeanModel> be) { 
			    	    departmentId =   Integer.parseInt(departmentGrid.getSelectionModel().getSelectedItem().get("id").toString());
						loadAssignmentTreeGrid();
			     }  
			   });  

	
  		  return departmentGrid;
		}
	
		private ContentPanel doReportPanel(){
				
				
				ContentPanel rightSidePanel = new ContentPanel();
				rightSidePanel.setWidth(650);
				rightSidePanel.setHeight(600);
				rightSidePanel.setHeaderVisible(false);
				rightSidePanel.setFrame(true);
		//		rightSidePanel.setTopComponent(addToolBar());
				rightSidePanel.add(doTreeGrid());
				return rightSidePanel;
				
			}
		/*
		private ToolBar addToolBar(){
			
			ToolBar toolBar = new ToolBar();
			toolBar.add(new LabelToolItem(myConstants.type() + "  :" ));
			final AssignmentTypeComboBox assignmentTypeComboBox =  new  AssignmentTypeComboBox();
			assignmentTypeComboBox.addListener(Events.Select,new  Listener<BaseEvent>(){
				@Override
				public void handleEvent(BaseEvent be) {
					type = assignmentTypeComboBox.getValue().getId();
					loadAssignmentTreeGrid();
				}
			});
			
			
			toolBar.add(assignmentTypeComboBox);
		    toolBar.add(new SeparatorToolItem());

		   
	//	    toolBar.add(xlsExportButton);
			return toolBar;
		}
	*/	
		private TreeGrid<ModelData> doTreeGrid(){
			assignmentStore = new TreeStore<ModelData>();
			
			ColumnConfig name = new ColumnConfig("name",myConstants.name(), 150);
			name.setFixed(true);
		    name.setRenderer(new TreeGridCellRenderer<ModelData>());  
		    ColumnConfig index = new ColumnConfig("assignmentIndex",myConstants.index(), 150);
		    index.setFixed(true);
		    index.setSortable(false);
		    index.setFixed(true);
		    index.setMenuDisabled(true);
		    ColumnConfig owner = new ColumnConfig("owner",myConstants.owner(), 270);  
		    ColumnModel cm = new ColumnModel(Arrays.asList(name, index, owner)); 
			
			
			
			
			assignmentTreeGrid = new TreeGrid<ModelData>(assignmentStore, cm);  
			assignmentTreeGrid.setBorders(true);  
			assignmentTreeGrid.setAutoExpandColumn("name");  
			assignmentTreeGrid.setTrackMouseOver(false);
			assignmentTreeGrid.setLoadMask(true);
			assignmentTreeGrid.setWidth(600);
			assignmentTreeGrid.setHeight(580);
			assignmentTreeGrid.setBorders(true);
			assignmentTreeGrid.getView().setEmptyText("no data");
			assignmentTreeGrid.setId("myAssignmentTreeGridid");
			assignmentTreeGrid.getSelectionModel().addListener(Events.SelectionChange,  
				            new Listener<SelectionChangedEvent<ModelData>>() {  
			            public void handleEvent(SelectionChangedEvent<ModelData> be) { 
			              if (be.getSelection().size() > 0) {  
			              
			              } else {  

			              }
			            }
			          }); 
		  return assignmentTreeGrid;
		}
		
		public  void loadAssignmentTreeGrid(){
			if(departmentId !=null){
			
			 final AssignmentServiceAsync assignmentService = GWT.create(AssignmentService.class);
				 assignmentService.getAssignmentsByDepartmentAndByType(departmentId,type, new AsyncCallback<AssignmentGridTemplate>() {
						public void onFailure(Throwable caught) {
							
						}
						public void onSuccess(AssignmentGridTemplate model) {
						assignmentTreeGrid.getStore().removeAll();
						assignmentTreeGrid.setIconProvider(new ModelIconProvider<ModelData>() {

							public AbstractImagePrototype getIcon(ModelData model) {
							ModelData attribute = (ModelData ) model;
								if (attribute.get("type").equals(0) ) {
									return Resources.ICONS.project_icon();
								}else if (attribute.get("type").equals(1) ) {
									return Resources.ICONS.tender_icon();
								}else if (attribute.get("type").equals(2) ) {
									return Resources.ICONS.office_icon();
								}else if (attribute.get("type").equals(3) ) {
									return Resources.ICONS.off_hour_icon();
								}
								return null;	
							}
							});
						assignmentStore.add(model.getChildren(),true);
						}
					});
				 
			   }
			}
			
		
}
