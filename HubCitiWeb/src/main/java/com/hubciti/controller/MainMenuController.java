/**
 * 
 */
package com.hubciti.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.exception.HubCitiServiceException;
import com.hubciti.common.pojo.AlertCategory;
import com.hubciti.common.pojo.AnythingPages;
import com.hubciti.common.pojo.AppSiteDetails;
import com.hubciti.common.pojo.ButtonDetails;
import com.hubciti.common.pojo.Category;
import com.hubciti.common.pojo.CityExperience;
import com.hubciti.common.pojo.Department;
import com.hubciti.common.pojo.GroupTemplate;
import com.hubciti.common.pojo.MenuDetails;
import com.hubciti.common.pojo.MenuFilterTyes;
import com.hubciti.common.pojo.ScreenSettings;
import com.hubciti.common.pojo.SubMenuDetails;
import com.hubciti.common.pojo.Type;
import com.hubciti.common.pojo.User;
import com.hubciti.common.util.Utility;
import com.hubciti.service.HubCitiService;
import com.hubciti.validator.ButtomBarButtonValidator;
import com.hubciti.validator.GroupAndListMenuValidation;
import com.hubciti.validator.IconinMenuValidator;
import com.hubciti.validator.TwoColTabValidator;

/**
 * This class is a Controller class for HubCiti Main Menu Setup.
 * 
 * @author sangeetha.ts
 */
@Controller
public class MainMenuController {
	/**
	 * Getting the logger Instance.
	 */

	private static final Logger LOG = LoggerFactory
			.getLogger(MainMenuController.class);

	private GroupAndListMenuValidation groupAndListMenuValidation;

	IconinMenuValidator iconinMenuValidator;

	private ButtomBarButtonValidator buttomBarButtonValidator;

	@Autowired
	public void setIconinMenuValidator(IconinMenuValidator iconinMenuValidator) {
		this.iconinMenuValidator = iconinMenuValidator;
	}

	TwoColTabValidator twoColTabValidator;

	@Autowired
	public void setTwoColTabValidator(TwoColTabValidator twoColTabValidator) {
		this.twoColTabValidator = twoColTabValidator;
	}

	@Autowired
	public void setButtomBarButtonValidator(
			ButtomBarButtonValidator buttomBarButtonValidator) {
		this.buttomBarButtonValidator = buttomBarButtonValidator;
	}

	/**
	 * @param groupAndListMenuValidation
	 *            the groupAndListMenuValidation to set
	 */
	@Autowired
	public void setGroupAndListMenuValidation(
			GroupAndListMenuValidation groupAndListMenuValidation) {
		this.groupAndListMenuValidation = groupAndListMenuValidation;
	}

	public final String ASSOCIATEDEPT = "Associate Dept";

	public final String ASSOCIATETYPE = "Associate Type";

	public final String UPLOADIMAGE = "Upload Image";

	public final String ASSOCIATECITY = "Associate City";

	/**
	 * This Method will return the main menu Screen.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/displaymenutemplate.htm", method = RequestMethod.GET)
	public String displayMainMenu(HttpServletRequest request, ModelMap model,
			HttpSession session) throws HubCitiServiceException {
		String methodName = "displayMainMenu";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		MenuFilterTyes menuFilterTyes = null;
		MenuDetails menuDetails = new MenuDetails();
		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");
		session.removeAttribute("filterDeptList");
		session.removeAttribute("filterTypeList");
		session.removeAttribute("menuFilterType");
		User user = (User) session.getAttribute("loginUser");
		SubMenuDetails subMenuDetailsObj = null;
		List<MenuDetails> subMenuList = null;
		AnythingPages anythingPageList = null;
		List<Category> businessCategoryList = null;
		ScreenSettings subMenuLstObj = new ScreenSettings();
		List<ScreenSettings> btnTypeList = null;
		List<ScreenSettings> tabBarButtonsList = null;

		AlertCategory eventCategoryList = null;

		AlertCategory fundraiserCategoryLst = null;

		if (request.getParameter("menuType").equals(
				ApplicationConstants.MAINMENUPARAM)) {
			menuDetails.setLevel(1);
			request.setAttribute("menuType", ApplicationConstants.MAINMENUPARAM);
		}
		// for submenu start...
		else if (request.getParameter("menuType").equals(
				ApplicationConstants.SUBMENU)) {
			menuDetails.setLevel(null);
			request.setAttribute("menuType", ApplicationConstants.SUBMENU);
			menuFilterTyes = hubCitiService.getMenuFilterTypes(user
					.getHubCitiID());

			session.setAttribute("filterDeptList",
					menuFilterTyes.getDeptNameList());
			session.setAttribute("filterTypeList",
					menuFilterTyes.getTypeNameList());

			// Adding below code for submenu changes implementation.
			List<MenuDetails> linkList = hubCitiService.getLinkList();
			for (MenuDetails link : linkList) {

				if ("SubMenu".equals(link.getMenuTypeVal())) {
					session.setAttribute("subMenuFctnId", link.getMenuTypeId());
					continue;
				}
				if ("AnythingPage".equals(link.getMenuTypeVal())) {
					session.setAttribute("anythingPageFctnId",
							link.getMenuTypeId());
					continue;

				}
				if ("AppSite".equals(link.getMenuTypeVal())) {
					session.setAttribute("appSiteFctnId", link.getMenuTypeId());
					continue;
				}

				if ("Text".equals(link.getMenuTypeVal())) {
					session.setAttribute("textFctnId", link.getMenuTypeId());
					continue;
				}
				if ("Label".equals(link.getMenuTypeVal())) {
					session.setAttribute("labelFctnId", link.getMenuTypeId());
					continue;
				}
				if ("City Experience".equals(link.getMenuTypeVal())) {
					session.setAttribute("expFctnId", link.getMenuTypeId());
					continue;
				}
				if ("Find".equals(link.getMenuTypeVal())) {
					session.setAttribute("findFctnId", link.getMenuTypeId());
					continue;
				}
				if ("Events".equals(link.getMenuTypeVal())) {
					session.setAttribute("eventFctnId", link.getMenuTypeId());
					continue;
				}
				if ("Fundraisers".equals(link.getMenuTypeVal())) {
					session.setAttribute("fundraFctnId", link.getMenuTypeId());
					continue;
				}
			}

			// fetching submenu list.
			subMenuDetailsObj = hubCitiService.displaySubMenu(user,
					subMenuLstObj);
			if (null != subMenuDetailsObj) {
				subMenuList = subMenuDetailsObj.getSubMenuList();
			}
			if (null != subMenuList) {
				session.setAttribute("subMenuList", subMenuList);
			}

			anythingPageList = hubCitiService.displayAnythingPage(user, null,
					null);

			if (null != anythingPageList) {
				session.setAttribute("anythingPageList",
						anythingPageList.getPageDetails());
			} else {
				session.setAttribute("anythingPageList", null);
			}

			businessCategoryList = hubCitiService.fetchBusinessCategoryList();
			session.setAttribute("businessCatList", businessCategoryList);

			for (int i = 0; i < linkList.size(); i++) {
				MenuDetails link = linkList.get(i);
				if ("Text".equals(link.getMenuTypeName())
						|| "Label".equals(link.getMenuTypeName())) {
					linkList.remove(i);

				}

			}
			session.setAttribute("linkList", linkList);
			tabBarButtonsList = hubCitiService.getTabBarButtons(null, user);
			session.setAttribute("tabBarButtonsList", tabBarButtonsList);

			// Start:Added code for Task:Event Category Changes
			eventCategoryList = hubCitiService.fetchEventCategories(null, user);
			if (null != eventCategoryList
					&& !eventCategoryList.getAlertCatLst().isEmpty()) {
				session.setAttribute("eventCatList",
						eventCategoryList.getAlertCatLst());
			} else {
				List<Category> eventCatLst = new ArrayList<Category>();
				session.setAttribute("eventCatList", eventCatLst);
			}
			// End: Added code for Task:Event Category Changes

			// Start :Adding code for displaying fundraiser categories

			fundraiserCategoryLst = hubCitiService
					.fetchFundraiserEventCategories(user);

			if (null != fundraiserCategoryLst
					&& !fundraiserCategoryLst.getAlertCatLst().isEmpty()) {
				session.setAttribute("fundraiserCatList",
						fundraiserCategoryLst.getAlertCatLst());

			} else {

				session.setAttribute("fundraiserCatList", null);

			}

			// End : Adding code for displaying fundraiser categories

			ScreenSettings subMenuUIDetails = null;
			subMenuUIDetails = hubCitiService.fetchGeneralSettings(
					user.getHubCitiID(), "SubMenu");
			if (null == subMenuUIDetails) {
				session.setAttribute("subMenuBG", "#d1d1d1");
				session.setAttribute("subMenuBtnClr", "#2B88D9");
				session.setAttribute("subMenuFntClr", "#FFFFFF");
				session.setAttribute("subMenuGrpClr", "#1A4A6E");
				session.setAttribute("subMenuGrpFntClr", "#FFFFFF");
				session.setAttribute("subMenuIconsFntClr", "#FFFFFF");
			} else {

				if (null != subMenuUIDetails.getBgColor()
						&& !"".equalsIgnoreCase(subMenuUIDetails.getBgColor())) {
					session.setAttribute("subMenuBGType", "Color");
					session.setAttribute("subMenuBG",
							subMenuUIDetails.getBgColor());
				}

				if (null != subMenuUIDetails.getLogoImageName()
						&& !"".equalsIgnoreCase(subMenuUIDetails
								.getLogoImageName())) {
					session.setAttribute("subMenuBGType", "Image");
					session.setAttribute("subMenuBG",
							subMenuUIDetails.getLogoPath());
				}
				session.setAttribute("subMenuBtnClr",
						subMenuUIDetails.getBtnColor());
				session.setAttribute("subMenuFntClr",
						subMenuUIDetails.getBtnFontColor());
				session.setAttribute("subMenuGrpClr",
						subMenuUIDetails.getGrpColor());
				session.setAttribute("subMenuGrpFntClr",
						subMenuUIDetails.getGrpFontColor());
				session.setAttribute("subMenuIconsFntClr",
						subMenuUIDetails.getIconsFontColor());

			}

		}
		// submenu end

		// Start:Added code for Task: Add Cities to menu buttons
		if ("RegionApp".equalsIgnoreCase((String) session
				.getAttribute("loginUserType"))) {
			List<CityExperience> citiesLst = hubCitiService
					.displayCitiesForRegionApp(user);
			session.setAttribute("citiesLst", citiesLst);
		}
		// End: Added code for Task: Add Cities to menu buttons

		btnTypeList = hubCitiService.getMenuButtonType();
		session.setAttribute("btnTypeList", btnTypeList);
		displayAppSites(request, session, model);

		if (null != request.getParameter("id")) {
			menuDetails.setMenuId(Integer.valueOf(request.getParameter("id")));
		}

		if (null != request.getParameter("menuname")
				&& !"".equals(request.getParameter("menuname"))) {
			menuDetails.setMenuName(request.getParameter("menuname"));
		}
		session.removeAttribute("previewMenuItems");
		session.removeAttribute("groupList");
		session.removeAttribute("groupedBtnList");
		session.removeAttribute("comboList");
		session.removeAttribute("comboBtnList");

		model.put("menuDetails", menuDetails);

		if (request.getParameter("menuType").equals(
				ApplicationConstants.SUBMENU)) {
			session.setAttribute(ApplicationConstants.MENUNAME,
					ApplicationConstants.SETUPSUBMENU);
		} else {
			session.setAttribute(ApplicationConstants.MENUNAME,
					ApplicationConstants.MAINMENUSCREEN);
		}
		// Default Image Path
		session.setAttribute("menuIconPreview",
				ApplicationConstants.DEFAULTIMAGE);

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "menutemplates";
	}

	/**
	 * This Method will return the List View Menu Screen.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/setuplistmenu.htm", method = RequestMethod.GET)
	public String setUpListMenu(
			@ModelAttribute("menuDetails") MenuDetails menuDetails,
			BindingResult result, HttpServletRequest request, ModelMap model,
			HttpSession session) throws HubCitiServiceException {
		String methodName = "setUpListMenu";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String menuId = null;
		String subMenuName = null;
		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");
		MenuDetails menuDetailsObj = null;
		session.removeAttribute("menuBarButtonsList");
		try {
			// Minimum crop height and width for login screen
			session.setAttribute("minCropHt", 40);
			session.setAttribute("minCropWd", 40);
			if (null != request.getParameter("hidMenuType")) {
				if (request.getParameter("hidMenuType").equals(
						ApplicationConstants.SUBMENU)) {
					session.setAttribute(ApplicationConstants.MENUNAME,
							ApplicationConstants.SETUPSUBMENU);
				} else {
					session.setAttribute(ApplicationConstants.MENUNAME,
							ApplicationConstants.MAINMENUSCREEN);
				}
			}
			// session.setAttribute(ApplicationConstants.MENUNAME,
			// ApplicationConstants.MAINMENUSCREEN);

			User user = (User) session.getAttribute("loginUser");

			menuDetailsObj = new MenuDetails();
			menuDetailsObj
					.setMenuTypeName(ApplicationConstants.LISTVIEWTEMPLATE);
			menuDetailsObj.setLevel(menuDetails.getLevel());

			if (null == menuDetails.getLevel()) {
				menuDetailsObj.setMenuName(null);

			} else {
				menuDetailsObj.setMenuName(ApplicationConstants.MAINMENU);

			}

			// Default Image Path
			session.setAttribute("menuIconPreview",
					ApplicationConstants.DEFAULTIMAGESQR);

			if (null != menuDetails.getMenuId()
					&& !"".equals(menuDetails.getMenuId())) {
				menuId = menuDetails.getMenuId().toString();
				subMenuName = menuDetails.getMenuName();
			} else {
				// Create Menu
				menuId = hubCitiService.createMenu(menuDetailsObj, user);

			}

			ScreenSettings screenSettings = new ScreenSettings();
			screenSettings.setMenuId(menuId);
			screenSettings.setMenuLevel(menuDetails.getLevel());
			screenSettings.setSubMenuName(subMenuName);
			// screenSettings.setMenuId(menuId);
			model.put("screenSettingsForm", screenSettings);

		} catch (HubCitiServiceException exception) {

			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);

		return "setuplistmenu";
	}

	public static List<AppSiteDetails> displayAppSites(
			HttpServletRequest request, HttpSession session, ModelMap model)
			throws HubCitiServiceException

	{
		final String strMethodName = "displayAppSites";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		List<AppSiteDetails> appSiteDetailsLst = null;
		int iHubCityId = 0;

		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");

		User user = (User) session.getAttribute("loginUser");
		if (null != user && !"".equals(user)) {
			iHubCityId = user.getHubCitiID();
		}

		appSiteDetailsLst = hubCitiService.getAppSites(null, iHubCityId, null);
		session.setAttribute("appsitelst", appSiteDetailsLst);
		return appSiteDetailsLst;
	}

	/**
	 * This Method will return Group menu Screen.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/setupgroupmenu.htm", method = RequestMethod.GET)
	public String setUpGroupMenu(
			@ModelAttribute("menuDetails") MenuDetails menuDetails,
			BindingResult result, HttpServletRequest request, ModelMap model,
			HttpSession session) throws HubCitiServiceException {
		String methodName = "setUpGroupMenu";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String menuId = null;
		String subMenuName = null;
		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");
		MenuDetails menuDetailsObj = null;
		session.removeAttribute("menuBarButtonsList");
		try {
			session.setAttribute("minCropHt", 40);
			session.setAttribute("minCropWd", 40);
			if (null != request.getParameter("hidMenuType")) {
				if (request.getParameter("hidMenuType").equals(
						ApplicationConstants.SUBMENU)) {
					session.setAttribute(ApplicationConstants.MENUNAME,
							ApplicationConstants.SETUPSUBMENU);
				} else {
					session.setAttribute(ApplicationConstants.MENUNAME,
							ApplicationConstants.MAINMENUSCREEN);
				}
			}
			// session.setAttribute(ApplicationConstants.MENUNAME,
			// ApplicationConstants.MAINMENUSCREEN);

			User user = (User) session.getAttribute("loginUser");

			menuDetailsObj = new MenuDetails();
			menuDetailsObj
					.setMenuTypeName(ApplicationConstants.GROUPEDTABTEMPLATE);
			menuDetailsObj.setLevel(menuDetails.getLevel());

			if (null == menuDetails.getLevel()) {
				menuDetailsObj.setMenuName(null);

			} else {
				menuDetailsObj.setMenuName(ApplicationConstants.MAINMENU);

			}

			if (null != menuDetails.getMenuId()
					&& !"".equals(menuDetails.getMenuId())) {
				menuId = menuDetails.getMenuId().toString();
				subMenuName = menuDetails.getMenuName();
			} else {
				// Create Menu
				menuId = hubCitiService.createMenu(menuDetailsObj, user);
			}

			// Default Image Path
			session.setAttribute("menuIconPreview",
					ApplicationConstants.DEFAULTIMAGESQR);

			ScreenSettings screenSettings = new ScreenSettings();
			screenSettings.setMenuId(menuId);
			screenSettings.setMenuLevel(menuDetails.getLevel());
			screenSettings.setSubMenuName(subMenuName);
			screenSettings.setIsGroupImg("false");
			// screenSettings.setMenuId(menuId);
			model.put("screenSettingsForm", screenSettings);

		} catch (HubCitiServiceException exception) {

			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);

		return "setupgroupmenu";
	}

	/**
	 * This Method will return Group menu Screen.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/creategroupmenu.htm", method = RequestMethod.POST)
	public String createGroupButton(
			@ModelAttribute("screenSettingsForm") ScreenSettings screenSettings,
			BindingResult result, HttpServletRequest request, ModelMap model,
			HttpSession session) throws HubCitiServiceException {
		String methodName = "createGroupButton";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		// session.setAttribute(ApplicationConstants.MENUNAME,
		// ApplicationConstants.MAINMENUSCREEN);
		String isGroupImage = screenSettings.getIsGroupImg();
		List<String> groupList = new ArrayList<String>();
		String grpList = screenSettings.getGrpList();
		String buttonName = screenSettings.getMenuBtnName();
		String menuId = screenSettings.getMenuId();
		String subMenuName = screenSettings.getSubMenuName();
		Integer menuLevel = screenSettings.getMenuLevel();
		String btnId = screenSettings.getMenuIconId();
		String viewName = screenSettings.getViewName();
		String addorDeleteMenuItem = screenSettings.getAddDeleteBtn();
		String menuFunctionlity = screenSettings.getMenuFucntionality();
		screenSettings
				.setHiddenmenuFnctn(screenSettings.getMenuFucntionality());
		screenSettings.setHiddenBtnLinkId(screenSettings.getBtnLinkId());
		screenSettings.setHiddenbtnGroup(screenSettings.getBtnGroup());
		screenSettings.setHiddenCitiId(screenSettings.getCitiId());
		ScreenSettings grpMenuItem = null;
		String selGrpName = screenSettings.getBtnGroup();
		ScreenSettings menuItem = null;
		GroupTemplate groupTemplate = null;
		String grpName = screenSettings.getGrpName();
		List<ScreenSettings> grpBtnsList = null;
		Integer subMenuFctnId = (Integer) session.getAttribute("subMenuFctnId");
		Integer anythingPageFctnId = (Integer) session
				.getAttribute("anythingPageFctnId");
		Integer appSiteFctnId = (Integer) session.getAttribute("appSiteFctnId");
		Integer findFctnId = (Integer) session.getAttribute("findFctnId");
		Integer eventFctnId = (Integer) session.getAttribute("eventFctnId");
		Integer fundraFctnId = (Integer) session.getAttribute("fundraFctnId");
		// for subcategories
		screenSettings.setHiddenSubCate(screenSettings.getChkSubCate());
		screenSettings.setChkSubCate(screenSettings.getChkSubCate());
		if (null != screenSettings.getMenuFucntionality()
				&& screenSettings.getMenuFucntionality().equals(
						String.valueOf(findFctnId))
				&& null != screenSettings.getBtnLinkId()) {
			String[] findCat = screenSettings.getBtnLinkId().split(",");
			String findCatList = null;

			for (String cat : findCat) {
				if (cat.contains("MC")) {
					if (null != findCatList) {
						findCatList += cat + ",";
					} else {
						findCatList = new String();
						findCatList = cat + ",";
					}
				}
			}
			if (findCatList.endsWith(",")) {
				findCatList = findCatList
						.substring(0, findCatList.length() - 1);
			}
			screenSettings.setBtnLinkId(findCatList);
		}

		if (null != request.getParameter("hidMenuType")) {
			if (request.getParameter("hidMenuType").equals(
					ApplicationConstants.SUBMENU)) {
				session.setAttribute(ApplicationConstants.MENUNAME,
						ApplicationConstants.SETUPSUBMENU);
			} else {
				session.setAttribute(ApplicationConstants.MENUNAME,
						ApplicationConstants.MAINMENUSCREEN);
			}
		}

		List<GroupTemplate> groupedBtnList = (ArrayList<GroupTemplate>) session
				.getAttribute("groupedBtnList");
		request.setAttribute("gprdMenuAction", "Button");
		// List of Menu items
		List<ScreenSettings> previewMenuItems = new ArrayList<ScreenSettings>();

		// Below Code is for Re-Ordering menu buttons
		String btnOrderArry[] = null;
		List<String> btnOrderListFixed = new ArrayList<String>();
		List<String> btnOrderList = new ArrayList<String>();
		String btnOrder = screenSettings.getBtnPosition();
		List<ScreenSettings> sortedMenuItems = new ArrayList<ScreenSettings>();
		if (!"".equals(Utility.checkNull(btnOrder))) {
			btnOrderArry = btnOrder.split("~");
			btnOrderListFixed = Arrays.asList(btnOrderArry);
			btnOrderList.addAll(btnOrderListFixed);

		}

		// below code is for storing the group list created by user
		if (!"".equals(Utility.checkNull(grpList))) {
			String[] grpNameList = grpList.split("~");
			for (int i = 0; i < grpNameList.length; i++) {
				groupList.add(grpNameList[i]);
			}
			session.setAttribute("groupList", groupList);
		}

		// Code for SubMenu Filters Implementation
		String[] menuFilterType = screenSettings.getMenuFilterType();

		if (null != menuFilterType) {
			if (menuFilterType.length != 0 || screenSettings.isEditFilter()) {

				session.setAttribute("menuFilterType", menuFilterType);
				screenSettings.setEditFilter(false);
			} else {

				menuFilterType = (String[]) session
						.getAttribute("menuFilterType");
				screenSettings.setMenuFilterType(menuFilterType);
			}

		}

		List<Type> filterTypeList = (ArrayList<Type>) session
				.getAttribute("filterTypeList");
		List<Department> filterDeptList = (ArrayList<Department>) session
				.getAttribute("filterDeptList");

		if (null != filterTypeList) {
			Type newType = null;
			if (!"0".equals(screenSettings.getBtnType())) {
				if (filterTypeList.size() != 0) {

					for (int i = 0; i < filterTypeList.size(); i++) {
						Type type = filterTypeList.get(i);

						if (type.getTypeName().equals(
								screenSettings.getBtnType())) {

							break;
						}

						if (i == filterTypeList.size() - 1) {
							newType = new Type();
							newType.setTypeName(screenSettings.getBtnType());
							filterTypeList.add(newType);
							session.setAttribute("filterTypeList",
									filterTypeList);
						}
					}
				} else {
					newType = new Type();
					newType.setTypeName(screenSettings.getBtnType());
					filterTypeList.add(newType);
					session.setAttribute("filterTypeList", filterTypeList);

				}

			}

		}

		if (null != filterDeptList) {
			Department newDept = null;
			if (!"0".equals(screenSettings.getBtnDept())) {
				if (filterDeptList.size() != 0) {

					for (int i = 0; i < filterDeptList.size(); i++) {
						Department dept = filterDeptList.get(i);

						if (dept.getDeptName().equals(
								screenSettings.getBtnDept())) {

							break;
						}

						if (i == filterDeptList.size() - 1) {
							newDept = new Department();
							newDept.setDeptName(screenSettings.getBtnDept());
							filterDeptList.add(newDept);
							session.setAttribute("filterDeptList",
									filterDeptList);
						}
					}
				} else {
					newDept = new Department();
					newDept.setDeptName(screenSettings.getBtnDept());
					filterDeptList.add(newDept);
					session.setAttribute("filterDeptList", filterDeptList);

				}

			}

		}

		displayAppSites(request, session, model);

		// Code for Adding menu item
		if (null != addorDeleteMenuItem
				&& "AddButton".equals(addorDeleteMenuItem)) {

			request.setAttribute("gprdMenuAction", "Button");

			// Set functionality type based on the value selected by the user,
			// this is used only for SubMenu,Appsite,AnythinPage and
			// Find
			if (null != screenSettings.getMenuFucntionality()
					&& !"".equals(screenSettings.getMenuFucntionality())) {
				if (screenSettings.getMenuFucntionality().equals(
						String.valueOf(subMenuFctnId))) {
					screenSettings
							.setFunctionalityType(ApplicationConstants.SUBMENU);
				} else if (screenSettings.getMenuFucntionality().equals(
						String.valueOf(appSiteFctnId))) {
					screenSettings
							.setFunctionalityType(ApplicationConstants.APPSITE);
				} else if (screenSettings.getMenuFucntionality().equals(
						String.valueOf(anythingPageFctnId))) {
					screenSettings
							.setFunctionalityType(ApplicationConstants.ANYTHINGPAGE);
				} else if (screenSettings.getMenuFucntionality().equals(
						String.valueOf(findFctnId))) {
					screenSettings
							.setFunctionalityType(ApplicationConstants.FIND);
					screenSettings.setHiddenSubCate(screenSettings
							.getChkSubCate());
					screenSettings
							.setChkSubCate(screenSettings.getChkSubCate());
				} else if (screenSettings.getMenuFucntionality().equals(
						String.valueOf(eventFctnId))) {
					screenSettings
							.setFunctionalityType(ApplicationConstants.SETUPEVENTS);
				}else if (screenSettings.getMenuFucntionality().equals(
						String.valueOf(fundraFctnId))) {

					screenSettings
							.setFunctionalityType(ApplicationConstants.FUNDRAISERS);
					screenSettings.setHiddenFundEvtId(screenSettings.getBtnLinkId());
					
					}			
				else {

					screenSettings.setBtnLinkId(null);
					screenSettings.setHiddenBtnLinkId(null);
					screenSettings.setHiddenSubCate(null);
					screenSettings.setChkSubCate(null);
				}

			}

			// Validate menu item details
			groupAndListMenuValidation.validate(screenSettings, result);

			if (result.hasErrors()) {
				screenSettings.setBtnLinkId(null);
				screenSettings.setChkSubCate(null);
				return viewName;
			} else {
				// If menuItems list is empty,create list, add menu item
				// to list
				// and store in session
				if (null == groupedBtnList
						|| (null != groupedBtnList && groupedBtnList.isEmpty())) {
					// Holds grouped menu template data
					groupedBtnList = new ArrayList<GroupTemplate>();

					// holds button details of a group
					grpBtnsList = new ArrayList<ScreenSettings>();

					// object for grouped menu template
					groupTemplate = new GroupTemplate();

					// set the group name for first group in the menu
					groupTemplate.setGrpName(selGrpName);

					// set button details
					grpMenuItem = new ScreenSettings();
					grpMenuItem.setMenuBtnName(screenSettings.getMenuBtnName()
							.trim());
					grpMenuItem.setMenuFucntionality(screenSettings
							.getMenuFucntionality());
					grpMenuItem.setHiddenmenuFnctn(screenSettings
							.getMenuFucntionality());
					grpMenuItem.setMenuIconId(ApplicationConstants.BUTTONID
							+ screenSettings.getMenuBtnName().trim());
					grpMenuItem.setBtnLinkId(screenSettings.getBtnLinkId());
					grpMenuItem.setHiddenBtnLinkId(screenSettings
							.getBtnLinkId());
					grpMenuItem.setSubCatIds(screenSettings.getSubCatIds());
					grpMenuItem
							.setHiddenSubCate(screenSettings.getChkSubCate());
					grpMenuItem.setChkSubCate(screenSettings.getChkSubCate());
					grpMenuItem.setCitiId(screenSettings.getCitiId());
					grpMenuItem.setBtnGroup(selGrpName);

					// Added to add Image in Grouped tab template
					if (isGroupImage != null
							&& "true".equalsIgnoreCase(isGroupImage)) {
						grpMenuItem.setLogoImageName(screenSettings
								.getLogoImageName());
						grpMenuItem
								.setImagePath(ApplicationConstants.TEMPIMGPATH
										+ screenSettings.getLogoImageName());
					} else {
						grpMenuItem.setLogoImageName(null);
						grpMenuItem.setImagePath(null);
					}

					if (!"0".equals(screenSettings.getBtnDept())) {
						grpMenuItem.setBtnDept(screenSettings.getBtnDept());

					} else {

						grpMenuItem.setBtnDept(null);
					}

					if (!"0".equals(screenSettings.getBtnType())) {
						grpMenuItem.setBtnType(screenSettings.getBtnType());

					} else {

						grpMenuItem.setBtnType(null);
					}

					grpBtnsList.add(grpMenuItem);
					groupTemplate.setGrpBtnsList(grpBtnsList);
					groupedBtnList.add(groupTemplate);

					session.setAttribute("groupedBtnList", groupedBtnList);
					session.setAttribute("menuIconPreview",
							ApplicationConstants.DEFAULTIMAGESQR);

				} else {
					// code for updating button details
					if (null != btnId && !"".equals(btnId)) {
						// old groupName is the name of group before update and
						// below code will be when user updates button details
						// without changing group
						if (screenSettings.getOldGroupName().equals(selGrpName)) {
							for (int i = 0; i < groupedBtnList.size(); i++) {
								GroupTemplate group = groupedBtnList.get(i);
								if (group.getGrpName().equals(selGrpName)) {
									List<ScreenSettings> btnList = group
											.getGrpBtnsList();

									for (int j = 0; j < btnList.size(); j++) {

										grpMenuItem = btnList.get(j);
										if (grpMenuItem.getMenuIconId().equals(
												btnId)) {
											for (int n = 0; n < btnList.size(); n++) {

												if (n != j) {
													ScreenSettings button = btnList
															.get(n);
													if (button.getMenuBtnName()
															.equals(buttonName)) {

														groupAndListMenuValidation
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATEBUTTONNAME);
														break;
													}

													if (button
															.getMenuFucntionality()
															.equals(String
																	.valueOf(subMenuFctnId))
															&& String
																	.valueOf(
																			subMenuFctnId)
																	.equals(menuFunctionlity)) {
														if (null != screenSettings
																.getBtnLinkId()
																&& !"".equals(screenSettings
																		.getBtnLinkId())) {
															if (button
																	.getBtnLinkId()
																	.equals(screenSettings
																			.getBtnLinkId())) {

																groupAndListMenuValidation
																		.validate(
																				screenSettings,
																				result,
																				ApplicationConstants.DUPLICATESUBMENU);
																break;
															}

														}

													}
													if (button
															.getMenuFucntionality()
															.equals(String
																	.valueOf(anythingPageFctnId))
															&& String
																	.valueOf(
																			anythingPageFctnId)
																	.equals(menuFunctionlity)) {
														if (null != screenSettings
																.getBtnLinkId()
																&& !"".equals(screenSettings
																		.getBtnLinkId())) {
															if (button
																	.getBtnLinkId()
																	.equals(screenSettings
																			.getBtnLinkId())) {

																groupAndListMenuValidation
																		.validate(
																				screenSettings,
																				result,
																				ApplicationConstants.DUPLICATEANYTHINGPAGE);
																break;
															}

														}

													}
													if (button
															.getMenuFucntionality()
															.equals(String
																	.valueOf(appSiteFctnId))
															&& String
																	.valueOf(
																			appSiteFctnId)
																	.equals(menuFunctionlity)) {
														if (null != screenSettings
																.getBtnLinkId()
																&& !"".equals(screenSettings
																		.getBtnLinkId())) {
															if (button
																	.getBtnLinkId()
																	.equals(screenSettings
																			.getBtnLinkId())) {

																groupAndListMenuValidation
																		.validate(
																				screenSettings,
																				result,
																				ApplicationConstants.DUPLICATEAPPSITE);
																break;
															}

														}

													}

													if (button
															.getMenuFucntionality()
															.equals(String
																	.valueOf(findFctnId))
															&& String
																	.valueOf(
																			findFctnId)
																	.equals(menuFunctionlity)) {
														if (null != screenSettings
																.getBtnLinkId()
																&& !"".equals(screenSettings
																		.getBtnLinkId())) {
															if (button
																	.getBtnLinkId()
																	.equals(screenSettings
																			.getBtnLinkId())) {
																if (button
																		.getChkSubCate()
																		.equals(screenSettings
																				.getChkSubCate())) {
																	if (button
																			.getBtnLinkId()
																			.contains(
																					",")) {
																		iconinMenuValidator
																				.validate(
																						screenSettings,
																						result,
																						ApplicationConstants.DUPLICATECATEGORIES);
																	} else {
																		iconinMenuValidator
																				.validate(
																						screenSettings,
																						result,
																						ApplicationConstants.DUPLICATECATEGORY);
																	}

																	break;
																}
															}
														}

													}

													if (button
															.getMenuFucntionality()
															.equals(String
																	.valueOf(eventFctnId))
															&& String
																	.valueOf(
																			eventFctnId)
																	.equals(menuFunctionlity)) {
														if (null != screenSettings
																.getBtnLinkId()
																&& !"".equals(screenSettings
																		.getBtnLinkId())) {
															if (button
																	.getBtnLinkId()
																	.equals(screenSettings
																			.getBtnLinkId())) {
																if (button
																		.getBtnLinkId()
																		.contains(
																				",")) {
																	iconinMenuValidator
																			.validate(
																					screenSettings,
																					result,
																					ApplicationConstants.DUPLICATECATEGORIES);
																} else {
																	iconinMenuValidator
																			.validate(
																					screenSettings,
																					result,
																					ApplicationConstants.DUPLICATECATEGORY);
																}

																break;
															}
														}

													}
													
													if (button
															.getMenuFucntionality()
															.equals(String
																	.valueOf(fundraFctnId))
															&& String
																	.valueOf(
																			fundraFctnId)
																	.equals(menuFunctionlity)) {
														if (null != screenSettings
																.getBtnLinkId()
																&& !"".equals(screenSettings
																		.getBtnLinkId())) {
															if (button
																	.getBtnLinkId()
																	.equals(screenSettings
																			.getBtnLinkId())) {
																if (button
																		.getBtnLinkId()
																		.contains(
																				",")) {
																	iconinMenuValidator
																			.validate(
																					screenSettings,
																					result,
																					ApplicationConstants.DUPLICATECATEGORIES);
																} else {
																	iconinMenuValidator
																			.validate(
																					screenSettings,
																					result,
																					ApplicationConstants.DUPLICATECATEGORY);
																}

																break;
															}
														}

													}



													if (button
															.getMenuFucntionality()
															.equals(screenSettings
																	.getMenuFucntionality())
															&& !button
																	.getMenuFucntionality()
																	.equals(String
																			.valueOf(subMenuFctnId))
															&& !button
																	.getMenuFucntionality()
																	.equals(String
																			.valueOf(anythingPageFctnId))
															&& !button
																	.getMenuFucntionality()
																	.equals(String
																			.valueOf(appSiteFctnId))
															&& !button
																	.getMenuFucntionality()
																	.equals(String
																			.valueOf(findFctnId))
															&& !button
																	.getMenuFucntionality()
																	.equals(String
																			.valueOf(eventFctnId))
																			
																			&& !button
																			.getMenuFucntionality()
																			.equals(String
																					.valueOf(fundraFctnId))
																	
															) {

														groupAndListMenuValidation
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATEFUNCTINALITY);
														break;
													}

												}

											}
											if (result.hasErrors()) {
												screenSettings
														.setBtnLinkId(null);
												screenSettings
														.setChkSubCate(null);
												return viewName;
											}
											grpMenuItem = new ScreenSettings();
											grpMenuItem
													.setMenuBtnName(screenSettings
															.getMenuBtnName()
															.trim());
											grpMenuItem
													.setMenuFucntionality(screenSettings
															.getMenuFucntionality());
											grpMenuItem
													.setHiddenmenuFnctn(screenSettings
															.getMenuFucntionality());
											grpMenuItem
													.setMenuIconId(ApplicationConstants.BUTTONID
															+ screenSettings
																	.getMenuBtnName()
																	.trim());
											grpMenuItem
													.setBtnLinkId(screenSettings
															.getBtnLinkId());
											grpMenuItem
													.setHiddenBtnLinkId(screenSettings
															.getBtnLinkId());
											grpMenuItem
													.setSubCatIds(screenSettings
															.getSubCatIds());
											grpMenuItem
													.setHiddenSubCate(screenSettings
															.getChkSubCate());
											grpMenuItem
													.setChkSubCate(screenSettings
															.getChkSubCate());
											grpMenuItem
													.setCitiId(screenSettings
															.getCitiId());
											grpMenuItem.setBtnGroup(selGrpName);

											// Added to add Image in Grouped tab
											// template
											if (isGroupImage != null
													&& "true"
															.equalsIgnoreCase(isGroupImage)) {
												grpMenuItem
														.setImagePath(ApplicationConstants.TEMPIMGPATH
																+ screenSettings
																		.getLogoImageName());
												// *** Need to look in image
												// selection implementation
												// grpMenuItem.setImagePath(screenSettings.getImagePath());
												grpMenuItem
														.setLogoImageName(screenSettings
																.getLogoImageName());
											} else {
												grpMenuItem.setImagePath(null);
												grpMenuItem
														.setLogoImageName(null);
											}

											if (!"0".equals(screenSettings
													.getBtnDept())) {
												grpMenuItem
														.setBtnDept(screenSettings
																.getBtnDept());

											} else {

												grpMenuItem.setBtnDept(null);
											}

											if (!"0".equals(screenSettings
													.getBtnType())) {
												grpMenuItem
														.setBtnType(screenSettings
																.getBtnType());

											} else {

												grpMenuItem.setBtnType(null);
											}

											btnList.remove(j);
											btnList.add(j, grpMenuItem);
											break;
										}
									}
									group.setGrpBtnsList(btnList);
									groupedBtnList.remove(i);
									groupedBtnList.add(i, group);
									break;
								}

							}

							if (!btnOrderList.isEmpty()) {
								for (int p = 0; p < btnOrderList.size(); p++) {

									if (btnOrderList.get(p).equals(btnId)) {
										btnOrderList
												.set(p,
														ApplicationConstants.BUTTONID
																+ screenSettings
																		.getMenuBtnName()
																		.trim());
										break;

									}

								}
							}
						} else {
							boolean newGrpFlag = false;
							List<ScreenSettings> btnList = null;
							// Below code will be executed when user changes
							// button group as well as button details
							for (int i = 0; i < groupedBtnList.size(); i++) {
								GroupTemplate group = groupedBtnList.get(i);
								if (group.getGrpName().equals(selGrpName)) {
									btnList = group.getGrpBtnsList();
									for (ScreenSettings button : btnList) {
										if (button.getMenuBtnName().equals(
												buttonName)) {

											groupAndListMenuValidation
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATEBUTTONNAME);
											break;
										}

										if (button
												.getMenuFucntionality()
												.equals(String
														.valueOf(subMenuFctnId))
												&& String
														.valueOf(subMenuFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (button
														.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {

													groupAndListMenuValidation
															.validate(
																	screenSettings,
																	result,
																	ApplicationConstants.DUPLICATESUBMENU);
													break;
												}

											}

										}
										if (button
												.getMenuFucntionality()
												.equals(String
														.valueOf(anythingPageFctnId))
												&& String
														.valueOf(
																anythingPageFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (null != button
														.getBtnLinkId()
														&& !"".equals(screenSettings
																.getBtnLinkId())) {
													if (button
															.getBtnLinkId()
															.equals(screenSettings
																	.getBtnLinkId())) {

														groupAndListMenuValidation
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATEANYTHINGPAGE);
														break;
													}
												}

											}

										}
										if (button
												.getMenuFucntionality()
												.equals(String
														.valueOf(appSiteFctnId))
												&& String
														.valueOf(appSiteFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (null != button
														.getBtnLinkId()
														&& !"".equals(screenSettings
																.getBtnLinkId())) {
													if (button
															.getBtnLinkId()
															.equals(screenSettings
																	.getBtnLinkId())) {

														groupAndListMenuValidation
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATEAPPSITE);
														break;
													}
												}

											}

										}
										if (button.getMenuFucntionality()
												.equals(String
														.valueOf(findFctnId))
												&& String
														.valueOf(findFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (button
														.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {
													if (button
															.getChkSubCate()
															.equals(screenSettings
																	.getChkSubCate())) {
														if (button
																.getBtnLinkId()
																.contains(",")) {
															iconinMenuValidator
																	.validate(
																			screenSettings,
																			result,
																			ApplicationConstants.DUPLICATECATEGORIES);
														} else {
															iconinMenuValidator
																	.validate(
																			screenSettings,
																			result,
																			ApplicationConstants.DUPLICATECATEGORY);
														}

														break;
													}
												}
											}

										}
										if (button.getMenuFucntionality()
												.equals(String
														.valueOf(eventFctnId))
												&& String
														.valueOf(eventFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (button
														.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {
													if (button.getBtnLinkId()
															.contains(",")) {
														iconinMenuValidator
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATECATEGORIES);
													} else {
														iconinMenuValidator
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATECATEGORY);
													}

													break;
												}
											}

										}
										
										if (button.getMenuFucntionality()
												.equals(String
														.valueOf(fundraFctnId))
												&& String
														.valueOf(fundraFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (button
														.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {
													if (button.getBtnLinkId()
															.contains(",")) {
														iconinMenuValidator
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATECATEGORIES);
													} else {
														iconinMenuValidator
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATECATEGORY);
													}

													break;
												}
											}

										}


										if (button
												.getMenuFucntionality()
												.equals(screenSettings
														.getMenuFucntionality())
												&& !button
														.getMenuFucntionality()
														.equals(String
																.valueOf(subMenuFctnId))
												&& !button
														.getMenuFucntionality()
														.equals(String
																.valueOf(anythingPageFctnId))
												&& !button
														.getMenuFucntionality()
														.equals(String
																.valueOf(appSiteFctnId))
												&& !button
														.getMenuFucntionality()
														.equals(String
																.valueOf(findFctnId))
												&& !button
														.getMenuFucntionality()
														.equals(String
																.valueOf(eventFctnId))
																&& !button
																.getMenuFucntionality()
																.equals(String
																		.valueOf(fundraFctnId))
																	
												
												) {

											groupAndListMenuValidation
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATEFUNCTINALITY);
											break;
										}

									}
									if (result.hasErrors()) {
										screenSettings.setBtnLinkId(null);
										screenSettings.setChkSubCate(null);
										return viewName;
									}

									grpMenuItem = new ScreenSettings();
									grpMenuItem.setMenuBtnName(screenSettings
											.getMenuBtnName().trim());
									grpMenuItem
											.setMenuFucntionality(screenSettings
													.getMenuFucntionality());
									grpMenuItem
											.setHiddenmenuFnctn(screenSettings
													.getMenuFucntionality());
									grpMenuItem
											.setMenuIconId(ApplicationConstants.BUTTONID
													+ screenSettings
															.getMenuBtnName()
															.trim());
									grpMenuItem.setBtnLinkId(screenSettings
											.getBtnLinkId());
									grpMenuItem
											.setHiddenBtnLinkId(screenSettings
													.getBtnLinkId());
									grpMenuItem.setSubCatIds(screenSettings
											.getSubCatIds());
									grpMenuItem.setHiddenSubCate(screenSettings
											.getChkSubCate());
									grpMenuItem.setChkSubCate(screenSettings
											.getChkSubCate());
									grpMenuItem.setCitiId(screenSettings
											.getCitiId());
									grpMenuItem.setBtnGroup(selGrpName);

									// Added to add Image in Grouped tab
									// template
									if (isGroupImage != null
											&& "true"
													.equalsIgnoreCase(isGroupImage)) {
										grpMenuItem
												.setImagePath(ApplicationConstants.TEMPIMGPATH
														+ screenSettings
																.getLogoImageName());
										// grpMenuItem.setImagePath(screenSettings.getImagePath());
										grpMenuItem
												.setLogoImageName(screenSettings
														.getLogoImageName());
									} else {
										grpMenuItem.setImagePath(null);
										// grpMenuItem.setImagePath(screenSettings.getImagePath());
										grpMenuItem.setLogoImageName(null);
									}

									if (!"0".equals(screenSettings.getBtnDept())) {
										grpMenuItem.setBtnDept(screenSettings
												.getBtnDept());

									} else {

										grpMenuItem.setBtnDept(null);
									}

									if (!"0".equals(screenSettings.getBtnType())) {
										grpMenuItem.setBtnType(screenSettings
												.getBtnType());

									} else {

										grpMenuItem.setBtnType(null);
									}

									btnList.add(grpMenuItem);

									group.setGrpBtnsList(btnList);
									groupedBtnList.remove(i);
									groupedBtnList.add(i, group);
									break;

								}

								if (i == groupedBtnList.size() - 1) {
									newGrpFlag = true;
									grpBtnsList = new ArrayList<ScreenSettings>();
									groupTemplate = new GroupTemplate();

									groupTemplate.setGrpName(selGrpName);

									grpMenuItem = new ScreenSettings();
									grpMenuItem.setMenuBtnName(screenSettings
											.getMenuBtnName().trim());
									grpMenuItem
											.setMenuFucntionality(screenSettings
													.getMenuFucntionality());
									grpMenuItem
											.setHiddenmenuFnctn(screenSettings
													.getMenuFucntionality());
									grpMenuItem
											.setMenuIconId(ApplicationConstants.BUTTONID
													+ screenSettings
															.getMenuBtnName()
															.trim());
									grpMenuItem.setBtnLinkId(screenSettings
											.getBtnLinkId());
									grpMenuItem
											.setHiddenBtnLinkId(screenSettings
													.getBtnLinkId());
									grpMenuItem.setSubCatIds(screenSettings
											.getSubCatIds());
									grpMenuItem.setHiddenSubCate(screenSettings
											.getChkSubCate());
									grpMenuItem.setChkSubCate(screenSettings
											.getChkSubCate());
									grpMenuItem.setCitiId(screenSettings
											.getCitiId());
									grpMenuItem.setBtnGroup(selGrpName);

									// Added to add Image in Grouped tab
									// template
									if (isGroupImage != null
											&& "true"
													.equalsIgnoreCase(isGroupImage)) {
										grpMenuItem
												.setImagePath(ApplicationConstants.TEMPIMGPATH
														+ screenSettings
																.getLogoImageName());
										// grpMenuItem.setImagePath(screenSettings.getImagePath());
										grpMenuItem
												.setLogoImageName(screenSettings
														.getLogoImageName());
									} else {
										grpMenuItem.setImagePath(null);
										// grpMenuItem.setImagePath(screenSettings.getImagePath());
										grpMenuItem.setLogoImageName(null);
									}

									if (!"0".equals(screenSettings.getBtnDept())) {
										grpMenuItem.setBtnDept(screenSettings
												.getBtnDept());

									} else {

										grpMenuItem.setBtnDept(null);
									}

									if (!"0".equals(screenSettings.getBtnType())) {
										grpMenuItem.setBtnType(screenSettings
												.getBtnType());

									} else {

										grpMenuItem.setBtnType(null);
									}

									grpBtnsList.add(grpMenuItem);
									groupTemplate.setGrpBtnsList(grpBtnsList);
									groupedBtnList.add(groupTemplate);

									session.setAttribute("groupedBtnList",
											groupedBtnList);
									break;
								}

							}
							// Remove button from old group
							for (int l = 0; l < groupedBtnList.size(); l++) {
								GroupTemplate updateGroup = groupedBtnList
										.get(l);
								if (updateGroup.getGrpName().equals(
										screenSettings.getOldGroupName())) {
									List<ScreenSettings> updatebtnList = updateGroup
											.getGrpBtnsList();
									for (int k = 0; k < updatebtnList.size(); k++) {
										ScreenSettings btn = updatebtnList
												.get(k);
										if (btn.getMenuIconId().equals(btnId)) {
											updatebtnList.remove(k);
											updateGroup
													.setGrpBtnsList(updatebtnList);
											groupedBtnList.remove(l);
											groupedBtnList.add(l, updateGroup);
											break;
										}
									}
									break;
								}
							}

							if (!btnOrderList.isEmpty()) {
								for (int p = 0; p < btnOrderList.size(); p++) {

									if (btnOrderList.get(p).equals(btnId)) {
										btnOrderList.remove(p);
										break;

									}

								}

								for (int p = 0; p < btnOrderList.size(); p++) {

									if (btnOrderList.get(p).equals(
											"Text-" + selGrpName)) {

										btnOrderList
												.add(p + btnList.size(),
														ApplicationConstants.BUTTONID
																+ screenSettings
																		.getMenuBtnName()
																		.trim());
										break;

									}

								}
							}

						}
						session.setAttribute("groupedBtnList", groupedBtnList);
					} else {
						// Code for adding button
						for (int i = 0; i < groupedBtnList.size(); i++) {

							GroupTemplate group = groupedBtnList.get(i);

							if (group.getGrpName().equals(selGrpName)) {

								List<ScreenSettings> btnList = group
										.getGrpBtnsList();

								for (ScreenSettings button : btnList) {
									if (button.getMenuBtnName().equals(
											buttonName)) {

										groupAndListMenuValidation
												.validate(
														screenSettings,
														result,
														ApplicationConstants.DUPLICATEBUTTONNAME);
										break;
									}

									if (button.getMenuFucntionality().equals(
											String.valueOf(subMenuFctnId))
											&& String.valueOf(subMenuFctnId)
													.equals(menuFunctionlity)) {
										if (null != screenSettings
												.getBtnLinkId()
												&& !"".equals(screenSettings
														.getBtnLinkId())) {
											if (button.getBtnLinkId().equals(
													screenSettings
															.getBtnLinkId())) {

												groupAndListMenuValidation
														.validate(
																screenSettings,
																result,
																ApplicationConstants.DUPLICATESUBMENU);
												break;
											}

										}

									}
									if (button.getMenuFucntionality().equals(
											String.valueOf(anythingPageFctnId))
											&& String.valueOf(
													anythingPageFctnId).equals(
													menuFunctionlity)) {
										if (null != screenSettings
												.getBtnLinkId()
												&& !"".equals(screenSettings
														.getBtnLinkId())) {
											if (null != button.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (button
														.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {

													groupAndListMenuValidation
															.validate(
																	screenSettings,
																	result,
																	ApplicationConstants.DUPLICATEANYTHINGPAGE);
													break;
												}
											}

										}

									}
									if (button.getMenuFucntionality().equals(
											String.valueOf(appSiteFctnId))
											&& String.valueOf(appSiteFctnId)
													.equals(menuFunctionlity)) {
										if (null != screenSettings
												.getBtnLinkId()
												&& !"".equals(screenSettings
														.getBtnLinkId())) {
											if (null != button.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (button
														.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {

													groupAndListMenuValidation
															.validate(
																	screenSettings,
																	result,
																	ApplicationConstants.DUPLICATEAPPSITE);
													break;
												}
											}

										}

									}
									if (button.getMenuFucntionality().equals(
											String.valueOf(findFctnId))
											&& String.valueOf(findFctnId)
													.equals(menuFunctionlity)) {
										if (null != screenSettings
												.getBtnLinkId()
												&& !"".equals(screenSettings
														.getBtnLinkId())) {
											if (button.getBtnLinkId().equals(
													screenSettings
															.getBtnLinkId())) {
												if (button
														.getChkSubCate()
														.equals(screenSettings
																.getChkSubCate())) {
													if (button.getBtnLinkId()
															.contains(",")) {
														iconinMenuValidator
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATECATEGORIES);
													} else {
														iconinMenuValidator
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATECATEGORY);
													}
													break;
												}
											}
										}

									}

									if (button.getMenuFucntionality().equals(
											String.valueOf(eventFctnId))
											&& String.valueOf(eventFctnId)
													.equals(menuFunctionlity)) {
										if (null != screenSettings
												.getBtnLinkId()
												&& !"".equals(screenSettings
														.getBtnLinkId())) {
											if (button.getBtnLinkId().equals(
													screenSettings
															.getBtnLinkId())) {
												if (button.getBtnLinkId()
														.contains(",")) {
													iconinMenuValidator
															.validate(
																	screenSettings,
																	result,
																	ApplicationConstants.DUPLICATECATEGORIES);
												} else {
													iconinMenuValidator
															.validate(
																	screenSettings,
																	result,
																	ApplicationConstants.DUPLICATECATEGORY);
												}

												break;
											}
										}

									}
									
									
									if (button.getMenuFucntionality().equals(
											String.valueOf(fundraFctnId))
											&& String.valueOf(fundraFctnId)
													.equals(menuFunctionlity)) {
										if (null != screenSettings
												.getBtnLinkId()
												&& !"".equals(screenSettings
														.getBtnLinkId())) {
											if (button.getBtnLinkId().equals(
													screenSettings
															.getBtnLinkId())) {
												if (button.getBtnLinkId()
														.contains(",")) {
													iconinMenuValidator
															.validate(
																	screenSettings,
																	result,
																	ApplicationConstants.DUPLICATECATEGORIES);
												} else {
													iconinMenuValidator
															.validate(
																	screenSettings,
																	result,
																	ApplicationConstants.DUPLICATECATEGORY);
												}

												break;
											}
										}

									}

									if (button.getMenuFucntionality().equals(
											screenSettings
													.getMenuFucntionality())
											&& !button
													.getMenuFucntionality()
													.equals(String
															.valueOf(subMenuFctnId))
											&& !button
													.getMenuFucntionality()
													.equals(String
															.valueOf(anythingPageFctnId))
											&& !button
													.getMenuFucntionality()
													.equals(String
															.valueOf(appSiteFctnId))
											&& !button
													.getMenuFucntionality()
													.equals(String
															.valueOf(findFctnId))
											&& !button
													.getMenuFucntionality()
													.equals(String
															.valueOf(eventFctnId))
															
															&& !button
															.getMenuFucntionality()
															.equals(String
																	.valueOf(fundraFctnId))
													
															
											) {

										groupAndListMenuValidation
												.validate(
														screenSettings,
														result,
														ApplicationConstants.DUPLICATEFUNCTINALITY);
										break;
									}

								}

								if (result.hasErrors()) {
									return viewName;
								}

								grpMenuItem = new ScreenSettings();
								grpMenuItem.setMenuBtnName(screenSettings
										.getMenuBtnName().trim());
								grpMenuItem.setMenuFucntionality(screenSettings
										.getMenuFucntionality());
								grpMenuItem.setHiddenmenuFnctn(screenSettings
										.getMenuFucntionality());
								grpMenuItem
										.setMenuIconId(ApplicationConstants.BUTTONID
												+ screenSettings
														.getMenuBtnName()
														.trim());
								grpMenuItem.setBtnLinkId(screenSettings
										.getBtnLinkId());
								grpMenuItem.setHiddenBtnLinkId(screenSettings
										.getBtnLinkId());
								grpMenuItem.setSubCatIds(screenSettings
										.getSubCatIds());
								grpMenuItem.setHiddenSubCate(screenSettings
										.getChkSubCate());
								grpMenuItem.setChkSubCate(screenSettings
										.getChkSubCate());
								grpMenuItem.setCitiId(screenSettings
										.getCitiId());
								grpMenuItem.setBtnGroup(selGrpName);

								// Added to add Image in Grouped tab template
								if (isGroupImage != null
										&& "true"
												.equalsIgnoreCase(isGroupImage)) {
									grpMenuItem
											.setImagePath(ApplicationConstants.TEMPIMGPATH
													+ screenSettings
															.getLogoImageName());
									// grpMenuItem.setImagePath(screenSettings.getImagePath());
									grpMenuItem.setLogoImageName(screenSettings
											.getLogoImageName());
								} else {
									grpMenuItem.setImagePath(null);
									// grpMenuItem.setImagePath(screenSettings.getImagePath());
									grpMenuItem.setLogoImageName(null);
								}

								if (!"0".equals(screenSettings.getBtnDept())) {
									grpMenuItem.setBtnDept(screenSettings
											.getBtnDept());

								} else {

									grpMenuItem.setBtnDept(null);
								}

								if (!"0".equals(screenSettings.getBtnType())) {
									grpMenuItem.setBtnType(screenSettings
											.getBtnType());

								} else {

									grpMenuItem.setBtnType(null);
								}

								btnList.add(grpMenuItem);

								group.setGrpBtnsList(btnList);

								groupedBtnList.remove(i);

								groupedBtnList.add(i, group);

								if (!btnOrderList.isEmpty()) {
									for (int p = 0; p < btnOrderList.size(); p++) {

										if (btnOrderList.get(p).equals(
												"Text-" + selGrpName)) {

											btnOrderList
													.add(p + btnList.size(),
															ApplicationConstants.BUTTONID
																	+ screenSettings
																			.getMenuBtnName()
																			.trim());
											break;

										}

									}
								}
								break;

							} else {
								// Code for creating new group
								if (i == groupedBtnList.size() - 1) {

									grpBtnsList = new ArrayList<ScreenSettings>();
									groupTemplate = new GroupTemplate();

									groupTemplate.setGrpName(selGrpName);

									grpMenuItem = new ScreenSettings();
									grpMenuItem.setMenuBtnName(screenSettings
											.getMenuBtnName().trim());
									grpMenuItem
											.setMenuFucntionality(screenSettings
													.getMenuFucntionality());
									grpMenuItem
											.setHiddenmenuFnctn(screenSettings
													.getMenuFucntionality());
									grpMenuItem
											.setMenuIconId(ApplicationConstants.BUTTONID
													+ screenSettings
															.getMenuBtnName()
															.trim());
									grpMenuItem.setBtnLinkId(screenSettings
											.getBtnLinkId());
									grpMenuItem
											.setHiddenBtnLinkId(screenSettings
													.getBtnLinkId());
									grpMenuItem.setSubCatIds(screenSettings
											.getSubCatIds());
									grpMenuItem.setHiddenSubCate(screenSettings
											.getChkSubCate());
									grpMenuItem.setChkSubCate(screenSettings
											.getChkSubCate());
									grpMenuItem.setCitiId(screenSettings
											.getCitiId());
									grpMenuItem.setBtnGroup(selGrpName);

									// Added to add Image in Grouped tab
									// template
									if (isGroupImage != null
											&& "true"
													.equalsIgnoreCase(isGroupImage)) {
										grpMenuItem
												.setImagePath(ApplicationConstants.TEMPIMGPATH
														+ screenSettings
																.getLogoImageName());
										// grpMenuItem.setImagePath(screenSettings.getImagePath());
										grpMenuItem
												.setLogoImageName(screenSettings
														.getLogoImageName());
									} else {
										grpMenuItem.setImagePath(null);
										// grpMenuItem.setImagePath(screenSettings.getImagePath());
										grpMenuItem.setLogoImageName(null);
									}

									if (!"0".equals(screenSettings.getBtnDept())) {
										grpMenuItem.setBtnDept(screenSettings
												.getBtnDept());

									} else {

										grpMenuItem.setBtnDept(null);
									}

									if (!"0".equals(screenSettings.getBtnType())) {
										grpMenuItem.setBtnType(screenSettings
												.getBtnType());

									} else {

										grpMenuItem.setBtnType(null);
									}

									grpBtnsList.add(grpMenuItem);
									groupTemplate.setGrpBtnsList(grpBtnsList);
									groupedBtnList.add(groupTemplate);

									session.setAttribute("groupedBtnList",
											groupedBtnList);
									break;
								}

							}

						}
					}
					session.setAttribute("groupedBtnList", groupedBtnList);
					session.setAttribute("menuIconPreview",
							ApplicationConstants.DEFAULTIMAGESQR);
				}
			}

		} else if (null != addorDeleteMenuItem
				&& "DeleteButton".equals(addorDeleteMenuItem)) {
			request.setAttribute("gprdMenuAction", "Button");
			for (int i = 0; i < groupedBtnList.size(); i++) {
				GroupTemplate group = groupedBtnList.get(i);
				if (group.getGrpName().equals(selGrpName)) {
					List<ScreenSettings> btnList = group.getGrpBtnsList();

					for (int j = 0; j < btnList.size(); j++) {

						grpMenuItem = btnList.get(j);
						if (grpMenuItem.getMenuIconId().equals(btnId)) {

							btnList.remove(j);
							break;
						}

					}
					group.setGrpBtnsList(btnList);
					groupedBtnList.remove(i);
					groupedBtnList.add(i, group);
					break;
				}

			}
			session.setAttribute("groupedBtnList", groupedBtnList);

		} else if (null != addorDeleteMenuItem
				&& "UpdateGroup".equals(addorDeleteMenuItem)) {
			request.setAttribute("gprdMenuAction", "Group");
			groupAndListMenuValidation.validate(screenSettings, result,
					ApplicationConstants.GROUPNAME);

			if (result.hasErrors()) {
				screenSettings.setBtnLinkId(null);
				return viewName;
			}
			if (null != btnId && !"".equals(btnId)) {
				for (int i = 0; i < groupedBtnList.size(); i++) {
					GroupTemplate group = groupedBtnList.get(i);
					if (group.getGrpName().equals(btnId.split("-")[1])) {

						for (int j = 0; j < groupedBtnList.size(); j++) {

							if (j != i) {

								GroupTemplate groupName = groupedBtnList.get(j);

								if (groupName.getGrpName().equals(grpName)) {

									groupAndListMenuValidation
											.validate(
													screenSettings,
													result,
													ApplicationConstants.DUPLICATEGROUP);
									break;
								}

							}

						}
						if (result.hasErrors()) {
							screenSettings.setBtnLinkId(null);
							return viewName;
						}

						group.setGrpName(grpName);
						groupedBtnList.remove(i);
						groupedBtnList.add(i, group);

						List<String> groupListUpdate = (ArrayList<String>) session
								.getAttribute("groupList");

						for (int k = 0; k <= groupListUpdate.size(); k++) {

							if (groupListUpdate.get(k).equals(
									btnId.split("-")[1])) {
								groupListUpdate.remove(k);
								groupListUpdate.add(k, grpName);
								session.setAttribute("groupList",
										groupListUpdate);

								if (!btnOrderList.isEmpty()) {
									for (int p = 0; p < btnOrderList.size(); p++) {

										if (btnOrderList.get(p).equals(btnId)) {
											btnOrderList.set(p,
													ApplicationConstants.TEXT
															+ "-" + grpName);
											break;

										}

									}
								}
								break;
							}
						}

						break;
					}

				}
				session.setAttribute("groupedBtnList", groupedBtnList);
			}

		} else if (null != addorDeleteMenuItem
				&& "DeleteGroup".equals(addorDeleteMenuItem)) {
			request.setAttribute("gprdMenuAction", "Group");
			groupAndListMenuValidation.validate(screenSettings, result,
					ApplicationConstants.GROUPNAME);

			if (result.hasErrors()) {
				screenSettings.setBtnLinkId(null);
				return viewName;
			}
			if (null != btnId && !"".equals(btnId)) {
				for (int i = 0; i < groupedBtnList.size(); i++) {
					GroupTemplate group = groupedBtnList.get(i);
					if (group.getGrpName().equals(btnId.split("-")[1])) {
						groupedBtnList.remove(i);
						break;
					}

				}
				List<String> groupListUpdate = (ArrayList<String>) session
						.getAttribute("groupList");
				for (int k = 0; k < groupListUpdate.size(); k++) {

					if (groupListUpdate.get(k).equals(btnId.split("-")[1])) {
						groupListUpdate.remove(k);
						session.setAttribute("groupList", groupListUpdate);
						break;
					}
				}

				session.setAttribute("groupedBtnList", groupedBtnList);

			}

		}
		// Below code is for re-ordering buttons

		// Code to Construct data as per the template
		if (null != groupedBtnList) {

			for (int i = 0; i < groupedBtnList.size(); i++) {

				GroupTemplate group = groupedBtnList.get(i);
				grpBtnsList = group.getGrpBtnsList();

				if (null != grpBtnsList && !grpBtnsList.isEmpty()) {
					menuItem = new ScreenSettings();
					menuItem.setMenuBtnName(group.getGrpName());
					menuItem.setMenuFucntionality(((Integer) session
							.getAttribute("textFctnId")).toString());
					menuItem.setMenuIconId(ApplicationConstants.TEXT + "-"
							+ group.getGrpName());
					previewMenuItems.add(menuItem);
					for (ScreenSettings btn : grpBtnsList) {
						menuItem = new ScreenSettings();

						menuItem.setMenuBtnName(btn.getMenuBtnName());
						menuItem.setMenuIconId(btn.getMenuIconId());
						menuItem.setMenuFucntionality(btn
								.getMenuFucntionality());
						menuItem.setBtnLinkId(btn.getBtnLinkId());
						menuItem.setBtnDept(btn.getBtnDept());
						menuItem.setBtnType(btn.getBtnType());
						menuItem.setBtnGroup(group.getGrpName());
						menuItem.setSubCatIds(btn.getSubCatIds());
						menuItem.setChkSubCate(btn.getChkSubCate());
						menuItem.setCitiId(btn.getCitiId());

						// Added to add Image in Grouped tab template
						if (isGroupImage != null
								&& "true".equalsIgnoreCase(isGroupImage)) {
							menuItem.setImagePath(btn.getImagePath());
							menuItem.setLogoImageName(btn.getLogoImageName());
						} else {
							menuItem.setImagePath(null);
							menuItem.setLogoImageName(null);
						}
						// menuItem.setImagePath(btn.getImagePath());
						// menuItem.setLogoImageName(btn.getLogoImageName());

						previewMenuItems.add(menuItem);
					}

				}

			}

			session.setAttribute("previewMenuItems", previewMenuItems);

		}

		if (!btnOrderList.isEmpty()) {
			for (int j = 0; j < btnOrderList.size(); j++) {

				for (int i = 0; i < previewMenuItems.size(); i++) {
					ScreenSettings btn = previewMenuItems.get(i);

					if (btn.getMenuIconId().equals(btnOrderList.get(j))) {

						sortedMenuItems.add(btn);
						previewMenuItems.remove(i);
						break;
					}

				}
			}

			if (!previewMenuItems.isEmpty()) {

				for (int i = 0; i < previewMenuItems.size(); i++) {

					sortedMenuItems.add(previewMenuItems.get(i));
				}

			}
			session.setAttribute("previewMenuItems", sortedMenuItems);
		}
		request.setAttribute("gprdMenuAction", "Button");
		screenSettings = new ScreenSettings();
		screenSettings.setMenuId(menuId);
		screenSettings.setMenuLevel(menuLevel);
		screenSettings.setSubMenuName(subMenuName);
		screenSettings.setIsmenuFilterTypeSelected(true);
		screenSettings.setMenuFilterType(menuFilterType);
		screenSettings.setEditFilter(false);
		screenSettings.setIsGroupImg(isGroupImage);
		model.put("screenSettingsForm", screenSettings);
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "setupgroupmenu";
	}

	/**
	 * Controller Method for displaying iconic menu template..
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/setupiconicmenu.htm", method = RequestMethod.POST)
	public String setUpGridViewMenu(
			@ModelAttribute("menuDetails") MenuDetails menuDetails,
			HttpServletRequest request, ModelMap model, HttpSession session)
			throws HubCitiServiceException {
		String methodName = "displayMainMenu";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String menuId = null;
		String subMenuName = null;
		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");
		MenuDetails menuDetailsObj = null;
		session.setAttribute("iconicTempbnrimgPreview",
				ApplicationConstants.DEFAULTBANNERPREVIEWIMAGE);
		session.removeAttribute("menuBarButtonsList");
		try {
			// Minimum crop height and width
			session.setAttribute("minCropHt", 160);
			session.setAttribute("minCropWd", 160);
			if (null != request.getParameter("hidMenuType")) {
				if (request.getParameter("hidMenuType").equals(
						ApplicationConstants.SUBMENU)) {
					session.setAttribute(ApplicationConstants.MENUNAME,
							ApplicationConstants.SETUPSUBMENU);
				} else {
					session.setAttribute(ApplicationConstants.MENUNAME,
							ApplicationConstants.MAINMENUSCREEN);
				}
			}
			// session.setAttribute(ApplicationConstants.MENUNAME,
			// ApplicationConstants.MAINMENUSCREEN);

			User user = (User) session.getAttribute("loginUser");

			menuDetailsObj = new MenuDetails();
			menuDetailsObj.setMenuTypeName(ApplicationConstants.ICONICTEMPLATE);
			menuDetailsObj.setLevel(menuDetails.getLevel());

			if (null == menuDetails.getLevel()) {
				menuDetailsObj.setMenuName(null);

			} else {
				menuDetailsObj.setMenuName(ApplicationConstants.MAINMENU);

			}

			if (null != menuDetails.getMenuId()
					&& !"".equals(menuDetails.getMenuId())) {
				menuId = menuDetails.getMenuId().toString();
				subMenuName = menuDetails.getMenuName();
			} else {
				// Create Menu
				menuId = hubCitiService.createMenu(menuDetailsObj, user);

			}

			ScreenSettings screenSettings = new ScreenSettings();
			screenSettings.setMenuId(menuId);
			screenSettings.setMenuLevel(menuDetails.getLevel());
			screenSettings.setSubMenuName(subMenuName);
			// screenSettings.setMenuId(menuId);
			model.put("screenSettingsForm", screenSettings);

		} catch (HubCitiServiceException exception) {

			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "iconicmenutemplate";
	}

	/**
	 * Controller Method for Creating iconic menu template.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addbutton.htm", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String createGridViewMenuItem(
			@ModelAttribute("screenSettingsForm") ScreenSettings screenSettings,
			BindingResult result, HttpServletRequest request, ModelMap model,
			HttpSession session) throws HubCitiServiceException {
		String methodName = "createGridViewMenuItem";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		ScreenSettings iconicMenuItem = null;
		String buttonName = screenSettings.getMenuBtnName();
		String menuId = screenSettings.getMenuId();
		String subMenuName = screenSettings.getSubMenuName();
		String bannerImageName = screenSettings.getBannerImageName();
		Integer menuLevel = screenSettings.getMenuLevel();
		String btnId = screenSettings.getMenuIconId();
		String viewName = screenSettings.getViewName();
		String addorDeleteMenuItem = screenSettings.getAddDeleteBtn();
		String menuFunctionlity = screenSettings.getMenuFucntionality();
		// List of Menu items
		List<ScreenSettings> iconicMenuItems = (ArrayList<ScreenSettings>) session
				.getAttribute("previewMenuItems");

		Integer subMenuFctnId = (Integer) session.getAttribute("subMenuFctnId");
		Integer anythingPageFctnId = (Integer) session
				.getAttribute("anythingPageFctnId");
		Integer appSiteFctnId = (Integer) session.getAttribute("appSiteFctnId");
		Integer findFctnId = (Integer) session.getAttribute("findFctnId");
		Integer eventFctnId = (Integer) session.getAttribute("eventFctnId");
		Integer fundraFctnId = (Integer) session.getAttribute("fundraFctnId");
		// for subcategories
		screenSettings.setHiddenSubCate(screenSettings.getChkSubCate());
		screenSettings.setChkSubCate(screenSettings.getChkSubCate());

		if (null != screenSettings.getMenuFucntionality()
				&& screenSettings.getMenuFucntionality().equals(
						String.valueOf(findFctnId))
				&& null != screenSettings.getBtnLinkId()) {
			String[] findCat = screenSettings.getBtnLinkId().split(",");
			String findCatList = null;

			for (String cat : findCat) {
				if (cat.contains("MC")) {
					if (null != findCatList) {
						findCatList += cat + ",";
					} else {
						findCatList = new String();
						findCatList = cat + ",";
					}
				}
			}
			if (findCatList.endsWith(",")) {
				findCatList = findCatList
						.substring(0, findCatList.length() - 1);
			}
			screenSettings.setBtnLinkId(findCatList);
		}

		// String anythingPageFctnId = (String)
		// session.getAttribute("anythingPageFctnId");

		// String appSiteFctnId = (String)
		// session.getAttribute("appSiteFctnId");

		// Below Code is for Re-Ordering menu buttons
		String btnOrderArry[] = null;
		String btnOrder = screenSettings.getBtnPosition();
		List<ScreenSettings> sortedMenuItems = new ArrayList<ScreenSettings>();
		if (!"".equals(Utility.checkNull(btnOrder))) {
			btnOrderArry = btnOrder.split("~");

		}

		if (null != buttonName) {

			buttonName = buttonName.trim();
		}

		screenSettings
				.setHiddenmenuFnctn(screenSettings.getMenuFucntionality());
		screenSettings.setHiddenBtnLinkId(screenSettings.getBtnLinkId());
		if ("RegionApp".equalsIgnoreCase(screenSettings.getUserType())) {
			screenSettings.setHiddenCitiId(screenSettings.getCitiId());
		}

		// Code for SubMenu Filters Implementation
		String[] menuFilterType = screenSettings.getMenuFilterType();

		if (null != menuFilterType) {
			if (menuFilterType.length != 0 || screenSettings.isEditFilter()) {

				session.setAttribute("menuFilterType", menuFilterType);
				screenSettings.setEditFilter(false);
			} else {

				menuFilterType = (String[]) session
						.getAttribute("menuFilterType");
				screenSettings.setMenuFilterType(menuFilterType);
			}

		}

		List<Type> filterTypeList = (ArrayList<Type>) session
				.getAttribute("filterTypeList");
		List<Department> filterDeptList = (ArrayList<Department>) session
				.getAttribute("filterDeptList");
		if (null != filterTypeList) {
			Type newType = null;
			if (!"0".equals(screenSettings.getBtnType())) {
				if (filterTypeList.size() != 0) {

					for (int i = 0; i < filterTypeList.size(); i++) {
						Type type = filterTypeList.get(i);

						if (type.getTypeName().equals(
								screenSettings.getBtnType())) {

							break;
						}

						if (i == filterTypeList.size() - 1) {
							newType = new Type();
							newType.setTypeName(screenSettings.getBtnType());
							filterTypeList.add(newType);
							session.setAttribute("filterTypeList",
									filterTypeList);
						}
					}
				} else {
					newType = new Type();
					newType.setTypeName(screenSettings.getBtnType());
					filterTypeList.add(newType);
					session.setAttribute("filterTypeList", filterTypeList);

				}

			}

		}

		if (null != filterDeptList) {
			Department newDept = null;
			if (!"0".equals(screenSettings.getBtnDept())) {
				if (filterDeptList.size() != 0) {

					for (int i = 0; i < filterDeptList.size(); i++) {
						Department dept = filterDeptList.get(i);

						if (dept.getDeptName().equals(
								screenSettings.getBtnDept())) {

							break;
						}

						if (i == filterDeptList.size() - 1) {
							newDept = new Department();
							newDept.setDeptName(screenSettings.getBtnDept());
							filterDeptList.add(newDept);
							session.setAttribute("filterDeptList",
									filterDeptList);
						}
					}
				} else {
					newDept = new Department();
					newDept.setDeptName(screenSettings.getBtnDept());
					filterDeptList.add(newDept);
					session.setAttribute("filterDeptList", filterDeptList);

				}

			}

		}

		// Code for Adding menu item
		if (null != addorDeleteMenuItem
				&& "AddButton".equals(addorDeleteMenuItem)) {

			if (null != screenSettings.getMenuFucntionality()
					&& !"".equals(screenSettings.getMenuFucntionality())) {
				if (screenSettings.getMenuFucntionality().equals(
						String.valueOf(subMenuFctnId))) {
					screenSettings
							.setFunctionalityType(ApplicationConstants.SUBMENU);
				} else if (screenSettings.getMenuFucntionality().equals(
						String.valueOf(appSiteFctnId))) {
					screenSettings
							.setFunctionalityType(ApplicationConstants.APPSITE);
				} else if (screenSettings.getMenuFucntionality().equals(
						String.valueOf(anythingPageFctnId))) {
					screenSettings
							.setFunctionalityType(ApplicationConstants.ANYTHINGPAGE);
				} else if (screenSettings.getMenuFucntionality().equals(
						String.valueOf(findFctnId))) {
					screenSettings
							.setFunctionalityType(ApplicationConstants.FIND);
					screenSettings.setHiddenSubCate(screenSettings
							.getChkSubCate());
					screenSettings
							.setChkSubCate(screenSettings.getChkSubCate());
				} else if (screenSettings.getMenuFucntionality().equals(
						String.valueOf(eventFctnId))) {
					screenSettings
							.setFunctionalityType(ApplicationConstants.SETUPEVENTS);
				}else if (screenSettings.getMenuFucntionality().equals(
						String.valueOf(fundraFctnId))) {

					screenSettings.setFunctionalityType(ApplicationConstants.FUNDRAISERS);
					screenSettings.setHiddenFundEvtId(screenSettings.getBtnLinkId());
				}else {

					screenSettings.setBtnLinkId(null);
					screenSettings.setHiddenBtnLinkId(null);
					screenSettings.setHiddenSubCate(null);
					screenSettings.setChkSubCate(null);
				}

			}

			// Validate menu item details
			iconinMenuValidator.validate(screenSettings, result);

			if (result.hasErrors()) {
				screenSettings.setBtnLinkId(null);
				screenSettings.setChkSubCate(null);
				return viewName;
			} else {
				// If iconicMenuItems list is empty,create list, add menu item
				// to list
				// and store in session
				if (null == iconicMenuItems) {
					iconicMenuItems = new ArrayList<ScreenSettings>();
					iconicMenuItem = new ScreenSettings();
					iconicMenuItem.setLogoImageName(screenSettings
							.getLogoImageName());
					iconicMenuItem
							.setImagePath(ApplicationConstants.TEMPIMGPATH
									+ screenSettings.getLogoImageName());
					iconicMenuItem.setMenuBtnName(screenSettings
							.getMenuBtnName().trim());
					iconicMenuItem.setMenuFucntionality(screenSettings
							.getMenuFucntionality());
					iconicMenuItem.setHiddenmenuFnctn(screenSettings
							.getMenuFucntionality());
					iconicMenuItem.setMenuIconId(ApplicationConstants.BUTTONID
							+ screenSettings.getMenuBtnName().trim());
					iconicMenuItem.setBtnLinkId(screenSettings.getBtnLinkId());
					iconicMenuItem.setHiddenBtnLinkId(screenSettings
							.getBtnLinkId());
					iconicMenuItem.setCategory(screenSettings.getCategory());
					iconicMenuItem.setSubCatIds(screenSettings.getSubCatIds());
					iconicMenuItem.setHiddenSubCate(screenSettings
							.getChkSubCate());
					iconicMenuItem
							.setChkSubCate(screenSettings.getChkSubCate());
					iconicMenuItem.setCitiId(screenSettings.getCitiId());

					if (!"0".equals(screenSettings.getBtnDept())) {
						iconicMenuItem.setBtnDept(screenSettings.getBtnDept());

					} else {

						iconicMenuItem.setBtnDept(null);
					}

					if (!"0".equals(screenSettings.getBtnType())) {
						iconicMenuItem.setBtnType(screenSettings.getBtnType());

					} else {

						iconicMenuItem.setBtnType(null);
					}

					iconicMenuItems.add(iconicMenuItem);

					session.setAttribute("previewMenuItems", iconicMenuItems);
				} else {
					// Code for updating button detail
					if (null != btnId && !"".equals(btnId)) {

						for (int i = 0; i < iconicMenuItems.size(); i++) {

							ScreenSettings menuItem = iconicMenuItems.get(i);

							if (menuItem.getMenuIconId().equals(btnId)) {

								for (int j = 0; j < iconicMenuItems.size(); j++) {

									ScreenSettings btn = iconicMenuItems.get(j);
									if (j != i) {
										if (btn.getMenuBtnName().equals(
												buttonName)) {

											iconinMenuValidator
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATEBUTTONNAME);
											break;
										}

										if (btn.getMenuFucntionality().equals(
												String.valueOf(subMenuFctnId))
												&& String
														.valueOf(subMenuFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (btn.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {

													iconinMenuValidator
															.validate(
																	screenSettings,
																	result,
																	ApplicationConstants.DUPLICATESUBMENU);
													break;
												}
											}

										}
										if (btn.getMenuFucntionality()
												.equals(String
														.valueOf(anythingPageFctnId))
												&& String
														.valueOf(
																anythingPageFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (btn.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {

													iconinMenuValidator
															.validate(
																	screenSettings,
																	result,
																	ApplicationConstants.DUPLICATEANYTHINGPAGE);
													break;
												}
											}

										}
										if (btn.getMenuFucntionality().equals(
												String.valueOf(appSiteFctnId))
												&& String
														.valueOf(appSiteFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (btn.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {

													iconinMenuValidator
															.validate(
																	screenSettings,
																	result,
																	ApplicationConstants.DUPLICATEAPPSITE);
													break;
												}
											}

										}
										if (btn.getMenuFucntionality().equals(
												String.valueOf(findFctnId))
												&& String
														.valueOf(findFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (btn.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {
													if (btn.getChkSubCate()
															.equals(screenSettings
																	.getChkSubCate())) {
														if (btn.getBtnLinkId()
																.contains(",")) {
															iconinMenuValidator
																	.validate(
																			screenSettings,
																			result,
																			ApplicationConstants.DUPLICATECATEGORIES);
														} else {
															iconinMenuValidator
																	.validate(
																			screenSettings,
																			result,
																			ApplicationConstants.DUPLICATECATEGORY);
														}

														break;
													}
												}
											}

										}

										if (btn.getMenuFucntionality().equals(
												String.valueOf(eventFctnId))
												&& String
														.valueOf(eventFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (btn.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {
													if (btn.getBtnLinkId()
															.contains(",")) {
														iconinMenuValidator
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATECATEGORIES);
													} else {
														iconinMenuValidator
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATECATEGORY);
													}

													break;
												}
											}

										}
										if (btn.getMenuFucntionality().equals(
												String.valueOf(fundraFctnId))
												&& String
														.valueOf(fundraFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (btn.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {
													if (btn.getBtnLinkId()
															.contains(",")) {
														iconinMenuValidator
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATECATEGORIES);
													} else {
														iconinMenuValidator
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATECATEGORY);
													}

													break;
												}
											}

										}									
										if (btn.getMenuFucntionality()
												.equals(screenSettings
														.getMenuFucntionality())
												&& !btn.getMenuFucntionality()
														.equals(String
																.valueOf(subMenuFctnId))
												&& !btn.getMenuFucntionality()
														.equals(String
																.valueOf(appSiteFctnId))
												&& !btn.getMenuFucntionality()
														.equals(String
																.valueOf(anythingPageFctnId))
												&& !btn.getMenuFucntionality()
														.equals(String
																.valueOf(findFctnId))
												&& !btn.getMenuFucntionality()
														.equals(String
																.valueOf(eventFctnId))
												&&!btn.getMenuFucntionality().equals(String.valueOf(fundraFctnId))) {				
											

											iconinMenuValidator
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATEFUNCTINALITY);
											break;
										}

									}
								}

								if (result.hasErrors()) {
									screenSettings.setBtnLinkId(null);
									screenSettings.setChkSubCate(null);
									return viewName;
								}

								menuItem.setLogoImageName(screenSettings
										.getLogoImageName());
								menuItem.setImagePath(ApplicationConstants.TEMPIMGPATH
										+ screenSettings.getLogoImageName());
								menuItem.setMenuBtnName(screenSettings
										.getMenuBtnName().trim());
								menuItem.setMenuFucntionality(screenSettings
										.getMenuFucntionality());
								menuItem.setHiddenmenuFnctn(screenSettings
										.getMenuFucntionality());
								menuItem.setMenuIconId(ApplicationConstants.BUTTONID
										+ screenSettings.getMenuBtnName()
												.trim());
								menuItem.setBtnLinkId(screenSettings
										.getBtnLinkId());
								menuItem.setHiddenBtnLinkId(screenSettings
										.getBtnLinkId());
								menuItem.setSubCatIds(screenSettings
										.getSubCatIds());
								menuItem.setHiddenSubCate(screenSettings
										.getChkSubCate());
								menuItem.setChkSubCate(screenSettings
										.getChkSubCate());
								menuItem.setCitiId(screenSettings.getCitiId());

								if (!"0".equals(screenSettings.getBtnDept())) {
									menuItem.setBtnDept(screenSettings
											.getBtnDept());

								} else {

									menuItem.setBtnDept(null);
								}

								if (!"0".equals(screenSettings.getBtnType())) {
									menuItem.setBtnType(screenSettings
											.getBtnType());

								} else {

									menuItem.setBtnType(null);

								}

								iconicMenuItems.remove(i);

								iconicMenuItems.add(i, menuItem);
								session.setAttribute("previewMenuItems",
										iconicMenuItems);

								// Below Code is for Re-Ordering menu buttons
								if (null != btnOrderArry) {
									for (int p = 0; p < btnOrderArry.length; p++) {

										if (btnOrderArry[p].equals(btnId)) {
											btnOrderArry[p] = ApplicationConstants.BUTTONID
													+ screenSettings
															.getMenuBtnName()
															.trim();
											break;

										}

									}
								}

							}

						}

					} else {
						// If menu items list is already created, Validate for
						// duplicate button and add unique menu items to list
						for (ScreenSettings menuItem : iconicMenuItems) {

							if (menuItem.getMenuBtnName().equals(buttonName)) {

								iconinMenuValidator
										.validate(
												screenSettings,
												result,
												ApplicationConstants.DUPLICATEBUTTONNAME);
								break;
							}

							if (menuItem.getMenuFucntionality().equals(
									String.valueOf(subMenuFctnId))
									&& String.valueOf(subMenuFctnId).equals(
											menuFunctionlity)) {
								if (null != screenSettings.getBtnLinkId()
										&& !"".equals(screenSettings
												.getBtnLinkId())) {
									if (menuItem.getBtnLinkId().equals(
											screenSettings.getBtnLinkId())) {

										iconinMenuValidator
												.validate(
														screenSettings,
														result,
														ApplicationConstants.DUPLICATESUBMENU);
										break;
									}
								}

							}

							if (menuItem.getMenuFucntionality().equals(
									String.valueOf(anythingPageFctnId))
									&& String.valueOf(anythingPageFctnId)
											.equals(menuFunctionlity)) {
								if (null != screenSettings.getBtnLinkId()
										&& !"".equals(screenSettings
												.getBtnLinkId())) {
									if (menuItem.getBtnLinkId().equals(
											screenSettings.getBtnLinkId())) {

										iconinMenuValidator
												.validate(
														screenSettings,
														result,
														ApplicationConstants.DUPLICATEANYTHINGPAGE);
										break;
									}
								}

							}
							if (menuItem.getMenuFucntionality().equals(
									String.valueOf(appSiteFctnId))
									&& String.valueOf(appSiteFctnId).equals(
											menuFunctionlity)) {
								if (null != screenSettings.getBtnLinkId()
										&& !"".equals(screenSettings
												.getBtnLinkId())) {
									if (menuItem.getBtnLinkId().equals(
											screenSettings.getBtnLinkId())) {

										iconinMenuValidator
												.validate(
														screenSettings,
														result,
														ApplicationConstants.DUPLICATEAPPSITE);
										break;
									}
								}

							}
							if (menuItem.getMenuFucntionality().equals(
									String.valueOf(findFctnId))
									&& String.valueOf(findFctnId).equals(
											menuFunctionlity)) {
								if (null != screenSettings.getBtnLinkId()
										&& !"".equals(screenSettings
												.getBtnLinkId())) {
									if (menuItem.getBtnLinkId().equals(
											screenSettings.getBtnLinkId())) {
										if (menuItem.getChkSubCate().equals(
												screenSettings.getChkSubCate())) {
											if (menuItem.getBtnLinkId()
													.contains(",")) {
												iconinMenuValidator
														.validate(
																screenSettings,
																result,
																ApplicationConstants.DUPLICATECATEGORIES);
											} else {
												iconinMenuValidator
														.validate(
																screenSettings,
																result,
																ApplicationConstants.DUPLICATECATEGORY);
											}

											break;
										}
									}
								}

							}
							if (menuItem.getMenuFucntionality().equals(
									String.valueOf(eventFctnId))
									&& String.valueOf(eventFctnId).equals(
											menuFunctionlity)) {
								if (null != screenSettings.getBtnLinkId()
										&& !"".equals(screenSettings
												.getBtnLinkId())) {
									if (menuItem.getBtnLinkId().equals(
											screenSettings.getBtnLinkId())) {
										if (menuItem.getBtnLinkId().contains(
												",")) {
											iconinMenuValidator
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATECATEGORIES);
										} else {
											iconinMenuValidator
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATECATEGORY);
										}

										break;
									}
								}

							}
							
							if (menuItem.getMenuFucntionality().equals(
									String.valueOf(fundraFctnId))
									&& String.valueOf(fundraFctnId).equals(
											menuFunctionlity)) {
								if (null != screenSettings.getBtnLinkId()
										&& !"".equals(screenSettings
												.getBtnLinkId())) {
									if (menuItem.getBtnLinkId().equals(
											screenSettings.getBtnLinkId())) {
										if (menuItem.getBtnLinkId().contains(
												",")) {
											iconinMenuValidator
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATECATEGORIES);
										} else {
											iconinMenuValidator
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATECATEGORY);
										}

										break;
									}
								}

							}
							
							
							if (menuItem.getMenuFucntionality().equals(
									screenSettings.getMenuFucntionality())
									&& !menuItem.getMenuFucntionality().equals(
											String.valueOf(subMenuFctnId))
									&& !menuItem.getMenuFucntionality().equals(
											String.valueOf(anythingPageFctnId))
									&& !menuItem.getMenuFucntionality().equals(
											String.valueOf(appSiteFctnId))
									&& !menuItem.getMenuFucntionality().equals(
											String.valueOf(findFctnId))
									&& !menuItem.getMenuFucntionality().equals(
											String.valueOf(eventFctnId))
									&&!menuItem.getMenuFucntionality().equals(String.valueOf(fundraFctnId))
									) {

								iconinMenuValidator
										.validate(
												screenSettings,
												result,
												ApplicationConstants.DUPLICATEFUNCTINALITY);
								break;
							}

						}
						if (result.hasErrors()) {
							screenSettings.setBtnLinkId(null);
							screenSettings.setChkSubCate(null);
							return viewName;
						}
						iconicMenuItem = new ScreenSettings();
						iconicMenuItem.setLogoImageName(screenSettings
								.getLogoImageName());
						iconicMenuItem
								.setImagePath(ApplicationConstants.TEMPIMGPATH
										+ screenSettings.getLogoImageName());
						iconicMenuItem.setMenuBtnName(screenSettings
								.getMenuBtnName().trim());
						iconicMenuItem.setMenuFucntionality(screenSettings
								.getMenuFucntionality());
						iconicMenuItem.setHiddenmenuFnctn(screenSettings
								.getMenuFucntionality());
						iconicMenuItem
								.setMenuIconId(ApplicationConstants.BUTTONID
										+ screenSettings.getMenuBtnName()
												.trim());
						iconicMenuItem.setBtnLinkId(screenSettings
								.getBtnLinkId());
						iconicMenuItem.setHiddenBtnLinkId(screenSettings
								.getBtnLinkId());
						iconicMenuItem.setSubCatIds(screenSettings
								.getSubCatIds());
						iconicMenuItem.setHiddenSubCate(screenSettings
								.getChkSubCate());
						iconicMenuItem.setChkSubCate(screenSettings
								.getChkSubCate());
						iconicMenuItem.setCitiId(screenSettings.getCitiId());

						if (!"0".equals(screenSettings.getBtnDept())) {
							iconicMenuItem.setBtnDept(screenSettings
									.getBtnDept());

						} else {

							iconicMenuItem.setBtnDept(null);
						}

						if (!"0".equals(screenSettings.getBtnType())) {
							iconicMenuItem.setBtnType(screenSettings
									.getBtnType());

						} else {

							iconicMenuItem.setBtnType(null);

						}

						iconicMenuItems.add(iconicMenuItem);
						session.setAttribute("previewMenuItems",
								iconicMenuItems);

					}

				}
			}

		}
		// Code for deleting menu item
		else if (null != addorDeleteMenuItem
				&& "DeleteButton".equals(addorDeleteMenuItem)) {

			for (int i = 0; i < iconicMenuItems.size(); i++) {

				ScreenSettings menuItem = iconicMenuItems.get(i);
				if (menuItem.getMenuBtnName().equals(buttonName)) {

					iconicMenuItems.remove(i);
					session.setAttribute("previewMenuItems", iconicMenuItems);
					break;
				}
			}

		}

		if (null != viewName) {
			// Default Image Path
			if ("setuplistmenu".equals(viewName)) {
				session.setAttribute("menuIconPreview",
						ApplicationConstants.DEFAULTIMAGESQR);

			} else if ("iconicmenutemplate".equals(viewName)) {

				session.setAttribute("menuIconPreview",
						ApplicationConstants.DEFAULTIMAGE);
			}
		}

		// Below Code is for Re-Ordering menu buttons
		if (null != btnOrderArry) {
			for (int j = 0; j < btnOrderArry.length; j++) {

				for (int i = 0; i < iconicMenuItems.size(); i++) {
					ScreenSettings btn = iconicMenuItems.get(i);

					if (btn.getMenuIconId().equals(btnOrderArry[j])) {

						sortedMenuItems.add(btn);
						iconicMenuItems.remove(i);
						break;
					}

				}
			}

			if (!iconicMenuItems.isEmpty()) {

				for (int i = 0; i < iconicMenuItems.size(); i++) {

					sortedMenuItems.add(iconicMenuItems.get(i));
				}

			}
			session.setAttribute("previewMenuItems", sortedMenuItems);
		}

		screenSettings = new ScreenSettings();
		screenSettings.setMenuId(menuId);
		screenSettings.setMenuLevel(menuLevel);
		screenSettings.setSubMenuName(subMenuName);
		screenSettings.setBannerImageName(bannerImageName);
		screenSettings.setIsmenuFilterTypeSelected(true);
		screenSettings.setEditFilter(false);
		screenSettings.setMenuFilterType(menuFilterType);
		model.put("screenSettingsForm", screenSettings);

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return viewName;
	}

	/**
	 * Controller Method for saving iconic template
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/savetemplate", method = RequestMethod.POST)
	@ResponseBody
	public final String savetemplate(
			@RequestParam(value = "menuId", required = true) String menuId,
			@RequestParam(value = "menuLevel", required = true) Integer menuLevel,
			@RequestParam(value = "subMenuName", required = true) String subMenuName,
			@RequestParam(value = "bottmBtnId", required = true) String bottmBtnId,
			@RequestParam(value = "template", required = true) String templateType,
			@RequestParam(value = "bannerImg", required = true) String bannerImg,
			@RequestParam(value = "typeFilter", required = true) boolean tyepFilter,
			@RequestParam(value = "deptFilter", required = true) boolean deptFilter,
			@RequestParam(value = "btnOrder", required = true) String btnOrder,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws HubCitiServiceException {

		String methodName = "saveiconictemplate";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String responseStr = null;
		String btnOrderArry[] = null;
		String loginUserType = (String) session.getAttribute("loginUserType");
		List<ScreenSettings> sortedMenuItems = new ArrayList<ScreenSettings>();
		try {
			final ServletContext servletContext = request.getSession()
					.getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext
					.getBean("hubCitiService");
			// List of Menu items

			String textFuctn = ((Integer) session.getAttribute("textFctnId"))
					.toString();

			List<ScreenSettings> previewMenuItems = (ArrayList<ScreenSettings>) session
					.getAttribute("previewMenuItems");
			User user = (User) session.getAttribute("loginUser");

			if (deptFilter) {

				for (int i = 0; i < previewMenuItems.size(); i++) {
					ScreenSettings btn = previewMenuItems.get(i);

					if (!btn.getMenuFucntionality().equals(textFuctn)
							&& null == btn.getBtnDept()) {

						responseStr = "Please associate Department for all the bottons to continue saving";
						return ASSOCIATEDEPT;
					}

				}

			}
			if (tyepFilter) {

				for (int i = 0; i < previewMenuItems.size(); i++) {
					ScreenSettings btn = previewMenuItems.get(i);

					if (!btn.getMenuFucntionality().equals(textFuctn)
							&& null == btn.getBtnType()) {

						responseStr = "Please associate Type for all the bottons to continue saving";
						return ASSOCIATETYPE;
					}

				}

			}

			if ("RegionApp".equalsIgnoreCase(loginUserType)) {
				for (int i = 0; i < previewMenuItems.size(); i++) {
					ScreenSettings btn = previewMenuItems.get(i);

					if (!btn.getMenuFucntionality().equals(textFuctn)
							&& null == btn.getCitiId()) {
						responseStr = "Please associate City for all the bottons to continue saving";
						return ASSOCIATECITY;
					}

				}
			}

			if (!"".equals(Utility.checkNull(btnOrder))) {

				btnOrderArry = btnOrder.split("~");
				for (int j = 0; j < btnOrderArry.length; j++) {

					for (int i = 0; i < previewMenuItems.size(); i++) {
						ScreenSettings btn = previewMenuItems.get(i);

						if (btn.getMenuIconId().equals(btnOrderArry[j])) {

							sortedMenuItems.add(btn);
							previewMenuItems.remove(i);
							break;
						}

					}
				}

				if (ApplicationConstants.GROUPEDTABTEMPLATE
						.equals(templateType)) {
					List<GroupTemplate> groupedBtnList = (ArrayList<GroupTemplate>) session
							.getAttribute("groupedBtnList");
					for (GroupTemplate objGroupedTemplate : groupedBtnList) {
						for (ScreenSettings obj : objGroupedTemplate
								.getGrpBtnsList()) {
							obj.setImagePath(null);
							obj.setLogoImageName(null);
						}
					}
					session.setAttribute("groupedBtnList", groupedBtnList);
				}

				session.setAttribute("previewMenuItems", sortedMenuItems);

				/*
				 * Added to add images in grouped tab template To check image
				 * path exist for Grouped Tab with Image. For Grouped Tab
				 * (without Image), checking images path - if path exist setting
				 * it to null.
				 */
				if (ApplicationConstants.GROUPEDTABTEMPLATEWITHIMG
						.equalsIgnoreCase(templateType)
						|| ApplicationConstants.GROUPEDTABTEMPLATE
								.equalsIgnoreCase(templateType)) {
					Integer menuType = (Integer) session
							.getAttribute("textFctnId");
					for (int i = 0; i < sortedMenuItems.size(); i++) {
						ScreenSettings objScreenSettings = new ScreenSettings();
						objScreenSettings = sortedMenuItems.get(i);
						if (menuType != Integer.parseInt(objScreenSettings
								.getMenuFucntionality())) {
							if (ApplicationConstants.GROUPEDTABTEMPLATEWITHIMG
									.equalsIgnoreCase(templateType)
									&& (objScreenSettings.getImagePath() == null || ""
											.equals(objScreenSettings
													.getImagePath().trim()))) {
								return UPLOADIMAGE;
							} else if (ApplicationConstants.GROUPEDTABTEMPLATE
									.equalsIgnoreCase(templateType)
									&& objScreenSettings.getImagePath() != null) {
								objScreenSettings.setImagePath(null);
								objScreenSettings.setLogoImageName(null);
							}
						}
					}
				}

				responseStr = hubCitiService.saveUpdateIconicMenuTemplate(user,
						sortedMenuItems, Integer.valueOf(menuId), menuLevel,
						subMenuName, bottmBtnId, templateType, bannerImg,
						tyepFilter, deptFilter);

			}
			/*
			 * else { responseStr =
			 * hubCitiService.saveUpdateIconicMenuTemplate(user,
			 * previewMenuItems, Integer.valueOf(menuId), menuLevel,
			 * subMenuName, bottmBtnId, templateType, bannerImg, tyepFilter,
			 * deptFilter); }
			 */

		} catch (HubCitiServiceException e) {
			throw new HubCitiServiceException(e);
		}
		return responseStr;
	}

	/**
	 * This Method will return the main menu Screen.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/displaymainmenu.htm", method = RequestMethod.GET)
	public ModelAndView displayMenu(HttpServletRequest request, ModelMap model,
			HttpSession session) throws HubCitiServiceException {
		String methodName = "displayMainMenu";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		// session.removeAttribute("iconicMenuItems");
		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");
		session.setAttribute(ApplicationConstants.MENUNAME,
				ApplicationConstants.MAINMENUSCREEN);
		User user = (User) session.getAttribute("loginUser");
		MenuDetails menuDetails = null;
		MenuDetails mainMenuDetails = null;
		List<MenuDetails> subMenuList = null;
		AnythingPages anythingPageList = null;
		List<ScreenSettings> tabBarButtonsList = null;
		List<ScreenSettings> menuBarButtonsList = null;
		List<ScreenSettings> btnTypeList = null;
		List<Category> businessCategoryList = null;
		ScreenSettings mainMenuUIDetails = null;
		ScreenSettings subMenuUIDetails = null;
		ScreenSettings subMenuLstObj = new ScreenSettings();
		SubMenuDetails subMenuDetailsObj = null;
		session.removeAttribute("groupList");
		session.removeAttribute("groupedBtnList");
		session.removeAttribute("menuFilterType");
		AlertCategory fundraiserCategoryLst = null;
		AlertCategory eventCategoryList = null;
		try {
			displayAppSites(request, session, model);
			List<MenuDetails> linkList = hubCitiService.getLinkList();
			menuDetails = new MenuDetails();
			menuDetails.setLevel(1);
			menuDetails.setMenuId(0);
			mainMenuDetails = hubCitiService.displayMenu(menuDetails, user);
			subMenuDetailsObj = hubCitiService.displaySubMenu(user,
					subMenuLstObj);
			if (null != subMenuDetailsObj) {
				subMenuList = subMenuDetailsObj.getSubMenuList();
			}

			anythingPageList = hubCitiService.displayAnythingPage(user, null,
					null);

			tabBarButtonsList = hubCitiService.getTabBarButtons(null, user);
			session.setAttribute("tabBarButtonsList", tabBarButtonsList);

			businessCategoryList = hubCitiService.fetchBusinessCategoryList();
			session.setAttribute("businessCatList", businessCategoryList);

			btnTypeList = hubCitiService.getMenuButtonType();
			session.setAttribute("btnTypeList", btnTypeList);

			// Start:Added code for Task:Event Category Changes
			eventCategoryList = hubCitiService.fetchEventCategories(null, user);
			if (null != eventCategoryList
					&& !eventCategoryList.getAlertCatLst().isEmpty()) {
				session.setAttribute("eventCatList",
						eventCategoryList.getAlertCatLst());
			} else {
				List<Category> eventCatLst = new ArrayList<Category>();
				session.setAttribute("eventCatList", eventCatLst);
			}

			// End: Added code for Task:Event Category Changes

			// Start :Adding code for displaying fundraiser categories

			fundraiserCategoryLst = hubCitiService
					.fetchFundraiserEventCategories(user);

			if (null != fundraiserCategoryLst
					&& !fundraiserCategoryLst.getAlertCatLst().isEmpty()) {
				session.setAttribute("fundraiserCatList",
						fundraiserCategoryLst.getAlertCatLst());

			} else {

				session.setAttribute("fundraiserCatList", null);

			}

			// End : Adding code for displaying fundraiser categories

			mainMenuUIDetails = hubCitiService.fetchGeneralSettings(
					user.getHubCitiID(), "MainMenu");
			if (null == mainMenuUIDetails) {
				session.setAttribute("mainMenuBGType", "Color");
				session.setAttribute("mainMenuBG", "#d1d1d1");
				session.setAttribute("mainMenuBtnClr", "#2B88D9");
				session.setAttribute("mainMenuFntClr", "#FFFFFF");
				session.setAttribute("mainMenuGrpClr", "#1A4A6E");
				session.setAttribute("mainMenuGrpFntClr", "#FFFFFF");
				session.setAttribute("mainMenuIconsFntClr", "#FFFFFF");

			} else {

				if (null != mainMenuUIDetails.getBgColor()
						&& !"".equalsIgnoreCase(mainMenuUIDetails.getBgColor())) {
					session.setAttribute("mainMenuBGType", "Color");
					session.setAttribute("mainMenuBG",
							mainMenuUIDetails.getBgColor());
				}

				if (null != mainMenuUIDetails.getLogoImageName()
						&& !"".equalsIgnoreCase(mainMenuUIDetails
								.getLogoImageName())) {
					session.setAttribute("mainMenuBGType", "Image");
					session.setAttribute("mainMenuBG",
							mainMenuUIDetails.getLogoPath());
				}
				session.setAttribute("mainMenuBtnClr",
						mainMenuUIDetails.getBtnColor());
				session.setAttribute("mainMenuFntClr",
						mainMenuUIDetails.getBtnFontColor());
				session.setAttribute("mainMenuGrpClr",
						mainMenuUIDetails.getGrpColor());
				session.setAttribute("mainMenuGrpFntClr",
						mainMenuUIDetails.getGrpFontColor());
				session.setAttribute("mainMenuIconsFntClr",
						mainMenuUIDetails.getIconsFontColor());

			}

			subMenuUIDetails = hubCitiService.fetchGeneralSettings(
					user.getHubCitiID(), "SubMenu");
			if (null == subMenuUIDetails) {
				session.setAttribute("subMenuBG", "#d1d1d1");
				session.setAttribute("subMenuBtnClr", "#2B88D9");
				session.setAttribute("subMenuFntClr", "#FFFFFF");
				session.setAttribute("subMenuGrpClr", "#1A4A6E");
				session.setAttribute("subMenuGrpFntClr", "#FFFFFF");
				session.setAttribute("subMenuIconsFntClr", "#FFFFFF");
			} else {

				if (null != subMenuUIDetails.getBgColor()
						&& !"".equalsIgnoreCase(subMenuUIDetails.getBgColor())) {
					session.setAttribute("subMenuBGType", "Color");
					session.setAttribute("subMenuBG",
							subMenuUIDetails.getBgColor());
				}

				if (null != subMenuUIDetails.getLogoImageName()
						&& !"".equalsIgnoreCase(subMenuUIDetails
								.getLogoImageName())) {
					session.setAttribute("subMenuBGType", "Image");
					session.setAttribute("subMenuBG",
							subMenuUIDetails.getLogoPath());
				}
				session.setAttribute("subMenuBtnClr",
						subMenuUIDetails.getBtnColor());
				session.setAttribute("subMenuFntClr",
						subMenuUIDetails.getBtnFontColor());
				session.setAttribute("subMenuGrpClr",
						subMenuUIDetails.getGrpColor());
				session.setAttribute("subMenuGrpFntClr",
						subMenuUIDetails.getGrpFontColor());
				session.setAttribute("subMenuIconsFntClr",
						subMenuUIDetails.getIconsFontColor());

			}

			if (null != subMenuList) {
				session.setAttribute("subMenuList", subMenuList);

			}
			if (null != anythingPageList) {
				session.setAttribute("anythingPageList",
						anythingPageList.getPageDetails());

			}
			for (MenuDetails link : linkList) {

				if ("SubMenu".equals(link.getMenuTypeVal())) {
					session.setAttribute("subMenuFctnId", link.getMenuTypeId());
					continue;
				}
				if ("AnythingPage".equals(link.getMenuTypeVal())) {
					session.setAttribute("anythingPageFctnId",
							link.getMenuTypeId());
					continue;

				}
				if ("AppSite".equals(link.getMenuTypeVal())) {
					session.setAttribute("appSiteFctnId", link.getMenuTypeId());
					continue;
				}

				if ("Text".equals(link.getMenuTypeVal())) {
					session.setAttribute("textFctnId", link.getMenuTypeId());
					continue;
				}
				if ("Label".equals(link.getMenuTypeVal())) {
					session.setAttribute("labelFctnId", link.getMenuTypeId());
					continue;
				}
				if ("City Experience".equals(link.getMenuTypeVal())) {
					session.setAttribute("expFctnId", link.getMenuTypeId());
					continue;
				}
				if ("Find".equals(link.getMenuTypeVal())) {
					session.setAttribute("findFctnId", link.getMenuTypeId());
					continue;
				}
				if ("Events".equals(link.getMenuTypeVal())) {
					session.setAttribute("eventFctnId", link.getMenuTypeId());
					continue;
				}

				if ("Fundraisers".equals(link.getMenuTypeVal())) {
					session.setAttribute("fundraFctnId", link.getMenuTypeId());
					continue;
				}

			}

			if (null != subMenuList) {
				session.setAttribute("subMenuList", subMenuList);

			}

			for (int i = 0; i < linkList.size(); i++) {
				MenuDetails link = linkList.get(i);
				if ("Text".equals(link.getMenuTypeName())
						|| "Label".equals(link.getMenuTypeName())) {
					linkList.remove(i);

				}

			}

			session.setAttribute("linkList", linkList);

			if (null == mainMenuDetails) {
				return new ModelAndView(new RedirectView(
						"displaymenutemplate.htm?menuType=mainmenu"));
			} else {
				menuBarButtonsList = hubCitiService.getTabBarButtons(
						mainMenuDetails, user);
				session.setAttribute("menuBarButtonsList", menuBarButtonsList);

				request.setAttribute("mainMenuSaveBtn", "Update Main Menu");
				session.setAttribute("mainMenuDetails", mainMenuDetails);

				if (null != mainMenuDetails.getMenuTypeName()
						&& mainMenuDetails.getMenuTypeName().equals(
								ApplicationConstants.ICONICTEMPLATE)) {
					session.setAttribute("iconicTempbnrimgPreview",
							mainMenuDetails.getBannerImg());
				} else if (null != mainMenuDetails.getMenuTypeName()
						&& mainMenuDetails
								.getMenuTypeName()
								.equals(ApplicationConstants.TWOCOLTABWITHBANNERVIEWTEMPLATE)) {
					session.setAttribute("bannerimage",
							mainMenuDetails.getBannerImg());
				}

				session.setAttribute("menuTemplteName",
						mainMenuDetails.getMenuTypeName());

				// for single/two column implementation.

				if (null != mainMenuDetails.getMenuTypeName()
						&& mainMenuDetails.getMenuTypeName().equals(
								ApplicationConstants.TWOCOLTABVIEWTEMPLATE)) {
					if (null != mainMenuDetails.getNoOfColumns()) {
						Integer iNoOfColumns = mainMenuDetails.getNoOfColumns();
						if (iNoOfColumns == 1) {
							session.setAttribute("colBtnView",
									ApplicationConstants.SINGLECOLUMN);
						} else {
							session.setAttribute("colBtnView",
									ApplicationConstants.TWOCOLUMN);
						}
					}
				}

			}

		} catch (HubCitiServiceException e) {
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return new ModelAndView("displaymenu");
	}

	/**
	 * Controller Method for displaying iconic menu template..
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/updatemainmenu.htm", method = RequestMethod.GET)
	public String updateGridViewMenu(HttpServletRequest request,
			ModelMap model, HttpSession session) throws HubCitiServiceException {
		String methodName = "displayMainMenu";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		// Default Image Path
		session.setAttribute("menuIconPreview",
				ApplicationConstants.DEFAULTIMAGE);

		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");
		User user = (User) session.getAttribute("loginUser");

		// Start:Added code for Task: Add Cities to menu buttons
		if ("RegionApp".equalsIgnoreCase((String) session
				.getAttribute("loginUserType"))) {
			List<CityExperience> citiesLst = hubCitiService
					.displayCitiesForRegionApp(user);
			session.setAttribute("citiesLst", citiesLst);
		}
		// End: Added code for Task: Add Cities to menu buttons

		MenuDetails mainMenuDetails = null;
		String returnView = null;
		List<ScreenSettings> iconicMenuItems = null;
		String grpName = null;
		String menuTepmateType = (String) session
				.getAttribute("menuTemplteName");

		ScreenSettings screenSettings = null;
		ScreenSettings iconicMenuItem = null;

		mainMenuDetails = (MenuDetails) session.getAttribute("mainMenuDetails");
		if (null != mainMenuDetails) {
			LOG.info("mainMenuDetails in not empty");
			screenSettings = new ScreenSettings();
			screenSettings
					.setMenuId(String.valueOf(mainMenuDetails.getMenuId()));
			screenSettings.setMenuLevel(mainMenuDetails.getLevel());
			// if(null != screenSettings.getbt)

			List<ButtonDetails> buttonDetails = mainMenuDetails.getButtons();
			ButtonDetails btnDetails = buttonDetails.get(0);
			String pageTitle = btnDetails.getFunctnName();
			screenSettings.setPageTitle(pageTitle);
			String findBtnCatId = null;
			iconicMenuItems = new ArrayList<ScreenSettings>();
			StringBuilder findCategories = null;
			for (ButtonDetails buttonDetail : buttonDetails) {
				iconicMenuItem = new ScreenSettings();
				iconicMenuItem.setLogoImageName(buttonDetail.getImageName());
				iconicMenuItem.setImagePath(buttonDetail.getBtnImage());
				iconicMenuItem.setMenuBtnName(buttonDetail.getBtnName());
				iconicMenuItem
						.setMenuFucntionality(buttonDetail.getBtnAction());
				iconicMenuItem
						.setMenuFucntionality(buttonDetail.getBtnAction());
				iconicMenuItem.setMenuIconId(ApplicationConstants.BUTTONID
						+ buttonDetail.getBtnName().trim());
				iconicMenuItem.setComboBtnType(buttonDetail.getComboBtnType());
				iconicMenuItem.setComboBtnTypeId(buttonDetail
						.getComboBtnTypeId());
				iconicMenuItem.setCitiId(buttonDetail.getCitiId());

				if (null != buttonDetail.getLinkId()) {

					if (!Utility.isEmptyOrNullString(buttonDetail.getFunType())) {
						if (buttonDetail.getFunType().equals("Find")) {
							findBtnCatId = buttonDetail.getLinkId();

							if (!Utility.isEmptyOrNullString(findBtnCatId)) {
								if (findBtnCatId.contains(",")) {
									findCategories = new StringBuilder();
									String[] strings = findBtnCatId.split(",");
									for (int i = 0; i < strings.length; i++) {
										findCategories.append(strings[i]
												+ "-MC");
										findCategories.append(",");
									}
									findBtnCatId = findCategories.toString();
								} else {
									findBtnCatId = buttonDetail.getLinkId()
											+ "-MC";
								}

							}

							iconicMenuItem.setBtnLinkId(findBtnCatId);

						} else {
							iconicMenuItem.setBtnLinkId(buttonDetail
									.getLinkId());
						}
					} else {
						iconicMenuItem.setBtnLinkId(buttonDetail.getLinkId());
					}
					iconicMenuItem.setChkSubCate(buttonDetail
							.getFindSubCatIds());
					iconicMenuItem.setSubCatIds(buttonDetail.getSvdSubCate());
					iconicMenuItem.setHiddenSubCate(buttonDetail
							.getFindSubCatIds());
				}

				if (null != buttonDetail.getFunctnName()
						&& (ApplicationConstants.TEXT.equals(buttonDetail
								.getFunctnName()) || ApplicationConstants.LABEL
								.equals(buttonDetail.getFunctnName()))) {
					grpName = buttonDetail.getBtnName();
					iconicMenuItem.setMenuIconId(ApplicationConstants.TEXT
							+ "-" + grpName);
					iconicMenuItem.setPageTitle(pageTitle);
				} else {
					iconicMenuItem.setMenuIconId(ApplicationConstants.BUTTONID
							+ buttonDetail.getBtnName().trim());
					iconicMenuItem.setPageTitle(pageTitle);
				}

				iconicMenuItem.setBtnGroup(grpName);
				if (null != grpName) {
					iconicMenuItem.setHiddenbtnGroup(grpName.replace(" ", ""));
				}
				iconicMenuItems.add(iconicMenuItem);
			}

			// session.setAttribute("twocolmenuitems", iconicMenuItems);
			// session.setAttribute("iconicMenuItems", iconicMenuItems);

			session.setAttribute("previewMenuItems", iconicMenuItems);
			// session.setAttribute("iconicMenuItems", iconicMenuItems);
			LOG.info("Template Name:" + menuTepmateType);
			if (null != menuTepmateType) {

				if (menuTepmateType.equals(ApplicationConstants.ICONICTEMPLATE)) {
					session.setAttribute("minCropHt", 50);
					session.setAttribute("minCropWd", 50);
					returnView = "iconicmenutemplate";
					// Default Image Path
					session.setAttribute("menuIconPreview",
							ApplicationConstants.DEFAULTIMAGE);
					session.setAttribute("iconicTempbnrimgPreview",
							mainMenuDetails.getBannerImg());

					screenSettings.setBannerImageName(mainMenuDetails
							.getBannerImageName());
				} else if (menuTepmateType
						.equals(ApplicationConstants.LISTVIEWTEMPLATE)) {

					session.setAttribute("minCropHt", 40);
					session.setAttribute("minCropWd", 40);
					returnView = "setuplistmenu";
					// Default Image Path
					session.setAttribute("menuIconPreview",
							ApplicationConstants.DEFAULTIMAGESQR);
				} else if (menuTepmateType
						.equals(ApplicationConstants.TWOCOLTABWITHBANNERVIEWTEMPLATE)) {
					session.setAttribute("minCropHt", 107);
					session.setAttribute("minCropWd", 768);
					returnView = "twocoltabview";
					// Default Image Path
					session.setAttribute("twocoltemp", menuTepmateType);
					session.setAttribute("menuIconPreview",
							mainMenuDetails.getBannerImg());
					session.setAttribute("bannerimage",
							mainMenuDetails.getBannerImg());
					// session.setAttribute("menuIconPreview",
					// ApplicationConstants.DEFAULTBANNERIMAGE);
					screenSettings.setLogoImageName(mainMenuDetails
							.getBannerImageName());

				} else if (menuTepmateType
						.equals(ApplicationConstants.TWOCOLTABVIEWTEMPLATE)) {
					// Adding code for single column template.

					Integer iNoColums = mainMenuDetails.getNoOfColumns();
					// for setting button view is single column or
					// two column
					if (null != iNoColums && !"".equals(iNoColums)) {
						if (iNoColums == 1) {
							session.setAttribute("colBtnView",
									ApplicationConstants.SINGLECOLUMN);
						} else {
							session.setAttribute("colBtnView",
									ApplicationConstants.TWOCOLUMN);
						}
					}

					returnView = "twocoltabview";
					session.setAttribute("twocoltemp",
							ApplicationConstants.TWOCOLTABVIEWTEMPLATE);
				} else if (menuTepmateType
						.equals(ApplicationConstants.GROUPEDTABTEMPLATE)
						|| menuTepmateType
								.equals(ApplicationConstants.GROUPEDTABTEMPLATEWITHIMG)) {
					// ArrayList<GroupTemplate> groupedBtnList
					// =(ArrayList<GroupTemplate>)
					// session.getAttribute
					// ("groupedBtnList");
					session.setAttribute("minCropHt", 40);
					session.setAttribute("minCropWd", 40);
					session.setAttribute("menuIconPreview",
							ApplicationConstants.DEFAULTIMAGESQR);
					if (menuTepmateType
							.equals(ApplicationConstants.GROUPEDTABTEMPLATEWITHIMG)) {
						screenSettings.setIsGroupImg("true");
					} else {
						screenSettings.setIsGroupImg("false");
					}
					List<GroupTemplate> groupedBtnList = new ArrayList<GroupTemplate>();
					List<ScreenSettings> grpBtnsList = null;
					ButtonDetails buttonDetail = null;
					GroupTemplate groupTemplate = null;
					List<String> groupList = new ArrayList<String>();
					for (int i = 0; i < buttonDetails.size(); i++) {
						buttonDetail = buttonDetails.get(i);

						if (null != buttonDetail.getFunctnName()
								&& ApplicationConstants.TEXT
										.equals(buttonDetail.getFunctnName())) {
							groupTemplate = new GroupTemplate();
							grpBtnsList = new ArrayList<ScreenSettings>();
							grpName = buttonDetail.getBtnName();
							groupTemplate.setGrpName(grpName);
							groupList.add(buttonDetail.getBtnName());
							for (int j = i + 1; j < buttonDetails.size(); j++) {
								buttonDetail = buttonDetails.get(j);
								if (null == buttonDetail.getFunctnName()) {

									ScreenSettings btn = new ScreenSettings();

									btn.setMenuBtnName(buttonDetail
											.getBtnName());
									btn.setMenuFucntionality(buttonDetail
											.getBtnAction());
									btn.setHiddenmenuFnctn(buttonDetail
											.getBtnAction());
									btn.setMenuIconId(ApplicationConstants.BUTTONID
											+ buttonDetail.getBtnName().trim());
									btn.setCitiId(buttonDetail.getCitiId());

									// To add image in grouped tab template
									btn.setImagePath(buttonDetail.getBtnImage());
									btn.setLogoImageName(buttonDetail
											.getImageName());

									if (null != buttonDetail.getLinkId()) {
										if (!Utility
												.isEmptyOrNullString(buttonDetail
														.getFunType())) {
											if (buttonDetail.getFunType()
													.equals("Find")) {
												findBtnCatId = buttonDetail
														.getLinkId();
												if (!Utility
														.isEmptyOrNullString(findBtnCatId)) {
													if (findBtnCatId
															.contains(",")) {
														findCategories = new StringBuilder();
														String[] strings = findBtnCatId
																.split(",");
														for (int cat = 0; cat < strings.length; cat++) {
															findCategories
																	.append(strings[cat]
																			+ "-MC");
															findCategories
																	.append(",");
														}
														findBtnCatId = findCategories
																.toString();
													} else {
														findBtnCatId = buttonDetail
																.getLinkId()
																+ "-MC";
													}

												}
												btn.setBtnLinkId(findBtnCatId);
												btn.setHiddenBtnLinkId(findBtnCatId);
											} else {
												btn.setBtnLinkId(buttonDetail
														.getLinkId());
												btn.setHiddenBtnLinkId(buttonDetail
														.getLinkId());
											}
										} else {
											btn.setBtnLinkId(buttonDetail
													.getLinkId());
											btn.setHiddenBtnLinkId(buttonDetail
													.getLinkId());
										}
										btn.setSubCatIds(buttonDetail
												.getSvdSubCate());
										btn.setChkSubCate(buttonDetail
												.getFindSubCatIds());
										btn.setHiddenSubCate(buttonDetail
												.getFindSubCatIds());
									}
									btn.setBtnGroup(grpName);
									grpBtnsList.add(btn);

									if (j == buttonDetails.size() - 1) {
										groupTemplate
												.setGrpBtnsList(grpBtnsList);
										groupedBtnList.add(groupTemplate);
										break;
									}
								} else {
									groupTemplate.setGrpBtnsList(grpBtnsList);
									groupedBtnList.add(groupTemplate);
									i = j - 1;
									break;
								}

							}

						}

					}

					session.setAttribute("groupedBtnList", groupedBtnList);
					session.setAttribute("groupList", groupList);
					returnView = "setupgroupmenu";
				} else if (menuTepmateType
						.equals(ApplicationConstants.COMBOTEMPLATE)) {
					session.setAttribute("setupcombomenuImgSquare",
							ApplicationConstants.DEFAULTIMAGESQR);
					session.setAttribute("setupcombomenuImgCircle",
							ApplicationConstants.DEFAULTIMAGE);
					session.setAttribute("minCropHt", 60);
					session.setAttribute("minCropWd", 60);

					List<GroupTemplate> groupedBtnList = new ArrayList<GroupTemplate>();
					List<ScreenSettings> grpBtnsList = null;
					ButtonDetails buttonDetail = null;
					GroupTemplate groupTemplate = null;
					List<String> groupList = new ArrayList<String>();
					for (int i = 0; i < buttonDetails.size(); i++) {
						buttonDetail = buttonDetails.get(i);

						if (null != buttonDetail.getFunctnName()
								&& (ApplicationConstants.TEXT
										.equals(buttonDetail.getFunctnName()) || ApplicationConstants.LABEL
										.equals(buttonDetail.getFunctnName()))) {
							groupTemplate = new GroupTemplate();
							grpBtnsList = new ArrayList<ScreenSettings>();
							grpName = buttonDetail.getBtnName();
							groupTemplate.setGrpName(grpName);
							groupList.add(buttonDetail.getBtnName());
							for (int j = i + 1; j < buttonDetails.size(); j++) {
								buttonDetail = buttonDetails.get(j);
								if (null == buttonDetail.getFunctnName()) {

									ScreenSettings btn = new ScreenSettings();

									btn.setLogoImageName(buttonDetail
											.getImageName());
									btn.setImagePath(buttonDetail.getBtnImage());
									btn.setMenuBtnName(buttonDetail
											.getBtnName());
									btn.setMenuFucntionality(buttonDetail
											.getBtnAction());
									btn.setHiddenmenuFnctn(buttonDetail
											.getBtnAction());
									btn.setComboBtnType(buttonDetail
											.getComboBtnType());
									btn.setComboBtnTypeId(buttonDetail
											.getComboBtnTypeId());
									btn.setMenuIconId(ApplicationConstants.BUTTONID
											+ buttonDetail.getBtnName().trim());
									btn.setCitiId(buttonDetail.getCitiId());
									if (null != buttonDetail.getLinkId()) {
										if (!Utility
												.isEmptyOrNullString(buttonDetail
														.getFunType())) {
											if (buttonDetail.getFunType()
													.equals("Find")) {
												findBtnCatId = buttonDetail
														.getLinkId();
												if (!Utility
														.isEmptyOrNullString(findBtnCatId)) {
													if (findBtnCatId
															.contains(",")) {
														findCategories = new StringBuilder();
														String[] strings = findBtnCatId
																.split(",");
														for (int cat = 0; cat < strings.length; cat++) {
															findCategories
																	.append(strings[cat]
																			+ "-MC");
															findCategories
																	.append(",");
														}
														findBtnCatId = findCategories
																.toString();
													} else {
														findBtnCatId = buttonDetail
																.getLinkId()
																+ "-MC";
													}

												}
												btn.setBtnLinkId(findBtnCatId);
												btn.setHiddenBtnLinkId(findBtnCatId);
											} else {
												btn.setBtnLinkId(buttonDetail
														.getLinkId());
												btn.setHiddenBtnLinkId(buttonDetail
														.getLinkId());
											}
										} else {
											btn.setBtnLinkId(buttonDetail
													.getLinkId());
											btn.setHiddenBtnLinkId(buttonDetail
													.getLinkId());
										}
										btn.setSubCatIds(buttonDetail
												.getSvdSubCate());
										btn.setChkSubCate(buttonDetail
												.getFindSubCatIds());
										btn.setHiddenSubCate(buttonDetail
												.getFindSubCatIds());
									}
									btn.setBtnGroup(grpName);
									btn.setBtnDept(buttonDetail.getBtnDept());
									btn.setBtnType(buttonDetail.getBtnType());
									grpBtnsList.add(btn);

									if (j == buttonDetails.size() - 1) {
										groupTemplate
												.setGrpBtnsList(grpBtnsList);
										groupedBtnList.add(groupTemplate);
										break;
									}
								} else {
									groupTemplate.setGrpBtnsList(grpBtnsList);
									groupedBtnList.add(groupTemplate);
									i = j - 1;
									break;
								}

							}

						}

					}

					session.setAttribute("comboBtnList", groupedBtnList);
					session.setAttribute("comboList", groupList);
					returnView = "setupcombomenu";
				}
			}
		}

		model.put("screenSettingsForm", screenSettings);
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return returnView;
	}

	/**
	 * This Method will return the main menu Screen.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updatesubmenu.htm", method = RequestMethod.GET)
	public ModelAndView displaySubMenuDetails(HttpServletRequest request,
			ModelMap model, HttpSession session) throws HubCitiServiceException {
		String methodName = "displayMainMenu";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		// session.removeAttribute("iconicMenuItems");
		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");
		session.setAttribute(ApplicationConstants.MENUNAME,
				ApplicationConstants.SETUPSUBMENU);
		User user = (User) session.getAttribute("loginUser");
		String subMenuId = (String) request.getParameter("subMenuId");
		MenuDetails menuDetails = null;
		MenuDetails subMenuDetails = null;
		List<ScreenSettings> iconicMenuItems = null;
		List<ScreenSettings> menuBarButtonsList = null;
		ScreenSettings screenSettings = null;
		ScreenSettings iconicMenuItem = null;
		// Default Image Path
		session.setAttribute("menuIconPreview",
				ApplicationConstants.DEFAULTIMAGE);
		session.setAttribute("iconicTempbnrimgPreview",
				ApplicationConstants.DEFAULTBANNERPREVIEWIMAGE);
		session.setAttribute("bannerimage",
				ApplicationConstants.DEFAULTBANNERPREVIEWIMAGE);
		String returnView = null;
		List<MenuDetails> subMenuList = null;
		String grpName = null;
		List<String> menuFilterTypeNames = new ArrayList<String>();
		MenuFilterTyes menuFilterTyes = null;
		AnythingPages anythingPageList = null;
		List<ScreenSettings> tabBarButtonsList = null;
		List<Category> businessCategoryList = null;
		List<ScreenSettings> btnTypeList = null;
		SubMenuDetails subMenuDetailsObj = null;
		ScreenSettings subMenuUIDetails = null;
		ScreenSettings subMenuLstObj = new ScreenSettings();
		AlertCategory fundraiserCategoryLst = null;
		AlertCategory eventCategoryList = null;
		try {

			subMenuDetailsObj = hubCitiService.displaySubMenu(user,
					subMenuLstObj);
			if (null != subMenuDetailsObj) {
				subMenuList = subMenuDetailsObj.getSubMenuList();
			}
			if (null != subMenuList) {
				session.setAttribute("subMenuList", subMenuList);

			}

			tabBarButtonsList = hubCitiService.getTabBarButtons(null, user);
			session.setAttribute("tabBarButtonsList", tabBarButtonsList);

			businessCategoryList = hubCitiService.fetchBusinessCategoryList();
			session.setAttribute("businessCatList", businessCategoryList);

			btnTypeList = hubCitiService.getMenuButtonType();
			session.setAttribute("btnTypeList", btnTypeList);

			// Start:Added code for Task:Event Category Changes
			eventCategoryList = hubCitiService.fetchEventCategories(null, user);
			if (null != eventCategoryList
					&& !eventCategoryList.getAlertCatLst().isEmpty()) {
				session.setAttribute("eventCatList",
						eventCategoryList.getAlertCatLst());
			} else {
				List<Category> eventCatLst = new ArrayList<Category>();
				session.setAttribute("eventCatList", eventCatLst);
			}

			// End: Added code for Task:Event Category Changes

			// Start :Adding code for displaying fundraiser categories

			fundraiserCategoryLst = hubCitiService
					.fetchFundraiserEventCategories(user);

			if (null != fundraiserCategoryLst
					&& !fundraiserCategoryLst.getAlertCatLst().isEmpty()) {
				session.setAttribute("fundraiserCatList",
						fundraiserCategoryLst.getAlertCatLst());

			} else {

				session.setAttribute("fundraiserCatList", null);

			}

			// End : Adding code for displaying fundraiser categories

			// Start:Added code for Task: Add Cities to menu buttons
			if ("RegionApp".equalsIgnoreCase((String) session
					.getAttribute("loginUserType"))) {
				List<CityExperience> citiesLst = hubCitiService
						.displayCitiesForRegionApp(user);
				session.setAttribute("citiesLst", citiesLst);
			}
			// End: Added code for Task: Add Cities to menu buttons

			displayAppSites(request, session, model);
			menuFilterTyes = hubCitiService.getMenuFilterTypes(user
					.getHubCitiID());
			session.setAttribute("filterDeptList",
					menuFilterTyes.getDeptNameList());
			session.setAttribute("filterTypeList",
					menuFilterTyes.getTypeNameList());

			anythingPageList = hubCitiService.displayAnythingPage(user, null,
					null);

			if (null != anythingPageList) {
				session.setAttribute("anythingPageList",
						anythingPageList.getPageDetails());

			}

			if (null != subMenuId && !"".equals(subMenuId)) {

				List<MenuDetails> linkList = hubCitiService.getLinkList();

				for (MenuDetails link : linkList) {

					if ("SubMenu".equals(link.getMenuTypeVal())) {
						session.setAttribute("subMenuFctnId",
								link.getMenuTypeId());
						continue;
					}
					if ("AnythingPage".equals(link.getMenuTypeVal())) {
						session.setAttribute("anythingPageFctnId",
								link.getMenuTypeId());
						continue;

					}
					if ("AppSite".equals(link.getMenuTypeVal())) {
						session.setAttribute("appSiteFctnId",
								link.getMenuTypeId());
						continue;
					}

					if ("Text".equals(link.getMenuTypeVal())) {
						session.setAttribute("textFctnId", link.getMenuTypeId());
						continue;
					}
					if ("Label".equals(link.getMenuTypeVal())) {
						session.setAttribute("labelFctnId",
								link.getMenuTypeId());
						continue;
					}
					if ("City Experience".equals(link.getMenuTypeVal())) {
						session.setAttribute("expFctnId", link.getMenuTypeId());
						continue;
					}
					if ("Find".equals(link.getMenuTypeVal())) {
						session.setAttribute("findFctnId", link.getMenuTypeId());
						continue;
					}
					if ("Events".equals(link.getMenuTypeVal())) {
						session.setAttribute("eventFctnId",
								link.getMenuTypeId());
						continue;
					}
					if ("Fundraisers".equals(link.getMenuTypeVal())) {
						session.setAttribute("fundraFctnId",
								link.getMenuTypeId());
						continue;
					}

				}

				for (int i = 0; i < linkList.size(); i++) {
					MenuDetails link = linkList.get(i);
					if ("Text".equals(link.getMenuTypeName())
							|| "Label".equals(link.getMenuTypeName())) {
						linkList.remove(i);
					}
				}

				session.setAttribute("linkList", linkList);
				menuDetails = new MenuDetails();
				menuDetails.setLevel(null);
				menuDetails.setMenuId(Integer.valueOf(subMenuId));
				subMenuDetails = hubCitiService.displayMenu(menuDetails, user);
				String findBtnCatId = null;
				StringBuilder findCategories = null;
				if (null != subMenuDetails) {
					screenSettings = new ScreenSettings();
					screenSettings.setMenuId(String.valueOf(subMenuDetails
							.getMenuId()));
					screenSettings.setMenuLevel(subMenuDetails.getLevel());
					screenSettings.setSubMenuName(subMenuDetails.getMenuName());

					List<ButtonDetails> buttonDetails = subMenuDetails
							.getButtons();

					ButtonDetails btnDetails = buttonDetails.get(0);
					String pageTitle = btnDetails.getFunctnName();
					screenSettings.setPageTitle(pageTitle);

					iconicMenuItems = new ArrayList<ScreenSettings>();

					for (ButtonDetails buttonDetail : buttonDetails) {
						iconicMenuItem = new ScreenSettings();
						iconicMenuItem.setLogoImageName(buttonDetail
								.getImageName());
						iconicMenuItem.setImagePath(buttonDetail.getBtnImage());
						iconicMenuItem
								.setMenuBtnName(buttonDetail.getBtnName());
						iconicMenuItem.setMenuFucntionality(buttonDetail
								.getBtnAction());
						iconicMenuItem.setHiddenmenuFnctn(buttonDetail
								.getBtnAction());
						iconicMenuItem.setComboBtnType(buttonDetail
								.getComboBtnType());
						iconicMenuItem.setComboBtnTypeId(buttonDetail
								.getComboBtnTypeId());

						if (null != buttonDetail.getLinkId()) {

							if (!Utility.isEmptyOrNullString(buttonDetail
									.getFunType())) {
								if (buttonDetail.getFunType().equals("Find")) {
									findBtnCatId = buttonDetail.getLinkId();

									if (!Utility
											.isEmptyOrNullString(findBtnCatId)) {
										if (findBtnCatId.contains(",")) {
											findCategories = new StringBuilder();
											String[] strings = findBtnCatId
													.split(",");
											for (int i = 0; i < strings.length; i++) {
												findCategories
														.append(strings[i]
																+ "-MC");
												findCategories.append(",");
											}
											findBtnCatId = findCategories
													.toString();
										} else {
											findBtnCatId = buttonDetail
													.getLinkId() + "-MC";
										}

									}

									iconicMenuItem.setBtnLinkId(findBtnCatId);

								} else {
									iconicMenuItem.setBtnLinkId(buttonDetail
											.getLinkId());
								}
							} else {
								iconicMenuItem.setBtnLinkId(buttonDetail
										.getLinkId());
							}
							iconicMenuItem.setSubCatIds(buttonDetail
									.getSvdSubCate());
							iconicMenuItem.setChkSubCate(buttonDetail
									.getFindSubCatIds());
							iconicMenuItem.setHiddenSubCate(buttonDetail
									.getFindSubCatIds());

						}

						iconicMenuItem.setCitiId(buttonDetail.getCitiId());

						if (null != buttonDetail.getFunctnName()
								&& (ApplicationConstants.TEXT
										.equals(buttonDetail.getFunctnName()) || ApplicationConstants.LABEL
										.equals(buttonDetail.getFunctnName()))) {
							grpName = buttonDetail.getBtnName();
							iconicMenuItem
									.setMenuIconId(ApplicationConstants.TEXT
											+ "-" + grpName);
							iconicMenuItem.setPageTitle(pageTitle);
						} else {
							iconicMenuItem
									.setMenuIconId(ApplicationConstants.BUTTONID
											+ buttonDetail.getBtnName().trim());
							iconicMenuItem.setPageTitle(pageTitle);
						}
						if (subMenuDetails.isDepartmentFlag()) {
							iconicMenuItem
									.setBtnDept(buttonDetail.getBtnDept());

						}
						if (subMenuDetails.isTypeFlag()) {

							iconicMenuItem
									.setBtnType(buttonDetail.getBtnType());

						}

						iconicMenuItem.setBtnGroup(grpName);
						if (null != grpName) {
							iconicMenuItem.setHiddenbtnGroup(grpName.replace(
									" ", ""));
						}

						iconicMenuItems.add(iconicMenuItem);
					}

					session.setAttribute("previewMenuItems", iconicMenuItems);
					LOG.info("Return View before:"
							+ subMenuDetails.getMenuTypeName());
					LOG.info("Return View before:"
							+ ApplicationConstants.TWOCOLTABVIEWTEMPLATE);

					if (subMenuDetails.getMenuTypeName().equals(
							ApplicationConstants.LISTVIEWTEMPLATE)) {
						session.setAttribute("minCropHt", 40);
						session.setAttribute("minCropWd", 40);
						returnView = "setuplistmenu";
						// Default Image Path
						session.setAttribute("menuIconPreview",
								ApplicationConstants.DEFAULTIMAGESQR);
					} else if (subMenuDetails.getMenuTypeName().equals(
							ApplicationConstants.ICONICTEMPLATE)) {
						session.setAttribute("minCropHt", 60);
						session.setAttribute("minCropWd", 60);
						returnView = "iconicmenutemplate";
						// Default Image Path
						session.setAttribute("menuIconPreview",
								ApplicationConstants.DEFAULTIMAGE);
						session.setAttribute("iconicTempbnrimgPreview",
								subMenuDetails.getBannerImg());

						screenSettings.setBannerImageName(subMenuDetails
								.getBannerImageName());
					} else if (subMenuDetails
							.getMenuTypeName()
							.equals(ApplicationConstants.TWOCOLTABWITHBANNERVIEWTEMPLATE)) {
						returnView = "twocoltabview";
						session.setAttribute("minCropHt", 107);
						session.setAttribute("minCropWd", 768);
						// Default Image Path
						session.setAttribute(
								"twocoltemp",
								ApplicationConstants.TWOCOLTABWITHBANNERVIEWTEMPLATE);
						session.setAttribute("menuIconPreview",
								subMenuDetails.getBannerImg());
						session.setAttribute("bannerimage",
								subMenuDetails.getBannerImg());
						screenSettings.setLogoImageName(subMenuDetails
								.getBannerImageName());
					} else if (subMenuDetails.getMenuTypeName().equals(
							ApplicationConstants.TWOCOLTABVIEWTEMPLATE)) {
						LOG.info("Matched Return View");

						if (null != subMenuDetails.getNoOfColumns()) {
							Integer iNoOfColumns = subMenuDetails
									.getNoOfColumns();
							if (iNoOfColumns == 1) {
								session.setAttribute("colBtnView",
										ApplicationConstants.SINGLECOLUMN);
							} else {
								session.setAttribute("colBtnView",
										ApplicationConstants.TWOCOLUMN);
							}
						}

						returnView = "twocoltabview";
						session.setAttribute("twocoltemp",
								ApplicationConstants.TWOCOLTABVIEWTEMPLATE);
					} else if (subMenuDetails.getMenuTypeName().equals(
							ApplicationConstants.GROUPEDTABTEMPLATE)
							|| subMenuDetails
									.getMenuTypeName()
									.equals(ApplicationConstants.GROUPEDTABTEMPLATEWITHIMG)) {

						session.setAttribute("minCropHt", 40);
						session.setAttribute("minCropWd", 40);
						session.setAttribute("menuIconPreview",
								ApplicationConstants.DEFAULTIMAGESQR);

						if (subMenuDetails.getMenuTypeName().equals(
								ApplicationConstants.GROUPEDTABTEMPLATEWITHIMG)) {
							screenSettings.setIsGroupImg("true");
						} else {
							screenSettings.setIsGroupImg("false");
						}

						// ArrayList<GroupTemplate>
						// groupedBtnList
						// =(ArrayList<GroupTemplate>)
						// session.getAttribute
						// ("groupedBtnList");
						List<GroupTemplate> groupedBtnList = new ArrayList<GroupTemplate>();
						List<ScreenSettings> grpBtnsList = null;
						ButtonDetails buttonDetail = null;
						GroupTemplate groupTemplate = null;
						List<String> groupList = new ArrayList<String>();
						for (int i = 0; i < buttonDetails.size(); i++) {
							buttonDetail = buttonDetails.get(i);

							if (null != buttonDetail.getFunctnName()
									&& ApplicationConstants.TEXT
											.equals(buttonDetail
													.getFunctnName())) {
								groupTemplate = new GroupTemplate();
								grpBtnsList = new ArrayList<ScreenSettings>();
								grpName = buttonDetail.getBtnName();
								groupTemplate.setGrpName(grpName);
								groupList.add(buttonDetail.getBtnName());
								for (int j = i + 1; j < buttonDetails.size(); j++) {
									buttonDetail = buttonDetails.get(j);
									if (null == buttonDetail.getFunctnName()) {

										ScreenSettings btn = new ScreenSettings();

										btn.setMenuBtnName(buttonDetail
												.getBtnName());
										btn.setMenuFucntionality(buttonDetail
												.getBtnAction());
										btn.setHiddenmenuFnctn(buttonDetail
												.getBtnAction());
										btn.setMenuIconId(ApplicationConstants.BUTTONID
												+ buttonDetail.getBtnName()
														.trim());
										btn.setCitiId(buttonDetail.getCitiId());
										if (null != buttonDetail.getLinkId()) {
											if (!Utility
													.isEmptyOrNullString(buttonDetail
															.getFunType())) {
												if (buttonDetail.getFunType()
														.equals("Find")) {
													findBtnCatId = buttonDetail
															.getLinkId();
													if (!Utility
															.isEmptyOrNullString(findBtnCatId)) {
														if (findBtnCatId
																.contains(",")) {
															findCategories = new StringBuilder();
															String[] strings = findBtnCatId
																	.split(",");
															for (int cat = 0; cat < strings.length; cat++) {
																findCategories
																		.append(strings[cat]
																				+ "-MC");
																findCategories
																		.append(",");
															}
															findBtnCatId = findCategories
																	.toString();
														} else {
															findBtnCatId = buttonDetail
																	.getLinkId()
																	+ "-MC";
														}

													}
													btn.setBtnLinkId(findBtnCatId);
													btn.setHiddenBtnLinkId(findBtnCatId);
												} else {
													btn.setBtnLinkId(buttonDetail
															.getLinkId());
													btn.setHiddenBtnLinkId(buttonDetail
															.getLinkId());
												}
											} else {
												btn.setBtnLinkId(buttonDetail
														.getLinkId());
												btn.setHiddenBtnLinkId(buttonDetail
														.getLinkId());
											}

											btn.setSubCatIds(buttonDetail
													.getSvdSubCate());
											btn.setChkSubCate(buttonDetail
													.getFindSubCatIds());
											btn.setHiddenSubCate(buttonDetail
													.getFindSubCatIds());
										}
										btn.setBtnGroup(grpName);
										btn.setBtnDept(buttonDetail
												.getBtnDept());
										btn.setBtnType(buttonDetail
												.getBtnType());
										btn.setImagePath(buttonDetail
												.getBtnImage());
										btn.setLogoImageName(buttonDetail
												.getImageName());
										grpBtnsList.add(btn);

										if (j == buttonDetails.size() - 1) {
											groupTemplate
													.setGrpBtnsList(grpBtnsList);
											groupedBtnList.add(groupTemplate);
											break;
										}
									} else {
										groupTemplate
												.setGrpBtnsList(grpBtnsList);
										groupedBtnList.add(groupTemplate);
										i = j - 1;
										break;
									}

								}

							}

						}

						session.setAttribute("groupedBtnList", groupedBtnList);
						session.setAttribute("groupList", groupList);
						returnView = "setupgroupmenu";

					} else if (subMenuDetails.getMenuTypeName().equals(
							ApplicationConstants.COMBOTEMPLATE)) {
						session.setAttribute("setupcombomenuImgSquare",
								ApplicationConstants.DEFAULTIMAGESQR);
						session.setAttribute("setupcombomenuImgCircle",
								ApplicationConstants.DEFAULTIMAGE);
						session.setAttribute("minCropHt", 60);
						session.setAttribute("minCropWd", 60);

						List<GroupTemplate> groupedBtnList = new ArrayList<GroupTemplate>();
						List<ScreenSettings> grpBtnsList = null;
						ButtonDetails buttonDetail = null;
						GroupTemplate groupTemplate = null;
						List<String> groupList = new ArrayList<String>();
						for (int i = 0; i < buttonDetails.size(); i++) {
							buttonDetail = buttonDetails.get(i);

							if (null != buttonDetail.getFunctnName()
									&& (ApplicationConstants.TEXT
											.equals(buttonDetail
													.getFunctnName()) || ApplicationConstants.LABEL
											.equals(buttonDetail
													.getFunctnName()))) {
								groupTemplate = new GroupTemplate();
								grpBtnsList = new ArrayList<ScreenSettings>();
								grpName = buttonDetail.getBtnName();
								groupTemplate.setGrpName(grpName);
								groupList.add(buttonDetail.getBtnName());
								for (int j = i + 1; j < buttonDetails.size(); j++) {
									buttonDetail = buttonDetails.get(j);
									if (null == buttonDetail.getFunctnName()) {

										ScreenSettings btn = new ScreenSettings();

										btn.setLogoImageName(buttonDetail
												.getImageName());
										btn.setImagePath(buttonDetail
												.getBtnImage());
										btn.setMenuBtnName(buttonDetail
												.getBtnName());
										btn.setMenuFucntionality(buttonDetail
												.getBtnAction());
										btn.setHiddenmenuFnctn(buttonDetail
												.getBtnAction());
										btn.setComboBtnType(buttonDetail
												.getComboBtnType());
										btn.setComboBtnTypeId(buttonDetail
												.getComboBtnTypeId());
										btn.setMenuIconId(ApplicationConstants.BUTTONID
												+ buttonDetail.getBtnName()
														.trim());
										btn.setCitiId(buttonDetail.getCitiId());
										if (null != buttonDetail.getLinkId()) {
											if (!Utility
													.isEmptyOrNullString(buttonDetail
															.getFunType())) {
												if (buttonDetail.getFunType()
														.equals("Find")) {
													findBtnCatId = buttonDetail
															.getLinkId();
													if (!Utility
															.isEmptyOrNullString(findBtnCatId)) {
														if (findBtnCatId
																.contains(",")) {
															findCategories = new StringBuilder();
															String[] strings = findBtnCatId
																	.split(",");
															for (int cat = 0; cat < strings.length; cat++) {
																findCategories
																		.append(strings[cat]
																				+ "-MC");
																findCategories
																		.append(",");
															}
															findBtnCatId = findCategories
																	.toString();
														} else {
															findBtnCatId = buttonDetail
																	.getLinkId()
																	+ "-MC";
														}

													}
													btn.setBtnLinkId(findBtnCatId);
													btn.setHiddenBtnLinkId(findBtnCatId);
												} else {
													btn.setBtnLinkId(buttonDetail
															.getLinkId());
													btn.setHiddenBtnLinkId(buttonDetail
															.getLinkId());
												}
											} else {
												btn.setBtnLinkId(buttonDetail
														.getLinkId());
												btn.setHiddenBtnLinkId(buttonDetail
														.getLinkId());
											}

											btn.setSubCatIds(buttonDetail
													.getSvdSubCate());
											btn.setChkSubCate(buttonDetail
													.getFindSubCatIds());
											btn.setHiddenSubCate(buttonDetail
													.getFindSubCatIds());
										}
										btn.setBtnGroup(grpName);
										btn.setBtnDept(buttonDetail
												.getBtnDept());
										btn.setBtnType(buttonDetail
												.getBtnType());
										grpBtnsList.add(btn);

										if (j == buttonDetails.size() - 1) {
											groupTemplate
													.setGrpBtnsList(grpBtnsList);
											groupedBtnList.add(groupTemplate);
											break;
										}
									} else {
										groupTemplate
												.setGrpBtnsList(grpBtnsList);
										groupedBtnList.add(groupTemplate);
										i = j - 1;
										break;
									}

								}

							}

						}

						session.setAttribute("comboBtnList", groupedBtnList);
						session.setAttribute("comboList", groupList);
						returnView = "setupcombomenu";
					}

					menuBarButtonsList = hubCitiService.getTabBarButtons(
							subMenuDetails, user);
					session.setAttribute("menuBarButtonsList",
							menuBarButtonsList);

					if (subMenuDetails.isDepartmentFlag()) {
						menuFilterTypeNames.add("Department");

					}
					if (subMenuDetails.isTypeFlag()) {

						menuFilterTypeNames.add("Type");

					}

				}

				// subMenuList = (ArrayList<MenuDetails>)
				// session.getAttribute("subMenuList");
				if (null != subMenuList) {

					for (int i = 0; i < subMenuList.size(); i++) {
						MenuDetails item = subMenuList.get(i);

						if (item.getMenuId().intValue() == Integer.valueOf(
								subMenuId).intValue()) {

							subMenuList.remove(i);
							session.setAttribute("subMenuList", subMenuList);
							break;
						}

					}
				}

			}
			subMenuUIDetails = hubCitiService.fetchGeneralSettings(
					user.getHubCitiID(), "SubMenu");
			if (null == subMenuUIDetails) {
				session.setAttribute("subMenuBG", "#d1d1d1");
				session.setAttribute("subMenuBtnClr", "#2B88D9");
				session.setAttribute("subMenuFntClr", "#FFFFFF");
				session.setAttribute("subMenuGrpClr", "#1A4A6E");
				session.setAttribute("subMenuGrpFntClr", "#FFFFFF");
				session.setAttribute("subMenuIconsFntClr", "#FFFFFF");
			} else {

				if (null != subMenuUIDetails.getBgColor()
						&& !"".equalsIgnoreCase(subMenuUIDetails.getBgColor())) {
					session.setAttribute("subMenuBGType", "Color");
					session.setAttribute("subMenuBG",
							subMenuUIDetails.getBgColor());
				}

				if (null != subMenuUIDetails.getLogoImageName()
						&& !"".equalsIgnoreCase(subMenuUIDetails
								.getLogoImageName())) {
					session.setAttribute("subMenuBGType", "Image");
					session.setAttribute("subMenuBG",
							subMenuUIDetails.getLogoPath());
				}
				session.setAttribute("subMenuBtnClr",
						subMenuUIDetails.getBtnColor());
				session.setAttribute("subMenuFntClr",
						subMenuUIDetails.getBtnFontColor());
				session.setAttribute("subMenuGrpClr",
						subMenuUIDetails.getGrpColor());
				session.setAttribute("subMenuGrpFntClr",
						subMenuUIDetails.getGrpFontColor());
				session.setAttribute("subMenuIconsFntClr",
						subMenuUIDetails.getIconsFontColor());

			}

		} catch (HubCitiServiceException e) {
			throw new HubCitiServiceException(e);
		}

		if (null != screenSettings) {
			screenSettings.setIsmenuFilterTypeSelected(true);
			screenSettings.setMenuFilterType(menuFilterTypeNames
					.toArray(new String[menuFilterTypeNames.size()]));

			session.setAttribute("menuFilterType", menuFilterTypeNames
					.toArray(new String[menuFilterTypeNames.size()]));
		}
		model.put("screenSettingsForm", screenSettings);
		LOG.info(ApplicationConstants.METHODEND + methodName);
		LOG.info("Return View" + returnView);
		return new ModelAndView(returnView);
	}

	@RequestMapping(value = "/twocoltabview.htm", method = RequestMethod.GET)
	public String createTwoColTabView(
			@ModelAttribute("menuDetails") MenuDetails menuDetails,
			BindingResult result, HttpServletRequest request, ModelMap model,
			HttpSession session) throws HubCitiServiceException {
		final String strMethodName = "displayTwoColTabView";
		final String strViewName = "twocoltabview";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		String menuId = null;
		String subMenuName = null;
		MenuDetails objMenuDetails = null;
		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");
		session.removeAttribute("menuBarButtonsList");
		session.removeAttribute("bannerimage");
		session.removeAttribute("colBtnView");
		session.setAttribute("bannerimage",
				ApplicationConstants.DEFAULTBANNERPREVIEWIMAGE);
		List<ScreenSettings> tabBarButtonsList = null;
		try {
			String strTempName = request.getParameter("menuTypeName");

			session.setAttribute("minCropHt", 107);
			session.setAttribute("minCropWd", 768);
			session.setAttribute("menuIconPreview",
					ApplicationConstants.DEFAULTIMAGESQR);
			if (null != request.getParameter("hidMenuType")) {
				if (request.getParameter("hidMenuType").equals(
						ApplicationConstants.SUBMENU)) {
					session.setAttribute(ApplicationConstants.MENUNAME,
							ApplicationConstants.SETUPSUBMENU);
				} else {
					session.setAttribute(ApplicationConstants.MENUNAME,
							ApplicationConstants.MAINMENUSCREEN);
				}
			}

			// session.setAttribute(ApplicationConstants.MENUNAME,
			// ApplicationConstants.MAINMENUSCREEN);

			User user = (User) session.getAttribute("loginUser");

			objMenuDetails = new MenuDetails();
			objMenuDetails.setMenuTypeName(strTempName);
			objMenuDetails.setLevel(menuDetails.getLevel());

			if (null == menuDetails.getLevel()) {
				objMenuDetails.setMenuName(null);

			} else {
				objMenuDetails.setMenuName(ApplicationConstants.MAINMENU);

			}

			if (null != menuDetails.getMenuId()
					&& !"".equals(menuDetails.getMenuId())) {
				menuId = menuDetails.getMenuId().toString();
				subMenuName = menuDetails.getMenuName();
			} else {
				// Create Menu
				menuId = hubCitiService.createMenu(objMenuDetails, user);

			}

			session.setAttribute("MenuId", menuId);

			// Calling method to display list of appsites.

			ScreenSettings screenSettings = new ScreenSettings();
			screenSettings.setMenuId(menuId);
			screenSettings.setMenuLevel(menuDetails.getLevel());
			screenSettings.setSubMenuName(subMenuName);
			// session.setAttribute("TwoColTabMenu",
			// ApplicationConstants.TWOCOLTABWITHBANNERVIEWTEMPLATE);
			model.put("screenSettingsForm", screenSettings);

			session.setAttribute("twocoltemp", strTempName);

		} catch (HubCitiServiceException exception) {
			throw new HubCitiServiceException(exception);
		}
		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strViewName;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/twocoltabview.htm", method = RequestMethod.POST)
	public String createTwoColTabView(
			@ModelAttribute("screenSettingsForm") ScreenSettings screenSettings,
			BindingResult result, HttpServletRequest request, ModelMap model,
			HttpSession session) throws HubCitiServiceException

	{
		String methodName = "createTwoColTabView";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String strViewName = "twocoltabview";
		ScreenSettings objTwoTabViewTemplate = null;
		// session.setAttribute("bannerimage",
		// ApplicationConstants.DEFAULTBANNERPREVIEWIMAGE);
		// fetching the values from screen settings.
		String buttonName = screenSettings.getMenuBtnName();
		String menuId = screenSettings.getMenuId();
		Integer menuLevel = screenSettings.getMenuLevel();
		String btnId = screenSettings.getMenuIconId();
		String addorDeleteMenuItem = screenSettings.getAddDeleteBtn();
		String menuFunctionlity = screenSettings.getMenuFucntionality();
		screenSettings.setHiddenmenuFnctn(menuFunctionlity);
		screenSettings.setHiddenBtnLinkId(screenSettings.getBtnLinkId());
		if ("RegionApp".equalsIgnoreCase(screenSettings.getUserType())) {
			screenSettings.setHiddenCitiId(screenSettings.getCitiId());
		}
		// fetching functional link ids
		String strBannerName = screenSettings.getLogoImageName();
		Integer subMenuFctnId = (Integer) session.getAttribute("subMenuFctnId");
		Integer anythingPageFctnId = (Integer) session
				.getAttribute("anythingPageFctnId");
		Integer appSiteFctnId = (Integer) session.getAttribute("appSiteFctnId");
		Integer findFctnId = (Integer) session.getAttribute("findFctnId");
		Integer eventFctnId = (Integer) session.getAttribute("eventFctnId");
		Integer fundraFctnId = (Integer) session.getAttribute("fundraFctnId");
		String subMenuName = screenSettings.getSubMenuName();
		String strBtnView = screenSettings.getBtnView();
		session.setAttribute("colBtnView", strBtnView);

		// for subcategories
		screenSettings.setHiddenSubCate(screenSettings.getChkSubCate());
		screenSettings.setChkSubCate(screenSettings.getChkSubCate());
		if (null != screenSettings.getMenuFucntionality()
				&& screenSettings.getMenuFucntionality().equals(
						String.valueOf(findFctnId))
				&& null != screenSettings.getBtnLinkId()) {
			String[] findCat = screenSettings.getBtnLinkId().split(",");
			String findCatList = null;

			for (String cat : findCat) {
				if (cat.contains("MC")) {
					if (null != findCatList) {
						findCatList += cat + ",";
					} else {
						findCatList = new String();
						findCatList = cat + ",";
					}
				}
			}
			if (findCatList.endsWith(",")) {
				findCatList = findCatList
						.substring(0, findCatList.length() - 1);
			}
			screenSettings.setBtnLinkId(findCatList);
		}

		List<ScreenSettings> twocolmenuitems = (ArrayList<ScreenSettings>) session
				.getAttribute("previewMenuItems");

		// Below Code is for Re-Ordering menu buttons
		String btnOrderArry[] = null;
		String btnOrder = screenSettings.getBtnPosition();
		List<ScreenSettings> sortedMenuItems = new ArrayList<ScreenSettings>();
		if (!"".equals(Utility.checkNull(btnOrder))) {
			btnOrderArry = btnOrder.split("~");

		}

		// Code for SubMenu Filters Implementation
		String[] menuFilterType = screenSettings.getMenuFilterType();

		if (null != menuFilterType) {
			if (menuFilterType.length != 0 || screenSettings.isEditFilter()) {

				session.setAttribute("menuFilterType", menuFilterType);
				screenSettings.setEditFilter(false);
			} else {

				menuFilterType = (String[]) session
						.getAttribute("menuFilterType");
				screenSettings.setMenuFilterType(menuFilterType);

			}

		}

		List<Type> filterTypeList = (ArrayList<Type>) session
				.getAttribute("filterTypeList");
		List<Department> filterDeptList = (ArrayList<Department>) session
				.getAttribute("filterDeptList");

		if (null != filterTypeList) {
			Type newType = null;
			if (!"0".equals(screenSettings.getBtnType())) {
				if (filterTypeList.size() != 0) {

					for (int i = 0; i < filterTypeList.size(); i++) {
						Type type = filterTypeList.get(i);

						if (type.getTypeName().equals(
								screenSettings.getBtnType())) {

							break;
						}

						if (i == filterTypeList.size() - 1) {
							newType = new Type();
							newType.setTypeName(screenSettings.getBtnType());
							filterTypeList.add(newType);
							session.setAttribute("filterTypeList",
									filterTypeList);
						}
					}
				} else {
					newType = new Type();
					newType.setTypeName(screenSettings.getBtnType());
					filterTypeList.add(newType);
					session.setAttribute("filterTypeList", filterTypeList);

				}

			}

		}

		if (null != filterDeptList) {
			Department newDept = null;
			if (!"0".equals(screenSettings.getBtnDept())) {
				if (filterDeptList.size() != 0) {

					for (int i = 0; i < filterDeptList.size(); i++) {
						Department dept = filterDeptList.get(i);

						if (dept.getDeptName().equals(
								screenSettings.getBtnDept())) {

							break;
						}

						if (i == filterDeptList.size() - 1) {
							newDept = new Department();
							newDept.setDeptName(screenSettings.getBtnDept());
							filterDeptList.add(newDept);
							session.setAttribute("filterDeptList",
									filterDeptList);
						}
					}
				} else {
					newDept = new Department();
					newDept.setDeptName(screenSettings.getBtnDept());
					filterDeptList.add(newDept);
					session.setAttribute("filterDeptList", filterDeptList);

				}

			}

		}

		if (null != buttonName) {
			buttonName = buttonName.trim();
		}

		// code for adding button.
		if (null != addorDeleteMenuItem
				&& "Addbutton".equals(addorDeleteMenuItem))

		{

			if (null != screenSettings.getMenuFucntionality()
					&& !"".equals(screenSettings.getMenuFucntionality())) {
				if (screenSettings.getMenuFucntionality().equals(
						String.valueOf(subMenuFctnId))) {
					screenSettings
							.setFunctionalityType(ApplicationConstants.SUBMENU);
				} else if (screenSettings.getMenuFucntionality().equals(
						String.valueOf(appSiteFctnId))) {
					screenSettings
							.setFunctionalityType(ApplicationConstants.APPSITE);
				} else if (screenSettings.getMenuFucntionality().equals(
						String.valueOf(anythingPageFctnId))) {
					screenSettings
							.setFunctionalityType(ApplicationConstants.ANYTHINGPAGE);
				} else if (screenSettings.getMenuFucntionality().equals(
						String.valueOf(findFctnId))) {
					screenSettings
							.setFunctionalityType(ApplicationConstants.FIND);
					screenSettings.setHiddenFindCategory(screenSettings
							.getBtnLinkId());
					screenSettings.setHiddenSubCate(screenSettings
							.getChkSubCate());
					screenSettings
							.setChkSubCate(screenSettings.getChkSubCate());
				} else if (screenSettings.getMenuFucntionality().equals(
						String.valueOf(eventFctnId))) {
					screenSettings
							.setFunctionalityType(ApplicationConstants.SETUPEVENTS);
				} else if (screenSettings.getMenuFucntionality().equals(
						String.valueOf(fundraFctnId))) {

					screenSettings
							.setFunctionalityType(ApplicationConstants.FUNDRAISERS);
					screenSettings.setHiddenFundEvtId(screenSettings.getBtnLinkId());
					

				} else {

					screenSettings.setBtnLinkId(null);
					screenSettings.setHiddenBtnLinkId(null);
					screenSettings.setHiddenSubCate(null);
					screenSettings.setChkSubCate(null);
				}

			}

			if (ApplicationConstants.TWOCOLTABWITHBANNERVIEWTEMPLATE
					.equals(session.getAttribute("twocoltemp")))

			{

				twoColTabValidator.validate(screenSettings, result, 2);

			} else {

				twoColTabValidator.validate(screenSettings, result, 1);
			}
			if (result.hasErrors()) {
				screenSettings.setBtnLinkId(null);
				screenSettings.setChkSubCate(null);
				return strViewName;
			} else {

				// if menu list is empty ,create list ,create settings add to
				// list and set list into session
				if (null == twocolmenuitems || twocolmenuitems.isEmpty()) {
					twocolmenuitems = new ArrayList<ScreenSettings>();
					objTwoTabViewTemplate = new ScreenSettings();

					objTwoTabViewTemplate.setMenuId(screenSettings.getMenuId());
					objTwoTabViewTemplate.setMenuLevel(screenSettings
							.getMenuLevel());
					objTwoTabViewTemplate.setMenuBtnName(screenSettings
							.getMenuBtnName().trim());
					objTwoTabViewTemplate
							.setMenuIconId(ApplicationConstants.BUTTONID
									+ screenSettings.getMenuBtnName().trim());
					objTwoTabViewTemplate.setMenuFucntionality(screenSettings
							.getMenuFucntionality());
					// objTwoTabViewTemplate.setLogoImageName(screenSettings.getLogoImageName());
					// objTwoTabViewTemplate.setImagePath(ApplicationConstants.TEMPIMGPATH
					// + screenSettings.getLogoImageName());
					objTwoTabViewTemplate.setBtnLinkId(screenSettings
							.getBtnLinkId());
					objTwoTabViewTemplate.setSubCatIds(screenSettings
							.getSubCatIds());
					objTwoTabViewTemplate.setChkSubCate(screenSettings
							.getChkSubCate());
					objTwoTabViewTemplate.setCitiId(screenSettings.getCitiId());

					if (!"0".equals(screenSettings.getBtnDept())) {
						objTwoTabViewTemplate.setBtnDept(screenSettings
								.getBtnDept());

					} else {

						objTwoTabViewTemplate.setBtnDept(null);
					}

					if (!"0".equals(screenSettings.getBtnType())) {
						objTwoTabViewTemplate.setBtnType(screenSettings
								.getBtnType());

					} else {

						objTwoTabViewTemplate.setBtnType(null);

					}

					twocolmenuitems.add(objTwoTabViewTemplate);
					session.setAttribute("previewMenuItems", twocolmenuitems);
				}
				// if menu list is not empty....
				else {
					if (null != btnId && !"".equals(btnId)) {
						for (int i = 0; i < twocolmenuitems.size(); i++) {
							ScreenSettings menuItem = twocolmenuitems.get(i);
							if (menuItem.getMenuIconId().equals(btnId)) {

								for (int j = 0; j < twocolmenuitems.size(); j++) {
									ScreenSettings btn = twocolmenuitems.get(j);
									if (j != i) {
										if (btn.getMenuBtnName().equals(
												buttonName)) {

											twoColTabValidator
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATEBUTTONNAME);
											break;
										}

										if (btn.getMenuFucntionality().equals(
												String.valueOf(subMenuFctnId))
												&& String
														.valueOf(subMenuFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (btn.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {
													twoColTabValidator
															.validate(
																	screenSettings,
																	result,
																	ApplicationConstants.DUPLICATESUBMENU);
													break;
												}
											}

										}
										if (btn.getMenuFucntionality()
												.equals(String
														.valueOf(anythingPageFctnId))
												&& String
														.valueOf(
																anythingPageFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (btn.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {

													twoColTabValidator
															.validate(
																	screenSettings,
																	result,
																	ApplicationConstants.DUPLICATEANYTHINGPAGE);
													break;
												}

											}

										}
										if (btn.getMenuFucntionality().equals(
												String.valueOf(appSiteFctnId))
												&& String
														.valueOf(appSiteFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (btn.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {

													twoColTabValidator
															.validate(
																	screenSettings,
																	result,
																	ApplicationConstants.DUPLICATEAPPSITE);
													break;
												}

											}

										}
										if (btn.getMenuFucntionality().equals(
												String.valueOf(findFctnId))
												&& String
														.valueOf(findFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (btn.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {
													if (btn.getChkSubCate()
															.equals(screenSettings
																	.getChkSubCate())) {
														if (btn.getBtnLinkId()
																.contains(",")) {
															iconinMenuValidator
																	.validate(
																			screenSettings,
																			result,
																			ApplicationConstants.DUPLICATECATEGORIES);
														} else {
															iconinMenuValidator
																	.validate(
																			screenSettings,
																			result,
																			ApplicationConstants.DUPLICATECATEGORY);
														}

														break;
													}
												}
											}

										}

										if (btn.getMenuFucntionality().equals(
												String.valueOf(eventFctnId))
												&& String
														.valueOf(eventFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (btn.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {
													if (btn.getBtnLinkId()
															.contains(",")) {
														iconinMenuValidator
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATECATEGORIES);
													} else {
														iconinMenuValidator
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATECATEGORY);
													}

													break;
												}
											}

										}
										//Adding code for fundriaser implementation.
										if (btn.getMenuFucntionality().equals(
												String.valueOf(fundraFctnId))
												&& String
														.valueOf(fundraFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (btn.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {
													if (btn.getBtnLinkId()
															.contains(",")) {
														iconinMenuValidator
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATECATEGORIES);
													} else {
														iconinMenuValidator
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATECATEGORY);
													}

													break;
												}
											}

										}

										
										
										

										if (btn.getMenuFucntionality().equals(screenSettings.getMenuFucntionality())
												&& !btn.getMenuFucntionality().equals(String.valueOf(subMenuFctnId))
												&& !btn.getMenuFucntionality()
														.equals(String
																.valueOf(anythingPageFctnId))
												&& !btn.getMenuFucntionality()
														.equals(String
																.valueOf(appSiteFctnId))
												&& !btn.getMenuFucntionality()
														.equals(String
																.valueOf(findFctnId))
												&& !btn.getMenuFucntionality()
														.equals(String
																.valueOf(eventFctnId))
												&&!btn.getMenuFucntionality().equals(String.valueOf(fundraFctnId))) {

											twoColTabValidator
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATEFUNCTINALITY);
											break;
										}

									} // End of i and j not equal
								}// end of j loop

								if (result.hasErrors()) {
									screenSettings.setBtnLinkId(null);
									return strViewName;
								}

								// menuItem.setLogoImageName(screenSettings.getLogoImageName());
								// menuItem.setImagePath(ApplicationConstants.TEMPIMGPATH
								// + screenSettings.getLogoImageName());
								menuItem.setMenuBtnName(screenSettings
										.getMenuBtnName().trim());
								menuItem.setMenuFucntionality(screenSettings
										.getMenuFucntionality());
								menuItem.setHiddenmenuFnctn(screenSettings
										.getMenuFucntionality());
								menuItem.setMenuIconId(ApplicationConstants.BUTTONID
										+ screenSettings.getMenuBtnName()
												.trim());
								menuItem.setBtnLinkId(screenSettings
										.getBtnLinkId());
								menuItem.setSubCatIds(screenSettings
										.getSubCatIds());
								menuItem.setChkSubCate(screenSettings
										.getChkSubCate());
								menuItem.setCitiId(screenSettings.getCitiId());

								if (!"0".equals(screenSettings.getBtnDept())) {
									menuItem.setBtnDept(screenSettings
											.getBtnDept());

								} else {

									menuItem.setBtnDept(null);
								}

								if (!"0".equals(screenSettings.getBtnType())) {
									menuItem.setBtnType(screenSettings
											.getBtnType());

								} else {

									menuItem.setBtnType(null);

								}

								twocolmenuitems.remove(i);

								twocolmenuitems.add(i, menuItem);
								session.setAttribute("previewMenuItems",
										twocolmenuitems);

								// Below Code is for Re-Ordering menu buttons
								if (null != btnOrderArry) {
									for (int p = 0; p < btnOrderArry.length; p++) {

										if (btnOrderArry[p].equals(btnId)) {
											btnOrderArry[p] = ApplicationConstants.BUTTONID
													+ screenSettings
															.getMenuBtnName()
															.trim();
											break;

										}

									}
								}
							}

						}

					}
					// if menu icon id is empty or null.

					else {
						// If menu items list is already created, Validate for
						// duplicate button and add unique menu items to list
						for (ScreenSettings menuItem : twocolmenuitems) {

							if (menuItem.getMenuBtnName().equals(buttonName)) {

								twoColTabValidator
										.validate(
												screenSettings,
												result,
												ApplicationConstants.DUPLICATEBUTTONNAME);

							}

							if (menuItem.getMenuFucntionality().equals(
									String.valueOf(subMenuFctnId))
									&& String.valueOf(subMenuFctnId).equals(
											menuFunctionlity)) {
								if (null != screenSettings.getBtnLinkId()
										&& !"".equals(screenSettings
												.getBtnLinkId())) {
									if (menuItem.getBtnLinkId().equals(
											screenSettings.getBtnLinkId())) {

										twoColTabValidator
												.validate(
														screenSettings,
														result,
														ApplicationConstants.DUPLICATESUBMENU);
										break;
									}
								}

							}
							if (menuItem.getMenuFucntionality().equals(
									String.valueOf(anythingPageFctnId))
									&& String.valueOf(anythingPageFctnId)
											.equals(menuFunctionlity)) {
								if (null != screenSettings.getBtnLinkId()
										&& !"".equals(screenSettings
												.getBtnLinkId())) {
									if (menuItem.getBtnLinkId().equals(
											screenSettings.getBtnLinkId())) {

										twoColTabValidator
												.validate(
														screenSettings,
														result,
														ApplicationConstants.DUPLICATEANYTHINGPAGE);
										break;
									}

								}

							}
							if (menuItem.getMenuFucntionality().equals(
									String.valueOf(appSiteFctnId))
									&& String.valueOf(appSiteFctnId).equals(
											menuFunctionlity)) {
								if (null != screenSettings.getBtnLinkId()
										&& !"".equals(screenSettings
												.getBtnLinkId())) {
									if (menuItem.getBtnLinkId().equals(
											screenSettings.getBtnLinkId())) {

										twoColTabValidator
												.validate(
														screenSettings,
														result,
														ApplicationConstants.DUPLICATEAPPSITE);
										break;
									}

								}

							}
							if (menuItem.getMenuFucntionality().equals(
									String.valueOf(findFctnId))
									&& String.valueOf(findFctnId).equals(
											menuFunctionlity)) {
								if (null != screenSettings.getBtnLinkId()
										&& !"".equals(screenSettings
												.getBtnLinkId())) {
									if (menuItem.getBtnLinkId().equals(
											screenSettings.getBtnLinkId())) {
										if (menuItem.getChkSubCate().equals(
												screenSettings.getChkSubCate())) {
											if (menuItem.getBtnLinkId()
													.contains(",")) {
												iconinMenuValidator
														.validate(
																screenSettings,
																result,
																ApplicationConstants.DUPLICATECATEGORIES);
											} else {
												iconinMenuValidator
														.validate(
																screenSettings,
																result,
																ApplicationConstants.DUPLICATECATEGORY);
											}

											break;
										}
									}
								}

							}

							if (menuItem.getMenuFucntionality().equals(
									String.valueOf(eventFctnId))
									&& String.valueOf(eventFctnId).equals(
											menuFunctionlity)) {
								if (null != screenSettings.getBtnLinkId()
										&& !"".equals(screenSettings
												.getBtnLinkId())) {
									if (menuItem.getBtnLinkId().equals(
											screenSettings.getBtnLinkId())) {
										if (menuItem.getBtnLinkId().contains(
												",")) {
											iconinMenuValidator
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATECATEGORIES);
										} else {
											iconinMenuValidator
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATECATEGORY);
										}

										break;
									}
								}

							}
							
							if (menuItem.getMenuFucntionality().equals(
									String.valueOf(fundraFctnId))
									&& String.valueOf(fundraFctnId).equals(
											menuFunctionlity)) {
								if (null != screenSettings.getBtnLinkId()
										&& !"".equals(screenSettings
												.getBtnLinkId())) {
									if (menuItem.getBtnLinkId().equals(
											screenSettings.getBtnLinkId())) {
										if (menuItem.getBtnLinkId().contains(
												",")) {
											iconinMenuValidator
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATECATEGORIES);
										} else {
											iconinMenuValidator
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATECATEGORY);
										}

										break;
									}
								}

							}

							if (menuItem.getMenuFucntionality().equals(
									screenSettings.getMenuFucntionality())
									&& !menuItem.getMenuFucntionality().equals(
											String.valueOf(subMenuFctnId))
									&& !menuItem.getMenuFucntionality().equals(
											String.valueOf(anythingPageFctnId))
									&& !menuItem.getMenuFucntionality().equals(
											String.valueOf(appSiteFctnId))
									&& !menuItem.getMenuFucntionality().equals(
											String.valueOf(findFctnId))
									&& !menuItem.getMenuFucntionality().equals(
											String.valueOf(eventFctnId))
									&& !menuItem.getMenuFucntionality().equals(
													String.valueOf(fundraFctnId))
											
											
									) {

								twoColTabValidator
										.validate(
												screenSettings,
												result,
												ApplicationConstants.DUPLICATEFUNCTINALITY);
								break;
							}

						}

						if (result.hasErrors()) {
							screenSettings.setBtnLinkId(null);
							return strViewName;
						}

						objTwoTabViewTemplate = new ScreenSettings();

						objTwoTabViewTemplate.setMenuId(screenSettings
								.getMenuId());
						objTwoTabViewTemplate.setMenuLevel(screenSettings
								.getMenuLevel());
						objTwoTabViewTemplate.setMenuBtnName(screenSettings
								.getMenuBtnName().trim());
						objTwoTabViewTemplate
								.setMenuIconId(ApplicationConstants.BUTTONID
										+ screenSettings.getMenuBtnName()
												.trim());
						objTwoTabViewTemplate
								.setMenuFucntionality(screenSettings
										.getMenuFucntionality());
						// objTwoTabViewTemplate.setLogoImageName(screenSettings.getLogoImageName());
						// objTwoTabViewTemplate.setImagePath(ApplicationConstants.TEMPIMGPATH
						// + screenSettings.getLogoImageName());
						// strBannerImg = ApplicationConstants.TEMPIMGPATH +
						// screenSettings.getLogoImageName();
						strBannerName = screenSettings.getLogoImageName();
						objTwoTabViewTemplate.setBtnLinkId(screenSettings
								.getBtnLinkId());
						objTwoTabViewTemplate.setSubCatIds(screenSettings
								.getSubCatIds());
						objTwoTabViewTemplate.setChkSubCate(screenSettings
								.getChkSubCate());
						objTwoTabViewTemplate.setCitiId(screenSettings
								.getCitiId());

						if (!"0".equals(screenSettings.getBtnDept())) {
							objTwoTabViewTemplate.setBtnDept(screenSettings
									.getBtnDept());

						} else {

							objTwoTabViewTemplate.setBtnDept(null);
						}

						if (!"0".equals(screenSettings.getBtnType())) {
							objTwoTabViewTemplate.setBtnType(screenSettings
									.getBtnType());

						} else {

							objTwoTabViewTemplate.setBtnType(null);
						}

						// objTwoTabViewTemplate.setBtnView(strBtnView);
						twocolmenuitems.add(objTwoTabViewTemplate);
						session.setAttribute("previewMenuItems",
								twocolmenuitems);

					}// end of :if menu icon id is empty or null.

				}

			}

		}
		// End of addbutton code ....
		// Code for deleting menu item
		else if (null != addorDeleteMenuItem
				&& "DeleteButton".equals(addorDeleteMenuItem)) {

			for (int i = 0; i < twocolmenuitems.size(); i++) {

				ScreenSettings menuItem = twocolmenuitems.get(i);
				if (menuItem.getMenuBtnName().equals(buttonName)) {
					twocolmenuitems.remove(i);
					session.setAttribute("previewMenuItems", twocolmenuitems);
					break;
				}
			}

		}

		// Below Code is for Re-Ordering menu buttons
		if (null != btnOrderArry) {
			for (int j = 0; j < btnOrderArry.length; j++) {

				for (int i = 0; i < twocolmenuitems.size(); i++) {
					ScreenSettings btn = twocolmenuitems.get(i);

					if (btn.getMenuIconId().equals(btnOrderArry[j])) {

						sortedMenuItems.add(btn);
						twocolmenuitems.remove(i);
						break;
					}

				}
			}

			if (!twocolmenuitems.isEmpty()) {

				for (int i = 0; i < twocolmenuitems.size(); i++) {

					sortedMenuItems.add(twocolmenuitems.get(i));
				}

			}
			session.setAttribute("previewMenuItems", sortedMenuItems);
		}
		session.setAttribute("bannername", strBannerName);
		screenSettings = new ScreenSettings();
		screenSettings.setMenuId(menuId);
		screenSettings.setMenuLevel(menuLevel);
		screenSettings.setIsmenuFilterTypeSelected(true);
		screenSettings.setMenuFilterType(menuFilterType);
		screenSettings.setEditFilter(false);
		/*
		 * screenSettings.setOldImageName(strBannerName);
		 * screenSettings.setImagePath(strBannerImg);
		 */
		if (ApplicationConstants.TWOCOLTABWITHBANNERVIEWTEMPLATE.equals(session
				.getAttribute("twocoltemp"))) {
			screenSettings.setLogoImageName(strBannerName);
			if (null != session.getAttribute("bannerimage")) {
				session.setAttribute("menuIconPreview",
						session.getAttribute("bannerimage"));
			} else {

				session.setAttribute("menuIconPreview",
						ApplicationConstants.DEFAULTBANNERPREVIEWIMAGE);
			}

		}

		screenSettings.setSubMenuName(subMenuName);
		model.put("screenSettingsForm", screenSettings);

		return strViewName;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/savetwotabtemplate.htm", method = RequestMethod.POST)
	@ResponseBody
	public final String saveTwoTabTemplate(
			@RequestParam(value = "menuId", required = true) String menuId,

			// @RequestParam(value = "menuLevel", required = true) Integer
			// menuLevel,
			// @RequestParam(value = "bannerImg", required = true) String
			// bannerImg, HttpServletRequest request, HttpServletResponse
			// response,

			@RequestParam(value = "menuLevel", required = true) Integer menuLevel,
			@RequestParam(value = "bannerImg", required = true) String bannerImg,
			@RequestParam(value = "subMenuName", required = true) String subMenuName,
			@RequestParam(value = "bottmBtnId", required = true) String bottmBtnId,
			@RequestParam(value = "template", required = true) String templateType,
			@RequestParam(value = "typeFilter", required = true) boolean tyepFilter,
			@RequestParam(value = "deptFilter", required = true) boolean deptFilter,
			@RequestParam(value = "btnOrder", required = true) String btnOrder,
			@RequestParam(value = "btnView", required = true) String btnView,
			HttpServletRequest request, HttpServletResponse response,

			HttpSession session) throws HubCitiServiceException {

		String methodName = "saveTwoTabTemplate sd";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String responseStr = null;
		Integer expFctnId = (Integer) session.getAttribute("expFctnId");
		List<ScreenSettings> sortedMenuItems = new ArrayList<ScreenSettings>();
		String loginUserType = (String) session.getAttribute("loginUserType");
		String btnOrderArry[] = null;
		try {
			final ServletContext servletContext = request.getSession()
					.getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext
					.getBean("hubCitiService");
			// List of Menu items

			List<ScreenSettings> previewMenuItems = (ArrayList<ScreenSettings>) session
					.getAttribute("previewMenuItems");
			User user = (User) session.getAttribute("loginUser");

			if (deptFilter) {

				for (int i = 0; i < previewMenuItems.size(); i++) {
					ScreenSettings btn = previewMenuItems.get(i);

					if (null == btn.getBtnDept()) {

						responseStr = "Please associate Department for all the bottons to continue saving";
						return ASSOCIATEDEPT;
					}

				}

			}
			if (tyepFilter) {

				for (int i = 0; i < previewMenuItems.size(); i++) {
					ScreenSettings btn = previewMenuItems.get(i);

					if (null == btn.getBtnType()) {

						responseStr = "Please associate Type for all the bottons to continue saving";
						return ASSOCIATETYPE;
					}

				}

			}

			if ("RegionApp".equalsIgnoreCase(loginUserType)) {
				for (int i = 0; i < previewMenuItems.size(); i++) {
					ScreenSettings btn = previewMenuItems.get(i);

					if (null == btn.getCitiId()) {
						responseStr = "Please associate City for all the bottons to continue saving";
						return ASSOCIATECITY;
					}

				}
			}

			for (int i = 0; i < previewMenuItems.size(); i++) {
				ScreenSettings btn = previewMenuItems.get(i);

				if (null != btn.getMenuFucntionality()
						&& !"".equals(btn.getMenuFucntionality())) {

					if (Integer.valueOf(btn.getMenuFucntionality()) == expFctnId
							.intValue()) {
						previewMenuItems.remove(i);
						previewMenuItems.add(0, btn);

						break;
					}

				}

			}
			if (!"".equals(Utility.checkNull(btnOrder))) {

				btnOrderArry = btnOrder.split("~");
				for (int j = 0; j < btnOrderArry.length; j++) {

					for (int i = 0; i < previewMenuItems.size(); i++) {
						ScreenSettings btn = previewMenuItems.get(i);

						if (btn.getMenuIconId().equals(btnOrderArry[j])) {

							sortedMenuItems.add(btn);
							previewMenuItems.remove(i);
							break;
						}

					}
				}

				session.setAttribute("previewMenuItems", sortedMenuItems);
				responseStr = hubCitiService.saveUpdateTwoTabMenuTemplate(user,
						sortedMenuItems, Integer.valueOf(menuId), menuLevel,
						bannerImg, subMenuName, bottmBtnId, templateType,
						tyepFilter, deptFilter, btnView);

			}

		} catch (HubCitiServiceException e) {
			throw new HubCitiServiceException(e);
		}
		return responseStr;
	}

	/**
	 * This Method will return the List View Menu Screen.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/setuptabbar.htm", method = RequestMethod.GET)
	public String setTabBar(HttpServletRequest request, ModelMap model,
			HttpSession session) throws HubCitiServiceException {
		String methodName = "setTabBar";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");

		List<MenuDetails> linkList = null;
		List<ScreenSettings> existingIconsList = null;
		List<ScreenSettings> tabBarButtonsList = null;
		MenuDetails menuDetails = null;
		ScreenSettings generalScreenDetails = null;
		List<Category> businessCategoryList = null;
		List<MenuDetails> subMenuList = null;
		AnythingPages anythingPageList = null;
		ScreenSettings subMenuLstObj = new ScreenSettings();
		SubMenuDetails subMenuDetailsObj = null;
		String findBtnCatId = null;
		// iconicMenuItems = new ArrayList<ScreenSettings>();
		StringBuilder findCategories = null;
		ScreenSettings iconicMenuItem = null;
		ArrayList<ScreenSettings> tabBarButtonsLst = null;
		AlertCategory fundraiserCategoryLst = null;
		AlertCategory eventCategoryList = null;
		try {
			displayAppSites(request, session, model);
			session.removeAttribute("btmlinkList");
			// Minimum crop height and width for login screen
			session.setAttribute("minCropHt", 50);
			session.setAttribute("minCropWd", 80);

			User user = (User) session.getAttribute("loginUser");

			session.setAttribute(ApplicationConstants.MENUNAME,
					ApplicationConstants.SETUPBOTTOMTABBAR);

			// Default Image Path
			session.setAttribute("tabBarIconPreview",
					ApplicationConstants.DEFAULTIMAGESQR);
			session.setAttribute("imageFileTabBarIconPreview",
					ApplicationConstants.DEFAULTIMAGESQR);

			businessCategoryList = hubCitiService.fetchBusinessCategoryList();
			subMenuDetailsObj = hubCitiService.displaySubMenu(user,
					subMenuLstObj);
			if (null != subMenuDetailsObj) {
				subMenuList = subMenuDetailsObj.getSubMenuList();
			}
			anythingPageList = hubCitiService.displayAnythingPage(user, null,
					null);

			linkList = (ArrayList<MenuDetails>) session
					.getAttribute("btmlinkList");

			existingIconsList = (ArrayList<ScreenSettings>) session
					.getAttribute("existingIconsList");

			generalScreenDetails = hubCitiService.fetchGeneralSettings(
					user.getHubCitiID(), "TabBarLogo");

			if (null == generalScreenDetails) {

				session.setAttribute(ApplicationConstants.MENUNAME,
						ApplicationConstants.GENERALSETTINGS);
				List<ScreenSettings> buttomBtnTyp = hubCitiService
						.displayButtomBtnType();
				session.setAttribute("buttomBtnType", buttomBtnTyp);

				model.put("screenSettingsForm", new ScreenSettings());
				session.setAttribute("btnname", "Save Settings");
				session.setAttribute("titleBarLogo", "images/upload_image.png");
				session.setAttribute("titleBarLogoPreview", "small-logo.png");
				session.setAttribute("appiconpreview",
						"images/upload_image.png");
				session.setAttribute("menuType", "TabBarLogo");
				return "generalsettings";
			}

			if (null != generalScreenDetails.getBottomBtnName()) {
				session.setAttribute("bottomBtnType",
						generalScreenDetails.getBottomBtnName());
			} else {
				session.setAttribute(ApplicationConstants.MENUNAME,
						ApplicationConstants.GENERALSETTINGS);
				List<ScreenSettings> buttomBtnTyp = hubCitiService
						.displayButtomBtnType();
				session.setAttribute("buttomBtnType", buttomBtnTyp);

				model.put("screenSettingsForm", generalScreenDetails);
				session.setAttribute("btnname", "Update Settings");
				session.setAttribute("titleBarLogo",
						generalScreenDetails.getLogoPath());
				session.setAttribute("titleBarLogoPreview",
						generalScreenDetails.getLogoPath());

				if (!"".equals(Utility.checkNull(generalScreenDetails
						.getImagePath()))) {

					session.setAttribute("appiconpreview",
							generalScreenDetails.getImagePath());
				} else {

					session.setAttribute("appiconpreview",
							"images/upload_image.png");
				}

				session.setAttribute("menuType", "TabBarLogo");
				return "generalsettings";
			}

			if (null == linkList) {
				linkList = hubCitiService
						.getBottomLinkList(user.getHubCitiID());

				session.setAttribute("btmlinkList", linkList);

				for (MenuDetails link : linkList) {

					if ("Share".equals(link.getMenuTypeName())) {
						session.setAttribute("tabShareFctnId",
								link.getMenuTypeId());
						continue;
					}
					if ("SubMenu".equals(link.getMenuTypeVal())) {
						session.setAttribute("subMenuFctnId",
								link.getMenuTypeId());
						continue;
					}
					if ("AnythingPage".equals(link.getMenuTypeVal())) {
						session.setAttribute("anythingPageFctnId",
								link.getMenuTypeId());
						continue;

					}
					if ("AppSite".equals(link.getMenuTypeVal())) {
						session.setAttribute("appSiteFctnId",
								link.getMenuTypeId());
						continue;
					}
					if ("Find".equals(link.getMenuTypeVal())) {
						session.setAttribute("findFctnId", link.getMenuTypeId());
						continue;
					}
					if ("Events".equals(link.getMenuTypeVal())) {
						session.setAttribute("eventFctnId",
								link.getMenuTypeId());
						continue;
					}
					if ("Fundraisers".equals(link.getMenuTypeVal())) {
						session.setAttribute("fundraFctnId",
								link.getMenuTypeId());
						continue;
					}
				}

				for (int i = 0; i < linkList.size(); i++) {
					MenuDetails link = linkList.get(i);
					if ("Text".equals(link.getMenuTypeName())
							|| "Label".equals(link.getMenuTypeName())) {
						linkList.remove(i);
					}
				}

			}

			if (null == existingIconsList) {
				existingIconsList = hubCitiService.getBottomBarExistingIcons();
				session.setAttribute("existingIconsList", existingIconsList);

			}

			tabBarButtonsList = hubCitiService.getTabBarButtons(menuDetails,
					user);

			for (int i = 0; i < tabBarButtonsList.size(); i++) {
				ScreenSettings item = tabBarButtonsList.get(i);
				item.setMenuIconId(ApplicationConstants.BUTTONID
						+ item.getMenuFucntionality());

				tabBarButtonsList.remove(i);
				tabBarButtonsList.add(i, item);
			}
			session.setAttribute("tabBarbuttonsList", tabBarButtonsList);
			session.setAttribute("subMenuList", subMenuList);
			session.setAttribute("businessCatList", businessCategoryList);
			session.setAttribute("anythingPageList",
					anythingPageList.getPageDetails());

			// Start:Added code for Task:Event Category Changes
			eventCategoryList = hubCitiService.fetchEventCategories(null, user);
			if (null != eventCategoryList
					&& !eventCategoryList.getAlertCatLst().isEmpty()) {
				session.setAttribute("eventCatList",
						eventCategoryList.getAlertCatLst());
			} else {
				List<Category> eventCatLst = new ArrayList<Category>();
				session.setAttribute("eventCatList", eventCatLst);
			}

			// End: Added code for Task:Event Category Changes

			// Start :Adding code for displaying fundraiser categories
			fundraiserCategoryLst = hubCitiService
					.fetchFundraiserEventCategories(user);

			if (null != fundraiserCategoryLst
					&& !fundraiserCategoryLst.getAlertCatLst().isEmpty()) {
				session.setAttribute("fundraiserCatList",
						fundraiserCategoryLst.getAlertCatLst());

			} else {

				session.setAttribute("fundraiserCatList", null);

			}

			// End : Adding code for displaying fundraiser categories

			ScreenSettings screenSettings = new ScreenSettings();

			model.put("screenSettingsForm", screenSettings);

		} catch (HubCitiServiceException exception) {

			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);

		return "tabBarSetup";
	}

	/**
	 * Controller Method for Creating iconic menu template.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addtabbarbutn.htm", method = RequestMethod.POST)
	public ModelAndView addtabbarbutn(
			@ModelAttribute("screenSettingsForm") ScreenSettings screenSettings,
			BindingResult result, HttpServletRequest request, ModelMap model,
			HttpSession session) throws HubCitiServiceException {
		final String methodName = "addtabbarbutn";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		ScreenSettings tabBarBtn = null;
		Integer bottomBtnId = screenSettings.getBottomBtnId();
		boolean isValidiTunesURL = true;
		boolean isValidGoogleURL = true;
		final String btnId = screenSettings.getMenuIconId();
		String viewName = screenSettings.getViewName();
		final String addorDeleteMenuItem = screenSettings.getAddDeleteBtn();

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		// List of buttom bar navigarions
		List<ScreenSettings> tabBarbuttonsList = (ArrayList<ScreenSettings>) session.getAttribute("tabBarbuttonsList");
		User user = (User) session.getAttribute("loginUser");
		screenSettings.setHiddenmenuFnctn(screenSettings.getMenuFucntionality());
		String status = null;
		
		final Integer subMenuFctnId = (Integer) session.getAttribute("subMenuFctnId");
		final Integer anythingPageFctnId = (Integer) session.getAttribute("anythingPageFctnId");
		final Integer appSiteFctnId = (Integer) session.getAttribute("appSiteFctnId");
		final Integer findFctnId = (Integer) session.getAttribute("findFctnId");
		final Integer eventFctnId = (Integer) session.getAttribute("eventFctnId");
		final Integer fundraFctnId = (Integer) session.getAttribute("fundraFctnId");
		final String menuFunctionlity = screenSettings.getMenuFucntionality();
		screenSettings.setHiddenBtnLinkId(screenSettings.getBtnLinkId());
		screenSettings.setHiddenSubCate(screenSettings.getChkSubCate());
		String strBtnlnkId = null;
		//Adding code fixing the button link id is NULL when  upload image for on and off
		
			
		   if(null != screenSettings.getMenuFucntionality()) {
			   final Integer menuFutnId = Integer.parseInt(screenSettings.getMenuFucntionality());
			   if(subMenuFctnId == menuFutnId || anythingPageFctnId == menuFutnId || appSiteFctnId == menuFutnId) {
				   if(null != screenSettings.getBtnLinkId()) {
					   String[] btnLinks = screenSettings.getBtnLinkId().split(",");
					   for(String btnlnk : btnLinks) {
						   if(null != btnlnk && !btnlnk.equals("NULL")) {
							   screenSettings.setBtnLinkId(btnlnk);
							   break;
						   }
					   }
				   }				   
			   }
		   }
		
		
		   if(null != screenSettings.getBtnLinkId()) {
				if(screenSettings.getBtnLinkId().startsWith("NULL")) {
					strBtnlnkId = screenSettings.getBtnLinkId();
					if(strBtnlnkId.equals("NULL")) {
						screenSettings.setBtnLinkId(null);
					}else{
					strBtnlnkId = strBtnlnkId.substring(strBtnlnkId.lastIndexOf("NULL,"), strBtnlnkId.length());
					if(strBtnlnkId != null && strBtnlnkId.startsWith("NULL,"))
					{
						strBtnlnkId = strBtnlnkId.replaceFirst("NULL,", ",");
						if(strBtnlnkId.startsWith(","))
						{
							strBtnlnkId = strBtnlnkId.substring(1, strBtnlnkId.length());
						
						}
					}
						
					if(strBtnlnkId != null && strBtnlnkId.equals("NULL"))
					{
						screenSettings.setBtnLinkId(null);
					}else{
					screenSettings.setBtnLinkId(strBtnlnkId);
						}
					}
				
			}
			}
		
		// Code for Adding menu item
		if (null != addorDeleteMenuItem
				&& "CreateTab".equals(addorDeleteMenuItem)) {

			// Validate menu item details
			buttomBarButtonValidator.validate(screenSettings, result);

			if (null != screenSettings.getiTunesLnk()
					&& !"".equals(screenSettings.getiTunesLnk())) {
				isValidiTunesURL = Utility.validateURL(screenSettings
						.getiTunesLnk());
				if (!isValidiTunesURL) {
					buttomBarButtonValidator.validate(screenSettings, result,
							ApplicationConstants.INVALIDURL,
							ApplicationConstants.IPHONETYPE);
				}
			}

			if (null != screenSettings.getPlayStoreLnk()
					&& !"".equals(screenSettings.getPlayStoreLnk())) {
				isValidGoogleURL = Utility.validateURL(screenSettings
						.getPlayStoreLnk());

				if (!isValidGoogleURL) {
					buttomBarButtonValidator.validate(screenSettings, result,
							ApplicationConstants.INVALIDURL,
							ApplicationConstants.ANDROIDTYPE);
				}
			}

			if (result.hasErrors()) {
				request.setAttribute("iPhone", screenSettings.getiTunesLnk());
				request.setAttribute("android",
						screenSettings.getPlayStoreLnk());
				return new ModelAndView(viewName);
			} else {
				// If iconicMenuItems list is empty,create list, add menu item
				// to list
				// and store in session
				if (null == tabBarbuttonsList || tabBarbuttonsList.isEmpty()) {
					tabBarbuttonsList = new ArrayList<ScreenSettings>();
					tabBarBtn = new ScreenSettings();

					if ("upldOwn".equals(screenSettings.getIconSelection())) {
						tabBarBtn.setImagePath(ApplicationConstants.TEMPIMGPATH
								+ screenSettings.getLogoImageName());
						tabBarBtn.setLogoImageName(screenSettings
								.getLogoImageName());

					} else if (screenSettings.getIconSelection().equals(
							"exstngIcon")) {

						tabBarBtn.setImagePath(screenSettings.getImagePath());
						tabBarBtn.setIconId(screenSettings.getIconId());

					}
					tabBarBtn.setMenuIconId(ApplicationConstants.BUTTONID
							+ screenSettings.getMenuFucntionality());
					tabBarBtn.setMenuFucntionality(screenSettings
							.getMenuFucntionality());
					tabBarBtn.setHiddenmenuFnctn(screenSettings
							.getMenuFucntionality());
					tabBarBtn.setiTunesLnk(screenSettings.getiTunesLnk());
					tabBarBtn.setPlayStoreLnk(screenSettings.getPlayStoreLnk());
					tabBarBtn.setBtnLinkId(screenSettings.getBtnLinkId());
					tabBarBtn.setHiddenBtnLinkId(screenSettings.getBtnLinkId());
					tabBarBtn.setHiddenSubCate(screenSettings.getChkSubCate());
					tabBarBtn.setChkSubCate(screenSettings.getChkSubCate());
					tabBarBtn.setSubCatIds(screenSettings.getSubCatIds());
					hubCitiService.saveUpdateTabBarButton(tabBarBtn, user);

					tabBarbuttonsList.add(tabBarBtn);

					session.setAttribute("tabBarbuttonsList", tabBarbuttonsList);
				} else {
					// Code for updating button detail
					if (null != btnId && !"".equals(btnId)) {

						mainLoop: for (int i = 0; i < tabBarbuttonsList.size(); i++) {

							ScreenSettings buttonbtn = tabBarbuttonsList.get(i);
							if (buttonbtn.getMenuIconId().equals(btnId)) {

								for (int j = 0; j < tabBarbuttonsList.size(); j++) {

									ScreenSettings btn = tabBarbuttonsList
											.get(j);
									if (!screenSettings.getBottomBtnId()
											.equals(btn.getBottomBtnId())) {

										if (btn.getMenuFucntionality().equals(
												String.valueOf(subMenuFctnId))
												&& String
														.valueOf(subMenuFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (btn.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {
													buttomBarButtonValidator
															.validate(
																	screenSettings,
																	result,
																	ApplicationConstants.DUPLICATESUBMENU);
													break;
												}
											}
										}
										if (btn.getMenuFucntionality()
												.equals(String
														.valueOf(anythingPageFctnId))
												&& String
														.valueOf(
																anythingPageFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (btn.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {
													buttomBarButtonValidator
															.validate(
																	screenSettings,
																	result,
																	ApplicationConstants.DUPLICATEANYTHINGPAGE);
													break;
												}
											}
										}
										if (btn.getMenuFucntionality().equals(
												String.valueOf(appSiteFctnId))
												&& String
														.valueOf(appSiteFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (btn.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {
													buttomBarButtonValidator
															.validate(
																	screenSettings,
																	result,
																	ApplicationConstants.DUPLICATEAPPSITE);
													break;
												}
											}
										}
										if (btn.getMenuFucntionality().equals(
												String.valueOf(findFctnId))
												&& String
														.valueOf(findFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (btn.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {
													if (btn.getBtnLinkId()
															.contains(",")) {
														buttomBarButtonValidator
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATECATEGORIES);
													} else {
														buttomBarButtonValidator
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATECATEGORY);
													}
													break;
												}
											}
										}

										if (btn.getMenuFucntionality().equals(
												String.valueOf(eventFctnId))
												&& String
														.valueOf(eventFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (btn.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {
													if (btn.getBtnLinkId()
															.contains(",")) {
														buttomBarButtonValidator
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATECATEGORIES);
													} else {
														buttomBarButtonValidator
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATECATEGORY);
													}

													break;
												}
											}

										}
										//fundraiser code
										if (btn.getMenuFucntionality().equals(
												String.valueOf(fundraFctnId))
												&& String
														.valueOf(fundraFctnId)
														.equals(menuFunctionlity)) {
											if (null != screenSettings
													.getBtnLinkId()
													&& !"".equals(screenSettings
															.getBtnLinkId())) {
												if (btn.getBtnLinkId()
														.equals(screenSettings
																.getBtnLinkId())) {
													if (btn.getBtnLinkId()
															.contains(",")) {
														buttomBarButtonValidator
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATECATEGORIES);
													} else {
														buttomBarButtonValidator
																.validate(
																		screenSettings,
																		result,
																		ApplicationConstants.DUPLICATECATEGORY);
													}

													break;
												}
											}

										}

										
										
										

										if (btn.getMenuFucntionality()
												.equals(screenSettings
														.getMenuFucntionality())
												&& !btn.getMenuFucntionality()
														.equals(String
																.valueOf(subMenuFctnId))
												&& !btn.getMenuFucntionality()
														.equals(String
																.valueOf(anythingPageFctnId))
												&& !btn.getMenuFucntionality()
														.equals(String
																.valueOf(appSiteFctnId))
												&& !btn.getMenuFucntionality()
														.equals(String
																.valueOf(findFctnId))
												&& !btn.getMenuFucntionality()
														.equals(String
																.valueOf(eventFctnId))
												&& !btn.getMenuFucntionality()
														.equals(String
																.valueOf(fundraFctnId))
																
												) {

											buttomBarButtonValidator
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATEFUNCTINALITY);
											break;
										}
									}
								}
								// Code for validating iphone and android links

								if (null != screenSettings.getiTunesLnk()) {
									isValidiTunesURL = Utility
											.validateURL(screenSettings
													.getiTunesLnk());
									if (!isValidiTunesURL) {
										buttomBarButtonValidator
												.validate(
														screenSettings,
														result,
														ApplicationConstants.INVALIDURL,
														ApplicationConstants.IPHONETYPE);
									}
								}
								if (null != screenSettings.getPlayStoreLnk()) {
									isValidGoogleURL = Utility
											.validateURL(screenSettings
													.getPlayStoreLnk());

									if (!isValidGoogleURL) {
										buttomBarButtonValidator
												.validate(
														screenSettings,
														result,
														ApplicationConstants.INVALIDURL,
														ApplicationConstants.ANDROIDTYPE);
									}
								}

								if (result.hasErrors()) {
									return new ModelAndView(viewName);
								}

								buttonbtn.setBottomBtnId(screenSettings
										.getBottomBtnId());

								if ("upldOwn".equals(screenSettings
										.getIconSelection())) {
									buttonbtn
											.setImagePath(ApplicationConstants.TEMPIMGPATH
													+ screenSettings
															.getLogoImageName());
									buttonbtn.setLogoImageName(screenSettings
											.getLogoImageName());
									buttonbtn.setIconId(null);
									buttonbtn
											.setImagePathOff(ApplicationConstants.TEMPIMGPATH
													+ screenSettings
															.getBannerImageName());
									buttonbtn.setBannerImageName(screenSettings
											.getBannerImageName());

								} else if ("exstngIcon".equals(screenSettings
										.getIconSelection())) {

									buttonbtn.setImagePath(screenSettings
											.getImagePath());
									buttonbtn.setIconId(screenSettings
											.getIconId());

								}
								buttonbtn
										.setMenuIconId(ApplicationConstants.BUTTONID
												+ screenSettings
														.getMenuFucntionality());
								buttonbtn.setMenuFucntionality(screenSettings
										.getMenuFucntionality());
								buttonbtn.setHiddenmenuFnctn(screenSettings
										.getMenuFucntionality());

								buttonbtn.setiTunesLnk(screenSettings
										.getiTunesLnk());
								buttonbtn.setPlayStoreLnk(screenSettings
										.getPlayStoreLnk());
								buttonbtn.setBtnLinkId(screenSettings
										.getBtnLinkId());
								buttonbtn.setHiddenBtnLinkId(screenSettings
										.getBtnLinkId());
								buttonbtn.setHiddenSubCate(screenSettings
										.getChkSubCate());
								buttonbtn.setChkSubCate(screenSettings
										.getChkSubCate());
								buttonbtn.setSubCatIds(screenSettings
										.getSubCatIds());
								hubCitiService.saveUpdateTabBarButton(
										buttonbtn, user);

								tabBarbuttonsList.remove(i);
								tabBarbuttonsList.add(i, buttonbtn);
								session.setAttribute("tabBarbuttonsList",
										tabBarbuttonsList);
								break mainLoop;
							}

						}

					} else {
						// If menu items list is already created, Validate for
						// duplicate button and add unique menu items to list
						for (ScreenSettings buttonbtn : tabBarbuttonsList) {
							if (buttonbtn.getMenuFucntionality().equals(
									String.valueOf(subMenuFctnId))
									&& String.valueOf(subMenuFctnId).equals(
											menuFunctionlity)) {
								if (null != screenSettings.getBtnLinkId()
										&& !"".equals(screenSettings
												.getBtnLinkId())) {
									if (buttonbtn.getBtnLinkId().equals(
											screenSettings.getBtnLinkId())) {
										buttomBarButtonValidator
												.validate(
														screenSettings,
														result,
														ApplicationConstants.DUPLICATESUBMENU);
										break;
									}
								}
							}
							if (buttonbtn.getMenuFucntionality().equals(
									String.valueOf(anythingPageFctnId))
									&& String.valueOf(anythingPageFctnId)
											.equals(menuFunctionlity)) {
								if (null != screenSettings.getBtnLinkId()
										&& !"".equals(screenSettings
												.getBtnLinkId())) {
									if (buttonbtn.getBtnLinkId().equals(
											screenSettings.getBtnLinkId())) {
										buttomBarButtonValidator
												.validate(
														screenSettings,
														result,
														ApplicationConstants.DUPLICATEANYTHINGPAGE);
										break;
									}
								}
							}
							if (buttonbtn.getMenuFucntionality().equals(
									String.valueOf(appSiteFctnId))
									&& String.valueOf(appSiteFctnId).equals(
											menuFunctionlity)) {
								if (null != screenSettings.getBtnLinkId()
										&& !"".equals(screenSettings
												.getBtnLinkId())) {
									if (buttonbtn.getBtnLinkId().equals(
											screenSettings.getBtnLinkId())) {
										buttomBarButtonValidator
												.validate(
														screenSettings,
														result,
														ApplicationConstants.DUPLICATEAPPSITE);
										break;
									}
								}
							}
							if (buttonbtn.getMenuFucntionality().equals(
									String.valueOf(findFctnId))
									&& String.valueOf(findFctnId).equals(
											menuFunctionlity)) {
								if (null != screenSettings.getBtnLinkId()
										&& !"".equals(screenSettings
												.getBtnLinkId())) {
									if (buttonbtn.getBtnLinkId().equals(
											screenSettings.getBtnLinkId())) {
										if (buttonbtn.getBtnLinkId().contains(
												",")) {
											buttomBarButtonValidator
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATECATEGORIES);
										} else {
											buttomBarButtonValidator
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATECATEGORY);
										}
										break;
									}
								}
							}

							if (buttonbtn.getMenuFucntionality().equals(
									String.valueOf(eventFctnId))
									&& String.valueOf(eventFctnId).equals(
											menuFunctionlity)) {
								if (null != screenSettings.getBtnLinkId()
										&& !"".equals(screenSettings
												.getBtnLinkId())) {
									if (buttonbtn.getBtnLinkId().equals(
											screenSettings.getBtnLinkId())) {
										if (buttonbtn.getBtnLinkId().contains(
												",")) {
											buttomBarButtonValidator
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATECATEGORIES);
										} else {
											buttomBarButtonValidator
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATECATEGORY);
										}

										break;
									}
								}

							}
							
							//fundraiser code
							if (buttonbtn.getMenuFucntionality().equals(
									String.valueOf(fundraFctnId))
									&& String.valueOf(fundraFctnId).equals(
											menuFunctionlity)) {
								if (null != screenSettings.getBtnLinkId()
										&& !"".equals(screenSettings
												.getBtnLinkId())) {
									if (buttonbtn.getBtnLinkId().equals(
											screenSettings.getBtnLinkId())) {
										if (buttonbtn.getBtnLinkId().contains(
												",")) {
											buttomBarButtonValidator
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATECATEGORIES);
										} else {
											buttomBarButtonValidator
													.validate(
															screenSettings,
															result,
															ApplicationConstants.DUPLICATECATEGORY);
										}

										break;
									}
								}

							}
							

							if (buttonbtn.getMenuFucntionality().equals(
									screenSettings.getMenuFucntionality())
									&& !buttonbtn.getMenuFucntionality()
											.equals(String
													.valueOf(subMenuFctnId))
									&& !buttonbtn
											.getMenuFucntionality()
											.equals(String
													.valueOf(anythingPageFctnId))
									&& !buttonbtn.getMenuFucntionality()
											.equals(String
													.valueOf(appSiteFctnId))
									&& !buttonbtn.getMenuFucntionality()
											.equals(String.valueOf(findFctnId))
									&& !buttonbtn
											.getMenuFucntionality()
											.equals(String.valueOf(eventFctnId))
									
									&& !buttonbtn
											.getMenuFucntionality()
											.equals(String.valueOf(fundraFctnId))
									) {

								buttomBarButtonValidator
										.validate(
												screenSettings,
												result,
												ApplicationConstants.DUPLICATEFUNCTINALITY);
								break;
							}

						}
						if (result.hasErrors()) {
							return new ModelAndView(viewName);
						}
						tabBarBtn = new ScreenSettings();

						if ("upldOwn".equals(screenSettings.getIconSelection())) {
							tabBarBtn
									.setImagePath(ApplicationConstants.TEMPIMGPATH
											+ screenSettings.getLogoImageName());
							tabBarBtn.setLogoImageName(screenSettings
									.getLogoImageName());
							tabBarBtn
									.setImagePathOff(ApplicationConstants.TEMPIMGPATH
											+ screenSettings
													.getBannerImageName());
							tabBarBtn.setBannerImageName(screenSettings
									.getBannerImageName());

						} else if ("exstngIcon".equals(screenSettings
								.getIconSelection())) {

							tabBarBtn.setImagePath(screenSettings
									.getImagePath());
							tabBarBtn.setIconId(screenSettings.getIconId());

						}
						tabBarBtn.setMenuIconId(ApplicationConstants.BUTTONID
								+ screenSettings.getMenuFucntionality());
						tabBarBtn.setMenuFucntionality(screenSettings
								.getMenuFucntionality());
						tabBarBtn.setHiddenmenuFnctn(screenSettings
								.getMenuFucntionality());
						tabBarBtn.setiTunesLnk(screenSettings.getiTunesLnk());
						tabBarBtn.setPlayStoreLnk(screenSettings
								.getPlayStoreLnk());
						tabBarBtn.setBtnLinkId(screenSettings.getBtnLinkId());
						tabBarBtn.setHiddenBtnLinkId(screenSettings
								.getBtnLinkId());
						tabBarBtn.setHiddenSubCate(screenSettings
								.getChkSubCate());
						tabBarBtn.setSubCatIds(screenSettings.getSubCatIds());
						tabBarBtn.setChkSubCate(screenSettings.getChkSubCate());
						tabBarBtn.setSubCatIds(screenSettings.getSubCatIds());
						hubCitiService.saveUpdateTabBarButton(tabBarBtn, user);
						tabBarbuttonsList.add(tabBarBtn);
						session.setAttribute("tabBarbuttonsList",
								tabBarbuttonsList);

					}

				}
			}

		}
		// Code for deleting menu item
		else if (null != addorDeleteMenuItem
				&& "DeleteButton".equals(addorDeleteMenuItem)) {

			LOG.info("Deleting Tab button:" + bottomBtnId);
			for (int i = 0; i < tabBarbuttonsList.size(); i++) {

				ScreenSettings menuItem = tabBarbuttonsList.get(i);
				if (menuItem.getBottomBtnId().equals(bottomBtnId)) {
					status = hubCitiService.deleteTabBarButton(bottomBtnId);

					if (null != status) {
						if (status.equals(ApplicationConstants.FAILURE)) {
							buttomBarButtonValidator.validate(screenSettings,
									result, ApplicationConstants.ISASSOCIATED);
							if (result.hasErrors()) {
								return new ModelAndView(viewName);
							}
						} else {
							tabBarbuttonsList.remove(i);
							session.setAttribute("tabBarbuttonsList",
									tabBarbuttonsList);
							break;
						}
					}
				}
			}
		}

		// session.setAttribute("tabBarIconPreview",
		// ApplicationConstants.DEFAULTIMAGESQR);
		screenSettings = new ScreenSettings();
		model.put("screenSettingsForm", screenSettings);

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return new ModelAndView(new RedirectView("setuptabbar.htm"));
	}

	@RequestMapping(value = "/displayretnames", method = RequestMethod.GET)
	@ResponseBody
	public String getHubCityRetailer(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws HubCitiServiceException {
		String strMethodName = "getHubCityRetailer";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		String strRetName = request.getParameter("term");
		List<AppSiteDetails> retailerLst = null;
		JSONObject object = new JSONObject();
		JSONObject valueObj = null;
		JSONArray array = new JSONArray();
		AppSiteDetails objAppSiteDetails = new AppSiteDetails();
		String value;

		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");

		try {

			response.setContentType("application/json");
			User user = (User) session.getAttribute("loginUser");

			objAppSiteDetails.setHubCityId(user.getHubCitiID());
			objAppSiteDetails.setSearchKey(strRetName);
			retailerLst = hubCitiService.getHubCityRetailer(objAppSiteDetails);

			if (null != retailerLst && !retailerLst.isEmpty()) {
				for (AppSiteDetails ret : retailerLst) {
					value = ret.getRetName();
					valueObj = new JSONObject();
					valueObj.put("retId", ret.getRetailId());
					valueObj.put("retname", ret.getRetName());
					valueObj.put("rname", value);
					valueObj.put("value", value);
					array.put(valueObj);
				}
			} else {
				valueObj = new JSONObject();
				valueObj.put("lable", "No Records Found");
				valueObj.put("value", "No Records Found");
				array.put(valueObj);
			}
			object.put("retnamelst", array);

		} catch (HubCitiServiceException exception) {
			throw new HubCitiServiceException(exception);
		}
		LOG.info(ApplicationConstants.METHODEND + strMethodName);

		return object.get("retnamelst").toString();

	}

	@RequestMapping(value = "/displayretLoc", method = RequestMethod.GET)
	@ResponseBody
	public String displayRetailLocations(
			@RequestParam(value = "retId", required = true) String retId,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session, ModelMap model) throws HubCitiServiceException

	{
		final String strMethodName = "displayRetailLocations";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		List<AppSiteDetails> retLocationLst = null;
		// String retId = request.getParameter("term");
		AppSiteDetails objAppSiteDetails = new AppSiteDetails();
		int iHubCityId = 0;
		JSONObject object = new JSONObject();
		JSONObject valueObj = null;
		JSONArray array = new JSONArray();
		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");
		String value = null;
		try {
			User user = (User) session.getAttribute("loginUser");
			if (null != user && !"".equals(user)) {
				iHubCityId = user.getHubCitiID();
				objAppSiteDetails.setHubCityId(iHubCityId);
			}
			if (null != retId && !"".equals(retId)) {
				objAppSiteDetails.setRetailId(Integer.valueOf(retId));
			}

			retLocationLst = hubCitiService
					.displayRetailLocations(objAppSiteDetails);

			if (null != retLocationLst && !retLocationLst.isEmpty()) {
				for (AppSiteDetails ret : retLocationLst) {
					value = ret.getAddress();
					valueObj = new JSONObject();

					valueObj.put("retLocId", ret.getRetLocId());
					valueObj.put("address", value);

					array.put(valueObj);
				}
			} else {
				valueObj = new JSONObject();
				valueObj.put("lable", "No Records Found");
				valueObj.put("value", "No Records Found");
				array.put(valueObj);
			}
			object.put("retloclst", array);

		} catch (HubCitiServiceException exception) {
			throw new HubCitiServiceException(exception);
		}

		return object.get("retloclst").toString();

	}

	@RequestMapping(value = "/saveappsite.htm", method = RequestMethod.GET)
	@ResponseBody
	public final String saveAppSite(
			@RequestParam(value = "sitename", required = true) String strApSiteName,
			@RequestParam(value = "retlocid", required = true) int retLocId,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws HubCitiServiceException {
		String strResponse = null;
		AppSiteDetails objAppSiteDetails = new AppSiteDetails();
		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");

		List<AppSiteDetails> appSiteDetailsLst = (ArrayList<AppSiteDetails>) session
				.getAttribute("appsitelst");

		if (null == appSiteDetailsLst) {

			appSiteDetailsLst = new ArrayList<AppSiteDetails>();
		}

		try {
			User user = (User) session.getAttribute("loginUser");
			if (null != user && !"".equals(user)) {
				objAppSiteDetails.setHubCityId(user.getHubCitiID());
				objAppSiteDetails.setHcUserId(user.gethCAdminUserID());
			}
			if (null != strApSiteName && !"".equals(strApSiteName)) {
				objAppSiteDetails.setAppSiteName(strApSiteName);
			}
			objAppSiteDetails.setRetLocId(retLocId);
			strResponse = hubCitiService.saveAppSite(objAppSiteDetails);

			if (null != strResponse
					&& strResponse.contains(ApplicationConstants.SUCCESS)) {

				objAppSiteDetails.setAppSiteId(strResponse.split(",")[1]);
				appSiteDetailsLst.add(objAppSiteDetails);
				session.setAttribute("appsitelst", appSiteDetailsLst);

			}

		} catch (HubCitiServiceException exception) {
			throw new HubCitiServiceException(exception);
		}

		return strResponse;
	}

}
