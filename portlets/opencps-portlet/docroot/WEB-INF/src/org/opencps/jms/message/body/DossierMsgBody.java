/**
 * OpenCPS is the open source Core Public Services software
 * Copyright (C) 2016-present OpenCPS community

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.jms.message.body;

import java.io.Serializable;
import java.util.List;

import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierTemplate;
import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.servicemgt.model.ServiceInfo;

import com.liferay.portal.service.ServiceContext;

/**
 * @author trungnt
 */
public class DossierMsgBody implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public Dossier getDossier() {
	
		return _dossier;
	}
	
	public DossierTemplate getDossierTemplate() {
	
		return _dossierTemplate;
	}
	
	public List<DossierFileMsgBody> getLstDossierFileMsgBody() {
	
		return _lstDossierFileMsgBody;
	}
	
	public ServiceConfig getServiceConfig() {
	
		return _serviceConfig;
	}
	
	public ServiceInfo getServiceInfo() {
	
		return _serviceInfo;
	}
	
	public void setDossier(Dossier dossier) {
	
		this._dossier = dossier;
	}
	
	public void setDossierTemplate(DossierTemplate dossierTemplate) {
	
		this._dossierTemplate = dossierTemplate;
	}
	
	public void setLstDossierFileMsgBody(
		List<DossierFileMsgBody> lstDossierFileMsgBody) {
	
		this._lstDossierFileMsgBody = lstDossierFileMsgBody;
	}
	
	public void setServiceConfig(ServiceConfig serviceConfig) {
	
		this._serviceConfig = serviceConfig;
	}
	
	public void setServiceInfo(ServiceInfo serviceInfo) {
	
		this._serviceInfo = serviceInfo;
	}
	
	public ServiceContext getServiceContext() {
	
		return _serviceContext;
	}

	public void setServiceContext(ServiceContext serviceContext) {
	
		this._serviceContext = serviceContext;
	}

	private Dossier _dossier;
	
	private DossierTemplate _dossierTemplate;
	
	private List<DossierFileMsgBody> _lstDossierFileMsgBody;
	
	private ServiceConfig _serviceConfig;
	
	private ServiceInfo _serviceInfo;

	private ServiceContext _serviceContext;
}
