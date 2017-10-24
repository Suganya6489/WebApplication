/**
 * 
 */
package com.hubciti.common.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sangeetha.ts
 *
 */
public class DealDetails {
	/*
	 * variable for deal Id
	 */
	private Integer dealId;
	/*
	 * variable for deal Name
	 */
	private String dealName;
	/*
	 * variable for deals
	 */
	private List<Deals> deals;
	/*
	 * variable for total Size 
	 */
	private Integer totalSize;
	/**
	 * @return the dealId
	 */
	public Integer getDealId() {
		return dealId;
	}
	/**
	 * @param dealId the dealId to set
	 */
	public void setDealId(Integer dealId) {
		this.dealId = dealId;
	}
	/**
	 * @return the dealName
	 */
	public String getDealName() {
		return dealName;
	}
	/**
	 * @param dealName the dealName to set
	 */
	public void setDealName(String dealName) {
		this.dealName = dealName;
	}
	/**
	 * @return the deals
	 */
	public List<Deals> getDeals() {
		return deals;
	}
	/**
	 * @param dealList the deals to set
	 */
	public void setDeals(List<Deals> dealList) {
		this.deals = dealList;
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
