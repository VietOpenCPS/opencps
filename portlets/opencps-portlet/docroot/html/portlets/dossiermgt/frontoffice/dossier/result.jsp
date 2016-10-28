
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
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearchUtil"%>
<%@page import="com.liferay.portal.kernel.dao.orm.QueryUtil"%>
<%@page import="com.liferay.portlet.documentlibrary.util.DLUtil"%>
<%@page import="org.opencps.util.DLFileEntryUtil"%>
<%@page import="com.liferay.portal.kernel.repository.model.FileEntry"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierPartLocalServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.dossiermgt.service.DossierLogLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierLog"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.util.DictItemUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="com.liferay.portal.kernel.util.OrderByComparator"%>

<%@ include file="../../init.jsp"%>

<%
	Dossier dossier = (Dossier) request.getAttribute(WebKeys.DOSSIER_ENTRY);
	DossierTemplate dossierTemplate = (DossierTemplate) request.getAttribute(WebKeys.DOSSIER_TEMPLATE_ENTRY);
%>

<c:choose>
	<c:when test="<%=dossier != null && dossier.getDossierStatus() != PortletConstants.DOSSIER_STATUS_NEW %>">
		<%
			String[] actors = new String[]{};
			String[] requestCommands = new String[]{StringPool.APOSTROPHE + WebKeys.DOSSIER_LOG_RESUBMIT_REQUEST + StringPool.APOSTROPHE, 
													StringPool.APOSTROPHE + WebKeys.DOSSIER_LOG_PAYMENT_REQUEST + StringPool.APOSTROPHE};
			List<DossierLog> dossierLogs = DossierLogLocalServiceUtil.findRequiredProcessDossier(dossier.getDossierId(), actors, requestCommands);
			List<DossierPart> dossierPartsLevel1 = new ArrayList<DossierPart>();
			
			ServiceInfo info = null;
			String serviceInfoName = StringPool.BLANK;
			
			try {
				
				info = ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());
				serviceInfoName = info.getServiceName();
				
			} catch (Exception e) {}
				
			if(dossierTemplate != null){
				
				try{
					List<DossierPart> lstTmp1 = DossierPartLocalServiceUtil.getDossierPartsByT_P_PT(dossierTemplate.getDossierTemplateId(), 0, PortletConstants.DOSSIER_PART_TYPE_RESULT);
					if(lstTmp1 != null){
						dossierPartsLevel1.addAll(lstTmp1);
					}
				}catch(Exception e){}
				
				try{
					List<DossierPart> lstTmp2 = DossierPartLocalServiceUtil.getDossierPartsByT_P_PT(dossierTemplate.getDossierTemplateId(), 0, PortletConstants.DOSSIER_PART_TYPE_MULTIPLE_RESULT);
					if(lstTmp2 != null){
						dossierPartsLevel1.addAll(lstTmp2);
					}
				}catch(Exception e){}
				
				
			}
			
			request.setAttribute("dossierPartsLevel1", dossierPartsLevel1);
			request.setAttribute("dossier", dossier);
		
		%>
		
		<aui:row cssClass="pd_b20">
			<aui:col width="20" cssClass="bold">
				<liferay-ui:message key="dossier-service-name"/>
			</aui:col>
			<aui:col width="80">
				<%=serviceInfoName %>
			</aui:col>
		</aui:row>
		
		<aui:row>
			<aui:col width="50">
				<aui:row>
					<aui:col width="30" cssClass="bold">
						<liferay-ui:message key="dossier-create-date"/>
					</aui:col>
					<aui:col width="70">
						<%=
							Validator.isNotNull(dossier.getCreateDate()) ?
							DateTimeUtil.convertDateToString(dossier.getCreateDate(), DateTimeUtil._VN_DATE_TIME_FORMAT) : 
							DateTimeUtil._EMPTY_DATE_TIME 
						%>
					</aui:col>
				</aui:row>
				
				<aui:row>
					<aui:col width="30" cssClass="bold">
						<liferay-ui:message key="dossier-receive-date"/>
					</aui:col>
					<aui:col width="70">
						<%=
							Validator.isNotNull(dossier.getReceiveDatetime()) ?
							DateTimeUtil.convertDateToString(dossier.getReceiveDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) : 
							DateTimeUtil._EMPTY_DATE_TIME 
						%>
					</aui:col>
				</aui:row>
				
				<aui:row>
					<aui:col width="30" cssClass="bold">
						<liferay-ui:message key="dossier-estimate-date"/>
					</aui:col>
					<aui:col width="70">
						<%=
							Validator.isNotNull(dossier.getEstimateDatetime()) ? 
							DateTimeUtil.convertDateToString(dossier.getEstimateDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) : 
							DateTimeUtil._EMPTY_DATE_TIME
						%>
					</aui:col>
				</aui:row>
				
				<aui:row>
					<aui:col width="30" cssClass="bold">
						<liferay-ui:message key="dossier-update-date"/>
					</aui:col>
					<aui:col width="70">
						<%=
							Validator.isNotNull(dossier.getModifiedDate()) ? 
							DateTimeUtil.convertDateToString(dossier.getModifiedDate(), DateTimeUtil._VN_DATE_TIME_FORMAT) : 
							DateTimeUtil._EMPTY_DATE_TIME
						%>
					</aui:col>
				</aui:row>
				
			</aui:col>
			
			<aui:col width="50">
				<aui:row>
					<aui:col width="30" cssClass="bold">
						<liferay-ui:message key="dossier-submit-date"/>
					</aui:col>
					<aui:col width="70">
						<%=
							Validator.isNotNull(dossier.getSubmitDatetime()) ? 
							DateTimeUtil.convertDateToString(dossier.getSubmitDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) : 
							DateTimeUtil._EMPTY_DATE_TIME
						%>
					</aui:col>
				</aui:row>
				
				<aui:row>
					<aui:col width="30" cssClass="bold">
						<liferay-ui:message key="dossier-reception-no"/>
					</aui:col>
					
					<aui:col width="70">
						<%=Validator.isNotNull(dossier.getReceptionNo()) ? dossier.getReceptionNo() : StringPool.DASH %>
					</aui:col>
				</aui:row>
				
				<aui:row>
					<aui:col width="30" cssClass="bold">
						<liferay-ui:message key="dossier-finish-date"/>
					</aui:col>
					<aui:col width="70">
						<%=
							Validator.isNotNull(dossier.getFinishDatetime()) ? 
							DateTimeUtil.convertDateToString(dossier.getFinishDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) : 
							DateTimeUtil._EMPTY_DATE_TIME
						%>
					</aui:col>
				</aui:row>
				
				<aui:row>
					<aui:col width="30" cssClass="bold">
						<liferay-ui:message key="log-status"/>
					</aui:col>
					<aui:col width="70">
						<%=PortletUtil.getDossierStatusLabel(dossier.getDossierStatus(), locale) %>
					</aui:col>
				</aui:row>
			</aui:col>
				
		</aui:row>
		
		<c:if test="<%=dossierLogs != null && !dossierLogs.isEmpty() %>">
		
			<aui:row cssClass="bottom-line mg-l-30 pd_b20 pd_t20 pd-r60"></aui:row>
		
			<aui:row cssClass="pd_t20">
				<label class="bold uppercase">
					<liferay-ui:message key="required-process"/>
				</label>
			
					<%
						int count = 1;
						for(DossierLog dossierLog : dossierLogs){
							
							%>
								<aui:row cssClass='<%=count <  dossierLogs.size() ? "bottom-line pd_b20 pd_t20" : "pd_t20" %>'>
									<aui:col width="30">
										<span class="span1">
											<i class="fa fa-circle blue sx10"></i>
										</span>
										
										<span class="span2 bold">
											<%=count %>
										</span>
										
										<span class="span9">
											<%=
												Validator.isNotNull(dossierLog.getUpdateDatetime()) ? 
												DateTimeUtil.convertDateToString(dossierLog.getUpdateDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) : 
												DateTimeUtil._EMPTY_DATE_TIME
											%>
										</span>
									</aui:col>
									<aui:col width="30">
										<span class="span4 bold">
											<liferay-ui:message key="request-command"/>
										</span>
										<span class="span8">
											<liferay-ui:message key="<%=dossierLog.getRequestCommand() %>"/>
										</span>
									</aui:col>
									<aui:col width="30">
										<span class="span4 bold">
											<liferay-ui:message key="message-info"/>
										</span>
										<span class="span8">
											<%= DossierMgtUtil.getDossierLogs(PortletConstants.REQUEST_COMMAND_PAYMENT, dossierLog.getMessageInfo()) %>
										</span>
									</aui:col>
								</aui:row>
								
							<%
							count ++;
						}
					%>
				
			</aui:row>
		</c:if>
		
		<!-- cau hinh hien thi sap xep giay to ket qua -->
		<c:choose>
			<c:when test="<%=Validator.isNotNull(orderFieldDossierFile) %>">
			<%-- 	<%@ include file="/html/portlets/dossiermgt/frontoffice/dossier/result_display/result_order.jsp" %>
			 --%>
			 
			<liferay-util:include 
				page="/html/portlets/dossiermgt/frontoffice/dossier/result_display/result_order.jsp" 
				servletContext="<%=application %>" 
			/>
			</c:when>
			
			<c:otherwise>
				<%-- <%@ include file="/html/portlets/dossiermgt/frontoffice/dossier/result_display/result_default.jsp" %> --%>
				<liferay-util:include 
					page="/html/portlets/dossiermgt/frontoffice/dossier/result_display/result_default.jsp" 
					servletContext="<%=application %>" 
			/>
			</c:otherwise>
		</c:choose>

	</c:when>
	
	<c:otherwise>
		<div class="portlet-msg-info">
			<liferay-ui:message key="no-dossier-result-info"/>
		</div>
	</c:otherwise>
	
</c:choose>