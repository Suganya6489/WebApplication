<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
<meta content="text/html;charset=utf-8" http-equiv="Content-Type">
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<script type="text/javascript">
	tblHt =0;
	$(document).ready(function() {
		var hiddenWeekDay = document.screenSettingsForm.hiddenWeekDay.value;
		if(null != hiddenWeekDay && "" != hiddenWeekDay)
		{
			$('input[name="everyWeekDayMonth"]').prop('checked', false);
				var arr = hiddenWeekDay.split(',');		
				jQuery.each(arr, function(i, val) {
					val = val.replace(/\s+/g, '');
					$('#everyWeekDayMonth'+val).prop('checked', 'checked');
				});
			document.screenSettingsForm.hiddenWeekDay.value = '';
			updateCstmSlct();
		}	

		$("#cstmDrpdwnInput").on('click',function(){
			$(this).next('.cstmDropdwn').toggle();
		});
		/* custom drop down control with checkbox & display selected value in input field */
		$(".cstmDropdwn").on('click.cstmTrgr','li',function(){
			var $prnt = $(this);
			var $chkbx = $prnt.toggleClass("mul-slct").find(".recDays");
			if($prnt.hasClass('mul-slct')) {
		      $chkbx.prop('checked', true);
			  updateCstmSlct();
		    } else {
		      $chkbx.prop('checked', false);
			  updateCstmSlct();
		    }
		});
		/*close custom drop down control when clicked outside of the control allowing multi click inside*/
		$(document).click(function(e) {
			var target = $(e.target);  
			var curContx = target.parents().hasClass("cstmSlct")  
			if (!curContx) {
			$(".cstmDropdwn").hide();
			}
		});
	
		var hiddenCategory = document.screenSettingsForm.hiddenCategory.value;
		if(null != hiddenCategory && "" != hiddenCategory)
		{
			$('#eventCategory').val(hiddenCategory).change();
		}

		var hiddenDays = document.screenSettingsForm.hiddenDays.value;
		if(null != hiddenDays && "" != hiddenDays)
		{
			$('input[name="days"]').prop('checked', false);
				var arr = hiddenDays.split(',');
		
				jQuery.each(arr, function(i, val) {
					val = val.replace(/\s+/g, '');
					$('#days'+val).prop('checked', 'checked');
			});
			document.screenSettingsForm.hiddenDays.value = '';
		}		

		$(window).load(function(){
			
			/*Range of recurrence: trgiger click on radio button when input:text is clicked*/
			$(".range").on('keydown.trgrRng click',function(){
				var curName = $(this).parent('td').find('input[type="radio"]').attr("name");
				var curInput = $(this).parent('td').find('input[name="'+curName+'"]').attr("id"); 
				$('input[name="'+curName+'"]').removeAttr("checked");
				var curIn = $("#"+curInput);
				$(curIn).click();
			});
			 
			$(".ui-datepicker-trigger").on('click',function(){
				$(this).trigger('keydown.trgrRng');
			});
		});

		$("input[name='recurrencePatternID']").on('click',function(){
			var patternName = $(this).attr("patternName");
			var hiddenDays = document.screenSettingsForm.hiddenDays.value; 

			if(patternName == "Daily")
			{				
				$('input:radio[name=isOngoingDaily]')[0].checked = true;
				document.screenSettingsForm.everyWeekDay.value = 1;

				if (document.getElementById('everyWeekDay.errors') != null) {
					document.getElementById('everyWeekDay.errors').style.display = 'none';
				}
			}
			else if(patternName == "Weekly")
			{
				document.screenSettingsForm.everyWeek.value = 1;
				var hiddenDays = document.screenSettingsForm.hiddenDay.value;
				if(null != hiddenDays && "" != hiddenDays)
				{
					$('input[name="days"]').prop('checked', false);
						var arr = hiddenDays.split(',');
				
						jQuery.each(arr, function(i, val) {
							val = val.replace(/\s+/g, '');
							$('#days'+val).prop('checked', 'checked');
					});
				}

				if (document.getElementById('days.errors') != null) {
					document.getElementById('days.errors').style.display = 'none';
				}
				if (document.getElementById('everyWeek.errors') != null) {
					document.getElementById('everyWeek.errors').style.display = 'none';
				}
				
			}
			else if(patternName == "Monthly")
			{
				$('input:radio[name=isOngoingMonthly]')[0].checked = true;
				document.screenSettingsForm.everyMonth.value = 1;
				document.screenSettingsForm.everyDayMonth.value = 1;
				document.screenSettingsForm.dateOfMonth.value = document.screenSettingsForm.hiddenDate.value;
				document.screenSettingsForm.dayNumber.value = document.screenSettingsForm.hiddenWeek.value;
				document.screenSettingsForm.everyWeekDayMonth.value = document.screenSettingsForm.hiddenWeekDay.value;

				if (document.getElementById('dateOfMonth.errors') != null) {
					document.getElementById('dateOfMonth.errors').style.display = 'none';
				}
				if (document.getElementById('everyMonth.errors') != null) {
					document.getElementById('everyMonth.errors').style.display = 'none';
				}
				if (document.getElementById('everyDayMonth.errors') != null) {
					document.getElementById('everyDayMonth.errors').style.display = 'none';
				}
			}
			dsplyRecur(this);
		});

		/*Range of recurrence: restrict input to 3 chars & accept only digits*/
		$('.numeric').on('input', function (event) { 
			this.value = this.value.replace(/[^0-9]/g, '');
		});
		
		/* ongoing event option change display related form elemnts */
		$("input[name='isOngoing']").on(
				'change.trgrOngng',
				function() {
					var evntOptn = $(this).attr('value');
					if (evntOptn == "yes") {
						$("tr.ongoing").show();
						$(".ongoing").show();
						$(".scngRow,.not-ongng").hide();									
						dsplyRecur("input[name='recurrencePatternID']:checked");
						//$("input[name='recurrencePatternID']:checked").trigger('click');
					} else if (evntOptn === "no") {
						$(".ongoing,.scngRow").hide();
						$(".not-ongng").show();
						$("#menu-pnl").height(
								$(".content").height()).trigger(
								"resize");
					}
				});
		$('input[name="isOngoing"]:checked').trigger('change');		
						
	$(".hdrClone span:gt(0)").css("text-indent","8px");
	
	if ($("#evntYes").prop('checked') || $("#evntLogisticYes").prop('checked')) {
		$("#saveEventBtn").hide();
		$("#nextBtn").show();
		$(".pill-tabs a").not(".active").css("display", "inline-block");
	} else {
		$("#saveEventBtn").show();
		$("#nextBtn").hide();
		$(".pill-tabs a").not(".active").css("display", "none");
	}

	var geoError = $("#geoError").val();
	if (geoError == 'true') {
		$("#dispLatLang").show();
	
	} else {
	
		$("#dispLatLang").hide();
	}

	var showEventPacgTab = $("#showEventPacgTab").val();
	var showLogisticsTab = $("#showLogisticsTab").val();
	if(showLogisticsTab == 'true')	{
		tglActive($(".pill-tabs a[ref='eventLogistics']"));
	} else if (showEventPacgTab == 'true') {
		tglActive($(".pill-tabs a[ref='eventPackage']"));
	} else	{
		tglActive($(".pill-tabs a[ref='eventDetails']"));
	}

	var hcEventID=$("#hcEventID").val();
	
	if(hcEventID != "")
	{
		document.title = "Update Event";		
		$("#eventTitle").text("Update Event");
					
					$("#saveEventBtn").val("Update");
					$("#pckDetBtn").val("Update");
					$("#logisticsSaveBtn").val("Update");
	}

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
	$("#eventStartDate").datepicker({
		showOn : 'both',
		buttonImageOnly : true,
		buttonText : 'Click to show the calendar',
		buttonImage : 'images/icon-calendar-active.png'
	});
	$("#eventEndDate").datepicker({
		showOn : 'both',
		buttonImageOnly : true,
		buttonText : 'Click to show the calendar',
		buttonImage : 'images/icon-calendar-active.png'
	}).next(".ui-datepicker-trigger").addClass("range");

	// Events Script: Start
	
	tglActive($(".pill-tabs a.active"));
	$(".pill-tabs a").on('click.pilltab', function() {
		$this = $(this);
		tglActive($this);
	});

	//$(".pill-tabs a").not(".active").css("display", "none");
	$("input[name='bsnsLoc']").change(function() {
		var reqOptn = $(this).attr('value');
		var getId = $(this).attr('id');

		if (reqOptn === "yes") {
			
			
			$(".evntLoctn").hide();
			
				var appSiteLstError='${requestScope.appSiteLstError}';
			
			if(appSiteLstError!=''){
				$(".bsnsLoctn").show();
				$("div.bsnsLoctn").parents('tr').hide();
			}else{
				
				$(".bsnsLoctn").show();
			}			

			if($("#mngevntTbl").length){
				if ($("#mngevntTbl tr").length < 9){
					 $("#mngevntTbl").parent('div.scrollTbl').removeClass("tblHt");
				}else{
					$("#mngevntTbl").parent('div.scrollTbl').addClass("tblHt");
				}
			}
			
			if ($("#mngevntTbl").length) {
				tblHt = $("#mngevntTbl").height();
			}

			$("#menu-pnl").height($(".content").height()).trigger("resize");
		} else if (reqOptn === "no") {
			$(".bsnsLoctn").hide();
			$(".evntLoctn").slideDown();
			var actHt = parseInt($(
					"#menu-pnl").height());
			var adjust = parseInt(actHt)
					- parseInt(tblHt + 80);
			$("#menu-pnl").height(adjust);
			
		}
	});

	$("input[name='evntHotel']").change(function() {
		var reqOptn = $(this).attr('value');
		var getId = $(this).attr('id');

		if (reqOptn === "yes") {
			$(".eventHotel").show();
			if ($("#eventHotel").length) {
				tblHtt = $("#eventHotel")
						.height();
			}			

			if($("#eventHotel").length){
				if ($("#eventHotel tr").length < 9){
					 $("#eventHotel").parent('div.scrollTbl').removeClass("tblHt");
				}else{
					
					$("#eventHotel").parent('div.scrollTbl').addClass("tblHt");
				}
			}
			 
		/* 	$('body,html')
					.animate(
							{
								scrollTop : $(
										".content")
										.prop(
												"scrollHeight")
							}, 1400); */
			$("#menu-pnl").height(
					$(".content").height())
					.trigger("resize");
		} else if (reqOptn === "no") {
			$(".eventHotel").hide();
			$("#menu-pnl").height(
					$(".cont-block")
							.height() + 30);
			$('body,html').animate({scrollTop : 0}, 'fast');
		}
	})
	$("input[name='evntPckg']").change(function() {
		var reqOptn = $(this).attr('value');
		if (reqOptn === "yes") {
			tglActive($(".pill-tabs a[ref='eventPackage']"));
			$("#saveEventBtn").hide();
			$("#nextBtn").show();
		}
		if (reqOptn === "no") {
			$("#evntNo").prop('checked',true);	
			//$(".pill-tabs a").not(".active").css("display","none");
			
			$(".pill-tabs a[ref='eventPackage']").hide();	
	
			if(!$("#evntLogisticYes").prop('checked'))	{
				$("#saveEventBtn").show();
				$("#nextBtn").hide();
			}
			//$("#saveEventBtn").show();
			//$("#nextBtn").hide();
			
			if(hcEventID != "")	{
				$("#saveEventBtn").val("Update");
			} else{
				$("#saveEventBtn").val("Save");
			}
		}
		toggleTab();
	});

	$("input[name='bsnsLoc']:checked").trigger('change');
	$("input[name='evntHotel']:checked").trigger('change');
	
	// Events Script: End	
	
	/* $("#nextBtn").click(function() {
		tglActive($(".pill-tabs a[ref='eventPackage']"));
	}); */
	
	$("#nextBtn").click(function() {
		if($("#evntYes").prop('checked'))	{
			tglActive($(".pill-tabs a[ref='eventPackage']"));
		} else	if($("#evntLogisticYes").prop('checked')) {
			tglActive($(".pill-tabs a[ref='eventLogistics']"));
		} else	{
			$("#nextBtn").hide();
		}
	});
	
	$("#backBtn").click(function() {
		tglActive($(".pill-tabs a[ref='eventDetails']"));
	});
	
	$("#logisticsBackBtn").click(function()	{
		if($("input[name='evntPckg']:checked").attr("value") === "yes") {
			tglActive($(".pill-tabs a[ref='eventPackage']"));
		} else	{
			tglActive($(".pill-tabs a[ref='eventDetails']"));
		}
	});
	
	$("#eventPckNextBtn").click(function()	{
		tglActive($(".pill-tabs a[ref='eventLogistics']"));
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
	
	/* For Event Logistics Start */
	
	var evntLogisticsFlag = document.screenSettingsForm.isEventLogistics.value;
	if(evntLogisticsFlag === "yes")	{
		$(".pill-tabs a[ref='eventLogistics']").show();
	}
	
	$("input[name='isEventLogistics']").change(function() {
		var reqOptn = $(this).attr('value');
		if (reqOptn === "yes") {
			tglActive($(".pill-tabs a[ref='eventLogistics']"));
			$("#saveEventBtn").hide();
			$("#nextBtn").show();
			$("#pckDetBtn").hide();
		} else if (reqOptn === "no") {
			$("#evntLogisticNo").prop('checked',true);
			//$(".pill-tabs a").not(".active").css("display",	"none");
			$(".pill-tabs a[ref='eventLogistics']").hide();
			if(!$("#evntYes").prop('checked'))	{
				$("#saveEventBtn").show();
				$("#nextBtn").hide();
			}

			if(hcEventID != "")	{
				$("#saveEventBtn").val("Update");
			} else{
				$("#saveEventBtn").val("Save");
			}
		}
		toggleTab();
	});
	
	$('#eventLogisticsBtn tr td').each(function() {
		var val = $.trim($(this).text());
		if (/\s/.test(val)) {
			$(this).css("word-break", "keeep-all");
		} else {
			$(this).css("word-break", "break-all");
		}
	});
	
	$('#trgrUpldLogisticsImg').bind('change', function() {
		
		var imageType = document.getElementById("trgrUpldLogisticsImg").value;
		var uploadImageType = $(this).attr('name');
		document.screenSettingsForm.imgUploadFor.value = "logistics";
		//document.screenSettingsForm.showLogisticsTab.value = true;

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
	
	$('#trgrUpld').bind('change', function() {
		document.screenSettingsForm.imgUploadFor.value = null;
	});
	
	setPlaceholderText('addLogisticBtnName');
	setPlaceholderText('addLogisticBtnLink');
	
	/* Event Logistics End */
	
});


	function updateCstmSlct() {
	var allVals = [];
	var $chkVal = $('.recDays:checked');
	$chkVal.each(function () {
		allVals.push($(this).attr("display"));
		});

	if($chkVal.length){
		$('#cstmDrpdwnInput').val(allVals);
	}
	else{
		$('#cstmDrpdwnInput').val("Select Day(s)");
	}

	}		

	function dsplyRecur(obj){
		document.screenSettingsForm.recurrencePatternName.value = $(obj).attr("patternName");;
		var recurId = $(obj).attr("id");
		var ongTrgr = $("#onGoing").prop("checked");
		 $(".recur").hide();
		$(".recurrence").show();
		$("."+recurId).css("display","block");
	}
	
/* 	function tglActive(obj) {
		var cur = obj;
		var curEle = $(obj).attr("ref");
		var prntEle = $(obj).parents().attr("class");
		if (curEle === "eventPackage") {
			//$(".pill-tabs a").not(".active").on('click.pilltab');
			$(".pill-tabs a").not(".active").css("display", "inline-block");
		}
		$("." + prntEle).find('a').removeClass("active");
		$(obj).addClass("active");
		$(".pill-cont").hide();
		$("." + curEle).show();
		$("#menu-pnl").height($(".cont-block").height()).trigger("resize");
		$('body,html').animate({scrollTop : 0}, 'slow');
	} */
	function tglActive(obj) {
		var cur = obj;
		var curEle = $(obj).attr("ref");
		var prntEle = $(obj).parents().attr("class");
		var isPackage = $("input[name='evntPckg']:checked").attr("value");
		var isLogistics = $("input[name='isEventLogistics']:checked").attr("value");
		
		if(isLogistics === "yes"){
			$(".pill-tabs a[ref='eventLogistics']").show();
			$("#pckDetBtn").hide();
		} 
		
		if (curEle === "eventPackage" && isPackage === "yes") {
			$(".pill-tabs a[ref='eventPackage']").show();
		}
		
		if(curEle==="eventLogistics" && isLogistics === "yes"){
			$(".pill-tabs a[ref='eventLogistics']").show();
		}
		
		if(isLogistics === "yes" && isPackage === "yes"){
			$(".pill-tabs a").show();
		}
		
		$("." + prntEle).find('a').removeClass("active");
		$(cur).addClass("active");
		$(".pill-cont").hide();
		$("." + curEle).show();
		$("#menu-pnl").height($(".cont-block").height()).trigger("resize");
		$('body,html').animate({scrollTop : 0}, 'fast');
		$('body,html').animate({scrollTop : 0}, 'fast');
		toggleTab();
	} 

	function searchAppSiteHotel(searchType) {		
		document.screenSettingsForm.searchType.value=searchType;
		document.screenSettingsForm.action = "addEvent.htm";
		document.screenSettingsForm.method = "POST";
		document.screenSettingsForm.submit();
	}

	function isLatLong(evt) {
		var charCode = (evt.which) ? evt.which : event.keyCode
		if ((charCode > 47 && charCode < 58) || charCode == 46 || charCode < 31
				|| charCode == 45 || charCode == 43)
			return true;
		return false;
	}
	
	
	function clearForm(){
		var r = confirm("Do you really want to clear the form");
		if(r == true)
		{			
			if (document.getElementById('hcEventName.errors') != null) {
				document.getElementById('hcEventName.errors').style.display = 'none';
			}			
			if (document.getElementById('eventCategory.errors') != null) {
				document.getElementById('eventCategory.errors').style.display = 'none';
			}
			if (document.getElementById('eventDate.errors') != null) {
				document.getElementById('eventDate.errors').style.display = 'none';
			}
			if (document.getElementById('address.errors') != null) {
				document.getElementById('address.errors').style.display = 'none';
			}
			if (document.getElementById('city.errors') != null) {
				document.getElementById('city.errors').style.display = 'none';
			}
			if (document.getElementById('postalCode.errors') != null) {
				document.getElementById('postalCode.errors').style.display = 'none';
			}			
			if (document.getElementById('state.errors') != null) {
				document.getElementById('state.errors').style.display = 'none';
			}
			if (document.getElementById('eventImageName.errors') != null) {
				document.getElementById('eventImageName.errors').style.display = 'none';
			}
			if (document.getElementById('shortDescription.errors') != null) {
				document.getElementById('shortDescription.errors').style.display = 'none';
			}
			if (document.getElementById('latitude.errors') != null) {
				document.getElementById('latitude.errors').style.display = 'none';
			}			
			if (document.getElementById('logitude.errors') != null) {
				document.getElementById('logitude.errors').style.display = 'none';
			}
			if (document.getElementById('moreInfoURL.errors') != null) {
				document.getElementById('moreInfoURL.errors').style.display = 'none';
			}
			document.screenSettingsForm.hcEventName.value = "";
			document.screenSettingsForm.eventCategory.value = "";
			document.screenSettingsForm.eventDate.value = "";
			document.screenSettingsForm.eventTimeHrs.value = "00";
			document.screenSettingsForm.eventTimeMins.value = "";
			document.screenSettingsForm.address.value = "";
			document.screenSettingsForm.city.value = "";
			document.screenSettingsForm.postalCode.value = "";
			document.screenSettingsForm.latitude.value = "";
			document.screenSettingsForm.logitude.value = "";
			document.screenSettingsForm.geoError.value =false;
			document.screenSettingsForm.showEventPacgTab.value =false;
			document.screenSettingsForm.hiddenCategory.value ="";
			document.screenSettingsForm.state.value = "";
			document.screenSettingsForm.eventImageName.value = "";
			document.screenSettingsForm.longDescription.value = "";
			document.screenSettingsForm.shortDescription.value = "";
			document.screenSettingsForm.appSiteSearchKey.value = "";
			document.screenSettingsForm.moreInfoURL.value = "";
			
			$("#dispLatLang").hide();
			$("#bsnsNo").prop('checked',true);
			$("#bsnsYes").prop('checked',false);
			$("#evntYes").prop('checked',false);
			$("#evntNo").prop('checked',true);
			$("#evntLogisticYes").prop('checked',false);
			$("#evntLogisticNo").prop('checked',true);
			$("#eventImageName").prop('src','images/uploadIconSqr.png');
			$(".bsnsLoctn").hide();
			
			$("input[name='appsiteID']").prop("checked",false);
			$('body,html').animate({scrollTop : 0}, 'fast');			
		}
		
		var height = $(".cont-pnl").height();
		$("#menu-pnl").height(height + 28);
	}
	
	function displayManageEvents()
	{		
		var r = confirm("Are you sure you want to leave this page without saving the changes!");
		if(r == true)
		{
			document.screenSettingsForm.action = "manageevents.htm";
			document.screenSettingsForm.method = "POST";
			document.screenSettingsForm.submit();
		}
	}
	
	function clearPackageForm(){		
		var r = confirm("Do you really want to clear the form");
		if(r == true)
		{
			document.screenSettingsForm.packagePrice.value = "";
			document.screenSettingsForm.packageTicketURL.value = "";
			document.screenSettingsForm.packageDescription.value = "";
			document.screenSettingsForm.hotelSearchKey.value = "";			
			$("#pckgYes").prop('checked',false);
			$("#pckgNo").prop('checked',true);			
			$("input[name='price']").val("");
			$("input[name='hotelRating']").val("");
			$("input[name='roomAvailtyCheckURL']").val("");
			$("input[name='hotelDiscountAmount']").val("");
			$("input[name='discountCode']").val("");
			$("input[name='roomBkngURL']").val("");
			$("input[name='retailLocationID']").prop("checked",false);
			$(".eventHotel").hide();
			$('body,html').animate({scrollTop : 0}, 'fast');
		}	
	}
	
	function addEventAppSite(obj){		
		$('body,html').animate({scrollTop : 0}, 'fast');
		addAppsiteEvent(obj);		
	}	

	function eventChange()
	{
		document.screenSettingsForm.hiddenCategory.value = $('#eventCategory').val();
	}

	function isThirtyFirst(obj)
	{
		if(obj.value == 31)
		{
			r = confirm("Some months have fewer than 31days. For these months, the occurrence will fall on the last day of the month.");
			if(r == false)
			{
				obj.value = 1;	
			}
		}	
	}
	
	function updateEventLogisticsButtonList(type, btnId)	{
		if(btnId === '')	{
			btnId = 0;
		}
		var buttonId = 0;
		var buttonName = null;
		var buttonLink = null;
		if(type === "add" || type == "update")	{
			buttonName = $("#addLogisticBtnName").val();
			buttonLink = $("#addLogisticBtnLink").val();
			
			var trimmedBtnName = $.trim(buttonName);
			var trimmedBtnLink = $.trim(buttonLink);
			
			if(trimmedBtnName === '' || trimmedBtnName === 'Enter Button Name')	{
				alert("Please enter valid button name");
				return false;
			}
			
			if(trimmedBtnLink === '' || trimmedBtnLink === 'Enter Button Link')	{
				alert("Please enter valid URL for button link");
				return false;
			}
			
			buttonName = trimmedBtnName;
			buttonLink = trimmedBtnLink;
			
			buttonId = document.screenSettingsForm.buttonId.value;
			if(buttonId != null && buttonId !='' && buttonId != 0 && buttonId !== 'null')	{
				type = 'update';
				//document.screenSettingsForm.buttonId.value = null;
			}
		} else if(type === "delete")	{
			var flag = confirm("Do you want to delete this button?");
			buttonId = btnId;
			if(flag == false)	{
				return false;
			}
		} else	{
			return false;
		}
		
		/* For IE Fix */
		if(buttonId === 'null')	{
			buttonId = null;
		}
		
		$.ajaxSetup({
			cache : false
		});
		
		$.ajax({
			type : "GET",
			url : "updatelogisticsbtnlist.htm",
			data : {
				'buttonId' : buttonId,
				'buttonName' : buttonName,
				'buttonLink' : buttonLink,
				'type' : type,
			},

			success : function(response) {
				var ajaxResponse = response;
				var arrResponse = response.split(":");
				if(arrResponse[0] === "Success")	{
					if(arrResponse[1] === "add")	{
						$("#eventLogisticsBtn").append('<tr id="logisticsBtnListId-' + arrResponse[2] + '"><td width="40%">' 
								+ buttonName + '</td><td width="40%">' + buttonLink + '</td><td>' 
								+ '<a href="#" title="edit"><img width="20" height="20" src="images/edit_icon.png" alt="edit" class="actn-icon" onclick="editEventLogisticsButton(\'logisticsBtnListId-'+ arrResponse[2] + '\')"></a>' 
								+ '<a href="#" title="delete"><img width="20" height="20" src="images/delete_icon.png" alt="delete" class="actn-icon" onclick="updateEventLogisticsButtonList(\'delete\','+ arrResponse[2] +')"></a>'
								+ '</td></tr>');
							
						if(buttonName.length > 35 || buttonLink.length > 40)	{
							$("#eventLogisticsBtn tr td").css("word-break", "break-all");
						}	
						
						document.getElementById('addLogisticBtnName').value = null;
						document.getElementById('addLogisticBtnLink').value = null;
						document.screenSettingsForm.buttonId.value = null;
						$("#addLogisticBtnName").val('');
						$("#addLogisticBtnLink").val('');
					} else if(arrResponse[1] ===  "update")	{
						$("#logisticsBtnListId-" + arrResponse[2] + " td:first-child").html(buttonName);
						$("#logisticsBtnListId-" + arrResponse[2] + " td:nth-child(2)").html(buttonLink);
						document.getElementById('addLogisticBtnName').value = null;
						document.getElementById('addLogisticBtnLink').value = null;
						document.screenSettingsForm.buttonId.value = null;
						$("#addLogisticBtnName").val('');
						$("#addLogisticBtnLink").val('');
					} else if(arrResponse[1] ===  "delete")	{
						$("#logisticsBtnListId-" + arrResponse[2]).remove();
					}
				}  else	{
					alert(ajaxResponse)
				}
			},
			error : function(e) {
				alert('Error occured while updating buttons');
			}
		});

	}
	
	function editEventLogisticsButton(btnId)	{
		var btnName = $("#" + btnId + " td:first-child").text();
		var btnLink = $("#" + btnId + " td:nth-child(2)").text();
		var strArr = btnId.split('-');
		document.getElementById('addLogisticBtnName').value = btnName;
		document.getElementById('addLogisticBtnLink').value = btnLink;
		document.screenSettingsForm.buttonId.value = strArr[1];
		//$("#addLogisticsBtn").attr("title", "Update Button");
	}
	
	function toggleTab() {								
		if($("input[name='isEventLogistics']:checked").attr("value") === "no") {
			$(".pill-tabs a[ref='eventLogistics']").hide();
			$("#pckDetBtn").show();
			$("#eventPckNextBtn").hide();
		} else	{
			$("#pckDetBtn").hide();
			$("#eventPckNextBtn").show();
		}
		
		if($("input[name='evntPckg']:checked").attr("value") === "no") {
			$(".pill-tabs a[ref='eventPackage']").hide();
		} 	
	}
	
	function saveLogisticsEvent()	{
		document.screenSettingsForm.buttonId.value = 0;
		if($("input[name='evntPckg']:checked").attr("value") === "yes")	{
			validateHotelDetails();
		} else	{
			saveEvent();
		}
	}
	
	function clearLogisticsForm()	{
		var r = confirm("Do you really want to clear the form");
		if(r == true)	{
			if (document.getElementById('logisticsImgName.errors') != null) {
				document.getElementById('logisticsImgName.errors').style.display = 'none';
			}
			if (document.getElementById('btnNames.errors') != null) {
				document.getElementById('btnNames.errors').style.display = 'none';
			}
			document.screenSettingsForm.logisticsImgName.value = "";
			document.screenSettingsForm.buttonName.value = "";
			document.screenSettingsForm.buttonLink.value = "";
			document.screenSettingsForm.isEventOverlay.value = "no";
			$("#logisticsImgName").prop('src','images/uploadIconSqr.png');
			$("#isEventOverlayYes").prop('checked',false);
			$("#isEventOverlayNo").prop('checked',true)
		}
	}

</script>

<div id="wrpr" class="eventsPg">
	<span class="clear"></span>
	<div class="wrpr-cont relative">
		<div id="slideBtn">
			<a href="#" onclick="revealPanel(this);" title="Hide Menu"><img
				src="images/slide_off.png" width="11" height="28" alt="btn_off" />
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
				<li><a href="manageevents.htm">Setup Events</a></li>
				<li class="last" id="eventTitle">Add Event</li>
			</ul>
		</div>
		<span class="blue-brdr"></span>
		<div class="content" id="login">
			<div id="menu-pnl" class="split">
				<jsp:include page="leftNavigation.jsp"></jsp:include>
			</div>
			<div class="cont-pnl split" id="">
				<div class="cont-block rt-brdr stretch">
					<div class="title-bar">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-events">&nbsp;</span></li>
							<li>Events</li>
						</ul>
					</div>
					<span class="clear"></span>

					<form:form name="screenSettingsForm" id="screenSettingsForm"
						commandName="screenSettingsForm" enctype="multipart/form-data"
						action="uploadevntimg.htm">
						<form:hidden path="viewName" value="addupdateevent" />
						<form:hidden path="hiddenState" />
						<form:hidden path="hiddenCategory" />
						<form:hidden path="hiddenDays" />
						<form:hidden path="hiddenDay" />
						<form:hidden path="hiddenDate" />
						<form:hidden path="hiddenWeek" />
						<form:hidden path="hiddenWeekDay" />
						<form:hidden path="lowerLimit" />
						<form:hidden path="hotelListJson" />
						<form:hidden path="geoError" id="geoError" />
						<form:hidden path="showEventPacgTab" id="showEventPacgTab" />
						<form:hidden path="hcEventID" id="hcEventID" />
						<form:hidden path="searchType" id="searchType" />
						<form:hidden path="recurrencePatternName"
							id="recurrencePatternName" />
						<form:hidden path="buttonId"/>
						<form:hidden path="imgUploadFor"/>
						<form:hidden path="showLogisticsTab" id="showLogisticsTab"/>
						<%-- <form:hidden path="isSignatureEvent" /> --%>
						<input type="hidden" name="pageNumber" />
						<input type="hidden" name="pageFlag" />
						<form:hidden path="isNewLogisticsImg" />
						<div class="pill-tabs">
							<a href="#" class="active" ref="eventDetails">Event Details</a>
							<a href="#" class="" ref="eventPackage">Event Package</a>
							<a href="#" class="" ref="eventLogistics">Event Logistics</a> 
						</div>


						<div class="cont-wrp">
							<div class="eventDetails pill-cont">
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="brdrlsTbl">
									<tr>
										<td width="20%"><label class="mand">Title</label></td>
										<td width="30%"><form:errors cssClass="errorDsply"
												path="hcEventName"></form:errors>
											<div class="cntrl-grp">
												<form:input path="hcEventName" type="text" class="inputTxt"
													tabindex="1" maxlength="40" />
											</div></td>


										<td width="20%"><label class="mand">Image</label></td>
										<td width=30%><form:errors cssClass="errorDsply"
												path="eventImageName"></form:errors> <label class="imgLbl">
												<img id="eventImageName" width="100" height="50"
												alt="upload" src="${sessionScope.eventImagePreview }">
										</label> <span class="topPadding cmnRow"> <label for="trgrUpld">
													<input type="button" value="Upload" id="trgrUpldBtn"
													class="btn trgrUpld" title="Upload Image File" tabindex="1">
													<form:hidden path="eventImageName" id="eventImageName" />
													<span class="instTxt nonBlk"></span> <form:input
														type="file" class="textboxBig" id="trgrUpld"
														path="eventImageFile" tabindex="2" />
											</label>
										</span><label id="maxSizeImageError" class="errorDsply"></label></td>


									</tr>
									<tr>
										<td align="left" valign="top"><label class="mand">Short
												Description</label></td>
										<td><form:errors cssClass="errorDsply"
												path="shortDescription"></form:errors>
											<div class="cntrl-grp">
												<form:textarea path="shortDescription" name="textarea"
													cols="25" rows="5" class="textareaTxt" tabindex="3"
													maxlength="255"></form:textarea>
											</div></td>
										<td valign="top">Long Description</td>
										<td><div class="cntrl-grp">
												<form:textarea path="longDescription" class="textareaTxt"
													cols="25" rows="5" tabindex="4" maxlength="2000"></form:textarea>
											</div></td>

									</tr>
									<tr>
										<td align="left" valign="top"><label class="mand">Category</label></td>
										<td><form:errors cssClass="errorDsply"
												path="eventCategory"></form:errors> <form:select
												path="eventCategory" name="select2" id="eventCategory"
												class="slctBx textareaTxt" tabindex="5"
												onchange="eventChange();">
												<option value="">---Select---</option>
												<c:forEach items="${sessionScope.eventCatLst}" var="item">
													<option value="${item.catId }">${item.catName }</option>
												</c:forEach>
											</form:select></td>
										<td align="left" valign="top">More Information URL</td>
										<td><form:errors cssClass="errorDsplyHtl"
												path="moreInfoURL"></form:errors>
											<div class="cntrl-grp">
												<form:input path="moreInfoURL" type="text" class="inputTxt"
													tabindex="26" />
											</div></td>
									</tr>
									<tr class="subHdr">
										<td colspan="4" align="left" valign="bottom"><span
											class="setLbl">Is Event Ongoing? </span> <span
											class="spacing"> <form:radiobutton path="isOngoing"
													value="yes" /> <label for="ongnYes"> Yes</label> <form:radiobutton
													path="isOngoing" value="no" /> <label for="ongnNo">No</label>
										</span></td>
									</tr>
									<tr class="scngRow">
										<td colspan="4"></td>
									</tr>
									<tr class="not-ongng">
										<td><label class="mand">Event Start Date</label></td>
										<td><form:errors cssClass="errorDsply" path="eventDate"></form:errors>
											<div class="cntrl-grp cntrl-dt floatL">
												<form:input path="eventDate" id="eventDate" type="text"
													class="inputTxt" tabindex="6" />
											</div></td>
										<td>Event Start Time</td>
										<td><form:select path="eventTimeHrs" class="slctSmall"
												name="etHr" tabindex="7">
												<form:options items="${StartHours}" />
											</form:select> Hrs <form:select path="eventTimeMins" class="slctSmall"
												name="stMin" tabindex="8">
												<form:options items="${StartMinutes}" />
											</form:select> Mins</td>
									</tr>
									<tr class="not-ongng">
										<td><label>Event End Date</label></td>
										<td><form:errors cssClass="errorDsply" path="eventEDate"></form:errors>
											<div class="cntrl-grp cntrl-dt floatL">
												<form:input path="eventEDate" id="eventEDate" type="text"
													class="inputTxt" tabindex="9" />
											</div></td>
										<td>Event End Time</td>
										<td><form:select path="eventETimeHrs" class="slctSmall"
												name="etHr" tabindex="10">
												<form:options items="${StartHours}" />
											</form:select> Hrs <form:select path="eventETimeMins" class="slctSmall"
												name="stMin" tabindex="11">
												<form:options items="${StartMinutes}" />
											</form:select> Mins</td>
									</tr>
									<tr class="ongoing  grey-bg">
										<td><label>Start Time</label></td>
										<td><form:select path="eventStartTimeHrs"
												class="slctSmall" name="etHr" tabindex="7">
												<form:options items="${StartHours}" />
											</form:select> Hrs <form:select path="eventStartTimeMins" class="slctSmall"
												name="stMin" tabindex="8">
												<form:options items="${StartMinutes}" />
											</form:select> Mins</td>
										<td><label>End Time</label></td>
										<td><form:select path="eventEndTimeHrs" class="slctSmall"
												name="etHr" tabindex="7">
												<form:options items="${StartHours}" />
											</form:select> Hrs <form:select path="eventEndTimeMins" class="slctSmall"
												name="stMin" tabindex="8">
												<form:options items="${StartMinutes}" />
											</form:select> Mins</td>
									</tr>
									<tr class="ongoing subHdr">
										<td colspan="4">Recurrence Pattern <span
											class="mrgn-left spacing"> <c:forEach
													items="${sessionScope.eventPatterns}" var="patterns">
													<form:radiobutton path="recurrencePatternID"
														value="${patterns.recurrencePatternID}"
														id="actn-${patterns.recurrencePatternName}"
														patternName="${patterns.recurrencePatternName}" />
													<label for="actn-${patterns.recurrencePatternName}">${patterns.recurrencePatternName}</label>
												</c:forEach>
										</span></td>
									</tr>
									<tr class="grey-bg equalPdng recurrence ongoing">
										<td colspan="4"><div class="recurenceCont">
												<div class="brdr actn-Daily recur">
													<table width="100%" cellspacing="0" cellpadding="0"
														class="white-bg formTbl">
														<thead>
															<tr class="tblHdr">
																<th colspan="2">Daily</th>
															</tr>
														</thead>
														<tbody>
															<tr class="">
																<td colspan="2"><form:errors path="everyWeekDay"
																		cssClass="errorDsply"></form:errors></td>
															</tr>
															<tr class="">
																<td width="23%" align="left"><form:radiobutton
																		path="isOngoingDaily" value="days" /> Every <form:input
																		path="everyWeekDay"
																		cssClass="inputText small numeric range" maxlength="3" />
																	day(s)</td>
																<td width="77%" align="left"><form:radiobutton
																		path="isOngoingDaily" value="weekDays" /> Every
																	Weekday</td>
															</tr>
														</tbody>
													</table>
												</div>
												<div class="actn-Weekly recur brdr">
													<table width="100%" cellspacing="0" cellpadding="0"
														class="white-bg formTbl">
														<thead>
															<tr class="tblHdr">
																<th>Weekly</th>
															</tr>
														</thead>
														<tbody>
															<tr class="">
																<td colspan="1"><form:errors path="everyWeek"
																		cssClass="errorDsply">
																	</form:errors> <form:errors path="days" cssClass="errorDsply">
																	</form:errors></td>
															</tr>
															<tr class="">
																<td align="left">Recur every <form:input
																		path="everyWeek" cssClass="inputText small numeric"
																		maxlength="2" />week(s) on:
																</td>
															</tr>
															<tr class="">
																<td align="left">
																	<ul class="week-actn">
																		<li><form:checkbox path="days" value="1"
																				id="days1" /> <label for="days1">Sunday</label></li>
																		<li><form:checkbox path="days" value="2"
																				id="days2" /> <label for="days2">Monday</label></li>
																		<li><form:checkbox path="days" value="3"
																				id="days3" /> <label for="days3">Tuesday</label></li>
																		<li><form:checkbox path="days" value="4"
																				id="days4" /> <label for="days4">Wednesday</label>
																		</li>
																		<li><form:checkbox path="days" value="5"
																				id="days5" /> <label for="days5">Thursday</label></li>
																		<li><form:checkbox path="days" value="6"
																				id="days6" /> <label for="days6">Friday</label></li>
																		<li><form:checkbox path="days" value="7"
																				id="days7" /> <label for="days7">Saturday</label></li>
																	</ul>
																</td>
															</tr>
														</tbody>
													</table>
												</div>
												<div class="actn-Monthly recur brdr">
													<table width="100%" cellspacing="0" cellpadding="0"
														class="white-bg formTbl">
														<thead>
															<tr class="tblHdr">
																<th>Monthly</th>
															</tr>
														</thead>
														<tbody>
															<tr>
																<td colspan="2"><form:errors cssClass="errorDsply"
																		path="dateOfMonth"></form:errors> <form:errors
																		cssClass="errorDsply" path="everyMonth"></form:errors>
																</td>
															</tr>
															<tr class="">
																<td align="left" valign="top"><form:radiobutton
																		path="isOngoingMonthly" value="date" /> Day <form:input
																		path="dateOfMonth"
																		cssClass="inputText small numeric range" maxlength="2"
																		onkeyup="isThirtyFirst(this);" /> of every <form:input
																		path="everyMonth"
																		cssClass="inputText small numeric range" maxlength="2" />
																	month(s)</td>
															</tr>
															<tr class="">
																<td align="left" valign="top"><form:errors
																		cssClass="errorDsply" path="everyDayMonth"></form:errors>
																	<form:radiobutton path="isOngoingMonthly" value="day" />
																	The <form:select path="dayNumber"
																		cssClass="slctBx medium range">
																		<form:option value="1">First</form:option>
																		<form:option value="2">Second</form:option>
																		<form:option value="3">Third</form:option>
																		<form:option value="4">Fourth</form:option>
																		<form:option value="5">last</form:option>
																	</form:select> <!--<form:select path="everyWeekDayMonth" cssClass="slctBx medium range">
																		<form:option value="2">Monday</form:option>
																		<form:option value="3">Tuesday</form:option>
																		<form:option value="4">Wednesday</form:option>
																		<form:option value="5">Thursday</form:option>
																		<form:option value="6">Friday</form:option>
																		<form:option value="7">Saturday</form:option>
																		<form:option value="1">Sunday</form:option>
																	</form:select>-->
																	<div class="cstmSlct relative">
																		<input type="text" id="cstmDrpdwnInput" value="Select"
																			class="dsblSlct" />
																		<ul class="cstmDropdwn">
																			<li><label for=""> <form:checkbox
																						path="everyWeekDayMonth" cssClass="recDays"
																						value="1" display="Sun" /> Sunday
																			</label></li>
																			<li><label for=""> <form:checkbox
																						path="everyWeekDayMonth" cssClass="recDays"
																						value="2" display="Mon" /> Monday <strong></strong></label>
																			</li>
																			<li><label for=""> <form:checkbox
																						path="everyWeekDayMonth" cssClass="recDays"
																						value="3" display="Tue" /> Tuesday
																			</label></li>
																			<li><label for=""> <form:checkbox
																						path="everyWeekDayMonth" cssClass="recDays"
																						value="4" display="Wed" /> Wednesday
																			</label></li>
																			<li><label for=""> <form:checkbox
																						path="everyWeekDayMonth" cssClass="recDays"
																						value="5" display="Thur" /> Thursday
																			</label></li>
																			<li><label for=""> <form:checkbox
																						path="everyWeekDayMonth" cssClass="recDays"
																						value="6" display="Fri" /> Friday
																			</label></li>
																			<li><label for=""> <form:checkbox
																						path="everyWeekDayMonth" cssClass="recDays"
																						value="7" display="Sat" /> Saturday
																			</label></li>
																		</ul>
																	</div> of every <form:input path="everyDayMonth"
																		cssClass="inputText small numeric range" maxlength="2" />
																	month(s)</td>
															</tr>
														</tbody>
													</table>
												</div>
											</div></td>
									</tr>
									<tr class="ongoing grey-bg">
										<td colspan="4"><div class="ongoing brdr">
												<table width="100%" cellspacing="0" cellpadding="0"
													class="white-bg formTbl innrTbl rangeActn">
													<thead>
														<tr class="tblHdr">
															<th colspan="3">Range of recurrence</th>
														</tr>
													</thead>
													<tbody>
														<tr class="">
															<td width="7%" align="left" valign="top"><label
																class="mand">Start</label></td>
															<td width="30%" align="left" valign="top"><form:errors
																	cssClass="errorDsply" path="eventStartDate"></form:errors>
																<form:input path="eventStartDate" id="eventStartDate"
																	type="text" class="inputTxt medium" tabindex="6" /></td>
															<td width="63%" align="left" valign="bottom"><form:radiobutton
																	name="range" path="occurenceType" value="noEndDate"
																	id="noEndDt" /> <label for="noEndDt">No end
																	date</label></td>
														</tr>
														<tr class="">
															<td align="left" valign="top">&nbsp;</td>
															<td align="left" valign="top">&nbsp;</td>
															<td align="left" valign="top"><form:errors
																	cssClass="errorDsply" path="endAfter"></form:errors> <form:radiobutton
																	name="range" path="occurenceType" value="endAfter"
																	id="endAftr" /> <label for="endAftr">End after</label>
																<form:input path="endAfter"
																	cssClass="inputText small range numeric" maxlength="3" />
																occurrence(s)</td>
														</tr>
														<tr class="">
															<td align="left" valign="top">&nbsp;</td>
															<td align="left" valign="top">&nbsp;</td>
															<td align="left" valign="top"><form:errors
																	cssClass="errorDsply" path="eventEndDate"></form:errors>
																<form:radiobutton name="range" path="occurenceType"
																	value="endBy" id="endBy" /> <label for="endBy"
																class="lblEndDt">End By</label>
															<form:input path="eventEndDate" id="eventEndDate"
																	type="text" class="inputTxt medium range" tabindex="6" /></td>
														</tr>
													</tbody>
												</table>
											</div></td>
									</tr>
									<tr class="ongoing">
										<td colspan="4"></td>
									</tr>
									<tr class="subHdr">
										<td colspan="4">Is Event at a Business: <span
											class="mrgn-left spacing"> <form:radiobutton
													path="bsnsLoc" type="radio" name="bsnsLoc" id="bsnsYes"
													value="yes" tabindex="9" /> <label for="bsnsYes">Yes</label>
												<form:radiobutton path="bsnsLoc" type="radio" name="bsnsLoc"
													id="bsnsNo" value="no" tabindex="10" /> <label
												for="bsnsNo">No</label> <form:errors
													cssClass="errorDsplyHtl" path="appsiteID"></form:errors>
										</span></td>
									</tr>
									<tr class="grey-bg bsnsLoctn">
										<td colspan="4"><table width="100%" cellspacing="0"
												cellpadding="0" border="0" class="zerobrdrTbl">
												<tbody>
													<tr>
														<td width="18%"><label>Search Appsites</label></td>
														<td width="30%"><div class="cntrl-grp">
																<form:input path="appSiteSearchKey" type="text"
																	class="inputTxtBig" tabindex="11" />
															</div></td>
														<td width="10%"><a href="#"><img height="17"
																width="20" title="search appsites" alt="search"
																src="images/searchIcon.png"
																onclick="searchAppSiteHotel('appsite');" tabindex="12">
														</a></td>
														<td width="40%" align="right"></td>
													</tr>
												</tbody>
											</table> <c:if test="${requestScope.appSiteLstError ne null}">

												<div class="alertBx warning mrgnTop cntrAlgn">
													<span class="actn-close" title="close"></span>
													<p class="msgBx">
														<c:out value="${requestScope.appSiteLstError}" />
													</p>
												</div>
											</c:if></td>
									</tr>
									<tr class="grey-bg equalPdng bsnsLoctn">
										<td colspan="4"><div class="relative bsnsLoctn">
												<div class="hdrClone"></div>
												<div class="scrollTbl">
													<table width="100%" cellspacing="0" cellpadding="0"
														border="0" class="grdTbl clone-hdr fxdhtTbl white-bg"
														id="mngevntTbl">
														<thead>
															<tr class="tblHdr">
																<th width="19%">Name</th>
																<th width="26%">Address</th>
																<th width="17%">City</th>
																<th width="17%">State</th>
																<th width="13%">Zip</th>
																<th width="8%">Action</th>
															</tr>
														</thead>
														<tbody class="scrollContent">
															<c:forEach items="${sessionScope.eventAppSiteLst }"
																var="item">
																<tr>
																	<td><c:out value="${item.appSiteName }"></c:out></td>
																	<td><div class="cell-wrp">
																			<c:out value="${item.address }"></c:out>
																		</div></td>
																	<td><c:out value="${item.city }"></c:out></td>
																	<td><c:out value="${item.state }"></c:out></td>
																	<td><c:out value="${item.postalCode }"></c:out></td>
																	<td align="center"><form:checkbox path="appsiteID"
																			value="${item.appSiteId}" id="" /> <label
																		for="checkbox"></label></td>
																</tr>
															</c:forEach>
														</tbody>
													</table>
												</div>
											</div></td>
									</tr>
									<tr class="grey-bg bsnsLoctn">
										<td colspan="4"><a href="javascript:void(0);"
											onclick="addEventAppSite(this)"><img
												src="images/btn_add.png" width="24" height="24" alt="add"
												class="addImg" /> Create New App Site</a></td>
									</tr>
									<tr class="grey-bg equalPdng evntLoctn">
										<td colspan="4"><div class="evntLoctn brdr">
												<table class="white-bg formTbl" width="100%" cellpadding="0"
													cellspacing="0">
													<thead>
														<tr class="tblHdr">
															<th colspan="4">Event Location</th>
														</tr>
													</thead>
													<tr class="zeroBrdr">
														<tr>
    														<td><label>Title</label></td>
    														<td><div class="cntrl-grp">
        														<form:input path="locationTitle" type="text" style="height: 22px;" class="inputTxt inputTxtBig" maxlength="30" tabindex="13"/>
    														</div></td>
														</tr>
														<td rowspan="2" align="left" valign="top" width="19%"><label class="mand">Address</label></td>
														<td rowspan="2" align="left" valign="top" width="31%">
															<form:errors cssClass="errorDsply" path="address"></form:errors>
															<div class="cntrl-grp">
																<form:textarea path="address" name="textarea2" cols="25" rows="5" class="textareaTxt" tabindex="13"></form:textarea>
															</div>
														</td>
														<td align="left" valign="top"><label class="mand">Zip
																Code</label></td>
														<td valign="top"><form:errors cssClass="errorDsply"
																path="postalCode"></form:errors>
															<div class="cntrl-grp">
																<form:input path="postalCode" type="text"
																	class="inputTxt inputTxtBig"
																	onkeypress="zipCodeAutocomplete();return isNumberKey(event)"
																	tabindex="14" maxlength="5" />
															</div></td>
													</tr>
													<tr class="">
														<td align="left" valign="top"><label class="mand">City</label></td>
														<td align="left" valign="top"><form:errors
																cssClass="errorDsply" path="city"></form:errors>
															<div class="cntrl-grp">
																<form:input path="city" type="text"
																	class="inputTxt inputTxtBig"
																	onkeypress="cityAutocomplete();" tabindex="15" />
															</div></td>
													</tr>
													<tr class="">
														<td><label class="mand">State</label></td>
														<td><form:errors cssClass="errorDsply" path="state"></form:errors>
															<div class="cntrl-grp zeroBrdr">

																<form:select path="state" id="state" class="slctBx"
																	name="state" tabindex="16">
																	<form:option value="">--Select--</form:option>
																	<c:forEach items="${sessionScope.states}" var="s">
																		<form:option value="${s.stateabbr}"
																			label="${s.stateName}" />
																	</c:forEach>
																</form:select>
															</div></td>
													</tr>
													<tr id="dispLatLang">
														<td><label class="mand">Latitude</label></td>
														<td><form:errors cssClass="errorDsply"
																path="latitude"></form:errors>
															<div class="cntrl-grp">
																<form:input path="latitude" id="latitude" type="text"
																	class="inputTxt inputTxtBig"
																	onkeypress="return isLatLong(event)" tabindex="17" />
															</div></td>
														<td><label class="mand">Logitude</label></td>
														<td><form:errors cssClass="errorDsply"
																path="logitude"></form:errors>
															<div class="cntrl-grp">
																<form:input path="logitude" id="logitude" type="text"
																	class="inputTxt inputTxtBig"
																	onkeypress="return isLatLong(event)" tabindex="18" />
															</div></td>
													</tr>
												</table>
											</div></td>
									</tr>
									<tr class="">
										<td colspan="4"></td>
									</tr>
									<%-- <tr class="subHdr">
										<td colspan="4"><span class="">Is Event a Signature Event: </span> 
											<span class="mrgn-left spacing">
												<form:radiobutton path="isSignatureEvent" id="signatureEventYes" value="yes" tabindex=""/>
												<label for="signatureEventYes">Yes</label>
												<form:radiobutton path="isSignatureEvent" id="signatureEventNo" value="no" tabindex=""/>
												<label for="signatureEventNo">No</label>
											</span>
										</td>
									</tr>
									<tr class="">
										<td colspan="4"></td>
									</tr> --%>
									<tr class="subHdr">
										<td colspan="4"><span class="lblWrp">Is Event a Package: </span> 
											<span class="mrgn-left spacing"> 
												<form:radiobutton path="evntPckg" type="radio" name="evntPckg" id="evntYes" value="yes" tabindex="19" /> 
												<label for="evntYes">Yes</label>
												<form:radiobutton path="evntPckg" type="radio" name="evntPckg" id="evntNo" value="no" tabindex="20" /> 
												<label for="evntNo">No</label>
											</span>
										</td>
									</tr>
									<tr class="">
                  						<td colspan="4"></td>
                					</tr>
                					<tr class="subHdr">
					                	<td colspan="4"><span class="">Is Event a Event Logistics: </span> 
					                		<span class="mrgn-left spacing">
					                    		<form:radiobutton path="isEventLogistics" name="isEventLogistics" id="evntLogisticYes" value="yes" tabindex="21"/>
					                    		<label for="evntLogisticYes">Yes</label>
					                    		<form:radiobutton path="isEventLogistics" name="isEventLogistics" id="evntLogisticNo" value="no" tabindex="22"/>
					                    		<label for="evntLogisticNo">No</label>
					                    	</span>
					                    </td>
               						</tr>
								</table>
								<div class="cntrInput mrgnTop">
									<input type="button" id="saveEventBtn" value="Save" onclick="saveEvent();" class="btn-blue" tabindex="23" title="save" /> 
									<input type="button" id="nextBtn" value="Next" class="btn-blue" tabindex="24" title="next" /> 
									<input type="button" value="Clear" onclick="clearForm();" class="btn-blue" tabindex="25" title="clear" /> 
									<input type="button" value="Back" onclick="displayManageEvents();" class="btn-blue" tabindex="26" title="back" />
								</div>
							</div>
							<div class="eventPackage pill-cont">
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="brdrlsTbl">
									<tr>
										<td width="20%"><label class="mand">Package Price</label></td>
										<td width="30%"><form:errors cssClass="errorDsply"
												path="packagePrice"></form:errors>
											<div class="cntrl-grp">
												<form:input path="packagePrice" type="text"
													class="inputTxt priceVal" tabindex="27" />
											</div></td>
										<td width="20%" align="left"><label class="mand">Package
												Tickets URL</label></td>
										<td align="left" valign="top"><form:errors
												cssClass="errorDsply" path="packageTicketURL"></form:errors>
											<div class="cntrl-grp">
												<form:input path="packageTicketURL" type="text"
													class="inputTxt" tabindex="28" />
											</div></td>
									</tr>
									<tr>
										<td align="left"><label class="mand">Package
												Description</label></td>

										<td><form:errors cssClass="errorDsply"
												path="packageDescription"></form:errors>
											<div class="cntrl-grp">
												<form:textarea path="packageDescription" name="textarea3"
													cols="25" rows="5" class="textareaTxt" tabindex="29"></form:textarea>
											</div></td>
										<td align="left" valign="top"></td>
									</tr>
									<tr class="subHdr">
										<td colspan="4">Event Hotels: <span
											class="mrgn-left spacing"> <form:radiobutton
													path="evntHotel" type="radio" name="evntHotel" id="pckgYes"
													value="yes" tabindex="30" /> <label for="pckgYes">Yes</label>
												<form:radiobutton path="evntHotel" type="radio"
													name="evntHotel" id="pckgNo" value="no" tabindex="31" /> <label
												for="pckgNo">No</label> <form:errors
													cssClass="errorDsplyHtl" path="retailLocationID"></form:errors>
										</span></td>
									</tr>
									<tr class="grey-bg eventHotel">
										<td colspan="4"><table width="100%" cellspacing="0"
												cellpadding="0" border="0" class="zerobrdrTbl">
												<tbody>
													<tr>
														<td width="18%"><label>Search Hotels</label></td>
														<td width="30%"><div class="cntrl-grp">
																<form:input type="text" path="hotelSearchKey"
																	class="inputTxtBig" style="height: 22px;" tabindex="32" />
															</div></td>
														<td width="10%"><a href="#"><img height="17"
																width="20" src="images/searchIcon.png" alt="search"
																title="search hotels"
																onclick="searchAppSiteHotel('hotel');" tabindex="33">
														</a></td>
														<td width="40%" align="right"></td>
													</tr>
												</tbody>
											</table> <c:if test="${requestScope.hotelListError ne null}">

												<div class="alertBx warning mrgnTop cntrAlgn">
													<span class="actn-close" title="close"></span>
													<p class="msgBx">
														<c:out value="${requestScope.hotelListError}" />
													</p>
												</div>
											</c:if></td>
									</tr>
									<tr class="grey-bg equalPdng eventHotel">
										<td colspan="4"><div class="relative">
												<div class="scrollTbl hrzScroll">
													<table width="1000" cellspacing="0" cellpadding="0"
														border="0" class="grdTbl fxdhtTbl white-bg"
														id="eventHotel">
														<c:if
															test="${sessionScope.hotelList ne null && !empty sessionScope.hotelList}">
															<thead>
																<tr class="tblHdr">
																	<th>Action</th>
																	<th>Name</th>
																	<th>Address</th>
																	<th>City</th>
																	<th>State</th>
																	<th>Zip</th>
																	<th>Add Hotel Price</th>
																	<th>Add Hotel Discount Code</th>
																	<th>Add Discount Amount</th>
																	<th>Room Availability Check URL</th>
																	<th>Room Booking URL</th>
																	<th>Rating</th>
																</tr>
															</thead>
														</c:if>
														<tbody class="scrollContent">
															<c:forEach items="${sessionScope.hotelList}" var="item">
																<tr id="${item.retailLocationID }">
																	<td align="center"><form:checkbox type="checkbox"
																			name="associateHotel" id="associateHotel"
																			path="retailLocationID"
																			value="${item.retailLocationID }" /> <label
																		for="checkbox"></label></td>
																	<td><input type="hidden" class="grdInput"
																		value="${item.retailLocationID }" readonly="readonly"
																		name="locationID"> <input type="text"
																		class="grdInput" value="${item.retailName }"
																		readonly="readonly" name="retailName"></td>
																	<td><input type="text" class="grdInput"
																		value="${item.address1 }" readonly="readonly"
																		name="hoteladdress" /></td>
																	<td><input type="text" class="grdInput"
																		value="${item.city }" readonly="readonly"
																		name="hotelcity" /></td>
																	<td><input type="text" class="grdInput"
																		value="${item.state }" readonly="readonly"
																		name="hotelstate" /></td>
																	<td><input type="text" class="grdInput"
																		value="${item.postalCode }" readonly="readonly"
																		name="zipCode" /></td>
																	<td><input type="text" class="grdInput priceVal"
																		name="price" value="${item.hotelPrice }" /></td>
																	<td><input type="text" class="grdInput"
																		name="discountCode" value="${item.discountCode }" /></td>
																	<td><input type="text" class="grdInput priceVal"
																		name="hotelDiscountAmount"
																		value="${item.discountAmount }" /></td>
																	<td><input type="text" class="grdInput"
																		name="roomAvailtyCheckURL"
																		value="${item.roomAvailabilityCheckURL }" /></td>
																	<td><input type="text" class="grdInput"
																		name="roomBkngURL" value="${item.roomBookingURL }" /></td>
																	<td><select class="slctSmall" name="hotelRating">
																			<c:set var="ratingInc" value="1" />
																			<c:forEach var="i" begin="1" end="9">
																				<fmt:formatNumber maxFractionDigits="1"
																					type="number" maxIntegerDigits="1"
																					value="${ratingInc}" var="flRating" scope="page">
																				</fmt:formatNumber>
																				<c:choose>
																					<c:when test="${item.rating == ratingInc}">
																						<option value="${ratingInc}" selected="selected">${ratingInc}</option>
																					</c:when>
																					<c:otherwise>
																						<option value="${ratingInc}">${ratingInc}</option>
																					</c:otherwise>
																				</c:choose>
																				<c:set var="ratingInc" value="${ratingInc + 0.5}" />
																			</c:forEach>
																	</select></td>
																</tr>
															</c:forEach>
														</tbody>
													</table>
												</div>
											</div></td>
									</tr>
								</table>
								<div class="cntrInput mrgnTop">
									<input type="button" class="btn-blue" id="pckDetBtn" onclick="validateHotelDetails();" value="Save" tabindex="34" title="save"> 
									<input type="button" id="eventPckNextBtn" value="Next" class="btn-blue" tabindex="24" title="next" /> 
									<input type="button" class="btn-blue" onclick="clearPackageForm();" value="Clear" tabindex="35" title="clear"> 
									<input type="button" class="btn-blue" id="backBtn" value="Back" tabindex="36" title="back">
								</div>
							</div>
							
							<!-- Event Logistics Start-->
							<div class="eventLogistics pill-cont">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="brdrlsTbl">
				                <tr>
				                  	<td width="20%"><label class="mand">Image Upload</label></td>
									<td width="30%" >
									  	<form:errors cssClass="errorDsply" path="logisticsImgName"></form:errors>
									   	<label class="imgLbl">
											<img id="logisticsImgName" width="100" height="50" alt="upload" src="${sessionScope.logisticsImgPreview}">
										</label>
										<span class="topPadding cmnRow"> 
											<label for="trgrUpldLogisticsImg">
												<input type="button" value="Upload" id="trgrUpldLogisticsImgBtn" class="btn trgrUpld" title="Upload Image File" tabindex="">
												<form:hidden path="logisticsImgName" id="logisticsImgName" />
												<span class="instTxt nonBlk"></span> 
												<form:input type="file" class="textboxBig" id="trgrUpldLogisticsImg" path="logisticsImgFile" tabindex="37" />
											</label>
										</span>
										<label id="maxSizeLogisticsImg" class="errorDsply"></label>
									</td>
				                  <td colspan="2" align="left" >&nbsp;</td>
				                </tr>
				            	<tr>
				                	<td align="left" ><label class="mand">Add Button</label></td>
				                  	<td ><div class="cntrl-grp">
				                    	<form:input path="buttonName" id="addLogisticBtnName" type="text" class="inputText" maxlength="40" placeholder="Enter Button Name" tabindex="38"/>
				                  	</div></td>
				                  	<td width="30%" align="left" valign="top">
				                  		<div class="cntrl-grp">
				                    		<form:input path="buttonLink" id="addLogisticBtnLink" type="text" class="inputText" maxlength="200" placeholder="Enter Button Link" tabindex="39"/>
				                  		</div> 
				                  	</td>
				                  	<td align="left" valign="top">
				                  		<a href="#" onclick="updateEventLogisticsButtonList('add', '')">
				                  			<img width="24" height="24" src="images/btn_add.png" alt="add" class="addElem" title="Add Button">
				                  		</a>
				                  	</td>
				                </tr>
				                
				                <tr class="">
                  					<td colspan="4"></td>
                				</tr>
				                
				               	<tr class="subHdr">
					               	<td colspan="4"><span class="">Is Event Logistics Overlay: </span> 
					               		<span class="mrgn-left spacing">
					                   		<form:radiobutton path="isEventOverlay" name="isEventOverlay" id="isEventOverlayYes" value="yes" tabindex="40"/>
					                   		<label for="isEventOverlayYes">Yes</label>
					                   		<form:radiobutton path="isEventOverlay" name="isEventOverlay" id="isEventOverlayNo" value="no" tabindex="41"/>
					                   		<label for="isEventOverlayNo">No</label>
					                   	</span>
					            	</td>
               					</tr>
				                
				                <tr>
				                  <td colspan="4"><form:errors id="btnNames" cssClass="errorDsply" path="btnNames"></form:errors><b>Button Details:</b></td>
				                </tr>
				                <tr class="equalPdng">
				                  <td colspan="4" ><div class="relative">
				                   <div class="hdrClone"></div>
				                      <div class="scrollTbl tblHt">
				                        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="grdTbl clone-hdr white-bg" id="eventLogisticsTbl">
				                          	<thead>
				                            	<tr class="tblHdr">
				                              		<th width="40%">Button Name</th>
				                              		<th width="45%">Button Link</th>
				                              		<th width="15%">Action </th>
				                            	</tr>
				                          	</thead>
				                          	<tbody class="scrollContent" id="eventLogisticsBtn">
				                          		<c:forEach items="${sessionScope.eventLogisticsBtns}" var="buttons">
					                          		<tr id="logisticsBtnListId-${buttons.buttonId}">
					                          			<td width="40%">${buttons.buttonName}</td>
					                          			<td width="40%">${buttons.buttonLink}</td>
					                          			<td>
					                          				<a href="#" title="edit"><img width="20" height="20" src="images/edit_icon.png" alt="edit" class="actn-icon" onclick="editEventLogisticsButton('logisticsBtnListId-${buttons.buttonId}')"></a>
					                          				<a href="#" title="delete"><img width="20" height="20" src="images/delete_icon.png" alt="delete" class="actn-icon" onclick="updateEventLogisticsButtonList('delete', ${buttons.buttonId})"></a>
					                          			</td>
				                          			</tr>
				                          		</c:forEach>
				                       		</tbody>
				                        </table>
				                      </div>
				                      <!-- <div class="pagination mrgnTop"><a title="backward" class="backward" href="#">&nbsp;</a> <a title="previous" class="previous" href="#">&nbsp;</a> Page 1 of 4 &nbsp;<a title="next" class="next" href="#">&nbsp;</a> <a title="forward" class="forward" href="#">&nbsp;</a></div> -->
				                    </div></td>
				                </tr>
				                
				        		</table>
				        		<div class="cntrInput mrgnTop">
					           		<input type="button" class="btn-blue" id="logisticsSaveBtn" onclick="saveLogisticsEvent();" value="Save" tabindex="42" title="save"> 
									<input type="button" class="btn-blue" onclick="clearLogisticsForm();" value="Clear" tabindex="43" title="clear"> 
									<input type="button" class="btn-blue" id="logisticsBackBtn" value="Back" tabindex="44" title="back">
					        	</div>
							</div>
							<!-- Event Logistics End -->
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
<script type="text/javascript">
	onLoad();

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
</script>
<script type="text/javascript">
	configureMenu("setupevents");
</script>