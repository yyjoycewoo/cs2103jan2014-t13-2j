package todomato;

import hirondelle.date4j.DateTime;

import java.io.IOException;
import java.util.Stack;

/**
 * This class stores information needed to process a user's command,
 * and contains methods to parse a command's arguments. 
 * 
 */
public class Processor {
	//protected static String fileLoc = "C:\\Users\\Hao Eng\\Desktop\\test.txt";
	protected static String fileLoc = "C:\\Users\\Joyce\\Documents\\Year 2\\test.txt";
	//protected static String fileLoc = "D:\\test.txt";
	protected static FileHandler fileHandler = new FileHandler(fileLoc);
	protected static TaskDTList list = fileHandler.readFile();
	protected static Stack<TaskDTList> oldLists = new Stack<TaskDTList>();
	protected static final int NO_OF_CHAR_IN_HOUR_AND_MINUTE = 4;
	protected static final int POS_OF_MINUTE = 2;
	protected static final String INVALID_TIME_FORMAT = "Invalid Date Format";
	protected static final int SPACE_NOT_FOUND = -1;

	/**
	 * This stores a copy of the current list before modifications are made
	 * for possible undo operations in the future.
	 */
	protected static void storeCurrentList() {
		TaskDTList lastList = new TaskDTList();
		lastList.deepCopy(list);
		oldLists.push(lastList);
	}

	public static TaskDTList getList() {
		return list;
	}

	/**
	 * Retrieves date from user input of the form DD/MM or DD/MM/YY Returns a
	 * Date Object with the corresponding day and month
	 * 
	 * @param input
	 * @return userDate
	 * @throws InvalidInputException
	 * @throws NumberFormatException
	 */
	protected static Date parseDateFromStandardForm(String input)
			throws NumberFormatException, InvalidInputException {
		String delims = "/";
		String[] dateTokens = input.split(delims);
		if (dateTokens.length == 2) {
			Date userDate = new Date(Integer.parseInt(dateTokens[0]),
					Integer.parseInt(dateTokens[1]));
			return userDate;
		} else if (dateTokens.length == 3) {
			Date userDate = new Date(Integer.parseInt(dateTokens[0]),
					Integer.parseInt(dateTokens[1]),
					Integer.parseInt(dateTokens[2]));
			return userDate;
		} else {
			return null;
		}

	}

	/**
	 * Retrieves date from user input of the form DD/MM or DD/MM/YY It also
	 * allows for "Feb 1" or "1 Feb" Returns a Date Object with the
	 * corresponding day and month
	 * 
	 * @param input
	 * @return userDate
	 * @throws InvalidInputException
	 * @throws NumberFormatException
	 */
	protected static Date retrieveDateStringFromInput(String input) {
		try {
			input = input.toLowerCase();
			String[] parts = input.split(" ");
			String dateDelimiter = "/";
			String[] months = new String[] { "jan", "feb", "mar", "apr", "may",
					"jun", "jul", "aug", "sep", "oct", "nov", "dec" };
			for (int i = 0; i < months.length; i++) {
				if (parts.length > 1) {
					if (parts[0].contains(months[i])) {
						String standardFormDate = convertDateToStandardForm(
								parts[1], String.valueOf(i + 1));
						Date userDate = parseDateFromStandardForm(standardFormDate);
						return userDate;
					} else if (parts[1].contains(months[i])) {
						String standardFormDate = convertDateToStandardForm(
								parts[0], String.valueOf(i + 1));
						Date userDate = parseDateFromStandardForm(standardFormDate);
						return userDate;
					}
				}
			}
			if (parts[0].contains(dateDelimiter)) {
				Date userDate = parseDateFromStandardForm(parts[0]);
				return userDate;
			}
		} catch (InvalidInputException e) {
			return null;
		}
		return null;
	}

	/**
	 * Converts "2" "1" to "2/1"
	 * @author Daryl
	 * @param String month, String day
	 * 
	 * @return userDate
	 * @throws IOException
	 */
	protected static String convertDateToStandardForm(String month, String day) {
		return month + "/" + day;
	}

	/**
	 * Returns time from user string i.e. 19 hours 30 minutes from "1930" or
	 * "730pm" or "0730pm"
	 * 
	 * @param input
	 * @return userTime
	 * @throws InvalidInputException
	 */
	protected static Time parseTimeFromString(String input)
			throws InvalidInputException {
		Time userTime = null;
		try {
			if (input == null) {
				return null;
			}
			int hour = 0, minute = -1;
			String meridiem[] = new String[] { "am", "pm" };
			int meridiemIndex = checkMeridiem(input);
			if (meridiemIndex != -1) {
				input = input.substring(0,
						input.indexOf(meridiem[meridiemIndex]));
				if (input.length() == 1) {
					hour = Integer.parseInt(input);
					minute = 0;
				} else if (input.length() == 3) {
					hour = Integer.parseInt(input.substring(0, 1));
					minute = Integer.parseInt(input
							.substring(POS_OF_MINUTE - 1));
				} else if (input.length() == 4) {
					hour = Integer.parseInt(input.substring(0, 2));
					minute = Integer.parseInt(input.substring(POS_OF_MINUTE));
				} else {
					throw new InvalidInputException(INVALID_TIME_FORMAT);
				}
				if (meridiemIndex == 1) {
					hour += 12;
				}
				userTime = new Time(hour, minute);
			} else {
				String userHour = null;
				String userMinute = null;
				if ((input.length() == 1) || (input.length() == 2)) {
					userHour = input;
					userTime = new Time(Integer.parseInt(input));
				} else if (input.length() == NO_OF_CHAR_IN_HOUR_AND_MINUTE) {
					userHour = input.substring(0, POS_OF_MINUTE);
					userMinute = input.substring(POS_OF_MINUTE);
					userTime = new Time(Integer.parseInt(userHour),
							Integer.parseInt(userMinute));
				} else if (input.length() == 3) {
					userHour = input.substring(0, 1);
					userMinute = input.substring(1);
					userTime = new Time(Integer.parseInt(userHour));
				} else {
					throw new InvalidInputException(INVALID_TIME_FORMAT);
				}
			}
		} catch (NumberFormatException e) {
			return null;
		}
		return userTime;
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
	protected static String parseDateString(String input) throws InvalidInputException {
		input = input.toLowerCase();
		System.out.print(input);
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
		if (meridiemIndex != -1) {
			input = input.substring(0,
					input.indexOf(meridiem[meridiemIndex]));
			if (input.length() == 1) {
				userHour = input;
			} else if (input.length() == 3) {
				userHour = input.substring(0, 1);
				userMinute = input.substring(POS_OF_MINUTE - 1);
			} else if (input.length() == 4) {
				userHour = input.substring(0, 2);
				userMinute = input.substring(POS_OF_MINUTE - 2);
			}
			if (meridiemIndex == 1) {
				userHour = Integer.toString(Integer.parseInt(userHour) + 12);
			}
		} else {
			if (input.length() == 5 && input.contains(":")) {
				return input;
			} else {
				if ((input.length() == 1) || (input.length() == 2)) {
					userHour = input;
				} else if (input.length() == NO_OF_CHAR_IN_HOUR_AND_MINUTE) {
					userHour = input.substring(0, POS_OF_MINUTE);
					userMinute = input.substring(POS_OF_MINUTE);
				} else if (input.length() == 3) {
					userHour = input.substring(0, 1);
					userMinute = input.substring(1);
				}
			}
		}
		if (userHour.length() == 1) {
			userHour = "0" + userHour;
		}
		TimeString = userHour + ":" + userMinute;
		//System.out.print(TimeString);
		return TimeString;
	}
	
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
	
}
