package com.damaitan.presentation;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.damaitan.datamodel.Task;
import com.damaitan.exception.ServiceException;
import com.damaitan.presentation.MainViewPresenter;
import com.damaitan.presentation.TaskFolderPresenter;

public class TaskFolderPresenterTest {
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
		//testListener = new TaskListenerTest();
		presenter = new TaskFolderPresenter(true);
		presenter.getSorter().init(presenter.getFolderByIndex(1));
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSaveTask() {
		Task task1 = new Task();
		task1.setName("haha");
		//task1.urgent = true;
		task1.priority = 0;
		try {
			presenter.saveTask(1, task1, true);
			int key1 = presenter.getSorter().judge(task1);
			//task1.priority = 1;
			task1.tags = "year";
			presenter.saveTask(1, task1, false);
			org.junit.Assert.assertEquals(1, presenter.getFolderByIndex(1).getTasks().size());
			org.junit.Assert.assertEquals(6,key1);
			//org.junit.Assert.assertEquals(-1,presenter.getSorter().get(key).start_position);
			//org.junit.Assert.assertEquals(0,presenter.getSorter().get(key).tasks.size());
			org.junit.Assert.assertEquals(2,presenter.getSorter().getCount());
			/*task1.urgent = true;
			presenter.saveTask(1, task1, false);
			key1 = presenter.getSorter().judge(task1);
			org.junit.Assert.assertEquals(1, presenter.getFolderByIndex(1).getTasks().size());
			org.junit.Assert.assertEquals("urgent",key1);
			org.junit.Assert.assertEquals(2,presenter.getSorter().getCount());*/
			
			
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
		
	}

	@Test
	public void testDelete() {
		Task task1 = new Task();
		task1.setName("haha");
		task1.urgent = true;
		Task task2 = new Task();
		task2.setName("ok");
		try {
			presenter.saveTask(1, task1, true);
			presenter.saveTask(1, task2, true);
			presenter.delete(1, task1, true);
			int key = presenter.getSorter().judge(task1);
			org.junit.Assert.assertEquals("urgent",key);
			org.junit.Assert.assertEquals(-1,presenter.getSorter().get(key).start_position);
			org.junit.Assert.assertEquals(0,presenter.getSorter().get(key).tasks.size());
			org.junit.Assert.assertEquals(2,presenter.getSorter().getCount());
			
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testFinishTask() {
		try {
			Task task1 = new Task();
			task1.setName("haha");
			task1.urgent = true;
			Task task2 = new Task();
			task2.setName("ok");
			//task2.expired = 2015 + Task.DATESPLITTER + 1 + Task.DATESPLITTER + 3;
			//task2.repeat = true;
			//task2.repeat_proid = 7;
			task2.urgent = true;
			presenter.saveTask(1, task1, true);
			presenter.saveTask(1, task2, true);
			org.junit.Assert.assertEquals(task1.getId(), presenter.getSorter().getTask(1).getId());
			org.junit.Assert.assertEquals(task2.getId(), presenter.getSorter().getTask(2).getId());
			org.junit.Assert.assertEquals(3, presenter.getSorter().getCount());
			presenter.finishTask(1, task1);
			org.junit.Assert.assertEquals(task2.getId(), presenter.getSorter().getTask(1).getId());
			org.junit.Assert.assertEquals(task1.getId(), presenter.getSorter().getTask(3).getId());
			org.junit.Assert.assertEquals(4, presenter.getSorter().getCount());
			//presenter.finishTask(1, task2);
			
		} catch (ServiceException e) {
			fail();
		}
		
	}

	@Test
	public void testActivateTask() {
		Task task = new Task();
		task.setName("haha");
		try {
			presenter.saveTask(1, task, true);
			presenter.finishTask(1, task);
			presenter.activateTask(1, task);
		} catch (ServiceException e) {
			fail();
		}
		
	}
	
	@Test
	public void testSaveTask_Expired() {
		Task task = new Task();
		task.setName("haha");
		task.expired = "2015/3/20";
		try {
			presenter.saveTask(1, task, true);
			org.junit.Assert.assertEquals(3, presenter.getSorter().judge(task));
			org.junit.Assert.assertNotNull(presenter.getSorter().getTask(1));
			org.junit.Assert.assertSame(presenter.getSorter().getTaskKey(1), "week");
		} catch (ServiceException e) {
			fail();
		}
		
	}
	
	@Test
	public void testFinishTask_Repeat() {
		Task task = new Task();
		task.setName("haha");
		task.repeat = true;
		task.repeat_proid = 2;
		task.expired = "2015/3/16";
		try {
			presenter.saveTask(1, task, true);
			presenter.finishTask(1, task);
			org.junit.Assert.assertEquals(4, presenter.getSorter().getCount());
			org.junit.Assert.assertEquals(presenter.getSorter().getTask(1).expired, "2015/03/18");
			org.junit.Assert.assertSame("finish",presenter.getSorter().getTaskKey(3));
		} catch (ServiceException e) {
			fail();
		}
		
	}
	
	@Test
	public void testChangeTaskFolder() {
		Task task = new Task();
		task.setName("haha");
		try {
			presenter.saveTask(1, task, true);
			task.taskFolderId = task.taskFolderId + 1;
			presenter.saveTask(1, task, false);
			org.junit.Assert.assertEquals(0, presenter.getSorter().getCount());
			org.junit.Assert.assertEquals(0,presenter.getFolderByIndex(1).getTasks().size());
		} catch (ServiceException e) {
			fail();
		}
		
	}
	
	

}
