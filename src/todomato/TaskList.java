package todomato;
import hirondelle.date4j.DateTime;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class to store a list of all TaskDTs that the user wants to keep track of
 * @author Joyce
 *
 */
public class TaskList {
	private static final String LINE_BREAK = "\r\n";
	private ArrayList<Task> list;
	private DateTime lastSyncTime;
	private String userName=null;
	private String password=null;
	
	/**
	 * Create a new empty TaskDTList
	 */
	public TaskList() {
		this.list = new ArrayList<Task>();
	}
	
	/**
	 * Add t to the TaskDTList
	 * @param t TaskDT to be added
	 * @return TaskDT that was added
	 */
	public Task addToList(Task t) {
		list.add(t);
		return t;
	}
	
	/**
	 * Delete TaskDT i from the TaskDTList
	 * @param i index of TaskDT to be deleted
	 * @return TaskDT deleted
	 */
	public Task deleteListItem(int i){
		Task TaskDTDeleted = list.get(i);
		list.remove(i);
		return TaskDTDeleted;
	}
	
	/**
	 * Clears all the TaskDTs in the TaskDTList
	 */
	public void clearList() {
		list = new ArrayList<Task>();
	}
	
	/**
	 * Get the TaskDT at index i
	 * @param i Index of TaskDT to get
	 * @return TaskDT at index i
	 */
	public Task getListItem(int i) {
		return list.get(i);
	}

	@Override
	public String toString() {
		String s = "";
		int i = 0;
		for (Task t : list){
			s += Integer.toString(i+1) + ": " + t.toString() + LINE_BREAK;
			i++;
		}
		return s;
	}
	
	/**
	 * Modifies TaskDTList to be a deep copy of copyList
	 * @param copyList TaskDTList to be copied
	 * @return modified TaskDTList
	 */
	public ArrayList<Task> deepCopy(TaskList copyList) {
		list = new ArrayList<Task>();
		for (Task i : copyList.getList()) {
			list.add(new Task(i));
		}
		return list;
	}
	
	/**
	 * Checks to see if the list is empty
	 * @return true if the list is empty
	 */
	public boolean isEmpty() {
		return getSize() == 0;
	}
	
	/**
	 * Finds the size of the list
	 * @return the size of the list
	 */
	public int getSize() {
		return list.size();
	}
	
	public ArrayList<Task> getList() {
		return list;
	} 

	public void setList(ArrayList<Task> list) {
		this.list = list;
	}
	
	public void swap(int i, int j) {
		Collections.swap(list, i, j);
	}
	
	public void reverse() {
		Collections.reverse(list);
	}
	
	public void setLastSyncTime (DateTime lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}
	
	public DateTime getLastSyncTime () {
		return lastSyncTime;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserNameAndPassword(String username, String password) {
		setUserName(username);
		setPassword(password);
	}
}
