
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
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.search.DictItemDisplayTerms"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="java.util.List"%>
<%@ include file="../init.jsp"%>

<%
	long dictCollectionId = ParamUtil.getLong(request, DictItemDisplayTerms.DICTCOLLECTION_ID);
	long dictItemId = ParamUtil.getLong(request, DictItemDisplayTerms.DICTITEM_ID);
	
	DictItem curDictItem = null;
	List<DictItem> dictItems = new ArrayList<DictItem>();

	try{
		if(dictItemId > 0){
			curDictItem = DictItemLocalServiceUtil.getDictItem(dictItemId);
		}
	}catch(Exception e){
		_log.error(e);
	} 
	
	try{
		if(dictCollectionId > 0){
			dictItems = DictItemLocalServiceUtil.getDictItemsByDictCollectionId(dictCollectionId);
		}
	}catch(Exception e){
		_log.error(e);
	} 
%>

<aui:select name="<%=DictItemDisplayTerms.PARENTITEM_ID %>" label="parent-item">
	<aui:option value="0"></aui:option>
	<%
		if(dictItems != null){
			for(DictItem dictItem : dictItems){
				if((curDictItem != null && dictItem.getDictItemId() == curDictItem.getDictItemId())||
						(curDictItem != null && dictItem.getTreeIndex().contains(curDictItem.getDictItemId() + StringPool.PERIOD))){
					continue;
				}
				
				int level = StringUtil.count(dictItem.getTreeIndex(), StringPool.PERIOD);
				String index = "|";
				for(int i = 0; i < level; i++){
					index += "_";
				}
				%>
					<aui:option value="<%=dictItem.getDictItemId() %>"><%=index + dictItem.getItemName(locale) %></aui:option>
				<%
			}
		}
	%>
</aui:select>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.data_management.admin.select_dictitems.jsp");
%>
