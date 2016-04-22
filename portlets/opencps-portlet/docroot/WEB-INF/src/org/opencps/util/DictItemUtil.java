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

package org.opencps.util;

import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.service.DictItemLocalServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author khoavd
 */
public class DictItemUtil {

	/**
	 * Get DictItem by itemId
	 * 
	 * @param itemId
	 * @return
	 */
	public static String getNameDictItem(String itemId) {

		String dictItemName = StringPool.BLANK;

		long dictItemId = GetterUtil.getLong(itemId, 0);

		try {
			DictItem di = DictItemLocalServiceUtil.fetchDictItem(dictItemId);

			if (Validator.isNotNull(di)) {
				dictItemName = di.getItemName();
			}
		}
		catch (Exception e) {
			_log.error(e);
		}

		return dictItemName;
	}

	private static Log _log = LogFactoryUtil.getLog(DictItemUtil.class);
}
