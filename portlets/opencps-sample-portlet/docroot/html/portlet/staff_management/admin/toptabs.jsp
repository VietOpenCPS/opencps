
<%@page import="org.opencps.sample.staff.permissions.StaffPermission"%>
<%@page import="org.opencps.sample.department.permissions.DepartmentPermission"%>
<%@page import="com.liferay.portal.service.permission.PortletPermissionUtil"%>
<%@page import="org.opencps.sample.utils.PortletConstants"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.kernel.util.StringUtil"%>
<%@ include file="../../init.jsp" %>

<%

	String[] names = new String[]{PortletConstants.TopTab.DEPARTMENT.toString(), PortletConstants.TopTab.STAFF.toString()};

	String value = ParamUtil.getString(request, "tabs1", PortletConstants.TopTab.DEPARTMENT.toString());

	List<String> urls = new ArrayList<String>();
	
	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), DepartmentPermission.VIEW) && 
			DepartmentPermission.contains(permissionChecker, scopeGroupId, DepartmentPermission.VIEW)) {
		PortletURL viewDepartmentURL = renderResponse.createRenderURL();
		viewDepartmentURL.setParameter("mvcPath", templatePath + "department.jsp");
		viewDepartmentURL.setParameter("tabs1", PortletConstants.TopTab.DEPARTMENT.toString());
		urls.add(viewDepartmentURL.toString());
	}
	
	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), StaffPermission.VIEW) && 
			StaffPermission.contains(permissionChecker, scopeGroupId, StaffPermission.VIEW)) {
		PortletURL viewStaffURL = renderResponse.createRenderURL();
		viewStaffURL.setParameter("mvcPath", templatePath + "staff.jsp");
		viewStaffURL.setParameter("tabs1", PortletConstants.TopTab.STAFF.toString());
		urls.add(viewStaffURL.toString());
	}
%>
<liferay-ui:tabs
	names="<%= StringUtil.merge(names) %>"
	param="tabs1"
	url0="<%=urls != null && urls.size() > 0 ? urls.get(0): StringPool.BLANK %>"
	url1="<%=urls != null && urls.size() > 1 ? urls.get(1): StringPool.BLANK %>"
/>
