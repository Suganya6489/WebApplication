<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<script type="text/javascript" src="scripts/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/jquery.form.js"></script>
<script src="/HubCiti/scripts/ckeditor/ckeditor.js"></script>
<script src="/HubCiti/scripts/ckeditor/config.js"></script>

<link rel="stylesheet" type="text/css" href="/HubCiti/styles/colorPicker.css" />

<script type="text/javascript">
	$(document).ready(function() {
	
		var uploadFileName = '${sessionScope.uploadedFile}';
		if (uploadFileName == null || uploadFileName == "") {
			document.getElementById('uploadFileDiv').style.display = "none";
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
	
		$(".exstngIcon").show();
		$(".TglLbl").text("Select Icons");
	
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
		var vIcnType = $("input[name='iconSelectHid']").val();
		var vPageType = document.screenSettingsForm.pageTypeHid.value;
		var iconSelect = document.screenSettingsForm.iconSelectHid.value;
	
		if (vIcnType.match(/(\icnSlctn)$/)) {
			$(".TglLbl").text("Upload Icon");
			$('#upldOwn').attr('checked', true);
			$(".cmnUpld").hide();
			$(".upldOwn").show();
		}
	
		if (null != vPageType && "" != vPageType) {
			if (vPageType == "Website") {
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
	
		if (iconSelect == "icnSlctn") {
			$('#upldOwn').attr("checked", true);
			$(".TglLbl").text("Upload Icon");
		} else {
			$('#exstngIcon').attr("checked", true);
			$(".TglLbl").text("Select Icons");
		}
	
	}
	
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
	}

	function saveImage(imageIconID) {
		document.screenSettingsForm.imageIconID.value = imageIconID;
	}

	/*function checkBannerSize(input) {
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
		}
	}

	function saveAnythingPageScreen() {
		document.screenSettingsForm.pathName.value = document.screenSettingsForm.pathName.value;
		document.screenSettingsForm.action = "saveanythingscreen.htm";
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
			<a href="#" onclick="revealPanel(this);" title="Hide Menu"><img src="images/slide_off.png" width="11" height="28" alt="btn_off" /> </a>
		</div>
		<div id="bread-crumb">
			<ul>
				<li class="scrn-icon"><span class="icon-home">&nbsp;</span></li>
				<li><a href="welcome.htm">Home</a></li>
				<li><a href="displayanythingpages.htm">AnyThing Page<sup>TM</sup> </a></li>
				<li class="last">Add AnyThing Page<sup>TM</sup></li>
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
							<li class="title-icon"><span class="icon-welcome">&nbsp;</span></li>
							<li>Create AnyThing Page<sup>TM</sup></li>
						</ul>
					</div>
					<div class="cont-wrphidoverflow">
						<form:form name="screenSettingsForm" id="screenSettingsForm" commandName="screenSettingsForm" enctype="multipart/form-data" action="uploadimg.htm">
							<form:hidden path="viewName" value="setupanythingpage" />
							<form:hidden path="iconSelectHid" />
							<form:hidden path="pageTypeHid" />
							<form:hidden path="imageIconID" />
							<form:hidden path="oldImageName" />
							<form:hidden path="pathName" value="${sessionScope.uploadedFile}" />
							<table width="100%" border="0" cellpadding="0" cellspacing="0" class="cmnTbl">
								<tr>
									<td width="42%"><label class="mand">Page Title</label></td>
									<td width="60%"><form:errors cssClass="errorDsply" path="pageTitle" />
										<div class="cntrl-grp">
											<form:input path="pageTitle" type="text" id="pageTitle" cssClass="inputTxtBig" maxlength="25" />
										</div></td>
								</tr>


								<tr>
									<td rowspan="2" align="left" valign="top">Icon Selection</td>
									<td><form:errors cssClass="errorDsply" path="logoImage" /> <form:input path="iconSelect" id="exstngIcon" value="exstngIcon" type="radio" name="iconSelect" /> <label for="exstngIcon">Existing Icon</label></td>
								</tr>
								<tr>
									<td><form:input path="iconSelect" id="upldOwn" value="icnSlctn" type="radio" name="iconSelect" /> <label for="upldOwn">Upload Icon</label></td>
								</tr>
								<tr>
									<td><label class="mand">Page Type</label></td>
									<td><div class="cntrl-grp zeroBrdr">
											<form:errors cssClass="errorDsply" path="pageType" />
											<form:select path="pageType" class="slctBx" onchange="onSelectExtnLink()">
												<form:option value="" label="--Select--" />
												<c:forEach items="${sessionScope.pageTypes}" var="pageTypes">
													<form:option value="${pageTypes.pageTypeId}" label="${pageTypes.pageTypeName}" />
												</c:forEach>
											</form:select>
										</div></td>
								</tr>

								<tr id="webLink" class="cmnOptn">
									<td width="42%"><label class="mand">Web Link</label></td>
									<td width="60%"><form:errors cssClass="errorDsply" path="pageAttachLink" />
										<div class="cntrl-grp">
											<form:input path="pageAttachLink" type="text" id="pageAttachLink" class="inputTxtBig" />
										</div></td>
								</tr>
								<tr id="fileUpld" class="cmnOptn">
									<td width="42%"><label class="mand">File</label></td>
									<td width="60%">
										<div class="cntrl-grp">
											<form:errors cssClass="errorDsply" path="filePaths" />
											<form:input class="inputTxtBig" id="fileName" path="filePaths" type="file" onchange="checkFileType(this);hideUploadDiv();" />
										</div>
										<div id="uploadFileDiv">
											<span>Uploaded File: </span>${sessionScope.uploadedFile}
										</div>
									</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td><input type="submit" name="button" id="button" value="Save" title="Save" class="btn-blue" onclick="saveAnythingPageScreen();" /></td>
								</tr>
							</table>
					</div>

				</div>
				<div class="cont-block">
					<div class="title-bar">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-iphone">&nbsp;</span></li>
							<li class="TglLbl">Select Icons</li>
						</ul>
					</div>
					<div class="cont-wrp grey-bg mnHt">
						<ul class="iconsPnl cmnUpld exstngIcon">
							<c:forEach items="${sessionScope.arHcImageList}" var="s">
								<c:choose>
									<c:when test="${sessionScope.ImageIcon eq s.customPageIconID}">
										<li class="active"><a href="#"> <img src="${s.imagePath}" alt="hcImages" id="${s.customPageIconID}" name="retPageImg" onclick="javascript:saveImage('${s.customPageIconID}');" /> </a>
										</li>
									</c:when>
									<c:otherwise>
										<li class=""><a href="#"> <img src="${s.imagePath}" alt="hcImages" id="${s.customPageIconID}" name="retPageImg" onclick="javascript:saveImage('${s.customPageIconID}');" /> </a>
										</li>
									</c:otherwise>
								</c:choose>

							</c:forEach>
						</ul>
						<span class="clear"></span>
						<div class="cmnUpld upldOwn">
							<table width="100%" border="0" cellpadding="0" cellspacing="0" class="cmnTbl">

								<tr id="fileUpld">
									<td width="33%"><label class="mand">Upload Icon</label></td>
									<td width="64%">
										<form:errors cssClass="errorDsply" path="logoImageName"></form:errors> 
										<label> 
											<img id="uploadImg" width="50" height="50" alt="upload" src="${sessionScope.anythingScreenImage }"> 
										</label> <span class="topPadding cmnRow"> 
										<label for="trgrUpld"> 
											<input type="button" value="Upload" id="trgrUpldBtn" class="btn trgrUpld" title="Upload Image File"> 
											<form:hidden path="logoImageName" id="logoImageName" /> 
											<span class="instTxt nonBlk"></span> 
											<form:input type="file" class="textboxBig" id="trgrUpld" path="logoImage" onchange="checkBannerSize(this);" /> 
											</label> </span><label id="maxSizeImageError" class="errorDsply"></label>
									</td>
								</tr>
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
			<img src="/HubCiti/images/popupClose.png" class="closeIframe" alt="close" onclick="javascript:closeIframePopup('ifrmPopup','ifrm')" title="Click here to Close" align="middle" /> <span id="popupHeader"></span>
		</div>
		<iframe frameborder="0" scrolling="auto" id="ifrm" src="" height="100%" allowtransparency="yes" width="100%" style="background-color: White"> </iframe>
	</div>

</div>

<script type="text/javascript">
	configureMenu("setupanythingpage");
</script>