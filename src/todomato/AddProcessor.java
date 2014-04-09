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
 * <li>" in " or " @" for the location <- no space after the @
 * <li> " priority " or " !" <- take note there is no space after the !
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


	private static String[] keywords = new String[] { " from ", " at ", 
			" until ", " due ", " on ", " in ", " @", " recur ", " priority ", " !"};
	
	private static int INDEX_OF_WORDS_AFTER_KEYWORDS = 1;
	private static final int NO_OF_KEYWORDS_FOR_TIME = 5;
	private static int NOT_FOUND = -1;
	private static String INVALID_INPUT = "Invalid Input format ";
	private static String INVALID_START_TIME = "Invalid Start Time ";
	private static String INVALID_END_TIME = "Invalid End Time ";
	private static String INVALID_END_DATE = "Invalid End Date format ";
	private static String INVALID_RECUR = "Invalid Recur format : Need Date before adding recurrence period";
	private static int INDEX_OF_DESC = 0;
	private static int INDEX_OF_START_TIME = 1;
	private static int INDEX_OF_END_TIME = 2;
	private static int INDEX_OF_START_DATE = 3;
	private static int INDEX_OF_END_DATE = 4;
	private static int INDEX_OF_LOCATION = 5;
	private static int INDEX_OF_RECUR = 6;
	private static int INDEX_OF_PRIORITY = 7;
	private static int NO_OF_TASK_DETAILS = 8;
	private static String TODAY = " today";
	private static String TOMORROW1 = " tmr";
	private static String TOMORROW2 = " tomorrow";
	private static String ADD_SUCCESSFUL = "Added ";
	private static Boolean[] errorsInInput = new Boolean[NO_OF_TASK_DETAILS];
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
		Arrays.fill(errorsInInput,false);
		Task userTask = null;
		userTask = parseTask(input);
		list.addToList(userTask);
		fileHandler.updateFile(list);
		displayList.deepCopy(list);
		String statusString = "";
		Boolean errorPresent = false;
		for (int i = 0; i < errorsInInput.length; i++) {
			if (errorsInInput[i] == true) {
				errorPresent = true;
				switch (i) {
				case 1:
					statusString += INVALID_START_TIME;
					break;
				case 2:
					statusString += INVALID_END_TIME;
					break;
				case 4:
					statusString += INVALID_END_DATE;
					break;
				case 6:
					statusString += INVALID_RECUR;
					break;
				}
			}
		}
		if (!errorPresent) {
			return ADD_SUCCESSFUL + userTask.toString();
		}
		else {
			return statusString;
		}
	}
	
	
	
	/**
	 * Parses Task from a String
	 * @param input
	 * @return Task
	 * @throws InvalidInputException
	 */
	public static Task parseTask(String input) {
		Arrays.fill(taskDetails,null);
		boolean taskDesExtracted = false;
		int keywordIndex = NOT_FOUND;
		Task userTaskDT = null;
		String[] stringFragments = null;
		
		if (checkForInvertedCommas(input)) {
			input = setDescWithWordsInsideInvertedCommas(input);
			taskDesExtracted = true;
		}
		
		if (!isKeywordForTimePresentFound(input)) {
			if (isTodayOrTomorrowFound(input)) {
				//Removes today and tomorrow from the string so that it does not appear in description
				input = setDateForTodayOrTomorrow(input);
			}
		}
		
		if (!keywordIsInString(input) && !taskDesExtracted) {
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
	 * @throws InvalidInputException 
	 */
	
	private static Task setUserTask(String[] taskDetails) {
		Task userTask = new Task(taskDetails[INDEX_OF_DESC]);
		DateTime startTime = convertStringToDateTime(taskDetails[INDEX_OF_START_TIME]);
		DateTime endTime = convertStringToDateTime(taskDetails[INDEX_OF_END_TIME]);
		DateTime startDate = convertStringToDateTime(taskDetails[INDEX_OF_START_DATE]);
		DateTime endDate = convertStringToDateTime(taskDetails[INDEX_OF_END_DATE]);
		if (startDate != null && endDate == null) {
			endDate = startDate;
		}
		if (startDate != null && endDate != null) {
			if (startDate.gt(endDate)) {
				endDate = startDate;
			}
			if (endTime != null && startTime != null) {
				if (endTime.lt(startTime)) {
					endDate = startDate.plusDays(1);
				}
			}
		}
		int recurPeriod = 0;
		try {
			if (endDate == null && taskDetails[INDEX_OF_RECUR] != null) {
				errorsInInput[INDEX_OF_RECUR] = true;
			} else if (taskDetails[INDEX_OF_RECUR] != null && endDate != null){
				recurPeriod = parseRecurrencePeriodFromString(taskDetails[INDEX_OF_RECUR]);
			}
		}
		catch (NumberFormatException e) {
			System.out.print(INVALID_INPUT);
		}
		userTask.setStartTime(startTime);
		userTask.setEndTime(endTime);
		userTask.setStartDate(startDate);
		userTask.setEndDate(endDate);
		userTask.setLocation(taskDetails[INDEX_OF_LOCATION]);
		userTask.setRecurrencePeriod(recurPeriod);
		userTask.setPriorityLevel(parsePriorityFromString(taskDetails[INDEX_OF_PRIORITY]));
		return userTask;
	}
	
	/**
	 * Calls helper methods based on what keyword type it is
	 * @param keywordType
	 * @param input
	 * @return String array with all the relevant task details
	 * @throws InvalidInputException
	 */

	private static String[] keywordHandler (int keywordType, String input) {
	
		int spaceIndex = input.indexOf(" ");
		if (spaceIndex == NOT_FOUND) {
			spaceIndex = input.length();
		}
		switch (keywordType) {
			case 0:
				try {
					taskDetails[INDEX_OF_START_TIME] = retrieveStartTime(input);
				} catch (InvalidInputException invalidStartTime) {
					errorsInInput[INDEX_OF_START_TIME] = true;
				}
				break;
			case 1:
			case 2:
			case 3:
				try {
					taskDetails[INDEX_OF_END_TIME] = retrieveEndTime(input);
				} catch (InvalidInputException invalidEndTime) {
					errorsInInput[INDEX_OF_END_TIME] = true;
				}
				break;
			case 4:
				try {
					taskDetails[INDEX_OF_END_DATE] = retrieveDate(input);
				}
				catch (InvalidInputException invalidDate) {
					errorsInInput[INDEX_OF_END_DATE] = true;
				}
				break;
			case 5:
			case 6:
				taskDetails[INDEX_OF_LOCATION] = retrieveLocation(input);
				break;
			case 7:
				taskDetails[INDEX_OF_RECUR] = retrieveRecurPeriod(input, spaceIndex);
				break;
			case 8:
			case 9:
				taskDetails[INDEX_OF_PRIORITY] = retrievePriority(input,spaceIndex);
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
	 * @return startTimeString
	 * @throws InvalidInputException 
	 */
	
	private static String retrieveStartTime (String input) throws InvalidInputException {
		String startTimeString = null;
		if (getFirstKeyword(input) == NOT_FOUND) {
			startTimeString = input;
		} else {
			startTimeString = getWordsBeforeNextKeyword(input, keywords[getFirstKeyword(input)]);
		}
		startTimeString = retrieveStartDateFromStartTime(startTimeString);
		startTimeString = parseTimeString(startTimeString);
		return startTimeString;
	}
	
	private static String retrieveStartDateFromStartTime (String startTimeString) throws InvalidInputException {
		String parts[] = startTimeString.split(" ");
		if (parts.length == 3) {
			if (isParseableByDate(parts[0] + " " + parts[1])) {
				taskDetails[INDEX_OF_START_DATE] = parseDateString(parts[0] + " " + parts[1]);
				startTimeString = parseTimeString(parts[2]);
			} else if (isParseableByDate (parts[1] + " " + parts[2])) {
				taskDetails[INDEX_OF_START_DATE] = parseDateString(parts[1] + " " + parts[2]);
				startTimeString = parseTimeString(parts[0]);
			}
		} else if (parts.length == 2) {
			if (isParseableByDate(parts[0])) {
				taskDetails[INDEX_OF_START_DATE] = parseDateString(parts[0]);
				startTimeString = parts[1];
			} else if (isParseableByDate(parts[1])) {
				taskDetails[INDEX_OF_START_DATE] = parseDateString(parts[1]);
				startTimeString = parts[0];
			} else if (isParseableByDate (parts[0] + " " + parts[1])) {
				taskDetails[INDEX_OF_START_DATE] = parseDateString(parts[0] + " " + parts[1]);
				startTimeString = null;
			}
		} else if (parts.length == 1) {
			if (isParseableByDate(parts[0])) {
				taskDetails[INDEX_OF_START_DATE] = parseDateString(parts[0]);
				startTimeString = parts[0];
			}
		}
		return startTimeString;
	}
	
	/**
	 * Retrieves the end time (first word) from input in the DateTime format (HH:MM)
	 * @param input
	 * @param spaceIndex
	 * @return endTimeString
	 * @throws InvalidInputException 
	 */
	
	private static String retrieveEndTime (String input) throws InvalidInputException {
		String endTimeString = null;
		if (getFirstKeyword(input) == NOT_FOUND) {
			endTimeString = input;
		} else {
			endTimeString = getWordsBeforeNextKeyword(input, keywords[getFirstKeyword(input)]);
		}
		endTimeString = retrieveEndDateFromEndTime(endTimeString);
		endTimeString = parseTimeString(endTimeString);
		return endTimeString;
	}
	
	private static String retrieveEndDateFromEndTime (String endTimeString) throws InvalidInputException {
		String parts[] = endTimeString.split(" ");
		if (parts.length == 3) {
			if (isParseableByDate(parts[0] + " " + parts[1])) {
				taskDetails[INDEX_OF_END_DATE] = parseDateString(parts[0] + " " + parts[1]);
				endTimeString = parseTimeString(parts[2]);
			} else if (isParseableByDate (parts[1] + " " + parts[2])) {
				taskDetails[INDEX_OF_END_DATE] = parseDateString(parts[1] + " " + parts[2]);
				endTimeString = parseTimeString(parts[0]);
			}
		} else if (parts.length == 2) {
			if (isParseableByDate(parts[0])) {
				taskDetails[INDEX_OF_END_DATE] = parseDateString(parts[0]);
				endTimeString = parts[1];
			} else if (isParseableByDate(parts[1])) {
				taskDetails[INDEX_OF_END_DATE] = parseDateString(parts[1]);
				endTimeString = parts[0];
			} else if (isParseableByDate (parts[0] + " " + parts[1])) {
				taskDetails[INDEX_OF_END_DATE] = parseDateString(parts[0] + " " + parts[1]);
				endTimeString = null;
			}
		} else if (parts.length == 1) {
			if (isParseableByDate(parts[0])) {
				taskDetails[INDEX_OF_END_DATE] = parseDateString(parts[0]);
				endTimeString = parts[0];
			}
		}
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
	
	private static Boolean isKeywordForTimePresentFound (String input) {
		for (int i = 0; i < NO_OF_KEYWORDS_FOR_TIME; i++) {
			if (input.contains(keywords[i])) {
				return true;
			}
		}
		return false;
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
	 * Checks if the input string has "today" "tmr" or tomorrow
	 * @param input
	 * @return true or false
	 */
	
	private static Boolean isTodayOrTomorrowFound (String input) {
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
	
	private static String setDateForTodayOrTomorrow (String input) {		
		if (input.contains(TODAY)) {
			taskDetails[INDEX_OF_END_DATE] = currentDate.toString();	
			input = input.substring(0,input.indexOf(TODAY)) + input.substring(input.indexOf(TODAY) + TODAY.length());
		} else if (input.contains(TOMORROW1)) {
			taskDetails[INDEX_OF_END_DATE] = currentDate.plusDays(1).toString();			
			input = input.substring(0,input.indexOf(TOMORROW1)) + input.substring(input.indexOf(TOMORROW1) + TOMORROW1.length());
		} else if (input.contains(TOMORROW2)) {
			taskDetails[INDEX_OF_END_DATE] = currentDate.plusDays(1).toString();			
			input = input.substring(0,input.indexOf(TOMORROW2)) + input.substring(input.indexOf(TOMORROW2) + TOMORROW2.length());
		}
		return input;
	}
	
	/**
	 * Checks if the string has two inverted commas
	 * @param input
	 * @return
	 */
	
	private static Boolean checkForInvertedCommas (String input) {
		if (input.contains("\"")) {
			if (input.substring(input.indexOf("\"")+1).contains("\"")) {
			return true;
			}
		} 
		return false;
	}
	
	/**
	 * Sets the task detail containing description with the words within the inverted commas
	 * @param input
	 * @return input without the description
	 */
	
	private static String setDescWithWordsInsideInvertedCommas (String input) {
		int firstIndex = input.indexOf("\"");
		int secondIndex = input.lastIndexOf("\"");
		//Extracts the task Description from in between the two quotation marks
		taskDetails[INDEX_OF_DESC] = input.substring(1, secondIndex - firstIndex);
		input = input.substring(secondIndex);
		return input;
	}
}