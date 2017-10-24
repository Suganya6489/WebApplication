
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<script type="text/javascript" src="scripts/jquery-1.10.2.js"></script>
<script type="text/javascript" src="scripts/jquery-ui.js"></script>
<script type="text/javascript" src="scripts/jquery-ui-min.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/jquery.form.js"></script>
<link rel="stylesheet" href="/HubCiti/styles/jquery-ui.css" />
<script src="/HubCiti/scripts/jquery-1.8.3.js"></script>

<script type="text/javascript">
	$(document)
			.ready(
					function() {

						var title = "${sessionScope.screenType}";

						if (title == 'edit') {
							document.title = "Edit Alerts";
						}

						$(".modal-close").on('click.popup', function() {
							$(".modal-popupWrp").hide();
							$(".modal-popup").slideUp();
							$(".modal-popup").find(".modal-bdy input").val("");
							$(".modal-popup i").removeClass("errDsply");
						});

						//var stHt = $(".cont-pnl").height();
						//$("#menu-pnl").height(stHt);

						$("#startDate").datepicker({
							showOn : 'both',
							buttonImageOnly : true,
							buttonText : 'Click to show the calendar',
							buttonImage : 'images/icon-calendar-active.png'
						});
						$('.ui-datepicker-trigger').css("padding-left", "5px");

						$("#endDate").datepicker({
							showOn : 'both',
							buttonImageOnly : true,
							buttonText : 'Click to show the calendar',
							buttonImage : 'images/icon-calendar-active.png'
						});
						$('.ui-datepicker-trigger').css("padding-left", "5px");

						var title = $("#title").val();
						var description = $("#description").val();
						var startDate = $("#startDate").val();
						var startTimeHrs = $("#startTimeHrs").val();
						var startTimeMins = $("#startTimeMins").val();
						var startTime = startTimeHrs + ":" + startTimeMins
						var endTimeHrs = $("#endTimeHrs").val();
						var endTimeMins = $("#endTimeMins").val();
						var endTime = endTimeHrs + ":" + endTimeMins
						var endDate = $("#endDate").val();
						if (description == "") {
							document.getElementById('descriptionPreview').innerHTML = "Description goes here..";

						} else {
							document.getElementById('descriptionPreview').innerHTML = description;
						}

						if (title == "") {
							$("#titlePreview").text("Title goes here..");

						} else {
							$("#titlePreview").text(title);
						}
						if (startDate == "") {
							$("#startDatePreview").text("");

						} else {
							$("#startDatePreview").text(startDate);
						}
						if (startTime == "") {
							$("#startTimePreview").text("00:00");

						} else {
							$("#startTimePreview").text(startTime);
						}
						if (endDate == "") {
							$("#endDatePreview").text("");

						} else {
							$("#endDatePreview").text(endDate);
						}
						if (endTime == "") {
							$("#endTimePreview").text("00:00");

						} else {
							$("#endTimePreview").text(endTime);
						}

						$("#title").keyup(function() {
							title = $("#title").val();
							if (title == "") {
								$("#titlePreview").text("Title goes here..");

							} else {
								$("#titlePreview").text(title);
							}
						});

						$("#title").change(function() {
							title = $("#title").val();
							if (title == "") {
								$("#titlePreview").text("Title goes here..");

							} else {
								$("#titlePreview").text(title);
							}
						});

						$("#description")
								.keyup(
										function() {
											description = $("#description")
													.val();
											if (description == "") {
												document
														.getElementById('descriptionPreview').innerHTML = "Description goes here..";

											} else {
												document
														.getElementById('descriptionPreview').innerHTML = description;
											}
										});

						$("#description")
								.change(
										function() {
											description = $("#description")
													.val();
											if (description == "") {
												document
														.getElementById('descriptionPreview').innerHTML = "Description goes here..";

											} else {
												document
														.getElementById('descriptionPreview').innerHTML = description;
											}
										});

						$("#startDate").keyup(function() {
							$("#startDatePreview").text($("#startDate").val());

						});

						$("#startDate").change(function() {
							$("#startDatePreview").text($("#startDate").val());

						});
						$("#startTimeHrs").keyup(function() {
							var startTimeHrs = $("#startTimeHrs").val();
							var startTimeMins = $("#startTimeMins").val();
							var startTime = startTimeHrs + ":" + startTimeMins
							$("#startTimePreview").text(startTime);

						});

						$("#startTimeHrs").change(function() {
							var startTimeHrs = $("#startTimeHrs").val();
							var startTimeMins = $("#startTimeMins").val();
							var startTime = startTimeHrs + ":" + startTimeMins
							$("#startTimePreview").text(startTime);

						});
						$("#startTimeMins").keyup(function() {
							var startTimeHrs = $("#startTimeHrs").val();
							var startTimeMins = $("#startTimeMins").val();
							var startTime = startTimeHrs + ":" + startTimeMins
							$("#startTimePreview").text(startTime);

						});

						$("#startTimeMins").change(function() {
							var startTimeHrs = $("#startTimeHrs").val();
							var startTimeMins = $("#startTimeMins").val();
							var startTime = startTimeHrs + ":" + startTimeMins
							$("#startTimePreview").text(startTime);
						});

						$("#endDate").keyup(function() {
							$("#endDatePreview").text($("#endDate").val());

						});

						$("#endDate").change(function() {
							$("#endDatePreview").text($("#endDate").val());

						});

						$("#endTimeHrs").keyup(function() {
							var endTimeHrs = $("#endTimeHrs").val();
							var endTimeMins = $("#endTimeMins").val();
							var endTime = endTimeHrs + ":" + endTimeMins
							$("#endTimePreview").text(endTime);

						});

						$("#endTimeHrs").change(function() {
							var endTimeHrs = $("#endTimeHrs").val();
							var endTimeMins = $("#endTimeMins").val();
							var endTime = endTimeHrs + ":" + endTimeMins
							$("#endTimePreview").text(endTime);

						});
						$("#endTimeMins").keyup(function() {
							var endTimeHrs = $("#endTimeHrs").val();
							var endTimeMins = $("#endTimeMins").val();
							var endTime = endTimeHrs + ":" + endTimeMins
							$("#endTimePreview").text(endTime);

						});

						$("#endTimeMins").change(function() {
							var endTimeHrs = $("#endTimeHrs").val();
							var endTimeMins = $("#endTimeMins").val();
							var endTime = endTimeHrs + ":" + endTimeMins
							$("#endTimePreview").text(endTime);

						});

					});
</script>

<script type="text/javascript">
	function saveAlerts() {
		document.addAlerts.lowerLimit.value = '${requestScope.lowerLimit}';
		document.addAlerts.action = "savealerts.htm";
		document.addAlerts.method = "POST";
		document.addAlerts.submit();
	}

	function updateAlerts() {
		document.addAlerts.lowerLimit.value = '${requestScope.lowerLimit}';
		document.addAlerts.action = "updatealerts.htm";
		document.addAlerts.method = "POST";
		document.addAlerts.submit();
	}

	function clearForm() {

		var r = confirm("Do you really want to clear the form");
		if (r == true) {
			document.addAlerts.title.value = "";
			document.addAlerts.description.value = "";
			document.addAlerts.startDate.value = "";
			document.addAlerts.endDate.value = "";
			document.addAlerts.startTimeHrs.value = "00";
			document.addAlerts.startTimeMins.value = "00";
			document.addAlerts.endTimeHrs.value = "00";
			document.addAlerts.endTimeMins.value = "00";
			document.addAlerts.categoryId.value = "";
			document.addAlerts.severityId.value = "";

			if (document.getElementById('title.errors') != null) {
				document.getElementById('title.errors').style.display = 'none';
			}
			if (document.getElementById('description.errors') != null) {
				document.getElementById('description.errors').style.display = 'none';
			}
			if (document.getElementById('startDate.errors') != null) {
				document.getElementById('startDate.errors').style.display = 'none';
			}
			if (document.getElementById('endDate.errors') != null) {
				document.getElementById('endDate.errors').style.display = 'none';
			}
			if (document.getElementById('categoryId.errors') != null) {
				document.getElementById('categoryId.errors').style.display = 'none';
			}
			if (document.getElementById('severityId.errors') != null) {
				document.getElementById('severityId.errors').style.display = 'none';
			}

			$("#descriptionPreview").text("Description goes here..");

			$("#titlePreview").text("Title goes here..");

			$("#startDatePreview").text("");

			$("#endDatePreview").text("");

			$("#startTimePreview").text("00:00");

			$("#endTimePreview").text("00:00");
		}
	}

	function BackToAlerts() {
		var r = confirm("Are you sure you want to leave this page without saving the changes!");
		if (r == true) {
			document.addAlerts.lowerLimit.value = '${requestScope.lowerLimit}';
			document.addAlerts.action = "displayalerts.htm";
			document.addAlerts.method = "GET";
			document.addAlerts.submit();
		}
	}
</script>

<div class="wrpr-cont relative">
	<div id="slideBtn">
		<a href="#" onclick="revealPanel(this);" title="Hide Menu"><img src="images/slide_off.png" width="11" height="28" alt="btn_off" /> </a>
	</div>
	<div id="bread-crumb">
		<ul>
			<li class="scrn-icon"><span class="icon-home">&nbsp;</span></li>
			<li><a href="welcome.htm">Home</a></li>
			<li><a href="displayalerts.htm">Manage Alerts</a></li>
			<c:choose>
				<c:when test="${sessionScope.screenType eq 'edit'}">
					<li class="last">Edit Alerts</li>
				</c:when>
				<c:otherwise>
					<li class="last">Add Alerts</li>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
	<span class="blue-brdr"></span>
	<div class="content" id="login">
		<div id="menu-pnl" class="split">
			<%@include file="leftNavigation.jsp"%>

		</div>
		<div class="cont-pnl split" id="equalHt">
			<div class="cont-block rt-brdr">
				<div class="title-bar">
					<ul class="title-actn">
						<li class="title-icon"><span class="icon-alerts">&nbsp;</span>
						</li>
						<li>Alerts</li>
					</ul>
				</div>
				<c:if test="${requestScope.responseStatus ne null && !empty requestScope.responseStatus}">
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
				<div class="cont-wrp">
					<form:form name="addAlerts" id="addAlerts" commandName="addAlerts">
						<form:hidden path="alertId" value="${sessionScope.alertId}" />
						<form:hidden path="lowerLimit" />
						<form:hidden path="viewName" value="editalert" />
						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="cmnTbl">
							<tr>
								<td width="33%"><label class="mand">Title</label></td>
								<td width="67%"><form:errors path="title" cssStyle="color:red"></form:errors>
									<div class="cntrl-grp">
										<form:input path="title" cssClass="inputTxt" maxlength="25" />
									</div></td>
							</tr>
							<tr>
								<td valign="top"><label class="mand">Description</label></td>
								<td colspan="2"><form:errors path="description" cssStyle="color:red"></form:errors>
									<div class="cntrl-grp">
										<form:textarea id="descriptions" path="description" cssClass="textareaTxt" cols="25" rows="5" />
									</div></td>
							</tr>
							<tr>
								<td width="33%"><label class="mand">Alert Category</label></td>
								<td width="67%" colspan="2"><form:errors path="categoryId" cssStyle="color:red"></form:errors>
									<div class="cntrl-grp zeroBrdr">
										<span class="lesWdth"> <form:select path="categoryId" cssClass="slctBx">
												<form:option value="">--Select--</form:option>
												<c:forEach items="${sessionScope.categories}" var="category">
													<form:option value="${category.catId}">${category.catName}</form:option>
												</c:forEach>
											</form:select> </span> <a onclick="showModal(this)" href="#" title="add" viewName="addAlert"> <img height="24" width="24" title="Add New Category" class="addElem" alt="add" src="images/btn_add.png"> </a>
									</div></td>
							</tr>
							<tr>
								<td><label class="mand">Severity</label></td>
								<td><form:errors path="severityId" cssStyle="color:red"></form:errors>
									<div class="cntrl-grp zeroBrdr">
										<form:select path="severityId" cssClass="slctBx" id="ctgryLst">
											<form:option value="">--Select--</form:option>
											<c:forEach items="${sessionScope.severities}" var="severity">
												<form:option value="${severity.severityId}">${severity.severityName}</form:option>
											</c:forEach>
										</form:select>
									</div></td>
							</tr>
							<tr>
								<td><label class="mand">Start Date</label></td>
								<td><form:errors path="startDate" cssStyle="color:red"></form:errors>
									<div class="cntrl-grp cntrl-dt floatL">
										<form:input path="startDate" id="startDate" cssClass="inputTxt" maxlength="10" />
									</div></td>
							</tr>
							<tr>
								<td>Start Time</td>
								<td><form:select path="startTimeHrs" class="slctSmall" name="etHr">
										<form:options items="${StartHours}" />
									</form:select>Hrs <form:select path="startTimeMins" class="slctSmall" name="stMin">
										<form:options items="${StartMinutes}" />
									</form:select> Mins</td>
							</tr>
							<tr>
								<td><label class="mand">End Date</label></td>
								<td><form:errors path="endDate" cssStyle="color:red"></form:errors>
									<div class="cntrl-grp cntrl-dt floatL">
										<form:input path="endDate" id="endDate" cssClass="inputTxt" maxlength="10" />
									</div></td>
							</tr>
							<tr>
								<td>End Time</td>
								<td><form:select path="endTimeHrs" class="slctSmall" name="etHr">
										<form:options items="${StartHours}" />
									</form:select>Hrs <form:select path="endTimeMins" class="slctSmall" name="stMin">
										<form:options items="${StartMinutes}" />
									</form:select> Mins</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><c:choose>
										<c:when test="${sessionScope.screenType eq 'edit'}">
											<input type="button" name="button" id="" value="Update" class="btn-blue" onclick="updateAlerts();" title="Update" />
											<input type="button" name="Back" id="" value="Back" class="btn-grey" onclick="BackToAlerts();" title="Back" />
										</c:when>
										<c:otherwise>
											<input type="button" name="Save" id="" value="Save" class="btn-blue" onclick="saveAlerts();" title="Save" />
											<input type="button" name="Clear" id="" value="Clear" class="btn-grey" onclick="clearForm();" title="Clear" />
											<input type="button" name="Back" id="" value="Back" class="btn-grey" onclick="BackToAlerts();" title="Back" />
										</c:otherwise>
									</c:choose></td>
							</tr>
						</table>
					</form:form>
				</div>
			</div>
			<div class="cont-block">
				<div class="title-bar">
					<ul class="title-actn">
						<li class="title-icon"><span class="icon-iphone">&nbsp;</span>
						</li>
						<li>Preview</li>
					</ul>
				</div>
				<div class="cont-wrp">
					<div id="iphone-preview">
						<!--Iphone Preview For Login screen starts here-->
						<div id="iphone-alerts-preview">
							<div class="iphone-status-bar"></div>
							<div class="navBar iphone">
								<table width="100%" border="0" cellspacing="0" cellpadding="0" class="titleGrd">
									<tr>
										<td width="19%"><img src="images/backBtn.png" alt="back" width="50" height="30" /></td>
										<td width="54%" class="genTitle">Details</td>
										<td width="27%" align="right"><img src="images/mainMenuBtn.png" width="78" height="30" alt="Main Menu" /></td>
									</tr>
								</table>
							</div>
							<div class="previewAreaScroll">
								<div class="iPhone-preview-conn-alerts">
									<div class="iPhnCont">
										<h2 class="iPhnTitle" id="titlePreview"></h2>
										<table width="100%" border="0" cellpadding="0" cellspacing="0">
											<tr>
												<td colspan="4"><div class="wraptxt" id="descriptionPreview"></div></td>
											</tr>
											<tr>
												<td width="25%">Start Date :</td>
												<td colspan="3" id="startDatePreview"></td>
											</tr>
											<tr>
												<td width="25%">Start Time :</td>
												<td colspan="3" id="startTimePreview"></td>
											</tr>
											<tr>
												<td width="25%">End Date :</td>
												<td colspan="3" id="endDatePreview"></td>
											</tr>
											<tr>
												<td width="25%">End Time :</td>
												<td colspan="3" id="endTimePreview"></td>
											</tr>
										</table>
										<div class="iPhnContBtm">&nbsp;</div>
									</div>
								</div>
							</div>
						</div>

						<!--Iphone Preview For Login screen ends here-->
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="modal-popupWrp">
	<div class="modal-popup">
		<div class="modal-hdr">
			<a class="modal-close" title="close">×</a>
			<h3>Alert Category</h3>
		</div>
		<div class="modal-bdy">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td width="30%">Category Name</td>
					<td width="56%"><div class="cntrl-grp">
							<input type="text" id="ctgryNm" class="req inputTxtBig" maxlength="20" />
						</div> <i class="emptyctgry">Please Enter Category Name</i>
						<p class="dupctgry">Category Name already exists</p></td>
				</tr>
			</table>
		</div>
		<div class="modal-ftr">
			<p align="right">
				<input type="submit" class="btn-blue" value="Save Category"> <input type="reset" class="btn-grey" value="Clear" id="" name="Reset" onclick="clearCategory()">
			</p>
		</div>
	</div>
</div>
<script type="text/javascript">
	configureMenu("setupalerts");
</script>
