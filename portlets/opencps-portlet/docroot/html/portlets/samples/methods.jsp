<%@page import="com.liferay.portal.kernel.util.MethodParameter"%>
<%@page import="java.lang.annotation.Annotation"%>
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

<%@page import="org.opencps.datamgt.service.impl.DictItemLocalServiceImpl"%>
<%@page import="org.opencps.datamgt.service.impl.DictCollectionLocalServiceImpl"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%@ include file="/init.jsp"%>

<%
	List<Class<?>> classes = new ArrayList<Class<?>>();

	classes.add(DictItemLocalServiceImpl.class);
	classes.add(DictCollectionLocalServiceImpl.class);

	Class<?> invokeClass = null;
	
	String invokeMethod = ParamUtil.getString(request, "methodName", StringPool.BLANK);
	
	try{
		invokeClass = (Class<?>)request.getAttribute("INVOKE_CLASS");
		
	}catch(Exception e){
		_log.error(e);
	}
	
	for(Class<?> cls : classes){
		%>
			<liferay-ui:panel-container cssClass="opencps-sample-methods" extended="true"  id="<%=cls.getSimpleName() %>">
				<liferay-ui:panel collapsible="<%=true%>" extended="<%=true%>" id='<%=cls.getSimpleName() + "_panel" %>' persistState="<%=true%>" title="<%=cls.getSimpleName() %>">
					<ul>
					<%
						Method[] methods = cls.getDeclaredMethods();
						if(methods != null && methods.length > 0){
							for(int m = 0; m < methods.length; m++){
								if(methods[m].getModifiers() == 1){
									
									List<String> lstParam = new ArrayList<String>();
									
									Class<?>[] params = methods[m].getParameterTypes();
									Annotation[][] annotations =  methods[m].getParameterAnnotations();
									
									System.out.println(annotations.length);
									System.out.println(params.length);
									
									if(params != null && params.length > 0){
										for(int p = 0; p < params.length; p++){
											lstParam.add(params[p].getName());
										}
									}
									
									PortletURL renderInvokeMethodURL = renderResponse.createRenderURL();
									renderInvokeMethodURL.setParameter("mvcPath", templatePath + "view.jsp");
									renderInvokeMethodURL.setParameter("className", cls.getName());
									renderInvokeMethodURL.setParameter("methodName", methods[m].getName());
									renderInvokeMethodURL.setParameter("paramNames", StringUtil.merge(lstParam, StringPool.DASH));
									
									String selected = (Validator.isNotNull(invokeMethod) && 
											invokeMethod.equals(methods[m].getName()) ? "selected":StringPool.BLANK);
									%>
										<li class="<%=selected%>"><a href="<%=renderInvokeMethodURL%>"><%=methods[m].getName() %></a></li>
									<%
								}
							}
						}
					%>
					</ul>
				</liferay-ui:panel>
			</liferay-ui:panel-container>
		<%
	}
%>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.samples.methods.jsp");
%>



