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
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.accountmgt.search.CitizenDisplayTerms"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="org.opencps.accountmgt.service.CitizenLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil"%>
<%@page import="com.liferay.portlet.documentlibrary.model.DLFileEntry"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>

<%@ include file="../../init.jsp" %>

<%
	
	if(request.getAttribute(WebKeys.CITIZEN_ENTRY) != null){
		citizen = (Citizen) request.getAttribute(WebKeys.CITIZEN_ENTRY);	
	}

	DictCollection dictCollection = null;
	
	long citizenId = citizen != null ? citizen.getCitizenId() : 0L;
	
	boolean isViewProfile = GetterUtil.get( (Boolean) request.getAttribute(WebKeys.ACCOUNTMGT_VIEW_PROFILE), false);
	
	boolean isAdminViewProfile = GetterUtil.get((Boolean) request.getAttribute(WebKeys.ACCOUNTMGT_ADMIN_PROFILE), false);
	
	Citizen citizenFromProfile = null;
	
	Date defaultBirthDate = citizen != null && citizen.getBirthdate() != null ? 
		citizen.getBirthdate() : DateTimeUtil.convertStringToDate("01/01/1970");
		
	PortletUtil.SplitDate spd = new PortletUtil.SplitDate(defaultBirthDate);
	
%>

<aui:model-context bean="<%=citizen %>" model="<%=Citizen.class%>" />

<c:if test="<%=isAdminViewProfile && citizenId > 0%>">
	<aui:row>
		<aui:col width="50">
			<aui:input 
				type="text"
				name="<%=CitizenDisplayTerms.CITIZEN_CREATEDDATE %>" 
				value="<%=DateTimeUtil.convertDateToString(citizen.getCreateDate(), DateTimeUtil._VN_DATE_FORMAT) %>"
				disabled="<%=isAdminViewProfile %>"
			/>
		</aui:col>
		<aui:col width="50">
			<aui:input name="<%=CitizenDisplayTerms.CITIZEN_ACCOUNTSTATUS%>" disabled="<%=isAdminViewProfile %>" />
		</aui:col>
		
	</aui:row>
</c:if>

<c:choose>
	<c:when test="<%=isAdminViewProfile && citizenId > 0%>">
		<aui:row>
			<aui:col width="50">
				<aui:input 
					name="<%=CitizenDisplayTerms.CITIZEN_FULLNAME %>" 
					disabled="<%=isViewProfile %>" 
					cssClass="input100"
				>
				<aui:validator name="required" />
				<aui:validator name="maxLength">255</aui:validator>
				</aui:input>
			</aui:col>
			
			<aui:col width="50">
				<aui:input 
					name="<%=CitizenDisplayTerms.CITIZEN_PERSONALID %>"
					disabled="<%=isViewProfile %>"
					cssClass="input100"
				>
				<aui:validator name="required" />
				</aui:input>
			</aui:col>
		</aui:row>
		
		<aui:row>
			<aui:col width="50">
				<label class="control-label custom-lebel" for='<portlet:namespace/><%=CitizenDisplayTerms.CITIZEN_BIRTHDATE %>'>
					<liferay-ui:message key="birth-date"/>
				</label>
				<liferay-ui:input-date 
					dayParam="<%=CitizenDisplayTerms.BIRTH_DATE_DAY %>"
					dayValue="<%= spd.getDayOfMoth() %>"
					disabled="<%=isViewProfile%>"
					monthParam="<%=CitizenDisplayTerms.BIRTH_DATE_MONTH %>"
					monthValue="<%= spd.getMonth() %>"
					name="<%=CitizenDisplayTerms.CITIZEN_BIRTHDATE %>"
					yearParam="<%=CitizenDisplayTerms.BIRTH_DATE_YEAR %>"
					yearValue="<%= spd.getYear() %>"
					formName="fm"
					autoFocus="<%=true %>"
					cssClass="input100"
				>
				</liferay-ui:input-date>
			</aui:col>
			
			<aui:col width="50">
				<aui:select 
					name="<%=CitizenDisplayTerms.CITIZEN_GENDER %>"
					disabled="<%=isViewProfile %>"
					cssClass="input100"
					required="true"
				>
					<aui:option label="<%=StringPool.BLANK %>" value="" />
					<%
						if(PortletPropsValues.USERMGT_GENDER_VALUES != null && 
							PortletPropsValues.USERMGT_GENDER_VALUES.length > 0){
							for(int g = 0; g < PortletPropsValues.USERMGT_GENDER_VALUES.length; g++){
								%>
									<aui:option 
										value="<%= PortletPropsValues.USERMGT_GENDER_VALUES[g]%>"
										selected="<%=citizen != null && citizen.getGender() == PortletPropsValues.USERMGT_GENDER_VALUES[g]%>"
									>
										<%= PortletUtil.getGender(PortletPropsValues.USERMGT_GENDER_VALUES[g], locale)%>
									</aui:option>
								<%
							}
						}
					%>
				</aui:select>
			</aui:col>
		</aui:row>
	</c:when>
	<c:otherwise>
		<aui:row>
			<aui:col width="25">
				<aui:input 
					name="<%=CitizenDisplayTerms.CITIZEN_FULLNAME %>" 
					disabled="<%=isViewProfile %>" 
					cssClass="input100"
				>
				<aui:validator name="required" />
				<aui:validator name="maxLength">255</aui:validator>
				</aui:input>
			</aui:col>
			
			<aui:col width="25">
				<aui:input 
					name="<%=CitizenDisplayTerms.CITIZEN_PERSONALID %>"
					disabled="<%=isViewProfile %>"
					cssClass="input100"
				>
				<aui:validator name="required" />
				</aui:input>
			</aui:col>
			
			<aui:col width="25">
				<label class="control-label custom-lebel" for='<portlet:namespace/><%=CitizenDisplayTerms.CITIZEN_BIRTHDATE %>'>
					<liferay-ui:message key="birth-date"/>
				</label>
				<liferay-ui:input-date 
					dayParam="<%=CitizenDisplayTerms.BIRTH_DATE_DAY %>"
					dayValue="<%= spd.getDayOfMoth() %>"
					disabled="<%=isViewProfile%>"
					monthParam="<%=CitizenDisplayTerms.BIRTH_DATE_MONTH %>"
					monthValue="<%= spd.getMonth() %>"
					name="<%=CitizenDisplayTerms.CITIZEN_BIRTHDATE %>"
					yearParam="<%=CitizenDisplayTerms.BIRTH_DATE_YEAR %>"
					yearValue="<%= spd.getYear() %>"
					formName="fm"
					autoFocus="<%=true %>"
					cssClass="input100"
				>
				</liferay-ui:input-date>
			</aui:col>
			
			<aui:col width="25">
				<aui:select 
					name="<%=CitizenDisplayTerms.CITIZEN_GENDER %>"
					disabled="<%=isViewProfile %>"
					cssClass="input100"
					required="true"
				>
					<aui:option label="<%=StringPool.BLANK %>" value="" />
					<%
						if(PortletPropsValues.USERMGT_GENDER_VALUES != null && 
							PortletPropsValues.USERMGT_GENDER_VALUES.length > 0){
							for(int g = 0; g < PortletPropsValues.USERMGT_GENDER_VALUES.length; g++){
								%>
									<aui:option 
										value="<%= PortletPropsValues.USERMGT_GENDER_VALUES[g]%>"
										selected="<%=citizen != null && citizen.getGender() == PortletPropsValues.USERMGT_GENDER_VALUES[g]%>"
									>
										<%= PortletUtil.getGender(PortletPropsValues.USERMGT_GENDER_VALUES[g], locale)%>
									</aui:option>
								<%
							}
						}
					%>
				</aui:select>
			</aui:col>
		</aui:row>
	</c:otherwise>
</c:choose>


<%!
	private Log _log = LogFactoryUtil.getLog(".html.portlets.accountmgt.registration/citizen.general_info.jsp");
%>
