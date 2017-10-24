<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<%@taglib prefix="page" uri="/WEB-INF/pagination.tld"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
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
		if(alrtId == 'alrtCat'){
			$("#"+alrtId).hide();
		}
		else if(elemnt){
		$(this).parent('div.alertBx').hide();
		}
	 });
	
});

function callNextPage(pagenumber, url) 
{
	document.alertcategoryform.pageNumber.value = pagenumber;
	document.alertcategoryform.pageFlag.value = "true";
	document.alertcategoryform.action = url;
	document.alertcategoryform.method = "get";
	document.alertcategoryform.submit();
}
</script>
</head>
<body onload="resizeDoc();" onresize="resizeDoc();">
	<div class="wrpr-cont relative">

		<div id="slideBtn">
			<a href="javascript:void(0);" onclick="revealPanel(this);" title="Hide Menu"> <img
				src="images/slide_off.png" width="11" height="28" alt="btn_off" />
			</a>
		</div>


		<div id="bread-crumb">
			<ul>
				<li class="scrn-icon"><span class="icon-home">&nbsp;</span></li>
				<li><a href="welcome.htm">Home</a></li>
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
					<form:form name="alertcategoryform" commandName="alertcategoryform">
						<form:hidden path="lowerLimit" />
						<input type="hidden" name="pageNumber" />
						<input type="hidden" name="pageFlag" />
						<div class="title-bar">
							<ul class="title-actn">
								<li class="title-icon"><span class="icon-alerts">&nbsp;</span>
								</li>
								<li>Setup Alerts</li>
							</ul>
						</div>
						<div class="tabd-pnl">
							<ul class="nav-tabs">
								<li><a id="mainMenu" class="" href="displayalerts.htm">Manage
										Alerts</a>
								</li>
								<li><a id="subMenu" href="javascript:void(0);" class="rt-brdr active">Manage
										Alert Category</a></li>
							</ul>
							<div class="clear"></div>
						</div>
						<div class="cont-wrp">

							<table width="100%" border="0" cellspacing="0" cellpadding="0"
								class="zerobrdrTbl">
								<tr>
												<c:choose>
								
								<c:when test ="${requestScope.searchAltCat eq 'searchAltCat'}">
								<td width="6%"><label>Search</label></td>
									<td width="20%">
									
									<div class="cntrl-grp">
											<form:input type="text" path="catName" class="inputTxtBig" onkeypress="searchAlertCategory(event)" />
										</div> <i class="emptysearh">Please Enter Search keyword</i>
										
									</td>
									<td width="10%"><a href="javascript:void(0);"><img
											src="images/searchIcon.png" width="20" height="17"
											alt="search" title="Search alert category"
											onclick="searchAlertCategory('')" /> </a>
									</td>
								
								
								</c:when>
								<c:otherwise>
								
							<c:if
										test="${sessionScope.alertcatlst ne null && !empty sessionScope.alertcatlst}">
									<td width="6%"><label>Search</label></td>
									<td width="20%">
									
									<div class="cntrl-grp">
											<form:input type="text" path="catName" class="inputTxtBig" onkeypress="searchAlertCategory(event)" />
										</div> <i class="emptysearh">Please Enter Search keyword</i>
										
									</td>
									<td width="10%"><a href="javascript:void(0);"><img
											src="images/searchIcon.png" width="20" height="17"
											alt="search" title="Search alert category"
											onclick="searchAlertCategory('')" /> </a>
									</td>
									</c:if>
								
								
								
								</c:otherwise>						
								
								
								</c:choose>	
								
								
								
								
									<td width="40%" align="right"><input type="button"
										value="Add Category" title="add" class="btn-blue"
										onclick="showModal(this)" /></td>
								</tr>
							</table>
							<div class="relative">
							<p class="ctgryassoci">
									<a class="alrtClose" href="javascript:void(0);" title="close">X</a>Alert has been
									associated to menu. Please deassociate and delete
								</p>
								</div>
							<div class="relative">
								<div class="hdrClone"></div>
								
								<c:choose>
									<c:when
										test="${sessionScope.alertcatlst ne null && !empty sessionScope.alertcatlst}">


										<div class="scrollTbl tblHt mrgnTop">

											<table width="100%" cellspacing="0" cellpadding="0"
												border="0" id="alertTbl" class="grdTbl clone-hdr fxdhtTbl">

												<thead>
													<tr class="tblHdr">
														<th width="88%">Category</th>
														<th width="12%">Action</th>
													</tr>
												</thead>
												<tbody class="scrollContent">
													<c:forEach items="${sessionScope.alertcatlst.alertCatLst}"
														var="cate">

														<tr>
															<td><a href="javascript:void(0);" id="${cate.catId}">${cate.catName}</a>
															</td>
															<td><a title="edit" href="javascript:void(0);"> <img height="20"
																	width="20" class="actn-icon" title="Edit alert category"
																	src="images/edit_icon.png" onclick="showModal(this)" />
															</a> <a title="delete" href="javascript:void(0);"> <img height="20"
																	width="20" class="actn-icon" title="Delete alert category"
																	src="images/delete_icon.png" alt="delete"
																	onclick="deleteCategory(${cate.catId})" /> </a></td>
														</tr>

													</c:forEach>
												</tbody>
											</table>
										</div>

									</c:when>
									<c:otherwise>
										<div class="alertBx warning mrgnTop cntrAlgn" id="alrtCat">
											<span class="actn-close" title="close"></span>
											<p class="msgBx">No Category found</p>
										</div>
									</c:otherwise>
								</c:choose>




							</div>
								<c:if
								test="${sessionScope.alertcatlst ne null && !empty sessionScope.alertcatlst}">
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

	<div class="modal-popupWrp">
		<div class="modal-popup">
			<div class="modal-hdr">
				<a class="modal-close" title="close">×</a>
				<h3>Add Category</h3>
			</div>
			<div class="modal-bdy">
				<table cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td width="30%">Category Name</td>
						<td width="56%"><div class="cntrl-grp">
								<input type="text" id="ctgryNm" class="req inputTxtBig"
									maxlength="20" />
							</div> <i class="emptyctgry">Please Enter Category Name</i>
							<p class="dupctgry">Category Name already exists</p></td>
					</tr>
				</table>
			</div>
			<div class="modal-ftr">
				<p align="right">
					<input type="submit" class="btn-blue" value="Save Category" title="Save category">
					<input type="reset" class="btn-grey" value="Clear" id="" title="Clear category"
						name="Reset" onclick="clearCategory()">
				</p>
			</div>
		</div>
	</div>
	
</body>
</html>
<script type="text/javascript">
	configureMenu("setupalerts");
</script>
