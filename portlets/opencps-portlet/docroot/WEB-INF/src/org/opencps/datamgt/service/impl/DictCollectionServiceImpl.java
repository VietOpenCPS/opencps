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

import org.opencps.datamgt.NoSuchDictCollectionException;
import org.opencps.datamgt.model.DictCollection;
import org.opencps.datamgt.service.base.DictCollectionServiceBaseImpl;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;

/**
 * The implementation of the dict collection remote service. <p> All custom
 * service methods should be put in this class. Whenever methods are added,
 * rerun ServiceBuilder to copy their definitions into the
 * {@link org.opencps.datamgt.service.DictCollectionService} interface. <p> This
 * is a remote service. Methods of this service are expected to have security
 * checks based on the propagated JAAS credentials because this service can be
 * accessed remotely. </p>
 *
 * @author khoavd
 * @author trungnt
 * @see org.opencps.datamgt.service.base.DictCollectionServiceBaseImpl
 * @see org.opencps.datamgt.service.DictCollectionServiceUtil
 */
public class DictCollectionServiceImpl extends DictCollectionServiceBaseImpl {

	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link org.opencps.datamgt.service.DictCollectionServiceUtil} to access
	 * the dict collection remote service.
	 */
	/**
	 * <p> Get DictCollection </p>
	 * 
	 * @param groupId
	 *            là mã UserGroup của người đăng nhập
	 * @param collectionCode
	 *            là mã của DictCollection
	 * @return trả về đối tượng DictCollection theo dictCollectionId
	 * @throws SystemException
	 *             Nếu ngoại lệ hệ thống xảy ra
	 * @throws NoSuchDictCollectionException
	 *             Khi xảy ra lỗi không tìm thấy DictCollection
	 */
	@JSONWebService(value = "get-dictcollection-by-gc")
	public DictCollection getDictCollection(long groupId, String collectionCode)
		throws NoSuchDictCollectionException, SystemException {

		return dictCollectionLocalService
			.getDictCollection(groupId, collectionCode);
	}
}
