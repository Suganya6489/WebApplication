package com.hubciti.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.pojo.Event;

public class EventValidator implements Validator {

	public boolean supports(Class<?> target) {
		return Event.class.isAssignableFrom(target);
	}

	public void validate(Object target, Errors errors) {
		Event eventDetails = (Event) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "hcEventName", "eventname.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eventImageName", "logoImageName.required");

		if (null == eventDetails.getEventCategory() || "".equals(eventDetails.getEventCategory())) {
			errors.rejectValue("eventCategory", "eventCategory.required");
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shortDescription", "shortDescription.required");

		if (null != eventDetails.getIsOngoing() && "yes".equals(eventDetails.getIsOngoing())) {
			if (null != eventDetails.getRecurrencePatternName()) {
				if ("Daily".equalsIgnoreCase(eventDetails.getRecurrencePatternName()) && "days".equalsIgnoreCase(eventDetails.getIsOngoingDaily())) {
					if (null == eventDetails.getEveryWeekDay() || "".equals(eventDetails.getEveryWeekDay())) {
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "everyWeekDay", "everyWeekDay.required");
					} else
						if (eventDetails.getEveryWeekDay() <= 0) {
							errors.rejectValue("everyWeekDay", "everyWeekDay.greaterZero");
						}
				} else
					if ("Weekly".equalsIgnoreCase(eventDetails.getRecurrencePatternName())) {
						if (null == eventDetails.getEveryWeek() || "".equals(eventDetails.getEveryWeek())) {
							ValidationUtils.rejectIfEmptyOrWhitespace(errors, "everyWeek", "everyWeek.required");
						} else
							if (eventDetails.getEveryWeek() <= 0) {
								errors.rejectValue("everyWeek", "everyWeek.greaterZero");
							} else
								if (eventDetails.getDays().length <= 0) {
									errors.rejectValue("days", "days.required");
								}
					} else
						if ("Monthly".equalsIgnoreCase(eventDetails.getRecurrencePatternName())) {
							if ("date".equalsIgnoreCase(eventDetails.getIsOngoingMonthly())) {
								if (null == eventDetails.getDateOfMonth() || "".equals(eventDetails.getDateOfMonth())) {
									ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateOfMonth", "dateOfMonth.required");
								} else
									if (eventDetails.getDateOfMonth() <= 0 || eventDetails.getDateOfMonth() > 31) {
										errors.rejectValue("dateOfMonth", "dayOfMonth.greaterZero");
									} else {
										if (null == eventDetails.getEveryMonth() || "".equals(eventDetails.getEveryMonth())) {
											ValidationUtils.rejectIfEmptyOrWhitespace(errors, "everyMonth", "everyMonth.required");
										} else
											if (eventDetails.getEveryMonth() <= 0) {
												errors.rejectValue("everyMonth", "everyMonth.greaterZero");
											}
									}
							} else {
								if (null == eventDetails.getEveryDayMonth() || "".equals(eventDetails.getEveryDayMonth())) {
									ValidationUtils.rejectIfEmptyOrWhitespace(errors, "everyDayMonth", "everyMonth.required");
								} else
									if (eventDetails.getEveryDayMonth() <= 0) {
										errors.rejectValue("everyDayMonth", "everyMonth.greaterZero");
									}
							}
						}

				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eventStartDate", "startDate.required");
				if ("endBy".equalsIgnoreCase(eventDetails.getOccurenceType())) {
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eventEndDate", "endDate.required");
				} else
					if ("endAfter".equalsIgnoreCase(eventDetails.getOccurenceType())) {
						if (null == eventDetails.getEndAfter() || "".equals(eventDetails.getEndAfter())) {
							ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endAfter", "endAfter.required");
						} else
							if (eventDetails.getEndAfter() <= 0) {
								errors.rejectValue("endAfter", "occurence.greaterZero");
							}

					}
			}
		} else {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eventDate", "startDate.required");
		}
		if (null != eventDetails.getEvntPckg() && "yes".equals(eventDetails.getEvntPckg())) {

			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "packagePrice", "packagePrice.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "packageTicketURL", "packageTicketURL.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "packageDescription", "packageDescription.required");
		}

		if (null != eventDetails.getEvntHotel() && "yes".equals(eventDetails.getEvntHotel())) {

			if (null == eventDetails.getRetailLocationID() || eventDetails.getRetailLocationID().length == 0) {

				errors.rejectValue("retailLocationID", "hotel.required");

			}

		}

		if (null != eventDetails.getBsnsLoc() && "yes".equals(eventDetails.getBsnsLoc())) {

			if (null == eventDetails.getAppsiteID() || eventDetails.getAppsiteID().length == 0) {
				errors.rejectValue("appsiteID", "appsite.required");

			}

		} else
			if (null != eventDetails.getBsnsLoc() && "no".equals(eventDetails.getBsnsLoc())) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "address.required");
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "city", "city.required");
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "state", "state.required");
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "postalCode", "postalCode.required");

			}
		
		/*if(null != eventDetails.getIsEventLogistics() && "yes".equals(eventDetails.getIsEventLogistics()))	{
			if(null == eventDetails.getLogisticsImgName() || "".equals(eventDetails.getLogisticsImgName())) 	{
				errors.rejectValue("logisticsImgName", "logoImageName.required");
			}
			
			if(null == eventDetails.getButtonName() || "".equals(eventDetails.getButtonName()))	{
				errors.rejectValue("buttonName", "menuBtnName.required");
			}
			
			if(null == eventDetails.getButtonLink() || "".equals(eventDetails.getButtonLink()))	{
				errors.rejectValue("buttonLink", "webUrl.required");
			}
		}*/

	}

	/**
	 * This method validate retailer location screen.
	 * 
	 * @param arg0
	 *            instance of Object.
	 * @param errors
	 *            instance of Errors.
	 * @param status
	 *            as request parameter.
	 */
	public final void validate(Object arg0, Errors errors, String status) {

		if (status.equals(ApplicationConstants.INVALIDURL)) {
			errors.rejectValue("packageTicketURL", "invalid.retailurl");
		}

		if (status.equals(ApplicationConstants.GEOERROR)) {
			errors.rejectValue("address", "invalid.geomessge");
		}
		if (status.equals(ApplicationConstants.LATITUDE)) {
			errors.rejectValue("latitude", "latitude.required");
		}
		if (status.equals(ApplicationConstants.LONGITUDE)) {
			errors.rejectValue("logitude", "longitude.required");
		}
		if (status.equals(ApplicationConstants.INVALIDEVENTURL)) {
			errors.rejectValue("moreInfoURL", "invalid.retailurl");
		}
		if (status.equals(ApplicationConstants.INVALIDPRICE)) {
			errors.rejectValue("packagePrice", "validprice");
		}
	}

	public final void validateDates(Object arg0, Errors errors, String status) {
		if (status.equals(ApplicationConstants.VALIDSTARTDATE)) {
			errors.rejectValue("eventStartDate", "validstartdate");
		}
		if (status.equals(ApplicationConstants.DATESTARTCURRENT)) {
			errors.rejectValue("eventStartDate", "datestartcurrent");
		}
		if (status.equals(ApplicationConstants.VALIDENDDATE)) {
			errors.rejectValue("eventEndDate", "validenddate");
		}
		if (status.equals(ApplicationConstants.DATEENDCURRENT)) {
			errors.rejectValue("eventEndDate", "dateendcurrent");
		}
		if (status.equals(ApplicationConstants.DATEAFTER)) {
			errors.rejectValue("eventEndDate", "datebefore");
		}
		if (status.equals(ApplicationConstants.VALIDDATE)) {
			errors.rejectValue("eventDate", "validstartdate");
		}
		if (status.equals(ApplicationConstants.VALIDEDATE)) {
			errors.rejectValue("eventEDate", "validenddate");
		}
		if (status.equals(ApplicationConstants.DATENOGSTARTCURRENT)) {
			errors.rejectValue("eventDate", "datestartcurrent");
		}
		if (status.equals(ApplicationConstants.DATENOGENDCURRENT)) {
			errors.rejectValue("eventEDate", "dateendcurrent");
		}
		if (status.equals(ApplicationConstants.DATENOGAFTER)) {
			errors.rejectValue("eventEDate", "datebefore");
		}
	}
	
	public final void validate(Object obj, Errors errors, Integer iStatus)
	{
		Event event = (Event)obj;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "evtMarkerName", "evtMarkerName.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "latitude", "latitude.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "logitude", "longitude.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eventImageName", "markericon.required");
		
		
	}
	
	public final void validateLogistics(Object target, Errors errors)	{
		Event eventDetails = (Event) target;
		if(null == eventDetails.getLogisticsImgName() || "".equals(eventDetails.getLogisticsImgName())) 	{
			errors.rejectValue("logisticsImgName", "logoImageName.required");
		}
		
		if(null == eventDetails.getBtnNames() || "".equals(eventDetails.getBtnNames()) || null == eventDetails.getBtnLinks() || "".equals(eventDetails.getBtnLinks()) )	{
			errors.rejectValue("btnNames", "buttons.required");
		}
		
		/*if(null == eventDetails.getBtnLinks() || "".equals(eventDetails.getBtnLinks()))	{
			errors.rejectValue("btnLinks", "webUrl.required");
		}*/
	}
}
