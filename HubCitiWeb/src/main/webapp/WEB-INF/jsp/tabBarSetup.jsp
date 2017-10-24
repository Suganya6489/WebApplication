<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<script type="text/javascript" src="scripts/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/jquery.form.js"></script>
<link rel="stylesheet" href="/HubCiti/styles/jquery-ui.css" />
<script src="/HubCiti/scripts/jquery-ui.js"></script>
<script src="/HubCiti/scripts/jquery-1.8.3.js"></script>
<script src="/HubCiti/scripts/global.js"></script>
<script type="text/javascript">
	window.onload = function() {
		var vShare = document.screenSettingsForm.functionalityType.value;
		if (vShare == "Share") {
			var tglRow = $('tr.shareInfo');
			document.screenSettingsForm.menuFucntionality.value = vShare;
			$("#dataFnctn option[typeval='" + vShare + "']").attr("selected",
					"selected");
			$(tglRow).show();
		}
	}

	$(document)
			.ready(
					function() {
					
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
						var setNewHt = $("#menu-pnl").height();
						$(".cont-block ").height(setNewHt - 28);
						onLoadslctFnctn();
						var selFuncVal = $("#dataFnctn option:selected").attr('typeVal');
						$("#dataFnctn option:selected").trigger('change');
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
							var busCatList = $("#hiddenFindCategory").val()
									.split(",");
							
							for ( var k = 0; k < busCatList.length; k++) {
							if(busCatList[k].contains('MC')){
												
								busCatList[k] = busCatList[k].substring(0, busCatList[k].lastIndexOf("-"));
										
								$("#FN-" + busCatList[k]).prop('checked', true);
								$('#FN-' +  busCatList[k]).parent().next('.sub-ctgry').show();
								}
							}				

							var tolCnt = $('#Find input[name$="btnLinkId"]:checkbox:visible').length;
							var chkCnt = $('#Find input[name$="btnLinkId"]:checkbox:checked:visible').length;
							if (tolCnt == chkCnt) {
								$('#findchkAll').prop('checked', 'checked');
							}
							else {
								$('#findchkAll').removeAttr('checked');
							}
						
						
						
						/*	$('input[name="btnLinkId"]').prop('checked', false);
							var arr = hiddenBtnLinkId.split(',');

							jQuery.each(arr, function(i, val) {
								$('#FN-' + val).prop('checked', 'checked');
							});
							var busCatList = $("#hiddenFindCategory").val()
									.split(",");

							for (var k = 0; k < busCatList.length; k++) {

								$("#FN-" + busCatList[k]).prop('checked', true);
							}
							$(".intput-actn").show();
							var tolCnt = $("input[type=checkbox][name='btnLinkId']").length;
							var chkCnt = $('input[name$="btnLinkId"]:checked').length;
							if (tolCnt == chkCnt) {
								$('#findchkAll').prop('checked', 'checked');
							} else {
								$('#findchkAll').removeAttr('checked');
							}
							$(".cmnList").hide();
							$("#" + selFuncVal).show();*/
						} else if (selFuncVal == "Events") {
							$('#Events input[name="btnLinkId"]').prop('checked', false);
							var arr = hiddenBtnLinkId.split(',');

							jQuery.each(arr, function(i, val) {
								$('#EVT-' + val).prop('checked', 'checked');
							});
							var busCatList = $("#hiddenFindCategory").val().split(",");

							for ( var k = 0; k < busCatList.length; k++) {
								$("#EVT-" + busCatList[k]).prop('checked', true);
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

						//for fundraiser
						else if (selFuncVal == "Fundraisers") {
							$('#Fundraisers input[name="btnLinkId"]').prop('checked', false);
							var arr = hiddenBtnLinkId.split(',');

							jQuery.each(arr, function(i, val) {
								$('#FUNDEVT-' + val).prop('checked', 'checked');
							});
							var busCatList = $("#hiddenFindCategory").val().split(",");

							for ( var k = 0; k < busCatList.length; k++) {
								$("#FUNDEVT-" + busCatList[k]).prop('checked', true);
							}
							$(".input-actn-fundraiser").show();
							var tolCnt = $("#Fundraisers input[type=checkbox][name='btnLinkId']").length;
							var chkCnt = $('#Fundraisers input[name$="btnLinkId"]:checked').length;
							if (tolCnt == chkCnt) {
								$('#fundraChkAll').prop('checked', 'checked');
							}
							else {
								$('#fundraChkAll').removeAttr('checked');
							}
							$(".cmnList").hide();
							$("#" + selFuncVal).show();
						}
						
						
						
						
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

						$("#dataFnctn").change(function() {											
											$('#findchkAll').removeAttr('checked');
											$('input[name="btnLinkId"]').removeAttr('checked');

											var selFuncVal = $("#dataFnctn option:selected").attr('typeVal');
											document.screenSettingsForm.functionalityType.value = selFuncVal;
											//$("#dataFnctn option:selected").trigger('change');
										
											var tglRow = $('tr.shareInfo');
											$(".cmnList").hide();
											$("#" + selFuncVal).show();
											if (selFuncVal == "Share") {
												$(tglRow).show();
												$("#menu-pnl").height(
														$(".content").height());
											} else {
												$(tglRow).hide();
												if (document
														.getElementById('playStoreLnk.errors') != null) {
													document
															.getElementById('playStoreLnk.errors').style.display = 'none';
												}
												if (document
														.getElementById('iTunesLnk.errors') != null) {
													document
															.getElementById('iTunesLnk.errors').style.display = 'none';
												}
												document
														.getElementById('iTunesLnk').value = "";
												document
														.getElementById('playStoreLnk').value = "";
											}
											if (selFuncVal === "Find") {
												$(".input-actn").show();
											} else {
												$(".input-actn").hide();
											}
											if (selFuncVal === "Events") {
												$(".input-actn-evnt").show();
											} else {
												$(".input-actn-evnt").hide();
											}
											if (selFuncVal === "Fundraisers") {
												$(".input-actn-fundraiser").show();
											} else {
												$(".input-actn-fundraiser").hide();
											}
											
											
										});

						/*var
						btmSlctn = $("#dataFnctn option:selected");
						$(btmSlctn).trigger('change');
						 */

						var menuIconId = document.screenSettingsForm.menuIconId.value
						if (null !== menuIconId && menuIconId != "") {
							$("#addBtn").attr("value", "Save Tab");
							$("#tab_del").show();
						}

						var iconSelection = $("#iconSelection").val();
						var iconid = $("#iconId").val();

						$(".cmnUpld").hide();
						$(".upldOwn").show();
						if (iconSelection == "" || iconSelection == "upldOwn") {
							$("#" + iconSelection).attr("checked", "true")
							$(".cmnUpld").hide();
							$(".upldOwn").show();
							$("#iconSelection").val('upldOwn');
						} else if (iconSelection == "exstngIcon") {
							$("#iconSelection").val('exstngIcon');
							$("#" + iconSelection).attr("checked", "true");
							$("#upldOwn").removeAttr();
							$(".cmnUpld").hide();
							$(".exstngIcon").show();

							var btnType = '${sessionScope.bottomBtnType}'

							if (btnType == "Default") {
								$("#" + iconid).parents('li')
										.addClass("active");
							} else {
								$("#" + iconid).attr("checked", true);
							}
						}

						$("input[name='icnSlctn']:radio").change(function() {
							var slctOptn = $(this).attr("id");
							$("#iconSelection").val(slctOptn);
							$(".cmnUpld").hide();
							$("." + slctOptn).slideDown();
						});
					});

	function creatDeleteTab(val) {
	
	
	var checkedSubCat = $.makeArray($('ul.sub-ctgry').find(
	'input[name="btnLinkId"]:checkbox:checked:visible').map(function() {
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

	var btnType = '${sessionScope.bottomBtnType}'

		if (btnType == "Default") {
			$(".prgrsStp li:first").addClass("step1Actv");
		}
		
		document.screenSettingsForm.subCatIds.value = subCat ;
		document.screenSettingsForm.addDeleteBtn.value = val;
		document.screenSettingsForm.action = "addtabbarbutn.htm";
		document.screenSettingsForm.method = "POST";
		document.screenSettingsForm.submit();
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
	function selectIcon(obj) {
		var iconId = $(obj).attr("iconId");
		var imgSrc = $(obj).attr("src");
		$("#iconId").val(iconId);
		$("#imagePath").val(imgSrc);
	}
</script>

<div class="clear"></div>
<div class="wrpr-cont relative">
	<div id="slideBtn">
		<a href="#" onclick="revealPanel(this);" title="Hide Menu"><img
			src="images/slide_off.png" width="11" height="28" alt="btn_off" /> </a>
	</div>
	<div id="bread-crumb">
		<ul>
			<li class="scrn-icon"><span class="icon-home">&nbsp;</span></li>
			<li><a href="welcome.htm">Home</a></li>
			<li class="last">Setup Tab Bar</li>
		</ul>
	</div>
	<span class="blue-brdr"></span>
	<div class="content" id="login">
		<div id="menu-pnl" class="split">
			<jsp:include page="leftNavigation.jsp"></jsp:include>

		</div>
		<div class="cont-pnl split">
			<div class="stretch">
				<div class="title-bar">
					<ul class="title-actn">
						<li class="title-icon"><span class="icon-tabbar">&nbsp;</span>
						</li>
						<li>Setup Tab Bar Controls</li>
					</ul>
				</div>
				<div class="tabd-pnl">
					<ul class="nav-tabs">
						<li><a class="active" href="#">General Tab Bar</a></li>
						<li><a href="setupmoduletabbars.htm" class="rt-brdr">Module
								Tab Bar</a></li>
					</ul>
					<div class="clear"></div>
				</div>
				<div class="">
					<div class="cont-block rt-brdr">
						<div class="cont-wrp">
							<form:form name="screenSettingsForm" id="screenSettingsForm"
								commandName="screenSettingsForm" enctype="multipart/form-data"
								action="uploadimg.htm">

								<form:errors cssStyle="color:red"></form:errors>
								<form:hidden path="viewName" value="tabBarSetup" />
								<form:hidden path="addDeleteBtn" />
								<form:hidden path="hiddenmenuFnctn" />
								<form:hidden path="iconId" id="iconId" />
								<form:hidden path="iconSelection" id="iconSelection" />
								<form:hidden path="imagePath" id="imagePath" />
								<form:hidden path="menuIconId" id="menuIconId" />
								<form:hidden path="bottomBtnId" id="bottomBtnId" />
								<form:hidden path="functionalityType" id="functionalityType" />
								<form:hidden path="hiddenBtnLinkId" />
								<form:hidden path="hiddenFindCategory" />
								<form:hidden path="uploadImageType" id="uploadImageType"
									value="logoImage" />
								<form:hidden path="pageType"
									value="${sessionScope.bottomBtnType}" />
									<form:hidden path="subCatIds" id="subCatIds"/>
									<form:hidden path="chkSubCate" id="chkSubCate" />
									<form:hidden path="hiddenSubCate" />
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="cmnTbl">
									<tr>
										<td width="40%" rowspan="2" align="left" valign="top"><label
											class="mand">Icon Selection</label></td>
										<td width="60%"><input type="radio" name="icnSlctn"
											id="upldOwn" value="radio" checked="checked" /> <label
											for="upldOwn">New</label></td>
									</tr>
									<tr>
										<td colspan="2"><input type="radio" name="icnSlctn"
											id="exstngIcon" value="radio" /> <label for="exstngIcon">Existing
										</label></td>
									</tr>
									<tr class="grey-bg2">
										<td colspan="3"><c:choose>
												<c:when test="${sessionScope.bottomBtnType eq 'Default'}">
													<ul class="iconsPnl cmnUpld exstngIcon" id="tabBarLst">
														<c:forEach items="${sessionScope.existingIconsList }"
															var="item">
															<li><a href="#"> <img class="active"
																	title="about" id="${item.menuIconId }"
																	iconId="${item.menuIconId }" alt="about"
																	onclick="selectIcon(this);" src="${item.imagePath }">
															</a></li>
														</c:forEach>
													</ul>
												</c:when>
												<c:otherwise>
													<ul class="tab-on-off cmnUpld exstngIcon" id="tabBarLst">
														<c:forEach items="${sessionScope.existingIconsList }"
															var="item">
															<li><input type="radio" name="iconOnOff"
																value="${item.menuIconId }" id="${item.menuIconId }"
																iconId="${item.menuIconId }" src="${item.imagePath }"
																onclick="selectIcon(this);"> <!--<form:radiobutton path="iconId" value="${item.menuIconId}" />-->
																<span> <img src="${item.imagePathOff }" /> <img
																	src="${item.imagePath }" />
															</span></li>
														</c:forEach>
													</ul>
												</c:otherwise>
											</c:choose> <span class="clear"></span>
											<div class="cmnUpld upldOwn">
												<table width="100%" border="0" cellpadding="0"
													cellspacing="0" class="cmnTbl">
													<c:choose>
														<c:when
															test="${sessionScope.bottomBtnType ne null && !empty sessionScope.bottomBtnType}">
															<c:choose>
																<c:when
																	test="${sessionScope.bottomBtnType eq 'Default'}">
																	<tr id="bnrUpld">
																		<td width="40%"><label class="mand">Upload
																				Image</label></td>
																		<td width="60%"><form:errors
																				cssClass="errorDsply" path="logoImageName"></form:errors>
																			<label> <img id="logoImageId" width="80"
																				height="50" alt="upload"
																				src="${sessionScope.tabBarIconPreview}">
																		</label> <span class="topPadding cmnRow"> <label
																				for="trgrUpld"> <input type="button"
																					value="Upload" id="trgrUpldBtn"
																					class="btn trgrUpld" title="Upload Image File"
																					tabindex="1"> <form:hidden
																						path="logoImageName" id="logoImageName" /> <span
																					class="instTxt nonBlk"></span> <form:input
																						type="file" class="textboxBig" id="trgrUpld"
																						path="logoImage" />
																			</label>
																		</span><label id="maxSizeImageError" class="errorDsply"></label></td>
																	</tr>
																</c:when>
																<c:otherwise>
																	<tr id="bnrUpld">
																		<td width="40%"><label class="mand">Upload
																				Image On</label></td>
																		<td width="60%"><form:errors
																				cssClass="errorDsply" path="logoImageName"></form:errors>
																			<label> <img id="logoImageId" width="80"
																				height="50" alt="upload"
																				src="${sessionScope.tabBarIconPreview }">
																		</label> <span class="topPadding cmnRow"> <label
																				for="trgrUpld"> <input type="button"
																					value="Upload" id="trgrUpldBtn"
																					class="btn trgrUpld" title="Upload Image File"
																					tabindex="1"> <form:hidden
																						path="logoImageName" id="logoImageName" /> <span
																					class="instTxt nonBlk"></span> <form:input
																						type="file" class="textboxBig" id="trgrUpld"
																						path="logoImage" />
																			</label>
																		</span><label id="maxSizeImageError" class="errorDsply"></label></td>
																	</tr>
																	<tr id="bnrUpld">
																		<td width="40%"><label class="mand">Upload
																				Image Off</label></td>
																		<td width="60%"><form:errors
																				cssClass="errorDsply" path="bannerImageName"></form:errors>
																			<label> <img id="imageFileId" width="80"
																				height="50" alt="upload"
																				src="${sessionScope.imageFileTabBarIconPreview}">
																		</label> <span class="topPadding cmnRow"> <label
																				for="trgrUpldImg"> <input type="button"
																					value="Upload" id="trgrUpldBtnImg"
																					class="btn trgrUpldImg" title="Upload Image File"
																					tabindex="1"> <form:hidden
																						path="bannerImageName" id="bannerImageName" /> <span
																					class="instTxt nonBlk"></span> <form:input
																						type="file" class="textboxBig" id="trgrUpldImg"
																						path="imageFile" />
																			</label>
																		</span><label id="maxSizeOffImageError" class="errorDsply"></label></td>
																	</tr>
																</c:otherwise>
															</c:choose>
														</c:when>
														<c:otherwise>

														</c:otherwise>
													</c:choose>
												</table>
											</div></td>
									</tr>
									<tr>
										<td width="32%"><label class="mand">Functionality</label>
										</td>
										<td width="64%">
											<div class="cntrl-grp zeroBrdr">
												<form:errors cssClass="errorDsply" path="menuFucntionality"></form:errors>
												<form:select path="menuFucntionality" class="slctBx"
													id="dataFnctn">
													<option selected="selected" value="0">Select
														Functionality</option>
													<c:forEach items="${sessionScope.btmlinkList}" var="link">
														<option value="${link.menuTypeId}"
															typeVal="${link.menuTypeVal}">${link.menuTypeName}</option>
													</c:forEach>
												</form:select>
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="3"><ul class="infoList cmnList" id="AppSite">

												<c:forEach items="${sessionScope.appsitelst}" var="appsite">

													<li><span class="cell"> <form:input
																type="radio" name="AppSite" path="btnLinkId"
																id="AS-${appsite.appSiteId}"
																value="${appsite.appSiteId}" /> ${appsite.appSiteName}
													</span> <span class="cell">${appsite.retName},${appsite.address}</span>
													</li>


												</c:forEach>

												<li class="actn"><a href="javascript:void(0);"
													onclick="addAppsite(this)"><img
														src="images/btn_add.png" width="24" height="24" alt="add"
														class="addImg" /> Create New App Site</a></li>
											</ul>
											<ul class="infoList cmnList" id="AnythingPage">
												<c:forEach items="${sessionScope.anythingPageList}"
													var="item">

													<li><span class="cell zeroMrgn"> <form:input
																type="radio" value="${item.hcAnythingPageId}"
																id="AP-${item.hcAnythingPageId}" name="anythingPage"
																path="btnLinkId" /> <c:out
																value="${item.anythingPageTitle }" />
													</span> <span class="cell">${item.pageType}</span></li>

												</c:forEach>
												<li class="actn"><a href="buildanythingpage.htm"><img
														src="images/btn_add.png" width="24" height="24" alt="add"
														class="addImg" /> Add New AnyThing<sup>TM</sup> Page</a></li>
											</ul>
											<ul class="infoList cmnList menuLst" id="SubMenu">

												<c:forEach items="${sessionScope.subMenuList }" var="item">
													<li><span class="cell zeroMrgn"> <form:input
																type="radio" value="${item.menuId}" name="subMenu"
																id="SM-${item.menuId}" path="btnLinkId" /> <c:out
																value="${item.menuName }"></c:out>
													</span></li>

												</c:forEach>


												<li class="actn"><a
													href="displaymenutemplate.htm?menuType=submenu"><img
														src="images/btn_add.png" width="24" height="24" alt="add"
														class="addImg" /> Add New SubMenu</a></li>
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
															value="${item.catId}-MC" /> <label for="find1">${item.catName
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
													<!--<li><span class="cell zeroMrgn"> <form:checkbox
																path="btnLinkId" name="category" id="FN-${item.catId}"
																value="${item.catId }" /> <label for="find1">${item.catName }</label>
													</span></li> -->


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
																value="${item.catId }" /> <label for="find1">${item.catName }</label>
													</span></li>


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
																				</td>
									</tr>
									<tr class="shareInfo">
										<td colspan="2">Please provide appstore links</td>
									</tr>
									<tr class="shareInfo">
										<td><label class="mand">iTunes</label></td>
										<td><form:errors cssClass="errorDsply" path="iTunesLnk"></form:errors>
											<div class="cntrl-grp input white-bg">
												<form:input type='text' path="iTunesLnk" id="iTunesLnk"
													class="inputTxtBig" />
											</div></td>
									</tr>
									<tr class="shareInfo">
										<td><label class="mand">Play Store</label></td>
										<td><form:errors cssClass="errorDsply"
												path="playStoreLnk"></form:errors>
											<div class="cntrl-grp input white-bg">
												<form:input type='text' path="playStoreLnk"
													id="playStoreLnk" class="inputTxtBig" />
											</div></td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td><input type="button" name="button" id="addBtn"
											onclick="creatDeleteTab('CreateTab');" value="Add New Tab"
											class="btn-blue" /> <input type="button" name="delBtn"
											id="tab_del" onclick="creatDeleteTab('DeleteButton')"
											value="Delete Button" class="btn-red" /></td>
									</tr>
								</table>
							</form:form>
						</div>
					</div>
					<div class="cont-block  grey-bg">
						<div class="cont-wrp fix-scrll">
							<ul class="iconsPnl" id="savdTab">
								<c:forEach items="${sessionScope.tabBarbuttonsList}" var="item">
									<li><c:choose>
											<c:when
												test="${item.menuFucntionality eq sessionScope.tabShareFctnId}">
												<a href="#" onclick="editTabBtn(this);"
													linkId="${item.btnLinkId }" iconimgname="${item.imageName}"
													iconimgnameoff="${item.imageNameOff}"
													subCat="${item.chkSubCate}"
													datactn="${item.menuFucntionality }"
													iconid="${item.iconId}" tabbarId="${item.bottomBtnId}"
													menuIconId="${item.menuIconId }"
													itunesLnk="${item.iTunesLnk}"
													playstoreLnk="${item.playStoreLnk}" functype="Share"
													imagePathOff="${item.imagePathOff }"><img
													class="active" width="80px" height="50px" alt="image"
													src="${item.imagePath }"> </a>

											</c:when>
											<c:otherwise>
												<a href="#" onclick="editTabBtn(this);"
													linkId="${item.btnLinkId }" iconimgname="${item.imageName}"
													subCat="${item.chkSubCate}"
													iconimgnameoff="${item.imageNameOff}"
													imagePathOff="${item.imagePathOff }"
													datactn="${item.menuFucntionality }"
													iconid="${item.iconId}" tabbarId="${item.bottomBtnId}"
													menuIconId="${item.menuIconId }"><img class="active"
													width="80px" height="50px" alt="image"
													src="${item.imagePath }"> </a>
											</c:otherwise>
										</c:choose></li>
								</c:forEach>
							</ul>
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
			alt="close" onclick="javascript:closeIframePopup('ifrmPopup','ifrm')"
			title="Click here to Close" align="middle" /> <span id="popupHeader"></span>
	</div>
	<iframe frameborder="0" scrolling="no" id="ifrm" src="" height="100%"
		allowtransparency="yes" width="100%" style="background-color: White">
	</iframe>
</div>
<script type="text/javascript">
	configureMenu("setuptabbar");
</script>