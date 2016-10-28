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
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.jms.business;

import org.opencps.jms.message.body.PaymentFileMsgBody;
import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.service.ServiceContext;

/**
 * @author trungnt
 */
public class SubmitPaymentFile {

	/**
	 * @param paymentFileMsgBody
	 * @return
	 * @throws SystemException
	 * @throws PortalException
	 */
	public PaymentFile syncPaymentFile(PaymentFileMsgBody paymentFileMsgBody)
		throws SystemException, PortalException {

		String oId = paymentFileMsgBody.getOid();

		PaymentFile paymentFile =
			PaymentFileLocalServiceUtil.getPaymentFileByOID(oId);

		ServiceContext serviceContext = paymentFileMsgBody.getServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);

		String sourceFileName =
			paymentFileMsgBody != null ? paymentFile.getPaymentFileId() +
				StringPool.DASH + paymentFileMsgBody.getFileTitle() +
				StringPool.DASH + System.currentTimeMillis() : StringPool.BLANK;

		System.out.println("####################SubmitPaymentFile: Starting synchronize PaymentFile");

		paymentFile =
			PaymentFileLocalServiceUtil.syncPaymentFile(
				oId, paymentFileMsgBody.getTypeUpdate(),
				paymentFileMsgBody.getPaymentStatus(),
				paymentFileMsgBody.getPaymentMethod(),
				paymentFileMsgBody.getApproveNote(),
				paymentFileMsgBody.getConfirmFileEntry(), sourceFileName,
				paymentFileMsgBody.getMimeType(), paymentFileMsgBody != null
					? paymentFileMsgBody.getFileTitle() : StringPool.BLANK,
				paymentFileMsgBody.getFileDescription(), StringPool.BLANK,
				serviceContext);

		// TODO add log

		return paymentFile;
	}

}
