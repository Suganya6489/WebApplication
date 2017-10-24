/**
 * 
 */
package com.hubciti.common.pojo;

import java.util.List;

/**
 * @author sangeetha.ts
 *
 */
public class UserDetails {
	
	private List<User> userLst;
	private Integer totalSize;
	/**
	 * @return the userLst
	 */
	public List<User> getUserLst() {
		return userLst;
	}
	/**
	 * @param userLst the userLst to set
	 */
	public void setUserLst(List<User> userLst) {
		this.userLst = userLst;
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
	
	
}
