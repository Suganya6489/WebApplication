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
 * @author sangeetha.ts
 *
 */
public class UsersValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {		
		User user = (User) target;		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "firstName.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "lastName.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "userName.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailId", "emailId.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "module", "module.required");
		if("NORMAL_USER".equals(user.getUserType())){
			if(null != user.getModuleName() && !"".equals(user.getModuleName())) {
				String[] splitCats = user.getModuleName().split(",");
				for(String cat : splitCats) {
					if(cat.equalsIgnoreCase("Events")) {
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eventCategory", "eventCategory.required");
					} else if(cat.equalsIgnoreCase("Fundraisers")) {
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fundraiserCategory", "eventCategory.required");
					}
				} 
			}
		}	
	}
	
	public void validate(Object arg0, Errors errors, String status)
	{
		if (status.equals(ApplicationConstants.INVALIDEMAIL))
		{
			errors.rejectValue("emailId", "invalid.email");
		}
		if (status.equals(ApplicationConstants.DUPLICATEUSERNAME))
		{
			errors.rejectValue("userName", "userName.exist");
		}
		if (status.equals(ApplicationConstants.DUPLICATEEMAIL))
		{
			errors.rejectValue("emailId", "email.exist");
		}
	}
	
	public void editValidate(Object target, Errors errors) {		
		User user = (User) target;		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "firstName.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "lastName.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailId", "emailId.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "module", "module.required");
		if("NORMAL_USER".equals(user.getUserType())){
			if(null != user.getModuleName() && !"".equals(user.getModuleName())) {
				String[] splitCats = user.getModuleName().split(",");
				for(String cat : splitCats) {
					if(cat.equalsIgnoreCase("Events")) {
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eventCategory", "eventCategory.required");
					} else if(cat.equalsIgnoreCase("Fundraisers")) {
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fundraiserCategory", "eventCategory.required");
					}
				} 
			}
		}
		/*if("NORMAL_USER".equals(user.getUserType())){
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eventCategory", "eventCategory.required");
		}*/	
	}
}
