/**
 * 
 */
package com.damaitan.mobileUI;

import java.util.List;
import com.damaitan.datamodel.Task;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;



/**
 * @author admin
 *
 */
public class TaskFolderFragmentItemAdapter extends BaseAdapter {
	private Context context;                        //运行上下文   
    private List<Task> tasks;    //信息集合   
    private boolean finished;
    private LayoutInflater listContainer;           //视图容器                     //记录商品选中状态   
    public final class ListViewItem{                //自定义控件集合        
    	public CheckBox finish;
    	public TextView task;
     }  
    
    public TaskFolderFragmentItemAdapter(Context context, List<Task> tasks) {   
        this.context = context;            
        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文   
        this.tasks = tasks; 
        this.finished = false;
    }
    
    public TaskFolderFragmentItemAdapter(Context context, List<Task> tasks, boolean finished) {   
        this.context = context;            
        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文   
        this.tasks = tasks; 
        this.finished = finished;
    }

	@Override
	public int getCount() {
		return tasks.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("TaskFolderAdapter method", "getView");   
        //final int selectID = position;   
        //自定义视图   
        ListViewItem  listViewItem = null;   
        if (convertView == null) {   
        	listViewItem = new ListViewItem();    
            //获取list_item布局文件的视图   
            convertView = listContainer.inflate(R.layout.task_folder_item, null);   
            //获取控件对象   
            listViewItem.finish = (CheckBox)convertView.findViewById(R.id.task_folder_item_finishit);   
            listViewItem.task = (TextView)convertView.findViewById(R.id.task_folder_item_name);   
   
            //设置控件集到convertView   
           convertView.setTag(listViewItem);   
        }else {   
        	listViewItem = (ListViewItem)convertView.getTag();   
        }   

        //设置文字
        listViewItem.finish.setChecked(finished);
        listViewItem.task.setText(tasks.get(position).getName());
             
        //注册按钮点击时间事件
        /*listViewItem.detail.setOnClickListener(new View.OnClickListener() {   
            @Override  
            public void onClick(View v) {   
                //显示物品详情   
                showDetailInfo(selectID);   
            }   
        });*/
        // 注册多选框状态事件处理   
//        listViewItem.check   
//                .setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {   
//                    @Override  
//                    public void onCheckedChanged(CompoundButton buttonView,   
//                            boolean isChecked) {   
//                        //记录物品选中状态   
//                        checkedChange(selectID);   
//                    }   
//        });   
           
        return convertView; 
	}
	
	

}
