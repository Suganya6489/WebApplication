function saveLoginSettings() {
	document.screenSettingsForm.action = "setuploginscreen.htm";
	document.screenSettingsForm.method = "POST";
	document.screenSettingsForm.submit();
}

function saveRegScreenSettings() {
	document.screenSettingsForm.action = "setupregscreen.htm";
	document.screenSettingsForm.method = "POST";
	document.screenSettingsForm.submit();
}

function saveAboutUsScreenSettings() {
	document.screenSettingsForm.action = "setupaboutusscreen.htm";
	document.screenSettingsForm.method = "POST";
	document.screenSettingsForm.submit();
}

function savePrivacyPolicyScreenSettings() {
	document.screenSettingsForm.action = "setupprivacypolicyscreen.htm";
	document.screenSettingsForm.method = "POST";
	document.screenSettingsForm.submit();
}

function saveSplashScreenSettings() {
	document.screenSettingsForm.action = "setupsplashscreen.htm";
	document.screenSettingsForm.method = "POST";
	document.screenSettingsForm.submit();
}

function saveGeneralSettings(type) {
	document.screenSettingsForm.pageType.value = type;
	document.screenSettingsForm.action = "generealsettings.htm";
	document.screenSettingsForm.method = "POST";
	document.screenSettingsForm.submit();
}

function displayGeneralSettings(type) {
	document.screenSettingsForm.pageType.value = type;
	document.screenSettingsForm.action = "generealsettings.htm";
	document.screenSettingsForm.method = "GET";
	document.screenSettingsForm.submit();
}

function isNumberKey(evt) {
	var charCode = (evt.which) ? evt.which : event.keyCode
	if ((charCode > 47 && charCode < 58) || charCode < 31)
		return true;

	return false;
}

function zipCodeAutocomplete() {

	$("#postalCode").autocomplete({
		minLength : 3,
		delay : 500,
		source : 'displayZipCodeStateCity.htm',
		select : function(event, ui) {

			if (ui.item.value == "No Records Found") {
				$("#city").val("");

			} else {
				$("#postalCode").val(ui.item.zip);
				$("#city").val(ui.item.city);
				$('#hiddenState').val(ui.item.statecode);

				var ddl = document.getElementById('state');

				var opts = ddl.options.length;

				for (var i = 0; i < opts; i++) {
					if (ddl.options[i].value == ui.item.statecode) {
						ddl.options[i].selected = true;
						break;
					}
				}
			}
			return false;
		}
	});
}

function cityAutocomplete(postalId) {

	$('#city').autocomplete({
		minLength : 4,
		delay : 500,
		source : 'displayCityStateZipCode.htm',
		select : function(event, ui) {
			$("#postalCode").val(ui.item.zip);
			$("#city").val(ui.item.city);
			$('#hiddenState').val(ui.item.statecode);
			// $('#stateCodeHidden').val(ui.item.statecode);

			var ddl = document.getElementById('state');
			var opts = ddl.options.length;
			for (var i = 0; i < opts; i++) {
				if (ddl.options[i].value == ui.item.statecode) {
					ddl.options[i].selected = true;
					break;
				}
			}

			return false;
		}
	});
}

// check for valid numeric values
function IsNumericDot(e) {
	var key = window.event ? e.keyCode : e.which;
	var keychar = String.fromCharCode(key);
	reg = /[0-9.]+(\.[0-9.])?/;
	return reg.test(keychar);
}

function validateHotelDetails() {

	var productJson = [];
	var productObj = {};
	var validrmAvbiltyCheckURLFlag = true;
	var roomBookingURLFlag = true;
	var objArr = [];
	var vRetLocList = [];
	var arrIndex = 0;
	if ($("#pckgYes").prop('checked')) {
		$("#eventHotel")
				.find("tr")
				.each(
						function(index) {
							if (index > 0) {
								var highlightObj = $(
										$("#eventHotel").find("tr")[index])
										.children();
								var object = {};
								var stringObj;

								/*
								 * if
								 * (highlightObj.find("input:checkbox:checked").length >
								 * 0) { highlightObj
								 * .find("input:checkbox:checked") .each(
								 * function() {
								 */

								highlightObj
										.find("input")
										.each(
												function(rVal) {

													var inputTagVal = $(this)[0].value;
													var name = $(this).attr(
															"name")

													if (name == 'locationID') {
														object.retailLocationID = inputTagVal;

													} else if (name == 'retailName') {
														object.retailName = inputTagVal;

													} else if (name == 'hoteladdress') {
														object.address = inputTagVal;

													} else if (name == 'hotelcity') {
														object.city = inputTagVal;

													} else if (name == 'hotelstate') {
														object.state = inputTagVal;

													} else if (name == 'zipCode') {
														object.zipCode = inputTagVal;

													} else if (name == 'price') {
														object.hotelPrice = inputTagVal;

													} else if (name == 'discountCode') {
														object.hotelDiscountCode = inputTagVal

													} else if (name == 'hotelDiscountAmount') {
														object.discountAmount = inputTagVal

													} else if (name == 'roomAvailtyCheckURL') {
														object.roomAvailabilityCheckURL = inputTagVal

													} else if (name == 'roomBkngURL') {
														object.roomBookingURL = inputTagVal

													} else if (name == 'keyword') {
														object.keyword = inputTagVal
													}

												});

								object.rating = highlightObj.find(
										"select option:selected").text();

								objArr[arrIndex++] = object;

								// });
								// }

							}
						});

		if (objArr.length > 0) {
			var validationFlag = true;
			for (var k = 0; k < objArr.length; k++) {

				var regexp = /(ftp\:\/\/|http\:\/\/|https\:\/\/|www\.)(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
				var object = objArr[k];
				var retailLocationID = object.retailLocationID;
				var roomAvailabilityCheckURL = object.roomAvailabilityCheckURL;
				var roomBookingURL = object.roomBookingURL;

				$("tr#" + retailLocationID).removeClass("hilite");
				if (roomAvailabilityCheckURL != '') {
					if (!regexp.test(roomAvailabilityCheckURL)) {
						$("tr#" + retailLocationID).addClass("requiredVal");
						validrmAvbiltyCheckURLFlag = false;
					}
				}

				if (roomBookingURL != '') {
					if (!regexp.test(roomBookingURL)) {
						$("tr#" + retailLocationID).addClass("requiredVal");
						roomBookingURLFlag = false;
					}
				}

			}

			if (!validrmAvbiltyCheckURLFlag) {
				alert("Please enter a valid Room Availibility Check URL.");
				return false;
			}

			else if (!roomBookingURLFlag) {
				alert("Please enter a valid Room Booking URL.");
				return false;
			} else {

				var strinObjArr = []
				for (var k = 0; k < objArr.length; k++) {

					var stringObj
					var object = objArr[k];
					stringObj = "{\"retailLocationID\":\""
							+ object.retailLocationID + "\","
							+ "\"hotelPrice\":\"" + object.hotelPrice + "\","
							+ "\"discountCode\":\"" + object.hotelDiscountCode
							+ "\"," + "\"discountAmount\":\""
							+ object.discountAmount + "\"," + "\"rating\":\""
							+ object.rating + "\","
							+ "\"roomAvailabilityCheckURL\":\""
							+ object.roomAvailabilityCheckURL + "\","
							+ "\"roomBookingURL\":\"" + object.roomBookingURL
							+ "\"}";
					strinObjArr[k] = stringObj;
				}

				saveEvent(strinObjArr.toString());

			}
		} else {

			saveEvent(null);
		}

	} else {

		saveEvent(null);
	}

}

function saveEvent(hotelData) {

	var evtPkg = $("input[name='evntPckg']:checked").attr("value");

	if (evtPkg == 'no') {
		$('input:radio[name=evntHotel]').attr('checked', false);

	}

	if (null != hotelData) {

		document.screenSettingsForm.hotelListJson.value = "{\"hotelList\":["
				+ hotelData + "]}";

	} else {

		document.screenSettingsForm.hotelListJson.value = "";
	}

	document.screenSettingsForm.action = "saveEvent.htm";
	document.screenSettingsForm.method = "POST";
	document.screenSettingsForm.submit();
}

function configureMenu(menuName) {

	var activeClass = $("#" + menuName).attr('class');
	$("#" + menuName).removeClass(activeClass)
	activeClass = activeClass + "-" + "active";

	$("#" + menuName).closest("li").addClass("active");
	$("#" + menuName).addClass(activeClass);

}

function displayDeals() {
	document.deals.dealName.value="Hotdeals";
	document.deals.dealSearchKey.value="";
	document.deals.lowerLimit.value=0;
	document.deals.action = "displaydeals.htm";
	document.deals.method = "GET";
	document.deals.submit();
}

function displayCoupons() {
	document.deals.dealName.value="Coupons";
	document.deals.dealSearchKey.value="";
	document.deals.lowerLimit.value=0;
	document.deals.action = "displaydeals.htm";
	document.deals.method = "GET";
	document.deals.submit();
}

function displaySpecials() {
	document.deals.dealName.value="SpecialOffers";
	document.deals.dealSearchKey.value="";
	document.deals.lowerLimit.value=0;
	document.deals.action = "displaydeals.htm";
	document.deals.method = "GET";
	document.deals.submit();
}