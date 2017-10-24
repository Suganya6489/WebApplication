<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<script type="text/javascript" src="scripts/jquery-1.10.2.js"></script>
<script>
	$(document).ready(function()	{
		var groupedTabWithImage = '${sessionScope.menuTemplteName}';
		if(groupedTabWithImage === 'Grouped Tab With Image')	{
			$("#dynTab").addClass("grpdImg");
		}
	});
</script>
<!--[if IE]>
<script type="text/javascript" src="scripts/jquery.corner.js"></script>
<![endif]-->


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

			<li class="last">Setup Menu</li>
		</ul>
	</div>
	<span class="blue-brdr"></span>
	<div class="content" id="login">
		<div id="menu-pnl" class="split">
			<jsp:include page="leftNavigation.jsp"></jsp:include>
		</div>
		<div class="cont-pnl split" id="equalHt">
			<div class="cont-block rt-brdr stretch">
				<div class="title-bar">
					<ul class="title-actn">
						<li class="title-icon"><span class="icon-main-menu">&nbsp;</span>
						</li>
						<li>Main Menu [ <c:out value="${sessionScope.menuTemplteName }"></c:out> ]</li>
					</ul>
				</div>
				<c:choose>


					<c:when test="${sessionScope.mainMenuDetails ne null}">
						<div class="cont-wrp">
							<div id="iphone-preview" class="brdr">
								<!--Iphone Preview For Login screen starts here-->
								<div id="iphone-priPolicy-preview">
									<div class="iphone-status-bar"></div>
									<div class="navBar iphone">
										<table width="100%" border="0" cellspacing="0" cellpadding="0" class="titleGrd">
											<tr>
												<td width="19%"><img src="images/backBtn.png" alt="back" width="50" height="30" />
												</td>
												<td width="54%" class="genTitle">
													<!--<img src="images/small-logo.png" width="86" height="35" alt="Logo" />--></td>
												<td width="27%" align="center"></td>
											</tr>
										</table>
									</div>


									<c:choose>
										<c:when test="${sessionScope.menuTemplteName eq 'Grouped Tab' || sessionScope.menuTemplteName eq 'Grouped Tab With Image'}">
											<div class="previewAreaScroll actualView">
												<ul class="singleTab grpdLst cstmBtn" id="dynTab">
													<c:forEach items="${sessionScope.mainMenuDetails.buttons }" var="item">
														<c:choose>

															<c:when test="${item.functnName eq 'Text' }">
																<li class="tabs grpHdr Group1"><c:out value="${item.btnName }"></c:out>
																</li>

															</c:when>

															<c:otherwise>

																<li class="tabs"><a> <img width="30" height="30" class="lstImg" alt="image" src="${item.btnImage}"> <span><c:out value='${item.btnName }'></c:out>
																	</span> </a>
																</li>
															</c:otherwise>

														</c:choose>
													</c:forEach>
												</ul>
											</div>
										</c:when>
										<c:when test="${sessionScope.menuTemplteName eq 'Single/Two Column Tab'}">

											<div class="previewAreaScroll actualView">
												<c:choose>
													<c:when test="${sessionScope.colBtnView eq 'singleCol'}">

														<ul id="dynTab" class="twinTab cstmBtn singleCol">
													</c:when>

													<c:otherwise>

														<ul id="dynTab" class="twinTab cstmBtn twoCol">
													</c:otherwise>

												</c:choose>

												<c:forEach items="${sessionScope.mainMenuDetails.buttons }" var="item">
													<li class="tabs"><c:choose>

															<c:when test="${item.btnAction eq sessionScope.expFctnId}">
																<a class="experience"><img class="lstImg" alt="image" src="<c:out
																value='${item.btnImage }'></c:out>"> <span><c:out value="${item.btnName }"></c:out>
																</span>
																</a>

															</c:when>
															<c:otherwise>

																<a><img width="30" height="30" class="lstImg" alt="image" src="<c:out
																value='${item.btnImage }'></c:out>"> <span><c:out value="${item.btnName }"></c:out>
																</span>
																</a>

															</c:otherwise>
														</c:choose>
													</li>

												</c:forEach>
												</ul>
											</div>
										</c:when>
										<c:when test="${sessionScope.menuTemplteName eq 'Two Column Tab with Banner Ad'}">

											<div class="previewAreaScroll actualView">
												<div id="bnrImg">
													<img id="imgView" src="${sessionScope.bannerimage}" title="Banner Ad" width="320" height="50" />
												</div>
												<ul id="dynTab" class="twinTabBnr cstmBtn">
													<c:forEach items="${sessionScope.mainMenuDetails.buttons }" var="item">
														<li class="tabs"><c:choose>

																<c:when test="${item.btnAction eq sessionScope.expFctnId}">
																	<a class="experience"><img class="lstImg" alt="image" src="<c:out
																value='${item.btnImage }'></c:out>"> <span><c:out value="${item.btnName }"></c:out>
																	</span>
																	</a>

																</c:when>
																<c:otherwise>

																	<a><img width="30" height="30" class="lstImg" alt="image" src="<c:out
																value='${item.btnImage }'></c:out>"> <span><c:out value="${item.btnName }"></c:out>
																	</span>
																	</a>

																</c:otherwise>
															</c:choose>
														</li>





													</c:forEach>
												</ul>
											</div>
										</c:when>
										<c:when test="${sessionScope.menuTemplteName eq 'List View'}">
											<div class="previewAreaScroll actualView">
												<ul class="listView" id="dynTab">
													<c:forEach items="${sessionScope.mainMenuDetails.buttons }" var="item">
														<li class="tabs"><a><img width="30" height="30" class="lstImg" alt="image" src="<c:out
																value='${item.btnImage }'></c:out>"> <span><c:out value="${item.btnName }"></c:out>
															</span>
														</a>
														</li>



													</c:forEach>
												</ul>
											</div>
										</c:when>
										<c:when test="${sessionScope.menuTemplteName eq 'Iconic Grid'}">
											<div class="previewAreaScroll actualView">

												<div id="bnrImg">
													<img id="imgView" src="${sessionScope.iconicTempbnrimgPreview}" title="Banner Ad" width="320" height="50" />
												</div>
												<ul class="gridView" id="dynTab">

													<c:forEach items="${sessionScope.mainMenuDetails.buttons }" var="item">
														<li class="tabs"><a>
																<div class="rnd">
																	<img width="60" height="60" alt="image" src="<c:out
																value='${item.btnImage }'></c:out>" />
																</div> <span><c:out value="${item.btnName }"></c:out>
															</span> </a>
														</li>



													</c:forEach>
												</ul>
											</div>
										</c:when>
										<c:when test="${sessionScope.menuTemplteName eq 'Combo Template'}">
											<div class="previewAreaScroll actualView">
												<ul class="singleTab cstmBtn" id="dynTab">
													<c:forEach items="${sessionScope.mainMenuDetails.buttons }" var="item">
														<c:choose>

															<c:when test="${item.functnName eq 'Text' ||  item.functnName eq 'Label'}">
																<li class="tabs grpHdr ${item.functnName}"><c:out value="${item.btnName }"></c:out>
																</li>

															</c:when>

															<c:otherwise>

																<li class="tabs ${item.comboBtnType}"><a class="${item.comboBtnType}"> <c:choose>
																			<c:when test="${item.comboBtnType ne 'Rectangle'}">
																				<c:choose>
																					<c:when test="${item.comboBtnType eq 'Circle'}">
																						<div class="rnd">
																							<c:choose>
																								<c:when test="${item.btnImage ne 'null' || !empty item.btnImage}">
																									<img width="60" height="60" alt="image" src="<c:out value='${item.btnImage }'></c:out>" />
																								</c:when>
																								<c:otherwise>
																									<img width="60" height="60" alt="image" src="images/uploadIcon.png" />
																								</c:otherwise>
																							</c:choose>
																						</div>
																						<span><c:out value='${item.btnName }'></c:out> </span>
																					</c:when>
																					<c:otherwise>
																						<div class="">
																							<c:choose>
																								<c:when test="${item.btnImage ne 'null' || !empty item.btnImage}">
																									<img width="30" height="30" alt="image" src="<c:out value='${item.btnImage }'></c:out>" />
																								</c:when>
																								<c:otherwise>
																									<img width="30" height="30" alt="image" src="images/dfltImg.png" />
																								</c:otherwise>
																							</c:choose>
																						</div>
																						<span><c:out value='${item.btnName }'></c:out> </span>
																					</c:otherwise>
																				</c:choose>
																			</c:when>
																			<c:otherwise>
																				<div class="">
																					<img width="50" height="50" class="lstImg" alt="image" src="<c:out value='${item.btnImage }'></c:out>">
																				</div>
																				<span><c:out value='${item.btnName }'></c:out> </span>
																			</c:otherwise>
																		</c:choose>
																</a>
																</li>
															</c:otherwise>

														</c:choose>
													</c:forEach>
												</ul>
											</div>
										</c:when>


									</c:choose>


								</div>
								<!--Iphone Preview For Login screen ends here-->
								<ul id="tab-main" class="tabbar">
									<c:forEach items="${sessionScope.menuBarButtonsList }" var="item">

										<li><img src="${item.imagePath }" width="80" height="50" alt="share" />
										</li>
									</c:forEach>


								</ul>
							</div>

						</div>
						<div class="title-bar" align="center">
							<a href="updatemainmenu.htm?menuType=mainmenu" class="btn-blue"><c:out value="Update Main Menu">
								</c:out>
							</a>
						</div>
					</c:when>
					<c:otherwise>

						<div class="title-bar" align="center">
							<a href="displaymenutemplate.htm?menuType=mainmenu" class="btn-blue"><c:out value="Add Main Menu">
								</c:out>
							</a>
						</div>
					</c:otherwise>
				</c:choose>

			</div>
			<!-- 
			<c:if test="${sessionScope.mainMenuDetails ne null}">
				<div class="cont-block">
					<div class="title-bar">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-submenu">&nbsp;</span>
							</li>
							<li>Sub Menu</li>
						</ul>
					</div>

					<div class="cont-wrp tmpltHt">
						<ul class="listCntrl relative">

							<c:forEach items="${sessionScope.subMenuList }" var="item">

								<li><a href="updatesubmenu.htm?subMenuId=${item.menuId }" title="Edit SubMenu"> <c:out value="${item.menuName }"></c:out>
								</a> <span class="actnIcon"> <a title="Edit SubMenu" class="actn-edit" href="updatesubmenu.htm?subMenuId=${item.menuId }"></a> <a title="Delete SubMenu" class="actn-del" href="#" onclick="deleteSubMenu(${item.menuId })"></a> </span></li>

							</c:forEach>

						</ul>
					</div>
					<div class="title-bar" align="center">

						<input type="submit" name="SubMnu" value="Add Sub Menu" class="btn-blue" id="addSubMnu" onclick="window.location.href='displaymenutemplate.htm?menuType=submenu'" />
					</div>
				</div>
			</c:if>
 -->


		</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(
			function() {			

				var mainMenuBGType = '${sessionScope.mainMenuBGType}';
				var mainMenuBG = '${sessionScope.mainMenuBG}';
				var mainMenuBtnClr = '${sessionScope.mainMenuBtnClr}';
				var mainMenuFntClr = '${sessionScope.mainMenuFntClr}';
				var menuTemplteName = '${sessionScope.menuTemplteName}';
				var mainMenuGrpClr = '${sessionScope.mainMenuGrpClr}';
				var mainMenuGrpFntClr = '${sessionScope.mainMenuGrpFntClr}';
				var mainMenuIconsFntClr = '${sessionScope.mainMenuIconsFntClr}';

				if(menuTemplteName == "Combo Template")
				{
					$("#dynTab").removeClass("singleTab cstmBtn").addClass("comboView");
					if ($('.actualView').hasScrollBar()) {
						$(".comboView li.tabs a.Square").removeClass("scrlOff").addClass("scrlOn");
					}
					else
					{
						$(".comboView li.tabs a.Square").removeClass("scrlOn").addClass("scrlOff");
					}
					
					$('.comboView li.tabs a.Circle span').each(function(){  
					var val = $.trim($(this).text());
					if (/\s/.test(val)) {	 		
						$(this).css("word-break", "keeep-all"); 	
					} 	
					else { 	
						$(this).css("word-break", "break-all"); 
					}					
				});
				}				

				if (mainMenuBGType == 'Image') {
					$(".previewAreaScroll").css("background",
							"url('" + mainMenuBG + "') no-repeat scroll 0 0");

				} else if (mainMenuBGType == 'Color') {
					$("#iphone-priPolicy-preview")
							.css("background", mainMenuBG);

				}
				if (menuTemplteName == 'Iconic Grid') {

					$(".gridView li.tabs a span").css("color", mainMenuIconsFntClr);
					
					$('.gridView li.tabs a span').each(function(){  
					var val = $.trim($(this).text());
					if (/\s/.test(val)) {	 		
						$(this).css("word-break", "keeep-all"); 	
					} 	
					else { 	
						$(this).css("word-break", "break-all"); 
					} 

					if ($('.actualView').hasScrollBar()) {
						$(".gridView li.tabs a").removeClass("scrlOff");
					} else {
						$(".gridView li.tabs a").addClass("scrlOff");
					}
				});
				} else if (menuTemplteName == 'Grouped Tab' || menuTemplteName == 'Grouped Tab With Image') {

					$(".singleTab li.tabs a").css("color", mainMenuFntClr);
					$(".singleTab").css("background", mainMenuBtnClr);
					$("#dynTab li.grpHdr").css("background", mainMenuGrpClr);
					$("#dynTab li.grpHdr").css("color", mainMenuGrpFntClr);
					
				} else if (menuTemplteName == 'List View') {

					$(".listView li.tabs a").css("color", mainMenuFntClr)
					$(".listView li.tabs").css("background", mainMenuBtnClr)
				} else if (menuTemplteName == 'Two Column Tab with Banner Ad'
						|| menuTemplteName == 'Single/Two Column Tab') {

					if ($('.actualView').hasScrollBar()) {

						$(".twinTab li.tabs a,.twinTabBnr li.tabs a").css(
								"width", "150px");
						$(".twinTab li.tabs a.experience,.twinTabBnr li.tabs a.experience")
								.css("width", "301px");
						$(".previewAreaScroll").css("width","319px");
						$("#iphone-preview").css("width","319px");
						
					} else {
						$(".twinTab li.tabs a,.twinTabBnr li.tabs a").css(
								"width", "159px");
						$(".twinTab li.tabs a.experience,.twinTabBnr li.tabs a.experience")
								.css("width", "320px");
						$(".previewAreaScroll").css("width","320px");
						$("#iphone-preview").css("width","320px");
					}

					$(".twinTab li.tabs a, .twinTabBnr li.tabs a").css("color",
							mainMenuFntClr)
					$(".twinTab li.tabs a, .twinTabBnr li.tabs a").css(
							"background", mainMenuBtnClr)
				}
				else if (menuTemplteName == "Combo Template") {
					
					$(".comboView li.tabs a").css("color", mainMenuFntClr);
					$(".comboView li.tabs.Circle a span").css("color", mainMenuIconsFntClr);
					$(".comboView li.tabs.Rectangle").css("background", mainMenuBtnClr);
					$("#dynTab li.grpHdr.Text").css("background", mainMenuGrpClr);
					$("#dynTab li.grpHdr").css("color", mainMenuGrpFntClr);

					if ($('.actualView').hasScrollBar()) {
						$(".comboView li.tabs a.Square").removeClass(
								"scrlOff").addClass("scrlOn");
						$(".comboView li.tabs a.Circle").removeClass(
						"scrlOff").addClass("scrlOn");
					} else {
						$(".comboView li.tabs a.Square").removeClass(
								"scrlOn").addClass("scrlOff");
						$(".comboView li.tabs a.Circle").removeClass(
						"scrlOn").addClass("scrlOff");
					}
				}

			});

	$(window).load(
			function() {

				if ($("#dynTab li").length > 1) {
					$("#dynTab li a.experience").parents('li').insertBefore(
							"#dynTab li.tabs:first");
				}
				//$(".rnd").corner("25px");

			});

	(function($) {
		$.fn.hasScrollBar = function() {
			return this.get(0).scrollHeight > this.height();
		}
	})(jQuery);
	
	(function($) {
		$.fn.hasScrollBar = function() {
			return this.get(0).scrollHeight > this.height();
		}
	})(jQuery);
	
</script>
<script type="text/javascript">
	configureMenu("setupmainmenu");
</script>
