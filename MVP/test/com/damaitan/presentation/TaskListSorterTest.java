package com.damaitan.presentation;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

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
		presenter = new TaskFolderPresenter(true);
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
		Task task1 = new Task();
		task1.setName("haha");
		task1.urgent = true;
		Task task2 = new Task();
		task2.setName("ok");
		try {
			presenter.saveTask(1, task1, true);
			presenter.saveTask(1, task2, true);
			TaskListSorter sort = new TaskListSorter();
			//sort.classifyData(presenter.getFolderByIndex(1));
			sort.remove(task1);
			org.junit.Assert.assertEquals(sort.getCount(),2);
			int key = sort.judge(task1);
			org.junit.Assert.assertEquals(1,key);
			org.junit.Assert.assertEquals(-1,sort.get(key).start_position);
			org.junit.Assert.assertEquals(0,sort.get(key).tasks.size());
			
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
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
			//sort.classifyData(presenter.getFolderByIndex(1));
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
		SimpleDateFormat df = TaskListSorter.dateFormat();
		try {
			Date exDate = df.parse("2015/2/1"); //过去
			Date date = new Date();   //现在
			long msNow = date.getTime(); //现在
			long msEx = exDate.getTime();    //过去
			long n = 0;
			if(msEx < msNow){
				n = (int) ((msNow - msEx)/(24*60*60*1000*7));
			}
			long temp = (n + 1) *7*24*60*60*1000;
			date.setTime(msEx + temp);
			String result = df.format(date);
			org.junit.Assert.assertEquals(false,result.isEmpty());
		} catch (Exception e) {
			fail();
		} 
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
	
	
	@Test
	public void testInit() {
		try {
			presenter.getSorter().init(presenter.getFolderByIndex(1));
		} catch (ServiceException e) {
			fail();
		}
	}

}
