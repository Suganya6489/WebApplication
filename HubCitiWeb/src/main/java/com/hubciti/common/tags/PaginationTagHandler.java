package com.hubciti.common.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hubciti.common.constatns.ApplicationConstants;

public class PaginationTagHandler extends SimpleTagSupport {

	/**
	 * Getting the logger Instance.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(PaginationTagHandler.class);

	/**
	 * This variable holds the current page value.
	 */
	private String currentPage;

	/**
	 * This variable holds the nextPage value.
	 */
	private String nextPage;

	/**
	 * This variable holds the totalSize of list value.
	 */

	private String totalSize;

	/**
	 * This variable holds the pageRange or number or records per page value.
	 */

	private String pageRange;

	/**
	 * This variable holds the pageRange or number or records per page value.
	 */
	private String url;

	/**
	 * This method returns current Page.
	 * 
	 * @return the currentPage.
	 */
	public String getCurrentPage() {
		return currentPage;
	}

	/**
	 * This method sets current Page.
	 * 
	 * @param currentPage
	 *            the currentPage to set.
	 */
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * This method returns next Page.
	 * 
	 * @return the nextPage.
	 */
	public String getNextPage() {
		return nextPage;
	}

	/**
	 * his method sets nextPage.
	 * 
	 * @param nextPage
	 *            the nextPage to set.
	 */
	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

	/**
	 * @return the totalSize.
	 */
	public String getTotalSize() {
		return totalSize;
	}

	/**
	 * @param totalSize
	 *            the totalSize to set
	 */
	public void setTotalSize(String totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * @return the url.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the pageRange.
	 */
	public String getPageRange() {
		return pageRange;
	}

	/**
	 * @param pageRange
	 *            the pageRange to set.
	 */
	public void setPageRange(String pageRange) {
		this.pageRange = pageRange;
	}

	public void doTag() throws JspException {

		PageContext pageContext = (PageContext) getJspContext();
		JspWriter out = pageContext.getOut();
		System.out.println("Inside Page Tag");
		int previousPage = 0;
		int nextpage = 0;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("<b>Simple tag Example </b>");
			sb.append("Current Page " + this.currentPage + "\n");
			sb.append("nextPage " + this.nextPage + "\n");
			sb.append("totalSize" + this.totalSize + "\n");

			int numberOfPages = Integer.valueOf(this.totalSize) / Integer.valueOf(this.pageRange);
			int remainder = Integer.valueOf(this.totalSize) % Integer.valueOf(this.pageRange);
			System.out.println("numberOfPages " + numberOfPages);
			StringBuffer pageHtml = new StringBuffer();
			if (numberOfPages >= 1) {
				if (remainder > 0) {
					numberOfPages = numberOfPages + 1;
				}
				// pageHtml.append(startDiv);
				previousPage = Integer.valueOf(this.currentPage) - 1;
				nextpage = Integer.valueOf(this.currentPage) + 1;
				/*
				 * pageHtml.append(getPreviusPageHtml(previousPage));
				 * pageHtml.append(getNextPageHtml(nextpage,numberOfPages));
				 */
				// pageHtml.append(endDiv);
				if (Integer.valueOf(this.currentPage) == 1) {
					// To disable the Last records button
					pageHtml.append(getPreviusPageDisableHtml(previousPage));

				} else {
					pageHtml.append(getPreviusPageHtml(previousPage));
				}
				pageHtml.append(getPaginationText(numberOfPages));
				if (Integer.valueOf(this.currentPage) == numberOfPages) {
					// to disable the Next button
					pageHtml.append(getNextPageDisableHtml(nextpage, numberOfPages));
				} else {
					pageHtml.append(getNextPageHtml(nextpage, numberOfPages));
				}
			}/*
			 * else{ pageHtml.append(getPaginationText(numberOfPages)); }
			 */

			out.println(pageHtml.toString());
		} catch (Exception e) {
			LOG.error(ApplicationConstants.EXCEPTIONOCCURRED + e.getStackTrace());
		}

	}

	public String getPreviusPageHtml(int previousPage) {
		StringBuffer prevContent = new StringBuffer();
		prevContent
				.append("<a class=\"backward\"  title=\"BackWard\" href=\"#\" onclick = \"callNextPage(" + 1 + ",'" + this.url + "')\">&nbsp;</a>");
		prevContent.append("<a class=\"previous\" href=\"#\" title=\"Previous\" onclick = \"callNextPage(" + previousPage + ",'" + this.url
				+ "')\">&nbsp;</a>");
		return prevContent.toString();
	}

	public String getPreviusPageDisableHtml(int previousPage) {
		StringBuffer prevContent = new StringBuffer();
		prevContent.append("<a href=\"#\" onclick = \"callNextPage(" + 0 + ",'" + this.url + "')\">&nbsp;</a>");
		prevContent.append("<a href=\"#\" onclick = \"callNextPage(" + previousPage + ",'" + this.url + "')\">&nbsp;</a>");
		return prevContent.toString();
	}

	public String getNextPageHtml(int nextpage, int numberOfPages) {
		StringBuffer nextContent = new StringBuffer();
		nextContent.append("<a class=\"next\" href=\"#\" title=\"Next\" onclick = \"callNextPage(" + nextpage + ",'" + this.url + "')\">&nbsp;</a>");
		nextContent.append("<a class=\"forward\" href=\"#\" title=\"ForWard\" onclick = \"callNextPage(" + numberOfPages + ",'" + this.url
				+ "')\">&nbsp;</a>");
		return nextContent.toString();
	}

	public String getNextPageDisableHtml(int nextpage, int numberOfPages) {
		StringBuffer nextContent = new StringBuffer();
		if (nextpage >= numberOfPages) {
			nextpage = numberOfPages;
		}
		nextContent.append("<a href=\"#\" onclick = \"callNextPage(" + nextpage + ",'" + this.url + "')\">&nbsp;</a>");
		nextContent.append("<a href=\"#\" onclick = \"callNextPage(" + numberOfPages + ",'" + this.url + "')\">&nbsp;</a>");
		return nextContent.toString();
	}

	public String getPaginationText(int noOfpages) {
		StringBuffer sb = new StringBuffer();
		sb.append("Page " + this.currentPage + " of " + noOfpages + "");
		return sb.toString();
	}
}
