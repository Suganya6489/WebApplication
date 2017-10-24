package com.hubciti.common.pojo;

public class AppSiteDetails {

	private String appSiteId;
	private String appSiteName;
	private int retailId;
	private String retName;
	private String address;
	private String city;
	private String state;
	private String postalCode;
	private int retLocId;
	private int hubCityId;
	private String compAddress;
	private String searchKey;
	private int hcUserId;
	private Integer totalRecordSize;

	public String getAppSiteId() {
		return appSiteId;
	}

	public void setAppSiteId(String appSiteId) {
		this.appSiteId = appSiteId;
	}

	public String getAppSiteName() {
		return appSiteName;
	}

	public void setAppSiteName(String appSiteName) {
		this.appSiteName = appSiteName;
	}

	public int getRetailId() {
		return retailId;
	}

	public void setRetailId(int retailId) {
		this.retailId = retailId;
	}

	public String getRetName() {
		return retName;
	}

	public void setRetName(String retName) {
		this.retName = retName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		/*
		 * if (address != null) { compAddress = address + ","; } if (this.city
		 * != null) { compAddress = compAddress + city + ","; } if (postalCode
		 * != null) { compAddress = compAddress + postalCode + ","; } if (state
		 * != null) { compAddress = compAddress + state; }
		 */

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

	public int getRetLocId() {
		return retLocId;
	}

	public void setRetLocId(int retLocId) {
		this.retLocId = retLocId;
	}

	public int getHubCityId() {
		return hubCityId;
	}

	public void setHubCityId(int hubCityId) {
		this.hubCityId = hubCityId;
	}

	public String getCompAddress() {
		return compAddress;
	}

	public void setCompAddress(String compAddress) {
		this.compAddress = compAddress;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public int getHcUserId() {
		return hcUserId;
	}

	public void setHcUserId(int hcUserId) {
		this.hcUserId = hcUserId;

	}

	public Integer getTotalRecordSize() {
		return totalRecordSize;
	}

	public void setTotalRecordSize(Integer totalRecordSize) {
		this.totalRecordSize = totalRecordSize;
	}

}
