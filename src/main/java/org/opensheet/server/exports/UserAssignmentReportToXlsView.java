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

import org.springframework.web.servlet.view.document.AbstractJExcelView;

import com.extjs.gxt.ui.client.data.BaseModel;

public class UserAssignmentReportToXlsView extends AbstractJExcelView{

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			WritableWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setContentType("application/vnd.ms-excel");
		response.setHeader ("Content-Disposition", "attachment; filename=timesheet.xls");
		
		
		@SuppressWarnings("unchecked")
		List<BaseModel> hours = (List<BaseModel>) model.get("Hours");
		WritableSheet sheet = workbook.createSheet("Assignment->User Export", 0);
       sheet.addCell(new Label(0, 0, "User Name"));
        sheet.addCell(new Label(1, 0, "Assignment"));
        sheet.addCell(new Label(2, 0, "Hour"));
        sheet.addCell(new Label(3, 0, "Department"));
        sheet.addCell(new Label(4, 0, "Branch"));
        sheet.addCell(new Label(5, 0, "inRateSum"));
        sheet.addCell(new Label(6, 0, "inRateSum"));
        
        int rowNum = 1;
        
        sheet.setColumnView(0, 40);
        sheet.setColumnView(1, 40);
        sheet.setColumnView(2, 10);
        sheet.setColumnView(3, 20);
        sheet.setColumnView(4, 20);
        sheet.setColumnView(5, 10);
        sheet.setColumnView(6, 10);
        
        
        for(BaseModel b: hours){
        	sheet.addCell(new Label(0,  rowNum,b.get("username").toString()));
        	sheet.addCell(new Label(1,  rowNum,b.get("assignment").toString()));
		    sheet.addCell(new jxl.write.Number(2, rowNum,Double.parseDouble(b.get("hour").toString())));
        	sheet.addCell(new Label(3,  rowNum,b.get("department").toString()));
        	sheet.addCell(new Label(4,  rowNum,b.get("branch").toString()));
		    sheet.addCell(new jxl.write.Number(5, rowNum,Double.parseDouble(b.get("inratesum").toString())));
		    sheet.addCell(new jxl.write.Number(6, rowNum,Double.parseDouble(b.get("extratesum").toString())));
		    rowNum++;
        }
		
	}

}
