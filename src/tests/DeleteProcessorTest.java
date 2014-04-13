//@author A0101578H
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

import todomato.DeleteProcessor;
import todomato.FileHandler;
import todomato.InvalidInputException;
import todomato.Processor;
import todomato.TaskList;

public class DeleteProcessorTest {
	private  File tasks;
	
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
        out.write("Lunch#null#null#null#null#null#0#1046042885#2014-04-09 16:19:17.842000000#HIGH#true#null#null#null\r\n");
        out.write("Dinner#null#18:00#2014-04-09#2014-04-10#null#0#-1351579072#2014-04-09 16:18:25.784000000#LOW#false#null#null#null\r\n");
        out.write("Project meeting#null#14:00#2014-04-01#2014-04-10#null#0#570051783#2014-04-09 16:18:48.669000000#MEDIUM#true#null#null#null\r\n");
        out.close();
        
        FileHandler fileHandler = new FileHandler("tasks.txt");
        TaskList newList = fileHandler.readFile();
        //System.out.println(newList.toString());
        Field f = Processor.class.getDeclaredField("list");
        f.setAccessible(true);
        if ("list".equals(f.getName())) {
            f.setAccessible(true);
            //System.out.println(f.get("list"));
            f.set("list", newList);
            //System.out.println(f.get("list"));
        }
	}

	@Test
	public void testDeleteAll() throws InvalidInputException, IOException {
		String message = DeleteProcessor.processDelete("all");
		assertEquals("Deleted: 3 task(s)", message);
	}
	
	@Test
	public void testDeleteSingle() throws InvalidInputException, IOException {
		String message = DeleteProcessor.processDelete("1");
		assertEquals("Deleted: Lunch", message);
	}
	
	// This is a boundary case for the valid indices partition
	@Test
	public void testDeleteMultiple() throws InvalidInputException, IOException {
		String message = DeleteProcessor.processDelete("3,1");
		assertEquals("Deleted: 2 task(s)", message);
	}
	
	@Test
	public void testDeleteStartDate() throws InvalidInputException, IOException {
		String message = DeleteProcessor.processDelete("startdate 1 apr");
		assertEquals("Deleted: 1 task(s)", message);
	}
	
	@Test
	public void testDeleteEndDate() throws InvalidInputException, IOException {
		String message = DeleteProcessor.processDelete("enddate 10 apr");
		assertEquals("Deleted: 2 task(s)", message);
	}
	
	@Test
	public void testDeleteRange() throws InvalidInputException, IOException {
		String message = DeleteProcessor.processDelete("1-3");
		assertEquals("Deleted: 3 task(s)", message);
	}
	
	// This is a boundary case for the invalid indices partition
	@Test
	public void testExceedMaxIndex() throws InvalidInputException {
		String message = DeleteProcessor.processDelete("6");
		assertEquals("Delete failed: Index out of bound", message);
	}
	
	// This is a boundary case for the invalid indices partition
	@Test
	public void testNegativeIndex() throws InvalidInputException {
		String message = DeleteProcessor.processDelete("-1");
		assertEquals("Delete failed: Index not in number format", message);
	}
		
	// This is a test for the empty list partition
	@Test
	public void testEmptyList() throws InvalidInputException {
		try {
			String message = DeleteProcessor.processDelete("all");
			assertEquals("Deleted: 3 task(s)", message);
			DeleteProcessor.processDelete("1");
			fail("Should have thrown InvalidInputException");
		} catch (InvalidInputException e){
			assertEquals(e.getMessage(), "empty list");
		}
	}
	
	@Test
	public void testInvalidNumberOfLimits() throws InvalidInputException {
		try {
			DeleteProcessor.processDelete("-");
			fail("Should have thrown InvalidInputException");
		} catch (InvalidInputException e){
			assertEquals(e.getMessage(), "Invalid range: Upper and lower limits required");
		}
	}
	
	@Test
	public void testInvalidRangeLimits() throws InvalidInputException {
		try {
			DeleteProcessor.processDelete("4-2");
			fail("Should have thrown InvalidInputException");
		} catch (InvalidInputException e){
			assertEquals(e.getMessage(), "Invalid range: Enter <lower index> - <higher index>");
		}
	}
}
