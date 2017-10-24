/**
 * 
 */
package com.hubciti.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.pojo.Event;

/**
 * @author sangeetha.ts
 *
 */
public class FundraiserEventValidator implements Validator {

	public boolean supports(Class<?> target) {
		return Event.class.isAssignableFrom(target);
	}

	public void validate(Object target, Errors errors) {
		Event eventDetails = (Event) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "hcEventName", "eventname.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eventImageName", "logoImageName.required");
		
		if("No".equalsIgnoreCase(eventDetails.getIsEventAppsite()))
		{
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "organizationHosting", "organizationHosting.required");
		}
		else {			
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "appsiteIDs", "required.appsite");
		}
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shortDescription", "shortDescription.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "longDescription", "longDescription.required");

		if (null == eventDetails.getEventCategory() || "".equals(eventDetails.getEventCategory())) {
			errors.rejectValue("eventCategory", "eventCategory.required");
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eventDate", "startDate.required");
		
		if ("yes".equalsIgnoreCase(eventDetails.getIsEventTied())) {
			//if (null == eventDetails.getEventTiedIds() || "".equalsIgnoreCase(eventDetails.getEventTiedIds())) {
				//errors.rejectValue("eventTiedIds", "eventTiedIds.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eventTiedIds", "eventTiedIds.required");
			//}
		}
	}

	/**
	 * This method validate URL's.
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
			errors.rejectValue("purchaseProducts", "invalid.retailurl");
		}
		if (status.equals(ApplicationConstants.INVALIDEVENTURL)) {
			errors.rejectValue("moreInfoURL", "invalid.retailurl");
		}
	}
	
	/**
	 * This method validate Dates.
	 * 
	 * @param arg0
	 *            instance of Object.
	 * @param errors
	 *            instance of Errors.
	 * @param status
	 *            as request parameter.
	 */
	public final void validateDates(Object arg0, Errors errors, String status) {
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
	
	public final void validatePrice(Object arg0, Errors errors, String status, String property) {
		if (status.equals(ApplicationConstants.INVALIDPRICE)) {
			errors.rejectValue(property, "validprice");
		}
	}
}
