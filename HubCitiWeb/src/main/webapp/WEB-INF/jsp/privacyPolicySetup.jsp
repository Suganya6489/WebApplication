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
						var pageTitle = $("#pageTitle").val();
						if (pageContent == "") {
							//$("#contentPreview").text("Content goes here..");
							document.getElementById('contentPreview').innerHTML = "Content goes here..";

						} else {
							document.getElementById('contentPreview').innerHTML = pageContent
							//$("#contentPreview").text($("#pageContent").val());
						}

						if (pageTitle == "") {
							$("#titlePreview").text("Title goes here..");

						} else {
							$("#titlePreview").text(pageTitle);
						}

						/* if ($("#contentPreview").val() != '') {
						var stt = $("#contentPreview").val();
						var reNewLines = /[\n\r]/g;
						$("#contentPreview").text(stt.replace(reNewLines, "<br />"));
						}
						 */

						var bgColor = $('#bgColor').val();
						var fontColor = $('#fontColor').val();

						//alert(bgColor)
						$(".bgColorSpectrum").spectrum("set", bgColor);
						$(".fontColorSpectrum").spectrum("set", fontColor);

						$("#pageTitle").keyup(function() {
							$("#titlePreview").text($("#pageTitle").val());

						});

						$("#pageTitle").change(function() {
							$("#titlePreview").text($("#pageTitle").val());

						});

					});
</script>
<div class="wrpr-cont relative">
	<div id="slideBtn">
		<a href="#" onclick="revealPanel(this);" title="Hide Menu"><img
			src="images/slide_off.png" width="11" height="28" alt="btn_off" /></a>
	</div>
	<div id="bread-crumb">
		<ul>
			<li class="scrn-icon"><span class="icon-home">&nbsp;</span></li>
			<li><a href="welcome.htm">Home</a></li>
			<li class="last">Setup Privacy Policy</li>
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
						<li class="title-icon"><span class="icon-privacy-policy">&nbsp;</span></li>
						<li>Privacy Policy</li>
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
						commandName="screenSettingsForm">
						<table width="100%" border="0" cellpadding="0" cellspacing="0"
							class="cmnTbl">
							<tr>
								<td valign="top"><span class="pick-color-label"><label
										class="mand">Background Color</label></span></td>
								<td><form:errors cssClass="errorDsply" path="bgColor"></form:errors>
									<input class="bgColorSpectrum" id="bgColorSpectrumClr"
									value='#d9d9d9' tabindex="1" /> <form:hidden path="bgColor"
										class="inputTxt" id="bgColor" name="bgColor" /></td>
							</tr>
							<tr>
								<td valign="top"><span class="pick-color-label"><label
										class="mand">Font Color</label></span></td>
								<td><form:errors cssClass="errorDsply" path="fontColor"></form:errors>
									<input class="fontColorSpectrum" tabindex="2"/> <form:hidden
										path="fontColor" class="inputTxt" id="fontColor"
										name="fontColor" /></td>
							</tr>
							<tr>
								<td><label class="mand">Title</label></td>
								<td><form:errors cssClass="errorDsply" path="pageTitle"></form:errors>
									<div class="cntrl-grp">
										<form:input path="pageTitle" type="text" class="inputTxtBig" tabindex="3" />
									</div></td>
							</tr>
							<tr>
								<td valign="top"><label class="mand">Content</label></td>
								<td><form:errors cssClass="errorDsply" path="pageContent"></form:errors>
									
										<form:textarea path="pageContent" class="textareaTxt cstmFix" tabindex="4"></form:textarea>
									</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><input type="button" name="button" id="button"
									onclick="savePrivacyPolicyScreenSettings()"
									value="${sessionScope.btnname}" title="${sessionScope.btnname}" class="btn-blue" tabindex="5" /></td>
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
						<div id="iphone-priPolicy-preview">
							<div class="iphone-status-bar"></div>
							<div class="navBar iphone">
								<table width="100%" border="0" cellspacing="0" cellpadding="0"
									class="titleGrd">
									<tr>
										<td width="19%"><img src="images/backBtn.png" alt="back"
											width="50" height="30" /></td>
										<td width="54%" class="genTitle"><img
											src="${sessionScope.titleBarLogoPreview}" width="86" height="35"
											alt="Logo" onerror="this.src='images/small-logo.png';"/></td>
										<td width="27%" align="center">Policy</td>
									</tr>
								</table>
							</div>
							<div class="previewAreaScroll">
								<div class="iPhone-preview-container">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td align="center"><h1 id="titlePreview">ScanSee
													Privacy policy</h1></td>
										</tr>
										<tr>
											<td class="star-font" align="center">*********************************************</td>
										</tr>
										<tr>
											<td>
												<div id="contentPreview" class="min-margin-btm-top">Content
													goes here...</div>
											</td>
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
<script type="text/javascript">
	function loadSetupScreen() {
		var bgColor = $('#bgColor').val();
		var fontColor = $('#fontColor').val();

		if (bgColor == "") {
			$('#bgColor').val("#d9d9d9");
		}

		if (fontColor == "") {
			$('#fontColor').val("#000000");
		}

		$("#iphone-priPolicy-preview").css("background-color", bgColor);
		$("#iphone-priPolicy-preview").css("color", fontColor);
		$("#iphone-priPolicy-preview h1").css("color", fontColor);
	}

	loadSetupScreen();
</script>

<script type="text/javascript">
	configureMenu("setupprivacypolicy");
</script>
