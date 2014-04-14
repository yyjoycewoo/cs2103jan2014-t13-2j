package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
import todomato.TaskList;
import todomato.UpdateProcessor;

//@author A0101324A
public class UpdateProcessorTest {

	private static final String TASK1 = "dotaing#01:00#02:00#null#null#utown#0#978601153#2014-04-10 00:33:20.173000000#LOW#false#null#2014-04-13 16:29:01.763000000#null\r\n";
	private static final String TASK2 = "Breakfast#07:00#null#2014-04-01#2014-04-01#null#0#1046042885#2014-04-09 16:19:17.842000000#LOW#true#null#null#null\r\n";
	private static final String TASK3 = "walk with dog and wash car#01:00#02:00#null#null#utown#0#978601153#null00:33:20.173000000#LOW#false#null#null16:29:01.763000000#null\r\n";
	private static final String STATUS_MSG = "Updated the task(s)";
	private static final String startEndTime = "1 starttime 7pm endtime 9pm";
	private static final String descLoc = "2 desc CS2103\" location com1\\";
	private static final String startEndDate = "3 startdate 11 apr enddate 12 apr";
	private static final String invalidkey = "1 rubbish";
	private static final String invalidIndex = "100";
	private static final String LOC = "utown";
	private static final String MULTIPLE_INDEX = "1,2,3 @utown";
	private static final String INDEX_OUT_OF_BOUND = "Index is out of the list.";
	private static final String NO_KEYWORDS_FOUND = "Please include any keywords to update i.e. starttime, endtime, location, desc, date";
	private static final String INVALID_DATE = "Start time cannot be greater than end time";
	private static final String INVALID_TIME = "Invalid Time";
	private static final String invalidDate = "1 startdate 12 apr enddate 11 apr";
	private static final String invalidTime = "2 starttime 2359pm endtime 7am";
	private static final String LIST = "list";
	private static final String FILE_DIR = "user.dir";
	private static final String FILE_NAME = "tasks.txt";
	private static final String FILE_HEADING = "null\r\nnull\r\nnull\r\n";

	private File tasks;

	// @author A0101578H
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Before
	public void createTestData() throws IOException, NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException {
		System.setProperty(FILE_DIR, folder.getRoot().toString());
		tasks = folder.newFile(FILE_NAME);
		BufferedWriter out = new BufferedWriter(new FileWriter(tasks));
		out.write(FILE_HEADING);
		out.write(TASK1);
		out.write(TASK2);
		out.write(TASK3);
		out.close();

		// To reset list to the tasks written before each test case
		// by modifying list in Processor
		FileHandler fileHandler = new FileHandler(FILE_NAME);
		TaskList newList = fileHandler.readFile();
		System.out.println(newList.getSize());
		Field f = Processor.class.getDeclaredField(LIST);
		f.setAccessible(true);
		if (LIST.equals(f.getName())) {
			f.setAccessible(true);
			f.set(LIST, newList);
		}
	}

	// @author A0101324A
	// testing the out of bound index
	@Test(expected = InvalidInputException.class)
	public void testInvalidIndexOne() throws InvalidInputException {
		String invalidIndex = "0";
		UpdateProcessor.processUpdate(invalidIndex);
	}

	// testing index out of the list's index
	@Test
	public void testExceedIndex() {
		try {
			UpdateProcessor.processUpdate(invalidIndex);
		} catch (InvalidInputException e) {
			assertEquals(INDEX_OUT_OF_BOUND, e.getMessage());
		}
	}

	// testing for invalid keyword
	@Test
	public void testInvalidKeyword() {
		try {
			UpdateProcessor.processUpdate(invalidkey);
		} catch (InvalidInputException e) {
			assertEquals(NO_KEYWORDS_FOUND, e.getMessage());
		}
	}

	// testing start and end time, desc and location, start and end date
	@Test
	public void testUpdateFeatures() throws InvalidInputException {
		String task1Msg = UpdateProcessor.processUpdate(startEndTime);
		String task2Msg = UpdateProcessor.processUpdate(descLoc);
		String task3Msg = UpdateProcessor.processUpdate(startEndDate);
		assertEquals(STATUS_MSG, task1Msg);
		assertEquals(STATUS_MSG, task2Msg);
		assertEquals(STATUS_MSG, task3Msg);
	}

	// testing for invalid date
	@Test
	public void testInvalidDate() throws InvalidInputException {
		try {
			UpdateProcessor.processUpdate(invalidDate);
			fail("Should have thrown invalid input");
		} catch (InvalidInputException e) {
			assertEquals(INVALID_DATE, e.getMessage());
		}
	}

	// testing for invalid time
	@Test
	public void testInvalidTime() throws InvalidInputException {
		try {
			UpdateProcessor.processUpdate(invalidTime);
			fail("Should have thrown invalid input");
		} catch (InvalidInputException e) {
			assertEquals(INVALID_TIME, e.getMessage());
		}
	}

	// testing for multiple updates
	@Test
	public void testMultipleIndex() throws InvalidInputException {
		String tasksMsg = UpdateProcessor.processUpdate(MULTIPLE_INDEX);
		assertEquals(STATUS_MSG, tasksMsg);
		for (int i = 0; i < Processor.getList().getSize(); i++) {
			assertEquals(LOC, Processor.getList().getListItem(i).getLocation());
		}
	}
}
