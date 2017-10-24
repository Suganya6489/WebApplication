/**
 * 
 */
package com.hubciti.common.pojo;

import java.util.List;

/**
 * @author sangeetha.ts
 */
public class FAQDetails {
	/**
	 * categoryName of type String.
	 */
	private String categoryName;
	
	private Integer faqcateId;

	/**
	 * faqs of type List.
	 */
	private List<FAQ> faqs;
	/**
	 * totalSize of type Integer.
	 */
	private Integer totalSize;

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param categoryName
	 *            the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * @return the faqs
	 */
	public List<FAQ> getFaqs() {
		return faqs;
	}

	/**
	 * @param faqs
	 *            the faqs to set
	 */
	public void setFaqs(List<FAQ> faqs) {
		this.faqs = faqs;
	}

	/**
	 * @param totalSize
	 *            the totalSize to set
	 */
	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * @return the totalSize
	 */
	public Integer getTotalSize() {
		return totalSize;
	}

	public Integer getFaqcateId() {
		return faqcateId;
	}

	public void setFaqcateId(Integer faqcateId) {
		this.faqcateId = faqcateId;
	}
}
