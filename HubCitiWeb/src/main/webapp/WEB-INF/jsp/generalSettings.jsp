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
							<form:hidden path="viewName" value="generalsettings" />
							<form:hidden path="uploadImageType" id="uploadImageType"
								value="logoImage" />
							<form:hidden path="oldAppIconImage" />

							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="cmnTbl">
								<tr>
								<tr>
									<td width="40%"><label class="mand">Title Bar Logo</label></td>

									<td width="60%"><form:errors cssClass="errorDsply"
											path="logoImageName"></form:errors> <label> <img
											id="loginScreenLogo" width="86" height="34" alt="upload"
											src="${sessionScope.titleBarLogo}">
									</label> <span class="topPadding cmnRow"> <label for="trgrUpld">
												<input type="button" value="Upload" id="trgrUpldBtn"
												class="btn trgrUpld" title="Upload Image File" tabindex="1">
												<form:hidden path="logoImageName" 													id="strBannerAdImagePath" /> <span class="instTxt nonBlk"></span>
												<form:input type="file" class="textboxBig" id="trgrUpld"
													path="logoImage" onchange="checkBannerSize(this);" />
										</label>
									</span><label id="maxSizeImageError" class="errorDsply"></label></td>
								</tr>


								<tr id="">

									<td width="40%"><label class="mand">App Icon</label></td>
									<td width="60%"><form:errors cssClass="errorDsply"
											path="bannerImageName"></form:errors> <label> <img
											id="twoColTmptImg" width="57" height="57" alt="upload"
											src="${sessionScope.appiconpreview}">
									</label> <span class="topPadding cmnRow"> <label
											for="trgrUpldImg"> <input type="button"
												value="Upload" id="trgrUpldBtnImg" class="btn trgrUpldImg"
												title="Upload Image File" tabindex="2"> <form:hidden
													path="bannerImageName" id="bannerImageName" />
												<span class="instTxt nonBlk"></span> <form:input type="file"
													class="textboxBig" id="trgrUpldImg" path="imageFile" />
										</label>
									</span><label id="maxSizeAppIconError" class="errorDsply"></label></td>
								</tr>

								<tr>
									<td><label class="mand">Bottom Button Type</label></td>
									<td colspan="2"><form:errors cssClass="errorDsply"
											path="bottomBtnId"></form:errors> <c:choose>
											<c:when test="${sessionScope.buttomBtn eq 'Exist'}">
												<c:forEach items="${sessionScope.buttomBtnType}"
													var="buttomBtn">
													<form:radiobutton path="bottomBtnId"
														value="${buttomBtn.bottomBtnId}" disabled="true"
														tabindex="3" />${buttomBtn.bottomBtnName}&nbsp;&nbsp;&nbsp;									
										</c:forEach>
											</c:when>
											<c:otherwise>
												<c:forEach items="${sessionScope.buttomBtnType}"
													var="buttomBtn">
													<form:radiobutton path="bottomBtnId"
														value="${buttomBtn.bottomBtnId}" tabindex="4" />${buttomBtn.bottomBtnName}&nbsp;&nbsp;&nbsp;									
										</c:forEach>
											</c:otherwise>
										</c:choose></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td><input type="button" name="button" id="button"
										title="${sessionScope.btnname}"
										onclick="saveGeneralSettings('TabBarLogo');"
										value="${sessionScope.btnname}" class="btn-blue" tabindex="2" />
									</td>
								</tr>
							</table>
						</form:form>
					</div>
				</div>
				<div class="cont-block">
					<div class="title-bar">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-iphone">&nbsp;</span>
							</li>
							<li>Preview</li>
						</ul>
					</div>
					<div class="cont-wrp">
						<div id="iphone-preview">
							<!--Iphone Preview For Login screen starts here-->
							<div id="iphone-aboutus-preview">
								<div class="iphone-status-bar"></div>
								<div class="navBar iphone">
									<table width="100%" border="0" cellspacing="0" cellpadding="0"
										class="titleGrd">
										<tr>

											<td width="54%" class="genTitle"><img
												src="${sessionScope.titleBarLogoPreview }"
												onerror="this.src='images/small-logo.png';" width="86"
												height="35" alt="Logo" /></td>

										</tr>
									</table>
								</div>
								<div class="previewAreaScroll">
									<div class="iPhone-preview-container"></div>
								</div>
							</div>
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
	$(document)
			.ready(
					function() {

						$('#trgrUpldImg')
								.bind(
										'change',
										function() {

											var imageType = document
													.getElementById("trgrUpldImg").value;
											var uploadImageType = $(this).attr(
													'name');

											document.screenSettingsForm.uploadImageType.value = uploadImageType;
											if (imageType != '') {
												var checkbannerimg = imageType
														.toLowerCase();
												if (!checkbannerimg
														.match(/(\.png|\.jpeg|\.jpg|\.gif|\.bmp)$/)) {
													alert("You must upload image with following extensions : .png, .gif, .bmp, .jpg, .jpeg");
													return false;
												} else {
													$("#screenSettingsForm")
															.ajaxForm(
																	{
																		success : function(
																				response) {
																			var imgRes = response
																					.getElementsByTagName('imageScr')[0].firstChild.nodeValue
																			var substr = imgRes
																					.split('|');
																			if (substr[0] == 'maxSizeImageError') {
																				$(
																						'#maxSizeAppIconError')
																						.text(
																								"Image Dimension should not exceed Width: 800px Height: 600px");
																			} else {
																				openIframePopup(
																						'ifrmPopup',
																						'ifrm',
																						'/HubCiti/cropImage.htm',
																						100,
																						99.5,
																						'Crop Image');
																			}
																		}
																	}).submit();
												}
											}

										});
					});
</script>

<script type="text/javascript">
	configureMenu("generalsettings");
</script>