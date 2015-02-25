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
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSaveTask() {
		fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testFinishTask() {
		try {
			Task task = new Task();
			task.setName("haha");
			presenter.saveTask(1, task, true);
			presenter.finishTask(1, task);
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
