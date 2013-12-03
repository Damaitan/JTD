/**
 * 
 */
package com.damaitan.service;

import java.util.ArrayList;

import com.damaitan.datamodel.Task;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.ServiceException;
import com.damaitan.datamodel.ModelStruct;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author admin
 *
 */
final class ModelManager{
	Gson gson = null;
	private ModelStruct modelStruct = new ModelStruct();
	private ArrayList<Task> allTasks = new ArrayList<Task>(); //Tasks are got from all task folders.
	private ArrayList<String> tags = new ArrayList<String>(); // Tags are got from all taks tags
	private long taskId = 0;
	private long taskFolderId = 0;
	
	private static ModelManager uniqueInstance = null;
	
	private ModelManager(){
		gson = new GsonBuilder()
		// .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性
				.enableComplexMapKeySerialization() // 支持Map的key为复杂对象的形式
				.serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")// 时间转化为特定格式
				.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)// 会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效.
				.setPrettyPrinting() // 对json结果格式化.
				.setVersion(1.0) // 有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.
									// @Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么
									// @Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.
				.create();
		
	}
	public static ModelManager getInstance() {
		 if (uniqueInstance == null) {
			 uniqueInstance = new ModelManager();
		 }
		 return uniqueInstance;
	}

	public Gson getGson() {
		
		return gson;
	}
	
	public String initJsonString(){
		String content[] = new String[]{"所有任务","待办事项","项目事务","短期目标","长期目标","愿景方向","六万英尺","未来清单"};
		ArrayList<TaskFolder> folders = new ArrayList<TaskFolder>();
		for(int i = 0; i< content.length; i++){
			TaskFolder folder = new TaskFolder();
			folder.setId(i);
			folder.setName(content[i]);
			folders.add(folder);
		}
		ModelStruct modelStruct = new ModelStruct();
		modelStruct.setFolders(folders);
		return getGson().toJson(modelStruct);
	}
	
	public String jsonString(){
		return getGson().toJson(modelStruct);
	}
	
	public ArrayList<TaskFolder> getFolders(){
		return modelStruct.getFolders();

	}
	 
	
	
	private void tag(Task task){
		if(task.getTags() == null)
			return;
		for(String item : task.getTags()){
			if(!tags.contains(item)){
				tags.add(item);
			}
		}
	}

	
	public void construct(String json) throws ServiceException {
		Gson gson = getGson();
		this.modelStruct = gson.fromJson(json, ModelStruct.class);
		for(TaskFolder item : modelStruct.getFolders()){
			if(item.getTasks() != null){
				allTasks.addAll(item.getTasks());
			}
			if(item.getId() > taskFolderId){
				taskFolderId = item.getId();
			}
		}
		for(Task item : allTasks){
			tag(item);
			if(item.getId() > taskId){
				taskId = item.getId();
			}
		}
	}
	
	public TaskFolder getTaskFolder(int index){
		
		return modelStruct.getFolders().get(index);
	}
	
	public long getNewTaskId(boolean updateId){
		if(updateId){
			taskId++;
			return taskId;
		}
		return taskId+1;
	}
	
	

	/*
	private TaskFolder findFolderById(long id){
		if(this.taskFolders == null) return null;
		for(int i = 0; i < this.taskFolders.size();i++){
			if(this.taskFolders.get(i).getId() == id){
				return this.taskFolders.get(i);
			}
		}
		return null;
	}
	
	private int findFolderIndexById(long id){
		if(this.taskFolders == null) return -1;
		for(int i = 0; i < this.taskFolders.size();i++){
			if(this.taskFolders.get(i).getId() == id){
				return i;
			}
		}
		return -1;
	}

	@Override
	public TaskFolder createFolder(String name) throws ServiceException {
		taskFolderId++;
		TaskFolder folder = new TaskFolder(taskFolderId, name);
		taskFolders.add(folder);
		return folder;
	}

	@Override
	public TaskFolder modifyFolder(long id, String newName)
			throws ServiceException {
		TaskFolder folder = findFolderById(id);
		if(folder != null){
			folder.setName(newName);
		}
		return folder;
	}

	@Override
	public TaskFolder deleteFolder(long id, boolean includeTask)
			throws ServiceException {
		int index = findFolderIndexById(id);
		TaskFolder folder = this.taskFolders.get(index);
		if(index != -1){
			this.taskFolders.remove(index);
		}
		if(includeTask){
			//Not finished yet
		}else{
			
		}
		return folder;
	}*/
}
