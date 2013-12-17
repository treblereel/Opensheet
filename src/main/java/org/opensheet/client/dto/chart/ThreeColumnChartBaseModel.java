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
package org.opensheet.client.dto.chart;

import com.extjs.gxt.ui.client.data.BaseModel;

public class ThreeColumnChartBaseModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7563749577925792453L;

	
	public ThreeColumnChartBaseModel(){
	}
	
	public ThreeColumnChartBaseModel(String date,Integer alpha,Integer beta,Integer gamma){
		setDate(date);
		setAlpha(alpha);
		setBeta(beta);	
		setGamma(gamma);
	}
	
	
	
	
	public void setDate(String date) {
	    set("date", date);
	}
	
	public void setAlpha(Integer alpha) {
	    set("alpha", alpha);
	}
	
	public void setBeta(Integer beta) {
	    set("beta", beta);
	}
	
	public void setGamma(Integer gamma) {
	    set("gamma", gamma);
	}
	
	

	 public int getAlpha() {
		    return get("alpha") == null ? 0 : (Integer) get("alpha");
	}

	 public int getBeta() {
		    return get("beta") == null ? 0 : (Integer) get("beta");
	 }

	 public int getGamma() {
		    return get("gamma") == null ? 0 : (Integer) get("gamma");
	  }

	 public String getDate() {
		    return (String) get("date");
	}
}
