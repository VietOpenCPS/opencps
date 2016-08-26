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

<%@page import="org.opencps.dossiermgt.bean.DossierFileBean"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileSearchTerms"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileSearch"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ include file="../init.jsp"%>

<liferay-util:include page='<%=templatePath + "toptabs.jsp" %>' servletContext="<%=application %>" />
<%-- <liferay-util:include page='<%=templatePath + "toolbar.jsp" %>' servletContext="<%=application %>" /> --%>
<%
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("#");
	headerNames.add("create-date");
	headerNames.add("dossier-file-no");
/* 	headerNames.add("dossier-file-date");
	headerNames.add("display-name");
	headerNames.add("dossier-name");
	headerNames.add("dossier-file-type");
	headerNames.add("original-file"); */
	
	String headers = StringUtil.merge(headerNames);
	
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "frontofficedossierfilelist.jsp");
	iteratorURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_FILE);

	List<DossierFileBean> dossierFileBeans = new ArrayList<DossierFileBean>();
	int totalCount = 0;
%>

<div class="opencps-searchcontainer-wrapper">
	<liferay-ui:search-container 
		searchContainer="<%= new DossierFileSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>" 
		headerNames="<%= headers %>"
	>
	
		<liferay-ui:search-container-results>
			<%
				DossierFileSearchTerms searchTerms = (DossierFileSearchTerms)searchContainer.getSearchTerms();
							
				String 	templateFileNo = StringPool.BLANK;
				
				int partType = -1;
				
				int original = 0;
			
				try {
					dossierFileBeans = DossierFileLocalServiceUtil.searchDossierFileAdvance(scopeGroupId, citizen != null ? citizen.getMappingUserId() : 0, business != null ? business.getMappingOrganizationId() : 0, searchTerms.getKeywords(), templateFileNo, 0, partType, original, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
					totalCount = DossierFileLocalServiceUtil.countDossierFileAdvance(scopeGroupId, citizen != null ? citizen.getMappingUserId() : 0, business != null ? business.getMappingOrganizationId() : 0, searchTerms.getKeywords(), templateFileNo, 0, partType, original);
				} catch(Exception e){
					
				}
				total = totalCount;
				results = dossierFileBeans;
				
				pageContext.setAttribute("results", results);
				pageContext.setAttribute("total", total);				
			%>
		</liferay-ui:search-container-results>	
			<liferay-ui:search-container-row 
				className="org.opencps.dossiermgt.bean.DossierFileBean" 
				modelVar="dossierFileBean" 
				keyProperty="dossierFileId"
			>
				<%
					DossierFile dossierFile = dossierFileBean.getDossierFile();
				%>
				
				<liferay-util:buffer var="dossierFileInfo">
					<div class="row-fluid">

						<div class="span4 bold-label">
							<liferay-ui:message key="create-date"/>
						</div>
						<div class="span8"><%=DateTimeUtil.convertDateToString(dossierFile.getCreateDate(), DateTimeUtil._VN_DATE_TIME_FORMAT) %></div>
					</div>
					
					<div class="row-fluid">
						<div class="span4 bold-label">
							<liferay-ui:message key="dossier-file-no"/>
						</div>
						
						<div class="span8"><%=dossierFile.getDossierFileNo() %></div>
					</div>
					
					<div class="row-fluid">
						<div class="span4 bold-label"><liferay-ui:message key="dossier-file-date"/></div>
						
						<div class="span8"><%=DateTimeUtil.convertDateToString(dossierFile.getDossierFileDate(), DateTimeUtil._VN_DATE_FORMAT)%></div>
					</div>
					
				</liferay-util:buffer>	
			
				<liferay-util:buffer var="dossierFileInfoDetail">
					<div class="row-fluid">
						<div class="span3 bold-label">
							<liferay-ui:message key="display-name"/>
						</div>
						<div class="span9"><%=dossierFile.getDisplayName()%> </div>
					</div>
					
					<div class="row-fluid">
					
						<div class="span3 bold-label">
							<liferay-ui:message key="dossier-name"/>
						</div>
						
						<div class="span9"><%=dossierFileBean.getReceptionNo() %></div>
					</div>
					
					<div class="row-fluid">
						<div class="span3 bold-label"><liferay-ui:message key="dossier-file-type"/></div>
						
						<div class="span9"><%=String.valueOf(dossierFileBean.getPartType())%></div>
					</div>
					
				</liferay-util:buffer>
				
				<%
				
					/* 
				    // no column
					row.addText(String.valueOf(row.getPos() + 1 + searchContainer.getStart()));
					*/
					row.setClassName("opencps-searchcontainer-row");
					
					row.addText(dossierFileInfo);
					
					row.addText(dossierFileInfoDetail);
					
					StringBuffer sb = new StringBuffer();
					sb.append("<input type=\"checkbox\""+ (dossierFile.getOriginal() == 0 ? "checked" : StringPool.BLANK) + ">");
					row.addText(sb.toString());
					
				%>	
			</liferay-ui:search-container-row> 
		
		<liferay-ui:search-iterator type="opencs_page_iterator"/>
	</liferay-ui:search-container>
</div>