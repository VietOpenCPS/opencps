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
<%@page import="org.opencps.usermgt.search.WorkingUnitDisplayTerms"%>
<%@page import="com.liferay.portal.kernel.dao.orm.QueryUtil"%>
<%@page import="org.opencps.usermgt.service.WorkingUnitLocalServiceUtil"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.usermgt.model.WorkingUnit"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>
<%@ include file="../../init.jsp"%>

<%	
	WorkingUnit workingUnit = (WorkingUnit) request.getAttribute(WebKeys.WORKING_UNIT_ENTRY);

	WorkingUnit workingUnitChild = null;

	long workingUnitId = workingUnit != null ? workingUnit.getWorkingunitId() : 0L;
	
	boolean isEmployer = true;
	
	String isAddChild = ParamUtil.getString(request, "isAddChild");
	
	List<WorkingUnit> workingUnits = new ArrayList<WorkingUnit>();
	
	if (workingUnitId > 0) {
		isEmployer = workingUnit.getIsEmployer();
	}
	
	try {
		workingUnits = WorkingUnitLocalServiceUtil.getWorkingUnits(scopeGroupId);
	}catch(Exception e) {
		_log.error(e);
		
	}
%>

<c:choose>
    <c:when test="<%=Validator.isNotNull(isAddChild) %>">
        <aui:model-context bean="<%=workingUnitChild%>" model="<%=WorkingUnit.class%>" />
    </c:when>
    <c:otherwise>
        <aui:model-context bean="<%=workingUnit%>" model="<%=WorkingUnit.class%>" />
    </c:otherwise>
</c:choose>
<aui:row>
    <aui:select name="<%=WorkingUnitDisplayTerms.WORKINGUNIT_PARENTWORKINGUNITID%>">
		<c:choose>
		   <c:when test="<%=Validator.isNotNull(isAddChild) %>">
		       <aui:option value="<%=workingUnitId %>">
		       	<%= workingUnit.getName() %>
		       </aui:option>
		   </c:when>
		   <c:otherwise>
		        <aui:option value="<%=0%>"><liferay-ui:message key="root"/></aui:option>
		        
		        <%
		            for(WorkingUnit unit : workingUnits) {
		                %>
		                    <aui:option value="<%=unit.getWorkingunitId() %>" >
		                        <%=unit.getName() %>
		                    </aui:option>
		                <%
		            }
		        %>
		   </c:otherwise>
		</c:choose>
	</aui:select>
	
	<aui:input name="<%=WorkingUnitDisplayTerms.WORKINGUNIT_NAME%>">
		<aui:validator name="required" />
		<aui:validator name="maxLength">255</aui:validator>
	</aui:input>
	
	<aui:input name="<%=WorkingUnitDisplayTerms.WORKINGUNIT_ENNAME%>" >
		<aui:validator name="maxLength">255</aui:validator>
	</aui:input>
	
	<div id = '<portlet:namespace />showOrHidebyIsEmployeeRequest'>
		
		<c:choose>
			<c:when test="<%=Validator.isNotNull(isAddChild) %>">
				<aui:input 
					name="<%=WorkingUnitDisplayTerms.WORKINGUNIT_ISEMPLOYER%>" 
					type="checkbox"
				/>
			</c:when>
			<c:otherwise>
				<aui:input 
					name="<%=WorkingUnitDisplayTerms.WORKINGUNIT_ISEMPLOYER%>" 
					type="checkbox"
					checked="<%=isEmployer%>"
				/>
			</c:otherwise>
		</c:choose>
		<div id="<portlet:namespace/>workingUnitGovAgencyCodeContainer">
			<aui:input name="<%=WorkingUnitDisplayTerms.WORKINGUNIT_GOVAGENCYCODE%>"/>
		</div>
	</div>
	
</aui:row>

<aui:script> 
	
	AUI().ready(function(A){
	
		var isEmployerCheckBox = A.one('#<portlet:namespace/>isEmployerCheckbox');
		
		var parentWorkingUnitValue = A.one('#<portlet:namespace/>parentWorkingUnitId')
		
		var workingUnitGovAgencyCodeContainerGlobal = 
			A.one('#<portlet:namespace/>workingUnitGovAgencyCodeContainer');
		
		if(isEmployerCheckBox.val() == 'false') {
			workingUnitGovAgencyCodeContainerGlobal.hide();
			workingUnitGovAgencyCodeContainerGlobal.clearData();
		} else {
			workingUnitGovAgencyCodeContainerGlobal.show();
		}
		
		if(isEmployerCheckBox){
			
			var workingUnitGovAgencyCodeContainer = A.one('#<portlet:namespace/>workingUnitGovAgencyCodeContainer');

			isEmployerCheckBox.on('click', function(){
				var isEmployerInput = A.one('#<portlet:namespace/><%= WorkingUnitDisplayTerms.WORKINGUNIT_ISEMPLOYER%>');
	
				if(isEmployerInput.val() == 'false'){
					workingUnitGovAgencyCodeContainer.hide();
				}else{
					workingUnitGovAgencyCodeContainer.show();
				}
				
			});
		}
	});
	
</aui:script>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.usermgt.admin.general_workingunit.jsp");
%>