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
package org.opensheet.client.mvc.events;

import com.extjs.gxt.ui.client.event.BaseObservable;

public class EventBus extends BaseObservable {
	private static EventBus instance;

	private EventBus() {
	}

	public static synchronized EventBus get() {
		if (instance == null)
			instance = new EventBus();
		return instance;
	}
}
