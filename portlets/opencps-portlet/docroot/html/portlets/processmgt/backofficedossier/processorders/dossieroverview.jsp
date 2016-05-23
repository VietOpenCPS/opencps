<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="java.text.Format"%>
<%@page import="com.liferay.portal.kernel.util.FastDateFormatFactoryUtil"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierException"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
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
<%@ include file="../../init.jsp"%>
<%
	Integer dossierId = ParamUtil.getInteger(request, "dossierId");
	
	Dossier dossier = null;
	
	try {
		dossier = DossierLocalServiceUtil.getDossier(dossierId);
	}
	catch (NoSuchDossierException ex) {
		
	}
	Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
%>
<c:choose>
	<c:when test="<%= (dossier != null) %>">
		<div class="overview">
			<table>
				<tr>
					<td class="col-left">
						<liferay-ui:message key="uuid"/>
					</td>
					<td class="col-right">
						<%= dossier.getUuid() %>
					</td>
				</tr>		
				<tr>
					<td class="col-left">
						<liferay-ui:message key="serviceinfo-name"/>
					</td>
					<td class="col-right">
						<%
							ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());
						%>
						<%= serviceInfo.getServiceName() %>
					</td>
				</tr>		
				<tr>
					<td class="col-left">
						<liferay-ui:message key="subjectname"/>
					</td>
					<td class="col-right">
						<%= dossier.getSubjectName() %>
					</td>
				</tr>		
				<tr>
					<td class="col-left">
						<liferay-ui:message key="address"/>
					</td>
					<td class="col-right">
						<%= dossier.getAddress() %>
					</td>
				</tr>		
				<tr>
					<td class="col-left">
						<liferay-ui:message key="contact-name"/>
					</td>
					<td class="col-right">
						<%= dossier.getContactName() %>
					</td>
				</tr>		
				<tr>
					<td class="col-left">
						<liferay-ui:message key="contact-telno"/>
					</td>
					<td class="col-right">
						<%= dossier.getContactTelNo() %>
					</td>
				</tr>		
				<tr>
					<td class="col-left">
						<liferay-ui:message key="contact-email"/>
					</td>
					<td class="col-right">
						<%= dossier.getContactEmail() %>
					</td>
				</tr>		
				<tr>
					<td class="col-left">
						<liferay-ui:message key="receive-datetime"/>
					</td>
					<td class="col-right">
						<%= dateFormatDate.format(dossier.getReceiveDatetime()) %>
					</td>
				</tr>		
				<tr>
					<td class="col-left">
						<liferay-ui:message key="reception-no"/>
					</td>
					<td class="col-right">
						<%= dossier.getReceptionNo() %>
					</td>
				</tr>	
				<tr>
					<td class="col-left">
						<liferay-ui:message key="estimate-datetime"/>
					</td>
					<td class="col-right">
						<%= dateFormatDate.format(dossier.getEstimateDatetime()) %>
					</td>
				</tr>	
				<tr>
					<td class="col-left">
						<liferay-ui:message key="finish-datetime"/>
					</td>
					<td class="col-right">
						<%= dateFormatDate.format(dossier.getFinishDatetime()) %>
					</td>
				</tr>	
				<tr>
					<td class="col-left">
						<liferay-ui:message key="dossier-status"/>
					</td>
					<td class="col-right">
						<% 
							DictItem item = DictItemLocalServiceUtil.getDictItem(dossier.getDossierStatus());
						%>
						<%= item.getItemName() %>
					</td>
				</tr>	
				<tr>
					<td class="col-left">
						<liferay-ui:message key="modified-datetime"/>
					</td>
					<td class="col-right">
						<%= dateFormatDate.format(dossier.getModifiedDate()) %>
					</td>
				</tr>	
				<tr>
					<td class="col-left">
						<liferay-ui:message key="note"/>
					</td>
					<td class="col-right">
						<%= dossier.getNote() %>
					</td>
				</tr>						
			</table>
		</div>	
	</c:when>
	<c:otherwise>
		<liferay-ui:message key="dossier-not-found"/>	
	</c:otherwise>
</c:choose>
