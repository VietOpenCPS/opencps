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
import org.opencps.util.PortletConstants;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.exception.SystemException;
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

	public static final String SEARCH_DOSSIER_LIKE_ALL = DossierFinder.class
				    .getName() + ".searchDossierLikeAll";
	public static final String COUNT_DOSSIER_LIKE_ALL = DossierFinder.class
				    .getName() + ".countDossierLikeAll";
				
	public static final String SEARCH_DOSSIER_MONITORING = DossierFinder.class
				    .getName() + ".searchDossierMonitoringNewUpdate";
	public static final String COUNT_DOSSIER_MONITORING = DossierFinder.class
				    .getName() + ".countDossierMonitoringNewUpdate";
	
	public int countDossierLikeAll(long groupId, String keywords)
				throws SystemException {
		String[] keys = null;
		boolean andOperator = false;
		if (Validator.isNotNull(keywords)) {
			keys = CustomSQLUtil.keywords(keywords);
		} else {
			andOperator = true;
		}
		return countDossierLikeAll(groupId, keys, andOperator);
	}
	private int countDossierLikeAll(long groupId, String[] keys,boolean andOperator)
	throws SystemException {

		Session session = null;
		keys = CustomSQLUtil.keywords(keys);
		try {
			session = openSession();
	
			String sql = CustomSQLUtil.get(COUNT_DOSSIER_LIKE_ALL);
			
			if(groupId <= 0) {
				sql = StringUtil.replace(
					sql, "(opencps_dossier.groupId = ?) AND", "");
			}
			
			sql = CustomSQLUtil.replaceKeywords(sql, "lower(opencps_dossier.dossierId)",
					StringPool.LIKE, false, keys);
			
			sql = CustomSQLUtil.replaceKeywords(sql, "lower(opencps_dossier.subjectName)",
				StringPool.LIKE, false, keys);
			
			sql = CustomSQLUtil.replaceKeywords(sql, "lower(opencps_serviceinfo.serviceName)",
				StringPool.LIKE, false, keys);
			
			sql = CustomSQLUtil.replaceKeywords(sql, "lower(opencps_dossier.govAgencyName)",
				StringPool.LIKE, false, keys);
			
			sql = CustomSQLUtil.replaceKeywords(sql, "lower(DATE_FORMAT(opencps_dossier.receiveDatetime,'%m/%d/%Y'))",
				StringPool.LIKE, false, keys);
			
			sql = CustomSQLUtil.replaceKeywords(sql, "lower(opencps_dossier.receptionNo)",
				StringPool.LIKE, true, keys);
			
			
			sql = CustomSQLUtil.replaceAndOperator(sql, andOperator);
	
			SQLQuery q = session.createSQLQuery(sql);
	
			q.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);
	
			QueryPos qPos = QueryPos.getInstance(q);
	
			if(groupId > 0) {
				qPos.add(groupId);
			}
			qPos.add(keys, 2);
			qPos.add(keys, 2);
			qPos.add(keys, 2);
			qPos.add(keys, 2);
			qPos.add(keys, 2);
			qPos.add(keys, 2);
			Iterator<Integer> itr = q.iterate();

			if (itr.hasNext()) {
				Integer count = itr.next();
				if (count != null) {
					return count.intValue();
				}
			}
	
			return 0;
		} catch (Exception e) {
			throw new SystemException(e);
		} finally {
			closeSession(session);
		}
	}
	
	public List<Dossier> searchDossierLikeAll(long groupId, String keywords,
		int start, int end, OrderByComparator obc) 
		throws SystemException {
	
		String[] keys = null;
		boolean andOperator = false;
		if (Validator.isNotNull(keywords)) {
			keys = CustomSQLUtil.keywords(keywords);
		} else {
			andOperator = true;
		}
		return searchDossierLikeAll(groupId, keys,
			andOperator, start, end, obc);
	}
	private List<Dossier> searchDossierLikeAll(
        long groupId, String[] keys, boolean andOperator, int start, int end,
        OrderByComparator obc) throws SystemException {

	    // TODO Auto-generated method stub
		Session session = null;
		keys = CustomSQLUtil.keywords(keys);
		try {
			session = openSession();
	
			String sql = CustomSQLUtil.get(SEARCH_DOSSIER_LIKE_ALL);
			
			if(groupId <= 0) {
				sql = StringUtil.replace(
					sql, "(opencps_dossier.groupId = ?) AND", "");
			}
			
			sql = CustomSQLUtil.replaceKeywords(sql, "lower(opencps_dossier.dossierId)",
					StringPool.LIKE, false, keys);
			
			sql = CustomSQLUtil.replaceKeywords(sql, "lower(opencps_dossier.subjectName)",
				StringPool.LIKE, false, keys);
			
			sql = CustomSQLUtil.replaceKeywords(sql, "lower(opencps_serviceinfo.serviceName)",
				StringPool.LIKE, false, keys);
			
			sql = CustomSQLUtil.replaceKeywords(sql, "lower(opencps_dossier.govAgencyName)",
				StringPool.LIKE, false, keys);
			
			sql = CustomSQLUtil.replaceKeywords(sql, "lower(DATE_FORMAT(opencps_dossier.receiveDatetime,'%m/%d/%Y'))",
				StringPool.LIKE, false, keys);
			
			sql = CustomSQLUtil.replaceKeywords(sql, "lower(opencps_dossier.receptionNo)",
				StringPool.LIKE, true, keys);
			
			
			sql = CustomSQLUtil.replaceAndOperator(sql, andOperator);

			SQLQuery q = session.createSQLQuery(sql);
	
			q.addEntity("Dossier", DossierImpl.class);
	
			QueryPos qPos = QueryPos.getInstance(q);
	
			if(groupId > 0) {
				qPos.add(groupId);
			}
			qPos.add(keys, 2);
			qPos.add(keys, 2);
			qPos.add(keys, 2);
			qPos.add(keys, 2);
			qPos.add(keys, 2);
			qPos.add(keys, 2);
			
			return (List<Dossier>) QueryUtil.list(q, getDialect(), start, end);
		} catch (Exception e) {
			throw new SystemException(e);
		} finally {
			closeSession(session);
		}
    }

	
	//new update monitoring
	public int countDossierMonitoringNewUpdate(long groupId, String serviceName, String govAgencyCode, String dossierStatus)
					throws SystemException {
			return _countDossierMonitoringNewUpdate(groupId, serviceName, govAgencyCode, dossierStatus);
		}
	private int _countDossierMonitoringNewUpdate(long groupId, String serviceName, String govAgencyCode, String dossierStatus)
		throws SystemException {

			Session session = null;
			try {
				session = openSession();
		
				String sql = CustomSQLUtil.get(COUNT_DOSSIER_MONITORING);
				
				if(groupId <= 0) {
					sql = StringUtil.replace(
						sql, "(opencps_dossier.groupId = ?) AND", "");
				}
				if(Validator.isNull(serviceName)) {
					sql = StringUtil.replace(
						sql, "(opencps_dossier.serviceInfoId = ?) AND", "");
				}
				if(Validator.isNull(govAgencyCode)) {
					sql = StringUtil.replace(
						sql, "(opencps_dossier.govAgencyCode = ?) AND", "");
				}
				if(Validator.isNull(dossierStatus)) {
					sql = StringUtil.replace(
						sql, "(opencps_dossier.dossierStatus = ?) AND", "(opencps_dossier.dossierStatus <> ?) AND");
				}
		
				SQLQuery q = session.createSQLQuery(sql);
		
				q.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);
		
				QueryPos qPos = QueryPos.getInstance(q);
		
				if(groupId > 0) {
					qPos.add(groupId);
				}
				if(Validator.isNotNull(serviceName)) {
					qPos.add(serviceName);
				}
				if(Validator.isNotNull(govAgencyCode)) {
					qPos.add(govAgencyCode);
				}
				if(Validator.isNull(dossierStatus)) {
					qPos.add(""+PortletConstants.DOSSIER_STATUS_DONE);
				}else{
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
			} catch (Exception e) {
				throw new SystemException(e);
			} finally {
				closeSession(session);
			}
		}
	
	public List<Dossier> searchDossierMonitoringNewUpdate(long groupId, String serviceName, String govAgencyCode, String dossierStatus,
		int start, int end, OrderByComparator obc) 
		throws SystemException {
		return _searchDossierMonitoringNewUpdate(groupId, serviceName,
			govAgencyCode, dossierStatus, start, end, obc);
	}
	private List<Dossier> _searchDossierMonitoringNewUpdate(
		long groupId, String serviceName, String govAgencyCode, String dossierStatus,
		int start, int end, OrderByComparator obc) throws SystemException {

	    // TODO Auto-generated method stub
		Session session = null;
		try {
			session = openSession();
			
			String sql = CustomSQLUtil.get(SEARCH_DOSSIER_MONITORING);
			
			if(groupId <= 0) {
				sql = StringUtil.replace(
					sql, "(opencps_dossier.groupId = ?) AND", "");
			}
			if(Validator.isNull(serviceName)) {
				sql = StringUtil.replace(
					sql, "(opencps_dossier.serviceInfoId = ?) AND", "");
			}
			if(Validator.isNull(govAgencyCode)) {
				sql = StringUtil.replace(
					sql, "(opencps_dossier.govAgencyCode = ?) AND", "");
			}
			if(Validator.isNull(dossierStatus)) {
				sql = StringUtil.replace(
					sql, "(opencps_dossier.dossierStatus = ?) AND", "(opencps_dossier.dossierStatus <> ?) AND");
			}
	System.out.println("DossierFinderImpl._searchDossierMonitoringNewUpdate()"+sql);
			SQLQuery q = session.createSQLQuery(sql);
	
			q.addEntity("Dossier", DossierImpl.class);
	
			QueryPos qPos = QueryPos.getInstance(q);
			
			if(groupId > 0) {
				qPos.add(groupId);
			}
			if(Validator.isNotNull(serviceName)) {
				qPos.add(serviceName);
			}
			if(Validator.isNotNull(govAgencyCode)) {
				qPos.add(govAgencyCode);
			}
			if(Validator.isNull(dossierStatus)) {
				qPos.add(""+PortletConstants.DOSSIER_STATUS_DONE);
			}else{
				qPos.add(dossierStatus);
			}
			
			return (List<Dossier>) QueryUtil.list(q, getDialect(), start, end);
		} catch (Exception e) {
			throw new SystemException(e);
		} finally {
			closeSession(session);
		}
    }
	
	
	private Log _log = LogFactoryUtil
	    .getLog(DossierFinder.class
	        .getName());
}
