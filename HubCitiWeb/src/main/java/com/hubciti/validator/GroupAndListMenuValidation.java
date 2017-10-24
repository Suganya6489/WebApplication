/**
 * 
 */
package com.hubciti.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.pojo.ScreenSettings;

/**
 * @author sangeetha.ts
 */
public class GroupAndListMenuValidation implements Validator {

	public boolean supports(Class<?> clazz) {
		return ScreenSettings.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		ScreenSettings screenSettings = (ScreenSettings) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "menuBtnName", "menuBtnName.required");
		
		/*
		 * To add images in grouped tab template
		 */
		if(null != screenSettings.getIsGroupImg() && screenSettings.getIsGroupImg().equals("true"))	{
			if(screenSettings.getLogoImageName() == null || "".equals(screenSettings.getLogoImageName()))	{
				errors.rejectValue("logoImageName", "logoImageName.required");
			}
		}

		if ("setupcombomenu".equals(screenSettings.getViewName())) {
			if ("0".equalsIgnoreCase(screenSettings.getBtnGroup())) {
				if ("Text".equals(screenSettings.getPageTitle())) {
					errors.rejectValue("btnGroup", "btnGroup.required");
				} else {
					errors.rejectValue("btnGroup", "btnTitle.required");
				}
			}

			if (null == screenSettings.getComboBtnTypeId() || "0".equals(screenSettings.getComboBtnTypeId())) {
				errors.rejectValue("comboBtnType", "comboBtnType.required");
			} else {
				if (!"Rectangle".equals(screenSettings.getComboBtnType())) {
					if ("Circle".equals(screenSettings.getComboBtnType())) {
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "logoImageName", "logoImageName.required");
					} else {
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bannerImageName", "logoImageName.required");
					}
				}
			}
		}

		if ("0".equalsIgnoreCase(screenSettings.getMenuFucntionality())) {
			errors.rejectValue("menuFucntionality", "menuFucntionality.required");
		} else
			if ("0".equalsIgnoreCase(screenSettings.getBtnGroup()) && !"setupcombomenu".equals(screenSettings.getViewName())) {
				errors.rejectValue("btnGroup", "btnGroup.required");
			} else {
				if (null != screenSettings.getFunctionalityType() && !"".equals(screenSettings.getFunctionalityType())
						&& null == screenSettings.getBtnLinkId()) {
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

					
				} else if ("RegionApp".equalsIgnoreCase(screenSettings.getUserType()) && (null == screenSettings.getCitiId()  || "".equalsIgnoreCase(screenSettings.getCitiId()))) {
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

		ScreenSettings details = (ScreenSettings) arg0;
		if (ApplicationConstants.DUPLICATEBUTTONNAME.equals(status)) {
			errors.reject("duplicate.button");

		}

		if (ApplicationConstants.DUPLICATEFUNCTINALITY.equals(status)) {
			errors.reject("duplicate.functionality");

		}

		if (ApplicationConstants.DUPLICATESUBMENU.equals(status)) {
			errors.reject("duplicate.submenu");

		}

		if (ApplicationConstants.DUPLICATEGROUP.equals(status)) {
			errors.reject("duplicate.group");

		}

		if (ApplicationConstants.GROUPNAME.equals(status) && details.getGrpName() != null && "".equals(details.getGrpName())) {
			errors.reject("groupname.required");
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
}
