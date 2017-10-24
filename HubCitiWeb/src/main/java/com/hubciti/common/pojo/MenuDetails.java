/**
 * 
 */
package com.hubciti.common.pojo;

import java.util.List;

/**
 * @author sangeetha.ts
 */
public class MenuDetails {
	/**
	 * variable for menu level.
	 */
	Integer level;
	/**
	 * variable for menu menuName.
	 */
	String menuName;
	/**
	 * variable for menu Type Id.
	 */
	private Integer menuId;

	/**
	 * variable for menu Type Id.
	 */
	private Integer menuTypeId;
	/**
	 * variable for menu Type Name.
	 */
	private String menuTypeName;
	/**
	 * variable for menu position.
	 */
	private Integer position;

	private ButtonDetails buttonDetails;

	private String bannerImg;
	/**
	 * @return the level
	 */
	private String bannerImageName;
	private String strBottomBtnId;

	private String menuTypeVal;

	private boolean typeFlag;

	private boolean departmentFlag;

	private Integer noOfColumns;

	private String comboBtnType;
	
	

	public Integer getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * @return the menuName
	 */
	public String getMenuName() {
		return menuName;
	}

	/**
	 * @param menuName
	 *            the menuName to set
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	/**
	 * variable for buttons.
	 */
	private List<ButtonDetails> buttons;

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	/**
	 * retrieves the value of menuTypeName.
	 * 
	 * @return the menuTypeName
	 */
	public String getMenuTypeName() {
		return menuTypeName;
	}

	/**
	 * Set the value for menuTypeName.
	 * 
	 * @param menuTypeName
	 *            the menuTypeName to set
	 */
	public void setMenuTypeName(String menuTypeName) {
		this.menuTypeName = menuTypeName;
	}

	/**
	 * retrieves the value of buttons.
	 * 
	 * @return the buttons
	 */
	public List<ButtonDetails> getButtons() {
		return buttons;
	}

	/**
	 * Set the value for buttons.
	 * 
	 * @param buttons
	 *            the buttons to set
	 */
	public void setButtons(List<ButtonDetails> buttons) {
		this.buttons = buttons;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(Integer position) {
		this.position = position;
	}

	/**
	 * @return the position
	 */
	public Integer getPosition() {
		return position;
	}

	public ButtonDetails getButtonDetails() {
		return buttonDetails;
	}

	public void setButtonDetails(ButtonDetails buttonDetails) {
		this.buttonDetails = buttonDetails;
	}

	public Integer getMenuTypeId() {
		return menuTypeId;
	}

	public void setMenuTypeId(Integer menuTypeId) {
		this.menuTypeId = menuTypeId;
	}

	public String getBannerImg() {
		return bannerImg;
	}

	public void setBannerImg(String bannerImg) {
		this.bannerImg = bannerImg;
	}

	public String getStrBottomBtnId() {
		return strBottomBtnId;
	}

	public void setStrBottomBtnId(String strBottomBtnId) {
		this.strBottomBtnId = strBottomBtnId;
	}

	public String getBannerImageName() {
		return bannerImageName;
	}

	public void setBannerImageName(String bannerImageName) {
		this.bannerImageName = bannerImageName;
	}

	public String getMenuTypeVal() {
		return menuTypeVal;
	}

	public void setMenuTypeVal(String menuTypeVal) {
		this.menuTypeVal = menuTypeVal;
	}

	public boolean isTypeFlag() {
		return typeFlag;
	}

	public void setTypeFlag(boolean typeFlag) {
		this.typeFlag = typeFlag;
	}

	public boolean isDepartmentFlag() {
		return departmentFlag;
	}

	public void setDepartmentFlag(boolean departmentFlag) {
		this.departmentFlag = departmentFlag;
	}

	public Integer getNoOfColumns() {
		return noOfColumns;
	}

	public void setNoOfColumns(Integer noOfColumns) {
		this.noOfColumns = noOfColumns;
	}

	/**
	 * @return the comboBtnType
	 */
	public String getComboBtnType() {
		return comboBtnType;
	}

	/**
	 * @param comboBtnType
	 *            the comboBtnType to set
	 */
	public void setComboBtnType(String comboBtnType) {
		this.comboBtnType = comboBtnType;
	}

	
}
