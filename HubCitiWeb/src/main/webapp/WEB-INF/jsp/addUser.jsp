<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<script type="text/javascript" src="scripts/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/jquery.form.js"></script>
<script src="/HubCiti/scripts/ckeditor/ckeditor.js"></script>
<script src="/HubCiti/scripts/ckeditor/config.js"></script>

<script type="text/javascript">
	$(document).ready(function() {				
				
				var chkdOptn_usr = $("input[name='userType']:radio:checked").val();
				if (chkdOptn_usr != 'NORMAL_USER') {
					$(".nrmlUsr").hide();
				}

				if (null != document.addUser.roleUserId.value
						&& '' != document.addUser.roleUserId.value) {
					$("#clearFields").hide();
					$("#SaveUpdate").val("Update User");
					$("#SaveUpdate").attr("title","update");
					$("#userName").attr("disabled", "disabled");
					$("#pageName").text("Edit User");
					document.title = "Edit User";
				}	
				
				var module = '${requestScope.module}';
				if(null != module && "" != module) {
					var arr = module.split(',');	
					jQuery.each(arr, function(i, val) { 
						$("#module option[value='" + val + "']").prop('selected', 'selected');
					});
					var totOpt = $('#module option').length;
					var totOptSlctd = $('#module option:selected').length;
					if (totOpt == totOptSlctd) {
						$('#chkModuleAllCat').prop('checked', 'checked');
					} else {
						$('#chkModuleAllCat').removeAttr('checked');
					}
				} else {
					$(".Events").hide();
					$(".Fundraisers").hide();
					
				}

				var eventCategory = '${requestScope.eventCat}';
				if (null != eventCategory && '' != eventCategory) {
					$("#module option").each(function() {
						var selectedVal = $(this).text();
						if(selectedVal == 'Events') {
							$(this).prop('selected', 'selected');
							$("." + selectedVal).show();
						}
					});
				}
				var arr = eventCategory.split(',');	
				jQuery.each(arr, function(i, val) { 
					$("#eventCategory option[value='" + val + "']").prop('selected', 'selected');
				});
				
				var fundraiserCategory = '${requestScope.fundraiserCat}';
				if (null != fundraiserCategory && '' != fundraiserCategory) {
					$("#module option").each(function() {
						var selectedVal = $(this).text();
						if(selectedVal == 'Fundraisers') {
							$(this).prop('selected', 'selected');
							$("." + selectedVal).show();
						}
					});
				}
				var arr = fundraiserCategory.split(',');	
				jQuery.each(arr, function(i, val) { 
					$("#fundraiserCategory option[value='" + val + "']").prop('selected', 'selected');
				});
				
				$("input[name='userType']:radio").change(function() {
					var getOptn = $(this).val();
					if (getOptn != 'NORMAL_USER')
						$(".nrmlUsr").hide();
					else {
						//$(".nrmlUsr").show();
						$("#module option").each(function() {
							var selectedVal = $(this).text();
							if ($(this).is(':selected')) {							
								$("." + selectedVal).show();
							} else {
								$("." + selectedVal).hide();
							}
						}); 
					}
				});
				
				$("#eventCategory").change(function() {		
					if (null != document.addUser.roleUserId.value && '' != document.addUser.roleUserId.value) {
						var isAssociated = false;										
						$("#eventCategory option").each(function() {									
							if (!($(this).is(':selected'))) {
								var associateCate = $(this).attr("associateCate");
								if(associateCate == "true") {
									isAssociated = true;
									$(this).prop('selected','selected');							
								}
							}
						});
						if(isAssociated == true)
						{
						    alert("Event has been created for this category(s). Please deassociate the category(s) or delete the event and proceed");
						}
					}
					var totOpt = $('#eventCategory option').length;
					var totOptSlctd = $('#eventCategory option:selected').length;
					if (totOpt == totOptSlctd) {
						$('#chkAllCat').prop('checked', 'checked');
					} else {
						$('#chkAllCat').removeAttr('checked');
					}					
				});	

				$("#fundraiserCategory").change(function() {		
					if (null != document.addUser.roleUserId.value && '' != document.addUser.roleUserId.value) {
						var isAssociated = false;										
						$("#fundraiserCategory option").each(function() {									
							if (!($(this).is(':selected'))) {
								var associateCate = $(this).attr("associateCate");
								if(associateCate == "true") {
									isAssociated = true;
									$(this).prop('selected','selected');							
								}
							}
						});
						if(isAssociated == true)
						{
						    alert("Fundraiser Event has been created for this category(s). Please deassociate the category(s) or delete the fundraiser event and proceed");
						}
					}
					var totOpt = $('#fundraiserCategory option').length;
					var totOptSlctd = $('#fundraiserCategory option:selected').length;
					if (totOpt == totOptSlctd) {
						$('#chkfundraiserAllCat').prop('checked', 'checked');
					} else {
						$('#chkfundraiserAllCat').removeAttr('checked');
					}					
				});	
				
				$("#module").change(function() {	
					if (null != document.addUser.roleUserId.value && '' != document.addUser.roleUserId.value) {
						var isAssociated = false;
						$("#module option").each(function() {							
							var selectedVal = $(this).text();
							if (!($(this).is(':selected'))) {
								var associateCate = $(this).attr("associateCate");
								if(associateCate == "true") {
									isAssociated = true;
									$(this).prop('selected','selected');
									$("." + selectedVal).show();
								} else {
									$("." + selectedVal).hide();
								}
							} else {
								$("." + selectedVal).show();
							}
						});
						if(isAssociated == true)
						{
						    alert("Event(s) has been created for this module(s). Please delete the event(s) and proceed");
						}
					} else {
						$("#module option").each(function() {
							var selectedVal = $(this).text();
							if ($(this).is(':selected')) {								
								$("." + selectedVal).show();
							} else {
								$("." + selectedVal).hide();
							}
						});
					}
					
					var totOpt = $('#module option').length;
					var totOptSlctd = $('#module option:selected').length;
					if (totOpt == totOptSlctd) {
						$('#chkModuleAllCat').prop('checked', 'checked');
					} else {
						$('#chkModuleAllCat').removeAttr('checked');
					}	
					
					var chkdOptn_usr = $("input[name='userType']:radio:checked").val();
					if (chkdOptn_usr != 'NORMAL_USER') {
						$(".nrmlUsr").hide();
					}
					
				});	

				$("#eventCategory").trigger("change");//fundraiser
				$("#fundraiserCategory").trigger("change");
				$("#module").trigger("change");

				$('#chkAllCat').click(function() {
					var sel = document.getElementById("eventCategory");
					if ($(this).is(':checked')){
						for ( var i = 0; i < sel.options.length; i++) {
							sel.options[i].selected = true;
						}
					}
					else {						
						var isAssociated = false;						
						$("#eventCategory option").each(function() {
							var associateCate = $(this).attr("associateCate");
							if(null != document.addUser.roleUserId.value && '' != document.addUser.roleUserId.value && associateCate == "true") {
								isAssociated = true;
								$(this).prop('selected','selected');							
							} else {
								$(this).prop('selected', false);
							}
						});
						if(isAssociated == true)
						{
						    alert("Event has been created for this category(s). Please deassociate the category(s) or delete the event and proceed");
						    var totOpt = $('#eventCategory option').length;
							var totOptSlctd = $('#eventCategory option:selected').length;
							if (totOpt == totOptSlctd) {
								$('#chkAllCat').prop('checked', 'checked');
							} else {
								$('#chkAllCat').removeAttr('checked');
							}
						}
					}					
					
				});
				
				$('#chkfundraiserAllCat').click(function() {
					var sel = document.getElementById("fundraiserCategory");
					if ($(this).is(':checked')){
						for ( var i = 0; i < sel.options.length; i++) {
							sel.options[i].selected = true;
						}
					}
					else {
						var isAssociated = false;
						$("#fundraiserCategory option").each(function() {	
							var associateCate = $(this).attr("associateCate");
							if(null != document.addUser.roleUserId.value && '' != document.addUser.roleUserId.value && associateCate == "true") {
								isAssociated = true;
								$(this).prop('selected','selected');							
							} else {
								$(this).prop('selected', false);
							}
						});
						if(isAssociated == true)
						{
						    alert("Fundraiser Event has been created for this category(s). Please deassociate the category(s) or delete the fundraiser event and proceed");
						    var totOpt = $('#fundraiserCategory option').length;
							var totOptSlctd = $('#fundraiserCategory option:selected').length;
							if (totOpt == totOptSlctd) {
								$('#chkfundraiserAllCat').prop('checked', 'checked');
							} else {
								$('#chkfundraiserAllCat').removeAttr('checked');
							}
						}
						/* for ( var i = 0; i < sel.options.length; i++) {
							sel.options[i].selected = false;
						} */
					}
					
				});
				
				$('#chkModuleAllCat').click(function() {
					var chkdOptn_usr = $("input[name='userType']:radio:checked").val();
					var sel = document.getElementById("module");
					if ($(this).is(':checked')){
						for ( var i = 0; i < sel.options.length; i++) {
							sel.options[i].selected = true;
							var selectedVal = sel.options[i].text;
							$("." + selectedVal).show();
						}
					}
					else {
						/* for ( var i = 0; i < sel.options.length; i++) {
							sel.options[i].selected = false;
							var selectedVal = sel.options[i].text;
							$("." + selectedVal).hide();
						} */
						
						var isAssociated = false;
						$("#module option").each(function() {	
							var selectedVal = $(this).text();
							var associateCate = $(this).attr("associateCate");							
							if(associateCate == "true") {
								isAssociated = true;
								$(this).prop('selected','selected');								
								$("." + selectedVal).show();
							} else {
								$(this).prop('selected', false);
								$("." + selectedVal).hide();
							}
						});
						if(isAssociated == true)
						{
							alert("Event has been created for this category(s). Please deassociate the category(s) or delete the event and proceed");
							var totOpt = $('#module option').length;
							var totOptSlctd = $('#module option:selected').length;
							if (totOpt == totOptSlctd) {
								$('#chkModuleAllCat').prop('checked', 'checked');
							} else {
								$('#chkModuleAllCat').removeAttr('checked');
							}
						}
					}
					
					var chkdOptn_usr = $("input[name='userType']:radio:checked").val();
					if (chkdOptn_usr != 'NORMAL_USER') {
						$(".nrmlUsr").hide();
					}
				});

				/*code which will always selects events for module
				$("#module").change(function() {
					$("#module option:first").prop('selected','selected');
				});		*/		
				
			});
</script>
<script type="text/javascript">
	
	function saveUpdateUser() {
		var mod = '';
		$("#module option:selected").each(function(){
		    if(mod == ''){
		        mod = $(this).text();
		    } else {
		        mod = mod + "," + $(this).text();
		    }
		});
		document.addUser.moduleName.value = mod;
		document.addUser.action = "saveuser.htm";
		document.addUser.method = "POST";
		document.addUser.submit();
	}
	function backToUsers() {
		r = confirm("Are you sure you want to return to previous page!");
		if(r === true)
		{
			document.addUser.action = "displayusers.htm";
			document.addUser.method = "GET";
			document.addUser.submit();
		}
	}
	function clearFields() {
		r = confirm("Are you sure you want to clear the field value!");
		if(r === true)
		{
			document.addUser.firstName.value = "";
			document.addUser.lastName.value = "";
			document.addUser.userName.value = "";
			document.addUser.emailId.value = "";
			
			document.addUser.userStatus.value = "1";
			$('input[name="userStatus"][value="1"]').prop('checked', true);
			
			document.addUser.userType.value = "NORMAL_USER";
			$('input[name="userType"][value="NORMAL_USER"]').prop('checked', true);
			
			document.addUser.module.value = "";
			$("#module option").prop('selected', false);
			$('#chkModuleAllCat').removeProp('checked');
			$(".nrmlUsr").hide();
			
			document.addUser.eventCategory.value = "";
			$("#eventCategory option").prop('selected', false);

			$('#chkAllCat').removeProp('checked');
			
			document.addUser.fundraiserCategory.value = "";
			$("#fundraiserCategory option").prop('selected', false);

			$('#chkfundraiserAllCat').removeProp('checked');
	
			if (document.getElementById('firstName.errors') != null) {
				document.getElementById('firstName.errors').style.display = 'none';
			}
			if (document.getElementById('lastName.errors') != null) {
				document.getElementById('lastName.errors').style.display = 'none';
			}
			if (document.getElementById('userName.errors') != null) {
				document.getElementById('userName.errors').style.display = 'none';
			}
			if (document.getElementById('emailId.errors') != null) {
				document.getElementById('emailId.errors').style.display = 'none';
			}
			if (document.getElementById('eventCategory.errors') != null) {
				document.getElementById('eventCategory.errors').style.display = 'none';
			}
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
				<li class=""><a href="displayusers.htm">Setup Users</a>
				</li>
				<li class="last" id="pageName">Add User</li>
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
							<li><a class="brdr-rt" href="#">Add User</a></li>
						</ul>
						<div class="clear"></div>
					</div>
					<div class="cont-wrp">
						<form:form name="addUser" id="addUser" commandName="addUser">
							<form:hidden path="roleUserId" />
							<form:hidden path="lowerLimit" value="${requestScope.lowerLimit}" />
							<form:hidden path="userSearch" value="${requestScope.userSearch}" />
							<form:hidden path="moduleName"/>
							<table width="100%" border="0" cellspacing="0" cellpadding="0" class="brdrlsTbl">
								<tr>
									<td width="18%"><label class="mand">First Name</label>
									</td>
									<td width="33%"><form:errors cssStyle="color:red" path="firstName"></form:errors>
										<div class="cntrl-grp">
											<form:input path="firstName" class="inputTxtBig" maxlength="50" tabindex="1"/>
										</div>
									</td>
									<td width="17%" align="right"><label class="mand">Last Name</label></td>
									<td width="32%"><form:errors cssStyle="color:red" path="lastName"></form:errors>
										<div class="cntrl-grp">
											<form:input path="lastName" class="inputTxtBig" maxlength="50" tabindex="2"/>
										</div>
									</td>
								</tr>
								<tr>
									<td><label class="mand">Email</label></td>
									<td><form:errors cssStyle="color:red" path="emailId"></form:errors>
										<div class="cntrl-grp">
											<form:input path="emailId" class="inputTxtBig" maxlength="70" tabindex="3"/>
										</div>
									</td>
									<td align="right"><label class="mand">User Name</label></td>
									<td><form:errors cssStyle="color:red" path="userName"></form:errors>
										<div class="cntrl-grp">
											<form:input path="userName" class="inputTxtBig" maxlength="50" tabindex="4"/>
										</div>
									</td>
								</tr>
								<tr>
									<td align="left">User Status</td>
									<td><span class="spacing"> 
											<form:radiobutton path="userStatus" value="1" checked="checked" tabindex="5"/><label for="usrActv"> Active</label> 
											<form:radiobutton path="userStatus" value="0" tabindex="6"/><label for="usrInactv"> Inactive</label> 
									</span></td>
									<td align="right">User Type</td>
									<td align="left">
										<span class="spacing"> 
											<form:radiobutton path="userType" value="NORMAL_USER" checked="checked" tabindex="7"/> <label for="nrmlUsr"> Normal User</label>
											<form:radiobutton path="userType" value="SUPER_USER" tabindex="8"/> <label for="suprUsr"> Super User</label> 
										</span>
									</td>										
								</tr>
								<tr>
									<td><label class="mand">Module</label></td>
									<td><form:errors cssStyle="color:red" path="module"></form:errors>
										<div class="cntrl-grp zeroBrdr ">
											<form:select path="module" size="1" multiple="multiple" cssClass="slctBx multiSlct" tabindex="9">
												<c:forEach items="${sessionScope.modulesLst}" var="modulesLst">
													<form:option value="${modulesLst.moduleID}" associateCate="${modulesLst.isAssociated}">${modulesLst.moduleName}</form:option>
												</c:forEach>
											</form:select>
											<label class="lblTxt">Hold Ctrl to select more than one module</label>
										</div>
									</td>
									<td valign="top" align="left" class="Label" colspan="2">
										<label>
											<input type="checkbox" id="chkModuleAllCat" tabindex="10">
												Select All Modules 
										</label>
									</td>
								</tr>
								<tr class="nrmlUsr Events">
									<td><label class="mand">Event Category</label></td>
									<td><form:errors cssStyle="color:red" path="eventCategory"></form:errors>
										<div class="cntrl-grp zeroBrdr">
											<form:select path="eventCategory" size="1" multiple="multiple" cssClass="slctBx userMultiSlct" tabindex="11">
												<c:forEach items="${sessionScope.eventCatLst}" var="eventCatLst">
													<form:option value="${eventCatLst.catId}" associateCate="${eventCatLst.associateCate}" selected="">${eventCatLst.catName}</form:option>
												</c:forEach>
											</form:select>
											<label class="lblTxt">Hold Ctrl to select more than one category</label>
										</div></td>
									<td valign="top" align="left" class="Label" colspan="2">
										<label>
											<input type="checkbox" id="chkAllCat" tabindex="12">
												Select All Categories 
										</label>
									</td>
									<!-- <td align="right">&nbsp;</td>
									<td align="left">&nbsp;</td> -->
								</tr>
								<tr class="nrmlUsr Fundraisers">
									<td><label class="mand">Fundraiser Category</label></td>
									<td><form:errors cssStyle="color:red" path="fundraiserCategory"></form:errors>
										<div class="cntrl-grp zeroBrdr">
											<form:select path="fundraiserCategory" size="1" multiple="multiple" cssClass="slctBx userMultiSlct" tabindex="13">
												<c:forEach items="${sessionScope.fundraiserCatLst}" var="fundraiserCatLst">
													<form:option value="${fundraiserCatLst.catId}" associateCate="${fundraiserCatLst.associateCate}" selected="">${fundraiserCatLst.catName}</form:option>
												</c:forEach>
											</form:select>
											<label class="lblTxt">Hold Ctrl to select more than one category</label>
										</div></td>
									<td valign="top" align="left" class="Label" colspan="2">
										<label>
											<input type="checkbox" id="chkfundraiserAllCat" tabindex="14">
												Select All Categories 
										</label>
									</td>
									<!-- <td align="right">&nbsp;</td>
									<td align="left">&nbsp;</td> -->
								</tr>
							</table>
						</form:form>
						<div class="cntrInput mrgnTop" align="center">							 
							<input type="button" class="btn-blue" value="Save User" onclick="saveUpdateUser();" id="SaveUpdate" title="Save User" tabindex="15"> 
							<input type="button" class="btn-blue" value="Clear" onclick="clearFields();" id="clearFields" title="clear" tabindex="16">
							<input type="button" class="btn-blue" value="Back" onclick="backToUsers();" title="back" tabindex="17">
						</div>
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