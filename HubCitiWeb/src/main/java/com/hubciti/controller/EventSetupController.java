package com.hubciti.controller;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
import com.hubciti.common.pojo.Category;
import com.hubciti.common.pojo.Event;
import com.hubciti.common.pojo.EventDetail;
import com.hubciti.common.pojo.RetailLocation;
import com.hubciti.common.pojo.SearchZipCode;
import com.hubciti.common.pojo.State;
import com.hubciti.common.pojo.User;
import com.hubciti.common.tags.Pagination;
import com.hubciti.common.util.Utility;
import com.hubciti.service.HubCitiService;
import com.hubciti.validator.EventValidator;

/**
 * This class is used to handle Event related functionalities.
 * 
 * @author shyamsundara_hm
 */
@Controller
public class EventSetupController {

	/**
	 * Getting the logger instance.
	 */

	private static final Logger LOG = Logger
			.getLogger(EventSetupController.class);

	EventValidator eventValidator;

	@Autowired
	public void setEventValidator(EventValidator eventValidator) {
		this.eventValidator = eventValidator;
	}

	/**
	 * This ModelAttribute sort Deal start and end minutes property.
	 * 
	 * @return sortedMap.
	 * @throws ScanSeeServiceException
	 *             will be thrown.
	 */
	@SuppressWarnings("rawtypes")
	@ModelAttribute("StartMinutes")
	public Map<String, String> populatemapDealStartMins()
			throws HubCitiServiceException {
		final HashMap<String, String> mapDealStartHrs = new HashMap<String, String>();
		for (int i = 0; i <= 55; i++) {
			if (i < 10) {
				mapDealStartHrs.put(ApplicationConstants.ZERO + i,
						ApplicationConstants.ZERO + i);
				i = i + 4;
			} else {
				mapDealStartHrs.put(String.valueOf(i), String.valueOf(i));
				i = i + 4;
			}
		}
		@SuppressWarnings("unused")
		final Iterator iterator = mapDealStartHrs.entrySet().iterator();
		@SuppressWarnings("unchecked")
		final Map<String, String> sortedMap = Utility
				.sortByComparator(mapDealStartHrs);
		return sortedMap;
	}

	/**
	 * This ModelAttribute sort Deal start and end hours property.
	 * 
	 * @return sortedMap.
	 * @throws ScanSeeServiceException
	 *             will be thrown.
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	@ModelAttribute("StartHours")
	public Map<String, String> populateDealStartHrs()
			throws HubCitiServiceException {
		final HashMap<String, String> mapDealStartHrs = new HashMap<String, String>();
		for (int i = 0; i < 24; i++) {
			if (i < 10) {
				mapDealStartHrs.put(ApplicationConstants.ZERO + i,
						ApplicationConstants.ZERO + i);
			} else {
				mapDealStartHrs.put(String.valueOf(i), String.valueOf(i));
			}
		}
		final Iterator iterator = mapDealStartHrs.entrySet().iterator();
		@SuppressWarnings("unchecked")
		final Map<String, String> sortedMap = Utility
				.sortByComparator(mapDealStartHrs);
		return sortedMap;
	}

	/**
	 * This method is used to display event categories
	 * 
	 * @param category
	 * @param result
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */

	@RequestMapping(value = "/displayeventcate.htm", method = RequestMethod.GET)
	public String displayEventCategory(
			@ModelAttribute("EventCategoryForm") Category category,
			BindingResult result, ModelMap model, HttpServletRequest request,
			HttpSession session) throws HubCitiServiceException {
		String strMethodName = "displayEventCategory";
		String strViewName = "displayeventcate";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		session.setAttribute(ApplicationConstants.MENUNAME,
				ApplicationConstants.SETUPEVENTS);
		Category objCategory = new Category();
		AlertCategory objAlertCategory = null;
		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");

		// for pagination.
		final String pageFlag = request.getParameter("pageFlag");
		Integer lowerLimit = 0;
		String pageNumber = "0";
		int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;

		try {
			session.removeAttribute("eventcatlst");

			User user = (User) session.getAttribute("loginUser");

			if (null != pageFlag && "true".equals(pageFlag)) {
				pageNumber = request.getParameter("pageNumber");
				final Pagination pageSess = (Pagination) session
						.getAttribute("pagination");
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
				if (null != category.getCatName()
						&& !"".equals(category.getCatName())) {
					objCategory.setCatName(category.getCatName());
					request.setAttribute("searchEvntCat", "searchEvntCat");
				}
			}

			objAlertCategory = hubCitiService.fetchEventCategories(objCategory,
					user);

			if (null != objAlertCategory) {

				if (null != objAlertCategory.getTotalSize()) {
					objPage = Utility.getPagination(
							objAlertCategory.getTotalSize(), currentPage,
							"displayeventcate.htm", recordCount);
					session.setAttribute("pagination", objPage);
				} else {
					session.removeAttribute("pagination");
				}
			}
			model.put("EventCategoryForm", category);
			session.setAttribute("eventcatlst", objAlertCategory);

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strViewName;
	}

	/**
	 * This method is used to add event categories
	 * 
	 * @param catName
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addeventcat.htm", method = RequestMethod.GET)
	public @ResponseBody
	String addEventCategory(
			@RequestParam(value = "catName", required = true) String catName,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws HubCitiServiceException {
		String strResponse = "";
		String strMethodName = "addEventCategory";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");

		try {
			List<Category> eventCatLst = (ArrayList<Category>) session
					.getAttribute("eventCatLst");
			User user = (User) session.getAttribute("loginUser");
			strResponse = hubCitiService.addEventCategory(catName, user);
			Category category = new Category();
			if (null != strResponse
					&& !strResponse.equals(ApplicationConstants.ALERTCATEXISTS)) {

				category.setCatId(Integer.parseInt(strResponse));
				category.setCatName(catName);
				if (null != eventCatLst) {
					eventCatLst.add(category);
				} else {
					eventCatLst = new ArrayList<Category>();
					eventCatLst.add(category);
				}
				session.setAttribute("eventCatLst", eventCatLst);
			}

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strResponse;
	}

	/**
	 * This method is used to delete event category
	 * 
	 * @param cateId
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/deleteeventcate.htm", method = RequestMethod.GET)
	public @ResponseBody
	String deleteEventCategory(
			@RequestParam(value = "cateId", required = true) int cateId,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws HubCitiServiceException {
		String strResponse = null;
		String strMethodName = "deleteEventCategory";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		AlertCategory objAlertCategory = null;
		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");
		List<Category> categoryLst = null;

		try {
			User user = (User) session.getAttribute("loginUser");

			objAlertCategory = (AlertCategory) session
					.getAttribute("eventcatlst");
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
								strResponse = hubCitiService
										.deleteEventCategory(cateId, user);
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
	 * This method is used to update event categories
	 * 
	 * @param catName
	 * @param cateId
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/updateeventcat.htm", method = RequestMethod.GET)
	public @ResponseBody
	String updateEventCategory(
			@RequestParam(value = "catName", required = true) String catName,
			@RequestParam(value = "cateId", required = true) int cateId,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws HubCitiServiceException {

		String strMethodName = "updateEventCategory";
		String strResponse = null;
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");
		Category objCategory = new Category();
		try {
			User user = (User) session.getAttribute("loginUser");
			if (null != catName && !"".equals(catName)) {
				objCategory.setCatName(catName);
			}
			if (!"".equals(cateId)) {
				objCategory.setCatId(cateId);
			}
			strResponse = hubCitiService.updateEventCategory(objCategory, user);
		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strResponse;
	}

	/**
	 * this method is used to search event category
	 * 
	 * @param category
	 * @param result
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/searcheventcat.htm", method = RequestMethod.POST)
	public String searchEventCategory(
			@ModelAttribute("EventCategoryForm") Category category,
			BindingResult result, HttpServletRequest request,
			HttpSession session, Model model) throws HubCitiServiceException {
		String strMethodName = "searchEventCategory";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		String strViewName = "displayeventcate";
		session.setAttribute(ApplicationConstants.MENUNAME,
				ApplicationConstants.SETUPALERTS);
		AlertCategory objAlertCategory = null;
		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");
		Category objCategory = new Category();
		try {
			session.removeAttribute("eventcatlst");
			User user = (User) session.getAttribute("loginUser");
			if (null != category && !"".equals(category)) {
				objCategory.setCatName(category.getCatName());
			}
			objAlertCategory = hubCitiService.fetchEventCategories(objCategory,
					user);

			session.setAttribute("eventcatlst", objAlertCategory);

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strViewName;
	}

	/**
	 * This method is used to display events.
	 */

	@RequestMapping(value = "/manageevents.htm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public ModelAndView displayEvents(
			@ModelAttribute("ManageEventForm") Event event,
			BindingResult result, ModelMap model, HttpServletRequest request,
			HttpSession session) throws HubCitiServiceException {
		String strMethodName = "displayEvents";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		session.setAttribute(ApplicationConstants.MENUNAME,
				ApplicationConstants.SETUPEVENTS);
		EventDetail objEventDetail = null;
		Event objEvent = new Event();

		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");

		session.removeAttribute("imageCropPage");
		// for pagination.
		final String pageFlag = request.getParameter("pageFlag");
		Integer lowerLimit = 0;
		String pageNumber = "0";
		int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;
		session.removeAttribute("responseEvntStatus");
		session.removeAttribute("responeEvntMsg");
		try {
			session.removeAttribute("eventlst");

			User user = (User) session.getAttribute("loginUser");

			if (null != pageFlag && "true".equals(pageFlag)) {
				pageNumber = request.getParameter("pageNumber");
				final Pagination pageSess = (Pagination) session
						.getAttribute("pagination");
				if (Integer.valueOf(pageNumber) != 0) {
					currentPage = Integer.valueOf(pageNumber);
					final int number = Integer.valueOf(currentPage) - 1;
					final int pageSize = pageSess.getPageRange();
					lowerLimit = pageSize * Integer.valueOf(number);
				}
			} else {
				currentPage = (lowerLimit + recordCount) / recordCount;
			}

			objEvent.setLowerLimit(lowerLimit);
			if (null != event) {
				if (null != event.getEventSearchKey()
						&& !"".equals(event.getEventSearchKey())) {
					objEvent.setEventSearchKey(event.getEventSearchKey());
					request.setAttribute("searchEvnt", "searchEvnt");
				}
			}

			objEventDetail = hubCitiService.displayEvents(objEvent, user, false);

			if (null != objEventDetail) {

				if (null != objEventDetail.getTotalSize()) {
					objPage = Utility.getPagination(
							objEventDetail.getTotalSize(), currentPage,
							"manageevents.htm", recordCount);
					session.setAttribute("pagination", objPage);
				} else {
					session.removeAttribute("pagination");
				}
			} else if (event.getEventSearchKey() == null) {

				return new ModelAndView(new RedirectView(
						"/HubCiti/addEvent.htm"));

			}

			model.put("EventCategoryForm", objEvent);
			session.setAttribute("eventlst", objEventDetail);

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return new ModelAndView("manageevents");
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
	@RequestMapping(value = "/deleteevent.htm", method = RequestMethod.GET)
	public @ResponseBody
	String deleteEvent(
			@RequestParam(value = "eventId", required = true) Integer eventId,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws HubCitiServiceException {
		String strResponse = null;
		String strMethodName = "deleteEvent";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");
		List<Event> eventLst = null;
		EventDetail objEventDetail = null;
		Integer eveId = null;
		try {
			User user = (User) session.getAttribute("loginUser");

			objEventDetail = (EventDetail) session.getAttribute("eventlst");
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
								strResponse = hubCitiService.deleteEvent(
										eventId, user);
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
	@RequestMapping(value = "/addEvent.htm", method = { RequestMethod.GET,
			RequestMethod.POST })
	String displayAddEventPage(
			@ModelAttribute("screenSettingsForm") Event eventDetails,
			BindingResult result, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap map)
			throws HubCitiServiceException {

		String strMethodName = "addEvent";
		String returnView = "addupdateevent";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");
		Category objCategory = new Category();
		List<Category> eventCatLst = null;
		// Event event = new Event();
		List<State> stateList = null;
		List<RetailLocation> hotelList = null;
		AlertCategory eventCatObj = null;
		List<AppSiteDetails> appSiteDetailsLst = null;
		List<Event> events = null;
		session.setAttribute("imageCropPage", "Events");
		// Minimum crop height and width
		session.setAttribute("minCropHt", 150);
		session.setAttribute("minCropWd", 300);
		session.removeAttribute("responseEvntStatus");
		session.removeAttribute("responeEvntMsg");
		session.removeAttribute("eventLogisticsBtns");
		session.removeAttribute("logisticsImgPreview");

		try {
			User user = (User) session.getAttribute("loginUser");

			if (null == eventDetails.getSearchType()
					|| "appsite".equals(eventDetails.getSearchType())) {

				eventDetails.setShowEventPacgTab(false);
				eventDetails.setShowLogisticsTab(false);
			} else if ("hotel".equals(eventDetails.getSearchType())) {
				eventDetails.setShowEventPacgTab(true);
				eventDetails.setShowLogisticsTab(false);
			}

			if (null == eventDetails.getEventImageName()
					|| "".equals(eventDetails.getEventImageName())) {
				session.setAttribute("eventImagePreview",
						ApplicationConstants.DEFAULTIMAGESQR);
			}

			if (null == eventDetails.getLogisticsImgName()
					|| "".equals(eventDetails.getLogisticsImgName())) {
				session.setAttribute("logisticsImgPreview",
						ApplicationConstants.DEFAULTIMAGESQR);
			}

			if (null == eventDetails.getEvntPckg()
					|| "".equals(eventDetails.getEvntPckg())) {
				eventDetails.setEvntPckg("no");
			}

			if (null == eventDetails.getEvntHotel()
					|| "".equals(eventDetails.getEvntHotel())) {
				eventDetails.setEvntHotel("no");
			}

			if (null == eventDetails.getBsnsLoc()
					|| "".equals(eventDetails.getBsnsLoc())) {
				eventDetails.setBsnsLoc("no");
			}

			if (null == eventDetails.getIsEventLogistics()
					|| "".equals(eventDetails.getIsEventLogistics())) {
				eventDetails.setIsEventLogistics("no");
			}

			if (null == eventDetails.getIsEventOverlay()
					|| "".equals(eventDetails.getIsEventOverlay())) {
				eventDetails.setIsEventOverlay("no");
			}

			eventCatObj = hubCitiService
					.fetchEventCategories(objCategory, user);

			stateList = hubCitiService.getAllStates(user.getHubCitiID());

			if (null != stateList) {

				session.setAttribute("states", stateList);
			}
			if (null != eventCatObj) {
				eventCatLst = eventCatObj.getAlertCatLst();

				session.setAttribute("eventCatLst", eventCatLst);
			}

			appSiteDetailsLst = hubCitiService.getAppSites(
					eventDetails.getAppSiteSearchKey(), user.getHubCitiID(),
					null);
			if (null != appSiteDetailsLst && !appSiteDetailsLst.isEmpty()) {

				session.setAttribute("eventAppSiteLst", appSiteDetailsLst);
			} else {

				if ("".equals(Utility.checkNull(eventDetails
						.getAppSiteSearchKey()))) {

					request.setAttribute("appSiteLstError",
							"No Appsite found, Please Create New");
				} else {

					request.setAttribute("appSiteLstError",
							"No Appsite found for the given search text");
				}

				session.removeAttribute("eventAppSiteLst");
			}

			hotelList = hubCitiService.getHotelList(user.getHubCitiID(),
					eventDetails.getHotelSearchKey());

			if (null != hotelList && !hotelList.isEmpty()) {

				session.setAttribute("hotelList", hotelList);
			} else {

				if ("".equals(Utility.checkNull(eventDetails
						.getHotelSearchKey()))) {

					request.setAttribute("hotelListError", "No Hotels found");
				} else {
					request.setAttribute("hotelListError",
							"No Hotels found for the given search text");
				}

				session.removeAttribute("hotelList");
			}

			events = hubCitiService.getEventPatterns();
			session.setAttribute("eventPatterns", events);

			if (null == eventDetails.getIsOngoing()) {
				eventDetails.setIsOngoing("no");
				eventDetails.setRecurrencePatternID(events.get(0)
						.getRecurrencePatternID());
				// Daily Recurrence
				eventDetails.setIsOngoingDaily("days");
				eventDetails.setEveryWeekDay(1);
				// Weekly Recurrence
				eventDetails.setEveryWeek(1);
				Date date = new Date();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				Integer day = calendar.get(Calendar.DAY_OF_WEEK);
				eventDetails.setDays(new String[] { day.toString() });
				eventDetails.setHiddenDay(day.toString());
				// Monthly Recurrence
				eventDetails.setIsOngoingMonthly("date");
				eventDetails.setDateOfMonth(calendar.get(Calendar.DATE));
				eventDetails.setHiddenDate(calendar.get(Calendar.DATE));
				eventDetails.setEveryMonth(1);
				// Month Day Recurrence
				Calendar tempCalendar = Calendar.getInstance();
				tempCalendar.setTime(date);
				tempCalendar.set(Calendar.DATE, 1);
				if (calendar.get(Calendar.DAY_OF_WEEK) < tempCalendar
						.get(Calendar.DAY_OF_WEEK)) {
					eventDetails.setDayNumber((calendar
							.get(Calendar.WEEK_OF_MONTH)) - 1);
					eventDetails.setHiddenWeek((calendar
							.get(Calendar.WEEK_OF_MONTH)) - 1);
				} else {
					eventDetails.setDayNumber(calendar
							.get(Calendar.WEEK_OF_MONTH));
					eventDetails.setHiddenWeek(calendar
							.get(Calendar.WEEK_OF_MONTH));
				}
				day = calendar.get(Calendar.DAY_OF_WEEK);
				eventDetails
						.setEveryWeekDayMonth(new String[] { day.toString() });
				eventDetails.setHiddenWeekDay(day.toString());
				eventDetails.setEveryDayMonth(1);
				eventDetails.setDayOfMonth(day.toString());
				// Range Of Recurrence
				Format formatter = new SimpleDateFormat("MM/dd/yyyy");
				eventDetails.setEventStartDate(formatter.format(date));
				eventDetails.setOccurenceType("noEndDate");
				eventDetails.setEndAfter(1);
				eventDetails.setEventEndDate(formatter.format(date));
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
	 * This controller method will return zipcode, state and the City List based
	 * on input parameter.
	 * 
	 * @param request
	 *            as request parameter.
	 * @param response
	 *            as request parameter.
	 * @param session
	 *            as request parameter.
	 * @return zipcode, state and the City List.
	 * @throws ScanSeeServiceException
	 *             will be thrown.
	 */
	@RequestMapping(value = "/displayZipCodeStateCity", method = RequestMethod.GET)
	@ResponseBody
	public final String getZipStateCity(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws HubCitiServiceException {
		String strMethodName = "getZipStateCity";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		String zipCode = request.getParameter("term");
		List<SearchZipCode> zipCodes = null;
		JSONObject object = new JSONObject();
		JSONObject valueObj = null;
		JSONArray array = new JSONArray();
		try {
			final ServletContext servletContext = request.getSession()
					.getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext
					.getBean("hubCitiService");
			User user = (User) session.getAttribute("loginUser");

			zipCodes = hubCitiService.getZipStateCity(zipCode,
					user.getHubCitiID());
			response.setContentType("application/json");
			if (null != zipCodes && !zipCodes.isEmpty()) {
				for (SearchZipCode code : zipCodes) {
					String value = code.getCity() + ", " + code.getStateName()
							+ " " + code.getZipCode();
					valueObj = new JSONObject();
					valueObj.put("lable", code.getZipCode());
					valueObj.put("value", value);
					valueObj.put("city", code.getCity());
					valueObj.put("statecode", code.getStateCode());
					valueObj.put("state", code.getStateName());
					valueObj.put("zip", code.getZipCode());
					array.put(valueObj);
				}
			} else {
				valueObj = new JSONObject();
				valueObj.put("lable", "No Records Found");
				valueObj.put("value", "No Records Found");
				array.put(valueObj);
			}
			object.put("zipcodes", array);
		} catch (HubCitiServiceException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw e;
		}
		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return object.get("zipcodes").toString();
	}

	/**
	 * This controller method will return zipcode, state and the City List based
	 * on input parameter.
	 * 
	 * @param request
	 *            as request parameter.
	 * @param response
	 *            as request parameter.
	 * @param session
	 *            as request parameter.
	 * @return zipcode, state and the City List.
	 * @throws ScanSeeServiceException
	 *             will be thrown.
	 */
	@RequestMapping(value = "/displayCityStateZipCode", method = RequestMethod.GET)
	@ResponseBody
	public final String getCityStateZip(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws HubCitiServiceException {
		String strMethodName = "getCityStateZip";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		String zipCode = request.getParameter("term");
		List<SearchZipCode> cities = null;
		JSONObject object = new JSONObject();
		JSONObject valueObj = null;
		JSONArray array = new JSONArray();
		try {
			final ServletContext servletContext = request.getSession()
					.getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext
					.getBean("hubCitiService");
			User user = (User) session.getAttribute("loginUser");

			cities = hubCitiService.getCityStateZip(zipCode,
					user.getHubCitiID());
			response.setContentType("application/json");
			if (null != cities && !cities.isEmpty()) {
				for (SearchZipCode city : cities) {
					String value = city.getCity() + ", " + city.getStateName()
							+ " " + city.getZipCode();
					valueObj = new JSONObject();
					valueObj.put("lable", city.getZipCode());
					valueObj.put("value", value);
					valueObj.put("city", city.getCity());
					valueObj.put("statecode", city.getStateCode());
					valueObj.put("state", city.getStateName());
					valueObj.put("zip", city.getZipCode());
					array.put(valueObj);
				}
			} else {
				valueObj = new JSONObject();
				valueObj.put("lable", "No Records Found");
				valueObj.put("value", "No Records Found");
				array.put(valueObj);
			}
			object.put("cities", array);
		} catch (HubCitiServiceException e) {
			LOG.info(ApplicationConstants.METHODEND + strMethodName);
			throw e;
		}
		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return object.get("cities").toString();
	}

	@RequestMapping(value = "/displayappsites.htm", method = RequestMethod.POST)
	public String appSitePagination(
			@ModelAttribute("screenSettingsForm") Event eventDetails,
			BindingResult result, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap map)
			throws IOException, HubCitiServiceException {

		String strMethodName = "getCityStateZip";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		String returnView = "addupdateevent";
		int currentPage = 1;
		String pageNumber = "0";
		int lowerLimit = 0;
		int recordCount = 10;
		List<AppSiteDetails> appSiteDetailsLst = null;
		final String pageFlag = (String) request.getParameter("pageFlag");
		User user = (User) session.getAttribute("loginUser");
		Pagination objPage = null;
		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");
		eventDetails.setHiddenCategory(eventDetails.getEventCategory());

		if (null != pageFlag && "true".equals(pageFlag)) {
			pageNumber = request.getParameter("pageNumber");
			final Pagination pageSess = (Pagination) session
					.getAttribute("pagination");
			if (Integer.valueOf(pageNumber) != 0) {
				currentPage = Integer.valueOf(pageNumber);
				final int number = Integer.valueOf(currentPage) - 1;
				final int pageSize = pageSess.getPageRange();
				lowerLimit = pageSize * Integer.valueOf(number);
			}
		} else {
			currentPage = (lowerLimit + recordCount) / recordCount;
		}

		appSiteDetailsLst = hubCitiService.getAppSites(
				eventDetails.getAppSiteSearchKey(), user.getHubCitiID(),
				lowerLimit);
		if (null != appSiteDetailsLst && !appSiteDetailsLst.isEmpty()) {

			if (null != appSiteDetailsLst.get(0).getTotalRecordSize()) {
				objPage = Utility.getPagination(appSiteDetailsLst.get(0)
						.getTotalRecordSize(), currentPage,
						"displayappsites.htm", recordCount);
				session.setAttribute("pagination", objPage);
			} else {
				session.removeAttribute("pagination");
				session.removeAttribute("eventAppSiteLst");
			}

			session.setAttribute("eventAppSiteLst", appSiteDetailsLst);
		} else {

			session.removeAttribute("eventAppSiteLst");
		}
		map.put("screenSettingsForm", eventDetails);
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
	@RequestMapping(value = "/saveEvent.htm", method = RequestMethod.POST)
	ModelAndView saveUpdateEvent(
			@ModelAttribute("screenSettingsForm") Event eventDetails,
			BindingResult result, HttpServletRequest request,
			HttpServletResponse httpresponse, HttpSession session, ModelMap map)
			throws HubCitiServiceException {

		String strMethodName = "saveEvent";
		String returnView = "addupdateevent";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		List<RetailLocation> hotelList = null;
		List<RetailLocation> hotelListTemp = null;
		String response = null;
		String compDate = null;
		eventDetails.setHiddenCategory(eventDetails.getEventCategory());
		eventDetails.setHiddenState(eventDetails.getState());
		Date currentDate = new Date();
		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");
		eventDetails.setShowEventPacgTab(false);
		eventDetails.setShowLogisticsTab(false);
		User user = (User) session.getAttribute("loginUser");
		List<AppSiteDetails> appSiteDetailsLst = null;
		Boolean validStartDate = false;
		Boolean validEndDate = false;

		try {

			if ("yes".equals(eventDetails.getIsEventLogistics())) {
				List<Event> logisticsBtnList = (List<Event>) session
						.getAttribute("eventLogisticsBtns");

				if (null != logisticsBtnList && !logisticsBtnList.isEmpty()) {
					Boolean firstEntry = true;
					String btnName = null, btnLink = null;
					String seperator = ",";
					for (Event objEvent : logisticsBtnList) {
						if (firstEntry) {
							btnName = objEvent.getButtonName();
							btnLink = objEvent.getButtonLink();
							firstEntry = false;
						} else {
							btnName = btnName + seperator
									+ objEvent.getButtonName();
							btnLink = btnLink + seperator
									+ objEvent.getButtonLink();
						}
					}
					eventDetails.setBtnNames(btnName);
					eventDetails.setBtnLinks(btnLink);
				}
			}
			
			/* To fix XSS */
			eventDetails.setHcEventName(Utility.getXssFreeString(eventDetails.getHcEventName()));
			eventDetails.setAddress(Utility.getXssFreeString(eventDetails.getAddress()));
			eventDetails.setCity(Utility.getXssFreeString(eventDetails.getCity()));

			eventValidator.validate(eventDetails, result);

			appSiteDetailsLst = hubCitiService.getAppSites(
					eventDetails.getAppSiteSearchKey(), user.getHubCitiID(),
					null);
			if (null != appSiteDetailsLst && !appSiteDetailsLst.isEmpty()) {

				session.setAttribute("eventAppSiteLst", appSiteDetailsLst);
			} else {

				if ("".equals(Utility.checkNull(eventDetails
						.getAppSiteSearchKey()))) {

					request.setAttribute("appSiteLstError",
							"No Appsite found, Please Create New");
				} else {

					request.setAttribute("appSiteLstError",
							"No Appsite found for the given search text");
				}

				session.removeAttribute("eventAppSiteLst");
			}

			hotelList = hubCitiService.getHotelList(user.getHubCitiID(),
					eventDetails.getHotelSearchKey());

			if (null != eventDetails.getHotelListJson()
					&& !"".equals(eventDetails.getHotelListJson())) {

				hotelListTemp = Utility.jsonToObjectList(eventDetails
						.getHotelListJson());

				for (int i = 0; i < hotelListTemp.size(); i++) {

					RetailLocation hotelInfoTemp = hotelListTemp.get(i);

					if (null == hotelList || hotelList.isEmpty()) {

						if ("".equals(Utility.checkNull(eventDetails
								.getHotelSearchKey()))) {

							request.setAttribute("hotelListError",
									"No Hotels found");
						} else {
							request.setAttribute("hotelListError",
									"No Hotels found for the given search text");
						}

					} else {

						for (int j = 0; j < hotelList.size(); j++) {
							RetailLocation hotelInfo = hotelList.get(j);

							if (hotelInfoTemp.getRetailLocationID().equals(
									hotelInfo.getRetailLocationID())) {
								hotelInfo.setHotelPrice(hotelInfoTemp
										.getHotelPrice());
								hotelInfo.setDiscountAmount(hotelInfoTemp
										.getDiscountAmount());
								hotelInfo.setDiscountCode(hotelInfoTemp
										.getDiscountCode());
								hotelInfo
										.setRoomAvailabilityCheckURL(hotelInfoTemp
												.getRoomAvailabilityCheckURL());
								hotelInfo.setRoomBookingURL(hotelInfoTemp
										.getRoomBookingURL());
								hotelInfo.setRating(hotelInfoTemp.getRating());
								hotelList.remove(j);
								hotelList.add(j, hotelInfo);
								break;

							}
						}
					}

				}

				session.setAttribute("hotelList", hotelList);

			} else {
				if (null == hotelList || hotelList.isEmpty()) {

					if ("".equals(Utility.checkNull(eventDetails
							.getHotelSearchKey()))) {

						request.setAttribute("hotelListError",
								"No Hotels found");
					} else {
						request.setAttribute("hotelListError",
								"No Hotels found for the given search text");
					}

				}
			}

			if (result.hasErrors()) {

				if (null != eventDetails.getEvntPckg()
						&& "yes".equals(eventDetails.getEvntPckg())) {

					FieldError errorhcEventName = result
							.getFieldError("hcEventName");
					FieldError erroreventImageName = result
							.getFieldError("eventImageName");
					FieldError erroreventCategory = result
							.getFieldError("eventCategory");
					FieldError errorshortDescription = result
							.getFieldError("shortDescription");
					FieldError erroreventDate = result
							.getFieldError("eventDate");
					FieldError erroraddress = result.getFieldError("address");
					FieldError errorcity = result.getFieldError("city");
					FieldError errorstate = result.getFieldError("state");
					FieldError errorhcpostalCode = result
							.getFieldError("postalCode");
					FieldError errorlatitude = result.getFieldError("latitude");
					FieldError errorlogitude = result.getFieldError("logitude");
					FieldError errorappsiteID = result
							.getFieldError("appsiteID");
					FieldError errormoreInfoURL = result
							.getFieldError("moreInfoURL");

					if (null == errorhcEventName && null == erroreventImageName
							&& null == erroreventCategory
							&& null == errorshortDescription
							&& null == erroreventDate && null == erroraddress
							&& null == errorcity && null == errorstate
							&& null == errorhcpostalCode
							&& null == errorlatitude && null == errorlogitude
							&& null == errorappsiteID
							&& null == errormoreInfoURL) {

						eventDetails.setShowEventPacgTab(true);
						eventDetails.setShowLogisticsTab(false);

					} else {
						eventDetails.setShowEventPacgTab(false);
						eventDetails.setShowLogisticsTab(false);
					}

				}
				if (null == eventDetails.getLogisticsImgName()
						|| "".equals(eventDetails.getLogisticsImgName())) {
					session.setAttribute("logisticsImgPreview",
							ApplicationConstants.DEFAULTIMAGESQR);
				}
				return new ModelAndView(returnView);
			}

			else {
				if ("yes".equalsIgnoreCase(eventDetails.getIsOngoing())) {

					if (!"".equals(Utility.checkNull(eventDetails
							.getEventStartDate()))) {
						validStartDate = Utility.isValidDate(eventDetails
								.getEventStartDate());

						if (validStartDate
								&& (null == eventDetails.getHcEventID() || ""
										.equals(eventDetails.getHcEventID()))) {
							compDate = Utility.compareCurrentDate(
									eventDetails.getEventStartDate(),
									currentDate);
							if (null != compDate) {
								eventValidator.validateDates(eventDetails,
										result,
										ApplicationConstants.DATESTARTCURRENT);
							}
						} else if (!validStartDate) {
							eventValidator.validateDates(eventDetails, result,
									ApplicationConstants.VALIDSTARTDATE);
						}
					}
					if (!"".equals(Utility.checkNull(eventDetails
							.getEventEndDate()))) {
						validEndDate = Utility.isValidDate(eventDetails
								.getEventEndDate());
						validStartDate = Utility.isValidDate(eventDetails
								.getEventStartDate());

						if ("endBy".equalsIgnoreCase(eventDetails
								.getOccurenceType())) {
							if (validEndDate) {
								if (validStartDate) {
									compDate = Utility.compareDate(
											eventDetails.getEventStartDate(),
											eventDetails.getEventEndDate());
									if (null != compDate) {
										eventValidator.validateDates(
												eventDetails, result,
												ApplicationConstants.DATEAFTER);
									}
								} else {
									compDate = Utility.compareCurrentDate(
											eventDetails.getEventEndDate(),
											currentDate);
									if (null != compDate) {
										eventValidator
												.validateDates(
														eventDetails,
														result,
														ApplicationConstants.DATEENDCURRENT);
									}
								}

							} else {
								eventValidator.validateDates(eventDetails,
										result,
										ApplicationConstants.VALIDENDDATE);
							}
						}
					}
				} else {
					validStartDate = Utility.isValidDate(eventDetails
							.getEventDate());
					validEndDate = Utility.isValidDate(eventDetails
							.getEventEDate());
					if (!"".equals(Utility.checkNull(eventDetails
							.getEventDate()))) {
						if (validStartDate
								&& null == eventDetails.getHcEventID()
								|| "".equals(eventDetails.getHcEventID())) {
							compDate = Utility.compareCurrentDate(
									eventDetails.getEventDate(), currentDate);
							if (null != compDate) {
								eventValidator
										.validateDates(
												eventDetails,
												result,
												ApplicationConstants.DATENOGSTARTCURRENT);
							}
						} else if (!validStartDate) {
							eventValidator.validateDates(eventDetails, result,
									ApplicationConstants.VALIDDATE);
						}
					}
					if (validEndDate) {
						if (validStartDate) {
							compDate = Utility.compareDate(
									eventDetails.getEventDate(),
									eventDetails.getEventEDate());
							if (null != compDate) {
								eventValidator.validateDates(eventDetails,
										result,
										ApplicationConstants.DATENOGAFTER);
							}
						} else {
							compDate = Utility.compareCurrentDate(
									eventDetails.getEventEDate(), currentDate);
							if (null != compDate) {
								eventValidator.validateDates(eventDetails,
										result,
										ApplicationConstants.DATENOGENDCURRENT);
							}
						}

					} else if (!"".equals(eventDetails.getEventEDate())) {
						eventValidator.validateDates(eventDetails, result,
								ApplicationConstants.VALIDEDATE);
					}

				}

				if (!"".equals(Utility.checkNull(eventDetails.getMoreInfoURL()))) {
					if (!Utility.validateURL(eventDetails.getMoreInfoURL())) {
						eventValidator.validate(eventDetails, result,
								ApplicationConstants.INVALIDEVENTURL);

					}

				}

				eventDetails.setShowEventPacgTab(false);
				eventDetails.setShowLogisticsTab(false);
				if (result.hasErrors()) {
					if (null == eventDetails.getLogisticsImgName()
							|| "".equals(eventDetails.getLogisticsImgName())) {
						session.setAttribute("logisticsImgPreview",
								ApplicationConstants.DEFAULTIMAGESQR);
					}
					return new ModelAndView(returnView);

				}
				if (!Utility.validateURL(eventDetails.getPackageTicketURL())) {
					eventValidator.validate(eventDetails, result,
							ApplicationConstants.INVALIDURL);

				}

				if (!Utility.isValidPrice(eventDetails.getPackagePrice())) {
					eventValidator.validate(eventDetails, result,
							ApplicationConstants.INVALIDPRICE);

				}

				if (result.hasErrors()) {
					eventDetails.setShowEventPacgTab(true);
					eventDetails.setShowLogisticsTab(false);
					if (null == eventDetails.getLogisticsImgName()
							|| "".equals(eventDetails.getLogisticsImgName())) {
						session.setAttribute("logisticsImgPreview",
								ApplicationConstants.DEFAULTIMAGESQR);
					}
					return new ModelAndView(returnView);
				}

				eventDetails = eventRecurrenceDeatils(eventDetails);
				/*
				 * if(null != eventDetails.getEventEndDate() &&
				 * !"".equals(eventDetails.getEventEndDate())) { compDate =
				 * Utility.compareDate(eventDetails.getEventStartDate(),
				 * eventDetails.getEventEndDate()); if (null != compDate) {
				 * eventValidator.validateDates(eventDetails, result,
				 * ApplicationConstants.DATEAFTER); } } if (result.hasErrors())
				 * { return new ModelAndView(returnView); }
				 */
				if (null != eventDetails.getIsEventLogistics()
						&& "yes".equals(eventDetails.getIsEventLogistics())) {
					eventValidator.validateLogistics(eventDetails, result);
					if (result.hasErrors()) {
						eventDetails.setShowEventPacgTab(false);
						eventDetails.setShowLogisticsTab(true);
						if (null == eventDetails.getLogisticsImgName()
								|| "".equals(eventDetails.getLogisticsImgName())) {
							session.setAttribute("logisticsImgPreview",
									ApplicationConstants.DEFAULTIMAGESQR);
						}
						return new ModelAndView(returnView);
					}
				}

				response = hubCitiService.saveUpdateEventDeatils(eventDetails,
						user);

				if (null != response && !"".equals(response)) {

					if (response.equals(ApplicationConstants.GEOERROR)) {

						if (eventDetails.isGeoError()) {

							if (null == eventDetails.getLatitude()) {
								eventValidator.validate(eventDetails, result,
										ApplicationConstants.LATITUDE);
							}

							if (null == eventDetails.getLogitude()) {
								eventValidator.validate(eventDetails, result,
										ApplicationConstants.LONGITUDE);
							}
						} else {

							eventValidator.validate(eventDetails, result,
									ApplicationConstants.GEOERROR);
							request.setAttribute("GEOERROR",
									ApplicationConstants.GEOERROR);
							eventDetails.setLatitude(null);
							eventDetails.setLogitude(null);
							eventDetails.setGeoError(true);
						}

						if (result.hasErrors()) {
							return new ModelAndView(returnView);
						}
					} else {

						if (response.equals(ApplicationConstants.SUCCESS)) {
							session.setAttribute("responseEvntStatus", response);
							if (null == eventDetails.getHcEventID()
									|| "".equals(eventDetails.getHcEventID())) {
								session.setAttribute("responeEvntMsg",
										"Event Created Succesfully");

							} else {

								session.setAttribute("responeEvntMsg",
										"Event Updated Succesfully");

							}

							return new ModelAndView(new RedirectView(
									"manageevents.htm"));

						}
					}

				}
			}

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return new ModelAndView(returnView);
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
	@RequestMapping(value = "/editEventDetails.htm", method = RequestMethod.GET)
	String displayEventDetails(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap map)
			throws HubCitiServiceException {
		String strMethodName = "addEvent";
		String returnView = "addupdateevent";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		session.removeAttribute("sessionEventDetails");

		final ServletContext servletContext = request.getSession()
				.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext
				.getBean("hubCitiService");
		Category objCategory = new Category();
		List<Category> eventCatLst = null;
		// Event event = new Event();
		List<State> stateList = null;
		List<RetailLocation> hotelList = null;
		AlertCategory eventCatObj = null;
		List<AppSiteDetails> appSiteDetailsLst = null;
		List<AppSiteDetails> eventAppSiteDetailsLst = null;
		session.setAttribute("imageCropPage", "Events");
		// Minimum crop height and width
		session.setAttribute("minCropHt", 150);
		session.setAttribute("minCropWd", 300);
		Event eventDetails = null;
		List<Event> events = null;
		List<RetailLocation> eventHotelList = null;
		session.removeAttribute("responseEvntStatus");
		session.removeAttribute("responeEvntMsg");
		session.removeAttribute("eventLogisticsBtns");

		try {
			User user = (User) session.getAttribute("loginUser");

			String eventId = (String) request.getParameter("eventId");

			eventCatObj = hubCitiService
					.fetchEventCategories(objCategory, user);

			stateList = hubCitiService.getAllStates(user.getHubCitiID());

			if (null != stateList) {

				session.setAttribute("states", stateList);
			}
			if (null != eventCatObj) {
				eventCatLst = eventCatObj.getAlertCatLst();

				session.setAttribute("eventCatLst", eventCatLst);
			}

			hotelList = hubCitiService.getHotelList(user.getHubCitiID(), null);

			if (null != hotelList && !hotelList.isEmpty()) {

				session.setAttribute("hotelList", hotelList);
			} else {

				session.removeAttribute("hotelList");
			}
			if (null != eventId && !"".equals(eventId)) {

				eventDetails = hubCitiService.fetchEventDetails(Integer
						.valueOf(eventId));
				eventDetails.setHcEventID(Integer.valueOf(eventId));
				eventDetails.setHiddenWeek(eventDetails.getDayNumber());
				String[] tempDays = eventDetails.getEveryWeekDayMonth();
				eventDetails.setHiddenWeekDay(tempDays[0]);
				eventDetails.setHiddenDate(eventDetails.getDateOfMonth());
				String result = "";
				for (String s : eventDetails.getDays()) {
					result += s;
				}
				eventDetails.setHiddenDay(result);

				if ("yes".equals(eventDetails.getIsEventLogistics())) {
					Event eventLogisticsBtns = hubCitiService
							.getEventLogisticsButtonDetails(
									user.getHubCitiID(),
									Integer.parseInt(eventId),
									user.gethCAdminUserID());
					session.setAttribute("eventLogisticsBtns",
							eventLogisticsBtns.getLogisticsBtnList());
					session.setAttribute("logisticsImgPreview",
							eventLogisticsBtns.getLogisticsImgPath());
					eventDetails.setLogisticsImgPath(eventLogisticsBtns
							.getLogisticsImgPath());
					eventDetails.setIsEventOverlay(eventLogisticsBtns
							.getIsEventOverlay());
					if (null != eventLogisticsBtns.getLogisticsImgPath()) {
						String[] pathArr = eventLogisticsBtns
								.getLogisticsImgPath().split("/");
						eventDetails
								.setLogisticsImgName(pathArr[pathArr.length - 1]);
					}
				} else {
					eventDetails.setIsEventOverlay("no");
					eventDetails.setShowLogisticsTab(false);
					session.setAttribute("logisticsImgPreview",
							ApplicationConstants.DEFAULTIMAGESQR);
				}

				if (null != eventDetails) {

					session.setAttribute("eventImagePreview",
							eventDetails.getEventImagePath());

					if ("yes".equals(eventDetails.getEvntHotel())) {

						eventHotelList = hubCitiService
								.getEventHotelList(Integer.valueOf(eventId));

						session.setAttribute("hotelList", eventHotelList);
						/*
						 * if (null != hotelList && !hotelList.isEmpty() && null
						 * != eventHotelList && !eventHotelList.isEmpty()) { for
						 * (int i = 0; i < eventHotelList.size(); i++) { for
						 * (int j = 0; j < hotelList.size(); j++) { if
						 * (eventHotelList
						 * .get(i).getRetailLocationID().equals(hotelList
						 * .get(j).getRetailLocationID())) {
						 * hotelList.remove(j); hotelList.add(i,
						 * eventHotelList.get(i)); break; } } } }
						 */

					}

				}

			}

			appSiteDetailsLst = hubCitiService.getAppSites(
					eventDetails.getAppSiteSearchKey(), user.getHubCitiID(),
					null);
			if (null != appSiteDetailsLst && !appSiteDetailsLst.isEmpty()) {

				if (!"".equals(Utility.checkNull(eventDetails.getAppsiteIDs()))) {
					eventAppSiteDetailsLst = new ArrayList<AppSiteDetails>();
					for (int i = 0; i < eventDetails.getAppsiteID().length; i++) {

						for (int j = 0; j < appSiteDetailsLst.size(); j++) {

							AppSiteDetails appSiteDetails = appSiteDetailsLst
									.get(j);
							if (appSiteDetailsLst.get(j).getAppSiteId()
									.equals(eventDetails.getAppsiteID()[i])) {
								// appSiteDetailsLst.remove(j);
								eventAppSiteDetailsLst.add(appSiteDetails);
							}

						}
					}
				}

				session.setAttribute("eventAppSiteLst", eventAppSiteDetailsLst);
			} else {

				session.removeAttribute("eventAppSiteLst");
			}

			events = hubCitiService.getEventPatterns();
			session.setAttribute("eventPatterns", events);

			if (null == eventDetails.getIsOngoing()
					|| "no".equalsIgnoreCase(eventDetails.getIsOngoing())) {
				eventDetails.setRecurrencePatternID(events.get(0)
						.getRecurrencePatternID());
			}
			map.put("screenSettingsForm", eventDetails);
		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return returnView;
	}

	@SuppressWarnings("deprecation")
	public Event eventRecurrenceDeatils(Event eventDetails)
			throws HubCitiServiceException {
		String eventStartTime = null;
		String eventEndTime = null;

		if ("yes".equalsIgnoreCase(eventDetails.getIsOngoing())) {
			eventStartTime = eventDetails.getEventStartTimeHrs() + ":"
					+ eventDetails.getEventStartTimeMins();
			eventDetails.setEventStartTime(eventStartTime);
			eventEndTime = eventDetails.getEventEndTimeHrs() + ":"
					+ eventDetails.getEventEndTimeMins();
			eventDetails.setEventEndTime(eventEndTime);

			if ("Daily".equalsIgnoreCase(eventDetails
					.getRecurrencePatternName())) {
				if ("days".equalsIgnoreCase(eventDetails.getIsOngoingDaily())) {
					eventDetails.setRecurrenceInterval(eventDetails
							.getEveryWeekDay());
					eventDetails.setIsWeekDay(false);
					eventDetails.setDays(null);
					eventDetails.setDayNumber(null);
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					Date startDate = new Date(eventDetails.getEventStartDate());
					Calendar c = Calendar.getInstance();
					c.setTime(startDate);
					Integer day = c.get(Calendar.DAY_OF_WEEK);
					Integer date = c.get(Calendar.DATE);

					if (day == 7) {
						date = date + 2;
					} else if (day == 1) {
						date = date + 1;
					}

					c.set(Calendar.DATE, date);
					eventDetails.setEventStartDate(sdf.format(c.getTime()));
					eventDetails.setRecurrenceInterval(null);
					eventDetails.setIsWeekDay(true);
					eventDetails.setDays(null);
					eventDetails.setDayNumber(null);
				}
			} else if ("Weekly".equalsIgnoreCase(eventDetails
					.getRecurrencePatternName())) {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				Date startDate = new Date(eventDetails.getEventStartDate());
				Calendar c = Calendar.getInstance();
				c.setTime(startDate);
				Integer day = c.get(Calendar.DAY_OF_WEEK);
				Boolean dayContain = false;
				Integer occurenceWeek = eventDetails.getEveryWeek() - 1;
				for (String days : eventDetails.getDays()) {
					if (day.equals(Integer.parseInt(days))) {
						dayContain = true;
					}
				}
				if (!dayContain) {
					Integer selectedDay = 0;
					for (String days : eventDetails.getDays()) {
						Integer tempDay = Integer.parseInt(days);
						if ((selectedDay == 0 && day < tempDay)
								|| (day < tempDay && selectedDay > tempDay)) {
							selectedDay = tempDay;
						}
					}
					if (selectedDay > day) {
						Integer temp = selectedDay - day;
						c.set(Calendar.DATE, c.get(Calendar.DATE) + temp);
						eventDetails.setEventStartDate(sdf.format(c.getTime()));
					} else {
						String[] selectedDays = eventDetails.getDays();
						selectedDay = Integer.parseInt(selectedDays[0]);

						Integer temp = day - selectedDay;
						Integer finalDay = 7 - temp;
						if (occurenceWeek > 0) {
							finalDay = finalDay + (occurenceWeek * 7);
						}
						c.set(Calendar.DATE, c.get(Calendar.DATE) + finalDay);
						eventDetails.setEventStartDate(sdf.format(c.getTime()));
					}
				}
				eventDetails.setRecurrenceInterval(eventDetails.getEveryWeek());
				eventDetails.setIsWeekDay(false);
				eventDetails.setDayNumber(null);
				LOG.info("EVENT START DATE :"
						+ eventDetails.getEventStartDate());
			} else if ("Monthly".equalsIgnoreCase(eventDetails
					.getRecurrencePatternName())) {
				if ("date".equalsIgnoreCase(eventDetails.getIsOngoingMonthly())) {
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					Date startDate = new Date(eventDetails.getEventStartDate());
					Calendar c = Calendar.getInstance();
					c.setTime(startDate);
					Integer strtdate = c.get(Calendar.DATE);
					Integer month = c.get(Calendar.MONTH);
					Integer lastDayOfMonth = c.getActualMaximum(Calendar.DATE);

					Integer comp = strtdate.compareTo(eventDetails
							.getDateOfMonth());

					if (comp != 0) {
						if (comp > 0) {
							Integer tempMonth = month
									+ eventDetails.getEveryMonth();
							c.set(Calendar.MONTH, tempMonth);
						}
						if (eventDetails.getDateOfMonth() < lastDayOfMonth) {
							c.set(Calendar.DATE, eventDetails.getDateOfMonth());
						} else {
							c.set(Calendar.DATE, lastDayOfMonth);
						}
					}
					eventDetails.setEventStartDate(sdf.format(c.getTime()));
					eventDetails.setRecurrenceInterval(eventDetails
							.getEveryMonth());
					eventDetails.setIsWeekDay(false);
					eventDetails.setDays(null);
					eventDetails.setDayNumber(eventDetails.getDateOfMonth());
					LOG.info("EVENT START DATE :"
							+ eventDetails.getEventStartDate());
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					Integer everyDay = eventDetails.getDayNumber();
					String[] everyDayOfMonth = eventDetails
							.getEveryWeekDayMonth();
					Date startDate = new Date(eventDetails.getEventStartDate());
					Integer tempWeek = 0;

					Calendar c = Calendar.getInstance();
					c.setTime(startDate);
					Integer tempDay = c.get(Calendar.DAY_OF_WEEK);
					// Integer tempDay1 = c.get(Calendar.DAY_OF_WEEK);
					Integer date = c.get(Calendar.DATE);
					c.set(Calendar.DATE, 1);
					Integer day = c.get(Calendar.DAY_OF_WEEK);
					// Integer tempDay = day;
					Boolean dayContain = false;
					for (String days : everyDayOfMonth) {
						if (tempDay.equals(Integer.parseInt(days))) {
							dayContain = true;
							break;
						}
					}
					if (!dayContain) {
						Integer length = everyDayOfMonth.length;
						Integer dayLoop = Integer
								.parseInt(everyDayOfMonth[length - 1])
								- tempDay;
						for (int i = 0; i < dayLoop; i++) {
							tempDay = tempDay + 1;
							for (String days : everyDayOfMonth) {
								if (tempDay.equals(Integer.parseInt(days))) {
									dayContain = true;
									break;
								}
							}
							if (dayContain) {
								break;
							}
						}
					}
					if (!dayContain) {
						tempDay = Integer.parseInt(everyDayOfMonth[0]);
						int numOfWeeksInMonth = c
								.getActualMaximum(Calendar.WEEK_OF_MONTH);

						if (tempWeek > numOfWeeksInMonth) {
							tempWeek = numOfWeeksInMonth;
						}
						c.set(Calendar.DAY_OF_WEEK, tempDay);
						c.set(Calendar.WEEK_OF_MONTH, everyDay);
					} else {
						if (tempDay < day) {
							tempWeek = everyDay + 1;
						} else {
							tempWeek = everyDay;
						}
						int numOfWeeksInMonth = c
								.getActualMaximum(Calendar.WEEK_OF_MONTH);

						if (tempWeek > numOfWeeksInMonth) {
							tempWeek = numOfWeeksInMonth;
						}
						c.set(Calendar.DAY_OF_WEEK, tempDay);
						c.set(Calendar.WEEK_OF_MONTH, tempWeek);
					}

					if (date <= c.get(Calendar.DATE)) {
						eventDetails.setEventStartDate(sdf.format(c.getTime()));
					} else {
						tempWeek = everyDay;
						Integer tempMonth = c.get(Calendar.MONTH)
								+ eventDetails.getEveryDayMonth();
						c.set(Calendar.MONTH, tempMonth);
						/*
						 * c.setTime(startDate); c.set(Calendar.MONTH,
						 * tempMonth);
						 */
						// tempDay = c.get(Calendar.DAY_OF_WEEK);
						c.set(Calendar.DATE, 1);
						day = c.get(Calendar.DAY_OF_WEEK);
						tempDay = day;

						for (String days : everyDayOfMonth) {
							if (tempDay.equals(Integer.parseInt(days))) {
								dayContain = true;
								break;
							}
						}

						if (!dayContain) {
							Integer length = everyDayOfMonth.length;
							Integer dayLoop = Integer
									.parseInt(everyDayOfMonth[length - 1])
									- tempDay;
							for (int i = 0; i < dayLoop; i++) {
								tempDay = tempDay + 1;
								for (String days : everyDayOfMonth) {
									if (tempDay.equals(Integer.parseInt(days))) {
										dayContain = true;
										break;
									}
								}
								if (dayContain) {
									break;
								}
							}
						}
						if (!dayContain) {
							tempDay = Integer.parseInt(everyDayOfMonth[0]);
							int numOfWeeksInMonth = c
									.getActualMaximum(Calendar.WEEK_OF_MONTH);

							if (tempWeek > numOfWeeksInMonth) {
								tempWeek = numOfWeeksInMonth;
							}
							c.set(Calendar.DAY_OF_WEEK, tempDay);
							c.set(Calendar.WEEK_OF_MONTH, everyDay);
						} else {
							if (tempDay < day) {
								tempWeek = everyDay + 1;
							} else {
								tempWeek = everyDay;
							}
							int numOfWeeksInMonth = c
									.getActualMaximum(Calendar.WEEK_OF_MONTH);

							if (tempWeek > numOfWeeksInMonth) {
								tempWeek = numOfWeeksInMonth;
							}
							c.set(Calendar.DAY_OF_WEEK, tempDay);
							c.set(Calendar.WEEK_OF_MONTH, tempWeek);
						}

						eventDetails.setEventStartDate(sdf.format(c.getTime()));
					}
					eventDetails.setRecurrenceInterval(eventDetails
							.getEveryDayMonth());
					eventDetails.setIsWeekDay(false);
					eventDetails.setDays(eventDetails.getEveryWeekDayMonth());
					LOG.info("EVENT START DATE :"
							+ eventDetails.getEventStartDate());
				}
			}
			if ("noEndDate".equalsIgnoreCase(eventDetails.getOccurenceType())) {
				eventDetails.setEventEndDate(null);
				eventDetails.setEndAfter(null);
			} else if ("endBy"
					.equalsIgnoreCase(eventDetails.getOccurenceType())) {
				eventDetails.setEndAfter(null);
			} else if ("endAfter".equalsIgnoreCase(eventDetails
					.getOccurenceType())) {
				Integer endAfter = eventDetails.getEndAfter();
				if ("Daily".equalsIgnoreCase(eventDetails
						.getRecurrencePatternName())) {
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					Date startDate = new Date(eventDetails.getEventStartDate());

					Calendar c = Calendar.getInstance();
					c.setTime(startDate);
					if ("days".equalsIgnoreCase(eventDetails
							.getIsOngoingDaily())) {
						Integer finalDay = c.get(Calendar.DATE)
								+ ((endAfter * eventDetails.getEveryWeekDay()) - eventDetails
										.getEveryWeekDay());
						c.set(Calendar.DATE, finalDay);
						eventDetails.setEventEndDate(sdf.format(c.getTime()));
					} else {
						Integer finalDay = c.get(Calendar.DATE);
						Integer day = c.get(Calendar.DAY_OF_WEEK);

						Integer tempDay = 6 - day;

						if (endAfter > 1) {
							finalDay = finalDay + tempDay
									+ ((endAfter - 1) * 7);
						} else {
							finalDay = finalDay + tempDay;
						}
						c.set(Calendar.DATE, finalDay);
						eventDetails.setEventEndDate(sdf.format(c.getTime()));
					}
				} else if ("Weekly".equalsIgnoreCase(eventDetails
						.getRecurrencePatternName())) {
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					Date startDate = new Date(eventDetails.getEventStartDate());

					Calendar c = Calendar.getInstance();
					c.setTime(startDate);
					Integer finalDay = c.get(Calendar.DATE)
							+ ((endAfter - 1) * (eventDetails.getEveryWeek() * 7));
					Integer day = c.get(Calendar.DAY_OF_WEEK);
					Integer i = eventDetails.getDays().length;
					String days = eventDetails.getDays()[i - 1];
					if (Integer.parseInt(days) > day) {
						int temp = Integer.parseInt(days) - day;
						finalDay = finalDay + temp;
					}
					c.set(Calendar.DATE, finalDay);
					eventDetails.setEventEndDate(sdf.format(c.getTime()));
				} else if ("Monthly".equalsIgnoreCase(eventDetails
						.getRecurrencePatternName())) {
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					Date startDate = new Date(eventDetails.getEventStartDate());

					Calendar c = Calendar.getInstance();
					c.setTime(startDate);
					if ("date".equalsIgnoreCase(eventDetails
							.getIsOngoingMonthly())) {
						c.set(Calendar.MONTH, c.get(Calendar.MONTH)
								+ (endAfter - 1) * eventDetails.getEveryMonth());

						Integer lastDayOfMonth = c
								.getActualMaximum(Calendar.DATE);

						if (eventDetails.getDateOfMonth() < lastDayOfMonth) {
							c.set(Calendar.DATE, eventDetails.getDateOfMonth());
						} else {
							c.set(Calendar.DATE, lastDayOfMonth);
						}
					} else {
						Integer everyDay = eventDetails.getDayNumber();
						String[] everyDayOfMonth = eventDetails
								.getEveryWeekDayMonth();

						c.set(Calendar.MONTH,
								c.get(Calendar.MONTH) + (endAfter - 1)
										* eventDetails.getEveryDayMonth());
						c.set(Calendar.DATE, 1);
						Integer day = c.get(Calendar.DAY_OF_WEEK);
						Integer weeksLength = everyDayOfMonth.length;
						if (day > Integer
								.parseInt(everyDayOfMonth[weeksLength - 1])) {
							everyDay = everyDay + 1;
						}
						c.set(Calendar.DAY_OF_WEEK, Integer
								.parseInt(everyDayOfMonth[weeksLength - 1]));
						Integer lastWeekOfMonth = c
								.getActualMaximum(Calendar.WEEK_OF_MONTH);
						if (everyDay > lastWeekOfMonth) {
							everyDay = everyDay - 1;
						}
						c.set(Calendar.WEEK_OF_MONTH, everyDay);
					}
					eventDetails.setEventEndDate(sdf.format(c.getTime()));
				}
			}

			if (null != eventDetails.getEventEndDate()
					&& ApplicationConstants.DATEAFTER.equals(Utility
							.compareDate(eventDetails.getEventStartDate(),
									eventDetails.getEventEndDate()))) {
				eventDetails.setEventEndDate(eventDetails.getEventStartDate());
			}
		} else {
			eventStartTime = eventDetails.getEventTimeHrs() + ":"
					+ eventDetails.getEventTimeMins();
			eventDetails.setEventTime(eventStartTime);
			if (null != eventDetails.getEventEDate()
					&& !"".equals(eventDetails.getEventEDate())) {
				eventEndTime = eventDetails.getEventETimeHrs() + ":"
						+ eventDetails.getEventETimeMins();
				eventDetails.setEventETime(eventEndTime);
			} else {
				eventDetails.setEventETime(null);
				eventDetails.setEventEDate(null);
			}

		}
		return eventDetails;
	}

	/**
	 * This method is used to navigate Add event screen.
	 * 
	 * @param event
	 * @param result
	 * @param request
	 * @param response
	 * @param session
	 * @param map
	 * @return
	 * @throws HubCitiServiceException
	 */

	@RequestMapping(value = "/addEvtMarker.htm", method = RequestMethod.GET)
	public ModelAndView addEventMarkers(
			@ModelAttribute("screenSettingsForm") Event event,
			BindingResult result, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap map)
			throws HubCitiServiceException {
		final String strViewName = "addEvtMarker";
		final String strMethodName = "addEventMarkers";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		session.setAttribute("eventIconPreview",
				ApplicationConstants.DEFAULTIMAGESQR);
		ArrayList<Event> evtMarkerLst = null;
		String eventId = null;
		try {

			session.setAttribute("evtMarkerImagePreview",
					ApplicationConstants.DEFAULTIMAGESQR);
			session.setAttribute("imageCropPage", "Events");
			// Minimum crop height and width
			session.setAttribute("minCropHt", 26);
			session.setAttribute("minCropWd", 26);
			session.removeAttribute("EvtMarkerLst");
			session.removeAttribute("markerInfo");
			session.removeAttribute("evtId");

			final ServletContext servletContext = request.getSession()
					.getServletContext();
			WebApplicationContext webApplicationContext = WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
			HubCitiService hubCitiService = (HubCitiService) webApplicationContext
					.getBean("hubCitiService");
			User user = (User) session.getAttribute("loginUser");

			eventId = request.getParameter("evtId");

			if (!Utility.isEmptyOrNullString(eventId)) {
				event.setHcEventID(Integer.valueOf(eventId));
				session.setAttribute("evtId", eventId);
			}

			evtMarkerLst = hubCitiService.getEvtMarkerInfo(event, user);
			if (null != evtMarkerLst && !evtMarkerLst.isEmpty()) {
				session.setAttribute("EvtMarkerLst", evtMarkerLst);
			}

		} catch (HubCitiServiceException exception) {

			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED
					+ exception.getMessage());
			throw new HubCitiServiceException(exception);

		}
		request.setAttribute("btnName", "Save");
		map.put("screenSettingsForm", event);

		LOG.info(ApplicationConstants.METHODEND + strMethodName);

		return new ModelAndView(strViewName);
	}

	/**
	 * This method is used to save or update marker details.
	 * 
	 * @param event
	 * @param result
	 * @param map
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/saveEvtMarker.htm", method = RequestMethod.POST)
	public ModelAndView saveEvtMarkerInfo(
			@ModelAttribute("screenSettingsForm") Event event,
			BindingResult result, ModelMap map, HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws HubCitiServiceException {
		final String strMethodName = "saveEvtMarkerInfo";
		final String strViewName = "addEvtMarker";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		String strResponse = null;
		Integer iStatus = null;
		ArrayList<Event> evtMarkerLst = null;
		try {

			if (null != event.getButtonId() && !event.getButtonId().equals("")
					&& !event.getButtonId().equals("null")
					&& event.getButtonName().equals("Update")) {
				Integer evtMId = Integer.valueOf(event.getButtonId());
				event.setEvtMarkerId(evtMId);
				request.setAttribute("markerId", event.getButtonId());
			} else {

			}

			session.removeAttribute("markerInfo");

			request.setAttribute("btnName", event.getButtonName());
			final ServletContext servletContext = request.getSession()
					.getServletContext();
			WebApplicationContext webApplicationContext = WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
			HubCitiService hubCitiService = (HubCitiService) webApplicationContext
					.getBean("hubCitiService");

			if (null != event && null != event.getEventImageFile()) {

				String evtImg = event.getEventImageFile().getOriginalFilename();

				if (!Utility.isEmptyOrNullString(evtImg)) {
					event.setEventImageName(evtImg);
				}

			}
			eventValidator.validate(event, result, iStatus);
			if (result.hasErrors()) {
				return new ModelAndView(strViewName);
			}

			User user = (User) session.getAttribute("loginUser");

			if (null != event) {
				if (null != event.getEvtMarkerId()
						&& event.getEvtMarkerId().equals("")) {
					event.setEvtMarkerId(null);
				}
			}
			// Adding marker details to database.
			strResponse = hubCitiService.saveEvtMarkerInfo(event, user);

			if (null != strResponse
					&& strResponse.equals(ApplicationConstants.SUCCESS)) {

				Event evnt = new Event();
				map.put("screenSettingsForm", evnt);
				session.setAttribute("evtMarkerImagePreview",
						ApplicationConstants.DEFAULTIMAGESQR);
				if (null != event.getEvtMarkerId()
						&& !event.getEvtMarkerId().equals("")) {

					request.setAttribute("successMsg",
							ApplicationConstants.EVENTMARKERUPDATETEXT);

				} else {
					request.setAttribute("successMsg",
							ApplicationConstants.EVENTMARKERSAVETEXT);
				}

			} else {
				map.put("screenSettingsForm", event);
				request.setAttribute("failureMsg",
						ApplicationConstants.EVENTMARKERDUPLICATETEXT);

			}

			evtMarkerLst = hubCitiService.getEvtMarkerInfo(event, user);
			if (null != evtMarkerLst && !evtMarkerLst.isEmpty()) {
				session.setAttribute("EvtMarkerLst", evtMarkerLst);
			}

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED
					+ exception.getMessage());
			throw new HubCitiServiceException(exception);

		}
		request.setAttribute("btnName", "Save");
		LOG.info(ApplicationConstants.METHODEND + strMethodName);

		return new ModelAndView(strViewName);

	}

	/**
	 * This method is used to delete marker event.
	 * 
	 * @param event
	 * @param result
	 * @param map
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/deleteEvtMarker.htm", method = RequestMethod.POST)
	public ModelAndView deleteEvtMaker(
			@ModelAttribute("screenSettingsForm") Event event,
			BindingResult result, ModelMap map, HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws HubCitiServiceException

	{

		final String strMethodName = "saveEvtMarkerInfo";
		final String strViewName = "addEvtMarker";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		String strResponse = null;

		final ServletContext servletContext = request.getSession()
				.getServletContext();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		HubCitiService hubCitiService = (HubCitiService) webApplicationContext
				.getBean("hubCitiService");
		ArrayList<Event> evtMarkerLst = null;
		try {

			session.removeAttribute("EvtMarkerLst");
			session.removeAttribute("markerInfo");

			strResponse = hubCitiService.deleteEvtMarker(event);

			request.setAttribute("successMsg", strResponse);

			Event evt = new Event();
			map.put("screenSettingsForm", evt);
			User user = (User) session.getAttribute("loginUser");

			evtMarkerLst = hubCitiService.getEvtMarkerInfo(event, user);
			if (null != evtMarkerLst && !evtMarkerLst.isEmpty()) {
				session.setAttribute("EvtMarkerLst", evtMarkerLst);
			}

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED
					+ exception.getMessage());
			throw new HubCitiServiceException(exception);
		}

		request.setAttribute("btnName", "Save");
		LOG.info(ApplicationConstants.METHODEND + strMethodName);

		return new ModelAndView(strViewName);

	}

	/**
	 * Method to add, edit and delete event logistics buttons from session list.
	 * 
	 * @param buttonId
	 * @param buttonName
	 * @param buttonLink
	 * @param type
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/updatelogisticsbtnlist.htm", method = RequestMethod.GET)
	@ResponseBody
	String updateLogisticsButtonList(
			@RequestParam(value = "buttonId") Integer buttonId,
			@RequestParam(value = "buttonName") String buttonName,
			@RequestParam(value = "buttonLink") String buttonLink,
			@RequestParam(value = "type") String type,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws HubCitiServiceException {
		String strMethodName = "addLogisticsButton";
		String strResponse = null;
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		List<Event> logisticsBtnList = (List<Event>) session
				.getAttribute("eventLogisticsBtns");
		Event objEvent = null;
		Boolean isBtnNameExist = false;

		try {

			if ("add".equalsIgnoreCase(type)) {
				if (null == buttonName || null == buttonLink) {
					return "Please enter button name and link";
				}

				if (!Utility.validateURL(buttonLink)) {
					return "Please enter valid URL for button link";
				}

				Integer tempBtnId = 1;
				Integer uniqueBtnId = 1;
				if (null != logisticsBtnList && !logisticsBtnList.isEmpty()) {
					Integer iTmp = 1;
					for (Event eventObj : logisticsBtnList) {
						if (iTmp < eventObj.getButtonId()) {
							iTmp = eventObj.getButtonId();
						}
						if (buttonName.equals(eventObj.getButtonName())) {
							isBtnNameExist = true;
						}
					}
					uniqueBtnId = iTmp + 1;
				} else {
					logisticsBtnList = new ArrayList<Event>();
				}

				if (null == session.getAttribute("logisticsBtnIdTemp")) {
					session.setAttribute("logisticsBtnIdTemp", uniqueBtnId);
				}
				tempBtnId = (Integer) session
						.getAttribute("logisticsBtnIdTemp");

				if (!isBtnNameExist) {
					objEvent = new Event();
					objEvent.setButtonId(tempBtnId);
					objEvent.setButtonName(buttonName);
					objEvent.setButtonLink(buttonLink);
					logisticsBtnList.add(objEvent);
					session.setAttribute("eventLogisticsBtns", logisticsBtnList);
					strResponse = "Success:" + type + ":" + tempBtnId;
					session.setAttribute("logisticsBtnIdTemp", ++tempBtnId);
				} else {
					strResponse = "Button name exist";
				}
			} else if ("update".equalsIgnoreCase(type)) {
				if (null == buttonName || "".equals(buttonName)
						|| null == buttonLink || "".equals(buttonLink)) {
					return "Please enter button name and link";
				}

				if (!Utility.validateURL(buttonLink)) {
					return "Please enter valid URL for button link";
				}

				int btnIdFromAjax = buttonId;
				int btnIdFromSession = 0;
				for (Event eventObj : logisticsBtnList) {
					if (buttonName.equals(eventObj.getButtonName())) {
						btnIdFromSession = eventObj.getButtonId();
						if (btnIdFromAjax != btnIdFromSession) {
							// if(buttonId != eventObj.getButtonId()) {
							isBtnNameExist = true;
							break;
						}
					}
				}

				Integer updatedBtnId = 0;
				if (!isBtnNameExist) {
					for (Event eventObj : logisticsBtnList) {
						btnIdFromSession = eventObj.getButtonId();
						if (btnIdFromAjax == btnIdFromSession) {
							// if(buttonId == eventObj.getButtonId()) {
							eventObj.setButtonName(buttonName);
							eventObj.setButtonLink(buttonLink);
							updatedBtnId = eventObj.getButtonId();
							session.setAttribute("eventLogisticsBtns",
									logisticsBtnList);
							break;
						}
					}
					strResponse = "Success:" + type + ":" + updatedBtnId;
				} else {
					strResponse = "Button name exist";
				}
			} else if ("delete".equalsIgnoreCase(type)) {
				Integer btnIndex = 0;
				Integer deletedBtnId = 0;
				int btnIdFromAjax = buttonId;
				int btnIdFromSession = 0;
				for (Event eventObj : logisticsBtnList) {
					btnIdFromSession = eventObj.getButtonId();
					if (btnIdFromAjax == btnIdFromSession) {
						// if(buttonId == eventObj.getButtonId()) {
						deletedBtnId = eventObj.getButtonId();
						break;
					}
					btnIndex++;
				}
				logisticsBtnList.remove(logisticsBtnList.get(btnIndex));
				session.setAttribute("eventLogisticsBtns", logisticsBtnList);
				strResponse = "Success:" + type + ":" + deletedBtnId;
			}

		} catch (Exception exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strResponse;
	}

	/**
	 * This method is used to edit marker details
	 * 
	 * @param event
	 * @param result
	 * @param map
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/editEvtMarker.htm", method = RequestMethod.POST)
	public ModelAndView editEvtMarkerInfo(
			@ModelAttribute("screenSettingsForm") Event event,
			BindingResult result, ModelMap map, HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws HubCitiServiceException {
		final String strMethodName = "editEvtMarkerInfo";
		final String strViewName = "addEvtMarker";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		Integer iStatus = null;
		ArrayList<Event> eventLst = null;
		try {

			session.removeAttribute("markerInfo");
			session.removeAttribute("markerId");

			final ServletContext servletContext = request.getSession()
					.getServletContext();
			WebApplicationContext webApplicationContext = WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
			HubCitiService hubCitiService = (HubCitiService) webApplicationContext
					.getBean("hubCitiService");

			User user = (User) session.getAttribute("loginUser");

			// Adding marker details to database.
			eventLst = hubCitiService.getMarkerInfo(event, user);

			if (null != eventLst && !eventLst.isEmpty()) {
				session.setAttribute("markerInfo", eventLst.get(0));
				session.setAttribute("evtMarkerImagePreview", eventLst.get(0)
						.getEventImageName());
				request.setAttribute("btnImg", eventLst.get(0)
						.getEvtMarkerImgPath());
			}

			request.setAttribute("markerId", event.getEvtMarkerId());
			request.setAttribute("btnName", "Update");

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED
					+ exception.getMessage());
			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);

		return new ModelAndView(strViewName);

	}

}
