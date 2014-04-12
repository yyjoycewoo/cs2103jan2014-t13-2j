package todomato;
import hirondelle.date4j.DateTime;

import java.util.ArrayList;
import java.util.Collections;

//@author A0120766H
/**
 * Class to store a list of all Tasks that the user wants to keep track of
 *
 */
public class TaskList {
	private static final String LINE_BREAK = "\r\n";
	private ArrayList<Task> list;
	private DateTime lastSyncTime=null;
	private String userName=null;
	private String password=null;
	
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
	 * @param i index of Task to be deleted
	 * @return Task deleted
	 */
	public Task deleteListItem(int i){
		Task TaskDeleted = list.get(i);
		list.remove(i);
		return TaskDeleted;
	}
	
	/**
	 * Clears all the Tasks in the TaskList
	 */
	public void clearList() {
		list = new ArrayList<Task>();
	}
	
	/**
	 * Get the Task at index i
	 * @param i Index of Task to get
	 * @return Task at index i
	 */
	public Task getListItem(int i) {
		return list.get(i);
	}
	
	/**
	 * Get the index at which the task with the id id can be found
	 * @param id id of Task to get
	 * @return index of Task with id id, or -1 if Task cannot be found
	 */
	public int getItem(int id) {
		for (int i=0; i<list.size(); i++) {
			if (list.get(i).getId() == id) {
				return i;
			}
		}
		return -1;
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
