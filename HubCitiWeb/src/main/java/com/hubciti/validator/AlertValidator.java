/**
 * 
 */
package com.hubciti.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.pojo.Alerts;

/**
 * @author sangeetha.ts
 */
public class AlertValidator implements Validator {
	/**
	 * this method returns true or false
	 */
	public boolean supports(Class<?> target) {
		return Alerts.class.isAssignableFrom(target);
	}

	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "pageTitle.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "description.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "categoryId", "category.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "severityId", "severity.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startDate", "startDate.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endDate", "endDate.required");
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
		if (status.equals(ApplicationConstants.VALIDSTARTDATE)) {
			errors.rejectValue("startDate", "validstartdate");
		}
		if (status.equals(ApplicationConstants.DATESTARTCURRENT)) {
			errors.rejectValue("startDate", "datestartcurrent");
		}
		if (status.equals(ApplicationConstants.VALIDENDDATE)) {
			errors.rejectValue("endDate", "validenddate");
		}
		if (status.equals(ApplicationConstants.DATEENDCURRENT)) {
			errors.rejectValue("endDate", "dateendcurrent");
		}
		if (status.equals(ApplicationConstants.DATEAFTER)) {
			errors.rejectValue("endDate", "datebefore");
		}
	}

}
