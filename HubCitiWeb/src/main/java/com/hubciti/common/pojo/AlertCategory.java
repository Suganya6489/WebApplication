package com.hubciti.common.pojo;

import java.util.List;

public class AlertCategory {

	private Integer totalSize;
	private List<Category> alertCatLst;

	public Integer getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * @return the alertCatLst
	 */
	public List<Category> getAlertCatLst() {
		return alertCatLst;
	}

	/**
	 * @param alertCatLst
	 *            the alertCatLst to set
	 */
	public void setAlertCatLst(List<Category> alertCatLst) {
		this.alertCatLst = alertCatLst;
	}

}
