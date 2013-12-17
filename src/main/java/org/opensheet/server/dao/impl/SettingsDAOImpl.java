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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.SessionFactory;
import org.opensheet.server.dao.SettingsDAO;
import org.opensheet.shared.model.SystemVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class SettingsDAOImpl implements SettingsDAO {
	@Autowired
	private SessionFactory sessionFactory;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	
	
	
	@Override
	@Transactional
	public SystemVariable get(String key) {
		SystemVariable systemVariable = (SystemVariable)sessionFactory.
				getCurrentSession().createQuery("from SystemVariable v  where v.key=?")
				.setString(0, key)
				.uniqueResult();

		
		return systemVariable;
	}




	@SuppressWarnings("deprecation")
	@Override
	public HashMap<Boolean, Date> getTimeSheetInputMode(){
		SystemVariable systemVariable = this.get("timeSheetInputMode");
		
		String values =  systemVariable.getValue();
		String[] v = values.split("&");
		Boolean isEnabled = Boolean.valueOf(v[0]);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = formatter.parse(v[1]);
		} catch (ParseException e) {
			date  = new Date();
			date.setMonth(date.getMonth()+1);
		}
		HashMap<Boolean, Date> result = new HashMap<Boolean, Date>();
		result.put(isEnabled, date);
		return result;
	}




	@Override
	@Transactional
	public void setTimeSheetInputMode(HashMap<Boolean, Date> data){
		SystemVariable systemVariable = this.get("timeSheetInputMode");
		StringBuffer sb = new StringBuffer();
		for(Map.Entry<Boolean, Date> kv: data.entrySet()){
			sb.append(kv.getKey());
			sb.append("&");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			sb.append(formatter.format(kv.getValue()));
		}
		systemVariable.setValue(sb.toString());
		sessionFactory.getCurrentSession().saveOrUpdate(systemVariable);
	}
	
	
	

}
