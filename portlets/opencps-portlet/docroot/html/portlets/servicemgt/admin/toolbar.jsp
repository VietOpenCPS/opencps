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


<%@ include file="../init.jsp"%>

<%
	String tabs1 = ParamUtil.getString(request, "tabs1", ServiceUtil.TOP_TABS_SERVICE);
	PortletURL searchURL = renderResponse.createRenderURL();
%>
<aui:nav-bar cssClass="custom-toolbar">
	<aui:nav id="toolbarContainer" cssClass="nav-display-style-buttons pull-left" >
		<c:choose>
			
			<c:when test="<%= tabs1.contentEquals(ServiceUtil.TOP_TABS_SERVICE) %>">
				
				<portlet:renderURL var="editServiceURL">
					<portlet:param name="mvcPath" value='<%= templatePath + "edit_service.jsp" %>'/>
					<portlet:param name="redirectURL" value="<%=currentURL %>"/>
					<portlet:param name="backURL" value="<%=currentURL %>"/>
				</portlet:renderURL>
				
				<c:if test="<%=ServicePermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_SERVICE) %>">
					<aui:nav-item 
						id="addService" 
						label="add-service" 
						iconCssClass="icon-plus"  
						href="<%=editServiceURL %>"
					/>
				</c:if>
				
			</c:when>
			
			<c:when test="<%= tabs1.contentEquals(ServiceUtil.TOP_TABS_TEMPLATE) %>">
				<portlet:renderURL var="editTemplateURL">
					<portlet:param name="mvcPath" value='<%= templatePath + "edit_template.jsp" %>'/>
					<portlet:param name="redirectURL" value="<%=currentURL %>"/>
					<portlet:param name="backURL" value="<%=currentURL %>"/>
				</portlet:renderURL>
				
				<aui:row>
					<c:if test="<%= ServiceTemplatePermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_TEMPLATE) %>">
						<aui:nav-item 
							id="addTemplate" 
							label="add-template" 
							iconCssClass="icon-plus"  
							href="<%=editTemplateURL %>"
						/>
					</c:if>
				</aui:row>
			</c:when>
			
			<c:when test="<%= tabs1.contentEquals(ServiceUtil.TOP_TABS_DOMAIN) %>">
			
			</c:when>
			
			<c:when test="<%= tabs1.contentEquals(ServiceUtil.TOP_TABS_SERVICE) %>">
				
			</c:when>
			
		</c:choose>
	</aui:nav>
	
	<aui:nav-bar-search cssClass="pull-right">
		<div class="form-search">
			<aui:form action="<%= searchURL %>" method="post" name="fm">
				<div class="toolbar_search_input">
					<c:choose>
						
						<c:when test="<%= tabs1.contentEquals(ServiceUtil.TOP_TABS_SERVICE) %>">
								
								<aui:row>
									<aui:col width="25">
										<datamgt:ddr cssClass="input30"
											depthLevel="1" 
											dictCollectionCode="SERVICE_ADMINISTRATION"
											itemNames="<%= ServiceDisplayTerms.SERVICE_ADMINISTRATION %>"
											itemsEmptyOption="true"
												
										>
										</datamgt:ddr>
		
									</aui:col>
									<aui:col width="25">
										<datamgt:ddr cssClass="input30"
											depthLevel="1" 
											dictCollectionCode="SERVICE_DOMAIN"
											itemNames="<%= ServiceDisplayTerms.SERVICE_DOMAINCODE %>"
											itemsEmptyOption="true"	
										>
										</datamgt:ddr>

									</aui:col>
									<aui:col width="45">
										<label>
											<liferay-ui:message key="keywords"/>
										</label>
										<liferay-ui:input-search 
											id="keywords1"
											name="keywords"
											title="keywords"
											placeholder='<%= LanguageUtil.get(locale, "name") %>' 
										/>
									</aui:col>
								</aui:row>
								
						</c:when>
						
						<c:when test="<%= tabs1.contentEquals(ServiceUtil.TOP_TABS_TEMPLATE) %>">
							<liferay-ui:input-search 
								id="keywords1" 
								name="keywords" 
								placeholder='<%= LanguageUtil.get(locale, "name") %>' 
							/>
						</c:when>
						
						<c:when test="<%= tabs1.contentEquals(ServiceUtil.TOP_TABS_DOMAIN) %>">
						
						</c:when>
						
						<c:when test="<%= tabs1.contentEquals(ServiceUtil.TOP_TABS_SERVICE) %>">
							
						</c:when>
						
					</c:choose>
				</div>
			</aui:form>
		</div>
	</aui:nav-bar-search>
</aui:nav-bar>


<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.servicemgt.admin.toolbar.jsp");
%>