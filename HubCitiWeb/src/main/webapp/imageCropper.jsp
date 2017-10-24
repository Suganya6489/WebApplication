<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>

<head>

<style>
#popup {
	border: 0px solid gray;
	border-radius: 5px 5px 5px 5px;
	float: left;
	left: 20%;
	margin: auto;
	position: fixed;
	top: 160px;
	z-index: 9999;
}
</style>
</head>

<script type="text/javascript" src="/HubCiti/scripts/jquery.min.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/jquery.form.js"></script>
<script src="/HubCiti/scripts/jquery.Jcrop.min.js"></script>
<link rel="stylesheet" href="/HubCiti/styles/jquery.Jcrop.css"
	type="text/css" />

<body>
	<div id="header"></div>
	<form:form name="uploadImageform"
		action="/HubCiti/uploadCroppedImage.htm" method="post"
		enctype="multipart/form-data">
		<h1>Upload Image</h1>
		<br>
		<br>
	        Upload your image<!--  <input type="file" name="photoimg" id="photoimg" onChange="readURL(this);"/> -->
		<input type="file" class="textboxBig" id="uploadImage"
			onChange="readURL(this);" />
		<!-- <label>X1</label> -->
		<input type="hidden" size="4" id="x" name="x" />
		<input type="hidden" size="4" id="y" name="y" />
		<input type="hidden" size="4" id="w" name="w" />
		<input type="hidden" size="4" id="h" name="h" />
	</form:form>
	<div id="popup" style="display: none">
		<p>
			To crop this image, drag the region below and then click "Set as
			logo" <a style="padding-left: 159px" id="popup-close" href=""
				class="button"><img src="/HubCiti/images/actnClose.png"
				alt="close" /></a>
		</p>
		<p>
			<img id="imgId" src="" height="600px" width="600px"><br> <br>
			<!--  <input type="button" id="cropButton" value="Crop"/>
                <input type="button" id="cancelButton" value="Cancel"/> -->
			<span class="text-align:center"><input type="button"
				id="saveButton" value="Set as logo" onclick="saveCroppedImage();" /></span>
		</p>
	</div>

	<script type="text/javascript">
		var jcrop_api;

		function readURL(input) {

			$("#imgId").attr("src", "");
			stopJcrop();
			if (input.files && input.files[0]) {
				var reader = new FileReader();
				reader.onload = function(e) {
					alert("File Read Complete!!");
					$("#imgId").attr("src", e.target.result);
					// jcrop_api.setImage($("#imgId").attr("src")); 
				};
				reader.readAsDataURL(input.files[0]);
				$("#popup:visible").hide(); //hide popup if it is open
				// e.preventDefault(); // don't follow link
				//  $("#imgId").attr("src", $(this).attr("href")); // replace image src with href from the link that was clicked
				//  alert("Src Value : "+ $("#imgId").attr("src").value);
				$("#popup").fadeIn("fast"); //show popup

				//initJcrop('imgId'); 
				$("#imgId").focus(function() {
					alert("Initiating Cropping!!");
					initJcrop('imgId');
				});
				$("#imgId").focusout(function() {
					alert("Disabling Cropping!!");
					stopJcrop();
				});
				setTimeout(function() {
					$('#imgId').focus();
				}, 1500);
			}
		}

		function initJcrop(id) {
			document.getElementById("uploadImage").disabled = true;
			jcrop_api = $.Jcrop('#' + id, {

				onSelect : showCoords,
				onChange : showCoords,
				bgColor : 'black',
				bgOpacity : .4,
				aspectRatio : 1,
				minSize : [ 70, 70 ],
				maxSize : [ 70, 70 ],
				// boxWidth:   picture_width,
				// boxHeight:  picture_height,
				setSelect : [ 100, 100, 100 + 70, 100 + 70 ]
			});
		}

		function stopJcrop() {
			if (jcrop_api) {
				jcrop_api.destroy();
				return (false);
			}

		}

		function showCoords(c) {
			jQuery('#x').val(c.x);
			jQuery('#y').val(c.y);
			// jQuery('#x2').val(c.x2);
			// jQuery('#y2').val(c.y2);
			jQuery('#w').val(c.w);
			jQuery('#h').val(c.h);
		};

		/*  function enableCropButton()
		 {          
		 	stopJcrop();
		 	document.getElementById("cropButton").disabled = false;
		 }; */

		function saveCroppedImage() {
			if (parseInt($('#w').val())) {
				/* window.opener.document.getElementById("xId").value = jQuery('#x').val();
				window.opener.document.getElementById("yId").value = jQuery('#y').val();
				window.opener.document.getElementById("wId").value = jQuery('#w').val();
				window.opener.document.getElementById("hId").value = jQuery('#h').val(); */

				submitCroppedImage();

			} else {
				alert('Please select a crop region then press submit.');
			}

		};

		function submitCroppedImage() {
			var imagePath = jQuery('#uploadImage').val();
			alert("Image Path :" + imagePath)
			if (imagePath == '' || imagePath == 'undefined') {
				alert('Please Select Retailer Logo to upload')
				return false;
			} else {

				//window.opener.document.getElementById("uploadImage").value =  jQuery('#photoimg').val();
				//window.opener.document.retaileruploadlogoinfoform.action = "/HubCiti/retailer/uploadCroppedLogo.htm?x="+jQuery('#x').val()+"&y="+jQuery('#y').val()+"&w="+jQuery('#w').val()+"&h="+jQuery('#h').val();
				//window.opener.document.retaileruploadlogoinfoform.method = "GET";
				document.uploadImageform.submit();

			}
		}
	</script>
</body>
</html>