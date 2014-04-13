/**
 * 
 */
package tests;

import static org.junit.Assert.assertEquals;

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

public class UpdateProcessorTest {

	private static final String TASK1 = "dotaing#01:00#02:00#null#null#utown#0#978601153#2014-04-10 00:33:20.173000000#LOW#false#null#2014-04-13 16:29:01.763000000#null";
	private static final String TASK2 = "cs2103 testcases#null#null#null#2014-04-11#haha#0#-695569587#2014-04-10 13:00:20.931000000#LOW#false#null#2014-04-13 16:27:42.395000000#null";
	private static final String TASK3 = "do tutorial cs2103#null#null#null#null#null#0#-700020665#2014-04-10 00:33:30.367000000#LOW#true#null#null#null";
	private static final String STATUS_MSG = "Updated the task(s)";
	String taskString = "1 starttime 730pm endtime 930pm";
	String invalidkey = "1 rubbish";
	String invalidIndex = "100";
	int index1 = 0;
	String INDEX_OUT_OF_BOUND = "Index is out of the list.";
	String NO_KEYWORDS_FOUND = "Please include any keywords to update i.e. starttime, endtime, location, desc, date";
	private static final String LIST = "list";
	private static final String FILE_DIR = "user.dir";
	private static final String FILE_NAME = "tasks.txt";
	private static final String FILE_HEADING = "null\r\nnull\r\nnull\r\n";

	private File tasks;

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
		Field f = Processor.class.getDeclaredField(LIST);
		f.setAccessible(true);
		if (LIST.equals(f.getName())) {
			f.setAccessible(true);
			f.set(LIST, newList);
		}
	}

	@Test
	public void testUpdateIndexOne() throws InvalidInputException {
		String task1 = UpdateProcessor.processUpdate(taskString);
		assertEquals(STATUS_MSG, task1);
	}

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
}
