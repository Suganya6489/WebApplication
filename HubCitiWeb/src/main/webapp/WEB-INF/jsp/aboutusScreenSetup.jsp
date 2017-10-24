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

<script type="text/javascript">
	$(document)
			.ready(
					function() {

						CKEDITOR
								.on(
										'instanceCreated',
										function(e) {
											//	alert("q : "+e.editor.name);
											var editorName = e.editor.name;
											$("#contentPreview").text(
													"Content goes here..");

											e.editor
													.on(
															'change',
															function(ev) {

																document
																		.getElementById('contentPreview').innerHTML = ev.editor
																		.getData();

															});
										});

						CKEDITOR.config.uiColor = '#FFFFFF';
						CKEDITOR.replace('pageContent',
								{
									extraPlugins : 'onchange',
									width : "266",
									toolbar : [
											/* { name: 'clipboard',   items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },
											{ name: 'editing',     items : [ 'Find','Replace','-','SelectAll','-','SpellChecker', 'Scayt' ] },
											{ name: 'forms',       items : [ 'Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField' ] },
											'/',
											{ name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat' ] },
											{ name: 'paragraph',   items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote','CreateDiv','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl' ] },
											{ name: 'links',       items : [ 'Link','Unlink','Anchor' ] },
											{ name: 'insert',      items : [ 'Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak' ] },
											'/',
											{ name: 'styles',      items : [ 'Styles','Format','Font','FontSize' ] },
											{ name: 'colors',      items : [ 'TextColor','BGColor' ] },
											{ name: 'tools',       items : [ 'Maximize', 'ShowBlocks','-','About' ] } */
											{
												name : 'basicstyles',
												items : [ 'Bold', 'Italic',
														'Underline' ]
											},
											{
												name : 'paragraph',
												items : [ 'JustifyLeft',
														'JustifyCenter',
														'JustifyRight',
														'JustifyBlock' ]
											},
											'/',
											{
												name : 'styles',
												items : [ 'Styles', 'Format' ]
											},
											'/',
											{
												name : 'tools',
												items : [ 'Font', 'FontSize',
														'RemoveFormat' ]
											}, '/', {
												name : 'colors',
												items : [ 'BGColor' ]
											}, {
												name : 'paragraph',
												items : [ 'Outdent', 'Indent' ]
											}, {
												name : 'links',
												items : [ 'Link', 'Unlink' ]
											} ],
									removePlugins : 'resize'
								});

						var pageContent = $("#pageContent").val();

						if (pageContent == "") {
							//$("#contentPreview").text("Content goes here..");
							document.getElementById('contentPreview').innerHTML = "Content goes here..";

						} else {
							document.getElementById('contentPreview').innerHTML = pageContent
							//$("#contentPreview").text($("#pageContent").val());
						}

						/* if ($("#contentPreview").val() != '') {
						var stt = $("#contentPreview").val();
						var reNewLines = /[\n\r]/g;
						$("#contentPreview").text(stt.replace(reNewLines, "<br />"));
						}
						 */

					});
</script>

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
				<li class="last">Setup About Us</li>
			</ul>
		</div>
		<span class="blue-brdr"></span>
		<div class="content" id="login">
			<div id="menu-pnl" class="split">
				<jsp:include page="leftNavigation.jsp"></jsp:include>

			</div>
			<div class="cont-pnl split" id="">
				<div class="cont-block rt-brdr">
					<div class="title-bar">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-aboutus">&nbsp;</span></li>
							<li>About Us</li>
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
							<form:hidden path="pageType" />
							<form:hidden path="hubCitiVersion" />
							<form:hidden path="viewName" value="setupaboutusscreen" />

							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="cmnTbl">
								<%-- <tr>
								<td><label class="mand">Small Logo</label></td>
								<td><form:errors cssStyle="color:red" path="logoImageName"></form:errors>
									<label><img id="loginScreenLogo" width="86" height="35"
										alt="upload" src="${sessionScope.smallLogo }"> </label> <span
									class="topPadding"><label for="trgrUpldSmallLogo">
											<input type="button" value="Upload" id="trgrUpldBtn"
											class="btn trgrUpldSmallLogo" title="Upload Image File"
											tabindex="4" > <form:hidden path="smallLogoImageName"
												 id="strBannerAdImagePath" /> <span
											class="instTxt nonBlk"></span> <form:input type="file"
												class="textboxBig" id="trgrUpldSmallLogo"
												path="smallLogoImage" />
									</label> </span></td>
							</tr> --%>
								<tr>
									<td><label class="mand">Image</label></td>
									<td><form:errors cssClass="errorDsply"
											path="logoImageName"></form:errors> <label><img
											id="loginScreenLogo" width="80" height="40" alt="upload"
											src="${sessionScope.aboutusScreenImage }"> </label> <span
										class="topPadding cmnRow"><label for="trgrUpld">
												<input type="button" value="Upload" id="trgrUpldBtn"
												class="btn trgrUpld" title="Upload Image File" tabindex="1">
												<form:hidden path="logoImageName" id="logoImageName" /> <span
												class="instTxt nonBlk"></span> <form:input type="file"
													class="textboxBig" id="trgrUpld" path="logoImage" />
										</label> </span><label id="maxSizeImageError" class="errorDsply"></td>
								</tr>
								<tr>
									<td valign="top"><label class="mand">Content</label></td>
									<td><form:errors cssClass="errorDsply" path="pageContent"></form:errors>
										<form:textarea class="textareaTxt" path="pageContent"
											id="pageContent" tabindex="2"></form:textarea></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td><input type="button" name="button" id="button"
										title="${sessionScope.btnname}"
										onclick="saveAboutUsScreenSettings();" tabindex="3"
										value="${sessionScope.btnname}" class="btn-blue" /></td>
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
							<div id="iphone-aboutus-preview">
								<div class="iphone-status-bar"></div>
								<div class="navBar iphone">
									<table width="100%" border="0" cellspacing="0" cellpadding="0"
										class="titleGrd">
										<tr>
											<td width="19%"><img src="images/backBtn.png" alt="back"
												width="50" height="30" /></td>
											<td width="54%" class="genTitle"><img
												src="${sessionScope.smallLogoPreview }" width="86"
												height="35" alt="Logo"
												onerror="this.src='images/small-logo.png';" /></td>
											<td width="27%" align="center"><b>About</b></td>
										</tr>
									</table>
								</div>
								<div class="previewAreaScroll zeroBg">
									<div class="iPhone-preview-container">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td align="center"><div class="min-margin-btm-top">
														<img src="${sessionScope.aboutusScreenImagePreview }"
															width="220" height="50" alt="Image" />
													</div></td>
											</tr>
											<tr>
												<td align="center"><span class="font-bold">${sessionScope.hubCitiAppVersion
														}
												</span></td>
											</tr>
											<tr>
												<td><div id="contentPreview" class="wraptxt"
														class="min-margin-btm-top">Version Details goes
														here...</div></td>
											</tr>
										</table>
									</div>
									<div class="powered-by-logo">
										Powered by<span class="powered-logo"></span>
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
	configureMenu("setupaboutus");
</script>
