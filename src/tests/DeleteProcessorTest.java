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
import todomato.UndoProcessor;

public class DeleteProcessorTest {
	private  File tasks;
	
	@Rule 
	public  TemporaryFolder folder= new TemporaryFolder();
	
	@Before
	public void createTestData() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		System.setProperty("user.dir", folder.getRoot().toString());
		tasks = folder.newFile("tasks.txt");
		BufferedWriter out = new BufferedWriter(new FileWriter(tasks));
        out.write("es1531 assignment 3#null#null#null#null#0#404344556#2014-04-01 23:11:04.575000000#LOW#false#null#null#null\r\n");
        out.write("cs post-lecture quiz#null#null#2014-03-28#null#7#-1997137046#2014-04-01 22:57:15.488000000#LOW#true#null#null#null\r\n");
        out.write("ie2150 project#null#null#null#null#0#-195311185#2014-04-02 22:49:07.307000000#HIGH#false#null#null#null\r\n");
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
		//UndoProcessor.processUndo();
	}
	
	@Test
	public void testDeleteSingle() throws InvalidInputException, IOException {
		String message = DeleteProcessor.processDelete("1");
		assertEquals("Deleted: es1531 assignment 3", message);
		//UndoProcessor.processUndo();
	}
	
	// This is a boundary case for the valid indices partition
	@Test
	public void testDeleteMultiple() throws InvalidInputException, IOException {
		String message = DeleteProcessor.processDelete("3,1");
		assertEquals("Deleted: 2 task(s)", message);
		//UndoProcessor.processUndo();
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
		assertEquals("Delete failed: Index out of bound", message);
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
		//UndoProcessor.processUndo();
	}
	
	
}
