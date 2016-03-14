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

<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="org.opencps.datamgt.search.DictCollectionSearch"%>
<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@page import="javax.swing.plaf.ListUI"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>
<%@page import="org.opencps.datamgt.search.DictCollectionDisplayTerms"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="com.liferay.util.dao.orm.CustomSQLUtil"%>
<%@page import="org.opencps.datamgt.search.DictCollectionSearchTerms"%>
<%@page import="org.opencps.datamgt.util.DataMgtUtil"%>
<%@ include file="../init.jsp"%>

<liferay-util:include page="/html/portlets/data_management/admin/toptabs.jsp" servletContext="<%=application %>" />
<liferay-util:include page="/html/portlets/data_management/admin/toolbar.jsp" servletContext="<%=application %>" />
<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", "/html/portlets/data_management/admin/dictcollection.jsp");
	iteratorURL.setParameter("tabs1", DataMgtUtil.TOP_TABS_DICTCOLLECTION);
	
	List<DictCollection> dictCollections = new ArrayList<DictCollection>();
	int totalCount = 0;
	
%>

<liferay-ui:search-container searchContainer="<%= new DictCollectionSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">

	<liferay-ui:search-container-results>
		<%
			DictCollectionSearchTerms searchTerms = (DictCollectionSearchTerms)searchContainer.getSearchTerms();
			
			String[] collectionNames = null;
			
			if(Validator.isNotNull(searchTerms.getKeywords())){
				collectionNames = CustomSQLUtil.keywords(searchTerms.getKeywords());
			}
			
			try{
				%>
					<%@include file="/html/portlets/data_management/admin/dictcollection_search_results.jspf" %>
				<%
			}catch(Exception e){
				_log.error(e);
			}
		
			total = totalCount;
			results = dictCollections;
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
	</liferay-ui:search-container-results>	
		<liferay-ui:search-container-row 
			className="org.opencps.datamgt.model.DictCollection" 
			modelVar="dictCollection" 
			keyProperty="dictCollectionId"
		>
			<%
				PortletURL editURL = renderResponse.createRenderURL();
				editURL.setParameter("mvcPath", "/html/portlets/data_management/admin/edit_dictcollection.jsp");
				editURL.setParameter(DictCollectionDisplayTerms.DICTCOLLECTION_ID, String.valueOf(dictCollection.getDictCollectionId()));
				editURL.setParameter("backURL", currentURL);
				
				//id column
				row.addText(String.valueOf(dictCollection.getDictCollectionId()), editURL);
			
				row.addText(dictCollection.getCollectionCode(), editURL);
				row.addText(dictCollection.getCollectionName(locale), editURL);
				row.addText(dictCollection.getDescription(), editURL);
				row.addText(DateTimeUtil.convertDateToString(dictCollection.getCreateDate(), DateTimeUtil._VN_DATE_TIME_FORMAT), editURL);
				row.addText(DateTimeUtil.convertDateToString(dictCollection.getModifiedDate(), DateTimeUtil._VN_DATE_TIME_FORMAT), editURL);
				
				//author column
				
				String authorName = StringPool.BLANK;
				try{
					User author = UserLocalServiceUtil.getUser(dictCollection.getUserId());
					authorName = author.getFullName();
				}catch(Exception e){
					_log.error(e);
				}
				
				row.addText(authorName, editURL);
				
				//action column
				row.addJSP("center",SearchEntry.DEFAULT_VALIGN,"/html/portlets/data_management/admin/dictcollection_actions.jsp", config.getServletContext(), request, response);
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.data_management.admin.dictcollection.jsp");
%>

