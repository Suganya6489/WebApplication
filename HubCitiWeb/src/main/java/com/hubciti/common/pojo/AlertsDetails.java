/**
 * 
 */
package com.hubciti.common.pojo;

import java.util.List;

/**
 * @author sangeetha.ts
 */
public class AlertsDetails {
	/**
	 * 
	 */
	private List<Alerts> alerts;
	/**
	 * 
	 */
	private Integer totalSize;

	/**
	 * @return the alerts
	 */
	public List<Alerts> getAlerts() {
		return alerts;
	}

	/**
	 * @param alerts
	 *            the alerts to set
	 */
	public void setAlerts(List<Alerts> alerts) {
		this.alerts = alerts;
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
