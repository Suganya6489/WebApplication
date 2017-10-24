package com.hubciti.common.pojo;

import java.util.List;

public class EventDetail {

	private List<Event> eventLst;
	private Integer totalSize;

	public List<Event> getEventLst() {
		return eventLst;
	}

	public void setEventLst(List<Event> eventLst) {
		this.eventLst = eventLst;
	}

	public Integer getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}
}
