package org.opencps.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.opencps.backend.util.AutoFillFormData;

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

public class PaymentFeeUtil {
	
	public static int getTotalDossierPayment(Pattern patternName,
			Matcher matcherName, String pattern, long dossierId) throws JSONException {

		int net = 0;
		
		patternName = Pattern.compile("#(.*?)@(.*?) ");
        
   	 	matcherName = patternName.matcher(pattern);
	        
	   	List<String> listSTR = new ArrayList<String>();
		       
	   	while(matcherName.find()){
		        	
	   		listSTR.add(matcherName.group(0));
		     
	   	}
		        
	   	listSTR = new ArrayList<String>(new LinkedHashSet<String>(listSTR));
		        
	   	JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
		        
	   	for (String string : listSTR) {
		        	
	   		jsonObject.put(string.trim(), string.trim());
				
	   	}
		        
	   	String result = AutoFillFormData.dataBinding(jsonObject.toString(), null, null, dossierId);
		        
		jsonObject = JSONFactoryUtil.createJSONObject(result);
		        
		Map<String, Object> jsonMap = AutoFillFormData.jsonToMap(jsonObject);
		        
		for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
			String valReplace = StringPool.BLANK;
			
			if(Validator.isNumber(String.valueOf(entry.getValue()))){
				
				valReplace = String.valueOf(entry.getValue());
				
		    }else{
		    	
		    	valReplace = "'"+String.valueOf(entry.getValue())+"'";
		    	
		    }
		    pattern = pattern.replaceAll(entry.getKey(), valReplace);
		}
		        
		ScriptEngineManager manager = new ScriptEngineManager();
			    
		ScriptEngine engine = manager.getEngineByExtension("js");
				
		patternName = Pattern.compile("net=\\[(.*?)\\]");
		       
		matcherName = patternName.matcher(pattern);
			    
		if(matcherName.find()){
		        	
		   manager = new ScriptEngineManager();
		    	    
		   engine = manager.getEngineByExtension("js");
		    	   
		   String netScript = matcherName.group(1);
		    		
		   try {
						
		    	engine.eval(netScript);
						
				 net = GetterUtil.getInteger(engine.get("payment"));
						
		   } catch (ScriptException e) {
			   
				_log.error(e);
				
		   }
		    		    
		}
		return net;
	}
	
	private static Log _log = LogFactoryUtil
			.getLog(PaymentFeeUtil.class);

	
}
