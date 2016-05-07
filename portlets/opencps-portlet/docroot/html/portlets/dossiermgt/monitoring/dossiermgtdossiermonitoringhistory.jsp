<%@page import="java.util.List"%>
<%@page import="org.opencps.processmgt.service.ProcessOrderLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.ProcessOrder"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="com.liferay.portal.kernel.dao.orm.QueryUtil"%>
<%@page import="org.opencps.processmgt.service.ActionHistoryLocalServiceUtil"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.processmgt.model.ActionHistory"%>
<%@page import="org.opencps.processmgt.search.ActionHistorySearchTerms"%>
<%@page import="org.opencps.processmgt.search.ActionHistorySearch"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.dossiermgt.model.impl.DossierImpl"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@ include file="../init.jsp"%>

<liferay-util:include page="<%=templatePath + \"toptabs.jsp\" %>" servletContext="<%=application %>" />

<%
	String tabs1 = ParamUtil.getString(request, "tabs1");
	String dossierId = ParamUtil.getString(request, DossierDisplayTerms.DOSSIER_ID);
	String redirectURL = ParamUtil.getString(request, WebKeys.REDIRECT);
	Dossier dossier = new DossierImpl();
	if(Validator.isNotNull(dossierId)){
		dossier = DossierLocalServiceUtil.fetchDossier(Long.valueOf(dossierId));
// 		ProcessOrder processOrder = ProcessOrderLocalServiceUtil.fe
	}
	String tenThuTuc = StringPool.BLANK;
	if(Validator.isNotNull(dossier) && dossier.getServiceInfoId() > 0){
		ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil.fetchServiceInfo(dossier.getServiceInfoId());
		tenThuTuc = Validator.isNotNull(serviceInfo)?serviceInfo.getServiceName():StringPool.BLANK;
	}
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "dossiermgtdossiermonitoringhistory.jsp");
	iteratorURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER);
%>

<liferay-portlet:renderURL var="searchURL">
	<liferay-portlet:param name="mvcPath" value="<%=templatePath + \"dossiermonitoringsearch.jsp\" %>"/>
	<liferay-portlet:param name="tabs1" value="<%=tabs1 %>"/>
</liferay-portlet:renderURL>

<aui:form action="#" name="fm">

<c:choose>
	<c:when test="<%=Validator.isNotNull(dossier) %>">
	<aui:row>
		<aui:col>
			<liferay-ui:message key="id-ho-so"></liferay-ui:message> : <%=dossier.getDossierId() %>
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:col>
			<liferay-ui:message key="chu-ho-so"></liferay-ui:message> : <%=HtmlUtil.escape(dossier.getSubjectName()) %>
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:col>
			<liferay-ui:message key="thu-tuc"></liferay-ui:message> : <%=HtmlUtil.escape(tenThuTuc) %>
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:col>
			<liferay-ui:message key="co-quan-thuc-hien"></liferay-ui:message> : <%=HtmlUtil.escape(dossier.getGovAgencyName()) %>
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:col>
			<liferay-ui:message key="qua-trinh-xu-ly-ho-so"></liferay-ui:message>
		</aui:col>
	</aui:row>
	
	<liferay-ui:search-container searchContainer="<%= new ActionHistorySearch(renderRequest, 1000, iteratorURL) %>">

		<liferay-ui:search-container-results>
			<%
				ActionHistorySearchTerms searchTerms = (ActionHistorySearchTerms)searchContainer.getSearchTerms();
				
				List<ActionHistory> listActionHistory = ActionHistoryLocalServiceUtil.findByF_ProcessOrderId(themeDisplay.getScopeGroupId(), 1);
				total = listActionHistory.size();
				results = listActionHistory;
				
				pageContext.setAttribute("results", results);
				pageContext.setAttribute("total", total);
			%>
		</liferay-ui:search-container-results>	
			<liferay-ui:search-container-row 
				className="org.opencps.processmgt.model.ActionHistory" 
				modelVar="actionHistory" 
				keyProperty="processOrderId"
				indexVar="index"
			>
				<%
					PortletURL detailURL = renderResponse.createRenderURL();
					detailURL.setParameter("mvcPath", templatePath + "dossiermgtdossiermonitoringhistory.jsp");
					detailURL.setParameter(DossierDisplayTerms.DOSSIER_ID, String.valueOf(dossier.getDossierId()));
					detailURL.setParameter(WebKeys.REDIRECT, currentURL);
					detailURL.setParameter("tabs1", tabs1);
					//id column
					row.addText(String.valueOf(searchContainer.getDelta()*(searchContainer.getCur()-1) +index + 1));
					row.addText(DateTimeUtil.convertDateToString(actionHistory.getActionDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT));
					row.addText(actionHistory.getStepName());
					row.addText(actionHistory.getActionName());
					row.addText(actionHistory.getActionNote());
				%>	
			</liferay-ui:search-container-row> 
		
		<liferay-ui:search-iterator/>
	</liferay-ui:search-container>				

	</c:when>
	<c:otherwise>
		<liferay-ui:message key="khong-tim-thay-ho-so"></liferay-ui:message>
	</c:otherwise>
</c:choose>

<aui:button-row>
	<aui:button type="button" href="<%=redirectURL.toString() %>" value="back"></aui:button>
</aui:button-row>
<style>
.info-td td{
	border: 1px solid gainsboro;
	padding: 5px;
}
</style>
</aui:form>
