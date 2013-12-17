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

public class ThreeColumnChart extends Chart{
	private ChartModel model;
	private ListStore<BaseModel> store;
	private BarChart barAlpha,barBeta,barGamma;

	
	public ThreeColumnChart(String url,List<BaseModel> s) {
		super(url);
		configure(s);
	}
	
	private void configure(List<BaseModel> s){
		setHeight(220);
		store = new ListStore<BaseModel>();
		store.add(s);
		setModel();
		
	}
	
	private void setModel(){
		model = new ChartModel();
		model.setBackgroundColour("#fefefe");  
	    model.setLegend(new Legend(Position.TOP, true));  
	    model.setScaleProvider(ScaleProvider.ROUNDED_NEAREST_SCALE_PROVIDER);  	
		
	    barAlpha = new BarChart(BarStyle.GLASS);  
	    barAlpha.setColour("#00aa00");
	    barAlpha.setText("alpha");
	    BarDataProvider barProvider = new BarDataProvider("alpha", "date");  
	    barProvider.bind(store);  
	    barAlpha.setDataProvider(barProvider);  
	    model.addChartConfig(barAlpha);  
	  
	    barBeta = new BarChart(BarStyle.GLASS);  
	    barBeta.setColour("#0000cc");  
	    barProvider = new BarDataProvider("beta");  
	    barProvider.bind(store);  
	    barBeta.setDataProvider(barProvider);  
	    model.addChartConfig(barBeta);  
	  
	    barGamma = new BarChart(BarStyle.GLASS);  
	    barGamma.setColour("#ff6600");  
	    barProvider = new BarDataProvider("gamma");  
	    barProvider.bind(store);  
	    barGamma.setDataProvider(barProvider);  
	    model.addChartConfig(barGamma);
	    
	    
	    model.addChartConfig();
	    
	    
	    setChartModel(model);  
	    
	}
	
	
	public void setTitle(String[] titles){
		barAlpha.setText(titles[0]);
		barBeta.setText(titles[1]);
		barGamma.setText(titles[2]);
	}
	

}
