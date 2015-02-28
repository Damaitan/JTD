package com.damaitan.presentation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.damaitan.datamodel.CommonString;
import com.damaitan.datamodel.Task;
import com.damaitan.datamodel.TaskFolder;

public class TaskListSorter {
	public final static class TaskListInfo {
    	public String key = "";
    	public int start_position = -1;
    	public List<Task> tasks = null;
    	public TaskListInfo(String key){
    		this.key = key;
    		tasks = new ArrayList<Task>();
    	}
    	public boolean add(Task task){
    		for(int i = 0; i < tasks.size();i++){
    			if(tasks.get(i).getId() == task.getId()){
    				return false;
    			}
    		}
    		return tasks.add(task);
    	}
    	public boolean remove(Task task){
    		for(int i = 0; i < tasks.size();i++){
    			if(tasks.get(i).getId() == task.getId()){
    				tasks.remove(i);
    				break;
    			}
    		}
    		if(tasks.size() == 0){
    			start_position = -1;
    		}
    		return true;
    	}
    }
	
	public final static String KEY_FINISH = "finish";
	public final static String[] keys = new String[]{"urgent", "expired", "day", "week", "month", "other",KEY_FINISH};
	private HashMap<String, TaskListInfo> m_data;
	public static int ITEM_TYPE_CLASS = 0; 
	public static int ITEM_TYPE_TASK = 1;
	
	public TaskListSorter(){
		m_data = new HashMap<String, TaskListInfo>();
		for(String item : keys){
			m_data.put(item, new TaskListInfo(item));
		}
	}
	
	public String judge(Task task){
		if(task.status == Task.Status.finished) return KEY_FINISH;
		
		if(task.urgent) return keys[0];
		
		int index = judgeDate(task);
		if(index == 0) return  keys[1];
		if(index == 1) return  keys[2];
		
		if (task.tags != null && !task.tags.trim().equalsIgnoreCase("")) {
			if (task.tags.contains(CommonString.InitTag[0])) {
					return keys[2];
			} else if (task.tags.contains(CommonString.InitTag[1])) {
					return keys[3];
			}else if (task.tags.contains(CommonString.InitTag[2])) {
				if(index == 2){
					return keys[3];
				}else{
					return keys[4];
				}
			}
		}
		return keys[5];
	}
	
	public final static SimpleDateFormat dateFormat(){
		return new SimpleDateFormat("yyyy" + Task.DATESPLITTER + "MM" + Task.DATESPLITTER + "dd",  Locale.CHINA);
	}
	
	public final static SimpleDateFormat monthDayFormat(){
		return new SimpleDateFormat("MM" + Task.DATESPLITTER + "dd",  Locale.CHINA);
	}
	
	private int judgeDate(Task task){
		if(task.expired != null && !task.expired.isEmpty()){
			SimpleDateFormat df = dateFormat();
			long diff = 0;
			try {
				Date expired = df.parse(task.expired);
				Date today = new Date();
				diff = expired.getTime() - today.getTime();
				diff = diff/(1000*60*60*24);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(diff < 0){
				return 0;
			} 
			if(diff < 2){
				return 1;
			}
			if(diff < 8 ){
				return 2;
			}else if(diff < 31){
				return 3;
			}else{
				return 4; 
			}
		}
		return -1;
	}
	
	public void classifyData(TaskFolder folder){
		
		for(Task task : folder.getTasks()){
			m_data.get(judge(task)).add(task);
    	}
		for(Task task : folder.getFinishedTasks()){
			m_data.get(KEY_FINISH).add(task);
		}
		startPostion();
	}
	
	private void startPostion(){
		int length = 0;
		for(int i = 0; i < keys.length; i++){
			TaskListInfo info = (TaskListInfo)m_data.get(keys[i]);
			if(info.tasks == null || info.tasks.size() == 0){
				info.start_position = -1;
				continue;
			}
			info.start_position = length;
			length = length + info.tasks.size() + 1;
		}
	}
	
	public void clearData(){
		for(String item : keys){
			m_data.get(item).tasks.clear();
		}
	}
	
	public boolean remove(Task task){
		boolean result = m_data.get(judge(task)).remove(task);
		startPostion();
		return result;
	}
	
	public boolean add(Task task){
		boolean result = m_data.get(judge(task)).add(task);
		startPostion();
		return result;
	}
	
	public Task getTask(int position){
		for(String key : keys){
			TaskListInfo info = (TaskListInfo)m_data.get(key);
			if(info.start_position < 0) continue;
			if(position > info.start_position && position < info.start_position + info.tasks.size() + 1){
				return info.tasks.get(position - info.start_position - 1);
			}
		}
		return null;
	}
	
	public int getItemType(int position){
		for(String key : keys){
			TaskListInfo info = (TaskListInfo)m_data.get(key);
			if(position == info.start_position) return ITEM_TYPE_CLASS;
		}
		return ITEM_TYPE_TASK;
	}
	
	public int getCount() {
		int count = 0;
		for(String key : keys){
			TaskListInfo info = (TaskListInfo)m_data.get(key);
			if(info.start_position < 0) continue;
			count = count + info.tasks.size() + 1;
		}
		return count;
	}
	
	public int getClassIndex(int position){
		for(int i = 0; i < keys.length; i++){
			TaskListInfo info = (TaskListInfo)m_data.get(keys[i]);
			if(position == info.start_position){
				return i;
			}
		}
		return -1;
	}
	
	public TaskListInfo get(String key){
		return m_data.get(key);
	}
	
	
}
