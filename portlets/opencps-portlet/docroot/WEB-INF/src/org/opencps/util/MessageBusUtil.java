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

package org.opencps.util;

import javax.portlet.PortletPreferences;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.Ticket;
import com.liferay.portal.model.TicketConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.TicketLocalServiceUtil;
import com.liferay.portal.util.SubscriptionSender;
import com.liferay.util.PwdGenerator;

/**
 * @author trungnt
 */
public class MessageBusUtil {

	public static void sendEmailAddressVerification(
	    String uuid, User user, String emailAddress, String type,
	    ServiceContext serviceContext)
	    throws PortalException, SystemException {

		if (user
		    .isEmailAddressVerified() && StringUtil
		        .equalsIgnoreCase(emailAddress, user
		            .getEmailAddress())) {

			return;
		}

		Ticket ticket = TicketLocalServiceUtil
		    .addDistinctTicket(user
		        .getCompanyId(), User.class
		            .getName(),
		        user
		            .getUserId(),
		        TicketConstants.TYPE_EMAIL_ADDRESS, emailAddress, null,
		        serviceContext);

		String verifyEmailAddressURL = serviceContext
		    .getPortalURL() + "/opencps-portlet/verify/email?token=" + uuid +
		    "&ticketKey=" + ticket
		        .getKey() +
		    "&type=" + type;

		long plid = serviceContext
		    .getPlid();

		if (plid > 0) {
			Layout layout = LayoutLocalServiceUtil
			    .fetchLayout(plid);

			if (layout != null) {
				Group group = layout
				    .getGroup();

				if (!layout
				    .isPrivateLayout() && !group
				        .isUser()) {
					verifyEmailAddressURL += "&p_l_id=" + serviceContext
					    .getPlid();
				}
			}
		}

		String fromName = PrefsPropsUtil
		    .getString(user
		        .getCompanyId(), PropsKeys.ADMIN_EMAIL_FROM_NAME);
		String fromAddress = PrefsPropsUtil
		    .getString(user
		        .getCompanyId(), PropsKeys.ADMIN_EMAIL_FROM_ADDRESS);

		String toName = user
		    .getFullName();
		String toAddress = emailAddress;

		String subject = PrefsPropsUtil
		    .getContent(user
		        .getCompanyId(), PropsKeys.ADMIN_EMAIL_VERIFICATION_SUBJECT);

		String body = PrefsPropsUtil
		    .getContent(user
		        .getCompanyId(), PropsKeys.ADMIN_EMAIL_VERIFICATION_BODY);

		SubscriptionSender subscriptionSender = new SubscriptionSender();

		subscriptionSender
		    .setBody(body);
		subscriptionSender
		    .setCompanyId(user
		        .getCompanyId());
		subscriptionSender
		    .setContextAttributes("[$EMAIL_VERIFICATION_CODE$]", ticket
		        .getKey(), "[$EMAIL_VERIFICATION_URL$]", verifyEmailAddressURL,
		        "[$REMOTE_ADDRESS$]", serviceContext
		            .getRemoteAddr(),
		        "[$REMOTE_HOST$]", serviceContext
		            .getRemoteHost(),
		        "[$USER_ID$]", user
		            .getUserId(),
		        "[$USER_SCREENNAME$]", user
		            .getScreenName());
		subscriptionSender
		    .setFrom(fromAddress, fromName);
		subscriptionSender
		    .setHtmlFormat(true);
		subscriptionSender
		    .setMailId("user", user
		        .getUserId(), System
		            .currentTimeMillis(),
		        PwdGenerator
		            .getPassword());
		subscriptionSender
		    .setServiceContext(serviceContext);
		subscriptionSender
		    .setSubject(subject);
		subscriptionSender
		    .setUserId(user
		        .getUserId());

		subscriptionSender
		    .addRuntimeSubscribers(toAddress, toName);

		subscriptionSender
		    .flushNotificationsAsync();
	}

	public static void sendEmailActiveAccount(
	    User user, String password, ServiceContext serviceContext)
	    throws SystemException {

		if (!PrefsPropsUtil
		    .getBoolean(user
		        .getCompanyId(), PropsKeys.ADMIN_EMAIL_USER_ADDED_ENABLED)) {

			return;
		}

		String fromName = PrefsPropsUtil
		    .getString(user
		        .getCompanyId(), PropsKeys.ADMIN_EMAIL_FROM_NAME);
		String fromAddress = PrefsPropsUtil
		    .getString(user
		        .getCompanyId(), PropsKeys.ADMIN_EMAIL_FROM_ADDRESS);

		String toName = user
		    .getFullName();
		String toAddress = user
		    .getEmailAddress();

		String subject = PrefsPropsUtil
		    .getContent(user
		        .getCompanyId(), PropsKeys.ADMIN_EMAIL_USER_ADDED_SUBJECT);

		String body = null;

		if (Validator
		    .isNotNull(password)) {
			body = PrefsPropsUtil
			    .getContent(user
			        .getCompanyId(), PropsKeys.ADMIN_EMAIL_USER_ADDED_BODY);
		}
		else {
			body = PrefsPropsUtil
			    .getContent(user
			        .getCompanyId(),
			        PropsKeys.ADMIN_EMAIL_USER_ADDED_NO_PASSWORD_BODY);
		}

		SubscriptionSender subscriptionSender = new SubscriptionSender();

		subscriptionSender
		    .setBody(body);
		subscriptionSender
		    .setCompanyId(user
		        .getCompanyId());
		subscriptionSender
		    .setContextAttributes("[$USER_ID$]", user
		        .getUserId(), "[$USER_PASSWORD$]", password,
		        "[$USER_SCREENNAME$]", user
		            .getScreenName());
		subscriptionSender
		    .setFrom(fromAddress, fromName);
		subscriptionSender
		    .setHtmlFormat(true);
		subscriptionSender
		    .setMailId("user", user
		        .getUserId(), System
		            .currentTimeMillis(),
		        PwdGenerator
		            .getPassword());
		subscriptionSender
		    .setServiceContext(serviceContext);
		subscriptionSender
		    .setSubject(subject);
		subscriptionSender
		    .setUserId(user
		        .getUserId());

		subscriptionSender
		    .addRuntimeSubscribers(toAddress, toName);

		subscriptionSender
		    .flushNotificationsAsync();
	}

	public static void sendEmailWelcomeNewUser(
	    User user, ServiceContext serviceContext)
	    throws SystemException {

		String fromName = PrefsPropsUtil
		    .getString(user
		        .getCompanyId(), PropsKeys.ADMIN_EMAIL_FROM_NAME);
		String fromAddress = PrefsPropsUtil
		    .getString(user
		        .getCompanyId(), PropsKeys.ADMIN_EMAIL_FROM_ADDRESS);

		String toName = user
		    .getFullName();
		String toAddress = user
		    .getEmailAddress();

		String subject = PrefsPropsUtil
		    .getContent(user
		        .getCompanyId(), PropsKeys.ADMIN_EMAIL_USER_ADDED_SUBJECT);


		PortletPreferences preferences = PrefsPropsUtil
		    .getPreferences(serviceContext
		        .getCompanyId(), true);

		/*
		 * String emailWelcomeSubject = GetterUtil .getString(preferences
		 * .getValue("WELCOME_NEW_USER_SUBJECT", StringPool.BLANK));
		 */
		String emailWelcomeBody = GetterUtil
		    .getString(preferences
		        .getValue("WELCOME_NEW_USER_BODY", StringPool.BLANK));

		

		SubscriptionSender subscriptionSender = new SubscriptionSender();

		subscriptionSender
		    .setBody(emailWelcomeBody);
		subscriptionSender
		    .setCompanyId(user
		        .getCompanyId());
		
		subscriptionSender
		    .setFrom(fromAddress, fromName);
		subscriptionSender
		    .setHtmlFormat(true);
		subscriptionSender
		    .setMailId("user", user
		        .getUserId(), System
		            .currentTimeMillis(),
		        PwdGenerator
		            .getPassword());
		subscriptionSender
		    .setServiceContext(serviceContext);
		subscriptionSender
		    .setSubject(subject);
		subscriptionSender
		    .setUserId(user
		        .getUserId());

		subscriptionSender
		    .addRuntimeSubscribers(toAddress, toName);

		subscriptionSender
		    .flushNotificationsAsync();
	}

}
