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
package org.opensheet.mvc;


import org.opensheet.server.dao.UserDAO;
import org.opensheet.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping("/Opensheet.htm")
public class OpensheetController{
	
	@Autowired
	private UserDAO userDAO;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView opensheet(){
		User user = userDAO.whoIam();
		ModelAndView model = new ModelAndView("opensheet");
		model.addObject("locale",user.getLang());
		return model;
	}

	
	
	
}
