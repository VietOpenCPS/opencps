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

package org.opencps.jms;

import java.util.Date;

import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.service.ServiceContext;

/**
 * @author trungnt
 */
public class SyncServiceContext {

	public SyncServiceContext(

		long companyId, long groupId, long userId,
		boolean isAddGroupPermissions, boolean isAddGuestPermissions) {

		Date now = new Date();
		ServiceContext serviceContext = new ServiceContext();
		serviceContext.setAddGroupPermissions(isAddGroupPermissions);
		serviceContext.setAddGuestPermissions(isAddGuestPermissions);
		serviceContext.setScopeGroupId(groupId);
		serviceContext.setCompanyId(companyId);
		serviceContext.setUserId(userId);
		serviceContext.setUuid(PortalUUIDUtil.generate());
		serviceContext.setCreateDate(now);
		serviceContext.setModifiedDate(now);
		this.setServiceContext(serviceContext);
	}

	public ServiceContext getServiceContext() {

		return _serviceContext;
	}

	public void setServiceContext(ServiceContext serviceContext) {

		this._serviceContext = serviceContext;
	}

	private ServiceContext _serviceContext;
}
