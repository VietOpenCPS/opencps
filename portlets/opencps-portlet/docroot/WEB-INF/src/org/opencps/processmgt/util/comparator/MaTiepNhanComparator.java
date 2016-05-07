//package org.opencps.processmgt.util.comparator;
//
//import org.opencps.processmgt.util.ProcessOrderUtils.CustomDisPlay;
//
//import com.liferay.portal.kernel.util.OrderByComparator;
//
//
//public class MaTiepNhanComparator extends OrderByComparator{
//
//	public static final String ORDER_BY_ASC =
//					"dossier.receptionNo ASC";
//
//	public static final String ORDER_BY_DESC =
//					"dossier.receptionNo DESC";
//
//	public static final String[] ORDER_BY_FIELDS = {
//					"receptionNo"
//				};
//	public MaTiepNhanComparator() {
//
//	    this(false);
//    }
//	
//	public MaTiepNhanComparator(boolean ascending) {
//
//		_ascending = ascending;
//    }
//	@Override
//    public int compare(Object arg0, Object arg1) {
//
//		// TODO Auto-generated method stub
//		CustomDisPlay customDisPlay1 = (CustomDisPlay) arg0;
//		CustomDisPlay customDisPlay2 = (CustomDisPlay) arg1;
//		int value = customDisPlay1.getMaTiepNhan().compareTo(customDisPlay2.getMaTiepNhan());
//	    
//		return _ascending ? value : -value;
//    }
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
