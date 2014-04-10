//@author A0096620E
package todomato;

import hirondelle.date4j.DateTime;

import java.io.IOException;
import java.util.Stack;
import java.util.TimeZone;

/**
 * This class stores information needed to process a user's command, and
 * contains methods to parse a command's arguments.
 * 
 */
public class Processor {

	protected static String fileLoc = "tasks.txt";
	protected static FileHandler fileHandler = new FileHandler(fileLoc);
	protected static TaskList list = fileHandler.readFile();
	protected static TaskList displayList = new TaskList();
	protected static Stack<TaskList> undoList = new Stack<TaskList>();
	protected static Stack<TaskList> redoList = new Stack<TaskList>();
	
	protected static final int PM = 1;
	protected static final int NO_OF_CHAR_IN_SINGLE_DIGIT_HOUR = 1;
	protected static final int NO_OF_CHAR_IN_DOUBLE_DIGIT_HOUR = 2;
	protected static final int NO_OF_CHAR_IN_SINGLE_DIGIT_HOUR_AND_MINUTES = 3;
	protected static final int NO_OF_CHAR_IN_DOUBLE_DIGIT_HOUR_AND_MINUTES = 4;
	protected static final int MAX_TIME_LENGTH = 5;
	protected static final int POS_OF_MINUTE_AFTER_SINGLE_DIGIT_HOUR = 1;
	protected static final int POS_OF_MINUTE = 2;
	protected static final int ONE_DAY = 1;
	protected static final int DAY_OFFSET = 1;
	protected static final int DAYS_IN_A_WEEK = 7;
	protected static final int SUNDAY_IN_DATETIME = 0;
	protected static final int NOON = 12;
	protected static final int PM_OFFSET = 12;
	protected static final int POSITION_OFFSET = 1;
	protected static final int ONE_DIGIT = 1;
	
	protected static final String SG_TIMEZONE = "GMT+8:00";
	protected static final String DEFAULT_MINUTE_FORMAT = "00";
	protected static final String DATETIME_PADDING = "0";
	protected static final String DATE_FORMAT_SYMBOL = "-";
	protected static final String TIME_FORMAT_SYMBOL = ":";
	protected static final String INVALID_DATE = "Invalid Date";
	protected static final String INVALID_TIME = "Invalid Time";
	
	protected static final int NOT_FOUND = -1;
	
	protected static final String PRIORITY_LOW = "LOW";
	protected static final String PRIORITY_MED = "MEDIUM";
	protected static final String PRIORITY_HIGH = "HIGH";
	
	protected static DateTime currentDate = DateTime.today(TimeZone
			.getDefault());
	
	protected static final String[] days = new String[] { "mon", "tues", "wed",
			"thurs", "fri", "sat", "sun" };
	protected static final String[] dateKeywords = new String[] { "today", "tomorrow", "tmr" };
	protected static final String meridiems[] = new String[] { "am", "pm" };

	/**
	 * This stores a copy of the current list before modifications are made for
	 * possible undo operations in the future.
	 */
	protected static void storeCurrentList() {
		TaskList lastList = new TaskList();
		lastList.deepCopy(list);
		undoList.push(lastList);
	}
	
	protected static void setUserNameAndPassword(String username, String password) {
		list.setUserNameAndPassword(username, password);
	}

	/**
	 * Converts "2" "1" to "YYYY-MM-DD"
	 * 
	 *    
	 * @param String
	 *            month, String day
	 * 
	 * @return userDate
	 * @throws IOException
	 */
	protected static String convertDateToStandardForm(String month, String day) {
		String year = Integer.toString(DateTime.now(
				TimeZone.getTimeZone(SG_TIMEZONE)).getYear());
		if (month.length() == ONE_DIGIT) {
			month = DATETIME_PADDING + month;
		}
		if (day.length() == ONE_DIGIT) {
			day = DATETIME_PADDING + day;
		}
		return year + DATE_FORMAT_SYMBOL + day + DATE_FORMAT_SYMBOL + month;
	}

	/**
	 * Checks whether "am" or "pm" is in the string
	 * 
	 *    
	 * @param input
	 * @return integer indicating which meridiem is present -1 if there is not
	 *         one
	 */

	protected static int checkMeridiem(String input) {
		for (int i = 0; i < meridiems.length; i++) {
			if (input.contains(meridiems[i])) {
				return i;
			}
		}
		return NOT_FOUND;
	}
	
	/**
	 * Function to determine if a string can be parsed into a date
	 * @param input
	 * @return true or false
	 */
	
	protected static Boolean isParseableByDate (String input) {
		try {
			if(isParseableByInt(input)) {
				return false;
			}
			if (isParseableByTime(input)) {
				return false;
			}
			input = parseDateString(input);
			if (!DateTime.isParseable(input)) {
				return false;
			}
		} catch (InvalidInputException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Function determines if a string can be parsed into an integer
	 * @param input
	 * @return true or false
	 */
	
	protected static Boolean isParseableByTime (String input) {
		try {
			parseTimeString(input);
		} catch (InvalidInputException e) {
			return false;
		}
		return true;
	}

	/**
	 * Converts "Jan 1" to "YYYY-MM-DD" (DateTime format) Other input formats
	 * include days of the week "Monday", "Tuesday", etc You can also put next
	 * before the days of the week This will set the string the DateTime format
	 * of the specified day
	 *    
	 * @param input
	 * @return standardFormDate
	 * @throws InvalidInputException
	 */
	protected static String parseDateString(String input)
			throws InvalidInputException {
		if (input == null) {
			throw new InvalidInputException(INVALID_DATE);
		}

		if (DateTime.isParseable(input)) {
			return input;
		}

		input = input.toLowerCase();
		String standardFormDate = null;
		if (hasTodayOrTmr(input)) {
			standardFormDate = getDateIfTdyOrTmrInString(input);
			return standardFormDate;
		}


		int dayIndex = checkForDay(input);
		if (dayIndex != NOT_FOUND) {
			standardFormDate = currentDate.plusDays(daysFromCurrentDay(input))
					.toString();
			return standardFormDate;
		}

		String[] parts = input.split(" ");
		String[] months = new String[] { "jan", "feb", "mar", "apr", "may",
				"jun", "jul", "aug", "sep", "oct", "nov", "dec" };

		for (int i = 0; i < months.length; i++) {
			if (parts.length > 1) {
				if (parts[0].contains(months[i])) {
					standardFormDate = convertDateToStandardForm(parts[1],
							String.valueOf(i + POSITION_OFFSET));
				} else if (parts[1].contains(months[i])) {
					standardFormDate = convertDateToStandardForm(parts[0],
							String.valueOf(i + POSITION_OFFSET));
				}
			}
		}

		if (standardFormDate == null) {
			throw new InvalidInputException(INVALID_DATE);
		}

		return standardFormDate;
	}
	/**
	 * Returns true if "today" "tomorrow" or "tmr" is in string
	 * @param input
	 * @return
	 */
	
	protected static Boolean hasTodayOrTmr (String input) {
		for (int i = 0; i < dateKeywords.length; i++) {
			if (input.contains(dateKeywords[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns today or tomorrow's date depending on which keyword is present
	 * @param input
	 * @return
	 */
	
	protected static String getDateIfTdyOrTmrInString (String input) {
		String userDate;
		for (int i = 0; i < dateKeywords.length; i++) {
			if (input.contains(dateKeywords[i])) {
				switch (i) {
				case 0:
					userDate = currentDate.toString();
					return userDate;
				case 1:
				case 2:
					userDate = currentDate.plusDays(ONE_DAY).toString();
					return userDate;
				}
			}
		}
		return input;
	}

	/**
	 * Returns the number of days it is from the current day Example : if today
	 * is Wednesday, and input is Tuesday, it will return -1
	 * 
	 * @param input
	 * @return
	 */

	protected static int daysFromCurrentDay(String input) {
		int currentDay = currentDate.getWeekDay();
		int userDay = checkForDay(input);
		
		//1 is used as Sunday is the first day in DateTime, while Monday is
		// first day in this program, the next 3 lines is to readjust
		 
		currentDay -= DAY_OFFSET;
		if (currentDay == SUNDAY_IN_DATETIME) {
			currentDay = DAYS_IN_A_WEEK;
		}
		int daysFromCurrent = userDay - currentDay;
		if (input.contains("next")) {
			daysFromCurrent += DAYS_IN_A_WEEK;
		}
		return daysFromCurrent;
	}
	/**
	 * Checks whether input contains "monday" or "tuesday"
	 * Returns an integer depending on which day it is
	 * @param input
	 * @return integer (1 - 7)
	 */

	protected static int checkForDay(String input) {
		int dayValue = 0;
		for (int i = 0; i < days.length; i++) {
			if (input.contains(days[i])) {
				dayValue = i + POSITION_OFFSET;
				return dayValue;
			}
		}
		return NOT_FOUND;
	}

	/**
	 * Converts "930pm" to HH:MM (DateTime Format)
	 * 
	 * @param input
	 * @return standardFormDate
	 * @throws InvalidInputException
	 */

	protected static String parseTimeString(String input)
			throws InvalidInputException {
		String timeString = null;
		if (input == null) {
			throw new InvalidInputException(INVALID_TIME);
		}
		if (input.length() > MAX_TIME_LENGTH) {
			throw new InvalidInputException(INVALID_TIME);
		}
		if ((input.length() == MAX_TIME_LENGTH) && input.contains(TIME_FORMAT_SYMBOL)) {
			if (DateTime.isParseable(input)) {
				return input;		
			}
		}
		
		timeString = convertStringToStdTimeString(input);
		return timeString;
	}
	
	/**
	 * converts a String to HH:MM(DateTime format)
	 * Supported String inputs are:
	 * 930pm
	 * 930
	 * 9
	 * 2130
	 * @param input
	 * @return
	 * @throws InvalidInputException
	 */
	
	protected static String convertStringToStdTimeString (String input) throws InvalidInputException {
		String userHour = null;
		String userMinute = DEFAULT_MINUTE_FORMAT;
		String timeString = null;
		int meridiemIndex = checkMeridiem(input);
		
		//if am or pm is present, removes them from the string
		if (meridiemIndex != NOT_FOUND) {
			input = input.substring(0, input.indexOf(meridiems[meridiemIndex]));
		}
		if (!isParseableByInt(input)) {
			throw new InvalidInputException(INVALID_TIME);
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
		if (meridiemIndex == PM) { 	
			if (Integer.parseInt(userHour) != NOON) {
				// Adds 12 hours to the hour if there is PM
				userHour = Integer.toString(Integer.parseInt(userHour) + PM_OFFSET);
			}
		}
		if (userHour.length() == NO_OF_CHAR_IN_SINGLE_DIGIT_HOUR) {
			// pads a single digit hour to fit the DateTime format
			userHour = DATETIME_PADDING + userHour;
		}
		timeString = userHour + TIME_FORMAT_SYMBOL + userMinute;
		return timeString;
	}
	/**
	 * Returns true if the string can be parsed by int
	 * Returns false if not
	 * @param input
	 * @return
	 */

	protected static Boolean isParseableByInt(String input) {
		try {
			Integer.parseInt(input);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * Converts strings of form YYYY-MM-DD or HH:MM to DateTime format
	 * 
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
				if (!userDateTime.hasYearMonthDay()) {
					userDateTime = new DateTime(userDateTime.toString());
				}
			}
		}
		return userDateTime;
	}
	
	/**
	 * Parses Priority from string
	 * The input can either be "1", "2" or "3" or
	 * "high", "med", "low"
	 * @param input
	 * @return
	 */

	protected static String parsePriorityFromString(String input) {
		if (input == null) {
			return PRIORITY_LOW;
		}
		if (isParseableByInt(input)) {
			return parsePriorityFromNumber(input);
		}
		return parsePriorityFromWords(input);
	}
	
	/**
	 * Parses priority from a number
	 * default = low
	 * 1 = low
	 * 2 = med
	 * 3 = high
	 * @param input
	 * @return
	 */
	
	protected static String parsePriorityFromNumber (String input) {
		int noPriority = Integer.parseInt(input);
		switch (noPriority) {
		case 1:
			return PRIORITY_LOW;
		case 2:
			return PRIORITY_MED;
		case 3:
			return PRIORITY_HIGH;
		}
		return PRIORITY_LOW;
	}
	
	/**
	 * Parses priority from a string
	 * an invalid input will default to low
	 * @param input
	 * @return
	 */
	
	protected static String parsePriorityFromWords (String input) {
		String priorityLevels[] = new String[] { "low", "med", "high" };
		input = input.toLowerCase();
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
	
	/**
	 * Checks if the String contains "daily" or "weekly" and
	 * returns an integer
	 * Daily = 1
	 * Weekly = 7
	 * @param input
	 * @return
	 */
		
	protected static int parseRecurrencePeriodFromString(String input) {
		if (isParseableByInt(input)) {
			return Integer.parseInt(input);
		}
		String recurPeriodKeywords[] = new String[] { "daily", "weekly"};
		input = input.toLowerCase();
		for (int i = 0; i < recurPeriodKeywords.length; i++) {
			if (input.contains(recurPeriodKeywords[i])) {
				switch (i) {
				case 0:
					return ONE_DAY;
				case 1:
					return DAYS_IN_A_WEEK;
				}
				
			}
		}
		return 0;
	}

	public static TaskList getList() {
		return list;
	}

	public static TaskList getDisplayList() {
		return displayList;
	}
}
