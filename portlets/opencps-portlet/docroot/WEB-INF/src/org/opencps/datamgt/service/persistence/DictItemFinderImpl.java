
/**
 * OpenCPS is the open source Core Public Services software
 * Copyright (C) 2016-present OpenCPS community
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.datamgt.service.persistence;

import java.util.ArrayList;
import java.util.List;

import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.model.impl.DictItemImpl;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
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
 * @author binhth
 */
public class DictItemFinderImpl extends BasePersistenceImpl<DictItem> 
	implements DictItemFinder{
	
	public static final String SEARCH_DICT_ITEM_BY_NAME_LIKE = DictItemFinder.class
			.getName() + ".searchDictItemByNameLike";
	
	public static final String FIND_DICTITEMS_BY_G_DC_S = DictItemFinder.class
			.getName() + ".findDictItemsByG_DC_S";
	
	/**
	 * @param collectionCode
	 * @param itemCode
	 * @param keyword
	 * @param groupId
	 * @param start
	 * @param end
	 * @param obc
	 * @return
	 * @throws SystemException 
	 */
	public List<DictItem> searchDictItemByName_like(
			String collectionCode, String itemCode, String keyword, long groupId, 
			int start, int end, OrderByComparator obc) throws SystemException {

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
		return searchDictItemByName_like(collectionCode, itemCode, keywords,
				groupId, andOperator, start, end,
			obc);
	}

	private List<DictItem> searchDictItemByName_like(String collectionCode,
			String itemCode, String[] keywords, long groupId,
			boolean andOperator, int start, int end, OrderByComparator obc) throws SystemException {
		// TODO Auto-generated method stub
		Session session = null;
		
		List<DictItem> results = new ArrayList<DictItem>();

		try {
			session = openSession();

			String sql = CustomSQLUtil
				.get(SEARCH_DICT_ITEM_BY_NAME_LIKE);
			
			if(Validator.isNull(collectionCode)){
				sql = StringUtil
						.replace(sql,
							"and opencps_dictcollection.collectionCode = ?",
							StringPool.BLANK);
			}
			
			if(Validator.isNull(itemCode) || itemCode.equals("0")){
				sql = StringUtil
						.replace(sql,
							"and opencps_dictitem.itemCode = ?",
							StringPool.BLANK);
			}else{
				sql = StringUtil
						.replace(sql,
							"and opencps_dictitem.parentItemId = ?",
							StringPool.BLANK);
			}

			if (keywords != null && keywords.length > 0) {
	
				sql = CustomSQLUtil
					.replaceKeywords(sql,
						"lower(ExtractValue(itemName, '//ItemName'))",
						StringPool.LIKE, true, keywords);

			}
			else {
				sql = StringUtil
					.replace(sql,
						"and (lower(ExtractValue(itemName, '//ItemName')) LIKE ? [$AND_OR_NULL_CHECK$])",
						StringPool.BLANK);
			}

			

			sql = CustomSQLUtil
				.replaceAndOperator(sql, andOperator);
			
			_log.info("SQL autocomplete:" + sql);
			
			SQLQuery q = session
				.createSQLQuery(sql);

			q
				.addEntity("DictItem", DictItemImpl.class);

			QueryPos qPos = QueryPos
				.getInstance(q);

			if(Validator.isNotNull(collectionCode)){
				qPos.add(collectionCode);
			}
			
			qPos.add(itemCode);
			
			qPos.add(groupId);
			
			if (keywords != null && keywords.length > 0) {
				
				qPos.add(keywords, 2);

			}
			
			results = (List<DictItem>) QueryUtil.list(q, getDialect(), start, end);

		}
		catch (Exception e) {
			throw new SystemException();
		}
		finally {
			closeSession(session);
		}
		
		return results;

	}
	

	public List<DictItem> findDictItemsByG_DC_S(long groupId, String dictCollectionCode , int issueStatus) throws SystemException{
		
		return _findDictItemsByG_DC_S(groupId, dictCollectionCode, issueStatus);
	}
	
	private List<DictItem> _findDictItemsByG_DC_S(long groupId, String dictCollectionCode, Integer issueStatus) throws SystemException{
		
		Session session = null;
		
		try{
			session = openSession();
			
			String sql = CustomSQLUtil
					.get(FIND_DICTITEMS_BY_G_DC_S);
						
			if(Validator.isNull(dictCollectionCode)){
				sql = StringUtil.replace(sql, "AND (opencps_dictcollection.collectionCode = ?)", StringPool.BLANK);
			}
			
			if(issueStatus == null){
				sql = StringUtil.replace(sql, "AND (opencps_dictitem.issueStatus = ?)", StringPool.BLANK);
			}
			
			SQLQuery queryObject = session.createSQLQuery(sql);
			queryObject.setCacheable(false);
			queryObject.addEntity("DictItem", DictItemImpl.class);
			
			QueryPos qPos = QueryPos.getInstance(queryObject);
			qPos.add(groupId);
			
			if(Validator.isNotNull(dictCollectionCode)){
				qPos.add(dictCollectionCode);
			}
			
			if(issueStatus != null){
				qPos.add(issueStatus);
			}
			
			return (List<DictItem>) queryObject.list();
			
		}catch (Exception e) {
			throw new SystemException(e);
			
		} finally {
			
			closeSession(session);
		}
	}
			
	
	private static Log _log = LogFactoryUtil.getLog(DictItemFinderImpl.class.getName());
	
}
