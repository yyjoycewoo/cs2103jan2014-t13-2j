import static org.junit.Assert.*;

import org.junit.Test;


public class ControllerTest {

	@Test
	public void testProcessAdd() {
		Task userTask = Controller.processAdd("dinner with parents at 1900 14/10/12");
		assertEquals("", userTask.getDescription(), "dinner with parents");
	}
	
	public void testProcessAddDes() {
		Task userTask = Controller.processAdd("dinner");
		assertEquals("", userTask.getDescription(), "dinner");
		assertNull("", userTask.getDate());
	}

	@Test
	public void testGetTaskDes() {
		assertEquals("GetTaskDes", "dinner", Controller.getTaskDes("dinner at 9 14/10/12"));
	}
	
	@Test
	public void testGetTimeAndDate() {
		assertEquals("Get Time and Date", "1900 14/10/12", Controller.getTimeAndDate("dinner at 1900 14/10/12"));
	}

	@Test
	public void testCheckForAtPosition() {
		assertEquals("Check for At", 4, Controller.checkForAtPosition("1234 at "));
	}
	
	@Test
	public void testCheckTime() {
		Time checkTime = Controller.getTime("1930");
		assertEquals("", 19, checkTime.getHour());
		assertEquals("", 30, checkTime.getMin());
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
