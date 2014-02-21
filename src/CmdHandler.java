public class CmdHandler {

	private static final String ADD_COMMAND = "add";
	private static final String DELETE_COMMAND = "delete";
	private static final String UPDATE_COMMAND = "update";
	private static final String DISPLAY_COMMAND = "display";
	private static final String EXIT_COMMAND = "exit";
	private static final String INVALID_COMMAND_MSG = "Invalid command entered, please try again.";
	
	private static Command command;
	
	public static String processCommand(String userInput) {	
		command = new Command(userInput.split(" ", 2));
		
		if (command.getAction().equals(EXIT_COMMAND)) {
			System.exit(0);
		}			
		if (command.getAction().equals(DISPLAY_COMMAND)) {
			return Controller.processDisplay();				
		}
		else if (command.getAction().equals(UPDATE_COMMAND)) {
			return Controller.processUpdate(command.getArgument());			
		}
		else if (command.getAction().equals(DELETE_COMMAND)) {
			return Controller.processDelete(command.getArgument());					
		}
		else if (command.getAction().equals(ADD_COMMAND)) {
			return Controller.processAdd(command.getArgument());			
		}
		else {
			return INVALID_COMMAND_MSG;
		}
	}
}
