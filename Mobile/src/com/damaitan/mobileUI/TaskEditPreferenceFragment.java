package com.damaitan.mobileUI;

import com.damaitan.datamodel.Task;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.ServiceException;
import com.damaitan.presentation.TaskFolderPresenter;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;

public class TaskEditPreferenceFragment extends android.preference.PreferenceFragment  implements Preference.OnPreferenceChangeListener{
	
	public Task _task = null;
	public int _folderindex = -1;
	public TaskFolderPresenter _presenter;
	
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
	
	

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
    	
    	String parentTaskName = loadTaskParent(this._folderindex);
        if(this._task.getId() != Task.invalidId){
        	namePreference.setDefaultValue(_task.getName());
        	namePreference.setSummary(_task.getName());
        	urgentPreference.setChecked(_task.urgent);
        	tagPreference.setDefaultValue(_task.tags.trim());
        	if(!this._task.tags.equalsIgnoreCase("")){
        		tagPreference.setSummary(_task.tags);
        	}
        	priorityPreference.setDefaultValue(this._task.priority);
        	priorityPreference.setSummary(priorityPreference.getEntries()[this._task.priority]);
			notePreference.setDefaultValue(this._task.note);
			if(!this._task.note.equalsIgnoreCase("")){
				notePreference.setSummary(_task.note);
        	}
        	if(!parentTaskName.isEmpty()){
        		parentPreference.setSummary(parentTaskName);
        	}
        	
        	repeatPreference.setChecked(_task.repeat);
        	expiredPreference.setDefaultValue(this._task.expired);
        	if(_task.repeat && !this._task.expired.isEmpty()){
        		expiredPreference.setSummary(_task.expired);
        	}
        	repeatPeroidPreference.setDefaultValue(this._task.repeat_proid);
        	if(_task.repeat){
        		repeatPeroidPreference.setSummary(String.valueOf(_task.repeat_proid));
        	}
        	
        	folderPreference.setDefaultValue(_task.taskFolderId);
        	
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
			parentFolder = _presenter.getFolderByIndex(folderIndex + 1);
		} catch (ServiceException e) {
			this.parentPreference.setEnabled(false);
		}
		int size = parentFolder.getTasks().size();
		Task parentTask = null;
		if(parentFolder != null && size > 0){
			String entries[] = new String[size];
			String values[] = new String[size];
			for(int i = 0; i < size; i++){
				Task task = parentFolder.getTasks().get(i);
				if(task.getId() == task.parentTaskId){
					parentTask = task;
				}
				entries[i] = task.getName();
				values[i] = Long.toString(task.getId());
			}
			this.parentPreference.setEntries(entries);
			this.parentPreference.setEntryValues(values);
			if(parentTask != null){
				this.parentPreference.setDefaultValue(Long.toString(parentTask.getId()));
			}
		}else{
			this.parentPreference.setEnabled(false);
		}
		return parentTask != null ? parentTask.getName(): "";
	}

	public boolean onPreferenceChange(Preference preference, Object objValue) {
		 if(preference.getKey().equals(TaskFolderPresenter.NAME_KEY)){
			 preference.setSummary((String)objValue);
			 this._task.setName((String)objValue);
		 }else if(preference.getKey().equals(TaskFolderPresenter.URGENT_KEY)){
			 this._task.urgent = ((Boolean)objValue).booleanValue();
		 }else if(preference.getKey().equals(TaskFolderPresenter.TAG_KEY)){
			 String value = (String)objValue;
			 if(value != null && !value.trim().isEmpty()){
				 preference.setSummary(value);
				 this._task.tags = value;
			 }
		 }else if(preference.getKey().equals(TaskFolderPresenter.PRIORITY_KEY)){
			 ListPreference lp = (ListPreference)preference;
			 int index = lp.findIndexOfValue((String)objValue);
			 lp.setSummary(lp.getEntries()[index]);
			 this._task.priority = (Integer.parseInt((String)objValue));//this code must be changed
		 }else if(preference.getKey().equals(TaskFolderPresenter.NOTE_KEY)){
			 preference.setSummary((String)objValue);
			 this._task.note = ((String)objValue);
		 }else if(preference.getKey().equals(TaskFolderPresenter.PARENET_KEY)){
			 ListPreference lp = (ListPreference)preference;
			 int index = lp.findIndexOfValue((String)objValue);
			 preference.setSummary(lp.getEntries()[index]);
			 this._task.parentTaskId = (Long.parseLong((String)objValue));
		 }else if(preference.getKey().equals(TaskFolderPresenter.REPEAT_PEROID_KEY)){
			 preference.setSummary((String)objValue);
			 this._task.repeat_proid = (Integer.parseInt((String)objValue));
		 }else if(preference.getKey().equals(TaskFolderPresenter.EXPIRE_KEY)){
			 preference.setSummary((String)objValue);
			 this._task.expired = ((String)objValue);
		 }else if(preference.getKey().equals(TaskFolderPresenter.TASK_FOLDER_KEY)){
			 ListPreference lp = (ListPreference)preference;
			 int index = lp.findIndexOfValue((String)objValue);
			 preference.setSummary(lp.getEntries()[index]);
			 this._task.taskFolderId = (Long.parseLong((String)objValue));
			 if(this._task.taskFolderId != this._folderindex){
				 this._task.parentTaskId = Task.invalidId;
				 loadTaskParent((int)(this._task.taskFolderId));
			 }
		 }else if((preference.getKey().equals(TaskFolderPresenter.REPEAT_KEY))){
			 this._task.repeat = ((Boolean)objValue).booleanValue();
		 }
		 
		 return true;
	 }
	
}
