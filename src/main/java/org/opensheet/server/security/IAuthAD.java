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
package org.opensheet.server.security;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.opensheet.shared.model.Authmethod;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class IAuthAD implements IAuth{
	
	private CustomUser user;
	private String password;
	private static DirContext dctx;
	private Authmethod authmethod;
	
	
	@Override
	@Transactional
	public Boolean doAuth(CustomUser user, String password,Authmethod authmethod) {
		this.user = user;
		this.password = password;
		this.authmethod= authmethod;

		
		try{
		    dctx = new InitialDirContext(prepairAuthValues(authmethod));
		    dctx.close();
		    return true;
	    } catch(Exception e){
		    	e.getStackTrace();
		    	return false;
	    }
		    
	}
	
	
	private Hashtable<String,String> prepairAuthValues(Authmethod	authmethod){
		Map<String,String> authData = parse(authmethod);

		String bindUser = 	user.getUsername();
		String domain   = 	authData.get("domain");
	    String Bindpasswd = password;
	    String ldapUrl  = 	authData.get("url");
	    String baseCn   = 	authData.get("basecn");
	    String sp       =	"com.sun.jndi.ldap.LdapCtxFactory";
		

	    
	    Hashtable<String,String> env = new Hashtable<String,String>();
	    
	    env.put(Context.INITIAL_CONTEXT_FACTORY,sp);
	    env.put(Context.SECURITY_AUTHENTICATION, "simple");
	    env.put(Context.SECURITY_PRINCIPAL,bindUser + "@" + domain);
	    env.put(Context.SECURITY_CREDENTIALS, Bindpasswd);
	    env.put(Context.PROVIDER_URL, ldapUrl);
	    return env;
	}
	
	
	
	
	private HashMap<String,String> parse(Authmethod	authmethod){
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
