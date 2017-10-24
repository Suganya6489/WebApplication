/**
 * 
 */
package com.hubciti.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.pojo.Alerts;
import com.hubciti.common.pojo.Filters;
import com.hubciti.common.util.Utility;

/**
 * @author sangeetha.ts
 */
public class FilterValidation implements Validator {
	/**
	 * this method returns true or false
	 */
	public boolean supports(Class<?> target) {
		return Alerts.class.isAssignableFrom(target);
	}

	public void validate(Object target, Errors errors) {
		Filters filters = (Filters) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "filterName", "filterName.required");
		if ((null == filters.getRetailerLocIds() || "".equalsIgnoreCase(filters.getRetailerLocIds())) && filters.getNextPage() != true) {
			errors.rejectValue("retailerLocIds", "locationAssociation");
		}
		if ("".equals(Utility.checkNull(filters.getLogoImageName()))) {
			errors.rejectValue("logoImageName", "imageFile");
		}

	}

	public final void validate(Object arg0, Errors errors, String status) {
		if (ApplicationConstants.DUPLICATEFILTER.equals(status)) {
			errors.reject("duplicate.filter");

		}
	}

}
