<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<%@taglib prefix="page" uri="/WEB-INF/pagination.tld"%>
<script type="text/javascript" src="/HubCiti/scripts/jquery-1.10.2.js"></script>
<script src="/HubCiti/scripts/jquery-ui.js"></script>
<%@ page
	import="java.util.List,com.hubciti.common.pojo.FAQ,com.hubciti.common.pojo.FAQDetails"%>

<script type="text/javascript">

$(function() {
	$( ".cstm-accrdn").sortable({
		'opacity': 0.6,
		containment: '.acrrdn-wrap',
		helper: 'clone' /*fix for mozilla click trigger*/,
		cursor: "move",
		tolerance: "pointer",
		update: function( event, ui ) {
		var stringDiv = "";
			$(".cstm-accrdn").children().each(function(i) {
				var li = $(this);
				liIndex = li.index();
				li.find('span.cnt').text(liIndex+1+".");
			});
	}
	});
	});

function searchFAQs() 
{
	document.faqs.faqLowerLimit.value = 0;
	document.faqs.action = "displayfaq.htm";
	document.faqs.method = "get";
	document.faqs.submit();

}



function searchFAQsOnkeyPress(event) 
{
	var keycode = (event.keyCode ? event.keyCode : event.which);
	if (keycode == '13') 
	{
		document.faqs.faqLowerLimit.value = 0;
		document.faqs.action = "displayfaq.htm";
		document.faqs.method = "get";
		document.faqs.submit();
	}
}

function callNextPage(pagenumber, url) 
{

	var vCateIds = new Array();
	var vSortIds = new Array();

var vqstnIds = new Array();
$('ul.cstm-accrdn').each(function (index) {
  var ulId = $(this).attr('id');
  vCateIds.push(ulId);

  $('#' + vCateIds[index]).find('li').each(function (index) {
	vSortIds.push(index+1);
  
    var qstn = $(this).attr('id');
    vqstnIds.push(qstn);
  });
  vqstnIds.push('!~~!');
  vSortIds.push('!~~!');
});

	document.faqs.sortOrderIds.value = vSortIds.toString();
	document.faqs.faqCatIds.value =  vCateIds.toString();
	document.faqs.qstnIds.value =  vqstnIds.toString();
	document.faqs.pageNumber.value = pagenumber;
	document.faqs.pageFlag.value = "true";
	document.faqs.action = url;
	document.faqs.method = "get";
	document.faqs.submit();
}

function addFAQs()
{
	document.faqs.faqLowerLimit.value = 0;
	document.faqs.action = "addfaq.htm";
	document.faqs.method = "get";
	document.faqs.submit();
}



function editFAQs(faqId)
{
	document.faqs.faqLowerLimit.value = '${requestScope.lowerLimit}';
	document.faqs.faqID.value = faqId;
	document.faqs.action = "editfaq.htm";
	document.faqs.method = "get";
	document.faqs.submit();
}

function deleteFAQs(alertId) 
{
	var r = confirm("Are you sure you want to delete this FAQ ?");
	if(r == true)
	{
		document.faqs.faqLowerLimit.value = '${requestScope.lowerLimit}';
		document.faqs.faqID.value = alertId;
		document.faqs.action = "deletefaq.htm";
		document.faqs.method = "POST";
		document.faqs.submit();
	}
}

function reorderQuestion()
{
	var rCount = $('ul.cstm-accrdn li').length;
	if (rCount == 1) {
			alert("Single record cannot be reorder");
		return false;
		}
		var result = confirm("Do you want to save the reorder changes");
		if (result) {
			saveFaqReorder();
		}
	

	}
	//This method is used to save faq order.
function saveFaqReorder()
{
	var vCateIds = new Array();
	var vSortIds = new Array();

var vqstnIds = new Array();
$('ul.cstm-accrdn').each(function (index) {
  var ulId = $(this).attr('id');
  vCateIds.push(ulId);

  $('#' + vCateIds[index]).find('li').each(function (index) {
	vSortIds.push(index+1);
  
    var qstn = $(this).attr('id');
    vqstnIds.push(qstn);
  });
  vqstnIds.push('!~~!');
  vSortIds.push('!~~!');
});

	var pageNumber = '${sessionScope.pageNum}';

	if (pageNumber == null || pageNumber == undefined || pageNumber == "" ) {
		document.faqs.pageFlag.value = "false";
	} else {
		document.faqs.pageNumber.value = pageNumber;
		document.faqs.pageFlag.value = "true";
	}
	
		document.faqs.sortOrderIds.value = vSortIds.toString();
		document.faqs.faqCatIds.value =  vCateIds.toString();
		document.faqs.qstnIds.value =  vqstnIds.toString();
		
		document.faqs.action = "savefaqreorderlist.htm";
		document.faqs.method = "POST";
		document.faqs.submit();
	
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
					<div class="title-bar">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-faq">&nbsp;</span>
							</li>
							<li>Setup FAQ's</li>
						</ul>
					</div>
					<div class="tabd-pnl">
						<ul class="nav-tabs">
							<li><a id="mainMenu" class="active" href="javascript:void(0);"
								title="Manage FAQ's">Manage FAQ's</a>
							</li>
							<li><a id="subMenu" href="displayfaqcat.htm" class="rt-brdr"
								title="Manage FAQ's Categories">Manage FAQ's Categories</a></li>
						</ul>
						<div class="clear"></div>
					</div>
					<div class="cont-wrp mrgnBtm_small">
						<form:form name="faqs" id="faqs" commandName="faqs">
							<form:hidden path="faqID" />
							<form:hidden path="faqLowerLimit" />
							<input type="hidden" name="pageNumber" />
							<input type="hidden" name="pageFlag" />
							<input type="hidden" name="faqCatIds" />
							<input type="hidden" name="qstnIds" />
							<input type="hidden" name="sortOrderIds" id="sortOrderIds" />



							<table width="100%" border="0" cellspacing="0" cellpadding="0"
								class="zerobrdrTbl">
								<tr>
									<c:if test="${requestScope.hideSearch ne 'yes'}">
										<td width="6%"><label>Search</label></td>
										<td width="20%">
											<div class="cntrl-grp">
												<form:input path="faqSearchKey" cssClass="inputTxtBig"
													onkeypress="searchFAQsOnkeyPress(event)" />
											</div></td>
										<td width="10%"><a href="#" onclick="searchFAQs();">
												<img src="images/searchIcon.png" width="20" height="17"
												alt="search" title="search" /> </a></td>
									</c:if>
									<td width="40%" align="right"><input type="button"
										value="Add FAQ" class="btn-blue" onclick="addFAQs();"
										title="Add FAQ" /></td>
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
						<c:if
							test="${sessionScope.faqDetails ne null && !empty sessionScope.faqDetails}">
							<div class="scrollTbl mrgnTop acrrdn-wrap">
								<c:set var="activeCount" value="1"></c:set>
								<c:forEach items="${sessionScope.faqDetails}" var="faqDetails">
									<c:set var="count" value="1"></c:set>
									<h2 class="grpd-title">${faqDetails.categoryName}</h2>
									<ul class="cstm-accrdn" id="${faqDetails.faqcateId}">
										<c:forEach items="${faqDetails.faqs}" var="faqs">
											<c:choose>
												<c:when test="${activeCount eq 1}">
													<li class="active" id="${faqs.faqID}">
														<h4 class="sub-title">
															<span class="cnt">${count}.</span> <span class="lst-txt"><c:out value="${faqs.question}" escapeXml='true'/></span>
															<span class="lst-cntrl"> <a href="#" title="edit"
																class="lst-edit" onclick="editFAQs(${faqs.faqID})">
																	&nbsp;</a> <a href="#" title="delete" class="lst-delete"
																onclick="deleteFAQs(${faqs.faqID})"></a> </span>
														</h4>
														<div class="lst-cont">${faqs.answer}</div></li>
												</c:when>
												<c:otherwise>
													<li class="" id="${faqs.faqID}">
														<h4 class="sub-title">
															<span class="cnt">${count}.</span> <span class="lst-txt"><c:out value="${faqs.question}" escapeXml='true'/></span>
															<span class="lst-cntrl"> <a href="#" title="edit"
																class="lst-edit" onclick="editFAQs(${faqs.faqID})">&nbsp;</a>
																<a href="#" title="delete" class="lst-delete"
																onclick="deleteFAQs(${faqs.faqID})"></a> </span>
														</h4>
														<div class="lst-cont">${faqs.answer}</div></li>
												</c:otherwise>
											</c:choose>
											<c:set var="activeCount" value="${activeCount + 1}"></c:set>
											<c:set var="count" value="${count + 1}"></c:set>
										</c:forEach>
									</ul>
								</c:forEach>
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
							test="${sessionScope.faqDetails ne null && !empty sessionScope.faqDetails}">
							<div class="info-pnl mrgnTop_small">
								<label class="faqText">To change the order and sort of
									the question in categories, drag and drop the question to the
									desired position.Click on Save Order button to save the sort
									changes.</label> <input type="button" class="btn-blue floatR"
									value="Save Order" onclick="reorderQuestion();">
							</div>

						</c:if>
						<!-- 	<c:if test="${requestScope.hideSearch ne 'yes'}"></c:if> -->


					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script>
$(document).ready(function () {
	$("ul.cstm-accrdn").on('click','li',function() {
		$("li div.lst-cont").css("display","none");	
		$("ul.cstm-accrdn li").removeClass("active").find('div.lst-cont').css("display","none");	
		$(this).addClass("active").find('div.lst-cont').css("display","block");
	});	
	// 1. variables
	var $container = $('.acrrdn-wrap');
	var $headers =$container.find('h2.grpd-title');
	
	var zIndex = 2;
	var containerTop = $container.offset().top + parseInt($container.css('marginTop')) + parseInt($container.css('borderTopWidth'));
	
	var $stickyHeader = $headers.filter(':first').clone();
	// 2. absolute position on the h2, and fix the z-index so they increase
	$headers.each(function () {
	// set position absolute, etc
	var $header = $(this), height = $header.outerHeight(), width = $header.outerWidth();
	zIndex += 2;
	$header.css({
	position: 'absolute',
	width: $header.width(),
	zIndex: zIndex
	});
	// create the white space
	var $spacer = $header.after('<div />').next();
	$spacer.css({
	height: height,
	width: width
	});
	});
	// 3. bind a scroll event and change the text of the take heading
	$container.scroll(function () {
	$headers.each(function () {
	var $header = $(this);
	var top = $header.offset().top;
	if (top < containerTop) {
	$stickyHeader.text($header.text());
	$stickyHeader.css('zIndex', parseInt($header.css('zIndex'))+1);
	}
	});
	});
	// 4. initialisation
	$container.wrap('<div class="box" />');
	$stickyHeader.css({ zIndex: 1, position: 'absolute', width: $headers.filter(':first').width() });
	$container.before($stickyHeader.text($headers.filter(':first').text()));
	$stickyHeader.addClass("stickyHdr");
	}); 
</script>

<script type="text/javascript">
	configureMenu("setupfqa");
</script>