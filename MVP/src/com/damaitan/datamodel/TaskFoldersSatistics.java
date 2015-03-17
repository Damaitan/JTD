package com.damaitan.datamodel;

import java.util.ArrayList;
import java.util.List;

public class TaskFoldersSatistics {
	public List<Satistics> satistics = new ArrayList<Satistics>();
	
	public static class Satistics{
		public long taskFolderId;
		public String folderName;
		public int allTasksNumber = 0;
		public int finishTasksNumber = 0;
		public int TopPriorityTasksNumber = 0;
		public int highPriorityTasksNumber = 0;
		public int NormalPriorityTasksNumber = 0;
		public int lowPriorityTasksNumber = 0;
	}

}
