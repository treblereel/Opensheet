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
package org.opensheet.client.services;

import java.util.List;

import org.opensheet.client.dto.AuthmethodDTO;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthmethodServiceAsync {
	 public void  getAuthMethods(AsyncCallback<List<AuthmethodDTO>> asyncCallback);
	 public void  getImportSources(AsyncCallback<List<AuthmethodDTO>> asyncCallback);
	 public void  get(Integer authMethodId,Boolean detailed,AsyncCallback<BaseModel> asyncCallback);
	 public void  set(BaseModel authmethod,AsyncCallback<Void> asyncCallback);
}


