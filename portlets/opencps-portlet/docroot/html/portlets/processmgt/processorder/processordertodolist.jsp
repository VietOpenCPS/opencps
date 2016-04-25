<%@page import="org.opencps.processmgt.search.ProcessOrderDisplayTerms"%>
<%@page import="org.opencps.processmgt.search.ProcessOrderSearch"%>
<%@page import="org.opencps.processmgt.model.ProcessOrder"%>
<%@ include file="../init.jsp"%>

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
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 **/
%>

<liferay-util:include page="<%=templatePath + \"toptabs.jsp\" %>" servletContext="<%=application %>" />

<%

	List<ProcessStep> processSteps = ProcessOrderUtils.getProcessSteps(themeDisplay.getScopeGroupId(), user.getRoleIds());	

	String active = (String)request.getSession().getAttribute(WebKeys.MENU_ACTIVE);
	String processStepId = active;
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "processordertodolist.jsp");

	List<ProcessOrderUtils.CustomDisPlay> processOrders = new ArrayList<ProcessOrderUtils.CustomDisPlay>();
	int totalCount = 0;
	
%>
<aui:form name="fm" action="#" method="post">
<liferay-portlet:actionURL var="menuActionURL" name="menuAction" >
	<portlet:param name="mvcPath" value="<%=templatePath + \"processordermenu.jsp\" %>"/>
</liferay-portlet:actionURL>
<aui:row>
	<aui:col width="30">
		<aui:select name="<%=WebKeys.MENU_ACTIVE %>" label="">
			<aui:option value="" label="xem-toan-bo-ho-so"></aui:option>
			<%
				for(ProcessStep ett: processSteps){
			%>
			<aui:option selected="<%=Validator.isNotNull(active) && active.equals(String.valueOf(ett.getProcessStepId())) %>" value="<%=ett.getProcessStepId() %>" label="<%=ett.getStepName() %>"></aui:option>
			<%} %>
		</aui:select>
	</aui:col>
	<aui:col width="70">
		<input class="btn btn-primary" name="search" value="search" type="button" onclick="openCPS_submit('<%= menuActionURL.toString() %>')" />
	</aui:col>
</aui:row>

<liferay-ui:search-container searchContainer="<%= new ProcessOrderSearch(
	renderRequest ,SearchContainer.DEFAULT_DELTA, iteratorURL) %>">
	<liferay-ui:search-container-results>
		
		<%@ include file="/html/portlets/processmgt/processorder/processorder_search_results.jspf"%>

	</liferay-ui:search-container-results>
	
	<liferay-ui:search-container-row 
		className="org.opencps.processmgt.util.ProcessOrderUtils.CustomDisPlay" 
		modelVar="processOrder" 
		keyProperty="id"
	>
	<liferay-portlet:renderURL var="detailURL">
		<liferay-portlet:param name="mvcPath" value="<%=templatePath + \"processordertodo.jsp\" %>"/>
		<liferay-portlet:param name="<%=ProcessOrderDisplayTerms.PROCESSORDERID %>" value="<%=String.valueOf(processOrder.getId()) %>"/>
		<liferay-portlet:param name="<%=WebKeys.CURRENT_URL %>" value="<%=currentURL %>"/>
	</liferay-portlet:renderURL>
		<%
			String maTiepNhan = "<a href=\"#\" onclick=\"openCPS_dossierStatus('"+detailURL.toString()+"','"+processOrder.getTrangThaiHoSo()+"')\" >" + processOrder.getMaTiepNhan() + "</a>";
			String chuHoSo = "<a href=\"#\" onclick=\"openCPS_dossierStatus('"+detailURL.toString()+"','"+processOrder.getTrangThaiHoSo()+"')\" >" + processOrder.getChuHoSo() + "</a>";
			String thuTuc =  "<a href=\"#\" onclick=\"openCPS_dossierStatus('"+detailURL.toString()+"','"+processOrder.getTrangThaiHoSo()+"')\" >" + processOrder.getThuTuc() + "</a>";
			String buocXuLy = "<a href=\"#\" onclick=\"openCPS_dossierStatus('"+detailURL.toString()+"','"+processOrder.getTrangThaiHoSo()+"')\" >" + processOrder.getBuocXuLy() + "</a>";
			String nguoiPhuTrach =  "<a href=\"#\" onclick=\"openCPS_dossierStatus('"+detailURL.toString()+"','"+processOrder.getTrangThaiHoSo()+"')\" >" + processOrder.getNguoiPhuTrach() + "</a>";
			String hanXuLy = "<a href=\"#\" onclick=\"openCPS_dossierStatus('"+detailURL.toString()+"','"+processOrder.getTrangThaiHoSo()+"')\" >" + processOrder.getHanXuLy() + "</a>";
			row.addText(maTiepNhan);
			row.addText(chuHoSo);
			row.addText(thuTuc);
			row.addText(buocXuLy);
			row.addText(nguoiPhuTrach);
			row.addText(hanXuLy);
		%>
	</liferay-ui:search-container-row>
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>

</aui:form>
<script type="text/javascript">
function openCPS_submit(url) {
	var A = AUI();
	document.getElementById('<portlet:namespace />fm').action = url;
	document.getElementById('<portlet:namespace />fm').submit();
}
function openCPS_dossierStatus(url,status) {
	if(status == 'error'){
		return;
	}else{
		window.location = url;
	}
}
</script>