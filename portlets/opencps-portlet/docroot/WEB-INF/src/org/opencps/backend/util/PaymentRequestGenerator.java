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

package org.opencps.backend.util;

import java.util.ArrayList;
import java.util.List;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;


/**
 * @author khoavd
 *
 */
public class PaymentRequestGenerator {
	
	public static void main(String[] args) {
		String pattern = "net=100 ship=300 tax=30 bank cash keypay $Thanh toan cho tao cai nay nhe$ $da thanh toan $" ;
		
		int totalPayment = getTotalPayment(pattern);
		
	    List<String> paymentMethod = getPaymentMethod(pattern);
	    
	    System.out.println(totalPayment);
	    
	    for (String method : paymentMethod) {
	    	System.out.println(method);
	    }
	    
	    List<String> messages = getMessagePayment(pattern);
	    
	    for (String msg : messages) {
	    	System.out.println(msg);
	    }
    }
	
	/**
	 * @param pattern
	 * @return
	 */
	public static int getTotalPayment(String pattern) {

		int total = 0;

		if (_validatorPattern(pattern)) {
			int net = _getAmount(pattern, PAY_AMOUNT_NET);

			int ship = _getAmount(pattern, PAY_AMOUNT_SHIP);

			int tax = _getAmount(pattern, PAY_AMOUNT_TAX);

			total = net + ship + tax;
		}

		return total;
	}

	/**
	 * @param pattern
	 * @return
	 */
	public static List<String> getPaymentMethod(String pattern) {

		List<String> paymentMethods = new ArrayList<String>();

		if (_validatorPattern(pattern)) {
			if (_checkcontains(pattern, PAY_METHOD_BANK)) {
				paymentMethods.add(PAY_METHOD_BANK);
			}

			if (_checkcontains(pattern, PAY_METHOD_CASH)) {
				paymentMethods.add(PAY_METHOD_CASH);
			}

			if (_checkcontains(pattern, PAY_METHOD_KEYPAY)) {
				paymentMethods.add(PAY_METHOD_KEYPAY);
			}

		}

		return paymentMethods;

	}

	/**
	 * Get payment message
	 * 
	 * @param pattern
	 * @return
	 */
	public static List<String> getMessagePayment(String pattern) {
		
		List<String> msgPayments = new ArrayList<String>();
		
		pattern = pattern.substring(pattern.indexOf(PAY_MESSAGE), pattern.lastIndexOf(PAY_MESSAGE));
		
		String [] splitPattern = StringUtil.split(pattern, PAY_MESSAGE);
		
		for (String element : splitPattern) {
			if (!element.trim().contentEquals("")) {
				msgPayments.add(StringUtil.trimLeading(element));
			}
		}

		return msgPayments;
	}

	private static boolean _validatorPattern(String pattern) {

		return true;
	}

	/**
	 * @param pattern
	 * @param content
	 * @return
	 */
	private static boolean _checkcontains(String pattern, String content) {

		boolean isContains = false;

		String[] splitPattern = StringUtil.split(pattern, StringPool.SPACE);

		for (String element : splitPattern) {
			if (Validator.equals(element, content)) {
				isContains = true;
				break;

			}
		}

		return isContains;
	}


	/**
	 * @param pattern
	 * @param content
	 * @return
	 */
	private static int _getAmount(String pattern, String content) {

		int amount = 0;

		String[] splitPattern = StringUtil.split(pattern, StringPool.SPACE);

		for (String element : splitPattern) {

			if (element.contains(content)) {

				String[] splipElement =
				    StringUtil.split(element, StringPool.EQUAL);

				if (splipElement.length == 2) {
					int tempAmount = GetterUtil.getInteger(splipElement[1], 0);
					
					if (tempAmount < 0) {
						tempAmount = 0;
					}
					
					amount = tempAmount;
				}
				
				break;

			}

		}

		return amount;

	}
	
	

	public static final String PAY_METHOD_BANK = "bank";
	public static final String PAY_METHOD_KEYPAY = "keypay";
	public static final String PAY_METHOD_CASH = "cash";
	public static final String PAY_AMOUNT_NET = "net";
	public static final String PAY_AMOUNT_SHIP = "ship";
	public static final String PAY_AMOUNT_TAX = "tax";
	public static final String PAY_MESSAGE = "$";
}
