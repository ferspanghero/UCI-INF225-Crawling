<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.*;"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ICS Search Engine :: Results</title>
    <link href="https://netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet">

    <!-- /container -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js">
    </script>
    <script src="https://netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js">
    </script>
    
    <style type="text/css">
	#tfheader{
		
	}
	#tfnewsearch{
	float: left;
    width: 1400px;
	}
	.tftextinput{
		margin: 0;
		padding: 5px 15px;
		font-family: Arial, Helvetica, sans-serif;
		font-size:14px;
		border:1px solid #0076a3; border-right:0px;
		border-top-left-radius: 5px 5px;
		border-bottom-left-radius: 5px 5px;
	}
	.tfbutton {
		margin: 0;
		padding: 5px 15px;
		font-family: Arial, Helvetica, sans-serif;
		font-size:14px;
		outline: none;
		cursor: pointer;
		text-align: center;
		text-decoration: none;
		color: #ffffff;
		border: solid 1px #0076a3; border-right:0px;
		background: #0095cd;
		background: -webkit-gradient(linear, left top, left bottom, from(#00adee), to(#0078a5));
		background: -moz-linear-gradient(top,  #00adee,  #0078a5);
		border-top-right-radius: 5px 5px;
		border-bottom-right-radius: 5px 5px;
		
	}
	.tfbutton:hover {
		text-decoration: none;
		background: #007ead;
		background: -webkit-gradient(linear, left top, left bottom, from(#0095cc), to(#00678e));
		background: -moz-linear-gradient(top,  #0095cc,  #00678e);
	}
	/* Fixes submit button height problem in Firefox */
	.tfbutton::-moz-focus-inner {
	  border: 0;
	}
	.tfclear{
		clear:both;
	}
	
</style>
</head>
<body>
	<div class="container">
	        <div class="container-fluid">
	            <div class="row">
	                    
	
					<!-- HTML for SEARCH BAR -->
				<div id="tfheader">
					<form id="tfnewsearch" class="form-inline" method="get" action="search">
					
			        	<h1 style="text-align:left"><a href="index.html">ICS Search Engine </a></h1>
			        	<input type="text" class="tftextinput" name="q" size="65" maxlength="120" value="<%=request.getQueryString()%>" /><input type="submit" value="search" class="tfbutton">
					</form>
					<div class="tfclear"></div>
				</div>
					<hr>
				</div>	
				<!-- This is where all of our search results will be placed -->
				<div class = "row">
				 <% 

				 List<String> listData = (ArrayList<String>) request.getAttribute("listData");
				 int listSize = listData.size();
				 
				 for (String s: listData){
					 
				 }
				 
				 %>
				
				</div>
			</div>
	</div>

</body>
</html>