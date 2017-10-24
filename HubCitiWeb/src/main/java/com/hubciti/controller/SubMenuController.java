package com.hubciti.controller;

import java.util.List;

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

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.exception.HubCitiServiceException;
import com.hubciti.common.pojo.MenuDetails;
import com.hubciti.common.pojo.ScreenSettings;
import com.hubciti.common.pojo.SubMenuDetails;
import com.hubciti.common.pojo.User;
import com.hubciti.common.tags.Pagination;
import com.hubciti.common.util.Utility;
import com.hubciti.service.HubCitiService;

@Controller
public class SubMenuController {

	/**
	 * Getting the Logger instance.
	 */
	private static final Logger LOG = Logger.getLogger(SubMenuController.class);

	/**
	 * this method is used to display submenu and search submen.
	 * 
	 * @param screenSettings
	 * @param result
	 * @param request
	 * @param model
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */

	@RequestMapping(value = "/displaysubmenu.htm", method = RequestMethod.GET)
	public String displaySubMenus(@ModelAttribute("SubMenuForm") ScreenSettings screenSettings, BindingResult result, HttpServletRequest request,
			ModelMap model, HttpServletResponse response, HttpSession session) throws HubCitiServiceException

	{
		String strMethodName = "displaySubMenus";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		String strViewName = "displaysubmenu";
		SubMenuDetails objSubMenuDetails = null;
		List<MenuDetails> subMenuList = null;

		// For pagination.
		String pageFlag = request.getParameter("pageFlag");
		String pageNumber = "0";
		Integer iCurrentPage = 1;
		Integer iLowerLimit = 0;
		Integer iRecordCount = 20;
		Pagination pagination = null;

		try {

			session.setAttribute("menuName", ApplicationConstants.SETUPSUBMENU);
			session.removeAttribute("subMenuInfoLst");
			session.removeAttribute("SubMenuSearchKey");
			final ServletContext servletContext = request.getSession().getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

			if (null != screenSettings.getSearchKey() && !"".equals(screenSettings.getSearchKey())) {

				session.setAttribute("SubMenuSearchKey", screenSettings.getSearchKey());
				request.setAttribute("SubMSearchKey", "SubMSearchKey");

			} else {
				screenSettings.setSearchKey(null);
				session.setAttribute("SubMenuSearchKey", screenSettings.getSearchKey());
			}

			if (null != pageFlag && "true".equals(pageFlag)) {
				pageNumber = request.getParameter("pageNumber");
				final Pagination paginationSession = (Pagination) session.getAttribute("pagination");
				if (null != pageNumber && Integer.valueOf(pageNumber) != 0) {
					iCurrentPage = Integer.parseInt(pageNumber);
					final int number = iCurrentPage - 1;
					final int pageRange = paginationSession.getPageRange();
					iLowerLimit = number * pageRange;

				}

			} else {

				iCurrentPage = (iLowerLimit + iRecordCount) / iRecordCount;
			}

			screenSettings.setLowerLimit(iLowerLimit);

			User user = (User) session.getAttribute("loginUser");
			if (null == screenSettings.getLowerLimit()) {
				screenSettings.setLowerLimit(0);
			}

			objSubMenuDetails = hubCitiService.displaySubMenu(user, screenSettings);

			if (null != objSubMenuDetails) {
				if (null != objSubMenuDetails.getTotalSize()) {
					pagination = Utility.getPagination(objSubMenuDetails.getTotalSize(), iCurrentPage, "displaysubmenu.htm", iRecordCount);
					session.setAttribute("pagination", pagination);

				} else {
					session.removeAttribute("pagination");
				}
				session.setAttribute("subMenuInfoLst", objSubMenuDetails.getSubMenuList());

			}
			model.put("SubMenuForm", screenSettings);

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		return strViewName;
	}

	/**
	 * This method is used to delete submenu.
	 * 
	 * @param submenuId
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */

	@RequestMapping(value = "/delsubmenu.htm", method = RequestMethod.GET)
	public @ResponseBody
	String deleteSubMenu(@RequestParam(value = "submenuid", required = true) String submenuId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws HubCitiServiceException {
		String strMethodName = "deleteSubMenu";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		String strResponse = null;
		Integer iHubCityId = null;

		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
		try {
			ScreenSettings objScreenSettings = new ScreenSettings();

			User user = (User) session.getAttribute("loginUser");
			if (null != user && !"".equals(user)) {
				iHubCityId = user.getHubCitiID();

			}
			if (null != submenuId && !"".equals(submenuId)) {
				objScreenSettings.setMenuId(submenuId);
			}

			strResponse = hubCitiService.deleteSubMenu(objScreenSettings, iHubCityId);

			if (strResponse.equalsIgnoreCase(ApplicationConstants.SUCCESS)) {
				request.setAttribute("responseMsg", "SubMenu Deleted Successfully");
				request.setAttribute("responseStatus", ApplicationConstants.SUCCESS);
			} else {
				request.setAttribute("ErrResponseMsg", strResponse);
				request.setAttribute("responseStatus", strResponse);
			}
		} catch (HubCitiServiceException exception) {
			throw new HubCitiServiceException(exception);
		}
		LOG.info(ApplicationConstants.METHODEND + strMethodName);

		return strResponse;
	}

}
