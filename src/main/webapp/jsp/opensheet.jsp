<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>

	<%
	String locale = (String) request.getAttribute("locale");
	if(locale.isEmpty()){
		locale = "en"; 
	}
	
	%>


<!doctype html>


<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=8" />
	<meta name="gwt:property" content="locale=<% out.print(locale); %>">

<!--  
	<meta http-equiv="X-UA-Compatible" content="IE=8" />
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
	 
                                                                  -->
    <!-- Consider inlining CSS to reduce the number of requested files -->
    <!--                                                               -->
    <link rel="stylesheet" type="text/css" href="css/Opensheet.css">
	<link rel="stylesheet" type="text/css" href="resources/css/gxt-all.css" />
    <title>Opensheet</title>
    
	<script language='javascript' src='resources/flash/swfobject.js' ></script>
	<script type="text/javascript" language="javascript" src="opensheet/opensheet.nocache.js"></script>
    
  </head>

  <body>

    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
    
    <noscript>
      <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
      </div>
    </noscript>

    
  </body>
</html>
