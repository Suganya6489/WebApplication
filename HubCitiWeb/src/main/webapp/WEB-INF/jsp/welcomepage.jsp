<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<script type="text/javascript" src="scripts/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/jquery.form.js"></script>
<script type="text/javascript"
	src="/HubCiti/scripts/colorPickDynamic.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/colorPicker.js"></script>
<script src="/HubCiti/scripts/ckeditor/ckeditor.js"></script>
<script src="/HubCiti/scripts/ckeditor/config.js"></script>
<link rel="stylesheet" type="text/css"
	href="/HubCiti/styles/colorPicker.css" />

<div class="wrpr-cont relative">
	<div id="slideBtn">
		<a href="#" onclick="revealPanel(this);" title="Hide Menu"> <img
			src="images/slide_off.png" width="11" height="28" alt="btn_off" />
		</a>
	</div>
	<div id="bread-crumb">
		<ul>
			<li class="scrn-icon"><span class="icon-home">&nbsp;</span></li>
			<li><a href="welcome.htm">Home</a></li>
			<li class="last">DashBoard</li>
		</ul>
	</div>
	<span class="blue-brdr"></span>
	<div class="content" id="login">
		<div id="menu-pnl" class="split">
			<jsp:include page="leftNavigation.jsp"></jsp:include>
		</div>
		<div class="cont-pnl split">
			<div class="cont-block stretch">
				<div class="title-bar">
					<ul class="title-actn">
						<li class="title-icon"><span class="icon-admin">&nbsp;</span></li>
						<c:choose>
							<c:when test="${sessionScope.loginUserType eq 'RegionApp'}">
								<li>Welcome to HubRegion<sup>TM</sup> Administration</li>
							</c:when>
							<c:otherwise>
								<li>Welcome to HubCiti<sup>TM</sup> Administration</li>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>
				<span class="clear"></span>
				<div class="cont-wrp mrngBtm_small">
					<p class="pnlHeading mrngBtm_small" align="center">
						Follow these steps to finalize your setup: <span></span>
					</p>
					<ul class="prgrsInfo">
						<c:choose>
							<c:when test="${sessionScope.pageStatus.loginPage == 1}">
								<li class="stsCmpltd"><a href="setuploginscreen.htm"
									class="prgrs-login" alt="Setup Login Screen"></a> <span>1.
										Setup Login Screen</span></li>
							</c:when>
							<c:otherwise>
								<li><a href="setuploginscreen.htm" class="prgrs-login"
									alt="Setup Login Screen"></a> <span>1. Setup Login
										Screen</span></li>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${sessionScope.pageStatus.generalSettingPage == 1}">
								<li class="stsCmpltd"><a href="generealsettings.htm"
									class="prgrs-general" alt="Setup General Setting Screen"></a> <span>2.
										Setup General Setting Screen</span></li>
							</c:when>
							<c:otherwise>
								<li><a href="generealsettings.htm" class="prgrs-general"
									alt="Setup General Setting Screen"></a> <span>2. Setup
										General Setting Screen</span></li>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${sessionScope.pageStatus.welcomePage == 1}">
								<li class="stsCmpltd"><a href="setupsplashscreen.htm"
									class="prgrs-welcome" alt="Setup Welcome Screen"></a> <span>3.
										Setup Splash Screen</span></li>
							</c:when>
							<c:otherwise>
								<li><a href="setupsplashscreen.htm" class="prgrs-welcome"
									alt="Setup Welcome Screen"></a> <span>3. Setup Splash
										Screen</span></li>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${sessionScope.pageStatus.aboutUsPage == 1}">
								<li class="stsCmpltd"><a href="setupaboutusscreen.htm"
									class="prgrs-about" alt="Setup About Us Screen"></a> <span>4.
										Setup About Us Screen</span></li>
							</c:when>
							<c:otherwise>
								<li><a href="setupaboutusscreen.htm" class="prgrs-about"
									alt="Setup About Us Screen"></a> <span>4. Setup About Us
										Screen</span></li>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${sessionScope.pageStatus.registrationPage == 1}">
								<li class="stsCmpltd"><a href="setupregscreen.htm"
									class="prgrs-registration" alt="Setup Registration Screen"></a>
									<span>5. Setup Registration Screen</span></li>
							</c:when>
							<c:otherwise>
								<li><a href="setupregscreen.htm" class="prgrs-registration"
									alt="Setup Registration Screen"></a> <span>5. Setup
										Registration Screen</span></li>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${sessionScope.pageStatus.privacyPage == 1}">
								<li class="stsCmpltd"><a
									href="setupprivacypolicyscreen.htm"
									class="prgrs-privacy-policy" alt="Setup Privacy Policy Screen"></a>
									<span>6. Setup Privacy Policy Screen</span></li>
							</c:when>
							<c:otherwise>
								<li><a href="setupprivacypolicyscreen.htm"
									class="prgrs-privacy-policy" alt="Setup Privacy Policy Screen"></a>
									<span>6. Setup Privacy Policy Screen</span></li>
							</c:otherwise>
						</c:choose>
					</ul>
					<span class="clear"></span>

					<div align="center">
						<input type="submit" class="btn-blue" value="Submit" id="button"
							name="button" title="Submit" disabled="disabled">
					</div>

				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	configureMenu("hubCitiadministration");
</script>