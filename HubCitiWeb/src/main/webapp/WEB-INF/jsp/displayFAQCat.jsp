<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<%@taglib prefix="page" uri="/WEB-INF/pagination.tld"%>
<html>
<head>
<script type="text/javascript" src="/HubCiti/scripts/jquery-1.10.2.js"></script>
<script src="/HubCiti/scripts/jquery-ui.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<%@ page
	import="java.util.List,com.hubciti.common.pojo.FAQ,com.hubciti.common.pojo.FAQDetails"%>
<script type="text/javascript">/* Drag and Drop/Reorder/Renumber table row functionality. */
 
window.onload = function() {
	<%List<FAQ> faqlst = (List<FAQ>) session
					.getAttribute("faqCategories");
			if (faqlst != null && !faqlst.isEmpty()) {
				StringBuffer strSortIndex = new StringBuffer();
				FAQ objPage = new FAQ();
				//List retPageList = retailersList.getPageList();
				//if (retPageList != null && !retPageList.isEmpty()) {
					
					if (faqlst.size() > 1) {
						for (int i = 0; i < faqlst.size(); i++) {
							objPage = (FAQ) faqlst.get(i);
							if (objPage.getSortOrder() == null) {
								strSortIndex.append(Integer.toString(objPage.getRowNum()));
								strSortIndex.append(",");
							} else {
								strSortIndex.append(Integer.toString(objPage
										.getSortOrder()));
								strSortIndex.append(",");
							}
						}%>
				document.getElementById("sortOrderIds").value = "<%=strSortIndex.toString()%>";
				<%} else {%>
				document.getElementById("displayOrderBtn").style.display = "none";
				<%}%>
			<%} else {%>
				document.getElementById("displayOrderBtn").style.display = "none";
			<%}%>
	
}
//This method is used to save the category sort order.

function saveCategoryReorder()
{
	
//	var vSortIds = new Array();
	var vCateIds = new Array() ;
	$('#faqTbl tr').each(function(index) {
		if (index > 0){
		$(this).attr("id"); 
		vCateIds.push($(this).attr("id"));
	//	vSortIds.push(index);
		}
	});
	var pageNumber = '${sessionScope.pageNum}';
	if (pageNumber == null || pageNumber == undefined || pageNumber == "" ) {
		document.faqcat.pageFlag.value = "false";
	} else {
		document.faqcat.pageNumber.value = pageNumber;
		document.faqcat.pageFlag.value = "true";
	}
	
	//	document.faqcat.sortOrderIds.value =  vSortIds.toString();
		document.faqcat.faqCatIds.value =  vCateIds.toString();
		document.faqcat.action = "savefaqcatereorderlst.htm";
		document.faqcat.method = "POST";
		document.faqcat.submit();

	}			
$(function() {
	var fixHelper = function(e, ui) {
	ui.children().each(function() {
	$(this).width($(this).width());
	});
	return ui;
};
$("#faqTbl tbody").sortable({
	'opacity': 0.6,
	containment: '.acrrdn-wrap',
	cursor: "move",
	tolerance: "pointer",
	helper: fixHelper /*fix for mozilla click trigger*/,
}).disableSelection();
});
$(document).ready(function() {
	$(".modal-close").on('click.popup', function() {
		$(".modal-popupWrp").hide();
		$(".modal-popup").slideUp();
		$(".modal-popup").find(".modal-bdy input").val("");
		$(".modal-popup i").removeClass("errDsply");
	});
	$(".alrtClose").click(function() {
		$('p.ctgryassoci').hide();
	});
	$(".actn-close").on("click",function() { 
		var elemnt = $(this).parents('div'); 
		var alrtId = elemnt.attr("id");
		if(alrtId == 'alrtCat')
		{
			$("#"+alrtId).hide();
			}
			else if(elemnt){
			$(this).parent('div.alertBx').hide();
		}
	 });
});

function callNextPage(pagenumber, url) 
{

	var vCateIds = new Array() ;
	$('#faqTbl tr').each(function(index) {
		if (index > 0){
		$(this).attr("id"); 
		vCateIds.push($(this).attr("id"));
	//	vSortIds.push(index);
		}
	});
	
	document.faqcat.faqCatIds.value =  vCateIds.toString();
	document.faqcat.pageNumber.value = pagenumber;
	document.faqcat.pageFlag.value = "true";
	document.faqcat.action = url;
	document.faqcat.method = "get";
	document.faqcat.submit();
}

function updateFAQCategory(vCatId) {
	var vCatName = document.getElementById("ctgryNm").value;
	$('p.dupctgry').hide();
	$('i.emptyctgry').hide();
	if ("" == $.trim(vCatName)) {
		$('i.emptyctgry').css("display", "block");
	} else {
		$.ajaxSetup({
			cache : false
		})
		$.ajax({
			type : "GET",
			url : "addupdatefaqcat.htm",
			data : {
				"catName" : vCatName,
				"cateId" : vCatId
			},
			success : function(response) {
				if (response != null && response != "" && response != "CategoryExists") {					
					$(".modal-popupWrp").hide();
					alert("Category Updated Successfully");
					window.location.href = "/HubCiti/displayfaqcat.htm";
				} else {
					$('p.dupctgry').css("display", "block");
				}
			},
			error : function(e) {
				alert("Error occured while updating FAQ category");
			}
		});
	}
}


function deleteFAQCategory(cateId, associateFlag) {
	if(associateFlag == true)
	{
		$('p.ctgryassoci').show();
	}
	else
	{
		$('p.ctgryassoci').hide();
		var msg = confirm("Are you sure you want to delete this category?");
		if (msg) {
			$.ajaxSetup({
				cache : false
			})
			$.ajax({
				type : "GET",
				url : "deletefaqcat.htm",
				data : {
					"cateId" : cateId,
				},

				success : function(response) {

					if (response == 'SUCCESS') {
						alert("Category Deleted Successfully");
						window.location.href = "/HubCiti/displayfaqcat.htm";
					}
				},
				error : function(e) {
					alert("Error occured while deleting FAQ category");

				}

			});

		}
	}	
}

function searchFAQCatOnkeyPress(event) 
{
	var keycode = (event.keyCode ? event.keyCode : event.which);
	if (keycode == '13') 
	{
		document.faqcat.action = "displayfaqcat.htm";
		document.faqcat.method = "get";
		document.faqcat.submit();
	}
	else if(event == '')
	{
		document.faqcat.action = "displayfaqcat.htm";
		document.faqcat.method = "get";
		document.faqcat.submit();
	}
}

function reorderCategory()
{
	var rCount = $('#faqTbl tr').length;
	if (rCount == 2) {
			alert("Single record cannot be reorder");
		return false;
		}
		var result = confirm("Do you want to save the reorder changes");
		if (result) {
			saveCategoryReorder();
		}
	

	}

</script>
</head>
<body onload="resizeDoc();" onresize="resizeDoc();">
	<div class="wrpr-cont relative">

		<div id="slideBtn">
			<a href="javascript:void(0);" onclick="revealPanel(this);"
				title="Hide Menu"> <img src="images/slide_off.png" width="11"
				height="28" alt="btn_off" /> </a>
		</div>


		<div id="bread-crumb">
			<ul>
				<li class="scrn-icon"><span class="icon-home">&nbsp;</span>
				</li>
				<li><a href="welcome.htm">Home</a>
				</li>
				<li class="last">Setup FAQ's</li>
			</ul>
		</div>
		<span class="blue-brdr"></span>
		<div class="content" id="login">
			<div id="menu-pnl" class="split">
				<jsp:include page="leftNavigation.jsp"></jsp:include>
			</div>
			<div class="cont-pnl split" id="">
				<div class="cont-block stretch">
					<form:form name="faqcat" commandName="faqcat">
						<form:hidden path="faqCatId" />
						<input type="hidden" name="pageNumber" />
						<input type="hidden" name="pageFlag" />
						<input type="hidden" id="sortOrderIds" name="sortOrderIds" />
						<input type="hidden" id="faqCatIds" name="faqCatIds" />

						<div class="title-bar">
							<ul class="title-actn">
								<li class="title-icon"><span class="icon-faq">&nbsp;</span>
								</li>
								<li>Setup FAQ's</li>
							</ul>
						</div>
						<div class="tabd-pnl">
							<ul class="nav-tabs">
								<li><a id="mainMenu" class="" href="displayfaq.htm">Manage
										FAQ's</a></li>
								<li><a id="subMenu" href="javascript:void(0);"
									class="rt-brdr active">Manage FAQ's Categories</a>
								</li>
							</ul>
							<div class="clear"></div>
						</div>
						<div class="cont-wrp">
							<table width="100%" border="0" cellspacing="0" cellpadding="0"
								class="zerobrdrTbl">
								<tr>
									<c:if test="${requestScope.hideSearch ne 'yes'}">
										<td width="14%"><label>Search Category</label>
										</td>
										<td width="20%">
											<div class="cntrl-grp">
												<form:input type="text" path="faqCatName"
													class="inputTxtBig"
													onkeypress="searchFAQCatOnkeyPress(event);" />
											</div>
										</td>
										<td width="10%"><a href="javascript:void(0);"> <img
												src="images/searchIcon.png" width="20" height="17"
												alt="search" title="Search category"
												onclick="searchFAQCatOnkeyPress('');" /> </a>
										</td>
									</c:if>
									<td width="40%" align="right"><input type="button"
										value="Add FAQ's Category" title="Add FAQ's Category"
										class="btn-blue" onclick="showFAQModal(this)" />
									</td>
								</tr>
							</table>


							<div class="relative">
								<p class="ctgryassoci">
									<a class="alrtClose" href="javascript:void(0);" title="close">X</a>Category
									has been associated to FAQ. Please deassociate and delete
								</p>
							</div>

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
								<c:choose>
									<c:when
										test="${sessionScope.faqCategories ne null && !empty sessionScope.faqCategories}">
										<div class="scrollTbl tblHt mrgnTop acrrdn-wrap">


											<table width="100%" cellspacing="0" cellpadding="0"
												border="0" id="faqTbl" class="grdTbl clone-hdr fxdhtTbl">
												<thead>
													<tr class="tblHdr">
														<th width="88%">Category</th>
														<th width="12%">Action</th>
													</tr>
												</thead>
												<tbody class="scrollContent ui-sortable">
													<c:forEach items="${sessionScope.faqCategories}" var="cate">
														<tr id="${cate.faqCatId}">
															<td id="${cate.faqCatName}">
																<div class="cell-wrp">${cate.faqCatName}</div></td>
															<c:choose>
																<c:when test="${cate.faqCatName ne 'General'}">
																	<td><a title="edit" href="javascript:void(0);">
																			<img height="20" width="20" class="actn-icon"
																			title="Edit" src="images/edit_icon.png"
																			onclick="showFAQModal(this)" /> </a> <a title="delete"
																		href="javascript:void(0);"> <img height="20"
																			width="20" class="actn-icon" title="Delete"
																			src="images/delete_icon.png" alt="delete"
																			onclick="deleteFAQCategory(${cate.faqCatId},${cate.associateFlag})" />
																	</a>
																	</td>
																</c:when>
																<c:otherwise>
																	<td></td>
																</c:otherwise>
															</c:choose>
														</tr>

													</c:forEach>
												</tbody>
											</table>
										</div>

									</c:when>
									<c:otherwise>
										<div class="alertBx warning mrgnTop cntrAlgn" id="alrtCat">
											<span class="actn-close" title="close"></span>
											<p class="msgBx">No FAQ's Categories Found</p>
										</div>
									</c:otherwise>
								</c:choose>
							</div>
							<div class="pagination mrgnTop">
								<page:pageTag
									currentPage="${sessionScope.pagination.currentPage}"
									nextPage="4" totalSize="${sessionScope.pagination.totalSize}"
									pageRange="${sessionScope.pagination.pageRange}"
									url="${sessionScope.pagination.url}" />
							</div>
							<c:if test="${sessionScope.faqCategories ne null && !empty sessionScope.faqCategories}">
								<div class="info-pnl mrgnTop_small">
									<label class="faqText">To change the order and sort of
										the categories, drag and drop the category to the desired
										position.Click on Save Order button to save the sort changes.</label>
									<input type="button" class="btn-blue floatR" value="Save Order"
										onclick="reorderCategory();">
								</div>
							</c:if>
					</form:form>
				</div>
			</div>

		</div>
	</div>
	</div>

	<div class="modal-popupWrp">
		<div class="modal-popup">
			<div class="modal-hdr">
				<a class="modal-close" title="close">×</a>
				<h3>Add Category</h3>
			</div>
			<div class="modal-bdy">
				<table cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td width="30%"><label class="mand">Category Name</label></td>
						<td width="56%"><div class="cntrl-grp">
								<input type="text" id="ctgryNm" class="req inputTxtBig"
									maxlength="20" />
							</div> <i class="emptyctgry">Please Enter Category Name</i>
							<p class="dupctgry">Category Name already exists</p>
						</td>
					</tr>
				</table>
			</div>
			<div class="modal-ftr">
				<p align="right">
					<input type="button" class="btn-blue" value="Save Category"
						title="Save category"> <input type="button"
						class="btn-grey" value="Clear" id="" title="Clear category"
						name="Reset" onclick="clearCategory()">
				</p>
			</div>
		</div>
	</div>
</body>
</html>
<script type="text/javascript">
	configureMenu("setupfqa");
</script>