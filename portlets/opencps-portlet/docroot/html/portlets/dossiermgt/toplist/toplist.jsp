
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
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.util.ContextPathUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="java.util.List"%>
<%@ include file="init.jsp" %>

<%
	List<Dossier> dossiers = new ArrayList<Dossier>();
	int totalCount = 0;

	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", "/html/portlets/dossiermgt/toplist/toplist.jsp");
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("boundcol1");
	headerNames.add("boundcol2");
	
	String headers = StringUtil.merge(headerNames, StringPool.COMMA);

%>
<div class="opencps-searchcontainer-wrapper">
	<liferay-ui:search-container 
			emptyResultsMessage="no-dossier-were-found"
			iteratorURL="<%=iteratorURL %>"
			delta="<%=20 %>"
			deltaConfigurable="true"
			headerNames="<%=headers %>"
			>
			<liferay-ui:search-container-results>
				<%
					dossiers = DossierLocalServiceUtil.getDossierByStatus(scopeGroupId,
						status, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
					totalCount = DossierLocalServiceUtil.countDossierByStatus(scopeGroupId, status);
					
					results = dossiers;
					total = totalCount;
					
					pageContext.setAttribute("results", results);
					pageContext.setAttribute("total", total);
				%>
			</liferay-ui:search-container-results>
			
			<liferay-ui:search-container-row 
				className="org.opencps.dossiermgt.model.Dossier" 
				modelVar="dossier" 
				keyProperty="dossierId"
			>
				<%
					String serviceName = StringPool.BLANK;
					
					String dossierName = StringPool.BLANK;
					
					User bossOfDossier = null;
					ServiceInfo serviceInfo = null;
					
					try {
						bossOfDossier = UserLocalServiceUtil.getUser(dossier.getUserId());
						dossierName = bossOfDossier.getFullName();
					} catch(Exception e) {
						//nothing to do
					}
					
					try {
						serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());
						serviceName = serviceInfo.getFullName();
					} catch (Exception e) {
						//nothing to do
					}
					
				%>
				<liferay-util:buffer var="boundcol1">
					<div class="row-fluid">
						<div class="row-fluid">
							<div class="span2 bold-label">
								<liferay-ui:message key="dossier-no"/>
							</div>
							<div class="span6">
								<%=dossier.getReceptionNo() %>
							</div>
						</div>
					</div>
					
					<div class="row-fluid">
						
						<div class="span2 bold-label">
							 <liferay-ui:message key="name-of-service"/>
						</div>
						
						<div class="span9">
							<%=serviceName %>
						</div>
					</div>
				</liferay-util:buffer>
				
				
				<liferay-util:buffer var="boundcol2">
				<div class="row-fluid">
					
					<div class="span5 bold-label"><liferay-ui:message key="boss-of-dossier"/></div>
					<div class="span6">
						<%=dossierName %>
					</div>
				</div>
				
				<div class="row-fluid">
					
					<div class="span5 bold-label">
						 <liferay-ui:message key="date-of-receiving"/>
					</div>
					
					<div class="span6">
						<%=DateTimeUtil.convertDateToString(dossier.getCreateDate(), DateTimeUtil._VN_DATE_TIME_FORMAT) %>
					</div>
				</div>
				
				
				</liferay-util:buffer>
				<%
					row.setClassName("opencps-searchcontainer-row");
					row.addText(boundcol1);
					row.addText(boundcol2);
				%>
				<%-- <liferay-ui:search-container-column-text 
					name="row-no" value="<%=dossier.getReceptionNo()%>"
				/>
				
				<liferay-ui:search-container-column-text 
					name="boss-of-dossier" value="<%= dossierName %>"
				/>
				
				<liferay-ui:search-container-column-text 
					name="name-of-service" value="<%= serviceName %>"
				/>
				
				<liferay-ui:search-container-column-text 
					name="date-for-receiving" value="<%=DateTimeUtil.convertDateToString(dossier.getCreateDate(), DateTimeUtil._VN_DATE_TIME_FORMAT) %>"
				/> --%>
			</liferay-ui:search-container-row>
			<liferay-ui:search-iterator type="opencs_page_iterator"/>
	</liferay-ui:search-container>
</div>