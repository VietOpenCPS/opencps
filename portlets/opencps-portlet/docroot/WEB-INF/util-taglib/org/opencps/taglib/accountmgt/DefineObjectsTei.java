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

package org.opencps.taglib.accountmgt;

import java.util.List;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.Citizen;
import org.opencps.usermgt.model.Employee;

import com.liferay.portlet.documentlibrary.model.DLFolder;

/**
 * @author trungnt
 */
public class DefineObjectsTei extends TagExtraInfo {

	@Override
	public VariableInfo[] getVariableInfo(TagData tagData) {

		return _variableInfo;
	}

	private static VariableInfo[] _variableInfo = new VariableInfo[] {
	    new VariableInfo("citizen", Citizen.class
	        .getName(), true, VariableInfo.AT_END),
	    new VariableInfo("business", Business.class
	        .getName(), true, VariableInfo.AT_END),
	    new VariableInfo("employee", Employee.class
	        .getName(), true, VariableInfo.AT_END),
	    new VariableInfo("accountType", String.class
	        .getName(), true, VariableInfo.AT_END),
	    new VariableInfo("accountFolder", DLFolder.class
	        .getName(), true, VariableInfo.AT_END),
	    new VariableInfo("accountRoles", List.class
	        .getName(), true, VariableInfo.AT_END),
	    new VariableInfo("accountOrgs", List.class
	        .getName(), true, VariableInfo.AT_END),
	    new VariableInfo("ownerUserId", Long.class
	        .getName(), true, VariableInfo.AT_END),
	    new VariableInfo("ownerOrganizationId", Long.class
	        .getName(), true, VariableInfo.AT_END)
	};
}
