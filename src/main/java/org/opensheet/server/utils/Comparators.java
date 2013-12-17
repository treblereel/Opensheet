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



import java.util.Comparator;

import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Hour;
import org.opensheet.shared.model.User;

public class Comparators {

	
	
	public static final Comparator<User> ComparatorUserName =  new Comparator<User>() {
               public int compare(User e1, User e2) {
                   return e1.getSecondName().compareTo(e2.getSecondName());
		                }
    };

    
	public static final Comparator<Assignment> ComparatorAssignmentName =  new Comparator<Assignment>() {
               public int compare(Assignment a1, Assignment a2) {
                   return a1.getName().compareTo(a2.getName());
		                }
    };
    
    public static final Comparator<Hour> ComparatorHoursByAssignmentName =  new Comparator<Hour>() {
        public int compare(Hour h1, Hour h2) {
            return h1.getAssignment().getName().compareTo(h2.getAssignment().getName());
	                }
};
    
}
