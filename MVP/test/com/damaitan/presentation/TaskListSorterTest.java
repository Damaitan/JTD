package com.damaitan.presentation;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.damaitan.datamodel.Task;
import com.damaitan.exception.ServiceException;

public class TaskListSorterTest {

	TaskFolderPresenter presenter; 
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MainViewPresenter mp = new MainViewPresenter();
		mp.initialization(mp.initJsonString());
		presenter = new TaskFolderPresenter(null);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTaskListSorter() {
		fail("Not yet implemented");
	}

	@Test
	public void testJudge() {
		fail("Not yet implemented");
	}

	@Test
	public void testClassifyData() {
		fail("Not yet implemented");
	}

	@Test
	public void testClearData() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemove() {
		fail("Not yet implemented");
	}

	@Test
	public void testAdd() {
		Task task1 = new Task();
		task1.setName("haha");
		Task task2 = new Task();
		task2.setName("ok");
		task2.urgent = true;
		try {
			presenter.saveTask(1, task1, true);
			presenter.saveTask(1, task2, true);
			TaskListSorter sort = new TaskListSorter();
			sort.classifyData(presenter.getFolderByIndex(1));
			sort.remove(task2);
			org.junit.Assert.assertEquals(sort.getCount(),2);
			task2.status = Task.Status.finished;
			sort.add(task2);
			org.junit.Assert.assertEquals(sort.getCount(),4);
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetTask() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetItemType() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetClassIndex() {
		fail("Not yet implemented");
	}

}
