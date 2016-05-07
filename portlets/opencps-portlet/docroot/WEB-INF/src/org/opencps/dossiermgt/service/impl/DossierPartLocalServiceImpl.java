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

import java.util.Date;
import java.util.List;

import org.opencps.dossiermgt.NoSuchDossierPartException;
import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.service.base.DossierPartLocalServiceBaseImpl;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.service.ServiceContext;

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
	
	public DossierPart addDossierPart(long dossierTemplateId,String partNo,
		String partName, String partTip, int partType, long parentId, double sibling,
		String formScript,String formReport, String sampleData, boolean required,
		String templateFileNo, long userId, ServiceContext serviceContext) throws SystemException, NoSuchDossierPartException {
		
		long dossierPartId = CounterLocalServiceUtil.increment(DossierPart.class.getName());
		DossierPart dossierPart = dossierPartPersistence.create(dossierPartId);
		
		String treeIndex = getTreeIndex(parentId, dossierPartId);
		
		Date currentDate = new Date();
		
		dossierPart.setUserId(userId);
		dossierPart.setCompanyId(serviceContext.getCompanyId());
		dossierPart.setGroupId(serviceContext.getScopeGroupId());
		dossierPart.setCreateDate(currentDate);
		dossierPart.setModifiedDate(currentDate);
		
		dossierPart.setDossierTemplateId(dossierTemplateId);
		dossierPart.setPartNo(partNo);
		dossierPart.setPartName(partName);
		dossierPart.setPartTip(partTip);
		dossierPart.setPartType(partType);
		dossierPart.setParentId(parentId);
		dossierPart.setSibling(sibling);
		dossierPart.setFormScript(formScript);
		dossierPart.setFormReport(formReport);
		dossierPart.setSampleData(sampleData);
		dossierPart.setRequired(required);
		dossierPart.setTemplateFileNo(templateFileNo);
		dossierPart.setTreeIndex(treeIndex);
		
		return dossierPartPersistence.update(dossierPart);	
	}
	
	public DossierPart updateDossierPart(long dossierPartId ,long dossierTemplateId,
		String partNo, String partName, String partTip, int partType, long parentId, double sibling,
		String formScript,String formReport, String sampleData, boolean required,
		String templateFileNo, long userId, ServiceContext serviceContext) throws SystemException {
		
		DossierPart dossierPart = dossierPartPersistence.fetchByPrimaryKey(dossierPartId);
		
		Date currentDate = new Date();
		
		dossierPart.setUserId(userId);
		dossierPart.setCompanyId(serviceContext.getCompanyId());
		dossierPart.setGroupId(serviceContext.getScopeGroupId());
		dossierPart.setCreateDate(currentDate);
		dossierPart.setModifiedDate(currentDate);
		
		dossierPart.setDossierTemplateId(dossierTemplateId);
		dossierPart.setPartName(partName);
		dossierPart.setPartNo(partNo);
		dossierPart.setPartTip(partTip);
		dossierPart.setPartType(partType);
		dossierPart.setParentId(parentId);
		dossierPart.setSibling(sibling);
		dossierPart.setFormScript(formScript);
		dossierPart.setFormReport(formReport);
		dossierPart.setSampleData(sampleData);
		dossierPart.setRequired(required);
		dossierPart.setTemplateFileNo(templateFileNo);
		
		return dossierPartPersistence.update(dossierPart);
	}
	
	public void deleteDossierPartById(long dossierPartId) throws NoSuchDossierPartException, SystemException {
		
		int dossierPartParentCount = dossierPartPersistence.countByParentId(dossierPartId);
		
		if(dossierPartParentCount == 0) {
			dossierPartPersistence.remove(dossierPartId);
		}
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
	
	public List<DossierPart> getDossierParts(int start, int end, 
		OrderByComparator orderByComparator) throws SystemException {
		return dossierPartPersistence.findAll(start, end, orderByComparator);
	}
	
	public List<DossierPart> getDossierParts (String partName)
					throws SystemException {
		return dossierPartPersistence.findByPartName(partName);
	}
	
	public int countAll() throws SystemException {
		return dossierPartPersistence.countAll();
	}
	
	public List<DossierPart> getDossierParts (long dossierTemplateId)
					throws SystemException {
		return dossierPartPersistence.findByDossierTemplateId(dossierTemplateId);
	} 
	
	public List<DossierPart> getDossierPartsByParentId(long parentId) throws SystemException {
		return dossierPartPersistence.findByParentId(parentId);
	}
	
	public int CountByTempalteId(long dossierTemplateId) throws SystemException {
		return dossierPartPersistence.countByDossierTemplateId(dossierTemplateId);
	}
	
	public int CountByParentId(long dossierPartParentId) throws SystemException {
		return dossierPartPersistence.countByParentId(dossierPartParentId);
	}
	
	public DossierPart getDossierPartByPartNo(String partNo) 
					throws NoSuchDossierPartException, SystemException {
		return dossierPartPersistence.findByPartNo(partNo);
	}
	
	public List<DossierPart> getDossierPartsByT_P(long dossierTemplateId,
		long parentId) throws SystemException {
		return dossierPartPersistence.findByT_P(dossierTemplateId, parentId);
	}
	
	public DossierPart getDossierPartByT_S(long dossierTemplateId , Double sibling)
					throws NoSuchDossierPartException, SystemException {
		return dossierPartPersistence.findByT_S(dossierTemplateId, sibling);
	}
	public List<DossierPart> getDossierPartsByT_T( 
		long dossierTemplateId, int partType) throws SystemException {
		return dossierPartPersistence.findByT_T(dossierTemplateId, partType);
	}
}