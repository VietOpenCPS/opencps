/**
* OpenCPS is the open source Core Public Services software
* Copyright (C) 2016-present OpenCPS community

* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Affero General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Affero General Public License for more details.
* You should have received a copy of the GNU Affero General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>
*/

package org.opencps.dossiermgt.search;

import java.util.Date;

import javax.portlet.PortletRequest;

import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * @author trungnt
 */
public class DossierFileDisplayTerms extends DisplayTerms {

	public static final String DISPAY_NAME = "dispayName";
	public static final String DOSSIER_FILE_NO = "dossierFileNo";
	public static final String DOSSIER_FILE_DATE = "dossierFileDate";
	public static final String DOSSIER_FILE_UPLOAD = "dossierFileUpload";
	public static final String DOSSIER_FILE_DATE_DAY = "dossierFileDateDay";
	public static final String DOSSIER_FILE_DATE_MONTH = "dossierFileDateMonth";
	public static final String DOSSIER_FILE_DATE_YEAR = "dossierFileDateYear";
	
	public static final String FILE_ENTRY_ID = "fileEntryId";
	public static final String FOLDE_ID = "folderId";
	public static final String DOSSIER_PART_ID = "dossierPartId";
	public static final String PART_NAME = "partName";
	
	public static final String SOURCE_FILE_NAME = "sourceFileName";
	public static final String FILE_TITLE = "fileTitle";
	public static final String MSG = "msg";
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String TEMP_FOLDER_NAME = "tempFolderName";

	public DossierFileDisplayTerms(PortletRequest portletRequest) {
		super(
		    portletRequest);

		dossierFileDate = ParamUtil
		    .getDate(portletRequest, DOSSIER_FILE_DATE, DateTimeUtil
		        .getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));

		dispayName = ParamUtil
		    .getString(portletRequest, DISPAY_NAME);

		dossierFileNo = ParamUtil
		    .getString(portletRequest, DOSSIER_FILE_NO);

		
	}
	
	public String getDispayName() {
	
		return dispayName;
	}
	
	public void setDispayName(String dispayName) {
	
		this.dispayName = dispayName;
	}
	
	public String getDossierFileNo() {
	
		return dossierFileNo;
	}
	
	public void setDossierFileNo(String dossierFileNo) {
	
		this.dossierFileNo = dossierFileNo;
	}
	
	public Date getDossierFileDate() {
	
		return dossierFileDate;
	}
	
	public void setDossierFileDate(Date dossierFileDate) {
	
		this.dossierFileDate = dossierFileDate;
	}



	protected String dispayName;
	protected String dossierFileNo;
	protected Date dossierFileDate;
}
