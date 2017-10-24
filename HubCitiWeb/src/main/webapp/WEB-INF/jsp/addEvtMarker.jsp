<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<%@taglib prefix="page" uri="/WEB-INF/pagination.tld"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add Event Markers</title>
<script type="text/javascript" src="scripts/jquery-1.10.2.js"></script>
<script type="text/javascript" src="scripts/jquery-ui.js"></script>
<script type="text/javascript" src="scripts/jquery-ui-min.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/jquery.form.js"></script>
<script src="/HubCiti/scripts/ckeditor/ckeditor.js"></script>
<script src="/HubCiti/scripts/ckeditor/config.js"></script>
<script type="text/javascript" src="scripts/web.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/bubble-tooltip.js"></script>
<link rel="stylesheet" href="/HubCiti/styles/jquery-ui.css" />
<script src="/HubCiti/scripts/jquery-1.8.3.js"></script>
<script type="text/javascript">

$(document).ready(function()

{
	
$('#trgrUpldMarkerImg').bind('change', function() {
		
		var imageType = document.getElementById("trgrUpldMarkerImg").value;
		var uploadImageType = $(this).attr('name');
		

		if (imageType != '') {
			var checkbannerimg = imageType.toLowerCase();
			if (!checkbannerimg.match(/(\.png|\.jpeg|\.jpg|\.gif|\.bmp)$/)) {
				alert("You must upload image with following extensions : .png, .gif, .bmp, .jpg, .jpeg");
				return false;
			} else {
				$("#screenSettingsForm").ajaxForm({
					
					success : function(response) {
					
						var imgRes = response.getElementsByTagName('imageScr')[0].firstChild.nodeValue
						var substr = imgRes.split('|');
						if (substr[0] == 'maxSizeImageError') {
							$('#maxSizeLogisticsImg').text("Image Dimension should not exceed Width: 800px Height: 600px");
						} else {
							openIframePopup('ifrmPopup', 'ifrm', '/HubCiti/cropImage.htm', 100, 99.5, 'Crop Image');
						}
					}
				}).submit();
			}
		}
	});
	
	
$("#logitude").focusout(function() {
	/* Matches	180.0, -180.0, 98.092391
	Non-Matches	181, 180, -98.0923913*/
	var vLatLngVal = /^-?([1]?[1-7][1-9]|[1]?[1-8][0]|[1-9]?[0-9])\.{1}\d{1,6}/;
	var vLong = $('#logitude').val();

	//Validate for Longitude.
	if (0 === vLong.length || !vLong || vLong == "") {
		return false;
	} else {
		if (!vLatLngVal.test(vLong)) {
			alert("Invalid Longitude");
			$("#logitude").val("").focus();
			return false;
		}
	}
});	



$("#latitude").focusout(function() {
	/* Matches	 90.0,-90.9,1.0,-23.343342
	Non-Matches	 90, 91.0, -945.0,-90.3422309*/
	var vLatLngVal = /^-?([1-8]?[1-9]|[1-9]0)\.{1}\d{1,6}/;
	var vLat = $('#latitude').val();

	//Validate for Latitude.
	if (0 === vLat.length || !vLat || vLat == "") {
		return false;
	} else {
		if (!vLatLngVal.test(vLat)) {
			alert("Invalid Latitude");
			$("#latitude").val("").focus();
			return false;
		}
	}
});


});
function SaveEvtMarkerInfo()
{
	var btnTitle =$("#btm").attr("value");
	
	if(btnTitle =='Save')
		{
		
	document.screenSettingsForm.buttonId.value='';
		
		}else{
			
		//	document.screenSettingsForm.evtMarkerId.value=$('#buttonId').val();
				
	//	document.screenSettingsForm.eventImageName.value=$('#evtMarkerImgPath').val(); 
		}
	
	document.screenSettingsForm.buttonName.value=btnTitle;
	//document.screenSettingsForm.hcEventID.value=$('#hcEventID').val();	
	document.screenSettingsForm.action="saveEvtMarker.htm";
	document.screenSettingsForm.Method="POST";
	document.screenSettingsForm.submit();
	
	
	}
	
	function deleteEvtMarker(markerId)
	{
		var r = confirm("Do you really delete this marker");
		if(r == true)	{
		//document.screenSettingsForm.hcEventID.value=$('hcEventID').val();	
		document.screenSettingsForm.evtMarkerId.value=markerId;
		document.screenSettingsForm.action="deleteEvtMarker.htm";
		document.screenSettingsForm.Method="POST";
		document.screenSettingsForm.submit();
		}
	}
	
	
	function editEvtMarker(markerId)
	{
		
	//	document.screenSettingsForm.hcEventID.value=$('hcEventID').val();	
		document.screenSettingsForm.evtMarkerId.value=markerId;
		document.screenSettingsForm.action="editEvtMarker.htm";
		document.screenSettingsForm.Method="POST";
		document.screenSettingsForm.submit();
	}
	
	
	function ClearEvtMarkerInfo()
	{
		
		
		var r = confirm("Do you really want to clear the form");
		if(r == true)	{
			$('.errorDsply').css("display","none");
			$('#successMsg').empty();
			$('#failureMsg').empty();
		//	document.getElementById('failureMsg').style.display = 'none';
			document.screenSettingsForm.evtMarkerName.value="";
			document.screenSettingsForm.eventImageName.value="";
			document.screenSettingsForm.latitude.value="";
			document.screenSettingsForm.logitude.value="";
			$("#eventImageName").prop('src','images/uploadIconSqr.png');
			
		}
		
		
		
		
	}
	function isLatLong(evt) {
		var charCode = (evt.which) ? evt.which : event.keyCode
		if ((charCode > 47 && charCode < 58) || charCode == 46 || charCode < 31
				|| charCode == 45 || charCode == 43)
			return true;
		return false;
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
				<sec:authorize
					access="hasAnyRole('ROLE_EVENT_SUPER_USER_VIEW','ROLE_EVENT_USER_VIEW')">
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
					<form:form name="screenSettingsForm"
						commandName="screenSettingsForm" id="screenSettingsForm"
						enctype="multipart/form-data" action="uploadevntimg.htm">

						<input type="hidden" name="viewName" value="addEvtMarker" />
						<form:input type="hidden" name="hcEventID" path="hcEventID"
							value='${sessionScope.evtId}' />

						<input type="hidden" name="markerBtnName"
							value="${requestScope.btnName}" />
						<input type="hidden" name="buttonName" />
						<form:input type="hidden" id="buttonId" path="buttonId"
							value='${requestScope.markerId}' />
						<input type="hidden" name="evtMarkerId" />
						<input type="hidden" id="evtMarkerImgPath" name="evtMarkerImgPath"
							value="${requestScope.btnImg}" />
						<div class="title-bar">
							<ul class="title-actn">
								<li class="title-icon"><span class="icon-alerts">&nbsp;</span>
								</li>
								<li>Setup Events</li>
							</ul>
						</div>
						<div class="tabd-pnl">
							<ul class="nav-tabs">


								<li><a id="mainMenu" href="manageevents.htm"
									class="rt-brdr">Manage Events</a></li>
								<li><a href="#" class="rt-brdr active">Manage Event
										Logistics</a></li>
								<!--<li><a id="subMenu" href="displayeventcate.htm"
									class="rt-brdr">Manage Event Category</a></li> -->
							</ul>
							<div class="clear"></div>
						</div>


						<div class="cont-wrp">
							<table width="100%" border="0" cellspacing="0" cellpadding="0"
								class="brdrlsTbl">
								<tr>
									<td width="20%"><label class="mand">Marker Name</label></td>
									<td width="30%"><form:errors cssClass="errorDsply"
											id="evtMarkerName" path="evtMarkerName"></form:errors>
										<div class="cntrl-grp">
											<form:input path="evtMarkerName" type="text"
												class="inputTxtBig" style="height: 22px;" tabindex="1"
												maxlength="40" value="${markerInfo.evtMarkerName}" />
										</div></td>


									<td width="20%" align="left"><label class="mand">Marker
											Icon</label></td>


									<td width="30%"><form:errors cssClass="errorDsply"
											path="eventImageName" id="eventImageName"></form:errors>
										<div class="cntrl-grp">

											<form:input type="file" class="inputTxtBig"
												id="eventImageFile" name="eventImageFile"
												path="eventImageFile" style="height: 22px;" />
										</div></td>

									<%-- 	<form:errors cssClass="errorDsply" path="eventImageName" id="eventImageName"></form:errors>
									   	<label class="imgLbl">
											<img id="eventImageName" width="26" height="26" alt="upload" src="${sessionScope.evtMarkerImagePreview}">
										</label>
                               
                <span class="topPadding cmnRow"> 
											<label for="trgrUpldMarkerImg">
												<input type="button" value="Upload" id="trgrUpldMarkerImgBtn" class="btn trgrUpld" 
												title="Upload Image File" >
													<form:hidden path="eventImageName" id="eventImageName" />
											
												<span class="instTxt nonBlk"></span> 
												<form:input type="file" class="textboxBig" id="trgrUpldMarkerImg" path="eventImageFile" tabindex="2" title="Upload Marker Icon"/>
											</label>
										</span>
										<label id="maxSizeLogisticsImg" class="errorDsply"></label>
									</td> --%>


								</tr>
								<tr>
									<td valign="top"><label class="mand">Latitude</label></td>
									<td><form:errors cssClass="errorDsply" id="latitude"
											path="latitude"></form:errors>
										<div class="cntrl-grp">
											<form:input path="latitude" type="text" class="inputTxtBig"
												style="height: 22px;" tabindex="3" maxlength="40"
												value="${markerInfo.latitude}"
												onkeypress="return isLatLong(event)" />
										</div></td>



									<td valign="top"><label class="mand">Longitude</label></td>
									<td><form:errors cssClass="errorDsply" id="logitude"
											path="logitude"></form:errors>
										<div class="cntrl-grp">
											<form:input path="logitude" type="text" class="inputTxtBig"
												style="height: 22px;" tabindex="4" maxlength="40"
												value="${markerInfo.logitude}"
												onkeypress="return isLatLong(event)" />
										</div></td>

								</tr>
							</table>

							<div class="cntrInput mrgnTop txt-right">
								<input type="button" id="btm" class="btn-blue" name="btn"
									tabindex="5" value="${requestScope.btnName}"
									onclick="SaveEvtMarkerInfo()" title="Save Marker details" /> <input
									type="button" class="btn-blue" value="Clear" tabindex="5"
									onclick="ClearEvtMarkerInfo()" title="Clear Marker details" />
							</div>
							<center>
								<c:if
									test="${requestScope.successMsg ne null && !empty requestScope.successMsg}">

									<span class="highLightTxt" id="successMsg"><c:out
											value="${requestScope.successMsg}" /> </span>

								</c:if>


								<c:if
									test="${requestScope.failureMsg ne null && !empty requestScope.failureMsg}">

									<span class="highLightErr" id="failureMsg"><c:out
											value="${requestScope.failureMsg}" /> </span>


								</c:if>

							</center>
							<div class="relative">
								<div class="hdrClone"></div>
								<div class="scrollTbl tblHt mrgnTop">
									<table width="100%" cellspacing="0" cellpadding="0" border="0"
										id="evntTbl" class="grdTbl clone-hdr fxdhtTbl">
										<thead>
											<tr class="tblHdr">
												<th width="40%">Marker Name</th>
												<th width="19%">Latitude</th>
												<th width="19%">Longitude</th>
												<th width="10%">Icon</th>
												<th width="12%">Action</th>
											</tr>
										</thead>
										<tbody class="scrollContent">


											<c:if
												test="${sessionScope.EvtMarkerLst ne null && !empty EvtMarkerLst}">

												<c:forEach items="${sessionScope.EvtMarkerLst}" var="marker">

													<tr>
														<td><div class="cell-wrp">${marker.evtMarkerName}</div></td>
														<td>${marker.latitude}</td>
														<td>${marker.logitude}</td>
														<td><img src="${marker.eventImageName}" width="26"
															height="26" /></td>
														<td><a title="edit" href="#"><img height="20"
																width="20" class="actn-icon" alt="edit"
																src="images/edit_icon.png"
																onclick="editEvtMarker(${marker.evtMarkerId})"
																title="Edit Marker details" /></a> <a title="delete"
															href="#"><img height="20" width="20"
																class="actn-icon" alt="delete"
																src="images/delete_icon.png"
																onclick="deleteEvtMarker(${marker.evtMarkerId})"
																title="Delete Marker details" /></a></td>

													</tr>

												</c:forEach>


											</c:if>

										</tbody>
									</table>
								</div>
				
							</div>

						</div>


					</form:form>
				</div>
			</div>
		</div>
	</div>
	<div class="ifrmPopupPannel" id="ifrmPopup" style="display: none;">
		<div class="headerIframe">
			<img src="/HubCiti/images/popupClose.png" class="closeIframe"
				alt="close"
				onclick="javascript:closeIframePopup('ifrmPopup','ifrm')"
				title="Click here to Close" align="middle" /> <span
				id="popupHeader"></span>
		</div>
		<iframe frameborder="0" scrolling="auto" id="ifrm" src=""
			height="100%" allowtransparency="yes" width="100%"
			style="background-color: White"> </iframe>
	</div>

</body>
<script type="text/javascript">
	configureMenu("setupevents");
</script>

</html>