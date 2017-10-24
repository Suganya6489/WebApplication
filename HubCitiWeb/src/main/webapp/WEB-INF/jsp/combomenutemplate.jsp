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
<link rel="stylesheet" href="/HubCiti/styles/jquery-ui.css" />
<script src="/HubCiti/scripts/jquery-ui.js"></script>

<script src="/HubCiti/scripts/global.js"></script>
<script>
	$(function() {
		$(".sortable")
				.sortable(
						{
							'opacity' : 0.6,
							helper : 'clone',
							cancel : '.disTab',
							items : "li:not(.grpHdr)",
							containment : ".gnrlScrn",

							update : function(event, ui) {
								var grpClass = ui.item.find("a").attr(
										"hiddenbtnGroup");
								var grpName = ui.item.find("a").attr("grpname");

								if ($("li.tabs." + grpClass).length == 1) {
									var pageTitle = $(
											"input[name='pageTitle']:checked")
											.val();
									if (pageTitle == "Text") {
										pageTitle = "Group";
									} else {
										pageTitle = "Title";
									}
									var confirmMsg = "Do you want to delete this "
											+ pageTitle + " and move button";

									if (!confirm(confirmMsg)) {
										return false;
									} else {
										$("li[iconid='Text-" + grpName + "']")
												.remove();
									}
								}

							},
							stop : function(event, ui) {

								var curObj = ui.item;
								var tempObj = ui.item;
								var oldBtnType = curObj.find("a").attr(
										"grpbtntype");
								var oldgrpName = curObj.find("a").attr(
										"hiddenbtnGroup");
								var oldgrp = curObj.find("a").attr("grpname");
								var chkCur = curObj.prev("li");
								if ($(chkCur).hasClass("grpHdr")) {
									chkCur = tempObj.next("li");
								}
								var slctType = $(chkCur).find("a").attr(
										"grpbtntype");
								var btnTypeId = $(chkCur).find("a").attr(
										"grpbtntypeId");
								var grpName = $(chkCur).find("a").attr(
										"grpname");
								var hiddenbtngroup = $(chkCur).find("a").attr(
										"hiddenbtngroup");

								if (grpName != ''
										&& typeof (grpName) !== 'undefined'
										&& slctType != oldBtnType) {
									curObj.removeClass(oldgrpName).addClass(
											hiddenbtngroup);
									curObj.removeClass(oldBtnType).addClass(
											slctType);
									var nextli = curObj.find("a");
									nextli.attr("grpbtntype", slctType);
									nextli.attr("grpbtntypeId", btnTypeId);
									nextli.attr("grpname", grpName);
									nextli.attr("hiddenbtngroup",
											hiddenbtngroup);
									$(nextli).removeClass(oldBtnType).addClass(
											slctType);
									if (oldBtnType === "Square"
											|| oldBtnType === "Circle") {
										$(nextli).removeClass("scrlOn");
										$(nextli).removeClass("scrlOff");
									}
									nextli = curObj.find("div");

									if (slctType === "Circle") {
										$(nextli).addClass("rnd");
										nextli = curObj.find("img");
										nextli.attr("height", 60);
										nextli.attr("width", 60);
										$(".comboView li.tabs.Circle")
												.removeAttr("style");

										var menuLevel = document.screenSettingsForm.menuLevel.value;
										if (menuLevel == 1) {
											var mainMenuBtnClr = '${sessionScope.mainMenuIconsFntClr}';
											$(".comboView li.tabs.Circle a")
													.css("color",
															mainMenuBtnClr);
										} else {
											var subMenuBtnClr = '${sessionScope.subMenuIconsFntClr}';
											$(".comboView li.tabs.Circle a")
													.css("color", subMenuBtnClr);
										}

										if ($('.gnrlScrn').hasScrollBar()) {
											$(".comboView li.tabs a.Circle")
													.removeClass("scrlOff")
													.addClass("scrlOn");
										} else {
											$(".comboView li.tabs a.Circle")
													.removeClass("scrlOn")
													.addClass("scrlOff");
										}

									} else if (slctType === "Square") {
										$(nextli).removeClass("rnd");
										nextli = curObj.find("img");
										nextli.attr("height", 60);
										nextli.attr("width", 60);
										$(".comboView li.tabs.Square").css(
												"background-color", "");

										if ($('.gnrlScrn').hasScrollBar()) {
											$(".comboView li.tabs a.Square")
													.removeClass("scrlOff")
													.addClass("scrlOn");
										} else {
											$(".comboView li.tabs a.Square")
													.removeClass("scrlOn")
													.addClass("scrlOff");
										}
									}
									if (oldBtnType === "Circle") {
										var menuLevel = document.screenSettingsForm.menuLevel.value;
										if (menuLevel == 1) {
											var mainMenuBtnClr = '${sessionScope.mainMenuBtnClr}';
											var mainMenuFntClr = '${sessionScope.mainMenuFntClr}';
											$(".comboView li.tabs.Rectangle a")
													.css("color",
															mainMenuFntClr);
											$(".comboView li.tabs.Square a")
													.css("color",
															mainMenuFntClr);
											$(".comboView li.tabs.Rectangle")
													.css("background-color",
															mainMenuBtnClr);
										} else {
											var subMenuBtnClr = '${sessionScope.subMenuBtnClr}';
											var subMenuFntClr = '${sessionScope.subMenuFntClr}';
											$(".comboView li.tabs.Rectangle a")
													.css("color", subMenuFntClr);
											$(".comboView li.tabs.Square a")
													.css("color", subMenuFntClr);
											$(".comboView li.tabs.Rectangle")
													.css("background-color",
															subMenuBtnClr);
										}
									}
									if (oldBtnType === "Square") {
										var menuLevel = document.screenSettingsForm.menuLevel.value;
										if (menuLevel == 1) {
											var mainMenuBtnClr = '${sessionScope.mainMenuBtnClr}';
											$(".comboView li.tabs.Rectangle")
													.css("background-color",
															mainMenuBtnClr);
										} else {
											var subMenuBtnClr = '${sessionScope.subMenuBtnClr}';
											$(".comboView li.tabs.Rectangle")
													.css("background-color",
															subMenuBtnClr);
										}
									}

									if (oldBtnType === "Rectangle") {
										if (slctType === "Square") {
											src = curObj.find("div")
													.find("img").attr("src");
											if (src != 'images/uploadIcon.png'
													&& src != "") {
												var image = "<img width='60' height='60' src=" + src +" class='lstImg' alt='image'>";
											} else {
												var image = "<img width='60' height='60' src='images/uploadIconSqr.png' class='lstImg' alt='image'>";
												if (slctType != oldBtnType) {
													alert('Please upload images for this button');
												}
											}
											nextli = curObj.find("div");
											nextli.empty();
											nextli.append(image);
										} else {
											src = curObj.find("div")
													.find("img").attr("src");
											if (src != ""
													&& src != 'images/uploadIconSqr.png') {
												var image = "<img width='60' height='60' src=" + src +" class='lstImg' alt='image'>";
											} else {
												var image = "<img width='60' height='60' src='images/uploadIcon.png' class='lstImg' alt='image'>";
												if (slctType != oldBtnType) {
													alert('Please upload images for this button');
												}
											}
											nextli = curObj.find("div");
											nextli.empty();
											nextli.append(image);
										}
									} else if (oldBtnType === "Square"
											&& slctType === "Circle") {
										src = curObj.find("div").find("img")
												.attr("src");
										if (src == 'images/uploadIconSqr.png') {
											var image = "<img width='60' height='60' src='images/uploadIcon.png' class='lstImg' alt='image'>";
											nextli = curObj.find("div");
											nextli.empty();
											nextli.append(image);
										}
									} else if (oldBtnType === "Circle"
											&& slctType === "Square") {
										src = curObj.find("div").find("img")
												.attr("src");
										if (src == 'images/uploadIcon.png') {
											var image = "<img width='60' height='60' src='images/uploadIconSqr.png' class='lstImg' alt='image'>";
											nextli = curObj.find("div");
											nextli.empty();
											nextli.append(image);
										}
									}

								} else if (grpName != ''
										&& typeof (grpName) !== 'undefined'
										&& oldgrp != grpName) {
									curObj.removeClass(oldgrpName).addClass(
											hiddenbtngroup);
									var nextli = curObj.find("a");
									nextli.attr("grpname", grpName);
									nextli.attr("hiddenbtngroup",
											hiddenbtngroup);
								}

								if ($('.gnrlScrn').hasScrollBar()) {
									$(".comboView li.tabs a.Square")
											.removeClass("scrlOff").addClass(
													"scrlOn");
									$(".comboView li.tabs a.Circle")
											.removeClass("scrlOff").addClass(
													"scrlOn");
								} else {
									$(".comboView li.tabs a.Square")
											.removeClass("scrlOn").addClass(
													"scrlOff");
									$(".comboView li.tabs a.Circle")
											.removeClass("scrlOn").addClass(
													"scrlOff");
								}
							}
						});
		$(".sortable").disableSelection();
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
				<li class="scrn-icon"><span class="icon-home">&nbsp;</span>
				</li>
				<li>Home</li>
				<li class=""><a href="#" id="slctTmpt">Change Template</a>
				</li>
				<li class="last" id="menutitle-bread-crumb">Main Menu[Combo
					Template]</li>
			</ul>
		</div>
		<span class="blue-brdr"></span>
		<div class="content" id="login">
			<div id="menu-pnl" class="split">
				<jsp:include page="leftNavigation.jsp"></jsp:include>
			</div>
			<div class="cont-pnl split">
				<div class="cont-block rt-brdr">
					<div class="title-bar" id="menutitleDiv">
						<ul class="title-actn">
							<c:choose>
								<c:when test="${sessionScope.menuName eq 'Setup Sub Menu' }">

									<li class="title-icon"><span class="icon-submenu">&nbsp;</span>
									</li>

								</c:when>
								<c:otherwise>
									<li class="title-icon"><span class="icon-main-menu">&nbsp;</span>
									</li>
								</c:otherwise>
							</c:choose>
							<li id="menutitle">Main Menu</li>
						</ul>
					</div>
					<form:form name="screenSettingsForm" id="screenSettingsForm"
						commandName="screenSettingsForm" enctype="multipart/form-data"
						action="uploadimg.htm">

						<div class="cont-wrp less-pdng" id="subMenuDetails">
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="cmnTbl" id="">

								<tr id="subMenuCont">
									<td width="36%"><label class="mand">SubMenu Name</label>
									</td>
									<td width="64%" colspan="2"><form:errors
											cssClass="errorDsply" path="subMenuName"></form:errors>
										<div class="cntrl-grp">

											<form:input path="subMenuName" type="text" id="subMenuInput"
												maxlength="35" class="inputTxtBig vldt"
												onkeypress="return specialCharCheck(event)" />
										</div> <span class="lbl">Chars Left:</span><span
										class="lblcnt subMenuInput"></span>
									</td>

								</tr>

							</table>
						</div>

						<div class="title-bar top-brdr" id="grpingContTitle">
							<ul class="title-actn">
								<li class="title-icon"><span class="icon-main-menu">&nbsp;</span>
								</li>
								<li id="menutitle">Sub Menu Filters</li>
							</ul>
						</div>

						<div class="cont-wrp less-pdng" id="grpingCont">
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="cmnTbl" id="grptabDetails">

								<tr id="grpInpDetail">

									<td class="multi-inputs" colspan="2"><label for="grpDept"><form:checkbox
												path="menuFilterType" id="grpDept" name="grpInput"
												onclick="tglGrping(this)" value="Department" /> Department</label>
										<label for="grpType"> <form:checkbox
												path="menuFilterType" id="grpType" name="grpInput"
												onclick="tglGrping(this)" value="Type" />Type </label>
									</td>
									<td width="32%" align="right" class="btnNm" id="edittglGrping"><input
										type="button" class="btn-grey" value="Edit"
										onclick="edittglGrping();" />
									</td>
								</tr>



							</table>
						</div>
						<div class="title-bar top-brdr">
							<ul class="title-actn">
								<li class="title-icon"><span class="icon-main-menu">&nbsp;</span>
								</li>
								<li id="menutitle">Button Details</li>
							</ul>
						</div>
						<div class="cont-wrp less-pdng" id="dynData">
							<input type="hidden" id="tmpltOptn" />

							<form:errors cssStyle="color:red"></form:errors>
							<form:hidden path="viewName" value="setupcombomenu" />
							<form:hidden path="addDeleteBtn" id="addDeleteBtn" />
							<form:hidden path="menuId" />
							<form:hidden path="menuLevel" />
							<form:hidden path="menuIconId" id="menuIconId" />
							<form:hidden path="hiddenmenuFnctn" />
							<form:hidden path="hiddenBtnLinkId" />
							<form:hidden path="grpList" />
							<form:hidden path="hiddenbtnGroup" />
							<form:hidden path="oldGroupName" />
							<form:hidden path="ismenuFilterTypeSelected" />
							<form:hidden path="editFilter" />
							<form:hidden path="hiddenFindCategory" />
							<form:hidden path="btnPosition" />
							<form:hidden path="uploadImageType" id="uploadImageType"
								value="logoImage" />
							<form:hidden path="comboBtnType" />
							<form:hidden path="grpBtnType" />
							<form:hidden path="grpBtnTypeId" />
							<form:hidden path="hiddenPageTitle" />
							<form:hidden path="subCatIds" id="subCatIds" />
							<form:hidden path="hiddenSubCate" />
							<form:hidden path="chkSubCate" id="chkSubCate" />
							<form:hidden path="userType" value="${sessionScope.loginUserType}"/>
							<form:hidden path="hiddenCitiId"/>
							<form:hidden path="hiddenFundEvtId" />

							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="cmnTbl" id="tabDetails">
								<tr class="grpDept">
									<td align="left" valign="center" width="36%"><label
										class="mand">Select Department</label>
									</td>
									<td colspan="2" width="64%"><form:errors
											cssClass="errorDsply" path="btnDept"></form:errors>
										<div class="cntrl-grp zeroBrdr">
											<span class="lesWdth"> <form:select path="btnDept"
													class="slctBx dynmOptn" id="slctDept">

													<form:option value="0">Select Department</form:option>
													<c:forEach items="${sessionScope.filterDeptList}"
														var="item">
														<form:option value="${item.deptName}">${item.deptName}</form:option>
													</c:forEach>


												</form:select> </span>

											<!--<input type="text" class="inputTxtBig" id="btnGroup" />-->
											<a href="#" onclick="tglGrp(this)" class="dept"><img
												src="images/btn_add.png" width="24" height="24" alt="add"
												class="addInfo" title="Add New Department" /> </a>
										</div>
									</td>
								</tr>
								<tr class="show-dept">
									<td width="36%" align="left" valign="top"><label
										class="mand">Department Name</label>
									</td>
									<td width="64%"><ul class="Department">
											<li class="mrgnBtm_small">
												<div class="cntrl-grp ">
													<input type="text" class="inputTxtBig" id="btnDept"
														maxlength="20" />
												</div> <span class="lbl">Chars Left:</span><span
												class="lblcnt btnDept"></span></li>
											<li><input type="button" value="Save" class="btn-blue"
												id="saveGrp" onclick="appendComboGrp(this);" /> <input
												type="button" value="Cancel" class="btn-red delGrp" id="" />
											</li>
										</ul>
									</td>
								</tr>
								<tr class="grpType">
									<td align="left" valign="center" width="36%"><label
										class="mand">Select Type</label>
									</td>
									<td colspan="2" width="64%"><form:errors
											cssClass="errorDsply" path="btnType"></form:errors>
										<div class="cntrl-grp zeroBrdr">
											<span class="lesWdth"> <form:select path="btnType"
													class="slctBx dynmOptn" id="slctType">
													<form:option value="0">Select Type</form:option>
													<c:forEach items="${sessionScope.filterTypeList}"
														var="item">
														<form:option value="${item.typeName}">${item.typeName}</form:option>
													</c:forEach>

												</form:select> </span>

											<!--<input type="text" class="inputTxtBig" id="btnGroup" />-->
											<a href="#" onclick="tglGrp(this)" class="type"><img
												src="images/btn_add.png" width="24" height="24" alt="add"
												class="addInfo" title="Add New Type" /> </a>
										</div>
									</td>
								</tr>
								<tr class="show-type">
									<td width="36%" align="left" valign="top"><label
										class="mand">Type Name</label>
									</td>
									<td width="64%"><ul class="Type">
											<li class="mrgnBtm_small">
												<div class="cntrl-grp ">
													<input type="text" class="inputTxtBig" id="btnType"
														maxlength="20" />
												</div> <span class="lbl">Chars Left:</span><span
												class="lblcnt btnType"></span></li>
											<li><input type="button" value="Save" class="btn-blue"
												id="saveGrp" onclick="appendComboGrp(this);" /> <input
												type="button" value="Cancel" class="btn-red delGrp" />
											</li>
										</ul>
									</td>
								</tr>
								<tr class="hdrType">
									<td align="left" valign="baseline"><label class="mand">Select
											Header</label>
									</td>
									<td colspan="2" class="multi-inputs"><span
										class="multi-inputs"> <form:radiobutton
												path="pageTitle" id="grp" value="Text" objTitle="Group" />
											<label>Group</label> <form:radiobutton path="pageTitle"
												id="ttl" value="Label" objTitle="Title" /> <label>Title</label>
									</span>
									</td>
								</tr>
								<tr class="btnGroup">
									<td align="left" valign="baseline" width="36%"><label
										class="mand">Select Group</label>
									</td>
									<td colspan="2" width="64%"><form:errors
											cssClass="errorDsply" path="btnGroup"></form:errors>
										<div class="cntrl-grp zeroBrdr">
											<span class="lesWdth"> <form:select path="btnGroup"
													class="slctBx dynmOptn" id="groupName">
													<form:option value="0">Select Group</form:option>
													<c:forEach items="${sessionScope.comboList}"
														var="groupName">
														<form:option value="${groupName}">${groupName}</form:option>
													</c:forEach>
												</form:select> </span>
											<!--<input type="text" class="inputTxtBig" id="btnGroup" />-->
											<a href="#" onclick="tglGrp(this)" class="group"><img
												src="images/btn_add.png" width="24" height="24" alt="add"
												class="addGrp" title="Add New Group" /> </a>
										</div>
									</td>
								</tr>
								<tr class="show-group">
									<td width="36%" align="left" valign="top"><label
										class="mand">Group Name</label>
									</td>
									<td width="64%"><ul class="Group" id="Group">
											<li class="mrgnBtm_small">
												<div class="cntrl-grp">
													<input type="text" class="inputTxtBig vldt" id="btnGroup"
														onkeypress="return specialCharCheck(event)" />
												</div> <span class="lbl">Chars Left:</span><span
												class="lblcnt btnGroupName"></span></li>
											<li><input type="button" value="Save" class="btn-blue"
												id="saveGrp" onclick="appendComboGrp(this);" /> <input
												type="button" value="Cancel" class="btn-red delGrp" />
											</li>
										</ul>
									</td>
								</tr>

								<tr>
									<td width="36%"><label class="mand">Button Name</label>
									</td>
									<td width="64%" colspan="2"><form:errors
											cssClass="errorDsply" path="menuBtnName"></form:errors>
										<div class="cntrl-grp">

											<form:input path="menuBtnName" type="text"
												class="inputTxtBig vldt" id="btnName" maxlength="24"
												onkeypress="return specialCharCheck(event)" />
										</div> <span class="lbl">Chars Left:</span><span
										class="lblcnt btnName"></span>
									</td>

								</tr>
								<tr id="btnShape">
									<td width="33%"><label class="mand">Button Type</label>
									</td>
									<td width="67%" colspan="2"><form:errors
											cssClass="errorDsply" path="comboBtnType"></form:errors>
										<div class="cntrl-grp">
											<form:select path="comboBtnTypeId"
												class="slctBx dynmBtns zeroBrdr" id="btn-type">
												<form:option value="" selected="selected">Select Type</form:option>
												<c:forEach items="${sessionScope.btnTypeList}" var="list">
													<form:option value="${list.comboBtnTypeId}">${list.comboBtnType}</form:option>
												</c:forEach>
											</form:select>
										</div>
									</td>
								</tr>
								<tr id="fileUpldCircle">
									<td width="36%"><label class="mand">Upload Icon</label>
									</td>
									<td width="64%"><form:errors cssClass="errorDsply"
											path="logoImageName"></form:errors> <label> <img
											id="iconincTmptImg" width="60" height="60" alt="uploadCircle"
											src="${sessionScope.setupcombomenuImgCircle}"> </label> <span
										class="topPadding cmnRow"> <label for="trgrUpldCombo">
												<input type="button" value="Upload" id="trgrUpldComboBtn"
												class="btn trgrUpldCombo" title="Upload Image File"
												tabindex="1"> <form:hidden path="logoImageName"
													id="logoImageName" /> <span class="instTxt nonBlk"></span>
												<form:input type="file" class="textboxBig"
													id="trgrUpldCombo" path="logoImage" /> </label> </span> <label
										id="maxSizeImageError" class="errorDsply"></label>
									</td>
								</tr>
								<tr id="fileUpldSquare">
									<td width="36%"><label class="mand">Upload Icon</label>
									</td>
									<td width="64%"><form:errors cssClass="errorDsply"
											path="bannerImageName"></form:errors> <label> <img
											id="imageFileId" width="60" height="60" alt="uploadSquare"
											src="${sessionScope.setupcombomenuImgSquare}"> </label> <span
										class="topPadding cmnRow"> <label for="trgrUpldImg">
												<input type="button" value="Upload" id="trgrUpldBtnImg"
												class="btn trgrUpldImg" title="Upload Image File"
												tabindex="1"> <form:hidden path="bannerImageName"
													id="bannerImageName" /> <span class="instTxt nonBlk"></span>
												<form:input type="file" class="textboxBig" id="trgrUpldImg"
													path="imageFile" /> </label> </span> <label id="maxSizeOffImageError"
										class="errorDsply"></label>
									</td>
								</tr>
								<tr>
									<td width="36%"><label class="mand">Functionality</label>
									</td>
									<td width="64%" colspan="2"><form:errors
											cssClass="errorDsply" path="menuFucntionality"></form:errors>
										<div class="cntrl-grp zeroBrdr">

											<form:select path="menuFucntionality" class="slctBx"
												id="dataFnctn" onchange="updateBtnLink();">
												<option selected="selected" value="0">Select
													Functionality</option>
												<c:forEach items="${sessionScope.linkList}" var="link">
													<option value="${link.menuTypeId}"
														typeVal="${link.menuTypeVal}">${link.menuTypeName}</option>
												</c:forEach>
											</form:select>
										</div>
									</td>
								</tr>
								<tr>
									<td colspan="3"><ul class="infoList cmnList " id="AppSite">

											<c:forEach items="${sessionScope.appsitelst}" var="appsite">

												<li><span class="cell"> <form:input type="radio"
															name="AppSite" path="btnLinkId"
															id="AS-${appsite.appSiteId}" value="${appsite.appSiteId}" />
														${appsite.appSiteName} </span> <span class="cell">${appsite.retName},${appsite.address}</span>
												</li>


											</c:forEach>

											<li class="actn"><a href="javascript:void(0);"
												onclick="addAppsite(this)"><img src="images/btn_add.png"
													width="24" height="24" alt="add" class="addImg" /> Create
													New App Site</a>
											</li>
										</ul>
										<ul class="infoList cmnList" id="AnythingPage">
											<c:forEach items="${sessionScope.anythingPageList}"
												var="item">

												<li><span class="cell zeroMrgn"> <form:input
															type="radio" value="${item.hcAnythingPageId}"
															id="AP-${item.hcAnythingPageId}" name="anythingPage"
															path="btnLinkId" /> <c:out
															value="${item.anythingPageTitle }" /> </span> <span
													class="cell">${item.pageType}</span>
												</li>

											</c:forEach>
											<li class="actn"><a href="buildanythingpage.htm"><img
													src="images/btn_add.png" width="24" height="24" alt="add"
													class="addImg" /> Add New AnyThing<sup>TM</sup> Page</a>
											</li>
										</ul>
										<ul class="infoList cmnList menuLst" id="SubMenu">

											<c:forEach items="${sessionScope.subMenuList }" var="item">
												<li><span class="cell zeroMrgn"> <form:input
															type="radio" value="${item.menuId}" name="subMenu"
															id="SM-${item.menuId}" path="btnLinkId" /> <c:out
															value="${item.menuName }"></c:out> </span>
												</li>


											</c:forEach>


											<li class="actn"><a
												href="displaymenutemplate.htm?menuType=submenu"><img
													src="images/btn_add.png" width="24" height="24" alt="add"
													class="addImg" /> Add New SubMenu</a>
											</li>
										</ul>
										<div class="intput-actn">
											<input type="checkbox" name="subMenu" id="findchkAll" /> <label
												for="find1">Select All</label>
										</div>
										<ul class="infoList cmnList menuLst" id="Find">

											<c:forEach items="${sessionScope.businessCatList }"
												var="item">
												<li><span class="cell zeroMrgn"> <form:checkbox
															path="btnLinkId" id="FN-${item.catId}" class="main-ctrgy"
															value="${item.catId }-MC" /> <label for="find1">${item.catName
															}</label> </span> <!--For listout the subcategoirs of main category -->
													<c:if
														test="${item.subArrayList ne null && !empty item.subArrayList}">
														<ul class="sub-ctgry">
															<c:forEach items="${item.subArrayList}" var="subItem">


																<c:choose>
																	<c:when
																		test="${subItem.subCatId ne null && subItem.subCatId ne ''}">
																		<li><input type="checkbox" name="btnLinkId"
																			value="${subItem.subCatId}"
																			id="FNS-${subItem.subCatId}" /> <label>${subItem.subCatName}</label>
																		</li>
																	</c:when>
																	<c:otherwise>
																		<li><input type="checkbox" name="btnLinkId"
																			value="NULL" id="FNS-${subItem.subCatId}"
																			checked="checked" class="hidFindChk" /> <label>${subItem.subCatName}</label>
																		</li>
																	</c:otherwise>

																</c:choose>
															</c:forEach>

														</ul>
													</c:if>
												</li>


											</c:forEach>

										</ul>

										<div class="input-actn-evnt">
											<input type="checkbox" id="evntchkAll" /> <label
												for="evntchkAll">Select All</label>
										</div>
										<ul class="infoList cmnList menuLst" id="Events">
											<c:forEach items="${sessionScope.eventCatList }" var="item">
												<li><span class="cell zeroMrgn"> <form:checkbox
															path="btnLinkId" name="category" id="EVT-${item.catId}"
															value="${item.catId }" /> <label for="find1">${item.catName
															}</label> </span>
												</li>


											</c:forEach>
										</ul>
										
										
											<!-- start : Adding code for fundraiser categories.. -->
							<div class="input-actn-fundraiser">
								<input type="checkbox" id="fundraChkAll" />
								<label for ="fundraChkAll">Select All</label>
															
							</div>
							<ul class="infoList cmnList menuLst" id="Fundraisers">
							<c:forEach items="${sessionScope.fundraiserCatList}" var = "funEvt">
							
							<li><span class="cell zeroMrgn"> <form:checkbox
															path="btnLinkId"  id="FUNDEVT-${funEvt.catId}"
															value="${funEvt.catId }" /> <label for="Fundraiser">${funEvt.catName
															}</label> </span>
												</li>
							</c:forEach>
												
							</ul>		
								<!-- end : Adding code for fundraiser categories.. -->
	
										<div>
											<div class="input-actn-city mrgnTop cityPnl prntChkbx">
							                    <input type="checkbox" id="citychkAll" class="trgrChk"/>
							                    <label for="citychkAll">Cities</label>
							                 </div>
											<ul class="infoList cmnList menuLst cityPnl"  id="Cities">
												<c:forEach items="${sessionScope.citiesLst }" var="item">
													<li><span class="cell zeroMrgn"> <form:checkbox
															path="citiId" id="CITY-${item.cityExpId}"
															value="${item.cityExpId }" /> <label for="find1">${item.cityExpName
															}</label>
														</span>
													</li>
												</c:forEach>
											</ul>
										</div>
									
										
									</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td><input type="button" name="button" value="Add Button"
										class="btn-blue" id="addBtn"
										onclick="creatDeleteMenuItem('AddButton')" />
									</td>
									<td align="right"><input type="button" name="delBtn"
										value="Delete Button" class="btn-red" id="delBtn"
										onclick="creatDeleteMenuItem('DeleteButton')" />
									</td>
								</tr>
							</table>
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="cmnTbl" id="grpDetails">
								<tr class="">
									<td width="36%" align="left">Group Name</td>
									<td width="64%" colspan="2">
										<div class="cntrl-grp">
											<form:input path="grpName" type="text"
												class="inputTxtBig vldt" id="btnGroupTxt"
												onkeypress="return specialCharCheck(event)" />
										</div> <span class="lbl">Chars Left:</span><span
										class="lblcnt btnGroupTxt"></span></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td><input type="button" name="savGrp" value="Save Group"
										class="btn-blue" id="savGrp"
										onclick="creatDeleteMenuItem('UpdateGroup')" />
									</td>
									<td width="35%" align="right"><input type="button"
										name="delGrp" value="Delete Group" class="btn-red" id="delGrp"
										onclick="creatDeleteMenuItem('DeleteGroup')" />
									</td>
								</tr>
							</table>

						</div>
					</form:form>
					<div class="title-bar top-brdr">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-main-menu">&nbsp;</span>
							</li>
							<li>Tab Bar Controls</li>
						</ul>
					</div>
					<div class="cont-wrp ">
						<ul id="multiplChk" class="cmnUpl d mulplchk fixHt">
							<c:forEach items="${sessionScope.tabBarButtonsList}" var="item">

								<li><img src="${item.imagePath}" alt="about" title="image"
									height="50px" width="80px" class="active"
									id="${item.bottomBtnId}"> <input name="" type="checkbox"
									value="" class="tabBtn" />
								</li>
							</c:forEach>

						</ul>
					</div>
				</div>
				<div class="cont-block">
					<div class="title-bar">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-iphone">&nbsp;</span>
							</li>
							<li>App Preview</li>
						</ul>
					</div>
					<div class="prgrsStp">
						<ul>
							<li class="step1"><span>Tab Creation</span>
							</li>
							<li class="step2"><span>Save Template</span>
							</li>
							<li class="saveTmplt"><input type="button" name="button"
								value="Save" class="btn-blue" id="saveTmplt"
								onclick="saveTemplate()" />
							</li>
							<li class="clear"></li>
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
												width="50" height="30" />
											</td>
											<td width="54%" class="genTitle">
												<!--<img src="images/small-logo.png" width="86" height="35" alt="Logo" />-->
											</td>
											<td width="27%" align="center"></td>
										</tr>
									</table>
								</div>
								<div class="previewAreaScroll gnrlScrn">
									<div id="preview_ie" title="Banner Ad"></div>
									<ul id="dynTab" class="sortable ui-sortable comboView">

										<c:forEach items="${sessionScope.previewMenuItems}" var="item">
											<c:set var="grpID" value="Text-${item.menuBtnName}" />
											<c:choose>
												<c:when
													test="${item.menuIconId eq grpID ||  item.menuIconId eq 'Label'}">
													<li class="tabs grpHdr ${item.pageTitle}" iconid="${grpID}"
														onclick="editGrpName(this);"><c:out
															value="${item.menuBtnName}"></c:out>
													</li>
												</c:when>
												<c:otherwise>
													<li
														class="tabs ${item.hiddenbtnGroup} ${item.comboBtnType}"><a
														onclick="editComboTab(this)" class="${item.comboBtnType}"
														hiddenbtnGroup="${item.hiddenbtnGroup}"
														pageTitle="${item.pageTitle}"
														grpBtnTypeId="${item.comboBtnTypeId}"
														grpBtnType="${item.comboBtnType}"
														datactn="<c:out value='${item.menuFucntionality }'></c:out>"
														iconid="${item.menuIconId }"
														iconimgname="${item.logoImageName }"
														grpName="${item.btnGroup}" linkId="${item.btnLinkId }"
														btnDept="${item.btnDept }" btnType="${item.btnType }"
														subCat="${item.chkSubCate}" citiId="${item.citiId}" href="javascript:void(0)">
															<c:choose>
																<c:when test="${item.comboBtnType ne 'Rectangle'}">
																	<c:choose>
																		<c:when test="${item.comboBtnType eq 'Circle'}">
																			<div class="rnd">
																				<c:choose>
																					<c:when
																						test="${item.imagePath ne '/Images/temp/null' && !empty item.imagePath && null ne item.imagePath}">
																						<img width="60" height="60" alt="image"
																							src="<c:out value='${item.imagePath }'></c:out>" />
																					</c:when>
																					<c:otherwise>
																						<img width="60" height="60" alt="image"
																							src="images/uploadIcon.png" />
																					</c:otherwise>
																				</c:choose>
																			</div>
																			<span><c:out value='${item.menuBtnName }'></c:out>
																			</span>
																		</c:when>
																		<c:otherwise>
																			<div class="">
																				<c:choose>
																					<c:when
																						test="${item.imagePath ne '/Images/temp/null' && !empty item.imagePath && null ne item.imagePath}">
																						<img width="60" height="60" alt="image"
																							src="<c:out value='${item.imagePath }'></c:out>" />
																					</c:when>
																					<c:otherwise>
																						<img width="60" height="60" alt="image"
																							src="images/uploadIconSqr.png" />
																					</c:otherwise>
																				</c:choose>
																			</div>
																			<span><c:out value='${item.menuBtnName }'></c:out>
																			</span>
																		</c:otherwise>
																	</c:choose>
																</c:when>
																<c:otherwise>
																	<div class="">
																		<img width="50" height="50" class="lstImg" alt="image"
																			src="<c:out value='${item.imagePath }'></c:out>">
																	</div>
																	<span><c:out value='${item.menuBtnName }'></c:out>
																	</span>
																</c:otherwise>
															</c:choose> </a>
													</li>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</ul>
								</div>
							</div>
							<!--Iphone Preview For Login screen ends here-->
							<ul id="tab-main" class="tabbar">
								<c:forEach items="${sessionScope.menuBarButtonsList }"
									var="item">
									<li><img src="${item.imagePath }"
										id="pre-${item.bottomBtnId}" btmbtnid="${item.bottomBtnId}"
										width="80" height="50" alt="share" />
									</li>
								</c:forEach>
							</ul>
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
	function creatDeleteMenuItem(val) {
		//Below code for find -Subcategory implmenation 		
		var checkedSubCat = $.makeArray($('ul.sub-ctgry').find(
		'input[name="btnLinkId"]:checkbox:checked:visible')
		.map(function() {
			return $(this).val();
		}));
		
		$('#chkSubCate').val(checkedSubCat);
		
		var checkedMainCat = $.makeArray($('.main-ctrgy:checked')
				.map(function() {
					return $(this).val();
		}));		
		
		var subArr = [];
		$.each(checkedMainCat, function(i,val){
			var arr = $.makeArray($('input[name="btnLinkId"][value=' + val + ']').parent('span').next('ul.sub-ctgry').find(
						'input[name="btnLinkId"]:checkbox:checked:visible')
				.map(function() {
					return $(this).val();
				}));	
			if(arr.length === 0) {
			   subArr.push('null');
			   subArr.push('!~~!');
			}else {
			   subArr.push(arr.toString());
			   subArr.push('!~~!');
			}
		});
		var subCat = subArr.toString().replace(/,!~~!,/gi, "!~~!");
		if (subCat.match(/,!~~!$/)) {
			subCat = subCat.replace(',!~~!', "");
		}
		
		$('#subCatIds').val(subCat); 	
		/*var checkedSubCat = $.makeArray($('ul.sub-ctgry').find(
				'input[name="btnLinkId"]:checkbox:checked:visible')

		.map(function() {
			return $(this).val()
		}));
		//NULL!~~!86,87,88,89,90,91!~~!NULL!~~!111,112,113,114,115,!~~!
		var arr = [];
		
		var subLen = $('.sub-ctgry').length;
		var bndry = $('.main-ctrgy:checked').parent().next('ul.sub-ctgry');
		var prntEle = $(bndry).length;
		var vCatId = null;
		for (i = 0; i < prntEle; i++) {
			$(bndry).find('li').each(
					function() {
						vCatId = $(this).find('input[name="btnLinkId"]:checkbox:checked').val();
						arr[i++] = vCatId;
						if (($(this).is(":last-child"))) {
							arr[i++] = "!~~!";
						}
					});
		}
		var subCat = arr.toString();
		if (null != subCat) {
			subCat = subCat.replace(/,!~~!,/gi, "!~~!");
			if (subCat.endsWith(",!~~!")) {
				subCat = subCat.replace(',!~~!', "")
			}
		}

		$('#subCatIds').val(subCat);
		$('#chkSubCate').val(checkedSubCat);*/

		var grpBtnType = "";
		var grpBtnTypeId = "";
		var btnOrderList = "";
		$("#dynTab li").each(
				function() {

					if ($(this).hasClass("grpHdr")) {

						var grpName = $(this).attr("iconid");
						var btnType = $(this).next("li").find("a").attr(
								"grpbtntype");
						var btnTypeId = $(this).next("li").find("a").attr(
								"grpbtntypeId");

						if (btnOrderList == "") {
							btnOrderList = grpName;
							grpBtnType = btnType;
							grpBtnTypeId = btnTypeId;
						} else {
							btnOrderList = btnOrderList + "~" + grpName;
							grpBtnType = grpBtnType + "~" + btnType;
							grpBtnTypeId = grpBtnTypeId + "~" + btnTypeId;
						}

					} else {

						var btnId = $(this).find('a').attr("iconid");
						if (btnOrderList == "") {
							btnOrderList = btnId;
						} else {
							btnOrderList = btnOrderList + "~" + btnId;
						}

					}

				});

		if (!($("#grpDept").is(':checked'))) {

			$("#slctDept option[value='0']").prop("selected", "selected");
		}
		if (!($("#grpType").is(':checked'))) {

			$("#slctType option[value='0']").prop("selected", "selected");
		}

		$(".prgrsStp li:first").addClass("step1Actv");

		var grpType = document.getElementById("groupName");
		var grpTypeName = "";
		if (grpType.length != 1) {
			for (i = 1; i < grpType.length; i++) {

				if (grpTypeName == "") {

					grpTypeName = grpType.options[i].value;
				} else {

					grpTypeName = grpTypeName + "~" + grpType.options[i].value;
				}

			}

			document.screenSettingsForm.btnPosition.value = btnOrderList;
			document.screenSettingsForm.grpBtnType.value = grpBtnType;
			document.screenSettingsForm.grpBtnTypeId.value = grpBtnTypeId;
			document.screenSettingsForm.grpList.value = grpTypeName;
			document.screenSettingsForm.addDeleteBtn.value = val;
			document.screenSettingsForm.action = "createcombomenu.htm";
			document.screenSettingsForm.method = "POST";
			document.screenSettingsForm.submit();

		} else {

			var inputNm = $("input[name='pageTitle']:checked").val();
			if (inputNm == "Text") {
				inputNm = "Group";
			} else {
				inputNm = "Title";
			}

			alert("Please Add " + inputNm);
		}

	}

	$(document)
			.ready(
					function() {

						//Below code for find -Subcategory implmentation 
						var mainChkd = $('.main-ctrgy').prop('checked');
						if (mainChkd) {
							$(mainChkd).each(function(index, element) {
								$(this).parent("span").next("ul .sub-ctgry").show();
							});							
						} else {
							$('.sub-ctgry').hide();									
						}					

						if ($(".sub-ctgry input:checkbox[value='NULL']").length > 0) {
							$(".sub-ctgry input[value='NULL']").parent('li').hide();
						}

						$('.comboView li.Circle a span').each(function() {
							var val = $.trim($(this).text());
							if (/\s/.test(val)) {
								$(this).css("word-break", "keeep-all");
							} else {
								$(this).css("word-break", "break-all");
							}
						});

						if ($('.gnrlScrn').hasScrollBar()) {
							$(".comboView li.tabs a.Square").removeClass(
									"scrlOff").addClass("scrlOn");
							$(".comboView li.tabs a.Circle").removeClass(
									"scrlOff").addClass("scrlOn");
						} else {
							$(".comboView li.tabs a.Square").removeClass(
									"scrlOn").addClass("scrlOff");
							$(".comboView li.tabs a.Circle").removeClass(
									"scrlOn").addClass("scrlOff");
						}

						$('#trgrUpldCombo')
								.bind(
										'change',
										function() {
											var imageType = document
													.getElementById("trgrUpldCombo").value;
											var btnOrderList = "";
											var grpBtnType = "";
											var grpBtnTypeId = "";
											$("#dynTab li")
													.each(
															function() {

																if ($(this)
																		.hasClass(
																				"grpHdr")) {

																	var grpName = $(
																			this)
																			.attr(
																					"iconid");
																	var btnType = null;

																	if (btnOrderList == "") {
																		btnOrderList = grpName
																		grpBtnType = btnType;
																		grpBtnTypeId = btnType;
																	} else {
																		btnOrderList = btnOrderList
																				+ "~"
																				+ grpName;
																		grpBtnType = grpBtnType
																				+ "~"
																				+ btnType;
																		grpBtnTypeId = grpBtnTypeId
																				+ "~"
																				+ btnType;
																	}

																} else {

																	var btnId = $(
																			this)
																			.find(
																					'a')
																			.attr(
																					"iconid");
																	var btnType = $(
																			this)
																			.find(
																					'a')
																			.attr(
																					"grpbtntype");
																	var btnTypeId = $(
																			this)
																			.find(
																					'a')
																			.attr(
																					"grpbtntypeId");
																	if (btnOrderList == "") {
																		btnOrderList = btnId;
																		grpBtnType = btnType;
																		grpBtnTypeId = btnTypeId;
																	} else {
																		btnOrderList = btnOrderList
																				+ "~"
																				+ btnId;
																		grpBtnType = grpBtnType
																				+ "~"
																				+ btnType;
																		grpBtnTypeId = grpBtnTypeId
																				+ "~"
																				+ btnTypeId;
																	}
																}

															});
											var grpType = document
													.getElementById("groupName");
											var grpTypeName = "";
											if (grpType.length != 1) {
												for (i = 1; i < grpType.length; i++) {

													if (grpTypeName == "") {

														grpTypeName = grpType.options[i].value;
													} else {

														grpTypeName = grpTypeName
																+ "~"
																+ grpType.options[i].value;
													}
												}

											}
											document.screenSettingsForm.grpList.value = grpTypeName;

											document.screenSettingsForm.btnPosition.value = btnOrderList;
											document.screenSettingsForm.grpBtnType.value = grpBtnType;
											document.screenSettingsForm.grpBtnTypeId.value = grpBtnTypeId;
											
											var checkedSubCat = $.makeArray($('ul.sub-ctgry').find('input[name="btnLinkId"]:checkbox:checked:visible')
													.map(function() {
														return $(this).val();
													}));
											
											document.screenSettingsForm.hiddenSubCate.value = checkedSubCat;

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

						$('#trgrUpldImg')
								.bind(
										'change',
										function() {
											var imageType = document
													.getElementById("trgrUpldImg").value;
											var uploadImageType = $(this).attr(
													'name');
											document.screenSettingsForm.uploadImageType.value = uploadImageType;
											if ($("#dynTab li").find("a").length != 0) {
												var btnOrderList = "";
												var grpBtnType = "";
												var grpBtnTypeId = "";
												$("#dynTab li")
														.each(
																function() {
																	if ($(this)
																			.hasClass(
																					"grpHdr")) {

																		var grpName = $(
																				this)
																				.attr(
																						"iconid");
																		var btnType = null;

																		if (btnOrderList == "") {
																			btnOrderList = grpName
																			grpBtnType = btnType;
																			grpBtnTypeId = btnType;
																		} else {
																			btnOrderList = btnOrderList
																					+ "~"
																					+ grpName;
																			grpBtnType = grpBtnType
																					+ "~"
																					+ btnType;
																			grpBtnTypeId = grpBtnTypeId
																					+ "~"
																					+ btnType;
																		}

																	} else {

																		var btnId = $(
																				this)
																				.find(
																						'a')
																				.attr(
																						"iconid");
																		var btnType = $(
																				this)
																				.find(
																						'a')
																				.attr(
																						"grpbtntype");
																		var btnTypeId = $(
																				this)
																				.find(
																						'a')
																				.attr(
																						"grpbtntypeId");
																		if (btnOrderList == "") {
																			btnOrderList = btnId;
																			grpBtnType = btnType;
																			grpBtnTypeId = btnTypeId;
																		} else {
																			btnOrderList = btnOrderList
																					+ "~"
																					+ btnId;
																			grpBtnType = grpBtnType
																					+ "~"
																					+ btnType;
																			grpBtnTypeId = grpBtnTypeId
																					+ "~"
																					+ btnTypeId;
																		}
																	}

																});
												document.screenSettingsForm.btnPosition.value = btnOrderList;
												document.screenSettingsForm.grpBtnType.value = grpBtnType;
												document.screenSettingsForm.grpBtnTypeId.value = grpBtnTypeId;
											}
											var grpType = document
													.getElementById("groupName");
											var grpTypeName = "";
											if (grpType.length != 1) {
												for (i = 1; i < grpType.length; i++) {

													if (grpTypeName == "") {

														grpTypeName = grpType.options[i].value;
													} else {

														grpTypeName = grpTypeName
																+ "~"
																+ grpType.options[i].value;
													}
												}

											}
											document.screenSettingsForm.grpList.value = grpTypeName;
											
											var checkedSubCat = $.makeArray($('ul.sub-ctgry').find('input[name="btnLinkId"]:checkbox:checked:visible')
													.map(function() {
														return $(this).val();
													}));
											
											document.screenSettingsForm.hiddenSubCate.value = checkedSubCat;

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
																						'#maxSizeOffImageError')
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

						$("#groupName")
								.on(
										'change.trgrFile',
										function() {
											var slctType = $('option:selected',
													$(this)).text();
											var btnType;
											$("#dynTab li")
													.each(
															function(index,
																	element) {
																if ($(this)
																		.hasClass(
																				"grpHdr")) {
																	var cl = $(
																			this)
																			.text();
																	if ($
																			.trim(cl) == slctType) {
																		var nextli = $(
																				this)
																				.next()
																				.find(
																						"a");
																		btnType = nextli
																				.attr("grpbtntype");
																		$(
																				"#btn-type option:contains("
																						+ btnType
																						+ ")")
																				.prop(
																						"selected",
																						"selected");
																		document.screenSettingsForm.comboBtnType.value = '';
																		$(
																				"#btn-type option:selected")
																				.trigger(
																						'change.trgrFile');
																		return false;
																	}
																}
															});
										});

						$("#btn-type")
								.on(
										'change.trgrFile',
										function() {
											var grpName = $('#groupName').val();
											var hGrpName = grpName.replace(
													/\ /g, "");
											var hiddenGrpName = $
													.trim(hGrpName);
											var containsGrp = false;
											$("#dynTab li")
													.each(
															function(index,
																	element) {
																if ($(this)
																		.hasClass(
																				hiddenGrpName)) {
																	containsGrp = true;
																}
															});
											var slctType = $('option:selected',
													$(this)).text();
											var btnTypeId = $(
													'option:selected', $(this))
													.val();
											var oldBtnType = document.screenSettingsForm.comboBtnType.value;

											if (oldBtnType != ""
													&& slctType != oldBtnType) {
												if (document
														.getElementById('bannerImageName.errors') != null) {
													document
															.getElementById('bannerImageName.errors').style.display = 'none';
												}

												if (document
														.getElementById('logoImageName.errors') != null) {
													document
															.getElementById('logoImageName.errors').style.display = 'none';
												}
												if (document
														.getElementById('comboBtnType.errors') != null) {
													document
															.getElementById('comboBtnType.errors').style.display = 'none';
												}
											}

											if (oldBtnType != ""
													&& grpName != "0"
													&& containsGrp == true
													&& slctType != oldBtnType) {
												var pageTitle = $(
														"input[name='pageTitle']:checked")
														.val();
												if (pageTitle == "Text") {
													pageTitle = "Group";
												} else {
													pageTitle = "Title";
												}
												var alertMsg = "Do you want to change the button type for this "
														+ pageTitle + "?";

												if (!confirm(alertMsg)) {
													document.screenSettingsForm.comboBtnType.value = oldBtnType;
													$(
															"#btn-type option:contains("
																	+ oldBtnType
																	+ ")")
															.prop("selected",
																	"selected");
													return false;
												} else {
													document.screenSettingsForm.comboBtnType.value = slctType;
													if (slctType === "Rectangle"
															|| slctType === "Select Type") {
														$("#fileUpldCircle")
																.hide();
														$("#fileUpldSquare")
																.hide();
													} else if (slctType === "Square") {
														$("#fileUpldCircle")
																.hide();
														$("#fileUpldSquare")
																.show();
													} else if (slctType === "Circle") {
														$("#fileUpldSquare")
																.hide();
														$("#fileUpldCircle")
																.show();
													}

													var noImage = false;

													$("#dynTab li")
															.each(
																	function(
																			index,
																			element) {
																		if ($(
																				this)
																				.hasClass(
																						hiddenGrpName
																								+ " "
																								+ oldBtnType)
																				|| $(
																						this)
																						.hasClass(
																								oldBtnType
																										+ " "
																										+ hiddenGrpName)
																				|| $(
																						this)
																						.hasClass(
																								hiddenGrpName
																										+ " active "
																										+ oldBtnType)) {
																			$(
																					this)
																					.removeClass(
																							oldBtnType)
																					.addClass(
																							slctType);
																			var nextli = $(
																					this)
																					.find(
																							"a");
																			if (oldBtnType === "Square"
																					|| oldBtnType === "Circle") {
																				$(
																						nextli)
																						.removeClass(
																								"scrlOn");
																				$(
																						nextli)
																						.removeClass(
																								"scrlOff");
																			}
																			nextli
																					.attr(
																							"grpbtntype",
																							slctType);
																			nextli
																					.attr(
																							"grpbtntypeId",
																							btnTypeId);
																			$(
																					nextli)
																					.removeClass(
																							oldBtnType)
																					.addClass(
																							slctType);
																			nextli = $(
																					this)
																					.find(
																							"div");

																			if (slctType === "Circle") {
																				$(
																						nextli)
																						.addClass(
																								"rnd");
																				nextli = $(
																						this)
																						.find(
																								"img");
																				$(
																						".comboView li.tabs.Circle")
																						.removeAttr(
																								"style");

																				var menuLevel = document.screenSettingsForm.menuLevel.value;
																				if (menuLevel == 1) {
																					var mainMenuBtnClr = '${sessionScope.mainMenuIconsFntClr}';
																					$(
																							".comboView li.tabs.Circle a")
																							.css(
																									"color",
																									mainMenuBtnClr);
																				} else {
																					var subMenuBtnClr = '${sessionScope.subMenuIconsFntClr}';
																					$(
																							".comboView li.tabs.Circle a")
																							.css(
																									"color",
																									subMenuBtnClr);
																				}
																			} else if (oldBtnType === "Circle") {
																				$(
																						nextli)
																						.removeClass(
																								"rnd");
																				var menuLevel = document.screenSettingsForm.menuLevel.value;
																				if (menuLevel == 1) {
																					var mainMenuBtnClr = '${sessionScope.mainMenuBtnClr}';
																					var mainMenuFntClr = '${sessionScope.mainMenuFntClr}';
																					$(
																							".comboView li.tabs.Rectangle a")
																							.css(
																									"color",
																									mainMenuFntClr);
																					$(
																							".comboView li.tabs.Square a")
																							.css(
																									"color",
																									mainMenuFntClr);
																					$(
																							".comboView li.tabs.Rectangle")
																							.css(
																									"background-color",
																									mainMenuBtnClr);

																				} else {
																					var subMenuBtnClr = '${sessionScope.subMenuBtnClr}';
																					var subMenuFntClr = '${sessionScope.subMenuFntClr}';
																					$(
																							".comboView li.tabs.Rectangle a")
																							.css(
																									"color",
																									subMenuFntClr);
																					$(
																							".comboView li.tabs.Square a")
																							.css(
																									"color",
																									subMenuFntClr);
																					$(
																							".comboView li.tabs.Rectangle")
																							.css(
																									"background-color",
																									subMenuBtnClr);
																				}
																			}
																			if (oldBtnType === "Square") {
																				var menuLevel = document.screenSettingsForm.menuLevel.value;
																				if (menuLevel == 1) {
																					var mainMenuBtnClr = '${sessionScope.mainMenuBtnClr}';
																					$(
																							".comboView li.tabs.Rectangle")
																							.css(
																									"background-color",
																									mainMenuBtnClr);

																				} else {
																					var subMenuBtnClr = '${sessionScope.subMenuBtnClr}';
																					$(
																							".comboView li.tabs.Rectangle")
																							.css(
																									"background-color",
																									subMenuBtnClr);
																				}
																			}

																			if (slctType === "Square") {
																				$(
																						".comboView li.tabs.Square")
																						.css(
																								"background-color",
																								"");
																				nextli = $(
																						this)
																						.find(
																								"img");
																				nextli
																						.attr(
																								"height",
																								60);
																				nextli
																						.attr(
																								"width",
																								60);
																			}
																			if (oldBtnType === "Rectangle") {
																				if (slctType === "Square") {
																					src = $(
																							this)
																							.find(
																									"div")
																							.find(
																									"img")
																							.attr(
																									"src");
																					if (src != 'images/uploadIcon.png'
																							&& src != "") {
																						var image = "<img width='60' height='60' src=" + src +" class='lstImg' alt='image'>";
																					} else {
																						var image = "<img width='60' height='60' src='images/uploadIconSqr.png' class='lstImg' alt='image'>";
																						noImage = true;
																					}
																					nextli = $(
																							this)
																							.find(
																									"div");
																					nextli
																							.empty();
																					nextli
																							.append(image);
																				} else {
																					src = $(
																							this)
																							.find(
																									"div")
																							.find(
																									"img")
																							.attr(
																									"src");
																					if (src != 'images/uploadIconSqr.png'
																							&& src != "") {
																						var image = "<img width='60' height='60' src=" + src +" class='lstImg' alt='image'>";
																					} else {
																						var image = "<img width='60' height='60' src='images/uploadIcon.png' class='lstImg' alt='image'>";
																						noImage = true;
																					}
																					nextli = $(
																							this)
																							.find(
																									"div");
																					nextli
																							.empty();
																					nextli
																							.append(image);
																				}
																			} else if (oldBtnType === "Square"
																					&& slctType === "Circle") {
																				var src = $(
																						this)
																						.find(
																								"div img")
																						.attr(
																								"src");
																				if (src == 'images/uploadIconSqr.png') {
																					var image = "<img width='60' height='60' src='images/uploadIcon.png' class='lstImg' alt='image'>";
																					nextli = $(
																							this)
																							.find(
																									"div");
																					nextli
																							.empty();
																					nextli
																							.append(image);
																				}
																			} else if (oldBtnType === "Circle"
																					&& slctType === "Square") {
																				var src = $(
																						this)
																						.find(
																								"div img")
																						.attr(
																								"src");
																				if (src == 'images/uploadIcon.png') {
																					var image = "<img width='60' height='60' src='images/uploadIconSqr.png' class='lstImg' alt='image'>";
																					nextli = $(
																							this)
																							.find(
																									"div");
																					nextli
																							.empty();
																					nextli
																							.append(image);
																				}
																			}
																		}
																	});

													if (oldBtnType === "Rectangle"
															&& noImage == true) {
														alert('Please upload images for all the button in the '
																+ pageTitle);
													}
												}
											} else {
												document.screenSettingsForm.comboBtnType.value = slctType;
												if (slctType === "Rectangle"
														|| slctType === "Select Type") {
													$("#fileUpldCircle").hide();
													$("#fileUpldSquare").hide();
												} else if (slctType === "Square") {
													$("#fileUpldCircle").hide();
													$("#fileUpldSquare").show();
												} else if (slctType === "Circle") {
													$("#fileUpldSquare").hide();
													$("#fileUpldCircle").show();
												}
											}

											if (slctType === "Square"
													|| slctType === "Circle") {
												if ($('.gnrlScrn')
														.hasScrollBar()) {
													$(
															".comboView li.tabs a.Square")
															.removeClass(
																	"scrlOff")
															.addClass("scrlOn");
													$(
															".comboView li.tabs a.Circle")
															.removeClass(
																	"scrlOff")
															.addClass("scrlOn");
												} else {
													$(
															".comboView li.tabs a.Square")
															.removeClass(
																	"scrlOn")
															.addClass("scrlOff");
													$(
															".comboView li.tabs a.Circle")
															.removeClass(
																	"scrlOn")
															.addClass("scrlOff");
												}
											}
										});
						$("#btn-type option:selected").trigger(
								'change.trgrFile');

						$("input[name='pageTitle']")
								.on(
										'change.trgrFile',
										this,
										function() {
											if (document.screenSettingsForm.hiddenPageTitle.value == "") {
												if (document
														.getElementById('btnGroup.errors') != null) {
													document
															.getElementById('btnGroup.errors').style.display = 'none';
												}

												if (document
														.getElementById('bannerImageName.errors') != null) {
													document
															.getElementById('bannerImageName.errors').style.display = 'none';
												}

												if (document
														.getElementById('logoImageName.errors') != null) {
													document
															.getElementById('logoImageName.errors').style.display = 'none';
												}

												if (document
														.getElementById('menuBtnName.errors') != null) {
													document
															.getElementById('menuBtnName.errors').style.display = 'none';
												}
												if (document
														.getElementById('menuFucntionality.errors') != null) {
													document
															.getElementById('menuFucntionality.errors').style.display = 'none';
												}
												if (document
														.getElementById('subMenuName.errors') != null) {
													document
															.getElementById('subMenuName.errors').style.display = 'none';
												}
												if (document
														.getElementById('screenSettingsForm.errors') != null) {
													document
															.getElementById('screenSettingsForm.errors').style.display = 'none';
												}

												if (document
														.getElementById('btnDept.errors') != null) {
													document
															.getElementById('btnDept.errors').style.display = 'none';
												}
												if (document
														.getElementById('btnType.errors') != null) {
													document
															.getElementById('btnType.errors').style.display = 'none';
												}
												if (document
														.getElementById('comboBtnType.errors') != null) {
													document
															.getElementById('comboBtnType.errors').style.display = 'none';
												}
											}

											document.screenSettingsForm.hiddenPageTitle.value = "";

											var getHdr = $(this).attr(
													'objTitle');
											var tdHdr = $(this).parents('tr')
													.next('tr')
													.find('td:first').text(
															"Select " + getHdr);
											$("#groupName option:eq(0)").html(
													tdHdr.text());
											var tmpWrd = tdHdr.text();
											var lastword = tmpWrd.split(" ")
													.pop();
											$(".addGrp").attr("title",
													"Add New " + lastword);
											$("tr.show-group").find('td:eq(0)')
													.text(getHdr + " Name");
											$("ul.Group").attr("id", lastword);
											if (getHdr !== "Group") {
												$("#dynTab").find('li.grpHdr')
														.removeClass("Text")
														.addClass("Label");
												//$("#dynTab").find('li.grpHdr').removeAttr("style");
											} else {
												$("#dynTab").find('li.grpHdr')
														.removeClass("Label")
														.addClass("Text");
												var menuLevel = document.screenSettingsForm.menuLevel.value;
												if (menuLevel == 1) {
													var mainMenuGrpClr = '${sessionScope.mainMenuGrpClr}'
													var mainMenuGrpFntClr = '${sessionScope.mainMenuGrpFntClr}'
													$("#dynTab li.grpHdr.Text")
															.css("background",
																	mainMenuGrpClr);
													$("#dynTab li.grpHdr").css(
															"color",
															mainMenuGrpFntClr);
												} else {
													var subMenuGrpClr = '${sessionScope.subMenuGrpClr}'
													var subMenuGrpFntClr = '${sessionScope.subMenuGrpFntClr}'

													$("#dynTab li.grpHdr.Text")
															.css("background",
																	subMenuGrpClr);
													$("#dynTab li.grpHdr").css(
															"color",
															subMenuGrpFntClr);
												}
											}
										});
						$("input[name='pageTitle']:checked").trigger(
								'change.trgrFile');

						/* Displaying button length */
						$("#btnType").limiter(20, $(".btnType"));
						$("#btnDept").limiter(20, $(".btnDept"));
						$("#btnName").limiter(24, $(".btnName"));
						$("#subMenuInput").limiter(35, $(".subMenuInput"));
						$("#btnGroupTxt").limiter(25, $(".btnGroupTxt"));
						$("#btnGroup").limiter(25, $(".btnGroupName"));
						var grpAction = $("#addDeleteBtn").val;

						if (grpAction == "UpdateGroup"
								|| grpAction == "DeleteGroup") {

							tglForm();
						}

						var licnt = $("#dynTab li").length;
						if (licnt) {
							$(".prgrsStp li:first").addClass("step1Actv");
						}

						var menuIconId = document.screenSettingsForm.menuIconId.value
						if (null !== menuIconId && menuIconId != "") {
							$("#addBtn").attr("value", "Save Button");
							$("#delBtn").show();
						}

						onLoadslctFnctn();
						displaySubMenuName();

						var selFuncVal = $("#dataFnctn option:selected").attr('typeVal');
						$("#dataFnctn option:selected").trigger('change');
						$("#dataFnctn option[typeVal='" + selFuncVal + "']").prop( "selected", true );
						var hiddenBtnLinkId = document.screenSettingsForm.hiddenBtnLinkId.value;
						var hiddenSubCate = document.screenSettingsForm.hiddenSubCate.value;
						if (selFuncVal == "SubMenu") {

							$('#SM-' + hiddenBtnLinkId).prop('checked',
									'checked');
							$(".cmnList").hide();
							$("#" + selFuncVal).show();

						} else if (selFuncVal == "AnythingPage") {

							$('#AP-' + hiddenBtnLinkId).prop('checked',
									'checked');
							$(".cmnList").hide();
							$("#" + selFuncVal).show();

						} else if (selFuncVal == "AppSite") {

							$('#AS-' + hiddenBtnLinkId).prop('checked',
									'checked');
							$(".cmnList").hide();
							$("#" + selFuncVal).show();

						} else if (selFuncVal == "Find") {
							$('#Find input[name="btnLinkId"]').prop('checked',
									false);
							var arr = hiddenBtnLinkId.split(',');

							jQuery.each(arr,
									function(i, val) {
										if (jQuery(val).index("MC") === -1) {

											val = val.substring(0, val
													.lastIndexOf("-"));

											$('#FN-' + val).prop('checked',
													'checked');
											$('#FN-' + val).parent().next(
													'.sub-ctgry').show();
										}

									});
							//for subcategories 
							if (hiddenSubCate != null) {

								hiddenSubCate = hiddenSubCate.replace(
										/NULL!~~!/gi, "");
								hiddenSubCate = hiddenSubCate.replace(
										/!~~!NULL/gi, "");
								var arr2 = hiddenSubCate.split(',');
								jQuery.each(arr2,
										function(i, val) {
											$('#FNS-' + val).prop('checked',
													'checked');
										});
							}
							$(".intput-actn").show();
							$(".cmnList").hide();
							$("#" + selFuncVal).show();
							/* var busCatList = $("#hiddenFindCategory").val()
									.split(",");

							for ( var k = 0; k < busCatList.length; k++) {
								if (busCatList[k].contains('MC')) {

									busCatList[k] = busCatList[k].substring(0,
											busCatList[k].lastIndexOf("-"));

									$("#FN-" + busCatList[k]).prop('checked',
											true);
									$('#FN-' + busCatList[k]).parent().next(
											'.sub-ctgry').show();
								}
							} */

							var tolCnt = $('#Find input[name$="btnLinkId"]:checkbox:visible').length;
							var chkCnt = $('#Find input[name$="btnLinkId"]:checkbox:checked:visible').length;
							if (tolCnt == chkCnt) {
								$('#findchkAll').prop('checked', 'checked');
							}
							else {
								$('#findchkAll').removeAttr('checked');
							}
						} else if (selFuncVal == "Events") {

							$('#Events input[name="btnLinkId"]').prop(
									'checked', false);
							var arr = hiddenBtnLinkId.split(',');

							jQuery.each(arr, function(i, val) {
								$('#EVT-' + val).prop('checked', 'checked');
							});
							var busCatList = $("#hiddenFindCategory").val()
									.split(",");

							for ( var k = 0; k < busCatList.length; k++) {

								$("#EVT-" + busCatList[k])
										.prop('checked', true);
							}
							$(".input-actn-evnt").show();
							var tolCnt = $("#Events input[type=checkbox][name='btnLinkId']").length;
							var chkCnt = $('#Events input[name$="btnLinkId"]:checked').length;
							if (tolCnt == chkCnt) {

								$('#evntchkAll').prop('checked', 'checked');
							}

							else {
								$('#evntchkAll').removeAttr('checked');
							}
							$(".cmnList").hide();
							$("#" + selFuncVal).show();

						} 
						
						//Start : Adding changes related to fundraiser implementation 
						else if(selFuncVal == "Fundraisers"){
						
							var arr = hiddenBtnLinkId.split(',');
							jQuery.each(arr,function(i,val)
								{								
								$('#FUNDEVT-'+val).prop('checked','checked');
								
								});
							
							var fundEvtCatIdLst = $("#hiddenFundEvtId").val().split(",");
							for(var fnd=0; fnd<fundEvtCatIdLst.length;fnd++)
								{
								$("#FUNDEVT-" +fundEvtCatIdLst[fnd]).prop('checked',true);
								
								}
							
							$(".input-actn-fundraiser").show();
							var tolCnt = $("#Fundraisers input[type=checkbox][name='btnLinkId']").length;
							var chkCnt = $("#Fundraisers input[name$='btnLinkId']:checked").length;
							if(tolCnt == chkCnt)
								{
								$('#fundraChkAll').prop('checked','checked');
								}else{
									$('#fundraChkAll').removeAttr('checked');
								}
							$(".cmnList").hide();
							$("#" +selFuncVal).show();
						}
						//End : Adding changes related to fundraiser implementation 
					
						
						//Start: Changes related to Adding cities
						var loginUserType = document.screenSettingsForm.userType.value;
						
						if (loginUserType == "RegionApp" && "undefined" != typeof(selFuncVal)) {
							
							$('#Cities input[name="citiId"]').prop(
									'checked', false);
							var hiddenCitiId = document.screenSettingsForm.hiddenCitiId.value;
							var arr = hiddenCitiId.split(',');

							jQuery.each(arr, function(i, val) {
								$('#CITY-' + val).prop('checked', 'checked');
							});
							
							$(".cityPnl").show();
							var tolCnt = $("#Cities input[type=checkbox][name='citiId']").length;
							var chkCnt = $('#Cities input[name$="citiId"]:checked').length;
							if (tolCnt == chkCnt) {
								$('#citychkAll').prop('checked', 'checked');
							}
							else {
								$('#citychkAll').removeAttr('checked');
							}						
						}
						//End: Changes related to Adding cities

						/*
						 * Code for bottom navigation
						 */
						$("#multiplChk")
								.on(
										'click',
										'li:not(.dsbled), li:not(.dsbled) input',
										function(e) {
											var chkSts, curImg, $chkBx;
											if (this.tagName.toUpperCase() === 'INPUT') {
												// checkbox function
												$chkBx = $(this);
												e.stopPropagation();
											} else {
												$chkBx = $(this).find(
														":checkbox");
												if (!$chkBx.prop("checked")) {
													$chkBx
															.prop("checked",
																	true);
												} else {
													$chkBx.prop("checked",
															false);
												}
											}

											chkSts = $chkBx[0].checked;
											curImg = $chkBx.prev('img')[0];
											// this code for checking length 4
											var ckkCnt = $("#multiplChk :checked").length;
											if (ckkCnt >= 4) {
												$(
														"#multiplChk :checkbox:not(:checked)")
														.each(
																function() {
																	$(this)
																			.parents(
																					'li')
																			.addClass(
																					'dsbled');
																	$(
																			'#multiplChk input:checkbox:not(:checked)')
																			.prop(
																					'disabled',
																					true);

																});
											} else {
												$("#multiplChk .dsbled")
														.removeClass('dsbled');
												$(
														'#multiplChk input:checkbox:not(:checked)')
														.prop('disabled', false);
											}

											if (chkSts) {
												// Add selected tab image to iphone
												// preview
												$('#tab-main')
														.append(
																"<li><img src='"
																+ curImg.src
																+ "' id='pre-"
																+ curImg.id
																+ "' btmBtnId='"
																+ curImg.id
																+ "' width='80' height='50'/></li>");
											} else {
												// Remove selected tab image from
												// iphone preview
												$('#tab-main #pre-' + curImg.id)
														.parent('li').remove();
											}
										});

						var slctTbIdArr = [];

						$('#tab-main li').find('img').each(function() {
							var id = $(this).attr("btmbtnid");
							slctTbIdArr.push(id);

						});

						for ( var i = 0; i < slctTbIdArr.length; i = i + 1) {
							$("#" + slctTbIdArr[i]).next('input').attr(
									"checked", true);
						}

						if (slctTbIdArr.length != 0) {

							$("#multiplChk :checkbox:not(:checked)")
									.each(
											function() {
												$(this).parents('li').addClass(
														'dsbled');
												$(
														'#multiplChk input:checkbox:not(:checked)')
														.prop('disabled', true);

											});
						}

						var grpAction = '${requestScope.gprdMenuAction}';

						if (grpAction == "Group") {

							tglForm();
						}
					});

	function saveTemplate() {
		var btnOrderList = "";
		var grpBtnType = "";
		var grpBtnTypeId = "";
		$("#dynTab li").each(function() {
			if ($(this).has("a").length == 1) {
				var btnId = $(this).find('a').attr("iconid");
				var btnType = $(this).find('a').attr("grpbtntype");
				var btnTypeId = $(this).find('a').attr("grpbtntypeId");
				if (btnOrderList == "") {
					btnOrderList = btnId
				} else {
					btnOrderList = btnOrderList + "~" + btnId;
				}
				if (grpBtnType == "") {
					grpBtnType = btnType;
				} else {
					grpBtnType = grpBtnType + "~" + btnType;
				}
				if (grpBtnTypeId == "") {
					grpBtnTypeId = btnTypeId;
				} else {
					grpBtnTypeId = grpBtnTypeId + "~" + btnTypeId;
				}

			} else {
				var grpName = $(this).attr("iconid");
				var btnType = null;

				if (btnOrderList == "") {
					btnOrderList = grpName
				} else {
					btnOrderList = btnOrderList + "~" + grpName;
				}
				if (grpBtnType == "") {
					grpBtnType = btnType;
				} else {
					grpBtnType = grpBtnType + "~" + btnType;
				}
				if (grpBtnTypeId == "") {
					grpBtnTypeId = btnType;
				} else {
					grpBtnTypeId = grpBtnTypeId + "~" + btnType;
				}
			}
		});

		$.ajaxSetup({
			cache : false
		});
		var licnt = $("#dynTab li").length;

		var bottmBtnId = "";

		if (licnt) {

			var bottmBtnLen = $("#tab-main li").length;

			if (bottmBtnLen != 0 && bottmBtnLen <= 3) {
				alert("Please select 4 Tab Bar Controls")
			} else {
				$("#tab-main li").find("img").each(function() {
					var t = $(this).attr("btmBtnId");
					if (bottmBtnId == "") {
						bottmBtnId = t
					} else {
						bottmBtnId = bottmBtnId + "," + t;
					}

				});
				var menuId = document.screenSettingsForm.menuId.value;
				var menuLevel = document.screenSettingsForm.menuLevel.value;
				var subMenuName = document.screenSettingsForm.subMenuName.value;
				var pageTitle = $("input[name='pageTitle']:checked").val();

				var typeFlag = false;
				var deptFlag = false;

				$("input[name='menuFilterType']:checked").each(function() {

					var selval = $(this).attr("value");

					if (selval == 'Department') {
						deptFlag = true;
					}
					if (selval == 'Type') {
						typeFlag = true;
					}

				});

				$
						.ajax({
							type : "POST",
							url : "savecombotemplate.htm",
							data : {
								'menuId' : menuId,
								'menuLevel' : menuLevel,
								'subMenuName' : subMenuName,
								'bottmBtnId' : bottmBtnId,
								'template' : 'Combo Template',
								'typeFilter' : typeFlag,
								'deptFilter' : deptFlag,
								'btnOrder' : btnOrderList,
								'grpBtnType' : grpBtnType,
								'grpBtnTypeId' : grpBtnTypeId,
								'pageTitle' : pageTitle
							},

							success : function(response) {

								if (response == 'SUCCESS') {

									alert("Template Saved");
									$(".prgrsStp li:eq(0)").addClass(
											"step1Actv");
									$(".prgrsStp li:eq(1)").addClass(
											"step2Actv");
								} else if (response == 'Associate Dept') {
									alert('Please associate Department for all the bottons to continue saving');
								} else if (response == 'Associate Type') {
									alert('Please associate Type for all the bottons to continue saving');
								} else if (response == 'Associate City') {
									alert('Please associate City for all the bottons to continue saving');
								} else if (response == 'GroupBtnType') {
									alert('Please associate same Button Type for all the bottons within the Group to continue saving');
								} else if (response == 'GroupBtnImage') {
									alert('Please Upload Image for the button(s)');
								} else {
									window.location.href = "/HubCiti/sessionTimeout.htm"
								}
							},
							error : function(e) {
								alert('Error occurred while saving template');
							}

						});
			}

		} else {
			alert("Empty Template");
		}
	}

	function onLoadslctFnctn() {
		var functionalityId = document.screenSettingsForm.hiddenmenuFnctn.value;
		var functionality = document.getElementById("dataFnctn");
		if (functionalityId != "null" || functionalityId != "") {

			for (i = 0; i < functionality.length; i++) {

				if (functionality.options[i].value == functionalityId) {
					functionality.options[i].selected = true;
					break;
				}
			}
		}

	}

	function displaySubMenuName() {
		var menuLevel = document.screenSettingsForm.menuLevel.value;

		if (menuLevel == 1) {

			$("#subMenuCont").addClass('btnNm');
			$("#grpingCont").addClass('btnNm');
			$("#grpingContTitle").addClass('btnNm');
			$("#menutitleDiv").addClass('btnNm');
			$("#subMenuDetails").addClass('btnNm');
			$("#slctTmpt").attr('href',
					"displaymenutemplate.htm?menuType=mainmenu");

			var mainMenuBGType = '${sessionScope.mainMenuBGType}';
			var mainMenuBG = '${sessionScope.mainMenuBG}'
			var mainMenuBtnClr = '${sessionScope.mainMenuBtnClr}'
			var mainMenuFntClr = '${sessionScope.mainMenuFntClr}'
			var mainMenuGrpClr = '${sessionScope.mainMenuGrpClr}'
			var mainMenuGrpFntClr = '${sessionScope.mainMenuGrpFntClr}'
			var mainMenuIconsFntClr = '${sessionScope.mainMenuIconsFntClr}'

			if (mainMenuBGType == 'Image') {
				$(".gnrlScrn").css("background",
						"url('" + mainMenuBG + "') no-repeat scroll 0 0");
			} else if (mainMenuBGType == 'Color') {
				$(".gnrlScrn").css("background", mainMenuBG);

			}
			$(".comboView li.tabs a").css("color", mainMenuFntClr);
			$(".comboView li.tabs.Circle a").css("color", mainMenuIconsFntClr);
			$(".comboView li.tabs.Rectangle").css("background-color",
					mainMenuBtnClr);
			/*$(".comboView li.tabs.Square").css("background-color",
					mainMenuBtnClr);*/
			$("#dynTab li.grpHdr.Text").css("background", mainMenuGrpClr);
			$("#dynTab li.grpHdr").css("color", mainMenuGrpFntClr);

		} else {

			var subMenuId = document.screenSettingsForm.menuId.value;
			var subMenuName = $("#subMenuInput").val()
			$("td.genTitle").text(subMenuName);
			$("#slctTmpt").attr(
					'href',
					"displaymenutemplate.htm?menuType=submenu&id=" + subMenuId
							+ "&menuname=" + subMenuName);

			$("#menutitle").text("Sub Menu")
			$("#menutitle-bread-crumb").text("Sub Menu [ Combo Template ]")

			/* Code for displaying menu background */
			var subMenuBGType = '${sessionScope.subMenuBGType}'
			var subMenuBG = '${sessionScope.subMenuBG}'
			var subMenuBtnClr = '${sessionScope.subMenuBtnClr}'
			var subMenuFntClr = '${sessionScope.subMenuFntClr}'
			var subMenuGrpClr = '${sessionScope.subMenuGrpClr}'
			var subMenuGrpFntClr = '${sessionScope.subMenuGrpFntClr}'
			var subMenuIconsFntClr = '${sessionScope.subMenuIconsFntClr}'

			if (subMenuBGType == 'Image') {
				$(".gnrlScrn").css("background",
						"url('" + subMenuBG + "') no-repeat scroll 0 0");

			} else if (subMenuBGType == 'Color') {
				$(".gnrlScrn").css("background", subMenuBG);

			}
			$(".comboView li.tabs a").css("color", subMenuFntClr)
			$(".comboView li.tabs.Circle a").css("color", subMenuIconsFntClr);
			$(".comboView li.Rectangle").css("background-color", subMenuBtnClr)
			/*$(".comboView li.Square").css("background-color", subMenuBtnClr)*/
			$("#dynTab li.grpHdr.Text").css("background", subMenuGrpClr);
			$("#dynTab li.grpHdr").css("color", subMenuGrpFntClr);

			var ismenuFilterTypeSelected = document.screenSettingsForm.ismenuFilterTypeSelected.value;

			if (ismenuFilterTypeSelected == 'true') {
				$('#grpType,#grpDept').prop('disabled', true);
				$('#grpDept')
						.click(
								function(e) {

									var chkdSlctn = $(this).attr("id");

									if (!($(this).is(':checked'))) {

										var rmvDept = confirm("Are you sure to remove Department association");
										if (rmvDept) {

											$(this).prop('checked', false);
											tglGrping(this);
										} else {

											$(this).prop('checked', true);
											tglGrping(this);
										}
									} else {
										$(this).prop('checked', true);
										tglGrping(this);
										alert("Please associate Department for all the buttons");
									}

								});
				$('#grpType')
						.click(
								function(e) {

									if (!($(this).is(':checked'))) {

										var rmvDept = confirm("Are you sure to remove Type association");
										if (rmvDept) {

											$(this).prop('checked', false);
											tglGrping(this);
										} else {

											$(this).prop('checked', true);
											tglGrping(this);
										}
									} else {
										$(this).prop('checked', true);
										tglGrping(this);
										alert("Please associate Type for all the buttons");
									}

								});

				$("#edittglGrping").removeClass("btnNm").addClass(
						'grpInputEdit');
			}
		}

	}
	function loadTab() {
		var tabCnt = $("#multiplChk li").length;

		if (tabCnt > 6) {
			$("#multiplChk").addClass("fixHt");
		} else {
			$("#multiplChk").removeClass("fixHt");
		}
	}
	loadTab();

	function tglForm() {
		$("#grpDetails").show();
		$("#tabDetails").hide();
	}
	function updateBtnLink() {
		$('input[name="btnLinkId"]').prop('checked', false);
	}

	function appendComboGrp(obj) {

		var isExist = false;
		var inputObj = $(obj).parents('ul').find('input:text'), inputVal = inputObj
				.val(), inputNm = $(obj).parents('ul').attr("class");
		inputVal = $.trim(inputObj.val());
		if (!inputVal) {
			if (inputNm == "Group") {
				inputNm = $("input[name='pageTitle']:checked").val();
				if (inputNm == "Text") {
					inputNm = "Group";
				} else {
					inputNm = "Title";
				}
			}
			alert("Please Enter " + inputNm + " Name");
			return false;
		} else {

			var grpType;

			if (inputNm == 'Department') {

				grpType = document.getElementById("slctDept");
			} else if (inputNm == 'Type') {
				grpType = document.getElementById("slctType");
			} else if (inputNm == 'Group') {
				grpType = document.getElementById("groupName");

			}

			for (i = 0; i < grpType.length; i++) {

				if (grpType.options[i].value == inputVal) {
					isExist = true;
					grpType.options[i].selected = true;
					if (inputNm == "Group") {
						inputNm = $("input[name='pageTitle']:checked").val();
						if (inputNm == "Text") {
							inputNm = "Group";
						} else {
							inputNm = "Title";
						}
					}
					alert(inputNm + " Name Already Exist")
					break;
				}
			}

			if (isExist == false) {
				$(obj).parents('tr').prev('tr').find(".dynmOptn option:last")
						.after(
								"<option selected='selected'>" + inputVal
										+ "</option>");
				inputObj[0].value = '';
				$(obj).parents('tr').hide();
			}

		}
		$("#groupName option:selected").trigger('change.trgrFile');
	}

	(function($) {
		$.fn.hasScrollBar = function() {
			return this.get(0).scrollHeight > this.height();
		}
	})(jQuery);
</script>
<script type="text/javascript">
	var menuLevel = document.screenSettingsForm.menuLevel.value;
	if (menuLevel == 1) {
		configureMenu("setupmainmenu");
	} else {

		configureMenu("setupsubmenu");
	}
</script>