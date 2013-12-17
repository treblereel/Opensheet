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
import org.opensheet.client.dto.UserDTO;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {


  public void getUsers(String criteria,AsyncCallback<List<UserDTO>> callback);
  public void getUsersByBranch(String criteria,Integer selectedBranch,AsyncCallback<List<UserDTO>> callback);
  public void getUsersByBranchAndByDepartment(String criteria,Integer selectedBranch,Integer selectedDepartment,AsyncCallback<List<UserDTO>> callback);

  public void getUser(Integer id,AsyncCallback<UserDTO> callback);

  public void whoAmI(AsyncCallback<UserDTO> callback);

  public void getUsersByRole(String criteria,AsyncCallback<List<UserDTO>> callback);
  
  public void getUsersAccordingOfAskersRoleByStatusAndByBranch(String status, Integer id,AsyncCallback<List<UserDTO>> callback);
  
  public void getUsersPermissions(PagingLoadConfig loadConfig,AsyncCallback<PagingLoadResult<BaseModel>> callback);
  

  public void updateUser(UserDTO user, AsyncCallback<Void> asyncCallback);
  public void updateUserPermission(UserDTO user, AsyncCallback<Void> asyncCallback);

  public void addUser(UserDTO user, AsyncCallback<Void> asyncCallback);
  public void getUserPermission(UserDTO user, AsyncCallback<UserDTO> asyncCallback);

  public void getUsersFromExternalStore(AuthmethodDTO storeType,AsyncCallback<List<BaseModel>> asyncCallback);
  public void setUsersFromExternalStore(List<BaseModel> users,AuthmethodDTO storeType,AsyncCallback<Void> asyncCallback);
  
  public void setUserInternalRate(Integer userId,Integer rate,AsyncCallback<Void> callback);
  
  public void getUserSessionTimeout(AsyncCallback<Integer> callback);
  public void setLang(UserDTO userDTO,AsyncCallback<Void> callback);
}

