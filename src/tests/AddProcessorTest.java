/**
 * 
 */
package tests;

import static org.junit.Assert.assertEquals;
import hirondelle.date4j.DateTime;

import org.junit.Test;

import todomato.InvalidInputException;
import todomato.Task;

/**
 * @author Hao Eng
 * 
 */
public class AddProcessorTest {

	public class TaskTest {
		@Test
		public void testAddNewTask() throws InvalidInputException {
			String taskString = "hate eat at 05:00 until 07:00 on 16/02/2014 in utown";
			assertEquals(Task.createTaskFromFileString(taskString).toString(),
					new Task("hate eat", new DateTime(5, 00, null, null, null,
							null, null), new DateTime(7, 00, null, null, null,
							null, null), new DateTime(16, 02, 2014, null, null,
							null, null), "utown", 0).toString());
		}

		@Test
		public void testAddEmptyString() throws InvalidInputException {
			String taskString = "";
			assertEquals(null, Task.createTaskFromFileString(taskString));
		}

		@Test
		public void testOneWord() throws InvalidInputException {
			String taskString = "talk";
			assertEquals(Task.createTaskFromFileString(taskString).toString(),
					new Task("talk").toString());
		}

	}

}
