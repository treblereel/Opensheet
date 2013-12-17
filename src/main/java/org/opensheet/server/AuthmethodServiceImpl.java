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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.opensheet.client.dto.AuthmethodDTO;
import org.opensheet.client.services.AuthmethodService;
import org.opensheet.server.dao.AuthDAO;
import org.opensheet.shared.model.Authmethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@Service("AuthmethodService")
public class AuthmethodServiceImpl extends RemoteServiceServlet implements AuthmethodService{

	@Autowired private AuthDAO authmethodDAO;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Secured({"ROLE_ADMIN"})
	@Override
	public List<AuthmethodDTO> getAuthMethods() {
		
		List<Authmethod> authmethods =   (List<Authmethod>) authmethodDAO.get();
		List<AuthmethodDTO> authmethodDTOs = new ArrayList<AuthmethodDTO>(authmethods != null ? authmethods.size() : 0);
		for(Authmethod a: authmethods){
			authmethodDTOs.add(a.getAuthmethodSimpleDTO());
		}
		return authmethodDTOs;
		

	}

	/**
	 * @param 
	 * authMethodId id of auth metod
	 * detailed true to get additional values of current authmethod
	 */
	@Secured({"ROLE_ADMIN"})
	@Override
	public BaseModel get(Integer authMethodId,Boolean detailed) {
		
		Authmethod	authmethod  = authmethodDAO.get(authMethodId);
		BaseModel result = new BaseModel();
		result.set("id", authmethod.getId());
		result.set("type", authmethod.getType());
		result.set("description", authmethod.getDescription());
		result.set("scannable", authmethod.getScannable());
		
		Map<String,String> data = parse(authmethod);
		if(detailed == true){
			Set<Entry<String, String>> set = data.entrySet();
			Iterator<Entry<String, String>> i = set.iterator();
			while(i.hasNext()){
			      Map.Entry<String, String> me = (Map.Entry<String, String>)i.next();
			      result.set(me.getKey(),me.getValue());
			    }
		}
		return result;
	}


	@Secured({"ROLE_ADMIN"})
	@Override
	public void set(BaseModel authmethodBaseModel) {
		
		StringBuffer data = new StringBuffer();

		data.append(authmethodBaseModel.get("binduser"));
		data.append("#");
		data.append(authmethodBaseModel.get("domain"));
		data.append("#");
		data.append(authmethodBaseModel.get("bindpasswd"));
		data.append("#");
		data.append(authmethodBaseModel.get("url"));
		data.append("#");
		data.append(authmethodBaseModel.get("basecn"));

		
		
		Authmethod	authmethod  = authmethodDAO.get(Integer.parseInt(authmethodBaseModel.get("id").toString()));
		authmethod.setDescription(authmethodBaseModel.get("description").toString());
		authmethod.setData(data.toString());
		
		authmethodDAO.set(authmethod);
	}

	
	private HashMap<String,String> parse(Authmethod	authmethod){
		Map<String,String> answer = new HashMap<String,String>();
		if(authmethod.getType().equals("ad")){
			return	parseAd(authmethod);
		}
		
		return (HashMap<String, String>) answer;
	}
	
	private HashMap<String,String> parseAd(Authmethod	authmethod){
		Map<String,String> answer = new HashMap<String,String>();
		String data = 	authmethod.getData();
		String[] val = data.split("#"); 
		answer.put("binduser", val[0]);
		answer.put("domain", val[1]);
		answer.put("bindpasswd", val[2]);
		answer.put("url", val[3]);
		answer.put("basecn", val[4]);
		
		
		
		return (HashMap<String, String>) answer;
		
	}

	@Secured({"ROLE_ADMIN"})
	@Override
	public List<AuthmethodDTO> getImportSources() {
		List<Authmethod> authmethods =   (List<Authmethod>) authmethodDAO.get();
		List<AuthmethodDTO> authmethodDTOs = new ArrayList<AuthmethodDTO>(authmethods != null ? authmethods.size() : 0);
		for(Authmethod a: authmethods){
			if(a.getScannable() == true)
				authmethodDTOs.add(a.getAuthmethodSimpleDTO());
		}
		return authmethodDTOs;
	}
	
}
