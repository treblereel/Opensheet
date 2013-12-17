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

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opensheet.server.dao.StatDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.extjs.gxt.ui.client.data.BaseModel;



@Controller
@RequestMapping("/UserAssignmentReportToPrinter.htm")
public class UserAssignmentReportToPrinterController extends AbstractController{
	@Autowired
	private StatDAO statDao;
	
	@SuppressWarnings("deprecation")
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse arg1) throws Exception {
		
		Date start =  new Date();
		Date end   =  new Date();
		Integer branch = Integer.parseInt(request.getParameter("branch").toString());
		Integer type = Integer.parseInt(request.getParameter("type").toString());
		
		start.setYear(Integer.parseInt(request.getParameter("s_year").toString()));
		start.setMonth(Integer.parseInt(request.getParameter("s_month").toString()));
		start.setDate(Integer.parseInt(request.getParameter("s_day").toString()));
		
		end.setYear(Integer.parseInt(request.getParameter("e_year").toString()));
		end.setMonth(Integer.parseInt(request.getParameter("e_month").toString()));
		end.setDate(Integer.parseInt(request.getParameter("e_day").toString()));
		
		
		List<BaseModel> hours = statDao.getHourReportByUserByPeriodByTypeByBranch(branch, type, start, end);
		return new ModelAndView("UserAssignmentReportToPrinter","Hours",hours);
	}

}
