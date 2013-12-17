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
package org.opensheet.client.exceptions;

import java.io.Serializable;

public class PermissionDeniedException extends Exception implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 private String symbol;

	  public PermissionDeniedException() {
	  }

	  public PermissionDeniedException(String symbol) {
	    this.symbol = symbol;
	  }

	  public String getMessage() {
	    return this.symbol;
	  }

}
