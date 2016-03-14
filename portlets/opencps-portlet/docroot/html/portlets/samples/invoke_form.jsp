
<%@page import="com.liferay.portal.kernel.util.OrderByComparator"%>
<%@page import="com.liferay.portal.service.ServiceContext"%>
<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.lang.reflect.Method"%>
<%
/*
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

<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@ include file="/init.jsp"%>

<%
	Class<?> invokeClass = null;

	String invokeMethod = ParamUtil.getString(request, "methodName", StringPool.BLANK);
	String paramNames = ParamUtil.getString(request, "paramNames", StringPool.BLANK);
	
	try{
		invokeClass = (Class<?>)request.getAttribute("INVOKE_CLASS");
	}catch(Exception e){
		_log.error(e);
	}
	
%>

<c:choose>
	<c:when test="<%=invokeClass != null %>">
		<h5>Class Name: <%=invokeClass.getName() %></h5>
		<h5>Method Name: <%=invokeMethod %></h5>
		<%
			String[] arrParamClassName = StringUtil.split(paramNames, StringPool.DASH);
			List<String> lstParam = ListUtil.fromArray(arrParamClassName);
			Class<?>[] params = new Class<?>[arrParamClassName.length];
		
			if(lstParam != null){
				int count = 0;
				for(String param : lstParam){
					Class<?> paramClass = null;
					if(param.equals("long")){
						paramClass = long.class;
					}else if(param.equals("int")){
						paramClass = int.class;
					}else if(param.equals("short")){
						paramClass = short.class;
					}else{
						paramClass = Class.forName(param);
					}
					
					params[count] = paramClass;
					count++;
				}
			}
			
			Method method = invokeClass.getDeclaredMethod(invokeMethod, params);
			
			
		%>
		
		<h5>Return type: <%=method.getReturnType().getName() %></h5>
		<h5>Exception Name: <%=method.getExceptionTypes() %></h5>
		
			<%
			if(params != null && params.length > 0){
				for(int p = 0; p < params.length; p++){
					if(params[p].getName().equals(ServiceContext.class.getName()) ||
							params[p].getName().equals(OrderByComparator.class.getName())){
						continue;
					}else{
						if(params[p].getName().equals(boolean.class.getName())){
							%>
								<aui:input name='<%="field_" + p %>' value="" suffix="<%=params[p].getName() %>" type="checkbox"/>
							<%
						}else{
							%>
								<aui:input name='<%="field_" + p %>' value="" suffix="<%=params[p].getName() %>" type="text"/>
							<%
						}
					}
				}
			}
		%>
	</c:when>
	<c:otherwise>
		<div class="portlet-msg-info"><liferay-ui:message key="no-class-for-invoke"></liferay-ui:message></div>
	</c:otherwise>
</c:choose>



<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.samples.invoke_form.jsp");
%>