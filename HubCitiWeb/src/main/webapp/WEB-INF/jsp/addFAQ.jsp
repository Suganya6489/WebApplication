<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<script src="/HubCiti/scripts/ckeditor/ckeditor.js"></script>

<script type="text/javascript">

$(document).ready(function() {

	var faqID=$("#faqID").val();
	
	if(faqID != "")
	{
		document.title = "Update FAQ";		
		$("#FAQTitle").text("Update FAQ");			
		$("#savefaq").val("Update FAQ");	
		$("#faqname").text("Update FAQ");		
	}
	
	$(".modal-close").on('click.popup', function() {
		$(".modal-popupWrp").hide();
		$(".modal-popup").slideUp();
		$(".modal-popup").find(".modal-bdy input").val("");
		$(".modal-popup i").removeClass("errDsply");
	});
	
	CKEDITOR
								.on(
										'instanceCreated',
										function(e) {
											//	alert("q : "+e.editor.name);
											var editorName = e.editor.name;
											$("#contentPreview").text(
													"Content goes here..");

											e.editor
													.on(
															'change',
															function(ev) {

																document
																		.getElementById('contentPreview').innerHTML = ev.editor
																		.getData();

															});
										});

						CKEDITOR.config.uiColor = '#FFFFFF';
						CKEDITOR.replace('answer',
								{
									extraPlugins : 'onchange',
									width : "100%",
									toolbar : [

											{
												name : 'basicstyles',
												items : [ 'Bold', 'Italic',
														'Underline' ]
											},
											{
												name : 'paragraph',
												items : [ 'JustifyLeft',
														'JustifyCenter',
														'JustifyRight',
														'JustifyBlock' ]
											},
											{
												name : 'colors',
												items : [ 'BGColor' ]
											},
											{
												name : 'paragraph',
												items : [ 'Outdent', 'Indent' ]
											},
											{
												name : 'links',
												items : [ 'Link', 'Unlink' ]
											},
											'/',
											{
												name : 'styles',
												items : [ 'Styles', 'Format' ]
											},
											{
												name : 'tools',
												items : [ 'Font', 'FontSize',
														'RemoveFormat' ]
											} ],
									removePlugins : 'resize'
								});

						var answer = $("#answer").val();

						if (answer == "") {
							document.getElementById('contentPreview').innerHTML = "Content goes here..";
						} else {
							document.getElementById('contentPreview').innerHTML = answer
						}
					});

	function saveFAQs() {
		document.addfaq.faqLowerLimit.value = '${requestScope.lowerLimit}';
		document.addfaq.action = "savefaq.htm";
		document.addfaq.method = "POST";
		document.addfaq.submit();
	}

	function clearFormFields() {
		var r = confirm("Do you really want to clear the form?");
		if (r == true) {
			document.addfaq.question.value = "";
			document.addfaq.answer.value = "";
			/* Added to clear content inside editor */
			$("iframe").contents().find("body").html(''); //$("#cke_contents_answer").html("");
			document.addfaq.faqCatId.value = "";
			
			if (document.getElementById('question.errors') != null) {
				document.getElementById('question.errors').style.display = 'none';
			}
			if (document.getElementById('answer.errors') != null) {
				document.getElementById('answer.errors').style.display = 'none';
			}
			if (document.getElementById('faqCatId.errors') != null) {
				document.getElementById('faqCatId.errors').style.display = 'none';
			}
		}
	}
</script>

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
				<li><a href="displayfaq.htm">Setup FAQ's</a></li>
				<li class="last" id="FAQTitle">Add FAQ</li>
			</ul>
		</div>
		<span class="blue-brdr"></span>
		<div class="content" id="login">
			<div id="menu-pnl" class="split">
				<jsp:include page="leftNavigation.jsp"></jsp:include>
			</div>
			<div class="cont-pnl split" id="">
				<div class="cont-block stretch">
					<div class="title-bar">
						<ul class="title-actn">
							<li class="title-icon"><span class="icon-faq">&nbsp;</span></li>
							<li id="faqname">Add FAQ</li>
						</ul>
					</div>
					<span class="clear"></span>
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
										<div class="alertBx failure mrgnTop cntrAlgn">
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
					<div class="cont-wrp mrgnBtm_small">
						<form:form name="addfaq" id="addfaq" commandName="addfaq">
							<form:hidden path="faqLowerLimit" />
							<form:hidden path="faqSearchKey" />
							<form:hidden path="faqID" id="faqID"/>
							<table width="100%" border="0" cellspacing="0" cellpadding="0" class="cmnTbl">
								<tr>
									<td width="22%"><label class="mand">Category</label></td>
									<td width="32%">
										<form:errors path="faqCatId" cssStyle="color:red"></form:errors>
										<div class="cntrl-grp zeroBrdr">
											<span class="lesWdth widthFix"> 
												<form:select path="faqCatId" cssClass="slctBx">
													<form:option value="">--Select--</form:option>
													<c:forEach items="${sessionScope.faqCategories}" var="faqCategory">
														<form:option value="${faqCategory.faqCatId}">${faqCategory.faqCatName}</form:option>
													</c:forEach>
												</form:select>
											</span> 
											<a href="#" onclick="showFAQModal(this)" viewName="addFAQ" title="Add FAQ's Category">
												<img width="24" height="24" src="images/btn_add.png" alt="add" class="addElem" >
											</a>
										</div>
									</td>
									<td width="22%">&nbsp;</td>
									<td width="24%" align="right">&nbsp;</td>
								</tr>
								<tr>
									<td><label class="mand">Question</label></td>
									<td colspan="3">
										<form:errors path="question" cssStyle="color:red"></form:errors>
										<div class="cntrl-grp">
											<form:textarea path="question" rows="5" cols="25" cssClass="textareaTxt" maxlength="1000"></form:textarea>
										</div>
									</td>
								</tr>
								<tr>
									<td><label class="mand">Answer</label></td>
									<td colspan="3">
										<form:errors path="answer" cssStyle="color:red"></form:errors>
										<div class=""> <!-- <div class="cntrl-grp"> -->
											<form:textarea path="answer" id="answer" rows="5" cols="25" cssClass="textareaTxt" maxlength="5000"></form:textarea>
										</div>
									</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td class="multi-inputs">
										<input type="button" name="button" id="savefaq" value="Save FAQ" class="btn-blue" onclick="saveFAQs();" />
										<input type="button" name="button" value="Clear" class="btn-grey" onclick="clearFormFields();" />
									</td>
									<td>&nbsp;</td>
									<td align="right">&nbsp;</td>
								</tr>
							</table>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="modal-popupWrp">
	<div class="modal-popup">
		<div class="modal-hdr">
			<a class="modal-close" title="close">×</a>
			<h3>Add Category</h3>
		</div>
		<div class="modal-bdy">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td width="30%"><label class="mand">Category Name</label></td>
					<td width="56%"><div class="cntrl-grp">
							<input type="text" id="ctgryNm" class="req inputTxtBig" maxlength="20" />
						</div> <i class="emptyctgry">Please Enter Category Name</i>
						<p class="dupctgry">Category Name already exists</p>
					</td>
				</tr>
			</table>
		</div>
		<div class="modal-ftr">
			<p align="right">
				<input type="submit" class="btn-blue" value="Save Category" title="Save category"> 
				<input type="reset" class="btn-grey" value="Clear" id="" title="Clear category" name="Reset" onclick="clearCategory()">
			</p>
		</div>
	</div>
</div>
<script type="text/javascript">
	configureMenu("setupfqa");
</script>
