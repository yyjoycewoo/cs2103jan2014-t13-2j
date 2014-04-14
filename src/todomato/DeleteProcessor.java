package todomato;

import hirondelle.date4j.DateTime;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

//@author A0101578H
/**
 * This class contains methods to process delete commands by the user.
 * It updates the user's lists of tasks, and saves it to disk.
 * 
 * <p>
 * The following ways to delete are supported:
 * <ul>
 * <li> delete a single task by specifying the index
 * <ul> <li> "delete 1" </ul>
 * <li> delete multiple tasks
 * <ul> <li> "delete 1,2,3" </ul>
 * <li> delete a range of tasks
 * <ul> <li> "delete 1-3" </ul> 
 * <li> delete all tasks
 * <ul> <li> "delete all" </ul>
 * <li> delete tasks that starts on a date
 * <ul> <li> "delete startdate 1 jan" </ul>
 * <li> delete tasks that ends on a date
 * <ul> <li> "delete enddate 20 jan" </ul>
 * <li> delete tasks that are completed
 * <ul> <li> "delete completed" </ul>
 * </ul>
 *
 *
 */
public class DeleteProcessor extends Processor {
	private static final String DELIMITER_FOR_ARGUMENT = "\\s*(,| )\\s*";
	private static final String DELIMITER_FOR_RANGE = "\\s*-\\s*";
	private static final String IDENTIFIER_RANGE = "-";
	private static final String ARGUMENT_START_DATE = "startdate";
	private static final String ARGUMENT_END_DATE = "enddate";
	private static final String ARGUMENT_ALL = "all";
	private static final String ARGUMENT_COMPLETE = "complete";
	private static final String TASKS = " task(s)";
	private static final String SUCCESSFUL_DELETE = "Deleted: ";
	private static final String INVALID_INPUT_EMPTY_LIST = "empty list";
	private static final String INVALID_INPUT_MISSING_ARGUMENT = "Missing argument";
	private static final String INVALID_NUMBER_OF_LIMITS = "Invalid range: Upper and lower limits required";
	private static final String INVALID_RANGE_LIMITS = "Invalid range: Enter <lower index> - <higher index>";
	private static final String ERROR_MESSAGE_NUMBER_FORMAT = "Delete failed: Index not in number format";
	private static final String ERROR_MESSAGE_INDEX_OUT_OF_BOUND = "Delete failed: Index out of bound";
	
	private static final int INDEX_OF_TYPE = 0;
	private static final int INDEX_OF_DATE_1 = 1;
	private static final int INDEX_OF_DATE_2 = 2;
	private static final int INDEX_OF_LOWER_LIMIT = 0;
	private static final int INDEX_OF_UPPER_LIMIT = 1;
	private static final int NUMBER_OF_LIMITS = 2;
	//private static final Logger logger = Logger.getLogger(DeleteProcessor.class.getName());
	
	
	/**
	 * Method for processing different ways to delete tasks
	 * 
	 * @param argument: user-specified
	 * @return success message 
	 */
	public static String processDelete(String argument) throws InvalidInputException {
		//logger.log(Level.INFO, "processing delete");
		if (list.getSize() == 0) {
			//logger.log(Level.INFO, "exited due to empty list");
			throw new InvalidInputException(INVALID_INPUT_EMPTY_LIST);
		}
		if (argument.isEmpty()) {
			//logger.log(Level.WARNING, "exited due to missing argument");
			throw new InvalidInputException(INVALID_INPUT_MISSING_ARGUMENT);
		}
		storeCurrentList();
		String[] argArr = argument.split(DELIMITER_FOR_ARGUMENT);
		String argType = argArr[INDEX_OF_TYPE];
		argType.toLowerCase();
		String statusMessage;
		try {
			if (argType.equalsIgnoreCase(ARGUMENT_ALL)) {
				statusMessage = SUCCESSFUL_DELETE + deleteAll() + TASKS; 
			}
			else if (argType.contains(ARGUMENT_COMPLETE)) {
				statusMessage = SUCCESSFUL_DELETE + deleteCompleted() + TASKS;
			}
			else if (argType.equalsIgnoreCase(ARGUMENT_START_DATE)) {
				statusMessage = SUCCESSFUL_DELETE + deleteStartDate(argArr) + TASKS;
			}
			else if (argType.equalsIgnoreCase(ARGUMENT_END_DATE)) {
				statusMessage = SUCCESSFUL_DELETE + deleteEndDate(argArr) + TASKS;
			}
			else if (argument.contains(IDENTIFIER_RANGE)) {
				statusMessage = SUCCESSFUL_DELETE + deleteRange(argument) + TASKS;
			}
			else if (argArr.length > 1) {
				statusMessage = SUCCESSFUL_DELETE + deleteMultiple(argArr) + TASKS;
			}
			else {
				statusMessage = SUCCESSFUL_DELETE + deleteSingle(argType);
			}
			displayList = list;
			fileHandler.updateFile(list);
			//logger.log(Level.INFO, "end of processing");
			return statusMessage;
		} catch(NumberFormatException e) {
			UndoProcessor.processUndo();
			//logger.log(Level.WARNING, "exited due to non-number");
			return ERROR_MESSAGE_NUMBER_FORMAT;
		} catch(IndexOutOfBoundsException e) {
			UndoProcessor.processUndo();
			//logger.log(Level.WARNING, "exited due to index out of bound");
			return ERROR_MESSAGE_INDEX_OUT_OF_BOUND;
		}		
	}

	/**
	 * Method for deleting a range of tasks
	 * 
	 * @param argument: contains user-specified range
	 * @return number of tasks deleted
	 * @throws InvalidInputException
	 */
	private static String deleteRange(String arg) throws InvalidInputException {
		//logger.log(Level.INFO, "processing delete range");
		String[] rangeLimits = arg.split(DELIMITER_FOR_RANGE);
		if (!(rangeLimits.length == NUMBER_OF_LIMITS)) {
			throw new InvalidInputException(INVALID_NUMBER_OF_LIMITS);
		}
		//logger.log(Level.INFO, "number of limits valid");
		int lowerLimitIndex = Integer.parseInt(rangeLimits[INDEX_OF_LOWER_LIMIT]) - 1;
		int upperLimitIndex = Integer.parseInt(rangeLimits[INDEX_OF_UPPER_LIMIT]) - 1;
		int numberOfTasksDeleted = 0;
		if (lowerLimitIndex > upperLimitIndex) {
			throw new InvalidInputException(INVALID_RANGE_LIMITS);
		}
		for (int i = upperLimitIndex; i >= lowerLimitIndex; i--) {
			list.deleteListItem(i);
			numberOfTasksDeleted++;
		}
		return Integer.toString(numberOfTasksDeleted);
	}

	/**
	 * Method for deleting all completed tasks
	 * 
	 * @return number of tasks deleted
	 */
	private static String deleteCompleted() {
		int numberOfTasksDeleted = 0;
		for (int i = list.getSize() - 1; i >= 0 ; i--) { 
			if (list.getListItem(i).getCompleted()) {
				list.deleteListItem(i);
				numberOfTasksDeleted++;
			}		
		}
		return Integer.toString(numberOfTasksDeleted);
	}

	/**
	 * Method for deleting all tasks that starts on a date
	 * 
	 * @param arg: contains user-specified date
	 * @return number of tasks deleted
	 */
	private static String deleteStartDate(String[] arg) {
		int numberOfTasksDeleted = 0;
		String date = arg[INDEX_OF_DATE_1] + " " + arg[INDEX_OF_DATE_2];
		try {
			date = parseDateString(date);
		} catch (InvalidInputException e) {
			return INVALID_DATE;
		}
		DateTime dateDT = convertStringToDateTime(date);
		for (int i = list.getSize() - 1; i >= 0; i--) {
			if(isSameStartDate(i,dateDT)) {
				list.deleteListItem(i);
				numberOfTasksDeleted++;
			}
		}
		return Integer.toString(numberOfTasksDeleted);
	}
	
	/**
	 * Method for checking if a task's startdate is same as a specified date
	 * 
	 * @param i: user-specified index of task
	 * @param dateDT: date to be checked against
	 * @return true if task's startdate is the same
	 */
	private static boolean isSameStartDate(int i, DateTime dateDT) {
		if (list.getListItem(i).getStartDate() == null) {
			return false;
		}
		if (list.getListItem(i).getStartDate().isSameDayAs(dateDT)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Method for deleting all tasks that ends on a specified date
	 * 
	 * @param arg: contains user-specified date
	 * @return number of tasks deleted
	 */
	private static String deleteEndDate(String[] arg) {
		int numberOfTasksDeleted = 0;
		String date = arg[INDEX_OF_DATE_1] + " " + arg[INDEX_OF_DATE_2];
		try {
			date = parseDateString(date);
		} catch (InvalidInputException e) {
			return INVALID_DATE;
		}
		DateTime dateDT = convertStringToDateTime(date);
		for (int i = list.getSize() - 1; i >= 0; i--) {
			if(isSameEndDate(i,dateDT)) {
				list.deleteListItem(i);
				numberOfTasksDeleted++;
			}
		}
		return Integer.toString(numberOfTasksDeleted);
	}

	/**
	 * Method for checking if a task's enddate is same as a specified date
	 * 
	 * @param i: user-specified index of task 
	 * @param dateDT: date to be checked against
	 * @return true if task has the same date
	 */
	private static boolean isSameEndDate(int i, DateTime dateDT) {
		if (list.getListItem(i).getEndDate() == null) {
			return false;
		}
		if (list.getListItem(i).getEndDate().isSameDayAs(dateDT)) {
			return true;
		}
		return false;
	}

	/**
	 * Method for deleting all tasks
	 * 
	 * @return number of tasks deleted
	 */
	private static String deleteAll() {
		int numberOfTasksDeleted = 0;
		while(list.getSize() != 0) {
			list.deleteListItem(0);
			
			numberOfTasksDeleted++;
		}
		return Integer.toString(numberOfTasksDeleted);
	}
	
	/**
	 * Method for deleting a single task 
	 * 
	 * @param indexStr: user-specified index of task to be deleted
	 * @return task details of deleted task
	 */
	private static String deleteSingle(String indexStr) {
		int index = Integer.parseInt(indexStr) - 1;
		Task deletedTask = list.getListItem(index);
		list.deleteListItem(index);
		return deletedTask.toString();
	}
	
	/**
	 * Method for deleting multiple tasks
	 * 
	 * @param strIndices: user-specified indices of tasks to be deleted
	 * @return number of tasks deleted
	 */
	private static String deleteMultiple(String[] strIndices) {
		int[] intIndices = new int[strIndices.length];
		for (int i = 0; i < strIndices.length; i++) {
			intIndices[i] = Integer.parseInt(strIndices[i]);
		}
		Arrays.sort(intIndices);
		reverse(intIndices);
		for (int i = 0; i < intIndices.length; i++) {
			list.deleteListItem(intIndices[i] - 1);
		}
		return Integer.toString(strIndices.length);
	}
	
	/**
	 * Method for reversing order in an array
	 * 
	 * @param arr: array to be reversed
	 */
	private static void reverse(int[] arr) {
		int length = arr.length;
		for (int i = 0; i < length/2; i++) {
			int t = arr[i];
			arr[i] = arr[length - 1 - i];
			arr[length - 1 - i] = t;
		}
	}
}
