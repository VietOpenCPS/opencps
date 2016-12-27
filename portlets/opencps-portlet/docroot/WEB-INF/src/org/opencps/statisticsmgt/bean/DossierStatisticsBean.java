package org.opencps.statisticsmgt.bean;

import java.util.Date;

public class DossierStatisticsBean {

	public String getGovItemCode() {
		return _govItemCode;
	}

	public void setGovItemCode(String govItemCode) {
		this._govItemCode = govItemCode;
	}

	public String getGovTreeIndex() {
		return _govTreeIndex;
	}

	public void setGovTreeIndex(String govTreeIndex) {
		this._govTreeIndex = govTreeIndex;
	}

	public String getDomainItemCode() {
		return _domainItemCode;
	}

	public void setDomainItemCode(String domainItemCode) {
		this._domainItemCode = domainItemCode;
	}

	public String getDomainTreeIndex() {
		return _domainTreeIndex;
	}

	public void setDomainTreeIndex(String domainTreeIndex) {
		this._domainTreeIndex = domainTreeIndex;
	}

	public int getAdministrationLevel() {
		return _administrationLevel;
	}

	public void setAdministrationLevel(int administrationLevel) {
		this._administrationLevel = administrationLevel;
	}

	public int getRemainingNumber() {
		return _remainingNumber;
	}

	public void setRemainingNumber(int remainingNumber) {
		this._remainingNumber = remainingNumber;
	}

	public int getReceivedNumber() {
		return _receivedNumber;
	}

	public void setReceivedNumber(int receivedNumber) {
		this._receivedNumber = receivedNumber;
	}

	public int getOntimeNumber() {
		return _ontimeNumber;
	}

	public void setOntimeNumber(int ontimeNumber) {
		this._ontimeNumber = ontimeNumber;
	}

	public int getOvertimeNumber() {
		return _overtimeNumber;
	}

	public void setOvertimeNumber(int overtimeNumber) {
		this._overtimeNumber = overtimeNumber;
	}

	public int getProcessingNumber() {
		return _processingNumber;
	}

	public void setProcessingNumber(int processingNumber) {
		this._processingNumber = processingNumber;
	}

	public int getDelayingNumber() {
		return _delayingNumber;
	}

	public void setDelayingNumber(int delayingNumber) {
		this._delayingNumber = delayingNumber;
	}

	public int getMonth() {
		return _month;
	}

	public void setMonth(int month) {
		this._month = month;
	}

	public int getYear() {
		return _year;
	}

	public void setYear(int year) {
		this._year = year;
	}

	
	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		this._groupId = groupId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		this._companyId = companyId;
	}

	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		this._userId = userId;
	}

	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _govItemCode;
	private String _govTreeIndex;
	private String _domainItemCode;
	private String _domainTreeIndex;
	private int _administrationLevel = -1;
	private int _remainingNumber;
	private int _receivedNumber;
	private int _ontimeNumber;
	private int _overtimeNumber;
	private int _processingNumber;
	private int _delayingNumber;
	private int _month;
	private int _year;
}
