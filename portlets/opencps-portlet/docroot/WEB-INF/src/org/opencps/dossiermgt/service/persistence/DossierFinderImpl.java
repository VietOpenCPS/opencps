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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.opencps.dossiermgt.bean.DossierBean;
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

	public static final String COUNT_DOSSIER_BY_USER = DossierFinder.class
		.getName() + ".countDossierByUser";

	public static final String COUNT_DOSSIER_BY_USER_NEW_REQUEST = DossierFinder.class
			.getName() + ".countDossierByUserNewRequest";
	
	public static final String COUNT_DOSSIER_BY_KEYWORDDOMAINANDSTATUS =
		DossierFinder.class
			.getName() + ".countDossierByKeywordDomainAndStatus";

	public static final String SEARCH_DOSSIER_BY_USER = DossierFinder.class
		.getName() + ".searchDossierByUser";

	public static final String SEARCH_DOSSIER_BY_USER_NEW_REQUEST = DossierFinder.class
			.getName() + ".searchDossierByUserNewRequest";
	public static final String SEARCH_DOSSIER_BY_KEYWORDDOMAINANDSTATUS =
		DossierFinder.class
			.getName() + ".searchDossierByKeywordDomainAndStatus";

	public static final String COUNT_DOSSIER_FOR_REMOTE_SERVICE = DossierFinder.class
			.getName() + ".countDossierForRemoteService";

	public static final String SEARCH_DOSSIER_FOR_REMOTE_SERVICE = DossierFinder.class
			.getName() + ".searchDossierForRemoteService";

	public static final String COUNT_DOSSIER_BY_USER_ASSIGN_PROCESSORDER = DossierFinder.class
			.getName() + ".countDossierByUserAssignProcessOrder";

	public static final String SEARCH_DOSSIER_BY_USER_ASSIGN_PROCESSORDER = DossierFinder.class
			.getName() + ".searchDossierByUserAssignProcessOrder";
	public static final String COUNT_DOSSIER_BY_P_S_U = DossierFinder.class
			.getName() + ".countDossierByP_S_U";

	public static final String SEARCH_DOSSIER_BY_P_S_U = DossierFinder.class
			.getName() + ".searchDossierByP_S_U";

	public static final String COUNT_DOSSIER_BY_P_SN_U = DossierFinder.class
			.getName() + ".countDossierByP_SN_U";

	public static final String SEARCH_DOSSIER_BY_P_SN_U = DossierFinder.class
			.getName() + ".searchDossierByP_SN_U";

	public static final String COUNT_DOSSIER_BY_DS_RD_SN_U = DossierFinder.class
			.getName() + ".countDossierByDS_RD_SN_U";

	public static final String SEARCH_DOSSIER_BY_DS_RD_SN_U = DossierFinder.class
			.getName() + ".searchDossierByDS_RD_SN_U";

	public static final String COUNT_DOSSIER_BY_P_PS_U = DossierFinder.class
			.getName() + ".countDossierByP_PS_U";

	public static final String SEARCH_DOSSIER_BY_P_PS_U = DossierFinder.class
.getName() + ".searchDossierByP_PS_U";
	
	private Log _log = LogFactoryUtil
		.getLog(DossierFinder.class
			.getName());

	/**
	 * @param groupId
	 * @param keyword
	 * @param domainCode
	 * @param dossierStatus
	 * @return
	 */
	public int countDossierByKeywordDomainAndStatus(
		long groupId, String keyword, String domainCode,
		List<String> govAgencyCodes, String dossierStatus) {

		String[] keywords = null;

		boolean andOperator = false;

		if (Validator.isNotNull(keyword)) {
			
			keywords = new String[]{
					StringUtil.quote(
						StringUtil.toLowerCase(keyword).trim(), 
						StringPool.PERCENT)};
			
		}
		else {
			andOperator = true;
		}

		return countDossierByKeywordDomainAndStatus(
			groupId, keywords, keyword, domainCode, govAgencyCodes, dossierStatus,
			andOperator);
	}

	/**
	 * @param groupId
	 * @param keywords
	 * @param domainCode
	 * @param dossierStatus
	 * @param andOperator
	 * @return
	 */
	private int countDossierByKeywordDomainAndStatus(
		long groupId, String[] keywords, String keywordStr, String domainCode,
		List<String> govAgencyCodes, String dossierStatus, boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql =
				CustomSQLUtil.get(COUNT_DOSSIER_BY_KEYWORDDOMAINANDSTATUS);

			if (keywords != null && keywords.length > 0) {
				sql =
					CustomSQLUtil.replaceKeywords(
						sql, "lower(opencps_serviceinfo.serviceName)",
						StringPool.LIKE, false, keywords);

				sql =
					CustomSQLUtil.replaceKeywords(
						sql, "lower(opencps_serviceinfo.fullName)",
						StringPool.LIKE, false, keywords);
				
				sql = CustomSQLUtil
						.replaceKeywords(sql,
							"lower(opencps_dossier.receptionNo)",
							StringPool.LIKE, false, keywords);
				
				sql = CustomSQLUtil
					.replaceKeywords(sql, "lower(opencps_dossier.subjectName)",
						StringPool.LIKE, true, keywords);
				
				/*sql = CustomSQLUtil
						.replaceKeywords(sql, "lower(opencps_dossier.dossierId)",
							StringPool.EQUAL, true, keywords);*/
			}

			if (keywords == null || keywords.length == 0) {

				sql = StringUtil
					.replace(sql,
						"AND ((lower(opencps_serviceinfo.serviceName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_serviceinfo.fullName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
				
				sql = StringUtil
						.replace(sql,
							"OR (lower(opencps_dossier.receptionNo) LIKE ? [$AND_OR_NULL_CHECK$])",
							StringPool.BLANK);
				
				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_dossier.subjectName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
				
				sql = StringUtil
						.replace(sql,
							"OR (opencps_dossier.dossierId = ?))",
							StringPool.BLANK);
			}

			if (Validator.isNull(domainCode)) {
				sql =
					StringUtil.replace(
						sql, "AND (opencps_dossier.serviceDomainIndex LIKE ?)",
						StringPool.BLANK);
			}

			if (Validator.isNull(dossierStatus)) {
				sql =
					StringUtil.replace(
						sql, "AND (opencps_dossier.dossierStatus = ?)",
						StringPool.BLANK);
			}

			if (Validator.isNull(govAgencyCodes) ||govAgencyCodes.isEmpty()) {
				sql =
					StringUtil.replace(
						sql, "AND opencps_dossier.govAgencyCode IN (?)",
						StringPool.BLANK);
			}

			sql = CustomSQLUtil.replaceAndOperator(sql, andOperator);
			
			SQLQuery q = session.createSQLQuery(sql);

			q.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);

			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywordStr);
			}

			if (Validator.isNotNull(dossierStatus)) {
				qPos.add(dossierStatus);
			}
			
			if (Validator
					.isNotNull(domainCode)) {
					
				qPos
					.add(domainCode +
						StringPool.PERCENT);

			}
			
			if (Validator.isNotNull(govAgencyCodes) && !govAgencyCodes.isEmpty()) {
				String govCodes = StringUtil.merge(govAgencyCodes);
				qPos.add(govCodes);
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
	 * @param groupId
	 * @param userId
	 * @param keyword
	 * @param serviceDomainTreeIndex
	 * @param dossierStatus
	 * @return
	 */
	public int countDossierByUser(
		long groupId, long userId, String keyword,
		String serviceDomainTreeIndex, String dossierStatus) {

		String[] keywords = null;

		boolean andOperator = false;

		if (Validator.isNotNull(keyword)) {
			keywords = new String[]{
				StringUtil.quote(
					StringUtil.toLowerCase(keyword).trim(), 
					StringPool.PERCENT)};
		}
		else {
			andOperator = true;
		}

		return countDossierByUser(groupId, userId, keywords,
			serviceDomainTreeIndex, dossierStatus, andOperator);
	}

	/**
	 * @param groupId
	 * @param userId
	 * @param keywords
	 * @param serviceDomainTreeIndex
	 * @param dossierStatus
	 * @param andOperator
	 * @return
	 */
	private int countDossierByUser(
		long groupId, long userId, String[] keywords,
		String serviceDomainTreeIndex, String dossierStatus,
		boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(COUNT_DOSSIER_BY_USER);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_serviceinfo.serviceName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_service_config.govAgencyName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql, "lower(opencps_dossier.subjectName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql, "lower(opencps_dossier.receptionNo)",
						StringPool.LIKE, true, keywords);
				
				sql = CustomSQLUtil
						.replaceKeywords(sql, "lower(opencps_dossier.dossierId)",
							StringPool.LIKE, true, keywords);
			}
			else {
				sql = StringUtil
					.replace(sql,
							"AND ((lower(opencps_serviceinfo.serviceName) LIKE ? [$AND_OR_NULL_CHECK$]) OR (lower(opencps_service_config.govAgencyName) LIKE ? [$AND_OR_NULL_CHECK$]) OR (lower(opencps_dossier.subjectName) LIKE ? [$AND_OR_NULL_CHECK$]) OR (lower(opencps_dossier.receptionNo) LIKE ? [$AND_OR_NULL_CHECK$]) OR (lower(opencps_dossier.dossierId) LIKE ? [$AND_OR_NULL_CHECK$]))",
						StringPool.BLANK);
			}

			if (Validator
				.isNull(serviceDomainTreeIndex)) {
				
				sql = StringUtil
						.replace(sql,
								"AND (opencps_dossier.serviceDomainIndex LIKE ? OR opencps_dossier.serviceDomainIndex = ?)",
							StringPool.BLANK);
			}
			else {
				if (serviceDomainTreeIndex.contains(StringPool.PERIOD)) {
					sql = StringUtil
						.replace(sql,
							"AND (opencps_dossier.serviceDomainIndex LIKE ? OR opencps_dossier.serviceDomainIndex = ?)",
							"AND (opencps_dossier.serviceDomainIndex LIKE ?)");

					serviceDomainTreeIndex = serviceDomainTreeIndex
						.substring(0, serviceDomainTreeIndex
							.indexOf(StringPool.PERIOD) + 1);

				}

			}

			if (Validator
				.isNull(dossierStatus)) {
				sql = StringUtil
					.replace(sql, "AND (opencps_dossier.dossierStatus = ?)",
						StringPool.BLANK);
			}

			sql = CustomSQLUtil
				.replaceAndOperator(sql, andOperator);
			
			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos
				.getInstance(q);

			qPos
				.add(groupId);

			qPos
				.add(userId);

			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
			}

			if (Validator
				.isNotNull(dossierStatus)) {
				qPos
					.add(dossierStatus);
			}
			
			if (Validator
				.isNotNull(serviceDomainTreeIndex) && serviceDomainTreeIndex.contains(StringPool.PERIOD)) {
				qPos
					.add(serviceDomainTreeIndex + StringPool.PERCENT);

			}
			else if (Validator
				.isNotNull(serviceDomainTreeIndex) && !serviceDomainTreeIndex.contains(StringPool.PERIOD)) {
				qPos
					.add(serviceDomainTreeIndex + StringPool.PERIOD +
						StringPool.PERCENT);
				qPos
					.add(serviceDomainTreeIndex);
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

	/**
	 * @param groupId
	 * @param keyword
	 * @param domainCode
	 * @param dossierStatus
	 * @param start
	 * @param end
	 * @param obc
	 * @return
	 */
	public List<Dossier> searchDossierByKeywordDomainAndStatus(
		long groupId, String keyword, String domainCode,
		List<String> govAgencyCodes, String dossierStatus, int start, int end,
		OrderByComparator obc) {

		boolean andOperator = false;
		String[] keywords = null;
		if (Validator.isNotNull(keyword)) {
			keywords = new String[]{
					StringUtil.quote(
						StringUtil.toLowerCase(keyword).trim(), 
						StringPool.PERCENT)};
		}
		else {
			andOperator = true;
		}
		return searchDossierByKeywordDomainAndStatus(
			groupId, keywords , keyword, domainCode, govAgencyCodes, dossierStatus,
			start, end, obc, andOperator);
	}

	/**
	 * @param groupId
	 * @param keywords
	 * @param domainCode
	 * @param dossierStatus
	 * @param start
	 * @param end
	 * @param obc
	 * @param andOperator
	 * @return
	 */
	private List<Dossier> searchDossierByKeywordDomainAndStatus(
		long groupId, String[] keywords, String keywordStr, String domainCode,
		List<String> govAgencyCodes, String dossierStatus, int start, int end,
		OrderByComparator obc, boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql =
				CustomSQLUtil.get(SEARCH_DOSSIER_BY_KEYWORDDOMAINANDSTATUS);
			
			if (keywords != null && keywords.length > 0) {
				sql =
					CustomSQLUtil.replaceKeywords(
						sql, "lower(opencps_serviceinfo.serviceName)",
						StringPool.LIKE, false, keywords);

				sql =
					CustomSQLUtil.replaceKeywords(
						sql, "lower(opencps_serviceinfo.fullName)",
						StringPool.LIKE, false, keywords);
				
				sql = CustomSQLUtil
						.replaceKeywords(sql,
							"lower(opencps_dossier.receptionNo)",
							StringPool.LIKE, false, keywords);
				
				sql = CustomSQLUtil
					.replaceKeywords(sql, "lower(opencps_dossier.subjectName)",
						StringPool.LIKE, true, keywords);
				
				/*sql = CustomSQLUtil
						.replaceKeywords(sql, "lower(opencps_dossier.dossierId)",
							StringPool.LIKE, true, keywords);*/
			}

			if (keywords == null || keywords.length == 0) {

				sql = StringUtil
					.replace(sql,
						"AND ((lower(opencps_serviceinfo.serviceName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_serviceinfo.fullName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
				
				sql = StringUtil
						.replace(sql,
							"OR (lower(opencps_dossier.receptionNo) LIKE ? [$AND_OR_NULL_CHECK$])",
							StringPool.BLANK);
				
				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_dossier.subjectName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
				
				sql = StringUtil
						.replace(sql,
							"OR (opencps_dossier.dossierId = ?))",
							StringPool.BLANK);
			}

			if (Validator.isNull(domainCode)) {
				sql =
					StringUtil.replace(
						sql, "AND (opencps_dossier.serviceDomainIndex LIKE ?)",
						StringPool.BLANK);
			}

			if (Validator.isNull(dossierStatus)) {
				sql =
					StringUtil.replace(
						sql, "AND (opencps_dossier.dossierStatus = ?)",
						StringPool.BLANK);
			}

			if (Validator.isNull(govAgencyCodes) ||govAgencyCodes.isEmpty()) {
				sql =
					StringUtil.replace(
						sql, "AND opencps_dossier.govAgencyCode IN (?)",
						StringPool.BLANK);
			}

			sql = CustomSQLUtil.replaceAndOperator(sql, andOperator);
			
			sql = CustomSQLUtil.replaceOrderBy(sql, obc);

			SQLQuery q = session.createSQLQuery(sql);

			q.addEntity("Dossier", DossierImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);

			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywordStr);
			}

			if (Validator.isNotNull(dossierStatus)) {
				qPos.add(dossierStatus);
			}
			
			if (Validator
					.isNotNull(domainCode)) {
					
				qPos
					.add(domainCode +
						StringPool.PERCENT);

			}
			
			if (Validator.isNotNull(govAgencyCodes) && !govAgencyCodes.isEmpty()) {
				String govCodes = StringUtil.merge(govAgencyCodes);
				qPos.add(govCodes);
			}

			List<Dossier> results =
				(List<Dossier>) QueryUtil.list(q, getDialect(), start, end);

			return results;
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
	 * @param groupId
	 * @param userId
	 * @param keyword
	 * @param serviceDomainTreeIndex
	 * @param dossierStatus
	 * @param start
	 * @param end
	 * @param obc
	 * @return
	 */
	public List searchDossierByUser(
		long groupId, long userId, String keyword,
		String serviceDomainTreeIndex, String dossierStatus, int start, int end,
		OrderByComparator obc) {

		String[] keywords = null;
		boolean andOperator = false;
		if (Validator.isNotNull(keyword)) {
			keywords = new String[]{
					StringUtil.quote(
						StringUtil.toLowerCase(keyword).trim(), 
						StringPool.PERCENT)};
		}
		else {
			andOperator = true;
		}
		return searchDossierByUser(groupId, userId, keywords,
			serviceDomainTreeIndex, dossierStatus, andOperator, start, end,
			obc);
	}

	/**
	 * @param groupId
	 * @param userId
	 * @param keywords
	 * @param serviceDomainTreeIndex
	 * @param dossierStatus
	 * @param andOperator
	 * @param start
	 * @param end
	 * @param obc
	 * @return
	 */
	private List<DossierBean> searchDossierByUser(
		long groupId, long userId, String[] keywords,
		String serviceDomainTreeIndex, String dossierStatus,
		boolean andOperator, int start, int end, OrderByComparator obc) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(SEARCH_DOSSIER_BY_USER);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_serviceinfo.serviceName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_service_config.govAgencyName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql, "lower(opencps_dossier.subjectName)",
						StringPool.LIKE, true, keywords);
				
				sql = CustomSQLUtil
					.replaceKeywords(sql, "lower(opencps_dossier.receptionNo)",
						StringPool.LIKE, true, keywords);
				
				sql = CustomSQLUtil
						.replaceKeywords(sql, "lower(opencps_dossier.dossierId)",
							StringPool.LIKE, true, keywords);
			}
			else {
				sql = StringUtil
					.replace(sql,
						"AND ((lower(opencps_serviceinfo.serviceName) LIKE ? [$AND_OR_NULL_CHECK$]) OR (lower(opencps_service_config.govAgencyName) LIKE ? [$AND_OR_NULL_CHECK$]) OR (lower(opencps_dossier.subjectName) LIKE ? [$AND_OR_NULL_CHECK$]) OR (lower(opencps_dossier.receptionNo) LIKE ? [$AND_OR_NULL_CHECK$]) OR (lower(opencps_dossier.dossierId) LIKE ? [$AND_OR_NULL_CHECK$]))",
						StringPool.BLANK);
			}

			if (Validator
				.isNull(serviceDomainTreeIndex)) {

				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossier.serviceDomainIndex LIKE ? OR opencps_dossier.serviceDomainIndex = ?)",
						StringPool.BLANK);
			}
			else {
				if (serviceDomainTreeIndex.contains(StringPool.PERIOD)) {
					sql = StringUtil
						.replace(sql,
							"AND (opencps_dossier.serviceDomainIndex LIKE ? OR opencps_dossier.serviceDomainIndex = ?)",
							"AND (opencps_dossier.serviceDomainIndex LIKE ?)");

					serviceDomainTreeIndex = serviceDomainTreeIndex
						.substring(0, serviceDomainTreeIndex
							.indexOf(StringPool.PERIOD) + 1);

				}

			}

			if (Validator
				.isNull(dossierStatus)) {
				sql = StringUtil
					.replace(sql, "AND (opencps_dossier.dossierStatus = ?)",
						StringPool.BLANK);
			}

			sql = CustomSQLUtil
				.replaceAndOperator(sql, andOperator);
			
			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.addEntity("Dossier", DossierImpl.class);
			q
				.addScalar("SERVICE_NAME", Type.STRING);

			QueryPos qPos = QueryPos
				.getInstance(q);

			qPos
				.add(groupId);

			qPos
				.add(userId);

			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
			}

			if (Validator
				.isNotNull(dossierStatus)) {
				qPos
					.add(dossierStatus);
			}
			
			if (Validator
				.isNotNull(serviceDomainTreeIndex) && serviceDomainTreeIndex.contains(StringPool.PERIOD)) {
				qPos
					.add(serviceDomainTreeIndex + StringPool.PERCENT);

			}
			else if (Validator
				.isNotNull(serviceDomainTreeIndex) && !serviceDomainTreeIndex.contains(StringPool.PERIOD)) {
				qPos
					.add(serviceDomainTreeIndex + StringPool.PERIOD +
						StringPool.PERCENT);
				qPos
					.add(serviceDomainTreeIndex);
			}

			Iterator<Object[]> itr = (Iterator<Object[]>) QueryUtil
				.list(q, getDialect(), start, end).iterator();

			List<DossierBean> dossierBeans = new ArrayList<DossierBean>();

			if (itr
				.hasNext()) {
				while (itr
					.hasNext()) {
					DossierBean dossierBean = new DossierBean();

					Object[] objects = itr
						.next();

					Dossier dossier = (Dossier) objects[0];

					String serviceName = (String) objects[1];

					dossierBean
						.setDossierId(dossier
							.getDossierId());
					dossierBean
						.setDossier(dossier);
					dossierBean
						.setServiceName(serviceName);

					dossierBeans
						.add(dossierBean);

				}
			}

			return dossierBeans;

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
	
	/**
	 * @param dossiertype
	 * @param organizationcode
	 * @param status
	 * @param fromdate
	 * @param todate
	 * @param documentyear
	 * @param customername
	 * @return
	 */
	public int countDossierForRemoteService(String dossiertype,
			String organizationcode, String processStepId, String status,
			String fromdate, String todate, int documentyear,
			String customername) {

		Session session = null;
		String[] keywords = null;
		boolean andOperator = false;
		if (Validator.isNotNull(customername)) {
			keywords = CustomSQLUtil.keywords(customername);
		} else {
			andOperator = true;
		}

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			session = openSession();

			String sql = CustomSQLUtil.get(COUNT_DOSSIER_FOR_REMOTE_SERVICE);
			if ("-1".equals(dossiertype)) {
				sql = StringUtil.replace(sql,
						"AND opencps_dossier.serviceInfoId = ?",
						StringPool.BLANK);
			}

			if ("-1".equals(status) || "".equals(status)
					|| Validator.isNull(status)) {
				sql = StringUtil.replace(sql,
						"AND opencps_dossier.dossierStatus = ?",
						StringPool.BLANK);
			}
			if (Validator.isNull(processStepId) || "-1".equals(processStepId)) {
				sql = StringUtil.replace(sql,
						"AND opencps_processorder.processStepId = ?",
						StringPool.BLANK);
			}
			if (Validator.isNull(todate) || "".equals(todate)) {
				sql = StringUtil.replace(sql,
						"AND opencps_dossier.receiveDatetime <= ?",
						StringPool.BLANK);
			}
			if (Validator.isNull(fromdate) || "".equals(fromdate)) {
				sql = StringUtil.replace(sql,
						"AND opencps_dossier.receiveDatetime >= ?",
						StringPool.BLANK);
			}
			if (documentyear <= 0) {
				sql = StringUtil.replace(sql,
						"AND YEAR(opencps_dossier.receiveDatetime) = ?",
						StringPool.BLANK);
			}
			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
						.replace(
								sql,
								"AND (lower(opencps_dossier.subjectName) LIKE ? [$AND_OR_NULL_CHECK$])",
								StringPool.BLANK);
			} else {
				sql = CustomSQLUtil.replaceKeywords(sql,
						"lower(opencps_dossier.subjectName)", StringPool.LIKE,
						true, keywords);
			}

			sql = CustomSQLUtil.replaceAndOperator(sql, andOperator);

			SQLQuery q = session.createSQLQuery(sql);

			q.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(organizationcode);
			if (!"-1".equals(dossiertype)) {
				qPos.add(dossiertype);
			}
			if (Validator.isNotNull(processStepId)
					&& !"-1".equals(processStepId)) {
				qPos.add(processStepId);
			}
			if (!"-1".equals(status)) {
				qPos.add(status);
			}
			if (Validator.isNotNull(todate) && !"".equals(todate)) {
				// _log.info("To date: " + sdf.format(todate));
				// qPos.add(sdf.format(todate));
				qPos.add(todate);
			}
			if (Validator.isNotNull(fromdate) && !"".equals(fromdate)) {
				// _log.info("From date: " + sdf.format(fromdate));
				// qPos.add(sdf.format(fromdate));
				qPos.add(fromdate);
			}
			if (documentyear > 0) {
				qPos.add(documentyear);
			}
			if (keywords != null && keywords.length > 0) {
				qPos.add(keywords, 2);
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
			_log.error(e);
		} finally {
			closeSession(session);
		}

		return 0;

	}

	/**
	 * @param dossiertype
	 * @param organizationcode
	 * @param status
	 * @param fromdate
	 * @param todate
	 * @param documentyear
	 * @param customername
	 * @return
	 */
	public List<Dossier> searchDossierForRemoteService(String dossiertype,
			String organizationcode, String processStepId, String status,
			String fromdate, String todate, int documentyear,
			String customername, int start, int end) {

		Session session = null;
		String[] keywords = null;
		boolean andOperator = false;
		if (Validator.isNotNull(customername)) {
			keywords = CustomSQLUtil.keywords(customername);
		} else {
			andOperator = true;
		}
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(SEARCH_DOSSIER_FOR_REMOTE_SERVICE);
			if (Validator.isNull(processStepId) || "-1".equals(processStepId)) {
				sql = StringUtil.replace(sql,
						"AND opencps_processorder.processStepId = ?",
						StringPool.BLANK);
			}
			if ("-1".equals(status) || "".equals(status)
					|| Validator.isNull(status)) {
				sql = StringUtil.replace(sql,
						"AND opencps_dossier.dossierStatus = ?",
						StringPool.BLANK);
			}
			if ("-1".equals(dossiertype)) {
				sql = StringUtil.replace(sql,
						"AND opencps_dossier.serviceInfoId = ?",
						StringPool.BLANK);
			}
			if (Validator.isNull(todate) || "".equals(todate)) {
				sql = StringUtil.replace(sql,
						"AND opencps_dossier.receiveDatetime <= ?",
						StringPool.BLANK);
			}
			if (Validator.isNull(fromdate) || "".equals(fromdate)) {
				sql = StringUtil.replace(sql,
						"AND opencps_dossier.receiveDatetime >= ?",
						StringPool.BLANK);
			}
			if (documentyear <= 0) {
				sql = StringUtil.replace(sql,
						"AND YEAR(opencps_dossier.receiveDatetime) = ?",
						StringPool.BLANK);
			}
			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
						.replace(
								sql,
								"AND (lower(opencps_dossier.subjectName) LIKE ? [$AND_OR_NULL_CHECK$])",
								StringPool.BLANK);
			} else {
				sql = CustomSQLUtil.replaceKeywords(sql,
						"lower(opencps_dossier.subjectName)", StringPool.LIKE,
						true, keywords);
			}

			sql = CustomSQLUtil.replaceAndOperator(sql, andOperator);

			SQLQuery q = session.createSQLQuery(sql);
			q.addEntity("Dossier", DossierImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(organizationcode);
			if (!"-1".equals(dossiertype)) {
				qPos.add(dossiertype);
			}
			if (Validator.isNotNull(processStepId)
					&& !"-1".equals(processStepId)) {
				qPos.add(processStepId);
			}
			if (!"-1".equals(status)) {
				qPos.add(status);
			}
			if (Validator.isNotNull(todate) && !"".equals(todate)) {
				// qPos.add(sdf.format(todate));
				qPos.add(todate);
			}
			if (Validator.isNotNull(fromdate) && !"".equals(fromdate)) {
				// qPos.add(sdf.format(fromdate));
				qPos.add(fromdate);
			}
			if (documentyear > 0) {
				qPos.add(documentyear);
			}
			if (keywords != null && keywords.length > 0) {
				qPos.add(keywords, 2);
			}
			return (List<Dossier>) QueryUtil.list(q, getDialect(), start, end);
		} catch (Exception e) {
			_log.error(e);
		} finally {
			closeSession(session);
		}

		return null;

	}

	/**
	 * @param userId
	 * @return
	 */
	public int countDossierByUserAssignProcessOrder(long userId) {

		Session session = null;
		String[] keywords = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
					.get(COUNT_DOSSIER_BY_USER_ASSIGN_PROCESSORDER);

			SQLQuery q = session.createSQLQuery(sql);

			q.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(userId);

			Iterator<Integer> itr = q.iterate();

			if (itr.hasNext()) {
				Integer count = itr.next();

				if (count != null) {
					return count.intValue();
				}
			}

			return 0;

		} catch (Exception e) {
			_log.error(e);
		} finally {
			closeSession(session);
		}

		return 0;

	}

	/**
	 * @param userId
	 * @return
	 */
	public List<Dossier> searchDossierByUserAssignByProcessOrder(long userId,
			int start, int end) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
					.get(SEARCH_DOSSIER_BY_USER_ASSIGN_PROCESSORDER);

			SQLQuery q = session.createSQLQuery(sql);
			q.addEntity("Dossier", DossierImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(userId);

			return (List<Dossier>) QueryUtil.list(q, getDialect(), start, end);
		} catch (Exception e) {
			_log.error(e);
		} finally {
			closeSession(session);
		}

		return null;

	}

	/**
	 * @param userId
	 * @param processNo
	 * @param stepNo
	 * @return
	 */
	public int countDossierByP_S_U(String processNo, String stepNo, long userId) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(COUNT_DOSSIER_BY_P_S_U);

			if (userId <= 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_processorder.assignToUserId = ?",
						StringPool.BLANK);
			}

			SQLQuery q = session.createSQLQuery(sql);

			q.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(processNo);
			qPos.add(stepNo);
			if (userId > 0) {
				qPos.add(userId);
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
			_log.error(e);
		} finally {
			closeSession(session);
		}

		return 0;

	}

	/**
	 * @param userId
	 * @param processNo
	 * @param stepNo
	 * @return
	 */
	public List<Dossier> searchDossierByP_S_U(String processNo, String stepNo,
			long userId, int start, int end) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(SEARCH_DOSSIER_BY_P_S_U);

			if (userId <= 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_processorder.assignToUserId = ?",
						StringPool.BLANK);
			}

			SQLQuery q = session.createSQLQuery(sql);
			q.addEntity("Dossier", DossierImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(processNo);
			qPos.add(stepNo);
			if (userId > 0) {
				qPos.add(userId);
			}

			return (List<Dossier>) QueryUtil.list(q, getDialect(), start, end);
		} catch (Exception e) {
			_log.error(e);
		} finally {
			closeSession(session);
		}

		return null;

	}

	/**
	 * @param userId
	 * @param processNo
	 * @param stepName
	 * @return
	 */
	public int countDossierByP_SN_U(String processNo, String stepName, long userId) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(COUNT_DOSSIER_BY_P_SN_U);

			if (userId <= 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_processorder.assignToUserId = ?",
						StringPool.BLANK);
			}
			if (Validator.isNull(processNo) || processNo.length() == 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_serviceprocess.processNo = ?",
						StringPool.BLANK);
				
			}
			if (Validator.isNull(stepName) || stepName.length() == 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_processstep.stepName = ?",
						StringPool.BLANK);
				
			}
			SQLQuery q = session.createSQLQuery(sql);

			q.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos.getInstance(q);
			
			if (Validator.isNotNull(processNo) && processNo.length() > 0) {
				qPos.add(processNo);
			}
			if (Validator.isNotNull(stepName) && stepName.length() > 0) {
				qPos.add(stepName);				
			}
			if (userId > 0) {
				qPos.add(userId);
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
			_log.error(e);
		} finally {
			closeSession(session);
		}

		return 0;

	}

	/**
	 * @param userId
	 * @param processNo
	 * @param stepName
	 * @return
	 */
	public List<Dossier> searchDossierByP_SN_U(String processNo, String stepName,
			long userId, int start, int end) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(SEARCH_DOSSIER_BY_P_SN_U);

			if (userId <= 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_processorder.assignToUserId = ?",
						StringPool.BLANK);
			}
			if (Validator.isNull(processNo) || processNo.length() == 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_serviceprocess.processNo = ?",
						StringPool.BLANK);
				
			}
			if (Validator.isNull(stepName) || stepName.length() == 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_processstep.stepName = ?",
						StringPool.BLANK);
				
			}

			SQLQuery q = session.createSQLQuery(sql);
			q.addEntity("Dossier", DossierImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			if (Validator.isNotNull(processNo) && processNo.length() > 0) {
				qPos.add(processNo);
			}
			if (Validator.isNotNull(stepName) && stepName.length() > 0) {
				qPos.add(stepName);				
			}
			
			if (userId > 0) {
				qPos.add(userId);
			}

			return (List<Dossier>) QueryUtil.list(q, getDialect(), start, end);
		} catch (Exception e) {
			_log.error(e);
		} finally {
			closeSession(session);
		}

		return null;

	}
	
	/**
	 * @param userId
	 * @param dossierStatus
	 * @param serviceNo
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public int countDossierByDS_RD_SN_U(long userId, String dossierStatus, String serviceNo, String fromDate, String toDate) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(COUNT_DOSSIER_BY_DS_RD_SN_U);

			if (userId <= 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_processorder.assignToUserId = ?",
						StringPool.BLANK);
			}
			if (Validator.isNull(dossierStatus) || dossierStatus.length() == 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_dossier.dossierStatus = ?",
						StringPool.BLANK);
				
			}
			if (Validator.isNull(serviceNo) || serviceNo.length() == 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_serviceinfo.serviceNo = ?",
						StringPool.BLANK);
				
			}			
			if (Validator.isNull(fromDate) || fromDate.length() == 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_dossier.submitDatetime >= ?",
						StringPool.BLANK);
				
			}
			if (Validator.isNull(toDate) || toDate.length() == 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_dossier.submitDatetime <= ?",
						StringPool.BLANK);
				
			}
			SQLQuery q = session.createSQLQuery(sql);

			q.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos.getInstance(q);
			
			if (Validator.isNotNull(dossierStatus) && dossierStatus.length() > 0) {
				qPos.add(dossierStatus);
			}
			if (Validator.isNotNull(serviceNo) && serviceNo.length() > 0) {
				qPos.add(serviceNo);
			}
			if (Validator.isNotNull(fromDate) && fromDate.length() > 0) {
				qPos.add(fromDate);
			}
			if (Validator.isNotNull(toDate) && toDate.length() > 0) {
				qPos.add(toDate);
			}
			
			if (userId > 0) {
				qPos.add(userId);
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
			_log.error(e);
		} finally {
			closeSession(session);
		}

		return 0;

	}

	/**
	 * @param userId
	 * @param dossierStatus
	 * @param serviceNo
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public List<Dossier> searchDossierByDS_RD_SN_U(String dossierStatus, String serviceNo, String fromDate, String toDate,
			long userId, int start, int end) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(SEARCH_DOSSIER_BY_DS_RD_SN_U);

			if (userId <= 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_processorder.assignToUserId = ?",
						StringPool.BLANK);
			}
			if (Validator.isNull(dossierStatus) || dossierStatus.length() == 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_dossier.dossierStatus = ?",
						StringPool.BLANK);
				
			}
			if (Validator.isNull(serviceNo) || serviceNo.length() == 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_serviceinfo.serviceNo = ?",
						StringPool.BLANK);
				
			}			
			if (Validator.isNull(fromDate) || fromDate.length() == 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_dossier.submitDatetime >= ?",
						StringPool.BLANK);
				
			}
			if (Validator.isNull(toDate) || toDate.length() == 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_dossier.submitDatetime <= ?",
						StringPool.BLANK);
				
			}

			SQLQuery q = session.createSQLQuery(sql);
			q.addEntity("Dossier", DossierImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			if (Validator.isNotNull(dossierStatus) && dossierStatus.length() > 0) {
				qPos.add(dossierStatus);
			}
			if (Validator.isNotNull(serviceNo) && serviceNo.length() > 0) {
				qPos.add(serviceNo);
			}
			if (Validator.isNotNull(fromDate) && fromDate.length() > 0) {
				qPos.add(fromDate);
			}
			if (Validator.isNotNull(toDate) && toDate.length() > 0) {
				qPos.add(toDate);
			}
			
			if (userId > 0) {
				qPos.add(userId);
			}
			
			return (List<Dossier>) QueryUtil.list(q, getDialect(), start, end);
		} catch (Exception e) {
			_log.error(e);
		} finally {
			closeSession(session);
		}

		return null;

	}	
	
	/**
	 * @param userId
	 * @param processNo
	 * @param processStepNo
	 * @return
	 */
	public int countDossierByP_PS_U(String processNo, String processStepNo, long userId) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(COUNT_DOSSIER_BY_P_PS_U);

			if (userId <= 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_processorder.assignToUserId = ?",
						StringPool.BLANK);
			}

			if (Validator.isNull(processNo) || processNo.length() <= 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_serviceprocess.processNo = ?",
						StringPool.BLANK);				
			}
			if (Validator.isNull(processStepNo) || processStepNo.length() <= 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_processstep.processStepNo = ?",
						StringPool.BLANK);				
			}

			SQLQuery q = session.createSQLQuery(sql);

			q.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos.getInstance(q);
			if (Validator.isNotNull(processNo) && processNo.length() > 0) {
				qPos.add(processNo);
			}
			if (Validator.isNotNull(processStepNo) && processStepNo.length() > 0) {
				qPos.add(processStepNo);
			}
			if (userId > 0) {
				qPos.add(userId);
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
			_log.error(e);
		} finally {
			closeSession(session);
		}

		return 0;

	}

	/**
	 * @param userId
	 * @param processNo
	 * @param processStepNo
	 * @return
	 */
	public List<Dossier> searchDossierByP_PS_U(String processNo, String processStepNo,
			long userId, int start, int end) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(SEARCH_DOSSIER_BY_P_PS_U);

			if (userId <= 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_processorder.assignToUserId = ?",
						StringPool.BLANK);
			}
			if (Validator.isNull(processNo) || processNo.length() <= 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_serviceprocess.processNo = ?",
						StringPool.BLANK);				
			}
			if (Validator.isNull(processStepNo) || processStepNo.length() <= 0) {
				sql = StringUtil.replace(sql,
						"AND opencps_processstep.processStepNo = ?",
						StringPool.BLANK);				
			}

			SQLQuery q = session.createSQLQuery(sql);
			q.addEntity("Dossier", DossierImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			if (Validator.isNotNull(processNo) && processNo.length() > 0) {
				qPos.add(processNo);
			}
			if (Validator.isNotNull(processStepNo) && processStepNo.length() > 0) {
				qPos.add(processStepNo);
			}
			if (userId > 0) {
				qPos.add(userId);
			}

			return (List<Dossier>) QueryUtil.list(q, getDialect(), start, end);
		} catch (Exception e) {
			_log.error(e);
		} finally {
			closeSession(session);
		}

		return null;
	} 	
	
	/**
	 * @param groupId
	 * @param userId
	 * @param keyword
	 * @param serviceDomainTreeIndex
	 * @param dossierStatus
	 * @return
	 */
	public int countDossierByUserNewRequest(
		long groupId, long userId) {

		boolean andOperator = false;

		return countDossierByUserNewRequest(groupId, userId, andOperator);
	}
	
	/**
	 * @param groupId
	 * @param userId
	 * @param keywords
	 * @param serviceDomainTreeIndex
	 * @param dossierStatus
	 * @param andOperator
	 * @return
	 */
	private int countDossierByUserNewRequest(
		long groupId, long userId, 
		boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(COUNT_DOSSIER_BY_USER_NEW_REQUEST);

			sql = CustomSQLUtil
				.replaceAndOperator(sql, andOperator);

			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos
				.getInstance(q);

			qPos
				.add(groupId);

			qPos
				.add(userId);

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


/**
	 * @param groupId
	 * @param userId
	 * @param keyword
	 * @param serviceDomainTreeIndex
	 * @param dossierStatus
	 * @param start
	 * @param end
	 * @param obc
	 * @return
	 */
	public List searchDossierByUserNewRequest(
		long groupId, long userId, int start, int end,
		OrderByComparator obc) {

		boolean andOperator = false;

		return searchDossierByUserNewRequest(groupId, userId, andOperator, start, end,
			obc);
	}

	/**
	 * @param groupId
	 * @param userId
	 * @param keywords
	 * @param serviceDomainTreeIndex
	 * @param dossierStatus
	 * @param andOperator
	 * @param start
	 * @param end
	 * @param obc
	 * @return
	 */
	private List<DossierBean> searchDossierByUserNewRequest(
		long groupId, long userId,
		boolean andOperator, int start, int end, OrderByComparator obc) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(SEARCH_DOSSIER_BY_USER_NEW_REQUEST);

			sql = CustomSQLUtil
				.replaceAndOperator(sql, andOperator);

			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.addEntity("Dossier", DossierImpl.class);
			q
				.addScalar("SERVICE_NAME", Type.STRING);

			QueryPos qPos = QueryPos
				.getInstance(q);

			qPos
				.add(groupId);

			qPos
				.add(userId);

			Iterator<Object[]> itr = (Iterator<Object[]>) QueryUtil
				.list(q, getDialect(), start, end).iterator();

			List<DossierBean> dossierBeans = new ArrayList<DossierBean>();

			if (itr
				.hasNext()) {
				while (itr
					.hasNext()) {
					DossierBean dossierBean = new DossierBean();

					Object[] objects = itr
						.next();

					Dossier dossier = (Dossier) objects[0];

					String serviceName = (String) objects[1];

					dossierBean
						.setDossierId(dossier
							.getDossierId());
					dossierBean
						.setDossier(dossier);
					dossierBean
						.setServiceName(serviceName);

					dossierBeans
						.add(dossierBean);

				}
			}

			return dossierBeans;

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
}
