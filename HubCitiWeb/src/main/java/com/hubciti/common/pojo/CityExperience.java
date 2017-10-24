package com.hubciti.common.pojo;

public class CityExperience {

	private Integer cityExpId;
	private String cityExpName;
	private String retSearchKey;
	private Integer retId;
	private Integer retLocId;
	private String retName;
	private String address;
	private String city;
	private String state;
	private String postalCode;
	private Integer lowerLimit;
	private String pageFlag;
	private String pageNumber;
	private String actionType;
	private Integer associate;
	private String unAssociRetLocId;
	private Boolean associateFlag;
	private Integer totChkCnt;
	private String stateName;
	private int hubCitiId;

	private String[] retLocIds;

	public Integer getCityExpId() {
		return cityExpId;
	}

	public void setCityExpId(Integer cityExpId) {
		this.cityExpId = cityExpId;
	}

	public String getRetSearchKey() {
		return retSearchKey;
	}

	public void setRetSearchKey(String retSearchKey) {
		this.retSearchKey = retSearchKey;
	}

	public Integer getRetId() {
		return retId;
	}

	public void setRetId(Integer retId) {
		this.retId = retId;
	}

	public Integer getRetLocId() {
		return retLocId;
	}

	public void setRetLocId(Integer retLocId) {
		this.retLocId = retLocId;
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

	public Integer getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(Integer lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public String getCityExpName() {
		return cityExpName;
	}

	public void setCityExpName(String cityExpName) {
		this.cityExpName = cityExpName;
	}

	public String[] getRetLocIds() {
		return retLocIds;
	}

	public void setRetLocIds(String[] retLocIds) {
		this.retLocIds = retLocIds;
	}

	public String getPageFlag() {
		return pageFlag;
	}

	public void setPageFlag(String pageFlag) {
		this.pageFlag = pageFlag;
	}

	public String getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Integer getAssociate() {
		return associate;
	}

	public void setAssociate(Integer associate) {
		this.associate = associate;
	}

	public String getUnAssociRetLocId() {
		return unAssociRetLocId;
	}

	public void setUnAssociRetLocId(String unAssociRetLocId) {
		this.unAssociRetLocId = unAssociRetLocId;
	}

	/**
	 * @param associateFlag
	 *            the associateFlag to set
	 */
	public void setAssociateFlag(Boolean associateFlag) {
		this.associateFlag = associateFlag;
	}

	/**
	 * @return the associateFlag
	 */
	public Boolean getAssociateFlag() {
		return associateFlag;
	}

	public Integer getTotChkCnt() {
		return totChkCnt;
	}

	public void setTotChkCnt(Integer totChkCnt) {
		this.totChkCnt = totChkCnt;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public int getHubCitiId() {
		return hubCitiId;
	}

	public void setHubCitiId(int hubCitiId) {
		this.hubCitiId = hubCitiId;
	}

}
