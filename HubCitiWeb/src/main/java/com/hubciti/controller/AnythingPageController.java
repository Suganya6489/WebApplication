package com.hubciti.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.exception.HubCitiServiceException;
import com.hubciti.common.pojo.AnythingPages;
import com.hubciti.common.pojo.HubCitiImages;
import com.hubciti.common.pojo.ScreenSettings;
import com.hubciti.common.pojo.User;
import com.hubciti.common.tags.Pagination;
import com.hubciti.common.util.Utility;
import com.hubciti.service.HubCitiService;
import com.hubciti.validator.AnyThingScreenSettingsValidation;

/**
 * This class is a controller class for AnythingPageController.
 * 
 * @author Sangeetha
 */
@Controller
public class AnythingPageController {
	/**
	 * Getting the logger Instance.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(AnythingPageController.class);

	/**
	 * variable of type anyThingPgeSettingsValidation.
	 */
	AnyThingScreenSettingsValidation anyThingPgeSettingsValidation;

	/**
	 * Setter for anyThingPgeSettingsValidation.
	 * 
	 * @param anyThingPgeSettingsValidation
	 *            the anyThingPgeSettingsValidation to set
	 */
	@Autowired
	public void setAnyThingPgeSettingsValidation(AnyThingScreenSettingsValidation anyThingPgeSettingsValidation) {
		this.anyThingPgeSettingsValidation = anyThingPgeSettingsValidation;
	}

	/**
	 * This method will display user created anything pages if exists otherwise
	 * will take you to build anything page screen.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/displayanythingpages.htm", method = RequestMethod.GET)
	public final String displayAnythingPages(@ModelAttribute("screenSettingsForm") ScreenSettings anythingPageDetails, BindingResult result,
			HttpServletRequest request, HttpSession session, ModelMap model) throws HubCitiServiceException {
		final String methodName = "displayAnythingPages";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		session.removeAttribute("ImageIcon");

		final String searchKey = anythingPageDetails.getSearchKey();
		final String pageFlag = request.getParameter("pageFlag");
		Integer lowerLimit = 0;
		String pageNumber = "0";
		int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;
		AnythingPages anythingPages = null;

		final User loginUser = (User) session.getAttribute(ApplicationConstants.LOGINUSER);
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.ANYTHINGPAGESCREEN);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean(ApplicationConstants.HUBCITISERVICE);

		if (null != pageFlag && "true".equals(pageFlag)) {
			pageNumber = request.getParameter("pageNumber");
			final Pagination pageSess = (Pagination) session.getAttribute(ApplicationConstants.PAGINATION);
			if (Integer.valueOf(pageNumber) != 0) {
				currentPage = Integer.valueOf(pageNumber);
				final int number = Integer.valueOf(currentPage) - 1;
				final int pageSize = pageSess.getPageRange();
				lowerLimit = pageSize * Integer.valueOf(number);
			}
		}

		anythingPages = hubCitiService.displayAnythingPage(loginUser, searchKey, lowerLimit);

		if (null != anythingPages) {
			if (anythingPages.getPageDetails().isEmpty() && (null == searchKey || "".equals(searchKey))) {
				model.put("screenSettingsForm", anythingPageDetails);

				LOG.info(ApplicationConstants.METHODEND + methodName);
				return "buildAnythingPage";
			} else
				if (anythingPages.getPageDetails().isEmpty()) {
					request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "No AnyThing Pages Found");
					request.setAttribute(ApplicationConstants.RESPONSESTATUS, "INFO");

					session.removeAttribute("anythingPageList");
					session.removeAttribute(ApplicationConstants.PAGINATION);
				} else {
					session.setAttribute("anythingPageList", anythingPages.getPageDetails());
					if (null != anythingPages.getTotalSize()) {
						objPage = Utility.getPagination(anythingPages.getTotalSize(), currentPage, "displayanythingpages.htm", recordCount);
						session.setAttribute(ApplicationConstants.PAGINATION, objPage);
					}
				}
		}

		request.setAttribute(ApplicationConstants.LOWERLIMIT, lowerLimit);
		session.setAttribute(ApplicationConstants.SEARCHKEY, searchKey);
		model.put("screenSettingsForm", new ScreenSettings());

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "displayanythingpage";
	}

	/**
	 * This method will display build anything page screen.
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws ScanSeeServiceException
	 */
	@RequestMapping(value = "/buildanythingpage.htm", method = RequestMethod.GET)
	public final ModelAndView buildAnythingPage(@ModelAttribute("screenSettingsForm") ScreenSettings anythingPageDetails, BindingResult result,
			HttpServletRequest request, HttpSession session, ModelMap model) throws HubCitiServiceException {
		final String methodName = "buildAnythingPage";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.ANYTHINGPAGESCREEN);
		request.setAttribute(ApplicationConstants.LOWERLIMIT, anythingPageDetails.getLowerLimit());
		model.put("screenSettingsForm", anythingPageDetails);

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return new ModelAndView("buildAnythingPage");
	}

	/**
	 * This method will display add link to existing anything page screen.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/setupanythingscreen.htm", method = RequestMethod.GET)
	public String setupAnythingPageScreen(@ModelAttribute("screenSettingsForm") ScreenSettings anythingPageDetails, BindingResult result,
			HttpServletRequest request, HttpSession session, ModelMap model) throws HubCitiServiceException {
		final String methodName = "setupAnythingPageScreen";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		session.removeAttribute("uploadedFile");
		session.removeAttribute("ImageIcon");

		session.setAttribute("minCropHt", 50);
		session.setAttribute("minCropWd", 50);
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.ANYTHINGPAGESCREEN);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean(ApplicationConstants.HUBCITISERVICE);

		final List<HubCitiImages> arHcImageList = hubCitiService.getHubCitiImageIconsDisplay("AttachLink");
		final List<HubCitiImages> pageTypes = hubCitiService.getAnythingPageType();

		session.setAttribute("arHcImageList", arHcImageList);
		session.setAttribute("pageTypes", pageTypes);
		session.setAttribute("anythingScreenImage", ApplicationConstants.DEFAULTIMAGEPATH);
		request.setAttribute(ApplicationConstants.LOWERLIMIT, anythingPageDetails.getLowerLimit());
		model.put("screenSettingsForm", new ScreenSettings());

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "setupanythingpage";
	}

	/**
	 * This method will return edit link to existing Anything page screen.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/editanythingscreen.htm", method = RequestMethod.POST)
	public String editAnythingPageScreen(@ModelAttribute("screenSettingsForm") ScreenSettings anythingPageDetails, BindingResult result,
			HttpServletRequest request, HttpSession session, ModelMap model) throws HubCitiServiceException {
		final String methodName = "editAnythingPageScreen";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		session.removeAttribute("uploadedFile");
		session.removeAttribute("ImageIcon");

		session.setAttribute("minCropHt", 50);
		session.setAttribute("minCropWd", 50);
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.ANYTHINGPAGESCREEN);

		final User loginUser = (User) session.getAttribute(ApplicationConstants.LOGINUSER);
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean(ApplicationConstants.HUBCITISERVICE);

		final ScreenSettings anythingPage = hubCitiService.getAnythingPage(anythingPageDetails, loginUser);
		final List<HubCitiImages> arHcImageList = hubCitiService.getHubCitiImageIconsDisplay("AttachLink");
		final List<HubCitiImages> pageTypes = hubCitiService.getAnythingPageType();

		if (null != anythingPage.getImageIconID() && !"".equals(anythingPage.getImageIconID())) {
			session.setAttribute("iconSelection", "existing");
		} else {
			session.setAttribute("iconSelection", "own");
		}

		session.setAttribute("arHcImageList", arHcImageList);
		session.setAttribute("filePath", anythingPage.getMediaPath());
		session.setAttribute("fileName", anythingPage.getPathName());
		session.setAttribute("oldFileName", anythingPage.getPathName());
		session.setAttribute("pageTypes", pageTypes);
		if (null != anythingPage.getLogoImageName()) {
			session.setAttribute("editAnythingScreenImage", anythingPage.getLogoImageName());
		} else {
			session.setAttribute("editAnythingScreenImage", ApplicationConstants.DEFAULTIMAGEPATH);
		}

		request.setAttribute(ApplicationConstants.LOWERLIMIT, anythingPageDetails.getLowerLimit());
		session.setAttribute("anythingPage", anythingPage);
		session.setAttribute("anythingPageId", anythingPageDetails.getHiddenBtnLinkId());
		model.put("screenSettingsForm", new ScreenSettings());

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "editanythingpage";
	}

	/**
	 * This method saves link to existing Anything page details.
	 * 
	 * @param anythingPageDetails
	 * @param result
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/saveanythingscreen.htm", method = RequestMethod.POST)
	public ModelAndView saveAnythingScreenDetails(@ModelAttribute("screenSettingsForm") ScreenSettings anythingPageDetails, BindingResult result,
			HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "saveAnythingScreenDetails";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		session.removeAttribute("uploadedFile");
		session.removeAttribute("ImageIcon");
		request.setAttribute(ApplicationConstants.LOWERLIMIT, anythingPageDetails.getLowerLimit());

		boolean isValidURL = true;
		String strResponse = null;
		final String searchKey = null;
		final Integer lowerLimit = 0;
		final int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;
		AnythingPages anythingPages = null;

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean(ApplicationConstants.HUBCITISERVICE);
		final User loginUser = (User) session.getAttribute(ApplicationConstants.LOGINUSER);

		if (null != anythingPageDetails.getFilePaths().getOriginalFilename()) {
			if (anythingPageDetails.getFilePaths().getSize() <= 0) {
				if (!"".equalsIgnoreCase(anythingPageDetails.getPathName()) || anythingPageDetails.getPathName() != null) {
					session.setAttribute("uploadedFile", anythingPageDetails.getPathName());
				}
			} else {
				if ("Image".equalsIgnoreCase(anythingPageDetails.getPageType())) {
					final String orinalFilePath = anythingPageDetails.getFilePaths().getOriginalFilename();
					final String[] fileSplit = orinalFilePath.split("\\.");
					final String fileName = fileSplit[0];
					final String pathName = fileName + ".png";
					anythingPageDetails.setPathName(pathName);
				} else {
					anythingPageDetails.setPathName(anythingPageDetails.getFilePaths().getOriginalFilename());
				}
				session.setAttribute("uploadedFile", anythingPageDetails.getPathName());

			}
		}
		anyThingPgeSettingsValidation.validate(anythingPageDetails, result);
		isValidURL = Utility.validateURL(anythingPageDetails.getPageAttachLink());
		if (!isValidURL) {
			anyThingPgeSettingsValidation.validate(anythingPageDetails, result, ApplicationConstants.INVALIDURL);
		}

		if (result.hasErrors()) {
			if (null != anythingPageDetails.getFilePaths() && !anythingPageDetails.getFilePaths().isEmpty()) {
				final String fileSeparator = System.getProperty("file.separator");
				final String tempImgPath = Utility.getTempMediaPath(ApplicationConstants.TEMP).toString();
				final String fileTempPath = tempImgPath + fileSeparator + anythingPageDetails.getFilePaths().getOriginalFilename();
				Utility.writeFileData(anythingPageDetails.getFilePaths(), fileTempPath);
			}

			session.setAttribute("ImageIcon", anythingPageDetails.getImageIconID());
			return new ModelAndView("setupanythingpage");
		} else {
			strResponse = hubCitiService.saveAnyThingScreen(anythingPageDetails, loginUser);

			if (null != strResponse && strResponse.equals(ApplicationConstants.SUCCESS)) {
				request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "AnyThing Page Created Succesfully");
				request.setAttribute(ApplicationConstants.RESPONSESTATUS, strResponse);

				session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.ANYTHINGPAGESCREEN);

				anythingPages = hubCitiService.displayAnythingPage(loginUser, searchKey, lowerLimit);

				if (null != anythingPages) {
					session.setAttribute("anythingPageList", anythingPages.getPageDetails());
					if (null != anythingPages.getTotalSize()) {
						objPage = Utility.getPagination(anythingPages.getTotalSize(), currentPage, "displayanythingpages.htm", recordCount);
						session.setAttribute(ApplicationConstants.PAGINATION, objPage);
					}
				}

				request.setAttribute(ApplicationConstants.LOWERLIMIT, lowerLimit);
				session.setAttribute(ApplicationConstants.SEARCHKEY, searchKey);
				model.put("screenSettingsForm", new ScreenSettings());
				return new ModelAndView("displayanythingpage");
			} else {
				request.setAttribute(ApplicationConstants.RESPONSESTATUS, strResponse);
				request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, strResponse);
			}

		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return new ModelAndView("setupanythingpage");
	}

	/**
	 * This method updates link to existing Anything page details.
	 * 
	 * @param anythingPageDetails
	 * @param result
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/updateanythingscreen.htm", method = RequestMethod.POST)
	public String updateAnythingScreenDetails(@ModelAttribute("screenSettingsForm") ScreenSettings anythingPageDetails, BindingResult result,
			HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "updateAnythingScreenDetails";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		session.removeAttribute("uploadedFile");
		session.removeAttribute("ImageIcon");
		request.setAttribute(ApplicationConstants.LOWERLIMIT, anythingPageDetails.getLowerLimit());

		boolean isValidURL = true;
		String strResponse = null;
		final String searchKey = null;
		Integer lowerLimit = 0;
		int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;
		AnythingPages anythingPages = null;

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean(ApplicationConstants.HUBCITISERVICE);
		final User loginUser = (User) session.getAttribute(ApplicationConstants.LOGINUSER);

		if (!"Website".equalsIgnoreCase(anythingPageDetails.getPageType())) {
			if (null != anythingPageDetails.getFilePaths().getOriginalFilename()) {
				if (anythingPageDetails.getFilePaths().getSize() <= 0) {
					if (!"".equalsIgnoreCase(anythingPageDetails.getPathName()) || anythingPageDetails.getPathName() != null) {
						/*
						 * session.removeAttribute("fileName");
						 * session.removeAttribute("filePath");
						 */
						session.setAttribute("uploadedFile", anythingPageDetails.getPathName());
					}
				} else {
					/*
					 * session.removeAttribute("fileName");
					 * session.removeAttribute("filePath");
					 */
					if ("Image".equalsIgnoreCase(anythingPageDetails.getPageTypeHid())) {
						final String orinalFilePath = anythingPageDetails.getFilePaths().getOriginalFilename();
						final String[] fileSplit = orinalFilePath.split("\\.");
						final String fileName = fileSplit[0];
						final String pathName = fileName + ".png";
						anythingPageDetails.setPathName(pathName);
					} else {
						anythingPageDetails.setPathName(anythingPageDetails.getFilePaths().getOriginalFilename());
					}

					session.setAttribute("uploadedFile", anythingPageDetails.getPathName());
				}
			}
		}

		if (null == anythingPageDetails.getLogoImageName() || "".equalsIgnoreCase(anythingPageDetails.getLogoImageName())) {
			if (null != anythingPageDetails.getImageName()) {
				anythingPageDetails.setLogoImageName(anythingPageDetails.getImageName());
			}
		}

		if ("exstngIcon".equals(anythingPageDetails.getIconSelect())) {
			session.setAttribute("iconSelection", "existing");
		} else {
			session.setAttribute("iconSelection", "own");
		}

		anyThingPgeSettingsValidation.validate(anythingPageDetails, result);
		isValidURL = Utility.validateURL(anythingPageDetails.getPageAttachLink());
		if (!isValidURL) {
			anyThingPgeSettingsValidation.validate(anythingPageDetails, result, ApplicationConstants.INVALIDURL);
		}

		if (result.hasErrors()) {
			if (null != anythingPageDetails.getFilePaths() && !anythingPageDetails.getFilePaths().isEmpty()) {
				final String fileSeparator = System.getProperty("file.separator");
				final String tempImgPath = Utility.getTempMediaPath(ApplicationConstants.TEMP).toString();
				final String fileTempPath = tempImgPath + fileSeparator + anythingPageDetails.getFilePaths().getOriginalFilename();
				Utility.writeFileData(anythingPageDetails.getFilePaths(), fileTempPath);
			}

			session.setAttribute("ImageIcon", anythingPageDetails.getImageIconID());
			// session.setAttribute("anythingPage", anythingPageDetails);
			return "editanythingpage";
		} else {
			strResponse = hubCitiService.updateAnyThingScreen(anythingPageDetails, loginUser);
			if (null != strResponse && strResponse.equals(ApplicationConstants.SUCCESS)) {
				request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "AnyThing Page Updated Succesfully");
				request.setAttribute(ApplicationConstants.RESPONSESTATUS, strResponse);

				if (null != anythingPageDetails.getLowerLimit()) {
					lowerLimit = anythingPageDetails.getLowerLimit();
					currentPage = (lowerLimit + recordCount) / recordCount;
				}

				anythingPages = hubCitiService.displayAnythingPage(loginUser, searchKey, lowerLimit);

				if (anythingPages.getPageDetails().isEmpty() && lowerLimit > 0) {
					lowerLimit = lowerLimit - 20;
					currentPage = (lowerLimit + recordCount) / recordCount;
					anythingPages = hubCitiService.displayAnythingPage(loginUser, searchKey, lowerLimit);
				}

				if (null != anythingPages) {
					session.setAttribute("anythingPageList", anythingPages.getPageDetails());
					if (null != anythingPages.getTotalSize()) {
						objPage = Utility.getPagination(anythingPages.getTotalSize(), currentPage, "displayanythingpages.htm", recordCount);
						session.setAttribute(ApplicationConstants.PAGINATION, objPage);
					}
				}

				session.setAttribute(ApplicationConstants.SEARCHKEY, searchKey);
				model.put("screenSettingsForm", new ScreenSettings());

				return "displayanythingpage";
			} else {
				request.setAttribute(ApplicationConstants.RESPONSESTATUS, strResponse);
				request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, strResponse);
			}

		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "editanythingpage";

	}

	/**
	 * This method deletes Anything page.
	 * 
	 * @param anythingPageDetails
	 * @param result
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/deleteanythingpage.htm", method = RequestMethod.POST)
	public final String deleteAnythingPage(@ModelAttribute("screenSettingsForm") ScreenSettings anythingPageDetails, BindingResult result,
			HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "deleteAnythingPage";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		session.removeAttribute("uploadedFile");
		session.removeAttribute("ImageIcon");
		final String searchKey = null;
		Integer lowerLimit = 0;
		int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.ANYTHINGPAGESCREEN);
		String strResponse = null;
		AnythingPages anythingPages = null;

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean(ApplicationConstants.HUBCITISERVICE);
		final User loginUser = (User) session.getAttribute(ApplicationConstants.LOGINUSER);

		strResponse = hubCitiService.deleteAnyThingPage(anythingPageDetails.getHiddenBtnLinkId(), loginUser);

		if (null != strResponse && strResponse.equals(ApplicationConstants.SUCCESS)) {
			request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "AnyThing Page Deleted Succesfully");
			request.setAttribute(ApplicationConstants.RESPONSESTATUS, strResponse);

			if (null != anythingPageDetails.getLowerLimit()) {
				lowerLimit = anythingPageDetails.getLowerLimit();
				currentPage = (lowerLimit + recordCount) / recordCount;
			}

			anythingPages = hubCitiService.displayAnythingPage(loginUser, searchKey, lowerLimit);

			if (anythingPages.getPageDetails().isEmpty() && lowerLimit > 0) {
				lowerLimit = lowerLimit - 20;
				currentPage = (lowerLimit + recordCount) / recordCount;
				anythingPages = hubCitiService.displayAnythingPage(loginUser, searchKey, lowerLimit);
			}

			if (null != anythingPages) {
				session.setAttribute("anythingPageList", anythingPages.getPageDetails());
				if (null != anythingPages.getTotalSize()) {
					objPage = Utility.getPagination(anythingPages.getTotalSize(), currentPage, "displayanythingpages.htm", recordCount);
					session.setAttribute(ApplicationConstants.PAGINATION, objPage);
				}
			}

			request.setAttribute(ApplicationConstants.LOWERLIMIT, lowerLimit);
			session.setAttribute(ApplicationConstants.SEARCHKEY, searchKey);
			model.put("screenSettingsForm", new ScreenSettings());

		} else {
			request.setAttribute(ApplicationConstants.RESPONSESTATUS, strResponse);
			request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "Failed to Delete AnyThing Page");
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "displayanythingpage";

	}

	/**
	 * This method will return add make your own Anything page screen.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/makeyourownanythingpage.htm", method = RequestMethod.GET)
	public final String makeYourOwnAnythingPage(@ModelAttribute("screenSettingsForm") ScreenSettings anythingPageDetails, BindingResult result,
			HttpServletRequest request, HttpSession session, ModelMap model) throws HubCitiServiceException {
		final String methodName = "makeYourOwnAnythingPage";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		session.removeAttribute("uploadedFile");
		session.removeAttribute("ImageIcon");
		session.setAttribute("minCropHt", 70);
		session.setAttribute("minCropWd", 70);
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.ANYTHINGPAGESCREEN);

		session.setAttribute("makeAnythngImage", "images/upload_image.png");
		session.setAttribute("makeAnythngImagePreview", "images/upload_image.png");
		request.setAttribute(ApplicationConstants.LOWERLIMIT, anythingPageDetails.getLowerLimit());
		model.put("screenSettingsForm", new ScreenSettings());

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "makeYourOwnAnythingPage";
	}

	/**
	 * This method saves make yours own Anything page details.
	 * 
	 * @param anythingPageDetails
	 * @param result
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/savemakeyourownanythingpage.htm", method = RequestMethod.POST)
	public final ModelAndView saveMakeYourOwnAnythingPage(@ModelAttribute("screenSettingsForm") ScreenSettings anythingPageDetails,
			BindingResult result, HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "saveMakeYourOwnAnythingPage";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		session.removeAttribute("uploadedFile");
		session.removeAttribute("ImageIcon");
		request.setAttribute(ApplicationConstants.LOWERLIMIT, anythingPageDetails.getLowerLimit());

		String strResponse = null;
		final String searchKey = null;
		final Integer lowerLimit = 0;
		final int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;

		AnythingPages anythingPages = null;

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean(ApplicationConstants.HUBCITISERVICE);
		final User loginUser = (User) session.getAttribute(ApplicationConstants.LOGINUSER);
		
		/* To fix XSS issue*/
		anythingPageDetails.setPageTitle(Utility.getXssFreeString(anythingPageDetails.getPageTitle()));
		anythingPageDetails.setShortDescription(Utility.getXssFreeString(anythingPageDetails.getShortDescription()));
		anythingPageDetails.setLongDescription(Utility.getXssFreeString(anythingPageDetails.getLongDescription()));

		anyThingPgeSettingsValidation.validateMakeYourAnythingPage(anythingPageDetails, result);

		result = validateDates(anythingPageDetails, result);

		if (result.hasErrors()) {
			return new ModelAndView("makeYourOwnAnythingPage");
		} else {
			strResponse = hubCitiService.saveAnyThingScreen(anythingPageDetails, loginUser);

			if (null != strResponse && strResponse.equals(ApplicationConstants.SUCCESS)) {
				request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "AnyThing Page Created Succesfully");
				request.setAttribute(ApplicationConstants.RESPONSESTATUS, strResponse);

				anythingPages = hubCitiService.displayAnythingPage(loginUser, searchKey, lowerLimit);

				if (null != anythingPages) {
					session.setAttribute("anythingPageList", anythingPages.getPageDetails());
					if (null != anythingPages.getTotalSize()) {
						objPage = Utility.getPagination(anythingPages.getTotalSize(), currentPage, "displayanythingpages.htm", recordCount);
						session.setAttribute(ApplicationConstants.PAGINATION, objPage);
					}
				}

				request.setAttribute(ApplicationConstants.LOWERLIMIT, lowerLimit);
				session.setAttribute(ApplicationConstants.SEARCHKEY, searchKey);
				model.put("screenSettingsForm", new ScreenSettings());
				return new ModelAndView("displayanythingpage");
			} else {
				request.setAttribute(ApplicationConstants.RESPONSESTATUS, strResponse);
				request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, strResponse);
			}

		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return new ModelAndView("makeYourOwnAnythingPage");
	}

	/**
	 * This method will return edit make your own Anything page screen.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/editmakeyoursownap.htm", method = RequestMethod.POST)
	public String editMakeYourOwnAnythingPage(@ModelAttribute("screenSettingsForm") ScreenSettings anythingPageDetails, BindingResult result,
			HttpServletRequest request, HttpSession session, ModelMap model) throws HubCitiServiceException {
		final String methodName = "editMakeYourOwnAnythingPage";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		session.removeAttribute("uploadedFile");
		session.removeAttribute("ImageIcon");

		session.setAttribute("minCropHt", 70);
		session.setAttribute("minCropWd", 70);
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.ANYTHINGPAGESCREEN);

		final User loginUser = (User) session.getAttribute(ApplicationConstants.LOGINUSER);
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean(ApplicationConstants.HUBCITISERVICE);

		ScreenSettings anythingPage = hubCitiService.getAnythingPage(anythingPageDetails, loginUser);

		if (null != anythingPage.getLogoImageName()) {
			session.setAttribute("makeAnythngImage", anythingPage.getLogoImageName());
			session.setAttribute("makeAnythngImagePreview", anythingPage.getLogoImageName());
		} else {
			session.setAttribute("makeAnythngImage", "images/upload_image.png");
			session.setAttribute("makeAnythngImagePreview", "images/upload_image.png");
		}

		anythingPage.setLogoImageName(null);
		request.setAttribute(ApplicationConstants.LOWERLIMIT, anythingPageDetails.getLowerLimit());
		session.setAttribute("anythingPage", anythingPage);
		session.setAttribute("anythingPageId", anythingPageDetails.getHiddenBtnLinkId());
		model.put("screenSettingsForm", anythingPage);

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "edityourownanythingpage";
	}

	/**
	 * This method updates make your own Anything page details.
	 * 
	 * @param anythingPageDetails
	 * @param result
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/updateyourownanythingpage.htm", method = RequestMethod.POST)
	public String updateYourOwnAnythingPage(@ModelAttribute("screenSettingsForm") ScreenSettings anythingPageDetails, BindingResult result,
			HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "updateYourOwnAnythingPage";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		session.removeAttribute("uploadedFile");
		session.removeAttribute("ImageIcon");
		request.setAttribute(ApplicationConstants.LOWERLIMIT, anythingPageDetails.getLowerLimit());

		String compDate = null;
		final Date currentDate = new Date();
		final String searchKey = null;
		Integer lowerLimit = 0;
		int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;
		AnythingPages anythingPages = null;
		String strResponse = null;

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean(ApplicationConstants.HUBCITISERVICE);
		final User loginUser = (User) session.getAttribute(ApplicationConstants.LOGINUSER);

		if (null == anythingPageDetails.getLogoImageName() || "".equalsIgnoreCase(anythingPageDetails.getLogoImageName())) {
			if (null != anythingPageDetails.getImageName()) {
				anythingPageDetails.setLogoImageName(anythingPageDetails.getImageName());
			}
		}

		anyThingPgeSettingsValidation.validateMakeYourAnythingPage(anythingPageDetails, result);

		result = validateDates(anythingPageDetails, result);

		if (result.hasErrors()) {
			return "edityourownanythingpage";
		}

		if (!"".equals(Utility.checkNull(anythingPageDetails.getEndDate()))) {
			compDate = Utility.compareCurrentDate(anythingPageDetails.getEndDate(), currentDate);
			if (null != compDate) {
				anyThingPgeSettingsValidation.validateDates(anythingPageDetails, result, ApplicationConstants.DATEENDCURRENT);
			}
		}

		if (result.hasErrors()) {
			return "edityourownanythingpage";
		} else {
			strResponse = hubCitiService.updateAnyThingScreen(anythingPageDetails, loginUser);
			if (null != strResponse && strResponse.equals(ApplicationConstants.SUCCESS)) {
				request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "AnyThing Page Updated Succesfully");
				request.setAttribute(ApplicationConstants.RESPONSESTATUS, strResponse);

				if (null != anythingPageDetails.getLowerLimit()) {
					lowerLimit = anythingPageDetails.getLowerLimit();
					currentPage = (lowerLimit + recordCount) / recordCount;
				}

				anythingPages = hubCitiService.displayAnythingPage(loginUser, searchKey, lowerLimit);

				if (anythingPages.getPageDetails().isEmpty() && lowerLimit > 0) {
					lowerLimit = lowerLimit - 20;
					currentPage = (lowerLimit + recordCount) / recordCount;
					anythingPages = hubCitiService.displayAnythingPage(loginUser, searchKey, lowerLimit);
				}

				if (null != anythingPages) {
					session.setAttribute("anythingPageList", anythingPages.getPageDetails());
					if (null != anythingPages.getTotalSize()) {
						objPage = Utility.getPagination(anythingPages.getTotalSize(), currentPage, "displayanythingpages.htm", recordCount);
						session.setAttribute(ApplicationConstants.PAGINATION, objPage);
					}
				}

				session.setAttribute(ApplicationConstants.SEARCHKEY, searchKey);
				model.put("screenSettingsForm", new ScreenSettings());

				return "displayanythingpage";
			} else {
				request.setAttribute(ApplicationConstants.RESPONSESTATUS, strResponse);
				request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, strResponse);
			}

		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "edityourownanythingpage";

	}

	/**
	 * This Method is to validate start and end date.
	 * 
	 * @param anythingPageDetails
	 * @param result
	 * @return
	 * @throws HubCitiServiceException
	 */
	public final BindingResult validateDates(ScreenSettings anythingPageDetails, BindingResult result) throws HubCitiServiceException {
		final String methodName = "validateDates";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String compDate = null;
		final Date currentDate = new Date();
		Boolean validStartDate = false;
		Boolean validEndDate = false;

		if (!"".equals(Utility.checkNull(anythingPageDetails.getStartDate()))) {
			validStartDate = Utility.isValidDate(anythingPageDetails.getStartDate());

			if (validStartDate && (null == anythingPageDetails.getHiddenBtnLinkId() || "".equals(anythingPageDetails.getHiddenBtnLinkId()))) {
				compDate = Utility.compareCurrentDate(anythingPageDetails.getStartDate(), currentDate);
				if (null != compDate) {
					anyThingPgeSettingsValidation.validateDates(anythingPageDetails, result, ApplicationConstants.DATESTARTCURRENT);
				}
			} else
				if (!validStartDate) {
					anyThingPgeSettingsValidation.validateDates(anythingPageDetails, result, ApplicationConstants.VALIDSTARTDATE);
				}
		}
		if (!"".equals(Utility.checkNull(anythingPageDetails.getEndDate()))) {
			validEndDate = Utility.isValidDate(anythingPageDetails.getEndDate());
			validStartDate = Utility.isValidDate(anythingPageDetails.getStartDate());

			if (validEndDate) {
				if (validStartDate) {
					compDate = Utility.compareDate(anythingPageDetails.getStartDate(), anythingPageDetails.getEndDate());
					if (null != compDate) {
						anyThingPgeSettingsValidation.validateDates(anythingPageDetails, result, ApplicationConstants.DATEAFTER);
					}
				} else {
					compDate = Utility.compareCurrentDate(anythingPageDetails.getEndDate(), currentDate);
					if (null != compDate) {
						anyThingPgeSettingsValidation.validateDates(anythingPageDetails, result, ApplicationConstants.DATEENDCURRENT);
					}
				}
			} else {
				anyThingPgeSettingsValidation.validateDates(anythingPageDetails, result, ApplicationConstants.VALIDENDDATE);
			}
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return result;
	}

}
