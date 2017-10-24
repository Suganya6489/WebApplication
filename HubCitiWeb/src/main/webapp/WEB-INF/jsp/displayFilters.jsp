<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<%@taglib prefix="page" uri="/WEB-INF/pagination.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<script type="text/javascript">
	var beforeSortOrder = "";
	var afterSortOrder = "";
	
	function beforeSort() {

		$('#mngalrtTbl tbody tr').each(function() {

			var filterId = "";
			filterId = $(this).attr('id');

			if (filterId != "" && filterId != "undefined") {

				//index++;

				if (beforeSortOrder == "") {
					beforeSortOrder = filterId;
					//sortOrder = index;

				} else {

					beforeSortOrder = beforeSortOrder + "," + filterId;
					//sortOrder = sortOrder + "," + index;
				}
			}

		});

	}
	$(function() {
		var fixHelper = function(e, ui) {
			ui.children().each(function() {
				$(this).width($(this).width());
			});
			return ui;
		};
		$("#mngalrtTbl tbody").sortable({
			'opacity' : 0.6,
			containment : '.scrollTbl',
			cursor : "move",
			tolerance : "pointer",
			helper : fixHelper /*fix for mozilla click trigger*/,
		}).disableSelection();
	});
	function callNextPage(pagenumber, url) {

		var filterOrder = "";
		var sortOrder = "";
		//var index = 0;
		var currentPage = '${sessionScope.pagination.currentPage}';
		var index = (currentPage - 1) * 20;
		var filterId = "";
		$('#mngalrtTbl tbody tr').each(function() {
			filterId = $(this).attr('id');

			if (filterId != "" && filterId != "undefined") {

				index++;

				if (filterOrder == "") {
					filterOrder = filterId;
					sortOrder = index;

				} else {

					filterOrder = filterOrder + "," + filterId;
					sortOrder = sortOrder + "," + index;
				}
			}

		});

		if (beforeSortOrder != filterOrder) {

			var result = confirm("Do you want to save the changes?");

			if (result != true) {
				filterOrder = beforeSortOrder;
			}
		}

		$.ajaxSetup({
			cache : false
		});

		$.ajax({
			type : "POST",
			url : "savefilterorder.htm",
			data : {
				'saveOrder' : sortOrder,
				'filterOrder' : filterOrder
			},

			success : function(response) {

				if (response == 'SUCCESS') {

					document.filters.pageNumber.value = pagenumber;
					document.filters.pageFlag.value = "true";
					document.filters.action = url;
					document.filters.method = "get";
					document.filters.submit();

				} else {
					alert('Error occured while saving Filters order');
				}

			},
			error : function(e) {
				alert('Error occured while saving Filters order');
			}
		});

	}

	function addFilters() {
		document.filters.lowerLimit.value = '${requestScope.lowerLimit}';
		document.filters.action = "addfilters.htm";
		document.filters.method = "get";
		document.filters.submit();
	}

	function editFilter(filterID) {
		document.filters.filterID.value = filterID;
		document.filters.lowerLimit.value = '${requestScope.lowerLimit}';
		document.filters.action = "editfilters.htm";
		document.filters.method = "get";
		document.filters.submit();
	}

	function searchFiltersOnkeyPress(event) {
		var keycode = (event.keyCode ? event.keyCode : event.which);
		if (keycode == '13') {
			document.filters.lowerLimit.value = 0;
			document.filters.action = "displayfilters.htm";
			document.filters.method = "get";
			document.filters.submit();
		}
	}

	function searchFilters() {
		document.filters.lowerLimit.value = 0;
		document.filters.action = "displayfilters.htm";
		document.filters.method = "get";
		document.filters.submit();
	}

	function deleteFilter(filterID) {
		r = confirm("Are you sure you want to delete this filter");
		if (r == true) {
			document.filters.filterID.value = filterID;
			document.filters.lowerLimit.value = '${requestScope.lowerLimit}';
			document.filters.action = "deletefilters.htm";
			document.filters.method = "POST";
			document.filters.submit();
		}
	}

	function saveOrder() {

		var filterOrder = "";
		var sortOrder = "";
		//var index = 0;
		var currentPage = '${sessionScope.pagination.currentPage}';
		var index = (currentPage - 1) * 20;
		var filterId = "";
		saveFlag = true;
		$('#mngalrtTbl tbody tr').each(function() {
			filterId = $(this).attr('id');

			if (filterId != "" && filterId != "undefined") {

				index++;

				if (filterOrder == "") {
					filterOrder = filterId;
					sortOrder = index;

				} else {

					filterOrder = filterOrder + "," + filterId;
					sortOrder = sortOrder + "," + index;
				}
			}

		});

		$.ajaxSetup({
			cache : false
		});

		$.ajax({
			type : "POST",
			url : "savefilterorder.htm",
			data : {
				'saveOrder' : sortOrder,
				'filterOrder' : filterOrder
			},

			success : function(response) {

				if (response == 'SUCCESS') {
					beforeSortOrder=filterOrder;
					alert("Filters order saved successfully");

				} else {
					alert('Error occured while saving Filters order');
				}

			},
			error : function(e) {
				alert('Error occured while saving Filters order');
			}
		});
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
				<li class="last">Manage Filters</li>
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
					<div class="tabd-pnl">
						<ul class="nav-tabs">
							<li><a class="" href="displaycityexp.htm">Manage
									Experience</a></li>
							<li><a href="#" class="rt-brdr active">Manage Filters</a></li>
						</ul>
						<div class="clear"></div>
					</div>
					<div class="cont-wrp">
						<form:form name="filters" id="filters" commandName="filters"
							enctype="multipart/form-data">
							<form:hidden path="filterID" />
							<form:hidden path="cityExperienceID"
								value="${sessionScope.cityExperienceID}" />
							<form:hidden path="lowerLimit" />
							<input type="hidden" name="pageNumber" />
							<input type="hidden" name="pageFlag" />
							<table width="100%" border="0" cellspacing="0" cellpadding="0"
								class="cmnTbl">
								<tr>
									<c:if test="${requestScope.NoFilters ne 'NoFilters'}">
										<td width="19%"><label>Search Filters</label></td>
										<td width="31%">
											<div class="cntrl-grp">
												<c:choose>
													<c:when
														test="${sessionScope.searchFilterName ne null && !empty sessionScope.searchFilterName}">
														<form:input path="searchFilterName" cssClass="inputTxtBig"
															value="${sessionScope.searchFilterName}"
															onkeypress="searchFiltersOnkeyPress(event)"
															maxlength="50" />
													</c:when>
													<c:otherwise>
														<form:input path="searchFilterName" cssClass="inputTxtBig"
															onkeypress="searchFiltersOnkeyPress(event)"
															maxlength="50" />
													</c:otherwise>
												</c:choose>
											</div>
										</td>
										<td><a href="#" onclick="searchFilters();"> <img
												src="images/searchIcon.png" width="20" height="17"
												alt="search" title="search" />
										</a></td>
									</c:if>
									<td align="right"><input type="button" value="Add Filter"
										class="btn-blue" onclick="addFilters();" title="Add Filter" />
									</td>
								</tr>
							</table>
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

											<div class="alertBx failure mrgnTop cntrAlgn alertBx_big">
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
						<div class="relative" id="rtlrList">
							<c:if
								test="${sessionScope.filtersDetails ne null && !empty sessionScope.filtersDetails}">
								<div class="hdrClone"></div>
								<div class="scrollTbl tblHt mrgnTop">
									<table width="100%" cellspacing="0" cellpadding="0" border="0"
										id="mngalrtTbl" class="grdTbl clone-hdr fxdhtTbl">
										<thead>
											<tr class="tblHdr">
												<th width="70%">Filter Name</th>
												<th width="18%">Filter Icon</th>
												<th width="12%">Action</th>
											</tr>
										</thead>
										<tbody class="scrollContent">
											<c:forEach items="${sessionScope.filtersDetails}"
												var="filter">
												<tr id="${filter.filterID}">
													<td>${filter.filterName}</td>
													<td><img height="40" width="40" class="actn-icon"
														alt="filterImage" src="${filter.logoImageName}" /></td>
													<td><a title="edit" href="#"
														onclick="editFilter('${filter.filterID}');"><img
															height="20" width="20" class="actn-icon" alt="edit"
															src="images/edit_icon.png" /> </a> <a title="delete"
														href="#" onclick="deleteFilter('${filter.filterID}');"><img
															height="20" width="20" class="actn-icon" alt="delete"
															src="images/delete_icon.png" /> </a></td>
												</tr>
											</c:forEach>

										</tbody>
									</table>
								</div>
							</c:if>
							<div class="pagination mrgnTop">
								<page:pageTag
									currentPage="${sessionScope.pagination.currentPage}"
									nextPage="4" totalSize="${sessionScope.pagination.totalSize}"
									pageRange="${sessionScope.pagination.pageRange}"
									url="${sessionScope.pagination.url}" />
							</div>
							<c:if
								test="${sessionScope.filtersDetails ne null && !empty sessionScope.filtersDetails && fn:length(filtersDetails) > 1}">
								<div class="info-pnl mrgnTop_small">
									To change the order and sort of the pages, drag and drop the
									page to the desired position. <input type="button"
										class="btn-blue floatR" value="Save Order"
										onclick="saveOrder();">
								</div>
							</c:if>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	beforeSort();
</script>

<script type="text/javascript">
	configureMenu("setupexperience");
</script>