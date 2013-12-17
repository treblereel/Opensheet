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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html;  charset=UTF-8" pageEncoding="utf-8"/>
<title>Opensheet</title>
<link rel="icon" type="image/png" href="resources/images/login/ramtiming.png"></link>
<meta name="description" content="" />
<script type="text/javascript" src="resources/js/jquery.min.js"></script>
<script>
	
jQuery(function($){
		   
	// jQuery validation script
	$('#login').submit(function(){
		
		var valid = true;
		var errormsg = 'This field is required!';
		var errorcn = 'error';
		
		$('.' + errorcn, this).remove();			
		
		$('.required', this).each(function(){
			var parent = $(this).parent();
			if( $(this).val() == '' ){
				var msg = $(this).attr('title');
				msg = (msg != '') ? msg : errormsg;
				$('<span class="'+ errorcn +'">'+ msg +'</span>')
					.appendTo(parent)
					.fadeIn('fast')
					.click(function(){ $(this).remove(); })
				valid = false;
			};
		});
		
		return valid;
	});
	
})	



</script>
<style>

/* HTML elements  */		

	h1, h2, h3, h4, h5, h6{
		font-weight:normal;
		margin:0;
		line-height:1.1em;
		color:#000;
		}	
	h1{font-size:2em;margin-bottom:.5em;}	
	h2{font-size:1.75em;margin-bottom:.5142em;padding-top:.2em;}	
	h3{font-size:1.5em;margin-bottom:.7em;padding-top:.3em;}
	h4{font-size:1.25em;margin-bottom:.6em;}
	h5,h6{font-size:1em;margin-bottom:.5em;font-weight:bold;}
	
	p, blockquote, ul, ol, dl, form, table, pre{line-height:inherit;margin:0 0 1.5em 0;}
	ul, ol, dl{padding:0;}
	ul ul, ul ol, ol ol, ol ul, dd{margin:0;}
	li{margin:0 0 0 2em;padding:0;display:list-item;list-style-position:outside;}	
	blockquote, dd{padding:0 0 0 2em;}
	pre, code, samp, kbd, var{font:100% mono-space,monospace;}
	pre{overflow:auto;}
	abbr, acronym{
		text-transform:uppercase;
		border-bottom:1px dotted #000;
		letter-spacing:1px;
		}
	abbr[title], acronym[title]{cursor:help;}
	small{font-size:.9em;}
	sup, sub{font-size:.8em;}
	em, cite, q{font-style:italic;}
	img{border:none;}			
	hr{display:none;}	
	table{width:100%;border-collapse:collapse;}
	th,caption{text-align:left;}
	form div{margin:.5em 0;clear:both;}
	label{display:block;}
	fieldset{margin:0;padding:0;border:none;}
	legend{font-weight:bold;}
	input[type="radio"],input[type="checkbox"], .radio, .checkbox{margin:0 .25em 0 0;}

/* //  HTML elements */	

/* basic */

body, table, input, textarea, select, li, button{
	font:1em "Verdana", sans-serif;
	line-height:1.5em;
	color:#444;
	}	
body{
	font-size:12px;
	background-image:url('resources/images/login/maket.png'); BACKGROUND-REPEAT: no-repeat; BACKGROUND-POSITION: 0% 0%; HEIGHT: 100%);		
	text-align:center;
	}		

/* // basic */

/* login form */	

#login{
	margin:6em 2em auto;
	background:#fff;
	border:8px solid #eee;
	width:500px;
	-moz-border-radius:5px;
	-webkit-border-radius:5px;
	border-radius:5px;
	-moz-box-shadow:0 0 10px #4e707c;
	-webkit-box-shadow:0 0 10px #4e707c;
	box-shadow:0 0 10px #4e707c;
	text-align:left;
	position:relative; float:left;
	}
#login a, #login a:visited{color:#0283b2;}
#login a:hover{color:#111;}
#login h1{
	background:#009DE0;
	color:#fff;
	text-shadow:#007dab 0 1px 0;
	font-size:14px;
	padding:18px 23px;
	margin:0 0 1.5em 0;
	border-bottom:1px solid #007dab;
	}
#login .register{
	position:absolute;
	float:left;
	margin:0;
	line-height:30px;
	top:-60px;
	left:0;
	font-size:11px;
	}
#login p{margin:.5em 25px;}
#login div{
	margin:.5em 25px;
	background:#eee;
	padding:4px;
	-moz-border-radius:3px;
	-webkit-border-radius:3px;
	border-radius:3px;
	text-align:right;
	position:relative;
	}	
#login label{
	float:left;
	line-height:30px;
	padding-left:10px;
	}
#login .field{
	border:1px solid #ccc;
	width:280px;
	font-size:12px;
	line-height:1em;
	padding:4px;
	-moz-box-shadow:inset 0 0 5px #ccc;
	-webkit-box-shadow:inset 0 0 5px #ccc;
	box-shadow:inset 0 0 5px #ccc;
	}	
#login div.submit{background:none;margin:1em 25px;text-align:left;}	
#login div.submit label{float:none;display:inline;font-size:11px;}	
#login button{
	border:0;
	padding:0 30px;
	height:30px;
	line-height:30px;
	text-align:center;
	font-size:12px;
	color:#fff;
	text-shadow:#007dab 0 1px 0;
	background:#009DE0;
	-moz-border-radius:50px;
	-webkit-border-radius:50px;
	border-radius:50px;
	cursor:pointer;
	}
	
#login .forgot{text-align:right;font-size:11px;}
#login .back{padding:1em 0;border-top:1px solid #eee;text-align:right;font-size:11px;}
#login .error{
	float:left;
	position:absolute;
	left:95%;
	top:-5px;
	background:#C63418;
	padding:5px 10px;	
	font-size:11px;
	color:#fff;
	text-shadow:#500 0 1px 0;
	text-align:left;
	white-space:nowrap;
	border:1px solid #500;
	-moz-border-radius:3px;
	-webkit-border-radius:3px;
	border-radius:3px;
	-moz-box-shadow:0 0 5px #700;
	-webkit-box-shadow:0 0 5px #700;
	box-shadow:0 0 5px #700;
	}


/* //  login form */	

/* logo */
.logo{
	float:left;
	position:absolute;	
	margin:1.5em 2em auto;
	}
/* //  logo */	
		
/* tooltips */

.tooltip {
    border-bottom: 1px dotted #0077AA;
    cursor: help;
}

.tooltip::after {
    background: rgba(121, 119, 102, 0.8);
    border-radius: 0px 8px 8px 8px;
    box-shadow: 1px 1px 30px rgba(0, 0, 0, 0.5);
    color: #FFF;
    content: attr(data-tooltip); /* main tooltip content */
    margin-top: 10px;
    opacity: 0;
    padding: 5px 50px;
    position: absolute;
    visibility: hidden;
    text-align:center;        
    transition: all 0.4s ease-in-out;
}
        
.tooltip:hover::after {
    opacity: 1; /* Показываем его */
    visibility: visible;
}	

/* // tooltips */
</style>


</head>

<body>
<p class="logo">
<a href="http://sourceforge.net/projects/opensheet/" title="Opensheet"></a> &nbsp;
<a href="#" title="Opensheet version: 0.0.2 beta"><img src="resources/images/login/timing.png"></img></a>
</p>

<form id="login" method="post" action="j_spring_security_check"> 

<h1> User your login and password</h1>
    <div>
    	<label for="login_username">User name: </label> 
    	<input type="text" name="j_username" id="login_username" class="field required" title="Введите имя пользователя" />
    </div>			

    <div>
    	<label for="login_password">password :</label>
    	<input type="password" name="j_password" id="login_password" class="field required" title="Введите пароль" />
    </div>			
    
    <p class="forgot"><span class="tooltip" data-tooltip=" "></span></p>
    			
    <div class="submit">
        <button type="submit">Submit</button> &nbsp;  
        <button type="reset">Reset</button>

    </div>

</p>
  
</form>	

</body>
</html>


