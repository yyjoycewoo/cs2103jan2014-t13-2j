package todomato;

import java.io.IOException;
import java.util.regex.Pattern;

public class AddProcessor extends Processor {

	private static String[] keywords = new String[] { " at ", " from ",
			" until ", " to ", " in ", " due " };

	/**
	 * @author Daryl
	 * @param input
	 * @return Task
	 * @throws InvalidInputException
	 * @throws NumberFormatException
	 * @throws IOException
	 */

	public static Task processAdd(String input) throws NumberFormatException,
			InvalidInputException {
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
		} else {
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
					}
				} else {
					switch (keywordIndex) {
					case 0:
					case 1:
						startTimeString = stringFragments[1].substring(0,
								spaceIndex);
						userDate = retrieveDateInTimeString(stringFragments[1]);
						break;
					case 2:
					case 3:
						endTimeString = stringFragments[1].substring(0,
								spaceIndex);
						userDate = retrieveDateInTimeString(stringFragments[1]);
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
						endTimeString = stringFragments[1].substring(0,
								spaceIndex);
						break;
					}
				}
			}
			Time endUserTime = getTime(endTimeString);
			Time startUserTime = getTime(startTimeString);
			userTask.setEndTime(endUserTime);
			userTask.setStartTime(startUserTime);
			userTask.setDate(userDate);
			userTask.setLocation(location);
		}
		list.addToList(userTask);
		list = fileHandler.updateFile(list);
		return userTask;
	}

	public static Date retrieveDateInTimeString(String input)
			throws NumberFormatException, InvalidInputException {
		String[] parts = input.split(" ");
		String dateDelimiter = "/";
		String[] months = new String[] { "Jan", "Feb", "Mar", "Jun", "Jul",
				"Aug", "Sep", "Oct", "Nov", "Dec" };
		for (int i = 0; i < months.length; i++) {
			if (parts.length > 2) {
				if (parts[1].contains(months[i])) {
					String standardFormDate = convertDateToStandardForm(
							parts[2], String.valueOf(i + 1));
					Date userDate = getDate(standardFormDate);
					return userDate;
				} else if (parts[2].contains(months[i])) {
					String standardFormDate = convertDateToStandardForm(
							parts[1], String.valueOf(i + 1));
					Date userDate = getDate(standardFormDate);
					return userDate;
				}
			}
		}
		if (parts[1].contains(dateDelimiter)) {
			Date userDate = getDate(parts[1]);
			return userDate;
		}
		return null;
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

	/**
	 * Retrieves date from user input of the form "19/10"
	 * 
	 * @param input
	 * @return userDate
	 * @throws InvalidInputException
	 * @throws NumberFormatException
	 */

	private static Date getDate(String input) throws NumberFormatException,
			InvalidInputException {
		String delims = "/";
		String[] dateTokens = input.split(delims);
		if (dateTokens.length == 2) {
			Date userDate = new Date(Integer.parseInt(dateTokens[0]),
					Integer.parseInt(dateTokens[1]));
			return userDate;
		} else if (dateTokens.length == 3) {
			Date userDate = new Date(Integer.parseInt(dateTokens[0]),
					Integer.parseInt(dateTokens[1]),
					Integer.parseInt(dateTokens[2]));
			return userDate;
		} else {
			return null;
		}

	}

	/**
	 * Returns time from user string i.e. 19 hours 30 minutes from "1930" or
	 * "730pm"
	 * 
	 * @param input
	 * @return userTime
	 * @throws InvalidInputException
	 */

	private static Time getTime(String input) throws InvalidInputException {
		if (input == null) {
			return null;
		}
		Time userTime = null;
		int hour = 0, minute = -1;
		String meridiem[] = new String[] { "am", "pm" };
		int meridiemIndex = checkMeridiem(input);
		if (meridiemIndex != -1) {
			input = input.substring(0, input.indexOf(meridiem[meridiemIndex]));
			if (input.length() == 1) {
				hour = Integer.parseInt(input);
			} else if (input.length() == 3) {
				hour = Integer.parseInt(input.substring(0, 1));
				minute = Integer.parseInt(input.substring(POS_OF_MINUTE - 1));
			} else if (input.length() == 4) {
				hour = Integer.parseInt(input.substring(0, 2));
				minute = Integer.parseInt(input.substring(POS_OF_MINUTE));
			}
			if (meridiemIndex == 1) {
				hour += 12;
			}
			userTime = new Time(hour, minute);
		} else {
			String userHour = input.substring(0, POS_OF_MINUTE);
			if (input.length() == NO_OF_CHAR_IN_HOUR_AND_MINUTE) {
				String userMinute = input.substring(POS_OF_MINUTE);
				userTime = new Time(Integer.parseInt(userHour),
						Integer.parseInt(userMinute));
			} else {
				userTime = new Time(Integer.parseInt(userHour));
			}
		}
		return userTime;
	}
}
