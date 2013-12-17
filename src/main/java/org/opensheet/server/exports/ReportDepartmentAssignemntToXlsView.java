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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Department;
import org.springframework.web.servlet.view.document.AbstractJExcelView;

public class ReportDepartmentAssignemntToXlsView  extends AbstractJExcelView{
    Logger logger = LoggerFactory.getLogger(ReportDepartmentAssignemntToXlsView.class);

	

	@Override
	protected void buildExcelDocument(@SuppressWarnings("rawtypes") Map model, WritableWorkbook workbook,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader ("Content-Disposition", "attachment; filename=timesheet.xls");
		
		
		@SuppressWarnings("unchecked")
		Map<Department, List<Assignment>>  map  =(Map<Department, List<Assignment>>) model.get("data");
		WritableSheet sheet = workbook.createSheet("Report->D->A2", 0);
        int rowNum = 0;
        sheet.setColumnView(0, 50);
        sheet.setColumnView(1, 50);
        sheet.setColumnView(2, 50);

        
        
        for(Map.Entry<Department, List<Assignment>> kv : map.entrySet()){
        	
        	
        	sheet.addCell(new Label(0, rowNum,kv.getKey().getName()   ));
        	sheet.addCell(new Label(1, rowNum,kv.getKey().getOwner().getFullName()   ));
        	rowNum++;
        	for(Assignment a: kv.getValue()){
        		sheet.addCell(new Label(1, rowNum,a.getName()));
            	sheet.addCell(new Label(2, rowNum,a.getOwner().getFullName()));
            	rowNum++;	
        	}
        }
        

        
        workbook.write(); 
        workbook.close();
	}
}
