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

import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.Number;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.opensheet.server.dao.AssignmentDAO;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Hour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractJExcelView;



public class QuickDepartmentReportByAssignmentAndByUserView extends AbstractJExcelView {

	
	private int i;
	private Map<Integer,Map<Integer,List<Hour>>> mapByType;
	WritableCellFormat tahoma14fontBlueformat;
	Map<Integer,Assignment> assignments;
	WritableSheet sheet;
	private Map<Integer,Integer> usersRates;
	
	private String[] type = {"Project","Tender","Office Tasks","Off Hours"};

	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			WritableWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		
		Map<String,List<Hour>> hourMap = (Map<String, List<Hour>>) model.get("hourMap");
		mapByType = (Map<Integer, Map<Integer, List<Hour>>>) model.get("mapByType");
		assignments = (Map<Integer,Assignment>) model.get("Assignments");
		usersRates = (Map<Integer, Integer>) model.get("UsersRates");

		for(Map.Entry<Integer, Integer> kv: usersRates.entrySet()){
			System.out.println();
		}
		
		response.setContentType("application/vnd.ms-excel");
		response.setHeader ("Content-Disposition", "attachment; filename=timesheet.xls");
	
		
		sheet = workbook.createSheet("mysheet", 0);              // created a sheet in the workbook

		
		WritableFont times16fontbold = new WritableFont(WritableFont.TIMES, 16, WritableFont.BOLD, true); 
		WritableCellFormat times16formatbold = new WritableCellFormat (times16fontbold); 
		
		WritableFont times10font = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD, true); 
		WritableCellFormat times10fontformatleft = new WritableCellFormat (times10font); 
		times10fontformatleft.setAlignment(Alignment.LEFT);
		
		WritableCellFormat times10fontformatcentre = new WritableCellFormat (times10font); 
		times10fontformatcentre.setAlignment(Alignment.CENTRE);
		
		WritableCellFormat times10fontformatright = new WritableCellFormat (times10font); 
		times10fontformatright.setAlignment(Alignment.RIGHT);
		
		
		WritableFont times10fontbold = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, true); 
		WritableCellFormat times10fontformatboldleft = new WritableCellFormat (times10fontbold); 
		times10fontformatboldleft.setAlignment(Alignment.LEFT);
		
		WritableCellFormat times10fontformatboldcentre = new WritableCellFormat (times10fontbold); 
		times10fontformatboldcentre.setAlignment(Alignment.CENTRE);
		
		WritableCellFormat times10fontformatboldright = new WritableCellFormat (times10fontbold); 
		times10fontformatboldright.setAlignment(Alignment.RIGHT);
		
		WritableFont tahoma10fontbold = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.BOLD, true); 
		WritableCellFormat tahoma10fontformatboldleft = new WritableCellFormat (tahoma10fontbold); 
		tahoma10fontformatboldleft.setAlignment(Alignment.LEFT);
		
		WritableCellFormat tahoma10fontformatboldcentre = new WritableCellFormat (tahoma10fontbold); 
		tahoma10fontformatboldcentre.setAlignment(Alignment.CENTRE);
		
		WritableCellFormat tahoma10fontformatboldright = new WritableCellFormat (tahoma10fontbold); 
		tahoma10fontformatboldright.setAlignment(Alignment.RIGHT);
		
		
		WritableFont tahoma10font = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.NO_BOLD, true); 
		WritableCellFormat tahoma10fontformatleft = new WritableCellFormat (tahoma10font); 
		tahoma10fontformatleft.setAlignment(Alignment.LEFT);
		
		WritableCellFormat tahoma10fontformatcentre = new WritableCellFormat (tahoma10font); 
		tahoma10fontformatcentre.setAlignment(Alignment.CENTRE);
		
		WritableCellFormat tahoma10fontformatright = new WritableCellFormat (tahoma10font); 
		tahoma10fontformatright.setAlignment(Alignment.RIGHT);
		
		WritableFont tahoma10fontred = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.NO_BOLD, true);
		tahoma10fontred.setColour(Colour.RED);
		WritableCellFormat tahoma10fontformatleftred = new WritableCellFormat (tahoma10fontred); 
		tahoma10fontformatleftred.setAlignment(Alignment.LEFT);
		
		WritableCellFormat tahoma10fontformatcentrered = new WritableCellFormat (tahoma10fontred); 
		tahoma10fontformatcentrered.setAlignment(Alignment.CENTRE);
		
		WritableCellFormat tahoma10fontformatrightred = new WritableCellFormat (tahoma10fontred); 
		tahoma10fontformatrightred.setAlignment(Alignment.RIGHT);
		
		
		
		WritableFont courier16fontbold = new WritableFont(WritableFont.COURIER, 16, WritableFont.BOLD, true); 
		WritableCellFormat  courier10formatbold = new WritableCellFormat(courier16fontbold); 
		courier10formatbold.setAlignment(Alignment.RIGHT);
		


		sheet.setColumnView(0, 35);
        sheet.setColumnView(1, 35);
        sheet.setColumnView(2, 35);
        sheet.setColumnView(3, 35);
        sheet.setColumnView(4, 35);
        sheet.setColumnView(5, 35);
        sheet.setColumnView(6, 35);
        sheet.setColumnView(7, 35);
        
		Label approwedBy = new Label(2,1,"APPROVED By",courier10formatbold);
		Label dmName = new Label(3,2,"name of department manager");
		
		Label dmSign = new Label(2,4,"Sign:",courier10formatbold);
		
		Label date = new Label(2,5,"Date:",courier10formatbold);
	
		
		
		sheet.addCell(approwedBy);
		sheet.addCell(dmName);
		sheet.addCell(dmSign);
		sheet.addCell(date);
		
		WritableFont departmentNameFont = new WritableFont(WritableFont.COURIER, 14, WritableFont.BOLD, true); 
		WritableCellFormat  departmentNameCellFormat = new WritableCellFormat(departmentNameFont); 
		departmentNameCellFormat.setAlignment(Alignment.CENTRE);
	
		
		
		Label departmentName = new Label(0,8,"Department name:",departmentNameCellFormat);
		sheet.addCell(departmentName);
		sheet.mergeCells(0,8,3,8);
		
		Label userReportLabel = new Label(0,9,"Assignment Report by Department users",departmentNameCellFormat);
		sheet.addCell(userReportLabel);
		sheet.mergeCells(0,9,3,9);
		
		
		sheet.mergeCells(1,10,2,10);
		
		Label userNameLabel = new Label(0,10,"Name",times10fontformatboldleft);
		Label assignmentNameLabel = new Label(1,10,"Assignment",times10fontformatboldcentre);
		Label hoursLabel = new Label(3,10,"Hours",times10fontformatboldleft);

				
		sheet.addCell(userNameLabel);
		sheet.addCell(assignmentNameLabel);
		sheet.addCell(hoursLabel);

		i = 11;
		Integer totalSum = 0;
		
		for(Map.Entry<String, List<Hour>> kv: hourMap.entrySet()){
			if(kv.getValue().size() != 0){
					Integer sum = 0;
					for(Hour h: kv.getValue()){
						sheet.mergeCells(1,i,2,i);
						
						userNameLabel = new Label(0,i,kv.getKey(),tahoma10fontformatleft);
						assignmentNameLabel = new Label(1,i,h.getAssignment().getName(),tahoma10fontformatcentre);
						Number hourLabel = new Number(3,i,h.getHour(),tahoma10fontformatright);
						sheet.addCell(userNameLabel);
						sheet.addCell(assignmentNameLabel);
						sheet.addCell(hourLabel);
						
						sum = sum + h.getHour();
						totalSum = totalSum  + h.getHour();
						i++;
					}
					
						
						sheet.mergeCells(1,i,2,i);
						assignmentNameLabel = new Label(1,i,"Sum",tahoma10fontformatright);
						Number sumCell = new Number(3,i,sum,tahoma10fontformatcentre);
						sheet.addCell(assignmentNameLabel);
						sheet.addCell(sumCell);
						i++;
				
		
			}else{
				sheet.mergeCells(1,i,2,i);
				userNameLabel = new Label(0,i,kv.getKey(),tahoma10fontformatleftred);
				sheet.addCell(userNameLabel);
				assignmentNameLabel = new Label(1,i,"Sum",tahoma10fontformatrightred);
				Number sumCell = new Number(3,i,0,tahoma10fontformatcentrered);
				sheet.addCell(assignmentNameLabel);
				sheet.addCell(sumCell);
				i++;
			}
			
			
			
			
			
			
		}
		
		WritableFont tahoma16fontGrey = new WritableFont(WritableFont.TAHOMA, 16, WritableFont.BOLD, true);
		WritableCellFormat tahoma16fontGreyformat = new WritableCellFormat (tahoma16fontGrey); 
		tahoma16fontGreyformat.setBackground(Colour.GREY_40_PERCENT);
		tahoma16fontGreyformat.setAlignment(Alignment.CENTRE);
		

		assignmentNameLabel = new Label(0,i,"Total Sum",tahoma16fontGreyformat);
		Number sumCell = new Number(3,i,totalSum,tahoma16fontGreyformat);
		sheet.addCell(assignmentNameLabel);
		sheet.addCell(sumCell);
		sheet.mergeCells(0,i,2,i);
		
		
		i++;
		
		i++;
		i++;
		
		Label allHoursLabel = new Label(0,i,"All Hours / Add Assignments:",departmentNameCellFormat);
		sheet.addCell(allHoursLabel);
		sheet.mergeCells(0,i,3,i);
		
		i++;
		

		for(int step=0;step<=3;step++){
			i++;
			
		
			if(mapByType.get(step) != null){
				doByTypes(step);
			}
			
			
		}

		
	}	
		
	
	private void  doByTypes(Integer step) throws RowsExceededException, WriteException{
	
		WritableFont tahoma14fontBlue = new WritableFont(WritableFont.TAHOMA, 14, WritableFont.NO_BOLD, true);
		tahoma14fontBlueformat = new WritableCellFormat (tahoma14fontBlue); 
		tahoma14fontBlueformat.setBackground(Colour.BLUE_GREY);
		tahoma14fontBlueformat.setAlignment(Alignment.CENTRE);
		
		Label assignmentType = new Label(0,i,type[step],tahoma14fontBlueformat);
		sheet.addCell(assignmentType );
		sheet.mergeCells(0,i,7,i);
		
		i++;
		
		int nextstep = i;
		nextstep++;
		
		WritableFont tahoma16fontGrey = new WritableFont(WritableFont.TAHOMA, 16, WritableFont.BOLD, true);
		WritableCellFormat tahoma16fontGreyformat = new WritableCellFormat (tahoma16fontGrey); 
		tahoma16fontGreyformat.setBackground(Colour.GREY_40_PERCENT);
		tahoma16fontGreyformat.setAlignment(Alignment.CENTRE);
		

		
		
		WritableFont tahoma10fontbold = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.BOLD, true); 
		WritableCellFormat tahoma10fontformatboldleft = new WritableCellFormat (tahoma10fontbold); 
		tahoma10fontformatboldleft.setAlignment(Alignment.LEFT);
		
		WritableCellFormat tahoma10fontformatboldcentre = new WritableCellFormat (tahoma10fontbold); 
		tahoma10fontformatboldcentre.setAlignment(Alignment.CENTRE);
		
		WritableCellFormat tahoma10fontformatboldright = new WritableCellFormat (tahoma10fontbold); 
		tahoma10fontformatboldright.setAlignment(Alignment.RIGHT);
		
		sheet.mergeCells(0,i,0,nextstep);
		sheet.mergeCells(1,i,1,nextstep);
		sheet.mergeCells(2,i,2,nextstep);
		
		Label assignmentNameLabel = new Label(0,i,"Assignment",tahoma10fontformatboldcentre);
		Label userNameLabel = new Label(1,i,"Name",tahoma10fontformatboldcentre);
		Label hoursLabel = new Label(2,i,"Hours",tahoma10fontformatboldcentre);
		
		sheet.addCell(userNameLabel);
		sheet.addCell(assignmentNameLabel);
		sheet.addCell(hoursLabel);
		nextstep++;
		
		sheet.mergeCells(3,i,4,i);
		
		Label finaceLabel = new Label(3,i,"Financional Data",tahoma10fontformatboldcentre);
		sheet.addCell(finaceLabel); 
		
		sheet.mergeCells(5,i,7,i);
		
		Label approvedLabel = new Label(5,i,"Approvment by Project Manager",tahoma10fontformatboldcentre);
		sheet.addCell(approvedLabel); 
		
		i++;
		
		Label hourlyRateLabel = new Label(3,i,"Personal Hourly Rate",tahoma10fontformatboldcentre);
		sheet.addCell(hourlyRateLabel); 
		
		Label incomingsLabel = new Label(4,i,"Revenue",tahoma10fontformatboldcentre);
		sheet.addCell(incomingsLabel); 
		
		Label pmNameLabel = new Label(5,i,"Project manager",tahoma10fontformatboldcentre);
		sheet.addCell(pmNameLabel); 
		
		Label signDeatilsLabel = new Label(6,i,"Amount/Hours/results approved",tahoma10fontformatboldcentre);
		sheet.addCell(signDeatilsLabel); 
		
		
		Label signLabel = new Label(7,i,"Sign",tahoma10fontformatboldcentre);
		sheet.addCell(signLabel); 
		
		
		
		i++;
		
		Integer sumTotal =0;
		Integer sumHrTotal =0;
		
		Map<Integer,List<Hour>> map =  mapByType.get(step);
		for(Map.Entry<Integer, List<Hour>>  kv: map.entrySet()){
			
			Integer assignmentId = kv.getKey();
			Assignment assignment = assignments.get(assignmentId);
			
			List<Hour> list = kv.getValue();
			Integer sum = 0;
			Integer sumInHR = 0;
			nextstep = i + list.size();
			nextstep--;
			sheet.mergeCells(0,i,0,nextstep);
			sheet.mergeCells(5,i,5,nextstep);
			sheet.mergeCells(6,i,6,nextstep);
			sheet.mergeCells(7,i,7,nextstep);
			
			assignmentNameLabel = new Label(0,i,assignment.getName(),tahoma10fontformatboldcentre);
			sheet.addCell(assignmentNameLabel);
			assignmentNameLabel = new Label(5,i,assignment.getOwner().getFullName(),tahoma10fontformatboldcentre);
			sheet.addCell(assignmentNameLabel);
			
			for(Hour h : list){
				int rate;
				
				if(! usersRates.containsKey(h.getUser().getId())){
					rate= 0;
				}else{
					rate = usersRates.get(h.getUser().getId());
				}
				
				int hr = rate*h.getHour();
				userNameLabel = new Label(1,i,h.getUser().getFullName(),tahoma10fontformatboldcentre);
				Number hour = new Number(2,i,h.getHour(),tahoma10fontformatboldcentre);
				Number internalRate = new Number(3,i,rate,tahoma10fontformatboldcentre);
				Number internalRateSum = new Number(4,i,hr,tahoma10fontformatboldcentre);
				
				sheet.addCell(userNameLabel);
				sheet.addCell(hour);
				sheet.addCell(internalRate);
				sheet.addCell(internalRateSum);
				sum = sum + h.getHour();
				sumInHR = sumInHR + hr;
				sumTotal = sumTotal + h.getHour();
				sumHrTotal = sumHrTotal + hr;
				i++;
			}
			userNameLabel = new Label(1,i,"Sum",tahoma10fontformatboldright);
			sheet.addCell(userNameLabel);
			Number hour = new Number(2,i,sum,tahoma10fontformatboldright);
			sheet.addCell(hour);
			Number hr = new Number(4,i,sumInHR,tahoma10fontformatboldright);
			sheet.addCell(hr);
			
			i++;
		}
		
		assignmentNameLabel = new Label(0,i,"Total Sum of " + type[step] + "'s" ,tahoma16fontGreyformat);
		Number sumCell = new Number(2,i,sumTotal,tahoma16fontGreyformat);
		Number sumHrCell = new Number(4,i,sumHrTotal,tahoma16fontGreyformat);
		sheet.addCell(assignmentNameLabel);
		sheet.addCell(sumCell);
		sheet.addCell(sumHrCell);
		sheet.mergeCells(0,i,1,i);
		i++;
		
	}
		
	
	
	

}
