<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<%@taglib prefix="page" uri="/WEB-INF/pagination.tld"%>
<script type="text/javascript" src="scripts/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/jquery.form.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/colorPickDynamic.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/colorPicker.js"></script>
<script src="/HubCiti/scripts/ckeditor/ckeditor.js"></script>
<script src="/HubCiti/scripts/ckeditor/config.js"></script>

<link rel="stylesheet" type="text/css" href="/HubCiti/styles/colorPicker.css" />

<script type="text/javascript">
	function createAnythingPage() 
	{
		document.screenSettingsForm.lowerLimit.value = '${requestScope.lowerLimit}';
		document.screenSettingsForm.action = "buildanythingpage.htm";
		document.screenSettingsForm.method = "get";
		document.screenSettingsForm.submit();

	}

	function searchAnythingPage() {
		document.screenSettingsForm.lowerLimit.value = 0;
		document.screenSettingsForm.action = "displayanythingpages.htm";
		document.screenSettingsForm.method = "get";
		document.screenSettingsForm.submit();

	}

	function searchAnythingPageOnkeyPress(event) {
		var keycode = (event.keyCode ? event.keyCode : event.which);
		if (keycode == '13') {
			document.screenSettingsForm.lowerLimit.value = 0;
			document.screenSettingsForm.action = "displayanythingpages.htm";
			document.screenSettingsForm.method = "get";
			document.screenSettingsForm.submit();

		}
	}

	function callNextPage(pagenumber, url) {
		document.screenSettingsForm.pageNumber.value = pagenumber;
		document.screenSettingsForm.pageFlag.value = "true";
		document.screenSettingsForm.action = url;
		document.screenSettingsForm.method = "get";
		document.screenSettingsForm.submit();
	}

	function editAnythingPageScreen(btnLinkId, pageView) {
		document.screenSettingsForm.lowerLimit.value = '${requestScope.lowerLimit}';
		document.screenSettingsForm.hiddenBtnLinkId.value = btnLinkId;
		if(pageView == "Make Your Own")
		{
			document.screenSettingsForm.action = "editmakeyoursownap.htm";
			document.screenSettingsForm.method = "POST";
			document.screenSettingsForm.submit();
		}
		else
		{
			document.screenSettingsForm.action = "editanythingscreen.htm";
			document.screenSettingsForm.method = "POST";
			document.screenSettingsForm.submit();
		}
		
	}

	function deleteAnythingPage(btnLinkId) 
	{
		var r = confirm("Are you sure you want to delete this Anything Page ?");
		if(r == true)
		{
			document.screenSettingsForm.lowerLimit.value = '${requestScope.lowerLimit}';
			document.screenSettingsForm.hiddenBtnLinkId.value = btnLinkId;
			document.screenSettingsForm.action = "deleteanythingpage.htm";
			document.screenSettingsForm.method = "POST";
			document.screenSettingsForm.submit();
		}
	}

	function deleteAssociationAlert()
	{
		alert("Anything page has been associated to menu. Please deassociate and continue.");
	}
</script>
<div id="wrpr">
	<div class="clear"></div>
	<div class="wrpr-cont relative">
		<div id="slideBtn">
			<a href="#" onclick="revealPanel(this);" title="Hide Menu"> <img src="images/slide_off.png" width="11" height="28" alt="btn_off" /> </a>
		</div>
		<div id="bread-crumb">
			<ul>
				<li class="scrn-icon"><span class="icon-home">&nbsp;</span>
				</li>
				<li><a href="welcome.htm">Home</a></li>
				<li class="last">Setup AnyThing Page<sup>TM</sup></li>
			</ul>
		</div>
		<span class="blue-brdr"></span>
		<div class="content" id="login">
			<div id="menu-pnl" class="split">
				<jsp:include page="leftNavigation.jsp"></jsp:include>
			</div>
			<div class="cont-pnl split">
				<div class="cont-block stretch">
					<div class="title-bar">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-aboutus">&nbsp;</span>
							</li>
							<li>Manage AnyThing Page<sup>TM</sup></li>
						</ul>
					</div>

					<span class="clear"></span>
					<div class="cont-wrp mrngBtm_small">
						<form:form name="screenSettingsForm" id="screenSettingsForm" commandName="screenSettingsForm" enctype="multipart/form-data">
							<form:hidden path="hiddenBtnLinkId" />
							<form:hidden path="lowerLimit"/>
							<input type="hidden" name="pageNumber" />
							<input type="hidden" name="pageFlag" />
							<table width="100%" border="0" cellspacing="0" cellpadding="0" class="cmnTbl">
								<tr>
									<td width="24%">Search AnyThing Page<sup>TM</sup></td>
									<td width="30%">
										<div class="cntrl-grp">
											<c:choose>
												<c:when test="${sessionScope.searchKey ne null && !empty sessionScope.searchKey}">
													<form:input path="searchKey" cssClass="inputTxtBig" value="${sessionScope.searchKey}" onkeypress="searchAnythingPageOnkeyPress(event)" />
												</c:when>
												<c:otherwise>
													<form:input path="searchKey" cssClass="inputTxtBig" onkeypress="searchAnythingPageOnkeyPress(event)" />
												</c:otherwise>
											</c:choose>
										</div></td>
									<td width="22%"><a href="#" onclick="searchAnythingPage();"><img src="images/searchIcon.png" width="20" height="17" alt="search" title="search" />
									</a></td>
									<td width="24%"><input type="button" name="button" value="Build AnyThing Page" class="btn-blue" id="buildAnyThngPg" onclick="createAnythingPage();" title="Create Anything Page" /></td>
								</tr>
							</table>							
						</form:form>
						<c:if test="${requestScope.responseStatus ne null && !empty requestScope.responseStatus}">
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
							<c:if test="${sessionScope.anythingPageList ne null && !empty sessionScope.anythingPageList}">
							<div class="hdrClone"></div>
           					<div class="scrollTbl mrgnTop">
								<table width="100%" border="0" cellspacing="0" cellpadding="0" class="grdTbl clone-hdr" id="anyThngTbl">
									<thead>
										<tr class="tblHdr">
											<th width="39%">Page Title</th>
											<th width="47%">Page Type</th>
											<th width="14%" align="center" colspan="2">Action</th>
										
										</tr>
									</thead>
									<tbody class="scrollContent">											
										<c:forEach items="${sessionScope.anythingPageList}" var="item">
											<tr>
												<td>${item.anythingPageTitle}</td>
												<td>${item.pageType}</td>
												<td>																							
													<a onclick="editAnythingPageScreen('${item.hcAnythingPageId}','${item.pageView}')" href="javascript:void(0);" title="Edit"> 
														<img height="20" width="20" alt="edit" src="images/edit_icon.png" class="actn-icon"> 
													</a>
													<c:choose>
														<c:when test="${item.menuItemExist eq true}">
															<a onclick="deleteAssociationAlert();" href="javascript:void(0);" title="Delete">
																<img height="20" width="20" src="images/delicon.png" alt="delete" class="actn-icon">
															</a>
														</c:when>
														<c:otherwise>
															<a onclick="deleteAnythingPage('${item.hcAnythingPageId}')" href="javascript:void(0);" title="Delete"> 
																<img height="20" width="20" src="images/delicon.png" alt="delete" class="actn-icon">
															</a>
														</c:otherwise>
													</c:choose>
												</td>
												
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
							</c:if>
						</div>
						<div class="pagination mrgnTop">
							<page:pageTag currentPage="${sessionScope.pagination.currentPage}" nextPage="4" totalSize="${sessionScope.pagination.totalSize}" pageRange="${sessionScope.pagination.pageRange}" url="${sessionScope.pagination.url}" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	configureMenu("setupanythingpage");
</script>