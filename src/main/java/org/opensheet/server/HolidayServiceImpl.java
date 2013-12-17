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
package org.opensheet.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.opensheet.client.services.HolidayService;
import org.opensheet.server.dao.HolidaysDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


@Service("HolidayService")
public class HolidayServiceImpl extends RemoteServiceServlet implements HolidayService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 145945755813482192L;

	@Autowired 	private HolidaysDAO holidays;
	
	
	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	@Override
	public List<BaseModelData> getHolidays(Date date) {

		int weeks;
		int firstWeek;
		int lastWeek;
		
		
		
		Calendar cal = new GregorianCalendar();
	    cal.set(Calendar.YEAR, date.getYear()+1900);
	    cal.set(Calendar.MONTH, date.getMonth());
	    


	    cal.set(Calendar.DAY_OF_MONTH, 1);

	  
	    int firstWeekDayOfFirstDay = cal.get(Calendar.DAY_OF_WEEK);
	    int row = 1;
	    firstWeekDayOfFirstDay--;
	   
	    firstWeek = cal.get(Calendar.WEEK_OF_YEAR);
	    cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
	    lastWeek = cal.get(Calendar.WEEK_OF_YEAR);
	    weeks = lastWeek  - firstWeek;
	    	if(weeks < 0 ){
	    		weeks=5;
	    	}
	    weeks++;
	    
	    
	    Map<Integer,Map<Integer,Integer>> weeksMap = new HashMap<Integer,Map<Integer,Integer>>(weeks);
	    HashMap<Integer,Boolean> holidaysMap = holidays.getHolidays(cal);
	    for(int w =1;w<=weeks;w++){
	    	Map<Integer,Integer>  weekMapTemp = new HashMap<Integer,Integer>(7);	
	    	for(int temp = 1; temp<=7;temp++){
	    		weekMapTemp.put(temp, 0);
		    }
	    	weeksMap.put(w, weekMapTemp);	
	    }

	    
	    
	    
		int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int step = firstWeekDayOfFirstDay;
			if(step == 0) step = 7;
		
		
	    for(int day = 1; day<=maxDays; day++){
	    	cal.set(Calendar.DAY_OF_MONTH, day);
	    	Map<Integer,Integer> wee = weeksMap.get(row);
	    	wee.put(step, day);
	    	weeksMap.put(row, wee);
	    	if(step == 7 ){
    			row++;
    			step = 1;
	    	}else{
    			step++;
    		}
	    	
	    }

		List<BaseModelData> list = new ArrayList<BaseModelData>(weeksMap.size());
	
	    Set s = weeksMap.entrySet();
	    Iterator iter = s.iterator();
	    while(iter.hasNext()){
	    	Map.Entry m =(Map.Entry) iter.next();
	    	BaseModelData holidayBaseModel = new BaseModelData();
	    	holidayBaseModel.set("week", m.getKey());
	    	Set ss =    weeksMap.get(m.getKey()).entrySet();
	    	Iterator weekIter = ss.iterator();
	    	while(weekIter.hasNext()){
	    		Map.Entry<Integer,Integer> weekM = (Entry<Integer, Integer>) weekIter.next();
	    		holidayBaseModel.set("day_"+weekM.getKey(), weekM.getValue());
	    		holidayBaseModel.set("id", weekM.getValue());
	    		holidayBaseModel.set("status_"+weekM.getKey(), holidaysMap.get(weekM.getValue()));
	    	}
	    	list.add(holidayBaseModel);
	    }
		return list;
	}


	@Override
	public List<BaseModelData> setAndUpdateHolidays(Date date,Boolean status) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		holidays.saveOrUpdateDay(cal, status);
		return this.getHolidays(date);
	}

}
