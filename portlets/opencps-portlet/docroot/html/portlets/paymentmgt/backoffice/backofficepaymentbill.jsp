<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
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
<%@ include file="../init.jsp"%>
<%
	String backRedirect = ParamUtil.getString(request, "redirect");
%>
<liferay-ui:header
	backURL="<%= backRedirect %>"
	title="payment-report"
	backLabel="back"
/>
<p></p>
<portlet:actionURL name="createReport" var="createReportURL" />
<aui:form name="payForm" action="#">
<div id="<portlet:namespace />wrapPDF" align="center">
	<iframe src="" height="200px" width="100%"></iframe>
</div>
<aui:button name="back" value="back" href="<%=backRedirect.toString() %>" />
</aui:form>
<aui:script>
AUI().ready(function(A){
			<portlet:namespace/>createReport(1);
});
	Liferay.provide(window, '<portlet:namespace/>createReport', function(paymentFileId) {
		var A = AUI();
		var loadingMask = new A.LoadingMask(
			{
				'strings.loading': '<%= UnicodeLanguageUtil.get(pageContext, "exporting-file") %>',
				target: A.one('#<portlet:namespace/>wrapPDF')
			}
		);
		
		loadingMask.show();
		
		A.io.request(
			'<%=createReportURL.toString() %>',
			{
			    dataType : 'json',
			    data:{    	
			    	<portlet:namespace/>paymentFileId : paymentFileId,
			    },   
			    on: {
			        success: function(event, id, obj) {
						var instance = this;
						var res = instance.get('responseData');
						
						var fileExportDir = res.fileExportDir;
						
						if(fileExportDir == ''){
// 							loadingMask.hide();
						}else{
							loadingMask.hide();
							A.one('#<portlet:namespace/>wrapPDF').setHTML("<iframe src=\""+fileExportDir + "\"" + "height=\"600px\" width=\"100%\"></iframe>");
						}
					},
			    	error: function(){
			    		loadingMask.hide();
			    	}
				}
			}
		);
	},['aui-io','liferay-portlet-url', 'aui-loading-mask-deprecated']);
</aui:script>
