/**
 * 
 */
package com.hubciti.common.pojo;

/**
 * @author sangeetha.ts
 */
public class Alerts {
	/**
	 * 
	 */
	private Integer alertId;
	/**
	 * 
	 */
	private String title;
	/**
	 * 
	 */
	private String description;
	/**
	 * 
	 */
	private Integer severityId;
	/**
	 * 
	 */
	private String severity;
	/**
	 * 
	 */
	private Integer categoryId;
	/**
	 * 
	 */
	private String category;
	/**
	 * 
	 */
	private String startDate;
	/**
	 * 
	 */
	private String endDate;
	/**
	 * 
	 */
	private String startTime;
	/**
	 * 
	 */
	private String endTime;
	/**
	 * 
	 */
	private String searchKey;
	/**
	 * 
	 */
	private Integer lowerLimit;
	/**
	 * 
	 */
	private Boolean menuItemExist;
	/**
	 * 
	 */
	private String startTimeHrs;
	/**
	 * 
	 */
	private String startTimeMins;
	/**
	 * 
	 */
	private String endTimeHrs;
	/**
	 * 
	 */
	private String endTimeMins;
	/**
	 * 
	 */
	private String viewName;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the severityId
	 */
	public Integer getSeverityId() {
		return severityId;
	}

	/**
	 * @param severityId
	 *            the severityId to set
	 */
	public void setSeverityId(Integer severityId) {
		this.severityId = severityId;
	}

	/**
	 * @return the categoryId
	 */
	public Integer getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId
	 *            the categoryId to set
	 */
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @param searchKey
	 *            the searchKey to set
	 */
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	/**
	 * @return the searchKey
	 */
	public String getSearchKey() {
		return searchKey;
	}

	/**
	 * @param alertId
	 *            the alertId to set
	 */
	public void setAlertId(Integer alertId) {
		this.alertId = alertId;
	}

	/**
	 * @return the alertId
	 */
	public Integer getAlertId() {
		return alertId;
	}

	/**
	 * @param menuItemExist
	 *            the menuItemExist to set
	 */
	public void setMenuItemExist(Boolean menuItemExist) {
		this.menuItemExist = menuItemExist;
	}

	/**
	 * @return the menuItemExist
	 */
	public Boolean getMenuItemExist() {
		return menuItemExist;
	}

	/**
	 * @param lowerLimit
	 *            the lowerLimit to set
	 */
	public void setLowerLimit(Integer lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	/**
	 * @return the lowerLimit
	 */
	public Integer getLowerLimit() {
		return lowerLimit;
	}

	/**
	 * @return the startTimeHrs
	 */
	public String getStartTimeHrs() {
		return startTimeHrs;
	}

	/**
	 * @param startTimeHrs
	 *            the startTimeHrs to set
	 */
	public void setStartTimeHrs(String startTimeHrs) {
		this.startTimeHrs = startTimeHrs;
	}

	/**
	 * @return the startTimeMins
	 */
	public String getStartTimeMins() {
		return startTimeMins;
	}

	/**
	 * @param startTimeMins
	 *            the startTimeMins to set
	 */
	public void setStartTimeMins(String startTimeMins) {
		this.startTimeMins = startTimeMins;
	}

	/**
	 * @return the endTimeHrs
	 */
	public String getEndTimeHrs() {
		return endTimeHrs;
	}

	/**
	 * @param endTimeHrs
	 *            the endTimeHrs to set
	 */
	public void setEndTimeHrs(String endTimeHrs) {
		this.endTimeHrs = endTimeHrs;
	}

	/**
	 * @return the endTimeMins
	 */
	public String getEndTimeMins() {
		return endTimeMins;
	}

	/**
	 * @param endTimeMins
	 *            the endTimeMins to set
	 */
	public void setEndTimeMins(String endTimeMins) {
		this.endTimeMins = endTimeMins;
	}

	/**
	 * @param severity
	 *            the severity to set
	 */
	public void setSeverity(String severity) {
		this.severity = severity;
	}

	/**
	 * @return the severity
	 */
	public String getSeverity() {
		return severity;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param viewName
	 *            the viewName to set
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * @return the viewName
	 */
	public String getViewName() {
		return viewName;
	}
}
