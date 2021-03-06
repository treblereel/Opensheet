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
package org.opensheet.server.dao;

import org.opensheet.server.security.IAuth;
import org.opensheet.server.utils.Exceptions.BadAuthMethodException;

public abstract class AuthFactory {
	public abstract  IAuth get(Integer authId)  throws BadAuthMethodException;
}




