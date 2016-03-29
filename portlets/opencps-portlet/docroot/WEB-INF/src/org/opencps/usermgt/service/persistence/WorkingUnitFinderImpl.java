
/*******************************************************************************
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/

package org.opencps.usermgt.service.persistence;

import java.util.Iterator;

import org.opencps.usermgt.model.WorkingUnit;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

/**
 * The persistence implementation for the working unit service.
 *
 * <p>
 * Caching information and settings can be found in
 * <code>portal.properties</code>
 * </p>
 *
 * @author trungnt
 * @see WorkingUnitPersistence
 * @see WorkingUnitUtil
 * @generated
 */
public class WorkingUnitFinderImpl extends BasePersistenceImpl<WorkingUnit>
		implements
			WorkingUnitFinder {
	public int findMaxSibling(long groupId) {

		Session session = null;

		try {

			session = openSession();

			String sql = CustomSQLUtil.get(FIND_MAX_SIBLING);

			SQLQuery q = session.createSQLQuery(sql);

			q.addScalar("SIBLING", Type.INTEGER);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);

			Iterator<Integer> itr = q.iterate();

			if (itr.hasNext()) {
				Integer siblingMax = itr.next();

				if (siblingMax != null) {
					return siblingMax.intValue();
				}
			}

			return 0;

		} catch (Exception e) {
			_log.error(e);
			return 0;
		} finally {
			closeSession(session);
		}

	}

	private static final String FIND_MAX_SIBLING = WorkingUnitFinder.class
			.getName() + ".findMaxSibling";

	private static Log _log = LogFactoryUtil
			.getLog(WorkingUnitFinderImpl.class.getName());

}