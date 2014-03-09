package tests;

import todomato.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import todomato.Controller;
import todomato.Date;
import todomato.InvalidInputException;
import todomato.Task;
import todomato.Time;

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
	public void testGetTaskDes() {
		assertEquals("GetTaskDes", "dinner",
				Controller.getTaskDes("dinner at 0900 14/10/12"));
	}

	@Test
	public void testGetTimeAndDate() {
		assertEquals("Get Time and Date", "1900 14/10/12",
				Controller.getTimeAndDate("dinner at 1900 14/10/12"));
	}

	@Test
	public void testCheckForAtPosition() {
		assertEquals("Check for At", 4,
				Controller.checkForAtPosition("1234 at "));
	}

	@Test
	public void testCheckTime() throws InvalidInputException {
		Time checkTime = Controller.getTime("1930");
		assertEquals("", 19, checkTime.getHour());
		assertEquals("", 30, checkTime.getMin());
	}

	@Test
	public void testCheckTimeHour() throws InvalidInputException {
		Time checkTime = Controller.getTime("19");
		assertEquals("", 19, checkTime.getHour());
		assertEquals("", 00, checkTime.getMin());
	}

	@Test
	public void testCheckForEmptyAtPosition() {
		assertEquals("Check Empty", 0, Controller.checkForAtPosition("1234"));
	}

	@Test
	public void testgetDate() throws NumberFormatException, InvalidInputException {
		Date checkDate = Controller.getDate("14/10");
		assertEquals("", 14, checkDate.getDay());
		assertEquals("", 10, checkDate.getMonth());
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
