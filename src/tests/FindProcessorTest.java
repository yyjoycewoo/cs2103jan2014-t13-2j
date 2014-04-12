package tests;
//@author A0120766H
import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import todomato.FileHandler;
import todomato.FindProcessor;
import todomato.Processor;
import todomato.TaskList;

public class FindProcessorTest {
	private static final String NO_TASKS_FOUND_MESSAGE = "No tasks found";
	private static final String SUCCESS_MSG = "Search completed";
	
	private File tasks;
	
	@Rule 
	public TemporaryFolder folder= new TemporaryFolder();

	@Before
	public void createTestData() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		System.setProperty("user.dir", folder.getRoot().toString());
		tasks = folder.newFile("tasks.txt");
		BufferedWriter out = new BufferedWriter(new FileWriter(tasks));
		out.write("null\r\nnull\r\nnull\r\n");
        out.write("CS2103 Tutorial#13:00#null#2014-04-10#2014-04-10#Home#0#395871680#2014-04-09 20:27:24.669000000#LOW#false#null#null#null\r\n");
        out.write("ES1531 Exam Prep#null#null#null#2014-04-23#Home#0#-1379251807#2014-04-09 20:27:29.951000000#HIGH#false#null#null#null\r\n");
        out.write("CS2103 Presentation#13:00#null#2014-04-17#2014-04-17#null#0#1463178207#2014-04-09 20:27:54.869000000#LOW#false#null#null#null\r\n");
        out.close();
        
        // To reset list to the tasks written ^ before each test case
        // by modifying list in Processor
        FileHandler fileHandler = new FileHandler("tasks.txt");
        TaskList newList = fileHandler.readFile();
        Field f = Processor.class.getDeclaredField("list");
        f.setAccessible(true);
        if ("list".equals(f.getName())) {
            f.setAccessible(true);
            f.set("list", newList);
        }
	}
	
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
			assertTrue(i.contains(command.toUpperCase()));
		}
	}
	
	@Test
	public void testFound1() {
		/*
		 * test case of a task that does exist, but in different cases
		 * (lowercase instead of upper case)
		 */
		String command = "cs2103";
		assertEquals(FindProcessor.processFind(command), SUCCESS_MSG);
		String found_list = Processor.getDisplayList().toString();
		String[] tasks = found_list.split("\n");
		for (String i : tasks) {
			assertTrue(i.contains(command.toUpperCase()));
		}
	}

}
