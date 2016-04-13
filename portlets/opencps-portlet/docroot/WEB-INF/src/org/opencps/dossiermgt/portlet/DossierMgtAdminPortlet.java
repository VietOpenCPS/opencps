
package org.opencps.dossiermgt.portlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.model.DossierTemplate;
import org.opencps.dossiermgt.search.DossierPartDisplayTerms;
import org.opencps.dossiermgt.search.DossierTemplateDisplayTerms;
import org.opencps.dossiermgt.service.DossierPartLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
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
				renderRequest.setAttribute(WebKeys.BUSINESS_ENTRY, dossierPart);
			}

		}

		catch (Exception e) {
			_log.error(e);
		}

		super.render(renderRequest, renderResponse);
	}

	public void updateDossier(
	    ActionRequest actionRequest, ActionResponse actionResponse) {

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

		try {
			if (dossierTemplateId == 0) {
				DossierTemplateLocalServiceUtil.addDossierTemplate(
				    templateNo, templateName, description);
			}
			else {
				DossierTemplateLocalServiceUtil.updateDossierTemplate(
				    dossierTemplateId, templateNo, templateName, description);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			// for validate exception
		}
	}

	public void updateDossierPart(
	    ActionRequest actionRequest, ActionResponse actionResponse) {

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
		int partType =
		    ParamUtil.getInteger(
		        actionRequest, DossierPartDisplayTerms.DOSSIERPART_PARTTYPE);
		double sibling =
		    ParamUtil.getDouble(
		        actionRequest, DossierPartDisplayTerms.DOSSIERPART_SIBLING);
		boolean required =
		    ParamUtil.getBoolean(
		        actionRequest, DossierPartDisplayTerms.DOSSIERPART_REQUIRED);

		try {
			if (dossierPartId == 0) {
				DossierPartLocalServiceUtil.addDossierPart(
				    dossierTemplateId, partName, partTip, partType, parentId,
				    sibling, formScript, sampleData, required, templateFileNo);
			}
			else {
				DossierPartLocalServiceUtil.updateDossierPart(
				    dossierPartId, dossierTemplateId, partName, partTip,
				    partType, parentId, sibling, formScript, sampleData,
				    required, templateFileNo);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//to validate exception
		}

	}
	private Log _log =
	    LogFactoryUtil.getLog(DossierMgtAdminPortlet.class.getName());
}
