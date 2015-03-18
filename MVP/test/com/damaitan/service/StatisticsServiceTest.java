package com.damaitan.service;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.damaitan.datamodel.Task;
import com.damaitan.presentation.MainViewPresenter;
import com.damaitan.presentation.TaskFolderPresenter;

public class StatisticsServiceTest {
	
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
		presenter.getSorter().init(presenter.getFolderByIndex(1));
		for(int i = 0; i < 20;i++){
			Task task = new Task();
			task.setName(String.valueOf(i));
			presenter.saveTask(1, task, true);
			if( i > 15){
				presenter.finishTask(1, task);
			}
		}
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInitArrayListOfTaskFolder() {
		StatisticsService.getInstance().init(ModelManager.getInstance().getFolders());
		String csv = StatisticsService.getInstance().getCSV();
		org.junit.Assert.assertTrue(csv.isEmpty());
		
	}

	@Test
	public void testInitString() {
		fail("Not yet implemented");
	}

	@Test
	public void testAct() {
		fail("Not yet implemented");
	}

	@Test
	public void testJsonString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCSV() {
		fail("Not yet implemented");
	}

}
