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

package org.opencps.dossiermgt.service.impl;

import org.opencps.dossiermgt.NoSuchDossierPartException;
import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.service.base.DossierPartLocalServiceBaseImpl;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;

/**
 * The implementation of the dossier part local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link org.opencps.dossiermgt.service.DossierPartLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author trungnt
 * @see org.opencps.dossiermgt.service.base.DossierPartLocalServiceBaseImpl
 * @see org.opencps.dossiermgt.service.DossierPartLocalServiceUtil
 */
public class DossierPartLocalServiceImpl extends DossierPartLocalServiceBaseImpl {
	
	public DossierPart addDossierPart(long dossierTemplateId,
		String partName, String partTip, int partType, long parentId, double sibling,
		String formScript, String sampleData, boolean required,
		String templateFileNo) throws SystemException, NoSuchDossierPartException {
		
		long dossierPartId = CounterLocalServiceUtil.increment(DossierPart.class.getName());
		DossierPart dossierPart = dossierPartPersistence.create(dossierPartId);
		
		String treeIndex = getTreeIndex(parentId, dossierPartId);
		
		dossierPart.setDossierTemplateId(dossierTemplateId);
		dossierPart.setPartName(partName);
		dossierPart.setPartTip(partTip);
		dossierPart.setPartType(partType);
		dossierPart.setParentId(parentId);
		dossierPart.setSibling(sibling);
		dossierPart.setFormScript(formScript);
		dossierPart.setSampleData(sampleData);
		dossierPart.setRequired(required);
		dossierPart.setTemplateFileNo(templateFileNo);
		dossierPart.setTreeIndex(treeIndex);
		
		return dossierPartPersistence.update(dossierPart);	
	}
	
	public DossierPart updateDossierPart(long dossierPartId ,long dossierTemplateId,
		String partName, String partTip, int partType, long parentId, double sibling,
		String formScript, String sampleData, boolean required,
		String templateFileNo) throws SystemException {
		
		DossierPart dossierPart = dossierPartPersistence.fetchByPrimaryKey(dossierPartId);
		
		dossierPart.setDossierpartId(dossierTemplateId);
		dossierPart.setPartName(partName);
		dossierPart.setPartTip(partTip);
		dossierPart.setPartType(partType);
		dossierPart.setParentId(parentId);
		dossierPart.setSibling(sibling);
		dossierPart.setFormScript(formScript);
		dossierPart.setSampleData(sampleData);
		dossierPart.setRequired(required);
		dossierPart.setTemplateFileNo(templateFileNo);
		
		return dossierPartPersistence.update(dossierPart);
	}
	
	public String getTreeIndex(long parentId, long dossierPartId) 
					throws SystemException, NoSuchDossierPartException {
		if(parentId == 0) {
			return String.valueOf(dossierPartId);
		} else if(parentId > 0) {
			DossierPart dossierPart = dossierPartPersistence.fetchByPrimaryKey(parentId);
			return dossierPart.getTreeIndex() + StringPool.PERIOD + String.valueOf(dossierPartId);
		} else {
			throw new NoSuchDossierPartException();
		}
	}
}