package todomato;

import hirondelle.date4j.DateTime;

//@author A0101578H
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
	private static final int INDC_INVALID_PARAMETERS = -1;
	private static final int INDEX_OF_TYPE = 0;
	private static final int INDEX_OF_ORDER = 1;
	private static final int NO_OF_ARG_WITH_ORDER = 2;
	
	/**
	 * Method for processing different types of sort
	 * 
	 * @param argument containing type (and order)
	 * @return success message
	 * @throws InvalidInputException
	 */
	public static String processSort(String argument) throws InvalidInputException {
		if (argument.isEmpty()) {
			throw new InvalidInputException(INVALID_INPUT_MISSING_ARGUMENT);
		}
		
		String argArr[] = argument.split(" ", 2);
		String type = argArr[INDEX_OF_TYPE];
		String order = "";
		if (argArr.length == NO_OF_ARG_WITH_ORDER) {
			order = argArr[INDEX_OF_ORDER];
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
		throw new InvalidInputException(INVALID_INPUT_TYPE);
	}
	
	/**
	 * Sort by completion in order of:
	 * Default/Ascending: unfinished -> completed tasks
	 * Descending: order reversed from ascending order
	 * 
	 * @param order: user-specified
	 * @return success sort by completion message
	 * @throws InvalidInputException
	 */
	private static String sortByCompletion(String order) throws InvalidInputException {
		if (!isValidOrder(order)) {
			throw new InvalidInputException(INVALID_INPUT_ORDER);
		}
		bubbleSort(ARGUMENT_COMPLETION);
		if (isDescending(order)) {
			list.reverse();
		}
		fileHandler.updateFile(list);
		return SUCCESS_SORT_BY_COMPLETION;
	}

	/**
	 * Sort by priority in order of:
	 * Default/Descending: High -> Medium -> Low -> completed
	 * Ascending: order reversed from descending order
	 * 
	 * @param order: user-specified
	 * @return
	 * @throws InvalidInputException
	 */
	private static String sortByPriority(String order) throws InvalidInputException {
		if (!isValidOrder(order)) {
			throw new InvalidInputException(INVALID_INPUT_ORDER);
		}
		bubbleSort(ARGUMENT_PRIORITY);
		if (isAscending(order)) {
			list.reverse();
		}
		fileHandler.updateFile(list);
		return SUCCESS_SORT_BY_PRIORITY;
	}
	
	/**
	 * Sort by start date in order of:
	 * Default/Ascending: Most recent -> Least recent -> No date -> completed
	 * Descending: in reverse order from ascending order
	 * 
	 * @param order
	 * @return success sort by date message
	 * @throws InvalidInputException
	 */
	private static String sortByStartDate(String order) throws InvalidInputException {
		if (!isValidOrder(order)) {
			throw new InvalidInputException(INVALID_INPUT_ORDER);
		}		
		bubbleSort(ARGUMENT_START_DATE);
		if (isDescending(order)) {
			list.reverse();
		}
		fileHandler.updateFile(list);
		return SUCCESS_SORT_BY_DATE;
	}
	
	/**
	 * Sort by end date in order of:
	 * Default/Ascending: Most recent -> Least recent -> No date -> completed
	 * Descending: in reverse order from ascending order
	 * 
	 * @param order
	 * @return success sort by date message
	 * @throws InvalidInputException
	 */
	private static String sortByEndDate(String order) throws InvalidInputException {
		if (!isValidOrder(order)) {
			throw new InvalidInputException(INVALID_INPUT_ORDER);
		}
		
		bubbleSort(ARGUMENT_END_DATE);
		if (isDescending(order)) {
			list.reverse();
		}
		fileHandler.updateFile(list);
		return SUCCESS_SORT_BY_DATE;
	}
	
	/**
	 * Compares completion status of two tasks
	 * 
	 * @param i: index of first task
	 * @param j: index of second task
	 * @return true if task i is completed but task j is not
	 */
	private static boolean compareCompletion(int i, int j) {
		if (list.getListItem(i).getCompleted()) {
			if (!list.getListItem(j).getCompleted()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Compares priority levels of two tasks
	 * 
	 * @param i: index of first task
	 * @param j: index of second task
	 * @return true if task i has lower priority than task j or has been completed
	 */
	private static boolean comparePriority(int i, int j) {
		if (list.getListItem(i).getCompleted()) {
			return true;
		}
		if (list.getListItem(j).getCompleted()) {
			return false;
		}
		if (isSamePriority(i, j)) {
			return false;
		}
		String iPriority = list.getListItem(i).getPriorityLevel();
		String jPriority = list.getListItem(j).getPriorityLevel();
		if ((iPriority.equals(PRIORITY_HIGH)) || (jPriority.equals(PRIORITY_LOW))) {
			return false;
		}
		if ((jPriority.equals(PRIORITY_HIGH)) || (iPriority.equals(PRIORITY_LOW))) {
			return true;
		}
		return false;
	}
	
	/**
	 * Check if priority levels of two tasks are the same
	 * 
	 * @param i: index of first task
	 * @param j: index of second task
	 * @return true if priority levels are same
	 */
	private static boolean isSamePriority(int i, int j) {
		String iPriority = list.getListItem(i).getPriorityLevel();
		String jPriority = list.getListItem(j).getPriorityLevel();
		if(iPriority.equals(jPriority)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Compares start dates of two tasks
	 * 
	 * @param i: index of first task
	 * @param j: index of second task
	 * @return true if task i starts later than task j
	 */
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
	
	/**
	 * Compares end dates of two tasks
	 * 
	 * @param i: index of first task
	 * @param j: index of second task
	 * @return true if task i ends later than task j
	 */
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
	
	/**
	 * Checks if start/end date of two tasks are the same 
	 * @param type: type of date to be checked
	 * @param i: index of first task
	 * @param j: index of second task
	 * @return true if tasks lie on the same start/end date
	 */
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
	 * @param type: type of time to be compared
	 * @param i: index of first task
	 * @param j: index of second task
	 * @return later task of the i'th and j'th tasks
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
		return INDC_INVALID_PARAMETERS;
	}
	
	/**
	 * Compares two tasks by either start or end date
	 * If the dates are the same, j will be returned
	 * 
	 * @param type: type of date to be compared
	 * @param i: index of first task
	 * @param j: index of second task
	 * @return later task of the i'th and j'th tasks
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
		return INDC_INVALID_PARAMETERS;
	}
	
	/**
	 * Bubble sort algorithm
	 * 
	 * @param type: type to sort by
	 */
	private static void bubbleSort(String type) {
		for (int i = 0; i < (list.getSize() - 1); i++) {
			for (int j = 0; j < (list.getSize() - i - 1); j++) {
				if (needSwap(type, j)) {
					list.swap(j, j + 1);
				}
			}
		}
	}

	/**
	 * Determine if there is a need to swap a task with the next 
	 * for bubble sort
	 * 
	 * @param type: type to sort by
	 * @param j: index of task
	 * @return true if swap is needed
	 */
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
	
	/**
	 * Check if user input for order is ascending
	 * 
	 * @param order: user input
	 * @return true if user input matches allowed commands for ascending
	 */
	private static boolean isAscending(String order) {
		for (String s: ascending) {
			if (order.equalsIgnoreCase(s)) {
				return true;				
			}
		}
		return false;
	}
	
	/**
	 * Check if user input for order is descending
	 * 
	 * @param order: user input
	 * @return true if user input matches allowed commands for descending
	 */
	private static boolean isDescending(String order) {
		for (String s: descending) {
			if (order.equalsIgnoreCase(s)) {
				return true;				
			}
		}
		return false;
	}
	
	/**
	 * Check if user input for order is ascending or descending
	 * 
	 * @param order: user input
	 * @return true if user input is empty(default) or ascending/descending
	 */
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
