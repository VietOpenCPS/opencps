/**
 * OpenCPS is the open source Core Public Services software Copyright (C)
 * 2016-present OpenCPS community This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3
 * of the License, or any later version. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details. You should have received a
 * copy of the GNU Affero General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>
 */
package org.opencps.servicemgt.portlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.datamgt.DuplicateItemException;
import org.opencps.datamgt.EmptyDictItemNameException;
import org.opencps.datamgt.EmptyItemCodeException;
import org.opencps.datamgt.NoSuchDictItemException;
import org.opencps.datamgt.OutOfLengthItemCodeException;
import org.opencps.datamgt.OutOfLengthItemNameException;
import org.opencps.datamgt.model.DictCollection;
import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.portlet.DataMamagementPortlet;
import org.opencps.datamgt.search.DictItemDisplayTerms;
import org.opencps.datamgt.service.DictCollectionLocalServiceUtil;
import org.opencps.datamgt.service.DictItemLocalServiceUtil;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.servicemgt.DuplicateFileNameException;
import org.opencps.servicemgt.DuplicateFileNoException;
import org.opencps.servicemgt.IOFileUploadException;
import org.opencps.servicemgt.model.ServiceInfo;
import org.opencps.servicemgt.model.TemplateFile;
import org.opencps.servicemgt.search.ServiceDisplayTerms;
import org.opencps.servicemgt.service.ServiceFileTemplateLocalServiceUtil;
import org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil;
import org.opencps.servicemgt.service.TemplateFileLocalServiceUtil;
import org.opencps.util.MessageKeys;
import org.opencps.util.PortletPropsValues;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author khoavd
 */
public class ServiceMgtPortlet extends MVCPortlet {
	
	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void deleteDomain(ActionRequest actionRequest, ActionResponse actionResponse) throws IOException { 
		long dictItemId = ParamUtil.getLong(actionRequest,
				DictItemDisplayTerms.DICTITEM_ID, 0L);
		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");
		try {
			DictItemLocalServiceUtil.deleteDictItem(dictItemId);
		} catch (Exception e) {
			SessionErrors.add(actionRequest,
					MessageKeys.DATAMGT_SYSTEM_EXCEPTION_OCCURRED);
			_log.error(e);
		} finally {
			if (Validator.isNotNull(redirectURL)) {
				actionResponse.sendRedirect(redirectURL);
			}
		}
	}
	
	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void updateAdministration(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException {

		long dictItemId =
			ParamUtil.getLong(actionRequest, DictItemDisplayTerms.DICTITEM_ID);
		long parentItemId =
			ParamUtil.getLong(actionRequest, DictItemDisplayTerms.PARENTITEM_ID);
		String itemName =
			ParamUtil.getString(actionRequest, DictItemDisplayTerms.ITEM_NAME);
		String itemCode =
			ParamUtil.getString(actionRequest, DictItemDisplayTerms.ITEM_CODE);
		String isAddchirld = ParamUtil.getString(actionRequest, "isAddchirld");
		String backURL = ParamUtil.getString(actionRequest, "backURL");
		String currentURL = ParamUtil.getString(actionRequest, "currentURL");

		Map<Locale, String> itemNameMap =
			LocalizationUtil.getLocalizationMap(
				actionRequest, DictItemDisplayTerms.ITEM_NAME);
		DictCollection dictCollection = null;

		try {

			ServiceContext serviceContext =
				ServiceContextFactory.getInstance(actionRequest);
			dictCollection =
				DictCollectionLocalServiceUtil.getDictCollection(
					serviceContext.getScopeGroupId(),
					PortletPropsValues.DATAMGT_MASTERDATA_SERVICE_ADMINISTRATION);
			DataMamagementPortlet.validatetDictItem(
				dictItemId, itemName, itemCode, serviceContext);
			if (dictItemId == 0) {
				DictItemLocalServiceUtil.addDictItem(
					serviceContext.getUserId(),
					dictCollection.getDictCollectionId(), itemCode,
					itemNameMap, parentItemId, serviceContext);
				SessionMessages.add(
					actionRequest, MessageKeys.DATAMGT_ADD_SUCESS);
			}
			else {

				if (Validator.isNotNull(isAddchirld)) {
					DictItemLocalServiceUtil.addDictItem(
						serviceContext.getUserId(),
						dictCollection.getDictCollectionId(), itemCode,
						itemNameMap, parentItemId, serviceContext);
					SessionMessages.add(
						actionRequest, MessageKeys.DATAMGT_ADD_SUCESS);
				}
				else {
					// tam thoi hard code dictversionId = 0

					DictItemLocalServiceUtil.updateDictItem(
						dictItemId, dictCollection.getDictCollectionId(), 0,
						itemCode, itemNameMap, parentItemId, serviceContext);
					SessionMessages.add(
						actionRequest, MessageKeys.DATAMGT_UPDATE_SUCESS);
				}
			}

			if (Validator.isNotNull(backURL)) {
				actionResponse.sendRedirect(backURL);
			}
		}
		catch (Exception e) {
			if (e instanceof EmptyItemCodeException) {
				SessionErrors.add(actionRequest, EmptyItemCodeException.class);
			}
			else if (e instanceof OutOfLengthItemCodeException) {
				SessionErrors.add(
					actionRequest, OutOfLengthItemCodeException.class);
			}
			else if (e instanceof EmptyDictItemNameException) {
				SessionErrors.add(
					actionRequest, EmptyDictItemNameException.class);
			}
			else if (e instanceof OutOfLengthItemNameException) {
				SessionErrors.add(
					actionRequest, OutOfLengthItemNameException.class);
			}
			else if (e instanceof DuplicateItemException) {
				SessionErrors.add(actionRequest, DuplicateItemException.class);
			}
			else if (e instanceof NoSuchDictItemException) {
				SessionErrors.add(actionRequest, NoSuchDictItemException.class);
			}
			else {
				SessionErrors.add(
					actionRequest,
					MessageKeys.DATAMGT_SYSTEM_EXCEPTION_OCCURRED);
			}

			if (Validator.isNotNull(currentURL)) {
				actionResponse.sendRedirect(currentURL);
			}
		}
	}
	
	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void updateDomain(ActionRequest actionRequest, ActionResponse actionResponse) throws IOException {
		long dictItemId = ParamUtil.getLong(actionRequest, DictItemDisplayTerms.DICTITEM_ID);
		long parentItemId = ParamUtil.getLong(actionRequest, DictItemDisplayTerms.PARENTITEM_ID);
		String itemName = ParamUtil.getString(actionRequest, DictItemDisplayTerms.ITEM_NAME);
		String itemCode = ParamUtil.getString(actionRequest, DictItemDisplayTerms.ITEM_CODE);
		String isAddchirld = ParamUtil.getString(actionRequest, "isAddchirld");
		String backURL = ParamUtil.getString(actionRequest, "backURL");
		String currentURL = ParamUtil.getString(actionRequest, "currentURL");
		
		Map<Locale, String> itemNameMap = LocalizationUtil.getLocalizationMap(
			actionRequest, DictItemDisplayTerms.ITEM_NAME);
		DictCollection dictCollection = null;
		
		try {
			
			ServiceContext serviceContext = ServiceContextFactory
						    .getInstance(actionRequest);
	        dictCollection = DictCollectionLocalServiceUtil
	        				.getDictCollection(serviceContext.getScopeGroupId(), PortletPropsValues.DATAMGT_MASTERDATA_SERVICE_DOMAIN);
	        DataMamagementPortlet.validatetDictItem(dictItemId, itemName, itemCode, serviceContext);
	        if(dictItemId == 0) {
	        	DictItemLocalServiceUtil.addDictItem(serviceContext.getUserId(), dictCollection.getDictCollectionId(),
	        		itemCode, itemNameMap, parentItemId, serviceContext);
	        	SessionMessages.add(actionRequest,
					MessageKeys.DATAMGT_ADD_SUCESS);
	        } else {
	        	
	        	if(Validator.isNotNull(isAddchirld)) {
	        		DictItemLocalServiceUtil.addDictItem(serviceContext.getUserId(), dictCollection.getDictCollectionId(),
		        		itemCode, itemNameMap, parentItemId, serviceContext);
	        		SessionMessages.add(actionRequest,
						MessageKeys.DATAMGT_ADD_SUCESS);
	        	} else {
	        		//tam thoi hard code dictversionId = 0
	        		
	        		DictItemLocalServiceUtil.updateDictItem(dictItemId, dictCollection.getDictCollectionId(), 0,
	        			itemCode, itemNameMap, parentItemId, serviceContext);
	        		SessionMessages.add(actionRequest,
						MessageKeys.DATAMGT_UPDATE_SUCESS);
	        	}
	        }
	        
	        if(Validator.isNotNull(backURL)) {
	        	actionResponse.sendRedirect(backURL);
	        }
        }
        catch (Exception e) {
        	if (e instanceof EmptyItemCodeException) {
				SessionErrors.add(actionRequest, EmptyItemCodeException.class);
			} else if (e instanceof OutOfLengthItemCodeException) {
				SessionErrors.add(actionRequest,
						OutOfLengthItemCodeException.class);
			} else if (e instanceof EmptyDictItemNameException) {
				SessionErrors.add(actionRequest,
						EmptyDictItemNameException.class);
			} else if (e instanceof OutOfLengthItemNameException) {
				SessionErrors.add(actionRequest,
						OutOfLengthItemNameException.class);
			} else if (e instanceof DuplicateItemException) {
				SessionErrors.add(actionRequest, DuplicateItemException.class);
			} else if (e instanceof NoSuchDictItemException) {
				SessionErrors.add(actionRequest, NoSuchDictItemException.class);
			} else {
				SessionErrors.add(actionRequest,
						MessageKeys.DATAMGT_SYSTEM_EXCEPTION_OCCURRED);
			}
        	
        	if(Validator.isNotNull(currentURL)) {
	        	actionResponse.sendRedirect(currentURL);
	        }
        }
	}
	/**
	 * @param renderRequest
	 * @param renderResponse
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public ServiceInfo updateService(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {

		ServiceDisplayTerms displayTerms =
		    new ServiceDisplayTerms(actionRequest);

		long serviceInfoId = displayTerms.getServiceId();

		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");
		String returnURL = ParamUtil.getString(actionRequest, "returnURL");

		SessionMessages.add(
		    actionRequest, PortalUtil.getPortletId(actionRequest) +
		        SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		String urlOnline = ParamUtil.getString(actionRequest, "urlOnline");
		String onlineURL = Validator.isNotNull(urlOnline) ? urlOnline : displayTerms.getOnlineUrl();
		try {
			ServiceContext serviceContext =
			    ServiceContextFactory.getInstance(actionRequest);

			if (serviceInfoId <= 0) {

				// TODO: Validator in here

				// Add ServiceInfo

				ServiceInfoLocalServiceUtil.addService(
				    displayTerms.getServiceNo(), displayTerms.getServiceName(),
				    displayTerms.getFullName(),
				    displayTerms.getServiceProcess(),
				    displayTerms.getServiceMethod(),
				    displayTerms.getServiceDossier(),
				    displayTerms.getServiceCondition(),
				    displayTerms.getServiceDuration(),
				    displayTerms.getServiceActors(),
				    displayTerms.getServiceResults(),
				    displayTerms.getServiceRecords(),
				    displayTerms.getServiceFee(),
				    displayTerms.getServiceInstructions(),
				    displayTerms.getAdministrationCode(),
				    displayTerms.getAdministrationIndex(),
				    displayTerms.getDomainCode(),
				    displayTerms.getDomainIndex(),
				    displayTerms.getActiveStatus(),
				    onlineURL,
				    displayTerms.setFileTemplateIds(actionRequest), serviceContext);

				// Redirect page

				if (Validator.isNotNull(redirectURL)) {
					actionResponse.sendRedirect(redirectURL);
				}

			}
			else {
				// TODO: Validator in here
				
				// Update ServiceInfo
				ServiceInfoLocalServiceUtil.updateService(
				    serviceInfoId, displayTerms.getServiceNo(),
				    displayTerms.getServiceName(), displayTerms.getFullName(),
				    displayTerms.getServiceProcess(),
				    displayTerms.getServiceMethod(),
				    displayTerms.getServiceDossier(),
				    displayTerms.getServiceCondition(),
				    displayTerms.getServiceDuration(),
				    displayTerms.getServiceActors(),
				    displayTerms.getServiceResults(),
				    displayTerms.getServiceRecords(),
				    displayTerms.getServiceFee(),
				    displayTerms.getServiceInstructions(),
				    displayTerms.getAdministrationCode(),
				    displayTerms.getAdministrationIndex(),
				    displayTerms.getDomainCode(),
				    displayTerms.getDomainIndex(), onlineURL,
				    displayTerms.setFileTemplateIds(actionRequest), serviceContext);
				
				// Redirect page
				if (Validator.isNotNull(redirectURL)) {
					actionResponse.sendRedirect(redirectURL);
				}
			}

		}
		catch (Exception e) {
			if (Validator.isNotNull(returnURL)) {
				actionResponse.sendRedirect(returnURL);
			}
		}

		return null;
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 * @throws IOException
	 */
	public void updateTempalteFile(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {

		long templateFileId =
		    ParamUtil.getLong(actionRequest, "templateFileId");
		
		String fileNo = ParamUtil.getString(actionRequest, "fileNo");
		String fileName = ParamUtil.getString(actionRequest, "fileName");

		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");
		String returnURL = ParamUtil.getString(actionRequest, "returnURL");
		
		SessionMessages.add(
		    actionRequest, PortalUtil.getPortletId(actionRequest) +
		        SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		

		try {
			ServiceContext serviceContext =
			    ServiceContextFactory.getInstance(actionRequest);
			
			serviceContext.setAddGuestPermissions(true);

			if (templateFileId <= 0) {

					if (TemplateFileLocalServiceUtil.isDuplicateFileName(
					    templateFileId, serviceContext.getScopeGroupId(), fileName)) {
						
					}

					if (TemplateFileLocalServiceUtil.isDuplicateFileNo(
						templateFileId, serviceContext.getScopeGroupId(), fileNo)) {
						
					}

					TemplateFileLocalServiceUtil.addServiceTemplateFile(
					    fileNo, fileName, actionRequest, actionResponse, 
					    serviceContext);
					SessionMessages.add(
					    actionRequest,
					    MessageKeys.SERVICE_TEMPLATE_FILE_UPDATE_SUCESS);

			}
			else {
								
				if (TemplateFileLocalServiceUtil.isDuplicateFileName(
				    templateFileId, serviceContext.getScopeGroupId(), fileName)) {
					
				}

				if (TemplateFileLocalServiceUtil.isDuplicateFileNo(
					templateFileId, serviceContext.getScopeGroupId(), fileNo)) {
					
				}
				
				TemplateFileLocalServiceUtil.updateServiceTemplateFile(
				    templateFileId, fileNo, fileName,actionRequest,actionResponse,
				    serviceContext);
				SessionMessages.add(
				    actionRequest,
				    MessageKeys.SERVICE_TEMPLATE_FILE_UPDATE_SUCESS);
			}

			if (Validator.isNotNull(redirectURL)) {
				actionResponse.sendRedirect(redirectURL);
			}
		}
		catch (Exception e) {
			if (e instanceof DuplicateFileNameException) {
				SessionErrors.add(
				    actionRequest,
				    MessageKeys.SERVICE_TEMPLATE_FILE_NAME_EXCEPTION);
			}
			else if (e instanceof DuplicateFileNoException) {
				SessionErrors.add(
				    actionRequest,
				    MessageKeys.SERVICE_TEMPLATE_FILE_NO_EXCEPTION);

			}
			else if (e instanceof IOFileUploadException) {
				SessionErrors.add(
				    actionRequest,
				    MessageKeys.SERVICE_TEMPLATE_UPLOAD_EXCEPTION);
			}
			else {
				SessionErrors.add(
				    actionRequest,
				    MessageKeys.SERVICE_TEMPLATE_EXCEPTION_OCCURRED);
			}

			if (Validator.isNotNull(returnURL)) {
				actionResponse.sendRedirect(returnURL);
			}
		}

	}
	
	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws PortalException
	 * @throws SystemException
	 * @throws IOException
	 */
	public void chooseServiceInfo(ActionRequest actionRequest, ActionResponse actionResponse) 
					throws PortalException, SystemException, IOException {
		long templatefileId = ParamUtil.getLong(actionRequest, "templateFileId");
		long [] serviceInfoIds = ParamUtil
					    .getLongValues(actionRequest, "rowIds");
		String backURL = ParamUtil.getString(actionRequest, "backURL");
		TemplateFileLocalServiceUtil.addChooseServceInfo(templatefileId, serviceInfoIds);
		if(Validator.isNotNull(backURL)) {
			actionResponse.sendRedirect(backURL);
		}
		
	}
	
	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws PortalException
	 * @throws SystemException
	 * @throws IOException
	 */
	public void deteleRelaSeInfoAndTempFile (ActionRequest actionRequest, ActionResponse actionResponse)
					throws PortalException, SystemException, IOException{
		long templatefileId = ParamUtil.getLong(actionRequest, "templateFileId");
		long serviceInfoId = ParamUtil.getLong(actionRequest, "serviceInfoId");
		String backURL = ParamUtil.getString(actionRequest, "backURL");
		ServiceFileTemplateLocalServiceUtil.deleteServiceFile(serviceInfoId, templatefileId);
		if(Validator.isNotNull(backURL)) {
			actionResponse.sendRedirect(backURL);
		}
		
	}
	
	public void deleteTemplate (ActionRequest actionRequest, ActionResponse actionResponse)
					throws PortalException, SystemException, IOException {
		long templateId = ParamUtil.getLong(actionRequest, "templateId");
		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");
		TemplateFileLocalServiceUtil.deleteServiceTemplateFile(templateId);
		
		if(Validator.isNotNull(redirectURL)) {
			actionResponse.sendRedirect(redirectURL);
		}
	}
	
	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException 
	 * @throws SystemException 
	 * @throws PortalException 
	 */
	public void deleteService(ActionRequest actionRequest , ActionResponse actionResponse) throws IOException, PortalException, SystemException {
		long serviceinfoId = ParamUtil.getLong(actionRequest, ServiceDisplayTerms.SERVICE_ID);
		
		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");
		
		ServiceConfig serviceConfig = null;
		java.util.List<Dossier> dossier = new ArrayList<Dossier>();
		java.util.List<ProcessOrder> processOrder = new ArrayList<ProcessOrder>(); 
		try {
			ServiceContext serviceContext = ServiceContextFactory
						    .getInstance(actionRequest);
			serviceConfig = ServiceConfigLocalServiceUtil
							.getServiceConfigByG_S(serviceContext.getScopeGroupId(), serviceinfoId);
			dossier =  DossierLocalServiceUtil.getDossiersByServiceInfo(serviceinfoId);
			processOrder = ProcessOrderLocalServiceUtil.getProcessOrdersByServiceInfoId(serviceinfoId);
		}
		catch (Exception e) {
			// nothing to do
		}
		
		if(serviceConfig != null) {
			SessionErrors.add(actionRequest, MessageKeys.SERVICE_DELERR_EXITS_SERVICECONFIG);
		} else if(!dossier.isEmpty() ) {
			SessionErrors.add(actionRequest, MessageKeys.SERVICE_DELERR_EXITS_DOSSIER);
		} else if(!processOrder.isEmpty()) {
			SessionErrors.add(actionRequest, MessageKeys.SERVICE_DELERR_EXITS_PROCESSORDER);
		} else {
			ServiceInfoLocalServiceUtil.deleteService(serviceinfoId);
			SessionMessages.add(actionRequest, MessageKeys.SERVICE_DELSUCC);
		}
		
		if(Validator.isNotNull(redirectURL)){
			actionResponse.sendRedirect(redirectURL);
		}
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see javax.portlet.GenericPortlet#render(javax.portlet.RenderRequest,
	 * javax.portlet.RenderResponse)
	 */
	@Override
	public void render(
	    RenderRequest renderRequest, RenderResponse renderResponse)
	    throws PortletException, IOException {

		long serviceinfoId = ParamUtil.getLong(renderRequest, "serviceinfoId");
		
		long dictItemId = ParamUtil.getLong(renderRequest, DictItemDisplayTerms.DICTITEM_ID);

		long templatefileId =
		    ParamUtil.getLong(renderRequest, "templateFileId");

		TemplateFile templateFile = null;

		ServiceInfo serviceInfo = null;
		
		DictItem dictItem = null;

		try {
			
			if(dictItemId > 0) {
				dictItem = DictItemLocalServiceUtil.getDictItem(dictItemId);
				renderRequest.setAttribute(WebKeys.DICT_ITEM_ENTRY, dictItem);
			}
			
			templateFile =
			    TemplateFileLocalServiceUtil.fetchTemplateFile(templatefileId);

			serviceInfo =
			    ServiceInfoLocalServiceUtil.fetchServiceInfo(serviceinfoId);
		}
		catch (Exception e) {
			_log.info(e);
		}

		renderRequest.setAttribute(WebKeys.SERVICE_ENTRY, serviceInfo);
		renderRequest.setAttribute(WebKeys.SERVICE_TEMPLATE_ENTRY, templateFile);

		super.render(renderRequest, renderResponse);

	}

	/**
	 * Upload file
	 * 
	 * @param actionRequest
	 * @param actionResponse
	 * @return
	 * @throws Exception
	 */

	private static final Log _log =
	    LogFactoryUtil.getLog(ServiceMgtPortlet.class);
}
