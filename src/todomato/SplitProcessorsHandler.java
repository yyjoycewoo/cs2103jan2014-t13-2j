package todomato;

/**
 * This class handles user commands by calling the processing method depending
 * on the action specified.
 * 
 * @author Joyce
 * 
 */
public class SplitProcessorsHandler {

	private static final String ADD_COMMAND = "add";
	private static final String DELETE_COMMAND = "delete";
	private static final String UPDATE_COMMAND = "update";
	private static final String DISPLAY_COMMAND = "display";
	private static final String SORT_COMMAND = "sort";
	private static final String UNDO_COMMAND = "undo";
	private static final String EXIT_COMMAND = "exit";
	private static final String REDO_COMMAND = "redo";
	private static final String FIND_COMMAND = "find";
	private static final String RECUR_COMMAND = "recur";
	private static final String NOTIFY_COMMAND = "notify";

	private static Command command;

	/**
	 * 
	 * @param userInput
	 * @return A status message describing the last action completed
	 * @throws InvalidInputException
	 */
	public static String processCommand(String userInput)
			throws InvalidInputException {
		command = new Command(userInput.split(" ", 2));
		if (command.getAction().equalsIgnoreCase(EXIT_COMMAND)) {
			System.exit(0);
		}
		if (command.getAction().equalsIgnoreCase(DISPLAY_COMMAND)) {
			return DisplayProcessor.processDisplay();
		}
		if (command.getAction().equalsIgnoreCase(UPDATE_COMMAND)) {
			return UpdateProcessor.processUpdate(command.getArgument());
		}
		if (command.getAction().equalsIgnoreCase(DELETE_COMMAND)) {
			return DeleteProcessor.processDelete(command.getArgument());
		}
		if (command.getAction().equalsIgnoreCase(UNDO_COMMAND)) {
			return UndoProcessor.processUndo();
		}
		if (command.getAction().equalsIgnoreCase(REDO_COMMAND)) {
			return RedoProcessor.processRedo();
		}
		if (command.getAction().equalsIgnoreCase(FIND_COMMAND)) {
			return FindProcessor.processFind(command.getArgument());
		}
		if (command.getAction().equalsIgnoreCase(SORT_COMMAND)) {
			return SortProcessor.processSort(command.getArgument());
		}
		if (command.getAction().equalsIgnoreCase(ADD_COMMAND)) {
			return AddProcessor.processAdd(command.getArgument());
		}
		if (command.getAction().equalsIgnoreCase(RECUR_COMMAND)) {
			return RecurProcessor.processRecur();
		}
		if (command.getAction().equals(NOTIFY_COMMAND)) {
			return NotifyProcessor.processNotify(command.getArgument());
		}
		// Do nothing if empty string is entered
		if (command.getAction().equals("")) {
			return " ";
		}
		// Otherwise, treat as an add command
		return AddProcessor.processAdd(userInput);
	}
}
