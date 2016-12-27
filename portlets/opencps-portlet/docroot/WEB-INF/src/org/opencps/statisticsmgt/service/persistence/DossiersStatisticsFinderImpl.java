/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package org.opencps.statisticsmgt.service.persistence;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.statisticsmgt.bean.DossierStatisticsBean;
import org.opencps.statisticsmgt.model.DossiersStatistics;
import org.opencps.statisticsmgt.util.StatisticsUtil;
import org.opencps.statisticsmgt.util.StatisticsUtil.StatisticsFieldNumber;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

/**
 * The implementation of the dossiers statistics local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link org.opencps.statisticsmgt.service.DossiersStatisticsLocalService}
 * interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author trungnt
 * @see org.opencps.statisticsmgt.service.base.DossiersStatisticsLocalServiceBaseImpl
 * @see org.opencps.statisticsmgt.service.DossiersStatisticsLocalServiceUtil
 */
public class DossiersStatisticsFinderImpl extends
		BasePersistenceImpl<DossiersStatistics> implements
		DossiersStatisticsFinder {
	/*
	 * NOTE FOR DEVELOPERS:
	 * 
	 * Never reference this interface directly. Always use {@link
	 * org.opencps.statisticsmgt.service.DossiersStatisticsLocalServiceUtil} to
	 * access the dossiers statistics local service.
	 */

	private static final String SQL_STATISTICS_COLUMN_NAMES_0 = DossiersStatisticsFinder.class
			.getName() + ".[COLUMN-NAMES-0]";

	private static final String SQL_STATISTICS_DATA_TYPES_0 = DossiersStatisticsFinder.class
			.getName() + ".[DATA-TYPES-0]";

	private static final String SQL_STATISTICS_COLUMN_NAMES_1 = DossiersStatisticsFinder.class
			.getName() + ".[COLUMN-NAMES-1]";

	private static final String SQL_STATISTICS_DATA_TYPES_1 = DossiersStatisticsFinder.class
			.getName() + ".[DATA-TYPES-1]";

	private static final String SQL_STATISTICS_COLUMN_NAMES_2 = DossiersStatisticsFinder.class
			.getName() + ".[COLUMN-NAMES-2]";

	private static final String SQL_STATISTICS_DATA_TYPES_2 = DossiersStatisticsFinder.class
			.getName() + ".[DATA-TYPES-2]";

	private static final String SQL_GENERAL_STATISTICS = DossiersStatisticsFinder.class
			.getName() + ".generalStatistics";

	private static final String SQL_STATISTICS_BY_DOMAIN = DossiersStatisticsFinder.class
			.getName() + ".statisticsByDomain";

	private static final String SQL_STATISTICS_BY_GOVAGENCY = DossiersStatisticsFinder.class
			.getName() + ".statisticsByGovagency";

	private static final String SQL_STATISTICS_MONTHS = DossiersStatisticsFinder.class
			.getName() + ".getMonths";

	private static final String SQL_GET_STATS_BY_GOV_DOMAIN = DossiersStatisticsFinder.class
			.getName() + ".getStatsByGovAndDomain";

	/**
	 * @param groupId
	 * @param month
	 * @param year
	 * @param field
	 * @param delayStatus
	 * @return
	 */
	public List generalStatistics(long groupId, int month, int year,
			String field, int delayStatus) {
		Session session = null;
		try {
			session = openSession();

			String sql = CustomSQLUtil.get(SQL_GENERAL_STATISTICS);

			String definedColumnNames = CustomSQLUtil
					.get(SQL_STATISTICS_COLUMN_NAMES_0);

			String definedCondition = StatisticsUtil.getFilterCondition(field,
					delayStatus);

			sql = StringUtil.replace(sql, "$COLUMNS$", definedColumnNames);

			sql = StringUtil.replace(sql, "$FILTER$", definedCondition);

			// _log.info(sql);

			String definedColumnDataTypes = CustomSQLUtil
					.get(SQL_STATISTICS_DATA_TYPES_0);

			String[] columnNames = StringUtil.split(definedColumnNames);

			String[] columnDataTypes = StringUtil.split(definedColumnDataTypes);

			SQLQuery q = session.createSQLQuery(sql);

			q = StatisticsUtil.bindingProperties(q, columnDataTypes, false);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);

			if (delayStatus >= 0) {
				qPos.add(delayStatus);
			}

			if (!field
					.equals(StatisticsFieldNumber.ProcessingNumber.toString())
					&& !field.equals(StatisticsFieldNumber.DelayingNumber
							.toString())) {
				qPos.add(month);

				qPos.add(year);
			}

			Iterator<Integer> itr = q.iterate();

			List<DossierStatisticsBean> statisticsBeans = new ArrayList<DossierStatisticsBean>();

			if (itr.hasNext()) {
				Integer count = itr.next();

				DossierStatisticsBean statisticsBean = new DossierStatisticsBean();

				statisticsBean.setMonth(month);

				statisticsBean.setGroupId(groupId);

				statisticsBean.setYear(year);
				String columnName = columnNames[0];
				String coulmnDataType = columnDataTypes[0];
				Method method = StatisticsUtil.getMethod(columnName,
						coulmnDataType, field);
				if (method != null) {
					method.invoke(statisticsBean, count.intValue());
				}
				statisticsBean.setAdministrationLevel(0);

				statisticsBeans.add(statisticsBean);
			}

			return statisticsBeans;
		} catch (Exception e) {
			_log.error(e);
		} finally {
			closeSession(session);
		}
		return null;
	}

	/**
	 * @param groupId
	 * @param month
	 * @param year
	 * @param option
	 * @param delayStatus
	 * @return
	 */
	public List statisticsByDomain(long groupId, int month, int year,
			String field, int delayStatus) {
		Session session = null;
		try {
			session = openSession();

			String sql = CustomSQLUtil.get(SQL_STATISTICS_BY_DOMAIN);

			String definedColumnNames = CustomSQLUtil
					.get(SQL_STATISTICS_COLUMN_NAMES_1);

			String definedCondition = StatisticsUtil.getFilterCondition(field,
					delayStatus);

			sql = StringUtil.replace(sql, "$COLUMNS$", definedColumnNames);

			sql = StringUtil.replace(sql, "$FILTER$", definedCondition);

			// _log.info(sql);

			String definedColumnDataTypes = CustomSQLUtil
					.get(SQL_STATISTICS_DATA_TYPES_1);

			String[] columnNames = StringUtil.split(definedColumnNames);

			String[] columnDataTypes = StringUtil.split(definedColumnDataTypes);

			SQLQuery q = session.createSQLQuery(sql);

			q = StatisticsUtil.bindingProperties(q, columnDataTypes, false);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);

			if (delayStatus >= 0) {
				qPos.add(delayStatus);
			}

			if (!field
					.equals(StatisticsFieldNumber.ProcessingNumber.toString())
					&& !field.equals(StatisticsFieldNumber.DelayingNumber
							.toString())) {
				qPos.add(month);

				qPos.add(year);
			}

			Iterator<Object[]> itr = (Iterator<Object[]>) QueryUtil.list(q,
					getDialect(), QueryUtil.ALL_POS, QueryUtil.ALL_POS)
					.iterator();

			List<DossierStatisticsBean> statisticsBeans = new ArrayList<DossierStatisticsBean>();

			if (itr.hasNext()) {
				while (itr.hasNext()) {
					DossierStatisticsBean statisticsBean = new DossierStatisticsBean();

					statisticsBean.setMonth(month);

					statisticsBean.setYear(year);

					statisticsBean.setGroupId(groupId);

					Object[] objects = itr.next();

					if (objects.length == columnDataTypes.length) {
						for (int i = 0; i < objects.length; i++) {
							String columnName = columnNames[i];
							String coulmnDataType = columnDataTypes[i];
							Method method = StatisticsUtil.getMethod(
									columnName, coulmnDataType, field);
							if (method != null) {
								method.invoke(statisticsBean, objects[i]);
							}

						}
					}

					statisticsBeans.add(statisticsBean);
				}
			}

			return statisticsBeans;
		} catch (Exception e) {
			_log.error(e);
		} finally {
			closeSession(session);
		}
		return null;
	}

	/**
	 * @param groupId
	 * @param month
	 * @param year
	 * @param option
	 * @param delayStatus
	 * @return
	 */
	public List statisticsByGovAgency(long groupId, int month, int year,
			String field, int delayStatus) {
		Session session = null;
		try {
			session = openSession();

			String sql = CustomSQLUtil.get(SQL_STATISTICS_BY_GOVAGENCY);

			String definedColumnNames = CustomSQLUtil
					.get(SQL_STATISTICS_COLUMN_NAMES_2);

			String definedCondition = StatisticsUtil.getFilterCondition(field,
					delayStatus);

			sql = StringUtil.replace(sql, "$COLUMNS$", definedColumnNames);

			sql = StringUtil.replace(sql, "$FILTER$", definedCondition);

			// _log.info(sql);

			String definedColumnDataTypes = CustomSQLUtil
					.get(SQL_STATISTICS_DATA_TYPES_2);

			String[] columnNames = StringUtil.split(definedColumnNames);

			String[] columnDataTypes = StringUtil.split(definedColumnDataTypes);

			SQLQuery q = session.createSQLQuery(sql);

			q = StatisticsUtil.bindingProperties(q, columnDataTypes, false);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);

			if (delayStatus >= 0) {
				qPos.add(delayStatus);
			}

			if (!field
					.equals(StatisticsFieldNumber.ProcessingNumber.toString())
					&& !field.equals(StatisticsFieldNumber.DelayingNumber
							.toString())) {
				qPos.add(month);

				qPos.add(year);
			}

			Iterator<Object[]> itr = (Iterator<Object[]>) QueryUtil.list(q,
					getDialect(), QueryUtil.ALL_POS, QueryUtil.ALL_POS)
					.iterator();

			List<DossierStatisticsBean> statisticsBeans = new ArrayList<DossierStatisticsBean>();

			if (itr.hasNext()) {
				while (itr.hasNext()) {
					DossierStatisticsBean statisticsBean = new DossierStatisticsBean();

					Object[] objects = itr.next();

					statisticsBean.setMonth(month);

					statisticsBean.setYear(year);

					statisticsBean.setGroupId(groupId);

					if (objects.length == columnDataTypes.length) {
						for (int i = 0; i < objects.length; i++) {
							String columnName = columnNames[i];
							String coulmnDataType = columnDataTypes[i];
							Method method = StatisticsUtil.getMethod(
									columnName, coulmnDataType, field);
							if (method != null) {
								method.invoke(statisticsBean, objects[i]);
							}

						}
					}
					statisticsBeans.add(statisticsBean);
				}
			}

			return statisticsBeans;
		} catch (Exception e) {
			_log.error(e);
		} finally {
			closeSession(session);
		}
		return null;
	}

	public List<Integer> getStatisticsMonths(long groupId, int year) {
		Session session = null;
		List<Integer> months = new ArrayList<Integer>();
		try {
			session = openSession();

			String sql = CustomSQLUtil.get(SQL_STATISTICS_MONTHS);

			_log.info(sql);

			SQLQuery q = session.createSQLQuery(sql);

			q.addScalar("COL0", Type.INTEGER);

			QueryPos qPos = QueryPos.getInstance(q);

			_log.info(groupId + "|" + year);

			qPos.add(groupId);

			qPos.add(year);

			Iterator<Integer> itr = q.iterate();

			if (itr.hasNext()) {
				while (itr.hasNext()) {
					Integer it = itr.next();
					int month = it.intValue();
					_log.info("########################## " + month);
					months.add(month);
				}
			}
		} catch (Exception e) {
			_log.error(e);
		} finally {
			closeSession(session);
		}

		_log.info("########################## months.size()" + months.size());
		return months;
	}

	/**
	 * @param groupId
	 * @param month
	 * @param year
	 * @param govCode
	 * @param domainCode
	 * @param level
	 * @param notNullGov
	 * @param notNullDomain
	 * @return
	 */
	public List<DossiersStatistics> getStatsByGovAndDomain(long groupId,
			int month, int year, String govCode, String domainCode, int level,
			boolean notNullGov, boolean notNullDomain) {
		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(SQL_GET_STATS_BY_GOV_DOMAIN);

			_log.info(sql);
			
			if (notNullDomain) {
				sql = StringUtil.replace(sql,
						"(opencps_dossierstatistics.domainCode = ?)",
						"(opencps_dossierstatistics.domainCode != '')");
			}

			if (notNullGov) {
				sql = StringUtil.replace(sql,
						"(opencps_dossierstatistics.govAgencyCode = ?)",
						"(opencps_dossierstatistics.govAgencyCode != '')");
			}

			if (month <= 0) {
				sql = StringUtil.replace(sql,
						"AND (opencps_dossierstatistics.month = ?)",
						StringPool.BLANK);
			}

			SQLQuery q = session.createSQLQuery(sql);

			q.addEntity("dossiersStatistics", DossiersStatistics.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);

			if (Validator.isNotNull(domainCode)) {
				qPos.add(domainCode);
			}

			if (Validator.isNotNull(govCode)) {
				qPos.add(govCode);
			}

			qPos.add(year);

			return (List<DossiersStatistics>) QueryUtil.list(q, getDialect(),
					QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		} catch (Exception e) {
			_log.error(e);
		} finally {
			closeSession(session);
		}

		return null;
	}

	private Log _log = LogFactoryUtil.getLog(DossiersStatisticsFinderImpl.class
			.getName());
}