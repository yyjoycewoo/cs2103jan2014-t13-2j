//@author A0101578H
package todomato;

import hirondelle.date4j.DateTime;

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
 * 
 */

public class SortProcessor extends Processor{
	private static final String ARGUMENT_START_DATE = "startdate";
	private static final String ARGUMENT_END_DATE = "enddate";
	private static final String ARGUMENT_PRIORITY = "priority";
	private static final String ARGUMENT_COMPLETION = "complete";
	private static final String SUCCESS_SORT_BY_DATE = "Sorted by date";
	private static final String SUCCESS_SORT_BY_PRIORITY = "Sorted by priority";
	private static final String SUCCESS_SORT_BY_COMPLETION = "Sorted by completion status";
	private static final String INVALID_INPUT_MISSING_ARGUMENT = "Missing argument";
	private static final String INVALID_INPUT_ORDER = "Sorting order could not be determined";
	private static final String INVALID_INPUT_TYPE = "Sorting type could not be determined";
	
	private static final String descending[] = {"descending", "d", "desc"};
	private static final String ascending[] = {"ascending", "a", "asc"};
	private static final int INVALID_PARAMETERS = -1;
	
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
		
		if (type.equalsIgnoreCase(ARGUMENT_START_DATE)) {
			return sortByStartDate(order);
		}
		if (type.equalsIgnoreCase(ARGUMENT_END_DATE)) {
			return sortByEndDate(order);
		}
		if (type.equalsIgnoreCase(ARGUMENT_PRIORITY)) {
			return sortByPriority(order);
		}
		if (type.equalsIgnoreCase(ARGUMENT_COMPLETION)) {
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
		
		bubbleSort(ARGUMENT_COMPLETION);
		if (isDescending(order)) {
			list.reverse();
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
		
		bubbleSort(ARGUMENT_PRIORITY);
		if (isAscending(order)) {
			list.reverse();
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
		
		bubbleSort(ARGUMENT_START_DATE);
		if (isDescending(order)) {
			list.reverse();
		}
		//fileHandler.updateFile(list);
		return SUCCESS_SORT_BY_DATE;
	}
	
	// Sort in order of..
	// Default/Ascending: Most recent -> Least recent -> No date
	// Descending: No date -> Least recent -> Most recent
	private static String sortByEndDate(String order) {
		if (!isValidOrder(order)) {
			return INVALID_INPUT_ORDER;
		}
		
		bubbleSort(ARGUMENT_END_DATE);
		if (isDescending(order)) {
			list.reverse();
		}
		//fileHandler.updateFile(list);
		return SUCCESS_SORT_BY_DATE;
	}
	
	private static void bubbleSort(String type) {
		for (int i = 0; i < (list.getSize() - 1); i++) {
			for (int j = 0; j < (list.getSize() - i - 1); j++) {
				if (needSwap(type, j)) {
					list.swap(j, j + 1);
				}
			}
		}
	}

	private static boolean needSwap(String type, int j) {
		if (type.equals(ARGUMENT_START_DATE)) {
			if (compareStartDate(j, j + 1)) {
				return true;
			}
		}
		if (type.equals(ARGUMENT_END_DATE)) {
			if (compareEndDate(j, j + 1)) {
				return true;
			}
		}
		if (type.equals(ARGUMENT_PRIORITY)) {
			if (comparePriority(j, j + 1)) {
				return true;
			}
		} 
		if (type.equals(ARGUMENT_COMPLETION)) {
			if (compareCompletion(j, j + 1)) {
				return true;
			}
		} 
		return false;
	}
	
//	Returns true if..
//	list[i] is completed but list[j] is not
	private static boolean compareCompletion(int i, int j) {
		if (list.getListItem(i).getCompleted()) {
			if (!list.getListItem(j).getCompleted()) {
				return true;
			}
		}
		return false;
	}

	// Returns true if..
	// list[i] starts later than list[j]
	// Tasks without date is considered latest
	// Tasks that are completed will also be ranked as latest
	private static boolean compareStartDate(int i, int j) {
		if (list.getListItem(i).getCompleted()) {
			if (!list.getListItem(j).getCompleted()) {
				return true;
			}
		}
		if (list.getListItem(j).getCompleted()) {
			return false;
		}
		if (hasSameDate(ARGUMENT_START_DATE, i, j)) {
			if (getLaterTimeTask(ARGUMENT_START_DATE, i, j) == i) {
				return true;
			}
			return false;
		}
		if (getLaterDateTask(ARGUMENT_START_DATE, i ,j) == i) {
			return true;
		}
		return false;
	}
	
	// Returns true if..
	// list[i] ends later than list[j]
	// Tasks without date is considered latest
	// Tasks that are completed will also be ranked as latest
	private static boolean compareEndDate(int i, int j) {
		if (list.getListItem(i).getCompleted()) {
			if (!list.getListItem(j).getCompleted()) {
				return true;
			}
		}
		if (list.getListItem(j).getCompleted()) {
			return false;
		}
		if (hasSameDate(ARGUMENT_END_DATE, i, j)) {
			if (getLaterTimeTask(ARGUMENT_END_DATE, i, j) == i) {
				return true;
			}
			return false;
		}
		if (getLaterDateTask(ARGUMENT_END_DATE, i ,j) == i) {
			return true;
		}
		return false;
	}
	
//	Returns true if..
//	list[i] has lower priority than list[j]
	private static boolean comparePriority(int i, int j) {
		if (list.getListItem(i).getCompleted()) {
			if (list.getListItem(j).getCompleted()) {
				return false;
			}
			return true;
		}
		if (list.getListItem(i).getPriorityLevel().equals(PRIORITY_LOW)) {
			if (list.getListItem(j).getPriorityLevel().equals(PRIORITY_LOW)) {
				return false;
			}
			return true;
		}
		if (list.getListItem(i).getPriorityLevel().equals(PRIORITY_MED)) {
			if(list.getListItem(j).getPriorityLevel().equals(PRIORITY_HIGH)) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	private static boolean hasSameDate(String type, int i, int j) {
		if (type.equals(ARGUMENT_START_DATE)) {
			DateTime iStartDate = list.getListItem(i).getStartDate();
			DateTime jStartDate = list.getListItem(j).getStartDate();
			if (iStartDate == null && jStartDate == null) {
				return true;
			}
			if (iStartDate == null || jStartDate == null) {
				return false;
			}
			if (iStartDate.isSameDayAs(jStartDate)) {
				return true;
			}
		}
		if (type.equals(ARGUMENT_END_DATE)) {
			DateTime iEndDate = list.getListItem(i).getEndDate();
			DateTime jEndDate = list.getListItem(j).getEndDate();
			if (iEndDate == null && jEndDate == null) {
				return true;
			}
			if (iEndDate == null || jEndDate == null) {
				return false;
			}
			if (iEndDate.isSameDayAs(jEndDate)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Compares two tasks by either start or end time
	 * If the times are the same, j will be returned
	 * 
	 * @param type
	 * @param i
	 * @param j
	 * @return later task of the i'th and j'th tasks
	 *  
	 */
	private static int getLaterTimeTask(String type, int i, int j) {
		if (type.equals(ARGUMENT_START_DATE)) {
			DateTime iStartTime = list.getListItem(i).getStartTime();
			DateTime jStartTime = list.getListItem(j).getStartTime();
			
			if (iStartTime == null) {
				if (jStartTime == null) {
					return j;
				}
				return i;
			}
			if (jStartTime == null) {
				return j;
			}
			if (jStartTime.gteq(iStartTime)) {
				return j;
			}
			return i;
		}
		if (type.equals(ARGUMENT_END_DATE)) {
			DateTime iEndTime = list.getListItem(i).getEndTime();
			DateTime jEndTime = list.getListItem(j).getEndTime();
			
			if (iEndTime == null) {
				if (jEndTime == null) {
					return j;
				}
				return i;
			}
			if (jEndTime == null) {
				return j;
			}
			if (jEndTime.gteq(iEndTime)) {
				return j;
			}
			return i;
		}
		return INVALID_PARAMETERS;
	}
	
	/**
	 * Compares two tasks by either start or end date
	 * If the dates are the same, j will be returned
	 * 
	 * @param type
	 * @param i
	 * @param j
	 * @return later task of the i'th and j'th tasks
	 *  
	 */
	private static int getLaterDateTask(String type, int i, int j) {
		if (type.equals(ARGUMENT_START_DATE)) {
			DateTime iStartDate = list.getListItem(i).getStartDate();
			DateTime jStartDate = list.getListItem(j).getStartDate();
			
			if (iStartDate == null) {
				if (jStartDate == null) {
					return j;
				}
				return i;
			}
			if (jStartDate == null) {
				return j;
			}
			if (jStartDate.gteq(iStartDate)) {
				return j;
			}
			return i;
		}
		if (type.equals(ARGUMENT_END_DATE)) {
			DateTime iEndDate = list.getListItem(i).getEndDate();
			DateTime jEndDate = list.getListItem(j).getEndDate();
			
			if (iEndDate == null) {
				if (jEndDate == null) {
					return j;
				}
				return i;
			}
			if (jEndDate == null) {
				return j;
			}
			if (jEndDate.gteq(iEndDate)) {
				return j;
			}
			return i;
		}
		return INVALID_PARAMETERS;
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
