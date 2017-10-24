package com.hubciti.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.pojo.ScreenSettings;

public class ButtomBarButtonValidator implements Validator {

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

		ScreenSettings screenSettings = (ScreenSettings) target;

		if ("upldOwn".equals(screenSettings.getIconSelection())) {
			if ("Default".equalsIgnoreCase(screenSettings.getPageType())) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "logoImageName", "logoImageName.required");
			} else {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "logoImageName", "logoImageName.required");
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bannerImageName", "logoImageName.required");
			}
		} else
			if ("exstngIcon".equals(screenSettings.getIconSelection())) {

				if (screenSettings.getIconId() == null) {
					errors.reject("required.icon");
				}
			}

		if ("0".equals(screenSettings.getMenuFucntionality())) {
			errors.rejectValue("menuFucntionality", "menuFucntionality.required");

		} else {

			if (null != screenSettings.getFunctionalityType() && !"".equals(screenSettings.getFunctionalityType())) {

				
				if (null == screenSettings.getBtnLinkId()) {
					if (screenSettings.getFunctionalityType().equalsIgnoreCase(ApplicationConstants.SUBMENU)) {
						errors.rejectValue("menuFucntionality", "required.submenu");

					}

					if (screenSettings.getFunctionalityType().equalsIgnoreCase(ApplicationConstants.APPSITE)) {
						errors.rejectValue("menuFucntionality", "required.appsite");

					}
					if (screenSettings.getFunctionalityType().equalsIgnoreCase(ApplicationConstants.ANYTHINGPAGE)) {
						errors.rejectValue("menuFucntionality", "required.anythingpage");

					}
					if (screenSettings.getFunctionalityType().equalsIgnoreCase(ApplicationConstants.FIND)) {
						errors.rejectValue("menuFucntionality", "required.category");

					}
					if (screenSettings.getFunctionalityType().equals("Events")) {
						errors.rejectValue("menuFucntionality", "required.category");

					}					
					if (screenSettings.getFunctionalityType().equals("Fundraisers")) {
						errors.rejectValue("menuFucntionality", "required.category");

					}
				}

			}
		}

		if (screenSettings.getFunctionalityType() != null) {
			if ("Share".equals(screenSettings.getFunctionalityType())) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "iTunesLnk", "iTunes.required");
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "playStoreLnk", "playStoreLnk.required");
			}

		}

	}

	public final void validate(Object arg0, Errors errors, String status) {
		if (ApplicationConstants.DUPLICATEFUNCTINALITY.equals(status)) {
			errors.reject("duplicate.functionality");
		}

		if (ApplicationConstants.DUPLICATESUBMENU.equals(status)) {
			errors.reject("duplicate.submenu");
		}

		if (ApplicationConstants.ISASSOCIATED.equals(status)) {
			errors.reject("btn.associated");
		}

		if (ApplicationConstants.DUPLICATEANYTHINGPAGE.equals(status)) {
			errors.reject("duplicate.anythingpage");
		}

		if (ApplicationConstants.DUPLICATEAPPSITE.equals(status)) {
			errors.reject("duplicate.appsite");
		}
		if (ApplicationConstants.DUPLICATECATEGORY.equals(status)) {
			errors.reject("duplicate.category");
		}

		if (ApplicationConstants.DUPLICATECATEGORIES.equals(status)) {
			errors.reject("duplicate.categories");
		}
	}

	public final void validate(Object arg0, Errors errors, String status, String appType) {
		if (null != status) {
			if (null != appType) {
				if ("iPhone".equals(appType)) {
					if (status.equals(ApplicationConstants.INVALIDURL)) {
						errors.rejectValue("iTunesLnk", "invalid.url");
					}
				} else {
					if (status.equals(ApplicationConstants.INVALIDURL)) {
						errors.rejectValue("playStoreLnk", "invalid.url");
					}
				}

			}

		}

	}

}