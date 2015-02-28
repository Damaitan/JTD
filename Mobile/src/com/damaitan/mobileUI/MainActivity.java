/*
 * Copyright (C) 2011 The Android Open Source Project
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.damaitan.exception.PresentationException;
import com.damaitan.presentation.MainViewPresenter;

public class MainActivity extends SherlockListActivity{
    private static int MENU_ID_SETTING = 0;
    private static int MENU_ID_STATISTICS = 2;
    private static int MENU_ID_CLEAN = 3;
    private MainViewPresenter presenter;
    private List<Map<String, Object>> m_listData = null;
    
    
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainViewPresenter();
        m_listData = new ArrayList<Map<String, Object>>();
        Log.i("Mobile", "MainActivity is starting...., Path is " + getApplicationContext().getFilesDir().getAbsolutePath() + JsonHelper.JTDFile);
		try {
			String path = getApplicationContext().getFilesDir().getAbsolutePath() + "/" + JsonHelper.JTDFile;
			String content;
			if (JsonHelper.isFileExist(path)) {
				content = JsonHelper.getJsonString(this);
				Log.d("MainActivity FileExist", content);
			}else{
				content = presenter.initJsonString();
				JsonHelper.saveJsonStringToFile(this, content);
				Log.i("Mobile", "Create new file : " + JsonHelper.JTDFile);
				Log.d("MainActivity No File", content);
			}
			presenter.initialization(content);
			presenter.getData(m_listData);
			setListAdapter(new SimpleAdapter(this, m_listData,
					android.R.layout.simple_list_item_1,
					new String[] { MainViewPresenter.Key_Title }, new int[] { android.R.id.text1 }));
		} catch (PresentationException e) {
			Log.e("Mobile", "PresentationException", e);
		} catch (FileNotFoundException e) { // File is not found
			Log.i("Mobile",
					"It must be something wrong, this file shall be created if existed",
					e);
		} catch (IOException e) { // comes from file writing
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getListView().setTextFilterEnabled(true);
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,MENU_ID_SETTING,MENU_ID_SETTING,this.getString(R.string.menu_main_setting))
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        
        menu.add(0,MENU_ID_STATISTICS,MENU_ID_STATISTICS,this.getString(R.string.menu_main_statistics))
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add(0,MENU_ID_CLEAN,MENU_ID_CLEAN,this.getString(R.string.menu_main_clean))
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        
        return true;
        
    }
    
    

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId() == MENU_ID_CLEAN){
    		presenter.cleanFinished();
		}
		
        return true;
	}


	@Override
    @SuppressWarnings("unchecked")
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, Object> map = (Map<String, Object>)l.getItemAtPosition(position);
        Intent intent = new Intent(this,TaskFolderActivity.class);
        Integer index = (Integer)map.get(MainViewPresenter.Key_Index);
        intent.putExtra(MainViewPresenter.Key_Index, index.intValue());
        startActivityForResult(intent,0);
    }

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 0 && resultCode == RESULT_OK){
			Log.d("MainActivity", "onActivityResult");
			m_listData.clear();
			presenter.getData(m_listData);
			((BaseAdapter) getListView().getAdapter()).notifyDataSetChanged();
		}
	}
	
	
}
