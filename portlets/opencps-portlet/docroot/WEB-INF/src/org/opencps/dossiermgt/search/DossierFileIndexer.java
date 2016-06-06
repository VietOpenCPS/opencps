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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javax.portlet.PortletURL;

import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.permissions.DossierFilePermission;
import org.opencps.dossiermgt.service.DossierFileLocalServiceUtil;
import org.opencps.dossiermgt.service.persistence.DossierFileActionableDynamicQuery;
import org.opencps.dossiermgt.util.PortletKeys;
import org.opencps.util.ActionKeys;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.search.BaseIndexer;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchEngineUtil;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.security.permission.PermissionChecker;

/**
 * @author trungdk
 */
public class DossierFileIndexer extends BaseIndexer {
	public static final String[] CLASS_NAMES = { DossierFile.class.getName() };

	public static final String PORTLET_ID = PortletKeys.PORTLET_19;

	@Override
	public String[] getClassNames() {
		// TODO Auto-generated method stub
		return CLASS_NAMES;
	}

	@Override
	public String getPortletId() {
		// TODO Auto-generated method stub
		return PORTLET_ID;
	}

	@Override
	public boolean hasPermission(PermissionChecker permissionChecker,
			String dossierFileClassName, long dossierFileClassPK,
			String actionId) throws Exception {

		return DossierFilePermission.contains(permissionChecker,
				dossierFileClassPK, ActionKeys.VIEW);
	}

	@Override
	protected void doDelete(Object obj) throws Exception {
		// TODO Auto-generated method stub
		DossierFile dossierFile = (DossierFile) obj;

		deleteDocument(dossierFile.getCompanyId(), dossierFile.getDossierId());
	}

	@Override
	protected Document doGetDocument(Object obj) throws Exception {
		// TODO Auto-generated method stub
		DossierFile dossierFile = (DossierFile) obj;

		Document document = getBaseModelDocument(PORTLET_ID, dossierFile);

		Field field = new Field(DossierFileDisplayTerms.DISPLAY_NAME,
				dossierFile.getDisplayName());
		field.setBoost(5);
		document.add(field);

		document.addDate(Field.MODIFIED_DATE, dossierFile.getModifiedDate());
		document.addText(DossierFileDisplayTerms.FORM_DATA,
				dossierFile.getFormData());
		document.addText(DossierFileDisplayTerms.DOSSIER_FILE_NO,
				dossierFile.getDossierFileNo());
		document.addNumber(DossierFileDisplayTerms.DOSSIER_FILE_ID, dossierFile.getDossierFileId());
		document.addKeyword(Field.GROUP_ID,
				getSiteGroupId(dossierFile.getGroupId()));
		document.addKeyword(Field.SCOPE_GROUP_ID, dossierFile.getGroupId());

		return document;
	}

	@Override
	protected Summary doGetSummary(Document document, Locale locale,
			String snippet, PortletURL portletURL) throws Exception {
		// TODO Auto-generated method stub
		Summary summary = createSummary(document);

		summary.setMaxContentLength(200);

		return summary;
	}

	@Override
	protected void doReindex(Object obj) throws Exception {
		// TODO Auto-generated method stub
		DossierFile dossierFile = (DossierFile) obj;

		Document document = getDocument(dossierFile);

		SearchEngineUtil.updateDocument(getSearchEngineId(),
				dossierFile.getCompanyId(), document);

	}

	@Override
	protected void doReindex(String[] ids) throws Exception {
		// TODO Auto-generated method stub
		long companyId = GetterUtil.getLong(ids[0]);

		reindexEntries(companyId);

	}

	protected void reindexEntries(long companyId) throws PortalException,
			SystemException {

		final Collection<Document> documents = new ArrayList<Document>();

		ActionableDynamicQuery actionableDynamicQuery = new DossierFileActionableDynamicQuery() {
			
			@Override
			protected void addCriteria(DynamicQuery dynamicQuery) {
			}

			@Override
			protected void performAction(Object object) throws PortalException {
				DossierFile dossierFile = (DossierFile) object;

				Document document = getDocument(dossierFile);

				documents.add(document);
			}

		};

		actionableDynamicQuery.setCompanyId(companyId);

		actionableDynamicQuery.performActions();

		SearchEngineUtil.updateDocuments(getSearchEngineId(), companyId,
				documents);
	}

	@Override
	protected void doReindex(String className, long classPK) throws Exception {
		// TODO Auto-generated method stub
        DossierFile dossierFile = DossierFileLocalServiceUtil.getDossierFile(classPK);

        doReindex(dossierFile);

	}

	@Override
	protected String getPortletId(SearchContext searchContext) {
		// TODO Auto-generated method stub
		return PORTLET_ID;
	}

}
