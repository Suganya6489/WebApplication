package com.hubciti.dao;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.StyledEditorKit.BoldAction;

import com.hubciti.common.exception.HubCitiServiceException;
import com.hubciti.common.exception.HubCitiWebSqlException;
import com.hubciti.common.pojo.AlertCategory;
import com.hubciti.common.pojo.Alerts;
import com.hubciti.common.pojo.AlertsDetails;
import com.hubciti.common.pojo.AnythingPages;
import com.hubciti.common.pojo.AppConfiguration;
import com.hubciti.common.pojo.AppSiteDetails;
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
import com.hubciti.common.pojo.SubMenuDetails;
import com.hubciti.common.pojo.User;
import com.hubciti.common.pojo.UserDetails;

public interface HubCitiDAO {
	/**
	 * This will return login screen details.
	 * 
	 * @param loginUser
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public ScreenSettings fetchScreenSettings(User loginUser) throws HubCitiWebSqlException;

	/**
	 * This will save login screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String insertLoginScreenSettings(ScreenSettings screenSettings, User user) throws HubCitiWebSqlException;

	/**
	 * This will save registration screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String insertRegScreenSettings(ScreenSettings screenSettings, User user) throws HubCitiWebSqlException;

	/**
	 * This will save About us screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String insertAboutusScreenSettings(ScreenSettings screenSettings, User user) throws HubCitiWebSqlException;

	/**
	 * This will save Privacy Policy screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String insertPrivacyPolicyScreenSettings(ScreenSettings screenSettings, User user) throws HubCitiWebSqlException;

	/**
	 * This will save Splash Screen screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String insertSplashScreenSettings(ScreenSettings screenSettings, User user) throws HubCitiWebSqlException;

	/**
	 * This will save the new password for the user.
	 * 
	 * @param objUser
	 * @return
	 * @throws HubCitiServiceException
	 */
	User forgotPwd(User objUser) throws HubCitiServiceException;

	/**
	 * This method will return application configuration.
	 * 
	 * @param configType
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	List<AppConfiguration> getAppConfig(String configType) throws HubCitiWebSqlException;

	/**
	 * This will save General Settings screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String insertGeneralSettingsDetails(ScreenSettings screenSettings, User user) throws HubCitiWebSqlException;

	/**
	 * This method will return general application configuration image.
	 * 
	 * @param configType
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	List<AppConfiguration> getAppConfigForGeneralImages(String configType) throws HubCitiWebSqlException;

	/**
	 * This methos is used to get the logged in user details.
	 * 
	 * @param userName
	 * @return User
	 */
	public User getLoginAdminDetails(String userName) throws HubCitiWebSqlException;

	/**
	 * This method returns the page details.
	 * 
	 * @param hubCitiId
	 * @return PageStatus
	 */
	public PageStatus getScreenStatus(int hubCitiId) throws HubCitiWebSqlException;

	/**
	 * This method updates the password with new password.
	 * 
	 * @param User
	 * @return String
	 */
	public String saveChangedPassword(User user) throws HubCitiWebSqlException;

	public String createMenu(MenuDetails menuDetails, User user) throws HubCitiWebSqlException;

	public List<MenuDetails> getLinkList() throws HubCitiWebSqlException;

	public String saveMenuTemplate(MenuDetails menuDetails, User user) throws HubCitiWebSqlException;

	public MenuDetails fetchMainMenuDetails(MenuDetails menuDetails, User user) throws HubCitiWebSqlException;

	public SubMenuDetails fetchSubMenuDetails(User user, ScreenSettings screenSettings) throws HubCitiWebSqlException;

	public List<AppSiteDetails> getAppSites(String searchKey, int ihubCityId, Integer lowerLimit) throws HubCitiWebSqlException;

	public List<MenuDetails> getBottomLinkList(int ihubCityId) throws HubCitiWebSqlException;

	public List<ScreenSettings> getBottomBarExistingIcons() throws HubCitiWebSqlException;

	public String insertUpdateTabBarButton(ScreenSettings buttondetails, User loginUser) throws HubCitiWebSqlException;

	public List<ScreenSettings> fetchTabBarButtons(MenuDetails menuDetails, User loginUser) throws HubCitiWebSqlException;

	public String deleteTabBarButton(Integer bottomBtnId) throws HubCitiWebSqlException;

	public List<AppSiteDetails> getHubCityRetailer(AppSiteDetails appSiteDetails) throws HubCitiWebSqlException;

	public List<AppSiteDetails> displayRetailLocations(AppSiteDetails appSiteDetails) throws HubCitiWebSqlException;

	public String saveAppSite(AppSiteDetails appSiteDetails) throws HubCitiWebSqlException;

	/**
	 * This will save Anything screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	String saveAnyThingScreen(ScreenSettings screenSettings, User user) throws HubCitiWebSqlException;

	/**
	 * This DAO method will return list of images to display
	 * 
	 * @return HubCitiImages Icon List List.
	 * @throws HubCitiWebSqlException
	 *             as SQL Exception will be thrown.
	 */
	List<HubCitiImages> getHubCitiImageIconsDisplay(String pageType) throws HubCitiWebSqlException;

	/**
	 * This will display anything pages created by user.
	 * 
	 * @param user
	 * @param searchKey
	 * @param lowerLimit
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public AnythingPages fetchAnythingPages(User user, String searchKey, Integer lowerLimit) throws HubCitiWebSqlException;

	/**
	 * This will return anything page types.
	 * 
	 * @return
	 * @throws HubCitiServiceException
	 */
	public List<HubCitiImages> getAnythingPageType() throws HubCitiWebSqlException;

	/**
	 * This Method will return the anything page details.
	 * 
	 * @param objScreenSettings
	 * @param objUser
	 * @return
	 * @throws HubCitiServiceException
	 */
	public ScreenSettings getAnyThingPage(ScreenSettings objScreenSettings, User objUser) throws HubCitiWebSqlException;

	/**
	 * This Method will update anything page details.
	 * 
	 * @param objScreenSettings
	 * @param user
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String updateAnyThingScreen(ScreenSettings objScreenSettings, User objUser) throws HubCitiWebSqlException;

	/**
	 * This Method will update anything page details.
	 * 
	 * @param objScreenSettings
	 * @param user
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String deleteAnyThingPage(String anythingPageID, User objUser) throws HubCitiWebSqlException;

	public ScreenSettings fetchGeneralSettings(Integer hubCitiID, String settingType) throws HubCitiWebSqlException;

	public List<Category> fetchBusinessCategoryList() throws HubCitiWebSqlException;

	/**
	 * This method is used to add alert category name to database.
	 */

	String addAlertCategory(String catName, User user) throws HubCitiWebSqlException;

	public AlertCategory fetchAlertCategories(Category category, User user) throws HubCitiWebSqlException;

	/**
	 * This method is used to add alert category name to database.
	 */

	String deleteAlertCategory(int cateId, User user) throws HubCitiWebSqlException;

	/**
	 * This method is used to update alert category.
	 */
	String updateAlertCategory(Category category, User user) throws HubCitiWebSqlException;

	public MenuFilterTyes getMenuFilterTypes(int hubCitiId) throws HubCitiWebSqlException;

	public List<Severity> fetchAlertSeverities() throws HubCitiWebSqlException;

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
	public AlertsDetails displaySearchAlerts(Integer userId, Integer hubCitiId, String searchKey, Integer lowerLimit) throws HubCitiWebSqlException;

	/**
	 * This method will save alter details.
	 * 
	 * @param alerts
	 * @param objUser
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String saveAlerts(Alerts alerts, User objUser) throws HubCitiWebSqlException;

	/**
	 * This method will return alter details.
	 * 
	 * @param alertID
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public Alerts fetchAlertDetails(Integer alertId) throws HubCitiWebSqlException;

	/**
	 * This method will update alter details.
	 * 
	 * @param alerts
	 * @param objUser
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String updateAlerts(Alerts alerts, User objUser) throws HubCitiWebSqlException;

	/**
	 * This method will delete alter.
	 * 
	 * @param alertID
	 * @param objUser
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String deleteAlerts(Integer alertID, User objUser) throws HubCitiWebSqlException;

	public List<ScreenSettings> displayButtomBtnType() throws HubCitiWebSqlException;

	// ######################### EVENT FUNCTIONALITY METHODS
	// ######################################################

	/**
	 * This method is used to fetch event categories.
	 */
	public AlertCategory fetchEventCategories(Category category, User user) throws HubCitiWebSqlException;

	/**
	 * This method is used to add event category.
	 */
	String addEventCategory(String catName, User user) throws HubCitiWebSqlException;

	/**
	 * This method is used to delete event category.
	 */

	String deleteEventCategory(int cateId, User user) throws HubCitiWebSqlException;

	/**
	 * This method is used to update event category name to database.
	 * 
	 * @param category
	 * @param user
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	String updateEventCategory(Category category, User user) throws HubCitiWebSqlException;

	EventDetail displayEvents(Event event, User user, Boolean fundraising) throws HubCitiWebSqlException;

	/**
	 * This method is used to fetch alert category.
	 */
	String deleteEvent(Integer eventId, User user) throws HubCitiWebSqlException;

	String deleteRetLocation(CityExperience cityExperience) throws HubCitiWebSqlException;

	public List<SearchZipCode> getZipStateCity(String zipCode, Integer hubCitiId) throws HubCitiWebSqlException;

	public List<SearchZipCode> getCityStateZip(String zipCode, Integer hubCitiId) throws HubCitiWebSqlException;

	public List<State> getAllStates(Integer hubCitiId) throws HubCitiWebSqlException;

	public List<RetailLocation> getHotelList(Integer hubCitiId, String searchKey) throws HubCitiWebSqlException;

	public String saveUpdateEventDeatils(Event eventDetails, User user) throws HubCitiWebSqlException;

	/**
	 * This method is used to fetch alert category.
	 */
	CityExperienceDetail displayCityExperience(CityExperience cityExperience, User user, Integer lowerLimit) throws HubCitiWebSqlException;

	CityExperienceDetail searchCityExperience(String retName, Integer lowerLimit, Integer filterID, User user) throws HubCitiWebSqlException;

	String saveCityExpRetLocs(String retLocIds, CityExperience cityExperience, User user) throws HubCitiWebSqlException;

	Event fetchEventDetails(Integer eventId) throws HubCitiWebSqlException;

	public List<RetailLocation> getEventHotelList(Integer eventId) throws HubCitiWebSqlException;

	/**
	 * This Method will display filters created by the user.
	 * 
	 * @param filters
	 * @param user
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public FiltersDetails displayFilters(ScreenSettings filters, User user) throws HubCitiWebSqlException;

	/**
	 * This method will save filter details
	 * 
	 * @param filters
	 * @param user
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String saveFilters(Filters filters, User user) throws HubCitiWebSqlException;

	/**
	 * This method will return filter details.
	 * 
	 * @param hubCitiId
	 * @param filterId
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public ScreenSettings fetchFilterDetails(Integer hubCitiId, Integer filterId) throws HubCitiWebSqlException;

	/**
	 * This method will de-associates the filter associated location.
	 * 
	 * @param filterID
	 * @param retailLocIDs
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String deAssociateFilterRetailLocs(Integer filterID, String retailLocIDs) throws HubCitiWebSqlException;

	/**
	 * This method will delete the filter.
	 * 
	 * @param filterID
	 * @param hubCitiID
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String deleteFilter(Integer filterID, Integer hubCitiID) throws HubCitiWebSqlException;

	/**
	 * This method is to fetch the user settings details.
	 * 
	 * @param hubCitiID
	 * @return userSettings details
	 * @throws HubCitiServiceException
	 */
	public ScreenSettings fetchUserSettings(Integer hubCitiID) throws HubCitiWebSqlException;

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
	public String saveUserSettings(ScreenSettings userSettings, User user) throws HubCitiWebSqlException;

	// ########################################### SETUP RETAILER LOCATION
	// METHODS ################################

	/**
	 * This method is used to display state list.
	 * 
	 * @param iHubCitiId
	 *            as hubciti id
	 * @return state list
	 * @throws HubCitiServiceException
	 */
	public List<CityExperience> getStatelst(int iHubCitiId) throws HubCitiWebSqlException;

	/**
	 * This method is used to display state list.
	 * 
	 * @param iHubCitiId
	 *            as hubciti id
	 * @param strState
	 *            as state
	 * @return city list based on state.
	 * @throws HubCitiServiceException
	 */
	public List<CityExperience> getCitilst(int iHubCitiId, String strState) throws HubCitiWebSqlException;

	/**
	 * This method is used to fetch zipcode list based on state and city.
	 * 
	 * @param cityExperience
	 * @return zipcode list
	 * @throws HubCitiServiceException
	 */
	public List<CityExperience> getZipcodelst(CityExperience cityExperience) throws HubCitiWebSqlException;

	/**
	 * This method is used to fetch retailer.
	 * 
	 * @param cityExperience
	 * @return
	 * @throws HubCitiServiceException
	 */

	public CityExperienceDetail getRetailer(CityExperience cityExperience) throws HubCitiWebSqlException;

	public String deAssociateRetailer(CityExperience cityExperience) throws HubCitiWebSqlException;

	public String associateRetailer(CityExperience cityExperience) throws HubCitiWebSqlException;

	/**
	 * This method will return list of event patterns.
	 * 
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public List<Event> getEventPatterns() throws HubCitiWebSqlException;

	/**
	 * This method will return list of user created FAQ's.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public FAQDetails fetchFQAs(FAQ faq) throws HubCitiWebSqlException;

	/**
	 * This method will return list of user created FAQ Categories.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public FAQDetails fetchFAQCategories(FAQ faq) throws HubCitiWebSqlException;

	/**
	 * This method will save FAQ details.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String saveFAQs(FAQ faq) throws HubCitiWebSqlException;

	/**
	 * This method will return FAQ Details.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public FAQ fetchFAQDetails(FAQ faq) throws HubCitiWebSqlException;

	/**
	 * This method will delete FAQ Details.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String deleteFAQ(FAQ faq) throws HubCitiWebSqlException;

	/**
	 * This method will save FAQ Category Details.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String addUpdateFAQCategory(FAQ faq) throws HubCitiWebSqlException;

	/**
	 * This method will delete FAQ Category.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String deleteFAQCategory(FAQ faq) throws HubCitiWebSqlException;

	public List<ScreenSettings> getMenuButtonType() throws HubCitiWebSqlException;

	/**
	 * This method is used to delete submenu.
	 * 
	 * @param screenSettings
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String deleteSubMenu(ScreenSettings screenSettings, Integer iHubCityId) throws HubCitiWebSqlException;

	public String saveFaqCateReorder(FAQ faq) throws HubCitiWebSqlException;

	public String saveFaqReorder(FAQ faq) throws HubCitiWebSqlException;

	public String insertFilterOrder(int hcHubCitiID, String hcFilterID, int hcCityExoerienceID, String sortOrder, int userID)
			throws HubCitiWebSqlException;
	
	public List<ScreenSettings> displayModuleTabBars(Integer userId, Integer hubCitiId) throws HubCitiWebSqlException;
	
	public List<ScreenSettings> displayModules(Integer userId, Integer hubCitiId) throws HubCitiWebSqlException;
	
	/**
	 * This method will save module tab bar details.
	 * 
	 * @param screenSettings
	 * @param objUser
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String saveModuleTabBar(ScreenSettings screenSettings, User objUser) throws HubCitiWebSqlException;
	/**
	 * This method will delete module tab bar details.
	 * 
	 * @param screenSettings
	 * @param objUser
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String deleteModuleTabBar(ScreenSettings screenSettings, User objUser) throws HubCitiWebSqlException;
	
	public UserDetails displayHubCitiCreatedUsers(User user) throws HubCitiWebSqlException;
	
	public List<Module> displayUserModules(Integer hubCitiID, Integer roleUserId) throws HubCitiWebSqlException;
	
	public String saveUpdateUserDeatils(User user) throws HubCitiWebSqlException;
	
	public User fetchUserDetails(Integer userId) throws HubCitiWebSqlException;
	/**
	 * This method will activate or de-activate requested user.
	 * 
	 * @param userId
	 * @return
	 * @throws HubCitiWebSqlException
	 */
	public String activateDeactivateUsers(Integer userId) throws HubCitiWebSqlException;	
	public List<CityExperience> displayCitiesForRegionApp(User user) throws HubCitiWebSqlException;	
	public AlertCategory fetchFundraiserEventCategories(User objUser) throws HubCitiWebSqlException;
	public List<Department> fetchFundraiserDepartments(User objUser) throws HubCitiWebSqlException;
	public String saveUpdateFundraiserEventDeatils(Event eventDetails, User user) throws HubCitiWebSqlException;
	public String addFundraiserDept(String catName, User objUser) throws HubCitiWebSqlException;
	public Event fetchFundraiserDetails(Integer eventId) throws HubCitiWebSqlException;
	public AlertCategory fetchFundraiserCategories(Category objCategory, User objUser) throws HubCitiWebSqlException;
	public DealDetails fetchDeals(Deals deals, User objUser) throws HubCitiWebSqlException;
	public String saveDealOfTheDay(Deals deals, User objUser) throws HubCitiWebSqlException;

	
	public String saveEvtMarkerInfo(Event event,User user)throws HubCitiWebSqlException;
	
	public ArrayList<Event> getEvtMarkerInfo(Event event,User user)throws HubCitiWebSqlException;
	
	public String deleteEvtMarker(Event event)throws HubCitiWebSqlException;

	
	//public List<Category> getCategoryImageDetails(Integer userId, Integer hubCitiId) throws HubCitiWebSqlException;
	
	//public String updateCategoryImage(ScreenSettings screenSettings, User user) throws HubCitiWebSqlException;
	
	public Event getEventLogisticsButtonDetails(Integer hubCitiId, Integer eventId, Integer userId) throws HubCitiWebSqlException;
	
	public ArrayList<Event> getMarkerInfo(Event event, User user) throws HubCitiWebSqlException;


}
