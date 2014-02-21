/** For the commands such as add, delete, update, display so on
 * 
 * @author Hao Eng
 *
 */
public class Controller {

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

	public static Task processAdd(String argument) {
		// TODO Auto-generated method stub
		return null;
	}
}
