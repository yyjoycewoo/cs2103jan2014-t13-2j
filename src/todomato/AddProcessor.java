package todomato;

import java.io.IOException;
import java.util.regex.Pattern;

public class AddProcessor extends Processor {

	private static String[] keywords = new String[] { " at ", " from ",
			" until ", " to ", " in ", " due ",  " on "};

	/**
	 * @author Daryl
	 * @param input
	 * @return Task
	 * @throws InvalidInputException
	 * @throws NumberFormatException
	 * @throws IOException
	 */

	public static Task processAdd(String input) throws NumberFormatException {
		
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
		if (!checkForKeywords(input)) {
			taskDes = input;
			userTask = new Task(taskDes);
		}
		while (checkForKeywords(input)) {
			keywordIndex = getFirstKeyword(input);
			stringFragments = splitByKeyword(input, keywords[keywordIndex]);
			if (!taskDesExtracted) {
				taskDes = stringFragments[0];
				userTask = new Task(taskDes);
				taskDesExtracted = true;
			}
			input = stringFragments[1];
			int spaceIndex = stringFragments[1].indexOf(" ");
			if (spaceIndex == SPACE_NOT_FOUND) {
				switch (keywordIndex) {
				case 0:
				case 1:
					startTimeString = stringFragments[1];
					break;
				case 2:
				case 3:
					endTimeString = stringFragments[1];
					break;
				case 4:
					location = stringFragments[1];
					break;
				case 5:
					endTimeString = stringFragments[1];
					break;
				case 6:
					System.out.print(stringFragments[1]);
					userDate = retrieveDateStringFromInput(stringFragments[1]);
					break;
				}
			} else {
				switch (keywordIndex) {
				case 0:
				case 1:
					startTimeString = (stringFragments[1].substring(0, spaceIndex));
					break;
				case 2:
				case 3:
					endTimeString = stringFragments[1].substring(0,spaceIndex);
					break;
				case 4:
					if (getFirstKeyword(stringFragments[1]) == -1) {
						location = stringFragments[1];
					} else {
						location = getWordsBeforeNextKeyword(
								stringFragments[1],
								keywords[getFirstKeyword(stringFragments[1])]);
					}
					break;
				case 5:
					endTimeString = stringFragments[1].substring(0, spaceIndex);
					break;
				case 6:
					userDate = retrieveDateStringFromInput(stringFragments[1]);
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
		}
		list.addToList(userTask);
		list = fileHandler.updateFile(list);
		return userTask;
	}

	private static String getWordsBeforeNextKeyword(String input, String keyword) {
		String wordsBeforeNextKeyword;
		wordsBeforeNextKeyword = input.substring(0, input.indexOf(keyword));
		return wordsBeforeNextKeyword;
	}

	private static Boolean checkForKeywords(String input) {
		for (String keyword : keywords) {
			if (input.contains(keyword)) {
				return true;
			}
		}
		return false;
	}

	private static int getFirstKeyword(String input) {
		int firstKeywordPos = input.length();
		int firstKeyword = -1;

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

	private static String[] splitByKeyword(String input, String keyword) {
		String[] splitWords = null;
		Pattern pattern = Pattern.compile(Pattern.quote(keyword));
		splitWords = pattern.split(input);

		return splitWords;
	}

}
