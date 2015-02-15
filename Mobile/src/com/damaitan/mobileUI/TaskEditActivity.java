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

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.damaitan.datamodel.Model;
import com.damaitan.datamodel.Task;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.ServiceException;
import com.damaitan.presentation.MainViewPresenter;
import com.damaitan.presentation.TaskFolderPresenter;
import com.damaitan.service.GsonHelper;
import com.damaitan.service.ModelManager;

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
	//private static int MENU_ID_NEW = MENU_ID_SAVE + 3;
	TaskFolderPresenter presenter;
	
	
	 
	
	public TaskEditActivity(){
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	/*if(this.task.getId() == Task.invalidId){
    		menu.add(0,MENU_ID_SAVE,0,this.getString(R.string.menu_taskedit_new))
    		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    	}else{
    		menu.add(0,MENU_ID_SAVE,0,this.getString(R.string.menu_taskedit_save))
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    	}*/
    	presenter = new TaskFolderPresenter(null);
    	if(this.task.getId() != Task.invalidId){
    		this.setTitle(this.task.getName());
    	}
    	menu.add(0,MENU_ID_SAVE,0,this.getString(R.string.menu_taskedit_save))
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
        
        String json = this.getIntent().getStringExtra(TaskFolderPresenter.KEY_TASK);
        this.folderindex = this.getIntent().getIntExtra(MainViewPresenter.Key_Index, -1);
        this.task = new GsonHelper().fromJson(json, Task.class);
        addPreferencesFromResource(R.xml.taskedit);
        namePreference = (EditTextPreference)findPreference(TaskFolderPresenter.NAME_KEY);
        urgentPreference = (CheckBoxPreference)findPreference(TaskFolderPresenter.URGENT_KEY);
        tagPreference = (TagPreference)findPreference(TaskFolderPresenter.TAG_KEY);
        priorityPreference = (ListPreference)findPreference(TaskFolderPresenter.PRIORITY_KEY);
        notePreference = (EditTextPreference)findPreference(TaskFolderPresenter.NOTE_KEY);
        parentPreference = (ListPreference)findPreference(TaskFolderPresenter.PARENET_KEY);
    	repeatPreference = (CheckBoxPreference)findPreference(TaskFolderPresenter.REPEAT_KEY);
    	expiredPreference = (DatePreference)findPreference(TaskFolderPresenter.EXPIRE_KEY);
    	repeatPeroidPreference = (EditTextPreference)findPreference(TaskFolderPresenter.REPEAT_PEROID_KEY);
    	TaskFolder parentFolder = null;
		try {
			parentFolder = presenter.getFolderByIndex(this.folderindex + 1);
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
        	urgentPreference.setChecked(this.task.urgent);
        	tagPreference.setDefaultValue(this.task.tags);
        	if(!this.task.tags.equalsIgnoreCase("")){
        		tagPreference.setSummary(task.tags);
        	}
        	priorityPreference.setDefaultValue(this.task.priority);
			notePreference.setDefaultValue(this.task.note);
        	parentPreference.setDefaultValue(Model.invalidId);
        	repeatPreference.setChecked(this.task.repeat);
        	expiredPreference.setDefaultValue(this.task.expired);
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
		 if(preference.getKey().equals(TaskFolderPresenter.NAME_KEY)){
			 preference.setSummary((String)objValue);
			 this.task.setName((String)objValue);
		 }else if(preference.getKey().equals(TaskFolderPresenter.URGENT_KEY)){
			 this.task.urgent = ((Boolean)objValue).booleanValue();
		 }else if(preference.getKey().equals(TaskFolderPresenter.TAG_KEY)){
			 preference.setSummary((String)objValue);
			 this.task.tags = ((String)objValue);
		 }else if(preference.getKey().equals(TaskFolderPresenter.PRIORITY_KEY)){
			 ListPreference lp = (ListPreference)preference;
			 int index = lp.findIndexOfValue((String)objValue);
			 lp.setSummary(lp.getEntries()[index]);
			 this.task.priority = (Integer.parseInt((String)objValue));//this code must be changed
		 }else if(preference.getKey().equals(TaskFolderPresenter.NOTE_KEY)){
			 preference.setSummary((String)objValue);
			 this.task.note = ((String)objValue);
		 }else if(preference.getKey().equals(TaskFolderPresenter.PARENET_KEY)){
			 ListPreference lp = (ListPreference)preference;
			 int index = lp.findIndexOfValue((String)objValue);
			 preference.setSummary(lp.getEntries()[index]);
			 this.task.parentTaskId = (Long.parseLong((String)objValue));
		 }else if(preference.getKey().equals(TaskFolderPresenter.REPEAT_PEROID_KEY)){
			 preference.setSummary((String)objValue);
			 this.task.repeat_proid = (Integer.parseInt((String)objValue));
		 }else if(preference.getKey().equals(TaskFolderPresenter.EXPIRE_KEY)){
			 preference.setSummary((String)objValue);
			 this.task.expired = ((String)objValue);
		 }
		 
		 return true;
	 }

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockPreferenceActivity#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == MENU_ID_SAVE) {
			Log.e("TaskEditActivity", "onOptionsItemSelected " + MENU_ID_SAVE);
			try {
				if (this.task.getName().trim().equalsIgnoreCase("")
						|| this.task.getName().trim()
								.equalsIgnoreCase(Model.invalidStr)) {
					Toast.makeText(this, "Please input task name",
							Toast.LENGTH_SHORT).show();
				} else {
					presenter.saveTask(folderindex, task, true);
					this.setTitle(this.task.getName());
					JsonHelper.saveJsonStringToFile(this, JsonHelper.JTDFile,
							ModelManager.getInstance().JsonString());
					Intent intent = new Intent();
					intent.putExtra("STATE", true);
					intent.putExtra("TASKID", task.getId());
					setResult(RESULT_OK, new Intent());
					finish();
				}
			} catch (ServiceException e) {
				Log.e("Error",
						"TaskEditActivity onOptionsItemSelected ServiceException",
						e);
			} catch (Exception e) {
				Log.e("Error", "TaskEditActivity onOptionsItemSelected", e);
			}

		}
		if (item.getItemId() == MENU_ID_DELETE) {
			Log.e("TaskEditActivity", "onOptionsItemSelected" + MENU_ID_DELETE);
			if (this.task.getId() != Task.invalidId) {
				Intent intent = new Intent();
				intent.putExtra("STATE", true);
				intent.putExtra("TASKID", task.getId());
				setResult(RESULT_OK, new Intent());
			}
			finish();
		}
		return true;
	}
	
	
	 
	 
	
	
	
}
