package todomato;

import java.util.Scanner;

/**
 * This class is the driver for the Todomato application. It creates the GUI,
 * and repeatedly obtains user input until the user in the main method, until
 * the user wants to exit.
 * 
 */
public class Todomato {

	private static final String INVALID_INPUT_MSG = "Invalid input: ";
	private static final String WELCOME_MSG = "Welcome to Todomato";
	private static final String PROMPT_USER_INPUT = "Command: ";
	private static Scanner scan = new Scanner(System.in);

	/**
	 * Take in and execute user commands until the user wants to exit.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * The next line opens the GUI Make sure the fileLoc in Controller is
		 * correct for it to work miglayout (http://www.miglayout.com/) must
		 * also be added to the build path
		 */
		TodomatoFrame f = new TodomatoFrame();

		System.out.println(WELCOME_MSG);

		Popup.show();

		// type "exit" to exit the program
		while (true) {
			try {
				System.out.print(PROMPT_USER_INPUT);
				Popup.keepCheckingNoticeTime();
				String command = scan.nextLine();
				String status = SplitProcessorsHandler.processCommand(command);
				System.out.println(status);
			} catch (InvalidInputException e) {
				System.out.println(INVALID_INPUT_MSG + e.getMessage());
			}
		}
	}
}