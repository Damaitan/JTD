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
	private ListView _lst = null;
	private EditText _edt = null;
	private ArrayList<Map<String,Object>> _items = null;
	private String _tag;
	private Map<Integer, Integer> m_selects;
	
	
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
		_lst = (ListView)view.findViewById(R.id.task_tag_list);
		_edt = (EditText)view.findViewById(R.id.task_tag_txt);
		_items = new ArrayList<Map<String,Object>>(); 
		getData(_items);
		m_selects = new HashMap<Integer, Integer>();
		initSelects();
		
		final SimpleAdapter adapter = new SimpleAdapter(this.getContext(),_items,R.layout.tag_list_item,new String[]{"selected"},new int[]{R.id.tag_check_listitem}){

			
			/* (non-Javadoc)
			 * @see android.widget.SimpleAdapter#getView(int, android.view.View, android.view.ViewGroup)
			 */
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				boolean setTag = false;
				if(convertView == null){
					setTag = true;
				}
				View view = super.getView(position, convertView, parent);
				
				CheckBox chbox = (CheckBox)view.findViewById(R.id.tag_check_listitem);
				if (m_selects.containsKey(position)) {
					chbox.setChecked(true);
				} else {
					chbox.setChecked(false);
				}
				Map<String,Object> lstitem = _items.get(position);
				chbox.setText((String)lstitem.get(nameKey));
				if(setTag){
					chbox.setTag(position);
				}
				
				if(!chbox.hasOnClickListeners()){
					chbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
						@Override
			            public void onCheckedChanged(CompoundButton buttonView, boolean ischecked) {
							int position  = (Integer)buttonView.getTag();
							if(ischecked){
								if(!m_selects.containsKey(position)){ 
									m_selects.put(position, position);
									Map<String,Object> lstitem = _items.get(position);
									lstitem.put(checkKey, true);
								}
							}else{
								m_selects.remove(position);
								Map<String,Object> lstitem = _items.get(position);
								lstitem.put(checkKey, false);
							}
							
						}
					});
				}
				return view;
			}
		}; 
		_lst.setAdapter(adapter);
		return view;
	}
	
	private void initSelects(){
    	m_selects.clear();
    	if(_items == null) return;
    	for(int i = 0; i < _items.size();i++){
    		Boolean value = (Boolean)(_items.get(i).get(checkKey));
    		if(value.booleanValue()){
    			m_selects.put(i, i);
    		}
    	}
    }
	
	private int keyIsExist(String key) {
		for (int i = 0; i < _items.size();i++) {
			Map<String,Object> item = _items.get(i);
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
			if(_tag != null && _tag.contains(item.trim())){
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
			String name = _edt.getText().toString().trim();
			name = name.replace("\n", Task.TAGSPLITTER);
			StringBuffer value = new StringBuffer();
			for(String key : name.split(Task.TAGSPLITTER)){
				if(keyIsExist(key.trim()) == -1) value.append(key.trim() + Task.TAGSPLITTER);
			}
			
			for(Map<String,Object> item : _items){
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
		_tag = ((String)defaultValue).trim();
		super.setDefaultValue(defaultValue);
	}

	
	
	


	
	

	
	
	
	

}
