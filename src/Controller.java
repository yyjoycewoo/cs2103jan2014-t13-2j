/**
 * For the commands such as add, delete, update, display so on
 * 
 * 
 */
public class Controller {

	// " at "
	private static final int noOfCharInAt = 4;
	private static final int posOfMinute = 2;
	// " time "
	private static final int noOfCharInTime = 6, noOfCharInDesc = 6;
	private static final String INVALID_UPDATE = "No parameter to edit.";
	private static boolean timeFlag = false, descFlag = false;
	private static TaskList list;

	/**
	 * @author Daryl
	 * @param input
	 * @return Task
	 */
	public static Task processAdd(String input) {
		// TODO Auto-generated method stub
		String taskDes = getTaskDes(input);
		String timeAndDate = getTimeAndDate(input);
		Date userDate = getDate(timeAndDate);
		Time userTime = getTime(timeAndDate);
		Task userTask = new Task(taskDes, userTime, userDate);
		return userTask;
	}

	/**
	 * @author Hao Eng
	 * @param argument
	 * @return Updated Task
	 */
	public static Task processUpdate(String argument) {
		int index = getTaskIndex(argument);
		int whichToEdit = findDetailToEdit(argument);
		if (whichToEdit == -1) {
			printInvalidEdit();
		} else {
			if (timeFlag) {
				return updateTime(index, whichToEdit, argument);
			}
			if (descFlag) {
				return updateDesc(index, whichToEdit, argument);
			}
		}
		return null;
	}

	/**
	 * @param index
	 * @param editTime
	 * @param argument
	 *            that contains new time
	 * @return updated task
	 */
	private static Task updateTime(int index, int editTime, String argument) {
		int time = Integer.parseInt(argument.substring(editTime
				+ noOfCharInTime));
		int hr = time / 100;
		int min = time % 100;
		list.getListItem(index).setStartTime(new Time(hr, min));
		return list.getListItem(index);
	}

	/**
	 * @param index
	 * @param editDesc
	 * @param argument
	 *            that contains new description
	 * @return updated task
	 */
	private static Task updateDesc(int index, int editDesc, String argument) {
		list.getListItem(index).setDescription(
				argument.substring(editDesc + noOfCharInDesc));
		return list.getListItem(index);
	}

	private static Task printInvalidEdit() {
		System.out.println(INVALID_UPDATE);
		return null;
	}

	/**
	 * @param argument
	 * @return the starting index of which task detail to change
	 */
	private static int findDetailToEdit(String argument) {
		int editTime = argument.indexOf(" time ");
		if (editTime > -1) {
			timeFlag = true;
			return editTime;
		} else {
			int editDesc = argument.indexOf(" desc ");
			if (editDesc > -1) {
				descFlag = true;
				return editDesc;
			} else {
				return -1;
			}
		}
	}

	/**
	 * @param argument
	 * @return task index
	 * @throws NumberFormatException
	 */
	private static int getTaskIndex(String argument)
			throws NumberFormatException {
		// remove the command word: edit
		String input = argument.replaceFirst("update ", "");
		int spaceAfterIndex = input.indexOf(" ");
		return (Integer.parseInt(input.substring(0, spaceAfterIndex)));
	}

	public static Task processDelete(String argument) {
		// TODO Auto-generated method stub
		return null;
	}

	public static TaskList processDisplay() {
		// TODO Auto-generated method stub
		return null;
	}

	public static int checkForAtPosition(String input) {
		String checkForAt = " at ";
		if (input.indexOf(checkForAt) == -1) {
			return 0;
		} else {
			return input.indexOf(checkForAt);
		}
	}

	public static int checkForSpcPos(String input) {
		String checkForSpc = " ";
		return input.indexOf(checkForSpc);
	}

	public static Date getDate(String input) {
		String delims = "/";
		String[] tokens = input.split(delims);
		Date userDate = new Date(Integer.parseInt(tokens[0]),
				Integer.parseInt(tokens[1]));
		return userDate;
	}

	/**
	 * Retrieves the task description from user input
	 * 
	 * @param input
	 *            the raw user input after the command word
	 * @return string with the task description
	 */

	public static String getTaskDes(String input) {
		String TaskDes = "";
		int index = checkForAtPosition(input);
		TaskDes = input.substring(0, index);
		return TaskDes;
	}

	public static String getTimeAndDate(String input) {
		String checkForAt = " at ";
		int index = input.indexOf(checkForAt);
		input = input.substring(index + noOfCharInAt);
		return input;
	}

	public static Time getTime(String input) {
		String delims = " ";
		int index = input.indexOf(delims);
		input = input.substring(0, index);
		String userHour = input.substring(0, posOfMinute);
		String userMinute = input.substring(posOfMinute);
		Time userTime = new Time(Integer.parseInt(userHour),
				Integer.parseInt(userMinute));
		return userTime;
	}

}
