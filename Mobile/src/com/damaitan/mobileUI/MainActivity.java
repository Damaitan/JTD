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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.PresentationException;
import com.damaitan.presentation.IViewMain;
import com.damaitan.presentation.MainViewPresenter;

public class MainActivity extends SherlockListActivity implements IViewMain{
    public static int THEME = R.style.Theme_Sherlock;
    public static String JTDFile = "JTD.json";
    public MainViewPresenter presenter;
    
    public static boolean isFileExist(String path) {
		if (path == null) {
			return false;
		}
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        presenter = new MainViewPresenter(this);
        Log.i("Mobile", "MainActivity is starting....");
		try {
			if (!isFileExist(JTDFile)) {
				String content = presenter.initJsonString();
				FileOutputStream fout = openFileOutput(JTDFile, MODE_PRIVATE);
				byte[] bytes = content.getBytes();
				fout.write(bytes);
				fout.close();
				Log.i("Mobile", "Create new file : " + JTDFile);
				presenter.initialization(content);
			}else{
				presenter.initialization(getJsonString());
			}
			setListAdapter(new SimpleAdapter(this, presenter.getData(),
					android.R.layout.simple_list_item_1,
					new String[] { "title" }, new int[] { android.R.id.text1 }));
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
    
	

	private String getJsonString() throws Exception{
		FileInputStream stream;

		stream = openFileInput(JTDFile);
		String json;

		byte[] bytes = new byte[1024];
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		while (stream.read(bytes) != -1) {
			arrayOutputStream.write(bytes, 0, bytes.length);
		}
		arrayOutputStream.close();
		stream.close();
		json = new String(arrayOutputStream.toByteArray());
		return json.trim();
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*SubMenu sub = menu.addSubMenu("Theme");
        sub.add(0, R.style.Theme_Sherlock, 0, "Default");
        sub.add(0, R.style.Theme_Sherlock_Light, 0, "Light");
        sub.add(0, R.style.Theme_Sherlock_Light_DarkActionBar, 0, "Light (Dark Action Bar)");
        sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);*/
        
        boolean isLight = ActionItems.THEME == R.style.Theme_Sherlock_Light;

        menu.add("Save")
            .setIcon(isLight ? R.drawable.ic_compose_inverse : R.drawable.ic_compose)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        menu.add("Search")
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add("Refresh")
            .setIcon(isLight ? R.drawable.ic_refresh_inverse : R.drawable.ic_refresh)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
        
    }


    /*protected List<Map<String, Object>> getData() {
        List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory("com.actionbarsherlock.sample.demos.EXAMPLE");

        PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);

        if (null == list)
            return myData;

        String[] prefixPath;
        String prefixWithSlash = prefix;

        if (prefix.equals("")) {
            prefixPath = null;
        } else {
            prefixPath = prefix.split("/");
            prefixWithSlash = prefix + "/";
        }

        int len = list.size();

        Map<String, Boolean> entries = new HashMap<String, Boolean>();

        for (int i = 0; i < len; i++) {
            ResolveInfo info = list.get(i);
            CharSequence labelSeq = info.loadLabel(pm);
            String label = labelSeq != null
                    ? labelSeq.toString()
                    : info.activityInfo.name;

            if (prefixWithSlash.length() == 0 || label.startsWith(prefixWithSlash)) {

                String[] labelPath = label.split("/");

                String nextLabel = prefixPath == null ? labelPath[0] : labelPath[prefixPath.length];

                if ((prefixPath != null ? prefixPath.length : 0) == labelPath.length - 1) {
                    addItem(myData, nextLabel, activityIntent(
                            info.activityInfo.applicationInfo.packageName,
                            info.activityInfo.name));
                } else {
                    if (entries.get(nextLabel) == null) {
                        addItem(myData, nextLabel, browseIntent(prefix.equals("") ? nextLabel : prefix + "/" + nextLabel));
                        entries.put(nextLabel, true);
                    }
                }
            }
        }

        Collections.sort(myData, sDisplayNameComparator);
        addItem(myData, "Á½ÍòÓ¢³ß - Projects", activityIntent(
                "Damai",
                "Hello"));

        return myData;
    }*/

    /*private final static Comparator<Map<String, Object>> sDisplayNameComparator =
        new Comparator<Map<String, Object>>() {
        private final Collator   collator = Collator.getInstance();

        public int compare(Map<String, Object> map1, Map<String, Object> map2) {
            return collator.compare(map1.get("title"), map2.get("title"));
        }
    };*/

    protected Intent activityIntent(String pkg, String componentName) {
        Intent result = new Intent();
        result.setClassName(pkg, componentName);
        return result;
    }

    protected Intent browseIntent(String path) {
        Intent result = new Intent();
        result.setClass(this, MainActivity.class);
        result.putExtra("com.example.android.apis.Path", path);
        return result;
    }

    protected void addItem(List<Map<String, Object>> data, String name, Intent intent) {
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("title", name);
        temp.put("intent", intent);
        data.add(temp);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, Object> map = (Map<String, Object>)l.getItemAtPosition(position);

        //Intent intent = (Intent) map.get("intent");
        //startActivity(intent);
    }

	@Override
	public List<Map<String, Object>> listItems(ArrayList<TaskFolder> folders) {
		List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();
		for (TaskFolder folder : folders) {
			addItem(myData, folder.getSimpleInfo(), activityIntent("Damai", "Hello"));
		}
		return myData;
	}
}
