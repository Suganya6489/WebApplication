package com.hubciti.service;

import java.util.ArrayList;
import java.util.List;

import com.hubciti.common.exception.HubCitiServiceException;
import com.hubciti.common.pojo.AlertCategory;
import com.hubciti.common.pojo.Alerts;
import com.hubciti.common.pojo.AlertsDetails;
import com.hubciti.common.pojo.AnythingPages;
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
import com.hubciti.common.pojo.TabBarDetails;
import com.hubciti.common.pojo.User;
import com.hubciti.common.pojo.UserDetails;

public interface HubCitiService {
	/**
	 * This will return login screen details.
	 * 
	 * @param loginUser
	 * @return
	 * @throws HubCitiServiceException
	 */
	public ScreenSettings fetchScreenSettings(User loginUser)
			throws HubCitiServiceException;

	/**
	 * This will save login screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String saveLoginScreenSettings(ScreenSettings screenSettings,
			User user) throws HubCitiServiceException;

	/**
	 * This will save registration screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String saveRegScreenSettings(ScreenSettings screenSettings, User user)
			throws HubCitiServiceException;

	/**
	 * This will save About us screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String saveAboutusScreenSettings(ScreenSettings screenSettings,
			User user) throws HubCitiServiceException;

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
			throws HubCitiServiceException;

	/**
	 * This will save Splash Screen screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String saveSplashScreenSettings(ScreenSettings screenSettings,
			User user) throws HubCitiServiceException;

	/**
	 * This will save the new password for the user.
	 * 
	 * @param objUser
	 * @return
	 * @throws HubCitiServiceException
	 */
	User forgotPwd(User objUser) throws HubCitiServiceException;

	/**
	 * This will save General Settings screen details.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String saveGeneralSettings(ScreenSettings screenSettings, User user)
			throws HubCitiServiceException;

	/**
	 * This methos is used to get the logged in user details.
	 * 
	 * @param userName
	 * @return User
	 * @throws HubCitiServiceException
	 */
	public User getLoginAdminDetails(String userName)
			throws HubCitiServiceException;

	/**
	 * This method returns the page details.
	 * 
	 * @param hubCitiId
	 * @return PageStatus
	 * @throws HubCitiServiceException
	 */
	public PageStatus getScreenStatus(int hubCitiId)
			throws HubCitiServiceException;

	/**
	 * This method updates the password with new password.
	 * 
	 * @param User
	 * @return String
	 * @throws HubCitiServiceException
	 */
	public String saveChangedPassword(User user) throws HubCitiServiceException;

	public String createMenu(MenuDetails menuDetails, User user)
			throws HubCitiServiceException;

	public List<MenuDetails> getLinkList() throws HubCitiServiceException;

	public String saveUpdateIconicMenuTemplate(User loginuser,
			List<ScreenSettings> menuItemsList, Integer menuId,
			Integer menuLevel,

			String menuName, String bottmBtnId, String templateType,
			String bannerImg, boolean tyepFilter, boolean deptFilter)

	throws HubCitiServiceException;

	public MenuDetails displayMenu(MenuDetails menuDetails, User user)
			throws HubCitiServiceException;

	public SubMenuDetails displaySubMenu(User user,
			ScreenSettings screenSettings) throws HubCitiServiceException;

	List<AppSiteDetails> getAppSites(String searchKey, int ihubCityId,
			Integer lowerLimit) throws HubCitiServiceException;

	public String saveUpdateTwoTabMenuTemplate(User loginuser,
			List<ScreenSettings> menuItemsList, Integer menuId,
			Integer menuLevel, String bannerImg, String menuName,
			String bottmBtnId, String templateType, boolean tyepFilter,
			boolean deptFilter, String btnView) throws HubCitiServiceException;

	/**
	 * This will save login screen details.
	 * 
	 * @param objScreenSettings
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	String saveAnyThingScreen(ScreenSettings objScreenSettings, User user)
			throws HubCitiServiceException;

	/**
	 * This service method is used for fetching the image icons to display in
	 * link page.
	 * 
	 * @return
	 * @throws ScanSeeServiceException
	 *             will be thrown as service exception.
	 */
	List<HubCitiImages> getHubCitiImageIconsDisplay(String pageType)
			throws HubCitiServiceException;

	public List<MenuDetails> getBottomLinkList(int ihubCityId)
			throws HubCitiServiceException;

	public List<ScreenSettings> getBottomBarExistingIcons()
			throws HubCitiServiceException;

	public String saveUpdateTabBarButton(ScreenSettings buttondetails,
			User loginUser) throws HubCitiServiceException;

	public List<ScreenSettings> getTabBarButtons(MenuDetails menuDetails,
			User loginUser) throws HubCitiServiceException;

	public String deleteTabBarButton(Integer bottomBtnId)
			throws HubCitiServiceException;

	public List<AppSiteDetails> getHubCityRetailer(AppSiteDetails appSiteDetails)
			throws HubCitiServiceException;

	List<AppSiteDetails> displayRetailLocations(AppSiteDetails appSiteDetails)
			throws HubCitiServiceException;

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
			Integer lowerLimit) throws HubCitiServiceException;

	/**
	 * This will return anything page types.
	 * 
	 * @return
	 * @throws HubCitiServiceException
	 */
	public List<HubCitiImages> getAnythingPageType()
			throws HubCitiServiceException;

	/**
	 * This Method will return the anything page details.
	 * 
	 * @param objScreenSettings
	 * @param objUser
	 * @return
	 * @throws HubCitiServiceException
	 */
	public ScreenSettings getAnythingPage(ScreenSettings objScreenSettings,
			User objUser) throws HubCitiServiceException;

	/**
	 * This Method will update anything page details.
	 * 
	 * @param objScreenSettings
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String updateAnyThingScreen(ScreenSettings objScreenSettings,
			User user) throws HubCitiServiceException;

	String saveAppSite(AppSiteDetails appSiteDetails)
			throws HubCitiServiceException;

	/**
	 * This Method will delete anything page .
	 * 
	 * @param anythingPageID
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String deleteAnyThingPage(String anythingPageID, User objUser)
			throws HubCitiServiceException;

	public ScreenSettings fetchGeneralSettings(Integer hubCitiID,
			String settingType) throws HubCitiServiceException;

	public List<Category> fetchBusinessCategoryList()
			throws HubCitiServiceException;

	/**
	 * This method is used to add alert category.
	 */
	String addAlertCategory(String catName, User user)
			throws HubCitiServiceException;

	/**
	 * This method is used to fetch alert category.
	 */
	public AlertCategory fetchAlertCategories(Category category, User user)
			throws HubCitiServiceException;

	/**
	 * This method is used to delete alert category.
	 */
	String deleteAlertCategory(int cateId, User user)
			throws HubCitiServiceException;

	/**
	 * This method is used to update alert category.
	 */
	String updateAlertCategory(Category category, User user)
			throws HubCitiServiceException;

	public MenuFilterTyes getMenuFilterTypes(int hubCitiId)
			throws HubCitiServiceException;

	public List<Severity> fetchAlertSeverities() throws HubCitiServiceException;

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
			throws HubCitiServiceException;

	/**
	 * This method will save alter details.
	 * 
	 * @param alerts
	 * @param objUser
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String saveAlerts(Alerts alerts, User objUser)
			throws HubCitiServiceException;

	/**
	 * This method will return alter details.
	 * 
	 * @param alertID
	 * @return
	 * @throws HubCitiServiceException
	 */
	public Alerts fetchAlertDetails(Integer alertId)
			throws HubCitiServiceException;

	/**
	 * This method will update alter details.
	 * 
	 * @param alerts
	 * @param objUser
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String updateAlerts(Alerts alerts, User objUser)
			throws HubCitiServiceException;

	/**
	 * This method will delete alter.
	 * 
	 * @param alertID
	 * @param objUser
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String deleteAlerts(Integer alertID, User objUser)
			throws HubCitiServiceException;

	public List<ScreenSettings> displayButtomBtnType()
			throws HubCitiServiceException;

	// ######################### EVENT FUNCTIONALITY METHODS
	// ######################################################

	/**
	 * This method is used to fetch alert category.
	 */
	public AlertCategory fetchEventCategories(Category category, User user)
			throws HubCitiServiceException;

	/**
	 * This method is used to add alert category.
	 */
	String addEventCategory(String catName, User user)
			throws HubCitiServiceException;

	/**
	 * This method is used to delete alert category.
	 */
	String deleteEventCategory(int cateId, User user)
			throws HubCitiServiceException;

	/**
	 * This method is used to update alert category.
	 */
	String updateEventCategory(Category category, User user)
			throws HubCitiServiceException;

	/**
	 * This method is used to display events
	 */

	EventDetail displayEvents(Event event, User user, Boolean fundraising)
			throws HubCitiServiceException;

	/**
	 * This method is used to delete events
	 */

	CityExperienceDetail displayCityExperience(CityExperience cityExperience,
			User user, Integer lowerLimit) throws HubCitiServiceException;

	String deleteEvent(Integer eventId, User user)
			throws HubCitiServiceException;

	/**
	 * This method is used to fetch alert category.
	 */
	CityExperienceDetail searchCityExperience(String retName,
			Integer lowerLimit, Integer filterID, User user)
			throws HubCitiServiceException;

	/**
	 * This Method will display filters created by the user.
	 * 
	 * @param filters
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	public FiltersDetails displayFilters(ScreenSettings filters, User user)
			throws HubCitiServiceException;

	String deleteRetLocation(CityExperience cityExperience)
			throws HubCitiServiceException;

	/**
	 * This method will save filter details
	 * 
	 * @param filters
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String saveFilters(Filters filters, User user)
			throws HubCitiServiceException;

	public List<SearchZipCode> getZipStateCity(String zipCode, Integer hubCitiId)
			throws HubCitiServiceException;

	String saveCityExpRetLocs(String retLocIds, CityExperience cityExperience,
			User user) throws HubCitiServiceException;

	public List<SearchZipCode> getCityStateZip(String city, Integer hubCitiId)
			throws HubCitiServiceException;

	public List<State> getAllStates(Integer hubCitiID)
			throws HubCitiServiceException;

	public List<RetailLocation> getHotelList(Integer hubCitiId, String searchKey)
			throws HubCitiServiceException;

	public String saveUpdateEventDeatils(Event eventDetails, User user)
			throws HubCitiServiceException;

	public Event fetchEventDetails(Integer eventId)
			throws HubCitiServiceException;

	public List<RetailLocation> getEventHotelList(Integer eventId)
			throws HubCitiServiceException;

	/**
	 * This method will return filter details.
	 * 
	 * @param hubCitiId
	 * @param filterId
	 * @return
	 * @throws HubCitiServiceException
	 */
	public ScreenSettings fetchFilterDetails(Integer hubCitiId, Integer filterId)
			throws HubCitiServiceException;

	/**
	 * This method will de-associates the filter associated location.
	 * 
	 * @param filterID
	 * @param retailLocIDs
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String deAssociateFilterRetailLocs(Integer filterID,
			String retailLocIDs) throws HubCitiServiceException;

	/**
	 * This method will delete the filter.
	 * 
	 * @param filterID
	 * @param hubCitiID
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String deleteFilter(Integer filterID, Integer hubCitiID)
			throws HubCitiServiceException;

	/**
	 * This method is to fetch the user settings details.
	 * 
	 * @param hubCitiID
	 * @return userSettings details
	 * @throws HubCitiServiceException
	 */
	public ScreenSettings fetchUserSettings(Integer hubCitiID)
			throws HubCitiServiceException;

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
	public String saveUserSettings(ScreenSettings userSettings, User user)
			throws HubCitiServiceException;

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
	public List<CityExperience> getStatelst(int iHubCitiId)
			throws HubCitiServiceException;

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
	public List<CityExperience> getCitilst(int iHubCitiId, String strState)
			throws HubCitiServiceException;

	/**
	 * This method is used to fetch zipcode list based on state and city.
	 * 
	 * @param cityExperience
	 * @return zipcode list
	 * @throws HubCitiServiceException
	 */
	public List<CityExperience> getZipcodelst(CityExperience cityExperience)
			throws HubCitiServiceException;

	/**
	 * This method is used to fetch retaier locations.
	 * 
	 * @param cityExperience
	 * @return
	 * @throws HubCitiServiceException
	 */

	public CityExperienceDetail getRetailer(CityExperience cityExperience)
			throws HubCitiServiceException;

	public String deAssociateRetailer(CityExperience cityExperience)
			throws HubCitiServiceException;

	public String associateRetailer(CityExperience cityExperience)
			throws HubCitiServiceException;

	/**
	 * This method will return list of event patterns.
	 * 
	 * @return
	 * @throws HubCitiServiceException
	 */
	public List<Event> getEventPatterns() throws HubCitiServiceException;

	/**
	 * This method will return list of user created FAQ's.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiServiceException
	 */
	public FAQDetails fetchFQAs(FAQ faq) throws HubCitiServiceException;

	/**
	 * This method will return list of user created FAQ Categories.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiServiceException
	 */
	public FAQDetails fetchFAQCategories(FAQ faq)
			throws HubCitiServiceException;

	/**
	 * This method saves the FAQ Details.
	 * 
	 * @param faq
	 * @return String
	 * @throws HubCitiServiceException
	 */
	public String saveFAQs(FAQ faq) throws HubCitiServiceException;

	/**
	 * This method will return FAQ Details.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiServiceException
	 */
	public FAQ fetchFAQDetails(FAQ faq) throws HubCitiServiceException;

	/**
	 * This method will delete FAQ Details.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String deleteFAQ(FAQ faq) throws HubCitiServiceException;

	/**
	 * This method will save FAQ Category Details.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String addUpdateFAQCategory(FAQ faq) throws HubCitiServiceException;

	/**
	 * This method will delete FAQ Category Details.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String deleteFAQCategory(FAQ faq) throws HubCitiServiceException;

	public List<ScreenSettings> getMenuButtonType()
			throws HubCitiServiceException;

	/**
	 * This method is used to delete submenu.
	 * 
	 * @param screenSettings
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String deleteSubMenu(ScreenSettings screenSettings,
			Integer iHubCityId) throws HubCitiServiceException;

	/**
	 * This method is used to save FAQ category re order.
	 * 
	 * @param faq
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String saveFaqCateReorder(FAQ faq) throws HubCitiServiceException;

	public String saveFaqReorder(FAQ faq) throws HubCitiServiceException;

	public String saveFilterOrder(int hcHubCitiID, String hcFilterID,
			int hcCityExoerienceID, String sortOrder, int userID)
			throws HubCitiServiceException;

	public List<TabBarDetails> displayModuleTabBars(Integer userId,
			Integer hubCitiId) throws HubCitiServiceException;

	public List<ScreenSettings> displayModules(Integer userId, Integer hubCitiId)
			throws HubCitiServiceException;

	/**
	 * This method will save module tab bar details.
	 * 
	 * @param screenSettings
	 * @param objUser
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String saveModuleTabBar(ScreenSettings screenSettings, User objUser)
			throws HubCitiServiceException;

	/**
	 * This method will delete module tab bar details.
	 * 
	 * @param screenSettings
	 * @param objUser
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String deleteModuleTabBar(ScreenSettings screenSettings, User objUser)
			throws HubCitiServiceException;

	public UserDetails displayHubCitiCreatedUsers(User user)
			throws HubCitiServiceException;

	public List<Module> displayUserModules(Integer hubCitiID, Integer roleUserId)
			throws HubCitiServiceException;

	public String createUserDeatils(User user, User loginUser)
			throws HubCitiServiceException;

	public String updateUserDeatils(User user, User loginUser)
			throws HubCitiServiceException;

	public User fetchUserDetails(Integer userId) throws HubCitiServiceException;

	/**
	 * This method will activate or de-activate requested user.
	 * 
	 * @param userId
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String activateDeactivateUsers(Integer userId)
			throws HubCitiServiceException;

	public List<CityExperience> displayCitiesForRegionApp(User user)
			throws HubCitiServiceException;

	public AlertCategory fetchFundraiserEventCategories(User user)
			throws HubCitiServiceException;

	public List<Department> fetchFundraiserDepartments(User objUser)
			throws HubCitiServiceException;

	public String saveUpdateFundraiserEventDeatils(Event eventDetails, User user)
			throws HubCitiServiceException;

	public String addFundraiserDept(String catName, User objUser)
			throws HubCitiServiceException;

	public Event fetchFundraiserDetails(Integer eventId)
			throws HubCitiServiceException;

	public AlertCategory fetchFundraiserCategories(Category category, User user)
			throws HubCitiServiceException;

	public DealDetails fetchDeals(Deals deals, User user)
			throws HubCitiServiceException;

	public String saveDealOfTheDay(Deals deals, User user)
			throws HubCitiServiceException;

	public String saveEvtMarkerInfo(Event event, User user)
			throws HubCitiServiceException;

	public ArrayList<Event> getEvtMarkerInfo(Event event, User user)
			throws HubCitiServiceException;
	
	public String deleteEvtMarker(Event event)throws HubCitiServiceException;
	
	//public List<Category> getCategoryImageDetails(Integer userId, Integer hubCitiId) throws HubCitiServiceException;
	
	/**
	 * This will save category image.
	 * 
	 * @param screenSettings
	 * @param user
	 * @return
	 * @throws HubCitiServiceException
	 */
	//public String updateCategoryImage(ScreenSettings screenSettings, User user) throws HubCitiServiceException;
	
	public Event getEventLogisticsButtonDetails(Integer hubCitiId, Integer eventId, Integer userId) throws HubCitiServiceException;
	
	public ArrayList<Event>  getMarkerInfo(Event event, User user) throws HubCitiServiceException;
}
