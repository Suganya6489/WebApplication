var isIE = !!navigator.userAgent.match(/msie/i);
var bVer = document.documentMode;
var isTE8 = isIE && bVer < 9 || document.documentMode == 8;
var isChrome = /Chrome/.test(navigator.userAgent)
		&& /Google Inc/.test(navigator.vendor);
$(document)
		.ready(
				function() {

					/*-------------------------------Reusable code for text truncate functuion call--------------------------*/

					if ($('.retName').length > 0) {
						$(".retName").truncateText({
							txtlength : 30
						// text limit
						});
					}

					/* START OF FIND SUBCATEGORY CHAGNES */					

					$('.sub-ctgry li input[name$="btnLinkId"]:checkbox')
							.click(
									function() {
										var childChkbx = $(this)
												.parents('li')
												.find(
														'input[name$="btnLinkId"]:checkbox:not(".main-ctrgy")').length;
										var childChkbxChkd = $(this)
												.parents('li')
												.find(
														'input[name$="btnLinkId"]:checkbox:not(".main-ctrgy"):checked').length;
										if (childChkbxChkd < 1) {
											$(this).parents('li').find(
													".main-ctrgy").removeAttr(
													'checked');
											$(this).parents('.sub-ctgry')
													.hide();
										} else {
											$(this).parents('li').find(
													".main-ctrgy").prop(
													'checked', 'checked');
											$(this).parents('.sub-ctgry')
													.show();
										}
										prntCheckAll();
									});

									/*var mainChkd = $('.main-ctrgy').prop('checked');
									if (mainChkd) {
												$(mainChkd).each(function(index, element) {													
													$(this).parent("span").next("ul .sub-ctgry").show();
													//$('.sub-ctgry').show();
												});
									}
									else{
										$('.sub-ctgry').hide();									
									}*/

					/*
					 * Any one of the child{chkbx) uncheck ,uncheck parent
					 * chkbx- SubCategory changes
					 */
					$('#Find input[name$="btnLinkId"]:checkbox').click(function() {
						//	$(document).on('click','#Find input[name$="btnLinkId"]:checkbox',function() {
						var prntCtgry = $(this).hasClass('main-ctrgy');
						var prntstatus = $(this).prop("checked");
						if (prntCtgry) {
							$(this).parents('li').find('.sub-ctgry li').each(function(index, element) {
								$(this).find('input[name$="btnLinkId"]:checkbox').prop('checked', prntstatus);
							});
						}
						if (prntstatus) {
							$(this).parents('li').find('.sub-ctgry').show();
						} else {
							childCheck(this);
						}
						prntCheckAll();
					});

					/* END OF FIND SUBCATEGORY CHAGNES */

					/*
					 * Search field clear option dislay on enter & clear if no
					 * value exists
					 */
					$(".srch-cntrl input").keyup(function() {
						if ($(this).val().length != 0) {
							$(".clear-srch").fadeIn('fast', function() {
								$(this).animate({
									'right' : '4px'
								});
							});
						} else {
							$(".clear-srch").stop().animate({
								'right' : '-14px'
							}, 'slow');
						}
					});
					$('.srch-cntrl input').keydown(function(e) {
						if (e.keyCode == 27) {
							$(this).val("");
							$(".clear-srch").stop().animate({
								'right' : '-14px'
							}, 'slow');
						}
					});
					$(".clear-srch").click(function(event) {
						$(".clear-srch").stop().animate({
							'right' : '-14px'
						}, 'slow');
						$(".srch-cntrl input").val("");
					});
					/* Set Placeholder: pass id as function attribute */
					// setPlaceholderText('rtlrSrch');
					/** END of Retailer location scripts * */

					/*
					 * Main Menu grouping input display content based on
					 * checkbox clicked
					 */
					$("input[name='menuFilterType']:checked").each(function() {
						var chkdSlctn = $(this).attr("id");
						$("." + chkdSlctn).show();
					});

					// For removing border in General Settings screen
					$(".tabd-nav li a:last").css("border-right", "0px");

					/*
					 * close modal popup on click of x
					 * $(".modal-close").on('click.popup', function() {
					 * $(".modal-popupWrp").hide(); $(".modal-popup").slideUp();
					 * $(".modal-popup").find(".modal-bdy input").val("");
					 * $(".modal-popup i").removeClass("errDsply"); });
					 */

					/* Table header fixed onscroll of table rows */
					$('.clone-hdr th').each(
							function() {
								var cellCnt = $(".clone-hdr").find("th");
								thwdth = $(this).width();
								for (i = 0; i < cellCnt.length; i++) {
									$cell = $(this);
									cellText = $cell.html();
									thwdth = $cell.attr("width");
									tmpCell = $("<span style='width:" + thwdth
											+ "'>" + cellText + "</span>");
								}
								$(".hdrClone").append(tmpCell);
							});
					$(".hdrClone").find('span:last').css("text-indent", "8px");

					$('#btmCntrl').change(function() {
						var tglRow = $('tr.shareInfo');
						if ($(this).find(':selected').val() === '') {
							$(tglRow).show();
						} else {
							$(tglRow).hide();
						}
					});
					var btmSlctn = $("#btmCntrl option:selected");
					$(btmSlctn).trigger('change');

					// To view the submenu name as you type in iphone preview
					$("#subMenuInput").keyup(function(event) {
						var stt = $(this).val();
						$("td.genTitle").text(stt);
					});

					$(".exstngIcon li").click(function() {
						$(".exstngIcon li").removeClass("active");
						$(this).addClass("active");
					});

					/* fix for ie8 last child issue */
					$(".infoList li span.cell:last-child").css({
						"margin-left" : '25px',
						"width" : '92%'
					});
					$(".delGrp").click(function() {
						$(this).parents('tr').hide();
					});

					/* Retain selected radio btn option on page load */
					$("input[name='btnLinkId']:radio").change(function() {
						var slctAppId = $(this).attr("id");

						$("#" + slctAppId).attr("chcked", true);
					});

					$("#tabBarLst li").click(function() {
						$("#tabBarLst li").removeClass("active");
						$(this).addClass("active");
					});

					$("#savdTab li").click(function() {
						$("#savdTab li").removeClass("active");
						$(this).addClass("active");
						$("#tab_del").show();

					});

					/*
					 * To Set equal heights for the divs pass div class names as
					 * parameter
					 */
					setequalHeight($("#equalHt .cont-block"));
					setequalHeight($(".cntrl-grp input"));
					// $("#menu-pnl").height($(".content").height());
					$(".actn-close").click(function() {
						$(this).parent('div.alertBx').remove();

					});

					$(document).keypress(
							function(e) {
								// for enter key & space bar.
								if (e.which == 13 || e.which == 32) {
									// alert('You pressed enter!');
									var bool = $('#loginSec').find(
											'ul.chngpswd').length > 0;
									if (bool) {
										validateUserForm([ '#pswdNew',
												'#pswdCfrm' ], 'li', chkPwd);
									} else {
										validateUserForm([ '#password',
												'#userName' ], 'li', chkUser);
									}
								}
							});
					if (isTE8) {
						$('html').addClass('ie8');
					}

					if (isChrome) {
						$('html').addClass('chrome');
					}

					$('.trgrUpld').click(function() {

						if (!isIE && !isTE8)
							$('#trgrUpld').click(function(event) {
								event.stopPropagation();
							});

					});

					$(".prgrsInfo li").mouseover(function() {
						var getTtl = $(this).find("a").attr("alt");
						var chkSts = $(this).attr("class");
						if (chkSts == "stsCmpltd") {
							$(this).attr("title", getTtl + " - Completed");
						} else {
							$(this).attr("title", getTtl + " - Pending");
						}
					});

					$('#trgrUpld')
							.bind(
									'change',
									function() {
										var imageType = document
												.getElementById("trgrUpld").value;
										if ($("#dynTab li").find("a").length != 0) {
											var btnOrderList = "";
											$("#dynTab li")
													.find("a")
													.each(
															function() {
																var btnId = $(
																		this)
																		.attr(
																				"iconid");
																if (btnOrderList == "") {
																	btnOrderList = btnId
																} else {
																	btnOrderList = btnOrderList
																			+ "~"
																			+ btnId;
																}

															});
											document.screenSettingsForm.btnPosition.value = btnOrderList;
										}
										var checkedSubCat = $.makeArray($('ul.sub-ctgry').find('input[name="btnLinkId"]:checkbox:checked:visible')
												.map(function() {
													return $(this).val();
												}));
										if(checkedSubCat.length > 0) {
											document.screenSettingsForm.hiddenSubCate.value = checkedSubCat;
										}

										if (imageType != '') {
											var checkbannerimg = imageType
													.toLowerCase();
											if (!checkbannerimg
													.match(/(\.png|\.jpeg|\.jpg|\.gif|\.bmp)$/)) {
												alert("You must upload image with following extensions : .png, .gif, .bmp, .jpg, .jpeg");
												return false;
											} else {
												$("#screenSettingsForm")
														.ajaxForm(
																{
																	success : function(
																			response) {

																		var imgRes = response
																				.getElementsByTagName('imageScr')[0].firstChild.nodeValue

																		var substr = imgRes
																				.split('|');
																		if (substr[0] == 'maxSizeImageError') {
																			$(
																					'#maxSizeImageError')
																					.text(
																							"Image Dimension should not exceed Width: 800px Height: 600px");
																		} else {
																			openIframePopup(
																					'ifrmPopup',
																					'ifrm',
																					'/HubCiti/cropImage.htm',
																					100,
																					99.5,
																					'Crop Image');
																		}

																	}
																}).submit();
											}
										}

									});

					/* Main Menu scripts */

					$(".grdLst li").click(
							function() {
								var cur = $(this).find(
										"input[name='tmpltOptn']");
								var getOptn = cur.attr("id");
								var getId = $(this).find(
										"input[name='tmpltOptn']").prop(
										"checked", "true").attr("id");
								// console.log(getId);
								$(".grdLst li span").removeClass("tmpltEnbl");
								$(".grdLst li a").hide();
								$(this).find("span").removeClass("tmpltDsbl")
										.addClass("tmpltEnbl");
								$(this).find("a").slideDown();
							});

					$(".grdLst li").mouseenter(function() {
						$(".grdLst li span").addClass("tmpltDsbl");
					}).mouseleave(function() {

						// $(".grdLst li span").removeClass("tmpltEnbl")
					});

					// Toggle content based on selected option
					$("#dataFnctn").change(function() {
								var slctVal = $("#dataFnctn option:selected").attr('typeVal');
								
								$(".cmnList").hide();

								$("#" + slctVal).show();
								if (slctVal === "Find") {
									$(".input-actn-fundraiser").hide();
									$(".input-actn-evnt").hide();
									$('#findchkAll').removeAttr('checked');
									$(".intput-actn").show();
									var mainChkd = $('.main-ctrgy').prop('checked');
									if (mainChkd) {
										$(mainChkd).each(function(index, element) {
											$(this).parent("span").next("ul .sub-ctgry").show();
										});							
									} else {
										$('.sub-ctgry').hide();									
									}					

									if ($(".sub-ctgry input:checkbox[value='NULL']").length > 0) {
										$(".sub-ctgry input[value='NULL']").parent('li').hide();
									}
								} else {
									$(".intput-actn").hide();
								}

								if (slctVal === "Events") {
									$(".intput-actn").hide();
									$('#evntchkAll').removeAttr('checked');
									$(".input-actn-evnt").show();
								} else {
									$(".input-actn-evnt").hide();
								}
								
								if("tabBarSetup" != document.screenSettingsForm.viewName.value){
								var loginUserType = document.screenSettingsForm.userType.value;
								if (loginUserType === "RegionApp" && "undefined" !== typeof(slctVal)) {
									$('#Cities input[name$="citiId"]:checkbox').removeAttr('checked');
									$('#citychkAll').removeAttr('checked');
									$(".cityPnl").show();
								}
								else {									
									$(".cityPnl").hide();
								}
								}
								//Start : Adding changes related to fundraiser implementation 
								if (slctVal === "Fundraisers") {
									
									$(".intput-actn").hide();
									$('#fundraChkAll').removeAttr('checked');
									$(".input-actn-fundraiser").show();
								} else {
									$(".input-actn-fundraiser").hide();
								}
								//End : Adding changes related to fundraiser implementation 
							});
					
					
					//Start: Changes related to Adding cities
					$('#citychkAll').click(function() {
						var status = $(this).prop("checked");
						var getChkbx = $('#Cities input[name$="citiId"]:checkbox').prop('checked', status);
						if (!status) {
							$('#Cities input[name$="citiId"]:checkbox').removeAttr('checked');
						}
					});
					
					$('input[name$="citiId"]').click(function() {
						var tolCnt = $('#Cities input[name$="citiId"]:checkbox').length;
						var chkCnt = $('#Cities input[name$="citiId"]:checkbox:checked').length;
						if (tolCnt == chkCnt)
							$('#citychkAll').prop('checked', 'checked');
						else
							$('#citychkAll').removeAttr('checked');
					});		
					//End: Changes related to Adding cities

					/*
					 * Check & uncheck all child chkbx on click of parent
					 * checkbox - done.
					 */
					$('#findchkAll').click(function() {
						var status = $(this).prop("checked");
						$('#Find input[name$="btnLinkId"]:checkbox').prop('checked', status);
						$(".sub-ctgry").show();
						if (!status) {
							$('#Find input[name$="btnLinkId"]:checkbox').removeAttr('checked');
							$(".sub-ctgry").hide();
						}
					});

					/*
					 * Any one of the child{chkbx) uncheck ,uncheck parent chkbx -
					 * done
					 */
					$('input[name$="btnLinkId"]').click(function() {
						var tolCnt = $('#Find input[name$="btnLinkId"]:checkbox:visible').length;
						var chkCnt = $('#Find input[name$="btnLinkId"]:checkbox:checked:visible').length;
						if (tolCnt == chkCnt)
							$('#findchkAll').prop('checked', 'checked');
						else
							$('#findchkAll').removeAttr('checked');
					});

					$("#dataFnctn option:selected").trigger('click');
					//$('input[name$="btnLinkId"]').trigger('click');

					/* Table height dynamic */
					var rowCnt = $("#anyThngTbl tr,.fxdhtTbl tr").length;
					if (rowCnt >= 6) {

						$(".grdTbl").parent('div').addClass("tblHt");
					} else {
						$(".grdTbl").parent('div').removeClass("tblHt");
					}

					/* Display table header on scroll down */
					$(".scrollTbl").scroll(function() {
						var scrlPos = $(".scrollTbl").scrollTop();
						if (scrlPos >= 30) {
							$(".hdrClone").slideDown('fast');
						} else {
							$(".hdrClone").hide();
						}
					});

					/*
					 * events:Check & uncheck all child chkbx on click of parent
					 * checkbox
					 */
					$('#evntchkAll').click(function() {
						// $('#findchkAll').on('click.chkAll',function()
						// {
						var status = $(this).prop("checked");
						var getChkbx = $(
								'#Events input[name$="btnLinkId"]:checkbox')
								.prop('checked', status);
						if (!status) {
							$(
									'#Events input[name$="btnLinkId"]:checkbox')
									.removeAttr('checked');
						}
					});
					/* Any one of the child{chkbx) uncheck ,uncheck parent chkbx */
					$('input[name$="btnLinkId"]').click(function() {
						var tolCnt = $('#Events input[name$="btnLinkId"]:checkbox').length;
						var chkCnt = $('#Events input[name$="btnLinkId"]:checkbox:checked').length;
						if (tolCnt == chkCnt)
							$('#evntchkAll').prop('checked',
									'checked');
						else
							$('#evntchkAll').removeAttr(
									'checked');
					});
					/*
					 * events:Check & uncheck all child chkbx on click of parent
					 * checkbox ends
					 */	
					
					/* Added to add images in group tab tempalte */
					/*$("input[name='grpTabImg']:visible").change(function(){
						var getOptn = $(this).val();
						if(getOptn !== "grpTabImgYes"){
							$("#grpTabImgView").hide();
							$("#dynTab").removeClass("grpdImg");
						}
						else {
							$("#dynTab").addClass("grpdImg");
							$("#grpTabImgView").show();
						}
					});
					$('input[name="grpTabImg"]:checked').trigger('change');	*/
					/*END*/
					
					/*Adding code for fundraiser checkAll implementation */
					/*
					 * Fundraiser:Check & uncheck all child chkbx on click of parent
					 * checkbox
					 */
					$('#fundraChkAll').click(function() {
									
						var status = $(this).prop("checked");
						var getChkbx = $(
								'#Fundraisers input[name$="btnLinkId"]:checkbox')
								.prop('checked', status);
						if (!status) {
							$(
									'#Fundraisers input[name$="btnLinkId"]:checkbox')
									.removeAttr('checked');
						}
					});
					
					/* Fundraiser: Any one of the child{chkbx) uncheck ,uncheck parent chkbx */
					$('input[name$="btnLinkId"]').click(function() {
						var tolCnt = $('#Fundraisers input[name$="btnLinkId"]:checkbox').length;
						var chkCnt = $('#Fundraisers input[name$="btnLinkId"]:checkbox:checked').length;
						if (tolCnt == chkCnt)
							$('#fundraChkAll').prop('checked',
									'checked');
						else
							$('#fundraChkAll').removeAttr(
									'checked');
					});
					
				});

$(window).load(function() {
	$("#menu-pnl").height($(".content").height() + 27);
});

/* Reveal panel: Toggle menu display */
function revealPanel(obj) {
	var curSec = $(obj).attr('name');
	var curImg = $(obj).find('img');
	var path = curImg.attr('src');
	if (curImg.attr('alt') == "btn_off") {
		curImg.attr({
			'src' : changeImgName(path, 'slide_on.png'),
			'alt' : 'btn_on',
			'title' : 'Show Menu'
		});
		$('#icon-menu li a span').css("display", "none");
		$('#icon-menu li').width(18);
		$('#menu-pnl').width(42);
		$(".cont-pnl").width(896);

	} else if (curImg.attr('alt') == "btn_on") {
		curImg.attr({
			'src' : changeImgName(path, 'slide_off.png'),
			'alt' : 'btn_off',
			'title' : 'Hide Menu'
		});
		$('#icon-menu li a span').css("display", "block");
		$('#icon-menu li').css("width", "auto");
		$('#icon-menu li.active').css("width", "192px");
		$('#menu-pnl').width(216);
		$(".cont-pnl").width(722);
	}
}

function changeImgName(path, newName) {
	var imgPath = path.substr(0, path.lastIndexOf('/'));
	return (imgPath + '/' + newName);
}
// To Set equal heights for the divs
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

// To resize the html page
function resizeDoc() {
	var headerPanel = document.getElementById('hdr');
	var footerPanel = document.getElementById('ftr-wrpr');
	var resizePanel = document.getElementById('wrpr');
	/* var dockPanel = document.getElementById('dockPanel'); */

	if (document.getElementById('wrpr')) {
		if (getWinDimension()[1]
				- (headerPanel.offsetHeight + footerPanel.offsetHeight) > 0) {
			resizePanel.style.minHeight = getWinDimension()[1]
					- (headerPanel.offsetHeight + footerPanel.offsetHeight)
					+ "px";
		}
	}
}

// To get client height
function getWinDimension() {
	var windowHeight = "";
	var windowWidth = "";

	if (!document.all || isOpera()) {
		windowHeight = window.innerHeight;
		windowWidth = window.innerWidth;
	} else {
		windowHeight = document.documentElement.clientHeight;
		windowWidth = document.documentElement.clientWidth;
	}
	return [ windowWidth, windowHeight ]

}
// check if the client is opera
function isOpera() {
	return (navigator.appName.indexOf('Opera') != -1);
}

/*----------------------Display settings dropdown in header--------------------------*/
$("#settingActn").click(function() {
	$this = $(this).parents('#hdr-actns').css("position", "relative");
	$(".dropdwnBx").toggle();
});
$(".dropdwnBx li a").click(function() {
	$(".dropdwnBx").hide();
});
/* close dropdown when clicked outside of dropdown area */
$(document).click(function(e) {
	var t = (e.target);
	$(t).parent();
	/* get(0):zero-based integer indicating which element to retrieve. */
	if ($(t).parent().get(0).id !== 'settingActn') {
		$(".dropdwnBx").slideUp(300);
	} else {
		$(".dropdwnBx").slideDown(300);
	}
});

/*
 * This function for Opening the iframe popup by using the any events & we
 * cann't able to access any thing in the screen except popup parameters:
 * popupId : Popup Container ID IframeID : Iframe ID url : page path i.e src of
 * a page height : Height of a popup width : Width of a popup name : here name
 * means Header text of a popup btnBool : if you have any buttons means we can
 * use it i.e true/false
 */
function openIframePopup(popupId, IframeID, url, height, width, name, btnBool) {
	frameDiv = document.createElement('div');
	frameDiv.className = 'framehide';
	document.body.appendChild(frameDiv);
	document.getElementById(IframeID).setAttribute('src', url);
	// frameDiv.setAttribute('onclick', closePopup(obj,popupId));
	document.getElementById(popupId).style.display = "block"
	height = (height == "100%") ? frameDiv.offsetHeight - 20 : height;
	width = (width == "100%") ? frameDiv.offsetWidth - 16 : width;
	document.getElementById(popupId).style.height = height + "%"
	document.getElementById(popupId).style.width = width + "%"
	var marLeft = -1 * parseInt(width / 2);
	var marTop = -1 * parseInt(height / 2);
	document.getElementById('popupHeader').innerHTML = name;
	// document.getElementById(popupId).style.marginLeft = marLeft + "px"
	// document.getElementById(popupId).style.marginTop = marTop + "px"
	// var iframeHt = height - 27;
	var setHt = getDocHeight();

	var iframeHt = setHt - 27;
	document.getElementById(IframeID).height = iframeHt + "px";
	if (btnBool) {
		var btnHt = height - 50;
		document.getElementById(IframeID).style.height = btnHt + "px";
	}

}
/*
 * This function for Closing the iframe popup parameters: popupId : Popup
 * Container ID IframeID : Iframe ID
 */
function closeIframePopup(popupId, IframeID, funcUrl) {
	try {
		document.body.removeChild(frameDiv);
		document.getElementById(popupId).style.display = "none";
		document.getElementById(popupId).style.height = "0px";
		document.getElementById(popupId).style.width = "0px";
		document.getElementById(popupId).style.marginLeft = "0px";
		document.getElementById(popupId).style.marginTop = "0px";
		document.getElementById(IframeID).removeAttribute('src');

	} catch (e) {
		top.$('.framehide').remove();
		top.document.getElementById(popupId).style.display = "none";
		top.document.getElementById(popupId).style.height = "0px";
		top.document.getElementById(popupId).style.width = "0px";
		top.document.getElementById(popupId).style.marginLeft = "0px";
		top.document.getElementById(popupId).style.marginTop = "0px";
		top.document.getElementById(IframeID).removeAttribute('src');

	}
}
function setPlaceholderText(inputid) {
	var isWebkit = /webkit/.test(navigator.userAgent);
	var isSafari = /Safari/.test(navigator.userAgent)
			&& /Apple Computer/.test(navigator.vendor);
	var isMozilla = /Firefox/.test(navigator.userAgent);
	var isIe = /msie/.test(navigator.userAgent.toLowerCase());
	var isChrome = /Chrome/.test(navigator.userAgent);

	if (isWebkit || isSafari || isMozilla || isChrome) {
		// alert( "webkit browsers!" );
		return false;
	}

	var plchldrVal = $('#' + inputid);

	if (plchldrVal.length == 0) {
	}
	/* for ie8 borwser */
	plchldrVal.each(function(i, plchldr) {
		plchldr = $(plchldr);
		var getPlchldr = plchldr.attr('placeholder');

		if (!getPlchldr)
			return true;

		plchldr.addClass('setplchldr');
		plchldr.attr('value', getPlchldr);
		plchldr.focus(function(e) {
			if (plchldr.val() == getPlchldr) {
				plchldr.removeClass('setplchldr');
				plchldr.attr('value', '');
			}
		});
		plchldr.blur(function(e) {
			if ($.trim(plchldr.val()) == '') {
				plchldr.addClass('setplchldr');
				//plchldr.attr('value', getPlchldr);
				/* IE9 fix */
				plchldr.val(getPlchldr);
			}
		});
	});

}
/* Forgot Password */
function forgetPwd(evt) {
	var vUsername = document.getElementById("usn").value;
	if (evt && evt.which == 13) {
		if (vUsername == null || vUsername == "") {
			alert("Please enter Username");
			return false;
		}
		document.forgetpwdform.action = "forgetpwd.htm";
		document.forgetpwdform.method = "POST";
		document.forgetpwdform.submit();
	} else if (evt == '') {
		if (vUsername == null || vUsername == "") {
			alert("Please enter Username");
			return false;
		}
		document.forgetpwdform.action = "forgetpwd.htm";
		document.forgetpwdform.method = "POST";
		document.forgetpwdform.submit();
	} else {
		return true;
	}
}

function validateUserForm(arryNm, parent, funcCall) {
	var errBool = true;

	$('.errIcon').removeClass('errIcon');
	for ( var i in arryNm) {
		var $frmE = $(arryNm[i]);
		var val = $.trim($frmE.val());
		val = (val == "(   )      -") ? "" : val;
		if (val == '') {
			$frmE.parents(parent).find('.errDisp').addClass('errIcon');
			errBool = false;
		}
	}
	if (errBool) {
		if (typeof funcCall == "function") {
			funcCall();
		} else {
			window.location.href = funcCall;
		}
	} else {
		return false;
	}
}
function chkPwd() {
	document.login.action = "savepassword.htm";
	document.login.method = "post";
	document.login.submit();
}
function chkUser() {
	document.forms[0].submit();
}

/* generate unique ID */
var selectedObj, dynId = 0;
function genDynId(prefix) {
	var id = ++dynId + '';
	return prefix ? prefix + id : id;
}
/* generate Tabs */
function createTab() {
	//alert('check');
	var btnNm = $("#dynData #btnName").val();
	var btnLnk = $("#dynData #btnLink").val();
	var btnActn = $("#dynData #dataFnctn option:selected").val();
	var tmplt = $('#tmpltOptn').val();
	var grpName = $("#btnGroup").val();
	var arr = [];
	var btnCnt = $("#dynTab li").length;
	arr.push(selectedObj ? "" : genDynId('temp-btn-'));
	arr.push(btnNm);
	arr.push(btnLnk);
	arr.push(btnActn);
	arr.push(grpName);

	if ($(".vldt").val() == "") {
		alert("Please enter the details");
	} else {
		$(".prgrsStp li:first").addClass("step1Actv");
		if (selectedObj) {
			if (!document.getElementById('dynTab').className
					.indexOf('gridView')) {
				$(selectedObj).attr({
					"dataHref" : arr[2],
					"datActn" : arr[3]
				}).html("<span>" + arr[1] + "</span>");

			} else {
				$(selectedObj).attr({
					"dataHref" : arr[2],
					"datActn" : arr[3]
				}).text(arr[1]);
			}
			if (!document.getElementById('dynTab').className
					.indexOf('listView')) {
				$(selectedObj)
						.attr({
							"dataHref" : arr[2],
							"datActn" : arr[3]
						})
						.html(
								"<img src='images/dfltImg.png' width='30' height='30' alt='image' class='lstImg'/><span>"
										+ arr[1] + "</span>");
			}

			//alert('check' + arr[3]);
			if (arr[3] !== 14 && selectedObj.className.indexOf(14) >= 0) {
				$("#dataFnctn option[value=14]").attr("disabled", false);
				$(selectedObj).removeClass(14);
			} else if (arr[3] == 14) {
				$(selectedObj).addClass(14).parents('li').insertBefore(
						"#dynTab li.tabs:first");
			}
			selectedObj = null;
			$("#addBtn").attr("value", "Add Button");
			$("#delBtn").hide();
			clearData();
		} else {
			/*
			 * if(document.getElementById('dynTab').className.indexOf('listView')){
			 * $("#dynTab").append("<li>"+arr[4]+"</li>"); }
			 */

			$("#dynTab")
					.addClass(tmplt)
					.append(
							"<li class='tabs'><a href='javascript:void(0)' id='"
									+ arr[0]
									+ "' dataHref='"
									+ arr[2]
									+ "' datActn='"
									+ arr[3]
									+ "' onclick='editTab(this)' class='"
									+ arr[3]
									+ "'><img src='images/dfltImg.png' width='30' height='30' alt='image' class='lstImg'/><span>"
									+ arr[1] + "</span></a></li>");

			/*
			 * else{ $("#dynTab li a").width(160); $("#dynTab li
			 * a").css("float","left!important"); }
			 */
		}

		if (arr[3] === '14') {
			$("#dataFnctn option[value=14]").attr("disabled", true);
			dynLstItm.insertBefore("#dynTab li.tabs:first");
		}
		clearData();
	}
}

function deleteTab() {
	var cur = selectedObj;
	selectedObj = null;
	/* if experience button is deleted enable experience option for selection */
	if ($(cur).hasClass(14)) {
		$("#dataFnctn option[value=14]").attr("disabled", false);
	}
	$(cur).parents('li.active').remove();
	$("#addBtn").attr("value", "Add Button");
	$("#delBtn").hide();
	clearData();
	if ($("#dynTab li").length == 0) {
		$(".prgrsStp li").removeClass("step1Actv");
		$(".prgrsStp li").removeClass("step2Actv");
	}

}
function clearData() {
	$(".vldt").val("");
	$('#dataFnctn option:first').prop('selected', true);

}

function readURL(input) {
	if (input.files && input.files[0]) {
		var reader = new FileReader();

		reader.onload = function(e) {
			$('#imgView').attr('src', e.target.result).width(320).height(50);
		};

		reader.readAsDataURL(input.files[0]);
	}
}
/* check for ie8 browser */
if (navigator.userAgent.indexOf('MSIE 8.0') !== -1) {
	// this code will only execute on IE8
	function readURL(imgFile) {
		var newPreview = document.getElementById("preview_ie");
		newPreview.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgFile.value;
		newPreview.style.width = "320px";
		newPreview.style.height = "50px";
	}
}

function checkBtns() {
	var licnt = $("#dynTab li").length;
	if (licnt) {
		$(".prgrsStp li:eq(0)").addClass("step1Actv");
		$(".prgrsStp li:eq(1)").addClass("step2Actv");
		alert("Template Saved");
	} else {
		alert("Empty Template");
	}
}

/*
 * Method for editing iconic template
 * 
 */
function editiconicListTab(obj, menuType) {

	if (document.getElementById('logoImageName.errors') != null) {
		document.getElementById('logoImageName.errors').style.display = 'none';
	}

	if (document.getElementById('menuBtnName.errors') != null) {
		document.getElementById('menuBtnName.errors').style.display = 'none';
	}
	if (document.getElementById('menuFucntionality.errors') != null) {
		document.getElementById('menuFucntionality.errors').style.display = 'none';
	}
	if (document.getElementById('subMenuName.errors') != null) {
		document.getElementById('subMenuName.errors').style.display = 'none';
	}
	if (document.getElementById('screenSettingsForm.errors') != null) {
		document.getElementById('screenSettingsForm.errors').style.display = 'none';
	}

	if (document.getElementById('btnDept.errors') != null) {
		document.getElementById('btnDept.errors').style.display = 'none';
	}
	if (document.getElementById('btnType.errors') != null) {
		document.getElementById('btnType.errors').style.display = 'none';
	}

	if (document.getElementById('bannerImageName.errors') != null) {
		document.getElementById('bannerImageName.errors').style.display = 'none';
	}

	var edtNm = $(obj).text();
	var iconid = $(obj).attr("iconid");
	var iconImageName = $(obj).attr("iconimgname");
	var edtActn = $(obj).attr("datactn");
	var templcls = $(obj).parents('ul').attr("class").split(" ")[0];
	var grpName = $("#btnGroup").val();
	var imgSrc = $(obj).find('img').attr("src");
	var btnLinkId = $(obj).attr("linkId");
	var subCat = $(obj).attr("subCat");
	if (menuType == 'Iconic Grid') {
		$("#iconincTmptImg").attr("src", imgSrc);
	} else if (menuType == 'List View') {

		$("#logoImage").attr("src", imgSrc);
	}
	$("#delBtn").show();
	$("#addBtn").attr("value", "Save Button");
	$("." + templcls + " li.tabs a").removeClass("active");
	$(obj).addClass("active");
	$("." + templcls + " li.tabs").removeClass("active");
	$(obj).parents('li.tabs').addClass("active");
	$("#dynData #btnName").val($.trim(edtNm));
	$("#menuIconId").val(iconid);
	$("#logoImageName").val(iconImageName);
	$("#hiddenmenuFnctn").val(edtActn);
	$("#dataFnctn option[value='" + edtActn + "']")
			.prop("selected", "selected");
	selectedObj = obj;

	var btnDept = $(obj).attr("btnDept");
	var btnType = $(obj).attr("btnType");
	$(".intput-actn").hide();
	$(".input-actn-evnt").hide();
	$(".input-actn-fundraiser").hide();
	$("input[type=checkbox][name='btnLinkId']").prop("checked", false);
	$("#findchkAll").prop("checked", false);
	$("#dataFnctn option[value='" + edtActn + "']")
			.prop("selected", "selected");

	if ($("#grpDept").is(':checked') && btnDept != "") {
		$("#slctDept option[value='" + btnDept + "']").prop("selected",
				"selected");
	} else {
		$("#slctDept option[value='0']").prop("selected", "selected");
	}

	if ($("#grpType").is(':checked') && btnType != "") {
		$("#slctType option[value='" + btnType + "']").prop("selected",
				"selected");

	} else {
		$("#slctType option[value='0']").prop("selected", "selected");

	}

	var selFuncVal = $("#dataFnctn option:selected").attr('typeVal');
	if (selFuncVal == "SubMenu") {

		$('#SM-' + btnLinkId).prop('checked', 'checked');
		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	} else if (selFuncVal == "AnythingPage") {

		$('#AP-' + btnLinkId).prop('checked', 'checked');
		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	} else if (selFuncVal == "AppSite") {

		$('#AS-' + btnLinkId).prop('checked', 'checked');
		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	} else if (selFuncVal == "Find") {

		$('#Find input[name$="btnLinkId"]:checkbox').prop('checked', false);
		var arr = btnLinkId.split(',');

		jQuery.each(arr, function(i, val) {			
			if (jQuery(val).index("MC") === -1) {
				val = val.substring(0, val.lastIndexOf("-"));

				$('#FN-' + val).prop('checked', 'checked');
				$('#FN-' + val).parent().next('.sub-ctgry').show();
			}

		});

		// for subcategories NULL!~~!86,87,88,89,90,91!~~!NULL
		if (subCat != null) {

			subCat = subCat.replace(/NULL!~~!/gi, "");
			subCat = subCat.replace(/!~~!NULL/gi, "");
			var arr2 = subCat.split(',');
			jQuery.each(arr2, function(i, val) {

				//if (null != val && val != 'NULL') {
				$('#FNS-' + val).prop('checked', 'checked');
				//}

			});
		}
		$(".intput-actn").show();
		$(".cmnList").hide();
		$("#" + selFuncVal).show();
		
		var tolCnt = $('#Find input[name$="btnLinkId"]:checkbox:visible').length;
		var chkCnt = $('#Find input[name$="btnLinkId"]:checkbox:checked:visible').length;
		if (tolCnt == chkCnt)
			$('#findchkAll').prop('checked', 'checked');
		else
			$('#findchkAll').removeAttr('checked');

	} else if (selFuncVal == "Events") {

		$('#Events input[name$="btnLinkId"]:checkbox').prop('checked', false);
		var arr = btnLinkId.split(',');
		jQuery.each(arr, function(i, val) {
			$('#EVT-' + val).prop('checked', 'checked');
		});
		$(".input-actn-evnt").show();
		var tolCnt = $('#Events input[name$="btnLinkId"]:checkbox').length;
		var chkCnt = $('#Events input[name$="btnLinkId"]:checkbox:checked').length;
		if (tolCnt == chkCnt) {

			$('#evntchkAll').prop('checked', 'checked');
		}

		else {
			$('#evntchkAll').removeAttr('checked');
		}

		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	} else if (selFuncVal == "Fundraisers") {

		$('#Fundraisers input[name$="btnLinkId"]:checkbox').prop('checked', false);
		var arr = btnLinkId.split(',');
		jQuery.each(arr, function(i, val) {
			$('#FUNDEVT-' + val).prop('checked', 'checked');
		});
		$(".input-actn-fundraiser").show();
		var tolCnt = $('#Fundraisers input[name$="btnLinkId"]:checkbox').length;
		var chkCnt = $('#Fundraisers input[name$="btnLinkId"]:checkbox:checked').length;
		if (tolCnt == chkCnt) {

			$('#fundraChkAll').prop('checked', 'checked');
		}

		else {
			$('#fundraChkAll').removeAttr('checked');
		}

		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	} else {

		$("#btnLinkId").val("");
		$(".cmnList").hide();
		$("#" + selFuncVal).hide();
	}
	
	var loginUserType = document.screenSettingsForm.userType.value;
	
	if(loginUserType == "RegionApp") {
		$('#Cities input[name="citiId"]').prop(
				'checked', false);
		var hiddenCitiId = $(obj).attr("citiId");
		var arr = hiddenCitiId.split(',');

		jQuery.each(arr, function(i, val) {
			$('#CITY-' + val).prop('checked', 'checked');
		});
		
		$(".cityPnl").show();
		var tolCnt = $("#Cities input[type=checkbox][name='citiId']").length;
		var chkCnt = $('#Cities input[name$="citiId"]:checked').length;
		if (tolCnt == chkCnt) {
			$('#citychkAll').prop('checked', 'checked');
		}
		else {
			$('#citychkAll').removeAttr('checked');
		}
	}
	else {
		$(".cityPnl").hide();
	}

}

function editTwoColmTab(obj, menuType) {

	if (document.getElementById('menuBtnName.errors') != null) {
		document.getElementById('menuBtnName.errors').style.display = 'none';
	}
	if (document.getElementById('menuFucntionality.errors') != null) {
		document.getElementById('menuFucntionality.errors').style.display = 'none';
	}
	if (document.getElementById('subMenuName.errors') != null) {
		document.getElementById('subMenuName.errors').style.display = 'none';
	}
	if (document.getElementById('screenSettingsForm.errors') != null) {
		document.getElementById('screenSettingsForm.errors').style.display = 'none';
	}
	if (document.getElementById('logoImageName.errors') != null) {
		document.getElementById('logoImageName.errors').style.display = 'none';
	}
	if (document.getElementById('btnDept.errors') != null) {
		document.getElementById('btnDept.errors').style.display = 'none';
	}
	if (document.getElementById('btnType.errors') != null) {
		document.getElementById('btnType.errors').style.display = 'none';
	}

	var edtNm = $(obj).text();
	var iconid = $(obj).attr("iconid");
	var iconImageName = $(obj).attr("iconimgname");
	// var banImageName = $(obj).attr("bannerImgName");
	var edtActn = $(obj).attr("datactn");
	var appSiteId = $(obj).attr("appSiteId");
	var templcls = $(obj).parents('ul').attr("class").split(" ")[0];
	var grpName = $("#btnGroup").val();
	var imgSrc = $(obj).find('img').attr("src");
	var btnLinkId = $(obj).attr("linkId");
	var subCat = $(obj).attr("subCat");

	if (menuType == 'twotabtemplate') {
		$("#iconincTmptImg").attr("src", iconImageName);
	} else {
	}
	$("#delBtn").show();
	$("#addBtn").attr("value", "Save Button");
	$("." + templcls + " li.tabs a").removeClass("active");
	$(obj).addClass("active");
	$("." + templcls + " li.tabs").removeClass("active");
	$(obj).parents('li.tabs').addClass("active");
	$("#dynData #btnName").val($.trim(edtNm));
	$("#menuIconId").val(iconid);
	// $("#logoImageName").val(banImageName);
	$("#hiddenmenuFnctn").val(edtActn);
	$(".intput-actn").hide();
	$(".input-actn-evnt").hide();
	$(".input-actn-fundraiser").hide();
	$("input[type=checkbox][name='btnLinkId']").prop("checked", false);
	$("#findchkAll").prop("checked", false);

	var btnDept = $(obj).attr("btnDept");
	var btnType = $(obj).attr("btnType");

	$("#dataFnctn option[value='" + edtActn + "']")
			.prop("selected", "selected");

	if ($("#grpDept").is(':checked') && btnDept != "") {

		$("#slctDept option[value='" + btnDept + "']").prop("selected",
				"selected");
	} else {
		$("#slctDept option[value='0']").prop("selected", "selected");
	}

	if ($("#grpType").is(':checked') && btnType != "") {

		$("#slctType option[value='" + btnType + "']").prop("selected",
				"selected");

	} else {
		$("#slctType option[value='0']").prop("selected", "selected");

	}

	$(".cmnList").hide();
	$("#" + edtActn).show();

	var radios = $('input:radio[name=btnLinkId]');

	radios.filter('[value=' + appSiteId + ']').prop('checked', true);

	selectedObj = obj;
	var selFuncVal = $("#dataFnctn option:selected").attr('typeVal');

	if (selFuncVal == "SubMenu") {

		$('#SM-' + btnLinkId).prop('checked', 'checked');
		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	} else if (selFuncVal == "AnythingPage") {

		$('#AP-' + btnLinkId).prop('checked', 'checked');
		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	} else if (selFuncVal == "AppSite") {

		$('#AS-' + btnLinkId).prop('checked', 'checked');
		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	} else if (selFuncVal == "Find") {

		$('#Find input[name$="btnLinkId"]:checkbox').prop('checked', false);
		var arr = btnLinkId.split(',');

		jQuery.each(arr, function(i, val) {
			if (jQuery(val).index("MC") === -1) {
				val = val.substring(0, val.lastIndexOf("-"));

				$('#FN-' + val).prop('checked', 'checked');
				$('#FN-' + val).parent().next('.sub-ctgry').show();
			}

		});

		// for subcategories NULL!~~!86,87,88,89,90,91!~~!NULL
		if (subCat != null) {

			subCat = subCat.replace(/NULL!~~!/gi, "");
			subCat = subCat.replace(/!~~!NULL/gi, "");
			var arr2 = subCat.split(',');
			jQuery.each(arr2, function(i, val) {

				//	if (null != val && val != 'NULL') {
				$('#FNS-' + val).prop('checked', 'checked');
				//	}

			});
		}
		$(".intput-actn").show();
		$(".cmnList").hide();
		$("#" + selFuncVal).show();
		
		var tolCnt = $('#Find input[name$="btnLinkId"]:checkbox:visible').length;
		var chkCnt = $('#Find input[name$="btnLinkId"]:checkbox:checked:visible').length;
		if (tolCnt == chkCnt)
			$('#findchkAll').prop('checked', 'checked');
		else
			$('#findchkAll').removeAttr('checked');

	} else if (selFuncVal == "Events") {

		$('#Events input[name$="btnLinkId"]:checkbox').prop('checked', false);
		var arr = btnLinkId.split(',');
		jQuery.each(arr, function(i, val) {
			$('#EVT-' + val).prop('checked', 'checked');
		});
		$(".input-actn-evnt").show();
		var tolCnt = $('#Events input[name$="btnLinkId"]:checkbox').length;
		var chkCnt = $('#Events input[name$="btnLinkId"]:checkbox:checked').length;
		if (tolCnt == chkCnt) {

			$('#evntchkAll').prop('checked', 'checked');
		}

		else {
			$('#evntchkAll').removeAttr('checked');
		}

		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	}else if (selFuncVal == "Fundraisers") {

		$('#Fundraisers input[name$="btnLinkId"]:checkbox').prop('checked', false);
		var arr = btnLinkId.split(',');
		jQuery.each(arr, function(i, val) {
			$('#FUNDEVT-' + val).prop('checked', 'checked');
		});
		$(".input-actn-fundraiser").show();
		var tolCnt = $('#Fundraisers input[name$="btnLinkId"]:checkbox').length;
		var chkCnt = $('#Fundraisers input[name$="btnLinkId"]:checkbox:checked').length;
		if (tolCnt == chkCnt) {

			$('#fundraChkAll').prop('checked', 'checked');
		}

		else {
			$('#fundraChkAll').removeAttr('checked');
		}

		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	} else {
		$("#btnLinkId").val("");
		$(".cmnList").hide();
		$("#" + selFuncVal).hide();
	}	
	var loginUserType = document.screenSettingsForm.userType.value;
	
	if(loginUserType == "RegionApp") {
		$('#Cities input[name="citiId"]').prop(
				'checked', false);
		var hiddenCitiId = $(obj).attr("citiId");
		var arr = hiddenCitiId.split(',');

		jQuery.each(arr, function(i, val) {
			$('#CITY-' + val).prop('checked', 'checked');
		});
		
		$(".cityPnl").show();
		var tolCnt = $("#Cities input[type=checkbox][name='citiId']").length;
		var chkCnt = $('#Cities input[name$="citiId"]:checked').length;
		if (tolCnt == chkCnt) {
			$('#citychkAll').prop('checked', 'checked');
		}
		else {
			$('#citychkAll').removeAttr('checked');
		}
	}
	else {
		$(".cityPnl").hide();
	}

}

function editTabBtn(obj) {

	if (document.getElementById('screenSettingsForm.errors') != null) {
		document.getElementById('screenSettingsForm.errors').style.display = 'none';
	}
	if (document.getElementById('menuFucntionality.errors') != null) {
		document.getElementById('menuFucntionality.errors').style.display = 'none';
	}
	if (document.getElementById('logoImageName.errors') != null) {
		document.getElementById('logoImageName.errors').style.display = 'none';
	}

	var edtNm = $(obj).text();
	var iconid = $(obj).attr("iconid");
	var menuIconId = $(obj).attr("menuIconId");
	var bottomBtnId = $(obj).attr("tabbarId");
	var iconImageName = $(obj).attr("iconimgname");
	var iconImageNameOff = $(obj).attr("iconimgnameoff");
	var edtActn = $(obj).attr("datactn");
	var imgSrc = $(obj).find('img').attr("src");
	var imgSrcOff = $(obj).attr("imagePathOff");
	$("#addBtn").attr("value", "Save Tab");
	// for editing share functionality.
	var funcType = $(obj).attr("functype");
	var itunesLnk = $(obj).attr("itunesLnk");
	var playStoreLnk = $(obj).attr("playStoreLnk");
	var btnLinkId = $(obj).attr("linkId");
	$("input[type=checkbox][name='btnLinkId']").prop("checked", false);
var subCat = $(obj).attr("subCat");

	if (iconid == "") {
		$("#upldOwn").prop("checked", "true")
		$("#iconSelection").val("upldOwn");
		$("#logoImageId").attr("src", imgSrc);
		$("#logoImageName").val(iconImageName);
		$("#imageFileId").attr("src", imgSrcOff);
		$("#bannerImageName").val(iconImageNameOff);
		$(".cmnUpld").hide();
		$(".upldOwn").show();

	} else {
		$("#exstngIcon").prop("checked", "true")
		$("#imagePath").val(imgSrc);
		$("#logoImageId").attr("src", "images/uploadIconSqr.png");
		$("#iconSelection").val("exstngIcon");
		$("#iconId").val(iconid);

		var btnType = '${sessionScope.bottomBtnType}';
		if (btnType == "Default") {
			$('#tabBarLst li').removeClass("active");
			$("#" + iconid).parents('#tabBarLst li').addClass("active");
		} else {
			$("input[name=iconOnOff][value=" + iconid + "]").prop('checked',
					true);
			// $("#" + iconid).attr("checked", true);
		}
		$(".cmnUpld").hide();
		$(".exstngIcon").show();

	}

	$(".intput-actn").hide();
	$(".input-actn-evnt").hide();
	$(".input-actn-fundraiser").hide();
	$("#bottomBtnId").val(bottomBtnId);
	$("#hiddenmenuFnctn").val(edtActn);
	$("#menuIconId").val(menuIconId);
	$("#dataFnctn option[value='" + edtActn + "']")
			.prop("selected", "selected");
	if (funcType == "Share") {
		$("#iTunesLnk").val(itunesLnk);
		$("#functionalityType").val(funcType);
		$("#playStoreLnk").val(playStoreLnk);
		$("tr.shareInfo").show();
	} else {
		$("tr.shareInfo").hide();
	}
	var selFuncVal = $("#dataFnctn option:selected").attr('typeVal');
	document.screenSettingsForm.functionalityType.value = selFuncVal;
	if (selFuncVal == "SubMenu") {
		$('#SM-' + btnLinkId).prop('checked', 'checked');
		$(".cmnList").hide();
		$("#" + selFuncVal).show();
	} else if (selFuncVal == "AnythingPage") {
		$('#AP-' + btnLinkId).prop('checked', 'checked');
		$(".cmnList").hide();
		$("#" + selFuncVal).show();
	} else if (selFuncVal == "AppSite") {
		$('#AS-' + btnLinkId).prop('checked', 'checked');
		$(".cmnList").hide();
		$("#" + selFuncVal).show();
	} else if (selFuncVal == "Find") {

	$('#Find input[name$="btnLinkId"]:checkbox').prop('checked', false);
		var arr = btnLinkId.split(',');

		jQuery.each(arr, function(i, val) {
			if (jQuery(val).index("MC") === -1) {
				val = val.substring(0, val.lastIndexOf("-"));
				
				$('#FN-' + val).prop('checked', 'checked');
				$('#FN-' + val).parent().next('.sub-ctgry').show();
			}

		});

		// for subcategories NULL!~~!86,87,88,89,90,91!~~!NULL
		if (subCat != null) {
	
			subCat = subCat.replace(/NULL!~~!/gi, "");
			subCat = subCat.replace(/!~~!NULL/gi, "");
			var arr2 = subCat.split(',');
			
			jQuery.each(arr2, function(i, val) {

				//	if (null != val && val != 'NULL') {
				$('#FNS-' + val).prop('checked', 'checked');
				//	}

			});
		}
		$(".intput-actn").show();
		$(".cmnList").hide();
		$("#" + selFuncVal).show();
		
		var tolCnt = $('#Find input[name$="btnLinkId"]:checkbox:visible').length;
		var chkCnt = $('#Find input[name$="btnLinkId"]:checkbox:checked:visible').length;
		
		if (tolCnt == chkCnt)
			$('#findchkAll').prop('checked', 'checked');
		else
			$('#findchkAll').removeAttr('checked');
	
	
	
	
		/*$('#Find input[name$="btnLinkId"]:checkbox').prop('checked', false);
		var arr = btnLinkId.split(',');
		jQuery.each(arr, function(i, val) {
			$('#FN-' + val).prop('checked', 'checked');
		});
		$(".intput-actn").show();
		var tolCnt = $('#Find input[name$="btnLinkId"]:checkbox:visible').length;
		var chkCnt = $('#Find input[name$="btnLinkId"]:checkbox:checked:visible').length;

		if (tolCnt == chkCnt) {

			$('#findchkAll').prop('checked', 'checked');
		}

		else {
			$('#findchkAll').removeAttr('checked');
		}

		$(".cmnList").hide();
		$("#" + selFuncVal).show(); */

	} else if (selFuncVal == "Events") {

		$('#Events input[name$="btnLinkId"]:checkbox').prop('checked', false);
		var arr = btnLinkId.split(',');
		jQuery.each(arr, function(i, val) {
			$('#EVT-' + val).prop('checked', 'checked');
		});
		$(".input-actn-evnt").show();
		var tolCnt = $('#Events input[name$="btnLinkId"]:checkbox').length;
		var chkCnt = $('#Events input[name$="btnLinkId"]:checkbox:checked').length;
		if (tolCnt == chkCnt) {

			$('#evntchkAll').prop('checked', 'checked');
		}

		else {
			$('#evntchkAll').removeAttr('checked');
		}

		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	} else if (selFuncVal == "Fundraisers") {
		
		$('#Fundraisers input[name$="btnLinkId"]:checkbox').prop('checked', false);
		var arr = btnLinkId.split(',');
		jQuery.each(arr, function(i, val) {
			$('#FUNDEVT-' + val).prop('checked', 'checked');
		});
		$(".input-actn-fundraiser").show();
		var tolCnt = $('#Fundraisers input[name$="btnLinkId"]:checkbox').length;
		var chkCnt = $('#Fundraisers input[name$="btnLinkId"]:checkbox:checked').length;
		if (tolCnt == chkCnt) {

			$('#fundraChkAll').prop('checked', 'checked');
		}

		else {
			$('#fundraChkAll').removeAttr('checked');
		}

		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	}else {

		$("#btnLinkId").val("");
		$(".cmnList").hide();
		$("#" + selFuncVal).hide();
	}
}
function tglGrp(obj) {
	// $(".btnNm").show();
	var getCls = $(obj).attr("class");
	$(".show-" + getCls).show();
}
function appendGrp(obj) {

	var isExist = false;
	var inputObj = $(obj).parents('ul').find('input:text'), inputVal = inputObj
			.val(), inputNm = $(obj).parents('ul').attr("class");
	inputVal = inputObj.val();
	if (!inputVal) {
		alert("Please Enter " + inputNm + " Name");
		return false;
	} else {

		var grpType;

		if (inputNm == 'Department') {

			grpType = document.getElementById("slctDept");
		} else if (inputNm == 'Type') {
			grpType = document.getElementById("slctType");
		} else if (inputNm == 'Group') {
			grpType = document.getElementById("groupName");

		}

		for (i = 0; i < grpType.length; i++) {

			if (grpType.options[i].value == inputVal) {
				isExist = true;
				grpType.options[i].selected = true;
				alert(inputNm + " Name Already Exist")
				break;
			}
		}

		if (isExist == false) {
			$(obj).parents('tr').prev('tr').find(".dynmOptn option:last")
					.after(
							"<option selected='selected'>" + inputVal
									+ "</option>");
			inputObj[0].value = '';
			$(obj).parents('tr').hide();
		}

	}
}

/*
 * Method for editing Grouped menu template
 * 
 */
function editGroupedMenuItem(obj) {

	if (document.getElementById('btnGroup.errors') != null) {
		document.getElementById('btnGroup.errors').style.display = 'none';
	}

	if (document.getElementById('menuBtnName.errors') != null) {
		document.getElementById('menuBtnName.errors').style.display = 'none';
	}
	if (document.getElementById('menuFucntionality.errors') != null) {
		document.getElementById('menuFucntionality.errors').style.display = 'none';
	}
	if (document.getElementById('subMenuName.errors') != null) {
		document.getElementById('subMenuName.errors').style.display = 'none';
	}
	if (document.getElementById('screenSettingsForm.errors') != null) {
		document.getElementById('screenSettingsForm.errors').style.display = 'none';
	}
	if (document.getElementById('btnDept.errors') != null) {
		document.getElementById('btnDept.errors').style.display = 'none';
	}
	if (document.getElementById('btnType.errors') != null) {
		document.getElementById('btnType.errors').style.display = 'none';
	}

	$("#grpDetails").hide();
	$("#tabDetails").show();

	var edtNm = $(obj).text();
	var iconid = $(obj).attr("iconid");
	var iconImageName = $(obj).attr("iconimgname");
	var edtActn = $(obj).attr("datactn");
	var templcls = $(obj).parents('ul').attr("class").split(" ")[0];
	var grpName = $("#btnGroup").val();
	var imgSrc = $(obj).find('img').attr("src");
	var btnLinkId = $(obj).attr("linkId");
	var grpName = $(obj).attr("grpName");
	// for subcategories
	var subCat = $(obj).attr("subCat");

	$("#delBtn").show();
	$("#addBtn").attr("value", "Save Button");
	$("." + templcls + " li.tabs a").removeClass("active");
	$(obj).addClass("active");
	$("." + templcls + " li.tabs").removeClass("active");
	$(obj).parents('li.tabs').addClass("active");
	$("#dynData #btnName").val($.trim(edtNm));
	$("#menuIconId").val(iconid);
	$("#logoImageName").val(iconImageName);
	$("#hiddenmenuFnctn").val(edtActn);
	$("#menuFucntionality").val(edtActn);
	//Added to add image in grouped tab template
	$("#logoImage").attr('src', imgSrc);

	$("#dataFnctn option[value='" + edtActn + "']")
			.prop("selected", "selected");

	$("#groupName option[value='" + grpName + "']")
			.prop("selected", "selected");

	$("#oldGroupName").val(grpName);
	$(".intput-actn").hide();
	$(".input-actn-evnt").hide();
	$(".input-actn-fundraiser").hide();
	$("input[type=checkbox][name='btnLinkId']").prop("checked", false);
	$("#findchkAll").prop("checked", false);

	var btnDept = $(obj).attr("btnDept");
	var btnType = $(obj).attr("btnType");

	$("#dataFnctn option[value='" + edtActn + "']")
			.prop("selected", "selected");

	if ($("#grpDept").is(':checked') && btnDept != "") {
		$("#slctDept option[value='" + btnDept + "']").prop("selected",
				"selected");
	} else {
		$("#slctDept option[value='0']").prop("selected", "selected");
	}

	if ($("#grpType").is(':checked') && btnType != "") {
		$("#slctType option[value='" + btnType + "']").prop("selected",
				"selected");

	} else {
		$("#slctType option[value='0']").prop("selected", "selected");

	}
	selectedObj = obj;
	var selFuncVal = $("#dataFnctn option:selected").attr('typeVal');
	if (selFuncVal == "SubMenu") {

		$('#SM-' + btnLinkId).prop('checked', 'checked');
		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	} else if (selFuncVal == "AnythingPage") {

		$('#AP-' + btnLinkId).prop('checked', 'checked');
		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	} else if (selFuncVal == "AppSite") {

		$('#AS-' + btnLinkId).prop('checked', 'checked');
		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	} else if (selFuncVal == "Find") {

		$('#Find input[name$="btnLinkId"]:checkbox').prop('checked', false);
		var arr = btnLinkId.split(',');

		jQuery.each(arr, function(i, val) {
			if (jQuery(val).index("MC") === -1) {
				val = val.substring(0, val.lastIndexOf("-"));

				$('#FN-' + val).prop('checked', 'checked');
				$('#FN-' + val).parent().next('.sub-ctgry').show();
			}

		});

		// for subcategories NULL!~~!86,87,88,89,90,91!~~!NULL
		if (subCat != null) {

			subCat = subCat.replace(/NULL!~~!/gi, "");
			subCat = subCat.replace(/!~~!NULL/gi, "");
			var arr2 = subCat.split(',');
			jQuery.each(arr2, function(i, val) {

				if (null != val && val != 'NULL') {
					$('#FNS-' + val).prop('checked', 'checked');
				}

			});
		}
		$(".intput-actn").show();
		$(".cmnList").hide();
		$("#" + selFuncVal).show();
		
		var tolCnt = $('#Find input[name$="btnLinkId"]:checkbox:visible').length;
		var chkCnt = $('#Find input[name$="btnLinkId"]:checkbox:checked:visible').length;
		if (tolCnt == chkCnt)
			$('#findchkAll').prop('checked', 'checked');
		else
			$('#findchkAll').removeAttr('checked');
	} else if (selFuncVal == "Events") {

		$('#Events input[name$="btnLinkId"]:checkbox').prop('checked', false);
		var arr = btnLinkId.split(',');
		jQuery.each(arr, function(i, val) {
			$('#EVT-' + val).prop('checked', 'checked');
		});
		$(".input-actn-evnt").show();
		var tolCnt = $('#Events input[name$="btnLinkId"]:checkbox').length;
		var chkCnt = $('#Events input[name$="btnLinkId"]:checkbox:checked').length;
		if (tolCnt == chkCnt) {

			$('#evntchkAll').prop('checked', 'checked');
		}

		else {
			$('#evntchkAll').removeAttr('checked');
		}

		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	} else if (selFuncVal == "Fundraisers") {

		$('#Fundraisers input[name$="btnLinkId"]:checkbox').prop('checked', false);
		var arr = btnLinkId.split(',');
		jQuery.each(arr, function(i, val) {
			$('#FUNDEVT-' + val).prop('checked', 'checked');
		});
		$(".input-actn-fundraiser").show();
		var tolCnt = $('#Fundraisers input[name$="btnLinkId"]:checkbox').length;
		var chkCnt = $('#Fundraisers input[name$="btnLinkId"]:checkbox:checked').length;
		if (tolCnt == chkCnt) {

			$('#fundraChkAll').prop('checked', 'checked');
		}

		else {
			$('#fundraChkAll').removeAttr('checked');
		}

		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	} else {

		$("#btnLinkId").val("");
		$(".cmnList").hide();
		$("#" + selFuncVal).hide();
	}
	
	var loginUserType = document.screenSettingsForm.userType.value;
	
	if(loginUserType == "RegionApp") {
		$('#Cities input[name="citiId"]').prop(
				'checked', false);
		var hiddenCitiId = $(obj).attr("citiId");
		var arr = hiddenCitiId.split(',');

		jQuery.each(arr, function(i, val) {
			$('#CITY-' + val).prop('checked', 'checked');
		});
		
		$(".cityPnl").show();
		var tolCnt = $("#Cities input[type=checkbox][name='citiId']").length;
		var chkCnt = $('#Cities input[name$="citiId"]:checked').length;
		if (tolCnt == chkCnt) {
			$('#citychkAll').prop('checked', 'checked');
		}
		else {
			$('#citychkAll').removeAttr('checked');
		}
	}
	else {
		$(".cityPnl").hide();
	}

}

function editGrpName(obj) {
	var hdrtxt = $(obj).text();
	var nhdrtxt = $.trim(hdrtxt);
	var iconid = $(obj).attr("iconid");
	$("#btnGroupTxt").val(nhdrtxt);
	$("#menuIconId").val(iconid);
	$("#btnGroup").val(nhdrtxt);
	$("#grpDetails").show();
	$("#tabDetails").hide();
}

// Code for creating appsite.
function addAppsite(obj) {
	var prnt = $(obj).parents('div#dynData');
	var bodyHt = $('body').height();
	var setHt = parseInt(bodyHt - 108);
	$(prnt).addClass("relative");

	if ($(".popupPnl").length == 0) {
		$("#wrpr")
				.append(
						"<div class='popupPnl'><div id='popup-wrp'><span class='closeBtn' title='Close'>"
								+ "</span>	<table class='cmnTbl' width='100%'><tr><td colspan='2' align='center'"
								+ " class='popHdr'>Add New AppSite</td></tr><tr><td width='33%'>"
								+ "<label class='mand'>AppSite Name</label> </span> <span	class='clear'></span> </td>"
								+ "<td width='67%'><div class='cntrl-grp'><input type='text' name='appSiteName' class='inputTxtBig req' id='appSiteName'/>"
								+ "</div><i>Please Enter AppSite Name</i></td></tr>"
								+ "<tr><td width='33%'><label class='mand'>Retailer Name</label></td><td width='67%'>"
								+ "<div class='cntrl-grp'>"
								+ "<input type='text' id='retName'  class='loadingInput req' onkeypress='retNameAutocomplete(retName);' ></div>"
								+ "<i>Please Enter Retailer Name</i></td></tr><tr><td width='33%'>Locations</td><td width='67%'><div class='cntrl-grp zeroBrdr'><select  class='slctBx req' id='address' name='address'><label class='mand'><option value=''>Select Retail Location</option></label></select>"
								+ "</div><i>Please Select Retailer Location</i></td></tr><tr><td></td><td><input type='submit' name='button' value='Add AppSite' class='btn-blue' id='add App'"
								+ "onclick='validatePopup();' />"
								+ "</td></tr></table></div></div>");
	}
	$(".popupPnl").height(setHt);
	$("#addApp").click(function() {
		$(".popupPnl").remove();
	});
	$(".closeBtn").click(function() {
		$(".popupPnl").remove();
	});
}

// for fetching retailer name auto complete
function retNameAutocomplete(retaName) {
	$('#address').find('option:not(:first)').remove();
	// alert('hi');
	$("#retName").autocomplete({
		minLength : 3,
		delay : 200,
		source : '/HubCiti/displayretnames.htm',
		select : function(event, ui) {

			if (ui.item.value == "No Records Found") {
				$("#retName").val("");
			} else {

				$("#retName").val(ui.item.rname);
				getRetailerLocs(ui.item.retId);

			}
			return false;
		}

	});
}

// for fetching retailer locations based on retailer name...
function getRetailerLocs(retId) {
	$('#address').empty();

	$.ajaxSetup({
		cache : false
	});

	$.ajax({
		type : "GET",
		url : "displayretLoc.htm",
		data : {
			'retId' : retId

		},

		success : function(response) {

			var rLocations = response;

			var objs = JSON.parse(rLocations);

			slctbox = document.getElementById('address');
			$('#address').find('option:not(:first)').remove();
			// slctbox.options.remove();
			if (objs != null && objs != 'undefined') {
				for ( var i = 0; i < objs.length; i++) {

					var opt = document.createElement('option');
					slctbox.options.add(new Option(objs[i].address,
							objs[i].retLocId));
				}

			}
		},
		error : function(e) {
			alert('Error occured while fetching retailer locations');
		}
	});

}

// for saving appsite details...
// for saving appsite details...
function saveAppsite() {
	var retLocId = document.getElementById('address').value;
	var retAdd = $('#address option:selected').text();
	var apSite = document.getElementById('appSiteName').value;
	var retName = document.getElementById('appSiteName').value;
	var viewName = document.getElementById('viewName').value;
	
	$.ajaxSetup({
		cache : false
	});

	$.ajax({
		type : "GET",
		url : "saveappsite.htm",
		data : {
			'sitename' : apSite,
			'retlocid' : retLocId,

		},

		success : function(response) {

			var splitRes = response.split(",");
			if (splitRes[0] == 'SUCCESS') {
				
				if(viewName === "addupdatefundraiserevent") {
					$('#appsiteIDs').append('<option value=' + splitRes[1] + ' selected="selected">' + retName + '</option>');
				} else {

					$(
							"<li><span class='cell'><input type='radio' value="
									+ splitRes[1] + " name='btnLinkId' id="
									+ splitRes[1] + "/>" + retName
									+ "</span> <span class='cell'>" + retAdd
									+ "</span></li>").insertBefore(
							"#AppSite li.actn");
				}

			} else {
				alert(splitRes);

			}

			document.getElementById('appSiteName').value = "";

		},
		error : function(e) {
			alert('Error occured while creating appsite');
		}
	});
	$(".popupPnl").remove();

}

function validatePopup() {

	$('.req').each(function() {
		var noVal = $(this).val().length === 0;

		if (noVal) {
			$(this).parent('.cntrl-grp').next('i').addClass('errDsply');

		}
		if (!noVal) {
			$(this).parent('.cntrl-grp').next('i').removeClass('errDsply');

		}
	});
	if ($('.errDsply').length === 0) {
		saveAppsite();
	}
}

/* Limit character length on keydown */
(function($) {
	$.fn.extend({
		limiter : function(limit, elem) {

			$(this).on("keydown focus", function() {
				setCount(this, elem);
			});
			function setCount(src, elem) {
				var chars = src.value.length;
				if (chars > limit) {
					src.value = src.value.substr(0, limit);
					chars = limit;
				}
				elem.html(limit - chars);
			}
			setCount($(this)[0], elem);
		}
	});
})(jQuery);

/* add category for alerts in ctgryLst drop down & alertTbl table */
function addCtgry() {

	var getCtgry = $("#ctgryNm").val();
	if (!getCtgry) {
		validatePopup();
		return false;
	} else {
		if ($("#ctgryLst").length) {
			var dynOptn = ("<option selected='selected'>" + getCtgry + "</option>");
			$("#ctgryLst option:last").after(dynOptn);
			closeModal();
		}
		if ($("#alertTbl").length) {
			var dynRow = $("<tr> <td>"
					+ getCtgry
					+ "</td><td><a href=# title=edit><img height=20 width=20 src=images/edit_icon.png alt=edit"
					+ "class=actn-icon></a><a href=# title=delete><img height=20 width=20 src=images/delete_icon.png alt=delete"
					+ " class=actn-icon></a></td></tr>");
			$("#alertTbl").find('tbody tr:last').after(dynRow);
			closeModal();
			$('.scrollTbl').animate({
				scrollTop : $('#alertTbl').prop("scrollHeight")
			}, 300);
		}
	}
}
function showModal() {

	var bodyHt = $('body').height();
	var setHt = parseInt(bodyHt);
	$(".modal-popupWrp").height(setHt);
	$(".modal-popupWrp").show();
	$(".modal-popup").slideDown('fast');

}
function closeModal() {
	$(".modal-popupWrp").hide();
	$(".modal-popup").slideUp();
	$(".modal-popup").find(".modal-bdy input").val("");
	$(".modal-popup i").removeClass("errDsply");
}

/** Alert category functions starts * */

function showModal(obj) {

	$('p.dupctgry').hide();
	$('i.emptyctgry').hide();
	var typeBtn = $(obj).attr("title");
	var viewName = $(obj).attr("viewName");

	var bodyHt = $('body').height();
	var setHt = parseInt(bodyHt);
	$(".modal-popupWrp").height(setHt);
	$(".modal-popupWrp").show();
	$(".modal-popup").slideDown('fast');
	if (typeBtn == "add") {
		$(".modal-popup .btn-blue").unbind('click').on('click', function() {
			addAlertCategory(viewName);
		});
	} else {
		var alrtCat = $(obj).parents('tr').find('td:first-child a').text();
		var alrtId = $(obj).parents('tr').find('td:first-child a').attr("id");
		$(".modal-hdr h3").text("Edit Category");
		$(".modal-popup .btn-blue").val("Update Category");
		$("#ctgryNm").val(alrtCat);
		$("#ctgryNm").attr("eid", alrtId);
		$(".modal-popup .btn-blue").unbind('click').on('click', function() {

			updateCategory(alrtId);
		});
	}
}

/** Alert category : Add category * */
function addAlertCategory(viewName) {

	var vCatName = document.getElementById("ctgryNm").value;
	$('p.dupctgry').hide();
	$('i.emptyctgry').hide();

	if ("" == $.trim(vCatName)) {
		$('i.emptyctgry').css("display", "block");

	} else {

		$.ajaxSetup({
			cache : false
		})
		$.ajax({
			type : "GET",
			url : "addalertcat.htm",
			data : {
				"catName" : vCatName,
			},

			success : function(response) {

				if (response != null && response != ""
						&& response != "CategoryExists") {
					$(".modal-popupWrp").hide();
					$(".modal-popup").find(".modal-bdy input").val("");
					if (viewName == 'addAlert') {
						// alert(vCatName + '&' + response)
						$('#categoryId').append(
								'<option value=' + response
										+ ' selected="selected">' + vCatName
										+ '</option>');

						// $("#categoryId option[value='" + response+
						// "']").prop("selected","selected");
						return true;
					} else {
						window.location.href = "/HubCiti/displayalertcat.htm";
					}

				} else {
					$('p.dupctgry').css("display", "block");

				}
			},
			error : function(e) {
				alert("Error occured while creating alert category");

			}

		});

	}

}
/** Alert category : clear category name * */
function clearCategory() {
	document.getElementById("ctgryNm").value = "";
	$('p.dupctgry').hide();
	$('i.emptyctgry').hide();
}

/** Alert category : delete category * */
function deleteCategory(cateId) {
	$('p.ctgryassoci').hide();
	var msg = confirm(" You want  to Delete Category?\n ");
	if (msg) {
		$.ajaxSetup({
			cache : false
		})
		$.ajax({
			type : "GET",
			url : "deletealertcate.htm",
			data : {
				"cateId" : cateId,
			},

			success : function(response) {

				if (response == 'SUCCESS') {

					document.alertcategoryform.action = "displayalertcat.htm";
					document.alertcategoryform.method = "GET";
					document.alertcategoryform.submit();

				} else {

					$('p.ctgryassoci').css("display", "block");
				}
			},
			error : function(e) {
				alert("Error occured while creating alert category");

			}

		});

	}
}

/** Alert category : update category * */
function updateCategory(vCatId) {

	var vCatName = document.getElementById("ctgryNm").value;

	$('p.dupctgry').hide();
	$('i.emptyctgry').hide();

	if ("" == $.trim(vCatName)) {
		$('i.emptyctgry').css("display", "block");

	} else {

		$.ajaxSetup({
			cache : false
		})
		$.ajax({
			type : "GET",
			url : "updatealertcat.htm",
			data : {
				"catName" : vCatName,
				"cateId" : vCatId,
			},

			success : function(response) {

				if (response == 'SUCCESS') {

					$(".modal-popupWrp").remove();
					window.location.href = "/HubCiti/displayalertcat.htm"

				} else {
					$('p.dupctgry').css("display", "block");

				}
			},
			error : function(e) {
				alert("Error occured while creating alert category");

			}

		});
	}

}

/** Alert category : search category * */
function searchAlertCategory(event) {
	var searchKey = document.alertcategoryform.catName.value;
	$('i.emptysearh').hide();

	var keycode = (event.keyCode ? event.keyCode : event.which);

	if (keycode == 13) {

		/*
		 * if (searchKey == '') {
		 * 
		 * $('i.emptysearh').css("display", "block"); event.preventDefault(); }
		 * else {
		 */

		document.alertcategoryform.action = "displayalertcat.htm";
		document.alertcategoryform.method = "GET";
		document.alertcategoryform.submit();
		// }
	} else if (event == '') {

		/*
		 * if (searchKey == '') { $('i.emptysearh').css("display", "block");
		 * return true; }
		 */
		document.alertcategoryform.action = "displayalertcat.htm";
		document.alertcategoryform.method = "GET";
		document.alertcategoryform.submit();

	} else {
		return true;
	}
}

/** Alert category functions ends * */

/** Event category functions starts * */

function showEventModal(obj) {
	var typeBtn = $(obj).attr("title");
	var viewName = $(obj).attr("viewName");
	var bodyHt = $('body').height();
	var setHt = parseInt(bodyHt);
	$(".modal-popupWrp").height(setHt);
	$(".modal-popupWrp").show();
	$(".modal-popup").slideDown('fast');
	if (typeBtn == "Add event category") {
		$(".modal-popup .btn-blue").unbind('click').on('click', function() {
			addEventCategory(viewName);
		});
	} else {
		var alrtCat = $(obj).parents('tr').find('td:first-child a').text();
		var alrtId = $(obj).parents('tr').find('td:first-child a').attr("id");
		$(".modal-hdr h3").text("Edit Category");
		$(".modal-popup .btn-blue").val("Update Category");
		$("#ctgryNm").val(alrtCat);
		$("#ctgryNm").attr("eid", alrtId);
		$(".modal-popup .btn-blue").unbind('click').on('click', function() {

			updateEventCategory(alrtId);
		});
	}
}

/** Event category : Add category * */
function addEventCategory(viewName) {

	var vCatName = document.getElementById("ctgryNm").value;
	$('p.dupctgry').hide();
	$('i.emptyctgry').hide();

	if ("" == vCatName.trim()) {
		$('i.emptyctgry').css("display", "block");

	} else {

		$.ajaxSetup({
			cache : false
		})
		$.ajax({
			type : "GET",
			url : "addeventcat.htm",
			data : {
				"catName" : vCatName,
			},

			success : function(response) {

				if (response != null && response != ""
						&& response != "CategoryExists") {
					$(".modal-popupWrp").remove();
					if (viewName == 'addAlert') {
						window.location.href = "/HubCiti/addalerts.htm";
					} else {
						window.location.href = "/HubCiti/displayeventcate.htm";
					}

				} else {
					$('p.dupctgry').css("display", "block");

				}
			},
			error : function(e) {
				alert("Error occured while creating event category");

			}

		});

	}

}
/** Event category : delete category * */
function deleteEventCategory(cateId) {
	$('p.ctgryassoci').hide();
	var msg = confirm(" You want  to Delete Category?\n ");
	if (msg) {
		$.ajaxSetup({
			cache : false
		})
		$.ajax({
			type : "GET",
			url : "deleteeventcate.htm",
			data : {
				"cateId" : cateId,
			},

			success : function(response) {

				if (response == 'SUCCESS') {

					document.EventCategoryForm.action = "displayeventcate.htm";
					document.EventCategoryForm.method = "GET";
					document.EventCategoryForm.submit();

				} else {

					$('p.ctgryassoci').css("display", "block");
				}
			},
			error : function(e) {
				alert("Error occured while creating event category");

			}

		});

	}
}

/** Event category : update category * */
function updateEventCategory(vCatId) {

	var vCatName = document.getElementById("ctgryNm").value;
	$('p.dupctgry').hide();
	$('i.emptyctgry').hide();

	if ("" == vCatName.trim()) {
		$('i.emptyctgry').css("display", "block");

	} else {

		$.ajaxSetup({
			cache : false
		})
		$.ajax({
			type : "GET",
			url : "updateeventcat.htm",
			data : {
				"catName" : vCatName,
				"cateId" : vCatId,
			},

			success : function(response) {

				if (response == 'SUCCESS') {

					$(".modal-popupWrp").remove();
					window.location.href = "/HubCiti/displayeventcate.htm"

				} else {
					$('p.dupctgry').css("display", "block");

				}
			},
			error : function(e) {
				alert("Error occured while creating event category");

			}

		});
	}

}
/** Event category : search category * */
function searchEventCategory(event) {
	var searchKey = document.EventCategoryForm.catName.value;
	$('i.emptysearh').hide();

	var keycode = (event.keyCode ? event.keyCode : event.which);

	if (keycode == 13) {

		/*
		 * if (searchKey == '') {
		 * 
		 * $('i.emptysearh').css("display", "block"); event.preventDefault(); }
		 * else {
		 */

		document.EventCategoryForm.action = "displayeventcate.htm";
		document.EventCategoryForm.method = "GET";
		document.EventCategoryForm.submit();

	} else if (event == '') {

		/*
		 * if (searchKey == '') { $('i.emptysearh').css("display", "block");
		 * return true; }
		 */
		document.EventCategoryForm.action = "displayeventcate.htm";
		document.EventCategoryForm.method = "GET";
		document.EventCategoryForm.submit();

	} else {
		return true;
	}
}
function tglGrping(obj) {
	var chkId = $(obj).attr("id");
	var chkbxSt = $(obj).prop("checked");
	if (chkbxSt) {
		$("." + chkId).show();
	} else {
		$("." + chkId).hide();
	}

}

function edittglGrping(obj) {
	$('#grpDept,#grpType').prop('disabled', false);
	$("#editFilter").val(true);
	// $('#grpDept,#grpType').unbind('click.grpTgl');

}

/** Event : delete event * */
function deleteEvent(eventId) {
	$('p.ctgryassoci').hide();
	var msg = confirm(" You want  to Delete Event?\n ");
	if (msg) {
		$.ajaxSetup({
			cache : false
		})
		$.ajax({
			type : "GET",
			url : "deleteevent.htm",
			data : {
				"eventId" : eventId,
			},

			success : function(response) {

				if (response == 'SUCCESS') {

					document.ManageEventForm.action = "manageevents.htm";
					document.ManageEventForm.method = "GET";
					document.ManageEventForm.submit();

				} else {

					$('p.ctgryassoci').css("display", "block");
				}
			},
			error : function(e) {
				alert("Error occured while creating event category");

			}

		});

	}
}
/** Event : search event * */
function searchEvent(event) {
	var searchKey = document.ManageEventForm.eventSearchKey.value;
	$('i.emptysearh').hide();

	var keycode = (event.keyCode ? event.keyCode : event.which);

	if (keycode == 13) {
		/*
		 * if (searchKey == '') {
		 * 
		 * $('i.emptysearh').css("display", "block"); event.preventDefault(); }
		 * else {
		 */

		document.ManageEventForm.action = "manageevents.htm";
		document.ManageEventForm.method = "GET";
		document.ManageEventForm.submit();
		// }
	} else if (event == '') {

		/*
		 * if (searchKey == '') { $('i.emptysearh').css("display", "block");
		 * return true; }
		 */
		document.ManageEventForm.action = "manageevents.htm";
		document.ManageEventForm.method = "GET";
		document.ManageEventForm.submit();

	} else {
		return true;
	}
}

// Code for creating appsite.
function addAppsiteEvent(obj) {

	var prnt = $(obj).parents('div#dynData');
	var bodyHt = $('body').height();
	var setHt = parseInt(bodyHt - 108);
	$(prnt).addClass("relative");

	if ($(".popupPnl").length == 0) {
		$("#wrpr")
				.append(
						"<div class='popupPnl'><div id='popup-wrp'><span class='closeBtn' title='Close'>"
								+ "</span>	<table class='cmnTbl' width='100%'><tr><td colspan='2' align='center'"
								+ " class='popHdr'>Add New AppSite</td></tr><tr><td width='33%'>"
								+ "<label class='mand'>AppSite Name</label> </span> <span	class='clear'></span> </td>"
								+ "<td width='67%'><div class='cntrl-grp'><input type='text' name='appSiteName' class='inputTxtBig req' id='appSiteName'/>"
								+ "</div><i>Please Enter AppSite Name</i></td></tr>"
								+ "<tr><td width='33%'><label class='mand'>Retailer Name</label></td><td width='67%'>"
								+ "<div class='cntrl-grp'>"
								+ "<input type='text' id='retName'  class='loadingInput req' onkeypress='retNameAutocompleteEvent(retName);' ></div>"
								+ "<i>Please Enter Retailer Name</i></td></tr><tr><td width='33%'>Locations</td><td width='67%'><div class='cntrl-grp zeroBrdr'><select  class='slctBx req' id='retaddress' name='retaddress'><label class='mand'><option value=''>Select Retail Location</option></label></select>"
								+ "</div><i>Please Select Retailer Location</i></td></tr><tr><td></td><td><input type='submit' name='button' value='Add AppSite' class='btn-blue' id='add App'"
								+ "onclick='validateAppsitePopup();' />"
								+ "</td></tr></table></div></div>");
	}
	$(".popupPnl").height(setHt);
	$("#addApp").click(function() {
		$(".popupPnl").remove();
	});
	$(".closeBtn").click(function() {
		$(".popupPnl").remove();
	});

}
function validateAppsitePopup() {

	$('.req').each(function() {
		var noVal = $(this).val().length === 0;

		if (noVal) {
			$(this).parent('.cntrl-grp').next('i').addClass('errDsply');

		}
		if (!noVal) {
			$(this).parent('.cntrl-grp').next('i').removeClass('errDsply');

		}
	});
	if ($('.cmnTbl .errDsply').length === 0) {
		saveEventAppsite();
	}
}
// for saving appsite details...
function saveEventAppsite() {

	var retLocId = document.getElementById('retaddress').value;
	var retAdd = $('#retaddress option:selected').text();
	var apSite = document.getElementById('appSiteName').value;
	var retName = document.getElementById('appSiteName').value;

	$.ajaxSetup({
		cache : false
	});

	$
			.ajax({
				type : "GET",
				url : "saveappsite.htm",
				data : {
					'sitename' : apSite,
					'retlocid' : retLocId,

				},

				success : function(response) {

					var splitRes = response.split(",");

					if (splitRes[0] == 'SUCCESS') {

						var adrs = retAdd.split(",").reverse().join(',');
						adrs = adrs.split(",");
						var retAddr = "";
						var city, state, postalCode;

						if (adrs[0] == "" || adrs[0] == 'undefined'
								|| adrs[0] == 'null') {
							postalCode = "";

						} else {
							postalCode = adrs[0];
						}

						if (adrs[1] == "" || adrs[1] == 'undefined'
								|| adrs[1] == 'null') {
							state = "";

						} else {

							state = adrs[1];
						}
						if (adrs[2] == "" || adrs[2] == 'undefined'
								|| adrs[2] == 'null') {
							city = "";

						} else {
							city = adrs[2];
						}

						for ( var i = 3; i < adrs.length; i++) {
							retAddr = retAddr + adrs[i];
						}

						var tblRow = "<tr><td>"
								+ retName
								+ "</td><td>"
								+ retAddr
								+ "</td><td>"
								+ city
								+ "</td><td>"
								+ state
								+ "</td><td>"
								+ postalCode
								+ "</td><td align=\"center\">"
								+ "<input type=\"checkbox\" value=\""
								+ splitRes[1]
								+ "\"name=\"appsiteID\">"
								+ "<input type=\"hidden\" value=\"on\" name=\"_appsiteID\"><label for=\"checkbox\"></label>"
								+ "</td></tr>";

						$("#mngevntTbl > tbody").append(tblRow);
						$("div.bsnsLoctn").parents('tr').show();

					} else {
						alert(splitRes);

					}

					// document.getElementById('appSiteName').value = "";

				},
				error : function(e) {
					alert('Error occured while creating appsite');
				}
			});
	$(".popupPnl").remove();

	$('body,html').animate({
		scrollTop : $(".content").prop("scrollHeight")
	}, 1400);

}

// for fetching retailer locations based on retailer name...
function getRetailerLocsEvent(retId) {
	$('#address').empty();

	$.ajaxSetup({
		cache : false
	});

	$.ajax({
		type : "GET",
		url : "displayretLoc.htm",
		data : {
			'retId' : retId,

		},

		success : function(response) {

			var rLocations = response;

			var objs = JSON.parse(rLocations);

			slctbox = document.getElementById('retaddress');

			$('#retaddress').find('option:not(:first)').remove();
			// slctbox.options.remove();
			if (objs != null && objs != 'undefined') {
				for ( var i = 0; i < objs.length; i++) {

					var opt = document.createElement('option');
					slctbox.options.add(new Option(objs[i].address,
							objs[i].retLocId));
				}

			}
		},
		error : function(e) {
			alert('Error occured while fetching retailer locations');
		}
	});

}
// for fetching retailer name auto complete
function retNameAutocompleteEvent(retaName) {
	$('#retaddress').find('option:not(:first)').remove();
	// alert('hi');
	$("#retName").autocomplete({
		minLength : 3,
		delay : 200,
		source : '/HubCiti/displayretnames.htm',
		select : function(event, ui) {

			if (ui.item.value == "No Records Found") {
				$("#retName").val("");
			} else {

				$("#retName").val(ui.item.rname);

				getRetailerLocsEvent(ui.item.retId);

			}
			return false;
		}

	});
}
/* Retailer Location clear state,city,postal code */
function clearValues(obj) {

	var getPrntele = $(obj).parents('tr').find('td div.cntrl-grp');
	var eleId = $(obj).attr("class").split(' ').slice(-1);
	var inputType = $(getPrntele).attr("class").split(' ').slice(-1);
	if (inputType == "input-slct") {
		$(getPrntele).find("#" + eleId + " option:eq(0)")
				.prop('selected', true);
		if (eleId == "rtlr-state") {
			$("#rtlr-city option:eq(0)").prop('selected', true);
			$("select.multiSlct option").remove();
		}
	}
	if (eleId == "rtlr-city") {
		$("#rtlr-city option:eq(0)").prop('selected', true);
		$("select.multiSlct option").remove();
	}
	if (eleId == "rtlr-srch") {
		$(getPrntele).find("#" + eleId + "").val("");
	}
	if (eleId == "multi-slct") {
		// $(getPrntele).find("select.multiSlct
		// option:selected").removeAttr("selected");
		$("#multi-slct option:selected").removeAttr("selected");
	}
}

(function($) {
	// jQuery plugin definition
	$.fn.truncateText = function(params) {

		// merge default and user parameters
		params = $.extend({
			txtlength : 20
		}, params);
		// traverse all nodes
		this.each(function() {
			var myellipse = "...";
			var gettxt = $(this).text();
			if (gettxt.length > params.txtlength) {
				var shortText = jQuery.trim(gettxt).substring(0,
						params.txtlength).split(" ").slice(0, -1).join(" ")
						+ myellipse;
				$(this).text(shortText);
			} else {
				$(this).text(gettxt);
			}
		});
		// allow jQuery chaining
		return this;
	};
})(jQuery);

// FAQ Add Category POP-Up
function showFAQModal(obj) {
	$('p.dupctgry').hide();
	$('i.emptyctgry').hide();
	var typeBtn = $(obj).attr("title");
	var viewName = $(obj).attr("viewName");
	var bodyHt = $('body').height();
	var setHt = parseInt(bodyHt);
	$(".modal-popupWrp").height(setHt);
	$(".modal-popupWrp").show();
	$(".modal-popup").slideDown('fast');
	if (typeBtn == "Add FAQ's Category") {
		$(".modal-popup .btn-blue").unbind('click').on('click', function() {
			addFAQCategory(viewName);
		});
	} else {
		// var faqCat = $(obj).parents('tr').find('td:first-child a').text();
		var faqCat = $(obj).parents('tr').find('td:first-child').attr("id");
		// var faqId = $(obj).parents('tr').find('td:first-child a').attr("id");
		var faqId = $(obj).parents('tr').attr("id");

		$(".modal-hdr h3").text("Edit Category");
		$(".modal-popup .btn-blue").val("Update Category");
		$("#ctgryNm").val(faqCat);
		$("#ctgryNm").attr("eid", faqId);
		$(".modal-popup .btn-blue").unbind('click').on('click', function() {
			updateFAQCategory(faqId);
		});
	}
}

// FAQ Category add.
function addFAQCategory(viewName) {
	var vCatName = document.getElementById("ctgryNm").value;
	$('p.dupctgry').hide();
	$('i.emptyctgry').hide();

	if ("" == $.trim(vCatName)) {
		$('i.emptyctgry').css("display", "block");
	} else {
		$.ajaxSetup({
			cache : false
		})
		$.ajax({
			type : "GET",
			url : "addupdatefaqcat.htm",
			data : {
				"catName" : vCatName,
				"cateId" : ""
			},
			success : function(response) {
				if (response != null && response != ""
						&& response != "CategoryExists") {
					$(".modal-popupWrp").hide();
					$(".modal-popup").find(".modal-bdy input").val("");
					if (viewName == 'addFAQ') {
						$('#faqCatId').append(
								'<option value=' + response
										+ ' selected="selected">' + vCatName
										+ '</option>');
						return true;
					} else {
						$(".modal-popupWrp").hide();
						alert("Category Created Successfully");
						window.location.href = "/HubCiti/displayfaqcat.htm";
					}

				} else {
					$('p.dupctgry').css("display", "block");

				}
			},
			error : function(e) {
				alert("Error occured while creating FAQ category");
			}
		});
	}
}

// Below function is used to dont allow ! and ~ operators for button name.
function specialCharCheck(evt) {

	var charCode = (evt.which) ? evt.which : event.keyCode;

	if (charCode == 126 || charCode == 33) {
		return false;
	} else {
		return true;
	}
}
/*
 * Method for editing Combo template
 * 
 */
function editComboTab(obj) {

	if (document.getElementById('btnGroup.errors') != null) {
		document.getElementById('btnGroup.errors').style.display = 'none';
	}

	if (document.getElementById('bannerImageName.errors') != null) {
		document.getElementById('bannerImageName.errors').style.display = 'none';
	}

	if (document.getElementById('menuBtnName.errors') != null) {
		document.getElementById('menuBtnName.errors').style.display = 'none';
	}
	if (document.getElementById('menuFucntionality.errors') != null) {
		document.getElementById('menuFucntionality.errors').style.display = 'none';
	}
	if (document.getElementById('subMenuName.errors') != null) {
		document.getElementById('subMenuName.errors').style.display = 'none';
	}
	if (document.getElementById('screenSettingsForm.errors') != null) {
		document.getElementById('screenSettingsForm.errors').style.display = 'none';
	}

	if (document.getElementById('btnDept.errors') != null) {
		document.getElementById('btnDept.errors').style.display = 'none';
	}
	if (document.getElementById('btnType.errors') != null) {
		document.getElementById('btnType.errors').style.display = 'none';
	}

	if (document.getElementById('logoImageName.errors') != null) {
		document.getElementById('logoImageName.errors').style.display = 'none';
	}
	if (document.getElementById('comboBtnType.errors') != null) {
		document.getElementById('comboBtnType.errors').style.display = 'none';
	}

	$("#grpDetails").hide();
	$("#tabDetails").show();

	var grpName = $(obj).attr("grpName");
	var grpBtnType = $(obj).attr("grpBtnType");
	var grpBtnTypeId = $(obj).attr("grpBtnTypeId");
	var edtNm = $(obj).text();
	var iconid = $(obj).attr("iconid");
	var iconImageName = $(obj).attr("iconimgname");
	var edtActn = $(obj).attr("datactn");
	var templcls = $(obj).parents('ul').attr("class").split(" ")[0];
	var imgSrc = $(obj).find('img').attr("src");
	var btnLinkId = $(obj).attr("linkId");
	var subCat = $(obj).attr("subCat");

	/*
	 * var pageTitle = $(obj).attr("pageTitle"); if(pageTitle == "Text") {
	 * pageTitle = "Group"; } if(pageTitle !== "Group"){
	 * $("#dynTab").find('li.grpHdr').removeClass("Text").addClass("Label");
	 * }else{ $("#dynTab").find('li.grpHdr').removeClass("Label") }
	 */

	$("#dataFnctn option[value='" + edtActn + "']")
			.prop("selected", "selected");

	$("#groupName option[value='" + grpName + "']")
			.prop("selected", "selected");

	$("#oldGroupName").val(grpName);

	$("#btn-type option[value='" + grpBtnTypeId + "']").prop("selected",
			"selected");

	document.screenSettingsForm.comboBtnType.value = grpBtnType;
	if (grpBtnType === "Rectangle") {
		$("#fileUpldCircle").hide();
		$("#fileUpldSquare").hide();
		$("#imageFileId").attr("src", 'images/uploadIconSqr.png');
		$("#iconincTmptImg").attr("src", 'images/uploadIcon.png');
	} else if (grpBtnType === "Square") {
		$("#fileUpldCircle").hide();
		$("#fileUpldSquare").show();
		if (imgSrc != 'images/uploadIcon.png') {
			$("#imageFileId").attr("src", imgSrc);
			$("#iconincTmptImg").attr("src", imgSrc);
		} else {
			$("#imageFileId").attr("src", 'images/uploadIconSqr.png');
		}
	} else if (grpBtnType === "Circle") {
		$("#fileUpldSquare").hide();
		$("#fileUpldCircle").show();
		if (imgSrc != 'images/uploadIcon.png') {
			$("#iconincTmptImg").attr("src", imgSrc);
			$("#imageFileId").attr("src", imgSrc);
		} else {
			$("#iconincTmptImg").attr("src", 'images/uploadIcon.png');
		}
	}
	$("#delBtn").show();
	$("#addBtn").attr("value", "Save Button");
	$("." + templcls + " li.tabs a").removeClass("active");
	$(obj).addClass("active");
	$("." + templcls + " li.tabs").removeClass("active");
	$(obj).parents('li.tabs').addClass("active");
	$("#dynData #btnName").val($.trim(edtNm));
	$("#menuIconId").val(iconid);
	$("#logoImageName").val(iconImageName);
	$("#bannerImageName").val(iconImageName);
	$("#hiddenmenuFnctn").val(edtActn);
	$("#dataFnctn option[value='" + edtActn + "']")
			.prop("selected", "selected");
	selectedObj = obj;

	var btnDept = $(obj).attr("btnDept");
	var btnType = $(obj).attr("btnType");
	$(".intput-actn").hide();
	$(".input-actn-evnt").hide();
	$(".input-actn-fundraiser").hide();
	$("input[type=checkbox][name='btnLinkId']").prop("checked", false);
	$("#findchkAll").prop("checked", false);
	$("#dataFnctn option[value='" + edtActn + "']")
			.prop("selected", "selected");

	if ($("#grpDept").is(':checked') && btnDept != "") {
		$("#slctDept option[value='" + btnDept + "']").prop("selected",
				"selected");
	} else {
		$("#slctDept option[value='0']").prop("selected", "selected");
	}

	if ($("#grpType").is(':checked') && btnType != "") {
		$("#slctType option[value='" + btnType + "']").prop("selected",
				"selected");

	} else {
		$("#slctType option[value='0']").prop("selected", "selected");

	}

	var selFuncVal = $("#dataFnctn option:selected").attr('typeVal');
	if (selFuncVal == "SubMenu") {

		$('#SM-' + btnLinkId).prop('checked', 'checked');
		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	} else if (selFuncVal == "AnythingPage") {

		$('#AP-' + btnLinkId).prop('checked', 'checked');
		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	} else if (selFuncVal == "AppSite") {

		$('#AS-' + btnLinkId).prop('checked', 'checked');
		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	} else if (selFuncVal == "Find") {

		$('#Find input[name$="btnLinkId"]:checkbox').prop('checked', false);
		var arr = btnLinkId.split(',');

		jQuery.each(arr, function(i, val) {
			if (jQuery(val).index("MC") === -1) {
				val = val.substring(0, val.lastIndexOf("-"));

				$('#FN-' + val).prop('checked', 'checked');
				$('#FN-' + val).parent().next('.sub-ctgry').show();
			}

		});

		// for subcategories NULL!~~!86,87,88,89,90,91!~~!NULL
		if (subCat != null) {

			subCat = subCat.replace(/NULL!~~!/gi, "");
			subCat = subCat.replace(/!~~!NULL/gi, "");
			var arr2 = subCat.split(',');
			jQuery.each(arr2, function(i, val) {

				if (null != val && val != 'NULL') {
					$('#FNS-' + val).prop('checked', 'checked');
				}

			});
		}
		$(".intput-actn").show();
		$(".cmnList").hide();
		$("#" + selFuncVal).show();
		
		var tolCnt = $('#Find input[name$="btnLinkId"]:checkbox:visible').length;
		var chkCnt = $('#Find input[name$="btnLinkId"]:checkbox:checked:visible').length;
		if (tolCnt == chkCnt)
			$('#findchkAll').prop('checked', 'checked');
		else
			$('#findchkAll').removeAttr('checked');

	} else if (selFuncVal == "Events") {

		$('#Events input[name$="btnLinkId"]:checkbox').prop('checked', false);
		var arr = btnLinkId.split(',');
		jQuery.each(arr, function(i, val) {
			$('#EVT-' + val).prop('checked', 'checked');
		});
		$(".input-actn-evnt").show();
		var tolCnt = $('#Events input[name$="btnLinkId"]:checkbox').length;
		var chkCnt = $('#Events input[name$="btnLinkId"]:checkbox:checked').length;
		if (tolCnt == chkCnt) {

			$('#evntchkAll').prop('checked', 'checked');
		}

		else {
			$('#evntchkAll').removeAttr('checked');
		}

		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	} else if (selFuncVal == "Fundraisers") {

		$('#Fundraisers input[name$="btnLinkId"]:checkbox').prop('checked', false);
		var arr = btnLinkId.split(',');
		jQuery.each(arr, function(i, val) {
			$('#FUNDEVT-' + val).prop('checked', 'checked');
		});
		$(".input-actn-fundraiser").show();
		var tolCnt = $('#Fundraisers input[name$="btnLinkId"]:checkbox').length;
		var chkCnt = $('#Fundraisers input[name$="btnLinkId"]:checkbox:checked').length;
		if (tolCnt == chkCnt) {

			$('#fundraChkAll').prop('checked', 'checked');
		}

		else {
			$('#fundraChkAll').removeAttr('checked');
		}

		$(".cmnList").hide();
		$("#" + selFuncVal).show();

	} else {

		$("#btnLinkId").val("");
		$(".cmnList").hide();
		$("#" + selFuncVal).hide();
	}
	
	var loginUserType = document.screenSettingsForm.userType.value;	
	if(loginUserType == "RegionApp") {
		$('#Cities input[name="citiId"]').prop(
				'checked', false);
		var hiddenCitiId = $(obj).attr("citiId");
		var arr = hiddenCitiId.split(',');

		jQuery.each(arr, function(i, val) {
			$('#CITY-' + val).prop('checked', 'checked');
		});
		
		$(".cityPnl").show();
		var tolCnt = $("#Cities input[type=checkbox][name='citiId']").length;
		var chkCnt = $('#Cities input[name$="citiId"]:checked').length;
		if (tolCnt == chkCnt) {
			$('#citychkAll').prop('checked', 'checked');
		}
		else {
			$('#citychkAll').removeAttr('checked');
		}
	}
	else {
		$(".cityPnl").hide();
	}

}

/* START OF FIND btnLinkId- SUBCATEGORY IMPLEMENTATION */
function prntCheckAll() {
	var tolCnt = $('input[name$="btnLinkId"]:checkbox:visible').length;
	var chkCnt = $('input[name$="btnLinkId"]:checkbox:checked:visible').length;
	if (tolCnt == chkCnt)
		$('#findchkAll').prop('checked', 'checked');
	else
		$('#findchkAll').removeAttr('checked');
}

function childCheck(obj) {
	/*var childChkbx = $(obj).parents('li').find('input[name$="btnLinkId"]:checkbox:not(".main-ctrgy")').length;
	var childChkbxChkd = $(obj).parents('li').find('input[name$="btnLinkId"]:checkbox:not(".main-ctrgy"):checked').length;
	if (childChkbx == childChkbxChkd) {
		$(obj).parents('li').find('.sub-ctgry').show();
	}
	if (childChkbxChkd < 1) {
		//  $(obj).parents('li').find('.sub-ctgry').parent('li').css("height","0");
		//	$(obj).parents('li').find('.sub-ctgry').css("visibility","hidden");
		$(this).parents('.sub-ctgry').hide();
		$(obj).parents('li').find(".main-ctrgy").removeAttr('checked');
	}*/	
	//var childChkbx = $(obj).parent('span').next("ul .sub-ctgry").find('input[name$="btnLinkId"]:checkbox').length;
	
	var childChkbxChkd = $(obj).parent('span').next("ul .sub-ctgry").find('input[name$="btnLinkId"]:checkbox:checked').length;
	if(childChkbxChkd > 0) {
		$(obj).parent('span').next("ul .sub-ctgry").show();
	}else {
		$(obj).parent('span').next("ul .sub-ctgry").hide();
		$(obj).parent('li').find(".main-ctrgy").removeAttr('checked');
	}
	
}
/* END OF FIND btnLinkId- SUBCATEGORY IMPLEMENTATION */

/** Fundraiser functions starts */

function deleteFundraisingEvent(eventId) {
	$('p.ctgryassoci').hide();
	var msg = confirm(" Do you want to delete this Fundraiser event?\n ");
	if (msg) {
		$.ajaxSetup({
			cache : false
		})
		$.ajax({
			type : "GET",
			url : "deletefndrevent.htm",
			data : {
				"eventId" : eventId,
			},

			success : function(response) {
				if (response == 'SUCCESS') {
					alert("Fundraiser event deleted successfully.");
					document.ManageEventForm.action = "managefundraisers.htm";
					document.ManageEventForm.method = "GET";
					document.ManageEventForm.submit();
				} else {
					$('div.alertBx').remove();
					$('p.ctgryassoci').css("display", "block");
				}
			},
			error : function(e) {
				alert("Error occured while creating event category");

			}

		});

	}
}

function showFundraiserDeptModal(obj) {
	
	$('p.dupdept').hide();
	$('i.dupctgry').hide();

	var bodyHt = $('body').height();
	var setHt = parseInt(bodyHt);
	$(".modal-popupWrp").height(setHt);
	$(".modal-popupWrp").show();
	$(".modal-popup").slideDown('fast');
	$(".modal-popup .btn-blue").unbind('click').on('click', function() {
		addFundraiserDept();
	});
}

/** Add Fundraiser Department */
function addFundraiserDept() {

	var deptName = document.getElementById("deptNm").value;
	$('p.dupctgry').hide();
	$('i.emptydept').hide();

	if ("" == $.trim(deptName)) {
		$('i.emptydept').css("display", "block");

	} else {

		$.ajaxSetup({
			cache : false
		})
		$.ajax({
			type : "GET",
			url : "adddept.htm",
			data : {
				"deptName" : deptName,
			},
			success : function(response) {
				if (response != null && response != ""
						&& response != "DepartmentExists") {
					$(".modal-popupWrp").hide();
					$(".modal-popup").find(".modal-bdy input").val("");
					$('#departmentId').append('<option value=' + response + ' selected="selected">' + deptName + '</option>');
					return true;					
				} else {
					$('p.dupctgry').css("display", "block");	
				}
			},
			error : function(e) {
				alert("Error occured while creating fundraiser department");
			}
		});
	}
}

/** clear department name */
function clearDept() {
	document.getElementById("deptNm").value = "";
	$('p.dupctgry').hide();
	$('i.emptydept').hide();
}

/** Fundraiser functions ends */

/** Deal Of the Day changes Start */
function saveDealOfTheDay(dealId) {	
	var dealName = document.deals.dealName.value;
	var currentDealId = document.deals.dealId.value;
	$.ajaxSetup({
		cache : false
	});
	if(currentDealId != dealId) {
		$.ajax({
			type : "GET",
			url : "saveDOD.htm",
			data : {
				'dealName' : dealName,
				'dealId' : dealId
			},
	
			success : function(response) {
				document.deals.dealId.value = dealId;
				$("#deal-" + dealId).find("img").attr("src", "images/dealActive_icon.png");
				$("#deal-" + currentDealId).find("img").attr("src", "images/deal_icon.png");				
			},
			error : function(e) {
				alert('Error occured while saving deal of the day');
			}
		});
	} else {
		alert("It is already a deal of the day");
	}

}
/** Deal Of the Day changes Ends */

/* Changes to upload image for category */
/*$("input[name='tglImgUpload']").on("change",function(){
	var getoptnId = $(this).attr("id");
	if(getoptnId === "tglLogoUpload"){
		$(".tglLogoUpload").show();
		$(".tglSubMenuOptns").hide();
		$("#iphone-welcome-preview").hide();
	}
	else {
		$(".tglLogoUpload").hide();
		$(".tglSubMenuOptns").show();
		$("#iphone-welcome-preview").show();
	}
});
$("input[name='tglImgUpload']:checked").trigger('change');*/

/* End*/
