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

	$(document)
			.ready(
					function() {
					
						$(window).load(function() {
						var tolCnt = $('input[name$="retLocIds"]:checkbox').length;   
						
						
						var chkCnt = $('input[name$="retLocIds"]:checkbox:checked').length;   
						document.CityExperienceForm.totChkCnt.value =chkCnt ;
						if(tolCnt==chkCnt)   {    
					
						$("#ctyexpChkAll").prop('checked','checked').trigger('change.trgrclone');   
						$("#ctyexpChkAll").prop('checked',true);   					
						}
						});
   
   
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
						/*	$(".actn-close").click(function() {
						 alert('event alert');
						 var vEid = $(this).attr('div.alertBx.id').value;
						 alert(vEid);
						 $(this).parent('div.alertBx').remove();

						 });*/

						$(".actn-close").on("click", function() {
							var elemnt = $(this).parents('div');
							var alrtId = elemnt.attr("id");
							if (alrtId == 'ctyClose') {
								$("#" + alrtId).hide();
								//	document.CityExperienceForm.retSearchKey.value=null;
								//	document.CityExperienceForm.action = "displaycityexp.htm";
								//	document.CityExperienceForm.method = "GET";
								//	document.CityExperienceForm.submit(); 
							} else if (elemnt) {
								$(this).parent('div.alertBx').hide();
							}
						});


											$(".hdrClone").find("span:gt(0)")
													.css("text-indent", "5px");
											$(".hdrClone").find("span:last")
													.css("text-indent", "10px");
											$(".hdrClone")
													.find("span:last")
													.find(
															"input[type='checkbox']")
													.removeAttr("id").attr(
															"id", "cloneId");

											$(document)
													.on(
															'change.trgrclone',
															'#ctyexpChkAll',
															function() {
																var curChkd = $(
																		this)
																		.prop(
																				"checked");
																var status = $(
																		this)
																		.prop(
																				"checked");
																var chkdvals = [];
																
																$("#cloneId")
																		.prop(
																				"checked",
																				curChkd);
																$(
																		'input[name$="retLocIds"]:checkbox')
																		.prop(
																				'checked',
																				status);
																if (!status) {
																	$(
																			'input[name$="retLocIds"]:checkbox')
																			.removeAttr(
																					'checked');
																}
																/* get all chkbx ids */
																$(
																		"input[name='retLocIds']:checkbox")
																		.each(
																				function() {
																					chkdvals
																							.push($(
																									this)
																									.attr(
																											"value"));
																				});
																  

																//deleteAllRetLocId(chkdvals);
															});
											$(document)
													.on(
															'change.trgrPrnt',
															'#cloneId',
															function() {
																var cloneChkd = $(
																		this)
																		.prop(
																				"checked");
																$(
																		"#ctyexpChkAll")
																		.prop(
																				"checked",
																				cloneChkd);
																$(
																		'input[name$="retLocIds"]:checkbox')
																		.prop(
																				'checked',
																				cloneChkd);
															});

											/*Any one of the child{chkbx) uncheck ,uncheck parent chkbx*/
											$(document)
													.on(
															'click',
															'input[name$="retLocIds"]:checkbox',
															function() {

																var tolCnt = $('input[name$="retLocIds"]:checkbox').length;
																var chkCnt = $('input[name$="retLocIds"]:checked').length;

																var curChkBx = $(
																		this)
																		.attr(
																				"value");

													
																if (tolCnt == chkCnt) {

																	$(
																			'#ctyexpChkAll')
																			.prop(
																					'checked',
																					'checked');
																	$(
																			'#cloneId')
																			.prop(
																					'checked',
																					'checked');
																} else {
																	$(
																			'#ctyexpChkAll')
																			.removeAttr(
																					'checked');
																	$(
																			'#cloneId')
																			.removeAttr(
																					'checked');
																}
															});

										

						var searchKey = $("#retSearchKey").val();

						if (searchKey == "") {

							$("#searchResTxt")
									.text(
											"Please Search and Associate Retail Location");

						} else {
							$("#searchResTxt")
									.text("No Retail Location Found ");
						}

					});

	function callNextPage(pagenumber, url) {
		
		$('i.emptycitiexp').hide();
		$('i.emptysearh').hide();
		$('i.emptyctgry').hide();
		var hidChdCnt = document.CityExperienceForm.totChkCnt.value;
		var tolChkCnt = $('input[name$="retLocIds"]:checkbox:checked').length;   
		var vCityExpName = document.CityExperienceForm.cityExpName.value;
	
		//for checked check box.
		var chkd = $.makeArray($('input[name="retLocIds"]:checkbox:checked')
                          .map(function() { return $(this).val() }));
	
			//for unchecked check box.
		var unchkd = $.makeArray($('input[name="retLocIds"]:checkbox:not(:checked)')
                          .map(function() { return $(this).val() }));
	
		
		document.CityExperienceForm.unAssociRetLocId.value=unchkd;
		if (vCityExpName == '') {

			$('i.emptycitiexp').css("display", "block");
		}else 
		{
		
		if(hidChdCnt != tolChkCnt )
		{
	//	var msg = confirm("Some of the records are modified,you want to move without saving these changes?\n ");
	var msg = confirm("Do you want to save the changes?\n ");

		if(msg)
		{
			document.CityExperienceForm.pageNumber.value = pagenumber;
			document.CityExperienceForm.pageFlag.value = "true";
			document.CityExperienceForm.action = "saveretloc.htm";
			document.CityExperienceForm.method = "post";
			document.CityExperienceForm.submit();
		
		}else{
		
		
		}
		
		
		} 
		
		else {

			document.CityExperienceForm.pageNumber.value = pagenumber;
			document.CityExperienceForm.pageFlag.value = "true";
			document.CityExperienceForm.action = "saveretloc.htm";
			document.CityExperienceForm.method = "post";
			document.CityExperienceForm.submit();
		}

	}
}

	function searchRetLoction(event) {
		var searchKey = document.CityExperienceForm.retSearchKey.value;
		$('i.emptysearh').hide();
		$('i.emptycitiexp').hide();
		$('i.emptyctgry').hide();
		var keycode = (event.keyCode ? event.keyCode : event.which);

		if (keycode == 13) {

		/*	if (searchKey == '') {

				$('i.emptysearh').css("display", "block");
				$("#ctyClose").hide();

			} else { */

				document.CityExperienceForm.action = "displaycityexp.htm";
				document.CityExperienceForm.method = "GET";
				document.CityExperienceForm.submit();
			//}
		} else if (event == '') {

		/*	if (searchKey == '') {
				$('i.emptysearh').css("display", "block");
				$("#ctyClose").hide();
				return true;

			}*/
			document.CityExperienceForm.action = "displaycityexp.htm";
			document.CityExperienceForm.method = "GET";
			document.CityExperienceForm.submit();

		} else {
			return true;
		}
	}
	function saveCityExpRetLocs() {
	
		var vCityExpName = document.CityExperienceForm.cityExpName.value;
		var selval = '';
		var unchkd = $("input[name$='retLocIds']:checkbox:not(:checked)").map(function() {    
		return this.value;     }).get();
		document.CityExperienceForm.unAssociRetLocId.value=unchkd;
				
	/*	$("input[name='retLocIds']:checked").each(function() {

			selval = $(this).attr("value");

		}); */
		$('i.emptycitiexp').hide();
		$('i.emptysearh').hide();
		$('i.emptyctgry').hide();

		if (vCityExpName == '') {

			$('i.emptycitiexp').css("display", "block");
			//	event.preventDefault();
		} //else if (selval == '') {

		//	$('i.emptyctgry').css("display", "block");
			//event.preventDefault();
		//} 
		else {

			document.CityExperienceForm.action = "saveretloc.htm";
			document.CityExperienceForm.method = "post";
			document.CityExperienceForm.submit();

		}
	}

	function deleteRetLocId(retlocIds) {

		$.ajaxSetup({
			cache : false
		})
		$.ajax({
			type : "GET",
			url : "deleteretloc.htm",
			data : {
				"retLocId" : retlocIds,
			},

			success : function(response) {

			},
			error : function(e) {
				alert("Error occured while  deleting retail location Id");

			}

		});
	}

	function deleteAllRetLocId(retLocId) {
		document.CityExperienceForm.retLocAssId.value = retLocId;
		document.CityExperienceForm.action = "deleteallretloc.htm";
		document.CityExperienceForm.method = "post";
		document.CityExperienceForm.submit();

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
				<li class="scrn-icon"><span class="icon-home">&nbsp;</span></li>
				<li>Home</li>
				<li class="last">Setup Experience</li>
			</ul>
		</div>
		<span class="blue-brdr"></span>
		<div class="content" id="login">
			<div id="menu-pnl" class="split">
				<jsp:include page="leftNavigation.jsp"></jsp:include>
			</div>
			<div class="cont-pnl split" id="">
				<div class="cont-block stretch">
					<form:form name="CityExperienceForm"
						commandName="CityExperienceForm">
						<form:hidden path="lowerLimit" />
							<form:hidden path="totChkCnt" />
						<!-- <form:hidden path="retLocId" />  -->
						<form:hidden path="actionType" />
						<form:hidden path="unAssociRetLocId" />

						<input type="hidden" name="pageNumber" />
						<input type="hidden" name="pageFlag" />
						<div class="title-bar">
							<ul class="title-actn">
								<li class="title-icon"><span class="icon-experience">&nbsp;</span>
								</li>
								<li>Setup Experience</li>
							</ul>
						</div>
						<div class="tabd-pnl">
							<ul class="nav-tabs">

								<li><a id="mainMenu" href="displaycityexp.htm"
									class="active">Manage Experience</a>
								</li>
								<li><a id="subMenu" href="displayfilters.htm" class="rt-brdr">Manage
										Filters</a></li>
							</ul>
							<div class="clear"></div>
						</div>
						<div class="cont-wrp">
							<table width="100%" border="0" cellspacing="0" cellpadding="0"
								class="cmnTbl">

								<tr>
									<td>Citi Experience Name</td>
									<td><div class="cntrl-grp">
											<form:input type="text" class="inputTxtBig"
												path="cityExpName" value="${sessionScope.cityExpName}" />
										</div> <i class="emptycitiexp">Please Enter City Experience Name</i>
									</td>
									<td>&nbsp;</td>

								</tr>

								<tr>
									<td width="19%"><label>Search Retailers</label>
									</td>
									<td width="31%"><div class="cntrl-grp">
											<form:input type="text" class="inputTxtBig"
												path="retSearchKey" onkeypress="searchRetLoction(event)" />
										</div> <i class="emptysearh">Please Enter Search keyword</i>
									</td>
									<td><a href="javascript:void(0);"
										onclick="searchRetLoction('')" class="rtlrList"><img
											src="images/searchIcon.png" width="20" height="17"
											alt="search" title="Search Retailer" /> </a>
									</td>
								</tr>
							</table>


							<div class="relative">
								<div class="hdrClone"></div>
								<p class="ctgryassoci">
									<a class="alrtClose" href="javascript:void(0);" title="close">x</a>Event
									has been associated to menu. Please deassociate and delete
								</p>



								<c:choose>
									<c:when
										test="${sessionScope.cityexplst.cityExpLst ne null && !empty sessionScope.cityexplst.cityExpLst}">
										<i class="emptyctgry">Please select Retail Location</i>
										<div class="scrollTbl tblHt mrgnTop">

											<table width="100%" cellspacing="0" cellpadding="0"
												border="0" id="alertTbl" class="grdTbl clone-hdr fxdhtTbl">

												<thead>
													<tr class="tblHdr">
														<th width="21%">Retailer Name</th>
														<th width="26%">Address</th>
														<th width="16%">City</th>
														<th width="15%">State</th>
														<th width="13%">Zip Code</th>
														<th width="9%"><input type="checkbox"
															id="ctyexpChkAll" />
														</th>
													</tr>
												</thead>
												<tbody class="scrollContent">


													<c:forEach items="${sessionScope.cityexplst.cityExpLst}"
														var="cityExp">

														<tr>
															<td>${cityExp.retName}</td>
															<td>${cityExp.address}</td>
															<td>${cityExp.city}</td>
															<td>${cityExp.state}</td>
															<td>${cityExp.postalCode}</td>
															<td><c:choose>
																	<c:when test="${cityExp.associate eq 1}">
																		<form:checkbox type="checkbox" path="retLocIds"
																			checked="true" name="retLocIds"
																			value="${cityExp.retLocId}" />
																	</c:when>
																	<c:otherwise>
																		<form:checkbox type="checkbox" path="retLocIds"
																			name="retLocIds" value="${cityExp.retLocId}" />
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
										<div class="alertBx warning mrgnTop cntrAlgn" id="ctyClose">
											<span class="actn-close" title="close"></span>
											<p class="msgBx" id="searchResTxt">No Retail Location
												found</p>
										</div>
									</c:otherwise>
								</c:choose>



							</div>

							<c:if
								test="${sessionScope.cityexplst ne null && !empty sessionScope.cityexplst}">
								<div class="pagination mrgnTop">
									<page:pageTag
										currentPage="${sessionScope.pagination.currentPage}"
										nextPage="4" totalSize="${sessionScope.pagination.totalSize}"
										pageRange="${sessionScope.pagination.pageRange}"
										url="${sessionScope.pagination.url}" />
								</div>
							</c:if>
							<c:if
								test="${sessionScope.cityexplst.cityExpLst ne null && !empty sessionScope.cityexplst.cityExpLst}">
								<div class="col" align="right">
									<input type="button" class="btn-blue" value="Associate" title="Associate Retail Locations"
										onclick="saveCityExpRetLocs()" />
								</div>
							</c:if>
					</form:form>
				</div>
			</div>
		</div>

	</div>
	</div>


</body>
</html>

<script type="text/javascript">
	configureMenu("setupexperience");
</script>