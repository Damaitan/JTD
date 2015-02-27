package com.damaitan.presentation;

import com.damaitan.datamodel.Task;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.presentation.OnTaskResult.ITaskListener;
import com.damaitan.presentation.OnTaskResult.ITaskListener.Type;

public class TaskListenerTest implements ITaskListener{
	
	public ITaskListener.Type trigger = ITaskListener.Type.add;
	public TaskFolder testFolder;
	public Task testOld;
	public Task testTask;
	public int count = 0;
	public TaskFolderPresenter presenter;
	
	
	@Override
	public int act(Type actType, TaskFolder folder, Task oldTask, Task task) {
		trigger = actType;
		testFolder = folder;
		testOld = oldTask;
		testTask = task;
		count++;
		if(actType == Type.add){
			presenter.getSorter().add(task);
		}else if(actType == Type.update){
			boolean updated = oldTask.tags.equalsIgnoreCase(task.tags) && oldTask.urgent == task.urgent && oldTask.expired.equalsIgnoreCase(task.expired);
			if(!updated){
				presenter.getSorter().remove(oldTask);
				presenter.getSorter().add(task);
			}
		}
		else {
			presenter.getSorter().remove(oldTask);
			if(actType == Type.finish || actType == Type.activate ){
				presenter.getSorter().add(task);
			}
		}
		
		return 0;
	}

}
