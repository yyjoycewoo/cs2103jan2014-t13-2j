package todomato;

import java.io.IOException;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * For the commands such as add, delete, update, display so on
 * 
 * 
 */
public class Controller {

	private static final String NO_CHANGES_TO_UNDO_MSG = "No changes to undo";
	// " at "
	private static final int NO_OF_CHAR_IN_HOUR_AND_MINUTE = 4;
	private static final int SPACE_NOT_FOUND = -1;
	private static final int POS_OF_MINUTE = 2;
	// " time "
	private static final int NO_OF_CHAR_IN_STIME = 11;
	private static final int NO_OF_CHAR_IN_ETIME = 9;
	private static final int NO_OF_CHAR_IN_LOC = 10;
	private static final int NO_OF_CHAR_IN_DESC = 6;
	private static final int NO_OF_CHAR_IN_DATE = 6;
	private static final String ARGUMENT_CLEAR_ALL = "all";
	//"C:\\Users\\Hao Eng\\Desktop\\test.txt";
	private static String fileLoc = "C:\\Users\\Joyce\\Documents\\Year 2\\test.txt";
	private static FileHandler fileHandler = new FileHandler(fileLoc);
	private static TaskList list = fileHandler.readFile();
	private static Stack<TaskList> oldLists = new Stack<TaskList>();
	private static String[] keywords = new String[] { " at ", " from ", " until ", " to ", " in ",
			" due "};
	private static String[] updateKeywords = new String[] { " starttime ",
			" endtime ", " desc ", " date ", " location " };

	/**
	 * @author Daryl
	 * @param input
	 * @return Task
	 * @throws InvalidInputException 
	 * @throws NumberFormatException 
	 * @throws IOException
	 */

	public static Task processAdd(String input) throws NumberFormatException, InvalidInputException {
		storeCurrentList();
		
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
					case 1:
						startTimeString = stringFragments[1];
						break;
					case 2:
					case 3:
						endTimeString = stringFragments[1];
						break;
					case 4:
						location = stringFragments[1];
						break;
					case 5:
						endTimeString = stringFragments[1];
						break;
					}
				} else {
					switch (keywordIndex) {
					case 0:
					case 1:
						startTimeString = stringFragments[1].substring(0,
								spaceIndex);
						userDate = retrieveDateInTimeString(stringFragments[1]);
						break;
					case 2:
					case 3:
						endTimeString = stringFragments[1].substring(0,
								spaceIndex);
						userDate = retrieveDateInTimeString(stringFragments[1]);
						break;
					case 4:
						if (getFirstKeyword(stringFragments[1]) == -1) {
							location = stringFragments[1];
						} else {
						location = getWordsBeforeNextKeyword(stringFragments[1], 
								keywords[getFirstKeyword(stringFragments[1])]);
						}
						break;
					case 5:
						endTimeString = stringFragments[1].substring(0,
								spaceIndex);
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
		list = fileHandler.updateFile(list);
		return userTask;
	}

	private static void storeCurrentList() {
		//store the current list before modifications for possible undo
		TaskList lastList = new TaskList();
		lastList.deepCopy(list);
		oldLists.push(lastList);
	}
	
	/**
	 * @author Daryl
	 * @param String month, String day
	 * 
	 * @return userDate
	 * @throws IOException
	 */

	public static String convertDateToStandardForm (String month, String day) {
		return month + "/" + day;
	}

	public static Date retrieveDateInTimeString(String input) throws NumberFormatException, InvalidInputException {
		String[] parts = input.split(" ");
		String dateDelimiter = "/";
		String[] months = new String[]{"Jan", "Feb", "Mar", "Jun", "Jul",
				"Aug", "Sep", "Oct", "Nov", "Dec"};
		for (int i = 0; i < months.length; i++) {
			if (parts.length > 2) {
				if (parts[1].contains(months[i])) {
					String standardFormDate = 
							convertDateToStandardForm(parts[2], String.valueOf(i+1));
					Date userDate = getDate(standardFormDate);
					return userDate;
				}
				else if (parts[2].contains(months[i])) {
					String standardFormDate = 
							convertDateToStandardForm(parts[1], String.valueOf(i+1));
					Date userDate = getDate(standardFormDate);
					return userDate;
				}
			}
		}
		if (parts[1].contains(dateDelimiter)) {
			Date userDate = getDate(parts[1]);
			return userDate;
		}
		return null;
	}
	
	private static String getWordsBeforeNextKeyword(String input, String keyword) {
		String wordsBeforeNextKeyword;
		wordsBeforeNextKeyword = input.substring(0, input.indexOf(keyword));
		return wordsBeforeNextKeyword;
	}

	private static Boolean checkForKeywords(String input) {
		for (String keyword : keywords) {
			if (input.contains(keyword)) {
				return true;
			}
		}
		return false;
	}

	private static int getFirstKeyword(String input) {
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

	private static String[] splitByKeyword(String input, String keyword) {
		String[] splitWords = null;
		Pattern pattern = Pattern.compile(Pattern.quote(keyword));
		splitWords = pattern.split(input);

		return splitWords;
	}

	/**
	 * @author linxuan
	 * @param argument
	 * @return TaskList containing deleted tasks
	 */
	public static TaskList processDelete(String argument) {
		storeCurrentList();
		
		String[] indices = argument.split(",");
		if(indices.length > 1) {
			return deleteMultiple(indices);
		} else {
			if(indices[0].equals(ARGUMENT_CLEAR_ALL)) {
				return deleteAll();
			} else {
				return deleteSingleTask(indices[0]);
			} 
		}
	}
	
	private static TaskList deleteAll() {
		TaskList deletedTasks = new TaskList();
		while(list.getSize() != 0) {
			deletedTasks.addToList(list.getListItem(0));
			list.deleteListItem(0);
		}
		fileHandler.updateFile(list);
		return deletedTasks;
	}
	
	private static TaskList deleteSingleTask(String indexString) {
		try {
			TaskList deletedTasks = new TaskList();
			int index = Integer.parseInt(indexString) - 1;
			deletedTasks.addToList(list.getListItem(index));
			list.deleteListItem(index);
			fileHandler.updateFile(list);
			return deletedTasks;
		} catch(NumberFormatException e) {
			// TODO
			return null;
		} catch(IndexOutOfBoundsException e) {
			// TODO
			return null;
		}
	}
	
	private static TaskList deleteMultiple(String[] indices) {
		try {
			TaskList deletedTasks = new TaskList();
			for (int i = 0; i < indices.length; i++) {
				int index = Integer.parseInt(indices[i]) - i - 1;
				deletedTasks.addToList(list.getListItem(index));
				list.deleteListItem(index);
			}
			fileHandler.updateFile(list);
			return deletedTasks;
		} catch (NumberFormatException e) {
			// TODO
			return null;
		} catch (IndexOutOfBoundsException e) {
			// TODO
			return null;
		}
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
	 * @throws InvalidInputException 
	 */
	public static Task processUpdate(String argument) throws InvalidInputException {
		storeCurrentList();
		
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
	 * @throws InvalidInputException 
	 */
	private static Task updateStartTime(int index, int editStartTime,
			String argument) throws InvalidInputException {
		int time = Integer.parseInt(argument.substring(editStartTime
				+ NO_OF_CHAR_IN_STIME, editStartTime + NO_OF_CHAR_IN_STIME + 4));
		int hr = time / 100;
		int min = time % 100;
		list.getListItem(index).setStartTime(new Time(hr, min));
		return list.getListItem(index);
	}

	private static Task updateEndTime(int index, int editEndTime,
			String argument) throws InvalidInputException {
		int time = Integer.parseInt(argument.substring(editEndTime
				+ NO_OF_CHAR_IN_ETIME, editEndTime + NO_OF_CHAR_IN_ETIME + 4));
		int hr = time / 100;
		int min = time % 100;
		list.getListItem(index).setEndTime(new Time(hr, min));
		return list.getListItem(index);
	}

	private static Task updateDate(int index, int editDate, String argument) throws InvalidInputException {
		String date = argument.substring(editDate + NO_OF_CHAR_IN_DATE, editDate
				+ NO_OF_CHAR_IN_DATE + 5);
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
				argument.substring(editLoc + NO_OF_CHAR_IN_LOC, stopIndex));
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
				argument.substring(editDesc + NO_OF_CHAR_IN_DESC, stopIndex));
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

	/**
	 * Retrieves date from user input of the form "19/10"
	 * 
	 * @param input
	 * @return userDate
	 * @throws InvalidInputException 
	 * @throws NumberFormatException 
	 */

	private static Date getDate(String input) throws NumberFormatException, InvalidInputException {
		String delims = "/";
		String[] dateTokens = input.split(delims);
		if (dateTokens.length == 2) {
			Date userDate = new Date(Integer.parseInt(dateTokens[0]),
					Integer.parseInt(dateTokens[1]));
			return userDate;
		} else if (dateTokens.length == 3) {
			Date userDate = new Date(Integer.parseInt(dateTokens[0]),
					Integer.parseInt(dateTokens[1]), Integer.parseInt(dateTokens[2]));
			return userDate;
		} else {
			return null;
		}

	}

	/**
	 * Returns time from user string i.e.
	 * 19 hours 30 minutes from "1930" or "730pm"
	 * 
	 * @param input
	 * @return userTime
	 * @throws InvalidInputException 
	 */

	private static Time getTime(String input) throws InvalidInputException {
		if (input == null) {
			return null;
		}
		Time userTime = null;
		int hour = 0, minute = -1;
		String meridiem[] = new String[] { "am", "pm"};
		int meridiemIndex = checkMeridiem(input); 
		if (meridiemIndex != -1) {
			input = input.substring(0,input.indexOf(meridiem[meridiemIndex]));
			if (input.length() == 1) {
				hour = Integer.parseInt(input);
			} else if (input.length() == 3) {
				hour = Integer.parseInt(input.substring(0,1));
				minute = Integer.parseInt(input.substring(POS_OF_MINUTE-1));
			} else if (input.length() == 4) {
				hour = Integer.parseInt(input.substring(0,2));
				minute = Integer.parseInt(input.substring(POS_OF_MINUTE));
			}
			if (meridiemIndex == 1) {
				hour += 12;
			}
			userTime = new Time(hour, minute);
		}
		else {
			String userHour = input.substring(0, POS_OF_MINUTE);
			if (input.length() == NO_OF_CHAR_IN_HOUR_AND_MINUTE) {
				String userMinute = input.substring(POS_OF_MINUTE);
				userTime = new Time(Integer.parseInt(userHour),
						Integer.parseInt(userMinute));
			} else {
				userTime = new Time(Integer.parseInt(userHour));
			}
		}
		return userTime;
	}
	
	private static int checkMeridiem(String input) {
		String meridiems[] = new String[] {"am", "pm"};
		for (int i = 0; i < meridiems.length; i++) {
			if (input.contains(meridiems[i])) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Undo last action, if possible
	 * @author Joyce
	 * @return String representation of list, or status message if no changes to undo
	 */
	public static String processUndo() {
		if (!oldLists.isEmpty()) {
			list = oldLists.pop();	
			fileHandler.updateFile(list);
			return list.toString();
		} else {
			return NO_CHANGES_TO_UNDO_MSG;
		}
	}
		
}
