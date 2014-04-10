/**
 * 
 */
package todomato;

import hirondelle.date4j.DateTime;

//@author A0101324A
/**
 * This class contains methods to process update commands by the user. It
 * updates the user's lists of tasks, and saves it to disk.
 * 
 * <p>
 * It can process commands that update a description, a start time, an end time,
 * a date and a location. Any subset of the possible attributes can be updated.
 * 
 * <p>
 * To update a task, start by typing "update," followed by a valid index. The
 * following keywords are necessary for the attributes, while the order is
 * flexible:
 * <ul>
 * <li>"starttime" for the start time
 * <li>"endtime" for the end time
 * <li>"desc" for the description, followed by '\' afterwards
 * <li>"location" for the location, followed by '\' afterwards
 * <li>"date" for the date
 * <li>"recur" for recurrence interval
 * <li>"priority" or "!" for priority level
 * <li>"complete" for completion status
 * </ul>
 * 
 * <p>
 * Each keyword should be proceeded by the new attribute.
 * 
 * <p>
 * Examples:
 * <ul>
 * <li>"update 2 enddate 4 Jan"
 * <li>"update 2,3,4 !high"
 * <li>"update 1 starttime 1900 desc dinner with parents\ location home\
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
 * 
 * <p>
 * The following date formats are supported (case does not matter):
 * <ul>
 * <li>jan 1
 * <li>1 january
 * <li>january 1
 * </ul>
 * 
 */
public class UpdateProcessor extends Processor {
	private static final int NO_OF_CHAR_IN_STIME = 11;
	private static final int NO_OF_CHAR_IN_ETIME = 9;
	private static final int NO_OF_CHAR_IN_LOC = 10;
	private static final int NO_OF_CHAR_IN_SDATE = 11;
	private static final int NO_OF_CHAR_IN_DESC = 6;
	private static final int NO_OF_CHAR_IN_EDATE = 9;
	private static final int NO_OF_CHAR_IN_RECUR = 7;
	private static final int NO_OF_CHAR_IN_PRIORITY = 10;
	private static final String START_TIME_GT_END_TIME = "Start time cannot be greater than end time";
	private static final String INVALID_INDEX = "Invalid Index";
	private static final String NO_KEYWORDS_FOUND = "Please include any keywords to update i.e. starttime, endtime, location, desc, date";
	private static final String INVALID_RECUR = "Need Date before adding recurrence period";
	private static final String UPDATED = "Updated the task(s)";

	private static String[] updateKeywords = new String[] { " starttime ",
			" endtime ", " desc ", " startdate ", " enddate ", " location ",
			" recur ", " priority ", " complete", " !", " @" };

	/**
	 * @author Hao Eng
	 * @param argument
	 *            : <index of the task> time <startTime e.g. 1300> or <index of
	 *            the task> desc <description e.g. cut dog's hair>
	 * @return Updated Task
	 * @throws InvalidInputException
	 */
	public static String processUpdate(String argument)
			throws InvalidInputException {
		storeCurrentList();
		printInvalidKeywords(argument);
		int[] whichToEdit = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
		int[] indices = getTaskIndex(argument);
		for (int indice : indices) {
			int index = indice - 1;
			printInvalidIndexMsg(index);
			whichToEdit = findDetailToEdit(argument);
			updater(argument, whichToEdit, index);
			displayList.deepCopy(list);
		}
		return UPDATED;
	}

	/**
	 * @param argument
	 * @param whichToEdit
	 * @param index
	 * @throws InvalidInputException
	 */
	private static void updater(String argument, int[] whichToEdit, int index)
			throws InvalidInputException {
		for (int i = 0; i < whichToEdit.length; i++) {
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
					updateStartDate(index, whichToEdit[3], argument);
					break;
				case 4:
					updateEndDate(index, whichToEdit[4], argument);
					break;
				case 5:
					updateLocation(index, whichToEdit[5], argument);
					break;
				case 6:
					updateRecur(index, whichToEdit[6], argument);
					break;
				case 7:
					updatePriority(index, whichToEdit[7], argument);
					break;
				case 8:
					updateCompletion(index);
					break;
				case 9:
					// for !
					updatePriority(index, whichToEdit[8], argument);
					break;
				case 10:
					// for @
					updateLocation(index, whichToEdit[9], argument);
					break;
				}
			}
		}
	}

	/**
	 * @param index
	 * @throws InvalidInputException
	 */
	private static void printInvalidIndexMsg(int index)
			throws InvalidInputException {
		if (index >= list.getSize()) {
			throw new InvalidInputException("Index " + index
					+ " is out of the list.");
		}
	}

	/**
	 * @param argument
	 * @throws InvalidInputException
	 */
	private static void printInvalidKeywords(String argument)
			throws InvalidInputException {
		if (argument.length() <= 2) {
			throw new InvalidInputException(NO_KEYWORDS_FOUND);
		}
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
		argument = argument.substring(editStartTime + NO_OF_CHAR_IN_STIME);
		String[] parts = argument.split(" ");
		DateTime time = convertStringToDateTime(parseTimeString(parts[0]));
		list.getListItem(index).setStartTime(time);
		if (isStartTimeLessThanEndTime(list.getListItem(index))) {
			throw new InvalidInputException(START_TIME_GT_END_TIME);
		}
		fileHandler.updateFile(list);
		return list.getListItem(index);
	}

	private static Task updateEndTime(int index, int editEndTime,
			String argument) throws InvalidInputException {
		argument = argument.substring(editEndTime + NO_OF_CHAR_IN_ETIME);
		String[] parts = argument.split(" ");
		DateTime time = convertStringToDateTime(parseTimeString(parts[0]));
		list.getListItem(index).setEndTime(time);
		if (isStartTimeLessThanEndTime(list.getListItem(index))) {
			throw new InvalidInputException(START_TIME_GT_END_TIME);
		}
		fileHandler.updateFile(list);
		return list.getListItem(index);
	}

	private static Task updateStartDate(int index, int editDate, String argument)
			throws InvalidInputException {
		DateTime date = convertStringToDateTime(parseDateString(argument
				.substring(editDate + NO_OF_CHAR_IN_SDATE)));
		list.getListItem(index).setStartDate(date);
		// Ensures that any task with a start date has an end date
		if (list.getListItem(index).getEndDate() == null) {
			list.getListItem(index).setEndDate(date);
		}
		if (isStartTimeLessThanEndTime(list.getListItem(index))) {
			throw new InvalidInputException(START_TIME_GT_END_TIME);
		}
		fileHandler.updateFile(list);
		return list.getListItem(index);
	}

	private static Task updateEndDate(int index, int editDate, String argument)
			throws InvalidInputException {
		DateTime date = convertStringToDateTime(parseDateString(argument
				.substring(editDate + NO_OF_CHAR_IN_EDATE)));
		list.getListItem(index).setEndDate(date);
		if (isStartTimeLessThanEndTime(list.getListItem(index))) {
			throw new InvalidInputException(START_TIME_GT_END_TIME);
		}
		fileHandler.updateFile(list);
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
		if (argument.contains(" @")) {
			list.getListItem(index).setLocation(
					argument.substring(editLoc + 2, stopIndex));
		} else {
			list.getListItem(index).setLocation(
					argument.substring(editLoc + NO_OF_CHAR_IN_LOC, stopIndex));
		}
		fileHandler.updateFile(list);
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
		fileHandler.updateFile(list);
		return list.getListItem(index);
	}

	/**
	 * Updates task with the new recurrent period
	 * 
	 * @param index
	 * @param recurDesc
	 * @param argument
	 *            that contains recurrence period
	 * @return updated task
	 * @throws InvalidInputException
	 */

	private static Task updateRecur(int index, int recurDesc, String argument)
			throws InvalidInputException {
		int stopIndex = argument.length();
		int userRecurrence = list.getListItem(index).getRecurrencePeriod();
		try {
			userRecurrence = Integer.parseInt(argument.substring(recurDesc
					+ NO_OF_CHAR_IN_RECUR, stopIndex));
		} catch (NumberFormatException e) {
			return null;
		}
		if (list.getListItem(index).getEndDate() == null) {
			throw new InvalidInputException(INVALID_RECUR);
		}
		list.getListItem(index).setRecurrencePeriod(userRecurrence);
		fileHandler.updateFile(list);
		return list.getListItem(index);
	}

	/**
	 * Updates task with the new priority
	 * 
	 * @param index
	 * @param recurDesc
	 * @param argument
	 *            that contains priority
	 * @return updated task
	 */

	private static Task updatePriority(int index, int priorityDesc,
			String argument) {
		int stopIndex = argument.length();
		String priority = null;
		if (argument.contains(" !")) {
			priority = parsePriorityFromString(argument.substring(
					priorityDesc + 2, stopIndex));
		} else {
			priority = parsePriorityFromString(argument.substring(priorityDesc
					+ NO_OF_CHAR_IN_PRIORITY, stopIndex));
		}
		list.getListItem(index).setPriorityLevel(priority);
		fileHandler.updateFile(list);
		return list.getListItem(index);
	}

	/**
	 * Toggles task completion status
	 * 
	 * @param index
	 * @return updated tasks
	 */
	private static Task updateCompletion(int index) {
		if (list.getListItem(index).getCompleted()) {
			list.getListItem(index).setCompleted(false);
		} else {
			list.getListItem(index).setCompleted(true);
		}
		fileHandler.updateFile(list);
		return list.getListItem(index);
	}

	/**
	 * @param argument
	 *            that contains index, time, date e.g. 1 time 1300 date 03/01
	 * @return the starting index of which task detail to change
	 */
	private static int[] findDetailToEdit(String argument) {
		int[] edit = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
		for (int i = 0; i < edit.length; i++) {
			if (argument.contains(updateKeywords[i])) {
				edit[i] = argument.indexOf(updateKeywords[i]);
			}
		}
		return edit;
	}

	private static Boolean isStartTimeLessThanEndTime(Task input) {
		if ((input.getStartDate() != null) && (input.getEndDate() != null)) {
			if (input.getStartDate().gt(input.getEndDate())) {
				return true;
			}
			if (input.getStartDate().equals(input.getEndDate())) {
				if ((input.getStartTime() != null)
						&& (input.getEndTime() != null)) {
					if (input.getStartTime().gt(input.getEndTime())) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * @param argument
	 * @return task index
	 * @throws NumberFormatException
	 * @throws InvalidInputException
	 */
	private static int[] getTaskIndex(String argument)
			throws NumberFormatException, InvalidInputException {
		int spaceAfterIndex = argument.indexOf(" ");
		// no index found
		if (spaceAfterIndex == NOT_FOUND) {
			throw new InvalidInputException(INVALID_INDEX);
		}
		// split different index
		String[] index = argument.substring(0, spaceAfterIndex).split(",");
		int[] indices = new int[index.length];

		System.out.println("indices len: " + indices.length);

		// only single index
		if (index.length == 1) {
			if (!isParseableByInt(index[0])) {
				throw new InvalidInputException(INVALID_INDEX);
			} else {
				indices[0] = Integer.parseInt(index[0]);
				return indices;
			}
		}
		// multiple index
		else {

			// change all strings to integers
			for (int i = 0; i < index.length; i++) {
				indices[i] = Integer.parseInt(index[i]);
			}
			return indices;
		}
	}
}
