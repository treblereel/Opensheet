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
package org.opensheet.server;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import org.opensheet.client.dto.AuthmethodDTO;
import org.opensheet.client.dto.UserDTO;
import org.opensheet.client.dto.UserRateDTO;
import org.opensheet.client.services.UserService;
import org.opensheet.server.dao.AuthDAO;
import org.opensheet.server.dao.ImportUserFactory;
import org.opensheet.server.dao.ImportUserFromExternalSource;
import org.opensheet.server.dao.PermissionDAO;
import org.opensheet.server.dao.UserDAO;
import org.opensheet.server.utils.Comparators;
import org.opensheet.shared.model.Authmethod;
import org.opensheet.shared.model.Branch;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.User;
import org.opensheet.shared.model.Permission;
import org.opensheet.shared.model.UserRate;



import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


@Service("UserService")
public class  UserServiceImpl extends RemoteServiceServlet implements UserService {

	@Autowired    private UserDAO userDAO;
	
	@Autowired 	private PermissionDAO permissionDAO;
	
	@Autowired 	private AuthDAO authDAO;
	
	@Autowired ImportUserFactory importUserFactory;
	
	@Autowired 	private ImportUserFromExternalSource importUserFromExternalSource;
	
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	
	/*@TODO
	 * Use Hibernate util to fetch lazy entetys
	 * 
	 * 
	 * (non-Javadoc)
	 * @see org.opensheet.client.services.UserService#getUsers(java.lang.String)
	 */
				@Override
				@Transactional
			  public List<UserDTO> getUsers(String s) {
					//shitty code
					Map<Integer,Authmethod> authmethods = new HashMap<Integer,Authmethod>();
					
					for(Authmethod a : authDAO.get()){
						authmethods.put(a.getId(), a);
					}
					
					
					List<User> users = userDAO.listUser(s);
					List<UserDTO> usersDTO = new ArrayList<UserDTO>(users != null ? users.size() : 0);
					for(User u : users ){
						UserDTO userDTO = new UserDTO(u);
						userDTO.setDepartment(u.getDepartment().getDepartmentDTO());
						userDTO.setAuthmethod(new AuthmethodDTO(authmethods.get(u.getAuthmethod())));
						usersDTO.add(userDTO);
						
					}
					return usersDTO;	
			
			  }
				
					@Override
					@Transactional
				  public List<UserDTO> getUsersByRole(String s) {
					List<User> users = userDAO.getUsersByRole(s);
					List<UserDTO> usersDTO = new ArrayList<UserDTO>(users != null ? users.size() : 0);
					for(User u : users ){
						UserDTO userDTO = new UserDTO(u);
						userDTO.setPermission(u.getPermission().getPermissionDTO());
						usersDTO.add(userDTO);
					}
					return usersDTO;
				  }	

				
			







				@Override
				public void updateUser(UserDTO userDTO) {
					User user = new User(userDTO);
					UserRate userRate = new UserRate(userDTO.getUserRateDTO());
					user.setAuthmethod(userDTO.getAuthmethod().getId());
					user.setDepartment(userDTO.getDepartment().getDepartment());
					user.setBranch(new Branch(userDTO.getBranch()));
					user.setUserRate(userRate);
					userDAO.updateUser(user);
				}
				
				
				
				@Override
				@Transactional
				public UserDTO getUser(Integer id) {
					User user = userDAO.getUser(id);
					UserDTO userDTO = new UserDTO(user);
					userDTO.setAuthmethod(new AuthmethodDTO(user.getAuthmethod()));
					userDTO.setDepartment(user.getDepartment().getDepartmentDTO());
					userDTO.setUserRateDTO(new UserRateDTO(user.getUserRate()));
					return userDTO;
				}
				
				
				
				@Override
				@Transactional
				public void updateUserPermission(UserDTO userDTO) {
					Permission permission  = new Permission();
					permission.setAdmin(userDTO.getPermission().getAdmin());
					permission.setPm(userDTO.getPermission().getPm());
					permission.setFd(userDTO.getPermission().getFd());
					permission.setDm(userDTO.getPermission().getDm());
					User user = new User(userDTO);
					user.setPermission(permission);
					permissionDAO.updateUserPermission(user);
				}
				
				

				@Override
				public void addUser(UserDTO userDTO) {
					User user = new User(userDTO);
					user.setDepartment(userDTO.getDepartment().getDepartment());
					user.setBranch(new Branch(userDTO.getBranch()));
					userDAO.addUser(user);
				}
				
				
				

				
				@Override
				@Transactional
				public PagingLoadResult<BaseModel> getUsersPermissions(PagingLoadConfig config) {
					Integer size = userDAO.listUser("1").size();
					List<User> users = permissionDAO.getUsersPermissions(config);
					List<UserDTO> usersDTO = new ArrayList<UserDTO>(users != null ? users.size() : 0);
					List<BaseModel> bm = new ArrayList<BaseModel>(users != null ? users.size() : 0);
					
					for(User u: users){
						BaseModel b = new BaseModel();
						b.set("fullName", u.getFullName());
						b.set("id", u.getId());
						b.set("pm", u.getPermission().getPm());
						b.set("dm", u.getPermission().getDm());
						b.set("fd", u.getPermission().getFd());
						b.set("admin", u.getPermission().getAdmin());
						bm.add(b);
					}
					int start = config.getOffset();
				    int limit = usersDTO.size();
				    
				    if (config.getLimit() > 0) {
				      limit = Math.min(start + config.getLimit(), limit);
				    }
				    return new BasePagingLoadResult<BaseModel>(bm, config.getOffset(), size);
				}

				
				
				@Override
				public UserDTO getUserPermission(UserDTO user) {
					return null;
				}

				@Override
				@Transactional
				public  UserDTO whoAmI() {
					User user = userDAO.whoIam();
					UserDTO userDTO = new UserDTO(user);
					userDTO.setPermission(user.getPermission().getPermissionDTO());
					return userDTO;
				}

				
				@Override
				@Transactional
				public List<UserDTO> getUsersAccordingOfAskersRoleByStatusAndByBranch(String status, Integer branch) {
					User user = userDAO.whoIam();
					List<UserDTO> usersDTO;
					
					if(user.getPermission().getAdmin() == true){
						return this.getUsersByBranch(status, branch);
					}else if(user.getPermission().getDm() == true){
						List<User> users = new ArrayList<User>(0);
						for(Department d:   userDAO.getManagedDepartments(user)){
							if(branch == 9999999){
								for(User u: d.getUsers()){
									if(status.equals("any")){
										if(u.getStatus()== true && users.contains(u) == false){
											users.add(u);
										}
									}else if(status.equals("1")){
										if(u.getStatus()== true && users.contains(u) == false){
											users.add(u);
										}
									}else if(status.equals("0")){
										if(u.getStatus()== false && users.contains(u) == false){
											users.add(u);
										}
									}
								}
							}else if(d.getBranch().getId() == branch){
								for(User u: d.getUsers()){
									if(status.equals("any")){
										if(u.getStatus()== true && users.contains(u) == false){
											users.add(u);
										}
									}else if(status.equals("1")){
										if(u.getStatus()== true && users.contains(u) == false){
											users.add(u);
										}
									}else if(status.equals("0")){
										if(u.getStatus()== false && users.contains(u) == false){
											users.add(u);
										}
									}
								}
							}
						}
						
						/*add User to the list of employers */
						if(users.contains(user) == false)
							users.add(user);
						
						
						usersDTO = new ArrayList<UserDTO>(users.size());
						Collections.sort(users,Comparators.ComparatorUserName);	
						
						for(User u: users){
							usersDTO.add(new UserDTO(u));
						}

						return usersDTO;
					}
					return Collections.emptyList();
				}
				
				
				

				@Override
				public List<BaseModel> getUsersFromExternalStore(AuthmethodDTO storeType) {
					Authmethod authmethod = authDAO.get(storeType.getId());
					List<BaseModel> usersDTO = new ArrayList<BaseModel>();
					
					List<User> users = importUserFromExternalSource.get(authmethod);
					Collections.sort(users,Comparators.ComparatorUserName);
					
					for(User u: users){
						BaseModel bm = new BaseModel();
						bm.set("fullName",u.getFullName());
						bm.set("giventName", u.getFirstName());
						bm.set("sn", u.getSecondName());
						bm.set("login", u.getLogin());
						bm.set("email",u.getEmail() );
						usersDTO.add(bm);
					}
				 return usersDTO;
				}

				@Override
				public void setUsersFromExternalStore(List<BaseModel> users,AuthmethodDTO storeType) {
					for(BaseModel bm: users){
						User u = new User();
						u.setLogin(bm.get("login").toString());
						u.setFirstName(bm.get("giventName").toString());
						u.setSecondName(bm.get("sn").toString());
						u.setEmail(bm.get("email").toString());
						u.setDepartment(new Department(1));
						u.setAuthmethod(storeType.getId());
						u.setBranch(new Branch(1));
						userDAO.addUser(u);
					}
				}

				
				@Override
			    public Integer getUserSessionTimeout() {
					ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
					HttpSession session = attr.getRequest().getSession(false);
					int timeout = 	session.getMaxInactiveInterval() * 1000;
			        return timeout;
			    }

				@Override
				@Transactional
				public List<UserDTO> getUsersByBranch(String s,Integer branch) {
					List<User> users = userDAO.getUsersByBranch(s, branch);
					List<UserDTO> usersDTO = new ArrayList<UserDTO>(users != null ? users.size() : 0);
					for(User u : users ){
						UserDTO userDTO = new UserDTO(u);
						userDTO.setDepartment(u.getDepartment().getDepartmentDTO());
						usersDTO.add(userDTO);
					}
					return usersDTO;
				}

				
				
				@Override
				public List<UserDTO> getUsersByBranchAndByDepartment(String criteria, Integer branch,
						Integer selectedDepartment) {
					
					List<User> users = userDAO.getUsersByBranchAndByDepartment(criteria, branch,selectedDepartment);
					List<UserDTO> usersDTO = new ArrayList<UserDTO>(users != null ? users.size() : 0);
					for(User u : users ){
						UserDTO userDTO = new UserDTO(u);
						userDTO.setDepartment(u.getDepartment().getDepartmentDTO());
						usersDTO.add(userDTO);
					}
					return usersDTO;

				}

				@Override
				public void setLang(UserDTO userDTO) {
					User user = new User(userDTO);
					userDAO.setLang(user);
					
				}

				@Override
				public void setUserInternalRate(Integer userId, Integer rate) {
					User user  = new User(userId);
					userDAO.setUserInternalRate(user,rate);
				}

				
				
}
