<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>ScanSee:HubCiti</title>
<link rel="stylesheet" type="text/css"  href="styles/style.css" />
<link rel="stylesheet" type="text/css" href="styles/colorPicker.css"/>
<script type="text/javascript" src="scripts/jquery-1.10.2.js"></script>
<script type="text/javascript" src="scripts/colorPickDynamic.js"></script>
<script type="text/javascript" src="scripts/colorPicker.js"></script>
<script type="text/javascript" src="scripts/global.js"></script>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
</head>
<body onload="resizeDoc();" onresize="resizeDoc();">
<form:form name="forgetpwdform" commandName="forgetpwdform">
<div id="wrpr">
  <div class="wrpr-cont relative">
    <div class="content zeroBg">
      <div id="login-wrpr">
        <div id="login-hdr" class="">Forgot Password</div>
        <div id="login-cont">
          <div class="cont-pdng pdngTop">
            <ul class="clear-fix">
            <c:if test="${requestScope.msgResponse ne null }">
								           <li>
													<div id="message">
														<center>
															 <label  style="${requestScope.fontStyle}">${requestScope.msgResponse}</label>
														</center>
													</div>
													<script>var PAGE_MESSAGE = true;</script>
										   </li>
										  </c:if>
            
            
           <li>Please enter your UserName and Auto-Generated password will be email to you.</li>
              <li><span><label 	class="mandTxt">User Name</label></span>
                <form:input type="text" class="inputText" path="userName" id="usn"  onKeyPress="return forgetPwd(event)" tabindex="1"/>
              </li>
              <li><span>&nbsp;</span>
                <input type="submit" class="btn-blue" value="Send" id="login" name="button" 
			title="Send"	onclick="return forgetPwd('')" tabindex="2"/>
                <a href="login.htm">Go back to login</a> </li>
            </ul>
          </div>
        </div>
        <div id="login-ftr"></div>
      </div>
    </div>
  </div>
</div>
</form:form>
</body>
</html>
