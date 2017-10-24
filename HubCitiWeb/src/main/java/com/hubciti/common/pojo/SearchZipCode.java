package com.hubciti.common.pojo;

public class SearchZipCode {
	/*
	 * 
	 */
	String city;
	/*
	 * 
	 */
	String stateName;
	/*
	 * 
	 */
	String stateCode;
	/*
	 * 
	 */
	String zipCode;

	/**
	 * @return
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return
	 */
	public String getStateName() {
		return stateName;
	}

	/**
	 * @param state
	 */
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	/**
	 * @return
	 */
	public String getStateCode() {
		return stateCode;
	}

	/**
	 * @param stateCode
	 */
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	/**
	 * @return
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * @param zipCode
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

}
