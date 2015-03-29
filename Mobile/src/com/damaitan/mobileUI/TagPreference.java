/**
 * 
 */
package com.damaitan.mobileUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.damaitan.datamodel.Task;
import com.damaitan.service.ModelManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * @author admin
 *
 */
public class TagPreference extends DialogPreference {

	final static private String nameKey = "name";
	final static private String checkKey = "select";
	private ListView m_lst = null;
	private EditText m_edt = null;
	private ArrayList<Map<String,Object>> m_items = null;
	private String m_tag;
	
	public TagPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPositiveButtonText(context.getResources().getString(R.string.ok));
		setNegativeButtonText(context.getResources().getString(R.string.cancel));
	}
	
	

	/* (non-Javadoc)
	 * @see android.preference.DialogPreference#onCreateDialogView()
	 */
	@SuppressLint("UseSparseArrays")
	@Override
	protected View onCreateDialogView() {
		this.setDialogLayoutResource(R.layout.tag_perference);
		View view = super.onCreateDialogView();
		m_lst = (ListView)view.findViewById(R.id.task_tag_list);
		m_edt = (EditText)view.findViewById(R.id.task_tag_txt);
		m_items = new ArrayList<Map<String,Object>>(); 
		getData(m_items);
		
		final SimpleAdapter adapter = new SimpleAdapter(this.getContext(),m_items,R.layout.tag_list_item,new String[]{"selected"},new int[]{R.id.tag_check_listitem}){

			
			/* (non-Javadoc)
			 * @see android.widget.SimpleAdapter#getView(int, android.view.View, android.view.ViewGroup)
			 */
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				CheckBox chbox = (CheckBox)view.findViewById(R.id.tag_check_listitem);
				Map<String,Object> lstitem = m_items.get(position);
				chbox.setChecked((Boolean)lstitem.get(checkKey));
				chbox.setText((String)lstitem.get(nameKey));
				chbox.setTag(position);
				if(!chbox.hasOnClickListeners()){
					chbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
						@Override
			            public void onCheckedChanged(CompoundButton buttonView, boolean ischecked) {
							int position  = (Integer)buttonView.getTag();
							Map<String,Object> lstitem = m_items.get(position);
							if(ischecked != (Boolean)lstitem.get(checkKey)){
								lstitem.put(checkKey, ischecked);
							}
						}
					});
				}
				return view;
			}
		}; 
		m_lst.setAdapter(adapter);
		return view;
	}
	
	
	private int keyIsExist(String key) {
		for (int i = 0; i < m_items.size();i++) {
			Map<String,Object> item = m_items.get(i);
			if (key.trim().equalsIgnoreCase((String) item.get(nameKey))) {
				return i;
			}
		}
		return -1;
	}
	
	void getData(ArrayList<Map<String,Object>> items){
		items.clear();
		for(String item : ModelManager.getInstance().getTags()){
			Map<String,Object> lstitem = new HashMap<String,Object>();
			if(m_tag != null && m_tag.contains(item.trim())){
				lstitem.put(checkKey, true);
			}else{
				lstitem.put(checkKey, false);
			}
			lstitem.put(nameKey, item.trim());
			items.add(lstitem); 
		}
	}

	/* (non-Javadoc)
	 * @see android.preference.DialogPreference#onDialogClosed(boolean)
	 */
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		Log.d("TagPreference onDialogClosed", Boolean.toString(positiveResult));
		if(positiveResult){
			String name = m_edt.getText().toString().trim();
			name = name.replace("\n", Task.TAGSPLITTER);
			StringBuffer value = new StringBuffer();
			for(String key : name.split(Task.TAGSPLITTER)){
				if(keyIsExist(key.trim()) == -1) value.append(key.trim() + Task.TAGSPLITTER);
			}
			
			for(Map<String,Object> item : m_items){
				Log.d("TagPreference onDialogClosed ", (String)item.get(nameKey) + ":" + (Boolean)item.get(checkKey));
				if((Boolean)item.get(checkKey)){
					value.append((((String)item.get(nameKey)).trim()) + Task.TAGSPLITTER);
				}
			}
			 
			Log.d("TagPreference onDialogClosed ","value : " + value.toString().trim());
			callChangeListener(value.toString().trim());
		}
		super.onDialogClosed(positiveResult);
	}

	/* (non-Javadoc)
	 * @see android.preference.Preference#setDefaultValue(java.lang.Object)
	 */
	@Override
	public void setDefaultValue(Object defaultValue) {
		m_tag = ((String)defaultValue).trim();
		super.setDefaultValue(defaultValue);
	}

	
	
	


	
	

	
	
	
	

}
