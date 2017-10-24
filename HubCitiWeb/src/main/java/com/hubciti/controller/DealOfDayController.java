/**
 * 
 */
package com.hubciti.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.exception.HubCitiServiceException;
import com.hubciti.common.pojo.DealDetails;
import com.hubciti.common.pojo.Deals;
import com.hubciti.common.pojo.User;
import com.hubciti.common.tags.Pagination;
import com.hubciti.common.util.Utility;
import com.hubciti.service.HubCitiService;

/**
 * @author sangeetha.ts
 *
 */
@Controller
public class DealOfDayController {
	/**
	 * Getting the logger Instance.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DealOfDayController.class);
	
	@RequestMapping(value = "/displaydeals.htm", method = RequestMethod.GET)
	public String displayDealOfDay(@ModelAttribute("deals") Deals deals,BindingResult result, HttpServletRequest request, HttpServletResponse response, 
			HttpSession session, ModelMap model) throws HubCitiServiceException {
		String strMethodName = "displayDealOfDay";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.SETUPDEALS);
		String strViewName = "displayCoupons";
		DealDetails dealDetails = null;
		String dealName = null;
		final String pageFlag = request.getParameter("pageFlag");
		Integer lowerLimit = 0;
		String pageNumber = "0";
		int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;
		try {
			session.removeAttribute("alertcatlst");
			final ServletContext servletContext = request.getSession().getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
			
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
			}
			
			deals.setLowerLimit(lowerLimit);
			dealDetails = hubCitiService.fetchDeals(deals, user);
			deals.setDealId(dealDetails.getDealId());
			dealName = dealDetails.getDealName();
			deals.setDealName(dealName);
			if("Hotdeals".equalsIgnoreCase(dealName)) {
				strViewName = "displayDeals";
			} else if("Coupons".equalsIgnoreCase(dealName)) {
				strViewName = "displayCoupons";
			} else if("SpecialOffers".equalsIgnoreCase(dealName)) {
				strViewName = "displaySpecials";
			}
			
			if(null != dealDetails && null != dealDetails.getDeals()  && !dealDetails.getDeals().isEmpty()) {
				objPage = Utility.getPagination(dealDetails.getTotalSize(), currentPage, "displaydeals.htm", recordCount);
				session.setAttribute(ApplicationConstants.PAGINATION, objPage);		
				session.setAttribute("dealList", dealDetails.getDeals());
			} else {
				if("Hotdeals".equalsIgnoreCase(dealName)) {
					request.setAttribute("errMsg", "No HotDeals Found");
				} else if("Coupons".equalsIgnoreCase(dealName)) {
					request.setAttribute("errMsg", "No Coupons Found");
				} else if("SpecialOffers".equalsIgnoreCase(dealName)) {
					request.setAttribute("errMsg", "No Special Offers Found");
				}				
				session.setAttribute(ApplicationConstants.PAGINATION, objPage);
				session.setAttribute("dealList", null);
			}
			
			model.put("deals", deals);			
		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}
		
		LOG.info(ApplicationConstants.METHODEND + strMethodName);
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
	@RequestMapping(value = "/saveDOD", method = RequestMethod.GET)
	public @ResponseBody
	final String saveDealOfTheDay(@RequestParam(value = "dealName", required = true) String dealName, @RequestParam(value = "dealId", required = true) Integer dealId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws HubCitiServiceException {
		String strMethodName = "saveDealOfTheDay";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		
		String responseStr = null;
		try {
			final ServletContext servletContext = request.getSession().getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
			
			User user = (User) session.getAttribute("loginUser");

			Deals deal = new Deals();
			deal.setDealName(dealName);
			deal.setDealId(dealId);			
			responseStr = hubCitiService.saveDealOfTheDay(deal, user);
			
		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}
		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return responseStr;
	}

}
