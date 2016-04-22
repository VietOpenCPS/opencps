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
<%@page import="org.opencps.usermgt.search.EmployeeDisplayTerm"%>
<%@page import="org.opencps.usermgt.service.WorkingUnitLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.model.WorkingUnit"%>
<%@page import="org.opencps.usermgt.model.JobPos"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.usermgt.model.Employee"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.usermgt.service.EmployeeLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.service.JobPosLocalServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.usermgt.util.UserMgtUtil"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.usermgt.NoSuchJobPosException"%>
<%@page import="org.opencps.usermgt.NoSuchWorkingUnitException"%>

<%@ include file="../../init.jsp"%>
<%

	Employee employee = (Employee)request.getAttribute(WebKeys.EMPLOYEE_ENTRY);

	WorkingUnit mappingWorkingUnit = (WorkingUnit)request.getAttribute(WebKeys.WORKING_UNIT_MAPPING_ENTRY);
	
	JobPos mainJobPos = (JobPos)request.getAttribute(WebKeys.MAIN_JOB_POS_ENTRY);
	
	boolean userViewProfile = GetterUtil.getBoolean((Boolean)request.getAttribute(WebKeys.USERMGT_VIEW_PROFILE), false);
	
	List<JobPos> jobPoses = new ArrayList<JobPos>();
	
	long mappingWorkingUnitId = 0;
	
	if(mappingWorkingUnit != null){
		mappingWorkingUnitId = mappingWorkingUnit.getWorkingunitId();
	}
	
	if(employee != null){
		try{
			jobPoses = JobPosLocalServiceUtil.getEmployeeJobPoses(employee.getEmployeeId());
			System.out.println(jobPoses.size());
		}catch(Exception e){
			// Nothing todo
		}
	}
	
	int[] jobPosIndexes = null;

	String jobPosIndexesParam = ParamUtil.getString(request, "jobPosIndexesParam");

	if (jobPoses != null && !jobPoses.isEmpty()) {
		
		jobPosIndexes = new int[jobPoses.size()];
		
		int count = 0;
		
		for(JobPos jobPos : jobPoses){
			jobPosIndexes[count] = count;
			count ++;
		}
	}
	else {
		if (jobPosIndexes == null) {
			jobPosIndexes = new int[] {0};
		}
	}
	
	List<WorkingUnit> workingUnits = WorkingUnitLocalServiceUtil.getWorkingUnit(scopeGroupId, true);
	
	long selectedMainWorkingUnitId = 0;
%>

<aui:model-context bean="<%=mappingWorkingUnit%>" model="<%=WorkingUnit.class%>" />
<liferay-ui:error-marker key="errorSection" value="working_unit" />

<liferay-ui:error exception="<%= NoSuchWorkingUnitException.class %>" 
	message="<%=NoSuchWorkingUnitException.class.getName() %>" 
/>
<liferay-ui:error exception="<%= NoSuchJobPosException.class %>" 
	message="<%=NoSuchJobPosException.class.getName() %>" 
/>
<aui:row>
	<aui:col width="100">
		<aui:select 
			name="<%= EmployeeDisplayTerm.WORKING_UNIT_ID %>" 
			cssClass="input95"
			showEmptyOption="<%=true %>"
			required="<%=true %>"
			disabled='<%=userViewProfile ? true : false %>'
		>
			<%
				if(workingUnits != null){
					for(WorkingUnit workingUnit : workingUnits){
						%>
							<aui:option 
								value="<%= workingUnit.getWorkingunitId()%>"
								selected="<%=mappingWorkingUnit != null && mappingWorkingUnit.getWorkingunitId() == workingUnit.getWorkingunitId()%>"
							>
								<%=workingUnit.getName() %>
							</aui:option>
						<%
					}
				}
			%>
		</aui:select>
	</aui:col>
</aui:row>

<aui:row>
	<aui:col width="100">
		<aui:input 
			name="<%= EmployeeDisplayTerm.WORKING_STATUS  %>" 
			type="checkbox" 
			inlineField="<%= true %>" 
			inlineLabel="right"
			value="<%=employee != null 
				&& employee.getWorkingStatus() == PortletConstants.WORKING_STATUS_ACTIVATE ? true : false %>"
			disabled='<%=userViewProfile ? true : false %>'
		/>
	</aui:col>
</aui:row>

<label><liferay-ui:message key="main-jobpos"/></label>
<aui:row id="mainJobPosBoundingBox">
	<aui:col width="50">
		<aui:select 
			name='<%= EmployeeDisplayTerm.WORKING_UNIT_ID%>' 
			label='<%=StringPool.BLANK %>' 
			onChange='<%=renderResponse.getNamespace() + "getJobPosByWorkingUnitId(this)" %>'
			required='<%=true %>'
			disabled="<%=userViewProfile ? true : false %>"
		>
			<aui:option value=""><liferay-ui:message key="select-working-unit"/></aui:option>
			<%
				if(mainJobPos != null){
					WorkingUnit workingUnit = null;
					try{
						workingUnit = WorkingUnitLocalServiceUtil.getFirstWorkingUnitByJobPosId(mainJobPos.getJobPosId());
					}catch(Exception e){
						
					}
					
					if(workingUnit != null){
						selectedMainWorkingUnitId = workingUnit.getWorkingunitId();
					}
				}
				if(mappingWorkingUnitId > 0){
					List<WorkingUnit> workingUnitsTemp = UserMgtUtil.getWorkingUnitsForEmployess(scopeGroupId, mappingWorkingUnitId);
					for(WorkingUnit workingUnit : workingUnitsTemp){
						%>
							<aui:option value="<%=workingUnit.getWorkingunitId()%>" selected="<%= workingUnit.getWorkingunitId() == selectedMainWorkingUnitId %>">
								<%=workingUnit.getName() %>
							</aui:option>
						<%
					}
				}
			%>
		</aui:select>
	</aui:col>
	<aui:col width="50">
		<aui:select 
			name='<%=EmployeeDisplayTerm.MAIN_JOBPOS_ID %>' 
			label='<%=StringPool.BLANK %>'  
			required='<%=true %>'
			disabled='<%=userViewProfile ? true : false %>'
		>
			<aui:option value=""><liferay-ui:message key="select-jobpos"/></aui:option>
			<%
				if(selectedMainWorkingUnitId > 0){
					List<JobPos> jobPos = JobPosLocalServiceUtil.getJobPoss(scopeGroupId, selectedMainWorkingUnitId);
					if(jobPos != null){
						for(JobPos jobPosTem : jobPos){
							%>
								<aui:option value="<%=jobPosTem.getJobPosId() %>" selected="<%=jobPosTem.getJobPosId() == mainJobPos.getJobPosId() %>">
									<%=jobPosTem.getTitle() %>
								</aui:option>
							<%
						}
					}
				}
			%>
		</aui:select>
	</aui:col>
</aui:row>

<label><liferay-ui:message key="other-jobpos"/></label>
<aui:row id="opencps-usermgt-employee-jobpos">
	<aui:fieldset id="boundingBox">
	<%
		for(int i = 0; i < jobPosIndexes.length; i++){
			
			int jobPosIndex = jobPosIndexes[i];
			JobPos jobPos = null;
			
			if(jobPoses != null && !jobPoses.isEmpty()){
				jobPos = jobPoses.get(i);
			}
			
			System.out.println(jobPosIndex);
			
			long selectedWorkingUnitId = 0;
			
			%>
				<div class="lfr-form-row lfr-form-row-inline">
					<div class="row-fields">
						<aui:col width="50">
							<aui:select 
								name='<%= EmployeeDisplayTerm.WORKING_UNIT_ID + jobPosIndex %>' 
								label='<%=StringPool.BLANK %>' 
								onChange='<%=renderResponse.getNamespace() + "getJobPosByWorkingUnitId(this)" %>'
								showEmptyOption='<%=true %>'
								disabled='<%=userViewProfile ? true : false %>'
							>
							
								<%

									if(jobPos != null){
										WorkingUnit workingUnit = null;
										try{
											workingUnit = WorkingUnitLocalServiceUtil.getFirstWorkingUnitByJobPosId(jobPos.getJobPosId());
										}catch(Exception e){
											
										}
										
										if(workingUnit != null){
											selectedWorkingUnitId = workingUnit.getWorkingunitId();
											System.out.println(selectedWorkingUnitId);
										}
									}
									if(mappingWorkingUnitId > 0){
										List<WorkingUnit> workingUnitsTemp = UserMgtUtil.getWorkingUnitsForEmployess(scopeGroupId, mappingWorkingUnitId);
										for(WorkingUnit workingUnit : workingUnitsTemp){
											%>
												<aui:option value="<%=workingUnit.getWorkingunitId()%>" selected="<%= workingUnit.getWorkingunitId() == selectedWorkingUnitId %>">
													<%=workingUnit.getName() %>
												</aui:option>
											<%
										}
									}
								%>
								
							</aui:select>
						</aui:col>
						<aui:col width="50">
							<aui:select 
								name='<%= "jobPosId" + jobPosIndex %>' 
								label='<%=StringPool.BLANK %>'
								showEmptyOption='<%=true %>'
								disabled='<%=userViewProfile ? true : false %>'
							>
								<%
									if(selectedWorkingUnitId > 0 && jobPos != null){
										List<JobPos> jobPosTemps = JobPosLocalServiceUtil.getJobPoss(scopeGroupId, selectedWorkingUnitId);
										if(jobPos != null){
											for(JobPos jobPosTem : jobPosTemps){
												%>
													<aui:option value="<%=jobPosTem.getJobPosId() %>" selected="<%=jobPosTem.getJobPosId() == jobPos.getJobPosId() %>">
														<%=jobPosTem.getTitle() %>
													</aui:option>
												<%
											}
										}
									}
								%>
							</aui:select>
						</aui:col>
					</div>
				</div>
				
			<%
		}
	%>
	</aui:fieldset>
	
	<aui:input name="jobPosIndexes" type="hidden" value="<%= StringUtil.merge(jobPosIndexes)%>" />
</aui:row>
