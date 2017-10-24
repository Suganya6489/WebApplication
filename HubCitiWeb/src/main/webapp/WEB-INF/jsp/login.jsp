<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<script type="text/javascript" src="scripts/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/jquery.form.js"></script>
<script type="text/javascript"
	src="/HubCiti/scripts/colorPickDynamic.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/colorPicker.js"></script>
<link rel="stylesheet" type="text/css"
	href="/HubCiti/styles/colorPicker.css" />

<div class="wrpr-cont relative">
	<div class="content zeroBg">
		<div id="login-wrpr">
			<div id="login-hdr">Login</div>
			<div id="login-cont">
				
			<form name='f' action="<c:url value='j_spring_security_check' />" method='POST'>
				<div class="cont-pdng pdngTop" id="loginSec">
					<c:if test="${not empty error}">
						<div class="errorblock">
							Your login attempt was not successful, try again.<br /> Caused :
							${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
						</div>
					</c:if>
					<ul>
						<li>
							<span class="floatL">
								<label class="mand">User Name</label>
							</span> 
							<input type="text" name="j_username" class="inputTxt" id="userName" name="userName" value="${requestScope.loggedinUserName }"/>
							<span class="clear"></span> 
							<i class="errDisp">Please enter Username</i>
						</li>
						<li>
							<span class="floatL">
								<label class="mand">Password</label>
							</span> 
							<input type="password" class="inputTxt" name='j_password' id="password" name="password"/>
							<span class="clear"></span> 
							<i class="errDisp">Please enter Password</i>
						</li>
						<li>
							<span>&nbsp;</span>
							<input type="button" class="btn-blue" value="Login" id="login" name="button" onclick="validateUserForm(['#password','#userName'],'li',chkUser);"> 
							<a href="forgetpwd.htm">Forgot Password?</a>
						</li>
					</ul>
				</div>
			</form>
			</div>
			<div id="login-ftr"></div>
		</div>
	</div>
</div>