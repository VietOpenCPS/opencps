<%@page import="org.opencps.statisticsmgt.util.StatisticsUtil"%>

<%@ include file="../init.jsp" %>

<liferay-portlet:actionURL var="doStatisticsURL" name="doStatistics"/>

<aui:form name="fm" action="<%=doStatisticsURL %>" method="post">
	<aui:input name="<%=StatisticsUtil.STATISTICS_BY %>" value="<%=StatisticsUtil.MONTH %>" type="hidden"/>
	<aui:input name="month" value="<%=currentMonth %>" type="hidden"/>
	<aui:input name="year" value="<%=currentYear %>" type="hidden"/>
	<aui:input name="groupId" value="<%=scopeGroupId %>" type="hidden"/>
	
	<aui:fieldset label="statistics-dossiers">
		<aui:button name="statsInCurrentMonth" value="stats-in-current-month" type="button" />
		<aui:button name="statsInCurrentYear" value="stats-in-current-year" type="button"/>
	</aui:fieldset>
</aui:form>



<aui:script>
	AUI().ready(function(A){
		
		var statsInCurrentMonth = A.one('#<portlet:namespace/>statsInCurrentMonth');
		var statsInCurrentYear = A.one('#<portlet:namespace/>statsInCurrentYear');
		var statisticBy = A.one('#<portlet:namespace/>statisticsBy');
		
		if(statsInCurrentMonth){
			statsInCurrentMonth.on('click', function(){
				statisticBy.val('month'); 
				submitForm(document.<portlet:namespace />fm);
			});
		}
		
		if(statsInCurrentYear){
			statsInCurrentYear.on('click', function(){
				statisticBy.val('year'); 
				submitForm(document.<portlet:namespace />fm);
			});
		}
	});
</aui:script>
