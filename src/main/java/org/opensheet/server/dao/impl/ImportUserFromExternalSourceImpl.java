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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.opensheet.server.dao.ImportUserFromExternalSource;
import org.opensheet.server.dao.UserDAO;
import org.opensheet.server.utils.Parser;
import org.opensheet.shared.model.Authmethod;
import org.opensheet.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class ImportUserFromExternalSourceImpl implements ImportUserFromExternalSource{
	@Autowired
	private UserDAO userDAO;
	private Authmethod authmethod;
	private static DirContext dctx;
	private List<User> users;

	
	
	@Override
	public List<User> get(Authmethod authmethod) {
		this.authmethod = authmethod;
		
		if(authmethod.getType().equals("ad")){
			return doAD();
		}
		
		return null;
	}

	


	
	private List<User> compareUsersOpensheetToAD(List<User> usersFromAD){
		List<User> usersDiff = new ArrayList<User>();
		Map<String,User> usersFromOpensheet = new HashMap<String,User>();
		
		
		for(User u: userDAO.listUser("any"))
			usersFromOpensheet.put(u.getLogin().toLowerCase(),u);
		
				
				
		for(User u: usersFromAD){
			if(!usersFromOpensheet.containsKey(u.getLogin().toLowerCase())){
				usersDiff.add(u);
			}
		}
		
		
		return usersDiff;
	}
	
	private List<User>  doAD(){
		users = new ArrayList<User>();
	
		Map<String,String> authData = Parser.parseAuthmethodAdData(authmethod);

		String bindUser = 	authData.get("binduser");
		String domain   = 	authData.get("domain");
	    String Bindpasswd = authData.get("bindpasswd");
	    String ldapUrl  = 	authData.get("url");
	    String baseCn   = 	authData.get("basecn");
	    String sp       =	"com.sun.jndi.ldap.LdapCtxFactory";
		

	    
	    Hashtable<String,String> env = new Hashtable<String,String>();
	    
	    env.put(Context.INITIAL_CONTEXT_FACTORY,sp);
	    env.put(Context.SECURITY_AUTHENTICATION, "simple");
	    env.put(Context.SECURITY_PRINCIPAL,bindUser + "@" + domain);
	    env.put(Context.SECURITY_CREDENTIALS, Bindpasswd);
	    env.put(Context.PROVIDER_URL, ldapUrl);
		
		
		
		
		
		
		try{
		    dctx = new InitialDirContext(env);
		SearchControls sc = new SearchControls();
	    
	    String[] attributeFilter = {"cn","mail","sn","givenName"};
	    
	    sc.setReturningAttributes(attributeFilter);
	    sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

	    String filter = "(&(objectClass=user)(objectCategory=person)(!(userAccountControl:1.2.840.113556.1.4.803:=2)))";
	    NamingEnumeration<SearchResult> results = dctx.search(baseCn, filter, sc);
	    while (results.hasMore()) {
	    	String sn = "",cn= "",mail= "",givenName = "";
	    	
	    	
	    	
	      SearchResult sr = (SearchResult) results.next();
		      Attributes attrs = sr.getAttributes();
		      
		      Attribute attr = attrs.get("cn");
		      	cn = (String) attr.get();
		      
		      attr = attrs.get("givenName");
		      if(attr != null)
		    	  givenName = (String) attr.get();
		      
		      attr = attrs.get("sn");
		      if(attr != null)
		    	  sn = (String) attr.get();
		     
		      attr = attrs.get("mail");
		      if(attr != null)
		    	  mail = (String) attr.get();
		     
		  
		    
	     
	      User user = new User();
	      user.setFirstName(givenName);
	      user.setSecondName(sn);
	      user.setLogin(cn.toLowerCase());
	      user.setEmail(mail);
	      user.setAuthmethod(authmethod.getId());
	      
	      
	      users.add(user);
	    }
		    dctx.close();
		   
	    } catch(Exception e){
		    	e.getStackTrace();
		    	
	    }
	
	return compareUsersOpensheetToAD(users);
	}
	

}
