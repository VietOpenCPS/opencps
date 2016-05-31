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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.Citizen;
import org.opencps.datamgt.service.DictItemLocalServiceUtil;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.dossiermgt.service.DossierFileLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil;
import org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.model.ProcessWorkflow;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.processmgt.service.ProcessStepLocalServiceUtil;
import org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;


/**
 * @author khoavd
 *
 */
public class BackendUtils {
	
	/**
	 * @param dossierId
	 * @return
	 */
	public static boolean checkPaymentStatus(long dossierId) {
		
		boolean paymentStatus = true;
		
		int countAllPayment = 0;
		
		int countPaymentComplated = 0;
		
		try {
	        countAllPayment = PaymentFileLocalServiceUtil.countAllPaymentFile(dossierId);
	        
	        countPaymentComplated = PaymentFileLocalServiceUtil.countPaymentFile(dossierId, 2);
	        
	        if (!((countAllPayment - countPaymentComplated) == 0)) {
	        	paymentStatus = false;
	        }
        }
        catch (Exception e) {
        	paymentStatus = false;
        }
		
		return paymentStatus;
	}
	
	/**
	 * @param dossierId
	 * @return
	 */
	public static long getGovAgencyOrgId(long dossierId) {
		
		long govAgencyOrgId = 0;
		
		try {
	        Dossier dossier = DossierLocalServiceUtil.getDossier(dossierId);
	        
	        long serviceConfigId = dossier.getServiceConfigId();
	        
	        ServiceConfig serviceConfig = ServiceConfigLocalServiceUtil.fetchServiceConfig(serviceConfigId);
	        
	        if (Validator.isNotNull(serviceConfig)) {
	        	govAgencyOrgId = serviceConfig.getGovAgencyOrganizationId();
	        }
        }
        catch (Exception e) {
	        // TODO: handle exception
        }
		
		return govAgencyOrgId;
	}
	
	public static int getDossierStatus(long dossierId, long fileGroupId) {
		int status = 0;
		
		try {
	        Dossier dossier = DossierLocalServiceUtil.fetchDossier(dossierId);
	        
	        if (Validator.isNotNull(dossier)) {
	        	status = dossier.getDossierStatus();
	        }
        }
        catch (Exception e) {
	        // TODO: handle exception
        }
		
		return status;
	}
	
	public static int getDossierStatus(long stepId) {
		
		int status = 0;
		
		try {
			ProcessStep step = ProcessStepLocalServiceUtil.fetchProcessStep(stepId);
			
			if (Validator.isNotNull(step)) {
				status = GetterUtil.getInteger(step.getDossierStatus());
			} 

        }
        catch (Exception e) {
	        return 0;
        }
		
		return status;
	}
	
	public static ProcessWorkflow getFirstProcessWorkflow(long serviceProcessId) {
		ProcessWorkflow flow = null;
		
		
		try {
			flow = ProcessWorkflowLocalServiceUtil.getFirstProcessWorkflow(serviceProcessId);
			
        }
        catch (Exception e) {
        }
		
		return flow;
	}
	

	
	/**
	 * @param serviceProcessId
	 * @return
	 */
	public static long getFristStepLocalService(long serviceProcessId) {
		
		ProcessWorkflow flow = null;
		
		long stepId = 0;
		
		try {
			flow = ProcessWorkflowLocalServiceUtil.getFirstProcessWorkflow(serviceProcessId);
			
			stepId = flow.getPostProcessStepId();
        }
        catch (Exception e) {
	        // TODO: handle exception
        }
		
		return stepId;
	}
	
	/**
	 * Get Dossier by DossierId
	 * 
	 * @param dossierId
	 * @return
	 */
	public static Dossier getDossier(long dossierId) {
		Dossier dossier = null;
		
		try {
	        dossier = DossierLocalServiceUtil.fetchDossier(dossierId);
        }
        catch (Exception e) {
	        _log.error(e);
        }
		
		return dossier;
	}
	

	/**
	 * Get ProcessOrder
	 * 
	 * @param dossierId
	 * @param fileGroupId
	 * @return
	 */
	public static ProcessOrder getProcessOrder(long dossierId, long fileGroupId) {

		ProcessOrder order = null;

		try {
			order =
			    ProcessOrderLocalServiceUtil. getProcessOrder(
			        dossierId, fileGroupId);
		}
		catch (Exception e) {
			return order;
		}
		return order;

	}
	
	// binding data
	
	public static String dataBinding(String sampleData,Citizen ownerCitizen, Business ownerBusiness, long dossierId) {
		String result = StringPool.BLANK;
		String _subjectName = StringPool.BLANK;
	 	String _subjectId = StringPool.BLANK;
	 	String _address = StringPool.BLANK;
	 	String _cityCode = StringPool.BLANK;
	 	String _cityName = StringPool.BLANK;
	 	String _districtCode = StringPool.BLANK;
	 	String _districtName = StringPool.BLANK;
	 	String _wardCode = StringPool.BLANK;
	 	String _wardName = StringPool.BLANK;
	 	String _contactName = StringPool.BLANK;
	 	String _contactTelNo = StringPool.BLANK;
	 	String _contactEmail = StringPool.BLANK;
	 	if(Validator.isNotNull(ownerCitizen)){
	 		_subjectName = ownerCitizen.getFullName();
	 		_subjectId = String.valueOf(ownerCitizen.getCitizenId());
	 		_address = ownerCitizen.getAddress();
	 		_cityCode = ownerCitizen.getCityCode();
	 		try {
	 			_cityName = DictItemLocalServiceUtil.getDictItemInuseByItemCode(1, ownerCitizen.getCityCode()).getItemName(Locale.getDefault());
			} catch (Exception e) {
				// TODO: handle exception
			}
	 		
	 		_districtCode = ownerCitizen.getDistrictCode();
	 		try {
	 			_districtName = DictItemLocalServiceUtil.getDictItemInuseByItemCode(1, ownerCitizen.getDistrictCode()).getItemName(Locale.getDefault());
			} catch (Exception e) {
				// TODO: handle exception
			}
	 		
	 		_wardCode = ownerCitizen.getWardCode();
	 		try {
	 			_wardName = DictItemLocalServiceUtil.getDictItemInuseByItemCode(1, ownerCitizen.getWardCode()).getItemName(Locale.getDefault());
			} catch (Exception e) {
				// TODO: handle exception
			}
	 		
	 		_contactName = ownerCitizen.getFullName();
	 		_contactTelNo = ownerCitizen.getTelNo();
	 		_contactEmail = ownerCitizen.getEmail();
	 	}
		if(Validator.isNotNull(ownerBusiness)){
			_subjectName = ownerBusiness.getName();
	 		_subjectId = String.valueOf(ownerBusiness.getBusinessId());
	 		_address = ownerBusiness.getAddress();
	 		_cityCode = ownerBusiness.getCityCode();
	 		try {
	 			_cityName = DictItemLocalServiceUtil.getDictItemInuseByItemCode(1, ownerBusiness.getCityCode()).getItemName(Locale.getDefault());
			} catch (Exception e) {
				// TODO: handle exception
			}
	 		
	 		_districtCode = ownerBusiness.getDistrictCode();
	 		try {
	 			_districtName = DictItemLocalServiceUtil.getDictItemInuseByItemCode(1, ownerBusiness.getDistrictCode()).getItemName(Locale.getDefault());
			} catch (Exception e) {
				// TODO: handle exception
			}
	 		
	 		_wardCode = ownerBusiness.getWardCode();
	 		try {
	 			_wardName = DictItemLocalServiceUtil.getDictItemInuseByItemCode(1, ownerBusiness.getWardCode()).getItemName(Locale.getDefault());
			} catch (Exception e) {
				// TODO: handle exception
			}
	 		
	 		_contactName = ownerBusiness.getShortName();
	 		_contactTelNo = ownerBusiness.getTelNo();
	 		_contactEmail = ownerBusiness.getEmail();
	 	}
		
		try {
			JSONObject jsonSampleData = new JSONObject(sampleData);
			Map<String, Object> jsonMap = jsonToMap(jsonSampleData);
			for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
//				System.out.println(entry.getKey() + ": " + entry.getValue());
				String value = String.valueOf(entry.getValue());
				if(value.startsWith("_")){
					if(value.equals("_subjectName")){
						jsonMap.put(entry.getKey(), _subjectName);
					}else if(value.equals("_subjectId")){
						jsonMap.put(entry.getKey(), _subjectId);
					}else if(value.equals("_address")){
						jsonMap.put(entry.getKey(), _address);
					}else if(value.equals("_cityCode")){
						jsonMap.put(entry.getKey(), _cityCode);
					}else if(value.equals("_cityName")){
						jsonMap.put(entry.getKey(), _cityName);
					}else if(value.equals("_districtCode")){
						jsonMap.put(entry.getKey(), _districtCode);
					}else if(value.equals("_districtName")){
						jsonMap.put(entry.getKey(), _districtName);
					}else if(value.equals("_wardCode")){
						jsonMap.put(entry.getKey(), _wardCode);
					}else if(value.equals("_wardName")){
						jsonMap.put(entry.getKey(), _wardName);
					}else if(value.equals("_contactName")){
						jsonMap.put(entry.getKey(), _contactName);
					}else if(value.equals("_contactTelNo")){
						jsonMap.put(entry.getKey(), _contactTelNo);
					}else if(value.equals("_contactEmail")){
						jsonMap.put(entry.getKey(), _contactEmail);
					}
				}else if(value.startsWith("#") && value.contains("@")){
					String newString = value.substring(1);
					String[] stringSplit = newString.split("@");
					String variable = stringSplit[0];
					String paper = stringSplit[1];
					try {
						DossierFile dossierFile = DossierFileLocalServiceUtil.fetchByTemplateFileNoDossierId_Last(dossierId, paper);
						if(Validator.isNotNull(dossierFile) && Validator.isNotNull(dossierFile.getFormData())){
							JSONObject jsonOtherData = new JSONObject(dossierFile.getFormData());
							Map<String, Object> jsonOtherMap = jsonToMap(jsonOtherData);
							jsonMap.put(entry.getKey(), String.valueOf(jsonOtherMap.get(variable)));
						}
					} catch (SystemException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			jsonMap.put("_ngayNopDon", ngayNopDon());
			jsonSampleData = new JSONObject(jsonMap);
			result = jsonSampleData.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static String ngayNopDon() {
		Date date = new Date();
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			int day = calendar.get(Calendar.DAY_OF_MONTH);
			String sDay = "";
			if (day < 10) {
				sDay = "0" + day;
			} else {
				sDay = day + "";
			}
			
			int month = calendar.get(Calendar.MONTH) + 1;
			String sMonth = "";
			if (month < 10) {
				sMonth = "0" + month;
			} else {
				sMonth = month + "";
			}
			
			int year = calendar.get(Calendar.YEAR);
		
			return  LanguageUtil.get(Locale.getDefault(), "sDay-cps") + " " + sDay + " " +
					LanguageUtil.get(Locale.getDefault(), "sMonth-cps") + " " + sMonth + " "+ 
					LanguageUtil.get(Locale.getDefault(), "year-cps") + " " + year;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public static Map<String, Object> jsonToMap(JSONObject json) {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(json != JSONObject.NULL) {
            try {
				retMap = toMap(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
	private static Log _log = LogFactoryUtil.getLog(BackendUtils.class);
}
