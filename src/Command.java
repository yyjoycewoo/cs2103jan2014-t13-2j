/**
 * Class to store the different components of a user's command: the action, and the argument
 * @author Joyce
 *
 */
public class Command {
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
