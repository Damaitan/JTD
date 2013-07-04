/**
 * 
 */
package com.damaitan.service;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.damaitan.exception.ServiceException;
import com.damaitan.service.AccessMock.MockType;

/**
 * @author admin
 *
 */
public class ModelManagerTest {

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
		manager = new ModelManager();
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
			manager.construct(accessMock);
		} catch (Exception e) {
			fail("Failed to test construction without exception");
		}
		
	}
	
	@Test(expected = ServiceException .class )
	public void testConstructException() throws Exception{
		accessMock.setType(MockType.exception);
		manager.construct(accessMock);
		
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
