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

import org.opencps.dossiermgt.NoSuchDossierTemplateException;
import org.opencps.dossiermgt.model.DossierTemplate;
import org.opencps.dossiermgt.service.base.DossierTemplateLocalServiceBaseImpl;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the dossier template local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link org.opencps.dossiermgt.service.DossierTemplateLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author trungnt
 * @see org.opencps.dossiermgt.service.base.DossierTemplateLocalServiceBaseImpl
 * @see org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil
 */
public class DossierTemplateLocalServiceImpl
	extends DossierTemplateLocalServiceBaseImpl {
	
	public DossierTemplate addDossierTemplate(String templateNo,
		String templateName, String description, long userId, ServiceContext serviceContext) throws SystemException {
		
		long dossierTemplateId = CounterLocalServiceUtil.increment(DossierTemplate.class.getName());
		DossierTemplate dossierTemplate = dossierTemplatePersistence.create(dossierTemplateId);
		
		Date currentDate = new Date();
		
		dossierTemplate.setUserId(userId);
		dossierTemplate.setCompanyId(serviceContext.getCompanyId());
		dossierTemplate.setGroupId(serviceContext.getScopeGroupId());
		dossierTemplate.setCreateDate(currentDate);
		dossierTemplate.setModifiedDate(currentDate);
		
		dossierTemplate.setTemplateNo(templateNo);
		dossierTemplate.setTemplateName(templateName);
		dossierTemplate.setDescription(description);
		
		return dossierTemplatePersistence.update(dossierTemplate);
	}
	
	public DossierTemplate updateDossierTemplate(long dossierTemplateId, 
		String templateNo,String templateName, String description, long userId, ServiceContext serviceContext) throws SystemException {
		
		DossierTemplate dossierTemplate = dossierTemplatePersistence.fetchByPrimaryKey(dossierTemplateId);
		
		Date currentDate = new Date();
		
		dossierTemplate.setUserId(userId);
		dossierTemplate.setCompanyId(serviceContext.getCompanyId());
		dossierTemplate.setGroupId(serviceContext.getScopeGroupId());
		dossierTemplate.setCreateDate(currentDate);
		dossierTemplate.setModifiedDate(currentDate);
		
		dossierTemplate.setTemplateNo(templateNo);
		dossierTemplate.setTemplateName(templateName);
		dossierTemplate.setDescription(description);
		
		return dossierTemplatePersistence.update(dossierTemplate);
	
	}
	
	public void deleteDossierTemplateById(long dossierTemplateId) throws SystemException,
	NoSuchDossierTemplateException {
		int  dossierPartCounts = dossierPartPersistence
						.countByDossierTemplateId(dossierTemplateId);
	
		if(dossierPartCounts == 0) {
			dossierTemplatePersistence.remove(dossierTemplateId);
		}
	}
	
	public List<DossierTemplate> getDossierTemplates(String templateName) 
					throws SystemException {
		return dossierTemplatePersistence.findByTemplateName(templateName);
	}
	
	public List<DossierTemplate> getDossierTemplates(String templateName, 
		int start, int end, OrderByComparator orderByComparator) 
					throws SystemException {
		String nameBuffer = Validator
						.isNotNull(templateName) ? StringPool.PERCENT + templateName + StringPool.PERCENT
							: StringPool.PERCENT + StringPool.PERCENT;
		return dossierTemplatePersistence
						.findByTemplateName(nameBuffer, start, end, orderByComparator);
	}
	
	public int countDossierTemplatesByName(String templateName) throws SystemException {
		return dossierTemplatePersistence.countByTemplateName(templateName);
	}
	
	public List<DossierTemplate> getDossierTemplates(int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		return dossierTemplatePersistence.findAll(start, end, orderByComparator);
	}
	
	public List<DossierTemplate> getAll() throws SystemException {
		return dossierTemplatePersistence.findAll();
	}
	
	public List<DossierTemplate> getDossierTemplatesByGroupId(long groupId) throws SystemException {
		return dossierTemplatePersistence.findByGroupId(groupId);
	}
	
	public DossierTemplate getDossierTemplate(String templateNo) 
					throws NoSuchDossierTemplateException, SystemException {
		return dossierTemplatePersistence.findByTemplateNo(templateNo);
	}
	
	public DossierTemplate getDossierTemplateById(long dossierTemplateId) 
					throws NoSuchDossierTemplateException, SystemException {
		return dossierTemplatePersistence.findByPrimaryKey(dossierTemplateId);
	}
	
	public int countAll() throws SystemException {
		return dossierTemplatePersistence.countAll();
	}
	
	
}