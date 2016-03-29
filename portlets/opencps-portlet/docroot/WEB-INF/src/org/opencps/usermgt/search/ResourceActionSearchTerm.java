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

package org.opencps.usermgt.search;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.util.ParamUtil;

public class ResourceActionSearchTerm extends ResourceActionDisplayTerms {

	public ResourceActionSearchTerm(PortletRequest request) {

		super(request);
		resourceActionId =
			ParamUtil.getLong(request, RESOURCE_ACTION_RESOURCEACTIONID);

		name = ParamUtil.getString(request, RESOURCE_ACTION_NAME);

		actionId = ParamUtil.getString(request, RESOURCE_ACTION_ACTIONID);

		bitwiseValue = ParamUtil.getLong(request, RESOURCE_ACTION_BITWISEVALUE);
	}

	public long getResourceActionId() {

		return resourceActionId;
	}

	public void setResourceActionId(long resourceActionId) {

		this.resourceActionId = resourceActionId;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getActionId() {

		return actionId;
	}

	public void setActionId(String actionId) {

		this.actionId = actionId;
	}

	public long getBitwiseValue() {

		return bitwiseValue;
	}

	public void setBitwiseValue(long bitwiseValue) {

		this.bitwiseValue = bitwiseValue;
	}
	protected long resourceActionId;
	protected String name;
	protected String actionId;
	protected long bitwiseValue;
}
