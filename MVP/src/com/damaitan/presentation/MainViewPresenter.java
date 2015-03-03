/**
 * 
 */
package com.damaitan.presentation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.PresentationException;
import com.damaitan.exception.ServiceException;
import com.damaitan.service.ModelManager;

/**
 * @author admin
 *
 */
public class MainViewPresenter {
	private  boolean isInitialized = false;
	public final static String Key_Title = "title";
	public final static String Key_Index = "index";
	public final static String Key_Id = "id";
	
	
	public MainViewPresenter(){
	}
	
	public void initialization(String json) throws PresentationException{
		try {
			if(!isInitialized){
				ModelManager.getInstance().construct(json);
				isInitialized = true;
			}
		} catch (ServiceException e) {
			throw new PresentationException("MainViewPresenter:initialization() - wrong!",e);
		}
		
	}

	public boolean getData(List<Map<String, Object>> data){
		if(data== null){
			return false;
		}
		data.clear();
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put(Key_Title, getFolder0SimpleInfo());
		temp.put(Key_Index, Integer.valueOf(0));
		data.add(temp);
		TaskFolder folder;
		for (int index = 1; index < ModelManager.getInstance().getFolders()
				.size(); index++) {
			folder = ModelManager.getInstance().getFolders().get(index);
			temp = new HashMap<String, Object>();
			temp.put(Key_Title, folder.getSimpleInfo());
			temp.put(Key_Index, Integer.valueOf(index));
			data.add(temp);
		}
		return true;
	}
	
	public String initJsonString(){
		return ModelManager.getInstance().initJsonString();
	}
	
	public boolean cleanFinished(){
		for(int i = 1 ; i < ModelManager.getInstance().getFolders().size();i++){
			ModelManager.getInstance().getTaskFolder(i).getFinishedTasks().clear();
		}
		return true;
	}
	
	public String getFolder0SimpleInfo(){
		int completedTasks = 0;
		int allTasks = 0;
		for(int i = 1 ; i < ModelManager.getInstance().getFolders().size();i++){
			allTasks = allTasks + ModelManager.getInstance().getTaskFolder(i).getAllTaskNumber();
			completedTasks = completedTasks + ModelManager.getInstance().getTaskFolder(i).getCompletedTaskNumber();
		}
		return ModelManager.getInstance().getTaskFolder(0).getName() + " - " + completedTasks + "/" + allTasks;
	}

}
