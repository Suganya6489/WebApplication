<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<%@taglib prefix="page" uri="/WEB-INF/pagination.tld"%>

<script type="text/javascript">

$(document).ready(function(){
	 $(".hdrClone").find("span:gt(2)").css("text-indent","8px");
});

function searchAlerts() 
{
	document.alerts.lowerLimit.value = 0;
	document.alerts.action = "displayalerts.htm";
	document.alerts.method = "get";
	document.alerts.submit();

}

function searchAlertsOnkeyPress(event) 
{
	var keycode = (event.keyCode ? event.keyCode : event.which);
	if (keycode == '13') 
	{
		document.alerts.lowerLimit.value = 0;
		document.alerts.action = "displayalerts.htm";
		document.alerts.method = "get";
		document.alerts.submit();
	}
}

function callNextPage(pagenumber, url) 
{
	document.alerts.pageNumber.value = pagenumber;
	document.alerts.pageFlag.value = "true";
	document.alerts.action = url;
	document.alerts.method = "get";
	document.alerts.submit();
}

function addAlerts()
{
	document.alerts.lowerLimit.value = '${requestScope.lowerLimit}';
	document.alerts.action = "addalerts.htm";
	document.alerts.method = "get";
	document.alerts.submit();
}



function editAlerts(alertId)
{
	document.alerts.lowerLimit.value = '${requestScope.lowerLimit}';
	document.alerts.alertId.value = alertId;
	document.alerts.action = "editalerts.htm";
	document.alerts.method = "get";
	document.alerts.submit();
}

function deleteAlerts(alertId) 
{
	var r = confirm("Are you sure you want to delete this Alert ?");
	if(r == true)
	{
		document.alerts.lowerLimit.value = '${requestScope.lowerLimit}';
		document.alerts.alertId.value = alertId;
		document.alerts.action = "deletealert.htm";
		document.alerts.method = "POST";
		document.alerts.submit();
	}
}

function deleteAssociationAlert()
{
	alert("Alert has been associated to menu. Please deassociate and continue.");
}

</script>

<div id="wrpr">
	<div class="clear"></div>
	<div class="wrpr-cont relative">
		<div id="slideBtn">
			<a href="#" onclick="revealPanel(this);" title="Hide Menu"><img src="images/slide_off.png" width="11" height="28" alt="btn_off" />
			</a>
		</div>
		<div id="bread-crumb">
			<ul>
				<li class="scrn-icon"><span class="icon-home">&nbsp;</span>
				</li>
				<li><a href="welcome.htm">Home</a>
				</li>
				<li class="last">Setup Alerts</li>
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
							<li class="title-icon"><span class="icon-alerts">&nbsp;</span>
							</li>
							<li>Setup Alerts</li>
						</ul>
					</div>
					<div class="tabd-pnl">
						<ul class="nav-tabs">
							<li><a id="mainMenu" class="active" href="displayalerts.htm" title="Manage Alerts">Manage Alerts</a></li>
							<li><a id="subMenu" href="displayalertcat.htm" class="rt-brdr" title="Manage Alert Category">Manage Alert Category</a>
							</li>
						</ul>
						<div class="clear"></div>
					</div>
					<div class="cont-wrp">
						<form:form name="alerts" id="alerts" commandName="alerts" enctype="multipart/form-data">
							<form:hidden path="alertId" />
							<form:hidden path="lowerLimit"/>
							<input type="hidden" name="pageNumber" />
							<input type="hidden" name="pageFlag" />
							<table width="100%" border="0" cellspacing="0" cellpadding="0" class="zerobrdrTbl">
								<tr>
									<td width="6%">
										<label>Search</label>
									</td>
									<td width="20%">
										<div class="cntrl-grp">
										<c:choose>
											<c:when test="${sessionScope.searchKey ne null && !empty sessionScope.searchKey}">
												<form:input path="searchKey" cssClass="inputTxtBig" value="${sessionScope.searchKey}" onkeypress="searchAlertsOnkeyPress(event)" />
											</c:when>
											<c:otherwise>
												<form:input path="searchKey" cssClass="inputTxtBig" onkeypress="searchAlertsOnkeyPress(event)" />
											</c:otherwise>
										</c:choose>
										</div>
									</td>
									<td width="10%">
										<a href="#" onclick="searchAlerts();">
											<img src="images/searchIcon.png" width="20" height="17" alt="search" title="search" />
										</a>
									</td>
									<td width="40%" align="right">
										<input type="button" value="Add Alert" class="btn-blue" onclick="addAlerts();" title="Add Alert"/>
									</td>
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
							<c:if test="${sessionScope.alerts ne null && !empty sessionScope.alerts}">
							<div class="hdrClone"></div>
							<div class="scrollTbl tblHt mrgnTop">
								<table width="100%" cellspacing="0" cellpadding="0" border="0" id="mngalrtTbl" class="grdTbl clone-hdr fxdhtTbl">
									<thead>
										<tr class="tblHdr">
											<th width="25%">Title</th>
											<th width="23%">Category</th>
											<th width="12%">Severity</th>
											<th width="14%">Start Date</th>
											<th width="14%">End Date</th>
											<th width="12%">Action</th>
										</tr>
									</thead>
									<tbody class="scrollContent">
										<c:forEach items="${sessionScope.alerts}" var="alert">
											<tr>
												<td>${alert.title}</td>
												<td>${alert.category}</td>
												<td align="left">
													<c:choose>
														<c:when test="${alert.severity eq 'Low'}">														
															<img src="images/low.png" width="16" height="14" alt="high" title="low" />
														</c:when>
														<c:when test="${alert.severity eq 'Medium'}">
															<img src="images/medium.png" width="16" height="14" alt="high" title="medium" />
														</c:when>
														<c:when test="${alert.severity eq 'High'}">
															<img src="images/high.png" width="16" height="14" alt="high" title="high" />
														</c:when>
														<c:otherwise>
															<img src="" width="16" height="14" alt="high" title="high" />
														</c:otherwise>
													</c:choose>
												</td>
												<td>${alert.startDate}</td>
												<td>${alert.endDate}</td>
												<td>
													<a title="edit" href="#" onclick="editAlerts(${alert.alertId})">
														<img height="20" width="20" class="actn-icon" alt="edit" src="images/edit_icon.png" />
													</a> 
													<c:choose>
														<c:when test="${alert.menuItemExist eq true}">
															<a onclick="deleteAssociationAlert();" href="javascript:void(0);" title="Delete">
																<img height="20" width="20" class="actn-icon" alt="delete" src="images/delete_icon.png" />
															</a>
														</c:when>
														<c:otherwise>
															<a onclick="deleteAlerts('${alert.alertId}')" href="javascript:void(0);" title="Delete"> 
																<img height="20" width="20" class="actn-icon" alt="delete" src="images/delete_icon.png" />
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
							<div class="pagination mrgnTop">
								<page:pageTag currentPage="${sessionScope.pagination.currentPage}" nextPage="4" totalSize="${sessionScope.pagination.totalSize}" pageRange="${sessionScope.pagination.pageRange}" url="${sessionScope.pagination.url}" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	configureMenu("setupalerts");
</script>