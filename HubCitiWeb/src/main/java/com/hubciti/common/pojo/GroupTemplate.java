package com.hubciti.common.pojo;

import java.util.List;

public class GroupTemplate {

	String grpName;

	List<ScreenSettings> grpBtnsList = null;

	public String getGrpName() {
		return grpName;
	}

	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}

	public List<ScreenSettings> getGrpBtnsList() {
		return grpBtnsList;
	}

	public void setGrpBtnsList(List<ScreenSettings> grpBtnsList) {
		this.grpBtnsList = grpBtnsList;
	}

}
