package todomato;

import hirondelle.date4j.DateTime;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains methods to process delete commands by the user.
 * It updates the user's lists of tasks, and saves it to disk.
 * 
 * <p>
 * The following ways to delete are supported:
 * <ul>
 * <li> delete a single task by specifying the index
 * <ul> <li> "delete 1" </ul>
 * <li> delete multiple tasks with indices in order
 * <ul> <li> "delete 1,2,3" </ul>
 * <li> delete all tasks
 * <ul> <li> "delete all" </ul>
 * <li> delete tasks that fall on a date
 * <ul> <li> "delete date 1 jan" </ul>
 * <li> delete tasks that are completed
 * <ul> <li> "delete completed" </ul>
 * </ul>
 *
 */
public class DeleteProcessor extends Processor {
	private static final String argDelimiter = "\\s*(,| )\\s*";
	private static final String ARGUMENT_DATE = "date";
	private static final String ARGUMENT_ALL = "all";
	private static final String ARGUMENT_COMPLETED = "completed";
	private static final String TASKS = " task(s)";
	private static final String SUCCESSFUL_DELETE = "Deleted: ";
	private static final String INVALID_INPUT_EMPTY_LIST = "empty list";
	private static final String INVALID_INPUT_MISSING_ARGUMENT = "missing argument";
	private static final String ERROR_MESSAGE_NUMBER_FORMAT = "Delete failed: Index not in number format";
	private static final String ERROR_MESSAGE_INDEX_OUT_OF_BOUND = "Delete failed: Index out of bound";
	
	private static final Logger logger = Logger.getLogger(DeleteProcessor.class.getName());
	
	
	/**
	 * @author linxuan
	 * @param argument
	 * @return String of success/error message accordingly 
	 */
	public static String processDelete(String argument) throws InvalidInputException {
		logger.log(Level.INFO, "processing delete");
		if (list.getSize() == 0) {
			logger.log(Level.INFO, "exited due to empty list");
			throw new InvalidInputException(INVALID_INPUT_EMPTY_LIST);
		}
		if (argument.isEmpty()) {
			logger.log(Level.WARNING, "exited due to missing argument");
			throw new InvalidInputException(INVALID_INPUT_MISSING_ARGUMENT);
		}
		
		storeCurrentList();
		
		String[] argArr = argument.split(argDelimiter);
		String argStr = argArr[0];
		String statusMessage;
		try {
			if (argStr.equalsIgnoreCase(ARGUMENT_ALL)) {
				statusMessage = SUCCESSFUL_DELETE + deleteAll() + TASKS; 
			}
			else if (argStr.equalsIgnoreCase(ARGUMENT_COMPLETED)) {
				statusMessage = SUCCESSFUL_DELETE + deleteCompleted() + TASKS;
			}
			else if (argStr.equalsIgnoreCase(ARGUMENT_DATE)) {
				statusMessage = SUCCESSFUL_DELETE + deleteDate(argArr) + TASKS;
			}
			else if (argArr.length > 1) {
				statusMessage = SUCCESSFUL_DELETE + deleteMultiple(argArr) + TASKS;
			}
			else {
				statusMessage = SUCCESSFUL_DELETE + deleteSingle(argStr);
			}
			/*
			if(argArr.length > 1) {
				if(argArr[0].equalsIgnoreCase(ARGUMENT_DATE)) {
					statusMessage = SUCCESSFUL_DELETE + deleteDate(argArr) + TASKS;
				} else {
					statusMessage = SUCCESSFUL_DELETE + deleteMultiple(argArr) + TASKS;
				}
			} else {
				if(argArr[0].equals(ARGUMENT_ALL)) {
					statusMessage = SUCCESSFUL_DELETE + deleteAll() + TASKS;
				} else {
					statusMessage = SUCCESSFUL_DELETE + deleteSingle(argArr[0]);
				} 
			}*/
			fileHandler.updateFile(list);

			displayList = list;
			
			logger.log(Level.INFO, "end of processing");
			return statusMessage;
		} catch(NumberFormatException e) {
			UndoProcessor.processUndo();
			logger.log(Level.WARNING, "exited due to non-number");
			return ERROR_MESSAGE_NUMBER_FORMAT;
		} catch(IndexOutOfBoundsException e) {
			UndoProcessor.processUndo();
			logger.log(Level.WARNING, "exited due to index out of bound");
			return ERROR_MESSAGE_INDEX_OUT_OF_BOUND;
		}		
	}
	
	private static String deleteCompleted() {
		// TODO Auto-generated method stub
		int numberOfTasksDeleted = 0;
		for (int i = 0; i < list.getSize(); i++) {
			if (list.getListItem(i).getCompleted()) {
				list.deleteListItem(i);
				numberOfTasksDeleted++;				
			}
		}
		return Integer.toString(numberOfTasksDeleted);
	}

	private static String deleteDate(String[] arg) {
		// TODO Auto-generated method stub
		int numberOfTasksDeleted = 0;
		String date = arg[1] + " " + arg[2];
		try {
			date = parseDateString(date);
		} catch (InvalidInputException e) {
			return INVALID_DATE;
		}
		DateTime dateDT = convertStringToDateTime(date);
		for (int i = list.getSize() - 1; i >= 0; i--) {
			if(isSameDate(i,dateDT)) {
				list.deleteListItem(i);
				numberOfTasksDeleted++;
			}
		}
		return Integer.toString(numberOfTasksDeleted);
	}

	private static boolean isSameDate(int i, DateTime dateDT) {
		// TODO Auto-generated method stub
		if (list.getListItem(i).getDate() == null) {
			return false;
		}
		if (list.getListItem(i).getDate().isSameDayAs(dateDT)) {
			return true;
		}
		return false;
	}

	private static String deleteAll() {
		int numberOfTasksDeleted = 0;
		while(list.getSize() != 0) {
			list.deleteListItem(0);
			numberOfTasksDeleted++;
		}
		return Integer.toString(numberOfTasksDeleted);
	}
	
	private static String deleteSingle(String indexString) {
		int index = Integer.parseInt(indexString) - 1;
		Task deletedTask = list.getListItem(index);
		list.deleteListItem(index);
		return deletedTask.toString();
	}
	
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
	
	private static void reverse(int[] intIndices) {
		int length = intIndices.length;
		for (int i = 0; i < length/2; i++) {
			int t = intIndices[i];
			intIndices[i] = intIndices[length - 1 - i];
			intIndices[length - 1 - i] = t;
		}
	}
}
