package com.hubciti.common.pojo;

public class GAddress {
	public String city;
	public String state;
	public String zipCode;
	public String country;
	public String countryCode;

	public double lat = 0.0;
	public double lng = 0.0;

	private String addrReturned;

	private String locationType;

	private String status;

	/**
	 * 
	 */
	public GAddress() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param fullAddress
	 * @param address
	 * @param accuracy
	 * @param city
	 * @param state
	 * @param zipCode
	 * @param county
	 * @param country
	 * @param countryCode
	 * @param lat
	 * @param lng
	 */
	public GAddress(String fullAddress, String address, int accuracy, String city, String state, String zipCode, String county, String country,
			String countryCode, double lat, double lng) {
		super();
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.country = country;
		this.countryCode = countryCode;
		this.lat = lat;
		this.lng = lng;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * @param zipCode
	 *            the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * @param countryCode
	 *            the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * @param lat
	 *            the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * @return the lng
	 */
	public double getLng() {
		return lng;
	}

	/**
	 * @param lng
	 *            the lng to set
	 */
	public void setLng(double lng) {
		this.lng = lng;
	}

	/**
	 * @return the addrReturned
	 */
	public String getAddrReturned() {
		return addrReturned;
	}

	/**
	 * @param addrReturned
	 *            the addrReturned to set
	 */
	public void setAddrReturned(String addrReturned) {
		this.addrReturned = addrReturned;
	}

	/**
	 * @return the locationType
	 */
	public String getLocationType() {
		return locationType;
	}

	/**
	 * @param locationType
	 *            the locationType to set
	 */
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @param fullAddress
	 * @param address
	 * @param accuracy
	 * @param city
	 * @param state
	 * @param zipCode
	 * @param county
	 * @param country
	 * @param countryCode
	 * @param lat
	 * @param lng
	 */

	public String toString() {
		return "GAddress" + " [city=" + city + ", state=" + state + ", zipCode=" + zipCode + ", country=" + country + ", countryCode=" + countryCode
				+ ", lat=" + lat + ", lng=" + lng + "]";
	}

}
