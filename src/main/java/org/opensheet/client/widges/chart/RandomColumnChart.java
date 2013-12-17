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

import java.util.List;

import org.opensheet.client.utils.Colors;


import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.charts.client.model.BarDataProvider;
import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.Legend;
import com.extjs.gxt.charts.client.model.Legend.Position;
import com.extjs.gxt.charts.client.model.ScaleProvider;
import com.extjs.gxt.charts.client.model.charts.BarChart;
import com.extjs.gxt.charts.client.model.charts.BarChart.BarStyle;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.store.ListStore;

public class RandomColumnChart extends Chart{
	private ChartModel model;
	private ListStore<BaseModel> store;
	private BarChart tempBar;
	private String[] indexes;
	private String[] names;
	private Integer width = 1024;
	
	public RandomColumnChart(String url,List<BaseModel> s) {
		super(url);
		configure(s);
		
	}
	
	private void configure(List<BaseModel> s){
		setHeight(205);
		this.setWidth(width);
		store = new ListStore<BaseModel>();
		store.add(s);
		
		if(s.size() > 12 ){
			this.setWidth(1000/12*s.size());
		}
		
		parseIndexes(s.get(0).get("indexes").toString());
		parseNames(s.get(0).get("names").toString());
		setModel();
		

	}
	
	private void setModel(){
		model = new ChartModel();
		model.setBackgroundColour("#fefefe");  
	    model.setLegend(new Legend(Position.TOP, true));  
	    model.setScaleProvider(ScaleProvider.ROUNDED_NEAREST_SCALE_PROVIDER);  	
		
	    
	    for(int i=0;i<indexes.length;i++){
		    tempBar = new BarChart(BarStyle.GLASS);  
		    tempBar.setColour(Colors.getRandom());
		    tempBar.setText(names[i]);
		    BarDataProvider barProvider;
		    if(i == 0){
		    	barProvider = new BarDataProvider(indexes[i],"date");  
		    }else{
			    barProvider = new BarDataProvider(indexes[i]);  

		    }
		    barProvider.bind(store);  
		    tempBar.setDataProvider(barProvider);  
		    model.addChartConfig(tempBar);  
	    }
	   
	    
	    model.addChartConfig();
	    
	    
	    setChartModel(model);  
	    
	}
	
	private void parseIndexes(String s){
		this.indexes = s.split("@");
	}
	
	private void parseNames(String s){
		this.names = s.split("@");
	}
	
	
	

}
