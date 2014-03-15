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
}
