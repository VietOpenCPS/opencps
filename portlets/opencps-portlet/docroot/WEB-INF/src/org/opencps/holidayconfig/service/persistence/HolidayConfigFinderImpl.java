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
package org.opencps.holidayconfig.service.persistence;

import java.util.ArrayList;
import java.util.List;

import org.opencps.holidayconfig.model.HolidayConfig;
import org.opencps.holidayconfig.model.impl.HolidayConfigImpl;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

public class HolidayConfigFinderImpl extends BasePersistenceImpl<HolidayConfig> implements HolidayConfigFinder {
	
	public final static String SQL_HOLIDAYCONFIG_FINDER =
			HolidayConfigFinder.class.getName() + ".getHolidayConfig";
	
	private static Log _log = LogFactoryUtil.getLog(HolidayConfigImpl.class);
	
	public List<HolidayConfig> getHolidayConfig(int remove) throws SystemException{
		
		List<HolidayConfig> holidayConfigList = new ArrayList<HolidayConfig>();

		Session session = null;
		
		try {
			session = openSession();
			
			String sql = CustomSQLUtil.get(SQL_HOLIDAYCONFIG_FINDER);
				
			SQLQuery query = session.createSQLQuery(sql);
			
			query.setCacheable(true);
			query.addEntity("HolidayConfig", HolidayConfigImpl.class);
			
			QueryPos qPos = QueryPos.getInstance(query);
			
			qPos.add(remove);
			
			holidayConfigList = (List<HolidayConfig>) query.list();

		} catch (Exception e) {
			throw new SystemException(e);
		} finally {
			closeSession(session);
		}
		
		return holidayConfigList;
	}
	
	
}