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

import org.opensheet.client.dto.BranchDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service/BranchService")
public interface BranchService  extends RemoteService{
	
	public BranchDTO getBranchById(Integer id);
	public List<BranchDTO> getBranchList(Boolean status);
	public List<BranchDTO> getBranchListForCombo(Boolean status,Boolean all);
	public void setBranch(BranchDTO branchDTO);
	public void addBranchDTO(BranchDTO branchDTO);

	
	
}
