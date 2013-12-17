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
package org.opensheet.client.dto.grid;

import java.io.Serializable;
import com.extjs.gxt.ui.client.data.BaseTreeModel;

public class Folder extends BaseTreeModel implements Serializable {

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int ID = 0;
	  
	  public Folder() {
	    set("id", ID++);
	  }

	  public Folder(String name) {
	    set("id", ID++);
	    set("name", name);
	  }

	  public Folder(String name, BaseTreeModel[] children) {
	    this(name);
	    for (int i = 0; i < children.length; i++) {
	      add(children[i]);
	    }
	  }

	  public Integer getId() {
	    return (Integer) get("id");
	  }

	  public String getName() {
	    return (String) get("name");
	  }

	  public String toString() {
	    return getName();
	  }

	}
