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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;

/**
 * @author trungnt
 */
public class DLFolderUtil {

	private static Log _log = LogFactoryUtil
	    .getLog(DLFolderUtil.class
	        .getName());

	public static DLFolder addFolder(
	    long userId, long groupId, long repositoryId, boolean mountPoint,
	    long parentFolderId, String name, String description, boolean hidden,
	    ServiceContext serviceContext) {

		DLFolder dlFolder = null;
		try {
			dlFolder = DLFolderLocalServiceUtil
			    .addFolder(
			        userId, groupId, repositoryId, mountPoint, parentFolderId,
			        name, description, hidden, serviceContext);
		}
		catch (Exception e) {
			_log
			    .info(e);
		}
		return dlFolder;
	}

	public static DLFolder getFolder(
	    long userId, long groupId, long repositoryId, boolean mountPoint,
	    long parentFolderId, String name, String description, boolean hidden,
	    ServiceContext serviceContext) {

		DLFolder dlFolder = makeFolder(
		    userId, groupId, repositoryId, mountPoint, parentFolderId, name,
		    description, hidden, serviceContext);

		return getFolder(userId, groupId, repositoryId, mountPoint, dlFolder
		    .getFolderId(), name, description, hidden, serviceContext);

	}

	public static DLFolder getFolder(
	    long groupId, long parentFolderId, String name) {

		DLFolder dlFolder = null;

		try {
			dlFolder = DLFolderLocalServiceUtil
			    .getFolder(groupId, parentFolderId, name);
		}
		catch (Exception e) {
			_log
			    .info(e);
		}

		return dlFolder;
	}

	public static DLFolder getTargetFolder(
	    long userId, long groupId, long repositoryId, boolean mountPoint,
	    long parentFolderId, String destination, String description,
	    boolean hidden, ServiceContext serviceContext) {

		DLFolder dlFolder = null;

		String[] folderNames = StringUtil
		    .split(destination, StringPool.FORWARD_SLASH);

		if (folderNames != null && folderNames.length > 0) {
			String name = folderNames[0];

			dlFolder = makeFolder(
			    userId, groupId, repositoryId, mountPoint, parentFolderId, name,
			    description, hidden, serviceContext);
			folderNames = ArrayUtil
			    .remove(folderNames, name);
			if (folderNames.length > 0) {
				dlFolder = getTargetFolder(
				    userId, groupId, repositoryId, mountPoint, dlFolder
				        .getFolderId(),
				    StringUtil
				        .merge(folderNames, StringPool.FORWARD_SLASH),
				    description, hidden, serviceContext);
			}

		}

		return dlFolder;
	}

	public static DLFolder getTargetFolder(
	    long groupId, long parentFolderId, String destination) {

		DLFolder dlFolder = null;

		String[] folderNames = StringUtil
		    .split(destination, StringPool.FORWARD_SLASH);

		if (folderNames != null && folderNames.length > 0) {
			String name = folderNames[0];
			dlFolder = getFolder(groupId, parentFolderId, name);
			folderNames = ArrayUtil
			    .remove(folderNames, name);
			if (folderNames.length > 0) {
				dlFolder = getTargetFolder(groupId, dlFolder
				    .getFolderId(), StringUtil
				        .merge(folderNames, StringPool.FORWARD_SLASH));
			}

		}

		return dlFolder;
	}

	public static boolean hasFolder(
	    long groupId, long parentFolderId, String name) {

		boolean result = false;

		DLFolder dlFolder = null;

		try {
			dlFolder = DLFolderLocalServiceUtil
			    .getFolder(groupId, parentFolderId, name);
		}
		catch (Exception e) {
			_log
			    .warn(e
			        .getMessage());
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
			    userId, groupId, repositoryId, mountPoint, parentFolderId, name,
			    description, hidden, serviceContext);
		}
	}
}
