/**
* OpenCPS is the open source Core Public Services software
* Copyright (C) 2016-present OpenCPS community

* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Affero General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Affero General Public License for more details.
* You should have received a copy of the GNU Affero General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>
*/

package org.opencps.dossiermgt.portlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.Citizen;
import org.opencps.accountmgt.search.BusinessDisplayTerms;
import org.opencps.accountmgt.service.BusinessLocalServiceUtil;
import org.opencps.accountmgt.service.CitizenLocalServiceUtil;
import org.opencps.datamgt.model.DictCollection;
import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.service.DictCollectionLocalServiceUtil;
import org.opencps.datamgt.service.DictItemLocalServiceUtil;
import org.opencps.dossiermgt.CreateDossierFolderException;
import org.opencps.dossiermgt.EmptyDossierAddressException;
import org.opencps.dossiermgt.EmptyDossierCityCodeException;
import org.opencps.dossiermgt.EmptyDossierContactNameException;
import org.opencps.dossiermgt.EmptyDossierDistrictCodeException;
import org.opencps.dossiermgt.EmptyDossierFileException;
import org.opencps.dossiermgt.EmptyDossierSubjectIdException;
import org.opencps.dossiermgt.EmptyDossierSubjectNameException;
import org.opencps.dossiermgt.EmptyDossierWardCodeException;
import org.opencps.dossiermgt.InvalidDossierObjectException;
import org.opencps.dossiermgt.OutOfLengthDossierAddressException;
import org.opencps.dossiermgt.OutOfLengthDossierContactEmailException;
import org.opencps.dossiermgt.OutOfLengthDossierContactNameException;
import org.opencps.dossiermgt.OutOfLengthDossierContactTelNoException;
import org.opencps.dossiermgt.OutOfLengthDossierSubjectIdException;
import org.opencps.dossiermgt.OutOfLengthDossierSubjectNameException;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.DossierTemplate;
import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.dossiermgt.search.DossierDisplayTerms;
import org.opencps.dossiermgt.search.DossierFileDisplayTerms;
import org.opencps.dossiermgt.service.DossierFileLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil;
import org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil;
import org.opencps.servicemgt.model.ServiceInfo;
import org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil;
import org.opencps.util.DLFolderUtil;
import org.opencps.util.MessageKeys;
import org.opencps.util.PortletConstants;
import org.opencps.util.PortletPropsValues;
import org.opencps.util.PortletUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author trungnt
 */
public class DossierMgtFrontOfficePortlet extends MVCPortlet {

	public void addTempFile(
	    ActionRequest actionRequest, ActionResponse actionResponse) {

		UploadPortletRequest uploadPortletRequest = PortalUtil
		    .getUploadPortletRequest(actionRequest);

		long folderId = ParamUtil
		    .getLong(uploadPortletRequest, DossierFileDisplayTerms.FOLDE_ID);

		long dossierPartId = ParamUtil
		    .getLong(
		        uploadPortletRequest, DossierFileDisplayTerms.DOSSIER_PART_ID);

		int index = ParamUtil
		    .getInteger(uploadPortletRequest, DossierFileDisplayTerms.INDEX);
		
		int level = ParamUtil
					    .getInteger(uploadPortletRequest, DossierFileDisplayTerms.LEVEL);

		String groupName = ParamUtil
		    .getString(
		        uploadPortletRequest, DossierFileDisplayTerms.GROUP_NAME);

		String templateFileNo = ParamUtil
		    .getString(
		        uploadPortletRequest, DossierDisplayTerms.TEMPLATE_FILE_NO);

		String fileName = ParamUtil
		    .getString(uploadPortletRequest, DossierFileDisplayTerms.FILE_NAME);
		
		String partType = ParamUtil
					    .getString(uploadPortletRequest, DossierFileDisplayTerms.PART_TYPE);

		String tempFolderName = ParamUtil
		    .getString(
		        uploadPortletRequest, DossierFileDisplayTerms.TEMP_FOLDER_NAME);

		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest
		    .getAttribute(WebKeys.THEME_DISPLAY);

		String redirectURL = ParamUtil
		    .getString(uploadPortletRequest, "redirectURL");

		String displayName = ParamUtil
		    .getString(
		        uploadPortletRequest, DossierFileDisplayTerms.DISPLAY_NAME);

		String dossierFileNo = ParamUtil
		    .getString(
		        uploadPortletRequest, DossierFileDisplayTerms.DOSSIER_FILE_NO);

		String dossierFileDate = ParamUtil
		    .getString(
		        uploadPortletRequest,
		        DossierFileDisplayTerms.DOSSIER_FILE_DATE);

		String sourceFileName = uploadPortletRequest
		    .getFileName(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

		sourceFileName = sourceFileName
		    .concat(PortletConstants.TEMP_RANDOM_SUFFIX).concat(StringUtil
		        .randomString());

		InputStream inputStream = null;

		JSONObject jsonObject = JSONFactoryUtil
		    .createJSONObject();

		try {

			inputStream = uploadPortletRequest
			    .getFileAsStream(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

			String contentType = uploadPortletRequest
			    .getContentType(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

			FileEntry fileEntry = DLAppServiceUtil
			    .addTempFileEntry(themeDisplay
			        .getScopeGroupId(), folderId, sourceFileName,
			        tempFolderName, inputStream, contentType);

			jsonObject
			    .put(DossierFileDisplayTerms.DOSSIER_FILE_NO, dossierFileNo);

			jsonObject
			    .put(DossierFileDisplayTerms.DISPLAY_NAME, displayName);

			jsonObject
			    .put(
			        DossierFileDisplayTerms.DOSSIER_FILE_DATE, dossierFileDate);

			jsonObject
			    .put(DossierFileDisplayTerms.FILE_TITLE, fileEntry
			        .getTitle());

			jsonObject
			    .put(DossierFileDisplayTerms.MIME_TYPE, fileEntry
			        .getMimeType());

			jsonObject
			    .put(DossierFileDisplayTerms.FILE_NAME, fileName);

			jsonObject
			    .put(DossierFileDisplayTerms.FILE_ENTRY_ID, fileEntry
			        .getFileEntryId());
			jsonObject
			    .put(DossierFileDisplayTerms.FOLDE_ID, fileEntry
			        .getFolderId());

			jsonObject
			    .put(DossierFileDisplayTerms.DOSSIER_PART_ID, dossierPartId);

			jsonObject
			    .put(DossierFileDisplayTerms.INDEX, index);
			
			jsonObject
		    	.put(DossierFileDisplayTerms.LEVEL, level);
			
			jsonObject
		    	.put(DossierFileDisplayTerms.PART_TYPE, partType);

			jsonObject
			    .put(DossierFileDisplayTerms.GROUP_NAME, groupName);

			jsonObject
			    .put(
			        DossierFileDisplayTerms.DOSSIER_FILE_ORIGINAL,
			        PortletConstants.DOSSIER_FILE_ORIGINAL);

			jsonObject
			    .put(
			        DossierFileDisplayTerms.DOSSIER_FILE_TYPE,
			        PortletConstants.DOSSIER_FILE_TYPE_INPUT);

			jsonObject
			    .put(DossierDisplayTerms.TEMPLATE_FILE_NO, templateFileNo);

		}
		catch (Exception e) {
			_log
			    .error(e);
			SessionErrors
			    .add(actionRequest, "upload-error");
		}
		finally {
			StreamUtil
			    .cleanUp(inputStream);
			HttpServletRequest request = PortalUtil
			    .getHttpServletRequest(actionRequest);
			request
			    .setAttribute(
			        WebKeys.RESPONSE_UPLOAD_TEMP_DOSSIER_FILE, jsonObject);

			if (Validator
			    .isNotNull(redirectURL)) {
				actionResponse
				    .setRenderParameter(
				        "jspPage",
				        "/html/portlets/dossiermgt/frontoffice/upload_dossier_file.jsp");
			}
		}
	}

	public void createReport(
	    ActionRequest actionRequest, ActionResponse actionResponse) {

	}

	public void deleteDossier(
	    ActionRequest actionRequest, ActionResponse actionResponse) {

		long dossierId = ParamUtil
		    .getLong(actionRequest, DossierDisplayTerms.DOSSIER_ID);
	}

	public void deleteDossierFile(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {

		long dossierFileId = ParamUtil
		    .getLong(actionRequest, DossierFileDisplayTerms.DOSSIER_FILE_ID);
		if (dossierFileId > 0) {
			DossierFile dossierFile = null;
			JSONObject jsonObject = JSONFactoryUtil
			    .createJSONObject();
			try {
				dossierFile = DossierFileLocalServiceUtil
				    .getDossierFile(dossierFileId);
				if (dossierFile != null) {
					long fileEntryId = dossierFile
					    .getFileEntryId();
					try {
						if (fileEntryId > 0) {
							DLAppServiceUtil
							    .deleteFileEntry(fileEntryId);
						}
					}
					catch (Exception e) {
						// nothing to do
					}

					DossierFileLocalServiceUtil
					    .deleteDossierFile(dossierFile);
					jsonObject
					    .put("deleted", Boolean.TRUE);
				}
			}
			catch (Exception e) {
				_log
				    .error(e);

				jsonObject
				    .put("deleted", Boolean.FALSE);

			}

			writeJSON(actionRequest, actionResponse, jsonObject);
		}

	}

	public void deleteTempFile(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {

		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest
		    .getAttribute(WebKeys.THEME_DISPLAY);

		long fileEntryId = ParamUtil
		    .getLong(actionRequest, "fileEntryId");

		JSONObject jsonObject = JSONFactoryUtil
		    .createJSONObject();

		try {
			DLAppServiceUtil
			    .deleteFileEntry(fileEntryId);
			jsonObject
			    .put("deleted", Boolean.TRUE);
		}
		catch (Exception e) {
			String errorMessage = themeDisplay
			    .translate(
			        "an-unexpected-error-occurred-while-deleting-the-file");

			jsonObject
			    .put("deleted", Boolean.FALSE);
			jsonObject
			    .put("errorMessage", errorMessage);
		}

		writeJSON(actionRequest, actionResponse, jsonObject);
	}

	protected void deleteTempFile(
	    long groupId, long folderId, String tempFolderName, String fileName)
	    throws Exception {

		try {
			DLAppServiceUtil
			    .deleteTempFileEntry(
			        groupId, folderId, fileName, tempFolderName);

		}
		catch (Exception e) {
			_log
			    .error(e);
		}
	}

	public void deleteTempFiles(
	    ActionRequest actionRequest, ActionResponse actionResponse) {

		String strFileEntryIds = ParamUtil
		    .getString(actionRequest, "fileEntryIds");

		if (Validator
		    .isNotNull(strFileEntryIds)) {
			long[] fileEntryIds = StringUtil
			    .split(strFileEntryIds, 0L);
			if (fileEntryIds != null) {
				for (int i = 0; i < fileEntryIds.length; i++) {
					try {
						DLAppServiceUtil
						    .deleteFileEntry(fileEntryIds[i]);
					}
					catch (Exception e) {
						continue;
					}
				}
			}
		}

	}

	@Override
	public void render(
	    RenderRequest renderRequest, RenderResponse renderResponse)
	    throws PortletException, IOException {

		long dossierId = ParamUtil
		    .getLong(renderRequest, DossierDisplayTerms.DOSSIER_ID);

		long serviceConfigId = ParamUtil
		    .getLong(renderRequest, DossierDisplayTerms.SERVICE_CONFIG_ID);

		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest
		    .getAttribute(WebKeys.THEME_DISPLAY);

		String accountType = StringPool.BLANK;
		String itemSelected = StringPool.BLANK;

		try {

			if (dossierId > 0) {
				Dossier dossier = DossierLocalServiceUtil
				    .getDossier(dossierId);

				if (dossier != null) {
					renderRequest
					    .setAttribute(WebKeys.DOSSIER_ENTRY, dossier);
					serviceConfigId = dossier
					    .getServiceConfigId();
					try {
						DictCollection dictCollection =
						    DictCollectionLocalServiceUtil
						        .getDictCollection(themeDisplay
						            .getScopeGroupId(),
						            PortletPropsValues.DATAMGT_MASTERDATA_ADMINISTRATIVE_REGION);

						if (dictCollection != null) {
							DictItem city = DictItemLocalServiceUtil
							    .getDictItemInuseByItemCode(dictCollection
							        .getDictCollectionId(), dossier
							            .getCityCode());
							DictItem district = DictItemLocalServiceUtil
							    .getDictItemInuseByItemCode(dictCollection
							        .getDictCollectionId(), dossier
							            .getDistrictCode());
							DictItem ward = DictItemLocalServiceUtil
							    .getDictItemInuseByItemCode(dictCollection
							        .getDictCollectionId(), dossier
							            .getWardCode());

							String[] dictItemIds = new String[3];

							dictItemIds[0] = city != null ? String
							    .valueOf(city
							        .getDictItemId())
							    : StringPool.BLANK;

							dictItemIds[1] = district != null ? String
							    .valueOf(district
							        .getDictItemId())
							    : StringPool.BLANK;

							dictItemIds[2] = ward != null ? String
							    .valueOf(ward
							        .getDictItemId())
							    : StringPool.BLANK;

							itemSelected = StringUtil
							    .merge(dictItemIds);

							renderRequest
							    .setAttribute(
							        WebKeys.DICT_ITEM_SELECTED, itemSelected);
						}
					}
					catch (Exception e) {
						// TODO: handle exception
					}

				}
			}

			if (serviceConfigId > 0) {
				ServiceConfig serviceConfig = ServiceConfigLocalServiceUtil
				    .getServiceConfig(serviceConfigId);

				renderRequest
				    .setAttribute(WebKeys.SERVICE_CONFIG_ENTRY, serviceConfig);

				if (serviceConfig != null && serviceConfig
				    .getServiceInfoId() > 0) {
					ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil
					    .getServiceInfo(serviceConfig
					        .getServiceInfoId());

					renderRequest
					    .setAttribute(WebKeys.SERVICE_INFO_ENTRY, serviceInfo);

					DossierTemplate dossierTemplate =
					    DossierTemplateLocalServiceUtil
					        .getDossierTemplate(serviceConfig
					            .getDossierTemplateId());
					renderRequest
					    .setAttribute(
					        WebKeys.DOSSIER_TEMPLATE_ENTRY, dossierTemplate);
				}
			}

			if (themeDisplay
			    .isSignedIn()) {

				List<UserGroup> userGroups = new ArrayList<UserGroup>();

				User user = themeDisplay
				    .getUser();
				userGroups = user
				    .getUserGroups();

				if (!userGroups
				    .isEmpty()) {
					for (UserGroup userGroup : userGroups) {
						if (userGroup
						    .getName().equals(
						        PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN) ||
						    userGroup
						        .getName().equals(
						            PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS)) {
							accountType = userGroup
							    .getName();
							break;
						}

					}
				}

				renderRequest
				    .setAttribute(WebKeys.ACCOUNT_TYPE, accountType);

				if (accountType
				    .equals(
				        PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN)) {
					Citizen citizen = CitizenLocalServiceUtil
					    .getCitizen(user
					        .getUserId());
					renderRequest
					    .setAttribute(WebKeys.CITIZEN_ENTRY, citizen);
				}
				else if (accountType
				    .equals(
				        PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS)) {

					Business business = BusinessLocalServiceUtil
					    .getBusiness(user
					        .getUserId());
					renderRequest
					    .setAttribute(WebKeys.BUSINESS_ENTRY, business);
				}

			}
		}
		catch (Exception e) {
			_log
			    .error(e);
		}

		super.render(renderRequest, renderResponse);

	}

	public void updateDossier(
	    ActionRequest actionRequest, ActionResponse actionResponse) {

		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest
		    .getAttribute(WebKeys.THEME_DISPLAY);

		Dossier dossier = null;

		long dossierId = ParamUtil
		    .getLong(actionRequest, DossierDisplayTerms.DOSSIER_ID);
		long dossierTemplateId = ParamUtil
		    .getLong(actionRequest, DossierDisplayTerms.DOSSIER_TEMPLATE_ID);
		long serviceInfoId = ParamUtil
		    .getLong(actionRequest, DossierDisplayTerms.SERVICE_INFO_ID);
		long cityId = ParamUtil
		    .getLong(actionRequest, DossierDisplayTerms.CITY_ID);
		long districtId = ParamUtil
		    .getLong(actionRequest, DossierDisplayTerms.DISTRICT_ID);
		long wardId = ParamUtil
		    .getLong(actionRequest, DossierDisplayTerms.WARD_ID);
		long serviceConfigId = ParamUtil
		    .getLong(actionRequest, DossierDisplayTerms.SERVICE_CONFIG_ID);
		long mappingOrganizationId = ParamUtil
		    .getLong(
		        actionRequest,
		        BusinessDisplayTerms.BUSINESS_MAPPINGORGANIZATIONID);
		long govAgencyOrganizationId = ParamUtil
		    .getLong(
		        actionRequest, DossierDisplayTerms.GOVAGENCY_ORGANIZATION_ID);
		int serviceMode = ParamUtil
		    .getInteger(actionRequest, DossierDisplayTerms.SERVICE_MODE);
		String serviceDomainIndex = ParamUtil
		    .getString(actionRequest, DossierDisplayTerms.SERVICE_DOMAIN_INDEX);

		String govAgencyCode = ParamUtil
		    .getString(actionRequest, DossierDisplayTerms.GOVAGENCY_CODE);
		String govAgencyName = ParamUtil
		    .getString(actionRequest, DossierDisplayTerms.GOVAGENCY_NAME);

		String serviceAdministrationIndex = ParamUtil
		    .getString(
		        actionRequest,
		        DossierDisplayTerms.SERVICE_ADMINISTRATION_INDEX);
		String templateFileNo = ParamUtil
		    .getString(actionRequest, DossierDisplayTerms.TEMPLATE_FILE_NO);
		String subjectName = ParamUtil
		    .getString(actionRequest, DossierDisplayTerms.SUBJECT_NAME);
		String subjectId = ParamUtil
		    .getString(actionRequest, DossierDisplayTerms.SUBJECT_ID);
		String address = ParamUtil
		    .getString(actionRequest, DossierDisplayTerms.ADDRESS);
		String contactName = ParamUtil
		    .getString(actionRequest, DossierDisplayTerms.CONTACT_NAME);
		String contactTelNo = ParamUtil
		    .getString(actionRequest, DossierDisplayTerms.CONTACT_TEL_NO);
		String contactEmail = ParamUtil
		    .getString(actionRequest, DossierDisplayTerms.CONTACT_EMAIL);
		String note = ParamUtil
		    .getString(actionRequest, DossierDisplayTerms.NOTE);
		String accountType = ParamUtil
		    .getString(
		        actionRequest, DossierDisplayTerms.ACCOUNT_TYPE,
		        PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN);
		String returnURL = ParamUtil
		    .getString(actionRequest, "returnURL");
		String redirectURL = ParamUtil
		    .getString(actionRequest, "redirectURL");

		String[][] uploadDataSchemas = new String[][] {
		    ParamUtil
		        .getParameterValues(actionRequest, "uploadDataSchema")
		};
		try {
			ServiceContext serviceContext = ServiceContextFactory
			    .getInstance(actionRequest);

			if (dossierId > 0) {
				dossier = DossierLocalServiceUtil
				    .getDossier(dossierId);
			}

			long userId = serviceContext
			    .getUserId();

			String dossierDestinationFolder = StringPool.BLANK;

			if (accountType
			    .equals(PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN)) {
				dossierDestinationFolder = PortletUtil
				    .getCitizenDossierDestinationFolder(serviceContext
				        .getScopeGroupId(), userId);
			}
			else if (accountType
			    .equals(PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS) &&
			    mappingOrganizationId > 0) {
				dossierDestinationFolder = PortletUtil
				    .getBusinessDossierDestinationFolder(serviceContext
				        .getScopeGroupId(), mappingOrganizationId);
			}

			if (dossier != null) {
				dossierDestinationFolder += StringPool.SLASH + dossier
				    .getCounter();
			}

			validateDossier(
			    cityId, districtId, wardId, accountType,
			    dossierDestinationFolder, subjectName, subjectId, address,
			    contactName, contactTelNo, contactEmail, uploadDataSchemas);

			String cityCode = StringPool.BLANK;
			String districtCode = StringPool.BLANK;
			String wardCode = StringPool.BLANK;

			String cityName = StringPool.BLANK;
			String districtName = StringPool.BLANK;
			String wardName = StringPool.BLANK;

			DictItem city = DictItemLocalServiceUtil
			    .getDictItem(cityId);
			DictItem district = DictItemLocalServiceUtil
			    .getDictItem(districtId);
			DictItem ward = DictItemLocalServiceUtil
			    .getDictItem(wardId);

			if (city != null) {
				cityCode = city
				    .getItemCode();
				cityName = city
				    .getItemName(themeDisplay
				        .getLocale());
			}

			if (district != null) {
				districtCode = district
				    .getItemCode();
				districtName = district
				    .getItemName(themeDisplay
				        .getLocale());
			}

			if (ward != null) {
				wardCode = ward
				    .getItemCode();
				wardName = ward
				    .getItemName(themeDisplay
				        .getLocale());
			}

			List<String> normalDossierSchemas = new ArrayList<String>();
			HashMap<String, List<String>> groupPrivateDossier =
			    new LinkedHashMap<String, List<String>>();
			if (uploadDataSchemas != null && uploadDataSchemas.length > 0) {
				for (int i = 0; i < uploadDataSchemas.length; i++) {
					System.out
					    .println(StringUtil
					        .merge(uploadDataSchemas[i]));

					if (uploadDataSchemas[i].length > 0) {

						for (int j = 0; j < uploadDataSchemas[i].length; j++) {
							String uploadDataSchema = uploadDataSchemas[i][j];
							if (Validator
							    .isNotNull(uploadDataSchema)) {
								JSONObject jsonObject = JSONFactoryUtil
								    .createJSONObject(uploadDataSchema);
								String groupName = jsonObject
								    .getString(
								        DossierFileDisplayTerms.GROUP_NAME);
								if (Validator
								    .isNotNull(groupName)) {
									if (groupPrivateDossier
									    .containsKey(groupName)) {
										List<String> dossierSchemas =
										    groupPrivateDossier
										        .get(groupName);
										dossierSchemas
										    .add(uploadDataSchema);
										groupPrivateDossier
										    .put(groupName, dossierSchemas);
									}
									else {
										List<String> dossierSchemas =
										    new ArrayList<String>();
										dossierSchemas
										    .add(uploadDataSchema);
										groupPrivateDossier
										    .put(groupName, dossierSchemas);
									}
								}
								else {
									normalDossierSchemas
									    .add(uploadDataSchema);
								}
							}

						}
					}

				}
			}

			DLFolder dossierFolder = DLFolderUtil
			    .getTargetFolder(userId, serviceContext
			        .getScopeGroupId(), serviceContext
			            .getScopeGroupId(),
			        false, 0, dossierDestinationFolder, StringPool.BLANK, false,
			        serviceContext);

			if (dossierId == 0) {
				DossierLocalServiceUtil
				    .addDossier(
				        userId, mappingOrganizationId, dossierTemplateId,
				        templateFileNo, serviceConfigId, serviceInfoId,
				        serviceDomainIndex, govAgencyOrganizationId,
				        govAgencyCode, govAgencyName, serviceMode,
				        serviceAdministrationIndex, cityCode, cityName,
				        districtCode, districtName, wardName, wardCode,
				        subjectName, subjectId, address, contactName,
				        contactTelNo, contactEmail, note,
				        PortletConstants.DOSSIER_SOURCE_DIRECT,
				        PortletConstants.DOSSIER_STATUS_NEW,
				        normalDossierSchemas, groupPrivateDossier, dossierFolder
				            .getFolderId(),
				        serviceContext);

			}
			else {
				DossierLocalServiceUtil
				    .updateDossier(
				        dossierId, userId, mappingOrganizationId,
				        dossierTemplateId, templateFileNo, serviceConfigId,
				        serviceInfoId, serviceDomainIndex,
				        govAgencyOrganizationId, govAgencyCode, govAgencyName,
				        serviceMode, serviceAdministrationIndex, cityCode,
				        cityName, districtCode, districtName, wardName,
				        wardCode, subjectName, subjectId, address, contactName,
				        contactTelNo, contactEmail, note, normalDossierSchemas,
				        groupPrivateDossier, dossierFolder
				            .getFolderId(),
				        serviceContext);
			}

			SessionMessages
			    .add(actionRequest, MessageKeys.DOSSIER_UPDATE_SUCCESS);
			if (Validator
			    .isNotNull(redirectURL)) {
				actionResponse
				    .sendRedirect(redirectURL);
			}

		}
		catch (Exception e) {
			if (e instanceof EmptyDossierCityCodeException ||
			    e instanceof EmptyDossierDistrictCodeException ||
			    e instanceof EmptyDossierWardCodeException ||
			    e instanceof InvalidDossierObjectException ||
			    e instanceof CreateDossierFolderException ||
			    e instanceof EmptyDossierSubjectNameException ||
			    e instanceof OutOfLengthDossierSubjectNameException ||
			    e instanceof EmptyDossierSubjectIdException ||
			    e instanceof OutOfLengthDossierSubjectIdException ||
			    e instanceof EmptyDossierAddressException ||
			    e instanceof OutOfLengthDossierContactEmailException ||
			    e instanceof OutOfLengthDossierContactNameException ||
			    e instanceof OutOfLengthDossierContactTelNoException ||
			    e instanceof EmptyDossierContactNameException ||
			    e instanceof OutOfLengthDossierAddressException ||
			    e instanceof EmptyDossierFileException) {

				SessionErrors
				    .add(actionRequest, e
				        .getClass());
			}
			else {
				SessionErrors
				    .add(
				        actionRequest,
				        MessageKeys.DOSSIER_SYSTEM_EXCEPTION_OCCURRED);
			}

			_log
			    .error(e);

			actionResponse
			    .setRenderParameter(
			        "mvcPath",
			        "/html/portlets/dossiermgt/frontoffice/edit_dossier.jsp");
			actionResponse
			    .setRenderParameter("backURL", returnURL);

			actionResponse
			    .setRenderParameter(
			        DossierDisplayTerms.SERVICE_CONFIG_ID, String
			            .valueOf(serviceConfigId));

		}
	}

	public void updateDossierFile(
	    ActionRequest actionRequest, ActionResponse actionResponse) {

		UploadPortletRequest uploadPortletRequest = PortalUtil
		    .getUploadPortletRequest(actionRequest);

		long dossierFileId = ParamUtil
		    .getLong(
		        uploadPortletRequest, DossierFileDisplayTerms.DOSSIER_FILE_ID);

		long dossierPartId = ParamUtil
		    .getLong(
		        uploadPortletRequest, DossierFileDisplayTerms.DOSSIER_PART_ID);

		int index = ParamUtil
		    .getInteger(uploadPortletRequest, DossierFileDisplayTerms.INDEX);

		String groupName = ParamUtil
		    .getString(
		        uploadPortletRequest, DossierFileDisplayTerms.GROUP_NAME);

		String templateFileNo = ParamUtil
		    .getString(
		        uploadPortletRequest, DossierDisplayTerms.TEMPLATE_FILE_NO);

		String fileName = ParamUtil
		    .getString(uploadPortletRequest, DossierFileDisplayTerms.FILE_NAME);

		String redirectURL = ParamUtil
		    .getString(uploadPortletRequest, "redirectURL");

		String displayName = ParamUtil
		    .getString(
		        uploadPortletRequest, DossierFileDisplayTerms.DISPLAY_NAME);

		String dossierFileNo = ParamUtil
		    .getString(
		        uploadPortletRequest, DossierFileDisplayTerms.DOSSIER_FILE_NO);

		String dossierFileDate = ParamUtil
		    .getString(
		        uploadPortletRequest,
		        DossierFileDisplayTerms.DOSSIER_FILE_DATE);

		String sourceFileName = uploadPortletRequest
		    .getFileName(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

		sourceFileName = sourceFileName
		    .concat(PortletConstants.TEMP_RANDOM_SUFFIX).concat(StringUtil
		        .randomString());

		String accountType = ParamUtil
		    .getString(uploadPortletRequest, WebKeys.ACCOUNT_TYPE);

		InputStream inputStream = null;

		JSONObject jsonObject = JSONFactoryUtil
		    .createJSONObject();

		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest
		    .getAttribute(WebKeys.THEME_DISPLAY);

		try {
			ServiceContext serviceContext = ServiceContextFactory
			    .getInstance(uploadPortletRequest);
			DossierFile dossierFile = DossierFileLocalServiceUtil
			    .getDossierFile(dossierFileId);

			long storeFolderId = 0;

			if (dossierFile != null) {
				long fileEntryId = dossierFile
				    .getFileEntryId();
				if (fileEntryId > 0) {
					FileEntry fileEntry = DLAppServiceUtil
					    .getFileEntry(fileEntryId);
					storeFolderId = fileEntry
					    .getFolderId();
				}
				else {
					long dossierId = dossierFile
					    .getDossierId();
					Dossier dossier = DossierLocalServiceUtil
					    .getDossier(dossierId);

					int dossierNo = dossier
					    .getCounter();

					String destination = StringPool.BLANK;
					if (accountType
					    .equals(
					        PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN)) {
						destination = PortletUtil
						    .getCitizenDossierDestinationFolder(dossier
						        .getGroupId(), themeDisplay
						            .getUserId()) +
						    StringPool.SLASH + String
						        .valueOf(dossierNo);

					}
					else if (accountType
					    .equals(
					        PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS)) {

						destination = PortletUtil
						    .getBusinessDossierDestinationFolder(dossier
						        .getGroupId(), dossier
						            .getOwnerOrganizationId()) +
						    StringPool.SLASH + String
						        .valueOf(dossierNo);

					}

					DLFolder storeFolder = DLFolderUtil
					    .getTargetFolder(themeDisplay
					        .getUserId(), themeDisplay
					            .getScopeGroupId(),
					        themeDisplay
					            .getScopeGroupId(),
					        false, 0, destination, StringPool.BLANK, false,
					        serviceContext);

					storeFolderId = storeFolder
					    .getFolderId();
				}
			}

			inputStream = uploadPortletRequest
			    .getFileAsStream(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

			long size = uploadPortletRequest
			    .getSize(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

			String contentType = uploadPortletRequest
			    .getContentType(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

			String mimeType = Validator
			    .isNotNull(contentType) ? MimeTypesUtil
			        .getContentType(contentType) : StringPool.BLANK;

			FileEntry fileEntry = DLAppServiceUtil
			    .addFileEntry(serviceContext
			        .getScopeGroupId(), storeFolderId, sourceFileName, mimeType,
			        displayName, StringPool.BLANK, StringPool.BLANK,
			        inputStream, size, serviceContext);

			jsonObject
			    .put(DossierFileDisplayTerms.DOSSIER_FILE_NO, dossierFileNo);

			jsonObject
			    .put(DossierFileDisplayTerms.DISPLAY_NAME, displayName);

			jsonObject
			    .put(
			        DossierFileDisplayTerms.DOSSIER_FILE_DATE, dossierFileDate);

			jsonObject
			    .put(DossierFileDisplayTerms.FILE_TITLE, fileEntry
			        .getTitle());

			jsonObject
			    .put(DossierFileDisplayTerms.MIME_TYPE, fileEntry
			        .getMimeType());

			jsonObject
			    .put(DossierFileDisplayTerms.FILE_NAME, fileName);

			jsonObject
			    .put(DossierFileDisplayTerms.FILE_ENTRY_ID, fileEntry
			        .getFileEntryId());
			jsonObject
			    .put(DossierFileDisplayTerms.FOLDE_ID, fileEntry
			        .getFolderId());

			jsonObject
			    .put(DossierFileDisplayTerms.DOSSIER_PART_ID, dossierPartId);

			jsonObject
			    .put(DossierFileDisplayTerms.INDEX, index);

			jsonObject
			    .put(DossierFileDisplayTerms.GROUP_NAME, groupName);

			jsonObject
			    .put(
			        DossierFileDisplayTerms.DOSSIER_FILE_ORIGINAL,
			        PortletConstants.DOSSIER_FILE_ORIGINAL);

			jsonObject
			    .put(
			        DossierFileDisplayTerms.DOSSIER_FILE_TYPE,
			        PortletConstants.DOSSIER_FILE_TYPE_INPUT);

			jsonObject
			    .put(DossierDisplayTerms.TEMPLATE_FILE_NO, templateFileNo);

		}
		catch (Exception e) {
			_log
			    .error(e);
			SessionErrors
			    .add(actionRequest, "upload-error");
		}
		finally {
			StreamUtil
			    .cleanUp(inputStream);
			HttpServletRequest request = PortalUtil
			    .getHttpServletRequest(actionRequest);
			request
			    .setAttribute(
			        WebKeys.RESPONSE_UPLOAD_TEMP_DOSSIER_FILE, jsonObject);

			if (Validator
			    .isNotNull(redirectURL)) {
				actionResponse
				    .setRenderParameter(
				        "jspPage",
				        "/html/portlets/dossiermgt/frontoffice/upload_dossier_file.jsp");
			}
		}
	}

	public void updateTempDynamicFormData(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {

		long dossierPartId = ParamUtil
		    .getLong(actionRequest, DossierFileDisplayTerms.DOSSIER_PART_ID);
		int index = ParamUtil
		    .getInteger(actionRequest, DossierFileDisplayTerms.INDEX);
		String formData = ParamUtil
		    .getString(actionRequest, DossierFileDisplayTerms.FORM_DATA);

		HttpServletRequest request = PortalUtil
		    .getHttpServletRequest(actionRequest);

		request
		    .setAttribute(WebKeys.FORM_DATA + String
		        .valueOf(dossierPartId) + StringPool.DASH + String
		            .valueOf(index),
		        formData);

		HttpSession session = request
		    .getSession();
		session
		    .setAttribute(WebKeys.FORM_DATA + String
		        .valueOf(dossierPartId) + StringPool.DASH + String
		            .valueOf(index),
		        formData);

		actionResponse
		    .setRenderParameter(
		        "mvcPath",
		        "/html/portlets/dossiermgt/frontoffice/dynamic_form.jsp");

	}

	protected void validateDossier(
	    long cityId, long districtId, long wardId, String accountType,
	    String dossierDestinationFolder, String subjectName, String subjectId,
	    String address, String contactName, String contactTelNo,
	    String contactEmail, String[][] uploadDataSchemas)
	    throws EmptyDossierCityCodeException, EmptyDossierDistrictCodeException,
	    EmptyDossierWardCodeException, InvalidDossierObjectException,
	    CreateDossierFolderException, EmptyDossierSubjectNameException,
	    OutOfLengthDossierSubjectNameException, EmptyDossierSubjectIdException,
	    OutOfLengthDossierSubjectIdException, EmptyDossierAddressException,
	    OutOfLengthDossierContactEmailException,
	    OutOfLengthDossierContactNameException,
	    OutOfLengthDossierContactTelNoException,
	    EmptyDossierContactNameException, OutOfLengthDossierAddressException,
	    InvalidDossierObjectException, EmptyDossierFileException {

		if (cityId <= 0) {
			throw new EmptyDossierCityCodeException();
		}

		if (districtId <= 0) {
			throw new EmptyDossierDistrictCodeException();
		}

		if (wardId <= 0) {
			throw new EmptyDossierWardCodeException();
		}

		if (Validator
		    .isNull(accountType)) {
			throw new InvalidDossierObjectException();
		}

		if (Validator
		    .isNull(dossierDestinationFolder)) {
			throw new CreateDossierFolderException();
		}

		if (Validator
		    .isNull(subjectName)) {
			throw new EmptyDossierSubjectNameException();
		}

		if (subjectName
		    .trim().length() > PortletPropsValues.DOSSIERMGT_DOSSIER_SUBJECT_NAME_LENGTH) {
			throw new OutOfLengthDossierSubjectNameException();
		}

		if (Validator
		    .isNull(subjectId)) {
			throw new EmptyDossierSubjectIdException();
		}

		if (subjectId
		    .trim().length() > PortletPropsValues.DOSSIERMGT_DOSSIER_SUBJECT_ID_LENGTH) {
			throw new OutOfLengthDossierSubjectIdException();
		}

		if (Validator
		    .isNull(address)) {
			throw new EmptyDossierAddressException();
		}

		if (address
		    .trim().length() > PortletPropsValues.DOSSIERMGT_DOSSIER_ADDRESS_LENGTH) {
			throw new OutOfLengthDossierAddressException();
		}

		if (Validator
		    .isNull(contactName)) {
			throw new EmptyDossierContactNameException();
		}

		if (contactName
		    .trim().length() > PortletPropsValues.DOSSIERMGT_DOSSIER_CONTACT_NAME_LENGTH) {
			throw new OutOfLengthDossierContactNameException();
		}

		if (contactTelNo
		    .trim().length() > PortletPropsValues.DOSSIERMGT_DOSSIER_CONTACT_TEL_NO_LENGTH) {
			throw new OutOfLengthDossierContactTelNoException();
		}

		if (contactEmail
		    .trim().length() > PortletPropsValues.DOSSIERMGT_DOSSIER_CONTACT_EMAIL_LENGTH) {
			throw new OutOfLengthDossierContactEmailException();
		}

		if (uploadDataSchemas == null || uploadDataSchemas.length == 0) {
			throw new EmptyDossierFileException();
		}
	}

	public void updateDossierStatus(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {

		long dossierId = ParamUtil
		    .getLong(actionRequest, DossierDisplayTerms.DOSSIER_ID);

		long fileGroupId = ParamUtil
		    .getLong(actionRequest, DossierFileDisplayTerms.DOSSIER_FILE_DATE);

		long govAgencyOrganizationId = ParamUtil
		    .getLong(
		        actionRequest, DossierDisplayTerms.GOVAGENCY_ORGANIZATION_ID);

		int dossierStatus = ParamUtil
		    .getInteger(actionRequest, DossierDisplayTerms.DOSSIER_STATUS);

		String redirectURL = ParamUtil
		    .getString(actionRequest, "redirectURL");

		try {
			ServiceContext serviceContext = ServiceContextFactory
			    .getInstance(actionRequest);

			DossierLocalServiceUtil
			    .updateDossierStatus(serviceContext
			        .getUserId(), dossierId, govAgencyOrganizationId,
			        dossierStatus,
			        PortletConstants.DOSSIER_FILE_SYNC_STATUS_REQUIREDSYNC,
			        fileGroupId, PortletConstants.DOSSIER_LOG_NORMAL,
			        serviceContext
			            .getLocale());

			switch (dossierStatus) {
			case PortletConstants.DOSSIER_STATUS_SYSTEM:

				Message message = new Message();
				message
				    .put("action", "submit");
				message
				    .put("dossierId", dossierId);
				message
				    .put("fileGroupId", fileGroupId);
				message
			    	.put("level", PortletConstants.DOSSIER_LOG_NORMAL);
				message
		    		.put("locale", serviceContext.getLocale());
				
				message
	    			.put("govAgencyOrganizationId", govAgencyOrganizationId);
				
				message
    				.put("userId", serviceContext.getUserId());
				
				MessageBusUtil
				    .sendMessage(
				        "opencps/frontoffice/out/destination", message);
				break;

			default:
				break;
			}

		}
		catch (Exception e) {
			_log
			    .error(e);
		}
		finally {
			if (Validator
			    .isNotNull(redirectURL)) {
				actionResponse
				    .sendRedirect(redirectURL);
			}
		}
	}

	protected void writeJSON(
	    ActionRequest actionRequest, ActionResponse actionResponse, Object json)
	    throws IOException {

		HttpServletResponse response = PortalUtil
		    .getHttpServletResponse(actionResponse);

		response
		    .setContentType(ContentTypes.APPLICATION_JSON);

		ServletResponseUtil
		    .write(response, json
		        .toString());
		response
		    .flushBuffer();

	}

	private Log _log = LogFactoryUtil
	    .getLog(DossierMgtFrontOfficePortlet.class
	        .getName());
}
