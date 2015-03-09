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
import com.damaitan.service.ModelManager;
import com.damaitan.service.OnTaskResult;


public class TaskListSorter implements OnTaskResult.ITaskListener {
	public final static class TaskListInfo {
    	public String key = "";
    	public int start_position = -1;
    	public List<Task> tasks;
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
    		return true;
    	}
    	
    	public boolean update(Task task){
    		for(int i = 0; i < tasks.size();i++){
    			if(tasks.get(i).getId() == task.getId()){
    				return tasks.set(i, task) != null;
    			}
    		}
    		return true;
    	}
    	
    }
	
	public final static int KEY_FINISH = 7;
	public final static String keys[] = {"urgent", "expired", "day", "week", "month", "year", "other","finish"};
	private HashMap<Integer, TaskListInfo> m_data;
	public static int ITEM_TYPE_CLASS = 0; 
	public static int ITEM_TYPE_TASK = 1;
	private TaskFolder m_folder = null;
	
	public TaskListSorter(){
		OnTaskResult.getInstance().join(this);
		m_data = new HashMap<Integer, TaskListInfo>();
		for(int i = 0; i < keys.length; i++){
			m_data.put(i, new TaskListInfo(keys[i]));
		}
	}
	
	public int judge(Task task){
		if(task.status == Task.Status.finished) return KEY_FINISH;
		
		if(task.urgent) return 0;
		
		int index = judgeDate(task);
		if(index == 0) return  1;
		if(index == 1) return  2;
		
		if (task.tags != null && !task.tags.isEmpty()) {
			if (task.tags.contains(CommonString.InitTag[0])) {
					return 2;
			} else if (task.tags.contains(CommonString.InitTag[1])) {
					return 3;
			}else if (task.tags.contains(CommonString.InitTag[2])) {
				if(index == 2){
					return 3;
				}else{
					return 4;
				}
			}else if (task.tags.contains(CommonString.InitTag[3])) {
				return 5;
			}
		}
		return 6;
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
	
	public boolean init(TaskFolder folder){
		clearData();
		m_folder = folder; 
		if(m_folder.getId() == 0){
			for(int i = 1; i < ModelManager.getInstance().getFolders().size();i++){
				classifyData(ModelManager.getInstance().getFolders().get(i));
			}
		}else{
			classifyData(m_folder);
		}
		startPostion();
		return true;
	}
	
	private void classifyData(TaskFolder folder){
		
		for(Task task : folder.getTasks()){
			m_data.get(judge(task)).add(task);
    	}
		for(Task task : folder.getFinishedTasks()){
			m_data.get(KEY_FINISH).add(task);
		}
		
	}
	
	private void startPostion(){
		int length = 0;
		for(int i = 0; i < keys.length; i++){
			TaskListInfo info = (TaskListInfo)m_data.get(i);
			if(info.tasks.size() == 0){
				info.start_position = -1;
				continue;
			}
			info.start_position = length;
			length = length + info.tasks.size() + 1;
		}
	}
	
	public void clearData(){
		for(int i = 0; i < KEY_FINISH + 1; i++){
			m_data.get(i).tasks.clear();
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
	public boolean update(Task task){
		boolean result = m_data.get(judge(task)).update(task);
		startPostion();
		return result;
	}
	
	
	public Task getTask(int position){
		for(int i = 0; i < KEY_FINISH + 1; i++){
			TaskListInfo info = (TaskListInfo)m_data.get(i);
			if(info.start_position < 0) continue;
			if(position > info.start_position && position < info.start_position + info.tasks.size() + 1){
				return info.tasks.get(position - info.start_position - 1);
			}
		}
		return null;
	}
	
	public int getItemType(int position){
		for(int i = 0; i < KEY_FINISH + 1; i++){
			TaskListInfo info = (TaskListInfo)m_data.get(i);
			if(position == info.start_position) return ITEM_TYPE_CLASS;
		}
		return ITEM_TYPE_TASK;
	}
	
	public int getCount() {
		int count = 0;
		for(int i = 0; i < KEY_FINISH + 1; i++){
			TaskListInfo info = (TaskListInfo)m_data.get(i);
			if(info.start_position < 0) continue;
			count = count + info.tasks.size() + 1;
		}
		return count;
	}
	
	public int getClassIndex(int position){
		for(int i = 0; i < keys.length; i++){
			TaskListInfo info = (TaskListInfo)m_data.get(i);
			if(position == info.start_position){
				return i;
			}
		}
		return -1;
	}
	
	public TaskListInfo get(int key){
		return m_data.get(key);
	}

	@Override
	public int act(Type type, TaskFolder folder, Task oldTask, Task task) {
		if (m_folder == null)
			return 0;
		if ((m_folder.getId() != folder.getId()) && m_folder.getId() != 0)
			return 0;

		if (type == Type.add) {
			add(task);
			return 0;
		}
		if (type == Type.update) {
			if(judge(oldTask) != (judge(task))){
				remove(oldTask);
				add(task);
			}else{
				update(task);
			}
			return 0;
		}
		remove(oldTask);
		if (type == Type.finish || type == Type.activate) {
			add(task);
		}
		return 0;
	}

	@Override
	protected void finalize() throws Throwable {
		OnTaskResult.getInstance().leave(this);
		super.finalize();
	}
	
	public String log(){
		StringBuilder buffer = new StringBuilder();
		buffer.append("Sort count: " + getCount() + "\n");
		for(int i = 0; i < getCount(); i++){
			if(getItemType(i) == ITEM_TYPE_CLASS){
				buffer.append("type class pos : " + i + " " + keys[getClassIndex(i)] + "\n");
			}else{
				buffer.append("type task  pos : " + i + " " + getTask(i).getName() + "\n");
			}
		}
		return buffer.toString();
	}
	
	
	
	
}
