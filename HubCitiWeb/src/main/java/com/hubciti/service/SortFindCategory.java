package com.hubciti.service;

import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.pojo.Category;

public class SortFindCategory implements Comparator<Category> {

	public static final Logger LOG = LoggerFactory.getLogger(SortFindCategory.class);

	public int compare(Category objCategory1, Category objCategory2) {

		String methodName = "compare";
		LOG.info(ApplicationConstants.METHODSTART + methodName);
		String cateName1 = null;
		String cateName2 = null;
		int cateCompare = 0;

		try {
			cateName1 = objCategory1.getCatName();
			cateName2 = objCategory2.getCatName();
			cateCompare = cateName1.compareTo(cateName2);

		} catch (Exception exception) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + methodName, exception);

		}
		// TODO Auto-generated method stub
		return cateCompare;
	}

}
