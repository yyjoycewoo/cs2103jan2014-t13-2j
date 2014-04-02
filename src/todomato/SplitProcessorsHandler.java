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
	private static final Object REDO_COMMAND = "redo";
	private static final Object FIND_COMMAND = "find";
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
		if (command.getAction().equals(EXIT_COMMAND)) {
			System.exit(0);
		}
		if (command.getAction().equals(DISPLAY_COMMAND)) {
			return DisplayProcessor.processDisplay();
		}
		if (command.getAction().equals(UPDATE_COMMAND)) {
			return UpdateProcessor.processUpdate(command.getArgument());
		}
		if (command.getAction().equals(DELETE_COMMAND)) {
			return DeleteProcessor.processDelete(command.getArgument());
		}
		if (command.getAction().equals(UNDO_COMMAND)) {
			return UndoProcessor.processUndo();
		}
		if (command.getAction().equals(REDO_COMMAND)) {
			return RedoProcessor.processRedo();
		}
		if (command.getAction().equals(FIND_COMMAND)) {
			return FindProcessor.processFind(command.getArgument());
		}
		if (command.getAction().equals(SORT_COMMAND)) {
			return SortProcessor.processSort(command.getArgument());
		}
		if (command.getAction().equals(ADD_COMMAND)) {
			return AddProcessor.processAdd(command.getArgument());
		}
		if (command.getAction().equals(RECUR_COMMAND)) {
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
