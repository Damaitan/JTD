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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import com.actionbarsherlock.app.SherlockListActivity;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.datamodel.Task;
import com.damaitan.exception.ServiceException;
import com.damaitan.service.ServiceHandler;
import com.damaitan.service.TaskFolderHandler;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

public class TaskActivity extends SherlockListActivity {

	 private int _folderIndex;
	 private TaskFolder _folder;
	 private static int MENU_ID_NEW = 0;
	 private static int MENU_ID_DELETE = MENU_ID_NEW + 1;
	 private static int MENU_ID_TAG = MENU_ID_NEW + 2;
	 private static int MENU_ID_FOLDER = MENU_ID_NEW + 3;
	 
	 
	 
	 
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Used to put dark icons on light action bar
    	
        menu.add(0,MENU_ID_NEW,0,this.getString(R.string.menu_task_new))
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        menu.add(0,MENU_ID_DELETE,1,this.getString(R.string.menu_task_delete))
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add(0,MENU_ID_TAG,2,this.getString(R.string.menu_task_tag))
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
        //subMenuItem.setIcon(R.drawable.ic_title_share_default);
        subMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        _folderIndex = this.getIntent().getIntExtra(Name.Index, 0);
        try {
			this.setTitle(TaskFolderHandler.getFolderByIndex(_folderIndex).getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        setListAdapter(new SimpleAdapter(this, getData(),
				android.R.layout.simple_list_item_1,
				new String[] { Name.Title }, new int[] { android.R.id.text1 }));
 
    }
    
    private List<Map<String, Object>> getData(){
    	List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();
		try {
			_folder = TaskFolderHandler.getFolderByIndex(_folderIndex);
			for(Task task : _folder.getTasks()){
	    		Map<String, Object> item = new HashMap<String, Object>();
	    		item.put(Name.Title, task.getName());
	    		//item.put(Name.Id, Long.valueOf(task.getId()));
	    		item.put(Name.Id, task.getId());
	    		myData.add(item);
	    	}
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return myData;
    }

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockListActivity#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*if (item.getItemId() == android.R.id.home || item.getItemId() == 0) {
            return false;
        }*/
        //THEME = item.getItemId();
		
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
