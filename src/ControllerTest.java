import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ControllerTest {

	@Test
	public void testProcessAdd() {
		Task userTask = Controller
				.processAdd("dinner with parents at 1930 14/10 in utown");
		assertEquals("", userTask.getDescription(), "dinner with parents");
		assertEquals("", 10, userTask.getDate().getMonth());
		assertEquals("", 14, userTask.getDate().getDay());
		assertEquals("", 19, userTask.getStartTime().getHour());
		assertEquals("", 30, userTask.getStartTime().getMin());
	}

	@Test
	public void testRetrieveDateFromEndTime() {
		String input = "1420 14/10";
		Date userDate = Controller.retrieveDateInTimeString(input);
		assertEquals("", 10, userDate.getMonth());
		assertEquals("", 14, userDate.getDay());
	}

	@Test
	public void testProcessUpdateTime() {
		Task userTask = Controller.processUpdate("1 time 1330");
		assertEquals("", userTask.getStartTime().getHour(), 13);
		assertEquals("", userTask.getStartTime().getMin(), 30);
	}

	@Test
	public void testProcessUpdateDesc() {
		Task userTask = Controller.processUpdate("1 desc cut dog's fur");
		assertEquals("", userTask.getDescription(), "cut dog's fur");
	}

	public void testProcessAddDes() {
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
	public void testCheckTime() {
		Time checkTime = Controller.getTime("1930");
		assertEquals("", 19, checkTime.getHour());
		assertEquals("", 30, checkTime.getMin());
	}

	@Test
	public void testCheckTimeHour() {
		Time checkTime = Controller.getTime("19");
		assertEquals("", 19, checkTime.getHour());
		assertEquals("", 00, checkTime.getMin());
	}

	@Test
	public void testCheckForEmptyAtPosition() {
		assertEquals("Check Empty", 0, Controller.checkForAtPosition("1234"));
	}

	@Test
	public void testgetDate() {
		Date checkDate = Controller.getDate("14/10");
		assertEquals("", 14, checkDate.getDay());
		assertEquals("", 10, checkDate.getMonth());
	}

}
