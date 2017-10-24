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
<title>Setup Retailer Location</title>

</head>


<body onload="resizeDoc();" onresize="resizeDoc();">
	<!--Wrapper div Starts-->
<div class="wrpr-cont relative">
		
			

				<div id="slideBtn">
					<a href="javascript:void(0);" onclick="revealPanel(this);"
						title="Hide Menu"><img src="images/slide_off.png" width="11"
						height="28" alt="btn_off" /> </a>
				</div>
				<!--Breadcrum div starts-->
				<div id="bread-crumb">
					<ul>
						<li class="scrn-icon"><span class="icon-home">&nbsp;</span>
						</li>
					
						<li><a href="welcome.htm">Home</a></li>
						
						
						<li class="last">Setup Retailer Location</li>
					</ul>
				</div>
				<!--Breadcrum div ends-->
				<span class="blue-brdr"></span>
				<!--Content div starts-->
				<div class="content" id="login">
					<div id="menu-pnl" class="split">
						<jsp:include page="leftNavigation.jsp"></jsp:include>
					</div>
					<!--Content panel div starts-->
					<div class="cont-pnl split" id="setupRtlrLoctn">
						<div class="cont-block stretch">
				<form:form name="SetupRetLocationForm"
			commandName="SetupRetLocationForm">
			<input type="hidden" name="pageNumber" />
				<form:hidden path="lowerLimit" />
			<input type="hidden" name="pageFlag" />
			<input type="hidden" name="unAssociRetLocId" />
			<input type="hidden" name="selCity" value="${sessionScope.selcity}" id="selCity">
			
			<input type="hidden" name="selzipcode" value="${sessionScope.selzipcode}" id="selzipcode"/>
		<!--	<input type="hidden" name="prevcity" value="${sessionScope.prevcity}" id="prevcity"/> -->
			
			
			<div class="clear"></div>
						
						
						
							<div class="title-bar">
								<ul class="title-actn">
									<li class="title-icon"><span class="icon-retlrLoctn">&nbsp;</span>
									</li>
									<li>Setup Retailer Location</li>
								</ul>
							</div>
							<div class="tabd-pnl">
								<ul class="nav-tabs">
									<li><a class="active" href="javascript:void(0);" 
									title="Associated Locations">Associated Locations</a></li>
									<li><a href="getunassociretlocs.htm" class="rt-brdr" title="UnAssociated Locations">
											UnAssociated Locations</a>
									</li>
								</ul>
								<div class="clear"></div>
							</div>
							<div class="cont-wrp">
								<table width="100%" border="0" cellspacing="0" cellpadding="0"
									id="rtlrAssoctn" class="cmnTbl">
									<tr>

										<!-- State list goes here --->
										<td width="5%" align="left">State</td>
										<td width="22%" align="left" valign="top">
											<div class="cntrl-grp zeroBrdr input-slct">
												<span class="lesWdth"> <form:select name="stateName" title="Select State"
														path="stateName" onchange="loadCity()" class="slctBx"
														id="rtlr-state">

														<option value="">--Select--</option>
														<c:forEach items="${sessionScope.retstatelst}"
															var="rstate">

															<c:if test="${sessionScope.selstate ne rstate.state}">
																<option value="${rstate.state}">
																	<c:out value="${rstate.stateName}"></c:out>
																</option>
															</c:if>

															<c:if test="${sessionScope.selstate eq rstate.state}">
																<option value="${rstate.state}" selected="selected">
																	<c:out value="${rstate.stateName}"></c:out>
																</option>
															</c:if>

														</c:forEach>


													</form:select> </span> <a href="#" title="Cancel" class="clrOptn rtlr-state"><img
													src="images/icon-cncl.png" width="12" height="12"
													alt="cancel" /> </a>
											</div>
										</td>

										<!-- City list goes here --->
										<td width="5%" align="right">City</td>
										<td width="25%" align="left" valign="top">
											<div id="cityDiv" class="cntrl-grp zeroBrdr input-slct">

												<span class="lesWdth"> <select name="city" title="Select City"
														 class="slctBx" id="rtlr-city"
														onchange="loadZipcodes()">
														<option value="">--Select--</option>
													</select> </span> <a href="#" title="Cancel" class="clrOptn rtlr-city"><img
													src="images/icon-cncl.png" width="12" height="12"
													alt="cancel" /> </a>
											</div>
										</td>

										<!-- Zipcode list goes here --->
										<td width="8%" rowspan="2" align="right" valign="top"><span
											class="lbl-topPdng">Zip Code</span></td>
										<td width="22%" rowspan="2">
											<div class="cntrl-grp zeroBrdr valgn-top input-mltislct"
												id="ZipcodeDiv">
												<span class="lesWdth"> <select name="postalCode" title="Clear All"
														 id="multi-slct" multiple="multiple" title="Select Zipcode"
														class="slctBx multiSlct">
														

													</select> </span> <a href="#" title="Cancel" class="clrOptn multi-slct"><img
													src="images/icon-cncl.png" width="10" height="10"
													alt="cancel" /> </a>
											</div>
										</td>
									</tr>


									<!-- Search retailer goes here placeholder="Retailers" --->
									<tr>
										<td align="left">Retailers</td>
										<td align="left" valign="top"><div
												class="cntrl-grp srch-cntrl relative">
												<form:input type="text" class="inputTxtBig"
													name="retSearchKey" path="retSearchKey"
													 id="rtlrSrch"
													onkeypress="searchRetailer(event)" />

												<span class="clear-srch" title="clear"></span>
											</div></td>
										<td align="left" valign=""><a href="javascript:void(0);"
											class="rtlrList"><img src="images/searchIcon.png"
												width="20" height="17" alt="search" title="Search Retailers"
												onclick="searchRetailer('')" /> </a> <!--<a href="#" title="cancel" class="clrOptn"><img src="images/icon-cncl.png" width="12" height="12" alt="cancel" /></a>-->
										</td>
										<td align="right" valign="top"><input type="button"
											class="btn-grey btn-mini" value="Clear All" title="Clear All" id="clearForm" />
										</td>

									</tr>
								</table>
								
								<c:if test="${requestScope.errorMessage ne null && !empty requestScope.errorMessage}">
								<div class="alertBx success mrgnTop cntrAlgn" id="retSucces" >
								<span title="close" class="actn-close"></span>
								<p class="msgBx">${requestScope.errorMessage}</p>
								</div>  
								</c:if>
								<i class="emptysearh">Please Select Retailer Location</i>
								<!-- Associated retailer location list  goes here --->
								<div class="relative" id="rtlrList">
								
								<c:choose>
								<c:when test="${sessionScope.retailerlst.cityExpLst ne null && !empty sessionScope.retailerlst.cityExpLst}">
										<div class="scrollTbl tblnoscroll mrgnTop">
										<table width="100%" cellspacing="0" cellpadding="0" border="0"
											id="mngalrtTbl" class="grdTbl clone-hdr fxdhtTbl">
											<thead>
												<tr class="tblHdr">
													<th width="30%">Retailer Name</th>
													<th width="30%">Address</th>
													<th width="15%">City</th>
													<th width="8%">State</th>
													<th width="12%">Zip Code</th>
													<th width="5%"><input type="checkbox" id="assctdLoctn" title="Check All" />
													</th>
												</tr>
											</thead>
											<tbody class="scrollContent">
												<c:if
													test="${sessionScope.retailerlst.cityExpLst ne null && !empty sessionScope.retailerlst.cityExpLst}">
													<c:forEach items="${sessionScope.retailerlst.cityExpLst}"
														var="retlst">

														<tr>
															<td class="title retName" title="${retlst.retName}">${retlst.retName}</td>
															<td class="title retName" title="${retlst.address}">${retlst.address}</td>
															<td>${retlst.city}</td>
															<td>${retlst.state}</td>
															<td>${retlst.postalCode}</td>
															<td>
														
															
															<form:checkbox type="checkbox" path="retLocIds"
																	 name="retLocIds" 
																	value="${retlst.retLocId}" />
															</td>

														</tr>

													</c:forEach>
												</c:if>

												</tbody>
										</table>
									</div>
								
								</c:when>
								
								<c:otherwise>
										<c:if test="${requestScope.errorMessage eq null && empty requestScope.errorMessage}">
								<div class="alertBx warning mrgnTop cntrAlgn" id="retClose">
											<span class="actn-close" title="close"></span>
											<p class="msgBx">No Retailer Location found</p>
										</div>
										</c:if>
								</c:otherwise>
								
						</c:choose>						

									<!-- Pagination goes here --->
									<div class="row mrgnTop">

										<c:if
											test="${sessionScope.retailerlst.cityExpLst ne null && !empty sessionScope.retailerlst.cityExpLst}">
											<div class="pagination col">
												<page:pageTag
													currentPage="${sessionScope.pagination.currentPage}"
													nextPage="4"
													totalSize="${sessionScope.pagination.totalSize}"
													pageRange="${sessionScope.pagination.pageRange}"
													url="${sessionScope.pagination.url}" />
											</div>
										

										<div class="col" align="right">
											<input type="button" class="btn-blue" value="De Associate" title="De Associate"
												onclick="deAssociateRetailer()" />
										</div>
										</c:if>
									</div>
								</div>
							</div>
		</form:form>
		<!--Wrapper div ends-->

	</div>
	</div>
	</div>



	<script type="text/javascript">
		$(document)
				.ready(
						function(e) {

					
	
				//clear values
				/* Retailer location: call clearValues function to clear the search values*/
		$(document).on('click', '.clrOptn', function() {	
			//$(".clrOptn").on('click',this,function() {
	
			clearValues(this);
		});

		/* Retailer location: clear all Values*/
		$("#clearForm").on('click',function(){
			$("#rtlrAssoctn").find("input.inputTxtBig").val("");
			$("#rtlrAssoctn .slctBx").each(function(index, element) {
	             $(this).find("option:eq(0)").prop('selected',true);
	        });
			$("#rtlrAssoctn .multiSlct option").remove();
			$(".clear-srch").stop().animate({'right':'-14px'},'fast');
		});
							
							
				
					// document.SetupRetLocationForm.retSearchKey.value=$('#tempRetKey').val();
				var tolCnt = $('input[name$="retLocIds"]:checkbox').length;
				var chkCnt = $('input[name$="retLocIds"]:checkbox:checked').length;
		if (tolCnt == chkCnt)
													$("#assctdLoctn").prop('checked',
															'checked');
													
			
						
							//for alert close
		$(".actn-close").on("click",function() { 
		var elemnt = $(this).parents('div'); 
		var alrtId = elemnt.attr("id");
		if(alrtId == 'retClose'){
			$("#"+alrtId).hide();
			$("#retSucces").hide();
		
			
			}
			else if(alrtId == 'retSucces'){
			$("#"+alrtId).hide();
							
			}			
			if(elemnt){
			$(this).parent('div.alertBx').hide();
			}
		 });
		 
		
		 
					if (null != "${sessionScope.selstate}"
								&& null != "${sessionScope.selcity}"
									&& null != "${sessionScope.selzipcode}") {
										
										
								loadCity();													
							}

							$(window)
									.load(
											function() {
												
							$(document).on(
									'click.chkAll',
									'#assctdLoctn',
									function() {
										var status = $(this).prop("checked");
										$('input[name$="retLocIds"]:checkbox').prop(
												'checked', status);
										if (!status) {
											$('input[name$="retLocIds"]:checkbox')
													.removeAttr('checked');
										}
									});

							/*Any one of the child{chkbx) uncheck ,uncheck parent chkbx*/
							$(document)
									.on(
											'click',
											'input[name$="retLocIds"]',
											function() {
												var tolCnt = $('input[name$="retLocIds"]:checkbox').length;
												var chkCnt = $('input[name$="retLocIds"]:checkbox:checked').length;
												var curChkBx = $(this).attr(
														"id");
												var grpdEle = $('#assctdLoctn');

												if (tolCnt == chkCnt)
													$(grpdEle).prop('checked',
															'checked');
												else
													$(grpdEle).removeAttr(
															'checked');
											});
											});



						});

						
						
		/* Below function is used to load the cities based on state */
		function loadCity() {
	
			//prevstate=$('#prevstate').val();
			document.SetupRetLocationForm.city.value = "";
			$("#rtlr-city option:eq(0)").prop('selected',true);
			$("select.multiSlct option").remove();
			var state = $('#rtlr-state').val();
			$.ajaxSetup({
				cache : false
			})
			$.ajax({
				type : "GET",
				url : "getcitis.htm",
				data : {
					'state' : state
					
				},

				success : function(response) {
					$('#cityDiv').html(response);
					loadZipcodes();
				},
				error : function(e) {
					alert("Error occured while  displaying Cities.");
				}
			});
		}
		
		
		/* Below function is used to load the zipcodes based on state and city*/
		function loadZipcodes() {
		
	//	prevcity = $('#prevcity').val();
	//	$("select.multiSlct option").remove();
			var state = $('#rtlr-state').val();
		
			var vfinalCity;
			vfinalCity = $('#rtlr-city').val();	
			
			$.ajaxSetup({
				cache : false
			})
			$.ajax({
				type : "GET",
				url : "getzipcodes.htm",
				data : {
					'state' : state,
					'city' : vfinalCity
					
				},

				success : function(response) {
					$('#ZipcodeDiv').html(response);
				},
				error : function(e) {
					alert("Error occured while  displaying Zip codes.");

				}
			});
		}

		/*Below method is used to search retailer location */
		function searchRetailer(event) {
			$("#retSucces").hide();
			var keyCode = (event.keyCode ? event.keyCode : event.which);
			var vState = $('#rtlr-state').val();
			var vCity = $('#rtlr-city').val();
			var vZipcode = "";
			$('#multi-slct > option:selected').each(function() {
				vZipcode = vZipcode + "," + $(this).val();
			});
	
			if(keyCode == 13)
			{

			document.SetupRetLocationForm.action = "getassociretlocs.htm";
			document.SetupRetLocationForm.method = "get";
			document.SetupRetLocationForm.submit();
			
			}else if(event == '')
				{
		
			 document.SetupRetLocationForm.action = "getassociretlocs.htm";
			 document.SetupRetLocationForm.method = "get";
			 document.SetupRetLocationForm.submit();
			
			
			 }else{
			 return true;
			 }
		}

		function callNextPage(pagenumber, url) {
		
		
			var vState = $('#rtlr-state').val();
			var vCity = $('#selCity').val();
			var vPostalcode = $('#selzipcode').val();

					
				
					$("input[name='city']").val(vCity);
					$("input[name='postalCode']").val(vPostalcode);
										
						
				var tolChkCnt=	 $.makeArray($('input[name="retLocIds"]:checkbox:checked')
							.map(function() {
								return $(this).val()
							}));
					
				if (tolChkCnt != "" ) {
				var msg = confirm("Do you want to save the changes?\n ");

				if (msg) {
									
					deAssociateRetailer();

				} else {

				}

			}

			else {

				document.SetupRetLocationForm.pageNumber.value = pagenumber;
				document.SetupRetLocationForm.pageFlag.value = "true";
				document.SetupRetLocationForm.action = "getassociretlocs.htm";
				document.SetupRetLocationForm.method = "get";
				document.SetupRetLocationForm.submit();
			}

		}

		function deAssociateRetailer() {
		
			//for un associating retailer
			var vState = $('#rtlr-state').val();
			var vCity = $('#rtlr-city').val();
			var vZipcode = "";
			$('#multi-slct > option:selected').each(function() {
				vZipcode = vZipcode + "," + $(this).val();
			});
				
			
			var vUnchkd = $.makeArray($('input[name="retLocIds"]:checkbox:checked').map(
					function() {
						return $(this).val()
					}));
					
					if(vUnchkd == '')
					{
					$('i.emptysearh').css("display", "block");
					
					}else{
				
				var unAssolist=vUnchkd;
				unAssolist = $.trim(unAssolist);
				//document.SetupRetLocationForm.retSearchKey.value=vCity;
				document.SetupRetLocationForm.unAssociRetLocId.value=unAssolist;
				document.SetupRetLocationForm.action = "deassociateret.htm";
				document.SetupRetLocationForm.method = "post";
				document.SetupRetLocationForm.submit();
			
}
		}

function testDup(){
           
var dupVal = {};
$("#multi-slct >option").each(function () {


    if(dupVal[this.text]) {                 
        $(this).remove();
    } else {
        dupVal[this.text] = this.value;
    }
});                           
}


	</script>
</body>

</body>
</html>
<script type="text/javascript">
	configureMenu("setupretailerlocation");
</script>