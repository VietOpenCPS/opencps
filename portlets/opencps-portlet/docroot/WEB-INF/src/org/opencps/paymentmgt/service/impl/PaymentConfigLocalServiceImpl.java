/*******************************************************************************
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
 *******************************************************************************/

package org.opencps.paymentmgt.service.impl;

import java.util.Date;

import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.paymentmgt.NoSuchPaymentConfigException;
import org.opencps.paymentmgt.model.PaymentConfig;
import org.opencps.paymentmgt.service.base.PaymentConfigLocalServiceBaseImpl;
import org.opencps.servicemgt.model.ServiceInfo;
import org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil;
import org.opencps.usermgt.model.WorkingUnit;
import org.opencps.usermgt.service.WorkingUnitLocalServiceUtil;
import org.opencps.util.PortletConstants;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the Payment configuration local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link org.opencps.paymentmgt.service.PaymentConfigLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author trungdk
 * @see org.opencps.paymentmgt.service.base.PaymentConfigLocalServiceBaseImpl
 * @see org.opencps.paymentmgt.service.PaymentConfigLocalServiceUtil
 */
public class PaymentConfigLocalServiceImpl
	extends PaymentConfigLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link org.opencps.paymentmgt.service.PaymentConfigLocalServiceUtil} to access the Payment configuration local service.
	 */
	
	public PaymentConfig addPaymentConfig(
	    long govAgencyOrganizationId,
	    String govAgencyName,
	    String govAgencyTaxNo,
	    String invoiceTemplateNo,
	    String invoiceIssueNo,
	    String invoiceLastNo,
	    String bankInfo,
	    String placeInfo,
	    String keypayDomain,
	    String keypayVersion,
	    String keypayMerchantCode,
	    String keypaySecureKey,
	    String reportTemplate,
	    long userId,
	    ServiceContext serviceContext)
	    throws PortalException, SystemException {

		long paymentConfigId = CounterLocalServiceUtil
		    .increment(PaymentConfig.class
		        .getName());

		PaymentConfig paymentConfig = paymentConfigPersistence
		    .create(paymentConfigId);

		Date currentDate = new Date();

		paymentConfig
		    .setUserId(userId);
		paymentConfig
		    .setCompanyId(serviceContext
		        .getCompanyId());
		paymentConfig
		    .setGroupId(serviceContext
		        .getScopeGroupId());
		paymentConfig
		    .setCreateDate(currentDate);
		paymentConfig
		    .setModifiedDate(currentDate);

		paymentConfig
		    .setGovAgencyOrganizationId(govAgencyOrganizationId);
		paymentConfig
			.setGovAgencyName(govAgencyName);
		paymentConfig
			.setGovAgencyTaxNo(govAgencyTaxNo);
		paymentConfig
			.setInvoiceTemplateNo(invoiceTemplateNo);
		paymentConfig
			.setInvoiceIssueNo(invoiceIssueNo);
		paymentConfig
			.setInvoiceLastNo(invoiceLastNo);
		paymentConfig
			.setBankInfo(bankInfo);
		paymentConfig
			.setPlaceInfo(placeInfo);
		paymentConfig
			.setKeypayDomain(keypayDomain);
		paymentConfig
			.setKeypayVersion(keypayVersion);
		paymentConfig
			.setKeypayMerchantCode(keypayMerchantCode);
		paymentConfig
			.setKeypaySecureKey(keypaySecureKey);
		paymentConfig
			.setReportTemplate(reportTemplate);
		return paymentConfigPersistence
		    .update(paymentConfig);

	}
	
	public PaymentConfig updatePaymentConfig(
	    long paymentConfigId, 
	    long govAgencyOrganizationId,
	    String govAgencyName,
	    String govAgencyTaxNo,
	    String invoiceTemplateNo,
	    String invoiceIssueNo,
	    String invoiceLastNo,
	    String bankInfo,
	    String placeInfo,
	    String keypayDomain,
	    String keypayVersion,
	    String keypayMerchantCode,
	    String keypaySecureKey,	 
	    String reportTemplate,
	    long userId,
	    ServiceContext serviceContext)
	    throws PortalException, SystemException {

		PaymentConfig paymentConfig = paymentConfigPersistence
		    .findByPrimaryKey(paymentConfigId);

		Date currentDate = new Date();

		paymentConfig
		    .setUserId(userId);
		paymentConfig
		    .setCompanyId(serviceContext
		        .getCompanyId());
		paymentConfig
		    .setGroupId(serviceContext
		        .getScopeGroupId());
		paymentConfig
		    .setCreateDate(currentDate);
		paymentConfig
		    .setModifiedDate(currentDate);

		paymentConfig
			.setGovAgencyOrganizationId(govAgencyOrganizationId);
		paymentConfig
			.setGovAgencyName(govAgencyName);
		paymentConfig
			.setGovAgencyTaxNo(govAgencyTaxNo);
		paymentConfig
			.setInvoiceTemplateNo(invoiceTemplateNo);
		paymentConfig
			.setInvoiceIssueNo(invoiceIssueNo);
		paymentConfig
			.setInvoiceLastNo(invoiceLastNo);
		paymentConfig
			.setBankInfo(bankInfo);
		paymentConfig
			.setPlaceInfo(placeInfo);
		paymentConfig
			.setKeypayDomain(keypayDomain);
		paymentConfig
			.setKeypayVersion(keypayVersion);
		paymentConfig
			.setKeypayMerchantCode(keypayMerchantCode);
		paymentConfig
			.setKeypaySecureKey(keypaySecureKey);
		paymentConfig
			.setReportTemplate(reportTemplate);
		
		return paymentConfigPersistence
		    .update(paymentConfig);

	}
	
	public PaymentConfig getPaymentConfigByGovAgency(long groupId, long govAgencyOrganizationId) throws SystemException {
		return paymentConfigPersistence.fetchByGovAgency(groupId, govAgencyOrganizationId);
	}
}