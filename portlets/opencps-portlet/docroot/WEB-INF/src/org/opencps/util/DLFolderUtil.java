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

import java.util.Date;

import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.Citizen;
import org.opencps.accountmgt.service.BusinessLocalServiceUtil;
import org.opencps.accountmgt.service.CitizenLocalServiceUtil;
import org.opencps.util.PortletUtil.SplitDate;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;

/**
 * @author trungnt
 */
public class DLFolderUtil {

	private static Log _log =
		LogFactoryUtil.getLog(DLFolderUtil.class.getName());

	public static DLFolder addFolder(
		long userId, long groupId, long repositoryId, boolean mountPoint,
		long parentFolderId, String name, String description, boolean hidden,
		ServiceContext serviceContext) {

		DLFolder dlFolder = null;
		try {
			if (hasFolder(groupId, parentFolderId, name)) {
				dlFolder =
					DLFolderLocalServiceUtil.getFolder(
						groupId, parentFolderId, name);
			}
			else {

				User user =
					UserLocalServiceUtil.getUser(serviceContext.getUserId());
				PermissionChecker checker =
					PermissionCheckerFactoryUtil.create(user);
				PermissionThreadLocal.setPermissionChecker(checker);
				dlFolder =
					DLFolderLocalServiceUtil.addFolder(
						userId, groupId, repositoryId, mountPoint,
						parentFolderId, name, description, hidden,
						serviceContext);
			}

		}
		catch (Exception e) {
			_log.info(e);
		}

		return dlFolder;
	}

	public static DLFolder getFolder(
		long userId, long groupId, long repositoryId, boolean mountPoint,
		long parentFolderId, String name, String description, boolean hidden,
		ServiceContext serviceContext) {

		DLFolder dlFolder =
			makeFolder(
				userId, groupId, repositoryId, mountPoint, parentFolderId,
				name, description, hidden, serviceContext);

		return getFolder(
			userId, groupId, repositoryId, mountPoint, dlFolder.getFolderId(),
			name, description, hidden, serviceContext);

	}

	public static DLFolder getFolder(
		long groupId, long parentFolderId, String name) {

		DLFolder dlFolder = null;

		try {
			dlFolder =
				DLFolderLocalServiceUtil.fetchFolder(
					groupId, parentFolderId, name);
		}
		catch (SystemException e) {
			_log.error(e);
		}

		return dlFolder;
	}

	public static DLFolder getTargetFolder(
		long userId, long groupId, long repositoryId, boolean mountPoint,
		long parentFolderId, String destination, String description,
		boolean hidden, ServiceContext serviceContext) {

		DLFolder dlFolder = null;

		String[] folderNames =
			StringUtil.split(destination, StringPool.FORWARD_SLASH);

		if (folderNames != null && folderNames.length > 0) {
			String name = folderNames[0];

			dlFolder =
				makeFolder(
					userId, groupId, repositoryId, mountPoint, parentFolderId,
					name, description, hidden, serviceContext);
			folderNames = ArrayUtil.remove(folderNames, name);
			if (folderNames.length > 0) {
				dlFolder =
					getTargetFolder(
						userId,
						groupId,
						repositoryId,
						mountPoint,
						dlFolder.getFolderId(),
						StringUtil.merge(folderNames, StringPool.FORWARD_SLASH),
						description, hidden, serviceContext);
			}

		}

		return dlFolder;
	}

	public static DLFolder getTargetFolder(
		long groupId, long parentFolderId, String destination) {

		DLFolder dlFolder = null;

		String[] folderNames =
			StringUtil.split(destination, StringPool.FORWARD_SLASH);

		if (folderNames != null && folderNames.length > 0) {
			String name = folderNames[0];
			dlFolder = getFolder(groupId, parentFolderId, name);
			folderNames = ArrayUtil.remove(folderNames, name);
			if (folderNames.length > 0) {
				dlFolder =
					getTargetFolder(
						groupId, dlFolder.getFolderId(),
						StringUtil.merge(folderNames, StringPool.FORWARD_SLASH));
			}

		}

		return dlFolder;
	}

	public static boolean hasFolder(
		long groupId, long parentFolderId, String name) {

		boolean result = false;

		DLFolder dlFolder = null;

		try {
			dlFolder =
				DLFolderLocalServiceUtil.fetchFolder(
					groupId, parentFolderId, name);
		}
		catch (SystemException e) {
			_log.error(e);
		}

		result = dlFolder != null ? true : false;

		return result;
	}

	public static DLFolder makeFolder(
		long userId, long groupId, long repositoryId, boolean mountPoint,
		long parentFolderId, String name, String description, boolean hidden,
		ServiceContext serviceContext) {

		if (hasFolder(groupId, parentFolderId, name)) {
			return getFolder(groupId, parentFolderId, name);
		}
		else {
			return addFolder(
				userId, groupId, repositoryId, mountPoint, parentFolderId,
				name, description, hidden, serviceContext);
		}
	}

	@Deprecated
	public static DLFolder getAccountFolder(
		long groupId, long userId, ServiceContext serviceContext) {

		DLFolder dlFolder = null;
		String destination = StringPool.BLANK;
		try {
			Citizen citizen =
				CitizenLocalServiceUtil.getByMappingUserId(userId);
			if (citizen != null) {
				destination =
					PortletUtil.getCitizenDossierDestinationFolder(
						groupId, userId);
			}
		}
		catch (Exception e) {
			_log.error(e);
		}

		if (Validator.isNull(destination)) {
			try {
				Business business =
					BusinessLocalServiceUtil.getBusiness(userId);
				if (business != null) {
					destination =
						PortletUtil.getBusinessDossierDestinationFolder(
							groupId, business.getMappingOrganizationId());
				}
			}
			catch (Exception e) {
				_log.error(e);
			}
		}

		if (Validator.isNotNull(destination)) {
			dlFolder =
				DLFolderUtil.getTargetFolder(
					serviceContext.getUserId(),
					serviceContext.getScopeGroupId(),
					serviceContext.getScopeGroupId(), false, 0, destination,
					StringPool.BLANK, false, serviceContext);
		}

		return dlFolder;
	}

	@Deprecated
	public static DLFolder getDossierFolder(
		long groupId, long userId, int dossierCount,
		ServiceContext serviceContext) {

		DLFolder dlFolder = null;
		String destination = StringPool.BLANK;
		try {
			Citizen citizen =
				CitizenLocalServiceUtil.getByMappingUserId(userId);
			if (citizen != null) {
				destination =
					PortletUtil.getCitizenDossierDestinationFolder(
						groupId, userId);
			}
		}
		catch (Exception e) {
			_log.error(e);
		}

		if (Validator.isNull(destination)) {
			try {
				Business business =
					BusinessLocalServiceUtil.getBusiness(userId);
				if (business != null) {
					destination =
						PortletUtil.getBusinessDossierDestinationFolder(
							groupId, business.getMappingOrganizationId());
				}
			}
			catch (Exception e) {
				_log.error(e);
			}
		}

		if (Validator.isNotNull(destination)) {
			dlFolder =
				DLFolderUtil.getTargetFolder(
					serviceContext.getUserId(),
					serviceContext.getScopeGroupId(),
					serviceContext.getScopeGroupId(), false, 0, destination +
						StringPool.SLASH + String.valueOf(dossierCount),
					StringPool.BLANK, false, serviceContext);
		}

		return dlFolder;
	}

	public static DLFolder getDossierFolder(
		long groupId, Date date, String oid, ServiceContext serviceContext) {

		DLFolder dlFolder = null;

		String destination = StringPool.BLANK;
		SplitDate splitDate = PortletUtil.splitDate(new Date());

		destination =
			PortletUtil.getDossierDestinationFolder(
				groupId, splitDate.getYear(), splitDate.getMonth(),
				splitDate.getDayOfMoth(), oid);

		if (Validator.isNotNull(destination)) {
			dlFolder =
				DLFolderUtil.getTargetFolder(
					serviceContext.getUserId(),
					serviceContext.getScopeGroupId(),
					serviceContext.getScopeGroupId(), false, 0, destination,
					StringPool.BLANK, false, serviceContext);
		}

		return dlFolder;
	}

	public static DLFolder getPaymentFolder(
		long groupId, Date date, long ownId, String accountType,
		ServiceContext serviceContext) {

		DLFolder dlFolder = null;

		String destination = StringPool.BLANK;
		SplitDate splitDate = PortletUtil.splitDate(new Date());

		destination =
			PortletUtil.getPaymentDestinationFolder(
				groupId, splitDate.getYear(), splitDate.getMonth(),
				splitDate.getDayOfMoth(), ownId, accountType);

		if (Validator.isNotNull(destination)) {
			dlFolder =
				DLFolderUtil.getTargetFolder(
					serviceContext.getUserId(),
					serviceContext.getScopeGroupId(),
					serviceContext.getScopeGroupId(), false, 0, destination,
					StringPool.BLANK, false, serviceContext);
		}

		return dlFolder;
	}

	public static DLFolder getSyncPaymentFolder(
		long groupId, Date date, String oId, ServiceContext serviceContext) {

		DLFolder dlFolder = null;

		String destination = StringPool.BLANK;
		SplitDate splitDate = PortletUtil.splitDate(new Date());

		destination =
			PortletUtil.getSyncPaymentDestinationFolder(
				groupId, splitDate.getYear(), splitDate.getMonth(),
				splitDate.getDayOfMoth(), oId);

		if (Validator.isNotNull(destination)) {
			dlFolder =
				DLFolderUtil.getTargetFolder(
					serviceContext.getUserId(),
					serviceContext.getScopeGroupId(),
					serviceContext.getScopeGroupId(), false, 0, destination,
					StringPool.BLANK, false, serviceContext);
		}

		return dlFolder;
	}
}
