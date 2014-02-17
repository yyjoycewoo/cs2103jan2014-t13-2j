import java.util.ArrayList;

/** For the commands such as add, delete, update, display so on
 */

/**
 * @author Hao Eng
 *
 */
public class Controller {

	private ArrayList<Task> list;
	
	// take in task description and index of the task
	public Task update(String des, int index){
		// description can have "from", "on", "to" and "at"
		// split according to the above keywords
		String[] words = des.split("\\s+");
		
		// modify the starting time
		if(words[0] == "from") {
			if(words[1] != null || words[1] != ""){
				int time = Integer.parseInt(words[1]);
				int hr = time/100;
				int min = time%100;
				list.get(index).setStartTime(new Time(hr, min));
			}
		}
		
		//TBC...
		return list.get(index);
	}
}
