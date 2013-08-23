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

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class TaskEditActivity extends SherlockPreferenceActivity  implements Preference.OnPreferenceChangeListener{
	private EditTextPreference namePreference;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Used to put dark icons on light action bar
        boolean isLight = MainActivity.THEME == R.style.Theme_Sherlock_Light;

        menu.add("Save")
            .setIcon(isLight ? R.drawable.ic_compose_inverse : R.drawable.ic_compose)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add("Search")
            .setIcon(isLight ? R.drawable.ic_search_inverse : R.drawable.ic_search)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add("Refresh")
            .setIcon(isLight ? R.drawable.ic_refresh_inverse : R.drawable.ic_refresh)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.taskedit);
        namePreference = (EditTextPreference)findPreference("taskedit_name");
        namePreference.setOnPreferenceChangeListener(this);
        
    }

	/*@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Toast.makeText(this, sharedPreferences.getString(key, "HahaTest"), Toast.LENGTH_SHORT).show();
		if(key.equals("taskedit_name")){
			Preference namePref = findPreference(key);
            // Set summary to be the user-description for the selected value
			//namePref.setSummary(sharedPreferences.getString(key, ""));
			String test = sharedPreferences.getString(key, "");
			namePref.setTitle(sharedPreferences.getString(key, ""));
		}
		
	}*/

	/* (non-Javadoc)
	 * @see android.preference.PreferenceActivity#onPreferenceTreeClick(android.preference.PreferenceScreen, android.preference.Preference)
	 */
	
	/*@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		
		Toast.makeText(this, preference.getTitle(), Toast.LENGTH_SHORT).show();
		preference.notifyDependencyChange(true);
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}*/
	
	 public boolean onPreferenceChange(Preference preference, Object objValue) { 
		 Toast.makeText(this, preference.getTitle(), Toast.LENGTH_SHORT).show();
		 preference.setTitle((String)objValue);
		 return true;
	 }
	
	
	
}
