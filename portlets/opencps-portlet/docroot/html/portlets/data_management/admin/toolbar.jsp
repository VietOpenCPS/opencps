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

<%@page import="java.util.ArrayList"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.datamgt.search.DictItemSearchTerms"%>
<%@page import="org.opencps.datamgt.permissions.DictCollectionPermission"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.datamgt.permissions.DictItemPermission"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.datamgt.util.DataMgtUtil"%>
<%@page import="org.opencps.datamgt.search.DictItemDisplayTerms"%>
<%@ include file="../init.jsp"%>
<%
	String tabs1 = ParamUtil.getString(request, "tabs1", DataMgtUtil.TOP_TABS_DICTITEM);
	
	PortletURL searchURL = renderResponse.createRenderURL();
	
	long dictCollectionId = ParamUtil.getLong(request, DictItemSearchTerms.DICTCOLLECTION_ID, 0L);
	
	List<DictCollection> dictCollections = new ArrayList<DictCollection>();
	
	try{
		dictCollections = DictCollectionLocalServiceUtil.getDictCollections(scopeGroupId);
		if(dictCollections != null && !dictCollections.isEmpty() && dictCollectionId == 0){
			dictCollectionId = dictCollections.get(0).getDictCollectionId();
		}
	}catch(Exception e){
		_log.error(e);
	}
	
	request.setAttribute(DictItemSearchTerms.DICTCOLLECTION_ID, dictCollectionId);
%>

<aui:nav-bar cssClass="custom-toolbar">
	<aui:nav id="toolbarContainer" cssClass="nav-display-style-buttons pull-left" >
		<c:if test="<%=DictItemPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_DICTITEM) && tabs1.equals(DataMgtUtil.TOP_TABS_DICTITEM)%>">
			<%
				searchURL.setParameter("mvcPath", templatePath + "dictitems.jsp");
				searchURL.setParameter("tabs1", DataMgtUtil.TOP_TABS_DICTITEM);
			%>
			<portlet:renderURL var="addDictItemURL" windowState="<%=LiferayWindowState.NORMAL.toString() %>">
				<portlet:param name="mvcPath" value="/html/portlets/data_management/admin/edit_dictitem.jsp"/>
				<portlet:param name="backURL" value="<%=currentURL %>"/>
			</portlet:renderURL>
			<aui:nav-item 
				id="addDictItem" 
				label="add-dictitem" 
				iconCssClass="icon-plus"  
				href="<%=addDictItemURL %>"
			/>
		</c:if>
		<c:if test="<%=DictCollectionPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_DICTCOLLECTION) && tabs1.equals(DataMgtUtil.TOP_TABS_DICTCOLLECTION)%>">
			<%
				searchURL.setParameter("mvcPath", templatePath + "dictcollection.jsp");
				searchURL.setParameter("tabs1", DataMgtUtil.TOP_TABS_DICTCOLLECTION);
			%>
			<portlet:renderURL var="addDictCollectionURL" windowState="<%=LiferayWindowState.NORMAL.toString() %>">
				<portlet:param name="mvcPath" value="/html/portlets/data_management/admin/edit_dictcollection.jsp"/>
				<portlet:param name="backURL" value="<%=currentURL %>"/>
			</portlet:renderURL>
			<aui:nav-item 
				id="addDictCollection" 
				label="add-dictcollection" 
				iconCssClass="icon-plus"  
				href="<%=addDictCollectionURL %>"
			/>
		</c:if>
	</aui:nav>
	
	<aui:nav-bar-search cssClass="pull-right">
		<div class="form-search">
			<aui:form action="<%= searchURL %>" method="post" name="fm">
				<div class="toolbar_search_input">
					<c:if test="<%=tabs1.equals(DataMgtUtil.TOP_TABS_DICTITEM)%>">
						<%
							searchURL.setParameter(DictItemSearchTerms.DICTCOLLECTION_ID, String.valueOf(dictCollectionId));
						%>
						<aui:select name="<%=DictItemDisplayTerms.DICTCOLLECTION_ID %>" inlineField="true" label="dict-collection">
						<%
							if(dictCollections != null){
								for(DictCollection dictCollection : dictCollections){
									%>
										<aui:option value="<%=dictCollection.getDictCollectionId() %>" selected="<%=dictCollectionId == dictCollection.getDictCollectionId()%>">
											<%=dictCollection.getCollectionName(locale) %>
										</aui:option>
									<%
								}
							}
						%>
					</aui:select> 
					</c:if>
					<liferay-ui:input-search 
						id="keywords1" 
						name="keywords" 
						placeholder='<%= LanguageUtil.get(locale, "name") %>' 
					/>
				</div>
			</aui:form>
		</div>
	</aui:nav-bar-search>
</aui:nav-bar>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.data_management.admin.toolbar.jsp");
%>