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

package org.opencps.paymentmgt.service.persistence;

import java.util.Iterator;
import java.util.List;

import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.paymentmgt.model.impl.PaymentFileImpl;
import org.opencps.processmgt.model.ServiceProcess;
import org.opencps.processmgt.model.impl.ServiceProcessImpl;
import org.opencps.processmgt.service.persistence.ServiceProcessFinder;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

/**
 * @author trungdk
 */
public class PaymentFileFinderImpl extends BasePersistenceImpl<PaymentFile>
implements PaymentFileFinder {

	public final static String SQL_PAYMENT_FINDER =
					PaymentFileFinder.class.getName() + ".searchPaymentFile";
	public final static String SQL_PAYMENT_COUNT =
					PaymentFileFinder.class.getName() + ".countPaymentFile";

	/**
	 * Search Process with keywords
	 * 
	 * @param groupId
	 * @param keywords
	 * @param start
	 * @param end
	 * @return
				 */
	public List<PaymentFile> searchPaymentFiles(
		   long groupId, int paymentStatus, long govAgencyOrganizationId, String keywords, int start, int end)throws SystemException {

			String[] names = null;
			boolean andOperator = false;
			if (Validator.isNotNull(keywords)) {
				
				names = new String[]{
						StringUtil.quote(
							StringUtil.toLowerCase(keywords).trim(), 
							StringPool.PERCENT)};
			}
			else {
				andOperator = true;
			}

			return _searchPaymentFiles(groupId, paymentStatus,govAgencyOrganizationId, andOperator, names, start, end);
	}

	/**
	 * Count Process with keywords
	 * 
	 * @param groupId
	 * @param keywords
	 * @return
	 */
	public int countPaymentFiles(long groupId, int paymentStatus, long govAgencyOrganizationId, String keywords)throws SystemException {
			String[] names = null;
			boolean andOperator = false;
			if (Validator.isNotNull(keywords)) {
				
				names = new String[]{
						StringUtil.quote(
							StringUtil.toLowerCase(keywords).trim(), 
							StringPool.PERCENT)};
			}
			else {
				andOperator = true;
			}
			return _countPaymentFiles(groupId, paymentStatus, govAgencyOrganizationId, andOperator, names);
	}

	private List<PaymentFile> _searchPaymentFiles(
			long groupId, int paymentStatus, long govAgencyOrganizationId, boolean andOperator, String[] keywords, int start, int end)throws SystemException {
			
			Session session = null;
			try {
				session = openSession();
	 			String sql = CustomSQLUtil.get(SQL_PAYMENT_FINDER);
	 			
	 			if(Validator.isNotNull(keywords) && keywords.length >0){
				
					sql = CustomSQLUtil.replaceKeywords(sql, "lower(opencps_payment_file.receptionNo)",StringPool.LIKE, true, keywords);
					
					
					sql = CustomSQLUtil.replaceKeywords(sql, "lower(opencps_payment_file.paymentName)",StringPool.LIKE, true, keywords);
					
					
					sql = CustomSQLUtil.replaceKeywords(sql, "lower(opencps_acc_citizen.fullName)",StringPool.LIKE, true, keywords);
					
					sql = CustomSQLUtil.replaceKeywords(sql, "lower(opencps_acc_business.name)",StringPool.LIKE, true, keywords);
	 			}else{
	 				sql = StringUtil.replace(sql,
	 						"AND ((lower(opencps_dossier.receptionNo) LIKE ? [$AND_OR_NULL_CHECK$]) OR (lower(opencps_payment_file.paymentName) LIKE ? [$AND_OR_NULL_CHECK$]) OR (lower(opencps_acc_citizen.fullName) LIKE ? [$AND_OR_NULL_CHECK$]) OR (lower(opencps_acc_business.name) LIKE ? [$AND_OR_NULL_CHECK$]))"
	 						,StringPool.BLANK);
	 			}
				
				if(paymentStatus < 0) {
					sql = StringUtil.replace(sql, "AND (opencps_payment_file.paymentStatus = ?)", StringPool.BLANK);
				}		
				
				sql = CustomSQLUtil.replaceAndOperator(sql, andOperator);
				
				SQLQuery q = session.createSQLQuery(sql);

				q.setCacheable(false);

				q.addEntity("PaymentFile", PaymentFileImpl.class);

				QueryPos qPos = QueryPos.getInstance(q);
				
				if(Validator.isNotNull(keywords) && keywords.length >0){

					qPos.add(keywords, 8);
				}
				if(paymentStatus >-1) {
					qPos.add(paymentStatus);
				}
				
				qPos.add(govAgencyOrganizationId);
				
				return (List<PaymentFile>) QueryUtil.list(
							    q, getDialect(), start, end);
			}catch (Exception e) {
				
				throw new SystemException(e);
				
			}
			finally {
				closeSession(session);
			}
	}

	private int _countPaymentFiles(
			long groupId, int paymentStatus, long govAgencyOrganizationId, boolean andOperator, String[] keywords)throws SystemException {

		Session session = null;
		try {
			session = openSession();
			String sql = CustomSQLUtil.get(SQL_PAYMENT_COUNT);
			
			if(Validator.isNotNull(keywords) && keywords.length >0){
			
				sql = CustomSQLUtil.replaceKeywords(sql, "lower(opencps_payment_file.receptionNo)",StringPool.LIKE, true, keywords);
				
				sql = CustomSQLUtil.replaceKeywords(
							        sql, "lower(opencps_payment_file.paymentName)",
							        StringPool.LIKE, true, keywords);
				
				sql = CustomSQLUtil.replaceKeywords(
			        sql, "lower(opencps_acc_citizen.fullName)",
			        StringPool.LIKE, true, keywords);
				
				sql = CustomSQLUtil.replaceKeywords(
			        sql, "lower(opencps_acc_business.name)",
			        StringPool.LIKE, true, keywords);
			}else{
	 				sql = StringUtil.replace(sql,
	 						"AND ((lower(opencps_dossier.receptionNo) LIKE ? [$AND_OR_NULL_CHECK$]) OR (lower(opencps_payment_file.paymentName) LIKE ? [$AND_OR_NULL_CHECK$]) OR (lower(opencps_acc_citizen.fullName) LIKE ? [$AND_OR_NULL_CHECK$]) OR (lower(opencps_acc_business.name) LIKE ? [$AND_OR_NULL_CHECK$]))"
	 						,StringPool.BLANK);
	 			}
			
			if(paymentStatus <0) {
				sql = StringUtil.replace(sql, "AND (opencps_payment_file.paymentStatus = ?)", StringPool.BLANK);
			}
			
			sql = CustomSQLUtil.replaceAndOperator(sql, andOperator);

			SQLQuery q = session.createSQLQuery(sql);
			
			q.setCacheable(false);

			q.addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos.getInstance(q);

			if(Validator.isNotNull(keywords) && keywords.length >0){

				qPos.add(keywords, 8);
			}

			if(paymentStatus>-1) {
				qPos.add(paymentStatus);
			}
			
			qPos.add(govAgencyOrganizationId);
			
			Iterator<Integer> itr = q.iterate();

			
			if (itr.hasNext()) {
			
				Integer count = itr.next();

				
				if (count != null) {
				
					return count.intValue();
					
				}
				
			}

			
			return 0;
			
		}
		
		catch (Exception e) {
				throw new SystemException(e);
		}
		
		finally {
		
			closeSession(session);
			
		}


	}
	
	public int countCustomerPaymentFile(long groupId, String keyword, boolean isCitizen, long customerId, int paymentStatus) {

		String[] keywords = null;

		boolean andOperator = false;

		if (Validator
		    .isNotNull(keyword)) {
			keywords = new String[]{"%"+keyword+"%"};/*CustomSQLUtil
			    .keywords(keyword);*/
		}
		else {
			andOperator = true;
		}

		return countCustomerPaymentFile(groupId, keywords, isCitizen, customerId, paymentStatus, andOperator);
	}

	private int countCustomerPaymentFile(
	    long groupId, String[] keywords, boolean isCitizen, long customerId, int paymentStatus,
	    boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
			    .get(COUNT_CUSTOMER_PAYMENTFILE);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
				    .replaceKeywords(
				        sql, "lower(opencps_payment_file.paymentName)",
				        StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
				    .replaceKeywords(
				        sql, "lower(opencps_payment_file.accountUserName)",
				        StringPool.LIKE, true, keywords);
				
				sql = CustomSQLUtil
					    .replaceKeywords(
					        sql, "lower(opencps_dossier.receptionNo)",
					        StringPool.LIKE, true, keywords);
			}

			

			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
				    .replace(
				        sql,
				        "AND (lower(opencps_payment_file.paymentName) LIKE ? [$AND_OR_NULL_CHECK$])",
				        StringPool.BLANK);

				sql = StringUtil
				    .replace(
				        sql,
				        "OR (lower(opencps_payment_file.accountUserName) LIKE ? [$AND_OR_NULL_CHECK$])",
				        StringPool.BLANK);	
				
				sql = StringUtil
					    .replace(
					        sql,
					        "OR (lower(opencps_dossier.receptionNo) LIKE ? [$AND_OR_NULL_CHECK$])",
					        StringPool.BLANK);
			}

			if (paymentStatus < 0) {
				sql = StringUtil
				    .replace(
				        sql, "AND (opencps_payment_file.paymentStatus = ?)",
				        StringPool.BLANK);
			}
			if (isCitizen) {
				sql = StringUtil
							    .replace(
							        sql, "AND (opencps_payment_file.ownerOrganizationId=?)",
							        StringPool.BLANK);				
			}
			else {
				sql = StringUtil
							    .replace(
							        sql, "AND (opencps_payment_file.ownerUserId=?)",
							        StringPool.BLANK);								
			}

			sql = CustomSQLUtil
						    .replaceAndOperator(sql, andOperator);

			SQLQuery q = session
			    .createSQLQuery(sql);

			q
			    .addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos
			    .getInstance(q);

			//qPos.add(groupId);
			
			qPos.add(customerId);
			
			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
			}

			if (paymentStatus >= 0) {
				qPos
				    .add(paymentStatus);
			}

			Iterator<Integer> itr = q
			    .iterate();

			if (itr
			    .hasNext()) {
				Integer count = itr
				    .next();

				if (count != null) {
					return count
					    .intValue();
				}
			}

			return 0;

		}
		catch (Exception e) {
			_log
			    .error(e);
		}
		finally {
			closeSession(session);
		}

		return 0;
	}

	public List<PaymentFile> searchCustomerPaymentFile(
	    long groupId, String keyword, boolean isCitizen, long customerId, int paymentStatus, int start, int end,
	    OrderByComparator obc) {

		String[] keywords = null;
		boolean andOperator = false;
		if (Validator
		    .isNotNull(keyword)) {
			keywords = new String[]{"%"+keyword+"%"};/*CustomSQLUtil
			    .keywords(keyword);*/
		}
		else {
			andOperator = true;
		}
		return searchCustomerPaymentFile(
		    groupId, keywords, isCitizen, customerId, paymentStatus, andOperator, start, end, obc);
	}

	private List<PaymentFile> searchCustomerPaymentFile(
	    long groupId, String[] keywords, boolean isCitizen, long customerId, int paymentStatus, boolean andOperator,
	    int start, int end, OrderByComparator obc) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
			    .get(SEARCH_CUSTOMER_PAYMENTFILE);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
							    .replaceKeywords(
							        sql, "lower(opencps_payment_file.paymentName)",
							        StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
				    .replaceKeywords(
				        sql, "lower(opencps_payment_file.accountUserName)",
				        StringPool.LIKE, true, keywords);
				
				sql = CustomSQLUtil
					    .replaceKeywords(
					        sql, "lower(opencps_dossier.receptionNo)",
					        StringPool.LIKE, true, keywords);
			}

			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
							    .replace(
							        sql,
							        "AND (lower(opencps_payment_file.paymentName) LIKE ? [$AND_OR_NULL_CHECK$])",
							        StringPool.BLANK);

				sql = StringUtil
				    .replace(
				        sql,
				        "OR (lower(opencps_payment_file.accountUserName) LIKE ? [$AND_OR_NULL_CHECK$])",
				        StringPool.BLANK);
				
				sql = StringUtil
					    .replace(
					        sql,
					        "OR (lower(opencps_dossier.receptionNo) LIKE ? [$AND_OR_NULL_CHECK$])",
					        StringPool.BLANK);		
			}

			if (paymentStatus < 0) {
				sql = StringUtil
				    .replace(
				        sql, "AND (opencps_payment_file.paymentStatus = ?)",
				        StringPool.BLANK);
			}
			if (isCitizen) {
				sql = StringUtil
							    .replace(
							        sql, "AND (opencps_payment_file.ownerOrganizationId=?)",
							        StringPool.BLANK);				
			}
			else {
				sql = StringUtil
							    .replace(
							        sql, "AND (opencps_payment_file.ownerUserId=?)",
							        StringPool.BLANK);								
			}
			
			sql = CustomSQLUtil
						    .replaceAndOperator(sql, andOperator);

			SQLQuery q = session
			    .createSQLQuery(sql);

			q.addEntity("PaymentFile", PaymentFileImpl.class);

			QueryPos qPos = QueryPos
			    .getInstance(q);

			//qPos.add(groupId);

			qPos.add(customerId);
			
			if (keywords != null && keywords.length > 0) {
				qPos
				    .add(keywords, 2);
				qPos
				    .add(keywords, 2);
				qPos
			    	.add(keywords, 2);
			}

			if (paymentStatus >= 0) {
				qPos
				    .add(paymentStatus);
			}

			return (List<PaymentFile>) QueryUtil
			    .list(q, getDialect(), start, end);
		}
		catch (Exception e) {
			_log
			    .error(e);
		}
		finally {
			closeSession(session);
		}

		return null;
	}
	
	public int countCustomerPaymentFileNewRequest(long groupId, String keyword, boolean isCitizen, long customerId, int[] paymentStatus) {

		String[] keywords = null;

		boolean andOperator = false;

		if (Validator
		    .isNotNull(keyword)) {
			keywords = new String[]{keyword};/*CustomSQLUtil
			    .keywords(keyword);*/
		}
		else {
			andOperator = true;
		}

		return countCustomerPaymentFileNewRequest(groupId, keywords, isCitizen, customerId, paymentStatus, andOperator);
	}

	private int countCustomerPaymentFileNewRequest(
	    long groupId, String[] keywords, boolean isCitizen, long customerId, int[] paymentStatus,
	    boolean andOperator) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
			    .get(COUNT_CUSTOMER_PAYMENTFILE_NEW_REQUEST);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
				    .replaceKeywords(
				        sql, "lower(opencps_payment_file.paymentName)",
				        StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
				    .replaceKeywords(
				        sql, "lower(opencps_payment_file.accountUserName)",
				        StringPool.LIKE, true, keywords);
				
				sql = CustomSQLUtil
					    .replaceKeywords(
					        sql, "lower(opencps_dossier.receptionNo)",
					        StringPool.LIKE, true, keywords);
			}

			

			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
				    .replace(
				        sql,
				        "AND (lower(opencps_payment_file.paymentName) LIKE ? [$AND_OR_NULL_CHECK$])",
				        StringPool.BLANK);

				sql = StringUtil
				    .replace(
				        sql,
				        "OR (lower(opencps_payment_file.accountUserName) LIKE ? [$AND_OR_NULL_CHECK$])",
				        StringPool.BLANK);
				
				sql = StringUtil
					    .replace(
					        sql,
					        "OR (lower(opencps_dossier.receptionNo) LIKE ? [$AND_OR_NULL_CHECK$])",
					        StringPool.BLANK);
			}

			if (Validator.isNull(paymentStatus) || paymentStatus.length <= 0) {
				sql = StringUtil
				    .replace(
				        sql, "AND (opencps_payment_file.paymentStatus IN (?) )",
				        StringPool.BLANK);
			}else{
				sql = StringUtil.replace(sql,
						"AND (opencps_payment_file.paymentStatus IN (?) )",
						"AND (opencps_payment_file.paymentStatus IN (" +
							StringUtil.merge(paymentStatus) + ") )");
			}
			if (isCitizen) {
				sql = StringUtil
							    .replace(
							        sql, "AND (opencps_payment_file.ownerOrganizationId=?)",
							        StringPool.BLANK);				
			}
			else {
				sql = StringUtil
							    .replace(
							        sql, "AND (opencps_payment_file.ownerUserId=?)",
							        StringPool.BLANK);								
			}

			sql = CustomSQLUtil
						    .replaceAndOperator(sql, andOperator);

			SQLQuery q = session
			    .createSQLQuery(sql);

			q
			    .addScalar(COUNT_COLUMN_NAME, Type.INTEGER);

			QueryPos qPos = QueryPos
			    .getInstance(q);

			//qPos.add(groupId);
			
			qPos.add(customerId);
			
			if (keywords != null && keywords.length > 0) {
				qPos
				    .add(keywords, 2);
				qPos
				    .add(keywords, 2);
				qPos
			    	.add(keywords, 2);
			}

			Iterator<Integer> itr = q
			    .iterate();

			if (itr
			    .hasNext()) {
				Integer count = itr
				    .next();

				if (count != null) {
					return count
					    .intValue();
				}
			}

			return 0;

		}
		catch (Exception e) {
			_log
			    .error(e);
		}
		finally {
			closeSession(session);
		}

		return 0;
	}

	public List<PaymentFile> searchCustomerPaymentFileNewRequest(
	    long groupId, String keyword, boolean isCitizen, long customerId, int[] paymentStatus, int start, int end,
	    OrderByComparator obc) {

		String[] keywords = null;
		boolean andOperator = false;
		if (Validator
		    .isNotNull(keyword)) {
			keywords = new String[]{keyword};/*CustomSQLUtil
			    .keywords(keyword);*/
		}
		else {
			andOperator = true;
		}
		return searchCustomerPaymentFileNewRequest(
		    groupId, keywords, isCitizen, customerId, paymentStatus, andOperator, start, end, obc);
	}

	private List<PaymentFile> searchCustomerPaymentFileNewRequest(
	    long groupId, String[] keywords, boolean isCitizen, long customerId, int[] paymentStatus, boolean andOperator,
	    int start, int end, OrderByComparator obc) {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil
			    .get(SEARCH_CUSTOMER_PAYMENTFILE_NEW_REQUEST);

			if (keywords != null && keywords.length > 0) {
				sql = CustomSQLUtil
							    .replaceKeywords(
							        sql, "lower(opencps_payment_file.paymentName)",
							        StringPool.LIKE, true, keywords);

				sql = CustomSQLUtil
				    .replaceKeywords(
				        sql, "lower(opencps_payment_file.accountUserName)",
				        StringPool.LIKE, true, keywords);
				
				sql = CustomSQLUtil
					    .replaceKeywords(
					        sql, "lower(opencps_dossier.receptionNo)",
					        StringPool.LIKE, true, keywords);
			}

			if (keywords == null || keywords.length == 0) {
				sql = StringUtil
							    .replace(
							        sql,
							        "AND (lower(opencps_payment_file.paymentName) LIKE ? [$AND_OR_NULL_CHECK$])",
							        StringPool.BLANK);

				sql = StringUtil
				    .replace(
				        sql,
				        "OR (lower(opencps_payment_file.accountUserName) LIKE ? [$AND_OR_NULL_CHECK$])",
				        StringPool.BLANK);
				
				sql = StringUtil
					    .replace(
					        sql,
					        "OR (lower(opencps_dossier.receptionNo) LIKE ? [$AND_OR_NULL_CHECK$])",
					        StringPool.BLANK);
			}

			if (Validator.isNull(paymentStatus) || paymentStatus.length <= 0) {
				sql = StringUtil
				    .replace(
				        sql, "AND (opencps_payment_file.paymentStatus IN (?) )",
				        StringPool.BLANK);
			}else{
				sql = StringUtil.replace(sql,
						"AND (opencps_payment_file.paymentStatus IN (?) )",
						"AND (opencps_payment_file.paymentStatus IN (" +
							StringUtil.merge(paymentStatus) + ") )");
			}
			if (isCitizen) {
				sql = StringUtil
							    .replace(
							        sql, "AND (opencps_payment_file.ownerOrganizationId=?)",
							        StringPool.BLANK);				
			}
			else {
				sql = StringUtil
							    .replace(
							        sql, "AND (opencps_payment_file.ownerUserId=?)",
							        StringPool.BLANK);								
			}
			
			sql = CustomSQLUtil
						    .replaceAndOperator(sql, andOperator);

			SQLQuery q = session
			    .createSQLQuery(sql);

			q
			    .addEntity("PaymentFile", PaymentFileImpl.class);

			QueryPos qPos = QueryPos
			    .getInstance(q);

			//qPos.add(groupId);

			qPos.add(customerId);
			
			if (keywords != null && keywords.length > 0) {
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
				qPos
					.add(keywords, 2);
			}

			return (List<PaymentFile>) QueryUtil
			    .list(q, getDialect(), start, end);
		}
		catch (Exception e) {
			_log
			    .error(e);
		}
		finally {
			closeSession(session);
		}

		return null;
	}
	public static final String SEARCH_CUSTOMER_PAYMENTFILE = PaymentFileFinder.class
	    .getName() + ".searchCustomerPaymentFile";
	public static final String COUNT_CUSTOMER_PAYMENTFILE = PaymentFileFinder.class
	    .getName() + ".countCustomerPaymentFile";
	
	public static final String SEARCH_CUSTOMER_PAYMENTFILE_NEW_REQUEST = PaymentFileFinder.class
		    .getName() + ".searchCustomerPaymentFileNewRequest";
		public static final String COUNT_CUSTOMER_PAYMENTFILE_NEW_REQUEST = PaymentFileFinder.class
		    .getName() + ".countCustomerPaymentFileNewRequest";

	private Log _log = LogFactoryUtil
	    .getLog(PaymentFileFinder.class
	        .getName());
	

}
