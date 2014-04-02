package tests;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import todomato.DeleteProcessor;
import todomato.InvalidInputException;
import todomato.UndoProcessor;

public class DeleteProcessorTest {
	private  File tasks;
	
	@Rule 
	public  TemporaryFolder folder= new TemporaryFolder();
	
	@Before
	public void createTestData() throws IOException {
		System.setProperty("user.dir", folder.getRoot().toString());
		tasks = folder.newFile("tasks.txt");
		BufferedWriter out = new BufferedWriter(new FileWriter(tasks));
        out.write("CS2103#13:00#null#2014-03-28#Comp 2#0#1916257709#2014-03-27 00:54:13.814000000#HIGH#false\r\n");
        out.write("ES1531 Essay Draft#null#19:00#2014-03-29#null#0#76891653#2014-03-27 00:54:45.213000000#MEDIUM#false\r\n");
        out.write("SomethingSomething#05:00#null#null#UTown Starbucks#0#-1028576150#2014-03-27 00:55:05.556000000#LOW#false\r\n");
        out.write("Dinner with parents#19:00#null#2014-03-29#Yishun#0#-1857384245#2014-03-27 00:55:34.935000000#LOW#false\r\n");
        out.write("Buy Groceries#18:00#null#2014-03-28#NTUC Fairprice#0#400330373#2014-03-27 00:56:13.423000000#LOW#false\r\n");
        out.close();
	}

	
	@Test
	public void testDeleteAll() throws InvalidInputException, IOException {
		String message = DeleteProcessor.processDelete("all");
		assertEquals("Deleted: 5 task(s)", message);
		UndoProcessor.processUndo();
	}
	
	@Test
	public void testDeleteSingle() throws InvalidInputException, IOException {
		String message = DeleteProcessor.processDelete("1");
		assertEquals("Deleted: CS2103 at 13:00 on Mar 28 2014 in Comp 2", message);
		UndoProcessor.processUndo();
	}
	
	// This is a boundary case for the valid indices partition
	@Test
	public void testDeleteMultiple() throws InvalidInputException, IOException {
		String message = DeleteProcessor.processDelete("4,1,5");
		assertEquals("Deleted: 3 task(s)", message);
		UndoProcessor.processUndo();
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
			assertEquals("Deleted: 5 task(s)", message);
			DeleteProcessor.processDelete("1");
			fail("Should have thrown InvalidInputException");
		} catch (InvalidInputException e){
			assertEquals(e.getMessage(), "empty list");
		}
		UndoProcessor.processUndo();
	}
	
	
}
