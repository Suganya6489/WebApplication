/**
 * 
 */
package com.hubciti.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.exception.HubCitiServiceException;
import com.hubciti.common.pojo.FAQ;
import com.hubciti.common.pojo.FAQDetails;
import com.hubciti.common.pojo.User;
import com.hubciti.common.tags.Pagination;
import com.hubciti.common.util.Utility;
import com.hubciti.service.HubCitiService;
import com.hubciti.validator.FAQValidator;

/**
 * This controller will manage FAQ's And FAQ's categories.
 * 
 * @author sangeetha.ts
 */
@Controller
public class FAQController {
	/**
	 * Getting the logger Instance.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FAQController.class);

	/**
	 * variable of type FAQValidator.
	 */
	FAQValidator faqValidator;

	/**
	 * Setter for faqValidator.
	 * 
	 * @param FAQValidator
	 *            the faqValidator to set
	 */
	@Autowired
	public void setFaqValidator(FAQValidator faqValidator) {
		this.faqValidator = faqValidator;
	}

	@RequestMapping(value = "/displayfaq.htm", method = RequestMethod.GET)
	public String displayFAQ(@ModelAttribute("faqs") FAQ faq, BindingResult result, HttpServletRequest request, HttpSession session, ModelMap model)
			throws HubCitiServiceException {
		final String methodName = "displayFAQ";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.SETUPFAQS);

		final User loginUser = (User) session.getAttribute("loginUser");
		final String pageFlag = request.getParameter("pageFlag");
		Integer lowerLimit = faq.getFaqLowerLimit();
		if (null == lowerLimit || "".equals(lowerLimit)) {
			lowerLimit = 0;
		}
		String pageNumber = "0";
		int currentPage = 1;
		final int recordCount = 20;
		List<FAQDetails> details = null;

		try {
			session.removeAttribute("pageNum");
			final ServletContext servletContext = request.getSession().getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

			if (null != pageFlag && "true".equals(pageFlag)) {
				pageNumber = request.getParameter("pageNumber");
				session.setAttribute("pageNum", pageNumber);
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

			faq.setFaqLowerLimit(lowerLimit);
			faq.setHubCitiId(loginUser.getHubCitiID());
			
			String strCategoryIds = null;
			if (!Utility.isEmptyOrNullString(faq.getFaqCatIds())) {
				strCategoryIds = faq.getFaqCatIds();
				strCategoryIds = strCategoryIds.replaceAll(",", "!~~!");
				strCategoryIds = strCategoryIds.replaceAll(",!~~!", "");
				faq.setFaqCatIds(strCategoryIds);
			}

			String strQustIds = null;
			if (!Utility.isEmptyOrNullString(faq.getQstnIds())) {
				strQustIds = faq.getQstnIds();
				strQustIds = strQustIds.replaceAll(",!~~!,", "!~~!");
				strQustIds = strQustIds.replaceAll(",!~~!", "");
				faq.setQstnIds(strQustIds);
			}

			String strSortOdrIds = null;
			if (!Utility.isEmptyOrNullString(faq.getSortOrderIds())) {
				strSortOdrIds = faq.getSortOrderIds();
				strSortOdrIds = strSortOdrIds.replaceAll(",!~~!,", "!~~!");
				strSortOdrIds = strSortOdrIds.replaceAll(",!~~!", "");
				faq.setSortOrderIds(strSortOdrIds);
			}
			
			
			
			
			details = sortFAQs(faq, currentPage, hubCitiService, session);

			if (details == null) {
				if (null == faq.getFaqSearchKey() || "".equals(faq.getFaqSearchKey())) {
					request.setAttribute("hideSearch", "yes");
				}
				session.removeAttribute(ApplicationConstants.PAGINATION);
				request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "No FAQ's Found");
				request.setAttribute(ApplicationConstants.RESPONSESTATUS, "INFO");
			}

			request.setAttribute(ApplicationConstants.LOWERLIMIT, lowerLimit);
			session.setAttribute("faqDetails", details);
			model.put("faqs", faq);

		} catch (HubCitiServiceException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName + e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "displayfaq";
	}

	@RequestMapping(value = "/addfaq.htm", method = RequestMethod.GET)
	public String addFAQ(@ModelAttribute("faqs") FAQ faq, BindingResult result, HttpServletRequest request, HttpSession session, ModelMap model)
			throws HubCitiServiceException {
		final String methodName = "addFAQ";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		session.removeAttribute("faqCategories");
		final User loginUser = (User) session.getAttribute("loginUser");
		FAQDetails faqCategories = null;
		try {
			final ServletContext servletContext = request.getSession().getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

			faq.setFaqLowerLimit(null);
			faq.setFaqSearchKey(null);
			faq.setHubCitiId(loginUser.getHubCitiID());
			faqCategories = hubCitiService.fetchFAQCategories(faq);
			if (null != faqCategories) {
				session.setAttribute("faqCategories", faqCategories.getFaqs());
			}
			request.setAttribute(ApplicationConstants.LOWERLIMIT, 0);
			model.put("addfaq", new FAQ());

		} catch (HubCitiServiceException exception) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName + exception.getStackTrace());
			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "addfaq";
	}

	@RequestMapping(value = "/savefaq.htm", method = RequestMethod.POST)
	public ModelAndView saveFAQ(@ModelAttribute("addfaq") FAQ faq, BindingResult result, HttpServletRequest request, ModelMap model,
			HttpSession session) throws HubCitiServiceException {
		final String methodName = "saveFAQ";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		final User loginUser = (User) session.getAttribute("loginUser");
		List<FAQDetails> details = null;
		Integer currentPage = 1;
		Integer lowerLimit = faq.getFaqLowerLimit();
		final Integer recordCount = 20;
		try {
			final ServletContext servletContext = request.getSession().getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

			if (null != faq.getFaqID() && !"".equals(faq.getFaqID())) {
				request.setAttribute(ApplicationConstants.LOWERLIMIT, faq.getFaqLowerLimit());
			} else {
				faq.setFaqLowerLimit(0);
				request.setAttribute(ApplicationConstants.LOWERLIMIT, 0);
			}

			/* To fix XSS issue*/
			faq.setQuestion(Utility.getXssFreeString(faq.getQuestion()));
			
			faqValidator.validate(faq, result);

			if (result.hasErrors()) {
				return new ModelAndView("addfaq");
			} else {
				faq.setUserId(loginUser.gethCAdminUserID());
				faq.setHubCitiId(loginUser.getHubCitiID());
				String response = hubCitiService.saveFAQs(faq);

				if (response.equalsIgnoreCase(ApplicationConstants.DUPLICATEQUESTION)) {
					request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "Question already exists!");
					request.setAttribute(ApplicationConstants.RESPONSESTATUS, "INFO");
					return new ModelAndView("addfaq");
				} else {
					if (null != faq.getFaqID() && !"".equals(faq.getFaqID())) {
						currentPage = (lowerLimit + recordCount) / recordCount;
						request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "FAQ Updated Successfully");
						request.setAttribute(ApplicationConstants.RESPONSESTATUS, ApplicationConstants.SUCCESS);
					} else {
						request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "FAQ Created Successfully");
						request.setAttribute(ApplicationConstants.RESPONSESTATUS, ApplicationConstants.SUCCESS);
					}

					details = sortFAQs(faq, currentPage, hubCitiService, session);
					session.setAttribute("faqDetails", details);
					model.put("faqs", faq);
				}
			}

		} catch (HubCitiServiceException exception) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName + exception.getStackTrace());
			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return new ModelAndView("displayfaq");
	}

	@RequestMapping(value = "/editfaq.htm", method = RequestMethod.GET)
	public String editFAQ(@ModelAttribute("faqs") FAQ faq, BindingResult result, HttpServletRequest request, HttpSession session, ModelMap model)
			throws HubCitiServiceException {
		final String methodName = "editFAQ";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		session.removeAttribute("faqCategories");
		final User loginUser = (User) session.getAttribute("loginUser");
		FAQDetails faqCategories = null;
		FAQ faqDetails = null;
		try {
			final ServletContext servletContext = request.getSession().getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

			request.setAttribute(ApplicationConstants.LOWERLIMIT, faq.getFaqLowerLimit());

			faq.setFaqLowerLimit(null);
			faq.setHubCitiId(loginUser.getHubCitiID());
			faqCategories = hubCitiService.fetchFAQCategories(faq);
			faqDetails = hubCitiService.fetchFAQDetails(faq);
			if (null != faqCategories) {
				session.setAttribute("faqCategories", faqCategories.getFaqs());
			}
			faqDetails.setFaqSearchKey(faq.getFaqSearchKey());
			model.put("addfaq", faqDetails);

		} catch (HubCitiServiceException exception) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName + exception.getStackTrace());
			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "addfaq";
	}

	@RequestMapping(value = "/deletefaq.htm", method = RequestMethod.POST)
	public String deleteFAQ(@ModelAttribute("faqs") FAQ faq, BindingResult result, HttpServletRequest request, HttpSession session, ModelMap model)
			throws HubCitiServiceException {
		final String methodName = "deleteFAQ";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		final User loginUser = (User) session.getAttribute("loginUser");
		List<FAQDetails> details = null;
		Integer currentPage = 1;
		Integer lowerLimit = faq.getFaqLowerLimit();
		final Integer recordCount = 20;
		try {
			final ServletContext servletContext = request.getSession().getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");

			faq.setHubCitiId(loginUser.getHubCitiID());
			hubCitiService.deleteFAQ(faq);

			request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "FAQ Deleted Successfully");
			request.setAttribute(ApplicationConstants.RESPONSESTATUS, ApplicationConstants.SUCCESS);

			currentPage = (lowerLimit + recordCount) / recordCount;
			details = sortFAQs(faq, currentPage, hubCitiService, session);
			if (null == details && lowerLimit > 0) {
				lowerLimit = lowerLimit - recordCount;
				currentPage = (lowerLimit + recordCount) / recordCount;
				faq.setFaqLowerLimit(lowerLimit);
				details = sortFAQs(faq, currentPage, hubCitiService, session);
			}
			if (null == details && (null == faq.getFaqSearchKey() || "".equals(faq.getFaqSearchKey()))) {
				request.setAttribute("hideSearch", "yes");
			}
			request.setAttribute(ApplicationConstants.LOWERLIMIT, lowerLimit);
			session.setAttribute("faqDetails", details);
			model.put("faqs", faq);

		} catch (HubCitiServiceException exception) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName + exception.getStackTrace());
			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "displayfaq";
	}

	public List<FAQDetails> sortFAQs(FAQ faq, Integer currentPage, HubCitiService hubCitiService, HttpSession session) throws HubCitiServiceException {
		final String methodName = "editFAQ";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		FAQDetails responseFAQDetails = null;
		FAQDetails faqDetails = null;
		List<FAQDetails> details = null;
		List<FAQ> faqs = null;
		List<FAQ> faqsList = null;
		String categoryName = null;
		Integer categoryId = null;
		Pagination objPage = null;
		final int recordCount = 20;
		try {
			responseFAQDetails = hubCitiService.fetchFQAs(faq);

			if (null != responseFAQDetails.getFaqs() && !responseFAQDetails.getFaqs().isEmpty()) {
				details = new ArrayList<FAQDetails>();
				faqs = responseFAQDetails.getFaqs();
				faqDetails = new FAQDetails();
				categoryName = faqs.get(0).getFaqCatName();
				faqDetails.setCategoryName(categoryName);
				categoryId = faqs.get(0).getFaqCatId();
				faqDetails.setFaqcateId(categoryId);
				faqsList = new ArrayList<FAQ>();
				faqDetails.setFaqs(faqsList);
				for (FAQ faq2 : faqs) {

					if (categoryName.equalsIgnoreCase(faq2.getFaqCatName())) {
						/*
						 * if(faqs.size() == 1){
						 * faq2.setSortOrder(faq2.getRowNum()); }
						 */
						faqsList.add(faq2);
					} else {
						details.add(faqDetails);
						faqDetails = new FAQDetails();
						categoryName = faq2.getFaqCatName();
						faqDetails.setCategoryName(categoryName);
						categoryId = faq2.getFaqCatId();
						faqDetails.setFaqcateId(categoryId);
						faqsList = new ArrayList<FAQ>();
						faqsList.add(faq2);
						faqDetails.setFaqs(faqsList);
					}
				}
				details.add(faqDetails);

				if (null != responseFAQDetails.getTotalSize()) {
					objPage = Utility.getPagination(responseFAQDetails.getTotalSize(), currentPage, "displayfaq.htm", recordCount);
					session.setAttribute(ApplicationConstants.PAGINATION, objPage);
				} else {
					session.removeAttribute(ApplicationConstants.PAGINATION);
				}
			}

		} catch (HubCitiServiceException exception) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName + exception.getStackTrace());
			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return details;
	}

	@RequestMapping(value = "/displayfaqcat.htm", method = RequestMethod.GET)
	public String displayFAQCategory(@ModelAttribute("faqcat") FAQ faq, BindingResult result, HttpServletRequest request, HttpSession session,
			ModelMap model) throws HubCitiServiceException {
		final String methodName = "displayFAQCategory";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		session.removeAttribute("faqCategories");
		session.setAttribute(ApplicationConstants.MENUNAME, ApplicationConstants.SETUPFAQS);

		FAQDetails faqCategories = null;
		final String pageFlag = request.getParameter("pageFlag");
		Integer lowerLimit = faq.getFaqLowerLimit();
		if (null == lowerLimit || !"".equals(lowerLimit)) {
			lowerLimit = 0;
		}
		String pageNumber = "0";
		int currentPage = 1;
		Pagination objPage = null;
		final int recordCount = 20;

		try {
			session.removeAttribute("pageNum");
			final ServletContext servletContext = request.getSession().getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
			User user = (User) session.getAttribute("loginUser");

			if (null != pageFlag && "true".equals(pageFlag)) {
				pageNumber = request.getParameter("pageNumber");
				session.setAttribute("pageNum", pageNumber);
				final Pagination pageSess = (Pagination) session.getAttribute("pagination");
				if (Integer.valueOf(pageNumber) != 0) {

					currentPage = Integer.valueOf(pageNumber);
					final int number = Integer.valueOf(currentPage) - 1;
					final int pageSize = pageSess.getPageRange();
					lowerLimit = pageSize * Integer.valueOf(number);
				}
			}

			faq.setHubCitiId(user.getHubCitiID());
			faq.setFaqLowerLimit(lowerLimit);
			faqCategories = hubCitiService.fetchFAQCategories(faq);

			if (null != faqCategories) {
				List<FAQ> lstFaqs = faqCategories.getFaqs();
				List<FAQ> finalLstFagCats = new ArrayList<FAQ>();
				if (null != lstFaqs && !lstFaqs.isEmpty()) {
					for (FAQ faq2 : lstFaqs) {

						if (null == faq2.getSortOrder()) {
							faq2.setSortOrder(faq.getRowNum());
						}
						finalLstFagCats.add(faq2);
					}

				}
				faqCategories.setFaqs(finalLstFagCats);

				if (null != faqCategories.getTotalSize()) {
					objPage = Utility.getPagination(faqCategories.getTotalSize(), currentPage, "displayfaqcat.htm", recordCount);
					session.setAttribute(ApplicationConstants.PAGINATION, objPage);
				} else {
					session.removeAttribute(ApplicationConstants.PAGINATION);
				}
			} else {
				if (null == faq.getFaqCatName() || "".equals(faq.getFaqCatName())) {
					request.setAttribute("hideSearch", "yes");
				}
				session.removeAttribute(ApplicationConstants.PAGINATION);
			}

			if (null != faqCategories && null != faqCategories.getFaqs()) {
				session.setAttribute("faqCategories", faqCategories.getFaqs());
			} else {
				session.removeAttribute("faqCategories");
			}

			model.put("faqcat", faq);

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return "displayfaqcat";
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
	@RequestMapping(value = "/addupdatefaqcat.htm", method = RequestMethod.GET)
	public @ResponseBody
	String addUpdateFAQCategory(@RequestParam(value = "catName", required = true) String catName,
			@RequestParam(value = "cateId", required = true) Integer catId, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws HubCitiServiceException {
		String strResponse = null;
		String strMethodName = "addUpdateFAQCategory";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		FAQ faq = null;

		try {
			final ServletContext servletContext = request.getSession().getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
			User user = (User) session.getAttribute("loginUser");

			catName = Utility.getXssFreeString(catName);
			if(null == catName || "".equals(catName))	{
				return "CategoryExists";
			}
			
			faq = new FAQ();
			faq.setFaqCatId(catId);
			faq.setFaqCatName(catName);
			faq.setUserId(user.gethCAdminUserID());
			faq.setHubCitiId(user.getHubCitiID());
			strResponse = hubCitiService.addUpdateFAQCategory(faq);

			if (null == catId && null != strResponse && !strResponse.equals(ApplicationConstants.ALERTCATEXISTS)) {
				@SuppressWarnings("unchecked")
				List<FAQ> faqCategories = (ArrayList<FAQ>) session.getAttribute("faqCategories");

				FAQ faq2 = new FAQ();

				faq2.setFaqCatId(Integer.parseInt(strResponse));
				faq2.setFaqCatName(catName);
				if (null != faqCategories) {
					faqCategories.add(faq2);
				} else {
					faqCategories = new ArrayList<FAQ>();
					faqCategories.add(faq2);
				}
				session.setAttribute("faqCategories", faqCategories);
			}

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);

		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strResponse;
	}

	@RequestMapping(value = "/deletefaqcat.htm", method = RequestMethod.GET)
	public @ResponseBody
	String deleteFAQCat(@RequestParam(value = "cateId", required = true) Integer catId, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws HubCitiServiceException {
		final String strMethodName = "deleteFAQCat";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		FAQ faq = null;
		String strResponse = null;

		try {
			final ServletContext servletContext = request.getSession().getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
			User user = (User) session.getAttribute("loginUser");

			faq = new FAQ();
			faq.setFaqCatId(catId);
			faq.setHubCitiId(user.getHubCitiID());
			strResponse = hubCitiService.deleteFAQCategory(faq);
		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strResponse;
	}

	/**
	 * This method is used to save faq category sort order.
	 * 
	 * @param faq
	 * @param result
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/savefaqcatereorderlst.htm", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView saveFaqCateReorder(@ModelAttribute("faqcat") FAQ faq, BindingResult result, ModelMap model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws HubCitiServiceException {
		String strViewName = "displayfaqcat";
		String strMethodName = "saveFaqCateReorder";
		String strResponse = "";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		try {
			final ServletContext servletContext = request.getSession().getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
			User user = (User) session.getAttribute("loginUser");
			FAQDetails faqCategories = null;
			Pagination objPage = null;
			int currentPage = 1;
			int recordCount = 20;
			String pageFlag = null;
			String pageNumber = "0";
			Integer lowerLimit = null;
			session.removeAttribute("pageNum");
			session.removeAttribute("faqCategories");
			if (null != user) {
				faq.setHubCitiId(user.getHubCitiID());
			}
			if (null == faq.getFaqLowerLimit() || !"".equals(faq.getFaqLowerLimit())) {
				lowerLimit = 0;
			}

			pageFlag = request.getParameter("pageFlag");

			if (null != pageFlag && pageFlag.equals("true")) {
				pageNumber = request.getParameter("pageNumber");
				session.setAttribute("pageNum", pageNumber);
				Pagination pageSession = (Pagination) session.getAttribute(ApplicationConstants.PAGINATION);
				if (Integer.valueOf(pageNumber) != 0) {
					currentPage = Integer.valueOf(pageNumber);
					final int number = currentPage - 1;
					final int pageSize = pageSession.getPageRange();
					lowerLimit = pageSize * number;

				}

			}
			if (!Utility.isEmptyOrNullString(faq.getFaqCatIds())) {
				if (faq.getFaqCatIds().length() == 1) {

					faq.setSortOrderIds(faq.getRowNum().toString());

				}
				strResponse = hubCitiService.saveFaqCateReorder(faq);
				if (!Utility.isEmptyOrNullString(strResponse)) {
					if (strResponse.equals(ApplicationConstants.SUCCESS)) {
						request.setAttribute(ApplicationConstants.RESPONSESTATUS, ApplicationConstants.SUCCESS);
						request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "Category sort order is saved successfully.");
					} else {
						request.setAttribute(ApplicationConstants.RESPONSESTATUS, ApplicationConstants.FAILURE);
						request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "Error occured while saving sort order.");
					}
				}
			}
			faq.setFaqLowerLimit(lowerLimit);
			faqCategories = hubCitiService.fetchFAQCategories(faq);

			if (null != faqCategories) {
				if (null != faqCategories.getTotalSize()) {
					objPage = Utility.getPagination(faqCategories.getTotalSize(), currentPage, "savefaqcatereorderlst.htm", recordCount);
					session.setAttribute(ApplicationConstants.PAGINATION, objPage);
				} else {
					session.removeAttribute(ApplicationConstants.PAGINATION);
				}
			} else {
				if (null == faq.getFaqCatName() || "".equals(faq.getFaqCatName())) {
					request.setAttribute("hideSearch", "yes");
				}
				session.removeAttribute(ApplicationConstants.PAGINATION);
			}

			if (null != faqCategories && null != faqCategories.getFaqs()) {
				session.setAttribute("faqCategories", faqCategories.getFaqs());
			} else {
				session.removeAttribute("faqCategories");
			}

			model.put("faqcat", faq);

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return new ModelAndView(strViewName);

	}

	/**
	 * This method is used to save faq sort order.
	 * 
	 * @param faq
	 * @param result
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HubCitiServiceException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/savefaqreorderlist.htm", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView saveFaqReorder(@ModelAttribute("faqcat") FAQ faq, BindingResult result, ModelMap model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws HubCitiServiceException {
		String strViewName = "displayfaq";
		String strMethodName = "saveFaqReorder";
		String strResponse = "";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		try {
			session.removeAttribute("faqCategories");
			session.removeAttribute("pageNum");
			final ServletContext servletContext = request.getSession().getServletContext();
			final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			final HubCitiService hubCitiService = (HubCitiService) appContext.getBean("hubCitiService");
			User user = (User) session.getAttribute("loginUser");
			FAQDetails faqCategories = null;
			Pagination objPage = null;
			int currentPage = 1;
			int recordCount = 20;
			String pageFlag = null;
			String pageNumber = "0";
			Integer lowerLimit = null;
			if (null != user) {
				faq.setHubCitiId(user.getHubCitiID());
			}
			if (null == faq.getFaqLowerLimit() || !"".equals(faq.getFaqLowerLimit())) {
				lowerLimit = 0;
			}
			final User loginUser = (User) session.getAttribute("loginUser");
			List<FAQDetails> details = null;

			pageFlag = request.getParameter("pageFlag");
			if (null != pageFlag && pageFlag.equals("true")) {
				pageNumber = request.getParameter("pageNumber");
				session.setAttribute("pageNum", pageNumber);
				Pagination pageSession = (Pagination) session.getAttribute(ApplicationConstants.PAGINATION);
				if (Integer.valueOf(pageNumber) != 0) {
					currentPage = Integer.valueOf(pageNumber);
					final int number = currentPage - 1;
					final int pageSize = pageSession.getPageRange();
					lowerLimit = pageSize * number;
				}
			}

			String strCategoryIds = null;
			if (!Utility.isEmptyOrNullString(faq.getFaqCatIds())) {
				strCategoryIds = faq.getFaqCatIds();
				strCategoryIds = strCategoryIds.replaceAll(",", "!~~!");
				strCategoryIds = strCategoryIds.replaceAll(",!~~!", "");
				faq.setFaqCatIds(strCategoryIds);
			}

			String strQustIds = null;
			if (!Utility.isEmptyOrNullString(faq.getQstnIds())) {
				strQustIds = faq.getQstnIds();
				strQustIds = strQustIds.replaceAll(",!~~!,", "!~~!");
				strQustIds = strQustIds.replaceAll(",!~~!", "");
				faq.setQstnIds(strQustIds);
			}

			String strSortOdrIds = null;
			if (!Utility.isEmptyOrNullString(faq.getSortOrderIds())) {
				strSortOdrIds = faq.getSortOrderIds();
				strSortOdrIds = strSortOdrIds.replaceAll(",!~~!,", "!~~!");
				strSortOdrIds = strSortOdrIds.replaceAll(",!~~!", "");
				faq.setSortOrderIds(strSortOdrIds);
			}

			if (!Utility.isEmptyOrNullString(faq.getFaqCatIds())) {

				strResponse = hubCitiService.saveFaqReorder(faq);

				if (!Utility.isEmptyOrNullString(strResponse)) {
					if (strResponse.equals(ApplicationConstants.SUCCESS)) {
						request.setAttribute(ApplicationConstants.RESPONSESTATUS, ApplicationConstants.SUCCESS);
						request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "Question sort order saved successfully.");
					} else {
						request.setAttribute(ApplicationConstants.RESPONSESTATUS, ApplicationConstants.FAILURE);
						request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "Error occured while saving sort order.");
					}
				}
			}
			faq.setFaqLowerLimit(lowerLimit);
			faq.setHubCitiId(loginUser.getHubCitiID());
			details = sortFAQs(faq, currentPage, hubCitiService, session);

			if (details == null) {
				if (null == faq.getFaqSearchKey() || "".equals(faq.getFaqSearchKey())) {
					request.setAttribute("hideSearch", "yes");
				}
				session.removeAttribute(ApplicationConstants.PAGINATION);
				request.setAttribute(ApplicationConstants.RESPONSEMESSAGE, "No FAQ's Found");
				request.setAttribute(ApplicationConstants.RESPONSESTATUS, "INFO");
			}

			request.setAttribute(ApplicationConstants.LOWERLIMIT, lowerLimit);
			session.setAttribute("faqDetails", details);
			model.put("faqs", faq);

		} catch (HubCitiServiceException exception) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiServiceException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return new ModelAndView(strViewName);
	}
}