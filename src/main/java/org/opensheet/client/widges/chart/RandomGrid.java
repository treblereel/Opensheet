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
package org.opensheet.client.widges.chart;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;

public class RandomGrid extends ContentPanel{

	private ListStore<BaseModel> store;
	private String[] indexes;
	private String[] names;
	private Integer width = 1024;
	
	public RandomGrid(List<BaseModel> s){
		super();
		setHeight(205);
		this.setHeaderVisible(false);
		this.setWidth(width);
		
		store = new ListStore<BaseModel>();
		store.add(s);
		
		
		parseIndexes(s.get(0).get("indexes").toString());
		parseNames(s.get(0).get("names").toString());
		
		
		if(indexes.length > 12 ){
			this.setWidth(1000/12*indexes.length);
		}
		
		doGrid();
	}
	
	private void doGrid(){
		
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		ColumnConfig depsConfig = new ColumnConfig("name","",50);
		depsConfig.setSortable(false);
		depsConfig.setMenuDisabled(true);
		configs.add(depsConfig); 		
		
		
		
		for(int i=0; i>indexes.length;i++){
			if(indexes[i] != null){
				
				depsConfig = new ColumnConfig(indexes[i],names[i],50);
				depsConfig.setSortable(false);
				depsConfig.setMenuDisabled(true);
				configs.add(depsConfig);
				
				
			}
			
		}
		
		ColumnModel cm = new ColumnModel(configs); 
		
		Grid<BaseModel> grid = new Grid<BaseModel>(store, cm);
		grid.setLoadMask(true);
		grid.setWidth(width);
		grid.setHeight(205);
	    grid.addStyleName(".my-table-style");
	    grid.setBorders(true);
	    grid.setAutoExpandColumn("login");
	    grid.getView().setEmptyText("no data");
	    grid.setId("myid");
		
	    add(grid);
	}
	
	
	
	
	
	
	private void parseIndexes(String s){
		this.indexes = s.split("@");
	}
	
	private void parseNames(String s){
		this.names = s.split("@");
	}
	
}
