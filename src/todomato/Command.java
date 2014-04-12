package todomato;
//@author A0120766H
/**
 * This class stores the different components of a user's command: the action, and the argument.
 * The action is the keyword specified in the first word of a command (eg. add, delete, update,
 * etc), and the argument is the rest of the command.
 *
 */
public class Command {
	private static final int ARGUMENT_POSITION = 1;
	private static final int ACTION_POSITION = 0;
	private static final String EMPTY_STRING = "";
	private static final int ONE_WORD = 1;
	private String action;
	private String argument;
	
	/**
	 * Create a new Command object with the specified action and argument as an empty String
	 * @param action The action that the user wants to perform
	 */
	public Command(String action) {
		this(action, "");
	}
	
	/**
	 * Create a new Command object with specified action and argument
	 * @param action The action that the user wants to perform
	 * @param argument The argument that the user wants to do with the action
	 */
	public Command(String action, String argument) {
		this.setAction(action);
		this.setArgument(argument);
	}

	/**
	 * Create a new Command object with specified input
	 * @param input Array with action and argument that the user wants to execute
	 */
	public Command(String[] input) {
		//command is the first word that the user enters
		this.setAction(input[ACTION_POSITION]);
		
		//argument is everything except the first word that the user enters
		if (input.length > ONE_WORD) {
			this.setArgument(input[ARGUMENT_POSITION]);
		} else {
			this.setArgument(EMPTY_STRING);
		}
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getArgument() {
		return argument;
	}

	public void setArgument(String argument) {
		this.argument = argument;
	}
}
