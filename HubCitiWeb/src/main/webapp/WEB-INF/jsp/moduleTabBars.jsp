<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>

<script type="text/javascript">

var selectedIds = [];

$(document).ready(function() {	
	/*module tab bar */
	$('#module-tab').on('change.module-trgr',function(){
		selectedIds.splice(0,selectedIds.length);
		var module = $(this).find("option:selected").val();
		$(".pnl-title").removeClass("active");
		$("#"+module).find(".pnl-title").addClass("active");
		$("#multiplChk").find('li .tabBtn').each(function(index, element) {
	        $(this).removeAttr("checked");
			$(this).removeClass("dsbled");
			$(this).removeAttr("disabled");
			//$(this).parents("li").removeClass("mandatory");
	    });
	    
		$("#"+module).find(".btn-pnl li").each(function(){
			var btnId = $(this).find("img").attr("btnid");
			//var isMandatory = $(this).find("img").attr("isMandatory");
			$("#multiplChk").find("li").each(function() {
				var bottomBtn = $(this).find(".tabBtn").val();
				if(btnId == bottomBtn)
				{
					$(this).find(".tabBtn").prop("checked",true);
					selectedIds.push(bottomBtn);
					/*if(isMandatory == "true") {
						$(this).addClass('dsbled');
						$(this).addClass('mandatory');
						$(this).find(".tabBtn").prop('disabled',true);
					}*/
				}
			});

		});

		var ckkCnt = $("#multiplChk :checked").length;
		if (ckkCnt >= 4) {
			$("#multiplChk :checkbox:not(:checked)").each(function() {
				$(this).parents('li').addClass('dsbled');
				$('#multiplChk input:checkbox:not(:checked)').prop('disabled',true);
			});
		} else {
			//$("#multiplChk :checkbox:not(:checked)").each(function() {
				$(this).parents('li').removeClass('dsbled');
				$('#multiplChk input:checkbox:not(:checked)').prop('disabled', false);
			//});
		}
	    
	});
	
	$(".lst-edit").on('click',function() {
		selectedIds.splice(0,selectedIds.length);
		var moduleId = $(this).attr("moduleId");
		$("#module-tab option[value='" + moduleId + "']").attr("selected","selected");
		$(".pnl-title").removeClass("active");
		$("#"+moduleId).find(".pnl-title").addClass("active");
		$("#multiplChk").find('li .tabBtn').each(function(index, element) {
	        $(this).removeAttr("checked");
			$(this).removeClass("dsbled");
			$(this).removeAttr("disabled");
			//$(this).parents("li").removeClass("mandatory");
	    });
		$(this).parents(".panel").find(".btn-pnl li").each(function(){
			var btnId = $(this).find("img").attr("btnid");
			//var isMandatory = $(this).find("img").attr("isMandatory");
			$("#multiplChk").find("li").each(function() {
				var bottomBtn = $(this).find(".tabBtn").val();
				if(btnId == bottomBtn)
				{
					$(this).find(".tabBtn").prop("checked",true);
					selectedIds.push(bottomBtn);
					/*if(isMandatory == "true") {
						$(this).addClass('dsbled');
						$(this).addClass('mandatory');
						$(this).find(".tabBtn").prop('disabled',true);
					}*/
				}
			});
		});

		var ckkCnt = $("#multiplChk :checked").length;
		if (ckkCnt >= 4) {
			$("#multiplChk :checkbox:not(:checked)").each(function() {
				$(this).parents('li').addClass('dsbled');
				$('#multiplChk input:checkbox:not(:checked)').prop('disabled',true);
			});
		} else {
			//$("#multiplChk :checkbox:not(:checked)").each(function() {
				$(this).parents('li').removeClass('dsbled');
				$('#multiplChk input:checkbox:not(:checked)').prop('disabled', false);
			//});
		}
		
	});
	
	$("#multiplChk").on('click', 'li:not(.dsbled), li:not(.dsbled) input', function(e) {
		
		var chkSts, curImg, $chkBx;
		if (this.tagName.toUpperCase() === 'INPUT') {
			// checkbox function
			$chkBx = $(this);
			if ($chkBx.is(':checked')) {
				selectedIds.push($(this).val());
			} else {
				selectedIds.splice($.inArray($(this).val(), selectedIds),1);
			}
			e.stopPropagation();
		} else {
			$chkBx = $(this).find(":checkbox");
			if (!$chkBx.prop("checked")) {
				$chkBx.prop("checked",true);
				selectedIds.push($(this).find("input").val());
			} else {
				$chkBx.prop("checked",false);
				selectedIds.splice($.inArray($(this).find("input").val(), selectedIds),1);
			}
		}
		chkSts = $chkBx[0].checked;
		curImg = $chkBx.prev('img')[0];
		// this code for checking length 4
		var ckkCnt = $("#multiplChk :checked").length;
		if (ckkCnt >= 4) {
			$("#multiplChk :checkbox:not(:checked)").each(function() {
				$(this).parents('li').addClass('dsbled');
				$('#multiplChk input:checkbox:not(:checked)').prop('disabled',true);
			});
		} else {
			//$("#multiplChk :checkbox:not(:checked)").each(function() {
				$(this).parents('li').removeClass('dsbled');
				$('#multiplChk input:checkbox:not(:checked)').prop('disabled', false);
			//});
		}
		
		if(chkSts){
			var btnId = $chkBx.val();
			var curModule = $("#module-tab option:selected").val();
		   	$('#'+curModule).find('ul').append("<li><img src='"+curImg.src+"' id='pre-"+curImg.id+"' btnId='"+btnId+"'/></li>");
		}else {
			var btnId = $chkBx.val();
			var curModule = $("#module-tab option:selected").val();
		   	$('#'+curModule).find("img[btnId='" + btnId + "']").parent('li').remove();
		}
	});
	
	/*$("#multiplChk").on('click', 'li.mandatory', function(e) {
		alert("This is a mandatory Bottom Button, You can not delete this");
	});*/
});

function saveModuleTabBar(saveDelete, moduleName)
{
	if(saveDelete === "Delete")	{
		/*var mand = false;
		$('#' + moduleName + " li").each(function(){
			var isMandatory = $(this).find("img").attr("isMandatory");
			if(isMandatory == "true") {
				mand = true;
			}
		});*/
		
		//if(!mand) {
			var r = confirm("Are you sure you want to delete this Module Tab Bar !");
			if(r == true) {
				document.screenSettingsForm.btnLinkId.value = "";
				document.screenSettingsForm.bottomBtnName.value = moduleName;
				document.screenSettingsForm.action = "savemoduletabbars.htm";
				document.screenSettingsForm.method = "POST";
				document.screenSettingsForm.submit();
			}
		//} 
		else {
			alert("Mandatory Bottom Bar Buttons are associated to this functionlity, You can not delete this");
		}
	} else {

		var curModule = $("#module-tab option:selected").val();
		if(curModule === "") {
			alert("Please select module name and then associate tab bars");
			selectedIds.splice(0,selectedIds.length);
			$("#multiplChk").find('li .tabBtn').each(function(index, element) {
		        $(this).removeAttr("checked");
				$(this).removeClass("dsbled");
				$(this).removeAttr("disabled");
				//$(this).parents("li").removeClass("mandatory");
		    });
		}
		else {
			var bottmBtnLen = selectedIds.length;
			if (bottmBtnLen != 0 && bottmBtnLen < 4) {
				alert("Please select 4 Tab Bar Controls");
			} else {
				document.screenSettingsForm.btnLinkId.value = selectedIds;
				document.screenSettingsForm.action = "savemoduletabbars.htm";
				document.screenSettingsForm.method = "POST";
				document.screenSettingsForm.submit();
			}
		}	
	}
}

/*function editModuleTabBar(id) {
		var bottomBtnId = $("#moduleTabBars").find("div.panel#" + id + "li img").attr("btnId");
		alert(bottomBtnId)
		$("#multiplChk").find("li").each(function() {
			var chkVal = $(this).next("input").val();
			if(chkVal == bottomBtnId) {
				$("#" + bottomBtnId).prop('checked', true);
			}
		});
}*/

</script>

<div id="wrpr">
	<div class="clear"></div>
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
				<li class="last">Setup Tab Bar</li>
			</ul>
		</div>
		<!--Breadcrum div ends-->
		<span class="blue-brdr"></span>
		<!--Content div starts-->
		<div class="content">
			<!--Left Menu div starts-->
			<div id="menu-pnl" class="split">
				<menu:MenuTag menuTitle="${sessionScope.menuName}" />
			</div>
			<!--Left Menu div ends-->
			<!--Content panel div starts-->
			<div class="cont-pnl split">
				<div class="cont-block stretch">
					<div class="title-bar">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-tabbar">&nbsp;</span>
							</li>
							<li>Setup Tab Bar Controls</li>
						</ul>
					</div>
					<div class="tabd-pnl">
						<ul class="nav-tabs">
							<li><a href="setuptabbar.htm">General Tab Bar</a></li>
							<li><a href="#" class="active rt-brdr">Module Tab Bar</a>
							</li>
						</ul>
						<div class="clear"></div>
					</div>
					<div class="">
						<div class="cont-block rt-brdr">
							<form:form name="screenSettingsForm" id="screenSettingsForm" commandName="screenSettingsForm">
								<form:hidden path="btnLinkId" />
								<div class="cont-wrp">
									<table width="100%" border="0" cellpadding="0" cellspacing="0" class="cmnTbl">
										<tr>
											<td>Module Name</td>
											<td width="60%"><div class="cntrl-grp zeroBrdr">
													<form:select path="bottomBtnName" id="module-tab" cssClass="slctBx">
														<form:option value="">Select Module</form:option>
														<c:forEach items="${sessionScope.modulesList}" var="item">
															<form:option value="${item.bottomBtnId}">${item.bottomBtnName}</form:option>
														</c:forEach>
													</form:select>
												</div>
											</td>
										</tr>
									</table>
									<div class="txt-center">
										<input type="button" id="saveTmplt" class="btn-blue" value="Save" name="button" onclick="saveModuleTabBar('Save');">
									</div>
								</div>
							</form:form>
							<div class="title-bar top-brdr">
								<ul class="title-actn">
									<li class="title-icon"><span class="icon-main-menu">&nbsp;</span>
									</li>
									<li>Tab Bar Controls</li>
								</ul>
							</div>
							<div class="cont-wrp ">
								<ul class="cmnUpl mulplchk max-ht" id="multiplChk">
									<c:forEach items="${sessionScope.tabBarbuttonsList}" var="item">
										<li><img id="tabAbt" class="active" src="${item.imagePath}"> <input type="checkbox" class="tabBtn" value="${item.bottomBtnId}" name=""></li>
									</c:forEach>
								</ul>
							</div>
						</div>
						<div class="cont-block grey-bg">
							<div class="cont-wrp">
								<div class="zeroBg vertical-layout">
									<!--Iphone Preview For Login screen starts here-->
									<div class="vertical-scroll" id="moduleTabBars">
										<c:if test="${sessionScope.tabBarlst ne null && !empty sessionScope.tabBarlst}">
											<c:forEach items="${sessionScope.tabBarlst}" var="moduleTabBars">
												<div class="panel" id="${moduleTabBars.functionalityId}">
													<h3 class="pnl-title">
														${moduleTabBars.functionalityName} <span class="lst-cntrl"> <a class="lst-edit" title="edit" href="javascript:void(0);" moduleId="${moduleTabBars.functionalityId}">&nbsp;</a> <a class="lst-delete" title="delete" href="javascript:void(0);" onclick='saveModuleTabBar("Delete", "${moduleTabBars.functionalityId}");'>&nbsp;</a> </span>
													</h3>
													<ul class="btn-pnl">
														<c:forEach items="${moduleTabBars.tabBarList}" var="tabBarList">
															<li><img id="pre-tabStng" src="${tabBarList.imagePath}" btnId="${tabBarList.bottomBtnId}"></li> <!-- isMandatory="${tabBarList.isMandatory}" -->
														</c:forEach>
													</ul>
												</div>
											</c:forEach>
										</c:if>
									</div>
									<!--Iphone Preview For Login screen ends here-->
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!--Content panel div ends-->
		</div>
		<!--Content div ends-->
	</div>
</div> 