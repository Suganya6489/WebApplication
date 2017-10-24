package com.hubciti.common.pojo;

import java.util.ArrayList;

public class Category {

	private Integer lowerLimit;
	private Integer rowNum;
	private Integer catId;
	private String catName;
	private boolean associateCate;
	private Integer subCatId;
	private String subCatName;
	
	private String catImgId;
	
	private String catImgPath;
	
	private ArrayList<SubCategory> subArrayList;
	/**
	 * @return the catId
	 */
	public Integer getCatId()

	{
		return catId;
	}

	/**
	 * @param catId
	 *            the catId to set
	 */
	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	/**
	 * @return the catyName
	 */
	public String getCatName() {
		return catName;
	}

	/**
	 * @param catyName
	 *            the catyName to set
	 */
	public void setCatName(String catName) {
		this.catName = catName;
	}

	public Integer getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(Integer lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	public boolean isAssociateCate() {
		return associateCate;
	}

	public void setAssociateCate(boolean associateCate) {
		this.associateCate = associateCate;
	}

	public Integer getSubCatId() {
		return subCatId;
	}

	public void setSubCatId(Integer subCatId) {
		this.subCatId = subCatId;
	}

	public String getSubCatName() {
		return subCatName;
	}

	public void setSubCatName(String subCatName) {
		this.subCatName = subCatName;
	}

	public ArrayList<SubCategory> getSubArrayList() {
		return subArrayList;
	}

	public void setSubArrayList(ArrayList<SubCategory> subArrayList) {
		this.subArrayList = subArrayList;
	}

	public String getCatImgId() {
		return catImgId;
	}

	public void setCatImgId(String catImgId) {
		this.catImgId = catImgId;
	}

	public String getCatImgPath() {
		return catImgPath;
	}

	public void setCatImgPath(String catImgPath) {
		this.catImgPath = catImgPath;
	}

}
