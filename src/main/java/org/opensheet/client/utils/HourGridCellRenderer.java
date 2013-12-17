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
package org.opensheet.client.utils;

import java.util.HashMap;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;


@SuppressWarnings("rawtypes")
public class HourGridCellRenderer implements GridCellRenderer{
	
	public void setHolidays(HashMap<Integer, Boolean> h){
	}
	
	@Override
	public Object render(ModelData model, String property,
			ColumnData config, int rowIndex, int colIndex,
			ListStore store, Grid grid) {
		
		
		/*
		if(Integer.parseInt(property) <= holidays.size()){
			if(holidays.get(Integer.parseInt(property))){
				config.css = "x-treegrid-column-holiday"; 
			}
		}
		*/
		config.css = "x-treegrid-column-holiday"; 

		if(model.get(property) == null){
			return	"<span style='color: green'></span>";
		} else{
			return	"<span style='color: green'>" + model.get(property) + "</span>";
		}
	}
	
	

}
