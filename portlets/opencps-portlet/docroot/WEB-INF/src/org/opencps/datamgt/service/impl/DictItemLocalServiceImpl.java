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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opencps.datamgt.NoSuchDictItemException;
import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.service.base.DictItemLocalServiceBaseImpl;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the dict item local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link org.opencps.datamgt.service.DictItemLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author khoavd
 * @author trungnt
 * @see org.opencps.datamgt.service.base.DictItemLocalServiceBaseImpl
 * @see org.opencps.datamgt.service.DictItemLocalServiceUtil
 */
public class DictItemLocalServiceImpl extends DictItemLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link
	 * org.opencps.datamgt.service.DictItemLocalServiceUtil} to access the dict
	 * item local service.
	 */

	public DictItem updateDataItem(long dictItemId, String collectionCode, String itemCode, String itemName,
			String node1, String node2, String node3, String node4, String node5, int dataLevel, Date validatedFrom,
			Date validatedTo, int issueStatus, int order, ServiceContext serviceContext)
			throws SystemException, PortalException {

		DictItem dictItem = null;

		Date now = new Date();

		if (dictItemId == 0) {
			dictItemId = CounterLocalServiceUtil.increment(DictItem.class.getName());
			dictItem = dictItemPersistence.create(dictItemId);
			dictItem.setCreateDate(now);
		} else {
			dictItem = dictItemPersistence.findByPrimaryKey(dictItemId);
		}

		User user = userLocalService.getUser(serviceContext.getUserId());

		dictItem.setCollectionCode(collectionCode);
		dictItem.setCompanyId(serviceContext.getCompanyId());
		dictItem.setDataLevel(dataLevel);
		dictItem.setGroupId(serviceContext.getScopeGroupId());
		dictItem.setIssueStatus(issueStatus);
		dictItem.setItemCode(itemCode);
		dictItem.setItemName(itemName);
		dictItem.setModifiedDate(now);
		dictItem.setNode_1(node1);
		dictItem.setNode_2(node2);
		dictItem.setNode_3(node3);
		dictItem.setNode_4(node4);
		dictItem.setNode_4(node5);
		dictItem.setUserId(user.getUserId());
		dictItem.setUserName(user.getFullName());
		dictItem.setValidatedFrom(validatedFrom);
		dictItem.setValidatedTo(validatedTo);
		dictItem.setOrder(order);
		

		return dictItemPersistence.update(dictItem);
	}

	public DictItem getDataItemById(long id) throws SystemException, NoSuchDictItemException {
		return dictItemPersistence.findByPrimaryKey(id);

	}

	public int countItem(String name, String collectionName, String node, int validator, int dataLevel) {
		return 0;
	}

	public List<DictItem> getListItem(String name, String collectionName, String node, int validator, int dataLevel,
			int start, int end, OrderByComparator order) {
		return new ArrayList<DictItem>();
	}
}
