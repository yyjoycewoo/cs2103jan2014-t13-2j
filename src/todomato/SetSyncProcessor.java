package todomato;

//@author A0096620E
/**
 * Class to set username and password via CLI
 *
 */
public class SetSyncProcessor extends Processor {
	private static final String SET_SUCCESSFUL = "Username and password successful set";
	private static final String INVALID_USERNAME = "Invalid username and password. Format is <username> <password>";
	public static String processSetSync(String input) {
		String[] parts = input.split(" ");
		if (parts.length != 2) {
			return INVALID_USERNAME;
		}
		String username = parts[FIRST_WORD];
		String password = parts[SECOND_WORD];
		list.setUserNameAndPassword(username, password);
		fileHandler.updateFile(list);
		return SET_SUCCESSFUL;
	}

}
