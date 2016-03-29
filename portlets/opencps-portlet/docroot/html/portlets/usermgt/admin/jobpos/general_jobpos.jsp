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

<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.usermgt.search.JobPosDisplayTerms"%>
<%@page import="org.opencps.usermgt.service.JobPosLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.model.JobPos"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.usermgt.service.WorkingUnitLocalServiceUtil"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.usermgt.model.WorkingUnit"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>

<%@ include file="../../init.jsp"%>

<%
	long workingUnitId = ParamUtil.getLong(request, "workingUnitId");
	long jobposId = ParamUtil.getLong(request, JobPosDisplayTerms.ID_JOBPOS);
	int[] rowIndexes = null;	
	rowIndexes = new int[]{0};
%>

	<aui:row>
		<div id="member-fields">

			<div class="lfr-form-row lfr-form-row-inline">
				<%
					for(int row = 0; row < rowIndexes.length; row++){
						int rowIndex = rowIndexes[row];
				%>
						<aui:row>
							<aui:col width="50">
								<aui:input type="text" 
									name='<%=JobPosDisplayTerms.TITLE_JOBPOS + rowIndex %>' 
									label="title"
								>
									<aui:validator name="required"></aui:validator>
								</aui:input>
								
								<aui:input
									name='<%=JobPosDisplayTerms.ID_JOBPOS + rowIndex %>'
									type="hidden" 
								/>
								
							</aui:col>
							<aui:col width="50">
								<aui:select name='<%=JobPosDisplayTerms.LEADER_JOBPOS + rowIndex%>'
									label="leader">
									<%
										for(int j = 0 ; j < PortletPropsValues.USERMGT_JOBPOS_LEADER.length; j++){
											%>
												<aui:option value="<%=PortletPropsValues.USERMGT_JOBPOS_LEADER[j] %>">
													<%=PortletUtil.getLeaderLabel(PortletPropsValues.USERMGT_JOBPOS_LEADER[j], locale) %>
												</aui:option>
											<%
										}
									%>
								</aui:select>
								<aui:input type="hidden" name ="rowIndexes" value="<%=StringUtil.merge(rowIndexes) %>"></aui:input>
							</aui:col>
						</aui:row>
				<%
					}
				%>

			</div>

		</div>

	</aui:row>

<aui:script>
	AUI().use('liferay-auto-fields', function(A) {
		new Liferay.AutoFields({
			contentBox : '#member-fields',
			fieldIndexes : '<portlet:namespace />rowIndexes'
		}).render();
	});
</aui:script>
	
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.usermgt.admin.jobpos.general_jobpos.jsp");
%>








