<%@page import="org.opencps.util.DLFileEntryUtil"%>
<%@ include file="../init.jsp"%>

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
 **/
%>
<%
	long idFile = ParamUtil.getLong(request, WebKeys.FILE_ID);
	String tenFileExport = "defaultPDF.pdfs";
	String fileName = "";
	String urlFileDowLoad = "";
	String urlFile = "";
// 	urlFile = DLFileEntryUtil.getURLById(idFile);
	if(urlFile.contains(".pdfs")){
		urlFileDowLoad = urlFile.replace(".pdfs", ".pdf");
	} else if(urlFile.contains(".doc")){
		urlFileDowLoad="https://docs.google.com/viewer?url="+PortalUtil.getPortalURL(request)+urlFile+"&embedded=true";
	} else{
		urlFileDowLoad = urlFile;
	}
	
%>
//<%=idFile %>/.....................................
<div align="center">
	<OBJECT data="<%=urlFileDowLoad%>#view=FitH&scrollbar=0&amp;page=1&toolbar=0&statusbar=0&messages=0&navpanes=0"
		type="text/html" TITLE='<%=LanguageUtil.get(pageContext, "view-tai-lieu") %>' 
		WIDTH="100%" HEIGHT="770px"
		style="background: white; min-height: 100%;" >
			<a href="<%=urlFileDowLoad%>">Tai file xuong</a> 
	</object>
</div>
