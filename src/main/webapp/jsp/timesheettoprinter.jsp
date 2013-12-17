<!-- 

#-------------------------------------------------------------------------------
# Copyright (c) 2012 Dmitry Tikhomirov.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the GNU Public License v3.0
# which accompanies this distribution, and is available at
# http://www.gnu.org/licenses/gpl.html
# 
# Contributors:
#     Dmitry Tikhomirov - initial API and implementation
#-------------------------------------------------------------------------------
 -->
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="javax.servlet.*"%>
<%@page import="org.opensheet.server.dao.*"%>
<%@page import="org.opensheet.server.dao.impl.*"%>
<%@page import="org.opensheet.shared.model.*"%>
  <% 
	@SuppressWarnings({ "unchecked" })

    
	
    HashMap<Assignment,ArrayList<Integer>> hoursByDays = (HashMap<Assignment,ArrayList<Integer>>) request.getAttribute("hoursByDays");  
    
  	Integer totalSum = 0;
    
  	User user = (User) request.getAttribute("user");  
    Calendar cal = (Calendar) request.getAttribute("calendar");
  

  
	Integer maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	List<Integer> dailySum = new ArrayList<Integer>();
	for(Integer in =0;in<= maxDays;in++){
		dailySum.add(in,0);
	}
	
                        %> 




<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
		<title>Timetable</title>
		<script type="text/javascript"> 
			window.print();
		</script>
		<style type="text/css">
					@media print {
					
					table {
						
						
						}
					
					
					
					
					table.main {
						width: 100%;
						height: 100%;
						border-collapse: collapse;
						text-align:center;
						background:#fc0;
						font-family:Courier;
					    
						
						
					}
					
					html, body {
						margin:0px;
						padding:0px;
					}
					
			}

</style>
	</head>
	<body>


<table style="width:100%;height:50px;background:#EAF5BF;border: 1px solid;border-collapse: collapse;"> 
 <thead>
  		<tr style="border: 1px solid;"> 
        <th class="t_header" rowspan="2" style="width:25%;border: 1px solid;">Company Name</th> 
        <th class="t_header" rowspan="2" style="width:25%;border: 1px solid;">Timesheet</th> 
        <th class="t_header" style="width:5%;border: 1px solid;">Date</th> 
        <th class="t_header" style="width:10%;border: 1px solid;">BR</th> 
        <th class="t_header" style="width:10%;border: 1px solid;">FTEE</th> 
        <th class="t_header" style="width:25%;border: 1px solid;">Employee :</th> 
      </tr> 
      <tr> 
        <th class="t_header" style="border: 1px solid;"><% out.print(cal.get(Calendar.MONTH)+1 + "/" + cal.get(Calendar.YEAR)); %></th> 
        <th class="t_header" style="border: 1px solid;">0 </th> 
        <th class="t_header" style="border: 1px solid;">0 </th> 
        <th class="t_header" style="border: 1px solid;"><% out.print(user.getFullName());  %></th> 
      </tr>
 </thead> 
	</table>	
	
	   <table style="width:100%;background:#EAF5BF;border: 1px solid;border-collapse: collapse;">
          
      <tr style="width:100%;height:20px;border: 1px solid black;">
      	<th style="width:21%;height:20px;border: 1px solid black;">
      		<B>Assignment Name</B>
      	</th>
      	<th style="width:8%;height:20px;border: 1px solid black;">
      		<B>Index</B>
      	</th>
      	<th style="width:7%;height:20px;border: 1px solid black;">
      		<B>Type</B>
      	</th>
      	<th style="width:4%;height:20px;border: 1px solid black;">
      		<B>Summ</B>
      	</th>
      	
      	<%
      	for(Integer day = 1; day<=maxDays;day++){
      	    	out.print(" <th style='width:2%;height:20px;border: 1px solid black;''>" + day  + " </th>");
      	    	
      	}
      	
      	Integer diff = 31 - maxDays;
	      	for(Integer i = 1; i<=diff;i++){
	  	    	out.print(" <th style='width:2%;height:20px;border: 1px solid black;''></th>");
	  	    	
  		}
      	
      	%>
      	
      </tr>
 <%
 
 
 
	
	Set<Assignment> keys = hoursByDays.keySet();
	
	Iterator<Assignment> iter = keys.iterator();
	
	while (iter.hasNext()) {
		String type = "";
		Integer sum = 0;
		
		Assignment a = (Assignment) iter.next();
		ArrayList<Integer> daysArrayList = hoursByDays.get(a);
		
		
		for(Integer h: daysArrayList){ 
			if(h != 0){
				
				
				sum = sum + h;	
			}
			
		//	sum = sum + h;
		//	System.out.println(h);	
		}
		totalSum = totalSum +sum;
		
		out.println("<tr>");
		
		if(a.getType() == 0) type="project";
		else if(a.getType() == 1) type="tender";
		else if(a.getType() == 2) type="office";
		else if(a.getType() == 1) type="off_hours";
		
		
		out.println("<td style='width:2%;height:20px;border: 1px solid black;''>" + a.getName() + "</td><td style='width:2%;height:20px;border: 1px solid black;''>" + a.getIndex() + "</td><td style='width:2%;height:20px;border: 1px solid black;''>" + type + "</td><td style='width:2%;height:20px;border: 1px solid black;''>" + sum + "</td>");

		
		
		
		Integer maxV = daysArrayList.size();
		maxV++;
		
		for(Integer d =1;d<daysArrayList.size();d++){
			if( daysArrayList.get(d) == 0){
				out.println("<td style='width:2%;height:20px;border: 1px solid black;''></td>");
			}else{
				
				dailySum.set(d,dailySum.get(d)+daysArrayList.get(d));
				out.println("<td style='width:2%;height:20px;border: 1px solid black;''>" + daysArrayList.get(d) + "</td>");
			}
			
		//	System.out.println(daysArrayList.get(d));	
			
		}
		
		
		
	    
		out.println("</tr>");
	}

	out.println("<tr><td style='width:2%;height:20px;border: 1px solid black;''>" + "Result" + "</td><td style='width:2%;height:20px;border: 1px solid black;''></td><td style='width:2%;height:20px;border: 1px solid black;''>" + "result" + "</td><td style='width:2%;height:20px;border: 1px solid black;''>" + totalSum + "</td>");
	for(Integer d =1;d<dailySum.size();d++){
		if( dailySum.get(d) == 0){
			out.println("<td style='width:2%;height:20px;border: 1px solid black;''></td>");
		}else{
			out.println("<td style='width:2%;height:20px;border: 1px solid black;''>" + dailySum.get(d) + "</td>");
		}
		
	//	System.out.println(daysArrayList.get(d));	
		
	}
	out.println("</tr>");
	out.println("</table>");
 %>
 
  
 
                 
 
 
    </table>
	
	
	 
	</body>








</html>
