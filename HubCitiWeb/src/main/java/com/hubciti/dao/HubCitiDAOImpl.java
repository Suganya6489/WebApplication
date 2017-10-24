package com.hubciti.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.exception.HubCitiServiceException;
import com.hubciti.common.exception.HubCitiWebSqlException;
import com.hubciti.common.pojo.AlertCategory;
import com.hubciti.common.pojo.Alerts;
import com.hubciti.common.pojo.AlertsDetails;
import com.hubciti.common.pojo.AnythingPageDetails;
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
import com.hubciti.common.pojo.Event;
import com.hubciti.common.pojo.EventDetail;
import com.hubciti.common.pojo.FAQ;
import com.hubciti.common.pojo.FAQDetails;
import com.hubciti.common.pojo.Filters;
import com.hubciti.common.pojo.FiltersDetails;
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
import com.hubciti.common.pojo.SubCategory;
import com.hubciti.common.pojo.SubMenuDetails;
import com.hubciti.common.pojo.Type;
import com.hubciti.common.pojo.User;
import com.hubciti.common.pojo.UserDetails;
import com.hubciti.common.util.Utility;
import com.hubciti.service.SortFindCategory;

/**
 * This class is a DAOImpl class for HubCiti.
 * 
 * @author dileepa_cc
 */
public class HubCitiDAOImpl implements HubCitiDAO {

	/**
	 * Getting the logger Instance.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(HubCitiDAOImpl.class);
	/**
	 * For JDBC connection.
	 */
	private JdbcTemplate jdbcTemplate;
	/**
	 * Getting the SimpleJdbcTemplate Instance.
	 */
	@SuppressWarnings("unused")
	private SimpleJdbcTemplate simpleJdbcTemplate;
	/**
	 * To call the StoredProcedure.
	 */
	private SimpleJdbcCall simpleJdbcCall;

	/**
	 * To set the dataSource to jdbcTemplate...
	 * 
	 * @param dataSource
	 *            from DataSource
	 */
	public final void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * This will return login screen details.
	 * 
	 * @param loginUser
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public ScreenSettings fetchScreenSettings(User loginUser)
			throws HubCitiWebSqlException {

		final String methodName = "fetchLoginScreenSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Map<String, Object> resultFromProcedure = null;
		Integer errorNum = null;
		Integer responseFromProc = null;
		List<ScreenSettings> screenDetailsList = new ArrayList<ScreenSettings>();
		ScreenSettings screenSettings = null;
		String hubCitiVersion = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall
					.withProcedureName("usp_WebHcConfiguarablePageDetails");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.returningResultSet("screenSettings",
					new BeanPropertyRowMapper<ScreenSettings>(
							ScreenSettings.class));
			final MapSqlParameterSource loginScreenDetailsParam = new MapSqlParameterSource();
			loginScreenDetailsParam.addValue("HCAdminUserID",
					loginUser.gethCAdminUserID());
			loginScreenDetailsParam.addValue("HubCitiID",
					loginUser.getHubCitiID());
			loginScreenDetailsParam.addValue("PageTypeName",
					loginUser.getPageType());

			resultFromProcedure = simpleJdbcCall
					.execute(loginScreenDetailsParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {

				screenDetailsList = (ArrayList<ScreenSettings>) resultFromProcedure
						.get("screenSettings");
				if (null != screenDetailsList && !screenDetailsList.isEmpty()) {
					screenSettings = screenDetailsList.get(0);
				}
				if (ApplicationConstants.ABOUTUSPAGE.equals(loginUser
						.getPageType())) {
					hubCitiVersion = (String) resultFromProcedure
							.get(ApplicationConstants.VERSIONNUMBER);
					if (null == screenSettings) {
						screenSettings = new ScreenSettings();
						screenSettings.setScreenSettingsFlag(true);
					}

					screenSettings.setHubCitiVersion(hubCitiVersion);

				}

			} else {
				errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
						+ "  : errorNumber  : " + errorNum + " errorMessage : "
						+ errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException exception) {
			errorNum = (Integer) resultFromProcedure
					.get(ApplicationConstants.ERRORNUMBER);
			final String errorMsg = (String) resultFromProcedure
					.get(ApplicationConstants.ERRORMESSAGE);
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ "  : errorNumber  : " + errorNum + " errorMessage : "
					+ errorMsg);
			throw new HubCitiWebSqlException(errorMsg);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return screenSettings;
	}

	/**
	 * This will save login screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String insertLoginScreenSettings(ScreenSettings loginScreenDetails,
			User user) throws HubCitiWebSqlException {
		final String methodName = "saveLoginScreenSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String status = null;
		Map<String, Object> resultFromProcedure = null;
		Integer errorNum = null;
		Integer responseFromProc = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.withProcedureName("usp_WebHcAdminSetupLoginPage");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			final MapSqlParameterSource loginScreenDetailsParam = new MapSqlParameterSource();
			loginScreenDetailsParam.addValue("HCAdminUserID",
					user.gethCAdminUserID());
			loginScreenDetailsParam.addValue("HubCitiID", user.getHubCitiID());
			loginScreenDetailsParam.addValue("BackgroundColor",
					loginScreenDetails.getBgColor());
			loginScreenDetailsParam.addValue("FontColor",
					loginScreenDetails.getFontColor());
			loginScreenDetailsParam.addValue("ButtonColor",
					loginScreenDetails.getBtnColor());
			loginScreenDetailsParam.addValue("ButtonFontColor",
					loginScreenDetails.getBtnFontColor());
			loginScreenDetailsParam.addValue("LogoImage",
					loginScreenDetails.getLogoImageName());
			resultFromProcedure = simpleJdbcCall
					.execute(loginScreenDetailsParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {

				status = ApplicationConstants.SUCCESS;
			} else {
				errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
						+ "  : errorNumber  : " + errorNum + " errorMessage : "
						+ errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException exception) {
			errorNum = (Integer) resultFromProcedure
					.get(ApplicationConstants.ERRORNUMBER);
			final String errorMsg = (String) resultFromProcedure
					.get(ApplicationConstants.ERRORMESSAGE);
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ "  : errorNumber  : " + errorNum + " errorMessage : "
					+ errorMsg);
			throw new HubCitiWebSqlException(errorMsg);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	/**
	 * This will save registration screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String insertRegScreenSettings(ScreenSettings loginScreenDetails,
			User user) throws HubCitiWebSqlException {
		final String methodName = "insertRegScreenSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String status = null;
		Map<String, Object> resultFromProcedure = null;
		Integer errorNum = null;
		Integer responseFromProc = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall
					.withProcedureName("usp_WebHcAdminSetupRegistrationScreen");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			final MapSqlParameterSource regScreenDetailsParam = new MapSqlParameterSource();
			regScreenDetailsParam.addValue("HCAdminUserID",
					user.gethCAdminUserID());
			regScreenDetailsParam.addValue("HubCitiID", user.getHubCitiID());
			regScreenDetailsParam.addValue("BackgroundColor",
					loginScreenDetails.getBgColor());
			regScreenDetailsParam.addValue("FontColor",
					loginScreenDetails.getFontColor());
			regScreenDetailsParam.addValue("ButtonColor",
					loginScreenDetails.getBtnColor());
			regScreenDetailsParam.addValue("ButtonFontColor",
					loginScreenDetails.getBtnFontColor());
			regScreenDetailsParam.addValue("Title",
					loginScreenDetails.getPageTitle());
			regScreenDetailsParam.addValue("Content",
					loginScreenDetails.getPageContent());
			resultFromProcedure = simpleJdbcCall.execute(regScreenDetailsParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {

				status = ApplicationConstants.SUCCESS;
			} else {
				errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
						+ "  : errorNumber  : " + errorNum + " errorMessage : "
						+ errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException exception) {
			errorNum = (Integer) resultFromProcedure
					.get(ApplicationConstants.ERRORNUMBER);
			final String errorMsg = (String) resultFromProcedure
					.get(ApplicationConstants.ERRORMESSAGE);
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ "  : errorNumber  : " + errorNum + " errorMessage : "
					+ errorMsg);
			throw new HubCitiWebSqlException(errorMsg);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	/**
	 * This will save About us screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String insertAboutusScreenSettings(ScreenSettings screenSettings,
			User user) throws HubCitiWebSqlException {
		final String methodName = "insertRegScreenSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String status = null;
		Map<String, Object> resultFromProcedure = null;
		Integer errorNum = null;
		Integer responseFromProc = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.withProcedureName("usp_WebHcAdminSetupAboutUsPage");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
			mapSqlParameterSource.addValue("HCAdminUserID",
					user.gethCAdminUserID());
			mapSqlParameterSource.addValue("HubCitiID", user.getHubCitiID());
			// mapSqlParameterSource.addValue("SmallLogo",
			// screenSettings.getSmallLogoImageName());
			mapSqlParameterSource.addValue("Image",
					screenSettings.getLogoImageName());
			mapSqlParameterSource.addValue("Content",
					screenSettings.getPageContent());

			resultFromProcedure = simpleJdbcCall.execute(mapSqlParameterSource);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {

				status = ApplicationConstants.SUCCESS;
			} else {
				errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
						+ "  : errorNumber  : " + errorNum + " errorMessage : "
						+ errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException exception) {
			errorNum = (Integer) resultFromProcedure
					.get(ApplicationConstants.ERRORNUMBER);
			final String errorMsg = (String) resultFromProcedure
					.get(ApplicationConstants.ERRORMESSAGE);
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ "  : errorNumber  : " + errorNum + " errorMessage : "
					+ errorMsg);
			throw new HubCitiWebSqlException(errorMsg);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	/**
	 * This will save Privacy Policy screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String insertPrivacyPolicyScreenSettings(
			ScreenSettings screenSettings, User user)
			throws HubCitiWebSqlException {
		final String methodName = "insertPrivacyPolicyScreenSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String status = null;
		Map<String, Object> resultFromProcedure = null;
		Integer errorNum = null;
		Integer responseFromProc = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall
					.withProcedureName("usp_WebHcAdminSetupPrivacyPolicy");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
			mapSqlParameterSource.addValue("HCAdminUserID",
					user.gethCAdminUserID());
			mapSqlParameterSource.addValue("HubCitiID", user.getHubCitiID());
			mapSqlParameterSource.addValue("BackgroundColor",
					screenSettings.getBgColor());
			mapSqlParameterSource.addValue("FontColor",
					screenSettings.getFontColor());
			mapSqlParameterSource.addValue("Title",
					screenSettings.getPageTitle());
			mapSqlParameterSource.addValue("Content",
					screenSettings.getPageContent());

			resultFromProcedure = simpleJdbcCall.execute(mapSqlParameterSource);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {

				status = ApplicationConstants.SUCCESS;
			} else {
				errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
						+ "  : errorNumber  : " + errorNum + " errorMessage : "
						+ errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException exception) {
			errorNum = (Integer) resultFromProcedure
					.get(ApplicationConstants.ERRORNUMBER);
			final String errorMsg = (String) resultFromProcedure
					.get(ApplicationConstants.ERRORMESSAGE);
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ "  : errorNumber  : " + errorNum + " errorMessage : "
					+ errorMsg);
			throw new HubCitiWebSqlException(errorMsg);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	/**
	 * This will save Splash Screen screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String insertSplashScreenSettings(ScreenSettings screenSettings,
			User user) throws HubCitiWebSqlException {
		final String methodName = "insertSplashScreenSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String status = null;
		Map<String, Object> resultFromProcedure = null;
		Integer errorNum = null;
		Integer responseFromProc = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.withProcedureName("usp_WebHcAdminSetupWelcomePage");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
			mapSqlParameterSource.addValue("HCAdminUserID",
					user.gethCAdminUserID());
			mapSqlParameterSource.addValue("HubCitiID", user.getHubCitiID());
			mapSqlParameterSource.addValue("WelcomePageImage",
					screenSettings.getLogoImageName());

			resultFromProcedure = simpleJdbcCall.execute(mapSqlParameterSource);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {

				status = ApplicationConstants.SUCCESS;
			} else {
				errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.info(ApplicationConstants.EXCEPTIONOCCURRED
						+ "  : errorNumber  : " + errorNum + " errorMessage : "
						+ errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException exception) {
			errorNum = (Integer) resultFromProcedure
					.get(ApplicationConstants.ERRORNUMBER);
			final String errorMsg = (String) resultFromProcedure
					.get(ApplicationConstants.ERRORMESSAGE);
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED
					+ "  : errorNumber  : " + errorNum + " errorMessage : "
					+ errorMsg);
			throw new HubCitiWebSqlException(errorMsg);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	/**
	 * This method will return application configuration.
	 * 
	 * @param configType
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	@SuppressWarnings("unchecked")
	public List<AppConfiguration> getAppConfig(String configType)
			throws HubCitiWebSqlException {
		final String strMethodName = "getAppConfig";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		List<AppConfiguration> appConfigurationList = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_GetScreenContent");
			simpleJdbcCall.returningResultSet("AppConfigurationList",
					new BeanPropertyRowMapper<AppConfiguration>(
							AppConfiguration.class));
			final MapSqlParameterSource productQueryParams = new MapSqlParameterSource();
			productQueryParams.addValue("ConfigurationType", configType);
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(productQueryParams);
			if (null != resultFromProcedure) {
				if (null == resultFromProcedure.get("ErrorNumber")) {
					appConfigurationList = (ArrayList<AppConfiguration>) resultFromProcedure
							.get("AppConfigurationList");
				}
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error("Inside HubCitiDAOImpl : getAppConfig : Error occurred in usp_GetScreenContent Store Procedure with error number: {} "
						+ errorNum + " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
			LOG.info("usp_GetScreenContent is  executed Successfully.");
		} catch (DataAccessException e) {
			LOG.error("Inside HubCitiDAOImpl : getAppConfig : " + e);
			throw new HubCitiWebSqlException(e);
		}
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		return appConfigurationList;
	}

	/**
	 * This will save the new password for the user.
	 * 
	 * @param objUser
	 * @return
	 * @throws HubCitiServiceException
	 */
	@SuppressWarnings("unchecked")
	public User forgotPwd(User objUser) throws HubCitiServiceException {
		final String strMethodName = "forgotPwd";
		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		String response = null;
		Map<String, Object> resultFromProcedure = null;
		final String strEnryptPwd = null;
		List<User> lstUser = null;
		User objForgotUser = new User();
		Integer responseFromProc = null;

		Integer isSuccess = null;
		try {

			// objUser.setPassword(strautogenPwd);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcAdminForgotPassword");
			simpleJdbcCall.returningResultSet("forgotUsrInfo",
					new BeanPropertyRowMapper<User>(User.class));

			final MapSqlParameterSource objCheckUsernameParameter = new MapSqlParameterSource();
			objCheckUsernameParameter.addValue("UserName",
					objUser.getUserName());
			objCheckUsernameParameter.addValue("NewPassword",
					objUser.getEncrptedPassword());
			resultFromProcedure = simpleJdbcCall
					.execute(objCheckUsernameParameter);
			lstUser = (ArrayList<User>) resultFromProcedure
					.get("forgotUsrInfo");
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);
			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				isSuccess = (Integer) resultFromProcedure.get("Status");
				if (isSuccess == 0) {
					if (null != lstUser && !lstUser.isEmpty()) {

						objForgotUser = lstUser.get(0);
						objForgotUser.setUserPwd(strEnryptPwd);
						response = ApplicationConstants.SUCCESS;
						objForgotUser.setResponse(response);
					} else {

						response = ApplicationConstants.FAILURETEXT;
						objForgotUser.setResponse(response);
					}

				} else {
					response = ApplicationConstants.FAILURETEXT;
					objForgotUser.setResponse(response);
				}
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.info("Inside HubCitiDAOImpl : forgotPwd  : errorNumber  : "
						+ errorNum + "errorMessage : " + errorMsg);
			}
			LOG.info("usp_WebUpdateUserPassword is  executed Successfully.");
		} catch (DataAccessException exception) {
			LOG.error("Inside HubCitiDAOImpl : forgotPwd : "
					+ exception.getMessage());
			throw new HubCitiServiceException(exception.getMessage(),
					exception.getCause());
		}

		LOG.info(ApplicationConstants.METHODSTART + strMethodName);
		return objForgotUser;
	}

	/**
	 * This will save General Settings screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String insertGeneralSettingsDetails(ScreenSettings screenSettings,
			User user) throws HubCitiWebSqlException {
		final String methodName = "insertGeneralSettingsDetails";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String status = null;
		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.withProcedureName("usp_WebHcGeneralSettings");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
			mapSqlParameterSource.addValue("HCAdminUserID",
					user.gethCAdminUserID());
			mapSqlParameterSource.addValue("HubCitiID", user.getHubCitiID());
			if ("TabBarLogo".equalsIgnoreCase(screenSettings.getPageType())) {
				mapSqlParameterSource.addValue("SmallLogo",
						screenSettings.getLogoImageName());
				mapSqlParameterSource.addValue("MenuBackgroundColor", null);
				mapSqlParameterSource.addValue("MenuBackgroundImage", null);
				mapSqlParameterSource.addValue("MenuButtonColor", null);
				mapSqlParameterSource.addValue("MenuButtonFontColor", null);
				mapSqlParameterSource.addValue("SubMenuBackgroundColor", null);
				mapSqlParameterSource.addValue("SubMenuBackgroundImage", null);
				mapSqlParameterSource.addValue("SubMenuButtonColor", null);
				mapSqlParameterSource.addValue("SubMenuButtonFontColor", null);
				mapSqlParameterSource.addValue("SettingsType",
						screenSettings.getPageType());
				mapSqlParameterSource.addValue("BottomButtonTypeID",
						screenSettings.getBottomBtnId());
				mapSqlParameterSource.addValue("AppIcon",
						screenSettings.getBannerImageName());
				mapSqlParameterSource
						.addValue("MenuGroupBackgroundColor", null);
				mapSqlParameterSource.addValue("MenuGroupFontColor", null);
				mapSqlParameterSource.addValue("SubMenuGroupBackgroundColor",
						null);
				mapSqlParameterSource.addValue("SubMenuGroupFontColor", null);
				mapSqlParameterSource.addValue("MenuIconicFontColor", null);
				mapSqlParameterSource.addValue("SubMenuIconicFontColor", null);

			} else if ("MainMenu"
					.equalsIgnoreCase(screenSettings.getPageType())) {
				mapSqlParameterSource.addValue("SmallLogo", null);
				mapSqlParameterSource.addValue("AppIcon", null);
				if (null != screenSettings.getIconSelection()
						&& !"".equalsIgnoreCase(screenSettings
								.getIconSelection())) {
					if ("image".equalsIgnoreCase(screenSettings
							.getIconSelection())) {
						mapSqlParameterSource.addValue("MenuBackgroundColor",
								null);
						mapSqlParameterSource.addValue("MenuBackgroundImage",
								screenSettings.getLogoImageName());
					} else if ("color".equalsIgnoreCase(screenSettings
							.getIconSelection())) {
						mapSqlParameterSource.addValue("MenuBackgroundColor",
								screenSettings.getBgColor());
						mapSqlParameterSource.addValue("MenuBackgroundImage",
								null);
					} else {
						mapSqlParameterSource.addValue("MenuBackgroundColor",
								null);
						mapSqlParameterSource.addValue("MenuBackgroundImage",
								null);
					}
				} else {
					mapSqlParameterSource.addValue("MenuBackgroundColor", null);
					mapSqlParameterSource.addValue("MenuBackgroundImage", null);
				}

				mapSqlParameterSource.addValue("MenuButtonColor",
						screenSettings.getBtnColor());
				mapSqlParameterSource.addValue("MenuButtonFontColor",
						screenSettings.getBtnFontColor());
				mapSqlParameterSource.addValue("SubMenuBackgroundColor", null);
				mapSqlParameterSource.addValue("SubMenuBackgroundImage", null);
				mapSqlParameterSource.addValue("SubMenuButtonColor", null);
				mapSqlParameterSource.addValue("SubMenuButtonFontColor", null);
				mapSqlParameterSource.addValue("SettingsType",
						screenSettings.getPageType());
				mapSqlParameterSource.addValue("BottomButtonTypeID", null);
				mapSqlParameterSource.addValue("MenuGroupBackgroundColor",
						screenSettings.getGrpColor());
				mapSqlParameterSource.addValue("MenuGroupFontColor",
						screenSettings.getGrpFontColor());
				mapSqlParameterSource.addValue("SubMenuGroupBackgroundColor",
						null);
				mapSqlParameterSource.addValue("SubMenuGroupFontColor", null);
				mapSqlParameterSource.addValue("MenuIconicFontColor",
						screenSettings.getIconsFontColor());
				mapSqlParameterSource.addValue("SubMenuIconicFontColor", null);
			} else if ("SubMenu".equalsIgnoreCase(screenSettings.getPageType())) {
				mapSqlParameterSource.addValue("SmallLogo", null);
				mapSqlParameterSource.addValue("AppIcon", null);
				if (null != screenSettings.getIconSelection()
						&& !"".equalsIgnoreCase(screenSettings
								.getIconSelection())) {
					if ("image".equalsIgnoreCase(screenSettings
							.getIconSelection())) {
						mapSqlParameterSource.addValue(
								"SubMenuBackgroundColor", null);
						mapSqlParameterSource.addValue(
								"SubMenuBackgroundImage",
								screenSettings.getLogoImageName());
					} else if ("color".equalsIgnoreCase(screenSettings
							.getIconSelection())) {
						mapSqlParameterSource.addValue(
								"SubMenuBackgroundColor",
								screenSettings.getBgColor());
						mapSqlParameterSource.addValue(
								"SubMenuBackgroundImage", null);
					} else {
						mapSqlParameterSource.addValue(
								"SubMenuBackgroundColor", null);
						mapSqlParameterSource.addValue(
								"SubMenuBackgroundImage", null);
					}
				} else {
					mapSqlParameterSource.addValue("SubMenuBackgroundColor",
							null);
					mapSqlParameterSource.addValue("SubMenuBackgroundImage",
							null);
				}

				mapSqlParameterSource.addValue("SubMenuButtonColor",
						screenSettings.getBtnColor());
				mapSqlParameterSource.addValue("SubMenuButtonFontColor",
						screenSettings.getBtnFontColor());
				mapSqlParameterSource.addValue("MenuBackgroundColor", null);
				mapSqlParameterSource.addValue("MenuBackgroundImage", null);
				mapSqlParameterSource.addValue("MenuButtonColor", null);
				mapSqlParameterSource.addValue("MenuButtonFontColor", null);
				mapSqlParameterSource.addValue("SettingsType",
						screenSettings.getPageType());
				mapSqlParameterSource.addValue("BottomButtonTypeID", null);
				mapSqlParameterSource
						.addValue("MenuGroupBackgroundColor", null);
				mapSqlParameterSource.addValue("MenuGroupFontColor", null);
				mapSqlParameterSource.addValue("SubMenuGroupBackgroundColor",
						screenSettings.getGrpColor());
				mapSqlParameterSource.addValue("SubMenuGroupFontColor",
						screenSettings.getGrpFontColor());
				mapSqlParameterSource.addValue("MenuIconicFontColor", null);
				mapSqlParameterSource.addValue("SubMenuIconicFontColor",
						screenSettings.getIconsFontColor());
			}

			resultFromProcedure = simpleJdbcCall.execute(mapSqlParameterSource);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {

				status = ApplicationConstants.SUCCESS;
			}
		} catch (DataAccessException exception) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ exception.getStackTrace());
			throw new HubCitiWebSqlException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	/**
	 * This methos is used to get the logged in user details.
	 * 
	 * @param userName
	 * @return User
	 */
	public User getLoginAdminDetails(String userName)
			throws HubCitiWebSqlException {
		final String methodName = "getLoginAdminDetails";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Map<String, Object> resultFromProcedure = null;
		Integer errorNum = null;
		Integer responseFromProc = null;
		List<User> UserDetails = new ArrayList<User>();
		User user = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.withProcedureName("usp_WebHcAdminLogin");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.returningResultSet("userdetails",
					new BeanPropertyRowMapper<User>(User.class));
			final MapSqlParameterSource loginScreenDetailsParam = new MapSqlParameterSource();
			loginScreenDetailsParam.addValue("UserName", userName);

			resultFromProcedure = simpleJdbcCall
					.execute(loginScreenDetailsParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				UserDetails = (ArrayList<User>) resultFromProcedure
						.get("userdetails");
				if (null != UserDetails && !UserDetails.isEmpty()) {
					user = UserDetails.get(0);
				}
			} else {
				errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ ": errorNumber  : " + errorNum + " errorMessage : "
						+ errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException exception) {
			errorNum = (Integer) resultFromProcedure
					.get(ApplicationConstants.ERRORNUMBER);
			final String errorMsg = (String) resultFromProcedure
					.get(ApplicationConstants.ERRORMESSAGE);
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ "  : errorNumber  : " + errorNum + " errorMessage : "
					+ errorMsg);
			throw new HubCitiWebSqlException(errorMsg);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return user;
	}

	/**
	 * This method returns the page details.
	 * 
	 * @param hubCitiId
	 * @return PageStatus
	 * @throws HubCitiWebSqlException
	 */
	@SuppressWarnings("unchecked")
	public final PageStatus getScreenStatus(int hubCitiId)
			throws HubCitiWebSqlException {
		final String methodName = "getScreenStatus";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Map<String, Object> resultFromProcedure = null;
		Integer errorNum = null;
		Integer responseFromProc = null;
		List<PageStatus> pageDetails = new ArrayList<PageStatus>();
		PageStatus status = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.withProcedureName("usp_WebHcPageDetails");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.returningResultSet("pagedetails",
					new BeanPropertyRowMapper<PageStatus>(PageStatus.class));
			final MapSqlParameterSource loginScreenDetailsParam = new MapSqlParameterSource();
			loginScreenDetailsParam.addValue("HubCitiID", hubCitiId);

			resultFromProcedure = simpleJdbcCall
					.execute(loginScreenDetailsParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				pageDetails = (ArrayList<PageStatus>) resultFromProcedure
						.get("pagedetails");
				if (null != pageDetails && !pageDetails.isEmpty()) {
					status = pageDetails.get(0);
				}
			} else {
				errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ ": errorNumber  : " + errorNum + " errorMessage : "
						+ errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException exception) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ exception.getStackTrace());
			throw new HubCitiWebSqlException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	/**
	 * This method updates the password with new password.
	 * 
	 * @param User
	 * @return String
	 * @throws HubCitiWebSqlException
	 */
	public final String saveChangedPassword(User user)
			throws HubCitiWebSqlException {
		final String methodName = "saveChangedPassword";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Map<String, Object> resultFromProcedure = null;
		Integer errorNum = null;
		Integer responseFromProc = null;
		String status = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.withProcedureName("usp_WebHcAdminResetPassword");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			final MapSqlParameterSource loginScreenDetailsParam = new MapSqlParameterSource();
			loginScreenDetailsParam.addValue("UserID", user.gethCAdminUserID());
			loginScreenDetailsParam.addValue("NewPassword", user.getPassword());

			resultFromProcedure = simpleJdbcCall
					.execute(loginScreenDetailsParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (responseFromProc == 0) {
				status = "Success";
			} else {
				errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ ": errorNumber  : " + errorNum + " errorMessage : "
						+ errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException exception) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ exception.getStackTrace());
			throw new HubCitiWebSqlException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	/**
	 * This method will return general application configuration image.
	 * 
	 * @param configType
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public List<AppConfiguration> getAppConfigForGeneralImages(String configType)
			throws HubCitiWebSqlException {
		String methodName = "getAppConfigForGeneralImages";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<AppConfiguration> appConfigurationList = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_GeneralImagesGetScreenContent");
			simpleJdbcCall.returningResultSet("AppConfigurationList",
					new BeanPropertyRowMapper<AppConfiguration>(
							AppConfiguration.class));
			final MapSqlParameterSource productQueryParams = new MapSqlParameterSource();
			productQueryParams.addValue("ConfigurationType", configType);
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(productQueryParams);
			if (null != resultFromProcedure) {
				if (null == resultFromProcedure.get("ErrorNumber")) {
					appConfigurationList = (ArrayList<AppConfiguration>) resultFromProcedure
							.get("AppConfigurationList");
				} else {
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error("Inside HubCitiDAOImpl : getAppConfigForGeneralImages : Error occurred in usp_GeneralImagesGetScreenContent Store Procedure with error number: {} "
							+ errorNum + " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);
				}
			}

			LOG.info("usp_GeneralImagesGetScreenContent is  executed Successfully.");
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return appConfigurationList;
	}

	public String createMenu(MenuDetails menuDetails, User user)
			throws HubCitiWebSqlException {
		final String methodName = "createMenu";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Integer menu = null;
		Boolean isDuplicate = null;
		String response = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcMenuCreation");
			final MapSqlParameterSource menuQueryParams = new MapSqlParameterSource();
			menuQueryParams.addValue("HubCitiID", user.getHubCitiID());
			// menuQueryParams.addValue("HCTemplateName",
			// menuDetails.getMenuTypeName());
			menuQueryParams.addValue("LevelID", menuDetails.getLevel());
			menuQueryParams.addValue("UserID", user.gethCAdminUserID());
			menuQueryParams.addValue("MenuName", menuDetails.getMenuName());

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(menuQueryParams);

			isDuplicate = (Boolean) resultFromProcedure.get("DuplicateFlag");
			menu = (Integer) resultFromProcedure.get("HCMenuID");
			if (isDuplicate) {
				response = "Duplicate";
			} else {
				response = menu.toString();
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return response;
	}

	/**
	 * This method will return general application configuration image.
	 * 
	 * @param configType
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public List<MenuDetails> getLinkList() throws HubCitiWebSqlException {
		final String methodName = "getLinkList";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<MenuDetails> linkList = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcHubCitiLinkTypeDisplay");
			simpleJdbcCall.returningResultSet("linkList",
					new BeanPropertyRowMapper<MenuDetails>(MenuDetails.class));
			final MapSqlParameterSource linkParam = new MapSqlParameterSource();
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(linkParam);
			if (null != resultFromProcedure) {
				if (null == resultFromProcedure.get("ErrorNumber")) {
					linkList = (ArrayList<MenuDetails>) resultFromProcedure
							.get("linkList");
				}
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
			LOG.info("usp_GeneralImagesGetScreenContent is  executed Successfully.");
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return linkList;
	}

	public String saveMenuTemplate(MenuDetails menuDetails, User user)
			throws HubCitiWebSqlException {
		final String methodName = "saveMenuTemplate";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String status = null;
		Map<String, Object> resultFromProcedure = null;
		Integer errorNum = null;
		Integer responseFromProc = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcMenuItemCreation");
			final MapSqlParameterSource menuQueryParams = new MapSqlParameterSource();

			menuQueryParams.addValue("HCMenuID", menuDetails.getMenuId());
			menuQueryParams.addValue("HCTemplateName",
					menuDetails.getMenuTypeName());
			menuQueryParams.addValue("Level", menuDetails.getLevel());
			menuQueryParams.addValue("MenuName", menuDetails.getMenuName());
			menuQueryParams.addValue("HCMenuItemName", menuDetails
					.getButtonDetails().getBtnName());
			menuQueryParams.addValue("HCLinkTypeID", menuDetails
					.getButtonDetails().getBtnAction());
			menuQueryParams.addValue("HCLinkID", menuDetails.getButtonDetails()
					.getLinkId());
			menuQueryParams.addValue("Position", menuDetails.getButtonDetails()
					.getBtnPosition());
			menuQueryParams.addValue("MenuItemImgPath", menuDetails
					.getButtonDetails().getBtnImage());
			menuQueryParams.addValue("HCHubCitiID", user.getHubCitiID());
			menuQueryParams.addValue("UserID", user.gethCAdminUserID());
			menuQueryParams.addValue("HCMenuBannerImage",
					menuDetails.getBannerImg());
			menuQueryParams.addValue("BottomButtonID",
					menuDetails.getStrBottomBtnId());
			menuQueryParams.addValue("HcDepartmentName", menuDetails
					.getButtonDetails().getBtnDept());
			menuQueryParams.addValue("HcTypeName", menuDetails
					.getButtonDetails().getBtnType());

			menuQueryParams.addValue("HcTypeFlag", menuDetails.isTypeFlag());
			menuQueryParams.addValue("HcDepartmentFlag",
					menuDetails.isDepartmentFlag());
			menuQueryParams.addValue("NoOfColumns",
					menuDetails.getNoOfColumns());
			menuQueryParams.addValue("HcMenuItemShapeID", menuDetails
					.getButtonDetails().getBtnShape());
			// adding parameter for find category - subcategory implementation
			menuQueryParams.addValue("HcBusinessSubCategoryID", menuDetails
					.getButtonDetails().getFindSubCatIds());

			if ("RegionApp".equalsIgnoreCase(user.getUserType())) {
				menuQueryParams.addValue("HcCityID", menuDetails
						.getButtonDetails().getCitiId());
			} else {
				menuQueryParams.addValue("HcCityID", null);
			}

			resultFromProcedure = simpleJdbcCall.execute(menuQueryParams);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {

				status = ApplicationConstants.SUCCESS;
			} else {
				errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
						+ "  : errorNumber  : " + errorNum + " errorMessage : "
						+ errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	@SuppressWarnings("unchecked")
	public MenuDetails fetchMainMenuDetails(MenuDetails menuDetails, User user)
			throws HubCitiWebSqlException {
		final String methodName = "fetchMainMenuDetails";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Map<String, Object> resultFromProcedure = null;
		Integer errorNum = null;
		Integer responseFromProc = null;
		MenuDetails mainMenuDetails = null;
		List<ButtonDetails> buttonDetailsList = null;
		Integer menuId = null;
		String templateName = null;
		Integer level = null;
		String menuName = null;
		String strBannerImg = null;
		String strBannerImgName = null;
		Boolean departmentFlag;
		Boolean typeFlag;
		Integer noOfColumns = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.withProcedureName("usp_WebHcHubCitiMenuDisplay");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.returningResultSet("btnDetails",
					new BeanPropertyRowMapper<ButtonDetails>(
							ButtonDetails.class));
			final MapSqlParameterSource menuDetailsParam = new MapSqlParameterSource();
			menuDetailsParam.addValue("HcMenuID", menuDetails.getMenuId());
			menuDetailsParam.addValue("LevelID", menuDetails.getLevel());
			menuDetailsParam.addValue("UserID", user.gethCAdminUserID());
			menuDetailsParam.addValue("HubCitiID", user.getHubCitiID());

			resultFromProcedure = simpleJdbcCall.execute(menuDetailsParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				buttonDetailsList = (List<ButtonDetails>) resultFromProcedure
						.get("btnDetails");
				if (null != buttonDetailsList && !buttonDetailsList.isEmpty()) {
					menuId = (Integer) resultFromProcedure.get("MenuID");
					level = (Integer) resultFromProcedure.get("Level");
					templateName = (String) resultFromProcedure
							.get("TemplateName");
					menuName = (String) resultFromProcedure.get("MenuName");
					strBannerImg = (String) resultFromProcedure
							.get("BannerImage");
					strBannerImgName = (String) resultFromProcedure
							.get("BannerImageName");
					departmentFlag = (Boolean) resultFromProcedure
							.get("HcDepartmentFlag");
					typeFlag = (Boolean) resultFromProcedure.get("HcTypeFlag");
					noOfColumns = (Integer) resultFromProcedure
							.get("NoOfColumns");
					mainMenuDetails = new MenuDetails();
					mainMenuDetails.setButtons(buttonDetailsList);
					mainMenuDetails.setMenuId(menuId);
					mainMenuDetails.setLevel(level);
					mainMenuDetails.setMenuTypeName(templateName);
					mainMenuDetails.setMenuName(menuName);
					mainMenuDetails.setBannerImg(strBannerImg);
					mainMenuDetails.setBannerImageName(strBannerImgName);
					mainMenuDetails.setDepartmentFlag(departmentFlag);
					mainMenuDetails.setTypeFlag(typeFlag);
					mainMenuDetails.setNoOfColumns(noOfColumns);
				}
			} else {
				errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ ": errorNumber  : " + errorNum + " errorMessage : "
						+ errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException exception) {
			errorNum = (Integer) resultFromProcedure
					.get(ApplicationConstants.ERRORNUMBER);
			final String errorMsg = (String) resultFromProcedure
					.get(ApplicationConstants.ERRORMESSAGE);
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ "  : errorNumber  : " + errorNum + " errorMessage : "
					+ errorMsg);
			throw new HubCitiWebSqlException(errorMsg);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return mainMenuDetails;
	}

	public SubMenuDetails fetchSubMenuDetails(User user,
			ScreenSettings screenSettings) throws HubCitiWebSqlException {
		final String methodName = "fetchSubMenuDetails";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Map<String, Object> resultFromProcedure = null;
		Integer errorNum = null;
		Integer responseFromProc = null;
		List<MenuDetails> subMenuLst = null;
		SubMenuDetails objSubMenuDetails = null;
		Integer iTotalSize = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.withProcedureName("usp_WebHcSubMenuDisplay");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.returningResultSet("subMenuDetails",
					new BeanPropertyRowMapper<MenuDetails>(MenuDetails.class));
			final MapSqlParameterSource menuDetailsParam = new MapSqlParameterSource();
			menuDetailsParam.addValue("HubCitiID", user.getHubCitiID());
			menuDetailsParam.addValue("SearchKey",
					screenSettings.getSearchKey());
			menuDetailsParam.addValue("LowerLimit",
					screenSettings.getLowerLimit());
			menuDetailsParam.addValue("ScreenName",
					ApplicationConstants.HUBCITIWEBPAGINATION);

			resultFromProcedure = simpleJdbcCall.execute(menuDetailsParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				subMenuLst = (ArrayList<MenuDetails>) resultFromProcedure
						.get("subMenuDetails");

				if (null != subMenuLst && !subMenuLst.isEmpty()) {
					iTotalSize = (Integer) resultFromProcedure.get("MaxCnt");

					objSubMenuDetails = new SubMenuDetails();
					objSubMenuDetails.setSubMenuList(subMenuLst);
					objSubMenuDetails.setTotalSize(iTotalSize);

				}

			} else {
				errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ ": errorNumber  : " + errorNum + " errorMessage : "
						+ errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException exception) {
			errorNum = (Integer) resultFromProcedure
					.get(ApplicationConstants.ERRORNUMBER);
			final String errorMsg = (String) resultFromProcedure
					.get(ApplicationConstants.ERRORMESSAGE);
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ "  : errorNumber  : " + errorNum + " errorMessage : "
					+ errorMsg);
			throw new HubCitiWebSqlException(errorMsg);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return objSubMenuDetails;
	}

	public List<AppSiteDetails> getAppSites(String searchKey, int ihubCityId,
			Integer lowerLimit) throws HubCitiWebSqlException {
		final String methodName = "getAppSites";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer responseFromProc = null;
		List<AppSiteDetails> appSiteDetailsLst = null;
		Integer iRowcount = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcHubCitiAppSiteDisplay");
			simpleJdbcCall.returningResultSet("appsites",
					new BeanPropertyRowMapper<AppSiteDetails>(
							AppSiteDetails.class));
			final MapSqlParameterSource appsiteparam = new MapSqlParameterSource();
			appsiteparam.addValue("HubCitiID", ihubCityId);
			appsiteparam.addValue("LowerLimit", lowerLimit);
			appsiteparam.addValue("ScreenName",
					ApplicationConstants.HUBCITIWEBPAGINATION);
			appsiteparam.addValue("SearchKey", searchKey);

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(appsiteparam);

			if (null != resultFromProcedure) {

				responseFromProc = (Integer) resultFromProcedure
						.get(ApplicationConstants.STATUS);

				if (null != responseFromProc
						&& responseFromProc.intValue() == 0) {
					appSiteDetailsLst = (ArrayList<AppSiteDetails>) resultFromProcedure
							.get("appsites");

					if (null != appSiteDetailsLst
							&& !appSiteDetailsLst.isEmpty()) {

						iRowcount = (Integer) resultFromProcedure.get("MaxCnt");

						AppSiteDetails appSiteDetails = appSiteDetailsLst
								.get(0);
						appSiteDetails.setTotalRecordSize(iRowcount);
						appSiteDetailsLst.remove(0);
						appSiteDetailsLst.add(0, appSiteDetails);
					}

				} else {
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ methodName + "Error number: {}" + errorNum
							+ " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);
				}
			}

			LOG.info("usp_WebHcHubCitiAppSiteDisplay is  executed Successfully.");
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return appSiteDetailsLst;
	}

	public List<MenuDetails> getBottomLinkList(int ihubCityId)
			throws HubCitiWebSqlException {
		final String methodName = "getButtomLinkList";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer responseFromProc = null;
		List<MenuDetails> linkList = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcBottomButtonLinkTypeDisplay");
			simpleJdbcCall.returningResultSet("linkList",
					new BeanPropertyRowMapper<MenuDetails>(MenuDetails.class));
			final MapSqlParameterSource linkParam = new MapSqlParameterSource();
			linkParam.addValue("HcHubCitiID", ihubCityId);
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(linkParam);

			if (null != resultFromProcedure) {

				responseFromProc = (Integer) resultFromProcedure
						.get(ApplicationConstants.STATUS);

				if (null != responseFromProc
						&& responseFromProc.intValue() == 0) {
					linkList = (ArrayList<MenuDetails>) resultFromProcedure
							.get("linkList");
				} else {
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ methodName + "Error number: {}" + errorNum
							+ " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);
				}
			}

			LOG.info("usp_WebHcBottomButtonLinkTypeDisplay is  executed Successfully.");
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return linkList;
	}

	public List<ScreenSettings> getBottomBarExistingIcons()
			throws HubCitiWebSqlException {
		final String methodName = "getButtomBarExistingIcons";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer responseFromProc = null;
		List<ScreenSettings> existingIconsList = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcBottomButtonIamgeIconsDisplay");
			simpleJdbcCall.returningResultSet("existingIconsList",
					new BeanPropertyRowMapper<ScreenSettings>(
							ScreenSettings.class));
			final MapSqlParameterSource linkParam = new MapSqlParameterSource();
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(linkParam);

			if (null != resultFromProcedure) {

				responseFromProc = (Integer) resultFromProcedure
						.get(ApplicationConstants.STATUS);

				if (null != responseFromProc
						&& responseFromProc.intValue() == 0) {
					existingIconsList = (ArrayList<ScreenSettings>) resultFromProcedure
							.get("existingIconsList");
				} else {
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ methodName + "Error number: {}" + errorNum
							+ " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);
				}
			}

			LOG.info("usp_WebHcBottomButtonIamgeIconsDisplay is  executed Successfully.");
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return existingIconsList;
	}

	public String insertUpdateTabBarButton(ScreenSettings buttondetails,
			User loginUser) throws HubCitiWebSqlException {

		final String methodName = "saveTabBarButton";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String status = ApplicationConstants.FAILURE;
		Integer responseFromProc = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcBottomButtonCreation");

			final MapSqlParameterSource buttondetailsParam = new MapSqlParameterSource();
			buttondetailsParam.addValue("HcBottomButtonID",
					buttondetails.getBottomBtnId());
			if (null == buttondetails.getLogoImageName()) {
				buttondetailsParam.addValue("BottomButtonImageOn", null);
			} else {
				buttondetailsParam.addValue("BottomButtonImageOn",
						buttondetails.getLogoImageName());
			}

			if (null == buttondetails.getBannerImageName()) {
				buttondetailsParam.addValue("BottomButtonImageOff", null);
			} else {
				buttondetailsParam.addValue("BottomButtonImageOff",
						buttondetails.getBannerImageName());
			}

			buttondetailsParam.addValue("BottomButtonLinkTypeID",
					buttondetails.getMenuFucntionality());
			buttondetailsParam.addValue("BottomButtonLinkID",
					buttondetails.getBtnLinkId());
			buttondetailsParam.addValue("HcBottomButtonImageIconID",
					buttondetails.getIconId());
			buttondetailsParam.addValue("UserID", loginUser.gethCAdminUserID());
			buttondetailsParam.addValue("HubCitiID", loginUser.getHubCitiID());
			buttondetailsParam.addValue("ItunesURL",
					buttondetails.getiTunesLnk());
			buttondetailsParam.addValue("GooglePlayURL",
					buttondetails.getPlayStoreLnk());
			buttondetailsParam.addValue("HcBusinessSubCategoryID",
					buttondetails.getSubCatIds());

			// buttondetailsParam.addValue("BottomButtonLinkID",'90|91!~~!NULL!~~!NULL'
			// buttondetails.getBtnLinkId());NULL!~~!90|88|86|89!~~!NULL

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(buttondetailsParam);
			if (null != resultFromProcedure) {
				responseFromProc = (Integer) resultFromProcedure
						.get(ApplicationConstants.STATUS);

				if (null != responseFromProc
						&& responseFromProc.intValue() == 0) {
					status = ApplicationConstants.SUCCESS;

				} else {
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ methodName + "Error number: {}" + errorNum
							+ " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);
				}

			}

			LOG.info("usp_WebHcBottomButtonCreation is  executed Successfully.");
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	@SuppressWarnings("unchecked")
	public List<ScreenSettings> fetchTabBarButtons(MenuDetails menuDetails,
			User loginUser) throws HubCitiWebSqlException {
		final String methodName = "fetchTabBarButtons";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer responseFromProc = null;
		List<ScreenSettings> tabBarButtonsList = null;
		String findBtnCatId = null;
		// iconicMenuItems = new ArrayList<ScreenSettings>();
		StringBuilder findCategories = null;
		ScreenSettings iconicMenuItem = null;
		List<ScreenSettings> tabBarButtonsLst = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcBottomButtonDisplay");
			simpleJdbcCall.returningResultSet("tabBarButtonsList",
					new BeanPropertyRowMapper<ScreenSettings>(
							ScreenSettings.class));
			final MapSqlParameterSource linkParam = new MapSqlParameterSource();
			linkParam.addValue("HcHubCitiID", loginUser.getHubCitiID());

			if (null == menuDetails) {
				linkParam.addValue("HcMenuID", null);

			} else {
				linkParam.addValue("HcMenuID", menuDetails.getMenuId());

			}

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(linkParam);

			if (null != resultFromProcedure) {

				responseFromProc = (Integer) resultFromProcedure
						.get(ApplicationConstants.STATUS);

				if (null != responseFromProc
						&& responseFromProc.intValue() == 0) {
					tabBarButtonsList = (ArrayList<ScreenSettings>) resultFromProcedure
							.get("tabBarButtonsList");

					if (null != tabBarButtonsList
							&& !tabBarButtonsList.isEmpty()) {
						tabBarButtonsLst = new ArrayList<ScreenSettings>();

						for (ScreenSettings buttonDetail : tabBarButtonsList) {
							iconicMenuItem = new ScreenSettings();
							iconicMenuItem.setBottomBtnId(buttonDetail
									.getBottomBtnId());

							iconicMenuItem.setMenuFucntionality(buttonDetail
									.getMenuFucntionality());
							iconicMenuItem.setiTunesLnk(buttonDetail
									.getiTunesLnk());
							iconicMenuItem.setPlayStoreLnk(buttonDetail
									.getPlayStoreLnk());
							iconicMenuItem.setIconId(buttonDetail.getIconId());
							iconicMenuItem.setImagePathOff(buttonDetail
									.getImagePathOff());
							iconicMenuItem.setImagePath(buttonDetail
									.getImagePath());
							iconicMenuItem.setImageName(buttonDetail
									.getImageName());
							iconicMenuItem.setImageNameOff(buttonDetail
									.getImageNameOff());
							// iconicMenuItem.setPo

							if (null != buttonDetail.getBtnLinkId()) {

								if (!Utility.isEmptyOrNullString(buttonDetail
										.getFunType())) {
									if (buttonDetail.getFunType()
											.equals("Find")) {
										findBtnCatId = buttonDetail
												.getBtnLinkId();

										if (!Utility
												.isEmptyOrNullString(findBtnCatId)) {
											if (findBtnCatId.contains(",")) {
												findCategories = new StringBuilder();
												String[] strings = findBtnCatId
														.split(",");
												for (int i = 0; i < strings.length; i++) {
													findCategories
															.append(strings[i]
																	+ "-MC");
													findCategories.append(",");
												}
												findBtnCatId = findCategories
														.toString();
											} else {
												findBtnCatId = buttonDetail
														.getBtnLinkId() + "-MC";
											}

										}

										iconicMenuItem
												.setBtnLinkId(findBtnCatId);

									} else {
										iconicMenuItem
												.setBtnLinkId(buttonDetail
														.getBtnLinkId());
									}
								} else {
									iconicMenuItem.setBtnLinkId(buttonDetail
											.getBtnLinkId());
								}

								iconicMenuItem.setSubCatIds(buttonDetail
										.getSvdSubCate());
								iconicMenuItem.setChkSubCate(buttonDetail
										.getFindSubCatIds());
								iconicMenuItem.setHiddenSubCate(buttonDetail
										.getFindSubCatIds());

							}

							tabBarButtonsLst.add(iconicMenuItem);
						}

					}

				} else {
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ methodName + "Error number: {}" + errorNum
							+ " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);
				}
			}

			LOG.info("usp_WebHcBottomButtonDisplay is  executed Successfully.");
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return tabBarButtonsLst;
	}

	public String deleteTabBarButton(Integer bottomBtnId)
			throws HubCitiWebSqlException {
		final String methodName = "deleteTabBarButton";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String status = ApplicationConstants.FAILURE;
		Integer responseFromProc = null;

		Boolean isAssociated = false;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcBottomButtonDeletion");
			final MapSqlParameterSource buttondetailsParam = new MapSqlParameterSource();
			buttondetailsParam.addValue("HcBottomButtonID", bottomBtnId);
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(buttondetailsParam);
			if (null != resultFromProcedure) {
				responseFromProc = (Integer) resultFromProcedure
						.get(ApplicationConstants.STATUS);

				if (null != responseFromProc
						&& responseFromProc.intValue() == 0) {
					status = ApplicationConstants.SUCCESS;

					isAssociated = (Boolean) resultFromProcedure
							.get(ApplicationConstants.ISASSOCIATED);
					if (isAssociated) {

						status = ApplicationConstants.FAILURE;
					}

				} else {
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ methodName + "Error number: {}" + errorNum
							+ " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);
				}

			}

			LOG.info("usp_WebHcBottomButtonDeletion is  executed Successfully.");
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	public List<AppSiteDetails> getHubCityRetailer(AppSiteDetails appSiteDetails)
			throws HubCitiWebSqlException {
		final String methodName = "getHubCityRetailer";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer responseFromProc = null;
		List<AppSiteDetails> appSiteDetailsLst = null;
		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcRetailerSearch");
			simpleJdbcCall.returningResultSet("retailer",
					new BeanPropertyRowMapper<AppSiteDetails>(
							AppSiteDetails.class));
			final MapSqlParameterSource appsiteparam = new MapSqlParameterSource();
			appsiteparam.addValue("HubCitiID", appSiteDetails.getHubCityId());
			appsiteparam.addValue("SearchKey", appSiteDetails.getSearchKey());
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(appsiteparam);

			if (null != resultFromProcedure) {

				responseFromProc = (Integer) resultFromProcedure
						.get(ApplicationConstants.STATUS);

				if (null != responseFromProc
						&& responseFromProc.intValue() == 0) {
					appSiteDetailsLst = (ArrayList<AppSiteDetails>) resultFromProcedure
							.get("retailer");
				} else {
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ methodName + "Error number: {}" + errorNum
							+ " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);
				}
			}

			LOG.info("usp_WebHcRetailerSearch is  executed Successfully.");
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return appSiteDetailsLst;
	}

	@SuppressWarnings("unchecked")
	public List<AppSiteDetails> displayRetailLocations(
			AppSiteDetails appSiteDetails) throws HubCitiWebSqlException {
		final String methodName = "displayRetailLocations";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer responseFromProc = null;
		List<AppSiteDetails> retailLocationLst = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcRetailLocationList");
			simpleJdbcCall.returningResultSet("retloclst",
					new BeanPropertyRowMapper<AppSiteDetails>(
							AppSiteDetails.class));
			final MapSqlParameterSource appsiteparam = new MapSqlParameterSource();
			appsiteparam.addValue("HubCitiID", appSiteDetails.getHubCityId());
			appsiteparam.addValue("RetailID", appSiteDetails.getRetailId());
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(appsiteparam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				retailLocationLst = (ArrayList<AppSiteDetails>) resultFromProcedure
						.get("retloclst");
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}

			LOG.info("usp_WebHcRetailLocationList is  executed Successfully.");
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.error(ApplicationConstants.METHODEND + methodName);
		return retailLocationLst;
	}

	/**
	 * This will save Anything screen details.
	 * 
	 * @param screenSettings
	 * @param objUser
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String saveAnyThingScreen(ScreenSettings objScreenSettings,
			User objUser) throws HubCitiWebSqlException {
		final String methodName = "saveAnyThingScreen";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;
		Map<String, Object> resultFromProcedure = null;
		Integer errorNum = null;
		Integer responseFromProc = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiAnythingPageCreation");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			final MapSqlParameterSource anythingPageParam = new MapSqlParameterSource();
			anythingPageParam.addValue("UserID", objUser.gethCAdminUserID());
			anythingPageParam.addValue("HcHubCitiID", objUser.getHubCitiID());
			anythingPageParam.addValue("AnythingPageName",
					objScreenSettings.getPageTitle());
			anythingPageParam.addValue("ShortDescription",
					objScreenSettings.getShortDescription());
			anythingPageParam.addValue("LongDescription",
					objScreenSettings.getLongDescription());
			if ("".equalsIgnoreCase(objScreenSettings.getStartDate())) {
				anythingPageParam.addValue("StartDate", null);
			} else {
				anythingPageParam
						.addValue("StartDate", Utility
								.getFormattedDateTime(objScreenSettings
										.getStartDate()));
			}
			if ("".equalsIgnoreCase(objScreenSettings.getEndDate())) {
				anythingPageParam.addValue("EndDate", null);
			} else {
				anythingPageParam.addValue("EndDate", Utility
						.getFormattedDateTime(objScreenSettings.getEndDate()));
			}
			anythingPageParam.addValue("HcAnyThingPageMediaTypeID",
					objScreenSettings.getPageType());
			if (null != objScreenSettings.getPageType()) {
				if ("4".equals(objScreenSettings.getPageType())) {
					anythingPageParam.addValue("URL",
							objScreenSettings.getPageAttachLink());
					anythingPageParam.addValue("MediaPath", null);
				} else {
					anythingPageParam.addValue("URL", null);
					anythingPageParam.addValue("MediaPath",
							objScreenSettings.getPathName());
				}
			} else {
				anythingPageParam.addValue("URL", null);
				anythingPageParam.addValue("MediaPath", null);
			}
			if (null != objScreenSettings.getIconSelect()) {
				if ("exstngIcon".equals(objScreenSettings.getIconSelect())) {
					anythingPageParam.addValue("ImageIconID",
							objScreenSettings.getImageIconID());
					anythingPageParam.addValue("ImageIconPath", null);
				} else {
					anythingPageParam.addValue("ImageIconID", null);
					anythingPageParam.addValue("ImageIconPath",
							objScreenSettings.getLogoImageName());
				}
			} else {
				anythingPageParam.addValue("ImageIconID", null);
				anythingPageParam.addValue("ImageIconPath",
						objScreenSettings.getLogoImageName());
			}

			resultFromProcedure = simpleJdbcCall.execute(anythingPageParam);

			if (null != resultFromProcedure) {
				responseFromProc = (Integer) resultFromProcedure
						.get(ApplicationConstants.STATUS);

				if (null != responseFromProc
						&& responseFromProc.intValue() == 0) {
					status = ApplicationConstants.SUCCESS;
				} else {
					errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ "  : errorNumber  : " + errorNum
							+ " errorMessage : " + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);
				}
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		} catch (ParseException e) {
			LOG.info(ApplicationConstants.EXCEPTIONOCCURRED + e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	/**
	 * This DAOImpl method will return list of images to display.
	 * 
	 * @param strPageType
	 * @return HubCitiImages Icon List.
	 * @throws HubCitiWebSqlException
	 *             as SQL Exception will be thrown.
	 */
	@SuppressWarnings("unchecked")
	public List<HubCitiImages> getHubCitiImageIconsDisplay(String strPageType)
			throws HubCitiWebSqlException {
		final String methodName = "getHubCitiImageIconsDisplay";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<HubCitiImages> arHcImageList = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiImageIconsDisplay");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.returningResultSet("LocationIdPageList",
					new BeanPropertyRowMapper<HubCitiImages>(
							HubCitiImages.class));
			final MapSqlParameterSource objAdsParameter = new MapSqlParameterSource();
			objAdsParameter.addValue("PageType", strPageType);
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(objAdsParameter);
			arHcImageList = (ArrayList<HubCitiImages>) resultFromProcedure
					.get("LocationIdPageList");
		} catch (DataAccessException e) {
			LOG.error("Inside HubCitiDAOImpl : getHubCitiImageIconsDisplay : "
					+ e.getMessage());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return arHcImageList;
	}

	/**
	 * This method returns list of anything pages created by that user.
	 * 
	 * @param user
	 * @param searchKey
	 * @param lowerLimit
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	@SuppressWarnings("unchecked")
	public AnythingPages fetchAnythingPages(User user, String searchKey,
			Integer lowerLimit) throws HubCitiWebSqlException {
		final String methodName = "fetchAnythingPages";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Map<String, Object> resultFromProcedure = null;
		Integer errorNum = null;
		Integer responseFromProc = null;
		List<AnythingPageDetails> anythingPageDetails = null;
		AnythingPages anythingPages = null;
		Integer totalSize = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiAnythingPagesDisplay");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.returningResultSet("anythingPageDetails",
					new BeanPropertyRowMapper<AnythingPageDetails>(
							AnythingPageDetails.class));
			final MapSqlParameterSource menuDetailsParam = new MapSqlParameterSource();
			menuDetailsParam.addValue("HubCitiID", user.getHubCitiID());
			menuDetailsParam.addValue("SearchKey", searchKey);
			menuDetailsParam.addValue("LowerLimit", lowerLimit);

			resultFromProcedure = simpleJdbcCall.execute(menuDetailsParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				anythingPageDetails = (ArrayList<AnythingPageDetails>) resultFromProcedure
						.get("anythingPageDetails");
				totalSize = (Integer) resultFromProcedure.get("MaxCnt");
				anythingPages = new AnythingPages();
				anythingPages.setPageDetails(anythingPageDetails);
				anythingPages.setTotalSize(totalSize);

			} else {
				errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ ": errorNumber  : " + errorNum + " errorMessage : "
						+ errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException exception) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ exception.getStackTrace());
			throw new HubCitiWebSqlException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return anythingPages;
	}

	/**
	 * This will return anything page types.
	 * 
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	@SuppressWarnings("unchecked")
	public List<HubCitiImages> getAnythingPageType()
			throws HubCitiWebSqlException {
		final String methodName = "getAnythingPageType";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<HubCitiImages> pageTypes = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiAnythingPageMediaType");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.returningResultSet("pageType",
					new BeanPropertyRowMapper<HubCitiImages>(
							HubCitiImages.class));
			final MapSqlParameterSource objAdsParameter = new MapSqlParameterSource();
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(objAdsParameter);
			pageTypes = (ArrayList<HubCitiImages>) resultFromProcedure
					.get("pageType");
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return pageTypes;
	}

	/**
	 * This will update Anything screen details.
	 * 
	 * @param screenSettings
	 * @param objUser
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String updateAnyThingScreen(ScreenSettings objScreenSettings,
			User objUser) throws HubCitiWebSqlException {
		final String methodName = "updateAnyThingScreen";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;
		Map<String, Object> resultFromProcedure = null;
		Integer errorNum = null;
		Integer responseFromProc = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiAnythingPageUpdation");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			final MapSqlParameterSource anythingPageParam = new MapSqlParameterSource();
			anythingPageParam.addValue("UserID", objUser.gethCAdminUserID());
			anythingPageParam.addValue("HcHubCitiID", objUser.getHubCitiID());
			anythingPageParam.addValue("AnythingPageID",
					objScreenSettings.getHiddenBtnLinkId());
			anythingPageParam.addValue("AnythingPageName",
					objScreenSettings.getPageTitle());
			anythingPageParam.addValue("ShortDescription",
					objScreenSettings.getShortDescription());
			anythingPageParam.addValue("LongDescription",
					objScreenSettings.getLongDescription());
			anythingPageParam.addValue("HcAnyThingPageMediaTypeID",
					objScreenSettings.getPageType());
			if ("".equalsIgnoreCase(objScreenSettings.getStartDate())) {
				anythingPageParam.addValue("StartDate", null);
			} else {
				anythingPageParam
						.addValue("StartDate", Utility
								.getFormattedDateTime(objScreenSettings
										.getStartDate()));
			}
			if ("".equalsIgnoreCase(objScreenSettings.getEndDate())) {
				anythingPageParam.addValue("EndDate", null);
			} else {
				anythingPageParam.addValue("EndDate", Utility
						.getFormattedDateTime(objScreenSettings.getEndDate()));
			}
			if (null != objScreenSettings.getPageType()) {
				if ("4".equals(objScreenSettings.getPageType())) {
					anythingPageParam.addValue("URL",
							objScreenSettings.getPageAttachLink());
					anythingPageParam.addValue("MediaPath", null);
				} else {
					anythingPageParam.addValue("URL", null);
					anythingPageParam.addValue("MediaPath",
							objScreenSettings.getPathName());
				}
			} else {
				anythingPageParam.addValue("URL", null);
				anythingPageParam.addValue("MediaPath", null);
			}
			if (null != objScreenSettings.getIconSelect()) {
				if ("exstngIcon".equals(objScreenSettings.getIconSelect())) {
					anythingPageParam.addValue("ImageIconID",
							objScreenSettings.getImageIconID());
					anythingPageParam.addValue("ImageIconPath", null);
				} else {
					anythingPageParam.addValue("ImageIconID", null);
					anythingPageParam.addValue("ImageIconPath",
							objScreenSettings.getLogoImageName());
				}
			} else {
				anythingPageParam.addValue("ImageIconID", null);
				anythingPageParam.addValue("ImageIconPath",
						objScreenSettings.getLogoImageName());
			}
			resultFromProcedure = simpleJdbcCall.execute(anythingPageParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);
			if (null != resultFromProcedure) {
				if (null != responseFromProc
						&& responseFromProc.intValue() == 0) {
					status = ApplicationConstants.SUCCESS;
				} else {
					errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ "  : errorNumber  : " + errorNum
							+ " errorMessage : " + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);
				}
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		} catch (ParseException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	/**
	 * This will fetch Anything screen details.
	 * 
	 * @param screenSettings
	 * @param objUser
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	@SuppressWarnings("unchecked")
	public ScreenSettings getAnyThingPage(ScreenSettings objScreenSettings,
			User objUser) throws HubCitiWebSqlException {
		final String methodName = "getAnyThingPage";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Map<String, Object> resultFromProcedure = null;
		Integer errorNum = null;
		Integer responseFromProc = null;
		List<ScreenSettings> anythingPageDetails = null;
		ScreenSettings anythingPage = null;
		Boolean menuExist = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiAnythingPageDetails");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.returningResultSet("anythingPageDetails",
					new BeanPropertyRowMapper<ScreenSettings>(
							ScreenSettings.class));
			final MapSqlParameterSource anythingPageParam = new MapSqlParameterSource();
			anythingPageParam.addValue("HcHubCitiID", objUser.getHubCitiID());
			anythingPageParam.addValue("AnythingPageID",
					objScreenSettings.getHiddenBtnLinkId());
			resultFromProcedure = simpleJdbcCall.execute(anythingPageParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);
			if (null != resultFromProcedure) {
				if (null != responseFromProc
						&& responseFromProc.intValue() == 0) {
					anythingPageDetails = (ArrayList<ScreenSettings>) resultFromProcedure
							.get("anythingPageDetails");
					anythingPage = anythingPageDetails.get(0);
					menuExist = (Boolean) resultFromProcedure
							.get("MenuItemExist");
					anythingPage.setMenuItemExist(menuExist);
				} else {
					errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ "  : errorNumber  : " + errorNum
							+ " errorMessage : " + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);
				}
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return anythingPage;
	}

	public String saveAppSite(AppSiteDetails appSiteDetails)
			throws HubCitiWebSqlException {
		final String methodName = "saveMenuTemplate";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String status = null;
		Map<String, Object> resultFromProcedure = null;
		Integer errorNum = null;
		Integer responseFromProc = null;
		Integer intDuplicateApSiteName = null;
		Integer appSiteId = null;
		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcHubCitiAppsiteCreation");
			final MapSqlParameterSource menuQueryParams = new MapSqlParameterSource();

			menuQueryParams.addValue("RetailLocationID",
					appSiteDetails.getRetLocId());
			menuQueryParams.addValue("UserID", appSiteDetails.getHcUserId());
			menuQueryParams.addValue("HcHubCitiID",
					appSiteDetails.getHubCityId());
			menuQueryParams.addValue("HcAppSiteName",
					appSiteDetails.getAppSiteName());

			resultFromProcedure = simpleJdbcCall.execute(menuQueryParams);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				intDuplicateApSiteName = (Integer) resultFromProcedure
						.get("DuplicateExist");

				if (null != intDuplicateApSiteName) {
					if (intDuplicateApSiteName.intValue() == 2) {
						status = ApplicationConstants.DUPLICATEAPPSITENAMETEXT;
					} else if (intDuplicateApSiteName.intValue() == 1) {
						status = ApplicationConstants.DUPLICATEAPPSITERETLOCTEXT;
					} else {

						appSiteId = (Integer) resultFromProcedure
								.get("HcAppSiteID");

						status = ApplicationConstants.SUCCESS + "," + appSiteId;
					}
				} else {
					status = ApplicationConstants.FAILURE;
				}

			} else {
				errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
						+ "  : errorNumber  : " + errorNum + " errorMessage : "
						+ errorMsg);
				status = ApplicationConstants.FAILURE;
				throw new HubCitiWebSqlException(errorMsg);

			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	/**
	 * This Method will update anything page details.
	 * 
	 * @param objScreenSettings
	 * @param user
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String deleteAnyThingPage(String anythingPageID, User objUser)
			throws HubCitiWebSqlException {

		final String methodName = "deleteAnyThingPage";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;
		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiAnythingPageDeletion");
			final MapSqlParameterSource anythingPageQueryParams = new MapSqlParameterSource();

			anythingPageQueryParams.addValue("AnythingPageID", anythingPageID);
			anythingPageQueryParams.addValue("HcHubCitiID",
					objUser.getHubCitiID());
			anythingPageQueryParams.addValue("UserID",
					objUser.gethCAdminUserID());

			resultFromProcedure = simpleJdbcCall
					.execute(anythingPageQueryParams);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				status = ApplicationConstants.SUCCESS;
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	@SuppressWarnings("unchecked")
	public ScreenSettings fetchGeneralSettings(Integer hubCitiID,
			String settingType) throws HubCitiWebSqlException {
		final String methodName = "fetchGeneralSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		List<ScreenSettings> screenDetailsList = null;
		ScreenSettings screenSettings = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.withProcedureName("usp_WebHcGeneralSettingsDisply");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.returningResultSet("generalSettings",
					new BeanPropertyRowMapper<ScreenSettings>(
							ScreenSettings.class));
			final MapSqlParameterSource loginScreenDetailsParam = new MapSqlParameterSource();
			loginScreenDetailsParam.addValue("HubCitiID", hubCitiID);
			loginScreenDetailsParam.addValue("SettingsType", settingType);

			resultFromProcedure = simpleJdbcCall
					.execute(loginScreenDetailsParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				screenDetailsList = (ArrayList<ScreenSettings>) resultFromProcedure
						.get("generalSettings");
				if (null != screenDetailsList && !screenDetailsList.isEmpty()) {
					screenSettings = screenDetailsList.get(0);
				}
			}
		} catch (DataAccessException exception) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ exception.getStackTrace());
			throw new HubCitiWebSqlException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return screenSettings;
	}

	@SuppressWarnings("unchecked")
	public List<Category> fetchBusinessCategoryList()
			throws HubCitiWebSqlException {
		final String methodName = "fetchBusinessCategoryList";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer keyCategoryId = 0;
		List<Category> categoryList = null;
		HashMap<Integer, Category> categoryMap = new HashMap<Integer, Category>();
		Category ObjCategory = null;
		ArrayList<SubCategory> subCategoriesLst = null;
		List<Category> businessCateLst = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcRetrieveRetailerBusinessCategoryList");
			simpleJdbcCall.returningResultSet("categoryList",
					new BeanPropertyRowMapper<Category>(Category.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {
				if (null == resultFromProcedure.get("ErrorNumber")) {
					categoryList = (ArrayList<Category>) resultFromProcedure
							.get("categoryList");
					// grouping of category and subcategory list.
					if (null != categoryList && !categoryList.isEmpty())

					{
						for (Category category : categoryList) {
							keyCategoryId = category.getCatId();

							if (null != keyCategoryId) {
								if (categoryMap.containsKey(keyCategoryId)) {
									ObjCategory = categoryMap
											.get(keyCategoryId);
									subCategoriesLst = ObjCategory
											.getSubArrayList();

									if (null != subCategoriesLst) {
										SubCategory subCategory = new SubCategory();
										subCategory.setSubCatId(category
												.getSubCatId());
										subCategory.setSubCatName(category
												.getSubCatName());
										subCategoriesLst.add(subCategory);
										ObjCategory
												.setSubArrayList(subCategoriesLst);
									}

								} else {

									ObjCategory = new Category();
									ObjCategory.setCatId(category.getCatId());
									ObjCategory.setCatName(category
											.getCatName());
									subCategoriesLst = new ArrayList<SubCategory>();
									SubCategory subCategory = new SubCategory();
									subCategory.setSubCatId(category
											.getSubCatId());
									subCategory.setSubCatName(category
											.getSubCatName());
									subCategoriesLst.add(subCategory);
									ObjCategory
											.setSubArrayList(subCategoriesLst);

								}

								categoryMap.put(keyCategoryId, ObjCategory);

							}

						}

						Set<Map.Entry<Integer, Category>> categorySet = categoryMap
								.entrySet();
						businessCateLst = new ArrayList<Category>();
						for (Entry<Integer, Category> entry : categorySet) {

							businessCateLst.add(entry.getValue());

						}

						// for displaying categories in alphabetical order.
						SortFindCategory sortFindCategory = new SortFindCategory();
						Collections.sort(businessCateLst, sortFindCategory);

					}

				} else {
					Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ "  : errorNumber  : " + errorNum
							+ " errorMessage : " + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);
				}
			}

			LOG.info("usp_WebHcRetrieveRetailerBusinessCategoryList is  executed Successfully.");
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return businessCateLst;
	}

	public String addAlertCategory(String catName, User objUser)
			throws HubCitiWebSqlException {
		String methodName = "addAlertCategory";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		boolean bCatExists;
		String strResponse = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiAlertsCategoryCreation");
			final MapSqlParameterSource addAlertCategoryParams = new MapSqlParameterSource();

			addAlertCategoryParams.addValue("UserID",
					objUser.gethCAdminUserID());
			addAlertCategoryParams.addValue("HcHubCitiID",
					objUser.getHubCitiID());
			addAlertCategoryParams.addValue("HcAlertCategoryName", catName);

			resultFromProcedure = simpleJdbcCall
					.execute(addAlertCategoryParams);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				bCatExists = (Boolean) resultFromProcedure.get("DuplicateFlag");
				if (bCatExists == true) {
					strResponse = ApplicationConstants.ALERTCATEXISTS;
				} else {
					Integer catId = (Integer) resultFromProcedure
							.get("AlertCategoryID");
					strResponse = catId.toString();
				}

			} else {
				strResponse = ApplicationConstants.FAILURE;
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	/**
	 * 
	 */
	public AlertCategory fetchAlertCategories(Category objCategory, User objUser)
			throws HubCitiWebSqlException {
		final String methodName = "fetchAlertCategories";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer iRowcount = null;
		List<Category> categoryList = null;
		AlertCategory objAlertCategory = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiAlertsCategoryDisplay");
			simpleJdbcCall.returningResultSet("categoryList",
					new BeanPropertyRowMapper<Category>(Category.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HubCitiID", objUser.getHubCitiID());
			param.addValue("CategoryName", objCategory.getCatName());
			param.addValue("LowerLimit", objCategory.getLowerLimit());
			param.addValue("ScreenName",
					ApplicationConstants.HUBCITIWEBPAGINATION);

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {

				iRowcount = (Integer) resultFromProcedure.get("MaxCnt");
				categoryList = (ArrayList<Category>) resultFromProcedure
						.get("categoryList");
				if (null != categoryList && !categoryList.isEmpty()) {
					objAlertCategory = new AlertCategory();
					objAlertCategory.setAlertCatLst(categoryList);
					objAlertCategory.setTotalSize(iRowcount);

				}
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return objAlertCategory;
	}

	public String deleteAlertCategory(int cateId, User objUser)
			throws HubCitiWebSqlException {
		String methodName = "deleteAlertCategory";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		boolean bCatExists;
		String strResponse = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiAlertsCategoryDeletion");
			final MapSqlParameterSource addAlertCategoryParams = new MapSqlParameterSource();

			addAlertCategoryParams
					.addValue("HubCitiID", objUser.getHubCitiID());
			addAlertCategoryParams.addValue("HcAlertCategoryID", cateId);

			resultFromProcedure = simpleJdbcCall
					.execute(addAlertCategoryParams);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				/*
				 * bCatExists = (Boolean)
				 * resultFromProcedure.get("DuplicateFlag"); if (bCatExists ==
				 * true) { strResponse = ApplicationConstants.ALERTCATEXISTS; }
				 * else
				 */
				{
					strResponse = ApplicationConstants.SUCCESS;
				}

			} else {
				strResponse = ApplicationConstants.FAILURE;
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	public String updateAlertCategory(Category objCategory, User objUser)
			throws HubCitiWebSqlException {
		String methodName = "updateAlertCategory";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		boolean bCatExists;
		String strResponse = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiAlertsCategoryUpdation");
			final MapSqlParameterSource addAlertCategoryParams = new MapSqlParameterSource();

			addAlertCategoryParams
					.addValue("HubCitiID", objUser.getHubCitiID());
			addAlertCategoryParams.addValue("UserID",
					objUser.gethCAdminUserID());
			addAlertCategoryParams.addValue("HcAlertCategoryID",
					objCategory.getCatId());
			addAlertCategoryParams.addValue("HcAlertCategoryName",
					objCategory.getCatName());

			resultFromProcedure = simpleJdbcCall
					.execute(addAlertCategoryParams);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				bCatExists = (Boolean) resultFromProcedure
						.get("DuplicateCategory");
				if (bCatExists == true) {
					strResponse = ApplicationConstants.ALERTCATEXISTS;
				} else {
					strResponse = ApplicationConstants.SUCCESS;
				}

			} else {
				strResponse = ApplicationConstants.FAILURE;
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	@SuppressWarnings("unchecked")
	public MenuFilterTyes getMenuFilterTypes(int hubCitiId)
			throws HubCitiWebSqlException {
		final String methodName = "getMenuFilterTypes";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		MenuFilterTyes menuFilterTyes = new MenuFilterTyes();
		List<Department> deptList = null;
		List<Type> typeList = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiDepartmentAndTypeDisplay");
			simpleJdbcCall.returningResultSet("deptList",
					new BeanPropertyRowMapper<Department>(Department.class));
			simpleJdbcCall.returningResultSet("typeList",
					new BeanPropertyRowMapper<Type>(Type.class));
			final MapSqlParameterSource linkParam = new MapSqlParameterSource();
			linkParam.addValue("HubCitiID", hubCitiId);
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(linkParam);
			if (null != resultFromProcedure) {
				if (null == resultFromProcedure.get("ErrorNumber")) {
					deptList = (ArrayList<Department>) resultFromProcedure
							.get("deptList");
					typeList = (ArrayList<Type>) resultFromProcedure
							.get("typeList");

					if (null != deptList) {

						menuFilterTyes.setDeptNameList(deptList);
					} else {

						deptList = new ArrayList<Department>();
						menuFilterTyes.setDeptNameList(deptList);
					}
					if (null != typeList) {

						menuFilterTyes.setTypeNameList(typeList);
					} else {

						typeList = new ArrayList<Type>();
						menuFilterTyes.setTypeNameList(typeList);
					}
				} else {
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ methodName + "Error number: {}" + errorNum
							+ " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);
				}
			}

			LOG.info("usp_WebHcHubCitiDepartmentAndTypeDisplay is  executed Successfully.");
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return menuFilterTyes;
	}

	@SuppressWarnings("unchecked")
	public List<Severity> fetchAlertSeverities() throws HubCitiWebSqlException {
		final String methodName = "fetchAlertSeverities";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<Severity> severityList = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiAlertsSeverityDisplay");
			simpleJdbcCall.returningResultSet("SeverityList",
					new BeanPropertyRowMapper<Severity>(Severity.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {
				severityList = (ArrayList<Severity>) resultFromProcedure
						.get("SeverityList");
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
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
	 * @throws HubCitiWebSqlException
	 */
	@SuppressWarnings("unchecked")
	public AlertsDetails displaySearchAlerts(Integer userId, Integer hubCitiId,
			String searchKey, Integer lowerLimit) throws HubCitiWebSqlException {
		final String methodName = "displaySearchAlerts";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Integer responseFromProc = null;
		AlertsDetails alertsDetails = null;
		List<Alerts> alerts = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcHubCitiAlertsDisplay");
			simpleJdbcCall.returningResultSet("alerts",
					new BeanPropertyRowMapper<Alerts>(Alerts.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HubCitiID", hubCitiId);
			param.addValue("UserID", userId);
			param.addValue("SearchParameter", searchKey);
			param.addValue("LowerLimit", lowerLimit);
			param.addValue("ScreenName",
					ApplicationConstants.HUBCITIWEBPAGINATION);

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				alertsDetails = new AlertsDetails();
				alerts = (ArrayList<Alerts>) resultFromProcedure.get("alerts");
				alertsDetails.setAlerts(alerts);
				Integer totalSize = (Integer) resultFromProcedure.get("MaxCnt");
				alertsDetails.setTotalSize(totalSize);
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);

			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return alertsDetails;

	}

	/**
	 * This method will save alter details.
	 * 
	 * @param alerts
	 * @param objUser
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String saveAlerts(Alerts alerts, User objUser)
			throws HubCitiWebSqlException {
		final String methodName = "saveAlerts";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;
		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.withProcedureName("usp_WebHcHubCitiAlertsCreation");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			final MapSqlParameterSource alertsParam = new MapSqlParameterSource();
			alertsParam.addValue("UserID", objUser.gethCAdminUserID());
			alertsParam.addValue("HcHubCitiID", objUser.getHubCitiID());
			alertsParam.addValue("HcAlertName", alerts.getTitle());
			alertsParam.addValue("Description", alerts.getDescription());
			alertsParam.addValue("HcAlertCategoryID", alerts.getCategoryId());
			alertsParam.addValue("HcSeverityID", alerts.getSeverityId());
			if ("".equalsIgnoreCase(alerts.getStartDate())) {
				alertsParam.addValue("StartDate", null);
			} else {
				alertsParam.addValue("StartDate",
						Utility.getFormattedDateTime(alerts.getStartDate()));
			}
			if ("".equalsIgnoreCase(alerts.getEndDate())) {
				alertsParam.addValue("EndDate", null);
			} else {
				alertsParam.addValue("EndDate",
						Utility.getFormattedDateTime(alerts.getEndDate()));
			}
			if ("".equalsIgnoreCase(alerts.getStartTime())) {
				alertsParam.addValue("StartTime", null);
			} else {
				alertsParam.addValue("StartTime", alerts.getStartTime());
			}
			if ("".equalsIgnoreCase(alerts.getEndTime())) {
				alertsParam.addValue("EndTime", null);
			} else {
				alertsParam.addValue("EndTime", alerts.getEndTime());
			}

			resultFromProcedure = simpleJdbcCall.execute(alertsParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != resultFromProcedure && responseFromProc == 0) {
				status = ApplicationConstants.SUCCESS;
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		} catch (ParseException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	/**
	 * This method will return alter details.
	 * 
	 * @param alertID
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	@SuppressWarnings("unchecked")
	public Alerts fetchAlertDetails(Integer alertId)
			throws HubCitiWebSqlException {
		final String methodName = "fetchAlertDetails";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		List<Alerts> alerts = null;
		Alerts alertDetails = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.withProcedureName("usp_WebHcHubCitiAlertsDetails");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.returningResultSet("alerts",
					new BeanPropertyRowMapper<Alerts>(Alerts.class));
			final MapSqlParameterSource alertsParam = new MapSqlParameterSource();
			alertsParam.addValue("HcAlertID", alertId);
			resultFromProcedure = simpleJdbcCall.execute(alertsParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);
			if (null != resultFromProcedure && responseFromProc == 0) {
				alerts = (ArrayList<Alerts>) resultFromProcedure.get("alerts");
				if (null != alerts && !alerts.isEmpty()) {
					alertDetails = alerts.get(0);
				}
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return alertDetails;
	}

	/**
	 * This method will update alter details.
	 * 
	 * @param alerts
	 * @param objUser
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String updateAlerts(Alerts alerts, User objUser)
			throws HubCitiWebSqlException {
		final String methodName = "updateAlerts";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;
		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.withProcedureName("usp_WebHcHubCitiAlertsUpdation");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			final MapSqlParameterSource alertsParam = new MapSqlParameterSource();
			alertsParam.addValue("UserID", objUser.gethCAdminUserID());
			alertsParam.addValue("HcHubCitiID", objUser.getHubCitiID());
			alertsParam.addValue("HcAlertID", alerts.getAlertId());
			alertsParam.addValue("HcAlertName", alerts.getTitle());
			alertsParam.addValue("Description", alerts.getDescription());
			alertsParam.addValue("HcAlertCategoryID", alerts.getCategoryId());
			alertsParam.addValue("HcSeverityID", alerts.getSeverityId());
			if ("".equalsIgnoreCase(alerts.getStartDate())) {
				alertsParam.addValue("StartDate", null);
			} else {
				alertsParam.addValue("StartDate",
						Utility.getFormattedDateTime(alerts.getStartDate()));
			}
			if ("".equalsIgnoreCase(alerts.getEndDate())) {
				alertsParam.addValue("EndDate", null);
			} else {
				alertsParam.addValue("EndDate",
						Utility.getFormattedDateTime(alerts.getEndDate()));
			}
			if ("".equalsIgnoreCase(alerts.getStartTime())) {
				alertsParam.addValue("StartTime", null);
			} else {
				alertsParam.addValue("StartTime", alerts.getStartTime());
			}
			if ("".equalsIgnoreCase(alerts.getEndTime())) {
				alertsParam.addValue("EndTime", null);
			} else {
				alertsParam.addValue("EndTime", alerts.getEndTime());
			}

			resultFromProcedure = simpleJdbcCall.execute(alertsParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != resultFromProcedure && responseFromProc == 0) {
				status = ApplicationConstants.SUCCESS;
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		} catch (ParseException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
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
	 * @throws HubCitiWebSqlException
	 */
	public String deleteAlerts(Integer alertID, User objUser)
			throws HubCitiWebSqlException {

		final String methodName = "deleteAlerts";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;
		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcHubCitiAlertsDeletion");
			final MapSqlParameterSource anythingPageQueryParams = new MapSqlParameterSource();

			anythingPageQueryParams.addValue("HcAlertID", alertID);
			anythingPageQueryParams.addValue("HubCitiID",
					objUser.getHubCitiID());

			resultFromProcedure = simpleJdbcCall
					.execute(anythingPageQueryParams);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				status = ApplicationConstants.SUCCESS;
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	@SuppressWarnings("unchecked")
	public List<ScreenSettings> displayButtomBtnType()
			throws HubCitiWebSqlException {
		final String methodName = "displayButtomBtnType";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		List<ScreenSettings> screenDetailsList = new ArrayList<ScreenSettings>();

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiBottomButtonTypeDisplay");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.returningResultSet("screenSettings",
					new BeanPropertyRowMapper<ScreenSettings>(
							ScreenSettings.class));
			final MapSqlParameterSource ButtomBtnParam = new MapSqlParameterSource();

			resultFromProcedure = simpleJdbcCall.execute(ButtomBtnParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				screenDetailsList = (ArrayList<ScreenSettings>) resultFromProcedure
						.get("screenSettings");
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException exception) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ exception.getStackTrace());
			throw new HubCitiWebSqlException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return screenDetailsList;
	}

	@SuppressWarnings("unchecked")
	public AlertCategory fetchEventCategories(Category objCategory, User objUser)
			throws HubCitiWebSqlException {
		final String methodName = "fetchEventCategories";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer iRowcount = null;
		List<Category> categoryList = null;
		AlertCategory objAlertCategory = null;
		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiEventsCategoryDisplay");
			simpleJdbcCall.returningResultSet("eventcategoryList",
					new BeanPropertyRowMapper<Category>(Category.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("UserID", objUser.gethCAdminUserID());
			param.addValue("CategoryName", null);
			param.addValue("LowerLimit", null);
			param.addValue("ScreenName",
					ApplicationConstants.HUBCITIWEBPAGINATION);
			param.addValue("EventCategoryID", null);
			param.addValue("RoleBasedUserID", objUser.getRoleUserId());

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {

				iRowcount = (Integer) resultFromProcedure.get("MaxCnt");
				categoryList = (ArrayList<Category>) resultFromProcedure
						.get("eventcategoryList");
				if (null != categoryList && !categoryList.isEmpty()) {
					objAlertCategory = new AlertCategory();
					objAlertCategory.setAlertCatLst(categoryList);
					objAlertCategory.setTotalSize(iRowcount);

				}
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return objAlertCategory;
	}

	public String addEventCategory(String catName, User objUser)
			throws HubCitiWebSqlException {
		String methodName = "addEventCategory";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		boolean bCatExists;
		String strResponse = null;

		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiEventsCategoryCreation");
			final MapSqlParameterSource addAlertCategoryParams = new MapSqlParameterSource();

			addAlertCategoryParams.addValue("UserID",
					objUser.gethCAdminUserID());
			addAlertCategoryParams.addValue("HcHubCitiID",
					objUser.getHubCitiID());
			addAlertCategoryParams.addValue("HcEventCategoryName", catName);

			resultFromProcedure = simpleJdbcCall
					.execute(addAlertCategoryParams);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				bCatExists = (Boolean) resultFromProcedure.get("DuplicateFlag");
				if (bCatExists == true) {
					strResponse = ApplicationConstants.ALERTCATEXISTS;
				} else {

					Integer catId = (Integer) resultFromProcedure
							.get("EventCategoryID");
					strResponse = String.valueOf(catId);

				}

			} else {
				strResponse = ApplicationConstants.FAILURE;
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	public String deleteEventCategory(int cateId, User user)
			throws HubCitiWebSqlException {
		String methodName = "deleteEventCategory";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		boolean bCatExists;
		String strResponse = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiEventsCategoryDeletion");
			final MapSqlParameterSource addAlertCategoryParams = new MapSqlParameterSource();

			addAlertCategoryParams.addValue("HubCitiID", user.getHubCitiID());
			addAlertCategoryParams.addValue("HcEventCategoryID", cateId);

			resultFromProcedure = simpleJdbcCall
					.execute(addAlertCategoryParams);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				/*
				 * bCatExists = (Boolean)
				 * resultFromProcedure.get("DuplicateFlag"); if (bCatExists ==
				 * true) { strResponse = ApplicationConstants.ALERTCATEXISTS; }
				 * else
				 */
				{
					strResponse = ApplicationConstants.SUCCESS;
				}

			} else {
				strResponse = ApplicationConstants.FAILURE;
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	public String updateEventCategory(Category objCategory, User objUser)
			throws HubCitiWebSqlException {
		String methodName = "updateEventCategory";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		boolean bCatExists;
		String strResponse = null;

		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiEventsCategoryUpdation");
			final MapSqlParameterSource addAlertCategoryParams = new MapSqlParameterSource();

			addAlertCategoryParams
					.addValue("HubCitiID", objUser.getHubCitiID());
			addAlertCategoryParams.addValue("UserID",
					objUser.gethCAdminUserID());
			addAlertCategoryParams.addValue("HcEventCategoryID",
					objCategory.getCatId());
			addAlertCategoryParams.addValue("HcEventCategoryName",
					objCategory.getCatName());

			resultFromProcedure = simpleJdbcCall
					.execute(addAlertCategoryParams);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				bCatExists = (Boolean) resultFromProcedure
						.get("DuplicateCategory");
				if (bCatExists == true) {
					strResponse = ApplicationConstants.ALERTCATEXISTS;
				} else {
					strResponse = ApplicationConstants.SUCCESS;
				}

			} else {
				strResponse = ApplicationConstants.FAILURE;
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	public EventDetail displayEvents(Event event, User objUser, Boolean fundraising)
			throws HubCitiWebSqlException {
		final String methodName = "fetchEventCategories";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer iRowcount = null;
		List<Event> eventLst = null;
		EventDetail objEventDetail = null;
		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			if (ApplicationConstants.FUNDRAISERSCREENNAME.equals(event
					.getScreenName())) {
				simpleJdbcCall
						.withProcedureName("usp_WebHcHubCitiFundraisingEventsDisplay");
			} else {
				simpleJdbcCall
						.withProcedureName("usp_WebHcHubCitiEventsDisplay");
			}
			simpleJdbcCall.returningResultSet("eventList",
					new BeanPropertyRowMapper<Event>(Event.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HubCitiID", objUser.getHubCitiID());
			param.addValue("UserID", objUser.gethCAdminUserID());
			param.addValue("Searchparameter", event.getEventSearchKey());
			param.addValue("LowerLimit", event.getLowerLimit());
			param.addValue("ScreenName", ApplicationConstants.HUBCITIWEBPAGINATION);
			param.addValue("Fundraising", fundraising);

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {

				iRowcount = (Integer) resultFromProcedure.get("MaxCnt");
				eventLst = (ArrayList<Event>) resultFromProcedure
						.get("eventList");
				if (null != eventLst && !eventLst.isEmpty()) {
					objEventDetail = new EventDetail();
					objEventDetail.setEventLst(eventLst);
					objEventDetail.setTotalSize(iRowcount);
				}
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return objEventDetail;
	}

	public String deleteEvent(Integer eventId, User user)
			throws HubCitiWebSqlException {
		String methodName = "deleteEvent";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;

		String strResponse = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			if (ApplicationConstants.FUNDRAISERSCREENNAME.equals(user
					.getModule())) {
				simpleJdbcCall
						.withProcedureName("usp_WebHcHubCitiFundraisingEventsDeletion");
			} else {
				simpleJdbcCall
						.withProcedureName("usp_WebHcHubCitiEventsDeletion");
			}
			final MapSqlParameterSource addAlertCategoryParams = new MapSqlParameterSource();

			addAlertCategoryParams.addValue("HubCitiID", user.getHubCitiID());
			addAlertCategoryParams.addValue("HcEventID", eventId);

			resultFromProcedure = simpleJdbcCall
					.execute(addAlertCategoryParams);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				strResponse = ApplicationConstants.SUCCESS;
			} else {
				strResponse = ApplicationConstants.FAILURE;
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	public List<SearchZipCode> getZipStateCity(String zipCode, Integer hubCitiId)
			throws HubCitiWebSqlException {
		String methodName = "getZipStateCity";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		List<SearchZipCode> zipCodes = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcGetCityStateListByPostalCode");
			final MapSqlParameterSource addData = new MapSqlParameterSource();
			simpleJdbcCall.returningResultSet("details", new BeanPropertyRowMapper<SearchZipCode>(SearchZipCode.class));
			
			addData.addValue("PostalCode", zipCode);
			addData.addValue("HcHubCitiID", hubCitiId);
			
			Map<String, Object> resultFromProcedure = simpleJdbcCall.execute(addData);
			Integer responseFromProc = (Integer) resultFromProcedure.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc == 0) {
				zipCodes = (List<SearchZipCode>) resultFromProcedure.get("details");
			} 
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return zipCodes;
	}

	public List<SearchZipCode> getCityStateZip(String city, Integer hubCitiId)
			throws HubCitiWebSqlException {
		String methodName = "getCityStateZip";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		List<SearchZipCode> cities = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcGetCityStateListByCity");
			final MapSqlParameterSource addData = new MapSqlParameterSource();
			simpleJdbcCall.returningResultSet("details", new BeanPropertyRowMapper<SearchZipCode>(SearchZipCode.class));
			
			addData.addValue("City", city);
			addData.addValue("HcHubCitiID", hubCitiId);
			
			Map<String, Object> resultFromProcedure = simpleJdbcCall.execute(addData);
			Integer responseFromProc = (Integer) resultFromProcedure.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc == 0) {
				cities = (List<SearchZipCode>) resultFromProcedure.get("details");
			} 
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return cities;
	}

	/**
	 * The DAOImpl method for displaying all the states and it return to service
	 * method.
	 * 
	 * @throws ScanSeeWebSqlException
	 *             as SQL Exception will be thrown.
	 * @throws ScanSeeWebSqlException
	 *             as SQL Exception will be thrown.
	 * @return arStatesList,List of states.
	 */
	public List<State> getAllStates(Integer hubCitiId)
			throws HubCitiWebSqlException {
		String methodName = "getZipStateCity";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		List<State> arStatesList = null;
		try {
			arStatesList = this.jdbcTemplate
					.query("select DISTINCT G.State as Stateabbrevation, S.StateName "
							+ "from GeoPosition G INNER JOIN State S ON S.Stateabbrevation = G.State "
							+ "INNER JOIN HcLocationAssociation HA ON HA.City = G.City AND HA.State = G.State AND HA.PostalCode = G.PostalCode "
							+ "where G.State = s.Stateabbrevation AND HA.HcHubCitiID ="
							+ hubCitiId, new RowMapper<State>() {
						public State mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							final State state = new State();
							state.setStateName(rs.getString("StateName"));
							state.setStateabbr(rs.getString("Stateabbrevation"));

							return state;
						}

					});
		} catch (EmptyResultDataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED, e);
			throw new HubCitiWebSqlException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return (List<State>) arStatesList;
	}

	public List<RetailLocation> getHotelList(Integer hubCitiId, String searchKey)
			throws HubCitiWebSqlException

	{
		final String methodName = "getHotelList";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer responseFromProc = null;
		List<RetailLocation> hotelList = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcHubCitiHotelList");
			simpleJdbcCall.returningResultSet("hotelList",
					new BeanPropertyRowMapper<RetailLocation>(
							RetailLocation.class));
			final MapSqlParameterSource linkParam = new MapSqlParameterSource();
			linkParam.addValue("HubCitiID", hubCitiId);
			linkParam.addValue("SearchKey", searchKey);
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(linkParam);

			if (null != resultFromProcedure) {

				responseFromProc = (Integer) resultFromProcedure
						.get(ApplicationConstants.STATUS);

				if (null != responseFromProc
						&& responseFromProc.intValue() == 0) {
					hotelList = (ArrayList<RetailLocation>) resultFromProcedure
							.get("hotelList");
				} else {
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ methodName + "Error number: {}" + errorNum
							+ " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);
				}
			}

			LOG.info("usp_WebHcHubCitiHotelList is  executed Successfully.");
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return hotelList;
	}

	public String saveUpdateEventDeatils(Event eventDetails, User user)
			throws HubCitiWebSqlException {
		String methodName = "saveEventDeatils";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String response = null;
		Integer responseFromProc = null;
		String days = Arrays.toString(eventDetails.getDays());
		Integer eventId = null;

		if ("null".equalsIgnoreCase(days)) {
			days = null;
		}
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			final MapSqlParameterSource queryParams = new MapSqlParameterSource();

			if (null == eventDetails.getHcEventID()
					|| "".equals(eventDetails.getHcEventID())) {
				simpleJdbcCall
						.withProcedureName("usp_WebHcHubCitiEventsCreation");
			} else {
				simpleJdbcCall
						.withProcedureName("usp_WebHcHubCitiEventUpdation");
				queryParams.addValue("HcEventID", eventDetails.getHcEventID());
			}

			queryParams.addValue("UserID", user.gethCAdminUserID());
			queryParams.addValue("HcHubCitiID", user.getHubCitiID());
			queryParams.addValue("HcEventName", eventDetails.getHcEventName());
			queryParams.addValue("ShortDescription",
					eventDetails.getShortDescription());

			if (null != eventDetails.getLongDescription()
					&& !"".equals(eventDetails.getLongDescription())) {
				queryParams.addValue("LongDescription",
						eventDetails.getLongDescription());
			} else {
				queryParams.addValue("LongDescription", null);
			}

			queryParams.addValue("HcEventCategoryID",
					eventDetails.getEventCategory());
			queryParams.addValue("ImagePath", eventDetails.getEventImageName());

			if (null != eventDetails.getMoreInfoURL()
					&& !"".equals(eventDetails.getMoreInfoURL())) {
				queryParams.addValue("MoreInformationURL",
						eventDetails.getMoreInfoURL());
			} else {
				queryParams.addValue("MoreInformationURL", null);
			}

			if ("yes".equalsIgnoreCase(eventDetails.getIsOngoing())) {
				queryParams.addValue("StartDate",
						eventDetails.getEventStartDate());
				queryParams.addValue("StartTime",
						eventDetails.getEventStartTime());
				queryParams.addValue("OngoingEvent", true);
				queryParams.addValue("EndTime", eventDetails.getEventEndTime());
				queryParams.addValue("EndDate", eventDetails.getEventEndDate());
				queryParams.addValue("RecurrencePatternID",
						eventDetails.getRecurrencePatternID());
				queryParams.addValue("RecurrenceInterval",
						eventDetails.getRecurrenceInterval());
				queryParams.addValue("EveryWeekday",
						eventDetails.getIsWeekDay());
				queryParams.addValue("Days", days);
				queryParams.addValue("DayNumber", eventDetails.getDayNumber());
				queryParams.addValue("EndAfter", eventDetails.getEndAfter());
			} else {
				queryParams.addValue("StartDate", eventDetails.getEventDate());
				queryParams.addValue("StartTime", eventDetails.getEventTime());
				queryParams.addValue("OngoingEvent", false);
				queryParams.addValue("EndTime", eventDetails.getEventETime());
				queryParams.addValue("EndDate", eventDetails.getEventEDate());
				queryParams.addValue("RecurrencePatternID", null);
				queryParams.addValue("RecurrenceInterval", null);
				queryParams.addValue("EveryWeekday", null);
				queryParams.addValue("Days", null);
				queryParams.addValue("EndAfter", null);
				queryParams.addValue("DayNumber", null);
			}

			if (null != eventDetails.getBsnsLoc()
					&& !"".equals(eventDetails.getBsnsLoc())) {
				if ("yes".equals(eventDetails.getBsnsLoc())) {
					queryParams.addValue("BussinessEvent", true);
					if (null != eventDetails.getAppsiteID()
							&& eventDetails.getAppsiteID().length > 0) {
						String appSiteId = new String();
						for (int i = 0; i < eventDetails.getAppsiteID().length; i++) {
							if (i == 0) {
								appSiteId = eventDetails.getAppsiteID()[i];
							} else {
								appSiteId = appSiteId + ","
										+ eventDetails.getAppsiteID()[i];
							}
						}
						queryParams.addValue("HcAppsiteID", appSiteId);
					} else {
						queryParams.addValue("HcAppsiteID", null);
					}
					queryParams.addValue("Address", null);
					queryParams.addValue("City", null);
					queryParams.addValue("State", null);
					queryParams.addValue("PostalCode", null);
					queryParams.addValue("Latitude", null);
					queryParams.addValue("Longitude", null);
					queryParams.addValue("GeoErrorFlag", false);
					queryParams.addValue("EventLocationTitle", null);
				} else if ("no".equals(eventDetails.getBsnsLoc())) {
					queryParams.addValue("BussinessEvent", false);
					queryParams.addValue("HcAppsiteID", null);
					queryParams.addValue("Address", eventDetails.getAddress());
					queryParams.addValue("City", eventDetails.getCity());
					queryParams.addValue("State", eventDetails.getState());
					queryParams.addValue("PostalCode",
							eventDetails.getPostalCode());
					queryParams
							.addValue("Latitude", eventDetails.getLatitude());
					queryParams.addValue("Longitude",
							eventDetails.getLogitude());
					queryParams.addValue("GeoErrorFlag",
							eventDetails.isGeoError());
					queryParams.addValue("EventLocationTitle",
							eventDetails.getLocationTitle());
				}
			} else {
				queryParams.addValue("BussinessEvent", false);
			}

			if (null != eventDetails.getEvntPckg()
					&& !"".equals(eventDetails.getEvntPckg())) {
				if ("yes".equals(eventDetails.getEvntPckg())) {
					queryParams.addValue("PackageEvent", true);
				} else if ("no".equals(eventDetails.getEvntPckg())) {
					queryParams.addValue("PackageEvent", false);
				}
			} else {
				queryParams.addValue("PackageEvent", false);
			}

			queryParams.addValue("PackageDescription",
					eventDetails.getPackageDescription());
			queryParams.addValue("PackageTicketURL",
					eventDetails.getPackageTicketURL());
			if (null == eventDetails.getPackagePrice()
					|| "".equals(eventDetails.getPackagePrice())) {
				queryParams.addValue("PackagePrice", null);
			} else {
				queryParams.addValue("PackagePrice",
						eventDetails.getPackagePrice());
			}

			queryParams.addValue("RetailLocationID", eventDetails.getHotelID());
			if (null != eventDetails.getEvntHotel()
					&& !"".equals(eventDetails.getEvntHotel())) {
				if ("yes".equals(eventDetails.getEvntHotel())) {
					queryParams.addValue("HotelEvent", true);
				} else if ("no".equals(eventDetails.getEvntHotel())) {
					queryParams.addValue("HotelEvent", false);
				}
			} else {
				queryParams.addValue("HotelEvent", false);
			}

			queryParams.addValue("HotelPrice", eventDetails.getHotelPrice());
			queryParams
					.addValue("DiscountCode", eventDetails.getDiscountCode());
			queryParams.addValue("DiscountAmount",
					eventDetails.getDiscountAmount());
			queryParams.addValue("Rating", eventDetails.getRating());
			queryParams.addValue("RoomAvailabilityCheckURL",
					eventDetails.getRoomAvailabilityCheckURL());
			queryParams.addValue("RoomBookingURL",
					eventDetails.getRoomBookingURL());

			if ("yes".equals(eventDetails.getIsEventLogistics())) {
				queryParams.addValue("EventsLogisticFlag", true);
				queryParams.addValue("EventsLogisticImagePath",
						eventDetails.getLogisticsImgName());
				if ("yes".equals(eventDetails.getIsEventOverlay())) {
					queryParams.addValue("EventsIsOverLayFlag", true);
				} else {
					queryParams.addValue("EventsIsOverLayFlag", false);
				}
				queryParams.addValue("ButtonName", eventDetails.getBtnNames());
				queryParams.addValue("ButtonLink", eventDetails.getBtnLinks());
			} else {
				queryParams.addValue("EventsLogisticFlag", false);
				queryParams.addValue("EventsLogisticImagePath", null);
				queryParams.addValue("EventsIsOverLayFlag", null);
				queryParams.addValue("ButtonName", null);
				queryParams.addValue("ButtonLink", null);
			}
			

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(queryParams);

			if (null != resultFromProcedure) {
				responseFromProc = (Integer) resultFromProcedure
						.get(ApplicationConstants.STATUS);

				if (null != responseFromProc
						&& responseFromProc.intValue() == 0) {
					if (null == eventDetails.getHcEventID()
							|| "".equals(eventDetails.getHcEventID())) {
						eventId = (Integer) resultFromProcedure
								.get("HcEventID");
					} else {
						eventId = eventDetails.getHcEventID();
					}
					response = ApplicationConstants.SUCCESS + ":" + eventId;
				} else {
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ methodName + "Error number: {}" + errorNum
							+ " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);
				}
			}

		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return response;
	}

	@SuppressWarnings("unchecked")
	public CityExperienceDetail displayCityExperience(
			CityExperience cityExperience, User user, Integer lowerLimit)
			throws HubCitiWebSqlException {
		final String methodName = "displayCityExperience";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		List<CityExperience> cityExperienceLst = null;
		CityExperienceDetail objCityExperienceDetail = null;
		String strCityExpName = null;
		Integer iCityExpId = null;
		Integer iTotalSize = null;
		try {
			if (null == cityExperience.getLowerLimit()
					|| "".equals(cityExperience.getLowerLimit()))
				;

			if (null == lowerLimit || "".equals(lowerLimit)) {
				lowerLimit = 0;
			}

			if ("".equals(cityExperience.getRetSearchKey())) {
				cityExperience.setRetSearchKey(null);
			}

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcCityExperienceRetailerList");
			simpleJdbcCall.returningResultSet("cityExplst",
					new BeanPropertyRowMapper<CityExperience>(
							CityExperience.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HubCitiID", user.getHubCitiID());
			param.addValue("SearchKey", cityExperience.getRetSearchKey());
			param.addValue("ScreenName",
					ApplicationConstants.HUBCITIWEBPAGINATION);
			param.addValue("LowerLimit", lowerLimit);

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {
				cityExperienceLst = (ArrayList<CityExperience>) resultFromProcedure
						.get("cityExplst");

				strCityExpName = (String) resultFromProcedure
						.get("CityExperienceName");
				iCityExpId = (Integer) resultFromProcedure
						.get("HcCityExperienceID");
				iTotalSize = (Integer) resultFromProcedure.get("MaxCnt");

				objCityExperienceDetail = new CityExperienceDetail();
				objCityExperienceDetail.setCityExpName(strCityExpName);
				objCityExperienceDetail.setCityExpId(iCityExpId);
				objCityExperienceDetail.setTotalSize(iTotalSize);
				if (null != cityExperienceLst && !cityExperienceLst.isEmpty()) {
					objCityExperienceDetail.setCityExpLst(cityExperienceLst);
				}
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return objCityExperienceDetail;
	}

	@SuppressWarnings("unchecked")
	public CityExperienceDetail searchCityExperience(String retName,
			Integer lowerLimit, Integer filterID, User user)
			throws HubCitiWebSqlException {
		final String methodName = "searchCityExperience";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		List<CityExperience> cityExperienceLst = null;
		CityExperienceDetail objCityExperienceDetail = null;
		Integer iTotalSize = null;
		try {
			/*
			 * if(null == lowerLimit || "".equals(lowerLimit)); { lowerLimit =0;
			 * }
			 */

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcCityExperienceRetailLocationSearch");
			simpleJdbcCall.returningResultSet("retLoclst",
					new BeanPropertyRowMapper<CityExperience>(
							CityExperience.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HubCitiID", user.getHubCitiID());
			param.addValue("FilterID", filterID);
			param.addValue("SearchKey", retName);
			param.addValue("LowerLimit", lowerLimit);
			param.addValue("ScreenName",
					ApplicationConstants.HUBCITIWEBPAGINATION);
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {
				cityExperienceLst = (ArrayList<CityExperience>) resultFromProcedure
						.get("retLoclst");

				iTotalSize = (Integer) resultFromProcedure.get("MaxCnt");

				if (null != cityExperienceLst && !cityExperienceLst.isEmpty()) {
					objCityExperienceDetail = new CityExperienceDetail();

					objCityExperienceDetail.setTotalSize(iTotalSize);
					objCityExperienceDetail.setCityExpLst(cityExperienceLst);

				}
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return objCityExperienceDetail;
	}

	public String saveCityExpRetLocs(String retLocIds, CityExperience cityExp,
			User user) throws HubCitiWebSqlException {
		final String methodName = "saveCityExpRetLocs";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String strResponse = null;
		Integer iStatus = null;
		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcCityExperienceUpdation");
			simpleJdbcCall.returningResultSet("retLoclst",
					new BeanPropertyRowMapper<CityExperience>(
							CityExperience.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HubCitiID", user.getHubCitiID());
			param.addValue("UserID", user.gethCAdminUserID());
			param.addValue("CityExperienceName", cityExp.getCityExpName());
			param.addValue("RetailLocationID", retLocIds);
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {
				iStatus = (Integer) resultFromProcedure.get("Status");
				if (null != iStatus && iStatus == 0) {
					strResponse = ApplicationConstants.SUCCESS;
				} else {
					strResponse = ApplicationConstants.FAILURE;
				}

			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	public String deleteRetLocation(CityExperience cityExperience)
			throws HubCitiWebSqlException {
		String methodName = "updateRetLocation";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		String strResponse = null;

		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcCityExperienceRetailLocationDeletion");
			final MapSqlParameterSource addAlertCategoryParams = new MapSqlParameterSource();

			addAlertCategoryParams.addValue("CitiExperienceID",
					cityExperience.getCityExpId());
			addAlertCategoryParams.addValue("RetailLocationID",
					cityExperience.getUnAssociRetLocId());

			resultFromProcedure = simpleJdbcCall
					.execute(addAlertCategoryParams);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				strResponse = ApplicationConstants.SUCCESS;
			} else {
				strResponse = ApplicationConstants.FAILURE;
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	@SuppressWarnings("unchecked")
	public Event fetchEventDetails(Integer eventId)
			throws HubCitiWebSqlException {
		final String methodName = "fetchEventDetails";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer iStatus = null;
		List<Event> eventList = null;
		Event eventDetails = null;
		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcHubCitiEventDetails");
			simpleJdbcCall.returningResultSet("eventDetails",
					new BeanPropertyRowMapper<Event>(Event.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HcEventID", eventId);

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {
				iStatus = (Integer) resultFromProcedure.get("Status");
				if (null != iStatus && iStatus == 0) {
					eventList = (ArrayList<Event>) resultFromProcedure
							.get("eventDetails");

					if (null != eventList && !eventList.isEmpty()) {
						eventDetails = eventList.get(0);
					}

				}

			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return eventDetails;
	}

	public List<RetailLocation> getEventHotelList(Integer eventId)
			throws HubCitiWebSqlException {
		final String methodName = "getEventHotelList";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer responseFromProc = null;
		List<RetailLocation> getEventHotelList = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiEventHotelsDisplay");
			simpleJdbcCall.returningResultSet("getEventHotelList",
					new BeanPropertyRowMapper<RetailLocation>(
							RetailLocation.class));
			final MapSqlParameterSource linkParam = new MapSqlParameterSource();
			linkParam.addValue("HcEventID", eventId);

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(linkParam);

			if (null != resultFromProcedure) {

				responseFromProc = (Integer) resultFromProcedure
						.get(ApplicationConstants.STATUS);

				if (null != responseFromProc
						&& responseFromProc.intValue() == 0) {
					getEventHotelList = (ArrayList<RetailLocation>) resultFromProcedure
							.get("getEventHotelList");
				} else {
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ methodName + "Error number: {}" + errorNum
							+ " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);
				}
			}

			LOG.info("usp_WebHcHubCitiHotelList is  executed Successfully.");
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
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
	 * @throws HubCitiWebSqlException
	 */
	@SuppressWarnings("unchecked")
	public FiltersDetails displayFilters(ScreenSettings filters, User user)
			throws HubCitiWebSqlException {
		final String methodName = "displayFilters";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<Filters> filtersLst = null;
		FiltersDetails filtersDetails = null;
		Integer responseFromProc = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcCityExperienceFiltersDisplay");
			simpleJdbcCall.returningResultSet("filterlst",
					new BeanPropertyRowMapper<Filters>(Filters.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HubCitiID", user.getHubCitiID());
			param.addValue("HcCityExperienceID", filters.getCityExperienceID());
			param.addValue("LowerLimit", filters.getLowerLimit());
			param.addValue("FilterName", filters.getSearchKey());
			param.addValue("ScreenName",
					ApplicationConstants.HUBCITIWEBPAGINATION);
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc == 0) {
				filtersLst = (ArrayList<Filters>) resultFromProcedure
						.get("filterlst");
				Integer totalSize = (Integer) resultFromProcedure.get("MaxCnt");
				filtersDetails = new FiltersDetails();
				filtersDetails.setFilters(filtersLst);
				filtersDetails.setTotalSize(totalSize);
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
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
	 * @throws HubCitiWebSqlException
	 */
	public String saveFilters(Filters filters, User user)
			throws HubCitiWebSqlException {
		final String methodName = "saveFilters";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String strResponse = null;
		Integer iStatus = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcCityExperienceFiltersCreationandUpdation");
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HcHubCitiID", user.getHubCitiID());
			param.addValue("UserID", user.gethCAdminUserID());
			param.addValue("HcFilterID", filters.getFilterID());
			param.addValue("FilterName", filters.getFilterName());
			param.addValue("HcCityExperienceID", filters.getCityExperienceID());
			param.addValue("ButtonImagePath", filters.getLogoImageName());
			param.addValue("RetailLocationID", filters.getRetailerLocIds());
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {
				iStatus = (Integer) resultFromProcedure.get("Status");
				Integer filterID = (Integer) resultFromProcedure
						.get("FilterID");
				Boolean duplicateFlag = (Boolean) resultFromProcedure
						.get("DuplicateFlag");

				if (null == filters.getFilterID()) {
					if (null != duplicateFlag && duplicateFlag == true) {
						strResponse = ApplicationConstants.DUPLICATEFILTER;
					} else if (null != iStatus && iStatus == 0) {
						strResponse = filterID.toString();
					} else {
						final Integer errorNum = (Integer) resultFromProcedure
								.get(ApplicationConstants.ERRORNUMBER);
						final String errorMsg = (String) resultFromProcedure
								.get(ApplicationConstants.ERRORMESSAGE);
						LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
								+ methodName + "Error number: {}" + errorNum
								+ " and error message: {}" + errorMsg);
						throw new HubCitiWebSqlException(errorMsg);
					}
				} else {
					if (null != iStatus && iStatus == 0) {
						strResponse = ApplicationConstants.SUCCESS;
					} else {
						strResponse = ApplicationConstants.FAILURE;
						final Integer errorNum = (Integer) resultFromProcedure
								.get(ApplicationConstants.ERRORNUMBER);
						final String errorMsg = (String) resultFromProcedure
								.get(ApplicationConstants.ERRORMESSAGE);
						LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
								+ methodName + "Error number: {}" + errorNum
								+ " and error message: {}" + errorMsg);
						throw new HubCitiWebSqlException(errorMsg);
					}
				}

			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	/**
	 * This method will return filter details.
	 * 
	 * @param hubCitiId
	 * @param filterId
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	@SuppressWarnings("unchecked")
	public ScreenSettings fetchFilterDetails(Integer hubCitiId, Integer filterId)
			throws HubCitiWebSqlException {
		final String methodName = "fetchFilterDetails";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<ScreenSettings> screenSettings = null;
		ScreenSettings settings = null;
		Integer responseFromProc = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcCityExperienceFilterDetails");
			simpleJdbcCall.returningResultSet("filterDetails",
					new BeanPropertyRowMapper<ScreenSettings>(
							ScreenSettings.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HubCitiID", hubCitiId);
			param.addValue("FilterID", filterId);
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc == 0) {
				screenSettings = (ArrayList<ScreenSettings>) resultFromProcedure
						.get("filterDetails");

				if (null != screenSettings && !screenSettings.isEmpty()) {
					settings = screenSettings.get(0);
				}
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
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
	 * @throws HubCitiWebSqlException
	 */
	public String deAssociateFilterRetailLocs(Integer filterID,
			String retailLocIDs) throws HubCitiWebSqlException {
		final String methodName = "deAssociateFilterRetailLocs";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String strResponse = null;
		Integer iStatus = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcCityExperienceFilterRetailLocationDeletion");
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HcFilterID", filterID);
			param.addValue("RetailLocationID", retailLocIDs);
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);
			iStatus = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != iStatus && iStatus == 0) {
				strResponse = ApplicationConstants.SUCCESS;
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	/**
	 * This method will delete the filter.
	 * 
	 * @param filterID
	 * @param hubCitiID
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String deleteFilter(Integer filterID, Integer hubCitiID)
			throws HubCitiWebSqlException {
		final String methodName = "deleteFilter";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String strResponse = null;
		Integer iStatus = null;
		Boolean isAssociated = false;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcCityExperienceFilterDeletion");
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("FilterID", filterID);
			param.addValue("HubCitiID", hubCitiID);
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);
			iStatus = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != iStatus && iStatus == 0) {

				isAssociated = (Boolean) resultFromProcedure.get("Associated");
				if (null != isAssociated && isAssociated == true) {
					strResponse = ApplicationConstants.FAILURETEXT;
				} else {

					strResponse = ApplicationConstants.SUCCESS;
				}
			} else {
				strResponse = ApplicationConstants.FAILURETEXT;
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	/**
	 * This method is to fetch the user settings details.
	 * 
	 * @param hubCitiID
	 * @return userSettings details
	 * @throws HubCitiServiceException
	 */
	@SuppressWarnings("unchecked")
	public final ScreenSettings fetchUserSettings(Integer hubCitiID)
			throws HubCitiWebSqlException {
		final String methodName = "fetchUserSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		ScreenSettings userSettings = null;
		List<ScreenSettings> userSettingsDetails = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcUserRegistrationFormsDisplay");
			simpleJdbcCall.returningResultSet("userSettingsDetails",
					new BeanPropertyRowMapper<ScreenSettings>(
							ScreenSettings.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HubCitiID", hubCitiID);
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {
				final Integer status = (Integer) resultFromProcedure
						.get("Status");
				if (null != status && status == 0) {
					userSettingsDetails = (ArrayList<ScreenSettings>) resultFromProcedure
							.get("userSettingsDetails");
					if (null != userSettingsDetails
							&& !userSettingsDetails.isEmpty()) {
						userSettings = userSettingsDetails.get(0);
					}
				} else {
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ "  : errorNumber  : " + errorNum
							+ " errorMessage : " + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);
				}
			}

		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName,
					e.getStackTrace());
			throw new HubCitiWebSqlException(e);
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
	 * @throws HubCitiWebSqlException
	 */
	public final String saveUserSettings(ScreenSettings userSettings, User user)
			throws HubCitiWebSqlException {
		final String methodName = "saveUserSettings";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String strResponse = null;
		Integer iStatus = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcUserRegistrationFormsCreation");
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HubCitiID", user.getHubCitiID());
			param.addValue("HcUserID", user.gethCAdminUserID());
			param.addValue("RegistrationFormFields",
					userSettings.getUserSettingsFields());
			param.addValue("ImagePath", userSettings.getLogoImageName());

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {
				iStatus = (Integer) resultFromProcedure.get("Status");

				if (null != iStatus && iStatus == 0) {
					strResponse = ApplicationConstants.SUCCESS;
				} else {
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ methodName + "Error number: {}" + errorNum
							+ " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);

				}

			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	/**
	 * This method is used to fetch state list.
	 */
	public List<CityExperience> getStatelst(int iHubCitiId)
			throws HubCitiWebSqlException {
		final String methodName = "getStatelst";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer iStatus = null;
		List<CityExperience> stateLst = null;
		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcAssociatedStateDisplay");
			simpleJdbcCall.returningResultSet("statelist",
					new BeanPropertyRowMapper<CityExperience>(
							CityExperience.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HubCitiID", iHubCitiId);

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {
				iStatus = (Integer) resultFromProcedure.get("Status");
				if (null != iStatus && iStatus == 0) {
					stateLst = new ArrayList<CityExperience>();
					stateLst = (ArrayList<CityExperience>) resultFromProcedure
							.get("statelist");
				} else {
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ methodName + "Error number: {}" + errorNum
							+ " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);

				}

			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return stateLst;
	}

	/**
	 * This method is used to fetch citi list based on state.
	 */
	@SuppressWarnings("unchecked")
	public List<CityExperience> getCitilst(int iHubCitiId, String strState)
			throws HubCitiWebSqlException {
		final String methodName = "getCitilst";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer iStatus = null;
		List<CityExperience> citilst = null;
		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcAssociatedCityDisplay");
			simpleJdbcCall.returningResultSet("citilist",
					new BeanPropertyRowMapper<CityExperience>(
							CityExperience.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HubCitiID", iHubCitiId);
			param.addValue("State", strState);

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {
				iStatus = (Integer) resultFromProcedure.get("Status");
				if (null != iStatus && iStatus == 0) {
					citilst = new ArrayList<CityExperience>();
					citilst = (ArrayList<CityExperience>) resultFromProcedure
							.get("citilist");
				} else {
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ methodName + "Error number: {}" + errorNum
							+ " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);

				}

			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return citilst;
	}

	/**
	 * This method is used to fetch zipcode list based on state and city.
	 */

	@SuppressWarnings("unchecked")
	public List<CityExperience> getZipcodelst(CityExperience cityExperience)
			throws HubCitiWebSqlException {
		final String methodName = "getZipcodelst";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer iStatus = null;
		List<CityExperience> zipcodelst = null;
		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcAssociatedPostalCodeDisplay");
			simpleJdbcCall.returningResultSet("zipcodelst",
					new BeanPropertyRowMapper<CityExperience>(
							CityExperience.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HubCitiID", cityExperience.getHubCitiId());
			param.addValue("State", cityExperience.getState());
			param.addValue("City", cityExperience.getCity());

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {
				iStatus = (Integer) resultFromProcedure.get("Status");
				if (null != iStatus && iStatus == 0) {
					zipcodelst = new ArrayList<CityExperience>();
					zipcodelst = (ArrayList<CityExperience>) resultFromProcedure
							.get("zipcodelst");
				} else {
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ methodName + "Error number: {}" + errorNum
							+ " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);

				}

			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return zipcodelst;
	}

	@SuppressWarnings("unchecked")
	public CityExperienceDetail getRetailer(CityExperience cityExperience)
			throws HubCitiWebSqlException {
		final String methodName = "getRetailer";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer iStatus = null;
		CityExperienceDetail objCityExperienceDetail = null;
		List<CityExperience> retailerLst = null;
		Integer iRowcount;
		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcAssociatedRetailersDisplay");
			simpleJdbcCall.returningResultSet("retailerlst",
					new BeanPropertyRowMapper<CityExperience>(
							CityExperience.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HubCitiID", cityExperience.getHubCitiId());
			param.addValue("City", cityExperience.getCity());
			param.addValue("State", cityExperience.getState());
			param.addValue("PostalCode", cityExperience.getPostalCode());
			param.addValue("RetailName", cityExperience.getRetSearchKey());
			param.addValue("LowerLimit", cityExperience.getLowerLimit());
			param.addValue("ScreenName",
					ApplicationConstants.SETUPRETAILERLOCATIONSCREENNAME);
			param.addValue("AssociatedFlag", cityExperience.getAssociateFlag());

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {
				iStatus = (Integer) resultFromProcedure.get("Status");
				if (null != iStatus && iStatus == 0) {

					retailerLst = (ArrayList<CityExperience>) resultFromProcedure
							.get("retailerlst");
					iRowcount = (Integer) resultFromProcedure.get("MaxCnt");
					if (null != retailerLst && !retailerLst.isEmpty()) {

						objCityExperienceDetail = new CityExperienceDetail();
						objCityExperienceDetail.setTotalSize(iRowcount);
						objCityExperienceDetail.setCityExpLst(retailerLst);
					}

				} else {
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ methodName + "Error number: {}" + errorNum
							+ " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);

				}

			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return objCityExperienceDetail;
	}

	public String deAssociateRetailer(CityExperience cityExperience)
			throws HubCitiWebSqlException {
		final String methodName = "deAssociateRetailer";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer iStatus = null;
		String strResponse = null;
		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcRetailerAssociationDeletion");
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HubCitiID", cityExperience.getHubCitiId());
			param.addValue("RetailLocationIDs",
					cityExperience.getUnAssociRetLocId());

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {
				iStatus = (Integer) resultFromProcedure.get("Status");
				if (null != iStatus && iStatus == 0) {
					strResponse = ApplicationConstants.SUCCESS;

				} else {
					strResponse = ApplicationConstants.FAILURE;
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ methodName + "Error number: {}" + errorNum
							+ " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);

				}

			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	public String associateRetailer(CityExperience cityExperience)
			throws HubCitiWebSqlException {
		final String methodName = "AssociateRetailer";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer iStatus = null;
		String strResponse = null;
		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcRetailerAssociation");
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HubCitiID", cityExperience.getHubCitiId());
			param.addValue("RetailLocationIDs",
					cityExperience.getUnAssociRetLocId());

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {
				iStatus = (Integer) resultFromProcedure.get("Status");
				if (null != iStatus && iStatus == 0) {
					strResponse = ApplicationConstants.SUCCESS;
				} else {
					strResponse = ApplicationConstants.FAILURE;
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ methodName + "Error number: {}" + errorNum
							+ " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);

				}

			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	/**
	 * This method will return list of event patterns.
	 * 
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public List<Event> getEventPatterns() throws HubCitiWebSqlException {
		final String methodName = "getEventPatterns";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		List<Event> events = null;
		Integer iStatus = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.returningResultSet("patterns",
					new BeanPropertyRowMapper<Event>(Event.class));
			simpleJdbcCall.withProcedureName("usp_WebHcEventPatternDisplay");
			final MapSqlParameterSource param = new MapSqlParameterSource();

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);
			iStatus = (Integer) resultFromProcedure.get("Status");

			if (null != iStatus && iStatus == 0) {
				events = (ArrayList<Event>) resultFromProcedure.get("patterns");
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);

			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return events;
	}

	/**
	 * This method will return list of user created FAQ's.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	@SuppressWarnings("unchecked")
	public FAQDetails fetchFQAs(FAQ faq) throws HubCitiWebSqlException {
		final String methodName = "fetchFQAs";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		List<FAQ> faqs = null;
		FAQDetails faqDetails = null;
		Integer status = null;
		Integer totalSize = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.returningResultSet("faqs",
					new BeanPropertyRowMapper<FAQ>(FAQ.class));
			simpleJdbcCall.withProcedureName("usp_WebHcFAQDisplay");
			final MapSqlParameterSource param = new MapSqlParameterSource();

			param.addValue("HcHubcitiID", faq.getHubCitiId());
			param.addValue("CategoryName", faq.getFaqSearchKey());
			param.addValue("LowerLimit", faq.getFaqLowerLimit());
			param.addValue("ScreenName",
					ApplicationConstants.HUBCITIWEBPAGINATION);
			param.addValue("FAQCategoryId", faq.getFaqCatIds());
			param.addValue("FAQId", faq.getQstnIds());
			param.addValue("SortOrder", faq.getSortOrderIds());

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);
			status = (Integer) resultFromProcedure.get("Status");

			if (null != status && status == 0) {
				faqs = (ArrayList<FAQ>) resultFromProcedure.get("faqs");
				totalSize = (Integer) resultFromProcedure.get("MaxCnt");
				faqDetails = new FAQDetails();
				faqDetails.setFaqs(faqs);
				faqDetails.setTotalSize(totalSize);
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return faqDetails;
	}

	/**
	 * This method will return list of user created FAQ Categories.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	@SuppressWarnings("unchecked")
	public FAQDetails fetchFAQCategories(FAQ faq) throws HubCitiWebSqlException {
		final String methodName = "fetchFAQCategories";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		List<FAQ> faqs = null;
		FAQDetails faqCategoryDetails = null;
		Integer status = null;
		Integer totalSize = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.returningResultSet("faqs",
					new BeanPropertyRowMapper<FAQ>(FAQ.class));
			simpleJdbcCall.withProcedureName("usp_WebHcFAQCategoryDisplay");
			final MapSqlParameterSource param = new MapSqlParameterSource();

			param.addValue("HcHubcitiID", faq.getHubCitiId());
			param.addValue("SearchKey", faq.getFaqCatName());
			param.addValue("LowerLimit", faq.getFaqLowerLimit());
			param.addValue("ScreenName",
					ApplicationConstants.HUBCITIWEBPAGINATION);
			param.addValue("FAQCategoryId", faq.getFaqCatIds());
			param.addValue("SortOrder", faq.getSortOrderIds());

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);
			status = (Integer) resultFromProcedure.get("Status");

			if (null != status && status == 0) {
				faqs = (ArrayList<FAQ>) resultFromProcedure.get("faqs");
				if (null != faqs && !faqs.isEmpty()) {
					totalSize = (Integer) resultFromProcedure.get("MaxCnt");
					faqCategoryDetails = new FAQDetails();
					faqCategoryDetails.setFaqs(faqs);
					faqCategoryDetails.setTotalSize(totalSize);
				}
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return faqCategoryDetails;
	}

	/**
	 * This method will save FAQ details.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String saveFAQs(FAQ faq) throws HubCitiWebSqlException {
		final String methodName = "saveFAQs";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;
		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		Boolean duplicate = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.withProcedureName("usp_WebHcFAQCreation");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("ScanSeeAdminUserID", faq.getUserId());
			param.addValue("HcHubcitiID", faq.getHubCitiId());
			param.addValue("FAQID", faq.getFaqID());
			param.addValue("HcFAQCategoryID", faq.getFaqCatId());
			param.addValue("Question", faq.getQuestion().trim());
			param.addValue("Answer", faq.getAnswer().trim());

			resultFromProcedure = simpleJdbcCall.execute(param);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != resultFromProcedure && responseFromProc == 0) {
				duplicate = (Boolean) resultFromProcedure.get("DuplicateFlag");
				if (duplicate == true) {
					status = ApplicationConstants.DUPLICATEQUESTION;
				} else {
					status = ApplicationConstants.SUCCESS;
				}
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	/**
	 * This method will return FAQ Details.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	@SuppressWarnings("unchecked")
	public FAQ fetchFAQDetails(FAQ faq) throws HubCitiWebSqlException {
		final String methodName = "fetchFAQDetails";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		List<FAQ> faqs = null;
		FAQ resultFAQ = null;
		Integer status = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.returningResultSet("faqs",
					new BeanPropertyRowMapper<FAQ>(FAQ.class));
			simpleJdbcCall.withProcedureName("usp_WebHcFAQDetails");
			final MapSqlParameterSource param = new MapSqlParameterSource();

			param.addValue("HcHubcitiID", faq.getHubCitiId());
			param.addValue("HcFAQID", faq.getFaqID());

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);
			status = (Integer) resultFromProcedure.get("Status");

			if (null != status && status == 0) {
				faqs = (ArrayList<FAQ>) resultFromProcedure.get("faqs");
				resultFAQ = faqs.get(0);
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return resultFAQ;
	}

	/**
	 * This method will delete FAQ Details.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String deleteFAQ(FAQ faq) throws HubCitiWebSqlException {
		final String methodName = "deleteFAQ";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Integer status = null;
		String response = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcFAQDeletion");
			final MapSqlParameterSource param = new MapSqlParameterSource();

			param.addValue("HcHubcitiID", faq.getHubCitiId());
			param.addValue("HcFAQID", faq.getFaqID());

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);
			status = (Integer) resultFromProcedure.get("Status");

			if (null != status && status == 0) {
				response = ApplicationConstants.SUCCESS;
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return response;
	}

	/**
	 * This method will save FAQ Category Details.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String addUpdateFAQCategory(FAQ faq) throws HubCitiWebSqlException {
		final String methodName = "addUpdateFAQCategory";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		boolean bCatExists;
		String strResponse = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcFAQCategoryCreation");
			final MapSqlParameterSource params = new MapSqlParameterSource();

			params.addValue("ScanSeeAdminUserID", faq.getUserId());
			params.addValue("HcHubCitiID", faq.getHubCitiId());
			params.addValue("FAQCategoryID", faq.getFaqCatId());
			params.addValue("CategoryName", faq.getFaqCatName());
			resultFromProcedure = simpleJdbcCall.execute(params);

			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				bCatExists = (Boolean) resultFromProcedure.get("DuplicateFlag");
				if (bCatExists == true) {
					strResponse = ApplicationConstants.ALERTCATEXISTS;
				} else {
					Integer catId = (Integer) resultFromProcedure
							.get("HcFAQCategoryID");
					if (null != catId) {
						strResponse = catId.toString();
					} else {
						strResponse = faq.getFaqCatId().toString();
					}
				}

			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	/**
	 * This method will delete FAQ Category.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String deleteFAQCategory(FAQ faq) throws HubCitiWebSqlException {
		final String methodName = "deleteFAQCategory";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		String strResponse = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcFAQCategoryDeletion");
			final MapSqlParameterSource params = new MapSqlParameterSource();

			params.addValue("HcHubCitiID", faq.getHubCitiId());
			params.addValue("HcFAQCategoryID", faq.getFaqCatId());
			resultFromProcedure = simpleJdbcCall.execute(params);

			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				strResponse = ApplicationConstants.SUCCESS;

			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	@SuppressWarnings("unchecked")
	public List<ScreenSettings> getMenuButtonType()
			throws HubCitiWebSqlException {
		final String methodName = "getMenuButtonType";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Integer responseFromProc = null;
		List<ScreenSettings> btnType = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcMenuItemShapeDisplay");
			simpleJdbcCall.returningResultSet("btnType",
					new BeanPropertyRowMapper<ScreenSettings>(
							ScreenSettings.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				btnType = (ArrayList<ScreenSettings>) resultFromProcedure
						.get("btnType");
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}

			LOG.info("usp_WebHcMenuItemShapeDisplay is  executed Successfully.");
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return btnType;
	}

	public String deleteSubMenu(ScreenSettings screenSettings,
			Integer iHubCityId) throws HubCitiWebSqlException {
		final String methodName = "deleteSubMenu";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		String strResponse = null;
		Integer iDeleted = null;
		String strAssciateMenus = null;
		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcMenuDeletion");
			final MapSqlParameterSource params = new MapSqlParameterSource();

			params.addValue("HcHubcitiID", iHubCityId);
			params.addValue("HcMenuID", screenSettings.getMenuId());
			resultFromProcedure = simpleJdbcCall.execute(params);

			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				iDeleted = (Integer) resultFromProcedure
						.get("SubMenuAssociateFlag");

				if (null != iDeleted && iDeleted == 0) {
					strResponse = ApplicationConstants.SUCCESS;
				} else {

					strResponse = ApplicationConstants.FAILURE;
					strResponse = (String) resultFromProcedure
							.get("SubMenuNames");
				}

			} else {
				strResponse = ApplicationConstants.FAILURE;
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	public String saveFaqCateReorder(FAQ faq) throws HubCitiWebSqlException {
		String strMethodName = "saveFaqCateReorder";
		String strResponse = null;
		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHCFAQCategorySortOrderUpdation");
			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
			mapSqlParameterSource.addValue("HcHubcitiID", faq.getHubCitiId());
			mapSqlParameterSource.addValue("FAQCategoryId", faq.getFaqCatIds());
			mapSqlParameterSource.addValue("SortOrder", faq.getSortOrderIds());

			resultFromProcedure = simpleJdbcCall.execute(mapSqlParameterSource);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				strResponse = ApplicationConstants.SUCCESS;

			} else {
				strResponse = ApplicationConstants.FAILURE;
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
						+ strMethodName + "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}

		} catch (HubCitiWebSqlException exception) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiWebSqlException();
		}
		return strResponse;
	}

	public String saveFaqReorder(FAQ faq) throws HubCitiWebSqlException {
		String strMethodName = "saveFaqReorder";
		String strResponse = null;
		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHCFAQSortOrderUpdation");
			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
			mapSqlParameterSource.addValue("HcHubcitiID", faq.getHubCitiId());
			mapSqlParameterSource.addValue("FAQCategoryId", faq.getFaqCatIds());
			mapSqlParameterSource.addValue("FAQId", faq.getQstnIds());
			mapSqlParameterSource.addValue("SortOrder", faq.getSortOrderIds());

			resultFromProcedure = simpleJdbcCall.execute(mapSqlParameterSource);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				strResponse = ApplicationConstants.SUCCESS;

			} else {
				strResponse = ApplicationConstants.FAILURE;
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
						+ strMethodName + "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}

		} catch (HubCitiWebSqlException exception) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + exception);
			throw new HubCitiWebSqlException();
		}
		return strResponse;
	}

	public String insertFilterOrder(int hcHubCitiID, String hcFilterID,
			int hcCityExoerienceID, String sortOrder, int userID)
			throws HubCitiWebSqlException {
		final String methodName = "insertFilterOrder";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		String strResponse = null;

		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcCityExperienceFilterSortOrderUpdation");
			final MapSqlParameterSource params = new MapSqlParameterSource();

			params.addValue("HcHubCitiID", hcHubCitiID);
			params.addValue("HcFilterID", hcFilterID);
			params.addValue("HcCityExperienceID", hcCityExoerienceID);
			params.addValue("SortOrder", sortOrder);
			params.addValue("UserID", userID);

			resultFromProcedure = simpleJdbcCall.execute(params);

			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {

				strResponse = ApplicationConstants.SUCCESS;

			} else {
				strResponse = ApplicationConstants.FAILURE;
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	@SuppressWarnings("unchecked")
	public List<ScreenSettings> displayModuleTabBars(Integer userId,
			Integer hubCitiId) throws HubCitiWebSqlException {

		final String methodName = "displayModuleTabBars";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Integer responseFromProc = null;
		List<ScreenSettings> moduleTabBars = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcBottomButtonListDisplay");
			simpleJdbcCall.returningResultSet("moduleTabBars",
					new BeanPropertyRowMapper<ScreenSettings>(
							ScreenSettings.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("UserID", userId);
			param.addValue("HcHubCitiID", hubCitiId);
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				moduleTabBars = (ArrayList<ScreenSettings>) resultFromProcedure
						.get("moduleTabBars");
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return moduleTabBars;
	}

	@SuppressWarnings("unchecked")
	public List<ScreenSettings> displayModules(Integer userId, Integer hubCitiId)
			throws HubCitiWebSqlException {

		final String methodName = "displayModules";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Integer responseFromProc = null;
		List<ScreenSettings> modules = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcBottomButtonModuleListDisplay");
			simpleJdbcCall.returningResultSet("modules",
					new BeanPropertyRowMapper<ScreenSettings>(
							ScreenSettings.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("UserID", userId);
			param.addValue("HcHubCitiID", hubCitiId);
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				modules = (ArrayList<ScreenSettings>) resultFromProcedure
						.get("modules");
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return modules;
	}

	/**
	 * This method will save module tab bar details.
	 * 
	 * @param screenSettings
	 * @param objUser
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String saveModuleTabBar(ScreenSettings screenSettings, User objUser)
			throws HubCitiWebSqlException {
		final String methodName = "saveModuleTabBar";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;
		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall
					.withProcedureName("usp_WebHcBottomButtonsAssociation");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			final MapSqlParameterSource alertsParam = new MapSqlParameterSource();
			alertsParam.addValue("UserID", objUser.gethCAdminUserID());
			alertsParam.addValue("HcHubCitiID", objUser.getHubCitiID());
			alertsParam.addValue("HcModuleID",
					screenSettings.getBottomBtnName());
			alertsParam.addValue("HcModuleBottomButtonIDs",
					screenSettings.getBtnLinkId());

			resultFromProcedure = simpleJdbcCall.execute(alertsParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != resultFromProcedure && responseFromProc == 0) {
				status = ApplicationConstants.SUCCESS;
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
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
	 * @throws HubCitiWebSqlException
	 */
	public String deleteModuleTabBar(ScreenSettings screenSettings, User objUser)
			throws HubCitiWebSqlException {
		final String methodName = "deleteModuleTabBar";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String status = null;
		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall
					.withProcedureName("usp_WebHcBottomButtonsAssociationDeletion");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			final MapSqlParameterSource alertsParam = new MapSqlParameterSource();
			alertsParam.addValue("UserID", objUser.gethCAdminUserID());
			alertsParam.addValue("HcHubCitiID", objUser.getHubCitiID());
			alertsParam.addValue("ModuleID", screenSettings.getBottomBtnName());

			resultFromProcedure = simpleJdbcCall.execute(alertsParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != resultFromProcedure && responseFromProc == 0) {
				status = ApplicationConstants.SUCCESS;
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return status;
	}

	@SuppressWarnings("unchecked")
	public UserDetails displayHubCitiCreatedUsers(User user)
			throws HubCitiWebSqlException {

		final String methodName = "displayHubCitiCreatedUsers";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Integer responseFromProc = null;
		List<User> usersLst = null;
		UserDetails userDetails = null;
		Integer maxCount = null;

		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcRoleBasedUserListDisplay");
			simpleJdbcCall.returningResultSet("users",
					new BeanPropertyRowMapper<User>(User.class));
			final MapSqlParameterSource userparam = new MapSqlParameterSource();
			userparam.addValue("HcHubcitiID", user.getHubCitiID());
			userparam.addValue("SearchKey", user.getUserSearch());
			userparam.addValue("HubcitiUserID", user.gethCAdminUserID());
			userparam.addValue("LowerLimit", user.getLowerLimit());
			userparam.addValue("ScreenName",
					ApplicationConstants.HUBCITIWEBPAGINATION);
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(userparam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				usersLst = (ArrayList<User>) resultFromProcedure.get("users");
				maxCount = (Integer) resultFromProcedure.get("MaxCnt");
				userDetails = new UserDetails();
				userDetails.setUserLst(usersLst);
				userDetails.setTotalSize(maxCount);

			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}

			LOG.info("usp_WebHcRoleBasedUserListDisplay is  executed Successfully.");

		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.error(ApplicationConstants.METHODEND + methodName);
		return userDetails;
	}

	@SuppressWarnings("unchecked")
	public List<Module> displayUserModules(Integer hubCitiID, Integer roleUserId)
			throws HubCitiWebSqlException {

		final String methodName = "displayUserModules";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Integer responseFromProc = null;
		List<Module> modules = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcRoleBasedModuleListDisplay");
			simpleJdbcCall.returningResultSet("modules",
					new BeanPropertyRowMapper<Module>(Module.class));
			final MapSqlParameterSource queryParam = new MapSqlParameterSource();
			queryParam.addValue("HcHubcitiID", hubCitiID);
			queryParam.addValue("HcRoleUserId", roleUserId);
			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(queryParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				modules = (ArrayList<Module>) resultFromProcedure
						.get("modules");
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return modules;
	}

	public String saveUpdateUserDeatils(User user)
			throws HubCitiWebSqlException {

		String methodName = "saveUpdateUserDeatils";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String response = null;
		Integer responseFromProc = null;
		Boolean duplicateUser = null;

		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			final MapSqlParameterSource queryParams = new MapSqlParameterSource();

			simpleJdbcCall
					.withProcedureName("usp_WebHcRoleBasedUserCreationandUpdation");
			queryParams.addValue("HubcitiUserID", user.gethCAdminUserID());
			queryParams.addValue("HcHubCitiID", user.getHubCitiID());
			queryParams.addValue("RoleBasedUserID", user.getRoleUserId());
			queryParams.addValue("FirstName", user.getFirstName());
			queryParams.addValue("LastName", user.getLastName());
			queryParams.addValue("EmailID", user.getEmailId());

			queryParams.addValue("UserName", user.getUserName());
			queryParams.addValue("UserStatus", user.getUserStatus());
			queryParams.addValue("Password", user.getEncrptedPassword());
			queryParams.addValue("ModuleID", user.getModule());
			queryParams.addValue("UserType", user.getUserType());

			if (null != user.getEventCategory()
					&& !"".equalsIgnoreCase(user.getEventCategory())) {
				queryParams
						.addValue("EventCategoryID", user.getEventCategory());
			} else {
				queryParams.addValue("EventCategoryID", null);
			}
			if (null != user.getFundraiserCategory()
					&& !"".equalsIgnoreCase(user.getFundraiserCategory())) {
				queryParams.addValue("FundraisingEventCategoryID",
						user.getFundraiserCategory());
			} else {
				queryParams.addValue("FundraisingEventCategoryID", null);
			}

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(queryParams);

			if (null != resultFromProcedure) {
				responseFromProc = (Integer) resultFromProcedure
						.get(ApplicationConstants.STATUS);
				duplicateUser = (Boolean) resultFromProcedure
						.get(ApplicationConstants.DUPLICATEUSERNAME);
				if (null != responseFromProc) {
					if (duplicateUser == true) {
						response = ApplicationConstants.DUPLICATEUSERNAME;
					} else if (responseFromProc.intValue() == 0) {
						response = ApplicationConstants.SUCCESS;
					} else {
						final Integer errorNum = (Integer) resultFromProcedure
								.get(ApplicationConstants.ERRORNUMBER);
						final String errorMsg = (String) resultFromProcedure
								.get(ApplicationConstants.ERRORMESSAGE);
						LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
								+ methodName + "Error number: {}" + errorNum
								+ " and error message: {}" + errorMsg);
						throw new HubCitiWebSqlException(errorMsg);
					}

				} else {
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ methodName + "Error number: {}" + errorNum
							+ " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);
				}

			}

		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return response;
	}

	/**
	 * This method returns requested user details.
	 * 
	 * @param userId
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	@SuppressWarnings("unchecked")
	public User fetchUserDetails(Integer userId) throws HubCitiWebSqlException {

		final String methodName = "fetchUserDetails";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		List<User> userDetails = null;
		User user = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.withProcedureName("usp_WebHcRoleBasedUserDetails");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.returningResultSet("userDetails",
					new BeanPropertyRowMapper<User>(User.class));
			final MapSqlParameterSource userDetailsParam = new MapSqlParameterSource();
			userDetailsParam.addValue("RoleBasedUserID", userId);

			resultFromProcedure = simpleJdbcCall.execute(userDetailsParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				userDetails = (ArrayList<User>) resultFromProcedure
						.get("userDetails");
				user = userDetails.get(0);
			} else {
				Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ ": errorNumber  : " + errorNum + " errorMessage : "
						+ errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException exception) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ exception.getStackTrace());
			throw new HubCitiWebSqlException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return user;
	}

	/**
	 * This method will activate or de-activate requested user.
	 * 
	 * @param userId
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String activateDeactivateUsers(Integer userId)
			throws HubCitiWebSqlException {

		final String methodName = "activateDeactivateUsers";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		String response = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.withProcedureName("usp_WebHcRoleBasedUserDeletion");
			simpleJdbcCall.withSchemaName(ApplicationConstants.WEBSCHEMA);
			final MapSqlParameterSource userDetailsParam = new MapSqlParameterSource();
			userDetailsParam.addValue("RoleBasedUserID", userId);

			resultFromProcedure = simpleJdbcCall.execute(userDetailsParam);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				response = ApplicationConstants.SUCCESS;
			} else {
				Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ ": errorNumber  : " + errorNum + " errorMessage : "
						+ errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException exception) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ exception.getStackTrace());
			throw new HubCitiWebSqlException(exception);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return response;
	}

	@SuppressWarnings("unchecked")
	public List<CityExperience> displayCitiesForRegionApp(User user)
			throws HubCitiWebSqlException {
		final String methodName = "displayCitiesForRegionApp";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		List<CityExperience> cityExperienceLst = null;
		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcCityList");
			simpleJdbcCall.returningResultSet("citylst",
					new BeanPropertyRowMapper<CityExperience>(
							CityExperience.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HcHubcitiID", user.getHubCitiID());
			param.addValue("UserID", user.gethCAdminUserID());

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {
				cityExperienceLst = (ArrayList<CityExperience>) resultFromProcedure
						.get("citylst");
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return cityExperienceLst;
	}

	@SuppressWarnings("unchecked")
	public AlertCategory fetchFundraiserEventCategories(User objUser)
			throws HubCitiWebSqlException {
		final String methodName = "fetchFundraiserEventCategories";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer iRowcount = null;
		List<Category> categoryList = null;
		AlertCategory objAlertCategory = null;
		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiFundraisingCategoryDisplay");
			simpleJdbcCall.returningResultSet("fundraisereventcategoryList",
					new BeanPropertyRowMapper<Category>(Category.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("UserID", objUser.gethCAdminUserID());
			param.addValue("CategoryName", null);
			param.addValue("LowerLimit", null);
			param.addValue("ScreenName",
					ApplicationConstants.HUBCITIWEBPAGINATION);
			param.addValue("FundraisingCategoryID", null);
			param.addValue("RoleBasedUserID", objUser.getRoleUserId());

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {

				iRowcount = (Integer) resultFromProcedure.get("MaxCnt");
				categoryList = (ArrayList<Category>) resultFromProcedure
						.get("fundraisereventcategoryList");
				if (null != categoryList && !categoryList.isEmpty()) {
					objAlertCategory = new AlertCategory();
					objAlertCategory.setAlertCatLst(categoryList);
					objAlertCategory.setTotalSize(iRowcount);

				}
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return objAlertCategory;
	}

	@SuppressWarnings("unchecked")
	public List<Department> fetchFundraiserDepartments(User objUser)
			throws HubCitiWebSqlException {

		final String methodName = "fetchFundraiserDepartments";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		List<Department> departmentList = null;

		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiFundraisingDepartmentDisplay");
			simpleJdbcCall.returningResultSet("fundraiserDepartmentList",
					new BeanPropertyRowMapper<Department>(Department.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("UserID", objUser.gethCAdminUserID());
			param.addValue("HcHubCitiID", objUser.getHubCitiID());

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);
			if (null != resultFromProcedure) {
				departmentList = (ArrayList<Department>) resultFromProcedure
						.get("fundraiserDepartmentList");
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return departmentList;
	}

	public String saveUpdateFundraiserEventDeatils(Event eventDetails, User user)
			throws HubCitiWebSqlException {

		String methodName = "saveUpdateFundraiserEventDeatils";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		String response = null;
		Integer responseFromProc = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			final MapSqlParameterSource queryParams = new MapSqlParameterSource();

			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiFundraisingEventsCreationAndUpdation");

			queryParams.addValue("UserID", user.gethCAdminUserID());
			queryParams.addValue("HcHubCitiID", user.getHubCitiID());
			queryParams.addValue("FundraisingID", eventDetails.getHcEventID());
			queryParams.addValue("FundraisingName",
					eventDetails.getHcEventName());
			queryParams.addValue("FundraisingOrganizationImagePath",
					eventDetails.getEventImageName());

			if ("No".equalsIgnoreCase(eventDetails.getIsEventAppsite())) {
				queryParams.addValue("FundraisingOrganizationName",
						eventDetails.getOrganizationHosting());
				queryParams.addValue("AppsiteID", null);
				queryParams.addValue("FundraisingAppsiteFlag", false);
			} else {
				queryParams.addValue("FundraisingOrganizationName", null);
				queryParams.addValue("AppsiteID", eventDetails.getAppsiteIDs());
				queryParams.addValue("FundraisingAppsiteFlag", true);
			}

			queryParams.addValue("ShortDescription",
					eventDetails.getShortDescription());
			queryParams.addValue("LongDescription",
					eventDetails.getLongDescription());
			queryParams.addValue("HcFundraisingCategoryID",
					eventDetails.getEventCategory());

			if (null != eventDetails.getDepartmentId()
					&& !"".equals(eventDetails.getDepartmentId())) {
				queryParams.addValue("HcFundraisingDepartmentID",
						eventDetails.getDepartmentId());
			} else {
				queryParams.addValue("HcFundraisingDepartmentID", null);
			}

			queryParams.addValue("StartDate", eventDetails.getEventDate());

			if (null != eventDetails.getEventEDate()
					&& !"".equals(eventDetails.getEventEDate())) {
				queryParams.addValue("EndDate", eventDetails.getEventEDate());
			} else {
				queryParams.addValue("EndDate", null);
			}

			if ("Yes".equalsIgnoreCase(eventDetails.getIsEventTied())) {
				queryParams.addValue("EventID", eventDetails.getEventTiedIds());
				queryParams.addValue("FundraisingEventFlag", true);
			} else {
				queryParams.addValue("EventID", null);
				queryParams.addValue("FundraisingEventFlag", false);
			}

			if (null != eventDetails.getMoreInfoURL()
					&& !"".equals(eventDetails.getMoreInfoURL())) {
				queryParams.addValue("MoreInformationURL",
						eventDetails.getMoreInfoURL());
			} else {
				queryParams.addValue("MoreInformationURL", null);
			}

			if (null != eventDetails.getPurchaseProducts()
					&& !"".equals(eventDetails.getPurchaseProducts())) {
				queryParams.addValue("PurchaseProductURL",
						eventDetails.getPurchaseProducts());
			} else {
				queryParams.addValue("PurchaseProductURL", null);
			}

			if (null != eventDetails.getFundraisingGoal()
					&& !"".equals(eventDetails.getFundraisingGoal())) {
				queryParams.addValue("FundraisingGoal",
						eventDetails.getFundraisingGoal());
			} else {
				queryParams.addValue("FundraisingGoal", null);
			}

			if (null != eventDetails.getCurrentLevel()
					&& !"".equals(eventDetails.getCurrentLevel())) {
				queryParams.addValue("CurrentLevel",
						eventDetails.getCurrentLevel());
			} else {
				queryParams.addValue("CurrentLevel", null);
			}

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(queryParams);

			if (null != resultFromProcedure) {
				responseFromProc = (Integer) resultFromProcedure
						.get(ApplicationConstants.STATUS);
				if (null != responseFromProc
						&& responseFromProc.intValue() == 0) {
					response = ApplicationConstants.SUCCESS;
				} else {
					final Integer errorNum = (Integer) resultFromProcedure
							.get(ApplicationConstants.ERRORNUMBER);
					final String errorMsg = (String) resultFromProcedure
							.get(ApplicationConstants.ERRORMESSAGE);
					LOG.error(ApplicationConstants.EXCEPTIONOCCURRED
							+ methodName + "Error number: {}" + errorNum
							+ " and error message: {}" + errorMsg);
					throw new HubCitiWebSqlException(errorMsg);
				}
			}

		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return response;
	}

	public String addFundraiserDept(String catName, User objUser)
			throws HubCitiWebSqlException {

		String methodName = "addFundraiserDept";
		LOG.info(ApplicationConstants.METHODSTART + methodName);

		Map<String, Object> resultFromProcedure = null;
		Integer responseFromProc = null;
		boolean deptExists;
		String strResponse = null;

		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiFundraisingDepartmentCreation");
			final MapSqlParameterSource params = new MapSqlParameterSource();

			params.addValue("UserID", objUser.gethCAdminUserID());
			params.addValue("HcHubCitiID", objUser.getHubCitiID());
			params.addValue("HcDepartmentName", catName);

			resultFromProcedure = simpleJdbcCall.execute(params);
			responseFromProc = (Integer) resultFromProcedure
					.get(ApplicationConstants.STATUS);

			if (null != responseFromProc && responseFromProc.intValue() == 0) {
				deptExists = (Boolean) resultFromProcedure.get("DuplicateFlag");
				if (deptExists == true) {
					strResponse = ApplicationConstants.DEPARTMENTEXISTS;
				} else {
					Integer deptId = (Integer) resultFromProcedure
							.get("HcFundraisingDepartmentID");
					strResponse = deptId.toString();
				}

			} else {
				strResponse = ApplicationConstants.FAILURE;
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return strResponse;
	}

	/**
	 * This method will return Fundraiser Details.
	 * 
	 * @param event
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	@SuppressWarnings("unchecked")
	public Event fetchFundraiserDetails(Integer eventId)
			throws HubCitiWebSqlException {
		final String methodName = "fetchFundraiserDetails";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		List<Event> events = null;
		Event resultEvent = null;
		Integer status = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.returningResultSet("event",
					new BeanPropertyRowMapper<Event>(Event.class));
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiFundraisingEventDetails");
			final MapSqlParameterSource param = new MapSqlParameterSource();

			param.addValue("HcFundraisingID", eventId);

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);
			status = (Integer) resultFromProcedure.get("Status");

			if (null != status && status == 0) {
				events = (ArrayList<Event>) resultFromProcedure.get("event");
				resultEvent = events.get(0);
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
					+ e.getStackTrace());
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return resultEvent;
	}

	@SuppressWarnings("unchecked")
	public AlertCategory fetchFundraiserCategories(Category objCategory,
			User objUser) throws HubCitiWebSqlException {
		final String methodName = "fetchFundraiserCategories";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer iRowcount = null;
		List<Category> categoryList = null;
		AlertCategory objAlertCategory = null;
		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiFundraisingCategoryDisplay");
			simpleJdbcCall.returningResultSet("eventcategoryList",
					new BeanPropertyRowMapper<Category>(Category.class));

			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("UserID", objUser.gethCAdminUserID());
			param.addValue("CategoryName", null);
			param.addValue("LowerLimit", null);
			param.addValue("ScreenName",
					ApplicationConstants.HUBCITIWEBPAGINATION);
			param.addValue("FundraisingCategoryID", null);
			param.addValue("RoleBasedUserID", objUser.getRoleUserId());

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			if (null != resultFromProcedure) {

				iRowcount = (Integer) resultFromProcedure.get("MaxCnt");
				categoryList = (ArrayList<Category>) resultFromProcedure
						.get("eventcategoryList");
				if (null != categoryList && !categoryList.isEmpty()) {
					objAlertCategory = new AlertCategory();
					objAlertCategory.setAlertCatLst(categoryList);
					objAlertCategory.setTotalSize(iRowcount);

				}
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}

		LOG.info(ApplicationConstants.METHODEND + methodName);
		return objAlertCategory;
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public DealDetails fetchDeals(Deals deals, User objUser)
			throws HubCitiWebSqlException {
		final String methodName = "fetchDeals";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer status = null;
		Integer iRowcount = null;
		Integer dealId = null;
		String dealName = null;
		List<Deals> dealList = null;
		DealDetails dealDetails = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcDealoftheDayList");
			simpleJdbcCall.returningResultSet("dealList",
					new BeanPropertyRowMapper<Deals>(Deals.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("UserID", objUser.gethCAdminUserID());
			param.addValue("HcHubCitiID", objUser.getHubCitiID());
			param.addValue("DealsName", deals.getDealName());
			param.addValue("SearchKey", deals.getDealSearchKey());
			param.addValue("LowerLimit", deals.getLowerLimit());
			param.addValue("ScreenName",
					ApplicationConstants.HUBCITIWEBPAGINATION);

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			status = (Integer) resultFromProcedure.get("Status");
			if (null != status && status == 0) {
				iRowcount = (Integer) resultFromProcedure.get("MaxCnt");
				dealList = (ArrayList<Deals>) resultFromProcedure
						.get("dealList");
				dealId = (Integer) resultFromProcedure.get("DealID");
				dealName = (String) resultFromProcedure.get("DealName");
				dealDetails = new DealDetails();
				dealDetails.setDealId(dealId);
				dealDetails.setDealName(dealName);
				if (null != dealList && !dealList.isEmpty()) {
					dealDetails.setDeals(dealList);
					dealDetails.setTotalSize(iRowcount);
				}
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return dealDetails;
	}

	/**
	 * 
	 */
	public String saveDealOfTheDay(Deals deals, User objUser)
			throws HubCitiWebSqlException {
		final String methodName = "fetchDeals";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer status = null;
		String response = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.withProcedureName("usp_WebHcDealoftheDayCreation");
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("UserID", objUser.gethCAdminUserID());
			param.addValue("HcHubCitiID", objUser.getHubCitiID());
			param.addValue("DealName", deals.getDealName());
			param.addValue("DealID", deals.getDealId());

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			status = (Integer) resultFromProcedure.get("Status");
			if (null != status && status == 0) {
				response = ApplicationConstants.SUCCESS;
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return response;
	}

	/**
	 * This method is used to save marker details
	 */
	public String saveEvtMarkerInfo(Event event, User objUser)
			throws HubCitiWebSqlException {
		final String methodName = "saveEvtMarkerInfo";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer status = null;
		String response = null;
		Integer evtMarkerId = null;
		Boolean duplicateLatLong = null;
		try {

			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiEventMarkerCreationAndUpdation");
			final MapSqlParameterSource param = new MapSqlParameterSource();

			param.addValue("HCEventsID", event.getHcEventID());
			param.addValue("UserID", objUser.gethCAdminUserID());
			param.addValue("EventMarkerID", event.getEvtMarkerId());
			param.addValue("EventMarkerName", event.getEvtMarkerName());
			param.addValue("EventMarkerImagePath", event.getEventImageName());
			param.addValue("Latitude", event.getLatitude());
			param.addValue("Longitude", event.getLogitude());

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			status = (Integer) resultFromProcedure.get("Status");
			if (null != status && status == 0) {

				duplicateLatLong = (Boolean) resultFromProcedure
						.get("DuplicateLatLong");

				if (null != duplicateLatLong && duplicateLatLong == false) {
					response = ApplicationConstants.SUCCESS;
				} else {
					response = ApplicationConstants.FAILURETEXT;
				}

			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return response;
	}

	/**
	 * This method is used to fetch all marker details
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Event> getEvtMarkerInfo(Event event, User user)
			throws HubCitiWebSqlException {
		final String methodName = "saveEvtMarkerInfo";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer status = null;
		String response = null;
		ArrayList<Event> evtMarkerLst = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.returningResultSet("evtmarkerinfo",
					new BeanPropertyRowMapper<Event>(Event.class));
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiEventMarkerDetailsDisplay");
			final MapSqlParameterSource param = new MapSqlParameterSource();

			param.addValue("HubCitiID", user.getHubCitiID());
			param.addValue("HcEventsID", event.getHcEventID());

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			status = (Integer) resultFromProcedure.get("Status");
			if (null != status && status == 0) {
				evtMarkerLst = (ArrayList<Event>) resultFromProcedure
						.get("evtmarkerinfo");

			} else {
				response = ApplicationConstants.FAILURETEXT;
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return evtMarkerLst;
	}

	/**
	 * This method is used to delete specific marker
	 */
	@SuppressWarnings("unchecked")
	public String deleteEvtMarker(Event event) throws HubCitiWebSqlException {
		final String methodName = "deleteEvtMarker";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer status = null;
		String response = null;

		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiEventMarkerDeletion");
			final MapSqlParameterSource param = new MapSqlParameterSource();

			param.addValue("EventMarkerID", event.getEvtMarkerId());

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			status = (Integer) resultFromProcedure.get("Status");
			if (null != status && status == 0) {

				response = ApplicationConstants.EVENTMARKERDELETETEXT;
			} else {

				response = ApplicationConstants.FAILURETEXT;
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return response;
	}

	/*
	 * public List<Category> getCategoryImageDetails(Integer userId, Integer
	 * hubCitiId) throws HubCitiWebSqlException { final String methodName =
	 * "getCategoryImageDetails"; LOG.info(ApplicationConstants.METHODSTART +
	 * methodName); List<Category> catList = null; Integer status = null; try {
	 * simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
	 * simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
	 * simpleJdbcCall.withProcedureName("usp_WebHcFindCategoryimagesList");
	 * simpleJdbcCall.returningResultSet("catList", new
	 * BeanPropertyRowMapper<Category>(Category.class)); final
	 * MapSqlParameterSource param = new MapSqlParameterSource();
	 * param.addValue("UserID", userId); param.addValue("HcHubCitiID",
	 * hubCitiId);
	 * 
	 * final Map<String, Object> resultFromProcedure =
	 * simpleJdbcCall.execute(param);
	 * 
	 * status = (Integer) resultFromProcedure.get("Status"); if (null != status
	 * && status == 0) { catList = (List<Category>)
	 * resultFromProcedure.get("catList"); } else { final Integer errorNum =
	 * (Integer) resultFromProcedure.get(ApplicationConstants.ERRORNUMBER);
	 * final String errorMsg = (String)
	 * resultFromProcedure.get(ApplicationConstants.ERRORMESSAGE);
	 * LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName +
	 * " SP name: usp_WebHcFindCategoryimagesList " + "Error number: {} " +
	 * errorNum + " and error message: {} " + errorMsg); throw new
	 * HubCitiWebSqlException(errorMsg); } } catch (DataAccessException e) {
	 * LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e); throw new
	 * HubCitiWebSqlException(e); } LOG.info(ApplicationConstants.METHODEND +
	 * methodName); return catList; }
	 */

	/*
	 * public String updateCategoryImage(ScreenSettings screenSettings, User
	 * user) throws HubCitiWebSqlException { final String methodName =
	 * "updateCategoryImage"; LOG.info(ApplicationConstants.METHODSTART +
	 * methodName); Integer status = null; String response = null; try {
	 * simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
	 * simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
	 * simpleJdbcCall.withProcedureName("usp_WebHcUpdateFindCategoryImage");
	 * final MapSqlParameterSource param = new MapSqlParameterSource();
	 * param.addValue("UserID", user.gethCAdminUserID());
	 * param.addValue("HcHubCitiID", user.getHubCitiID());
	 * param.addValue("HcFindCategoryImageID", screenSettings.getCategory());
	 * param.addValue("CategoryImage", screenSettings.getCatImgName());
	 * 
	 * final Map<String, Object> resultFromProcedure =
	 * simpleJdbcCall.execute(param);
	 * 
	 * status = (Integer) resultFromProcedure.get("Status"); if (null != status
	 * && status == 0) { response = ApplicationConstants.SUCCESS; } else { final
	 * Integer errorNum = (Integer)
	 * resultFromProcedure.get(ApplicationConstants.ERRORNUMBER); final String
	 * errorMsg = (String)
	 * resultFromProcedure.get(ApplicationConstants.ERRORMESSAGE);
	 * LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName +
	 * " SP name: usp_WebHcUpdateFindCategoryImage " + "Error number: {} " +
	 * errorNum + " and error message: {} " + errorMsg); throw new
	 * HubCitiWebSqlException(errorMsg); } } catch (DataAccessException e) {
	 * LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e); throw new
	 * HubCitiWebSqlException(e); } LOG.info(ApplicationConstants.METHODEND +
	 * methodName); return response; }
	 */

	/**
	 * DAO method to get Event logistics button details.
	 * 
	 */
	public Event getEventLogisticsButtonDetails(Integer hubCitiId,
			Integer eventId, Integer userId) throws HubCitiWebSqlException {
		final String methodName = "getEventLogisticsButtonDetails";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer status = null;
		List<Event> logisticsList = null;
		Event event = null;
		String logisticsImgPath = null;
		Boolean eventOverlayFlag = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiEventLogisticsDetails");
			simpleJdbcCall.returningResultSet("logisticsBtnDetails",
					new BeanPropertyRowMapper<Event>(Event.class));
			final MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("HubCitiID", hubCitiId);
			param.addValue("HcEventID", eventId);
			param.addValue("UserID", userId);

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);
			status = (Integer) resultFromProcedure.get("Status");
			if (null != status && status == 0) {
				logisticsList = (List<Event>) resultFromProcedure
						.get("logisticsBtnDetails");
				logisticsImgPath = (String) resultFromProcedure
						.get("EventLogisticImgPath");
				eventOverlayFlag = (Boolean) resultFromProcedure
						.get("EventsIsOverLayFlag");
				event = new Event();
				event.setLogisticsBtnList(logisticsList);
				event.setLogisticsImgPath(logisticsImgPath);
				if (eventOverlayFlag != null && eventOverlayFlag) {
					event.setIsEventOverlay("yes");
				} else {
					event.setIsEventOverlay("no");
				}
			} else {
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ " SP name: usp_WebHcHubCitiEventLogisticsDetails "
						+ "Error number: {} " + errorNum
						+ " and error message: {} " + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return event;
	}

	/**
	 * This method is used to fetch specific marker details
	 */

	@SuppressWarnings("unchecked")
	public ArrayList<Event> getMarkerInfo(Event event, User user)
			throws HubCitiWebSqlException {
		final String methodName = "getMarkerInfo";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		Integer status = null;
		String response = null;
		ArrayList<Event> evtMarkerLst = null;
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			simpleJdbcCall.setSchemaName(ApplicationConstants.WEBSCHEMA);
			simpleJdbcCall.returningResultSet("markerinfo",
					new BeanPropertyRowMapper<Event>(Event.class));
			simpleJdbcCall
					.withProcedureName("usp_WebHcHubCitiEventMarkerDetails");
			final MapSqlParameterSource param = new MapSqlParameterSource();

			param.addValue("EventMarkerID", event.getEvtMarkerId());
			param.addValue("UserID", user.gethCAdminUserID());
			param.addValue("HubCitiID", user.getHubCitiID());
			param.addValue("HcEventsID", event.getHcEventID());

			final Map<String, Object> resultFromProcedure = simpleJdbcCall
					.execute(param);

			status = (Integer) resultFromProcedure.get("Status");
			if (null != status && status == 0) {
				evtMarkerLst = (ArrayList<Event>) resultFromProcedure
						.get("markerinfo");

			} else {
				response = ApplicationConstants.FAILURETEXT;
				final Integer errorNum = (Integer) resultFromProcedure
						.get(ApplicationConstants.ERRORNUMBER);
				final String errorMsg = (String) resultFromProcedure
						.get(ApplicationConstants.ERRORMESSAGE);
				LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName
						+ "Error number: {}" + errorNum
						+ " and error message: {}" + errorMsg);
				throw new HubCitiWebSqlException(errorMsg);
			}
		} catch (DataAccessException e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e);
			throw new HubCitiWebSqlException(e);
		}
		LOG.info(ApplicationConstants.METHODEND + methodName);
		return evtMarkerLst;
	}

}
