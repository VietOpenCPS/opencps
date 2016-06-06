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
package org.opencps.jasperreport.util;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;

import org.opencps.jasperreport.compile.JRReportTemplate;
import org.opencps.jasperreport.datasource.JRJSONDataSource;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;

/**
 * @author trungnt
 */
public class JRReportUtil {

	public synchronized static String createReportPDFfFile(
	    String jrxmlTemplate, String jsonData, Map<String, Object> parameters,
	    String outputDestination, String exportName) {

		String sourceFileName = outputDestination + exportName;
		try {
			JasperReport reportTemplate = JRReportTemplate
			    .getJasperReport(jrxmlTemplate);
			JRJSONDataSource dataSource = JRJSONDataSource
			    .getInstance(jsonData);
			
			JasperPrint jasperPrint =
			    getJasperPrint(reportTemplate, parameters, dataSource);

			return exportPdfFile(jasperPrint, sourceFileName);
		}
		catch (Exception e) {
			_log
			    .error(e);

			return StringPool.BLANK;

		}
	}
	
	
	/**
	 * @param response
	 * @param writer
	 * @param jrxmlTemplate
	 * @param jsonData
	 * @param parameters
	 * @SuppressWarnings("rawtypes")
        Exporter exporter;
        switch (format) {
        case PDF:
            exporter = new JRPdfExporter();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            break;
        case CSV:
            exporter = new JRCsvExporter();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            break;
        case XLSX:
            exporter = new JRXlsxExporter();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            break;
        case HTML:
            //The HtmlExport can not be configured with SimpleOutputStreamExporterOutput
            exporter = new HtmlExporter();
            exporter.setExporterOutput(new SimpleHtmlExporterOutput(out));
            break;
        default:
            throw new ReportException("Unknown export format");
        }
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.exportReport();
	 */
	public synchronized static void renderReportHTMLStream(
	    HttpServletResponse response, PrintWriter writer, String jrxmlTemplate,
	    String jsonData, Map<String, Object> parameters) {

		try {
			JasperReport reportTemplate = JRReportTemplate
			    .getJasperReport(jrxmlTemplate);
			JRJSONDataSource dataSource = JRJSONDataSource
			    .getInstance(jsonData);
			JasperPrint jasperPrint =
			    getJasperPrint(reportTemplate, parameters, dataSource);

			HtmlExporter exporter = new HtmlExporter();

			SimpleHtmlExporterOutput exporterOutput =
			    new SimpleHtmlExporterOutput(writer);
			SimpleExporterInput exporterInput =
			    new SimpleExporterInput(jasperPrint);
			exporter
			    .setExporterInput(exporterInput);

			exporter
			    .setExporterOutput(exporterOutput);

			exporter
			    .exportReport();
		
		}
		catch (Exception e) {
			_log
			    .error(e);

		}
	}


	protected static JasperPrint getJasperPrint(
	    JasperReport jrReportTemplate, Map<String, Object> parameters,
	    JRJSONDataSource dataSource)
	    throws JRException {

		JasperPrint jasperPrint = JasperFillManager
		    .fillReport(jrReportTemplate, null, dataSource);

		return jasperPrint;
	}

	protected static JasperPrint getJasperPrint(
	    JRReportTemplate jrReportTemplate, Map<String, Object> parameters,
	    JRJSONDataSource dataSource)
	    throws JRException {

		JasperPrint jasperPrint = JasperFillManager
		    .fillReport(jrReportTemplate, null, dataSource);

		return jasperPrint;
	}

	protected static String exportPdfFile(
	    JasperPrint jasperPrint, String sourceFileName)
	    throws JRException {

		JasperExportManager
		    .exportReportToPdfFile(jasperPrint, sourceFileName);

		return sourceFileName;

	}

	private static Log _log = LogFactoryUtil
	    .getLog(JRReportUtil.class
	        .getName());
}
