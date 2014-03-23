package todomato;

/**
 * This class contains methods to process display commands by the user. It is
 * used to display all of a user's tasks when using the command line, but the
 * GUI automatically displays the user's tasks after each command. It also
 * creates notifications about upcoming tasks.
 * 
 */
public class DisplayProcessor extends Processor {
	private static final String ARGUMENT_SORT_BY_DATE = "by date";
	// private static final String ARGUMENT_NOTIFICATION = "notification";
	private static final String SUCCESS_SORT_BY_DATE = "Sorted by date";
	private static final String INVALID_ARGUMENT_MESSAGE = "Invalid argument";

	// private static TaskDTList notify = new TaskDTList();
	/**
	 * @author linxuan
	 * @return TaskList
	 */
	public static String processDisplay(String argument) {
		displayList = list;

		if (argument.isEmpty()) {
			return display();
		}
		if (argument.equalsIgnoreCase(ARGUMENT_SORT_BY_DATE)) {
			return sortByDate();
		}
		// if (argument.equalsIgnoreCase(ARGUMENT_NOTIFICATION)) {
		// return notify().toString();
		// }
		// TODO
		return INVALID_ARGUMENT_MESSAGE;
	}

	private static String display() {
		return list.toString();
	}

	/**
	 * This method sorts the task list by date in ascending order.
	 * 
	 * @return status message
	 */
	private static String sortByDate() {
		// TODO
		// may need for displaying: stores original indices before sorting
		int[] orderOfIndices = new int[list.getSize()];
		initArray(orderOfIndices);
		bubbleSort(orderOfIndices);
		fileHandler.updateFile(list);
		return SUCCESS_SORT_BY_DATE;
	}

	private static void bubbleSort(int[] orderOfIndices) {
		for (int i = 0; i < (list.getSize() - 1); i++) {
			for (int j = 0; j < (list.getSize() - i - 1); j++) {
				// if (array[d] > array[d+1]) /* For descending order use < */
				if (compare(i, j)) {
					int swap = orderOfIndices[j];
					orderOfIndices[j] = orderOfIndices[j + 1];
					orderOfIndices[j + 1] = swap;
					list.swap(j, j + 1);
				}
			}
		}
	}

	private static boolean compare(int i, int j) {
		return list.getListItem(j).getDate()
				.compareTo(list.getListItem(j + 1).getDate()) > 0;
	}

	private static void initArray(int[] orderOfIndices) {
		for (int i = 0; i < orderOfIndices.length; i++) {
			orderOfIndices[i] = i + 1;
		}
	}
}
