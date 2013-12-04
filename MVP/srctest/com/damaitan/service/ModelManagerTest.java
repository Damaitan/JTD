/**
 * 
 */
package com.damaitan.service;



import java.util.ArrayList;


import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.damaitan.datamodel.ModelStruct;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.ServiceException;
import com.damaitan.service.AccessMock.MockType;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author admin
 *
 */
public class ModelManagerTest extends TestCase{

	AccessMock accessMock = new AccessMock();
	ModelManager manager;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		manager = ModelManager.getInstance();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	

	/**
	 * Test method for {@link com.damaitan.service.ModelManager#construct(com.damaitan.access.IDataAccess)}.
	 */
	@Test
	public void testConstruct() {
		accessMock.setType(MockType.normal);
		try {
			ModelManager dm = ModelManager.getInstance();
			dm.construct(dm.initJsonString());
			//manager.construct(accessMock);
			String content[] = new String[]{"所有任务","待办事项","项目事务","短期目标","长期目标","愿景方向","六万英尺"};
			ArrayList<TaskFolder> folders = new ArrayList<TaskFolder>();
			for(int i = 0; i< content.length; i++){
				TaskFolder folder = new TaskFolder();
				folder.setId(i);
				folder.setName(content[i]);
				folders.add(folder);
			}
			
			
			/*String content2;
			try {
				//content2 = ModelJSON.ToJsonString(folders);
				//Assert.assertEquals(content2, content1);
			} catch (AccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			ModelStruct modelStruct = new ModelStruct();
			modelStruct.setFolders(folders);
			Gson gson = new GsonBuilder()  
	        //.excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性  
	        .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式  
	        .serializeNulls()
	        .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")//时间转化为特定格式    
	        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效.  
	        .setPrettyPrinting() //对json结果格式化.  
	        .setVersion(1.0)    //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.  
	                            //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么  
	                            //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.  
	        .create();  
			//Gson gson = new Gson();
			String content2 = gson.toJson(modelStruct);
			System.out.print(content2);
			
			//ModelStruct struct1 = gson.fromJson(content2, ModelStruct.class);
			//Assert.assertEquals(content2, content1);
			//GsonBuilder builder = new GsonBuilder();
			
			
		} catch (Exception e) {
			fail("Failed to test construction without exception");
		}
		
	}
	
	@Test(expected=ServiceException.class)
	public void testConstructException() throws Exception{
		//accessMock.setType(MockType.exception);
		//manager.construct(accessMock);
		
	}

	/**
	 * Test method for {@link com.damaitan.service.ModelManager#createFolder(java.lang.String)}.
	 */
	@Test
	public void testCreateFolder() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.damaitan.service.ModelManager#modifyFolder(int, java.lang.String)}.
	 */
	@Test
	public void testModifyFolder() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.damaitan.service.ModelManager#deleteFolder(int, boolean)}.
	 */
	@Test
	public void testDeleteFolder() {
		fail("Not yet implemented");
	}

}
