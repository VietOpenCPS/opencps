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
<%@page import="com.liferay.portal.kernel.util.ArrayUtil"%>
<%@page import="org.opencps.jasperreport.util.JRReportUtil.DocType"%>

<%@ include file="init.jsp"%>

<%
	String tabs2 = ParamUtil.getString(request, "tabs2", "todolist");
	String tabs2Names = "todolist,justfinishedlist,processorder,digital-signature";
%>

<liferay-portlet:renderURL portletConfiguration="true" var="configurationRenderURL">
	<portlet:param name="tabs2" value="<%= tabs2 %>" />
</liferay-portlet:renderURL>

<liferay-ui:success key="potlet-config-saved" message="portlet-configuration-have-been-successfully-saved" />

<liferay-portlet:actionURL var="configurationActionURL" portletConfiguration="true">
	<portlet:param name="tabs2" value="<%= tabs2 %>" />
</liferay-portlet:actionURL>


<liferay-ui:tabs
	names="<%= tabs2Names %>"
	param="tabs2"
	url="<%= configurationRenderURL %>"
	tabsValues="<%=tabs2Names %>"
/>


<aui:form action="<%= configurationActionURL %>" method="post" name="configurationForm">

	<c:choose>
		<c:when test='<%=tabs2.equalsIgnoreCase("todolist") %>'>
			<%-- <liferay-util:include page="/html/portlets/processmgt/processorder/configuration/todolist.jsp.jsp" servletContext="<%=this.getServletContext() %>" portletId="16_WAR_opencpsportlet"/> --%>
			<liferay-ui:panel-container 
				extended="<%= true %>" 
				id="todolistSelectionPanelContainer" 
				persistState="<%= true %>"
			>
				<liferay-ui:panel 
					collapsible="<%= true %>" 
					extended="<%= true %>" 
					id="todolistDisplayPanel" 
					persistState="<%= true %>" 
					title="display-style">
					
					<aui:fieldset>
						<aui:select name="todolistDisplayStyle" id="todolistDisplayStyle">		
							<aui:option selected="<%= todolistDisplayStyle.equals(\"default\") %>" value="default">
								<liferay-ui:message key="default"/>
							</aui:option>
								
							<aui:option selected="<%= todolistDisplayStyle.equals(\"treemenu_left\") %>" value="treemenu_left">
								<liferay-ui:message key="treemenu-left"/>
							</aui:option>
						</aui:select>
					</aui:fieldset>
					
					<aui:fieldset>
						<aui:select name="assignFormDisplayStyle">
							<aui:option 
								value="popup"
								selected='<%=assignFormDisplayStyle.equals("popup") %>'
							>
								<liferay-ui:message key="popup"/>
							</aui:option>
							<aui:option 
								value="form"
								selected='<%=assignFormDisplayStyle.equals("form") %>'
							>
								<liferay-ui:message key="form"/>
							</aui:option>
						</aui:select>
					</aui:fieldset>
					
					<aui:fieldset>
						<aui:input 
							type="checkbox"
							name="hiddenToDoListTreeMenuEmptyNode" 
							value="<%=hiddenToDoListTreeMenuEmptyNode %>"
						/>
					</aui:fieldset>
				</liferay-ui:panel>
				
				<liferay-ui:panel 
					collapsible="<%= true %>" 
					extended="<%= true %>" 
					id="todolistOrderPanel" 
					persistState="<%= true %>" 
					title="order-by"
				>
					<aui:row>
						<aui:col width="50">
							<aui:select name="todolistOrderByField">
								<aui:option value='<%=ProcessOrderDisplayTerms.MODIFIEDDATE %>' selected='<%= todolistOrderByField.equals(ProcessOrderDisplayTerms.MODIFIEDDATE) %>'>
									<liferay-ui:message key="order-by-modified-date"/>
								</aui:option>
							</aui:select>
						</aui:col>
						
						<aui:col width="50">
							<aui:select name="todolistOrderByType">
								<aui:option value="<%= WebKeys.ORDER_BY_ASC %>" selected='<%= todolistOrderByType.equals(WebKeys.ORDER_BY_ASC) %>'>
									<liferay-ui:message key="order-by-old-dossier"/>
								</aui:option>
								
								<aui:option value="<%= WebKeys.ORDER_BY_DESC %>" selected='<%= todolistOrderByType.equals(WebKeys.ORDER_BY_DESC) %>'>
									<liferay-ui:message key="order-by-new-dossier"/>
								</aui:option>
							</aui:select>
						</aui:col>
					</aui:row>
				</liferay-ui:panel>
			</liferay-ui:panel-container>
		</c:when>
		
		<c:when test='<%=tabs2.equalsIgnoreCase("justfinishedlist") %>'>
			<liferay-ui:panel-container 
				extended="<%= true %>" 
				id="justfinishedlistSelectionPanelContainer" 
				persistState="<%= true %>"
			>
				<liferay-ui:panel 
					collapsible="<%= true %>" 
					extended="<%= true %>" 
					id="justfinishedlistDisplayPanel" 
					persistState="<%= true %>" 
					title="display">
					<aui:fieldset>
						<aui:select name="justfinishedlistDisplayStyle" id="justfinishedlistDisplayStyle">		
							<aui:option selected="<%= justfinishedlistDisplayStyle.equals(\"default\") %>" value="default">
								<liferay-ui:message key="default"/>
							</aui:option>
								
							<aui:option selected="<%= justfinishedlistDisplayStyle.equals(\"treemenu_left\") %>" value="treemenu_left">
								<liferay-ui:message key="treemenu-left"/>
							</aui:option>
						</aui:select>
					</aui:fieldset>
					
					<aui:fieldset>
						<aui:input 
							type="checkbox"
							name="hiddenJustFinishedListEmptyNode" 
							value="<%=hiddenJustFinishedListEmptyNode %>"
						/>
					</aui:fieldset>
				</liferay-ui:panel>
				
				<liferay-ui:panel 
					collapsible="<%= true %>" 
					extended="<%= true %>" 
					id="justfinishedlistOrderPanel" 
					persistState="<%= true %>" 
					title="order-by"
				>
					<aui:row>
						<aui:col width="50">
							<aui:select name="justfinishedlistOrderByField">
								<aui:option value='<%=ProcessOrderDisplayTerms.MODIFIEDDATE %>' selected='<%= justfinishedlistOrderByField.equals(ProcessOrderDisplayTerms.MODIFIEDDATE) %>'>
									<liferay-ui:message key="order-by-modified-date"/>
								</aui:option>
							</aui:select>
						</aui:col>
						
						<aui:col width="50">
							<aui:select name="justfinishedlistOrderByType">
								<aui:option value="<%= WebKeys.ORDER_BY_ASC %>" selected='<%= justfinishedlistOrderByType.equals(WebKeys.ORDER_BY_ASC) %>'>
									<liferay-ui:message key="order-by-old-dossier"/>
								</aui:option>
								
								<aui:option value="<%= WebKeys.ORDER_BY_DESC %>" selected='<%= justfinishedlistOrderByType.equals(WebKeys.ORDER_BY_DESC) %>'>
									<liferay-ui:message key="order-by-new-dossier"/>
								</aui:option>
							</aui:select>
						</aui:col>
					</aui:row>
				</liferay-ui:panel>
			</liferay-ui:panel-container>
		</c:when>
		
		<c:when test='<%=tabs2.equalsIgnoreCase("processorder") %>'>
			<liferay-ui:panel-container 
				extended="<%= true %>" 
				id="processorderSelectionPanelContainer" 
				persistState="<%= true %>"
			>
				<liferay-ui:panel 
					collapsible="<%= true %>" 
					extended="<%= true %>" 
					id="processorderExportReportPanel" 
					persistState="<%= true %>" 
					title="report">
					<aui:fieldset>
						<aui:select name="reportType" id="reportType" multiple="true">		
							<%
								for(DocType docType : DocType.values()){
									%>
										<aui:option value="<%=docType.getValue() %>" selected="<%=ArrayUtil.contains(reportTypes, docType.getValue()) %>">
											<%=docType.getValue() %>
										</aui:option>
									<%
								}
							%>
						</aui:select>
					</aui:fieldset>
				</liferay-ui:panel>
				
				<liferay-ui:panel 
					collapsible="<%= true %>" 
					extended="<%= true %>" 
					id="processOrderViewerPanel" 
					persistState="<%= true %>" 
					title="process-order-viewer-panel"
				>
					<aui:row>
						<aui:select name="processOrderViewer" id="processOrderViewer">
							<aui:option value="default"
								selected='<%=processOrderDetailPage.equals("default") %>'
							><%=LanguageUtil.get(pageContext, "default") %></aui:option>
							<aui:option value="process_order_detail"
								selected='<%=processOrderDetailPage.equals("process_order_detail") %>'
							><%=LanguageUtil.get(pageContext, "process_order_detail") %></aui:option>
						</aui:select>
					</aui:row>
				</liferay-ui:panel>
			</liferay-ui:panel-container>
		</c:when>
		
		<c:when test='<%=tabs2.equalsIgnoreCase("digital-signature") %>'>
			<aui:fieldset>
				<aui:input 
					type="checkbox"
					name="assign-task-after-sign" 
					value="<%=assignTaskAfterSign %>"
				/>
			</aui:fieldset>
			
			<aui:fieldset>
				<aui:row>
					<aui:col width="30">
						<aui:input name="imageZoom" value="<%=imageZoom %>"/>
					</aui:col>
					<aui:col width="30">
						<aui:input name="offsetX" value="<%=offsetX %>"/>
					</aui:col>
					<aui:col width="30">
						<aui:input name="offsetY" value="<%=offsetY %>"/>
					</aui:col>
				</aui:row>
			</aui:fieldset>
		</c:when>
		
		<c:otherwise>
		
			
		</c:otherwise>
	</c:choose>
	
	<aui:button type="submit" name="Save" value="save"/>

</aui:form>
