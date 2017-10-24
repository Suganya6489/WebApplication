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

<div id="wrpr">
	<div class="clear"></div>
	<div class="wrpr-cont relative">
		<div id="slideBtn">
			<a href="#" onclick="revealPanel(this);" title="Hide Menu"><img
				src="images/slide_off.png" width="11" height="28" alt="btn_off" /></a>
		</div>
		<div id="bread-crumb">
			<ul>
				<li class="scrn-icon"><span class="icon-home">&nbsp;</span></li>
				<li><a href="welcome.htm">Home</a></li>
				<li class="last">Setup Login Screen</li>
			</ul>
		</div>
		<span class="blue-brdr"></span>
		<div class="content" id="login">
			<div id="menu-pnl" class="split">
				<jsp:include page="leftNavigation.jsp"></jsp:include>
			</div>
			<div class="cont-pnl split" id="equalHt">
				<div class="cont-block rt-brdr">
					<div class="title-bar">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-login">&nbsp;</span></li>
							<li>Login Details</li>
						</ul>
					</div>
					<div class="cont-wrp">
						<c:choose>
							<c:when test="${requestScope.responseStatus eq 'SUCCESS' }">

								<div class="alertBx success">
									<span class="actn-close" title="close"></span>
									<p class="msgBx">
										<c:out value="${requestScope.responeMsg}" />
									</p>
								</div>

							</c:when>

							<c:when test="${requestScope.responseStatus eq 'FAILURE' }">

								<div class="alertBx failure">
									<span class="actn-close" title="close"></span>
									<p class="msgBx">
										<c:out value="${requestScope.responeMsg}" />
									</p>
								</div>

							</c:when>

						</c:choose>



						<form:form name="screenSettingsForm" id="screenSettingsForm"
							commandName="screenSettingsForm" enctype="multipart/form-data"
							action="uploadimg.htm">

							<form:hidden path="oldImageName" />
							<form:hidden path="viewName" value="setuploginpage" />
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="cmnTbl">
								<tr>
									<td width="36%"><label class="mand">Choose Logo</label></td>

									<td width="64%"><form:errors cssClass="errorDsply"
											path="logoImageName"></form:errors> <label><img
											id="loginScreenLogo" width="80" height="80" alt="upload"
											src="${sessionScope.loginScreenLogo }"> </label> <span
										class="topPadding cmnRow"><label for="trgrUpld">
												<input type="button" value="Upload" id="trgrUpldBtn"
												class="btn trgrUpld" title="Upload Image File" tabindex="1">
												<form:hidden path="logoImageName" id="strBannerAdImagePath" />
												<span class="instTxt nonBlk"></span> <form:input type="file"
													class="textboxBig" id="trgrUpld" path="logoImage"
													onchange="checkBannerSize(this);" />
										</label> </span><label id="maxSizeImageError" class="errorDsply"></label></td>

								</tr>
								<tr>
									<td valign="top"><span class="pick-color-label"><label
											class="mand">Background Color</label></span></td>
									<td><form:errors cssClass="errorDsply" path="bgColor"></form:errors>
										<input class="bgColorSpectrum" id="bgColorSpectrumClr"
										value='#d9d9d9' tabindex="2" /> <form:hidden path="bgColor"
											class="inputTxt" id="bgColor" name="bgColor" /></td>
								</tr>
								<tr>

									<td valign="top"><span class="pick-color-label"><label
											class="mand">Font Color</label></span></td>
									<td><form:errors cssClass="errorDsply" path="fontColor"></form:errors>
										<input class="fontColorSpectrum" tabindex="3" /> <form:hidden
											path="fontColor" class="inputTxt" id="fontColor"
											name="fontColor" /></td>
								</tr>
								<tr>

									<td valign="top"><span class="pick-color-label"><label
											class="mand">Button Color</label></span></td>
									<td><form:errors cssClass="errorDsply" path="btnColor"></form:errors>
										<input class="btnColorSpectrum" tabindex="4" /> <form:hidden
											path="btnColor" class="inputTxt" name="btnColor"
											id="btnColor" /></td>
								</tr>
								<tr>

									<td valign="top"><span class="pick-color-label"><label
											class="mand">Button Font Color</label></span></td>
									<td><form:errors cssClass="errorDsply" path="btnFontColor"></form:errors>
										<input class="btnFontColorSpectrum" tabindex="5" /> <form:hidden
											path="btnFontColor" class="inputTxt" name="btnFontColor"
											id="btnFontColor" /></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td><input type="button" name="loginSettings"
										onclick="saveLoginSettings();" id="loginSettings"
										title="${sessionScope.btnname}"
										value="${sessionScope.btnname}" class="btn-blue" tabindex="6" /></td>
								</tr>
							</table>
						</form:form>


					</div>
				</div>

				<div class="cont-block">
					<div class="title-bar">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-iphone">&nbsp;</span></li>
							<li>App Preview</li>
						</ul>
					</div>
					<div class="cont-wrp">
						<div id="iphone-preview">
							<!--Iphone Preview For Login screen starts here-->
							<div id="iphone-login-preview">
								<div class="iphone-status-bar"></div>
								<div class="iPhone-preview-container">
									<div class="iphone-login-logo">
										<img src="${sessionScope.loginScreenLogoPreview}" alt="Logo"
											width="280" height="140" />
									</div>
									<ul>
										<li><label>Username</label> <span> <input
												type="text" id="usernameText" name="usernameText" />
										</span></li>
										<li class="min-margin-top"><label>Password</label> <span>
												<input type="password" id="passwordBox" name="passwordBox" />
										</span></li>
										<li><input type="button" id="login" name="login"
											value="Login" title="Login" /></li>
										<li><input type="button" id="signUp" name="signUp"
											value="Sign Up" class="min-margin-top" title="Sign Up" /></li>
									</ul>
								</div>
								<div class="powered-by-logo">
									<lable>Powered by</lable>
									<span class="powered-logo"></span>
								</div>
							</div>
							<!--Iphone Preview For Login screen ends here-->
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
	<div class="ifrmPopupPannel" id="ifrmPopup" style="display: none;">
		<div class="headerIframe">
			<img src="/HubCiti/images/popupClose.png" class="closeIframe"
				alt="close"
				onclick="javascript:closeIframePopup('ifrmPopup','ifrm')"
				title="Click here to Close" align="middle" /> <span
				id="popupHeader"></span>
		</div>
		<iframe frameborder="0" scrolling="no" id="ifrm" src="" height="100%"
			allowtransparency="yes" width="100%" style="background-color: White">
		</iframe>
	</div>
</div>

<script type="text/javascript">
	function loadSetupScreen() {
		var bgColor = $('#bgColor').val();
		var fontColor = $('#fontColor').val();
		var btnColor = $('#btnColor').val();
		var btnFontColor = $('#btnFontColor').val();

		if (bgColor == "") {
			$('#bgColor').val("#d9d9d9");
		}

		if (fontColor == "") {
			$('#fontColor').val("#000000");
		}
		if (btnColor == "") {
			$('#btnColor').val("#000000");
		}

		if (btnFontColor == "") {
			$('#btnFontColor').val("#FFFFFF");
		}

		//$(".bgColorSpectrum").spectrum("set", bgColor);

		$("#iphone-login-preview").css("background-color", bgColor);
		$("#iphone-login-preview label").css("color", fontColor);
		$("#iphone-login-preview").css("color", fontColor);
		$("#iphone-login-preview input[type='button']").css("background-color",
				btnColor);
		$("#iphone-login-preview input[type='button']").css("color",
				btnFontColor);
	}

	loadSetupScreen();
</script>
<script type="text/javascript">
	$(document).ready(function() {
		var bgColor = $('#bgColor').val();
		var fontColor = $('#fontColor').val();
		var btnColor = $('#btnColor').val();
		var btnFontColor = $('#btnFontColor').val();
		//alert(bgColor)
		$(".bgColorSpectrum").spectrum("set", bgColor);
		$(".fontColorSpectrum").spectrum("set", fontColor);
		$(".btnColorSpectrum").spectrum("set", btnColor);
		$(".btnFontColorSpectrum").spectrum("set", btnFontColor);
	});
</script>

<script type="text/javascript">
	configureMenu("setuploginpage");
</script>