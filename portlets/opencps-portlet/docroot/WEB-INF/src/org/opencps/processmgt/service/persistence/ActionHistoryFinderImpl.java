/**
 * OpenCPS is the open source Core Public Services software Copyright (C)
 * 2016-present OpenCPS community This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3
 * of the License, or any later version. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details. You should have received a
 * copy of the GNU Affero General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.processmgt.service.persistence;

import java.util.List;

import org.opencps.processmgt.model.ActionHistory;
import org.opencps.processmgt.model.impl.ActionHistoryImpl;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

public class ActionHistoryFinderImpl extends BasePersistenceImpl<ActionHistory> implements ActionHistoryFinder {
	public final static String SQL_SEARCH_ACTION_HISTORY_BY_DOSSIERID =
					ActionHistoryFinder.class.getName() + ".searchActionHistoryByDossierId";

	/**
	 * Search action history with width dossierId
	 * 
	 * @param groupId
	 * @param dossierId
	 * @return
	 */
	public List<ActionHistory> searchActionHistoryByDossierId(
	    long groupId, long dossierId) {

		return _searchActionHistoryByDossierId(groupId, dossierId);

	}

	private List<ActionHistory> _searchActionHistoryByDossierId(

	long groupId, long dossierId) {

		Session session = null;
		try {
			session = openSession();

			String sql = CustomSQLUtil.get(SQL_SEARCH_ACTION_HISTORY_BY_DOSSIERID);

			SQLQuery q = session.createSQLQuery(sql);

			q.setCacheable(false);

			q.addEntity("ActionHistory", ActionHistoryImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);
			qPos.add(dossierId);
			
			return (List<ActionHistory>) q.list();
		}
		catch (Exception e) {
			try {
				throw new SystemException(e);
			}
			catch (SystemException se) {
				se.printStackTrace();
			}
		}
		finally {
			closeSession(session);
		}

		return null;

	}
	
}
