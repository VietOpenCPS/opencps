
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.liferay.portal.kernel.exception.SystemException;
import org.opencps.dossiermgt.bean.DossierFileBean;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.impl.DossierFileImpl;
import org.opencps.dossiermgt.util.DossierMgtUtil;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

/**
 * @author trungnt
 */
public class DossierFileFinderImpl extends BasePersistenceImpl<DossierFile>
	implements DossierFileFinder {

	public static final String COUNT_DOSSIER_FILE_ADVANCE =
		DossierFileFinder.class
			.getName() + ".countDossierFileAdvance";

	public static final String COUNT_DOSSIER_FILE_BY_G_K_T_R =
		DossierFileFinder.class
			.getName() + ".countDossierFileByG_K_T_R";

	public static final String COUNT_DOSSIER_FILE_TEMPLATE =
		DossierFileFinder.class
			.getName() + ".countDossierFile";

	public static final String SEARCH_DOSSIER_FILE_ADVANCE =
		DossierFileFinder.class
			.getName() + ".searchDossierFileAdvance";

	public static final String SEARCH_DOSSIER_FILE_BY_G_K_T_R =
		DossierFileFinder.class
			.getName() + ".searchDossierFileByG_K_T_R";

	public static final String SEARCH_DOSSIER_FILE_TEMPLATE =
		DossierFileFinder.class
			.getName() + ".searchDossierFile";
	
	public static final String SEARCH_DOSSIER_FILE_RESULT =
					DossierFileFinder.class
						.getName() + ".searchDossierFileResult";

	private Log _log = LogFactoryUtil
		.getLog(DossierFileFinder.class
			.getName());

	/**
	 * @param groupId
	 * @param keyword
	 * @param templateFileNo
	 * @param removed
	 * @return
	 */
	public int countDossierFile(
		long groupId, long ownerUserId, long ownerOrganizationId,
		String keyword, String templateFileNo, int removed) {

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

		return countDossierFile(groupId, ownerUserId, ownerOrganizationId,
			keywords, templateFileNo, removed, andOperator);
	}

	/**
	 * @param groupId
	 * @param keywords
	 * @param templateFileNo
	 * @param removed
	 * @param andOperator
	 * @return
	 */
	private int countDossierFile(
		long groupId, long ownerUserId, long ownerOrganizationId,
		String[] keywords, String templateFileNo, int removed,
		boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(COUNT_DOSSIER_FILE_BY_G_K_T_R);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossier_file.displayName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossier_file.dossierFileNo)",
						StringPool.LIKE, true, keywords);
			}

			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
					.replace(sql,
						"AND (lower(opencps_dossier_file.displayName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_dossier_file.dossierFileNo) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
			}

			if (Validator
				.isNull(templateFileNo)) {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossier_file.templateFileNo = ?)",
						StringPool.BLANK);
			}

			if (ownerUserId <= 0) {
				sql = StringUtil
					.replace(sql, "AND (opencps_dossier_file.ownerUserId = ?)",
						StringPool.BLANK);
			}

			if (ownerOrganizationId <= 0) {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossier_file.ownerOrganizationId = ?)",
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
				.add(removed);

			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
			}

			if (Validator
				.isNotNull(templateFileNo)) {
				qPos
					.add(templateFileNo);
			}

			if (ownerUserId > 0) {
				qPos
					.add(ownerUserId);
			}

			if (ownerOrganizationId > 0) {
				qPos
					.add(ownerOrganizationId);
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
	 * @param dossierTemplateId
	 * @param fileEntryId
	 * @param onlyViewFileResult
	 * @return
	 */
	public int countDossierFile(
		long groupId, String keyword, long dossierTemplateId, long fileEntryId,
		boolean onlyViewFileResult) {

		String[] keywords = null;
		int dossierFileType = DossierMgtUtil.DOSSIERFILETYPE_ALL;
		boolean andOperator = false;
		if (Validator
			.isNotNull(keyword)) {
			keywords = CustomSQLUtil
				.keywords(keyword);
		}
		else {
			andOperator = true;
		}
		if (onlyViewFileResult) {
			dossierFileType = DossierMgtUtil.DOSSIERFILETYPE_OUTPUT;
		}
		return countDossierFile(groupId, keywords, dossierTemplateId,
			dossierFileType, fileEntryId, andOperator);
	}

	/**
	 * @param groupId
	 * @param keywords
	 * @param dossierTemplateId
	 * @param dossierFileType
	 * @param fileEntryId
	 * @param andOperator
	 * @return
	 */
	private int countDossierFile(
		long groupId, String[] keywords, long dossierTemplateId,
		int dossierFileType, long fileEntryId, boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(COUNT_DOSSIER_FILE_TEMPLATE);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossier_file.displayName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossier_file.dossierFileNo)",
						StringPool.LIKE, true, keywords);
			}

			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
					.replace(sql,
						"AND (lower(opencps_dossier_file.displayName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_dossier_file.dossierFileNo) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
			}

			if (dossierTemplateId != 0) {
			}
			else {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossierpart.dossierTemplateId = ?)",
						StringPool.BLANK);
			}

			if (dossierFileType != DossierMgtUtil.DOSSIERFILETYPE_ALL) {
			}
			else {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossier_file.dossierFileType = ?)",
						StringPool.BLANK);
			}

			if (fileEntryId < 0) {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossier_file.fileEntryId = ?)",
						StringPool.BLANK);
			}
			else {

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

			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
			}

			if (fileEntryId > 0) {
				qPos
					.add(fileEntryId);
			}

			if (dossierFileType != DossierMgtUtil.DOSSIERFILETYPE_ALL) {
				qPos
					.add(dossierFileType);
			}

			if (dossierTemplateId > 0) {
				qPos
					.add(dossierTemplateId);
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
	 * @param ownerUserId
	 * @param ownerOrganizationId
	 * @param keyword
	 * @param templateFileNo
	 * @param removed
	 * @param partType
	 * @param original
	 * @return
	 */
	public int countDossierFileAdvance(
		long groupId, long ownerUserId, long ownerOrganizationId,
		String keyword, String templateFileNo, int removed, int partType,
		int original) {

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

		return countDossierFileAdvance(groupId, ownerUserId,
			ownerOrganizationId, keywords, templateFileNo, removed, partType,
			original, andOperator);
	}

	/**
	 * @param groupId
	 * @param ownerUserId
	 * @param ownerOrganizationId
	 * @param keywords
	 * @param templateFileNo
	 * @param removed
	 * @param partType
	 * @param original
	 * @param andOperator
	 * @return
	 */
	private int countDossierFileAdvance(
		long groupId, long ownerUserId, long ownerOrganizationId,
		String[] keywords, String templateFileNo, int removed, int partType,
		int original, boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(COUNT_DOSSIER_FILE_ADVANCE);

			if (partType < 0) {
				sql = StringUtil
					.replace(sql, "AND opencps_dossierpart.dossierpartId = ?",
						StringPool.BLANK);
			}

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossier_file.displayName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossier_file.dossierFileNo)",
						StringPool.LIKE, true, keywords);
			}

			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
					.replace(sql,
						"AND (lower(opencps_dossier_file.displayName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_dossier_file.dossierFileNo) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
			}

			if (Validator
				.isNull(templateFileNo)) {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossier_file.templateFileNo = ?)",
						StringPool.BLANK);
			}

			if (ownerUserId <= 0) {
				sql = StringUtil
					.replace(sql, "AND (opencps_dossier_file.ownerUserId = ?)",
						StringPool.BLANK);
			}

			if (ownerOrganizationId <= 0) {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossier_file.ownerOrganizationId = ?)",
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

			if (partType >= 0) {
				qPos
					.add(partType);
			}

			qPos
				.add(groupId);

			qPos
				.add(removed);

			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
			}

			if (Validator
				.isNotNull(templateFileNo)) {
				qPos
					.add(templateFileNo);
			}

			qPos
				.add(original);

			if (ownerUserId > 0) {
				qPos
					.add(ownerUserId);
			}

			if (ownerOrganizationId > 0) {
				qPos
					.add(ownerOrganizationId);
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

	public List<DossierFile> searchDossierFile(
		long groupId, long ownerUserId, long ownerOrganizationId,
		String keyword, String templateFileNo, int removed, int start, int end,
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

		return searchDossierFile(groupId, ownerUserId, ownerOrganizationId,
			keywords, templateFileNo, removed, start, end, obc, andOperator);
	}

	/**
	 * @param groupId
	 * @param keywords
	 * @param templateFileNo
	 * @param removed
	 * @param start
	 * @param end
	 * @param obc
	 * @param andOperator
	 * @return
	 */
	private List<DossierFile> searchDossierFile(
		long groupId, long ownerUserId, long ownerOrganizationId,
		String[] keywords, String templateFileNo, int removed, int start,
		int end, OrderByComparator obc, boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(SEARCH_DOSSIER_FILE_BY_G_K_T_R);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossier_file.displayName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossier_file.dossierFileNo)",
						StringPool.LIKE, true, keywords);
			}

			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
					.replace(sql,
						"AND (lower(opencps_dossier_file.displayName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_dossier_file.dossierFileNo) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
			}

			if (Validator
				.isNull(templateFileNo)) {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossier_file.templateFileNo = ?)",
						StringPool.BLANK);
			}

			if (ownerUserId <= 0) {
				sql = StringUtil
					.replace(sql, "AND (opencps_dossier_file.ownerUserId = ?)",
						StringPool.BLANK);
			}

			if (ownerOrganizationId <= 0) {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossier_file.ownerOrganizationId = ?)",
						StringPool.BLANK);
			}

			sql = CustomSQLUtil
				.replaceAndOperator(sql, andOperator);
			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.addEntity("DossierFile", DossierFileImpl.class);

			QueryPos qPos = QueryPos
				.getInstance(q);

			qPos
				.add(groupId);

			qPos
				.add(removed);

			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
			}

			if (Validator
				.isNotNull(templateFileNo)) {
				qPos
					.add(templateFileNo);
			}

			if (ownerUserId > 0) {
				qPos
					.add(ownerUserId);
			}

			if (ownerOrganizationId > 0) {
				qPos
					.add(ownerOrganizationId);
			}

			return (List<DossierFile>) QueryUtil
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
	
	
	/**
	 * @param removed
	 * @param dossierId
	 * @param syncStatus
	 * @param dossierPartResultType
	 * @param dossierPartMultiResultType
	 * @param start
	 * @param end
	 * @param orderByComparator
	 * @return
	 * @throws SystemException
	 */
	public List<DossierFile> searchDossierFileResult(int removed, long dossierId,
		int syncStatus,int dossierPartResultType, int dossierPartMultiResultType , 
		int start, int end, OrderByComparator orderByComparator) throws SystemException {
		
		return _searchDossierFileResult(removed, dossierId, syncStatus,dossierPartResultType,
			dossierPartMultiResultType,start, end, orderByComparator);
	}
	
	
	/**
	 * @param removed
	 * @param dossierId
	 * @param syncStatus
	 * @param dossierPartResultType
	 * @param dossierPartMultiResultType
	 * @param start
	 * @param end
	 * @param orderByComparator
	 * @return
	 * @throws SystemException
	 */
	private List<DossierFile> _searchDossierFileResult (int removed, long dossierId,
		int syncStatus,int dossierPartResultType, int dossierPartMultiResultType , 
		int start, int end, OrderByComparator orderByComparator) throws SystemException {
		
		Session session = null;
		
		try {
			session = openSession();
			
			String sql = CustomSQLUtil.get(SEARCH_DOSSIER_FILE_RESULT);
			
			sql = CustomSQLUtil.replaceOrderBy(sql, orderByComparator);
			SQLQuery q = session.createSQLQuery(sql);
			
			q.setCacheable(false);
			q.addEntity("DossierFile", DossierFileImpl.class);
			
			QueryPos qPos = QueryPos.getInstance(q);
			
			qPos.add(removed);
			qPos.add(dossierId);
			qPos.add(syncStatus);
			qPos.add(dossierPartResultType);
			qPos.add(dossierPartMultiResultType);
			
			return (List<DossierFile>) QueryUtil
						.list(q, getDialect(), start, end);
		}
		catch (Exception e) {
			_log.error(e);
			throw new SystemException();
		} finally {
			closeSession(session);
		}
	}

	/**
	 * @param groupId
	 * @param keyword
	 * @param dossierTemplateId
	 * @param fileEntryId
	 * @param onlyViewFileResult
	 * @param start
	 * @param end
	 * @param obc
	 * @return
	 */
	public List<DossierFile> searchDossierFile(
		long groupId, String keyword, long dossierTemplateId, long fileEntryId,
		boolean onlyViewFileResult, int start, int end, OrderByComparator obc) {

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
		int dossierFileType = DossierMgtUtil.DOSSIERFILETYPE_ALL;
		if (onlyViewFileResult) {
			dossierFileType = DossierMgtUtil.DOSSIERFILETYPE_OUTPUT;
		}
		return searchDossierFile(groupId, keywords, dossierTemplateId,
			dossierFileType, fileEntryId, start, end, obc, andOperator);
	}

	/**
	 * @param groupId
	 * @param keywords
	 * @param dossierTemplateId
	 * @param dossierFileType
	 * @param fileEntryId
	 * @param start
	 * @param end
	 * @param obc
	 * @param andOperator
	 * @return
	 */
	private List<DossierFile> searchDossierFile(
		long groupId, String[] keywords, long dossierTemplateId,
		int dossierFileType, long fileEntryId, int start, int end,
		OrderByComparator obc, boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(SEARCH_DOSSIER_FILE_TEMPLATE);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossier_file.displayName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossier_file.dossierFileNo)",
						StringPool.LIKE, true, keywords);
			}

			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
					.replace(sql,
						"AND (lower(opencps_dossier_file.displayName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_dossier_file.dossierFileNo) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
			}

			if (dossierTemplateId != 0) {
			}
			else {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossierpart.dossierTemplateId = ?)",
						StringPool.BLANK);
			}

			if (dossierFileType != DossierMgtUtil.DOSSIERFILETYPE_ALL) {
			}
			else {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossier_file.dossierFileType = ?)",
						StringPool.BLANK);
			}

			if (fileEntryId < 0) {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossier_file.fileEntryId = ?)",
						StringPool.BLANK);
			}
			else {

			}

			sql = CustomSQLUtil
				.replaceAndOperator(sql, andOperator);
			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.addEntity("DossierFile", DossierFileImpl.class);

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

			if (fileEntryId > 0) {
				qPos
					.add(fileEntryId);
			}

			if (dossierFileType != DossierMgtUtil.DOSSIERFILETYPE_ALL) {
				qPos
					.add(dossierFileType);
			}

			if (dossierTemplateId > 0) {
				qPos
					.add(dossierTemplateId);
			}
			return (List<DossierFile>) QueryUtil
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

	public List searchDossierFileAdvance(
		long groupId, long ownerUserId, long ownerOrganizationId,
		String keyword, String templateFileNo, int removed, int partType,
		int original, int start, int end, OrderByComparator obc) {

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

		return searchDossierFileAdvance(groupId, ownerUserId,
			ownerOrganizationId, keywords, templateFileNo, removed, partType,
			original, start, end, obc, andOperator);
	}

	/**
	 * @param groupId
	 * @param ownerUserId
	 * @param ownerOrganizationId
	 * @param keywords
	 * @param templateFileNo
	 * @param removed
	 * @param partType
	 * @param original
	 * @param start
	 * @param end
	 * @param obc
	 * @param andOperator
	 * @return
	 */
	private List<DossierFileBean> searchDossierFileAdvance(
		long groupId, long ownerUserId, long ownerOrganizationId,
		String[] keywords, String templateFileNo, int removed, int partType,
		int original, int start, int end, OrderByComparator obc,
		boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(SEARCH_DOSSIER_FILE_ADVANCE);

			if (partType < 0) {
				sql = StringUtil
					.replace(sql, "AND opencps_dossierpart.dossierpartId = ?",
						StringPool.BLANK);
			}

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossier_file.displayName)",
						StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(opencps_dossier_file.dossierFileNo)",
						StringPool.LIKE, true, keywords);
			}

			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
					.replace(sql,
						"AND (lower(opencps_dossier_file.displayName) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);

				sql = StringUtil
					.replace(sql,
						"OR (lower(opencps_dossier_file.dossierFileNo) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
			}

			if (Validator
				.isNull(templateFileNo)) {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossier_file.templateFileNo = ?)",
						StringPool.BLANK);
			}

			if (ownerUserId <= 0) {
				sql = StringUtil
					.replace(sql, "AND (opencps_dossier_file.ownerUserId = ?)",
						StringPool.BLANK);
			}

			if (ownerOrganizationId <= 0) {
				sql = StringUtil
					.replace(sql,
						"AND (opencps_dossier_file.ownerOrganizationId = ?)",
						StringPool.BLANK);
			}

			sql = CustomSQLUtil
				.replaceAndOperator(sql, andOperator);
			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.addEntity("DossierFile", DossierFileImpl.class);

			q
				.addScalar("serviceInfoId", Type.LONG);
			q
				.addScalar("receptionNo", Type.STRING);
			q
				.addScalar("partType", Type.INTEGER);

			QueryPos qPos = QueryPos
				.getInstance(q);

			if (partType >= 0) {
				qPos
					.add(partType);
			}

			qPos
				.add(groupId);

			qPos
				.add(removed);

			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
			}

			if (Validator
				.isNotNull(templateFileNo)) {
				qPos
					.add(templateFileNo);
			}

			qPos
				.add(original);

			if (ownerUserId > 0) {
				qPos
					.add(ownerUserId);
			}

			if (ownerOrganizationId > 0) {
				qPos
					.add(ownerOrganizationId);
			}

			Iterator<Object[]> itr = (Iterator<Object[]>) QueryUtil
				.list(q, getDialect(), start, end).iterator();

			List<DossierFileBean> dossierFileBeans =
				new ArrayList<DossierFileBean>();

			if (itr
				.hasNext()) {
				while (itr
					.hasNext()) {
					DossierFileBean dossierFileBean = new DossierFileBean();

					Object[] objects = itr
						.next();

					DossierFile dossierFile = (DossierFile) objects[0];

					long serviceInfoId = GetterUtil
						.getLong(objects[1]);

					String receptionNo = (String) objects[2];

					int dossierPartType = GetterUtil
						.getInteger(objects[3]);

					dossierFileBean
						.setDossierFileId(dossierFile
							.getDossierFileId());

					dossierFileBean
						.setDossierFile(dossierFile);
					dossierFileBean
						.setDossierId(dossierFile
							.getDossierId());
					dossierFileBean
						.setPartType(dossierPartType);
					dossierFileBean
						.setServiceInfoId(serviceInfoId);

					dossierFileBean
						.setReceptionNo(receptionNo);

					dossierFileBeans
						.add(dossierFileBean);
				}
			}

			return dossierFileBeans;
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
