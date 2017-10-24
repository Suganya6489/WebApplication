<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<%@taglib prefix="page" uri="/WEB-INF/pagination.tld"%>
<script type="text/javascript">
	$(document).ready(function(){
		var dealList = "${sessionScope.dealList}";
		var searchKey = $("#dealSearchKey").val();
		if((null === dealList || "" === dealList) && searchKey === "") {
			$(".zerobrdrTbl").hide();
		}
		var dealOfDay = $("#dealId").val();
		if(null !== dealOfDay && "" !== dealOfDay) {
			$("#deal-" + dealOfDay).find("img").attr("src", "images/dealActive_icon.png");		
		}		
		if($(".hdrClone span").length) {
			$(".hdrClone").find("span:gt(2)").css("text-indent","8px");
		} else {
			$(".hdrClone").find("span:gt(2)").css("text-indent","0px");
		}	
	});
</script>
<script type="text/javascript">

	function callNextPage(pagenumber, url) 
	{
		document.deals.pageNumber.value = pagenumber;
		document.deals.pageFlag.value = "true";
		document.deals.action = url;
		document.deals.method = "get";
		document.deals.submit();
	}	
	
	function searchDeals() 
	{
		document.deals.lowerLimit.value = 0;
		document.deals.action = "displaydeals.htm";
		document.deals.method = "get";
		document.deals.submit();

	}

	function searchDealsOnkeyPress(event) 
	{
		var keycode = (event.keyCode ? event.keyCode : event.which);
		if (keycode == '13') 
		{
			document.deals.lowerLimit.value = 0;
			document.deals.action = "displaydeals.htm";
			document.deals.method = "get";
			document.deals.submit();
		}
	}	
	
</script>
<div id="wrpr">
	<div class="clear"></div>
	<div class="wrpr-cont relative">
		<div id="slideBtn">
			<a href="#" onclick="revealPanel(this);" title="Hide Menu"><img
				src="images/slide_off.png" width="11" height="28" alt="btn_off" /></a>
		</div>
		<!--Breadcrum div starts-->
		<div id="bread-crumb">
			<ul>
				<li class="scrn-icon"><span class="icon-home">&nbsp;</span></li>
				<li><a href="welcome.htm">Home</a></li>
				<li class="last">Setup Deals</li>
			</ul>
		</div>
		<!--Breadcrum div ends-->
		<span class="blue-brdr"></span>
		<!--Content div starts-->
		<div class="content" id="">
			<!--Left Menu div starts-->
			<div id="menu-pnl" class="split">
				<jsp:include page="leftNavigation.jsp"></jsp:include>
			</div>
			<!--Left Menu div ends-->
			<!--Content panel div starts-->
			<div class="cont-pnl split" id="equalHt">
				<div class="cont-block rt-brdr stretch">
					<div class="title-bar">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-deal">&nbsp;</span></li>
							<li>Setup Deal Of The Day</li>
						</ul>
					</div>
					<div class="tabd-pnl">
						<ul class="nav-tabs">
							<li><a class="" href="javascript:void(0);"
								onclick="displayCoupons();">Coupon</a></li>
							<li><a class="active" href="#">Hot Deal</a></li>
							<li><a class="brdr-rt" href="javascript:void(0);"
								onclick="displaySpecials();">Special Page</a></li>
						</ul>
						<div class="clear"></div>
					</div>
					<div class="cont-wrp">
						<form:form name="deals" id="deals" commandName="deals">
							<form:hidden path="dealId" />
							<form:hidden path="dealName" />
							<form:hidden path="lowerLimit" />
							<input type="hidden" name="pageNumber" />
							<input type="hidden" name="pageFlag" />

							<table width="100%" border="0" cellspacing="0" cellpadding="0"
								class="zerobrdrTbl">
								<tr>
									<td width="6%">Deal</td>
									<td width="20%"><div class="cntrl-grp">
											<form:input path="dealSearchKey" class="inputTxtBig" onkeypress="searchDealsOnkeyPress(event)"/>
										</div></td>
									<td width="10%"><a href="#"><img
											src="images/searchIcon.png" width="20" height="17"
											alt="search" title="search deals" onclick="searchDeals()"/></a></td>
									<td width="40%" align="right"></td>
								</tr>
							</table>
							<c:choose>
								<c:when test="${sessionScope.dealList ne null}">
									<div class="relative">
										<div class="hdrClone"></div>
										<div class="scrollTbl tblHt mrgnTop">
											<table width="100%" cellspacing="0" cellpadding="0"
												border="0" id="mngevntTbl" class="grdTbl clone-hdr fxdhtTbl txtAlgnFix">
												<thead>
													<tr class="tblHdr">
														<th width="25%">Name</th>
														<th width="25%">Description</th>
														<th width="11%">Sale Price</th>
														<th width="8%">Price</th>
														<th width="11%">Start Date</th>
														<th width="11%">End Date</th>
														<th width="9%">Action</th>
													</tr>
												</thead>
												<tbody class="scrollContent">
													<c:forEach items="${sessionScope.dealList}" var="deal">
														<tr>
															<td>${deal.dealName}</td>
															<td><div class="cell-wrp">${deal.dealDescription}</div></td>
															<td>${deal.salePrice}</td>
															<td>${deal.price}</td>
															<td>${deal.startDate}</td>
															<td>${deal.endDate}</td>
															<td align="center" id="deal-${deal.dealId}"><a href="#"
																title="deal of the day"
																onclick="saveDealOfTheDay(${deal.dealId});"><img
																	src="images/deal_icon.png" width="24" height="24"
																	alt="deal of the day" /></a></td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
										<div class="pagination mrgnTop">
											<page:pageTag
												currentPage="${sessionScope.pagination.currentPage}"
												nextPage="4"
												totalSize="${sessionScope.pagination.totalSize}"
												pageRange="${sessionScope.pagination.pageRange}"
												url="${sessionScope.pagination.url}" />
										</div>
									</div>
								</c:when>
								<c:otherwise>
									<div class="alertBx warning mrgnTop cntrAlgn">
										<span class="actn-close" title="close"></span>
										<p class="msgBx">
											<c:out value="${requestScope.errMsg}" />
										</p>
									</div>
								</c:otherwise>
							</c:choose>
						</form:form>
					</div>
				</div>
			</div>
			<!--Content panel div ends-->
		</div>
		<!--Content div ends-->
	</div>
</div>
<script type="text/javascript">
	configureMenu("setupdeals");
</script>