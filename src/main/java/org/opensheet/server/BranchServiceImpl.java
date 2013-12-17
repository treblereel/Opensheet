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
import java.util.List;

import org.opensheet.client.dto.BranchDTO;
import org.opensheet.client.services.BranchService;
import org.opensheet.server.dao.BranchDAO;
import org.opensheet.shared.model.Branch;
import org.opensheet.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@Service("BranchService")
public class BranchServiceImpl extends RemoteServiceServlet implements BranchService{

	@Autowired BranchDAO branchDAO;
	
	private static final long serialVersionUID = 6477601228248166360L;

	
	@Secured({"ROLE_USER"})
	@Override
	@Transactional
	public BranchDTO getBranchById(Integer id) {
		Branch branch = branchDAO.getBranchById(id);
		BranchDTO branchDTO = new BranchDTO(branch);	
		branchDTO.setOwner(branch.getOwner().getUserDTO());
			return branchDTO;
	}

	@Secured({"ROLE_USER"})
	@Override
	@Transactional
	public List<BranchDTO> getBranchList(Boolean status) {
		List<Branch> branches = branchDAO.getBranchList(status);
		List<BranchDTO> brancheDTOs = new ArrayList<BranchDTO>(branches.size());
				for(Branch b: branches){
					brancheDTOs.add(new BranchDTO(b));
				}
		return brancheDTOs;
	}

	
	@Secured({"ROLE_ADMIN"})
	@Override
	@Transactional
	public void setBranch(BranchDTO branchDTO) {
		Branch b = new Branch(branchDTO);
		b.setOwner(new User(branchDTO.getOwner().getId()));
		branchDAO.setBranch(b);
	}

	@Secured({"ROLE_ADMIN"})
	@Override
	@Transactional
	public void addBranchDTO(BranchDTO branchDTO) {
		Branch b = new Branch(branchDTO);
		b.setOwner(new User(branchDTO.getOwner().getId()));
		
		branchDAO.addBranch(b);
		
	}

	/**Return List of Branch DTO's for BranchComboBox
 	 * 
	 * @param
	 *  status true - get active branch's, false - inactive 
	 *  
	 * @param 
	 *  all   true to add "All" value.
	 *  @return List BranchDTO
	 */
	
	@Secured({"ROLE_USER"})
	@Override
	@Transactional
	public List<BranchDTO> getBranchListForCombo(Boolean status, Boolean all) {
		List<BranchDTO> list = getBranchList(status);
		if(list == null){
			return Collections.emptyList();
		}
		
		if(all == true){
			BranchDTO b = new BranchDTO(9999999);
			b.setName("All");
			list.add(0,b);
		}
		return list;
	}

}
