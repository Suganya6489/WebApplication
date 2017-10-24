/**
 * 
 */
package com.hubciti.common.pojo;

import java.util.List;

/**
 * @author sangeetha.ts
 */
public class AnythingPages {
	/**
	 * 
	 */
	private List<AnythingPageDetails> pageDetails;
	/**
	 * 
	 */
	private Integer totalSize;

	/**
	 * @return the pageDetails
	 */
	public List<AnythingPageDetails> getPageDetails() {
		return pageDetails;
	}

	/**
	 * @param pageDetails
	 *            the pageDetails to set
	 */
	public void setPageDetails(List<AnythingPageDetails> pageDetails) {
		this.pageDetails = pageDetails;
	}

	/**
	 * @return the totalSize
	 */
	public Integer getTotalSize() {
		return totalSize;
	}

	/**
	 * @param totalSize
	 *            the totalSize to set
	 */
	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}

}
