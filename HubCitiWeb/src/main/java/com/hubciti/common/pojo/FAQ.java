/**
 * 
 */
package com.hubciti.common.pojo;

/**
 * @author sangeetha.ts
 */
public class FAQ {
	/**
	 * 
	 */
	private Integer userId;
	/**
	 * 
	 */
	private Integer hubCitiId;
	/**
	 * FAQID of type Integer.
	 */
	private Integer faqID;
	/**
	 * question of type String.
	 */
	private String question;
	/**
	 * answer of type String.
	 */
	private String answer;
	/**
	 * faqCatId of type Integer.
	 */
	private Integer faqCatId;
	/**
	 * faqCatName of type String.
	 */
	private String faqCatName;
	/**
	 * lowerLimit of type Integer.
	 */
	private Integer faqLowerLimit;
	/**
	 * searchKey of type String.
	 */
	private String faqSearchKey;
	/**
	 * associateFlag of type Boolean.
	 */
	private Boolean associateFlag;
	
	private String faqCatIds;
	
	private Integer sortOrder;
	
	private Integer rowNum;
		
	private String sortOrderIds;
	
	private String qstnIds;

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return the hubCitiId
	 */
	public Integer getHubCitiId() {
		return hubCitiId;
	}

	/**
	 * @param hubCitiId
	 *            the hubCitiId to set
	 */
	public void setHubCitiId(Integer hubCitiId) {
		this.hubCitiId = hubCitiId;
	}

	/**
	 * @return the faqID
	 */
	public Integer getFaqID() {
		return faqID;
	}

	/**
	 * @param faqID
	 *            the faqID to set
	 */
	public void setFaqID(Integer faqID) {
		this.faqID = faqID;
	}

	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * @param question
	 *            the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * @param answer
	 *            the answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/**
	 * @return the faqCatId
	 */
	public Integer getFaqCatId() {
		return faqCatId;
	}

	/**
	 * @param faqCatId
	 *            the faqCatId to set
	 */
	public void setFaqCatId(Integer faqCatId) {
		this.faqCatId = faqCatId;
	}

	/**
	 * @return the faqCatName
	 */
	public String getFaqCatName() {
		return faqCatName;
	}

	/**
	 * @param faqCatName
	 *            the faqCatName to set
	 */
	public void setFaqCatName(String faqCatName) {
		this.faqCatName = faqCatName;
	}

	/**
	 * @param faqLowerLimit
	 *            the faqLowerLimit to set
	 */
	public void setFaqLowerLimit(Integer faqLowerLimit) {
		this.faqLowerLimit = faqLowerLimit;
	}

	/**
	 * @return the faqLowerLimit
	 */
	public Integer getFaqLowerLimit() {
		return faqLowerLimit;
	}

	/**
	 * @return the faqSearchKey
	 */
	public String getFaqSearchKey() {
		return faqSearchKey;
	}

	/**
	 * @param faqSearchKey
	 *            the faqSearchKey to set
	 */
	public void setFaqSearchKey(String faqSearchKey) {
		this.faqSearchKey = faqSearchKey;
	}

	/**
	 * @param associateFlag
	 *            the associateFlag to set
	 */
	public void setAssociateFlag(Boolean associateFlag) {
		this.associateFlag = associateFlag;
	}

	/**
	 * @return the associateFlag
	 */
	public Boolean getAssociateFlag() {
		return associateFlag;
	}

	public String getFaqCatIds() {
		return faqCatIds;
	}

	public void setFaqCatIds(String faqCatIds) {
		this.faqCatIds = faqCatIds;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	public String getSortOrderIds() {
		return sortOrderIds;
	}

	public void setSortOrderIds(String sortOrderIds) {
		this.sortOrderIds = sortOrderIds;
	}

	public String getQstnIds() {
		return qstnIds;
	}

	public void setQstnIds(String qstnIds) {
		this.qstnIds = qstnIds;
	}

}
