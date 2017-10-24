/**
 * 
 */
package com.hubciti.common.pojo;

import java.util.List;

/**
 * @author sangeetha.ts
 */
public class FiltersDetails {
	/**
	 * 
	 */
	private List<Filters> filters;
	/**
	 * 
	 */
	private Integer totalSize;

	/**
	 * @return the filters
	 */
	public List<Filters> getFilters() {
		return filters;
	}

	/**
	 * @param filters
	 *            the filters to set
	 */
	public void setFilters(List<Filters> filters) {
		this.filters = filters;
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
