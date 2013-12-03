/**
 * 
 */
package com.damaitan.presentation;

import java.util.List;
import java.util.Map;

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
	
	
	public MainViewPresenter(IViewMain mainView){
		this.mainView = mainView;

	}
	public void initialization(String json) throws PresentationException{
		
		try {
			ServiceHandler.initialization(json);
		} catch (ServiceException e) {
			throw new PresentationException("MainViewPresenter:initialization() - wrong!",e);
		}
		
	}

	public List<Map<String, Object>> getData() throws PresentationException{
		try {
			return mainView.listItems(TaskFolderHandler.getFolders());
		} catch (ServiceException e) {
			throw new PresentationException("MainViewPresenter:getData() can not get folders",e);
		}
	}
	
	public String initJsonString(){
		return ServiceHandler.initJsonString();
	}

}
