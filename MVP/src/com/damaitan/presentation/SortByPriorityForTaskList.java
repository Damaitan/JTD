package com.damaitan.presentation;

import java.util.Comparator;

import com.damaitan.datamodel.Task;

public class SortByPriorityForTaskList implements Comparator<Task>{

	@Override
	public int compare(Task arg0, Task arg1) {
		Integer pri0 = arg0.priority;
		Integer pri1 = arg1.priority;
		return pri1.compareTo(pri0);
	}

}
