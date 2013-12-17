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

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

import org.opensheet.client.services.SystemService;
import org.opensheet.server.dao.SettingsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@Service("SystemService")
public class SystemServiceImpl extends RemoteServiceServlet implements SystemService{

	@Autowired
	private SettingsDAO settingsDAO;
	
	
	private static final long serialVersionUID = -7449260299091392515L;

	@Override
	@Transactional
	public HashMap<Boolean, Date> getTimeSheetInputMode(){
		return settingsDAO.getTimeSheetInputMode();
		
	}

	@Override
	@Transactional
	public void setTimeSheetInputMode(HashMap<Boolean, Date> data){
		settingsDAO.setTimeSheetInputMode(data);
	}

}
