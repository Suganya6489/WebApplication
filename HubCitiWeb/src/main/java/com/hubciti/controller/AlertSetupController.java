package com.hubciti.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.hubciti.common.pojo.AlertCategory;
import com.hubciti.common.pojo.Alerts;
import com.hubciti.common.pojo.AlertsDetails;
import com.hubciti.common.pojo.Category;
import com.hubciti.common.pojo.ScreenSettings;
import com.hubciti.common.pojo.Severity;
import com.hubciti.common.pojo.User;
import com.hubciti.common.tags.Pagination;
import com.hubciti.common.util.Utility;
import com.hubciti.service.HubCitiService;
import com.hubciti.validator.AlertValidator;

/**
 * This controller is used to handle alert setup functionalities.
 * 
 * @author shyamsundara_hm
 */
@Controller
public class AlertSetupController {

	/**
	 * Getting the logger Instance.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(AlertSetupController.class);

	/**
	 * variable of type AlertValidator.
	 */
	AlertValidator alertValidator;

	/**
	 * Setter for alertValidator.
	 * 
	 * @param AlertValidator
	 *            the alertValidator to set
	 */
	@Autowired
	public void setAlertValidator(AlertValidator alertValidator) {
		this.alertValidator = alertValidator;
	}

	@RequestMapping(value = "/displayalertcat.htm", method = RequestMethod.GET)
	public String displayAlertCategory(@ModelAttribute("alertcategoryform") Category category, BindingResult result, HttpServletRequest request,
			HttpSession session, ModelMap model) throws HubCitiServiceException {
		String strMethodName = "";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		String strViewName = "displayalertcat";
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.SETUPALERTS);
		AlertCategory objAlertCategory = null;
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		Category objCategory = new Category();
		final String pageFlag = request.getParameter("pageFlag");
		Integer lowerLimit = 0;
		String pageNumber = "0";
		int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;

		try {
			session.removeAttribute("alertcatlst");

			User user = (User) session.getAttribute("loginUser");

			if (null != pageFlag && "true".equals(pageFlag)) {
				pageNumber = request.getParameter("pageNumber");
				final Pagination pageSess = (Pagination) session.getAttribute("pagination");
				if (Integer.valueOf(pageNumber) != 0) {
					currentPage = Integer.valueOf(pageNumber);
					final int number = Integer.valueOf(currentPage) - 1;
					final int pageSize = pageSess.getPageRange();
					lowerLimit = pageSize * Integer.valueOf(number);
				}
			} else {
				currentPage = (lowerLimit + recordCount) / recordCount;
			}

			objCategory.setLowerLimit(lowerLimit);
			if (null != category) {
				if (null != category.getCatName() && !"".equals(category.getCatName())) {
					objCategory.setCatName(category.getCatName());
					request.setAttribute("searchAltCat", "searchAltCat");
				}
			}

			objAlertCategory = hubCitiService.fetchAlertCategories(objCategory, user);

			if (null != objAlertCategory) {

				if (null != objAlertCategory.getTotalSize()) {
					objPage = Utility.getPagination(objAlertCategory.getTotalSize(), currentPage, "displayalertcat.htm", recordCount);
					session.setAttribute(ApplicationConstants.PAGINATION, objPage);
				} else {
					session.removeAttribute(ApplicationConstants.PAGINATION);
				}
			}
			model.put("alertcategoryform", category);
			session.setAttribute("alertcatlst", objAlertCategory);

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strViewName;
	}

	/**
	 * This method is used to add alert categories
	 * 
	 * @param catName
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/addalertcat.htm", method = RequestMethod.GET)
	public @ResponseBody
	String addAlertCategory(@RequestParam(value = "catName", required = true) String catName, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws HubCitiServiceException {
		String strResponse = null;
		String strMethodName = "addAlertCategory";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		try {
			User user = (User) session.getAttribute("loginUser");
			
			/*To fix XSS issue*/
			catName = Utility.getXssFreeString(catName);
			if(null == catName || "".equals(catName.trim()))	{
				return "CategoryExists";
			}
			
			strResponse = hubCitiService.addAlertCategory(catName, user);

			@SuppressWarnings("unchecked")
			List<Category> categories = (ArrayList<Category>) session.getAttribute("categories");

			Category category = new Category();

			if (null != strResponse && !strResponse.equals(ApplicationConstants.ALERTCATEXISTS)) {
				category.setCatId(Integer.parseInt(strResponse));
				category.setCatName(catName);
				if (null != categories) {
					categories.add(category);
				} else {
					categories = new ArrayList<Category>();
					categories.add(category);
				}
				session.setAttribute("categories", categories);

			}

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strResponse;
	}

	/**
	 * This method is used to delete alert category
	 * 
	 * @param cateId
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/deletealertcate.htm", method = RequestMethod.GET)
	public @ResponseBody
	String deleteAlertCategory(@RequestParam(value = "cateId", required = true) int cateId, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws HubCitiServiceException {
		String strResponse = null;
		String strMethodName = "deleteAlertCategory";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		AlertCategory objAlertCategory = null;
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		List<Category> categoryLst = null;

		try {
			User user = (User) session.getAttribute("loginUser");

			objAlertCategory = (AlertCategory) session.getAttribute("alertcatlst");
			if (objAlertCategory != null) {
				categoryLst = objAlertCategory.getAlertCatLst();
				if (null != categoryLst && !"".equals(categoryLst)) {
					for (Category objCate : categoryLst) {
						int catId = objCate.getCatId();
						if (catId == cateId) {
							if (objCate.isAssociateCate() == true) {
								strResponse = ApplicationConstants.ALERTCATEGORYASSCOIATED;
								break;
							} else {
								strResponse = hubCitiService.deleteAlertCategory(cateId, user);
								break;
							}

						}
					}
				}

			}

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strResponse;
	}

	/**
	 * This method is used to update alert categories.
	 * 
	 * @param catName
	 * @param cateId
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/updatealertcat.htm", method = RequestMethod.GET)
	public @ResponseBody
	String updateAlertCategory(@RequestParam(value = "catName", required = true) String catName,
			@RequestParam(value = "cateId", required = true) int cateId, HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws HubCitiServiceException {
		String strResponse = null;
		String strMethodName = "updateAlertCategory";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		Category objCategory = new Category();
		try {
			User user = (User) session.getAttribute("loginUser");
			if (null != catName && !"".equals(catName)) {
				objCategory.setCatName(catName);
			}
			if (!"".equals(cateId)) {
				objCategory.setCatId(cateId);
			}
			strResponse = hubCitiService.updateAlertCategory(objCategory, user);
		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strResponse;
	}

	/**
	 * this method is used to search alert category
	 * 
	 * @param category
	 * @param result
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 * @throws HubCitiServiceException
	 */

	@RequestMapping(value = "/searchalertcat.htm", method = RequestMethod.POST)
	public String searchAlertCategory(@ModelAttribute("alertcategoryform") Category category, BindingResult result, HttpServletRequest request,
			HttpSession session, Model model) throws HubCitiServiceException {
		String strMethodName = "searchAlertCategory";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		String strViewName = "displayalertcat";
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.SETUPALERTS);
		AlertCategory objAlertCategory = null;
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		Category objCategory = new Category();
		try {
			session.removeAttribute("alertcatlst");
			User user = (User) session.getAttribute("loginUser");
			if (null != category && !"".equals(category)) {
				objCategory.setCatName(category.getCatName());
			}
			objAlertCategory = hubCitiService.fetchAlertCategories(objCategory, user);

			session.setAttribute("alertcatlst", objAlertCategory);

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strViewName;
	}

	/**
	 * This method is to display alerts which are created by the user.
	 * 
	 * @param anythingPageDetails
	 * @param result
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/displayalerts.htm", method = RequestMethod.GET)
	public final String displayAlerts(@ModelAttribute("screenSettingsForm") ScreenSettings anythingPageDetails, BindingResult result,
			HttpServletRequest request, HttpSession session, ModelMap model) throws HubCitiServiceException {
		final String methodName = "displayAlerts";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.SETUPALERTS);

		final String searchKey = anythingPageDetails.getSearchKey();
		final String pageFlag = request.getParameter("pageFlag");
		Integer lowerLimit = 0;
		String pageNumber = "0";
		int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;
		final User loginUser = (User) session.getAttribute("loginUser");
		AlertsDetails alerts = null;
		List<Severity> severities = null;
		AlertCategory alertCategory = null;
		final Category category = new Category();

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		if (null != anythingPageDetails.getLowerLimit()) {
			lowerLimit = anythingPageDetails.getLowerLimit();
		}
		if (null != pageFlag && "true".equals(pageFlag)) {
			pageNumber = request.getParameter("pageNumber");
			final Pagination pageSess = (Pagination) session.getAttribute(ApplicationConstants.PAGINATION);
			if (Integer.valueOf(pageNumber) != 0) {
				currentPage = Integer.valueOf(pageNumber);
				final int number = Integer.valueOf(currentPage) - 1;
				final int pageSize = pageSess.getPageRange();
				lowerLimit = pageSize * Integer.valueOf(number);
			}
		} else {
			currentPage = (lowerLimit + recordCount) / recordCount;
		}

		alerts = hubCitiService.displaySearchAlerts(loginUser.gethCAdminUserID(), loginUser.getHubCitiID(), searchKey, lowerLimit);

		if (null != alerts) {
			if ((null == searchKey || "".equals(searchKey)) && alerts.getAlerts().isEmpty()) {
				session.removeAttribute(ApplicationConstants.PAGINATION);
				session.removeAttribute("alerts");
				if (null == searchKey || "".equals(searchKey)) {
					severities = hubCitiService.fetchAlertSeverities();
					alertCategory = hubCitiService.fetchAlertCategories(category, loginUser);

					session.setAttribute("severities", severities);
					if (null != alertCategory) {
						session.setAttribute("categories", alertCategory.getAlertCatLst());
					}

					request.setAttribute(ApplicationConstants.LOWERLIMIT, lowerLimit);
					session.setAttribute(ApplicationConstants.SEARCHKEY, searchKey);
					session.setAttribute("screenType", "add");
					model.put("addAlerts", new Alerts());

					LOG.info(ApplicationConstants.METHODEND + methodName);
					return "addAlerts";
				}
			} else
				if (alerts.getAlerts().isEmpty()) {
					session.removeAttribute(ApplicationConstants.PAGINATION);
					session.removeAttribute("alerts");
					request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "No Alerts Found");
					request.setAttribute(ApplicationConstants.RESPONSESTATUS, "INFO");
				} else {
					session.setAttribute("alerts", alerts.getAlerts());
					objPage = Utility.getPagination(alerts.getTotalSize(), currentPage, "displayalerts.htm", recordCount);
					session.setAttribute(ApplicationConstants.PAGINATION, objPage);
				}
		}

		request.setAttribute(ApplicationConstants.LOWERLIMIT, lowerLimit);
		session.setAttribute(ApplicationConstants.SEARCHKEY, searchKey);
		model.put("alerts", new Alerts());

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "displayAlerts";
	}

	/**
	 * This method will return add alerts screen.
	 * 
	 * @param alerts
	 * @param result
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/addalerts.htm", method = RequestMethod.GET)
	public final String addAlerts(@ModelAttribute("alerts") Alerts alerts, BindingResult result, HttpServletRequest request, HttpSession session,
			ModelMap model) throws HubCitiServiceException {
		final String methodName = "addAlerts";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.SETUPALERTS);

		final User loginUser = (User) session.getAttribute("loginUser");
		AlertCategory alertCategory = null;
		List<Severity> severities = null;
		final Category category = new Category();

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		severities = hubCitiService.fetchAlertSeverities();
		alertCategory = hubCitiService.fetchAlertCategories(category, loginUser);

		session.setAttribute("severities", severities);

		if (null != alertCategory) {
			session.setAttribute("categories", alertCategory.getAlertCatLst());
		}

		request.setAttribute(ApplicationConstants.LOWERLIMIT, alerts.getLowerLimit());
		session.setAttribute("screenType", "add");
		session.removeAttribute("alertId");
		model.put("addAlerts", new Alerts());

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "addAlerts";
	}

	/**
	 * This method will save alert details.
	 * 
	 * @param alerts
	 * @param result
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/savealerts.htm", method = RequestMethod.POST)
	public final ModelAndView saveAlerts(@ModelAttribute("addAlerts") Alerts alerts, BindingResult result, HttpServletRequest request,
			ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "saveAlerts";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		request.setAttribute(ApplicationConstants.LOWERLIMIT, alerts.getLowerLimit());
		String status = null;
		String sTime = null;
		String endTime = null;
		AlertsDetails alertDetails = null;
		final Integer lowerLimit = 0;
		final int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;
		final String searchKey = null;
		String viewName = null;

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		final User loginUser = (User) session.getAttribute("loginUser");

		/* To fix XSS issue */
		alerts.setTitle(Utility.getXssFreeString(alerts.getTitle()));
		
		alertValidator.validate(alerts, result);

		result = validateDates(alerts, result);

		if (result.hasErrors()) {
			return new ModelAndView("addAlerts");
		} else {
			sTime = String.valueOf(alerts.getStartTimeHrs()) + ":" + String.valueOf(alerts.getStartTimeMins());
			endTime = String.valueOf(alerts.getEndTimeHrs()) + ":" + String.valueOf(alerts.getEndTimeMins());

			alerts.setStartTime(sTime);
			alerts.setEndTime(endTime);
			status = hubCitiService.saveAlerts(alerts, loginUser);

			if (null != status && status.equals(ApplicationConstants.SUCCESS)) {
				request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "Alert Created Successfully");
				request.setAttribute(ApplicationConstants.RESPONSESTATUS, status);

				alertDetails = hubCitiService.displaySearchAlerts(loginUser.gethCAdminUserID(), loginUser.getHubCitiID(), searchKey, lowerLimit);

				if (null != alertDetails && null != alertDetails.getTotalSize()) {
					session.setAttribute("alerts", alertDetails.getAlerts());
					objPage = Utility.getPagination(alertDetails.getTotalSize(), currentPage, "displayalerts.htm", recordCount);
					session.setAttribute(ApplicationConstants.PAGINATION, objPage);
				}

				request.setAttribute(ApplicationConstants.LOWERLIMIT, lowerLimit);
				session.setAttribute(ApplicationConstants.SEARCHKEY, searchKey);
				model.put("alerts", new Alerts());
				viewName = "displayAlerts";
			} else {
				request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, status);
				request.setAttribute(ApplicationConstants.RESPONSESTATUS, status);
				viewName = "addAlerts";
			}

		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return new ModelAndView(viewName);
	}

	/**
	 * This method will return alert edit screen.
	 * 
	 * @param alerts
	 * @param result
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/editalerts.htm", method = RequestMethod.GET)
	public final String editAlerts(@ModelAttribute("alerts") Alerts alerts, BindingResult result, HttpServletRequest request, HttpSession session,
			ModelMap model) throws HubCitiServiceException {
		final String methodName = "editAlerts";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.SETUPALERTS);

		final User loginUser = (User) session.getAttribute("loginUser");
		AlertCategory alertCategory = null;
		List<Severity> severities = null;
		final Category category = new Category();
		Alerts alertDetails = null;

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		severities = hubCitiService.fetchAlertSeverities();
		alertCategory = hubCitiService.fetchAlertCategories(category, loginUser);

		alertDetails = hubCitiService.fetchAlertDetails(alerts.getAlertId());

		if (null != alertDetails.getStartTime()) {
			final String startTime = alertDetails.getStartTime();
			final String[] tempStartTime = startTime.split(":");
			alertDetails.setStartTimeHrs(tempStartTime[0]);
			alertDetails.setStartTimeMins(tempStartTime[1]);
		}

		if (null != alertDetails.getEndTime()) {
			final String endTime = alertDetails.getEndTime();
			final String[] tempEndTime = endTime.split(":");
			alertDetails.setEndTimeHrs(tempEndTime[0]);
			alertDetails.setEndTimeMins(tempEndTime[1]);
		}

		session.setAttribute("severities", severities);
		if (null != alertCategory) {
			session.setAttribute("categories", alertCategory.getAlertCatLst());
		}
		request.setAttribute(ApplicationConstants.LOWERLIMIT, alerts.getLowerLimit());
		session.setAttribute("alertId", alertDetails.getAlertId());
		session.setAttribute("screenType", "edit");
		session.setAttribute("menuItemExist", alertDetails.getMenuItemExist());
		model.put("addAlerts", alertDetails);

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "addAlerts";
	}

	/**
	 * This method will update Alert details.
	 * 
	 * @param alerts
	 * @param result
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/updatealerts.htm", method = RequestMethod.POST)
	public final ModelAndView updateAlerts(@ModelAttribute("addAlerts") Alerts alerts, BindingResult result, HttpServletRequest request,
			ModelMap model, HttpSession session) throws HubCitiServiceException {
		final String methodName = "updateAlerts";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		request.setAttribute(ApplicationConstants.LOWERLIMIT, alerts.getLowerLimit());
		String status = null;
		String compDate = null;
		final Date currentDate = new Date();
		String searchKey = null;
		Integer lowerLimit = 0;
		int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;
		AlertsDetails alertDetails = null;
		String viewName = null;
		String sTime = null;
		String endTime = null;

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		final User loginUser = (User) session.getAttribute("loginUser");

		alertValidator.validate(alerts, result);

		result = validateDates(alerts, result);

		if (result.hasErrors()) {
			return new ModelAndView("addAlerts");
		}

		if (!"".equals(Utility.checkNull(alerts.getEndDate()))) {
			compDate = Utility.compareCurrentDate(alerts.getEndDate(), currentDate);
			if (null != compDate) {
				alertValidator.validateDates(alerts, result, ApplicationConstants.DATEENDCURRENT);
			}
		}

		if (result.hasErrors()) {
			return new ModelAndView("addAlerts");
		}

		else {
			sTime = String.valueOf(alerts.getStartTimeHrs()) + ":" + String.valueOf(alerts.getStartTimeMins());
			endTime = String.valueOf(alerts.getEndTimeHrs()) + ":" + String.valueOf(alerts.getEndTimeMins());

			alerts.setStartTime(sTime);
			alerts.setEndTime(endTime);
			status = hubCitiService.updateAlerts(alerts, loginUser);

			if (null != status && status.equals(ApplicationConstants.SUCCESS)) {
				request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "Alert Updated Successfully");
				request.setAttribute(ApplicationConstants.RESPONSESTATUS, status);

				session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.SETUPALERTS);

				if (null != alerts.getLowerLimit()) {
					lowerLimit = alerts.getLowerLimit();
					currentPage = (lowerLimit + recordCount) / recordCount;
				}

				if (null != alerts.getSearchKey()) {
					searchKey = alerts.getSearchKey();
				}

				alertDetails = hubCitiService.displaySearchAlerts(loginUser.gethCAdminUserID(), loginUser.getHubCitiID(), searchKey, lowerLimit);

				if (null != alerts && null != alertDetails.getTotalSize()) {
					session.setAttribute("alerts", alertDetails.getAlerts());
					objPage = Utility.getPagination(alertDetails.getTotalSize(), currentPage, "displayalerts.htm", recordCount);
					session.setAttribute(ApplicationConstants.PAGINATION, objPage);
				}

				request.setAttribute(ApplicationConstants.LOWERLIMIT, lowerLimit);
				session.setAttribute(ApplicationConstants.SEARCHKEY, searchKey);
				model.put("alerts", new Alerts());
				viewName = "displayAlerts";
			} else {
				request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, status);
				request.setAttribute(ApplicationConstants.RESPONSESTATUS, status);
				viewName = "addAlerts";
			}

		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return new ModelAndView(viewName);
	}

	/**
	 * This method is used to delete alerts.
	 * 
	 * @param alerts
	 * @param result
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/deletealert.htm", method = RequestMethod.POST)
	public final String deleteAlert(@ModelAttribute("alerts") Alerts alerts, BindingResult result, HttpServletRequest request, ModelMap model,
			HttpSession session) throws HubCitiServiceException {
		final String methodName = "deleteAlert";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		final String searchKey = null;
		Integer lowerLimit = 0;
		int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;
		String status = null;
		AlertsDetails alertDetails = null;

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		final User loginUser = (User) session.getAttribute("loginUser");

		status = hubCitiService.deleteAlerts(alerts.getAlertId(), loginUser);

		if (null != status && status.equals(ApplicationConstants.SUCCESS)) {
			request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "Alert Deleted Successfully");
			request.setAttribute(ApplicationConstants.RESPONSESTATUS, status);

			if (null != alerts.getLowerLimit()) {
				lowerLimit = alerts.getLowerLimit();
				currentPage = (lowerLimit + recordCount) / recordCount;
			}

			alertDetails = hubCitiService.displaySearchAlerts(loginUser.gethCAdminUserID(), loginUser.getHubCitiID(), searchKey, lowerLimit);

			if (alertDetails.getAlerts().isEmpty() && lowerLimit > 0) {
				lowerLimit = lowerLimit - 20;
				currentPage = (lowerLimit + recordCount) / recordCount;
				alertDetails = hubCitiService.displaySearchAlerts(loginUser.gethCAdminUserID(), loginUser.getHubCitiID(), searchKey, lowerLimit);
			}

			if (null != alerts) {
				session.setAttribute("alerts", alertDetails.getAlerts());
				if (null != alertDetails.getTotalSize()) {
					objPage = Utility.getPagination(alertDetails.getTotalSize(), currentPage, "displayalerts.htm", recordCount);
					session.setAttribute(ApplicationConstants.PAGINATION, objPage);
				}
			}

			session.setAttribute(ApplicationConstants.SEARCHKEY, searchKey);
			request.setAttribute(ApplicationConstants.LOWERLIMIT, alerts.getLowerLimit());
			model.put("alerts", new Alerts());
		} else {
			request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, status);
			request.setAttribute(ApplicationConstants.RESPONSESTATUS, status);
		}

		return "displayAlerts";
	}

	/**
	 * This ModelAttribute sort start and end minutes property.
	 * 
	 * @return sortedMap.
	 * @throws HubCitiServiceException
	 *             will be thrown.
	 */
	@SuppressWarnings("rawtypes")
	@ModelAttribute("StartMinutes")
	public final Map<String, String> populatemapDealStartMins() throws HubCitiServiceException {
		final HashMap<String, String> mapDealStartHrs = new HashMap<String, String>();
		for (int i = 0; i <= 55; i++) {
			if (i < 10) {
				mapDealStartHrs.put(ApplicationConstants.ZERO + i, ApplicationConstants.ZERO + i);
				i = i + 4;
			} else {
				mapDealStartHrs.put(String.valueOf(i), String.valueOf(i));
				i = i + 4;
			}
		}
		@SuppressWarnings("unused")
		final Iterator iterator = mapDealStartHrs.entrySet().iterator();
		@SuppressWarnings("unchecked")
		final Map<String, String> sortedMap = Utility.sortByComparator(mapDealStartHrs);
		return sortedMap;
	}

	/**
	 * This ModelAttribute sort start and end hours property.
	 * 
	 * @return sortedMap.
	 * @throws HubCitiServiceException
	 *             will be thrown.
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	@ModelAttribute("StartHours")
	public final Map<String, String> populateDealStartHrs() throws HubCitiServiceException {
		final HashMap<String, String> mapDealStartHrs = new HashMap<String, String>();
		for (int i = 0; i < 24; i++) {
			if (i < 10) {
				mapDealStartHrs.put(ApplicationConstants.ZERO + i, ApplicationConstants.ZERO + i);
			} else {
				mapDealStartHrs.put(String.valueOf(i), String.valueOf(i));
			}
		}
		final Iterator iterator = mapDealStartHrs.entrySet().iterator();
		@SuppressWarnings("unchecked")
		final Map<String, String> sortedMap = Utility.sortByComparator(mapDealStartHrs);
		return sortedMap;
	}

	/**
	 * This Method is to validate start and end date.
	 * 
	 * @param alerts
	 * @param result
	 * @return
	 * @throws HubCitiServiceException
	 */
	public final BindingResult validateDates(Alerts alerts, BindingResult result) throws HubCitiServiceException {
		String compDate = null;
		final Date currentDate = new Date();
		Boolean validStartDate = false;
		Boolean validEndDate = false;

		if (!"".equals(Utility.checkNull(alerts.getStartDate()))) {
			validStartDate = Utility.isValidDate(alerts.getStartDate());

			if (validStartDate && (null == alerts.getAlertId() || "".equals(alerts.getAlertId()))) {
				compDate = Utility.compareCurrentDate(alerts.getStartDate(), currentDate);
				if (null != compDate) {
					alertValidator.validateDates(alerts, result, ApplicationConstants.DATESTARTCURRENT);
				}
			} else
				if (!validStartDate) {
					alertValidator.validateDates(alerts, result, ApplicationConstants.VALIDSTARTDATE);
				}
		}
		if (!"".equals(Utility.checkNull(alerts.getEndDate()))) {
			validEndDate = Utility.isValidDate(alerts.getEndDate());
			validStartDate = Utility.isValidDate(alerts.getStartDate());

			if (validEndDate) {
				if (validStartDate) {
					compDate = Utility.compareDate(alerts.getStartDate(), alerts.getEndDate());
					if (null != compDate) {
						alertValidator.validateDates(alerts, result, ApplicationConstants.DATEAFTER);
					}
				} else {
					compDate = Utility.compareCurrentDate(alerts.getEndDate(), currentDate);
					if (null != compDate) {
						alertValidator.validateDates(alerts, result, ApplicationConstants.DATEENDCURRENT);
					}
				}
			} else {
				alertValidator.validateDates(alerts, result, ApplicationConstants.VALIDENDDATE);
			}
		}
		return result;
	}
}
