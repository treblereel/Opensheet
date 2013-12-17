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
package org.opensheet.client.utils;

import com.google.gwt.user.client.Random;


public class Colors {
	private static String[] colors = {"#FFC74E","#A91E23","#633393","#026699","#00A4AA","#2EAA4A","#EEDC0E","#2D65E0","#CA3C26","#A47D9A"};
	
	
	public static final String getRandom(){
			return colors[Random.nextInt(colors.length)];
	}

}
