/**
 * 
 */
package com.hubciti.common.pojo;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * @author sangeetha.ts
 */
public class Filters {
	/**
	 * 
	 */
	private Integer filterID;
	/**
	 * 
	 */
	private Integer cityExperienceID;
	/**
	 * 
	 */
	private String filterName;
	/**
	 * 
	 */
	private String searchFilterName;
	/**
	 * 
	 */
	private String logoImageName;
	/**
	 * 
	 */
	private CommonsMultipartFile logoImage;
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
	private String viewName;
	/**
	 * 
	 */
	private String retailerLocIds;
	/**
	 * 
	 */
	private String hiddenRetailLocs;
	/**
	 * 
	 */
	private Boolean nextPage;

	/**
	 * @return the filterID
	 */
	public Integer getFilterID() {
		return filterID;
	}

	/**
	 * @param filterID
	 *            the filterID to set
	 */
	public void setFilterID(Integer filterID) {
		this.filterID = filterID;
	}

	/**
	 * @return the filterName
	 */
	public String getFilterName() {
		return filterName;
	}

	/**
	 * @param filterName
	 *            the filterName to set
	 */
	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	/**
	 * @return the filterImage
	 */
	public CommonsMultipartFile getLogoImage() {
		return logoImage;
	}

	/**
	 * @param filterImage
	 *            the filterImage to set
	 */
	public void setLogoImage(CommonsMultipartFile logoImage) {
		this.logoImage = logoImage;
	}

	/**
	 * @return the searchKey
	 */
	public String getSearchKey() {
		return searchKey;
	}

	/**
	 * @param searchKey
	 *            the searchKey to set
	 */
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	/**
	 * @return the lowerLimit
	 */
	public Integer getLowerLimit() {
		return lowerLimit;
	}

	/**
	 * @param lowerLimit
	 *            the lowerLimit to set
	 */
	public void setLowerLimit(Integer lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	/**
	 * @param cityExperienceID
	 *            the cityExperienceID to set
	 */
	public void setCityExperienceID(Integer cityExperienceID) {
		this.cityExperienceID = cityExperienceID;
	}

	/**
	 * @return the cityExperienceID
	 */
	public Integer getCityExperienceID() {
		return cityExperienceID;
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

	/**
	 * @param logoImageName
	 *            the logoImageName to set
	 */
	public void setLogoImageName(String logoImageName) {
		this.logoImageName = logoImageName;
	}

	/**
	 * @return the logoImageName
	 */
	public String getLogoImageName() {
		return logoImageName;
	}

	/**
	 * @param retailerLocIds
	 *            the retailerLocIds to set
	 */
	public void setRetailerLocIds(String retailerLocIds) {
		this.retailerLocIds = retailerLocIds;
	}

	/**
	 * @return the retailerLocIds
	 */
	public String getRetailerLocIds() {
		return retailerLocIds;
	}

	/**
	 * @param hiddenRetailLocs
	 *            the hiddenRetailLocs to set
	 */
	public void setHiddenRetailLocs(String hiddenRetailLocs) {
		this.hiddenRetailLocs = hiddenRetailLocs;
	}

	/**
	 * @return the hiddenRetailLocs
	 */
	public String getHiddenRetailLocs() {
		return hiddenRetailLocs;
	}

	/**
	 * @param searchFilterName
	 *            the searchFilterName to set
	 */
	public void setSearchFilterName(String searchFilterName) {
		this.searchFilterName = searchFilterName;
	}

	/**
	 * @return the searchFilterName
	 */
	public String getSearchFilterName() {
		return searchFilterName;
	}

	/**
	 * @param nextPage
	 *            the nextPage to set
	 */
	public void setNextPage(Boolean nextPage) {
		this.nextPage = nextPage;
	}

	/**
	 * @return the nextPage
	 */
	public Boolean getNextPage() {
		return nextPage;
	}
}
