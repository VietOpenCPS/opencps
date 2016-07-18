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

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.opencps.dossiermgt.model.DossierLog;
import org.opencps.dossiermgt.model.impl.DossierLogImpl;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.CalendarUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

/**
 * @author trungnt
 */
public class DossierLogFinderImpl extends BasePersistenceImpl<DossierLog>
	implements DossierLogFinder {

	public static final String COUNT_ADMIN_LOG =
		DossierLogFinder.class.getName() + ".countAdminLog";

	public static final String FIND_REQUIRED_PROCESS_DOSSIER =
		DossierLogFinder.class.getName() + ".findRequiredProcessDossier";

	public static final String SEARCH_ADMIN_LOG =
		DossierLogFinder.class.getName() + ".searchAdminLog";

	private Log _log = LogFactoryUtil.getLog(DossierLogFinder.class.getName());

	/**
	 * @param fromUpdateDatetime
	 * @param toUpdateDatetime
	 * @param level
	 * @param dossierStatus
	 * @return
	 */
	public int countAdminLog(
		Date fromUpdateDatetime, Date toUpdateDatetime, int level,
		String dossierStatus) {

		boolean andOperator = false;

		return countAdminLog(fromUpdateDatetime, toUpdateDatetime, level,
			dossierStatus, andOperator);
	}

	// count log for admin
	/**
	 * @param fromUpdateDatetime
	 * @param toUpdateDatetime
	 * @param level
	 * @param dossierStatus
	 * @param andOperator
	 * @return
	 */
	private int countAdminLog(
		Date fromUpdateDatetime, Date toUpdateDatetime, int level,
		String dossierStatus, boolean andOperator) {

		Timestamp fromUpdateTime_TS =
			CalendarUtil.getTimestamp(fromUpdateDatetime);
		Timestamp toUpdateTime_TS = CalendarUtil.getTimestamp(toUpdateDatetime);
		Session session = null;
		try {
			session = openSession();
			String sql = CustomSQLUtil.get(COUNT_ADMIN_LOG);
			if (Validator.isNull(dossierStatus)) {
				sql =
					StringUtil.replace(sql,
						"AND opencps_dossier_log.dossierStatus = ?",
						StringPool.BLANK);
			}

			if (Validator.isNull(fromUpdateDatetime) ||
				Validator.isNull(toUpdateDatetime)) {
				sql =
					StringUtil.replace(
						sql,
						"AND opencps_dossier_log.updateDatetime BETWEEN  ? AND ?",
						StringPool.BLANK);
			}

			if (level == -1) {
				sql =
					StringUtil.replace(sql,
						"AND opencps_dossier_log.level = ?", StringPool.BLANK);
			}

			sql = CustomSQLUtil.replaceAndOperator(sql, andOperator);

			SQLQuery q = session.createSQLQuery(sql);

			q.setCacheable(false);
			q.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos.getInstance(q);

			if (level != -1) {
				qPos.add(level);
			}

			if (Validator.isNotNull(fromUpdateDatetime) &&
				Validator.isNotNull(fromUpdateDatetime)) {
				qPos.add(fromUpdateTime_TS);
				qPos.add(toUpdateTime_TS);
			}

			if (Validator.isNotNull(dossierStatus)) {
				qPos.add(dossierStatus);
			}
			Iterator<Integer> itr = q.iterate();
			if (itr.hasNext()) {
				Integer count = itr.next();

				if (count != null) {
					return count.intValue();
				}
			}

			return 0;

		}
		catch (Exception e) {
			_log.error(e);
		}
		finally {
			closeSession(session);
		}
		return 0;
	}

	/**
	 * @param dossierId
	 * @param actors
	 * @param requestCommands
	 * @return
	 */
	public List<DossierLog> findRequiredProcessDossier(
		long dossierId, String[] actors, String[] requestCommands) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(FIND_REQUIRED_PROCESS_DOSSIER);

			if (actors == null || actors.length == 0) {
				sql =
					StringUtil.replace(sql,
						"AND opencps_dossier_log.actor IN (?)",
						StringPool.BLANK);

			}
			else {
				sql =
					StringUtil.replace(
						sql,
						"AND opencps_dossier_log.actor IN (?)",
						"AND opencps_dossier_log.actor IN (" +
							StringUtil.merge(actors) + ")");
			}

			if (requestCommands == null || requestCommands.length == 0) {
				sql =
					StringUtil.replace(sql,
						"AND opencps_dossier_log.requestCommand IN (?)",
						StringPool.BLANK);

			}
			else {
				sql =
					StringUtil.replace(sql,
						"AND opencps_dossier_log.requestCommand IN (?)",
						"AND opencps_dossier_log.requestCommand IN (" +
							StringUtil.merge(requestCommands) + ")");
			}

			SQLQuery q = session.createSQLQuery(sql);

			q.addEntity("DossierLog", DossierLogImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(dossierId);

			return (List<DossierLog>) QueryUtil.list(q, getDialect(),
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		}
		catch (Exception e) {
			_log.error(e);
		}
		finally {
			closeSession(session);
		}

		return null;
	}

	/**
	 * @param fromUpdateDatetime
	 * @param toUpdateDatetime
	 * @param level
	 * @param dossierStatus
	 * @param start
	 * @param end
	 * @return
	 */
	public List<DossierLog> searchAdminLog(
		Date fromUpdateDatetime, Date toUpdateDatetime, int level,
		String dossierStatus, int start, int end) {

		boolean andOperator = false;

		return searchAdminLog(fromUpdateDatetime, toUpdateDatetime, level,
			dossierStatus, start, end, andOperator);

	}

	// search Log for Admin
	/**
	 * @param fromUpdateDatetime
	 * @param toUpdateDatetime
	 * @param level
	 * @param dossierStatus
	 * @param start
	 * @param end
	 * @param andOperator
	 * @return
	 */
	private List<DossierLog> searchAdminLog(
		Date fromUpdateDatetime, Date toUpdateDatetime, int level,
		String dossierStatus, int start, int end, boolean andOperator) {

		Session session = null;
		Timestamp fromUpdateTime_TS =
			CalendarUtil.getTimestamp(fromUpdateDatetime);
		Timestamp toUpdateTime_TS = CalendarUtil.getTimestamp(toUpdateDatetime);
		try {

			session = openSession();
			String sql = CustomSQLUtil.get(SEARCH_ADMIN_LOG);
			if (Validator.isNull(dossierStatus)) {
				sql =
					StringUtil.replace(sql,
						"AND opencps_dossier_log.dossierStatus = ?",
						StringPool.BLANK);
			}

			if (Validator.isNull(fromUpdateDatetime) ||
				Validator.isNull(toUpdateDatetime)) {
				sql =
					StringUtil.replace(
						sql,
						"AND opencps_dossier_log.updateDatetime BETWEEN  ? AND ?",
						StringPool.BLANK);
			}

			if (level == -1) {
				sql =
					StringUtil.replace(sql,
						"AND opencps_dossier_log.level = ?", StringPool.BLANK);
			}

			sql = CustomSQLUtil.replaceAndOperator(sql, andOperator);

			SQLQuery q = session.createSQLQuery(sql);
			q.setCacheable(false);
			q.addEntity("DossierLog", DossierLogImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			if (level != -1) {
				qPos.add(level);
			}

			if (Validator.isNotNull(fromUpdateDatetime) &&
				Validator.isNotNull(fromUpdateDatetime)) {
				qPos.add(fromUpdateTime_TS);
				qPos.add(toUpdateTime_TS);
			}

			if (Validator.isNotNull(dossierStatus)) {
				qPos.add(dossierStatus);
			}
			return (List<DossierLog>) QueryUtil.list(q, getDialect(), start,
				end);
		}
		catch (Exception e) {
			_log.error(e);
		}
		finally {
			closeSession(session);
		}

		return null;
	}
}
