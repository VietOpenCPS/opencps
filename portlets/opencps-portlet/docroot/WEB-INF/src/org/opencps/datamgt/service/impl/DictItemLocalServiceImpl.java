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

import org.opencps.datamgt.NoSuchDictItemException;
import org.opencps.datamgt.NoSuchDictVersionException;
import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.model.DictVersion;
import org.opencps.datamgt.service.base.DictItemLocalServiceBaseImpl;
import org.opencps.util.PortletConstants;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the dict item local service. <p> All custom service
 * methods should be put in this class. Whenever methods are added, rerun
 * ServiceBuilder to copy their definitions into the
 * {@link org.opencps.datamgt.service.DictItemLocalService} interface. <p> This
 * is a local service. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM. </p>
 *
 * @author khoavd
 * @author trungnt
 * @see org.opencps.datamgt.service.base.DictItemLocalServiceBaseImpl
 * @see org.opencps.datamgt.service.DictItemLocalServiceUtil
 */
public class DictItemLocalServiceImpl extends DictItemLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link org.opencps.datamgt.service.DictItemLocalServiceUtil} to access
	 * the dict item local service.
	 */

	/**
	 * <p> Phuong thuc them moi mot DictItem co version va mac dinh trang thai
	 * drafting </p>
	 * 
	 * @param userId
	 *            - Data type<code>[long]</code> - Id tai khoan nguoi cap nhat
	 * @param dictCollectionId
	 *            - Data type<code>[long]</code> - Id ban ghi trong bang quan he
	 *            DictCollection
	 * @param itemCode
	 *            - Data type<code>[String]</code> - Ma code
	 * @param itemNameMap
	 *            - Data type<code>[Map&lt;Locale,String&gt;]</code> -
	 * @param parentId
	 *            - Data type<code>[long]</code> - Id DictItem cha
	 * @param treeIndex
	 *            - Data type<code>[String]</code> - Phan cap du lieu dang cay.
	 *            <ul> <li>Cap cha: 1</li> <li>Cap con: 1.1</li> <li>Cap chau:
	 *            1.1.1</li> </ul>
	 * @param serviceContext
	 *            - Data type<code>[ServiceContext]</code>
	 * @return DictItem
	 * @throws SystemException
	 *             Ngoai le xay ra trong qua trinh thuc thi phuong thuc
	 * @throws NoSuchDictVersionException
	 *             Ngoai le khong tim thay DictVersion
	 * @throws NoSuchDictItemException
	 *             Ngoai le khong tim duoc treeIndex cap cha
	 */
	public DictItem addDictItem(
		long userId, long dictCollectionId, long dictVersionId, String itemCode,
		Map<Locale, String> itemNameMap, long parentId,
		ServiceContext serviceContext)
		throws NoSuchDictVersionException, SystemException,
		NoSuchDictItemException {

		long dictItemId = CounterLocalServiceUtil
			.increment(DictItem.class
				.getName());

		DictItem dictItem = dictItemPersistence
			.create(dictItemId);

		DictVersion dictVersion = dictVersionPersistence
			.findByPrimaryKey(dictVersionId);
		Date now = new Date();
		String treeIndex = getTreeIndex(dictItemId, parentId);

		dictItem
			.setCompanyId(serviceContext
				.getCompanyId());
		dictItem
			.setCreateDate(now);
		dictItem
			.setDictCollectionId(dictCollectionId);
		dictItem
			.setGroupId(serviceContext
				.getScopeGroupId());
		dictItem
			.setIssueStatus(PortletConstants.DRAFTING);
		dictItem
			.setItemCode(itemCode);
		dictItem
			.setItemNameMap(itemNameMap);
		dictItem
			.setModifiedDate(now);
		dictItem
			.setParentItemId(parentId);
		dictItem
			.setTreeIndex(treeIndex);
		dictItem
			.setUserId(userId);
		dictItem
			.setDictVersionId(dictVersion
				.getDictVersionId());
		return dictItemPersistence
			.update(dictItem);
	}

	/**
	 * <p> Phuong thuc them moi mot DictItem khong co version va mac dinh trang
	 * thai inuse </p>
	 * 
	 * @param userId
	 *            - Data type<code>[long]</code> - Id tai khoan nguoi cap nhat
	 * @param dictCollectionId
	 *            - Data type<code>[long]</code> - Id ban ghi trong bang quan he
	 *            DictCollection
	 * @param itemCode
	 *            - Data type<code>[String]</code> - Ma code
	 * @param itemNameMap
	 *            - Data type<code>[Map&lt;Locale,String&gt;]</code> -
	 * @param parentId
	 *            - Data type<code>[long]</code> - Id DictItem cha
	 * @param treeIndex
	 *            - Data type<code>[String]</code> - Phan cap du lieu dang cay.
	 *            <ul> <li>Cap cha: 1</li> <li>Cap con: 1.1</li> <li>Cap chau:
	 *            1.1.1</li> </ul>
	 * @param serviceContext
	 *            - Data type<code>[ServiceContext]</code>
	 * @return DictItem
	 * @throws SystemException
	 *             Ngoai le xay ra trong qua trinh thuc thi phuong thuc
	 * @throws NoSuchDictItemException
	 *             Ngoai le khong tim duoc treeIndex cap cha
	 */
	public DictItem addDictItem(
		long userId, long dictCollectionId, String itemCode,
		Map<Locale, String> itemNameMap, long parentId,
		ServiceContext serviceContext)
		throws SystemException, NoSuchDictItemException {

		long dictItemId = CounterLocalServiceUtil
			.increment(DictItem.class
				.getName());

		DictItem dictItem = dictItemPersistence
			.create(dictItemId);

		Date now = new Date();
		String treeIndex = getTreeIndex(dictItemId, parentId);

		dictItem
			.setCompanyId(serviceContext
				.getCompanyId());
		dictItem
			.setCreateDate(now);
		dictItem
			.setDictCollectionId(dictCollectionId);
		dictItem
			.setGroupId(serviceContext
				.getScopeGroupId());
		dictItem
			.setIssueStatus(PortletConstants.INUSE);
		dictItem
			.setItemCode(itemCode);
		dictItem
			.setItemNameMap(itemNameMap);
		dictItem
			.setModifiedDate(now);
		dictItem
			.setParentItemId(parentId);
		dictItem
			.setTreeIndex(treeIndex);
		dictItem
			.setUserId(userId);

		return dictItemPersistence
			.update(dictItem);
	}

	public int countByDictCollectionId(long dictCollectionId)
		throws SystemException {

		return dictItemPersistence
			.countByDictCollectionId(dictCollectionId);
	}

	public int countByDictCollectionId(
		long dictCollectionId, String[] itemNames)
		throws SystemException {

		return dictItemPersistence
			.countByC_N(dictCollectionId, itemNames);
	}

	/**
	 * <p> Phuong thu xoa DictItem theo dictItemId. Neu DictItem co cap con thi
	 * khong xoa
	 * 
	 * @param dictItemId
	 *            - Data type<code>[long]</code> - Khoa chinh DictItem
	 * @return void
	 * @throws SystemException
	 *             - Loi he thong trong qua trinh thuc thi phuong thuc
	 * @throws NoSuchDictItemException
	 *             - Loi khong tim thay DictItem voi dictItemId tuong ung
	 */
	public void deleteDictItemById(long dictItemId)
		throws SystemException, NoSuchDictItemException {

		List<DictItem> dictItems = dictItemPersistence
			.findByTreeIndex(dictItemId + StringPool.PERIOD);
		if (dictItems == null || dictItems
			.isEmpty()) {
			dictItemPersistence
				.remove(dictItemId);
		}
	}

	/**
	 * <p> Phuong thu tim DictItem theo dictItemId </p>
	 * 
	 * @param dictItemId
	 *            - Data type<code>[long]</code> - Khoa chinh DictItem
	 * @return DictItem
	 * @throws SystemException
	 *             - Loi he thong trong qua trinh thuc thi phuong thuc
	 * @throws NoSuchDictItemException
	 *             - Loi khong tim thay DictItem voi dictItemId tuong ung
	 */
	public DictItem getDictItem(long dictItemId)
		throws NoSuchDictItemException, SystemException {

		return dictItemPersistence
			.findByPrimaryKey(dictItemId);
	}

	/**
	 * <p> Phuong thu tim DictItem dang co trang thai Inuse </p>
	 * 
	 * @param dictCollectionId
	 *            - Data type<code>[long]</code> - Khoa chinh DictCollection
	 * @param itemCode
	 *            - Data type<code>[String]</code> - Ma code
	 * @return DictItem
	 * @throws SystemException
	 *             - Loi he thong trong qua trinh thuc thi phuong thuc
	 * @throws NoSuchDictItemException
	 *             - Loi khong tim thay DictItem tuong ung
	 */
	public DictItem getDictItemInuseByItemCode(
		long dictCollectionId, String itemCode)
		throws NoSuchDictItemException, SystemException {

		return dictItemPersistence
			.findByC_C_I(dictCollectionId, itemCode);
	}

	public List<DictItem> getDictItems(long dictCollectionId, String itemName)
		throws SystemException {

		return dictItemPersistence
			.findByC_N(dictCollectionId, itemName);
	}

	public List<DictItem> getDictItems(
		long dictCollectionId, String[] itemNames, int start, int end,
		OrderByComparator obc)
		throws SystemException {

		return dictItemPersistence
			.findByC_N(dictCollectionId, itemNames, start, end, obc);
	}

	public List<DictItem> getDictItemsByDictCollectionId(long dictCollectionId)
		throws SystemException {

		return dictItemPersistence
			.findByDictCollectionId(dictCollectionId);
	}

	public List<DictItem> getDictItemsByDictCollectionId(
		long dictCollectionId, int start, int end, OrderByComparator obc)
		throws SystemException {

		return dictItemPersistence
			.findByDictCollectionId(dictCollectionId, start, end, obc);
	}

	public List<DictItem> getDictItemsByParentItemId(long parentItemId)
		throws SystemException {

		return dictItemPersistence
			.findByParentItemId(parentItemId);
	}

	public List<DictItem> getDictItemsByParentItemId(
		long parentItemId, int start, int end, OrderByComparator obc)
		throws SystemException {

		return dictItemPersistence
			.findByParentItemId(parentItemId, start, end, obc);
	}

	public List<DictItem> getDictItemsByDictVersionId(long dictVersionId)
		throws SystemException {

		return dictItemPersistence
			.findByDictVersionId(dictVersionId);
	}

	public List<DictItem> getDictItemsInUseByDictCollectionId(
		long dictCollectionId)
		throws SystemException {

		return dictItemPersistence
			.findByD_I(dictCollectionId);
	}

	public List<DictItem> getDictItemsInUseByDictCollectionIdAndParentItemId(
		long dictCollectionId, long parentItemId)
		throws SystemException {

		return dictItemPersistence
			.findByDictCollectionId_ParentItemId_Inuse(dictCollectionId,
				parentItemId);
	}

	protected String getTreeIndex(long dictItemId, long dictParentItemId)
		throws NoSuchDictItemException, SystemException {

		if (dictParentItemId == 0) {
			return String
				.valueOf(dictItemId);
		}
		else if (dictParentItemId > 0) {
			DictItem parentItem = dictItemPersistence
				.findByPrimaryKey(dictParentItemId);
			return parentItem
				.getTreeIndex() + StringPool.PERIOD + dictItemId;
		}
		else {
			throw new NoSuchDictItemException();
		}
	}

	/**
	 * <p> Phuong thuc update DictItem. Neu co version thi trang thai la draf,
	 * neu khong co version thi trang thai la inuse </p>
	 * 
	 * @param dictItemId
	 *            - Data type<code>[long]</code> - Id dictItem
	 * @param dictCollectionId
	 *            - Data type<code>[long]</code> - Id ban ghi trong bang quan he
	 *            DictCollection
	 * @param dictVersionId
	 *            - Data type<code>[long]</code> - Id ban ghi trong bang quan he
	 *            DictVersion
	 * @param itemCode
	 *            - Data type<code>[String]</code> - Ma code
	 * @param itemNameMap
	 *            - Data type<code>[Map&lt;Locale,String&gt;]</code> -
	 * @param parentItemId
	 *            - Data type<code>[long]</code> - Id DictItem cha
	 * @param serviceContext
	 *            - Data type<code>[ServiceContext]</code>
	 * @return DictItem
	 * @throws SystemException
	 *             Ngoai le xay ra trong qua trinh thuc thi phuong thuc
	 * @throws NoSuchDictVersionException
	 *             Ngoai le khong tim thay DictItem voi dictItemId tuong ung
	 */
	public DictItem updateDictItem(
		long dictItemId, long dictCollectionId, long dictVersionId,
		String itemCode, Map<Locale, String> itemNameMap, long parentItemId,
		ServiceContext serviceContext)
		throws NoSuchDictItemException, SystemException,
		NoSuchDictVersionException {

		DictItem dictItem = dictItemPersistence
			.findByPrimaryKey(dictItemId);

		Date now = new Date();

		String treeIndex = getTreeIndex(dictItemId, parentItemId);

		dictItem
			.setCompanyId(serviceContext
				.getCompanyId());
		dictItem
			.setCreateDate(now);
		dictItem
			.setDictCollectionId(dictCollectionId);
		dictItem
			.setGroupId(serviceContext
				.getScopeGroupId());
		dictItem
			.setIssueStatus(dictVersionId > 0
				? PortletConstants.DRAFTING : PortletConstants.INUSE);
		dictItem
			.setItemCode(itemCode);
		dictItem
			.setItemNameMap(itemNameMap);
		dictItem
			.setModifiedDate(now);
		dictItem
			.setParentItemId(parentItemId);
		dictItem
			.setTreeIndex(treeIndex);
		dictItem
			.setUserId(serviceContext
				.getUserId());
		dictItem
			.setDictVersionId(dictVersionId);
		return dictItemPersistence
			.update(dictItem);
	}

}
