/**
 * 
 */
package com.damaitan.mobileUI;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.damaitan.datamodel.Task;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.ServiceException;
import com.damaitan.service.ServiceHandler;
import com.damaitan.service.TaskFolderHandler;

/**
 * @author admin
 *
 */
public class TaskFolderActivity extends SherlockActivity {
	
	private static int MENU_ID_NEW = 0;
	private static int MENU_ID_DELETE = MENU_ID_NEW + 1;
	private static int MENU_ID_TAG = MENU_ID_NEW + 2;
	private static int MENU_ID_FOLDER = MENU_ID_NEW + 3;
	private int _folderIndex;
	private TaskFolder _folder;
	
	private ListView mListTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_folder_simple); 
		_folderIndex = this.getIntent().getIntExtra(Name.Index, 0);
        try {
        	_folder = TaskFolderHandler.getFolderByIndex(_folderIndex);
        	/*Task task1 = new Task();
        	task1.setName("Task1");
        	Task task2 = new Task();
        	task2.setName("Task2");
        	_folder.addTask(task1);
        	_folder.addTask(task2);*/
			this.setTitle(TaskFolderHandler.getFolderByIndex(_folderIndex).getName());
		} catch (Exception e) {
			Log.e("Error", "TaskActivity onCreate", e);
			//Exit program
		}
        mListTask = (ListView)findViewById(R.id.lst_task_folder);
        mListTask.setAdapter(new TaskFolderAdapter(this,classifyData()));
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Used to put dark icons on light action bar
    	
        menu.add(0,MENU_ID_NEW,0,this.getString(R.string.menu_task_new))
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        menu.add(0,MENU_ID_DELETE,1,this.getString(R.string.menu_task_delete))
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add(0,MENU_ID_TAG,2,this.getString(R.string.menu_task_tag))
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        
        SubMenu subMenu = menu.addSubMenu(0,MENU_ID_FOLDER,2,this.getString(R.string.menu_task_folder));
        try {
			ArrayList<TaskFolder> folders = TaskFolderHandler.getFolders();
			for(int i=0;i<folders.size();i++){
				TaskFolder folder = folders.get(i);
				subMenu.add(i,i+MENU_ID_FOLDER+1,i,folder.getName());
			}
		} catch (ServiceException e) {
			Log.e("Error", "TaskActivity onCreateOptionsMenu : ServiceException", e);
		}
        MenuItem subMenuItem = subMenu.getItem();
        subMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }
	
	private HashMap<String, List<Task>> classifyData(){
		HashMap<String, List<Task>> data= new HashMap<String, List<Task>>();
		data.put(TaskFolderAdapter.EXPIRED, new ArrayList<Task>());
		data.put(TaskFolderAdapter.DAY, new ArrayList<Task>());
		data.put(TaskFolderAdapter.WEEK, new ArrayList<Task>());
		data.put(TaskFolderAdapter.MONTH, new ArrayList<Task>());
		data.put(TaskFolderAdapter.OTHER, new ArrayList<Task>());
		data.put(TaskFolderAdapter.FINISH, new ArrayList<Task>());
		
		for(Task task : _folder.getTasks()){
			data.get(TaskFolderAdapter.DAY).add(task);
			data.get(TaskFolderAdapter.MONTH).add(task);
    	}
		for(Task task : _folder.getFinishedTasks()){
			data.get(TaskFolderAdapter.FINISH).add(task);
		}
		return data;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == MENU_ID_NEW){
			Intent intent = new Intent(this,TaskEditActivity.class);
			Task task = new Task();
			task.setTaskFolderId(_folder.getId());
			intent.putExtra(Name.TASK_KEY, ServiceHandler.toJson(task));
			intent.putExtra(Name.Index, _folderIndex);
	        startActivity(intent);
		}
        return true;
	}
	
	static public class TaskFolderAdapter extends BaseAdapter{
		private static int ITEM_TYPE_CLASS = 0; 
		private static int ITEM_TYPE_TASK = 1;
		private int position_expired = -1;
		private int position_day = -1;
		private int position_week = -1;
		private int position_month = -1;
		private int position_other = -1;
		private int position_finish = -1;
		private int count = 0;
		public static String EXPIRED = "expired";
		public static String DAY = "day";
		public static String WEEK = "week";
		public static String MONTH = "month";
		public static String OTHER = "other";
		public static String FINISH = "finish";
		HashMap<String, List<Task>> tasks = null;
	    private LayoutInflater mLayoutInflater; 
	    private Context context;
	    public TaskFolderAdapter(Context context, HashMap<String, List<Task>> tasks){
	    	this.tasks = tasks;
	    	init();
	    	this.context = context;
	    	mLayoutInflater = LayoutInflater.from(context);
	    }
	    
		@Override
		public int getCount() {
			return this.count;
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
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int itemType = getItemType(position);
			if(itemType == ITEM_TYPE_CLASS){
				convertView = mLayoutInflater.inflate(R.layout.task_folder_text, null);
				TextView textView = (TextView)convertView.findViewById(R.id.task_folder_listitem_text);
				textView.setText(getClassName(position));
				
			}else if(itemType == ITEM_TYPE_TASK){
				convertView = mLayoutInflater.inflate(R.layout.task_folder_item, null);
				Task task = getTask(position);
				if(task.getStatus() == Task.Status.finished){
					CheckBox chb = (CheckBox)convertView.findViewById(R.id.task_folder_item_finishit);
					chb.setChecked(true);
				}
				TextView txt = (TextView)convertView.findViewById(R.id.task_folder_item_name);
				txt.setText(task.getName());
			}
			return convertView;
		}
		
		public int getItemType(int position){
			if(position == position_expired || position == position_day || position == position_week || position == position_month || position == position_other || position == position_finish){
				return ITEM_TYPE_CLASS;
			}else{
				return ITEM_TYPE_TASK;
			}
		}
		
		private void init(){
			if(this.tasks != null){
				int nextposition = -1;
				int length = this.tasks.get(EXPIRED).size();
				int length_pre = 0;
				if(length != 0){
					position_expired = nextposition + length_pre + 1;
					nextposition = position_expired;
					length_pre = length;
				}
				
				length = this.tasks.get(DAY).size();
				if(length != 0){
					position_day = nextposition + length_pre + 1;
					nextposition = position_day;
					length_pre = length;
				}
				
				length = this.tasks.get(WEEK).size();
				if(length != 0){
					position_week = nextposition + length_pre + 1;
					nextposition = position_week;
					length_pre = length;
				}
				
				length = this.tasks.get(MONTH).size();
				if(length != 0){
					position_month = nextposition + length_pre + 1;
					nextposition = position_month;
					length_pre = length;
				}
				
				length = this.tasks.get(OTHER).size();
				if(length != 0){
					position_other = nextposition + length_pre + 1;
					nextposition = position_other;
					length_pre = length;
				}
				
				length = this.tasks.get(FINISH).size();
				if(length != 0){
					position_finish = nextposition + length_pre + 1;
				}
				count = nextposition + length_pre + 1;
			}
		}
		
		private String getClassName(int position){
			if(position == position_expired)
				return this.context.getString(R.string.txt_task_folder_expired);
			if(position == position_day)
				return this.context.getString(R.string.txt_task_folder_day);
			if(position == position_week)
				return this.context.getString(R.string.txt_task_folder_week);
			if(position == position_month)
				return this.context.getString(R.string.txt_task_folder_month);
			if(position == position_other)
				return this.context.getString(R.string.txt_task_folder_other);
			if(position == position_finish)
				return this.context.getString(R.string.txt_task_folder_finish);
			return null;
		}
		
		private Task getTask(int position){
			if(position_finish != -1 && position > position_finish)
				return this.tasks.get(FINISH).get(position - position_finish - 1);
			if(position_other != -1 && position > position_other)
				return this.tasks.get(OTHER).get(position - position_other - 1);
			if(position_month != -1 && position > position_month)
				return this.tasks.get(MONTH).get(position - position_month - 1);
			if(position_week != -1 && position > position_week)
				return this.tasks.get(WEEK).get(position - position_week - 1);
			if(position_day != -1 && position > position_day)
				return this.tasks.get(DAY).get(position - position_day - 1);
			if(position_expired != -1 && position > position_expired)
				return this.tasks.get(EXPIRED).get(position - position_expired - 1);
			return null;
		}
	
	}
}