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
 */
%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@ include file="../../init.jsp" %>

<%
	DictCollection dc = DictCollectionLocalServiceUtil.getDictCollection(scopeGroupId, ServiceUtil.SERVICE_DOMAIN);
	
	List<DictItem> ls = DictItemLocalServiceUtil.getDictItemsByDictCollectionId(dc.getDictCollectionId());
	
	HttpServletRequest httpReq = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(renderRequest));
%>


<liferay-portlet:renderURL varImpl="filter" portletName="<%= ServiceUtil.SERVICE_PUBLIC_PORTLET_NAME %>">
	
</liferay-portlet:renderURL>

<div class="service-menu">
	<ul>
		<%
			for (DictItem di : ls ) {
				filter.setParameter(ServiceDisplayTerms.SERVICE_DOMAINCODE,
					Long.toString(di.getDictItemId()));
				String css = "odd";
				if(ls.indexOf(di) % 2 == 0){
					css = "even";
				}
		%>
			<li class="<%=css%>">
				<i class="fa fa-chevron-circle-right" aria-hidden="true"></i>
				<a href="<%= filter.toString() %>">
					<%= di.getItemName(locale) %> 
					<span class="badge">
							<%= ServiceInfoLocalServiceUtil.countServiceInDomain(scopeGroupId,
							Long.toString(di.getDictItemId()), 1) %>
					</span>	
				</a>
			</li>
		<%
			}
		%>
	</ul>
</div>
