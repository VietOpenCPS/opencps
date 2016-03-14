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

import org.opencps.datamgt.NoSuchDictVersionException;
import org.opencps.datamgt.model.DictCollection;
import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.model.DictVersion;
import org.opencps.datamgt.service.base.DictVersionLocalServiceBaseImpl;
import org.opencps.util.PortletConstants;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the dict version local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link org.opencps.datamgt.service.DictVersionLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author khoavd
 * @see org.opencps.datamgt.service.base.DictVersionLocalServiceBaseImpl
 * @see org.opencps.datamgt.service.DictVersionLocalServiceUtil
 */
public class DictVersionLocalServiceImpl extends
		DictVersionLocalServiceBaseImpl {
	/**
	 * <p>
	 * Add DictVersion
	 * </p>
	 * 
	 * @param userId
	 *            là id của người đăng nhập
	 * @param dictCollection
	 *            là đối tượng DictCollection
	 * @param version
	 *            là thuộc tính phiên bản của DictVersion
	 * @param serviceContext
	 *            Có thể lấy ra các userId, GroupId, CompanyId
	 * @param validatedFrom
	 *            Ngày phê bắt đầu phê duyệt
	 * @param validatedTo
	 *            Ngày kết thúc phê duyệt
	 * @param description
	 *            Mô tả
	 * @return trả về đối tượng DictVersion
	 * @throws SystemException
	 *             Nếu ngoại lệ hệ thống xảy ra
	 */
	public DictVersion addDictVersion(long userId,
			DictCollection dictCollection, String version, String description,
			Date validatedFrom, Date validatedTo, ServiceContext serviceContext)
			throws SystemException {
		long dictVersionId = CounterLocalServiceUtil
				.increment(DictVersion.class.getName());
		DictVersion dictVersion = dictVersionPersistence.create(dictVersionId);
		Date curDate = new Date();
		dictVersion.setCompanyId(serviceContext.getCompanyId());
		dictVersion.setGroupId(serviceContext.getScopeGroupId());
		dictVersion.setUserId(userId);
		dictVersion.setCreateDate(curDate);
		dictVersion.setModifiedDate(curDate);
		dictVersion.setDictCollectionId(dictCollection.getDictCollectionId());
		dictVersion.setVersion(version);
		dictVersion.setDescription(description);
		dictVersion.setValidatedFrom(validatedFrom);
		dictVersion.setValidatedTo(validatedTo);
		dictVersion.setIssueStatus(PortletConstants.DRAFTING);
		return dictVersionPersistence.update(dictVersion);
	}

	/**
	 * <p>
	 * Delete DictVersion
	 * </p>
	 * 
	 * @param dictVersionId
	 *            là id cuả DictVersion
	 * @throws SystemException
	 *             Nếu ngoại lệ hệ thống xảy ra
	 * @throws NoSuchDictVersionException
	 *             Khi xảy ra lỗi không tìm thấy DictVersion
	 */
	public void removeDictVersion(long dictVersionId)
			throws NoSuchDictVersionException, SystemException {
		dictVersionPersistence.remove(dictVersionId);
	}

	/**
	 * <p>
	 * Update DictVersion
	 * </p>
	 * 
	 * @param dictVersionId
	 *            là id của DictVersion
	 * @param userId
	 *            là id của người đăng nhập
	 * @param dictCollection
	 *            là đối tượng DictCollection
	 * @param version
	 *            là thuộc tính phiên bản của DictVersion
	 * @param serviceContext
	 *            Có thể lấy ra các userId, GroupId, CompanyId
	 * @param validatedFrom
	 *            Ngày phê bắt đầu phê duyệt
	 * @param validatedTo
	 *            Ngày kết thúc phê duyệt
	 * @param description
	 *            Mô tả
	 * @return trả về đối tượng DictVersion
	 * @throws SystemException
	 *             Nếu ngoại lệ hệ thống xảy ra
	 * @throws NoSuchDictVersionException
	 *             Khi xảy ra lỗi không tìm thấy DictVersion
	 */
	public DictVersion updateDictVersion(long dictVersionId, long userId,
			DictCollection dictCollection, String version, String description,
			Date validatedFrom, Date validatedTo, ServiceContext serviceContext)
			throws NoSuchDictVersionException, SystemException {
		DictVersion dictVersion = dictVersionPersistence
				.findByPrimaryKey(dictVersionId);
		Date curDate = new Date();
		dictVersion.setCompanyId(serviceContext.getCompanyId());
		dictVersion.setGroupId(serviceContext.getScopeGroupId());
		dictVersion.setUserId(userId);
		dictVersion.setCreateDate(curDate);
		dictVersion.setModifiedDate(curDate);
		dictVersion.setDictCollectionId(dictCollection.getDictCollectionId());
		dictVersion.setVersion(version);
		dictVersion.setDescription(description);
		dictVersion.setValidatedFrom(validatedFrom);
		dictVersion.setValidatedTo(validatedTo);
		return dictVersionPersistence.update(dictVersion);
	}

	/**
	 * <p>
	 * Get DictVersion
	 * </p>
	 * 
	 * @param dictVersionId
	 *            là DictVersion
	 * @return đối tượng DictVersions
	 * @throws SystemException
	 *             Nếu ngoại lệ hệ thống xảy ra
	 * @throws NoSuchDictVersionException
	 *             Khi xảy ra lỗi không tìm thấy DictVersion
	 */
	public DictVersion getDictVersion(long dictVersionId)
			throws NoSuchDictVersionException, SystemException {
		return dictVersionPersistence.findByPrimaryKey(dictVersionId);
	}
	
	public List<DictVersion> getDictVersion(int start, int end, OrderByComparator odc)
			throws NoSuchDictVersionException, SystemException {
		return dictVersionPersistence.findAll(start, end, odc);
	}
	
	public int countAll() throws SystemException {
		return dictVersionPersistence.countAll();
	}

	/**
	 * <p>
	 * Get DictVersion
	 * </p>
	 * 
	 * @param dictVersionId
	 *            là DictVersion
	 * @return đối tượng DictVersions
	 * @throws SystemException
	 *             Nếu ngoại lệ hệ thống xảy ra Chuyển trạng thái version từ
	 *             drafting sang inuse, khi đó phiên bản đang sử dụng sẽ tự động
	 *             được chuyển thành hết hạn
	 * @throws NoSuchDictVersionException
	 *             Khi xảy ra lỗi không tìm thấy DictVersion
	 */
	public DictVersion makeDictVersionInUse(int dictVersionId)
			throws NoSuchDictVersionException, SystemException {
		DictVersion dictVersion = dictVersionPersistence
				.findByPrimaryKey(dictVersionId);
		List<DictItem> lstDictItem = dictItemPersistence
				.findByDictVersionId(dictVersion.getDictCollectionId());
		if (dictVersion.getIssueStatus() != PortletConstants.INUSE) {
			for (DictItem dictItem : lstDictItem) {
				dictItem.setIssueStatus(PortletConstants.INUSE);
				dictItemPersistence.update(dictItem);
			}
		} 
		dictVersion.setIssueStatus(PortletConstants.INUSE);
		return dictVersionPersistence.update(dictVersion);
	}

	/**
	 * <p>
	 * Get DictVersion
	 * </p>
	 * 
	 * @param dictCollectionId
	 *            là id DictVersion
	 * @return tập hợp đối tượng DictVersion
	 * @throws SystemException
	 *             Nếu ngoại lệ hệ thống xảy ra
	 * @throws NoSuchDictVersionException
	 *             Khi xảy ra lỗi không tìm thấy DictVersion
	 */
	public List<DictVersion> getDictVersions(int dictCollectionId)
			throws SystemException {
		return dictVersionPersistence.findByDictCollectionId(dictCollectionId);
	}

	/**
	 * <p>
	 * Get DictVersion
	 * </p>
	 * 
	 * @param dictCollection
	 *            là đối tượng dictCollection đã được xác định
	 * @return Lấy version đang sử dụng của một collection, mỗi collection chỉ
	 *         có tối đa 1 version đang sử dụng
	 * @throws SystemException
	 *             Nếu ngoại lệ hệ thống xảy ra
	 * @throws NoSuchDictVersionException
	 *             Khi xảy ra lỗi không tìm thấy DictVersion
	 */
	public DictVersion getDictVersionInUse(DictCollection dictCollection)
			throws SystemException {
		List<DictVersion> lstDictVersion = dictVersionPersistence
				.findByDictCollectionId(dictCollection.getDictCollectionId());
		for (DictVersion dictVersion : lstDictVersion) {
			if (dictVersion.getIssueStatus() == PortletConstants.INUSE) {
				return dictVersion;
			}
		}
		return null;
	}

	/**
	 * <p>
	 * Get DictVersion
	 * </p>
	 * 
	 * @param dictCollection
	 *            là đối tượng dictCollection đã được xác định
	 * @return Lấy version đang biên tập của một collection, mỗi collection chỉ
	 *         có tối đa 1 version đang biên tập
	 * @throws SystemException
	 *             Nếu ngoại lệ hệ thống xảy ra
	 * @throws NoSuchDictVersionException
	 *             Khi xảy ra lỗi không tìm thấy DictVersion
	 */
	DictVersion getDictVersionDrafting(DictCollection dictCollection)
			throws SystemException {
		List<DictVersion> lstDictVersion = dictVersionPersistence
				.findByDictCollectionId(dictCollection.getDictCollectionId());
		for (DictVersion dictVersion : lstDictVersion) {
			if (dictVersion.getIssueStatus() == PortletConstants.DRAFTING) {
				return dictVersion;
			}
		}
		return null;
	}
}