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

public class Hash {
	
	public static String md5(String input) {
		String md5 = "";
		
        try {
            StringBuffer code = new StringBuffer(); 
            java.security.MessageDigest messageDigest =  java.security.MessageDigest.getInstance("MD5");
            byte bytes[] = input.getBytes();
            byte digest[] = messageDigest.digest(bytes);
            
            for (int i = 0; i < digest.length; ++i) {
                code.append(Integer.toHexString(0x0100 + (digest[i] & 0x00FF)).substring(1));
            }
            
            md5 = code.toString();
        } catch(Exception e) { }
        
        return md5;
    }

}
