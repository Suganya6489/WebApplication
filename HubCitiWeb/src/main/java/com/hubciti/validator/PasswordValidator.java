/**
 * 
 */
package com.hubciti.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.pojo.User;

/**
 * This class is a validator class for about us screen.
 * 
 * @author dileep_cc
 */
public class PasswordValidator implements Validator {
	/**
	 * this method returns true or false
	 */
	public boolean supports(Class<?> target) {
		return User.class.isAssignableFrom(target);
	}

	/**
	 * this method validates mandatory fields
	 */
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "confirmPassword.required");
	}

	/**
	 * This method validate user change password screen.
	 * 
	 * @param arg0
	 *            instance of Object.
	 * @param errors
	 *            instance of Errors.
	 * @param status
	 *            as request parameter.
	 */
	public final void validate(Object arg0, Errors errors, String status) {
		if (status.equals(ApplicationConstants.PASSWORD)) {
			errors.reject("password.match");
		}
		if (status.equals(ApplicationConstants.PASSWORDLength)) {
			errors.rejectValue("password", "password.length");
		} else
			if (status.equals(ApplicationConstants.PASSWORDMATCH)) {
				errors.rejectValue("password", "password.digit");
			}
	}

}
