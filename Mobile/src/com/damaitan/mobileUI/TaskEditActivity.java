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
import android.util.Log;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.damaitan.datamodel.Model;
import com.damaitan.datamodel.Task;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.ServiceException;
import com.damaitan.service.ServiceHandler;
import com.damaitan.service.TaskFolderHandler;

public class TaskEditActivity extends SherlockPreferenceActivity  implements Preference.OnPreferenceChangeListener{
	private EditTextPreference namePreference;
	private CheckBoxPreference urgentPreference;
	private TagPreference tagPreference;
	private ListPreference priorityPreference;
	private EditTextPreference notePreference;
	private ListPreference parentPreference;
	private CheckBoxPreference repeatPreference;
	private DatePreference expiredPreference;
	private EditTextPreference repeatPeroidPreference;
	private Task task = null;
	private int folderindex = -1;
	
	private static int MENU_ID_SAVE = 4;
	private static int MENU_ID_DELETE = MENU_ID_SAVE + 1;
	private static int MENU_ID_RELATION = MENU_ID_SAVE + 2;
	private static int MENU_ID_NEW = MENU_ID_SAVE + 3;
	
	private static String NAME_KEY = "taskedit_name";
	private static String URGENT_KEY = "taskedit_urgent";
	private static String TAG_KEY = "taskedit_tag";
	private static String PRIORITY_KEY = "taskedit_priority";
	private static String NOTE_KEY = "taskedit_note";
	private static String PARENET_KEY = "taskedit_parent";
	private static String REPEAT_KEY = "taskedit_repeat";
	private static String EXPIRE_KEY = "taskedit_expire";
	private static String REPEAT_PEROID_KEY = "taskedit_repeat_peroid";
	 
	
	public TaskEditActivity(){
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	if(this.task.getId() == Task.invalidId){
    		menu.add(0,MENU_ID_NEW,0,this.getString(R.string.menu_taskedit_new))
    		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    	}else{
    		menu.add(0,MENU_ID_SAVE,0,this.getString(R.string.menu_taskedit_save))
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    	}
		
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
        namePreference = (EditTextPreference)findPreference(NAME_KEY);
        urgentPreference = (CheckBoxPreference)findPreference(URGENT_KEY);
        tagPreference = (TagPreference)findPreference(TAG_KEY);
        priorityPreference = (ListPreference)findPreference(PRIORITY_KEY);
        notePreference = (EditTextPreference)findPreference(NOTE_KEY);
        parentPreference = (ListPreference)findPreference(PARENET_KEY);
    	repeatPreference = (CheckBoxPreference)findPreference(REPEAT_KEY);
    	expiredPreference = (DatePreference)findPreference(EXPIRE_KEY);
    	repeatPeroidPreference = (EditTextPreference)findPreference(REPEAT_PEROID_KEY);
    	TaskFolder parentFolder = null;
		try {
			parentFolder = TaskFolderHandler.getFolderByIndex(this.folderindex + 1);
		} catch (ServiceException e) {
			this.parentPreference.setEnabled(false);
		}
		int size = parentFolder.getTasks().size();
		if(parentFolder != null && size > 0){
			String entries[] = new String[size];
			String values[] = new String[size];
			for(int i = 0; i < size; i++){
				Task task = parentFolder.getTasks().get(i);
				entries[i] = task.getName();
				values[i] = Long.toString(task.getId());
			}
			this.parentPreference.setEntries(entries);
			this.parentPreference.setEntryValues(values);
		}else{
			this.parentPreference.setEnabled(false);
		}
		
        if(this.task.getId() != Task.invalidId){
        	namePreference.setDefaultValue(this.task.getName());
        	urgentPreference.setChecked(this.task.isUrgent());
        	tagPreference.setDefaultValue(this.task.getTags());
        	if(!this.task.getTags().equalsIgnoreCase("")){
        		tagPreference.setSummary(task.getTags());
        	}
        	priorityPreference.setDefaultValue(this.task.getPriority());
        	notePreference.setDefaultValue(this.task.getNote());
        	parentPreference.setDefaultValue(Model.invalidId);
        	repeatPreference.setChecked(this.task.isRepeat());
        	expiredPreference.setDefaultValue(this.task.getExpired());
        	repeatPeroidPreference.setDefaultValue(this.task.getRepeat_proid());
        }
        
        //Ìí¼ÓÕìÌýÊÂ¼þ
        namePreference.setOnPreferenceChangeListener(this);
        urgentPreference.setOnPreferenceChangeListener(this);
        tagPreference.setOnPreferenceChangeListener(this);
        priorityPreference.setOnPreferenceChangeListener(this);
        notePreference.setOnPreferenceChangeListener(this);
        parentPreference.setOnPreferenceChangeListener(this);
        repeatPreference.setOnPreferenceChangeListener(this);
        expiredPreference.setOnPreferenceChangeListener(this);
        repeatPeroidPreference.setOnPreferenceChangeListener(this);
    }

	 public boolean onPreferenceChange(Preference preference, Object objValue) {
		 if(preference.getKey().equals(NAME_KEY)){
			 preference.setTitle(this.getString(R.string.title_taskedit_name) + ":" + (String)objValue);
			 this.task.setName((String)objValue);
		 }else if(preference.getKey().equals(URGENT_KEY))
		 {
			 this.task.setUrgent(((Boolean)objValue).booleanValue());
		 }else if(preference.getKey().equals(TAG_KEY))
		 {
			 preference.setSummary((String)objValue);
			 this.task.setTags((String)objValue);
		 }else if(preference.getKey().equals(PRIORITY_KEY))
		 {
			 ListPreference lp = (ListPreference)preference;
			 int index = lp.findIndexOfValue((String)objValue);
			 lp.setTitle(this.getString(R.string.title_taskedit_priority) + ":" + lp.getEntries()[index]);
			 this.task.setPriority(Integer.parseInt((String)objValue));//this code must be changed
		 }
		 else if(preference.getKey().equals(NOTE_KEY))
		 {
			 preference.setSummary((String)objValue);
			 this.task.setNote((String)objValue);
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
				Log.e("Error","TaskEditActivity onOptionsItemSelected ServiceException", e);
			} catch (Exception e) {
				Log.e("Error","TaskEditActivity onOptionsItemSelected", e);
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
