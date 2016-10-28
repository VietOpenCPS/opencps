/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package org.opencps.api.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.opencps.api.service.ApiServiceLocalServiceUtil;
import org.opencps.api.service.base.ApiServiceServiceBaseImpl;
import org.opencps.api.util.APIServiceConstants;
import org.opencps.backend.message.SendToEngineMsg;
import org.opencps.backend.message.UserActionMsg;
import org.opencps.dossiermgt.NoSuchDossierException;
import org.opencps.dossiermgt.NoSuchDossierFileException;
import org.opencps.dossiermgt.NoSuchDossierPartException;
import org.opencps.dossiermgt.bean.ProcessOrderBean;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.processmgt.NoSuchProcessOrderException;
import org.opencps.processmgt.NoSuchProcessWorkflowException;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.model.ProcessWorkflow;
import org.opencps.servicemgt.model.ServiceInfo;
import org.opencps.util.DLFolderUtil;
import org.opencps.util.DateTimeUtil;
import org.opencps.util.PortletConstants;
import org.opencps.util.PortletUtil;
import org.opencps.util.WebKeys;
import org.opencps.util.PortletUtil.SplitDate;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;
import com.liferay.portlet.documentlibrary.util.DLUtil;

/**
 * The implementation of the api service remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link org.opencps.api.service.ApiServiceService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author trungdk
 * @see org.opencps.api.service.base.ApiServiceServiceBaseImpl
 * @see org.opencps.api.service.ApiServiceServiceUtil
 */
public class ApiServiceServiceImpl extends ApiServiceServiceBaseImpl {
	
	@JSONWebService(value = "sms", method = "GET")
	public void receiveSMS(String phone, String message) 
		throws SystemException, PortalException {
		
		ServiceContext serviceContext = getServiceContext();
		
		JSONObject inputObj = JSONFactoryUtil.createJSONObject();
		inputObj.put("phone", phone);
		inputObj.put("message", message);
		
		ApiServiceLocalServiceUtil.addLog(getUserId(), APIServiceConstants.CODE_06, 
			serviceContext.getRemoteAddr(), phone, inputObj.toString(), 
			APIServiceConstants.IN, serviceContext);
	}
	
	@JSONWebService(value = "dossier", method = "POST")
	public JSONObject addDossier(String event, String dossierInfo) 
		throws SystemException, PortalException {
		
		JSONObject resultObj = JSONFactoryUtil.createJSONObject();
		
		ServiceContext serviceContext = getServiceContext();
		
		long userId = 0;
		
		try {
			userId = getUserId();
			
			JSONObject input = JSONFactoryUtil.createJSONObject();
			input.put("event", event);
			input.put("dossierInfo", dossierInfo);

			ApiServiceLocalServiceUtil.addLog(userId,
				APIServiceConstants.CODE_07, serviceContext.getRemoteAddr(), event, 
				input.toString(), APIServiceConstants.IN,
				serviceContext);
			
			JSONObject dossierInfoObj = JSONFactoryUtil.createJSONObject(dossierInfo);
			
			String govAgencyCode = dossierInfoObj.getString("govAgencyCode");
			String serviceNo = dossierInfoObj.getString("serviceNo");
			//String serviceName = dossierInfoObj.getString("serviceName");
			String receptionNo = dossierInfoObj.getString("receptionNo");
			String receiveDatetime = dossierInfoObj.getString("receiveDatetime");
			String estimateDatetime = dossierInfoObj.getString("estimateDatetime");
			String subjectName = dossierInfoObj.getString("subjectName");
			String address = dossierInfoObj.getString("address");
			String cityCode = dossierInfoObj.getString("cityCode");
			String cityName = dossierInfoObj.getString("cityName");
			String districtCode = dossierInfoObj.getString("districtCode");
			String districtName = dossierInfoObj.getString("districtName");
			String wardCode = dossierInfoObj.getString("wardCode");
			String wardName = dossierInfoObj.getString("wardName");
			String contactName = dossierInfoObj.getString("contactName");
			String contactTelNo = dossierInfoObj.getString("contactTelNo");
			String contactEmail = dossierInfoObj.getString("contactEmail");
			String note = dossierInfoObj.getString("note");
			String dossierFiles = dossierInfoObj.getString("dossierFiles");
			
			ServiceInfo serviceInfo = serviceInfoPersistence.fetchByC_SN(
				serviceContext.getCompanyId(), serviceNo);
			
			ServiceConfig serviceConfig = serviceConfigPersistence.findByG_S_G(
				serviceContext.getScopeGroupId(), serviceInfo.getServiceinfoId(), govAgencyCode);
			
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			long ownerOrganizationId = 0;
			long dossierTemplateId = serviceConfig.getDossierTemplateId();
			String templateFileNo = StringPool.BLANK;
			long serviceConfigId = serviceConfig.getServiceConfigId();
			long serviceInfoId = serviceInfo.getServiceinfoId();
			String serviceDomainIndex = serviceConfig.getServiceDomainIndex();
			long govAgencyOrganizationId = serviceConfig.getGovAgencyOrganizationId();
			String govAgencyName = serviceConfig.getGovAgencyName();
			int serviceMode = serviceConfig.getServiceMode();
			String serviceAdministrationIndex = serviceConfig.getServiceAdministrationIndex();
			String subjectId = StringPool.BLANK;
			
			String dossierDestinationFolder = StringPool.BLANK;

			SplitDate splitDate = PortletUtil.splitDate(new Date());

			dossierDestinationFolder =
				PortletUtil.getDossierDestinationFolder(
					serviceContext.getScopeGroupId(), splitDate.getYear(),
					splitDate.getMonth(), splitDate.getDayOfMoth());

			DLFolder dossierFolder =
				DLFolderUtil.getTargetFolder(
					serviceContext.getUserId(),
					serviceContext.getScopeGroupId(),
					serviceContext.getScopeGroupId(), false, 0,
					dossierDestinationFolder, StringPool.BLANK, false,
					serviceContext);
			
			// tao moi ho so
			Dossier dossier = DossierLocalServiceUtil.addDossier(
				serviceContext.getUserId(), ownerOrganizationId,
				dossierTemplateId, templateFileNo, serviceConfigId,
				serviceInfoId, serviceDomainIndex,
				govAgencyOrganizationId, govAgencyCode, govAgencyName,
				serviceMode, serviceAdministrationIndex, cityCode,
				cityName, districtCode, districtName, wardName,
				wardCode, subjectName, subjectId, address, contactName,
				contactTelNo, contactEmail, note,
				PortletConstants.DOSSIER_SOURCE_DIRECT,
				PortletConstants.DOSSIER_STATUS_NEW,
				dossierFolder.getFolderId(), StringPool.BLANK,
				serviceContext);
			
			dossier.setReceptionNo(receptionNo);
			
			if(Validator.isNotNull(receiveDatetime)) {
				Date date_receiveDatetime = formatter.parse(receiveDatetime);
				dossier.setReceiveDatetime(date_receiveDatetime);
			}
			
			if(Validator.isNotNull(estimateDatetime)) {
				Date date_estimateDatetime = formatter.parse(estimateDatetime);
				dossier.setEstimateDatetime(date_estimateDatetime);
			}
			
			//update ho so co ma tiep nhan
			dossier = DossierLocalServiceUtil.updateDossier(dossier);
			
			//update ho so ve system
			DossierLocalServiceUtil.updateDossierStatus(
				dossier.getDossierId(), 0, PortletConstants.DOSSIER_STATUS_SYSTEM,
				WebKeys.DOSSIER_ACTOR_CITIZEN, 0,
				StringPool.BLANK, StringPool.BLANK, PortletUtil.getActionInfo(
					PortletConstants.DOSSIER_STATUS_SYSTEM,
					serviceContext.getLocale()), StringPool.BLANK,
				PortletConstants.DOSSIER_FILE_SYNC_STATUS_REQUIREDSYNC,
				PortletConstants.DOSSIER_LOG_NORMAL);
			
			//chuyen ho so vao backend
			UserActionMsg actionMsg = new UserActionMsg();

			Message message = new Message();
			
			actionMsg.setAction(WebKeys.ACTION_SUBMIT_VALUE);

			actionMsg.setDossierId(dossier.getDossierId());

			actionMsg.setFileGroupId(0);

			actionMsg.setLocale(serviceContext.getLocale());

			actionMsg.setUserId(serviceContext.getUserId());

			actionMsg.setGroupId(serviceContext.getScopeGroupId());

			actionMsg.setCompanyId(dossier.getCompanyId());

			actionMsg.setGovAgencyCode(dossier.getGovAgencyCode());

			actionMsg.setDossierOId(dossier.getOid());

			actionMsg.setDossierStatus(PortletConstants.DOSSIER_STATUS_NEW);

			message.put("msgToEngine", actionMsg);
			
			MessageBusUtil.sendMessage(
				"opencps/frontoffice/out/destination", message);
			
			
			resultObj.put("statusCode", "Success");
			resultObj.put("oid", dossier.getOid());
		} catch (Exception e) {
			_log.error(e);
			
			resultObj = JSONFactoryUtil.createJSONObject();
			resultObj.put("statusCode", "Error");
			resultObj.put("message", e.getClass().getName());
		}
		
		ApiServiceLocalServiceUtil.addLog(userId, APIServiceConstants.CODE_07, 
			serviceContext.getRemoteAddr(), "", resultObj.toString(), 
			APIServiceConstants.OUT, serviceContext);
		
		return resultObj;
	}
	
	@JSONWebService(value = "dossiers", method = "GET")
	public JSONObject searchDossierByUserAssignProcessOrder(String username)
			throws SystemException {
		
		JSONObject resultObj = JSONFactoryUtil.createJSONObject();
		
		ServiceContext serviceContext = getServiceContext();
		
		long userId = 0;
		
		try {
			userId = getUserId();
			
			JSONObject inputObj = JSONFactoryUtil.createJSONObject();
			inputObj.put("username", username);
			
			if(_log.isDebugEnabled()) {
				ApiServiceLocalServiceUtil.addLog(userId, APIServiceConstants.CODE_02, 
					serviceContext.getRemoteAddr(), "", inputObj.toString(), 
					APIServiceConstants.IN, serviceContext);
			}
			
			int serviceInfoId = 0;
			int processStepId = 0;
			
			//int count = ProcessOrderLocalServiceUtil.countProcessOrder(serviceInfoId, processStepId, userId, userId);
			
			List<ProcessOrderBean> processOrders = processOrderLocalService.searchProcessOrder(
					serviceInfoId, processStepId, userId, userId,
					QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
			
			JSONArray resultArr = JSONFactoryUtil.createJSONArray();
			for (ProcessOrderBean processOrderBean : processOrders) {
				
				Dossier dossier = dossierPersistence.fetchByPrimaryKey(processOrderBean.getDossierId());
				
				JSONObject dossierObj = getDossierForList(dossier);
				
				if(dossierObj != null) {
					resultArr.put(dossierObj);
				}
			}

			resultObj.put("data", resultArr);
			resultObj.put("statusCode", "Success");
		}
		catch (Exception e) {
			_log.error(e);
			
			resultObj = JSONFactoryUtil.createJSONObject();
			resultObj.put("statusCode", "Error");
			resultObj.put("message", e.getClass().getName());
		}	
		
		if(_log.isDebugEnabled()) {
			ApiServiceLocalServiceUtil.addLog(userId, APIServiceConstants.CODE_02, 
				serviceContext.getRemoteAddr(), "", resultObj.toString(), 
				APIServiceConstants.OUT, serviceContext);
		}
			
		return resultObj;
	}

	@JSONWebService(value = "dossiers", method = "GET")
	public JSONObject searchDossierByProcessStepAndUser(String processno,
			String stepno, String username) {
		
		JSONObject resultObj = JSONFactoryUtil.createJSONObject();
		
		ServiceContext serviceContext = getServiceContext();
		
		long userId = 0;
		
		try {
			userId = getUserId();
			
			JSONObject inputObj = JSONFactoryUtil.createJSONObject();
			inputObj.put("processno", processno);
			inputObj.put("stepno", stepno);
			inputObj.put("username", username);
			
			if(_log.isDebugEnabled()) {
				ApiServiceLocalServiceUtil.addLog(userId, APIServiceConstants.CODE_01, 
					serviceContext.getRemoteAddr(), "", inputObj.toString(), 
					APIServiceConstants.IN, serviceContext);
			}
			
			/*int count = dossierLocalService.countDossierByP_PS_U(processno, stepno,
					username);*/
			
			List<Dossier> dossiers = dossierLocalService.searchDossierByP_PS_U(
					processno, stepno, username, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
			
			JSONArray resultArr = JSONFactoryUtil.createJSONArray();
			
			for (Dossier dossier : dossiers) {
				JSONObject dossierObj = getDossierForList(dossier);
				
				if(dossierObj != null) {
					resultArr.put(dossierObj);
				}
			}

			resultObj.put("data", resultArr);
			resultObj.put("statusCode", "Success");
		}
		catch (Exception e) {
			_log.error(e);
			
			resultObj = JSONFactoryUtil.createJSONObject();
			resultObj.put("statusCode", "Error");
			resultObj.put("message", e.getClass().getName());
		}
		
		if(_log.isDebugEnabled()) {
			ApiServiceLocalServiceUtil.addLog(userId, APIServiceConstants.CODE_01, 
				serviceContext.getRemoteAddr(), "", resultObj.toString(), 
				APIServiceConstants.OUT, serviceContext);
		}

		return resultObj;
	}

	@JSONWebService(value = "dossiers", method = "GET")
	public JSONObject getByoid(String oid) {
		
		JSONObject jsonObject = null;
		
		Dossier dossier = null;
		
		ServiceContext serviceContext = getServiceContext();
		
		long userId = 0;
		
		try {
			jsonObject = JSONFactoryUtil.createJSONObject();
			
			userId = getUserId();
			
			JSONObject input = JSONFactoryUtil.createJSONObject();
			input.put("oid", oid);

			ApiServiceLocalServiceUtil.addLog(userId, APIServiceConstants.CODE_03, 
				serviceContext.getRemoteAddr(), oid, input.toString(), 
				APIServiceConstants.IN, serviceContext);
			
			dossier = dossierPersistence.findByOID(oid);
			
			jsonObject = getDossierDetail(dossier, serviceContext);
		} catch (Exception e) {
			_log.error(e);
			
			jsonObject = JSONFactoryUtil.createJSONObject();
			
			jsonObject.put("statusCode", "Error");
			
			if(e instanceof NoSuchDossierException) {
				jsonObject.put("message", "DossierNotFound");
			} else {
				jsonObject.put("message", e.getClass().getName());
			}
			
		}
		
		ApiServiceLocalServiceUtil.addLog(userId,
			APIServiceConstants.CODE_03, serviceContext.getRemoteAddr(), oid, jsonObject.toString(), APIServiceConstants.OUT,
			serviceContext);

		return jsonObject;
	}

	@JSONWebService(value = "dossierfile", method = "POST")
	public JSONObject addDossierFile(String oid, String dossierfile) {
		
		JSONObject resultObj = JSONFactoryUtil.createJSONObject();
		
		ServiceContext serviceContext = getServiceContext();
		
		long userId = 0;
		
		try {
			userId = getUserId();
			
			JSONObject input = JSONFactoryUtil.createJSONObject();
			input.put("oid", oid);
			input.put("dossierfile", dossierfile);

			ApiServiceLocalServiceUtil.addLog(userId,
				APIServiceConstants.CODE_04, serviceContext.getRemoteAddr(), oid, 
				input.toString(), APIServiceConstants.IN,
				serviceContext);
			
			JSONObject dossierfileObj = JSONFactoryUtil.createJSONObject(dossierfile);
			String dossierFileOid = dossierfileObj.getString("dossierFileOid");
			String dossierPartNo = dossierfileObj.getString("dossierPartNo");
			String dossierFileContent = dossierfileObj.getString("dossierFileContent");
			String dossierFileURL = dossierfileObj.getString("dossierFileURL");
			String dossierFileName = dossierfileObj.getString("dossierFileName");
			String dossierFileNo = dossierfileObj.getString("dossierFileNo");
			String dossierFileDate = dossierfileObj.getString("dossierFileDate");
			
			Dossier dossier = dossierPersistence.findByOID(oid);
			
			long dossierId = dossier.getDossierId();
			
			DossierPart dossierPart = dossierPartLocalService
				.getDossierPartByT_PN(dossier.getDossierTemplateId(), dossierPartNo);
			
			DossierFile dossierFile = null;
			
			if(Validator.isNotNull(dossierFileOid)) {
				dossierFile = dossierFilePersistence.findByOid(dossierFileOid);
			}
			
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			Date fileDate = null;
			
			if(Validator.isNotNull(dossierFileDate)) {
				try {
					fileDate = formatter.parse(dossierFileDate);
				}
				catch (ParseException e) {
					_log.error(e);
				}
			}
			
			if(fileDate == null) {
				fileDate = new Date();
			}
			
			if (Validator.isNull(dossierFileOid)) {
				if (Validator.isNull(dossierFileURL)) {

					serviceContext.setUserId(dossier.getUserId());
					
					dossierFileLocalService
							.addDossierFile(
									dossier.getUserId(),
									dossierId,
									dossierPart.getDossierpartId(),
									dossierPart.getTemplateFileNo(),
									StringPool.BLANK,
									0,
									0,
									dossier.getUserId(),
									dossier.getOwnerOrganizationId(),
									dossierFileName,
									dossierFileContent,
									0,
									PortletConstants.DOSSIER_FILE_MARK_UNKNOW,
									2,
									dossierFileNo,
									fileDate,
									1,
									PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
									serviceContext);

					
				} else if (dossierFileContent.equals("") || dossierFileContent.equals("{}")) {
					byte[] bytes = getFileFromURL(dossierFileURL);
					
					String sourceFileName = dossierFileName;
					
					String extension = FileUtil.getExtension(sourceFileName);
					
					if(Validator.isNull(extension)) {
						extension = StringUtil.replace(FileUtil.getExtension(dossierFileURL), 
							StringPool.FORWARD_SLASH, StringPool.BLANK);
						
						if(Validator.isNotNull(extension)) {
							sourceFileName = dossierFileName.concat(StringPool.UNDERLINE)
									.concat(String.valueOf(System.nanoTime()))
									.concat(StringPool.PERIOD).concat(extension);
						}
					}
					
					String mimeType = MimeTypesUtil.getExtensionContentType(extension);
					
					serviceContext.setUserId(dossier.getUserId());
					
					DLFolder dossierFolder = DLFolderUtil.getDossierFolder(
							serviceContext.getScopeGroupId(),
							null, dossier.getOid(),
							serviceContext);
					
					dossierFileLocalService
							.addDossierFile(
									dossier.getUserId(),
									dossierId,
									dossierPart.getDossierpartId(),
									dossierPart.getTemplateFileNo(),
									StringPool.BLANK,
									0L,
									0L,
									dossier.getUserId(),
									dossier.getOwnerOrganizationId(),
									dossierFileName,
									mimeType,
									PortletConstants.DOSSIER_FILE_MARK_UNKNOW,
									2,
									dossierFileNo,
									fileDate,
									1,
									PortletConstants.DOSSIER_FILE_SYNC_STATUS_SYNCSUCCESS,
									dossierFolder.getFolderId(),
									sourceFileName, mimeType, dossierFileName,
									StringPool.BLANK, StringPool.BLANK,
									bytes, serviceContext);
				}
			} else {
				if (Validator.isNull(dossierFileURL)) {
					serviceContext.setUserId(dossier.getUserId());

					dossierFileLocalService
							.addDossierFile(
									dossier.getUserId(),
									dossierId,
									dossierPart.getDossierpartId(),
									dossierPart.getTemplateFileNo(),
									StringPool.BLANK,
									0,
									0,
									dossier.getUserId(),
									dossier.getOwnerOrganizationId(),
									dossierFileName,
									dossierFileContent,
									dossierFile != null ? dossierFile
											.getFileEntryId() : 0,
									PortletConstants.DOSSIER_FILE_MARK_UNKNOW,
									2,
									dossierFileNo,
									new Date(),
									1,
									PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
									serviceContext);

				} else {
					byte[] bytes = getFileFromURL(dossierFileURL);
					
					String sourceFileName = dossierFileName;
					
					String extension = FileUtil.getExtension(sourceFileName);
					
					if(Validator.isNull(extension)) {
						extension = StringUtil.replace(FileUtil.getExtension(dossierFileURL), 
							StringPool.FORWARD_SLASH, StringPool.BLANK);
						
						if(Validator.isNotNull(extension)) {
							sourceFileName = dossierFileName.concat(StringPool.UNDERLINE)
									.concat(String.valueOf(System.nanoTime()))
									.concat(StringPool.PERIOD).concat(extension);
						}
					}
					
					String mimeType = MimeTypesUtil.getExtensionContentType(extension);

					serviceContext.setScopeGroupId(dossier.getGroupId());
					serviceContext.setCompanyId(dossier.getCompanyId());
					serviceContext.setUserId(dossier.getUserId());

					DLFolder dossierFolder = DLFolderUtil.getDossierFolder(
							serviceContext.getScopeGroupId(),
							null, dossier.getOid(),
							serviceContext);
					
					dossierPart = dossierPartLocalService
							.getDossierPartByPartNo(dossierPartNo);
					
					dossierFileLocalService
							.addDossierFile(
									dossier.getUserId(),
									dossierId,
									dossierPart.getDossierpartId(),
									dossierPart.getTemplateFileNo(),
									StringPool.BLANK,
									0L,
									0L,
									dossier.getUserId(),
									dossier.getOwnerOrganizationId(),
									dossierFileName,
									mimeType,
									PortletConstants.DOSSIER_FILE_MARK_UNKNOW,
									2,
									dossierFileNo,
									new Date(),
									1,
									PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
									dossierFolder.getFolderId(),
									sourceFileName, mimeType, dossierFileName,
									StringPool.BLANK, StringPool.BLANK, bytes, serviceContext);
				}
			}
			
			resultObj.put("statusCode", "Success");
		} catch (Exception e) {
			_log.error(e);
			
			resultObj = JSONFactoryUtil.createJSONObject();
			resultObj.put("statusCode", "Error");
			
			if(e instanceof NoSuchDossierException) {
				resultObj.put("message", "DossierNotFound");
			} else if(e instanceof NoSuchDossierPartException) {
				resultObj.put("message", "DossierPartNotFound");
			} else if(e instanceof NoSuchDossierFileException) {
				resultObj.put("message", "DossierFileNotFound");
			} else {
				resultObj.put("message", e.getClass().getName());
			}
		}
		
		ApiServiceLocalServiceUtil.addLog(userId, APIServiceConstants.CODE_04, 
			serviceContext.getRemoteAddr(), oid, resultObj.toString(), 
			APIServiceConstants.OUT, serviceContext);
		
		return resultObj;
	}

	@JSONWebService(value = "processorder", method = "POST")
	public JSONObject nextStep(String oid, String actioncode, String username) {
		
		return apiServiceService.nextStep(oid, actioncode, StringPool.BLANK, username);
	}

	@JSONWebService(value = "processorder", method = "POST")
	public JSONObject nextStep(String oid, String actioncode, String actionnote, String username) {
		
		JSONObject resultObj = JSONFactoryUtil.createJSONObject();
		
		ServiceContext serviceContext = getServiceContext();
		
		long userId = 0;
		
		try {
			userId = getUserId();
			
			JSONObject input = JSONFactoryUtil.createJSONObject();
			input.put("oid", oid);
			input.put("actioncode", actioncode);
			input.put("actionnote", actionnote);
			input.put("username", username);
			
			// insert log received
			ApiServiceLocalServiceUtil.addLog(userId, APIServiceConstants.CODE_05, 
				serviceContext.getRemoteAddr(), oid, 
				input.toString(), APIServiceConstants.IN,
				serviceContext);
			
			Dossier dossier = dossierPersistence.findByOID(oid);
			
			ProcessOrder processOrder = processOrderPersistence.findByD_F(
					dossier.getDossierId(), 0);
			
			User user = userLocalService.getUserByScreenName(
					dossier.getCompanyId(), username);

			ProcessWorkflow processWorkflow = processWorkflowPersistence
					.findByActionCode(actioncode);

			Message message = new Message();
			
			/*if (Validator.isNotNull(processWorkflow.getAutoEvent())) {
				message.put(ProcessOrderDisplayTerms.EVENT, 
						processWorkflow.getAutoEvent());
			} else {
				message.put(ProcessOrderDisplayTerms.PROCESS_WORKFLOW_ID,
						processWorkflow.getProcessWorkflowId());
			}
			
			message.put(ProcessOrderDisplayTerms.ACTION_NOTE, actionnote);
			message.put(ProcessOrderDisplayTerms.PROCESS_STEP_ID,
					processOrder.getProcessStepId());
			message.put(ProcessOrderDisplayTerms.ASSIGN_TO_USER_ID, 0);
			message.put(ProcessOrderDisplayTerms.SERVICE_PROCESS_ID,
					processOrder.getServiceProcessId());
			message.put(ProcessOrderDisplayTerms.PAYMENTVALUE, 0);
			
			message.put(ProcessOrderDisplayTerms.ACTION_USER_ID,
					user.getUserId());

			message.put(ProcessOrderDisplayTerms.PROCESS_ORDER_ID,
					processOrder.getProcessOrderId());
			message.put(ProcessOrderDisplayTerms.FILE_GROUP_ID, 0);
			message.put(ProcessOrderDisplayTerms.DOSSIER_ID,
					dossier.getDossierId());

			message.put(ProcessOrderDisplayTerms.GROUP_ID, dossier.getGroupId());

			message.put(ProcessOrderDisplayTerms.COMPANY_ID,
					dossier.getCompanyId());*/

			SendToEngineMsg sendToEngineMsg = new SendToEngineMsg();

			// sendToEngineMsg.setAction(WebKeys.ACTION);
			sendToEngineMsg.setCompanyId(dossier.getCompanyId());
			sendToEngineMsg.setGroupId(dossier.getGroupId());
			sendToEngineMsg.setActionNote(actionnote);
			sendToEngineMsg.setAssignToUserId(0);
			sendToEngineMsg.setActionUserId(user.getUserId());
			sendToEngineMsg.setDossierId(dossier.getDossierId());
			sendToEngineMsg.setFileGroupId(0);
			sendToEngineMsg.setPaymentValue(GetterUtil.getDouble(0));
			sendToEngineMsg.setProcessOrderId(processOrder.getProcessOrderId());
			/*
			sendToEngineMsg.setProcessWorkflowId(processWorkflow
					.getProcessWorkflowId());
			*/
			sendToEngineMsg.setReceptionNo(Validator.isNotNull(dossier
					.getReceptionNo()) ? dossier.getReceptionNo()
					: StringPool.BLANK);
			sendToEngineMsg.setSignature(0);
			sendToEngineMsg.setDossierStatus(dossier.getDossierStatus());
			
			if (Validator.isNotNull(processWorkflow.getAutoEvent())) {
				sendToEngineMsg.setEvent(processWorkflow.getAutoEvent());
			}
			else {
				sendToEngineMsg.setProcessWorkflowId(processWorkflow
						.getProcessWorkflowId());
			}

			message.put("msgToEngine", sendToEngineMsg);
			
			MessageBusUtil.sendMessage("opencps/backoffice/engine/destination",
					message);
			
			resultObj.put("statusCode", "Success");
			
		} catch (Exception e) {
			_log.error(e);
			
			resultObj = JSONFactoryUtil.createJSONObject();
			resultObj.put("statusCode", "Error");
			
			if(e instanceof NoSuchDossierException) {
				resultObj.put("message", "DossierNotFound");
			} else if(e instanceof NoSuchProcessOrderException) {
				resultObj.put("message", "ProcessOrderNotFound");
			} else if(e instanceof NoSuchProcessWorkflowException) {
				resultObj.put("message", "ActionCodeNotFound");
			} else {
				resultObj.put("message", e.getClass().getName());
			}
		}
		
		ApiServiceLocalServiceUtil.addLog(userId, APIServiceConstants.CODE_05,
			serviceContext.getRemoteAddr(), oid, resultObj.toString(), APIServiceConstants.OUT,
			serviceContext);
		
		return resultObj;
	}

	@JSONWebService(value = "dossiers", method = "GET")
	public JSONObject searchDossierByDS_RD_SN_U(String dossierstatus,
			String serviceno, String fromdate, String todate, String username)
			throws SystemException {
		
		JSONObject resultObj = JSONFactoryUtil.createJSONObject();
		
		ServiceContext serviceContext = getServiceContext();
		
		long userId = 0;
		
		try {
			userId = getUserId();
			
			JSONObject inputObj = JSONFactoryUtil.createJSONObject();
			inputObj.put("dossierstatus", dossierstatus);
			inputObj.put("serviceno", serviceno);
			inputObj.put("fromdate", fromdate);
			inputObj.put("todate", todate);
			inputObj.put("username", username);
			
			ApiServiceLocalServiceUtil.addLog(userId, APIServiceConstants.CODE_05, 
				serviceContext.getRemoteAddr(), "", 
				inputObj.toString(), APIServiceConstants.IN,
				serviceContext);
			
			/*
			int count = dossierLocalService.countDossierByDS_RD_SN_U(dossierstatus,
					serviceno, fromdate, todate, username);*/
			
			List<Dossier> dossiers = dossierLocalService.searchDossierByDS_RD_SN_U(
					dossierstatus,serviceno, fromdate, todate, username, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
			
			JSONArray resultArr = JSONFactoryUtil.createJSONArray();
			
			for (Dossier dossier : dossiers) {
				JSONObject dossierObj = getDossierForList(dossier);
				
				if(dossierObj != null) {
					resultArr.put(dossierObj);
				}
			}
	
			resultObj.put("statusCode", "Success");
			resultObj.put("data", resultArr);
			
		} catch (Exception e) {
			_log.error(e);
			
			resultObj = JSONFactoryUtil.createJSONObject();
			
			resultObj.put("statusCode", "Error");
			resultObj.put("message", e.getClass().getName());
		}
		
		ApiServiceLocalServiceUtil.addLog(userId, APIServiceConstants.CODE_05, 
			serviceContext.getRemoteAddr(), "", 
			resultObj.toString(), APIServiceConstants.OUT,
			serviceContext);

		return resultObj;
	}
	
	@JSONWebService(value = "dossiers", method = "GET")
	public JSONObject getByoid(String oid, String filetype) {

		return getByoid(oid);
	}	
	
	/**
	 * function build dossier jsonobject for api get dossier
	 * 
	 * @param dossier
	 * @return
	 * @throws SystemException
	 */
	private JSONObject getDossierDetail(Dossier dossier, ServiceContext serviceContext) 
			throws PortalException, SystemException {
		
		JSONObject dossierObj = null;
		
		if(dossier != null) {
			ServiceInfo serviceInfo = serviceInfoPersistence.findByPrimaryKey(dossier.getServiceInfoId());
		
			SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtil._VN_DATE_TIME_FORMAT);
			
			dossierObj = JSONFactoryUtil.createJSONObject();
			
			dossierObj.put("oid", dossier.getOid());
			dossierObj.put("serviceNo", serviceInfo.getServiceNo());
			dossierObj.put("serviceName", serviceInfo.getServiceName());
			dossierObj.put("govAgencyCode", dossier.getGovAgencyCode());
			dossierObj.put("govAgencyName", dossier.getGovAgencyName());
			dossierObj.put("subjectName", dossier.getSubjectName());
			dossierObj.put("address", dossier.getAddress());
			dossierObj.put("cityCode", dossier.getCityCode());
			dossierObj.put("cityName", dossier.getCityName());
			dossierObj.put("districtCode", dossier.getDistrictCode());
			dossierObj.put("districtName", dossier.getDistrictName());
			dossierObj.put("wardCode", dossier.getWardCode());
			dossierObj.put("wardName", dossier.getWardName());
			dossierObj.put("contactName", dossier.getContactName());
			dossierObj.put("contactTelNo", dossier.getContactTelNo());
			dossierObj.put("contactEmail", dossier.getContactEmail());
			dossierObj.put("note", dossier.getNote());
			
			if (dossier.getSubmitDatetime() != null) {
				dossierObj.put("submitDatetime",
						sdf.format(dossier.getSubmitDatetime()));
			}
			
			if (dossier.getReceiveDatetime() != null) {
				dossierObj.put("receiveDatetime",
						sdf.format(dossier.getReceiveDatetime()));
			}
			
			dossierObj.put("receptionNo", dossier.getReceptionNo());
			
			if (dossier.getEstimateDatetime() != null) {
				dossierObj.put("estimateDatetime", 
						sdf.format(dossier.getEstimateDatetime()));
			}
			
			if (dossier.getFinishDatetime() != null) {
				dossierObj.put("finishDatetime",
						sdf.format(dossier.getFinishDatetime()));
			}
			
			dossierObj.put("dossierStatus", dossier.getDossierStatus());
			dossierObj.put("delayStatus", dossier.getDelayStatus());

			List<DossierFile> dossierFiles = dossierFileLocalService
					.getDossierFileByDossierId(dossier.getDossierId());
			
			JSONArray dfArr = JSONFactoryUtil.createJSONArray();
			
			for (DossierFile df : dossierFiles) {
				DossierPart dpart = dossierPartPersistence.fetchByPrimaryKey(df.getDossierPartId());
				
				if(dpart != null) {
					JSONObject jsonDossierFile = JSONFactoryUtil.createJSONObject();
					
					jsonDossierFile.put("dossierFileOid", df.getOid());
					jsonDossierFile.put("dossierFileURL", "");
					jsonDossierFile.put("dossierPartNo", dpart.getPartNo());
					jsonDossierFile.put("dossierFileName", df.getDisplayName());
					jsonDossierFile.put("templateFileNo", df.getTemplateFileNo());
					jsonDossierFile.put("dossierFileNo", df.getDossierFileNo());
					
					if (df.getFileEntryId() > 0) {
						try {
							FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(df.getFileEntryId());
							jsonDossierFile.put("dossierFullFileName", df.getDisplayName() + "." + fileEntry.getExtension());
							
							String url = getFileURL(fileEntry, serviceContext);
	
							jsonDossierFile.put("dossierFileURL", url);
	
						} catch (Exception e) {
							_log.error(e);
						}
					} 
					
					if (Validator.isNotNull(df.getFormData())) {
						jsonDossierFile.put("dossierFileContent", df.getFormData());
					}
					
					if (df.getDossierFileDate() != null) {
						jsonDossierFile.put("dossierFileDate", sdf.format(df.getDossierFileDate()));
					}
					
					dfArr.put(jsonDossierFile);
				}
			}

			dossierObj.put("dossierFiles", dfArr);
		}
		
		return dossierObj;
	}
	
	/**
	 * function build dossier jsonobject for api get list dossier
	 * 
	 * @param dossier
	 * @return
	 * @throws SystemException
	 */
	private JSONObject getDossierForList(Dossier dossier) throws SystemException {
		JSONObject dossierObj = null;
		
		if(dossier != null) {
			ServiceInfo serviceInfo = serviceInfoPersistence.fetchByPrimaryKey(dossier.getServiceInfoId());
			
			if(serviceInfo != null) {
				SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtil._VN_DATE_TIME_FORMAT);
				
				dossierObj = JSONFactoryUtil.createJSONObject();
				
				dossierObj.put("oid", dossier.getOid());
				dossierObj.put("serviceNo", serviceInfo.getServiceNo());
				dossierObj.put("serviceName", serviceInfo.getServiceName());
				dossierObj.put("subjectName", dossier.getSubjectName());
				dossierObj.put("address", dossier.getAddress());
				dossierObj.put("receptionNo", dossier.getReceptionNo());
				dossierObj.put("dossierStatus", dossier.getDossierStatus());
				dossierObj.put("delayStatus", dossier.getDelayStatus());
				
				if (dossier.getSubmitDatetime() != null) {
					dossierObj.put("submitDatetime",
						sdf.format(dossier.getSubmitDatetime()));
				}
				
				if (dossier.getReceiveDatetime() != null) {
					dossierObj.put("receiveDatetime",
						sdf.format(dossier.getReceiveDatetime()));
				}
				
				if (dossier.getEstimateDatetime() != null) {
					dossierObj.put("estimateDatetime", 
						sdf.format(dossier.getEstimateDatetime()));
				}
			}
		}
		
		return dossierObj;
	}
	
	private ServiceContext getServiceContext() {
		
		ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();
		
		try {
			if(serviceContext == null) {
				serviceContext = new ServiceContext();
				
				if(getUser() != null) {
					serviceContext.setUserId(getUser().getUserId());
					serviceContext.setScopeGroupId(getUser().getGroupId());
					serviceContext.setCompanyId(getUser().getCompanyId());
				}
			}
		} catch(Exception e) {
			_log.error(e);
		}
		
		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		
		return serviceContext;
	}
	
	private String getFileURL(FileEntry fileEntry, ServiceContext serviceContext) 
			throws PortalException, SystemException {

		String portalURL = serviceContext.getPortalURL();
		
		if(Validator.isNull(portalURL)) {
			Company company = CompanyLocalServiceUtil.getCompany(getUser().getCompanyId());
			
			portalURL = PortalUtil.getPortalURL(
				company.getVirtualHostname(), PortalUtil.getPortalPort(false), false);
		}
		
		String fileURL = portalURL + DLUtil.getPreviewURL(fileEntry, fileEntry.getFileVersion(), null, "");
		
		return fileURL;
	}
	
	private byte[] getFileFromURL(String fileURL) throws IOException {
		
		HttpURLConnection connection = null;
		byte[] bytes = null;
		if(Validator.isNotNull(fileURL)) {
			
			try {
				URL url = new URL(fileURL);
				
				connection = (HttpURLConnection) url.openConnection();
				connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
				connection.addRequestProperty("User-Agent", "Mozilla");
				connection.addRequestProperty("Referer", "google.com");
				
				connection.setInstanceFollowRedirects(false);
				connection.setConnectTimeout(5000);	// 5s
				connection.setReadTimeout(5000);	// 5s
				
				int status = connection.getResponseCode();
				
				boolean redirect = false;

				// normally, 3xx is redirect
				if (status != HttpURLConnection.HTTP_OK) {
					if (status == HttpURLConnection.HTTP_MOVED_TEMP
						|| status == HttpURLConnection.HTTP_MOVED_PERM
							|| status == HttpURLConnection.HTTP_SEE_OTHER)
					redirect = true;
				}
				
				if (redirect) {

					// get redirect url from "location" header field
					String newUrl = connection.getHeaderField("Location");

					// get the cookie if need, for login
					String cookies = connection.getHeaderField("Set-Cookie");

					// open the new connnection again
					connection = (HttpURLConnection) new URL(newUrl).openConnection();
					
					connection.setRequestProperty("Cookie", cookies);
					connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
					connection.addRequestProperty("User-Agent", "Mozilla");
					connection.addRequestProperty("Referer", "google.com");
					
					connection.setConnectTimeout(5000);	// 5s
					connection.setReadTimeout(5000);	// 5s
											
					status = connection.getResponseCode();
				}
			
				if(status == HttpURLConnection.HTTP_OK) {
					InputStream is = connection.getInputStream();
					//File file = FileUtil.createTempFile(is);
					//long size = connection.getContentLengthLong();
					//_log.info("===fileURL===" + fileURL + "===" + file.getAbsolutePath());
					//_log.info("===fileURL===" + fileURL + "===" + size);
					
					bytes = FileUtil.getBytes(is);
					
					//FileUtil.createTempFile(bytes);
				}
			} catch(IOException ioe) {
				throw new IOException(ioe.getMessage());
			}finally{
				connection.disconnect();
			}
		}
		
		return bytes;
	}
	
	private static Log _log = LogFactoryUtil.getLog(ApiServiceServiceImpl.class.getName());	
}