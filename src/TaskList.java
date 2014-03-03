import java.util.ArrayList;

/**
 * Class to store a list of all Tasks that the user wants to keep track of
 * @author Joyce
 *
 */
public class TaskList {
	private static final String LINE_BREAK = "\r\n";
	private ArrayList<Task> list;
	
	/**
	 * Create a new empty TaskList
	 */
	public TaskList() {
		this.list = new ArrayList<Task>();
	}
	
	/**
	 * Add t to the TaskList
	 * @param t Task to be added
	 */
	public void addToList(Task t) {
		list.add(t);
	}
	
	/**
	 * Get the task at index i
	 * @param i Index of Task to get
	 * @return Task at index i
	 */
	public Task getListItem(int i) {
		return list.get(i);
	}

	@Override
	public String toString() {
		String s = "";
		
		for (Task t : this.list){
			s += t.toString() + LINE_BREAK;
		}
		return s;
	}
	
	public ArrayList<Task> getList() {
		return list;
	} 

	public void setList(ArrayList<Task> list) {
		this.list = list;
	}
}
