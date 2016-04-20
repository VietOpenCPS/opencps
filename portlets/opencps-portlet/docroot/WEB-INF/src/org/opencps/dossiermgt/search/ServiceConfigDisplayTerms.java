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
package org.opencps.dossiermgt.search;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.ParamUtil;


public class ServiceConfigDisplayTerms extends DisplayTerms{
	
	public static final String SERVICE_ADMINISTRATION = "administrationCode";
	public static final String SERVICE_CONFIG_SERVICECONFIGID = "serviceConfigId";
	public static final String SERVICE_CONFIG_SERVICEINFOID = "serviceInfoId";
	public static final String SERVICE_CONFIG_DOSSIERTEMPLATEID = "dossierTemplateId";
	public static final String SERVICE_CONFIG_GOVAGENCYORGANIZATIONID = "govAgencyOrganizationId";
	public static final String SERVICE_CONFIG_SERVICEPROCESSID = "serviceProcessId";
	
	public static final String SERVICE_CONFIG_SERVICEDOMAININDEX = "serviceDomainIndex";
	public static final String SERVICE_CONFIG_SERVICEADMINISTRATIONINDEX = "serviceAdministrationIndex";
	public static final String SERVICE_CONFIG_GOVAGENCYCODE = "govAgencyCode";
	public static final String SERVICE_CONFIG_GOVAGENCYNAME = "govAgencyName";
	public static final String SERVICE_CONFIG_DOMAINCODE = "domainCode";
	
	public static final String SERVICE_CONFIG_SERVICEMODE = "serviceMode";
	
	public ServiceConfigDisplayTerms(PortletRequest request) {

	
		super(request);
		serviceConfigId = ParamUtil.getLong(request, SERVICE_CONFIG_SERVICECONFIGID);
		serviceInfoId = ParamUtil.getLong(request, SERVICE_CONFIG_SERVICEINFOID);
		dossierTemplateId = ParamUtil.getLong(request, SERVICE_CONFIG_DOSSIERTEMPLATEID);
		govAgencyOrganizationId = ParamUtil.getLong(request, SERVICE_CONFIG_GOVAGENCYORGANIZATIONID);
		serviceProcessId = ParamUtil.getLong(request, SERVICE_CONFIG_SERVICEPROCESSID);
		
		serviceDomainIndex = ParamUtil.getString(request, SERVICE_CONFIG_SERVICEDOMAININDEX);
		serviceAdministrationIndex = ParamUtil.getString(request, SERVICE_CONFIG_SERVICEADMINISTRATIONINDEX);
		govAgencyCode = ParamUtil.getString(request, SERVICE_CONFIG_GOVAGENCYCODE);
		govAgencyName = ParamUtil.getString(request, SERVICE_CONFIG_GOVAGENCYNAME);
		domainCode = ParamUtil.getString(request, SERVICE_CONFIG_DOMAINCODE);
		administrationCode = ParamUtil.getString(request, SERVICE_ADMINISTRATION);
		
		serviceMode = ParamUtil.getInteger(request, SERVICE_CONFIG_SERVICEMODE);
    }
	
    public long getServiceConfigId() {
    
    	return serviceConfigId;
    }

	
    public void setServiceConfigId(long serviceConfigId) {
    
    	this.serviceConfigId = serviceConfigId;
    }

	
    public long getServiceInfoId() {
    
    	return serviceInfoId;
    }

	
    public void setServiceInfoId(long serviceInfoId) {
    
    	this.serviceInfoId = serviceInfoId;
    }

	
    public long getDossierTemplateId() {
    
    	return dossierTemplateId;
    }

	
    public void setDossierTemplateId(long dossierTemplateId) {
    
    	this.dossierTemplateId = dossierTemplateId;
    }

	
    public long getGovAgencyOrganizationId() {
    
    	return govAgencyOrganizationId;
    }

	
    public void setGovAgencyOrganizationId(long govAgencyOrganizationId) {
    
    	this.govAgencyOrganizationId = govAgencyOrganizationId;
    }

	
    public long getServiceProcessId() {
    
    	return serviceProcessId;
    }

	
    public void setServiceProcessId(long serviceProcessId) {
    
    	this.serviceProcessId = serviceProcessId;
    }

	
    public String getServiceDomainIndex() {
    
    	return serviceDomainIndex;
    }

	
    public void setServiceDomainIndex(String serviceDomainIndex) {
    
    	this.serviceDomainIndex = serviceDomainIndex;
    }

	
    public String getServiceAdministrationIndex() {
    
    	return serviceAdministrationIndex;
    }

	
    public void setServiceAdministrationIndex(String serviceAdministrationIndex) {
    
    	this.serviceAdministrationIndex = serviceAdministrationIndex;
    }

	
    public String getGovAgencyCode() {
    
    	return govAgencyCode;
    }

	
    public void setGovAgencyCode(String govAgencyCode) {
    
    	this.govAgencyCode = govAgencyCode;
    }

	
    public String getGovAgencyName() {
    
    	return govAgencyName;
    }

	
    public void setGovAgencyName(String govAgencyName) {
    
    	this.govAgencyName = govAgencyName;
    }

	
    public int getServiceMode() {
    
    	return serviceMode;
    }

	
    public void setServiceMode(int serviceMode) {
    
    	this.serviceMode = serviceMode;
    }
    
	
    public String getDomainCode() {
    
    	return domainCode;
    }

	
    public void setDomainCode(String domainCode) {
    
    	this.domainCode = domainCode;
    }
    
    public String getAdministrationCode() {
        
    	return administrationCode;
    }

	
    public void setAdministrationCode(String administrationCode) {
    
    	this.administrationCode = administrationCode;
    }

	protected int serviceMode;

	protected long serviceConfigId;
	protected long serviceInfoId;
	protected long dossierTemplateId;
	protected long govAgencyOrganizationId;
	protected long serviceProcessId;
	
	protected String serviceDomainIndex;
	protected String serviceAdministrationIndex;	
	protected String govAgencyCode;
	protected String govAgencyName;
	protected String domainCode;
	protected String administrationCode;

}
