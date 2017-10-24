<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<script type="text/javascript" src="/HubCiti/scripts/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/farbtastic/farbtastic.js"></script>
<link rel="stylesheet" href="/HubCiti/styles/farbtastic/farbtastic.css" type="text/css" />

<div class="wrpr-cont relative">
	<div id="slideBtn">
		<a href="#" onclick="revealPanel(this);" title="Hide Menu"><img src="images/slide_off.png" width="11" height="28" alt="btn_off" />
		</a>
	</div>
	<div id="bread-crumb">
		<ul>
			<li class="scrn-icon"><span class="icon-home">&nbsp;</span>
			</li>
			<li>Home</li>
			<li class="last"><a href="#">Dashboard</a>
			</li>
		</ul>
	</div>
	<span class="blue-brdr"></span>
	<div class="content" id="login">
		<div id="menu-pnl" class="split">
			<ul id="icon-menu">
				<li class="active"><a href="#" class="icon-login" title="Setup Login Screen"><span>Setup Login Screen</span>
				</a>
				</li>
				<li><a href="#" class="icon-about" title="Setup About Us"><span>Setup About Us</span>
				</a>
				</li>
				<li><a href="#" class="icon-menu" title="Setup Main Menu"><span>Setup Main Menu</span>
				</a>
				</li>
				<li><a href="#" class="icon-alert" title="Setup Alerts"><span>Setup Alerts</span>
				</a>
				</li>
				<li><a href="#" class="icon-event" title="Setup Events"><span>Setup Events</span>
				</a>
				</li>
				<li><a href="#" class="icon-news" title="Setup News"><span>Setup News</span>
				</a>
				</li>
			</ul>
		</div>
		<div class="cont-pnl split" id="equalHt">
			<div class="cont-block rt-brdr">
				<div class="title-bar">
					<ul class="title-actn">
						<li class="title-icon"><span class="icon-login">&nbsp;</span>
						</li>
						<li>Login Details</li>
					</ul>
				</div>
				<div class="cont-wrp">
					<form:form name="loginDetailForm" commandName="loginDetailForm" enctype="multipart/form-data" action="uploadimg.htm">
						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="cmnTbl">
							<tr>
								<td width="33%">Choose Logo</td>

								<td width="67%"><label><img id="bannerADImg" width="80" height="80" alt="upload" src="../images/upload_image.png"> </label> <span class="topPadding"><label for="trgrUpld"> <input type="button" value="Upload" id="trgrUpldBtn" class="btn trgrUpld" title="Upload Image File" tabindex="4"> <form:hidden path="logoImagePath" name="strBannerAdImagePath" id="strBannerAdImagePath" /> <span class="instTxt nonBlk"></span> <input type="file" class="textboxBig"
											id="trgrUpld" onchange="checkBannerSize(this);" /> </label> </span>
								</td>

							</tr>
							<tr>
								<td valign="top"><span class="pick-color-label">Background Color</span>
								</td>
								<td>
									<div class="cntrl-grp">
										<form:input type="text" path="bgColor" class="inputTxt" id="bgColor" name="bgColor" value="#123456" />


									</div>
									<div id="bgClrPicker"></div></td>
							</tr>
							<tr>

								<td valign="top"><span class="pick-color-label">Font Color</span>
								</td>
								<td>
									<div class="cntrl-grp">
										<form:input type="text" path="fontColor" class="inputTxt" id="fontColor" name="fontColor" value="#123456" />


									</div>
									<div id="fontClrPicker"></div></td>
							</tr>
							<tr>
								<td valign="top"><span class="pick-color-label">Button Color</span>
								</td>
								<td><div class="cntrl-grp">
										<form:input type="text" path="btnColor" class="inputTxt" name="btnColor" value="#123456" id="btnColor" />
									</div>
									<div id="btnClrPicker"></div>
								</td>
							</tr>
							<tr>
								<td valign="top"><span class="pick-color-label">Button Font Color</span>
								</td>
								<td><div class="cntrl-grp">
										<form:input type="text" path="btnFontColor" class="inputTxt" name="btnFontColor" value="#123456" id="btnFontColor" />
									</div>

									<div id="btnFntClrPicker"></div>
								</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><input type="submit" name="button" id="button" value="Save Settings" class="btn-blue" />
								</td>
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
						<li>iPhone Preview</li>
					</ul>
				</div>
				<div class="cont-wrp">
					<div id="iphone-preview">
						<!--Iphone Preview For Login screen starts here-->
						<div id="iphone-login-preview">
							<div class="iphone-status-bar"></div>
							<div class="iPhone-preview-container">
								<div class="iphone-login-logo">
									<img src="images/dummy-logo.png" alt="Logo" width="200" height="90" />
								</div>
								<ul>
									<li><label>Username</label> <span> <input type="text" id="usernameText" name="usernameText" /> </span>
									</li>
									<li class="min-margin-top"><label>Password</label> <span> <input type="password" id="passwordBox" name="passwordBox" /> </span>
									</li>
									<li><input type="button" id="signUp" name="signUp" value="Sign Up" />
									</li>
								</ul>
							</div>
							<div class="powered-by-logo">
								<lable>Powered by</lable>
								<span class="powered-logo"></span>
							</div>
						</div>
						<!--Iphone Preview For Login screen ends here-->
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		$('#bgClrPicker').hide();
		$('#fontClrPicker').hide();
		$('#btnClrPicker').hide();
		$('#btnFntClrPicker').hide();

	});

	$('#bgColor').focus(function() {
		$('#bgClrPicker').farbtastic('#bgColor');
		$('#bgClrPicker').show();
		$('#fontClrPicker').hide();
		$('#btnFntClrPicker').hide();
		$('#btnClrPicker').hide();

	});
	$('#fontColor').focus(function() {
		$('#fontClrPicker').farbtastic('#fontColor');
		$('#fontClrPicker').show();
		$('#bgClrPicker').hide();
		$('#btnFntClrPicker').hide();
		$('#btnClrPicker').hide();
	});

	$('#btnColor').focus(function() {
		$('#btnClrPicker').farbtastic('#btnColor');
		$('#btnClrPicker').show();
		$('#fontClrPicker').hide();
		$('#bgClrPicker').hide();
		$('#btnFntClrPicker').hide();

	});

	$('#btnFontColor').focus(function() {
		$('#btnFntClrPicker').farbtastic('#btnFontColor');
		$('#btnFntClrPicker').show();
		$('#fontClrPicker').hide();
		$('#bgClrPicker').hide();
		$('#btnClrPicker').hide();

	});

	$('#bgClrPicker').farbtastic(function(color) {
		$('#bgColor').val(color)
		$("#iphone-login-preview").css("background-color", color);
	});
	$('#fontClrPicker').farbtastic(function(color) {
		$('#fontColor').val(color)
		$("#iphone-login-preview label").css("color", color);

	});

	$('#btnClrPicker').farbtastic(
			function(color) {
				$('#btnColor').val(color)
				$("#iphone-login-preview input[type='button']").css(
						"background-color", color);
			});
	$('#btnFntClrPicker').farbtastic(function(color) {
		$('#btnFontColor').val(color)
		$("#iphone-login-preview input[type='button']").css("color", color);
	});
</script>
<script type="text/javascript">
	$('#trgrUpld')
			.bind(
					'change',
					function() {
						$("#uploadBtn").val("trgrUpldBtn")
						$("#createbanneradform")
								.ajaxForm(
										{
											success : function(response) {

												var imgRes = response
														.getElementsByTagName('imageScr')[0].firstChild.nodeValue

												if (imgRes == 'UploadLogoMaxSize') {
													$('#bannerAdImagePathErr')
															.text(
																	"Image Dimension should not exceed Width: 950px Height: 1024px");
												} else if (imgRes == 'UploadLogoMinSize') {
													$('#bannerAdImagePathErr')
															.text(
																	"Image Dimension should be Minimum Width: 320px Height: 460px");
												} else {
													$('#bannerAdImagePathErr')
															.text("");
													var substr = imgRes
															.split('|');

													if (substr[0] == 'ValidImageDimention') {
														var imgName = substr[1];
														$(
																'#strBannerAdImagePath')
																.val(imgName);
														$('#bannerADImg').attr(
																"src",
																substr[2]);
													} else {
														openIframePopup(
																'ifrmPopup',
																'ifrm',
																'/ScanSeeWeb/retailer/cropImageGeneral.htm',
																750, 950,
																'Crop Image');
													}

												}

											}
										}).submit();

					});
</script>