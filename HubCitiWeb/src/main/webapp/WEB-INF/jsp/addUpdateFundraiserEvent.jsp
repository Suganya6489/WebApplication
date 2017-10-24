<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
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
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>


<script type="text/javascript">
	$(document).ready(function() {		
		
		CKEDITOR.config.uiColor = '#FFFFFF';
		CKEDITOR.replace('longDescription',
				{
					extraPlugins : 'onchange',
					width : "100%",
					toolbar : [
							/* { name: 'clipboard',   items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },
							{ name: 'editing',     items : [ 'Find','Replace','-','SelectAll','-','SpellChecker', 'Scayt' ] },
							{ name: 'forms',       items : [ 'Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField' ] },
							'/',
							{ name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat' ] },
							{ name: 'paragraph',   items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote','CreateDiv','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl' ] },
							{ name: 'links',       items : [ 'Link','Unlink','Anchor' ] },
							{ name: 'insert',      items : [ 'Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak' ] },
							'/',
							{ name: 'styles',      items : [ 'Styles','Format','Font','FontSize' ] },
							{ name: 'colors',      items : [ 'TextColor','BGColor' ] },
							{ name: 'tools',       items : [ 'Maximize', 'ShowBlocks','-','About' ] } */
							{
								name : 'basicstyles',
								items : [ 'Bold', 'Italic',
										'Underline' ]
							},
							{
								name : 'paragraph',
								items : [ 'JustifyLeft',
										'JustifyCenter',
										'JustifyRight',
										'JustifyBlock' ]
							}, {
								name : 'colors',
								items : [ 'BGColor' ]
							}, {
								name : 'paragraph',
								items : [ 'Outdent', 'Indent' ]
							}, {
								name : 'links',
								items : [ 'Link', 'Unlink' ]
							} ,	'/',
							{
								name : 'styles',
								items : [ 'Styles', 'Format' ]
							}, {
								name : 'tools',
								items : [ 'Font', 'FontSize',
										'RemoveFormat' ]
							} ],
					removePlugins : 'resize'
				});
		
		var hcEventID=$("#hcEventID").val();
		
		if(hcEventID != "") {
			document.title = "Update Fundraiser Event";		
			$("#fundraiserTitle").text("Update Fundraiser Event");	
			$("#Save").val("Update");
			$("#Save").attr("title", "Update");
			$("#Clear").hide();
		}

		$(".modal-close").on('click.popup', function() {
			$(".modal-popupWrp").hide();
			$(".modal-popup").slideUp();
			$(".modal-popup").find(".modal-bdy input").val("");
			$(".modal-popup i").removeClass("errDsply");
		});
		
		/*fundraisers tied to event: toggle display based on option selected
		input: radio buttons
		 */
		$("input[name='isEventTied']").change(function() {
			var frEvntId = $(this).attr("id");
			if (frEvntId == "fndYes") {
				$("." + frEvntId).show();
				var eventTiedIds = '${requestScope.eventTiedIds}';
				var arr = eventTiedIds.split(',');	
				jQuery.each(arr, function(i, val) { 
					$("input[name='eventTiedIds'][value='" + val + "']").attr("checked",true);
					//$("#eventCategory option[value='" + val + "']").prop('selected', 'selected');
				});
				var height = $(".cont-pnl").height();
				$("#menu-pnl").height(height + 28);
			} else {
				$(".fndYes").hide();
				var height = $(".cont-pnl").height();
				$("#menu-pnl").height(height + 28);
			}
		});
		$('input[name="isEventTied"]:checked').trigger('change');

		$("#eventDate").datepicker({
			showOn : 'both',
			buttonImageOnly : true,
			buttonText : 'Click to show the calendar',
			buttonImage : 'images/icon-calendar-active.png'
		});
		$("#eventEDate").datepicker({
			showOn : 'both',
			buttonImageOnly : true,
			buttonText : 'Click to show the calendar',
			buttonImage : 'images/icon-calendar-active.png'
		});	
		
		$('.priceVal').keypress(function(event) {	   
			if(event.keyCode != 9) {			
				if (event.which != 46 && (event.which < 47 || event.which > 59) && event.which != 8  )
			    {
			        event.preventDefault();
			        if ((event.which == 46) && ($(this).indexOf('.') != -1)) {
			            event.preventDefault();
			        }
			    }	
			}	
		});
		
		$(window).load(function() {
			
			/*Range of recurrence: trgiger click on radio button when input:text is clicked*/
			$(".range").on('keydown.trgrRng click',function() { 
				var curInput = $(this).attr("id"); 
				var curIn = $("."+curInput).find('input[type="radio"]');
				var curName = $(curIn).attr("name");
				$('input[name="'+curName+'"]').removeAttr("checked");
				$(curIn).click();
			});
			
		});
		
	});

	
</script>
<script type="text/javascript">
	function saveFundraiserEvent() {
		document.screenSettingsForm.action = "saveFundraiserEvent.htm";
		document.screenSettingsForm.method = "POST";
		document.screenSettingsForm.submit();
	}

	function searchEvents() {
		document.screenSettingsForm.action = "searchEvents.htm";
		document.screenSettingsForm.method = "GET";
		document.screenSettingsForm.submit();
	}
	
	function backToFundraiserEvent() {
		if(confirm("Are you sure you want to leave this page without saving the form !")) {
			document.screenSettingsForm.action = "managefundraisers.htm";
			document.screenSettingsForm.method = "GET";
			document.screenSettingsForm.submit();
		}
	}
	
	function clearForm() {
		var r = confirm("Do you really want to clear the form");
		if(r == true)
		{			
			if (document.getElementById('hcEventName.errors') != null) {
				document.getElementById('hcEventName.errors').style.display = 'none';
			}
			if (document.getElementById('organizationHosting.errors') != null) {
				document.getElementById('organizationHosting.errors').style.display = 'none';
			}
			if (document.getElementById('appsiteIDs.errors') != null) {
				document.getElementById('appsiteIDs.errors').style.display = 'none';
			}
			if (document.getElementById('eventCategory.errors') != null) {
				document.getElementById('eventCategory.errors').style.display = 'none'; 
			}
			if (document.getElementById('eventDate.errors') != null) {
				document.getElementById('eventDate.errors').style.display = 'none';
			}
			if (document.getElementById('eventImageName.errors') != null) {
				document.getElementById('eventImageName.errors').style.display = 'none';
			}
			if (document.getElementById('shortDescription.errors') != null) {
				document.getElementById('shortDescription.errors').style.display = 'none';
			}
			if (document.getElementById('longDescription.errors') != null) {
				document.getElementById('longDescription.errors').style.display = 'none';
			}
			if (document.getElementById('eventTiedIds.errors') != null) {
				document.getElementById('eventTiedIds.errors').style.display = 'none';
			}
			document.screenSettingsForm.hcEventName.value = "";
			
			document.screenSettingsForm.eventImageName.value = "";
			$("#eventImageName").prop('src','images/uploadIconSqr.png');
			
			$("input:radio[value='No']").prop('checked',true);	
			document.screenSettingsForm.organizationHosting.value = "";
			
			document.screenSettingsForm.appsiteIDs.value = "";
			document.screenSettingsForm.shortDescription.value = "";
			document.screenSettingsForm.longDescription.value = "";
			CKEDITOR.instances['longDescription'].setData('');
			
			document.screenSettingsForm.eventCategory.value = "";
			document.screenSettingsForm.hiddenCategory.value ="";
			
			document.screenSettingsForm.departmentId.value = "";
			document.screenSettingsForm.eventDate.value = "";
			document.screenSettingsForm.eventEDate.value = "";
			
			$("#fndYes").prop('checked',false);
			$(".fndYes").hide();
			document.screenSettingsForm.appSiteSearchKey.value = "";
			$("input[name='eventTiedIds']").prop("checked",false);
			
			$("#fndNo").prop('checked',true);
			
			
			document.screenSettingsForm.moreInfoURL.value = "";			
			document.screenSettingsForm.fundraisingGoal.value = "";
			document.screenSettingsForm.purchaseProducts.value = "";
			document.screenSettingsForm.currentLevel.value = "";
			
			$('body,html').animate({
				scrollTop : 0
			}, 'slow');
			
			var height = $(".cont-pnl").height();
			$("#menu-pnl").height(height + 28);
		}		
	}
</script>

<div id="wrpr" class="fundraiserPg">
	<span class="clear"></span>
	<div class="wrpr-cont relative">
		<div id="slideBtn">
			<a href="#" onclick="revealPanel(this);" title="Hide Menu"><img src="images/slide_off.png" width="11" height="28" alt="btn_off" /> </a>
		</div>
		<div id="bread-crumb">
			<ul>
				<li class="scrn-icon"><span class="icon-home">&nbsp;</span>
				</li>
				<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
					<li><a href="welcome.htm">Home</a></li>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('ROLE_FUNDRAISING_SUPER_USER_VIEW','ROLE_FUNDRAISING_USER_VIEW')">
					<li>Home</li>
				</sec:authorize>
				<li><a href="managefundraisers.htm">Setup Fundraisers</a>
				</li>
				<li class="last" id="fundraiserTitle">Add Fundraiser Event</li>
			</ul>
		</div>
		<span class="blue-brdr"></span>
		<div class="content" id="login">
			<div id="menu-pnl" class="split">
				<ul id="icon-menu">
					<jsp:include page="leftNavigation.jsp"></jsp:include>
				</ul>
			</div>
			<div class="cont-pnl split" id="">
				<div class="cont-block rt-brdr stretch">
					<div class="title-bar">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-fundraising">&nbsp;</span>
							</li>
							<li>Fundraisers</li>
						</ul>
					</div>
					<span class="clear"></span>
					<form:form name="screenSettingsForm" id="screenSettingsForm" commandName="screenSettingsForm" enctype="multipart/form-data" action="uploadevntimg.htm">
						<form:hidden path="viewName" value="addupdatefundraiserevent" />
						<form:hidden path="hiddenDept" />
						<form:hidden path="hiddenCategory" />
						<form:hidden path="hcEventID"/>
						<form:hidden path="lowerLimit" value="${requestScope.lowerLimit}" />
						<form:hidden path="eventSearchKey" value="${requestScope.eventSearchKey}"/>
						
						<div class="pill-tabs">
							<a href="#" class="active" ref="eventDetails">Fundraiser Details</a>
						</div>
						<div class="cont-wrp">
							<div class="eventDetails">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="brdrlsTbl">
									<tr>
										<td width="23%"><label class="mand">Title</label></td>
										<td width="28%"><form:errors cssClass="errorDsply" path="hcEventName"></form:errors>
											<div class="cntrl-grp">
												<form:input path="hcEventName" cssClass="inputTxt inputTxtBig" maxlength="40" tabindex="1"/>
											</div>
										</td>
										<td width="21%" align="left"><label class="mand">Organization Image</label>
										</td>
										<td width="28%"><form:errors cssClass="errorDsply" path="eventImageName"></form:errors> 
											<label class="imgLbl"> <img id="eventImageName" width="100" height="50" alt="upload" src="${sessionScope.eventImagePreview }"> </label> 
											<span class="topPadding cmnRow"> <label for="trgrUpld"> 
												<input type="button" value="Upload" id="trgrUpldBtn" class="btn trgrUpld" title="Upload Image File" tabindex="2"> 
												<form:hidden path="eventImageName" id="eventImageName" /> 
													<span class="instTxt nonBlk"></span> 
													<form:input type="file" class="textboxBig" id="trgrUpld" path="eventImageFile" /> 
											</label> </span>
											<label id="maxSizeImageError" class="errorDsply"></label>
										</td>
									</tr>
									<tr>
										<td align="left" valign="top" class="organizationHosting"><form:radiobutton path="isEventAppsite" value="No"/> Organization Hosting</td>
										<td><form:errors cssClass="errorDsply" path="organizationHosting"></form:errors>
											<div class="cntrl-grp">
												<form:input path="organizationHosting" cssClass="inputTxt inputTxtBig range" maxlength="40" tabindex="3"/>
											</div>
										</td>
										<td valign="top" class="appsiteIDs"><span class="set-padding"><form:radiobutton path="isEventAppsite" value="Yes" /> </span>Select Appsite</td>
										<td><form:errors cssClass="errorDsply" path="appsiteIDs"></form:errors>
											<div class="cntrl-grp zeroBrdr">
												<span class="lesserWdth">
													<form:select path="appsiteIDs" cssClass="slctBx range" tabindex="4">
														<form:option value="">Select Appsite</form:option>
														<c:forEach items="${sessionScope.eventAppSiteLst}" var="appSite">
															<form:option value="${appSite.appSiteId}">${appSite.appSiteName}</form:option>
														</c:forEach>
													</form:select>
												</span>
												<a onclick="addAppsite(this)" href="javascript:void(0);" title="Add New Appsite" viewName="addupdatefundraiserevent" tabindex="5"> 
													<img height="24" width="24" class="addImg" alt="add" src="images/btn_add.png"> 
												</a>
											</div>											
										</td>
									</tr>
									<tr>
										<td valign="top"><label class="mand">Short Description</label></td>
										<td colspan="3"><form:errors cssClass="errorDsply" path="shortDescription"></form:errors>
											<div class="cntrl-grp">
												<form:textarea path="shortDescription" name="textarea" cols="25" rows="5" class="textareaTxt" tabindex="6" maxlength="255"></form:textarea>
											</div>
										</td>
									</tr>
									<tr>
										<td valign="top"><label class="mand">Long Description</label></td>
										<td colspan="3"><form:errors cssClass="errorDsply" path="longDescription"></form:errors>
											<div class="">
												<form:textarea path="longDescription" class="textareaTxt cstmFix" cols="25" rows="5" tabindex="7" maxlength="2000"></form:textarea>
											</div>
										</td>
									</tr>
									<tr class="not-ongng">
										<td><label class="mand">Category</label></td>
										<td><form:errors cssClass="errorDsply" path="eventCategory"></form:errors>
											<div class="cntrl-grp zeroBrdr">
												<form:select path="eventCategory" cssClass="slctBx" tabindex="8">
													<form:option value="">Select Category</form:option>
													<c:forEach items="${sessionScope.catLst}" var="catLst">
														<form:option value="${catLst.catId}">${catLst.catName}</form:option>
													</c:forEach>
												</form:select>
											</div>
										</td>
										<td>Department</td>
										<td><div class="cntrl-grp zeroBrdr">
												<span class="lesserWdth"> <form:select path="departmentId" cssClass="slctBx" id="departmentId" tabindex="9">
														<form:option value="">Select Department</form:option>
														<c:forEach items="${sessionScope.deptLst}" var="deptLst">
															<form:option value="${deptLst.deptId}">${deptLst.deptName}</form:option>
														</c:forEach>
													</form:select> </span> 
												<a href="#" onclick="showFundraiserDeptModal(this)" viewName="addupdatefundraiserevent">
													<img width="24" height="24" src="images/btn_add.png" alt="add" class="addElem" title="Add New Department" tabindex="10"> 
												</a>
											</div>
										</td>
									</tr>
									<tr class="not-ongng">
										<td><label class="mand">Start Date</label></td>
										<td><form:errors cssClass="errorDsply" path="eventDate"></form:errors>
											<div class="cntrl-grp cntrl-dt floatL">
												<form:input path="eventDate" id="eventDate" type="text" class="inputTxt inputTxtBig" tabindex="9" maxlength="11"/>
											</div>
										</td>
										<td>End Date</td>
										<td><form:errors cssClass="errorDsply" path="eventEDate"></form:errors>
											<div class="cntrl-grp cntrl-dt floatL">
												<form:input path="eventEDate" id="eventEDate" type="text" class="inputTxt inputTxtBig" tabindex="9" maxlength="12"/>
											</div>
										</td>
									</tr>
									<tr class="subHdr">
										<td colspan="4">Is this tied to an event? <span class="mrgn-left spacing"> 
											<form:radiobutton path="isEventTied" value="Yes" id="fndYes" tabindex="13"/><label for="fndYes">Yes</label> 
											<form:radiobutton path="isEventTied" value="No" id="fndNo" tabindex="14"/><label for="fndNo">No</label> 
											<form:errors cssClass="errorDsplyHtl" path="eventTiedIds"></form:errors>
										</span>
										</td>
									</tr>
									<tr class="grey-bg fndYes" style="display: table-row;">
										<td colspan="4"><table cellspacing="0" cellpadding="0" width="100%" border="0" class="zerobrdrTbl">
												<tbody>
													<tr>
														<td width="18%"><label>Search Events</label>
														</td>
														<td width="30%"><div class="cntrl-grp">
																<form:input path="appSiteSearchKey" cssClass="inputTxtBig" cssStyle="height: 22px;" maxlength="50"/>
															</div>
														</td>
														<td width="10%"><a href="javascript:void(0)"><img width="20" height="17" src="images/searchIcon.png" alt="search" title="search events" onclick="searchEvents();"> </a>
														</td>
														<td width="40%" align="right"></td>
													</tr>
												</tbody>
											</table>
											<c:if test="${requestScope.eventError ne null}">
												<div class="alertBx warning mrgnTop cntrAlgn">
													<span class="actn-close" title="close"></span>
													<p class="msgBx">
														<c:out value="${requestScope.eventError}" />
													</p>
												</div>
											</c:if>
										</td>
									</tr>
									<c:if test="${sessionScope.eventLst ne null}">
									<tr class="grey-bg equalPdng fndYes">
										<td colspan="4">
											<div class="relative">
												<div class="hdrClone"></div>
												<div class="scrollTbl tblHt">
													
														<table width="100%" cellspacing="0" cellpadding="0" border="0" class="grdTbl clone-hdr fxdhtTbl white-bg" id="mngevntTbl">
															<thead>
																<tr class="tblHdr">
																	<th width="22%">Event Name</th>
																	<th width="32%">Location</th>
																	<th width="18%">Start Date</th>
																	<th width="17%">End Date</th>
	
																	<th width="11%">Action</th>
																</tr>
															</thead>
															<tbody class="scrollContent">
																<c:forEach items="${sessionScope.eventLst}" var="eve">
																	<tr>
																		<td>${eve.hcEventName}</td>
																		<td>${eve.address}, ${eve.city}, ${eve.state}, ${eve.postalCode}</td>
																		<td>${eve.eventStartDate}</td>
																		<td>${eve.eventEndDate}</td>
																		<td align="center"><form:checkbox path="eventTiedIds" value="${eve.hcEventID}" /><label for="checkbox"></label></td>
																	</tr>
																</c:forEach>
															</tbody>
														</table>
													
												</div>
											</div>
										</td>
									</tr>
									</c:if>
									<tr class="">
										<td>More Information</td>
										<td><form:errors cssClass="errorDsply" path="moreInfoURL"></form:errors>
											<div class="cntrl-grp">
												<form:input path="moreInfoURL" cssClass="inputTxt" maxlength="100" tabindex="15"/>
											</div>
										</td>
										<td>Fundraising Goal</td>
										<td><form:errors cssClass="errorDsply" path="fundraisingGoal"></form:errors><div class="cntrl-grp">
												<form:input path="fundraisingGoal" cssClass="inputTxt priceVal" maxlength="10" tabindex="16"/>
											</div>
										</td>
									</tr>
									<tr class="">
										<td>Purchase Products</td>
										<td><form:errors cssClass="errorDsply" path="purchaseProducts"></form:errors>
											<div class="cntrl-grp">
												<form:input path="purchaseProducts" cssClass="inputTxt" maxlength="100" tabindex="17"/>
											</div>
										</td>
										<td>Current Level</td>
										<td><form:errors cssClass="errorDsply" path="currentLevel"></form:errors><div class="cntrl-grp">
												<form:input path="currentLevel" cssClass="inputTxt priceVal" maxlength="10" tabindex="18"/>
											</div>
										</td>
									</tr>
								</table>
								<div class="cntrInput mrgnTop">
									<input type="button" id="Save" value="Save" class="btn-blue" title="Save" onclick="saveFundraiserEvent();" tabindex="19"/> 
									<input type="button" id="Clear" value="Clear" class="btn-blue" title="Clear" onclick="clearForm();" tabindex="20"/> 
									<input type="button" id="Back" value="Back" class="btn-blue" title="Back" onclick="backToFundraiserEvent();" tabindex="21"/>
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
</div>
<div class="modal-popupWrp">
	<div class="modal-popup">
		<div class="modal-hdr">
			<a class="modal-close" title="close">×</a>
			<h3>Create Department</h3>
		</div>
		<div class="modal-bdy">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td width="30%">Department Name</td>
					<td width="56%"><div class="cntrl-grp">
							<input type="text" id="deptNm" class="inputTxtBig" maxlength="20" />
						</div> <i class="emptydept">Please Enter Department Name</i>
						<p class="dupctgry">Department Name already exists</p></td>
				</tr>
			</table>
		</div>
		<div class="modal-ftr">
			<p align="right">
				<input type="submit" class="btn-blue" value="Save Department" title="Save Department"> <input type="reset" class="btn-grey" value="Clear" id="" name="Reset" onclick="clearDept()" title="Clear"> 
			</p>
		</div>
	</div>
</div>
<script type="text/javascript">
	configureMenu("setupfndrevt");
</script>