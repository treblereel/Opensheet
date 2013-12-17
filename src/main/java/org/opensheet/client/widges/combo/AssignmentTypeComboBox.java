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
package org.opensheet.client.widges.combo;

import java.util.List;

import org.opensheet.client.l10n.OpensheetConstants;
import org.opensheet.client.utils.AssignmentTypes;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.core.client.GWT;

public class AssignmentTypeComboBox extends ComboBox<AssignmentTypes>{
	private OpensheetConstants myConstants = (OpensheetConstants) GWT.create(OpensheetConstants.class);

	public  AssignmentTypeComboBox(){
		
		List<AssignmentTypes> list = AssignmentTypes.get();  
        final ListStore<AssignmentTypes>  store = new ListStore<AssignmentTypes>();  
        store.add(list);  
		
		
		setTriggerAction(TriggerAction.ALL);  
        setEditable(false);  
        setWidth(100);
        setDisplayField("name");
        setName("name");
        setFieldLabel( myConstants.type());
        setStore(store);
        setAllowBlank(false);
        
        this.setValue(list.get(0));
	}

	

	
}
