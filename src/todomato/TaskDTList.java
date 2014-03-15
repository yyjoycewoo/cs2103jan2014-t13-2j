package todomato;
import java.util.ArrayList;

/**
 * Class to store a list of all TaskDTs that the user wants to keep track of
 * @author Joyce
 *
 */
public class TaskDTList {
	private static final String LINE_BREAK = "\r\n";
	private ArrayList<TaskDT> list;
	
	/**
	 * Create a new empty TaskDTList
	 */
	public TaskDTList() {
		this.list = new ArrayList<TaskDT>();
	}
	
	/**
	 * Add t to the TaskDTList
	 * @param t TaskDT to be added
	 * @return TaskDT that was added
	 */
	public TaskDT addToList(TaskDT t) {
		list.add(t);
		return t;
	}
	
	/**
	 * Delete TaskDT i from the TaskDTList
	 * @param i index of TaskDT to be deleted
	 * @return TaskDT deleted
	 */
	public TaskDT deleteListItem(int i){
		TaskDT TaskDTDeleted = list.get(i);
		list.remove(i);
		return TaskDTDeleted;
	}
	
	/**
	 * Clears all the TaskDTs in the TaskDTList
	 */
	public void clearList() {
		list = new ArrayList<TaskDT>();
	}
	
	/**
	 * Get the TaskDT at index i
	 * @param i Index of TaskDT to get
	 * @return TaskDT at index i
	 */
	public TaskDT getListItem(int i) {
		return list.get(i);
	}

	@Override
	public String toString() {
		String s = "";
		
		for (TaskDT t : list){
			s += t.toString() + LINE_BREAK;
		}
		return s;
	}
	
	/**
	 * Modifies TaskDTList to be a deep copy of copyList
	 * @param copyList TaskDTList to be copied
	 * @return modified TaskDTList
	 */
	public ArrayList<TaskDT> deepCopy(TaskDTList copyList) {
		list = new ArrayList<TaskDT>();
		for (TaskDT i : copyList.getList()) {
			list.add(new TaskDT(i));
		}
		return list;
	}
	
	public ArrayList<TaskDT> getList() {
		return list;
	} 

	public void setList(ArrayList<TaskDT> list) {
		this.list = list;
	}
	
	public int getSize() {
		return list.size();
	}
}
