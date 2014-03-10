package todomato;
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
	 * @return Task that was added
	 */
	public Task addToList(Task t) {
		list.add(t);
		return t;
	}
	
	/**
	 * Delete Task i from the TaskList
	 * @param i index of task to be deleted
	 * @return Task deleted
	 */
	public Task deleteListItem(int i){
		Task taskDeleted = list.get(i);
		list.remove(i);
		return taskDeleted;
	}
	
	/**
	 * Clears all the Tasks in the TaskList
	 */
	public void clearList() {
		list = new ArrayList<Task>();
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
		
		for (Task t : list){
			s += t.toString() + LINE_BREAK;
		}
		return s;
	}
	
	/**
	 * Modifies TaskList to be a deep copy of copyList
	 * @param copyList TaskList to be copied
	 * @return modified TaskList
	 */
	public ArrayList<Task> deepCopy(TaskList copyList) {
		list = new ArrayList<Task>();
		for (Task i : copyList.getList()) {
			list.add(new Task(i));
		}
		return list;
	}
	
	public ArrayList<Task> getList() {
		return list;
	} 

	public void setList(ArrayList<Task> list) {
		this.list = list;
	}
	
	public int getSize() {
		return list.size();
	}
}
