/**
 * 
 */
package com.damaitan.mobileUI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.annotation.SuppressLint;
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
import com.damaitan.presentation.TaskListSorter;
import com.damaitan.service.GsonHelper;
import com.damaitan.service.ModelManager;

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
	private boolean mSync = false;
	
	
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
        presenter.getSorter().classifyData(_folder);
        m_adapter = new TaskFolderAdapter(this,_folderIndex, presenter);
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
	protected void onDestroy() {
		presenter.leave(this);
		if(mSync){
			try {
				JsonHelper.saveJsonStringToFile(this, ModelManager.getInstance().JsonString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mSync = false;
		}		
		super.onDestroy();
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
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == MENU_ID_NEW){
			Task task = new Task();
			task.taskFolderId = _folder.getId();
			m_adapter.startTaskActivity(task);
		}
        return true;
	}
	
	
	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo)item.getMenuInfo();
		Task task = presenter.getSorter().getTask(menuInfo.position);
		switch(item.getItemId()){
			case CONTEXTMENU_ID_EDIT:
				if(task != null){
					
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
		public final static int[] res_keys = new int[]{R.string.txt_task_folder_urgent,R.string.txt_task_folder_expired, R.string.txt_task_folder_day, R.string.txt_task_folder_week, R.string.txt_task_folder_month, R.string.txt_task_folder_other, R.string.txt_task_folder_finish};
		HashMap<String,TaskListInfo> tasks = null;
	    private LayoutInflater mLayoutInflater; 
	    private Context context;
    	public  int taskFolderIndex;
    	private TaskFolderPresenter m_presenter;
    	private Map<Integer, Integer> m_selects;
    	
	    public final static  class TaskFolderGridItem{
	    	public int type;
	    	public int position;
	    	public TextView className = null;
	    	public CheckBox doFinish = null;
	    	public TextView name = null;
	    	public TextView tag = null; 
	    	public Button date = null;
	    	
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
	    	m_selects = new HashMap<Integer, Integer>();
	    	initSelects();
	    }
	    
	    public void initSelects(){
	    	m_selects.clear();
	    	for(int i = 0; i < m_presenter.getSorter().getCount();i++){
	    		if(getItemViewType(i) == TaskListSorter.ITEM_TYPE_CLASS) continue;
	    		Task task = m_presenter.getSorter().getTask(i);
	    		if(task.status == Task.Status.finished){
	    			m_selects.put(i, i);
	    		}
	    	}
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
				item.position = position;
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
					if(item.type == TaskListSorter.ITEM_TYPE_CLASS && item.date == null){
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
			} else {
				taskToShow(position, item);
			}
			item.type = type;
			item.position = position;
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
				item.date = (Button) convertView
						.findViewById(R.id.task_folder_item_btn_date);
			}
		}
		
		private void taskToShow(int position , TaskFolderGridItem item) {
			Task task = (Task) getItem(position);
			Log.d("TaskFolderActivity taskToShow", position + "-" + task.getName());
			Log.d("TaskFolderActivity taskToShow item", item.name.getText().toString());
			item.name.setText(task.getName());
			item.name.setTag(String.valueOf(position));
			String tagPrefix = "";
			if (this.taskFolderIndex == 0) {
				TaskFolder folder = ModelManager.getInstance().getTaskFolder(
						(int) task.parentTaskId);
				if (folder != null) {
					tagPrefix = folder.getName() + " - ";
				}
			}
			item.tag.setText(tagPrefix + "(" + task.tags + ")");
			if (task.expired == null || task.expired.isEmpty()) {
				item.date.setText(R.string.txt_task_folder_nothing);
			} else {
				SimpleDateFormat df = new SimpleDateFormat("MM"
						+ Task.DATESPLITTER + "dd", Locale.getDefault());
				try {
					item.date
							.setText(df.parse(task.expired).toString());
				} catch (ParseException e) {
					item.date.setText(R.string.txt_task_folder_nothing);
				}
			}
			
			
			if (m_selects.containsKey(position))
				item.doFinish.setChecked(true);
			else
				item.doFinish.setChecked(false);
			item.doFinish.setTag(position);
			if (!item.doFinish.hasOnClickListeners()) {
				item.doFinish
						.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean ischecked) {
								int position = ((Integer) buttonView.getTag())
										.intValue();
								if (ischecked) {
									if (!m_selects.containsKey(buttonView
											.getTag())) {
										Task changedTask = (Task) getItem(position);
										m_selects.put(
												(Integer) buttonView.getTag(),
												position);
										m_presenter.finishTask(taskFolderIndex,
												changedTask);
									}

								} else {
									if (m_selects.containsKey(buttonView
											.getTag())) {
										Task changedTask = (Task) getItem(position);
										m_selects.remove((Integer) buttonView
												.getTag());
										m_presenter.activateTask(
												taskFolderIndex, changedTask);
									}

								}
							}
						});
			}
			((Activity) this.context).registerForContextMenu(item.name);
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

			if (!item.date.hasOnClickListeners()) {
				item.date.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {

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
			Intent intent = new Intent(this.context,TaskEditActivity.class);
			intent.putExtra(TaskFolderPresenter.KEY_TASK, new GsonHelper().jsonString(task));
			intent.putExtra(MainViewPresenter.Key_Index, taskFolderIndex);
			this.context.startActivity(intent);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	@Override
	public int act(Type actType, TaskFolder folder, Task oldTask, Task task){
		StringBuffer msg = new StringBuffer();
		msg.append(actType);
		msg.append("0:");
		msg.append(folder.getName());
		msg.append("1:");
		msg.append(oldTask.getName());
		msg.append("2:");
		msg.append(task.getName());;
		Log.d("TaskFolderActivity act", msg.toString());
		mSync = true;
		if(this._folderIndex != folder.getId()) return 0;
		if(actType == Type.add){
			presenter.getSorter().add(task);
		}else if(actType == Type.update){
			boolean updated = oldTask.tags.equalsIgnoreCase(task.tags) && oldTask.urgent == task.urgent && oldTask.expired.equalsIgnoreCase(task.expired);
			if(!updated){
				presenter.getSorter().remove(oldTask);
				presenter.getSorter().add(task);
			}
		}
		else {
			presenter.getSorter().remove(oldTask);
			if(actType == Type.finish || actType == Type.activate ){
				presenter.getSorter().add(task);
			}
		}
		m_adapter.initSelects();
		Log.d("TaskFolderActivity act count",String.valueOf(presenter.getSorter().getCount()));
		m_adapter.notifyDataSetChanged();
		return 0;
	}
}
