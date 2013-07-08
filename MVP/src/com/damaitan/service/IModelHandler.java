/**
 * 
 */
package com.damaitan.service;

import com.damaitan.access.IDataAccess;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.ServiceException;

/**
 * @author admin
 *
 */
public interface IModelHandler {
	void construct(IDataAccess handler) throws ServiceException;
	TaskFolder createFolder(String name) throws ServiceException;
	TaskFolder modifyFolder(long id, String newName) throws ServiceException;
	TaskFolder deleteFolder(long id, boolean includeTask) throws ServiceException;
}
