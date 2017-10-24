package com.hubciti.common.pojo;

import java.util.List;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class Event {

	private int rowNum;
	private Integer hcEventID;
	private String hcEventName;
	private String hcEventCategoryName;

	private String eventDate;
	private String eventTime;
	private String eventTimeHrs;
	private String eventTimeMins;

	private String address;
	private String city;
	private String state;
	private String postalCode;
	private String eventSearchKey;
	private Integer lowerLimit;
	private boolean menuItemExist;
	private CommonsMultipartFile eventImageFile;
	private String eventImageName;
	private String longDescription;
	private String shortDescription;
	private String bsnsLoc;
	private String[] appsiteID;
	private String evntPckg;
	private String packagePrice;
	private String packageTicketURL;
	private String packageDescription;
	private String[] retailLocationID;
	private String hotelPrice;
	private String discountCode;
	private String discountAmount;
	private String rating;
	private String roomAvailabilityCheckURL;
	private String roomBookingURL;
	private String eventCategory;
	private String appSiteSearchKey;
	private String hotelSearchKey;
	private String evntHotel;
	private String viewName;
	private String hiddenState;
	private String hiddenCategory;
	private String hotelListJson;

	private Double latitude;
	private Double logitude;
	private boolean geoError;
	private String hotelID;
	private boolean showEventPacgTab;
	private List<RetailLocation> hotelList;
	private String eventImagePath;
	private String appsiteIDs;
	private String retailLocationIDs;
	private String searchType;
	private String moreInfoURL;

	private String isOngoing;
	private String isOngoingDaily;
	private String isOngoingMonthly;
	private String eventStartDate;
	private String eventStartTime;
	private String eventStartTimeHrs;
	private String eventStartTimeMins;

	private String eventEndDate;
	private String eventEndTime;
	private String eventEndTimeHrs;
	private String eventEndTimeMins;

	private Integer recurrencePatternID;
	private String recurrencePatternName;

	private Integer everyWeekDay;
	private Integer everyWeek;
	private Integer dateOfMonth;
	private Integer everyMonth;
	private Integer everyDayMonth;
	private String[] everyWeekDayMonth;
	private String dayOfMonth;
	private String[] days;
	private Integer endAfter;
	private Integer dayNumber;
	private String occurenceType;
	private Integer recurrenceInterval;
	private Boolean isWeekDay;
	private Boolean byDayNumber;
	private String hiddenDays;
	private String hiddenDay;
	private Integer hiddenDate;
	private Integer hiddenWeek;
	private String hiddenWeekDay;
	private String eventEDate;
	private String eventETime;
	private String eventETimeHrs;
	private String eventETimeMins;
	private String hiddenDept;
	private Integer departmentId;
	private Integer departmentName;
	private String isEventAppsite;
	private String organizationHosting;
	private String isEventTied;
	private String eventTiedIds;
	private String fundraisingGoal;
	private String currentLevel;
	private String purchaseProducts;
	private String screenName;

	private Integer evtMarkerId;
	private String evtMarkerName;
	private String evtMarkerImgPath;
	private CommonsMultipartFile evtMarkerImgFile;

	
	private String isEventLogistics;
	private Integer buttonId;
	private String buttonName;
	private String buttonLink;
	private List<Event> logisticsBtnList;
	private String logisticsImgName;
	private CommonsMultipartFile logisticsImgFile;
	private String logisticsImgPath;
	private String imgUploadFor;
//	private String isLogisticsHTMLActive;
	private Boolean showLogisticsTab;
	private String isSignatureEvent;
	private Boolean isNewLogisticsImg;
	private String isEventOverlay;

	private String markerBtnName;

	private String btnNames;
	private String btnLinks;
	private String locationTitle;

	

	/**
	 * @return the isOngoingDaily
	 */
	public String getIsOngoingDaily() {
		return isOngoingDaily;
	}

	/**
	 * @param isOngoingDaily
	 *            the isOngoingDaily to set
	 */
	public void setIsOngoingDaily(String isOngoingDaily) {
		this.isOngoingDaily = isOngoingDaily;
	}

	/**
	 * @return the isOngoingMonthly
	 */
	public String getIsOngoingMonthly() {
		return isOngoingMonthly;
	}

	/**
	 * @param isOngoingMonthly
	 *            the isOngoingMonthly to set
	 */
	public void setIsOngoingMonthly(String isOngoingMonthly) {
		this.isOngoingMonthly = isOngoingMonthly;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getEventSearchKey() {
		return eventSearchKey;
	}

	public void setEventSearchKey(String eventSearchKey) {
		this.eventSearchKey = eventSearchKey;
	}

	public Integer getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(Integer lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public Integer getHcEventID() {
		return hcEventID;
	}

	public void setHcEventID(Integer hcEventID) {
		this.hcEventID = hcEventID;
	}

	public String getHcEventName() {
		return hcEventName;
	}

	public void setHcEventName(String hcEventName) {
		this.hcEventName = hcEventName;
	}

	public String getHcEventCategoryName() {
		return hcEventCategoryName;
	}

	public void setHcEventCategoryName(String hcEventCategoryName) {
		this.hcEventCategoryName = hcEventCategoryName;
	}

	public boolean isMenuItemExist() {
		return menuItemExist;
	}

	public void setMenuItemExist(boolean menuItemExist) {
		this.menuItemExist = menuItemExist;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public CommonsMultipartFile getEventImageFile() {
		return eventImageFile;
	}

	public void setEventImageFile(CommonsMultipartFile eventImageFile) {
		this.eventImageFile = eventImageFile;
	}

	public String getEventImageName() {
		return eventImageName;
	}

	public void setEventImageName(String eventImageName) {
		this.eventImageName = eventImageName;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getBsnsLoc() {
		return bsnsLoc;
	}

	public void setBsnsLoc(String bsnsLoc) {
		this.bsnsLoc = bsnsLoc;
	}

	public String[] getAppsiteID() {
		return appsiteID;
	}

	public void setAppsiteID(String[] appsiteID) {
		this.appsiteID = appsiteID;
	}

	public String getEvntPckg() {
		return evntPckg;
	}

	public void setEvntPckg(String evntPckg) {
		this.evntPckg = evntPckg;
	}

	public String getPackagePrice() {
		return packagePrice;
	}

	public void setPackagePrice(String packagePrice) {
		this.packagePrice = packagePrice;
	}

	public String getPackageTicketURL() {
		return packageTicketURL;
	}

	public void setPackageTicketURL(String packageTicketURL) {
		this.packageTicketURL = packageTicketURL;
	}

	public String getPackageDescription() {
		return packageDescription;
	}

	public void setPackageDescription(String packageDescription) {
		this.packageDescription = packageDescription;
	}

	public String[] getRetailLocationID() {
		return retailLocationID;
	}

	public void setRetailLocationID(String[] retailLocationID) {
		this.retailLocationID = retailLocationID;
	}

	public String getHotelPrice() {
		return hotelPrice;
	}

	public void setHotelPrice(String hotelPrice) {
		this.hotelPrice = hotelPrice;
	}

	public String getDiscountCode() {
		return discountCode;
	}

	public void setDiscountCode(String discountCode) {
		this.discountCode = discountCode;
	}

	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getRoomAvailabilityCheckURL() {
		return roomAvailabilityCheckURL;
	}

	public void setRoomAvailabilityCheckURL(String roomAvailabilityCheckURL) {
		this.roomAvailabilityCheckURL = roomAvailabilityCheckURL;
	}

	public String getRoomBookingURL() {
		return roomBookingURL;
	}

	public void setRoomBookingURL(String roomBookingURL) {
		this.roomBookingURL = roomBookingURL;
	}

	public String getEventCategory() {
		return eventCategory;
	}

	public void setEventCategory(String eventCategory) {
		this.eventCategory = eventCategory;
	}

	public String getAppSiteSearchKey() {
		return appSiteSearchKey;
	}

	public void setAppSiteSearchKey(String appSiteSearchKey) {
		this.appSiteSearchKey = appSiteSearchKey;
	}

	public String getHotelSearchKey() {
		return hotelSearchKey;
	}

	public void setHotelSearchKey(String hotelSearchKey) {
		this.hotelSearchKey = hotelSearchKey;
	}

	public String getEvntHotel() {
		return evntHotel;
	}

	public void setEvntHotel(String evntHotel) {
		this.evntHotel = evntHotel;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getHiddenState() {
		return hiddenState;
	}

	public void setHiddenState(String hiddenState) {
		this.hiddenState = hiddenState;
	}

	public String getHiddenCategory() {
		return hiddenCategory;
	}

	public void setHiddenCategory(String hiddenCategory) {
		this.hiddenCategory = hiddenCategory;
	}

	public String getEventTimeHrs() {
		return eventTimeHrs;
	}

	public void setEventTimeHrs(String eventTimeHrs) {
		this.eventTimeHrs = eventTimeHrs;
	}

	public String getEventTimeMins() {
		return eventTimeMins;
	}

	public void setEventTimeMins(String eventTimeMins) {
		this.eventTimeMins = eventTimeMins;
	}

	public String getHotelListJson() {
		return hotelListJson;
	}

	public void setHotelListJson(String hotelListJson) {
		this.hotelListJson = hotelListJson;
	}

	public List<RetailLocation> getHotelList() {
		return hotelList;
	}

	public void setHotelList(List<RetailLocation> hotelList) {
		this.hotelList = hotelList;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLogitude() {
		return logitude;
	}

	public void setLogitude(Double logitude) {
		this.logitude = logitude;
	}

	public boolean isGeoError() {
		return geoError;
	}

	public void setGeoError(boolean geoError) {
		this.geoError = geoError;
	}

	public String getHotelID() {
		return hotelID;
	}

	public void setHotelID(String hotelID) {
		this.hotelID = hotelID;
	}

	public boolean isShowEventPacgTab() {
		return showEventPacgTab;
	}

	public void setShowEventPacgTab(boolean showEventPacgTab) {
		this.showEventPacgTab = showEventPacgTab;
	}

	public String getEventImagePath() {
		return eventImagePath;
	}

	public void setEventImagePath(String eventImagePath) {
		this.eventImagePath = eventImagePath;
	}

	public String getAppsiteIDs() {
		return appsiteIDs;
	}

	public void setAppsiteIDs(String appsiteIDs) {
		this.appsiteIDs = appsiteIDs;
	}

	public String getRetailLocationIDs() {
		return retailLocationIDs;
	}

	public void setRetailLocationIDs(String retailLocationIDs) {
		this.retailLocationIDs = retailLocationIDs;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getMoreInfoURL() {
		return moreInfoURL;
	}

	public void setMoreInfoURL(String moreInfoURL) {
		this.moreInfoURL = moreInfoURL;
	}

	/**
	 * @return the eventStartDate
	 */
	public String getEventStartDate() {
		return eventStartDate;
	}

	/**
	 * @param eventStartDate
	 *            the eventStartDate to set
	 */
	public void setEventStartDate(String eventStartDate) {
		this.eventStartDate = eventStartDate;
	}

	/**
	 * @return the eventStartTime
	 */
	public String getEventStartTime() {
		return eventStartTime;
	}

	/**
	 * @param eventStartTime
	 *            the eventStartTime to set
	 */
	public void setEventStartTime(String eventStartTime) {
		this.eventStartTime = eventStartTime;
	}

	/**
	 * @return the eventStartTimeHrs
	 */
	public String getEventStartTimeHrs() {
		return eventStartTimeHrs;
	}

	/**
	 * @param eventStartTimeHrs
	 *            the eventStartTimeHrs to set
	 */
	public void setEventStartTimeHrs(String eventStartTimeHrs) {
		this.eventStartTimeHrs = eventStartTimeHrs;
	}

	/**
	 * @return the eventStartTimeMins
	 */
	public String getEventStartTimeMins() {
		return eventStartTimeMins;
	}

	/**
	 * @param eventStartTimeMins
	 *            the eventStartTimeMins to set
	 */
	public void setEventStartTimeMins(String eventStartTimeMins) {
		this.eventStartTimeMins = eventStartTimeMins;
	}

	/**
	 * @return the eventEndDate
	 */
	public String getEventEndDate() {
		return eventEndDate;
	}

	/**
	 * @param eventEndDate
	 *            the eventEndDate to set
	 */
	public void setEventEndDate(String eventEndDate) {
		this.eventEndDate = eventEndDate;
	}

	/**
	 * @return the eventEndTime
	 */
	public String getEventEndTime() {
		return eventEndTime;
	}

	/**
	 * @param eventEndTime
	 *            the eventEndTime to set
	 */
	public void setEventEndTime(String eventEndTime) {
		this.eventEndTime = eventEndTime;
	}

	/**
	 * @return the eventEndTimeHrs
	 */
	public String getEventEndTimeHrs() {
		return eventEndTimeHrs;
	}

	/**
	 * @param eventEndTimeHrs
	 *            the eventEndTimeHrs to set
	 */
	public void setEventEndTimeHrs(String eventEndTimeHrs) {
		this.eventEndTimeHrs = eventEndTimeHrs;
	}

	/**
	 * @return the eventEndTimeMins
	 */
	public String getEventEndTimeMins() {
		return eventEndTimeMins;
	}

	/**
	 * @param eventEndTimeMins
	 *            the eventEndTimeMins to set
	 */
	public void setEventEndTimeMins(String eventEndTimeMins) {
		this.eventEndTimeMins = eventEndTimeMins;
	}

	/**
	 * @return the recurrencePatternID
	 */
	public Integer getRecurrencePatternID() {
		return recurrencePatternID;
	}

	/**
	 * @param recurrencePatternID
	 *            the recurrencePatternID to set
	 */
	public void setRecurrencePatternID(Integer recurrencePatternID) {
		this.recurrencePatternID = recurrencePatternID;
	}

	/**
	 * @return the recurrencePatternName
	 */
	public String getRecurrencePatternName() {
		return recurrencePatternName;
	}

	/**
	 * @param recurrencePatternName
	 *            the recurrencePatternName to set
	 */
	public void setRecurrencePatternName(String recurrencePatternName) {
		this.recurrencePatternName = recurrencePatternName;
	}

	/**
	 * @return the days
	 */
	public String[] getDays() {
		return days;
	}

	/**
	 * @param days
	 *            the days to set
	 */
	public void setDays(String[] days) {
		this.days = days;
	}

	/**
	 * @return the endAfter
	 */
	public Integer getEndAfter() {
		return endAfter;
	}

	/**
	 * @param endAfter
	 *            the endAfter to set
	 */
	public void setEndAfter(Integer endAfter) {
		this.endAfter = endAfter;
	}

	/**
	 * @return the dayNumber
	 */
	public Integer getDayNumber() {
		return dayNumber;
	}

	/**
	 * @param dayNumber
	 *            the dayNumber to set
	 */
	public void setDayNumber(Integer dayNumber) {
		this.dayNumber = dayNumber;
	}

	/**
	 * @param occurenceType
	 *            the occurenceType to set
	 */
	public void setOccurenceType(String occurenceType) {
		this.occurenceType = occurenceType;
	}

	/**
	 * @return the occurenceType
	 */
	public String getOccurenceType() {
		return occurenceType;
	}

	/**
	 * @param isOngoing
	 *            the isOngoing to set
	 */
	public void setIsOngoing(String isOngoing) {
		this.isOngoing = isOngoing;
	}

	/**
	 * @return the isOngoing
	 */
	public String getIsOngoing() {
		return isOngoing;
	}

	/**
	 * @param everyWeekDay
	 *            the everyWeekDay to set
	 */
	public void setEveryWeekDay(Integer everyWeekDay) {
		this.everyWeekDay = everyWeekDay;
	}

	/**
	 * @return the everyWeekDay
	 */
	public Integer getEveryWeekDay() {
		return everyWeekDay;
	}

	/**
	 * @param everyWeek
	 *            the everyWeek to set
	 */
	public void setEveryWeek(Integer everyWeek) {
		this.everyWeek = everyWeek;
	}

	/**
	 * @return the everyWeek
	 */
	public Integer getEveryWeek() {
		return everyWeek;
	}

	/**
	 * @return the dateOfMonth
	 */
	public Integer getDateOfMonth() {
		return dateOfMonth;
	}

	/**
	 * @param dateOfMonth
	 *            the dateOfMonth to set
	 */
	public void setDateOfMonth(Integer dateOfMonth) {
		this.dateOfMonth = dateOfMonth;
	}

	/**
	 * @return the everyMonth
	 */
	public Integer getEveryMonth() {
		return everyMonth;
	}

	/**
	 * @param everyMonth
	 *            the everyMonth to set
	 */
	public void setEveryMonth(Integer everyMonth) {
		this.everyMonth = everyMonth;
	}

	/**
	 * @return the dayOfMonth
	 */
	public String getDayOfMonth() {
		return dayOfMonth;
	}

	/**
	 * @param dayOfMonth
	 *            the dayOfMonth to set
	 */
	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	/**
	 * @param everyDayMonth
	 *            the everyDayMonth to set
	 */
	public void setEveryDayMonth(Integer everyDayMonth) {
		this.everyDayMonth = everyDayMonth;
	}

	/**
	 * @return the everyDayMonth
	 */
	public Integer getEveryDayMonth() {
		return everyDayMonth;
	}

	/**
	 * @param everyWeekDayMonth
	 *            the everyWeekDayMonth to set
	 */
	public void setEveryWeekDayMonth(String[] everyWeekDayMonth) {
		this.everyWeekDayMonth = everyWeekDayMonth;
	}

	/**
	 * @return the everyWeekDayMonth
	 */
	public String[] getEveryWeekDayMonth() {
		return everyWeekDayMonth;
	}

	/**
	 * @param recurrenceInterval
	 *            the recurrenceInterval to set
	 */
	public void setRecurrenceInterval(Integer recurrenceInterval) {
		this.recurrenceInterval = recurrenceInterval;
	}

	/**
	 * @return the recurrenceInterval
	 */
	public Integer getRecurrenceInterval() {
		return recurrenceInterval;
	}

	/**
	 * @param isWeekDay
	 *            the isWeekDay to set
	 */
	public void setIsWeekDay(Boolean isWeekDay) {
		this.isWeekDay = isWeekDay;
	}

	/**
	 * @return the isWeekDay
	 */
	public Boolean getIsWeekDay() {
		return isWeekDay;
	}

	/**
	 * @param byDayNumber
	 *            the byDayNumber to set
	 */
	public void setByDayNumber(Boolean byDayNumber) {
		this.byDayNumber = byDayNumber;
	}

	/**
	 * @return the byDayNumber
	 */
	public Boolean getByDayNumber() {
		return byDayNumber;
	}

	/**
	 * @param hiddenDays
	 *            the hiddenDays to set
	 */
	public void setHiddenDays(String hiddenDays) {
		this.hiddenDays = hiddenDays;
	}

	/**
	 * @return the hiddenDays
	 */
	public String getHiddenDays() {
		return hiddenDays;
	}

	/**
	 * @return the hiddenDay
	 */
	public String getHiddenDay() {
		return hiddenDay;
	}

	/**
	 * @param hiddenDay
	 *            the hiddenDay to set
	 */
	public void setHiddenDay(String hiddenDay) {
		this.hiddenDay = hiddenDay;
	}

	/**
	 * @return the hiddenDate
	 */
	public Integer getHiddenDate() {
		return hiddenDate;
	}

	/**
	 * @param hiddenDate
	 *            the hiddenDate to set
	 */
	public void setHiddenDate(Integer hiddenDate) {
		this.hiddenDate = hiddenDate;
	}

	/**
	 * @return the hiddenWeek
	 */
	public Integer getHiddenWeek() {
		return hiddenWeek;
	}

	/**
	 * @param hiddenWeek
	 *            the hiddenWeek to set
	 */
	public void setHiddenWeek(Integer hiddenWeek) {
		this.hiddenWeek = hiddenWeek;
	}

	/**
	 * @return the hiddenWeekDay
	 */
	public String getHiddenWeekDay() {
		return hiddenWeekDay;
	}

	/**
	 * @param hiddenWeekDay
	 *            the hiddenWeekDay to set
	 */
	public void setHiddenWeekDay(String hiddenWeekDay) {
		this.hiddenWeekDay = hiddenWeekDay;
	}

	/**
	 * @return the eventEDate
	 */
	public String getEventEDate() {
		return eventEDate;
	}

	/**
	 * @param eventEDate the eventEDate to set
	 */
	public void setEventEDate(String eventEDate) {
		this.eventEDate = eventEDate;
	}

	/**
	 * @return the eventETime
	 */
	public String getEventETime() {
		return eventETime;
	}

	/**
	 * @param eventETime the eventETime to set
	 */
	public void setEventETime(String eventETime) {
		this.eventETime = eventETime;
	}

	/**
	 * @return the eventETimeHrs
	 */
	public String getEventETimeHrs() {
		return eventETimeHrs;
	}

	/**
	 * @param eventETimeHrs the eventETimeHrs to set
	 */
	public void setEventETimeHrs(String eventETimeHrs) {
		this.eventETimeHrs = eventETimeHrs;
	}

	/**
	 * @return the eventETimeMins
	 */
	public String getEventETimeMins() {
		return eventETimeMins;
	}

	/**
	 * @param eventETimeMins the eventETimeMins to set
	 */
	public void setEventETimeMins(String eventETimeMins) {
		this.eventETimeMins = eventETimeMins;
	}

	/**
	 * @return the hiddenDept
	 */
	public String getHiddenDept() {
		return hiddenDept;
	}

	/**
	 * @param hiddenDept the hiddenDept to set
	 */
	public void setHiddenDept(String hiddenDept) {
		this.hiddenDept = hiddenDept;
	}

	/**
	 * @return the departmentId
	 */
	public Integer getDepartmentId() {
		return departmentId;
	}

	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * @return the departmentName
	 */
	public Integer getDepartmentName() {
		return departmentName;
	}

	/**
	 * @param departmentName the departmentName to set
	 */
	public void setDepartmentName(Integer departmentName) {
		this.departmentName = departmentName;
	}

	/**
	 * @return the isEventAppsite
	 */
	public String getIsEventAppsite() {
		return isEventAppsite;
	}

	/**
	 * @param isEventAppsite the isEventAppsite to set
	 */
	public void setIsEventAppsite(String isEventAppsite) {
		this.isEventAppsite = isEventAppsite;
	}

	/**
	 * @return the organizationHosting
	 */
	public String getOrganizationHosting() {
		return organizationHosting;
	}

	/**
	 * @param organizationHosting the organizationHosting to set
	 */
	public void setOrganizationHosting(String organizationHosting) {
		this.organizationHosting = organizationHosting;
	}

	/**
	 * @return the isEventTied
	 */
	public String getIsEventTied() {
		return isEventTied;
	}

	/**
	 * @param isEventTied the isEventTied to set
	 */
	public void setIsEventTied(String isEventTied) {
		this.isEventTied = isEventTied;
	}

	/**
	 * @return the eventTiedIds
	 */
	public String getEventTiedIds() {
		return eventTiedIds;
	}

	/**
	 * @param eventTiedIds the eventTiedIds to set
	 */
	public void setEventTiedIds(String eventTiedIds) {
		this.eventTiedIds = eventTiedIds;
	}

	/**
	 * @return the fundraisingGoal
	 */
	public String getFundraisingGoal() {
		return fundraisingGoal;
	}

	/**
	 * @param fundraisingGoal the fundraisingGoal to set
	 */
	public void setFundraisingGoal(String fundraisingGoal) {
		this.fundraisingGoal = fundraisingGoal;
	}

	/**
	 * @return the currentLevel
	 */
	public String getCurrentLevel() {
		return currentLevel;
	}

	/**
	 * @param currentLevel the currentLevel to set
	 */
	public void setCurrentLevel(String currentLevel) {
		this.currentLevel = currentLevel;
	}

	/**
	 * @return the purchaseProducts
	 */
	public String getPurchaseProducts() {
		return purchaseProducts;
	}

	/**
	 * @param purchaseProducts the purchaseProducts to set
	 */
	public void setPurchaseProducts(String purchaseProducts) {
		this.purchaseProducts = purchaseProducts;
	}

	/**
	 * @return the screenName
	 */
	public String getScreenName() {
		return screenName;
	}

	/**
	 * @param screenName the screenName to set
	 */
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}


	public Integer getEvtMarkerId() {
		return evtMarkerId;
	}

	public void setEvtMarkerId(Integer evtMarkerId) {
		this.evtMarkerId = evtMarkerId;
	}

	public String getEvtMarkerName() {
		return evtMarkerName;
	}

	public void setEvtMarkerName(String evtMarkerName) {
		this.evtMarkerName = evtMarkerName;
	}

	public String getEvtMarkerImgPath() {
		return evtMarkerImgPath;
	}

	public void setEvtMarkerImgPath(String evtMarkerImgPath) {
		this.evtMarkerImgPath = evtMarkerImgPath;
	}


	public String getIsEventLogistics() {
		return isEventLogistics;
	}

	public void setIsEventLogistics(String isEventLogistics) {
		this.isEventLogistics = isEventLogistics;
	}

	public Integer getButtonId() {
		return buttonId;
	}

	public void setButtonId(Integer buttonId) {
		this.buttonId = buttonId;
	}

	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	public String getButtonLink() {
		return buttonLink;
	}

	public void setButtonLink(String buttonLink) {
		this.buttonLink = buttonLink;
	}

	public String getLogisticsImgName() {
		return logisticsImgName;
	}

	public void setLogisticsImgName(String logisticsImgName) {
		this.logisticsImgName = logisticsImgName;
	}

	public CommonsMultipartFile getLogisticsImgFile() {
		return logisticsImgFile;
	}

	public void setLogisticsImgFile(CommonsMultipartFile logisticsImgFile) {
		this.logisticsImgFile = logisticsImgFile;
	}

	public String getImgUploadFor() {
		return imgUploadFor;
	}

	public void setImgUploadFor(String imgUploadFor) {
		this.imgUploadFor = imgUploadFor;
	}

	public String getLogisticsImgPath() {
		return logisticsImgPath;
	}

	public void setLogisticsImgPath(String logisticsImgPath) {
		this.logisticsImgPath = logisticsImgPath;
	}

	public List<Event> getLogisticsBtnList() {
		return logisticsBtnList;
	}

	public void setLogisticsBtnList(List<Event> logisticsBtnList) {
		this.logisticsBtnList = logisticsBtnList;
	}

	/*public Boolean isShowLogisticsTab() {
		return showLogisticsTab;
	}

	public void setShowLogisticsTab(boolean showLogisticsTab) {
		this.showLogisticsTab = showLogisticsTab;
	}*/

	public String getIsSignatureEvent() {
		return isSignatureEvent;
	}

	public void setIsSignatureEvent(String isSignatureEvent) {
		this.isSignatureEvent = isSignatureEvent;
	}

	/**
	 * @return the isNewLogisticsImg
	 */
	public Boolean getIsNewLogisticsImg() {
		return isNewLogisticsImg;
	}

	/**
	 * @param isNewLogisticsImg the isNewLogisticsImg to set
	 */
	public void setIsNewLogisticsImg(Boolean isNewLogisticsImg) {
		this.isNewLogisticsImg = isNewLogisticsImg;
	}

	/**
	 * @return the isEventOverlay
	 */
	public String getIsEventOverlay() {
		return isEventOverlay;
	}

	/**
	 * @param isEventOverlay the isEventOverlay to set
	 */
	public void setIsEventOverlay(String isEventOverlay) {
		this.isEventOverlay = isEventOverlay;
	}

	/**
	 * @return the showLogisticsTab
	 */
	public Boolean getShowLogisticsTab() {
		return showLogisticsTab;
	}

	/**
	 * @param showLogisticsTab the showLogisticsTab to set
	 */
	public void setShowLogisticsTab(Boolean showLogisticsTab) {
		this.showLogisticsTab = showLogisticsTab;
	}


	public CommonsMultipartFile getEvtMarkerImgFile() {
		return evtMarkerImgFile;
	}

	public void setEvtMarkerImgFile(CommonsMultipartFile evtMarkerImgFile) {
		this.evtMarkerImgFile = evtMarkerImgFile;
	}

	public String getMarkerBtnName() {
		return markerBtnName;
	}

	public void setMarkerBtnName(String markerBtnName) {
		this.markerBtnName = markerBtnName;
	}



	/**
	 * @return the btnNames
	 */
	public String getBtnNames() {
		return btnNames;
	}

	/**
	 * @param btnNames the btnNames to set
	 */
	public void setBtnNames(String btnNames) {
		this.btnNames = btnNames;
	}

	/**
	 * @return the btnLinks
	 */
	public String getBtnLinks() {
		return btnLinks;
	}

	/**
	 * @param btnLinks the btnLinks to set
	 */
	public void setBtnLinks(String btnLinks) {
		this.btnLinks = btnLinks;
	}

	public String getLocationTitle() {
		return locationTitle;
	}

	public void setLocationTitle(String locationTitle) {
		this.locationTitle = locationTitle;
	}

	
}