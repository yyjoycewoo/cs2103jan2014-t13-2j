package todomato;

/**
 * This class contains methods to process sort commands by the user. 
 * It updates the user's list of tasks, and saves it to disk.
 * 
 * <p>
 * The following ways to sort are supported: 
 * <ul>
 * <li> sort tasks by date
 * <ul> <li> "sort date" </ul>
 * <li> sort tasks by priority from high to low
 * <ul> <li> "sort priority" </ul>
 * </ul>
 * 
 */

public class SortProcessor extends Processor{
	private static final String ARGUMENT_SORT_BY_DATE = "date";
	private static final String ARGUMENT_SORT_BY_PRIORITY = "priority";
	private static final String SUCCESS_SORT_BY_DATE = "Sorted by date";
	private static final String SUCCESS_SORT_BY_PRIORITY = "Sorted by priority";
	private static final String INVALID_ARGUMENT_MESSAGE = "Invalid argument";
	
	public static String processSort(String argument) {
		displayList = list;

		if (argument.equalsIgnoreCase(ARGUMENT_SORT_BY_DATE)) {
			return sortByDate();
		}
		if (argument.equalsIgnoreCase(ARGUMENT_SORT_BY_PRIORITY)) {
			return sortByPriority();
		}
		return INVALID_ARGUMENT_MESSAGE;
	}
	
	private static String sortByDate() {
		bubbleSort(ARGUMENT_SORT_BY_DATE);
		fileHandler.updateFile(list);
		return SUCCESS_SORT_BY_DATE;
	}

	private static String sortByPriority() {
		bubbleSort(ARGUMENT_SORT_BY_PRIORITY);
		fileHandler.updateFile(list);
		return SUCCESS_SORT_BY_PRIORITY;
	}
	
	private static void bubbleSort(String arg) {
		for (int i = 0; i < (list.getSize() - 1); i++) {
			for (int j = 0; j < (list.getSize() - i - 1); j++) {
				if (arg.equals(ARGUMENT_SORT_BY_DATE)) {
					if (compareDate(j, j + 1)) {
						list.swap(j, j + 1);
					}
				} else if (arg.equals(ARGUMENT_SORT_BY_PRIORITY)) {
					if (comparePriority(j, j + 1)) {
						list.swap(j, j + 1);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param i
	 * @param j
	 * @return true if date of list[i] is later than list[j], or 
	 * list[i has a date but list[j] doesn't i.e needs to be swapped.
	 * False otherwise. 
	 * i and j are consecutive indices of items in the list, where i + 1 = j
	 */
	private static boolean compareDate(int i, int j) {
		if(list.getListItem(i).getDate() == null) {
			return false;
		} if(list.getListItem(j).getDate() == null) {
			return true;
		} if(list.getListItem(i).getDate()
				.compareTo(list.getListItem(j).getDate()) > 0) {
			return true;
		} 
		return false;
	}
	
	/**
	 * 
	 * @param i
	 * @param j
	 * @return True if list[i] has lower priority than list[j] i.e. need swap.
	 * False otherwise.
	 * i and j are consecutive indices of items in the list, where i + 1 = j
	 */
	private static boolean comparePriority(int i, int j) {
		if(list.getListItem(i).getPriorityLevel().equals(PRIORITY_LOW)) {
			if(list.getListItem(j).getPriorityLevel().equals(PRIORITY_LOW)) {
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
}
