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
<%@page import="com.liferay.portal.kernel.bean.BeanParamUtil"%>
<%@page import="com.liferay.portal.kernel.repository.model.FileEntry"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.dossiermgt.model.impl.DossierPartImpl"%>
<%@page import="org.hsqldb.SessionManager"%>
<%@ include file="../init.jsp"%>

<%
	
	//DossierFile dossierFile = (DossierFile) request.getAttribute(WebKeys.DOSSIER_FILE_ENTRY);

	//DossierPart dossierPart = (DossierPart) request.getAttribute(WebKeys.DOSSIER_PART_ENTRY);
	
	long dossierPartId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_PART_ID);

	int index = ParamUtil.getInteger(request, "index");
	
	String groupName = ParamUtil.getString(request, "groupName");
	
	List<DossierPart> dossierParts3 = new ArrayList<DossierPart>();
	DossierPart dossierPart3 = new DossierPartImpl();
	dossierPart3.setDossierpartId(3);
	dossierPart3.setPartType(4);
	dossierPart3.setPartName("dossierPart3");
	dossierPart3.setTreeIndex("3");
	
	dossierParts3.add(dossierPart3);
	
	DossierPart dossierPart31 = new DossierPartImpl();
	dossierPart31.setDossierpartId(31);
	dossierPart31.setPartType(1);
	dossierPart31.setPartName("dossierPart31");
	dossierPart31.setTreeIndex("3.1");
	
	dossierParts3.add(dossierPart31);
	
	DossierPart dossierPart32 = new DossierPartImpl();
	dossierPart32.setDossierpartId(32);
	dossierPart32.setPartType(2);
	dossierPart32.setPartName("dossierPart32");
	dossierPart32.setTreeIndex("3.2");
	
	dossierParts3.add(dossierPart32);
	
	%>
		<div id='<%=renderResponse.getNamespace() + "privateDossierPartGroup" + dossierParts3.get(0).getDossierpartId() + StringPool.DASH + index%>' class="opencps dossiermgt dossier-part-tree">
			<%
			for(DossierPart dossierPart : dossierParts3){
				int level = 1;
				String treeIndex = dossierPart.getTreeIndex();
				if(Validator.isNotNull(treeIndex)){
					level = StringUtil.count(treeIndex, StringPool.PERIOD);
				}
				
				%>
					<div 
						id='<%=renderResponse.getNamespace() + "row-" + dossierPart.getDossierpartId() + StringPool.DASH + index %>' 
						index="<%=index %>"
						dossier-part="<%=dossierPart.getDossierpartId() %>"
						class="opencps dossiermgt dossier-part-row"
					>
						<span class='<%="level-" + level + " opencps dossiermgt dossier-part"%>'>
							<i id='<%="rowcheck" + dossierPart.getDossierpartId() + StringPool.DASH + index %>' class="fa fa-square-o" aria-hidden="true"></i>
							<span class="opencps dossiermgt dossier-part-name">
								<%
									if(Validator.isNotNull(groupName) && dossierParts3.indexOf(dossierPart) == 0){
										%>
											<%=groupName %>
										<%
									}else{
										%>
											<%=dossierPart.getPartName() %>
										<%
									}
								%>
							</span>
						</span>
					
						<span class="opencps dossiermgt dossier-part-control">
							<liferay-util:include page="/html/portlets/dossiermgt/frontoffice/dossier_file_controls.jsp"  servletContext="<%=application %>">
								<portlet:param name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID %>" value="<%=String.valueOf(dossierPart.getDossierpartId()) %>"/>
								<portlet:param name="index" value="<%=String.valueOf(index) %>"/>
								<portlet:param name="level" value="<%=String.valueOf(level) %>"/>
								<portlet:param name="partType" value="<%=String.valueOf(dossierPart.getPartType()) %>"/>
							</liferay-util:include>
						</span>
					</div>
				<%
				index ++;
			}	
		%>
		</div>
	<%
%>
