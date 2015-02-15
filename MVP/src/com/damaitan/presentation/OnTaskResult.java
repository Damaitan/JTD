/**
 * 
 */
package com.damaitan.presentation;

import java.util.ArrayList;

import com.damaitan.datamodel.Task;
import com.damaitan.datamodel.TaskFolder;

/**
 * @author admin
 *
 */
public class OnTaskResult {
	private ArrayList<ITaskListener> m_listeners = new ArrayList<ITaskListener>();
	private static OnTaskResult _instance;
	
	public interface ITaskListener {
		public enum Type {
			add, update, delete, finish,activate
		}

		public int act(Type type, TaskFolder folder, Task task);
	}
	
	private OnTaskResult(){
		
	}
	
	public static OnTaskResult getInstance(){
		if(_instance == null){
			_instance = new OnTaskResult();
		}
		return _instance;
	}
	
	public void join(ITaskListener listener){
		m_listeners.add(listener);
	}
	
	public void leave(ITaskListener listener){
		m_listeners.remove(listener);
	}
	
	public void inform(ITaskListener.Type type, TaskFolder folder, Task task){
		for(ITaskListener listener : m_listeners){
			if(listener != null) listener.act(type, folder, task);
		}
	}
}
