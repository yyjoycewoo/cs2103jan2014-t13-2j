/**
 * 
 */
package todomato;

import hirondelle.date4j.DateTime;

/**
 * This class contains methods to process update commands by the user.
 * It updates the user's lists of tasks, and saves it to disk.
 * 
 * <p>
 * It can process commands that update a description, a start time, an end
 * time, a date and a location. Any subset of the possible attributes
 * can be updated.
 * 
 * <p>
 * To update a task, start by typing "update," followed by a valid index.
 * The following keywords are necessary for the attributes, while the order
 * is flexible:
 * <ul>
 * <li>"starttime" for the start time
 * <li>"endtime" for the end time
 * <li>"desc" for the description, followed by '\' afterwards
 * <li>"location" for the location, followed by '\' afterwards
 * <li> "date" for the date
 * </ul>
 * 
 * <p>
 * Each keyword should be proceeded by the new attribute.
 * 
 * <p>
 * Examples:
 * <ul>
 * <li>"update 2 date 04/01/14"
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
 * <li>DD/MM
 * <li>DD/MM/YY
 * </ul>
 * 
 * @author Hao Eng
 * 
 */
public class UpdateProcessor extends Processor {
	private static final int NO_OF_CHAR_IN_STIME = 11;
	private static final int NO_OF_CHAR_IN_ETIME = 9;
	private static final int NO_OF_CHAR_IN_LOC = 10;
	private static final int NO_OF_CHAR_IN_DESC = 6;
	private static final int NO_OF_CHAR_IN_DATE = 6;

	private static String[] updateKeywords = new String[] { " starttime ",
			" endtime ", " desc ", " date ", " location " };

	/**
	 * @author Hao Eng
	 * @param argument
	 *            : <index of the task> time <startTime e.g. 1300> or <index of
	 *            the task> desc <description e.g. cut dog's hair>
	 * @return Updated Task
	 * @throws InvalidInputException
	 */
	public static TaskDT processUpdate(String argument)
			throws InvalidInputException {
		storeCurrentList();
		printInvalidKeywords(argument);
		int[] whichToEdit = { -1, -1, -1, -1, -1 };
		int index = getTaskIndex(argument) - 1;
		printInvalidIndexMsg(index);
		whichToEdit = findDetailToEdit(argument);
		updater(argument, whichToEdit, index);

		return list.getListItem(index);
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
					updateDate(index, whichToEdit[3], argument);
					break;
				case 4:
					updateLocation(index, whichToEdit[4], argument);
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
			throw new InvalidInputException(
					"Please include any keywords to update i.e. starttime, endtime, location, desc, date");
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
	private static TaskDT updateStartTime(int index, int editStartTime,
			String argument) throws InvalidInputException {
		DateTime time = convertStringToDateTime(parseTimeStringFromInput(argument
				.substring(editStartTime + NO_OF_CHAR_IN_STIME)));
		list.getListItem(index).setStartTime(time);
		fileHandler.updateFile(list);
		return list.getListItem(index);
	}

	private static TaskDT updateEndTime(int index, int editEndTime,
			String argument) throws InvalidInputException {
		DateTime time = convertStringToDateTime(parseTimeStringFromInput(argument.substring(editEndTime
				+ NO_OF_CHAR_IN_ETIME)));
		list.getListItem(index).setEndTime(time);
		fileHandler.updateFile(list);
		return list.getListItem(index);
	}

	private static TaskDT updateDate(int index, int editDate, String argument)
			throws InvalidInputException {
		DateTime date = convertStringToDateTime(parseDateString(argument.substring(editDate
				+ NO_OF_CHAR_IN_DATE)));
		list.getListItem(index).setDate(date);
		fileHandler.updateFile(list);
		return list.getListItem(index);
	}

	private static TaskDT updateLocation(int index, int editLoc, String argument) {
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
	private static TaskDT updateDesc(int index, int editDesc, String argument) {
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
}
