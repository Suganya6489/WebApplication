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
	<div id="slideBtn">
		<a href="#" onclick="revealPanel(this);" title="Hide Menu"><img
			src="images/slide_off.png" width="11" height="28" alt="btn_off" /></a>
	</div>
	<div id="bread-crumb">
		<ul>
			<li class="scrn-icon"><span class="icon-home">&nbsp;</span></li>
			<li><a href="welcome.htm">Home</a></li>
			<li class="last">Setup Registration</li>
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
						<li class="title-icon"><span class="icon-registration">&nbsp;</span></li>
						<li>Registration</li>
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
						<form:hidden path="logoImageName" />
						<form:hidden path="oldImageName" />
						<table width="100%" border="0" cellpadding="0" cellspacing="0"
							class="cmnTbl">
							<tr>
								<td><label class="mand">Title</label></td>
								<td><form:errors cssClass="errorDsply" path="pageTitle"></form:errors>
									<div class="cntrl-grp">

										<form:input type="text" path="pageTitle" id="pageTitle"
											class="inputTxtBig" tabindex="1" />
									</div></td>
							</tr>
							<tr>
								<td valign="top"><label class="mand">Content</label></td>
								<td><form:errors cssClass="errorDsply" path="pageContent"></form:errors>
									<div class="cntrl-grp">

										<form:textarea path="pageContent" class="textareaTxt" tabindex="2" ></form:textarea>
									</div></td>
							</tr>
							<tr>
								<td valign="top"><span class="pick-color-label"><label
										class="mand">Background Color</label></span></td>
								<td><form:errors cssClass="errorDsply" path="bgColor"></form:errors>
									<input class="bgColorSpectrum" id="bgColorSpectrumClr"
									value='#d9d9d9' tabindex="3"  /> <form:hidden path="bgColor"
										class="inputTxt" id="bgColor" name="bgColor" /></td>
							</tr>
							<tr>
								<td valign="top"><span class="pick-color-label"><label
										class="mand">Font Color</label></span></td>
								<td><form:errors cssClass="errorDsply" path="fontColor"></form:errors>
									<input class="fontColorSpectrum" tabindex="4"  /> <form:hidden
										path="fontColor" class="inputTxt" id="fontColor"
										name="fontColor" /></td>
							</tr>
							<tr>
								<td valign="top"><span class="pick-color-label"><label
										class="mand">Button Color</label></span></td>
								<td><form:errors cssClass="errorDsply" path="btnColor"></form:errors>
									<input class="btnColorSpectrum"  tabindex="5" /> <form:hidden
										path="btnColor" class="inputTxt" name="btnColor" id="btnColor" />

								</td>
							</tr>
							<tr>
								<td valign="top"><span class="pick-color-label"><label
										class="mand">Button Font Color</label></span></td>
								<td><form:errors cssClass="errorDsply" path="btnFontColor"></form:errors>
									<input class="btnFontColorSpectrum" tabindex="6" /> <form:hidden
										path="btnFontColor" class="inputTxt" name="btnFontColor"
										id="btnFontColor" /></td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><input type="button" name="button" id="button"
									onclick="saveRegScreenSettings();" title="${sessionScope.btnname}"
									value="${sessionScope.btnname}" class="btn-blue" tabindex="7"  /></td>
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
						<div id="iphone-registration-preview">
							<div class="iphone-status-bar"></div>
							<div class="navBar iphone">
								<table width="100%" border="0" cellspacing="0" cellpadding="0"
									class="titleGrd">
									<tr>
										<td style="font-size: 14px; text-align: center"><b>Registration</b></td>
									</tr>
								</table>
							</div>
							<div class="previewAreaScroll">
								<div class="iPhone-preview-container">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td><h1 align="center" class="max-margin-top"
													id="titlePreview">Registration Successful</h1></td>
										</tr>
										<tr>
											<td><p  class="max-margin-top cont-height wraptxt"
													id="contentPreview">ScanSee does not share or sell your
													private information to anyone. Ever. However, we do want to
													know about you to help us provide relevent and timely
													information that will be of interest to you exclude
													information we know you won't like.</p></td>
										</tr>
										<tr>
											<td><input type="button" value="Set Preferences"
												name="setPreferences" id="setPreferences" title="Set Preferences"></td>
										</tr>
										<tr>
											<td><input type="button" value="Get Started"
												name="getStarted" id="getStarted" title="Get Started"></td>
										</tr>
									</table>
								</div>
							</div>
						</div>

						<!--Iphone Preview For Login screen ends here-->
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
		<iframe frameborder="0" scrolling="auto" id="ifrm" src=""
			height="100%" allowtransparency="yes" width="100%"
			style="background-color: White"> </iframe>
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

		$("#iphone-registration-preview").css("background-color", bgColor);
		$("#iphone-registration-preview").css("color", fontColor);
		$("#iphone-registration-preview h1").css("color", fontColor);
		$("#iphone-registration-preview input[type='button']").css(
				"background-color", btnColor);
		$("#iphone-registration-preview input[type='button']").css("color",
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

	$("#pageTitle").keyup(function() {
		$("#titlePreview").text($("#pageTitle").val());

	});

	$("#pageContent").keyup(function() {
		$("#contentPreview").text($("#pageContent").val());

	});
	$("#pageTitle").change(function() {
		$("#titlePreview").text($("#pageTitle").val());

	});
	$("#pageContent").change(function() {
		$("#contentPreview").text($("#pageContent").val());

	});

	$(document).ready(function() {
		
		var pageTitle=$("#pageTitle").val();
		var pageContent=$("#pageContent").val();
		
		if(pageTitle==""){
			
			$("#titlePreview").text("Title goes here..");	
		}else{
			
			$("#titlePreview").text(pageTitle);
		}
		
		if(pageContent==""){
			$("#contentPreview").text("Content goes here..");
			
		}else{
			
			$("#contentPreview").text(pageContent);
		}
		
		
	});
</script>
<script type="text/javascript">
	configureMenu("setupregistration");
</script>