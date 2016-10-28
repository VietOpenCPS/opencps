<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.json.JSONFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.json.JSONObject"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
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

<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.bean.ServiceBean"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="org.opencps.dossiermgt.search.ServiceSearch"%>
<%@page import="org.opencps.dossiermgt.search.ServiceSearchTerms"%>
<%@page import="org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.util.ServiceUtil"%>
<%@page import="org.opencps.util.PortletConstants"%>

<%@ include file="../../init.jsp"%>

<%
	long serviceDomainId = ParamUtil.getLong(request, "serviceDomainId");
	
	long govAgencyId = ParamUtil.getLong(request, "govAgencyId");
	
	String dossierStatus = ParamUtil.getString(request, "dossierStatus", StringPool.BLANK);
	
	String tabs1 = ParamUtil.getString(request, "tabs1", DossierMgtUtil.TOP_TABS_DOSSIER);

	PortletURL searchURL = renderResponse.createRenderURL();
	searchURL.setParameter("mvcPath", templatePath + "display/20_80_servicelist_04.jsp");
	searchURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER);
	searchURL.setParameter("isListServiceConfig", String.valueOf(true));
	searchURL.setParameter("backURL", currentURL);
	
	List<DictItem> dictItems = PortletUtil.getDictItemInUseByCode(themeDisplay.getScopeGroupId(), 
			PortletPropsValues.DATAMGT_MASTERDATA_SERVICE_DOMAIN, 
			PortletConstants.TREE_VIEW_DEFAULT_ITEM_CODE);
%>

<aui:row>
	<aui:col width="100" >

		<liferay-util:include page='<%=templatePath + "toolbar.jsp" %>' servletContext="<%=application %>" />

		<ul class="sitemap-class opencps-horizontal">
		
			<%
				for(DictItem dictItem: dictItems){
					searchURL.setParameter("administrationId", String.valueOf(dictItem.getDictItemId()));
					searchURL.setParameter("dictItemCode", dictItem.getItemCode());
			%>
			
			<li onclick="window.location.href='<%=searchURL.toString() %>'">
				
				<div class="img-<%=dictItem.getItemCode() %>"> 
					<div> 
						<a href="<%=searchURL.toString() %>"><%=dictItem.getItemName(locale) %></a> 
					</div>
				</div>
				
			</li>
			
			<%
				} 
			%>
		
		</ul>
		
	</aui:col>
</aui:row>

<portlet:actionURL var="keywordsAutoCompleteURL" name="keywordsAutoComplete"/>

<script type="text/javascript">

AUI().ready(function(A){
	var dataSource = new Bloodhound({
		  datumTokenizer: function (datum) {
		        return Bloodhound.tokenizers.whitespace(datum.value);
		  },
		  queryTokenizer: Bloodhound.tokenizers.whitespace,
		  prefetch: {
			  	url: '<%=keywordsAutoCompleteURL.toString() %>',
			  	
			  	filter: function (item) {
			           return $.map(item, function (data) {
		                return {
		                	value: data.serviceName,
			                   code: data.serviceinfoId
		                };
		            });
			  	}
		  }
	});
	// Initialize the Bloodhound suggestion engine
	dataSource.initialize();
	$('#<portlet:namespace/>keywords1').typeahead({
		
		  highlight: true
		
		},
		{
			
			name: 'dataSource-typeahead',
			
			display: 'value',
			
			source: dataSource.ttAdapter(),

			limit: 8,
			
			templates: {
				empty: [
		      	   '<div class="empty-message">',
		     	   '<%=LanguageUtil.get(pageContext, "empty-message") %>',
		    	   '</div>'
		     	  ].join('\n'),
		 		suggestion: Handlebars.compile('<div> <i class="icon-file"></i> &nbsp;&nbsp; {{value}}</div>')
			}
		}
		).on(
				{
			        'typeahead:select': function(e, datum) {
			        	var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.P26_SUBMIT_ONLINE, PortalUtil.getPlidFromPortletId(themeDisplay.getScopeGroupId(),  WebKeys.P26_SUBMIT_ONLINE), PortletRequest.RENDER_PHASE) %>');
			    		portletURL.setParameter("mvcPath", "/html/portlets/dossiermgt/submit/dossier_submit_online.jsp");
			    		portletURL.setWindowState('<%=LiferayWindowState.NORMAL.toString() %>'); 
			    		portletURL.setPortletMode("normal");
			    		portletURL.setParameter("serviceinfoId", datum.code + "");
			    		portletURL.setParameter("backURL", "<%=currentURL.toString() %>");
			    		
			    		window.location = portletURL.toString();
			            console.log(datum);
			            console.log('selected');
			        },
			        'typeahead:change': function(e, datum) {
			            console.log(datum);
			            console.log('change');
			        }
				}
		);
});

</script>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.frontoffice.frontofficeservicelist.jsp");
%>
