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

<script type="text/javascript">
	$(document)
			.ready(
					function() {
						var bgColor = $('#bgColor').val();
						var btnColor = $('#btnColor').val();
						var btnFontColor = $('#btnFontColor').val();
						var grpColor = $('#grpColor').val();
						var grpFontColor = $('#grpFontColor').val();
						var iconsFontColor = $('#iconsFontColor').val();

						if (bgColor == "") {
							$(".bgColorSpectrum").spectrum("set", "#D1D1D1");
							$('#bgColor').val("#D1D1D1");
						} else {
							$(".bgColorSpectrum").spectrum("set", bgColor);
						}
						if (btnColor == "") {
							$(".btnColorSpectrum").spectrum("set", "#186A99");
							$('#btnColor').val("#186A99");
						} else {
							$(".btnColorSpectrum").spectrum("set", btnColor);
						}
						if (btnFontColor == "") {
							$(".btnFontColorSpectrum").spectrum("set",
									"#FFFFFF");
							$('#btnFontColor').val("#FFFFFF");
						} else {
							$(".btnFontColorSpectrum").spectrum("set",
									btnFontColor);
						}
						

						
						if (grpColor == "") {
							$(".grpColorSpectrum").spectrum("set", "#1A4A6E");
							$('#grpColor').val("#1A4A6E");
						} else {
							$(".grpColorSpectrum").spectrum("set", grpColor);
						}
						
						if (grpFontColor == "") {
							$(".grpFontColorSpectrum").spectrum("set",
									"#FFFFFF");
							$('#grpFontColor').val("#FFFFFF");
						} else {
							$(".grpFontColorSpectrum").spectrum("set",
									grpFontColor);
						}

						if (iconsFontColor == "") {
							$(".iconsFontColorSpectrum").spectrum("set",
									"#FFFFFF");
							$('#iconsFontColor').val("#FFFFFF");
						} else {
							$(".iconsFontColorSpectrum").spectrum("set",
									iconsFontColor);
						}
						
						
						
						$("input[name='iconSelection']:radio")
								.change(
										function() {
											var getOptn = $(this).attr("id");
											var getVal = $(this).attr("value");
											var bgColor = $('#bgColor').val();

											$(".tgl-row").hide();
											$("." + getOptn).show();
											if ("image" == getVal) {
												var imageUrl = "${sessionScope.titleBarLogoPreview}";
												if (imageUrl == ""
														|| imageUrl == "small-logo.png") {
													$(
															"#iphone-registration-preview")
															.css("background",
																	"#D1D1D1");
												} else {
													$(
															"#iphone-registration-preview")
															.css(
																	{
																		'background' : 'url('
																				+ imageUrl
																				+ ')',
																		'background-repeat' : 'no-repeat'
																	});
												}
											} else {
												$(
														"#iphone-registration-preview")
														.css("background",
																bgColor);
											}
										});

						var chkdOptn = $(
								"input[name='iconSelection']:radio:checked")
								.attr("id");
						var chkdOptnVal = $(
								"input[name='iconSelection']:radio:checked")
								.attr("value");

						$("." + chkdOptn).show();

						if ("image" == chkdOptnVal) {
							var imageUrl = "${sessionScope.titleBarLogoPreview}";
							$("#iphone-registration-preview").css({
								'background' : 'url(' + imageUrl + ')',
								'background-repeat' : 'no-repeat'
							});
						} else {
							$("#iphone-registration-preview").css("background",
									bgColor);
						}

						$("#iphone-registration-preview input[type='button']")
								.css("background-color", btnColor);
						$("#iphone-registration-preview input[type='button']")
								.css("color", btnFontColor);
					});
</script>


<div id="wrpr">
	<div class="clear"></div>
	<div class="wrpr-cont relative">
		<div id="slideBtn">
			<a href="#" onclick="revealPanel(this);" title="Hide Menu"><img
				src="images/slide_off.png" width="11" height="28" alt="btn_off" />
			</a>
		</div>
		<div id="bread-crumb">
			<ul>
				<li class="scrn-icon"><span class="icon-home">&nbsp;</span></li>
				<li><a href="welcome.htm">Home</a></li>
				<li class="last">General Settings</li>
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
							<li class="title-icon"><span class="icon-settings">&nbsp;</span>
							</li>
							<li>General Settings</li>
						</ul>
					</div>
					<ul class="tabd-nav">
						<li><c:choose>
								<c:when test="${sessionScope.menuType eq 'MainMenu'}">
									<a href="#" class="active"
										onclick="displayGeneralSettings('MainMenu')">Main-Menu</a>
								</c:when>
								<c:otherwise>
									<a href="#" onclick="displayGeneralSettings('MainMenu')">Main-Menu</a>
								</c:otherwise>
							</c:choose></li>
						<li><c:choose>
								<c:when test="${sessionScope.menuType eq 'SubMenu'}">
									<a href="#" class="active"
										onclick="displayGeneralSettings('SubMenu')">Sub-Menu</a>
								</c:when>
								<c:otherwise>
									<a href="#" onclick="displayGeneralSettings('SubMenu')">Sub-Menu</a>
								</c:otherwise>
							</c:choose></li>
						<li><c:choose>
								<c:when test="${sessionScope.menuType eq 'TabBarLogo'}">
									<a href="#" class="active"
										onclick="displayGeneralSettings('TabBarLogo')">Others</a>
								</c:when>
								<c:otherwise>
									<a href="#" onclick="displayGeneralSettings('TabBarLogo')">Others</a>
								</c:otherwise>
							</c:choose></li>
					</ul>
					<span class="clear"></span>
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
							<form:hidden path="pageType" />
							<form:hidden path="viewName" value="menugeneralsettings" />
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="cmnTbl">
								<tr>
									<td width="40%">Background</td>
									<td width="60%" class="actn-cntrl"><form:errors
											cssClass="errorDsply" path="iconSelection"></form:errors> <span
										class=""> <form:radiobutton path="iconSelection"
												id="bg-image" value="image" cssClass="" /> <label
											for="bg-image">Image</label>
									</span> <span class="mrgn-left">&nbsp;&nbsp;&nbsp; <form:radiobutton
												path="iconSelection" id="bg-color" value="color" cssClass="" />
											<label for="bg-color">Color</label>
									</span></td>
								</tr>
								<tr class="bg-image tgl-row rowHt">
									<td width="40%">&nbsp;</td>
									<td width="64%"><form:errors cssClass="errorDsply"
											path="logoImageName"></form:errors> <label> <img
											id="loginScreenLogo" width="80" height="80" alt="upload"
											src="${sessionScope.titleBarLogo}">
									</label> <span class="topPadding cmnRow"> <label for="trgrUpld">
												<input type="button" value="Upload" id="trgrUpldBtn"
												class="btn trgrUpld" title="Upload Image File"> <form:hidden
													path="logoImageName" id="strBannerAdImagePath" /> <span class="instTxt nonBlk"></span>
												<form:input type="file" class="textboxBig" id="trgrUpld"
													path="logoImage" onchange="checkBannerSize(this);" />
										</label>
									</span><label id="maxSizeImageError" class="errorDsply"></label></td>
								</tr>
								<tr class="bg-color tgl-row rowHt">
									<td>&nbsp;</td>
									<td><form:errors cssClass="errorDsply" path="bgColor"></form:errors>
										<input class="bgColorSpectrum" id="bgColorSpectrumClr" /> <form:hidden
											path="bgColor" class="inputTxt" id="bgColor" name="bgColor" />
									</td>
								</tr>
								<tr>
									<td valign="top"><span class="pick-color-label"><label
											class="mand">Button Color</label> </span></td>
									<td><form:errors cssClass="errorDsply" path="btnColor"></form:errors>
										<input class="btnColorSpectrum" /> <form:hidden
											path="btnColor" class="inputTxt" name="btnColor"
											id="btnColor" /></td>
								</tr>
								<tr>
									<td valign="top"><span class="pick-color-label"><label
											class="mand">Button Font Color</label> </span></td>
									<td><form:errors cssClass="errorDsply" path="btnFontColor"></form:errors>
										<input class="btnFontColorSpectrum" /> <form:hidden
											path="btnFontColor" class="inputTxt" name="btnFontColor"
											id="btnFontColor" /></td>
								</tr>
								<tr>
								<td valign="top"><span class="pick-color-label"><label>Group Background</label> </span></td>
									<td>
										<input class="grpColorSpectrum" /> <form:hidden
											path="grpColor" class="inputTxt" name="grpColor"
											id="grpColor" /></td>
								
								</tr>
								<tr>
								<td valign="top"><span class="pick-color-label"><label>Group Font Color</label> </span></td>
									<td>
										<input class="grpFontColorSpectrum" /> <form:hidden
											path="grpFontColor" class="inputTxt" name="grpFontColor"
											id="grpFontColor" /></td>
								
								</tr>
								<tr>
								<td valign="top"><span class="pick-color-label"><label>Icons Font Color</label> </span></td>
									<td>
										<input class="iconsFontColorSpectrum" /> <form:hidden
											path="iconsFontColor" class="inputTxt" name="iconsFontColor"
											id="iconsFontColor" /></td>
								
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td><c:choose>
											<c:when test="${sessionScope.menuType eq 'SubMenu'}">
												<input type="button" name="button" id="button"
													title="${sessionScope.btnname}"
													onclick="saveGeneralSettings('SubMenu');"
													value="${sessionScope.btnname}" class="btn-blue"
													tabindex="2" />
											</c:when>
											<c:otherwise>
												<input type="button" name="button" id="button"
													title="${sessionScope.btnname}"
													onclick="saveGeneralSettings('MainMenu');"
													value="${sessionScope.btnname}" class="btn-blue"
													tabindex="2" />
											</c:otherwise>
										</c:choose></td>
								</tr>
							</table>
						</form:form>
					</div>
				</div>
				<div class="cont-block">
					<div class="title-bar">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-iphone">&nbsp;</span></li>
							<li>Preview</li>
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
											<td width="19%">&nbsp;</td>
											<td width="54%">
											<td width="27%"></td>
										</tr>
									</table>
								</div>
								<div class="previewAreaScroll">
									<div class="iPhone-preview-container">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td><input type="button" value="Button"
													name="ButtonName" id="ButtonId" title="Button"></td>
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
	configureMenu("generalsettings");
</script>
