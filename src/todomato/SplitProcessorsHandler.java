package todomato;
/**
 * Class to handle user commands, calls the appropriate methods depending on command
 * @author Joyce
 *
 */
public class SplitProcessorsHandler {

	private static final String SUCCESSFUL_UPDATE_MSG = "Updated task to: ";
	//private static final String SUCCESSFUL_DELETE_MSG = "Deleted task(s): ";
	private static final String SUCCESSFUL_ADD_MSG = "Added task: ";
	private static final String ADD_COMMAND = "add";
	private static final String DELETE_COMMAND = "delete";
	private static final String UPDATE_COMMAND = "update";
	private static final String DISPLAY_COMMAND = "display";
	private static final String UNDO_COMMAND = "undo";
	private static final String EXIT_COMMAND = "exit";
	private static final String INVALID_COMMAND_MSG = "Invalid command entered, please try again.";
	
	private static Command command;
	
	/**
	 * 
	 * @param userInput
	 * @return A status message describing the last action completed
	 * @throws InvalidInputException
	 */
	public static String processCommand(String userInput) throws InvalidInputException {	
		command = new Command(userInput.split(" ", 2));
		if (command.getAction().equals(EXIT_COMMAND)) {
			System.exit(0);
		}			
		if (command.getAction().equals(DISPLAY_COMMAND)) {
			return DisplayProcessor.processDisplay().toString();		
		}
		if (command.getAction().equals(UPDATE_COMMAND)) {
			return SUCCESSFUL_UPDATE_MSG  + UpdateProcessor.processUpdate(command.getArgument()).toString();
		}
		if (command.getAction().equals(DELETE_COMMAND)) {
			//return SUCCESSFUL_DELETE_MSG + DeleteProcessor.processDelete(command.getArgument());
			return DeleteProcessor.processDelete(command.getArgument());
		}
		if (command.getAction().equals(ADD_COMMAND)) {
			return SUCCESSFUL_ADD_MSG + AddProcessor.processAdd(command.getArgument());
		}
		if (command.getAction().equals(UNDO_COMMAND)) {
			return UndoProcessor.processUndo();
		}
		return INVALID_COMMAND_MSG;
	}
}
