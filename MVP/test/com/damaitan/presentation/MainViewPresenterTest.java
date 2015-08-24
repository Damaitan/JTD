package com.damaitan.presentation;

import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.damaitan.datamodel.Task;
import com.damaitan.exception.PresentationException;
import com.damaitan.exception.ServiceException;
import com.damaitan.presentation.MainViewPresenter;

public class MainViewPresenterTest {

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

	@SuppressWarnings({ "unused", "deprecation" })
	@Test
	public void test_initialization() {
		
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		String test = calendar.toString();
		test = calendar.getTime().toLocaleString();
		Date date3 = calendar.getTime();
		long a1 = calendar.getTimeInMillis();//xianzai
		
		
		calendar.set(Calendar.YEAR, 2015);
		calendar.set(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 12);
		test = calendar.toString();
		test = calendar.getTime().toString();
		test = calendar.getTime().toGMTString();
		test = calendar.getTime().toLocaleString();
		long a2 = calendar.getTimeInMillis(); //guoqu
		long day = (a1 - a2)/(1000*60*60*24);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy" + Task.DATESPLITTER + "MM" + Task.DATESPLITTER + "dd",  Locale.CHINA);
		test = df.format(calendar.getTime());
		int weeks = calendar.get(Calendar.WEEK_OF_YEAR);
		df = new SimpleDateFormat("MM" + Task.DATESPLITTER + "dd",  Locale.CHINA);
		test = df.format(calendar.getTime());
		
		Date date1 = calendar.getTime();//guoqu
		Date date2 = new Date();//xianzai
		
		int diff = date1.getDay() - date2.getDay();
		diff = date1.getDay() - date3.getDay();
		day = (date1.getTime() - date2.getTime())/(1000*60*60*24);
		
		//Date date1 = new Date();
		//date1.
		
		MainViewPresenter presenter = new MainViewPresenter();
		try {
			presenter.initialization(presenter.initJsonString());
		} catch (PresentationException e) {
			fail();
		}
		List<Map<String, Object>> m_listData = new ArrayList<Map<String, Object>>();
		presenter.getData(m_listData);
		org.junit.Assert.assertEquals(m_listData.size(), 7);
	}
	
	@Test
	public void test_cleanFinished(){
		MainViewPresenter mp = new MainViewPresenter();
		try {
			mp.initialization(mp.initJsonString());
		} catch (PresentationException e) {
			fail();
		}
		TaskFolderPresenter presenter = new TaskFolderPresenter(false);
		Task task1 = new Task();
		Task task2 = new Task();
		task1.setName("haha");
		task2.setName("yeah");
		task1.priority = 0;
		task2.priority = 0;
		try {
			presenter.saveTask(1, task1, true);
			presenter.saveTask(1, task2, true);
			presenter.finishTask(1, task1);
			mp.cleanFinished();
			org.junit.Assert.assertEquals(1,presenter.getFolderByIndex(1).getCompletedTaskNumber());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
