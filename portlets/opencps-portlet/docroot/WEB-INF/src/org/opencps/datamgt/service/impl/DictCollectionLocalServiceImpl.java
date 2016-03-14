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

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.opencps.datamgt.NoSuchDictCollectionException;
import org.opencps.datamgt.model.DictCollection;
import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.model.DictVersion;
import org.opencps.datamgt.service.base.DictCollectionLocalServiceBaseImpl;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the dict collection local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link org.opencps.datamgt.service.DictCollectionLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author khoavd
 * @see org.opencps.datamgt.service.base.DictCollectionLocalServiceBaseImpl
 * @see org.opencps.datamgt.service.DictCollectionLocalServiceUtil
 */
public class DictCollectionLocalServiceImpl extends DictCollectionLocalServiceBaseImpl {

	/**
	 * <p>
	 * Add DictCollection
	 * </p>
	 * 
	 * @param userId
	 *            là id của người đăng nhập
	 * @param collectionCode
	 *            là thuộc tính mã của DictCollection
	 * @param collectionName
	 *            là thuộc tính tên của DictCollection
	 * @param serviceContext
	 *            Có thể lấy ra các userId, GroupId, CompanyId
	 * @param description
	 *            Mô tả
	 * @return trả về đối tượng DictCollection
	 * @throws SystemException
	 *             Nếu ngoại lệ hệ thống xảy ra
	 */

	public DictCollection addDictCollection(long userId, String collectionCode, Map<Locale, String> collectionNameMap,
			String description, ServiceContext serviceContext) throws SystemException {
		long dictCollectionId = CounterLocalServiceUtil.increment(DictCollection.class.getName());
		DictCollection dictCollection = dictCollectionPersistence.create(dictCollectionId);
		Date curDate = new Date();
		dictCollection.setCompanyId(serviceContext.getCompanyId());
		dictCollection.setGroupId(serviceContext.getScopeGroupId());
		dictCollection.setUserId(userId);
		dictCollection.setCreateDate(curDate);
		dictCollection.setModifiedDate(curDate);
		dictCollection.setCollectionCode(collectionCode);
		dictCollection.setCollectionNameMap(collectionNameMap);
		dictCollection.setDescription(description);
		return dictCollectionPersistence.update(dictCollection);
	}

	/**
	 * <p>
	 * Xoa ban ghi DictCollection. Kiem tra truong hop neu ton tai ban ghi lien
	 * quan trong bang DictVersion va DictItem thi khong xoa
	 * </p>
	 * 
	 * @param dictCollectionId
	 *            là DictCollection
	 * @throws SystemException
	 *             Nếu ngoại lệ hệ thống xảy ra
	 * @throws NoSuchDictCollectionException
	 *             Khi xảy ra lỗi không tìm thấy DictCollection
	 */

	public void deleteCollection(long dictCollectionId) throws NoSuchDictCollectionException, SystemException {
		List<DictVersion> lstDicVersion = dictVersionPersistence.findByDictCollectionId(dictCollectionId);
		List<DictItem> lstDictItem = dictItemPersistence.findByDictCollectionId(dictCollectionId);
		if (lstDictItem.isEmpty() && lstDicVersion.isEmpty()) {
			dictCollectionPersistence.remove(dictCollectionId);
		}

	}

	public int countAll() throws SystemException {
		return dictCollectionPersistence.countAll();
	}

	public int countDictCollection(long groupId, String[] collectionNames) throws SystemException {
		return dictCollectionPersistence.countByG_N(groupId, collectionNames);
	}

	/**
	 * <p>
	 * Cap nhat DictCollection
	 * </p>
	 * 
	 * @param dictCollectionId
	 *            là id của DictCollection
	 * @param userId
	 *            là id của người đăng nhập
	 * @param collectionCode
	 *            là thuộc tính mã của DictCollection
	 * @param collectionName
	 *            là thuộc tính tên của DictCollection
	 * @param serviceContext
	 *            Có thể lấy ra các userId, GroupId, CompanyId
	 * @param description
	 *            Mô tả
	 * @return trả về đối tượng DictCollection
	 * @throws SystemException
	 *             Nếu ngoại lệ hệ thống xảy ra
	 * @throws NoSuchDictCollectionException
	 *             Khi xảy ra lỗi không tìm thấy DictCollection
	 */
	public DictCollection updateDictCollection(long dictCollectionId, long userId, String collectionCode,
			Map<Locale, String> collectionNameMap, String description, ServiceContext serviceContext)
			throws NoSuchDictCollectionException, SystemException {
		DictCollection dictCollection = dictCollectionPersistence.findByPrimaryKey(dictCollectionId);
		Date curDate = new Date();
		dictCollection.setCompanyId(serviceContext.getCompanyId());
		dictCollection.setGroupId(serviceContext.getScopeGroupId());
		dictCollection.setUserId(userId);
		dictCollection.setCreateDate(curDate);
		dictCollection.setModifiedDate(curDate);
		dictCollection.setCollectionCode(collectionCode);
		dictCollection.setCollectionNameMap(collectionNameMap);
		dictCollection.setDescription(description);
		return dictCollectionPersistence.update(dictCollection);
	}

	/**
	 * <p>
	 * Lay mot doi tuong DictCollection
	 * </p>
	 * 
	 * @param dictCollectionId
	 *            là id của DictCollection
	 * @return trả về đối tượng DictCollection theo dictCollectionId
	 * @throws SystemException
	 *             Nếu ngoại lệ hệ thống xảy ra
	 * @throws NoSuchDictCollectionException
	 *             Khi xảy ra lỗi không tìm thấy DictCollection
	 */
	public DictCollection getDictCollection(long dictCollectionId)
			throws NoSuchDictCollectionException, SystemException {
		return dictCollectionPersistence.findByPrimaryKey(dictCollectionId);
	}

	/**
	 * <p>
	 * Get DictCollection
	 * </p>
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
	public DictCollection getDictCollection(long groupId, String collectionCode)
			throws NoSuchDictCollectionException, SystemException {
		return dictCollectionPersistence.findByG_C(groupId, collectionCode);
	}

	/**
	 * <p>
	 * Lay tat ca doi tuong DictCollection trong CSDL
	 * </p>
	 * 
	 * @return trả về tập hợp các đối tượng DictCollection
	 * @throws SystemException
	 *             Nếu ngoại lệ hệ thống xảy ra
	 * @throws SystemException
	 *             Nếu ngoại lệ hệ thống xảy ra
	 */
	public List<DictCollection> getDictCollections() throws SystemException {
		return dictCollectionPersistence.findAll();
	}

	public List<DictCollection> getDictCollections(int start, int end, OrderByComparator odc) throws SystemException {
		return dictCollectionPersistence.findAll(start, end, odc);
	}

	public List<DictCollection> getDictCollections(long groupId, String[] collectionNames, int start, int end,
			OrderByComparator odc) throws SystemException {
		return dictCollectionPersistence.findByG_N(groupId, collectionNames, start, end, odc);
	}

	/**
	 * <p>
	 * Lay tat doi tuong DictCollection
	 * </p>
	 * 
	 * @param groupId
	 *            là id của UserGroup
	 * @return trả về đối tượng DictCollection theo groupId
	 * @throws SystemException
	 *             Nếu ngoại lệ hệ thống xảy ra
	 */
	public List<DictCollection> getDictCollections(long groupId) throws SystemException {
		return dictCollectionPersistence.findByGroupId(groupId);
	}
}