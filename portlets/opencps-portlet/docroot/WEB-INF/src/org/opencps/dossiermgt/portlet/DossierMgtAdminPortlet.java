
package org.opencps.dossiermgt.portlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.dossiermgt.DuplicateDossierPartNumberException;
import org.opencps.dossiermgt.DuplicateDossierPartSiblingException;
import org.opencps.dossiermgt.DuplicateDossierTemplateNumberException;
import org.opencps.dossiermgt.InvalidInWorkingUnitException;
import org.opencps.dossiermgt.InvalidServiceConfigGovCodeException;
import org.opencps.dossiermgt.InvalidServiceConfigGovNameException;
import org.opencps.dossiermgt.InvalidServiceDomainException;
import org.opencps.dossiermgt.NoSuchDossierPartException;
import org.opencps.dossiermgt.NoSuchDossierTemplateException;
import org.opencps.dossiermgt.OutOfLengthDossierPartNameException;
import org.opencps.dossiermgt.OutOfLengthDossierPartNumberException;
import org.opencps.dossiermgt.OutOfLengthDossierTemplateFileNumberException;
import org.opencps.dossiermgt.OutOfLengthDossierTemplateNameException;
import org.opencps.dossiermgt.OutOfLengthDossierTemplateNumberException;
import org.opencps.dossiermgt.OutOfLengthServiceConfigGovCodeException;
import org.opencps.dossiermgt.OutOfLengthServiceConfigGovNameException;
import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.model.DossierTemplate;
import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.dossiermgt.search.DossierPartDisplayTerms;
import org.opencps.dossiermgt.search.DossierTemplateDisplayTerms;
import org.opencps.dossiermgt.search.ServiceConfigDisplayTerms;
import org.opencps.dossiermgt.service.DossierPartLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil;
import org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil;
import org.opencps.dossiermgt.util.DossierMgtUtil;
import org.opencps.usermgt.model.WorkingUnit;
import org.opencps.usermgt.service.WorkingUnitLocalServiceUtil;
import org.opencps.util.MessageKeys;
import org.opencps.util.PortletConstants;
import org.opencps.util.PortletPropsValues;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.util.bridges.mvc.MVCPortlet;
/**
 * Portlet implementation class ServiceConfigMgtPortlet
 */
public class DossierMgtAdminPortlet extends MVCPortlet {

	@Override
	public void render(
	    RenderRequest renderRequest, RenderResponse renderResponse)
	    throws PortletException, IOException {

		long dossierTemplateId =
		    ParamUtil.getLong(
		        renderRequest,
		        DossierTemplateDisplayTerms.DOSSIERTEMPLATE_DOSSIERTEMPLATEID);

		long dossierPartId =
		    ParamUtil.getLong(
		        renderRequest,
		        DossierPartDisplayTerms.DOSSIERPART_DOSSIERPARTID);

		long serviceConfigId =
		    ParamUtil.getLong(
		        renderRequest,
		        ServiceConfigDisplayTerms.SERVICE_CONFIG_SERVICECONFIGID);

		try {

			if (dossierTemplateId > 0) {
				DossierTemplate dossierTemplate =
				    DossierTemplateLocalServiceUtil.fetchDossierTemplate(dossierTemplateId);
				renderRequest.setAttribute(
				    WebKeys.DOSSIER_TEMPLATE_ENTRY, dossierTemplate);
			}

			if (dossierPartId > 0) {
				DossierPart dossierPart =
				    DossierPartLocalServiceUtil.fetchDossierPart(dossierPartId);
				renderRequest.setAttribute(
				    WebKeys.DOSSIER_PART_ENTRY, dossierPart);
			}

			if (serviceConfigId > 0) {
				ServiceConfig serviceConfig =
				    ServiceConfigLocalServiceUtil.fetchServiceConfig(serviceConfigId);
				renderRequest.setAttribute(
				    WebKeys.SERVICE_CONFIG_ENTRY, serviceConfig);
			}

		}

		catch (Exception e) {
			_log.error(e);
		}

		super.render(renderRequest, renderResponse);
	}

	public void deleteDossierTemplate(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws SystemException, NoSuchDossierTemplateException, IOException {

		long dossierTemplateId =
		    ParamUtil.getLong(
		        actionRequest,
		        DossierTemplateDisplayTerms.DOSSIERTEMPLATE_DOSSIERTEMPLATEID);
		String currentURL = ParamUtil.getString(actionRequest, "CurrentURL");

		int dossierPartCount =
		    DossierPartLocalServiceUtil.CountByTempalteId(dossierTemplateId);

		if (dossierPartCount == 0) {
			DossierTemplateLocalServiceUtil.deleteDossierTemplateById(dossierTemplateId);
		}
		else {

			if (Validator.isNotNull(currentURL)) {
				SessionErrors.add(
				    actionRequest, MessageKeys.DOSSIER_TEMPLATE_DELETE_ERROR);
				actionResponse.sendRedirect(currentURL);
			}
		}
	}

	public void updateDossier(
	    ActionRequest actionRequest, ActionResponse actionResponse) throws IOException {

		long dossierTemplateId =
		    ParamUtil.getLong(
		        actionRequest,
		        DossierTemplateDisplayTerms.DOSSIERTEMPLATE_DOSSIERTEMPLATEID);
		String templateNo =
		    ParamUtil.getString(
		        actionRequest,
		        DossierTemplateDisplayTerms.DOSSIERTEMPLATE_TEMPLATENO);
		String templateName =
		    ParamUtil.getString(
		        actionRequest,
		        DossierTemplateDisplayTerms.DOSSIERTEMPLATE_TEMPLATENAME);
		String description =
		    ParamUtil.getString(
		        actionRequest,
		        DossierTemplateDisplayTerms.DOSSIERTEMPLATE_DESCRIPTION);
		String returnURL = ParamUtil.getString(actionRequest, "returnURL");

		try {
			
			ServiceContext serviceContext = ServiceContextFactory
						    .getInstance(actionRequest);
			
			dossierTemplateValidate(dossierTemplateId, templateNo, templateName);

			if (dossierTemplateId == 0) {
				DossierTemplateLocalServiceUtil.addDossierTemplate(
				    templateNo, templateName, description, serviceContext.getUserId(),
				    serviceContext);
			}
			else {
				DossierTemplateLocalServiceUtil.updateDossierTemplate(
				    dossierTemplateId, templateNo, templateName, description,
				    serviceContext.getUserId(),
				    serviceContext);
			}
		}
		catch (Exception e) {
			if (e instanceof OutOfLengthDossierTemplateNameException) {
				SessionErrors.add(actionRequest, OutOfLengthDossierTemplateNameException.class);
			} 
			else if(e instanceof OutOfLengthDossierTemplateNumberException) {
				SessionErrors.add(actionRequest, OutOfLengthDossierTemplateNumberException.class);
			} 
			else if(e instanceof DuplicateDossierTemplateNumberException) {
				SessionErrors.add(actionRequest, DuplicateDossierTemplateNumberException.class);
			} else {
				SessionErrors.add(actionRequest, MessageKeys.DOSSIER_SYSTEM_EXCEPTION_OCCURRED);
			}
			
			if(Validator.isNotNull(returnURL)) {
				actionResponse.sendRedirect(returnURL);
			}
		}
	}

	public void deleteDossierPart(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws NoSuchDossierPartException, SystemException, IOException {

		long dossierPartId =
		    ParamUtil.getLong(
		        actionRequest,
		        DossierPartDisplayTerms.DOSSIERPART_DOSSIERPARTID);
		String currentURL = ParamUtil.getString(actionRequest, "CurrentURL");
		int dossierPartParentCount =
		    DossierPartLocalServiceUtil.CountByParentId(dossierPartId);

		if (dossierPartParentCount == 0) {
			DossierPartLocalServiceUtil.deleteDossierPartById(dossierPartId);
		}
		else {
			if (Validator.isNotNull(currentURL)) {
				SessionErrors.add(
				    actionRequest, MessageKeys.DOSSIER_PART_DELETE_ERROR);
				actionResponse.sendRedirect(currentURL);
			}
		}

	}

	public void updateDossierPart(
	    ActionRequest actionRequest, ActionResponse actionResponse) throws IOException, PortalException, SystemException {

		long dossierPartId =
		    ParamUtil.getLong(
		        actionRequest,
		        DossierPartDisplayTerms.DOSSIERPART_DOSSIERPARTID);
		long parentId =
		    ParamUtil.getLong(
		        actionRequest, DossierPartDisplayTerms.DOSSIERPART_PARENTID);
		long dossierTemplateId =
		    ParamUtil.getLong(
		        actionRequest,
		        DossierPartDisplayTerms.DOSSIERPART_DOSSIERTEMPLATEID);
		String partNo =
		    ParamUtil.getString(
		        actionRequest, DossierPartDisplayTerms.DOSSIERPART_PARTNO);
		String partName =
		    ParamUtil.getString(
		        actionRequest, DossierPartDisplayTerms.DOSSIERPART_PARTNAME);
		String partTip =
		    ParamUtil.getString(
		        actionRequest, DossierPartDisplayTerms.DOSSIERPART_PARTTIP);
		String formScript =
		    ParamUtil.getString(
		        actionRequest, DossierPartDisplayTerms.DOSSIERPART_FORMSCRIPT);
		String sampleData =
		    ParamUtil.getString(
		        actionRequest, DossierPartDisplayTerms.DOSSIERPART_SAMPLEDATA);
		String templateFileNo =
		    ParamUtil.getString(
		        actionRequest,
		        DossierPartDisplayTerms.DOSSIERPART_TEMPLATEFILENO);
		String returnURL = ParamUtil.getString(actionRequest, "returnURL");
		
		String isAddChilds = ParamUtil.getString(actionRequest, "isAddChilds");
		
		int partType =
		    ParamUtil.getInteger(
		        actionRequest, DossierPartDisplayTerms.DOSSIERPART_PARTTYPE);
		double sibling =
		    ParamUtil.getDouble(
		        actionRequest, DossierPartDisplayTerms.DOSSIERPART_SIBLING);
		boolean required =
		    ParamUtil.getBoolean(
		        actionRequest, DossierPartDisplayTerms.DOSSIERPART_REQUIRED);
		
		
		/*for(DossierPart dos : DossierMgtUtil.getTreeDossierPart(dossierPartId, new ArrayList<DossierPart>())) {
			System.out.println("dos : " + dos.getTreeIndex());
		}*/
		try {
			
			ServiceContext serviceContext = ServiceContextFactory
						    .getInstance(actionRequest);
			
			dossierPartValidate(dossierPartId, partName, partNo, sibling, templateFileNo);
			if (dossierPartId == 0) {
				DossierPartLocalServiceUtil.addDossierPart(
				    dossierTemplateId, partNo, partName, partTip, partType,
				    parentId, sibling, formScript, sampleData, required,
				    templateFileNo, serviceContext.getUserId(), serviceContext);
			}
			else {

				if (Validator.isNotNull(isAddChilds)) {
					DossierPartLocalServiceUtil.addDossierPart(
					    dossierTemplateId, partNo, partName, partTip, partType,
					    parentId, sibling, formScript, sampleData, required,
					    templateFileNo, serviceContext.getUserId(), serviceContext);
				}
				else {
					DossierPartLocalServiceUtil.updateDossierPart(
					    dossierPartId, dossierTemplateId, partNo, partName,
					    partTip, partType, parentId, sibling, formScript,
					    sampleData, required, templateFileNo, 
					    serviceContext.getUserId(), serviceContext);
				}

			}
		}
		catch (Exception e) {
			if(e instanceof OutOfLengthDossierPartNameException) {
				SessionErrors
				.add(actionRequest, OutOfLengthDossierPartNameException.class);
			} 
			else if(e instanceof OutOfLengthDossierPartNumberException) {
				SessionErrors
				.add(actionRequest, OutOfLengthDossierPartNumberException.class);
			}
			else if(e instanceof OutOfLengthDossierTemplateFileNumberException) {
				SessionErrors
				.add(actionRequest, OutOfLengthDossierTemplateFileNumberException.class);
			}
			else if(e instanceof DuplicateDossierPartNumberException) {
				SessionErrors
				.add(actionRequest, DuplicateDossierPartNumberException.class);
			}
			
			else if(e instanceof DuplicateDossierPartSiblingException) {
				SessionErrors
				.add(actionRequest, DuplicateDossierPartSiblingException.class);
			} 
			
			else {
				SessionErrors.add(actionRequest, MessageKeys.DOSSIER_SYSTEM_EXCEPTION_OCCURRED);
			}
			
			if(Validator.isNotNull(returnURL)) {
				actionResponse.sendRedirect(returnURL);
			}
		}

	}

	public void deleteServiceConfig(ActionRequest actionRequest, ActionResponse actionResponse) 
					throws PortalException, SystemException {
		long serviceConfigId = ParamUtil.getLong(actionRequest, 
			ServiceConfigDisplayTerms.SERVICE_CONFIG_SERVICECONFIGID);
		ServiceConfigLocalServiceUtil.deleteServiceConfig(serviceConfigId);
	}
	
	public void updateServiceConfig(
	    ActionRequest actionRequest, ActionResponse actionResponse) throws IOException {

		long serviceConfigId =
		    ParamUtil.getLong(
		        actionRequest,
		        ServiceConfigDisplayTerms.SERVICE_CONFIG_SERVICECONFIGID);
		long serviceInfoId =
		    ParamUtil.getLong(
		        actionRequest,
		        ServiceConfigDisplayTerms.SERVICE_CONFIG_SERVICEINFOID);
		long dossierTemplateId =
		    ParamUtil.getLong(
		        actionRequest,
		        ServiceConfigDisplayTerms.SERVICE_CONFIG_DOSSIERTEMPLATEID);
		long domainCode =
		    ParamUtil.getLong(
		        actionRequest,
		        ServiceConfigDisplayTerms.SERVICE_CONFIG_DOMAINCODE);
		String govAgencyCode =
		    ParamUtil.getString(
		        actionRequest,
		        ServiceConfigDisplayTerms.SERVICE_CONFIG_GOVAGENCYCODE);
		String govAgencyName =
		    ParamUtil.getString(
		        actionRequest,
		        ServiceConfigDisplayTerms.SERVICE_CONFIG_GOVAGENCYNAME);
		String returnURL = ParamUtil.getString(actionRequest, "returnURL");
		
		int serviceMode =
		    ParamUtil.getInteger(
		        actionRequest,
		        ServiceConfigDisplayTerms.SERVICE_CONFIG_SERVICEMODE);

		String serviceDomainIndex = StringPool.BLANK;
		String serviceAdministrationIndex = StringPool.BLANK;

		try {
			
			ServiceContext serviceContext = ServiceContextFactory
						    .getInstance(actionRequest);
			serviceConfigValidate(serviceConfigId, govAgencyCode,
				govAgencyName, domainCode, serviceContext, serviceMode);
			
			if (serviceConfigId == 0) {
				ServiceConfigLocalServiceUtil.addServiceConfig(
				    serviceInfoId, serviceAdministrationIndex,
				    serviceDomainIndex, dossierTemplateId, govAgencyCode,
				    govAgencyName, serviceMode, String.valueOf(domainCode),
				    serviceContext.getUserId(), serviceContext);
			}
			else {
				ServiceConfigLocalServiceUtil.updateServiceConfig(
				    serviceConfigId, serviceInfoId, serviceAdministrationIndex,
				    serviceDomainIndex, dossierTemplateId, govAgencyCode,
				    govAgencyName, serviceMode, String.valueOf(domainCode),
				    serviceContext.getUserId(), serviceContext);
			}
		}
		catch (Exception e) {
			if(e instanceof OutOfLengthServiceConfigGovCodeException) {
				SessionErrors.add(actionRequest, OutOfLengthServiceConfigGovCodeException.class);
			} 
			else if(e instanceof OutOfLengthServiceConfigGovNameException) {
				SessionErrors.add(actionRequest, OutOfLengthServiceConfigGovNameException.class);
			} 
			else if(e instanceof InvalidServiceConfigGovCodeException) {
				SessionErrors.add(actionRequest, InvalidServiceConfigGovCodeException.class);
			} 
			else if(e instanceof InvalidServiceConfigGovNameException) {
				SessionErrors.add(actionRequest, InvalidServiceConfigGovNameException.class);
			} 
			else if(e instanceof InvalidServiceDomainException) {
				SessionErrors.add(actionRequest, InvalidServiceDomainException.class);
			} 
			else if(e instanceof InvalidInWorkingUnitException) {
				SessionErrors.add(actionRequest, InvalidInWorkingUnitException.class);
			} 
			else {
				SessionErrors.add(actionRequest, MessageKeys.DOSSIER_SYSTEM_EXCEPTION_OCCURRED);
			}
			
			if(Validator.isNotNull(returnURL)) {
				actionResponse.sendRedirect(returnURL);
			}
		}

	}
	
	protected void serviceConfigValidate(long serviceConfigId, String govAgencyCode,
		String govAgencyName, long domainCode, ServiceContext serviceContext, int serviceMode) 
						throws OutOfLengthServiceConfigGovCodeException,
						OutOfLengthServiceConfigGovNameException,
						InvalidServiceConfigGovCodeException,
						InvalidServiceConfigGovNameException,
						InvalidServiceDomainException, InvalidInWorkingUnitException {
		
		WorkingUnit workingUnit = null;
		
		try {
	        workingUnit = WorkingUnitLocalServiceUtil
	        				.getWorkingUnit(serviceContext.getScopeGroupId(), govAgencyCode);
        }
        catch (Exception e) {
	        // nothing to do
        }
		
		if(govAgencyCode.length() > PortletPropsValues.DOSSIERMGT_SERVICE_CONFIG_GOVCODE_LENGTH) {
			throw new OutOfLengthServiceConfigGovCodeException();
		}
		else if(govAgencyName.length() > PortletPropsValues.DOSSIERMGT_SERVICE_CONFIG_GOVNAME_LENGTH) {
			throw new OutOfLengthServiceConfigGovNameException();
		}
		
		else if(Validator.equals(govAgencyCode, StringPool.BLANK)) {
			throw new InvalidServiceConfigGovCodeException();
		}

		else if(Validator.equals(govAgencyName, StringPool.BLANK)) {
			throw new InvalidServiceConfigGovNameException();
		}
		
		else if(domainCode == 0) {
			throw new InvalidServiceDomainException();
		}
		else if(serviceMode == PortletConstants.SERVICE_CONFIG_BACKOFFICE ||
				serviceMode ==	PortletConstants.SERVICE_CONFIG_FRONT_BACK_OFFICE) {
			if(workingUnit == null) {
				throw new InvalidInWorkingUnitException();
			}
		}
		
	}
	
	protected void dossierPartValidate(long dossierPartId ,String partName, String partNo, double sibling,
		String templateFileNo) throws OutOfLengthDossierPartNameException, 
		OutOfLengthDossierPartNumberException, OutOfLengthDossierTemplateFileNumberException,
		DuplicateDossierPartNumberException, DuplicateDossierPartSiblingException {
		DossierPart dossierPartNo = null;
		DossierPart dossierPartSibling = null;
		
		try {
	        dossierPartNo = DossierPartLocalServiceUtil
	        				.getDossierPartByPartNo(partNo);
	        dossierPartSibling = DossierPartLocalServiceUtil
	        				.getDossierPartBySibling(sibling);
		}
        catch (Exception e) {
	        // nothing to do
        }
		
		if(partName.length() > PortletPropsValues.DOSSIERMGT_PART_NAME_LENGTH) {
			throw new OutOfLengthDossierPartNameException();
		}
		else if(partNo.length() > PortletPropsValues.DOSSIERMGT_PART_NUMBER_LENGTH) {
			throw new OutOfLengthDossierPartNumberException();
		}
		else if(templateFileNo.length() > PortletPropsValues.DOSSIERMGT_PART_TEMPLATE_FILE_NUMBER_LENGTH){
			throw new OutOfLengthDossierTemplateFileNumberException();
		} 
		//add dossier
		else if(dossierPartId == 0 && Validator.isNotNull(dossierPartNo)) {
			throw new DuplicateDossierPartNumberException();
		}
		//update dossier
		else if(dossierPartId > 0 && Validator.isNotNull(dossierPartNo) 
						&& Validator.equals(dossierPartNo.getDossierpartId(), dossierPartId)) {
			throw new DuplicateDossierPartNumberException();
		}
		
		else if(dossierPartId == 0 && Validator.isNotNull(dossierPartSibling)) {
			throw new DuplicateDossierPartSiblingException();
		}
		
		else if (dossierPartId > 0 && Validator.isNotNull(dossierPartSibling) 
						&& Validator.equals(dossierPartSibling.getDossierpartId(), dossierPartId)) {
			throw new DuplicateDossierPartSiblingException();
		}
			
		
	}
	
	protected void dossierTemplateValidate(
	    long dossierTemplateId, String templateNo, String templateName)
	    throws OutOfLengthDossierTemplateNameException,
	    OutOfLengthDossierTemplateNumberException,
	    DuplicateDossierTemplateNumberException {

		DossierTemplate dossierTemplate = null;

		try {
			dossierTemplate =
			    DossierTemplateLocalServiceUtil.getDossierTemplate(templateNo);
		}
		catch (Exception e) {
			// nothing to do
		}

		if (templateName.length() > PortletPropsValues.DOSSIERMGT_TEMPLATE_NAME_LENGTH) {
			throw new OutOfLengthDossierTemplateNameException();
		}

		else if (templateNo.length() > PortletPropsValues.DOSSIERMGT_TEMPLATE_NAME_LENGTH) {
			throw new OutOfLengthDossierTemplateNumberException();
		}
		// add dossier template
		else if (dossierTemplate != null && dossierTemplateId == 0) {
			throw new DuplicateDossierTemplateNumberException();
		}
		// update dossier template
		else if (dossierTemplateId > 0 &&
		    dossierTemplate != null &&
		    Validator.equals(
		        dossierTemplate.getDossierTemplateId(), dossierTemplateId)) {
			throw new DuplicateDossierTemplateNumberException();
		}
	}
	private Log _log =
	    LogFactoryUtil.getLog(DossierMgtAdminPortlet.class.getName());
}
