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
package org.opensheet.server.dao.impl;

import org.opensheet.server.dao.ImportUserFactory;
import org.opensheet.server.dao.ImportUserFromExternalSource;
import org.opensheet.shared.model.Authmethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ImportUserFactoryImpl extends ImportUserFactory{

@Autowired private	ImportUserFromExternalSource importUserFromExternalSource;	
	
	@Override
	public ImportUserFromExternalSource get(Authmethod authmethod) {
		
		if(authmethod.getType().equals("ad")){
		//	ImportUserFromExternalSource source = new ImportUserFromExternalSourceAD();
		//	source.setAuthmethod(authmethod);
		//	importUserFromExternalSource.setAuthmethod(authmethod);
			return importUserFromExternalSource;
		}
		
		
		return null;
	}

}
