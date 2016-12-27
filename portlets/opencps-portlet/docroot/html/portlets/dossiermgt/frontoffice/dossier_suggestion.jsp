

<%@page import="org.opencps.util.AccountUtil"%>
<%@page import="org.opencps.dossiermgt.bean.AccountBean"%>
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
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@page import="com.liferay.portal.kernel.management.jmx.ListDomainsAction"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionErrors"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionMessages"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierPartLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.dao.orm.QueryUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>

<%@ include file="../init.jsp"%>

<%
	long dossierId = ParamUtil.getLong(request, "dossierId");

	List<Dossier> dossiersSuggestion = new ArrayList<Dossier>();

	List<String> templateFileNos = new ArrayList<String>();
	
	List<Integer> dossierPartTypes = new ArrayList<Integer>();
	
	List<String> dossierPartNos = new ArrayList<String>();
	
	int totalCount = 0;

	Dossier dossier = null;
	try {
		dossier = DossierLocalServiceUtil.getDossier(dossierId);
		List<DossierPart> dossierParts = DossierPartLocalServiceUtil.getDossierParts(dossier.getDossierTemplateId());
		if(dossierParts != null){
			for(DossierPart dossierPart : dossierParts){
				if(Validator.isNotNull(dossierPart.getTemplateFileNo()) && !templateFileNos.contains(dossierPart.getTemplateFileNo())){
					templateFileNos.add(dossierPart.getTemplateFileNo());
				}
				
				if(!dossierPartTypes.contains(dossierPart.getPartType())){
					dossierPartTypes.add(dossierPart.getPartType());
				}
				
				if(Validator.isNotNull(dossierPart.getPartNo()) && !dossierPartNos.contains(dossierPart.getPartNo())){
					dossierPartNos.add(dossierPart.getPartNo());
				}
			}
		}
	} catch (Exception e) {
		
	}
	
	String keywords = ParamUtil.getString(request, "keywords");
	
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "dossier_suggestion.jsp");
	iteratorURL.setParameter("dossierId", String.valueOf(dossierId));
	iteratorURL.setParameter("keywords1", keywords);
	
	PortletURL searchURL = renderResponse.createRenderURL();
	searchURL.setParameter("mvcPath", templatePath + "dossier_suggestion.jsp");
	searchURL.setParameter("dossierId", String.valueOf(dossierId));
	searchURL.setParameter("keywords1", keywords);

	boolean success = false;
	
	try{
		success = !SessionMessages.isEmpty(renderRequest) && SessionErrors.isEmpty(renderRequest);
	}catch(Exception e){
		
	}
	
%>


<aui:nav-bar cssClass="custom-toolbar">
	<aui:nav-bar-search cssClass="pull-right">
		<div class="form-search">
			<aui:form action="<%= searchURL %>" method="post" name="fmSearch">
				<aui:row>
						<liferay-ui:input-search 
							id="keywords"
							name="keywords"
							title='<%= LanguageUtil.get(locale, "keywords") %>'
							placeholder='<%= LanguageUtil.get(locale, "keywords") %>' 
						/>
				</aui:row>
			</aui:form>
		</div>
	</aui:nav-bar-search>
</aui:nav-bar>

<div class="opencps-searchcontainer-wrapper" id = "<portlet:namespace/>opencps-searchcontainer-wrapper">
	<liferay-ui:search-container 
			emptyResultsMessage="no-serviceinfo-were-found"
			iteratorURL="<%=iteratorURL %>"
			delta="<%=5 %>"
			deltaConfigurable="true"
		>
		
		<liferay-ui:search-container-results>
			<%
				AccountBean accountBean = AccountUtil.getAccountBean(request);
				long owner = 0;
				
				if(accountBean.isBusiness()) {
					owner = accountBean.getOwnerOrganizationId();
				} else {
					owner = accountBean.getOwnerUserId();
				}
			
				dossiersSuggestion = DossierLocalServiceUtil.getDossierSuggesstion(owner,keywords ,suggestionDossierStatus, dossierPartTypes , templateFileNos,dossierPartNos ,searchContainer.getStart(), searchContainer.getEnd());
				totalCount = DossierLocalServiceUtil.countDossierSuggesstion(owner,keywords, suggestionDossierStatus, dossierPartTypes , templateFileNos,dossierPartNos);
				
				results = dossiersSuggestion;
				total = totalCount;
				pageContext.setAttribute("results", results);
				pageContext.setAttribute("total", total);
			%>
		</liferay-ui:search-container-results>
		
		<liferay-ui:search-container-row 
			className="org.opencps.dossiermgt.model.Dossier" 
			modelVar="dossierSuggestion" 
			keyProperty="dossierId"
		>

			<portlet:renderURL var="viewDossierURL">
				<portlet:param 
					name="mvcPath"
					value='<%=templatePath + "edit_dossier.jsp"%>' 
				/>
				<portlet:param 
					name="<%=DossierDisplayTerms.DOSSIER_ID%>"
					value="<%=String.valueOf(dossierSuggestion.getDossierId())%>" 
				/>
				<portlet:param 
					name="<%=Constants.CMD%>" 
					value="<%=Constants.VIEW%>" 
				/>
				<portlet:param 
					name="isEditDossier" 
					value="<%=String.valueOf(false)%>" 
				/>
			</portlet:renderURL>
										
			<portlet:actionURL var="updateDossierSuggestionURL" name="updateDossierSuggestion">
				<portlet:param name="dossierSuggestionId" value="<%=String.valueOf(dossierSuggestion.getDossierId()) %>"/>
				<portlet:param name="dossierId" value="<%=String.valueOf(dossierId) %>"/>
			</portlet:actionURL>
										
			<% 
				String serviceName = StringPool.BLANK;
			
				ServiceInfo serviceInfo = null;
				try {
					serviceInfo = ServiceInfoLocalServiceUtil.
							getServiceInfo(ServiceConfigLocalServiceUtil.getServiceConfig(dossierSuggestion.getServiceConfigId()).getServiceInfoId());
					serviceName = serviceInfo.getServiceName();
				} catch(Exception e) {
					
				}
				
				String viewDossierUrlNomal = viewDossierURL.toString();
				if(viewDossierUrlNomal.contains("p_p_state=pop_up"))  {
					viewDossierUrlNomal = StringUtil.replace(viewDossierUrlNomal, "p_p_state=pop_up", "p_p_state=nomal");
				}				
				
				List<DossierFile> dossierFiles = new ArrayList<DossierFile>();
				try {
					dossierFiles = DossierFileLocalServiceUtil.getDossierFileSuggesstion(
							dossierSuggestion.getDossierId(), templateFileNos, themeDisplay.getUserId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS);
				} catch (Exception e){
					//e.printStackTrace();
				}
				
				List<String> dossierFileNos = new ArrayList<String>();
				for (DossierFile dossierFile : dossierFiles){
					dossierFileNos.add(dossierFile.getDossierFileNo());
				}
			%>
			<liferay-util:buffer var="boundcol1">
				<%-- <div class="row-fluid">
					
					<div class="span6 bold-label">
						<liferay-ui:message key="dossier-no"/>
					</div>
					<div class="span6"><%=String.valueOf(dossierSuggestion.getDossierId())%></div>
				</div> --%>
				
				<div class="row-fluid">
					
					<div class="span6 bold-label">
						<liferay-ui:message key="reception-no"/>
					</div>
					<div class="span6">
						<%=Validator.isNotNull(dossierSuggestion.getReceptionNo()) ? dossierSuggestion.getReceptionNo() : StringPool.DASH%>
					</div>
				</div>
				
				<div class="row-fluid">
					
					<div class="span6 bold-label">
						<liferay-ui:message key="reception-file-no"/>
					</div>
					<div class="span6">
						<%
							for (String dossierFileNo : dossierFileNos){
								%>
									<%=dossierFileNo %><br>
								<%
							}
						%>
					</div>
				</div>
				
			</liferay-util:buffer>
			
			<liferay-util:buffer var="boundcol2">
				<div class="row-fluid">
					<div class="span5 bold-label">
						<liferay-ui:message key="service-name"/>
					</div>
					<div class="span7"><%= Validator.isNotNull(serviceName) ? serviceName : StringPool.DASH %> </div>
				</div>
			</liferay-util:buffer>
			
			<liferay-util:buffer var="boundcol3">
				<div class="row-fluid">
					<div class="span5 bold-label">
						<liferay-ui:message key="submit-date"/>
					</div>
					
					<div class="span7">
						<%=
							Validator.isNotNull(dossierSuggestion.getSubmitDatetime()) ?
							DateTimeUtil.convertDateToString(dossierSuggestion.getSubmitDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) :
								DateTimeUtil._EMPTY_DATE_TIME 
						%> 
					</div>
				</div>
				
				<div class="row-fluid">
					<div class="span5 bold-label">
						<liferay-ui:message key="finish-date"/>
					</div>
					
					<div class="span7">
						<%= 
							Validator.isNotNull(dossierSuggestion.getFinishDatetime()) ?
							DateTimeUtil.convertDateToString(dossierSuggestion.getFinishDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) :
								DateTimeUtil._EMPTY_DATE_TIME
						%> 
					</div>
				</div>
				
			</liferay-util:buffer>
			
			<liferay-util:buffer var="boundcol4">
				<aui:row>
					<a href="<%=viewDossierUrlNomal.toString()%>" target="_blank" class="btn">
						<i class="fa fa-eye"><liferay-ui:message key="view"/></i>
					</a>
				</aui:row>
				
				<aui:row>
					 <aui:a cssClass="btn"
						href="<%=\"javascript:\" + renderResponse.getNamespace()+ \"loadingMark('\" + updateDossierSuggestionURL.toString() +\"')\" %>" 
					>
						<i class = "fa fa-check"><liferay-ui:message key="choose"/></i>
					</aui:a> 
				</aui:row>
				
				
				 <%-- <aui:button  
					name="dossierSuggestion" 
					value="choose" 
					onClick="<%= renderResponse.getNamespace()+ \"loadingMark('\" + updateDossierSuggestionURL.toString() +\"')\" %>"
				/> --%>
			</liferay-util:buffer>
			
			<%
				row.setClassName("opencps-searchcontainer-row");
				row.addText(String.valueOf(row.getPos() + 1));
				row.addText(boundcol1);
				row.addText(boundcol2);
				row.addText(boundcol3);
				row.addText(boundcol4);
			%>
				
		</liferay-ui:search-container-row>
			
		<liferay-ui:search-iterator type="opencs_page_iterator"/>
	
	</liferay-ui:search-container>
</div>

<aui:script>
	AUI().ready(function(A) {
		var success = <%=success%>;
		
		if(success) {
			closeDialog('submit-dossier-suggesstion', '<%=WebKeys.DOSSIER_MGT_PORTLET%>_');
		}
	});
	
	Liferay.provide(window, '<portlet:namespace/>loadingMark', function(url) {
		var A = AUI();
		var loadingMask = new A.LoadingMask(
			{
				'strings.loading': '<%= UnicodeLanguageUtil.get(pageContext, "rending...") %>',
				target: A.one('#<portlet:namespace/>opencps-searchcontainer-wrapper')
			}
		);
		
		loadingMask.show();
		
		A.io.request(
			url,
			{
				on: {
					success: function(event, id, obj) {
						var response = this.get('responseData');
						loadingMask.hide();
						if(response){
							response = JSON.parse(response);
							if(response.msg == "success"){
								closeDialog('submit-dossier-suggesstion', '<%=WebKeys.DOSSIER_MGT_PORTLET%>_');
							}else{
								alert('<%= UnicodeLanguageUtil.get(pageContext, "error-while-add-dossier-file") %>');
								
							}
						}
					}
				}
			}
		);

	},['aui-loading-mask-deprecated']);
</aui:script>