package org.opensheet.server.exports;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.opensheet.shared.model.User;
import org.springframework.web.servlet.view.document.AbstractJExcelView;


public class AssignmnetUsersToXlsView extends AbstractJExcelView{

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			WritableWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setContentType("application/vnd.ms-excel");
		response.setHeader ("Content-Disposition", "attachment; filename=timesheet.xls");
		
		
		@SuppressWarnings("unchecked")
		List<User> users = (List<User>) model.get("Users");
		WritableSheet sheet = workbook.createSheet("Assignment->Users Export", 0);
        sheet.addCell(new Label(0, 0, "User Name"));
        sheet.addCell(new Label(1, 0, "Department"));
    
        
        int rowNum = 1;
        
        sheet.setColumnView(0, 40);
        sheet.setColumnView(1, 40);
        
        for(User u: users){
        	
        	sheet.addCell(new Label(0,  rowNum,u.getFullName().toString()));
        	sheet.addCell(new Label(1,  rowNum,u.getDepartment().getName().toString()));
        	rowNum++;

        }
	}
}
