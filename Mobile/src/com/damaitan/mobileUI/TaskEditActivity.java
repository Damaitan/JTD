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

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class TaskEditActivity extends SherlockPreferenceActivity  implements Preference.OnPreferenceChangeListener{
	private EditTextPreference namePreference;
	private CheckBoxPreference urgentPreference;
	private EditTextPreference tagPreference;
	private ListPreference priorityPreference;
	private EditTextPreference notePreference; 
	
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
		 Toast.makeText(this, "Preference Key:" + preference.getKey(), Toast.LENGTH_SHORT).show();
		 if(preference.getKey().equals("taskedit_name")){
			 preference.setTitle(this.getString(R.string.title_taskedit_name) + ":" + (String)objValue);
		 }else if(preference.getKey().equals("taskedit_urgent"))
		 {
		 }else if(preference.getKey().equals("taskedit_tag"))
		 {
			 preference.setSummary((String)objValue);
		 }else if(preference.getKey().equals("taskedit_priority"))
		 {
			 preference.setTitle(this.getString(R.string.title_taskedit_priority) + ":" + (String)objValue);
		 }
		 else if(preference.getKey().equals("taskedit_note"))
		 {
			 preference.setSummary((String)objValue);
		 }
		 return true;
	 }
	
	
	
}
