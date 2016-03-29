
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
<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@page import="javax.swing.plaf.ListUI"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="com.liferay.util.dao.orm.CustomSQLUtil"%>
<%@page import="org.opencps.datamgt.search.DictItemDisplayTerms"%>
<%@page import="org.opencps.datamgt.search.DictItemSearchTerms"%>
<%@page import="org.opencps.datamgt.search.DictItemSearch"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.datamgt.util.DataMgtUtil"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.datamgt.service.DictVersionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictVersion"%>
<%@ include file="../init.jsp"%>

<liferay-util:include page="/html/portlets/data_management/admin/toptabs.jsp" servletContext="<%=application %>" />
<liferay-util:include page="/html/portlets/data_management/admin/toolbar.jsp" servletContext="<%=application %>" />
<%
	long dictCollectionId = (Long)request.getAttribute(DictItemSearchTerms.DICTCOLLECTION_ID);

	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", "/html/portlets/data_management/admin/dictitems.jsp");
	iteratorURL.setParameter("tabs1", DataMgtUtil.TOP_TABS_DICTITEM);
	
	List<DictItem> dictItems = new ArrayList<DictItem>();
	
	int totalCount = 0;
	
%>

<liferay-ui:search-container searchContainer="<%= new DictItemSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">

	<liferay-ui:search-container-results>
		<%
			DictItemSearchTerms searchTerms = (DictItemSearchTerms)searchContainer.getSearchTerms();
			
			String[] itemNames = null;
			
			if(Validator.isNotNull(searchTerms.getKeywords())){
				itemNames = CustomSQLUtil.keywords(searchTerms.getKeywords());
			}
			
			try{
				
				%>
					<%@include file="/html/portlets/data_management/admin/dictitem_search_results.jspf" %>
				<%
			}catch(Exception e){
				_log.error(e);
			}
		
			total = totalCount;
			results = dictItems;
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
	</liferay-ui:search-container-results>	
		<liferay-ui:search-container-row 
			className="org.opencps.datamgt.model.DictItem" 
			modelVar="dictItem" 
			keyProperty="dictItemId"
		>
			<%
				PortletURL editURL = renderResponse.createRenderURL();
				editURL.setParameter("mvcPath", "/html/portlets/data_management/admin/edit_dictitem.jsp");
				editURL.setParameter(DictItemDisplayTerms.DICTITEM_ID, String.valueOf(dictItem.getDictItemId()));
				editURL.setParameter("backURL", currentURL);
				
				//id column
				row.addText(String.valueOf(dictItem.getDictItemId()), editURL);
			
				row.addText(dictItem.getItemCode(), editURL);
				row.addText(dictItem.getItemName(locale), editURL);
				
				row.addText(dictItem.getTreeIndex(), editURL);
				
				row.addText(DateTimeUtil.convertDateToString(dictItem.getCreateDate(), DateTimeUtil._VN_DATE_TIME_FORMAT), editURL);
				row.addText(DateTimeUtil.convertDateToString(dictItem.getModifiedDate(), DateTimeUtil._VN_DATE_TIME_FORMAT), editURL);
				
				// version column
				String versionName = StringPool.BLANK;
				
				try{
					DictVersion dictVersion = DictVersionLocalServiceUtil.getDictVersion(dictItem.getDictItemId());
					if(dictVersion != null){
						versionName = dictVersion.getVersion();
					}
				}catch(Exception e){
					// Nothing todo
				}
				
				row.addText(versionName, editURL);
				
				//author column
				String authorName = StringPool.BLANK;
				try{
					User author = UserLocalServiceUtil.getUser(dictItem.getUserId());
					authorName = author.getFullName();
				}catch(Exception e){
					_log.error(e);
				}
				
				row.addText(authorName, editURL);
				
				//inuse column
				String status = LanguageUtil.get(locale, "draft");
				
				if(dictItem.getIssueStatus() == PortletConstants.DRAFTING){
					status = LanguageUtil.get(locale, "draft");
				}else if(dictItem.getIssueStatus() == PortletConstants.INUSE){
					status = LanguageUtil.get(locale, "inuse");
				}else if(dictItem.getIssueStatus() == PortletConstants.EXPIRED){
					status = LanguageUtil.get(locale, "expired");
				}else{
					status = LanguageUtil.get(locale, "unknown");
				}
				
				row.addText(status, editURL);
				
				//action column
				row.addJSP("center",SearchEntry.DEFAULT_VALIGN,"/html/portlets/data_management/admin/dictitem_actions.jsp", config.getServletContext(), request, response);
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.data_management.admin.dictitem.jsp");
%>

