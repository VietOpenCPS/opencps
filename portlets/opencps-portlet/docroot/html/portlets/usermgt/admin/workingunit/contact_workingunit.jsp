
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
<%@page import="org.opencps.usermgt.model.WorkingUnit"%>
<%@page import="org.opencps.usermgt.search.WorkingUnitDisplayTerms"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="org.opencps.util.WebKeys"%>
<%
	WorkingUnit workingUnit =
		(WorkingUnit) request.getAttribute(WebKeys.WORKING_UNIT_ENTRY);
    WorkingUnit workingUnitChild = null;
	long workingUnitId =
		workingUnit != null ? workingUnit.getWorkingunitId() : 0L;
	String isAddChild = ParamUtil.getString(request, "isAddChild");
	String dictItemCityId = "0";
	String dictItemDistrictId = "0";
	String dictItemWardId = "0";
	List<String> regions = new ArrayList<String>();
	try {
		if (workingUnit != null) {
			dictItemCityId = workingUnit.getCityCode();
			dictItemDistrictId = workingUnit.getDistrictCode();
			dictItemWardId = workingUnit.getWardCode();
		}

	}
	catch (Exception e) {

	}
	regions.add(dictItemCityId);
	regions.add(dictItemDistrictId);
	regions.add(dictItemWardId);
	String region = StringUtil.merge(regions);
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
	<aui:input name="<%=WorkingUnitDisplayTerms.WORKINGUNIT_ADDRESS%>">
		<aui:validator name="required" />
		<aui:validator name="maxLength">500</aui:validator>
	</aui:input>

	<datamgt:ddr cssClass="input100" depthLevel="3"
		dictCollectionCode="<%=PortletPropsValues.DATAMGT_MASTERDATA_ADMINISTRATIVE_REGION%>"
		itemNames="cityCode,districtCode,wardCode"
		itemsEmptyOption="true,true,true" 
		selectedItems="<%= region %>">

	</datamgt:ddr>

	<aui:row>
		<aui:col width="50">
			<aui:input name="<%=WorkingUnitDisplayTerms.WORKINGUNIT_TELNO%>">
				<aui:validator name="minLength">10</aui:validator>
				<aui:validator name="number"></aui:validator>
			</aui:input>
		</aui:col>
		<aui:col width="50">
			<aui:input name="<%=WorkingUnitDisplayTerms.WORKINGUNIT_FAXNO%>">
				<aui:validator name="minLength">5</aui:validator>
				<aui:validator name="number"></aui:validator>
			</aui:input>
		</aui:col>
	</aui:row>

	<aui:row>
		<aui:col width="50">
			<aui:input name="<%=WorkingUnitDisplayTerms.WORKINGUNIT_EMAIL%>">
				<aui:validator name="email" />
				<aui:validator name="required" />
				<aui:validator name="maxLength">255</aui:validator>
			</aui:input>
		</aui:col>
		<aui:col width="50">
			<aui:input name="<%=WorkingUnitDisplayTerms.WORKINGUNIT_WEBSITE%>">
				<aui:validator name="url" />
				<aui:validator name="maxLength">1000</aui:validator>
			</aui:input>
		</aui:col>
	</aui:row>

</aui:row>

<%!Log _log =
		LogFactoryUtil.getLog("html.portlets.usermgt.admin.workingunit.contact_workingunit.jsp");%>