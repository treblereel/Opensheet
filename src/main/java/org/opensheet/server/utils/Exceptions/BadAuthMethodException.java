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
package org.opensheet.server.utils.Exceptions;

public class BadAuthMethodException extends Exception {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BadAuthMethodException() {
	  }

	  public BadAuthMethodException(String msg) {
	    super(msg);
	  }
	}
