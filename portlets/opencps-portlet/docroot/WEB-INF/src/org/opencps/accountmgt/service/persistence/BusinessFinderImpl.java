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
/**
 * 
 */
package org.opencps.accountmgt.service.persistence;

import java.util.Iterator;
import java.util.List;

import javax.transaction.SystemException;

import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.impl.BusinessImpl;

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
 * @author dunglt
 *
 */
public class BusinessFinderImpl extends BasePersistenceImpl<Business> implements
		BusinessFinder {

	public static final String SEARCH_BUSINESS = BusinessFinder.class.getName()
			+ ".searchBusiness";
	public static final String COUNT_BUSINESS = BusinessFinder.class.getName()
			+ ".countBusiness";

	public List<Business> searchBusiness(long groupId, String keywords,
			int accountStatus, String businessDomain, int start, int end) {

		String[] names = null;
		boolean andOperator = false;

		if (Validator.isNotNull(keywords)) {
			names = CustomSQLUtil.keywords(keywords);
		} else {
			andOperator = true;
		}
		
		return _searchBusiness(groupId, names, accountStatus, andOperator,
				businessDomain, start, end);
	}
	
	/**
	 * @param groupId
	 * @param keywords
	 * @param accountStatus
	 * @param andOperator
	 * @param businessDomain
	 * @param start
	 * @param end
	 * @return
	 */
	private List<Business> _searchBusiness(long groupId, String[] keywords,
			int accountStatus, boolean andOperator, String businessDomain,
			int start, int end) {

		// /*
		// * keywords = CustomSQLUtil .keywords(keywords, false);
		// */

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(SEARCH_BUSINESS);

			if (keywords != null && keywords.length > 0) {

				sql = CustomSQLUtil.replaceKeywords(sql,
						"lower(opencps_acc_business.name)", StringPool.LIKE,
						true, keywords);

				sql = CustomSQLUtil.replaceKeywords(sql,
						"lower(opencps_acc_business.shortName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil.replaceKeywords(sql,
						"lower(opencps_acc_business.enName))", StringPool.LIKE,
						true, keywords);

			} else {

				sql = StringUtil
						.replace(
								sql,
								"AND ((lower(opencps_acc_business.name) LIKE ? [$AND_OR_NULL_CHECK$])",
								StringPool.BLANK);

				sql = StringUtil
						.replace(
								sql,
								"OR (lower(opencps_acc_business.shortName) LIKE ? [$AND_OR_NULL_CHECK$])",
								StringPool.BLANK);

				sql = StringUtil
						.replace(
								sql,
								"OR (lower(opencps_acc_business.enName) LIKE ? [$AND_OR_NULL_CHECK$]))",
								StringPool.BLANK);

			}

			if (Validator.isNull(businessDomain)) {
				sql = StringUtil
						.replace(
								sql,
								"AND (opencps_acc_businessdomain.businessDomainCode = ?)",
								StringPool.BLANK);
				sql = StringUtil.replace(sql, 
						"INNER JOIN opencps_acc_businessdomain ON opencps_acc_business.businessId = opencps_acc_businessdomain.businessId"
						, StringPool.BLANK);

			}

			if (accountStatus == -1) {
				sql = StringUtil.replace(sql,
						"AND (opencps_acc_business.accountStatus = ?)",
						StringPool.BLANK);
			}

			sql = CustomSQLUtil.replaceAndOperator(sql, andOperator);
			
			SQLQuery q = session.createSQLQuery(sql);
			q.setCacheable(false);
			q.addEntity("Business", BusinessImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);
			qPos.add(groupId);

			if (Validator.isNotNull(businessDomain)) {
				qPos.add(businessDomain);
			}
			if (accountStatus != -1) {
				qPos.add(accountStatus);
			}

			if (keywords != null && keywords.length > 0) {

				qPos.add(keywords, 2);

				qPos.add(keywords, 2);

				qPos.add(keywords, 2);

			}

			return (List<Business>) QueryUtil.list(q, getDialect(), start, end);
		} catch (Exception e) {
			try {
				throw new SystemException();
			}
			catch (SystemException ex) {
				ex.printStackTrace();
			}
		} finally {
			session.close();
		}
		return null;
	}

	public int countBussiness(long groupId, String keywords, int accountStatus,
			String businessDomain) {

		String[] names = null;
		boolean andOperator = false;

		if (Validator.isNotNull(keywords)) {
			names = CustomSQLUtil.keywords(keywords);
		} else {
			andOperator = true;
		}

		return _countBussiness(groupId, names, accountStatus, businessDomain,
				andOperator);
	}

	/**
	 * @param groupId
	 * @param keywords
	 * @param accountStatus
	 * @param businessDomain
	 * @param andOperator
	 * @return
	 */
	private int _countBussiness(long groupId, String[] keywords,
			int accountStatus, String businessDomain, boolean andOperator) {

		/*
		 * keywords = CustomSQLUtil .keywords(keywords, false);
		 */

		Session session = null;
		try {
			session = openSession();
			String sql = CustomSQLUtil.get(COUNT_BUSINESS);
			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil.replaceKeywords(sql,
						"lower(opencps_acc_business.name)", StringPool.LIKE,
						true, keywords);

				sql = CustomSQLUtil.replaceKeywords(sql,
						"lower(opencps_acc_business.shortName))",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil.replaceKeywords(sql,
						"lower(opencps_acc_business.enName", StringPool.LIKE,
						true, keywords);
			} else {
				sql = StringUtil
						.replace(
								sql,
								"AND ((lower(opencps_acc_business.name) LIKE ? [$AND_OR_NULL_CHECK$])",
								StringPool.BLANK);

				sql = StringUtil
						.replace(
								sql,
								"OR (lower(opencps_acc_business.shortName) LIKE ? [$AND_OR_NULL_CHECK$])",
								StringPool.BLANK);

				sql = StringUtil
						.replace(
								sql,
								"OR (lower(opencps_acc_business.enName) LIKE ? [$AND_OR_NULL_CHECK$]))",
								StringPool.BLANK);
			}
			
			if (Validator.isNull(businessDomain)) {
				sql = StringUtil
						.replace(
								sql,
								"AND (opencps_acc_businessdomain.businessDomainCode = ?)",
								StringPool.BLANK);
				sql = StringUtil.replace(sql, 
						"INNER JOIN opencps_acc_businessdomain ON opencps_acc_business.businessId = opencps_acc_businessdomain.businessId"
						, StringPool.BLANK);

			}

			if (accountStatus == -1) {
				sql = StringUtil.replace(sql,
						"AND (opencps_acc_business.accountStatus = ?)",
						StringPool.BLANK);
			}
			sql = CustomSQLUtil.replaceAndOperator(sql, andOperator);
			SQLQuery q = session.createSQLQuery(sql);
			q.setCacheable(false);

			q.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);

			if (accountStatus != -1) {
				qPos.add(accountStatus);
			}

			if (Validator.isNotNull(businessDomain)) {
				qPos.add(businessDomain);
			}

			if (keywords != null && keywords.length > 0) {
				
				qPos.add(keywords, 2);
				
				qPos.add(keywords, 2);

				qPos.add(keywords, 2);

			}
			Iterator<Integer> itr = q.iterate();

			if (itr.hasNext()) {
				Integer count = itr.next();

				if (count != null) {
					return count.intValue();
				}
			}

		} catch (Exception e) {
			try {
				throw new SystemException();
			}
			catch (SystemException ex) {
				ex.printStackTrace();
			}
			
			
		} finally {
			session.close();
		}

		return 0;
	}

	private Log _log = LogFactoryUtil.getLog(BusinessFinderImpl.class);
}