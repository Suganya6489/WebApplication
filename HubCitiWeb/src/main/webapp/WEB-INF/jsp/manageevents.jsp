<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<%@taglib prefix="page" uri="/WEB-INF/pagination.tld"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$(".hdrClone span:gt(1)").css("text-indent","8px");
	$(".modal-close").on('click.popup', function() {
		$(".modal-popupWrp").hide();
		$(".modal-popup").slideUp();
		$(".modal-popup").find(".modal-bdy input").val("");
		$(".modal-popup i").removeClass("errDsply");
	});
	$(".alrtClose").click(function() {
						$('p.ctgryassoci').hide();
						//$("ctgryassoci").hide();
					});
	/*$(".actn-close").click(function() {
		alert('event alert');
		var vEid = $(this).attr('div.alertBx.id').value;
		alert(vEid);
		$(this).parent('div.alertBx').remove();

	});*/

	$(".actn-close").on("click",function() { 
		var elemnt = $(this).parents('div'); 
		var alrtId = elemnt.attr("id");
		if(alrtId == 'evntClose'){
			$("#"+alrtId).hide();
			/*document.ManageEventForm.eventSearchKey.value=null;
			document.ManageEventForm.action = "manageevents.htm";
			document.ManageEventForm.method = "GET";
			document.ManageEventForm.submit();*/
		}
		else if(elemnt){
		$(this).parent('div.alertBx').hide();
		}else{
			}
	 });	
});

function callNextPage(pagenumber, url) 
{
	document.ManageEventForm.pageNumber.value = pagenumber;
	document.ManageEventForm.pageFlag.value = "true";
	document.ManageEventForm.action = url;
	document.ManageEventForm.method = "get";
	document.ManageEventForm.submit();
}
</script>
</head>
<body onload="resizeDoc();" onresize="resizeDoc();">
	<div class="wrpr-cont relative">
		<div id="slideBtn">
			<a href="javascript:void(0);" onclick="revealPanel(this);"
				title="Hide Menu"> <img src="images/slide_off.png" width="11"
				height="28" alt="btn_off" />
			</a>
		</div>
		<div id="bread-crumb">
			<ul>
				<li class="scrn-icon"><span class="icon-home">&nbsp;</span></li>
				<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
					<li><a href="welcome.htm">Home</a></li>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('ROLE_EVENT_SUPER_USER_VIEW','ROLE_EVENT_USER_VIEW')">
					<li>Home</li>
				</sec:authorize>
				<!-- <li>Home</li> -->
				<li class="last">Setup Events</li>
			</ul>
		</div>
		<span class="blue-brdr"></span>
		<div class="content" id="login">
			<div id="menu-pnl" class="split">
				<jsp:include page="leftNavigation.jsp"></jsp:include>
			</div>
			<div class="cont-pnl split" id="">
				<div class="cont-block stretch">
					<form:form name="ManageEventForm" commandName="ManageEventForm">
						<form:hidden path="lowerLimit" />
						<input type="hidden" name="pageNumber" />
						<input type="hidden" name="pageFlag" />
						<div class="title-bar">
							<ul class="title-actn">
								<li class="title-icon"><span class="icon-alerts">&nbsp;</span>
								</li>
								<li>Setup Events</li>
							</ul>
						</div>
						<div class="tabd-pnl">
							<ul class="nav-tabs">
								<li><a id="mainMenu" href="manageevents.htm" class="active rt-brdr" >Manage
										Events</a></li>
								<!--<li><a id="subMenu" href="displayeventcate.htm"
									class="rt-brdr">Manage Event Category</a></li> -->
							</ul>
							<div class="clear"></div>
						</div>
						<div class="cont-wrp">
							<table width="100%" border="0" cellspacing="0" cellpadding="0"
								class="zerobrdrTbl">
								<tr>
									<c:choose>
										<c:when test="${requestScope.searchEvnt eq 'searchEvnt'}">
											<td width="6%"><label>Search</label></td>
											<td width="20%"><div class="cntrl-grp">
													<form:input type="text" path="eventSearchKey"
														class="inputTxtBig" onkeypress="searchEvent(event)" />
												</div> <i class="emptysearh">Please Enter Search keyword</i></td>
											<td width="10%"><a href="javascript:void(0);"><img
													src="images/searchIcon.png" width="20" height="17"
													alt="search" title="Search event" maxlength="30"
													onclick="searchEvent('')" /> </a></td>
										</c:when>
										<c:otherwise>
											<c:if
												test="${sessionScope.eventlst ne null && !empty sessionScope.eventlst  }">
												<td width="6%"><label>Search</label></td>
												<td width="20%"><div class="cntrl-grp">
														<form:input type="text" path="eventSearchKey"
															class="inputTxtBig" onkeypress="searchEvent(event)" />
													</div> <i class="emptysearh">Please Enter Search keyword</i></td>
												<td width="10%"><a href="javascript:void(0);"><img
														src="images/searchIcon.png" width="20" height="17"
														alt="search" title="Search event" maxlength="30"
														onclick="searchEvent('')" /> </a></td>
											</c:if>
										</c:otherwise>
									</c:choose>
									<td width="40%" align="right"><input type="button"
										value="Add Event" title="Add event" class="btn-blue"
										onclick="window.location.href='addEvent.htm'" /></td>
								</tr>
							</table>
							<c:if
								test="${sessionScope.responseEvntStatus ne null && !empty sessionScope.responseEvntStatus}">
								<c:choose>
									<c:when test="${sessionScope.responseEvntStatus eq 'SUCCESS' }">
										<div class="alertBx success mrgnTop cntrAlgn">
											<span class="actn-close" title="close"></span>
											<p class="msgBx">
												<c:out value="${sessionScope.responeEvntMsg}" />
											</p>
										</div>
									</c:when>
								</c:choose>
							</c:if>
							<div class="relative">
								<div class="hdrClone"></div>
								<p class="ctgryassoci">
									<a class="alrtClose" href="javascript:void(0);" title="close">x</a>Event
									has been associated to menu. Please deassociate and delete
								</p>
								<c:choose>
									<c:when
										test="${sessionScope.eventlst ne null && !empty sessionScope.eventlst}">
										<div class="scrollTbl tblHt mrgnTop">
											<table width="100%" cellspacing="0" cellpadding="0"
												border="0" id="alertTbl" class="grdTbl clone-hdr fxdhtTbl">
												<thead>
													<tr class="tblHdr">
														<th width="20%">Event Name</th>
														<th width="34%">Location</th>
														<th width="15%">Start Date</th>
														<th width="15%">End Date</th>
														<th width="16%">Action</th>
													</tr>
												</thead>
												<tbody class="scrollContent">
													<c:forEach items="${sessionScope.eventlst.eventLst}"
														var="eve">
														<tr>
															<td>${eve.hcEventName}</td>
												
															<td><div class="cell-wrp">${eve.address}, ${eve.city}, ${eve.state},
																${eve.postalCode}</div></td>
															<td>${eve.eventStartDate}</td>
															<td>${eve.eventEndDate}</td>

															<td><a title="edit" href="javascript:void(0);">
																	<img height="20" width="20" class="evt-actn-icon"
																	title="Edit event" src="images/edit_icon.png"
																	onclick="window.location.href='editEventDetails.htm?eventId=${eve.hcEventID}'" />
															</a> <a title="Delete event" href="javascript:void(0);">
																	<img height="20" width="20" class="evt-actn-icon"
																	alt="delete" src="images/delete_icon.png"
																	onclick="deleteEvent(${eve.hcEventID})" />
															</a>
															
															<c:choose>
															<c:when test ="${eve.isNewLogisticsImg eq true}">
															
															<a title="map" href="addEvtMarker.htm?evtId=${eve.hcEventID}">
															<img height="20" width="20" class="evt-actn-icon" alt="map" src="images/marker_icon.png" title="Add Marker Details" /></a>
															</c:when>
															
															<c:otherwise>
															
																<a title="map" href="#">
															<img height="20" width="20" class="evt-actn-icon" alt="map" src="images/marker_icon_dsbl.png"  title="Add Marker Details"  /></a>
															</c:otherwise>
															
															
															</c:choose>
															
															
															
															
															</td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
									</c:when>
									<c:otherwise>
										<div class="alertBx warning mrgnTop cntrAlgn" id="evntClose">
											<span class="actn-close" title="close"></span>
											<p class="msgBx">No Event found</p>
										</div>
									</c:otherwise>
								</c:choose>
							</div>
							<c:if
								test="${sessionScope.eventlst ne null && !empty sessionScope.eventlst}">
								<div class="pagination mrgnTop">
									<page:pageTag
										currentPage="${sessionScope.pagination.currentPage}"
										nextPage="4" totalSize="${sessionScope.pagination.totalSize}"
										pageRange="${sessionScope.pagination.pageRange}"
										url="${sessionScope.pagination.url}" />
								</div>
							</c:if>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
	configureMenu("setupevents");
</script>

</html>