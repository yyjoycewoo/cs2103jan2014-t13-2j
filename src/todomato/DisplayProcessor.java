//@author A0101578H
package todomato;

import java.util.Locale;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * This class contains methods to process display commands by the user. 
 * 
 * <p>
 * The following ways to display are supported: 
 * <ul>
 * <li> display all tasks
 * <ul> <li> "display" </ul>
 * </ul>
 * 
 */
public class DisplayProcessor extends Processor {
	private static final String SUCCESS_DISPLAY = "All tasks have been displayed: ";
	private static final String INVALID_ARGUMENT_MESSAGE = "Invalid argument";
	private static final int INDEX_OFFSET = 1;

	private static int currWeekday;
	private static DateTime startDate;
	private static DateTime endDate;
	private static String currDaysViewed;
	private static DateTime viewDate;
	private static DateTime currDate = DateTime.now(TimeZone.getDefault());
	
	/**
	 * @param argument  
	 * @return Status message along with a String of the list 
	 */
	public static String processDisplay(String argument) throws InvalidInputException {
		return SUCCESS_DISPLAY + list.toString();
	}
}
