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
<%@ include file="../../../init.jsp"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.kernel.dao.orm.QueryUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearchUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="com.liferay.portlet.documentlibrary.util.DLUtil"%>
<%@page import="org.opencps.util.DLFileEntryUtil"%>
<%@page import="com.liferay.portal.kernel.repository.model.FileEntry"%> 
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%
	List<DossierPart> dossierPartsLevel1 = (List<DossierPart>) request.getAttribute("dossierPartsLevel1");
	Dossier dossier = (Dossier) request.getAttribute("dossier");
%>

<c:if test="<%= dossierPartsLevel1 != null && !dossierPartsLevel1.isEmpty() %>">
		
	<aui:row cssClass="bottom-line mg-l-30 pd_b20 pd_t20 pd-r60"></aui:row>
	
	<aui:row cssClass="pd_t20">
		<label class="bold uppercase">
			<liferay-ui:message key="dossier-file-result"/>
		</label>
		<%
			List<DossierFile> dossierFiles = new ArrayList<DossierFile>();
			int count = 0;
			try {
				if(Validator.isNotNull(dossier)) {
					dossierFiles = DossierFileLocalServiceUtil
									.getDossierFileResult(dossier.getDossierId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS,
										DossierSearchUtil.getDossierFileOrderByComparator(orderFieldDossierFile, orderBydDossierFile));
				}
			} catch (Exception e) {
				
			}
			
			for(DossierFile dossierFile : dossierFiles){
					String fileURL = StringPool.BLANK;
					
					try{
						FileEntry fileEntry = DLFileEntryUtil.getFileEntry(dossierFile.getFileEntryId());
						if(fileEntry != null){
							fileURL = DLUtil.getPreviewURL(fileEntry, fileEntry.getFileVersion(), 
									themeDisplay, StringPool.BLANK);
						}
					}catch(Exception e){
						continue;
						
					}
				%>
					<aui:row cssClass='<%=count > 1 ? "top-line pd_b20 pd_t20" : "pd_b20 pd_t20" %>'>
						<aui:col width="50">
							<aui:row>
								<aui:col width="50">
									<span class="span1">
										<i class="fa fa-circle blue sx10"></i>
									</span>
									<span class="span2">
										<%=count %>
									</span>
									<span class="span9">
										<%=
											Validator.isNotNull(dossierFile.getDossierFileDate()) ? 
											DateTimeUtil.convertDateToString(dossierFile.getDossierFileDate(), DateTimeUtil._VN_DATE_TIME_FORMAT) : 
											DateTimeUtil._EMPTY_DATE_TIME
										%>
									</span>
								</aui:col>
								<aui:col width="50">
									<span class="span5 bold">
										<liferay-ui:message key="dossier-file-no"/>
									</span>
									<span class="span7">
										<%=Validator.isNotNull(dossierFile.getDossierFileNo()) ? dossierFile.getDossierFileNo() : StringPool.DASH %>
									</span>
								</aui:col>
							</aui:row>
						</aui:col>
						<aui:col width="50">
							<span class="span3 bold">
								<liferay-ui:message key="dossier-file-name"/>
							</span>
							<span class="span6">
								<a class="blue" href="<%=fileURL%>" target="_blank">
									<%=Validator.isNotNull(dossierFile.getDisplayName()) ? dossierFile.getDisplayName() : StringPool.BLANK  %>
								</a>
							</span>
							<span class="span3">
								
							</span>
						</aui:col>
					</aui:row>
				<%
				count ++;
			}
		%>
	</aui:row>
</c:if>