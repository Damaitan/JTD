/**
 * 
 */
package com.damaitan.mobileUI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.internal.widget.IcsAdapterView.AdapterContextMenuInfo;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.damaitan.datamodel.Task;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.presentation.MainViewPresenter;
import com.damaitan.presentation.OnTaskResult;
import com.damaitan.presentation.TaskFolderPresenter;
import com.damaitan.service.GsonHelper;
import com.damaitan.service.ModelManager;
import com.damaitan.datamodel.CommonString;

/**
 * @author admin
 *
 */
public class TaskFolderActivity extends SherlockActivity implements OnTaskResult.ITaskListener{
	
	private static final int MENU_ID_NEW = 0;
	//private static int MENU_ID_DELETE = MENU_ID_NEW + 1;
	//private static int MENU_ID_TAG = MENU_ID_NEW + 2;
	private static final int MENU_ID_FOLDER = MENU_ID_NEW + 3;
	private static final int CONTEXTMENU_ID_EDIT = MENU_ID_NEW + 4;
	private static final int CONTEXTMENU_ID_DELETE = MENU_ID_NEW + 5;
	private static final int CONTEXTMENU_ID_TAG = MENU_ID_NEW + 6;
	
	private int _folderIndex;
	private TaskFolder _folder;
	private ListView mListTask;
	private TaskFolderAdapter m_adapter;
	private TaskFolderPresenter presenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_folder_simple); 
		presenter = new TaskFolderPresenter(this);
		_folderIndex = this.getIntent().getIntExtra(MainViewPresenter.Key_Index, 0);
        try {
        	_folder = presenter.getFolderByIndex(_folderIndex);
			this.setTitle(presenter.getFolderByIndex(_folderIndex).getName());
		} catch (Exception e) {
			Log.e("Error", "TaskActivity onCreate", e);
		}
        mListTask = (ListView)findViewById(R.id.lst_task_folder);
        m_adapter = new TaskFolderAdapter(this,_folderIndex,classifyData());
        mListTask.setAdapter(m_adapter);
        
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Used to put dark icons on light action bar
    	
        menu.add(0,MENU_ID_NEW,0,this.getString(R.string.menu_task_new))
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        /*menu.add(0,MENU_ID_DELETE,1,this.getString(R.string.menu_task_delete))
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);*/

        /*menu.add(0,MENU_ID_TAG,2,this.getString(R.string.menu_task_tag))
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);*/
        
        SubMenu subMenu = menu.addSubMenu(0,MENU_ID_FOLDER,1,this.getString(R.string.menu_task_folder));
        ArrayList<TaskFolder> folders = ModelManager.getInstance().getFolders();
		for(int i=0;i<folders.size();i++){
			TaskFolder folder = folders.get(i);
			subMenu.add(i,i+MENU_ID_FOLDER+1,i,folder.getName());
		}
        MenuItem subMenuItem = subMenu.getItem();
        subMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo info;
		if(menuInfo == null){
			info = new AdapterContextMenuInfo(v,Integer.parseInt((String)v.getTag()), v.getId());
		}else{
			info = (AdapterContextMenuInfo)menuInfo;
			info.position = Integer.parseInt((String)v.getTag());
			info.id = v.getId();
			info.targetView = v;
		}
		super.onCreateContextMenu(menu, v, info);
		menu.setHeaderTitle(this.getString(R.string.contextmenu_task_title));
		menu.add(0, CONTEXTMENU_ID_EDIT, 0, this.getString(R.string.contextmenu_task_edit));
		menu.add(0, CONTEXTMENU_ID_DELETE, 0, this.getString(R.string.contextmenu_task_delete));
		menu.add(0, CONTEXTMENU_ID_TAG, 0, this.getString(R.string.contextmenu_task_tag));
	}
	
	private HashMap<String, TaskFolderAdapter.TaskListInfo> classifyData(){
		HashMap<String, TaskFolderAdapter.TaskListInfo> data= new HashMap<String, TaskFolderAdapter.TaskListInfo>();
		
		for(String item : TaskFolderAdapter.keys){
			data.put(item, new TaskFolderAdapter.TaskListInfo(item));
		}
		for(Task task : _folder.getTasks()){
			data.get(judge(task)).add(task);
    	}
		for(Task task : _folder.getFinishedTasks()){
			data.get(TaskFolderAdapter.KEY_FINISH).add(task);
		}
		startPostion(data);
		return data;
	}
	
	private void startPostion(HashMap<String, TaskFolderAdapter.TaskListInfo> data){
		int length = 0;
		for(int i = 0; i < TaskFolderAdapter.keys.length; i++){
			TaskFolderAdapter.TaskListInfo info = (TaskFolderAdapter.TaskListInfo)data.get(TaskFolderAdapter.keys[i]);
			if(info.tasks == null || info.tasks.size() == 0){
				info.start_position = -1;
				continue;
			}
			info.start_position = length;
			length = length + info.tasks.size() + 1;
		}
	}
	
	private String judge(Task task){
		if(task.status == Task.Status.finished) return TaskFolderAdapter.keys[5];
		if(task.urgent) return TaskFolderAdapter.keys[0];
		
		int index = 0;
		if(task.expired != null && !task.expired.trim().equalsIgnoreCase("")){
			SimpleDateFormat df = new SimpleDateFormat("yyyy" + Task.DATESPLITTER + "MM" + Task.DATESPLITTER + "dd",  Locale.getDefault());
			long diff = df.getCalendar().getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
			if(diff < 0){
				return TaskFolderAdapter.keys[1];
			} 
			diff = diff/(1000*60*60*24);
			if(diff < 1){
				return TaskFolderAdapter.keys[2];
			}
			if(diff < 7 ){
				index = 2;
			}else if(diff < 31){
				index = 3;
			}
		}
		if (task.tags != null && !task.tags.trim().equalsIgnoreCase("")) {
			if (task.tags.contains(CommonString.InitTag[0])) {
					return TaskFolderAdapter.keys[2];
			} else if (task.tags.contains(CommonString.InitTag[1])) {
					return TaskFolderAdapter.keys[3];
			}else if (task.tags.contains(CommonString.InitTag[2])) {
				if(index == 2){
					return TaskFolderAdapter.keys[3];
				}else{
					return TaskFolderAdapter.keys[4];
				}
			}
		}
		return TaskFolderAdapter.keys[5];
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == MENU_ID_NEW){
			Task task = new Task();
			startTaskActivity(task);
		}
        return true;
	}
	
	private void startTaskActivity(Task task){
		Intent intent = new Intent(this,TaskEditActivity.class);
		task.taskFolderId = _folder.getId();
		intent.putExtra(TaskFolderPresenter.KEY_TASK, new GsonHelper().jsonString(task));
		intent.putExtra(MainViewPresenter.Key_Index, _folderIndex);
        startActivityForResult(intent, 0 );
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0 && resultCode == RESULT_OK){
			Log.d("TaskFolderAdapter", "onActivityResult");
			
			//((BaseAdapter) getListView().getAdapter()).notifyDataSetChanged();
		}
	}
	
	
	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo)item.getMenuInfo();
		Task task = m_adapter.getTask(menuInfo.position);
		switch(item.getItemId()){
			case CONTEXTMENU_ID_EDIT:
				if(task != null){
					startTaskActivity(task);
				}
				break;
			case CONTEXTMENU_ID_DELETE:
				
				break;
			case CONTEXTMENU_ID_TAG:
				
				break;
			default:
				
				break;
		}
		return super.onContextItemSelected(item);
	}
	
	


	@Override
	public void onBackPressed() {
		Log.d("TaskFolderActivity", "onBackPressed");
		setResult(RESULT_OK, new Intent());
		finish();
		super.onBackPressed();
	}

	static public class TaskFolderAdapter extends BaseAdapter{
		private static int ITEM_TYPE_CLASS = 0; 
		private static int ITEM_TYPE_TASK = 1;
		public final static String KEY_FINISH = "finish";
		public final static String[] keys = new String[]{"urgent", "expired", "day", "week", "month", "other",KEY_FINISH};
		public final static int[] res_keys = new int[]{R.string.txt_task_folder_urgent,R.string.txt_task_folder_expired, R.string.txt_task_folder_day, R.string.txt_task_folder_week, R.string.txt_task_folder_month, R.string.txt_task_folder_other, R.string.txt_task_folder_finish};
		HashMap<String,TaskListInfo> tasks = null;
	    private LayoutInflater mLayoutInflater; 
	    private Context context;
	    public String tagPrefix;
	    
	    public final static  class TaskFolderGridItem{
	    	public CheckBox doFinish;
	    	public TextView name;
	    	public TextView tag; 
	    	public Button date;
	    	
	    }
	    
	    public final static class TaskListInfo {
	    	public String key = "";
	    	public int start_position = -1;
	    	public List<Task> tasks = null;
	    	public TaskListInfo(String key){
	    		this.key = key;
	    		tasks = new ArrayList<Task>();
	    	}
	    	public boolean add(Task task){
	    		return tasks.add(task);
	    	}
	    }
	    
	    public TaskFolderAdapter(Context context, int taskFolderIndex, HashMap<String,TaskListInfo> data){
	    	this.context = context;
	    	mLayoutInflater = LayoutInflater.from(context);
	    	this.tasks = data;
	    	tagPrefix = taskFolderIndex == 0 ? CommonString.InitJsonString[0] + " - ": "";
	    }
	    
		@Override
		public int getCount() {
			int count = 0;
			for(String key : keys){
				TaskFolderAdapter.TaskListInfo info = (TaskFolderAdapter.TaskListInfo)this.tasks.get(key);
				if(info.start_position < 0) continue;
				count = count + info.tasks.size() + 1;
			}
			return count;
		}

		@Override
		public int getItemViewType(int position) {
			return getItemType(position);
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public Object getItem(int position) {
			return getTask(position);
		}

		@Override
		public long getItemId(int position) {
			Task task = getTask(position);
			if(task != null) return task.getId();
			return Task.invalidId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.d("TaskFolderAdpater getView", "postion " + position);
			int itemType = getItemType(position);
			if(itemType == ITEM_TYPE_CLASS){
				convertView = mLayoutInflater.inflate(R.layout.task_folder_text, null);
				TextView textView = (TextView)convertView.findViewById(R.id.task_folder_listitem_text);
				textView.setText(getClassName(position));
				
			}else if(itemType == ITEM_TYPE_TASK){
				convertView = mLayoutInflater.inflate(R.layout.task_folder_item, null);
				TaskFolderGridItem item = new TaskFolderGridItem();
				item.doFinish = (CheckBox)convertView.findViewById(R.id.task_folder_item_check);
				item.name = (TextView)convertView.findViewById(R.id.task_folder_item_name);
				item.tag = (TextView)convertView.findViewById(R.id.task_folder_item_tag);
				item.date = (Button)convertView.findViewById(R.id.task_folder_item_btn_date);
				Task task = getTask(position);
				item.doFinish.setChecked(task.status == Task.Status.finished);
				item.name.setText(task.getName());
				item.name.setTag(String.valueOf(position));
				item.tag.setText(tagPrefix + "(" + task.tags+ ")");
				if(task.expired == null || task.expired.isEmpty()){
					item.date.setText(R.string.txt_task_folder_nothing);
				}else{
					SimpleDateFormat df = new SimpleDateFormat("MM" + Task.DATESPLITTER + "dd",  Locale.getDefault());
					try {
						item.date.setText(df.parse(task.expired).toString());
					} catch (ParseException e) {
						item.date.setText(R.string.txt_task_folder_nothing);
					}
				}
				if(!item.doFinish.hasOnClickListeners()){
					item.doFinish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
						@Override
			            public void onCheckedChanged(CompoundButton buttonView, boolean ischecked) {
							
						}
					});
				}
				((Activity) this.context).registerForContextMenu(item.name); 
				//((Activity) this.context).registerForContextMenu(item.tag);
				if(!item.date.hasOnClickListeners()){
					item.date.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View arg0) {
							
							
						}
						
					});
				}
					
				
			}
			return convertView;
		}
		
		public int getItemType(int position){
			for(String key : keys){
				TaskFolderAdapter.TaskListInfo info = (TaskFolderAdapter.TaskListInfo)tasks.get(key);
				if(position == info.start_position) return ITEM_TYPE_CLASS;
			}
			return ITEM_TYPE_TASK;
		}
		
		
		private String getClassName(int position){
			for(int i = 0; i < keys.length; i++){
				TaskFolderAdapter.TaskListInfo info = (TaskFolderAdapter.TaskListInfo)tasks.get(keys[i]);
				if(position == info.start_position){
					return this.context.getString(res_keys[i]);
				}
			}
			return null;
		}
		
		private Task getTask(int position){
			for(String key : keys){
				TaskFolderAdapter.TaskListInfo info = (TaskFolderAdapter.TaskListInfo)tasks.get(key);
				if(info.start_position < 0) continue;
				if(position > info.start_position && position < info.start_position + info.tasks.size() + 1){
					return info.tasks.get(position - info.start_position - 1);
				}
			}
			return null;
		}
	
	}

	@Override
	public int act(Type arg0, TaskFolder arg1, Task arg2) {
		// TODO Auto-generated method stub
		return 0;
	}
}
