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

import java.util.List;

import org.opencps.dossiermgt.model.DossierTemplate;
import org.opencps.dossiermgt.service.base.DossierTemplateLocalServiceBaseImpl;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.OrderByComparator;

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
		String templateName, String description) throws SystemException {
		
		long dossierTemplateId = CounterLocalServiceUtil.increment(DossierTemplate.class.getName());
		DossierTemplate dossierTemplate = dossierTemplatePersistence.create(dossierTemplateId);
		
		dossierTemplate.setTemplateNo(templateNo);
		dossierTemplate.setTemplateName(templateName);
		dossierTemplate.setDescription(description);
		
		return dossierTemplatePersistence.update(dossierTemplate);
	}
	
	public DossierTemplate updateDossierTemplate(long dossierTemplateId, 
		String templateNo,String templateName, String description) throws SystemException {
		
		DossierTemplate dossierTemplate = dossierTemplatePersistence.fetchByPrimaryKey(dossierTemplateId);
		
		dossierTemplate.setTemplateNo(templateNo);
		dossierTemplate.setTemplateName(templateName);
		dossierTemplate.setDescription(description);
		
		return dossierTemplatePersistence.update(dossierTemplate);
	
	}
	
	public List<DossierTemplate> getDossierTemplates(String templateName) 
					throws SystemException {
		return dossierTemplatePersistence.findByTemplateName(templateName);
	}
	
	public List<DossierTemplate> getDossierTemplates(int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		return dossierTemplatePersistence.findAll(start, end, orderByComparator);
	}
	
	public int countAll() throws SystemException {
		return dossierTemplatePersistence.countAll();
	}
}