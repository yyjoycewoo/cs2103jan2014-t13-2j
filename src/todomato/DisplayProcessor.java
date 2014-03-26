package todomato;

/**
 * This class contains methods to process display commands by the user. 
 * It updates the user's list of tasks, and saves it to disk.
 * 
 * <p>
 * The following ways to display are supported: 
 * <ul>
 * <li> display all tasks
 * <ul> <li> "display" </ul>
 * <li> display tasks by date
 * <ul> <li> "display by date" </ul>
 * <li> display tasks by priority from high to low
 * <ul> <li> "display by priority" </ul>
 * </ul>
 * 
 */
public class DisplayProcessor extends Processor {
	private static final String ARGUMENT_SORT_BY_DATE = "by date";
	private static final String ARGUMENT_SORT_BY_PRIORITY = "by priority";
	private static final String SUCCESS_DISPLAY = "All tasks have been displayed: ";
	private static final String SUCCESS_SORT_BY_DATE = "Sorted by date";
	private static final String SUCCESS_SORT_BY_PRIORITY = "Sorted by priority";
	private static final String INVALID_ARGUMENT_MESSAGE = "Invalid argument";
	
	/**
	 * @author linxuan
	 * @return Status message telling user if command was successfully executed 
	 */
	public static String processDisplay(String argument) {
		displayList = list;

		if (argument.isEmpty()) {
			return display();
		}
		if (argument.equalsIgnoreCase(ARGUMENT_SORT_BY_DATE)) {
			return sortByDate();
		}
		if (argument.equalsIgnoreCase(ARGUMENT_SORT_BY_PRIORITY)) {
			return sortByPriority();
		}
		return INVALID_ARGUMENT_MESSAGE;
	}

	

	private static String display() {
		return SUCCESS_DISPLAY + list.toString();
	}

	/**
	 * This method sorts the task list by date in ascending order.
	 * 
	 * @return status message
	 */
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
