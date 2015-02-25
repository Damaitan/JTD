package com.damaitan.presentation;

import com.damaitan.datamodel.Task;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.presentation.OnTaskResult.ITaskListener;

public class TaskListenerTest implements ITaskListener{
	
	public ITaskListener.Type trigger = ITaskListener.Type.add;
	public TaskFolder testFolder;
	public Task testOld;
	public Task testTask;
	@Override
	public int act(Type type, TaskFolder folder, Task oldTask, Task task) {
		trigger = type;
		testFolder = folder;
		testOld = oldTask;
		testTask = task;
		return 0;
	}

}
