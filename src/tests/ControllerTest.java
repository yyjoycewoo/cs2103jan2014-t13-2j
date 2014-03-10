package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import todomato.Controller;
import todomato.Date;
import todomato.InvalidInputException;
import todomato.Task;

public class ControllerTest {

	@Test
	public void testProcessAdd() throws NumberFormatException, InvalidInputException {
		Task userTask = Controller
				.processAdd("dinner with parents at 1930 14/10 in utown");
		assertEquals("", userTask.getDescription(), "dinner with parents");
		assertEquals("", 10, userTask.getDate().getMonth());
		assertEquals("", 14, userTask.getDate().getDay());
		assertEquals("", 19, userTask.getStartTime().getHour());
		assertEquals("", 30, userTask.getStartTime().getMin());
	}

	@Test
	public void testRetrieveDateFromEndTime() throws NumberFormatException, InvalidInputException {
		String input = "1420 14/10";
		Date userDate = Controller.retrieveDateInTimeString(input);
		assertEquals("", 10, userDate.getMonth());
		assertEquals("", 14, userDate.getDay());
	}

	@Test
	public void testProcessAddDes() throws NumberFormatException, InvalidInputException {
		Task userTask = Controller.processAdd("dinner");
		assertEquals("", userTask.getDescription(), "dinner");
		assertNull("", userTask.getDate());
	}

	@Test
	public void testProcessUpdateTime() throws InvalidInputException {
		Task userTask = Controller.processUpdate("1 endtime 1330");
		assertEquals("", userTask.getEndTime().getHour(), 13);
		assertEquals("", userTask.getEndTime().getMin(), 30);
	}

	@Test
	public void testProcessUpdateDesc() throws InvalidInputException {
		Task userTask = Controller.processUpdate("1 desc cut dog's fur");
		assertEquals("", userTask.getDescription(), "cut dog's fur");
	}

	@Test
	public void testProcessUpdateTimeDesc() throws InvalidInputException {
		Task alternate = Controller.processUpdate("1 starttime 2200 desc haha");
		assertEquals("", alternate.getDescription(), "haha");
		assertEquals("", alternate.getStartTime().getHour(), 22);
		assertEquals("", alternate.getStartTime().getMin(), 00);
	}

	@Test
	public void testProcessUpdateDate() throws InvalidInputException {
		Task userTask = Controller.processUpdate("1 date 13/10");
		assertEquals("", userTask.getDate().getDay(), 13);
		assertEquals("", userTask.getDate().getMonth(), 10);
	}
}
