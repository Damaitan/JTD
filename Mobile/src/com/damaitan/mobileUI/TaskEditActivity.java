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

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.damaitan.presentation.TaskFolderPresenter;
import com.damaitan.service.GsonHelper;

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
	private ListPreference folderPreference;
	private Task mTask = null;
	private int folderindex = -1;
	boolean includeChilds = true;
	
	private static int MENU_ID_SAVE = 4;
	private static int MENU_ID_DELETE = MENU_ID_SAVE + 1;
	private static int MENU_ID_RELATION = MENU_ID_SAVE + 2;
	//private static int MENU_ID_NEW = MENU_ID_SAVE + 3;
	private static int MENU_ID_FINISH = MENU_ID_SAVE + 4;
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
    	if(this.mTask.getId() != Task.invalidId){
    		this.setTitle(this.mTask.getName());
    	}
    	menu.add(0,MENU_ID_SAVE,0,this.getString(R.string.menu_taskedit_save))
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		if (this.mTask.getId() != Task.invalidId) {
			menu.add(0, MENU_ID_FINISH, 0,
					this.getString(R.string.menu_taskedit_finish))
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	
			menu.add(0, MENU_ID_DELETE, 0,
					this.getString(R.string.menu_taskedit_delete))
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
			
			menu.add(0, MENU_ID_RELATION, 0,
					this.getString(R.string.menu_taskedit_relation))
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
		
        return super.onCreateOptionsMenu(menu);
    }
    
	@SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new TaskFolderPresenter(false);
        String json = this.getIntent().getStringExtra(TaskFolderPresenter.KEY_TASK);
        this.mTask = new GsonHelper().fromJson(json, Task.class);
        this.folderindex = (int)this.mTask.taskFolderId;
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
    	folderPreference = (ListPreference)findPreference(TaskFolderPresenter.TASK_FOLDER_KEY);
    	
    	String parentTaskName = loadTaskParent(this.folderindex);
		
        if(this.mTask.getId() != Task.invalidId){
        	namePreference.setDefaultValue(mTask.getName());
        	namePreference.setSummary(mTask.getName());
        	urgentPreference.setChecked(mTask.urgent);
        	tagPreference.setDefaultValue(mTask.tags);
        	if(!this.mTask.tags.equalsIgnoreCase("")){
        		tagPreference.setSummary(mTask.tags);
        	}
        	priorityPreference.setDefaultValue(this.mTask.priority);
        	priorityPreference.setSummary(priorityPreference.getEntries()[this.mTask.priority]);
			notePreference.setDefaultValue(this.mTask.note);
			if(!this.mTask.note.equalsIgnoreCase("")){
				notePreference.setSummary(mTask.note);
        	}
        	parentPreference.setDefaultValue(mTask.parentTaskId);
        	if(!parentTaskName.equalsIgnoreCase("")){
        		parentPreference.setSummary(parentTaskName);
        	}
        	repeatPreference.setChecked(this.mTask.repeat);
        	expiredPreference.setDefaultValue(this.mTask.expired);
        	if(!this.mTask.expired.isEmpty()){
        		expiredPreference.setSummary(mTask.expired);
        	}
        	repeatPeroidPreference.setDefaultValue(this.mTask.repeat_proid);
        	if(this.mTask.repeat_proid != -1){
        		repeatPeroidPreference.setSummary(String.valueOf(mTask.repeat_proid));
        	}
        	
        	folderPreference.setDefaultValue(mTask.taskFolderId);
        	
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
        folderPreference.setOnPreferenceChangeListener(this);
    }
	
	private String loadTaskParent(int folderIndex){
		TaskFolder parentFolder = null;
		try {
			parentFolder = presenter.getFolderByIndex(folderIndex + 1);
		} catch (ServiceException e) {
			this.parentPreference.setEnabled(false);
		}
		int size = parentFolder.getTasks().size();
		String parentTaskName = "";
		if(parentFolder != null && size > 0){
			String entries[] = new String[size];
			String values[] = new String[size];
			for(int i = 0; i < size; i++){
				Task task = parentFolder.getTasks().get(i);
				if(task.parentTaskId == this.mTask.parentTaskId){
					parentTaskName = task.getName();
				}
				entries[i] = task.getName();
				values[i] = Long.toString(task.getId());
			}
			this.parentPreference.setEntries(entries);
			this.parentPreference.setEntryValues(values);
		}else{
			this.parentPreference.setEnabled(false);
		}
		return parentTaskName;
	}

	 public boolean onPreferenceChange(Preference preference, Object objValue) {
		 if(preference.getKey().equals(TaskFolderPresenter.NAME_KEY)){
			 preference.setSummary((String)objValue);
			 this.mTask.setName((String)objValue);
		 }else if(preference.getKey().equals(TaskFolderPresenter.URGENT_KEY)){
			 this.mTask.urgent = ((Boolean)objValue).booleanValue();
		 }else if(preference.getKey().equals(TaskFolderPresenter.TAG_KEY)){
			 preference.setSummary((String)objValue);
			 this.mTask.tags = ((String)objValue);
		 }else if(preference.getKey().equals(TaskFolderPresenter.PRIORITY_KEY)){
			 ListPreference lp = (ListPreference)preference;
			 int index = lp.findIndexOfValue((String)objValue);
			 lp.setSummary(lp.getEntries()[index]);
			 this.mTask.priority = (Integer.parseInt((String)objValue));//this code must be changed
		 }else if(preference.getKey().equals(TaskFolderPresenter.NOTE_KEY)){
			 preference.setSummary((String)objValue);
			 this.mTask.note = ((String)objValue);
		 }else if(preference.getKey().equals(TaskFolderPresenter.PARENET_KEY)){
			 ListPreference lp = (ListPreference)preference;
			 int index = lp.findIndexOfValue((String)objValue);
			 preference.setSummary(lp.getEntries()[index]);
			 this.mTask.parentTaskId = (Long.parseLong((String)objValue));
		 }else if(preference.getKey().equals(TaskFolderPresenter.REPEAT_PEROID_KEY)){
			 preference.setSummary((String)objValue);
			 this.mTask.repeat_proid = (Integer.parseInt((String)objValue));
		 }else if(preference.getKey().equals(TaskFolderPresenter.EXPIRE_KEY)){
			 preference.setSummary((String)objValue);
			 this.mTask.expired = ((String)objValue);
		 }else if(preference.getKey().equals(TaskFolderPresenter.TASK_FOLDER_KEY)){
			 ListPreference lp = (ListPreference)preference;
			 int index = lp.findIndexOfValue((String)objValue);
			 preference.setSummary(lp.getEntries()[index]);
			 this.mTask.taskFolderId = (Long.parseLong((String)objValue));
			 if(this.mTask.taskFolderId != this.folderindex){
				 this.mTask.parentTaskId = Task.invalidId;
				 loadTaskParent((int)(this.mTask.taskFolderId));
			 }
		 }else if((preference.getKey().equals(TaskFolderPresenter.REPEAT_KEY))){
			 this.mTask.repeat = ((Boolean)objValue).booleanValue();
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
				if (this.mTask.getName().trim().equalsIgnoreCase("")
						|| this.mTask.getName().trim()
								.equalsIgnoreCase(Model.invalidStr)) {
					Toast.makeText(this, "Please input task name",
							Toast.LENGTH_SHORT).show();
				} else {
					if (this.mTask.getId() != Task.invalidId) {
						setResult(presenter.saveTask(folderindex,
								mTask, false), new Intent());
					} else {
						setResult(presenter.saveTask((int) (mTask.taskFolderId),
								mTask, true), new Intent());
					}
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
			includeChilds = true;
			new AlertDialog.Builder(this)
					.setTitle(this.getTitle())
					.setMessage(R.string.alert_taskedit_delete)
					.setPositiveButton(R.string.ok,null)
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									includeChilds = false;
								}
							}).show();

			presenter.delete(this.folderindex, mTask, includeChilds);
			setResult(RESULT_OK, new Intent());
			finish();
		}
		if (item.getItemId() == MENU_ID_FINISH){
			presenter.finishTask(this.folderindex,
					mTask);
			setResult(RESULT_OK, new Intent());
			finish();
		}
		return true;
	}
	
	
	 
	 
	
	
	
}
