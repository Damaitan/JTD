package com.damaitan.datamodel;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TaskTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testClone() {
		fail("Not yet implemented");
	}

	@Test
	public void testEqualsObject() {
		Task task1 = new Task();
		task1.setId(1);
		task1.expired = "2015/2/12";
		task1.note = "";
		task1.priority = 0;
		task1.urgent = false;
		Task task2 = task1.clone();
		org.junit.Assert.assertEquals(task1,task2);
		task1.note = " ";
		org.junit.Assert.assertNotEquals(task1,task2);
	}

}
