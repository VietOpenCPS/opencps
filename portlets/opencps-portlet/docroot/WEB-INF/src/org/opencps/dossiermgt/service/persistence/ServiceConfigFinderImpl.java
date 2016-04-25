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

import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.dossiermgt.model.impl.ServiceConfigImpl;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

/**
 * @author dunglt
 */

public class ServiceConfigFinderImpl extends BasePersistenceImpl<ServiceConfig> 
implements ServiceConfigFinder{
	public static final String SEARCH_SERVICE_CONFIG_SQL =
					ServiceConfigFinder.class.getName() + ".searchServiceConfig";

				public static final String COUNT_SERVICE_CONFIG_SQL =
					ServiceConfigFinder.class.getName() + ".countServiceConfig";
	public int countServiceConfig(long groupId, String keywords, String govAgencyCode,
		String domainCode) {
		String[] names = null;
		boolean andOperator = false;
		
		if (Validator.isNotNull(keywords)) {
			names = CustomSQLUtil.keywords(keywords);
		}
		else {
			andOperator = true;
		}
		
		return _countServiceConfig( groupId,  names,  govAgencyCode,
			 domainCode, andOperator);
	}
	
	public List<ServiceConfig> searchServiceConfig(long groupId, String keywords, String govAgencyCode,
		String domainCode, int start, int end) {
		
		String[] names = null;
		boolean andOperator = false;
		
		if (Validator.isNotNull(keywords)) {
			names = CustomSQLUtil.keywords(keywords);
		}
		else {
			andOperator = true;
		}
		
		return _searchServiceConfig( groupId,  names,  govAgencyCode,
		 domainCode, andOperator ,start, end);
	}
	
	/**
	 * @param groupId
	 * @param keywords
	 * @param govAgencyCode
	 * @param domainCode
	 * @param andOperator
	 * @param start
	 * @param end
	 * @return
	 */
	private List<ServiceConfig>_searchServiceConfig(long groupId, String [] keywords, String govAgencyCode,
		String domainCode, boolean andOperator , int start, int end) {
		
		keywords = CustomSQLUtil.keywords(keywords);
		
		Session session = null;
		
		try {
	        session = openSession();
	        //get sql command from sql xml
	        String sql = CustomSQLUtil.get(SEARCH_SERVICE_CONFIG_SQL);
	        
	        sql = CustomSQLUtil.replaceKeywords(sql, 
	        	"lower(opencps_service_config.govAgencyName", 
	        	StringPool.LIKE, true, keywords);
	        
	        sql = CustomSQLUtil.replaceAndOperator(sql, andOperator);
	        
	        // remove condition query
	        if(govAgencyCode.equals(StringPool.BLANK)) {
	        	sql = StringUtil.replace(sql, 
	        		"AND (opencps_service_config.govAgencyCode = ?)", StringPool.BLANK);
	        }
	        
	        if(domainCode.equals("0") || domainCode.equals(StringPool.BLANK)) {
	        	sql = StringUtil.replace(sql, 
	        		"AND (opencps_service_config.domainCode = ?)", StringPool.BLANK);
	        }
	        
	        SQLQuery q = session.createSQLQuery(sql);
	        
	        q.setCacheable(false);
	        q.addEntity("ServiceConfig", ServiceConfigImpl.class);
	        
	        QueryPos qPos = QueryPos.getInstance(q);
	        
	        qPos.add(groupId);
	        qPos.add(keywords, 2);
	        
	        if(!govAgencyCode.equals(StringPool.BLANK)) {
	        	qPos.add(govAgencyCode);
	        }
	        
	        if(!domainCode.equals(StringPool.BLANK) && !domainCode.equals("0")) {
	        	qPos.add(domainCode);
	        }
			
	        
			return (List<ServiceConfig>) QueryUtil.list(
			    q, getDialect(), start, end);
        }
        catch (Exception e) {
        	try {
				throw new SystemException(e);
			}
			catch (SystemException se) {
				se.printStackTrace();
			}
        } finally {
        	session.close();
        }
		
		return null;
	}
	
	/**
	 * @param groupId
	 * @param keywords
	 * @param govAgencyCode
	 * @param domainCode
	 * @param andOperator
	 * @return
	 */
	private int _countServiceConfig(long groupId, String [] keywords, String govAgencyCode,
		String domainCode, boolean andOperator) {
	
		keywords = CustomSQLUtil.keywords(keywords);
		
		Session session = null;
		
		try {
	        session = openSession();
	        //get sql command from sql xml
	        String sql = CustomSQLUtil.get(COUNT_SERVICE_CONFIG_SQL);
	        
	        sql = CustomSQLUtil.replaceKeywords(sql, 
	        	"lower(opencps_service_config.govAgencyName", 
	        	StringPool.LIKE, true, keywords);
	        
	        sql = CustomSQLUtil.replaceAndOperator(sql, andOperator);
	        
	        // remove condition query
	        if(govAgencyCode.equals(StringPool.BLANK)) {
	        	sql = StringUtil.replace(sql, 
	        		"AND (opencps_service_config.govAgencyCode = ?)", StringPool.BLANK);
	        }
	        
	        if(domainCode.equals("0") || domainCode.equals(StringPool.BLANK)) {
	        	sql = StringUtil.replace(sql, 
	        		"AND (opencps_service_config.domainCode = ?)", StringPool.BLANK);
	        }
	        
	        SQLQuery q = session.createSQLQuery(sql);
	        
	        q.setCacheable(false);
	        q.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);
	        
	        QueryPos qPos = QueryPos.getInstance(q);
	        
	        qPos.add(groupId);
	        qPos.add(keywords, 2);
	        
	        if(!govAgencyCode.equals(StringPool.BLANK)) {
	        	qPos.add(govAgencyCode);
	        }
	        
	        if(!domainCode.equals(StringPool.BLANK) && !domainCode.equals("0")) {
	        	qPos.add(domainCode);
	        }
	        
	        Iterator<Integer> itr = q.iterate();

			if (itr.hasNext()) {
				Integer count = itr.next();

				if (count != null) {
					return count.intValue();
				}
			}
			
			
        }
        catch (Exception e) {
        	try {
				throw new SystemException(e);
			}
			catch (SystemException se) {
				se.printStackTrace();
			}
        } finally {
        	session.close();
        }
		return 0;
	}
}
