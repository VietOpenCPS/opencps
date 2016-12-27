package org.opencps.processmgt.util;

public class OutDateStatus {
	
	public boolean isOutDate() {
		return _isOutDate;
	}
	public void setIsOutDate(boolean isOutDate) {
		this._isOutDate = isOutDate;
	}
	public long getDaysOutdate() {
		return _daysOutdate;
	}
	public void setDaysOutdate(long daysOutdate) {
		this._daysOutdate = daysOutdate;
	}
	private boolean _isOutDate;
	private long _daysOutdate;
}
