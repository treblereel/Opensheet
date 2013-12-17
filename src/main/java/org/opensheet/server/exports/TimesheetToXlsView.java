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

import jxl.format.Colour;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.write.Label;

import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.opensheet.shared.model.Assignment;
import org.springframework.web.servlet.view.document.AbstractJExcelView;

public class TimesheetToXlsView extends AbstractJExcelView{

	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(@SuppressWarnings("rawtypes") Map model, WritableWorkbook workbook,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		response.setContentType("application/vnd.ms-excel");
		response.setHeader ("Content-Disposition", "attachment; filename=timesheet.xls");
		
		
		Map<Assignment,List<Integer>> hours  = (Map<Assignment,List<Integer>>) model.get("Hours");
		HashMap<Integer, Boolean> getHolidaysMap = (HashMap<Integer, Boolean>) model.get("holidays");
		
		Integer days = getHolidaysMap.size();
		getHolidaysMap.put(0, false);
		
		WritableSheet sheet = workbook.createSheet("Timesheet Export", 0);
		
		
		WritableCellFormat format = new WritableCellFormat(); 
		Colour orange = Colour.ORANGE;
		format.setBackground(orange);
		
		
        sheet.addCell(new Label(0, 0, "Assignment"));
        sheet.addCell(new Label(1, 0, "Hours summ"));
        sheet.setColumnView(0, 25);
        sheet.setColumnView(1, 5);
        
        for(Integer i=1;i<=days;i++){
        	int step = i;
        	step++;
        	if(getHolidaysMap.get(i) == true){
          		sheet.addCell(new jxl.write.Number(step, 0,i,format));
        	}else{
            	sheet.addCell(new jxl.write.Number(step, 0,i));

        	}
        	sheet.setColumnView(step, 3);
        }

        int rowNum = 1;
   
        
        for(Map.Entry<Assignment, List<Integer>> kv: hours.entrySet()){
        	Assignment assignment = kv.getKey();
        	
        	if(assignment.getLevel() == 0){
            	sheet.addCell(new Label(0, rowNum,assignment.getName()));
        	}else{
            	sheet.addCell(new Label(0, rowNum,"  " + assignment.getName()));
        	}
        	
        	List<Integer> list = kv.getValue();
        	for(Integer i=0;i<=days;i++){
        		int step = i;
            	step++;
            	if(list.get(i) != 0){
            		if(getHolidaysMap.get(i) == true){
            			sheet.addCell(new jxl.write.Number(step, rowNum,list.get(i),format));
            		}else{
                		sheet.addCell(new jxl.write.Number(step, rowNum,list.get(i)));
            		}
            	}else{
            		if(getHolidaysMap.get(i) == true){
            			sheet.addCell(new Label(step, rowNum,"",format));
            		}else{
            			sheet.addCell(new Label(step, rowNum,""));
            		}
            	}
        	}
        	rowNum++;
       }
 	}
}
