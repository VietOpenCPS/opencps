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

package org.opencps.datamgt.service.impl;

import java.util.List;
import java.util.Locale;

import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.service.base.DictItemServiceBaseImpl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.security.ac.AccessControlled;

/**
 * The implementation of the dict item remote service. <p> All custom service
 * methods should be put in this class. Whenever methods are added, rerun
 * ServiceBuilder to copy their definitions into the
 * {@link org.opencps.datamgt.service.DictItemService} interface. <p> This is a
 * remote service. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be accessed
 * remotely. </p>
 *
 * @author trungnt
 * @see org.opencps.datamgt.service.base.DictItemServiceBaseImpl
 * @see org.opencps.datamgt.service.DictItemServiceUtil
 */
public class DictItemServiceImpl extends DictItemServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link org.opencps.datamgt.service.DictItemServiceUtil} to access the
	 * dict item remote service.
	 */

	@JSONWebService(value = "get-dictitem-by-pk")
	@AccessControlled(guestAccessEnabled = true)
	public DictItem getDictItem(long dictItemId)
		throws SystemException, PortalException {

		return dictItemLocalService
			.getDictItem(dictItemId);
	}

	@JSONWebService(value = "get-dictitems-by-parentId")
	@AccessControlled(guestAccessEnabled = true)
	public List<DictItem> getDictItemsByParentItemId(long parentItemId)
		throws SystemException {

		return dictItemLocalService
			.getDictItemsByParentItemId(parentItemId);
	}

	@JSONWebService(value = "get-dictitems-by-dictcollectionId")
	@AccessControlled(guestAccessEnabled = true)
	public List<DictItem> getDictItemsByDictCollectionId(long dictCollectionId)
		throws SystemException {

		return dictItemLocalService
			.getDictItemsByDictCollectionId(dictCollectionId);
	}

	@JSONWebService(value = "get-dictitems-inuse-by-dictcollectionId_parentItemId")
	@AccessControlled(guestAccessEnabled = true)
	public List<DictItem> getDictItemsInUseByDictCollectionIdAndParentItemId(
		long dictCollectionId, long parentItemId)
		throws SystemException {

		return dictItemLocalService
			.getDictItemsInUseByDictCollectionIdAndParentItemId(
				dictCollectionId, parentItemId);
	}
	
	@JSONWebService(value = "get-dictitems-inuse-by-dictcollectionId_parentItemId_datasource")
	@AccessControlled(guestAccessEnabled = true)
	public JSONObject getDictItemsInUseByDictCollectionIdAndParentItemIdDataSource(
		long dictCollectionId, long parentItemId)
		throws SystemException {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
		List<DictItem> result = dictItemLocalService
			.getDictItemsInUseByDictCollectionIdAndParentItemId(
				dictCollectionId, parentItemId);
		for (DictItem dictItem : result) {
			jsonObject.put(String.valueOf(dictItem.getDictItemId()),dictItem.getItemName(Locale.getDefault()));
        }
		return jsonObject;
	}
}
