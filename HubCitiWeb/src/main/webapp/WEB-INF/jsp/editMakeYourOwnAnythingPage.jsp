<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<script type="text/javascript" src="scripts/jquery-1.10.2.js"></script>
<script type="text/javascript" src="scripts/jquery-ui.js"></script>
<script type="text/javascript" src="scripts/jquery-ui-min.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/jquery.form.js"></script>
<script src="/HubCiti/scripts/ckeditor/ckeditor.js"></script>
<script src="/HubCiti/scripts/ckeditor/config.js"></script>

<link href="/ScanSeeWeb/styles/jquery-ui.css" rel="stylesheet"
	type="text/css">

<script type="text/javascript">
	$(document)
			.ready(
					function() {

						/*var stHt = $(".cont-pnl").height();
						$("#menu-pnl").height(stHt);*/
						$(window).load(
								function() {
									$("#menu-pnl").height(
											$(".cont-pnl").height() + 30);
								});

						CKEDITOR
								.on(
										'instanceCreated',
										function(e) {
											//	alert("q : "+e.editor.name);
											var editorName = e.editor.name;
											$("#shortPreview")
													.text(
															"Short Description goes here....");
											$("#longPreview")
													.text(
															"Long Description goes here....");

											e.editor
													.on(
															'change',
															function(ev) {
																if (editorName == 'longDescription') {
																	document
																			.getElementById('longPreview').innerHTML = ev.editor
																			.getData();
																} else if (editorName == 'shortDescription') {
																	document
																			.getElementById('shortPreview').innerHTML = ev.editor
																			.getData();
																}
															});

										});

						CKEDITOR.config.uiColor = '#FFFFFF';
						CKEDITOR.replace('shortDescription',
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

						CKEDITOR.config.uiColor = '#FFFFFF';
						CKEDITOR.replace('longDescription',
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

						var pageTitle = $("#pageTitle").val();
						if (pageTitle == "") {
							$("#titlePreview").text("Title goes here....");

						} else {
							$("#titlePreview").text(pageTitle);
						}

						$("#pageTitle").keyup(function() {
							$("#titlePreview").text($("#pageTitle").val());

						});

						$("#pageTitle").change(function() {
							$("#titlePreview").text($("#pageTitle").val());

						});

						var shortDesc = $("#shortDescription").val();
						if (shortDesc == "") {
							//$("#contentPreview").text("Content goes here..");
							document.getElementById('shortPreview').innerHTML = "Short Description goes here....";

						} else {
							document.getElementById('shortPreview').innerHTML = shortDesc;
							//$("#contentPreview").text($("#pageContent").val());
						}

						var longDesc = $("#longDescription").val();
						if (longDesc == "") {
							//$("#contentPreview").text("Content goes here..");
							document.getElementById('longPreview').innerHTML = "Long Description goes here....";

						} else {
							document.getElementById('longPreview').innerHTML = longDesc;
							//$("#contentPreview").text($("#pageContent").val());
						}

						$("#startDate").datepicker({
							showOn : 'both',
							buttonImageOnly : true,
							buttonText : 'Click to show the calendar',
							buttonImage : 'images/icon-calendar-active.png'
						});
						$('.ui-datepicker-trigger').css("padding-left", "5px");

						$("#endDate").datepicker({
							showOn : 'both',
							buttonImageOnly : true,
							buttonText : 'Click to show the calendar',
							buttonImage : 'images/icon-calendar-active.png'
						});
						$('.ui-datepicker-trigger').css("padding-left", "5px");

					});

	/*function checkBannerSize(input) {
		document.screenSettingsForm.lowerLimit.value = '${requestScope.lowerLimit}';
		var bannerImage = document.getElementById("trgrUpld").value;
		if (bannerImage != '') {
			var checkbannerimg = bannerImage.toLowerCase();
			if (!checkbannerimg.match(/(\.png|\.jpeg|\.jpg|\.gif|\.bmp)$/)) {
				alert("You must upload Banner image with following extensions : .png, .gif, .bmp, .jpg, .jpeg");
				document.screenSettingsForm.logoImage.value = "";
				document.getElementById("trgrUpld").focus();
				return false;
			}
		}
	}*/

	function updateAnythingPageScreen() {
		document.screenSettingsForm.hiddenBtnLinkId.value = document.screenSettingsForm.hiddenBtnLinkId.value;
		document.screenSettingsForm.lowerLimit.value = '${requestScope.lowerLimit}';
		document.screenSettingsForm.action = "updateyourownanythingpage.htm";
		document.screenSettingsForm.method = "POST";
		document.screenSettingsForm.submit();
	}
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
				<li><a href="displayanythingpages.htm">AnyThing Page<sup>TM</sup>
				</a></li>
				<li class="last">Create AnyThing Page<sup>TM</sup></li>
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
							<li class="title-icon"><span class="icon-aboutus">&nbsp;</span>
							</li>
							<li>Edit AnyThing Page<sup>TM</sup></li>
						</ul>
					</div>
					<div class="cont-wrphidoverflow">
						<form:form name="screenSettingsForm" id="screenSettingsForm"
							commandName="screenSettingsForm" enctype="multipart/form-data"
							action="uploadimg.htm">
							<form:hidden path="viewName" value="edityourownanythingpage" />
							<form:hidden path="hiddenBtnLinkId"
								value="${sessionScope.anythingPageId}" />
							<form:hidden path="oldImageName"
								value="${sessionScope.anythingPage.imageName}" />
							<form:hidden path="lowerLimit" />
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="cmnTbl">
								<tr>
									<td width="40%"><label class="mand">Page Title</label></td>
									<td width="60%"><form:errors cssClass="errorDsply"
											path="pageTitle" />
										<div class="cntrl-grp">
											<form:input path="pageTitle" type="text" id="pageTitle"
												cssClass="inputTxtBig" maxlength="25" />
										</div></td>
								</tr>
								<tr>
									<td><label class="mand">Upload Image</label></td>
									<td><form:errors cssClass="errorDsply"
											path="logoImageName"></form:errors> <label> <img
											id="uploadImg" width="70" height="70" alt="upload"
											src="${sessionScope.makeAnythngImage}"> <form:hidden
												path="imageName" id="imageName"
												value="${sessionScope.anythingPage.imageName }" />
									</label> <span class="topPadding cmnRow"> <label for="trgrUpld">
												<input type="button" value="Upload" id="trgrUpldBtn"
												class="btn trgrUpld" title="Upload Image File"> <form:hidden
													path="logoImageName" id="logoImageName" /> <span
												class="instTxt nonBlk"></span> <form:input type="file"
													class="textboxBig" id="trgrUpld" path="logoImage"
													onchange="checkBannerSize(this);" />
										</label>
									</span><label id="maxSizeImageError"
										class="errorDsply"></label></td>
								</tr>
								<tr>
									<td><label class="mand">Short Description</label></td>
									<td><form:errors path="shortDescription"
											cssStyle="color:red">
										</form:errors> <form:textarea id="shortDescription" path="shortDescription"
											cssClass="textareaTxt cstmFix" /></td>
								</tr>
								<tr>
									<td><label class="mand">Long Description</label></td>
									<td><form:errors path="longDescription"
											cssStyle="color:red">
										</form:errors> <form:textarea id="longDescription" path="longDescription"
											cssClass="textareaTxt cstmFix" /></td>
								</tr>
								<tr>
									<td>Start Date</td>
									<td><form:errors path="startDate" cssStyle="color:red"></form:errors>
										<div class="cntrl-grp cntrl-dt floatL">
											<form:input path="startDate" id="startDate"
												cssClass="inputTxt" maxlength="10" />
										</div></td>
								</tr>
								<tr>
									<td>End Date</td>
									<td><form:errors path="endDate" cssStyle="color:red"></form:errors>
										<div class="cntrl-grp cntrl-dt floatL">
											<form:input path="endDate" id="endDate"
												cssClass="inputTxt" maxlength="10" />
										</div></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td><input type="button"
										onclick="updateAnythingPageScreen();" class="btn-blue"
										title="Update AnyThing Page" value="Update" id="button"
										name="button"></td>
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
							<div id="iphone-priPolicy-preview">
								<div class="iphone-status-bar"></div>
								<div class="navBar iphone">
									<table width="100%" border="0" cellspacing="0" cellpadding="0"
										class="titleGrd">
										<tr>
											<td width="19%"><img src="images/backBtn.png" alt="back"
												width="50" height="30" /></td>
											<td width="54%" class="genTitle"></td>
											<td width="27%" align="center"></td>
										</tr>
									</table>
								</div>
								<div class="previewAreaScroll enbl-scrl">
									<div class="iPhone-preview-container">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td align="center">
													<h1 id="titlePreview">Title goes here..</h1>
												</td>
											</tr>
											<tr>
												<td class="star-font" align="center"><img
													src="${sessionScope.makeAnythngImagePreview}" width="50"
													height="50" alt="Image" /></td>
											</tr>
											<tr>
												<td>
													<div id="shortPreview" class="min-margin-btm-top">
														Short Description goes here....</div>
												</td>
											</tr>
											<tr>
												<td>
													<div id="longPreview" class="min-margin-btm-top">
														Long Description goes here....</div>
												</td>
											</tr>
										</table>
									</div>
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
		<iframe frameborder="0" scrolling="no" id="ifrm" src=""
			height="100%" allowtransparency="yes" width="100%"
			style="background-color: White"> </iframe>
	</div>
</div>
<script type="text/javascript">
	configureMenu("setupanythingpage");
</script>