import java.util.Scanner;


public class Todomato {

	private static final String WELCOME_MSG = "Welcome to Todomato";
	private static final String PROMPT_USER_INPUT = "Command: ";
	private static Scanner scan = new Scanner(System.in);

	/**
	 * Take in and execute user commands until the user wants to exit.
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(WELCOME_MSG);
		
		//type "exit" to exit the program
		while (true) {
			System.out.print(PROMPT_USER_INPUT);
			String command = scan.nextLine();
			String status = CmdHandler.processCommand(command);
			System.out.println(status);	
		}
	}
}