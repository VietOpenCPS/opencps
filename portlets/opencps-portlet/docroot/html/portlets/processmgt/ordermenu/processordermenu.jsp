<%@page import="org.opencps.processmgt.search.ProcessOrderDisplayTerms"%>
<%@page import="com.liferay.portal.kernel.util.GroupThreadLocal"%>
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */
<<<<<<< HEAD
%>
=======
%>

<%@ include file="../init.jsp" %>

<%
	long[] roleIds = user.getRoleIds();
	String active = PortalUtil.getOriginalServletRequest(request).getParameter("_"+WebKeys.PROCESS_ORDER_PORTLET+"_"+ProcessOrderDisplayTerms.PROCESS_STEP_ID);
	boolean counter = true;
%>
<liferay-portlet:renderURL var="renderURL" portletName="<%=WebKeys.PROCESS_ORDER_PORTLET %>">
	<portlet:param name="mvcPath" value="/html/portlets/processmgt/processorder/processordertodolist.jsp"/>
</liferay-portlet:renderURL>
<liferay-portlet:actionURL  var="menuCounterUrl" name="menuCounterAction" >
</liferay-portlet:actionURL>
<%=ProcessOrderUtils.generateMenuBuocXuLy(renderRequest, roleIds, active, counter, renderURL.toString())  %>
<aui:script use="io,aui-loading-mask">
	menu_left_count('<%=menuCounterUrl.toString() %>');
</aui:script>
<script type="text/javascript">
function openCPS_menu_submit(renderURL, stepId) {
	var A = AUI();
	window.location = renderURL + '&<%="_"+WebKeys.PROCESS_ORDER_PORTLET+"_"+ProcessOrderDisplayTerms.PROCESS_STEP_ID %>=' + stepId;
}
function menu_left_count(url) {
	var A = AUI();
	var allBadge = A.all(".badge");
    AUI().io(
    		url,
            {
                on: {
                	start: function() {
                        // Set HMTL
                        allBadge.each(function (taskNode) {
                        	if (taskNode.loadingmask == null) {
                        		taskNode.plug(A.LoadingMask, {background: '#000'});
                           	 	taskNode.loadingmask.show();
                        	}
                        });
                		console.log("menu js sleep start!");
                    },

                    success: function(id, xhr) {
                        // Grab the elements
                    	var json = JSON.parse(xhr.responseText);
                    	for(j in json){
                            var sub_key = j;
                            var sub_val = json[j];
                            A.one('#'+sub_key).setHTML(sub_val);
                        }
                    },
                    
                    end: function() {
                    	allBadge.each(function (taskNode) {
                       	 taskNode.loadingmask.hide();
                       	 taskNode.unplug();
                        });
                        console.log("menu js sleep end!");
                    }
                }
            }
    );
}

</script>
<style>
.menu-opencps{
	list-style: outside none none; 
	margin: 0px !important;
}
.menu-opencps-li{
	line-height: 20px ! important; 
	padding-bottom: 5px; 
	padding-top: 5px; 
	border-bottom: 1px solid gainsboro;
	word-break: break-all;
	overflow: hidden;
	cursor:pointer;
}
.aui .badge{
	float: right;
	border-radius: 4px !important;
}
.menu-opencps-li a:hover{
	text-decoration: none;
}
.active-menu, .menu-opencps-li:hover, .menu-opencps-li:active, .menu-opencps-li:focus{
	text-decoration: none;
	background-color: #f5f5f5;
}
.loadingmask{
	left: 34px !important;
}
</style>
>>>>>>> center_repo/master
