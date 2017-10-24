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
public class SplashScreenSettingsValidator implements Validator {
	/**
	 * this method returns true or false
	 */
	public boolean supports(Class<?> target) {
		return ScreenSettings.class.isAssignableFrom(target);
	}

	/**
	 * this method validates mandatory fields
	 */
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "logoImageName", "logoImageName.required");
	}

	/**
	 * this method validates mandatory fields
	 */
	public void validateGeneralSettings(Object target, Errors errors) {
		ScreenSettings settings = (ScreenSettings) target;

		if ("TabBarLogo".equalsIgnoreCase(settings.getPageType())) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "logoImageName", "logoImageName.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bottomBtnId", "bottomBtnId.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bannerImageName", "logoImageName.required");
		} else {
			if (null != settings.getIconSelection() && !"".equalsIgnoreCase(settings.getIconSelection())) {
				if ("image".equalsIgnoreCase(settings.getIconSelection())) {
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "logoImageName", "logoPath.required");
				}
			} else {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "iconSelection", "background.required");
			}
		}

	}

}
