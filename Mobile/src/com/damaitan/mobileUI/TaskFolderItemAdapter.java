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
public class TaskFolderItemAdapter extends BaseAdapter {
	private Context context;                        //����������   
    private List<Task> tasks;    //��Ϣ����   
    private boolean finished;
    private LayoutInflater listContainer;           //��ͼ����                     //��¼��Ʒѡ��״̬   
    public final class ListViewItem{                //�Զ���ؼ�����        
    	public CheckBox finish;
    	public TextView task;
     }  
    
    public TaskFolderItemAdapter(Context context, List<Task> tasks) {   
        this.context = context;            
        listContainer = LayoutInflater.from(context);   //������ͼ����������������   
        this.tasks = tasks; 
        this.finished = false;
    }
    
    public TaskFolderItemAdapter(Context context, List<Task> tasks, boolean finished) {   
        this.context = context;            
        listContainer = LayoutInflater.from(context);   //������ͼ����������������   
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
        //�Զ�����ͼ   
        ListViewItem  listViewItem = null;   
        if (convertView == null) {   
        	listViewItem = new ListViewItem();    
            //��ȡlist_item�����ļ�����ͼ   
            convertView = listContainer.inflate(R.layout.task_folder_item, null);   
            //��ȡ�ؼ�����   
            listViewItem.finish = (CheckBox)convertView.findViewById(R.id.task_folder_item_finishit);   
            listViewItem.task = (TextView)convertView.findViewById(R.id.task_folder_item_name);   
   
            //���ÿؼ�����convertView   
           convertView.setTag(listViewItem);   
        }else {   
        	listViewItem = (ListViewItem)convertView.getTag();   
        }   

        //��������
        listViewItem.finish.setChecked(finished);
        listViewItem.task.setText(tasks.get(position).getName());
             
        //ע�ᰴť���ʱ���¼�
        /*listViewItem.detail.setOnClickListener(new View.OnClickListener() {   
            @Override  
            public void onClick(View v) {   
                //��ʾ��Ʒ����   
                showDetailInfo(selectID);   
            }   
        });*/
        // ע���ѡ��״̬�¼�����   
//        listViewItem.check   
//                .setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {   
//                    @Override  
//                    public void onCheckedChanged(CompoundButton buttonView,   
//                            boolean isChecked) {   
//                        //��¼��Ʒѡ��״̬   
//                        checkedChange(selectID);   
//                    }   
//        });   
           
        return convertView; 
	}
	
	

}