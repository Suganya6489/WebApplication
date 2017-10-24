package com.hubciti.controller;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.hubciti.common.constatns.ApplicationConstants;
import com.hubciti.common.exception.HubCitiServiceException;
import com.hubciti.common.pojo.Department;
import com.hubciti.common.pojo.Event;
import com.hubciti.common.pojo.ScreenSettings;
import com.hubciti.common.pojo.Type;
import com.hubciti.common.util.Utility;

/**
 * This Class is a Controller Class for Image Cropping.
 * 
 * @author dileepa_cc
 */
@Controller
public class ImageCropController {

	/**
	 * Getting the logger Instance.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ImageCropController.class);

	/**
	 * This method returns image cropping pop up.
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @param session1
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/cropImage.htm", method = RequestMethod.GET)
	public String cropImage(HttpServletRequest request, HttpSession session, ModelMap model, HttpSession session1) throws HubCitiServiceException {

		LOG.info("cropImage:: Inside Get Method");
		LOG.info("cropImage: Inside Exit Get Method");
		return "imageCropPopUp";

	}

	/**
	 * This method crops the selected portion of the image.
	 * 
	 * @param loginPageDetails
	 * @param result
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/uploadimg.htm", method = RequestMethod.POST)
	public String onSubmitImage(@ModelAttribute("screenSettingsForm") ScreenSettings loginPageDetails, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception, HubCitiServiceException {
		final String fileSeparator = System.getProperty("file.separator");
		String imageSource = null;
		boolean imageSizeValFlg = false;
		final boolean imageValidSizeValFlg = false;
		final StringBuffer strResponse = new StringBuffer();
		final Date date = new Date();
		session.removeAttribute("cropImageSource");
		int w = 0;
		int h = 0;
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/xml");
		String imageFileName = null;
		InputStream inputStream = null;
		session.removeAttribute("imageCropPage");

		if ("setupanythingpage".equalsIgnoreCase(loginPageDetails.getViewName())
				|| "editanythingpage".equalsIgnoreCase(loginPageDetails.getViewName())) {
			if (null != loginPageDetails.getFilePaths() && !loginPageDetails.getFilePaths().isEmpty()) {
				String tempImgPath = Utility.getTempMediaPath(ApplicationConstants.TEMP).toString();
				String fileTempPath = tempImgPath + fileSeparator + loginPageDetails.getFilePaths().getOriginalFilename();
				Utility.writeFileData(loginPageDetails.getFilePaths(), fileTempPath);

				if ("Image".equalsIgnoreCase(loginPageDetails.getPageTypeHid())) {
					String imagetempSource = FilenameUtils.removeExtension(loginPageDetails.getFilePaths().getOriginalFilename());
					imagetempSource = imagetempSource + ".png";

					loginPageDetails.setPathName(imagetempSource);
					session.setAttribute("uploadedFile", imagetempSource);
				} else {
					loginPageDetails.setPathName(loginPageDetails.getFilePaths().getOriginalFilename());
					session.setAttribute("uploadedFile", loginPageDetails.getFilePaths().getOriginalFilename());
				}
			}
		}

		if ("setupcombomenu".equalsIgnoreCase(loginPageDetails.getViewName())) {

			String grpList = loginPageDetails.getGrpList();
			List<String> comboList = new ArrayList<String>();
			if (null != grpList && !"".equals(grpList)) {
				String[] grpNameList = grpList.split("~");

				for (int i = 0; i < grpNameList.length; i++) {
					comboList.add(grpNameList[i]);
				}
				session.setAttribute("comboList", comboList);
			}
			loginPageDetails.setHiddenPageTitle(null);
		}

		if (null == loginPageDetails.getUploadImageType() || "logoImage".equals(loginPageDetails.getUploadImageType())) {
			inputStream = loginPageDetails.getLogoImage().getInputStream();
			imageFileName = loginPageDetails.getLogoImage().getOriginalFilename();
		} else
			if ("imageFile".equals(loginPageDetails.getUploadImageType())) {
				if ("iconicmenutemplate".equals(loginPageDetails.getViewName())) {
					session.setAttribute("minCropHt", 50);
					session.setAttribute("minCropWd", 320);
				} else
					if ("generalsettings".equals(loginPageDetails.getViewName())) {
						session.setAttribute("minCropHt", 57);
						session.setAttribute("minCropWd", 57);

					}
				inputStream = loginPageDetails.getImageFile().getInputStream();
				imageFileName = loginPageDetails.getImageFile().getOriginalFilename();
			}

		String[] menuFilterType = loginPageDetails.getMenuFilterType();

		if (null != menuFilterType) {
			if (menuFilterType.length != 0) {

				session.setAttribute("menuFilterType", menuFilterType);
			} else {

				menuFilterType = (String[]) session.getAttribute("menuFilterType");
				loginPageDetails.setMenuFilterType(menuFilterType);
			}

		}

		List<Type> filterTypeList = (ArrayList<Type>) session.getAttribute("filterTypeList");
		List<Department> filterDeptList = (ArrayList<Department>) session.getAttribute("filterDeptList");

		if (null != filterTypeList) {
			Type newType = null;
			if (null != loginPageDetails.getBtnType() && !"".equals(loginPageDetails.getBtnType()) && !"0".equals(loginPageDetails.getBtnType())) {

				for (int i = 0; i < filterTypeList.size(); i++) {
					Type type = filterTypeList.get(i);

					if (type.getTypeName().equals(loginPageDetails.getBtnType())) {

						break;
					}

					if (i == filterTypeList.size() - 1) {
						newType = new Type();
						newType.setTypeName(loginPageDetails.getBtnType());
						filterTypeList.add(newType);
						session.setAttribute("filterTypeList", filterTypeList);
					}
				}

			}

		}

		if (null != filterDeptList) {
			Department newDept = null;

			if (null != loginPageDetails.getBtnDept() && !"".equals(loginPageDetails.getBtnDept()) && !"0".equals(loginPageDetails.getBtnDept())) {
				for (int i = 0; i < filterDeptList.size(); i++) {
					Department dept = filterDeptList.get(i);

					if (dept.getDeptName().equals(loginPageDetails.getBtnDept())) {

						break;
					}

					if (i == filterDeptList.size() - 1) {
						newDept = new Department();
						newDept.setDeptName(loginPageDetails.getBtnDept());
						filterDeptList.add(newDept);
						session.setAttribute("filterDeptList", filterDeptList);
					}
				}

			}

		}

		imageSizeValFlg = Utility.validMinDimension(ApplicationConstants.CROPIMAGEHEIGHT, ApplicationConstants.CROPIMAGEWIDTH, inputStream);

		if (imageSizeValFlg) {

			response.getWriter().write("<imageScr>" + "maxSizeImageError" + "</imageScr>");
			return null;
		} else {
			if (null == loginPageDetails.getUploadImageType() || "logoImage".equals(loginPageDetails.getUploadImageType())) {
				inputStream = loginPageDetails.getLogoImage().getInputStream();

			} else
				if ("imageFile".equals(loginPageDetails.getUploadImageType())) {
					if (null != inputStream) {
						inputStream.close();
					}
					inputStream = loginPageDetails.getImageFile().getInputStream();

				}
			@SuppressWarnings("deprecation")
			final BufferedImage img = Utility.getBufferedImageForMinDimension(inputStream, request.getRealPath("images"), imageFileName,
					"WelcomePage");
			w = img.getWidth(null);
			h = img.getHeight(null);
			session.setAttribute("imageHt", h);
			session.setAttribute("imageWd", w);

			final String tempImgPath = Utility.getTempMediaPath(ApplicationConstants.TEMP).toString();
			// session.removeAttribute("welcomePageBtnVal");
			imageSource = imageFileName;
			imageSource = FilenameUtils.removeExtension(imageSource);
			imageSource = imageSource + ".png" + "?" + date.getTime();
			final String filePath = tempImgPath + fileSeparator + imageFileName;
			// Utility.writeFileData(objRetLocAds.getBannerAdImagePath(),
			// filePath);
			Utility.writeImage(img, filePath);
			if (imageValidSizeValFlg) {
				strResponse.append("ValidImageDimention");
				strResponse.append("|" + imageFileName);

				session.setAttribute("welcomePageCreateImgPath", "/" + ApplicationConstants.IMAGES + "/" + ApplicationConstants.TEMPFOLDER + "/"
						+ imageSource);

			}
			strResponse.append("|" + "/" + ApplicationConstants.IMAGES + "/" + ApplicationConstants.TEMPFOLDER + "/" + imageSource);
			response.getWriter().write("<imageScr>" + strResponse.toString() + "</imageScr>");

			// Used for image cropping popup
			session.setAttribute("cropImageSource", "/" + ApplicationConstants.IMAGES + "/" + ApplicationConstants.TEMPFOLDER + "/" + imageSource);

			// }
			if (null != inputStream) {

				inputStream.close();
			}
			return null;
		}

	}

	/**
	 * This method displays or uploads the cropped image.
	 * 
	 * @param loginScreenDetails
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/uploadcroppedimage.htm", method = RequestMethod.POST)
	public String uploadCroppedImage(@ModelAttribute("screenSettingsForm") ScreenSettings loginScreenDetails, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model) throws HubCitiServiceException {

		String imageSource = null;
		String outputFileName = null;
		String uploadImageType = "logoImage";

		if (null == loginScreenDetails.getUploadImageType() || "logoImage".equals(loginScreenDetails.getUploadImageType())) {
			uploadImageType = "logoImage";

		} else
			if ("imageFile".equals(loginScreenDetails.getUploadImageType())) {
				uploadImageType = "imageFile";

			}

		if ("logoImage".equals(uploadImageType)) {

			outputFileName = saveCroppedImage(request, response, session, loginScreenDetails.getLogoImage());
		} else
			if ("imageFile".equals(uploadImageType)) {
				outputFileName = saveCroppedImage(request, response, session, loginScreenDetails.getImageFile());
			}

		String[] menuFilterType = loginScreenDetails.getMenuFilterType();

		if (null != menuFilterType) {
			if (menuFilterType.length != 0) {

				session.setAttribute("menuFilterType", menuFilterType);
			} else {

				menuFilterType = (String[]) session.getAttribute("menuFilterType");
				loginScreenDetails.setMenuFilterType(menuFilterType);
			}

		}
		String imageName = null;
		final Date date = new Date();
		final String strImageStatus = (String) session.getAttribute("cropImageSource");

		final String returnViewName = loginScreenDetails.getViewName();
		String imageSourceSession = null;
		String previewImageSourceSession = null;
		String bannerimageSourceSession = null;
		if (null != returnViewName) {

			if ("setuploginpage".equals(returnViewName)) {

				imageSourceSession = "loginScreenLogo";
				previewImageSourceSession = "loginScreenLogoPreview";
			} else
				if ("setupaboutusscreen".equals(returnViewName)) {
					imageSourceSession = "aboutusScreenImage";
					previewImageSourceSession = "aboutusScreenImagePreview";
				} else
					if ("setupsplashscreen".equals(returnViewName)) {
						imageSourceSession = "splashImage";
						previewImageSourceSession = "splashImagePreview";
					} else
						if ("generalsettings".equals(returnViewName)) {
							imageSourceSession = "titleBarLogo";
							previewImageSourceSession = "titleBarLogoPreview";

							if (uploadImageType.equals("imageFile")) {
								bannerimageSourceSession = "appiconpreview";
							}
						} else
							if ("menugeneralsettings".equals(returnViewName)) {
								imageSourceSession = "titleBarLogo";
								previewImageSourceSession = "titleBarLogoPreview";
							} else
								if ("setuplistmenu".equals(returnViewName)) {
									imageSourceSession = "menuIconPreview";
									previewImageSourceSession = "menuIconPreview";
									reOrderMenu(loginScreenDetails.getBtnPosition(), "", "", session);
								} else
									if ("iconicmenutemplate".equals(returnViewName)) {
										imageSourceSession = "menuIconPreview";
										previewImageSourceSession = "menuIconPreview";

										if ("imageFile".equals(uploadImageType)) {
											bannerimageSourceSession = "iconicTempbnrimgPreview";
										}

										reOrderMenu(loginScreenDetails.getBtnPosition(), "", "", session);

									} else
										if ("twocoltabview".equals(returnViewName)) {
											imageSourceSession = "bannerimage";
											previewImageSourceSession = "menuIconPreview";
											reOrderMenu(loginScreenDetails.getBtnPosition(), "", "", session);
										} else
											if ("setupanythingpage".equals(returnViewName)) {
												imageSourceSession = "anythingScreenImage";
												previewImageSourceSession = "anythingScreenImage";
											} else
												if ("editanythingpage".equals(returnViewName)) {
													imageSourceSession = "editAnythingScreenImage";
													previewImageSourceSession = "editAnythingScreenImage";
												} else
													if ("tabBarSetup".equals(returnViewName)) {
														imageSourceSession = "tabBarIconPreview";
														previewImageSourceSession = "tabBarIconPreview";

														if ("imageFile".equals(uploadImageType)) {
															bannerimageSourceSession = "imageFileTabBarIconPreview";
														}
													} else
														if ("makeYourOwnAnythingPage".equals(returnViewName)) {
															imageSourceSession = "makeAnythngImage";
															previewImageSourceSession = "makeAnythngImagePreview";
														} else
															if ("edityourownanythingpage".equals(returnViewName)) {
																imageSourceSession = "makeAnythngImage";
																previewImageSourceSession = "makeAnythngImagePreview";
															} else
																if ("addFilters".equals(returnViewName)) {
																	imageSourceSession = "filterImage";
																	previewImageSourceSession = "filterImagePreview";
																} else
																	if ("userSettings".equals(returnViewName)) {
																		imageSourceSession = "userSettingImg";
																		previewImageSourceSession = "userSettingImgPrev";
																	} else
																		if ("setupcombomenu".equals(returnViewName)) {
																			imageSourceSession = "setupcombomenuImgCircle";
																			previewImageSourceSession = "setupcombomenuImgCirclePrev";
																			if ("imageFile".equals(uploadImageType)) {
																				bannerimageSourceSession = "setupcombomenuImgSquare";
																			}
																			reOrderMenu(loginScreenDetails.getBtnPosition(),
																					loginScreenDetails.getGrpBtnType(),
																					loginScreenDetails.getGrpBtnTypeId(), session);
																		} else
																			if ("setupgroupmenu".equals(returnViewName)) {
																				imageSourceSession = "menuIconPreview";
																				previewImageSourceSession = "menuIconPreview";
																			}
			
		}

		if (null != strImageStatus) {
			imageSource = outputFileName;

			final int dotIndex = strImageStatus.lastIndexOf('.');
			if (dotIndex == -1) {
				session.setAttribute(imageSourceSession, ApplicationConstants.UPLOADIMAGEPATH);

			} else {

				imageName = strImageStatus.substring(0, strImageStatus.lastIndexOf("/") + 1) + imageSource + "?" + date.getTime();

				if ("logoImage".equals(uploadImageType)) {
					loginScreenDetails.setLogoImageName(imageSource);
					session.setAttribute(imageSourceSession, imageName);
					session.setAttribute(previewImageSourceSession, imageName);
				} else
					if ("imageFile".equals(uploadImageType)) {
						loginScreenDetails.setBannerImageName(imageSource);
						session.setAttribute(bannerimageSourceSession, imageName);

						if ("iconicmenutemplate".equals(returnViewName)) {
							// Minimum crop height and width
							session.setAttribute("minCropHt", 60);
							session.setAttribute("minCropWd", 60);
						} else
							if ("generalsettings".equals(returnViewName)) {
								// Minimum crop height and width
								session.setAttribute("minCropHt", 57);
								session.setAttribute("minCropWd", 57);
							} else
								if ("setupcombomenu".equals(returnViewName)) {
									// Minimum crop height and width
									session.setAttribute("minCropHt", 40);
									session.setAttribute("minCropWd", 40);
								}

					}

			}

		} else {
			loginScreenDetails.setLogoImageName(null);
			session.setAttribute("loginScreenLogo", ApplicationConstants.UPLOADIMAGEPATH);
			LOG.info("Problem Occured when Retailer Logo upload....");
		}

		if (null != loginScreenDetails.getLowerLimit()) {
			request.setAttribute("lowerLimit", loginScreenDetails.getLowerLimit());

		}
		if (null != loginScreenDetails.getHiddenRetailLocs()) {
			session.setAttribute("selectedRetLocId", loginScreenDetails.getHiddenRetailLocs());
		}

		/* Code for re populating for values */
		loginScreenDetails.setHiddenmenuFnctn(loginScreenDetails.getMenuFucntionality());
		loginScreenDetails.setHiddenFindCategory(loginScreenDetails.getBtnLinkId());

		loginScreenDetails.setHiddenBtnLinkId(loginScreenDetails.getBtnLinkId());
		loginScreenDetails.setBtnLinkId(null);
		loginScreenDetails.setHiddenCitiId(loginScreenDetails.getCitiId());
		loginScreenDetails.setCitiId(null);
		String buttomBtn = (String) session.getAttribute("buttomBtn");
		if ("Exist".equalsIgnoreCase(buttomBtn)) {
			loginScreenDetails.setBottomBtnId((Integer) session.getAttribute("bottomBtnId"));
		}

		model.put("screenSettingsForm", loginScreenDetails);
		return returnViewName;
	}

	/**
	 * This method saves the cropped image.
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param imageFile
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String saveCroppedImage(HttpServletRequest request, HttpServletResponse response, HttpSession session, CommonsMultipartFile imageFile)
			throws HubCitiServiceException {
		LOG.info("Inside saveCroppedImage");
		final ServletContext servletContext = request.getSession().getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		String outputFileName;
		final String status = "FALSE";
		final double parseX = Double.parseDouble(request.getParameter("x"));
		final double parseY = Double.parseDouble(request.getParameter("y"));
		final double parseW = Double.parseDouble(request.getParameter("w"));
		final double parseH = Double.parseDouble(request.getParameter("h"));
		final int xPixel = (int) parseX;
		final int yPixel = (int) parseY;
		final int wPixel = (int) parseW;
		final int hPixel = (int) parseH;

		try {

			@SuppressWarnings("deprecation")
			final String filePathImages = request.getRealPath("images");
			outputFileName = processRetailerCroppedImage(imageFile, filePathImages, xPixel, yPixel, wPixel, hPixel);

		} catch (HubCitiServiceException e) {
			LOG.error("Exception occurred in  UploadCroppedLogoController:::::" + e.getMessage());
			throw new HubCitiServiceException(e);
		}
		return outputFileName;
	}

	/**
	 * This will check the height and width of cropped image.
	 * 
	 * @param imageInfo
	 * @param realPath
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 * @throws HubCitiServiceException
	 */
	public String processRetailerCroppedImage(CommonsMultipartFile imageInfo, String realPath, int x, int y, int w, int h)
			throws HubCitiServiceException {

		final String response = ApplicationConstants.SUCCESS;
		final String fileSeparator = System.getProperty("file.separator");
		final Random rand = new Random();
		int numNoRange = rand.nextInt(100000);
		String outputFileName;
		String sourceFileName;
		try {
			final StringBuilder mediaPathBuilder = Utility.getTempMediaPath(ApplicationConstants.TEMP);
			final String imgMediaPath = mediaPathBuilder.toString();
			// String imgMediaPath = realPath + fileSeparator + "flow_old";
			outputFileName = imageInfo.getOriginalFilename();
			sourceFileName = imageInfo.getOriginalFilename();
			if (!Utility.isEmptyOrNullString(outputFileName)) {
				outputFileName = FilenameUtils.removeExtension(imageInfo.getOriginalFilename());
				sourceFileName = FilenameUtils.removeExtension(imageInfo.getOriginalFilename());
				
				/* For Security Fix*/
				outputFileName = outputFileName.replaceAll("'", "-")
						.replaceAll("\"", "-").replaceAll("[()]", "BR")
						.replaceAll("<", "LE").replaceAll(">", "GT")
						.replaceAll("=", "EQ");
				
				outputFileName = outputFileName + "_" + String.valueOf(numNoRange) + ".png";
				sourceFileName = sourceFileName + ".png";
			}
			LOG.info(" Images path ********************" + imgMediaPath + fileSeparator + outputFileName);
			// Update local Utiliy Code and uncomment below line for cropping
			// feature
			Utility.writeCroppedFileData(imageInfo, imgMediaPath + fileSeparator + sourceFileName, imgMediaPath + fileSeparator + outputFileName, x,
					y, w, h);

		} catch (Exception exception) {
			throw new HubCitiServiceException(exception.getMessage());
		}
		return outputFileName;
	}

	/**
	 * This method is for cropping about us screen image.
	 * 
	 * @param screenDetails
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/uploadaboutusscreenlogo.htm", method = RequestMethod.POST)
	public String uploadCroppedAboutUsImage(@ModelAttribute("screenSettingsForm") ScreenSettings screenDetails, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model) throws HubCitiServiceException {

		String imageSource = null;
		String outputFileName = null;
		outputFileName = saveCroppedImage(request, response, session, screenDetails.getLogoImage());
		String imageName = null;

		final Date date = new Date();

		final String strImageStatus = (String) session.getAttribute("cropImageSource");

		if (null != strImageStatus) {
			imageSource = outputFileName;
			screenDetails.setLogoImageName(imageSource);
			final int dotIndex = strImageStatus.lastIndexOf('.');
			if (dotIndex == -1) {
				session.setAttribute("aboutusScreenImage", ApplicationConstants.UPLOADIMAGEPATH);

			} else {

				imageName = strImageStatus.substring(0, strImageStatus.lastIndexOf("/") + 1) + imageSource + "?" + date.getTime();
				session.setAttribute("aboutusScreenImage", imageName);
				session.setAttribute("aboutusScreenImagePreview", imageName);

			}

		} else {
			screenDetails.setLogoImageName(null);
			session.setAttribute("aboutusScreenImage", ApplicationConstants.UPLOADIMAGEPATH);
			LOG.info("Problem Occured when Retailer Logo upload....");
		}
		model.put("screenSettingsForm", screenDetails);
		return "setupaboutusscreen";
	}

	/**
	 * This method is for uploading small images.
	 * 
	 * @param screenDetails
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/uploadaboutusscreensmalllogo.htm", method = RequestMethod.POST)
	public String uploadCroppedAboutUsSmallImage(@ModelAttribute("screenSettingsForm") ScreenSettings screenDetails, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model) throws HubCitiServiceException {

		String imageSource = null;
		String outputFileName = null;
		outputFileName = saveCroppedImage(request, response, session, screenDetails.getSmallLogoImage());
		String imageName = null;

		final Date date = new Date();

		final String strImageStatus = (String) session.getAttribute("cropImageSource");

		if (null != strImageStatus) {
			imageSource = outputFileName;
			screenDetails.setSmallLogoImageName(imageSource);
			final int dotIndex = strImageStatus.lastIndexOf('.');
			if (dotIndex == -1) {
				session.setAttribute("smallLogo", ApplicationConstants.UPLOADIMAGEPATH);

			} else {

				imageName = strImageStatus.substring(0, strImageStatus.lastIndexOf("/") + 1) + imageSource + "?" + date.getTime();
				session.setAttribute("smallLogo", imageName);
				session.setAttribute("smallLogoPreview", imageName);

			}

		} else {
			screenDetails.setSmallLogoImageName(null);
			session.setAttribute("smallLogo", ApplicationConstants.UPLOADIMAGEPATH);
			LOG.info("Problem Occured when Retailer Logo upload....");
		}
		model.put("screenSettingsForm", screenDetails);
		return "setupaboutusscreen";
	}

	/**
	 * This method crops the selected portion of the image.
	 * 
	 * @param loginPageDetails
	 * @param result
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/uploadiconimg.htm", method = RequestMethod.POST)
	public String onSubmitImage2(@ModelAttribute("screenSettingsForm") ScreenSettings loginPageDetails, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception, HubCitiServiceException {
		final String fileSeparator = System.getProperty("file.separator");
		String imageSource = null;
		final boolean imageValidSizeValFlg = false;
		final StringBuffer strResponse = new StringBuffer();
		final Date date = new Date();
		session.removeAttribute("cropImageSource");
		int w = 0;
		int h = 0;
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/xml");
		String imageFileName = null;
		session.removeAttribute("imageCropPage");
		InputStream inputStream = loginPageDetails.getImageFile().getInputStream();
		imageFileName = loginPageDetails.getImageFile().getOriginalFilename();

		@SuppressWarnings("deprecation")
		final BufferedImage img = Utility.getBufferedImageForMinDimension(inputStream, request.getRealPath("images"), imageFileName, "WelcomePage");
		w = img.getWidth(null);
		h = img.getHeight(null);
		session.setAttribute("imageHt", h);
		session.setAttribute("imageWd", w);

		final String tempImgPath = Utility.getTempMediaPath(ApplicationConstants.TEMP).toString();
		// session.removeAttribute("welcomePageBtnVal");
		imageSource = imageFileName;
		imageSource = FilenameUtils.removeExtension(imageSource);
		imageSource = imageSource + ".png" + "?" + date.getTime();
		final String filePath = tempImgPath + fileSeparator + imageFileName;
		// Utility.writeFileData(objRetLocAds.getBannerAdImagePath(),
		// filePath);
		Utility.writeImage(img, filePath);
		if (imageValidSizeValFlg) {
			strResponse.append("ValidImageDimention");
			strResponse.append("|" + imageFileName);

			session.setAttribute("welcomePageCreateImgPath", "/" + ApplicationConstants.IMAGES + "/" + ApplicationConstants.TEMPFOLDER + "/"
					+ imageSource);

		}
		strResponse.append("|" + "/" + ApplicationConstants.IMAGES + "/" + ApplicationConstants.TEMPFOLDER + "/" + imageSource);
		response.getWriter().write("<imageScr>" + strResponse.toString() + "</imageScr>");

		// Used for image cropping popup
		session.setAttribute("cropImageSource", "/" + ApplicationConstants.IMAGES + "/" + ApplicationConstants.TEMPFOLDER + "/" + imageSource);

		// }

		return null;
	}

	/**
	 * This method crops the selected portion of the image.
	 * 
	 * @param loginPageDetails
	 * @param result
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/uploadevntimg.htm", method = RequestMethod.POST)
	public String onSubmitEventImage(@ModelAttribute("screenSettingsForm") Event eventDetails, BindingResult result, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception, HubCitiServiceException {
		final String fileSeparator = System.getProperty("file.separator");
		String imageSource = null;

		final boolean imageValidSizeValFlg = false;
		final StringBuffer strResponse = new StringBuffer();
		final Date date = new Date();
		session.removeAttribute("cropImageSource");
		int w = 0;
		int h = 0;
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/xml");
		String imageFileName = null;
		InputStream inputStream = null;
		boolean imageSizeValFlg = false;
		if(eventDetails.getImgUploadFor() != null && "logistics".equals(eventDetails.getImgUploadFor()))	{
			inputStream = eventDetails.getLogisticsImgFile().getInputStream();
			imageFileName = eventDetails.getLogisticsImgFile().getOriginalFilename();
		} else	{
			inputStream = eventDetails.getEventImageFile().getInputStream();
			imageFileName = eventDetails.getEventImageFile().getOriginalFilename();
		}

		eventDetails.setHiddenCategory(eventDetails.getEventCategory());
		eventDetails.setHiddenState(eventDetails.getState());
		//eventDetails.setEventTiedIds(eventDetails.getEventTiedIds());
		imageSizeValFlg = Utility.validMinDimension(ApplicationConstants.CROPIMAGEHEIGHT, ApplicationConstants.CROPIMAGEWIDTH, inputStream);
		if (null != inputStream) {

			inputStream.close();
		}
		if (imageSizeValFlg) {

			response.getWriter().write("<imageScr>" + "maxSizeImageError" + "</imageScr>");
			return null;
		} else {
			if(eventDetails.getImgUploadFor() != null && "logistics".equalsIgnoreCase(eventDetails.getImgUploadFor()) )	{
				inputStream = eventDetails.getLogisticsImgFile().getInputStream();
			} else	{
				inputStream = eventDetails.getEventImageFile().getInputStream();
			}
			@SuppressWarnings("deprecation")
			final BufferedImage img = Utility.getBufferedImageForMinDimension(inputStream, request.getRealPath("images"), imageFileName,
					"WelcomePage");
			w = img.getWidth(null);
			h = img.getHeight(null);
			session.setAttribute("imageHt", h);
			session.setAttribute("imageWd", w);

			final String tempImgPath = Utility.getTempMediaPath(ApplicationConstants.TEMP).toString();
			// session.removeAttribute("welcomePageBtnVal");
			imageSource = imageFileName;
			imageSource = FilenameUtils.removeExtension(imageSource);
			imageSource = imageSource + ".png" + "?" + date.getTime();
			final String filePath = tempImgPath + fileSeparator + imageFileName;
			// Utility.writeFileData(objRetLocAds.getBannerAdImagePath(),
			// filePath);
			Utility.writeImage(img, filePath);
			if (imageValidSizeValFlg) {
				strResponse.append("ValidImageDimention");
				strResponse.append("|" + imageFileName);

				session.setAttribute("welcomePageCreateImgPath", "/" + ApplicationConstants.IMAGES + "/" + ApplicationConstants.TEMPFOLDER + "/"
						+ imageSource);

			}
			strResponse.append("|" + "/" + ApplicationConstants.IMAGES + "/" + ApplicationConstants.TEMPFOLDER + "/" + imageSource);
			response.getWriter().write("<imageScr>" + strResponse.toString() + "</imageScr>");

			// Used for image cropping popup
			session.setAttribute("cropImageSource", "/" + ApplicationConstants.IMAGES + "/" + ApplicationConstants.TEMPFOLDER + "/" + imageSource);

			// }
			if (null != inputStream) {

				inputStream.close();
			}
			return null;
		}

	}

	/**
	 * This method displays or uploads the cropped image.
	 * 
	 * @param loginScreenDetails
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @return
	 * @throws HubCitiServiceException
	 */
	@RequestMapping(value = "/uploadcroppedevntimage.htm", method = RequestMethod.POST)
	public String uploadCroppedEventImage(@ModelAttribute("screenSettingsForm") Event eventDetails, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model) throws HubCitiServiceException {

		String imageSource = null;
		String outputFileName = null;

		if(eventDetails.getImgUploadFor() != null && "logistics".equals(eventDetails.getImgUploadFor()))	{
			outputFileName = saveCroppedImage(request, response, session, eventDetails.getLogisticsImgFile());
			eventDetails.setShowLogisticsTab(true);
			eventDetails.setShowEventPacgTab(false);
			eventDetails.setIsNewLogisticsImg(true);
		} else	{
			outputFileName = saveCroppedImage(request, response, session, eventDetails.getEventImageFile());
			eventDetails.setShowLogisticsTab(false);
			eventDetails.setShowEventPacgTab(false);
		}

		String imageName = null;
		final Date date = new Date();
		final String strImageStatus = (String) session.getAttribute("cropImageSource");

		final String returnViewName = eventDetails.getViewName();
		String imageSourceSession = null;
		
		if("addupdatefundraiserevent".equalsIgnoreCase(returnViewName)) {
			if("Yes".equalsIgnoreCase(eventDetails.getIsEventTied()) && null != eventDetails.getEventTiedIds()) {
				request.setAttribute("eventTiedIds", eventDetails.getEventTiedIds());
			}
			request.setAttribute("lowerLimit", eventDetails.getLowerLimit());
			request.setAttribute("eventSearchKey", eventDetails.getEventSearchKey());
		}

		if (null != returnViewName) {
			if(eventDetails.getImgUploadFor() != null && "logistics".equals(eventDetails.getImgUploadFor()))	{
				imageSourceSession = "logisticsImgPreview";
			}else if(returnViewName.equals("addEvtMarker")){
				request.setAttribute("btnName", eventDetails.getMarkerBtnName());
				
				
				imageSourceSession = "evtMarkerImagePreview";
			}		
			
			else	{
				imageSourceSession = "eventImagePreview";
			}
		}

		if (null != strImageStatus) {
			imageSource = outputFileName;

			final int dotIndex = strImageStatus.lastIndexOf('.');
			if (dotIndex == -1) {
				session.setAttribute(imageSourceSession, ApplicationConstants.UPLOADIMAGEPATH);

			} else {

				imageName = strImageStatus.substring(0, strImageStatus.lastIndexOf("/") + 1) + imageSource + "?" + date.getTime();
				
				if(eventDetails.getImgUploadFor() != null && "logistics".equals(eventDetails.getImgUploadFor()))	{
					eventDetails.setLogisticsImgName(imageSource);
				} else	{
					eventDetails.setEventImageName(imageSource);
				}
				session.setAttribute(imageSourceSession, imageName);
			}

		} else {
			eventDetails.setEventImageName(null);
			session.setAttribute("loginScreenLogo", ApplicationConstants.UPLOADIMAGEPATH);
			LOG.info("Problem Occured when Retailer Logo upload....");
		}
		
		if(returnViewName.equals("addEvtMarker")){
			
			model.put("screenSettingsForm", eventDetails);
		}

		model.put("eventSetUpForm", eventDetails);
		return returnViewName;
	}

	/**
	 * This ModelAttribute sort Deal start and end minutes property.
	 * 
	 * @return sortedMap.
	 * @throws ScanSeeServiceException
	 *             will be thrown.
	 */
	@SuppressWarnings("rawtypes")
	@ModelAttribute("StartMinutes")
	public Map<String, String> populatemapDealStartMins() throws HubCitiServiceException {
		final HashMap<String, String> mapDealStartHrs = new HashMap<String, String>();
		for (int i = 0; i <= 55; i++) {
			if (i < 10) {
				mapDealStartHrs.put(ApplicationConstants.ZERO + i, ApplicationConstants.ZERO + i);
				i = i + 4;
			} else {
				mapDealStartHrs.put(String.valueOf(i), String.valueOf(i));
				i = i + 4;
			}
		}
		@SuppressWarnings("unused")
		final Iterator iterator = mapDealStartHrs.entrySet().iterator();
		@SuppressWarnings("unchecked")
		final Map<String, String> sortedMap = Utility.sortByComparator(mapDealStartHrs);
		return sortedMap;
	}

	/**
	 * This ModelAttribute sort Deal start and end hours property.
	 * 
	 * @return sortedMap.
	 * @throws ScanSeeServiceException
	 *             will be thrown.
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	@ModelAttribute("StartHours")
	public Map<String, String> populateDealStartHrs() throws HubCitiServiceException {
		final HashMap<String, String> mapDealStartHrs = new HashMap<String, String>();
		for (int i = 0; i < 24; i++) {
			if (i < 10) {
				mapDealStartHrs.put(ApplicationConstants.ZERO + i, ApplicationConstants.ZERO + i);
			} else {
				mapDealStartHrs.put(String.valueOf(i), String.valueOf(i));
			}
		}
		final Iterator iterator = mapDealStartHrs.entrySet().iterator();
		@SuppressWarnings("unchecked")
		final Map<String, String> sortedMap = Utility.sortByComparator(mapDealStartHrs);
		return sortedMap;
	}

	public void reOrderMenu(String btnOrder, String grpBtnType, String grpBtnTypeId, HttpSession session) {

		List<ScreenSettings> previewMenuItems = (ArrayList<ScreenSettings>) session.getAttribute("previewMenuItems");
		String btnOrderArry[] = null;
		String grpBtnTypeArry[] = null;
		String grpBtnTypeIdArry[] = null;
		List<ScreenSettings> sortedMenuItems = new ArrayList<ScreenSettings>();
		// String btnOrder = loginScreenDetails.getBtnPosition();
		if (!"".equals(Utility.checkNull(btnOrder))) {
			btnOrderArry = btnOrder.split("~");
			for (int j = 0; j < btnOrderArry.length; j++) {
				for (int i = 0; i < previewMenuItems.size(); i++) {
					ScreenSettings btn = previewMenuItems.get(i);
					if (btn.getMenuIconId().equals(btnOrderArry[j])) {
						sortedMenuItems.add(btn);
						previewMenuItems.remove(i);
						break;
					}

				}
			}

			if (!"".equals(Utility.checkNull(grpBtnType))) {
				grpBtnTypeArry = grpBtnType.split("~");
				grpBtnTypeIdArry = grpBtnTypeId.split("~");

				for (int i = 0; i < sortedMenuItems.size(); i++) {
					ScreenSettings btn = sortedMenuItems.get(i);
					btn.setComboBtnType(grpBtnTypeArry[i]);
					if (!"null".equals(grpBtnTypeIdArry[i])) {
						btn.setComboBtnTypeId(Integer.parseInt(grpBtnTypeIdArry[i]));
					} else {
						btn.setComboBtnTypeId(null);
					}
				}
			}
			session.setAttribute("previewMenuItems", sortedMenuItems);
		}
	}
	
	@RequestMapping(value = "/uploadevntmarkerimg.htm", method = RequestMethod.POST)
	public String uploadCroppedEventMarkerImage(@ModelAttribute("AddEventMarker") Event eventDetails, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model) throws HubCitiServiceException {

		String imageSource = null;
		String outputFileName = null;

	
			outputFileName = saveCroppedImage(request, response, session, eventDetails.getEvtMarkerImgFile());
		
		

		String imageName = null;
		final Date date = new Date();
		final String strImageStatus = (String) session.getAttribute("cropImageSource");

		final String returnViewName = "addEvtMarker";
		String imageSourceSession = null;
	

		imageSourceSession ="evtMarkerImgPath";

		if (null != strImageStatus) {
			imageSource = outputFileName;

			final int dotIndex = strImageStatus.lastIndexOf('.');
			if (dotIndex == -1) {
				session.setAttribute(imageSourceSession, ApplicationConstants.UPLOADIMAGEPATH);

			} else {

				imageName = strImageStatus.substring(0, strImageStatus.lastIndexOf("/") + 1) + imageSource + "?" + date.getTime();
						
					eventDetails.setEvtMarkerImgPath(imageSource);
				
				session.setAttribute(imageSourceSession, imageName);
			}

		} else {
			eventDetails.setEvtMarkerImgPath(null);
		
			LOG.info("Problem Occured when Retailer Logo upload....");
		}

		model.put("AddEventMarker", eventDetails);
		return returnViewName;
	}
}
