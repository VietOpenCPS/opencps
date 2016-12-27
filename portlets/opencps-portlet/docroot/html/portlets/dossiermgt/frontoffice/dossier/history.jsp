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

<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.dossiermgt.model.DossierLog"%>
<%@page import="org.opencps.dossiermgt.service.DossierLogLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.processmgt.util.ProcessUtils"%>
<%@page import="org.opencps.processmgt.model.ProcessStep"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.opencps.dossiermgt.model.DossierFileLog"%>

<%@ include file="../../init.jsp"%>

<%
	Dossier dossier = (Dossier) request.getAttribute(WebKeys.DOSSIER_ENTRY);

	long dossierId = dossier != null ? dossier.getDossierId() : 0L;
	
	List<DossierLog> dossierLogs = new ArrayList<DossierLog>();
	
	String serviceName = StringPool.DASH;
	String receptionNo = StringPool.DASH;
	
	ServiceInfo serviceInfo = null;
	
	if(Validator.isNotNull(dossier)) {
		receptionNo = dossier.getReceptionNo();
		try {
			serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());
			serviceName = serviceInfo.getServiceName();
		} catch (Exception e) {
			
		}
	}
	
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", "/html/portlets/dossiermgt/frontoffice/dossier/history.jsp");

%>
<div class="header-title custom-title pdl30">
		<liferay-ui:message key="history"/>
</div>

<aui:row>
	<aui:col width="20" cssClass="bold">
		<liferay-ui:message key="dossier-reception-no"/> 
	</aui:col>
	<aui:col width="80">
		<%=receptionNo %>
	</aui:col>
</aui:row>

<aui:row cssClass="pd_b20">
	<aui:col width="20" cssClass="bold">
		<liferay-ui:message key="dossier-service-name"/> 
	</aui:col>
	<aui:col width="80">
		<%=serviceName%>
	</aui:col>
</aui:row>

<liferay-ui:search-container 
	emptyResultsMessage="no-history-were-found"
	iteratorURL="<%=iteratorURL %>"
	delta="<%= PAGINATE_NUMBER %>"
	deltaConfigurable="true"
>
	<liferay-ui:search-container-results>
		<%
/* 			dossierLogs = DossierLogLocalServiceUtil.getDossierLogByDossierId(dossierId, searchContainer.getStart(), searchContainer.getEnd());
 */			
 			dossierLogs = DossierLogLocalServiceUtil.findDossierLog(1, dossierId, searchContainer.getStart(), searchContainer.getEnd());

			results = dossierLogs;
			
			//total = DossierLogLocalServiceUtil.countDossierLogByDossierId(dossierId);
			total = DossierLogLocalServiceUtil.countDossierLog(1, dossierId);
			
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
			
			//List<ResultRow> rows = searchContainer.getResultRows();
			
		%>
	</liferay-ui:search-container-results>
	
	<liferay-ui:search-container-row 
		className="org.opencps.dossiermgt.model.DossierLog" 
		modelVar="dossierLog" 
		keyProperty="dossierLogId"
	>
		<aui:row cssClass="top-line pd_b20 pd_t20">
			<aui:col width="30">
				<span class="span1">
					<i class="fa fa-circle blue sx10"></i>
				</span>
				
				<span class="span8">
					<%=
						Validator.isNotNull(dossierLog.getUpdateDatetime()) ?
						DateTimeUtil.convertDateToString(dossierLog.getUpdateDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) : 
						DateTimeUtil._EMPTY_DATE_TIME 
					%>
				</span>
			</aui:col>
			<aui:col width="70">

				<aui:row>
					<span class="span4 bold">
						<liferay-ui:message key="step-name" />
					</span>
					
					<span class="span8">
						<liferay-ui:message key="<%= dossierLog.getStepName() %>"/>
					</span>
				</aui:row>
			
				<aui:row>
					<span class="span4 bold">
						<liferay-ui:message key="dossier-status" />
					</span>
					
					<span class="span8">
						
						<%
							String dossierSubStatus = StringPool.BLANK;
							
							ProcessStep processStep = ProcessUtils.getPostProcessStep(dossierLog.getStepId());

							if (Validator.isNotNull(processStep) && Validator.isNotNull(processStep.getDossierSubStatus())) {
								dossierSubStatus = LanguageUtil.get(locale, processStep.getDossierSubStatus());
							}
							
						%>
						
						<%=PortletUtil.getDossierStatusLabel(dossierLog.getDossierStatus(), locale)%> 
						
						<c:if test="<%= Validator.isNotNull(dossierSubStatus) %>">
							<span class="sub-status">
								<%= StringPool.COLON + StringPool.SPACE + dossierSubStatus %> 
							</span>
						</c:if>
						
					</span>
				</aui:row>
			
				<aui:row>
					<span class="span4 bold">
						<liferay-ui:message key="actor" />
					</span>
					
					<span class="span8">
						<liferay-ui:message key="<%= dossierLog.getActorName() %>"/>
					</span>
				</aui:row>
				
				<aui:row>
					<span class="span4 bold">
						<liferay-ui:message key="action" />
					</span>
					
					<span class="span8">
						<liferay-ui:message key="<%= DossierMgtUtil.getDossierLogs(StringPool.BLANK, dossierLog.getActionInfo())  %>"/>
					</span>
					
				</aui:row>
				
				<aui:row>
					<span class="span4 bold">
						<liferay-ui:message key="note" />
					</span>
					
					<span class="span8">
						<liferay-ui:message key="<%= DossierMgtUtil.getDossierLogs(StringPool.BLANK, dossierLog.getMessageInfo()).replaceAll(\"update-version-file\", LanguageUtil.get(pageContext, \"update-version-file\"))  %>"/>
					</span>
					
				</aui:row>
				
				<%
					List<DossierFileLog> logFiles = DossierMgtUtil.getFileLogs(dossierLog.getDossierLogId(), dossierId);
				
				
				%>
				<c:if test="<%= logFiles.size() != 0 %>">
					<aui:row>
						<span class="span12 bold">
							<liferay-ui:message key="file-modified" />
						</span>
						
					</aui:row>
				
					<aui:row>
						<span class="span12">
							<%
								SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yy | hh:mm:ss");
								for (DossierFileLog lf : logFiles) {
									
									String cssClass = "dossier-file-status-" + lf.getActionCode();
									String actionCode = LanguageUtil.get(locale, cssClass);
							%>
								<div style="padding: 3px; ">
									<%= StringPool.GREATER_THAN %> 
										 <aui:a href="#" >
										 	<%= lf.getFileName() %> 
										 	<span style="font: smaller; color: #cbcbcb;">(<%= sdf.format(lf.getModifiedDate()) %> )</span>
										 	
										 </aui:a>
								</div>
							<%
								}
							%>
							
						</span>
						
					</aui:row>
				
				</c:if>
				
			</aui:col>
		</aui:row>
	</liferay-ui:search-container-row>
	<liferay-ui:search-iterator paginate="false"/>
</liferay-ui:search-container>

<%!
	private int PAGINATE_NUMBER = 200;
%>

