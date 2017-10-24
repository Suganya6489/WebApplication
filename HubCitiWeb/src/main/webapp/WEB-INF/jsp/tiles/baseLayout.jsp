<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><tiles:getAsString name="title" ignore="true" /></title>
<link rel="shortcut icon" href="/HubCiti/images/hubciti.png" />
</head>

<body onload="resizeDoc();" onresize="resizeDoc();">
	<tiles:insertAttribute name="header" ignore="true" />	
	<div id="wrpr">
		<tiles:insertAttribute name="body" ignore="true" />
	</div>
	<tiles:insertAttribute name="footer" ignore="true" />
</body>
</html>
