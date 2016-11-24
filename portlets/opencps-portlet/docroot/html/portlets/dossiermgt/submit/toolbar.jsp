<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.processmgt.util.ProcessOrderUtils"%>
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
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.servicemgt.search.ServiceDisplayTerms"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>

<%@ include file="/init.jsp"%>
<%
	PortletURL searchURL = renderResponse.createRenderURL();

	searchURL.setParameter("mvcPath", templatePath + "submitinstruction.jsp");
	
	String administrationCode = ParamUtil.getString(request, ServiceDisplayTerms.SERVICE_ADMINISTRATION);
	
	String domainCode = ParamUtil.getString(request, ServiceDisplayTerms.SERVICE_DOMAINCODE);
	
	request.setAttribute(ServiceDisplayTerms.SERVICE_ADMINISTRATION, administrationCode);
	
	request.setAttribute(ServiceDisplayTerms.SERVICE_DOMAINCODE, domainCode);
	
	DictCollection collectionDomain = null;
	DictItem curDictItem = null;
	try {
		collectionDomain = DictCollectionLocalServiceUtil.getDictCollection(scopeGroupId, WebKeys.SERVICE_DOMAIN);
	} catch (Exception e) {
		
	}
	
	List<DictItem> dictItems = new ArrayList<DictItem>();
	
	if(Validator.isNotNull(collectionDomain)) {
		dictItems = DictItemLocalServiceUtil.getDictItemsByDictCollectionId(collectionDomain.getDictCollectionId());
	}

	String myComboTree = ProcessOrderUtils.generateComboboxTree(PortletPropsValues.DATAMGT_MASTERDATA_SERVICE_DOMAIN, PortletConstants.TREE_VIEW_ALL_ITEM, 
			PortletConstants.TREE_VIEW_LEVER_2, false, renderRequest);
	
%>


<aui:script use="aui-base,aui-io">
$(document).ready(function(){
	var myComboTree = '<%=myComboTree %>';
	var domainCode = '<%=domainCode%>';
	$('#comboboxTree').combotree({  
	
	    animate:true,
	    data: JSON.parse(myComboTree),
	    onChange:function(){
            $('#<portlet:namespace /><%=ServiceDisplayTerms.SERVICE_DOMAINCODE %>').val($('#comboboxTree').combotree('getValue'));
        },
        onClick:function(){
        	<portlet:namespace />onSelectSubmit();
        },
	
	});

	$('#comboboxTree').combotree('setValue', domainCode);
	
	$("#<portlet:namespace />administrationCode").change(function() {
		<portlet:namespace />onSelectSubmit();
	});
	Liferay.provide(window, '<portlet:namespace/>onSelectSubmit', function() {
		var A = AUI();
		
		submitForm(document.<portlet:namespace />fm);
	});
});

</aui:script>
<aui:nav-bar cssClass="opencps-toolbar custom-toolbar">
	<aui:nav-bar-search cssClass="pull-right">
		<div class="form-search">
			<aui:form action="<%= searchURL %>" method="post" name="fm">
				<div class="toolbar_search_input">
					<aui:row>
						<aui:col width="30" cssClass="search-col">
							<datamgt:ddr
								depthLevel="1" 
								dictCollectionCode="GOVERNMENT_AGENCY"
								itemNames="administrationCode"
								itemsEmptyOption="true"
								cssClass="search-input select-box"
								emptyOptionLabels="gov-code"
								showLabel="false"
								selectedItems="<%= administrationCode %>"
							>
							</datamgt:ddr>

						</aui:col>
						<aui:col width="30" cssClass="search-col">

							<aui:input name="<%=ServiceDisplayTerms.SERVICE_DOMAINCODE %>" type="hidden"></aui:input>
							<select id="comboboxTree" class="easyui-combotree"></select>

						</aui:col>
						<aui:col width="30" cssClass="search-col">
							<%-- <label>
								<liferay-ui:message key="keywords"/>
							</label> --%>
							<liferay-ui:input-search 
								cssClass="search-input input-keyword"
								id="keywords1"
								name="keywords"
								title='<%= LanguageUtil.get(locale, "keywords") %>'
								placeholder='<%= LanguageUtil.get(locale, "put-keyword") %>' 
							/>
						</aui:col>
					</aui:row>
				</div>
			</aui:form>
		</div>
	</aui:nav-bar-search>
</aui:nav-bar>
