<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<script type="text/javascript" src="scripts/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/jquery.form.js"></script>

<script type="text/javascript">
	$(document)
			.ready(
					function() {

						var options = document.screenSettingsForm.userSettingsFields.value;
						var myArray = options.split(',');

						for ( var i = 0; i < myArray.length; i++) {
							arrayId = myArray[i];
							$('#userSettingTable tr.optnl')
									.each(
											function() {
												var id = $(this).find('input')
														.attr('class');
												if (id == arrayId
														&& arrayId != "optn-dntn") {
													$(this).find('input').attr(
															'checked', true);
															frmOptn = arrayId,
															lstRow = $("#dynFormTbl > tbody tr:last"),
															cellLbl = $(this)
																	.find(
																			'td:eq(1)')
																	.html(),
															cellData = $(this)
																	.find(
																			'td:eq(2)')
																	.html();
													var newRow = $("<tr id='"+frmOptn+"'><td>"
															+ cellLbl
															+ "</td><td>"
															+ cellData
															+ "</td></tr>");
													$(newRow).insertAfter(
															lstRow);
												}
												if (id == arrayId
														&& arrayId == "optn-dntn") {
													$(this).find('input').attr(
															'checked', true);
													var imgSrc = $(this).find(
															'input').attr(
															"imgSrc");
													if (imgSrc) {
														$(".usrImg")
																.html(
																		"<img src='"+imgSrc+"'/>");
														$(this)
																.find("div")
																.css("display",
																		"block");
													} else {
														$(".usrImg").html("");
														$(this)
																.find("div")
																.css("display",
																		"block");
													}
												}
											});
						}

						$('#trgrUpldImg')
								.bind(
										'change',
										function() {

											var selectedopt = ',';
											$('#dynFormTbl tr').each(
													function() {
														selectedopt += $(this)
																.attr('id')
																+ ",";
													});
											selectedopt += "optn-dntn";
											document.screenSettingsForm.userSettingsFields.value = selectedopt;

											var imageType = document
													.getElementById("trgrUpldImg").value;
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
																					.getElementsByTagName('imageScr')[0].firstChild.nodeValue;
																			var substr = imgRes
																					.split('|');
																			if (substr[0] == 'maxSizeImageError') {
																				$(
																						'#maxSizeImageError')
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

						$("input[name='dynForm']")
								.on(
										'change.showOptns',
										function() {
											var curChkd = $(this).prop(
													"checked"), frmOptn = $(
													this).attr("class"), lstRow = $("#dynFormTbl > tbody tr:last"), prntRow = $(
													this).parents('tr.optnl'), cellLbl = $(
													prntRow).find('td:eq(1)')
													.html(), cellData = $(
													prntRow).find('td:eq(2)')
													.html();
											var newRow = $("<tr id='"+frmOptn+"'><td>"
													+ cellLbl
													+ "</td><td>"
													+ cellData + "</td></tr>");
											if (curChkd)
												$(newRow).insertAfter(lstRow);
											else
												$('#' + frmOptn).remove();
										});

						$("input[name='dynFormImg']")
								.on(
										'change',
										function() {
											var imgSrc = $(this).attr("imgSrc");
											var curChkd = $(this).prop(
													"checked");
											if ($(this).attr("class") == "optn-dntn"
													&& curChkd && imgSrc) {
												$(".usrImg")
														.html(
																"<img src='"+imgSrc+"'/>");
												$(this).parents('tr.optnl')
														.find("div").css(
																"display",
																"block");
											} else if ($(this).attr("class") == "optn-dntn"
													&& curChkd && !imgSrc) {
												$(".usrImg").html("");
												$(this).parents('tr.optnl')
														.find("div").css(
																"display",
																"block");
											} else {
												$(".usrImg").html("");
												$(this).parents('tr.optnl')
														.find("div.cntrl-grp1")
														.css("display", "none");
												document
														.getElementById('logoImageName.errors').style.display = 'none';
											}
										});
					});
</script>
<script type="text/javascript">
	function saveUserSettings() {
		var selectedopt = ',';
		$('#dynFormTbl tr').each(function() {
			selectedopt += $(this).attr('id') + ",";
		});
		$('#userSettingTable tr.optnl input:checked').each(function() {
			var id = $(this).attr('class');
			if (id == "optn-dntn") {
				selectedopt += "optn-dntn";
			}
		});

		document.screenSettingsForm.userSettingsFields.value = selectedopt;
		document.screenSettingsForm.action = "saveusersettings.htm";
		document.screenSettingsForm.method = "POST";
		document.screenSettingsForm.submit();

	}
</script>

<div id="wrpr">
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
				<li class="last">Setup User Settings</li>
			</ul>
		</div>
		<!--Breadcrum div starts-->
		<span class="blue-brdr"></span>
		<!--Content div starts-->
		<div class="content" id="">
			<!--Left Menu div starts-->
			<div id="menu-pnl" class="split">
				<jsp:include page="leftNavigation.jsp"></jsp:include>
			</div>
			<!--Left Menu div ends-->
			<!--Content panel div starts-->
			<div class="cont-pnl split" id="equalHt">
				<div class="cont-block rt-brdr">
					<div class="title-bar">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-usrstng">&nbsp;</span>
							</li>
							<li>User Settings</li>
						</ul>
					</div>
					<div class="cont-wrp">
						<form:form name="screenSettingsForm" id="screenSettingsForm" commandName="screenSettingsForm" enctype="multipart/form-data"	action="uploadimg.htm">
						<form:hidden path="userSettingsFields"/>
						<form:hidden path="pageView" value="${sessionScope.pageView}"/>
						<form:hidden path="viewName" value="userSettings" />
						<form:hidden path="oldImageName"/>
						<c:if test="${requestScope.responseStatus ne null && !empty requestScope.responseStatus}">
							<c:choose>
								<c:when test="${requestScope.responseStatus eq 'INFO' }">
									<div class="alertBx warning zeroMrgn cntrAlgn">
										<span class="actn-close" title="close"></span>
										<p class="msgBx">
											<c:out value="${requestScope.responeMsg}" />
										</p>
									</div>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${requestScope.responseStatus eq 'SUCCESS' }">
											<div class="alertBx success zeroMrgn cntrAlgn">
												<span class="actn-close" title="close"></span>
												<p class="msgBx">
													<c:out value="${requestScope.responeMsg}" />
												</p>
											</div>
										</c:when>
										<c:otherwise>
											<div class="alertBx failure zeroMrgn cntrAlgn">
												<span class="actn-close" title="close"></span>
												<p class="msgBx">
													<c:out value="${requestScope.responeMsg}" />
												</p>
											</div>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</c:if>
						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="cmnTbl formLbl" id="userSettingTable">
							<tr>
								<td colspan="3"><b>Mandatory fields for User Settings</b>
								</td>
							</tr>
							<tr>
								<td>[ <span>*</span> ]</td>
								<td>First Name</td>
								<td width="65%"><div class="cntrl-grp">
										<input type="text" class="inputTxtBig" disabled="disabled"/>
									</div>
								</td>
							</tr>
							<tr>
								<td>[ <span>*</span> ]</td>
								<td>Last Name</td>
								<td><div class="cntrl-grp">
										<input type="text" class="inputTxtBig" disabled="disabled"/>
									</div>
								</td>
							</tr>
							<tr>
								<td>[ <span>*</span> ]</td>
								<td>Email</td>
								<td><div class="cntrl-grp">
										<input type="text" class="inputTxtBig" disabled="disabled"/>
									</div>
								</td>
							</tr>
							<tr>
								<td>[ <span>*</span> ]</td>
								<td>Zip Code</td>
								<td><div class="cntrl-grp">
										<input type="text" class="inputTxtBig" disabled="disabled"/>
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="3"><b>Optional fields can be seleced from the below list</b>
								</td>
							</tr>
							<tr class="optnl">
								<td width="8%"><input type="checkbox" name="dynForm" class="optn-mobile" /></td>
								<td width="27%">Mobile #</td>
								<td><div class="cntrl-grp">
										<input type="text" class="inputTxtBig" disabled="disabled"/>
									</div>
								</td>
							</tr>
							<tr class="optnl">
								<td><input type="checkbox" name="dynForm" class="optn-gender" />
								</td>
								<td>Gender</td>
								<td width="65%" class="actn-cntrl">
								<span class=""> 
								<input type="radio" class="" value="radio" id="gndrMale" name="setGndr" disabled="disabled"> 
								<label for="gndrMale">Male</label> 
								</span> 
								<span class="mrgn-left"> 
								<input type="radio" value="radio" id="gndrFemale" name="setGndr" disabled="disabled"> 
								<label for="gndrFemale">Female</label> </span>
								</td>
							</tr>
							<tr class="optnl">
								<td><input type="checkbox" name="dynForm" class="optn-education" />
								</td>
								<td>Education</td>
								<td><div class="cntrl-grp zeroBrdr">
										<select id="eductn" class="slctBx" disabled="disabled">
											<option selected="selected">Select</option>
										</select>
									</div>
								</td>
							</tr>
							<tr class="optnl">
								<td><input type="checkbox" name="dynForm" class="optn-mrtlSts" />
								</td>
								<td>Marital Status</td>
								<td><div class="cntrl-grp zeroBrdr">
										<select id="mrtlSts" class="slctBx" disabled="disabled">
											<option selected="selected">Select</option>
										</select>
									</div>
								</td>
							</tr>
							<tr class="optnl">
								<td valign="top">
									<c:choose>
										<c:when test="${sessionScope.userSettingImg ne null && sessionScope.userSettingImg ne 'images/upload_image.png'}">											
											<input type="checkbox" name="dynFormImg" class="optn-dntn" imgSrc="${sessionScope.userSettingImg}" />
										</c:when>
										<c:otherwise>											
											<input type="checkbox" name="dynFormImg" class="optn-dntn"/>
										</c:otherwise>
									</c:choose>
								</td>
								<td valign="top">Image</td>								
								<td>
									<form:errors path="logoImageName" cssClass="errorDsply"></form:errors>
									<div class="cntrl-grp1">
										<label>
											<img id="loginScreenLogo" width="80" height="40" alt="upload" src="${sessionScope.userSettingImg }"> 
										</label> 
										<span class="topPadding cmnRow">
											<label for="trgrUpldImg">
												<input type="button" value="Upload" id="trgrUpldBtnImg" class="btn trgrUpldImg" title="Upload Image File" tabindex="1">
												<form:hidden path="logoImageName" id="strBannerAdImagePath" /> 
												<span class="instTxt nonBlk"></span>
												<form:input type="file" class="textboxBig" id="trgrUpldImg" path="logoImage"/>
											</label> 
										</span><label id="maxSizeImageError" class="errorDsply"></label>
									</div>
								</td>
							</tr>
							<tr class="optnl">
								<td><input type="checkbox" name="dynForm" class="optn-income" />
								</td>
								<td>Income Range</td>
								<td><div class="cntrl-grp zeroBrdr">
										<select id="income" class="slctBx" disabled="disabled">
											<option selected="selected">Select</option>
										</select>
									</div>
								</td>
							</tr>
							<tr class="optnl">
								<td><input type="checkbox" name="dynForm" class="optn-dob" /></td>
								<td>Date Of Birth</td>
								<td><div class="cntrl-grp">
										<input type="text" class="inputTxtBig" disabled="disabled"/>
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="2">&nbsp;</td>
								<td><input type="button" title="${sessionScope.btnname}" value="${sessionScope.btnname}" class="btn-blue" onclick="saveUserSettings();"/>
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
											<td width="19%"><img src="images/backBtn.png" alt="back"
												width="50" height="30" /></td>
											<td width="54%" class="genTitle">Tell us about you</td>
											<td width="27%" align="center"><img
												src="images/mainMenuBtn.png" width="78" height="30"
												alt="mainmenu" /></td>
										</tr>
									</table>
								</div>
								<div class="previewAreaScroll gnrlScrn zeroBg">
									<div class="iPhone-preview-container">
										<table width="100%" border="0" cellspacing="0" cellpadding="0"
											class="mobTbl mrgnTop" id="dynFormTbl">
											<tr id="First_Name">
												<td width="35%" align="left">First Name</td>
												<td width="65%" align="center"><div class="cntrl-grp">
														<input type="text" class="inputTxtBig" disabled="disabled" />
													</div></td>
											</tr>
											<tr id="Last_Name">
												<td align="left">Last Name</td>
												<td align="center"><div class="cntrl-grp">
														<input type="text" class="inputTxtBig" disabled="disabled" />
													</div></td>
											</tr>
											<tr id="Email">
												<td valign="top" align="left">Email</td>
												<td align="right">
													<div class="cntrl-grp">
														<input type="text" disabled="disabled" class="inputTxtBig"
															style="height: 22px;">
													</div> <span class="prmry-link">Change Password</span>
												</td>
											</tr>
											<tr id="Zip_Code">
												<td align="left">Zip Code</td>
												<td align="center"><div class="cntrl-grp">
														<input type="text" class="inputTxtBig" disabled="disabled" />
													</div></td>
											</tr>
										</table>
									</div>
									<div class="usrImg mrgnTop">
										<!--<img src="images/imgDntn.png" alt="dontaion">-->
									</div>
								</div>
							</div>
							<ul class="tabbar" id="tab-main">
								<table width="100%" height="51" border="0" cellpadding="0"
									cellspacing="0">
									<tr>
										<td width="33%" align="right"><img
											src="images/privacyplcyBtn_down.png" width="100" height="30"
											alt="privacypolicy" /></td>
										<td width="47%">&nbsp;</td>
										<td width="20%"><img src="images/saveBtn_down.png"
											width="58" height="30" alt="save" /></td>
									</tr>
								</table>
							</ul>
							<!--Iphone Preview For Login screen ends here-->
						</div>
					</div>
				</div>
			</div>
			<!--Content panel div ends-->
		</div>
		<!--Content div ends-->
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
	configureMenu("setupusersettings");
</script>
