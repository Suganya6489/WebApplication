<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<script type="text/javascript" src="scripts/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/jquery.form.js"></script>
<script type="text/javascript"
	src="/HubCiti/scripts/colorPickDynamic.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/colorPicker.js"></script>
<script src="/HubCiti/scripts/ckeditor/ckeditor.js"></script>
<script src="/HubCiti/scripts/ckeditor/config.js"></script>

<link rel="stylesheet" type="text/css"
	href="/HubCiti/styles/colorPicker.css" />

<script type="text/javascript">
	$(document).ready(function() {

		var uploadFileName = '${sessionScope.fileName}';
		var uploadFilePath = '${sessionScope.filePath}';
		var displayFlag = false;
		
		if (uploadFilePath != null && uploadFilePath != "") {
			var uploadFileName = '${sessionScope.uploadedFile}';
			if (uploadFileName == null || uploadFileName == "") {
				document.getElementById('uploadFileDiv').style.display = "none";
				document.screenSettingsForm.pathName.value = '${sessionScope.fileName}';
			}
		} 
		else {
			displayFlag = true;
		}
		if (displayFlag) {
			document.getElementById('upldList').style.display = "none";
			var uploadFileName = '${sessionScope.uploadedFile}';
			if (uploadFileName == null || uploadFileName == "") {
				document.getElementById('uploadFileDiv').style.display = "none";
			}
		}
		
		$("input[name='iconSelect']:radio").change(function() {
			var slctOptn = $(this).attr("id");
			$(".cmnUpld").hide();
			$("." + slctOptn).slideDown();

			if (slctOptn == "exstngIcon") {
				$(".TglLbl").text("Select Icons");
			}
			if (slctOptn == "upldOwn") {
				$(".TglLbl").text("Upload Icon");
			}
		});
		/*To Set equal heights for the divs pass div class names as parameter*/
		setequalHeight($("#equalHt .cont-block"));
		setequalHeight($(".cntrl-grp input"));
		$("#menu-pnl").height($(".content").height());

		$("input:radio[name=iconSelect]").click(function() {
			var value = $(this).val();
			document.screenSettingsForm.iconSelectHid.value = value;
		});

	});

	window.onload = function() {
		var iconSelect = document.screenSettingsForm.iconSelectHid.value;

		if (null != iconSelect && "" != iconSelect) {
			if (iconSelect.match(/(\existing)$/)) {
				$(".TglLbl").text("Select Icons");
				$('#exstngIcon').attr('checked', true);
				$(".cmnUpld").hide();
				$(".exstngIcon").show();
			} else {
				$(".TglLbl").text("Upload Icon");
				$('#upldOwn').attr('checked', true);
				$(".cmnUpld").hide();
				$(".upldOwn").show();
			}
		} else {
			var iconSelect = '${sessionScope.iconSelection}';
			if (iconSelect.match(/(\existing)$/)) {
				$(".TglLbl").text("Select Icons");
				$('#exstngIcon').attr('checked', true);
				$(".cmnUpld").hide();
				$(".exstngIcon").show();
			} else {
				$(".TglLbl").text("Upload Icon");
				$('#upldOwn').attr('checked', true);
				$(".cmnUpld").hide();
				$(".upldOwn").show();
			}
		}

		var vPageType = document.screenSettingsForm.pageTypeHid.value;

		if (null != vPageType && "" != vPageType) {
			$('#pageType > option').each(function() {
				if ($(this).text() == vPageType) {
				$("#pageType option:selected").attr("selected", false);					 					
					this.selected = true;
					return false;
				}
			});

			if (vPageType == "Website") {
				$(".cmnOptn").hide();
				$("#webLink").show();
			} else {
				$(".cmnOptn").hide();
				$("#fileUpld").show();
			}

			var fileType = "${sessionScope.anythingPage.pageType}";

			if (fileType == vPageType) {
				$("#upldList").show();
			} else {
				$("#upldList").hide();
			}
		} else {
			var vPageType = '${sessionScope.anythingPage.pageType}';
			if (null != vPageType && "" != vPageType) {
				if (vPageType == 4) {
					$(".cmnOptn").hide();
					$("#webLink").show();
				} else {
					$(".cmnOptn").hide();
					$("#fileUpld").show();
				}
			} else {
				$(".cmnOptn").hide();
				$("#webLink").hide();
				$("#fileUpld").hide();

			}
		}

		document.screenSettingsForm.pageTypeHid.value = $("#pageType option:selected").text();

	}
	//To Set equal heights for the divs
	function setequalHeight(obj) {
		tallest = 0;
		obj.each(function() {
			curHeight = $(this).height();
			if (curHeight > tallest) {
				tallest = curHeight;
			}
		});
		obj.height(tallest);
	}

	function onSelectExtnLink() {
		document.screenSettingsForm.filePaths.value = "";
		var control = $("#fileName");
		control.replaceWith(control = control.clone(true));
		if (document.getElementById('filePaths.errors') != null) {
			document.getElementById('filePaths.errors').style.display = 'none';
		}
		var val = $("#pageType option:selected").text();
		$('#uploadFileDiv').hide();
		if (val == "Website") {
			$(".cmnOptn").hide();
			$("#webLink").show();
			document.screenSettingsForm.pageTypeHid.value = val;
		} else {

			$("#webLink").hide();
			$("#fileUpld").show();
			document.screenSettingsForm.pageTypeHid.value = val;

		}

		var val = $("#pageType option:selected").val();
		var fileType = "${sessionScope.anythingPage.pageType}";

		if (fileType == val) {
			$("#upldList").show();
			document.screenSettingsForm.pathName.value = '${sessionScope.fileName}';
		} else {
			$("#upldList").hide();
			document.screenSettingsForm.pathName.value = '';
		}
	}

	function saveImage(imageIconID) {
		document.screenSettingsForm.imageIconID.value = imageIconID;
	}

	/*function checkBannerSize(input) {
		document.screenSettingsForm.lowerLimit.value = '${requestScope.lowerLimit}';
		var bannerImage = document.getElementById("trgrUpld").value;
		if (bannerImage != '') {
			var checkbannerimg = bannerImage.toLowerCase();
			if (!checkbannerimg.match(/(\.png|\.jpeg|\.jpg|\.gif|\.bmp)$/)) {
				alert("You must upload Banner image with following extensions : .png, .gif, .bmp, .jpg, .jpeg");
				document.screenSettingsForm.logoImage.value = "";
				document.getElementById("trgrUpld").focus();
				return false;
			}
		}
	}*/

	function checkFileType(input) {
		var vFileType = document.getElementById("fileName").value;
		var vPageType = document.screenSettingsForm.pageTypeHid.value;

		//alert(vPageType)

		if (vFileType != '') {
			
			if (vPageType == "PDF") {
				var vCheckFileType = vFileType.toLowerCase();
				if (!vCheckFileType.match(/(\.pdf)$/)) {
					alert("You must upload file with following extensions : .pdf");
					document.screenSettingsForm.filePaths.value = "";
					var control = $("#fileName");
					control.replaceWith(control = control.clone(true));
					document.getElementById("fileName").focus();
					return false;
				}
			} else if (vPageType == "Video") {
				var vCheckFileType = vFileType.toLowerCase();
				if (!vCheckFileType.match(/(\.mp4|\.m4v|\.mov)$/)) {
					alert("You must upload file with following extensions : .mp4, .m4v, .mov");
					document.screenSettingsForm.filePaths.value = "";
					var control = $("#fileName");
					control.replaceWith(control = control.clone(true));
					document.getElementById("fileName").focus();
					return false;
				}
			} else if (vPageType == "Audio") {
				var vCheckFileType = vFileType.toLowerCase();
				if (!vCheckFileType.match(/(\.mp3|\.wav)$/)) {
					alert("You must upload file with following extensions : .mp3, .wav");
					document.screenSettingsForm.filePaths.value = "";
					var control = $("#fileName");
					control.replaceWith(control = control.clone(true));
					document.getElementById("fileName").focus();
					return false;
				}
			} else if (vPageType == "Image") {
				var vCheckFileType = vFileType.toLowerCase();
				if (!vCheckFileType.match(/(\.png|\.jpeg|\.jpg|\.gif|\.bmp)$/)) {
					alert("You must upload file with following extensions : .png, .gif, .bmp, .jpg, .jpeg");
					document.screenSettingsForm.filePaths.value = "";
					var control = $("#fileName");
					control.replaceWith(control = control.clone(true));
					document.getElementById("fileName").focus();
					return false;
				}
			}
			document.getElementById('upldList').style.display = "none";
			document.getElementById('uploadFileDiv').style.display = "none";			
		}
	}

	function updateAnythingPageScreen() {
		var fileType = "${sessionScope.anythingPage.pageType}";
		document.screenSettingsForm.hiddenBtnLinkId.value = document.screenSettingsForm.hiddenBtnLinkId.value;
		var val = $("#pageType option:selected").text();

		if (val != "Website") {
			/*if (fileType == document.screenSettingsForm.pageType.value) {
				document.screenSettingsForm.pathName.value = document.screenSettingsForm.pathName.value;
			} else {
				document.screenSettingsForm.pathName.value = "";
			}*/
		}
		document.screenSettingsForm.lowerLimit.value = '${requestScope.lowerLimit}';
		document.screenSettingsForm.action = "updateanythingscreen.htm";
		document.screenSettingsForm.method = "POST";
		document.screenSettingsForm.submit();
	}

	function hideUploadDiv() {
		$("#uploadFileDiv").hide();
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
				<li><a href="displayanythingpages.htm">AnyThing Page<sup>TM</sup>
				</a></li>
				<li class="last">Edit AnyThing Page<sup>TM</sup></li>
			</ul>
		</div>
		<span class="blue-brdr"></span>
		<div class="content" id="login">
			<div id="menu-pnl" class="split">
				<jsp:include page="leftNavigation.jsp"></jsp:include>
			</div>
			<div class="cont-pnl split" id="equalHt">
				<div class="cont-block rt-brdr">
					<div class="title-bar">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-welcome">&nbsp;</span>
							</li>
							<li>Edit AnyThing Page<sup>TM</sup></li>
						</ul>
					</div>
					<div class="cont-wrphidoverflow">
						<c:if
							test="${requestScope.responseStatus ne null && !empty requestScope.responseStatus}">
							<c:choose>
								<c:when test="${requestScope.responseStatus eq 'SUCCESS' }">
									<div class="alertBx success">
										<span class="actn-close" title="close"></span>
										<p class="msgBx">
											<c:out value="${requestScope.responeMsg}" />
										</p>
									</div>
								</c:when>
								<c:otherwise>
									<div class="alertBx failure">
										<span class="actn-close" title="close"></span>
										<p class="msgBx">
											<c:out value="${requestScope.responeMsg}" />
										</p>
									</div>
								</c:otherwise>
							</c:choose>
						</c:if>
						<form:form name="screenSettingsForm" id="screenSettingsForm"
							commandName="screenSettingsForm" enctype="multipart/form-data"
							action="uploadimg.htm">
							<form:hidden path="hiddenBtnLinkId"
								value="${sessionScope.anythingPageId}" />
							<form:hidden path="viewName" value="editanythingpage" />
							<form:hidden path="lowerLimit" />
							<form:hidden path="iconSelectHid" />
							<form:hidden path="pageTypeHid" />
							<form:hidden path="oldImageName"
								value="${sessionScope.anythingPage.imageName}" />
							<form:hidden path="oldFileName" value="${sessionScope.oldFileName}"/>
							<form:hidden path="imageIconID"
								value="${sessionScope.anythingPage.imageIconID}" />
							<form:hidden path="pathName" value="${sessionScope.uploadedFile}" />
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="cmnTbl">
								<tr>
									<td width="42%"><label class="mand">Page Title</label></td>
									<td width="60%"><form:errors cssClass="errorDsply"
											path="pageTitle" />
										<div class="cntrl-grp">
											<form:input path="pageTitle" type="text" id="pageTitle" value='${sessionScope.anythingPage.pageTitle}' cssClass="inputTxtBig" maxlength="25" />
										</div>
									</td>
								</tr>


								<tr>
									<td rowspan="2" align="left" valign="top">Icon Selection</td>
									<td><form:errors cssClass="errorDsply" path="logoImage" /> <form:input path="iconSelect" id="exstngIcon" value="exstngIcon" type="radio" name="iconSelect" /> <label for="exstngIcon">Existing Icon</label>
									</td>
								</tr>
								<tr>
									<td><form:input path="iconSelect" id="upldOwn" value="icnSlctn" type="radio" name="iconSelect" /> <label for="upldOwn">Upload Icon</label>
									</td>
								</tr>
								<tr>
									<td><label class="mand">Page Type</label></td>
									<td><div class="cntrl-grp zeroBrdr">
											<form:errors cssClass="errorDsply" path="pageType" />
											<form:select path="pageType" class="slctBx" onchange="onSelectExtnLink()">
												<form:option value="" label="--Select--" />
												<c:forEach items="${sessionScope.pageTypes}" var="pageTypes">
													<c:choose>
														<c:when
															test="${sessionScope.anythingPage.pageType eq pageTypes.pageTypeId}">
															<form:option value="${pageTypes.pageTypeId}"
																label="${pageTypes.pageTypeName}" selected="selected" />
														</c:when>
														<c:otherwise>
															<form:option value="${pageTypes.pageTypeId}"
																label="${pageTypes.pageTypeName}" />
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</form:select>
										</div></td>
								</tr>

								<tr id="webLink" class="cmnOptn">
									<td width="42%"><label class="mand">Web Link</label></td>
									<td width="60%"><form:errors cssClass="errorDsply"
											path="pageAttachLink" />
										<div class="cntrl-grp">
											<form:input path="pageAttachLink" type="text"
												id="pageAttachLink"
												value='${sessionScope.anythingPage.pageAttachLink}'
												cssClass="inputTxtBig" />
										</div></td>
								</tr>
								<tr id="fileUpld" class="cmnOptn">
									<td width="42%"><label class="mand">File</label></td>
									<td width="64%">
										<div class="cntrl-grp">
											<form:errors cssClass="errorDsply" path="filePaths" />
											<form:input class="inputTxtBig" id="fileName"
												path="filePaths" type="file" onchange="checkFileType(this);hideUploadDiv();" />
										</div> 
										<div id="upldList" class="upldList">
											<a title="view" target="_blank"
												href="${sessionScope.filePath}">${sessionScope.fileName}</a>
										</div>
									
										<div id="uploadFileDiv">
											<span>Uploaded File:</span>${sessionScope.uploadedFile}
										</div>
									</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td><input type="button" onclick="updateAnythingPageScreen();" class="btn-blue" title="Update AnyThing Page" value="Update" id="button" name="button">
									</td>
								</tr>
							</table>
					</div>

				</div>
				<div class="cont-block">
					<div class="title-bar">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-iphone">&nbsp;</span>
							</li>
							<li class="TglLbl">Select Icons</li>
						</ul>
					</div>
					<div class="cont-wrp grey-bg mnHt">
						<ul class="iconsPnl cmnUpld exstngIcon">
							<c:forEach items="${sessionScope.arHcImageList}" var="s">
								<c:choose>
									<c:when
										test="${s.customPageIconID eq sessionScope.anythingPage.imageIconID}">
										<li class="active"><a href="#"> <img
												src="${s.imagePath}" alt="hcImages"
												id="${s.customPageIconID}" name="retPageImg"
												onclick="javascript:saveImage('${s.customPageIconID}');" />
										</a></li>
									</c:when>
									<c:otherwise>
										<li class=""><a href="#"> <img src="${s.imagePath}"
												alt="hcImages" id="${s.customPageIconID}" name="retPageImg"
												onclick="javascript:saveImage('${s.customPageIconID}');" />
										</a></li>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</ul>
						<span class="clear"></span>
						<div class="cmnUpld upldOwn">
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="cmnTbl">

								<tr id="fileUpld">
									<td width="33%"><label class="mand">Upload Icon</label>
									<td width="64%">
									<form:errors cssClass="errorDsply" path="logoImageName"></form:errors> 
									<label> 
									<img id="uploadImg" width="50" height="50" alt="upload" src="${sessionScope.editAnythingScreenImage }"> 
									<form:hidden path="imageName" id="imageName" value="${sessionScope.anythingPage.imageName }" /> 
									</label> 
									<span class="topPadding cmnRow"> 
									<label for="trgrUpld"> 
									<input type="button" value="Upload" id="trgrUpldBtn" class="btn trgrUpld" title="Upload Image File">
									<form:hidden path="logoImageName" id="logoImageName" /> 
									<span class="instTxt nonBlk"></span> 
									<form:input type="file" class="textboxBig" id="trgrUpld" path="logoImage" onchange="checkBannerSize(this);" /> 
									</label> </span>
									<label id="maxSizeImageError" class="errorDsply"></label>
									</td>
								</tr>
								<tr>
							</table>
						</div>

					</div>
				</div>
				</form:form>
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

<script type="text/javascript">
	configureMenu("setupanythingpage");
</script>