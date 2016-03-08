<%@page import="org.opencps.sample.department.search.DepartmentDisplayTerms"%>
<%@page import="org.opencps.sample.utils.OpenCPSWebkeys"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="org.opencps.sample.department.model.Department"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@ include file="../../init.jsp" %>

<%
	Department department = (Department) request.getAttribute(OpenCPSWebkeys.DEPARTMENT);
	String backURL = ParamUtil.getString(request, "backURL", currentURL);
%>

<portlet:actionURL var="editDepartmentURL" name="editDepartment">
	<portlet:param name="<%=Constants.CMD %>" value="<%=department != null ? Constants.UPDATE : Constants.ADD%>"/>
</portlet:actionURL>

<liferay-ui:header
	backURL="<%= backURL %>"
	title='<%= (department == null) ? "add-department" : "update-department" %>'
/>

<aui:form name="fm" method="post" action="<%=editDepartmentURL %>">
	<aui:model-context bean="<%= department %>" model="<%= Department.class %>" />
	<aui:input name="<%=DepartmentDisplayTerms.DEPARTMENT_ID %>" value="<%=department != null ? department.getDepartmentId() : 0 %>" type="hidden"/>
	<aui:input name="redirectURL" value="<%=currentURL %>" type="hidden"/>
	<aui:row>
		<aui:select name="<%=DepartmentDisplayTerms.PARENT_ID %>" label="parent" bean="">
			
		</aui:select>
	</aui:row>
	
	<aui:row>
		<aui:input name="<%=DepartmentDisplayTerms.NAME %>" type="text" inlineField="true" label="name">
			<aui:validator name="required"/>
			<aui:validator name="maxLength">255</aui:validator>
		</aui:input>
	</aui:row>
	
	<aui:row>
		<aui:input name="<%=DepartmentDisplayTerms.DESCRIPTION %>" type="textarea" inlineField="true" label="description">
			<aui:validator name="maxLength">500</aui:validator>
		</aui:input>
	</aui:row>
	
	<aui:row>
		<aui:button type="submit" name="save" value="save"/>
	</aui:row>
</aui:form>