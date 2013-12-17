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
package org.opensheet.client.l10n;

import com.google.gwt.i18n.client.Messages;

public interface OpensheetMessages extends Messages {

	
	@DefaultMessage("This month we have '{0}' work days and '{1}' work hours")
	String this_month_we_have_param_work_days_and_param_work_hours(String days, String hours);
	
}
