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
package org.opensheet.server.dao.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import org.hibernate.SessionFactory;
import org.opensheet.server.dao.HolidaysDAO;
import org.opensheet.shared.model.Holiday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class HolidaysDAOImpl implements HolidaysDAO{
	
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings({"static-access" })
	@Override
	@Transactional
	public HashMap<Integer,Boolean> getHolidays(Calendar cal){
		int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        HashMap<Integer, Boolean> hm = new HashMap<Integer, Boolean>();
        HashMap<Integer, Holiday> holidayMap = getHolidayMap(cal);
         
    	for(Integer i=1;i<=maxDay;i++){
    		cal.set(Calendar.getInstance().DAY_OF_MONTH,i);

    		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    		if(dayOfWeek == 1 || dayOfWeek == 7 ){
    			if(holidayMap.containsKey(cal.get(Calendar.DATE))){
    				hm.put(i,holidayMap.get(cal.get(Calendar.DATE)).getStatus());
    			}else{
    				hm.put(i,true);	
    			}
    		}else{
    			if(holidayMap.containsKey(cal.get(Calendar.DATE))){
    				hm.put(i,holidayMap.get(cal.get(Calendar.DATE)).getStatus());
    			}else{
    				hm.put(i,false);	
    			}
    		}
    	}
	

    	
		return hm;
	}

	
	@Override
	public Boolean checkDay(Calendar c) {
		Holiday h  = getDay(c);
		if(h.getStatus() == true){
			return true;
		}else{
			return false;
		}
		
	}

	@Override
	@Transactional
	public void saveOrUpdateDay(Calendar c, Boolean status) {
		
		
		Holiday h  = getDay(c);
		if(h != null){
			h.setStatus(status);
			sessionFactory.getCurrentSession().saveOrUpdate(h);
		}else{
			Holiday newHoliday = new Holiday();
			newHoliday.setDate(c.getTime());
			newHoliday.setStatus(status);
			sessionFactory.getCurrentSession().saveOrUpdate(newHoliday);

		}
	}

	
	@Override
	@Transactional
	public Holiday getDay(Calendar c) {

		Holiday h  =  (Holiday) sessionFactory.getCurrentSession().
		createQuery("from Holiday h where h.date=?")
		.setCalendar(0, c)
		.uniqueResult();
		
		return h;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional
	public HashMap<Integer, Holiday> getHolidayMap(Calendar c) {

		Integer maxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		Calendar firstDay = (Calendar) c.clone();
		firstDay.set(Calendar.DATE,1);
		Calendar lastDay = (Calendar) c.clone();
		lastDay.set(Calendar.DATE,maxDays);
		
		HashMap<Integer, Holiday> map = new HashMap<Integer, Holiday>();
		
		List<Holiday> list = (List<Holiday>) sessionFactory.getCurrentSession().
		createQuery("from Holiday h where h.date >= ? and h.date <= ?")
		.setCalendar(0, firstDay)
		.setCalendar(1, lastDay)
		.list();
		
		for(Holiday h: list){
			map.put(h.getDate().getDate(), h);	
		}
		
		return map;
	}

}
