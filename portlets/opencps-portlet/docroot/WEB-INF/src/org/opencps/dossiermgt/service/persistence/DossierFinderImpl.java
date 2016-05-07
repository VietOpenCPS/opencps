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

package org.opencps.dossiermgt.service.persistence;

import java.util.Iterator;
import java.util.List;

import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.impl.DossierImpl;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

/**
 * @author trungnt
 */
public class DossierFinderImpl extends BasePersistenceImpl<Dossier>
    implements DossierFinder {

	public int countDossier(long groupId, String keyword, int dossierStatus) {

		String[] keywords = null;

		boolean andOperator = false;

		if (Validator
		    .isNotNull(keyword)) {
			keywords = CustomSQLUtil
			    .keywords(keyword);
		}
		else {
			andOperator = true;
		}

		return countDossier(groupId, keywords, dossierStatus, andOperator);
	}

	private int countDossier(
	    long groupId, String[] keywords, int dossierStatus,
	    boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
			    .get(COUNT_DOSSIER);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
				    .replaceKeywords(
				        sql, "lower(opencps_serviceinfo.serviceName)",
				        StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
				    .replaceKeywords(
				        sql, "lower(opencps_service_config.govAgencyName)",
				        StringPool.LIKE, true, keywords);
			}

			

			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
				    .replace(
				        sql,
				        "INNER JOIN opencps_serviceinfo ON opencps_dossier.serviceInfoId = opencps_serviceinfo.serviceInfoId",
				        StringPool.BLANK);
				sql = StringUtil
				    .replace(
				        sql,
				        "INNER JOIN opencps_service_config ON opencps_dossier.serviceConfigId = opencps_service_config.serviceConfigId",
				        StringPool.BLANK);

				sql = StringUtil
				    .replace(
				        sql,
				        "AND (lower(opencps_serviceinfo.serviceName) LIKE ? [$AND_OR_NULL_CHECK$])",
				        StringPool.BLANK);

				sql = StringUtil
				    .replace(
				        sql,
				        "OR (lower(opencps_service_config.govAgencyName) LIKE ? [$AND_OR_NULL_CHECK$])",
				        StringPool.BLANK);
			}

			if (dossierStatus < 0) {
				sql = StringUtil
				    .replace(
				        sql, "AND (opencps_dossier.dossierStatus = ?)",
				        StringPool.BLANK);
			}

			SQLQuery q = session
			    .createSQLQuery(sql);
			
			sql = CustomSQLUtil
						    .replaceAndOperator(sql, andOperator);

			q
			    .addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos
			    .getInstance(q);

			qPos
			    .add(groupId);

			if (keywords != null && keywords.length > 0) {
				qPos
				    .add(keywords, 2);
				qPos
				    .add(keywords, 2);
			}

			if (dossierStatus >= 0) {
				qPos
				    .add(dossierStatus);
			}

			Iterator<Integer> itr = q
			    .iterate();

			if (itr
			    .hasNext()) {
				Integer count = itr
				    .next();

				if (count != null) {
					return count
					    .intValue();
				}
			}

			return 0;

		}
		catch (Exception e) {
			_log
			    .error(e);
		}
		finally {
			closeSession(session);
		}

		return 0;
	}

	public List<Dossier> searchDossier(
	    long groupId, String keyword, int dossierStatus, int start, int end,
	    OrderByComparator obc) {

		String[] keywords = null;
		boolean andOperator = false;
		if (Validator
		    .isNotNull(keyword)) {
			keywords = CustomSQLUtil
			    .keywords(keyword);
		}
		else {
			andOperator = true;
		}
		return searchDossier(
		    groupId, keywords, dossierStatus, andOperator, start, end, obc);
	}

	private List<Dossier> searchDossier(
	    long groupId, String[] keywords, int dossierStatus, boolean andOperator,
	    int start, int end, OrderByComparator obc) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
			    .get(SEARCH_DOSSIER);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
				    .replaceKeywords(
				        sql, "lower(opencps_serviceinfo.serviceName)",
				        StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
				    .replaceKeywords(
				        sql, "lower(opencps_service_config.govAgencyName)",
				        StringPool.LIKE, true, keywords);
			}

			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
				    .replace(
				        sql,
				        "INNER JOIN opencps_serviceinfo ON opencps_dossier.serviceInfoId = opencps_serviceinfo.serviceInfoId",
				        StringPool.BLANK);
				sql = StringUtil
				    .replace(
				        sql,
				        "INNER JOIN opencps_service_config ON opencps_dossier.serviceConfigId = opencps_service_config.serviceConfigId",
				        StringPool.BLANK);

				sql = StringUtil
				    .replace(
				        sql,
				        "AND (lower(opencps_serviceinfo.serviceName) LIKE ? [$AND_OR_NULL_CHECK$])",
				        StringPool.BLANK);

				sql = StringUtil
				    .replace(
				        sql,
				        "OR (lower(opencps_service_config.govAgencyName) LIKE ? [$AND_OR_NULL_CHECK$])",
				        StringPool.BLANK);
			}

			if (dossierStatus < 0) {
				sql = StringUtil
				    .replace(
				        sql, "AND (opencps_dossier.dossierStatus = ?)",
				        StringPool.BLANK);
			}
			
			sql = CustomSQLUtil
						    .replaceAndOperator(sql, andOperator);

			SQLQuery q = session
			    .createSQLQuery(sql);

			q
			    .addEntity("Dossier", DossierImpl.class);

			QueryPos qPos = QueryPos
			    .getInstance(q);

			qPos
			    .add(groupId);

			if (keywords != null && keywords.length > 0) {
				qPos
				    .add(keywords, 2);
				qPos
				    .add(keywords, 2);
			}

			if (dossierStatus >= 0) {
				qPos
				    .add(dossierStatus);
			}

			return (List<Dossier>) QueryUtil
			    .list(q, getDialect(), start, end);
		}
		catch (Exception e) {
			_log
			    .error(e);
		}
		finally {
			closeSession(session);
		}

		return null;
	}

	public static final String SEARCH_DOSSIER = DossierFinder.class
	    .getName() + ".searchDossier";
	public static final String COUNT_DOSSIER = DossierFinder.class
	    .getName() + ".countDossier";

	private Log _log = LogFactoryUtil
	    .getLog(DossierFinder.class
	        .getName());
}
