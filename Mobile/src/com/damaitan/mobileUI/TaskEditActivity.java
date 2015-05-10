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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.damaitan.datamodel.Model;
import com.damaitan.datamodel.Task;
import com.damaitan.exception.ServiceException;
import com.damaitan.presentation.TaskFolderPresenter;
import com.damaitan.service.GsonHelper;

public class TaskEditActivity extends Activity{
	
	boolean m_includeChilds = true;
	TaskFolderPresenter m_presenter;
	TaskEditPreferenceFragment m_fragment;
	
	private static int MENU_ID_SAVE = 4;
	private static int MENU_ID_DELETE = MENU_ID_SAVE + 1;
	private static int MENU_ID_FINISH = MENU_ID_SAVE + 4; 
	
	public TaskEditActivity(){
	}
	
    public boolean onCreateOptionsMenu(Menu menu) {
    	if(m_fragment._task.getId() != Task.invalidId){
    		this.setTitle(m_fragment._task.getName());
    	}
    	menu.add(0,MENU_ID_SAVE,0,this.getString(R.string.menu_taskedit_save))
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		if (m_fragment._task.getId() != Task.invalidId) {
			menu.add(0, MENU_ID_FINISH, 0,
					this.getString(R.string.menu_taskedit_finish))
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	
			menu.add(0, MENU_ID_DELETE, 0,
					this.getString(R.string.menu_taskedit_delete))
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
		
        return super.onCreateOptionsMenu(menu);
    }
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.);
        m_presenter = new TaskFolderPresenter(false);
        String json = this.getIntent().getStringExtra(TaskFolderPresenter.KEY_TASK);
        m_fragment = new TaskEditPreferenceFragment();
        m_fragment._task = new GsonHelper().fromJson(json, Task.class);
        m_fragment._folderindex = (int)(m_fragment._task.taskFolderId);
        m_fragment._presenter = m_presenter;
        android.app.FragmentManager fragmentManager = getFragmentManager();  
        FragmentTransaction fragmentTransaction =   
             fragmentManager.beginTransaction();  
        fragmentTransaction.replace(android.R.id.content, m_fragment);          
        fragmentTransaction.addToBackStack(null);   
        fragmentTransaction.commit();  
        
    	
    	
    }

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockPreferenceActivity#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == MENU_ID_SAVE) {
			Log.e("TaskEditActivity", "onOptionsItemSelected " + MENU_ID_SAVE);
			try {
				if (m_fragment._task.getName().trim().equalsIgnoreCase("")
						|| m_fragment._task.getName().trim()
								.equalsIgnoreCase(Model.invalidStr)) {
					Toast.makeText(this, "Please input task name",
							Toast.LENGTH_SHORT).show();
				} else {
					if (m_fragment._task.getId() != Task.invalidId) {
						setResult(m_presenter.saveTask(m_fragment._folderindex,
								m_fragment._task, false), new Intent());
					} else {
						setResult(m_presenter.saveTask((int) (m_fragment._task.taskFolderId),
								m_fragment._task, true), new Intent());
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
			m_includeChilds = true;
			new AlertDialog.Builder(this)
					.setTitle(this.getTitle())
					.setMessage(R.string.alert_taskedit_delete)
					.setPositiveButton(R.string.ok,null)
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									m_includeChilds = false;
								}
							}).show();

			m_presenter.delete(m_fragment._folderindex, m_fragment._task, m_includeChilds);
			setResult(RESULT_OK, new Intent());
			finish();
		}
		if (item.getItemId() == MENU_ID_FINISH){
			m_presenter.finishTask(m_fragment._folderindex,
					m_fragment._task);
			setResult(RESULT_OK, new Intent());
			finish();
		}
		return true;
	}
	
}
