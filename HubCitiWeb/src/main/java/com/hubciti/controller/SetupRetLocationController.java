package com.hubciti.controller;

import java.util.List;
import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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
import com.hubciti.common.pojo.User;
import com.hubciti.common.tags.Pagination;
import com.hubciti.common.util.Utility;
import com.hubciti.service.HubCitiService;

/**
 * This class is used to display retailer locations based on state, city and
 * zipcodes. HubCiti Admin can assoicate or un associate retailer locations.
 * 
 * @author shyamsundara_hm
 */
@Controller
public class SetupRetLocationController {

	/**
	 * Getting logger instance.
	 */
	public static final Logger Log = Logger.getLogger(SetupRetLocationController.class);

	/**
	 * This method is used to display associated retailer list.
	 * 
	 * @param cityExperience
	 * @param result
	 * @param map
	 * @param request
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/getassociretlocs", method = RequestMethod.GET)
	public String getAssocitedRetLocation(@ModelAttribute("SetupRetLocationForm") CityExperience cityExperience, BindingResult result,
			ModelMap model, HttpServletRequest request, HttpSession session) throws HubCitiServiceException

	{
		String strMethodName = "getAssocitedRetLocation";
		Log.info(ApplicationConstants.METHODSTART + strMethodName);
		session.setAttribute("menuName", ApplicationConstants.SETUPRETAILERLOCATION);
		Log.info(ApplicationConstants.METHODEND + strMethodName);
		List<CityExperience> retStateLst = null;
		int iHubCitiId = 0;
		String strViewName = "getassociretlocs";
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		CityExperienceDetail objCityExperienceDetail = null;
		// For pagination.
		int iCurrentPage = 1;
		Pagination objPage = null;
		String pageNumber = "0";
		String pageFlag = request.getParameter("pageFlag");
		Integer lowerLimit = 0;
		Integer iRecordCount = 10;
		String strRetSearchKey = null;
		String strDeAssociateRes = null;

		try {
			// Removing session variables.
			session.removeAttribute("retstatelst");
			session.removeAttribute("retailerlst");
			session.removeAttribute("retsearchkey");
			session.removeAttribute("selcity");
			session.removeAttribute("selstate");
			session.removeAttribute("selzipcode");
			
			/* To Fix XSS issue*/
			if(null != cityExperience.getStateName() && !"".equals(cityExperience.getStateName()))	{
				cityExperience.setStateName(Utility.getXssFreeString(cityExperience.getStateName()));
			}
			if(null != cityExperience.getCity() && !"".equals(cityExperience.getCity()))	{
				cityExperience.setCity(Utility.getXssFreeString(cityExperience.getCity()));
			}

			if (null != request.getParameter("deassociresp") && !"".equals(request.getParameter("deassociresp"))) {

				strDeAssociateRes = request.getParameter("deassociresp");
				request.setAttribute("errorMessage", strDeAssociateRes);
			}

			if (cityExperience.getRetSearchKey() == null) {
				strRetSearchKey = (String) session.getAttribute("retsearchkey");
				cityExperience.setRetSearchKey(strRetSearchKey);

			}
			User user = (User) session.getAttribute("loginUser");

			if (null != user) {
				iHubCitiId = user.getHubCitiID();
			}
			// Fetching state list.
			retStateLst = hubCitiService.getStatelst(iHubCitiId);

			// For pagination.

			if (null != pageFlag && "true".equals(pageFlag)) {
				pageNumber = request.getParameter("pageNumber");
				Pagination objPagination = (Pagination) session.getAttribute("pagination");

				if (Integer.valueOf(pageNumber) != 0) {
					iCurrentPage = Integer.valueOf(pageNumber);
					final int number = Integer.valueOf(iCurrentPage) - 1;
					final int pageSize = objPagination.getPageRange();
					lowerLimit = pageSize * Integer.valueOf(number);
				}

			} else {

				iCurrentPage = (lowerLimit + iRecordCount) / iRecordCount;
			}

			// Below validations for retailer search

			if ("".equals(cityExperience.getStateName())) {
				cityExperience.setStateName(null);
			} else
				if (null != cityExperience.getStateName() && "undefined".equals(cityExperience.getStateName())) {
					cityExperience.setStateName(null);
				}
			// for city.

			if ("".equals(cityExperience.getCity())) {
				cityExperience.setCity(null);
			} else
				if (null != cityExperience.getCity() && "undefined".equals(cityExperience.getCity())) {
					cityExperience.setCity(null);
				}
			// for postal code.

			if ("".equals(cityExperience.getPostalCode())) {
				cityExperience.setPostalCode(null);
			} else
				if (null != cityExperience.getPostalCode() && "undefined".equals(cityExperience.getPostalCode())) {
					cityExperience.setPostalCode(null);
				}
			// for retailer search key.

			if ("".equals(cityExperience.getRetSearchKey())) {

				cityExperience.setRetSearchKey(null);
			}

			// Fetching associated retailer list.
			cityExperience.setAssociateFlag(true);
			cityExperience.setHubCitiId(iHubCitiId);

			if (null == cityExperience.getLowerLimit()) {
				cityExperience.setLowerLimit(0);
			} else {
				cityExperience.setLowerLimit(lowerLimit);
			}
			objCityExperienceDetail = hubCitiService.getRetailer(cityExperience);

			if (null != objCityExperienceDetail) {
				if (null != objCityExperienceDetail.getTotalSize()) {
					Pagination pagination = Utility.getPagination(objCityExperienceDetail.getTotalSize(), iCurrentPage, "getassociretlocs",
							iRecordCount);
					session.setAttribute("pagination", pagination);

				} else {

					session.removeAttribute("pagination");

				}

			}

			session.setAttribute("retstatelst", retStateLst);
			session.setAttribute("retailerlst", objCityExperienceDetail);
			session.setAttribute("retsearchkey", cityExperience.getRetSearchKey());
			// session.setAttribute("searchKey", searchKey);
			session.setAttribute("selcity", cityExperience.getCity());
			session.setAttribute("selstate", cityExperience.getStateName());
			session.setAttribute("selzipcode", cityExperience.getPostalCode());
			model.put("SetupRetLocationForm", cityExperience);

		} catch (HubCitiServiceException exception) {
			Log.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}
		Log.info(ApplicationConstants.METHODEND + strMethodName);
		return strViewName;
	}

	/**
	 * This method is used to display associated retailer list.
	 * 
	 * @param cityExperience
	 * @param result
	 * @param map
	 * @param request
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */

	@RequestMapping(value = "/getunassociretlocs", method = RequestMethod.GET)
	public String getUnAssocitedRetLocation(@ModelAttribute("SetupRetLocationForm") CityExperience cityExperience, BindingResult result,
			ModelMap model, HttpServletRequest request, HttpSession session) throws HubCitiServiceException

	{
		String strMethodName = "getUnAssocitedRetLocation";
		Log.info(ApplicationConstants.METHODSTART + strMethodName);
		session.setAttribute("menuName", ApplicationConstants.SETUPRETAILERLOCATION);
		Log.info(ApplicationConstants.METHODEND + strMethodName);
		String strViewName = "getunassociretlocs";
		List<CityExperience> retStateLst = null;
		int iHubCitiId = 0;
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		CityExperienceDetail objCityExperienceDetail = null;
		// For pagination.
		int iCurrentPage = 1;
		Pagination objPage = null;
		String pageNumber = "0";
		String pageFlag = request.getParameter("pageFlag");
		Integer lowerLimit = 0;
		Integer iRecordCount = 10;
		String strRetSearchKey = null;
		String strAssociateRes = null;
		try {
			// Removing session variables.
			session.removeAttribute("retstatelst");
			session.removeAttribute("unassocretlst");
			session.removeAttribute("retsearchkey");
			session.removeAttribute("selcity");
			session.removeAttribute("selstate");
			session.removeAttribute("selzipcode");

			if (null != request.getParameter("unassociresp") && !"".equals(request.getParameter("unassociresp"))) {

				strAssociateRes = request.getParameter("unassociresp");
				request.setAttribute("errorMessage", strAssociateRes);
			}

			if (cityExperience.getRetSearchKey() == null) {
				strRetSearchKey = (String) session.getAttribute("retsearchkey");
				cityExperience.setRetSearchKey(strRetSearchKey);

			}
			User user = (User) session.getAttribute("loginUser");

			if (null != user) {
				iHubCitiId = user.getHubCitiID();
			}
			// Fetching state list.
			retStateLst = hubCitiService.getStatelst(iHubCitiId);

			// For pagination.

			if (null != pageFlag && "true".equals(pageFlag)) {
				pageNumber = request.getParameter("pageNumber");
				Pagination objPagination = (Pagination) session.getAttribute("pagination");

				if (Integer.valueOf(pageNumber) != 0) {
					iCurrentPage = Integer.valueOf(pageNumber);
					final int number = Integer.valueOf(iCurrentPage) - 1;
					final int pageSize = objPagination.getPageRange();
					lowerLimit = pageSize * Integer.valueOf(number);
				}

			} else {

				iCurrentPage = (lowerLimit + iRecordCount) / iRecordCount;
			}

			// Below validations for retailer search

			if ("".equals(cityExperience.getStateName())) {
				cityExperience.setStateName(null);
			} else
				if (null != cityExperience.getStateName() && "undefined".equals(cityExperience.getStateName())) {
					cityExperience.setStateName(null);
				}
			// for city.

			if ("".equals(cityExperience.getCity())) {
				cityExperience.setCity(null);
			} else
				if (null != cityExperience.getCity() && "undefined".equals(cityExperience.getCity())) {
					cityExperience.setCity(null);
				}

			// for postal code.
			if ("".equals(cityExperience.getPostalCode())) {
				cityExperience.setPostalCode(null);
			} else
				if (null != cityExperience.getPostalCode() && "undefined".equals(cityExperience.getPostalCode())) {
					cityExperience.setPostalCode(null);
				}
			// for retailer search key.

			if ("".equals(cityExperience.getRetSearchKey())) {

				cityExperience.setRetSearchKey(null);
			}

			// Fetching associated retailer list.
			cityExperience.setAssociateFlag(false);
			cityExperience.setHubCitiId(iHubCitiId);

			if (null == cityExperience.getLowerLimit()) {
				cityExperience.setLowerLimit(0);
			} else {
				cityExperience.setLowerLimit(lowerLimit);
			}
			objCityExperienceDetail = hubCitiService.getRetailer(cityExperience);

			if (null != objCityExperienceDetail) {
				if (null != objCityExperienceDetail.getTotalSize()) {
					Pagination pagination = Utility.getPagination(objCityExperienceDetail.getTotalSize(), iCurrentPage, "getunassociretlocs",
							iRecordCount);
					session.setAttribute("pagination", pagination);

				} else {

					session.removeAttribute("pagination");

				}

			}

			session.setAttribute("retstatelst", retStateLst);
			session.setAttribute("unassocretlst", objCityExperienceDetail);
			session.setAttribute("retsearchkey", cityExperience.getRetSearchKey());
			// session.setAttribute("searchKey", searchKey);
			session.setAttribute("selcity", cityExperience.getCity());
			session.setAttribute("selstate", cityExperience.getStateName());
			session.setAttribute("selzipcode", cityExperience.getPostalCode());
			model.put("SetupRetLocationForm", cityExperience);

		} catch (HubCitiServiceException exception) {
			Log.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}
		Log.info(ApplicationConstants.METHODEND + strMethodName);
		return strViewName;
	}

	/**
	 * This method is used to display cities list based on state.
	 * 
	 * @param stateName
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/getcitis", method = RequestMethod.GET)
	public @ResponseBody
	final String getCitiLst(@RequestParam(value = "state", required = true) String stateName, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws HubCitiServiceException {
		String strMethodName = "getStatelst";
		Log.info(ApplicationConstants.METHODSTART + strMethodName);
		List<CityExperience> cityLst = null;
		final StringBuffer innerHtml = new StringBuffer();
		String strHtml = null;
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		int iHubCitiId = 0;
		String city = null;
		try {
			// session.removeAttribute("selzipcode");

			strHtml = "<span class='lesWdth'><select name='city' title='Select City'  class='slctBx' id='rtlr-city'  onchange='loadZipcodes()'><option value=''>--Select--</option>";
			innerHtml.append(strHtml);
			response.setContentType("text/html");
			User user = (User) session.getAttribute("loginUser");

			if (null != user) {
				iHubCitiId = user.getHubCitiID();
			}
			cityLst = hubCitiService.getCitilst(iHubCitiId, stateName);

			/*
			 * if (null != prvstate && !"".equals(prvstate) && null != stateName
			 * && !"".equals(stateName)) { if (!prvstate.equals(stateName)) {
			 * session.removeAttribute("selcity"); } }
			 */
			// session.setAttribute("prevstate", stateName);

			String selCity = (String) session.getAttribute("selcity");
			if (null == selCity || "".equals(selCity)) {
				selCity = city;
			}
			if (null != cityLst && !cityLst.isEmpty()) {
				for (int i = 0; i < cityLst.size(); i++) {
					final String cityName = cityLst.get(i).getCity();
					if (null != selCity && cityName.equals(selCity)) {
						innerHtml.append("<option selected=true >" + cityLst.get(i).getCity() + "</option>");
					} else {
						innerHtml.append("<option>" + cityLst.get(i).getCity() + "</option>");
					}

				}

			}
			innerHtml
					.append("</select></span><a href='#'  title='Cancel' class='clrOptn rtlr-city'><img src='images/icon-cncl.png' width='12' height='12' alt='cancel' /></a>");

		} catch (HubCitiServiceException exception) {
			Log.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}
		Log.info(ApplicationConstants.METHODEND + strMethodName);
		return innerHtml.toString();
	}

	/**
	 * This method is used to display zipcode list based on state and city.
	 * 
	 * @param state
	 * @param city
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/getzipcodes", method = RequestMethod.GET)
	public @ResponseBody
	final String getZipcodeLst(@RequestParam(value = "state", required = true) String state,
			@RequestParam(value = "city", required = true) String city, HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws HubCitiServiceException {
		String strMethodName = "getZipcodeLst";
		Log.info(ApplicationConstants.METHODSTART + strMethodName);
		List<CityExperience> zipcodeLst = null;
		final StringBuffer innerHtml = new StringBuffer();
		String strHtml = null;
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		int iHubCitiId = 0;
		CityExperience objCityExperience = new CityExperience();
		String zipcode = null;
		String[] strZipCodeArray = null;
		boolean isSame = false;
		String strContains = null;
		try {

			strHtml = "<span class='lesWdth'><select name='postalCode' title='Select Zipcode'  id='multi-slct' multiple='multiple' class='slctBx multiSlct'>";
			innerHtml.append(strHtml);
			response.setContentType("text/xml");
			User user = (User) session.getAttribute("loginUser");

			/*
			 * if (null != prevcity && !"".equals(prevcity) && null != city &&
			 * !"".equals(city)) { if (!prevcity.equals(city)) {
			 * session.removeAttribute("selzipcode"); } }
			 * session.setAttribute("prevcity", city);
			 */
			if (null != user) {
				iHubCitiId = user.getHubCitiID();
			}

			objCityExperience.setHubCitiId(iHubCitiId);
			objCityExperience.setState(state);
			objCityExperience.setCity(city);

			zipcodeLst = hubCitiService.getZipcodelst(objCityExperience);

			String selZipcode = (String) session.getAttribute("selzipcode");

			if (null == selZipcode || "".equals(selZipcode)) {
				selZipcode = zipcode;
			}

			if (null != zipcodeLst && !zipcodeLst.isEmpty()) {
				for (CityExperience exp : zipcodeLst) {
					Boolean postalFlag = false;
					final String sZipcode = exp.getPostalCode();

					if (null != selZipcode && !"".equals(selZipcode)) {
						List<String> postalList = Arrays.asList(selZipcode.split(","));

						for (String postalCode : postalList) {
							if (sZipcode.equals(postalCode)) {
								innerHtml.append("<option selected=true>" + sZipcode + "</option>");
								postalFlag = true;
								break;
							}
						}
						if (!postalFlag) {
							innerHtml.append("<option>" + sZipcode + "</option>");
						}

					} else {
						innerHtml.append("<option>" + sZipcode + "</option>");
					}

				}
			}
			innerHtml
					.append("</select></span><a href='#' title='Cancel'  class='clrOptn multi-slct'><img src='images/icon-cncl.png' width='12' height='12' alt='cancel' /></a>");

		} catch (HubCitiServiceException exception) {
			Log.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}
		Log.info(ApplicationConstants.METHODEND + strMethodName);

		return innerHtml.toString();

	}

	/**
	 * This
	 * 
	 * @param cityExperience
	 * @param result
	 * @param map
	 * @param request
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */

	@RequestMapping(value = "/deassociateret.htm", method = RequestMethod.POST)
	public ModelAndView deAssociateRetailer(@ModelAttribute("SetupRetLocationForm") CityExperience cityExperience, BindingResult result,
			ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws HubCitiServiceException {
		String strMethodName = "deAssociateRetailer";
		Log.info(ApplicationConstants.METHODSTART + strMethodName);
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		String strResponse = null;
		int iHubCitiId = 0;
		CityExperience objCityExperience = new CityExperience();
		try {
			User user = (User) session.getAttribute("loginUser");

			if (null != user) {
				iHubCitiId = user.getHubCitiID();
			}

			objCityExperience.setHubCitiId(iHubCitiId);
			objCityExperience.setUnAssociRetLocId(cityExperience.getUnAssociRetLocId());

			// Setting city,state and zipcode.

			strResponse = hubCitiService.deAssociateRetailer(objCityExperience);
			if (null != strResponse && "SUCCESS".equals(strResponse)) {
				strResponse = ApplicationConstants.SETUPRETAILERDEASSOCIATETEXT;
			} else {
				strResponse = ApplicationConstants.SETUPRETAILERDEASSOCIATEFAILURETEXT;
			}

			Log.info(ApplicationConstants.METHODEND + strMethodName);
			model.put("SetupRetLocationForm", cityExperience);
		} catch (HubCitiServiceException exception) {
			Log.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}
		return new ModelAndView(new RedirectView("/HubCiti/getassociretlocs.htm?deassociresp=" + strResponse));
	}

	@RequestMapping(value = "/associateret.htm", method = RequestMethod.POST)
	public ModelAndView AssociateRetailer(@ModelAttribute("SetupRetLocationForm") CityExperience cityExperience, BindingResult result,
			ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws HubCitiServiceException {
		String strMethodName = "AssociateRetailer";
		Log.info(ApplicationConstants.METHODSTART + strMethodName);
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		String strResponse = null;
		int iHubCitiId = 0;

		CityExperience objCityExperience = new CityExperience();
		try {
			User user = (User) session.getAttribute("loginUser");

			if (null != user) {
				iHubCitiId = user.getHubCitiID();
			}

			objCityExperience.setHubCitiId(iHubCitiId);
			objCityExperience.setUnAssociRetLocId(cityExperience.getUnAssociRetLocId());

			strResponse = hubCitiService.associateRetailer(objCityExperience);

			if (null != strResponse && "SUCCESS".equals(strResponse)) {
				strResponse = ApplicationConstants.SETUPRETAILERASSOCIATETEXT;
			} else {
				strResponse = ApplicationConstants.SETUPRETAILERASSOCIATEERRORTEXT;
			}
			Log.info(ApplicationConstants.METHODEND + strMethodName);

			model.put("SetupRetLocationForm", cityExperience);

		} catch (HubCitiServiceException exception) {
			Log.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}
		return new ModelAndView(new RedirectView("/HubCiti/getunassociretlocs.htm?unassociresp=" + strResponse));
	}

}
