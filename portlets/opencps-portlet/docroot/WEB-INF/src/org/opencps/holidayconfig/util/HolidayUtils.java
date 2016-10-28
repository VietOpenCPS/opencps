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
package org.opencps.holidayconfig.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
















import org.opencps.holidayconfig.model.HolidayConfig;
import org.opencps.holidayconfig.model.HolidayConfigExtend;
import org.opencps.holidayconfig.service.HolidayConfigExtendLocalServiceUtil;
import org.opencps.holidayconfig.service.HolidayConfigLocalServiceUtil;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

public class HolidayUtils {
	
	private static Log _log = LogFactoryUtil.getLog(HolidayUtils.class);
	
	private final static String SATURDAY = "SATURDAY";
	private final static String SUNDAY = "SUNDAY";
	private final static int ACTIVE = 1;
	
	public static Calendar getEndDate(Date baseDate,long daysDuration){
		
		if(baseDate == null){
	        baseDate = new Date();
	    }
		
		Calendar baseDateCal = Calendar.getInstance();
	    baseDateCal.setTime(baseDate);
		
		
		try{
			
			int saturdayIsNotHoliday = 0;
			int sundayIsNotHoliday = 0;
			
			/*Kiểm tra xem flag sunday,saturday có được tính là ngày nghỉ không*/
	    	List<HolidayConfigExtend> holidayConfigExtendList = HolidayConfigExtendLocalServiceUtil
	    			.getHolidayConfigExtends(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
	    	
	    	for(HolidayConfigExtend holidayConfigExtend:holidayConfigExtendList){
	    		
	    		if(holidayConfigExtend.equals(SATURDAY)){
	    			saturdayIsNotHoliday = holidayConfigExtend.getActive();
	    		}
	    		
	    		if(holidayConfigExtend.equals(SUNDAY)){
	    			sundayIsNotHoliday = holidayConfigExtend.getActive();
	    		}
	    	}
		
			for(int i=0;i<daysDuration;i++){
				
				baseDateCal.add(Calendar.DATE,1);
				
				baseDateCal = checkDay(baseDateCal,baseDate,null,saturdayIsNotHoliday,sundayIsNotHoliday);
				
			}
		}catch(Exception e){
			_log.error(e);
		}
		
		return baseDateCal;
	}
	
	private static Calendar checkDay (Calendar baseDateCal,Date baseDate,List<HolidayConfig> holidayConfigList,
			int saturdayIsNotHoliday,int sundayIsNotHoliday){
		
		
		boolean isHoliday = false;
		
		try{
		
			if(Validator.isNull(holidayConfigList) || (holidayConfigList.size() <= 0)){
				holidayConfigList = HolidayConfigLocalServiceUtil.getHolidayConfig(ACTIVE);
			}
			
			/*Kiểm tra ngày xử lý có trùng vào list ngày nghỉ đã config hay chưa
			 * Nếu trùng thì sẽ + thêm ngày xử lý*/
			isHoliday = isHoliday(baseDateCal,holidayConfigList);
			
			
			
			if(baseDateCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY 
					|| baseDateCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || isHoliday)
			{
	    	
		    	baseDateCal = isHolidayCal(baseDateCal,holidayConfigList);
		    	
		    	/*Nếu flag saturday,sunday bật thì ko tính là ngày nghỉ*/
		    	
		    	if(saturdayIsNotHoliday == ACTIVE){
		    	
		    		baseDateCal = checkSaturday(baseDateCal);
		    	}
		    	
		    	if(sundayIsNotHoliday == ACTIVE){
		    		baseDateCal = checkSunday(baseDateCal);
		    	}
		    	
		    	checkDay(baseDateCal,baseDate,holidayConfigList,saturdayIsNotHoliday,sundayIsNotHoliday);
			}else{
				
			}
		}catch (Exception e){
			_log.error(e);
		}
		
		return baseDateCal;
	}
	
	private static Calendar checkSaturday (Calendar baseDateCal){
		
		if(baseDateCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
    		baseDateCal.add(Calendar.DATE,2);
        }
		return baseDateCal;
	}
	
	private static Calendar checkSunday (Calendar baseDateCal){
		
		if(baseDateCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
    		baseDateCal.add(Calendar.DATE,1);		           
        }
		return baseDateCal;
	}
	
	
	
	private static Calendar isHolidayCal(Calendar baseDateCal,List<HolidayConfig> holidayConfigList){
		
       
       int baseDate = 0;
       int holidayDate = 0;
       Calendar holidayCal = Calendar.getInstance();
        
       try {
    	   
    	   if(Validator.isNull(holidayConfigList) || (holidayConfigList.size() <=0)){
   			
				holidayConfigList = HolidayConfigLocalServiceUtil.getHolidayConfig(ACTIVE);
			}
			
			for(int i =0;i<holidayConfigList.size();i++){
				
				
				holidayCal.setTime(holidayConfigList.get(i).getHoliday());
	    		
				baseDate = baseDateCal.get(Calendar.DATE);
				holidayDate = holidayCal.get(Calendar.DATE);

	    		if(baseDate == holidayDate){
	    			baseDateCal.add(Calendar.DATE,1);
	    		}
	    	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			_log.info(e);
		}
		return baseDateCal;
	}
	
	
	
	private static boolean isHoliday(Calendar baseDateCal,List<HolidayConfig> holidayConfigList){
		
		
		int baseDate = 0;
		int holidayDate = 0;
		Calendar holidayCal = Calendar.getInstance();

		try{
			
			if(Validator.isNull(holidayConfigList) || (holidayConfigList.size() <=0)){
			
				holidayConfigList = HolidayConfigLocalServiceUtil.getHolidayConfig(ACTIVE);
			}
			
			for(int i =0;i<holidayConfigList.size();i++){
				
				holidayCal.setTime(holidayConfigList.get(i).getHoliday());
				
				baseDate = baseDateCal.get(Calendar.DATE);
				holidayDate = holidayCal.get(Calendar.DATE);
				
	    		if(baseDate == holidayDate){
	    			return true;
	    		}
	    	}
		}catch(Exception e){
			_log.error(e);
		}
		
		return false;
		
	}
}