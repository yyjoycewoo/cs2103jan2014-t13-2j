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
	private static final int POS_OF_MINUTE = 2;
	private static final int POS_OF_DATE = 3;
	private static final int POS_OF_TIME = 2;
	private static final int NO_OF_PARTS_WITH_DATE_AND_TIME = 4;
	private static final int NO_OF_PARTS_WITH_TIME = 3;
	private static final int AT_NOT_FOUND = 0;
	// " time "
	private static final int noOfCharInTime = 6, noOfCharInDesc = 6;
	private static final String INVALID_UPDATE = "No parameter to edit.";
	private static boolean timeFlag = false, descFlag = false;
	private static String fileLoc = "D:\\test.txt"; // TODO
	private static FileHandler fileHandler = new FileHandler(fileLoc);
	private static TaskList list = fileHandler.readFile();

	/**
	 * @author Daryl
	 * @param input
	 * @return Task
	 * @throws IOException
	 */
	public static Task processAdd(String input) {
		// TODO Auto-generated method stub
		String[] parts = input.split(" ");
		String taskDes = getTaskDes(input);
		Task userTask;
		if (parts.length == noOfPartsWithDateandTime) {
			Date userDate = getDate(parts[posOfDate]);
			Time userTime = getTime(parts[posOfTime]);
			userTask = new Task(taskDes, userTime, userDate);
		Task userTask = null;
		if (checkForAtPosition(input) == AT_NOT_FOUND) {
			userTask = new Task(taskDes);
		}
		else {
			input = input.substring(checkForAtPosition(input));
			String[] parts = input.split(" ");
			if (parts.length == NO_OF_PARTS_WITH_DATE_AND_TIME) {
				Date userDate = getDate(parts[POS_OF_DATE]);
				Time userTime = getTime(parts[POS_OF_TIME]);
				userTask = new Task(taskDes, userTime, userDate);
				
			} else if (parts.length == NO_OF_PARTS_WITH_TIME) {
				Time userTime = getTime(parts[POS_OF_TIME]);
				userTask = new Task(taskDes, userTime);
			}
		}

		list.addToList(userTask);
		//list = fileHandler.updateFile(list);
		return userTask;
	}
	
	public static int checkForKeywords(String input) {
		String[] keywords = new String[] {" at ", " from ", " in ", " due "};
		for (int i = 0; i < keywords.length; i++) {
			if (input.contains(keywords[i])) {
				return i;
			}
		}
		return -1;
	}
	
	public static Task processAdd2(String input) {
		String[] keywords = new String[] {" at ", " from ", " in ", "due "};
		String[] data = null;
		Task userTask = null;
		if (checkForKeywords(input) == -1) {
			String taskDes = input;
			userTask = new Task(taskDes);
		}
		else {
			data = splitByKeyword (input, keywords[checkForKeywords(input)]);
			String taskDes = data[0];
		}
		/*
		for (int i = 0; i < keywords.length; i++) {
			if (input.contains(keywords[i])) {
				data = splitByKeyword(input, keywords[i]);
			}
		}*/
		return userTask;
	}
	
	public static int checkWhichKeywordIsFirst(String input) {
		String[] keywords = new String[] {" at ", " from ", " in ", " due "};
		//int[] keywordpos = null;
		int firstKeywordPos = input.length();
		int firstKeyword = -1;
		
		for (int i = 0; i < keywords.length; i++) {
			if (input.contains(keywords[i])) {
				if(input.indexOf(keywords[i]) < firstKeywordPos) {
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
	 * @author Hao Eng
	 * @param argument
	 *            : <index of the task> time <startTime e.g. 1300> or <index of
	 *            the task> desc <description e.g. cut dog's hair>
	 * @return Updated Task
	 */
	public static Task processUpdate(String argument) {
		int index = getTaskIndex(argument);
		int whichToEdit = findDetailToEdit(argument);
		if (whichToEdit == -1) {
			printInvalidEdit();
		} else {
			if (timeFlag) {
				return updateTime(index, whichToEdit, argument);
			}
			if (descFlag) {
				return updateDesc(index, whichToEdit, argument);
			}
		}
		return null;
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
	 * @param index
	 * @param editTime
	 * @param argument
	 *            that contains new time 1300
	 * @return updated task
	 */
	private static Task updateTime(int index, int editTime, String argument) {
		int time = Integer.parseInt(argument.substring(editTime
				+ noOfCharInTime));
		int hr = time / 100;
		int min = time % 100;
		list.getListItem(index).setStartTime(new Time(hr, min));
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
		list.getListItem(index).setDescription(
				argument.substring(editDesc + noOfCharInDesc));
		return list.getListItem(index);
	}

	private static Task printInvalidEdit() {
		System.out.println(INVALID_UPDATE);
		return null;
	}

	/**
	 * @param argument
	 *            that contains index, time, date e.g. 1 time 1300 date 03/01
	 * @return the starting index of which task detail to change
	 */
	private static int findDetailToEdit(String argument) {
		int editTime = argument.indexOf(" time ");
		if (editTime > -1) {
			timeFlag = true;
			return editTime;
		} else {
			int editDesc = argument.indexOf(" desc ");
			if (editDesc > -1) {
				descFlag = true;
				return editDesc;
			} else {
				return -1;
			}
		}
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
	 * Returns time from user string
	 * i.e. 19 hours 30 minutes from "1930"
	 * Returns time from user string i.e. 19 hours 30 minutes from "1930"
	 * 
	 * @param input
	 * @return userTime
	 */

	public static Time getTime(String input) {
		String userHour = input.substring(0, POS_OF_MINUTE);
		String userMinute = input.substring(POS_OF_MINUTE);
		Time userTime = new Time(Integer.parseInt(userHour),
				Integer.parseInt(userMinute));
		return userTime;
	}

}

