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
	private static final int SPACE_NOT_FOUND = -1;
	private static final String ARGUMENT_CLEAR_ALL = "all";

	//private static String fileLoc = "C:\\Users\\Hao Eng\\Desktop\\test.txt";
	// "C:\\Users\\Joyce\\Documents\\Year 2\\test.txt";
	private static String fileLoc = "D:\\test.txt";
	private static FileHandler fileHandler = new FileHandler(fileLoc);
	private static TaskList list = fileHandler.readFile();
	private static Stack<TaskList> oldLists = new Stack<TaskList>();
	private static String[] keywords = new String[] { " at ", " from ",
			" until ", " to ", " in ", " due " };

	/**
	 * @author Daryl
	 * @param input
	 * @return Task
	 * @throws InvalidInputException
	 * @throws NumberFormatException
	 * @throws IOException
	 */

	public static Task processAdd(String input) throws NumberFormatException,
			InvalidInputException {
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
		}
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
					if (!isTimePresent(startTimeString)) {
						userDate = Processor
								.retrieveDateStringFromInput(startTimeString);
					}
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
					if (isTimePresent(stringFragments[1])) {
						if (getFirstKeyword(stringFragments[1]) == -1) {
							startTimeString = stringFragments[1].substring(0,
									spaceIndex);
						} else {
							startTimeString = stringFragments[1].substring(0,
									spaceIndex);
							userDate = Processor
									.retrieveDateStringFromInput(stringFragments[1]
											.substring(spaceIndex + 1));
						}
					} else {
						if (getFirstKeyword(stringFragments[1]) != -1) {
							userDate = Processor
									.retrieveDateStringFromInput(getWordsBeforeNextKeyword(
											stringFragments[1],
											keywords[getFirstKeyword(stringFragments[1])]));
						} else {
							userDate = Processor
									.retrieveDateStringFromInput(stringFragments[1]);
						}
					}
					break;
				case 2:
				case 3:
					if (isTimePresent(stringFragments[1])) {
						if (getFirstKeyword(stringFragments[1]) == -1) {
							endTimeString = stringFragments[1].substring(0,
									spaceIndex);
						} else {
							endTimeString = stringFragments[1].substring(0,
									spaceIndex);
							userDate = Processor
									.retrieveDateStringFromInput(stringFragments[1]
											.substring(spaceIndex));
						}
					} else {
						userDate = Processor
								.retrieveDateStringFromInput(getWordsBeforeNextKeyword(
										stringFragments[1],
										keywords[getFirstKeyword(stringFragments[1])]));
					}
					break;
				case 4:
					if (getFirstKeyword(stringFragments[1]) == -1) {
						location = stringFragments[1];
					} else {
						location = getWordsBeforeNextKeyword(
								stringFragments[1],
								keywords[getFirstKeyword(stringFragments[1])]);
					}
					break;
				case 5:
					endTimeString = stringFragments[1].substring(0, spaceIndex);
					break;
				}
			}
			Time endUserTime = Processor.parseTimeFromString(endTimeString);
			Time startUserTime = Processor.parseTimeFromString(startTimeString);
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
		// store the current list before modifications for possible undo
		TaskList lastList = new TaskList();
		lastList.deepCopy(list);
		oldLists.push(lastList);
	}

	public static Date retrieveDateInTimeString(String input)
			throws NumberFormatException, InvalidInputException {
		String[] parts = input.split(" ");
		String dateDelimiter = "/";
		String[] months = new String[] { "Jan", "Feb", "Mar", "Apr", "May",
				"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
		for (int i = 0; i < months.length; i++) {
			if (parts.length > 2) {
				if (parts[1].contains(months[i])) {
					String standardFormDate = Processor
							.convertDateToStandardForm(parts[2],
									String.valueOf(i + 1));
					Date userDate = Processor
							.parseDateFromStandardForm(standardFormDate);
					return userDate;
				} else if (parts[2].contains(months[i])) {
					String standardFormDate = Processor
							.convertDateToStandardForm(parts[1],
									String.valueOf(i + 1));
					Date userDate = Processor
							.parseDateFromStandardForm(standardFormDate);
					return userDate;
				}
			}
		}
		if (parts[1].contains(dateDelimiter)) {
			Date userDate = Processor.parseDateFromStandardForm(parts[1]);
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
		if (indices.length > 1) {
			return deleteMultiple(indices);
		} else {
			if (indices[0].equals(ARGUMENT_CLEAR_ALL)) {
				return deleteAll();
			} else {
				return deleteSingleTask(indices[0]);
			}
		}
	}

	private static TaskList deleteAll() {
		TaskList deletedTasks = new TaskList();
		while (list.getSize() != 0) {
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
		} catch (NumberFormatException e) {
			// TODO
			return null;
		} catch (IndexOutOfBoundsException e) {
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

	private static Boolean isTimePresent(String input) {
		try {
			Processor.parseTimeFromString(input);
		} catch (NumberFormatException e1) {
			return false;
		} catch (InvalidInputException e2) {
			return false;
		}
		return true;
	}

	/**
	 * Undo last action, if possible
	 * 
	 * @author Joyce
	 * @return String representation of list, or status message if no changes to
	 *         undo
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

	public static TaskList getList() {
		return list;
	}

}
