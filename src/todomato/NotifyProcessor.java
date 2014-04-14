package todomato;

import hirondelle.date4j.DateTime;

//@author A0101324A
/**
 * 
 * This class contains method to change notification commands by the user. It
 * changes the time or date to notify the user.
 * 
 * <p>
 * It can process command that allows pop-up window at the new notification time
 * or date.
 * 
 * <p>
 * To change the notification of a task, start by typing "notify" followed by a
 * valid index. The following keywords are necessary for the attributes, while
 * the order is flexible:
 * <ul>
 * <li>"time" for the time
 * <li>"date" for the date
 * </ul>
 * 
 * <p>
 * Each keyword should be proceeded by the new attribute.
 * 
 * <p>
 * Examples:
 * <ul>
 * <li>"notify 1 time 1900"
 * </ul>
 * 
 * <p>
 * The following time formats are supported:
 * <ul>
 * <li>930am/pm
 * <li>9am/pm
 * <li>1230
 * <li>0730
 * <li>0730pm
 * </ul>
 */
public class NotifyProcessor extends Processor {

	private static String NOTIFIED = "Recorded the notified time.";
	private static String notifyKeyword = " time ";
	private static int NO_OF_CHAR_IN_NTIME = 6;
	private static String NO_DATE = "No start or end date for the selected task.";
	private static String LIMIT_TODAY = "Notification time is only for today's task!";
	private static String GOT_KEYWORDS = "Have you type notify <index> time <time>?";
	private static String INVALID_INDEX = "Index %d is out of the list.";
	private static String NOTHING_ERROR = "Please include the index(s) and keyword(s).";

	public static String processNotify(String argument)
			throws InvalidInputException {
		storeCurrentList();
		checkingInputErrors(argument);
		int index = getTaskIndex(argument) - 1;
		notifyTime(index, findDetailToEdit(argument), argument);
		return NOTIFIED;
	}

	// limited to today's task
	private static void notifyTime(int index, int which, String argument)
			throws InvalidInputException {
		if (list.getListItem(index).getEndDate() != null) {
			String item_date = convertDateToStandardForm(list
					.getListItem(index).getEndDate().getMonth().toString(),
					list.getListItem(index).getEndDate().getDay().toString());
			if (!item_date.contains(CurrentDate.date())) {
				throw new InvalidInputException(LIMIT_TODAY);
			}
			DateTime time = convertStringToDateTime(parseTimeString(argument
					.substring(which + NO_OF_CHAR_IN_NTIME)));
			list.getListItem(index).setNoticeTime(time);
			fileHandler.updateFile(list);
		} else {
			throw new InvalidInputException(NO_DATE);
		}
	}

	/**
	 * 
	 * @param argument
	 * @throws InvalidInputException
	 */
	private static void checkingInputErrors(String argument)
			throws InvalidInputException {
		if (argument.isEmpty()) {
			throw new InvalidInputException(NOTHING_ERROR);
		}
		String[] words = argument.split(" ");
		// checking index is zero
		if (words.length >= 1) {
			printInvalidIndexMsg(Integer.parseInt(words[0]), argument);
		} else if (words.length == 0) {
			printInvalidIndexMsg(0, null);
		} else {
			printInvalidKeywords(argument);
		}
	}

	/**
	 * @param argument
	 *            that contains index, time, date e.g. 1 time 1300 date 03/01
	 * @return the starting index of which task detail to change
	 */
	private static int findDetailToEdit(String argument) {
		int edit = -1;
		if (argument.contains(notifyKeyword)) {
			edit = argument.indexOf(notifyKeyword);
		}
		return edit;
	}

	/**
	 * @param index
	 * @throws InvalidInputException
	 */
	private static void printInvalidIndexMsg(int index, String argument)
			throws InvalidInputException {
		if ((index > list.getSize()) || (index <= 0) || argument.equals(null)) {
			String INDEX_OUT_BOUNDS = String.format(INVALID_INDEX, index);
			throw new InvalidInputException(INDEX_OUT_BOUNDS);
		} else {
			printInvalidKeywords(argument);
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

	/**
	 * @param argument
	 * @throws InvalidInputException
	 */
	private static void printInvalidKeywords(String argument)
			throws InvalidInputException {
		if (argument.length() <= 2) {
			throw new InvalidInputException(GOT_KEYWORDS);
		}
	}
}
