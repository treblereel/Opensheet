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
package org.opensheet.client.utils.icons;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

@SuppressWarnings("deprecation")
public interface Icons extends ImageBundle {

	  @Resource("table.png")
	  AbstractImagePrototype table();

	  @Resource("add16.gif")
	  AbstractImagePrototype add16();

	  @Resource("add24.gif")
	  AbstractImagePrototype add24();

	  @Resource("add32.gif")
	  AbstractImagePrototype add32();

	  @Resource("application_side_list.png")
	  AbstractImagePrototype side_list();

	  @Resource("application_form.png")
	  AbstractImagePrototype form();

	  @Resource("connect.png")
	  AbstractImagePrototype connect();

	  @Resource("user_add.png")
	  AbstractImagePrototype user_add();

	  @Resource("user_delete.png")
	  AbstractImagePrototype user_delete();

	  @Resource("accordion.gif")
	  AbstractImagePrototype accordion();

	  @Resource("add.gif")
	  AbstractImagePrototype add();

	  @Resource("delete.gif")
	  AbstractImagePrototype delete();

	  @Resource("calendar.gif")
	  AbstractImagePrototype calendar();

	  @Resource("menu-show.gif")
	  AbstractImagePrototype menu_show();

	  @Resource("list-items.gif")
	  AbstractImagePrototype list_items();

	  @Resource("album.gif")
	  AbstractImagePrototype album();

	  @Resource("text.png")
	  AbstractImagePrototype text();

	  @Resource("plugin.png")
	  AbstractImagePrototype plugin();
	  
	  @Resource("music.png")
	  AbstractImagePrototype music();
	  
	  @Resource("error.png")
	  AbstractImagePrototype error();
	  
	  @Resource("recycled.png")
	  AbstractImagePrototype  reload();
	  
	  @Resource("project.png")
	  AbstractImagePrototype  project();
	  
	  @Resource("tender.png")
	  AbstractImagePrototype  tender();
	  
	  @Resource("flower.png")
	  AbstractImagePrototype  flower();
	  
	  @Resource("alarmbell.png")
	  AbstractImagePrototype  alarmbell();
	  
	  @Resource("folder_locked.png")
	  AbstractImagePrototype  folder_locked();
	  
	  @Resource("project.png")
	  AbstractImagePrototype  project_icon();
	  
	  @Resource("tender.png")
	  AbstractImagePrototype  tender_icon();
	  
	  @Resource("office.png")
	  AbstractImagePrototype  office_icon();
	  
	  @Resource("off_hour.png")
	  AbstractImagePrototype  off_hour_icon();

	  @Resource("kcontrol.png")
	  AbstractImagePrototype  tools_icon();
	  
	  @Resource("date.png")
	  AbstractImagePrototype  calendar_icon();
	  
	}
