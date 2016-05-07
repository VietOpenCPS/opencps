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
	String receptionNo = ParamUtil.getString(request, DossierDisplayTerms.RECEPTION_NO);
	Dossier dossier = new DossierImpl();
	if(Validator.isNotNull(receptionNo)){
		dossier = DossierLocalServiceUtil.fetchByF_ReceptionNo(themeDisplay.getScopeGroupId(), receptionNo);
	}
%>

<liferay-portlet:renderURL var="searchURL">
	<liferay-portlet:param name="mvcPath" value="<%=templatePath + \"dossiermonitoringsearch.jsp\" %>"/>
	<liferay-portlet:param name="tabs1" value="<%=tabs1 %>"/>
</liferay-portlet:renderURL>

<aui:form action="#" name="fm">

<!-- search control tool -->

<aui:row>
	<aui:col>
		<aui:input name="<%=DossierDisplayTerms.RECEPTION_NO %>" inlineField="true"></aui:input>
		<aui:button type="button" value="search" onClick="<%= renderResponse.getNamespace() + \"fitterALL()\" %>"></aui:button>
	</aui:col>
</aui:row>

<c:choose>
	<c:when test="<%=Validator.isNotNull(dossier) %>">
		<table style="width: 100%;" class="info-td">
			<tr style="background-color: rgb(245, 245, 245); font-weight: bold;">
				<td style="width: 30%;"><liferay-ui:message key="ho-so-so" /></th>
				<td><%=(Validator.isNotNull(dossier)?String.valueOf(dossier.getDossierId()):StringPool.BLANK) %></th>
			</tr>
			<tr >
				<td style="width: 30%;"><liferay-ui:message key="thu-tuc" /></td>
				<td>
				<%
					String tenThuTuc = StringPool.BLANK;
					if(Validator.isNotNull(dossier) && dossier.getServiceInfoId() > 0){
						ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil.fetchServiceInfo(dossier.getServiceInfoId());
						tenThuTuc = Validator.isNotNull(serviceInfo)?serviceInfo.getServiceName():StringPool.BLANK;
					}
				%>
				<%=HtmlUtil.escape(tenThuTuc) %>
				</td>
			</tr>
			<tr>
				<td style="width: 30%;"><liferay-ui:message key="co-quan-thuc-hien" /></td>
				<td><%=(Validator.isNotNull(dossier)?HtmlUtil.escape(dossier.getGovAgencyName()):StringPool.BLANK) %></td>
			</tr>
			<tr>
				<td style="width: 30%;"><liferay-ui:message key="chu-ho-so" /></td>
				<td><%=(Validator.isNotNull(dossier)?HtmlUtil.escape(dossier.getSubjectName()):StringPool.BLANK) %></td>
			</tr>
			<tr>
				<td><liferay-ui:message key="dia-chi" /></td>
				<td><%=(Validator.isNotNull(dossier)?HtmlUtil.escape(dossier.getAddress()):StringPool.BLANK) %></td>
			</tr>
			<tr>
				<td style="width: 30%;"><liferay-ui:message key="ngay-tiep-nhan" /></td>
				<td><%=(Validator.isNotNull(dossier) && Validator.isNotNull(dossier.getReceiveDatetime())?DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT).format(dossier.getReceiveDatetime()):StringPool.BLANK) %></td>
			</tr>
			<tr>
				<td style="width: 30%;"><liferay-ui:message key="ngay-hen-tra" /></td>
				<td><%=(Validator.isNotNull(dossier) && Validator.isNotNull(dossier.getEstimateDatetime())?DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT).format(dossier.getEstimateDatetime()):StringPool.BLANK) %></td>
			</tr>
			<tr>
				<td style="width: 30%;"><liferay-ui:message key="ngay-hoan-thanh" /></td>
				<td><%=(Validator.isNotNull(dossier) && Validator.isNotNull(dossier.getFinishDatetime())?DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT).format(dossier.getFinishDatetime()):StringPool.BLANK) %></td>
			</tr>
			<tr>
				<td style="width: 30%;"><liferay-ui:message key="trang-thai-ho-so" /></td>
				<td><%=(Validator.isNotNull(dossier)?PortletUtil.getDossierStatusLabel(dossier.getDossierStatus(), locale):StringPool.BLANK) %></td>
			</tr>
			<tr>
				<td style="width: 30%;"><liferay-ui:message key="ngay-cap-nhat" /></td>
				<td><%=(Validator.isNotNull(dossier) && Validator.isNotNull(dossier.getSubmitDatetime())?DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT).format(dossier.getSubmitDatetime()):StringPool.BLANK) %></td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<liferay-ui:message key="khong-tim-thay-ho-so"></liferay-ui:message>
	</c:otherwise>
</c:choose>


<style>
.info-td td{
	border: 1px solid gainsboro;
	padding: 5px;
}
</style>
</aui:form>

<aui:script>
function <portlet:namespace />fitterALL() {
    var A = AUI();
	var url = '<%=searchURL.toString() %>';
	
	if(A.one('#<portlet:namespace /><%=DossierDisplayTerms.RECEPTION_NO %>')) {
		url += '&<portlet:namespace /><%=DossierDisplayTerms.RECEPTION_NO %>=' + A.one('#<portlet:namespace /><%=DossierDisplayTerms.RECEPTION_NO %>').get('value');
	}
	location.href = url;
}
</aui:script>
