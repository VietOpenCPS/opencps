//
//package org.opencps.processmgt.util.comparator;
//
//import org.opencps.processmgt.util.ProcessOrderUtils.CustomDisPlay;
//
//import com.liferay.portal.kernel.util.OrderByComparator;
//
//public class ThuTucComparator extends OrderByComparator {
//
//	public static final String ORDER_BY_ASC = "opencps_serviceinfo.serviceName ASC";
//
//	public static final String ORDER_BY_DESC = "opencps_serviceinfo.serviceName DESC";
//
//	public static final String[] ORDER_BY_FIELDS = {
//		"serviceName"
//	};
//
//	public ThuTucComparator() {
//
//		this(false);
//	}
//
//	public ThuTucComparator(boolean ascending) {
//
//		_ascending = ascending;
//	}
//
//	@Override
//	public int compare(Object arg0, Object arg1) {
//
//		// TODO Auto-generated method stub
//		CustomDisPlay customDisPlay1 = (CustomDisPlay) arg0;
//		CustomDisPlay customDisPlay2 = (CustomDisPlay) arg1;
//		int value =
//		    customDisPlay1.getThuTuc().compareTo(
//		        customDisPlay2.getThuTuc());
//
//		return _ascending ? value : -value;
//	}
//
//	@Override
//	public String getOrderBy() {
//
//		if (_ascending) {
//			return ORDER_BY_ASC;
//		}
//		else {
//			return ORDER_BY_DESC;
//		}
//	}
//
//	@Override
//	public String[] getOrderByFields() {
//
//		return ORDER_BY_FIELDS;
//	}
//
//	@Override
//	public boolean isAscending() {
//
//		return _ascending;
//	}
//
//	private boolean _ascending;
//}
