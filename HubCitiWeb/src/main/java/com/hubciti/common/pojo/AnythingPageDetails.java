/**
 * 
 */
package com.hubciti.common.pojo;

/**
 * @author sangeetha.ts
 */
public class AnythingPageDetails {

	private Integer hcAnythingPageId;

	private String anythingPageTitle;

	private String pageType;

	private String pageLink;

	private String pageImage;
	/**
	 * 
	 */
	private Boolean bottomButtonExist;
	/**
	 * 
	 */
	private Boolean menuItemExist;
	/**
	 * variable for page Content.
	 */
	private String pageView;

	/**
	 * @return the hcAnythingPageId
	 */
	public Integer getHcAnythingPageId() {
		return hcAnythingPageId;
	}

	/**
	 * @param hcAnythingPageId
	 *            the hcAnythingPageId to set
	 */
	public void setHcAnythingPageId(Integer hcAnythingPageId) {
		this.hcAnythingPageId = hcAnythingPageId;
	}

	/**
	 * @return the anythingPageTitle
	 */
	public String getAnythingPageTitle() {
		return anythingPageTitle;
	}

	/**
	 * @param anythingPageTitle
	 *            the anythingPageTitle to set
	 */
	public void setAnythingPageTitle(String anythingPageTitle) {
		this.anythingPageTitle = anythingPageTitle;
	}

	/**
	 * @return the pageType
	 */
	public String getPageType() {
		return pageType;
	}

	/**
	 * @param pageType
	 *            the pageType to set
	 */
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	/**
	 * @return the pageLink
	 */
	public String getPageLink() {
		return pageLink;
	}

	/**
	 * @param pageLink
	 *            the pageLink to set
	 */
	public void setPageLink(String pageLink) {
		this.pageLink = pageLink;
	}

	/**
	 * @return the pageImage
	 */
	public String getPageImage() {
		return pageImage;
	}

	/**
	 * @param pageImage
	 *            the pageImage to set
	 */
	public void setPageImage(String pageImage) {
		this.pageImage = pageImage;
	}

	/**
	 * @param bottomButtonExist
	 *            the bottomButtonExist to set
	 */
	public void setBottomButtonExist(Boolean bottomButtonExist) {
		this.bottomButtonExist = bottomButtonExist;
	}

	/**
	 * @return the bottomButtonExist
	 */
	public Boolean getBottomButtonExist() {
		return bottomButtonExist;
	}

	/**
	 * @param menuItemExist
	 *            the menuItemExist to set
	 */
	public void setMenuItemExist(Boolean menuItemExist) {
		this.menuItemExist = menuItemExist;
	}

	/**
	 * @return the menuItemExist
	 */
	public Boolean getMenuItemExist() {
		return menuItemExist;
	}

	/**
	 * @param pageView
	 *            the pageView to set
	 */
	public void setPageView(String pageView) {
		this.pageView = pageView;
	}

	/**
	 * @return the pageView
	 */
	public String getPageView() {
		return pageView;
	}

}
