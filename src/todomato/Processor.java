package todomato;

import hirondelle.date4j.DateTime;

import java.io.IOException;
import java.util.Stack;
import java.util.TimeZone;

/**
 * This class stores information needed to process a user's command,
 * and contains methods to parse a command's arguments. 
 * 
 */
public class Processor {

	protected static String fileLoc = "tasks.txt";
	protected static FileHandler fileHandler = new FileHandler(fileLoc);
	protected static TaskDTList list = fileHandler.readFile();
	protected static TaskDTList displayList = list;
	protected static Stack<TaskDTList> undoList = new Stack<TaskDTList>();
	protected static Stack<TaskDTList> redoList = new Stack<TaskDTList>();
	protected static final int PM = 1;
	protected static final int NO_OF_CHAR_IN_SINGLE_DIGIT_HOUR = 1;
	protected static final int NO_OF_CHAR_IN_DOUBLE_DIGIT_HOUR = 2;
	protected static final int NO_OF_CHAR_IN_SINGLE_DIGIT_HOUR_AND_MINUTES = 3;
	protected static final int NO_OF_CHAR_IN_DOUBLE_DIGIT_HOUR_AND_MINUTES = 4;
	protected static final int POS_OF_MINUTE_AFTER_SINGLE_DIGIT_HOUR = 1;
	protected static final int POS_OF_MINUTE = 2;
	protected static final String INVALID_TIME_FORMAT = "Invalid Date Format";
	protected static final int NOT_FOUND = -1;
	protected static final String PRIORITY_LOW = "LOW";
	protected static final String PRIORITY_MED = "MEDIUM";
	protected static final String PRIORITY_HIGH = "HIGH";
	

	/**
	 * This stores a copy of the current list before modifications are made
	 * for possible undo operations in the future.
	 */
	protected static void storeCurrentList() {
		TaskDTList lastList = new TaskDTList();
		lastList.deepCopy(list);
		undoList.push(lastList);
	}
	
	/**
	 * Converts "2" "1" to "YYYY-MM-DD"
	 * @author Daryl
	 * @param String month, String day
	 * 
	 * @return userDate
	 * @throws IOException
	 */
	protected static String convertDateToStandardForm(String month, String day) {
		String year = Integer.toString(DateTime.now(TimeZone.getTimeZone("GMT+8:00")).getYear());
		if (month.length() == 1) {
			month = "0" + month;
		}
		if (day.length() == 1) {
			day = "0" + day;
		}
		return year + "-" + day + "-" + month;
	}
	
	/**
	 * Checks whether "am" or "pm" is in the string
	 * @param input
	 * @return integer indicating which meridiem is present
	 * -1 if there is not one
	 */

	protected static int checkMeridiem(String input) {
		String meridiems[] = new String[] { "am", "pm" };
		for (int i = 0; i < meridiems.length; i++) {
			if (input.contains(meridiems[i])) {
				return i;
			}
		}
		return -1;
	}
	/**
	 * Converts "Jan 1" to "YYYY-MM-DD" (DateTime format)
	 * @param input
	 * @return standardFormDate
	 * @throws InvalidInputException
	 */
	protected static String parseDateString(String input) {
		if (DateTime.isParseable(input)) {
			return input;
		}
		if (input == null) {
			return null;
		}
		input = input.toLowerCase();
		String[] parts = input.split(" ");
		String standardFormDate = null;
		String[] months = new String[] { "jan", "feb", "mar", "apr", "may",
				"jun", "jul", "aug", "sep", "oct", "nov", "dec" };
		for (int i = 0; i < months.length; i++) {
			if (parts.length > 1) {
				if (parts[0].contains(months[i])) {
					standardFormDate = convertDateToStandardForm(
							parts[1], String.valueOf(i + 1));
				} else if (parts[1].contains(months[i])) {
					standardFormDate = convertDateToStandardForm(
							parts[0], String.valueOf(i + 1));
				}
			}
		}
		
		return standardFormDate;
	}
	
	/**
	 * Converts "930pm" to HH:MM (DateTime Format)
	 * @param input
	 * @return standardFormDate
	 * @throws InvalidInputException
	 */
	
	protected static String parseTimeStringFromInput(String input) {
		String TimeString = null;
		if (input == null) {
			return "";
		}
		String meridiem[] = new String[] { "am", "pm" };
		String userHour = null;
		String userMinute = "00";
		int meridiemIndex = checkMeridiem(input);
		if (meridiemIndex != NOT_FOUND) {
			input = input.substring(0,
					input.indexOf(meridiem[meridiemIndex]));
			if (input.length() == NO_OF_CHAR_IN_SINGLE_DIGIT_HOUR) {
				userHour = input;
			} else if (input.length() == NO_OF_CHAR_IN_DOUBLE_DIGIT_HOUR) {
				userHour = input;
			} else if (input.length() == NO_OF_CHAR_IN_SINGLE_DIGIT_HOUR_AND_MINUTES) {
				userHour = input.substring(0, POS_OF_MINUTE_AFTER_SINGLE_DIGIT_HOUR);
				userMinute = input.substring(POS_OF_MINUTE_AFTER_SINGLE_DIGIT_HOUR);
			} else if (input.length() == NO_OF_CHAR_IN_DOUBLE_DIGIT_HOUR_AND_MINUTES) {
				userHour = input.substring(0, POS_OF_MINUTE);
				userMinute = input.substring(POS_OF_MINUTE);
			}
			if (meridiemIndex == PM) {
				if (Integer.parseInt(userHour) != 12) {
					//Adds 12 hours to the hour if there is PM
					userHour = Integer.toString(Integer.parseInt(userHour) + 12);
				}
			}
		} else {
			if (input.length() == 5 && input.contains(":")) {
				return input;
			} else {
				if ((input.length() == NO_OF_CHAR_IN_SINGLE_DIGIT_HOUR) 
						|| (input.length() == NO_OF_CHAR_IN_DOUBLE_DIGIT_HOUR)) {
					userHour = input;
				} else if (input.length() == NO_OF_CHAR_IN_DOUBLE_DIGIT_HOUR_AND_MINUTES) {
					userHour = input.substring(0, POS_OF_MINUTE);
					userMinute = input.substring(POS_OF_MINUTE);
				} else if (input.length() == NO_OF_CHAR_IN_SINGLE_DIGIT_HOUR_AND_MINUTES) {
					userHour = input.substring(0, POS_OF_MINUTE_AFTER_SINGLE_DIGIT_HOUR);
					userMinute = input.substring(POS_OF_MINUTE_AFTER_SINGLE_DIGIT_HOUR);
				}
			}
		}
		if (userHour.length() == NO_OF_CHAR_IN_SINGLE_DIGIT_HOUR) {
			//pads a single digit hour to fit the DateTime format
			userHour = "0" + userHour;
		}
		TimeString = userHour + ":" + userMinute;
		return TimeString;
	}
	
	/**
	 * Converts strings of form YYYY-MM-DD or
	 * HH:MM to DateTime format
	 * @param input
	 * @return DateTime
	 */
	
	protected static DateTime convertStringToDateTime(String input) {
		DateTime userDateTime = null;
		if (input == null) {
			return null;
		} else {
			if (!DateTime.isParseable(input)) {
				return null;
			} else {
				userDateTime = new DateTime(input);
			}
		}
		return userDateTime;
	}
	
	protected static String parsePriorityFromString (String input) {
		if (input == null) {
			return PRIORITY_LOW;
		}
		input = input.toLowerCase();
		String priorityLevels[] = new String[] { "low", "med", "high"};
		for (int i = 0; i < priorityLevels.length; i++) {
			if (input.contains(priorityLevels[i])) {
				switch (i) {
					case 0:
							return PRIORITY_LOW;
					case 1:
							return PRIORITY_MED;
					case 2:
							return PRIORITY_HIGH;
				}
			}
		}
		return PRIORITY_LOW;
	}

	public static TaskDTList getList() {
		return list;
	}

	public static TaskDTList getDisplayList() {
		return displayList;
	}
}
