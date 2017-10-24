package com.hubciti.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.exception.HubCitiServiceException;
import com.hubciti.common.exception.HubCitiWebSqlException;
import com.hubciti.common.pojo.AlertCategory;
import com.hubciti.common.pojo.Alerts;
import com.hubciti.common.pojo.AlertsDetails;
import com.hubciti.common.pojo.AnythingPages;
import com.hubciti.common.pojo.AppConfiguration;
import com.hubciti.common.pojo.AppSiteDetails;
import com.hubciti.common.pojo.ButtonDetails;
import com.hubciti.common.pojo.Category;
import com.hubciti.common.pojo.CityExperience;
import com.hubciti.common.pojo.CityExperienceDetail;
import com.hubciti.common.pojo.DealDetails;
import com.hubciti.common.pojo.Deals;
import com.hubciti.common.pojo.Department;
import com.hubciti.common.pojo.EncryptDecryptPwd;
import com.hubciti.common.pojo.Event;
import com.hubciti.common.pojo.EventDetail;
import com.hubciti.common.pojo.FAQ;
import com.hubciti.common.pojo.FAQDetails;
import com.hubciti.common.pojo.Filters;
import com.hubciti.common.pojo.FiltersDetails;
import com.hubciti.common.pojo.GAddress;
import com.hubciti.common.pojo.HubCitiImages;
import com.hubciti.common.pojo.MenuDetails;
import com.hubciti.common.pojo.MenuFilterTyes;
import com.hubciti.common.pojo.Module;
import com.hubciti.common.pojo.PageStatus;
import com.hubciti.common.pojo.RetailLocation;
import com.hubciti.common.pojo.ScreenSettings;
import com.hubciti.common.pojo.SearchZipCode;
import com.hubciti.common.pojo.Severity;
import com.hubciti.common.pojo.State;
import com.hubciti.common.pojo.SubMenuDetails;
import com.hubciti.common.pojo.TabBarDetails;
import com.hubciti.common.pojo.User;
import com.hubciti.common.pojo.UserDetails;
import com.hubciti.common.util.Utility;
import com.hubciti.dao.HubCitiDAO;

/**
 * This class is a service implementation class for hubciti services.
 * 
 * @author dileepa_cc
 */
public class HubCitiServiceImpl implements HubCitiService {

	/**
	 * Getting the logger Instance.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(HubCitiServiceImpl.class);

	/**
	 * Variable of type HubCitiDAO.
	 */
	private HubCitiDAO hubCitiDAO;

	/**
	 * @param hubCitiDAO
	 */
	public void setHubCitiDAO(HubCitiDAO hubCitiDAO) {
		this.hubCitiDAO = hubCitiDAO;
	}

	final String ITEMDELEMETER = "!~~!";

	/**
	 * This will return login screen details.
	 * 
	 * @param loginUser
	 * @return
	 * @throws HubCitiServiceException
	 */
	public ScreenSettings fetchScreenSettings(User loginUser)
			throws HubCitiServiceException {
		final String methodName = "fetchLoginScreenSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		ScreenSettings loginScreenDetails = null;
		try {
			loginScreenDetails = hubCitiDAO.fetchScreenSettings(loginUser);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return loginScreenDetails;
	}

	/**
	 * This will save login screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String saveLoginScreenSettings(ScreenSettings loginScreenDetails, User user) throws HubCitiServiceException {
		final String methodName = "saveLoginScreenSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		final String daoStatus;
		final String fileSeparator = System.getProperty("file.separator");
		try {
			final StringBuilder mediaPathBuilder = Utility.getMediaPath( ApplicationConstants.HUBCITI, user.getHubCitiID());
			final StringBuilder mediaTempPathBuilder = Utility .getTempMediaPath(ApplicationConstants.TEMP);
			final String hubCitiMediaPath = mediaPathBuilder.toString();
			final String tempMediaPath = mediaTempPathBuilder.toString();

			if (null != loginScreenDetails.getLogoImageName() && !"".equals(loginScreenDetails.getLogoImageName()) && null != loginScreenDetails.getOldImageName()
					&& !loginScreenDetails.getOldImageName().equals(loginScreenDetails.getLogoImageName())) {
				final InputStream inputStream = new BufferedInputStream(new FileInputStream(tempMediaPath + fileSeparator + loginScreenDetails.getLogoImageName()));
				if (null != inputStream) {
					Utility.writeFileData(inputStream, hubCitiMediaPath + fileSeparator + loginScreenDetails.getLogoImageName());
				}
			}

			daoStatus = hubCitiDAO.insertLoginScreenSettings(loginScreenDetails, user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		} catch (FileNotFoundException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}
		return daoStatus;
	}

	/**
	 * This will save registration screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String saveRegScreenSettings(ScreenSettings loginScreenDetails,
			User user) throws HubCitiServiceException {
		final String methodName = "saveRegScreenSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		final String daoStatus;
		try {
			daoStatus = hubCitiDAO.insertRegScreenSettings(loginScreenDetails,
					user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		return daoStatus;
	}

	/**
	 * This will save About us screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String saveAboutusScreenSettings(ScreenSettings screenSettings,
			User user) throws HubCitiServiceException {
		final String methodName = "saveRegScreenSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String daoStatus = null;
		String aboutUsHtml = null;
		String hubCitiHTMLMediaPath = null;
		String fileSeparator = null;
		try {

			StringBuilder mediaPathBuilder = Utility.getMediaPath(
					ApplicationConstants.HUBCITI, user.getHubCitiID());
			final StringBuilder mediaTempPathBuilder = Utility
					.getTempMediaPath(ApplicationConstants.TEMP);
			final String hubCitiMediaPath = mediaPathBuilder.toString();
			final String tempMediaPath = mediaTempPathBuilder.toString();
			fileSeparator = System.getProperty("file.separator");
			if (null != screenSettings.getLogoImageName()
					&& !"".equals(screenSettings.getLogoImageName())
					&& null != screenSettings.getOldImageName()
					&& !screenSettings.getOldImageName().equals(
							screenSettings.getLogoImageName())) {
				final InputStream inputStream = new BufferedInputStream(
						new FileInputStream(tempMediaPath + fileSeparator
								+ screenSettings.getLogoImageName()));
				if (null != inputStream) {
					Utility.writeFileData(inputStream, hubCitiMediaPath
							+ fileSeparator + screenSettings.getLogoImageName());

				}
			}

			daoStatus = hubCitiDAO.insertAboutusScreenSettings(screenSettings,
					user);

			if (null != daoStatus
					&& daoStatus.equals(ApplicationConstants.SUCCESS)) {

				mediaPathBuilder = Utility
						.getHTMLMediaPath(ApplicationConstants.HTML,
								ApplicationConstants.HUBCITI);

				mediaPathBuilder.append(fileSeparator);
				mediaPathBuilder.append(user.getHubCitiID());

				final File obj = new File(mediaPathBuilder.toString());
				if (!obj.exists()) {
					obj.mkdir();
				}

				mediaPathBuilder.append(fileSeparator);
				mediaPathBuilder
						.append(ApplicationConstants.ABOUTUSHTMLFILENAME);
				hubCitiHTMLMediaPath = mediaPathBuilder.toString();

				aboutUsHtml = Utility.generateAboutUsHtml(
						"/" + ApplicationConstants.IMAGES + "/"
								+ ApplicationConstants.HUBCITI + "/"
								+ user.getHubCitiID() + "/"
								+ screenSettings.getLogoImageName(),
						screenSettings.getPageContent(),
						screenSettings.getHubCitiVersion());
				Utility.generateHTMLFile(aboutUsHtml, hubCitiHTMLMediaPath);

			}
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		} catch (FileNotFoundException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		return daoStatus;
	}

	/**
	 * This will save Privacy Policy screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String savePrivacyPolicyScreenSettings(
			ScreenSettings screenSettings, User user)
			throws HubCitiServiceException {
		final String methodName = "savePrivacyPolicyScreenSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		final String daoStatus;
		String privacyPolicyHtml = null;
		String hubCitiHTMLMediaPath = null;
		String fileSeparator = null;
		try {
			daoStatus = hubCitiDAO.insertPrivacyPolicyScreenSettings(
					screenSettings, user);

			if (null != daoStatus
					&& daoStatus.equals(ApplicationConstants.SUCCESS)) {

				fileSeparator = System.getProperty("file.separator");
				final StringBuilder mediaPathBuilder = Utility
						.getHTMLMediaPath(ApplicationConstants.HTML,
								ApplicationConstants.HUBCITI);

				mediaPathBuilder.append(fileSeparator);
				mediaPathBuilder.append(user.getHubCitiID());

				final File obj = new File(mediaPathBuilder.toString());
				if (!obj.exists()) {
					obj.mkdir();
				}

				mediaPathBuilder.append(fileSeparator);
				mediaPathBuilder
						.append(ApplicationConstants.PRIVICYPOLICYHTMLFILENAME);
				hubCitiHTMLMediaPath = mediaPathBuilder.toString();

				privacyPolicyHtml = Utility.generatePrivacyPolicyHtml(
						screenSettings.getPageTitle(),
						screenSettings.getPageContent(),
						screenSettings.getBgColor(),
						screenSettings.getFontColor());
				Utility.generateHTMLFile(privacyPolicyHtml,
						hubCitiHTMLMediaPath);

			}
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		return daoStatus;
	}

	/**
	 * This will save Splash Screen screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String saveSplashScreenSettings(ScreenSettings screenSettings,
			User user) throws HubCitiServiceException {
		final String methodName = "saveSplashScreenSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		final String daoStatus;
		final String fileSeparator = System.getProperty("file.separator");
		try {
			final StringBuilder mediaPathBuilder = Utility.getMediaPath(
					ApplicationConstants.HUBCITI, user.getHubCitiID());
			final StringBuilder mediaTempPathBuilder = Utility
					.getTempMediaPath(ApplicationConstants.TEMP);
			final String hubCitiMediaPath = mediaPathBuilder.toString();
			final String tempMediaPath = mediaTempPathBuilder.toString();

			if (null != screenSettings.getLogoImageName()
					&& !"".equals(screenSettings.getLogoImageName())
					&& null != screenSettings.getOldImageName()
					&& !screenSettings.getOldImageName().equals(
							screenSettings.getLogoImageName())) {
				final InputStream inputStream = new BufferedInputStream(
						new FileInputStream(tempMediaPath + fileSeparator
								+ screenSettings.getLogoImageName()));
				if (null != inputStream) {
					Utility.writeFileData(inputStream, hubCitiMediaPath
							+ fileSeparator + screenSettings.getLogoImageName());

				}
			}

			daoStatus = hubCitiDAO.insertSplashScreenSettings(screenSettings,
					user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		} catch (FileNotFoundException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}
		return daoStatus;
	}

	/**
	 * This will save the new password for the user.
	 * 
	 * @param objUser
	 * @return
	 * @throws HubCitiServiceException
	 */
	@SuppressWarnings("unused")
	public User forgotPwd(User objUser) throws HubCitiServiceException {
		final String strMethodName = "forgotPwd ds";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		User objforgotPwdUser = null;
		String strResponse = ApplicationConstants.FAILURETEXT;
		String smtpHost = null;
		String smtpPort = null;
		String strAdminEmailId = null;
		String isPwdSend = null;
		String strDomainName = null;
		String strLogoImage = null;
		String enryptPassword = null;
		String autogenPassword = null;
		final EncryptDecryptPwd enryptDecryptpwd = null;

		try {

			// For generating auto generated password.
			autogenPassword = Utility.randomString(5);
			objUser.setUserPwd(autogenPassword);
			final PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
			enryptPassword = passwordEncoder.encodePassword(autogenPassword,
					objUser.getUserName());
			objUser.setEncrptedPassword(enryptPassword);

			objforgotPwdUser = hubCitiDAO.forgotPwd(objUser);

			// for fetching getting general images.
			final List<AppConfiguration> domainNameList = hubCitiDAO
					.getAppConfigForGeneralImages(ApplicationConstants.SERVER_CONFIGURATION);
			for (int j = 0; j < domainNameList.size(); j++) {
				strDomainName = domainNameList.get(j).getScreenContent();
			}
			if (null != strDomainName && !"".equals(strDomainName)) {
				strLogoImage = strDomainName
						+ ApplicationConstants.SCANSEE_LOGO_FOR_MAILSENDING;
			}

			// for fetching email configuration properties.
			if (null != objforgotPwdUser
					&& objforgotPwdUser.getResponse().equals(
							ApplicationConstants.SUCCESS)) {
				final List<AppConfiguration> emailConf = hubCitiDAO
						.getAppConfig(ApplicationConstants.EMAILCONFIG);

				for (int j = 0; j < emailConf.size(); j++) {
					if (emailConf.get(j).getScreenName()
							.equals(ApplicationConstants.SMTPHOST)) {
						smtpHost = emailConf.get(j).getScreenContent();
					}
					if (emailConf.get(j).getScreenName()
							.equals(ApplicationConstants.SMTPPORT)) {
						smtpPort = emailConf.get(j).getScreenContent();
					}
				}
				final List<AppConfiguration> adminEmailList = hubCitiDAO
						.getAppConfig(ApplicationConstants.HUBCITIREGISTRATION);
				for (int j = 0; j < adminEmailList.size(); j++) {
					if (adminEmailList.get(j).getScreenName()
							.equals(ApplicationConstants.HUBCITIADMINEMAILID)) {
						strAdminEmailId = adminEmailList.get(j)
								.getScreenContent();
					}
				}

				isPwdSend = Utility.sendMailForgetPassword(objforgotPwdUser,
						smtpHost, smtpPort, strAdminEmailId, strLogoImage,
						autogenPassword);
				if (isPwdSend != null
						&& isPwdSend.equals(ApplicationConstants.SUCCESS)) {
					strResponse = ApplicationConstants.SUCCESS;
					objforgotPwdUser.setResponse(strResponse);
				}

			}
		} catch (HubCitiServiceException exception) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + strMethodName,
					exception);
			throw new HubCitiServiceException(exception);

		} catch (HubCitiWebSqlException exception) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + strMethodName,
					exception);
			throw new HubCitiServiceException(exception);
		}
		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return objforgotPwdUser;
	}

	/**
	 * This will save General Settings screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String saveGeneralSettings(ScreenSettings screenSettings, User user)
			throws HubCitiServiceException {
		final String methodName = "saveGeneralSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		final String daoStatus;
		final String fileSeparator = System.getProperty("file.separator");
		try {
			final StringBuilder mediaPathBuilder = Utility.getMediaPath(
					ApplicationConstants.HUBCITI, user.getHubCitiID());
			final StringBuilder mediaTempPathBuilder = Utility
					.getTempMediaPath(ApplicationConstants.TEMP);
			final String hubCitiMediaPath = mediaPathBuilder.toString();
			final String tempMediaPath = mediaTempPathBuilder.toString();

			if (null != screenSettings.getLogoImageName()
					&& !"".equals(screenSettings.getLogoImageName())
					&& null != screenSettings.getOldImageName()
					&& !screenSettings.getOldImageName().equals(
							screenSettings.getLogoImageName())) {
				final InputStream inputStream = new BufferedInputStream(
						new FileInputStream(tempMediaPath + fileSeparator
								+ screenSettings.getLogoImageName()));
				if (null != inputStream) {
					Utility.writeFileData(inputStream, hubCitiMediaPath
							+ fileSeparator + screenSettings.getLogoImageName());

				}
			}

			if (null != screenSettings.getBannerImageName()
					&& !"".equals(screenSettings.getBannerImageName())
					&& null != screenSettings.getBannerImageName()
					&& !screenSettings.getOldAppIconImage().equals(
							screenSettings.getBannerImageName())) {
				final InputStream inputStream = new BufferedInputStream(
						new FileInputStream(tempMediaPath + fileSeparator
								+ screenSettings.getBannerImageName()));
				if (null != inputStream) {
					Utility.writeFileData(
							inputStream,
							hubCitiMediaPath + fileSeparator
									+ screenSettings.getBannerImageName());

				}
			}

			daoStatus = hubCitiDAO.insertGeneralSettingsDetails(screenSettings,
					user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		} catch (FileNotFoundException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}
		return daoStatus;
	}

	/**
	 * This methos is used to get the logged in user details.
	 * 
	 * @param userName
	 * @return User
	 * @throws HubCitiServiceException
	 */
	public User getLoginAdminDetails(String userName)
			throws HubCitiServiceException {
		final String methodName = "getLoginAdminDetails";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		User user = null;

		try {
			user = hubCitiDAO.getLoginAdminDetails(userName);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return user;
	}

	/**
	 * This method returns the page details.
	 * 
	 * @param hubCitiId
	 * @return PageStatus
	 * @throws HubCitiServiceException
	 */
	public PageStatus getScreenStatus(int hubCitiId)
			throws HubCitiServiceException {
		final String methodName = "getScreenStatus";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		PageStatus status = null;

		try {
			status = hubCitiDAO.getScreenStatus(hubCitiId);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	/**
	 * This method updates the password with new password.
	 * 
	 * @param User
	 * @return String
	 * @throws HubCitiServiceException
	 */
	public String saveChangedPassword(User user) throws HubCitiServiceException {
		final String methodName = "saveChangedPassword";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;

		try {
			final PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
			user.setPassword(passwordEncoder.encodePassword(user.getPassword(),
					user.getUserName()));
			status = hubCitiDAO.saveChangedPassword(user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	public String createMenu(MenuDetails menuDetails, User user)
			throws HubCitiServiceException {
		final String methodName = "createMenu";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;

		try {
			status = hubCitiDAO.createMenu(menuDetails, user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	public List<MenuDetails> getLinkList() throws HubCitiServiceException {
		final String methodName = "getLinkList";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<MenuDetails> linkList = null;

		try {
			linkList = hubCitiDAO.getLinkList();
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return linkList;
	}

	public String saveUpdateIconicMenuTemplate(User loginuser,
			List<ScreenSettings> menuItemsList, Integer menuId,
			Integer menuLevel, String menuName, String bottmBtnId,
			String templateType, String bannerImg, boolean tyepFilter,
			boolean deptFilter) throws HubCitiServiceException

	{
		final String methodName = "saveUpdateIconicMenuTemplate";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String daoStatus = null;
		final String fileSeparator = System.getProperty("file.separator");
		InputStream inputStream = null;
		MenuDetails menuDetails = null;
		ButtonDetails buttonDetails = null;
		String[] menuItemDetails = null;
		int index = 1;
		String linkId = null;
		String subCatIds = null;
		StringBuilder mainCatId = null;
		StringBuilder findSubCatId = null;
		try {
			final StringBuilder mediaPathBuilder = Utility.getMediaPath(
					ApplicationConstants.HUBCITI, loginuser.getHubCitiID());
			final StringBuilder mediaTempPathBuilder = Utility
					.getTempMediaPath(ApplicationConstants.TEMP);
			final String hubCitiMediaPath = mediaPathBuilder.toString();
			final String tempMediaPath = mediaTempPathBuilder.toString();
			menuDetails = new MenuDetails();
			menuItemDetails = new String[10];
			buttonDetails = new ButtonDetails();

			if (null != bannerImg && !"".equals(bannerImg)) {
				inputStream = new BufferedInputStream(new FileInputStream(
						tempMediaPath + fileSeparator + bannerImg));
				if (null != inputStream) {
					Utility.writeFileData(inputStream, hubCitiMediaPath
							+ fileSeparator + bannerImg);
				}
			}
			for (ScreenSettings menuItem : menuItemsList) {
				linkId = null;
				subCatIds = null;
				if (null != menuItem.getLogoImageName()
						&& !"".equals(menuItem.getLogoImageName())) {
					inputStream = new BufferedInputStream(new FileInputStream(
							tempMediaPath + fileSeparator
									+ menuItem.getLogoImageName()));
					if (null != inputStream) {
						Utility.writeFileData(inputStream, hubCitiMediaPath
								+ fileSeparator + menuItem.getLogoImageName());
					}
				}
				if (index == 1) {
					menuItemDetails[0] = menuItem.getMenuBtnName();
					menuItemDetails[1] = menuItem.getMenuFucntionality();

					if (null != menuItem.getBtnLinkId()
							&& !"".equals(menuItem.getBtnLinkId())) {
						if (menuItem.getBtnLinkId().contains(",")) {

							if (menuItem.getBtnLinkId().contains("MC")) {
								String[] strings = menuItem.getBtnLinkId()
										.split(",");
								mainCatId = new StringBuilder();
								for (int i = 0; i < strings.length; i++) {

									if (null != strings[i]
											&& strings[i].contains("MC")) {
										mainCatId
												.append(strings[i]
														.substring(
																0,
																strings[i]
																		.lastIndexOf("-")));
										if (i != strings.length - 1) {
											mainCatId.append(",");
										}
									}
								}
								linkId = mainCatId.toString();
								linkId = linkId.replaceAll(",", "|");

							} else {

								linkId = menuItem.getBtnLinkId().replaceAll(
										",", "|");
							}

						} else {
							if (menuItem.getBtnLinkId().contains("MC")) {
								linkId = menuItem.getBtnLinkId().substring(
										0,
										menuItem.getBtnLinkId()
												.lastIndexOf("-"));

							} else {
								linkId = menuItem.getBtnLinkId();
							}

						}

					}

					if (null != menuItem.getSubCatIds()
							&& !"".equals(menuItem.getSubCatIds())) {
						if (menuItem.getSubCatIds().contains(",")) {
							String[] fstrings = menuItem.getSubCatIds().split(
									",");
							findSubCatId = new StringBuilder();
							for (int i = 0; i < fstrings.length; i++) {
								if (null != fstrings[i]
										&& !fstrings[i].equals("")
										&& !"null"
												.equalsIgnoreCase(fstrings[i])) {
									findSubCatId.append(fstrings[i]);
									findSubCatId.append(",");
								}
							}
							subCatIds = findSubCatId.toString();

							subCatIds = subCatIds.replaceAll(",", "|");

						} else {

							subCatIds = menuItem.getSubCatIds();
						}
					}

					if (!Utility.isEmptyOrNullString(subCatIds)) {
						if (subCatIds.endsWith("|")
								|| subCatIds.endsWith("!~~!")) {
							subCatIds = subCatIds.substring(0,
									subCatIds.length() - 1);
						}
					}

					if (!Utility.isEmptyOrNullString(linkId)) {
						menuItemDetails[2] = linkId;
					}

					if (null != menuItemDetails[2]
							&& !menuItemDetails[2].isEmpty()) {
						if (menuItemDetails[2].endsWith("|")) {
							menuItemDetails[2] = menuItemDetails[2].substring(
									0, menuItemDetails[2].length() - 1);
						}
					}
					menuItemDetails[3] = String.valueOf(index);
					menuItemDetails[4] = menuItem.getLogoImageName();
					if (null != subCatIds) {
						menuItemDetails[8] = subCatIds;
					} else {
						menuItemDetails[8] = null;
					}
					if (tyepFilter) {
						menuItemDetails[5] = menuItem.getBtnType();
					} else {
						menuItemDetails[5] = null;
					}
					if (deptFilter) {
						menuItemDetails[6] = menuItem.getBtnDept();
					} else {
						menuItemDetails[6] = null;
					}
					if (null != menuItem.getComboBtnTypeId()
							&& !"".equals(menuItem.getComboBtnTypeId())) {
						menuItemDetails[7] = menuItem.getComboBtnTypeId()
								.toString();
					} else {
						menuItemDetails[7] = null;
					}

					if (null != menuItem.getCitiId()
							&& !"".equalsIgnoreCase(menuItem.getCitiId())) {
						menuItemDetails[9] = menuItem.getCitiId().replaceAll(
								",", "|");
					} else {
						menuItemDetails[9] = null;
					}

				}
				// if more than 1 buttons

				else {
					menuItemDetails[0] = menuItemDetails[0] + ITEMDELEMETER
							+ menuItem.getMenuBtnName();
					menuItemDetails[1] = menuItemDetails[1] + ITEMDELEMETER
							+ menuItem.getMenuFucntionality();

					if (null != menuItem.getBtnLinkId()
							&& !"".equals(menuItem.getBtnLinkId())) {
						if (menuItem.getBtnLinkId().contains(",")) {

							if (menuItem.getBtnLinkId().contains("MC")) {
								String[] strings = menuItem.getBtnLinkId()
										.split(",");
								mainCatId = new StringBuilder();
								for (int i = 0; i < strings.length; i++) {

									if (null != strings[i]
											&& strings[i].contains("MC")) {
										mainCatId
												.append(strings[i]
														.substring(
																0,
																strings[i]
																		.lastIndexOf("-")));
										if (i != strings.length - 1) {
											mainCatId.append(",");
										}

									}
								}
								linkId = mainCatId.toString();
								linkId = linkId.replaceAll(",", "|");

							} else {

								linkId = menuItem.getBtnLinkId().replaceAll(
										",", "|");
							}

						} else {
							if (menuItem.getBtnLinkId().contains("MC")) {
								linkId = menuItem.getBtnLinkId().substring(
										0,
										menuItem.getBtnLinkId()
												.lastIndexOf("-"));

							} else {
								linkId = menuItem.getBtnLinkId();
							}

						}

					}

					if (null != menuItem.getSubCatIds()
							&& !"".equals(menuItem.getSubCatIds())) {
						if (menuItem.getSubCatIds().contains(",")) {
							String[] fstrings = menuItem.getSubCatIds().split(
									",");
							findSubCatId = new StringBuilder();
							for (int i = 0; i < fstrings.length; i++) {
								if (null != fstrings[i]
										&& !fstrings[i].equals("")
										&& !"null"
												.equalsIgnoreCase(fstrings[i])) {
									findSubCatId.append(fstrings[i]);
									findSubCatId.append(",");
								}
							}
							subCatIds = findSubCatId.toString();

							subCatIds = subCatIds.replaceAll(",", "|");

						} else {

							subCatIds = menuItem.getSubCatIds();
						}
					}
					if (!Utility.isEmptyOrNullString(subCatIds)) {

						if (subCatIds.endsWith("|")
								|| subCatIds.endsWith("!~~!")) {
							subCatIds = subCatIds.substring(0,
									subCatIds.length() - 1);
						}
					}

					if (!Utility.isEmptyOrNullString(linkId)) {
						if (linkId.endsWith("|")) {
							linkId = linkId.substring(0, linkId.length() - 1);
						}
					}

					menuItemDetails[2] = menuItemDetails[2] + ITEMDELEMETER
							+ linkId;
					menuItemDetails[3] = menuItemDetails[3] + ITEMDELEMETER
							+ String.valueOf(index);
					menuItemDetails[4] = menuItemDetails[4] + ITEMDELEMETER
							+ menuItem.getLogoImageName();
					if (null != subCatIds) {
						if (null != menuItemDetails[8]) {
							menuItemDetails[8] = menuItemDetails[8]
									+ ITEMDELEMETER + subCatIds;
						} else {
							menuItemDetails[8] = subCatIds;
						}
					}
					if (tyepFilter) {
						menuItemDetails[5] = menuItemDetails[5] + ITEMDELEMETER
								+ menuItem.getBtnType();
					}

					if (deptFilter) {
						menuItemDetails[6] = menuItemDetails[6] + ITEMDELEMETER
								+ menuItem.getBtnDept();
					}

					if (null != menuItem.getComboBtnTypeId()
							&& !"".equals(menuItem.getComboBtnTypeId())) {
						menuItemDetails[7] = menuItemDetails[7] + ITEMDELEMETER
								+ menuItem.getComboBtnTypeId().toString();
					} else {
						menuItemDetails[7] = menuItemDetails[7] + ITEMDELEMETER
								+ null;
					}
					if (null != menuItem.getCitiId()
							&& !"".equalsIgnoreCase(menuItem.getCitiId())) {
						menuItemDetails[9] = menuItemDetails[9] + ITEMDELEMETER
								+ menuItem.getCitiId().replaceAll(",", "|");
					} else {
						menuItemDetails[9] = menuItemDetails[9] + ITEMDELEMETER
								+ null;
					}
				}
				index++;
			}

			if (null != menuItemDetails[2] && !menuItemDetails[2].isEmpty()) {
				if (menuItemDetails[2].endsWith("|")) {
					menuItemDetails[2] = menuItemDetails[2].substring(0,
							menuItemDetails[2].length() - 1);
				}
			}

			menuDetails.setMenuId(menuId);
			menuDetails.setLevel(menuLevel);
			menuDetails.setMenuName(menuName);
			if ("".equals(bottmBtnId)) {
				menuDetails.setStrBottomBtnId(null);
			} else {
				menuDetails.setStrBottomBtnId(bottmBtnId);
			}
			buttonDetails.setBtnName(menuItemDetails[0]);
			buttonDetails.setBtnAction(menuItemDetails[1]);
			buttonDetails.setLinkId(menuItemDetails[2]);
			buttonDetails.setBtnPosition(menuItemDetails[3]);
			buttonDetails.setBtnImage(menuItemDetails[4]);
			buttonDetails.setBtnType(menuItemDetails[5]);
			buttonDetails.setBtnDept(menuItemDetails[6]);
			buttonDetails.setBtnShape(menuItemDetails[7]);
			buttonDetails.setFindSubCatIds(menuItemDetails[8]);
			buttonDetails.setCitiId(menuItemDetails[9]);
			menuDetails.setButtonDetails(buttonDetails);
			menuDetails.setMenuTypeName(templateType);
			menuDetails.setBannerImg(bannerImg);
			menuDetails.setDepartmentFlag(deptFilter);
			menuDetails.setTypeFlag(tyepFilter);
			daoStatus = hubCitiDAO.saveMenuTemplate(menuDetails, loginuser);
		}

		catch (FileNotFoundException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}
		return daoStatus;
	}

	public MenuDetails displayMenu(MenuDetails menuDetails, User user)
			throws HubCitiServiceException {
		final String methodName = "displayMenu";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		MenuDetails mainMenuDetails = null;
		try {
			mainMenuDetails = hubCitiDAO
					.fetchMainMenuDetails(menuDetails, user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return mainMenuDetails;
	}

	public SubMenuDetails displaySubMenu(User user,
			ScreenSettings screenSettings) throws HubCitiServiceException {
		final String methodName = "displayMenu";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		SubMenuDetails subMenuDetails = null;
		try {
			subMenuDetails = hubCitiDAO.fetchSubMenuDetails(user,
					screenSettings);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return subMenuDetails;
	}

	public List<AppSiteDetails> getAppSites(String searchKey, int ihubCityId,
			Integer lowerLimit) throws HubCitiServiceException {
		final String methodName = "getAppSites";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<AppSiteDetails> appSiteDetailsLst = null;

		try {
			appSiteDetailsLst = hubCitiDAO.getAppSites(searchKey, ihubCityId,
					lowerLimit);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return appSiteDetailsLst;
	}

	public String saveUpdateTwoTabMenuTemplate(User loginuser,
			List<ScreenSettings> menuItemsList, Integer menuId,
			Integer menuLevel, String bannerImg, String menuName,
			String bottmBtnId, String templateType, boolean tyepFilter,
			boolean deptFilter, String btnView) throws HubCitiServiceException {
		final String methodName = "saveUpdateTwoTabMenuTemplate";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String daoStatus = null;
		final String fileSeparator = System.getProperty("file.separator");
		InputStream inputStream = null;
		MenuDetails menuDetails = null;
		ButtonDetails buttonDetails = null;
		String[] menuItemDetails = null;
		int index = 1;
		String linkId = null;
		String subCatIds = null;
		StringBuilder mainCatId = null;
		StringBuilder findSubCatId = null;
		try {
			final StringBuilder mediaPathBuilder = Utility.getMediaPath(
					ApplicationConstants.HUBCITI, loginuser.getHubCitiID());
			final StringBuilder mediaTempPathBuilder = Utility
					.getTempMediaPath(ApplicationConstants.TEMP);
			final String hubCitiMediaPath = mediaPathBuilder.toString();
			final String tempMediaPath = mediaTempPathBuilder.toString();
			menuDetails = new MenuDetails();
			menuItemDetails = new String[9];
			buttonDetails = new ButtonDetails();

			if (null != bannerImg && !"".equals(bannerImg)) {

				inputStream = new BufferedInputStream(new FileInputStream(
						tempMediaPath + fileSeparator + bannerImg));
				if (null != inputStream) {
					Utility.writeFileData(inputStream, hubCitiMediaPath
							+ fileSeparator + bannerImg);

				}

			}
			for (ScreenSettings menuItem : menuItemsList) {
				linkId = null;
				subCatIds = null;
				if (index == 1) {
					menuItemDetails[0] = menuItem.getMenuBtnName();
					menuItemDetails[1] = menuItem.getMenuFucntionality();
					if (null != menuItem.getBtnLinkId()
							&& !"".equals(menuItem.getBtnLinkId())) {
						if (menuItem.getBtnLinkId().contains(",")) {

							if (menuItem.getBtnLinkId().contains("MC")) {
								String[] strings = menuItem.getBtnLinkId()
										.split(",");
								mainCatId = new StringBuilder();
								for (int i = 0; i < strings.length; i++) {

									if (null != strings[i]
											&& strings[i].contains("MC")) {
										mainCatId
												.append(strings[i]
														.substring(
																0,
																strings[i]
																		.lastIndexOf("-")));
										if (i != strings.length - 1) {
											mainCatId.append(",");
										}
									}
								}
								linkId = mainCatId.toString();
								linkId = linkId.replaceAll(",", "|");

							} else {

								linkId = menuItem.getBtnLinkId().replaceAll(
										",", "|");
							}

						} else {
							if (menuItem.getBtnLinkId().contains("MC")) {
								linkId = menuItem.getBtnLinkId().substring(
										0,
										menuItem.getBtnLinkId()
												.lastIndexOf("-"));

							} else {
								linkId = menuItem.getBtnLinkId();
							}

						}

					}

					if (null != menuItem.getSubCatIds()
							&& !"".equals(menuItem.getSubCatIds())) {
						if (menuItem.getSubCatIds().contains(",")) {
							String[] fstrings = menuItem.getSubCatIds().split(
									",");
							findSubCatId = new StringBuilder();
							for (int i = 0; i < fstrings.length; i++) {
								if (null != fstrings[i]
										&& !fstrings[i].equals("")
										&& !"null"
												.equalsIgnoreCase(fstrings[i])) {
									findSubCatId.append(fstrings[i]);
									findSubCatId.append(",");
								}
							}
							subCatIds = findSubCatId.toString();

							subCatIds = subCatIds.replaceAll(",", "|");

						} else {

							subCatIds = menuItem.getSubCatIds();
						}
					}

					if (!Utility.isEmptyOrNullString(subCatIds)) {
						if (subCatIds.endsWith("|")
								|| subCatIds.endsWith("!~~!")) {
							subCatIds = subCatIds.substring(0,
									subCatIds.length() - 1);
						}
					}
					/*
					 * if (null != menuItemDetails[2] &&
					 * !menuItemDetails[2].isEmpty()) { if
					 * (menuItemDetails[2].endsWith("|")) { menuItemDetails[2] =
					 * menuItemDetails[2].substring(0,
					 * menuItemDetails[2].length() - 1); } }
					 */

					menuItemDetails[2] = linkId;
					if (null != menuItemDetails[2]
							&& !menuItemDetails[2].isEmpty()) {
						if (menuItemDetails[2].endsWith("|")) {
							menuItemDetails[2] = menuItemDetails[2].substring(
									0, menuItemDetails[2].length() - 1);
						}
					}
					menuItemDetails[3] = String.valueOf(index);
					menuItemDetails[4] = null;
					if (null != subCatIds) {
						menuItemDetails[7] = subCatIds;
					} else {
						menuItemDetails[7] = null;
					}

					if (tyepFilter) {
						menuItemDetails[5] = menuItem.getBtnType();
					} else {

						menuItemDetails[5] = null;
					}

					if (deptFilter) {
						menuItemDetails[6] = menuItem.getBtnDept();
					} else {

						menuItemDetails[6] = null;
					}
					if (null != menuItem.getCitiId()
							&& !"".equalsIgnoreCase(menuItem.getCitiId())) {
						menuItemDetails[8] = menuItem.getCitiId().replaceAll(
								",", "|");
					} else {
						menuItemDetails[8] = null;
					}
				}

				// if more than 1 buttons
				else {

					menuItemDetails[0] = menuItemDetails[0] + ITEMDELEMETER
							+ menuItem.getMenuBtnName();
					menuItemDetails[1] = menuItemDetails[1] + ITEMDELEMETER
							+ menuItem.getMenuFucntionality();

					if (null != menuItem.getBtnLinkId()
							&& !"".equals(menuItem.getBtnLinkId())) {
						if (menuItem.getBtnLinkId().contains(",")) {

							if (menuItem.getBtnLinkId().contains("MC")) {
								String[] strings = menuItem.getBtnLinkId()
										.split(",");
								mainCatId = new StringBuilder();
								for (int i = 0; i < strings.length; i++) {

									if (null != strings[i]
											&& strings[i].contains("MC")) {
										mainCatId
												.append(strings[i]
														.substring(
																0,
																strings[i]
																		.lastIndexOf("-")));
										if (i != strings.length - 1) {
											mainCatId.append(",");
										}

									}
								}
								linkId = mainCatId.toString();
								linkId = linkId.replaceAll(",", "|");

							} else {

								linkId = menuItem.getBtnLinkId().replaceAll(
										",", "|");
							}

						} else {
							if (menuItem.getBtnLinkId().contains("MC")) {
								linkId = menuItem.getBtnLinkId().substring(
										0,
										menuItem.getBtnLinkId()
												.lastIndexOf("-"));

							} else {
								linkId = menuItem.getBtnLinkId();
							}

						}

					}

					/*
					 * if (null != menuItem.getSubCatIds() &&
					 * !"".equals(menuItem.getSubCatIds())) { if
					 * (menuItem.getSubCatIds().contains(",")) { subCatIds =
					 * menuItem.getSubCatIds().replaceAll(",", "|"); } else {
					 * subCatIds = menuItem.getSubCatIds(); } }
					 */
					if (null != menuItem.getSubCatIds()
							&& !"".equals(menuItem.getSubCatIds())) {
						if (menuItem.getSubCatIds().contains(",")) {
							String[] fstrings = menuItem.getSubCatIds().split(
									",");
							findSubCatId = new StringBuilder();
							for (int i = 0; i < fstrings.length; i++) {
								if (null != fstrings[i]
										&& !fstrings[i].equals("")
										&& !"null"
												.equalsIgnoreCase(fstrings[i])) {
									findSubCatId.append(fstrings[i]);
									findSubCatId.append(",");
								}
							}
							subCatIds = findSubCatId.toString();

							subCatIds = subCatIds.replaceAll(",", "|");

						} else {

							subCatIds = menuItem.getSubCatIds();
						}
					}
					if (!Utility.isEmptyOrNullString(subCatIds)) {
						if (subCatIds.endsWith("|")
								|| subCatIds.endsWith("!~~!")) {
							subCatIds = subCatIds.substring(0,
									subCatIds.length() - 1);
						}

					}
					if (!Utility.isEmptyOrNullString(linkId)) {
						if (linkId.endsWith("|")) {
							linkId = linkId.substring(0, linkId.length() - 1);
						}
					}
					menuItemDetails[2] = menuItemDetails[2] + ITEMDELEMETER
							+ linkId;
					menuItemDetails[3] = menuItemDetails[3] + ITEMDELEMETER
							+ String.valueOf(index);
					menuItemDetails[4] = menuItemDetails[4] + ITEMDELEMETER
							+ null;

					if (null != subCatIds) {
						if (null != menuItemDetails[7]) {
							menuItemDetails[7] = menuItemDetails[7]
									+ ITEMDELEMETER + subCatIds;
						} else {
							menuItemDetails[7] = subCatIds;
						}
					}

					if (tyepFilter) {
						menuItemDetails[5] = menuItemDetails[5] + ITEMDELEMETER
								+ menuItem.getBtnType();
					}

					if (deptFilter) {
						menuItemDetails[6] = menuItemDetails[6] + ITEMDELEMETER
								+ menuItem.getBtnDept();
					}
					if (null != menuItem.getCitiId()
							&& !"".equalsIgnoreCase(menuItem.getCitiId())) {
						menuItemDetails[8] = menuItemDetails[8] + ITEMDELEMETER
								+ menuItem.getCitiId().replaceAll(",", "|");
					} else {
						menuItemDetails[8] = menuItemDetails[8] + ITEMDELEMETER
								+ null;
					}

				}
				index++;
			}

			menuDetails.setMenuId(menuId);
			menuDetails.setLevel(menuLevel);
			menuDetails.setMenuName(menuName);
			if ("".equals(bottmBtnId)) {
				menuDetails.setStrBottomBtnId(null);
			} else {

				menuDetails.setStrBottomBtnId(bottmBtnId);
			}

			if (null != menuItemDetails[2] && !menuItemDetails[2].isEmpty()) {
				if (menuItemDetails[2].endsWith("|")) {
					menuItemDetails[2] = menuItemDetails[2].substring(0,
							menuItemDetails[2].length() - 1);
				}
			}
			if (null != menuItemDetails[7] && !menuItemDetails[7].isEmpty()) {
				if (menuItemDetails[7].endsWith("|!~~!")) {
					menuItemDetails[7] = menuItemDetails[7].substring(0,
							menuItemDetails[7].lastIndexOf("|"));
				}

				/*
				 * if (menuItemDetails[7].contains("||") ) { menuItemDetails[7]
				 * = menuItemDetails[7].replace("||", "|"); }else if(
				 * menuItemDetails[7].contains("|||")) { menuItemDetails[7] =
				 * menuItemDetails[7].replace("|||", "|"); }
				 */
			}

			buttonDetails.setBtnName(menuItemDetails[0]);
			buttonDetails.setBtnAction(menuItemDetails[1]);
			buttonDetails.setLinkId(menuItemDetails[2]);
			buttonDetails.setBtnPosition(menuItemDetails[3]);
			buttonDetails.setBtnImage(menuItemDetails[4]);
			buttonDetails.setBtnType(menuItemDetails[5]);
			buttonDetails.setBtnDept(menuItemDetails[6]);
			buttonDetails.setFindSubCatIds(menuItemDetails[7]);
			buttonDetails.setCitiId(menuItemDetails[8]);
			menuDetails.setBannerImg(bannerImg);
			menuDetails.setButtonDetails(buttonDetails);
			menuDetails.setMenuTypeName(templateType);
			menuDetails.setDepartmentFlag(deptFilter);
			menuDetails.setTypeFlag(tyepFilter);

			// for setting button view is single column or two column
			if (null != btnView && !"".equals(btnView)) {
				if (btnView.equals(ApplicationConstants.SINGLECOLUMN)) {
					menuDetails.setNoOfColumns(1);
				} else {
					menuDetails.setNoOfColumns(2);
				}

			} else {
				menuDetails.setNoOfColumns(null);
			}

			daoStatus = hubCitiDAO.saveMenuTemplate(menuDetails, loginuser);
		}

		catch (FileNotFoundException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}
		return daoStatus;
	}

	/**
	 * This will save Anything screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String saveAnyThingScreen(ScreenSettings objScreenSettings, User user)
			throws HubCitiServiceException {
		final String methodName = "saveAnyThingScreen";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		final String strResponse;
		String filePath = null;
		try {
			final StringBuilder mediaPathBuilder = Utility.getMediaPath(
					ApplicationConstants.HUBCITI, user.getHubCitiID());
			final StringBuilder mediaTempPathBuilder = Utility
					.getTempMediaPath(ApplicationConstants.TEMP);
			final String hubCitiMediaPath = mediaPathBuilder.toString();
			final String tempMediaPath = mediaTempPathBuilder.toString();
			final String fileSeparator = System.getProperty("file.separator");

			if (!"".equals(Utility.checkNull(objScreenSettings
					.getLogoImageName()))) {
				final InputStream inputStream = new BufferedInputStream(
						new FileInputStream(tempMediaPath + fileSeparator
								+ objScreenSettings.getLogoImageName()));
				if (null != inputStream) {
					Utility.writeFileData(inputStream,
							hubCitiMediaPath + fileSeparator
									+ objScreenSettings.getLogoImageName());
				}
			}

			if (null != objScreenSettings.getFilePaths()
					&& !objScreenSettings.getFilePaths().isEmpty()) {
				filePath = mediaPathBuilder
						+ fileSeparator
						+ objScreenSettings.getFilePaths()
								.getOriginalFilename();
				Utility.writeFileData(objScreenSettings.getFilePaths(),
						filePath);

				if ("Image"
						.equalsIgnoreCase(objScreenSettings.getPageTypeHid())) {
					String imageSource = FilenameUtils
							.removeExtension(objScreenSettings.getFilePaths()
									.getOriginalFilename());
					imageSource = imageSource + ".png";
					objScreenSettings.setPathName(imageSource);
				} else {
					objScreenSettings.setPathName(objScreenSettings
							.getFilePaths().getOriginalFilename());
				}
			} else if (null != objScreenSettings.getPathName()
					&& !"".equalsIgnoreCase(objScreenSettings.getPathName())) {
				filePath = mediaPathBuilder + fileSeparator
						+ objScreenSettings.getPathName();
				InputStream inputStream = new FileInputStream(
						mediaTempPathBuilder + fileSeparator
								+ objScreenSettings.getPathName());
				Utility.writeFileData(inputStream, filePath);
			}

			strResponse = hubCitiDAO
					.saveAnyThingScreen(objScreenSettings, user);

		} catch (HubCitiWebSqlException e) {
			LOG.error("Inside HubCitiServiceImpl : saveAnyThingScreen"
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		} catch (FileNotFoundException e) {
			LOG.error("Inside HubCitiServiceImpl : saveAnyThingScreen"
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	/**
	 * This serviceImpl method is used for fetching the image icons to display
	 * in link page.
	 * 
	 * @return
	 * @throws HubCitiServiceException
	 *             will be thrown as service exception.
	 * @throws HubCitiWebSqlException
	 */
	public List<HubCitiImages> getHubCitiImageIconsDisplay(String pageType)
			throws HubCitiServiceException {
		final String methodName = "getHubCitiImageIconsDisplay";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<HubCitiImages> retailerLocations = null;

		try {
			retailerLocations = hubCitiDAO
					.getHubCitiImageIconsDisplay(pageType);
		} catch (HubCitiWebSqlException e) {
			LOG.error("Inside HubCitiServiceImpl : getHubCitiImageIconsDisplay"
					+ e.getMessage());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return retailerLocations;
	}

	public List<MenuDetails> getBottomLinkList(int ihubCityId)
			throws HubCitiServiceException {

		final String methodName = "getButtomLinkList";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<MenuDetails> linkList = null;

		try {
			linkList = hubCitiDAO.getBottomLinkList(ihubCityId);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return linkList;
	}

	public List<ScreenSettings> getBottomBarExistingIcons()
			throws HubCitiServiceException {
		final String methodName = "getButtomBarExistingIcons";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<ScreenSettings> existingIconsList = null;

		try {
			existingIconsList = hubCitiDAO.getBottomBarExistingIcons();
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return existingIconsList;
	}

	public String saveUpdateTabBarButton(ScreenSettings buttondetails,
			User loginUser) throws HubCitiServiceException {
		final String methodName = "getButtomBarExistingIcons";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String status = ApplicationConstants.FAILURE;
		final String fileSeparator = System.getProperty("file.separator");
		InputStream inputStream = null;
		StringBuilder mainCatId = null;
		String linkId = null;
		String subCatIds = null;
		StringBuilder findSubCatId = null;
		try {

			final StringBuilder mediaPathBuilder = Utility.getMediaPath(
					ApplicationConstants.HUBCITI, loginUser.getHubCitiID());
			final StringBuilder mediaTempPathBuilder = Utility
					.getTempMediaPath(ApplicationConstants.TEMP);
			final String hubCitiMediaPath = mediaPathBuilder.toString();
			final String tempMediaPath = mediaTempPathBuilder.toString();
			if (null != buttondetails.getLogoImageName()
					&& !"".equals(buttondetails.getLogoImageName())) {

				inputStream = new BufferedInputStream(new FileInputStream(
						tempMediaPath + fileSeparator
								+ buttondetails.getLogoImageName()));
				if (null != inputStream) {
					Utility.writeFileData(inputStream, hubCitiMediaPath
							+ fileSeparator + buttondetails.getLogoImageName());

				}

			}
			if (null != buttondetails.getBannerImageName()
					&& !"".equals(buttondetails.getBannerImageName())) {

				inputStream = new BufferedInputStream(new FileInputStream(
						tempMediaPath + fileSeparator
								+ buttondetails.getBannerImageName()));
				if (null != inputStream) {
					Utility.writeFileData(
							inputStream,
							hubCitiMediaPath + fileSeparator
									+ buttondetails.getBannerImageName());

				}

			}

			if (null != buttondetails.getBtnLinkId()
					&& !"".equals(buttondetails.getBtnLinkId())) {
				if (buttondetails.getBtnLinkId().contains(",")) {

					if (buttondetails.getBtnLinkId().contains("MC")) {
						String[] strings = buttondetails.getBtnLinkId().split(
								",");
						mainCatId = new StringBuilder();
						for (int i = 0; i < strings.length; i++) {

							if (null != strings[i] && strings[i].contains("MC")) {
								mainCatId.append(strings[i].substring(0,
										strings[i].lastIndexOf("-")));
								if (i != strings.length - 1) {
									mainCatId.append(",");
								}
							}
						}
						linkId = mainCatId.toString();
						linkId = linkId.replaceAll(",", "!~~!");

					} else {

						linkId = buttondetails.getBtnLinkId();
						// .replaceAll(",", "!~~!");
					}

				} else {
					if (buttondetails.getBtnLinkId().contains("MC")) {
						linkId = buttondetails.getBtnLinkId().substring(0,
								buttondetails.getBtnLinkId().lastIndexOf("-"));

					} else {
						linkId = buttondetails.getBtnLinkId();
					}

				}

			}

			if (!Utility.isEmptyOrNullString(linkId)) {
				buttondetails.setBtnLinkId(linkId);
			}
			if (!Utility.isEmptyOrNullString(linkId)) {

				if (linkId.endsWith("|") || linkId.endsWith("!~~!")) {
					linkId = linkId.substring(0, linkId.length() - 1);
					buttondetails.setBtnLinkId(linkId);
				}
			}

			if (null != buttondetails.getSubCatIds()
					&& !"".equals(buttondetails.getSubCatIds())) {
				if (buttondetails.getSubCatIds().contains(",")) {
					String[] fstrings = buttondetails.getSubCatIds().split(",");
					findSubCatId = new StringBuilder();
					for (int i = 0; i < fstrings.length; i++) {
						if (null != fstrings[i] && !fstrings[i].equals("")
								&& !"null".equalsIgnoreCase(fstrings[i])) {
							findSubCatId.append(fstrings[i]);
							findSubCatId.append(",");
						}
					}
					subCatIds = findSubCatId.toString();

					subCatIds = subCatIds.replaceAll(",", "|");

				} else {

					subCatIds = buttondetails.getSubCatIds();

				}

			}
			if (!Utility.isEmptyOrNullString(subCatIds)) {

				if (subCatIds.endsWith("|") || subCatIds.endsWith("!~~!")) {
					subCatIds = subCatIds.substring(0, subCatIds.length() - 1);
					buttondetails.setSubCatIds(subCatIds);
				}
			}

			if (!Utility.isEmptyOrNullString(buttondetails.getSubCatIds())) {
				subCatIds = buttondetails.getSubCatIds().replaceAll(",", "|");
				buttondetails.setSubCatIds(subCatIds);
			} else {
				buttondetails.setSubCatIds(null);
			}

			status = hubCitiDAO.insertUpdateTabBarButton(buttondetails,
					loginUser);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		} catch (FileNotFoundException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	public List<ScreenSettings> getTabBarButtons(MenuDetails menuDetails,
			User loginUser) throws HubCitiServiceException {
		final String methodName = "getButtomBarExistingIcons";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<ScreenSettings> tabBarButtonsList = null;

		try {
			tabBarButtonsList = hubCitiDAO.fetchTabBarButtons(menuDetails,
					loginUser);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return tabBarButtonsList;
	}

	public String deleteTabBarButton(Integer bottomBtnId)
			throws HubCitiServiceException {
		final String methodName = "deleteTabBarButton";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String status = ApplicationConstants.FAILURE;
		try {
			status = hubCitiDAO.deleteTabBarButton(bottomBtnId);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	public List<AppSiteDetails> getHubCityRetailer(AppSiteDetails appSiteDetails)
			throws HubCitiServiceException {
		final String methodName = "getAppSites";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<AppSiteDetails> appSiteDetailsLst = null;

		try {
			appSiteDetailsLst = hubCitiDAO.getHubCityRetailer(appSiteDetails);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return appSiteDetailsLst;
	}

	public List<AppSiteDetails> displayRetailLocations(
			AppSiteDetails appSiteDetails) throws HubCitiServiceException {
		final String methodName = "displayRetailLocations";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<AppSiteDetails> appSiteDetailsLst = null;

		try {
			appSiteDetailsLst = hubCitiDAO
					.displayRetailLocations(appSiteDetails);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return appSiteDetailsLst;
	}

	/**
	 * This will display anything pages created by user.
	 * 
	 * @param user
	 * @param searchKey
	 * @param lowerLimit
	 * @return
	 * @throws HubCitiServiceException
	 */
	public AnythingPages displayAnythingPage(User user, String searchKey,
			Integer lowerLimit) throws HubCitiServiceException {
		final String methodName = "displayAnythingPage";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		AnythingPages anythingPageDetails = null;

		try {
			anythingPageDetails = hubCitiDAO.fetchAnythingPages(user,
					searchKey, lowerLimit);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName,
					e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return anythingPageDetails;
	}

	/**
	 * This will return anything page types.
	 * 
	 * @return
	 * @throws HubCitiServiceException
	 */
	public List<HubCitiImages> getAnythingPageType()
			throws HubCitiServiceException {
		final String methodName = "getAnythingPageType";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<HubCitiImages> pageTypes = null;
		try {
			pageTypes = hubCitiDAO.getAnythingPageType();
		} catch (HubCitiWebSqlException e) {
			LOG.error("Inside HubCitiServiceImpl : getAnythingPageType"
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODSTART + methodName);
		return pageTypes;
	}

	/**
	 * This Method will return the anything page details.
	 * 
	 * @param objScreenSettings
	 * @param objUser
	 * @return
	 * @throws HubCitiServiceException
	 */
	public ScreenSettings getAnythingPage(ScreenSettings objScreenSettings,
			User objUser) throws HubCitiServiceException {
		final String methodName = "getAnythingPage";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		ScreenSettings anythingPage = null;

		try {
			anythingPage = hubCitiDAO.getAnyThingPage(objScreenSettings,
					objUser);
			if (null != anythingPage.getStartDate()) {
				anythingPage.setStartDate(Utility
						.formattedDateWithTime(anythingPage.getStartDate()));
			}
			if (null != anythingPage.getEndDate()) {
				anythingPage.setEndDate(Utility
						.formattedDateWithTime(anythingPage.getEndDate()));
			}
		} catch (HubCitiWebSqlException e) {
			LOG.error("Inside HubCitiServiceImpl : getAnythingPage"
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		} catch (ParseException e) {
			LOG.info("Inside HubCitiServiceImpl : getAnythingPage"
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODSTART + methodName);
		return anythingPage;
	}

	/**
	 * This Method will update anything page details.
	 * 
	 * @param objScreenSettings
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String updateAnyThingScreen(ScreenSettings objScreenSettings,
			User user) throws HubCitiServiceException {
		final String methodName = "updateAnyThingScreen";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		final String strResponse;
		final String fileSeparator = System.getProperty("file.separator");
		String filePath = null;
		try {
			final StringBuilder mediaPathBuilder = Utility.getMediaPath(
					ApplicationConstants.HUBCITI, user.getHubCitiID());
			final StringBuilder mediaTempPathBuilder = Utility
					.getTempMediaPath(ApplicationConstants.TEMP);
			final String hubCitiMediaPath = mediaPathBuilder.toString();
			final String tempMediaPath = mediaTempPathBuilder.toString();

			if (!"".equals(Utility.checkNull(objScreenSettings
					.getLogoImageName()))
					&& null != objScreenSettings.getOldImageName()
					&& !objScreenSettings.getOldImageName().equals(
							objScreenSettings.getLogoImageName())) {
				final InputStream inputStream = new BufferedInputStream(
						new FileInputStream(tempMediaPath + fileSeparator
								+ objScreenSettings.getLogoImageName()));
				if (null != inputStream) {
					Utility.writeFileData(inputStream,
							hubCitiMediaPath + fileSeparator
									+ objScreenSettings.getLogoImageName());
				}
			}
			if (null != objScreenSettings.getFilePaths()
					&& !objScreenSettings.getFilePaths().isEmpty()) {
				filePath = mediaPathBuilder
						+ fileSeparator
						+ objScreenSettings.getFilePaths()
								.getOriginalFilename();
				Utility.writeFileData(objScreenSettings.getFilePaths(),
						filePath);

				if ("Image"
						.equalsIgnoreCase(objScreenSettings.getPageTypeHid())) {
					String imageSource = FilenameUtils
							.removeExtension(objScreenSettings.getFilePaths()
									.getOriginalFilename());
					imageSource = imageSource + ".png";
					objScreenSettings.setPathName(imageSource);
				} else {
					objScreenSettings.setPathName(objScreenSettings
							.getFilePaths().getOriginalFilename());
				}
			} else if (null != objScreenSettings.getPathName()
					&& !"".equalsIgnoreCase(objScreenSettings.getPathName())
					&& null != objScreenSettings.getOldFileName()
					&& !objScreenSettings.getOldFileName().equals(
							objScreenSettings.getPathName())) {
				filePath = mediaPathBuilder + fileSeparator
						+ objScreenSettings.getPathName();
				InputStream inputStream = new FileInputStream(
						mediaTempPathBuilder + fileSeparator
								+ objScreenSettings.getPathName());
				Utility.writeFileData(inputStream, filePath);
			}

			strResponse = hubCitiDAO.updateAnyThingScreen(objScreenSettings,
					user);

		} catch (HubCitiWebSqlException e) {
			LOG.error("Inside HubCitiServiceImpl : saveAnyThingScreen"
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		} catch (FileNotFoundException e) {
			LOG.error("Inside HubCitiServiceImpl : saveAnyThingScreen"
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	public String saveAppSite(AppSiteDetails appSiteDetails)
			throws HubCitiServiceException {
		final String methodName = "saveAppSite";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String response = null;

		try {
			response = hubCitiDAO.saveAppSite(appSiteDetails);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return response;
	}

	/**
	 * This Method will update anything page details.
	 * 
	 * @param objScreenSettings
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String deleteAnyThingPage(String anythingPageID, User objUser)
			throws HubCitiServiceException {
		final String methodName = "deleteAnyThingPage";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;

		try {
			status = hubCitiDAO.deleteAnyThingPage(anythingPageID, objUser);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName,
					e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	/**
	 * This will return login screen details.
	 * 
	 * @param loginUser
	 * @return
	 * @throws HubCitiServiceException
	 */
	public ScreenSettings fetchGeneralSettings(Integer hubCitiID,
			String settingType) throws HubCitiServiceException {
		final String methodName = "fetchGeneralSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		ScreenSettings generalSettings = null;

		try {
			generalSettings = hubCitiDAO.fetchGeneralSettings(hubCitiID,
					settingType);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return generalSettings;
	}

	public List<Category> fetchBusinessCategoryList()
			throws HubCitiServiceException {
		final String methodName = "fetchBusinessCategoryList";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<Category> categoryList = null;

		try {
			categoryList = hubCitiDAO.fetchBusinessCategoryList();
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return categoryList;
	}

	public String addAlertCategory(String catName, User objUser)
			throws HubCitiServiceException {
		final String strMethodName = "addAlertCategory";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		String strResponse = null;

		try {
			strResponse = hubCitiDAO.addAlertCategory(catName, objUser);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + strMethodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strResponse;

	}

	public AlertCategory fetchAlertCategories(Category category, User user)
			throws HubCitiServiceException {
		final String methodName = "fetchAlertCategories";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		AlertCategory objCategory = null;

		try {
			objCategory = hubCitiDAO.fetchAlertCategories(category, user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return objCategory;
	}

	public List<Severity> fetchAlertSeverities() throws HubCitiServiceException {
		final String methodName = "fetchAlertSeverities";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<Severity> severityList = null;

		try {
			severityList = hubCitiDAO.fetchAlertSeverities();
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return severityList;
	}

	/**
	 * This method will display alerts.
	 * 
	 * @param userId
	 * @param hubCitiId
	 * @param searchKey
	 * @param lowerLimit
	 * @return
	 * @throws HubCitiServiceException
	 */
	public AlertsDetails displaySearchAlerts(Integer userId, Integer hubCitiId,
			String searchKey, Integer lowerLimit)
			throws HubCitiServiceException {
		final String methodName = "displaySearchAlerts";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		AlertsDetails alerts = null;

		try {
			alerts = hubCitiDAO.displaySearchAlerts(userId, hubCitiId,
					searchKey, lowerLimit);

			if (null != alerts && !alerts.getAlerts().isEmpty()) {
				for (Alerts alerts2 : alerts.getAlerts()) {
					if (null != alerts2.getStartDate()) {
						alerts2.setStartDate(Utility.formattedDate(alerts2
								.getStartDate()));
					}
					if (null != alerts2.getEndDate()) {
						alerts2.setEndDate(Utility.formattedDate(alerts2
								.getEndDate()));
					}
				}
			}
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName,
					e.getStackTrace());
			throw new HubCitiServiceException(e);
		} catch (ParseException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName,
					e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return alerts;
	}

	/**
	 * This method will save alter details.
	 * 
	 * @param alerts
	 * @param objUser
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String saveAlerts(Alerts alerts, User objUser)
			throws HubCitiServiceException {
		final String methodName = "saveAlerts";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;

		try {
			status = hubCitiDAO.saveAlerts(alerts, objUser);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	public String deleteAlertCategory(int cateId, User objUser)
			throws HubCitiServiceException {
		final String strMethodName = "deleteAlertCategory";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		String strResponse = null;

		try {
			strResponse = hubCitiDAO.deleteAlertCategory(cateId, objUser);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + strMethodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strResponse;
	}

	public String updateAlertCategory(Category category, User user)
			throws HubCitiServiceException {
		final String strMethodName = "deleteAlertCategory";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		String strResponse = null;

		try {
			strResponse = hubCitiDAO.updateAlertCategory(category, user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + strMethodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strResponse;
	}

	public MenuFilterTyes getMenuFilterTypes(int hubCitiId)
			throws HubCitiServiceException {
		final String methodName = "getMenuFilterTypes";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		MenuFilterTyes menuFilterTyes = null;

		try {
			menuFilterTyes = hubCitiDAO.getMenuFilterTypes(hubCitiId);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return menuFilterTyes;
	}

	/**
	 * This method will return alter details.
	 * 
	 * @param alertID
	 * @return
	 * @throws HubCitiServiceException
	 */
	public Alerts fetchAlertDetails(Integer alertId)
			throws HubCitiServiceException {
		final String methodName = "fetchAlertDetails";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Alerts alertsDetails = null;

		try {
			alertsDetails = hubCitiDAO.fetchAlertDetails(alertId);

			if (null != alertsDetails.getStartDate()) {
				alertsDetails.setStartDate(Utility.formattedDate(alertsDetails
						.getStartDate()));
			}
			if (null != alertsDetails.getEndDate()) {
				alertsDetails.setEndDate(Utility.formattedDate(alertsDetails
						.getEndDate()));
			}
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName,
					e.getStackTrace());
			throw new HubCitiServiceException(e);
		} catch (ParseException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName,
					e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return alertsDetails;
	}

	/**
	 * This method will update alter details.
	 * 
	 * @param alerts
	 * @param objUser
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String updateAlerts(Alerts alerts, User objUser)
			throws HubCitiServiceException {
		final String methodName = "updateAlerts";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;

		try {
			status = hubCitiDAO.updateAlerts(alerts, objUser);
		} catch (HubCitiWebSqlException e) {
			LOG.error("Inside HubCitiServiceImpl : saveAnyThingScreen"
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	/**
	 * This method will delete alter.
	 * 
	 * @param alertID
	 * @param objUser
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String deleteAlerts(Integer alertID, User objUser)
			throws HubCitiServiceException {
		final String methodName = "deleteAlerts";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;

		try {
			status = hubCitiDAO.deleteAlerts(alertID, objUser);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName,
					e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	public List<ScreenSettings> displayButtomBtnType()
			throws HubCitiServiceException {
		final String methodName = "displayButtomBtnType";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		List<ScreenSettings> buttomBtnType = null;

		try {
			buttomBtnType = hubCitiDAO.displayButtomBtnType();
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return buttomBtnType;
	}

	public AlertCategory fetchEventCategories(Category category, User user)
			throws HubCitiServiceException {
		final String methodName = "fetchEventCategories";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		AlertCategory objCategory = null;

		try {
			objCategory = hubCitiDAO.fetchEventCategories(category, user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return objCategory;
	}

	public String addEventCategory(String catName, User objUser)
			throws HubCitiServiceException {
		final String strMethodName = "addEventCategory";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		String strResponse = null;

		try {
			strResponse = hubCitiDAO.addEventCategory(catName, objUser);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + strMethodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strResponse;
	}

	public String deleteEventCategory(int cateId, User user)
			throws HubCitiServiceException {
		final String strMethodName = "deleteEventCategory";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		String strResponse = null;

		try {
			strResponse = hubCitiDAO.deleteEventCategory(cateId, user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + strMethodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strResponse;
	}

	public String updateEventCategory(Category category, User user)
			throws HubCitiServiceException {
		final String strMethodName = "updateEventCategory";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		String strResponse = null;

		try {
			strResponse = hubCitiDAO.updateEventCategory(category, user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + strMethodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strResponse;
	}

	public EventDetail displayEvents(Event event, User user, Boolean fundraising)
			throws HubCitiServiceException {
		final String methodName = "displayEvents";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		EventDetail objEventDetail = null;
		List<Event> eventLst = null;
		try {
			objEventDetail = hubCitiDAO.displayEvents(event, user, fundraising);

			if (null != objEventDetail) {
				eventLst = objEventDetail.getEventLst();
				if (null != eventLst && !"".equals(eventLst)) {
					for (Event ObjEvent : eventLst) {
						if (null != ObjEvent.getEventStartDate()
								&& !"".equals(ObjEvent.getEventStartDate())) {
							ObjEvent.setEventStartDate(Utility
									.formattedDate(ObjEvent.getEventStartDate()));
						}
						if (null != ObjEvent.getEventEndDate()
								&& !"".equals(ObjEvent.getEventEndDate())) {
							ObjEvent.setEventEndDate(Utility
									.formattedDate(ObjEvent.getEventEndDate()));
						}
					}
				}
			}

		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		} catch (ParseException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return objEventDetail;
	}

	public String deleteEvent(Integer eventId, User user)
			throws HubCitiServiceException {
		final String strMethodName = "deleteEvent";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		String strResponse = null;

		try {
			strResponse = hubCitiDAO.deleteEvent(eventId, user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + strMethodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strResponse;
	}

	public List<SearchZipCode> getCityStateZip(String city, Integer hubCitiId)
			throws HubCitiServiceException {
		final String strMethodName = "getCityStateZip";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		List<SearchZipCode> cities = null;
		try {
			cities = (List<SearchZipCode>) hubCitiDAO.getCityStateZip(city,
					hubCitiId);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + strMethodName, e);
			throw new HubCitiServiceException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return cities;

	}

	public List<SearchZipCode> getZipStateCity(String zipCode, Integer hubCitiId)
			throws HubCitiServiceException {

		final String strMethodName = "getCityStateZip";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		List<SearchZipCode> zipCodes = null;
		try {
			zipCodes = (List<SearchZipCode>) hubCitiDAO.getZipStateCity(
					zipCode, hubCitiId);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + strMethodName, e);
			throw new HubCitiServiceException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return zipCodes;

	}

	/**
	 * The service method for displaying all the states and it will call DAO.
	 * 
	 * @throws ScanSeeServiceException
	 *             as service exception.
	 * @return states,List of states.
	 */
	public final List<State> getAllStates(Integer hubCitiID)
			throws HubCitiServiceException {
		LOG.info("Inside RetailerServiceImpl : getAllStates ");
		List<State> states = null;
		try {
			states = hubCitiDAO.getAllStates(hubCitiID);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED, e);
			throw new HubCitiServiceException(e);
		}
		return states;
	}

	public List<RetailLocation> getHotelList(Integer hubCitiId, String searchKey)
			throws HubCitiServiceException

	{
		final String methodName = "getHotelList";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<RetailLocation> hotelList = null;

		try {

			hotelList = hubCitiDAO.getHotelList(hubCitiId, searchKey);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return hotelList;
	}

	public String saveUpdateEventDeatils(Event eventDetails, User user)
			throws HubCitiServiceException {
		final String methodName = "saveEventDeatils";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		final String commas = ",";
		String strAddress = null;
		String response = null;
		List<RetailLocation> hotelList = null;
		String[] hotelData = new String[7];
		int index = 1;
		InputStream inputStream = null;
		final String fileSeparator = System.getProperty("file.separator");
		try {

			final StringBuilder mediaPathBuilder = Utility.getMediaPath(
					ApplicationConstants.HUBCITI, user.getHubCitiID());
			final StringBuilder mediaTempPathBuilder = Utility
					.getTempMediaPath(ApplicationConstants.TEMP);
			final String hubCitiMediaPath = mediaPathBuilder.toString();
			final String tempMediaPath = mediaTempPathBuilder.toString();
			/*
			 * if (!eventDetails.isGeoError()) {
			 */

			if (null != eventDetails.getBsnsLoc()
					&& "no".equals(eventDetails.getBsnsLoc())) {
				strAddress = eventDetails.getAddress().trim() + commas
						+ eventDetails.getCity() + commas
						+ eventDetails.getState();

				final GAddress objGAddress = Utility.getGeoDetails(strAddress);
				if (!"".equals(Utility.checkNull(objGAddress))) {
					if ("OK".equals(objGAddress.getStatus())
							&& "ROOFTOP".equals(objGAddress.getLocationType())) {
						eventDetails.setLatitude(objGAddress.getLat());
						eventDetails.setLogitude(objGAddress.getLng());
						eventDetails.setGeoError(false);
					} else if (!eventDetails.isGeoError()) {
						response = "GEOERROR";
						return response;
					} else if (eventDetails.isGeoError()
							&& null == eventDetails.getLatitude()
							&& null == eventDetails.getLogitude()) {
						response = "GEOERROR";
						return response;

					}

				}
			}

			// }

			if (null != eventDetails.getEventImageName()
					&& !"".equals(eventDetails.getEventImageName())) {

				inputStream = new BufferedInputStream(new FileInputStream(
						tempMediaPath + fileSeparator
								+ eventDetails.getEventImageName()));
				if (null != inputStream) {
					Utility.writeFileData(inputStream, hubCitiMediaPath
							+ fileSeparator + eventDetails.getEventImageName());

				}
			}

			if (!"".equals(Utility.checkNull(eventDetails.getEvntHotel()))) {

				hotelList = Utility.jsonToObjectList(eventDetails
						.getHotelListJson());

				if (null != hotelList && !hotelList.isEmpty()) {

					for (RetailLocation hotelInfo : hotelList) {

						if (null != eventDetails.getRetailLocationID()
								&& eventDetails.getRetailLocationID().length != 0) {

							for (int i = 0; i < eventDetails
									.getRetailLocationID().length; i++) {

								if (hotelInfo.getRetailLocationID().equals(
										Integer.valueOf(eventDetails
												.getRetailLocationID()[i]))) {

									if (index == 1) {

										hotelData[0] = String.valueOf(hotelInfo
												.getRetailLocationID());
										hotelData[1] = hotelInfo
												.getHotelPrice();
										hotelData[2] = hotelInfo
												.getDiscountAmount();
										hotelData[3] = hotelInfo
												.getDiscountCode();
										hotelData[4] = hotelInfo
												.getRoomAvailabilityCheckURL();
										hotelData[5] = hotelInfo
												.getRoomBookingURL();
										hotelData[6] = hotelInfo.getRating();
										index++;
									} else {
										hotelData[0] = hotelData[0]
												+ ","
												+ String.valueOf(hotelInfo
														.getRetailLocationID());
										hotelData[1] = hotelData[1] + ","
												+ hotelInfo.getHotelPrice();
										hotelData[2] = hotelData[2] + ","
												+ hotelInfo.getDiscountAmount();
										hotelData[3] = hotelData[3] + ","
												+ hotelInfo.getDiscountCode();
										hotelData[4] = hotelData[4]
												+ ","
												+ hotelInfo
														.getRoomAvailabilityCheckURL();
										hotelData[5] = hotelData[5] + ","
												+ hotelInfo.getRoomBookingURL();
										hotelData[6] = hotelData[6] + ","
												+ hotelInfo.getRating();
									}
									break;
								}

							}
						}

					}

				}

				eventDetails.setHotelID(hotelData[0]);
				eventDetails.setHotelPrice(hotelData[1]);
				eventDetails.setDiscountAmount(hotelData[2]);
				eventDetails.setDiscountCode(hotelData[3]);
				eventDetails.setRoomAvailabilityCheckURL(hotelData[4]);
				eventDetails.setRoomBookingURL(hotelData[5]);
				eventDetails.setRating(hotelData[6]);
			}

			response = hubCitiDAO.saveUpdateEventDeatils(eventDetails, user);

			String[] arr = response.split(":");
			response = arr[0];
			if (null != eventDetails.getIsNewLogisticsImg()
					&& eventDetails.getIsNewLogisticsImg() == true) {
				if (arr.length > 1) {
					String eventId = arr[1];
					String mainFolder = "events";
					String path = hubCitiMediaPath + fileSeparator + mainFolder;
					File obj = new File(path);
					if (!obj.exists()) {
						obj.mkdir();
					}

					path = path + fileSeparator + eventId;
					obj = new File(path);
					if (!obj.exists()) {
						obj.mkdir();
					}

					if (null != eventDetails.getLogisticsImgName()
							&& !"".equals(eventDetails.getLogisticsImgName())) {
						inputStream = new BufferedInputStream(
								new FileInputStream(tempMediaPath
										+ fileSeparator
										+ eventDetails.getLogisticsImgName()));
						if (null != inputStream) {
							Utility.writeFileData(
									inputStream,
									path
											+ fileSeparator
											+ eventDetails
													.getLogisticsImgName());
						}
					}
				}
			}

		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		} catch (FileNotFoundException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		return response;

	}

	public CityExperienceDetail displayCityExperience(
			CityExperience cityExperience, User user, Integer lowerLimit)
			throws HubCitiServiceException {
		final String methodName = "displayCityExperience";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		CityExperienceDetail ObjCityExperienceDetail = null;
		try {
			ObjCityExperienceDetail = hubCitiDAO.displayCityExperience(
					cityExperience, user, lowerLimit);

		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return ObjCityExperienceDetail;
	}

	public CityExperienceDetail searchCityExperience(String retName,
			Integer lowerLimit, Integer filterID, User user)
			throws HubCitiServiceException {
		final String methodName = "searchCityExperience";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		CityExperienceDetail ObjCityExperienceDetail = null;
		try {
			ObjCityExperienceDetail = hubCitiDAO.searchCityExperience(retName,
					lowerLimit, filterID, user);

		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return ObjCityExperienceDetail;
	}

	public String saveCityExpRetLocs(String retLocIds,
			CityExperience cityExperience, User user)
			throws HubCitiServiceException {
		final String methodName = "saveCityExpRetLocs";

		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String strResponse = null;
		try {
			strResponse = hubCitiDAO.saveCityExpRetLocs(retLocIds,
					cityExperience, user);

		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	public String deleteRetLocation(CityExperience cityExperience)
			throws HubCitiServiceException {
		final String methodName = "deleteRetLocation";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String strResponse = null;
		try {
			strResponse = hubCitiDAO.deleteRetLocation(cityExperience);

		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	@SuppressWarnings("deprecation")
	public Event fetchEventDetails(Integer eventId)
			throws HubCitiServiceException {
		final String methodName = "saveCityExpRetLocs";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Event eventDetails = null;
		try {
			eventDetails = hubCitiDAO.fetchEventDetails(eventId);
			if (null != eventDetails) {

				eventDetails.setHiddenCategory(eventDetails.getEventCategory());

				if (null == eventDetails.getIsOngoing()
						|| "0".equalsIgnoreCase(eventDetails.getIsOngoing())) {
					eventDetails.setIsOngoing("no");

					Format formatter = new SimpleDateFormat("MM/dd/yyyy");
					Date date = new Date();

					if (null != eventDetails.getEventStartDate()
							&& !"".equals(eventDetails.getEventStartDate())) {
						String strtDate = Utility.formattedDate(eventDetails
								.getEventStartDate());
						eventDetails.setEventDate(strtDate);
						date = new Date(strtDate);
						eventDetails.setEventStartDate(strtDate);
					} else {
						eventDetails.setEventStartDate(formatter.format(date));
					}

					if (null != eventDetails.getEventStartTime()
							&& !"".equals(eventDetails.getEventStartTime())) {
						String eventTime = eventDetails.getEventStartTime();
						String[] tempTime = eventTime.split(":");
						eventDetails.setEventTimeHrs(tempTime[0]);
						eventDetails.setEventTimeMins(tempTime[1]);
					}

					if (null != eventDetails.getEventEndDate()
							&& !"".equals(eventDetails.getEventEndDate())) {
						String endDate = Utility.formattedDate(eventDetails
								.getEventEndDate());
						eventDetails.setEventEDate(endDate);
						eventDetails.setEventEndDate(endDate);
					} else {
						eventDetails.setEventEndDate(formatter.format(date));
					}
					if (null != eventDetails.getEventEndTime()
							&& !"".equals(eventDetails.getEventEndTime())) {
						String eventTime = eventDetails.getEventEndTime();
						String[] tempTime = eventTime.split(":");
						eventDetails.setEventETimeHrs(tempTime[0]);
						eventDetails.setEventETimeMins(tempTime[1]);
					}

					// Daily Recurrence
					eventDetails.setIsOngoingDaily("days");
					eventDetails.setEveryWeekDay(1);
					// Weekly Recurrence
					eventDetails.setEveryWeek(1);
					eventDetails.setIsOngoingMonthly("date");
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					Integer day = calendar.get(Calendar.DAY_OF_WEEK);
					eventDetails.setDays(new String[] { day.toString() });
					// Monthly Recurrence
					eventDetails.setDateOfMonth(calendar.get(Calendar.DATE));
					eventDetails.setEveryMonth(1);
					Calendar tempCalendar = Calendar.getInstance();
					tempCalendar.setTime(date);
					tempCalendar.set(Calendar.DATE, 1);
					if (calendar.get(Calendar.DAY_OF_WEEK) < tempCalendar
							.get(Calendar.DAY_OF_WEEK)) {
						eventDetails.setDayNumber((calendar
								.get(Calendar.WEEK_OF_MONTH)) - 1);
					} else {
						eventDetails.setDayNumber(calendar
								.get(Calendar.WEEK_OF_MONTH));
					}
					day = calendar.get(Calendar.DAY_OF_WEEK);
					eventDetails.setEveryWeekDayMonth(new String[] { day
							.toString() });
					eventDetails.setEveryDayMonth(1);
					eventDetails.setDayOfMonth(day.toString());
					// Range Of Recurrence
					eventDetails.setOccurenceType("noEndDate");
					eventDetails.setEndAfter(1);

				} else {
					eventDetails.setIsOngoing("yes");

					if (null != eventDetails.getEventStartDate()
							&& !"".equals(eventDetails.getEventStartDate())) {
						eventDetails
								.setEventStartDate(Utility
										.formattedDate(eventDetails
												.getEventStartDate()));
					}

					if (null != eventDetails.getEventEndDate()
							&& !"".equals(eventDetails.getEventEndDate())) {
						eventDetails.setEventEndDate(Utility
								.formattedDate(eventDetails.getEventEndDate()));
					}

					if ("Daily".equalsIgnoreCase(eventDetails
							.getRecurrencePatternName())) {
						if (eventDetails.getIsWeekDay() == false) {
							eventDetails.setIsOngoingDaily("days");
							eventDetails.setEveryWeekDay(eventDetails
									.getRecurrenceInterval());
						} else {
							eventDetails.setIsOngoingDaily("weekDays");
							eventDetails.setEveryWeekDay(1);
						}
						// Weekly Recurrence
						eventDetails.setEveryWeek(1);
						Date date = new Date(eventDetails.getEventStartDate());
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(date);
						Integer day = calendar.get(Calendar.DAY_OF_WEEK);
						eventDetails.setDays(new String[] { day.toString() });
						// Monthly Recurrence
						eventDetails.setIsOngoingMonthly("date");
						eventDetails
								.setDateOfMonth(calendar.get(Calendar.DATE));
						eventDetails.setEveryMonth(1);
						Calendar tempCalendar = Calendar.getInstance();
						tempCalendar.setTime(date);
						tempCalendar.set(Calendar.DATE, 1);
						if (calendar.get(Calendar.DAY_OF_WEEK) < tempCalendar
								.get(Calendar.DAY_OF_WEEK)) {
							eventDetails.setDayNumber((calendar
									.get(Calendar.WEEK_OF_MONTH)) - 1);
						} else {
							eventDetails.setDayNumber(calendar
									.get(Calendar.WEEK_OF_MONTH));
						}
						day = calendar.get(Calendar.DAY_OF_WEEK);
						eventDetails.setEveryWeekDayMonth(new String[] { day
								.toString() });
						eventDetails.setEveryDayMonth(1);
						eventDetails.setDayOfMonth(day.toString());
					} else if ("Weekly".equalsIgnoreCase(eventDetails
							.getRecurrencePatternName())) {
						eventDetails.setEveryWeek(eventDetails
								.getRecurrenceInterval());
						String[] tempDays = eventDetails.getDays();
						eventDetails.setHiddenDays(tempDays[0]);

						// Daily Recurrence
						eventDetails.setIsOngoingDaily("days");
						eventDetails.setEveryWeekDay(1);

						// Monthly Recurrence
						Date date = new Date(eventDetails.getEventStartDate());
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(date);
						Integer day = calendar.get(Calendar.DAY_OF_WEEK);
						eventDetails.setIsOngoingMonthly("date");
						eventDetails
								.setDateOfMonth(calendar.get(Calendar.DATE));
						eventDetails.setEveryMonth(1);
						Calendar tempCalendar = Calendar.getInstance();
						tempCalendar.setTime(date);
						tempCalendar.set(Calendar.DATE, 1);
						if (calendar.get(Calendar.DAY_OF_WEEK) < tempCalendar
								.get(Calendar.DAY_OF_WEEK)) {
							eventDetails.setDayNumber((calendar
									.get(Calendar.WEEK_OF_MONTH)) - 1);
						} else {
							eventDetails.setDayNumber(calendar
									.get(Calendar.WEEK_OF_MONTH));
						}
						day = calendar.get(Calendar.DAY_OF_WEEK);
						eventDetails.setEveryWeekDayMonth(new String[] { day
								.toString() });
						eventDetails.setEveryDayMonth(1);
						eventDetails.setDayOfMonth(day.toString());
					} else if ("Monthly".equalsIgnoreCase(eventDetails
							.getRecurrencePatternName())) {
						Date date = new Date(eventDetails.getEventStartDate());
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(date);
						Integer day = calendar.get(Calendar.DAY_OF_WEEK);

						if (eventDetails.getByDayNumber() == true) {
							eventDetails.setIsOngoingMonthly("date");
							eventDetails.setDateOfMonth(eventDetails
									.getDayNumber());
							eventDetails.setEveryMonth(eventDetails
									.getRecurrenceInterval());

							Calendar tempCalendar = Calendar.getInstance();
							tempCalendar.setTime(date);
							tempCalendar.set(Calendar.DATE, 1);
							if (calendar.get(Calendar.DAY_OF_WEEK) < tempCalendar
									.get(Calendar.DAY_OF_WEEK)) {
								eventDetails.setDayNumber((calendar
										.get(Calendar.WEEK_OF_MONTH)) - 1);
							} else {
								eventDetails.setDayNumber(calendar
										.get(Calendar.WEEK_OF_MONTH));
							}
							day = calendar.get(Calendar.DAY_OF_WEEK);
							eventDetails
									.setEveryWeekDayMonth(new String[] { day
											.toString() });
							eventDetails.setEveryDayMonth(1);
						} else {
							eventDetails.setIsOngoingMonthly("day");
							day = calendar.get(Calendar.DAY_OF_WEEK);
							String[] tempDays = eventDetails.getDays();
							eventDetails.setHiddenDays(tempDays[0]);
							eventDetails.setEveryWeekDayMonth(tempDays);
							eventDetails.setEveryDayMonth(eventDetails
									.getRecurrenceInterval());

							eventDetails.setDateOfMonth(calendar
									.get(Calendar.DATE));
							eventDetails.setEveryMonth(1);
						}
						// Weekly Recurrence
						eventDetails.setEveryWeek(1);
						eventDetails.setDays(new String[] { day.toString() });
						// Daily Recurrence
						eventDetails.setIsOngoingDaily("days");
						eventDetails.setEveryWeekDay(1);
					}

					if (null == eventDetails.getEventEndDate()
							&& null == eventDetails.getEndAfter()) {
						Date date = new Date();
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(date);
						Format formatter = new SimpleDateFormat("MM/dd/yyyy");
						eventDetails.setOccurenceType("noEndDate");
						eventDetails.setEndAfter(1);
						eventDetails.setEventEndDate(formatter.format(date));
					} else if (null != eventDetails.getEventEndDate()
							&& null != eventDetails.getEndAfter()) {
						eventDetails.setOccurenceType("endAfter");
					} else {
						eventDetails.setOccurenceType("endBy");
						eventDetails.setEndAfter(1);
					}

					if (null != eventDetails.getEventStartTime()
							&& !"".equals(eventDetails.getEventStartTime())) {
						String eventTime = eventDetails.getEventStartTime();
						String[] tempTime = eventTime.split(":");
						eventDetails.setEventStartTimeHrs(tempTime[0]);
						eventDetails.setEventStartTimeMins(tempTime[1]);
					}
					if (null != eventDetails.getEventEndTime()
							&& !"".equals(eventDetails.getEventEndTime())) {
						String eventTime = eventDetails.getEventEndTime();
						String[] tempTime = eventTime.split(":");
						eventDetails.setEventEndTimeHrs(tempTime[0]);
						eventDetails.setEventEndTimeMins(tempTime[1]);
					}

				}

				if (null == eventDetails.getBsnsLoc()
						|| "0".equals(eventDetails.getBsnsLoc())) {
					eventDetails.setBsnsLoc("no");
				} else {
					eventDetails.setBsnsLoc("yes");

					if (!"".equals(Utility.checkNull(eventDetails
							.getAppsiteIDs()))) {
						eventDetails.setAppsiteID(eventDetails.getAppsiteIDs()
								.split(","));

					}

				}

				if (null == eventDetails.getEvntHotel()
						|| "0".equals(eventDetails.getEvntHotel())) {
					eventDetails.setEvntHotel("no");
				} else {
					eventDetails.setEvntHotel("yes");

					if (!"".equals(Utility.checkNull(eventDetails
							.getRetailLocationIDs()))) {
						eventDetails.setRetailLocationID(eventDetails
								.getRetailLocationIDs().split(","));

					}

				}

				if (null == eventDetails.getEvntPckg()
						|| "0".equals(eventDetails.getEvntPckg())) {
					eventDetails.setEvntPckg("no");
				} else {
					eventDetails.setEvntPckg("yes");
				}

				if (null == eventDetails.getIsEventLogistics()
						|| "0".equals(eventDetails.getIsEventLogistics())) {
					eventDetails.setIsEventLogistics("no");
				} else {
					eventDetails.setIsEventLogistics("yes");
				}
			}

		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		} catch (ParseException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return eventDetails;
	}

	public List<RetailLocation> getEventHotelList(Integer eventId)
			throws HubCitiServiceException {
		final String methodName = "getHotelList";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<RetailLocation> getEventHotelList = null;

		try {
			getEventHotelList = hubCitiDAO.getEventHotelList(eventId);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return getEventHotelList;
	}

	/**
	 * This Method will display filters created by the user.
	 * 
	 * @param filters
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	public FiltersDetails displayFilters(ScreenSettings filters, User user)
			throws HubCitiServiceException {
		final String methodName = "displayFilters";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		FiltersDetails filtersDetails = null;

		try {
			filtersDetails = hubCitiDAO.displayFilters(filters, user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName,
					e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return filtersDetails;
	}

	/**
	 * This method will save filter details
	 * 
	 * @param filters
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String saveFilters(Filters filters, User user)
			throws HubCitiServiceException {
		final String methodName = "saveFilters";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String response = null;
		final String fileSeparator = System.getProperty("file.separator");
		try {
			final StringBuilder mediaPathBuilder = Utility.getMediaPath(
					ApplicationConstants.HUBCITI, user.getHubCitiID());
			final StringBuilder mediaTempPathBuilder = Utility
					.getTempMediaPath(ApplicationConstants.TEMP);
			final String hubCitiMediaPath = mediaPathBuilder.toString();
			final String tempMediaPath = mediaTempPathBuilder.toString();

			if (null != filters.getLogoImageName()
					&& !"".equals(filters.getLogoImageName())) {
				final InputStream inputStream = new BufferedInputStream(
						new FileInputStream(tempMediaPath + fileSeparator
								+ filters.getLogoImageName()));
				if (null != inputStream) {
					Utility.writeFileData(inputStream, hubCitiMediaPath
							+ fileSeparator + filters.getLogoImageName());

				}
			}
			response = hubCitiDAO.saveFilters(filters, user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName,
					e.getStackTrace());
			throw new HubCitiServiceException(e);
		} catch (FileNotFoundException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return response;
	}

	/**
	 * This method will return filter details.
	 * 
	 * @param hubCitiId
	 * @param filterId
	 * @return
	 * @throws HubCitiServiceException
	 */
	public ScreenSettings fetchFilterDetails(Integer hubCitiId, Integer filterId)
			throws HubCitiServiceException {
		final String methodName = "fetchFilterDetails";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		ScreenSettings settings = null;

		try {
			settings = hubCitiDAO.fetchFilterDetails(hubCitiId, filterId);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName,
					e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return settings;

	}

	/**
	 * This method will de-associates the filter associated location.
	 * 
	 * @param filterID
	 * @param retailLocIDs
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String deAssociateFilterRetailLocs(Integer filterID,
			String retailLocIDs) throws HubCitiServiceException {
		final String methodName = "deAssociateFilterRetailLocs";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String response = null;
		try {
			response = hubCitiDAO.deAssociateFilterRetailLocs(filterID,
					retailLocIDs);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName,
					e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return response;
	}

	/**
	 * This method will delete the filter.
	 * 
	 * @param filterID
	 * @param hubCitiID
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String deleteFilter(Integer filterID, Integer hubCitiID)
			throws HubCitiServiceException {
		final String methodName = "deleteFilter";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String response = null;
		try {
			response = hubCitiDAO.deleteFilter(filterID, hubCitiID);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName,
					e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return response;
	}

	/**
	 * This method is to fetch the user settings details.
	 * 
	 * @param hubCitiID
	 * @return userSettings details
	 * @throws HubCitiServiceException
	 */
	public final ScreenSettings fetchUserSettings(Integer hubCitiID)
			throws HubCitiServiceException {
		final String methodName = "fetchUserSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		ScreenSettings userSettings = null;
		try {
			userSettings = hubCitiDAO.fetchUserSettings(hubCitiID);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName,
					e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return userSettings;
	}

	/**
	 * This method is to save the user settings details.
	 * 
	 * @param userSettings
	 *            details
	 * @param user
	 *            details
	 * @return String(success or failure)
	 * @throws HubCitiServiceException
	 */
	public final String saveUserSettings(ScreenSettings userSettings, User user)
			throws HubCitiServiceException {
		final String methodName = "saveUserSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String response = null;
		final String fileSeparator = System.getProperty("file.separator");
		try {
			if (userSettings.getUserSettingsFields().contains("optn-dntn")) {
				final StringBuilder mediaPathBuilder = Utility.getMediaPath(
						ApplicationConstants.HUBCITI, user.getHubCitiID());
				final StringBuilder mediaTempPathBuilder = Utility
						.getTempMediaPath(ApplicationConstants.TEMP);
				final String hubCitiMediaPath = mediaPathBuilder.toString();
				final String tempMediaPath = mediaTempPathBuilder.toString();

				if (null != userSettings.getLogoImageName()
						&& !"".equals(userSettings.getLogoImageName())
						&& null != userSettings.getOldImageName()
						&& !userSettings.getOldImageName().equals(
								userSettings.getLogoImageName())) {
					final InputStream inputStream = new BufferedInputStream(
							new FileInputStream(tempMediaPath + fileSeparator
									+ userSettings.getLogoImageName()));
					if (null != inputStream) {
						Utility.writeFileData(
								inputStream,
								hubCitiMediaPath + fileSeparator
										+ userSettings.getLogoImageName());

					}
				}
			} else {
				userSettings.setLogoImageName(null);
			}

			response = hubCitiDAO.saveUserSettings(userSettings, user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName,
					e.getStackTrace());
			throw new HubCitiServiceException(e);
		} catch (FileNotFoundException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName,
					e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return response;
	}

	public List<CityExperience> getStatelst(int iHubCitiId)
			throws HubCitiServiceException {
		final String methodName = "getStatelst";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		List<CityExperience> stateLst = null;
		try {
			stateLst = hubCitiDAO.getStatelst(iHubCitiId);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED, e);
			throw new HubCitiServiceException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return stateLst;
	}

	public List<CityExperience> getCitilst(int iHubCitiId, String strState)
			throws HubCitiServiceException {
		final String methodName = "getCitilst";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		List<CityExperience> citiLst = null;
		try {
			citiLst = hubCitiDAO.getCitilst(iHubCitiId, strState);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED, e);
			throw new HubCitiServiceException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return citiLst;
	}

	public List<CityExperience> getZipcodelst(CityExperience cityExperience)
			throws HubCitiServiceException {
		final String methodName = "getZipcodelst";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		List<CityExperience> zipcodeLst = null;
		try {
			zipcodeLst = hubCitiDAO.getZipcodelst(cityExperience);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED, e);
			throw new HubCitiServiceException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return zipcodeLst;
	}

	public CityExperienceDetail getRetailer(CityExperience cityExperience)
			throws HubCitiServiceException {
		final String methodName = "getRetailer";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		CityExperienceDetail objCityExperienceDetail = null;
		try {

			objCityExperienceDetail = hubCitiDAO.getRetailer(cityExperience);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED, e);
			throw new HubCitiServiceException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return objCityExperienceDetail;
	}

	public String deAssociateRetailer(CityExperience cityExperience)
			throws HubCitiServiceException {
		final String methodName = "deAssociateRetailer";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String strResponse = null;
		try {

			strResponse = hubCitiDAO.deAssociateRetailer(cityExperience);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED, e);
			throw new HubCitiServiceException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	public String associateRetailer(CityExperience cityExperience)
			throws HubCitiServiceException {
		final String methodName = "associateRetailer";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String strResponse = null;
		try {

			strResponse = hubCitiDAO.associateRetailer(cityExperience);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED, e);
			throw new HubCitiServiceException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	/**
	 * This method will return list of event patterns.
	 * 
	 * @return
	 * @throws HubCitiServiceException
	 */
	public List<Event> getEventPatterns() throws HubCitiServiceException {
		final String methodName = "getEventPatterns";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<Event> eventList = null;

		try {
			eventList = hubCitiDAO.getEventPatterns();
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return eventList;
	}

	/**
	 * This method will return list of user created FAQ's.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiServiceException
	 */
	public FAQDetails fetchFQAs(FAQ faq) throws HubCitiServiceException {
		final String methodName = "fetchFQAs";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		FAQDetails faqDetails = null;

		try {
			faqDetails = hubCitiDAO.fetchFQAs(faq);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return faqDetails;
	}

	/**
	 * This method will return list of user created FAQ Categories.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiServiceException
	 */
	public FAQDetails fetchFAQCategories(FAQ faq)
			throws HubCitiServiceException {
		final String methodName = "fetchFAQCategories";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		FAQDetails faqCategories = null;

		try {
			faqCategories = hubCitiDAO.fetchFAQCategories(faq);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return faqCategories;
	}

	/**
	 * This method saves the FAQ Details.
	 * 
	 * @param faq
	 * @return String
	 * @throws HubCitiServiceException
	 */
	public String saveFAQs(FAQ faq) throws HubCitiServiceException {
		final String methodName = "saveFAQs";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;

		try {
			status = hubCitiDAO.saveFAQs(faq);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	/**
	 * This method will return FAQ Details.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiServiceException
	 */
	public FAQ fetchFAQDetails(FAQ faq) throws HubCitiServiceException {
		final String methodName = "fetchFAQCategories";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		FAQ faqDetails = null;

		try {
			faqDetails = hubCitiDAO.fetchFAQDetails(faq);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return faqDetails;
	}

	/**
	 * This method will delete FAQ Details.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String deleteFAQ(FAQ faq) throws HubCitiServiceException {
		final String methodName = "deleteFAQ";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String response = null;

		try {
			response = hubCitiDAO.deleteFAQ(faq);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return response;
	}

	/**
	 * This method will save FAQ Category Details.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String addUpdateFAQCategory(FAQ faq) throws HubCitiServiceException {
		final String strMethodName = "addFAQCategory";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		String strResponse = null;

		try {
			strResponse = hubCitiDAO.addUpdateFAQCategory(faq);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + strMethodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strResponse;

	}

	/**
	 * This method will delete FAQ Category Details.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String deleteFAQCategory(FAQ faq) throws HubCitiServiceException {
		final String methodName = "deleteFAQCategory";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String response = null;

		try {
			response = hubCitiDAO.deleteFAQCategory(faq);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return response;
	}

	public List<ScreenSettings> getMenuButtonType()
			throws HubCitiServiceException {
		final String methodName = "getMenuButtonType";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<ScreenSettings> btnType = null;

		try {
			btnType = hubCitiDAO.getMenuButtonType();
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return btnType;
	}

	public String deleteSubMenu(ScreenSettings screenSettings,
			Integer iHubCityId) throws HubCitiServiceException {
		final String methodName = "deleteSubMenu";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String response = null;

		try {
			response = hubCitiDAO.deleteSubMenu(screenSettings, iHubCityId);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return response;
	}

	public String saveFaqCateReorder(FAQ faq) throws HubCitiServiceException {
		final String methodName = "saveFaqCateReorder";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String strResponse = null;

		try {
			strResponse = hubCitiDAO.saveFaqCateReorder(faq);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	public String saveFaqReorder(FAQ faq) throws HubCitiServiceException {
		final String methodName = "saveFaqReorder";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String strResponse = null;

		try {
			strResponse = hubCitiDAO.saveFaqReorder(faq);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	public String saveFilterOrder(int hcHubCitiID, String hcFilterID,
			int hcCityExoerienceID, String sortOrder, int userID)
			throws HubCitiServiceException {
		final String methodName = "saveFilterOrder";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String response = null;

		try {
			response = hubCitiDAO.insertFilterOrder(hcHubCitiID, hcFilterID,
					hcCityExoerienceID, sortOrder, userID);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return response;
	}

	public List<TabBarDetails> displayModuleTabBars(Integer userId,
			Integer hubCitiId) throws HubCitiServiceException {

		final String methodName = "displayModuleTabBars";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<ScreenSettings> response = null;
		List<TabBarDetails> tabBarList = null;
		List<ScreenSettings> settings = null;
		String moduleName = null;
		Boolean moduleExist = false;
		TabBarDetails tabBarDetails = null;

		try {
			response = hubCitiDAO.displayModuleTabBars(userId, hubCitiId);

			if (null != response && response.size() > 0) {

				for (ScreenSettings screenSettings : response) {
					moduleName = screenSettings.getFunctionalityType();

					if (null != tabBarList && tabBarList.size() > 0) {
						moduleExist = false;
						for (TabBarDetails barDetails : tabBarList) {
							if (moduleName.equalsIgnoreCase(barDetails
									.getFunctionalityName())) {
								moduleExist = true;
							}
						}
					} else {
						tabBarList = new ArrayList<TabBarDetails>();
					}

					if (!moduleExist) {
						if (tabBarList.size() > 0) {
							tabBarDetails.setTabBarList(settings);
						}
						tabBarDetails = new TabBarDetails();
						tabBarDetails.setFunctionalityName(moduleName);
						tabBarDetails.setFunctionalityId(screenSettings
								.getFunctionalityId());
						tabBarList.add(tabBarDetails);
						settings = new ArrayList<ScreenSettings>();
						settings.add(screenSettings);
					} else {
						settings.add(screenSettings);
					}
				}
				tabBarDetails.setTabBarList(settings);
			}
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return tabBarList;
	}

	public List<ScreenSettings> displayModules(Integer userId, Integer hubCitiId)
			throws HubCitiServiceException {

		final String methodName = "displayModules";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<ScreenSettings> moduleList = null;

		try {

			moduleList = hubCitiDAO.displayModules(userId, hubCitiId);

		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return moduleList;
	}

	/**
	 * This method will save module tab bar details.
	 * 
	 * @param screenSettings
	 * @param objUser
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String saveModuleTabBar(ScreenSettings screenSettings, User objUser)
			throws HubCitiServiceException {

		final String methodName = "saveModuleTabBar";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;

		try {

			status = hubCitiDAO.saveModuleTabBar(screenSettings, objUser);

		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	/**
	 * This method will delete module tab bar details.
	 * 
	 * @param screenSettings
	 * @param objUser
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String deleteModuleTabBar(ScreenSettings screenSettings, User objUser)
			throws HubCitiServiceException {

		final String methodName = "saveModuleTabBar";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;

		try {

			status = hubCitiDAO.deleteModuleTabBar(screenSettings, objUser);

		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	public UserDetails displayHubCitiCreatedUsers(User user)
			throws HubCitiServiceException {

		final String methodName = "displayHubCitiCreatedUsers";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		UserDetails usersLst = null;

		try {

			usersLst = hubCitiDAO.displayHubCitiCreatedUsers(user);

		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return usersLst;

	}

	public List<Module> displayUserModules(Integer hubCitiID, Integer roleUserId)
			throws HubCitiServiceException {

		final String methodName = "displayUserModules";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<Module> moduleList = null;

		try {

			moduleList = hubCitiDAO.displayUserModules(hubCitiID, roleUserId);

		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return moduleList;
	}

	public String createUserDeatils(User user, User loginUser)
			throws HubCitiServiceException {

		final String methodName = "createUserDeatils";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;
		String enryptPassword = null;
		String autogenPassword = null;
		String smtpHost = null;
		String smtpPort = null;
		String strAdminEmailId = null;
		String strResponse = null;

		try {

			autogenPassword = Utility.randomString(5);
			user.setPassword(autogenPassword);
			PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
			enryptPassword = passwordEncoder.encodePassword(autogenPassword,
					user.getUserName());
			user.setEncrptedPassword(enryptPassword);
			status = hubCitiDAO.saveUpdateUserDeatils(user);

			if (!status.equals(ApplicationConstants.DUPLICATEUSERNAME)
					&& !status.equals(ApplicationConstants.DUPLICATEEMAIL)) {
				final List<AppConfiguration> emailConf = hubCitiDAO
						.getAppConfig(ApplicationConstants.EMAILCONFIG);
				for (int j = 0; j < emailConf.size(); j++) {
					if (emailConf.get(j).getScreenName()
							.equals(ApplicationConstants.SMTPHOST)) {
						smtpHost = emailConf.get(j).getScreenContent();
					}
					if (emailConf.get(j).getScreenName()
							.equals(ApplicationConstants.SMTPPORT)) {
						smtpPort = emailConf.get(j).getScreenContent();
					}
				}
				final List<AppConfiguration> adminEmailList = hubCitiDAO
						.getAppConfig(ApplicationConstants.WEBREGISTRATION);
				for (int j = 0; j < adminEmailList.size(); j++) {
					if (adminEmailList.get(j).getScreenName()
							.equals(ApplicationConstants.ADMINEMAILID)) {
						strAdminEmailId = adminEmailList.get(j)
								.getScreenContent();
					}
				}
				final List<AppConfiguration> list = hubCitiDAO
						.getAppConfig(ApplicationConstants.HUBCITICONFG);
				for (int j = 0; j < list.size(); j++) {
					if (list.get(j).getScreenName()
							.equals(ApplicationConstants.SCANSEEBASEURL)) {

						user.setHubCitiUrl(list.get(j).getScreenContent());

					}
				}
				strResponse = Utility.sendMailHubCitiLoginSuccess(user,
						loginUser, smtpHost, smtpPort, strAdminEmailId,
						ApplicationConstants.SCANSEE_LOGO_FOR_MAILSENDING);
				if (strResponse != null
						&& strResponse.equals(ApplicationConstants.SUCCESS)) {
					status = ApplicationConstants.SUCCESS;
				}
			}

		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;

	}

	public String updateUserDeatils(User user, User loginUser)
			throws HubCitiServiceException {

		final String methodName = "updateUserDeatils";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;
		String enryptPassword = null;
		String autogenPassword = null;
		String smtpHost = null;
		String smtpPort = null;
		String strAdminEmailId = null;
		String strResponse = null;

		try {

			if (!user.getEmailId().equalsIgnoreCase(user.getPreviousEmailId())) {
				autogenPassword = Utility.randomString(5);
				user.setPassword(autogenPassword);
				PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
				enryptPassword = passwordEncoder.encodePassword(
						autogenPassword, user.getUserName());
				user.setEncrptedPassword(enryptPassword);
			}

			status = hubCitiDAO.saveUpdateUserDeatils(user);

			if (!user.getEmailId().equalsIgnoreCase(user.getPreviousEmailId())
					&& !status.equals(ApplicationConstants.DUPLICATEUSERNAME)
					&& !status.equals(ApplicationConstants.DUPLICATEEMAIL)) {
				final List<AppConfiguration> emailConf = hubCitiDAO
						.getAppConfig(ApplicationConstants.EMAILCONFIG);
				for (int j = 0; j < emailConf.size(); j++) {
					if (emailConf.get(j).getScreenName()
							.equals(ApplicationConstants.SMTPHOST)) {
						smtpHost = emailConf.get(j).getScreenContent();
					}
					if (emailConf.get(j).getScreenName()
							.equals(ApplicationConstants.SMTPPORT)) {
						smtpPort = emailConf.get(j).getScreenContent();
					}
				}
				final List<AppConfiguration> adminEmailList = hubCitiDAO
						.getAppConfig(ApplicationConstants.WEBREGISTRATION);
				for (int j = 0; j < adminEmailList.size(); j++) {
					if (adminEmailList.get(j).getScreenName()
							.equals(ApplicationConstants.ADMINEMAILID)) {
						strAdminEmailId = adminEmailList.get(j)
								.getScreenContent();
					}
				}
				final List<AppConfiguration> list = hubCitiDAO
						.getAppConfig(ApplicationConstants.HUBCITICONFG);
				for (int j = 0; j < list.size(); j++) {
					if (list.get(j).getScreenName()
							.equals(ApplicationConstants.SCANSEEBASEURL)) {

						user.setHubCitiUrl(list.get(j).getScreenContent());

					}
				}
				strResponse = Utility.sendMailHubCitiLoginSuccess(user,
						loginUser, smtpHost, smtpPort, strAdminEmailId,
						ApplicationConstants.SCANSEE_LOGO_FOR_MAILSENDING);
				if (strResponse != null
						&& strResponse.equals(ApplicationConstants.SUCCESS)) {
					status = ApplicationConstants.SUCCESS;
				}
			}

		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	public User fetchUserDetails(Integer userId) throws HubCitiServiceException {

		final String methodName = "fetchUserDetails";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		User user = null;

		try {

			user = hubCitiDAO.fetchUserDetails(userId);

		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);

		return user;
	}

	/**
	 * This method will activate or de-activate requested user.
	 * 
	 * @param userId
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String activateDeactivateUsers(Integer userId)
			throws HubCitiServiceException {

		final String methodName = "activateDeactivateUsers";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;

		try {

			status = hubCitiDAO.activateDeactivateUsers(userId);

		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	public List<CityExperience> displayCitiesForRegionApp(User user)
			throws HubCitiServiceException {
		final String methodName = "displayCitiesForRegionApp";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		List<CityExperience> ObjCities = null;
		try {
			ObjCities = hubCitiDAO.displayCitiesForRegionApp(user);

		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return ObjCities;
	}

	public AlertCategory fetchFundraiserEventCategories(User user)
			throws HubCitiServiceException {
		final String methodName = "fetchEventCategories";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		AlertCategory objCategory = null;

		try {
			objCategory = hubCitiDAO.fetchFundraiserEventCategories(user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return objCategory;
	}

	public List<Department> fetchFundraiserDepartments(User objUser)
			throws HubCitiServiceException {

		final String methodName = "fetchFundraiserDepartments";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<Department> departmentList = null;

		try {
			departmentList = hubCitiDAO.fetchFundraiserDepartments(objUser);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return departmentList;
	}

	public String saveUpdateFundraiserEventDeatils(Event eventDetails, User user)
			throws HubCitiServiceException {

		final String methodName = "saveUpdateFundraiserEventDeatils";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String response = null;
		InputStream inputStream = null;
		final String fileSeparator = System.getProperty("file.separator");

		try {

			final StringBuilder mediaPathBuilder = Utility.getMediaPath(
					ApplicationConstants.HUBCITI, user.getHubCitiID());
			final StringBuilder mediaTempPathBuilder = Utility
					.getTempMediaPath(ApplicationConstants.TEMP);
			final String hubCitiMediaPath = mediaPathBuilder.toString();
			final String tempMediaPath = mediaTempPathBuilder.toString();

			if (null != eventDetails.getEventImageName()
					&& !"".equals(eventDetails.getEventImageName())) {
				inputStream = new BufferedInputStream(new FileInputStream(
						tempMediaPath + fileSeparator
								+ eventDetails.getEventImageName()));
				if (null != inputStream) {
					Utility.writeFileData(inputStream, hubCitiMediaPath
							+ fileSeparator + eventDetails.getEventImageName());
				}
			}

			response = hubCitiDAO.saveUpdateFundraiserEventDeatils(
					eventDetails, user);

		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		} catch (FileNotFoundException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		return response;
	}

	public String addFundraiserDept(String catName, User objUser)
			throws HubCitiServiceException {

		final String strMethodName = "addFundraiserDept";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);

		String strResponse = null;

		try {
			strResponse = hubCitiDAO.addFundraiserDept(catName, objUser);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + strMethodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + strMethodName);
		return strResponse;
	}

	/**
	 * This method will return Fundraiser Details.
	 * 
	 * @param event
	 * @return
	 * @throws HubCitiServiceException
	 */
	public Event fetchFundraiserDetails(Integer eventId)
			throws HubCitiServiceException {
		final String methodName = "fetchFundraiserDetails";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Event fundraiserDetails = null;

		try {
			fundraiserDetails = hubCitiDAO.fetchFundraiserDetails(eventId);
			if (null != fundraiserDetails.getEventDate()) {
				fundraiserDetails.setEventDate(Utility
						.formattedDate(fundraiserDetails.getEventDate()));
			}
			if (null != fundraiserDetails.getEventEDate()) {
				fundraiserDetails.setEventEDate(Utility
						.formattedDate(fundraiserDetails.getEventEDate()));
			}
			if ("1".equalsIgnoreCase(fundraiserDetails.getIsEventAppsite())) {
				fundraiserDetails.setIsEventAppsite("Yes");
			} else {
				fundraiserDetails.setIsEventAppsite("No");
			}
			if ("1".equalsIgnoreCase(fundraiserDetails.getIsEventTied())) {
				fundraiserDetails.setIsEventTied("Yes");
			} else {
				fundraiserDetails.setIsEventTied("No");
			}
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		} catch (ParseException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return fundraiserDetails;
	}

	public AlertCategory fetchFundraiserCategories(Category category, User user)
			throws HubCitiServiceException {
		final String methodName = "fetchFundraiserCategories";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		AlertCategory objCategory = null;

		try {
			objCategory = hubCitiDAO.fetchFundraiserCategories(category, user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return objCategory;
	}

	public DealDetails fetchDeals(Deals deals, User user)
			throws HubCitiServiceException {
		final String methodName = "fetchDeals";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		DealDetails dealDetails = null;
		List<Deals> dealLst = null;

		try {
			dealDetails = hubCitiDAO.fetchDeals(deals, user);

			if (null != dealDetails) {
				dealLst = dealDetails.getDeals();
				if (null != dealLst && !"".equals(dealLst)) {
					for (Deals deals2 : dealLst) {
						if (null != deals2.getStartDate()
								&& !"".equals(deals2.getStartDate())) {
							deals2.setStartDate(Utility.formattedDate(deals2
									.getStartDate()));
						}
						if (null != deals2.getEndDate()
								&& !"".equals(deals2.getEndDate())) {
							deals2.setEndDate(Utility.formattedDate(deals2
									.getEndDate()));
						}
					}
				}
			}

		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		} catch (ParseException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return dealDetails;
	}

	public String saveDealOfTheDay(Deals deals, User user)
			throws HubCitiServiceException {
		final String methodName = "fetchDeals";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String response = null;

		try {
			response = hubCitiDAO.saveDealOfTheDay(deals, user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return response;
	}

	/**
	 * This method is used to save event marker details.
	 */
	public String saveEvtMarkerInfo(Event eventDetails, User user)
			throws HubCitiServiceException {
		final String methodName = "saveEvtMarkerInfo";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		BufferedInputStream bufferedInputStream = null;
		String response = null;
		final String fileSeparator = System.getProperty("file.separator");
		try {

			/*
			 * final String eventMediaPath =
			 * ApplicationConstants.HUBCITI+fileSeparator+
			 * user.getHubCitiID()+fileSeparator+ApplicationConstants.EVENTS ;
			 * final StringBuilder mediaPathBuilder =
			 * Utility.getEvtMarkerMediaPath(eventMediaPath,
			 * eventDetails.getHcEventID()); final StringBuilder
			 * mediaTempPathBuilder =
			 * Utility.getTempMediaPath(ApplicationConstants.TEMP);
			 * 
			 * final String tempMediaPath = mediaTempPathBuilder.toString();
			 * 
			 * final String hubCitiMediaPath = mediaPathBuilder.toString() ;
			 * 
			 * if (null != eventDetails.getEventImageName() &&
			 * !"".equals(eventDetails.getEventImageName())) { inputStream = new
			 * BufferedInputStream(new FileInputStream(tempMediaPath +
			 * fileSeparator + eventDetails.getEventImageName())); if (null !=
			 * inputStream) { Utility.writeFileData(inputStream,
			 * hubCitiMediaPath + fileSeparator +
			 * eventDetails.getEventImageName()); } }
			 */

			final String eventMediaPath = ApplicationConstants.HUBCITI
					+ fileSeparator + user.getHubCitiID() + fileSeparator
					+ ApplicationConstants.EVENTS;
			final StringBuilder mediaPathBuilder = Utility
					.getEvtMarkerMediaPath(eventMediaPath,
							eventDetails.getHcEventID());
			final String hubCitiMediaPath = mediaPathBuilder.toString();

			MultipartFile file = eventDetails.getEventImageFile();

			InputStream inputStream = file.getInputStream();

			String fileName = file.getOriginalFilename();
			fileName = Utility.getImagewithDateTime(fileName);
			eventDetails.setEventImageName(fileName);

			bufferedInputStream = new BufferedInputStream(inputStream);
			Utility.writeFileData(inputStream, hubCitiMediaPath + fileSeparator
					+ fileName);

			response = hubCitiDAO.saveEvtMarkerInfo(eventDetails, user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		} catch (FileNotFoundException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		} catch (IOException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return response;
	}

	/**
	 * This method is used to fetch event marker details.
	 */
	public ArrayList<Event> getEvtMarkerInfo(Event event, User user)
			throws HubCitiServiceException {
		final String methodName = "getEvtMarkerInfo";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		ArrayList<Event> evtMarkerlst = null;

		try {
			evtMarkerlst = hubCitiDAO.getEvtMarkerInfo(event, user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return evtMarkerlst;
	}

	/**
	 * This method is used to delete event marker details.
	 */

	public String deleteEvtMarker(Event event) throws HubCitiServiceException {
		final String methodName = "deleteEvtMarker";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String strResponse = null;
		try {
			strResponse = hubCitiDAO.deleteEvtMarker(event);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	/*
	 * public List<Category> getCategoryImageDetails(Integer userId, Integer
	 * hubCitiId) throws HubCitiServiceException {
	 * LOG.info(ApplicationConstants.METHODSTART + " getCategoryImageDetails");
	 * List<Category> findCatList = null; try { findCatList =
	 * hubCitiDAO.getCategoryImageDetails(userId, hubCitiId); }
	 * catch(HubCitiWebSqlException e) {
	 * LOG.error(ApplicationConstants.EXCEPTIONOCCURRED +
	 * " getCategoryImageDetails", e); throw new HubCitiServiceException(e); }
	 * LOG.info(ApplicationConstants.METHODEND + " getCategoryImageDetails");
	 * return findCatList; }
	 */

	/**
	 * This will save new Category image.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	/*
	 * public String updateCategoryImage(ScreenSettings screenSettings, User
	 * user) throws HubCitiServiceException { final String methodName =
	 * "saveGeneralSettings"; LOG.info(ApplicationConstants.METHODSTART +
	 * methodName); final String daoStatus; final String fileSeparator =
	 * System.getProperty("file.separator"); try { final StringBuilder
	 * mediaPathBuilder = Utility.getMediaPath(ApplicationConstants.HUBCITI,
	 * user.getHubCitiID()); final StringBuilder mediaTempPathBuilder =
	 * Utility.getTempMediaPath(ApplicationConstants.TEMP); final String
	 * hubCitiMediaPath = mediaPathBuilder.toString(); final String
	 * tempMediaPath = mediaTempPathBuilder.toString();
	 * 
	 * if (null != screenSettings.getCatImgName() &&
	 * !"".equals(screenSettings.getCatImgName()) && null !=
	 * screenSettings.getOldImageName() &&
	 * !screenSettings.getOldImageName().equals(screenSettings.getCatImgName()))
	 * { final InputStream inputStream = new BufferedInputStream(new
	 * FileInputStream(tempMediaPath + fileSeparator +
	 * screenSettings.getCatImgName())); if (null != inputStream) {
	 * Utility.writeFileData(inputStream, hubCitiMediaPath + fileSeparator +
	 * screenSettings.getCatImgName()); } }
	 * 
	 * daoStatus = hubCitiDAO.updateCategoryImage(screenSettings, user); } catch
	 * (HubCitiWebSqlException e) {
	 * LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e); throw
	 * new HubCitiServiceException(e); } catch (FileNotFoundException e) {
	 * LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e); throw
	 * new HubCitiServiceException(e); } return daoStatus; }
	 */

	public Event getEventLogisticsButtonDetails(Integer hubCitiId,
			Integer eventId, Integer userId) throws HubCitiServiceException {
		LOG.info(ApplicationConstants.METHODSTART
				+ " getEventLogisticsButtonDetails");
		Event objEvent = null;
		try {
			objEvent = hubCitiDAO.getEventLogisticsButtonDetails(hubCitiId,
					eventId, userId);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ " getEventLogisticsButtonDetails", e.getMessage());
			throw new HubCitiServiceException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + " getCategoryImageDetails");
		return objEvent;
	}

	/**
	 * This method is used to fetch specific event marker details.
	 */
	public ArrayList<Event> getMarkerInfo(Event event, User user)
			throws HubCitiServiceException {
		final String methodName = "getMarkerInfo";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		ArrayList<Event> eventLst = null;

		try {
			eventLst = hubCitiDAO.getMarkerInfo(event, user);
		} catch (HubCitiWebSqlException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, e);
			throw new HubCitiServiceException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return eventLst;
	}

	public static void main(String args[]) {
		Date d = new Date();
		String enteredDate = d.toString();
		final Date date = new Date();
		Date parsedUtilDate = null;
		String cDate = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY hh:mm:ss");
			String dateString = sdf.format(date);
			System.out.println("date :" + dateString);

		} catch (Exception exception) {
			LOG.info("Exception in convertDBdate method"
					+ exception.getMessage());
			// return ApplicationConstants.NOTAPPLICABLE;
		}

	}

}
