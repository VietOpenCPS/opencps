/**
 * OpenCPS is the open source Core Public Services software Copyright (C)
 * 2016-present OpenCPS community This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3
 * of the License, or any later version. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details. You should have received a
 * copy of the GNU Affero General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.servicemgt.search;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * @author khoavd
 */
public class ServiceDisplayTerms extends DisplayTerms {

	public static final String SERVICE_ID = "serviceinfoId";
	public static final String SERVICE_NO = "serviceNo";
	public static final String SERVICE_NAME = "serviceName";
	public static final String SERVICE_FULLNAME = "fullName";
	public static final String SERVICE_PROCESS = "serviceProcess";
	public static final String SERVICE_METHOD = "serviceMethod";
	public static final String SERVICE_DOSSIER = "serviceDossier";
	public static final String SERVICE_CONDITION = "serviceCondition";
	public static final String SERVICE_DURATION = "serviceDuration";
	public static final String SERVICE_ACTORS = "serviceActors";
	public static final String SERVICE_RESULTS = "serviceResults";
	public static final String SERVICE_RECORDS = "serviceRecords";
	public static final String SERVICE_FEE = "serviceFee";
	public static final String SERVICE_INSTRUCTIONS = "serviceInstructions";
	public static final String SERVICE_ADMINISTRATION = "administrationCode";
	public static final String SERVICE_ADMINISTRATIONINDEX =
	    "administrationIndex";
	public static final String SERVICE_DOMAINCODE = "domainCode";
	public static final String SERVICE_DOMAININDEX = "domainIndex";
	public static final String SERVICE_ACTIVESTATUS = "activeStatus";
	public static final String SERVICE_HASTEMPLATEFILES = "hasTemplateFiles";
	public static final String SERVICE_ONLINEURL = "onlineUrl";
	public static final String GROUP_ID = "groupId";
	public static final String COMPANY_ID = "companyId";

	public static final String TEMPLATE_FILE_IDS =
	    "templateSearchContainerPrimaryKeys";

	/**
	 * @param request
	 */
	public ServiceDisplayTerms(PortletRequest request) {

		super(request);

		serviceNo = ParamUtil.getString(request, SERVICE_NO);

		serviceName = ParamUtil.getString(request, SERVICE_NAME);

		fullName = ParamUtil.getString(request, SERVICE_FULLNAME);

		serviceProcess = ParamUtil.getString(request, SERVICE_PROCESS);

		serviceMethod = ParamUtil.getString(request, SERVICE_METHOD);

		serviceDossier = ParamUtil.getString(request, SERVICE_DOSSIER);

		serviceCondition = ParamUtil.getString(request, SERVICE_CONDITION);

		serviceDuration = ParamUtil.getString(request, SERVICE_DURATION);

		serviceActors = ParamUtil.getString(request, SERVICE_ACTORS);

		serviceResults = ParamUtil.getString(request, SERVICE_RESULTS);

		serviceRecords = ParamUtil.getString(request, SERVICE_RECORDS);

		serviceFee = ParamUtil.getString(request, SERVICE_FEE);

		serviceInstructions =
		    ParamUtil.getString(request, SERVICE_INSTRUCTIONS);

		administrationCode =
		    ParamUtil.getString(request, SERVICE_ADMINISTRATION);

		administrationIndex =
		    ParamUtil.getString(request, SERVICE_ADMINISTRATIONINDEX);

		domainCode = ParamUtil.getString(request, SERVICE_DOMAINCODE);

		domainIndex = ParamUtil.getString(request, SERVICE_DOMAININDEX);

		activeStatus = ParamUtil.getInteger(request, SERVICE_ACTIVESTATUS);

		onlineUrl = ParamUtil.getString(request, SERVICE_ONLINEURL);

		groupId = setGroupId(request);
		
		serviceId = ParamUtil.getLong(request, SERVICE_ID);

	}

	/**
	 * @return the serviceNo
	 */
	public String getServiceNo() {

		return serviceNo;
	}

	/**
	 * @param serviceNo
	 *            the serviceNo to set
	 */
	public void setServiceNo(String serviceNo) {

		this.serviceNo = serviceNo;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {

		return serviceName;
	}

	/**
	 * @param serviceName
	 *            the serviceName to set
	 */
	public void setServiceName(String serviceName) {

		this.serviceName = serviceName;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {

		return fullName;
	}

	/**
	 * @param shortName
	 *            the shortName to set
	 */
	public void setFullName(String fullName) {

		this.fullName = fullName;
	}

	/**
	 * @return the serviceProcess
	 */
	public String getServiceProcess() {

		return serviceProcess;
	}

	/**
	 * @param serviceProcess
	 *            the serviceProcess to set
	 */
	public void setServiceProcess(String serviceProcess) {

		this.serviceProcess = serviceProcess;
	}

	/**
	 * @return the serviceMethod
	 */
	public String getServiceMethod() {

		return serviceMethod;
	}

	/**
	 * @param serviceMethod
	 *            the serviceMethod to set
	 */
	public void setServiceMethod(String serviceMethod) {

		this.serviceMethod = serviceMethod;
	}

	/**
	 * @return the serviceDossier
	 */
	public String getServiceDossier() {

		return serviceDossier;
	}

	/**
	 * @param serviceDossier
	 *            the serviceDossier to set
	 */
	public void setServiceDossier(String serviceDossier) {

		this.serviceDossier = serviceDossier;
	}

	/**
	 * @return the serviceCondition
	 */
	public String getServiceCondition() {

		return serviceCondition;
	}

	/**
	 * @param serviceCondition
	 *            the serviceCondition to set
	 */
	public void setServiceCondition(String serviceCondition) {

		this.serviceCondition = serviceCondition;
	}

	/**
	 * @return the serviceDuration
	 */
	public String getServiceDuration() {

		return serviceDuration;
	}

	/**
	 * @param serviceDuration
	 *            the serviceDuration to set
	 */
	public void setServiceDuration(String serviceDuration) {

		this.serviceDuration = serviceDuration;
	}

	/**
	 * @return the serviceActors
	 */
	public String getServiceActors() {

		return serviceActors;
	}

	/**
	 * @param serviceActors
	 *            the serviceActors to set
	 */
	public void setServiceActors(String serviceActors) {

		this.serviceActors = serviceActors;
	}

	/**
	 * @return the serviceResults
	 */
	public String getServiceResults() {

		return serviceResults;
	}

	/**
	 * @param serviceResults
	 *            the serviceResults to set
	 */
	public void setServiceResults(String serviceResults) {

		this.serviceResults = serviceResults;
	}

	/**
	 * @return the serviceRecords
	 */
	public String getServiceRecords() {

		return serviceRecords;
	}

	/**
	 * @param serviceRecords
	 *            the serviceRecords to set
	 */
	public void setServiceRecords(String serviceRecords) {

		this.serviceRecords = serviceRecords;
	}

	/**
	 * @return the serviceFee
	 */
	public String getServiceFee() {

		return serviceFee;
	}

	/**
	 * @param serviceFee
	 *            the serviceFee to set
	 */
	public void setServiceFee(String serviceFee) {

		this.serviceFee = serviceFee;
	}

	/**
	 * @return the serviceInstructions
	 */
	public String getServiceInstructions() {

		return serviceInstructions;
	}

	/**
	 * @param serviceInstructions
	 *            the serviceInstructions to set
	 */
	public void setServiceInstructions(String serviceInstructions) {

		this.serviceInstructions = serviceInstructions;
	}

	/**
	 * @return the administrationCode
	 */
	public String getAdministrationCode() {

		return administrationCode;
	}

	/**
	 * @param administrationCode
	 *            the administrationCode to set
	 */
	public void setAdministrationCode(String administrationCode) {

		this.administrationCode = administrationCode;
	}

	/**
	 * @return the administrationIndex
	 */
	public String getAdministrationIndex() {

		return administrationIndex;
	}

	/**
	 * @param administrationIndex
	 *            the administrationIndex to set
	 */
	public void setAdministrationIndex(String administrationIndex) {

		this.administrationIndex = administrationIndex;
	}

	/**
	 * @return the domainCode
	 */
	public String getDomainCode() {

		return domainCode;
	}

	/**
	 * @param domainCode
	 *            the domainCode to set
	 */
	public void setDomainCode(String domainCode) {

		this.domainCode = domainCode;
	}

	/**
	 * @return the domainIndex
	 */
	public String getDomainIndex() {

		return domainIndex;
	}

	/**
	 * @param domainIndex
	 *            the domainIndex to set
	 */
	public void setDomainIndex(String domainIndex) {

		this.domainIndex = domainIndex;
	}

	/**
	 * @return the activeStatus
	 */
	public int getActiveStatus() {

		return activeStatus;
	}

	/**
	 * @param activeStatus
	 *            the activeStatus to set
	 */
	public void setActiveStatus(int activeStatus) {

		this.activeStatus = activeStatus;
	}

	/**
	 * @return the hasTemplateFiles
	 */
	public int getHasTemplateFiles() {

		return hasTemplateFiles;
	}

	/**
	 * @param hasTemplateFiles
	 *            the hasTemplateFiles to set
	 */
	public void setHasTemplateFiles(int hasTemplateFiles) {

		this.hasTemplateFiles = hasTemplateFiles;
	}

	/**
	 * @return the onlineUrl
	 */
	public String getOnlineUrl() {

		return onlineUrl;
	}

	/**
	 * @param onlineUrl
	 *            the onlineUrl to set
	 */
	public void setOnlineUrl(String onlineUrl) {

		this.onlineUrl = onlineUrl;
	}

	/**
	 * @return the groupId
	 */
	public long getGroupId() {

		return groupId;
	}

	/**
	 * @param groupId
	 *            the groupId to set
	 */
	public long setGroupId(PortletRequest portletRequest) {

		groupId = ParamUtil.getLong(portletRequest, GROUP_ID);

		if (groupId != 0) {
			return groupId;
		}

		ThemeDisplay themeDisplay =
		    (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);

		return themeDisplay.getScopeGroupId();
	}

	
    /**
     * @return the serviceId
     */
    public long getServiceId() {
    
    	return serviceId;
    }

	
    /**
     * @param serviceId the serviceId to set
     */
    public void setServiceId(long serviceId) {
    
    	this.serviceId = serviceId;
    }

	protected long serviceId;
	protected String serviceNo;
	protected String serviceName;
	protected String fullName;
	protected String serviceProcess;
	protected String serviceMethod;
	protected String serviceDossier;
	protected String serviceCondition;
	protected String serviceDuration;
	protected String serviceActors;
	protected String serviceResults;
	protected String serviceRecords;
	protected String serviceFee;
	protected String serviceInstructions;
	protected String administrationCode;
	protected String administrationIndex;
	protected String domainCode;
	protected String domainIndex;
	protected int activeStatus;
	protected int hasTemplateFiles;
	protected String onlineUrl;
	protected long groupId;

	/**
	 * @return the fileTemplateIds
	 */
	public long[] getFileTemplateIds() {

		return fileTemplateIds;
	}

	/**
	 * @param fileTemplateIds
	 *            the fileTemplateIds to set
	 */
	public long[] setFileTemplateIds(PortletRequest portletRequest) {

		String[] strFileTemplateIds =
		    ParamUtil.getParameterValues(portletRequest, TEMPLATE_FILE_IDS);

		
		return GetterUtil.getLongValues(strFileTemplateIds, new long [] {});
	}

	protected long[] fileTemplateIds;
}
