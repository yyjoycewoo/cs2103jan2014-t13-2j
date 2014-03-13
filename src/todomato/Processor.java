package todomato;

import java.io.IOException;
import java.util.Stack;

public class Processor {
	protected static String fileLoc = "C:\\Users\\Hao Eng\\Desktop\\test.txt";
	// "C:\\Users\\Joyce\\Documents\\Year 2\\test.txt";
	// protected static String fileLoc = "D:\\test.txt";
	protected static FileHandler fileHandler = new FileHandler(fileLoc);
	protected static TaskList list = fileHandler.readFile();
	protected static Stack<TaskList> oldLists = new Stack<TaskList>();
	protected static final int NO_OF_CHAR_IN_HOUR_AND_MINUTE = 4;
	protected static final int POS_OF_MINUTE = 2;
	protected static final String INVALID_TIME_FORMAT = "Invalid Date Format";
	protected static final int SPACE_NOT_FOUND = -1;

	protected static void storeCurrentList() {
		// store the current list before modifications for possible undo
		TaskList lastList = new TaskList();
		lastList.deepCopy(list);
		oldLists.push(lastList);
	}

	public static TaskList getList() {
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

	protected static Date parseDateFromString(String input)
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
			String[] parts = input.split(" ");
			String dateDelimiter = "/";
			String[] months = new String[] { "Jan", "Feb", "Mar", "Apr", "May",
					"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
			for (int i = 0; i < months.length; i++) {
				if (parts.length > 1) {
					if (parts[0].contains(months[i])) {
						String standardFormDate = convertDateToStandardForm(
								parts[1], String.valueOf(i + 1));
						Date userDate = parseDateFromString(standardFormDate);
						return userDate;
					} else if (parts[1].contains(months[i])) {
						String standardFormDate = convertDateToStandardForm(
								parts[0], String.valueOf(i + 1));
						Date userDate = parseDateFromString(standardFormDate);
						return userDate;
					}
				}
			}
			if (parts[0].contains(dateDelimiter)) {
				Date userDate = parseDateFromString(parts[0]);
				return userDate;
			}
		} catch (InvalidInputException e) {
			return null;
		}
		return null;
	}

	/**
	 * @author Daryl
	 * @param String
	 *            month, String day
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
				} else if (input.length() == 3) {
					hour = Integer.parseInt(input.substring(0, 1));
					minute = Integer.parseInt(input
							.substring(POS_OF_MINUTE - 1));
				} else if (input.length() == 4) {
					hour = Integer.parseInt(input.substring(0, 2));
					minute = Integer.parseInt(input.substring(POS_OF_MINUTE));
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

	protected static int checkMeridiem(String input) {
		String meridiems[] = new String[] { "am", "pm" };
		for (int i = 0; i < meridiems.length; i++) {
			if (input.contains(meridiems[i])) {
				return i;
			}
		}
		return -1;
	}
}
