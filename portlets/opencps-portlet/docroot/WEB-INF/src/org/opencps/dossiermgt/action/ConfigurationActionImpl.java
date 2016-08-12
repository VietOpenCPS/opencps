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

package org.opencps.dossiermgt.action;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.service.DossierFileLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;

import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

public class ConfigurationActionImpl implements ConfigurationAction {

	@Override
	public void processAction(PortletConfig portletConfig,
			ActionRequest actionRequest, ActionResponse actionResponse)

	throws Exception {
		String action = ParamUtil.getString(actionRequest, "action");
		if (action.equals("save")) {
			String portletResource = ParamUtil.getString(actionRequest,
					"portletResource");
			String servicepage = ParamUtil.getString(actionRequest,
					"servicepage", "");
			String dossierpage = ParamUtil.getString(actionRequest,
					"dossierpage", "");
			String dossierfilepage = ParamUtil.getString(actionRequest,
					"dossierfilepage", "");
			PortletPreferences prefs = PortletPreferencesFactoryUtil
					.getPortletSetup(actionRequest, portletResource);

			prefs.setValue("servicepage", servicepage);
			
			prefs.setValue("dossierpage", dossierpage);
			
			prefs.setValue("dossierfilepage", dossierfilepage);

			prefs.store();

			SessionMessages.add(actionRequest, "config-stored");

			SessionMessages.add(actionRequest, portletConfig.getPortletName()
					+ SessionMessages.KEY_SUFFIX_REFRESH_PORTLET,
					portletResource);
		} else if (action.equals("reindexdossier")) {
			int end = DossierLocalServiceUtil.getDossiersCount();
			List<Dossier> dossiers = DossierLocalServiceUtil.getDossiers(0, end);
			Indexer indexer = IndexerRegistryUtil.nullSafeGetIndexer(
	                Dossier.class);
	
			for (Dossier ds : dossiers) {
				try {
					indexer.reindex(ds);
				}
				catch (SearchException e) {
					SessionErrors.add(actionRequest, "dossier-reindex-error");					
				}
			}
			SessionMessages.add(actionRequest, "dossier-reindex");
		} else if (action.equals("reindexdossierfile")) {
			int end = DossierFileLocalServiceUtil.getDossierFilesCount();
			List<DossierFile> dossierFiles = DossierFileLocalServiceUtil.getDossierFiles(0, end);
			Indexer indexer = IndexerRegistryUtil.nullSafeGetIndexer(
	                DossierFile.class);
	
			for (DossierFile df : dossierFiles) {
				try {
					indexer.reindex(df);
				}
				catch (SearchException e) {
					SessionErrors.add(actionRequest, "dossierfile-reindex-error");					
				}
			}
			SessionMessages.add(actionRequest, "dossierfile-reindex");
		}
	}

	@Override
	public String render(PortletConfig portletConfig,
			RenderRequest renderRequest, RenderResponse renderResponse)
			throws Exception {
		return "/html/portlets/dossiermgt/monitoring/configuration.jsp";
	}
}
