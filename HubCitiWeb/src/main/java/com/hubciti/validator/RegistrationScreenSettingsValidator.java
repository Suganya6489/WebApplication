package com.hubciti.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.hubciti.common.pojo.ScreenSettings;

/**
 * This class is a validator class for about us screen.
 * 
 * @author dileep_cc
 */
public class RegistrationScreenSettingsValidator implements Validator {
	/**
	 * this method returns true or false.
	 */
	public boolean supports(Class<?> target) {
		return ScreenSettings.class.isAssignableFrom(target);
	}

	/**
	 * this method validates mandatory fields.
	 */
	public final void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pageTitle", "pageTitle.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pageContent", "pageContent.required");

	}

	/**
	 * this method validates mandatory fields.
	 */
	public final void validateUserSeetings(Object target, Errors errors) {
		final ScreenSettings userSettings = (ScreenSettings) target;

		if (userSettings.getUserSettingsFields().contains("optn-dntn")) {
			ValidationUtils.rejectIfEmpty(errors, "logoImageName", "logoImageName.required");
		}
	}
}
