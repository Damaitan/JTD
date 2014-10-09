/*
* Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.damaitan.mobileUI;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.datamodel.Task;
import com.damaitan.exception.ServiceException;
import com.damaitan.service.ServiceHandler;
import com.damaitan.service.TaskFolderHandler;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

public class TaskActivity extends SherlockActivity {

	 private int _folderIndex;
	 private TaskFolder _folder;
	 private static int MENU_ID_NEW = 0;
	 private static int MENU_ID_DELETE = MENU_ID_NEW + 1;
	 //private static int MENU_ID_TAG = MENU_ID_NEW + 2;
	 private static int MENU_ID_FINISH = MENU_ID_NEW + 2;
	 private static int MENU_ID_FOLDER = MENU_ID_NEW + 3;
	 
	 private TaskFolderItemAdapter expiredTaskAdapter; 
	 private TaskFolderItemAdapter dayTaskAdapter;
	 private TaskFolderItemAdapter weekTaskAdapter;
	 private TaskFolderItemAdapter monthTaskAdapter;
	 private TaskFolderItemAdapter otherTaskAdapter;
	 private TaskFolderItemAdapter finishTaskAdapter;
	 private ListView expiredTaskView; 
	 private ListView dayTaskView;
	 private ListView weekTaskView;
	 private ListView monthTaskView;
	 private ListView otherTaskView;
	 private ListView finishTaskView;
	 private List<Task> listExpiredTask;
	 private List<Task> listDayTask;
	 private List<Task> listWeekTask;
	 private List<Task> listMonthTask;
	 private List<Task> listOtherTask;
	 private List<Task> listFinishTask;
	 
	 
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Used to put dark icons on light action bar
    	
        menu.add(0,MENU_ID_NEW,0,this.getString(R.string.menu_task_new))
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        menu.add(0,MENU_ID_DELETE,1,this.getString(R.string.menu_task_delete))
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        //menu.add(0,MENU_ID_TAG,2,this.getString(R.string.menu_task_tag))
        menu.add(0,MENU_ID_FINISH,2,this.getString(R.string.menu_task_finish))
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        
        SubMenu subMenu = menu.addSubMenu(0,MENU_ID_FOLDER,2,this.getString(R.string.menu_task_folder));
        try {
			ArrayList<TaskFolder> folders = TaskFolderHandler.getFolders();
			for(int i=0;i<folders.size();i++){
				TaskFolder folder = folders.get(i);
				subMenu.add(i,i+MENU_ID_FOLDER+1,i,folder.getName());
			}
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        MenuItem subMenuItem = subMenu.getItem();
        subMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        
	    

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_folder); 
        _folderIndex = this.getIntent().getIntExtra(Name.Index, 0);
        try {
			this.setTitle(TaskFolderHandler.getFolderByIndex(_folderIndex).getName());
			classifyData();
		} catch (Exception e) {
			Log.e("Error", "TaskActivity onCreate", e);
		}
        
        expiredTaskView = (ListView)findViewById(R.id.lst_task_folder_expired); 
	   	dayTaskView = (ListView)findViewById(R.id.lst_task_folder_day);
	   	weekTaskView = (ListView)findViewById(R.id.lst_task_folder_week);
	   	monthTaskView = (ListView)findViewById(R.id.lst_task_folder_month);
	   	otherTaskView = (ListView)findViewById(R.id.lst_task_folder_other);
	   	finishTaskView = (ListView)findViewById(R.id.lst_task_folder_finish);
	   	
	   	expiredTaskAdapter = new TaskFolderItemAdapter(this, listExpiredTask);
	   	expiredTaskView.setAdapter(expiredTaskAdapter);
	   	dayTaskAdapter = new TaskFolderItemAdapter(this, listDayTask);
	   	dayTaskView.setAdapter(dayTaskAdapter);
	   	weekTaskAdapter = new TaskFolderItemAdapter(this, listWeekTask);
	   	weekTaskView.setAdapter(weekTaskAdapter);
	   	monthTaskAdapter = new TaskFolderItemAdapter(this, listMonthTask);
	   	monthTaskView.setAdapter(monthTaskAdapter);
	   	otherTaskAdapter = new TaskFolderItemAdapter(this, listOtherTask);
	   	otherTaskView.setAdapter(otherTaskAdapter);
	   	finishTaskAdapter = new TaskFolderItemAdapter(this, listFinishTask,true);
	   	finishTaskView.setAdapter(finishTaskAdapter);
 
    }
    
    private void classifyData(){
    	listExpiredTask = new ArrayList<Task>();
    	listDayTask = new ArrayList<Task>();
    	listWeekTask = new ArrayList<Task>();
    	listMonthTask = new ArrayList<Task>();
    	listOtherTask = new ArrayList<Task>();
    	listFinishTask = new ArrayList<Task>();
    	
		try {
			_folder = TaskFolderHandler.getFolderByIndex(_folderIndex);
			for(Task task : _folder.getTasks()){
	    		listExpiredTask.add(task);
	    		listDayTask.add(task);
	    		listWeekTask.add(task);
	    		listMonthTask.add(task);
	    		listOtherTask.add(task);
	    		listFinishTask.add(task);
	    	}
		} catch (ServiceException e) {
			Log.e("Error", "TaskActivity getData()", e);
		}
    }

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockListActivity#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
        Toast.makeText(this, "Menu changed to \"" + item.getTitle() + "\"" + " ID:" + item.getItemId(), Toast.LENGTH_SHORT).show();
		if(item.getItemId() == MENU_ID_NEW){
			Intent intent = new Intent(this,TaskEditActivity.class);
			Task task = new Task();
			task.setTaskFolderId(_folder.getId());
			intent.putExtra(Name.TASK_KEY, ServiceHandler.toJson(task));
			intent.putExtra(Name.Index, _folderIndex);
	        startActivity(intent);
		}
		
        return true;
        
	}

   
}
