package com.hubciti.controller;

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

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.exception.HubCitiServiceException;
import com.hubciti.common.pojo.ScreenSettings;
import com.hubciti.common.pojo.User;
import com.hubciti.common.util.Utility;
import com.hubciti.service.HubCitiService;
import com.hubciti.validator.AboutUsScreenSettingsValidation;
import com.hubciti.validator.LoginScreenSettingsValidator;
import com.hubciti.validator.PrivacyPolicyScreenSettingsValidation;
import com.hubciti.validator.RegistrationScreenSettingsValidator;
import com.hubciti.validator.SplashScreenSettingsValidator;

/**
 * This class is a controller class for Screen Settings.
 * 
 * @author dileep_cc
 */
@Controller
public class ScreenSettingsController {
	/**
	 * Getting the logger Instance.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ScreenSettingsController.class);

	/**
	 * variable of type LoginScreenSettingsValidator.
	 */
	LoginScreenSettingsValidator loginScreenDetailsValidator;
	/**
	 * variable of type RegistrationScreenSettingsValidator.
	 */
	RegistrationScreenSettingsValidator registrationScreenSettingsValidator;
	/**
	 * variable of type AboutUsScreenSettingsValidation.
	 */
	AboutUsScreenSettingsValidation aboutUsScreenSettingsValidation;
	/**
	 * variable of type PrivacyPolicyScreenSettingsValidation.
	 */
	PrivacyPolicyScreenSettingsValidation policyScreenSettingsValidation;
	/**
	 * variable of type SplashScreenSettingsValidator.
	 */
	SplashScreenSettingsValidator splashScreenSettingsValidator;

	/**
	 * @param splashScreenSettingsValidator
	 */
	@Autowired
	public void setSplashScreenSettingsValidator(SplashScreenSettingsValidator splashScreenSettingsValidator) {
		this.splashScreenSettingsValidator = splashScreenSettingsValidator;
	}

	/**
	 * @param loginScreenDetailsValidator
	 */
	@Autowired
	public void setLoginScreenDetailsValidator(LoginScreenSettingsValidator loginScreenDetailsValidator) {
		this.loginScreenDetailsValidator = loginScreenDetailsValidator;
	}

	/**
	 * @param registrationScreenSettingsValidator
	 */
	@Autowired
	public void setRegistrationScreenSettingsValidator(RegistrationScreenSettingsValidator registrationScreenSettingsValidator) {
		this.registrationScreenSettingsValidator = registrationScreenSettingsValidator;
	}

	/**
	 * @param aboutUsScreenSettingsValidation
	 */
	@Autowired
	public void setAboutUsScreenSettingsValidation(AboutUsScreenSettingsValidation aboutUsScreenSettingsValidation) {
		this.aboutUsScreenSettingsValidation = aboutUsScreenSettingsValidation;
	}

	/**
	 * @param policyScreenSettingsValidation
	 */
	@Autowired
	public void setPolicyScreenSettingsValidation(PrivacyPolicyScreenSettingsValidation policyScreenSettingsValidation) {
		this.policyScreenSettingsValidation = policyScreenSettingsValidation;
	}

	/**
	 * This method will return login setup screen.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/setuploginscreen.htm", method = RequestMethod.GET)
	public String setupLoginScreen(HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "setupLoginScreen";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		// Minimum crop height and width for login screen
		session.setAttribute("minCropHt", 140);
		session.setAttribute("minCropWd", 280);
		// Default images path
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.LOGINSCREEN);

		final User loginUser = (User) session.getAttribute("loginUser");
		ScreenSettings loginScreenDetails = null;
		loginUser.setPageType("Login Page");

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		loginScreenDetails = hubCitiService.fetchScreenSettings(loginUser);
		if (null == loginScreenDetails) {
			model.put("screenSettingsForm", new ScreenSettings());
			session.setAttribute("btnname", "Save Settings");
			session.setAttribute("loginScreenLogo", "images/upload_image.png");
			session.setAttribute("loginScreenLogoPreview", "images/dummy-logo.png");
			session.setAttribute("titleBarLogoPreview", "images/small-logo.png");
		} else {

			loginScreenDetails.setOldImageName(loginScreenDetails.getLogoImageName());
			model.put("screenSettingsForm", loginScreenDetails);
			session.setAttribute("btnname", "Update Settings");
			session.setAttribute("loginScreenLogo", loginScreenDetails.getLogoPath());
			session.setAttribute("loginScreenLogoPreview", loginScreenDetails.getLogoPath());
			session.setAttribute("titleBarLogoPreview", loginScreenDetails.getSmallLogoPath());
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "setuploginpage";
	}

	/**
	 * This method saves the login setup details.
	 * 
	 * @param loginScreenDetails
	 * @param result
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/setuploginscreen.htm", method = RequestMethod.POST)
	public String saveLoginSetupScreenDetails(@ModelAttribute("screenSettingsForm") ScreenSettings loginScreenDetails, BindingResult result,
			HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "saveLoginSetupScreenDetails";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		loginScreenDetailsValidator.validate(loginScreenDetails, result);
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		final User loginUser = (User) session.getAttribute("loginUser");
		String status = null;
		String reponse = null;
		if (result.hasErrors()) {
			return "setuploginpage";
		} else {
			
			status = hubCitiService.saveLoginScreenSettings(loginScreenDetails, loginUser);

			if (null != status && status.equals(ApplicationConstants.SUCCESS)) {

				reponse = "Settings Saved Succesfully";
				request.setAttribute("responeMsg", reponse);
				request.setAttribute("responseStatus", ApplicationConstants.SUCCESS);
				session.setAttribute("btnname", "Update Settings");
			} else {
				session.setAttribute("btnname", "Save Settings");
				reponse = "Error Occured While Saving Settings";
				request.setAttribute("responeMsg", reponse);
				request.setAttribute("responseStatus", ApplicationConstants.FAILURE);
			}

		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "setuploginpage";
	}

	/**
	 * This method returns registration screen.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/setupregscreen.htm", method = RequestMethod.GET)
	public String setupRegistrationScreen(HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "setupRegistrationScreen";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		// Default images path
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.REGISTRATION);

		final User loginUser = (User) session.getAttribute("loginUser");
		ScreenSettings registrationScreenDetails = null;
		loginUser.setPageType(ApplicationConstants.REGISTRATIONPAGE);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		registrationScreenDetails = hubCitiService.fetchScreenSettings(loginUser);
		if (null == registrationScreenDetails) {

			registrationScreenDetails = new ScreenSettings();
			registrationScreenDetails.setPageTitle(ApplicationConstants.REGISTRATIONSUCCESFULLTITLE);
			registrationScreenDetails.setPageContent(ApplicationConstants.REGISTRATIONSUCCESFULLTEXT);
			model.put("screenSettingsForm", registrationScreenDetails);
			session.setAttribute("btnname", "Save Settings");

		} else {

			registrationScreenDetails.setOldImageName(registrationScreenDetails.getLogoImageName());
			model.put("screenSettingsForm", registrationScreenDetails);
			session.setAttribute("btnname", "Update Settings");

		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "setupregscreen";
	}

	/**
	 * This method saves the registration details.
	 * 
	 * @param regScreenSettings
	 * @param result
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/setupregscreen.htm", method = RequestMethod.POST)
	public String saveRegScreenSettings(@ModelAttribute("screenSettingsForm") ScreenSettings regScreenSettings, BindingResult result,
			HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "saveRegScreenSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		registrationScreenSettingsValidator.validate(regScreenSettings, result);
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		final User loginUser = (User) session.getAttribute("loginUser");
		String status = null;
		String reponse = null;
		if (result.hasErrors()) {
			return "setupregscreen";
		} else {
			status = hubCitiService.saveRegScreenSettings(regScreenSettings, loginUser);

			if (null != status && status.equals(ApplicationConstants.SUCCESS)) {

				reponse = "Settings Saved Succesfully";
				request.setAttribute("responeMsg", reponse);
				request.setAttribute("responseStatus", ApplicationConstants.SUCCESS);
				session.setAttribute("btnname", "Update Settings");
			} else {
				session.setAttribute("btnname", "Save Settings");
				reponse = "Error occured while saving Settings";
				request.setAttribute("responeMsg", reponse);
				request.setAttribute("responseStatus", ApplicationConstants.FAILURE);
			}

		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "setupregscreen";
	}

	/**
	 * This method will return about us screen.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/setupaboutusscreen.htm", method = RequestMethod.GET)
	public String setupAboutUsScreen(HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "setupAboutUsScreen";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		session.setAttribute("minCropWd", "220");
		session.setAttribute("minCropHt", "50");

		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.ABOUTUS);

		final User loginUser = (User) session.getAttribute("loginUser");
		ScreenSettings aboutusScreenSettings = null;
		loginUser.setPageType(ApplicationConstants.ABOUTUSPAGE);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		aboutusScreenSettings = hubCitiService.fetchScreenSettings(loginUser);
		if (null != aboutusScreenSettings && aboutusScreenSettings.isScreenSettingsFlag()) {
			model.put("screenSettingsForm", new ScreenSettings());
			session.setAttribute("hubCitiAppVersion", aboutusScreenSettings.getHubCitiVersion());
			session.setAttribute("btnname", "Save Settings");
			// session.setAttribute("smallLogo", "images/upload_image.png");
			session.setAttribute("aboutusScreenImage", "images/upload_image.png");
			session.setAttribute("aboutusScreenImagePreview", "images/aboutus-image.png");
			session.setAttribute("smallLogoPreview", "images/small-logo.png");
		} else {

			aboutusScreenSettings.setOldImageName(aboutusScreenSettings.getLogoImageName());
			aboutusScreenSettings.setSmallOldImageName(aboutusScreenSettings.getSmallLogoImageName());
			session.setAttribute("hubCitiAppVersion", aboutusScreenSettings.getHubCitiVersion());
			model.put("screenSettingsForm", aboutusScreenSettings);
			session.setAttribute("btnname", "Update Settings");
			// session.setAttribute("smallLogo",678
			// aboutusScreenSettings.getSmallLogoPath());
			session.setAttribute("aboutusScreenImage", aboutusScreenSettings.getLogoPath());
			session.setAttribute("aboutusScreenImagePreview", aboutusScreenSettings.getLogoPath());
			session.setAttribute("smallLogoPreview", aboutusScreenSettings.getSmallLogoPath());
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "setupaboutusscreen";
	}

	/**
	 * This method saves the about us screen details.
	 * 
	 * @param screenSettings
	 * @param result
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/setupaboutusscreen.htm", method = RequestMethod.POST)
	public String saveAboutUsScreenSettings(@ModelAttribute("screenSettingsForm") ScreenSettings screenSettings, BindingResult result,
			HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "saveAboutUsScreenSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		aboutUsScreenSettingsValidation.validate(screenSettings, result);
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		final User loginUser = (User) session.getAttribute("loginUser");
		String status = null;
		String reponse = null;
		if (result.hasErrors()) {
			return "setupaboutusscreen";
		} else {
			status = hubCitiService.saveAboutusScreenSettings(screenSettings, loginUser);

			if (null != status && status.equals(ApplicationConstants.SUCCESS)) {

				reponse = "Settings Saved Succesfully";
				request.setAttribute("responeMsg", reponse);
				request.setAttribute("responseStatus", ApplicationConstants.SUCCESS);
				session.setAttribute("btnname", "Update Settings");
			} else {
				session.setAttribute("btnname", "Save Settings");
				reponse = "Error occured while saving Settings";
				request.setAttribute("responeMsg", reponse);
				request.setAttribute("responseStatus", ApplicationConstants.FAILURE);
			}

		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "setupaboutusscreen";
	}

	/**
	 * This method will return privacy policy setup screen.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/setupprivacypolicyscreen.htm", method = RequestMethod.GET)
	public String setupPrivacyPolicyScreen(HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "setupPrivacyPolicyScreen";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		// Default images path
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.PRIVACYPOLICY);

		final User loginUser = (User) session.getAttribute("loginUser");
		ScreenSettings privacyPolicyScreenSettings = null;
		loginUser.setPageType(ApplicationConstants.PRIVACYPOLICYSCREEN);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		privacyPolicyScreenSettings = hubCitiService.fetchScreenSettings(loginUser);
		if (null == privacyPolicyScreenSettings) {
			model.put("screenSettingsForm", new ScreenSettings());
			session.setAttribute("btnname", "Save Settings");
		} else {

			model.put("screenSettingsForm", privacyPolicyScreenSettings);
			session.setAttribute("btnname", "Update Settings");
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "setupprivacypolicyscreen";
	}

	/**
	 * This method saves the privacy policy details.
	 * 
	 * @param screenSettings
	 * @param result
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/setupprivacypolicyscreen.htm", method = RequestMethod.POST)
	public String savePrivacyPolicyScreenSettings(@ModelAttribute("screenSettingsForm") ScreenSettings screenSettings, BindingResult result,
			HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "saveRegScreenSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		policyScreenSettingsValidation.validate(screenSettings, result);
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		final User loginUser = (User) session.getAttribute("loginUser");

		String status = null;
		String reponse = null;
		if (result.hasErrors()) {
			return "setupprivacypolicyscreen";
		} else {
			/* Code to FIX XSS */
			screenSettings.setPageContent(Utility.getXssFreeString(screenSettings.getPageContent()));

			status = hubCitiService.savePrivacyPolicyScreenSettings(screenSettings, loginUser);

			if (null != status && status.equals(ApplicationConstants.SUCCESS)) {

				reponse = "Settings Saved Succesfully";
				request.setAttribute("responeMsg", reponse);
				request.setAttribute("responseStatus", ApplicationConstants.SUCCESS);
				session.setAttribute("btnname", "Update Settings");
			} else {
				session.setAttribute("btnname", "Save Settings");
				reponse = "Error occured while saving Settings";
				request.setAttribute("responeMsg", reponse);
				request.setAttribute("responseStatus", ApplicationConstants.FAILURE);
			}

		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "setupprivacypolicyscreen";
	}

	/**
	 * This method will return splash screen setup screen.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/setupsplashscreen.htm", method = RequestMethod.GET)
	public String setupSplashScreen(HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "setupSplashScreen";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		// Minimum crop height and width for login screen
		session.setAttribute("minCropHt", 568);
		session.setAttribute("minCropWd", 320);
		// Default images path
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.SPLASHSCREEN);

		final User loginUser = (User) session.getAttribute("loginUser");
		ScreenSettings splashScreenDetails = null;

		loginUser.setPageType(ApplicationConstants.SPLASHIMAGE);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		splashScreenDetails = hubCitiService.fetchScreenSettings(loginUser);
		if (null == splashScreenDetails) {
			model.put("screenSettingsForm", new ScreenSettings());
			session.setAttribute("btnname", "Save Settings");
			session.setAttribute("splashImage", "images/upload_image.png");
			session.setAttribute("splashImagePreview", "images/spash-screeen.png");

		} else {

			splashScreenDetails.setOldImageName(splashScreenDetails.getLogoImageName());
			model.put("screenSettingsForm", splashScreenDetails);
			session.setAttribute("btnname", "Update Settings");
			session.setAttribute("splashImage", splashScreenDetails.getLogoPath());
			session.setAttribute("splashImagePreview", splashScreenDetails.getLogoPath());

		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "setupsplashscreen";
	}

	/**
	 * This method will save splash screen details.
	 * 
	 * @param screenSettings
	 * @param result
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/setupsplashscreen.htm", method = RequestMethod.POST)
	public String saveSplashScreenSettings(@ModelAttribute("screenSettingsForm") ScreenSettings screenSettings, BindingResult result,
			HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "saveSplashScreenSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		splashScreenSettingsValidator.validate(screenSettings, result);
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		final User loginUser = (User) session.getAttribute("loginUser");
		String status = null;
		String reponse = null;
		if (result.hasErrors()) {
			return "setupsplashscreen";
		} else {
			status = hubCitiService.saveSplashScreenSettings(screenSettings, loginUser);

			if (null != status && status.equals(ApplicationConstants.SUCCESS)) {

				reponse = "Settings Saved Succesfully";
				request.setAttribute("responeMsg", reponse);
				request.setAttribute("responseStatus", ApplicationConstants.SUCCESS);
				session.setAttribute("btnname", "Update Settings");
			} else {
				session.setAttribute("btnname", "Save Settings");
				reponse = "Error occured while saving Settings";
				request.setAttribute("responeMsg", reponse);
				request.setAttribute("responseStatus", ApplicationConstants.FAILURE);
			}

		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "setupsplashscreen";
	}

	/**
	 * This method will return general setup screen.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/generealsettings.htm", method = RequestMethod.GET)
	public String showGeneralSettingsScreen(@ModelAttribute("screenSettingsForm") ScreenSettings screenSettings, BindingResult result,
			HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "showGeneralSettingsScreen";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.GENERALSETTINGS);

		final User loginUser = (User) session.getAttribute("loginUser");
		ScreenSettings generalScreenDetails = null;
		String settingType = "MainMenu";
		String view = "menugeneralsettings";

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		if (null != screenSettings.getPageType() || "".equalsIgnoreCase(screenSettings.getPageType())) {
			settingType = screenSettings.getPageType();
		}

		if ("TabBarLogo".equalsIgnoreCase(settingType)) {
			// Minimum crop height and width for login screen
			session.setAttribute("minCropHt", 34);
			session.setAttribute("minCropWd", 86);
			List<ScreenSettings> buttomBtnTyp = hubCitiService.displayButtomBtnType();
			session.setAttribute("buttomBtnType", buttomBtnTyp);
			view = "generalsettings";
		} else {
			// Minimum crop height and width for login screen
			session.setAttribute("minCropHt", 480);
			session.setAttribute("minCropWd", 320);
			view = "menugeneralsettings";
		}

		generalScreenDetails = hubCitiService.fetchGeneralSettings(loginUser.getHubCitiID(), settingType);

		if (null == generalScreenDetails) {
			model.put("screenSettingsForm", new ScreenSettings());
			session.setAttribute("btnname", "Save Settings");
			session.setAttribute("titleBarLogo", "images/upload_image.png");
			session.setAttribute("titleBarLogoPreview", "small-logo.png");
			session.setAttribute("appiconpreview", "images/upload_image.png");
		} else {

			if (null != generalScreenDetails.getBgColor() && !"".equalsIgnoreCase(generalScreenDetails.getBgColor())) {
				generalScreenDetails.setIconSelection("color");
				session.setAttribute("titleBarLogo", "images/upload_image.png");
				session.setAttribute("titleBarLogoPreview", "small-logo.png");

			}

			if (null != generalScreenDetails.getLogoImageName() && !"".equalsIgnoreCase(generalScreenDetails.getLogoImageName())) {
				generalScreenDetails.setIconSelection("image");
				generalScreenDetails.setOldImageName(generalScreenDetails.getLogoImageName());
				session.setAttribute("titleBarLogo", generalScreenDetails.getLogoPath());
				session.setAttribute("titleBarLogoPreview", generalScreenDetails.getLogoPath());

			} else {
				session.setAttribute("titleBarLogo", "images/upload_image.png");
				session.setAttribute("titleBarLogoPreview", "small-logo.png");
			}

			if (null != generalScreenDetails.getBannerImageName() && !"".equalsIgnoreCase(generalScreenDetails.getBannerImageName())) {
				generalScreenDetails.setIconSelection("image");
				generalScreenDetails.setOldAppIconImage(generalScreenDetails.getBannerImageName());
				session.setAttribute("appiconpreview", generalScreenDetails.getImagePath());

			} else {
				session.setAttribute("appiconpreview", "images/upload_image.png");

			}

			if (null != generalScreenDetails.getBottomBtnId()) {
				session.setAttribute("buttomBtn", "Exist");
				session.setAttribute("bottomBtnId", generalScreenDetails.getBottomBtnId());
			}

			model.put("screenSettingsForm", generalScreenDetails);
			session.setAttribute("btnname", "Update Settings");
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		session.setAttribute("menuType", settingType);
		return view;
	}

	/**
	 * This method will save the general setup details.
	 * 
	 * @param screenSettings
	 * @param result
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/generealsettings.htm", method = RequestMethod.POST)
	public String saveGeneralSettings(@ModelAttribute("screenSettingsForm") ScreenSettings screenSettings, BindingResult result,
			HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "saveGeneralSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;
		String reponse = null;
		String screenType = screenSettings.getPageType();
		session.setAttribute("menuType", screenType);

		String view = "menugeneralsettings";
		if ("TabBarLogo".equalsIgnoreCase(screenType)) {
			String buttomBtn = (String) session.getAttribute("buttomBtn");
			if ("Exist".equalsIgnoreCase(buttomBtn)) {
				screenSettings.setBottomBtnId((Integer) session.getAttribute("bottomBtnId"));
			}

			view = "generalsettings";
		}

		splashScreenSettingsValidator.validateGeneralSettings(screenSettings, result);
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		final User loginUser = (User) session.getAttribute("loginUser");
		if (result.hasErrors()) {
			return view;
		} else {
			status = hubCitiService.saveGeneralSettings(screenSettings, loginUser);

			if ("TabBarLogo".equalsIgnoreCase(screenType)) {
				session.setAttribute("buttomBtn", "Exist");
				session.setAttribute("bottomBtnId", screenSettings.getBottomBtnId());
			}

			if (null != status && status.equals(ApplicationConstants.SUCCESS)) {

				reponse = "Settings Saved Succesfully";
				request.setAttribute("responeMsg", reponse);
				request.setAttribute("responseStatus", ApplicationConstants.SUCCESS);
				session.setAttribute("btnname", "Update Settings");
			} else {
				session.setAttribute("btnname", "Save Settings");
				reponse = "Error occured while saving Settings";
				request.setAttribute("responeMsg", reponse);
				request.setAttribute("responseStatus", ApplicationConstants.FAILURE);
			}

		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return view;
	}

	/**
	 * This method will display User Settings Screen.
	 * 
	 * @param screenSettings
	 * @param result
	 * @param request
	 * @param model
	 * @param session
	 * @return view
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/usersettings.htm", method = RequestMethod.GET)
	public String displayUserSettings(@ModelAttribute("screenSettingsForm") ScreenSettings screenSettings, BindingResult result,
			HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "displayUserSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		ScreenSettings userSettings = null;
		// Minimum crop height and width for login screen
		session.setAttribute("minCropHt", 116);
		session.setAttribute("minCropWd", 300);

		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.USERSETTINGSSCREEN);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		final User loginUser = (User) session.getAttribute("loginUser");

		userSettings = hubCitiService.fetchUserSettings(loginUser.getHubCitiID());

		if (null != userSettings) {
			session.setAttribute("btnname", "Update");
			if (null != userSettings.getLogoPath()) {
				session.setAttribute("userSettingImg", userSettings.getLogoPath());
				userSettings.setOldImageName(userSettings.getLogoImageName());
			} else {
				session.setAttribute("userSettingImg", "images/upload_image.png");
			}
			session.setAttribute("pageView", "edit");
			model.put("screenSettingsForm", userSettings);
		} else {
			session.setAttribute("btnname", "Save");
			session.setAttribute("userSettingImg", "images/upload_image.png");
			session.setAttribute("pageView", "add");
			model.put("screenSettingsForm", new ScreenSettings());
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "userSettings";
	}

	/**
	 * This method will Save User Settings Details.
	 * 
	 * @param screenSettings
	 * @param result
	 * @param request
	 * @param model
	 * @param session
	 * @return view
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/saveusersettings.htm", method = RequestMethod.POST)
	public String saveUserSettings(@ModelAttribute("screenSettingsForm") ScreenSettings screenSettings, BindingResult result,
			HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "saveUserSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.USERSETTINGSSCREEN);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		final User loginUser = (User) session.getAttribute("loginUser");
		String status = null;
		String response = null;

		registrationScreenSettingsValidator.validateUserSeetings(screenSettings, result);

		if (result.hasErrors()) {
			return "userSettings";
		} else {
			status = hubCitiService.saveUserSettings(screenSettings, loginUser);
			if (null != status && status.equals(ApplicationConstants.SUCCESS)) {
				if ("add".equalsIgnoreCase(screenSettings.getPageView())) {
					response = "User Settings Saved Succesfully";
				} else {
					response = "User Settings Updated Succesfully";
				}

				request.setAttribute("responeMsg", response);
				request.setAttribute("responseStatus", ApplicationConstants.SUCCESS);
				session.setAttribute("btnname", "Update");
				session.setAttribute("pageView", "edit");

				if (!screenSettings.getUserSettingsFields().contains("optn-dntn")) {
					session.setAttribute("userSettingImg", "images/upload_image.png");
					screenSettings.setLogoImageName(null);
					screenSettings.setOldImageName(null);
				} else {
					screenSettings.setOldImageName(screenSettings.getLogoImageName());
				}

			} else {
				session.setAttribute("btnname", "Save");
				if ("add".equalsIgnoreCase(screenSettings.getPageView())) {
					response = "Error occured while saving User Settings";
					session.setAttribute("pageView", "edit");
				} else {
					response = "Error occured while updating User Settings";
				}
				request.setAttribute("responeMsg", response);
				request.setAttribute("responseStatus", ApplicationConstants.FAILURE);
			}
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "userSettings";
	}
}
