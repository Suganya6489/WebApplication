package com.hubciti.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.pojo.ScreenSettings;
import com.hubciti.common.util.Utility;

/**
 * This class is a validator class for about us screen.
 * 
 * @author Kumar D
 */
public class AnyThingScreenSettingsValidation implements Validator {
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
		final ScreenSettings objScreenSettings = (ScreenSettings) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pageTitle", "pageTitle.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pageType", "pageType.required");

		if (null != objScreenSettings.getPageTypeHid() && !"".equalsIgnoreCase(objScreenSettings.getPageTypeHid())) {
			if ("Website".equalsIgnoreCase(objScreenSettings.getPageTypeHid())) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pageAttachLink", "webUrl.required");
			} else {
				if (null != objScreenSettings.getFilePaths() && objScreenSettings.getFilePaths().getSize() <= 0) {
					if (null == objScreenSettings.getPathName() || "".equalsIgnoreCase(objScreenSettings.getPathName())) {
						errors.rejectValue("filePaths", "file.required");
					}
				}
			}
		}

		if ("exstngIcon".equals(objScreenSettings.getIconSelect())) {
			if (null == objScreenSettings.getImageIconID()) {
				errors.rejectValue("logoImage", "imageIcon");
			} else
				if (null != objScreenSettings.getImageIconID() && !(objScreenSettings.getImageIconID() > 0)) {
					errors.rejectValue("logoImage", "imageIcon");
				}
		} else
			if ("icnSlctn".equals(objScreenSettings.getIconSelect())) {
				if ("".equals(Utility.checkNull(objScreenSettings.getLogoImageName()))) {
					errors.rejectValue("logoImage", "imageFile");
				}
			}
	}

	/**
	 * This method validate Retailer custom page.
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
			errors.rejectValue("pageAttachLink", "invalid.url");
		}
	}

	/**
	 * this method validates mandatory fields
	 */
	public void validateMakeYourAnythingPage(Object target, Errors errors) {
		final ScreenSettings objScreenSettings = (ScreenSettings) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pageTitle", "pageTitle.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shortDescription", "shortDescription.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "longDescription", "longDescription.required");
		if ("".equals(Utility.checkNull(objScreenSettings.getLogoImageName()))) {
			errors.rejectValue("logoImageName", "imageFile");
		}
	}

	/**
	 * This method validate Retailer Special Offer page screen.
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
