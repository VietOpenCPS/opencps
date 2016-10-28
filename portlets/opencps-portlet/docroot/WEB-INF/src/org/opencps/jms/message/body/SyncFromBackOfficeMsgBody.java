/**
 * OpenCPS is the open source Core Public Services software
 * Copyright (C) 2016-present OpenCPS community

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation;private either version 3 of the License;private or
 * any later version.

 * This program is distributed in the hope that it will be useful;private
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not;private see <http://www.gnu.org/licenses/>
 */

package org.opencps.jms.message.body;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.opencps.dossiermgt.model.DossierLog;
import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.processmgt.model.ActionHistory;

import com.liferay.portal.service.ServiceContext;

/**
 * @author trungnt
 */
public class SyncFromBackOfficeMsgBody implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getActionInfo() {

		return _actionInfo;
	}

	public void setActionInfo(String actionInfo) {

		this._actionInfo = actionInfo;
	}

	public long getDossierId() {

		return _dossierId;
	}

	public void setDossierId(long dossierId) {

		this._dossierId = dossierId;
	}

	public String getDossierStatus() {

		return _dossierStatus;
	}

	public void setDossierStatus(String dossierStatus) {

		this._dossierStatus = dossierStatus;
	}

	public Date getEstimateDatetime() {

		return _estimateDatetime;
	}

	public void setEstimateDatetime(Date estimateDatetime) {

		this._estimateDatetime = estimateDatetime;
	}

	public long getFileGroupId() {

		return _fileGroupId;
	}

	public void setFileGroupId(long fileGroupId) {

		this._fileGroupId = fileGroupId;
	}

	public Date getFinishDatetime() {

		return _finishDatetime;
	}

	public void setFinishDatetime(Date finishDatetime) {

		this._finishDatetime = finishDatetime;
	}

	public List<DossierFileMsgBody> getLstDossierFileMsgBody() {

		return _lstDossierFileMsgBody;
	}

	public void setLstDossierFileMsgBody(
		List<DossierFileMsgBody> lstDossierFileMsgBody) {

		this._lstDossierFileMsgBody = lstDossierFileMsgBody;
	}

	public String getMessageInfo() {

		return _messageInfo;
	}

	public void setMessageInfo(String messageInfo) {

		this._messageInfo = messageInfo;
	}

	public String getOid() {

		return _oid;
	}

	public void setOid(String oid) {

		this._oid = oid;
	}

	public Date getReceiveDatetime() {

		return _receiveDatetime;
	}

	public void setReceiveDatetime(Date receiveDatetime) {

		this._receiveDatetime = receiveDatetime;
	}

	public String getReceptionNo() {

		return _receptionNo;
	}

	public void setReceptionNo(String receptionNo) {

		this._receptionNo = receptionNo;
	}

	public String getRequestCommand() {

		return _requestCommand;
	}

	public void setRequestCommand(String requestCommand) {

		this._requestCommand = requestCommand;
	}

	public ServiceContext getServiceContext() {

		return _serviceContext;
	}

	public void setServiceContext(ServiceContext serviceContext) {

		this._serviceContext = serviceContext;
	}

	/**
	 * @return the _paymentFile
	 */
	public PaymentFile getPaymentFile() {

		return _paymentFile;
	}

	/**
	 * @param _paymentFile
	 *            the _paymentFile to set
	 */
	public void setPaymentFile(PaymentFile _paymentFile) {

		this._paymentFile = _paymentFile;
	}

	/**
	 * @return the _submitDateTime
	 */
	public Date getSubmitDateTime() {

		return _submitDateTime;
	}

	/**
	 * @param _submitDateTime
	 *            the _submitDateTime to set
	 */
	public void setSubmitDateTime(Date _submitDateTime) {

		this._submitDateTime = _submitDateTime;
	}

	/**
	 * @return the actor
	 */
	public int getActor() {

		return _actor;
	}

	/**
	 * @param actor
	 *            the actor to set
	 */
	public void setActor(int actor) {

		this._actor = actor;
	}

	/**
	 * @return the actorId
	 */
	public long getActorId() {

		return _actorId;
	}

	/**
	 * @param actorId
	 *            the actorId to set
	 */
	public void setActorId(long actorId) {

		this._actorId = actorId;
	}

	/**
	 * @return the actorName
	 */
	public String getActorName() {

		return _actorName;
	}

	/**
	 * @param actorName
	 *            the actorName to set
	 */
	public void setActorName(String actorName) {

		this._actorName = actorName;
	}

	public DossierLog getDossierLog() {

		return _dossierLog;
	}

	public void setDossierLog(DossierLog dossierLog) {

		this._dossierLog = dossierLog;
	}

	public ActionHistory getActionHistory() {

		return _actionHistory;
	}

	public void setActionHistory(ActionHistory actionHistory) {

		this._actionHistory = actionHistory;
	}

	private String _actionInfo;

	private long _dossierId;

	private String _dossierStatus;

	private Date _estimateDatetime;

	private long _fileGroupId;

	private Date _finishDatetime;

	private List<DossierFileMsgBody> _lstDossierFileMsgBody;

	private String _messageInfo;

	private String _oid;

	private Date _receiveDatetime;

	private String _receptionNo;

	private String _requestCommand;

	private ServiceContext _serviceContext;

	private PaymentFile _paymentFile;

	private Date _submitDateTime;

	private int _actor;

	private long _actorId;

	private String _actorName;

	private DossierLog _dossierLog;

	private ActionHistory _actionHistory;

}
