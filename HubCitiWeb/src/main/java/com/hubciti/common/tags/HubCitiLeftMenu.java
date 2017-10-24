package com.hubciti.common.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.util.Utility;

/**
 * Tag Class for Left Menu.
 * 
 * @author dileepa_cc
 */
public class HubCitiLeftMenu extends SimpleTagSupport {
	// this tag will take module name which user has clicked and based on value
	// it will form the html and return.
	/**
	 * Getting the logger Instance.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(HubCitiLeftMenu.class);

	/**
	 * Variable for menu Deatails.
	 */
	String[][] menuDetails = new String[][] { { ApplicationConstants.HUBCITIADMINISTRATION, "welcome.htm:icon-adminStng" },
			{ ApplicationConstants.LOGINSCREEN, "setuploginscreen.htm:icon-login" },
			{ ApplicationConstants.GENERALSETTINGS, "generealsettings.htm:icon-setting" },
			{ ApplicationConstants.SPLASHSCREEN, "setupsplashscreen.htm:icon-welcome" },
			{ ApplicationConstants.USERSETTINGSSCREEN, "usersettings.htm:icon-usrstng" },
			{ ApplicationConstants.ABOUTUS, "setupaboutusscreen.htm:icon-about" },
			{ ApplicationConstants.REGISTRATION, "setupregscreen.htm:icon-registration" },
			{ ApplicationConstants.PRIVACYPOLICY, "setupprivacypolicyscreen.htm:icon-privacy-policy" },
			{ ApplicationConstants.MAINMENUSCREEN, "displaymainmenu.htm:icon-menu" },
			{ ApplicationConstants.SETUPSUBMENU, "displaysubmenu.htm:icon-submenu"},
			{ ApplicationConstants.SETUPBOTTOMTABBAR, "setuptabbar.htm:icon-tabbar" },
			{ ApplicationConstants.ANYTHINGPAGESCREEN, "displayanythingpages.htm:icon-about" },
			{ ApplicationConstants.SETUPALERTS, "displayalerts.htm:icon-alert" },
			{ ApplicationConstants.SETUPEVENTS, "manageevents.htm:icon-event" },
			{ ApplicationConstants.CITYEXPERIENCE, "displaycityexp.htm:icon-experience" },
			{ ApplicationConstants.SETUPRETAILERLOCATION, "getassociretlocs.htm:icon-retlrLoctn" },
			{ ApplicationConstants.SETUPFAQS, "displayfaq.htm:icon-faq" }

	};

	/**
	 * This variable holds selects menu name.
	 */
	public String menuTitle = null;

	/**
	 * retrieves menuTitle value.
	 * 
	 * @return the menuTitle
	 */
	public final String getMenuTitle() {
		return menuTitle;
	}

	/**
	 * sets value for menuTitle.
	 * 
	 * @param menuTitle
	 *            the menuTitle to set
	 */
	public final void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}

	/**
	 * This method is for left menu.
	 * 
	 * @throws JspException
	 * @throws IOException
	 */
	@Override
	public final void doTag() throws JspException, IOException {
		LOG.info("Inside HubCiti Left Menu Tag : doTag ");
		final PageContext pageContext = (PageContext) getJspContext();
		final JspWriter out = pageContext.getOut();
		final StringBuffer menuStrBuffer = new StringBuffer(" <ul id=\"icon-menu\">");
		if (!"".equals(Utility.checkNull(this.menuTitle))) {
			/*
			 * if (HOME.equals(this.menuTitle)) {
			 */
			menuStrBuffer.append(getMenuItem());
			menuStrBuffer.append("</ul>");
			LOG.info("Left Menu Item :: " + menuStrBuffer.toString());
			out.write(menuStrBuffer.toString());

			// }
		} else {
			LOG.error("No Menu items Provided");

		}

	}

	/**
	 * This method will give menu items.
	 * 
	 * @return String
	 */
	public final String getMenuItem() {
		final StringBuffer itemStrBuffer = new StringBuffer();
		String[] menuItem = null;
		String cssClassName = null;
		for (int i = 0; i < menuDetails.length; i++) {
			menuItem = menuDetails[i][1].split(":");
			if (menuTitle.equals(menuDetails[i][0])) {
				itemStrBuffer.append("<li class=\"active\">");
				cssClassName = menuItem[1] + "-active";
			} else {
				itemStrBuffer.append("<li>");
				cssClassName = menuItem[1];
			}

			if (menuDetails[i][0].equals(ApplicationConstants.HUBCITIADMINISTRATION)) {

				itemStrBuffer.append("<a href=\"" + menuItem[0] + "\" class=\"" + cssClassName + "\" title=\"" + "HubCiti Administration" + "\" >");
			} else
				if (menuDetails[i][0].equals(ApplicationConstants.ANYTHINGPAGESCREEN)) {

					itemStrBuffer.append("<a href=\"" + menuItem[0] + "\" class=\"" + cssClassName + "\" title=\"" + "Setup AnyThing Page" + "\" >");
				} else {

					itemStrBuffer.append("<a href=\"" + menuItem[0] + "\" class=\"" + cssClassName + "\" title=\"" + menuDetails[i][0] + "\" >");
				}

			itemStrBuffer.append("<span>");
			itemStrBuffer.append(menuDetails[i][0]);
			itemStrBuffer.append("</span></a></li>");
		}
		return itemStrBuffer.toString();
	}
}
