/**
 * 
 */
package com.hubciti.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.hubciti.common.pojo.FAQ;

/**
 * @author sangeetha.ts
 */
public class FAQValidator implements Validator {
	/**
	 * this method returns true or false
	 */
	public boolean supports(Class<?> target) {
		return FAQ.class.isAssignableFrom(target);
	}

	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "faqCatId", "category.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "question", "question.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "answer", "answer.required");
	}
}
