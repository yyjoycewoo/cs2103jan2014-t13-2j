package tests;

//@ A0101324A
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
import todomato.NotifyProcessor;
import todomato.Processor;
import todomato.TaskList;

public class NotifyProcessorTest {

	private static String TASK1 = "dotaing#01:00#02:00#null#null#utown#0#978601153#null00:33:20.173000000#LOW#false#null#null16:29:01.763000000#null\r\n";
	private static String TASK2 = "revise CS2106#null#18:00#2014-04-09#2014-04-10#null#0#-1351579072#2014-04-09 16:18:25.784000000#LOW#false#null#null#null\r\n";
	private static String TASK3 = "walk with dog and wash car#01:00#02:00#null#null#utown#0#978601153#null00:33:20.173000000#LOW#false#null#null16:29:01.763000000#null\r\n";
	private File tasks;
	private static final String LIST = "list";
	private static final String FILE_DIR = "user.dir";
	private static final String FILE_NAME = "tasks.txt";
	private static final String FILE_HEADING = "null\r\nnull\r\nnull\r\n";
	private static final String task1 = "1 time 3pm";
	private static final String task2 = "2 time 3pm";
	private static final String task3 = "3 ";
	private static final String wrongIndexRightKey = "0 time 3pm";
	private static final String NO_DATE = "No start or end date for the selected task.";
	private static final String NEED_TODAY = "Notification time is only for today's task!";
	private static final String GOT_KEYWORDS = "Have you type notify <index> time <time>?";

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
		System.out.println(newList);
		Field f = Processor.class.getDeclaredField(LIST);
		f.setAccessible(true);
		if (LIST.equals(f.getName())) {
			f.setAccessible(true);
			f.set(LIST, newList);
		}
	}

	// Testing for notify time on tasks with no dates
	@Test
	public void testNoDate() throws InvalidInputException {
		try {
			NotifyProcessor.processNotify(task1);
		} catch (InvalidInputException e) {
			assertEquals(NO_DATE, e.getMessage());
		}
	}

	// Testing for notify time on past or future tasks
	@Test
	public void testPastOrFutureTask() throws InvalidInputException {
		try {
			NotifyProcessor.processNotify(task2);
		} catch (InvalidInputException e) {
			assertEquals(NEED_TODAY, e.getMessage());
		}
	}

	// Testing for notify keyword
	@Test
	public void testKeyword() throws InvalidInputException {
		try {
			NotifyProcessor.processNotify(task3);
		} catch (InvalidInputException e) {
			assertEquals(GOT_KEYWORDS, e.getMessage());
		}
	}

	// Testing for wrong index with right keywords
	@Test
	public void testWrongIndexRightKey() throws InvalidInputException {
		try {
			NotifyProcessor.processNotify(wrongIndexRightKey);
			fail("Should have thrown invalid index.");
		} catch (InvalidInputException e) {
		}
	}
}
