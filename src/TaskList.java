import java.util.ArrayList;

/**
 * Class to store a list of all Tasks that the user wants to keep track of
 * @author Joyce
 *
 */
public class TaskList {
	private ArrayList<Task> list;
	
	public void sortByDate() {
		//TODO
	}
	
	public void addToList(Task t) {
		list.add(t);
	}
	
	public Task getListItem(int i) {
		return list.get(i);
	}

	public ArrayList<Task> getList() {
		return list;
	}

	public void setList(ArrayList<Task> list) {
		this.list = list;
	}
}
