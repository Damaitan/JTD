/**
 * 
 */
package com.damaitan.presentation;

import java.util.List;
import java.util.Map;

import com.damaitan.access.IDataAccess;
import com.damaitan.exception.PresentationException;
import com.damaitan.exception.ServiceException;
import com.damaitan.service.ServiceHandler;
import com.damaitan.service.TaskFolderHandler;

/**
 * @author admin
 *
 */
public class MainViewPresenter {
	private IViewMain mainView;
	private TaskFolderHandler handler;
	
	public MainViewPresenter(IViewMain mainView){
		this.mainView = mainView;

	}
	public void initialization(IDataAccess access) throws PresentationException{
		
		handler = new TaskFolderHandler();
		try {
			ServiceHandler.initialization(access);
		} catch (ServiceException e) {
			throw new PresentationException("MainViewPresenter:initialization() - wrong!",e);
		}
		
	}

	public List<Map<String, Object>> getData() throws PresentationException{
		try {
			return mainView.listItems(handler.getCopied());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			throw new PresentationException("MainViewPresenter:getData() can not get folders",e);
		}
	}

}
