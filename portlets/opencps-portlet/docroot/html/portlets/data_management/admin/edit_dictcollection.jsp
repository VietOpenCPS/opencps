
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
<%@page import="org.opencps.datamgt.search.DictCollectionDisplayTerms"%>
<%@page import="org.opencps.datamgt.model.impl.DictCollectionImpl"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="org.opencps.datamgt.OutOfLengthCollectionNameException"%>
<%@page import="org.opencps.datamgt.OutOfLengthCollectionCodeException"%>
<%@page import="org.opencps.datamgt.DuplicateCollectionException"%>
<%@page import="org.opencps.datamgt.NoSuchDictCollectionException"%>
<%@page import="org.opencps.datamgt.EmptyCollectionCodeException"%>
<%@page import="org.opencps.datamgt.EmptyDictCollectionNameException"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@ include file="../init.jsp"%>

<portlet:actionURL var="updateDictCollectionURL" name="updateDictCollection" />

<%
	DictCollection dictCollection = (DictCollection)request.getAttribute(WebKeys.DICT_COLLECTION_ENTRY);
	long collectionId = dictCollection != null ? dictCollection.getDictCollectionId() : 0L;
	String backURL = ParamUtil.getString(request, "backURL");
%>

<liferay-ui:header
	backURL="<%= backURL %>"
	title='<%= (dictCollection == null) ? "add-dictcollection" : "update-dictcollection" %>'
/>

<div class="opencps-datamgt collection-wrapper">
	<div class="edit-form">
		<liferay-ui:error exception="<%= OutOfLengthCollectionCodeException.class %>" message="<%=OutOfLengthCollectionCodeException.class.getName() %>" />
		<liferay-ui:error exception="<%= OutOfLengthCollectionNameException.class %>" message="<%=OutOfLengthCollectionNameException.class.getName() %>" />
		<liferay-ui:error exception="<%= DuplicateCollectionException.class %>" message="<%=DuplicateCollectionException.class.getName() %>" />
		<liferay-ui:error exception="<%= NoSuchDictCollectionException.class %>" message="<%=NoSuchDictCollectionException.class.getName() %>" />
		<liferay-ui:error exception="<%= EmptyCollectionCodeException.class %>" message="<%=EmptyCollectionCodeException.class.getName() %>" />
		<liferay-ui:error exception="<%= EmptyDictCollectionNameException.class %>" message="<%=EmptyDictCollectionNameException.class.getName() %>" />
		<liferay-ui:error key="<%= MessageKeys.DATAMGT_SYSTEM_EXCEPTION_OCCURRED%>" message="<%=MessageKeys.DATAMGT_SYSTEM_EXCEPTION_OCCURRED %>" />

		<aui:form action="<%=updateDictCollectionURL.toString() %>" method="post" name="fm">
			
			<aui:model-context bean="<%=dictCollection %>" model="<%=DictCollection.class %>" />
			<aui:input name="<%=DictCollectionDisplayTerms.DICTCOLLECTION_ID %>" type="hidden"/>
			<aui:input name="redirectURL" type="hidden" value="<%=backURL %>"/>
			<aui:input name="returnURL" type="hidden" value="<%=currentURL %>"/>
			
			<aui:fieldset>
			
				<aui:input name="<%=DictCollectionDisplayTerms.COLLECTION_NAME %>" cssClass="input80">
					<aui:validator name="required"/>
					<aui:validator name="minLength">3</aui:validator>
					<aui:validator name="maxLength">255</aui:validator>
				</aui:input>
				
				<aui:input name="<%=DictCollectionDisplayTerms.COLLECTION_CODE %>" type="text" cssClass="input20">
					<aui:validator name="required"/>
					<aui:validator name="maxLength">100</aui:validator>
				</aui:input>
					
				<aui:input name="<%=DictCollectionDisplayTerms.DESCRIPTION %>" type="textarea" cssClass="input100"/>

			</aui:fieldset>
			<aui:fieldset>
				<aui:button type="submit" name="submit" value="submit"/>
				<aui:button type="reset" value="clear"/>
			</aui:fieldset>	
		</aui:form>
	</div>
</div>