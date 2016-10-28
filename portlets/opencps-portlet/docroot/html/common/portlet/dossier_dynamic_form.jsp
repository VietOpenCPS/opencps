
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.util.MessageKeys"%>
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


<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.dossiermgt.service.DossierPartLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileDisplayTerms"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionErrors"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionMessages"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="javax.portlet.WindowState"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.model.Business"%>
<%@page import="org.opencps.dossiermgt.bean.AccountBean"%>
<%@page import="org.opencps.util.AccountUtil"%>
<%@page import="org.opencps.accountmgt.service.BusinessLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.service.CitizenLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="org.opencps.backend.util.AutoFillFormData"%>
<%@page import="org.opencps.backend.util.BackendUtils"%>
<%@page import="org.opencps.util.JsonUtils"%>
<%@page import="com.liferay.portal.security.auth.AuthTokenUtil"%>

<%@ include file="/init.jsp"%>

<%
	boolean success = false;

	boolean isViewForm = true;

	try{
		success = !SessionMessages.isEmpty(renderRequest) && SessionErrors.isEmpty(renderRequest);
	}catch(Exception e){
		
	}
	
	long primaryKey = ParamUtil.getLong(request, "primaryKey");
	
	long dossierId = ParamUtil.getLong(request, DossierDisplayTerms.DOSSIER_ID);
	
	long dossierPartId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_PART_ID);
	
	long dossierFileId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_FILE_ID);
	
	long fileGroupId = ParamUtil.getLong(request, DossierDisplayTerms.FILE_GROUP_ID);
	
	long groupDossierPartId = ParamUtil.getLong(request, "groupDossierPartId");

	String groupName = ParamUtil.getString(request, DossierFileDisplayTerms.GROUP_NAME);
	
	String modalDialogId = ParamUtil.getString(request, "modalDialogId");
	
	String redirectURL = ParamUtil.getString(request, "redirectURL");
	
	String sampleData = StringPool.BLANK;
	
	if(primaryKey > 0){
		dossierFileId = primaryKey;
	}
	
	
	DossierPart dossierPart = null;
	
	if(dossierPartId > 0){
		try{
			dossierPart = DossierPartLocalServiceUtil.getDossierPart(dossierPartId);
			sampleData = dossierPart.getSampleData();
		}catch(Exception e){
			
		}
	}
	
	Dossier dossier = null;
	
	if(dossierId > 0){
		try{
			dossier = DossierLocalServiceUtil.getDossier(dossierId);
		}catch(Exception e){
			
		}
	}
	
	
	AccountBean accBean = AccountUtil.getAccountBean(request);
	
	Citizen ownerCitizen = null;
	
	Business ownerBusiness = null;
	
	if (accBean.isCitizen()) {
		ownerCitizen = (Citizen) accBean.getAccountInstance();
		if(dossier != null && (dossier.getDossierStatus().equals(PortletConstants.DOSSIER_STATUS_NEW) || 
						dossier.getDossierStatus().equals(PortletConstants.DOSSIER_STATUS_WAITING))){
			isViewForm = false;
		}
	} else if (accBean.isBusiness()) {
		ownerBusiness = (Business) accBean.getAccountInstance();
		if(dossier != null && (dossier.getDossierStatus().equals(PortletConstants.DOSSIER_STATUS_NEW) || 
						dossier.getDossierStatus().equals(PortletConstants.DOSSIER_STATUS_WAITING))){
			isViewForm = false;
		}
	}else if(accBean.isEmployee()){
		if(dossierPart.getPartType() != PortletConstants.DOSSIER_PART_TYPE_MULTIPLE_RESULT && 
						dossierPart.getPartType() != PortletConstants.DOSSIER_PART_TYPE_RESULT){
			isViewForm = true;
		}else{
			isViewForm = false;
		}
	}
	
	String formData = "";
	
	String auTock = AuthTokenUtil.getToken(request);
	  
	String alpacaSchema = dossierPart != null && Validator.isNotNull(dossierPart.getFormScript()) ? 
	      dossierPart.getFormScript().replaceAll("\\?p_auth=REPLACEKEY", "/group-id/"+ themeDisplay.getScopeGroupId()+"?p_auth="+auTock) : StringPool.BLANK;


	DossierFile dossierFile = null;
	
	if(dossierFileId > 0){
		try{
			dossierFile = DossierFileLocalServiceUtil.getDossierFile(dossierFileId);
		}catch(Exception e){
			
		}
		
		if(dossierFile != null && Validator.isNotNull(dossierFile.getFormData())){
			formData = JsonUtils.quote(dossierFile.getFormData());
		}
	}else{
		formData = AutoFillFormData.dataBinding(sampleData, ownerCitizen, ownerBusiness, dossierId);
	}
	
	String portleName = WebKeys.DOSSIER_MGT_PORTLET;
	
	if(renderResponse.getNamespace().equals(StringPool.UNDERLINE + WebKeys.PROCESS_ORDER_PORTLET + StringPool.UNDERLINE)){
		portleName = WebKeys.PROCESS_ORDER_PORTLET;
	}
	
%>

<liferay-ui:success  key="<%=MessageKeys.DEFAULT_SUCCESS_KEY %>" message="<%=MessageKeys.DEFAULT_SUCCESS_KEY %>"/>

<portlet:actionURL var="updateDynamicFormDataURL" name="updateDynamicFormData"/>

<aui:form 
	name="fm" action="<%=updateDynamicFormDataURL.toString() %>" 
	method="post" onSubmit='<%= "event.preventDefault();" %>'
>
	<aui:input name="redirectURL" type="hidden" value="<%=currentURL %>"/>
	<aui:input name="<%=DossierDisplayTerms.DOSSIER_ID %>" type="hidden" value="<%=dossierId %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.DOSSIER_FILE_ID %>" type="hidden" value="<%=dossierFile != null ? dossierFile.getDossierFileId() : 0 %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID %>" type="hidden" value="<%=dossierPartId %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.FORM_DATA %>" type="hidden" value=""/>
	<aui:input name="<%=DossierFileDisplayTerms.GROUP_NAME %>" type="hidden" value="<%=groupName %>"/>
	<aui:input name="<%=DossierDisplayTerms.FILE_GROUP_ID %>" type="hidden" value="<%=fileGroupId%>"/>
	<aui:input name="<%=DossierFileDisplayTerms.DOSSIER_FILE_TYPE %>" type="hidden" value="<%=String.valueOf(renderResponse.getNamespace().equals(StringPool.UNDERLINE + WebKeys.DOSSIER_MGT_PORTLET + StringPool.UNDERLINE) ? PortletConstants.DOSSIER_FILE_TYPE_INPUT : PortletConstants.DOSSIER_FILE_TYPE_OUTPUT) %>"/>
	<aui:input name="groupDossierPartId" type="hidden" value="<%=groupDossierPartId%>"/>
	
	<aui:fieldset id="dynamicForm"></aui:fieldset>
	<aui:fieldset>
		<c:choose>
			<c:when test="<%=!isViewForm %>">
				<c:if test="<%=Validator.isNotNull(alpacaSchema) %>">
					<aui:button type="button" value="save" name="save" cssClass="saveForm"/>
				</c:if>
					
				<c:if test="<%=dossierFileId > 0%>">
					<aui:button type="button" value="preview" name="preview"/>
					
<%-- 					<aui:button type="button" value="create-file" name="create-file"/> --%>
				</c:if>
			</c:when>
		</c:choose>
	</aui:fieldset>
</aui:form>

<aui:script>

	var alpacaSchema = <%=Validator.isNotNull(alpacaSchema) ? alpacaSchema : PortletConstants.UNKNOW_ALPACA_SCHEMA%>;
	var formData = '<%=formData%>';
	var dossierFileId = '<%=dossierFileId%>';
	
	AUI().ready(function(A){

		if(alpacaSchema.options != 'undefined' && alpacaSchema.schema != 'undefined'){
			
			if(formData != ''){
				alpacaSchema.data = JSON.parse(formData);
			}
			
			//Overwrite function
			alpacaSchema.postRender = function(control){
				$(".saveForm").click(function(e) {
					var formData = control.getValue();
					$("#<portlet:namespace />formData" ).val(JSON.stringify(formData));
					
					//Validate form submit
					var errorMessage = '';
// 					$('div[class*="has-error"] > label').each(function( index ) {
						
// 						errorMessage += ( index + 1 ) + ': ' + $( this ).text() + '\n';
						  
// 					});

					$('div[class*="has-error"] :input').each(function( index ) {
						
						if($(this).val() != null){
							errorMessage += ( index + 1 ) + '\n';
						}
					  
					});
					
					console.log("Alpacajs-required: "+errorMessage);
					
					if(errorMessage.length == 0){
					
						$("#<portlet:namespace />fm" ).submit();
					
					}else{
						
						alert( '<%=LanguageUtil.get(pageContext, "fields-required") %>');
						
					}
					
			    });
				
				$(".alpaca-field-table").delegate('select.alpaca-control', 'change', function(){   
					  var listbox = $('#'+$(this).attr('id') + ' option:selected');
					  var idText = $(this).attr('name') + "Text";
					  var hiddenInput = $("input[name='"+idText+"']");
					  hiddenInput.val(listbox.text());
					  console.log(hiddenInput.val());
					});
			};
		
		}
		var el = $("#<portlet:namespace/>dynamicForm");
		
		Alpaca(el, alpacaSchema);
		
		var createReportBtn = A.one('#<portlet:namespace/>create-file');
		if(createReportBtn){
			createReportBtn.on('click', function(){
				<portlet:namespace/>createReport(dossierFileId);
			});
		}
		
		var previewFormBtn = A.one('#<portlet:namespace/>preview');
		if(previewFormBtn){
			previewFormBtn.on('click', function(){
				<portlet:namespace/>previewForm(dossierFileId);
			});
		}
		
		var success = '<%=success%>';
		
		if(success == 'true'){
			var data = {
				'conserveHash': true
			};
			
			Liferay.Util.getOpener().Liferay.Portlet.refresh('#p_p_id' + '<portlet:namespace/>', data);
			
			<portlet:namespace/>createReport(dossierFileId);
		}
	});
	
	Liferay.provide(window, '<portlet:namespace/>createReport', function(dossierFileId) {
		var A = AUI();
		var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, portleName, themeDisplay.getPlid(), PortletRequest.ACTION_PHASE) %>');
		portletURL.setParameter("javax.portlet.action", "createReport");
		portletURL.setWindowState('<%=WindowState.NORMAL%>');
		var loadingMask = new A.LoadingMask(
			{
				'strings.loading': '<%= UnicodeLanguageUtil.get(pageContext, "exporting-file") %>',
				target: A.one('#<portlet:namespace/>fm')
			}
		);
		
		loadingMask.show();
		
		A.io.request(
			portletURL.toString(),
			{
			    dataType : 'json',
			    data:{    	
			    	<portlet:namespace/>dossierFileId : dossierFileId,
			    },   
			    on: {
			        success: function(event, id, obj) {
						var instance = this;
						var res = instance.get('responseData');
						
						var fileExportDir = res.fileExportDir;
						
						loadingMask.hide();
						if(fileExportDir == ''){
							alert('<%= UnicodeLanguageUtil.get(pageContext, "error-while-export-file") %>');
						}else{
							var ns = '<portlet:namespace/>';
							ns = ns.substring(1, ns.length);
							closeDialog('<portlet:namespace/>dossier-dynamic-form', ns);
						}
					},
			    	error: function(){
			    		loadingMask.hide();
			    	}
				}
			}
		);
	},['aui-io','liferay-portlet-url', 'aui-loading-mask-deprecated']);
	
	Liferay.provide(window, '<portlet:namespace/>previewForm', function(dossierFileId) {
		var A = AUI();
		var uri = '<%=PortletPropsValues.OPENCPS_SERVLET_PREVIEW_DOSSIER_FORM_URL%>' + dossierFileId;
		var loadingMask = new A.LoadingMask(
			{
				'strings.loading': '<%= UnicodeLanguageUtil.get(pageContext, "loading-form") %>',
				target: A.one('#<portlet:namespace/>fm')
			}
		);
		
		loadingMask.show();
		
		openDialog(uri, '<portlet:namespace />previewDynamicForm','<%= UnicodeLanguageUtil.get(pageContext, "preview") %>');
		
		loadingMask.hide();
		
	},['aui-io','liferay-portlet-url', 'aui-loading-mask-deprecated']);
	
</aui:script>


<script type="text/javascript">
	function openCPSSelectedTextValue(id) {
		var listbox = document.getElementById(id);
		var selIndex = listbox.selectedIndex;
		var selText = listbox.options[selIndex].text; 
	    return selText;
	}
	
	function openCPSSelectedbildDataSource(controlId,dictCollectionId, parentItemId) {
		var comboTarget = document.getElementById(controlId);
		comboTarget.innerHTML = "";
		var optionBlank = comboTarget.appendChild(document.createElement('option'));
		optionBlank.value = "";
		optionBlank.text = "<%=LanguageUtil.get(pageContext, "select-combo-blank") %>";
		Liferay.Service(
				  '/opencps-portlet.dictitem/get-dictitems_itemCode_datasource',
				  {
					  collectionCode: dictCollectionId,
					  itemCode: parentItemId,
					  groupId: themeDisplay.getScopeGroupId()
				  },
				  function(obj) {
				    for(j in obj){
	                    var sub_key = j;
	                    var sub_val = obj[j];
	                    var newOpt = comboTarget.appendChild(document.createElement('option'));
						newOpt.value = sub_key;
						newOpt.text = sub_val;
	                }
				  }
				);
	}
	
	function openCPSAutoCompletebildDataSource(controlId, minLength, dictCollectionId, parentItemId, keywords, bildingControlId, iconFa) {
			
		var iconFaObj = '<i class="fa '+iconFa+'"></i> &nbsp;&nbsp;';
		
		var myTemplateDisplay = '<div>{{value}}</div>';
		
		if(iconFa != null){
			myTemplateDisplay = '<div>'+iconFaObj+'{{value}}</div>';
		}
		
		var dataSource = new Bloodhound({
			  datumTokenizer: function (datum) {
			        return Bloodhound.tokenizers.whitespace(datum.value);
			  },
			  queryTokenizer: Bloodhound.tokenizers.whitespace,
			  prefetch: {
					  	url: '/api/jsonws/opencps-portlet.dictitem/get-dictitems_itemCode_keywords_datasource/collection-code/'+dictCollectionId
		  				+'/item-code/'+parentItemId
		  				+'/-keywords'
		  				+'/group-id/'+themeDisplay.getScopeGroupId()
		  				+'?p_auth='+Liferay.authToken,
		  			wildcard: '%QUERY',
				  	filter: function (item) {
	   		           return $.map(item, function (data) {
			                return {
			                    value: data.itemNameCurrentValue,
			                    code: data.itemCode,
			                    desc: data.itemDescriptionCurrentValue
			                };
			            });
				  	}
			  },
			  remote: {
				  	url: '/api/jsonws/opencps-portlet.dictitem/get-dictitems_itemCode_keywords_datasource/collection-code/'+dictCollectionId
		  				+'/item-code/'+parentItemId
		  				+'/keywords/%QUERY'
		  				+'/group-id/'+themeDisplay.getScopeGroupId()
		  				+'?p_auth='+Liferay.authToken,
		  			wildcard: '%QUERY',
				  	filter: function (item) {
	   		           return $.map(item, function (data) {
			                return {
			                    value: data.itemNameCurrentValue,
			                    code: data.itemCode,
			                    desc: data.itemDescriptionCurrentValue
			                };
			            });
				  	}
			  },
		});
		
		// Initialize the Bloodhound suggestion engine
		dataSource.initialize();
		console.log(dataSource);
		$('#'+controlId).typeahead({
			
			  minLength: minLength,
			
			  highlight: true
			
			},
			{
				
				name: 'dataSource-typeahead',
				
				display: 'value',
				
				source: dataSource.ttAdapter(),

				limit: 20,
				
				templates: {
					empty: [
			      	   '<div class="empty-message">',
			     	   '<%=LanguageUtil.get(pageContext, "empty-message") %>',
			    	   '</div>'
			     	  ].join('\n'),
			 		suggestion: Handlebars.compile(myTemplateDisplay)
				}
			}
			).on(
					{
				        'typeahead:select': function(e, datum) {
				        	$("#"+controlId).val(datum.value);
							$("#"+controlId+"Id").val(datum.code);
							$("#"+controlId+"Text").val(datum.value);
							
							if(bildingControlId != null){
								$("#"+bildingControlId).val(datum.desc);
							}
				            console.log(datum);
				            console.log('selected');
				        },
				        'typeahead:change': function(e, datum) {
				        	if($("#"+controlId).val() != $("#"+controlId+"Text").val()){ 
								$("#"+controlId).val('');
								$("#"+controlId+"Id").val('');
								$("#"+controlId+"Text").val('');
								if(bildingControlId != null){
									$("#"+bildingControlId).val('');
								}
							}
				            console.log(datum);
				            console.log('change');
				        }
					}
			);
		
	}
	
	//paging
	function openCPSAutoCompletebildDataSource(controlId, minLength, dictCollectionId, parentItemId, keywords, bildingControlId, iconFa, start, end) {
		
		var iconFaObj = '<i class="fa '+iconFa+'"></i> &nbsp;&nbsp;';
		
		var myTemplateDisplay = '<div>{{value}}</div>';
		
		if(iconFa != null){
			myTemplateDisplay = '<div>'+iconFaObj+'{{value}}</div>';
		}
		
		var dataSource = new Bloodhound({
			  datumTokenizer: function (datum) {
			        return Bloodhound.tokenizers.whitespace(datum.value);
			  },
			  queryTokenizer: Bloodhound.tokenizers.whitespace,
			  prefetch: {
					  	url: '/api/jsonws/opencps-portlet.dictitem/get-dictitems_itemCode_keywords_datasource/collection-code/'+dictCollectionId
		  				+'/item-code/'+parentItemId
		  				+'/-keywords'
		  				+'/group-id/'+themeDisplay.getScopeGroupId()
		  				+'/start/'+start
		  				+'/end/'+end
		  				+'?p_auth='+Liferay.authToken,
		  			wildcard: '%QUERY',
				  	filter: function (item) {
	   		           return $.map(item, function (data) {
			                return {
			                    value: data.itemNameCurrentValue,
			                    code: data.itemCode,
			                    desc: data.itemDescriptionCurrentValue
			                };
			            });
				  	}
			  },
			  remote: {
				  	url: '/api/jsonws/opencps-portlet.dictitem/get-dictitems_itemCode_keywords_datasource/collection-code/'+dictCollectionId
		  				+'/item-code/'+parentItemId
		  				+'/keywords/%QUERY'
		  				+'/group-id/'+themeDisplay.getScopeGroupId()
		  				+'/start/'+start
		  				+'/end/'+end
		  				+'?p_auth='+Liferay.authToken,
		  			wildcard: '%QUERY',
				  	filter: function (item) {
	   		           return $.map(item, function (data) {
			                return {
			                    value: data.itemNameCurrentValue,
			                    code: data.itemCode,
			                    desc: data.itemDescriptionCurrentValue
			                };
			            });
				  	}
			  },
		});
		
		// Initialize the Bloodhound suggestion engine
		dataSource.initialize();
		console.log(dataSource);
		$('#'+controlId).typeahead({
			
			  minLength: minLength,
			
			  highlight: true
			
			},
			{
				
				name: 'dataSource-typeahead',
				
				display: 'value',
				
				source: dataSource.ttAdapter(),

				limit: 20,
				
				templates: {
					empty: [
			      	   '<div class="empty-message">',
			     	   '<%=LanguageUtil.get(pageContext, "empty-message") %>',
			    	   '</div>'
			     	  ].join('\n'),
			 		suggestion: Handlebars.compile(myTemplateDisplay)
				}
			}
			).on(
					{
				        'typeahead:select': function(e, datum) {
				        	$("#"+controlId).val(datum.value);
							$("#"+controlId+"Id").val(datum.code);
							$("#"+controlId+"Text").val(datum.value);
							
							if(bildingControlId != null){
								$("#"+bildingControlId).val(datum.desc);
							}
				            console.log(datum);
				            console.log('selected');
				        },
				        'typeahead:change': function(e, datum) {
				        	if($("#"+controlId).val() != $("#"+controlId+"Text").val()){ 
								$("#"+controlId).val('');
								$("#"+controlId+"Id").val('');
								$("#"+controlId+"Text").val('');
								if(bildingControlId != null){
									$("#"+bildingControlId).val('');
								}
							}
				            console.log(datum);
				            console.log('change');
				        }
					}
			);
		
	}
</script>