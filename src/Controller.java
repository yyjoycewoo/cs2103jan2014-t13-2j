/** For the commands such as add, delete, update, display so on
 * 
 * @author Hao Eng
 *
 */
public class Controller {

	private static final int noOfCharInAt = 4;
	private static final int posOfMinute = 2;
	
	private TaskList list;
	
	// take in task description and index of the task
	public Task processUpdate(String des, int index){
		// description can have "from", "on", "to" and "at"
		// split according to the above keywords
		String[] words = des.split("\\s+");
		
		// modify the starting time
		if(words[0] == "from") {
			if(words[1] != null || words[1] != "") {
				int time = Integer.parseInt(words[1]);
				int hr = time / 100;
				int min = time % 100;
				list.getListItem(index).setStartTime(new Time(hr, min));
			}
		}
		
		//TBC...
		return list.getListItem(index);
	}

	/*Hao Eng, your function should be like this (takes in a string)
	 * You would have to parse it to get the des and index, then call
	 * a helper function (refer to image from whiteboard)
	 */
	public static Task processUpdate(String argument) {
		// TODO Auto-generated method stub
		return null;
	}

	public static TaskList processDisplay() {
		// TODO Auto-generated method stub
		return null;
	}

	public static Task processDelete(String argument) {
		// TODO Auto-generated method stub
		return null;
	}

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
	 * Retrieves the task description from user input
	 * @param input The raw user input after the command word
	 * @return string with the task description
	 */
	
	public static String getTaskDes (String input) {
		String TaskDes = "";
		int index = checkForAtPosition(input);
		TaskDes = input.substring(0, index);
		return TaskDes;
	}
	
	public static int checkForAtPosition(String input) {
		String checkForAt = " at ";
		if (input.indexOf(checkForAt) == -1)
		{
			return 0;
		}
		else
		{
			return input.indexOf(checkForAt);
		}
	}
	
	public static int checkForSpcPos (String input) {
		String checkForSpc = " ";
		return input.indexOf(checkForSpc);
	}
	
	public static String getTimeAndDate (String input) {
		String checkForAt = " at ";
		int index = input.indexOf(checkForAt);
		input = input.substring(index + noOfCharInAt);
		return input;
	}
	
	public static Time getTime(String input) {
		String delims = " ";
		int index = input.indexOf(delims);
		input = input.substring(0, index);
		String userHour = input.substring(0,posOfMinute);
		String userMinute = input.substring(posOfMinute);
		Time userTime = new Time(Integer.parseInt(userHour), Integer.parseInt(userMinute));
		return userTime;
	}
	
	public static Date getDate (String input) {
		String delims = "/";
		String[] tokens = input.split(delims);
		Date userDate = new Date(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
		return userDate;
	}
	
}
	
	

