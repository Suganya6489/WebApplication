package com.hubciti.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.pojo.ScreenSettings;

public class TwoColTabValidator implements Validator {

	public boolean supports(Class<?> target) {
		// TODO Auto-generated method stub
		return ScreenSettings.class.isAssignableFrom(target);
	}

	public void validate(Object target, Errors errors, int val) {
		ScreenSettings screenSettings = (ScreenSettings) target;

		if (val == 2) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "logoImageName", "logoImageName.required");
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "menuBtnName", "menuBtnName.required");

		// ValidationUtils.rejectIfEmptyOrWhitespace(errors, "btnView",
		// "btnView.required");

		if ("0".equals(screenSettings.getMenuFucntionality())) {
			errors.rejectValue("menuFucntionality", "menuFucntionality.required");

		} else {
			if (null != screenSettings.getFunctionalityType() && !"".equals(screenSettings.getFunctionalityType())
					&& (null == screenSettings.getBtnLinkId()) || "null".equalsIgnoreCase(screenSettings.getBtnLinkId())) {
				if (screenSettings.getFunctionalityType().equals(ApplicationConstants.SUBMENU)) {
					errors.rejectValue("menuFucntionality", "required.submenu");
				}
				if (screenSettings.getFunctionalityType().equals(ApplicationConstants.APPSITE)) {
					errors.rejectValue("menuFucntionality", "required.appsite");
				}
				if (screenSettings.getFunctionalityType().equals(ApplicationConstants.ANYTHINGPAGE)) {
					errors.rejectValue("menuFucntionality", "required.anythingpage");
				}
				if (screenSettings.getFunctionalityType().equals(ApplicationConstants.FIND)) {
					errors.rejectValue("menuFucntionality", "required.category");					
				}
				if (screenSettings.getFunctionalityType().equals(ApplicationConstants.SETUPEVENTS)) {
					errors.rejectValue("menuFucntionality", "required.category");
					
				}if(screenSettings.getFunctionalityType().equals(ApplicationConstants.FUNDRAISERS)){
					errors.rejectValue("menuFucntionality", "required.category");
					
				}				
			}  else if ("RegionApp".equalsIgnoreCase(screenSettings.getUserType()) && (null == screenSettings.getCitiId()  || "".equalsIgnoreCase(screenSettings.getCitiId()))) {
				errors.rejectValue("menuFucntionality", "citi.select");	
			}
		}
		if (null == screenSettings.getMenuLevel()) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "subMenuName", "subMenuName.required");
		}

		if (null != screenSettings.getMenuFilterType() && screenSettings.getMenuFilterType().length >= 1) {
			if ("Department".equals(screenSettings.getMenuFilterType()[0])) {
				if ("0".equals(screenSettings.getBtnDept())) {
					errors.rejectValue("btnDept", "dept.required");
				}
			} else
				if ("Type".equals(screenSettings.getMenuFilterType()[0]) && "0".equals(screenSettings.getBtnType())) {
					errors.rejectValue("btnType", "type.required");
				}

			if (screenSettings.getMenuFilterType().length == 2) {
				if ("Department".equals(screenSettings.getMenuFilterType()[1])) {
					if ("0".equals(screenSettings.getBtnDept())) {
						errors.rejectValue("btnDept", "dept.required");
					}
				} else
					if ("Type".equals(screenSettings.getMenuFilterType()[1]) && "0".equals(screenSettings.getBtnType())) {
						errors.rejectValue("btnType", "type.required");
					}
			}

		}
	}

	public final void validate(Object arg0, Errors errors, String status) {
		if (ApplicationConstants.DUPLICATEBUTTONNAME.equals(status)) {
			errors.reject("duplicate.button");

		}

		if (ApplicationConstants.DUPLICATEFUNCTINALITY.equals(status)) {
			errors.reject("duplicate.functionality");

		}

		if (ApplicationConstants.DUPLICATESUBMENU.equals(status)) {
			errors.reject("duplicate.submenu");

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

	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub

	}

}
