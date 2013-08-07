/**
 * 
 */
package com.damaitan.service;

import com.damaitan.access.IDataAccess;
import com.damaitan.exception.ServiceException;

/**
 * @author admin
 *
 */
public class ServiceHandler {

	public static void initialization(IDataAccess handler) throws ServiceException{
		ModelManager dm = ModelManager.getInstance();
		dm.construct(handler);
	}
}
