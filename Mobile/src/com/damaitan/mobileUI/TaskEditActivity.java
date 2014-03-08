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

import java.io.FileOutputStream;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.damaitan.datamodel.Task;
import com.damaitan.exception.ServiceException;
import com.damaitan.service.ServiceHandler;
import com.damaitan.service.TaskFolderHandler;

public class TaskEditActivity extends SherlockPreferenceActivity  implements Preference.OnPreferenceChangeListener{
	private EditTextPreference namePreference;
	private CheckBoxPreference urgentPreference;
	private EditTextPreference tagPreference;
	private ListPreference priorityPreference;
	private EditTextPreference notePreference;
	
	private Task task = null;
	private int folderindex = -1;
	
	private static int MENU_ID_SAVE = 4;
	private static int MENU_ID_DELETE = MENU_ID_SAVE + 1;
	private static int MENU_ID_RELATION = MENU_ID_SAVE + 2;
	private static int MENU_ID_NEW = MENU_ID_SAVE + 3;
	 
	
	public TaskEditActivity(){
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Used to put dark icons on light action bar
    	
		menu.add(0,MENU_ID_SAVE,0,this.getString(R.string.menu_taskedit_save))
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		menu.add(0,MENU_ID_NEW,0,this.getString(R.string.menu_taskedit_new))
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		menu.add(0,MENU_ID_DELETE,0,this.getString(R.string.menu_taskedit_delete))
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        menu.add(0,MENU_ID_RELATION,0,this.getString(R.string.menu_taskedit_relation))
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        String json = this.getIntent().getStringExtra(Name.TASK_KEY);
        this.folderindex = this.getIntent().getIntExtra(Name.Index, -1);
        this.task = ServiceHandler.fromJson(json, Task.class);
        
        addPreferencesFromResource(R.xml.taskedit);
        namePreference = (EditTextPreference)findPreference("taskedit_name");
        urgentPreference = (CheckBoxPreference)findPreference("taskedit_urgent");
        tagPreference = (EditTextPreference)findPreference("taskedit_tag");
        priorityPreference = (ListPreference)findPreference("taskedit_priority");
        notePreference = (EditTextPreference)findPreference("taskedit_note");
        
        namePreference.setOnPreferenceChangeListener(this);
        urgentPreference.setOnPreferenceChangeListener(this);
        tagPreference.setOnPreferenceChangeListener(this);
        priorityPreference.setOnPreferenceChangeListener(this);
        notePreference.setOnPreferenceChangeListener(this);
    }

	 public boolean onPreferenceChange(Preference preference, Object objValue) {
		 Toast.makeText(this, "Preference Key:" + preference.getKey() + " value:" + (String)objValue, Toast.LENGTH_SHORT).show();
		 String value = (String)objValue;
		 if(preference.getKey().equals("taskedit_name")){
			 preference.setTitle(this.getString(R.string.title_taskedit_name) + ":" + (String)objValue);
			 this.task.setName((String)objValue);
		 }else if(preference.getKey().equals("taskedit_urgent"))
		 {
			 this.task.setUrgent(Boolean.getBoolean(value));
		 }else if(preference.getKey().equals("taskedit_tag"))
		 {
			 preference.setSummary((String)objValue);
			 this.task.setTags(value);
		 }else if(preference.getKey().equals("taskedit_priority"))
		 {
			 preference.setTitle(this.getString(R.string.title_taskedit_priority) + ":" + (String)objValue);
			 this.task.setPriority(1);//this code must be changed
		 }
		 else if(preference.getKey().equals("taskedit_note"))
		 {
			 preference.setSummary((String)objValue);
			 this.task.setNote(value);
		 }
		 return true;
	 }

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockPreferenceActivity#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == MENU_ID_SAVE){
			try {
				TaskFolderHandler.saveTask(folderindex, task, true);
				saveJsonStringToFile(ServiceHandler.modelString());
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return true;
	}
	
	private boolean saveJsonStringToFile(String json) throws Exception{
		try {
			FileOutputStream fout = openFileOutput("JTD.json", MODE_PRIVATE);
			byte[] bytes = json.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return true;
	}
	
	 
	 
	
	
	
}
