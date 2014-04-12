package todomato;

//@author A0120766H
/**
 * This class processes find commands by the user.
 * 
 * The user should enter the keyword "find" followed
 * a String of word(s) that they would like to search
 * for.
 *  
 *
 */
public class FindProcessor extends Processor {

	private static final String NO_TASKS_FOUND_MESSAGE = "No tasks found";
	private static final String SUCCESS_MSG = "Search completed";

	/**
	 * Finds and modifies the displayList to display all tasks that contain
	 * the String argument. 
	 * @param argument String to be found
	 * @return a status message stating whether or not tasks have been found
	 */
	public static String processFind(String argument) {
		//Make search case insensitive by converting everything to uppercase
		argument = argument.toUpperCase();
		TaskList tasksFound = new TaskList();
		TaskList tasksNotFound = new TaskList();
		
		createTasksFoundList(argument, tasksFound, tasksNotFound);
		reorderList(tasksFound, tasksNotFound);
		
		fileHandler.updateFile(list);		
		displayList = tasksFound;
		
		if (tasksFound.isEmpty())
			return NO_TASKS_FOUND_MESSAGE;
		
		return SUCCESS_MSG;
	}

	private static void reorderList(TaskList tasksFound, TaskList tasksNotFound) {
		list.deepCopy(tasksFound);
		for (Task i : tasksNotFound.getList()) {
			list.addToList(i);
		}
	}

	private static void createTasksFoundList(String argument,
			TaskList tasksFound, TaskList tasksNotFound) {
		for (Task i : list.getList()) {
			String task = i.toString().toUpperCase();
			if (task.contains(argument) && task != "") {
				tasksFound.addToList(i);
				System.out.println(task);
			} else {
				tasksNotFound.addToList(i);
			}
		}
	}

}