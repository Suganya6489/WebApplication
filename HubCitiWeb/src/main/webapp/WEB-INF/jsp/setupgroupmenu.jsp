<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<script type="text/javascript" src="scripts/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/jquery.form.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/colorPickDynamic.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/colorPicker.js"></script>
<link rel="stylesheet" type="text/css" href="/HubCiti/styles/colorPicker.css" />

<script type="text/javascript">

	$(document).ready(function()
	{			
		var menu = ${sessionScope.buttonList}.length;			
	});
	

</script>

<script type="text/javascript">
function createButton()
{
	document.button.mode.value="add";
	document.button.action = "creategroupmenu.htm";	
	document.button.method = "post";
	document.button.submit();
}

function editButton(btnDetails)
{
	var btnNm = $(btnDetails).text().trim();
		
	var btnGrp =  $(btnDetails).attr("datgrp");	

	var btnActn = $(btnDetails).attr("datactn");

	var menupos =  $(btnDetails).attr("datmpos");	

	var btnpos = $(btnDetails).attr("datbpos");

	 $("#grpType option").each(function(i){

        if($(this).val() == btnGrp)
	    {
		    $(this).attr('selected', true);   
	    }
	 });

	 $("#dataFnctn option").each(function(i){

        if($(this).val() == btnActn)
	    {
		    $(this).attr('selected', true);   
	    }
	});

	$('#btnName').val(btnNm);

	$("#addBtn").attr('value', 'Update');

	$("#addBtn").attr('onclick', 'update(' + menupos + ',' + btnpos + ')');

	$("#delBtn").show();

	$("#delBtn").attr('onclick', 'deleteButton(' + menupos + ',' + btnpos + ')');
}

function update(menupos, btnpos)
{
	document.button.mode.value="edit";
	document.button.menuPosition.value=menupos;
	document.button.position.value=btnpos;
	document.button.action = "creategroupmenu.htm";	
	document.button.method = "post";
	document.button.submit();
}

function deleteButton(menupos, btnpos)
{
	document.button.mode.value="delete";
	document.button.menuPosition.value=menupos;
	document.button.position.value=btnpos;
	document.button.action = "creategroupmenu.htm";	
	document.button.method = "post";
	document.button.submit();
}
</script>

<div class="wrpr-cont relative">
	<div id="slideBtn">
		<a href="#" onclick="revealPanel(this);" title="Hide Menu">
			<img src="images/slide_off.png" width="11" height="28" alt="btn_off" />
		</a>
	</div>
	<div id="bread-crumb">
		<ul>
			<li class="scrn-icon">
				<span class="icon-home">&nbsp;</span>
			</li>
			<li>
				Home
			</li>
			<li class="">
				<a href="slctTmplt.html">Change Template</a>
			</li>
			<li class="last">
				Main Menu [ <span id="tmpltNm"> Grouped Tab Template </span> ]
			</li>
		</ul>
	</div>
	<span class="blue-brdr"></span>
	<div class="content" id="login">
		<div id="menu-pnl" class="split">
			<jsp:include page="leftNavigation.jsp"></jsp:include>
		</div>
		<div class="cont-pnl split" id="equalHt">
			<div class="cont-block rt-brdr">
				<div class="title-bar">
					<ul id="title-actn">
						<c:choose>
						<c:when test="${sessionScope.menuName eq 'Setup Sub Menu' }">
						
							<li class="title-icon"><span class="icon-submenu">&nbsp;</span>
							</li>
						
						</c:when>
						<c:otherwise>
						<li class="title-icon"><span class="icon-main-menu">&nbsp;</span>
							</li>
						</c:otherwise>
						</c:choose>
						
						<li>Main Menu</li>
					</ul>
				</div>
				<div class="cont-wrp"  id="dynData">
					<form:form name="button" id="button" commandName="button">
					<form:hidden path="mode"/>
					<form:hidden path="position"/>
					<form:hidden path="menuPosition"/>
						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="cmnTbl">
							<tr>
								<td align="left" valign="baseline">Group Name</td>
								<td colspan="2"> 
									<form:errors path="btnGroup" cssClass="errorDsply"></form:errors>
									<div class="cntrl-grp zeroBrdr">               
										<form:select path="btnGroup" class="slctBx lesWdth" id="grpType">
											<form:option value="0">Select Group</form:option>
											<c:forEach items="${sessionScope.groupList}" var="groupName">
												<form:option value="${groupName}">${groupName}</form:option>
											</c:forEach>
										</form:select>                     
										<a href="#" onclick="tglGrp()">
											<img src="images/btn_add.png" width="24" height="24" alt="add" class="addGrp" />
										</a>
									</div>
									<ul class="btnNm mrgnTop">
										<li><div class="cntrl-grp btnNm"><input type="text" class="inputTxtBig" id="btnGroup"  onkeypress="return specialCharCheck(event)"/></div></li>
										<li><input type="button" value="Save" class="btn-blue" id="saveGrp" onclick="appendGrp()"/></li>
									</ul>
								</td>
							</tr>
							<tr>
								<td width="33%">Button Name</td>								
								<td width="67%" colspan="2">
								<form:errors path="btnName" cssClass="errorDsply"></form:errors>
									<div class="cntrl-grp">
										<form:input path="btnName" class="inputTxtBig vldt" id="btnName" maxlength="25"/>
									</div>
								</td>
							</tr>
							<tr>
								<td width="33%">Functionality</td>
								<td width="67%" colspan="2">								
									<form:errors path="btnAction" cssClass="errorDsply"></form:errors>
									<div class="cntrl-grp zeroBrdr">
										<form:select path="btnAction" class="slctBx" id="dataFnctn">
											<form:option value="0">Select Functionality</form:option>
											<c:forEach items="${sessionScope.linkList}" var="link">                   	  
												<form:option value="${link.menuTypeId}">${link.menuTypeName}</form:option>                    
											</c:forEach>
										</form:select>                 
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="3"><ul class="infoList cmnList" id="appSiteLst">
									<li>
									<input type="radio" name="appsites" />
									<span class="cell">Appsite Name</span><span class="cell">Location</span> </li>
									<li>
									<input type="radio" name="appsites" />
									<span class="cell">Appsite Name</span><span class="cell">Location</span> </li>
									<li class="actn"><a href="#"><img src="images/btn_add.png" width="24" height="24" alt="add" class="addImg"/> Create New App Site</a></li>
									</ul>
								</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td>
									<input type="button" name="button" value="Add Button" class="btn-blue" id="addBtn" onclick="createButton();" />
								</td>
								<td align="right">
									<input type="button" name="delBtn" value="Delete Button" class="btn btn-red" id="delBtn"/>
								</td>
							</tr>
							<tr>
							<td>&nbsp;</td>
							<td colspan="2">&nbsp;</td>
							</tr>
						</table>
					</form:form>
				</div>
			</div>
			<div class="cont-block">
				<div class="title-bar">
					<ul id="title-actn">
						<li class="title-icon"><span class="icon-iphone">&nbsp;</span></li>
						<li>App Preview</li>
					</ul>
				</div>
				<div class="prgrsStp">   
					<ul>
						<li class="step1">
							<span>Tab Creation</span>
						</li>
						<li class="step2">
							<span>Save Template</span>
						</li>
						<li class="saveTmplt">
							<input type="button" name="button" value="Save" class="btn-blue" id="saveTmplt" onclick="checkBtns()"/>
						</li>
						<li class="clear"></li>
					</ul>
				</div>
				<div class="cont-wrp">
					<div id="iphone-preview"> 
						<div id="iphone-priPolicy-preview">
							<div class="iphone-status-bar"></div>
							<div class="navBar iphone">
								<table width="100%" border="0" cellspacing="0" cellpadding="0" class="titleGrd">
								<tr>
								<td width="19%"><img src="images/backBtn.png" alt="back" width="50" height="30" /></td>
								<td width="54%" class="genTitle"><!--<img src="images/small-logo.png" width="86" height="35" alt="Logo" />--></td>
								<td width="27%" align="center"></td>
								</tr>
								</table>
							</div>
							<div class="previewAreaScroll">                  
								<div id="bnrImg"><img id="imgView" src="#" title="Banner Ad" /></div>
								<div id="preview_ie" title="Banner Ad"> </div>
								<ul id="dynTab" class="singleTab">									
									<c:forEach items="${sessionScope.buttonList}" var="buttonList">
											<li class="grpHdr"> ${buttonList.menuName} 	</li>										
											<c:forEach items="${buttonList.buttons}" var="button">																				
												<a onclick="editButton(this)" datmpos="${buttonList.position}"  datbpos="${button.position}" datgrp="${buttonList.menuName}" datactn="${button.btnAction}" datahref="undefined" id="temp-btn-1" href="javascript:void(0)">
												<span>${button.btnName}</span></a>												
											</c:forEach>		
														
									</c:forEach>
								</ul>
							</div>
						</div>
						<ul id="tab-main" class="tabbar">
							<li><img src="images/tab_btn_up_share.png" width="80" height="50" alt="share" /></li>
							<li><img src="images/tab_btn_up_settings.png" width="80" height="50" alt="settings" /></li>
							<li><img src="images/tab_btn_up_about.png" width="80" height="50" alt="about" /></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>