/**
 * 
 */
package com.hubciti.common.pojo;

import java.util.List;

/**
 * @author sangeetha.ts
 *
 */
public class TabBarDetails {	
	/**
	 * variable for tabBarList.
	 */
	private List<ScreenSettings> tabBarList;
	/**
	 * variable for totalSize.
	 */
	private Integer	totalSize;
	/**
	 * variable for functionalityName.
	 */
	private String functionalityName;
	/**
	 * variable for functionalityId.
	 */
	private Integer functionalityId;
	
	/**
	 * @return the tabBarList
	 */
	public List<ScreenSettings> getTabBarList() {
		return tabBarList;
	}
	/**
	 * @param tabBarList the tabBarList to set
	 */
	public void setTabBarList(List<ScreenSettings> tabBarList) {
		this.tabBarList = tabBarList;
	}
	/**
	 * @return the totalSize
	 */
	public Integer getTotalSize() {
		return totalSize;
	}
	/**
	 * @param totalSize the totalSize to set
	 */
	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}
	/**
	 * @return the functionalityName
	 */
	public String getFunctionalityName() {
		return functionalityName;
	}
	/**
	 * @param functionalityName the functionalityName to set
	 */
	public void setFunctionalityName(String functionalityName) {
		this.functionalityName = functionalityName;
	}
	/**
	 * @return the functionalityId
	 */
	public Integer getFunctionalityId() {
		return functionalityId;
	}
	/**
	 * @param functionalityId the functionalityId to set
	 */
	public void setFunctionalityId(Integer functionalityId) {
		this.functionalityId = functionalityId;
	}
}
