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
package org.opensheet.server.utils;

import java.util.List;

public class ListUtil {

	 public static int sum(List<Integer> list){
	      if(list==null || list.size()<1)
	        return 0;

	      int sum = 0;
	      for(Integer i: list)
	        sum = sum+i;

	      return sum;
	    }
	
}
