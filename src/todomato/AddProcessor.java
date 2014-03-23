package todomato;

import hirondelle.date4j.DateTime;

import java.io.IOException;
import java.util.Arrays;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * This class contains methods to process add commands by the user.
 * It updates the user's lists of tasks, and saves it to disk.
 * 
 * <p>
 * It can process commands with a description, a start time, an end
 * time, a date and a location. Only a description is mandatory.
 * 
 * <p>
 * The following keywords are necessary, while the order is flexible:
 * <ul>
 * <li>" at " or " from " keyword for the start time
 * <li>" to ", " until ", " due " for the end time
 * <li>" on " for the date
 * <li>" in " for the keyword
 * </ul>
 * 
 * <p>
 * Example: "add dinner with bob at 1900 on mar 3 in uTown"
 * 
 * <p>
 * The following time formats are supported:
 * <ul>
 * <li>930am/pm
 * <li>9am/pm
 * <li>1230
 * <li>0730
 * <li>0730pm
 * </ul>
 * 
 * <p>
 * The following date formats are supported (case does not matter):
 * <ul>
 * <li>jan 1
 * <li>1 january
 * <li>january 1
 * </ul>
 * 
 * @author Daryl
 * 
 */
public class AddProcessor extends Processor {

	private static String[] keywords = new String[] { " at ", " from ",
			" until ", " to ", " in ", " due ",  " on ", " recur ", " priority "};
	
	private static int INDEX_OF_WORDS_AFTER_KEYWORDS = 1;
	private static int NOT_FOUND = -1;
	private static String INVALID_INPUT = "Invalid input format";
	private static String RECUR_KEYWORD = "recur";
	private static int INDEX_OF_DESC = 0;
	private static int INDEX_OF_START_TIME_STRING = 1;
	private static int INDEX_OF_END_TIME_STRING = 2;
	private static int INDEX_OF_DATE_STRING = 3;
	private static int INDEX_OF_LOCATION_STRING = 4;
	private static int INDEX_OF_RECUR_STRING = 5;
	private static int INDEX_OF_PRIORITY_STRING = 6;
	private static int NO_OF_TASK_DETAILS = 7;
	private static String RECURRING_TASKS_ADDED = "Recurring tasks have been added";
	private static String CANNOT_RECUR_WITHOUT_DATE = "Cannot set recurring period without setting date";
	private static String TODAY = " today";
	private static String TOMORROW1 = " tmr";
	private static String TOMORROW2 = " tomorrow";
	private static String ADD_SUCCESSFUL = "Added ";
	private static String[] taskDetails = new String[NO_OF_TASK_DETAILS];
	private static DateTime currentDate = DateTime.today(TimeZone.getDefault());
	


	/**
	 * @author Daryl
	 * Adds Task to the list and writes to file the updated list
	 * @param input
	 * @return Task
	 * @throws InvalidInputException
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static String processAdd(String input) throws NumberFormatException {
		storeCurrentList();
		TaskDT userTask = null;
		if (input.equals(RECUR_KEYWORD)) {
			for (int i = 0; i < list.getSize(); i++){
				addsRecurringTask(list.getListItem(i));
				}
			fileHandler.updateFile(list);
			return RECURRING_TASKS_ADDED;
		}
		else {
			try {
				userTask = parseTask(input);
			} catch (InvalidInputException e) {
				return INVALID_INPUT;
			}
			list.addToList(userTask);
		}
		fileHandler.updateFile(list);
		displayList = list;
		return ADD_SUCCESSFUL + userTask.toString();
	}
	
	
	
	/**
	 * Parses Task from a String
	 * @param input
	 * @return Task
	 * @throws InvalidInputException
	 */
	public static TaskDT parseTask(String input) throws InvalidInputException {
		Arrays.fill(taskDetails,null);
		boolean taskDesExtracted = false;
		int keywordIndex = NOT_FOUND;
		TaskDT userTaskDT = null;
		String[] stringFragments = null;
		
		if (checkForTodayAndTomorrowStrings(input)) {
			input = setDateForTodayAndTomorrow(input);
		}
		
		if (!keywordIsInString(input)) {
			taskDetails[INDEX_OF_DESC] = input;
		}
		
		while (keywordIsInString(input)) {
			keywordIndex = getFirstKeyword(input);
			stringFragments = splitByKeyword(input, keywords[keywordIndex]);
			if (!taskDesExtracted) {
				taskDetails[INDEX_OF_DESC] = stringFragments[0];
				taskDesExtracted = true;
			}
			taskDetails = keywordHandler(keywordIndex, stringFragments[INDEX_OF_WORDS_AFTER_KEYWORDS]);
			input = stringFragments[INDEX_OF_WORDS_AFTER_KEYWORDS];
		}
		
		userTaskDT = setUserTask(taskDetails);
		
		return userTaskDT;
	}
	
	/**
	 * Creates and sets userTask with all the information stored in
	 * an array
	 * @param taskDetails
	 * @return userTaskDT with all the task details
	 */
	
	private static TaskDT setUserTask(String[] taskDetails) {
		TaskDT userTask = new TaskDT(taskDetails[INDEX_OF_DESC]);
		DateTime startTime = convertStringToDateTime(taskDetails[INDEX_OF_START_TIME_STRING]);
		DateTime endTime = convertStringToDateTime(taskDetails[INDEX_OF_END_TIME_STRING]);
		DateTime date = convertStringToDateTime(taskDetails[INDEX_OF_DATE_STRING]);
		int recurPeriod = 0;
		try {
			if (date == null && taskDetails[INDEX_OF_RECUR_STRING] != null) {
				System.out.print(CANNOT_RECUR_WITHOUT_DATE);
			} else if (taskDetails[INDEX_OF_RECUR_STRING] != null && date != null){
				recurPeriod = Integer.parseInt(taskDetails[INDEX_OF_RECUR_STRING]);
			}
		}
		catch (NumberFormatException e) {
			System.out.print(INVALID_INPUT);
		}
		userTask.setStartTime(startTime);
		userTask.setEndTime(endTime);
		userTask.setDate(date);
		userTask.setLocation(taskDetails[INDEX_OF_LOCATION_STRING]);
		userTask.setRecurrencePeriod(recurPeriod);
		userTask.setPriorityLevel(parsePriorityFromString(taskDetails[INDEX_OF_PRIORITY_STRING]));
		return userTask;
	}
	
	/**
	 * Calls helper methods based on what keyword type it is
	 * @param keywordType
	 * @param input
	 * @return String array with all the relevant task details
	 * @throws InvalidInputException
	 */

	private static String[] keywordHandler (int keywordType, String input) throws InvalidInputException {
	
		int spaceIndex = input.indexOf(" ");
		if (spaceIndex == NOT_FOUND) {
			spaceIndex = input.length();
		}
		switch (keywordType) {
			case 0:
			case 1:
				taskDetails[INDEX_OF_START_TIME_STRING] = retrieveStartTime(input, spaceIndex);
				break;
			case 2:
			case 3:
				taskDetails[INDEX_OF_END_TIME_STRING] = retrieveEndTime(input,spaceIndex);
				break;
			case 4:
				taskDetails[INDEX_OF_LOCATION_STRING] = retrieveLocation(input);
				break;
			case 5:
				taskDetails[INDEX_OF_END_TIME_STRING] = retrieveEndTime(input,spaceIndex);
				break;
			case 6:
				taskDetails[INDEX_OF_DATE_STRING] = retrieveDate(input);
				break;
			case 7:
				taskDetails[INDEX_OF_RECUR_STRING] = retrieveRecurPeriod(input, spaceIndex);
				break;
			case 8:
				taskDetails[INDEX_OF_PRIORITY_STRING] = retrievePriority(input,spaceIndex);
				break;
		}

		return taskDetails;
	}

	/**
	 * Retrieves the recurring period (first word) from input
	 * @param input
	 * @param spaceIndex
	 * @return recurPeriod
	 */
	
	private static String retrieveRecurPeriod (String input, int spaceIndex) {
		String recurPeriod = (input.substring(0, spaceIndex));
		return recurPeriod;
	}
	
	/**
	 * Retrieves the start time (first word) from input in the DateTime format (HH:MM)
	 * @param input
	 * @param spaceIndex
	 * @return startTimeString
	 */
	
	private static String retrieveStartTime (String input, int spaceIndex) {
		String startTimeString = (input.substring(0, spaceIndex));
		startTimeString = parseTimeStringFromInput(startTimeString);
		return startTimeString;
	}
	
	/**
	 * Retrieves the end time (first word) from input in the DateTime format (HH:MM)
	 * @param input
	 * @param spaceIndex
	 * @return endTimeString
	 */
	
	private static String retrieveEndTime (String input, int spaceIndex) {
		String endTimeString = (input.substring(0, spaceIndex));
		endTimeString = parseTimeStringFromInput(endTimeString);
		return endTimeString;
	}
	
	/**
	 * Retrieves location from input
	 * @param input
	 * @param spaceIndex
	 * @return startTimeString
	 */
	
	private static String retrieveLocation (String input) {
		String location = null;
		if (getFirstKeyword(input) == NOT_FOUND) {
			location = input;
		} else {
			location = getWordsBeforeNextKeyword(input, keywords[getFirstKeyword(input)]);
		}
		
		return location;
	}
	
	/**
	 * Retrieves the Date from input in the DateTime format (YYYY-MM-DD)
	 * @param input
	 * @param spaceIndex
	 * @return dateString
	 */
	
	private static String retrieveDate (String input) throws InvalidInputException {
		String dateString = parseDateString(input);
		return dateString;
	}
	

	/**
	 * Retrieves the priority
	 * @param input
	 * @param spaceIndex
	 * @return priorityString
	 */
	
	private static String retrievePriority(String input, int spaceIndex) {
		String priorityString = input.substring(0, spaceIndex);
		return priorityString;
	}
	

	/**
	 * Returns words before the specified keyword
	 * @param input
	 * @param keyword
	 * @return wordsBeforeNextKeyword
	 */

	private static String getWordsBeforeNextKeyword(String input, String keyword) {
		String wordsBeforeNextKeyword;
		wordsBeforeNextKeyword = input.substring(0, input.indexOf(keyword));
		return wordsBeforeNextKeyword;
	}
	
	/**
	 * Checks whether there are any keywords in the string
	 * @param input
	 * @return true for yes/false for no
	 */

	private static Boolean keywordIsInString(String input) {
		for (String keyword : keywords) {
			if (input.contains(keyword)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retrieves the first keyword in the string
	 * @param input
	 * @return
	 */

	private static int getFirstKeyword(String input) {
		int firstKeywordPos = input.length();
		int firstKeyword = NOT_FOUND;

		for (int i = 0; i < keywords.length; i++) {
			if (input.contains(keywords[i])) {
				if (input.indexOf(keywords[i]) < firstKeywordPos) {
					firstKeywordPos = input.indexOf(keywords[i]);
					firstKeyword = i;
				}
			}
		}
		return firstKeyword;
	}
	
	/**
	 * Splits the input into 2, one portion before the keyword, the other after
	 * @param input
	 * @param keyword
	 * @return
	 */

	private static String[] splitByKeyword(String input, String keyword) {
		String[] splitWords = null;
		Pattern pattern = Pattern.compile(Pattern.quote(keyword));
		splitWords = pattern.split(input);

		return splitWords;
	}
	
	/**
	 * Adds tasks that have expired and needs to be recurred
	 * @param task
	 */
	

	protected static void addsRecurringTask(TaskDT task) {
		if (needsToBeRecurred(task)) {
			TaskDT newTask = new TaskDT(task);
			newTask.setDate(task.getDate().plusDays(task.getRecurrencePeriod()));
			list.addToList(newTask);
		}
	}
	/**
	 * Checks if there will be a duplicate task
	 * @param task
	 * @return Boolean
	 */
	protected static Boolean checkIfDuplicateRecurTaskExist (TaskDT task) {

		DateTime recurDate = task.getDate().plusDays(task.getRecurrencePeriod());
		for (int i = 0; i < list.getSize(); i++) {
			if (list.getListItem(i).getDate().equals(recurDate)) {
				if (list.getListItem(i).compareDescAndLocation(task)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks if the task needs to be readded to the list
	 * These are the two conditions for recurrence:
	 * Task must have expired
	 * There must not be another task with the same description
	 * and location on the date it is set to be recurred
	 * @param task
	 * @return Boolean
	 */

	protected static Boolean needsToBeRecurred(TaskDT task) {
		if (task.getRecurrencePeriod() == 0) {
			return false;
		}
		TimeZone SGT = TimeZone.getTimeZone("GMT+8");
		DateTime recurDate = task.getDate().plusDays(task.getRecurrencePeriod());
		if (DateTime.today(SGT).numDaysFrom(recurDate) < task.getRecurrencePeriod()) {
			if (!checkIfDuplicateRecurTaskExist(task)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the input string has "today" "tmr" or tomorrow
	 * @param input
	 * @return true or false
	 */
	
	private static Boolean checkForTodayAndTomorrowStrings (String input) {
		if (input.contains(TODAY) || input.contains(TOMORROW1) || input.contains(TOMORROW2)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Updates Tasklist with current date or tomorrow's date
	 * @param input
	 * @return input without the today or tomorrow word
	 */
	
	private static String setDateForTodayAndTomorrow (String input) {		
		if (input.contains(TODAY)) {
			taskDetails[INDEX_OF_DATE_STRING] = currentDate.toString();	
			input = input.substring(0,input.indexOf(TODAY)) + input.substring(input.indexOf(TODAY) + TODAY.length());
		} else if (input.contains(TOMORROW1)) {
			taskDetails[INDEX_OF_DATE_STRING] = currentDate.plusDays(1).toString();			
			input = input.substring(0,input.indexOf(TOMORROW1)) + input.substring(input.indexOf(TOMORROW1) + TOMORROW1.length());
		} else if (input.contains(TOMORROW2)) {
			taskDetails[INDEX_OF_DATE_STRING] = currentDate.plusDays(1).toString();			
			input = input.substring(0,input.indexOf(TOMORROW2)) + input.substring(input.indexOf(TOMORROW2) + TOMORROW2.length());
		}
		return input;
	}
}
