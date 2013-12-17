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
import org.springframework.security.access.annotation.Secured;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("service/UserService")

public interface  UserService extends RemoteService {


  @Secured("ROLE_USER")
  public  List<UserDTO> getUsers(String criteria);
  @Secured("ROLE_ADMIN")
  public  List<UserDTO> getUsersByBranch(String criteria,Integer selectedBranch);
  public  List<UserDTO> getUsersByBranchAndByDepartment(String criteria,Integer selectedBranch,Integer selectedDepartment);

  public  UserDTO getUser(Integer id);
  
  public  List<UserDTO> getUsersAccordingOfAskersRoleByStatusAndByBranch(String status, Integer id);
  
  @Secured("ROLE_USER")
  public  UserDTO whoAmI();
  
  public  List<UserDTO> getUsersByRole(String criteria);
  public  PagingLoadResult<BaseModel> getUsersPermissions(PagingLoadConfig loadConfig);

  public void addUser(UserDTO user);
  public void updateUser(UserDTO user);
  public void updateUserPermission(UserDTO user);
  public UserDTO getUserPermission(UserDTO user);
  
  public  List<BaseModel> getUsersFromExternalStore(AuthmethodDTO storeType);
  public void setUsersFromExternalStore(List<BaseModel> users,AuthmethodDTO storeType);
  
  public void setUserInternalRate(Integer userId,Integer rate);
  
  public Integer getUserSessionTimeout();
  public void setLang(UserDTO userDTO);

}
