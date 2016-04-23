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

<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="java.util.Date"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileDisplayTerms"%>
<%@ include file="../init.jsp"%>

<%
	DossierFile dossierFile = (DossierFile) request.getAttribute(WebKeys.DOSSIER_FILE_ENTRY);

	Date defaultDossierFileDate = dossierFile != null && dossierFile.getDossierFileDate() != null ? 
			dossierFile.getDossierFileDate() : DateTimeUtil.convertStringToDate("01/01/1970");
			
	PortletUtil.SplitDate spd = new PortletUtil.SplitDate(defaultDossierFileDate);
%>
<aui:form name="fm" method="post" action="">
	<aui:row>
		<aui:col width="100">
			<aui:input name="<%= DossierFileDisplayTerms.DISPAY_NAME %>" type="text" cssClass="input97">
				<aui:validator name="required"/>
			</aui:input>
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:col width="70">
			<aui:input name="<%= DossierFileDisplayTerms.DOSSIER_FILE_NO %>" type="text" cssClass="input90"/>
		</aui:col>
		
		<aui:col width="30">
			<label class="control-label custom-lebel" for='<portlet:namespace/><%=DossierFileDisplayTerms.DOSSIER_FILE_DATE %>'>
				<liferay-ui:message key="dossier-file-date"/>
			</label>
			<liferay-ui:input-date
				dayParam="<%=DossierFileDisplayTerms.DOSSIER_FILE_DATE_DAY %>"
				dayValue="<%=spd.getDayOfMoth() %>"
				disabled="<%= false %>"
				monthParam="<%=DossierFileDisplayTerms.DOSSIER_FILE_DATE_MONTH %>"
				monthValue="<%=spd.getMonth() %>"
				name="<%=DossierFileDisplayTerms.DOSSIER_FILE_DATE%>"
				yearParam="<%=DossierFileDisplayTerms.DOSSIER_FILE_DATE_YEAR %>"
				yearValue="<%=spd.getYear() %>"
				formName="fm"
				autoFocus="<%=true %>"
				nullable="<%=dossierFile == null || dossierFile.getDossierFileDate() == null ? true : false %>"
			/>
		</aui:col>
	</aui:row>
	<aui:row>
		<aui:col width="100">
			<aui:input name="<%=DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD %>" type="file"/>
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:button name="agree" type="button" value="agree"/>
		<aui:button name="cancel" type="button" value="cancel"/>
	</aui:row>
</aui:form>

<aui:script use="aui-base,aui-io">
	AUI().ready(function(A){
		var cancelButton = A.one('#<portlet:namespace/>cancel');
		if(cancelButton){
			cancelButton.on('click', function(){
				var dialog = Liferay.Util.getWindow('<portlet:namespace/>dossierFileId');
				dialog.destroy(); // You can try toggle/hide whate
			});
		}
	});
	
	Liferay.provide(window, '<portlet:namespace/>uploadDossierFile', function() {
		var Util = Liferay.Util;
		
		Util.getOpener().Liferay.fire('getUserData', {name:'xxx'});
	});
</aui:script>