/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package org.opencps.datamgt.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.opencps.datamgt.model.AdministrationServicedomain;
import org.opencps.datamgt.model.DictCollection;
import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.service.base.AdministrationServicedomainLocalServiceBaseImpl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

/**
 * The implementation of the administration servicedomain local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link org.opencps.datamgt.service.AdministrationServicedomainLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author khoavd
 * @see org.opencps.datamgt.service.base.AdministrationServicedomainLocalServiceBaseImpl
 * @see org.opencps.datamgt.service.AdministrationServicedomainLocalServiceUtil
 */
public class AdministrationServicedomainLocalServiceImpl
	extends AdministrationServicedomainLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link org.opencps.datamgt.service.AdministrationServicedomainLocalServiceUtil} to access the administration servicedomain local service.
	 */
	
	public List<AdministrationServicedomain> getMappingAdministrationCode(long groupId, String dictCollectionCode, String itemCode)
			throws SystemException, PortalException{

		List<AdministrationServicedomain> results = new ArrayList<AdministrationServicedomain>();

		results = administrationServicedomainPersistence.findByF_AC_ACC(groupId, itemCode, dictCollectionCode);
		
		return results;
	}
}