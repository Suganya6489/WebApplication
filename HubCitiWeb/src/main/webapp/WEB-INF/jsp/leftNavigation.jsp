<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<ul id="icon-menu">
	<c:choose>
		<c:when test="${sessionScope.loginUserType eq 'RegionApp'}">
			<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
				<li><a href="welcome.htm" class="icon-adminStng"
					title="HubRegion Admin" id="hubCitiadministration"><span>HubRegion<sup>TM</sup>
							Admin
					</span></a></li>
			</sec:authorize>
		</c:when>
		<c:otherwise>
			<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
				<li><a href="welcome.htm" class="icon-adminStng"
					title="Hubciti Admin" id="hubCitiadministration"><span>HubCiti<sup>TM</sup>
							Administration
					</span></a></li>
			</sec:authorize>
		</c:otherwise>
	</c:choose>
	<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
		<li><a href="displayusers.htm" class="icon-usersetup" title="Setup Users"
			id="setupusers"><span>Setup Users</span></a></li>
	</sec:authorize>
	<!--  <li><a href="setupUsers.html" class="icon-usersetup" title="Setup Login Screen"><span>Setup User</span>s</a></li> -->
	<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
		<li><a href="setuploginscreen.htm" class="icon-login"
			title="Setup Login Screen" id="setuploginpage"><span>Setup
					Login Screen</span></a></li>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
		<li><a href="generealsettings.htm" class="icon-setting"
			title="General Settings" id="generalsettings"><span>General
					Settings</span></a></li>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
		<li><a href="setupsplashscreen.htm" class="icon-welcome"
			title="Setup Welcome Page" id="setupsplashscreen"><span>Setup
					Welcome Page</span></a></li>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
		<li><a href="usersettings.htm" class="icon-usrstng"
			title="Setup User Settings" id="setupusersettings"><span>Setup
					User Settings</span></a></li>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
		<li><a href="setupregscreen.htm" class="icon-registration"
			title="Setup Registration" id="setupregistration"><span>Setup
					Registration</span></a></li>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
		<li><a href="setupprivacypolicyscreen.htm"
			class="icon-privacy-policy" title="Setup Privacy Policy"
			id="setupprivacypolicy"><span>Setup Privacy Policy</span></a></li>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
		<li><a href="setupaboutusscreen.htm" class="icon-about"
			title="Setup About Us" id="setupaboutus"><span>Setup About
					Us</span></a></li>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
		<li><a href="displaymainmenu.htm" class="icon-menu"
			title="Setup Main Menu" id="setupmainmenu"><span>Setup
					Main Menu</span></a></li>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
		<li><a href="displaysubmenu.htm" class="icon-submenu"
			title="Setup Sub Menu" id="setupsubmenu"><span>Setup Sub
					Menu</span></a></li>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
		<li><a href="setuptabbar.htm" class="icon-tabbar"
			title="Setup Tab Bar" id="setuptabbar"><span>Setup Tab Bar</span></a></li>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
		<li><a href="displayanythingpages.htm" class="icon-about"
			title="Setup AnyThing Page" id="setupanythingpage"><span>Setup
					AnyThing<sup>TM</sup> Page
			</span></a></li>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
		<li><a href="displayalerts.htm" class="icon-alert"
			title="Setup Alerts" id="setupalerts"><span>Setup Alerts</span></a></li>
	</sec:authorize>
	<sec:authorize
		access="hasAnyRole('ROLE_ADMIN_VIEW','ROLE_EVENT_SUPER_USER_VIEW','ROLE_EVENT_USER_VIEW')">
		<li><a href="manageevents.htm" class="icon-event"
			title="Setup Events" id="setupevents"><span>Setup Events</span></a></li>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
		<li><a href="displaycityexp.htm" class="icon-experience"
			title="Setup Experience" id="setupexperience"><span>Setup
					Experience</span></a></li>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
		<li><a href="getassociretlocs.htm" class="icon-retlrLoctn"
			title="Setup Retailer Location" id="setupretailerlocation"><span>Setup
					Retailer Location</span></a></li>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
		<li><a href="displayfaq.htm" class="icon-faq" title="Setup FAQ"
			id="setupfqa"><span>Setup FAQ's</span></a></li>
	</sec:authorize>	
	<sec:authorize
		access="hasAnyRole('ROLE_ADMIN_VIEW','ROLE_FUNDRAISING_SUPER_USER_VIEW','ROLE_FUNDRAISING_USER_VIEW')">
		<li><a href="managefundraisers.htm" class="icon-fundraising"
			title="Setup Fundraisers" id="setupfndrevt"><span>Setup Fundraisers</span></a></li>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN_VIEW')">
		<li><a href="displaydeals.htm" class="icon-deal"
			title="Setup Deals" id="setupdeals"><span>Setup Deals</span></a></li>
	</sec:authorize>
</ul>