package com.hubciti.common.pojo;

public class HubCitiImages {
	/**
	 * 
	 */
	public String imagePath;

	/**
	 * 
	 */
	public int customPageIconID;
	/**
	 * 
	 */
	private String pageTypeName;
	/**
	 * 
	 */
	private Integer pageTypeId;

	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * @param imagePath
	 *            the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	/**
	 * @return the customPageIconID
	 */
	public int getCustomPageIconID() {
		return customPageIconID;
	}

	/**
	 * @param customPageIconID
	 *            the customPageIconID to set
	 */
	public void setCustomPageIconID(int customPageIconID) {
		this.customPageIconID = customPageIconID;
	}

	/**
	 * @param pageTypeName
	 *            the pageTypeName to set
	 */
	public void setPageTypeName(String pageTypeName) {
		this.pageTypeName = pageTypeName;
	}

	/**
	 * @return the pageTypeName
	 */
	public String getPageTypeName() {
		return pageTypeName;
	}

	/**
	 * @param pageTypeId
	 *            the pageTypeId to set
	 */
	public void setPageTypeId(Integer pageTypeId) {
		this.pageTypeId = pageTypeId;
	}

	/**
	 * @return the pageTypeId
	 */
	public Integer getPageTypeId() {
		return pageTypeId;
	}
}
