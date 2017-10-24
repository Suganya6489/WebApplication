/**
 * Project     : Scan See
 * File        : Category.java
 * Author      : Kumar D
 * Company     : Span Systems   
 * Date Created: 20th January 2011
 */
package com.hubciti.common.pojo;

/**
 * POJO for app configuration.
 * 
 * @author sangeetha.ts
 */
public class AppConfiguration {

	/**
	 * stockImagePath variable declared as String.
	 */
	private String screenContent;
	/**
	 * stockHeader variable declared as String.
	 */
	private String screenName;

	/**
	 * Retrieve the value of screenContent.
	 * 
	 * @return the screenContent
	 */
	public final String getScreenContent() {
		return screenContent;
	}

	/**
	 * Set the value of screenContent.
	 * 
	 * @param screenContent
	 *            the screenContent to set
	 */
	public final void setScreenContent(String screenContent) {
		this.screenContent = screenContent;

	}

	/**
	 * Retrieve the value of screenName.
	 * 
	 * @return the screenName
	 */
	public final String getScreenName() {
		return screenName;
	}

	/**
	 * Set the value of screenName.
	 * 
	 * @param screenName
	 *            the screenName to set
	 */
	public final void setScreenName(String screenName) {
		this.screenName = screenName;

	}

}
