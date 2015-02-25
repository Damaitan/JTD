package com.damaitan.presentation;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.damaitan.exception.PresentationException;
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

	@Test
	public void test_initialization() {
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

}
