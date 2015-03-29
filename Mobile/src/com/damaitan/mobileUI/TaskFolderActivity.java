/**
 * 
 */
package com.damaitan.mobileUI;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.damaitan.datamodel.CommonString;
import com.damaitan.datamodel.Task;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.ServiceException;
import com.damaitan.presentation.MainViewPresenter;
import com.damaitan.presentation.TaskFolderPresenter;
import com.damaitan.presentation.TaskListSorter;
import com.damaitan.service.GsonHelper;
import com.damaitan.service.ModelManager;
import com.damaitan.service.StatisticsService;

/**
 * @author admin
 *
 */
public class TaskFolderActivity extends SherlockActivity {
	
	private static final int MENU_ID_NEW = 0;
	private static final int MENU_ID_FOLDER = MENU_ID_NEW + 10;
	
	private int m_folderIndex;
	private TaskFolder m_folder;
	private ListView mListTask;
	private TaskFolderAdapter m_adapter;
	private TaskFolderPresenter m_presenter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_folder_simple); 
		m_presenter = new TaskFolderPresenter(true);
		m_folderIndex = this.getIntent().getIntExtra(MainViewPresenter.Key_Index, 0);
		mListTask = (ListView)findViewById(R.id.lst_task_folder);
        m_adapter = new TaskFolderAdapter(this,m_folderIndex, m_presenter);
        load(m_folderIndex,false);
        mListTask.setAdapter(m_adapter);
    
	}
	
	private boolean load(int folderIndex, boolean reload) {
		m_folderIndex = folderIndex;
		try {
			m_folder = m_presenter.getFolderByIndex(m_folderIndex);
			this.setTitle(m_presenter.getFolderByIndex(m_folderIndex).getName());
		} catch (Exception e) {
			Log.e("Error", "TaskActivity onCreate", e);
		}
		m_presenter.getSorter().init(m_folder);
		//m_adapter.initSelects();
		m_adapter.taskFolderIndex = folderIndex;
		Log.d("TaskFolderActivity sort", m_presenter.getSorter().log());
		if(reload){
			m_adapter.notifyDataSetChanged();
		}
		return true;
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
        menu.add(0,MENU_ID_NEW,0,this.getString(R.string.menu_task_new))
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        
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
	protected void onDestroy() {
		persist();		
		super.onDestroy();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 0 ){
			m_adapter.notifyDataSetChanged();
		}
	}
	

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == MENU_ID_NEW){
			if(m_folder.getId() != 0){
				Task task = new Task();
				task.taskFolderId = m_folder.getId();
				m_adapter.startTaskActivity(task);
			}
		}
		int folderIndex = item.getItemId() - MENU_ID_FOLDER;
		if(folderIndex > 0){
			load(folderIndex - 1, true);
		}
        return true;
	}
	
	
	
	private void persist(){
		try {
			JsonHelper.saveContentToFile(this, ModelManager.getInstance().JsonString());
			JsonHelper.saveStatisticsToFile(this, StatisticsService.getInstance().JsonString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		Log.d("TaskFolderActivity", "onBackPressed");
		setResult(RESULT_OK, new Intent());
		persist();	
		super.onBackPressed();
	}
	
	

	static public class TaskFolderAdapter extends BaseAdapter{
		public final static int res_keys[] = {R.string.txt_task_folder_urgent,R.string.txt_task_folder_expired, R.string.txt_task_folder_day, R.string.txt_task_folder_week, R.string.txt_task_folder_month, R.string.txt_task_folder_year,R.string.txt_task_folder_other, R.string.txt_task_folder_finish};
		HashMap<String,TaskListInfo> tasks = null;
	    private LayoutInflater mLayoutInflater; 
	    private Context context;
    	public  int taskFolderIndex;
    	private TaskFolderPresenter m_presenter;
    	private boolean m_isGetsystemColor = false;
    	private ColorStateList m_sysColor;
    	
	    public final static  class TaskFolderGridItem{
	    	public int type;
	    	public TextView className = null;
	    	public CheckBox doFinish = null;
	    	public TextView name = null;
	    	public TextView tag = null; 
	    	public Button delete = null;
	    	
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
	    
	    @SuppressLint("UseSparseArrays")
		public TaskFolderAdapter(Context context, int taskFolderIndex, TaskFolderPresenter presenter){
	    	this.context = context;
	    	mLayoutInflater = LayoutInflater.from(context);
	    	this.taskFolderIndex = taskFolderIndex;
	    	m_presenter = presenter;
	    }
	    
		@Override
		public int getCount() {
			int count =  m_presenter.getSorter().getCount();
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
			return m_presenter.getSorter().getTask(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.d("TaskFolderAdpater getView", "postion " + position);
			TaskFolderGridItem item;
			if (convertView == null) {
				item = new TaskFolderGridItem();
				item.type = getItemViewType(position);
				if (getItemViewType(position) == TaskListSorter.ITEM_TYPE_CLASS) {
					convertView = mLayoutInflater.inflate(
							R.layout.task_folder_text, null);
				} else {
					convertView = mLayoutInflater.inflate(
							R.layout.task_folder_item, null);
				}
				prepareView(item.type,convertView, item);
				prepareViewData(item.type,position,item);
				convertView.setTag(item);
			} else {
				item = (TaskFolderGridItem) convertView.getTag();
				if (item.type != getItemViewType(position)){
					if(item.type == TaskListSorter.ITEM_TYPE_CLASS && item.delete == null){
						item.type = TaskListSorter.ITEM_TYPE_TASK;
						convertView = mLayoutInflater.inflate(
								R.layout.task_folder_item, null);
					}
					if(item.type == TaskListSorter.ITEM_TYPE_TASK && item.className == null){
						item.type = TaskListSorter.ITEM_TYPE_CLASS;
						convertView = mLayoutInflater.inflate(
								R.layout.task_folder_text, null);
					}	
				}
				prepareView(item.type,convertView, item);
				prepareViewData(item.type,position,item);
			}
			return convertView;
		}
		
		private void prepareViewData(int type, int position, TaskFolderGridItem item){
			if (type == TaskListSorter.ITEM_TYPE_CLASS) {
				item.className.setText(getClassName(position));
				item.className.setBackgroundColor(Color.WHITE);
				item.className.setTextColor(Color.BLACK);
			} else {
				taskToShow(position, item);
			}
			item.type = type;
		}
		
		private void prepareView(int type, View convertView, TaskFolderGridItem item){
			if(convertView == null) return;
			if (type == TaskListSorter.ITEM_TYPE_CLASS) {
				item.className = (TextView) convertView
						.findViewById(R.id.task_folder_listitem_text);
			} else {
				item.doFinish = (CheckBox) convertView
						.findViewById(R.id.task_folder_item_check);
				item.name = (TextView) convertView
						.findViewById(R.id.task_folder_item_name);
				item.tag = (TextView) convertView
						.findViewById(R.id.task_folder_item_tag);
				item.delete = (Button) convertView
						.findViewById(R.id.task_folder_item_btn_date);
				
				if(!m_isGetsystemColor){
					m_sysColor = item.name.getTextColors();
					m_isGetsystemColor = true;
				}
			}
		}
		
		private void taskToShow(int position , TaskFolderGridItem item) {
			Log.d("TaskFolderActivity sort", m_presenter.getSorter().log());
			Task task = (Task) getItem(position);
			Log.d("TaskFolderActivity taskToShow", "pos:"+ position + "-t:" + task.getName() + "-txt:" + item.name.getText().toString());
			
			String key = m_presenter.getSorter().getTaskKey(position);
			if(key.equals(TaskListSorter.keys[0]) || key.equals(TaskListSorter.keys[1])){
				item.name.setTextColor(Color.RED);
				//item.tag.setTextColor(Color.RED);
			}else{
				item.name.setTextColor(m_sysColor);
				//item.tag.setTextColor(m_sysColor);
			}
			
			String tagPrefix = "";
			if (this.taskFolderIndex == 0) {
				TaskFolder folder = ModelManager.getInstance().getTaskFolder(
						(int) task.taskFolderId);
				if (folder != null) {
					tagPrefix = folder.getName() + " - ";
				}
			}
			item.tag.setText(tagPrefix + "(" + task.tags + ")" + " - " + CommonString.PriorityString[task.priority]);
			item.doFinish.setTag(position);
			if(task.status == Task.Status.finished){
				item.doFinish.setChecked(true);
				item.name.getPaint().setStrikeThruText(true);
			}else{
				item.doFinish.setChecked(false);
				item.name.getPaint().setStrikeThruText(false);
			}
			if (!item.doFinish.hasOnClickListeners()) {
				item.doFinish
						.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean ischecked) {
								int position = ((Integer) buttonView.getTag())
										.intValue();
								Task task = m_presenter.getSorter().getTask(position);
								if(ischecked == (task.status == Task.Status.finished)) return;
								if (ischecked) {
									m_presenter.finishTask((int)(task.taskFolderId),
											task);
									notifyDataSetChanged();

								} else {
									m_presenter.activateTask(
											(int)(task.taskFolderId), task);
									notifyDataSetChanged();

								}
							}
						});
			}
			String folderName = "";
			if(task.parentTaskId != Task.invalidId){
				try {
					TaskFolder parentFolder = m_presenter.getFolderByIndex((int)task.taskFolderId + 1);
					Task parent = parentFolder.findTask(task.parentTaskId);
					if(parent != null){
						folderName = " : "  + parent.getName();
					}
				} catch (ServiceException e) {
					folderName = "";
				}
			}
			if(task.expired.isEmpty()){
				item.name.setText(task.getName() + folderName);
			}else{
				item.name.setText(task.getName() + folderName + " - " + task.expired.substring(task.expired.indexOf(Task.DATESPLITTER) + 1));
			}
			item.name.setTag(task);
			if (!item.name.hasOnClickListeners()) {
				item.name.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Task updatedTask = (Task) arg0.getTag();
						if(updatedTask.status == Task.Status.ongoing){
							startTaskActivity(updatedTask);
						}
					}
				});
			}
			
			item.delete.setTag(task);
			if (!item.delete.hasOnClickListeners()) {
				item.delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Task task = (Task) arg0.getTag();
						m_presenter.delete((int)(task.taskFolderId), task, false);
						notifyDataSetChanged();
					}

				});
			}
			
			
			
		}
		
		public int getItemType(int position){
			return  m_presenter.getSorter().getItemType(position);
		}

		private String getClassName(int position){
			int index = m_presenter.getSorter().getClassIndex(position);
			return this.context.getString(res_keys[index]);
		}
		
		public void startTaskActivity(Task task){
			if(this.context instanceof TaskFolderActivity){
				TaskFolderActivity activity = (TaskFolderActivity)context;
				Intent intent = new Intent(this.context,TaskEditActivity.class);
				intent.putExtra(TaskFolderPresenter.KEY_TASK, new GsonHelper().jsonString(task));
				activity.startActivityForResult(intent,0);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}
}
