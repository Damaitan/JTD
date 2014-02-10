/**
 * 
 */
package com.damaitan.service;

import com.damaitan.exception.ServiceException;


/**
 * @author admin
 *
 */
public class ServiceHandler {
	
	static boolean isInitialized = false;

	public static void initialization(String json) throws ServiceException{
		ModelManager dm = ModelManager.getInstance();
		if(!isInitialized){
			dm.construct(json);
			isInitialized = true;
		}
	}
	
	//	It's used when this software is installed at the first time
	public static String initJsonString(){
		return ModelManager.getInstance().initJsonString();
	}
	
	public static String toJson(Object obj){
		return ModelManager.getInstance().getGson().toJson(obj);
	}
	
	
	
	public static <T> T fromJson(String json,Class<T> classOfT){
		return ModelManager.getInstance().getGson().fromJson(json, classOfT);
	}
	
	public static String modelString(){
		return ModelManager.getInstance().jsonString();
	}
	
}
