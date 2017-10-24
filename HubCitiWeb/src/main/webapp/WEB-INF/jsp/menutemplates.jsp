<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="menu" uri="/WEB-INF/hubcitileftmenu.tld"%>
<script type="text/javascript" src="scripts/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/jquery.form.js"></script>
<script type="text/javascript"
	src="/HubCiti/scripts/colorPickDynamic.js"></script>
<script type="text/javascript" src="/HubCiti/scripts/colorPicker.js"></script>
<script type="text/javascript">

function displayListView()
{
	document.menuDetails.menuTypeName.value = "List View";
	document.menuDetails.action = "setuplistmenu.htm";	
	document.menuDetails.method = "get";
	document.menuDetails.submit();
}

function displayGroupView()
{
	document.menuDetails.menuTypeName.value = "Grouped Tab";
	document.menuDetails.action = "setupgroupmenu.htm";	
	document.menuDetails.method = "get";
	document.menuDetails.submit();
}

function createiconincTemplate()
{
	document.menuDetails.action = "setupiconicmenu.htm";	
	document.menuDetails.method = "POST";
	document.menuDetails.submit();
}

function createComboTemplate()
{
	document.menuDetails.action = "setupcombomenu.htm";	
	document.menuDetails.method = "get";
	document.menuDetails.submit();
}
function displayTwoColView(template) {

	document.menuDetails.menuTypeName.value = template;
	document.menuDetails.action = "twocoltabview.htm";
	document.menuDetails.method = "GET";
	document.menuDetails.submit();
}

</script>
<link rel="stylesheet" type="text/css"
	href="/HubCiti/styles/colorPicker.css" />
<div class="wrpr-cont relative">
    <div id="slideBtn"><a href="#" onclick="revealPanel(this);" title="Hide Menu"><img src="images/slide_off.png" width="11" height="28" alt="btn_off" /></a></div>
    <div id="bread-crumb">
      <ul>
        <li class="scrn-icon"><span class="icon-home">&nbsp;</span></li>
        <li>Home</li>
        <li class="last">Select Template</li>
      </ul>
    </div>
    <span class="blue-brdr"></span>
    <div class="content" id="login">
      <div id="menu-pnl" class="split">
			<jsp:include page="leftNavigation.jsp"></jsp:include>
	  </div>
      <div class="cont-pnl split" id="equalHt">
        <div class="">
          <div class="title-bar">
           <ul class="title-actn">
              <li class="title-icon"><span class="icon-main-menu">&nbsp;</span></li>
              <li>Select Template</li>
            </ul>
          </div>
          <form:form name="menuDetails" commandName="menuDetails">
         	<form:hidden path="menuTypeName"/>
         	<form:hidden path="level"/>
         	<form:hidden path="menuId"/>
         	<form:hidden path="menuName"/>
         	<input type="hidden" name="hidMenuType" id="hidMenuType" value="${requestScope.menuType}" />
	            <div class="cont-wrp grey-bg">

	          <p class="mrngBtm_small">Click on the templates to select</p>
	            <ul class="grdLst">
	              <li><span class="overlayInfo">Grouped Tab With/Without Image Template</span><img src="images/tmplt-sngle.png" width="200" height="300" alt="template" />
	                <input type="radio" name="tmpltOptn" id="singleTab" class="tmpltOptn" btnType="Grouped Tab Template"/>
	                <span class="tmpltDsbl"></span>
	                <a href="javascript:void(0);" onclick="displayGroupView();">Next</a>
	              </li>
	              <li><span class="overlayInfo">Single/Two Column Tab Template </span><img src="images/tmpltImg.png" width="200" height="300" alt="template" />
	                <input type="radio" name="tmpltOptn" id="twinTab" class="tmpltOptn" btnType="2 Column Tab Template"/>
	                 <span class="tmpltDsbl"></span>
	               <a href="javascript:void(0);" onclick="displayTwoColView('Single/Two Column Tab');">Next</a>
	              </li>
				    
				   <li><span class="overlayInfo">Two Column Tab & Banner Template </span><img src="images/twintab_bnrBg.png" width="200" height="300" alt="template" />
                <input type="radio" name="tmpltOptn" id="twinTabBnr" class="tmpltOptn" btnType="2 Column Tab Template"/>
                 <span class="tmpltDsbl"></span>
                 <a href="javascript:void(0);" onclick="displayTwoColView('Two Column Tab with Banner Ad')" >Next</a>
              </li>  
	              <li><span class="overlayInfo">List View Template</span><img src="images/lstViewBg.png" width="200" height="300" alt="template" />
	                <input type="radio" name="tmpltOptn" id="listView" class="tmpltOptn" btnType="List View Template"/>
	                 <span class="tmpltDsbl"></span>
	                 <a href="javascript:void(0);" onclick="displayListView();">Next</a>
	              </li>
	              <li><span class="overlayInfo">Iconic Grid Template</span>
	              <img src="images/grdViewBg.png" width="200" height="300" alt="template" />
	                <input type="radio" name="tmpltOptn" id="gridView" class="tmpltOptn" btnType="Iconic Grid Template"/>
	                 <span class="tmpltDsbl"></span>
	                 <a href="#" onclick="createiconincTemplate();">Next</a>
	              </li>
	              <li><span class="overlayInfo">Combo Template</span>
	              <img src="images/comboBg.png" width="200" height="300" alt="template" />
	                <input type="radio" name="tmpltOptn" id="gridView" class="tmpltOptn" btnType="Combo Template"/>
	                 <span class="tmpltDsbl"></span>
	                 <a href="javascript:void(0);" onclick="createComboTemplate();">Next</a>
	              </li>
	            </ul>
	          </div>
	       </form:form>
        </div>
      </div>
    </div>
  </div>
  
  <script type="text/javascript">
	var menuType = '${requestScope.menuType}';
	if (menuType == 'mainmenu') {
		configureMenu("setupmainmenu");
	} else {

		configureMenu("setupsubmenu");
	}
</script>