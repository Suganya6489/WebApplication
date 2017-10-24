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
				<li class="last">Setup Splash Screen</li>
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
							<li class="title-icon"><span class="icon-welcome">&nbsp;</span></li>
							<li>Splash Screen</li>
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
							<form:hidden path="viewName" value="setupsplashscreen" />
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="cmnTbl">
								<tr>
								<tr>
									<td width="36%"><label class="mand">Splash Image</label></td>

									<td width="64%"><form:errors cssClass="errorDsply"
											path="logoImageName"></form:errors> <label><img
											id="loginScreenLogo" width="80" height="80" alt="upload"
											src="${sessionScope.splashImage }"> </label> <span
										class="topPadding cmnRow"><label for="trgrUpld">
												<input type="button" value="Upload" id="trgrUpldBtn"
												class="btn trgrUpld" title="Upload Image File" tabindex="1">
												<form:hidden path="logoImageName"
													id="strBannerAdImagePath" /> <span class="instTxt nonBlk"></span>
												<form:input type="file" class="textboxBig" id="trgrUpld"
													path="logoImage" onchange="checkBannerSize(this);" />
										</label> </span><label id="maxSizeImageError" class="errorDsply"></label></td>

								</tr>
								</tr>

								<tr>
									<td>&nbsp;</td>
									<td><input type="button" name="button" id="button"
										onclick="saveSplashScreenSettings();" tabindex="2"
										value="${sessionScope.btnname}"
										title="${sessionScope.btnname}" class="btn-blue" /></td>
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
							<div id="iphone-welcome-preview">
								<div class="iphone-status-bar"></div>
								<div>
									<img src="${sessionScope.splashImagePreview}" width="320"
										height="460" alt="Welcome Page" />
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
	configureMenu("setupsplashscreen");
</script>