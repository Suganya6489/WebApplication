<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<%@taglib prefix="page" uri="/WEB-INF/pagination.tld"%>
<script type="text/javascript" src="scripts/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/jquery.form.js"></script>
<script src="/HubCiti/scripts/ckeditor/ckeditor.js"></script>
<script src="/HubCiti/scripts/ckeditor/config.js"></script>

<script type="text/javascript">
	function callNextPage(pagenumber, url) {
		document.users.pageNumber.value = pagenumber;
		document.users.pageFlag.value = "true";
		document.users.action = url;
		document.users.method = "get";
		document.users.submit();
	}

	function searchUsers() {
		document.users.lowerLimit.value = 0;
		document.users.action = "displayusers.htm";
		document.users.method = "GET";
		document.users.submit();
	}

	function searchUsersOnKeyPress(event) {
		document.users.lowerLimit.value = 0;
		var keycode = (event.keyCode ? event.keyCode : event.which);
		if (keycode == '13') {
			document.users.action = "displayusers.htm";
			document.users.method = "GET";
			document.users.submit();
		}
	}

	function addUser() {
		document.users.lowerLimit.value = '${requestScope.lowerLimit}';
		document.users.action = "adduser.htm";
		document.users.method = "GET";
		document.users.submit();
	}

	function editUser(roleUserId) {
		document.users.lowerLimit.value = '${requestScope.lowerLimit}';
		document.users.roleUserId.value = roleUserId;
		document.users.action = "edituser.htm";
		document.users.method = "GET";
		document.users.submit();
	}

	function activateDeactivateUser(roleUserId, flag) {
		r = confirm("Are you sure you want to " + flag + " this user!")
		if (r === true) {
			document.users.userStatus.value = flag;
			document.users.lowerLimit.value = '${requestScope.lowerLimit}';
			document.users.roleUserId.value = roleUserId;
			document.users.action = "activatedeactivateuser.htm";
			document.users.method = "POST";
			document.users.submit();
		}
	}
</script>


<div id="wrpr">
	<span class="clear"></span>
	<div class="wrpr-cont relative">
		<div id="slideBtn">
			<a href="#" onclick="revealPanel(this);" title="Hide Menu"><img src="images/slide_off.png" width="11" height="28" alt="btn_off" /> </a>
		</div>
		<!--Breadcrum div starts-->
		<div id="bread-crumb">
			<ul>
				<li class="scrn-icon"><span class="icon-home">&nbsp;</span>
				</li>
				<li><a href="welcome.htm">Home</a>
				</li>
				<li class="last">Setup Users</li>
			</ul>
		</div>
		<!--Breadcrum div ends-->
		<span class="blue-brdr"></span>
		<!--Content div starts-->
		<div class="content" id="login">
			<!--Left Menu div starts-->
			<div id="menu-pnl" class="split">
				<ul id="icon-menu">
					<jsp:include page="leftNavigation.jsp"></jsp:include>
				</ul>
			</div>
			<!--Left Menu div ends-->
			<!--Content panel div starts-->
			<div class="cont-pnl split" id="equalHt">
				<div class="cont-block rt-brdr stretch">
					<div class="title-bar">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-usersetup">&nbsp;</span>
							</li>
							<li>Setup Users</li>
						</ul>
					</div>
					<div class="tabd-pnl">
						<ul class="nav-tabs">
							<li><a class="brdr-rt" href="#">Manage Users</a></li>
						</ul>
						<div class="clear"></div>
					</div>
					<div class="cont-wrp">
						<form:form name="users" id="users" commandName="users">
							<form:hidden path="roleUserId" />
							<form:hidden path="lowerLimit" />
							<form:hidden path="userStatus" />
							<input type="hidden" name="pageNumber" />
							<input type="hidden" name="pageFlag" />
							<table width="100%" border="0" cellspacing="0" cellpadding="0" class="zerobrdrTbl">
								<tr>
									<td width="6%"><label>Search</label>
									</td>
									<td width="20%">
										<div class="cntrl-grp">
											<form:input path="userSearch" maxlength="50" cssClass="inputTxtBig" onkeypress="searchUsersOnKeyPress(event);" tabindex="1"/>
										</div>
									</td>
									<td width="10%"><a href="#"><img src="images/searchIcon.png" width="20" height="17" alt="search" title="search User" onclick="searchUsers();" /> </a>
									</td>
									<td width="40%" align="right"><input type="button" value="Add User" class="btn-blue" onclick="addUser();" title="Add User" tabindex="2"/>
									</td>
								</tr>
							</table>
						</form:form>
						<c:if test="${requestScope.responseStatus ne null && !empty requestScope.responseStatus}">
							<c:choose>
								<c:when test="${requestScope.responseStatus eq 'INFO' }">
									<div class="alertBx warning mrgnTop cntrAlgn">
										<span class="actn-close" title="close"></span>
										<p class="msgBx">
											<c:out value="${requestScope.responeMsg}" />
										</p>
									</div>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${requestScope.responseStatus eq 'SUCCESS' }">
											<div class="alertBx success mrgnTop cntrAlgn">
												<span class="actn-close" title="close"></span>
												<p class="msgBx">
													<c:out value="${requestScope.responeMsg}" />
												</p>
											</div>
										</c:when>
										<c:otherwise>

											<div class="alertBx failure mrgnTop cntrAlgn alertBx_big">
												<span class="actn-close" title="close"></span>
												<p class="msgBx">
													<c:out value="${requestScope.responeMsg}" />
												</p>
											</div>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</c:if>
						<c:if test="${userLst ne null}">
							<div class="relative">
								<div class="hdrClone"></div>
								<div class="scrollTbl tblHt mrgnTop">
									<table width="100%" cellspacing="0" cellpadding="0" border="0" id="" class="grdTbl clone-hdr fxdhtTbl">
										<thead>
											<tr class="tblHdr">
												<th width="22.5%">User Name</th>
												<th width="24.5%">First Name</th>
												<th width="22%">Module</th>
												<th width="17%">User Type</th>
												<th width="14%">Action</th>
											</tr>
										</thead>
										<tbody class="scrollContent">
											<c:forEach items="${sessionScope.userLst}" var="userLst">
												<tr>
													<td><div class="txtWrp"><c:out value="${userLst.userName}"></c:out></div>
													</td>
													<td><div class="txtWrp">
															<c:out value="${userLst.firstName}"></c:out>
														</div></td>
													<td><div class="txtWrp"><c:out value="${userLst.module}"></c:out></div>
													</td>
													<td><c:out value="${userLst.userType}"></c:out>
													</td>
													<!--  <td align="center">													
												</td> -->
													<td><a title="edit user" href="#"> <img height="20" width="20" class="actn-icon" alt="edit user" src="images/edit_icon.png" onclick='editUser("${userLst.roleUserId}")' /> </a>  
													<c:choose>
																<c:when test="${userLst.userStatus eq 1}">
																<a title="deactivate user" href="#">
																	<img height="20" width="20" class="actn-icon" alt="deactivate user" src="images/deactivate_icon.png" onclick='activateDeactivateUser("${userLst.roleUserId}", "deactivate")' />
																</a>
																</c:when>
																<c:otherwise>
																<a title="activate user" href="#">
																	<img height="20" width="20" class="actn-icon" alt="activate user" src="images/activate_icon.png" onclick='activateDeactivateUser("${userLst.roleUserId}", "activate")' />
																	</a>
																</c:otherwise>
															</c:choose></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
								<div class="pagination mrgnTop">
									<page:pageTag currentPage="${sessionScope.pagination.currentPage}" nextPage="4" totalSize="${sessionScope.pagination.totalSize}" pageRange="${sessionScope.pagination.pageRange}" url="${sessionScope.pagination.url}" />
								</div>
							</div>
						</c:if>
					</div>
				</div>
			</div>
			<!--Content panel div ends-->
		</div>
		<!--Content div ends-->
	</div>
</div>
<script type="text/javascript">
	configureMenu("setupusers");
</script>