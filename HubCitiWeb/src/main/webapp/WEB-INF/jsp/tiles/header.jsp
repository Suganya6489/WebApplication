<link href="/HubCiti/styles/styles.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/HubCiti/scripts/jquery-1.10.2.js"></script>
<script src="/HubCiti/scripts/global.js" type="text/javascript"></script>
<script type="text/javascript" src="/HubCiti/scripts/web.js"></script>
<script src="scripts/jquery-ui.js"></script>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@page session="true"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.Date.*"%>
<div id="hdr">
	<%
		String currentDate = null;
		try {
			DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
			Date date1 = new Date();
			currentDate = df.format(date1);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	%>

	<div id="hdr-wrpr">
		<div id="hdr-cont">
			<div class="logo">
				<c:choose>
					<c:when test="${sessionScope.loginUserType eq 'RegionApp'}">
						<img src="images/hubregion.png" width="80" height="19" alt="hibciti" />
					</c:when>
					<c:otherwise>
						<img src="images/hubciti_logo.png" width="80" height="19" alt="hibciti" />
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<c:choose>
			<c:when test="${sessionScope.loginUser ne null}">
				<div id="hdr-bar">
					<div id="hdr-mainSctn" class="hdr-pnl relative">
						Dashboard
						<!--<a href="#" id="menuTgl" onclick="revealPanel(this);" title="Hide Menu"> <img src="images/btn_off.png" alt="btn_off" /></a> -->
					</div>
					<div id="hdr-subSctn" class="hdr-pnl">
						<ul id="hdr-actns">
							<!-- <li class="settings"><a href="#">Settings <span
									class="actn actn-dwn"></span></a></li> -->
							<li class="user"><label>Welcome:</label> <a href="#"
								class="prmry-link"><c:out value="${sessionScope.loginUser.userName }"></c:out> <span class="actn actn-dwn"></span>
									<label><%=currentDate%></label>
							</a></li>
							<li class="logout"><a href="logout.htm">Logout</a></li>
						</ul>
					</div>
				</div>

			</c:when>

		</c:choose>
	</div>
</div>