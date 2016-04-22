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


package org.opencps.processmgt.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.portlet.RenderRequest;

import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.model.ServiceProcess;
import org.opencps.processmgt.model.StepAllowance;
import org.opencps.processmgt.service.ProcessStepLocalServiceUtil;
import org.opencps.processmgt.service.ServiceProcessLocalServiceUtil;
import org.opencps.processmgt.service.StepAllowanceLocalServiceUtil;

import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.service.RoleLocalServiceUtil;

/**
 * @author binhth
 */
public class ProcessOrderUtils {

	/**
	 * @param roleIds
	 * @return
	 */
	public static List<ProcessStep> getProcessSteps(long[] roleIds) {

		List<ProcessStep> results = null;
		
		try {
			List<StepAllowance> listStepAllowance = StepAllowanceLocalServiceUtil.findByRoleIds(roleIds);
			long[] processStepIds = new long[listStepAllowance.size()];
			int index = 0;
			for (StepAllowance stepAllowance : listStepAllowance) {
				processStepIds[index++] = stepAllowance.getStepAllowanceId();
	        }
			results = ProcessStepLocalServiceUtil.findByProcessStepIds(processStepIds);
		}
		catch (Exception e) {
			return new ArrayList<ProcessStep>();
		}
		return results;
	}
	/**
	 * @param roleIds
	 * @return
	 */
	public static String generateMenuBuocXuLy(RenderRequest renderRequest, long[] roleIds, String groupName, String cssClass, String active, boolean counter, String actionURL) {

		StringBuilder sbHtml = new StringBuilder();
		sbHtml.append("<ul class=\"menu-opencps\">");
		for(ProcessStep ett: getProcessSteps(roleIds)){
			String mnClass =(Validator.isNotNull(active) && active.equalsIgnoreCase(String.valueOf(ett.getProcessStepId()))?"active-menu":"");
			sbHtml.append("<li class=\"menu-opencps-li "+mnClass+"\" onclick=\"openCPS_menu_submit('"+actionURL+"','"+ett.getProcessStepId()+"')\" >");
			sbHtml.append("<a>");
			if(counter)sbHtml.append("<span id=\""+"badge_" + ett.getProcessStepId()+"\" class=\"badge\">0</span>");
			sbHtml.append(HtmlUtil.escape(ett.getStepName()));
			sbHtml.append("</a>");
			sbHtml.append("</li>");
		}
		sbHtml.append("</ul>");
		return sbHtml.toString();
	}
}
