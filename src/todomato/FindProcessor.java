package todomato;

public class FindProcessor extends Processor {

	private static final String NO_TASKS_FOUND_MESSAGE = "No tasks found";
	private static final String SUCCESS_MSG = "Search completed";

	public static String processFind(String argument) {
		//Make search case insensitive by converting everything to uppercase
		argument = argument.toUpperCase();
		TaskList tasksFound = new TaskList();
		TaskList tasksNotFound = new TaskList();
		
		for (Task i : list.getList()) {
			String task = i.toString().toUpperCase();
			if (task.contains(argument) && task != "") {
				tasksFound.addToList(i);
				System.out.println(task);
			} else {
				tasksNotFound.addToList(i);
			}
		}
		
		list.deepCopy(tasksFound);
		for (Task i : tasksNotFound.getList()) {
			list.addToList(i);
		}
		fileHandler.updateFile(list);
		
		displayList = tasksFound;
		
		if (tasksFound.isEmpty())
			return NO_TASKS_FOUND_MESSAGE;
		
		return SUCCESS_MSG;
	}

}