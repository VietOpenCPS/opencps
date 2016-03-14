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
<%@page import="org.opencps.datamgt.model.impl.DictCollectionImpl"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="org.opencps.datamgt.EmptyItemCodeException"%>
<%@page import="org.opencps.datamgt.OutOfLengthItemCodeException"%>
<%@page import="org.opencps.datamgt.EmptyDictItemNameException"%>
<%@page import="org.opencps.datamgt.OutOfLengthItemNameException"%>
<%@page import="org.opencps.datamgt.DuplicateItemException"%>
<%@page import="org.opencps.datamgt.NoSuchDictItemException"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.datamgt.search.DictItemDisplayTerms"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@ include file="../init.jsp"%>

<portlet:actionURL var="updateDictItemURL" name="updateDictItem" />

<%
	DictItem dictItem = (DictItem)request.getAttribute(WebKeys.DICT_ITEM_ENTRY);
	long dictItemId = dictItem != null ? dictItem.getDictItemId() : 0L;
	String backURL = ParamUtil.getString(request, "backURL");
	
	List<DictCollection> dictCollections = new ArrayList<DictCollection>();
	List<DictItem> dictItems = new ArrayList<DictItem>();
	
	try{
		dictCollections = DictCollectionLocalServiceUtil.getDictCollections(scopeGroupId);
	}catch(Exception e){
		_log.error(e);
	}
	
	/* try{
		dictItems = DictItemLocalServiceUtil.getDictItemsByDictCollectionId(dictCollectionId)
	}catch(Exception e){
		_log.error(e);
	} */
%>

<liferay-ui:header
	backURL="<%= backURL %>"
	title='<%= (dictItem == null) ? "add-dictitem" : "update-dictitem" %>'
/>


<div class="opencps-datamgt dictitem-wrapper">
	<div class="edit-form">
		<liferay-ui:error exception="<%= EmptyItemCodeException.class %>" message="<%=EmptyItemCodeException.class.getName() %>" />
		<liferay-ui:error exception="<%= OutOfLengthItemCodeException.class %>" message="<%=OutOfLengthItemCodeException.class.getName() %>" />
		<liferay-ui:error exception="<%= EmptyDictItemNameException.class %>" message="<%=EmptyDictItemNameException.class.getName() %>" />
		<liferay-ui:error exception="<%= OutOfLengthItemNameException.class %>" message="<%=OutOfLengthItemNameException.class.getName() %>" />
		<liferay-ui:error exception="<%= DuplicateItemException.class %>" message="<%=DuplicateItemException.class.getName() %>" />
		<liferay-ui:error exception="<%= NoSuchDictItemException.class %>" message="<%=NoSuchDictItemException.class.getName() %>" />
		<liferay-ui:error key="<%= MessageKeys.DATAMGT_SYSTEM_EXCEPTION_OCCURRED%>" message="<%=MessageKeys.DATAMGT_SYSTEM_EXCEPTION_OCCURRED %>" />

		<aui:form action="<%=updateDictItemURL.toString() %>" method="post" name="fm">
			
			<aui:model-context bean="<%=dictItem %>" model="<%=DictItem.class %>" />
			<aui:input name="<%=DictItemDisplayTerms.DICTITEM_ID %>" type="hidden"/>
			<aui:input name="redirectURL" type="hidden" value="<%=backURL %>"/>
			<aui:input name="returnURL" type="hidden" value="<%=currentURL %>"/>
			<aui:fieldset>
			
				<aui:input name="<%=DictItemDisplayTerms.ITEM_NAME %>" cssClass="input80" label="item-name">
					<aui:validator name="required"/>
					<aui:validator name="minLength">3</aui:validator>
					<aui:validator name="maxLength">255</aui:validator>
				</aui:input>
				
				<aui:input name="<%=DictItemDisplayTerms.ITEM_CODE%>" type="text" cssClass="input20">
					<aui:validator name="required"/>
					<aui:validator name="maxLength">100</aui:validator> 
				</aui:input>
				
				<aui:select name="<%=DictItemDisplayTerms.DICTCOLLECTION_ID %>" label="dict-collection">
					<%
						if(dictCollections != null){
							for(DictCollection dictCollection : dictCollections){
								%>
									<aui:option value="<%=dictCollection.getDictCollectionId() %>">
										<%=dictCollection.getCollectionName(locale) %>
									</aui:option>
								<%
							}
						}
					%>
				</aui:select> 
				<div id='<%=renderResponse.getNamespace() + "parentItem" %>'>
					<aui:select name="<%=DictItemDisplayTerms.PARENTITEM_ID %>" label="parent-item">
						<aui:option value="0"></aui:option>
					</aui:select>
				</div>
				<aui:select name="<%=DictItemDisplayTerms.DICTVERSION_ID %>" label="dict-version">
					<aui:option value="0"></aui:option>
				</aui:select>
				
			</aui:fieldset>
			
			<aui:fieldset>
				<aui:button type="submit" name="submit" value="submit"/>
				<aui:button type="reset" value="clear"/>
			</aui:fieldset>	
		</aui:form>
	</div>
</div>
<aui:script>
	AUI().ready('aui-base','liferay-portlet-url','aui-io',function(A){
		
		var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.DATA_MANAGEMENT_ADMIN_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
		portletURL.setParameter("mvcPath", "/html/portlets/data_management/admin/select_dictitems.jsp");
		portletURL.setWindowState("<%=LiferayWindowState.EXCLUSIVE.toString()%>"); 
		portletURL.setPortletMode("normal");
		
		
		var dictCollection = A.one('#<portlet:namespace/><%=DictItemDisplayTerms.DICTCOLLECTION_ID%>');
		
		if(dictCollection){
			
			var dictCollectionId = dictCollection.val();
			var dictItemId = '<%=dictItemId%>';
			portletURL.setParameter("dictCollectionId", dictCollectionId);
			portletURL.setParameter("dictItemId", dictItemId);
			getDictItems(portletURL.toString());
			
			dictCollection.on('change', function(){
				
				dictCollectionId = dictCollection.val();
				
				portletURL.setParameter("dictCollectionId", dictCollectionId);
				portletURL.setParameter("dictItemId", dictItemId);
				
				getDictItems(portletURL.toString());
			});
		}
	});
	
	Liferay.provide(window, 'getDictItems', function(url) {
		var A = AUI();
		A.io.request(
			url,
			{
			    dataType: 'json',
			    data:{    	
			                	
			    },   
			    on: {
			        success: function(event, id, obj) {
						var instance = this;
						var dictItems = instance.get('responseData');
						var parentItemContainer = A.one("#<portlet:namespace/>parentItem");
						if(parentItemContainer){
							parentItemContainer.empty();
							parentItemContainer.html(dictItems);
						}
							
					},
			    	error: function(){}
				}
			}
		);
	},['aui-base','liferay-portlet-url','aui-io']);
	
</aui:script>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.data_management.admin.edit_dictitem.jsp");
%>
