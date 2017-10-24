package com.hubciti.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.exception.HubCitiServiceException;
import com.hubciti.common.pojo.PageStatus;
import com.hubciti.common.pojo.User;
import com.hubciti.common.util.Utility;
import com.hubciti.service.HubCitiService;
import com.hubciti.validator.PasswordValidator;

/**
 * This class is a controller class for Login.
 * 
 * @author dileep_cc
 */
@Controller
public class LoginController {

	/**
	 * Getting the logger Instance.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

	/**
	 * variable of type PasswordValidator.
	 */
	PasswordValidator passwordValidator;

	/**
	 * @param passwordValidator
	 *            the passwordValidator to set
	 */
	@Autowired
	public void setPasswordValidator(PasswordValidator passwordValidator) {
		this.passwordValidator = passwordValidator;
	}

	/**
	 * This method return login screen.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login.htm", method = RequestMethod.GET)
	public ModelAndView login(ModelMap model, HttpSession session, HttpServletRequest request) {

		// Flag to indicate if user has already logged-in and session is still
		// valid
		Boolean hasLoggedIn = (Boolean) session.getAttribute("hasLoggedIn");
		if (hasLoggedIn != null && hasLoggedIn) {
			User user = (User) session.getAttribute("loginUser");
			if (null != user) {

				request.setAttribute("loggedinUserName", user.getUserName());
			}

		}

		return new ModelAndView("login");
	}

	/**
	 * This method will invoke when the login error occur.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loginfailed.htm", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {

		model.addAttribute("error", "true");
		return "login";

	}

	/**
	 * This method will call when the user wants to logout from the application.
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/logout.htm", method = RequestMethod.GET)
	public String logout(ModelMap model, HttpSession session) {

		final String methodName = "logout";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		session.invalidate();

		LOG.info(ApplicationConstants.METHODEND + methodName);

		return "login";

	}

	/**
	 * This method will invoke when the session is expired.
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @param session1
	 * @return
	 */
	@RequestMapping(value = "/sessionTimeout.htm", method = RequestMethod.GET)
	public final String SessionTimeOut(HttpServletRequest request, HttpSession session, ModelMap model, HttpSession session1) {
		final String methodName = "SessionTimeOut";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "sessionTimeout";
	}

	/**
	 * This method is used when the user forgot the password.
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/forgetpwd.htm", method = RequestMethod.GET)
	public final String showForgotPasswordPage(HttpServletRequest request, HttpSession session, ModelMap model) {
		final String strMethodName = "showForgotPasswordPage";
		final String strViewName = "forgetpwd";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		final User objUser = new User();
		model.put("forgetpwdform", objUser);
		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strViewName;
	}

	/**
	 * This method returns the new password to the user email.
	 * 
	 * @param objUser
	 * @param result
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/forgetpwd.htm", method = RequestMethod.POST)
	public final ModelAndView submitForgotPasswordPage(@ModelAttribute("forgetpwdform") User objUser, BindingResult result,
			HttpServletRequest request, HttpSession session, ModelMap model) throws HubCitiServiceException {
		final String strMethodName = "showForgotPasswordPage";
		final String strViewName = "forgetpwd";
		final String strResponse = null;
		User objForgotPwd = null;
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		try {
			objForgotPwd = hubCitiService.forgotPwd(objUser);

			if (null != objForgotPwd && objForgotPwd.getResponse().equalsIgnoreCase(ApplicationConstants.SUCCESS)) {
				request.setAttribute("msgResponse", "Check the message sent to you at " + objForgotPwd.getEmailId() + " to find out what to do next.");
				request.setAttribute("fontStyle", "font-weight:bold;color:#00559c;");
			} else
				if (objForgotPwd.getResponse().equalsIgnoreCase(ApplicationConstants.FAILURETEXT)) {
					request.setAttribute("msgResponse", "UserName does not exist.");
					request.setAttribute("fontStyle", "font-weight:bold;color:red");
				} else {
					request.setAttribute("msgResponse", "Error occurred while send the password.");
					request.setAttribute("fontStyle", "font-weight:bold;color:red");
				}

		} catch (HubCitiServiceException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED, e.getMessage());
			request.setAttribute("msgResponse", "Error occurred while send the password.");
			request.setAttribute("fontStyle", "font-weight:bold;color:red");
			return new ModelAndView(strViewName);
		}
		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return new ModelAndView(strViewName);
	}

	/**
	 * This method invokes when the login is success.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/loginsuccess.htm", method = RequestMethod.GET)
	public ModelAndView loginSuccess(HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "loginSuccess";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String redirectLink = null;
		User user = new User();
		final String userName = SecurityContextHolder.getContext().getAuthentication().getName();

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		user = hubCitiService.getLoginAdminDetails(userName);

		session.setAttribute("loginUser", user);
		if(null != user.getUserType()) {
			session.setAttribute("loginUserType", user.getUserType());
		}
		redirectLink = findHomePageByUserRole();
		if (user.getResetPassword() == 0) {
			return new ModelAndView(new RedirectView("changepassword.htm"));
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return new ModelAndView(new RedirectView(redirectLink));
	}

	/**
	 * This method invoke when the user is logining in for the first time.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/changepassword.htm", method = RequestMethod.GET)
	public String changePassword(HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "changePassword";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		final User login = new User();

		model.put("login", login);

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "changepassword";
	}

	/**
	 * This method invoke when the user is logining in for the first time.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/savepassword.htm", method = RequestMethod.POST)
	public ModelAndView savePassword(@ModelAttribute("login") User requestUser, BindingResult result, HttpServletRequest request, ModelMap model,
			HttpSession session) throws HubCitiServiceException {
		final String methodName = "savePassword";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		final User user = (User) session.getAttribute("loginUser");
		String status = new String();
		String redirectLink = "welcome.htm";
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		passwordValidator.validate(requestUser, result);

		if (result.hasErrors()) {
			return new ModelAndView("changepassword");
		}

		final String newpassword = requestUser.getPassword();
		final String cnfrmPswd = requestUser.getConfirmPassword();

		if (newpassword.length() < 8) {
			passwordValidator.validate(requestUser, result, ApplicationConstants.PASSWORDLength);
		} else
			if (!(newpassword.equals(cnfrmPswd))) {
				passwordValidator.validate(requestUser, result, ApplicationConstants.PASSWORD);
			} else {
				final Boolean isValidPassword = Utility.validatePassword(newpassword);
				if (!isValidPassword) {
					passwordValidator.validate(requestUser, result, ApplicationConstants.PASSWORDMATCH);
				}
			}

		if (result.hasErrors()) {
			return new ModelAndView("changepassword");
		}

		user.setPassword(newpassword);
		status = hubCitiService.saveChangedPassword(user);
		redirectLink = findHomePageByUserRole();
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return new ModelAndView(new RedirectView(redirectLink));
	}

	/**
	 * This method returns user welcome page.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/welcome.htm", method = RequestMethod.GET)
	public String showWelcomePage(HttpServletRequest request, ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "showWelcomePage";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.HUBCITIADMINISTRATION);
		final User user = (User) session.getAttribute("loginUser");
		final int hubCitiId = user.getHubCitiID();

		PageStatus status = new PageStatus();
		status = hubCitiService.getScreenStatus(hubCitiId);

		session.setAttribute("pageStatus", status);
		// This will only be removed once the session has expired
		// If it doesn't expire the user will not be able to login
		// again!
		session.setAttribute("hasLoggedIn", new Boolean(true));
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "welcomepage";
	}

	public String findHomePageByUserRole() {
		final String methodName = "findHomePageByUserRole";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Collection<GrantedAuthority> userAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		List<String> rolesList = null;
		String redirectLink = "welcome.htm";
		if (null != userAuthorities && userAuthorities.size() > 0) {
			rolesList = new ArrayList<String>();
			for (GrantedAuthority authority : userAuthorities) {
				rolesList.add(authority.getAuthority());
			}

			if (rolesList.contains(ApplicationConstants.HUBCITIEVENTSUPERUSERROLE) || rolesList.contains(ApplicationConstants.HUBCITIEVENTUSERROLE)) {
				redirectLink = "manageevents.htm";
			} else if (rolesList.contains(ApplicationConstants.HUBCITIFUNDRAISINGSUPERUSERROLE) || rolesList.contains(ApplicationConstants.HUBCITIFUNDRAISINGUSERROLE)) {
				redirectLink = "managefundraisers.htm";
			}
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return redirectLink;
	}
}
