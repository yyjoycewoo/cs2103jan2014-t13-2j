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
 * </ul>
 * 
 */
public class DisplayProcessor extends Processor {
	private static final String ARGUMENT_SORT_BY_DATE = "by date";
	private static final String SUCCESS_DISPLAY = "All tasks have been displayed: ";
	private static final String SUCCESS_SORT_BY_DATE = "Sorted by date";
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
		bubbleSort();
		fileHandler.updateFile(list);
		return SUCCESS_SORT_BY_DATE;
	}

	private static void bubbleSort() {
		for (int i = 0; i < (list.getSize() - 1); i++) {
			for (int j = 0; j < (list.getSize() - i - 1); j++) {
				if (compare(j, j + 1)) {
					list.swap(j, j + 1);
				}
			}
		}
	}

	private static boolean compare(int i, int j) {
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
}
