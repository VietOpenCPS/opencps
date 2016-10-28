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
package org.opencps.paymentmgt.util;

import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 * @author trungdk
 */
public class PaymentMgtUtil {
	public static final int PAYMENT_STATUS_REQUESTED = 0;
	public static final int PAYMENT_STATUS_CONFIRMED = 1;
	public static final int PAYMENT_STATUS_APPROVED = 2;
	public static final int PAYMENT_STATUS_REJECTED = 3;
	
	public static final int PAYMENT_METHOD_CASH = 1;
	public static final int PAYMENT_METHOD_KEYPAY = 2;
	public static final int PAYMENT_METHOD_BANK = 3;
	
	public static final int PAYMENT_STATUS_KEYPAY_OK = 0;
	public static final int PAYMENT_STATUS_KEYPAY_PENDING = 1;
	
	/**
	 * @param ownerUserId
	 * @param ownerOrgId
	 * @return
	 */
	public static String getOwnerPayment(long ownerUserId, long ownerOrgId) {

		String ownerName = StringPool.BLANK;

		if (ownerUserId != 0) {
			try {
				User user = UserLocalServiceUtil.fetchUser(ownerUserId);
				ownerName = user.getFullName();
			}
			catch (Exception e) {
				_log.error(e);
			}
		}

		if (ownerOrgId != 0) {
			try {
				Organization org =
				    OrganizationLocalServiceUtil.fetchOrganization(ownerOrgId);
				ownerName = org.getName();
			}
			catch (Exception e) {
				_log.error(e);
			}
		}

		return ownerName;
	}
	
	/**
	 * @param paymentFileId
	 * @return
	 */
	public static String getPaymentMethod(long paymentFileId) {

		String paymentMethodName = StringPool.BLANK;

		PaymentFile paymentFile = null;

		try {
			paymentFile =
			    PaymentFileLocalServiceUtil.fetchPaymentFile(paymentFileId);

			int paymentMethod = paymentFile.getPaymentMethod();

			if (paymentFile.getPaymentStatus() == 1 ||
			    paymentFile.getPaymentStatus() == 2) {

				switch (paymentMethod) {
				case 1:
					paymentMethodName = "payment-method-cash";
					break;
				case 2:
					paymentMethodName = "payment-method-keypay";
					break;
				case 4:
					paymentMethodName = "payment-method-bank";
					break;
				default:
					break;
				}

			}
		}
		catch (Exception e) {
			_log.error(e);
		}

		return paymentMethodName;
	}
	
	/**
	 * Get dossierStatus by PaymentFile
	 * 
	 * @param paymentFile
	 * @return String (DossierStatus)
	 */
	public static String getDossierStatus(PaymentFile paymentFile) {
		String dossierStatus = StringPool.BLANK;
		
		try {
			Dossier dossier = DossierLocalServiceUtil.fetchDossier(paymentFile.getDossierId());
			
			dossierStatus = dossier.getDossierStatus();
			
        }
        catch (Exception e) {
	        _log.error(e);
        }
		
		return dossierStatus;
		
	}
	
	public static Log _log = LogFactoryUtil.getLog(PaymentMgtUtil.class);
}
