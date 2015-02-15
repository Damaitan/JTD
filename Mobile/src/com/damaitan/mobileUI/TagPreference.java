/**
 * 
 */
package com.damaitan.mobileUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.damaitan.datamodel.Task;
import com.damaitan.service.ModelManager;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
	private Button _btn = null;
	private ArrayList<Map<String,Object>> _items = null;
	private String _tag;
	public TagPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPositiveButtonText(context.getResources().getString(R.string.ok));
		setNegativeButtonText(context.getResources().getString(R.string.cancel));
	}

	/* (non-Javadoc)
	 * @see android.preference.DialogPreference#onCreateDialogView()
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected View onCreateDialogView() {
		this.setDialogLayoutResource(R.layout.tag_perference);
		View view = super.onCreateDialogView();
		_lst = (ListView)view.findViewById(R.id.task_tag_list);
		_edt = (EditText)view.findViewById(R.id.task_tag_txt);
		_btn = (Button)view.findViewById(R.id.task_tag_btn_add);
		_items = (ArrayList<Map<String, Object>>) getData();
		final SimpleAdapter adapter = new SimpleAdapter(this.getContext(),_items,R.layout.tag_list_item,new String[]{"selected"},new int[]{R.id.tag_check_listitem}){

			
			/* (non-Javadoc)
			 * @see android.widget.SimpleAdapter#getView(int, android.view.View, android.view.ViewGroup)
			 */
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				Map<String,Object> lstitem = _items.get(position);
				CheckBox chbox = (CheckBox)view.findViewById(R.id.tag_check_listitem);
				chbox.setText((String)lstitem.get(nameKey));
				chbox.setChecked((Boolean)lstitem.get(checkKey));
				chbox.setTag(lstitem);
				if(!chbox.hasOnClickListeners()){
					chbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
						@Override
			            public void onCheckedChanged(CompoundButton buttonView, boolean ischecked) {
			            	Map<String,Object> lstitem = (Map<String,Object>)buttonView.getTag();
			            	lstitem.put(checkKey, ischecked);
						}
					});
			        
				}
				return view;
			}
		}; 
		_lst.setAdapter(adapter);
		_btn.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {		
				String name = _edt.getText().toString().trim();
				name = name.replace("\n", Task.TAGSPLITTER);
				for(String str : name.split(Task.TAGSPLITTER)){
					if(!str.trim().equalsIgnoreCase("")){
						boolean goNext = true;
						for(Map<String,Object> item : _items){
							if(str.trim().equalsIgnoreCase((String)item.get(nameKey))){
								item.put(checkKey, true);
								goNext = false;
								break;
							}
						}
						if(!goNext){
							continue;
						}
						Map<String,Object> lstitem = new HashMap<String,Object>();
						lstitem.put(checkKey, true);
						lstitem.put(nameKey, str);
						_items.add(lstitem);
					}
				}
				Log.d("TagPreference", "Button is clicked :" + name);
				adapter.notifyDataSetChanged();
				_edt.setText("");
			}
		});
		return view;
	}
	
	List<? extends Map<String, ?>> getData(){
		ArrayList<Map<String,Object>> items = new ArrayList<Map<String,Object>>(); 
		for(String item : ModelManager.getInstance().getTags()){
			Map<String,Object> lstitem = new HashMap<String,Object>();
			lstitem.put(checkKey, _tag != null ? _tag.contains(item) : false);
			lstitem.put(nameKey, item);
			items.add(lstitem); 
		}
		if(_tag == null){
			return items;
		}
		for(String item : _tag.split(Task.TAGSPLITTER)){
			if(ModelManager.getInstance().getTags() == null || !ModelManager.getInstance().getTags().contains(item)){
				Map<String,Object> lstitem = new HashMap<String,Object>();
				lstitem.put(checkKey, true);
				lstitem.put(nameKey, item.trim());
				items.add(lstitem); 
			}
		}
		return items;
	}

	/* (non-Javadoc)
	 * @see android.preference.DialogPreference#onDialogClosed(boolean)
	 */
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		Log.d("TagPreference onDialogClosed", Boolean.toString(positiveResult));
		if(positiveResult){
			StringBuffer value = new StringBuffer();
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
		_tag = (String)defaultValue;
		super.setDefaultValue(defaultValue);
	}

	
	
	


	
	

	
	
	
	

}
