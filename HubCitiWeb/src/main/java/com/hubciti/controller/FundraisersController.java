package com.hubciti.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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
import org.springframework.web.servlet.view.RedirectView;

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.exception.HubCitiServiceException;
import com.hubciti.common.pojo.AlertCategory;
import com.hubciti.common.pojo.AppSiteDetails;
import com.hubciti.common.pojo.Department;
import com.hubciti.common.pojo.Event;
import com.hubciti.common.pojo.EventDetail;
import com.hubciti.common.pojo.User;
import com.hubciti.common.pojo.UserDetails;
import com.hubciti.common.tags.Pagination;
import com.hubciti.common.util.Utility;
import com.hubciti.service.HubCitiService;
import com.hubciti.validator.FundraiserEventValidator;

@Controller
public class FundraisersController {

	/**
	 * Getting the logger instance.
	 */

	private static final Logger LOG = Logger.getLogger(FundraisersController.class);

	FundraiserEventValidator fundraiserEventValidator;

	@Autowired
	public void setFundraiserEventValidator(FundraiserEventValidator fundraiserEventValidator) {
		this.fundraiserEventValidator = fundraiserEventValidator;
	}
	
	public void fetchFundraisingEvents(Event event, Integer currentPage, HttpServletRequest request, HttpSession session) throws HubCitiServiceException {
		
		EventDetail objEventDetail = null;
		Pagination objPage = null;
		final int recordCount = 20;

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		try {

			User loginUser = (User) session.getAttribute("loginUser");
			event.setScreenName(ApplicationConstants.FUNDRAISERSCREENNAME);

			objEventDetail = hubCitiService.displayEvents(event, loginUser, true);

			if (null == objEventDetail && event.getLowerLimit() > 0) {
				Integer lowerLimit = event.getLowerLimit() - 1;
				event.setLowerLimit(lowerLimit);
				objEventDetail = hubCitiService.displayEvents(event, loginUser, true);
			}

			if (null != objEventDetail && objEventDetail.getTotalSize() > 0) {
				session.setAttribute("fundraiserseventlst", objEventDetail);
				objPage = Utility.getPagination(objEventDetail.getTotalSize(), currentPage, "managefundraisers.htm", recordCount);
				session.setAttribute(ApplicationConstants.PAGINATION, objPage);
			} else
				if (null != event.getEventSearchKey()) {
					request.setAttribute("responseEvntStatus", "INFO");
					request.setAttribute("responeEvntMsg", "No Fundraiser Events found");
					session.setAttribute("fundraiserseventlst", null);
					session.setAttribute(ApplicationConstants.PAGINATION, null);
				} else {
					session.setAttribute("fundraiserseventlst", null);
					session.setAttribute(ApplicationConstants.PAGINATION, null);
				}

			request.setAttribute("lowerLimit", event.getLowerLimit());

		} catch (HubCitiServiceException exception) {

			throw new HubCitiServiceException(exception);
		}
	}

	/**
	 * This method is used to Manage Fundraisers events
	 * 
	 * @param event
	 *            Model attribute
	 * @param result
	 * @param model
	 * @param request
	 *            HttpServletRequest
	 * @param session
	 *            HttpSession
	 * @return view
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/managefundraisers.htm", method = RequestMethod.GET)
	public ModelAndView displayFundraisersEvents(@ModelAttribute("ManageEventForm") Event event, BindingResult result, ModelMap model,
			HttpServletRequest request, HttpSession session) throws HubCitiServiceException {
		
		final String strMethodName = "displayFundraisersEvents";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		
		final String pageFlag = request.getParameter("pageFlag");
		Integer lowerLimit = 0;
		String pageNumber = "0";
		Integer currentPage = 1;
		final int recordCount = 20;
		session.removeAttribute("imageCropPage");
		
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
			} else if(null != event.getLowerLimit()) {
				lowerLimit = event.getLowerLimit();
				currentPage = (lowerLimit + recordCount) / recordCount;
			}

			event.setLowerLimit(lowerLimit);
			fetchFundraisingEvents(event, currentPage, request, session);
			
			if ((event.getEventSearchKey() == null || "".equalsIgnoreCase(event.getEventSearchKey())) && null == session.getAttribute("fundraiserseventlst")) {
				return new ModelAndView(new RedirectView("/HubCiti/addFundraiserEvent.htm"));
			}

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}
		
		model.put("ManageEventForm", event);

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return new ModelAndView("manageFundraisingEvent");
	}
	
	/**
	 * This method is used to delete event.
	 * 
	 * @param cateId
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/deletefndrevent.htm", method = RequestMethod.GET)
	public @ResponseBody
	String deleteEvent(@RequestParam(value = "eventId", required = true) Integer eventId, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws HubCitiServiceException {
		String strResponse = null;
		String strMethodName = "deleteEvent";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		List<Event> eventLst = null;
		EventDetail objEventDetail = null;
		Integer eveId = null;
		try {
			User user = (User) session.getAttribute("loginUser");
			user.setModule(ApplicationConstants.FUNDRAISERSCREENNAME);
			objEventDetail = (EventDetail) session.getAttribute("fundraiserseventlst");
			if (objEventDetail != null) {
				eventLst = objEventDetail.getEventLst();
				if (null != eventLst && !"".equals(eventLst)) {
					for (Event ObjEvent : eventLst) {
						eveId = ObjEvent.getHcEventID();
						if (eveId.intValue() == eventId.intValue()) {
							if (ObjEvent.isMenuItemExist() == true) {
								strResponse = ApplicationConstants.FAILURE;
								break;
							} else {
								strResponse = hubCitiService.deleteEvent(eventId, user);
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
	 * This method is used for adding an event
	 * 
	 * @param catName
	 * @param cateId
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/addFundraiserEvent.htm", method = { RequestMethod.GET, RequestMethod.POST })
	String addFundraiserEvent(@ModelAttribute("screenSettingsForm") Event eventDetails, BindingResult result, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap map) throws HubCitiServiceException {

		String strMethodName = "addFundraiserEvent";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		String returnView = "addupdatefundraiserevent";
		Event objEvent = new Event();
		AlertCategory fundraiserEventCat = null;
		ArrayList<Department> deptList = null;
		List<AppSiteDetails> appSiteDetailsLst = null;
		EventDetail events = null;
		session.setAttribute("imageCropPage", "Events");
		// Minimum crop height and width
		session.setAttribute("minCropHt", 150);
		session.setAttribute("minCropWd", 300);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		try {
			
			request.setAttribute("lowerLimit", eventDetails.getLowerLimit());
			request.setAttribute("eventSearchKey", eventDetails.getEventSearchKey());

			User user = (User) session.getAttribute("loginUser");

			if (null == eventDetails.getEventImageName() || "".equals(eventDetails.getEventImageName())) {
				session.setAttribute("eventImagePreview", ApplicationConstants.DEFAULTIMAGESQR);
			}

			appSiteDetailsLst = hubCitiService.getAppSites(null, user.getHubCitiID(), null);
			if (null != appSiteDetailsLst && !appSiteDetailsLst.isEmpty()) {

				session.setAttribute("eventAppSiteLst", appSiteDetailsLst);
			}

			fundraiserEventCat = hubCitiService.fetchFundraiserEventCategories(user);

			if (null != fundraiserEventCat) {
				session.setAttribute("catLst", fundraiserEventCat.getAlertCatLst());
			}

			deptList = (ArrayList<Department>) hubCitiService.fetchFundraiserDepartments(user);
			session.setAttribute("deptLst", deptList);

			events = hubCitiService.displayEvents(objEvent, user, true);

			if (null != events) {
				if (null != events.getEventLst() && !events.getEventLst().isEmpty()) {
					session.setAttribute("eventLst", events.getEventLst());
				} else {
					request.setAttribute("eventError", "No Events found");
				}
			}

			eventDetails.setIsEventAppsite("No");
			eventDetails.setIsEventTied("No");
			map.put("screenSettingsForm", eventDetails);

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return returnView;
	}
	
	/**
	 * This method is used for editing an event
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/editFundraiserEvent.htm", method = RequestMethod.GET)
	String editFundraiserEvent(@ModelAttribute("screenSettingsForm") Event eventDetails, BindingResult result, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap map) throws HubCitiServiceException {

		String strMethodName = "editFundraiserEvent";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		String returnView = "addupdatefundraiserevent";
		final Integer eventId = eventDetails.getHcEventID();
		Event objEvent = new Event();
		AlertCategory fundraiserEventCat = null;
		ArrayList<Department> deptList = null;
		List<AppSiteDetails> appSiteDetailsLst = null;
		EventDetail events = null;
		session.setAttribute("imageCropPage", "Events");
		// Minimum crop height and width
		session.setAttribute("minCropHt", 150);
		session.setAttribute("minCropWd", 300);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		try {
			
			request.setAttribute("lowerLimit", eventDetails.getLowerLimit());
			request.setAttribute("eventSearchKey", eventDetails.getEventSearchKey());

			User user = (User) session.getAttribute("loginUser");

			appSiteDetailsLst = hubCitiService.getAppSites(null, user.getHubCitiID(), null);
			if (null != appSiteDetailsLst && !appSiteDetailsLst.isEmpty()) {

				session.setAttribute("eventAppSiteLst", appSiteDetailsLst);
			}

			fundraiserEventCat = hubCitiService.fetchFundraiserEventCategories(user);		

			if (null != fundraiserEventCat) {
				session.setAttribute("catLst", fundraiserEventCat.getAlertCatLst());
			}

			deptList = (ArrayList<Department>) hubCitiService.fetchFundraiserDepartments(user);
			session.setAttribute("deptLst", deptList);

			events = hubCitiService.displayEvents(objEvent, user, true);

			if (null != events) {
				if (null != events.getEventLst() && !events.getEventLst().isEmpty()) {
					session.setAttribute("eventLst", events.getEventLst());
				} else {
					request.setAttribute("eventError", "No Events found");
				}
			}

			eventDetails = hubCitiService.fetchFundraiserDetails(eventId);
			
			request.setAttribute("eventTiedIds", eventDetails.getEventTiedIds());
			
			session.setAttribute("eventImagePreview", eventDetails.getEventImagePath());			
			eventDetails.setHcEventID(eventId);
			map.put("screenSettingsForm", eventDetails);

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return returnView;
	}
	
	/**
	 * This method is used for editing an event
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/searchEvents.htm", method = RequestMethod.GET)
	String searchEvents(@ModelAttribute("screenSettingsForm") Event eventDetails, BindingResult result, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap map) throws HubCitiServiceException {

		String strMethodName = "searchEvents";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		String returnView = "addupdatefundraiserevent";
		EventDetail events = null;

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		try {
			
			request.setAttribute("lowerLimit", eventDetails.getLowerLimit());
			request.setAttribute("eventSearchKey", eventDetails.getEventSearchKey());

			User user = (User) session.getAttribute("loginUser");
			eventDetails.setEventSearchKey(eventDetails.getAppSiteSearchKey());
			events = hubCitiService.displayEvents(eventDetails, user, true);
			eventDetails.setEventSearchKey((String)request.getAttribute("eventSearchKey"));

			if (null != events) {
				if (null != events.getEventLst() && !events.getEventLst().isEmpty()) {
					session.setAttribute("eventLst", events.getEventLst());
				} else {
					request.setAttribute("eventError", "No Events found");
					session.removeAttribute("eventLst");
				}
			} else {
				request.setAttribute("eventError", "No Events found");
				session.removeAttribute("eventLst");
			}
			
			map.put("screenSettingsForm", eventDetails);

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return returnView;
	}

	/**
	 * This method is used for adding an event
	 * 
	 * @param catName
	 * @param cateId
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/saveFundraiserEvent.htm", method = RequestMethod.POST)
	ModelAndView saveUpdateFundraiserEvent(@ModelAttribute("screenSettingsForm") Event eventDetails, BindingResult result,
			HttpServletRequest request, HttpServletResponse httpresponse, HttpSession session, ModelMap map) throws HubCitiServiceException {

		String strMethodName = "saveUpdateFundraiserEvent";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		String returnView = "addupdatefundraiserevent";
		String response = null;
		String compDate = null;
		eventDetails.setHiddenCategory(eventDetails.getEventCategory());
		Date currentDate = new Date();
		Boolean validStartDate = false;
		Boolean validEndDate = false;
		User user = (User) session.getAttribute("loginUser");

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		try {
			
			request.setAttribute("lowerLimit", eventDetails.getLowerLimit());
			request.setAttribute("eventSearchKey", eventDetails.getEventSearchKey());
			request.setAttribute("eventTiedIds", eventDetails.getEventTiedIds());

			fundraiserEventValidator.validate(eventDetails, result);

			if (result.hasErrors()) {
				return new ModelAndView(returnView);
			} else {
				validStartDate = Utility.isValidDate(eventDetails.getEventDate());
				validEndDate = Utility.isValidDate(eventDetails.getEventEDate());

				if (!"".equals(Utility.checkNull(eventDetails.getEventDate()))) {
					if (validStartDate && (null == eventDetails.getHcEventID() || "".equals(eventDetails.getHcEventID()))) {
						compDate = Utility.compareCurrentDate(eventDetails.getEventDate(), currentDate);
						if (null != compDate) {
							fundraiserEventValidator.validateDates(eventDetails, result, ApplicationConstants.DATENOGSTARTCURRENT);
						}
					} else
						if (!validStartDate) {
							fundraiserEventValidator.validateDates(eventDetails, result, ApplicationConstants.VALIDDATE);
						}
				}
				if (validEndDate) {
					if (validStartDate) {
						compDate = Utility.compareDate(eventDetails.getEventDate(), eventDetails.getEventEDate());
						if (null != compDate) {
							fundraiserEventValidator.validateDates(eventDetails, result, ApplicationConstants.DATENOGAFTER);
						}
					} else {
						compDate = Utility.compareCurrentDate(eventDetails.getEventEDate(), currentDate);
						if (null != compDate) {
							fundraiserEventValidator.validateDates(eventDetails, result, ApplicationConstants.DATENOGENDCURRENT);
						}
					}

				} else
					if (!"".equals(eventDetails.getEventEDate())) {
						fundraiserEventValidator.validateDates(eventDetails, result, ApplicationConstants.VALIDEDATE);
					}

				if (!"".equals(Utility.checkNull(eventDetails.getMoreInfoURL()))) {
					if (!Utility.validateURL(eventDetails.getMoreInfoURL())) {
						fundraiserEventValidator.validate(eventDetails, result, ApplicationConstants.INVALIDEVENTURL);
					}
				}
				if (!"".equals(Utility.checkNull(eventDetails.getPurchaseProducts()))) {
					if (!Utility.validateURL(eventDetails.getPurchaseProducts())) {
						fundraiserEventValidator.validate(eventDetails, result, ApplicationConstants.INVALIDURL);
					}
				}
				if (!"".equals(Utility.checkNull(eventDetails.getFundraisingGoal()))) {
					if (!Utility.isValidPrice(eventDetails.getFundraisingGoal())) {
						fundraiserEventValidator.validatePrice(eventDetails, result, ApplicationConstants.INVALIDPRICE, "fundraisingGoal");
					}
				}
				if (!"".equals(Utility.checkNull(eventDetails.getCurrentLevel()))) {
					if (!Utility.isValidPrice(eventDetails.getCurrentLevel())) {
						fundraiserEventValidator.validatePrice(eventDetails, result, ApplicationConstants.INVALIDPRICE, "currentLevel");
					}
				}

				if (result.hasErrors()) {
					return new ModelAndView(returnView);
				}

				response = hubCitiService.saveUpdateFundraiserEventDeatils(eventDetails, user);

				if (null != response && !"".equals(response) && response.equals(ApplicationConstants.SUCCESS)) {
					
					returnView = "manageFundraisingEvent";
					request.setAttribute("responseEvntStatus", response);					
					Event event = new Event();
					Integer currentPage = 1;
					Integer lowerLimit = eventDetails.getLowerLimit();
					
					if (null == eventDetails.getHcEventID() || "".equals(eventDetails.getHcEventID())) {
						request.setAttribute("responeEvntMsg", "Fundraiser Event Created Successfully");
						event.setLowerLimit(0);
					} else {
						request.setAttribute("responeEvntMsg", "Fundraiser Event Updated Successfully");
						event.setEventSearchKey(eventDetails.getEventSearchKey());
						event.setLowerLimit(lowerLimit);
						if(null != lowerLimit && lowerLimit > 0) {
							currentPage = (lowerLimit + 20) / 20;
						}
					}
					
					fetchFundraisingEvents(event, currentPage, request, session);
				}
			}
			
			map.put("ManageEventForm", eventDetails);

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return new ModelAndView(returnView);
	}

	/**
	 * This method is used to add departments
	 * 
	 * @param catName
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/adddept.htm", method = RequestMethod.GET)
	public @ResponseBody
	String addFundraiserDept(@RequestParam(value = "deptName", required = true) String deptName, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws HubCitiServiceException {

		String strMethodName = "addFundraiserDept";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		String strResponse = null;

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		try {
			User user = (User) session.getAttribute("loginUser");
			strResponse = hubCitiService.addFundraiserDept(deptName, user);			

			if (null != strResponse && !strResponse.equals(ApplicationConstants.DEPARTMENTEXISTS)) {
				List<Department> departments = (ArrayList<Department>) session.getAttribute("deptLst");
				Department department = new Department();
				department.setDeptId(Integer.parseInt(strResponse));
				department.setDeptName(deptName);
				if (null != departments) {
					departments.add(department);
				} else {
					departments = new ArrayList<Department>();
					departments.add(department);
				}
				session.setAttribute("deptLst", departments);
			}

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strResponse;
	}

}
