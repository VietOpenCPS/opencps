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
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */
%>
<%@ include file="../init.jsp"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.datamgt.search.DictItemSearch"%>

<div class="ocps-serviceinfo-list">

<liferay-util:include page='<%= templatePath + "toptabs.jsp" %>' servletContext="<%=application %>" />

<liferay-util:include page='<%= templatePath + "toolbar.jsp"%>' servletContext="<%=application %>" />
<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "templatefileserviceinfo.jsp");
	iteratorURL.setParameter("tabs1", ServiceUtil.TOP_TABS_DOMAIN);
	List<DictItem> dictItems = new ArrayList<DictItem>();
	
	DictCollection dictCollection = null;
	try {
		dictCollection = DictCollectionLocalServiceUtil.getDictCollection(scopeGroupId, PortletPropsValues.DATAMGT_MASTERDATA_SERVICE_DOMAIN);
	} catch (Exception e) {
		
	}
	
	int totalCount = 0;
%>
<liferay-ui:search-container searchContainer="<%= new DictItemSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">
	<liferay-ui:search-container-results>
		<%
			if(dictCollection != null) {
				dictItems = DictItemLocalServiceUtil.getDictItemsByDictCollectionId(dictCollection.getDictCollectionId());
				totalCount = DictItemLocalServiceUtil.countByDictCollectionId(dictCollection.getDictCollectionId());
			}
			results = dictItems;
			total = totalCount;
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
	</liferay-ui:search-container-results>
		<liferay-ui:search-container-row 
			className="org.opencps.datamgt.model.DictItem" 
			modelVar="dictItem" 
			keyProperty="dictItemId"
		>
			<liferay-ui:search-container-column-text 
				name="row-index" value="<%= String.valueOf(row.getPos() + 1) %>"
			/>
			
			<liferay-ui:search-container-column-text 
				name="service-number" value="<%= dictItem.getItemCode() %>"
			/>
			
			<liferay-ui:search-container-column-text 
				name="service-name" value="<%= dictItem.getItemName(locale, true) %>"
			/>
			
			<liferay-ui:search-container-column-jsp path='<%=templatePath + "domain_actions.jsp" %>' name="action"/>
		</liferay-ui:search-container-row>
<liferay-ui:search-iterator type="opencs_page_iterator"/>
</liferay-ui:search-container>
</div>