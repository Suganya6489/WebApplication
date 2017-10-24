package com.hubciti.common.pojo;

import java.util.List;

public class SubMenuDetails {

	private Integer totalSize;
	List<MenuDetails> subMenuList;

	public Integer getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}

	public List<MenuDetails> getSubMenuList() {
		return subMenuList;
	}

	public void setSubMenuList(List<MenuDetails> subMenuList) {
		this.subMenuList = subMenuList;
	}

}
