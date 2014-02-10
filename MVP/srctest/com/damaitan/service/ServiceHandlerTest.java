/**
 * 
 */
package com.damaitan.service;


import java.io.IOException;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.damaitan.exception.ServiceException;

/**
 * @author admin
 *
 */
public class ServiceHandlerTest extends TestCase{

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
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.damaitan.service.ServiceHandler#initialization(java.lang.String)}.
	 */
	@Test
	public void testInitialization() {
		
		try {
			String json = FileUtils.readFile("C:\\swtools\\JTD\\MVP\\srctest\\JTD.json");
			ServiceHandler.initialization(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}

}
