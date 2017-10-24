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

<link rel="stylesheet" type="text/css"
	href="/ScanSeeWeb/styles/bubble-tooltip.css" media="screen" />

<script type="text/javascript" src="/HubCiti/scripts/bubble-tooltip.js"></script>
<link rel="stylesheet" href="/HubCiti/styles/jquery-ui.css" />

<script src="/HubCiti/scripts/jquery-ui.js"></script>
<script src="/HubCiti/scripts/global.js"></script>
<script>
	$(function() {
		$(".sortable").sortable({
			items : "li:not(.dragexp)"
		});
		$(".sortable").sortable({
			'opacity' : 0.6,
			helper : 'clone'
		});
		$(".sortable").disableSelection();
	});
</script>

<div id="wrpr">
	<div class="clear"></div>
	<div class="wrpr-cont relative">
		<div id="slideBtn">
			<a href="#" onclick="revealPanel(this);" title="Hide Menu"> <img
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
				<li class="last" id="menutitle-bread-crumb">Main
					Menu[${sessionScope.twocoltemp} ]</li>
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
								<c:if
									test="${sessionScope.twocoltemp eq 'Two Column Tab with Banner Ad'}">
									<tr id="fileUpld">
										<td width="36%"><label class="mand">Banner Image</label>
										</td>
										<td width="64%"><form:errors cssClass="errorDsply"
												path="logoImageName"></form:errors> <label> <img
												id="twoColTmptImg" width="150" height="50" alt="upload"
												src="${sessionScope.menuIconPreview}"> </label> <span
											class="topPadding cmnRow"> <label for="trgrUpld">
													<input type="button" value="Upload" id="trgrUpldBtn"
													class="btn trgrUpld" title="Upload Image File" tabindex="1">
													<form:hidden path="logoImageName" id="logoImageName" /> <span
													class="instTxt nonBlk"></span> <form:input type="file"
														class="textboxBig" id="trgrUpld" path="logoImage" /> </label> </span><label
											id="maxSizeImageError" class="errorDsply"></label>
										</td>
									</tr>
								</c:if>
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
								class="cmnTbl" id="tabDetails">

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


							<form:errors cssStyle="color:red"></form:errors>
							<form:hidden path="viewName" value="twocoltabview" />
							<form:hidden path="addDeleteBtn" />
							<form:hidden path="menuId" />
							<form:hidden path="menuLevel" />
							<form:hidden path="menuIconId" id="menuIconId" />
							<form:hidden path="hiddenmenuFnctn" />
							<form:hidden path="oldImageName" />
							<form:hidden path="hiddenBtnLinkId" />
							<form:hidden path="ismenuFilterTypeSelected" />
							<form:hidden path="editFilter" />
							<form:hidden path="hiddenFindCategory" />
							<form:hidden path="btnPosition" />
							<form:hidden path="subCatIds" id="subCatIds" />
							<form:hidden path="hiddenSubCate" />
							<form:hidden path="chkSubCate" id="chkSubCate" />
							<form:hidden path="userType" value="${sessionScope.loginUserType}"/>
							<form:hidden path="hiddenCitiId"/>
							<form:hidden path="hiddenFundEvtId" />

							<input type="hidden" id="tmpltOptn" value="gridView" />
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="cmnTbl">


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
												id="saveGrp" onclick="appendGrp(this);" /> <input
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
												class="lblcnt btnType"> </span></li>
											<li><input type="button" value="Save" class="btn-blue"
												id="saveGrp" onclick="appendGrp(this);" /> <input
												type="button" value="Cancel" class="btn-red delGrp" />
											</li>
										</ul>
									</td>
								</tr>
								<c:if
									test="${sessionScope.twocoltemp eq 'Single/Two Column Tab'}">
									<tr id="btnView">
										<td width="36%"><label class="mand">Button View</label>
										</td>
										<td width="64%" class=""><form:input type="radio"
												checked="checked" path="btnView" value="singleCol"
												name="btnView" id="singleCol" /> <label>Single
												Column</label> <form:input type="radio" value="twoCol"
												name="btnView" id="twoCol" path="btnView" /> <label>Two
												Column</label>
										</td>
									</tr>

								</c:if>

								<tr>
									<td width="36%"><label class="mand">Button Name</label>
									</td>
									<td width="64%" colspan="2"><form:errors
											cssClass="errorDsply" path="menuBtnName"></form:errors>
										<div class="cntrl-grp">

											<form:input path="menuBtnName" type="text"
												class="inputTxtBig vldt" id="btnName" 
												onkeypress="return specialCharCheck(event)" />
										</div> <span class="lbl">Chars Left:</span><span
										class="lblcnt btnName"></span>
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
											<ul class="infoList cmnList menuLst cityPnl" id="Cities">
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
									<td class="multi-inputs"><input type="button"
										name="button" value="Add Button" class="btn-blue" id="addBtn"
										onclick="createTwoColDeleteMenuItem('Addbutton');" /> <input
										type="button" name="delBtn" value="Delete Button"
										class="btn-red" id="delBtn"
										onclick="createTwoColDeleteMenuItem('DeleteButton');" />
									</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td colspan="2">&nbsp;</td>
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
								onclick="saveTwoColTemplate()" />
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
								<div class="previewAreaScroll actualView fixscrl">

									<c:if
										test="${sessionScope.twocoltemp eq 'Two Column Tab with Banner Ad'}">
										<div id="bnrImg">
											<img id="imgView" src="${sessionScope.bannerimage}"
												title="Banner Ad" width="320" height="50" />
										</div>
									</c:if>
									<div id="preview_ie" title="Banner Ad"></div>

									<!-- Single/Two column template implementation -->


									<c:if
										test="${sessionScope.twocoltemp eq 'Single/Two Column Tab'}">
										<c:choose>
											<c:when test="${sessionScope.colBtnView eq 'singleCol'}">
												<ul id="dynTab"
													class="twinTabBnr cstmBtn sortable singleCol">
											</c:when>


											<c:otherwise>
												<ul id="dynTab" class="twinTabBnr cstmBtn sortable twoCol">
											</c:otherwise>

										</c:choose>

									</c:if>
									<c:if
										test="${sessionScope.twocoltemp eq 'Two Column Tab with Banner Ad'}">
										<ul id="dynTab" class="twinTabBnr cstmBtn sortable">
									</c:if>

									<c:forEach items="${sessionScope.previewMenuItems}" var="item">
										<c:choose>

											<c:when
												test="${item.menuFucntionality eq sessionScope.expFctnId}">




												<li class='tabs dragexp'><a
													onclick="editTwoColmTab(this,'twotabtemplate')"
													class="experience"
													datactn="<c:out value='${item.menuFucntionality }'></c:out>"
													iconid="${item.menuIconId }"
													iconimgname="${sessionScope.bannerimage}"
													bannerImgName="${sessionScope.bannername}"
													linkId='${item.btnLinkId }' href="javascript:void(0)"
													btnDept="${item.btnDept }" btnType="${item.btnType }" citiId="${item.citiId}">


														<span><c:out value='${item.menuBtnName }'></c:out>
													</span> </a>
												</li>
											</c:when>
											<c:otherwise>



												<li class='tabs'><a
													onclick="editTwoColmTab(this,'twotabtemplate')"
													datactn="<c:out value='${item.menuFucntionality }'></c:out>"
													iconid="${item.menuIconId }"
													iconimgname="${sessionScope.bannerimage}"
													bannerImgName="${sessionScope.bannername}"
													linkId='${item.btnLinkId }' href="javascript:void(0)"
													btnDept="${item.btnDept }" btnType="${item.btnType}"
													subCat="${item.chkSubCate}" citiId="${item.citiId}"> <span><c:out
																value='${item.menuBtnName }'></c:out> </span> </a>
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

<script type="text/javascript">	dynHt = 0;
	$(document)
			.ready(
					function() {
					
						//Below code for find -Subcategory implmenation 
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

						var vMenuType = '${sessionScope.twocoltemp}';
						/**dynHt :for calucating height of UL in  IE 8 ($("#dynTab").height() does not work.**/
						var dynHt = $("#dynTab")[0].scrollHeight;

						if (dynHt > 344 || dynHt == 344) {

							$(".actualView").removeClass("fixscrl");
						} else {

							$(".actualView").addClass("fixscrl");

						}

						if (vMenuType == 'Single/Two Column Tab') {
							if (dynHt <= 344) {

								$(".actualView").addClass("fixscrl");
							}
						}
						if (vMenuType == 'Single/Two Column Tab') {

							if ($('.actualView').hasScrollBar()) {

								$(".twinTab li.tabs a,.twinTabBnr li.tabs a")
										.css("width", "150px");
								$(
										".twinTab li.tabs a.experience,.twinTabBnr li.tabs a.experience")
										.css("width", "301px");
							} else {

								$(".twinTab li.tabs a,.twinTabBnr li.tabs a")
										.css("width", "159px");
								$(
										".twinTab li.tabs a.experience,.twinTabBnr li.tabs a.experience")
										.css("width", "320px");
							}
							var iBtnView = '${sessionScope.colBtnView}';

							if (iBtnView == "singleCol") {

								$('input:radio[id=singleCol]').attr('checked',
										true);
								$('input:radio[id=twoCol]').attr('checked',
										false);
							} else if (iBtnView == "twoCol") {

								$('input:radio[id=twoCol]').attr('checked',
										true);
								$('input:radio[id=singleCol]').attr('checked',
										false);
							}
						}

						/**Single Column template changes :changing preview of button depending upon button view. **/
						$("input[name='btnView']")
								.on(
										'change',
										function() {

											var prntLst = $("#dynTab");

											var curView = $(
													"input[name='btnView']:checked")
													.attr("value");

											if (curView == "singleCol") {

												/*add txt-trnct class to span if it is single column template*/
												$(prntLst).find('.tabs .trunc')
														.removeClass(
																"txt-trnct");
												$(prntLst)
														.removeClass("twoCol")
														.addClass(curView);

												if (dynHt > 344) {
													$(".actualView")
															.removeClass(
																	"fixscrl");
												} else {
													$(".actualView").addClass(
															"fixscrl");
												}
												if ($('.actualView')
														.hasScrollBar()) {

													$(
															".twinTab li.tabs a,.twinTabBnr li.tabs a")
															.css("width",
																	"150px");
													$(
															".twinTab li.tabs a.experience,.twinTabBnr li.tabs a.experience")
															.css("width",
																	"301px");
												} else {

													$(
															".twinTab li.tabs a,.twinTabBnr li.tabs a")
															.css("width",
																	"159px");
													$(
															".twinTab li.tabs a.experience,.twinTabBnr li.tabs a.experience")
															.css("width",
																	"320px");
												}
											} else {
												$(prntLst).removeClass(
														"singleCol").addClass(
														"twoCol");
												$(prntLst).find('.tabs .trunc')
														.addClass("txt-trnct");

												if (dynHt > 344) {
													$(".actualView")
															.removeClass(
																	"fixscrl");
												} else {
													$(".actualView").addClass(
															"fixscrl");
												}
												if ($('.actualView')
														.hasScrollBar()) {

													$(
															".twinTab li.tabs a,.twinTabBnr li.tabs a")
															.css("width",
																	"150px");
													$(
															".twinTab li.tabs a.experience,.twinTabBnr li.tabs a.experience")
															.css("width",
																	"301px");
												} else {

													$(
															".twinTab li.tabs a,.twinTabBnr li.tabs a")
															.css("width",
																	"159px");
													$(
															".twinTab li.tabs a.experience,.twinTabBnr li.tabs a.experience")
															.css("width",
																	"320px");

												}
											}

										});

						if (vMenuType != 'Single/Two Column Tab') {

							if ($('.actualView').hasScrollBar()) {

								$(".twinTab li.tabs a,.twinTabBnr li.tabs a")
										.css("width", "150px");
								$(
										".twinTab li.tabs a.experience,.twinTabBnr li.tabs a.experience")
										.css("width", "301px");
								if (vMenuType != 'Single/Two Column Tab') {

									$(".previewAreaScroll").css("width",
											"319px");
									$("#iphone-preview").css("width", "319px");
								}

							} else {

								$(".twinTab li.tabs a,.twinTabBnr li.tabs a")
										.css("width", "159px");
								$(
										".twinTab li.tabs a.experience,.twinTabBnr li.tabs a.experience")
										.css("width", "320px");
								$(".previewAreaScroll").css("width", "320px");
								$("#iphone-preview").css("width", "320px");
							}

						}
						/* Displaying button length */

						$("#btnType").limiter(20, $(".btnType"));
						$("#btnDept").limiter(20, $(".btnDept"));
						if (vMenuType != 'Single/Two Column Tab') {
							$("#btnName").limiter(16, $(".btnName"));
						} else {
							$("#btnName").limiter(35, $(".btnName"));
						}
						$("#subMenuInput").limiter(35, $(".subMenuInput"));

						var licnt = $("#dynTab li").length;
						if (licnt) {
							$(".prgrsStp li:first").addClass("step1Actv");
						}
						if ($("#dynTab li").length > 1) {
							$("#dynTab li a.experience").parents('li')
									.insertBefore("#dynTab li.tabs:first");
							$("#dynTab li a.experience").parents('ul')
									.addClass("expBtn");
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
								
							jQuery.each(arr, function(i, val) {
							if(jQuery(val).index("MC") === -1)
								{
							
								val = val.substring(0, val.lastIndexOf("-"));
									
								$('#FN-' + val).prop('checked', 'checked');
									$('#FN-' + val).parent().next('.sub-ctgry').show();
								}
						
							});
							//for subcategories 
							
							if(hiddenSubCate != null)
									{
									hiddenSubCate=hiddenSubCate.replace(/NULL!~~!/gi,"");
									hiddenSubCate=hiddenSubCate.replace(/!~~!NULL/gi,"");
									var arr2 = hiddenSubCate.split(',');
										jQuery.each(arr2, function(i, val) {
										$('#FNS-' + val).prop('checked', 'checked');
									});
									}
							$(".intput-actn").show();
							$(".cmnList").hide();
							$("#" + selFuncVal).show();
							/*var busCatList = $("#hiddenFindCategory").val()
									.split(",");
							
							for ( var k = 0; k < busCatList.length; k++) {
							if(busCatList[k].contains('MC')){
												
								busCatList[k] = busCatList[k].substring(0, busCatList[k].lastIndexOf("-"));
										
								$("#FN-" + busCatList[k]).prop('checked', true);
								$('#FN-' +  busCatList[k]).parent().next('.sub-ctgry').show();
								}
							}		*/		

							var tolCnt = $('#Find input[name$="btnLinkId"]:checkbox:visible').length;
							var chkCnt = $('#Find input[name$="btnLinkId"]:checkbox:checked:visible').length;
							if (tolCnt == chkCnt) {
								$('#findchkAll').prop('checked', 'checked');
							}
							else {
								$('#findchkAll').removeAttr('checked');
							}
						} else if (selFuncVal == "Events") {

							//$('#Events input[name="btnLinkId"]').prop(
							//		'checked', false);
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
						} else {
							$('#citychkAll').removeAttr('checked');
							$(".cityPnl").hide();
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

					});

function createTwoColDeleteMenuItem(val) {

	//Below code for find -Subcategory implmenation 		
	var checkedSubCat = $.makeArray($('ul.sub-ctgry').find(
	'input[name="btnLinkId"]:checkbox:checked:visible')

	.map(function() {
		return $(this).val()
	}));
	
	$('#chkSubCate').val(checkedSubCat);
	
	var checkedMainCat = $.makeArray($('.main-ctrgy:checked')
			.map(function() {
				return $(this).val()
	}));		
	
	var subArr = [];
	$.each(checkedMainCat, function(i,val){
		var arr = $.makeArray($('input[name="btnLinkId"][value=' + val + ']').parent('span').next('ul.sub-ctgry').find(
					'input[name="btnLinkId"]:checkbox:checked:visible')
			.map(function() {
				return $(this).val()
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
		subCat = subCat.replace(/,!~~!/gi, "");
	}
	
	$('#subCatIds').val(subCat); 		
		/*var checkedSubCat  = $.makeArray($('ul.sub-ctgry').find('input[name="btnLinkId"]:checkbox:checked').map(function() { return $(this).val() }));
				  //NULL!~~!86,87,88,89,90,91!~~!NULL!~~!111,112,113,114,115,!~~!
				  
			
		     var arr = [];
			
		var subLen = $('.sub-ctgry').length;
		var bndry = $('.main-ctrgy:checked').parent().next('ul.sub-ctgry');
		var prntEle = $(bndry).length;
		var vCatId = null;
		for(i=0;i<prntEle;i++){
		  $(bndry).find('li').each(function(){
		  
		 vCatId = $(this).find('input[name="btnLinkId"]:checkbox:checked').val();
								
		/*if(vCatId == null || vCatId == "undefined" ||vCatId == "")
		 {  arr[i++]= "NULL";  }else{*/	
		
			 /*arr[i++] = vCatId;
		  if(($(this).is(":last-child"))){
		    arr[i++] = "!~~!";
		  }
		 
		  });
		 
		}
		var subCat = arr.toString();
		if(null != subCat ){
		subCat =subCat.replace(/,!~~!,/gi,"!~~!");
		if(subCat.endsWith(",!~~!")){
		subCat = subCat.replace(',!~~!',"")}
		}
		$('#subCatIds').val(subCat);	
		$('#chkSubCate').val(checkedSubCat);*/
		
	
		var btnOrderList = "";
		if (!($("#grpDept").is(':checked'))) {

			$("#slctDept option[value='0']").prop("selected", "selected");
		}
		if (!($("#grpType").is(':checked'))) {

			$("#slctType option[value='0']").prop("selected", "selected");
		}

		$(".prgrsStp li:first").addClass("step1Actv");
		$("#dynTab li").find("a").each(function() {
			var btnId = $(this).attr("iconid");
			if (btnOrderList == "") {
				btnOrderList = btnId
			} else {
				btnOrderList = btnOrderList + "~" + btnId;
			}

		});
		document.screenSettingsForm.btnPosition.value = btnOrderList;

		var menf = document.screenSettingsForm.menuFucntionality.value;
		document.screenSettingsForm.addDeleteBtn.value = val;
		document.screenSettingsForm.action = "twocoltabview.htm";
		document.screenSettingsForm.method = "POST";
		document.screenSettingsForm.submit();
		var dynHt = $("#dynTab")[0].scrollHeight;

		if (dynHt <= 344) {

			$(".actualView").addClass("fixscrl");
		}

	}

	function saveTwoColTemplate() {
	
	
		$.ajaxSetup({
			cache : false
		});
		var licnt = $("#dynTab li").length;
		var bottmBtnId = "";
		var btnOrderList = "";
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

				$("#dynTab li").find("a").each(function() {
					var btnId = $(this).attr("iconid");
					if (btnOrderList == "") {
						btnOrderList = btnId
					} else {
						btnOrderList = btnOrderList + "~" + btnId;
					}

				});
				var menuType = '${sessionScope.twocoltemp}';

				var bannerImg;
				var menuId = document.screenSettingsForm.menuId.value;
				var menuLevel = document.screenSettingsForm.menuLevel.value;
				var subMenuName = document.screenSettingsForm.subMenuName.value;
				if (menuType == 'Two Column Tab with Banner Ad') {

					bannerImg = document.screenSettingsForm.logoImageName.value;
				} else {

					bannerImg = "";
				}
				//Adding code for single colum template changes.
				var vBtnView;
				if (menuType == 'Single/Two Column Tab') {
					vBtnView = $("input[name='btnView']:checked").attr("value");

				} else {

					vBtnView = "";
				}

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

				$.ajaxSetup({
					cache : false
				});

				$
						.ajax({
							type : "POST",
							url : "savetwotabtemplate.htm",
							data : {
								'menuId' : menuId,
								'menuLevel' : menuLevel,
								'bannerImg' : bannerImg,
								'subMenuName' : subMenuName,
								'bottmBtnId' : bottmBtnId,
								'template' : menuType,
								'typeFilter' : typeFlag,
								'deptFilter' : deptFlag,
								'btnOrder' : btnOrderList,
								'btnView' : vBtnView
								
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
		var menuType = '${sessionScope.twocoltemp}';
		if (menuLevel == 1) {
			$("#subMenuCont").addClass('btnNm');
			$("#grpingCont").addClass('btnNm');
			$("#grpingContTitle").addClass('btnNm');

			$("#grpingContTitle").addClass('btnNm');
			$("#slctTmpt").attr('href',
					"displaymenutemplate.htm?menuType=mainmenu");

			var mainMenuBGType = '${sessionScope.mainMenuBGType}';
			var mainMenuBG = '${sessionScope.mainMenuBG}'
			var mainMenuBtnClr = '${sessionScope.mainMenuBtnClr}'
			var mainMenuFntClr = '${sessionScope.mainMenuFntClr}'

			if (mainMenuBGType == 'Image') {
				$("#iphone-priPolicy-preview").css("background",
						"url('" + mainMenuBG + "') no-repeat scroll 0 0");

			} else if (mainMenuBGType == 'Color') {
				$("#iphone-priPolicy-preview").css("background", mainMenuBG);

			}
			$(".twinTab li.tabs a, .twinTabBnr li.tabs a").css("color",
					mainMenuFntClr)
			$(".twinTab li.tabs a, .twinTabBnr li.tabs a").css("background",
					mainMenuBtnClr)

			if (menuType != 'Two Column Tab with Banner Ad') {
				$("#menutitleDiv").addClass('btnNm');
				$("#subMenuDetails").addClass('btnNm');

			}

		} else {
			var subMenuId = document.screenSettingsForm.menuId.value;
			var subMenuName = $("#subMenuInput").val()
			$("td.genTitle").text(subMenuName);
			$("#slctTmpt").attr(
					'href',
					"displaymenutemplate.htm?menuType=submenu&id=" + subMenuId
							+ "&menuname=" + subMenuName);
			$("#menutitle").text("Sub Menu");

			if (menuType == 'Two Column Tab with Banner Ad') {

				$("#menutitle-bread-crumb").text(
						"Sub Menu [ Two Column Tab with Banner Ad ]")
			} else {

				$("#menutitle-bread-crumb").text(
						"Sub Menu [ Single/Two Column Tab Template]")
			}

			/* Code for displaying menu background */
			var subMenuBGType = '${sessionScope.subMenuBGType}'
			var subMenuBG = '${sessionScope.subMenuBG}'
			var subMenuBtnClr = '${sessionScope.subMenuBtnClr}'
			var subMenuFntClr = '${sessionScope.subMenuFntClr}'

			if (subMenuBGType == 'Image') {
				$("#iphone-priPolicy-preview").css("background",
						"url('" + subMenuBG + "') no-repeat scroll 0 0");

			} else if (subMenuBGType == 'Color') {
				$("#iphone-priPolicy-preview").css("background", subMenuBG);

			}
			$(".twinTab li.tabs a, .twinTabBnr li.tabs a").css("color",
					subMenuFntClr)
			$(".twinTab li.tabs a, .twinTabBnr li.tabs a").css(
					"background-color", subMenuBtnClr)

			var ismenuFilterTypeSelected = document.screenSettingsForm.ismenuFilterTypeSelected.value;

			if (ismenuFilterTypeSelected == 'true') {

				//$('#grpDept').addClass("grpOpct");
				//$('#grpType').addClass("grpOpct");

				$('#grpType,#grpDept').prop('disabled', true);

				$('#grpDept')
						.click(
								function(e) {

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

	function updateBtnLink() {
		$('input[name="btnLinkId"]').prop('checked', false);
	}

	(function($) {
		$.fn.hasScrollBar = function() {
			return this.get(0).scrollHeight > this.height();
		}
	})(jQuery);
--></script>
<script type="text/javascript">
	var menuLevel = document.screenSettingsForm.menuLevel.value;
	if (menuLevel == 1) {
		configureMenu("setupmainmenu");
	} else {

		configureMenu("setupsubmenu");
	}
</script>