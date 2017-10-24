package com.hubciti.common.pojo;

import java.util.List;

public class CityExperienceDetail {

	List<CityExperience> cityExpLst;
	private String cityExpName;
	private Integer cityExpId;
	private Integer totalSize;

	public List<CityExperience> getCityExpLst() {
		return cityExpLst;
	}

	public void setCityExpLst(List<CityExperience> cityExpLst) {
		this.cityExpLst = cityExpLst;
	}

	public String getCityExpName() {
		return cityExpName;
	}

	public void setCityExpName(String cityExpName) {
		this.cityExpName = cityExpName;
	}

	public Integer getCityExpId() {
		return cityExpId;
	}

	public void setCityExpId(Integer cityExpId) {
		this.cityExpId = cityExpId;
	}

	public Integer getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}

}
