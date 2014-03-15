package todomato;

import java.util.Arrays;

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
 * </ul>
 *
 */
public class DeleteProcessor extends Processor {
	private static final String indicesDelimiter = "\\s*(,| )\\s*";
	private static final String ARGUMENT_CLEAR_ALL = "all";
	private static final String TASKS = " tasks";
	private static final String SUCCESSFUL_DELETE = "Deleted: ";
	private static final String INVALID_INPUT_EMPTY_LIST = "empty list";
	private static final String ERROR_MESSAGE_NUMBER_FORMAT = "Delete failed: Index not in number format";
	private static final String ERROR_MESSAGE_INDEX_OUT_OF_BOUND = "Delete failed: Index out of bound";
	
	/**
	 * Allowed format:
	 * delete <index>
	 * delete <index>,<index>,<index> (unwanted space ignored)
	 * delete <index> <index> (unwanted space ignored)
	 * delete all
	 * @author linxuan
	 * @param argument
	 * @return String of success/error message accordingly 
	 */
	public static String processDelete(String argument) throws InvalidInputException {
		if (list.getSize() == 0) {
			throw new InvalidInputException(INVALID_INPUT_EMPTY_LIST);
		}
		
		storeCurrentList();
		
		String[] indices = argument.split(indicesDelimiter);
		try {	
			if(indices.length > 1) {
				return SUCCESSFUL_DELETE + deleteMultiple(indices) + TASKS;
			} else {
				if(indices[0].equals(ARGUMENT_CLEAR_ALL)) {
					return SUCCESSFUL_DELETE + deleteAll() + TASKS;
				} else {
					return SUCCESSFUL_DELETE + deleteSingleTask(indices[0]);
				} 
			}
		} catch(NumberFormatException e) {
			// TODO
			return ERROR_MESSAGE_NUMBER_FORMAT;
		} catch(IndexOutOfBoundsException e) {
			// TODO
			return ERROR_MESSAGE_INDEX_OUT_OF_BOUND;
		}
	}
	
	private static String deleteAll() {
		//TaskList deletedTasks = new TaskList();
		int numberOfTasksDeleted = 0;
		while(list.getSize() != 0) {
			//deletedTasks.addToList(list.getListItem(0));
			list.deleteListItem(0);
			numberOfTasksDeleted++;
		}
		fileHandler.updateFile(list);
		//return deletedTasks;
		return Integer.toString(numberOfTasksDeleted);
	}
	
	private static String deleteSingleTask(String indexString) {
		//TaskList deletedTasks = new TaskList();
		int index = Integer.parseInt(indexString) - 1;
		//deletedTasks.addToList(list.getListItem(index));
		TaskDT deletedTask = list.getListItem(index);
		list.deleteListItem(index);
		fileHandler.updateFile(list);
		return deletedTask.toString();
	}
	
	private static String deleteMultiple(String[] strIndices) {
		//TaskList deletedTasks = new TaskList();
		int[] intIndices = new int[strIndices.length];
		for (int i = 0; i < strIndices.length; i++) {
			intIndices[i] = Integer.parseInt(strIndices[i]);
			//deletedTasks.addToList(list.getListItem(index));
			//list.deleteListItem(index);
		}
		Arrays.sort(intIndices);
		reverse(intIndices);
		for (int i = 0; i < intIndices.length; i++) {
			list.deleteListItem(intIndices[i] - 1);
		}
		fileHandler.updateFile(list);
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
