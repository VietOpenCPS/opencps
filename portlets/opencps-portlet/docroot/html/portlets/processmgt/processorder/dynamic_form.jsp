
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

<%@page import="net.sf.jasperreports.engine.JasperReport"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.service.DossierPartLocalServiceUtil"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionErrors"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionMessages"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="org.opencps.report.datasource.adapter.JRJSONDataSource"%>
<%@page import="net.sf.jasperreports.engine.JRDataSource"%>
<%@page import="net.sf.jasperreports.engine.JREmptyDataSource"%>
<%@page import="net.sf.jasperreports.engine.JasperExportManager"%>
<%@page import="net.sf.jasperreports.engine.JasperFillManager"%>
<%@page import="net.sf.jasperreports.engine.JasperPrint"%>
<%@page import="java.nio.charset.StandardCharsets"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="net.sf.jasperreports.engine.JasperCompileManager"%>
<%@page import="java.io.InputStream"%>
<%@ include file="../init.jsp"%>

<%
	boolean success = false;
	
	try{
		success = !SessionMessages.isEmpty(renderRequest) && SessionErrors.isEmpty(renderRequest);
		
	}catch(Exception e){
		
	}
	
	long dossierPartId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_PART_ID);

	long dossierFileId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_FILE_ID);

	int index = ParamUtil.getInteger(request, DossierFileDisplayTerms.INDEX);

	DossierPart dossierPart = null;
	
	if(dossierPartId > 0){
		try{
			dossierPart = DossierPartLocalServiceUtil.getDossierPart(dossierPartId);
		}catch(Exception e){
			
		}
	}
	
	String formData = StringPool.BLANK;
	
	//String formData = GetterUtil.getString((String)request.getAttribute(WebKeys.FORM_DATA + String.valueOf(dossierPartId) + StringPool.DASH + String.valueOf(index)), StringPool.BLANK);
	
	String alpacaSchema = dossierPart != null && Validator.isNotNull(dossierPart.getFormScript()) ? 
			dossierPart.getFormScript() : PortletConstants.UNKNOW_ALPACA_SCHEMA;
	try{
		formData = session.getAttribute(WebKeys.FORM_DATA + String.valueOf(dossierPartId) + StringPool.DASH + String.valueOf(index)).toString();
	}catch(Exception e){
		
	}
	
	DossierFile dossierFile = null;
	
	if(dossierFileId > 0){
		try{
			dossierFile = DossierFileLocalServiceUtil.getDossierFile(dossierFileId);
		}catch(Exception e){
			//nothing todo
		}
		
		if(dossierFile != null && Validator.isNotNull(dossierFile.getFormData())){
			formData = dossierFile.getFormData();
		}
	}
	
	if(dossierFile != null && Validator.isNotNull(dossierFile.getFormData())){
		InputStream template = new ByteArrayInputStream(dossierPart.getFormReport().getBytes(StandardCharsets.UTF_8));
		JasperReport jasperReport = JasperCompileManager.compileReport(template);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<String, Object>() , JRJSONDataSource.getDataSource(dossierFile.getFormData())); 
		
		JasperExportManager.exportReportToPdfFile(jasperPrint, "/home/trungnt/test.pdf");
	} 
	
	
%>

<portlet:actionURL var="updateTempDynamicFormDataURL" name="updateTempDynamicFormData"/>

<aui:form 
	name="fm" action="<%=updateTempDynamicFormDataURL.toString() %>" 
	method="post"
	onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "saveDynamicFormData();" %>'
>
	<aui:input name="redirectURL" type="hidden" value="<%=currentURL %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.INDEX %>" type="hidden" value="<%=index %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID %>" type="hidden" value="<%=dossierPartId %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.FORM_DATA %>" type="hidden" value=""/>
	<aui:fieldset id="dynamicForm"></aui:fieldset>
	<aui:fieldset>
		<aui:button type="button" value="save" name="save" cssClass="saveForm"/>
	</aui:fieldset>
</aui:form>

<aui:script>
	var success = '<%=success%>';
	var alpacaSchema = <%=alpacaSchema%>;
	var index = '<%=index%>';
	var dossierPartId = '<%=dossierPartId%>';
	var formData = '<%=formData%>';
	
	AUI().ready(function(A){
		if(success == 'true'){
			if(formData != ''){
				jsonData = JSON.parse(formData);
			}
			var responseData = new Object();
			responseData.index = index;
			responseData.dossierPartId = dossierPartId;
			responseData.formData = JSON.parse(formData);
			<portlet:namespace/>responseData(responseData);
		}
		
		if(alpacaSchema.options != 'undefined' && alpacaSchema.schema != 'undefined'){
			
			if(formData != ''){
				alpacaSchema.data = JSON.parse(formData);
			}
			
			//Overwrite function
			alpacaSchema.postRender = function(control){
				$(".saveForm").click(function(e) {
					var formData = control.getValue();
					$("#<portlet:namespace />formData" ).val(JSON.stringify(formData));
					$("#<portlet:namespace />fm" ).submit();
					
			    });
			};
		
		}
		var el = $("#<portlet:namespace/>dynamicForm");
		
		Alpaca(el, alpacaSchema);
	});
	
	Liferay.provide(window, '<portlet:namespace/>responseData', function(schema) {
		var Util = Liferay.Util;
		Util.getOpener().Liferay.fire('getDynamicFormDataSchema', {responseData:schema});
		//<portlet:namespace/>closeDialog();
	});
	
	Liferay.provide(window, '<portlet:namespace/>closeDialog', function() {
		var dialog = Liferay.Util.getWindow('<portlet:namespace/>dynamicForm');
		dialog.destroy(); // You can try toggle/hide whate
	});
	
	Liferay.provide(window, '<portlet:namespace/>saveDynamicFormData', function() {
		var A = AUI();
		var uri = A.one('#<portlet:namespace/>fm').attr('action');
		var configs = {
             method: 'POST',
             form: {
                 id: '#<portlet:namespace/>fm'
             },
             on: {
             	failure: function(event, id, obj) {
             		console.log(event);
				},
				success: function(event, id, obj) {
					var response = this.get('responseData');
					console.log(response);
					
				},
                complete: function(event, id, obj){
                	console.log(event);
                }
             }
        };
	            
	    A.io.request(uri, configs); 
	},['aui-io']);
</aui:script>
