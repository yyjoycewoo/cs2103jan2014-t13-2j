package todomato;

import hirondelle.date4j.DateTime;

import java.io.IOException;
import java.util.regex.Pattern;

public class AddProcessor extends Processor {

	private static String[] keywords = new String[] { " at ", " from ",
			" until ", " to ", " in ", " due ",  " on "};
	//private static String EMPTY_DESCRIPTION = "Description is empty";
	private static int POS_OF_WORDS_STORED = 1;
	private static int NOT_FOUND = -1;
	private static String INVALID_INPUT = "Invalid syntax";

	/**
	 * @author Daryl
	 * @param input
	 * @return Task
	 * @throws InvalidInputException
	 * @throws NumberFormatException
	 * @throws IOException
	 */

	public static String processAdd(String input) throws NumberFormatException {
		
		storeCurrentList();

		boolean taskDesExtracted = false;
		int keywordIndex = -1;
		Task userTask = null;
		String location = null;
		String startTimeString = null;
		String endTimeString = null;
		String[] stringFragments = null;
		String taskDes = null;
		Date userDate = null;
		if (!keywordIsInString(input)) {
			taskDes = input;
			userTask = new Task(taskDes);
		}
		while (keywordIsInString(input)) {
			keywordIndex = getFirstKeyword(input);
			stringFragments = splitByKeyword(input, keywords[keywordIndex]);
			if (!taskDesExtracted) {
				taskDes = stringFragments[0];
				userTask = new Task(taskDes);
				taskDesExtracted = true;
			}
			input = stringFragments[POS_OF_WORDS_STORED];
			int spaceIndex = stringFragments[POS_OF_WORDS_STORED].indexOf(" ");
			if (spaceIndex == NOT_FOUND) {
				spaceIndex = stringFragments[POS_OF_WORDS_STORED].length();
			}
			switch (keywordIndex) {
				case 0:
				case 1:
					startTimeString = (stringFragments[POS_OF_WORDS_STORED].substring(0, spaceIndex));
					break;
				case 2:
				case 3:
					endTimeString = stringFragments[POS_OF_WORDS_STORED].substring(0,spaceIndex);
					break;
				case 4:
					if (getFirstKeyword(stringFragments[POS_OF_WORDS_STORED]) == NOT_FOUND) {
						location = stringFragments[POS_OF_WORDS_STORED];
					} else {
						location = getWordsBeforeNextKeyword(
								stringFragments[POS_OF_WORDS_STORED], keywords[getFirstKeyword(stringFragments[1])]);
					}
					break;
				case 5:
					endTimeString = stringFragments[POS_OF_WORDS_STORED].substring(0, spaceIndex);
					break;
				case 6:
					userDate = retrieveDateStringFromInput(stringFragments[POS_OF_WORDS_STORED]);
					break;
			}
		}
		try {
			Time endUserTime = parseTimeFromString(endTimeString);
			Time startUserTime = parseTimeFromString(startTimeString);
			userTask.setEndTime(endUserTime);
			userTask.setStartTime(startUserTime);
			userTask.setDate(userDate);
			userTask.setLocation(location);
		} catch (InvalidInputException e) {
			return null;
		}
		list.addToList(userTask);
		list = fileHandler.updateFile(list);
		return userTask.toString();
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

}
