/**
 * 
 */
package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import todomato.InvalidInputException;
import todomato.Processor;
import todomato.UpdateProcessor;

/**
 * @author Hao Eng
 * 
 */
public class UpdateProcessorTest {

	@Test
	public void testUpdateIndexOne() throws InvalidInputException {
		String taskString = "1 starttime 730pm endtime 930pm";
		String task = UpdateProcessor.processUpdate(taskString);
		assertEquals(Processor.getList().getListItem(1), task);
	}

	// testing the out of bound index
	@Test(expected = InvalidInputException.class)
	public void testInvalidIndexOne() throws InvalidInputException {
		String invalidIndex = "0";
		UpdateProcessor.processUpdate(invalidIndex);
	}

	@Test
	public void testExceedIndex() {
		try {
			String invalidIndex = "100";
			UpdateProcessor.processUpdate(invalidIndex);
			fail("My method didn't throw when I expected it to");
		} catch (InvalidInputException expectedException) {
		}
	}

	// testing for invalid keyword
	@Test
	public void testInvalidKeyword() {
		try {
			String invalidkey = "1 haha";
			UpdateProcessor.processUpdate(invalidkey);
		} catch (InvalidInputException e) {
		}
	}

}
