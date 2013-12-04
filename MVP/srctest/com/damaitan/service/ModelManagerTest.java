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
			String content[] = new String[]{"��������","��������","��Ŀ����","����Ŀ��","����Ŀ��","Ը������","����Ӣ��"};
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
	        //.excludeFieldsWithoutExposeAnnotation() //������ʵ����û����@Exposeע�������  
	        .enableComplexMapKeySerialization() //֧��Map��keyΪ���Ӷ������ʽ  
	        .serializeNulls()
	        .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")//ʱ��ת��Ϊ�ض���ʽ    
	        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//����ֶ�����ĸ��д,ע:����ʵ����ʹ����@SerializedNameע��Ĳ�����Ч.  
	        .setPrettyPrinting() //��json�����ʽ��.  
	        .setVersion(1.0)    //�е��ֶβ���һ��ʼ���е�,�����Ű汾��������ӽ���,��ô�ڽ������л��ͷ����л���ʱ��ͻ���ݰ汾����ѡ���Ƿ�Ҫ���л�.  
	                            //@Since(�汾��)��������ʵ���������.�����ֶο���,���Ű汾��������ɾ��,��ô  
	                            //@Until(�汾��)Ҳ��ʵ���������,GsonBuilder.setVersion(double)������Ҫ����.  
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
