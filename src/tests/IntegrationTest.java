package tests;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.*;
import org.junit.rules.TemporaryFolder;

import todomato.FileHandler;
import todomato.FindProcessor;
import todomato.Processor;
import todomato.SplitProcessorsHandler;
import todomato.InvalidInputException;
import todomato.Task;
import todomato.TaskList;

public class IntegrationTest {
	private File tasks;
	
	@Rule 
	public TemporaryFolder folder= new TemporaryFolder();
	
	@Before
	public void createTestData() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		System.setProperty("user.dir", folder.getRoot().toString());
		tasks = folder.newFile("tasks.txt");
		BufferedWriter out = new BufferedWriter(new FileWriter(tasks));
        out.write("cs post-lecture quiz#null#null#2014-03-28#null#7#-1997137046#2014-04-01 22:57:15.488000000#LOW#true#null#null#null\r\n");
        out.write("study#12:00#16:00#2014-03-31#utown#7#950768200#2014-04-02 23:17:24.118000000#LOW#true#null#null#null\r\n");
        out.write("es1531 assignment 2#null#null#2014-04-01#null#0#636174328#2014-03-30 16:24:17.877000000#HIGH#true#null#null#null\r\n");
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
	public void testIntegrated() throws InvalidInputException, IOException {
		String fileLoc = "tasks.txt";
		String command = "";
		FileHandler fileHandler = new FileHandler(fileLoc);
		TaskList list = fileHandler.readFile();
		
		assertEquals("cs post-lecture quiz", list.getListItem(0).getDescription());
		String messageAdd1 = SplitProcessorsHandler.processCommand("add something");
		assertEquals("Added something", messageAdd1);
		
		String messageUndo1 = SplitProcessorsHandler.processCommand("undo");
		assertEquals("Last action undone", messageUndo1);
		
		//test undo when there is nothing to undo
		String messageUndo2 = SplitProcessorsHandler.processCommand("undo");
		assertEquals("No changes to undo", messageUndo2);
		
		String messageRedo1 = SplitProcessorsHandler.processCommand("redo");
		assertEquals("Last action redone", messageRedo1);
		
		//test redo when there is nothing to redo
		String messageRedo2 = SplitProcessorsHandler.processCommand("redo");
		assertEquals("No changes to redo", messageRedo2);

		//test case of a task that does not exist
		String messageFind1 = SplitProcessorsHandler.processCommand("find AONETUHEONUTHOu");
		assertEquals("No tasks found", messageFind1);

		//test boundary case of searching an empty string
		String messageFind2 = FindProcessor.processFind("");
		assertEquals("Search completed", messageFind2);
		String original_list = Processor.getList().toString();
		String found_list = Processor.getDisplayList().toString();
		assertEquals(original_list, found_list);	

		String messageAdd2 = SplitProcessorsHandler.processCommand("add Tutorial @ERC-SR2 on 5 Feb at 2pm");		
		assertEquals("Added Tutorial at 14:00 on Feb 05 2014 in ERC-SR2", messageAdd2);

		//test case of a task that does exist 
		command = "tutorial";
		String messageFind3 = FindProcessor.processFind(command);
		assertEquals("Search completed", messageFind3);
		
		String messageUpdate1 = SplitProcessorsHandler.processCommand("update 4 desc Dinner with Parents\\ location home\\");
		assertEquals("Dinner with Parents in home", messageUpdate1);
		
		String messageDelete1 = SplitProcessorsHandler.processCommand("delete all");
		assertEquals("Deleted: 5 task(s)", messageDelete1);
		assertEquals(Processor.getList().toString(), "");
	}
}
