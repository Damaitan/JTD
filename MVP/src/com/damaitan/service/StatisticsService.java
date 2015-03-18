package com.damaitan.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.damaitan.datamodel.Task;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.datamodel.TaskFoldersSatistics;

public class StatisticsService implements OnTaskResult.ITaskListener{
	
	private static StatisticsService uniqueInstance = null;
	private StatisticsService(){
		OnTaskResult.getInstance().join(this);
	}
	
	public static StatisticsService getInstance() {
		 if (uniqueInstance == null) {
			 uniqueInstance = new StatisticsService();
		 }
		 return uniqueInstance;
	}
	
	
	private TaskFoldersSatistics mTaskFoldersSatistics = null;
	
	public void init(ArrayList<TaskFolder> folders){
		if(mTaskFoldersSatistics == null){
			mTaskFoldersSatistics = new TaskFoldersSatistics();
		}else{
			mTaskFoldersSatistics.satistics.clear();
		}
		TaskFoldersSatistics.Satistics all = new TaskFoldersSatistics.Satistics();
		all.taskFolderId = 0;
		all.folderName = folders.get(0).getName();
		mTaskFoldersSatistics.satistics.add(all);
		for(int i = 1; i < folders.size(); i++){
			TaskFolder folder = folders.get(i);
			TaskFoldersSatistics.Satistics satistics = new TaskFoldersSatistics.Satistics();
			satistics.taskFolderId = folder.getId();
			satistics.folderName = folder.getName();
			satistics.allTasksNumber = folder.getAllTaskNumber();
			satistics.finishTasksNumber = folder.getCompletedTaskNumber();
			for(Task task : folder.getFinishedTasks()){
				if(task.priority == 3) satistics.TopPriorityTasksNumber++;
				if(task.priority == 2) satistics.highPriorityTasksNumber++;
				if(task.priority == 1) satistics.NormalPriorityTasksNumber++;
				if(task.priority == 0) satistics.lowPriorityTasksNumber++;
			}
			mTaskFoldersSatistics.satistics.add(satistics);
			all.allTasksNumber = all.allTasksNumber + satistics.allTasksNumber;
			all.finishTasksNumber = all.finishTasksNumber + satistics.finishTasksNumber;
			all.TopPriorityTasksNumber = all.TopPriorityTasksNumber + satistics.TopPriorityTasksNumber;
			all.highPriorityTasksNumber = all.highPriorityTasksNumber + satistics.highPriorityTasksNumber;
			all.NormalPriorityTasksNumber = all.NormalPriorityTasksNumber + satistics.NormalPriorityTasksNumber;
			all.lowPriorityTasksNumber = all.lowPriorityTasksNumber + satistics.lowPriorityTasksNumber;
		}
	}
	
	
	public void init(String json){
		mTaskFoldersSatistics = new GsonHelper().fromJson(json, TaskFoldersSatistics.class);
	}

	@Override
	public int act(Type type, TaskFolder folder, Task oldTask, Task task) {
		if (type == Type.add) {
			mTaskFoldersSatistics.satistics.get(0).allTasksNumber++;
			mTaskFoldersSatistics.satistics.get((int)(folder.getId())).allTasksNumber++;
		}
		if (type == Type.finish){
			mTaskFoldersSatistics.satistics.get(0).finishTasksNumber++;
			mTaskFoldersSatistics.satistics.get((int)(folder.getId())).finishTasksNumber++;
			if(oldTask.priority == 3){
				mTaskFoldersSatistics.satistics.get(0).TopPriorityTasksNumber++;
				mTaskFoldersSatistics.satistics.get((int)(folder.getId())).TopPriorityTasksNumber++;
			}
			if(oldTask.priority == 2) {
				mTaskFoldersSatistics.satistics.get(0).highPriorityTasksNumber++;
				mTaskFoldersSatistics.satistics.get((int)(folder.getId())).highPriorityTasksNumber++;
			}
			if(oldTask.priority == 1){
				mTaskFoldersSatistics.satistics.get(0).NormalPriorityTasksNumber++;
				mTaskFoldersSatistics.satistics.get((int)(folder.getId())).NormalPriorityTasksNumber++;
			}
			if(oldTask.priority == 0) {
				mTaskFoldersSatistics.satistics.get(0).lowPriorityTasksNumber++;
				mTaskFoldersSatistics.satistics.get((int)(folder.getId())).lowPriorityTasksNumber++;
			}
		}
		if(type == Type.activate) {
			mTaskFoldersSatistics.satistics.get(0).finishTasksNumber--;
			mTaskFoldersSatistics.satistics.get((int)(folder.getId())).finishTasksNumber--;
			if(oldTask.priority == 3){
				mTaskFoldersSatistics.satistics.get(0).TopPriorityTasksNumber--;
				mTaskFoldersSatistics.satistics.get((int)(folder.getId())).TopPriorityTasksNumber--;
			}
			if(oldTask.priority == 2) {
				mTaskFoldersSatistics.satistics.get(0).highPriorityTasksNumber--;
				mTaskFoldersSatistics.satistics.get((int)(folder.getId())).highPriorityTasksNumber--;
			}
			if(oldTask.priority == 1){
				mTaskFoldersSatistics.satistics.get(0).NormalPriorityTasksNumber--;
				mTaskFoldersSatistics.satistics.get((int)(folder.getId())).NormalPriorityTasksNumber--;
			}
			if(oldTask.priority == 0) {
				mTaskFoldersSatistics.satistics.get(0).lowPriorityTasksNumber--;
				mTaskFoldersSatistics.satistics.get((int)(folder.getId())).lowPriorityTasksNumber--;
			}
		}
		if (type == Type.delete) {
			if (oldTask.status != Task.Status.finished) {
				mTaskFoldersSatistics.satistics.get(0).allTasksNumber--;
				mTaskFoldersSatistics.satistics.get((int) (folder.getId())).allTasksNumber--;
			}
		}
		return 0;
	}
	
	@Override
	protected void finalize() throws Throwable {
		OnTaskResult.getInstance().leave(this);
		super.finalize();
	}
	
	public String JsonString(){
		return new GsonHelper().jsonString(mTaskFoldersSatistics);
	}
	
	
	public String getCSV(){
		StringBuffer buffer = new StringBuffer();
		SimpleDateFormat df = new SimpleDateFormat("yyyy" + Task.DATESPLITTER + "MM" + Task.DATESPLITTER + "dd",  Locale.CHINA);
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		String splitter = ",";
		buffer.append(df.format(calendar.getTime()));
		buffer.append(splitter + calendar.get(Calendar.WEEK_OF_YEAR));
		TaskFoldersSatistics.Satistics  all = mTaskFoldersSatistics.satistics.get(0);
		buffer.append(splitter + all.allTasksNumber);
		buffer.append(splitter + all.finishTasksNumber);
		buffer.append(splitter + all.TopPriorityTasksNumber);
		buffer.append(splitter + all.highPriorityTasksNumber);
		buffer.append(splitter + all.NormalPriorityTasksNumber);
		buffer.append(splitter + all.lowPriorityTasksNumber);
		for(int i = 1; i < mTaskFoldersSatistics.satistics.size(); i++){
			buffer.append(splitter + mTaskFoldersSatistics.satistics.get(i).allTasksNumber);
			buffer.append(splitter + mTaskFoldersSatistics.satistics.get(i).finishTasksNumber);
		}
		return buffer.toString();
		
	}

}
