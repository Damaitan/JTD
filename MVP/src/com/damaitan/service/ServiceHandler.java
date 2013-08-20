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

	public static void initialization(String json) throws ServiceException{
		ModelManager dm = ModelManager.getInstance();
		dm.construct(json);
	}
	
	public static String initJsonString(){
		return ModelManager.initJsonString();
	}
	
	
}
