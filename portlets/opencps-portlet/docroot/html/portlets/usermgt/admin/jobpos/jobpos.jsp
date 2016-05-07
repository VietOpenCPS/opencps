<%
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
%>
<%@ include file="../../init.jsp"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.usermgt.model.WorkingUnit"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.usermgt.service.JobPosLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.model.JobPos"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="org.opencps.usermgt.search.JobPosDisplayTerms"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>

<%
	String redirectURL = ParamUtil.getString(request, "redirectURL");
	long jobPosId = ParamUtil.getLong(request, JobPosDisplayTerms.ID_JOBPOS);
	JobPos jobPos = null;
	try {
		jobPos = JobPosLocalServiceUtil.fetchJobPos(jobPosId);
	} catch(Exception e) {
		_log.error(e);
	}
%>




	<aui:model-context bean="<%=jobPos %>" model="<%=JobPos.class %>" />
	<aui:input name="<%=JobPosDisplayTerms.TITLE_JOBPOS %>">
		<aui:validator name="required"></aui:validator>
	</aui:input>
	<aui:input name="<%=JobPosDisplayTerms.ID_JOBPOS %>" type="hidden" />
	<aui:input name="<%=JobPosDisplayTerms.ID_JOBPOS %>" type="hidden" />
	<aui:select name="<%=JobPosDisplayTerms.LEADER_JOBPOS %>">
		<%
			for(int j = 0 ; j < PortletPropsValues.USERMGT_JOBPOS_LEADER.length; j++){
				%>
					<aui:option value="<%=PortletPropsValues.USERMGT_JOBPOS_LEADER[j] %>">
						<%=PortletUtil.getLeaderLabel(
							PortletPropsValues.USERMGT_JOBPOS_LEADER[j], locale) %>
					</aui:option>
				<%
			}
		%>
	</aui:select>

<aui:script>
	AUI().ready(function(A) {
		var putCurValueForSelect = A.one('#<portlet:namespace/><%=JobPosDisplayTerms.LEADER_JOBPOS%>');
		if(putCurValueForSelect) {
			putCurValueForSelect.setValue('<%=jobPos.getLeader()%>');
		}
	});
</aui:script>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.usermgt.admin.update_jobpos.jsp");
%>