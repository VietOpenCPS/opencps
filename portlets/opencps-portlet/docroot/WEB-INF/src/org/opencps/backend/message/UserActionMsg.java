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

package org.opencps.backend.message;

import java.util.Locale;


/**
 * @author khoavd
 *
 */
public class UserActionMsg {

	/**
	 * @return the action
	 */
	public String getAction() {

		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {

		this.action = action;
	}

	/**
	 * @return the dossierId
	 */
	public long getDossierId() {

		return dossierId;
	}

	/**
	 * @param dossierId
	 *            the dossierId to set
	 */
	public void setDossierId(long dossierId) {

		this.dossierId = dossierId;
	}

	/**
	 * @return the fileGroupId
	 */
	public long getFileGroupId() {

		return fileGroupId;
	}

	/**
	 * @param fileGroupId
	 *            the fileGroupId to set
	 */
	public void setFileGroupId(long fileGroupId) {

		this.fileGroupId = fileGroupId;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {

		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {

		this.userId = userId;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {

		return locale;
	}

	/**
	 * @param locale
	 *            the locale to set
	 */
	public void setLocale(Locale locale) {

		this.locale = locale;
	}

	/**
	 * @return the processOrderId
	 */
	public long getProcessOrderId() {

		return processOrderId;
	}

	/**
	 * @param processOrderId
	 *            the processOrderId to set
	 */
	public void setProcessOrderId(long processOrderId) {

		this.processOrderId = processOrderId;
	}

	/**
	 * @return the companyId
	 */
	public long getCompanyId() {

		return companyId;
	}

	/**
	 * @param companyId
	 *            the companyId to set
	 */
	public void setCompanyId(long companyId) {

		this.companyId = companyId;
	}

	/**
	 * @return the govAgencyCode
	 */
	public String getGovAgencyCode() {

		return govAgencyCode;
	}

	/**
	 * @param govAgencyCode
	 *            the govAgencyCode to set
	 */
	public void setGovAgencyCode(String govAgencyCode) {

		this.govAgencyCode = govAgencyCode;
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
	public void setGroupId(long groupId) {

		this.groupId = groupId;
	}

	/**
	 * @return the noReceptionNo
	 */
	public String getNoReceptionNo() {

		return noReceptionNo;
	}

	/**
	 * @param noReceptionNo
	 *            the noReceptionNo to set
	 */
	public void setNoReceptionNo(String noReceptionNo) {

		this.noReceptionNo = noReceptionNo;
	}

	/**
	 * @return the paymentFileId
	 */
	public long getPaymentFileId() {

		return paymentFileId;
	}

	/**
	 * @param paymentFileId
	 *            the paymentFileId to set
	 */
	public void setPaymentFileId(long paymentFileId) {

		this.paymentFileId = paymentFileId;
	}
	
	
	
	/**
	 * @return
	 */
	public String getDossierOId() {
	
		return dossierOId;
	}

	
	/**
	 * @param dossierOId
	 */
	public void setDossierOId(String dossierOId) {
	
		this.dossierOId = dossierOId;
	}



	
    /**
     * @return the dossierStatus
     */
    public String getDossierStatus() {
    
    	return dossierStatus;
    }

	
    /**
     * @param dossierStatus the dossierStatus to set
     */
    public void setDossierStatus(String dossierStatus) {
    
    	this.dossierStatus = dossierStatus;
    }



	
    public String getActionNote() {
    
    	return actionNote;
    }

	
    public void setActionNote(String actionNote) {
    
    	this.actionNote = actionNote;
    }

	protected long paymentFileId;
	protected String noReceptionNo;
	protected long groupId;
	protected long companyId;
	protected String govAgencyCode;
	protected String action;
	protected long dossierId;
	protected long fileGroupId;
	protected long userId;
	protected Locale locale;
	protected long processOrderId;
	protected String dossierOId;
	protected String dossierStatus;
	protected String actionNote;

}
