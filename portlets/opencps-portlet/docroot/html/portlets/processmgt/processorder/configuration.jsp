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
<%@ include file="init.jsp"%>

<%

	String templatesToDisplay_cfg = GetterUtil.getString(portletPreferences.getValue("templatesToDisplay", "default"));

%>

<liferay-ui:success key="potlet-config-saved" message="portlet-configuration-have-been-successfully-saved" />

<liferay-portlet:actionURL var="configurationActionURL" portletConfiguration="true"/>

<aui:form action="<%= configurationActionURL %>" method="post" name="configurationForm">
	<aui:row>
		<aui:col width="50">
			<aui:select name="oderFieldToDo">
				<aui:option value='<%=ProcessOrderDisplayTerms.MODIFIEDDATE %>' selected='<%= oderFieldToDo.equals(ProcessOrderDisplayTerms.MODIFIEDDATE) %>'>
					<liferay-ui:message key="order-by-modified-date"/>
				</aui:option>
				
			</aui:select>
		</aui:col>
		<aui:col width="50">
			<aui:select name="oderByToDo">
				<aui:option value="<%= WebKeys.ORDER_BY_ASC %>" selected='<%= oderByToDo.equals(WebKeys.ORDER_BY_ASC) %>'>
					<liferay-ui:message key="order-by-old-dossier"/>
				</aui:option>
				
				<aui:option value="<%= WebKeys.ORDER_BY_DESC %>" selected='<%= oderByToDo.equals(WebKeys.ORDER_BY_DESC) %>'>
					<liferay-ui:message key="order-by-new-dossier"/>
				</aui:option>
			</aui:select>
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:col width="50" >
			<aui:select name="oderFieldJustFinish">
				<aui:option value='<%=ProcessOrderDisplayTerms.MODIFIEDDATE %>' selected='<%= oderFieldJustFinish.equals(ProcessOrderDisplayTerms.MODIFIEDDATE) %>'>
					<liferay-ui:message key="order-by-modified-date"/>
				</aui:option>
				
			</aui:select>
		</aui:col>
		<aui:col width="50">
			<aui:select name="oderByJustFinish">
				<aui:option value="<%= WebKeys.ORDER_BY_ASC %>" selected='<%= oderByJustFinish.equals(WebKeys.ORDER_BY_ASC ) %>'>
					<liferay-ui:message key="order-by-old-dossier"/>
				</aui:option>
				
				<aui:option value="<%= WebKeys.ORDER_BY_DESC %>" selected='<%= oderByJustFinish.equals(WebKeys.ORDER_BY_DESC)%>'>
					<liferay-ui:message key="order-by-new-dossier" />
				</aui:option>
			</aui:select>
		</aui:col>
	</aui:row>

	<aui:select name="templatesToDisplay" id="templatesToDisplay">
			
			<aui:option selected="<%= templatesToDisplay_cfg.equals(\"default\") %>" value="default">default</aui:option>
			
			<aui:option selected="<%= templatesToDisplay_cfg.equals(\"20_80\") %>" value="20_80">20_80</aui:option>
	
	</aui:select>
	<aui:input 
		type="checkbox"
		name="hiddenTreeNodeEqualNone" 
		value="<%=hiddenTreeNodeEqualNone %>"
	/>
	<aui:button type="submit" name="Save" value="save"/>
</aui:form>
