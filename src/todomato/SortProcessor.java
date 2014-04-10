package todomato;

/**
 * This class contains methods to process sort commands by the user. 
 * It updates the user's list of tasks, and saves it to disk.
 * 
 * <p>
 * The following ways to sort are supported: 
 * <ul>
 * <li> sort tasks by date
 * <ul> <li> "sort startdate" </ul>
 * <ul> <li> "sort enddate" </ul>
 * <li> sort tasks by priority from high to low
 * <ul> <li> "sort priority" </ul>
 * <li> sort tasks by completion status
 * <ul> <li> "sort complete" </ul>
 * </ul>
 * 
 * <p> 
 * User may also specify order of sorting to be ascending or descending
 * by appending it to the end of the command.
 * 
 * <p>
 * The following notations for ascending order are allowed:
 * <ul>
 * <li> "a"
 * <li> "asc"
 * <li> "ascending"
 * </ul>
 * 
 * <p>
 * The following notations for descending order are allowed:
 * <ul>
 * <li> "d"
 * <li> "desc"
 * <li> "descending"
 * </ul>
 * 
 * @author A0101578H
 * 
 */

public class SortProcessor extends Processor{
	private static final String ARGUMENT_SORT_BY_START_DATE = "startdate";
	private static final String ARGUMENT_SORT_BY_DATE = "date";
	private static final String ARGUMENT_SORT_BY_PRIORITY = "priority";
	private static final String ARGUMENT_SORT_BY_COMPLETION = "complete";
	private static final String SUCCESS_SORT_BY_DATE = "Sorted by date";
	private static final String SUCCESS_SORT_BY_PRIORITY = "Sorted by priority";
	private static final String SUCCESS_SORT_BY_COMPLETION = "Sorted by completion status";
	private static final String INVALID_INPUT_MISSING_ARGUMENT = "Missing argument";
	private static final String INVALID_INPUT_ORDER = "Sorting order could not be determined";
	private static final String INVALID_INPUT_TYPE = "Sorting type could not be determined";
	
	private static final String descending[] = {"descending", "d", "desc"};
	private static final String ascending[] = {"ascending", "a", "asc"};
	
	public static String processSort(String argument) {
		if (argument.isEmpty()) {
			return INVALID_INPUT_MISSING_ARGUMENT;
		}
		
		String argArr[] = argument.split(" ", 2);
		String type = argArr[0];
		String order = "";
		if (argArr.length == 2) {
			order = argArr[1];
		}
		
		if (type.equalsIgnoreCase(ARGUMENT_SORT_BY_START_DATE)) {
			return sortByStartDate(order);
		}
		if (type.equalsIgnoreCase(ARGUMENT_SORT_BY_DATE)) {
			return sortByDate(order);
		}
		if (type.equalsIgnoreCase(ARGUMENT_SORT_BY_PRIORITY)) {
			return sortByPriority(order);
		}
		if (type.equalsIgnoreCase(ARGUMENT_SORT_BY_COMPLETION)) {
			return sortByCompletion(order);
		}
		
		return INVALID_INPUT_TYPE;
	}
	
	// Sort in order of..
	// Default/Ascending: unfinished -> completed tasks
	// Descending: completed -> unfinished tasks
	private static String sortByCompletion(String order) {
		if (!isValidOrder(order)) {
			return INVALID_INPUT_ORDER;
		}
		
		bubbleSort(ARGUMENT_SORT_BY_COMPLETION);
		if (isDescending(order)) {
			displayList.reverse();
		}
		fileHandler.updateFile(list);
		return SUCCESS_SORT_BY_COMPLETION;
	}

	// Sort in order of..
	// Default/Descending: High -> Medium -> Low priority
	// Ascending: Low -> Medium -> High priority
	private static String sortByPriority(String order) {
		if (!isValidOrder(order)) {
			return INVALID_INPUT_ORDER;
		}
		
		bubbleSort(ARGUMENT_SORT_BY_PRIORITY);
		if (isAscending(order)) {
			displayList.reverse();
		}
		//fileHandler.updateFile(list);
		return SUCCESS_SORT_BY_PRIORITY;
	}
	
	// Sort in order of..
	// Default/Ascending: Most recent -> Least recent -> No date
	// Descending: No date -> Least recent -> Most recent
	private static String sortByStartDate(String order) {
		if (!isValidOrder(order)) {
			return INVALID_INPUT_ORDER;
		}
		
		bubbleSort(ARGUMENT_SORT_BY_START_DATE);
		if (isDescending(order)) {
			displayList.reverse();
		}
		//fileHandler.updateFile(list);
		return SUCCESS_SORT_BY_DATE;
	}
	
	// Sort in order of..
	// Default/Ascending: Most recent -> Least recent -> No date
	// Descending: No date -> Least recent -> Most recent
	private static String sortByDate(String order) {
		if (!isValidOrder(order)) {
			return INVALID_INPUT_ORDER;
		}
		
		bubbleSort(ARGUMENT_SORT_BY_DATE);
		if (isDescending(order)) {
			displayList.reverse();
		}
		//fileHandler.updateFile(list);
		return SUCCESS_SORT_BY_DATE;
	}
	
	private static void bubbleSort(String type) {
		for (int i = 0; i < (displayList.getSize() - 1); i++) {
			for (int j = 0; j < (displayList.getSize() - i - 1); j++) {
				if (needSwap(type, j)) {
					displayList.swap(j, j + 1);
				}
			}
		}
	}

	private static boolean needSwap(String type, int j) {
		if (type.equals(ARGUMENT_SORT_BY_START_DATE)) {
			if (compareStartDate(j, j + 1)) {
				return true;
			}
		}
		if (type.equals(ARGUMENT_SORT_BY_DATE)) {
			if (compareDate(j, j + 1)) {
				return true;
			}
		}
		if (type.equals(ARGUMENT_SORT_BY_PRIORITY)) {
			if (comparePriority(j, j + 1)) {
				return true;
			}
		} 
		if (type.equals(ARGUMENT_SORT_BY_COMPLETION)) {
			if (compareCompletion(j, j + 1)) {
				return true;
			}
		} 
		return false;
	}
	
//	Returns true if..
//	list[i] is completed but list[j] is not
	private static boolean compareCompletion(int i, int j) {
		if (displayList.getListItem(i).getCompleted()) {
			if (!displayList.getListItem(j).getCompleted()) {
				return true;
			}
		}
		return false;
	}

//  Returns true if..
//  list[i] later than list[j]
//	Tasks without date is considered latest
	private static boolean compareStartDate(int i, int j) {
		if(displayList.getListItem(i).getStartDate() == null) {
			if(displayList.getListItem(j).getStartDate() == null) {
				return false;
			}
			return true;
		} if(displayList.getListItem(j).getStartDate() == null) {
			return false;
		} if(displayList.getListItem(i).getStartDate()
				.compareTo(displayList.getListItem(j).getStartDate()) > 0) {
			return true;
		} 
		return false;
	}
	
	
//  Returns true if..
//  list[i] later than list[j]
//	Tasks without date is considered latest
	private static boolean compareDate(int i, int j) {
		if(displayList.getListItem(i).getEndDate() == null) {
			if(displayList.getListItem(j).getEndDate() == null) {
				return false;
			}
			return true;
		} if(displayList.getListItem(j).getEndDate() == null) {
			return false;
		} if(displayList.getListItem(i).getEndDate()
				.compareTo(displayList.getListItem(j).getEndDate()) > 0) {
			return true;
		} 
		return false;
	}
	
//	Returns true if..
//	list[i] has lower priority than list[j]
	private static boolean comparePriority(int i, int j) {
		if(displayList.getListItem(i).getPriorityLevel().equals(PRIORITY_LOW)) {
			if(displayList.getListItem(j).getPriorityLevel().equals(PRIORITY_LOW)) {
				return false;
			}
			return true;
		}
		if (displayList.getListItem(i).getPriorityLevel().equals(PRIORITY_MED)) {
			if(displayList.getListItem(j).getPriorityLevel().equals(PRIORITY_HIGH)) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	private static boolean isAscending(String order) {
		for (String s: ascending) {
			if (order.equalsIgnoreCase(s)) {
				return true;				
			}
		}
		return false;
	}
	
	private static boolean isDescending(String order) {
		for (String s: descending) {
			if (order.equalsIgnoreCase(s)) {
				return true;				
			}
		}
		return false;
	}
	
	private static boolean isValidOrder(String order) {
		if (order.isEmpty()) {
			return true;
		}
		if (isAscending(order)) {
			return true;
		}
		if (isDescending(order)) {
			return true;
		}
		return false;
	}
}
