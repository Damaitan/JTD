/**
 * 
 */
package com.damaitan.mobileUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * @author admin
 *
 */
public class TagSelectionFragment extends Fragment {

	private ListView lst = null;
	private EditText edt = null;
	private Button btn = null;
	
	/**
	 * 
	 */
	public TagSelectionFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(container == null)
			return null;
		View tagView = inflater.inflate(R.layout.tag_perference, container, false);
		lst = (ListView)tagView.findViewById(R.id.task_tag_list);
		edt = (EditText)tagView.findViewById(R.id.task_tag_txt);
		btn = (Button)tagView.findViewById(R.id.task_tag_btn_add);
		lst.setAdapter(new SimpleAdapter(this.getActivity(),getData(),R.layout.tag_list_item,new String[]{"selected"},new int[]{R.id.tag_check_listitem}));
		
		return tagView;
	}
	
	List<? extends Map<String, ?>> getData(){
		ArrayList<Map<String,Object>> items = new ArrayList<Map<String,Object>>(); 
		Map<String,Object> lstitem = new HashMap<String,Object>();
		lstitem.put("selected", true);
		lstitem.put("name", "tag");
		items.add(lstitem); 
		return items;
	}
	
	
}
