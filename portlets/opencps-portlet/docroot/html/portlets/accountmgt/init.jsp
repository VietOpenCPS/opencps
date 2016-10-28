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

<%@ include file="/init.jsp" %>

<%  
	String businessRegStep_cfg = GetterUtil.getString(portletPreferences.getValue("businessRegStep", "3"));
	String citizenRegStep_cfg = GetterUtil.getString(portletPreferences.getValue("citizenRegStep", "3"));
	String emailConfirmToAdmin = GetterUtil.getString(portletPreferences.getValue("emailConfirmToAdmin", ""));

	boolean showLabelTaglibDatamgt = GetterUtil.getBoolean(portletPreferences.getValue("showLabelTaglibDatamgt", "false"), false);
	
	String messageSuccessfullRegistration = GetterUtil.getString(portletPreferences.getValue("messageSuccessfullRegistration", StringPool.BLANK));
%>