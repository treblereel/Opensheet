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
package org.opensheet.server.utils;

import java.util.HashMap;
import java.util.Map;

import org.opensheet.shared.model.Authmethod;

public class Parser {
	
	
	
	public static HashMap<String,String> parseAuthmethodAdData(Authmethod	authmethod){
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

}
