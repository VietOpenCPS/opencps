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

import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.Citizen;
import org.opencps.datamgt.service.DictItemLocalServiceUtil;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.FileGroup;
import org.opencps.dossiermgt.portlet.DossierMgtFrontOfficePortlet;
import org.opencps.dossiermgt.service.DossierFileLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.dossiermgt.service.FileGroupLocalServiceUtil;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author khoavd
 *
 */
public class AutoFillFormData {
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
			if(Validator.isNull(sampleData)){
				sampleData = "{}";
			}
			JSONObject jsonSampleData = JSONFactoryUtil.createJSONObject(sampleData);
			Map<String, Object> jsonMap = jsonToMap(jsonSampleData);
			for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
//				System.out.println(entry.getKey() + ": " + entry.getValue());
				String value = String.valueOf(entry.getValue());
				if(value.startsWith("_") && !value.contains(":")){
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
					}else if(value.equals("_ngayNopDon")){
						jsonMap.put(entry.getKey(), ngayNopDon());
					}else if(value.equals("_donViThucHien")){
						if(dossierId > 0){
							try {
								Dossier dossier = DossierLocalServiceUtil.fetchDossier(dossierId);
								jsonMap.put(entry.getKey(), dossier.getGovAgencyName());
							} catch (SystemException e) {
								// TODO Auto-generated catch block
								_log.error(e);
								
							}
						}
					}else if(value.equals("_thanhPhanHoSoCon")){
						if(dossierId > 0){
							try {
								List<FileGroup> fileGroups = FileGroupLocalServiceUtil.getFileGroupByDossierId(dossierId);
								JSONArray arrays = JSONFactoryUtil.createJSONArray();
								JSONObject obJsonObject = null;
								int i = 0;
								for (FileGroup fileGroup : fileGroups) {
									i++;
									obJsonObject = JSONFactoryUtil.createJSONObject();
									obJsonObject.put("stt", i);
									obJsonObject.put("tenSanPham", fileGroup.getDisplayName());
									arrays.put(obJsonObject);
								}
								jsonMap.put(entry.getKey(), arrays);
							} catch (SystemException e) {
								// TODO Auto-generated catch block
								_log.error(e);
							}
						}
					}
					
				}else if(value.startsWith("_") && value.contains(":")){
					String resultBinding = StringPool.BLANK;
					String[] valueSplit = value.split(":");
					for (String string : valueSplit) {
						if(string.equals("_subjectName")){
							resultBinding += ", " +  _subjectName;
						}else if(string.equals("_subjectId")){
							resultBinding += ", " +  _subjectId;
						}else if(string.equals("_address")){
							resultBinding += ", " +  _address;
						}else if(string.equals("_wardCode")){
							resultBinding += ", " +  _wardCode;
						}else if(string.equals("_wardName")){
							resultBinding += ", " +  _wardName;
						}else if(string.equals("_districtCode")){
							resultBinding += ", " +  _districtCode;
						}else if(string.equals("_districtName")){
							resultBinding += ", " +  _districtName;
						}else if(string.equals("_cityCode")){
							resultBinding += ", " +  _cityCode;
						}else if(string.equals("_cityName")){
							resultBinding += ", " +  _cityName;
						}else if(string.equals("_contactName")){
							resultBinding += ", " +  _contactName;
						}else if(string.equals("_contactTelNo")){
							resultBinding += ", " +  _contactTelNo;
						}else if(string.equals("_contactEmail")){
							resultBinding += ", " +  _contactEmail;
						}else if(string.equals("_ngayNopDon")){
							resultBinding += ", " + ngayNopDon();
						}else if(string.equals("_donViThucHien")){
							if(dossierId > 0){
								try {
									Dossier dossier = DossierLocalServiceUtil.fetchDossier(dossierId);
									resultBinding += ", " + dossier.getGovAgencyName();
								} catch (SystemException e) {
									// TODO Auto-generated catch block
									_log.error(e);
								}
							}
						}
					}
					
					jsonMap.put(entry.getKey(), resultBinding.replaceFirst(", ", StringPool.BLANK));

				}else if(value.startsWith("#") && value.contains("@")){
					String newString = value.substring(1);
					String[] stringSplit = newString.split("@");
					String variable = stringSplit[0];
					String paper = stringSplit[1];
					try {
						DossierFile dossierFile = DossierFileLocalServiceUtil.fetchByTemplateFileNoDossierId_First(dossierId, paper);
						if(Validator.isNotNull(dossierFile) && Validator.isNotNull(dossierFile.getFormData())){
							JSONObject jsonOtherData = JSONFactoryUtil.createJSONObject(dossierFile.getFormData());
							Map<String, Object> jsonOtherMap = jsonToMap(jsonOtherData);
							String myCHK = StringPool.BLANK;
								try {
									if(variable.contains(":")){
										String[] variableMuti = variable.split(":");
										for (String string : variableMuti) {
											myCHK += ", " + jsonOtherMap.get(string).toString();
										}
										myCHK = myCHK.replaceFirst(", ", "");
									}else{
										myCHK = jsonOtherMap.get(variable.replaceAll("_ok", "").replaceAll("_fail", "")).toString();
									}
								} catch (Exception e) {
									// TODO: handle exception
								}
								
							
							if(myCHK.startsWith("[{")){
								JSONArray orignJsonArray = (JSONArray)jsonOtherMap.get(variable.replaceAll("_ok", "").replaceAll("_fail", ""));
								if(!variable.contains("_ok") && !variable.contains("_fail")){
									jsonMap.put(entry.getKey(), orignJsonArray);
								}else{
									JSONArray orignJsonArrayOk = JSONFactoryUtil.createJSONArray();
									JSONArray orignJsonArrayFail = JSONFactoryUtil.createJSONArray();
									for (int i = 0; i < orignJsonArray.length(); i++) {
										JSONObject childObj = orignJsonArray.getJSONObject(i);
										String ketQuaTD = StringPool.BLANK;
										try {
											ketQuaTD = childObj.getString("ketQua");
										} catch (Exception e) {
											// TODO: handle exception
										}
										
										childObj.put("stt", i+1);
										if(ketQuaTD.equalsIgnoreCase("0")){
											orignJsonArrayFail.put(childObj);
										}else{
											orignJsonArrayOk.put(childObj);
										}
									}
									if(variable.contains("_ok")){
										jsonMap.put(entry.getKey(), orignJsonArrayOk);
									}else if(variable.contains("_fail")){
										jsonMap.put(entry.getKey(), orignJsonArrayFail);
									}
								}
								
							}else if(myCHK.startsWith("#")){
								jsonMap.put(entry.getKey(), "");
							}else{
								jsonMap.put(entry.getKey(), myCHK.toString());
							}
						}
					} catch (SystemException e) {
						// TODO Auto-generated catch block
						_log.error(e);
					}
				}
			}
			
			jsonSampleData = JSONFactoryUtil.createJSONObject();
			for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
				Object value = null;
	            if(entry.getValue().getClass().getName().contains("JSONArray")){
	            	jsonSampleData.put(entry.getKey(), (JSONArray)entry.getValue());
	            }else if(entry.getValue().getClass().getName().contains("JSONObject")){
	            	jsonSampleData.put(entry.getKey(), (JSONObject)entry.getValue());
	            }else{
	            	jsonSampleData.put(entry.getKey(), entry.getValue() + "");
	            }
				
			}
			
			
			result = jsonSampleData.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			_log.error(e);
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
		
			return  "Ng\u00E0y" + " " + sDay + " " +
					"th\u00E1ng" + " " + sMonth + " "+ 
					"n\u0103m" + " " + year;
		} catch (Exception e) {
			_log.error(e);
			
		}
		return "";
	}
	public static Map<String, Object> jsonToMap(JSONObject json) {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(Validator.isNotNull(json)) {
            try {
				retMap = toMap(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				_log.error(e);
			}
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = null;
            if(Validator.isNotNull(object.getJSONArray(key))) {
                value = (JSONArray) object.getJSONArray(key);
                map.put(key, value);
            }

            else if(Validator.isNotNull(object.getJSONObject(key))) {
                value = (JSONObject) object.getJSONObject(key);
                map.put(key, value);
            }
            
            else  {
                value = object.getString(key);
                map.put(key, value);
            }
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.getJSONObject(i);

            if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
    
    private static Log _log =
    		LogFactoryUtil.getLog(AutoFillFormData.class.getName());
}
