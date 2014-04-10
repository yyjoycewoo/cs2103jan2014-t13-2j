package todomato;

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
	//private static final String INVALID_ARGUMENT_MESSAGE = "Invalid argument";
	
	/**
	 * @author linxuan
	 * @param argument 
	 * @return Status message along with a String of the list 
	 */
	public static String processDisplay() {
		displayList.deepCopy(list);
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
}
