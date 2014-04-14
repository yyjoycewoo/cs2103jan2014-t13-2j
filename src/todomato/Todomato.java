package todomato;

import java.util.Scanner;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

//@author A0120766H
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
	 * @throws SchedulerException
	 */
	public static void main(String[] args) throws SchedulerException {
		new TodomatoFrame();

		System.out.println(WELCOME_MSG);

		// show tasks for the next 3 days
		Popup.show();
		// show tasks that are due today according to the time of the day
		Scheduler scheduler = Scheduling.schedulingTasks();

		// loops forever until user types "exit"
		while (true) {
			try {
				System.out.print(PROMPT_USER_INPUT);
				String command = scan.nextLine();
				assert command != null;
				
				// stop the scheduler from running
				if (command == "exit") {
					try {
						scheduler.shutdown();
					} catch (SchedulerException e) {
						System.err.println(e.getMessage());
					}
				}
				
				String status = SplitProcessorsHandler.processCommand(command);
				System.out.println(status);
			} catch (InvalidInputException e) {
				System.out.println(INVALID_INPUT_MSG + e.getMessage());
			}
		}
	}

}