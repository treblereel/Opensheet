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

import org.opensheet.server.utils.Hash;
import org.opensheet.shared.model.Authmethod;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;

public class IAuthDbPasswd implements IAuth {

 private PasswordEncoder passwordEncoder = new PlaintextPasswordEncoder();
 
	
	@Override
	public Boolean doAuth(CustomUser user,String password,Authmethod authmethod) {
		password = Hash.md5(password);

		return passwordEncoder.isPasswordValid(user.getPassword(),password,null);
	}

}
