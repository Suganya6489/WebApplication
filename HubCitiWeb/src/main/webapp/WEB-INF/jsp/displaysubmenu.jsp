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
</head>
<body onload="resizeDoc();" onresize="resizeDoc();">
	<div id="wrpr">
		<div class="clear"></div>
		<div class="wrpr-cont relative">

			<div id="slideBtn">
				<a href="javascript:void(0);" onclick="revealPanel(this);"
					title="Hide Menu"><img src="images/slide_off.png" width="11"
					height="28" alt="btn_off" /> </a>
			</div>
			<!--Breadcrum div starts-->
			<div id="bread-crumb">
				<ul>
					<li class="scrn-icon"><span class="icon-home">&nbsp;</span></li>
					<li><a href="welcome.htm">Home</a></li>
					<li class="last">Setup Sub Menu</li>
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
				<div class="cont-pnl split" id="">
					<div class="cont-block stretch">
						<form:form name="SubMenuForm" commandName="SubMenuForm">
							<form:hidden path="lowerLimit" />
							<input type="hidden" name="pageNumber" />
							<input type="hidden" name="pageFlag" />
							<div class="title-bar">
								<ul class="title-actn">
									<li class="title-icon"><span class="icon-submenu">&nbsp;</span>
									</li>
									<li>Setup Sub Menu</li>
								</ul>
							</div>
							<p class="alertErr">
										<span></span> <a class="alrtClose" href="javascript:void(0);"
											title="Close">x</a>
									</p>
									<p class="alertInfo">
										Sub Menu is Deleted Successfully. <a class="alrtClose"
											href="javascript:void(0);" title="Close">x</a>
									</p>
							<div class="cont-wrp">
								<table width="100%" border="0" cellspacing="0" cellpadding="0"
									class="zerobrdrTbl">
									<tr>
										<c:choose>
											<c:when
												test="${requestScope.SubMSearchKey eq 'SubMSearchKey'}">
												<td width="9%"><label>Sub Menu</label></td>
												<td width="20%"><div class="cntrl-grp">
														<form:input type="text" path="searchKey"
															class="inputTxtBig" onkeypress="SearchSubMenu(event)" />
													</div></td>
												<td width="10%"><a href="#"><img
														src="images/searchIcon.png" width="20" height="17"
														alt="search" title="Search Sub Menu"
														onclick="SearchSubMenu('')"
														 /> </a></td>

												<td width="40%" align="right"><input type="button"
													name="SubMenu" value="Add Sub Menu" class="btn-blue"
													title="Add Sub Menu" id="addSubMnu"
													onclick="window.location.href='displaymenutemplate.htm?menuType=submenu'" />
												</td>
											</c:when>
											<c:otherwise>

												<c:if
													test="${sessionScope.subMenuInfoLst ne null && !empty sessionScope.subMenuInfoLst}">

													<td width="9%"><label>Sub Menu</label></td>
													<td width="20%"><div class="cntrl-grp">
															<form:input type="text" path="searchKey"
																class="inputTxtBig" onkeypress="SearchSubMenu(event)"/>
														</div></td>
													<td width="10%"><a href="#"><img
															src="images/searchIcon.png" width="20" height="17"
															alt="search" title="Search Sub Menu"
															onclick="SearchSubMenu('')"
															 /> </a></td>

												</c:if>
												<td width="40%" align="right"><input type="button"
													name="SubMenu" value="Add Sub Menu" class="btn-blue"
													title="Add Sub Menu" id="addSubMnu"
													onclick="window.location.href='displaymenutemplate.htm?menuType=submenu'" />
												</td>
											</c:otherwise>
										</c:choose>
									</tr>
								</table>



								<div class="relative">

									<!-- 	<c:if
					test="${requestScope.ErrResponseMsg ne null && !empty requestScope.ErrResponseMsg}">
					<div class="alertBx success mrgnTop cntrAlgn" id="retSucces">
						<span title="close" class="actn-close"></span>
						<p class="msgBx">${requestScope.ErrResponseMsg}</p>
					</div>
				</c:if> -->


									
									<div class="hdrClone"></div>
									<c:choose>
										<c:when
											test="${sessionScope.subMenuInfoLst ne null && !empty sessionScope.subMenuInfoLst}">
											<div class="scrollTbl tblHt mrgnTop">
												<table width="100%" cellspacing="0" cellpadding="0"
													border="0" id="mngalrtTbl"
													class="grdTbl clone-hdr fxdhtTbl">
													<thead>
														<tr class="tblHdr">
															<th width="88%">Sub Menu</th>
															<th width="12%">Action</th>
														</tr>
													</thead>
													<tbody class="scrollContent">
														<c:forEach items="${sessionScope.subMenuInfoLst }"
															var="item">
															<tr>

																<td><c:out value="${item.menuName }"></c:out>
																</td>
																<td><a title="Edit SubMenu"
																	href="updatesubmenu.htm?subMenuId=${item.menuId}"><img
																		height="20" width="20" class="actn-icon" alt="edit"
																		src="images/edit_icon.png" /> </a> <a
																	title="Delete SubMenu" href="#"
																	onclick="deleteSubMenu(${item.menuId })"><img
																		height="20" width="20" class="actn-icon" alt="delete"
																		src="images/delete_icon.png" /> </a></td>
															</tr>
														</c:forEach>
													</tbody>
												</table>
											</div>
										</c:when>
										<c:otherwise>
											<div class="alertBx warning mrgnTop cntrAlgn"
												id="evntCatClose">
												<span class="actn-close" title="close"></span>
												<p class="msgBx">No SubMenu Found</p>
											</div>


										</c:otherwise>
									</c:choose>
								</div>
								<c:if
									test="${sessionScope.subMenuInfoLst ne null && !empty sessionScope.subMenuInfoLst}">
									<div class="pagination mrgnTop">
										<page:pageTag
											currentPage="${sessionScope.pagination.currentPage}"
											nextPage="4" totalSize="${sessionScope.pagination.totalSize}"
											pageRange="${sessionScope.pagination.pageRange}"
											url="${sessionScope.pagination.url}" />
									</div>
								</c:if>
							</div>
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

	$(document).ready( function() {

		$(".alrtClose").click(function() {
								$('p.alertErr').hide();
								$('p.alertInfo').css("display", "none");
							});
		$(".actn-close").click(function() {
								
				var elemnt = $(this).parents('div'); 
				var alrtId = elemnt.attr("id");
				if(alrtId == 'evntCatClose'){
					$("#"+alrtId).hide();
					}
					else if(elemnt){
					$(this).parent('div.alertBx').hide();
					}
				 });	
								
							});
	function SearchSubMenu(event)
	{
		var keycode = (event.keyCode ? event.keyCode : event.which);
		
		if (keycode == 13) {
			$('p.alertErr').hide();
			$('p.alertInfo').css("display", "none");
			document.SubMenuForm.action = "displaysubmenu.htm";
			document.SubMenuForm.method = "get";
			document.SubMenuForm.submit();

		}else if (event == '') {
		$('p.alertErr').hide();
		$('p.alertInfo').css("display", "none");
		document.SubMenuForm.action = "displaysubmenu.htm";
		document.SubMenuForm.method = "get";
		document.SubMenuForm.submit();

	}else{
	return true;
		}
	}
	function callNextPage(pagenumber, url) 
	{
		$('p.alertErr').hide();
		$('p.alertInfo').css("display", "none");
		document.SubMenuForm.pageNumber.value = pagenumber;
		document.SubMenuForm.pageFlag.value = "true";
		document.SubMenuForm.action = url;
		document.SubMenuForm.method = "get";
		document.SubMenuForm.submit();
	}
	
		function deleteSubMenu(vSubMenuId) {
		
			$('p.alertErr').hide();
			$('p.alertInfo').css("display", "none");

			delSubMenu = confirm("Are you sure you want to delete this SubMenu");
			if(delSubMenu == true)
			{
			
			$.ajaxSetup({
				cache : false
			})
			$
					.ajax({
						type : "GET",
						url : "delsubmenu.htm",
						data : {
							"submenuid" : vSubMenuId
						},

						success : function(response) {
							

							if (response == 'SUCCESS') {

								//$('p.alertInfo').show();
								alert("SubMenu is Deleted Successfully.");
							window.location.href = "/HubCiti/displaysubmenu.htm";

							} else {
								$('p.alertErr span').text(response);
								$('p.alertErr').show();
								
							}
						},
						error : function(e) {
							alert("Error occured while deleting Sub Menu");

						}

					});
			}

		}
	</script>
</body>


</html>

<script type="text/javascript">
	configureMenu("setupsubmenu");
</script>