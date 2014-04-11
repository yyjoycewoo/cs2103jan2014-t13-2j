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

		if (argument.equals("all")) {
			getViewAll();
		} else if (argument.equals("week")) {
			viewDate = currDate;
			getViewWeek(viewDate);
		} else if (argument.equals("next")) {
			viewDate = viewDate.plusDays(DAYS_IN_A_WEEK);
			getViewWeek(viewDate);
		} else if (argument.equals("prev")) {
			viewDate = viewDate.minusDays(DAYS_IN_A_WEEK);
			getViewWeek(viewDate);
		} else {
			throw new InvalidInputException(argument);
		}
		
		return SUCCESS_DISPLAY + displayList.toString();
	}

	public static void displayBetweenDates(DateTime startDate, DateTime endDate) {
		if (startDate == null && endDate == null) {
			displayList.deepCopy(list);
		} else {
			TaskList currTasks = new TaskList();
			for (Task i : list.getList()) {
				DateTime date = i.getEndDate();
				if (date == null)
					currTasks.addToList(i);
				else if (date.numDaysFrom(startDate) <= 0 && date.numDaysFrom(endDate) >= 0) {
					currTasks.addToList(i);
				}
			}
			displayList.deepCopy(currTasks);
		}
	}
	
	private static void getViewAll() {
		displayBetweenDates(null, null);
		currDaysViewed = "Displaying all tasks";
	}

	private static void getViewWeek(DateTime viewDate) {
		currWeekday = viewDate.getWeekDay();
		startDate = viewDate.minusDays(DAYS_IN_A_WEEK - currWeekday);
		endDate = viewDate.plusDays(currWeekday - INDEX_OFFSET);
		setCurrDaysViewed(startDate.format("MMM DD, YYYY", new Locale("US")));
		setCurrDaysViewed(getCurrDaysViewed() + " - ");
		setCurrDaysViewed(getCurrDaysViewed() + endDate.format("MMM DD, YYYY", new Locale("US")));
		
		//System.out.println(currDaysViewed);

		displayBetweenDates(startDate, endDate);

	}

	public static String getCurrDaysViewed() {
		return currDaysViewed;
	}

	public static void setCurrDaysViewed(String currDaysViewed) {
		DisplayProcessor.currDaysViewed = currDaysViewed;
	}
}
