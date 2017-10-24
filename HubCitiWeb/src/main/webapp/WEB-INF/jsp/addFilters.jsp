<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<%@taglib prefix="page" uri="/WEB-INF/pagination.tld"%>
<script type="text/javascript" src="scripts/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/jquery.form.js"></script>

<script type="text/javascript">
	$(document)
			.ready(
					function() {

						$(window)
								.load(
										function() {
											var tolCnt = $('input[name$="retLocIds"]:checkbox').length;

											var chkCnt = $('input[name$="retLocIds"]:checkbox:checked').length;

											if (tolCnt == chkCnt) {

												$("#fltrChkAll")
														.prop('checked',
																'checked')
														.trigger(
																'change.trgrclone');
												$("#fltrChkAll").prop(
														'checked', true);
											}
										});

						$(".hdrClone").find("span:gt(0)").css("text-indent",
								"5px");
						$(".hdrClone").find("span:last").css("text-indent",
								"10px");
						$(".hdrClone").find("span:last").find(
								"input[type='checkbox']").removeAttr("id")
								.attr("id", "cloneId");

						$(document).on(
								'change.trgrclone',
								'#fltrChkAll',
								function() {
									var curChkd = $(this).prop("checked");
									var status = $(this).prop("checked");
									var chkdvals = [];

									$("#cloneId").prop("checked", curChkd);
									$('input[name$="retLocIds"]:checkbox')
											.prop('checked', status);
									if (!status) {
										$('input[name$="retLocIds"]:checkbox')
												.removeAttr('checked');
									}
									/* get all chkbx ids */
									$("input[name='retLocIds']:checkbox").each(
											function() {
												chkdvals.push($(this).attr(
														"value"));
											});

									//deleteAllRetLocId(chkdvals);
								});
						$(document)
								.on(
										'change.trgrPrnt',
										'#cloneId',
										function() {
											var cloneChkd = $(this).prop(
													"checked");
											$("#fltrChkAll").prop("checked",
													cloneChkd);
											$(
													'input[name$="retLocIds"]:checkbox')
													.prop('checked', cloneChkd);
										});

						/*Any one of the child{chkbx) uncheck ,uncheck parent chkbx*/
						$(document)
								.on(
										'click',
										'input[name$="retLocIds"]:checkbox',
										function() {
											var tolCnt = $('input[name$="retLocIds"]:checkbox').length;
											var chkCnt = $('input[name$="retLocIds"]:checked').length;

											var curChkBx = $(this)
													.attr("value");
											if (tolCnt == chkCnt) {
												$('#fltrChkAll').prop(
														'checked', 'checked');
												$('#cloneId').prop('checked',
														'checked');
											} else {
												$('#fltrChkAll').removeAttr(
														'checked');
												$('#cloneId').removeAttr(
														'checked');
											}
										});

						$('#trgrUpldImg')
								.bind(
										'change',
										function() {
											var imageType = document
													.getElementById("trgrUpldImg").value;

											var checkedValues = $(
													'input[name="retLocIds"]:checked')
													.map(function() {
														return this.value;
													}).get();

											document.screenSettingsForm.hiddenRetailLocs.value = checkedValues;

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

					});
</script>

<script type="text/javascript">
	function callNextPage(pagenumber, url) {
		var checkedValues = $('input[name="retLocIds"]:checked').map(
				function() {
					return this.value;
				}).get();

		var uncheckedValues = $('input[name="retLocIds"]:not(:checked)').map(
				function() {
					return this.value;
				}).get();

		searchKey = "${sessionScope.searchKey}";
		currentSearchKey = document.screenSettingsForm.searchKey.value;

		if (searchKey != currentSearchKey) {
			document.screenSettingsForm.searchKey.value = searchKey;
		}
		document.screenSettingsForm.hiddenDeAssociateLocs.value = uncheckedValues;
		document.screenSettingsForm.hiddenRetailLocs.value = checkedValues;
		document.screenSettingsForm.pageNumber.value = pagenumber;
		document.screenSettingsForm.pageFlag.value = "true";
		document.screenSettingsForm.action = url;
		document.screenSettingsForm.method = "get";
		document.screenSettingsForm.submit();
	}

	function searchRetailers() {
		var mode = document.screenSettingsForm.pageType.value;

		if (mode == "add") {
			var checkedValues = $('input[name="retLocIds"]:checked').map(
					function() {
						return this.value;
					}).get();

			associatedFlag = "${sessionScope.associatedFlag}";

			if (associatedFlag == "false") {
				if (checkedValues.length > 0) {
					associatedFlag = "true";
				}

			}
			if (associatedFlag == "true") {
				r = confirm("You want to save current data and continue ?");

				if (r == true) {
					document.screenSettingsForm.pageTypeHid.value = "SearchAndSave";
					document.screenSettingsForm.hiddenRetailLocs.value = checkedValues;
					document.screenSettingsForm.action = "savefilters.htm";
					document.screenSettingsForm.method = "POST";
					document.screenSettingsForm.submit();
				} else {
					document.screenSettingsForm.action = "searchretailers.htm";
					document.screenSettingsForm.method = "get";
					document.screenSettingsForm.submit();
				}

			} else {
				document.screenSettingsForm.action = "searchretailers.htm";
				document.screenSettingsForm.method = "get";
				document.screenSettingsForm.submit();
			}
		} else {
			searchKey = "${sessionScope.searchKey}";
			currentSearchKey = document.screenSettingsForm.searchKey.value;

			var checkedValues = $('input[name="retLocIds"]:checked').map(
					function() {
						return this.value;
					}).get();

			var uncheckedValues = $('input[name="retLocIds"]:not(:checked)')
					.map(function() {
						return this.value;
					}).get();

			associatedFlag = "${sessionScope.associatedFlag}";
			deAssociatedFlag = "${sessionScope.deAssociatedFlag}";
			hiddenRetLocIDs = "${sessionScope.hiddenRetLocIDs}";

			if (deAssociatedFlag == "false" && currentSearchKey == "") {
				if (uncheckedValues.length > 0) {
					deAssociatedFlag = "true";
				}
			} else if (associatedFlag == "false" && searchKey != "") {
				if (checkedValues.length > 0
						&& checkedValues != hiddenRetLocIDs) {
					associatedFlag = "true";
				} else {
					associatedFlag = "false";
				}
			}

			if (deAssociatedFlag == "true" || associatedFlag == "true") {
				r = confirm("You want to save current data and continue ?");

				if (r == true) {
					document.screenSettingsForm.pageTypeHid.value = "SearchAndUpdate";
					document.screenSettingsForm.hiddenDeAssociateLocs.value = uncheckedValues;
					document.screenSettingsForm.hiddenRetailLocs.value = checkedValues;
					document.screenSettingsForm.action = "updatefilters.htm";
					document.screenSettingsForm.method = "POST";
					document.screenSettingsForm.submit();
				} else {
					document.screenSettingsForm.action = "searchretailers.htm";
					document.screenSettingsForm.method = "get";
					document.screenSettingsForm.submit();

				}
			} else {
				document.screenSettingsForm.action = "searchretailers.htm";
				document.screenSettingsForm.method = "get";
				document.screenSettingsForm.submit();
			}
		}
	}

	function searchRetailersOnkeyPress(event) {
		var keycode = (event.keyCode ? event.keyCode : event.which);
		if (keycode == '13') {
			var mode = document.screenSettingsForm.pageType.value;

			var checkedValues = $('input[name="retLocIds"]:checked').map(
					function() {
						return this.value;
					}).get();

			if (mode == "add") {
				associatedFlag = "${sessionScope.associatedFlag}";

				if (associatedFlag == "false") {
					if (checkedValues.length > 0) {
						associatedFlag = "true";
					}

				}
				if (associatedFlag == "true") {
					r = confirm("You want to save current data and continue ?");

					if (r == true) {
						document.screenSettingsForm.pageTypeHid.value = "SearchAndSave";
						document.screenSettingsForm.hiddenRetailLocs.value = checkedValues;
						document.screenSettingsForm.action = "savefilters.htm";
						document.screenSettingsForm.method = "POST";
						document.screenSettingsForm.submit();
					} else {
						document.screenSettingsForm.action = "searchretailers.htm";
						document.screenSettingsForm.method = "get";
						document.screenSettingsForm.submit();
					}

				} else {
					document.screenSettingsForm.action = "searchretailers.htm";
					document.screenSettingsForm.method = "get";
					document.screenSettingsForm.submit();
				}
			} else {
				searchKey = "${sessionScope.searchKey}";
				currentSearchKey = document.screenSettingsForm.searchKey.value;

				var checkedValues = $('input[name="retLocIds"]:checked').map(
						function() {
							return this.value;
						}).get();

				var uncheckedValues = $('input[name="retLocIds"]:not(:checked)')
						.map(function() {
							return this.value;
						}).get();

				associatedFlag = "${sessionScope.associatedFlag}";
				deAssociatedFlag = "${sessionScope.deAssociatedFlag}";
				hiddenRetLocIDs = "${sessionScope.hiddenRetLocIDs}";

				if (deAssociatedFlag == "false" && currentSearchKey == "") {
					if (uncheckedValues.length > 0) {
						deAssociatedFlag = "true";
					}
				} else if (associatedFlag == "false" && searchKey != "") {
					if (checkedValues.length > 0
							&& checkedValues != hiddenRetLocIDs) {
						associatedFlag = "true";
					} else {
						associatedFlag = "false";
					}
				}

				if (deAssociatedFlag == "true" || associatedFlag == "true") {
					r = confirm("You want to save current data and continue ?");

					if (r == true) {
						document.screenSettingsForm.pageTypeHid.value = "SearchAndUpdate";
						document.screenSettingsForm.hiddenDeAssociateLocs.value = uncheckedValues;
						document.screenSettingsForm.hiddenRetailLocs.value = checkedValues;
						document.screenSettingsForm.action = "updatefilters.htm";
						document.screenSettingsForm.method = "POST";
						document.screenSettingsForm.submit();
					} else {
						document.screenSettingsForm.action = "searchretailers.htm";
						document.screenSettingsForm.method = "get";
						document.screenSettingsForm.submit();

					}
				} else {
					document.screenSettingsForm.action = "searchretailers.htm";
					document.screenSettingsForm.method = "get";
					document.screenSettingsForm.submit();
				}
			}
		}
	}

	function saveFilters() {
		var checkedValues = $('input[name="retLocIds"]:checked').map(
				function() {
					return this.value;
				}).get();

		document.screenSettingsForm.pageTypeHid.value = "Save";
		document.screenSettingsForm.hiddenRetailLocs.value = checkedValues;
		document.screenSettingsForm.action = "savefilters.htm";
		document.screenSettingsForm.method = "POST";
		document.screenSettingsForm.submit();

	}

	function updateFilters() {
		var varTotal="";
		var checkedValues = $('input[name="retLocIds"]:checked').map(
				function() {
					return this.value;
				}).get();

		var uncheckedValues = $('input[name="retLocIds"]:not(:checked)').map(
				function() {
					return this.value;
				}).get();

		document.screenSettingsForm.pageTypeHid.value = "Update";
		hiddenRetLocIDs = "${sessionScope.associretids}";		
		document.screenSettingsForm.hiddenDeAssociateLocs.value = uncheckedValues;
		document.screenSettingsForm.hiddenRetailLocs.value = checkedValues;
		varTotal = checkedValues +","+ hiddenRetLocIDs;
		////alert("check values :"+checkedValues);
		//alert("uncheckedValues values :"+uncheckedValues);
		//alert("hidden  values :"+hiddenRetLocIDs);
		//alert("total values "+varTotal);
		document.screenSettingsForm.hiddenRetailLocs.value = varTotal;
		document.screenSettingsForm.action = "updatefilters.htm";
		document.screenSettingsForm.method = "POST";
		document.screenSettingsForm.submit();

	}

	function backToFilters() {
		r = confirm("Are you sure you want to leave this page without saving the details ?");
		if (r == true) {
			document.screenSettingsForm.pageType.value = "back";
			document.screenSettingsForm.action = "searchfilters.htm";
			document.screenSettingsForm.method = "get";
			document.screenSettingsForm.submit();
		}
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
				<li><a href="displayfilters.htm">Manage Filters</a>
				</li>
				<li class="last">Add Filters</li>
			</ul>
		</div>
		<span class="blue-brdr"></span>
		<div class="content" id="login">
			<div id="menu-pnl" class="split">
				<jsp:include page="leftNavigation.jsp"></jsp:include>
			</div>
			<div class="cont-pnl split" id="">
				<div class="cont-block stretch">
					<div class="title-bar">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-experience">&nbsp;</span>
							</li>
							<li>Setup Experience</li>
						</ul>
					</div>
					<div class="cont-wrp">
						<form:form name="screenSettingsForm" id="screenSettingsForm"
							commandName="screenSettingsForm" enctype="multipart/form-data"
							action="uploadimg.htm">
							<form:errors cssStyle="color:red"></form:errors>
							<form:hidden path="filterID" value="${sessionScope.filterID}" />
							<form:hidden path="viewName" value="addFilters" />
							<form:hidden path="lowerLimit" />
							<form:hidden path="hiddenLowerLimit" />
							<form:hidden path="hiddenRetailLocs" />
							<form:hidden path="hiddenDeAssociateLocs" />
							<form:hidden path="searchFilterName"
								value="${sessionScope.filterName}" />
							<form:hidden path="pageType" value="${sessionScope.mode}" />
							<form:hidden path="pageTypeHid" />
							<input type="hidden" name="pageNumber" />
							<input type="hidden" name="pageFlag" />
							<table width="100%" border="0" cellspacing="0" cellpadding="0"
								class="cmnTbl">
								<tr>
									<td><label class="mand">Filter Name</label></td>
									<td><form:errors cssClass="errorDsply" path="filterName"></form:errors>
										<div class="cntrl-grp">
											<form:input path="filterName" id="filterName"
												cssClass="inputTxtBig" maxlength="25" />
										</div></td>
									<td width="12%" align="right"><label class="mand">Icon</label></td>
									<td width="38%"><form:errors cssClass="errorDsply"
											path="logoImageName"></form:errors> <label> <img
											id="logoImageId" class="mrgn-left" width="40" height="40"
											alt="upload" src="${sessionScope.filterImage }">
									</label></td>
								</tr>
								<tr>
									<td width="18%"><label>Search Retailers</label></td>
									<td width="32%">
										<div class="cntrl-grp">
											<c:choose>
												<c:when
													test="${sessionScope.searchKey ne null && !empty sessionScope.searchKey}">
													<form:input path="searchKey" cssClass="inputTxtBig"
														value="${sessionScope.searchKey}"
														onkeypress="searchRetailersOnkeyPress(event)"
														maxlength="50" />
												</c:when>
												<c:otherwise>
													<form:input path="searchKey" cssClass="inputTxtBig"
														onkeypress="searchRetailersOnkeyPress(event)"
														maxlength="50" />
												</c:otherwise>
											</c:choose>
										</div>
									</td>
									<td><a href="#" onclick="searchRetailers();"> <img
											src="images/searchIcon.png" width="20" height="17"
											alt="search" title="search" />
									</a></td>
									<td class="topPaddingfiltr"><span
										class="topPadding cmnRow"> <label for="trgrUpldImg">
												<input type="button" value="Upload" id="trgrUpldBtn"
												class="btn trgrUpldImg" title="Upload Image File"
												tabindex="1"> <form:hidden path="logoImageName"
													 id="logoImageName" /> <span
												class="instTxt nonBlk"></span> <form:input type="file"
													class="textboxBig" id="trgrUpldImg" path="logoImage" />
										</label>
									</span> <label id="maxSizeImageError" class="errorDsply"></label></td>

								</tr>
							</table>
							<form:errors cssClass="errorDsply" path="retailerLocIds"
								cssStyle="text-align: center"></form:errors>
						</form:form>
						<c:if
							test="${requestScope.responseStatus ne null && !empty requestScope.responseStatus}">
							<c:choose>
								<c:when test="${requestScope.responseStatus eq 'INFO' }">
									<div class="alertBx warning mrgnTop cntrAlgn">
										<span class="actn-close" title="close"></span>
										<p class="msgBx">
											<c:out value="${requestScope.responeMsg}" />
										</p>
									</div>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${requestScope.responseStatus eq 'SUCCESS' }">
											<div class="alertBx success mrgnTop cntrAlgn">
												<span class="actn-close" title="close"></span>
												<p class="msgBx">
													<c:out value="${requestScope.responeMsg}" />
												</p>
											</div>
										</c:when>
										<c:otherwise>
											<div class="alertBx failure mrgnTop cntrAlgn">
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
						<div class="relative">
							<div class="hdrClone"></div>
							<c:if
								test="${sessionScope.retailerList ne null && !empty sessionScope.retailerList}">
								<div class="scrollTbl tblHt mrgnTop">
									<table width="100%" cellspacing="0" cellpadding="0" border="0"
										id="alertTbl" class="grdTbl clone-hdr fxdhtTbl">
										<thead>
											<tr class="tblHdr">
												<th width="21%">Retailer Name</th>
												<th width="26%">Address</th>
												<th width="16%">City</th>
												<th width="15%">State</th>
												<th width="13%">Zip Code</th>
												<th width="9%"><input type="checkbox" id="fltrChkAll" />
												</th>
											</tr>
										</thead>
										<tbody class="scrollContent">
											<c:forEach items="${sessionScope.retailerList}"
												var="retailer">
												<tr>
													<td>${retailer.retName}</td>
													<td>${retailer.address}</td>
													<td>${retailer.city}</td>
													<td>${retailer.state}</td>
													<td>${retailer.postalCode}</td>
													<td><c:set var="loc" value="0"></c:set> <c:forEach
															items="${sessionScope.selectedRetLocId}" var="locId">
															<c:if test="${retailer.retLocId eq locId}">
																<c:set var="loc" value="1"></c:set>
															</c:if>
														</c:forEach> <c:choose>
															<c:when test="${loc eq '1'}">
																<input type="checkbox" name="retLocIds"
																	value="${retailer.retLocId}" id="retLocIds"
																	checked="checked">
															</c:when>
															<c:otherwise>
																<input type="checkbox" name="retLocIds"
																	value="${retailer.retLocId}" id="retLocIds">
															</c:otherwise>
														</c:choose></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
								<div class="row mrgnTop">
									<div class="pagination mrgnTop">
										<page:pageTag
											currentPage="${sessionScope.pagination.currentPage}"
											nextPage="4" totalSize="${sessionScope.pagination.totalSize}"
											pageRange="${sessionScope.pagination.pageRange}"
											url="${sessionScope.pagination.url}" />
									</div>
									<div class="col" align="right">
										<input type="button" class="btn-blue" value="Back"
											onclick="backToFilters()" title="Back" />
										<c:choose>
											<c:when test="${sessionScope.save eq 'save'}">
												<input type="button" class="btn-blue" value="Save"
													onclick="saveFilters();" title="Save" />
											</c:when>
											<c:otherwise>
												<input type="button" class="btn-blue" value="Update"
													onclick="updateFilters();" title="Update" />
											</c:otherwise>
										</c:choose>
									</div>
								</div>
							</c:if>
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
		<iframe frameborder="0" scrolling="auto" id="ifrm" src=""
			height="100%" allowtransparency="yes" width="100%"
			style="background-color: White"> </iframe>
	</div>
</div>
<script type="text/javascript">
	configureMenu("setupexperience");
</script>