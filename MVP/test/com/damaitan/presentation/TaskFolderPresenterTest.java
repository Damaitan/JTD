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
import com.damaitan.presentation.OnTaskResult.ITaskListener;
import com.damaitan.presentation.TaskFolderPresenter;

public class TaskFolderPresenterTest {
	TaskFolderPresenter presenter; 
	TaskListenerTest testListener;

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
		testListener = new TaskListenerTest();
		presenter = new TaskFolderPresenter(testListener);
		testListener.presenter = presenter;
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSaveTask() {
		
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
			org.junit.Assert.assertEquals(3,this.testListener.count);
			String key = presenter.getSorter().judge(task1);
			org.junit.Assert.assertEquals("urgent",key);
			org.junit.Assert.assertEquals(-1,presenter.getSorter().get(key).start_position);
			org.junit.Assert.assertEquals(0,presenter.getSorter().get(key).tasks.size());
			String key1 = presenter.getSorter().judge(testListener.testOld);
			org.junit.Assert.assertEquals(key,key1);
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
			presenter.saveTask(1, task1, true);
			presenter.saveTask(1, task2, true);
			org.junit.Assert.assertEquals(task1.getId(), presenter.getSorter().getTask(1).getId());
			org.junit.Assert.assertEquals(task2.getId(), presenter.getSorter().getTask(3).getId());
			presenter.finishTask(1, task1);
			org.junit.Assert.assertEquals(task2.getId(), presenter.getSorter().getTask(1).getId());
			org.junit.Assert.assertEquals(task1.getId(), presenter.getSorter().getTask(3).getId());
			org.junit.Assert.assertEquals(ITaskListener.Type.finish, testListener.trigger);
			org.junit.Assert.assertEquals(testListener.testOld.status, Task.Status.ongoing);
			org.junit.Assert.assertEquals(testListener.testTask.status, Task.Status.finished);
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
			org.junit.Assert.assertEquals(ITaskListener.Type.activate, testListener.trigger);
			org.junit.Assert.assertEquals(testListener.testOld.status, Task.Status.finished);
			org.junit.Assert.assertEquals(testListener.testTask.status, Task.Status.ongoing);
		} catch (ServiceException e) {
			fail();
		}
		
	}

}
