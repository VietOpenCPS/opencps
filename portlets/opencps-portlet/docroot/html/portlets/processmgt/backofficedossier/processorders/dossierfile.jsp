<%@page import="org.opencps.dossiermgt.NoSuchDossierFileException"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.dossiermgt.service.DossierPartLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="java.util.List"%>
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
<%@page import="org.opencps.dossiermgt.NoSuchDossierException"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@ include file="../../init.jsp"%>

<%
	long dossierId = ParamUtil.getLong(request, "dossierId");
	Dossier dossier = null;
	try {
		dossier = DossierLocalServiceUtil.getDossier(dossierId);
	}
	catch (NoSuchDossierException nsde) {
		
	}
	
	List<DossierPart> lsDossierParts = DossierPartLocalServiceUtil.getDossierParts(dossier.getDossierTemplateId());
%>

<%
	for (DossierPart part : lsDossierParts) {
		DossierFile df = null;
		try {
			df = DossierFileLocalServiceUtil.getDossierFileByD_P(dossier.getDossierId(), part.getDossierpartId());
		}
		catch (NoSuchDossierFileException nsdfe) {
			
		}
%>
<aui:input checked="<%= df != null %>" type="checkbox" name="<%= part.getPartNo() %>" label="">
	<%= part.getPartName() %>
</aui:input>
<%
		List<DossierFile> files = DossierFileLocalServiceUtil.getDossierFileByDossierAndDossierPart(dossier.getDossierId(), part.getDossierpartId());
		for (DossierFile f : files) {
%>
<aui:input style="margin-left: 10%;" type="checkbox" name="<%= f.getDisplayName() %>" checked="<%= true %>" label="">
	<span style="margin-left: 10%;"><%= f.getDisplayName() %></span>
</aui:input>
<%
		}
	}
%>
<div id="myTreeView"></div>
<script type="text/javascript">
YUI().use(
		  'aui-tree-view',
		  function(Y) {
		    new Y.TreeViewDD(
		      {
		        boundingBox: '#myTreeView',
		        children: [
		          {
		            children: [
		              {label: 'Watermelon', leaf: true, type: 'check'},
		              {label: 'Apricot', leaf: true, type: 'check'},
		              {label: 'Pineapple', leaf: true, type: 'check'},
		              {label: 'Kiwi', leaf: true, type: 'check'},
		              {label: 'Orange', leaf: true, type: 'check'},
		              {label: 'Pomegranate', leaf: true, type: 'check'}
		            ],
		            expanded: true,
		            label: 'Checkboxes'
		          },
		          {
		            children: [
		              {label: 'Watermelon', leaf: true, type: 'radio'},
		              {label: 'Apricot', leaf: true, type: 'radio'},
		              {label: 'Pineapple', leaf: true, type: 'radio'},
		              {label: 'Kiwi', leaf: true, type: 'radio'},
		              {label: 'Orange', leaf: true, type: 'radio'},
		              {label: 'Pomegranate', leaf: true, type: 'radio'}
		            ],
		            expanded: true,
		            label: 'Radio'
		          },
		          {
		            children: [
		              {label: 'Watermelon', leaf: true, type: 'task'},
		              {label: 'Apricot', leaf: true, type: 'task'},
		              {label: 'Pineapple', leaf: true,  type: 'task'},
		              {label: 'Kiwi', leaf: true, type: 'task'},
		              {label: 'Orange', leaf: true, type: 'task'},
		              {label: 'Pomegranate', leaf: true,  type: 'task'}
		            ],
		            expanded: true,
		            label: 'Task',
		            type: 'task'
		          }
		        ]
		      }
		    ).render();
		  }
		);
</script>
