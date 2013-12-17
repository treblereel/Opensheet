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

import java.util.List;

import org.opensheet.shared.model.Branch;

public interface BranchDAO {

	public Branch getBranchById(Integer id);
	public List<Branch> getBranchList(Boolean status);
	public void setBranch(Branch branch);
	public void addBranch(Branch branch);
	
}
