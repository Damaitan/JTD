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

//import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockListActivity;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.datamodel.Task;
import com.damaitan.exception.ServiceException;
import com.damaitan.mobileUI.R;
import com.damaitan.service.ServiceHandler;
import com.damaitan.service.TaskFolderHandler;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class TaskActivity extends SherlockListActivity {
	 public static int THEME = R.style.Theme_Sherlock_Light;
	 private int _folderIndex;
	 private TaskFolder _folder;
	 
	 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Used to put dark icons on light action bar
        boolean isLight = TaskActivity.THEME == R.style.Theme_Sherlock_Light;

        menu.add("New")
            .setIcon(isLight ? R.drawable.ic_compose_inverse : R.drawable.ic_compose)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        menu.add("Search")
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add("Refresh")
            .setIcon(isLight ? R.drawable.ic_refresh_inverse : R.drawable.ic_refresh)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

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
		if(item.getTitle().toString().trim() == "New"){
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
