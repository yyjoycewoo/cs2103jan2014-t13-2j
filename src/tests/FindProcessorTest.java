package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import todomato.FindProcessor;
import todomato.Processor;

public class FindProcessorTest {
	private static final String NO_TASKS_FOUND_MESSAGE = "No tasks found";
	private static final String SUCCESS_MSG = "Search completed";
	
	@Test
	public void testEmptyString() {
		//test boundary case of searching an empty string
		String command = "";
		assertEquals(FindProcessor.processFind(command), SUCCESS_MSG);
		String original_list = Processor.getList().toString();
		String found_list = Processor.getDisplayList().toString();
		assertEquals(original_list, found_list);	
	}
	
	@Test
	public void testNotFound() {
		//test case of a task that does not exist
		String command = "AOEUNTOHENTHAUC<#NPH#@P#@";
		assertEquals(FindProcessor.processFind(command), NO_TASKS_FOUND_MESSAGE);
		String found_list = Processor.getDisplayList().toString();
		assertEquals(found_list, "");	
	}
	
	@Test
	public void testFound() {
		//test case of a task that does exist
		String command = "CS2103";
		assertEquals(FindProcessor.processFind(command), SUCCESS_MSG);
		String found_list = Processor.getDisplayList().toString();
		String[] tasks = found_list.split("\n");
		for (String i : tasks) {
			assertTrue(i.contains(command));
		}
	}

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

}
