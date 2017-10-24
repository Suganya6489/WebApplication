<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<script type="text/javascript" src="scripts/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/jquery.form.js"></script>
<script type="text/javascript"
	src="/HubCiti/scripts/colorPickDynamic.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/colorPicker.js"></script>
<script src="/HubCiti/scripts/ckeditor/ckeditor.js"></script>
<script src="/HubCiti/scripts/ckeditor/config.js"></script>

<link rel="stylesheet" type="text/css"
	href="/HubCiti/styles/colorPicker.css" />
	
<style>
.error {
	color: #ff0000;
	font-style: italic;
	text-align: center;
}
</style>
	
<div class="wrpr-cont relative">
	<div class="content zeroBg">
		<div id="login-wrpr">
			<div id="login-hdr">Change Password</div>
				<div id="login-cont">
					<form:form commandName="login" name="login">
						<div class="cont-pdng pdngTop" id="loginSec">
							<ul class="clear-fix chngpswd">	
								<form:errors path="*" cssStyle="color:red"></form:errors>
						        <li> 
							        
							        	<span class="floatL">
							            	<label class="mand">New Password</label>
							          	</span> 
							          	<form:password path="password" id="pswdNew" tabindex="1" cssClass="inputText"/>
							          	<span class="clear"></span> 
							          	<i class="errDisp">Please enter Password</i> 
						        </li>								  
						        <li>
						        	
						        	<span class="floatL">
						            <label class="mand">Confirm Password</label>
						          	</span> 
						          	<form:password path="confirmPassword" id="pswdCfrm" tabindex="2" cssClass="inputText"/>
						          	<span class="clear"></span> 
						          	<i class="errDisp">Please enter Confirm Password</i> 
						        </li>								  
						        <li>
						        	<span>&nbsp;</span>
						        	<input type="button" name="button" id="login" value="Save" class="btn-blue" onclick="validateUserForm(['#pswdNew','#pswdCfrm'],'li',chkPwd);"/>
						        </li>
						      </ul>
						</div>
				</form:form>
			</div>
			<div id="login-ftr"></div>
		</div>
	</div>
</div>