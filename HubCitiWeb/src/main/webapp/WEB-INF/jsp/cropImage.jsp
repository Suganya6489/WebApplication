<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Image Crop</title>
<link rel="stylesheet" type="text/css" href="/HubCiti/styles/styles.css" />
<script type="text/javascript" src="/HubCiti/scripts/jquery.min.js"></script>

<script src="/HubCiti/scripts/global.js" type="text/javascript"></script>

<script src="/HubCiti/scripts/jquery.Jcrop.min.js"></script>
<link rel="stylesheet" href="/HubCiti/styles/jquery.Jcrop.css" />
<style type="text/css">
.image {
	position: relative;
	width: 100%; /* for IE 6 */
}

h2 {
	position: absolute;
	width: 100%;
}

h2 span {
	color: white;
	font: bold 7px/7px Helvetica, Sans-Serif;
	letter-spacing: 2px;
	background: rgb(0, 0, 0); /* fallback color */
	background: rgba(0, 0, 0, 0.0);
	padding: 5px;
}

.arrowDownImgId {
	color: white;
	background: rgb(0, 0, 0); /* fallback color */
	background: rgba(0, 0, 0, 0.0);
	padding: 5px;
}

h2 span.spacer {
	padding: 0 142px;
}

h2 span.spacer1 {
	padding-left: 2pt;
}
</style>

<script type="text/javascript">
	var imageCropPage = '${sessionScope.imageCropPage}';

	var imgHt;
	var imgWd;
	var flag = true;
	$(document).ready(function() {
		imgHt = '${sessionScope.imageHt}';
		imgWd = '${sessionScope.imageWd}';
		imgHt = imgHt / 2;
		imgWd = imgWd / 2;
		setTimeout(cropImage, 200);
	});

	function submitCroppedImage() {

		uploadLoginScreenCroppedImage(xPixel, yPixel, wPixel, hPixel);

	}

	function uploadLoginScreenCroppedImage(x, y, w, h) {

		if (imageCropPage == 'Events') {
			top.document.screenSettingsForm.action = "/HubCiti/uploadcroppedevntimage.htm?x="
					+ x + "&y=" + y + "&w=" + w + "&h=" + h;// +"&imgValue="+img;
			top.document.screenSettingsForm.method = "POST";
			top.document.screenSettingsForm.submit();
		} else {
			top.document.screenSettingsForm.action = "/HubCiti/uploadcroppedimage.htm?x="
					+ x + "&y=" + y + "&w=" + w + "&h=" + h;// +"&imgValue="+img;
			top.document.screenSettingsForm.method = "POST";
			top.document.screenSettingsForm.submit();
		}

	}

	function showCoords(c) {

		if (flag) {
			$('#initialText').show();
			$('#arrowDownImgId').show();
			$('h2').css({
				"left" : (c.x - 150) + 'px'
			});
			$('h2').css({
				"top" : (c.y2 - 110) + 'px'
			});
			flag = false;
		} else {
			$('#initialText').hide();
			$('#arrowDownImgId').hide();
		}

		xPixel = c.x;
		yPixel = c.y;
		wPixel = c.w;
		hPixel = c.h;
		// jQuery('#x').val(c.x);
		// jQuery('#y').val(c.y);
		// jQuery('#x2').val(c.x2);
		// jQuery('#y2').val(c.y2);
		// jQuery('#w').val(c.w);
		// jQuery('#h').val(c.h);
	};
	function cropImage() {
		var minHt;
		var minWd;
		minWd = '${sessionScope.minCropWd}';
		minHt = '${sessionScope.minCropHt}';
		jQuery(function($) {
			$('#imgCrop').Jcrop({
				onSelect : showCoords,
				bgColor : 'black',
				bgOpacity : .2,
				minSize : [ minWd, minHt ],
				setSelect : [ imgHt, imgWd, imgHt - 40, imgWd - 40 ],
				aspectRatio : minWd / minHt
			});
		});
	}
</script>
</head>
<body class="whiteBG">
	<div class="contBlock">
		<fieldset class="popUpSrch">

			<div class="grdCont searchGrd">

				<p align="center">
					To crop this image, drag the region below and then click "Save
					Image" <br /> <br /> Click and drag the handles to resize the
					crop box <br /> <br /> <input type="button" class="btn"
						id="saveButton" value="Save Image" onclick="submitCroppedImage();"
						title="Save Image" /><br />
				</p>
				<br>
					<div class="image">
						<img src='<%=session.getAttribute("cropImageSource")%>'
							id="imgCrop" /> <span id="initialText">
							<h2 align="center">
								<!-- Click and drag the handles to resize the crop box <br> <span
										class='spacer'><img
											src='/HubCiti/images/arrow-down.png' /> <span
											style="padding-left: 6em;"><img
												src='/HubCiti/images/arrow-down.png' /> </span> </span> -->
							</h2>
						</span>
					</div>
					<p align="center" class="MrgnTop">
						<input type="button" class="btn" id="saveButton"
							value="Save Image" onclick="submitCroppedImage();" />
					</p>
			</div>
		</fieldset>
	</div>
	<script>
		$(window).load(
				function() {

					var bodyHt = $("body").height();
					var ifrmHt = $('#ifrm', parent.document).height();

					if (bodyHt > ifrmHt) {

						$('#ifrmPopup', parent.document).height(
								$("body").height());
						$('#ifrm', parent.document).height($("body").height());
					} else {

						$("body").height($('#ifrm', parent.document).height());
						$('#ifrmPopup', parent.document).height(
								$("body").height() + 20);

					}

				});
	</script>
</body>
</html>
