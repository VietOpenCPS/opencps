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

import java.util.Date;
import java.util.List;

import org.opencps.datamgt.search.DictCollectionDisplayTerms;
import org.opencps.datamgt.util.comparator.DictCollectionCreateDateComparator;
import org.opencps.processmgt.NoSuchActionHistoryException;
import org.opencps.processmgt.model.ActionHistory;
import org.opencps.processmgt.service.base.ActionHistoryLocalServiceBaseImpl;
import org.opencps.processmgt.util.comparator.ActionHistoryCreateDateComparator;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the action history local service. <p> All custom
 * service methods should be put in this class. Whenever methods are added,
 * rerun ServiceBuilder to copy their definitions into the
 * {@link org.opencps.processmgt.service.ActionHistoryLocalService} interface.
 * <p> This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM. </p>
 *
 * @author khoavd
 * @author trungnt
 * @see org.opencps.processmgt.service.base.ActionHistoryLocalServiceBaseImpl
 * @see org.opencps.processmgt.service.ActionHistoryLocalServiceUtil
 */
public class ActionHistoryLocalServiceImpl
    extends ActionHistoryLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link org.opencps.processmgt.service.ActionHistoryLocalServiceUtil} to
	 * access the action history local service.
	 */

	/**
	 * @param userId
	 * @param groupId
	 * @param companyId
	 * @param processOrderId
	 * @param processWorkflowId
	 * @param actionDatetime
	 * @param stepName
	 * @param actionName
	 * @param actionNote
	 * @param actionUserId
	 * @param daysDoing
	 * @param daysDelay
	 * @return
	 * @throws SystemException
	 */
	public ActionHistory addActionHistory(
	    long userId, long groupId, long companyId, long processOrderId,
	    long processWorkflowId, Date actionDatetime, String stepName,
	    String actionName, String actionNote, long actionUserId, int daysDoing,
	    int daysDelay, int dossierStatus)
	    throws SystemException {

		long actionHistoryId = counterLocalService
		    .increment(ActionHistory.class
		        .getName());
		ActionHistory actionHistory = actionHistoryPersistence
		    .create(actionHistoryId);

		Date now = new Date();

		actionHistory
		    .setUserId(userId);
		actionHistory
		    .setGroupId(groupId);
		actionHistory
		    .setCompanyId(companyId);
		actionHistory
		    .setCreateDate(now);
		actionHistory
		    .setModifiedDate(now);

		actionHistory
		    .setProcessOrderId(processOrderId);

		if (processWorkflowId >= 0) {
			actionHistory
			    .setProcessWorkflowId(processWorkflowId);
		}

		actionHistory
		    .setActionDatetime(actionDatetime);
		actionHistory
		    .setStepName(stepName);
		actionHistory
		    .setActionName(actionName);
		actionHistory
		    .setActionNote(actionNote);
		if (actionUserId > 0) {
			actionHistory
			    .setActionUserId(actionUserId);
		}

		if (daysDelay >= 0) {
			actionHistory
			    .setDaysDoing(daysDoing);
		}

		actionHistory
		    .setDaysDelay(daysDelay);
		return actionHistoryPersistence
		    .update(actionHistory);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.opencps.processmgt.service.ActionHistoryLocalService#addActionHistory
	 * (long, long, java.util.Date, java.lang.String, java.lang.String,
	 * java.lang.String, long, int, int,
	 * com.liferay.portal.service.ServiceContext)
	 */
	public ActionHistory addActionHistory(
	    long processOrderId, long processWorkflowId, Date actionDatetime,
	    String stepName, String actionName, String actionNote,
	    long actionUserId, int daysDoing, int daysDelay,
	    ServiceContext serviceContext)
	    throws SystemException {

		long actionHistoryId = counterLocalService
		    .increment(ActionHistory.class
		        .getName());
		ActionHistory actionHistory = actionHistoryPersistence
		    .create(actionHistoryId);

		Date now = new Date();

		actionHistory
		    .setUserId(serviceContext
		        .getUserId());
		actionHistory
		    .setGroupId(serviceContext
		        .getScopeGroupId());
		actionHistory
		    .setCompanyId(actionHistory
		        .getCompanyId());
		actionHistory
		    .setCreateDate(now);
		actionHistory
		    .setModifiedDate(now);

		actionHistory
		    .setProcessOrderId(processOrderId);
		actionHistory
		    .setProcessWorkflowId(processWorkflowId);
		actionHistory
		    .setActionDatetime(actionDatetime);
		actionHistory
		    .setStepName(stepName);
		actionHistory
		    .setActionName(actionName);
		actionHistory
		    .setActionNote(actionNote);
		actionHistory
		    .setActionUserId(actionUserId);
		actionHistory
		    .setDaysDoing(daysDoing);
		actionHistory
		    .setDaysDelay(daysDelay);
		return actionHistoryPersistence
		    .update(actionHistory);
	}
	
	public ActionHistory getLatestActionHistory(
	    long processOrderId, long processWorkflowId)
	    throws NoSuchActionHistoryException, SystemException {

		boolean orderByAsc = false;

		OrderByComparator orderByComparator =
		    new ActionHistoryCreateDateComparator(orderByAsc);

		return actionHistoryPersistence
		    .findByPOID_PWID_First(
		        processOrderId, processWorkflowId, orderByComparator);
	}

	public List<ActionHistory> getActionHistory(
	    long processOrderId, long processWorkflowId)
	    throws NoSuchActionHistoryException, SystemException {

		return actionHistoryPersistence
		    .findByPOID_PWID(processOrderId, processWorkflowId);
	}

	
	/**
	 * @param groupId
	 * @param dossierId
	 * @return
	 * @throws SystemException
	 */
	public List<ActionHistory> searchActionHistoryByDossierId(long groupId, long dossierId) {

		return actionHistoryFinder
		    .searchActionHistoryByDossierId(groupId, dossierId);
	}
	
}
