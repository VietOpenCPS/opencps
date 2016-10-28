
<%@page import="org.opencps.dossiermgt.search.DossierPartDisplayTerms"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
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

<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="org.opencps.processmgt.util.ProcessMgtUtil"%>
<%@page import="org.opencps.processmgt.search.ProcessDisplayTerms"%>
<%@page import="org.opencps.servicemgt.util.ServiceUtil"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>

<%@ include file="../init.jsp"%>

<%
	String tabs1 = ParamUtil.getString(request, "tabs1", ProcessMgtUtil.TOP_TABS_DOSSIERLIST);	
	
	String dossierStatus = ParamUtil.getString(request, DossierDisplayTerms.DOSSIER_STATUS, StringPool.BLANK);
	
	PortletURL searchURL = renderResponse.createRenderURL();	
	searchURL.setParameter("tabs1", tabs1);

	if(tabs1.equals(ProcessMgtUtil.TOP_TABS_DOSSIERLIST)){
		searchURL.setParameter("mvcPath", templatePath + "backofficedossierlist.jsp");
	}else{
		searchURL.setParameter("mvcPath", templatePath + "backofficedossierfilelist.jsp");
	}
	
	DictItem curDictItem = null;
	
	DictCollection dictCollection = DictCollectionLocalServiceUtil.
			getDictCollection(themeDisplay.getScopeGroupId(), PortletPropsValues.DATAMGT_MASTERDATA_SERVICE_DOMAIN);
	
	List<DictItem> dictItems = DictItemLocalServiceUtil.getDictItemsByDictCollectionId(dictCollection.getDictCollectionId());
	
%>
<aui:nav-bar cssClass="opencps-toolbar custom-toolbar">
	<aui:nav-bar-search cssClass="pull-right front-custom-select-search" style="width: 98%;">
		<div class="form-search">
			<aui:form 
				action="<%= searchURL %>"
				name="fmSearch" 
			>
				<c:choose>
					<c:when test="<%=tabs1.equals(ProcessMgtUtil.TOP_TABS_DOSSIERLIST) %>">
						<aui:row>
							<aui:col width="30" cssClass="search-col">
										
								<aui:select name="<%=DossierDisplayTerms.SERVICE_DOMAIN_CODE %>" label="" cssClass="search-input select-box input100" >
									<aui:option value="">
										<liferay-ui:message key="filter-by-service-domain"/>
									</aui:option>
									<%
										if(dictItems != null){
											for(DictItem dictItem : dictItems){
												if((curDictItem != null && dictItem.getDictItemId() == curDictItem.getDictItemId())||
													(curDictItem != null && dictItem.getTreeIndex().contains(curDictItem.getDictItemId() + StringPool.PERIOD))){
													continue;
											}
															
											int level = StringUtil.count(dictItem.getTreeIndex(), StringPool.PERIOD);
											String index = "|";
											for(int i = 0; i < level; i++){
												index += "_";
											}
									%>
										<aui:option value="<%=dictItem.getDictItemId() %>"><%=index + dictItem.getItemName(locale) %></aui:option>
									<%
											}
										}
									%>
								</aui:select>
							</aui:col>
							<aui:col width="30" cssClass="search-col">
									
								<datamgt:ddr 
									depthLevel="1" 
									dictCollectionCode="DOSSIER_STATUS" 
									showLabel="<%=false%>"
									emptyOptionLabels="dossier-status"
									itemsEmptyOption="true"
									itemNames="<%=DossierDisplayTerms.DOSSIER_STATUS %>"
									selectedItems="<%=dossierStatus%>"
									optionValueType="code"
									cssClass="search-input select-box input100"
									
								/>
											
							</aui:col>
							<aui:col width="30" cssClass="search-col">
								<liferay-ui:input-search 
									id="keywords"
									name="keywords"
									title='<%= LanguageUtil.get(locale, "keywords") %>'
									placeholder='<%=LanguageUtil.get(locale, "keywords") %>'
									cssClass="search-input input-keyword"
								/>
							</aui:col>
						</aui:row>
					</c:when>
					<c:otherwise>
						<%
							long dossierTemplateId = ParamUtil.getLong(request, ProcessDisplayTerms.PROCESS_DOSSIERTEMPLATE_ID, -1);
						
							List<DossierTemplate> lsTemplates = DossierTemplateLocalServiceUtil.getAll();
									
							boolean onlyViewFileResult = ParamUtil.getBoolean(request, "onlyViewFileResult");
						%>
						<aui:row>
							<aui:col width="30" cssClass="search-col">
										
								<aui:input checked="<%= onlyViewFileResult %>" name="onlyViewFileResult" value="false" type="checkbox" 
									label="<%=LanguageUtil.get(pageContext, \"only-view-file-result\") %>" 
									inlineField="<%=true %>"
									inlineLabel="true" />
								
							</aui:col>
							<aui:col width="30" cssClass="search-col">
									
								<aui:select label="" name="<%= ProcessDisplayTerms.PROCESS_DOSSIERTEMPLATE_ID %>" style="width: 100%;">
											<aui:option value="<%= DossierMgtUtil.DOSSIERFILETYPE_ALL %>">
												<liferay-ui:message key="filter-by-dossier-template"></liferay-ui:message>
											</aui:option>
											<%
												for (DossierTemplate template : lsTemplates) {
											%>
														<aui:option selected="<%=dossierTemplateId == template.getDossierTemplateId()?true:false %>" value="<%= template.getDossierTemplateId() %>"><%= template.getTemplateName() %></aui:option>
											<% 
												}
											%>
										</aui:select>	
							</aui:col>
							<aui:col width="30" cssClass="search-col">
								<liferay-ui:input-search 
									id="keywords"
									name="keywords"
									title='<%= LanguageUtil.get(locale, "keywords") %>'
									placeholder='<%=LanguageUtil.get(locale, "keywords") %>'
									cssClass="search-input input-keyword"
								/>
							</aui:col>
						</aui:row>
					</c:otherwise>
				</c:choose>
			</aui:form>
		</div>
	</aui:nav-bar-search>
</aui:nav-bar>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.processmgt.backofficedossier.toolbar.jsp");
%>