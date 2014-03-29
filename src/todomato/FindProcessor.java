package todomato;

public class FindProcessor extends Processor {

	private static final String NO_TASKS_FOUND_MESSAGE = "No tasks found";
	private static final String SUCCESS_MSG = "Search completed";

	public static String processFind(String argument) {
		TaskList tasksFound = new TaskList();
		for (Task i : list.getList()) {
			String task = i.toString();
			if (task.contains(argument) && task != "") {
				tasksFound.addToList(i);
			}
		}
		
		displayList = tasksFound;
		
		if (tasksFound.isEmpty())
			return NO_TASKS_FOUND_MESSAGE;
		
		return SUCCESS_MSG;
	}

}
