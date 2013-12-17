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
package org.opensheet.server.exports;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Hour;
import org.opensheet.shared.model.User;
import org.springframework.web.servlet.view.document.AbstractJExcelView;



public class UserAssignmentByPeriodReportToXls extends AbstractJExcelView{

	
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			WritableWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
	
		
		response.setContentType("application/vnd.ms-excel");
		response.setHeader ("Content-Disposition", "attachment; filename=timesheet.xls");
		
		
		@SuppressWarnings("unchecked")
		Map<User, List<Hour>> map  = (Map<User, List<Hour>>) model.get("Hours");
		Assignment assignment = (Assignment)  model.get("assignment");
		
		WritableSheet sheet = workbook.createSheet("Assignment->User Export", 0);
        sheet.addCell(new Label(0, 0, "User Name"));
        sheet.addCell(new Label(1, 0, "Assignment"));
        sheet.addCell(new Label(2, 0, "Hour"));
        sheet.addCell(new Label(3, 0, "inratesum"));
        sheet.addCell(new Label(4, 0, "extratesum"));

        
        int rowNum = 1;
        
        sheet.setColumnView(0, 40);
        sheet.setColumnView(1, 40);
        sheet.setColumnView(2, 10);
        sheet.setColumnView(3, 20);
        sheet.setColumnView(4, 20);
 
        
        
		
		
		for(Map.Entry<User,List<Hour>> m: map.entrySet()){
			List<Hour> list  =  m.getValue();
			Hour rootHour = list.get(0);
			
	        	sheet.addCell(new Label(0,  rowNum,m.getKey().getFullName()));
	        	sheet.addCell(new Label(1,  rowNum,rootHour.getAssignment().getName()));
			    sheet.addCell(new jxl.write.Number(2, rowNum,rootHour.getHour()));
	        	sheet.addCell(new jxl.write.Number(3, rowNum,rootHour.getInratesum()));
	        	sheet.addCell(new jxl.write.Number(4, rowNum,rootHour.getExtratesum()));
			    rowNum++;

			list.remove(0);
			
			if(assignment.getLevel() == 0){
				for(Hour h: list){
					sheet.addCell(new Label(0,  rowNum," "));
		        	sheet.addCell(new Label(1,  rowNum,h.getAssignment().getName()));
				    sheet.addCell(new jxl.write.Number(2, rowNum,h.getHour()));
		        	sheet.addCell(new jxl.write.Number(3, rowNum,h.getInratesum()));
		        	sheet.addCell(new jxl.write.Number(4, rowNum,h.getExtratesum()));
		        	rowNum++;
				}
			}
		
		}
	}

}
