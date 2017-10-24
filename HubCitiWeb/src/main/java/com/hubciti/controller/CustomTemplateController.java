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

import org.junit.runner.Request;
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

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.exception.HubCitiServiceException;
import com.hubciti.common.pojo.AppSiteDetails;
import com.hubciti.common.pojo.Department;
import com.hubciti.common.pojo.GroupTemplate;
import com.hubciti.common.pojo.MenuDetails;
import com.hubciti.common.pojo.ScreenSettings;
import com.hubciti.common.pojo.TabBarDetails;
import com.hubciti.common.pojo.Type;
import com.hubciti.common.pojo.User;
import com.hubciti.common.util.Utility;
import com.hubciti.service.HubCitiService;
import com.hubciti.validator.GroupAndListMenuValidation;
import com.hubciti.validator.IconinMenuValidator;
import com.hubciti.validator.TwoColTabValidator;

/**
 * This class is a Controller class for HubCiti Custom Menu Templates Setup.
 * 
 * @author sangeetha.ts
 */
@Controller
public class CustomTemplateController {
	/**
	 * Getting the logger Instance.
	 */

	private static final Logger LOG = LoggerFactory.getLogger(MainMenuController.class);

	private GroupAndListMenuValidation groupAndListMenuValidation;

	IconinMenuValidator iconinMenuValidator;

	@Autowired
	public void setIconinMenuValidator(IconinMenuValidator iconinMenuValidator) {
		this.iconinMenuValidator = iconinMenuValidator;
	}

	TwoColTabValidator twoColTabValidator;

	@Autowired
	public void setTwoColTabValidator(TwoColTabValidator twoColTabValidator) {
		this.twoColTabValidator = twoColTabValidator;
	}

	/**
	 * @param groupAndListMenuValidation
	 *            the groupAndListMenuValidation to set
	 */
	@Autowired
	public void setGroupAndListMenuValidation(GroupAndListMenuValidation groupAndListMenuValidation) {
		this.groupAndListMenuValidation = groupAndListMenuValidation;
	}

	public final String ASSOCIATEDEPT = "Associate Dept";

	public final String ASSOCIATETYPE = "Associate Type";

	public final String GROUPBTNTYPE = "GroupBtnType";

	public final String GROUPBTNIMG = "GroupBtnImage";
	
	public final String ASSOCIATECITY = "Associate City";

	/**
	 * This Method will return Group menu Screen.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/setupcombomenu.htm", method = RequestMethod.GET)
	public String setUpComboMenu(@ModelAttribute("menuDetails") MenuDetails menuDetails, BindingResult result, HttpServletRequest request,
			ModelMap model, HttpSession session) throws HubCitiServiceException {
		String methodName = "setUpComboMenu";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String menuId = null;
		String subMenuName = null;
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		MenuDetails menuDetailsObj = null;
		session.removeAttribute("menuBarButtonsList");
		try {

			if (request.getParameter("hidMenuType").equals(ApplicationConstants.SUBMENU)) {
				session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.SETUPSUBMENU);
			} else {
				session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.MAINMENUSCREEN);
			}
			// session.setAttribute(ApplicationConstants.MENUNAME,
			// ApplicationConstants.MAINMENUSCREEN);

			session.setAttribute("setupcombomenuImgSquare", ApplicationConstants.DEFAULTIMAGESQR);
			session.setAttribute("setupcombomenuImgCircle", ApplicationConstants.DEFAULTIMAGE);
			session.setAttribute("minCropHt", 160);
			session.setAttribute("minCropWd", 160);

			User user = (User) session.getAttribute("loginUser");

			menuDetailsObj = new MenuDetails();
			menuDetailsObj.setMenuTypeName(ApplicationConstants.COMBOTEMPLATE);
			menuDetailsObj.setLevel(menuDetails.getLevel());

			if (null == menuDetails.getLevel()) {
				menuDetailsObj.setMenuName(null);

			} else {
				menuDetailsObj.setMenuName(ApplicationConstants.MAINMENU);

			}

			if (null != menuDetails.getMenuId() && !"".equals(menuDetails.getMenuId())) {
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
			screenSettings.setPageTitle("Text");
			// screenSettings.setMenuId(menuId);
			model.put("screenSettingsForm", screenSettings);

		} catch (HubCitiServiceException exception) {

			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);

		return "setupcombomenu";
	}

	/**
	 * This Method will return Combo menu Screen.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/createcombomenu.htm", method = RequestMethod.POST)
	public String createComboButton(@ModelAttribute("screenSettingsForm") ScreenSettings screenSettings, BindingResult result,
			HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		String methodName = "createComboButton";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.MAINMENUSCREEN);
		List<String> comboList = new ArrayList<String>();
		String grpList = screenSettings.getGrpList();
		String buttonName = screenSettings.getMenuBtnName();
		String menuId = screenSettings.getMenuId();
		String subMenuName = screenSettings.getSubMenuName();
		Integer menuLevel = screenSettings.getMenuLevel();
		String btnId = screenSettings.getMenuIconId();
		String viewName = screenSettings.getViewName();
		String addorDeleteMenuItem = screenSettings.getAddDeleteBtn();
		String menuFunctionlity = screenSettings.getMenuFucntionality();
		screenSettings.setHiddenmenuFnctn(screenSettings.getMenuFucntionality());
		screenSettings.setHiddenBtnLinkId(screenSettings.getBtnLinkId());
		screenSettings.setHiddenbtnGroup(screenSettings.getBtnGroup());
		screenSettings.setHiddenCitiId(screenSettings.getCitiId());
		ScreenSettings grpMenuItem = null;
		String selGrpName = screenSettings.getBtnGroup();
		ScreenSettings menuItem = null;
		GroupTemplate groupTemplate = null;
		String grpName = screenSettings.getGrpName();
		List<ScreenSettings> grpBtnsList = null;
		String comboBtnType = screenSettings.getComboBtnType();
		String groupHeader = screenSettings.getPageTitle();
		String grpbtntype = screenSettings.getGrpBtnType();
		String grpbtntypeId = screenSettings.getGrpBtnTypeId();
		screenSettings.setHiddenPageTitle(groupHeader);
		Integer subMenuFctnId = (Integer) session.getAttribute("subMenuFctnId");
		Integer anythingPageFctnId = (Integer) session.getAttribute("anythingPageFctnId");
		Integer appSiteFctnId = (Integer) session.getAttribute("appSiteFctnId");
		Integer findFctnId = (Integer) session.getAttribute("findFctnId");
		Integer eventFctnId = (Integer) session.getAttribute("eventFctnId");
		Integer fundraFctnId = (Integer) session.getAttribute("fundraFctnId");
		
		screenSettings.setHiddenSubCate(screenSettings.getChkSubCate());
		screenSettings.setChkSubCate(screenSettings.getChkSubCate());
		if(null != screenSettings.getMenuFucntionality() && screenSettings.getMenuFucntionality().equals(String.valueOf(findFctnId)) && null != screenSettings.getBtnLinkId()) {
			String[] findCat = screenSettings.getBtnLinkId().split(",");
			String findCatList = null;
			
			for(String cat : findCat) {
				if(cat.contains("MC")) {					
					if(null != findCatList) {
						findCatList += cat + ",";
					}
					else {
						findCatList = new String();
						findCatList = cat + ",";
					}					
				}
			}
			if(findCatList.endsWith(",")) {
				findCatList = findCatList.substring(0, findCatList.length() - 1);
			}
			screenSettings.setBtnLinkId(findCatList);
		}

		List<GroupTemplate> comboBtnList = (ArrayList<GroupTemplate>) session.getAttribute("comboBtnList");
		List<GroupTemplate> newComboBtnList = null;
		request.setAttribute("gprdMenuAction", "Button");
		// List of Menu items
		List<ScreenSettings> previewMenuItems = new ArrayList<ScreenSettings>();		
		String tempgrpbtntype[] = null;
		String tempgrpbtntypeId[] = null;

		String btnOrderArry[] = null;
		List<String> btnOrderListFixed = new ArrayList<String>();
		List<String> btnOrderList = new ArrayList<String>();
		String btnOrder = screenSettings.getBtnPosition();
		@SuppressWarnings("unused")
		boolean newGrpFlag = false;
		if (!"".equals(Utility.checkNull(btnOrder))) {
			btnOrderArry = btnOrder.split("~");
			btnOrderListFixed = Arrays.asList(btnOrderArry);
			btnOrderList.addAll(btnOrderListFixed);

		}

		tempgrpbtntype = grpbtntype.split("~");
		tempgrpbtntypeId = grpbtntypeId.split("~");

		if (!btnOrderList.isEmpty()) {
			newComboBtnList = new ArrayList<GroupTemplate>();

			for (String btnName : btnOrderList) {
				boolean isGrp = false;
				for (GroupTemplate template : comboBtnList) {
					if (btnName.equals("Text-" + template.getGrpName())) {
						GroupTemplate template2 = new GroupTemplate();
						template2.setGrpName(template.getGrpName());
						template2.setGrpBtnsList(null);
						newComboBtnList.add(template2);
						isGrp = true;
						break;
					}
				}
				if (!isGrp) {
					int index = newComboBtnList.size() - 1;
					GroupTemplate template2 = newComboBtnList.get(index);
					boolean isBtnExist = false;
					for (GroupTemplate template1 : comboBtnList) {
						for (ScreenSettings comboBtn : template1.getGrpBtnsList()) {
							if (comboBtn.getMenuIconId().equals(btnName)) {
								isBtnExist = true;
								if (null != tempgrpbtntype[index] && !"null".equals(tempgrpbtntype[index])) {
									comboBtn.setComboBtnType(tempgrpbtntype[index]);
									if ("Rectangle".equals(tempgrpbtntype[index])) {
										comboBtn.setLogoImage(null);
										comboBtn.setImagePath(null);
									}
								} else {
									comboBtn.setComboBtnType(null);
								}

								if (null != tempgrpbtntypeId[index] && !"null".equals(tempgrpbtntypeId[index])) {
									comboBtn.setComboBtnTypeId(Integer.parseInt(tempgrpbtntypeId[index]));
								} else {
									comboBtn.setComboBtnTypeId(null);
								}
								if (null != template2.getGrpBtnsList()) {
									List<ScreenSettings> list = template2.getGrpBtnsList();
									list.add(comboBtn);
									template2.setGrpBtnsList(list);
									newComboBtnList.remove(index);
									newComboBtnList.add(index, template2);
									template1.getGrpBtnsList().remove(comboBtn);
									break;
								} else {
									List<ScreenSettings> list = new ArrayList<ScreenSettings>();
									list.add(comboBtn);
									template2.setGrpBtnsList(list);
									newComboBtnList.remove(index);
									newComboBtnList.add(index, template2);
									template1.getGrpBtnsList().remove(comboBtn);
									break;
								}
							}
						}
						if (isBtnExist) {
							break;
						}
					}
				}
			}
		}

		comboBtnList = newComboBtnList;

		session.setAttribute("comboBtnList", comboBtnList);

		if (null != comboBtnList) {
			for (int i = 0; i < comboBtnList.size(); i++) {
				GroupTemplate group = comboBtnList.get(i);
				grpBtnsList = group.getGrpBtnsList();
				if (null != grpBtnsList && !grpBtnsList.isEmpty()) {
					menuItem = new ScreenSettings();
					menuItem.setMenuBtnName(group.getGrpName());
					if (groupHeader.equals(ApplicationConstants.TEXT)) {
						menuItem.setMenuFucntionality(((Integer) session.getAttribute("textFctnId")).toString());
					} else {
						menuItem.setMenuFucntionality(((Integer) session.getAttribute("labelFctnId")).toString());
					}
					menuItem.setMenuIconId(ApplicationConstants.TEXT + "-" + group.getGrpName());
					menuItem.setComboBtnType(null);
					menuItem.setComboBtnTypeId(null);
					menuItem.setPageTitle(groupHeader);
					previewMenuItems.add(menuItem);
					for (ScreenSettings btn : grpBtnsList) {
						menuItem = new ScreenSettings();

						menuItem.setMenuBtnName(btn.getMenuBtnName());
						menuItem.setMenuIconId(btn.getMenuIconId());
						menuItem.setMenuFucntionality(btn.getMenuFucntionality());
						menuItem.setBtnLinkId(btn.getBtnLinkId());
						menuItem.setBtnDept(btn.getBtnDept());
						menuItem.setBtnType(btn.getBtnType());
						menuItem.setBtnGroup(group.getGrpName());
						menuItem.setHiddenbtnGroup(group.getGrpName().replace(" ", ""));
						menuItem.setPageTitle(groupHeader);
						menuItem.setComboBtnType(btn.getComboBtnType());
						menuItem.setComboBtnTypeId(btn.getComboBtnTypeId());
						menuItem.setSubCatIds(btn.getSubCatIds());
						menuItem.setHiddenSubCate(btn.getChkSubCate());
						menuItem.setChkSubCate(btn.getChkSubCate());
						menuItem.setCitiId(btn.getCitiId());
						
						if (!"Rectangle".equals(btn.getComboBtnType())) {
							menuItem.setLogoImageName(btn.getLogoImageName());
							menuItem.setImagePath(ApplicationConstants.TEMPIMGPATH + btn.getLogoImageName());
						}
						previewMenuItems.add(menuItem);
					}
				}
			}
			session.setAttribute("previewMenuItems", previewMenuItems);
		}

		if (null != grpList && !"".equals(grpList)) {
			String[] grpNameList = grpList.split("~");

			for (int i = 0; i < grpNameList.length; i++) {
				comboList.add(grpNameList[i]);
			}
			session.setAttribute("comboList", comboList);
		}

		// Code for SubMenu Filters Implementation
		String[] menuFilterType = screenSettings.getMenuFilterType();

		if (null != menuFilterType) {
			if (menuFilterType.length != 0 || screenSettings.isEditFilter()) {
				session.setAttribute("menuFilterType", menuFilterType);
				screenSettings.setEditFilter(false);
			} else {
				menuFilterType = (String[]) session.getAttribute("menuFilterType");
				screenSettings.setMenuFilterType(menuFilterType);
			}

		}

		List<Type> filterTypeList = (ArrayList<Type>) session.getAttribute("filterTypeList");
		List<Department> filterDeptList = (ArrayList<Department>) session.getAttribute("filterDeptList");

		if (null != filterTypeList) {
			Type newType = null;
			if (!"0".equals(screenSettings.getBtnType())) {
				if (filterTypeList.size() != 0) {
					for (int i = 0; i < filterTypeList.size(); i++) {
						Type type = filterTypeList.get(i);

						if (type.getTypeName().equals(screenSettings.getBtnType())) {

							break;
						}

						if (i == filterTypeList.size() - 1) {
							newType = new Type();
							newType.setTypeName(screenSettings.getBtnType());
							filterTypeList.add(newType);
							session.setAttribute("filterTypeList", filterTypeList);
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

						if (dept.getDeptName().equals(screenSettings.getBtnDept())) {
							break;
						}

						if (i == filterDeptList.size() - 1) {
							newDept = new Department();
							newDept.setDeptName(screenSettings.getBtnDept());
							filterDeptList.add(newDept);
							session.setAttribute("filterDeptList", filterDeptList);
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
		if (null != addorDeleteMenuItem && "AddButton".equals(addorDeleteMenuItem)) {
			request.setAttribute("gprdMenuAction", "Button");
			// Validate menu item details
			if (null != screenSettings.getMenuFucntionality() && !"".equals(screenSettings.getMenuFucntionality())) {
				if (screenSettings.getMenuFucntionality().equals(String.valueOf(subMenuFctnId))) {
					screenSettings.setFunctionalityType(ApplicationConstants.SUBMENU);
				} else
					if (screenSettings.getMenuFucntionality().equals(String.valueOf(appSiteFctnId))) {
						screenSettings.setFunctionalityType(ApplicationConstants.APPSITE);
					} else
						if (screenSettings.getMenuFucntionality().equals(String.valueOf(anythingPageFctnId))) {
							screenSettings.setFunctionalityType(ApplicationConstants.ANYTHINGPAGE);
						} else
							if (screenSettings.getMenuFucntionality().equals(String.valueOf(findFctnId))) {
								screenSettings.setFunctionalityType(ApplicationConstants.FIND);
								screenSettings.setHiddenSubCate(screenSettings.getChkSubCate());
								screenSettings.setChkSubCate(screenSettings.getChkSubCate());
							} else
								if (screenSettings.getMenuFucntionality().equals(String.valueOf(eventFctnId))) {
									screenSettings.setFunctionalityType(ApplicationConstants.SETUPEVENTS);
								} else if (screenSettings.getMenuFucntionality().equals(
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
			groupAndListMenuValidation.validate(screenSettings, result);

			if (result.hasErrors()) {
				screenSettings.setBtnLinkId(null);
				return viewName;
			} else {
				// If menuItems list is empty,create list, add menu item
				// to list
				// and store in session
				if (null == comboBtnList || (null != comboBtnList && comboBtnList.isEmpty())) {
					// Holds grouped menu template data
					comboBtnList = new ArrayList<GroupTemplate>();
					// holds button details of a group
					grpBtnsList = new ArrayList<ScreenSettings>();
					// object for grouped menu template
					groupTemplate = new GroupTemplate();
					// set the group name for first group in the menu
					groupTemplate.setGrpName(selGrpName);

					grpMenuItem = new ScreenSettings();
					grpMenuItem.setMenuBtnName(screenSettings.getMenuBtnName().trim());
					grpMenuItem.setMenuFucntionality(screenSettings.getMenuFucntionality());
					grpMenuItem.setHiddenmenuFnctn(screenSettings.getMenuFucntionality());
					grpMenuItem.setMenuIconId(ApplicationConstants.BUTTONID + screenSettings.getMenuBtnName().trim());
					grpMenuItem.setBtnLinkId(screenSettings.getBtnLinkId());
					grpMenuItem.setHiddenBtnLinkId(screenSettings.getBtnLinkId());
					grpMenuItem.setBtnGroup(selGrpName);
					grpMenuItem.setHiddenbtnGroup(selGrpName.replace(" ", ""));
					grpMenuItem.setPageTitle(groupHeader);
					grpMenuItem.setComboBtnType(comboBtnType);
					grpMenuItem.setComboBtnTypeId(screenSettings.getComboBtnTypeId());
					grpMenuItem.setSubCatIds(screenSettings.getSubCatIds());
					grpMenuItem.setHiddenSubCate(screenSettings.getChkSubCate());
					grpMenuItem.setChkSubCate(screenSettings.getChkSubCate());
					grpMenuItem.setCitiId(screenSettings.getCitiId());
					
					if (!"Rectangle".equals(comboBtnType)) {
						if ("Circle".equals(comboBtnType)) {
							grpMenuItem.setLogoImageName(screenSettings.getLogoImageName());
							grpMenuItem.setImagePath(ApplicationConstants.TEMPIMGPATH + screenSettings.getLogoImageName());
						} else {
							grpMenuItem.setLogoImageName(screenSettings.getBannerImageName());
							grpMenuItem.setImagePath(ApplicationConstants.TEMPIMGPATH + screenSettings.getBannerImageName());
						}

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
					comboBtnList.add(groupTemplate);
					session.setAttribute("comboBtnList", comboBtnList);
				} else {
					// code for updating button details
					if (null != btnId && !"".equals(btnId)) {
						// old groupName is the name of group before update and
						// below code will be when user updates button details
						// without changing group
						if (screenSettings.getOldGroupName().equals(selGrpName)) {
							for (int i = 0; i < comboBtnList.size(); i++) {
								GroupTemplate group = comboBtnList.get(i);
								if (group.getGrpName().equals(selGrpName)) {
									List<ScreenSettings> btnList = group.getGrpBtnsList();

									for (int j = 0; j < btnList.size(); j++) {
										grpMenuItem = btnList.get(j);
										if (grpMenuItem.getMenuIconId().equals(btnId)) {
											for (int n = 0; n < btnList.size(); n++) {
												if (n != j) {
													ScreenSettings button = btnList.get(n);
													if (button.getMenuBtnName().equals(buttonName)) {
														groupAndListMenuValidation.validate(screenSettings, result,
																ApplicationConstants.DUPLICATEBUTTONNAME);
														break;
													}

													if (button.getMenuFucntionality().equals(String.valueOf(subMenuFctnId))
															&& String.valueOf(subMenuFctnId).equals(menuFunctionlity)) {
														if (null != screenSettings.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
															if (button.getBtnLinkId().equals(screenSettings.getBtnLinkId())) {
																groupAndListMenuValidation.validate(screenSettings, result,
																		ApplicationConstants.DUPLICATESUBMENU);
																break;
															}
														}
													}
													if (button.getMenuFucntionality().equals(String.valueOf(anythingPageFctnId))
															&& String.valueOf(anythingPageFctnId).equals(menuFunctionlity)) {
														if (null != screenSettings.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
															if (button.getBtnLinkId().equals(screenSettings.getBtnLinkId())) {
																groupAndListMenuValidation.validate(screenSettings, result,
																		ApplicationConstants.DUPLICATEANYTHINGPAGE);
																break;
															}
														}
													}
													if (button.getMenuFucntionality().equals(String.valueOf(appSiteFctnId))
															&& String.valueOf(appSiteFctnId).equals(menuFunctionlity)) {
														if (null != screenSettings.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
															if (button.getBtnLinkId().equals(screenSettings.getBtnLinkId())) {
																groupAndListMenuValidation.validate(screenSettings, result,
																		ApplicationConstants.DUPLICATEAPPSITE);
																break;
															}
														}
													}
													if (button.getMenuFucntionality().equals(String.valueOf(findFctnId))
															&& String.valueOf(findFctnId).equals(menuFunctionlity)) {
														if (null != screenSettings.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
															if (button.getBtnLinkId().equals(screenSettings.getBtnLinkId())) {
																if(button.getChkSubCate().equals(screenSettings.getChkSubCate())) {
																	if (button.getBtnLinkId().contains(",")) {
																		iconinMenuValidator.validate(screenSettings, result,
																				ApplicationConstants.DUPLICATECATEGORIES);
																	} else {
																		iconinMenuValidator.validate(screenSettings, result,
																				ApplicationConstants.DUPLICATECATEGORY);
																	}
																	break;
																}
															}
														}
													}

													if (button.getMenuFucntionality().equals(String.valueOf(eventFctnId))
															&& String.valueOf(eventFctnId).equals(menuFunctionlity)) {
														if (null != screenSettings.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
															if (button.getBtnLinkId().equals(screenSettings.getBtnLinkId())) {
																if (button.getBtnLinkId().contains(",")) {
																	iconinMenuValidator.validate(screenSettings, result,
																			ApplicationConstants.DUPLICATECATEGORIES);
																} else {
																	iconinMenuValidator.validate(screenSettings, result,
																			ApplicationConstants.DUPLICATECATEGORY);
																}

																break;
															}
														}

													}
													
													if (button.getMenuFucntionality().equals(String.valueOf(fundraFctnId))
															&& String.valueOf(fundraFctnId).equals(menuFunctionlity)) {
														if (null != screenSettings.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
															if (button.getBtnLinkId().equals(screenSettings.getBtnLinkId())) {
																if (button.getBtnLinkId().contains(",")) {
																	iconinMenuValidator.validate(screenSettings, result,
																			ApplicationConstants.DUPLICATECATEGORIES);
																} else {
																	iconinMenuValidator.validate(screenSettings, result,
																			ApplicationConstants.DUPLICATECATEGORY);
																}

																break;
															}
														}

													}


													if (button.getMenuFucntionality().equals(screenSettings.getMenuFucntionality())
															&& !button.getMenuFucntionality().equals(String.valueOf(subMenuFctnId))
															&& !button.getMenuFucntionality().equals(String.valueOf(anythingPageFctnId))
															&& !button.getMenuFucntionality().equals(String.valueOf(appSiteFctnId))
															&& !button.getMenuFucntionality().equals(String.valueOf(findFctnId))
															&& !button.getMenuFucntionality().equals(String.valueOf(eventFctnId))
															&&!button.getMenuFucntionality().equals(String.valueOf(fundraFctnId))
															) {

														groupAndListMenuValidation.validate(screenSettings, result,
																ApplicationConstants.DUPLICATEFUNCTINALITY);
														break;
													}

												}

											}
											if (result.hasErrors()) {
												screenSettings.setBtnLinkId(null);
												return viewName;
											}
											grpMenuItem = new ScreenSettings();
											grpMenuItem.setMenuBtnName(screenSettings.getMenuBtnName().trim());
											grpMenuItem.setMenuFucntionality(screenSettings.getMenuFucntionality());
											grpMenuItem.setHiddenmenuFnctn(screenSettings.getMenuFucntionality());
											grpMenuItem.setMenuIconId(ApplicationConstants.BUTTONID + screenSettings.getMenuBtnName().trim());
											grpMenuItem.setBtnLinkId(screenSettings.getBtnLinkId());
											grpMenuItem.setHiddenBtnLinkId(screenSettings.getBtnLinkId());
											grpMenuItem.setBtnGroup(selGrpName);
											grpMenuItem.setHiddenbtnGroup(selGrpName.replace(" ", ""));
											grpMenuItem.setPageTitle(groupHeader);
											grpMenuItem.setComboBtnTypeId(screenSettings.getComboBtnTypeId());
											grpMenuItem.setComboBtnType(comboBtnType);
											grpMenuItem.setSubCatIds(screenSettings.getSubCatIds());
											grpMenuItem.setHiddenSubCate(screenSettings.getChkSubCate());
											grpMenuItem.setChkSubCate(screenSettings.getChkSubCate());
											grpMenuItem.setCitiId(screenSettings.getCitiId());
											
											if (!"Rectangle".equals(comboBtnType)) {
												if ("Circle".equals(comboBtnType)) {
													grpMenuItem.setLogoImageName(screenSettings.getLogoImageName());
													grpMenuItem.setImagePath(ApplicationConstants.TEMPIMGPATH + screenSettings.getLogoImageName());
												} else {
													grpMenuItem.setLogoImageName(screenSettings.getBannerImageName());
													grpMenuItem.setImagePath(ApplicationConstants.TEMPIMGPATH + screenSettings.getBannerImageName());
												}
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

											btnList.remove(j);
											btnList.add(j, grpMenuItem);
											break;
										}
									}
									group.setGrpBtnsList(btnList);
									comboBtnList.remove(i);
									comboBtnList.add(i, group);
									break;
								}
							}
						} else {
							List<ScreenSettings> btnList = null;
							// Below code will be executed when user changes
							// button group as well as button details
							for (int i = 0; i < comboBtnList.size(); i++) {
								GroupTemplate group = comboBtnList.get(i);
								if (group.getGrpName().equals(selGrpName)) {
									btnList = group.getGrpBtnsList();
									for (ScreenSettings button : btnList) {
										if (button.getMenuBtnName().equals(buttonName)) {
											groupAndListMenuValidation.validate(screenSettings, result, ApplicationConstants.DUPLICATEBUTTONNAME);
											break;
										}

										if (button.getMenuFucntionality().equals(String.valueOf(subMenuFctnId))
												&& String.valueOf(subMenuFctnId).equals(menuFunctionlity)) {
											if (null != screenSettings.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
												if (button.getBtnLinkId().equals(screenSettings.getBtnLinkId())) {
													groupAndListMenuValidation
															.validate(screenSettings, result, ApplicationConstants.DUPLICATESUBMENU);
													break;
												}
											}
										}
										if (button.getMenuFucntionality().equals(String.valueOf(anythingPageFctnId))
												&& String.valueOf(anythingPageFctnId).equals(menuFunctionlity)) {
											if (null != screenSettings.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
												if (null != button.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
													if (button.getBtnLinkId().equals(screenSettings.getBtnLinkId())) {
														groupAndListMenuValidation.validate(screenSettings, result,
																ApplicationConstants.DUPLICATEANYTHINGPAGE);
														break;
													}
												}
											}
										}
										if (button.getMenuFucntionality().equals(String.valueOf(appSiteFctnId))
												&& String.valueOf(appSiteFctnId).equals(menuFunctionlity)) {
											if (null != screenSettings.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
												if (null != button.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
													if (button.getBtnLinkId().equals(screenSettings.getBtnLinkId())) {
														groupAndListMenuValidation.validate(screenSettings, result,
																ApplicationConstants.DUPLICATEAPPSITE);
														break;
													}
												}
											}

										}
										if (button.getMenuFucntionality().equals(String.valueOf(findFctnId))
												&& String.valueOf(findFctnId).equals(menuFunctionlity)) {
											if (null != screenSettings.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
												if (button.getBtnLinkId().equals(screenSettings.getBtnLinkId())) {
													if(button.getChkSubCate().equals(screenSettings.getChkSubCate())) {
														if (button.getBtnLinkId().contains(",")) {
															iconinMenuValidator
																	.validate(screenSettings, result, ApplicationConstants.DUPLICATECATEGORIES);
														} else {
															iconinMenuValidator.validate(screenSettings, result, ApplicationConstants.DUPLICATECATEGORY);
														}
														break;
													}
												}
											}
										}
										if (button.getMenuFucntionality().equals(String.valueOf(eventFctnId))
												&& String.valueOf(eventFctnId).equals(menuFunctionlity)) {
											if (null != screenSettings.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
												if (button.getBtnLinkId().equals(screenSettings.getBtnLinkId())) {
													if (button.getBtnLinkId().contains(",")) {
														iconinMenuValidator
																.validate(screenSettings, result, ApplicationConstants.DUPLICATECATEGORIES);
													} else {
														iconinMenuValidator.validate(screenSettings, result, ApplicationConstants.DUPLICATECATEGORY);
													}

													break;
												}
											}

										}
										
										
										if (button.getMenuFucntionality().equals(String.valueOf(fundraFctnId))
												&& String.valueOf(fundraFctnId).equals(menuFunctionlity)) {
											if (null != screenSettings.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
												if (button.getBtnLinkId().equals(screenSettings.getBtnLinkId())) {
													if (button.getBtnLinkId().contains(",")) {
														iconinMenuValidator
																.validate(screenSettings, result, ApplicationConstants.DUPLICATECATEGORIES);
													} else {
														iconinMenuValidator.validate(screenSettings, result, ApplicationConstants.DUPLICATECATEGORY);
													}

													break;
												}
											}

										}								
										
										
										if (button.getMenuFucntionality().equals(screenSettings.getMenuFucntionality())
												&& !button.getMenuFucntionality().equals(String.valueOf(subMenuFctnId))
												&& !button.getMenuFucntionality().equals(String.valueOf(anythingPageFctnId))
												&& !button.getMenuFucntionality().equals(String.valueOf(appSiteFctnId))
												&& !button.getMenuFucntionality().equals(String.valueOf(findFctnId))
												&& !button.getMenuFucntionality().equals(String.valueOf(eventFctnId))
												&&!button.getMenuFucntionality().equals(String.valueOf(fundraFctnId))
												) {
											groupAndListMenuValidation.validate(screenSettings, result, ApplicationConstants.DUPLICATEFUNCTINALITY);
											break;
										}
									}
									if (result.hasErrors()) {
										screenSettings.setBtnLinkId(null);
										return viewName;
									}

									grpMenuItem = new ScreenSettings();
									grpMenuItem.setMenuBtnName(screenSettings.getMenuBtnName().trim());
									grpMenuItem.setMenuFucntionality(screenSettings.getMenuFucntionality());
									grpMenuItem.setHiddenmenuFnctn(screenSettings.getMenuFucntionality());
									grpMenuItem.setMenuIconId(ApplicationConstants.BUTTONID + screenSettings.getMenuBtnName().trim());
									grpMenuItem.setBtnLinkId(screenSettings.getBtnLinkId());
									grpMenuItem.setHiddenBtnLinkId(screenSettings.getBtnLinkId());
									grpMenuItem.setBtnGroup(selGrpName);
									grpMenuItem.setHiddenbtnGroup(selGrpName.replace(" ", ""));
									grpMenuItem.setPageTitle(groupHeader);
									grpMenuItem.setComboBtnTypeId(screenSettings.getComboBtnTypeId());
									grpMenuItem.setComboBtnType(comboBtnType);
									grpMenuItem.setSubCatIds(screenSettings.getSubCatIds());
									grpMenuItem.setHiddenSubCate(screenSettings.getChkSubCate());
									grpMenuItem.setChkSubCate(screenSettings.getChkSubCate());
									grpMenuItem.setCitiId(screenSettings.getCitiId());
									
									if (!"Rectangle".equals(comboBtnType)) {
										if ("Circle".equals(comboBtnType)) {
											grpMenuItem.setLogoImageName(screenSettings.getLogoImageName());
											grpMenuItem.setImagePath(ApplicationConstants.TEMPIMGPATH + screenSettings.getLogoImageName());
										} else {
											grpMenuItem.setLogoImageName(screenSettings.getBannerImageName());
											grpMenuItem.setImagePath(ApplicationConstants.TEMPIMGPATH + screenSettings.getBannerImageName());
										}
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

									btnList.add(grpMenuItem);

									group.setGrpBtnsList(btnList);
									comboBtnList.remove(i);
									comboBtnList.add(i, group);
									break;
								}
								if (i == comboBtnList.size() - 1) {
									newGrpFlag = true;
									grpBtnsList = new ArrayList<ScreenSettings>();
									groupTemplate = new GroupTemplate();

									groupTemplate.setGrpName(selGrpName);

									grpMenuItem = new ScreenSettings();
									grpMenuItem.setMenuBtnName(screenSettings.getMenuBtnName().trim());
									grpMenuItem.setMenuFucntionality(screenSettings.getMenuFucntionality());
									grpMenuItem.setHiddenmenuFnctn(screenSettings.getMenuFucntionality());
									grpMenuItem.setMenuIconId(ApplicationConstants.BUTTONID + screenSettings.getMenuBtnName().trim());
									grpMenuItem.setBtnLinkId(screenSettings.getBtnLinkId());
									grpMenuItem.setHiddenBtnLinkId(screenSettings.getBtnLinkId());
									grpMenuItem.setBtnGroup(selGrpName);
									grpMenuItem.setHiddenbtnGroup(selGrpName.replace(" ", ""));
									grpMenuItem.setPageTitle(groupHeader);
									grpMenuItem.setComboBtnType(comboBtnType);
									grpMenuItem.setComboBtnTypeId(screenSettings.getComboBtnTypeId());
									grpMenuItem.setSubCatIds(screenSettings.getSubCatIds());
									grpMenuItem.setHiddenSubCate(screenSettings.getChkSubCate());
									grpMenuItem.setChkSubCate(screenSettings.getChkSubCate());
									grpMenuItem.setCitiId(screenSettings.getCitiId());
									
									if (!"Rectangle".equals(comboBtnType)) {
										if ("Circle".equals(comboBtnType)) {
											grpMenuItem.setLogoImageName(screenSettings.getLogoImageName());
											grpMenuItem.setImagePath(ApplicationConstants.TEMPIMGPATH + screenSettings.getLogoImageName());
										} else {
											grpMenuItem.setLogoImageName(screenSettings.getBannerImageName());
											grpMenuItem.setImagePath(ApplicationConstants.TEMPIMGPATH + screenSettings.getBannerImageName());
										}

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
									comboBtnList.add(groupTemplate);

									session.setAttribute("comboBtnList", comboBtnList);
									break;
								}

							}
							// Remove button from old group
							for (int l = 0; l < comboBtnList.size(); l++) {
								GroupTemplate updateGroup = comboBtnList.get(l);
								if (updateGroup.getGrpName().equals(screenSettings.getOldGroupName())) {
									List<ScreenSettings> updatebtnList = updateGroup.getGrpBtnsList();
									for (int k = 0; k < updatebtnList.size(); k++) {
										ScreenSettings btn = updatebtnList.get(k);
										if (btn.getMenuIconId().equals(btnId)) {
											updatebtnList.remove(k);
											updateGroup.setGrpBtnsList(updatebtnList);
											comboBtnList.remove(l);
											comboBtnList.add(l, updateGroup);
											break;
										}
									}
									break;
								}
							}
						}
						session.setAttribute("comboBtnList", comboBtnList);
					} else {
						for (int i = 0; i < comboBtnList.size(); i++) {
							GroupTemplate group = comboBtnList.get(i);

							if (group.getGrpName().equals(selGrpName)) {
								List<ScreenSettings> btnList = group.getGrpBtnsList();

								for (ScreenSettings button : btnList) {
									if (button.getMenuBtnName().equals(buttonName)) {
										groupAndListMenuValidation.validate(screenSettings, result, ApplicationConstants.DUPLICATEBUTTONNAME);
										break;
									}
									if (button.getMenuFucntionality().equals(String.valueOf(subMenuFctnId))
											&& String.valueOf(subMenuFctnId).equals(menuFunctionlity)) {
										if (null != screenSettings.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
											if (button.getBtnLinkId().equals(screenSettings.getBtnLinkId())) {
												groupAndListMenuValidation.validate(screenSettings, result, ApplicationConstants.DUPLICATESUBMENU);
												break;
											}
										}
									}
									if (button.getMenuFucntionality().equals(String.valueOf(anythingPageFctnId))
											&& String.valueOf(anythingPageFctnId).equals(menuFunctionlity)) {
										if (null != screenSettings.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
											if (null != button.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
												if (button.getBtnLinkId().equals(screenSettings.getBtnLinkId())) {
													groupAndListMenuValidation.validate(screenSettings, result,
															ApplicationConstants.DUPLICATEANYTHINGPAGE);
													break;
												}
											}
										}
									}
									if (button.getMenuFucntionality().equals(String.valueOf(appSiteFctnId))
											&& String.valueOf(appSiteFctnId).equals(menuFunctionlity)) {
										if (null != screenSettings.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
											if (null != button.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
												if (button.getBtnLinkId().equals(screenSettings.getBtnLinkId())) {
													groupAndListMenuValidation
															.validate(screenSettings, result, ApplicationConstants.DUPLICATEAPPSITE);
													break;
												}
											}
										}
									}
									if (button.getMenuFucntionality().equals(String.valueOf(findFctnId))
											&& String.valueOf(findFctnId).equals(menuFunctionlity)) {
										if (null != screenSettings.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
											if (button.getBtnLinkId().equals(screenSettings.getBtnLinkId())) {
												if(button.getChkSubCate().equals(screenSettings.getChkSubCate())) {
													if (button.getBtnLinkId().contains(",")) {
														iconinMenuValidator.validate(screenSettings, result, ApplicationConstants.DUPLICATECATEGORIES);
													} else {
														iconinMenuValidator.validate(screenSettings, result, ApplicationConstants.DUPLICATECATEGORY);
													}
	
													break;
												}
											}
										}

									}

									if (button.getMenuFucntionality().equals(String.valueOf(eventFctnId))
											&& String.valueOf(eventFctnId).equals(menuFunctionlity)) {
										if (null != screenSettings.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
											if (button.getBtnLinkId().equals(screenSettings.getBtnLinkId())) {
												if (button.getBtnLinkId().contains(",")) {
													iconinMenuValidator.validate(screenSettings, result, ApplicationConstants.DUPLICATECATEGORIES);
												} else {
													iconinMenuValidator.validate(screenSettings, result, ApplicationConstants.DUPLICATECATEGORY);
												}

												break;
											}
										}

									}
									
									if (button.getMenuFucntionality().equals(String.valueOf(fundraFctnId))
											&& String.valueOf(fundraFctnId).equals(menuFunctionlity)) {
										if (null != screenSettings.getBtnLinkId() && !"".equals(screenSettings.getBtnLinkId())) {
											if (button.getBtnLinkId().equals(screenSettings.getBtnLinkId())) {
												if (button.getBtnLinkId().contains(",")) {
													iconinMenuValidator.validate(screenSettings, result, ApplicationConstants.DUPLICATECATEGORIES);
												} else {
													iconinMenuValidator.validate(screenSettings, result, ApplicationConstants.DUPLICATECATEGORY);
												}

												break;
											}
										}

									}
									
									
									
									if (button.getMenuFucntionality().equals(screenSettings.getMenuFucntionality())
											&& !button.getMenuFucntionality().equals(String.valueOf(subMenuFctnId))
											&& !button.getMenuFucntionality().equals(String.valueOf(anythingPageFctnId))
											&& !button.getMenuFucntionality().equals(String.valueOf(appSiteFctnId))
											&& !button.getMenuFucntionality().equals(String.valueOf(findFctnId))
											&& !button.getMenuFucntionality().equals(String.valueOf(eventFctnId))
											&&!button.getMenuFucntionality().equals(String.valueOf(fundraFctnId))
											) {

										groupAndListMenuValidation.validate(screenSettings, result, ApplicationConstants.DUPLICATEFUNCTINALITY);
										break;
									}

								}

								if (result.hasErrors()) {
									return viewName;
								}

								grpMenuItem = new ScreenSettings();
								grpMenuItem.setMenuBtnName(screenSettings.getMenuBtnName().trim());
								grpMenuItem.setMenuFucntionality(screenSettings.getMenuFucntionality());
								grpMenuItem.setHiddenmenuFnctn(screenSettings.getMenuFucntionality());
								grpMenuItem.setMenuIconId(ApplicationConstants.BUTTONID + screenSettings.getMenuBtnName().trim());
								grpMenuItem.setBtnLinkId(screenSettings.getBtnLinkId());
								grpMenuItem.setHiddenBtnLinkId(screenSettings.getBtnLinkId());
								grpMenuItem.setBtnGroup(selGrpName);
								grpMenuItem.setHiddenbtnGroup(selGrpName.replace(" ", ""));
								grpMenuItem.setPageTitle(groupHeader);
								grpMenuItem.setComboBtnTypeId(screenSettings.getComboBtnTypeId());
								grpMenuItem.setComboBtnType(comboBtnType);
								grpMenuItem.setSubCatIds(screenSettings.getSubCatIds());
								grpMenuItem.setHiddenSubCate(screenSettings.getChkSubCate());
								grpMenuItem.setChkSubCate(screenSettings.getChkSubCate());
								grpMenuItem.setCitiId(screenSettings.getCitiId());
								
								if (!"Rectangle".equals(comboBtnType)) {
									if ("Circle".equals(comboBtnType)) {
										grpMenuItem.setLogoImageName(screenSettings.getLogoImageName());
										grpMenuItem.setImagePath(ApplicationConstants.TEMPIMGPATH + screenSettings.getLogoImageName());
									} else {
										grpMenuItem.setLogoImageName(screenSettings.getBannerImageName());
										grpMenuItem.setImagePath(ApplicationConstants.TEMPIMGPATH + screenSettings.getBannerImageName());
									}
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

								btnList.add(grpMenuItem);
								group.setGrpBtnsList(btnList);
								comboBtnList.remove(i);
								comboBtnList.add(i, group);
								break;
							} else {
								// Code for creating new group
								if (i == comboBtnList.size() - 1) {
									grpBtnsList = new ArrayList<ScreenSettings>();
									groupTemplate = new GroupTemplate();

									groupTemplate.setGrpName(selGrpName);

									grpMenuItem = new ScreenSettings();
									grpMenuItem.setMenuBtnName(screenSettings.getMenuBtnName().trim());
									grpMenuItem.setMenuFucntionality(screenSettings.getMenuFucntionality());
									grpMenuItem.setHiddenmenuFnctn(screenSettings.getMenuFucntionality());
									grpMenuItem.setMenuIconId(ApplicationConstants.BUTTONID + screenSettings.getMenuBtnName().trim());
									grpMenuItem.setBtnLinkId(screenSettings.getBtnLinkId());
									grpMenuItem.setHiddenBtnLinkId(screenSettings.getBtnLinkId());
									grpMenuItem.setBtnGroup(selGrpName);
									grpMenuItem.setHiddenbtnGroup(selGrpName.replace(" ", ""));
									grpMenuItem.setPageTitle(groupHeader);
									grpMenuItem.setComboBtnTypeId(screenSettings.getComboBtnTypeId());
									grpMenuItem.setComboBtnType(comboBtnType);
									grpMenuItem.setSubCatIds(screenSettings.getSubCatIds());
									grpMenuItem.setHiddenSubCate(screenSettings.getChkSubCate());
									grpMenuItem.setChkSubCate(screenSettings.getChkSubCate());
									grpMenuItem.setCitiId(screenSettings.getCitiId());
									
									if (!"Rectangle".equals(comboBtnType)) {
										if ("Circle".equals(comboBtnType)) {
											grpMenuItem.setLogoImageName(screenSettings.getLogoImageName());
											grpMenuItem.setImagePath(ApplicationConstants.TEMPIMGPATH + screenSettings.getLogoImageName());
										} else {
											grpMenuItem.setLogoImageName(screenSettings.getBannerImageName());
											grpMenuItem.setImagePath(ApplicationConstants.TEMPIMGPATH + screenSettings.getBannerImageName());
										}
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
									comboBtnList.add(groupTemplate);
									session.setAttribute("comboBtnList", comboBtnList);
									break;
								}
							}
						}
					}
					session.setAttribute("comboBtnList", comboBtnList);
				}
			}
		} else
			if (null != addorDeleteMenuItem && "DeleteButton".equals(addorDeleteMenuItem)) {
				request.setAttribute("gprdMenuAction", "Button");
				for (int i = 0; i < comboBtnList.size(); i++) {
					GroupTemplate group = comboBtnList.get(i);
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
						comboBtnList.remove(i);
						comboBtnList.add(i, group);
						break;
					}
				}
				session.setAttribute("comboBtnList", comboBtnList);

			} else
				if (null != addorDeleteMenuItem && "UpdateGroup".equals(addorDeleteMenuItem)) {
					request.setAttribute("gprdMenuAction", "Group");
					groupAndListMenuValidation.validate(screenSettings, result, ApplicationConstants.GROUPNAME);

					if (result.hasErrors()) {
						return viewName;
					}
					if (null != btnId && !"".equals(btnId)) {
						for (int i = 0; i < comboBtnList.size(); i++) {
							GroupTemplate group = comboBtnList.get(i);
							if (group.getGrpName().equals(btnId.split("-")[1])) {

								for (int j = 0; j < comboBtnList.size(); j++) {

									if (j != i) {

										GroupTemplate groupName = comboBtnList.get(j);

										if (groupName.getGrpName().equals(grpName)) {

											groupAndListMenuValidation.validate(screenSettings, result, ApplicationConstants.DUPLICATEGROUP);
											break;
										}

									}

								}
								if (result.hasErrors()) {
									screenSettings.setBtnLinkId(null);
									return viewName;
								}

								group.setGrpName(grpName);
								comboBtnList.remove(i);
								comboBtnList.add(i, group);

								List<String> comboListUpdate = (ArrayList<String>) session.getAttribute("comboList");

								for (int k = 0; k <= comboListUpdate.size(); k++) {

									if (comboListUpdate.get(k).equals(btnId.split("-")[1])) {
										comboListUpdate.remove(k);
										comboListUpdate.add(k, grpName);
										session.setAttribute("comboList", comboListUpdate);

										if (!btnOrderList.isEmpty()) {
											for (int p = 0; p < btnOrderList.size(); p++) {

												if (btnOrderList.get(p).equals(btnId)) {
													btnOrderList.set(p, ApplicationConstants.TEXT + "-" + grpName);
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
						session.setAttribute("comboBtnList", comboBtnList);
					}

				} else
					if (null != addorDeleteMenuItem && "DeleteGroup".equals(addorDeleteMenuItem)) {
						request.setAttribute("gprdMenuAction", "Group");
						groupAndListMenuValidation.validate(screenSettings, result, ApplicationConstants.GROUPNAME);

						if (result.hasErrors()) {
							return viewName;
						}
						if (null != btnId && !"".equals(btnId)) {
							for (int i = 0; i < comboBtnList.size(); i++) {
								GroupTemplate group = comboBtnList.get(i);
								if (group.getGrpName().equals(btnId.split("-")[1])) {
									comboBtnList.remove(i);
									break;
								}

							}
							List<String> comboListUpdate = (ArrayList<String>) session.getAttribute("comboList");
							for (int k = 0; k <= comboListUpdate.size(); k++) {

								if (comboListUpdate.get(k).equals(btnId.split("-")[1])) {
									comboListUpdate.remove(k);
									session.setAttribute("comboList", comboListUpdate);
									break;
								}
							}

							session.setAttribute("comboBtnList", comboBtnList);

						}

					}

		if (null != comboBtnList) {
			previewMenuItems = new ArrayList<ScreenSettings>();
			for (int i = 0; i < comboBtnList.size(); i++) {
				GroupTemplate group = comboBtnList.get(i);
				grpBtnsList = group.getGrpBtnsList();
				if (null != grpBtnsList && !grpBtnsList.isEmpty()) {
					menuItem = new ScreenSettings();
					menuItem.setMenuBtnName(group.getGrpName());
					if (groupHeader.equals(ApplicationConstants.TEXT)) {
						menuItem.setMenuFucntionality(((Integer) session.getAttribute("textFctnId")).toString());
					} else {
						menuItem.setMenuFucntionality(((Integer) session.getAttribute("labelFctnId")).toString());
					}
					menuItem.setMenuIconId(ApplicationConstants.TEXT + "-" + group.getGrpName());
					menuItem.setComboBtnType(null);
					menuItem.setComboBtnTypeId(null);
					menuItem.setPageTitle(groupHeader);
					previewMenuItems.add(menuItem);
					for (ScreenSettings btn : grpBtnsList) {
						menuItem = new ScreenSettings();

						menuItem.setMenuBtnName(btn.getMenuBtnName());
						menuItem.setMenuIconId(btn.getMenuIconId());
						menuItem.setMenuFucntionality(btn.getMenuFucntionality());
						menuItem.setBtnLinkId(btn.getBtnLinkId());
						menuItem.setBtnDept(btn.getBtnDept());
						menuItem.setBtnType(btn.getBtnType());
						menuItem.setBtnGroup(group.getGrpName());
						menuItem.setHiddenbtnGroup(group.getGrpName().replace(" ", ""));
						menuItem.setPageTitle(groupHeader);
						menuItem.setComboBtnType(btn.getComboBtnType());
						menuItem.setComboBtnTypeId(btn.getComboBtnTypeId());
						menuItem.setSubCatIds(btn.getSubCatIds());
						menuItem.setHiddenSubCate(btn.getChkSubCate());
						menuItem.setChkSubCate(btn.getChkSubCate());
						menuItem.setCitiId(btn.getCitiId());
						
						if (!"Rectangle".equals(btn.getComboBtnType())) {
							menuItem.setLogoImageName(btn.getLogoImageName());
							menuItem.setImagePath(ApplicationConstants.TEMPIMGPATH + btn.getLogoImageName());
						}
						previewMenuItems.add(menuItem);
					}
				}
			}
			session.setAttribute("previewMenuItems", previewMenuItems);
		}

		request.setAttribute("gprdMenuAction", "Button");
		session.setAttribute("setupcombomenuImgSquare", ApplicationConstants.DEFAULTIMAGESQR);
		session.setAttribute("setupcombomenuImgCircle", ApplicationConstants.DEFAULTIMAGE);
		screenSettings = new ScreenSettings();
		screenSettings.setMenuId(menuId);
		screenSettings.setMenuLevel(menuLevel);
		screenSettings.setSubMenuName(subMenuName);
		screenSettings.setIsmenuFilterTypeSelected(true);
		screenSettings.setMenuFilterType(menuFilterType);
		screenSettings.setEditFilter(false);
		screenSettings.setPageTitle(groupHeader);
		model.put("screenSettingsForm", screenSettings);
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "setupcombomenu";
	}

	/**
	 * Controller Method for saving iconic template
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/savecombotemplate", method = RequestMethod.POST)
	@ResponseBody
	public final String saveCombotemplate(@RequestParam(value = "menuId", required = true) String menuId,
			@RequestParam(value = "menuLevel", required = true) Integer menuLevel,
			@RequestParam(value = "subMenuName", required = true) String subMenuName,
			@RequestParam(value = "bottmBtnId", required = true) String bottmBtnId,
			@RequestParam(value = "template", required = true) String templateType,
			@RequestParam(value = "typeFilter", required = true) boolean tyepFilter,
			@RequestParam(value = "deptFilter", required = true) boolean deptFilter,
			@RequestParam(value = "btnOrder", required = true) String btnOrder,
			@RequestParam(value = "grpBtnType", required = true) String grpBtnType,
			@RequestParam(value = "grpBtnTypeId", required = true) String grpBtnTypeId,
			@RequestParam(value = "pageTitle", required = true) String pageTitle, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws HubCitiServiceException {

		String methodName = "saveCombotemplate";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String responseStr = null;
		String btnOrderArry[] = null;
		String grpBtnTypeArry[] = null;
		String grpBtnTypeIdArry[] = null;
		String loginUserType = (String) session.getAttribute("loginUserType");
		List<ScreenSettings> sortedMenuItems = new ArrayList<ScreenSettings>();
		try {
			final ServletContext servletContext = request.getSession().getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
			// List of Menu items

			String textFuctn = ((Integer) session.getAttribute("textFctnId")).toString();
			String labelFunctn = ((Integer) session.getAttribute("labelFctnId")).toString();

			List<ScreenSettings> previewMenuItems = (ArrayList<ScreenSettings>) session.getAttribute("previewMenuItems");
			User user = (User) session.getAttribute("loginUser");

			if (pageTitle.equals(ApplicationConstants.TEXT)) {
				for (int i = 0; i < previewMenuItems.size(); i++) {
					ScreenSettings btn = previewMenuItems.get(i);
					if (btn.getMenuIconId().equals(ApplicationConstants.TEXT + "-" + btn.getMenuBtnName())) {
						btn.setMenuFucntionality(textFuctn);
					}
				}
			} else {
				for (int i = 0; i < previewMenuItems.size(); i++) {
					ScreenSettings btn = previewMenuItems.get(i);
					if (btn.getMenuIconId().equals(ApplicationConstants.TEXT + "-" + btn.getMenuBtnName())) {
						btn.setMenuFucntionality(labelFunctn);
					}

				}
			}

			if (deptFilter) {
				for (int i = 0; i < previewMenuItems.size(); i++) {
					ScreenSettings btn = previewMenuItems.get(i);
					if ((!btn.getMenuFucntionality().equals(textFuctn) && !btn.getMenuFucntionality().equals(labelFunctn))
							&& null == btn.getBtnDept()) {
						responseStr = "Please associate Department for all the bottons to continue saving";
						return ASSOCIATEDEPT;
					}
				}
			}
			if (tyepFilter) {
				for (int i = 0; i < previewMenuItems.size(); i++) {
					ScreenSettings btn = previewMenuItems.get(i);
					if ((!btn.getMenuFucntionality().equals(textFuctn) && !btn.getMenuFucntionality().equals(labelFunctn))
							&& null == btn.getBtnType()) {
						responseStr = "Please associate Type for all the bottons to continue saving";
						return ASSOCIATETYPE;
					}
				}
			}
			
			if("RegionApp".equalsIgnoreCase(loginUserType)) {
				for (int i = 0; i < previewMenuItems.size(); i++) {
					ScreenSettings btn = previewMenuItems.get(i);
	
					if (!btn.getMenuFucntionality().equals(textFuctn) && !btn.getMenuFucntionality().equals(labelFunctn) && null == btn.getCitiId()) {
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

				if (!"".equals(Utility.checkNull(grpBtnType))) {
					grpBtnTypeArry = grpBtnType.split("~");
					grpBtnTypeIdArry = grpBtnTypeId.split("~");

					for (int i = 0; i < sortedMenuItems.size(); i++) {
						ScreenSettings btn = sortedMenuItems.get(i);
						btn.setComboBtnType(grpBtnTypeArry[i]);
						if (!"null".equals(grpBtnTypeIdArry[i])) {
							btn.setComboBtnTypeId(Integer.parseInt(grpBtnTypeIdArry[i]));
						} else {
							btn.setComboBtnTypeId(null);
						}
					}

					session.setAttribute("previewMenuItems", sortedMenuItems);

					for (int i = 0; i < sortedMenuItems.size(); i++) {
						ScreenSettings btn = sortedMenuItems.get(i);
						if (("Circle".equalsIgnoreCase(btn.getComboBtnType()) || "Square".equalsIgnoreCase(btn.getComboBtnType()))
								&& (null == btn.getLogoImageName() || "".equals(btn.getLogoImageName()))) {
							responseStr = "Please Upload Image for the button(s)";
							return GROUPBTNIMG;
						} else
							if ("Rectangle".equalsIgnoreCase(btn.getComboBtnType())) {
								sortedMenuItems.get(i).setLogoImageName(null);
							}
					}
				}

				responseStr = hubCitiService.saveUpdateIconicMenuTemplate(user, sortedMenuItems, Integer.valueOf(menuId), menuLevel, subMenuName,
						bottmBtnId, templateType, null, tyepFilter, deptFilter);
			} else {
				if (!"".equals(Utility.checkNull(grpBtnType))) {
					grpBtnTypeArry = grpBtnType.split("~");
					grpBtnTypeIdArry = grpBtnTypeId.split("~");

					for (int i = 0; i < previewMenuItems.size(); i++) {
						ScreenSettings btn = previewMenuItems.get(i);
						btn.setComboBtnType(grpBtnTypeArry[i]);
						if (!"null".equals(grpBtnTypeIdArry[i])) {
							btn.setComboBtnTypeId(Integer.parseInt(grpBtnTypeIdArry[i]));
						} else {
							btn.setComboBtnTypeId(null);
						}
					}

					session.setAttribute("previewMenuItems", previewMenuItems);

					for (int i = 0; i < previewMenuItems.size(); i++) {
						ScreenSettings btn = previewMenuItems.get(i);
						if (("Circle".equalsIgnoreCase(btn.getComboBtnType()) || "Square".equalsIgnoreCase(btn.getComboBtnType()))
								&& (null == btn.getLogoImageName() || "".equals(btn.getLogoImageName()))) {
							responseStr = "Please Upload Image for the button(s)";
							return GROUPBTNIMG;
						} else
							if ("Rectangle".equalsIgnoreCase(btn.getComboBtnType())) {
								previewMenuItems.get(i).setLogoImageName(null);
							}
					}
				}

				responseStr = hubCitiService.saveUpdateIconicMenuTemplate(user, previewMenuItems, Integer.valueOf(menuId), menuLevel, subMenuName,
						bottmBtnId, templateType, null, tyepFilter, deptFilter);
			}

		} catch (HubCitiServiceException e) {
			throw new HubCitiServiceException(e);
		}
		return responseStr;
	}

	public static List<AppSiteDetails> displayAppSites(HttpServletRequest request, HttpSession session, ModelMap model)
			throws HubCitiServiceException

	{
		final String strMethodName = "displayAppSites";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		List<AppSiteDetails> appSiteDetailsLst = null;
		int iHubCityId = 0;

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		User user = (User) session.getAttribute("loginUser");
		if (null != user && !"".equals(user)) {
			iHubCityId = user.getHubCitiID();
		}

		appSiteDetailsLst = hubCitiService.getAppSites(null, iHubCityId, null);
		session.setAttribute("appsitelst", appSiteDetailsLst);
		return appSiteDetailsLst;
	}

	@RequestMapping(value = "/setupmoduletabbars", method = RequestMethod.GET)
	public ModelAndView displayModuleTabBars(HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {

		final String methodName = "displayModuleTabBars";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<TabBarDetails> tabBarDetails = null;
		List<ScreenSettings> modulesList = null;

		try {

			final ServletContext servletContext = request.getSession().getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

			User user = (User) session.getAttribute("loginUser");
			Integer userId = user.gethCAdminUserID();
			Integer hubCitiId = user.getHubCitiID();

			modulesList = hubCitiService.displayModules(userId, hubCitiId);
			tabBarDetails = hubCitiService.displayModuleTabBars(userId, hubCitiId);

			session.setAttribute("modulesList", modulesList);
			session.setAttribute("tabBarlst", tabBarDetails);
			model.put("screenSettingsForm", new ScreenSettings());

		} catch (HubCitiServiceException e) {
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return new ModelAndView("moduleTabBars");
	}

	@RequestMapping(value = "/savemoduletabbars", method = RequestMethod.POST)
	public ModelAndView saveModuleTabBarDetails(@ModelAttribute("screenSettingsForm") ScreenSettings moduletabBarDetails, BindingResult result,
			HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {

		final String methodName = "displayModuleTabBars";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<TabBarDetails> tabBarDetails = null;
		String status = null;

		try {

			final ServletContext servletContext = request.getSession().getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

			User user = (User) session.getAttribute("loginUser");
			
			if(null != moduletabBarDetails.getBtnLinkId() && !moduletabBarDetails.getBtnLinkId().isEmpty())
			{
				status = hubCitiService.saveModuleTabBar(moduletabBarDetails, user);
			}
			else
			{
				status = hubCitiService.deleteModuleTabBar(moduletabBarDetails, user);
			}

			if (status.equalsIgnoreCase(ApplicationConstants.SUCCESS)) {
				tabBarDetails = hubCitiService.displayModuleTabBars(user.gethCAdminUserID(), user.getHubCitiID());

				session.setAttribute("tabBarlst", tabBarDetails);
				model.put("screenSettingsForm", new ScreenSettings());
			}

		} catch (HubCitiServiceException e) {
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return new ModelAndView("moduleTabBars");
	}
}
