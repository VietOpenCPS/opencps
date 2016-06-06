<%@page import="org.opencps.usermgt.service.WorkingUnitLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.model.WorkingUnit"%>
<%@page import="org.opencps.paymentmgt.NoSuchPaymentConfigException"%>
<%@page import="org.opencps.paymentmgt.service.PaymentConfigLocalServiceUtil"%>
<%@page import="org.opencps.paymentmgt.model.PaymentConfig"%>
<%@page import="org.opencps.paymentmgt.permissions.PaymentConfigPermission"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="com.liferay.portal.model.Organization"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.kernel.dao.orm.QueryUtil"%>
<%@page import="com.liferay.portal.service.OrganizationLocalServiceUtil"%>
<%@page import="org.opencps.paymentmgt.search.PaymentConfigDisplayTerms"%>
<%
/**
 * OpenCPS is the open source Core Public Services software
 * Copyright (C) 2016-present OpenCPS community
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
%>
<%@ include file="../init.jsp"%>
<%
	String backURL = ParamUtil.getString(request, "backURL");
	long paymentConfigId = ParamUtil.getLong(request, PaymentConfigDisplayTerms.PAYMENT_CONFIG_ID, 0L);
	PaymentConfig c = null;
	try {
		c = PaymentConfigLocalServiceUtil.getPaymentConfig(paymentConfigId);
	}
	catch (NoSuchPaymentConfigException e) {
		
	}
	List<WorkingUnit> wunits = WorkingUnitLocalServiceUtil.getWorkingUnits(scopeGroupId);
	List<Organization> orgs = OrganizationLocalServiceUtil.getOrganizations(QueryUtil.ALL_POS,QueryUtil.ALL_POS);
%>
<c:choose>
<c:when test="<%= PaymentConfigPermission.contains(permissionChecker, scopeGroupId, ActionKeys.MANAGE_PAYMENT_CONFIG) %>">
	<portlet:resourceURL var="resourceURL"/>
	<script type="text/javascript">
		loadPaymentConfig();
		function loadPaymentConfig() {
		    AUI().use('aui-io-request', function(A){
		    	var govAgencyOrganizationId = A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.GOV_AGENCY_ORGANIZATION_ID %>').val();
		        A.io.request('<%=resourceURL.toString()%>', {
		        	 method: 'post',
		             data: {
		            	 <portlet:namespace /><%= PaymentConfigDisplayTerms.GOV_AGENCY_ORGANIZATION_ID %>: govAgencyOrganizationId
		             },
		             dataType: 'json',
		             on: {
		                 success: function() {
		                 	var paymentConfig = this.get('responseData');
		                 	A.Array.each(paymentConfig, function(obj, idx) {
		                 		if (obj) {
		                 			if (obj.<%= PaymentConfigDisplayTerms.PAYMENT_CONFIG_ID %>)
			                 			A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.PAYMENT_CONFIG_ID %>').set('value', obj.<%= PaymentConfigDisplayTerms.PAYMENT_CONFIG_ID %>);
		                 			else 
		                 				A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.PAYMENT_CONFIG_ID %>').set('value', '0');		                 			
		                 			if (obj.<%= PaymentConfigDisplayTerms.BANK_INFO %>)
			                 			A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.BANK_INFO %>').set('value', obj.<%= PaymentConfigDisplayTerms.BANK_INFO %>);
		                 			else 
		                 				A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.BANK_INFO %>').set('value', '');
		                 			if (obj.<%= PaymentConfigDisplayTerms.PLACE_INFO %>)
		                 				A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.PLACE_INFO %>').set('value', obj.<%= PaymentConfigDisplayTerms.PLACE_INFO %>);
		                 			else 
		                 				A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.PLACE_INFO %>').set('value', '');
		                 			if (obj.<%= PaymentConfigDisplayTerms.KEYPAY_DOMAIN %>)
			                 			A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.KEYPAY_DOMAIN %>').set('value', obj.<%= PaymentConfigDisplayTerms.KEYPAY_DOMAIN %>);
		                 			else
		                 				A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.KEYPAY_DOMAIN %>').set('value', '');
		                 			if (obj.<%= PaymentConfigDisplayTerms.KEYPAY_VERSION %>)
			                 			A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.KEYPAY_VERSION %>').set('value', obj.<%= PaymentConfigDisplayTerms.KEYPAY_VERSION %>);
		                 			else
		                 				A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.KEYPAY_VERSION %>').set('value', '');
		                 			if (obj.<%= PaymentConfigDisplayTerms.KEYPAY_MERCHANT_CODE %>)
			                 			A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.KEYPAY_MERCHANT_CODE %>').set('value', obj.<%= PaymentConfigDisplayTerms.KEYPAY_MERCHANT_CODE %>);
		                 			else
		                 				A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.KEYPAY_MERCHANT_CODE %>').set('value', '');
		                 			if (obj.<%= PaymentConfigDisplayTerms.KEYPAY_SECURE_KEY %>)
			                 			A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.KEYPAY_SECURE_KEY %>').set('value', obj.<%= PaymentConfigDisplayTerms.KEYPAY_SECURE_KEY %>);
		                 			else
		                 				A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.KEYPAY_SECURE_KEY %>').set('value', '');
		                 			if (obj.<%= PaymentConfigDisplayTerms.GOV_AGENCY_TAX_NO %>)
			                 			A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.GOV_AGENCY_TAX_NO %>').set('value', obj.<%= PaymentConfigDisplayTerms.GOV_AGENCY_TAX_NO %>);
		                 			else
		                 				A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.GOV_AGENCY_TAX_NO %>').set('value', '');
		                 			if (obj.<%= PaymentConfigDisplayTerms.INVOICE_TEMPLATE_NO %>)
			                 			A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.INVOICE_TEMPLATE_NO %>').set('value', obj.<%= PaymentConfigDisplayTerms.INVOICE_TEMPLATE_NO %>);
		                 			else
		                 				A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.INVOICE_TEMPLATE_NO %>').set('value', '');
		                 			if (obj.<%= PaymentConfigDisplayTerms.INVOICE_ISSUE_NO %>)
			                 			A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.INVOICE_ISSUE_NO %>').set('value', obj.<%= PaymentConfigDisplayTerms.INVOICE_ISSUE_NO %>);
		                 			else
		                 				A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.INVOICE_ISSUE_NO %>').set('value', '');
		                 			if (obj.<%= PaymentConfigDisplayTerms.INVOICE_LAST_NO %>)
			                 			A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.INVOICE_LAST_NO %>').set('value', obj.<%= PaymentConfigDisplayTerms.INVOICE_LAST_NO %>);
		                 			else
		                 				A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.INVOICE_LAST_NO %>').set('value', '');
		                 			
		                 			if (obj.<%= PaymentConfigDisplayTerms.REPORT_TEMPLATE %>)
			                 			A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.REPORT_TEMPLATE %>').set('value', obj.<%= PaymentConfigDisplayTerms.REPORT_TEMPLATE %>);
		                 			else
		                 				A.one('#<portlet:namespace /><%= PaymentConfigDisplayTerms.REPORT_TEMPLATE %>').set('value', '');
		                 		}
		                 	});
		             	 }
		             }	             
		        });
		    });
		} 
	</script>
	<portlet:actionURL var="updatePaymentConfigURL" name="updatePaymentConfig">			
		<portlet:param name="returnURL" value="<%=currentURL %>"/>
		<portlet:param name="backURL" value="<%=backURL %>"/>
	</portlet:actionURL>
	
	<liferay-ui:success key="update-payment-config-success" message="update-payment-config-success"
	/>
	<liferay-ui:error key="update-payment-config-error" message="update-payment-config-error" />
	
	<aui:form 
		action="<%= updatePaymentConfigURL.toString() %>"
		method="post"
		name="fm"
		id="fm"
	>
	<aui:input 
		type="hidden" 
		id="<%= PaymentConfigDisplayTerms.PAYMENT_CONFIG_ID %>"
		name="<%= PaymentConfigDisplayTerms.PAYMENT_CONFIG_ID %>" 
		value="<%= String.valueOf(paymentConfigId) %>"
	/>
	
	<aui:row>
		<aui:col>
			<aui:select id="<%= PaymentConfigDisplayTerms.GOV_AGENCY_ORGANIZATION_ID %>" onChange="loadPaymentConfig()" name="<%= PaymentConfigDisplayTerms.GOV_AGENCY_ORGANIZATION_ID %>" label="gov-agency-organization-id">
			<%
				for (WorkingUnit wunit : wunits) {
					if (c != null && wunit.getMappingOrganisationId() == c.getGovAgencyOrganizationId()) {
			%>	
			<aui:option selected="<%= true %>" value="<%= wunit.getMappingOrganisationId() %>"><%= wunit.getName() %></aui:option>
			<%
					}
					else {
			%>
			<aui:option selected="<%= false %>" value="<%= wunit.getMappingOrganisationId() %>"><%= wunit.getName() %></aui:option>
			<%			
					}
				}
			%>
			</aui:select>	
		</aui:col>
	</aui:row>
	<aui:row>
		<aui:col width="50">
			<aui:input value="<%= c != null ? c.getBankInfo() : \"\" %>" id="<%= PaymentConfigDisplayTerms.BANK_INFO %>" style="width: 98%;" name="<%= PaymentConfigDisplayTerms.BANK_INFO %>" type="textarea" label="bank-info">
				<aui:validator name="required"></aui:validator>
			</aui:input>
		</aui:col>
		<aui:col width="50">
			<aui:input value="<%= c != null ? c.getPlaceInfo() : \"\" %>" id="<%= PaymentConfigDisplayTerms.PLACE_INFO %>" style="width: 98%;" name="<%= PaymentConfigDisplayTerms.PLACE_INFO %>" type="textarea" label="place-info">
				<aui:validator name="required"></aui:validator>
			</aui:input>
		</aui:col>
	</aui:row>
	<aui:row>
		<aui:col>
			<div class="navbar">
				<div class="navbar-inner">
					<a class="brand">
						<liferay-ui:message key="keypay-configuration"></liferay-ui:message>
					</a>
				</div>
			</div>
		</aui:col>
	</aui:row>
	<aui:row>
		<aui:col width="50">
			<aui:input value="<%= c != null ? c.getKeypayDomain() : \"\" %>" id="<%= PaymentConfigDisplayTerms.KEYPAY_DOMAIN  %>" style="width: 98%;" name="<%= PaymentConfigDisplayTerms.KEYPAY_DOMAIN %>" label="keypay-domain">
				<aui:validator name="required"></aui:validator>
				<aui:validator name="url"/>
			</aui:input>
		</aui:col>
		<aui:col width="50">
			<aui:input value="<%= c != null ? c.getKeypayVersion() : \"\" %>" id="<%= PaymentConfigDisplayTerms.KEYPAY_VERSION %>" style="width: 98%;" name="<%= PaymentConfigDisplayTerms.KEYPAY_VERSION %>" label="keypay-version">
				<aui:validator name="required"></aui:validator>
			</aui:input>
		</aui:col>
	</aui:row>
	<aui:row>
		<aui:col width="50">
			<aui:input value="<%= c != null ? c.getKeypayMerchantCode() : \"\" %>" id="<%= PaymentConfigDisplayTerms.KEYPAY_MERCHANT_CODE %>" style="width: 98%;" name="<%= PaymentConfigDisplayTerms.KEYPAY_MERCHANT_CODE %>" label="keypay-merchant-code">
				<aui:validator name="required"></aui:validator>
			</aui:input>
		</aui:col>
		<aui:col width="50">
			<aui:input value="<%= c != null ? c.getKeypaySecureKey() : \"\" %>" id="<%= PaymentConfigDisplayTerms.KEYPAY_SECURE_KEY %>" style="width: 98%;" name="<%= PaymentConfigDisplayTerms.KEYPAY_SECURE_KEY %>" label="keypay-secure-key">
				<aui:validator name="required"></aui:validator>
			</aui:input>
		</aui:col>
	</aui:row>
	<aui:row>
		<aui:col>
			<div class="navbar">
				<div class="navbar-inner">
					<a class="brand">
						<liferay-ui:message key="invoice-configuration"></liferay-ui:message>
					</a>
				</div>
			</div>
		</aui:col>
	</aui:row>
	<aui:row>
		<aui:col width="50">
			<aui:input value="<%= c != null ? c.getGovAgencyTaxNo() : \"\" %>" id="<%= PaymentConfigDisplayTerms.GOV_AGENCY_TAX_NO %>" style="width: 98%;" name="<%= PaymentConfigDisplayTerms.GOV_AGENCY_TAX_NO %>" label="gov-agency-tax-no">
				<aui:validator name="required"></aui:validator>
			</aui:input>
		</aui:col>
		<aui:col width="50">
			<aui:input value="<%= c != null ? c.getInvoiceTemplateNo() : \"\" %>" id="<%= PaymentConfigDisplayTerms.INVOICE_TEMPLATE_NO %>" style="width: 98%;" name="<%= PaymentConfigDisplayTerms.INVOICE_TEMPLATE_NO %>" label="invoice-template-no">
				<aui:validator name="required"></aui:validator>
			</aui:input>	
		</aui:col>
	</aui:row>
	<aui:row>
		<aui:col width="50">
			<aui:input value="<%= c != null ? c.getInvoiceIssueNo() : \"\" %>" id="<%= PaymentConfigDisplayTerms.INVOICE_ISSUE_NO %>" style="width: 98%;" name="<%= PaymentConfigDisplayTerms.INVOICE_ISSUE_NO %>" label="invoice-issue-no">
				<aui:validator name="required"></aui:validator>
			</aui:input>
		</aui:col>
		<aui:col width="50">
			<aui:input value="<%= c != null ? c.getInvoiceLastNo() : \"\" %>" id="<%= PaymentConfigDisplayTerms.INVOICE_LAST_NO %>" style="width: 98%;" name="<%= PaymentConfigDisplayTerms.INVOICE_LAST_NO %>" label="invoice-last-no">
				<aui:validator name="required"></aui:validator>
				<aui:validator name="maxLength"
	            	errorMessage="no-more-than-7-characters">7</aui:validator>
			</aui:input>	
		</aui:col>
	</aui:row>
	<aui:row>
		<aui:col>
			<aui:button cssClass="pull-right" name="view-report-template" value="view-report-template"></aui:button>
		</aui:col>
	</aui:row>
	<aui:row>
		<aui:col>
			<aui:input value="<%= c != null ? c.getReportTemplate() : \"\" %>" id="<%= PaymentConfigDisplayTerms.REPORT_TEMPLATE %>" type="textarea" style="width: 98%" name="<%= PaymentConfigDisplayTerms.REPORT_TEMPLATE %>">
			</aui:input>
		</aui:col>
	</aui:row>
	<aui:row>
		<aui:col>
			<aui:button name="submit" type="submit" value="submit"/>
		</aui:col>
	</aui:row>
	</aui:form>
</c:when>
<c:otherwise>
	<liferay-ui:message key="do-not-have-permission"></liferay-ui:message>
</c:otherwise>
</c:choose>
