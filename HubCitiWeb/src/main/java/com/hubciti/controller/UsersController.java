/**
 * 
 */
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
import com.hubciti.common.pojo.AlertCategory;
import com.hubciti.common.pojo.Module;
import com.hubciti.common.pojo.User;
import com.hubciti.common.pojo.UserDetails;
import com.hubciti.common.tags.Pagination;
import com.hubciti.common.util.Utility;
import com.hubciti.service.HubCitiService;
import com.hubciti.validator.UsersValidator;

/**
 * This class is a Controller class for Managing HubCiti Created Users.
 * 
 * @author sangeetha.ts
 */
@Controller
public class UsersController {

	/**
	 * Getting the logger Instance.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(UsersController.class);

	UsersValidator usersValidator;

	@Autowired
	void setUsersValidator(UsersValidator usersValidator) {
		this.usersValidator = usersValidator;
	}

	public void fetchUsers(User user, Integer currentPage, HttpServletRequest request, HttpSession session) throws HubCitiServiceException {

		UserDetails userLst = null;
		Pagination objPage = null;
		final int recordCount = 20;

		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.SETUPUSERS);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		try {

			User loginUser = (User) session.getAttribute("loginUser");
			user.setHubCitiID(loginUser.getHubCitiID());
			user.sethCAdminUserID(loginUser.gethCAdminUserID());

			userLst = hubCitiService.displayHubCitiCreatedUsers(user);

			if (null == userLst && user.getLowerLimit() > 0) {
				Integer lowerLimit = user.getLowerLimit() - 1;
				user.setLowerLimit(lowerLimit);
				userLst = hubCitiService.displayHubCitiCreatedUsers(user);
			}

			if (null != userLst && userLst.getTotalSize() > 0) {
				session.setAttribute("userLst", userLst.getUserLst());
				objPage = Utility.getPagination(userLst.getTotalSize(), currentPage, "displayusers.htm", recordCount);
				session.setAttribute(ApplicationConstants.PAGINATION, objPage);
			} else
				if (null != user.getUserSearch()) {
					request.setAttribute("responseStatus", "INFO");
					request.setAttribute("responeMsg", "No users found");
					session.setAttribute("userLst", null);
					session.setAttribute(ApplicationConstants.PAGINATION, null);
				} else {
					session.setAttribute("userLst", null);
					session.setAttribute(ApplicationConstants.PAGINATION, null);
				}

			request.setAttribute("lowerLimit", user.getLowerLimit());

		} catch (HubCitiServiceException exception) {

			throw new HubCitiServiceException(exception);
		}
	}

	/**
	 * This Method will return all the users created by the hubciti admin.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/displayusers.htm", method = RequestMethod.GET)
	public String displayHubCitiCreatedUsers(@ModelAttribute("users") User users, BindingResult result, HttpServletRequest request, ModelMap model,
			HttpSession session) throws HubCitiServiceException {

		String methodName = "displayHubCitiCreatedUsers";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String viewName = "setupUsers";
		final String pageFlag = request.getParameter("pageFlag");
		Integer lowerLimit = 0;
		String pageNumber = "0";
		Integer currentPage = 1;

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		AlertCategory eventCatObj = null;
		AlertCategory fundraiserCatObj = null;
		List<Module> modules = null;
		try {

			if (null != pageFlag && "true".equals(pageFlag)) {
				pageNumber = request.getParameter("pageNumber");
				final Pagination pageSess = (Pagination) session.getAttribute(ApplicationConstants.PAGINATION);
				if (Integer.valueOf(pageNumber) != 0) {
					currentPage = Integer.valueOf(pageNumber);
					final int number = Integer.valueOf(currentPage) - 1;
					final int pageSize = pageSess.getPageRange();
					lowerLimit = pageSize * Integer.valueOf(number);
				}
			} else
				if (null != users.getLowerLimit()) {
					lowerLimit = users.getLowerLimit();
					currentPage = (lowerLimit + 20) / 20;
				}

			users.setLowerLimit(lowerLimit);
			fetchUsers(users, currentPage, request, session);
			User loginUser = (User) session.getAttribute("loginUser");
		
			
			modules = hubCitiService.displayUserModules(loginUser.getHubCitiID(), null);
			session.setAttribute("modulesLst", modules);
			
			eventCatObj = hubCitiService.fetchEventCategories(null, loginUser);
			if (null != eventCatObj) {
				session.setAttribute("eventCatLst", eventCatObj.getAlertCatLst());
			}
			
			fundraiserCatObj = hubCitiService.fetchFundraiserCategories(null, loginUser);
			if (null != fundraiserCatObj) {
				session.setAttribute("fundraiserCatLst", fundraiserCatObj.getAlertCatLst());
			}

			User modelUser = new User();
			if (null != users.getUserSearch()) {
				modelUser.setUserSearch(users.getUserSearch());
			}

			if ((null == users.getUserSearch() || "".equalsIgnoreCase(users.getUserSearch())) && null == session.getAttribute("userLst")) {
				request.setAttribute("userSearch", null);
				model.put("addUser", new User());
				viewName = "addUser";
			} else {
				model.put("users", modelUser);
			}

		} catch (HubCitiServiceException exception) {

			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);

		return viewName;
	}

	/**
	 * This Method will return all the users created by the hubciti admin.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/adduser.htm", method = RequestMethod.GET)
	public String createHubCitiUsers(@ModelAttribute("users") User users, BindingResult result, HttpServletRequest request, ModelMap model,
			HttpSession session) throws HubCitiServiceException {

		String methodName = "createHubCitiUsers";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		
		List<Module> modules = null;
		AlertCategory eventCatObj = null;
		AlertCategory fundraiserCatObj = null;
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		try {

			User loginUser = (User) session.getAttribute("loginUser");
			
			modules = hubCitiService.displayUserModules(loginUser.getHubCitiID(), null);
			session.setAttribute("modulesLst", modules);
			
			eventCatObj = hubCitiService.fetchEventCategories(null, loginUser);
			if (null != eventCatObj) {
				session.setAttribute("eventCatLst", eventCatObj.getAlertCatLst());
			}
			
			fundraiserCatObj = hubCitiService.fetchFundraiserCategories(null, loginUser);
			if (null != fundraiserCatObj) {
				session.setAttribute("fundraiserCatLst", fundraiserCatObj.getAlertCatLst());
			}

			request.setAttribute("userSearch", users.getUserSearch());
			request.setAttribute("lowerLimit", users.getLowerLimit());
		} catch (HubCitiServiceException exception) {
			throw new HubCitiServiceException(exception);
		}
		model.put("addUser", new User());

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "addUser";
	}

	/**
	 * This Method will return all the users created by the hubciti admin.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/saveuser.htm", method = RequestMethod.POST)
	public String saveUpdateHubCitiUsers(@ModelAttribute("addUser") User user, BindingResult result, HttpServletRequest request, ModelMap model,
			HttpSession session) throws HubCitiServiceException {

		String methodName = "saveUpdateHubCitiUsers";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;
		Boolean isValidEmail = null;
		User modelUser = new User();
		String userType = "";
		String[] modules = null;

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		try {

			User loginUser = (User) session.getAttribute("loginUser");

			request.setAttribute("eventCat", user.getEventCategory());
			request.setAttribute("fundraiserCat", user.getFundraiserCategory());
			request.setAttribute("module", user.getModule());
			user.setHubCitiID(loginUser.getHubCitiID());
			user.sethCAdminUserID(loginUser.gethCAdminUserID());

			if (null == user.getRoleUserId()) {
				usersValidator.validate(user, result);

				if (result.hasErrors()) {
					return "addUser";
				}
				isValidEmail = Utility.validateEmailId(user.getEmailId());

				if (!isValidEmail) {
					usersValidator.validate(user, result, ApplicationConstants.INVALIDEMAIL);

					if (result.hasErrors()) {
						return "addUser";
					}
				}
				
				modules = user.getModuleName().split(",");			
				if("NORMAL_USER".equalsIgnoreCase(user.getUserType())) {
					for (String module : modules) {
						if("Events".equalsIgnoreCase(module)) {						
							if("".equalsIgnoreCase(userType)) {
								userType = "ROLE_EVENT_USER";
							} else {
								userType = userType + ",ROLE_EVENT_USER";
							}
						} else if("Fundraisers".equalsIgnoreCase(module)) {						
							if("".equalsIgnoreCase(userType)) {
								userType = "ROLE_FUNDRAISING_USER";
							} else {
								userType = userType + ",ROLE_FUNDRAISING_USER";
							}
						}
					} 
				} else {
					for (String module : modules) {
						if("Events".equalsIgnoreCase(module)) {						
							if("".equalsIgnoreCase(userType)) {
								userType = "ROLE_EVENT_SUPER_USER";
							} else {
								userType = userType + ",ROLE_EVENT_SUPER_USER";
							}
						} else if("Fundraisers".equalsIgnoreCase(module)) {						
							if("".equalsIgnoreCase(userType)) {
								userType = "ROLE_FUNDRAISING_SUPER_USER";
							} else {
								userType = userType + ",ROLE_FUNDRAISING_SUPER_USER";
							}
						}
					}
					
					user.setEventCategory(null);
					user.setFundraiserCategory(null);
				}
				
				user.setUserType(userType);
				status = hubCitiService.createUserDeatils(user, loginUser);
			} else {
				user.setUserName((String) session.getAttribute("userName"));
				usersValidator.editValidate(user, result);

				if (result.hasErrors()) {
					request.setAttribute("eventCat", user.getEventCategory());
					return "addUser";
				}

				isValidEmail = Utility.validateEmailId(user.getEmailId());

				if (!isValidEmail) {
					usersValidator.validate(user, result, ApplicationConstants.INVALIDEMAIL);

					if (result.hasErrors()) {
						request.setAttribute("eventCat", user.getEventCategory());
						return "addUser";
					}
				}

				user.setPreviousEmailId((String) session.getAttribute("previousEmail"));
				
				modules = user.getModuleName().split(",");			
				if("NORMAL_USER".equalsIgnoreCase(user.getUserType())) {
					for (String module : modules) {
						if("Events".equalsIgnoreCase(module)) {						
							if("".equalsIgnoreCase(userType)) {
								userType = "ROLE_EVENT_USER";
							} else {
								userType = userType + ",ROLE_EVENT_USER";
							}
						} else if("Fundraisers".equalsIgnoreCase(module)) {						
							if("".equalsIgnoreCase(userType)) {
								userType = "ROLE_FUNDRAISING_USER";
							} else {
								userType = userType + ",ROLE_FUNDRAISING_USER";
							}
						}
					} 
					
					if(!userType.contains("ROLE_EVENT_USER")) {
						user.setEventCategory(null);
					} else if(!userType.contains("ROLE_FUNDRAISING_USER")) {
						user.setFundraiserCategory(null);
					}
					
				} else {
					for (String module : modules) {
						if("Events".equalsIgnoreCase(module)) {						
							if("".equalsIgnoreCase(userType)) {
								userType = "ROLE_EVENT_SUPER_USER";
							} else {
								userType = userType + ",ROLE_EVENT_SUPER_USER";
							}
						} else if("Fundraisers".equalsIgnoreCase(module)) {						
							if("".equalsIgnoreCase(userType)) {
								userType = "ROLE_FUNDRAISING_SUPER_USER";
							} else {
								userType = userType + ",ROLE_FUNDRAISING_SUPER_USER";
							}
						}
					}					
					user.setEventCategory(null);
					user.setFundraiserCategory(null);
				}
				
				user.setUserType(userType);				
				
				status = hubCitiService.updateUserDeatils(user, loginUser);
			}

			if (status.equalsIgnoreCase(ApplicationConstants.DUPLICATEUSERNAME)) {
				usersValidator.validate(user, result, ApplicationConstants.DUPLICATEUSERNAME);
				if (result.hasErrors()) {
					return "addUser";
				}
			}
			if (status.equalsIgnoreCase(ApplicationConstants.DUPLICATEEMAIL)) {
				usersValidator.validate(user, result, ApplicationConstants.DUPLICATEEMAIL);
				if (result.hasErrors()) {
					return "addUser";
				}
			}
			if (status.equalsIgnoreCase(ApplicationConstants.SUCCESS)) {

				User userDetails = new User();
				userDetails.setHubCitiID(loginUser.getHubCitiID());
				userDetails.sethCAdminUserID(loginUser.gethCAdminUserID());
				Integer currentPage = 1;
				Integer lowerLimit = user.getLowerLimit();

				request.setAttribute("responseStatus", "SUCCESS");
				if (null == user.getRoleUserId()) {
					request.setAttribute("responeMsg", "User Created successfully");
					userDetails.setLowerLimit(0);
				} else {
					request.setAttribute("responeMsg", "User Updated successfully");
					userDetails.setUserSearch(user.getUserSearch());
					modelUser.setUserSearch(user.getUserSearch());
					userDetails.setLowerLimit(lowerLimit);
					if (null != lowerLimit && lowerLimit > 0) {
						currentPage = (lowerLimit + 20) / 20;
					}
				}

				fetchUsers(userDetails, currentPage, request, session);

			}
		} catch (HubCitiServiceException exception) {
			throw new HubCitiServiceException(exception);
		}

		model.put("users", modelUser);

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "setupUsers";
	}

	/**
	 * This Method will return all the users created by the hubciti admin.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/edituser.htm", method = RequestMethod.GET)
	public String editHubCitiUsers(@ModelAttribute("users") User users, BindingResult result, HttpServletRequest request, ModelMap model,
			HttpSession session) throws HubCitiServiceException {

		String methodName = "editHubCitiUsers";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		session.removeAttribute("previousEmail");
		session.removeAttribute("userName");

		User userDetails = null;
		AlertCategory eventCatObj = null;
		AlertCategory fundraiserCatObj = null;
		List<Module> modules = null;
		
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		try {

			userDetails = hubCitiService.fetchUserDetails(users.getRoleUserId());
			
			if(null == userDetails.getEventCategory() && null == userDetails.getFundraiserCategory()) {
				userDetails.setUserType("SUPER_USER");
				request.setAttribute("module", userDetails.getModule());
			} else {
				userDetails.setUserType("NORMAL_USER");
				request.setAttribute("eventCat", userDetails.getEventCategory());
				request.setAttribute("fundraiserCat", userDetails.getFundraiserCategory());
				request.setAttribute("module", userDetails.getModule());
			}			

			User loginUser = (User) session.getAttribute("loginUser");
			loginUser.setRoleUserId(userDetails.getRoleUserId());
			
			modules = hubCitiService.displayUserModules(loginUser.getHubCitiID(), users.getRoleUserId());
			session.setAttribute("modulesLst", modules);
			
			eventCatObj = hubCitiService.fetchEventCategories(null, loginUser);
			if (null != eventCatObj) {
				session.setAttribute("eventCatLst", eventCatObj.getAlertCatLst());
			}
			
			fundraiserCatObj = hubCitiService.fetchFundraiserCategories(null, loginUser);
			if (null != fundraiserCatObj) {
				session.setAttribute("fundraiserCatLst", fundraiserCatObj.getAlertCatLst());
			}

		} catch (HubCitiServiceException exception) {
			throw new HubCitiServiceException(exception);
		}

		session.setAttribute("previousEmail", userDetails.getEmailId());
		session.setAttribute("userName", userDetails.getUserName());
		request.setAttribute("userSearch", users.getUserSearch());
		request.setAttribute("lowerLimit", users.getLowerLimit());
		model.put("addUser", userDetails);
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "addUser";
	}

	/**
	 * This Method will return all the users created by the hubciti admin.
	 * 
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/activatedeactivateuser.htm", method = RequestMethod.POST)
	public String activateDeactivateUsers(@ModelAttribute("addUser") User user, BindingResult result, HttpServletRequest request, ModelMap model,
			HttpSession session) throws HubCitiServiceException {

		String methodName = "activateDeactivateUsers";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		try {

			status = hubCitiService.activateDeactivateUsers(user.getRoleUserId());

			if (status.equalsIgnoreCase(ApplicationConstants.SUCCESS)) {

				User loginUser = (User) session.getAttribute("loginUser");
				request.setAttribute("responseStatus", "SUCCESS");
				if ("deactivate".equalsIgnoreCase(user.getUserStatus())) {
					request.setAttribute("responeMsg", "User deactivated successfully");
				} else {
					request.setAttribute("responeMsg", "User activated successfully");
				}

				Integer currentPage = 1;
				Integer lowerLimit = user.getLowerLimit();

				User userDetails = new User();
				userDetails.setHubCitiID(loginUser.getHubCitiID());
				userDetails.sethCAdminUserID(loginUser.gethCAdminUserID());
				userDetails.setLowerLimit(lowerLimit);
				userDetails.setUserSearch(user.getUserSearch());

				if (null != lowerLimit && lowerLimit > 0) {
					currentPage = (lowerLimit + 20) / 20;
				}

				fetchUsers(userDetails, currentPage, request, session);

			}
		} catch (HubCitiServiceException exception) {
			throw new HubCitiServiceException(exception);
		}

		User modelUser = new User();
		if (null != user.getUserSearch()) {
			modelUser.setUserSearch(user.getUserSearch());
		}

		model.put("users", modelUser);

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "setupUsers";
	}
}