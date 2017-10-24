<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<%@taglib prefix="page" uri="/WEB-INF/pagination.tld"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<script type="text/javascript">

$(document).ready(function() {	
	$(".alrtClose").click(function() {
		$('p.ctgryassoci').hide();
	});	
	
	$('.grdTbl tr td').each(function() {
		var val = $.trim($(this).text());
		if (/\s/.test(val)) {
			$(this).css("word-break", "keeep-all");
		} else {
			$(this).css("word-break", "break-all");
		}
	});
});

</script>

<script type="text/javascript">

	function callNextPage(pagenumber, url) 
	{
		document.ManageEventForm.pageNumber.value = pagenumber;
		document.ManageEventForm.pageFlag.value = "true";
		document.ManageEventForm.action = url;
		document.ManageEventForm.method = "get";
		document.ManageEventForm.submit();
	}
	
	function addFundraiserEvent() {
		document.ManageEventForm.hcEventID.value = '';
		document.ManageEventForm.action = "addFundraiserEvent.htm";
		document.ManageEventForm.method = "get";
		document.ManageEventForm.submit();
	}
	
	function editFundraiserEvent(eventId) {
		document.ManageEventForm.hcEventID.value = eventId;
		document.ManageEventForm.action = "editFundraiserEvent.htm";
		document.ManageEventForm.method = "get";
		document.ManageEventForm.submit();
	}
	
	function searchFundraisingEvents() {
		document.ManageEventForm.lowerLimit.value = 0;
		document.ManageEventForm.action = "managefundraisers.htm";
		document.ManageEventForm.method = "GET";
		document.ManageEventForm.submit();
	}

	function searchFundraisingEventsOnKeyPress(event) {
		document.ManageEventForm.lowerLimit.value = 0;
		var keycode = (event.keyCode ? event.keyCode : event.which);
		if (keycode == '13') {
			document.ManageEventForm.action = "managefundraisers.htm";
			document.ManageEventForm.method = "GET";
			document.ManageEventForm.submit();
		}
	}
</script>

<div id="wrpr">
	<span class="clear"></span>
	<div class="wrpr-cont relative">
		<div id="slideBtn">
			<a href="#" onclick="revealPanel(this);" title="Hide Menu"><img
				src="images/slide_off.png" width="11" height="28" alt="btn_off" /></a>
		</div>
		<!--Breadcrum div starts-->
		<div id="bread-crumb">
			<ul>
				<li class="scrn-icon"><span class="icon-home">&nbsp;</span></li>
				<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
					<li><a href="welcome.htm">Home</a></li>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('ROLE_FUNDRAISING_SUPER_USER_VIEW','ROLE_FUNDRAISING_USER_VIEW')">
					<li>Home</li>
				</sec:authorize>
				<li class="last">Setup Fundraisers</li>
			</ul>
		</div>
		<!--Breadcrum div ends-->
		<span class="blue-brdr"></span>
		<!--Content div starts-->
		<div class="content" id="login">
			<!--Left Menu div starts-->
			<div id="menu-pnl" class="split">
				<jsp:include page="leftNavigation.jsp"></jsp:include>
			</div>
			<!--Left Menu div ends-->
			<!--Content panel div starts-->
			<div class="cont-pnl split" id="equalHt">
				<div class="cont-block rt-brdr stretch">
					<form:form name="ManageEventForm" commandName="ManageEventForm">
						<form:hidden path="hcEventID" />
						<form:hidden path="lowerLimit" value="${requestScope.lowerLimit}" />
						<input type="hidden" name="pageNumber" />
						<input type="hidden" name="pageFlag" />
						<div class="title-bar">
							<ul class="title-actn">
								<li class="title-icon"><span class="icon-fundraising">&nbsp;</span></li>
								<li>Setup Fundraiser Events</li>
							</ul>
						</div>
						<div class="tabd-pnl">
							<ul class="nav-tabs">
								<li><a class="brdr-rt" href="managefundraisers.htm">Manage Fundraiser
										Events</a></li>

							</ul>
							<div class="clear"></div>
						</div>
						<div class="cont-wrp">

							<table width="100%" border="0" cellspacing="0" cellpadding="0"
								class="zerobrdrTbl">
								<tr>
									<td width="6%"><label>Search</label></td>
									<td width="20%">
										<div class="cntrl-grp">
											<form:input type="text" path="eventSearchKey" class="inputTxtBig" onkeypress="searchFundraisingEventsOnKeyPress(event)" />
										</div>
									</td>
									<td width="10%">
										<a href="javascript:void(0);">
											<img src="images/searchIcon.png" width="20" height="17" alt="search" title="Search Fundraiser event" maxlength="30"
											 	onclick="searchFundraisingEvents('')" /> 
										</a>
									</td>
									<td width="40%" align="right">
										<input type="button" value="Add Event" title="Add Fundraiser Event" class="btn-blue" onclick="addFundraiserEvent();" />
									</td>
								</tr>
							</table>
							<c:if test="${requestScope.responseEvntStatus ne null && !empty requestScope.responseEvntStatus}">
								<c:choose>
									<c:when test="${requestScope.responseEvntStatus eq 'SUCCESS' }">
										<div class="alertBx success mrgnTop cntrAlgn">
											<span class="actn-close" title="close"></span>
											<p class="msgBx">
												<c:out value="${requestScope.responeEvntMsg}" />
											</p>
										</div>
									</c:when>
									<c:otherwise>
										<div class="alertBx warning mrgnTop cntrAlgn">
											<span class="actn-close" title="close"></span>
											<p class="msgBx">
												<c:out value="${requestScope.responeEvntMsg}" />
											</p>
										</div>
									</c:otherwise>
								</c:choose>
							</c:if>
							<div class="relative">
								<div class="hdrClone"></div>
								<p class="ctgryassoci">
									<a class="alrtClose" href="javascript:void(0);" title="close">x</a>Fundraising
									Event has been associated to menu. Please de-associate and
									delete
								</p>
								<c:choose>
									<c:when
										test="${sessionScope.fundraiserseventlst ne null && !empty sessionScope.fundraiserseventlst}">
										<div class="scrollTbl tblHt mrgnTop">
											<table width="100%" cellspacing="0" cellpadding="0"
												border="0" id="alertTbl" class="grdTbl clone-hdr fxdhtTbl">
												<thead>
													<tr class="tblHdr">
														<th width="25%">Title</th>
														<th width="33%">Organization Hosting/Appsite</th>
														<th width="15%">Start Date</th>
														<th width="15%">End Date</th>
														<th width="12%">Action</th>
													</tr>
												</thead>
												<tbody class="scrollContent">
													<c:forEach
														items="${sessionScope.fundraiserseventlst.eventLst}"
														var="eve">
														<tr>
															<td>${eve.hcEventName}</td>
															<td>${eve.address}</td>
															<td>${eve.eventStartDate}</td>
															<td>${eve.eventEndDate}</td>

															<td><a title="edit" href="javascript:void(0);">
																	<img height="20" width="20" class="actn-icon"
																	title="Edit Fundraiser Event" src="images/edit_icon.png" onclick="editFundraiserEvent(${eve.hcEventID})"/>
															</a> <a title="Delete Fundraiser Event" href="javascript:void(0);">
																	<img height="20" width="20" class="actn-icon"
																	alt="delete" src="images/delete_icon.png"
																	onclick="deleteFundraisingEvent(${eve.hcEventID})" />
															</a></td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
									</c:when>
								</c:choose>
							</div>
							<c:if
								test="${sessionScope.fundraiserseventlst ne null && !empty sessionScope.fundraiserseventlst}">
								<div class="pagination mrgnTop">
									<page:pageTag
										currentPage="${sessionScope.pagination.currentPage}"
										nextPage="4" totalSize="${sessionScope.pagination.totalSize}"
										pageRange="${sessionScope.pagination.pageRange}"
										url="${sessionScope.pagination.url}" />
								</div>
							</c:if>
						</div>
					</form:form>
				</div>
			</div>
			<!--Content panel div ends-->
		</div>
		<!--Content div ends-->
	</div>
</div>
<script type="text/javascript">
	configureMenu("setupfndrevt");
</script>