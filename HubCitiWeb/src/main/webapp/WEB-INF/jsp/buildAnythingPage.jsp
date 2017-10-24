<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<script type="text/javascript" src="scripts/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/jquery.form.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/colorPickDynamic.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/colorPicker.js"></script>
<script src="/HubCiti/scripts/ckeditor/ckeditor.js"></script>
<script src="/HubCiti/scripts/ckeditor/config.js"></script>

<link rel="stylesheet" type="text/css" href="/HubCiti/styles/colorPicker.css" />

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
				<li class="last">Build AnyThing Page<sup>TM</sup></li>
			</ul>
		</div>
		<span class="blue-brdr"></span>
		<div class="content" id="login">
			<div id="menu-pnl" class="split">
				<jsp:include page="leftNavigation.jsp"></jsp:include>
			</div>
			<div class="cont-pnl split">
				<form:form name="screenSettingsForm" id="screenSettingsForm" commandName="screenSettingsForm">
				<form:hidden path="lowerLimit" value="${requestScope.lowerLimit}"/>
					<div class="cont-block stretch">
						<div class="title-bar">
							<ul class="title-actn">
								<li class="title-icon"><span class="icon-aboutus">&nbsp;</span></li>
								<li>Build AnyThing Page<sup>TM</sup></li>
							</ul>
						</div>
						<span class="clear"></span>
						<div class="cont-wrp mnHt">
							<h4>Please choose the type of page you would like to create:</h4>
							<ul class="cont-list mrgnTop">
								<li>
									<!-- <input type="radio" name="pageType" id="existingWebPg" value="linktoExstngPg.htm" /> -->
									<input name="pgSlctn" type="radio" value="" id="existingPg" onclick="window.location.href='setupanythingscreen.htm'"/>
									<label for="existingPg">Link to Existing</label>
								</li>
								<li>
									<input name="pgSlctn" type="radio" value="" id="makeOwnPg" onclick="window.location.href='makeyourownanythingpage.htm'" /> 
									<label for="makeOwnPg">Make Your Own</label>
								</li>
							</ul>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	configureMenu("setupanythingpage");
</script>