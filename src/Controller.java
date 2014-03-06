import java.io.IOException;
import java.util.regex.Pattern;

/**
 * For the commands such as add, delete, update, display so on
 * 
 * 
 */
public class Controller {

	// " at "
	private static final int NO_OF_CHAR_IN_AT = 4;
	private static final int NO_OF_CHAR_IN_HOUR_AND_MINUTE = 4;
	private static final int SPACE_NOT_FOUND = -1;
	private static final int POS_OF_MINUTE = 2;
	// " time "
	private static final int noOfCharInSTime = 11, noOfCharInETime = 9,
			noOfCharInLoc = 10, noOfCharInDesc = 6, noOfCharInDate = 6;
	private static String fileLoc = // "D:\\test.txt";
	"C:\\Users\\Hao Eng\\Desktop\\test.txt";
	private static FileHandler fileHandler = new FileHandler(fileLoc);
	private static TaskList list = fileHandler.readFile();
	private static String[] keywords = new String[] { " at ", " from ", " in ",
			" due " };
	private static String[] updateKeywords = new String[] { " starttime ",
			" endtime ", " desc ", " date ", " location " };

	/**
	 * @author Daryl
	 * @param input
	 * @return Task
	 * @throws IOException
	 * 
	 * 
	 * 
	 *             private static final int POS_OF_DATE = 3; private static
	 *             final int POS_OF_TIME = 2; private static final int
	 *             NO_OF_PARTS_WITH_DATE_AND_TIME = 4; private static final int
	 *             NO_OF_PARTS_WITH_TIME = 3; private static final int
	 *             AT_NOT_FOUND = 0; public static Task processAdd(String input)
	 *             { // TODO Auto-generated method stub String taskDes =
	 *             getTaskDes(input); Task userTask = null; if
	 *             (checkForAtPosition(input) == AT_NOT_FOUND) { userTask = new
	 *             Task(taskDes); } else { input =
	 *             input.substring(checkForAtPosition(input)); String[] parts =
	 *             input.split(" "); if (parts.length ==
	 *             NO_OF_PARTS_WITH_DATE_AND_TIME) { Date userDate =
	 *             getDate(parts[POS_OF_DATE]); Time userTime =
	 *             getTime(parts[POS_OF_TIME]); userTask = new Task(taskDes,
	 *             userTime, userDate);
	 * 
	 *             } else if (parts.length == NO_OF_PARTS_WITH_TIME) { Time
	 *             userTime = getTime(parts[POS_OF_TIME]); userTask = new
	 *             Task(taskDes, userTime); } }
	 *             System.out.print(userTask.toString());
	 *             list.addToList(userTask); list =
	 *             fileHandler.updateFile(list); return userTask; }
	 */

	public static Task processAdd(String input) {
		boolean taskDesExtracted = false;
		int keywordIndex = -1;
		Task userTask = null;
		String location = null;
		String startTimeString = null;
		String endTimeString = null;
		String[] stringFragments = null;
		String taskDes = null;
		Date userDate = null;
		if (!checkForKeywords(input)) {
			taskDes = input;
			userTask = new Task(taskDes);
		} else {
			while (checkForKeywords(input)) {
				keywordIndex = getFirstKeyword(input);
				stringFragments = splitByKeyword(input, keywords[keywordIndex]);
				if (!taskDesExtracted) {
					taskDes = stringFragments[0];
					userTask = new Task(taskDes);
					taskDesExtracted = true;
				}
				input = stringFragments[1];
				int spaceIndex = stringFragments[1].indexOf(" ");
				if (spaceIndex == SPACE_NOT_FOUND) {
					switch (keywordIndex) {
					case 0:
						startTimeString = stringFragments[1];
						break;
					case 1:
						endTimeString = stringFragments[1];
						break;
					case 2:
						location = stringFragments[1];
						break;
					}
				} else {
					switch (keywordIndex) {
					case 0:
						startTimeString = stringFragments[1].substring(0,
								spaceIndex);
						userDate = retrieveDateInTimeString(stringFragments[1]);
						break;
					case 1:
						endTimeString = stringFragments[1].substring(0,
								spaceIndex);
						break;
					case 2:
						location = stringFragments[1].substring(0, spaceIndex);
						break;
					}
				}
			}
			Time endUserTime = getTime(endTimeString);
			Time startUserTime = getTime(startTimeString);
			userTask.setEndTime(endUserTime);
			userTask.setStartTime(startUserTime);
			userTask.setDate(userDate);
			userTask.setLocation(location);
		}
		list.addToList(userTask);
		// list = fileHandler.updateFile(list);
		return userTask;
	}

	public static Date retrieveDateInTimeString(String input) {
		String[] parts = input.split(" ");
		String dateDelimiter = "/";
		if (parts[1].contains(dateDelimiter)) {
			Date userDate = getDate(parts[1]);
			return userDate;
		}
		return null;
	}

	public static Boolean checkForKeywords(String input) {
		for (String keyword : keywords) {
			if (input.contains(keyword)) {
				return true;
			}
		}
		return false;
	}

	public static int getFirstKeyword(String input) {
		int firstKeywordPos = input.length();
		int firstKeyword = -1;

		for (int i = 0; i < keywords.length; i++) {
			if (input.contains(keywords[i])) {
				if (input.indexOf(keywords[i]) < firstKeywordPos) {
					firstKeywordPos = input.indexOf(keywords[i]);
					firstKeyword = i;
				}
			}
		}
		return firstKeyword;
	}

	public static String[] splitByKeyword(String input, String keyword) {
		String[] splitWords = null;
		Pattern pattern = Pattern.compile(Pattern.quote(keyword));
		splitWords = pattern.split(input);

		return splitWords;
	}

	/**
	 * @author linxuan
	 * @param argument
	 * @return task at index
	 */
	public static Task processDelete(String argument) {
		int index = Integer.parseInt(argument) - 1;
		Task deletedTask = list.getListItem(index);
		list.deleteListItem(index);
		fileHandler.updateFile(list);
		return deletedTask;
	}

	/**
	 * @author linxuan
	 * @return TaskList
	 */
	public static TaskList processDisplay() {
		return list;
	}

	/**
	 * @author Hao Eng
	 * @param argument
	 *            : <index of the task> time <startTime e.g. 1300> or <index of
	 *            the task> desc <description e.g. cut dog's hair>
	 * @return Updated Task
	 */
	public static Task processUpdate(String argument) {
		int[] whichToEdit = { -1, -1, -1, -1, -1 };
		int index = getTaskIndex(argument) - 1;
		whichToEdit = findDetailToEdit(argument);
		for (int i = 0; i < 5; i++) {
			if (whichToEdit[i] != -1) {
				switch (i) {
				case 0:
					updateStartTime(index, whichToEdit[0], argument);
					break;
				case 1:
					updateEndTime(index, whichToEdit[1], argument);
					break;
				case 2:
					updateDesc(index, whichToEdit[2], argument);
					break;
				case 3:
					updateDate(index, whichToEdit[3], argument);
					break;
				case 4:
					updateLocation(index, whichToEdit[4], argument);
					break;
				}
			}
		}
		return list.getListItem(index);
	}

	/**
	 * @param index
	 * @param editTime
	 * @param argument
	 *            that contains new time 1300
	 * @return updated task
	 */
	private static Task updateStartTime(int index, int editStartTime,
			String argument) {
		int time = Integer.parseInt(argument.substring(editStartTime
				+ noOfCharInSTime, editStartTime + noOfCharInSTime + 4));
		int hr = time / 100;
		int min = time % 100;
		list.getListItem(index).setStartTime(new Time(hr, min));
		return list.getListItem(index);
	}

	private static Task updateEndTime(int index, int editEndTime,
			String argument) {
		int time = Integer.parseInt(argument.substring(editEndTime
				+ noOfCharInETime, editEndTime + noOfCharInETime + 4));
		int hr = time / 100;
		int min = time % 100;
		list.getListItem(index).setEndTime(new Time(hr, min));
		return list.getListItem(index);
	}

	private static Task updateDate(int index, int editDate, String argument) {
		String date = argument.substring(editDate + noOfCharInDate, editDate
				+ noOfCharInDate + 5);
		String[] tokens = date.split("/");
		int day = Integer.parseInt(tokens[0]);
		int mth = Integer.parseInt(tokens[1]);
		if (tokens.length == 2) {
			list.getListItem(index).setDate(new Date(day, mth));
		}
		return list.getListItem(index);
	}

	private static Task updateLocation(int index, int editLoc, String argument) {
		int stopIndex = argument.length();
		if (argument.contains("\\")) {
			int escChar = argument.indexOf("\\");
			if (escChar < editLoc) {
				stopIndex = argument.lastIndexOf("\\");
			} else {
				stopIndex = escChar;
			}
		}
		list.getListItem(index).setLocation(
				argument.substring(editLoc + noOfCharInLoc, stopIndex));
		return list.getListItem(index);
	}

	/**
	 * @param index
	 * @param editDesc
	 * @param argument
	 *            that contains new description
	 * @return updated task
	 */
	private static Task updateDesc(int index, int editDesc, String argument) {
		int stopIndex = argument.length();
		if (argument.contains("\\")) {
			int escChar = argument.indexOf("\\");
			if (escChar < editDesc) {
				stopIndex = argument.lastIndexOf("\\");
			} else {
				stopIndex = escChar;
			}
		}
		list.getListItem(index).setDescription(
				argument.substring(editDesc + noOfCharInDesc, stopIndex));
		return list.getListItem(index);
	}

	/**
	 * @param argument
	 *            that contains index, time, date e.g. 1 time 1300 date 03/01
	 * @return the starting index of which task detail to change
	 */
	private static int[] findDetailToEdit(String argument) {
		int[] edit = { -1, -1, -1, -1, -1 };
		for (int i = 0; i < 5; i++) {
			if (argument.contains(updateKeywords[i])) {
				edit[i] = argument.indexOf(updateKeywords[i]);
			}
		}
		return edit;
	}

	/**
	 * @param argument
	 * @return task index
	 * @throws NumberFormatException
	 */
	private static int getTaskIndex(String argument)
			throws NumberFormatException {
		int spaceAfterIndex = argument.indexOf(" ");
		return (Integer.parseInt(argument.substring(0, spaceAfterIndex)));
	}

	public static int checkForAtPosition(String input) {
		String checkForAt = " at ";
		if (input.indexOf(checkForAt) == -1) {
			return 0;
		} else {
			return input.indexOf(checkForAt);
		}
	}

	/**
	 * Retrieves date from user input of the form "19/10"
	 * 
	 * @param input
	 * @return userDate
	 */

	public static Date getDate(String input) {
		String delims = "/";
		String[] tokens = input.split(delims);
		if (tokens.length == 2) {
			Date userDate = new Date(Integer.parseInt(tokens[0]),
					Integer.parseInt(tokens[1]));
			return userDate;
		} else if (tokens.length == 3) {
			Date userDate = new Date(Integer.parseInt(tokens[0]),
					Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
			return userDate;
		} else {
			return null;
		}

	}

	/**
	 * Retrieves the task description from user input
	 * 
	 * @param input
	 *            the raw user input after the command word
	 * @return string with the task description
	 */

	public static String getTaskDes(String input) {
		String TaskDes = "";
		int index = checkForAtPosition(input);
		TaskDes = input.substring(0, index);
		return TaskDes;
	}

	/**
	 * Retrieves the time and date from user input
	 * 
	 * @param input
	 *            the raw user input after command word
	 * @return string with time and date
	 */

	public static String getTimeAndDate(String input) {
		String checkForAt = " at ";
		int index = input.indexOf(checkForAt);
		input = input.substring(index + NO_OF_CHAR_IN_AT);
		return input;
	}

	/**
	 * Returns time from user string i.e. 19 hours 30 minutes from "1930"
	 * Returns time from user string i.e. 19 hours 30 minutes from "1930"
	 * 
	 * @param input
	 * @return userTime
	 */

	public static Time getTime(String input) {
		if (input == null) {
			return null;
		}
		String userHour = input.substring(0, POS_OF_MINUTE);
		Time userTime = null;
		if (input.length() == NO_OF_CHAR_IN_HOUR_AND_MINUTE) {
			String userMinute = input.substring(POS_OF_MINUTE);
			userTime = new Time(Integer.parseInt(userHour),
					Integer.parseInt(userMinute));
		} else {
			userTime = new Time(Integer.parseInt(userHour));
		}
		return userTime;
	}

}
