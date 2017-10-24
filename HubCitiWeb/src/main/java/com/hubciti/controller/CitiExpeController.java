package com.hubciti.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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
import com.hubciti.common.pojo.CityExperience;
import com.hubciti.common.pojo.CityExperienceDetail;
import com.hubciti.common.pojo.Filters;
import com.hubciti.common.pojo.FiltersDetails;
import com.hubciti.common.pojo.ScreenSettings;
import com.hubciti.common.pojo.User;
import com.hubciti.common.tags.Pagination;
import com.hubciti.common.util.Utility;
import com.hubciti.service.HubCitiService;
import com.hubciti.validator.FilterValidation;

/**
 * This class is used to handle city experience functionalities
 * 
 * @author shyamsundara_hm
 */
@Controller
public class CitiExpeController {

	/**
	 * Getting the logger instance.
	 */

	private static final Logger LOG = Logger.getLogger(CitiExpeController.class);

	/**
	 * variable for FilterValidation
	 */
	FilterValidation filterValidation;

	@Autowired
	public void setFilterValidation(FilterValidation filterValidation) {
		this.filterValidation = filterValidation;
	}

	/**
	 * This method is used to display city experience name and its assoicated
	 * retailer locations.
	 * 
	 * @param category
	 * @param result
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */

	@RequestMapping(value = "/displaycityexp.htm", method = RequestMethod.GET)
	public String displayCityExp(@ModelAttribute("CityExperienceForm") CityExperience cityExperience, BindingResult result, ModelMap model,
			HttpServletRequest request, HttpSession session) throws HubCitiServiceException {
		String strMethodName = "displayCityExp";
		String strViewName = "displaycityexp";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.CITYEXPERIENCE);
		Integer searchPageNo = null;
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		// for pagination.
		final String pageFlag = (String) request.getParameter("pageFlag");
		if (null != request.getParameter("pageNumber") && !"".equals(request.getParameter("pageNumber"))) {
			searchPageNo = (Integer) Integer.valueOf(request.getParameter("pageNumber"));
		}
		Integer lowerLimit = 0;
		String pageNumber = "0";
		int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;
		String strRetSearchKey = null;
		CityExperienceDetail objCityExperienceDetail = null;
		try {

			session.removeAttribute("cityexplst");
			session.removeAttribute("savePageNumber");
			session.removeAttribute("savePageFlag");

			if (cityExperience.getRetSearchKey() == null && null != searchPageNo && searchPageNo >= 0) {
				strRetSearchKey = (String) session.getAttribute("retsearchkey");
				cityExperience.setRetSearchKey(strRetSearchKey);

			}

			User user = (User) session.getAttribute("loginUser");

			if (null != pageFlag && "true".equals(pageFlag)) {
				pageNumber = (String) request.getParameter("pageNumber");
				final Pagination pageSess = (Pagination) session.getAttribute("pagination");
				if (null != pageNumber) {
					if (Integer.valueOf(pageNumber) != 0) {
						currentPage = Integer.valueOf(pageNumber);
						final int number = Integer.valueOf(currentPage) - 1;
						final int pageSize = pageSess.getPageRange();
						lowerLimit = pageSize * Integer.valueOf(number);
					}
				}
			} else {
				currentPage = (lowerLimit + recordCount) / recordCount;
			}
			// cityExperience.setLowerLimit(lowerLimit);

			objCityExperienceDetail = hubCitiService.displayCityExperience(cityExperience, user, lowerLimit);

			if (null != objCityExperienceDetail) {

				if (null != objCityExperienceDetail.getTotalSize()) {
					objPage = Utility.getPagination(objCityExperienceDetail.getTotalSize(), currentPage, "displaycityexp.htm", recordCount);
					session.setAttribute("pagination", objPage);
				} else {
					session.removeAttribute("pagination");
				}
			}
			// model.put("CityExperienceForm", new CityExperience());
			session.setAttribute("cityexplst", objCityExperienceDetail);
			session.setAttribute("associcityexplst", objCityExperienceDetail);
			session.setAttribute("cityExpName", objCityExperienceDetail.getCityExpName());
			session.setAttribute("cityExpId", objCityExperienceDetail.getCityExpId());
			session.setAttribute("retsearchkey", cityExperience.getRetSearchKey());
			session.setAttribute("savePageNumber", pageNumber);
			session.setAttribute("savePageFlag", pageFlag);
		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strViewName;
	}

	@RequestMapping(value = "/saveretloc.htm", method = RequestMethod.POST)
	public ModelAndView saveCityExpRetLocs(@ModelAttribute("CityExperienceForm") CityExperience cityExperience, BindingResult result, ModelMap model,
			HttpServletRequest request, HttpSession session) throws HubCitiServiceException {
		String strMethodName = "saveCityExpRetLocs";
		String strViewName = "displaycityexp";
		String strResponse = null;
		String pageNumber = null;
		String pageFlag = null;
		Integer cityExpId = null;
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.SETUPEVENTS);
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		try {

			if (cityExperience.getPageFlag() != null && !"".equals(cityExperience.getPageFlag())) {
				pageFlag = cityExperience.getPageFlag();

			} else {
				pageFlag = (String) session.getAttribute("savePageFlag");
			}

			if (cityExperience.getPageNumber() != null && !"".equals(cityExperience.getPageNumber())) {
				pageNumber = cityExperience.getPageNumber();

			} else {
				pageNumber = (String) session.getAttribute("savePageNumber");
			}

			session.removeAttribute("cityexplst");
			User user = (User) session.getAttribute("loginUser");

			String finalAssociRetLocIds = null;
			String strArray[] = null;

			// traversing string array.
			if (null != cityExperience.getRetLocIds() && !"".equals(cityExperience.getRetLocIds())) {
				strArray = cityExperience.getRetLocIds();

				for (String str : strArray) {

					if (finalAssociRetLocIds == null) {
						if (!"".equals(str)) {
							finalAssociRetLocIds = str;
						}
					} else {
						finalAssociRetLocIds = finalAssociRetLocIds + "," + str;
					}

				}
			}

			cityExpId = (Integer) session.getAttribute("cityExpId");

			cityExperience.setCityExpId(cityExpId);

			if (null != cityExperience.getUnAssociRetLocId() && !"".equals(cityExperience.getUnAssociRetLocId())) {
				strResponse = hubCitiService.deleteRetLocation(cityExperience);

			}

			if (null != finalAssociRetLocIds && !"".equals(finalAssociRetLocIds)) {

				strResponse = hubCitiService.saveCityExpRetLocs(finalAssociRetLocIds, cityExperience, user);

			}
			model.put("CityExperienceForm", cityExperience);

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);

		return new ModelAndView(new RedirectView("/HubCiti/displaycityexp.htm?pageFlag=" + pageFlag + "&pageNumber=" + pageNumber));

	}

	@RequestMapping(value = "/deleteretloc.htm", method = RequestMethod.GET)
	public @ResponseBody
	String deleteRetailLocation(@RequestParam(value = "retLocId", required = true) String retLocIds, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws HubCitiServiceException {
		String strResponse = null;
		String strMethodName = "deleteRetailLocation";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		Integer cityExpId = null;
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		CityExperience objcitCityExperience = new CityExperience();
		// try
		// {
		User user = (User) session.getAttribute("loginUser");

		cityExpId = (Integer) session.getAttribute("cityExpId");

		objcitCityExperience.setCityExpId(cityExpId);
		// strResponse = hubCitiService.deleteRetLocation(retLocIds,
		// objcitCityExperience);

		// }
		/*
		 * catch (HubCitiServiceException exception) {
		 * LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception); throw
		 * new HubCitiServiceException(exception); }
		 */

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strResponse;
	}

	/**
	 * This method will display filters.
	 * 
	 * @param filters
	 * @param result
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/displayfilters.htm", method = RequestMethod.GET)
	public String displayFilters(@ModelAttribute("filters") ScreenSettings filters, BindingResult result, ModelMap model, HttpServletRequest request,
			HttpSession session) throws HubCitiServiceException {
		final String strMethodName = "displayFilters";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.CITYEXPERIENCE);
		session.removeAttribute("filterName");
		session.removeAttribute("searchKey");
		session.removeAttribute("filterID");
		session.removeAttribute("filterLowerLimit");
		session.removeAttribute("filtersDetails");

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		String searchKey = filters.getSearchFilterName();
		final String pageFlag = request.getParameter("pageFlag");
		Integer lowerLimit = 0;
		String pageNumber = "0";
		int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;
		FiltersDetails filtersDetails = null;
		Integer cityExpId = (Integer) session.getAttribute("cityExpId");

		try {
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
				if (null != filters.getLowerLimit()) {
					lowerLimit = filters.getLowerLimit();
					currentPage = (lowerLimit + recordCount) / recordCount;
				}
			}

			if ("".equalsIgnoreCase(searchKey)) {
				searchKey = null;
				filters.setSearchKey(null);
			}

			filters.setLowerLimit(lowerLimit);
			filters.setSearchKey(searchKey);
			filters.setCityExperienceID(cityExpId);
			filtersDetails = hubCitiService.displayFilters(filters, user);

			if (null != filtersDetails) {
				if (null != filtersDetails.getTotalSize()) {
					objPage = Utility.getPagination(filtersDetails.getTotalSize(), currentPage, "displayfilters.htm", recordCount);
					session.setAttribute("pagination", objPage);
				} else {
					session.removeAttribute("pagination");
				}
			}

			if (filtersDetails.getFilters().size() <= 0) {
				request.setAttribute("responeMsg", "No Filters Found");
				request.setAttribute("responseStatus", "INFO");

				if (null == searchKey) {
					request.setAttribute("NoFilters", "NoFilters");
				}
			}

			request.setAttribute("lowerLimit", lowerLimit);
			session.setAttribute("searchFilterName", searchKey);
			session.setAttribute("filtersDetails", filtersDetails.getFilters());
			model.put("filters", filters);

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return "displayFilters";
	}

	/**
	 * This method is used to display city experience name and its assoicated
	 * retailer locations.
	 * 
	 * @param category
	 * @param result
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/searchfilters.htm", method = RequestMethod.GET)
	public String searchFilters(@ModelAttribute("filters") ScreenSettings filters, BindingResult result, ModelMap model, HttpServletRequest request,
			HttpSession session) throws HubCitiServiceException {
		String strMethodName = "searchFilters";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.CITYEXPERIENCE);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		String mode = (String) session.getAttribute("mode");
		String searchKey = null;
		Integer lowerLimit = 0;
		String message = "Filter Created Successfully";
		if (!"add".equalsIgnoreCase(mode) || "back".equalsIgnoreCase(filters.getPageType())) {
			searchKey = (String) session.getAttribute("filterName");
			lowerLimit = (Integer) session.getAttribute("filterLowerLimit");
			if (null == lowerLimit) {
				lowerLimit = 0;
			}
			message = "Filter Updated Successfully";
		}

		int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;
		FiltersDetails filtersDetails = null;
		Integer cityExpId = (Integer) session.getAttribute("cityExpId");

		try {
			User user = (User) session.getAttribute("loginUser");

			if ("".equalsIgnoreCase(searchKey)) {
				searchKey = null;
				filters.setSearchKey(null);
			}

			filters.setLowerLimit(lowerLimit);
			filters.setSearchKey(searchKey);
			filters.setCityExperienceID(cityExpId);
			filtersDetails = hubCitiService.displayFilters(filters, user);

			if (null != filtersDetails) {
				if (null != filtersDetails.getTotalSize()) {
					objPage = Utility.getPagination(filtersDetails.getTotalSize(), currentPage, "displayfilters.htm", recordCount);
					session.setAttribute("pagination", objPage);
				} else {
					session.removeAttribute("pagination");
				}
			}

			if (filtersDetails.getFilters().size() <= 0) {
				request.setAttribute("responeMsg", "No Filters Found");
				request.setAttribute("responseStatus", "INFO");
			}

			if (("add".equalsIgnoreCase(mode) || "edit".equalsIgnoreCase(mode)) && !"back".equalsIgnoreCase(filters.getPageType())) {
				request.setAttribute("responeMsg", message);
				request.setAttribute("responseStatus", ApplicationConstants.SUCCESS);
			}

			request.setAttribute("lowerLimit", lowerLimit);
			if (!"add".equalsIgnoreCase(mode)) {
				session.setAttribute("searchFilterName", searchKey);
			} else {
				session.setAttribute("searchFilterName", null);
			}
			session.setAttribute("filtersDetails", filtersDetails.getFilters());
			session.removeAttribute("filterName");
			session.removeAttribute("searchKey");
			session.removeAttribute("filterID");
			session.removeAttribute("filterLowerLimit");
			model.put("filters", filters);

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return "displayFilters";
	}

	@RequestMapping(value = "/addfilters.htm", method = RequestMethod.GET)
	public String addFilters(@ModelAttribute("filters") ScreenSettings filters, BindingResult result, ModelMap model, HttpServletRequest request,
			HttpSession session) throws HubCitiServiceException {
		String methodName = "addFilters";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.CITYEXPERIENCE);
		session.removeAttribute("selectedRetLocId");
		session.removeAttribute("filterID");

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		CityExperienceDetail retailerList = new CityExperienceDetail();
		String searchKey = null;
		String filterName = filters.getSearchFilterName();
		Integer lowerLimit = 0;
		int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;

		try {
			User user = (User) session.getAttribute("loginUser");

			if ("".equalsIgnoreCase(searchKey)) {
				searchKey = null;
			}

			retailerList = hubCitiService.searchCityExperience(searchKey, lowerLimit, null, user);

			if (null != retailerList) {
				if (null != retailerList.getTotalSize()) {
					objPage = Utility.getPagination(retailerList.getTotalSize(), currentPage, "displayretailers.htm", recordCount);
					session.setAttribute("pagination", objPage);
				} else {
					session.removeAttribute("pagination");
				}
			} else {
				session.removeAttribute("pagination");
			}

			if (null != retailerList) {
				if (retailerList.getCityExpLst().size() <= 0) {
					request.setAttribute("responeMsg", "No Retailers Found");
					request.setAttribute("responseStatus", "INFO");
				}
			}

			HashMap<Integer, String> retailLocs = new HashMap<Integer, String>();

			session.setAttribute("retailLocsMap", retailLocs);
			session.setAttribute("filterName", filterName);
			session.setAttribute("filterLowerLimit", filters.getLowerLimit());
			session.setAttribute("searchKey", searchKey);
			session.setAttribute("hiddenLowerLimit", lowerLimit);
			if (null != retailerList) {
				if (null != retailerList.getCityExpLst()) {
					session.setAttribute("retailerList", retailerList.getCityExpLst());
				} else {
					session.setAttribute("retailerList", null);
				}
			} else {
				session.setAttribute("retailerList", null);
			}
			// Minimum crop height and width for login screen
			session.setAttribute("minCropHt", 40);
			session.setAttribute("minCropWd", 40);
			session.setAttribute("filterImage", "images/upload_image.png");
			session.setAttribute("save", "save");
			session.setAttribute("mode", "add");
			session.setAttribute("associatedFlag", false);
			model.put("screenSettingsForm", new ScreenSettings());

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "addFilters";
	}

	@RequestMapping(value = "/searchretailers.htm", method = RequestMethod.GET)
	public String searchRetailers(@ModelAttribute("filters") ScreenSettings filters, BindingResult result, ModelMap model,
			HttpServletRequest request, HttpSession session) throws HubCitiServiceException {
		String methodName = "searchRetailers";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.CITYEXPERIENCE);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		CityExperienceDetail retailerList = new CityExperienceDetail();
		/*TO fix XSS issue Utility method getXssFreeString() is called*/
		String searchKey = Utility.getXssFreeString(filters.getSearchKey());
		
		Integer lowerLimit = 0;
		int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;

		try {
			User user = (User) session.getAttribute("loginUser");

			if ("".equalsIgnoreCase(searchKey)) {
				searchKey = null;
			}

			retailerList = hubCitiService.searchCityExperience(searchKey, lowerLimit, filters.getFilterID(), user);

			if (null != retailerList) {
				if (null != retailerList.getTotalSize()) {
					objPage = Utility.getPagination(retailerList.getTotalSize(), currentPage, "displayretailers.htm", recordCount);
					session.setAttribute("pagination", objPage);

					if ("edit".equalsIgnoreCase(filters.getPageType()) && null == searchKey) {
						if (retailerList.getTotalSize() > 20) {
							session.setAttribute("nextPage", true);
						} else {
							session.setAttribute("nextPage", false);
						}
					} else {
						session.removeAttribute("nextPage");
					}

					List<String> items = new ArrayList<String>();
					String hiddenRetLocIDs = new String();
					for (CityExperience cityExperience : retailerList.getCityExpLst()) {
						Boolean association = cityExperience.getAssociateFlag();

						if (association == true) {
							items.add(cityExperience.getRetLocId().toString());
							hiddenRetLocIDs += cityExperience.getRetLocId().toString() + ",";
						}
					}

					if (null != hiddenRetLocIDs) {
						if (hiddenRetLocIDs.endsWith(",")) {
							hiddenRetLocIDs = hiddenRetLocIDs.substring(0, hiddenRetLocIDs.length() - 1);
						}
					}

					session.setAttribute("hiddenRetLocIDs", hiddenRetLocIDs);
					session.setAttribute("selectedRetLocId", items);

				} else {
					session.removeAttribute("hiddenRetLocIDs");
					session.removeAttribute("selectedRetLocId");
					session.removeAttribute("nextPage");
					session.removeAttribute("pagination");
				}
			} else {
				session.removeAttribute("hiddenRetLocIDs");
				session.removeAttribute("selectedRetLocId");
				session.removeAttribute("nextPage");
				session.removeAttribute("pagination");
			}

			if (null != retailerList) {
				if (retailerList.getCityExpLst().size() <= 0) {
					request.setAttribute("responeMsg", "No Retailers Found");
					request.setAttribute("responseStatus", "INFO");
				}
			} else {
				request.setAttribute("responeMsg", "No Retailers Found");
				request.setAttribute("responseStatus", "INFO");
			}

			HashMap<Integer, String> retailLocs = new HashMap<Integer, String>();
			HashMap<Integer, String> deAssociateLocs = new HashMap<Integer, String>();
			session.setAttribute("retailLocsMap", retailLocs);
			session.setAttribute("deAssociateLocs", deAssociateLocs);
			session.setAttribute("searchKey", searchKey);
			session.setAttribute("hiddenLowerLimit", lowerLimit);
			if (null != retailerList) {
				if (null != retailerList.getCityExpLst()) {
					session.setAttribute("retailerList", retailerList.getCityExpLst());
				} else {
					session.setAttribute("retailerList", null);
				}
			} else {
				session.setAttribute("retailerList", null);
			}

			session.setAttribute("deAssociatedFlag", false);
			session.setAttribute("associatedFlag", false);
			model.put("screenSettingsForm", filters);

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "addFilters";
	}

	@RequestMapping(value = "/displayretailers.htm", method = RequestMethod.GET)
	public String displayRetailers(@ModelAttribute("screenSettingsForm") ScreenSettings filters, BindingResult result, ModelMap model,
			HttpServletRequest request, HttpSession session) throws HubCitiServiceException {
		String methodName = "displayRetailers";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.CITYEXPERIENCE);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		CityExperienceDetail retailerList = new CityExperienceDetail();
		String searchKey = filters.getSearchKey();
		final String pageFlag = request.getParameter("pageFlag");
		Integer lowerLimit = 0;
		String pageNumber = "0";
		int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;
		@SuppressWarnings("unchecked")
		HashMap<Integer, String> retailLocs = (HashMap<Integer, String>) session.getAttribute("retailLocsMap");
		@SuppressWarnings("unchecked")
		HashMap<Integer, String> deAssociateLocs = (HashMap<Integer, String>) session.getAttribute("deAssociateLocs");
		Boolean associatedFlag = (Boolean) session.getAttribute("associatedFlag");
		Boolean currentAssociatedFlag = false;

		try {
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

				Integer page = pageSess.getCurrentPage();

				if (null != retailLocs) {
					if (retailLocs.containsKey(page) && null != filters.getHiddenRetailLocs()) {
						retailLocs.remove(page);
						retailLocs.put(page, filters.getHiddenRetailLocs());
					} else {
						retailLocs.put(page, filters.getHiddenRetailLocs());
					}
				}

				if ("edit".equalsIgnoreCase(filters.getPageType()) && null == searchKey) {
					session.setAttribute("associatedFlag", false);
				} else
					if ("edit".equalsIgnoreCase(filters.getPageType())) {
						String hiddenRetLocIDs = (String) session.getAttribute("hiddenRetLocIDs");
						if (null != filters.getHiddenRetailLocs() && !"".equalsIgnoreCase(filters.getHiddenRetailLocs())
								&& !"".equalsIgnoreCase(searchKey)) {
							String currenetRetLocIDs = filters.getHiddenRetailLocs();

							if (null != hiddenRetLocIDs) {
								if (currenetRetLocIDs.equalsIgnoreCase(hiddenRetLocIDs)) {
									session.setAttribute("associatedFlag", false);
								} else {
									session.setAttribute("associatedFlag", true);
								}
							} else {
								session.setAttribute("associatedFlag", true);
							}

						} else {
							session.setAttribute("associatedFlag", false);
						}

					} else {
						if (null != filters.getHiddenRetailLocs() && !"".equalsIgnoreCase(filters.getHiddenRetailLocs())) {
							currentAssociatedFlag = true;
						}

						if (associatedFlag == true || currentAssociatedFlag == true) {
							session.setAttribute("associatedFlag", true);
						} else {
							session.setAttribute("associatedFlag", false);
						}
					}

				if ("edit".equalsIgnoreCase(filters.getPageType())) {
					Boolean deAssociatedFlag = (Boolean) session.getAttribute("deAssociatedFlag");
					Boolean currentDeAssociatedFlag = false;

					if (null != filters.getHiddenDeAssociateLocs() && !"".equalsIgnoreCase(filters.getHiddenDeAssociateLocs())
							&& (searchKey == null || "".equalsIgnoreCase(searchKey))) {
						currentDeAssociatedFlag = true;
					}

					if (deAssociatedFlag == true || currentDeAssociatedFlag == true) {
						session.setAttribute("deAssociatedFlag", true);
					} else {
						session.setAttribute("deAssociatedFlag", false);
					}

					if (null != deAssociateLocs) {
						if (deAssociateLocs.containsKey(page) && null != filters.getHiddenDeAssociateLocs()) {
							deAssociateLocs.remove(page);
							deAssociateLocs.put(page, filters.getHiddenDeAssociateLocs());
						} else {
							deAssociateLocs.put(page, filters.getHiddenDeAssociateLocs());
						}
					}

				}
			}

			if ("".equalsIgnoreCase(searchKey)) {
				searchKey = null;
			}

			retailerList = hubCitiService.searchCityExperience(searchKey, lowerLimit, filters.getFilterID(), user);

			if (null != retailerList) {
				if (retailerList.getCityExpLst().size() <= 0) {
					session.removeAttribute("retailerList");
					request.setAttribute("responeMsg", "No Retailers Found");
					request.setAttribute("responseStatus", "INFO");
				}
			} else {
				session.removeAttribute("retailerList");
				request.setAttribute("responeMsg", "No Retailers Found");
				request.setAttribute("responseStatus", "INFO");
			}

			if (retailLocs.containsKey(currentPage)) {
				String selectedRetLocId = retailLocs.get(currentPage);
				List<String> items = Arrays.asList(selectedRetLocId.split(","));
				session.setAttribute("selectedRetLocId", items);
			} else
				if (null != retailerList) {
					if (null != retailerList.getCityExpLst()) {
						List<String> items = new ArrayList<String>();
						String hiddenRetLocIDs = new String();
						for (CityExperience cityExperience : retailerList.getCityExpLst()) {
							Boolean association = cityExperience.getAssociateFlag();

							if (association == true) {
								items.add(cityExperience.getRetLocId().toString());
								hiddenRetLocIDs += cityExperience.getRetLocId().toString() + ",";
							}
						}

						if (hiddenRetLocIDs.endsWith(",")) {
							hiddenRetLocIDs = hiddenRetLocIDs.substring(0, hiddenRetLocIDs.length() - 1);
						}

						session.setAttribute("hiddenRetLocIDs", hiddenRetLocIDs);
						session.setAttribute("selectedRetLocId", items);
					}
				} else {
					session.removeAttribute("selectedRetLocId");
				}

			if (null != retailerList) {
				if (null != retailerList.getTotalSize()) {
					objPage = Utility.getPagination(retailerList.getTotalSize(), currentPage, "displayretailers.htm", recordCount);
					session.setAttribute("pagination", objPage);

					if ("edit".equalsIgnoreCase(filters.getPageType())) {
						if (retailerList.getTotalSize() > 20) {
							session.setAttribute("nextPage", true);
						} else {
							session.setAttribute("nextPage", false);
						}
					} else {
						session.removeAttribute("nextPage");
					}
				} else {
					session.removeAttribute("nextPage");
					session.removeAttribute("pagination");
				}
			} else {
				session.removeAttribute("nextPage");
				session.removeAttribute("pagination");
			}

			session.setAttribute("retailLocsMap", retailLocs);
			session.setAttribute("deAssociateLocs", deAssociateLocs);
			request.setAttribute("lowerLimit", filters.getLowerLimit());
			session.setAttribute("searchKey", searchKey);
			session.setAttribute("hiddenLowerLimit", lowerLimit);
			if (null != retailerList) {
				session.setAttribute("retailerList", retailerList.getCityExpLst());
			} else {
				session.removeAttribute("retailerList");
			}
			model.put("screenSettingsForm", filters);

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "addFilters";
	}

	@RequestMapping(value = "/savefilters.htm", method = RequestMethod.POST)
	public ModelAndView saveFilters(@ModelAttribute("screenSettingsForm") ScreenSettings filters, BindingResult result, ModelMap model,
			HttpServletRequest request, HttpSession session) throws HubCitiServiceException {
		String methodName = "saveFilters";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.CITYEXPERIENCE);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		int currentPage = 1;
		final int recordCount = 20;
		@SuppressWarnings("unchecked")
		HashMap<Integer, String> retailLocs = (HashMap<Integer, String>) session.getAttribute("retailLocsMap");
		String retailerIds = new String();
		Integer cityExpId = (Integer) session.getAttribute("cityExpId");
		Integer hiddenLowerLimit = (Integer) session.getAttribute("hiddenLowerLimit");

		try {
			User user = (User) session.getAttribute("loginUser");

			if (null != retailLocs) {
				Integer page = (hiddenLowerLimit + recordCount) / recordCount;
				if (retailLocs.containsKey(page) && null != filters.getHiddenRetailLocs()) {
					retailLocs.remove(page);
					retailLocs.put(page, filters.getHiddenRetailLocs());
				} else {
					retailLocs.put(page, filters.getHiddenRetailLocs());
				}
			}

			for (Integer key : retailLocs.keySet()) {
				String retailerId = retailLocs.get(key);
				if (!"".equalsIgnoreCase(retailerId)) {
					retailerIds += retailerId;
					retailerIds += ",";
				}
			}
			if (null != retailerIds) {
				if (retailerIds.endsWith(",")) {
					retailerIds = retailerIds.substring(0, retailerIds.length() - 1);
				}
			}

			Filters filterDetails = new Filters();

			filterDetails.setFilterName(filters.getFilterName());
			filterDetails.setLogoImageName(filters.getLogoImageName());
			filterDetails.setRetailerLocIds(retailerIds);
			filterDetails.setCityExperienceID(cityExpId);
			filterDetails.setNextPage(false);

			filterValidation.validate(filterDetails, result);

			if (result.hasErrors()) {
				if (retailLocs.containsKey(currentPage)) {
					String selectedRetLocId = retailLocs.get(currentPage);
					List<String> items = Arrays.asList(selectedRetLocId.split(","));
					session.setAttribute("selectedRetLocId", items);
				}
				request.setAttribute("lowerLimit", filters.getLowerLimit());
				session.setAttribute("retailLocsMap", retailLocs);
				return new ModelAndView("addFilters");
			}

			String response = hubCitiService.saveFilters(filterDetails, user);

			if (response.equalsIgnoreCase(ApplicationConstants.DUPLICATEFILTER)) {
				filterValidation.validate(filterDetails, result, ApplicationConstants.DUPLICATEFILTER);

				if (result.hasErrors()) {
					if (retailLocs.containsKey(currentPage)) {
						String selectedRetLocId = retailLocs.get(currentPage);
						List<String> items = Arrays.asList(selectedRetLocId.split(","));
						session.setAttribute("selectedRetLocId", items);
					}

					session.setAttribute("retailLocsMap", retailLocs);
					return new ModelAndView("addFilters");
				}
			} else
				if (response.equalsIgnoreCase(ApplicationConstants.FAILURE)) {
					request.setAttribute("responeMsg", "Error while creating filter");
					request.setAttribute("responseStatus", response);
				} else {
					session.removeAttribute("selectedRetLocId");

					if ("Save".equalsIgnoreCase(filters.getPageTypeHid())) {
						session.setAttribute("mode", "add");

						LOG.info(ApplicationConstants.METHODEND + methodName);
						return new ModelAndView(new RedirectView("searchfilters.htm"));
					} else {
						Integer filterID = Integer.valueOf(response);
						session.setAttribute("filterID", filterID);
						session.setAttribute("searchKey", filters.getSearchKey());

						LOG.info(ApplicationConstants.METHODEND + methodName);
						return new ModelAndView(new RedirectView("editfilters.htm"));
					}

				}

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}
		return new ModelAndView("addFilters");
	}

	@RequestMapping(value = "/editfilters.htm", method = RequestMethod.GET)
	public String editFilters(@ModelAttribute("filters") ScreenSettings filters, BindingResult result, ModelMap model, HttpServletRequest request,
			HttpSession session) throws HubCitiServiceException {
		String methodName = "editFilters";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.CITYEXPERIENCE);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		CityExperienceDetail retailerList = new CityExperienceDetail();
		String searchKey = (String) session.getAttribute("searchKey");
		String filterName = filters.getSearchFilterName();
		if (null == filterName) {
			filterName = (String) session.getAttribute("filterName");
		}
		Integer lowerLimit = 0;
		int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;
		ScreenSettings filterDetails = null;

		try {
			session.removeAttribute("associretids");
			User user = (User) session.getAttribute("loginUser");

			Integer filterID = filters.getFilterID();

			if (null == filterID) {
				filterID = (Integer) session.getAttribute("filterID");
			}

			if ("".equalsIgnoreCase(searchKey)) {
				searchKey = null;
			}

			retailerList = hubCitiService.searchCityExperience(searchKey, lowerLimit, filterID, user);

			filterDetails = hubCitiService.fetchFilterDetails(user.getHubCitiID(), filterID);

			if (null != retailerList) {
				if (null != retailerList.getTotalSize()) {
					objPage = Utility.getPagination(retailerList.getTotalSize(), currentPage, "displayretailers.htm", recordCount);
					session.setAttribute("pagination", objPage);
					if (retailerList.getTotalSize() > 20 && (null == searchKey || "".equalsIgnoreCase(searchKey))) {
						session.setAttribute("nextPage", true);
					} else {
						session.setAttribute("nextPage", false);
					}
				} else {
					session.removeAttribute("nextPage");
					session.removeAttribute("pagination");
				}
			} else {
				session.removeAttribute("nextPage");
				session.removeAttribute("pagination");
			}

			if (null != retailerList) {
				if (retailerList.getCityExpLst().size() <= 0) {
					request.setAttribute("responeMsg", "No Retailers Found");
					request.setAttribute("responseStatus", "INFO");
				}
			} else {
				request.setAttribute("responeMsg", "No Retailers Found");
				request.setAttribute("responseStatus", "INFO");
			}

			HashMap<Integer, String> retailLocs = new HashMap<Integer, String>();
			HashMap<Integer, String> deAssociateLocs = new HashMap<Integer, String>();

			if (null != retailerList) {
				if (null != retailerList.getCityExpLst()) {
					List<String> items = new ArrayList<String>();
					String hiddenRetLocIDs = new String();
					for (CityExperience cityExperience : retailerList.getCityExpLst()) {
						Boolean association = cityExperience.getAssociateFlag();

						if (association == true) {
							items.add(cityExperience.getRetLocId().toString());
							hiddenRetLocIDs += cityExperience.getRetLocId().toString() + ",";
						}
					}
					if (null != hiddenRetLocIDs) {
						if (hiddenRetLocIDs.endsWith(",")) {
							hiddenRetLocIDs = hiddenRetLocIDs.substring(0, hiddenRetLocIDs.length() - 1);
						}
					}

					session.setAttribute("hiddenRetLocIDs", hiddenRetLocIDs);
					session.setAttribute("selectedRetLocId", items);
					session.setAttribute("associretids", hiddenRetLocIDs);

				} else {
					session.removeAttribute("hiddenRetLocIDs");
				}
			} else {
				session.removeAttribute("hiddenRetLocIDs");
			}

			session.setAttribute("retailLocsMap", retailLocs);
			session.setAttribute("deAssociateLocs", deAssociateLocs);
			session.setAttribute("filterID", filterID);
			session.setAttribute("filterName", filterName);
			Integer filterLowerLimit = filters.getLowerLimit();
			if (null == filterLowerLimit) {
				filterLowerLimit = (Integer) session.getAttribute("filterLowerLimit");
			}
			session.setAttribute("filterLowerLimit", filters.getLowerLimit());
			if (null != retailerList) {
				session.setAttribute("retailerList", retailerList.getCityExpLst());
			} else {
				session.removeAttribute("retailerList");
			}
			// Minimum crop height and width for login screen
			session.setAttribute("minCropHt", 40);
			session.setAttribute("minCropWd", 40);
			session.setAttribute("filterImage", filterDetails.getImagePath());
			session.setAttribute("save", "update");
			session.setAttribute("mode", "edit");
			session.setAttribute("deAssociatedFlag", false);
			session.setAttribute("associatedFlag", false);
			session.setAttribute("hiddenLowerLimit", lowerLimit);
			model.put("screenSettingsForm", filterDetails);

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "addFilters";
	}

	@RequestMapping(value = "/updatefilters.htm", method = RequestMethod.POST)
	public ModelAndView updateFilters(@ModelAttribute("screenSettingsForm") ScreenSettings filters, BindingResult result, ModelMap model,
			HttpServletRequest request, HttpSession session) throws HubCitiServiceException {
		String methodName = "updateFilters";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.CITYEXPERIENCE);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		int currentPage = 1;
		final int recordCount = 20;
		@SuppressWarnings("unchecked")
		HashMap<Integer, String> retailLocs = (HashMap<Integer, String>) session.getAttribute("retailLocsMap");
		@SuppressWarnings("unchecked")
		HashMap<Integer, String> deAssociateLocs = (HashMap<Integer, String>) session.getAttribute("deAssociateLocs");
		String retailerIds = new String();
		String retLocIds = new String();
		String deAssociateRetailerIds = new String();
		Integer cityExpId = (Integer) session.getAttribute("cityExpId");
		Integer hiddenLowerLimit = (Integer) session.getAttribute("hiddenLowerLimit");
		Set<String> finalRetIdslst = new HashSet<String>();
		// Set<Integer> set = new HashSet<Integer>();
		String[] temp;
		String[] retIds;
		try {
			User user = (User) session.getAttribute("loginUser");

			if (null != retailLocs) {
				if (null == hiddenLowerLimit) {
					hiddenLowerLimit = 0;
				}

				Integer page = (hiddenLowerLimit + recordCount) + recordCount;

				if (retailLocs.containsKey(page) && null != filters.getHiddenRetailLocs()) {
					retailLocs.remove(page);
					retailLocs.put(page, filters.getHiddenRetailLocs());
				} else {
					retailLocs.put(page, filters.getHiddenRetailLocs());
				}
			}

			if (null != deAssociateLocs) {
				if (deAssociateLocs.containsKey(currentPage) && null != filters.getHiddenDeAssociateLocs()) {
					deAssociateLocs.remove(currentPage);
					deAssociateLocs.put(currentPage, filters.getHiddenDeAssociateLocs());
				} else {
					deAssociateLocs.put(currentPage, filters.getHiddenDeAssociateLocs());
				}
			}

			for (Integer key : retailLocs.keySet()) {

				String retailerId = retailLocs.get(key);
				if (!"".equalsIgnoreCase(retailerId)) {

					retailerIds += retailerId;
					retailerIds += ",";

				}
			}

			
			if (null != retailerIds) {
				if (retailerIds.startsWith(",")) {
					retailerIds = retailerIds.substring(1, retailerIds.length());
				}
			}
			if (!Utility.isEmptyOrNullString(retailerIds)) {
				retIds = retailerIds.split(",");

				for (int i = 0; i < retIds.length; i++) {

					finalRetIdslst.add(retIds[i]);
				}

			}

		/*	if (null != retailerIds) {
				if (retailerIds.endsWith(",")) {
					retailerIds = retailerIds.substring(0, retailerIds.length() - 1);
				}
			}

			if (null != retailerIds) {
				if (retailerIds.startsWith(",")) {
					retailerIds = retailerIds.substring(1, retailerIds.length());
				}
			}*/
			for (Integer key : deAssociateLocs.keySet()) {
				String deAssociateRetailerId = deAssociateLocs.get(key);
				if (!"".equalsIgnoreCase(deAssociateRetailerId)) {
					deAssociateRetailerIds += deAssociateRetailerId;
					deAssociateRetailerIds += ",";
				}
			}
			if (null != deAssociateRetailerIds) {
				if (deAssociateRetailerIds.endsWith(",")) {
					deAssociateRetailerIds = deAssociateRetailerIds.substring(0, deAssociateRetailerIds.length() - 1);
				}
			}

			if (!Utility.isEmptyOrNullString(deAssociateRetailerIds)) {
				temp = deAssociateRetailerIds.split(",");

				for (int i = 0; i < temp.length; i++) {
					if (null != finalRetIdslst && !finalRetIdslst.isEmpty()) {
						if (finalRetIdslst.contains(temp[i])) {
							finalRetIdslst.remove(temp[i]);
						}
					}

				}

			}

			for (String s : finalRetIdslst) {

				retLocIds += s;
				retLocIds += ",";
			}
			if (null != retLocIds) {
				if (retLocIds.endsWith(",")) {
					retLocIds = retLocIds.substring(0, retLocIds.length() - 1);
				}
				if (retLocIds.startsWith(",")) {
					retLocIds = retLocIds.substring(1, retLocIds.length() );
				}
			}
			
			Filters filterDetails = new Filters();

			filterDetails.setFilterName(filters.getFilterName());
			filterDetails.setLogoImageName(filters.getLogoImageName());
			filterDetails.setRetailerLocIds(retLocIds);
			filterDetails.setCityExperienceID(cityExpId);
			filterDetails.setFilterID(filters.getFilterID());
			Boolean nextPage = (Boolean) session.getAttribute("nextPage");

			if (null != nextPage) {
				filterDetails.setNextPage(nextPage);
			} else {
				filterDetails.setNextPage(false);
			}

			/* To fix XSS */
			filterDetails.setFilterName(Utility.getXssFreeString(filterDetails.getFilterName()));
			
			filterValidation.validate(filterDetails, result);

			if (result.hasErrors()) {
				if (retailLocs.containsKey(currentPage)) {
					String selectedRetLocId = retailLocs.get(currentPage);
					List<String> items = Arrays.asList(selectedRetLocId.split(","));
					session.setAttribute("selectedRetLocId", items);
				}
				request.setAttribute("lowerLimit", filters.getLowerLimit());
				session.setAttribute("retailLocsMap", retailLocs);
				return new ModelAndView("addFilters");
			}

			String deAssociateresponse = hubCitiService.deAssociateFilterRetailLocs(filters.getFilterID(), deAssociateRetailerIds);

			if (!deAssociateresponse.equalsIgnoreCase(ApplicationConstants.SUCCESS)) {
				request.setAttribute("responeMsg", "Error deassociating filter");
				request.setAttribute("responseStatus", deAssociateresponse);
				return new ModelAndView("addFilters");
			}

			if ("".equalsIgnoreCase(retailerIds)) {
				filterDetails.setRetailerLocIds(null);
			}
			String response = hubCitiService.saveFilters(filterDetails, user);

			if (response.equalsIgnoreCase(ApplicationConstants.FAILURE)) {
				request.setAttribute("responeMsg", "Error while updating filter");
				request.setAttribute("responseStatus", response);
			} else {
				session.removeAttribute("selectedRetLocId");

				if ("Update".equalsIgnoreCase(filters.getPageTypeHid())) {
					session.setAttribute("mode", "edit");

					LOG.info(ApplicationConstants.METHODEND + methodName);
					return new ModelAndView(new RedirectView("searchfilters.htm"));
				} else {
					session.setAttribute("searchKey", filters.getSearchKey());

					LOG.info(ApplicationConstants.METHODEND + methodName);
					return new ModelAndView(new RedirectView("editfilters.htm"));
				}
			}

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return new ModelAndView("displayFilters");
	}

	@RequestMapping(value = "/deletefilters.htm", method = RequestMethod.POST)
	public String deleteFilters(@ModelAttribute("screenSettingsForm") ScreenSettings filters, BindingResult result, ModelMap model,
			HttpServletRequest request, HttpSession session) throws HubCitiServiceException {
		String methodName = "deleteFilters";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.CITYEXPERIENCE);

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

		String searchKey = filters.getSearchFilterName();
		Integer lowerLimit = 0;
		int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;
		Integer cityExpId = (Integer) session.getAttribute("cityExpId");

		try {
			User user = (User) session.getAttribute("loginUser");

			String response = hubCitiService.deleteFilter(filters.getFilterID(), user.getHubCitiID());

			if (response.equalsIgnoreCase(ApplicationConstants.SUCCESS)) {
				request.setAttribute("responeMsg", "Filter Deleted successfully");
				request.setAttribute("responseStatus", ApplicationConstants.SUCCESS);
			} else {
				request.setAttribute("responeMsg", "Filter has been associated to tab bar. Please deassociate and delete");
				request.setAttribute("responseStatus", response);
			}

			if (null != filters.getLowerLimit()) {
				lowerLimit = filters.getLowerLimit();
			}

			currentPage = (lowerLimit + recordCount) / recordCount;

			filters.setLowerLimit(lowerLimit);
			filters.setSearchKey(searchKey);
			filters.setCityExperienceID(cityExpId);
			FiltersDetails filterslst = hubCitiService.displayFilters(filters, user);

			if (null != filterslst) {
				if (null != filterslst.getTotalSize()) {
					objPage = Utility.getPagination(filterslst.getTotalSize(), currentPage, "displayfilters.htm", recordCount);
					session.setAttribute("pagination", objPage);
				} else {
					session.removeAttribute("pagination");
				}
			} else {
				session.removeAttribute("pagination");
			}

			request.setAttribute("lowerLimit", lowerLimit);
			session.setAttribute("searchFilterName", searchKey);
			if (null != filterslst) {
				session.setAttribute("filtersDetails", filterslst.getFilters());
			} else {
				session.removeAttribute("filtersDetails");
			}
			model.put("filters", filters);

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "displayFilters";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/savefilterorder", method = RequestMethod.POST)
	@ResponseBody
	public final String saveOrder(@RequestParam(value = "saveOrder", required = true) String saveOrder,
			@RequestParam(value = "filterOrder", required = true) String filterOrder, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws HubCitiServiceException {

		String methodName = "saveiconictemplate";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String responseStr = null;
		Integer cityExpId = null;
		try {
			final ServletContext servletContext = request.getSession().getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
			cityExpId = (Integer) session.getAttribute("cityExpId");
			User user = (User) session.getAttribute("loginUser");
			responseStr = hubCitiService.saveFilterOrder(user.getHubCitiID(), filterOrder, cityExpId, saveOrder, user.gethCAdminUserID());
		} catch (HubCitiServiceException exception) {
			LOG.error("Exception occurred in saveOrder method:" + exception);
			throw new HubCitiServiceException(exception);
		}

		return responseStr;
	}
}
