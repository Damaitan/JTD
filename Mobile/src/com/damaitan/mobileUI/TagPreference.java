/**
 * 
 */
package com.damaitan.mobileUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.damaitan.service.TaskFolderHandler;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
	private ListView lst = null;
	private EditText edt = null;
	private Button btn = null;
	private ArrayList<Map<String,Object>> items = null;
	private String tag;
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
		lst = (ListView)view.findViewById(R.id.task_tag_list);
		edt = (EditText)view.findViewById(R.id.task_tag_txt);
		btn = (Button)view.findViewById(R.id.task_tag_btn_add);
		items = (ArrayList<Map<String, Object>>) getData();
		final SimpleAdapter adapter = new SimpleAdapter(this.getContext(),items,R.layout.tag_list_item,new String[]{"selected"},new int[]{R.id.tag_check_listitem}){

			/* (non-Javadoc)
			 * @see android.widget.SimpleAdapter#getView(int, android.view.View, android.view.ViewGroup)
			 */
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				View view = super.getView(position, convertView, parent);
				Map<String,Object> lstitem = items.get(position);
				CheckBox chbox = (CheckBox)view.findViewById(R.id.tag_check_listitem);
				chbox.setText((String)lstitem.get(nameKey));
				chbox.setChecked((Boolean)lstitem.get(checkKey));
				return view;
			}
			
		}; 
		lst.setAdapter(adapter);
		btn.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {		
				Map<String,Object> lstitem = new HashMap<String,Object>();
				String name = edt.getText().toString().trim();
				lstitem.put(checkKey, true);
				lstitem.put(nameKey, name);
				items.add(lstitem);
				Log.d("TagPreference", "Button is clicked :" + name);
				adapter.notifyDataSetChanged();
			}
			
		});
		
		return view;
	}
	
	List<? extends Map<String, ?>> getData(){
		ArrayList<Map<String,Object>> items = new ArrayList<Map<String,Object>>(); 
		for(String item : TaskFolderHandler.getTags()){
			Map<String,Object> lstitem = new HashMap<String,Object>();
			lstitem.put(checkKey, tag.contains(item));
			lstitem.put(nameKey, item);
			items.add(lstitem); 
		}
		return items;
	}

	/* (non-Javadoc)
	 * @see android.preference.DialogPreference#onDialogClosed(boolean)
	 */
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		// TODO Auto-generated method stub
		Log.d("TagPreference onDialogClosed", Boolean.toString(positiveResult));
		super.onDialogClosed(positiveResult);
	}

	/* (non-Javadoc)
	 * @see android.preference.Preference#onGetDefaultValue(android.content.res.TypedArray, int)
	 */
	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		// TODO Auto-generated method stub
		tag = a.getString(index);
		Log.d("TagPreference onGetDefaultValue", tag);
		return super.onGetDefaultValue(a, index);
	}
	
	


	
	

	
	
	
	

}
