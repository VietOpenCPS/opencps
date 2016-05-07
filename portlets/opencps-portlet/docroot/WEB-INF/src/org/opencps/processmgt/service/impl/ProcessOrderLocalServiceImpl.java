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

package org.opencps.processmgt.service.impl;

import java.util.List;

import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.service.base.ProcessOrderLocalServiceBaseImpl;

import com.liferay.portal.kernel.util.OrderByComparator;

/**
 * The implementation of the process order local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link org.opencps.processmgt.service.ProcessOrderLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author khoavd
 * @see org.opencps.processmgt.service.base.ProcessOrderLocalServiceBaseImpl
 * @see org.opencps.processmgt.service.ProcessOrderLocalServiceUtil
 */
public class ProcessOrderLocalServiceImpl
	extends ProcessOrderLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link org.opencps.processmgt.service.ProcessOrderLocalServiceUtil} to access the process order local service.
	 */
	public int countProcessOrder(long groupId, String keywords, String processStepId, String assignToUserId) {
		return processOrderFinder.countProcessOrder(groupId, keywords, processStepId, assignToUserId);
    }
	
	public List searchProcessOrder(long groupId, String keywords, String processStepId, String assignToUserId, int start, int end, OrderByComparator orderByComparator) {
		return processOrderFinder.searchProcessOrder(groupId, keywords, processStepId, assignToUserId, start, end, orderByComparator);
    }
}
