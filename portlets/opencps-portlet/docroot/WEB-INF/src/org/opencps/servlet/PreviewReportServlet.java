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

package org.opencps.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.service.DossierFileLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierPartLocalServiceUtil;
import org.opencps.jasperreport.util.JRReportUtil;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * @author trungnt
 */
public class PreviewReportServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected synchronized void doGet(
	    HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

		long dossierFileId = ParamUtil
		    .getLong(request, "dossierFileId");
		PrintWriter writer = response
		    .getWriter();

		try {
			// Get dossier file
			DossierFile dossierFile = DossierFileLocalServiceUtil
			    .getDossierFile(dossierFileId);

			// Get dossier part
			DossierPart dossierPart = DossierPartLocalServiceUtil
			    .getDossierPart(dossierFile
			        .getDossierPartId());

			String formData = dossierFile
			    .getFormData();
			String jrxmlTemplate = dossierPart
			    .getFormReport();

			// Validate json string

			JSONObject jsonObject = JSONFactoryUtil
						    .createJSONObject(formData);
			
			/*JSONObject dataSource = JSONFactoryUtil
						    .createJSONObject();
			
			dataSource.put("opencps", jsonObject);*/

			JRReportUtil
			    .renderReportHTMLStream(
			        response, writer, jrxmlTemplate, jsonObject.toString(), null);

		}
		catch (Exception e) {
			writer.write("<div class=\"portlet-msg-alert\">" + LanguageUtil.get(request.getLocale(), "error-while-preview-report") + "</div>");
		}finally {
			if(writer != null){
				writer.println();
				writer.flush();
				writer.close();
			}
		}
	}

	@Override
	public void destroy() {

		// TODO Auto-generated method stub
		super.destroy();
	}
}
