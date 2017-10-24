package com.hubciti.common.pojo;

public class RetailLocation {

	private Integer retailID;
	private String retailName;
	private Integer retailLocationID;
	private String address1;
	private String city;
	private String state;
	private String postalCode;

	private String hotelPrice;
	private String discountCode;
	private String discountAmount;
	private String rating;
	private String roomAvailabilityCheckURL;
	private String roomBookingURL;

	public Integer getRetailID() {
		return retailID;
	}

	public void setRetailID(Integer retailID) {
		this.retailID = retailID;
	}

	public Integer getRetailLocationID() {
		return retailLocationID;
	}

	public void setRetailLocationID(Integer retailLocationID) {
		this.retailLocationID = retailLocationID;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
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

	public String getRetailName() {
		return retailName;
	}

	public void setRetailName(String retailName) {
		this.retailName = retailName;
	}

	public String getHotelPrice() {
		return hotelPrice;
	}

	public void setHotelPrice(String hotelPrice) {
		if ("".equals(hotelPrice)) {

			this.hotelPrice = null;
		} else {

			this.hotelPrice = hotelPrice;
		}

	}

	public String getDiscountCode() {

		return discountCode;
	}

	public void setDiscountCode(String discountCode) {

		if ("".equals(discountCode)) {

			this.discountCode = null;
		} else {
			this.discountCode = discountCode;
		}

	}

	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		if ("".equals(discountAmount)) {

			this.discountAmount = null;
		} else {
			this.discountAmount = discountAmount;
		}

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
		if ("".equals(roomAvailabilityCheckURL)) {
			this.roomAvailabilityCheckURL = null;

		} else {
			this.roomAvailabilityCheckURL = roomAvailabilityCheckURL;
		}

	}

	public String getRoomBookingURL() {
		return roomBookingURL;
	}

	public void setRoomBookingURL(String roomBookingURL) {
		if ("".equals(roomBookingURL)) {
			this.roomBookingURL = null;

		} else {
			this.roomBookingURL = roomBookingURL;
		}

	}

}
