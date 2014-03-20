package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import todomato.Date;
import todomato.FileHandler;
import todomato.InvalidInputException;
import todomato.Task;
import todomato.TaskDT;
import todomato.Time;
import todomato.UpdateProcessor;

public class TaskTest {

	FileHandler f = new FileHandler("D:/test.txt");

	@Test
	public void test() throws InvalidInputException {
		String taskString = "hate eat at 05:00 until 07:00 on 16/02/2014 in utown";
		assertEquals(Task.createTaskFromString(taskString).toString(),
				new Task("hate eat", new Time(5, 00), new Time(7, 00),
						new Date(16, 02, 2014), "utown").toString());
	}

	@Test
	public void test2() throws InvalidInputException {
		String taskString = "eat at 05:00 on 14/02/2014";
		assertEquals(
				true,
				Task.createTaskFromString(taskString)
						.toString()
						.equals(new Task("eat", new Time(5, 00), new Date(14,
								02, 2014)).toString()));
	}

	@Test
	public void test3() throws InvalidInputException {
		String taskString = "hate eat in utown plaza";
		assertEquals(true, Task.createTaskFromString(taskString).toString()
				.equals(new Task("hate eat", "utown plaza").toString()));
	}

	@Test
	public void test4() throws InvalidInputException {
		String taskString = "eat on 14/02/2014 in starbucks";
		assertEquals(
				true,
				Task.createTaskFromString(taskString)
						.toString()
						.equals(new Task("eat", new Date(14, 02, 2014),
								"starbucks").toString()));
	}

	@Test
	public void test5() throws InvalidInputException {
		String taskString = "facebooking at 12:00 on 14/02/2014";
		assertEquals(
				true,
				Task.createTaskFromString(taskString)
						.toString()
						.equals(new Task("facebooking", new Time(12, 00),
								new Date(14, 02, 2014)).toString()));
	}

	@Test
	public void test6() throws InvalidInputException {
		String taskString = "";
		assertEquals(null, Task.createTaskFromString(taskString));
	}

	@Test
	public void test7() throws InvalidInputException {
		String taskString = "talk";
		assertEquals(Task.createTaskFromString(taskString).toString(),
				new Task("talk").toString());
	}

	@Test
	public void test8() throws InvalidInputException {
		String taskString = "1 starttime 730pm endtime 930pm";
		TaskDT task = UpdateProcessor.processUpdate(taskString);
		assertEquals("", task.getStartTime(), "19:30");
	}
	/*
	 * @Test public void test8() throws IOException { String outputString =
	 * "eat in utown" + "\r\n" + "eat at 08:00 on 14/02/2014" + "\r\n"; TaskList
	 * list = f.readFile(); assertEquals(outputString , list.toString()); }
	 * 
	 * @Test public void test9() throws IOException, InvalidInputException {
	 * String outputString = "eat in utown" + "\r\n" +
	 * "eat at 08:00 on 14/02/2014" + "\r\n" + "talk" + "\r\n"; TaskList list =
	 * f.readFile(); String taskString = "talk"; Task t =
	 * Task.createTaskFromString(taskString); list.addToList(t); TaskList
	 * updatedList = f.updateFile(list); assertEquals(outputString ,
	 * updatedList.toString()); }
	 */

}
