package tests;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import todomato.FileHandler;
import todomato.InvalidInputException;
import todomato.Processor;
import todomato.SortProcessor;
import todomato.TaskList;

//@author A0101578H
public class SortProcessorTest {
	private  File tasks;
	private static final String ITEM_ONE = "Lunch at 12:00 on May 09 2014\r\n";
	private static final String ITEM_TWO = "Orientation camp at 18:00 on Apr 09 2014 until 13:00 on Apr 14 2014\r\n";
	private static final String ITEM_THREE = "Project meeting at 08:00 on Apr 01 2014 until 14:00\r\n";
	private static final String ITEM_FOUR = "Breakfast at 07:00 on Apr 01 2014\r\n";
	private static final String ITEM_FIVE = "AA1234 Homework 7 on Apr 14 2014\r\n";
	
	@Rule 
	public  TemporaryFolder folder= new TemporaryFolder();
	
	@Before
	public void createTestData() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		System.setProperty("user.dir", folder.getRoot().toString());
		tasks = folder.newFile("tasks.txt");
		BufferedWriter out = new BufferedWriter(new FileWriter(tasks));
		out.write("null\r\n");
        out.write("null\r\n");
        out.write("null\r\n");
        out.write("Lunch#12:00#null#2014-05-09#2014-05-09#null#0#1046042885#2014-04-07 16:19:17.842000000#LOW#false#null#null#null\r\n");
        out.write("Orientation camp#18:00#13:00#2014-04-09#2014-04-14#null#0#-1351579072#2014-04-09 16:18:25.784000000#LOW#false#null#null#null\r\n");
        out.write("Project meeting#08:00#14:00#2014-04-01#2014-04-01#null#0#570051783#2014-04-09 16:18:48.669000000#HIGH#false#null#null#null\r\n");
        out.write("Breakfast#07:00#null#2014-04-01#2014-04-01#null#0#1046042885#2014-04-09 16:19:17.842000000#LOW#true#null#null#null\r\n");
        out.write("AA1234 Homework 7#null#null#2014-04-14#2014-04-14#null#0#-1351579072#2014-04-09 16:18:25.784000000#MEDIUM#false#null#null#null\r\n");
        out.close();
        
        FileHandler fileHandler = new FileHandler("tasks.txt");
        TaskList newList = fileHandler.readFile();
        Field f = Processor.class.getDeclaredField("list");
        f.setAccessible(true);
        if ("list".equals(f.getName())) {
            f.setAccessible(true);
            f.set("list", newList);
        }
	}
	
	public static TaskList getList() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = Processor.class.getDeclaredField("list");
        f.setAccessible(true);
        if ("list".equals(f.getName())) {
            f.setAccessible(true);
            return (TaskList) f.get("list");
        }
		return null;
	}
	
	@Test
	public void testSortStartDate() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InvalidInputException {
		String expectedList =  
				"1: " + ITEM_THREE +
				"2: " + ITEM_TWO +
				"3: " + ITEM_FIVE +
				"4: " + ITEM_ONE +
				"5: " + ITEM_FOUR;
		SortProcessor.processSort("startdate");
		String resultList = getList().toString();
		assertEquals(expectedList, resultList);
	}
	
	@Test
	public void testSortStartDateAscending() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InvalidInputException {
		String expectedList =  
				"1: " + ITEM_THREE +
				"2: " + ITEM_TWO +
				"3: " + ITEM_FIVE +
				"4: " + ITEM_ONE +
				"5: " + ITEM_FOUR;
		SortProcessor.processSort("startdate a");
		String resultList = getList().toString();
		assertEquals(expectedList, resultList);
	}
	
	@Test
	public void testSortStartDateDescending() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InvalidInputException {
		String expectedList = 
				"1: " + ITEM_FOUR +
				"2: " + ITEM_ONE + 
				"3: " + ITEM_FIVE + 
				"4: " + ITEM_TWO +
				"5: " + ITEM_THREE;
		SortProcessor.processSort("startdate d");
		String resultList = getList().toString();
		assertEquals(expectedList, resultList);
	}
	
	@Test
	public void testSortEndDate() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InvalidInputException {
		String expectedList = 
				"1: " + ITEM_THREE + 
				"2: " + ITEM_TWO + 
				"3: " + ITEM_FIVE +
				"4: " + ITEM_ONE +
				"5: " + ITEM_FOUR;
		SortProcessor.processSort("enddate");
		String resultList = getList().toString();
		assertEquals(expectedList, resultList);
	}
	
	@Test
	public void testSortEndDateAscending() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InvalidInputException {
		String expectedList = 
				"1: " + ITEM_THREE + 
				"2: " + ITEM_TWO + 
				"3: " + ITEM_FIVE +
				"4: " + ITEM_ONE +
				"5: " + ITEM_FOUR;
		SortProcessor.processSort("enddate a");
		String resultList = getList().toString();
		assertEquals(expectedList, resultList);
	}
	
	@Test
	public void testSortEndDateDescending() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InvalidInputException {
		String expectedList = 
				"1: " + ITEM_FOUR + 
				"2: " + ITEM_ONE + 
				"3: " + ITEM_FIVE +
				"4: " + ITEM_TWO +
				"5: " + ITEM_THREE;
		SortProcessor.processSort("enddate d");
		String resultList = getList().toString();
		assertEquals(expectedList, resultList);
	}
	
	@Test
	public void testSortComplete() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InvalidInputException {
		String expectedList = 
				"1: " + ITEM_ONE + 
				"2: " + ITEM_TWO +
				"3: " + ITEM_THREE +
				"4: " + ITEM_FIVE +
				"5: " + ITEM_FOUR;
		SortProcessor.processSort("complete");
		String resultList = getList().toString();
		assertEquals(expectedList, resultList);
	}
	
	@Test
	public void testSortCompleteAscending() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InvalidInputException {
		String expectedList = 
				"1: " + ITEM_ONE + 
				"2: " + ITEM_TWO +
				"3: " + ITEM_THREE +
				"4: " + ITEM_FIVE +
				"5: " + ITEM_FOUR;
		SortProcessor.processSort("complete a");
		String resultList = getList().toString();
		assertEquals(expectedList, resultList);
	}
	
	@Test
	public void testSortCompleteDescending() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InvalidInputException {
		String expectedList = 
				"1: " + ITEM_FOUR + 
				"2: " + ITEM_FIVE +
				"3: " + ITEM_THREE +
				"4: " + ITEM_TWO +
				"5: " + ITEM_ONE;
		SortProcessor.processSort("complete d");
		String resultList = getList().toString();
		assertEquals(expectedList, resultList);
	}
	
	@Test
	public void testSortPriority() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InvalidInputException {
		String expectedList = 
				"1: " + ITEM_THREE + 
				"2: " + ITEM_FIVE +
				"3: " + ITEM_ONE +
				"4: " + ITEM_TWO +
				"5: " + ITEM_FOUR;
		SortProcessor.processSort("priority");
		String resultList = getList().toString();
		assertEquals(expectedList, resultList);
	}
	
	@Test
	public void testSortPriorityAscending() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InvalidInputException {
		String expectedList = 
				"1: " + ITEM_FOUR + 
				"2: " + ITEM_TWO +
				"3: " + ITEM_ONE +
				"4: " + ITEM_FIVE +
				"5: " + ITEM_THREE;
		SortProcessor.processSort("priority a");
		String resultList = getList().toString();
		assertEquals(expectedList, resultList);
	}
	
	@Test
	public void testSortPriorityDescending() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InvalidInputException {
		String expectedList = 
				"1: " + ITEM_THREE + 
				"2: " + ITEM_FIVE +
				"3: " + ITEM_ONE +
				"4: " + ITEM_TWO +
				"5: " + ITEM_FOUR;
		SortProcessor.processSort("priority d");
		String resultList = getList().toString();
		assertEquals(expectedList, resultList);
	}
	
	public void testMultipleSortPriority() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InvalidInputException {
		String expectedList = 
				"1: " + ITEM_THREE + 
				"2: " + ITEM_FIVE +
				"3: " + ITEM_ONE +
				"4: " + ITEM_TWO +
				"5: " + ITEM_FOUR;
		SortProcessor.processSort("priority");
		SortProcessor.processSort("priority a");
		SortProcessor.processSort("priority d");
		String resultList = getList().toString();
		assertEquals(expectedList, resultList);
	}
	
	@Test
	public void testMissingArgument() throws InvalidInputException {
		try {
			SortProcessor.processSort("");
			fail("Should have thrown InvalidInputException");
		} catch (InvalidInputException e){
			assertEquals(e.getMessage(), "Missing argument");
		}
	}
	
	@Test
	public void testInvalidSortType() throws InvalidInputException {
		try {
			SortProcessor.processSort("starryday");
			fail("Should have thrown InvalidInputException");
		} catch (InvalidInputException e){
			assertEquals(e.getMessage(), "Sorting type could not be determined");
		}
	}
	
	@Test
	public void testInvalidSortOrder() throws InvalidInputException {
		try {
			SortProcessor.processSort("priority ass");
			fail("Should have thrown InvalidInputException");
		} catch (InvalidInputException e){
			assertEquals(e.getMessage(), "Sorting order could not be determined");
		}
	}
}
