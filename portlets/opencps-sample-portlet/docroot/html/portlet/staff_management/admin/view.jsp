<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.sample.department.search.DepartmentSearch"%>
<%@ include file="../../init.jsp" %>

<liferay-util:include page="/html/portlet/staff_management/admin/toolbar.jsp" 
					servletContext="<%=application %>" />
<liferay-util:include page="/html/portlet/staff_management/admin/toptabs.jsp" 
					servletContext="<%=application %>" />

<%
	PortletURL portletURL = renderResponse.createRenderURL();
	
	portletURL.setParameter("mvcPath", "/html/portlet/staff_management/admin/view.jsp");
	
	String portletURLString = portletURL.toString();
	
	/* DepartmentSearch searchContainer = new DepartmentSearch(renderRequest, portletURL);
	
	List<String> headerNames = searchContainer.getHeaderNames();

	headerNames.add(StringPool.BLANK); */
%>

<liferay-ui:search-container
	delta="20"
	searchContainer="<%=new DepartmentSearch(renderRequest, portletURL) %>"  
	iteratorURL="<%=portletURL %>"
	headerNames="<%=searchContainer.getHeaderNames() %>"
	orderByComparator="<%=searchContainer.getOrderByComparator() %>"
	emptyResultsMessage="no-found-result"
>	

	<%
		
	%>
	<liferay-ui:search-container-results
		results="<%= null %>" total="0"
	/>
	<liferay-ui:search-container-row
		className="org.opencps.sample.department.model.Department"
		escapedModel="<%= true %>"
		keyProperty="departmentId"
		modelVar="department"
	>
	
	</liferay-ui:search-container-row>
</liferay-ui:search-container>