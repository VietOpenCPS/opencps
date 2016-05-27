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

package org.opencps.paymentmgt.portlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.opencps.paymentmgt.NoSuchPaymentConfigException;
import org.opencps.paymentmgt.model.PaymentConfig;
import org.opencps.paymentmgt.search.PaymentConfigDisplayTerms;
import org.opencps.paymentmgt.service.PaymentConfigLocalServiceUtil;
import org.opencps.util.MessageKeys;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author trungdk
 */
public class PaymentMgtPaymentConfigPortlet extends MVCPortlet {

	@Override
	public void serveResource(
	    ResourceRequest resourceRequest, ResourceResponse resourceResponse)
	    throws IOException, PortletException {

		long govAgencyOrganizationId =
		    ParamUtil.getLong(
		        resourceRequest,
		        PaymentConfigDisplayTerms.GOV_AGENCY_ORGANIZATION_ID);
		PaymentConfig pc = null;
		ThemeDisplay themeDisplay =
			(ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);

		long groupId = themeDisplay.getScopeGroupId();
		
		try {
			pc =
			    PaymentConfigLocalServiceUtil.getPaymentConfigByGovAgency(groupId, govAgencyOrganizationId);
		}
		catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PrintWriter writer = resourceResponse.getWriter();
		JSONArray paymentConfigsJsonArray=JSONFactoryUtil.createJSONArray();
		
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
		if (pc != null) {
			jsonObject.put(PaymentConfigDisplayTerms.PAYMENT_CONFIG_ID, pc.getPaymentConfigId());
			jsonObject.put(
			    PaymentConfigDisplayTerms.BANK_INFO, pc.getBankInfo());
			jsonObject.put(PaymentConfigDisplayTerms.PLACE_INFO, pc.getPlaceInfo());
			jsonObject.put(PaymentConfigDisplayTerms.KEYPAY_DOMAIN, pc.getKeypayDomain());
			jsonObject.put(PaymentConfigDisplayTerms.KEYPAY_VERSION, pc.getKeypayVersion());
			jsonObject.put(PaymentConfigDisplayTerms.KEYPAY_MERCHANT_CODE, pc.getKeypayMerchantCode());
			jsonObject.put(PaymentConfigDisplayTerms.KEYPAY_SECURE_KEY, pc.getKeypaySecureKey());
			jsonObject.put(PaymentConfigDisplayTerms.GOV_AGENCY_TAX_NO, pc.getGovAgencyTaxNo());
			jsonObject.put(PaymentConfigDisplayTerms.INVOICE_TEMPLATE_NO, pc.getInvoiceTemplateNo());
			jsonObject.put(PaymentConfigDisplayTerms.INVOICE_ISSUE_NO, pc.getInvoiceIssueNo());
			jsonObject.put(PaymentConfigDisplayTerms.INVOICE_LAST_NO, pc.getInvoiceLastNo());
			jsonObject.put(PaymentConfigDisplayTerms.REPORT_TEMPLATE, pc.getReportTemplate());
		}
		paymentConfigsJsonArray.put(jsonObject);
		
		writer.print(paymentConfigsJsonArray.toString());
		writer.flush();
		writer.close();

		super.serveResource(resourceRequest, resourceResponse);
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void updatePaymentConfig(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {

		long paymentConfigId =
					    ParamUtil.getLong(
					        actionRequest,
					        PaymentConfigDisplayTerms.PAYMENT_CONFIG_ID);
		long govAgencyOrganizationId =
		    ParamUtil.getLong(
		        actionRequest,
		        PaymentConfigDisplayTerms.GOV_AGENCY_ORGANIZATION_ID);
		String govAgencyName = ParamUtil.getString(actionRequest, PaymentConfigDisplayTerms.GOV_AGENCY_NAME);
		String govAgencyTaxNo = ParamUtil.getString(actionRequest, PaymentConfigDisplayTerms.GOV_AGENCY_TAX_NO);
		String invoiceTemplateNo = ParamUtil.getString(actionRequest, PaymentConfigDisplayTerms.INVOICE_TEMPLATE_NO);
		String invoiceIssueNo = ParamUtil.getString(actionRequest, PaymentConfigDisplayTerms.INVOICE_ISSUE_NO);
		String invoiceLastNo = ParamUtil.getString(actionRequest, PaymentConfigDisplayTerms.INVOICE_LAST_NO);
		String bankInfo = ParamUtil.getString(actionRequest, PaymentConfigDisplayTerms.BANK_INFO);
		String placeInfo = ParamUtil.getString(actionRequest, PaymentConfigDisplayTerms.PLACE_INFO);
		String keypayDomain = ParamUtil.getString(actionRequest, PaymentConfigDisplayTerms.KEYPAY_DOMAIN);
		String keypayVersion = ParamUtil.getString(actionRequest, PaymentConfigDisplayTerms.KEYPAY_VERSION);
		String keypayMerchantCode = ParamUtil.getString(actionRequest, PaymentConfigDisplayTerms.KEYPAY_MERCHANT_CODE);
		String keypaySecureKey = ParamUtil.getString(actionRequest, PaymentConfigDisplayTerms.KEYPAY_SECURE_KEY);
		String reportTemplate = ParamUtil.getString(actionRequest, PaymentConfigDisplayTerms.REPORT_TEMPLATE);
		
		String returnURL = ParamUtil.getString(actionRequest, "returnURL");
		String currentURL = ParamUtil.getString(actionRequest, "currentURL");
		String backURL = ParamUtil.getString(actionRequest, "backURL");

		try {

			ServiceContext serviceContext =
			    ServiceContextFactory.getInstance(actionRequest);
			if (paymentConfigId == 0) {
				 PaymentConfig c = PaymentConfigLocalServiceUtil.addPaymentConfig(govAgencyOrganizationId, govAgencyName, govAgencyTaxNo, invoiceTemplateNo, invoiceIssueNo, invoiceLastNo, bankInfo, placeInfo, keypayDomain, keypayVersion, keypayMerchantCode, keypaySecureKey, reportTemplate, serviceContext.getUserId(), serviceContext);
				 paymentConfigId = c.getPaymentConfigId();
			}
			else {
				PaymentConfigLocalServiceUtil.updatePaymentConfig(paymentConfigId, govAgencyOrganizationId, govAgencyName, govAgencyTaxNo, invoiceTemplateNo, invoiceIssueNo, invoiceLastNo, bankInfo, placeInfo, keypayDomain, keypayVersion, keypayMerchantCode, keypaySecureKey, reportTemplate, serviceContext.getUserId(), serviceContext);
			}

			addProcessActionSuccessMessage = false;
			actionResponse.setRenderParameter(PaymentConfigDisplayTerms.PAYMENT_CONFIG_ID, String.valueOf(paymentConfigId));
			if (Validator.isNotNull(returnURL)) {
				actionResponse.sendRedirect(returnURL);
			}
			else if (Validator.isNotNull(backURL)) {
				actionResponse.sendRedirect(backURL);
			}
		}
		catch (Exception e) {
			if (Validator.isNotNull(currentURL)) {
				actionResponse.sendRedirect(currentURL);
			}
		}

	}

	private Log _log =
	    LogFactoryUtil.getLog(PaymentMgtPaymentConfigPortlet.class.getName());

}
